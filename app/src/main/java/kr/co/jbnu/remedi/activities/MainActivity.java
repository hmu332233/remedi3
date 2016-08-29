package kr.co.jbnu.remedi.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.Utils.GlobalValue;
import kr.co.jbnu.remedi.Utils.MakeFileName;
import kr.co.jbnu.remedi.Utils.ProgressBarDialog;
import kr.co.jbnu.remedi.Utils.SharePreferenceUtil;
import kr.co.jbnu.remedi.adapters.BoardAdapter;
import kr.co.jbnu.remedi.adapters.ProfileMenuAdapter;
import kr.co.jbnu.remedi.models.Answer;
import kr.co.jbnu.remedi.models.Board;
import kr.co.jbnu.remedi.models.Medicine;
import kr.co.jbnu.remedi.models.Reply;
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

public class MainActivity extends AppCompatActivity {


    ListView boardListView;
    BoardAdapter boardAdapter;

    private ProgressBarDialog progressBarDialog;
    private Boolean isConnectionOk = false;
    private Boolean isprofile = false;

    private ListView lvNavList;
    private FrameLayout flContainer;
    private int pagenumber = GlobalValue.QUESTION_PAGENUMBER;

    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> navItems = new ArrayList<>();
        navItems.add("프로필 이미지 변경");
        navItems.add("아이디 패스워드 변경");
        navItems.add("로그아웃");

        TextView profile_name_textview = (TextView)findViewById(R.id.profile_name_textview_setting);
        TextView profile_email_textview = (TextView)findViewById(R.id.profile_email_textview_setting);

        profile_email_textview.setText(User.getInstance().getEmail());
        profile_name_textview.setText(User.getInstance().getName());

        de.hdodenhof.circleimageview.CircleImageView profileimageview = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.profile_imageview_setting);

        if(User.getInstance().getProfile_imgname()!=null) {
            System.out.println("세팅하는곳에 들어왔습니다");
            if(User.getInstance().getProfile_imgname()!=null)Picasso.with(getApplicationContext()).load(Uri.parse(User.getInstance().getprofileuri())).error(R.drawable.ic_nocover).into(profileimageview);
        }else{
            System.out.println("프로파일이 널이랍니다");
        }

        lvNavList = (ListView)findViewById(R.id.left_drawer);

        lvNavList.setAdapter(new ProfileMenuAdapter(this,R.layout.item_profile_menu,navItems));
        lvNavList.setOnItemClickListener(new DrawerItemClickListener());

        dlDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        dtToggle = new ActionBarDrawerToggle(this, dlDrawer,
                R.drawable.ic_drawer, R.string.open_drawer, R.string.close_drawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };
        dlDrawer.setDrawerListener(dtToggle);

        ImageButton settingimgbutton = (ImageButton)findViewById(R.id.setting_btn);
        settingimgbutton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521,PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        dlDrawer.openDrawer(Gravity.LEFT);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });


        progressBarDialog = new ProgressBarDialog(MainActivity.this);

        if(User.getInstance().getUserBoardList() == null)
        {
            User.getInstance().setUserBoardList(new ArrayList<Board>());
        }


        boardAdapter = new BoardAdapter(this,User.getInstance(),User.getInstance().getUserBoardList());
        boardListView = (ListView) findViewById(R.id.lv_board);
        boardListView.setAdapter(boardAdapter);



        final com.github.clans.fab.FloatingActionMenu fabmenu = (com.github.clans.fab.FloatingActionMenu)findViewById(R.id.menu);

        if(User.getInstance().getUser_type().equals("normal")){
            com.github.clans.fab.FloatingActionButton btn_pick_gallery = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.btn_pick_gallery);
            btn_pick_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getImageFromGallery();
                    fabmenu.close(true);
                }
            });
            btn_pick_gallery.setLabelTextColor(Color.BLACK);
            com.github.clans.fab.FloatingActionButton btn_take_picture = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.btn_take_picture);
            btn_take_picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getTakeCamera();
                }
            });
            btn_take_picture.setLabelTextColor(Color.BLACK);
        }else{

            com.github.clans.fab.FloatingActionButton btn_pick_gallery = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.btn_pick_gallery);
            btn_pick_gallery.setImageResource(R.drawable.answer);
            btn_pick_gallery.setLabelText("답글 리스트 확인");
            btn_pick_gallery.setLabelTextColor(Color.BLACK);
            btn_pick_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBarDialog.show();
                    pagenumber = GlobalValue.ANSWER_PAGENUMBER;
                    get_pharmacist_answerlist();
                    fabmenu.close(true);

                }
            });



            com.github.clans.fab.FloatingActionButton btn_take_picture = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.btn_take_picture);
            btn_take_picture.setImageResource(R.drawable.question);
            btn_take_picture.setLabelText("질문 리스트 확인");
            btn_take_picture.setLabelTextColor(Color.BLACK);
            btn_take_picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBarDialog.show();
                    pagenumber = GlobalValue.QUESTION_PAGENUMBER;
                    get_pharmacist_boardlist();
                    fabmenu.close(true);
                }
            });
            //ViewGroup layout = (ViewGroup) btn_take_picture.getParent();

            //if(layout!=null) layout.removeView(btn_take_picture);

        }

        ImageButton refereshbtn = (ImageButton)findViewById(R.id.refresh_btn);
        refereshbtn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        progressBarDialog.show();

                        if(User.getInstance().getUser_type().equals("normal")){
                            get_normaluser_boardlist();
                        }else{
                            if(pagenumber==GlobalValue.QUESTION_PAGENUMBER){
                                get_pharmacist_boardlist();
                            }else{
                                get_pharmacist_answerlist();
                            }

                        }

                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });






/*
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Medicine_Parser medicine_Parser = new Medicine_Parser();

                ArrayList<Medicine> medicineList = medicine_Parser.requestMedicines("타이레놀");

                Medicine medicine = medicineList.get(0);

                medicine = medicine_Parser.requestMedicine_detail(medicine,medicine.getCodeNumber());

                medicine.print();
            }
        });*/
       // t.start();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {
            if (requestCode == GlobalValue.TAKE_CAMERA) {
                if (data != null) {
                    Log.e("Test", "result = " + data);

                    Uri imageURI = data.getData();

                    /*
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    if (thumbnail != null) {
                        Log.d("알림", "사진찍기 완료 완료");
                    }*/
                    System.out.println("사진찍기 데이터 : "+data.getExtras().get("data").toString());

                    Intent intent = new Intent(this,WriteBoardActivity.class);
                    intent.setData(imageURI);

                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageURI));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    bitmap = scaleDownBitmap(bitmap,100,getApplicationContext());

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();



                    intent.putExtra("data",bytes);
                    intent.putExtra("type","camera");
                    startActivityForResult(intent,321);
                }
            }


            if(requestCode == GlobalValue.GET_MEDICINE)
            {

                Log.d("알림","그거테스트입니다");
                setAnswer(data.getIntExtra("index",0),(Medicine)data.getSerializableExtra("medicine"));
            }


            if( requestCode == GlobalValue.PICK_FROM_GALLERY && isprofile == false)
            {
                Uri imageURI = data.getData() ;
                Log.d("알림", "이미지 가져오기 완료");
                //System.out.println("갤러리 데이터 : "+data.getExtras().get("data").toString());
                Intent intent = new Intent(this,WriteBoardActivity.class);
                System.out.println("이미지 유알아이 세팅");
                intent.setData(imageURI);
                System.out.println("비트맵세팅");
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageURI));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                bitmap = scaleDownBitmap(bitmap,100,getApplicationContext());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();



                intent.putExtra("data",bytes);
                intent.putExtra("type","gallery");

                System.out.println("데이터는 모든 처리가 되었습니다");
                startActivityForResult(intent,321);
            }else if(requestCode == GlobalValue.PICK_FROM_GALLERY && isprofile == true){
                Uri imageURI = data.getData() ;
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageURI));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                bitmap = scaleDownBitmap(bitmap,100,getApplicationContext());
                File imagefile = saveBitmap(bitmap);

                System.out.println("프로파일 이미지 저장입니다.");
                uploadFile(imagefile);
                de.hdodenhof.circleimageview.CircleImageView profileimageview = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.profile_imageview_setting);
                profileimageview.setImageURI(imageURI);

            }

            if( requestCode == 321 )
            {

                User.getInstance().getUserBoardList().add((Board)data.getSerializableExtra("board"));
                boardAdapter.notifyDataSetChanged();
            }
        }




    }


    private void setAnswer(final int index, final Medicine medicine)
    {
        Log.d("보드인덱스",index+"");

        View view = getViewByPosition(index,boardListView);
        final Board board = (Board)boardListView.getAdapter().getItem(index);

        TextView tv_medicine_name = (TextView) view.findViewById(R.id.tv_medicine_name);
        Log.d("보드인덱스",medicine.getName());
        tv_medicine_name.setText(medicine.getName());

        final EditText et_answer = (EditText)view.findViewById(R.id.et_answer_content);

        Button btn_add_answer = (Button) view.findViewById(R.id.btn_add_answer);

        btn_add_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBarDialog.show();



                Answer answer = new Answer(medicine.getName(),et_answer.getText().toString(),medicine.getShape(),
                        medicine.getEnterprise(),medicine.getStandardCode(),medicine.getCategory(),medicine.getEffect(),"","");
                System.out.println("medi category 확인 : "+answer.getMedi_category());


                register_answer(board,answer,index);


            }
        });


    }

    public void getImageFromGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GlobalValue.PICK_FROM_GALLERY);
    }

    private void getTakeCamera()
    {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, GlobalValue.TAKE_CAMERA);
    }

    private void register_answer(final Board board, Answer answer,final int index){
        System.out.println("답변 등록 요청");

        ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
        ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();


        Call<Answer> registeranswer = serverConnectionService.register_answer(board.getId(),answer.getMedi_name(),answer.getAnswer_content(),answer.getMedi_element()
        ,answer.getMedi_company(),answer.getMedi_serialnumber(), answer.getMedi_category(),answer.getMedi_effect(),User.getInstance().getEmail(),User.getInstance().getName());

        registeranswer.enqueue(new Callback<Answer>() {

            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {

                //System.out.println("존재 하는 값 확인"+isConnectionOk.toString());

                    progressBarDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "답변 등록 완료", Toast.LENGTH_SHORT).show();
                    Answer answer = response.body();
                    board.setAnswer(answer);

                    User.getInstance().getUserBoardList().set(index,board);
                    boardAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                //final TextView textView = (TextView) findViewById(R.id.textView);
                //textView.setText("Something went wrong: " + t.getMessage());
                progressBarDialog.dismiss();
                Log.w("서버 통신 실패",t.getMessage());
            }
        });
    }



    public Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private void get_pharmacist_boardlist(){
        System.out.println("약사 보드 데이터 요청");

        ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
        ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();

        final Call<ArrayList<Board>> board = serverConnectionService.get_pharmacist_normalboard(User.getInstance().getEmail());
        board.enqueue(new Callback<ArrayList<Board>>() {

            @Override
            public void onResponse(Call<ArrayList<Board>> call, Response<ArrayList<Board>> response) {

                ArrayList<Board> boardlist = response.body();
                User.getInstance().setUserBoardList(boardlist);
                progressBarDialog.dismiss();
                boardchangeNotify();

                if(boardlist!=null){
                    for(int i=0;i<boardlist.size();i++){
                        System.out.println("board 아이디 : "+boardlist.get(i).getId());
                        if(boardlist.get(i).getUpdated_at()!=null) System.out.println("날짜 : "+boardlist.get(i).getUpdated_at().getTime());
                        if(boardlist.get(i).getAnswer()!=null){
                            Answer answer = boardlist.get(i).getAnswer();
                            System.out.println("답글 내용"+answer.getMedi_name()+answer.getMedi_company()+answer.getMedi_effect());
                            ArrayList<Reply> replies = answer.getRepliesList();
                            if(replies!=null){
                                for(int j=0;j<replies.size();j++)
                                    System.out.println("댓글 내용 : "+replies.get(j).getContent());
                            }
                        }
                    }
                }

            }
            @Override
            public void onFailure(Call<ArrayList<Board>> call, Throwable t) {
                //final TextView textView = (TextView) findViewById(R.id.textView);
                //textView.setText("Something went wrong: " + t.getMessage());
                //progressBarDialog.dismiss();
                Log.w("서버 통신 실패",t.getMessage());
            }
        });
    }

    private void get_pharmacist_answerlist(){
        System.out.println("약사 답글 데이터 요청");

        ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
        ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();

        final Call<ArrayList<Board>> board = serverConnectionService.get_pharmacist_writtenboard(User.getInstance().getEmail());
        board.enqueue(new Callback<ArrayList<Board>>() {

            @Override
            public void onResponse(Call<ArrayList<Board>> call, Response<ArrayList<Board>> response) {

                ArrayList<Board> boardlist = response.body();
                User.getInstance().setUserBoardList(boardlist);
                boardchangeNotify();



                if(boardlist!=null){
                    for(int i=0;i<boardlist.size();i++){
                        System.out.println("board 아이디 : "+boardlist.get(i).getId());
                        if(boardlist.get(i).getUpdated_at()!=null) System.out.println("날짜 : "+boardlist.get(i).getUpdated_at().getTime());
                        if(boardlist.get(i).getAnswer()!=null){
                            Answer answer = boardlist.get(i).getAnswer();
                            System.out.println("답글 내용"+answer.getMedi_name()+answer.getMedi_company()+answer.getMedi_effect());
                            ArrayList<Reply> replies = answer.getRepliesList();
                            if(replies!=null){
                                for(int j=0;j<replies.size();j++)
                                    System.out.println("댓글 내용 : "+replies.get(j).getContent());
                            }
                        }
                    }
                }



            }
            @Override
            public void onFailure(Call<ArrayList<Board>> call, Throwable t) {
                //final TextView textView = (TextView) findViewById(R.id.textView);
                //textView.setText("Something went wrong: " + t.getMessage());
                //progressBarDialog.dismiss();
                Log.w("서버 통신 실패",t.getMessage());
            }
        });
    }



    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position,
                                long id) {
            switch(position){
                case 0: // 프로필 이미지 변경
                    isprofile = true;
                    getImageFromGallery();
                    //isprofile = false;
                    break;
                case 1: // 아이디 패스워드 변경
                    break;

                case 2: // 로그아웃
                    SharePreferenceUtil sharePreferenceUtil = SharePreferenceUtil.getInstance();
                    sharePreferenceUtil.deleteUser(getApplicationContext());
                    Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            dlDrawer.closeDrawer(Gravity.LEFT);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(dtToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    private void boardchangeNotify(){
        final Handler handler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                // 원래 하고싶었던 일들 (UI변경작업 등...)
                boardAdapter.clear();
                boardAdapter.addAll(User.getInstance().getUserBoardList());
                boardAdapter.notifyDataSetChanged();
                progressBarDialog.dismiss();
            }
        };

        new Thread()
        {

            public void run()
            {
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }

        }.start();
    }

    private void uploadFile(File imagefile) {
        progressBarDialog.show();
        // create upload service client
        System.out.println("이미지 업로드 요청");
        String imagefilename = null;
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

        final String serverimgname = imagefilename;
        // finally, execute the request
        Call<ResponseBody> call = service.upload(img_name, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
                deleteBitmap();
                changeProfile(serverimgname);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
                deleteBitmap();
            }
        });
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

    private void changeProfile(final String imgname){

        System.out.println("프로파일 변경 요청");

        ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
        ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();

        final Call<Void> service = serverConnectionService.change_profile_img(User.getInstance().getEmail(),imgname);
        service.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("성공");
                //progressBarDialog.dismiss();
                SharePreferenceUtil shareutil = SharePreferenceUtil.getInstance();
                User user = shareutil.getUser(getApplicationContext());
                user.setProfile_imgname(imgname);
                User.getInstance().setProfile_imgname(imgname);
                shareutil.storeUser(user,getApplicationContext());
                isprofile = false;

                if(User.getInstance().getUser_type().equals("normal")){
                    get_normaluser_boardlist();
                }else{
                    if(pagenumber==GlobalValue.QUESTION_PAGENUMBER){
                        get_pharmacist_boardlist();
                    }else{
                        get_pharmacist_answerlist();
                    }
                }

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //final TextView textView = (TextView) findViewById(R.id.textView);
                //textView.setText("Something went wrong: " + t.getMessage());
                //progressBarDialog.dismiss();
                isprofile = false;
                Log.w("서버 통신 실패",t.getMessage());
            }
        });
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

    private void get_normaluser_boardlist(){
        System.out.println("일반 유저 보드 데이터 요청");

        ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
        ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();



        final Call<ArrayList<Board>> board = serverConnectionService.get_normaluser_board(User.getInstance().getEmail());
        board.enqueue(new Callback<ArrayList<Board>>() {

            @Override
            public void onResponse(Call<ArrayList<Board>> call, Response<ArrayList<Board>> response) {

                ArrayList<Board> boardlist = response.body();
                User.getInstance().setUserBoardList(boardlist);
                progressBarDialog.dismiss();
                for(int i=0;i<boardlist.size();i++){
                    System.out.println("board 아이디 : "+boardlist.get(i).getId());
                    if(boardlist.get(i).getUpdated_at()!=null) System.out.println("날짜 : "+boardlist.get(i).getUpdated_at().getTime());
                    if(boardlist.get(i).getAnswer()!=null){
                        Answer answer = boardlist.get(i).getAnswer();
                        System.out.println("답글 내용"+answer.getMedi_name()+answer.getMedi_company()+answer.getMedi_effect());
                        ArrayList<Reply> replies = answer.getRepliesList();
                        if(replies!=null){
                            for(int j=0;j<replies.size();j++)
                                System.out.println("댓글 내용 : "+replies.get(j).getContent());
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Board>> call, Throwable t) {
                //final TextView textView = (TextView) findViewById(R.id.textView);
                //textView.setText("Something went wrong: " + t.getMessage());
                //progressBarDialog.dismiss();
                Log.w("서버 통신 실패",t.getMessage());
            }
        });
    }




}
