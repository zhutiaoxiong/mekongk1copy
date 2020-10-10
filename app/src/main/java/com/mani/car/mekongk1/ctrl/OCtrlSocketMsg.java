package com.mani.car.mekongk1.ctrl;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.LogMe;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_assistant.SoundHelper;
import com.kulala.staticsfunc.static_system.NotificationUtils;
import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.kulala.staticsview.toast.ToastConfirmNormal;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.SoundAlertZhu;
import com.mani.car.mekongk1.common.blue.BlueAdapter;
import com.mani.car.mekongk1.common.socketutils.BootBroadcastReceiver;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerCommon;
import com.mani.car.mekongk1.model.ManagerDownloadVoice;
import com.mani.car.mekongk1.model.ManagerLoginReg;
import com.mani.car.mekongk1.model.ManagerVoiceSet;
import com.mani.car.mekongk1.model.ManagerWarnings;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.common.DataVoice;
import com.mani.car.mekongk1.ui.home.ActivityHome;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.mani.car.mekongk1.common.blue.OnBlueStateListenerRoll.STATE_CONNECT_OK;
import static com.mani.car.mekongk1.ctrl.CarControlResult.CARCONTROL_ARRIVE_MODEL;
import static com.mani.car.mekongk1.ctrl.CarControlResult.CARCONTROL_SENDED;
import static com.mani.car.mekongk1.ctrl.CarControlResult.CARCONTROL_SUCESS;

/**
 * 1.连接命令（cmd=1，返回cmd=101）
 * 2.心跳命令（cmd=2，返回cmd=102）
 * 3.服务端推送命令（cmd=3，返回cmd=103）个推所有都是cmd 3
 * 4.客户端端推送命令（cmd=4，返回cmd=104）
 */
public class OCtrlSocketMsg {
    //    private static long FogotMessageTime=1000*10;//不理会的超时消息 10S
//    private static long AverageMessageInterval=1000;//平均消息间隔 1000S
//    private static long AverageMessageIntervalADD=100;//平均消息间隔增加或减少 100S
    // ========================out======================
    HashMap<Long, Integer> messageIdList;//已发送的消息ID+控制的指令,value1-6,value10086是发消息
    private OCtrlSocketMsg() {
        messageIdList = new HashMap<Long, Integer>();
    }
    private static OCtrlSocketMsg _instance;
    public static OCtrlSocketMsg getInstance() {
        if (_instance == null)
            _instance = new OCtrlSocketMsg();
        return _instance;
    }
    // ========================out======================
    /**
     * @param JsonData {"mType":4,"content":"【粤A446688】汽车启动成功"
     *                 ,"fromId":0,"createTime":1545102953616,"messageId":"2140013709"
     *                 ,"data":{"alertType":2,"content":"【粤A446688】汽车启动成功","createTime":1545102953701
     *                 ,"alertId":1,"carId":14250},"messageAlertType":0,"isNotice":1}
     */
    public void onReceiveData(final String JsonData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //没有设过默认声音的，先加载
                if(TextUtils.isEmpty(ManagerDownloadVoice.getInstance().getCurrentVoice())){
                    ManagerDownloadVoice.getInstance().saveCurrentVoice(ODBHelper.convertString(DataVoice.toJsonObject(new DataVoice())));
                    ManagerDownloadVoice.getInstance().loadMp3( GlobalContext.getCurrentActivity(),new DataVoice(),"voice_01_startcar", null);
                }
                Gson       gson             = new Gson();
                JsonObject result           = gson.fromJson(JsonData, JsonObject.class);
                int        mType            = OJsonGet.getInteger(result, "mType");//消息类型 1-14
                String     content          = OJsonGet.getString(result, "content");//消息说明
                long       userId           = OJsonGet.getLong(result, "fromId");//用户ID
                String     messageId        = OJsonGet.getString(result, "messageId");//服务端消息指令
                long       createTime       = OJsonGet.getLong(result, "createTime");
                int        isNotice         = OJsonGet.getInteger(result, "isNotice");//是否提示 1
                int        messageAlertType = OJsonGet.getInteger(result, "messageAlertType");//0：不展示弹窗，1：弹窗（我知道了）
                JsonObject data             = OJsonGet.getJsonObject(result, "data");

                final long carId          = OJsonGet.getLong(data, "carId");
                final int  alertType      = OJsonGet.getInteger(data, "alertType");// 1：消息，2：警报，3：安全
                final   int        alertId        = OJsonGet.getInteger(data, "alertId");//1-31
                long       createTimeMesg = OJsonGet.getLong(data, "createTime");//服务端消息时间
                if (messageAlertType == 1) {
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, ActivityHome.PAGE_IS_BLACKSTYLE)
                            .withInfo(content)
                            .withButton("", "我知道了")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm)
                                        OCtrlCommon.getInstance().ccmd1314_isGetMessage(carId, alertType);
                                }
                            }).show();
                }
//                //判定消息超时,服务器控制
//                long now = System.currentTimeMillis();
//                long distance = now - createTime;
//                if(distance > FogotMessageTime + AverageMessageInterval)return;//超时的消息不理
//                if(distance > AverageMessageInterval + AverageMessageIntervalADD*2)AverageMessageInterval += AverageMessageIntervalADD;//均值增加
//                else if(distance < AverageMessageInterval - AverageMessageIntervalADD*2)AverageMessageInterval -= AverageMessageIntervalADD;//均值减少
//                Log.e("Average","AverageMessageInterval:"+AverageMessageInterval + " distance:"+distance);
//                //判定消息超时
                switch (mType) {
                    case 2://2：汽车控制结果消息（云端发送）,蓝牙模式不收socket控制状态事件
                        //蓝牙模式不收socket控制状态事件
                        if(BlueAdapter.current_blue_state >= STATE_CONNECT_OK)return;
                        CarControlResult result11 = new CarControlResult();
                        result11.fromData(data);
                        result11.currentProcess = CARCONTROL_SUCESS;
//                        ODispatcher.dispatchEvent(OEventName.CAR_CONTROL_RESULT, result11);//http与此重复，不发消息，纯socket控车才发
                        break;
                    case 3://3：汽车状态更新消息（云端发送）只要有此状态就一定刷新
                        if (data == null) return;
                        //蓝牙模式不收socket控制状态事件
                        if(BlueAdapter.current_blue_state >= STATE_CONNECT_OK)return;
                        JsonObject carStatusInfo = OJsonGet.getJsonObject(data, "carStatusInfo");
                        carStatusInfo.addProperty("ide", carId);
                        ManagerCarList.getInstance().saveCarStatus(carStatusInfo);
                        ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);
                        break;
                    case 4://4：汽车预警消息（云端发送）
                        if (isNotice == 1) {//提示消息,内外都提示
                            String            title             = "安酷拉K1消息提醒:";
                            String            info              = content + "  " + ODateTime.time2StringHHmm(createTime);
                            NotificationUtils notificationUtils = new NotificationUtils(GlobalContext.getContext());
                            notificationUtils.sendNotification(title, info, R.drawable.push);
                            if (alertType == 3 || alertType == 2) {//保存的警告
                                ManagerWarnings.getInstance().saveNewWarnings(data);//新消息存入缓存
                            }
                            if (alertType == 3 && ManagerVoiceSet.getInstance().getIsOpenAlertVoice() == -1) {//警告声音
                                SoundHelper.playSoundPool(GlobalContext.getContext(), ManagerVoiceSet.getInstance().getAlertVoiceResId());
                            }
                            if (alertId != 0) {//语音
                                DataVoice dataVoice=DataVoice.fromJsonObject(ODBHelper.convertJsonObject(ManagerDownloadVoice.getInstance().getCurrentVoice()));
                                if(dataVoice==null)dataVoice=new DataVoice();
                                String folder  =  ManagerDownloadVoice.getInstance().getMp3Folder(GlobalContext.getContext(), dataVoice);
                                String fileName=SoundAlertZhu.getSoundIdFrom(alertId);
                                if(fileName==null||fileName.equals(""))return;
                                ManagerDownloadVoice.getInstance().playMp3(folder+ File.separator+ fileName+".mp3");
                            }
                        }
                        break;
                    case 6://6：聊天消息（云端推送给客户端）
                        break;
                    case 7://7：踢出登录（云端推送，客户端收到这条消息，则退出登录）
                        Log.e("LoginOut", "LoginOut from socket");
                        ManagerCommon.getInstance().exitToLogin(content);
                        break;
                    case 8://8：其它通知的弹窗
                        GlobalContext.popMessage(content,GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));
                        OCtrlCar.getInstance().ccmd1203_getcarlist(true);//副车主变化要刷新车列表
                        break;
                    case 9://9：主车主授权和取消授权消息（云端推送，如果收到则直接直接弹框）已取消
                        break;
                    case 10://10：紧急消息（云端推送，如果收到则直接直接弹框）
                        if (data == null) return;
                        ManagerCommon.getInstance().saveMessageUserList(data);
                        new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, true)
                                .withTitle("提示")
                                .withInfo(ManagerCommon.getInstance().messageUserList.content)
                                .withButton("", "确认")
                                .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                    @Override
                                    public void onClickConfirm(boolean isClickConfirm) {
//                                if (isClickConfirm)
//                                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_message_user);
                                    }
                                }).show();
                        break;
                    case 11://11：卡片赠送消息（云端发送，收到后展示卡片详情）
                        break;
                    case 12://12：积分消息（云端发送，收到后弹toast）
                        break;
                    case 13://13：保养消息（云端发送，具体弹出框见data中的汽车保养对象的msgType）
                        break;
                    case 14://14：汽车控制收到信息（云端发送，提示GPS已经收到的消息） 已发送消息到模组
                        //蓝牙模式不收socket控制状态事件
                        if(BlueAdapter.current_blue_state >= STATE_CONNECT_OK)return;
                        CarControlResult result1 = new CarControlResult();
                        result1.fromData(data);
                        result1.currentProcess = CARCONTROL_ARRIVE_MODEL;
                        ODispatcher.dispatchEvent(OEventName.CAR_CONTROL_RESULT, result1);
                        break;
                    case 15://15：副车主授权结果消息（车豆夹使用）
                        GlobalContext.popMessage(content,GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));
                        OCtrlCar.getInstance().ccmd1203_getcarlist(true);//副车主变化要刷新车列表
                        break;
                }
            }
        }).start();
    }



    public void processResult(int protocol, JsonObject obj) {
        switch (protocol) {
            case 104: scmd_104(obj); break;//服务端推送命令（cmd=4，返回cmd=104）
            case 3: scmd_3(obj); break;//服务端推送命令（cmd=3，返回cmd=103）
        }
    }
    // ============================ccmd==================================
    /**
     * 发送聊天信息 客户端端推送命令（cmd=4，返回cmd=104）
     * cmd（1个字节）	length（4个字节）	data（jsonString）
     */
    public void ccmdSendChat(String content) {
        JsonObject data = new JsonObject();//head
        data.addProperty("mType", 5);//发消息
        long fromId = 111;
        long time   = System.currentTimeMillis();
        if (ManagerLoginReg.getInstance().getCurrentUser() != null) {
            fromId = ManagerLoginReg.getInstance().getCurrentUser().userId;
        }
        data.addProperty("fromId", fromId);
        data.addProperty("createTime", time);
        data.addProperty("content", content);
        data.addProperty("messageId", time);//客户端消息指令
        BootBroadcastReceiver.sendMessage(GlobalContext.getContext(), 4, data.toString());
        messageIdList.put(time, 10086);
    }
    /**
     * 控制车 客户端端推送命令（cmd=4，返回cmd=104）
     * instruction控制命令1：开启2：熄火3：开锁4：解锁5：开启尾箱6：寻车 time只有0到7档，每档5分钟，0档位5分钟
     **/
    public void ccmd_controlCar(DataCarInfo car, int controlCmd, int time) {
        //蓝牙模式不收socket控制状态事件
        if (BlueAdapter.current_blue_state>= STATE_CONNECT_OK) return;
        JsonObject data = new JsonObject();
        preCarId = car.ide;
        preCmd = controlCmd;
        data.addProperty("carId", car.ide);
        long currentTime = System.currentTimeMillis();
        data.addProperty("ts", currentTime);
        data.addProperty("instruction", controlCmd);
        data.addProperty("time", time);
        JsonObject dataa = new JsonObject();
        dataa.addProperty("instruction", controlCmd);
        dataa.addProperty("time", time);
        Log.e("LOCK", ">>>>>>>>test CONTROL: 1");
        LogMe.e("TsControl","1.<><>有控制指令发socket:"+controlCmd);
        try {
//            byte[] bytes = AES.AESgenerator(dataa.toString(), car.carsig);
//            if (bytes == null) return;
//            String strBase64 = new String(Base64.encode(bytes, Base64.DEFAULT));
//            data.addProperty("instruction", strBase64);
            Log.e("LOCK", ">>>>>>>>test CONTROL: 2");

            sendToSocket(data, currentTime, controlCmd);
        } catch (Exception e) {
            LogMe.e("TsControl","1.<><>error:"+e.toString());
            Log.e("LOCK", ">>>>>>>>test CONTROL: 2 error");
            e.printStackTrace();
        }
    }
    /**
     * dataa是要发送的数据,messageId10086是聊天
     **/
    private void sendToSocket(JsonObject dataa, long messageId, int controlCmd) {
        JsonObject data = new JsonObject();//head
        data.addProperty("mType", 1);
        long fromId = 111;
        if (ManagerLoginReg.getInstance().getCurrentUser() != null) {
            fromId = ManagerLoginReg.getInstance().getCurrentUser().userId;
        }
        data.addProperty("fromId", fromId);
        data.addProperty("createTime", messageId);//消息时间
        data.addProperty("messageId", messageId);//消息id，客户端生成，为唯一id
        data.add("data", dataa);
        data.addProperty("status", 1);//-1：没法连接到终端，0：不成功，1：成功
        Log.e("推送", "sendToSocket: "+data.toString() );
//        LogMe.e("TsControl","2.准备发Broadcast广播");
        BootBroadcastReceiver.sendMessage(GlobalContext.getContext(), 4, data.toString());
        messageIdList.put(messageId, controlCmd);
        //test
//		try{
//			byte[] rsa = RSA.RSAgenerator(RSA.testStr,RSA.publicKey);
//			byte[] rsaa = RSA.decryptByPrivateKey(rsa,RSA.pkey);
//			String str = new String(rsaa);
//			Log.i("test",str);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
    }
    // ============================scmd==================================
    /**
     * 4.客户端端推送命令（cmd=4，返回cmd=104）
     **/
    private long preCarId;
    private int  preCmd;
    private void scmd_104(JsonObject obj) {
        if (obj == null) return;
        String msgid     = OJsonGet.getString(obj, "msgid");//== messageId//消息id，客户端生成，为唯一id
        long   messageId = Long.valueOf(msgid);
        Integer va = messageIdList.get(messageId);
        if(va == null)return;//没有发出过指令，怎么回了
        int    value     = va;
        messageIdList.remove(messageId);
        if (value == 10086) {
            //messageId10086是聊天
        } else {
            //蓝牙模式不收socket控制状态事件
            if (BlueAdapter.current_blue_state>=STATE_CONNECT_OK) return;
            //是发送控车
            CarControlResult result = new CarControlResult();
            result.carId = preCarId;
            result.currentProcess = CARCONTROL_SENDED;
            result.instruction = preCmd;
            result.status = 1;
//            LogMe.e("TsControl","10. <><>收到汽车控制发送成功:"+msgid);
            ODispatcher.dispatchEvent(OEventName.CAR_CONTROL_RESULT, result);
        }
        //清除错误的消息，未回包的消息
        if (messageIdList.size() > 200) {
            long     now  = System.currentTimeMillis();
            Iterator iter = messageIdList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                long      key   = (Long) entry.getKey();
                if (now - key > 30000L) messageIdList.remove(key);
            }
        }
    }
    private void socketControlCarStatusChane(CarControlResult result){
//        Log.e("純soket", "客戶端 +111");
//        DataCarStatus status= ManagerCarList.getInstance().getCurrentCarStatus();
////        public int isStart;            // 是否启动 0：熄火中，1：已启动
////        public int isON;            // 0：关闭，1：开启，用这个字段来控制启动按钮是否开启，如果isON为1，isStart为0，则点击时弹出“当前车辆正在耗电，请先关闭”
////        public int isTheft;            // 0：解防 白色 1：设防 红色 2:报警 3:二次防盗 ,撤防0 执行设防，isTheft 1,2,3其它的都算设防
////        public int isLock = 1;            // 是否锁定 0：未锁定，1：锁定中
//        if(result.instruction==1){
//            status.isStart=1;
//        }else if(result.instruction==2){
//            status.isStart=0;
//        }else if  (result.instruction==3){
//            status.isLock=1;
//        }else  if(result.instruction==4){
//            status.isLock=0;
//        }
//        LcdManagerCarStatus.sendCarStatus(status);
    }
    /**
     * 1：汽车控制消息（客户端发送）
     2：汽车控制结果消息（云端发送）
     3：汽车状态更新消息（云端发送）
     4：汽车预警消息（云端发送）
     5：聊天消息（客户端推送）
     6：聊天消息（云端推送给客户端）
     7：踢出登录（云端推送，客户端收到这条消息，则退出登录）
     8：副车主授权结果消息（云端推送，如果收到则直接弹个确定框）
     9：主车主授权和取消授权消息（云端推送，如果收到则直接直接弹框）
     10：紧急消息（云端推送，如果收到则直接直接弹框）
     11：卡片赠送消息（云端发送，收到后展示卡片详情）
     12：积分消息（云端发送，收到后弹toast）
     13：保养消息（云端发送，具体弹出框见data中的汽车保养对象的msgType）
     14：汽车控制收到信息（云端发送，提示GPS已经收到的消息）
     */


    /**
     * 3.服务端推送命令（cmd=3，返回cmd=103）
     * @param result {"mType":4,"content":"【粤A446688】汽车启动成功"
     *                 ,"fromId":0,"createTime":1545102953616,"messageId":"2140013709"
     *                 ,"data":{"alertType":2,"content":"【粤A446688】汽车启动成功","createTime":1545102953701
     *                 ,"alertId":1,"carId":14250},"messageAlertType":0,"isNotice":1}
     */
    public void scmd_3(final JsonObject result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Gson gson             = new Gson();
//                JsonObject result           = gson.fromJson(JsonData, JsonObject.class);
                //没有设过默认声音的，先加载
                if(TextUtils.isEmpty(ManagerDownloadVoice.getInstance().getCurrentVoice())){
                    ManagerDownloadVoice.getInstance().saveCurrentVoice(ODBHelper.convertString(DataVoice.toJsonObject(new DataVoice())));
                    ManagerDownloadVoice.getInstance().loadMp3( GlobalContext.getCurrentActivity(),new DataVoice(),"voice_01_startcar", null);
                }
                int        mType            = OJsonGet.getInteger(result, "mType");//消息类型 1-14
                String     content          = OJsonGet.getString(result, "content");//消息说明
                long       userId           = OJsonGet.getLong(result, "fromId");//用户ID
                String     messageId        = OJsonGet.getString(result, "messageId");//服务端消息指令
                long       createTime       = OJsonGet.getLong(result, "createTime");
                int        isNotice         = OJsonGet.getInteger(result, "isNotice");//是否通知栏展示，0：不展示，1：展示
                int        messageAlertType = OJsonGet.getInteger(result, "messageAlertType");//0：不展示弹窗，1：弹窗（我知道了）

                JsonObject data             = OJsonGet.getJsonObject(result, "data");
                final long carId          = OJsonGet.getLong(data, "carId");
                final int  alertType      = OJsonGet.getInteger(data, "alertType");// 1：消息，2：警报，3：安全
                int        alertId        = OJsonGet.getInteger(data, "alertId");//1-31
                long       createTimeMesg = OJsonGet.getLong(data, "createTime");//服务端消息时间

                if (messageAlertType == 1) {
                    new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, false)
                            .withInfo(content)
                            .withButton("", "我知道了")
                            .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                @Override
                                public void onClickConfirm(boolean isClickConfirm) {
                                    if (isClickConfirm) OCtrlCommon.getInstance().ccmd1314_isGetMessage(carId, alertType);//解除警报消息推送
                                }
                            }).show();
                }
//                DataCarInfo carInfo          = ManagerCarList.getInstance().getCurrentCar();
//                //判定消息超时,服务器控制
//                long now = System.currentTimeMillis();
//                long distance = now - createTime;
//                if(distance > FogotMessageTime + AverageMessageInterval)return;//超时的消息不理
//                if(distance > AverageMessageInterval + AverageMessageIntervalADD*2)AverageMessageInterval += AverageMessageIntervalADD;//均值增加
//                else if(distance < AverageMessageInterval - AverageMessageIntervalADD*2)AverageMessageInterval -= AverageMessageIntervalADD;//均值减少
//                Log.e("Average","AverageMessageInterval:"+AverageMessageInterval + " distance:"+distance);
//                //判定消息超时
                switch (mType) {
                    case 2://2：汽车控制结果消息（云端发送）
                        // 蓝牙模式不收socket控制状态事件
                        if (BlueAdapter.current_blue_state >= STATE_CONNECT_OK) return;
                        CarControlResult result11 = new CarControlResult();
                        result11.fromData(data);
                        result11.currentProcess = CARCONTROL_SUCESS;
                        ODispatcher.dispatchEvent(OEventName.CAR_CONTROL_RESULT, result11);//http与此重复，不发消息，纯socket控车才发

                        break;
                    case 3://3：汽车状态更新消息（云端发送）只要有此状态就一定刷新
//                        //蓝牙模式不收socket控制状态事件
//                        if (BlueAdapter.current_blue_state >= STATE_CONNECT_OK) return;
//                        if (data == null) return;
//                        JsonObject carStatusInfo = OJsonGet.getJsonObject(data, "carStatusInfo");
//                        carStatusInfo.addProperty("ide", carId);
//                        ManagerCarList.getInstance().saveCarStatus(carStatusInfo,"scmd_3");//socket有部分数据是没有，比如启动时长
//                        ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);
////                        ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);

                        if (data == null) return;
                        //蓝牙模式不收socket控制状态事件
                        if(BlueAdapter.current_blue_state >= STATE_CONNECT_OK)return;
                        JsonObject carStatusInfo = OJsonGet.getJsonObject(data, "carStatusInfo");
                        carStatusInfo.addProperty("ide", carId);
                        ManagerCarList.getInstance().saveCarStatus(carStatusInfo);
                        ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);

                        break;
                    case 4://4：汽车预警消息（云端发送）
//                        Log.e("TsControl", "预警消息4");
////                        DataWarnings war = DataWarnings.fromJsonObject(data);
////                        if (war.alertId == 17) {
////                            ActivityKulalaMain.areaWar = war;
////                            ODispatcher.dispatchEvent(OEventName.GLOBAL_NEED_CANCEL_AREA);
////                        }
//                        ManagerWarnings.getInstance().saveNewWarnings(data);

                        if (isNotice == 1) {//提示消息,内外都提示
                            String            title             = "安酷拉K1消息提醒:";
                            String            info              = content + "  " + ODateTime.time2StringHHmm(createTime);
                            NotificationUtils notificationUtils = new NotificationUtils(GlobalContext.getContext());
                            notificationUtils.sendNotification(title, info, R.drawable.push);
                            if (alertType == 3 || alertType == 2) {//保存的警告
                                ManagerWarnings.getInstance().saveNewWarnings(data);//新消息存入缓存
                            }
                            if (alertType == 3 && ManagerVoiceSet.getInstance().getIsOpenAlertVoice() == -1) {//警告声音
                                SoundHelper.playSoundPool(GlobalContext.getContext(), ManagerVoiceSet.getInstance().getAlertVoiceResId());
                            }
                            if (alertId != 0) {//语音
                                DataVoice dataVoice=DataVoice.fromJsonObject(ODBHelper.convertJsonObject(ManagerDownloadVoice.getInstance().getCurrentVoice()));
                                if(dataVoice==null)dataVoice=new DataVoice();
                                String folder  =  ManagerDownloadVoice.getInstance().getMp3Folder(GlobalContext.getContext(), dataVoice);
                                String fileName=SoundAlertZhu.getSoundIdFrom(alertId);
                                if(fileName==null||fileName.equals(""))return;
                                ManagerDownloadVoice.getInstance().playMp3(folder+ File.separator+ fileName+".mp3");
                            }
                        }
                        break;
                    case 6://6：聊天消息（云端推送给客户端）
//                        ManagerChat.getInstance().saveChatOne(result);
                        ODispatcher.dispatchEvent(OEventName.CHAT_INFO_BACK, result);
                        break;
                    case 7://7：踢出登录（云端推送，客户端收到这条消息，则退出登录）
                        Log.e("LoginOut", "LoginOut from server");
                        ManagerCommon.getInstance().exitToLogin(content);
                        break;
                    case 8://修改为副车主需要刷新页面,已返回汽车粤888888的权限
                        GlobalContext.popMessage(content,GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));
                        OCtrlCar.getInstance().ccmd1203_getcarlist(true);//副车主变化要刷新车列表
                        break;
                    case 9://9：主车主授权和取消授权消息（云端推送，如果收到则直接直接弹框）已取消
//			ManagerAuthorization.getInstance().saveAuthorInforSocketSingle(data);
//			ODispatcher.dispatchEvent(OEventName.GLOBAL_NEED_POPCONFIRM_NEXT);
                        break;
                    case 10://10：紧急消息（云端推送，如果收到则直接直接弹框）
//                        if (data == null) return;
//                        ManagerCommon.getInstance().saveMessageUserList(data);
//                        new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, false)
//                                .withTitle("提示")
//                                .withInfo(ManagerCommon.getInstance().messageUserList.content)
//                                .withButton("", "确认")
//                                .withClick(new ToastConfirmNormal.OnButtonClickListener() {
//                                    @Override
//                                    public void onClickConfirm(boolean isClickConfirm) {
//                                        if (isClickConfirm)
//                                            ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_message_user);
//                                    }
//                                }).show();

                        if (data == null) return;
                        ManagerCommon.getInstance().saveMessageUserList(data);
                        new ToastConfirmNormal(GlobalContext.getCurrentActivity(), null, true)
                                .withTitle("提示")
                                .withInfo(ManagerCommon.getInstance().messageUserList.content)
                                .withButton("", "确认")
                                .withClick(new ToastConfirmNormal.OnButtonClickListener() {
                                    @Override
                                    public void onClickConfirm(boolean isClickConfirm) {
//                                if (isClickConfirm)
//                                    ODispatcher.dispatchEvent(OEventName.ACTIVITY_KULALA_GOTOVIEW, R.layout.view_me_message_user);
                                    }
                                }).show();
                        break;
//                    case 11://11：卡片赠送消息（云端发送，收到后展示卡片详情）
//                        if (data == null) return;
//                        JsonObject fromUserInfo = OJsonGet.getJsonObject(data, "fromUserInfo");
//                        JsonObject cardInfo = OJsonGet.getJsonObject(data, "cardInfo");
//                        CardSynthesisSuccess.recivieCardInfo = CardInfo.fromJsonObject(cardInfo);
//                        String name = OJsonGet.getString(fromUserInfo, "name");
//                        CardSynthesisSuccess.recicieUserName = name;
//                        ODispatcher.dispatchEvent(OEventName.CARD_RECIVIE, content);
//                        break;
//                    case 12://12：积分消息（云端发送，收到后弹toast）
//                        if (content == null || content.equals("")) return;
//                        ManagerCommon.getInstance().saveMessageUserList(data);
//                        ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST, content);
//                        break;
//                    case 13://13：保养消息（云端发送，具体弹出框见data中的汽车保养对象的msgType）
//                        ManagerMaintainList.getInstance().maintain = DataMaintain.fromJsonObject(data);
//                        if (ManagerMaintainList.getInstance().maintain != null) {
//                            ODispatcher.dispatchEvent(OEventName.MAINTAIN_MESSAGEBACK, content);
//                        }
//                        break;
                    case 14://14：汽车控制收到信息（云端发送，提示GPS已经收到的消息） 已发送消息到模组
                        // 蓝牙模式不收socket控制状态事件
                        //蓝牙模式不收socket控制状态事件
//                        if (BlueLinkReceiver.getInstance().getIsBlueConnOK()) return;
//                        CarControlResult result1 = new CarControlResult();
//                        result1.fromData(data);
//                        result1.currentProcess = CARCONTROL_ARRIVE_MODEL;
//                        ODispatcher.dispatchEvent(OEventName.CAR_CONTROL_RESULT, result1);

                        //蓝牙模式不收socket控制状态事件
                        if(BlueAdapter.current_blue_state >= STATE_CONNECT_OK)return;
                        CarControlResult result1 = new CarControlResult();
                        result1.fromData(data);
                        result1.currentProcess = CARCONTROL_ARRIVE_MODEL;
                        ODispatcher.dispatchEvent(OEventName.CAR_CONTROL_RESULT, result1);
                        break;
                    case 15://15：副车主授权结果消息（车豆夹使用）
//                        GlobalContext.popMessage(content,GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));
//                        OCtrlCar.getInstance().ccmd1203_getcarlist(true);//副车主变化要刷新车列表
                        GlobalContext.popMessage(content,GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));
                        OCtrlCar.getInstance().ccmd1203_getcarlist(true);//副车主变化要刷新车列表
                        break;
                }
                BootBroadcastReceiver.sendMessage(GlobalContext.getContext(), 103, "");
            }
        }).start();
    }
    // ============================ccmd==================================


}