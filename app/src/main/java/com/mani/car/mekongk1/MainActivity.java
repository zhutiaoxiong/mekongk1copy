package com.mani.car.mekongk1;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.kulala.tools.utils.permisionutil.JsPermission;
import com.kulala.tools.utils.permisionutil.JsPermissionListener;
import com.kulala.tools.utils.permisionutil.JsPermissionUtils;
import com.mani.car.mekongk1.R;

/**
 * 安卓6.0动态权限申请的封装 首先在Activity实现JsPermissionListener 会重写两个方法onPermit，onCancel授权成功失败，
 * 重写onRequestPermissionsResult方法里加入 JsPermission.onRequestPermissionResult(requestCode, permissions, grantResults);
 * */
public class MainActivity extends AppCompatActivity implements JsPermissionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//权限类子
        if (JsPermissionUtils.needRequestPermission()) {
            JsPermission.with(this)
                    .requestCode(20)
                    .permission(Manifest.permission.CAMERA)
                    .callBack(this)
                    .send();
        }

    }

    @Override
    public void onPermit(int requestCode, String... permission) {

    }

    @Override
    public void onCancel(int requestCode, String... permission) {
        JsPermissionUtils.getAppDetailSettingIntent(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        JsPermission.onRequestPermissionResult(requestCode, permissions, grantResults);
    }
}
