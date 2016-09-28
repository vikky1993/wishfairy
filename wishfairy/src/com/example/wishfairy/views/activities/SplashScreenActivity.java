package com.example.wishfairy.views.activities;

import com.example.wishfairy.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;

public class SplashScreenActivity extends Activity {

	
	public static final String MY_PREFS = "loggedUser";
	private static int SPLASH_TIME_OUT = 3000;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		getActionBar().hide();
		new Handler().postDelayed(new Runnable() {
	
            @Override
            public void run() {
            	SharedPreferences myPrefs = getSharedPreferences(MY_PREFS, 0);
            	//Editor e = myPrefs.edit();
            	//e.putString("username", )
            	String username = myPrefs.getString("username", "");
            	if(username.equals(""))
            	{
            		 Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                     startActivity(i);
                     finish();
            	}
            	else
            	{
            		Intent i = new Intent(SplashScreenActivity.this, HomePage.class);
            		startActivity(i);
            		finish();
            	}
               
            }
        }, SPLASH_TIME_OUT);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

}
