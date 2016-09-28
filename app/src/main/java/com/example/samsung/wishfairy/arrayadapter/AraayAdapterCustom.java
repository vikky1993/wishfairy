package com.example.samsung.wishfairy.arrayadapter;

import android.app.Activity;

import com.example.samsung.wishfairy.database.DBHelper;

/**
 * Created by SAMSUNG on 03-10-2015.
 */
public class AraayAdapterCustom extends Activity{
    public  String Head;
    public  String matter;
    public String date;
    public  int wishId;
    public  int bitmap;
    public int matter_position;
    public int getResId() {
        return resId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public  int resId;

    public AraayAdapterCustom(int id, String head, String matter, int wishId, String date,int resId,int matter_position){
            this.Head=head;
            this.matter=matter;
            this.bitmap=id;
            this.wishId=wishId;
            this.date=date;
            this.resId=resId;
            this.matter_position=matter_position;
    }

    public int getWishId() {
        return wishId;
    }

    public void setWishId(int wishId) {
        this.wishId = wishId;
    }

    public  String getHead() {
        return Head;
    }

    public  void setHead(String head) {
        Head = head;
    }

    public  String getMatter() {
        return matter;
    }

    public  void setMatter(String matter) {
        this.matter = matter;
    }

    public  int getBitmap() {
        return bitmap;
    }

    public  void setBitmap(int bitmap) {
        this.bitmap = bitmap;
    }
}
