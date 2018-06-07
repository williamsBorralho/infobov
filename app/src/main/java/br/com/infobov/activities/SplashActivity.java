package br.com.infobov.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import br.com.infobov.activities.ibovmobile.R;
import br.com.infobov.network.NetworkHelper;

public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_splash);

        if (!NetworkHelper.isConnect(this)) {
            Toast.makeText(this, "Desculpe, mas parece que seu smartphone esta desconectado da internet!", Toast.LENGTH_LONG).show();
            ;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
//                MTools.vibra(Splash.this);
                }
            }, 3000);

            return;
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loginFinal();
                    //                MTools.vibra(Splash.this);
                }
            }, SPLASH_DISPLAY_LENGTH);

        }

    }


    private void loginFinal() {
        Intent maindItent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(maindItent);
        SplashActivity.this.finish();
    }
}
