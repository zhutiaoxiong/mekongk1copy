package com.mani.car.mekongk1.model;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.blue.BlueAdapter;
import com.mani.car.mekongk1.model.advertising.DataAdvertising;
import com.mani.car.mekongk1.model.common.DataAuthorization;
import com.mani.car.mekongk1.model.common.DataBrands;
import com.mani.car.mekongk1.model.common.DataContact;
import com.mani.car.mekongk1.model.common.DataPayWay;
import com.mani.car.mekongk1.model.common.DataShare;
import com.mani.car.mekongk1.model.common.DataViolation;
import com.mani.car.mekongk1.model.common.DataWxPay;
import com.mani.car.mekongk1.model.common.TrackShareObj;
import com.mani.car.mekongk1.model.messageuser.DataMessageUser;
import com.mani.car.mekongk1.ui.login.ActivityLogin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ManagerCommon {
    public List<DataAuthorization> authorlist;//汽车权限列表
    private List<DataBrands> brandsList;        // 汽车品牌列表
    public List<DataPayWay>        payWayList;
    public List<DataViolation>     violationList;
    public         DataMessageUser messageUserList;//紧急消息列表
    public static  DataContact     contactInfo;
    private static DataShare       shareInfo;
    public static  DataWxPay       wxpayInfo;
    public static  TrackShareObj   trackShareObj;
    public static  String          hotLine;                                            // 服务热线
    public static  String          email;                                                // 电子邮件
    public static  String          dealerLine;                                            // 经销商热线
    public static  DataAdvertising dataAdvertising;//广告

    private long brandUpdateTime = 0;

    // ========================out======================
    private static ManagerCommon _instance;
    private ManagerCommon() {
        violationList = new ArrayList<DataViolation>();
        payWayList = new ArrayList<DataPayWay>();
        messageUserList = new DataMessageUser();
    }
    public static ManagerCommon getInstance() {
        if (_instance == null)
            _instance = new ManagerCommon();
        return _instance;
    }
    // =================================================
    public long getBrandUpdateTime() {
        if (brandUpdateTime == 0) {
            String brandTime = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("brandUpdateTime");
//            String brandTime = ODataShare.loadString(GlobalContext.getContext(), ODataShare.MODE_STATIC, "brandUpdateTime");
            if (brandTime != null) {
                if (!brandTime.equals(""))
                    brandUpdateTime = Long.valueOf(brandTime);
            }
        }
        return brandUpdateTime;
    }
    public List<DataBrands>  getBrandsList(){
        getBrandUpdateTime();
        if (brandsList == null || brandsList.size() == 0){
            //品牌列表from缓存
            String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("brandsList");
//        JsonArray brands = ODataShare.loadJsonArray(GlobalContext.getContext(), ODataShare.MODE_STATIC, "brandsList");
            if (result != null && result.length()>0) {
                this.brandsList = DataBrands.fromJsonArray(ODBHelper.convertJsonArray(result));
            } else {
                //品牌列表from文件
                InputStream inputStream = GlobalContext.getCurrentActivity().getResources().openRawResource(R.raw.brand_default_info);
                String      strrr       = getRawString(inputStream);
                Gson gson        = new Gson();
                JsonArray arrr        = gson.fromJson(strrr, JsonArray.class);
                this.brandsList = DataBrands.fromJsonArray(arrr);
            }
        }
        return this.brandsList;
    }
    public DataBrands getBrands(String brandsName) {
        getBrandsList();
        for (DataBrands brand : brandsList) {
            if (brand.name.equals(brandsName)) {
                return brand;
            }
        }
        return null;
    }
    public DataShare  getShareInfo(){
        if(shareInfo == null){
            //分享内容
            String shareA=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("commonShare");
            JsonObject share =ODBHelper.convertJsonObject(shareA);
            if (share != null) {
                shareInfo = DataShare.fromJsonObject(share);
            }
        }
        return shareInfo;
    }
    public static String getRawString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer   sb     = new StringBuffer("");
        String         line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    // =================================================
    //最多只有一个消息
    public void saveMessageUserList(JsonObject message) {
        this.messageUserList = DataMessageUser.fromJsonObject(message);
    }
    public void saveBrandList(JsonArray brands, long updateTime) {
        if (updateTime <= brandUpdateTime) return;
        if (brands == null || brands.size() <= 0) return;
        this.brandsList = DataBrands.fromJsonArray(brands);
        brandUpdateTime = updateTime;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("brandUpdateTime",String.valueOf(updateTime));
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("brandsList",ODBHelper.convertString(brands));
//        ODataShare.saveJsonArray(GlobalContext.getContext(), ODataShare.MODE_STATIC, "brandsList", brands);
//        ODataShare.saveString(GlobalContext.getContext(), ODataShare.MODE_STATIC, "brandUpdateTime", String.valueOf(updateTime));
    }
    public void saveAuthorList(JsonArray authors) {
        authorlist = new ArrayList<DataAuthorization>();
        if (authors == null) return;
        for (int i = 0; i < authors.size(); i++) {
            JsonObject        object = authors.get(i).getAsJsonObject();
            DataAuthorization author = new DataAuthorization();
            author.jsonObjectToAuthor(object);
            authorlist.add(author);
        }
    }
    public void savePayWay(JsonArray onlinePayInfos) {
        payWayList = DataPayWay.fromJsonArray(onlinePayInfos);
        if (payWayList == null)
            payWayList = new ArrayList<DataPayWay>();
    }
    public void saveContact(JsonObject contact) {
        if (contact == null)
            return;
        contactInfo = DataContact.fromJsonObject(contact);
        if (contactInfo == null)
            contactInfo = new DataContact();
    }
    public void saveShare(JsonObject share) {
        if (share == null)
            return;
        shareInfo = DataShare.fromJsonObject(share);
        if (shareInfo == null)
            shareInfo = new DataShare();
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("commonShare", ODBHelper.convertString(share));
    }
    public void saveWxPay(JsonObject wx) {
        if (wx == null)
            return;
        wxpayInfo = DataWxPay.fromJsonObject(wx);
        if (wxpayInfo == null)
            wxpayInfo = new DataWxPay();
    }
    public void saveViolationList(JsonArray jsonArray) {
        if (jsonArray == null) return;
        violationList = DataViolation.fromJsonArray(jsonArray);
    }
    public void saveTrackShareObj(JsonObject share) {
        if (share == null)
            return;
        trackShareObj = TrackShareObj.fromJsonObject(share);
        if (trackShareObj == null)
            trackShareObj = new TrackShareObj();
    }
    public void saveDataAdvertising(JsonObject dataAdvertisingInfo) {
        if (dataAdvertisingInfo == null)
            return;
        dataAdvertising = DataAdvertising.fromJsonObject(dataAdvertisingInfo);
        if (dataAdvertising == null)
            dataAdvertising = new DataAdvertising();
    }
    //=====================================================
    /**
     * 退出分二种情况，1109自已点击退，被别人逼退
     */
    private long preTime = 0;
    public void exitToLogin(final String error) {
        long now = System.currentTimeMillis();
        if (now - preTime < 1000) return;
        preTime = now;
        if (error == null || error.length() == 0) {//直接退1109
            exitLo();

            Intent intent = new Intent();
            intent.setClass(GlobalContext.getContext(), ActivityLogin.class);//未登录
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            GlobalContext.getContext().startActivity(intent);

            if(GlobalContext.getCurrentActivity() != null)GlobalContext.getCurrentActivity().finish();
        } else {//不弹框，直接显示提示文字
            exitLo();
            GlobalContext.popMessage("您的账号已在其它手机登录！",GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));
        }
    }
    private void exitLo() {
        ManagerCarList.getInstance().saveCarList(null, "exitToLogin");
        ManagerLoginReg.getInstance().exitLogin();//这里会重置socket
        ManagerCarList.getInstance().exit();
        BlueAdapter.getInstance().closeBlueReal();//"Common退出到登录"
        ODispatcher.dispatchEvent(OEventName.CAR_CHOOSE_CHANGE);
        ODispatcher.dispatchEvent(OEventName.EXIT_LOGIN);
    }

}
