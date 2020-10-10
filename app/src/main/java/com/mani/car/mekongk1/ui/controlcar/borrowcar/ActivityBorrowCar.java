package com.mani.car.mekongk1.ui.controlcar.borrowcar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_system.OConver;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.style.LeftText;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlRegLogin;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.ui.lendcar.ActivityLendChooseDate;

import butterknife.BindView;
import butterknife.ButterKnife;
@Deprecated
@CreatePresenter(BorrowCarPresenter.class)
public class ActivityBorrowCar extends BaseMvpActivity<BorrowCarView, BorrowCarPresenter> implements BorrowCarView,OEventObject {
    @BindView(R.id.temporary_object)
    LeftText temporary_object;
    @BindView(R.id.trust_object)
    LeftText trust_object;
    @BindView(R.id.edit_phone)
    EditText edit_phone;
    @BindView(R.id.img_delete)
    ImageView img_delete;
    @BindView(R.id.txt_tip_content)
    TextView txt_tip_content;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_car);
        ButterKnife.bind(this);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CHANGE_PHONENUM_RESULTBACK,this);
        ODispatcher.addEventListener(OEventName.BORROW_CAR_BACK_CONTROL_CAR_PAGE,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }
    @Override
    protected void onDestroy() {
        ODispatcher.removeEventListener(OEventName.CHANGE_PHONENUM_RESULTBACK,this);
        ODispatcher.removeEventListener(OEventName.BORROW_CAR_BACK_CONTROL_CAR_PAGE,this);
        super.onDestroy();
    }
    @Override
    public void initEvents() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_phone.getText().length() > 0) {
                    img_delete.setVisibility(View.VISIBLE);
                } else {
                    img_delete.setVisibility(View.INVISIBLE);
                }
            }
        };
        edit_phone.addTextChangedListener(watcher);
        img_delete.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                edit_phone.setText("");
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                String phoneNum=edit_phone.getText().toString();
                if(phoneNum.length()==11){
                    OCtrlRegLogin.getInstance().CCMD_1128_checkPhoneNum(phoneNum);
                }else{
                    GlobalContext.popMessage("请输入正确的11位手机号码",getResources().getColor(R.color.normal_bg_color_tip_red));
                }
            }
        });
        temporary_object.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                setIsNeedPhoneNum(0);
                ManagerPublicData.phoneNum="";
                ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityLendChooseDate.class);
            }
        });
        trust_object.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                setIsNeedPhoneNum(1);
            }
        });
    }

    private void setIsNeedPhoneNum(int type) {
        edit_phone.setVisibility(View.INVISIBLE);
        txt_tip_content.setVisibility(View.INVISIBLE);
        btn_confirm.setVisibility(View.INVISIBLE);
        img_delete.setVisibility(View.INVISIBLE);
        if (type == 1) {
            edit_phone.setVisibility(View.VISIBLE);
            txt_tip_content.setVisibility(View.VISIBLE);
            btn_confirm.setVisibility(View.VISIBLE);
            txt_tip_content.setText(OConver.StrToDBC("对方有下载并注册了么控K1账号，可以直接输入\n对方账号，进行信任授权使用车辆。"));
        }
    }

    @Override
    public void initViews() {
        setIsNeedPhoneNum(0);
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.CHANGE_PHONENUM_RESULTBACK)){
            final String result= (String) o;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(result.equals("")){
                        String phoneNum=edit_phone.getText().toString();
                        ManagerPublicData.phoneNum=phoneNum;
                        ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityLendChooseDate.class);
                    }else{
                        GlobalContext.popMessage(result,getResources().getColor(R.color.normal_bg_color_tip_red));
                    }
                }
            });

        }else if(s.equals(OEventName.BORROW_CAR_BACK_CONTROL_CAR_PAGE)){
            finish();
        }
    }
}
