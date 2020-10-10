package com.mani.car.mekongk1.ui.personcenter.carmanager.voice.voicelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.staticsfunc.static_assistant.SoundHelper;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.OnClickListenerMy;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ui.login.RecycleViewDivider;
import com.mani.car.mekongk1.ui.personcenter.carmanager.voice.MyVoiceSelect;
import com.mani.car.mekongk1.ui.personcenter.know_product.online.OnlineMessageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 声音库页面
 */
@CreatePresenter(VoiceLibraryPresenter.class)
public class ActivityVoiceLibrary extends BaseMvpActivity<VoiceLibraryView, VoiceLibraryPresenter> implements OnlineMessageView {
    @BindView(R.id.voice_library_list_recycleview)
    RecyclerView voice_library_list_recycleview;
    @BindView(R.id.title_head)
    ClipTitleHead title_head;
    private VoiceLibraryRecycleViewAdapter myRecycleViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<MyVoice> voiceList=new ArrayList<>();
    private String title;
    private int selectResId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_library);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        if(intent!=null){
            title=intent.getStringExtra("title");
        }
        initViews();
        initEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }

    @Override
    public void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("title",title);
                intent.putExtra("resId", selectResId);
                //设置返回数据
                ActivityVoiceLibrary.this.setResult(RESULT_OK, intent);
                //关闭Activity
                ActivityVoiceLibrary.this.finish();
            }
        });
    }

    @Override
    public void initViews() {
        voiceList.clear();
        switch (title){
            case "启动":
                title_head.setTitle("启动音效");
                String []  voiceName={"启动默认","卡车启动1","卡车启动2","跑车启动1","跑车启动2","跑车启动3","跑车启动4","跑车启动5","跑车启动6","跑车启动7","跑车启动8","跑车启动9","跑车启动10","跑车启动11","跑车启动12","跑车启动13","跑车启动14","跑车启动15","跑车启动16","拖拉机启动1","拖拉机启动2"};
                int [] voiceFile={R.raw.start_a_default,R.raw.start_b_truck_1,R.raw.start_b_truck_2,R.raw.start_c_sports_car_1,R.raw.start_c_sports_car_2,R.raw.start_c_sports_car_3,R.raw.start_c_sports_car_4,R.raw.start_c_sports_car_5,R.raw.start_c_sports_car_6,R.raw.start_c_sports_car_7,R.raw.start_c_sports_car_8,R.raw.start_c_sports_car_9,R.raw.start_c_sports_car_10,R.raw.start_c_sports_car_11,R.raw.start_c_sports_car_12,R.raw.start_c_sports_car_13,R.raw.start_c_sports_car_14,R.raw.start_c_sports_car_15,R.raw.start_c_sports_car_16,R.raw.start_d_tractor_1,R.raw.start_d_tractor_2};
                for (int i = 0; i <voiceFile.length ; i++) {
                    voiceList.add(new MyVoice(voiceName[i],voiceFile[i]));
                }
                 break;
            case "上落锁":
                title_head.setTitle("上落锁音效");
                String [] voiceName1={"上落锁音效默认","上落锁音效1","上落锁音效2","上落锁音效3","上落锁音效4","上落锁音效5","上落锁音效6","上落锁音效7"};
                int [] voiceFile1={R.raw.lock_up_a_deafault,R.raw.lock_up_b_1,R.raw.lock_up_b_2,R.raw.lock_up_b_3,R.raw.lock_up_b_4,R.raw.lock_up_b_5,R.raw.lock_up_b_6,R.raw.lock_up_b_7};
                for (int i = 0; i <voiceFile1.length ; i++) {
                    voiceList.add(new MyVoice(voiceName1[i],voiceFile1[i]));
                }
                break;
            case "警报":
                title_head.setTitle("警报音效");
                String [] voiceName2={"警报音效默认","警报音效1","警报音效2","警报音效3","警报音效4","警报音效5","警报音效6","警报音效7","警报音效8","警报音效9","警报音效10","警报音效11","警报音效12","警报音效13"};
                int [] voiceFile2={R.raw.alert_message_a_default,R.raw.alert_message_b_1,R.raw.alert_message_b_2,R.raw.alert_message_b_3,R.raw.alert_message_b_4,R.raw.alert_message_b_5,R.raw.alert_message_b_6,R.raw.alert_message_b_7,R.raw.alert_message_b_8,R.raw.alert_message_b_9,R.raw.alert_message_b_10,R.raw.alert_message_b_11,R.raw.alert_message_b_12,R.raw.alert_message_b_13};
                for (int i = 0; i <voiceFile2.length ; i++) {
                    voiceList.add(new MyVoice(voiceName2[i],voiceFile2[i]));
                }
                break;
            case "点击车音效":
                title_head.setTitle("点击车音效");
                String [] voiceName3={"点击车音效默认"};
                int [] voiceFile3={R.raw.voice_click_car_body};
                for (int i = 0; i <voiceFile3.length ; i++) {
                    voiceList.add(new MyVoice(voiceName3[i],voiceFile3[i]));
                }
                break;
            case "滑动按钮音效":
                title_head.setTitle("滑动按钮音效");
                String [] voiceName4={"滑动按钮音效默认"};
                int [] voiceFile4={R.raw.voice_control_page_change};
                for (int i = 0; i <voiceFile4.length ; i++) {
                    voiceList.add(new MyVoice(voiceName4[i],voiceFile4[i]));
                }
                break;
        }
        mLinearLayoutManager = new LinearLayoutManager(this);
        voice_library_list_recycleview.setLayoutManager(mLinearLayoutManager);
        // do not change the size of the RecyclerView
        voice_library_list_recycleview.setHasFixedSize(true);
        voice_library_list_recycleview.setItemAnimator(new DefaultItemAnimator());
        voice_library_list_recycleview.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this, 1), getResources().getColor(R.color.normal_bg_color_big_white)));
        if (myRecycleViewAdapter == null) {
            myRecycleViewAdapter = new VoiceLibraryRecycleViewAdapter(voiceList,title);
            voice_library_list_recycleview.setAdapter(myRecycleViewAdapter);
        } else {
            myRecycleViewAdapter.setData(voiceList);
            myRecycleViewAdapter.notifyDataSetChanged();
        }
        myRecycleViewAdapter.setOnItemClickListener(new VoiceLibraryRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onVoiceNameSelect(View view, int position) {
                int resId=voiceList.get(position).resId;
                String voiveName=voiceList.get(position).voiceName;
                selectResId=resId;
                myRecycleViewAdapter.notifyDataSetChanged();
                switch (title){
                    case "启动":
                        MyVoiceSelect.getInstance().setStartVoice(voiveName);
                        break;
                    case "上落锁":
                        MyVoiceSelect.getInstance().setLockUpVoice(voiveName);
                        break;
                    case "警报":
                        MyVoiceSelect.getInstance().setAlertVoice(voiveName);
                        break;
                    case "点击车音效":
                        MyVoiceSelect.getInstance().setClickCarVoice(voiveName);
                        break;
                    case "滑动按钮音效":
                        MyVoiceSelect.getInstance().setScrollButtonVoice(voiveName);
                        break;
                }
                SoundHelper.playSoundPool(ActivityVoiceLibrary.this,selectResId);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra("title",title);
        intent.putExtra("resId", selectResId);
        //设置返回数据
        ActivityVoiceLibrary.this.setResult(RESULT_OK, intent);
        //关闭Activity
        ActivityVoiceLibrary.this.finish();
    }
}
