package com.kulala.staticsview.style;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kulala.staticsview.R;


public class LeftEdit_RightImg extends LinearLayout {
    public EditText txt_left;
    public ImageView img_right;
    public LeftEdit_RightImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.leftedit_rightimg, this, true);
        img_right= (ImageView) findViewById(R.id.img_right);
        txt_left= (EditText) findViewById(R.id.txt_left);
        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        boolean     hideRightImg    = ta.getBoolean(R.styleable.androidMe_hideRightImage,false);
        int        rightres  = ta.getResourceId(R.styleable.androidMe_rightres, 0);
        String     leftTxt    = ta.getString(R.styleable.androidMe_lefttxt);
        if(hideRightImg){
            img_right.setVisibility(View.INVISIBLE);
        }else{
            img_right.setVisibility(View.VISIBLE);
        }

        if (leftTxt != null && !leftTxt.equals("")) {
            txt_left.setHint(leftTxt);
        }
        if (rightres != 0) {
            img_right.setImageResource(rightres);
            img_right.setVisibility(VISIBLE);
        }else{
            img_right.setVisibility(INVISIBLE);
        }
        ta.recycle();
    }
}
