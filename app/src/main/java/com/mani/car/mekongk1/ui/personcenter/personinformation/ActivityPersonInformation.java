package com.mani.car.mekongk1.ui.personcenter.personinformation;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.image.CircleImg;
import com.kulala.staticsview.style.LeftText;
import com.kulala.staticsview.style.LeftTextRightImg;
import com.kulala.tools.utils.ActivityUtils;
import com.kulala.tools.utils.FileUtil;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlCommon;
import com.mani.car.mekongk1.ctrl.OCtrlGesture;
import com.mani.car.mekongk1.ctrl.OCtrlRegLogin;
import com.mani.car.mekongk1.model.ManagerGesture;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.mani.car.mekongk1.model.loginreg.DataUser;
import com.mani.car.mekongk1.ui.gusturepassword.gestureedit.ActivityGestureEdit;
import com.mani.car.mekongk1.ui.personcenter.help.sugest.OToastSelectPic;
import com.mani.car.mekongk1.ui.personcenter.personinformation.changeusername.ActivityPersonChangeUserName;
import com.mani.car.mekongk1.ui.personcenter.personinformation.cutpic.ActivityClipCutPic;
import com.mani.car.mekongk1.ui.registerorchangepassword.ActivityRigisterOrChangePassWord;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@CreatePresenter(PersonInformationPresenter.class)
public class ActivityPersonInformation extends BaseMvpActivity<PersonInformationView, PersonInformationPresenter> implements PersonInformationView ,OEventObject{
    @BindView(R.id.change_username)
    LeftText change_username;
    @BindView(R.id.change_gueture_password)
    LeftTextRightImg change_gueture_password;
    @BindView(R.id.change_password)
    LeftText change_password;
    @BindView(R.id.change_phonenum)
    LeftText change_phonenum;
    @BindView(R.id.titile)
    ClipTitleHead titile;
    @BindView(R.id.user_logo)
    CircleImg user_logo;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.phone_number)
    TextView phone_number;
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private String    urlpath;            // 图片本地缓存路径
    private Context mContext;
    private CircleImg avatarImg;// 头像图片
    private String    prefix; //头像地址前缀
    private String    path;//传递到裁剪页面的路径

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_information);
        ButterKnife.bind(this);
        initEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ODispatcher.addEventListener(OEventName.GET_UPLOADPIC_TOKEN_RESULTBACK, this);
        ODispatcher.addEventListener(OEventName.CHANGE_USER_INFO_OK, this);
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
//        Log.e("观看手势密码", "onResume: ActivityPersonInformation ");
        initViews();
    }

    @Override
    public void initEvents() {
        change_username.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivity(ActivityPersonInformation.this, ActivityPersonChangeUserName.class);
            }
        });
        change_password.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivityTakeData(ActivityPersonInformation.this, ActivityRigisterOrChangePassWord.class, "忘记密码");
            }
        });
        change_gueture_password.txt_left.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {

                String isOpenGesture= ManagerGesture.getInstance().getIsOpenGesture();
                if(isOpenGesture.equals("开启")){
                    change_gueture_password.right_img.setImageResource(R.drawable.gesture_close);
                    ManagerGesture.getInstance().saveGesture("关闭", "");
                    OCtrlGesture.getInstance().CCMD_1311_setupGesture(0,"");
                }else{
                    ManagerPublicData.fromPage="已经进系统";
                    ActivityUtils.startActivity(ActivityPersonInformation.this, ActivityGestureEdit.class);
                }
            }
        });
        change_gueture_password.right_img.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                String isOpenGesture= ManagerGesture.getInstance().getIsOpenGesture();
                if(isOpenGesture.equals("开启")){
                    change_gueture_password.right_img.setImageResource(R.drawable.gesture_close);
                    ManagerGesture.getInstance().saveGesture("关闭", "");
                    OCtrlGesture.getInstance().CCMD_1311_setupGesture(0,"");
                }else{
                    ManagerPublicData.fromPage="已经进系统";
                    ActivityUtils.startActivity(ActivityPersonInformation.this, ActivityGestureEdit.class);
                }
            }
        });
        change_phonenum.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                ActivityUtils.startActivityTakeData(ActivityPersonInformation.this, ActivityRigisterOrChangePassWord.class, "更改账号旧手机号");
            }
        });
        user_logo.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                OToastSelectPic.getInstance().show(user_logo, new OToastSelectPic.OnClickButtonListener() {
                    @Override
                    public void onClick(int pos) {
//                        Log.e("posi", "onClick: " );
                        if (pos == 1) {
                            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                            // 如果要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                pickIntent.setType("image/*");
                            startActivityForResult(pickIntent, REQUESTCODE_PICK);
                            ManagerPublicData.isNotPopGusture =true;
                        } else if (pos == 2) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                int permissionCamera = checkSelfPermission(Manifest.permission.CAMERA);
                                int permissionRead = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                                //拍照权限
                                if (permissionCamera != PackageManager.PERMISSION_GRANTED || permissionRead != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                } else {
                                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    //下面这句指定调用相机拍照后的照片存储的路径
                                    File fullPath = new File(DataUser.getHeadCacheDir(), DataUser.IMAGE_FILE_NAME);
//                        fullPath.mkdirs();
                                    Uri uu = Uri.fromFile(fullPath);
                                    if (Build.VERSION.SDK_INT < 24) {
                                        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, uu);
                                        startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                                        ManagerPublicData.isNotPopGusture =true;
                                    } else {
                                        Uri contentUri = FileProvider.getUriForFile(ActivityPersonInformation.this,getPackageName(), fullPath);
                                        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                                        startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                                        ManagerPublicData.isNotPopGusture =true;
                                    }
                                }
                            } else {
                                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //下面这句指定调用相机拍照后的照片存储的路径
                                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(DataUser.getHeadCacheDir(), DataUser.IMAGE_FILE_NAME)));
                                startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                                ManagerPublicData.isNotPopGusture =true;
                            }
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.e("ViewHead", "onActivityResult" + " requestCode:" + requestCode + " resultCode:" + resultCode + " data:" + data);
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        Uri uri = null;
        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                if (data == null) {
//                    ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, "您的机型不支持");
                    return;
                }
                //            try {
////                    Intent intent =new Intent(ViewUserInfoActivity.this,CutPicActivity.class);
//                    startPhotoZoom(data.getData());
//                } catch (NullPointerException e) {
//                    e.printStackTrace();//
//                }]
                ContentResolver resolver = getContentResolver();
                Bitmap bitmap = null;
                try {
                    uri = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path1 = cursor.getString(column_index);
                Log.e("path5", "path1" + path1);
                Intent cutPicIntentTwo = new Intent(ActivityPersonInformation.this, ActivityClipCutPic.class);
                cutPicIntentTwo.putExtra("path", path1);
                startActivityForResult(cutPicIntentTwo, REQUESTCODE_CUTTING);
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
//                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
//                startPhotoZoom(Uri.fromFile(temp));
//                uri = Uri.fromFile(new File(path));
                path = DataUser.getHeadCacheDir() + "/" + DataUser.IMAGE_FILE_NAME;
                Intent cutPicIntentOne = new Intent(ActivityPersonInformation.this, ActivityClipCutPic.class);
                cutPicIntentOne.putExtra("path", path);
                startActivityForResult(cutPicIntentOne, REQUESTCODE_CUTTING);
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    urlpath = data.getStringExtra("path");
                    Log.e("urlpath3", "urlpath" + urlpath);
                    OCtrlCommon.getInstance().cmmd1312_uplloadPic();
//                    setPicToView(data);
//                  ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_userinfo);
                }
                break;
        }
    }


    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/png");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
//          Drawable drawable = new BitmapDrawable(null, photo);
            urlpath = FileUtil.saveFile(mContext, "temphead.png", photo);
//          avatarImg.setImageDrawable(drawable);
            //在服务器端请求网址前缀
            OCtrlCommon.getInstance().cmmd1312_uplloadPic();
        }
    }

    @Override
    public void initViews() {
        DataUser user = ManagerLoginReg.getInstance().getCurrentUser();
        if (user == null) return;
        if (user.name != null && !user.name.equals("")) {
            user_name.setText(user.name);
        }
        if (user.phoneNum != null && !user.phoneNum.equals("")) {
            phone_number.setText(user.phoneNum);
        }
        if (user.avatarUrl != null && !user.avatarUrl.equals("")&&!user.avatarUrl.equals("0")) {
            Glide.with(this).load(user.avatarUrl)
                    .placeholder(R.drawable.push)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource,
                                                    GlideAnimation<? super GlideDrawable> glideAnimation) {
                            user_logo.setImageDrawable(resource);
                        }
                    });
        }
        String isOpenGesture= ManagerGesture.getInstance().getIsOpenGesture();
        if(isOpenGesture.equals("关闭")){
            change_gueture_password.right_img.setImageResource(R.drawable.gesture_close);
        }else{
            change_gueture_password.right_img.setImageResource(R.drawable.gesture_open);
        }
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if(s.equals(OEventName.GET_UPLOADPIC_TOKEN_RESULTBACK)){
            String token = OJsonGet.getString((JsonObject) o, "uptoken");
            prefix = OJsonGet.getString((JsonObject) o, "prefix");
            //上传到千牛然后上传到公司服务器
            uploadPicToQianNiuAndLocalClint(token);
        }else if(s.equals(OEventName.CHANGE_USER_INFO_OK)){
            //改变用户名头像。。。
            changeHistoryHeadPic();
            int a=(Integer)o;
            if(a==1){
                handleSetHeadPicAndUserName();
            }
        }else if(s.equals(OEventName.SETUP_GESTURE_RESULTBACK)){
           handleCancleGesture();
        }
    }
    private void changeHistoryHeadPic(){
        DataUser user=ManagerLoginReg.getInstance().getCurrentUser();
        if(user==null)return;
       List<DataUser> userHisTory= ManagerLoginReg.getInstance().getUserHistory();
       if(userHisTory==null||userHisTory.size()==0)return;
       for (int i=0;i<userHisTory.size();i++){
           if(userHisTory.get(i).userId==user.userId){
               userHisTory.get(i).avatarUrl=user.avatarUrl;
               JsonArray arr = DataUser.toJsonArray(userHisTory);
               ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo( "userHistory", ODBHelper.convertString(arr));
           }
       }
    }
    private void uploadPicToQianNiuAndLocalClint(String token) {
        Configuration config = new Configuration.Builder()
                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
                .connectTimeout(10) // 链接超时。默认10秒
                .responseTimeout(60) // 服务器响应超时。默认60秒
                .recorder(null)  // recorder分片上传时，已上传片记录器。默认null
                .recorder(null, null)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
                .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        UploadManager uploadManager = new UploadManager(config);
        long userId = ManagerLoginReg.getInstance().getCurrentUser() == null ? 0 : ManagerLoginReg.getInstance().getCurrentUser().userId;
        String        key           = "pic/avatar/" + userId + "/" + System.currentTimeMillis() + ".png";
        //先使用1312接口请求拿token再上传千牛，在更新用户信息。

        uploadManager.put(urlpath, key, token, new UpCompletionHandler() {

            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                System.out.print(info.isOK());
                if (info != null) {
                    if (info.isOK()) {
                        DataUser user = ManagerLoginReg.getInstance().getCurrentUser().copy();
                        user.avatarUrl = prefix + key;
                        OCtrlRegLogin.getInstance().CCMD_1110_changeUserInfo(user.toJsonObject());
                    }
                }
            }
        }, null);
    }
    private final ActivityPersonInformation.MyHandler handler=new ActivityPersonInformation.MyHandler(this);
    private static class MyHandler extends Handler {
        private final WeakReference<ActivityPersonInformation> mActivity;
        public MyHandler(ActivityPersonInformation activity) {
            mActivity = new WeakReference<ActivityPersonInformation>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ActivityPersonInformation activityPersonInformation=mActivity.get();
            if(activityPersonInformation!=null){
                if(msg.what==110){
                    activityPersonInformation.initViews();
                }else  if(msg.what==111){
                   GlobalContext.popMessage("手势密码已取消",activityPersonInformation.getResources().getColor(R.color.normal_txt_color_cyan));
                }
            }
        }
    }

    /**设置提醒文字*/
    private void handleSetHeadPicAndUserName(){
        Message message=Message.obtain();
        message.what=110;
        handler.sendMessage(message);
    }
    /**取消手势密码*/
    private void handleCancleGesture(){
        Message message=Message.obtain();
        message.what=111;
        handler.sendMessage(message);
    }

    @Override
    protected void onDestroy() {
        ODispatcher.removeEventListener(OEventName.GET_UPLOADPIC_TOKEN_RESULTBACK, this);
        ODispatcher.removeEventListener(OEventName.CHANGE_USER_INFO_OK, this);
        ManagerPublicData.isNotPopGusture =false;
        super.onDestroy();
    }

}
