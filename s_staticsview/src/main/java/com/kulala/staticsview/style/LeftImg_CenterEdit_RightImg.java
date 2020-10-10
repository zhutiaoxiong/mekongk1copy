package com.kulala.staticsview.style;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kulala.staticsview.R;


public class LeftImg_CenterEdit_RightImg extends RelativeLayout {
    public EditText txt_left;
    public ImageView img_right,img_center,img_left;
    public LeftImg_CenterEdit_RightImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.leftimg_centeredit_rightimg, this, true);
        img_right= (ImageView) findViewById(R.id.img_right);
        img_center= (ImageView) findViewById(R.id.img_center);
        img_left= (ImageView) findViewById(R.id.img_left);
        txt_left= (EditText) findViewById(R.id.txt_left);
        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        boolean     hideRightImg    = ta.getBoolean(R.styleable.androidMe_hideRightImage,false);
        boolean     hideCenterImg    = ta.getBoolean(R.styleable.androidMe_hideCenterImage,false);
        int        leftres  = ta.getResourceId(R.styleable.androidMe_leftres, 0);
        int        rightres  = ta.getResourceId(R.styleable.androidMe_rightres, 0);
        int        res = ta.getResourceId(R.styleable.androidMe_res, 0);
        String     leftTxt    = ta.getString(R.styleable.androidMe_lefttxt);
        if(hideRightImg){
            img_right.setVisibility(View.INVISIBLE);
        }else{
            img_right.setVisibility(View.VISIBLE);
        }

        if(hideCenterImg){
            img_center.setVisibility(View.INVISIBLE);
        }else{
            img_center.setVisibility(View.VISIBLE);
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
        if (leftres != 0) {
            img_left.setImageResource(leftres);
            img_left.setVisibility(VISIBLE);
        }else{
            img_left.setVisibility(INVISIBLE);
        }
        if (res != 0) {
            img_center.setImageResource(res);
            img_center.setVisibility(VISIBLE);
        }else{
            img_center.setVisibility(INVISIBLE);
        }
        ta.recycle();
    }
}
