package com.mani.car.mekongk1.ui.personcenter.personinformation.cutpic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.kulala.baseclass.BaseMvpActivity;
import com.kulala.baseclass.factory.CreatePresenter;
import com.kulala.tools.ImageTools;
import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**主页*/
@CreatePresenter(ClipCutPicPresenter.class)
public class ActivityClipCutPic extends BaseMvpActivity<ClipCutPicView,ClipCutPicPresenter>  implements ClipCutPicView {
    @BindView(R.id.titile)
    ClipTitleHead titile;
    @BindView(R.id.id_clipImageLayout)
    ClipImageLayout mClipImageLayout;
    private String path;
    private ProgressDialog loadingDialog;
    private static final String IMAGE_FILE_NAME = "avatarImage.png";// 头像文件名称

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipimage);
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
        titile.txt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadingDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = mClipImageLayout.clip();
                        String path= Environment.getExternalStorageDirectory()+"/ "+IMAGE_FILE_NAME;
                        ImageTools.savePhotoToSDCard(bitmap,path);
                        loadingDialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("path",path);
                        Log.e( "path2"," path:" + path );
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }).start();
            }
        });
    }

    @Override
    public void initViews() {
        //这步必须要加
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog=new ProgressDialog(this);
        loadingDialog.setTitle("请稍后...");
        path=getIntent().getStringExtra("path");
        Log.e( "1234"," path:" + path );
        if(TextUtils.isEmpty(path)||!(new File(path).exists())){
            Toast.makeText(this, "加载失败",Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bitmap=ImageTools.convertToBitmap(path, 400,400);
        Log.e( "bitmap"," bitmap:" + bitmap );
        if(bitmap==null){
            Toast.makeText(this, "加载失败",Toast.LENGTH_SHORT).show();
            return;
        }
        mClipImageLayout.setBitmap(bitmap);
    }
}
