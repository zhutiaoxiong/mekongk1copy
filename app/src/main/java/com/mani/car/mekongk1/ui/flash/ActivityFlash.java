package com.mani.car.mekongk1.ui.flash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_assistant.UrlHelper;
import com.kulala.staticsfunc.static_system.LoadPermissions;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.VersionNewDownloadApk;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.ui.gusturepassword.ActivityGesturePassword;
import com.mani.car.mekongk1.ui.home.ActivityHome;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(FlashPresenter.class)
public class ActivityFlash extends BaseMvpActivity<FlashView,FlashPresenter> implements FlashView ,OEventObject {
    private String downLoadUrl;
    @BindView(R.id.txt_download)TextView txt_download;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        ButterKnife.bind(this);
        LoadPermissions.getInstance().setOnAllPermissionGranted(new LoadPermissions.OnAllPermissionGranted() {
            @Override
            public void onAllGranted() {
                getMvpPresenter().delayJump();
            }
        });
        LoadPermissions.getInstance().loadPermissionAll(ActivityFlash.this);
        ODispatcher.addEventListener(OEventName.GETVERSIONINFO_RESULTBACK, ActivityFlash.this);
    }
    @Override
    protected void onDestroy() {
        ODispatcher.removeEventListener(OEventName.GETVERSIONINFO_RESULTBACK, this);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.e("activity","onStart");
        ManagerPublicData.isNotPopGusture = true;
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        Log.e("loadPermission", "onRequestPermissionsResult" + "  " + System.currentTimeMillis());
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LoadPermissions.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults, ActivityFlash.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_bg_color_big_white));
    }

    @Override
    public void initEvents() {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void jumpHomePage(String condition) {
        ActivityUtils.startActivityTakeData(this, ActivityHome.class,condition);
        finish();
    }

    @Override
    public void jumpGesturePasswordPage() {
        ActivityUtils.startActivity(this, ActivityGesturePassword.class);
    }

    @Override
    public void receiveEvent(String key, Object paramObj) {
        if (key.equals(OEventName.GETVERSIONINFO_RESULTBACK)) {
            FlashPresenter.isVerCheckResultBack = true;
            JsonObject obj = (JsonObject) paramObj;
            if (obj == null) {
                jumpHomePage("GETVERSIONINFO_RESULTBACK null");
            } else {
                int isUpdate = OJsonGet.getInteger(obj, "isUpdate");
                final String info = OJsonGet.getString(obj, "comment");
                if (isUpdate == 1) {
                    downLoadUrl = OJsonGet.getString(obj, "downLoadUrl");
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(),null,true)
                            .withTitle("版本升级")
                            .withInfo(info)
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm) {//点了升级
                                        txt_download.setVisibility(View.VISIBLE);
                                        if(VersionNewDownloadApk.checkInstallPermission(getApplicationContext())) {
                                            ODispatcher.removeEventListener(OEventName.GETVERSIONINFO_RESULTBACK, ActivityFlash.this);
                                            new VersionNewDownloadApk(getApplicationContext(), downLoadUrl, UrlHelper.getFileName(downLoadUrl) + ".apk");
                                        }else{
                                            txt_download.setText("请重新打开App检查更新!");
                                        }
                                    } else {
                                        jumpHomePage("cancel Update");
                                    }
                                }
                            }).show();
                } else {
                    jumpHomePage("GET_VERSION 无需升级");
                }
            }
        }
    }



}
