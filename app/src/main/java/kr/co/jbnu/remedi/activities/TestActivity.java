package kr.co.jbnu.remedi.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.models.Answer;
import kr.co.jbnu.remedi.models.Board;
import kr.co.jbnu.remedi.models.Reply;
import kr.co.jbnu.remedi.models.User;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionManager;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    EditText editText;
    private Boolean isConnectionOk = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button btn = (Button)findViewById(R.id.testbtn);
        editText = (EditText)findViewById(R.id.reply_content);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_reply();
            }
        });
    }


    private void register_reply(){
        System.out.println("댓글 등록 요청");

        ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
        ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();

        //user 임시 세팅
        User user = new User("rupitere@naver.com","고석현","normal");
        User.setUser(user);


    }

    private void get_normaluser_boardlist(){
        System.out.println("보드 데이터 요청");

        ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
        ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();

        //user 임시 세팅
        User user = new User("rupitere@naver.com","고석현","normal");
        User.setUser(user);

        final Call<ArrayList<Board>> board = serverConnectionService.get_normaluser_board(User.getInstance().getEmail());
        board.enqueue(new Callback<ArrayList<Board>>() {

            @Override
            public void onResponse(Call<ArrayList<Board>> call, Response<ArrayList<Board>> response) {

                ArrayList<Board> boardlist = response.body();
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
