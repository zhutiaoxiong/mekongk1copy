package com.kulala.staticsview.style;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsview.R;


public class CenterImgAndTxt extends RelativeLayout {
    public TextView txt;
    public ImageView img;
    public View view;
    public CenterImgAndTxt(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.centerimg_and_txt, this, true);
        img= (ImageView) findViewById(R.id.img);
        txt= (TextView) findViewById(R.id.txt);
        view= findViewById(R.id.view);
        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        int        res  = ta.getResourceId(R.styleable.androidMe_res, 0);
        String     text    = ta.getString(R.styleable.androidMe_text);
        int        background  = ta.getResourceId(R.styleable.androidMe_background, 0);
        if (text != null && !text.equals("")) {
            txt.setText(text);
        }
        if (res != 0) {
            img.setImageResource(res);
        }
        if (background != 0) {
            view.setBackgroundResource(background);
        }

        ta.recycle();
    }
}
