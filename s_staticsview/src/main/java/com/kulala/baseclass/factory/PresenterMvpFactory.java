package com.kulala.baseclass.factory;


import com.kulala.baseclass.BaseMvpPresenter;
import com.kulala.baseclass.BaseMvpMethod;

/**
 *  Presenter工厂接口
 */
public interface PresenterMvpFactory<V extends BaseMvpMethod,P extends BaseMvpPresenter<V>> {

    /**
     * 创建Presenter的接口方法
     * @return 需要创建的Presenter
     */
    P createMvpPresenter();
}
