package com.mani.car.mekongk1.ui.personcenter.carmanager.message;

import android.content.Intent;
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
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.LoadPermissions;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlCommon;
import com.mani.car.mekongk1.model.ManagerDownloadVoice;
import com.mani.car.mekongk1.model.common.DataVoice;
import com.mani.car.mekongk1.ui.login.RecycleViewDivider;
import com.mani.car.mekongk1.ui.personcenter.carmanager.voice.VoiceSetPresenter;
import com.mani.car.mekongk1.ui.personcenter.carmanager.voice.VoiceSetView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**语音包*/
@CreatePresenter(VoiceSetPresenter.class)
public class ActivityVoiceAlertLibrary extends BaseMvpActivity<VoiceSetView,VoiceSetPresenter> implements VoiceSetView ,OEventObject {
    @BindView(R.id.voice_list_recycleview)
    RecyclerView voice_list_recycleview;
    @BindView(R.id.title)
    ClipTitleHead title;
    private MessageAlertLibrayAdapter myRecycleViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<DataVoice> voiceList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_set);
        ButterKnife.bind(this);
        initViews();
        initEvents();
        if(ManagerDownloadVoice.getInstance().getDownLoadVoiceListInfo()==null){
            OCtrlCommon.getInstance().ccmd1321_get_downLoad_VoiceUrls();
        }
        ODispatcher.addEventListener(OEventName.VOICE_LIST_RESULT_BACK,this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }
    @Override
    protected void onDestroy() {
        ODispatcher.removeEventListener(OEventName.VOICE_LIST_RESULT_BACK, this);
        super.onDestroy();
    }

    @Override
    public void initEvents() {

    }

    @Override
    public void initViews() {
        title.setTitle("语音包");
        voiceList= ManagerDownloadVoice.getInstance().getDownLoadVoiceListInfo();
       if(voiceList==null)return;
        mLinearLayoutManager = new LinearLayoutManager(this);
        voice_list_recycleview.setLayoutManager(mLinearLayoutManager);
        // do not change the size of the RecyclerView
        voice_list_recycleview.setHasFixedSize(true);
        voice_list_recycleview.setItemAnimator(new DefaultItemAnimator());
        voice_list_recycleview.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this,1), getResources().getColor(R.color.normal_bg_color_big_white)));
        if(myRecycleViewAdapter==null){
            myRecycleViewAdapter=new MessageAlertLibrayAdapter(voiceList);
            voice_list_recycleview.setAdapter(myRecycleViewAdapter);
        }else{
            myRecycleViewAdapter.setData(voiceList);
            myRecycleViewAdapter.notifyDataSetChanged();
        }
        myRecycleViewAdapter.setOnItemClickListener(new MessageAlertLibrayAdapter.OnItemClickListener() {
            @Override
            public void onVoiceNameSelect(View view,final int position) {
                final DataVoice voice= voiceList.get(position);
                ManagerDownloadVoice.getInstance().setCurrentMp3(voice);
                ManagerDownloadVoice.getInstance().loadMp3( ActivityVoiceAlertLibrary.this,voice,"voice_01_startcar", new ManagerDownloadVoice.OnLoadMp3Listener() {
                    @Override
                    public void loadCompleted(String fileFolder) {
                        ManagerDownloadVoice.getInstance().saveUseVoiceId(position+1+"");
                        ManagerDownloadVoice.getInstance().saveCurrentVoice(ODBHelper.convertString(DataVoice.toJsonObject(voice)));
                        myRecycleViewAdapter.setSelect(position+1+"");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myRecycleViewAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void loadFail(String errorInfo) {

                    }
                });
            }

            @Override
            public void onVoicePlayClick(View view, final int position) {
                LoadPermissions.getInstance().checkPermissionIO(GlobalContext.getCurrentActivity());
                final DataVoice voice= voiceList.get(position);
                ManagerDownloadVoice.getInstance().setCurrentMp3(voice);
                ManagerDownloadVoice.getInstance().loadMp3ReListen( ActivityVoiceAlertLibrary.this,voice, new ManagerDownloadVoice.OnLoadMp3Listener() {
                    @Override
                    public void loadCompleted(final  String fileFolder) {
                  final      String folder  =  ManagerDownloadVoice.getInstance().getMp3Folder(ActivityVoiceAlertLibrary.this, voice);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ManagerDownloadVoice.getInstance().playMp3(fileFolder);
                            }
                        });
                    }

                    @Override
                    public void loadFail(String errorInfo) {

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==RESULT_OK){
            String title=data.getStringExtra("title");
            int resId=data.getIntExtra("resId",0);
            switch (title){
                case "启动":
//                    selectStartVoice=resId;
//                    ManagerVoiceSet.getInstance().saveStartVoiceResId(selectStartVoice);
                    break;
            }
        }
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.VOICE_LIST_RESULT_BACK)){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initViews();
                }
            });
        }
    }
}
