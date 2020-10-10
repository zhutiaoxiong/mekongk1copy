package com.kulala.staticsview.style;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsview.R;


public class LeftTextCenterEdit extends RelativeLayout {
    public TextView txt_left;
    public View view_splitline;
    public EditText txt_center;
    public LeftTextCenterEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.left_txt_center_edit, this, true);
        txt_left= (TextView) findViewById(R.id.txt_left);
        view_splitline= findViewById(R.id.view_splitline);
        txt_center= (EditText)findViewById(R.id.txt_center);
        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     leftTxt    = ta.getString(R.styleable.androidMe_lefttxt);
        String     gravityTxt    = ta.getString(R.styleable.androidMe_gravityTxt);
        String     hint    = ta.getString(R.styleable.androidMe_hint);
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
        if (hint != null && !hint.equals("")) {
            txt_center.setHint(hint);
        }
        ta.recycle();
    }
}
