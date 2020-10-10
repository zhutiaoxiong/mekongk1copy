package com.mani.car.mekongk1.ui.personcenter.help.sugest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;
import com.google.gson.JsonObject;
import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.dispatcher.param.OEventObject;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.OnClickListenerMy;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.ctrl.OCtrlCommon;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.model.ManagerPublicData;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;

@CreatePresenter(SugestPresenter.class)
public class ActivitySugest extends BaseMvpActivity<SugestView, SugestPresenter> implements SugestView, OEventObject {
    @BindView(R.id.edit_info)
    EditText edit_info;
    @BindView(R.id.txt_count)
    TextView txt_count;
    @BindView(R.id.gv_pic)
    GridView gv_pic;
    @BindView(R.id.edit_phone_num)
    EditText edit_phone_num;
    @BindView(R.id.btn_confirm)
    TextView btn_confirm;
    @BindView(R.id.titile)
    ClipTitleHead titile;
    private SugestGridAdapter adapter;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private static final int REQUEST_CAMERA_CODE = 11;
    private ArrayList<String> imagePaths = null;
    private String cotennt = "";
    private String phoneNum = "";
    private String prefix = ""; //头像地址前缀
    private String mytoken = ""; //token
    private String urls = ""; //上传给公司的地址

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_sugest);
        ButterKnife.bind(this);
        initViews();
        initEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }

    @Override
    public void initEvents() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                cotennt = edit_info.getText().toString();
                phoneNum = edit_phone_num.getText().toString();
                txt_count.setText(cotennt.length() + "/200");
                checkBtn_confirmVisible();
            }
        };
        edit_info.addTextChangedListener(watcher);
        edit_phone_num.addTextChangedListener(watcher);
        gv_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imagePaths.size() <= 3) {
                    if (adapter.getmMaxPosition() == 1 || position == adapter.getmMaxPosition() - 1) {
                        OToastSelectPic.getInstance().show(gv_pic, new OToastSelectPic.OnClickButtonListener() {
                            @Override
                            public void onClick(int pos) {
                                if (pos == 1) {
                                    PhotoPickerIntent intent1 = new PhotoPickerIntent(ActivitySugest.this);
                                    intent1.setSelectModel(SelectModel.MULTI);
                                    intent1.setShowCarema(true); // 是否显示拍照
                                    intent1.setMaxTotal(4); // 最多选择照片数量，默认为9
                                    intent1.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                                    startActivityForResult(intent1, REQUEST_CAMERA_CODE);
                                } else if (pos == 2) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        int permissionCamera = checkSelfPermission(Manifest.permission.CAMERA);
                                        int permissionRead = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                                        //拍照权限
                                        if (permissionCamera != PackageManager.PERMISSION_GRANTED || permissionRead != PackageManager.PERMISSION_GRANTED) {
                                            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                        } else {
                                            try {
                                                if (captureManager == null) {
                                                    captureManager = new ImageCaptureManager(ActivitySugest.this);
                                                }
                                                Intent intent3 = captureManager.dispatchTakePictureIntent();
                                                startActivityForResult(intent3, ImageCaptureManager.REQUEST_TAKE_PHOTO);
                                                ManagerPublicData.isNotPopGusture =true;
                                            } catch (IOException e) {
//                                                Toast.makeText(ActivitySugest.this, com.foamtrace.photopicker.R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            if (captureManager == null) {
                                                captureManager = new ImageCaptureManager(ActivitySugest.this);
                                            }
                                            Intent intent3 = captureManager.dispatchTakePictureIntent();
                                            startActivityForResult(intent3, ImageCaptureManager.REQUEST_TAKE_PHOTO);
                                            ManagerPublicData.isNotPopGusture =true;
                                        } catch (IOException e) {
//                                            Toast.makeText(ActivitySugest.this, com.foamtrace.photopicker.R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        PhotoPreviewIntent intent = new PhotoPreviewIntent(ActivitySugest.this);
                        intent.setCurrentItem(position);
                        intent.setPhotoPaths(imagePaths);
                        startActivityForResult(intent, 22);
                    }
                } else if (imagePaths.size() == 4) {
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(ActivitySugest.this);
                    intent.setCurrentItem(position);
                    intent.setPhotoPaths(imagePaths);
                    startActivityForResult(intent, 22);
                }
            }
        });

        btn_confirm.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
//                if (!cotennt.equals("") && imagePaths!=null&&imagePaths.size()>0) {
                    uploadPicToQianNiuAndLocalClint(mytoken);
//                    OCtrlCommon.getInstance().ccmd1313_sendSuggest(1, cotennt, urls);
//                }
            }
        });
    }

    @Override
    public void initViews() {
        txt_count.setText("0" + "/200");
        edit_info.setHint("请填写10个字以上的问题描述以便我们提供更好的帮助");
        if (imagePaths == null) imagePaths = new ArrayList<>();
        adapter = new SugestGridAdapter(imagePaths, ActivitySugest.this);
        gv_pic.setAdapter(adapter);
        ODispatcher.addEventListener(OEventName.GET_UPLOADPIC_TOKEN_RESULTBACK, ActivitySugest.this);
        ODispatcher.addEventListener(OEventName.SUGGEST_HTTP_RESULTBACK, ActivitySugest.this);
        OCtrlCommon.getInstance().cmmd1312_uplloadPic();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    if (data != null) {
                        loadAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    }
                    break;
                //浏览照片
                case 22:
                    if (data != null) {
                        loadAdpater(data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT));
                    }
                    break;
                // 调用相机拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        ArrayList<String> paths = new ArrayList<>();
//                        paths.add(captureManager.getCurrentPhotoPath());
//                        loadAdpater(paths);
//                        ArrayList<String> paths = new ArrayList<>();
//                        paths.add(captureManager.getCurrentPhotoPath());
//                        loadAdpater(paths);
                        if (imagePaths != null) {
                            paths.addAll(imagePaths);
                        }
                        if (paths != null && paths.size() < 4) {
                            paths.add(captureManager.getCurrentPhotoPath());
                            loadAdpater(paths);
                        } else {
                            GlobalContext.popMessage("最多只能选四张图", getResources().getColor(R.color.normal_bg_color_tip_red));
                        }
                    }
                    break;
            }
        }
    }

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths == null) {
            imagePaths = new ArrayList<>();
        }
        imagePaths.clear();
        imagePaths.addAll(paths);
        checkBtn_confirmVisible();
    }


    private void checkBtn_confirmVisible() {
        adapter = new SugestGridAdapter(imagePaths, ActivitySugest.this);
        gv_pic.setAdapter(adapter);
        btn_confirm.setEnabled(false);
        if (!cotennt.equals("") && !phoneNum.equals("") && imagePaths != null && imagePaths.size() > 0) {
//            if(cotennt.length()<10){
//                GlobalContext.popMessage("问题描述不得小于十个文字", getResources().getColor(R.color.normal_bg_color_tip_red));
//                return;
//            }
//            if(phoneNum.length()!=11){
//                GlobalContext.popMessage("请填入11位手机号码", getResources().getColor(R.color.normal_bg_color_tip_red));
//                return;
//            }
            btn_confirm.setEnabled(true);
        }
    }

    private String getSplicingUrl(ArrayList<String> list) {
        if (list != null && list.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    sb.append(list.get(i));
                } else {
                    sb.append(list.get(i) + ",");
                }
            }
            return sb.toString();
        }
        return "";
    }

    @Override
    public void receiveEvent(String s, Object o) {
        if (s.equals(OEventName.GET_UPLOADPIC_TOKEN_RESULTBACK)) {
            mytoken = OJsonGet.getString((JsonObject) o, "uptoken");
            prefix = OJsonGet.getString((JsonObject) o, "prefix");
            //上传到千牛然后上传到公司服务器
        } else if (s.equals(OEventName.SUGGEST_HTTP_RESULTBACK)) {
            boolean result = (boolean) o;
            handleShowCommitResult(result);
        }
    }

    private void uploadPicToQianNiuAndLocalClint(String token) {
        if (token == null || token.equals("")) return;
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

        final StringBuilder sb = new StringBuilder();
        List<File> imageList=new ArrayList<>();
        imageList.clear();
        for (int j=0;j<imagePaths.size();j++){
            try {
                File compressedImageFile = new Compressor(this).compressToFile(new File(imagePaths.get(j)));
                imageList.add(compressedImageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (imageList != null &&imageList.size()>0&& imagePaths.size() > 0&&imageList.size()==imagePaths.size()) {
            for (int i = 0; i < imagePaths.size(); i++) {
                final int position = i;
                String mykey = "pic/avatar/" + userId + "/" +  UUID.randomUUID().toString()+".png";
                //先使用1312接口请求拿token再上传千牛，在更新用户信息。
                uploadManager.put(imageList.get(i), mykey, token, new UpCompletionHandler() {

                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                        System.out.print(info.isOK());
                        if (info != null) {
                            if (info.isOK()) {
                                sb.append(prefix + key + ",");
                                String cacheUrl = sb.toString();
                                if (cacheUrl != null && !cacheUrl.equals("")) {
                                    urls = cacheUrl.substring(0, cacheUrl.length() - 1);
                                    if (urls != null && !urls.equals("")) {
                                        String[] strs = urls.split(",");
                                        if (strs.length == imagePaths.size()) {
//                                            Log.e("key", "当前位置" + position + "当前数据大小" + imagePaths.size() + urls);
                                            OCtrlCommon.getInstance().ccmd1313_sendSuggest(1, cotennt, urls);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }, null);
            }
        }

    }


    @Override
    protected void onDestroy() {
        ODispatcher.removeEventListener(OEventName.GET_UPLOADPIC_TOKEN_RESULTBACK, ActivitySugest.this);
        ODispatcher.removeEventListener(OEventName.SUGGEST_HTTP_RESULTBACK, ActivitySugest.this);
        super.onDestroy();
        ManagerPublicData.isNotPopGusture =false;
    }

    private final ActivitySugest.MyHandler handler = new ActivitySugest.MyHandler(ActivitySugest.this);

    private static class MyHandler extends Handler {
        private final WeakReference<ActivitySugest> mActivity;

        public MyHandler(ActivitySugest activity) {
            mActivity = new WeakReference<ActivitySugest>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ActivitySugest activitySugest = mActivity.get();
            if (activitySugest != null) {
                if (msg.what == 112) {
                    boolean resultOk = (Boolean) msg.obj;
                    if (resultOk) {
                        activitySugest.finish();
                    } else {
                        GlobalContext.popMessage("提交失败请重试", activitySugest.getResources().getColor(R.color.normal_bg_color_tip_red));
                    }
                }
            }
        }
    }

    /**
     * 设置提醒文字
     */
    private void handleShowCommitResult(boolean result) {
        Message message = Message.obtain();
        message.what = 112;
        message.obj = result;
        handler.sendMessage(message);
    }
}
