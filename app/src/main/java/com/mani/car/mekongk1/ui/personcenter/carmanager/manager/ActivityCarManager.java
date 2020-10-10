package com.mani.car.mekongk1.ui.personcenter.carmanager.manager;

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
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.tools.utils.ActivityUtils;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlCar;
import com.mani.car.mekongk1.ctrl.OCtrlCommon;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.ui.login.RecycleViewDivider;
import com.mani.car.mekongk1.ui.personcenter.carmanager.manager.addcar.ActivityAddCar;
import com.mani.car.mekongk1.ui.personcenter.know_product.online.OnlineMessageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**车辆管理页面*/
@CreatePresenter(CarManagerPresenter.class)
public class ActivityCarManager extends BaseMvpActivity<CarManagerView,CarManagerPresenter>  implements OnlineMessageView,OEventObject {
    @BindView(R.id.car_list_recycleview)
    RecyclerView mRecycleView;
    @BindView(R.id.title)
    ClipTitleHead title;
    private List<DataCarInfo> carList;
    private  CarManagerRecycleViewAdapter myRecycleViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manager);
        ButterKnife.bind(this);
        initViews();
        initEvents();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));

        OCtrlCommon.getInstance().ccmd1303_getCommonInfo();
        ODispatcher.addEventListener(OEventName.CAR_CHOOSE_CHANGE,this);
        ODispatcher.addEventListener(OEventName.CAR_NEW_SUCESS,ActivityCarManager.this);
        ODispatcher.addEventListener(OEventName.CAR_ACTIVATE_SUCESS,ActivityCarManager.this);
        ODispatcher.addEventListener(OEventName.CAR_UNACTIVATE_SUCESS,ActivityCarManager.this);
        ODispatcher.addEventListener(OEventName.CAR_DELETE_SUCESS,ActivityCarManager.this);
        ODispatcher.addEventListener(OEventName.CAR_LIST_CHANGE,ActivityCarManager.this);
        ODispatcher.addEventListener(OEventName.PAY_SUCSESS,ActivityCarManager.this);
    }
    @Override
    protected void onDestroy() {
        ODispatcher.removeEventListener(OEventName.CAR_CHOOSE_CHANGE,this);
        ODispatcher.removeEventListener(OEventName.CAR_NEW_SUCESS,this);
        ODispatcher.removeEventListener(OEventName.CAR_ACTIVATE_SUCESS,this);
        ODispatcher.removeEventListener(OEventName.CAR_UNACTIVATE_SUCESS,this);
        ODispatcher.removeEventListener(OEventName.CAR_DELETE_SUCESS,ActivityCarManager.this);
        ODispatcher.removeEventListener(OEventName.CAR_LIST_CHANGE,ActivityCarManager.this);
        ODispatcher.removeEventListener(OEventName.PAY_SUCSESS,ActivityCarManager.this);
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        OCtrlCar.getInstance().ccmd1203_getcarlist();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }

    @Override
    public void initEvents() {
        title.img_right.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityCarManager.this, ActivityAddCar.class);
            }
        });
    }

    @Override
    public void initViews() {
        carList= ManagerCarList.getInstance().getCarInfoList();
        sortCarList();
        if(carList==null){
            carList.add(new DataCarInfo());
        }
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLinearLayoutManager);
        // do not change the size of the RecyclerView
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this,24), getResources().getColor(R.color.normal_bg_color_big_white)));
        //下划线
        if(carList==null)return;
        if(carList.size()==1&&carList.get(0)!=null&&carList.get(0).ide==0)return;
        if(myRecycleViewAdapter==null){
            myRecycleViewAdapter=new CarManagerRecycleViewAdapter(carList,ActivityCarManager.this);
            mRecycleView.setAdapter(myRecycleViewAdapter);
        }else{
            myRecycleViewAdapter.setData(carList);
            myRecycleViewAdapter.notifyDataSetChanged();
        }
        myRecycleViewAdapter.setOnItemClickListener(new CarManagerRecycleViewAdapter.OnItemClickListener() {

            @Override
            public void onWechat(View view, int position) {

            }

            @Override
            public void onPhone(View view, int position) {

            }
        });
    }
    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.CAR_CHOOSE_CHANGE)){
            refressUI();
        }else if(s.equals(OEventName.CAR_NEW_SUCESS)){
           refressUI();
           ODispatcher.dispatchEvent(OEventName.CAR_CHOOSE_CHANGE);
        }else if(s.equals(OEventName.CAR_ACTIVATE_SUCESS)){
            refressUI();
            ODispatcher.dispatchEvent(OEventName.CAR_CHOOSE_CHANGE);
        }else if(s.equals(OEventName.CAR_UNACTIVATE_SUCESS)){
            refressUI();
            ODispatcher.dispatchEvent(OEventName.CAR_CHOOSE_CHANGE);
        }else if(s.equals(OEventName.CAR_DELETE_SUCESS)){
            refressUI();
        }else if(s.equals(OEventName.CAR_LIST_CHANGE)){
            refressUI();
        }else if(s.equals(OEventName.PAY_SUCSESS)){
            GlobalContext.popMessage("充值成功",getResources().getColor(R.color.normal_txt_color_cyan));
        }
    }
    public void refressUI(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                carList= ManagerCarList.getInstance().getCarInfoList();
                if(carList==null)return;
                if(carList.size()==1&&carList.get(0)!=null&&carList.get(0).ide==0)return;
                sortCarList();//根据选择的车进行排序
                if(myRecycleViewAdapter==null){
                    myRecycleViewAdapter=new CarManagerRecycleViewAdapter(carList,ActivityCarManager.this);
                    mRecycleView.setAdapter(myRecycleViewAdapter);
                }else{
                    myRecycleViewAdapter.setData(carList);
                    myRecycleViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    public void sortCarList(){
        int index=-1;
        if(carList!=null&&carList.size()>0){
            for (int i = 0; i <carList.size() ; i++) {
                if(carList.get(i).ide==ManagerCarList.getInstance().getCurrentCarID()){
                    index=i;
                    break;
                }
            }
        }

        if(index!=-1){
            DataCarInfo dataCarInfo=carList.get(index);
            carList.remove(index);
            carList.add(0,dataCarInfo);
        }
    }
}
