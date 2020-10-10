package com.mani.car.mekongk1.ui.personcenter.carmanager.recharge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.style.CenterImgAndTxt;
import com.kulala.staticsview.style.TopTxtBottomTxtRightCornorImg;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.global.OWXShare;
import com.mani.car.mekongk1.ctrl.OCtrlCommon;
import com.mani.car.mekongk1.model.ManagerCommon;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.common.DataPayWay;
import com.mani.car.mekongk1.ui.personcenter.know_product.online.OnlineMessageView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**充值服务页面
 * 充值成功后跳转到车辆管理页面 并弹出提示框”充值成功“文字提醒"
 * 充值失败的话，则还在充值页面 并弹出提示框”充值失败，请稍后再试“
 * */
@CreatePresenter(RechargePresenter.class)
public class ActivityRecharge extends BaseMvpActivity<RechargeView,RechargePresenter>  implements OnlineMessageView ,OEventObject{
    @BindView(R.id.car_num)
    TextView car_num;
    @BindView(R.id.service_time)
    TextView service_time;
    @BindView(R.id.one_year)
    TopTxtBottomTxtRightCornorImg one_year;
    @BindView(R.id.two_year)
    TopTxtBottomTxtRightCornorImg two_year;
    @BindView(R.id.three_year)
    TopTxtBottomTxtRightCornorImg three_year;
    @BindView(R.id.ali_pay)
    CenterImgAndTxt ali_pay;
    @BindView(R.id.wechat_pay)
    CenterImgAndTxt wechat_pay;
    public static DataCarInfo data;
    private float fee=180;
    private int time=13;
    private List<DataPayWay> payWayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.PAY_CHECKPAY_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.PAY_WX_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.PAY_WX_SUCESS, this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initEvents() {
        one_year.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    setOtherInvisible(1);
                    fee=180;
                    if(payWayList!=null&&payWayList.size()>0){
                        time=payWayList.get(3).time;
                    }
                }
                return false;
            }
        });
        two_year.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    setOtherInvisible(2);
                    fee=360;
                    if(payWayList!=null&&payWayList.size()>0){
                        time=payWayList.get(1).time;
                    }
                }
                return false;
            }
        });
        three_year.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    setOtherInvisible(3);
                    fee=540;
                    if(payWayList!=null&&payWayList.size()>0){
                        time=payWayList.get(0).time;
                    }
                }
                return false;
            }
        });
        ali_pay.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if(data==null||data.ide==0)return;
                OCtrlCommon.getInstance().ccmd1002_checkpay(fee, data.ide, time);
            }
        });
        wechat_pay.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if(data==null||data.ide==0)return;
                OCtrlCommon.getInstance().ccmd1001_wxpay(fee, data.ide,time);
                ManagerPublicData.isNotPopGusture =true;
            }
        });
    }



    public void setOtherInvisible(int which){
        one_year.view_background.setSelected(false);
        two_year.view_background.setSelected(false);
        three_year.view_background.setSelected(false);
        one_year.view_background.setPressed(false);
        two_year.view_background.setPressed(false);
        three_year.view_background.setPressed(false);
        one_year.img_right.setVisibility(View.INVISIBLE);
        three_year.img_right.setVisibility(View.INVISIBLE);
        two_year.img_right.setVisibility(View.INVISIBLE);
        switch (which){
            case 1:
                one_year.view_background.setSelected(true);
                one_year.view_background.setPressed(false);
                one_year.img_right.setVisibility(View.VISIBLE);
                break;
            case 2:
                two_year.view_background.setSelected(true);
                two_year.view_background.setPressed(false);
                two_year.img_right.setVisibility(View.VISIBLE);
                break;
            case 3:
                three_year.view_background.setSelected(true);
                three_year.view_background.setPressed(false);
                three_year.img_right.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void initViews() {
        payWayList = ManagerCommon.getInstance().payWayList;
        if(payWayList!=null&&payWayList.size()>0){
            time=payWayList.get(3).time;
        }
        setOtherInvisible(1);
        if(data==null)return;
        car_num.setText(data.num);
        if (data.isActive == 1) {
            service_time.setText("服务到期日" + ODateTime.time2StringOnlyDate(data.endTime));
        } else {
            service_time.setText("服务到期日" + "-年-月-日");
        }
    }
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo          = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void receiveEvent(String eventName, Object paramObj) {
        if (eventName.equals(OEventName.PAY_CHECKPAY_RESULTBACK)) {
            final String payStr = (String) paramObj;
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(GlobalContext.getCurrentActivity());
                    // 调用支付接口，获取支付结果
                    // resultStatus={9000};memo={};result={_input_charset="utf-8"&body="么控K1充值服务"&notify_url="http%3A%2F%2F120.27.137.20%3A8099%2FpayGateway%2Fpayway%2Falipay%2Fsdk%2Fnotify_url.jsp"&out_trade_no="20913"&partner="2088221499108097"&payment_type="1"&seller_id="3098057476@qq.com"&service="mobile.securitypay.pay"&subject="充值"&total_fee="0.01"&success="true"&sign_type="RSA"&sign="dPg3i9ShlEOMd3m0O1BEglUV+rZ6Ga7HbFXTBkHAkVFfuILDSCSWWzMU5yJ1UYvdgQuZWZyuq+ZJFbD62api9QatuP00BHq5qrkigAbRVi+CQbAp06Bwa2gPiph1RdO43zW+JeGarLh6+1ps4862nusgAkvLKLJdDhdyOtUI2zY="}
                    String   result    = alipay.pay(payStr, true);
                    String[] resultArr = result.split(";");
                    if (resultArr != null && resultArr.length > 1 && "resultStatus={9000}".equals(resultArr[0])) {
                        ODispatcher.dispatchEvent(OEventName.PAY_SUCSESS);
                        finish();
                    } else if ("resultStatus={4000}".equals(resultArr[0])) {
                        showTips("您未安装支付宝或已取消支付!");
                    }else if ("resultStatus={6001}".equals(resultArr[0])) {

                    }else  {
                        showTips("充值失败,请稍后再试");
                    }
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }else  if (eventName.equals(OEventName.PAY_WX_RESULTBACK)) {
            if (!isWeixinAvilible(ActivityRecharge.this)) {
                showTips("请先安装微信软件!");
                return;
            }
            IWXAPI api;
            // 实例化
            try {
                api = WXAPIFactory.createWXAPI(GlobalContext.getContext(), OWXShare.getAppIdWX());
                api.registerApp(OWXShare.getAppIdWX());
                PayReq request = new PayReq();
                request.appId = OWXShare.getAppIdWX();
                request.partnerId = ManagerCommon.wxpayInfo.partnerid;
                request.prepayId = ManagerCommon.wxpayInfo.prepayId;
                request.packageValue = ManagerCommon.wxpayInfo.packageValue;
                request.nonceStr = ManagerCommon.wxpayInfo.nonceStr;
                request.timeStamp = ManagerCommon.wxpayInfo.timestamp;
                request.sign = ManagerCommon.wxpayInfo.sign;
                api.sendReq(request);
            } catch (PackageManager.NameNotFoundException e) {
                showTips("充值失败,请稍后再试!");
                e.printStackTrace();
            }
        }else  if(eventName.equals(OEventName.PAY_WX_SUCESS)){
            ODispatcher.dispatchEvent(OEventName.PAY_SUCSESS);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ODispatcher.removeEventListener(OEventName.PAY_CHECKPAY_RESULTBACK,this);
        ODispatcher.removeEventListener(OEventName.PAY_WX_RESULTBACK,this);
        ODispatcher.removeEventListener(OEventName.PAY_WX_SUCESS, this);
    }
    public void showTips( String tip){
     GlobalContext.popMessage(tip,getResources().getColor(R.color.normal_bg_color_tip_red));
    }
}
