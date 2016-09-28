package com.example.samsung.wishfairy.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samsung.wishfairy.R;
import com.example.samsung.wishfairy.database.DBHelper;
import com.example.samsung.wishfairy.login.LoginActivity;

/**
 * Created by SAMSUNG on 29-07-2015.
 */
public class Registration extends ActionBarActivity {
    EditText name,password,cPassword,email,phone;
    TextView warning;
    String mName,mPassword,mConfirmPassword,mEmail,mPhone;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        name=(EditText)findViewById(R.id.userName);
        password=(EditText)findViewById(R.id.password);
        cPassword=(EditText)findViewById(R.id.cPassword);
        email=(EditText)findViewById(R.id.email);
        warning= (TextView) findViewById(R.id.warning);
        phone=(EditText)findViewById(R.id.phone);
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getText();
               validate();
            }
        });

    }
    public void getText(){
        mName=name.getText().toString();
        mPassword=password.getText().toString();
        mConfirmPassword=cPassword.getText().toString();
        mEmail=email.getText().toString();
        mPhone=phone.getText().toString();
    }
    public void addDetails(){
        RegistrationProcess process=new RegistrationProcess(name.getText().toString(),password.getText().toString(),email.getText().toString(),phone.getText().toString());
        DBHelper db=new DBHelper(this);
        db.addContact(process);
        final SharedPreferences preferences=getApplicationContext().getSharedPreferences("FirstTimeLogin", Context.MODE_PRIVATE);
        preferences.edit().putBoolean(name.getText().toString(),true).commit();
        Toast.makeText(getApplicationContext(),"Account Created Successfully",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent("android.intent.action.LOGIN");
        //LoginActivity.e1.setText(mName);
       // LoginActivity.e2.setText(mPassword);
        startActivity(intent);
        finish();
    }
    public void validate(){
        if(!mPassword.equals(mConfirmPassword)){
            input();
            reset(password);
            warning.setText("passwords do not match");
        }
        else if(mName.equals("")||(!mName.matches(".*[a-zA-Z].*"))){
            input();
            warning.setText("user name should contain a character");
            reset(name);
        }
        else if(!(mPassword.length()>4)){
            input();
            warning.setText("password should be more than 4 character or digits");
            reset(password);
        }
        else if(!(mEmail.matches(".*[a-zA-Z].*"))||!mEmail.contains("@")||mEmail.endsWith("@")){
            input();
            warning.setText("enter a valid email id,eg :abc@gmail.com");
            reset(email);
        }else if((mPhone.length()<10)||mPhone.length()>11){
            input();
            warning.setText("phone number is not valid ");
            reset(phone);
        }
        else {addDetails();}

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent("android.intent.action.LOGIN");
        startActivity(intent);
        finish();
    }

    public void reset(EditText editText){
       editText.setText("");

    }
    public void input(){
        Toast.makeText(getApplicationContext(),"please enter a valid input ",Toast.LENGTH_SHORT).show();
    }
}
