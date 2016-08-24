package kr.co.jbnu.remedi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import kr.co.jbnu.remedi.R;

/**
 * Created by sec on 2016-08-21.
 */
public class IntroActivity extends AppCompatActivity {

    private ImageView no_join_btn;
    private ImageView join_btn;
    private ImageView already_join_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        initComponent();
        initEvent();
    }

    private void initComponent(){
        no_join_btn = (ImageView) findViewById(R.id.intro_no_Join_btn);
        join_btn = (ImageView) findViewById(R.id.intro_join_btn);
        already_join_btn = (ImageView) findViewById(R.id.intro_already_btn);

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
            }
        });
        already_join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBarDialog = new ProgressBarDialog(Login_Activity.this);
                //progressBarDialog.show();
                Intent intent = new Intent(IntroActivity.this, Login_Activity.class);
                startActivity(intent);
            }
        });

    }


}
