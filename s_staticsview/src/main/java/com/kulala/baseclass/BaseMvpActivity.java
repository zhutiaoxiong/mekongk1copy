package com.kulala.baseclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.kulala.baseclass.factory.PresenterMvpFactory;
import com.kulala.baseclass.factory.PresenterMvpFactoryImpl;
import com.kulala.baseclass.proxy.BaseMvpProxy;
import com.kulala.baseclass.proxy.PresenterProxyInterface;
import com.kulala.staticsview.R;
import com.umeng.analytics.MobclickAgent;


/**
 * 继承SimpleActivity的MvpFragment基类
 */
public abstract class BaseMvpActivity<V extends BaseMvpMethod, P extends BaseMvpPresenter<V>> extends SimpleActivity implements PresenterProxyInterface<V,P>  {
    private static final String PRESENTER_SAVE_KEY = "presenter_save_key";
    private LinearLayout layout;
    /**
     * 创建被代理对象,传入默认Presenter的工厂
     */
    private BaseMvpProxy<V,P> mProxy = new BaseMvpProxy<>(PresenterMvpFactoryImpl.<V,P>createFactory(getClass()));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        layout=findViewById(R.id.layout);
//        Log.e("perfect-mvp","V onCreate");
//        Log.e("perfect-mvp","V onCreate mProxy = " + mProxy);
//        Log.e("perfect-mvp","V onCreate this = " + this.hashCode());
        if(savedInstanceState != null){
            mProxy.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_SAVE_KEY));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("perfect-mvp","V onResume");
        mProxy.onResume((V) this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.e("perfect-mvp","V onDestroy = " + isChangingConfigurations());
        mProxy.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Log.e("perfect-mvp","V onSaveInstanceState");
        outState.putBundle(PRESENTER_SAVE_KEY,mProxy.onSaveInstanceState());
    }

    @Override
    public void setPresenterFactory(PresenterMvpFactory<V, P> presenterFactory) {
//        Log.e("perfect-mvp","V setPresenterFactory");
        mProxy.setPresenterFactory(presenterFactory);
    }

    @Override
    public PresenterMvpFactory<V, P> getPresenterFactory() {
//        Log.e("perfect-mvp","V getPresenterFactory");
        return mProxy.getPresenterFactory();
    }

    @Override
    public P getMvpPresenter() {
//        Log.e("perfect-mvp","V getMvpPresenter");
        return mProxy.getMvpPresenter();
    }
    public abstract void initEvents();
    public abstract void initViews();


}
