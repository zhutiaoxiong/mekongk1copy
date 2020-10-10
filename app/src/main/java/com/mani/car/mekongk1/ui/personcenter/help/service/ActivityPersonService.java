package com.mani.car.mekongk1.ui.personcenter.help.service;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.loginreg.DataUser;
import com.mani.car.mekongk1.ui.login.RecycleViewDivider;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(PersonServicePresenter.class)
public class ActivityPersonService extends BaseMvpActivity<PersonServiceView,PersonServicePresenter> implements PersonServiceView {
    @BindView(R.id.titile)
    ClipTitleHead titile;
    @BindView(R.id.customer_service_list)
    RecyclerView customer_service_list;
    private  PersonServiceRecycleViewAdapter myRecycleViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<DataUser> userHistory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_service);
        ButterKnife.bind(this);
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

    @Override
    public void initViews() {
        userHistory=new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DataUser dataUser=new DataUser();
            dataUser.avatarUrl="http://image.so.com/v?q=%E5%9B%BE%E7%89%87&cmsid=8c887a69dfc6887af75a5491a926286a&cmran=0&cmras=0&i=0&cmg=7ea62163ea45f0dcaad8cb07833e07ff&src=360pic_strong&z=1#multiple=0&dataindex=0&id=752fba009ee264de0fd66d4c360f5305&itemindex=0&currsn=0&gn=0&cn=0&kn=0";
            dataUser.name="张三";
            userHistory.add(dataUser);
        }
        mLinearLayoutManager = new LinearLayoutManager(this);
        customer_service_list.setLayoutManager(mLinearLayoutManager);
        // do not change the size of the RecyclerView
        customer_service_list.setHasFixedSize(true);
        customer_service_list.setItemAnimator(new DefaultItemAnimator());
        customer_service_list.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, ODipToPx.dipToPx(this,24), getResources().getColor(R.color.normal_bg_color_big_white)));
        //下划线
        if(myRecycleViewAdapter==null){
            myRecycleViewAdapter=new PersonServiceRecycleViewAdapter(userHistory,ActivityPersonService.this);
            customer_service_list.setAdapter(myRecycleViewAdapter);
        }else{
            myRecycleViewAdapter.setData(userHistory);
            myRecycleViewAdapter.notifyDataSetChanged();
        }
        myRecycleViewAdapter.setOnItemClickListener(new PersonServiceRecycleViewAdapter.OnItemClickListener() {

            @Override
            public void onWechat(View view, int position) {

            }

            @Override
            public void onPhone(View view, int position) {

            }
        });
    }
}
