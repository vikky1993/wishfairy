package com.example.samsung.wishfairy.arrayadapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samsung.wishfairy.R;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by SAMSUNG on 03-10-2015.
 */
public class CustomListAdapter extends ArrayAdapter {

    private int resource;
    public static int j=0;
    private LayoutInflater inflater;
    private Context context;
    private static int ii;
    List<AraayAdapterCustom> item;
    public static int viewcount=0;
    private static Cursor cursor;
    public static HashMap hashMap;
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    public CustomListAdapter(Context context, int resource,
                            int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }
    public static void setHashMap(HashMap hash){
        hashMap=hash;
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }


    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }
    public CustomListAdapter(Context context, List<AraayAdapterCustom> resource){
        super(context, R.layout.list_item, resource);
        this.item=resource;
    }
    public void setCurser(Context context, Cursor cursor) {

        this.cursor=cursor;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;Log.d("text:::::::::::::::::::::::::::::::::::::::::::::::::::","");

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            /*cursor.moveToFirst();
            int count=0;
            do {
                viewHolder.tvTitle.setText(cursor.getString(cursor.getColumnIndex("head")));
                Log.d("text:::::::::::::::::::::::::::::::::::::::::::::::::::",""+cursor.getString(cursor.getColumnIndex("head")));
                viewHolder.ivIcon.setImageResource(cursor.getInt(cursor.getColumnIndex("thumbid")));
                viewHolder.tvDescription.setText(cursor.getString(cursor.getColumnIndex("category")));
                if(j==count){
                    j=j+1;
                    break;
                }count++;
            }while (cursor.moveToNext());*/
        }
            else {
                // recycle the already inflated view
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.ivIcon.setImageResource(item.get(position).bitmap);
            viewHolder.tvTitle.setText(item.get(position).Head);
            viewHolder.tvDescription.setText(item.get(position).matter);
            convertView.setTag(viewHolder);
            if(hashMap!=null){
                if (hashMap.containsKey(position)){
                    convertView.setBackgroundColor(Color.CYAN);
                }else{convertView.setBackgroundColor(Color.WHITE);}
            }else{convertView.setBackgroundColor(Color.WHITE);}
        Log.d("call","reurns view");
            return convertView;
    }
    private static class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDescription;

    }
}