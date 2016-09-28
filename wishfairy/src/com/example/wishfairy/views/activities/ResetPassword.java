package com.example.wishfairy.views.activities;


import java.io.IOException;

import com.example.wishfairy.R;
import com.example.wishfairy.database.DatabaseAdapter;

import android.os.Bundle;
import android.view.View.OnClickListener;
import android.app.Activity;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.content.Intent;

public class ResetPassword extends Activity {
	TextView securityQuestionTextView;
	Button fetchQuestionButton, resetPasswordButton;
	EditText emailIDEditText, securityAnswerEditText, passwordEditText, confirmPasswordEditText;
	String Username, securityQuestion, securityAnswer;
	private DatabaseAdapter db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_reset_password);
		db = new DatabaseAdapter(getApplicationContext());

	     try {
	        	db.createDataBase();
	     } catch (IOException ioe) {
		 		throw new Error("Unable to create database");
		 }
		db.openDataBase();

		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		fetchQuestionButton = (Button) findViewById(R.id.button1);
		resetPasswordButton = (Button) findViewById(R.id.button2);
		emailIDEditText = (EditText) findViewById(R.id.emailID_ResetPassword);
		securityAnswerEditText = (EditText) findViewById(R.id.securityAnswer_ResetPassword);
		passwordEditText = (EditText) findViewById(R.id.password_ResetPassword);
		confirmPasswordEditText = (EditText) findViewById(R.id.confirmPassword_ResetPassword);

		fetchQuestionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Username = emailIDEditText.getText().toString();
				if(Username.equals(""))
				{
					Toast.makeText(getBaseContext(),"Enter email ID!", Toast.LENGTH_SHORT).show();
					return;
				}
				securityQuestion = db.returnQuestion(Username);
				securityAnswer  = db.returnAnswer(Username);
				if(securityQuestion.equals(""))
				{
					Toast.makeText(getBaseContext(),"Enter valid email ID", Toast.LENGTH_SHORT).show();
					return;
				}
				LinearLayout ll = (LinearLayout) findViewById(R.id.passwordResetLinearLayout);
				ll.setVisibility(View.VISIBLE);
				securityQuestionTextView  = (TextView)findViewById(R.id.securityQuestion_ResetPassword);
				securityQuestionTextView.setText("Security Question: "+ securityQuestion);
			}
		});
		resetPasswordButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				// TODO Auto-generated method stub
				/*
				 * Read the answer, display a dialog to read passwords and update the password 
				 */
				if(!securityAnswer.equals(securityAnswerEditText.getText().toString()))
					{
					Toast.makeText(getBaseContext(),"Answer doesnt match!", Toast.LENGTH_SHORT).show();
					return;
					}
				String password, confirmPassword;
				password = passwordEditText.getText().toString();
				confirmPassword = confirmPasswordEditText.getText().toString();
				if(password.equals(""))
				{
					Toast.makeText(getBaseContext(),"Enter new password", Toast.LENGTH_SHORT).show();
					return;
				}
				if(confirmPassword.equals(""))
				{
					Toast.makeText(getBaseContext(),"Enter confirm password", Toast.LENGTH_SHORT).show();
					return;
				}
				if(password.equals(confirmPassword) && password.length() > 3)
				{
					DatabaseAdapter db = new DatabaseAdapter(getBaseContext());
					db.open();
					int affectRows = db.updatePassword(Username, password);
					if(affectRows == 1)
					{
						Toast.makeText(getBaseContext(), "Password Updated Successfully", Toast.LENGTH_SHORT).show();
					}
					db.close();
					emailIDEditText.setText("");
					passwordEditText.setText("");
					securityQuestionTextView.setText("Security Question: ");
					securityAnswerEditText.setText("");
					confirmPasswordEditText.setText("");
					startActivity(new Intent(ResetPassword.this,HomePage.class));
					
				}
				else if(password.equals(confirmPassword) && password.length() < 4)
				{
					Toast.makeText(ResetPassword.this, "Password Length too short!", Toast.LENGTH_SHORT).show();
				}
				
				else
				{
					Toast.makeText(ResetPassword.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
				}
				
				
//			   LayoutInflater li = LayoutInflater.from(getBaseContext());
//			   final View inflator = li.inflate(R.layout.passwordreset, null);
//		       final AlertDialog.Builder alert = new AlertDialog.Builder(getBaseContext()); 
//		       
//		        alert.setTitle("Tilte"); 
//		        alert.setMessage("Message"); 
//		        alert.setView(inflator); 
//		        
//		        final EditText password = (EditText) findViewById(R.id.passwordDialog);
//		        final EditText confirmPassword = (EditText) findViewById(R.id.confirmPasswordDialog);
//		       alert.show(); 
			}
		});
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		db.close();
		super.onDestroy();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reset_password, menu);
		return true;
	}

}
