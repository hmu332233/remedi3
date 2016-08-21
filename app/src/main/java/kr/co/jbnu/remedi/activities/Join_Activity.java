package kr.co.jbnu.remedi.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionManager;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Join_Activity extends AppCompatActivity {

    EditText email_input;
    EditText password_input;
    EditText name_input;
    Button sign_up_btn;

    String type = "normal";
    private Boolean is_exist = false;
    Context context;

    /*
    * Intent로 일반 인지 아닌지 받아줘야함
     * 만약 화면을 바꿀경우 다른 엑티비티로 만들예정
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_);
        context = getApplicationContext();
        initComponent();
        initEvent();

    }

    private void initComponent(){
        email_input = (EditText)findViewById(R.id.email_input);
        password_input = (EditText)findViewById(R.id.password_input);
        name_input = (EditText)findViewById(R.id.name_input);
        sign_up_btn = (Button)findViewById(R.id.sign_up_button);
    }

    private void initEvent(){
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkExistUser();
            }
        });
    }

    private void joinUser(){
        System.out.println("일반 유저 회원가입 시도");

        ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
        ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();

        String email = email_input.getText().toString();
        String pw = password_input.getText().toString();
        String name = name_input.getText().toString();
        Call<Void> user_join =  serverConnectionService.user_join(email,pw,name,type,"123");


        user_join.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //is_exist = response.body();
                System.out.println("존재 하는 값 ");

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //final TextView textView = (TextView) findViewById(R.id.textView);
                //textView.setText("Something went wrong: " + t.getMessage());
                Log.w("서버 통신 실패",t.getMessage());
            }
        });
    }



    private void checkExistUser(){
        System.out.println("유저 존재 확인 시도");

        ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
        ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();

        Call<Boolean> is_exsist = serverConnectionService.check_exist_user(email_input.getText().toString());



        is_exsist.enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                is_exist = response.body();
                System.out.println("존재 하는 값 확인"+is_exist.toString());
                if(is_exist==true){
                    Toast.makeText(context, "존재하는 아이디 입니다 다른 아이디로 바꿔주세요", Toast.LENGTH_SHORT).show();
                }else{
                    joinUser();
                }

            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                //final TextView textView = (TextView) findViewById(R.id.textView);
                //textView.setText("Something went wrong: " + t.getMessage());
                Log.w("서버 통신 실패",t.getMessage());
            }
        });
    }


}
