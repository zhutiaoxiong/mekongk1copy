package com.kulala.baseclass.proxy;


import com.kulala.baseclass.BaseMvpPresenter;
import com.kulala.baseclass.BaseMvpMethod;
import com.kulala.baseclass.factory.PresenterMvpFactory;

/**
 *
 *
 *  代理接口
 */
public interface PresenterProxyInterface<V extends BaseMvpMethod,P extends BaseMvpPresenter<V>> {


    /**
     * 设置创建Presenter的工厂
     * @param presenterFactory PresenterFactory类型
     */
    void setPresenterFactory(PresenterMvpFactory<V, P> presenterFactory);

    /**
     * 获取Presenter的工厂类
     * @return 返回PresenterMvpFactory类型
     */
    PresenterMvpFactory<V,P> getPresenterFactory();


    /**
     * 获取创建的Presenter
     * @return 指定类型的Presenter
     */
    P getMvpPresenter();


}
