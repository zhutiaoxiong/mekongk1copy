package com.mani.car.mekongk1.model;


import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.mani.car.mekongk1.common.GlobalContext;


public class ManagerGesture {
	private String isOpenGesture;
	private String signPasswordGesture = "";
	// ========================out======================
	private static ManagerGesture _instance;
	private ManagerGesture() {
		init();
	}
	public static ManagerGesture getInstance() {
		if (_instance == null)
			_instance = new ManagerGesture();
		return _instance;
	}
	private void init() {
		loadLocalData();
	}
	// =================================================
	public void loadLocalData() {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
				isOpenGesture=	ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isOpenGesture");
				signPasswordGesture=	ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("signPasswordGesture");
//			}
//		}).start();
	}
	// =================================================
	public String getIsOpenGesture(){
		return isOpenGesture;
	}
	public String getSignPasswordGesture(){
		return signPasswordGesture;
	}
	// =================================================
	public void saveGesture(String isOpen,String signPassword) {
		isOpenGesture = isOpen;
		signPasswordGesture = signPassword;
		ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("isOpenGesture",isOpen);
		ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("signPasswordGesture",signPasswordGesture);
	}
}
