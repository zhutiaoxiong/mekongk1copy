package com.mani.car.mekongk1.ui.personcenter.carmanager.voice;

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
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.ManagerVoiceSet;
import com.mani.car.mekongk1.ui.login.RecycleViewDivider;
import com.mani.car.mekongk1.ui.personcenter.carmanager.voice.voicelibrary.ActivityVoiceLibrary;
import com.mani.car.mekongk1.ui.personcenter.know_product.online.OnlineMessageView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;


/**声音设置页面
 * 1音效内容有启动，上落锁，警报
 * 2默认为素材库的第一个音频文件
 * 3点击播放按钮后，箭头标志变成停止标志，播放完成后变成播放标志
 *4 点击恢复标志，则恢复成默认音效，①恢复成功则出现提示框”恢复成功“
 *                                  ②恢复失败则提醒”恢复失败请重新安装app“
 *  点击加号跳转到音效库页面，点击不同的加号出现不同的音效库，标题也变成对应的文字
 *  点击声音栏后出现 V标识已选当前音效并同时播放声音
 *  播放当前音效的同时如果点击其他音效，马上切换成其他音效，并标识
 *  点击返回键 则回到声音设置页面把原来的音效替换成现在选的
 * */

@CreatePresenter(VoiceSetPresenter.class)
public class ActivityVoiceSet extends BaseMvpActivity<VoiceSetView,VoiceSetPresenter>  implements OnlineMessageView {
    @BindView(R.id.voice_list_recycleview)
    RecyclerView voice_list_recycleview;
    private VoiceSetRecycleViewAdapter myRecycleViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int selectStartVoice=R.raw.start_a_default;
    private int selectlockUpVoice=R.raw.lock_up_a_deafault;
    private int selectAlertVoice=R.raw.alert_message_a_default;
    private int selectScrollButtonVoice=R.raw.voice_control_page_change;
    private int selectClickCarVoice=R.raw.voice_click_car_body;
    private String[] voiceList={"启动","上落锁","警报","点击车音效","滑动按钮音效"};
    private Map<String ,Integer> startVoiceMap=new HashMap<>();
    private Map<String ,Integer> alertVoiceVoiceMap=new HashMap<>();
    private Map<String ,Integer> lockUpVoiceMap=new HashMap<>();
    private Map<String ,Integer> clickCarVoiceMap=new HashMap<>();
    private Map<String ,Integer> scrollButtonVoiceMap=new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_set);
        ButterKnife.bind(this);
        initData();
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

    }
    private void initData(){
        String []  voiceName={"启动默认","卡车启动1","卡车启动2","跑车启动1","跑车启动2","跑车启动3","跑车启动4","跑车启动5","跑车启动6","跑车启动7","跑车启动8","跑车启动9","跑车启动10","跑车启动11","跑车启动12","跑车启动13","跑车启动14","跑车启动15","跑车启动16","拖拉机启动1","拖拉机启动2"};
        int [] voiceFile={R.raw.start_a_default,R.raw.start_b_truck_1,R.raw.start_b_truck_2,R.raw.start_c_sports_car_1,R.raw.start_c_sports_car_2,R.raw.start_c_sports_car_3,R.raw.start_c_sports_car_4,R.raw.start_c_sports_car_5,R.raw.start_c_sports_car_6,R.raw.start_c_sports_car_7,R.raw.start_c_sports_car_8,R.raw.start_c_sports_car_9,R.raw.start_c_sports_car_10,R.raw.start_c_sports_car_11,R.raw.start_c_sports_car_12,R.raw.start_c_sports_car_13,R.raw.start_c_sports_car_14,R.raw.start_c_sports_car_15,R.raw.start_c_sports_car_16,R.raw.start_d_tractor_1,R.raw.start_d_tractor_2};
        for (int i = 0; i <voiceFile.length ; i++) {
            startVoiceMap.put(voiceName[i],voiceFile[i]);
        }
        String [] voiceName1={"上落锁音效默认","上落锁音效1","上落锁音效2","上落锁音效3","上落锁音效4","上落锁音效5","上落锁音效6","上落锁音效7"};
        int [] voiceFile1={R.raw.lock_up_a_deafault,R.raw.lock_up_b_1,R.raw.lock_up_b_2,R.raw.lock_up_b_3,R.raw.lock_up_b_4,R.raw.lock_up_b_5,R.raw.lock_up_b_6,R.raw.lock_up_b_7};
        for (int i = 0; i <voiceFile1.length ; i++) {
            lockUpVoiceMap.put(voiceName1[i],voiceFile1[i]);
        }
        String [] voiceName2={"警报音效默认","警报音效1","警报音效2","警报音效3","警报音效4","警报音效5","警报音效6","警报音效7","警报音效8","警报音效9","警报音效10","警报音效11","警报音效12","警报音效13"};
        int [] voiceFile2={R.raw.alert_message_a_default,R.raw.alert_message_b_1,R.raw.alert_message_b_2,R.raw.alert_message_b_3,R.raw.alert_message_b_4,R.raw.alert_message_b_5,R.raw.alert_message_b_6,R.raw.alert_message_b_7,R.raw.alert_message_b_8,R.raw.alert_message_b_9,R.raw.alert_message_b_10,R.raw.alert_message_b_11,R.raw.alert_message_b_12,R.raw.alert_message_b_13};
        for (int i = 0; i <voiceFile2.length ; i++) {
            alertVoiceVoiceMap.put(voiceName2[i],voiceFile2[i]);
        }
        String [] voiceName3={"点击车音效默认"};
        int [] voiceFile3={R.raw.voice_click_car_body};
        for (int i = 0; i <voiceFile3.length ; i++) {
            clickCarVoiceMap.put(voiceName3[i],voiceFile3[i]);
        }
        String [] voiceName4={"滑动按钮音效默认"};
        int [] voiceFile4={R.raw.voice_control_page_change};
        for (int i = 0; i <voiceFile4.length ; i++) {
            scrollButtonVoiceMap.put(voiceName4[i],voiceFile4[i]);
        }
    }

    @Override
    public void initViews() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        voice_list_recycleview.setLayoutManager(mLinearLayoutManager);
        // do not change the size of the RecyclerView
        voice_list_recycleview.setHasFixedSize(true);
        voice_list_recycleview.setItemAnimator(new DefaultItemAnimator());
        voice_list_recycleview.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this,1), getResources().getColor(R.color.normal_bg_color_big_white)));
        if(myRecycleViewAdapter==null){
            myRecycleViewAdapter=new VoiceSetRecycleViewAdapter(voiceList);
            voice_list_recycleview.setAdapter(myRecycleViewAdapter);
        }else{
            myRecycleViewAdapter.setData(voiceList);
            myRecycleViewAdapter.notifyDataSetChanged();
        }
        myRecycleViewAdapter.setOnItemClickListener(new VoiceSetRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onVoiceNameSelect(View view, int position) {
//                ActivityUtils.startActivity(ActivityVoiceSet.this, ActivityVoiceLibrary.class);
            }

            @Override
            public void onVoicePlayClick(View view, int position) {
                String voiceType=voiceList[position];
                int playVoiceId=0;
                switch (voiceType){
                    case "启动":
                        String startVoice= MyVoiceSelect.getInstance().getStartVoice();
                        int voiceid1=getVoiceId(startVoice,startVoiceMap);
                        playVoiceId=voiceid1;
                        break;
                    case "上落锁":
                        String lockUpVoice= MyVoiceSelect.getInstance().getLockUpVoice();
                        int voiceid2=getVoiceId(lockUpVoice,lockUpVoiceMap);
                        playVoiceId=voiceid2;
                        break;
                    case "警报":
                        String alertVoice= MyVoiceSelect.getInstance().getAlertVoice();
                        int voiceid3=getVoiceId(alertVoice,alertVoiceVoiceMap);
                        playVoiceId=voiceid3;
                        break;
                    case "点击车音效":
                        String clickCarVoice= MyVoiceSelect.getInstance().getClickCarVoice();
                        int voiceId4=getVoiceId(clickCarVoice,clickCarVoiceMap);
                        playVoiceId=voiceId4;
                        break;
                    case "滑动按钮音效":
                        String scrollButtonVoice= MyVoiceSelect.getInstance().getScrollButtonVoice();
                        int voiceid5=getVoiceId(scrollButtonVoice,scrollButtonVoiceMap);
                        playVoiceId=voiceid5;
                        break;
                }
                SoundHelper.playSoundPool(ActivityVoiceSet.this,playVoiceId);
            }

            @Override
            public void onVoiceHornClick(View view, int position) {
                String voiceType=voiceList[position];
                 int isOpenStartVoice= ManagerVoiceSet.getInstance().getIsOpenStartVoice();
                 int isOpenAlertVoice= ManagerVoiceSet.getInstance().getIsOpenAlertVoice();
                 int isOpenLockUpVoice= ManagerVoiceSet.getInstance().getIsOpenLockUpVoice();
                int isOpenClickCarVoice= ManagerVoiceSet.getInstance().getIsOpenClickCarVoice();
                int isOpenScrollButtonVoice= ManagerVoiceSet.getInstance().getIsOpenScrollButtonVoice();
//                Log.e("aaaaaa", "点击的position: "+position +"isOpenStartVoice"+isOpenStartVoice+"isOpenAlertVoice"+isOpenAlertVoice+"isOpenLockUpVoice"+isOpenLockUpVoice);
                switch (position){
                    case 0:
                       if(isOpenStartVoice==-1){
                           //关闭
//                           Log.e("aaaaaa", "点击的position: "+position +"isOpenStartVoice"+isOpenStartVoice+"isOpenAlertVoice"+isOpenAlertVoice+"isOpenLockUpVoice"+isOpenLockUpVoice);

                           ManagerVoiceSet.getInstance().saveIsOpenStartVoice(1);
                       }else  if(isOpenStartVoice==1){
                           //开启
                           ManagerVoiceSet.getInstance().saveIsOpenStartVoice(-1);
                       }
                        break;
                    case 1:
                        if(isOpenLockUpVoice==-1){
                            //关闭
                            ManagerVoiceSet.getInstance().saveIsOpenLockUpVoice(1);
                        }else  if(isOpenLockUpVoice==1){
                            //开启
                            ManagerVoiceSet.getInstance().saveIsOpenLockUpVoice(-1);
                        }
                        break;
                    case 2:
                        if(isOpenAlertVoice==-1){
                            //关闭
                            ManagerVoiceSet.getInstance().saveIsOpenAlertVoice(1);
                        }else  if(isOpenAlertVoice==1){
                            //开启
                            ManagerVoiceSet.getInstance().saveIsOpenAlertVoice(-1);
                        }
                        break;
                    case 3:
                        if(isOpenClickCarVoice==-1){
                            //关闭
                            ManagerVoiceSet.getInstance().saveIsOpenClickCarVoice(1);
                        }else  if(isOpenClickCarVoice==1){
                            //开启
                            ManagerVoiceSet.getInstance().saveIsOpenClickCarVoice(-1);
                        }
                        break;
                    case 4:
                        if(isOpenScrollButtonVoice==-1){
                            //关闭
                            ManagerVoiceSet.getInstance().saveIsOpenScrollButtonVoice(1);
                        }else  if(isOpenScrollButtonVoice==1){
                            //开启
                            ManagerVoiceSet.getInstance().saveIsOpenScrollButtonVoice(-1);
                        }
                        break;
                }
                myRecycleViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onVoiceSelectClick(View view, int position) {
                Intent intent=new Intent();
                intent.setClass(ActivityVoiceSet.this,ActivityVoiceLibrary.class);
                intent.putExtra("title",voiceList[position]);
                startActivityForResult(intent,1);
            }

            @Override
            public void onVoiceDefaultClick(View view, int position) {
                String voiceType=voiceList[position];
                switch (voiceType){
                    case "启动":
                        selectStartVoice=R.raw.start_a_default;
                        ManagerVoiceSet.getInstance().saveStartVoiceResId(selectStartVoice);
                        MyVoiceSelect.getInstance().setStartVoice("启动默认");
                        break;
                    case "上落锁":
                        selectlockUpVoice=R.raw.lock_up_a_deafault;
                        ManagerVoiceSet.getInstance().saveStartVoiceResId(selectlockUpVoice);
                        MyVoiceSelect.getInstance().setLockUpVoice("上落锁音效默认");
                        break;
                    case "警报":
                        selectAlertVoice=R.raw.alert_message_a_default;
                        ManagerVoiceSet.getInstance().saveStartVoiceResId(selectAlertVoice);
                        MyVoiceSelect.getInstance().setAlertVoice("警报音效默认");
                        break;
                    case "点击车音效":
                        selectClickCarVoice=R.raw.voice_click_car_body;
                        ManagerVoiceSet.getInstance().saveClickCarVoiceResId(selectClickCarVoice);
                        MyVoiceSelect.getInstance().setClickCarVoice("点击车音效默认");
                        break;
                    case "滑动按钮音效":
                        selectScrollButtonVoice=R.raw.voice_control_page_change;
                        ManagerVoiceSet.getInstance().saveScrollButtonVoiceResId(selectScrollButtonVoice);
                        MyVoiceSelect.getInstance().setScrollButtonVoice("滑动按钮音效默认");
                        break;
                }
                GlobalContext.popMessage("已恢复默认音效",getResources().getColor(R.color.normal_txt_color_cyan));
            }
        });
    }
    private int getVoiceId(String voiceName, Map<String,Integer> map){
        int resid=0;
        Set<String> k = map.keySet(); //Set<String>相当于返回值类型，此相当于Set集合加上了泛型，类型为String,k相当于变量名
        //有了Set集合，就可以获取其迭代器.（注意Set集合的类型要和迭代器保持一致）
        Iterator<String> it = k.iterator();
        while(it.hasNext()){
            String key = it.next();
            if(voiceName.equals(key)){
                resid=map.get(key);
            }
        }
        return resid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==RESULT_OK){
            String title=data.getStringExtra("title");
            int resId=data.getIntExtra("resId",0);
            switch (title){
                case "启动":
                    selectStartVoice=resId;
                    ManagerVoiceSet.getInstance().saveStartVoiceResId(selectStartVoice);
                    break;
                case "上落锁":
                    selectlockUpVoice=resId;
                    ManagerVoiceSet.getInstance().saveLockUpVoiceResId(selectlockUpVoice);
                    break;
                case "警报":
                    selectAlertVoice=resId;
                    ManagerVoiceSet.getInstance().saveAlertVoiceId(selectAlertVoice);
                    break;
                case "点击车音效":
                    selectClickCarVoice=resId;
                    ManagerVoiceSet.getInstance().saveClickCarVoiceResId(selectClickCarVoice);
                    break;
                case "滑动按钮音效":
                    selectScrollButtonVoice=resId;
                    ManagerVoiceSet.getInstance().saveScrollButtonVoiceResId(selectScrollButtonVoice);
                    break;
            }
        }
    }
}
