package com.mani.car.mekongk1.ctrl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.common.PHeadHttp;
import com.mani.car.mekongk1.model.ManagerCommon;
import com.kulala.links.http.HttpConn;
import com.kulala.links.interfaces.HttpInitDataListener;
import com.kulala.links.tools.OJsonGet1;
public class OCtrlBaseHttp {
	// ========================out======================
	private static OCtrlBaseHttp _instance;
	public static OCtrlBaseHttp getInstance() {
		if (_instance == null)
			_instance = new OCtrlBaseHttp();
		return _instance;
	}

//	// ========================out======================
	public void init() {
        HttpConn.getInstance().setHttpInitDataListener(new HttpInitDataListener() {
            @Override
            public JsonObject getPHeadJsonObj(String subscription) {
                return PHeadHttp.getInstance().getPHead(subscription);
            }
            @Override
            public void onReceiveData(int cmd, String receiveStr) {
                try {
                    Gson       gson   = new Gson();
                    JsonObject data   = gson.fromJson(receiveStr, JsonObject.class);
                    JsonObject result = data.get("result").getAsJsonObject();
                    int        status = result.get("status").getAsInt();// status=1:处理成功；status=-1:服务器处理出错；status=-2:业务处理异常；
                    int errorcode = OJsonGet1.getInteger(result, "errorcode");
                    final String msg = OJsonGet1.getString(result, "msg");
                    if (status == 1) {//正常的业务流程
                        processResult(cmd,status, data,"");//发包成功的
                    } else {
                        String CACHE_ERROR = handlerResult(errorcode,msg, cmd);
                        processResult(cmd,status, data,CACHE_ERROR);
						if(CACHE_ERROR!=null && CACHE_ERROR.length()>1 && cmd!=1219){
							GlobalContext.popMessage(CACHE_ERROR,GlobalContext.getContext().getResources().getColor(R.color.popTipWarning));
						}
                    }
                } catch (JsonSyntaxException e) {
//					Log.e("<<<Http>>>", ">>>JsonSyntaxException<<<:" + e.toString() + "\n" + receiveStr);
                }
            }
            @Override
            public String getBasicUrl() {
                return PHeadHttp.getBasicUrl();
            }
        });
	}
	protected void processResult(int protocol,int status, JsonObject obj, String CACHE_ERROR) {
		OCtrlCommon.getInstance().processResult(protocol,status,obj,CACHE_ERROR);
		OCtrlRegLogin.getInstance().processResult(protocol,status,obj,CACHE_ERROR);
		OCtrlGesture.getInstance().processResult(protocol,status,obj,CACHE_ERROR);
		OCtrlCar.getInstance().processResult(protocol,status,obj,CACHE_ERROR);
		OCtrlAuthorization.getInstance().processResult(protocol,status,obj,CACHE_ERROR);
		OCtrlGps.getInstance().processResult(protocol,status,obj,CACHE_ERROR);
	}
//	// ============================error==================================
	/**
	 * -1:运行时异常0:无异常(status=1的时候)1:数据库操作异常2:网络处理异常3:业务逻辑异常4:java代码异常5:通用错误：
	 * 客户端弹toast将msg显示出来6:账号过期，需要重新登录,客户端直接退出
	 **/
	protected String handlerResult(int errorcode, String msg,int protoco1) {
		if(protoco1 == 1219 && errorcode!=6){
			return msg;//1219刷新车辆不提示,退出用户才执行下面
		}
		String CACHE_ERROR = "";
		switch (errorcode) {
			case -1 :
				CACHE_ERROR = "运行时异常";
				break;
			case 1 :
				CACHE_ERROR = "数据库操作异常";
				break;
			case 2 :
				CACHE_ERROR = "网络处理异常";
				break;
			case 3 :
				CACHE_ERROR = "业务逻辑异常";
				break;
			case 4 :
				CACHE_ERROR = "java代码异常";
				break;
			case 5 :
				CACHE_ERROR = msg;
				break;
			case 6 :
				ManagerCommon.getInstance().exitToLogin(msg);
				break;
//			case 7:
//				CACHE_ERROR = msg;
//				ODispatcher.dispatchEvent(OEventName.CHECK_VERFICODE_FAILED_THREE);
//				break;
//			case 8:
//				CACHE_ERROR = msg;
//				ODispatcher.dispatchEvent(OEventName.CHECK_VERFICODE_FAILED_FIVE);
//				break;
		}
//		Log.e("HttpBase", "CACHE_ERROR:"+CACHE_ERROR);
//		if(CACHE_ERROR.equals(GlobalContext.getContext().getString(R.string.business_logic_is_abnormal))){
//			if (GlobalContext.IS_DEBUG_MODEL) {
////				new ToastTxt(GlobalContext.getCurrentActivity(),null).withInfo(CACHE_ERROR).show();
//			} else {
//				//非debug不提示
//			}
//		}
//		if (!CACHE_ERROR.equals("")) {
//			if(errorcode==7||errorcode==8){
//				return "";
//			}
//			if(protoco1 != 1408 && protoco1 != 1409 && protoco1!=1226&&errorcode!=7&&errorcode!=8) {
////				Toast.makeText(GlobalContext.getCurrentActivity(),CACHE_ERROR,Toast.LENGTH_SHORT).show();
//			}
//		}
		return CACHE_ERROR;
	}
}
