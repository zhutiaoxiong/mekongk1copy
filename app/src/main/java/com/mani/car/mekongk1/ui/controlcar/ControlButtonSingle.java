package com.mani.car.mekongk1.ui.controlcar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.ManagerControlButtons;
import com.mani.car.mekongk1.model.carlist.DataControlButton;

/**
 * 车身
 */
@Deprecated
public class ControlButtonSingle extends RelativeLayout {
    private Button btn_01, btn_02,btn_03;
    private TextView txt_01, txt_02,txt_03;

    PointF downP = new PointF();//触摸时按下的点
    PointF curP  = new PointF();//触摸时当前的点
    @SuppressLint("ClickableViewAccessibility")
    public ControlButtonSingle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.controlbutton_array, this, true);
        btn_01 = (Button) findViewById(R.id.btn_01);
        btn_02 = (Button) findViewById(R.id.btn_02);
        btn_03 = (Button) findViewById(R.id.btn_03);
        txt_01 = (TextView) findViewById(R.id.txt_01);
        txt_02 = (TextView) findViewById(R.id.txt_02);
        txt_03 = (TextView) findViewById(R.id.txt_03);
    }
    /**viewpager 下传事件 按下*/
    public void OnDownPoint(PointF point) {
        Button downBtn = getPoint2Button(point);
        if(downBtn!=null && downBtn.getVisibility() == VISIBLE)setButtonPressNormal(downBtn);
    }
    /**viewpager 下传事件 抬起*/
    public void OnUpPoint(PointF point) {
        if (btn_01!=null && btn_01.getVisibility() == VISIBLE &&
                ManagerControlButtons.getInstance().isSameBtnsName(txt_01.getText().toString(),105)) {
            setButtonFace2Func(btn_01);
        }else if(btn_01.getVisibility() == VISIBLE){
            setButtonFace2Normal(btn_01);
        }
        if (btn_02!=null && btn_02.getVisibility() == VISIBLE &&
                ManagerControlButtons.getInstance().isSameBtnsName(txt_02.getText().toString(),105)) {
            setButtonFace2Func(btn_02);
        }else if(btn_02.getVisibility() == VISIBLE){
            setButtonFace2Normal(btn_02);
        }
        if (btn_03!=null && btn_03.getVisibility() == VISIBLE &&
                ManagerControlButtons.getInstance().isSameBtnsName(txt_03.getText().toString(),105)) {
            setButtonFace2Func(btn_03);
        }else if(btn_03.getVisibility() == VISIBLE){
            setButtonFace2Normal(btn_03);
        }
    }
    /**viewpager 下传事件*/
    public void OnClickPoint(PointF point) {
        Button downBtn = getPoint2Button(point);
        if(downBtn!=null && downBtn.getVisibility() == VISIBLE){
            String cmdTxt="";
            if(downBtn.equals(btn_01)){
                cmdTxt=txt_01.getText().toString();
            }else if(downBtn.equals(btn_02)){
                cmdTxt=txt_02.getText().toString();
            }else if(downBtn.equals(btn_03)){
                cmdTxt=txt_03.getText().toString();
            }
            DataControlButton dataControlButton= (DataControlButton) downBtn.getTag();
            if(dataControlButton==null)return;
            ControlControlFuncZhu.getInstance().controlButtonPress(dataControlButton.ide,dataControlButton.isUse,this);
        }
    }
    /**
     * name null visible = false
     */
    public void setButtonName(DataControlButton btn1, DataControlButton btn2, DataControlButton btn3) {
        //1
        if (btn1 != null) {
//            btn_01.setText(btn1.name);
            setButtonBackground(btn_01,btn1.ide);
            txt_01.setText(btn1.name);
            btn_01.setTag(btn1);btn_01.setVisibility(VISIBLE);txt_01.setVisibility(VISIBLE);
            if (ManagerControlButtons.getInstance().isSameBtnsName(btn1.name,105)) setButtonFace2Func(btn_01);
            else setButtonFace2Normal(btn_01);
        } else {
            btn_01.setVisibility(INVISIBLE);txt_01.setVisibility(INVISIBLE);
        }
        //2
        if (btn2 != null) {
//            btn_02.setText(btn2.name);
            setButtonBackground(btn_02,btn2.ide);
            txt_02.setText(btn2.name);
            btn_02.setTag(btn2); btn_02.setVisibility(VISIBLE);txt_02.setVisibility(VISIBLE);
            if (ManagerControlButtons.getInstance().isSameBtnsName(btn2.name,105)) setButtonFace2Func(btn_02);
            else setButtonFace2Normal(btn_02);
        } else {
            btn_02.setVisibility(INVISIBLE);txt_02.setVisibility(INVISIBLE);
        }
        //3
        if (btn3 != null) {
//            btn_03.setText(btn3.name);
            setButtonBackground(btn_03,btn3.ide);
            txt_03.setText(btn3.name);
            btn_03.setTag(btn3); btn_03.setVisibility(VISIBLE);txt_03.setVisibility(VISIBLE);
            if (ManagerControlButtons.getInstance().isSameBtnsName(btn3.name,105)) setButtonFace2Func(btn_03);
            else setButtonFace2Normal(btn_03);
        } else{
            btn_03.setVisibility(INVISIBLE);txt_03.setVisibility(INVISIBLE);
        }
    }
    private Button getPoint2Button(PointF point){
        if(point.x > btn_01.getLeft() && point.x < btn_01.getRight()){
            return btn_01;
        }else if(point.x > btn_02.getLeft() && point.x < btn_02.getRight()){
            return btn_02;
        }else if(point.x > btn_03.getLeft() && point.x < btn_03.getRight()){
            return btn_03;
        }
        return null;
    }
    private int getPoint2Pos(PointF point){
        if(point.x > btn_01.getLeft() && point.x < btn_01.getRight()){
            return 1;
        }else if(point.x > btn_02.getLeft() && point.x < btn_02.getRight()){
            return 2;
        }else if(point.x > btn_03.getLeft() && point.x < btn_03.getRight()){
            return 3;
        }
        return -1;
    }
    private void setButtonFace2Func(Button btn){
//                StateListDrawable bt3Bg = ButtonBgStyle.createDrawableSelector(getContext(), BitmapFactory.decodeResource(getResources(), R.drawable.control_fun_normal), BitmapFactory.decodeResource(getResources(), R.drawable.control_press), BitmapFactory.decodeResource(getResources(), R.drawable.control_press));
//        btn.setBackground(getResources().getDrawable(R.drawable.control_fun_normal));
//        btn.setTextColor(Color.parseColor("#000000"));
    }
    private void setButtonFace2Normal(Button btn){
//                StateListDrawable bt3Bg = ButtonBgStyle.createDrawableSelector(getContext(), BitmapFactory.decodeResource(getResources(), R.drawable.control_normal), BitmapFactory.decodeResource(getResources(), R.drawable.control_press), BitmapFactory.decodeResource(getResources(), R.drawable.control_press));
//        btn.setBackground(getResources().getDrawable(R.drawable.control_normal));
//        btn.setTextColor(Color.parseColor("#000000"));
    }
    private void setButtonPressNormal(Button btn){
//        btn.setBackground(getResources().getDrawable(R.drawable.control_press));
    }
    private void setButtonBackground(Button btn, int ide){
        switch (ide){
            case 105:
                btn.setBackground(GlobalContext.getContext().getDrawable(R.drawable.fuc_menu));
                break;
            case 112:
                btn.setBackground(GlobalContext.getContext().getDrawable(R.drawable.fuc_bluetooth));
                break;
            case 107:
                btn.setBackground(GlobalContext.getContext().getDrawable(R.drawable.fuc_borrow_car));
                break;
            case 109:
                btn.setBackground(GlobalContext.getContext().getDrawable(R.drawable.fuc_car_records));
                break;
            case 111:
                btn.setBackground(GlobalContext.getContext().getDrawable(R.drawable.fuc_change_car));
                break;
            case 100:
                btn.setBackground(GlobalContext.getContext().getDrawable(R.drawable.fuc_close_lock));
                break;
            case 103:
                btn.setBackground(GlobalContext.getContext().getDrawable(R.drawable.fuc_laba));
                break;
            case 106:
                btn.setBackground(GlobalContext.getContext().getDrawable(R.drawable.fuc_navi));
                break;
            case 102:
                btn.setBackground(GlobalContext.getContext().getDrawable(R.drawable.fuc_open_lock));
                break;
            case 110:
                btn.setBackground(GlobalContext.getContext().getDrawable(R.drawable.fuc_share_app));
                break;
            case 101:
                btn.setBackground(GlobalContext.getContext().getDrawable(R.drawable.fuc_start));
                break;
            case 104:
                btn.setBackground(GlobalContext.getContext().getDrawable(R.drawable.fuc_tailbox));
                break;
            case 108:
                btn.setBackground(GlobalContext.getContext().getDrawable(R.drawable.fuc_weilan));
                break;
        }
    }
}
