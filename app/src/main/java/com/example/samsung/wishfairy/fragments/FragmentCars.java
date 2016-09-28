package com.example.samsung.wishfairy.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.samsung.wishfairy.R;
import com.example.samsung.wishfairy.database.DBHelper;

import java.util.Vector;

/**
 * Created by SAMSUNG on 16-11-2015.
 */
@SuppressLint("ValidFragment")
public class FragmentCars extends Fragment implements View.OnClickListener,View.OnTouchListener {
    int ar[];
    int ari[];
    String head[];
    int length;
    ImageView large;
    ImageView temperory;
    LinearLayout l1;
    private static TypedArray typedArraySmall;
    private static TypedArray typedArrayBig;
    FrgamentDesc desc;
    HorizontalScrollView h1;
    Vector<ImageView> viewGroup;
    int j = 0;
    static int  largePosition;
    static int headPosition;
    ImageView small;
    public static boolean touched = false;
    private  static DBHelper dbHelper;
    public static ImageView previous, next;
    @SuppressLint("ValidFragment")
    public FragmentCars(int largePosition,int headPosition) {
        this.largePosition=largePosition;
        this.headPosition=largePosition;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_layout, container, false);

        // insertChiledFragment();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //insertChiledFragment();
        dbHelper=new DBHelper(getActivity());
        previous = (ImageView) getActivity().findViewById(R.id.prev);
        next = (ImageView) getActivity().findViewById(R.id.next);
        next.setImageResource(R.drawable.next);
        previous.setImageResource(R.drawable.prev);
        setAlphaForImageViews(120, previous);
        setAlphaForImageViews(255, next);
        h1 = (HorizontalScrollView) getActivity().findViewById(R.id.h1);
        h1.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (h1.getScrollX() > (h1.getChildAt(0).getMeasuredWidth() - (getActivity().getWindowManager().getDefaultDisplay().getWidth()))) {
                    setAlphaForImageViews(120, next);

                } else if (h1.getScrollX() > 20 && !(h1.getScrollX() > (h1.getChildAt(0).getMeasuredWidth() - (getActivity().getWindowManager().getDefaultDisplay().getWidth())))) {
                    setAlphaForImageViews(255, previous);
                    setAlphaForImageViews(255, next);
                } else if (h1.getScrollX() == 0) {
                    setAlphaForImageViews(120, previous);
                }

            }
        });

        ar = new int[]{R.drawable.m5,R.drawable.gallardo,R.drawable.porche_911,R.drawable.aventador,R.drawable.diavel,R.drawable.fz1};
        ari = new int[]{R.drawable.rsz_m5,R.drawable.rsz_gallardo,R.drawable.rsz_911,R.drawable.rsz_aventador,R.drawable.rsz_diavel,R.drawable.rsz_fz1};
        head= new String[]{"BMW M5","Lamborghini Gallardo ","Porche 911","Lamborghini Aventador","Ducati Diavel_","Yamaha FZ1_"};
        length =ari.length;
        for(int i=0;i<length;i++){
            if(!(dbHelper.checkForDuplicationOfItems(head[i]))){
                dbHelper.addItems(head[i]);
            }
        }
        viewGroup = new Vector<ImageView>();
        large = (ImageView) getActivity().findViewById(R.id.large);
        large.setImageResource(R.drawable.m5);
        //desc.changeContent(head[headPosition]);
        l1 = (LinearLayout) getActivity().findViewById(R.id.inner);
        for (int i = 0; i < length; i++) {
            small = new ImageView(getActivity());
            // small.setPadding(1,1,3,1);
            small.setAlpha(50);
            small.setMaxWidth(150);

            small.setImageResource(ari[i]);
            small.setId(i);
            l1.addView(small);
            viewGroup.add(small);
            j = i;
            small.setOnTouchListener(this);
            small.setOnClickListener(this);

        }insertChiledFragment();
        // setProperties(headPosition);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setAlphaForImageViews(int alphaForImageViews, ImageView iv) {
        iv.setImageAlpha(alphaForImageViews);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void insertChiledFragment(){

        desc=new FrgamentDesc("Super Cars");
        FragmentTransaction transaction=getChildFragmentManager().beginTransaction();
        // desc.changeContent(head[headPosition]);
        String title=head[headPosition];

        Log.d("heading ", "headposition" + head[headPosition]);
        transaction.add(R.id.frame,desc);
        transaction.addToBackStack(null).commit();
        desc.selectedItem(ari[0],ar[0],head[0],0);

    }
    @Override
    public void onClick(View v) {
        if (touched) {
            int position = v.getId();
            large.setImageResource(ar[position]);
            int resourceId=ar[position];
            int thumbnailId=ari[position];
            String title=head[position];
            Animation myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.aimee);
            large.setAnimation(myFadeInAnimation);
            // frg=new FrgamentDesc();
            setProperties(position);
            desc.selectedItem(thumbnailId,resourceId,title,position);
            desc.setMatter();
            setAlphaForImageViews(255, large);
            for (int i = 0; i < ar.length; i++) {
                setAlphaForImageViews(50, viewGroup.get(i));
            }
            touched = false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        for(int i=0;i<ar.length;i++){
            //Animation myFadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.aimee);
            temperory=viewGroup.get(i);
            setAlphaForImageViews(255,temperory);

        }
        touched=true;
        setAlphaForImageViews(50,large);
        return false;
    }
    public void setProperties(int title){
        String heading=head[title];
        desc.changeContent(heading);
    }
}
