package com.mani.car.mekongk1.ui.personcenter.plate_manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**板块管理*/
@CreatePresenter(PlateManagerPresenter.class)
public class ActivityPlateManager extends BaseMvpActivity<PlateManagerView, PlateManagerPresenter> implements PlateManagerView {
    @BindView(R.id.dslvList)
    DragSortListView listView;
    private AMDragRateAdapter adapter;
    private Button btnEdit;
    private DragSortController mController;
    List<DataPlateManager> l;
    //    listview的数据源
    //图片数据源
    // 监听器在手机拖动停下的时候触发
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
//            from to 分别表示 被拖动控件原位置 和目标位置
            if (from != to) {
                DataPlateManager item = (DataPlateManager) adapter.getItem(from);
//                得到listview的适配器
                adapter.remove(from);
//                在适配器中”原位置“的数据。
                adapter.insert(item, to);
//                在目标位置中插入被拖动的控件。
            }
        }
    };
    //    删除监听器，点击左边差号就触发。删除item操作。
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
            adapter.remove(which);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_manager);
        ButterKnife.bind(this);
        String[] array = {"车辆管理"};//, "OBD管理", "记账管理", "油耗管理","PK板块"
        int[] picArrays = new int[]{1,1,1,1,1};
        //初始化数据源
        l = new ArrayList<DataPlateManager>();
        for (int i = 0; i < array.length; i++) {
            DataPlateManager b = new DataPlateManager();
            b.title = array[i];
            b.src = picArrays[i];
            l.add(b);
        }
        listView.setDropListener(onDrop);
        listView.setRemoveListener(onRemove);
        mController = buildController(listView);
        //设置悬浮框管理器,点击监听器
        listView.setFloatViewManager(mController);
        listView.setOnTouchListener(mController);
        adapter = new AMDragRateAdapter(ActivityPlateManager.this, l);
        listView.setAdapter(adapter);
        listView.setDragEnabled(true); //设置是否可拖动。
    }

    public DragSortController buildController(DragSortListView dslv) {
        DragSortController controller = new DragSortController(dslv);
        //设置拖动对象 id映射View
        controller.setDragHandleId(R.id.drag_handle);
        // 设置移除开关
        controller.setRemoveEnabled(false);
        // 设置拖动/移除 模式
        controller.setDragInitMode(DragSortController.ON_DRAG);
        controller.setRemoveMode(DragSortController.FLING_REMOVE);
        return controller;
    }
    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }


    @Override
    public void initEvents() {
    }

    @Override
    public void initViews() {

    }

    public class DataPlateManager {
        //        放置adapter数据的类
        int src;
        String title;
    }
}

