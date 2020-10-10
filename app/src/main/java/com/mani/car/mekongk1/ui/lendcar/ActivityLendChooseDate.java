package com.mani.car.mekongk1.ui.lendcar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.calendar.CalendarView;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlAuthorization;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.ui.controlcar.borrowcar.borrowsuccess.ActivityBorrowSuccess;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(LendChooseDatePresenter.class)
public class ActivityLendChooseDate extends BaseMvpActivity<LendChooseDate, LendChooseDatePresenter> implements LendChooseDate, OEventObject {
    @BindView(R.id.title_head)
    ClipTitleHead title_head;
    @BindView(R.id.calendar)
    CalendarView calendar;
    @BindView(R.id.txt_datefromto)
    TextView txt_datefromto;
    @BindView(R.id.txt_use_way)
    TextView txt_use_way;
    @BindView(R.id.txt_use_way_copy_tips)
    TextView txt_use_way_copy_tips;
    @BindView(R.id.txt_use_way_copy)
    TextView txt_use_way_copy;
    @BindView(R.id.btn_confirm)
    TextView btn_confirm;
    private Date myselectedStartDate;
    private Date myselectedEndDate;
    private int myChoice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_choose_date);
        ButterKnife.bind(this);
        calendar.setSelectMore(true);
        initViews();
        initEvents();
        ODispatcher.addEventListener(OEventName.AUTHOR_CODRIVER_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.AUTHOR_CODRIVER_RESULTBACK_FAILED, this);
        ODispatcher.addEventListener(OEventName.BORROW_CAR_BACK_CONTROL_CAR_PAGE,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ODispatcher.removeEventListener(OEventName.AUTHOR_CODRIVER_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.AUTHOR_CODRIVER_RESULTBACK_FAILED, this);
        ODispatcher.removeEventListener(OEventName.BORROW_CAR_BACK_CONTROL_CAR_PAGE,this);
    }

    @Override
    public void initEvents() {
        title_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLendChooseDate.this.finish();
            }
        });
        calendar.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void OnItemClick(Date selectedStartDate, Date selectedEndDate, Date downDate) {
                myselectedStartDate = selectedStartDate;
                myselectedEndDate = selectedEndDate;
                txt_datefromto.setText("日期截止:" +
                        DateFormat.format("yyyy年MM月dd日", selectedStartDate)
                        + " 至 " +
                        DateFormat.format("yyyy年MM月dd日", selectedEndDate));
//                Log.e("calendar", "now:" + calendar.getCalendatData() + " start: " + selectedStartDate + " end:" + selectedEndDate + " down:" + downDate);
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityUtils.startActivity(ActivityLendChooseDate.this, ActivityBorrowSuccess.class);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

//                long startTime=myselectedStartDate
                DataCarInfo carInfo= ManagerCarList.getInstance().getCurrentCar();
                if(carInfo==null)return;
                int authorityType=carInfo.authorityType;
                long carId = ManagerCarList.getInstance().getCurrentCarID();
                JsonArray authoritys = new JsonArray();
                if (myChoice == 2) {
                    ManagerPublicData.foreverTime="永久";
                    ManagerPublicData.mystartTime="";
                    ManagerPublicData.endTime="";
                    Calendar calendar = new GregorianCalendar();
                    Date date = new Date();
                    calendar.setTime(date);
                    calendar.add(calendar.YEAR, 10);//把日期往后增加一年.整数往后推,负数往前移动
                    // calendar.add(calendar.DAY_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
                    // calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
                    // calendar.add(calendar.WEEK_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
                     date=calendar.getTime();   //这个时间就是日期往后推一天的结果
                    long time= date.getTime();
                    if(authorityType==0){
                        OCtrlAuthorization.getInstance().ccmd1206_giveauthor(carId, authoritys, ManagerPublicData.phoneNum, System.currentTimeMillis(), time);
                    }else{
                        OCtrlAuthorization.getInstance().ccmd1206_giveauthor(carId, authoritys, ManagerPublicData.phoneNum, System.currentTimeMillis(), time,1);
                    }
                } else {
                    if(carId==0)  { GlobalContext.popMessage("车辆未激活,请先激活车辆", getResources().getColor(R.color.normal_bg_color_tip_red));
                        return;
                    }
                    if (myselectedStartDate == null) {
                        GlobalContext.popMessage("请选择开始时间", getResources().getColor(R.color.normal_bg_color_tip_red));
                        return;
                    }
                    if (myselectedEndDate == null) {
                        GlobalContext.popMessage("请选择结束时间", getResources().getColor(R.color.normal_bg_color_tip_red));
                        return;
                    }
                    ManagerPublicData.foreverTime="";
                    ManagerPublicData.mystartTime= (DateFormat.format("yyyy年MM月dd日", myselectedStartDate)).toString();
                    ManagerPublicData.endTime=(DateFormat.format("yyyy年MM月dd日", myselectedEndDate)).toString();;
                  if(authorityType==0){
                      OCtrlAuthorization.getInstance().ccmd1206_giveauthor(carId, authoritys, ManagerPublicData.phoneNum, myselectedStartDate.getTime(), myselectedEndDate.getTime());
                  }else{
                      OCtrlAuthorization.getInstance().ccmd1206_giveauthor(carId, authoritys, ManagerPublicData.phoneNum, myselectedStartDate.getTime(), myselectedEndDate.getTime(),1);
                  }
                }
            }
        });
        txt_use_way.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIsForeverAuth(2);
            }
        });
        txt_datefromto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ManagerPublicData.phoneNum.equals("")) {
                    setIsForeverAuth(1);
                }
            }
        });

    }

    /**
     * 是否永久授权
     */
    private void setIsForeverAuth(int choice) {
        myChoice = choice;
        txt_use_way_copy_tips.setVisibility(View.INVISIBLE);
        txt_use_way_copy.setVisibility(View.INVISIBLE);
        txt_use_way.setVisibility(View.INVISIBLE);
        calendar.setVisibility(View.INVISIBLE);
        if (choice == 1) {
            //显示日历
            txt_use_way.setVisibility(View.VISIBLE);
            calendar.setVisibility(View.VISIBLE);
        } else if (choice == 2) {
            //永久授权
            txt_use_way_copy_tips.setVisibility(View.VISIBLE);
            txt_use_way_copy.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initViews() {
        txt_use_way.setVisibility(View.INVISIBLE);
        if (!ManagerPublicData.phoneNum.equals("")) {
            txt_use_way.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.AUTHOR_CODRIVER_RESULTBACK)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ActivityUtils.startActivity(ActivityLendChooseDate.this, ActivityBorrowSuccess.class);
                }
            });
        }else if(s.equals(OEventName.AUTHOR_CODRIVER_RESULTBACK_FAILED)){
            final String error= (String) o;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GlobalContext.popMessage(error,getResources().getColor(R.color.normal_bg_color_tip_red));
                }
            });
        }else if(s.equals(OEventName.BORROW_CAR_BACK_CONTROL_CAR_PAGE)){
            finish();
        }
    }
}
