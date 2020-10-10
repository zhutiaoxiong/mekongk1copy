package com.mani.car.mekongk1.tencent.tauth;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by qq522414074 on 2017/3/27.
 */

public class BaseUiListener implements IUiListener {
    @Override
    public void onComplete(Object o) {
//        GlobalContext.popMessage("分享成功",GlobalContext.getContext().getResources().getColor(R.color.normal_txt_color_cyan));
    }

    @Override
    public void onError(UiError uiError) {
//        GlobalContext.popMessage("拒绝分享",GlobalContext.getContext().getResources().getColor(R.color.normal_bg_color_tip_red));
    }

    @Override
    public void onCancel() {
//        GlobalContext.popMessage("取消分享",GlobalContext.getContext().getResources().getColor(R.color.normal_bg_color_tip_red));
    }
}
