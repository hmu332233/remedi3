package kr.co.jbnu.remedi.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.Utils.GlobalValue;
import kr.co.jbnu.remedi.Utils.ProgressBarDialog;
import kr.co.jbnu.remedi.adapters.BoardAdapter;
import kr.co.jbnu.remedi.models.Answer;
import kr.co.jbnu.remedi.models.Board;
import kr.co.jbnu.remedi.models.Medicine;
import kr.co.jbnu.remedi.models.User;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionManager;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    ListView boardListView;
    BoardAdapter boardAdapter;
    ArrayList<Board> boards;

    private ProgressBarDialog progressBarDialog;
    private Boolean isConnectionOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //asdfasd
        //asdfasdf
        boards = new ArrayList<>();
        Board b = new Board("","답변없는질문",null);
        Board c = new Board("","없없",null);
        Board d = new Board("","질문?",null);

        boards.add(b);
        boards.add(c);
        boards.add(d);

        //Board b2 = new Board("","답변이 달려있음",new Answer("","이건답변입니다","","","","","","",""));
        //boards.add(b2);

        User user = new User(1,"email","name", User.PHARM,"a",boards);
        boardAdapter = new BoardAdapter(this,user.getUser_type(),user.getUserBoardList());


        boardListView = (ListView) findViewById(R.id.lv_board);
        boardListView.setAdapter(boardAdapter);

        com.github.clans.fab.FloatingActionButton btn_pick_gallery = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.btn_pick_gallery);
        btn_pick_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromGallery();
            }
        });

        com.github.clans.fab.FloatingActionButton btn_take_picture = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.btn_take_picture);
        btn_take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTakeCamera();
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

                    Intent intent = new Intent(this,WriteBoardActivity.class);
                    intent.setData(imageURI);
                    startActivityForResult(intent,321);
                }
            }


            if(requestCode == GlobalValue.GET_MEDICINE)
            {

                Log.d("알림","그거테스트입니다");
                setAnswer(data.getIntExtra("index",0),(Medicine)data.getSerializableExtra("medicine"));
            }


            if( requestCode == GlobalValue.PICK_FROM_GALLERY)
            {
                Uri imageURI = data.getData() ;
                Log.d("알림", "이미지 가져오기 완료");

                Intent intent = new Intent(this,WriteBoardActivity.class);
                intent.setData(imageURI);
                startActivityForResult(intent,321);
            }

            if( requestCode == 321 )
            {

                boards.add((Board)data.getSerializableExtra("board"));
                boardAdapter.notifyDataSetChanged();
            }
        }




    }


    private void setAnswer(final int index, final Medicine medicine)
    {
        View view = boardListView.getChildAt(index);
        final Board board = (Board)boardListView.getAdapter().getItem(index);

        TextView tv_medicine_name = (TextView) view.findViewById(R.id.tv_medicine_name);
        tv_medicine_name.setText(medicine.getName());

        final EditText et_answer = (EditText)view.findViewById(R.id.et_answer_content);

        Button btn_add_answer = (Button) view.findViewById(R.id.btn_add_answer);

        btn_add_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBarDialog = new ProgressBarDialog(MainActivity.this);
                progressBarDialog.show();
                //user 임시 세팅
                User user = new User("rupitere@naver.com","고석현","normal");
                User.setUser(user);



                Answer answer = new Answer(medicine.getName(),et_answer.getText().toString(),medicine.getShape(),
                        medicine.getEnterprise(),medicine.getStandardCode(),medicine.getCategory(),medicine.getEffect(),"","");
                System.out.println("medi category 확인 : "+answer.getMedi_category());
                //board id 임시 세팅
                board.setId(4);
                board.setAnswer(answer);

                boards.set(index,board);

                register_answer(board.getId(),answer);

                boardAdapter.notifyDataSetChanged();
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

    private void register_answer(int board_id,Answer answer){
        System.out.println("질문 등록 요청");

        ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
        ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();

        //user 임시 세팅
        User user = new User("rupitere@naver.com","고석현","normal");
        User.setUser(user);

        Call<Boolean> registeranswer = serverConnectionService.register_answer(board_id,answer.getMedi_name(),answer.getAnswer_content(),answer.getMedi_element()
        ,answer.getMedi_company(),answer.getMedi_serialnumber(), answer.getMedi_category(),answer.getMedi_effect(),User.getInstance().getEmail());

        registeranswer.enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                isConnectionOk = response.body();
                System.out.println("존재 하는 값 확인"+isConnectionOk.toString());
                if(isConnectionOk==true){
                    progressBarDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "답변 등록 완료", Toast.LENGTH_SHORT).show();
                }else{
                    progressBarDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "답변 등록 오류", Toast.LENGTH_SHORT).show();
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
