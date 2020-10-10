package com.mani.car.mekongk1.ui.personcenter.carmanager.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlCommon;
import com.mani.car.mekongk1.model.ManagerAlert;
import com.mani.car.mekongk1.model.pushmessage.AlertInfos;
import com.mani.car.mekongk1.ui.login.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**消息设置页面
 * 消息按钮，点击后可以关闭文字提醒
 * 语音按钮 点击后可以关闭或者打开语音消息
 * */
@CreatePresenter(MessageSetPresenter.class)
public class ActivityMessageSwitch extends BaseMvpActivity<MessageSetView,MessageSetPresenter>  implements MessageSetView,OEventObject {
    @BindView(R.id.message_list_recycleview)
    RecyclerView message_list_recycleview;
    private MessageSwitchRecycleViewAdapter myRecycleViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<AlertInfos> messageList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_switch);
        ButterKnife.bind(this);
        OCtrlCommon.getInstance().ccmd1319_getAlertList();
        ODispatcher.addEventListener(OEventName.ALERT_LIST_RESULT_BACK,this);
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
        ODispatcher.removeEventListener(OEventName.ALERT_LIST_RESULT_BACK,this);
    }

    @Override
    public void initEvents() {

    }

    @Override
    public void initViews() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        message_list_recycleview.setLayoutManager(mLinearLayoutManager);
        // do not change the size of the RecyclerView
        message_list_recycleview.setHasFixedSize(true);
        message_list_recycleview.setItemAnimator(new DefaultItemAnimator());
        message_list_recycleview.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this,1), getResources().getColor(R.color.normal_bg_color_big_white)));

    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.ALERT_LIST_RESULT_BACK)){
            refressUI();
        }
    }
    private void refressUI(){
        messageList= ManagerAlert.getInstance().getAlertList();
        if(messageList==null||messageList.size()==0)return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(myRecycleViewAdapter==null){
                    myRecycleViewAdapter=new MessageSwitchRecycleViewAdapter(messageList);
                    message_list_recycleview.setAdapter(myRecycleViewAdapter);
                }else{
                    myRecycleViewAdapter.setData(messageList);
                    myRecycleViewAdapter.notifyDataSetChanged();
                }
                myRecycleViewAdapter.setOnItemClickListener(new MessageSwitchRecycleViewAdapter.OnItemClickListener() {
                    @Override
                    public void onMessageButtonStusChange(View view, int position) {
                        AlertInfos alertInfos=messageList.get(position);
                        if(alertInfos==null)return;
                        if(alertInfos.isOpen==0){
                            OCtrlCommon.getInstance().ccmd1320_ChangeAlertStatus(alertInfos.ide,1);
                        }else{
                            OCtrlCommon.getInstance().ccmd1320_ChangeAlertStatus(alertInfos.ide,0);
                        }
                    }
                });
            }
        });
    }
}
