package com.mani.car.mekongk1.ui.flash;

import com.kulala.baseclass.BaseMvpMethod;

public interface FlashView extends BaseMvpMethod {
    /**跳转主页*/
        void jumpHomePage(String condition);
        /**跳转手势密码页*/
        void jumpGesturePasswordPage();
}
