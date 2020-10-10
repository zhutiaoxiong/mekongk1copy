package com.mani.car.mekongk1.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.OConver;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.MyPushReceiveService;
import com.mani.car.mekongk1.common.MyPushService;
import com.mani.car.mekongk1.common.socketutils.BootBroadcastReceiver;
import com.mani.car.mekongk1.ctrl.OCtrlCar;
import com.mani.car.mekongk1.ctrl.OCtrlCheckCarState;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.ui.useragreement.ActivityUserAgreeMent;
import com.mani.car.mekongk1.ui.useragreement.ActivityUserAgreeMentPravate;

import butterknife.BindView;
import butterknife.ButterKnife;

/**主页*/
@CreatePresenter(HomePresenter.class)
public class ActivityHome extends BaseMvpActivity<HomeView,HomePresenter> implements HomeView {
    public static boolean isFirstInto = true;//是否首进
    public static boolean PAGE_IS_BLACKSTYLE = true;
    @BindView(R.id.viewpager)ViewPager viewPager;
    @BindView(R.id.re_note)
    RelativeLayout re_note;
    @BindView(R.id.btn_confirm)Button btn_confirm;
    @BindView(R.id.btn_cancle)
    Button btn_cancle;
    @BindView(R.id.yinsizhengce)
    TextView yinsizhengce;
    @BindView(R.id.yonghuxieyi)
    TextView yonghuxieyi;
    @BindView(R.id.warnning_note)
    TextView warnning_note;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initViews();
        initEvents();
        long useId = ManagerLoginReg.getInstance().getCurrentUser().userId;
        String isMyFirstIn= ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isMyFirstIn");
        if(isMyFirstIn.equals("")){
            re_note.setVisibility(View.VISIBLE);
            warnning_note.setText(OConver.StrToDBC("我们非常重视用户的隐私和个人信息保护希望您仔细阅读《隐私政策》和《用户服务协议》，详细了解我们对信息的收集，使用方式，以便您更好地了解我们的服务并作出适当的选择。如您同意隐私政策和用户服务协议，请点击“同意”并开始使用我们的产品及服务。若点击“不同意”,则相关服务不可用"));
        }else{
            re_note.setVisibility(View.INVISIBLE);
        }
        if(isFirstInto && useId!=0)
            OCtrlCar.getInstance().ccmd1203_getcarlist(true);
        BootBroadcastReceiver.getInstance().initReceiver(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        /**初始化设第一个页面的状态颜色，依页面不同修改*/
        GlobalContext.setStatusBarColorForPage(Color.parseColor("#101822"));
        //加载推送,防止断连接
        BootBroadcastReceiver.startC();
        BootBroadcastReceiver.sendMessage(GlobalContext.getContext(),2,"");//发心跳

        PushManager.getInstance().initialize(this.getApplicationContext(), MyPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), MyPushReceiveService.class);
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        BootBroadcastReceiver.getInstance().unRegReceiver();
    }
    @Override
    public void initEvents() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position %5==0){
                    PAGE_IS_BLACKSTYLE = true;
                    OCtrlCheckCarState.getInstance().setNeedCheck(true,3);
                    /**第一个页面的状态颜色，依页面不同修改*/
                    GlobalContext.setStatusBarColorForPage(Color.parseColor("#101822"));
                }else{
                    PAGE_IS_BLACKSTYLE = false;
                    OCtrlCheckCarState.getInstance().setNeedCheck(false,3);
                    /**其它页面的状态颜色，依页面不同修改*/
                    GlobalContext.setStatusBarColorForPage(Color.parseColor("#FFFFFF"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("isMyFirstIn","isMyFirstIn");
                re_note.setVisibility(View.INVISIBLE);
            }
        });
        yinsizhengce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(ActivityHome.this, ActivityUserAgreeMentPravate.class);
            }
        });
        yonghuxieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(ActivityHome.this, ActivityUserAgreeMent.class);
            }
        });
        re_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void initViews() {
        viewPager.setOffscreenPageLimit(MyAdapter.TOTAL_FRAGMENT-1);
//        List<Fragment> list       = new ArrayList<>();
//        Fragment01 fragment01 = new Fragment01();
//        Fragment02 fragment02 = new Fragment02();
//        Fragment03 fragment03 = new Fragment03();
//        Fragment04 fragment04 = new Fragment04();
//        Fragment05 fragment05 = new Fragment05();
//
//        list.add(fragment01);
//        list.add(fragment02);
//        list.add(fragment03);
//        list.add(fragment04);
//        list.add(fragment05);
//        MyAdapter adater = new MyAdapter(getSupportFragmentManager(), list);
        MyAdapter adater = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adater);
        viewPager.setCurrentItem(5000);
//        viewPager.setPageTransformer(false, new ScaleTranceFormer(ActivityMain.this));
    }

    long preBackTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long now = System.currentTimeMillis();
            if (now - preBackTime < 2000L) {
                finish();
                System.exit(0);
            } else {
                Toast.makeText(getApplicationContext(), "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                preBackTime = now;
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
