package com.mani.car.mekongk1.ctrl;

import android.bluetooth.BluetoothDevice;
import android.os.CountDownTimer;
import android.util.Log;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.staticsfunc.static_assistant.ByteHelper;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.blue.BlueAdapter;
import com.mani.car.mekongk1.common.blue.BluePermission;
import com.mani.car.mekongk1.common.blue.BlueScanner;
import com.mani.car.mekongk1.common.blue.DataReceive;
import com.mani.car.mekongk1.common.blue.ManagerCarStatus;
import com.mani.car.mekongk1.common.blue.OnBlueStateListenerRoll;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;

import java.util.Timer;
import java.util.TimerTask;

import static com.mani.car.mekongk1.common.blue.OnBlueStateListenerRoll.STATE_CONNECTING;
import static com.mani.car.mekongk1.ctrl.CarControlResult.CARCONTROL_SENDED;

/**
 * 100-299
 */
public class OCtrlCheckCarState {
    private static int     CHANGE_TIME = 3;//3,10
    private int countNum = 0;//记时器
    private        CountDownTimer     countDownTimer;
    // ========================out======================
    private static OCtrlCheckCarState _instance;

    protected OCtrlCheckCarState() {
    }

    public static OCtrlCheckCarState getInstance() {
        if (_instance == null)
            _instance = new OCtrlCheckCarState();
        return _instance;
    }

    public void setNeedCheck(boolean needCheck, int timeSecond) {
        if(timeSecond>0)CHANGE_TIME = timeSecond;
        if (countDownTimer != null) countDownTimer.cancel();
        if (needCheck) {
            countNum = 0;
            countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000L) {//每秒记一次
                @Override
                public void onTick(long millisUntilFinished) {
                    countNum ++;
                    if(countNum % CHANGE_TIME == 0){
                        DataCarInfo car = ManagerCarList.getInstance().getCurrentCar();
                        if (car == null) return;
                        if (car.ide == 0) return;//未激活车，不请求
                        if (car.isActive == 1) {
                            OCtrlCar.getInstance().ccmd1219_changeSelectCar(car.ide, 0);
                        }
                    }
                    if(countNum % 2 == 0)ODispatcher.dispatchEvent(OEventName.CAR_STATUS_SECOND_CHANGE);//每二秒刷新车状态
                    if(countNum % 3 == 0){//三秒搜一次蓝牙
                        DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
                        if(currentCar.isBindBluetooth == 1 &&
                                !BlueScanner.getInstance().getIsScanning() &&
                                BlueAdapter.getInstance().current_blue_state < STATE_CONNECTING &&
                                BluePermission.checkPermission(GlobalContext.getCurrentActivity()) == 1){//已开蓝牙就使用蓝牙
                                Log.e("blue3", "启动蓝牙扫秒");
                                BlueScanner.getInstance().scanLeDevice(GlobalContext.getContext(), true, currentCar.bluetoothName, new BlueScanner.OnScanBlueListener() {
                                    @Override
                                    public void onScanedDevice(BluetoothDevice device) {
                                        if (BlueAdapter.getInstance().initializeOK(GlobalContext.getContext())) {
                                            BlueAdapter.getInstance().setOnBlueStateListener(onBlueStateListenerRoll);
                                            BlueAdapter.getInstance().gotoConnDevice(device);
                                        }
                                    }

                                    @Override
                                    public void onScanStop() {
                                    }
                                });
                        }
                    }
                }
                @Override
                public void onFinish() {
                }
            };
            countDownTimer.start();
        }
    }


    private OnBlueStateListenerRoll onBlueStateListenerRoll = new OnBlueStateListenerRoll() {
        @Override
        public void onConnecting() {

        }

        @Override
        public void onConnectedOK() {
            GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BlueAdapter.getInstance().gotoDiscoverService();
                }
            });
        }

        @Override
        public void onConnectedFailed(String error, boolean isNodevice) {
            ODispatcher.dispatchEvent(OEventName.BLUE_CONN_PROGRESS_FAIL);
        }

        @Override
        public void onDiscovering() {

        }

        @Override
        public void onDiscoverOK() {
            TimerTask task = new TimerTask() {
                public void run() {
                    DataCarInfo currentCar = ManagerCarList.getInstance().getCurrentCar();
                    //有验证串的车,连接先发验证串
                    if (currentCar != null && currentCar.blueCarsig != null && currentCar.blueCarsig.length() > 0) {
                        byte[]       bytes = currentCar.blueCarsig.getBytes();
                        final byte[] mess  = DataReceive.newBlueMessage((byte) 1, (byte) 1, bytes);
                        Log.e("blue", "onDiscoverOK sendmessage carsig:" +currentCar.blueCarsig+" "+ ByteHelper.bytesToHexString(mess));//不能UI线程，熄屏无法操作
                        BlueAdapter.getInstance().sendMessage(ByteHelper.bytesToHexString(mess));
                    }
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 200L);
        }

        @Override
        public void onDiscoverFailed(String error, boolean isNoList) {
            ODispatcher.dispatchEvent(OEventName.BLUE_CONN_PROGRESS_FAIL);

        }

        @Override
        public void onMessageSended(byte[] bytes) {
            if (bytes == null) return;
            String byteStr = ByteHelper.bytesToHexString(bytes);
            Log.e("blue", "onMessageSended length:" + bytes.length + " " + byteStr);
            if (bytes.length > 16) {//发验证串成功
                TimerTask task = new TimerTask() {
                    public void run() {
                        BlueAdapter.getInstance().sendMessage("AA 02 55 0A F4");//连接成功!发送开启指令
                        }
                    };
                Timer timer = new Timer();
                timer.schedule(task, 200L);
            } else if (bytes.length == 5 && bytes[0] == ByteHelper.hexStringToBytes("AA")[0]//这是发送开启指令
                    && bytes[1] == 2
                    && bytes[2] == ByteHelper.hexStringToBytes("55")[0]
                    && bytes[3] == ByteHelper.hexStringToBytes("0A")[0]
                    && bytes[4] == ByteHelper.hexStringToBytes("F4")[0]) {
                ODispatcher.dispatchEvent(OEventName.BLUE_CONN_PROGRESS_SUCESS);
            } else {
                CarControlResult result1 = new CarControlResult();
                result1.carId = ManagerCarList.getInstance().getCurrentCarID();
                result1.currentProcess = CARCONTROL_SENDED;
                result1.instruction = OCtrlCar.getInstance().getPreCmd();
                result1.status = 1;
                ODispatcher.dispatchEvent(OEventName.CAR_CONTROL_RESULT, result1);
                GlobalContext.popMessage("控车成功", GlobalContext.getContext().getResources().getColor(R.color.popTipNormal));//Color.parseColor("#90CF26")
            }
        }

        @Override
        public void onDataBack() {

        }

        @Override
        public void onDataReceived(DataReceive data) {
            if (data == null) return;
            DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
            if (data.dataType == 0x21) {
                ManagerCarStatus.setData0x21(data.data, carInfo.ide);
            } else if (data.dataType == 0x22) {
                ManagerCarStatus.setData0x22(data.data, carInfo.ide);
            }
        }

        @Override
        public void onReadRemoteRssi(int rssi, int status) {
            Log.e("blue", "读取到rssi:" + rssi + " status:" + status);
        }

        @Override
        public void needLog(String log) {
            Log.e("blue", "needLog:" + log);
        }
    };
}
