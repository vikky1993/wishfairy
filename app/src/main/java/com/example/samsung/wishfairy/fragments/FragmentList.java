package com.example.samsung.wishfairy.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LauncherActivity;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.samsung.wishfairy.R;
import com.example.samsung.wishfairy.arrayadapter.AraayAdapterCustom;
import com.example.samsung.wishfairy.arrayadapter.CustomCursorAdapter;
import com.example.samsung.wishfairy.arrayadapter.CustomListAdapter;
import com.example.samsung.wishfairy.database.DBHelper;
import com.example.samsung.wishfairy.handlewishes.RemoveWish;
import com.example.samsung.wishfairy.home.HomeActivity;
import com.example.samsung.wishfairy.login.LoginProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Created by SAMSUNG on 03-10-2015.
 */
public class FragmentList extends ListFragment {
    private static List<AraayAdapterCustom> mItems;
    CustomCursorAdapter adapter;
    public static CustomListAdapter listAdapter;
    public static ActionMode modeGlobal;
    public static boolean checkedMode;
    public static  SharedPreferences preferences;
    View view;
    HashMap<Integer,Boolean> itemsToRemove=new HashMap<Integer,Boolean>();
    Cursor cursor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        HomeActivity.drawerListSelectedPosition=0;
        FrgmentItem frgmentItem=new FrgmentItem((ArrayList<AraayAdapterCustom>) mItems,position);
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.content_frame, frgmentItem).addToBackStack(null)
                .commit();
    }
    public void addItemToMap(int position,boolean value){
        itemsToRemove.put(position,value);
    }
    public void removeItemsFromMap(int position){
        itemsToRemove.remove(position);
        Log.d("size", "" + itemsToRemove.size());
    }
    public void size(){
        Toast.makeText(getActivity(),""+itemsToRemove.size(),Toast.LENGTH_SHORT).show();
        itemsToRemove.clear();
    }
    public void removeItemsFromWishList(){
        Set set=itemsToRemove.entrySet();
        Iterator iterator=set.iterator();
        while (iterator.hasNext()){
            Map.Entry me= (Map.Entry) iterator.next();
            int positionToDelete= (int) me.getKey();
            Toast.makeText(getActivity(),"position :"+positionToDelete,Toast.LENGTH_SHORT).show();
            RemoveWish removeWish=new RemoveWish();
            removeWish.remove(mItems.get(positionToDelete).getWishId(),getActivity().getApplicationContext());
            FragmentTransaction tr = getFragmentManager().beginTransaction();
            tr.replace(R.id.content_frame, new FragmentList());
            tr.commit();
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences=getActivity().getSharedPreferences("OVERLAY",Context.MODE_PRIVATE);
        mItems = new ArrayList<AraayAdapterCustom>();
        DBHelper dbHelper=new DBHelper(getActivity());
        if(preferences.contains(LoginProcess.username)){
            boolean flag=preferences.getBoolean(LoginProcess.username,true);
               Log.d("contains flag............","true");
           if(flag){
               showOverlay();
           }else{
               Log.d("check box is............","false");

           }
        }
        Cursor temp=dbHelper.getWish(LoginProcess.userDetails.getInt(LoginProcess.userDetails.getColumnIndex("id")));
        if(temp.getCount()>0&&!(preferences.contains(LoginProcess.username))) {
            preferences.edit().putBoolean(LoginProcess.username,false).commit();
            showOverlay();
        }

        Cursor cursor = dbHelper.getWish(LoginProcess.userDetails.getInt(LoginProcess.userDetails.getColumnIndex("id")));
         if(cursor.moveToFirst()){int i=0;
            do{
                // The Cursor is now set to the right position
                mItems.add(new AraayAdapterCustom(cursor.getInt(cursor.getColumnIndex("thumbid")),cursor.getString(cursor.getColumnIndex("head")),
                        cursor.getString(cursor.getColumnIndex("category")),cursor.getInt(cursor.getColumnIndex("wishid")),cursor.getString(cursor.getColumnIndex("date")),cursor.getInt(cursor.getColumnIndex("resourceid")),cursor.getInt(cursor.getColumnIndex("matterposition"))));
            }while (cursor.moveToNext());
        }
        cursor.moveToFirst();
        if(cursor.getCount()>0){

        }
        listAdapter=new CustomListAdapter(getActivity(),mItems);
        listAdapter.setCurser(getActivity(),cursor);
        setListAdapter(listAdapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private  int nr=0;
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    nr++;
                    checked=true;
                    listAdapter.setNewSelection(position, checked);
                    addItemToMap(position,true);
                    if(itemsToRemove.size()!=0){
                        CustomListAdapter.setHashMap(itemsToRemove);
                    }
                } else {
                    nr--;
                    removeItemsFromMap(position);
                    CustomListAdapter.setHashMap(itemsToRemove);
                    listAdapter.notifyDataSetChanged();
                }
                mode.setTitle(nr + " Selected");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                nr = 0;
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.contextual_menu, menu);
                modeGlobal=mode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                checkedMode=true;
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode1, MenuItem item) {
                modeGlobal=mode1;
                switch (item.getItemId()) {

                    case R.id.item_delete:
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        View v=getActivity().getLayoutInflater().inflate(R.layout.exit_dialog_layout_title,null);
                        View layout=getActivity().getLayoutInflater().inflate(R.layout.delete_dialoge_layout,null);
                        alert.setCustomTitle(v);
                        alert.setView(layout);
                        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                nr = 0;
                                listAdapter.clearSelection();
                                modeGlobal.finish();
                                removeItemsFromWishList();
                                CustomListAdapter.setHashMap(null );
                            }

                        });
                        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        modeGlobal.finish();
                                    }
                                });

                                alert.show();

                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                listAdapter.clearSelection();
                checkedMode=false;
                CustomListAdapter.setHashMap(null);
            }
        });
        //View emptyView=getActivity().getLayoutInflater().inflate(R.layout.empty_listrview_layout,null);
        setEmptyText("You dont have any items in your wish list");
    }
    public void showOverlay(){
        final Dialog dialog = new Dialog(getActivity(),
        android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.overlay_layout);
        final CheckBox checkBox=(CheckBox)dialog.findViewById(R.id.do_not_show);
        LinearLayout layout = (LinearLayout) dialog
        .findViewById(R.id.llOverlay_activity);
        layout.setBackgroundColor(Color.TRANSPARENT);
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(checkBox.isChecked()){
                    preferences.edit().putBoolean(LoginProcess.username,false).commit();
                }else{
                    Log.d("checked box is"," not checked");
                    preferences.edit().putBoolean(LoginProcess.username,true).commit();
                }
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        CustomListAdapter.j=0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
