package com.kulala.staticsview.style;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.R;


public class LeftTextRightEdit extends RelativeLayout {
    public TextView txt_left;
    public View view_splitline;
    public EditText txt_center;
    public LeftTextRightEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.left_txt_right_edit, this, true);
        txt_left= (TextView) findViewById(R.id.txt_left);
        txt_center= (EditText) findViewById(R.id.txt_center);
        view_splitline= findViewById(R.id.view_splitline);
        TypedArray ta      = context.obtainStyledAttributes(attrs, R.styleable.androidMe);
        String     leftTxt    = ta.getString(R.styleable.androidMe_lefttxt);
        String     text    = ta.getString(R.styleable.androidMe_text);
        boolean        splitLine  = ta.getBoolean(R.styleable.androidMe_splitLine, false);
        boolean        needPadding  = ta.getBoolean(R.styleable.androidMe_needPadding, false);
        if (leftTxt != null && !leftTxt.equals("")) {
            txt_left.setText(leftTxt);
        }
        if(needPadding){
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) txt_left.getLayoutParams();
            lp.setMargins(0,lp.topMargin,lp.rightMargin,lp.bottomMargin);
            int dp10= ODipToPx.dipToPx(context,10);
            txt_left.setPadding(dp10,0,0,0);
        }
        if (text != null && !text.equals("")) {
            txt_center.setHint(text);
        }
        if (splitLine) {
            view_splitline.setVisibility(VISIBLE);
        }else{
            view_splitline.setVisibility(INVISIBLE);
        }
        ta.recycle();
    }
}
