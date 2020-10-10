package com.kulala.baseclass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kulala.baseclass.factory.PresenterMvpFactory;
import com.kulala.baseclass.factory.PresenterMvpFactoryImpl;
import com.kulala.baseclass.proxy.BaseMvpProxy;
import com.kulala.baseclass.proxy.PresenterProxyInterface;

/**
 * 继承SimpleActivity的MvpFragment基类
 */
public abstract class BaseMvpView<V extends BaseMvpMethod, P extends BaseMvpPresenter<V>> extends RelativeLayout implements PresenterProxyInterface<V,P> {
    private static final String PRESENTER_SAVE_KEY = "presenter_save_key";
    /**
     * 创建被代理对象,传入默认Presenter的工厂
     */
    private BaseMvpProxy<V,P> mProxy = new BaseMvpProxy<>(PresenterMvpFactoryImpl.<V,P>createFactory(getClass()));
    public BaseMvpView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        LayoutInflater.from(context).inflate(R.layout.titlehead, this, true);
//        img_left = (ImageView) findViewById(R.id.img_left);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mProxy.onResume((V) this);
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mProxy.onDestroy();
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
    public abstract void handlerDoMessage(Message msg);
    protected Handler handlerParent = new MyHandler();
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            handlerDoMessage(msg);
        }
    }
}
