package com.kulala.staticsview.style;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsview.R;


public class LeftTextCenterTxt extends RelativeLayout {
    public TextView txt_left;
    public View view_splitline;
    public TextView txt_center;
    public LeftTextCenterTxt(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.left_txt_center_txt, this, true);
        txt_left= (TextView) findViewById(R.id.txt_left);
        view_splitline= findViewById(R.id.view_splitline);
        txt_center= (TextView)findViewById(R.id.txt_center);
        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     leftTxt    = ta.getString(R.styleable.androidMe_lefttxt);
        String     text    = ta.getString(R.styleable.androidMe_text);
        boolean        splitLine  = ta.getBoolean(R.styleable.androidMe_splitLine, false);
        if (leftTxt != null && !leftTxt.equals("")) {
            txt_left.setText(leftTxt);
        }
        if (splitLine) {
            view_splitline.setVisibility(VISIBLE);
        }else{
            view_splitline.setVisibility(INVISIBLE);
        }
        if (text != null && !text.equals("")) {
            txt_center.setText(text);
        }
        ta.recycle();
    }
}
