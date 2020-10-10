package com.mani.car.mekongk1.ui.personcenter.carmanager.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.style.LeftText;
import com.kulala.tools.utils.AToBigA;
import com.kulala.tools.utils.ActivityUtils;
import com.kulala.tools.utils.EditTextUtils;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlCar;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.ui.personcenter.carmanager.appearance.ActivityAppearance;
import com.mani.car.mekongk1.ui.personcenter.carmanager.carmodule.ActivityCarMoudule;
import com.mani.car.mekongk1.ui.personcenter.carmanager.manager.carbrand.brand.MyBrandActivity;
import com.mani.car.mekongk1.ui.personcenter.carmanager.manager.carseries.ActivityCarSeries;
import com.mani.car.mekongk1.ui.personcenter.carmanager.message.ActivityMessageSet;
import com.mani.car.mekongk1.ui.personcenter.carmanager.recharge.ActivityRecharge;
import com.mani.car.mekongk1.ui.personcenter.carmanager.voice.ActivityVoiceSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CarmanagerItem extends RelativeLayout {
    @BindView(R.id.car_brand)
    TextView car_brand;
    @BindView(R.id.car_num)
    EditText car_num;
    @BindView(R.id.img_car_logo)
    ImageView img_car_logo;
    @BindView(R.id.img_car_selected)
    ImageView img_car_selected;
    @BindView(R.id.img_suv_selected)
    ImageView img_suv_selected;
    @BindView(R.id.img_chahao)
    ImageView img_chahao;
    @BindView(R.id.service_time)
    LeftText service_time;
    @BindView(R.id.car_moudle)
    LeftText car_moudle;
//    @BindView(R.id.car_blue)
//    LeftText car_blue;
    @BindView(R.id.change_voice)
    LeftText change_voice;
    @BindView(R.id.car_appearance)
    LeftText car_appearance;
    @BindView(R.id.message_tip)
    LeftText message_tip;
    @BindView(R.id.recharge_service)
    LeftText recharge_service;
    @BindView(R.id.txt_select_car)
    TextView txt_select_car;
    private DataCarInfo carInfo;
    private MyHandler handler = new MyHandler();


    public CarmanagerItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.carmanager_itemm, this, true);
        ButterKnife.bind(this);
//        setEditTextInhibitInputSpeChat(car_num);
        car_num.setTransformationMethod(new AToBigA());
        initevent();
    }
    public void setData(DataCarInfo data) {
        if (data == null) return;
        carInfo = data;
//        Log.e("ChangeCurCar","setData:"+carInfo.num+ " id:"+carInfo.ide);
        car_brand.setText(data.brand);
        car_num.setText(data.num);
        if (data.isActive == 1) {
            service_time.txt_left.setText("服务到期日" + ODateTime.time2StringOnlyDate(data.endTime));

        } else {
            service_time.txt_left.setText("服务到期日" + "-年-月-日");
        }
        if (data.isActive == 0) {
            img_chahao.setVisibility(View.VISIBLE);
            car_moudle.txt_left.setText("绑定模组");
        } else {
            img_chahao.setVisibility(View.INVISIBLE);
            car_moudle.txt_left.setText("解绑模组");
        }
        if(data.carType==1){
            img_car_selected.setSelected(true);
            img_suv_selected.setSelected(false);
        }else if(data.carType==2){
            img_suv_selected.setSelected(true);
            img_car_selected.setSelected(false);
        }
        if(data.logo!=null&&!data.logo.equals("")){
            Glide.with(getContext()).load(data.logo)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource,
                                                    GlideAnimation<? super GlideDrawable> glideAnimation) {
                            img_car_logo.setImageDrawable(resource);
                        }
                    });
        }
        handler.obtainMessage(HANDLER_CHANGE_COVER_DISPLAY).sendToTarget();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initevent() {
        car_num.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String string = car_num.getText().toString().toUpperCase();
                if(carInfo.num.equals(string)) {//无改变
                    return;
                }else if (string.length() <=6) {
                    if(carInfo.isMyCar==0){
                        car_num.setText(carInfo.num);
                        return;
                    }
                    GlobalContext.popMessage("车牌号输入最少7位!",getResources().getColor(R.color.popTipWarning));
                }else if (!EditTextUtils.plateNumRuleMatch(string)) {
                    if(carInfo.isMyCar==0){
                        car_num.setText(carInfo.num);
                        return;
                    }
                    GlobalContext.popMessage("车牌号格式不正确!",getResources().getColor(R.color.popTipWarning));
                }else{
                    if(carInfo.isMyCar==0){
                        car_num.setText(carInfo.num);
                        GlobalContext.popMessage("副车主无权修改",getResources().getColor(R.color.popTipWarning));
                        return;
                    }
                    carInfo.num = string;
                    OCtrlCar.getInstance().ccmd1201_newrepairCar(carInfo);
                }
                car_num.setText(carInfo.num);
            }
        });
        img_car_selected.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(carInfo.isMyCar==0){
                        GlobalContext.popMessage("副车主无权修改",getResources().getColor(R.color.popTipWarning));
                        return false;
                    }
                        img_car_selected.setSelected(true);
                        img_suv_selected.setSelected(false);
                        if(carInfo!=null){
                            carInfo.carType=1;
                            OCtrlCar.getInstance().ccmd1201_newrepairCar(carInfo);
                        }
                        break;
                }
                return false;
            }
        });
        img_suv_selected.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(carInfo.isMyCar==0){
                        GlobalContext.popMessage("副车主无权修改",getResources().getColor(R.color.popTipWarning));
                        return false;
                    }
                    img_suv_selected.setSelected(true);
                    img_car_selected.setSelected(false);
                    if(carInfo!=null){
                        carInfo.carType=2;
                        OCtrlCar.getInstance().ccmd1201_newrepairCar(carInfo);
                    }
                }
                return false;
            }
        });

        /**如果当前文字是绑定模组，则跳转到模组绑定页面，
         * 如果当前文字是解绑模组，1则要先判断是否解绑蓝牙，没有的话弹出红色提示框提示”解绑模组前，请先解绑蓝牙“，
         *                         2如果已经解绑的话则跳转到模组解绑页面*/
        car_moudle.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(carInfo.isMyCar==0){
                    GlobalContext.popMessage("副车主无权修改",getResources().getColor(R.color.popTipWarning));
                    return;
                }
                if(carInfo!=null){
                    ActivityCarMoudule.carInfo=carInfo;
                    ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityCarMoudule.class);
                }
            }
        });
//        car_blue.setOnClickListener(new OnClickListenerMy() {
//            @Override
//            public void onClickNoFast(View v) {
//                ActivityUtils.startActivityTakeData(GlobalContext.getCurrentActivity(), ActivityCarBlue.class,carInfo.ide);
//            }
//        });

        change_voice.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(carInfo.isMyCar==0){
                    GlobalContext.popMessage("副车主无权修改",getResources().getColor(R.color.popTipWarning));
                    return;
                }
                ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityVoiceSet.class);
            }
        });
        car_appearance.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(carInfo.isMyCar==0){
                    GlobalContext.popMessage("副车主无权修改",getResources().getColor(R.color.popTipWarning));
                    return;
                }
                ActivityUtils.startActivityTakeData(GlobalContext.getCurrentActivity(), ActivityAppearance.class,carInfo.ide);
            }
        });
        message_tip.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(carInfo.isMyCar==0){
                    GlobalContext.popMessage("副车主无权修改",getResources().getColor(R.color.popTipWarning));
                    return;
                }
                ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityMessageSet.class);
            }
        });
        recharge_service.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(carInfo.isMyCar==0){
                    GlobalContext.popMessage("副车主无权修改",getResources().getColor(R.color.popTipWarning));
                    return;
                }
                ActivityRecharge.data=carInfo;
                ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityRecharge.class);
            }
        });
        img_car_logo.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(carInfo.isMyCar==0){
                    GlobalContext.popMessage("副车主无权修改",getResources().getColor(R.color.popTipWarning));
                    return;
                }
                MyBrandActivity.data=carInfo;
                MyBrandActivity.fromPage=1;
                ActivityCarSeries.from=1;
                ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), MyBrandActivity.class);
            }
        });
        car_brand.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(carInfo.isMyCar==0){
                    GlobalContext.popMessage("副车主无权修改",getResources().getColor(R.color.popTipWarning));
                    return;
                }
                ActivityCarSeries.data=carInfo;
                MyBrandActivity.fromPage=1;
                ActivityCarSeries.from=1;
                if(carInfo!=null){
                    ActivityUtils.startActivityTakeOneData(GlobalContext.getCurrentActivity(), ActivityCarSeries.class,"slecteCarBrand",carInfo.brand);
                }
            }
        });
        txt_select_car.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ManagerCarList.getInstance().setCurrentCar(carInfo.ide);
            }
        });
        img_chahao.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(carInfo!=null){
                    OCtrlCar.getInstance().ccmd1202_deletecar(carInfo.ide);
                }
            }
        });
    }
    public void setSelected(boolean selected){
        if(selected){

        }else{

        }
    }


    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？·~《》_<>]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }
    public static final int HANDLER_CHANGE_COVER_DISPLAY = 1000;

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_CHANGE_COVER_DISPLAY:
                    if(carInfo.ide == ManagerCarList.getInstance().getCurrentCarID()){
                        txt_select_car.setText("已选择此车辆");
                        txt_select_car.setBackground(GlobalContext.getContext().getResources().getDrawable(R.drawable.white_press));
                    }else{
                        txt_select_car.setText("选择此车辆");
                        txt_select_car.setBackground(GlobalContext.getContext().getResources().getDrawable(R.drawable.white_nomal));
                    }
                    break;
            }
        }
    }
}
