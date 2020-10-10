package com.mani.car.mekongk1.ui.personcenter.carmanager.carmodule;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
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
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlCar;
import com.mani.car.mekongk1.ctrl.OCtrlRegLogin;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.ui.personcenter.know_product.ActivityKnowProduct;
import com.mani.car.mekongk1.ui.personcenter.know_product.online.OnlineMessageView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;


/**模组绑定解绑页面
 * 绑定
 * 点击输入框弹出纯数字键盘
 * 点击了解产品跳转到了解产品界面
 * 点击返回跳转到车辆管理页面
 * 1输入数字后在底部出现绑定按钮
 * 2如果绑定失败，则出现提示框提示 ' 绑定失败"
 * 3如果绑定成功则直接返回车辆管理页面 弹出提示框 ”绑定成功“ 回到车辆管理页面显示解绑模组
 *
 *  解绑
 *  1用户输入密码，显示*号，解绑按钮变成绿色，不输入的时候变成灰绿色
 *  2点击解绑  ①如果成功则提示框”解绑成功“ ② 失败 提示框 ”密码错误"
 *  3 解绑成功后车辆管理页面由 解绑模组变成绑定模组 凡是没有绑定模组的在车辆管理右上角出现垃圾桶图标 点击可删除卡片
 * */
@CreatePresenter(CarMoudulePresenter.class)
public class ActivityCarMoudule extends BaseMvpActivity<CarMouduleView,CarMoudulePresenter>  implements OnlineMessageView ,OEventObject{
    @BindView(R.id.img_carmoudle)
    ImageView img_carmoudle;
    @BindView(R.id.title)
    ClipTitleHead title;
    @BindView(R.id.txt_carmoudle)
    EditText txt_carmoudle;
    @BindView(R.id.btn_confirm)       TextView btn_confirm;
    @BindView(R.id.txt_tip)        TextView txt_tip;
    @BindView(R.id.txt_tip_content)        TextView txt_tip_content;
    @BindView(R.id.txt_koow_product)        TextView txt_koow_product;
    private String carMoudleName;
    public static DataCarInfo carInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_moudle);
        ButterKnife.bind(this);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.CHECK_PASSWORD_RESULTBACK,this);
        ODispatcher.addEventListener(OEventName.CAR_ACTIVATE_SUCESS,this);
        ODispatcher.addEventListener(OEventName.CAR_UNACTIVATE_SUCESS,this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ODispatcher.removeEventListener(OEventName.CHECK_PASSWORD_RESULTBACK,this);
        ODispatcher.removeEventListener(OEventName.CAR_ACTIVATE_SUCESS,this);
        ODispatcher.removeEventListener(OEventName.CAR_UNACTIVATE_SUCESS,this);
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
                 carMoudleName = txt_carmoudle.getText().toString();
                if (carMoudleName.length() >= 1) {
                    btn_confirm.setEnabled(true);
                } else {
                    btn_confirm.setEnabled(false);
                }
            }
        };
        txt_carmoudle.addTextChangedListener(watcher);
        btn_confirm.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if(carInfo==null)return;
                if(carInfo.isActive==0){
                    OCtrlCar.getInstance().ccmd1204_activatecar(carInfo.ide, carMoudleName);
                }else{
                    OCtrlRegLogin.getInstance().CCMD_1104_checkPassword(carMoudleName);
                }
            }
        });
        txt_koow_product.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityCarMoudule.this, ActivityKnowProduct.class);
            }
        });
    }

    @Override
    public void initViews() {
        if(carInfo==null)return;
        if(carInfo.isActive==1){
            img_carmoudle.setImageResource(R.drawable.img_un_bind);
            btn_confirm.setSelected(true);
            btn_confirm.setText("解绑");
            txt_carmoudle.setHint("请输入APP登陆密码");
            txt_tip.setText("解绑须知");
            txt_carmoudle.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            txt_tip_content.setText("解绑模组后将无法控制此车辆。");
            title.txt_title.setText("解绑模组");
        }else{
            img_carmoudle.setImageResource(R.drawable.img_bind);
            btn_confirm.setSelected(false);
            txt_carmoudle.setHint("请输入模组号绑定");
            txt_tip.setText("绑定须知");
            btn_confirm.setText("绑定");
            txt_carmoudle.setInputType(InputType.TYPE_CLASS_NUMBER);
            txt_tip_content.setText(OConver.StrToDBC("1.用户购买产品后，由安装工程师操作绑定；\n2.设备需要接收到GPS及GSM信号后才能被绑定；\n3.确认安全的前提下将车辆启动一次 ；\n4.将车辆移至能够接收GPS信号的户外； \n5.接收信号将耗时几分钟，请耐心等待。"));
            title.txt_title.setText("绑定模组");
        }
        txt_koow_product.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.CAR_ACTIVATE_SUCESS)) {
            String tips= (String) o;
            if(tips.equals("")){
//                handleShowTips("绑定成功",1);
                finish();//finish了还看个毛
            }else{
                handleShowTips(tips,2);
            }
        }else if (s.equals(OEventName.CAR_UNACTIVATE_SUCESS)) {
            String tips= (String) o;
            if(tips.equals("")){
//                handleShowTips("解绑成功",1);
//                finish();//finish了还看个毛
                ActivityUtils.startActivity(ActivityCarMoudule.this,ActivityCaUnActiveSuccess.class);
            }else{
                handleShowTips(tips,2);
            }
        }else if (s.equals(OEventName.CHECK_PASSWORD_RESULTBACK)) {
            boolean check = (Boolean) o;
            if (check) {
                 ManagerPublicData.terminalNum=carInfo.terminalNum;
                 OCtrlCar.getInstance().ccmd1220_unactivatecar(carInfo.ide);
            }else{
                handleShowTips("密码错误",2);
            }
        }
    }
    private final ActivityCarMoudule.MyHandler handler=new ActivityCarMoudule.MyHandler(ActivityCarMoudule.this);
    private static class MyHandler extends Handler {
        private final WeakReference<ActivityCarMoudule> mActivity;
        public MyHandler(ActivityCarMoudule activity) {
            mActivity = new WeakReference<ActivityCarMoudule>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ActivityCarMoudule activityCarMoudle=mActivity.get();
            if(activityCarMoudle!=null){
                if(msg.what==110){
                    /**隐藏进度条*/
                    String tips= (String) msg.obj;
                    int result=msg.arg1;
                    if(result==1){
                        GlobalContext.popMessage(tips,activityCarMoudle.getResources().getColor(R.color.normal_txt_color_cyan));
                    }else     if(result==2){
                        GlobalContext.popMessage(tips,activityCarMoudle.getResources().getColor(R.color.normal_bg_color_tip_red));
                    }
                }
            }
        }
    }


    private void handleShowTips(String tips,int result){
        Message message=Message.obtain();
        message.what=110;
        message.obj=tips;
        message.arg1=result;
        handler.sendMessage(message);
    }

}
