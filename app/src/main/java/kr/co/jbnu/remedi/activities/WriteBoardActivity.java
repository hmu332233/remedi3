package kr.co.jbnu.remedi.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.Utils.MakeFileName;
import kr.co.jbnu.remedi.Utils.ProgressBarDialog;
import kr.co.jbnu.remedi.models.Board;
import kr.co.jbnu.remedi.models.User;
import kr.co.jbnu.remedi.serverIDO.ImageServerConnectionManager;
import kr.co.jbnu.remedi.serverIDO.ImageServerConnectionService;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionManager;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sec on 2016-08-21.
 */
public class WriteBoardActivity extends AppCompatActivity {

    EditText et_question;
    Button btn_ok;
    private ProgressBarDialog progressBarDialog;
    private Boolean isConnectionOk = false;
    private Uri imageUri;
    private String imagefilename = null;
    private File imagefile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board);

        Intent intent = getIntent();

        imageUri = getIntent().getData();
        ImageView iv_medicine_image = (ImageView)findViewById(R.id.iv_medicine_image);
        iv_medicine_image.setImageURI(imageUri);
        Bitmap bitmap;

        System.out.println("비트맵을 가져오기전");

        byte[] bytes = getIntent().getByteArrayExtra("data");
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        //bitmap = (Bitmap) getIntent().getParcelableExtra("data");

        System.out.println("비트맵을 가져온후");

        System.out.println(imageUri.toString());


        imagefile = saveBitmap(bitmap);
        // bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        // iv_medicine_image.setImageURI(imageUri);
        iv_medicine_image.setImageBitmap(bitmap);



        et_question = (EditText)findViewById(R.id.et_question_content);
        btn_ok = (Button)findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarDialog = new ProgressBarDialog(WriteBoardActivity.this);
                progressBarDialog.show();
                uploadFile(imageUri);
                //Board board = new Board("",et_question.getText().toString(),null);

                //Intent intent = new Intent();
                //intent.putExtra("board",board);
                //setResult(RESULT_OK,intent);
                //finish();
            }
        });
    }

    private void register_board(){
        System.out.println("질문 등록 요청");

        ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
        ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();

        //user 임시 세팅
        User user = User.getInstance();

        

        Call<Boolean> registerboard = serverConnectionService.register_board(User.getInstance().getEmail(),imagefilename,et_question.getText().toString());

        registerboard.enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                isConnectionOk = response.body();
                System.out.println("존재 하는 값 확인"+isConnectionOk.toString());
                if(isConnectionOk==true){
                    progressBarDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "게시물 등록 완료", Toast.LENGTH_SHORT).show();
                    Board board = new Board(imagefilename,et_question.getText().toString(),null);

                    Intent intent = new Intent();
                    intent.putExtra("board",board);
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    progressBarDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "게시물 등록 오류", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                //final TextView textView = (TextView) findViewById(R.id.textView);
                //textView.setText("Something went wrong: " + t.getMessage());
                progressBarDialog.dismiss();
                Log.w("서버 통신 실패",t.getMessage());
            }
        });
    }

    private void uploadFile(Uri fileUri) {
        // create upload service client
        System.out.println("이미지 업로드 요청");
        ImageServerConnectionManager serverConnectionManager = ImageServerConnectionManager.getInstance();
        ImageServerConnectionService service = serverConnectionManager.getServerConnection();


        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = imagefile;

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // add another part within the multipart request

        imagefilename = file.getName();
        imagefilename = MakeFileName.getInstance().makefilename(imagefilename);

        RequestBody img_name =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), imagefilename);

        // finally, execute the request
        Call<ResponseBody> call = service.upload(img_name, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
                deleteBitmap();
                register_board();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
                deleteBitmap();
            }
        });
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public Bitmap resizeBitmapImageFn(
            Bitmap bmpSource, int maxResolution){
        int iWidth = bmpSource.getWidth();      //비트맵이미지의 넓이
        int iHeight = bmpSource.getHeight();     //비트맵이미지의 높이
        int newWidth = iWidth ;
        int newHeight = iHeight ;
        float rate = 0.0f;

        Log.d("알림",iWidth+"");
        Log.d("알림",iHeight+"");
        //이미지의 가로 세로 비율에 맞게 조절
        if(iWidth > iHeight ){
            if(maxResolution < iWidth ){
                rate = maxResolution / (float) iWidth ;
                newHeight = (int) (iHeight * rate);
                newWidth = maxResolution;
            }
        }else{
            if(maxResolution < iHeight ){
                rate = maxResolution / (float) iHeight ;
                newWidth = (int) (iWidth * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(
                bmpSource, newWidth, newHeight, true);
    }

    private File saveBitmap(Bitmap bitmap)
    {
        try{

            File file = new File("data/data/kr.co.jbnu.remedi/files/test.png");
            FileOutputStream fos = openFileOutput("test.png" , 0);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100 , fos);
            fos.flush();
            fos.close();

            Toast.makeText(this, "file ok", Toast.LENGTH_SHORT).show();

            return file;
        }catch(Exception e) { Toast.makeText(this, "file error", Toast.LENGTH_SHORT).show();}

        return null;
    }


    private void deleteBitmap()
    {
        try{
            File file = new File("data/data/kr.co.jbnu.remedi/files/");
            File[] flist = file.listFiles();
            Toast.makeText(getApplicationContext(), "imgcnt = " + flist.length, Toast.LENGTH_SHORT).show();
            for(int i = 0 ; i < flist.length ; i++)
            {
                String fname = flist[i].getName();
                if(fname.equals("test.png"))
                {
                    flist[i].delete();
                }
            }
        }catch(Exception e){Toast.makeText(getApplicationContext(), "파일 삭제 실패 ", Toast.LENGTH_SHORT).show();}
    }

}
