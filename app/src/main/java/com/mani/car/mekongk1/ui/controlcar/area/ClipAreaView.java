package com.mani.car.mekongk1.ui.controlcar.area;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsfunc.static_assistant.SoundHelper;
import com.mani.car.mekongk1.R;

import java.util.ArrayList;

public class ClipAreaView extends RelativeLayout {
    private WheelNumView wheel_num;
    private TextView txtLeft;
    private TextView txtRight;
    private int selectNum = 2;
    public ClipAreaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.clip_area_wheel, this, true);
        wheel_num = (WheelNumView) findViewById(R.id.wheel_num);
        txtLeft = (TextView) findViewById(R.id.txt_left);
        txtRight = (TextView) findViewById(R.id.txt_right);
        ArrayList<String> data = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            data.add("" + i);
        }
        wheel_num.setData(data);
        wheel_num.setSelected(selectNum-2);
        wheel_num.setOnSelectListener(new WheelNumView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectNum = Integer.parseInt(text);
                SoundHelper.playSoundPool(getContext(),R.raw.voice_move_num);
            }
        });
    }
    public void setText(String leftTxt,String rightTxt ){
        txtLeft.setText(leftTxt);
        txtRight.setText(rightTxt);
    }
    public int getSelectNum(){
        return selectNum;
    }
    public void setSelectNum(int selectNum){
        this. selectNum=selectNum;
    }

//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        new android.os.Handler().postDelayed(new Runnable() {
//                                                 @Override
//                                                 public void run() {
//                                                     wheel_num.setSelected(0);
//                                                 }
//                                             },300L);
//
//    }
}
