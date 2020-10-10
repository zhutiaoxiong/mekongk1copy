package com.mani.car.mekongk1.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Process;
import android.util.Log;

import com.google.gson.JsonArray;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_assistant.ImageConverHelper;
import com.kulala.staticsfunc.static_system.MD5;
import com.kulala.staticsfunc.static_system.UtilFileLoading;
import com.kulala.staticsfunc.static_system.UtilFileSave;
import com.mani.car.mekongk1.BuildConfig;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.carlist.DataCarSkin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ManagerSkins {
    private List<DataCarSkin> skinListCAR;
    private List<DataCarSkin> skinListSUV;
    private List<DataCarSkin> stickerListCAR;
    private List<DataCarSkin> stickerListSUV;

    private HashMap<String, Drawable> cacheZipPng;
    public static final String TRANSPARENT = "TRANSPARENT";
    // ========================out======================
    private static ManagerSkins _instance;

    private ManagerSkins() {
    }

    public static ManagerSkins getInstance() {
        if (_instance == null)
            _instance = new ManagerSkins();
        return _instance;
    }
    // =================================================

    /**
     * @param carType 1：轿车，2：SUV
     */
    public DataCarSkin getSkinInfo(int skinId, int carType) {
        List<DataCarSkin> arr = (carType == 2) ? getSkinListSUV() : getSkinListCAR();
        if (BuildConfig.DEBUG) Log.e("loadSkinById", "getSkinInfo:" + arr);
        if (arr == null) return null;
        for (DataCarSkin skin : arr) {
            if (skin.ide == skinId) return skin;
        }
        return null;
    }

    /**
     * @param carType 1：轿车，2：SUV
     */
    public DataCarSkin getStickerInfo(int stickerId, int carType) {
        List<DataCarSkin> arr = (carType == 2) ? getStickerListSUV() : getStickerListCAR();
        if (arr == null) return null;
        for (DataCarSkin skin : arr) {
            if (skin.ide == stickerId) return skin;
        }
        return null;
    }

    public List<DataCarSkin> getSkinListCAR() {
        if (skinListCAR == null) {
            String json = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("skinListCAR");
            if (json != null && json.length() > 0) {
                JsonArray jsonList = ODBHelper.convertJsonArray(json);
                skinListCAR = DataBase.fromJsonArray(jsonList, DataCarSkin.class);
            }
        }
        return skinListCAR;
    }

    public List<DataCarSkin> getSkinListSUV() {
        if (skinListSUV == null) {
            String json = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("skinListSUV");
            if (json != null && json.length() > 0) {
                JsonArray jsonList = ODBHelper.convertJsonArray(json);
                skinListSUV = DataBase.fromJsonArray(jsonList, DataCarSkin.class);
            }
        }
        return skinListSUV;
    }

    public List<DataCarSkin> getStickerListCAR() {
        if (stickerListCAR == null) {
            String json = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("stickerListCAR");
            if (json != null && json.length() > 0) {
                JsonArray jsonList = ODBHelper.convertJsonArray(json);
                stickerListCAR = DataBase.fromJsonArray(jsonList, DataCarSkin.class);
            }
        }
        return stickerListCAR;
    }

    public List<DataCarSkin> getStickerListSUV() {
        if (stickerListSUV == null) {
            String json = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("stickerListSUV");
            if (json != null && json.length() > 0) {
                JsonArray jsonList = ODBHelper.convertJsonArray(json);
                stickerListSUV = DataBase.fromJsonArray(jsonList, DataCarSkin.class);
            }
        }
        return stickerListSUV;
    }
    // =================================================

    /**
     * 保存车皮肤表
     *
     * @param skinType 1：轿车，2：SUV
     */
    public void saveSkinListByType(int skinType, JsonArray jsonList) {
        if (skinType == 2) {
            skinListSUV = DataBase.fromJsonArray(jsonList, DataCarSkin.class);
            ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("skinListSUV", ODBHelper.convertString(jsonList));
        } else {
            skinListCAR = DataBase.fromJsonArray(jsonList, DataCarSkin.class);
            ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("skinListCAR", ODBHelper.convertString(jsonList));
        }
    }

    /**
     * 保存车贴表
     *
     * @param skinType 1：轿车，2：SUV
     */
    public void saveStickerListByType(int skinType, JsonArray jsonList) {
        if (skinType == 2) {
            stickerListSUV = DataBase.fromJsonArray(jsonList, DataCarSkin.class);
            ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("stickerListSUV", ODBHelper.convertString(jsonList));
        } else {
            stickerListCAR = DataBase.fromJsonArray(jsonList, DataCarSkin.class);
            ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("stickerListCAR", ODBHelper.convertString(jsonList));
        }
    }
    // =======================falsh==========================

    /**
     * @param skinId  0,1,2,3
     * @param carType 1：轿车，2：SUV
     * @return null 取皮肤数据错
     */
    public String getSkinZipFileName(int skinId, int carType) {
        if (skinId == 0) {//是默认皮肤就去复制并解压
            if (carType == 2) return MD5.MD5generator("skin_default_suv");
            else return MD5.MD5generator("skin_default_car");
        } else {
            DataCarSkin info = getSkinInfo(skinId, carType);
            if (info != null && info.url != null) return MD5.MD5generator(info.url);
        }
        return null;
    }

    /**
     * @param skinId  0,1,2,3
     * @param carType 1：轿车，2：SUV
     * @return null 取皮肤数据错
     */
    public String getStickerFileName(int skinId, int carType) {
        DataCarSkin info = getStickerInfo(skinId, carType);
        if (info != null && info.url != null) return MD5.MD5generator(info.url);
        return null;
    }

    public String getSkinZipFolder(Context context) {
        return UtilFileSave.getSkinBaseDir(context);
    }

    public String getStickerFolder(Context context) {
        return UtilFileSave.getSkinBaseDir(context);
    }

    // =======================oub==========================

    /**
     * key : 全路径,no pngName
     */
    public Drawable getPngImage(String key) {
        if(key == null)return null;
        if(key.length() == 0 || key.equals(TRANSPARENT)){
            Drawable trans = cacheZipPng.get("");
            if(trans == null)putPngImage("", new PaintDrawable(Color.TRANSPARENT));
            return cacheZipPng.get("");
        }
        if (cacheZipPng == null || cacheZipPng.get(key) == null) return null;//"未保存过的皮肤");
        return cacheZipPng.get(key);
    }

    private void putPngImage(String key, Drawable image) {
        if (cacheZipPng == null) cacheZipPng = new HashMap<>();
        cacheZipPng.put(key, image);
    }

    // =======================oub==========================
    public interface OnLoadPngListener {
        void loadCompleted(Drawable image);//zipUSE dir+filename+skinname,sticker use dir+filename

        void loadFail(String errorInfo);
    }

    /***
     * skinFolder + fileName+pngName
     * 数据会真实重读
     */
    public void loadSkinByIdReal(final Context context, final int skinId, final int skinType, final String pngName, final OnLoadPngListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                if (BuildConfig.DEBUG)
                    Log.e("loadSkinById", "skinId:" + skinId + " skinType:" + skinType);
                //文件目录
                final String skinFolder = getSkinZipFolder(context);
                final String fileName = getSkinZipFileName(skinId, skinType);
                final String skinZipDir = skinFolder + "/" + fileName + ".zip";
                //是否有缓存
                Drawable cacheImage = getPngImage(skinZipDir + "/" + pngName);
                if (cacheImage != null) {
                    if (listener != null) listener.loadCompleted(cacheImage);
                    return;
                }
                //加载本地
                if (UtilFileSave.isFileInDisk(skinFolder, fileName + ".zip")) {//有zip文件
                    if (BuildConfig.DEBUG) Log.e("loadSkinById", "硬盘读取");
                    readZipFile(skinZipDir);
                    Drawable localImage = getPngImage(skinZipDir + "/" + pngName);
                    if (localImage != null) {
                        if (listener != null) listener.loadCompleted(localImage);
                    } else {
                        if (listener != null) listener.loadFail("硬盘读取 localImage read Error");
                    }
                } else {//加载网络
                    //默认皮肤
                    if (skinId == 0) {
                        if (skinType == 2)
                            UtilFileSave.copyResFileToDisk(context, skinFolder, "skin_default_suv.zip", fileName + ".zip");
                        else
                            UtilFileSave.copyResFileToDisk(context, skinFolder, "skin_default_car.zip", fileName + ".zip");
                        readZipFile(skinZipDir);
                        if (BuildConfig.DEBUG) Log.e("loadSkinById", "默认皮肤:解压");
                        //查看数据
                        Drawable localImage = getPngImage(skinZipDir + "/" + pngName);
                        if (localImage != null) {
                            if (listener != null) listener.loadCompleted(localImage);
                        } else {
                            if (listener != null)
                                listener.loadFail("默认皮肤 硬盘读取 localImage read Error");
                        }
                        //下载皮肤
                    } else {
                        DataCarSkin skin = getSkinInfo(skinId, skinType);
                        if (skin == null || skin.url == null) {
                            if (listener != null) listener.loadFail("下载皮肤无数据");
                            return;
                        }
                        if (BuildConfig.DEBUG) Log.e("loadSkinById", "非默认皮肤:下载");
                        UtilFileLoading.getInstance().loadFileFromServer(skin.url, skinFolder, fileName + ".zip", new UtilFileLoading.SetLoadProgressListener() {
                            @Override
                            public void setMaxProgress(int max) {
                            }

                            @Override
                            public void setProgress(int progress) {
                            }

                            @Override
                            public void onCompleted(File savedFile) {
                                if (BuildConfig.DEBUG) Log.e("loadSkinById", "没有zip:下载成功");
                                readZipFile(skinZipDir);
                                //查看数据
                                Drawable localImage = getPngImage(skinZipDir + "/" + pngName);
                                if (localImage != null) {
                                    if (listener != null) listener.loadCompleted(localImage);
                                } else {
                                    if (listener != null)
                                        listener.loadFail("下载皮肤 硬盘读取 localImage read Error");
                                }
                            }

                            @Override
                            public void onLoadFailed(String errorInfo) {
                                if (BuildConfig.DEBUG) Log.e("loadSkinById", "没有zip:下载失败");
                                if (listener != null) listener.loadFail("下载皮肤失败");
                            }
                        });

                    }
                }
            }
        }).start();
    }

    private String preNameForTestError = "";
    private int countNumNameForTestError = 0;

    public void readZipFile(String fileDir) {
        try {
//            if (BuildConfig.DEBUG) Log.e("readZip", "start:" + fileDir);
            ZipFile zf = new ZipFile(fileDir);
            InputStream in = new BufferedInputStream(new FileInputStream(fileDir));
            ZipInputStream zin = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                } else {
//                    if (BuildConfig.DEBUG) Log.e("readZip", ze.getName());
                    String[] nameArr = ze.getName().split("\\.");
                    if (nameArr != null && nameArr.length == 2 && nameArr[1].equals("png")) {//是png图片
                        if (nameArr[0].equals(preNameForTestError)) {
                            countNumNameForTestError++;
                            if (countNumNameForTestError >= 3)
                                UtilFileSave.RecursionDeleteFile(new File(fileDir));
                        } else {
                            countNumNameForTestError = 0;
                        }
                        preNameForTestError = nameArr[0];
                        putPngImage(fileDir + "/" + nameArr[0], new BitmapDrawable(BitmapFactory.decodeStream(zf.getInputStream(ze))));
                    }
                }
            }
            zin.closeEntry();
        } catch (IOException e) {
            Log.e("readZipFile", "ERROR:" + e.getMessage());
            UtilFileSave.RecursionDeleteFile(new File(fileDir));
        }
    }

    /**
     * @param stickerId 0,1,2,3
     * @param carType   1：轿车，2：SUV
     * @return sticker+id+".oipc"
     * stickerFolder + fileName
     * stickerFolder + "/" + fileName + UtilFileSave.PNG_CHANGE_NAME
     */
    public void loadStickerById(final Context context, final int stickerId, final int carType, final OnLoadPngListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                final String stickerFolder = getStickerFolder(context);
                final String fileName = getStickerFileName(stickerId, carType);
                if (stickerId == 0 || fileName == null) {//默认0是没有车贴,无文件null也是没设车贴
                    Drawable noImage = getPngImage("");
                    if (noImage == null) putPngImage("", new PaintDrawable(Color.TRANSPARENT));
                    if (listener != null) listener.loadCompleted(getPngImage(""));
                    return;
                }
                //是否有本地文件
                if (UtilFileSave.isFileInDisk(stickerFolder, fileName + UtilFileSave.PNG_CHANGE_NAME)) {//有png文件
                    Drawable localImage = getPngImage(stickerFolder + "/" + fileName);
                    if (localImage != null) {
                        if (listener != null) listener.loadCompleted(localImage);
                    } else {
                        localImage = ImageConverHelper.getImageDrawableFromFile(stickerFolder + "/" + fileName + UtilFileSave.PNG_CHANGE_NAME);
                        if (localImage != null) {
                            putPngImage(stickerFolder + "/" + fileName, localImage);
                            if (listener != null) listener.loadCompleted(localImage);
                        } else {
                            if (listener != null) listener.loadFail("localImage read Error");
                        }
                    }
                } else {//需要去下载的
                    DataCarSkin skin = getStickerInfo(stickerId, carType);
                    UtilFileLoading.getInstance().loadFileFromServer(skin.url, stickerFolder, fileName + UtilFileSave.PNG_CHANGE_NAME, new UtilFileLoading.SetLoadProgressListener() {
                        @Override
                        public void setMaxProgress(int max) {
                        }

                        @Override
                        public void setProgress(int progress) {
                        }

                        @Override
                        public void onCompleted(File savedFile) {
                            Drawable localImage = ImageConverHelper.getImageDrawableFromFile(stickerFolder + "/" + fileName + UtilFileSave.PNG_CHANGE_NAME);
                            if (localImage != null) {
                                putPngImage(stickerFolder + "/" + fileName, localImage);
                                if (listener != null) listener.loadCompleted(localImage);
                            } else {
                                if (listener != null)
                                    listener.loadFail("download Image read Error");
                            }
                        }

                        @Override
                        public void onLoadFailed(String errorInfo) {
                            if (listener != null) listener.loadFail("download fail");
                        }
                    });

                }
            }
        }).start();
    }

    /**
     * stickerFolder + "/" + fileName + UtilFileSave.PNG_CHANGE_NAME
     */
    public void loadPngFromUrl(final Context context, final String url, final OnLoadPngListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                final String stickerFolder = getStickerFolder(context);
                final String fileName = MD5.MD5generator(url);
                //是否有焕春
                Drawable cacheImage = getPngImage(url);
                if (cacheImage != null) {
                    if (listener != null) listener.loadCompleted(cacheImage);
                    return;
                }
                //是否有本地文件
                if (UtilFileSave.isFileInDisk(stickerFolder, fileName + UtilFileSave.PNG_CHANGE_NAME)) {//有png文件
                    Drawable localImage = getPngImage(url);
                    if (localImage != null) {
                        if (listener != null) listener.loadCompleted(localImage);
                    } else {
                        localImage = ImageConverHelper.getImageDrawableFromFile(stickerFolder + "/" + fileName + UtilFileSave.PNG_CHANGE_NAME);
                        if (localImage != null) {
                            putPngImage(url, localImage);
                            if (listener != null) listener.loadCompleted(localImage);
                        } else {
                            if (listener != null) listener.loadFail("localImage read Error");
                        }
                    }
                } else {//需要去下载的
                    UtilFileLoading.getInstance().loadFileFromServer(url, stickerFolder, fileName + UtilFileSave.PNG_CHANGE_NAME, new UtilFileLoading.SetLoadProgressListener() {
                        @Override
                        public void setMaxProgress(int max) {
                        }

                        @Override
                        public void setProgress(int progress) {
                        }

                        @Override
                        public void onCompleted(File savedFile) {
                            Drawable localImage = ImageConverHelper.getImageDrawableFromFile(stickerFolder + "/" + fileName + UtilFileSave.PNG_CHANGE_NAME);
                            if (localImage != null) {
                                putPngImage(url, localImage);
                                if (listener != null) listener.loadCompleted(localImage);
                            } else {
                                if (listener != null)
                                    listener.loadFail("download Image read Error");
                            }
                        }

                        @Override
                        public void onLoadFailed(String errorInfo) {
                            if (listener != null) listener.loadFail("download fail");
                        }
                    });

                }
            }
        }).start();
    }
}
