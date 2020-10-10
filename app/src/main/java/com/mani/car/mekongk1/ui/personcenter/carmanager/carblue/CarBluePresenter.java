package com.mani.car.mekongk1.ui.personcenter.carmanager.carblue;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;

import com.kulala.baseclass.BaseMvpPresenter;

import java.util.ArrayList;
import java.util.List;

public class CarBluePresenter extends BaseMvpPresenter<CarBlueView> {


    /**
     * 扫描蓝牙
     * showTips 是否提示没开蓝牙
     */
    public long preScanTime;
    private ArrayList<BluetoothDevice> scanedDevices;
    public void ScanBlue(Context context, boolean showTips) {
//        //先初始化扫描
//        final String scanError = BlueScanner.getInstance().initialize(context);
//        if(scanError!=null){
//            if(showTips)GlobalContext.popMessage(scanError, context.getResources().getColor(R.color.popTipWarning));
//            return;
//        }else if (BluePermission.checkPermission(GlobalContext.getCurrentActivity()) != 1) {
//            if(showTips)GlobalContext.popMessage("蓝牙未开，请开启手机蓝牙", context.getResources().getColor(R.color.popTipWarning));
//            return;
//        }else {
//            preScanTime = System.currentTimeMillis();
//            BlueScanner.getInstance().scanLeDevice(true, new BlueScanner.OnScanBlueListListener() {
//                @Override
//                public void onScanedEndGetList(ArrayList<BluetoothDevice> devices) {
//                    scanedDevices = devices;
//                    getMvpView().changeBlueAdapter();
//                }
//            });
//        }
    }
    public ArrayList<BluetoothDevice> getScanedDevices() {
        return scanedDevices;
    }
    public void clearScanedDevices() {
        scanedDevices = new ArrayList<BluetoothDevice>();//解绑成功后清掉扫描的数据
    }
    public List<String> getScanedDevicesNames() {
        List<String> names = new ArrayList<>();
        if(scanedDevices == null)return null;
        for(int i = 0;i<scanedDevices.size();i++){
            if (scanedDevices.get(i) != null && !TextUtils.isEmpty(scanedDevices.get(i).getName())){
                names.add(scanedDevices.get(i).getName());
            }
        }
        return names;
    }
}
