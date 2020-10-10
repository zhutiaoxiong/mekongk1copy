package com.mani.car.mekongk1.ui.personcenter.know_product.online;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.style.LeftTextRightEdit;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlCommon;

import butterknife.BindView;
import butterknife.ButterKnife;


/**主页*/
@CreatePresenter(OnlineMessagePresenter.class)
public class ActivityOnlineMessage extends BaseMvpActivity<OnlineMessageView,OnlineMessagePresenter>  implements OnlineMessageView ,OEventObject{
    @BindView(R.id.name)
    LeftTextRightEdit name;
    @BindView(R.id.contact)
    LeftTextRightEdit contact;
    @BindView(R.id.city)
    LeftTextRightEdit city;
    @BindView(R.id.brand)
    LeftTextRightEdit brand;
    @BindView(R.id.series)
    LeftTextRightEdit series;
    @BindView(R.id.time)
    LeftTextRightEdit time;
    @BindView(R.id.btn_confirm)
    TextView btn_confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_message);
        ButterKnife.bind(this);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.ONLINE_MESSAGE_RESULT,this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ODispatcher.removeEventListener(OEventName.ONLINE_MESSAGE_RESULT,this);
    }

    @Override
    public void initEvents() {
        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String userName=name.txt_center.getText().toString();
                String phoneNum=contact.txt_center.getText().toString();
                String cityName=city.txt_center.getText().toString();
                String brandName=brand.txt_center.getText().toString();
                String seriesName=series.txt_center.getText().toString();
                String timeString=time.txt_center.getText().toString();
                if(userName!=null&&userName.length()>0&&phoneNum!=null&&phoneNum.length()>0&&cityName!=null&&cityName.length()>0&&brandName!=null&&brandName.length()>0&&seriesName!=null&&seriesName.length()>0&&timeString!=null&&timeString.length()>0){
                    btn_confirm.setEnabled(true);
                }else{
                    btn_confirm.setEnabled(false);
                }
            }
        };
        name.txt_center.addTextChangedListener(textWatcher);
        contact.txt_center.addTextChangedListener(textWatcher);
        city.txt_center.addTextChangedListener(textWatcher);
        brand.txt_center.addTextChangedListener(textWatcher);
        series.txt_center.addTextChangedListener(textWatcher);
        time.txt_center.addTextChangedListener(textWatcher);
        btn_confirm.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                //String name, String phone,String city ,String brand ,String series, String time
                String userName=name.txt_center.getText().toString();
                String phoneNum=contact.txt_center.getText().toString();
                String cityName=city.txt_center.getText().toString();
                String brandName=brand.txt_center.getText().toString();
                String seriesName=series.txt_center.getText().toString();
                String timeString=time.txt_center.getText().toString();
                OCtrlCommon.getInstance().ccmd1318_OnlineMessage(userName,phoneNum,cityName,brandName,seriesName,timeString);
            }
        });
    }


    @Override
    public void initViews() {
        contact.txt_center.setInputType(InputType.TYPE_CLASS_PHONE);
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.ONLINE_MESSAGE_RESULT)){
            final String result= (String) o;
            if(!result.equals("")){
                GlobalContext.popMessage(result,getResources().getColor(R.color.normal_bg_color_tip_red));
            }else{
                finish();
            }
        }
    }
}
