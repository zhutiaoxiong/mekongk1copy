package com.mani.car.mekongk1.ui.personcenter.carmanager.manager.carbrand.brand;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyBrandActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private WaveSideBar mSideBar;
    private SortAdapter mAdapter;
//    private ClearEditText mClearEditText;
    private LinearLayoutManager manager;

    private List<SortModel> mDateList;
    private TitleItemDecoration mDecoration;
    public static DataCarInfo data;
    private DataBrands selectBrand;
    public static int fromPage;//1修改 2  加新车
    private ClipTitleHead titleHead;

    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator mComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_brands);
        initViews();
        initEvents();
    }
    private void  initEvents(){
        titleHead.img_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if(fromPage==2){
                    ODispatcher.dispatchEvent(OEventName.SELECT_CAR_BRAND,"");
                }
                finish();
            }
        });
    }

    private void initViews() {
        if (data != null) {
            selectBrand = ManagerCommon.getInstance().getBrands(data.brand);
        }
        titleHead=(ClipTitleHead) findViewById(R.id.titile);
        mComparator = new PinyinComparator();

        mSideBar = (WaveSideBar) findViewById(R.id.sideBar);

        //设置右侧SideBar触摸监听
        mSideBar.setOnTouchLetterChangeListener(new WaveSideBar.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(letter.charAt(0));
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        List<DataBrands> brands=  ManagerCommon.getInstance().getBrandsList();
        if(brands==null)return;
        mDateList = filledData(brands);
        // 根据a-z进行排序源数据
        Collections.sort(mDateList, mComparator);
        //RecyclerView设置manager
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new SortAdapter(this, mDateList);
        mAdapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String clickStr = mDateList.get(position).getName();
                if(clickStr.length()>1) {
                    if(fromPage==1){
                        if(data!=null){
                            data.brand=clickStr;
                            selectBrand = ManagerCommon.getInstance().getBrands(data.brand);
                            data.brandId = selectBrand.ide;
                            data.logo=selectBrand.logo;
                            OCtrlCar.getInstance().ccmd1201_newrepairCar(data);
                        }
                    }else if(fromPage==2){
                        ODispatcher.dispatchEvent(OEventName.SELECT_CAR_BRAND,clickStr);
                    }
                    finish();
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mDecoration = new TitleItemDecoration(this, mDateList);
        //如果add两个，那么按照先后顺序，依次渲染。
        mRecyclerView.addItemDecoration(mDecoration);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this,1), getResources().getColor(R.color.normal_bg_color_big_white)));


//        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
//
//        //根据输入框输入值的改变来过滤搜索
//        mClearEditText.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
//                filterData(s.toString());
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }

    /**
     * 为RecyclerView填充数据
     *
     * @param dataBrands
     * @return
     */
    private List<SortModel> filledData(List<DataBrands> dataBrands) {
        List<SortModel> mSortList = new ArrayList<>();

        for (int i = 0; i < dataBrands.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(dataBrands.get(i).name);
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(dataBrands.get(i).name);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setLetters(sortString.toUpperCase());
            } else {
                sortModel.setLetters("#");
            }
            sortModel.setImageUrl(dataBrands.get(i).logo);
            mSortList.add(sortModel);
        }
        return mSortList;
    }

    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
//            filterDateList = filledData(getResources().getStringArray(R.array.date));
        } else {
            filterDateList.clear();
            for (SortModel sortModel : mDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                        ) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, mComparator);
        mDateList.clear();
        mDateList.addAll(filterDateList);
        mAdapter.notifyDataSetChanged();
    }
}
