package com.mani.car.mekongk1;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsview.OnClickListenerMy;
import com.mani.car.mekongk1.common.GlobalContext;


public class ClipTitleHead extends RelativeLayout {
    public ImageView img_left, img_right;
    public TextView txt_title;
    public WebViewProgressBar my_progress;
    public TextView txt_right;

    public ClipTitleHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.titlehead, this, true);
        img_left = (ImageView) findViewById(R.id.img_left);
        img_right = (ImageView) findViewById(R.id.img_right);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_right = (TextView) findViewById(R.id.txt_right);
        my_progress = (WebViewProgressBar) findViewById(R.id.my_progress);
        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        int        leftres  = ta.getResourceId(R.styleable.androidMe_leftres, 0);
        String     text    = ta.getString(R.styleable.androidMe_text);
        int        background  = ta.getResourceId(R.styleable.androidMe_background, 0);
        String     righttxt    = ta.getString(R.styleable.androidMe_righttxt);
        int        rightres  = ta.getResourceId(R.styleable.androidMe_rightres, 0);
        boolean splitLine=ta.getBoolean(R.styleable.androidMe_splitLine,false);
        if (text != null && !text.equals("")) {
            txt_title.setText(text);
        }
        if (background != 0) {
            txt_title.setBackgroundResource(background);
        }else{
            txt_title.setBackgroundColor(getResources().getColor(R.color.normal_title_color));
        }
        if (righttxt != null && !righttxt.equals("")) {
            txt_right.setText(righttxt);
            txt_right.setVisibility(VISIBLE);
        }else{
            txt_right.setVisibility(INVISIBLE);
        }
        if (leftres != 0) {
            img_left.setImageResource(leftres);
            img_left.setVisibility(VISIBLE);
        }else{
            img_left.setVisibility(INVISIBLE);
        }
        if(splitLine){
            my_progress.setVisibility(View.VISIBLE);
        }else{
            my_progress.setVisibility(View.INVISIBLE);
        }
        if (rightres != 0) {
            img_right.setImageResource(rightres);
            img_right.setVisibility(VISIBLE);
        }else{
            img_right.setVisibility(View.INVISIBLE);
        }
        ta.recycle();
        img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                GlobalContext.getCurrentActivity().finish();
            }
        });
    }

    public void setTitle(String title) {
        txt_title.setText(title);
    }

//    public void setLeftRes(int resId) {
//        if (resId == 0) {
//            img_left.setImageDrawable(null);
//        } else {
//            img_left.setImageResource(resId);
//        }
//    }
}

