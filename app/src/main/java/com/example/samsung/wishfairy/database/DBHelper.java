package com.example.samsung.wishfairy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.samsung.wishfairy.R;
import com.example.samsung.wishfairy.arrayadapter.AraayAdapterCustom;
import com.example.samsung.wishfairy.arrayadapter.CustomListAdapter;
import com.example.samsung.wishfairy.handlewishes.AddWish;
import com.example.samsung.wishfairy.arrayadapter.GetWish;
import com.example.samsung.wishfairy.login.LoginProcess;
import com.example.samsung.wishfairy.signup.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by SAMSUNG on 19-07-2015.
 */
public class DBHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static int userID;
    // Database Name
    private static final String DATABASE_NAME = "wish_ferry";

    // Contacts table name
    private static final String TABLE_REGISTER = "registration";
    String ph=null;
    // Contacts Table Columns names
    public static  Vector<AraayAdapterCustom> wish;
    private static final String KEY_ID = "id";
    private static final String KEY_UNAME = "uname";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_REGISTER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_UNAME+ " TEXT,"
                + KEY_PASSWORD + " TEXT," +KEY_EMAIL+" TEXT,"+ KEY_PHONE +" TEXT "+")";
        String WISH_LIST_TABLE="CREATE TABLE wishlist(uid INTEGER,resourceid INTEGER,thumbid INTEGER,head TEXT,wishid INTEGER PRIMARY KEY,date TEXT,category TEXT,matterposition INTEGER,itemid INTEGER)";
        String ITEM_TABLE="CREATE TABLE items(itemid INTEGER PRIMARY KEY,head TEXT)";
        db.execSQL(WISH_LIST_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(ITEM_TABLE);
    }
    public int addContact(RegistrationProcess process) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UNAME, process.getUname()); // Contact Name
        values.put(KEY_PASSWORD, process.getPassword()); // Contact Phone Number
        values.put(KEY_EMAIL,process.getEmail());
        values.put(KEY_PHONE,process.getPhone());


        // Inserting Row
        db.insert(TABLE_REGISTER, null, values);

        //db.close(); // Closing database connection
        return 1;
    }
    public boolean addWish(AddWish addWish){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid",addWish.getuID());
        values.put("date",addWish.getCurrentDate());
        values.put("resourceid",addWish.getResourceId());
        values.put("thumbid",addWish.getThumbId());
        values.put("head",addWish.getHead());
        values.put("category",addWish.getCategory());
        values.put("matterposition",addWish.getMatterPosition());
        values.put("itemid",addWish.getItemID());
        db.insert("wishlist",null,values);

       // db.
       //  Log.d()
        db.close();
        return true;
    }
    public boolean checkItemIdInWishList(int id,int uid){
        SQLiteDatabase db = this.getReadableDatabase();
        int itemID = id;
        String query = "SELECT  rowid _id, * FROM wishlist WHERE itemid ="+itemID+" AND uid="+uid+"";
        Log.d("query",query);
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount()>0){
            return true;
        }else{
            return false;
        }
    }
    public void addItems(String head){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("head",head);
        db.insert("items",null,values);
        db.close();
    }
    public boolean checkForDuplicationOfItems(String head){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  rowid _id, * FROM items WHERE head = '"+head+"'";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount()>0){return true;}
        else{return false;}
    }
    public boolean getPassword(String name,String password) {
       /* String query = "SELECT item_name FROM todo WHERE category =" + vg ;
        Cursor  cursor = database.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() != true) {
                string itemname =  cursor.getString(cursor.getColumnIndex("item_name")));

            }
        }*/
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_REGISTER + " WHERE " + KEY_UNAME + " ='"+ name+"'";
        Log.d("Query :", query);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            ph = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
            LoginProcess.userDetails=cursor;
            Log.d("password**************",ph);
            if(!(ph==null)){
                if(password.equals(ph)){
                    Log.d("password matched *****","password matched*****");
                    return true;
                }
                else {
                return false;
                }
            }
            else{
                return false;
            }
        }else{
            return false;
        }

    }
public Cursor getWish(int uID) {
    SQLiteDatabase db = this.getReadableDatabase();
    int uid = uID;
    String query = "SELECT  rowid _id, * FROM wishlist WHERE uid ="+uID+"";
    Log.d("query",query);
    Cursor cursor = db.rawQuery(query, null);
    Log.d("after executing","query");
    return cursor;
}
    public int getItemId(String item_head){
        SQLiteDatabase db = this.getReadableDatabase();
        String head = item_head;
        String query = "SELECT  rowid _id, * FROM items WHERE head ='"+head+"'";
        Log.d("query",query);
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("itemid"));
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public  void deleteWish(int wish_id){
        String query="DELETE FROM wishlist\n" +
                "WHERE wishid = "+wish_id+"";
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(query);
    }
}
