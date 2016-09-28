package com.example.wishfairy.views.fragment;

import com.example.wishfairy.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImgFragment extends Fragment {

    public ImgFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_img, container, false);
        ImageView img = ((ImageView) rootView.findViewById(R.id.image));
        img.setImageResource(R.drawable.new_wish);
        img.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        return rootView;
    }
}