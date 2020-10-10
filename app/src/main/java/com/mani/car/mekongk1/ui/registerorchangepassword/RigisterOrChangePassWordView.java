package com.mani.car.mekongk1.ui.registerorchangepassword;

import com.kulala.baseclass.BaseMvpMethod;

public interface RigisterOrChangePassWordView extends BaseMvpMethod {
    void imgClickAgreeOk();
    void imgClickAgreeCancle();
    void txtGetVerfyCodeCountDown(String txt);
    void txtGetVerfyComPlete(String txt);
    void toMakeSurePassWordPage();
    void clearEditText();
    void setTipsText(String cacheError);
    void changUI();
}
