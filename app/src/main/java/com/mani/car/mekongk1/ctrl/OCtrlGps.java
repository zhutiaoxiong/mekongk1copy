package com.mani.car.mekongk1.ctrl;

import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;
import com.kulala.links.http.HttpConn;
import com.kulala.staticsfunc.static_system.OJsonGet;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerGps;
import com.mani.car.mekongk1.model.carlist.DataCarStatus;
import com.mani.car.mekongk1.ui.navifindcar.AMapUtil;

import java.util.List;



/**
 * 100-299
 */
public class OCtrlGps {
	// ========================out======================
	private static OCtrlGps	_instance;
	protected OCtrlGps() {
		init();
	}
	public static OCtrlGps getInstance() {
		if (_instance == null)
			_instance = new OCtrlGps();
		return _instance;
	}
	protected void init() {
	}
	// ========================out======================
	public void processResult(int protocol, int status,JsonObject obj, String CACHE_ERROR) {
		switch (protocol) {
			case 1213 :
				backdata_1213_getCarPos(status,obj,CACHE_ERROR);
				break;
//			case 1214 :
//				backdata_1214_setArea(status,obj,CACHE_ERROR);
//				break;
			case 1215 :
				backdata_1215_getPathList(status,obj,CACHE_ERROR);
				break;
			case 1216 :
				backdata_1216_favoritePos(status,obj,CACHE_ERROR);
				break;
			case 1217 :
				backdata_1217_favoriteDelete(status,obj,CACHE_ERROR);
				break;
			case 1218 :
				backdata_1218_favoriteList(status,obj,CACHE_ERROR);
				break;
			case 1224 :
				backdata_1224_deletePath(status,obj,CACHE_ERROR);
				break;
			case 1242:
				backdata_1242_findPath(status,obj,CACHE_ERROR);
				break;
			case 1243:
				backdata_1243_addComment(status,obj,CACHE_ERROR);
				break;
			case 1244:
				backdata_1244_GPSAreaCollect(status,obj,CACHE_ERROR);
				break;
			case 1245:
				backdata_1245_getGPSAreaList(status,obj,CACHE_ERROR);
				break;
			case 1246:
				backdata_1246_setNavigation(status,obj,CACHE_ERROR);
				break;
			case 1247:
				backdata_1247_getNavigationInfomation(status,obj,CACHE_ERROR);
				break;
			case 1225:
				backdata_1225_favoriteIntro(status,obj,CACHE_ERROR);
				break;
		}
	}
	// ============================ccmd==================================
	/** 取轨迹列表 **/
//	public void ccmd1215_getPathList(long carId, long startTime, long endTime) {
//		JsonObject data = new JsonObject();
//		data.addProperty("carId", carId);
//		data.addProperty("startTime", startTime);
//		data.addProperty("endTime", endTime);
//		HttpConn.getInstance().sendMessage(data, 1215);
//	}
	public void ccmd1215_getPathList(long carId, long startTime, long endTime,int start,int size) {
		JsonObject data = new JsonObject();
		data.addProperty("carId", carId);
		data.addProperty("startTime", startTime);
		data.addProperty("endTime", endTime);
		data.addProperty("start", start);
		data.addProperty("size", size);
		HttpConn.getInstance().sendMessage(data, 1215);
	}
	/** 取车位置 **/
	public void ccmd1213_getCarPos(long carId,int isDemo) {
		JsonObject data = new JsonObject();
		data.addProperty("carId", carId);
		data.addProperty("isDemo", isDemo);
		HttpConn.getInstance().sendMessage(data, 1213);
	}
//	/** 设车半径 单位是米**/
//	public void ccmd1214_setArea(long carId,int radius,int isOpen) {
//		JsonObject data = new JsonObject();
//		data.addProperty("carId", carId);
//		data.addProperty("radius", radius*1000);
//		data.addProperty("isOpen", isOpen);
//		HttpConn.getInstance().sendMessage(data, 1214);
//	}
	/** 添加，修改，删除 收藏备注 **/
	public void ccmd1225_favoriteIntro(long collectId, String note) {
		JsonObject data = new JsonObject();
		data.addProperty("collectId", collectId);
		data.addProperty("note", note);
		HttpConn.getInstance().sendMessage(data, 1225);
	}
	/** 添加收藏 **/
	public void ccmd1216_favoritePos(String latlng, String location) {
		JsonObject data = new JsonObject();
		data.addProperty("latlng", latlng);
		data.addProperty("location", location);
		HttpConn.getInstance().sendMessage(data, 1216);
	}
	/** 删除收藏 **/
	public void ccmd1217_favoriteDelete(List<Long> deleteList) {
		Gson gson = new Gson();
		String    json = gson.toJson(deleteList);
		JsonArray arr  = gson.fromJson(json, JsonArray.class);
		//==================send======
		JsonObject data = new JsonObject();
		data.add("collectIds", arr);
		HttpConn.getInstance().sendMessage(data, 1217);
	}


	/** 删除轨迹 **/
	public void ccmd1224_deletePath(long trailInfoId,long carId,long startTime, long endTime,int start, int size) {
		JsonObject data = new JsonObject();
		data.addProperty("trailInfoId", trailInfoId);
		data.addProperty("carId", carId);
		data.addProperty("startTime", startTime);
		data.addProperty("endTime", endTime);
		data.addProperty("start", start);
		data.addProperty("size", size);
		HttpConn.getInstance().sendMessage(data, 1224);
	}
	/** 删除轨迹 **/
	public void ccmd1224_deletePath(long trailInfoId,long carId,long startTime, long endTime) {
		JsonObject data = new JsonObject();
		data.addProperty("trailInfoId", trailInfoId);
		data.addProperty("carId", carId);
		data.addProperty("startTime", startTime);
		data.addProperty("endTime", endTime);
		HttpConn.getInstance().sendMessage(data, 1224);
	}
	/** 收藏列表 **/
	public void ccmd1218_favoriteList() {
		HttpConn.getInstance().sendMessage(null, 1218);
	}
	/** 车辆轨迹条件查询 **/
	public void ccmd1242_findPath(long carId,int start,int size, String conditions) {
		JsonObject data = new JsonObject();
		data.addProperty("carId", carId);
		data.addProperty("start", start);
		data.addProperty("size", size);
		data.addProperty("conditions", conditions);
		HttpConn.getInstance().sendMessage(data, 1242);
	}
	/** 轨迹备注添加 **/
	public void ccmd1243_addComment(long carId,long trailInfoId,String comment, long startTime,long endTime,int start,int  size) {
		JsonObject data = new JsonObject();
		data.addProperty("carId", carId);
		data.addProperty("trailInfoId", trailInfoId);
		data.addProperty("comment", comment);
		data.addProperty("startTime", startTime);
		data.addProperty("endTime", endTime);
		data.addProperty("start", start);
		data.addProperty("size", size);
		HttpConn.getInstance().sendMessage(data, 1243);
	}
	/** 轨迹收藏取消收藏 **/
	public void ccmd1244_GPSAreaCollect(long carId,long trailInfoId,int start, int size,int type) {
		JsonObject data = new JsonObject();
		data.addProperty("carId", carId);
		data.addProperty("trailInfoId", trailInfoId);
		data.addProperty("start", start);
		data.addProperty("size", size);
		data.addProperty("type", type);
		HttpConn.getInstance().sendMessage(data, 1244);
	}
	/** 轨迹收藏列表 **/
	public void ccmd1245_getGPSAreaList(long carId,int start,int size) {
		JsonObject data = new JsonObject();
		data.addProperty("carId", carId);
		data.addProperty("start", start);
		data.addProperty("size", size);
		HttpConn.getInstance().sendMessage(data, 1245);
	}
	/** 导航家设置 type 0：家，1：公司**/
	public void ccmd1246_setNavigation(String addressName,String latitude,int type) {
		JsonObject data = new JsonObject();
		data.addProperty("name", addressName);
		data.addProperty("latitude", latitude);
		data.addProperty("type", type);
		HttpConn.getInstance().sendMessage(data, 1246);
	}
	/** 获取导航首页信息 **/
	public void ccmd1247_getNavigationInfomation() {
		JsonObject data = new JsonObject();
		HttpConn.getInstance().sendMessage(data, 1247);
	}
	// ============================scmd==================================
	/** 取轨迹列表 **/
	private void backdata_1215_getPathList(int status,JsonObject obj, String CACHE_ERROR) {
		JsonArray paths = OJsonGet.getJsonArray(obj, "trails");
		if (paths == null)
			paths = new JsonArray();
		double miles = OJsonGet.getDouble(obj, "miles");
		// saveData
 		if (status == 1) {
			ManagerGps.getInstance().savePathList(miles, paths);
			ODispatcher.dispatchEvent(OEventName.GPS_PATHLIST_RESULTBACK,1215);
		}
	}
//	/** 设车半径 **/
//		public void backdata_1214_setArea(int status,JsonObject obj, String CACHE_ERROR) {
//			if (status == 1) {
//				ODispatcher.dispatchEvent(OEventName.GPS_SETAREA_RESULTBACK);
//			}
//		}
	/** 取车位置 **/
	public void backdata_1213_getCarPos(int status,JsonObject obj, String CACHE_ERROR) {
		// saveData
		if (status == 1) {
			ManagerGps.getInstance().areaMeter = OJsonGet.getInteger(obj, "radius")/1000;
			ManagerGps.getInstance().areaOpen = OJsonGet.getInteger(obj, "isOpen");
			String posarea = OJsonGet.getString(obj, "radiusLatlng");
			if(posarea == null || "".equals(posarea)){
				ManagerGps.getInstance().areaPos = new LatLng(0,0);
			}else {
//				LatLng toBaiduarea = NAVI.gps2baidu(NAVI.Str2Latlng(posarea));
				ManagerGps.getInstance().areaPos = AMapUtil.Str2Latlng(posarea);
			}

			String pos = OJsonGet.getString(obj, "latlng");
			int direction = OJsonGet.getInteger(obj, "direction");
			long startTime = OJsonGet.getLong(obj,"startTime");
			if(pos == null || "".equals(pos))return;
			DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
			carStatus.setGps(pos);
			carStatus.direction = direction;//角度值
			carStatus.startTime =startTime;//启动、停止时间
			ODispatcher.dispatchEvent(OEventName.GPS_CARPOS_RESULTBACK,true);
		}else{
			ODispatcher.dispatchEvent(OEventName.GPS_CARPOS_RESULTBACK,false);
		}
	}
	/** 收藏备注 **/
	public void backdata_1225_favoriteIntro(int status,JsonObject obj, String CACHE_ERROR) {
		if (status == 1) {
//			JsonArray collectInfos = OJsonGet.getJsonArray(obj, "collectInfos");
//			ManagerGps.getInstance().saveFavoriteList(collectInfos);
//			ODispatcher.dispatchEvent(OEventName.GPS_FAVORITE_INTRO_CHANGE_OK,1225);
		}
	}
	/** 添加收藏 **/
	public void backdata_1216_favoritePos(int status,JsonObject obj, String CACHE_ERROR) {
		if (status == 1) {
//			JsonArray collectInfos = OJsonGet.getJsonArray(obj, "collectInfos");
//			ManagerGps.getInstance().saveFavoriteList(collectInfos);
//			ODispatcher.dispatchEvent(OEventName.GLOBAL_POP_TOAST,"收藏成功");
		}
	}
	/** 删除收藏 **/
	public void backdata_1217_favoriteDelete(int status,JsonObject obj, String CACHE_ERROR) {
		if (status == 1) {
//			JsonArray collectInfos = OJsonGet.getJsonArray(obj, "collectInfos");
//			ManagerGps.getInstance().saveFavoriteList(collectInfos);
//			ODispatcher.dispatchEvent(OEventName.GPS_FAVORITE_LISTCHANGE,1217);
		}
	}
	/**删除轨迹列表 **/
	private void backdata_1224_deletePath(int status,JsonObject obj, String CACHE_ERROR) {
		JsonArray paths = OJsonGet.getJsonArray(obj, "trails");
		if (paths == null)
			paths = new JsonArray();
		double miles = OJsonGet.getDouble(obj, "miles");
		// saveData
		if (status == 1) {
			ManagerGps.getInstance().savePathList(miles, paths);
			ODispatcher.dispatchEvent(OEventName.GPS_PATHLIST_RESULTBACK,1224);
		}
	}
	/** 收藏列表 **/
	public void backdata_1218_favoriteList(int status,JsonObject obj, String CACHE_ERROR) {
		if (status == 1) {
//			JsonArray collectInfos = OJsonGet.getJsonArray(obj, "collectInfos");
//			ManagerGps.getInstance().saveFavoriteList(collectInfos);
//			ODispatcher.dispatchEvent(OEventName.GPS_FAVORITE_LISTCHANGE,1218);
		}
	}
	/** 轨迹条件查询列表 **/
	public void backdata_1242_findPath(int status,JsonObject obj, String CACHE_ERROR) {
		JsonArray paths = OJsonGet.getJsonArray(obj, "trails");
		if (paths == null)
			paths = new JsonArray();
		double miles = OJsonGet.getDouble(obj, "miles");
		// saveData
		if (status == 1) {
			ManagerGps.getInstance().savePathList(miles, paths);
			ODispatcher.dispatchEvent(OEventName.GPS_PATHLIST_RESULTBACK,42);
		}
	}
	/** 添加备注返回数据 **/
	public void backdata_1243_addComment(int status,JsonObject obj, String CACHE_ERROR) {
		JsonArray paths = OJsonGet.getJsonArray(obj, "trails");
		if (paths == null)
			paths = new JsonArray();
		double miles = OJsonGet.getDouble(obj, "miles");
		// saveData
		if (status == 1) {
			ManagerGps.getInstance().savePathList(miles, paths);
			ODispatcher.dispatchEvent(OEventName.GPS_PATHLIST_RESULTBACK,1243);
		}
	}
	/** 轨迹收藏取消收藏 **/
	public void backdata_1244_GPSAreaCollect(int status,JsonObject obj, String CACHE_ERROR) {
		JsonArray paths = OJsonGet.getJsonArray(obj, "collectionTrails");
		if (paths == null)
			paths = new JsonArray();
		double miles = OJsonGet.getDouble(obj, "miles");
		// saveData
		if (status == 1) {
//			ManagerGps.getInstance().savePathList(miles, paths);
			ODispatcher.dispatchEvent(OEventName.GPS_PATHLIST_RESULTBACK,1244);
		}
	}
	/** 轨迹收藏列表 **/
	public void backdata_1245_getGPSAreaList(int status,JsonObject obj, String CACHE_ERROR) {
		JsonArray paths = OJsonGet.getJsonArray(obj, "collectionTrails");
		if (paths == null)
			paths = new JsonArray();
		double miles = OJsonGet.getDouble(obj, "miles");
		// saveData
		if (status == 1) {
			ManagerGps.getInstance().savePathList(miles, paths);
			ODispatcher.dispatchEvent(OEventName.GPS_PATHLIST_RESULTBACK,1245);
		}
	}
	/** 导航设置 **/
	public void backdata_1246_setNavigation(int status,JsonObject obj, String CACHE_ERROR) {
		if(status == 1){
			//通知设置成功
//			JsonObject object = OJsonGet.getJsonObject(obj,"homeCompany");
//			ManagerNavi.getInstance().saveNaviInfo(DataNavigation.fromJsonObject(object));
//			ODispatcher.dispatchEvent(OEventName.GPS_NAVI_HOME_SET_SUCESS);
		}
	}
	/** 拿到导航设置的内容 **/
	public void backdata_1247_getNavigationInfomation(int status,JsonObject obj, String CACHE_ERROR) {
		if(status == 1){
			//通知数据返回成功
//			JsonObject object = OJsonGet.getJsonObject(obj,"homeCompany");
//			ManagerNavi.getInstance().saveNaviInfo(DataNavigation.fromJsonObject(object));
//			ODispatcher.dispatchEvent(OEventName.GPS_NAVI_INFO_BACK);
		}
	}

}
