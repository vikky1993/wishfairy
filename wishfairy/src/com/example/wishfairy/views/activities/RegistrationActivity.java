package com.example.wishfairy.views.activities;

import com.example.wishfairy.R;
import com.example.wishfairy.database.DatabaseAdapter;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;
public class RegistrationActivity extends Activity {

	
	//static SQLiteDatabase db;
	//final static String TABLE_NAME = "USER_TABLE";
	//private static String DATABASE_CREATE_TABLE = "create table if not exists " + TABLE_NAME +"( username text primary key, password text not null)";
	//public static final String userNameSuggestionIntentMessage = "com.example.wishfairy.LoginActivity";
	private DatabaseAdapter db;
	
	EditText emailID, passwordText, confirmPassword, securityQuestion, securityAnswer;
	Button submitButton, clearButton; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		//DatabaseHelper helper= new DatabaseHelper(getBaseContext());
		db = new DatabaseAdapter(getBaseContext());
		db.open();
		//db = helper.getReadableDatabase();
		//db = openOrCreateDatabase("WishFairy", MODE_PRIVATE, null);
		//db.execSQL(DATABASE_CREATE_TABLE);
		
		init();
		
		
	}

	private void init() {
		// TODO Auto-generated method stub
		emailID = (EditText) findViewById(R.id.emailIDEditText);
		//Intent intent = getIntent();
		//String usernameSuggestion = intent.getStringExtra(LoginActivity.userNameSuggestionIntentMessage);
		//emailID.setText(usernameSuggestion);
		
		
		passwordText = (EditText) findViewById(R.id.passwordReg);
		confirmPassword = (EditText) findViewById(R.id.confirmPasswordReg);
		securityQuestion = (EditText) findViewById(R.id.securityQuestionReg);
		securityAnswer = (EditText) findViewById(R.id.securityAnswerReg);
		
		
		
		submitButton = (Button) findViewById(R.id.registerButton);
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String emailIDText = emailID.getText().toString();
				String passwordStr = passwordText.getText().toString();   
				String confirmStr = confirmPassword.getText().toString();
				String securityQuestionStr  = securityQuestion.getText().toString();
				String securityAnswerStr = securityAnswer.getText().toString();
				
				
				if(emailIDText.equals(""))
				{
					Toast.makeText(getBaseContext(), "Enter email ID", Toast.LENGTH_SHORT).show();
					return;
				}
				if(passwordStr.equals(""))
				{
					Toast.makeText(getBaseContext(), "Enter Password", Toast.LENGTH_SHORT).show();
					return;
				}
				if(confirmStr.equals(""))
				{
					Toast.makeText(getBaseContext(), "Confirm Password", Toast.LENGTH_SHORT).show();
					return;
				}
				if(securityQuestionStr.equals(""))
				{
					Toast.makeText(getBaseContext(), "Enter the security question", Toast.LENGTH_SHORT).show();
					return;
				}
				if(securityAnswerStr.equals(""))
				{
					Toast.makeText(getBaseContext(), "Enter the security answer", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!emailIDText.matches("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+"))
				{
					Toast.makeText(getBaseContext(), "Enter valid Email", Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					if(passwordStr.equals(confirmStr) && passwordStr.length() > 3 )
					{
						// insert to the db
						// move to login
//						ContentValues cd = new ContentValues();
//						cd.put("username", emailIDText);
//						cd.put("password", passwordStr);
						db.openDataBase();
						long flag =  db.createUser(emailIDText, passwordStr, securityQuestionStr, securityAnswerStr);
						db.close();
						if(flag == -1)
						{
							Toast.makeText(getBaseContext(), "User already existing", Toast.LENGTH_SHORT).show();
							return;
						}
//						try{
//							db.insertOrThrow(DatabaseHelper.TABLE_NAME, null, cd);
//						}
//						catch(Exception e)
//						{
//							Toast.makeText(getBaseContext(), "User already existing", Toast.LENGTH_SHORT).show();
//							return;
//						}
						Toast.makeText(getBaseContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
						db.close();
						//cd.clear();
						emailID.setText("");
						passwordText.setText("");
						confirmPassword.setText("");
						securityAnswer.setText("");
						securityQuestion.setText("");
						Intent i = new Intent(RegistrationActivity.this,HomePage.class);
						//i.putExtra(userNameSuggestionIntentMessage, emailIDText);
						//i.putExtra("id", emailIDText);
						startActivity(i); 
					}
					else if(!passwordStr.equals(confirmStr))
					{
						Toast.makeText(getBaseContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
						passwordText.setText("");
						confirmPassword.setText("");
						return;
					} else 
					{
						Toast.makeText(getBaseContext(), "Password length must be greater than 3", Toast.LENGTH_SHORT).show();
						passwordText.setText("");
						confirmPassword.setText("");
					}
					
				}
				
			}
		});
		
		clearButton = (Button) findViewById(R.id.clearButton);
		clearButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				emailID.setText("");
				passwordText.setText("");
				confirmPassword.setText("");
			}
		});
	}
	
	public void onBackPressed()
	{
		//Intent i = new Intent(HomePage.this,LoginActivity.class);
		//startActivity(i);
		finish();
		super.onBackPressed();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}

}
