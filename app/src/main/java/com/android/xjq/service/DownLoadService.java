package com.android.xjq.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.bean.giftDownload.GiftDownloadBean;
import com.android.xjq.bean.giftDownload.GiftDownloadEntity;
import com.android.xjq.utils.XjqUrlEnum;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import git.dzc.downloadmanagerlib.download.DownloadManager;
import git.dzc.downloadmanagerlib.download.DownloadStatus;
import git.dzc.downloadmanagerlib.download.DownloadTask;
import git.dzc.downloadmanagerlib.download.DownloadTaskListener;
import git.dzc.downloadmanagerlib.download.service.ZipUtils;

import static com.android.xjq.XjqApplication.GIFT_VERSION;

/**
 * 下载器后台服务
 */

public class DownLoadService extends Service implements OnHttpResponseListener {

    public static final String TAG = "DownLoadService";

    private DownloadManager downloadManager;

    private ArrayList<String> urlList;

    private int downloadCompleteCount;

    private HttpRequestHelper httpRequestHelper;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        downloadManager = DownloadManager.getInstance(getApplicationContext());
        requestData();
        LogUtils.e(TAG, "onCreate");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
    }

    private void requestData() {

        httpRequestHelper = new HttpRequestHelper(this, this);

        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.GIFT_EFFECT_RESOURCE_QUERY, false);

        map.put("clientType", "ANDROID");

        httpRequestHelper.startRequest(map, false);

    }

    private void checkIsNeedDownload() {
        //http://livemapi.huohongshe.net/giftEffect.resource?giftConfigId=9&resourceVersion=1.0
        if (urlList != null && urlList.size() > 0) {
            for (String url : urlList) {

                float version = Float.valueOf(getVersion(url));
                if (version <= GIFT_VERSION) {
                    //不需要下载
                    return;
                }
                //没有下载过，直接下载
                download(url);
            }
        }
    }

    private String getGiftId(String url) {

        int index = url.indexOf("giftConfigId=");

        String giftConfigId = url.substring(index + "giftConfigId=".length() + 1, url.indexOf("&"));

        return giftConfigId;

    }

    private String getVersion(String url) {

        int index = url.indexOf("resourceVersion=");

        String resourceVersion = url.substring(index + "resourceVersion=".length(), url.length());

        return resourceVersion;

    }

    public String getDiskCacheRootDir() {
        File diskRootFile;
        if (existsSdcard()) {
            diskRootFile = getExternalCacheDir();
        } else {
            diskRootFile = getCacheDir();
        }
        String cachePath;
        if (diskRootFile != null) {
            cachePath = diskRootFile.getPath();
        } else {
            throw new IllegalArgumentException("disk is invalid");
        }
        return cachePath;
    }

    public Boolean existsSdcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private void getData(Intent intent) {
        if (intent == null) {
            stopSelf();
            return;
        }
        ArrayList<String> urlList = intent.getStringArrayListExtra("url");

        this.urlList = urlList;
    }

    private void download(final String url) {

        DownloadTask task = new DownloadTask();

//        task.setFileName(bean.getFileName());
        //存放文件的唯一标识
        task.setId(url);

        task.setSaveDirPath(getDiskCacheRootDir() + "/");

        task.setUrl(url);

        downloadManager.addDownloadTask(task, new DownloadTaskListener() {
            @Override
            public void onPrepare(final DownloadTask downloadTask) {
                LogUtils.d(TAG, "onPrepare");
            }

            @Override
            public void onStart(final DownloadTask downloadTask) {
                Log.d(TAG, "onStart");

            }

            @Override
            public void onDownloading(DownloadTask downloadTask) {
                LogUtils.d(TAG, "onDownloading");
            }

            @Override
            public void onPause(DownloadTask downloadTask) {
                LogUtils.d(TAG, "onPause");

            }

            @Override
            public void onCancel(DownloadTask downloadTask) {
                LogUtils.d(TAG, "onCancel");

            }

            @Override
            public void onCompleted(DownloadTask downloadTask) {
                LogUtils.d(TAG, "onCompleted");
                //解压,并校验文件完整性
                try {
                    ZipUtils.UnZipFolder(new File(downloadTask.getSaveDirPath() + downloadTask.getFileName()).getPath(), downloadTask.getSaveDirPath());
                    downloadCompleteCount++;
                    if (urlList.size() == downloadCompleteCount) {
                        stopSelf();
                        LogUtils.i(TAG, "下载完成,停止服务");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAlreadyDownload(DownloadTask downloadTask) {
                LogUtils.d(TAG, "onAlreadyDownload");
                downloadCompleteCount++;
                if (urlList.size() == downloadCompleteCount) {
                    stopSelf();
                    LogUtils.i(TAG, "下载完成,停止服务");
                }
            }

            @Override
            public void onError(DownloadTask downloadTask, int errorCode) {
                LogUtils.d(TAG, "onError");
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放downLoadManager
        List<DownloadTask> downloadTasks = downloadManager.loadAllTask();
        if (downloadTasks != null && downloadTasks.size() > 0) {
            for (DownloadTask downloadTask : downloadTasks) {
                if (DownloadStatus.DOWNLOAD_STATUS_COMPLETED != downloadTask.getDownloadStatus()) {

                    downloadManager.pause(downloadTask);
                }
            }
        }
        downloadManager = null;
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            GiftDownloadEntity giftDownloadEntity = new Gson().fromJson(jsonObject.toString(), GiftDownloadEntity.class);
            HashMap<String, GiftDownloadBean> giftConfigCodeMap = giftDownloadEntity.getGiftConfigCodeAndResourceUrlMap();
            HashMap<String, GiftDownloadBean> modelGiftConfigCodeMap = giftDownloadEntity.getModelResourceTypeAndResourceUrlMap();
            if (giftConfigCodeMap != null) {
                AssetManager assetManager = getAssets();
                String[] list = assetManager.list("GIFT");
                List<String> giftList = Arrays.asList(list);
                findNewGiftDownload(giftConfigCodeMap, giftList, false);
                findNewGiftDownload(modelGiftConfigCodeMap, giftList, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void findNewGiftDownload(HashMap<String, GiftDownloadBean> giftConfigCodeMap, List<String> giftList, boolean isModel) {
        Set<String> keys = giftConfigCodeMap.keySet();
        for (String key : keys) {
            GiftDownloadBean giftDownloadBean = giftConfigCodeMap.get(key);
            String giftName = key + (isModel ? "" : "_V" + (int) giftDownloadBean.getResourceVersion());
            if (!giftList.contains(giftName)) {
                //新礼物,直接下载
                download(giftDownloadBean.getGiftEffectResourceUrl());
            }
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {

    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }


}
