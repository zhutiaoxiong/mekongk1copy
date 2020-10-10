package com.mani.car.mekongk1.ui.login;

import com.kulala.baseclass.BaseMvpMethod;
import com.mani.car.mekongk1.model.loginreg.DataUser;

import java.util.List;

public interface LoginView extends BaseMvpMethod{
    void showLoading();
    void hideLoading();
    void showLoginResult(String result);
    void recycleViewAndCenterImgShow(List<DataUser> userHistory);
    void recycleViewAndCenterImgHide();
    void imgClickAgreeOk();
    void imgClickAgreeCancle();
    void clearUserName();
    void clearPassWord();
    void toHomePage();
}
