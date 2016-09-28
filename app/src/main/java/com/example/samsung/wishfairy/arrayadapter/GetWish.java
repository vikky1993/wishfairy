package com.example.samsung.wishfairy.arrayadapter;

/**
 * Created by SAMSUNG on 03-10-2015.
 */
public class GetWish {
    private static int resourceId;
    private static int thumbId;

    public static int getResourceId() {
        return resourceId;
    }

    public static void setResourceId(int resourceId) {
        GetWish.resourceId = resourceId;
    }

    public static int getThumbId() {
        return thumbId;
    }

    public static void setThumbId(int thumbId) {
        GetWish.thumbId = thumbId;
    }

    public static String getHead() {
        return head;
    }

    public static void setHead(String head) {
        GetWish.head = head;
    }

    public static int getuID() {
        return uID;
    }

    public static void setuID(int uID) {
        GetWish.uID = uID;
    }

    private static String head;
    private static int uID;
}
