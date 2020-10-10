package com.mani.car.mekongk1.ui.personcenter.carmanager.manager.carseries;

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
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.kulala.staticsview.OnClickListenerMy;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlCar;
import com.mani.car.mekongk1.model.ManagerCommon;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.common.DataBrands;
import com.mani.car.mekongk1.ui.login.RecycleViewDivider;
import com.mani.car.mekongk1.ui.personcenter.know_product.online.OnlineMessageView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 添加新车页面
 */
@CreatePresenter(CarSeriesPresenter.class)
public class ActivityCarSeries extends BaseMvpActivity<CarSeriesView, CarSeriesPresenter> implements OnlineMessageView {
    @BindView(R.id.recycleview_car_seris)
    RecyclerView mRecycleView;
    @BindView(R.id.title)
    ClipTitleHead title;

    private CarSeiesRecycleViewAdapter myRecycleViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    public static DataCarInfo data;
    public static int from ;//1修改2添加
    private DataBrands selectBrand;
    private  String[] arr;
    private String slecteCarBrand;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_series);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        slecteCarBrand= intent.getStringExtra("slecteCarBrand");
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
        title.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if(from==2){
                    ODispatcher.dispatchEvent(OEventName.SELECT_CAR_BRAND,"");
                }
                finish();
            }
        });
    }

    @Override
    public void initViews() {
        if(slecteCarBrand==null||slecteCarBrand.equals(""))return;
            selectBrand = ManagerCommon.getInstance().getBrands(slecteCarBrand);
        if (selectBrand == null ) return;
         arr = selectBrand.getSeriesArr();
        if(arr==null||arr.length==0)return;
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLinearLayoutManager);
        // do not change the size of the RecyclerView
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this,24), getResources().getColor(R.color.normal_bg_color_big_white)));
        //下划线
        if(myRecycleViewAdapter==null){
            myRecycleViewAdapter=new CarSeiesRecycleViewAdapter(arr,ActivityCarSeries.this);
            mRecycleView.setAdapter(myRecycleViewAdapter);
        }else{
            myRecycleViewAdapter.setData(arr);
            myRecycleViewAdapter.notifyDataSetChanged();
        }
        myRecycleViewAdapter.setOnItemClickListener(new CarSeiesRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(arr!=null&&arr.length>0){
                    if(from==1){
                        data.series=arr[position];
                        OCtrlCar.getInstance().ccmd1201_newrepairCar(data);
                    }else{
                        ODispatcher.dispatchEvent(OEventName.SELECT_CAR_SERIES,arr[position]);
                    }
                    finish();
                }
            }
        });
    }
}
