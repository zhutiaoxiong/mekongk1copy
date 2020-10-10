package com.mani.car.mekongk1.ui.personcenter.aboutus.company_profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.mani.car.mekongk1.ClipTitleHead;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;


/**
 * Intent intent = new Intent();
 * Bundle bundle = new Bundle();
 * bundle.putString(ActivityWeb.TITLE_NAME, "用户使用协议");
 * String address;
 * try {
 * ApplicationInfo appInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
 * address = "http://api.91kulala.com/kulala/protocol/protocol.jsp?cid="+appInfo.metaData.getInt("cid");
 * } catch (PackageManager.NameNotFoundException e) {
 * address = "http://api.91kulala.com/kulala/protocol/protocol.jsp?cid="+0;
 * }
 * bundle.putString(ActivityWeb.HTTP_ADDRESS, address);
 * intent.putExtras(bundle);
 * intent.setClass(getContext(), ActivityWeb.class);
 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 * getContext().startActivity(intent);
 * <p>
 * Bundle bundle = this.getIntent().getExtras();
 * String str=bundle.getString("USERNAME");
 */
public class ActivityWeb extends AppCompatActivity {
    public static String HTTP_ADDRESS = "HTTP_ADDRESS";
    public static String TITLE_NAME = "TITLE_NAME";
    private RelativeLayout linlin;
    private WebView web_info;
    private String http_address_use;
    private String titleName;
    private ClipTitleHead titleHead;
    public ActivityWeb() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        web_info = (WebView) findViewById(R.id.web_info);
        linlin = (RelativeLayout) findViewById(R.id.linlin);
        titleHead = (ClipTitleHead) findViewById(R.id.titile);
        Bundle bundle = this.getIntent().getExtras();
        http_address_use = bundle.getString(HTTP_ADDRESS);
        titleName=bundle.getString(TITLE_NAME);
        titleHead.setTitle(titleName);
        initViews();
    }

    @Override
    protected void onPause() {
        web_info.loadUrl("about:blank");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.setStatusBarColorForPage(getResources().getColor(R.color.normal_title_color));
    }

    @Override
    protected void onDestroy() {
        if (web_info != null) {
            web_info.getSettings().setSupportZoom(false);
            web_info.getSettings().setBuiltInZoomControls(false);
            web_info.setVisibility(View.GONE);
            web_info.removeAllViews();
            web_info.stopLoading();
            web_info.setVisibility(View.GONE);
            linlin.removeView(web_info);
            web_info.destroy();
        }
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.onDestroy();
    }

    protected void initViews() {

        if (http_address_use != null) {
            invalidateUI();
        }
    }


    public void invalidateUI() {
        web_info.setWebViewClient(new MyWebViewClient());
        web_info.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//取消硬件加速
        WebSettings webSettings = web_info.getSettings();
//设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
//设置可以访问文件
        webSettings.setAllowFileAccess(true);
//设置支持缩放
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
//加载需要显示的网页
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setDomStorageEnabled(true);
        web_info.loadUrl(http_address_use);
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}