package com.example.samsung.wishfairy.handlewishes;

import android.content.Context;

import com.example.samsung.wishfairy.database.DBHelper;

/**
 * Created by SAMSUNG on 30-09-2015.
 */
public class RemoveWish {
    public void remove(int wishId,Context context){
        DBHelper dbHelper=new DBHelper(context);
        dbHelper.deleteWish(wishId);
    }
}
