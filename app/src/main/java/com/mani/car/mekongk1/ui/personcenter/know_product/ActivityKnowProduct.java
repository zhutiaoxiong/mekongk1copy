package com.mani.car.mekongk1.ui.personcenter.know_product;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.style.LeftText;
import com.kulala.staticsview.style.TopImgBottomTxt;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.ui.personcenter.aboutus.company_profile.ActivityWeb;
import com.mani.car.mekongk1.ui.personcenter.know_product.online.ActivityOnlineMessage;

import butterknife.BindView;
import butterknife.ButterKnife;


/**主页*/
@CreatePresenter(KnowProductPresenter.class)
public class ActivityKnowProduct extends BaseMvpActivity<KnowProductView,KnowProductPresenter>  implements KnowProductView ,OEventObject{
    @BindView(R.id.media_view)
    View media_view;
    @BindView(R.id.more_info)
    LeftText more_info;
    @BindView(R.id.online_message)
    LeftText online_message;
    @BindView(R.id.taobao)
    TopImgBottomTxt taobao;
    @BindView(R.id.phone_consult)
    TopImgBottomTxt phone_consult;
    @BindView(R.id.wechat_consult)
    TopImgBottomTxt wechat_consult;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konw_product);
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
        online_message.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityKnowProduct.this, ActivityOnlineMessage.class);
            }
        });
        taobao.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ManagerPublicData.isNotPopGusture =true;
                String path ="https://shop199703978.taobao.com";
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri uri = Uri.parse(path);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        more_info.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                String address="http://api.91kulala.com/kulala/protocol/company_chedoujia.html";
                bundle.putString(ActivityWeb.HTTP_ADDRESS, address);
                bundle.putString(ActivityWeb.TITLE_NAME, "更多详情");
                intent.putExtras(bundle);
                intent.setClass(ActivityKnowProduct.this, ActivityWeb.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void initViews() {

    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.ONLINE_MESSAGE_RESULT)){
            final String result= (String) o;
            if(result.equals("")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(800);
                            GlobalContext.popMessage("您的需求已经提交，会有工作人员与您联系",getResources().getColor(R.color.normal_txt_color_cyan));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }
}
