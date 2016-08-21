package kr.co.jbnu.remedi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import kr.co.jbnu.remedi.R;

/**
 * Created by sec on 2016-08-21.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 1500);
    }

    class splashhandler implements Runnable {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
            startActivity(intent);

            finish();
        }
    }
}
