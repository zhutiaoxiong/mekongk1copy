package com.mani.car.mekongk1.ui.controlcar.borrowcar.borrowsuccess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.OToastSharePath;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.global.OWXShare;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.loginreg.DataUser;
import com.mani.car.mekongk1.tencent.tauth.TencentCommon;
import com.mani.car.mekongk1.wxapi.WXEntryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(BorrowSuccessPresenter.class)
public class ActivityBorrowSuccess extends BaseMvpActivity<BorrowSuccessView, BorrowSuccessPresenter> implements BorrowSuccessView,OEventObject {
    @BindView(R.id.txt_tip_content)
    TextView txt_tip_content;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;
    @BindView(R.id.title_head)
    ClipTitleHead title_head;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_success);
        ButterKnife.bind(this);
        initViews();
        initEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ManagerPublicData.isNotPopGusture =false;
    }
    @Override
    public void initEvents() {
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastSharePath.getInstance().show(txt_tip_content, "分享", new OToastSharePath.OnClickButtonListener() {
                    @Override
                    public void onClick(int pos) {
                        String shareUrl="";
                       if(ManagerPublicData.phoneNum==null||ManagerPublicData.phoneNum.equals("")){
                           //临时授权
                            String authCode=   ManagerPublicData.authCode;//临时授权码
                           shareUrl="http://api.91kulala.com/kulala/borrow/temporary_control.jsp?authCode="+authCode;
                       }else {
                           long carId=ManagerCarList.getInstance().getCurrentCarID();
                           if(carId==0)return;
                           DataUser dataUser=ManagerLoginReg.getInstance().getCurrentUser();
                           if(dataUser==null)return;
                           long userId= dataUser.userId;
                           if(userId==0)return;
                           shareUrl="http://api.91kulala.com/kulala/borrow/notice.jsp?carId="+carId+"&userId="+userId;
                       }
                        if(pos==1){
//                            DataShare shareInfo = ManagerCommon.getInstance().getShareInfo();
//                            if(shareInfo ==null)return;
                                WXEntryActivity.NEED_WXSHARE_RESULT = true;
                                OWXShare.ShareFriendURL("有辆车已经借给您了","请点击了解详细内容",shareUrl);
                                ManagerPublicData.isNotPopGusture =true;
                        }else if(pos==2){
                            OWXShare.ShareTimeLineURL("有辆车已经借给您了","请点击了解详细内容",shareUrl);
                            ManagerPublicData.isNotPopGusture =true;
                        }else if(pos==3){
                            TencentCommon.toTencent(ActivityBorrowSuccess.this,"有辆车已经借给您了","请点击了解详细内容",shareUrl,0,"");
                            ManagerPublicData.isNotPopGusture =true;
                        }
                    }
                });
            }
        });

        title_head.img_left.setOnClickListener( new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
              ODispatcher.dispatchEvent(OEventName.BORROW_CAR_BACK_CONTROL_CAR_PAGE);
              finish();
            }
        });
    }

    @Override
    public void initViews() {
        DataCarInfo carInfo=ManagerCarList.getInstance().getCurrentCar();
        if(carInfo==null)return;
        String carNum=carInfo.num;
        long authorityEndTime1=carInfo.authorityEndTime1;
        int hour=(int)((authorityEndTime1-System.currentTimeMillis())/(60*60*1000));
        if(carNum==null||carNum.equals(""))return;
        if(ManagerPublicData.isBorrowCarLongTime){
            title_head.setTitle("生成成功");
            btn_confirm.setText("发送通知");
            if(!ManagerPublicData.foreverTime.equals("")){
            String str="您的爱车"+"<font color='#FF0000'>"+carNum+"</font>"+"现在已经产生了虚拟钥匙，授权期限是"+"<font color='#FF0000'>"+ManagerPublicData.foreverTime+"</font>"+"性的。对方需要登陆么控K1APP后才能够使用。" ;
            txt_tip_content.setText(android.text.Html.fromHtml(str));
//                txt_tip_content.setText("您的爱车"+carNum+"现在已经产生了虚拟钥匙，授权期限是"+ManagerPublicData.foreverTime+",过期自动失效。对方收到后只能用手机开关锁。同时也要提醒对方不要把虚拟钥匙发送给其他人使用。" );
            }else{
             String str="您的爱车"+"<font color='#FF0000'>"+carNum+"</font>"+"现在已经产生了虚拟钥匙，授权期限是"+"<font color='#FF0000'>"+ManagerPublicData.mystartTime+"</font>"+"至"+"<font color='#FF0000'>"+ManagerPublicData.endTime+"</font>"+"止,过期自动失效。对方收到后只能用手机开关锁。同时也要提醒对方不要把虚拟钥匙发送给其他人使用。" ;
           txt_tip_content.setText(android.text.Html.fromHtml(str));
//        txt_tip_content.setText("您的爱车"+carNum+"现在已经产生了虚拟钥匙，授权期限是" +ManagerPublicData.mystartTime+"至"+ManagerPublicData.endTime+"止，过期自动失效。对方收到后只能用手机开关锁。同时也要提醒对方不要把虚拟钥匙发送给其他人使用。");
            }
        }else{
            title_head.setTitle("生成成功");
            btn_confirm.setText("发送钥匙");
            String str="您的爱车"+"<font color='#FF0000'>"+carNum+"</font>"+"现在已经产生了虚拟钥匙，授权期限是"+"<font color='#FF0000'>"+ ODateTime.time2StringWithHHCN(System.currentTimeMillis()) +"</font>"+"至"+"<font color='#FF0000'>"+ODateTime.time2StringWithHHCN(authorityEndTime1)+"</font>"+"止,过期自动失效。对方收到后只能用手机开关锁。同时也要提醒对方不要把虚拟钥匙发送给其他人使用。" ;

//            String str="您的爱车"+"<font color='#FF0000'>"+carNum+"</font>"+"现在已经产生了虚拟钥匙，授权期限是"+"<font color='#FF0000'>"+hour+"</font>"+"时后过期自动失效。对方收到后只能用手机开关锁。同时也要提醒对方不要把虚拟钥匙发送给其他人使用。" ;
            txt_tip_content.setText(android.text.Html.fromHtml(str));
//            txt_tip_content.setText("您的爱车"+carNum+"现在已经产生了虚拟钥匙，"+hour+"时后过期自动失效。对方收到后只能用手机开关锁。同时也要提醒对方不要把虚拟钥匙发送给其他人使用。" );
        }
    }

    @Override
    public void receiveEvent(String s, Object o) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        title_head.img_left.callOnClick();
    }
}
