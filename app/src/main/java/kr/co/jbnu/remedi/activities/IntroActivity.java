package kr.co.jbnu.remedi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.Utils.ProgressBarDialog;
import kr.co.jbnu.remedi.Utils.SharePreferenceUtil;
import kr.co.jbnu.remedi.models.Answer;
import kr.co.jbnu.remedi.models.Board;
import kr.co.jbnu.remedi.models.Reply;
import kr.co.jbnu.remedi.models.User;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionManager;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sec on 2016-08-21.
 */
public class IntroActivity extends AppCompatActivity {

    private ImageView no_join_btn;
    private ImageView join_btn;
    private ImageView already_join_btn;
    private ProgressBarDialog progressBarDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        initComponent();
        initEvent();

        SharePreferenceUtil shareutil = SharePreferenceUtil.getInstance();
        User user = shareutil.getUser(getApplicationContext());
        if(user!=null){
            System.out.println("유저 데이터 요청"+user.getName());
            User.setUser(user);

            if(User.getInstance().getUser_type().equals("normal")){
                progressBarDialog.show();
                get_normaluser_boardlist();
            }else{
                progressBarDialog.show();
                get_pharmacist_boardlist();
            }

        }
    }

    private void initComponent(){
        no_join_btn = (ImageView) findViewById(R.id.intro_no_Join_btn);
        join_btn = (ImageView) findViewById(R.id.intro_join_btn);
        already_join_btn = (ImageView) findViewById(R.id.intro_already_btn);
        progressBarDialog = new ProgressBarDialog(IntroActivity.this);
    }

    private void initEvent(){

        no_join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBarDialog = new ProgressBarDialog(Login_Activity.this);
                //progressBarDialog.show();

            }
        });
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBarDialog = new ProgressBarDialog(Login_Activity.this);
                //progressBarDialog.show();
                Intent intent = new Intent(IntroActivity.this, Join_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        already_join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBarDialog = new ProgressBarDialog(Login_Activity.this);
                //progressBarDialog.show();
                Intent intent = new Intent(IntroActivity.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });

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
                if(boardlist!=null){
                    for(int i=0;i<boardlist.size();i++){
                        System.out.println("board 아이디 : "+boardlist.get(i).getId());
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

                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
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

                if(boardlist!=null){
                    for(int i=0;i<boardlist.size();i++){
                        System.out.println("board 아이디 : "+boardlist.get(i).getId());
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

                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

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
