package com.mani.car.mekongk1.ui.controlcar.trajectory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.staticsview.calendar.CalendarView;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ui.lendcar.LendChooseDate;
import com.mani.car.mekongk1.ui.lendcar.LendChooseDatePresenter;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(LendChooseDatePresenter.class)
public class ActivityLendChooseDateForTrajectory extends BaseMvpActivity<LendChooseDate, LendChooseDatePresenter> implements LendChooseDate {
    @BindView(R.id.title_head)
    ClipTitleHead title_head;
    @BindView(R.id.calendar)
    CalendarView calendar;
    @BindView(R.id.btn_confirm)
    TextView btn_confirm;
    private Date myselectedStartDate;
    private Date myselectedEndDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_choose_date_for_trajctory);
        ButterKnife.bind(this);
        calendar.setSelectMore(true);
        calendar.setChooseMode(CalendarView.TYPE_SELECT_DAY_BEFORE_TODAY);
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
    }

    @Override
    public void initEvents() {
        title_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLendChooseDateForTrajectory.this.finish();
            }
        });
        calendar.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void OnItemClick(Date selectedStartDate, Date selectedEndDate, Date downDate) {
                myselectedStartDate = selectedStartDate;
                myselectedEndDate = selectedEndDate;
//                Log.e("calendar", "now:" + calendar.getCalendatData() + " start: " + selectedStartDate + " end:" + selectedEndDate + " down:" + downDate);
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("startTime", myselectedStartDate.getTime());
                intent.putExtra("endTime", myselectedEndDate.getTime());
                //设置返回数据
                ActivityLendChooseDateForTrajectory.this.setResult(RESULT_OK, intent);
                //关闭Activity
                ActivityLendChooseDateForTrajectory.this.finish();
            }
        });
    }


    @Override
    public void initViews() {

    }
}
