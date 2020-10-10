package com.mani.car.mekongk1.ui.controlcar;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_assistant.SoundHelper;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsfunc.time.CountDownTimerMy;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.toast.ToastConfirmNormalForAllClick;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.PHeadHttp;
import com.mani.car.mekongk1.common.blue.BlueAdapter;
import com.mani.car.mekongk1.ctrl.CarControlResult;
import com.mani.car.mekongk1.ctrl.OCtrlCar;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.model.ManagerSkins;
import com.mani.car.mekongk1.model.ManagerVoiceSet;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.carlist.DataCarStatus;
import com.mani.car.mekongk1.model.carlist.DataControlButton;
import com.mani.car.mekongk1.model.loginreg.DataUser;
import com.mani.car.mekongk1.ui.controlcar.borrowcar.borrowsuccess.ActivityBorrowSuccess;
import com.mani.car.mekongk1.ui.home.ActivityHome;
import com.mani.car.mekongk1.ui.login.ActivityLogin;
import com.mani.car.mekongk1.ui.personcenter.ActivityPersonCenter;

import static com.mani.car.mekongk1.common.blue.OnBlueStateListenerRoll.STATE_CONNECT_OK;

/**
 * 控车主页面
 */
public class ViewControlCar extends RelativeLayout implements OEventObject {// extends BaseMvpView<ControlCarMethod, ControlCarPresenter>  ControlCarMethod,
    private static final String TAG = "ViewControlCar";
    private ClipTitleHead          title_head;
    private ViewCarBody            img_carbody;
    private ImageView              img_findcar;
    private ImageView  img_guide_four,img_guide_three,img_guide_two,img_guide_one;
//    private ControlButtonViewpager btn_pager;
//    private LinearLayout           dot_horizontal;
    private ViewSideState side_state;
    private boolean isFirstGuide=false;
//    private FlashTextView flash_txt;
    private ScrollLayout gesture_layout;
    private ControlButtonSingleZhu control_button;
    private long clickUnLockTime;
    private long messageComeTime;

    private int bodyW,bodyH,thisHigh,thisWidth;
    public ViewControlCar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.controlcar_main, this, true);
//    public ViewControlCar(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        LayoutInflater.from(context).inflate(R.layout.controlcar_main, this, true);
        Log.e("123","456");
        title_head = (ClipTitleHead) findViewById(R.id.title_head);
        img_carbody = (ViewCarBody) findViewById(R.id.img_carbody);
        img_findcar = (ImageView) findViewById(R.id.img_findcar);
        img_guide_four = (ImageView) findViewById(R.id.img_guide_four);
        img_guide_three = (ImageView) findViewById(R.id.img_guide_three);
        img_guide_two = (ImageView) findViewById(R.id.img_guide_two);
        img_guide_one = (ImageView) findViewById(R.id.img_guide_one);
        gesture_layout = (ScrollLayout) findViewById(R.id.gesture_layout);
//        btn_pager = (ControlButtonViewpager) findViewById(R.id.btn_pager);
//        dot_horizontal = (LinearLayout) findViewById(R.id.dot_horizontal);
        side_state = (ViewSideState) findViewById(R.id.side_state);
        control_button = (ControlButtonSingleZhu) findViewById(R.id.control_button);
//        flash_txt = (FlashTextView) findViewById(R.id.flash_txt);
        title_head.my_progress.setTestInfo("控车");
        gesture_layout.setIsNeedScrollDown(false);
        initViews();
        initEvents();
        img_carbody.setOnImageLoadCompleted(new ViewCarBody.OnImageLoadCompleted() {
            @Override
            public void OnImageLoadCompleted(int bodyW, int bodyH) {
                ViewControlCar.this.bodyW = bodyW;
                ViewControlCar.this.bodyH = bodyH;
                resetBody();
            }
        });
    }
    /**用于解决部分手机车身变小问题**/
    private boolean runOnce = false;
    private void resetBody(){
        if(bodyW!=0 && bodyH!=0 && thisHigh!=0 && thisWidth!=0 && !runOnce){
            runOnce = true;
            GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams lp = img_carbody.getLayoutParams();
                    lp.width = bodyW*thisHigh/bodyH;
                    lp.height = thisHigh;
                    img_carbody.setLayoutParams(lp);
                    img_carbody.setMaxHeight(thisHigh);
                    img_carbody.setMaxWidth(lp.width);
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        thisHigh = this.getMeasuredHeight()-ODipToPx.dipToPx(getContext(),125+48);
        thisWidth = this.getMeasuredWidth();
        resetBody();
    }
    /**用于解决部分手机车身变小问题end**/


    public void onResumeControlCar(){
        title_head.my_progress.onResumeControlCar();
    }
    public void initViews() {
        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        chageHeadPic();
        title_head.setTitle(carInfo.num);
//        btn_pager.setVisibility(INVISIBLE);
        img_carbody.changeData();
        showGuidePage();
        setButtonName();
    }
    public void setButtonName(){
        DataCarInfo info=ManagerCarList.getInstance().getCurrentCar();
        if(info==null)return;
        setButtonData(info);
    }
    private void setButtonData(DataCarInfo dataCarInfo){
        DataControlButton button1=new DataControlButton("关锁",100);
        DataControlButton button3=new DataControlButton("开锁",102);
        DataControlButton button2=new DataControlButton();
        if(ManagerCarList.getInstance().getCurrentCarStatus().isStart==0){
            button2.name="启动";
            button2.ide=101;
            button2.isUse=0;
        }else{
            button2.name="熄火";
            button2.ide=101;
            button2.isUse=1;
        }
        if(button1!=null&&button2!=null&&button3!=null){
            control_button.setButtonName(button1,button2,button3);
        }
    }
    public void  showGuidePage(){
        String guide_control_one = ODBHelper.getInstance(getContext()).queryCommonInfo("guide_control_car_one");
        if(!guide_control_one.equals("true")){
            ODBHelper.getInstance(getContext()).changeCommonInfo("guide_control_car_one","true");
            img_guide_one.setVisibility(VISIBLE);
        }
    }
    public void  guidePageClickEvent(){
        img_guide_four.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                img_guide_four.setVisibility(INVISIBLE);
                isFirstGuide=true;
            }
        });
        img_guide_three.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                img_guide_three.setVisibility(INVISIBLE);
                img_guide_four.setVisibility(VISIBLE);
            }
        });
        img_guide_two.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                img_guide_two.setVisibility(INVISIBLE);
                img_guide_three.setVisibility(VISIBLE);
            }
        });
        img_guide_one.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                img_guide_one.setVisibility(INVISIBLE);
                img_guide_two.setVisibility(VISIBLE);
            }
        });
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ODispatcher.addEventListener(OEventName.CLICK_FUNCBTN_HIDE_BUTTONS,this);
        ODispatcher.addEventListener(OEventName.CAR_CONTROL_RESULT,this);
        ODispatcher.addEventListener(OEventName.CAR_STATUS_SECOND_CHANGE,this);
        ODispatcher.addEventListener(OEventName.LOGIN_SUCCESS,this);
        ODispatcher.addEventListener(OEventName.CHANGE_USER_INFO_OK,this);
        ODispatcher.addEventListener(OEventName.EXIT_LOGIN,this);
        ODispatcher.addEventListener(OEventName.BLUE_CONN_PROGRESS_SUCESS,this);
        ODispatcher.addEventListener(OEventName.BLUE_CONN_PROGRESS_FAIL,this);
        ODispatcher.addEventListener(OEventName.BLUE_CONN_PROGRESS_BEGIN,this);
        ODispatcher.addEventListener(OEventName.FRAGMENT01_ON_RESUME,this);
        ODispatcher.addEventListener(OEventName.AUTHOR_CODRIVER_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.AUTHOR_CODRIVER_RESULTBACK_FAILED, this);
        ODispatcher.addEventListener(OEventName.AUTHORIZATION_USER_STOPED,this);
    }

    @Override
    protected void onDetachedFromWindow() {
        ODispatcher.removeEventListener(OEventName.CLICK_FUNCBTN_HIDE_BUTTONS,this);
        ODispatcher.removeEventListener(OEventName.CAR_CONTROL_RESULT,this);
        ODispatcher.removeEventListener(OEventName.CAR_STATUS_SECOND_CHANGE,this);
        ODispatcher.removeEventListener(OEventName.LOGIN_SUCCESS,this);
        ODispatcher.removeEventListener(OEventName.CHANGE_USER_INFO_OK,this);
        ODispatcher.removeEventListener(OEventName.EXIT_LOGIN,this);
        ODispatcher.removeEventListener(OEventName.BLUE_CONN_PROGRESS_SUCESS,this);
        ODispatcher.removeEventListener(OEventName.BLUE_CONN_PROGRESS_FAIL,this);
        ODispatcher.removeEventListener(OEventName.BLUE_CONN_PROGRESS_BEGIN,this);
        ODispatcher.removeEventListener(OEventName.FRAGMENT01_ON_RESUME,this);
        ODispatcher.removeEventListener(OEventName.AUTHOR_CODRIVER_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.AUTHOR_CODRIVER_RESULTBACK_FAILED, this);
        ODispatcher.removeEventListener(OEventName.AUTHORIZATION_USER_STOPED,this);
        super.onDetachedFromWindow();
    }
    private int controlTime = 0;
    public void initEvents() {
        title_head.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
                String token = PHeadHttp.getInstance().getToken();
                if (user == null || user.userId == 0 || token == null || token.length() == 0) {
                    ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityLogin.class);
                }else {
                    ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityPersonCenter.class);
                }

            }
        });
        //上滑弹起功能列表按钮
        gesture_layout.setOnScrollDownListLister(new ScrollLayout.OnScrollDownListLister() {
            @Override
            public void onScrollDown(boolean isScrollDown) {
                if(!isScrollDown){
                    PopChangeButtons.getInstance().show(img_carbody);
                }
            }
        });
//        img_carbody.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                boolean isHide = btn_pager.getTag() == null ? false : (Boolean)btn_pager.getTag();
////                popView(isHide);
//            }
//        });
//        img_carbody.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                PopChangeButtons.getInstance().show(img_carbody);
//                return false;
//            }
//        });
        guidePageClickEvent();
    }

//    @Override
//    public void jumpPage() {
//    }
//
//    @Override
//    public void settext(String txt1) {
//    }
    @Override
    public void receiveEvent(String eventName, final Object paramObj) {
        if(eventName.equals(OEventName.CLICK_FUNCBTN_HIDE_BUTTONS)){
            handlerParent.obtainMessage(MSG_CLICK_FUNCBTN_HIDE_BUTTONS).sendToTarget();
        }else if(eventName.equals(OEventName.CAR_CONTROL_RESULT)){
            handlerParent.obtainMessage(MSG_SHOW_CONTROLRESULT,paramObj).sendToTarget();
        }else if(eventName.equals(OEventName.CAR_STATUS_SECOND_CHANGE)){
            //用于检测蓝牙换车
            BluetoothDevice device = BlueAdapter.getInstance().getUsedDevice();
            if(BlueAdapter.current_blue_state >= STATE_CONNECT_OK &&
                    !ManagerCarList.getInstance().getCurrentCar().bluetoothName.equals(device.getName()))
                BlueAdapter.getInstance().closeBlueReal();
            handlerParent.obtainMessage(CAR_STATUS_SECOND_CHANGE).sendToTarget();
        }else if(eventName.equals(OEventName.LOGIN_SUCCESS)){
            handlerParent.obtainMessage(LOGIN_SUCCESS).sendToTarget();
        }else if(eventName.equals(OEventName.CHANGE_USER_INFO_OK)){
            handlerParent.obtainMessage(CHANGE_USER_INFO_OK).sendToTarget();
        }else if(eventName.equals(OEventName.EXIT_LOGIN)){
            handlerParent.obtainMessage(EXIT_LOGIN).sendToTarget();
        }else if(eventName.equals(OEventName.BLUE_CONN_PROGRESS_BEGIN)){
            title_head.my_progress.begin();
        }else if(eventName.equals(OEventName.BLUE_CONN_PROGRESS_FAIL)){
            title_head.my_progress.failed();
        }else if(eventName.equals(OEventName.BLUE_CONN_PROGRESS_SUCESS)){
            title_head.my_progress.success();
        }else if(eventName.equals(OEventName.FRAGMENT01_ON_RESUME)){
            onResumeControlCar();
        }else if(eventName.equals(OEventName.AUTHOR_CODRIVER_RESULTBACK)){
           ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), ActivityBorrowSuccess.class);
        }else if(eventName.equals(OEventName.AUTHOR_CODRIVER_RESULTBACK_FAILED)){
            final String error= (String) paramObj;
            GlobalContext.popMessage(error,getResources().getColor(R.color.normal_bg_color_tip_red));
        }else if(eventName.equals(OEventName.AUTHORIZATION_USER_STOPED)){
            //取消授權
            GlobalContext.popMessage("取消授权成功",GlobalContext.getContext().getResources().getColor(R.color.normal_txt_color_cyan));
        }
    }
    private static final int MSG_CLICK_FUNCBTN_HIDE_BUTTONS = 1000;
    private static final int MSG_SHOW_CONTROLRESULT = 1001;
    private static final int CAR_STATUS_SECOND_CHANGE = 1002;
    private static final int CAR_LIGHTFIND_SHOW = 1112;
    private static final int CAR_LIGHTFIND_HIDE = 1113;
    private static final int LOGIN_SUCCESS = 999;
    private static final int CHANGE_USER_INFO_OK = 998;
    private static final int EXIT_LOGIN = 997;

    private long preWarningTime = 0;
    private long preWarningTime2 = 0;//二次防盜的提醒時間

    public void handlerDoMessage(Message msg) {
     final   DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        switch (msg.what){
            case MSG_CLICK_FUNCBTN_HIDE_BUTTONS:
                img_carbody.performClick();
                break;
            case MSG_SHOW_CONTROLRESULT:
                CarControlResult result = (CarControlResult)msg.obj;
                if(result.status == 1) {//成功
                    //1：开启2：熄火3：开锁4：解锁5：开启尾箱6：寻车
                    if(result.instruction == 1) {
                        SoundHelper.playSoundPool(GlobalContext.getContext(), ManagerVoiceSet.getInstance().getStartResId());
                    }else if(result.instruction == 3 || result.instruction == 4) {
                        SoundHelper.playSoundPool(GlobalContext.getContext(), ManagerVoiceSet.getInstance().getLockUpVoiceResId());
                    }else if(result.instruction == 5) {
                        SoundHelper.playSoundPool(GlobalContext.getContext(), R.raw.voice_backpag);
                    }else if(result.instruction == 6) {
                        countDownTimerMyHide.start();
                        SoundHelper.playSoundPool(GlobalContext.getContext(), R.raw.voice_beep_findcar);
                    }
                    GlobalContext.popMessage("控车成功", GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));//Color.parseColor("#90CF26")
                }else{
                    GlobalContext.popMessage("控车失败", GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));//Color.parseColor("#A00000")
                }
                break;
            case CAR_STATUS_SECOND_CHANGE:
                //1.标题
                if(carInfo.isActive == 1)title_head.setTitle(carInfo.num);
                else if(carInfo.ide == 0)title_head.setTitle(carInfo.num);
                else title_head.setTitle(carInfo.num+"(模拟车辆)");
                //2.状态
                side_state.resetData(true);
                //3.按扭
//                btn_pager.changeData();
                //4.车身显示
                img_carbody.changeData();
                //5.闪灯显示
                String carSkinDir = ManagerSkins.getInstance().getSkinZipFolder(getContext())+"/"+ManagerSkins.getInstance().getSkinZipFileName(carInfo.getCarTypeId(), carInfo.carType)+ ".zip";
                Drawable drawablefind = ManagerSkins.getInstance().getPngImage(carSkinDir+"/car_lightfind");
                if (drawablefind != null) img_findcar.setImageDrawable(drawablefind);
                //下面固定三個控車按鈕顯示
                setButtonName();
                //报警状态下提示
          final      long now = System.currentTimeMillis();
                if(now - preWarningTime > 1000*10) {
                    DataCarStatus status = ManagerCarList.getInstance().getCurrentCarStatus();
                    if (status == null) return;
                    if (status.isWarn == 2) {
                        GlobalContext.popMessage("车辆发生警报，请及时查看", getResources().getColor(R.color.popTipWarning));
                    } else if (status.isWarn == 1) {
                        if(now-preWarningTime2>1000*10){
                            new ToastConfirmNormalForAllClick(GlobalContext.getCurrentActivity(), null, ActivityHome.PAGE_IS_BLACKSTYLE)
                                    .withTitle("当前车辆无法手动启动")
                                    .withInfo("请点击开锁后方可手动启动")
                                    .withButton("关锁", "开锁")
                                    .withClick(new ToastConfirmNormalForAllClick.OnButtonClickListener() {
                                        @Override
                                        public void onClickConfirm(int isClickConfirm) {
                                            if (isClickConfirm == 2) {
                                                preWarningTime2=now;
                                                OCtrlCar.getInstance().ccmd1233_controlCar(carInfo, 4, 0);
                                            } else if (isClickConfirm == 1) {
                                                OCtrlCar.getInstance().ccmd1233_controlCar(carInfo, 3, 0);
                                            }
                                        }
                                    })
                                    .show();
                        }
                    }
                    preWarningTime = now;
                }
                break;
            case 1112:
                Log.e("寻车灯", " 显示");
                img_findcar.setVisibility(View.VISIBLE);
                break;
            case 1113:
                Log.e("寻车灯", "隐藏 ");
                img_findcar.setVisibility(View.INVISIBLE);
                break;
            case LOGIN_SUCCESS:
                chageHeadPic();
                break;
            case CHANGE_USER_INFO_OK:
                chageHeadPic();
                break;
            case EXIT_LOGIN :
                title_head.img_left.setImageResource(R.drawable.personal_center);
                break;
        }
    }
    private void chageHeadPic(){
        DataUser dataUser=ManagerLoginReg.getInstance().getCurrentUser();
        if(dataUser!=null&&dataUser.avatarUrl!=null&&dataUser.avatarUrl.length()>10){
            Glide.with(getContext()).load(dataUser.avatarUrl)
                    .placeholder(R.drawable.personal_center)
                    .into( title_head.img_left);
        }
    }
    private CountDownTimerMy countDownTimerMyHide = new CountDownTimerMy(1800, 300) {
        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("寻车灯", " " + millisUntilFinished);
            if (1500 < millisUntilFinished && millisUntilFinished <= 1800) {
                handlerParent.obtainMessage(CAR_LIGHTFIND_SHOW).sendToTarget();
            } else if (1200 < millisUntilFinished && millisUntilFinished <= 1500) {
                handlerParent.obtainMessage(CAR_LIGHTFIND_HIDE).sendToTarget();
            } else if (900 < millisUntilFinished && millisUntilFinished <= 1200) {
                handlerParent.obtainMessage(CAR_LIGHTFIND_SHOW).sendToTarget();
            } else if (600 < millisUntilFinished && millisUntilFinished <= 900) {
                handlerParent.obtainMessage(CAR_LIGHTFIND_HIDE).sendToTarget();
            } else if (300 < millisUntilFinished && millisUntilFinished <= 600) {
                handlerParent.obtainMessage(CAR_LIGHTFIND_SHOW).sendToTarget();
            }
        }

        @Override
        public void onFinish() {
            handlerParent.obtainMessage(CAR_LIGHTFIND_HIDE).sendToTarget();
        }
    };
    public void popView(final boolean isHide) {
//        btn_pager.changeData();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                TranslateAnimation animation ;
//                //相对自身位移百分比 1f==100%
//                if(isHide){
//                    flash_txt.setVisibility(View.VISIBLE);
//                    dot_horizontal.setVisibility(INVISIBLE);
//                    animation=new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
//                }else{
//                    flash_txt.setVisibility(View.INVISIBLE);
//                    btn_pager.addOnPageChangeListener(new DotIndicator(getContext(), dot_horizontal, btn_pager.getPageNum()));
//                    dot_horizontal.setVisibility(VISIBLE);
//                    btn_pager.setCurrentItem(0);//设计要求默认第一个位置，dot_horizontal重新显示会刷到位置1
//                    animation= new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
//                    if(!isFirstGuide){
//                        if(ManagerVoiceSet.getInstance().getIsOpenClickCarVoice()==-1){
//                            SoundHelper.playSoundPool(GlobalContext.getContext(), ManagerVoiceSet.getInstance().getClickCarVoiceResId());
//                        }
//                    }else{
//                        isFirstGuide=false;
//                    }
//                }
//                animation.setInterpolator(new LinearInterpolator());//OvershootInterpolator
//                animation.setFillAfter(true);
//                animation.setDuration(300);
//                //如果清除动画，回还原坐标
////                if(btn_pager.getAlpha() == 0.01f)btn_pager.setAlpha(1f);
//                if(btn_pager.getVisibility() == INVISIBLE){
//                    btn_pager.setVisibility(VISIBLE);
//                }
//                btn_pager.startAnimation(animation);
//                btn_pager.setTag(!isHide);
//            }
//        },100L);
    }


    protected Handler handlerParent = new MyHandler();
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            handlerDoMessage(msg);
        }
    }
}

