package com.example.wishfairy.views.activities;

import java.io.IOException;

import com.example.wishfairy.R;
import com.example.wishfairy.database.DatabaseAdapter;

import android.os.Bundle;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class LoginActivity extends Activity {

	//static SQLiteDatabase db;
	String Empty = "";
	private DatabaseAdapter db;
	
	//String userNameSuggestion=Empty;
	//public static final String userNameSuggestionIntentMessage = "com.example.wishfairy.LoginActivity";

	//final static String TABLE_NAME = "USER_TABLE";
	//private static String DATABASE_CREATE_TABLE = "create table if not exists " + TABLE_NAME +"( username text primary key, password text not null)";
	public static final String MY_PREFS = "loggedUser";
	Button LoginButton, RegisterButton;
	EditText UsernameText, PasswordText;
	TextView forgotPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
        db = new DatabaseAdapter(getApplicationContext());

	     try {
	        	db.createDataBase();
	     } catch (IOException ioe) {
		 		throw new Error("Unable to create database");
		 }
		db.openDataBase();
		//db = openOrCreateDatabase("WishFairy", MODE_PRIVATE, null);
		//db.execSQL(DATABASE_CREATE_TABLE);
		//db = helper.getReadableDatabase();
		
//		ContentValues cd = new ContentValues();
//		cd.put("username", "madhu1");
//		cd.put("password","4324");
//		db.insert(TABLE_NAME, null, cd);
	   //getActionBar().hide();
		init();
//		com.nox.android.widget.ActionBar actionBar = (com.nox.android.widget.ActionBar) findViewById(R.id.actionbar);
//getActionBar().setDisplayHomeAsUpEnabled(true);
//		getActionBar().getDisplayOptions()
//		actionBar.setHomeAction(new IntentAction(this, createIntent(this), R.drawable.ic_launcher));
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		db.close();
		super.onDestroy();
	}
	 public static Intent createIntent(Context context) {
	        Intent i = new Intent(context, HomePage.class);
	        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        return i;
	    }
	 
	 
	private void init() {
		// TODO Auto-generated method stub
		forgotPassword = (TextView) findViewById(R.id.forgotPassword);
		forgotPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(LoginActivity.this,ResetPassword.class);
				startActivity(i);
			}
		});
		
		
		LoginButton = (Button) findViewById(R.id.loginButton);
		LoginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//String Empty = "";
				String userName = UsernameText.getText().toString();
				if(UsernameText.getText().toString().equals(Empty) && PasswordText.getText().toString().equals(Empty))
				{
					Toast.makeText(getBaseContext(),"Enter username and Password!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(UsernameText.getText().toString().equals(Empty))
				{
					Toast.makeText(getBaseContext(),"Enter username!", Toast.LENGTH_SHORT).show();
					return;
				}
				if(PasswordText.getText().toString().equals(Empty))
				{
					Toast.makeText(getBaseContext(),"Enter password!", Toast.LENGTH_SHORT).show();
					return;
				}
//				Cursor c = db.query("USER_TABLE", new String[] {"password"}, "username=?", new String[] {userName} , null, null, null);
//				String passWord = Empty;
//				if(c.moveToFirst())
//				{
//					passWord = c.getString(0);
//				}
				String passWord = Empty;
				passWord = db.returnPassword(userName);
				
				if(passWord.equals(Empty))
				{
					Toast.makeText(getBaseContext(),"Invalid user, please register!", Toast.LENGTH_SHORT).show();
					
				}
				if(passWord.equals(PasswordText.getText().toString()))
				{
					//c.close();
					UsernameText.setText("");
					PasswordText.setText("");
					SharedPreferences myPrefs = getSharedPreferences(MY_PREFS,0);
					Editor e = myPrefs.edit();
					e.putString("username", userName);
					e.commit();
					Intent i = new Intent(LoginActivity.this,HomePage.class);
					startActivity(i); 
					finish();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
					
				}
			}
		});
		RegisterButton = (Button) findViewById(R.id.login_registerButton);
		RegisterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UsernameText.setText("");
				PasswordText.setText("");
				Intent i = new Intent(LoginActivity.this,RegistrationActivity.class);
				//userNameSuggestion = UsernameText.getText().toString();
				//i.putExtra(userNameSuggestionIntentMessage, userNameSuggestion);
				
				startActivity(i); 
			}
		});
		UsernameText = (EditText) findViewById(R.id.userNameEditText);
		PasswordText = (EditText) findViewById(R.id.passWordEditText); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
