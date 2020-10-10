package com.mani.car.mekongk1.ui.personcenter.carmanager.manager.addcar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.style.LeftTextCenterEdit;
import com.kulala.staticsview.style.LeftTextCenterTxt;
import com.kulala.tools.utils.AToBigA;
import com.kulala.tools.utils.ActivityUtils;
import com.kulala.tools.utils.EditTextUtils;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlCar;
import com.mani.car.mekongk1.model.ManagerCommon;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.common.DataBrands;
import com.mani.car.mekongk1.ui.personcenter.carmanager.manager.carbrand.brand.MyBrandActivity;
import com.mani.car.mekongk1.ui.personcenter.carmanager.manager.carseries.ActivityCarSeries;
import com.mani.car.mekongk1.ui.personcenter.know_product.online.OnlineMessageView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 添加新车页面
 */
@CreatePresenter(AddCarPresenter.class)
public class ActivityAddCar extends BaseMvpActivity<AddCarView, AddCarPresenter> implements OnlineMessageView ,OEventObject{
    @BindView(R.id.title)
    ClipTitleHead title;
    @BindView(R.id.car_brand)
    LeftTextCenterTxt car_brand;
    @BindView(R.id.car_model)
    LeftTextCenterTxt car_model;
    @BindView(R.id.img_car_selected)
    ImageView img_car_selected;
    @BindView(R.id.img_suv_selected)
    ImageView img_suv_selected;
    @BindView(R.id.license_plate)
    LeftTextCenterEdit license_plate;
    @BindView(R.id.btn_confirm)
    TextView btn_confirm;



    private String selectCarBrand;
    private  String selectCarSeries;
    private int brandId;
    private DataBrands selectBrands;
    private int carType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        ButterKnife.bind(this);
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
        img_car_selected.setSelected(true);
        img_suv_selected.setSelected(false);
        carType=1;
        license_plate.txt_center.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        license_plate.txt_center.setTransformationMethod(new AToBigA());
        car_model.txt_center.setSingleLine(true);
        car_brand.txt_center.setSingleLine(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
        ODispatcher.addEventListener(OEventName.SELECT_CAR_BRAND,this);
        ODispatcher.addEventListener(OEventName.SELECT_CAR_SERIES,this);
    }

    @Override
    public void initEvents() {
        img_car_selected.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        img_car_selected.setSelected(true);
                        img_suv_selected.setSelected(false);
                        carType=1;
                        break;
                }
                return false;
            }
        });
        img_suv_selected.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    img_suv_selected.setSelected(true);
                    img_car_selected.setSelected(false);
                    carType=2;
                }
                return false;
            }
        });
        title.img_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                //如果 发送添加车辆协议
                finish();
            }
        });

        car_brand.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                MyBrandActivity.fromPage=2;
                ActivityUtils.startActivity(GlobalContext.getCurrentActivity(), MyBrandActivity.class);
            }
        });
        car_model.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if(selectCarBrand!=null&&selectCarBrand.length()>0){
                    ActivityCarSeries.from=2;
                    ActivityUtils.startActivityTakeOneData(GlobalContext.getCurrentActivity(), ActivityCarSeries.class,"slecteCarBrand",selectCarBrand);
                }else{
                   GlobalContext.popMessage("请先选择车辆型号",getResources().getColor(R.color.normal_bg_color_tip_red));
                }
            }
        });
        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                String carNum = license_plate.txt_center.getText().toString().toUpperCase();
                if(brandId==0 || selectCarBrand==null || selectCarBrand.equals("")){
                    GlobalContext.popMessage("请选择车辆品牌",getResources().getColor(R.color.popTipWarning));
                }else if(selectCarSeries==null || selectCarSeries.equals("")){
                    GlobalContext.popMessage("请选择车辆型号",getResources().getColor(R.color.popTipWarning));
                }else if (carNum.length() <=6) {
                    GlobalContext.popMessage("车牌号输入最少7位!",getResources().getColor(R.color.popTipWarning));
                }else if (!EditTextUtils.plateNumRuleMatch(carNum)) {
                    GlobalContext.popMessage("车牌号格式不正确!",getResources().getColor(R.color.popTipWarning));
                }else{
                    DataCarInfo info=new DataCarInfo();
                    info.brand=selectCarBrand;
                    info.series=selectCarSeries;
                    info.num=carNum;
                    info.brandId=brandId;
                    info.carType=carType;
                    OCtrlCar.getInstance().ccmd1201_newrepairCar(info);
                    finish();
                }
            }
        });
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.SELECT_CAR_BRAND)){
            String brand= (String) o;
            if(!brand.equals("")){
                selectCarBrand=brand;
                selectBrands = ManagerCommon.getInstance().getBrands(selectCarBrand);
                if(selectCarBrand!=null){
                    brandId=selectBrands.ide;
                }
                selectCarSeries="";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        car_model.txt_center.setText("请您输入车辆型号");
                        car_brand.txt_center.setText(selectCarBrand);
                    }
                });
            }
        }else    if(s.equals(OEventName.SELECT_CAR_SERIES)){
            String series= (String) o;
            if(!series.equals("")){
                selectCarSeries=series;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        car_model.txt_center.setText(selectCarSeries);
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ODispatcher.removeEventListener(OEventName.SELECT_CAR_BRAND,this);
        ODispatcher.removeEventListener(OEventName.SELECT_CAR_SERIES,this);
    }
}
