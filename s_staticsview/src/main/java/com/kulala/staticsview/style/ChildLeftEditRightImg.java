package com.kulala.staticsview.style;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;

import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.R;


public class ChildLeftEditRightImg extends LeftEdit_RightImg {
    private boolean isPassword;

    public ChildLeftEditRightImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        ChildLeftEditRightImg.super.txt_left.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        super.img_right.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if(isPassword){
                    isPassword=false;
                    ChildLeftEditRightImg.super.txt_left.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                    ChildLeftEditRightImg.super.img_right.setImageResource(R.drawable.open_eyes);
                }else{
                    isPassword=true;
                    ChildLeftEditRightImg.super.txt_left.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                    ChildLeftEditRightImg.super.img_right.setImageResource(R.drawable.close_eyes);
                }
            }
        });

    }
}
