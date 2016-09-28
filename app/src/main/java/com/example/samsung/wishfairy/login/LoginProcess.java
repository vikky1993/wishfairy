package com.example.samsung.wishfairy.login;

import android.content.Context;
import android.database.Cursor;

import com.example.samsung.wishfairy.database.DBHelper;

/**
 * Created by SAMSUNG on 19-07-2015.
 */
public class LoginProcess {
    public static String username;
    public static String password;
    public static Cursor userDetails;
    public LoginProcess(){

    }
    public boolean validate(String uname,Context context,String password){
        LoginProcess.username=uname;
        LoginProcess.password=password;
        DBHelper dbHelper=new DBHelper(context);
        boolean check=dbHelper.getPassword(uname,password);
        return check;
    }
}
