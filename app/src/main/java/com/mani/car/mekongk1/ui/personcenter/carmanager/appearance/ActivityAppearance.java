package com.mani.car.mekongk1.ui.personcenter.carmanager.appearance;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlCar;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerSkins;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.carlist.DataCarSkin;
import com.mani.car.mekongk1.ui.personcenter.know_product.online.OnlineMessageView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**外观更改页面
 * 1. 首进先显示上次保存的
 * 2. 有进度条
 * */
@CreatePresenter(AppearancePresenter.class)
public class ActivityAppearance extends BaseMvpActivity<AppearanceView,AppearancePresenter>  implements OnlineMessageView,OEventObject {
    @BindView(R.id.img_carbody)
    ImageView img_carbody;
    @BindView(R.id.img_sticker)
    ImageView img_sticker;
    @BindView(R.id.txt_mark)
    TextView       txt_mark;
    @BindView(R.id.txt_carcolor)
    TextView       txt_carcolor;
    @BindView(R.id.txt_sticker)
    TextView       txt_sticker;
    @BindView(R.id.pager_image)
    RecyclerView   pager_image;
    @BindView(R.id.title)
    ClipTitleHead   title;

    public static final int SHOW_TYPE_SKIN = 1,SHOW_TYPE_STICKER = 3;
    private int currentShowType = SHOW_TYPE_SKIN;
    private long useCarId;
    private DataCarInfo        carInfo;//当前用的是哪辆车
    private ListSmImageAdapter listImageRecycleAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance);
        ButterKnife.bind(this);
        //    得到跳转到该Activity的Intent对象
        Intent intent = getIntent();
        useCarId = intent.getLongExtra("condition", 0);
        carInfo = ManagerCarList.getInstance().getCarByID(useCarId);
        initViews();
        initEvents();
        OCtrlCar.getInstance().ccmd1410_getSkinList(useCarId);
        ODispatcher.addEventListener(OEventName.CAR_SKIN_LIST_BACK,ActivityAppearance.this);
        ODispatcher.addEventListener(OEventName.CAR_SKIN_CHANGE,ActivityAppearance.this);
    }
    @Override
    protected void onDestroy() {
        ODispatcher.removeEventListener(OEventName.CAR_SKIN_LIST_BACK,this);
        ODispatcher.removeEventListener(OEventName.CAR_SKIN_CHANGE,this);
        super.onDestroy();
    }
    @Override
    public void initViews() {
    }

    @Override
    public void initEvents() {
        txt_carcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_carcolor.setTextColor(getResources().getColor(R.color.txtclBlack));
                txt_sticker.setTextColor(getResources().getColor(R.color.txtclGray));
                currentShowType = SHOW_TYPE_SKIN;
                loadListByTypeId();
                if(carInfo!=null){
                    DataCarSkin skin = ManagerSkins.getInstance().getSkinInfo(carInfo.getCarTypeId(),carInfo.carType);
                    if(skin!=null){
                        txt_mark.setText(skin.title);
                    }
                }
            }
        });
        txt_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_carcolor.setTextColor(getResources().getColor(R.color.txtclGray));
                txt_sticker.setTextColor(getResources().getColor(R.color.txtclBlack));
                currentShowType = SHOW_TYPE_STICKER;
                loadListByTypeId();
                if(carInfo!=null){
                    DataCarSkin sticker = ManagerSkins.getInstance().getStickerInfo(carInfo.getCarStickerId(),carInfo.carType);
                    if(sticker!=null){
                        txt_mark.setText(sticker.title);
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }
    //===============================reload==================================
    private void reloadData(){
        reloadList(ManagerSkins.getInstance().getSkinListSUV());
        reloadList(ManagerSkins.getInstance().getSkinListCAR());
        reloadList(ManagerSkins.getInstance().getStickerListSUV());
        reloadList(ManagerSkins.getInstance().getStickerListCAR());
        if (myThread!= null){
            myThread.interrupt();
        }
        myThread = new MyThread();
        title.my_progress.begin();
        myThread.start();
    }
    private void reloadList(List<DataCarSkin> skinList){
        if(skinList!=null){
            for(DataCarSkin data:skinList){
                if(data.pics[0]!=null && data.pics[0].length()>0)urlArr.add(data.pics[0]);//大图
                if(data.smallPic!=null && data.smallPic.length()>0)urlArr.add(data.smallPic);//小图
            }
        }
    }
    private MyThread myThread;
    HashSet<String> urlArr = new HashSet<>();
    public class MyThread extends Thread {
        @Override
        public void run() {
            Iterator<String> it = urlArr.iterator();
            if (it.hasNext()) {
                final String str = it.next();
//                Log.e("ActivityAppearance", "开始加载:" + str);
                ManagerSkins.getInstance().loadPngFromUrl(getApplicationContext(), str, new ManagerSkins.OnLoadPngListener() {
                    @Override
                    public void loadCompleted(final Drawable drawable) {
                        urlArr.remove(str);
//                        Log.e("ActivityAppearance", "urlArr:" + urlArr.size());
                        if (myThread!= null){
                            myThread.interrupt();
                        }
                        myThread = new MyThread();
                        myThread.start();
                    }
                    @Override
                    public void loadFail(String error) {
//                        Log.e("ActivityAppearance", "加载出错：" + error);
                        if (myThread!= null){
                            myThread.interrupt();
                        }
                        myThread = new MyThread();
                        myThread.start();
                    }
                });
            }else{
                //全部加载完成
                loadCarFaceByTypeId();
                loadListByTypeId();
                title.my_progress.success();
            }
        }
    }
    //===============================reload==================================
    //加载车显示
    private void loadCarFaceByTypeId(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DataCarSkin skin = ManagerSkins.getInstance().getSkinInfo(carInfo.getCarTypeId(),carInfo.carType);
                DataCarSkin sticker = ManagerSkins.getInstance().getStickerInfo(carInfo.getCarStickerId(),carInfo.carType);
                if(skin == null)return;
                Drawable drawableBody = ManagerSkins.getInstance().getPngImage(skin.pics[0]);
                img_carbody.setImageDrawable(drawableBody);
                Drawable drawableSticker;
                if(carInfo.getCarStickerId() == 0){
                    drawableSticker = ManagerSkins.getInstance().getPngImage("");
                    img_sticker.setImageDrawable(drawableSticker);
                }else if(sticker!=null){
                    drawableSticker = ManagerSkins.getInstance().getPngImage(sticker.pics[0]);
                    img_sticker.setImageDrawable(drawableSticker);
                }
                if(currentShowType==SHOW_TYPE_SKIN){
                    txt_mark.setText(skin.title);
                }else if(currentShowType==SHOW_TYPE_STICKER){
                    txt_mark.setText(sticker.title);
                }
            }
        });
    }
    private boolean isFirstLoad = true;
    //加载列表显示
    private void loadListByTypeId(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(isFirstLoad) {
                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(ActivityAppearance.this);
                    mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    pager_image.setLayoutManager(mLinearLayoutManager);
                    // do not change the size of the RecyclerView
                    pager_image.setHasFixedSize(true);
//        pager_image.setItemAnimator(new DefaultItemAnimator());
                    //分割线
                    pager_image.addItemDecoration(new DividerItemDecoration(ActivityAppearance.this, DividerItemDecoration.HORIZONTAL));
//        pager_image.addItemDecoration(new RecycleViewDivider(
//                this, LinearLayoutManager.HORIZONTAL,
//                ODipToPx.dipToPx(this,5), Color.TRANSPARENT));
                    isFirstLoad = false;
                }
                List<DataCarSkin> arr;
                if(carInfo.carType == 2) {//1：轿车，2：SUV
                    if(currentShowType == SHOW_TYPE_SKIN){
                        arr = ManagerSkins.getInstance().getSkinListSUV();
                    }else{// if(showType == SHOW_TYPE_STICKER)
                        arr = ManagerSkins.getInstance().getStickerListSUV();
                    }
                }else{
                    if(currentShowType == SHOW_TYPE_SKIN){
                        arr = ManagerSkins.getInstance().getSkinListCAR();
                    }else{// if(showType == SHOW_TYPE_STICKER)
                        arr = ManagerSkins.getInstance().getStickerListCAR();
                    }
                }
                if(arr == null){
                    Toast.makeText(getApplicationContext(),"无车皮肤数据",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(listImageRecycleAdapter == null){
                    listImageRecycleAdapter=new ListSmImageAdapter(arr,ActivityAppearance.this,carInfo.ide,currentShowType);
                    pager_image.setAdapter(listImageRecycleAdapter);
                }else{
                    listImageRecycleAdapter.setData(arr,currentShowType);
                    listImageRecycleAdapter.notifyDataSetChanged();
                }
                listImageRecycleAdapter.setOnItemClickListener(new ListSmImageAdapter.OnItemClickListener() {
                    @Override
                    public void onClickPicture(int position, DataCarSkin skin) {
                        if(currentShowType == SHOW_TYPE_STICKER){//换车贴
                            OCtrlCar.getInstance().ccmd1403_change_car_skin(carInfo.ide,skin.ide,3);
                        }else if(currentShowType == SHOW_TYPE_SKIN){//换车身
                            OCtrlCar.getInstance().ccmd1403_change_car_skin(carInfo.ide,skin.ide,1);
                        }
                    }
                });
            }
        });
    }
    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.CAR_SKIN_LIST_BACK)){
            reloadData();
//            loadCarFaceByTypeId();
//            loadListByTypeId(currentShowType);
        }else if(s.equals(OEventName.CAR_SKIN_CHANGE)){
            //皮肤改变，刷新显示
            carInfo = ManagerCarList.getInstance().getCarByID(carInfo.ide);
            reloadData();
//                    loadCarFaceByTypeId();
//                    loadListByTypeId(currentShowType);
        }
    }
}
