package com.mani.car.mekongk1.ui.controlcar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerControlButtonsZhu;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.carlist.DataControlButton;

import java.util.List;

public class AdapterChangeButtons extends BaseAdapter {
    private Context mContext = null;
    private List<DataControlButton> datas;
    /**
     * @param
     */
    public AdapterChangeButtons(Context ctx) {
        mContext = ctx;
        List<DataControlButton> list = ManagerControlButtonsZhu.getInstance().getCurrentButtonList();
        datas=ManagerControlButtonsZhu.getInstance().getPopList(list);
    }
    public boolean isNoMove(int pos) {
//        if (pos == 5) return true;
        if (pos < 0) return true;
        if (pos >= datas.size()) return true;
//        if (datas.get(pos).status == 0) return true;
        return false;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder        item;
        DataControlButton buttonData = datas.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.controlbutton_simple, parent, false);
            item = new ViewHolder();
            item.btn = (Button) convertView.findViewById(R.id.btn);
            item.textView=(TextView) convertView.findViewById(R.id.txt_text);
            convertView.setTag(item);
//            item.btn.setTag(position);
//            item.textView.setTag(position);
        } else {// 有直接获得ViewHolder
            item = (ViewHolder) convertView.getTag();
        }
        if(buttonData == null || buttonData.name == null){
            GlobalContext.popMessage("功能列表出错",mContext.getResources().getColor(R.color.popTipWarning));
            return convertView;
        }
        item.textView.setText(buttonData.name);
//        setButtonBackgroundNoaml(item.btn,buttonData.name,buttonData.status);
        //是否可移
        setButtonBackgroundNoaml(item.btn,buttonData.ide,buttonData.isUse);
//        if (ManagerControlButtons.getInstance().isSameBtnsName(buttonData.name,105)) {
//            setButtonBackgroundNoaml(item.btn,buttonData.ide,buttonData.isUse);
////            StateListDrawable gnBG = ButtonBgStyle.createDrawableSelector(getContext(), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.control_fun_normal), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.control_press), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.control_press));
////            item.btn.setBackground(gnBG);
////            item.btn.setTextColor(Color.parseColor("#000000"));
//        } else if (buttonData.status == 0) {
////            item.btn.setBackground(getContext().getResources().getDrawable(R.drawable.control_hide_normal));
////            item.btn.setTextColor(Color.parseColor("#FFFFFF"));
////            setButtonBackgroundNotEnable(item.btn,buttonData.ide);
//        } else {
//            setButtonBackgroundNoaml(item.btn,buttonData.ide,buttonData.isUse);
////            StateListDrawable norBG = ButtonBgStyle.createDrawableSelector(getContext(), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.control_normal), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.control_press), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.control_press));
////            item.btn.setBackground(norBG);
////            item.btn.setTextColor(Color.parseColor("#000000"));
//        }
//
// item.btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                int posi = (Integer) v.getTag();
//                int posi = position;
//                if (posi < datas.size()) {
//                    if(posi==5){
//                        PopChangeButtons.getInstance().closePop();
//                    }else{
//                        DataControlButton data = datas.get(posi);
//                        data.status = data.status == 1 ? 0 : 1;
//                        ManagerControlButtons.getInstance().changeSingleButtons(data);
//                        datas = ManagerControlButtons.getInstance().getCurrentButtonList();
//                        notifyDataSetChanged();
//                    }
//                }
//            }
//        });
        item.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("执行循序", "3" );
//                int posi = (Integer) v.getTag();
                int posi = position;
                if (posi < datas.size()) {
                    int id=datas.get(posi).ide;
                    if(id!=103){
                        PopChangeButtons.getInstance().closePop();
                    }
                       ControlControlFuncZhu.getInstance().controlButtonPress(datas.get(posi).ide,datas.get(posi).isUse,item.btn);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public Button btn;
        public TextView textView;
    }
    private void setButtonBackgroundNoaml(Button btn,int ide,int isUse){
       DataCarInfo  currentCar= ManagerCarList.getInstance().getCurrentCar();
        switch (ide){
            case 105:
                btn.setBackground(mContext.getDrawable(R.drawable.fuc_menu));
                break;
            case 112:
                btn.setBackground(mContext.getDrawable(R.drawable.fuc_bluetooth));
                break;
            case 107:
                if(isUse==1){
                    btn.setBackground(mContext.getDrawable(R.drawable.fuc_cancle_borrow_car));
                }else{
                    btn.setBackground(mContext.getDrawable(R.drawable.fuc_borrow_car));
                }
                break;
            case 109:
                btn.setBackground(mContext.getDrawable(R.drawable.fuc_car_records));
                break;
            case 111:
                btn.setBackground(mContext.getDrawable(R.drawable.fuc_change_car));
                break;
            case 100:
                btn.setBackground(mContext.getDrawable(R.drawable.fuc_close_lock));
                break;
            case 103:
                btn.setBackground(mContext.getDrawable(R.drawable.fuc_laba));
                break;
            case 106:
                btn.setBackground(mContext.getDrawable(R.drawable.fuc_navi));
                break;
            case 102:
                btn.setBackground(mContext.getDrawable(R.drawable.fuc_open_lock));
                break;
            case 110:
                btn.setBackground(mContext.getDrawable(R.drawable.fuc_share_app));
                break;
            case 101:
                btn.setBackground(mContext.getDrawable(R.drawable.fuc_start));
                break;
            case 104:
                btn.setBackground(mContext.getDrawable(R.drawable.fuc_tailbox));
                break;
            case 108:
                if(isUse==1){
                    btn.setBackground(mContext.getDrawable(R.drawable.fuc_weilan_cancle));
                }else{
                    btn.setBackground(mContext.getDrawable(R.drawable.fuc_weilan));
                }
//                btn.setBackground(mContext.getDrawable(R.drawable.fuc_weilan));
                break;
            case 113:
                if(isUse==1){
                    btn.setBackground(mContext.getDrawable(R.drawable.fuc_cancle_temporary));
                }else{
                    btn.setBackground(mContext.getDrawable(R.drawable.fuc_temporary));
                }
                break;
        }
    }
    //[{"ide":107,"name":"信任授权","status":1,"isUse":0},
    // {"ide":103,"name":"寻车","status":1,"isUse":0},
    // {"ide":106,"name":"导航找车","status":0,"isUse":0},
    // {"ide":101,"name":"启动","status":0,"isUse":0},
    // {"ide":111,"name":"切换车辆","status":0,"isUse":0},
    // {"ide":105,"name":"功能开关","status":1,"isUse":0},
    // {"ide":104,"name":"尾箱","status":0,"isUse":0},
    // {"ide":108,"name":"电子围栏","status":0,"isUse":0},
    // {"ide":102,"name":"开锁","status":0,"isUse":0},
    // {"ide":100,"name":"关锁","status":0,"isUse":0},
    // {"ide":109,"name":"行车记录","status":0,"isUse":0},
    // {"ide":110,"name":"分享APP","status":0,"isUse":0}]
    private void setButtonBackgroundNotEnable(Button btn,int ide){
        switch (ide){
//            case 112:
//                btn.setBackground(mContext.getDrawable(R.drawable.fuc_bluetooth_close));
//                break;
//            case 107:
//                btn.setBackground(mContext.getDrawable(R.drawable.fuc_borrow_car_close));
//                break;
//            case 109:
//                btn.setBackground(mContext.getDrawable(R.drawable.fuc_car_records_close));
//                break;
//            case 111:
//                btn.setBackground(mContext.getDrawable(R.drawable.fuc_change_car_close));
//                break;
//            case 100:
//                btn.setBackground(mContext.getDrawable(R.drawable.fuc_close_lock_close));
//                break;
//            case 103:
//                btn.setBackground(mContext.getDrawable(R.drawable.fuc_laba_close));
//                break;
//            case 106:
//                btn.setBackground(mContext.getDrawable(R.drawable.fuc_navi_close));
//                break;
//            case 102:
//                btn.setBackground(mContext.getDrawable(R.drawable.fuc_open_lock_close));
//                break;
//            case 110:
//                btn.setBackground(mContext.getDrawable(R.drawable.fuc_share_app_close));
//                break;
//            case 101:
//                btn.setBackground(mContext.getDrawable(R.drawable.fuc_start_close));
//                break;
//            case 104:
//                btn.setBackground(mContext.getDrawable(R.drawable.fuc_tailbox_close));
//                break;
//            case 108:
//                btn.setBackground(mContext.getDrawable(R.drawable.fuc_weilan_close));
//                break;
        }
    }
}