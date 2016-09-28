package com.example.samsung.wishfairy.delete;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by SAMSUNG on 15-07-2015.
 */
public class CustomFont extends TextView {


    public CustomFont(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
