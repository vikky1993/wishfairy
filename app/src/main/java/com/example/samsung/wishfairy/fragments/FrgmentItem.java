package com.example.samsung.wishfairy.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samsung.wishfairy.R;
import com.example.samsung.wishfairy.arrayadapter.AraayAdapterCustom;
import com.example.samsung.wishfairy.handlewishes.RemoveWish;
import com.example.samsung.wishfairy.login.LoginProcess;

import java.util.ArrayList;

import tyrantgit.explosionfield.ExplosionField;

/**
 * Created by SAMSUNG on 17-10-2015.
 */
@SuppressLint("ValidFragment")
public class FrgmentItem extends Fragment {
    ImageView iv;
    Button b1;
    public  ArrayList<AraayAdapterCustom> arrayList;
    public static int position;
    View content;
    ImageView delete_item;
    int onSwipePosition;
    ImageView zoom;
    SharedPreferences preference;
    private Matrix matrix = new Matrix();
    TextView category,date,head,about;
    private ScaleGestureDetector scaleGestureDetector;
    @SuppressLint("ValidFragment")
    public  FrgmentItem(ArrayList<AraayAdapterCustom> arrayList,int position){
        this.arrayList=arrayList;
        this.position=position;
        this.onSwipePosition=position;
    }
    public void setAbout(int pposition){
        String about;
        String ar[];
       switch (arrayList.get(pposition).matter){
           case "Super Bikes":
               ar=getResources().getStringArray(R.array.super_bikes);
               about="           '' " +ar[arrayList.get(pposition).matter_position]+" ''";
               this.about.setText(about);
               break;
           case "Super Cars":
               ar=getResources().getStringArray(R.array.super_cars);
               about="           '' "+ar[arrayList.get(pposition).matter_position]+" ''";
               this.about.setText(about);
               break;
           case "Dream Lands":
               ar=getResources().getStringArray(R.array.dream_lands);
               about="           '' "+ar[arrayList.get(pposition).matter_position]+" ''";
               this.about.setText(about);
               break;
       }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        content=inflater.inflate(R.layout.fragment_item_layout,container,false);

        return content;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preference= getActivity().getApplicationContext().getSharedPreferences("FirstTimeLogin",Context.MODE_PRIVATE);
        boolean firstTime=preference.getBoolean(LoginProcess.username,false);
        b1= (Button) getActivity().findViewById(R.id.search);
        //b2= (Button) getActivity().findViewById(R.id.remove);
        delete_item= (ImageView) getActivity().findViewById(R.id.delete_item);
        category= (TextView) getActivity().findViewById(R.id.category);
        category.setText(arrayList.get(position).getMatter());
        date= (TextView) getActivity().findViewById(R.id.date);
        date.setText(arrayList.get(position).getHead());
        head= (TextView) getActivity().findViewById(R.id.item_head);
        about= (TextView) getActivity().findViewById(R.id.about);
        iv= (ImageView) getActivity().findViewById(R.id.itemImage);
        iv.setImageResource(arrayList.get(position).getResId());
        date.setText(arrayList.get(position).getDate());
        head.setText(arrayList.get(position).getHead());
        Typeface font = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/Roboto-Regular.ttf");
        about.setTypeface(font,2);
        setAbout(position);
        scaleGestureDetector = new ScaleGestureDetector(getActivity(),new ScaleListener());
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        Log.d("Fling","inside fling");
                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.d("Fling", "Right to left");
                                changeContentOnSwipe(1);
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.d("Fling", "Left to Right");
                                changeContentOnSwipe(-1);
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //scaleGestureDetector.onTouchEvent(event);

                Dialog dialog = new Dialog(getActivity()) {
                    @Override
                    public boolean onTouchEvent(MotionEvent event) {
                        return true;
                    }
                };
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.pinch_zoom_layot);
                zoom = (ImageView) dialog.findViewById(R.id.zoom);
                zoom.setImageResource(arrayList.get(position).getResId());
                zoom.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        scaleGestureDetector.onTouchEvent(event);
                        return true;
                    }
                });

                dialog.show();
                return false;
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.google.com/#q="+head.getText()+"");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                View title=getActivity().getLayoutInflater().inflate(R.layout.exit_dialog_layout_title,null);
                View layout=getActivity().getLayoutInflater().inflate(R.layout.delete_dialoge_layout,null);
                alert.setCustomTitle(title);
                alert.setView(layout);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ExplosionField explosionField=new ExplosionField(getActivity());
                        explosionField.explode(iv);

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                //Your code here
                                try {
                                    Thread.sleep(600);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                RemoveWish removeWish=new RemoveWish();
                                removeWish.remove(arrayList.get(position).getWishId(),getActivity().getApplicationContext());
                                FragmentList fragmentList = new FragmentList();
                                FragmentManager frgManager = getFragmentManager();
                                frgManager.beginTransaction().replace(R.id.content_frame, fragmentList).addToBackStack(null)
                                        .commit();
                            }
                        });
                    }

                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                alert.show();



            }
        });
        if(firstTime){
        showOverlay();}
    }
    public void showOverlay(){
        final Dialog dialog = new Dialog(getActivity(),
                android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.overlay_layout_list_item);
        final CheckBox checkBox=(CheckBox)dialog.findViewById(R.id.do_not_show);
        LinearLayout layout = (LinearLayout) dialog
                .findViewById(R.id.llOverlay_activity);
        layout.setBackgroundColor(Color.TRANSPARENT);
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(checkBox.isChecked()){
                   preference.edit().putBoolean(LoginProcess.username,false).commit();
                }else{
                    Log.d("checked box is"," not checked");
                   // preference.edit().putBoolean("FLAG",true).commit();
                }
                dialog.dismiss();

            }
        });
        dialog.show();
    }
    public void changeContentOnSwipe(int direction){
        if(position+direction>=0&&position+direction<arrayList.size()){
            position=position+direction;
            category.setText(arrayList.get(position).matter);
            date.setText(arrayList.get(position).getDate());
            head.setText(arrayList.get(position).Head);
            iv.setImageResource(arrayList.get(position).getResId());
            setAbout(position);
        }
    }
    private class ScaleListener extends ScaleGestureDetector.
            SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            matrix.setScale(scaleFactor, scaleFactor);
            zoom.setImageMatrix(matrix);
            return true;
        }

    }


}

