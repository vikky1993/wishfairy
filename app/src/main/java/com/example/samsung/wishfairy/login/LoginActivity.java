package com.example.samsung.wishfairy.login;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samsung.wishfairy.R;


public class LoginActivity extends ActionBarActivity {
    TextView t1,t2;
    Button signIn,signUp;
    CheckBox check;
    public static EditText e1,e2;
    private static final String PREF_NAME="SET";
    String name;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        t1=(TextView)findViewById(R.id.text);
        t2=(TextView)findViewById(R.id.t2);
        signIn=(Button)findViewById(R.id.signIn);
        signUp=(Button)findViewById(R.id.signup);
        check=(CheckBox)findViewById(R.id.checkbox);
        e1=(EditText)findViewById(R.id.userName);
        e2=(EditText)findViewById(R.id.password);
        e1.setText("");
        e2.setText("");//#91a3b0
        setTypeFace();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = e1.getText().toString();
                String password = e2.getText().toString();
                if (check.isChecked()) {
                    SharedPreferences settings = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

                    settings.edit().putString("password", password)

                            .putString("name", strName).commit();
                    //Toast.makeText(get,Toast.LENGTH_LONG).show();
                }
                LoginProcess loginProcess = new LoginProcess();
                if (loginProcess.validate(strName, getApplicationContext(), password)) {
                    Toast.makeText(getApplicationContext(), "You are succesfully Logged in", Toast.LENGTH_LONG).show();
                    Intent i = new Intent("android.intent.action.HOMEACTIVITY");
                    startActivity(i);
                    finish();
                } else {
                    e1.setText("");
                    e2.setText("");
                    Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_LONG).show();
                }


            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent("android.intent.action.REGISTRATION");
                startActivity(i);
                finish();
            }
        });
        e1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final SharedPreferences settings=getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                name=settings.getString("name","");
                password=settings.getString("password","");
                if(e1.getText().toString().equals(name)){
                    e2.setText(password);}
            }
        });


    }
    public void setTypeFace(){

        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Regular.ttf");

        t1.setTypeface(font,1);
        t2.setTypeface(font,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.exit){
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}
