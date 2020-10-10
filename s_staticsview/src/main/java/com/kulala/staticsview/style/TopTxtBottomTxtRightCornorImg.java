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


public class TopTxtBottomTxtRightCornorImg extends RelativeLayout {
    public TextView txt_bottom,txt_top;
    public ImageView img_right;
    public View view_background;
    public TopTxtBottomTxtRightCornorImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.top_txt_bottom_txt_right_connorimg, this, true);
        txt_top= (TextView) findViewById(R.id.txt_top);
        txt_bottom= (TextView) findViewById(R.id.txt_bottom);
        img_right= (ImageView)findViewById(R.id.img_right);
        view_background= findViewById(R.id.view_background);
        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     texttop    = ta.getString(R.styleable.androidMe_texttop);
        String     textbottom    = ta.getString(R.styleable.androidMe_textbottom);
        if (texttop != null && !texttop.equals("")) {
            txt_top.setText(texttop);
        }
        if (textbottom != null && !textbottom.equals("")) {
            txt_bottom.setText(textbottom);
        }
        ta.recycle();
    }
}
