package com.mani.car.mekongk1.ctrl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.links.http.HttpConn;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerPublicData;


/**
 * 100-299
 */
public class OCtrlAuthorization {
	// ========================out======================
	private static OCtrlAuthorization	_instance;
	private OCtrlAuthorization() {
		init();
	}
	public static OCtrlAuthorization getInstance() {
		if (_instance == null)
			_instance = new OCtrlAuthorization();
		return _instance;
	}
	protected void init() {
	}
	// ========================out======================
	public void processResult(int protocol,int status, JsonObject obj, String CACHE_ERROR) {
		switch (protocol) {
			case 1210 :
				backdata_1210_getuserlist(status,obj,CACHE_ERROR);
				break;
			case 1211 :
				backdata_1211_findnewuser(status,obj,CACHE_ERROR);
				break;
			case 1212 :
				backdata_1212_deleteuser(status,obj,CACHE_ERROR);
				break;
			case 1206 :
				backdata_1206_giveauthor(status,obj,CACHE_ERROR);
				break;
			case 1207 :
				backdata_1207_stopauthor(status,obj,CACHE_ERROR);
				break;
			case 1208 :
				backdata_1208_getAuthorUserlist(status,obj,CACHE_ERROR);
				break;
			case 1209 :
				backdata_1209_scan(status,obj,CACHE_ERROR);
				break;
			case 1222 :
				backdata_1222_codriver_confirmauthor(status,obj,CACHE_ERROR);
				break;
		}
	}
	// ============================ccmd==================================
	/** 取联系人列表 **/
	public void ccmd1210_getuserlist() {
		HttpConn.getInstance().sendMessage(null, 1210);
	}
	/** 取授权中联系人列表 **/
	public void ccmd1208_getAuthorUserlist() {
		HttpConn.getInstance().sendMessage(null, 1208);
	}
	/** 添加新联系人 **/
	public void ccmd1211_findnewuser(String phoneNum) {
		JsonObject data = new JsonObject();
		data.addProperty("phoneNum", phoneNum);
		HttpConn.getInstance().sendMessage(data, 1211);
	}
	public void ccmd1211_findnewuser(String phoneNum,String name) {
		JsonObject data = new JsonObject();
		data.addProperty("phoneNum", phoneNum);
		data.addProperty("note",name);
		HttpConn.getInstance().sendMessage(data, 1211);
	}

	/** 删除联系人 **/
	public void ccmd1212_deleteuser(long userId) {
		JsonObject data = new JsonObject();
		data.addProperty("userId", userId);
		HttpConn.getInstance().sendMessage(data, 1212);
	}
	/** 授权副车主 **/
	public void ccmd1206_giveauthor(long carId, JsonArray authoritys, String phoneNum, long startTime, long endTime) {
		JsonObject data = new JsonObject();
		data.addProperty("carId", carId);
		data.add("authoritys", authoritys);
		data.addProperty("phoneNum", phoneNum);
		data.addProperty("startTime", startTime);
		data.addProperty("endTime", endTime);
		HttpConn.getInstance().sendMessage(data, 1206);
	}
	/** 授权副车主 **/
	public void ccmd1206_giveauthor(long carId, JsonArray authoritys, String phoneNum, long startTime, long endTime,int type) {
		JsonObject data = new JsonObject();
		data.addProperty("carId", carId);
		data.add("authoritys", authoritys);
		data.addProperty("phoneNum", phoneNum);
		data.addProperty("startTime", startTime);
		data.addProperty("endTime", endTime);
		data.addProperty("type", type);
		HttpConn.getInstance().sendMessage(data, 1206);
	}
	/** 终止授权副车主 **/
	public void ccmd1207_stopauthor(long userId,long carId) {
		JsonObject data = new JsonObject();
		data.addProperty("userId", userId);
		data.addProperty("carId", carId);
		HttpConn.getInstance().sendMessage(data, 1207);
	}
	/** 终止授权副车主 **/
	public void ccmd1207_stopauthor(long userId,long carId,int type) {
		JsonObject data = new JsonObject();
		data.addProperty("userId", userId);
		data.addProperty("carId", carId);
		data.addProperty("type", type);
		HttpConn.getInstance().sendMessage(data, 1207);
	}
	/** 副车主同意授权和终止授权 type 1：同意授权，2：拒绝授权，3：同意终止授权，4：拒绝终止授权**/
	public void ccmd1222_codriver_confirmauthor(long authorityId, int type) {
		JsonObject data = new JsonObject();
		data.addProperty("authorityId", authorityId);
		data.addProperty("type", type);
		HttpConn.getInstance().sendMessage(data, 1222);
	}
	/** 副车主扫码 **/
	public void ccmd1209_scan(String authCode) {
		JsonObject data = new JsonObject();
		data.addProperty("authCode", authCode);
		HttpConn.getInstance().sendMessage(data, 1209);
	}
	// ============================scmd==================================
	/** 取联系人列表 **/
	private void backdata_1210_getuserlist(int status,JsonObject obj, String CACHE_ERROR) {
//		if(status == 1){
//			ManagerAuthorization.getInstance().saveUserList(OJsonGet.getJsonArray(obj, "userInfos"));
//			ODispatcher.dispatchEvent(OEventName.AUTHORIZATION_USERLIST_RESULTBACK);
//		}
	}
	/** 取授权中联系人列表 **/
	private void backdata_1208_getAuthorUserlist(int status,JsonObject obj, String CACHE_ERROR) {
//		if(status == 1){
//			JsonArray athorInfos = OJsonGet.getJsonArray(obj, "athorInfos");
//			ManagerAuthorization.getInstance().saveAuthoredUserlist(athorInfos);
//			ODispatcher.dispatchEvent(OEventName.AUTHORIZATION_USERLIST_AUTHORED_RESULTBACK);
//		}
	}
	/** 添加新联系人 **/
	private void backdata_1211_findnewuser(int status,JsonObject obj, String CACHE_ERROR) {
//		if(status == 1){
//			ManagerAuthorization.getInstance().saveUserList(OJsonGet.getJsonArray(obj, "userInfos"));
//			ODispatcher.dispatchEvent(OEventName.AUTHORIZATION_USERLIST_RESULTBACK);
//		}
	}
	/** 删除联系人 **/
	private void backdata_1212_deleteuser(int status,JsonObject obj, String CACHE_ERROR) {
//		if(status == 1){
//			ManagerAuthorization.getInstance().saveUserList(OJsonGet.getJsonArray(obj, "userInfos"));
//			ODispatcher.dispatchEvent(OEventName.AUTHORIZATION_USERLIST_RESULTBACK);
//		}
	}
	/** 授权副车主 **/
	private void backdata_1206_giveauthor(int status,JsonObject obj, String CACHE_ERROR) {
		if(status == 1){
			String code = OJsonGet.getString(obj, "authCode");
			ManagerPublicData.authCode=code;
			ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1206");
			ODispatcher.dispatchEvent(OEventName.AUTHOR_CODRIVER_RESULTBACK);
			ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
		}else{
			ODispatcher.dispatchEvent(OEventName.AUTHOR_CODRIVER_RESULTBACK_FAILED,CACHE_ERROR);
		}
	}
	/** 副车主同意授权和终止授权 type 1：同意授权，2：拒绝授权，3：同意终止授权，4：拒绝终止授权**/
		private void backdata_1222_codriver_confirmauthor(int status,JsonObject obj, String CACHE_ERROR) {
			if(status == 1){
				ODispatcher.dispatchEvent(OEventName.CODRIVER_CONFIRM_AUTHOR_RESULTBACK);
			}
		}
	/** 终止授权副车主 **/
	public void backdata_1207_stopauthor(int status,JsonObject obj, String CACHE_ERROR) {
		if(status == 1){
//            JsonArray jsonArray = OJsonGet.getJsonArray(obj, "funInfos");
//			DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
//			carInfo.funInfos = jsonArray;
			ManagerCarList.getInstance().saveCarList(OJsonGet.getJsonArray(obj, "carInfos"), "backdata_1207");
			ODispatcher.dispatchEvent(OEventName.AUTHORIZATION_USER_STOPED);
			ODispatcher.dispatchEvent(OEventName.CAR_LIST_CHANGE);
		}
	}
	/** 副车主扫码 **/
	public void backdata_1209_scan(int status,JsonObject obj, String CACHE_ERROR) {
		if(status == 1){
			ODispatcher.dispatchEvent(OEventName.QRCODE_CODRIVER_RESULTBACK);
		}
	}

}
