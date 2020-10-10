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


public class LeftImgCenterText extends RelativeLayout {
    public TextView txt_left;
    public ImageView img_left;
    public View view_splitline;
    public LeftImgCenterText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.leftimg_centertext, this, true);
        img_left= (ImageView) findViewById(R.id.img_left);
        txt_left= (TextView) findViewById(R.id.txt_left);
        view_splitline= findViewById(R.id.view_splitline);
        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        int        leftres  = ta.getResourceId(R.styleable.androidMe_leftres, 0);
        String     leftTxt    = ta.getString(R.styleable.androidMe_lefttxt);
        boolean        splitLine  = ta.getBoolean(R.styleable.androidMe_splitLine, false);
        if (leftTxt != null && !leftTxt.equals("")) {
            txt_left.setText(leftTxt);
        }
        if (leftres != 0) {
            img_left.setImageResource(leftres);
            img_left.setVisibility(VISIBLE);
        }else{
            img_left.setVisibility(INVISIBLE);
        }
        if (splitLine) {
            view_splitline.setVisibility(VISIBLE);
        }else{
            view_splitline.setVisibility(INVISIBLE);
        }
        ta.recycle();
    }
}
