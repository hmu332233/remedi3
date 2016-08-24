package kr.co.jbnu.remedi.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.util.TextUtils;

import java.io.IOException;
import java.util.ArrayList;

import kr.co.jbnu.remedi.R;
import kr.co.jbnu.remedi.Utils.ProgressBarDialog;
import kr.co.jbnu.remedi.gcm.PreferenceUtil;
import kr.co.jbnu.remedi.models.Answer;
import kr.co.jbnu.remedi.models.Board;
import kr.co.jbnu.remedi.models.Reply;
import kr.co.jbnu.remedi.models.User;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionManager;
import kr.co.jbnu.remedi.serverIDO.ServerConnectionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Activity extends AppCompatActivity {

    EditText idText;
    EditText pwText;
    Button login_btn;
    private ProgressBarDialog progressBarDialog;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String SENDER_ID = "701682787336";

    private GoogleCloudMessaging _gcm;
    private String registration_id = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        initComponent();
        initEvent();


    }


    private void initComponent(){
        idText = (EditText) findViewById(R.id.login_email_input);
        pwText = (EditText) findViewById(R.id.login_password_input);
        login_btn = (Button) findViewById(R.id.sign_in_button);

    }

    private void initEvent(){

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarDialog = new ProgressBarDialog(Login_Activity.this);
                progressBarDialog.show();
                loginRequset();

            }
        });
    }

    private void loginRequset(){
        System.out.println("로그인 시도");
        ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
        ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();

        Call<User> user_data = serverConnectionService.login_request(idText.getText().toString(),pwText.getText().toString());

        user_data.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                User.setUser(user);

                if(user==null) {
                    Toast.makeText(getApplicationContext(), "아이디 혹은 패스워드 오류", Toast.LENGTH_SHORT).show();
                    progressBarDialog.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(), "로그인 성공"+user.getEmail(), Toast.LENGTH_SHORT).show();


                    if(user.getRegister_id()==null){ // 웹에서 회원가입을 한경우
                            if(getRegistrationId()!=null) { // 현재 레지스트레이션 아이디가 존재한다면 서버에 저장한다.
                                registration_id = getRegistrationId();
                                User.getInstance().setRegister_id(registration_id);
                                updateRegisterId();
                            }else{ // 아에 없는 경우임으로 새로 발급한다.
                                getRegisterIdFromServer();
                            }
                    }else{
                            if(getRegistrationId()!=null){
                                if(getRegistrationId().equals(User.getInstance().getRegister_id())==false){ // 레지스트아이디가 있는데 현재 아이디와 서버의 아이디가 다르면 현재 있는 것을 서버에 저장한다.
                                    registration_id = getRegistrationId();
                                    updateRegisterId();
                                }else{// 기존 사용자임으로 그냥 진행하면 된다.

                                    if(User.getInstance().getUser_type().equals("normal")) get_normaluser_boardlist();
                                    else get_pharmacist_boardlist();


                                }
                            }else{ //웹에서 가져온 레지스트 레이션을 preferecne에 저장하고 진행한다.
                                storeRegistrationId(User.getInstance().getRegister_id());
                                if(User.getInstance().getUser_type().equals("normal")) get_normaluser_boardlist();
                                else get_pharmacist_boardlist();
                            }
                    }
                }


            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //final TextView textView = (TextView) findViewById(R.id.textView);
                //textView.setText("Something went wrong: " + t.getMessage());
                progressBarDialog.dismiss();
                Log.w("서버 통신 실패",t.getMessage());
            }
        });
    }

    private void updateRegisterId(){
        System.out.println("reg키 업데이트 시키기");

        ServerConnectionManager serverConnectionManager = ServerConnectionManager.getInstance();
        ServerConnectionService serverConnectionService = serverConnectionManager.getServerConnection();

        System.out.println(registration_id+"id 확인");
        Call<Void> update_registerid =  serverConnectionService.update_gcm_register_id(User.getInstance().getEmail(),registration_id);


        update_registerid.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //is_exist = response.body();
                System.out.println("존재 하는 값 ");
                Toast.makeText(getApplicationContext(), "register업데이트 완료", Toast.LENGTH_SHORT).show();
                if(User.getInstance().getUser_type().equals("normal")) get_normaluser_boardlist();
                else get_pharmacist_boardlist();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //final TextView textView = (TextView) findViewById(R.id.textView);
                //textView.setText("Something went wrong: " + t.getMessage());
                Log.w("서버 통신 실패",t.getMessage());
                progressBarDialog.dismiss();
            }
        });
    }

    private void getRegisterIdFromServer(){
        if (checkPlayServices())
        {
            _gcm = GoogleCloudMessaging.getInstance(this);
             registerInBackground();
        }
        else
        {
            System.out.println("구글 서비스를 이용 할 수 없습니다");
        }
    }




    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        // display received msg
        String msg = intent.getStringExtra("msg");

        if (!TextUtils.isEmpty(msg));

    }

    // google play service가 사용가능한가
    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {

                finish();
            }
            return false;
        }
        return true;
    }

    // registration  id를 가져온다.
    private String getRegistrationId()
    {
        String registrationId = PreferenceUtil.instance(getApplicationContext()).regId();
        if (TextUtils.isEmpty(registrationId))
        {

            return null;
        }
        int registeredVersion = PreferenceUtil.instance(getApplicationContext()).appVersion();
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion)
        {

            return null;
        }
        return registrationId;
    }

    // app version을 가져온다. 뭐에 쓰는건지는 모르겠다.
    private int getAppVersion()
    {
        try
        {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    // gcm 서버에 접속해서 registration id를 발급받는다.
    private void registerInBackground()
    {
        new AsyncTask<Void, Void, String>()
        {

            @Override
            protected String doInBackground(Void... params)
            {
                String reg_id = "";
                String msg = "";
                try
                {
                    if (_gcm == null)
                    {
                        _gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    reg_id = _gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + reg_id;
                    System.out.println("gcm async 안"+reg_id);
                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(reg_id);
                }
                catch (IOException ex)
                {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }

                return reg_id;
            }

            @Override
            protected void onPostExecute(String reg_id)
            {
                registration_id = reg_id;
                //여기에 발급당한후 해야할일
                User.getInstance().setRegister_id(registration_id);
                updateRegisterId();
            }
        }.execute(null, null, null);
    }

    // registraion id를 preference에 저장한다.
    private void storeRegistrationId(String regId)
    {
        int appVersion = getAppVersion();

        PreferenceUtil.instance(getApplicationContext()).putRedId(regId);
        PreferenceUtil.instance(getApplicationContext()).putAppVersion(appVersion);
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
                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                startActivity(intent);

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

                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                startActivity(intent);

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
