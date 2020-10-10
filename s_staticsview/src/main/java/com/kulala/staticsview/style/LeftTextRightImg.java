package com.kulala.staticsview.style;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsview.R;


public class LeftTextRightImg extends RelativeLayout {
    public TextView txt_left;
    public View view_splitline;
    public ImageView right_img;

    public LeftTextRightImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.left_txt_right_img, this, true);
        txt_left= (TextView) findViewById(R.id.txt_left);
        right_img= (ImageView) findViewById(R.id.right_img);
        view_splitline= findViewById(R.id.view_splitline);
        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     leftTxt    = ta.getString(R.styleable.androidMe_lefttxt);
        String     gravityTxt    = ta.getString(R.styleable.androidMe_gravityTxt);
        int        rightres  = ta.getResourceId(R.styleable.androidMe_rightres, 0);
        boolean        splitLine  = ta.getBoolean(R.styleable.androidMe_splitLine, false);
        if (leftTxt != null && !leftTxt.equals("")) {
            txt_left.setText(leftTxt);
        }
        if (splitLine) {
            view_splitline.setVisibility(VISIBLE);
        }else{
            view_splitline.setVisibility(INVISIBLE);
        }
        if(gravityTxt==null||gravityTxt.equals("")){
            txt_left.setGravity(Gravity.CENTER_VERTICAL);
        }else if(gravityTxt.equals("center")){
            txt_left.setGravity(Gravity.CENTER);
        }else if(gravityTxt.equals("right")){
            txt_left.setGravity(Gravity.RIGHT);
        }
        if(rightres!=0){
            right_img.setImageResource(rightres);
        }
        ta.recycle();
    }
}
