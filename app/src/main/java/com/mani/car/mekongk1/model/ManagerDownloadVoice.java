package com.mani.car.mekongk1.model;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Process;
import android.util.Log;

import com.google.gson.JsonArray;
import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_assistant.UrlHelper;
import com.kulala.staticsfunc.static_system.MD5;
import com.kulala.staticsfunc.static_system.UtilFileLoading;
import com.kulala.staticsfunc.static_system.UtilFileSave;
import com.kulala.staticsfunc.static_system.ZipUtil;
import com.mani.car.mekongk1.BuildConfig;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.common.DataVoice;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ManagerDownloadVoice {
    private static final String TAG = ManagerDownloadVoice.class.getSimpleName();
    private List<DataVoice> voiceList;
    private DataVoice currentMp3;
    private static ManagerDownloadVoice _instance;
    private String useVOice;
    private ManagerDownloadVoice() {

    }

    public static ManagerDownloadVoice getInstance() {
        if (_instance == null)
            _instance = new ManagerDownloadVoice();
        return _instance;
    }

    public void saveDownLoadVoiceListInfo(JsonArray sounds) {
        if (sounds == null) return;
        voiceList = DataVoice.fromJsonArray(sounds);
    }

    public List<DataVoice> getDownLoadVoiceListInfo() {
        return voiceList;
    }

    //================================================================
    public DataVoice getCurrentMp3() {
        if (currentMp3 == null) {
            return new DataVoice();
        }
        return currentMp3;
    }
    public void setCurrentMp3(DataVoice mp3) {
        this.currentMp3=mp3;
    }

    //================================================================

    private String mp3NameCheck(String mp3Name) {
        return UrlHelper.getFileName(mp3Name);
    }

    public static String getMp3ZipFolder(Context context) {
        if (context == null) return null;
        String baseFolder = UtilFileSave.getBaseDir(context);
        return baseFolder.concat("/mp3pkg/");
    }

    public String getMp3Folder(Context context, DataVoice mp3PkgData) {
        if (context == null || mp3PkgData == null) return null;
        String baseFolder = UtilFileSave.getBaseDir(context);
        return baseFolder.concat("/mp3pkg/").concat(MD5.MD5generator(mp3PkgData.downUrl));
    }

    //================================================================
    public interface OnLoadMp3Listener {
        void loadCompleted(String fileFolder);//zipUSE dir+filename+skinname,sticker use dir+filename

        void loadFail(String errorInfo);
    }
    //================================================================

    /***
     * getMp3ZipFolder + fileName+pngName
     * 加载预播放音乐
     *
     */
    public void loadMp3ReListen(final Context context, final DataVoice mp3PkgData, final OnLoadMp3Listener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
//                if (BuildConfig.DEBUG)Log.e(TAG, "skinId:" + skinId + " skinType:" + skinType);
                //文件目录
                final String mp3DataFolder = getMp3Folder(context, mp3PkgData);
                final String checkedMp3Name = mp3NameCheck(mp3PkgData.profileUrl);
                //是否有本地
                if (UtilFileSave.isFileInDisk(mp3DataFolder, checkedMp3Name + ".mp3")) {//有mp3文件
                    if (listener != null)
                        listener.loadCompleted(mp3DataFolder + "/" + checkedMp3Name + ".mp3");
                } else {//加载网络
                    //默认声音
                    if (mp3PkgData.ide == 0) {
                        UtilFileSave.copyResFileToDisk(context, getMp3ZipFolder(context), "voice_push_default.zip", "voice_push_default.zip");
                        if (BuildConfig.DEBUG) Log.e(TAG, "默认声音:解压");
                        ZipUtil.unzip(getMp3ZipFolder(context) + "/" + "voice_push_default.zip", mp3DataFolder, new ZipUtil.OnUnzipFileListener() {
                            @Override
                            public void onUnzipOK() {
                                if (listener != null)
                                    listener.loadCompleted(mp3DataFolder + "/" + checkedMp3Name + ".mp3");
                            }

                            @Override
                            public void onUnzipFailed() {
                                if (BuildConfig.DEBUG) Log.e(TAG, "默认声音:解压  失败");
                            }
                        });
                        //下载声音
                    } else {
                        if (mp3PkgData == null || mp3PkgData.profileUrl == null) {
                            if (listener != null)
                                listener.loadFail("下载mp3无数据 id:" + mp3PkgData.ide);
                            return;
                        }
                        if (BuildConfig.DEBUG) Log.e("loadSkinById", "mp3试听:下载");
                        UtilFileLoading.getInstance().loadFileFromServer(mp3PkgData.profileUrl, mp3DataFolder, checkedMp3Name + ".mp3", new UtilFileLoading.SetLoadProgressListener() {
                            @Override
                            public void setMaxProgress(int max) {
                            }

                            @Override
                            public void setProgress(int progress) {
                            }

                            @Override
                            public void onCompleted(File savedFile) {
                                if (BuildConfig.DEBUG) Log.e("loadSkinById", "mp3试听:下载成功");
                                if (listener != null)
                                    listener.loadCompleted(mp3DataFolder + "/" + checkedMp3Name + ".mp3");
                            }

                            @Override
                            public void onLoadFailed(String errorInfo) {
                                if (BuildConfig.DEBUG) Log.e("loadSkinById", "mp3试听:下载失败");
                                if (listener != null) listener.loadFail("下载mp3试听失败");
                            }
                        });

                    }
                }
            }
        }).start();
    }

    /***
     * skinFolder + fileName+pngName
     * 非预播放
     */
    public void loadMp3(final Context context, final DataVoice mp3PkgData, final String mp3Name, final OnLoadMp3Listener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
//                if (BuildConfig.DEBUG)Log.e("loadSkinById", "skinId:" + skinId + " skinType:" + skinType);
                //文件目录
                final String mp3DataFolder = getMp3Folder(context, mp3PkgData);
                final String checkedMp3Name = mp3NameCheck(mp3Name);
                //是否有本地
                if (UtilFileSave.isFileInDisk(mp3DataFolder, checkedMp3Name + ".mp3")) {//有mp3文件
                    if (listener != null)
                        listener.loadCompleted(mp3DataFolder + "/" + checkedMp3Name + ".mp3");
                } else {//加载网络
                    //默认声音
                    if (mp3PkgData.ide == 0) {
                        UtilFileSave.copyResFileToDisk(context, getMp3ZipFolder(context), "voice_push_default.zip", "voice_push_default.zip");
                        if (BuildConfig.DEBUG) Log.e(TAG, "默认声音:解压");
                        ZipUtil.unzip(getMp3ZipFolder(context) + "/" + "voice_push_default.zip", mp3DataFolder, new ZipUtil.OnUnzipFileListener() {
                            @Override
                            public void onUnzipOK() {
                                if (listener != null)
                                    listener.loadCompleted(mp3DataFolder + "/" + checkedMp3Name + ".mp3");
                            }

                            @Override
                            public void onUnzipFailed() {
                                if (BuildConfig.DEBUG) Log.e(TAG, "默认声音:解压  失败");
                            }
                        });
                        //下载声音
                    } else {
                        if (mp3PkgData == null || mp3PkgData.downUrl == null) {
                            if (listener != null)
                                listener.loadFail("下载mp3无数据 id:" + mp3PkgData.ide);
                            return;
                        }
                        if (BuildConfig.DEBUG) Log.e(TAG, "mp3包:下载");
                        UtilFileLoading.getInstance().loadFileFromServer(mp3PkgData.downUrl, getMp3ZipFolder(context), MD5.MD5generator(mp3PkgData.downUrl) + ".zip", new UtilFileLoading.SetLoadProgressListener() {
                            @Override
                            public void setMaxProgress(int max) {
                            }

                            @Override
                            public void setProgress(int progress) {
                                if (BuildConfig.DEBUG) Log.e(TAG, "mp3包:下载:"+progress);
                            }

                            @Override
                            public void onCompleted(File savedFile) {
                                if (BuildConfig.DEBUG) Log.e(TAG, "mp3包:下载 成功");
                                ZipUtil.unzip(getMp3ZipFolder(context) + "/" + MD5.MD5generator(mp3PkgData.downUrl) + ".zip", mp3DataFolder, new ZipUtil.OnUnzipFileListener() {
                                    @Override
                                    public void onUnzipOK() {
                                        if (BuildConfig.DEBUG) Log.e(TAG, "mp3包:下载 解压 成功");
                                        if (listener != null)
                                            listener.loadCompleted(mp3DataFolder + "/" + checkedMp3Name + ".mp3");
                                    }

                                    @Override
                                    public void onUnzipFailed() {
                                        if (BuildConfig.DEBUG) Log.e(TAG, "mp3包:解压  失败");
                                    }
                                });
                            }

                            @Override
                            public void onLoadFailed(String errorInfo) {
                                if (BuildConfig.DEBUG) Log.e("loadSkinById", "mp3包:下载失败");
                                if (listener != null) listener.loadFail("下载mp3包失败");
                            }
                        });

                    }
                }
            }
        }).start();
    }
    public void saveUseVoiceId(String useVOice) {
        this.useVOice = useVOice;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("useVOice",String.valueOf(useVOice));
    }
    public String getUseVoiceId() {
        String useVOice=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("useVOice");
        return useVOice;
    }
    private String currentVoiceJson;
    public void saveCurrentVoice(String currentVoiceJson) {
        this.currentVoiceJson = currentVoiceJson;
        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("currentVoiceJson",currentVoiceJson);
    }
    public String getCurrentVoice() {
        String currentVoiceJson=ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("currentVoiceJson");
        return currentVoiceJson;
    }
    private MediaPlayer mediaPlayer;
    public void playMp3(String pathMp3){
        if(mediaPlayer==null){
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.release();
                    mediaPlayer = null;
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.seekTo(0);
                    mp.start();
                }
            });
            if (!mediaPlayer.isPlaying()) {
                try {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    //开启权限
                    mediaPlayer.setDataSource(pathMp3);
                    mediaPlayer.setVolume(1.0f, 1.0f);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
