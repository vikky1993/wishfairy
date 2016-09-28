package com.example.samsung.wishfairy.handlewishes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by SAMSUNG on 30-09-2015.
 */
public class AddWish {
    private static int resourceId;
    private static int thumbId;
    private static String head;
    private static int uID;
    private static int itemID;
    private static String currentDate;
    private static String category;
    private static int matterPosition;
    public static String getCategory() {
        return category;
    }

    public static int getItemID() {
        return itemID;
    }

    public static void setItemID(int itemID) {
        AddWish.itemID = itemID;
    }

    public static void setCategory(String category) {
        AddWish.category = category;
    }

    public AddWish(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        currentDate = df.format(c.getTime());
    }

    public static int getMatterPosition() {
        return matterPosition;
    }

    public static void setMatterPosition(int matterPosition) {
        AddWish.matterPosition = matterPosition;
    }

    public static String getCurrentDate() {
        return currentDate;
    }

    public static int getThumbId() {
        return thumbId;
    }

    public static void setThumbId(int thumbId) {
        AddWish.thumbId = thumbId;
    }

    public static int getResourceId() {
        return resourceId;
    }

    public static void setResourceId(int resourceId) {
        AddWish.resourceId = resourceId;
    }

    public static String getHead() {
        return head;
    }

    public static void setHead(String head) {
        AddWish.head = head;
    }

    public static int getuID() {
        return uID;
    }

    public static void setuID(int uID) {
        AddWish.uID = uID;
    }
}
