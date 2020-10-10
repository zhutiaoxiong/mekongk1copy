package com.mani.car.mekongk1.ui.personcenter.aboutus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_assistant.UrlHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsfunc.static_system.SystemMe;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.style.LeftText;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.VersionNewDownloadApk;
import com.mani.car.mekongk1.ctrl.OCtrlCommon;
import com.mani.car.mekongk1.ui.home.ActivityHome;
import com.mani.car.mekongk1.ui.personcenter.aboutus.company_profile.ActivityWeb;
import com.mani.car.mekongk1.ui.personcenter.aboutus.publicnumber.ActivityPublicNumber;
import com.mani.car.mekongk1.ui.useragreement.ActivityUserAgreeMent;
import com.mani.car.mekongk1.ui.useragreement.ActivityUserAgreeMentPravate;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(AboutUsPresenter.class)
public class ActivityAboutUs extends BaseMvpActivity<AboutUsView,AboutUsPresenter> implements AboutUsView,OEventObject {
    @BindView(R.id.public_number)
    LeftText public_number;
    @BindView(R.id.user_agreement)
    LeftText user_agreement;
    @BindView(R.id.user_private)
    LeftText user_private;

    @BindView(R.id.company_profile)
    LeftText company_profile;
    @BindView(R.id.titile)
    ClipTitleHead titile;
    @BindView(R.id.version_code)
    TextView version_code;
    @BindView(R.id.need_update)
    TextView need_update;
    @BindView(R.id.copyright_declaration)
    TextView copyright_declaration;

    private static long    lastCheckUpdateTime = 0;//上回检查更新的时间
    private  String         downLoadUrl;
    private static  ActivityAboutUs activityAboutUsThis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_aboutus);
        ButterKnife.bind(this);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.GETVERSIONINFO_RESULTBACK, ActivityAboutUs.this);
    }

    @Override
    public void initViews() {
        version_code.setText(SystemMe.getVersionName(ActivityAboutUs.this));
        if(VersionNewDownloadApk.isDownloading){
            need_update.setText("正在下载更新...");
        }else {
            need_update.setText(SystemMe.getVersionName(ActivityAboutUs.this));
        }
        long now = System.currentTimeMillis();
        if (now - lastCheckUpdateTime > 10*1000L) {
            OCtrlCommon.getInstance().ccmd1302_getVersionInfo(1);
            lastCheckUpdateTime = now;
        }
        copyright_declaration.setText("copyright@2016-2019chedoujia\nAll Rights Reserve");
    }
    @Override
    protected void onDestroy() {
        ODispatcher.removeEventListener(OEventName.GETVERSIONINFO_RESULTBACK, this);
        super.onDestroy();
    }
    @Override
    public void receiveEvent(String key, Object paramObj) {
        if (key.equals(OEventName.GETVERSIONINFO_RESULTBACK)) {
            JsonObject obj = (JsonObject) paramObj;
            int isUpdate = OJsonGet.getInteger(obj, "isUpdate");
            final String info = OJsonGet.getString(obj, "comment");
            if (isUpdate == 1) {
                if(VersionNewDownloadApk.isDownloading){
                    handlerSetup_need_update_Text("正在下载更新...");
                }else {
                    downLoadUrl = OJsonGet.getString(obj, "downLoadUrl");
                    handlerSetup_need_update_Text("需要升级");
                }
            } else {
                handlerSetup_need_update_Text("当前已是最新版本");
            }
        }
    }
    private void handlerSetup_need_update_Text(final String txt){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                need_update.setText(txt);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        activityAboutUsThis=ActivityAboutUs.this;
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityAboutUsThis=null;
    }

    @Override
    public void initEvents() {
        public_number.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityAboutUs.this, ActivityPublicNumber.class);
            }
        });
        user_agreement.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityAboutUs.this, ActivityUserAgreeMent.class);
            }
        });
        user_private.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityAboutUs.this, ActivityUserAgreeMentPravate.class);
            }
        });

        company_profile.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
//                ActivityUtils.startActivity(ActivityAboutUs.this, ActivityCompanyProfile.class);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                String address="http://api.91kulala.com/kulala/protocol/company_chedoujia.html";
                bundle.putString(ActivityWeb.HTTP_ADDRESS, address);
                bundle.putString(ActivityWeb.TITLE_NAME, "公司简介");
                intent.putExtras(bundle);
                intent.setClass(ActivityAboutUs.this, ActivityWeb.class);
                startActivity(intent);
            }
        });
        need_update.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                String info = need_update.getText().toString().trim();
                if ("正在下载更新...".equals(info)){
                    GlobalContext.popMessage("正在下载更新...",GlobalContext.getContext().getResources().getColor(R.color.normal_bg_color_tip_red));
                } else if ("当前已是最新版本".equals(info)){
                    GlobalContext.popMessage("当前已是最新版本",GlobalContext.getContext().getResources().getColor(R.color.normal_bg_color_tip_red));
                }else if ("需要升级".equals(info)) {
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, ActivityHome.PAGE_IS_BLACKSTYLE)
                            .withInfo("有新版本，现在就更新吗?")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm){
                                        need_update.setText("正在下载更新...");
                                        if(VersionNewDownloadApk.checkInstallPermission(getApplicationContext())) {
                                            new VersionNewDownloadApk(getApplicationContext(), downLoadUrl, UrlHelper.getFileName(downLoadUrl) + ".apk");
                                        }else{
                                            need_update.setText("请30秒后再检测更新!");
                                        }
                                    }
                                }
                            }).show();
                }else {
                    long now = System.currentTimeMillis();
                    if (now - lastCheckUpdateTime > 10 * 1000L) {
                        OCtrlCommon.getInstance().ccmd1302_getVersionInfo(1);
                        lastCheckUpdateTime = now;
                    }
                }
            }
        });
    }
}
