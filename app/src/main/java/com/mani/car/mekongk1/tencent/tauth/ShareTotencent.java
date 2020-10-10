package com.mani.car.mekongk1.tencent.tauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.model.ManagerCommon;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by qq522414074 on 2017/3/27.
 */

public class ShareTotencent extends Activity {
    private Tencent mTencent;
    private TextView tx;
    public  int flag;//分享到QQ还是QQ控件
    private String title;
    private String info;
    private String url;
    private  String sharePic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ceshi);
//        tx = (TextView) findViewById(R.id.tx);
        mTencent = Tencent.createInstance("101539768", this.getApplicationContext());//tencent
        Intent intent=getIntent();
         title=  intent.getStringExtra("title");
         info=  intent.getStringExtra("info");
         url=  intent.getStringExtra("url");
        flag=intent.getIntExtra("flag",0);
        sharePic=intent.getStringExtra("sharePic");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isQQClientAvailable(ShareTotencent.this)) {
            if (flag == 0) {
                onClickShare();
            } else if (flag == 1) {
                shareToQzone();
            }
        }
//        else{
//            GlobalContext.popMessage("手机未安装手机QQ，请下载",GlobalContext.getContext().getResources().getColor(R.color.normal_bg_color_tip_red));
//        }
        finish();
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResult(requestCode, resultCode, data);
    }

    private void onClickShare() {

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, info);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,url);
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ(ShareTotencent.this, params, new BaseUiListener());
    }

    private void shareToQzone() {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填 title
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, info);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,url);//必填
        ArrayList<String> imgList = new ArrayList<>();
        if(sharePic==null||sharePic.equals("")){
            sharePic= ManagerCommon.getInstance().getShareInfo().sharePic;
        }
        imgList.add(sharePic);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgList);
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        mTencent.shareToQzone(ShareTotencent.this, params, new BaseUiListener());
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }
}
