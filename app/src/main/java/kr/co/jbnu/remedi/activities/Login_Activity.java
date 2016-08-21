package kr.co.jbnu.remedi.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.models.User;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionManager;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Activity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    EditText idText;
    EditText pwText;
    Button login_btn;
    TextView result_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        initComponent();
        initEvent();


    }






    private void initComponent(){
        idText = (EditText) findViewById(R.id.id_text);
        pwText = (EditText) findViewById(R.id.pw_text);
        login_btn = (Button) findViewById(R.id.login_btn);
        result_textview = (TextView) findViewById(R.id.result_text);
    }

    private void initEvent(){

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("로그인 시도");

                ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
                ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();

                Call<User> user_data = serverConnectionService.login_request(idText.getText().toString(),pwText.getText().toString());

                    user_data.enqueue(new Callback<User>() {

                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();
                            if(user==null) result_textview.setText("아이디 혹은 패스워드 오류");
                            else result_textview.setText(user.getName());

                        }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        //final TextView textView = (TextView) findViewById(R.id.textView);
                        //textView.setText("Something went wrong: " + t.getMessage());
                        Log.w("서버 통신 실패",t.getMessage());
                    }
                });


            }
        });
    }





}
