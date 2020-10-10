package com.mani.car.mekongk1.wxapi;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.mani.car.mekongk1.common.global.OWXShare;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;





public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口
    public static boolean NEED_WXSHARE_RESULT = false;
    private IWXAPI api; 
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        try {
            api = WXAPIFactory.createWXAPI(this, OWXShare.getAppIdWX(), false);
            api.handleIntent(getIntent(), this);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState); 
    } 
    @Override 
    public void onReq(BaseReq arg0) { } 
 
    @Override 
    public void onResp(BaseResp resp) { 
        switch (resp.errCode) { 
        case BaseResp.ErrCode.ERR_OK:
            if(NEED_WXSHARE_RESULT)ODispatcher.dispatchEvent(OEventName.WX_SHARE_SUCESS);
//            GlobalContext.popMessage("分享成功",GlobalContext.getContext().getResources().getColor(R.color.normal_txt_color_cyan));
            break;
        case BaseResp.ErrCode.ERR_USER_CANCEL:
//            GlobalContext.popMessage("取消分享",GlobalContext.getContext().getResources().getColor(R.color.normal_bg_color_tip_red));
            break;
        case BaseResp.ErrCode.ERR_AUTH_DENIED:
//            GlobalContext.popMessage("拒絕分享",GlobalContext.getContext().getResources().getColor(R.color.normal_bg_color_tip_red));
            break;
        }
        NEED_WXSHARE_RESULT = false;
        this.finish();
    } 
}  
