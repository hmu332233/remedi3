package kr.co.jbnu.remedi.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.Utils.ProgressBarDialog;
import kr.co.jbnu.remedi.models.User;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionManager;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionService;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board);


        Uri imageUri = getIntent().getData();

        ImageView iv_medicine_image = (ImageView)findViewById(R.id.iv_medicine_image);
        iv_medicine_image.setImageURI(imageUri);


        et_question = (EditText)findViewById(R.id.et_question_content);
        btn_ok = (Button)findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarDialog = new ProgressBarDialog(WriteBoardActivity.this);
                progressBarDialog.show();
                register_board();
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
        User user = new User("rupitere@naver.com","고석현","normal");
        User.setUser(user);

        Call<Boolean> registerboard = serverConnectionService.register_board(User.getInstance().getEmail(),"dummy_img_name",et_question.getText().toString());

        registerboard.enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                isConnectionOk = response.body();
                System.out.println("존재 하는 값 확인"+isConnectionOk.toString());
                if(isConnectionOk==true){
                    progressBarDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "게시물 등록 완료", Toast.LENGTH_SHORT).show();
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


}
