package com.example.samsung.wishfairy.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.samsung.wishfairy.R;
import com.example.samsung.wishfairy.login.LoginProcess;

/**
 * Created by SAMSUNG on 22-11-2015.
 */
public class SplashActivity extends Activity {
    ProgressBar secondBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_layout);
        secondBar = (ProgressBar)findViewById(R.id.secondBar);
        secondBar.setVisibility(View.VISIBLE);
        secondBar.setProgress(10);
        Thread t=new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
               // secondBar.setVisibility(View.VISIBLE);
               // secondBar.setProgress(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

           final SharedPreferences preferences=getApplicationContext().getSharedPreferences("LogInDetails", Context.MODE_PRIVATE);
            if(preferences.contains("UserName")){
                boolean flag=preferences.getBoolean("Flag",false);
                if(flag) {
                    Log.d("shared pref", "shared pref is active");
                    String userName = preferences.getString("UserName", "");
                    String password = preferences.getString("Password", "");
                    LoginProcess loginProcess = new LoginProcess();
                    loginProcess.validate(userName, getApplicationContext(), password);
                    LoginProcess.username = userName;
                    LoginProcess.password = password;
                    Intent i = new Intent("android.intent.action.HOMEACTIVITY");
                    startActivity(i);
                    finish();
                }
                else {
                    Intent intent=new Intent("android.intent.action.LOGIN");
                    startActivity(intent);
                    finish();
                }
            }
            else {
                Log.d("shared pref", "shared pref is not active");
                Intent intent=new Intent("android.intent.action.LOGIN");
                startActivity(intent);
                finish();
            }
        }
    });
    t.start();
}
}
