package com.kulala.staticsview.style;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsview.R;


public class TopImgBottomTxt extends RelativeLayout {
    public TextView txt_center;
    public ImageView img_top;
    public TopImgBottomTxt(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.top_img_bottom_txt, this, true);
        txt_center= (TextView) findViewById(R.id.txt_center);
        img_top= (ImageView)findViewById(R.id.img_top);
        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     text    = ta.getString(R.styleable.androidMe_text);
        int        res  = ta.getResourceId(R.styleable.androidMe_res, 0);
        if (text != null && !text.equals("")) {
            txt_center.setText(text);
        }
        if (res!=0) {
            img_top.setImageResource(res);
        }
        ta.recycle();
    }
}
