package com.example.samsung.wishfairy.arrayadapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samsung.wishfairy.R;

/**
 * Created by SAMSUNG on 08-10-2015.
 */
public class CustomCursorAdapter extends CursorAdapter{
    Context context;
    private LayoutInflater cursorInflater;
    public static int j=0;
    public CustomCursorAdapter(Context context, Cursor c, int flags) {
        super(context,c,flags);
        // this.cursor = cursor;
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }




    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d("*********************************************", "****************************************1");
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_item, parent, false);
        Log.d("*********************************************", "****************************************211111111111");
        return v;
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        Log.d("*********************************************", "****************************************");

        ImageView imageView= (ImageView) view.findViewById(R.id.ivIcon);
        TextView textView= (TextView) view.findViewById(R.id.tvTitle);
        TextView des= (TextView) view.findViewById(R.id.tvDescription);
        cursor.moveToFirst();
        imageView.setImageResource(cursor.getInt(cursor.getColumnIndexOrThrow("thumbid")));
        textView.setText(cursor.getString(cursor.getColumnIndexOrThrow("head")));
        des.setText(cursor.getString(cursor.getColumnIndexOrThrow("head")));
        Log.d("*********************************************", ""+cursor.getString(cursor.getColumnIndexOrThrow("head")));int i=0;
        do {
            if(j>cursor.getCount()){
                j=0;
            } if(i>cursor.getCount()){
                i=0;
            }
            imageView.setImageResource(cursor.getInt(cursor.getColumnIndexOrThrow("thumbid")));
            textView.setText(cursor.getString(cursor.getColumnIndexOrThrow("head")));
            des.setText(cursor.getString(cursor.getColumnIndexOrThrow("head")));
            Log.d("this is the row returned",""+cursor.getString(cursor.getColumnIndexOrThrow("head")));
            Log.d("value of j ="," "+j);
            Log.d("value of i =",""+i);
            Log.d("value of cursor count =",""+cursor.getCount());
            if(i>=j){j++;break;}i++;
        }while (cursor.moveToNext());

    }
    private class ViewHolder {
        private ImageView imageView;
        private TextView mImage;
        private TextView mCheckBox;

    }
}
