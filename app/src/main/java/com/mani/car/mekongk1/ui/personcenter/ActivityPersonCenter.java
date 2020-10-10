package com.mani.car.mekongk1.ui.personcenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.style.LeftImgCenterText;
import com.kulala.staticsview.toast.OToastSharePath;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.global.OWXShare;
import com.mani.car.mekongk1.ctrl.OCtrlRegLogin;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.tencent.tauth.TencentCommon;
import com.mani.car.mekongk1.ui.home.ActivityHome;
import com.mani.car.mekongk1.ui.personcenter.aboutus.ActivityAboutUs;
import com.mani.car.mekongk1.ui.personcenter.carmanager.manager.ActivityCarManager;
import com.mani.car.mekongk1.ui.personcenter.carmanager.manager.addcar.ActivityAddCar;
import com.mani.car.mekongk1.ui.personcenter.help.ActivityHelp;
import com.mani.car.mekongk1.ui.personcenter.know_product.ActivityKnowProduct;
import com.mani.car.mekongk1.ui.personcenter.personinformation.ActivityPersonInformation;
import com.mani.car.mekongk1.ui.personcenter.plate_manager.ActivityPlateManager;
import com.mani.car.mekongk1.wxapi.WXEntryActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(PersonCenterPresenter.class)
public class ActivityPersonCenter extends BaseMvpActivity<PersonCenterView,PersonCenterPresenter> implements PersonCenterView {
    @BindView(R.id.person_plate_manager)
    LeftImgCenterText person_plate_manager;
    @BindView(R.id.person_carmanager)
    LeftImgCenterText person_carmanager;
    @BindView(R.id.person_know_product)
    LeftImgCenterText person_know_product;
    @BindView(R.id.person_information)
    LeftImgCenterText person_information;
    @BindView(R.id.person_share)
    LeftImgCenterText person_share;
    @BindView(R.id.person_help)
    LeftImgCenterText person_help;
    @BindView(R.id.person_aboutus)
    LeftImgCenterText person_aboutus;
    @BindView(R.id.person_exitlogin)
    LeftImgCenterText person_exitlogin;
    @BindView(R.id.titile)
    ClipTitleHead titile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        ButterKnife.bind(this);
        titile.my_progress.setTestInfo("个人中心");
        initEvents();
//        OCtrlCommon.getInstance().ccmd1303_getCommonInfo();
    }
    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            titile.img_left.callOnClick();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void initEvents() {
        titile.img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityUtils.startActivity(ActivityPersonCenter.this, ActivityHome.class);
                finish();
            }
        });
        person_exitlogin.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                new ToastConfirmNormal(ActivityPersonCenter.this, null, ActivityHome.PAGE_IS_BLACKSTYLE)
                        .withTitle("退出登录")
                        .withInfo("你确定要退出登录吗?")
                        .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                            @Override
                            public void onClickConfirm(boolean isClickConfirm) {
                                if(isClickConfirm) OCtrlRegLogin.getInstance().CCMD_1109_exitlogin();
                            }
                        }).show();
            }
        });
        person_information.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityPersonCenter.this, ActivityPersonInformation.class);
            }
        });
        person_aboutus.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityPersonCenter.this, ActivityAboutUs.class);
            }
        });
        person_help.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityPersonCenter.this, ActivityHelp.class);
            }
        });
        person_plate_manager.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityPersonCenter.this, ActivityPlateManager.class);
            }
        });
        person_know_product.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityPersonCenter.this, ActivityKnowProduct.class);
            }
        });
        person_carmanager.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                List<DataCarInfo> carList= ManagerCarList.getInstance().getCarInfoList();
                if(carList==null){
                    ActivityUtils.startActivity(ActivityPersonCenter.this, ActivityAddCar.class);
                }else  if(carList!=null&&carList.size()==1&&carList.get(0)!=null&&carList.get(0).ide==0){
                    ActivityUtils.startActivity(ActivityPersonCenter.this, ActivityAddCar.class);
                }else{
                    ActivityUtils.startActivity(ActivityPersonCenter.this, ActivityCarManager.class);
                }
            }
        });
        person_share.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
//                ActivityUtils.startActivity(ActivityPersonCenter.this, ActivityShare.class);
                OToastSharePath.getInstance().show(person_share, "分享下载", new OToastSharePath.OnClickButtonListener() {
                    @Override
                    public void onClick(int pos) {
                        if(pos==1){
//                            DataShare shareInfo = ManagerCommon.getInstance().getShareInfo();
//                            if(shareInfo ==null)return;
                            WXEntryActivity.NEED_WXSHARE_RESULT = true;
                            OWXShare.ShareFriendURL("么控K1","下载地址", ManagerPublicData.shareDownLoadUrl);
                            ManagerPublicData.isNotPopGusture =true;
                        }else if(pos==2){
                            OWXShare.ShareTimeLineURL("么控K1","下载地址",ManagerPublicData.shareDownLoadUrl);
                            ManagerPublicData.isNotPopGusture =true;
                        }  else if(pos==3){
                            TencentCommon.toTencent(ActivityPersonCenter.this,"么控K1","下载地址",ManagerPublicData.shareDownLoadUrl,0,"");
                            ManagerPublicData.isNotPopGusture =true;
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ManagerPublicData.isNotPopGusture =false;
    }

    @Override
    public void initViews() {

    }
}
