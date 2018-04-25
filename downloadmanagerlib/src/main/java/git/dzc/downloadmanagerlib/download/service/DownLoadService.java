package git.dzc.downloadmanagerlib.download.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import git.dzc.downloadmanagerlib.download.DownloadManager;
import git.dzc.downloadmanagerlib.download.DownloadStatus;
import git.dzc.downloadmanagerlib.download.DownloadTask;
import git.dzc.downloadmanagerlib.download.DownloadTaskListener;

/**
 * 下载器后台服务
 */

public class DownLoadService extends Service {

    public static final String TAG = "DownLoadService";

    private DownloadManager downloadManager;

    private ArrayList<String> urlList;

    private int downloadCompleteCount;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        downloadManager = DownloadManager.getInstance(getApplicationContext());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (downloadManager == null) {

            downloadManager = DownloadManager.getInstance(getApplicationContext());
        }

        getData(intent);
        //判断是否需要下载
        checkIsNeedDownload();

        return START_NOT_STICKY;
    }

    private void checkIsNeedDownload() {
        //http://livemapi.huohongshe.net/giftEffect.resource?giftConfigId=9&resourceVersion=1.0
        if (urlList != null && urlList.size() > 0) {
            for (String url : urlList) {

                float version = Float.valueOf(getVersion(url));
                if (version == 1.0f) {
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

        String resourceVersion = url.substring(index + "resourceVersion=".length() + 1, url.length());

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

    public static Boolean existsSdcard() {
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
                Log.d(TAG, "onPrepare");
            }

            @Override
            public void onStart(final DownloadTask downloadTask) {
                Log.d(TAG, "onStart");

            }

            @Override
            public void onDownloading(DownloadTask downloadTask) {
                Log.d(TAG, "onDownloading");
            }

            @Override
            public void onPause(DownloadTask downloadTask) {
                Log.d(TAG, "onPause");

            }

            @Override
            public void onCancel(DownloadTask downloadTask) {
                Log.d(TAG, "onCancel");

            }

            @Override
            public void onCompleted(DownloadTask downloadTask) {
                Log.d(TAG, "onCompleted");
                //解压,并校验文件完整性
                try {
                    ZipUtils.UnZipFolder(new File(downloadTask.getSaveDirPath() + downloadTask.getFileName()).getPath(), downloadTask.getSaveDirPath());
                    downloadCompleteCount++;
                    if (urlList.size() == downloadCompleteCount) {
                        stopSelf();
                        Log.i(TAG, "下载完成,停止服务");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAlreadyDownload(DownloadTask downloadTask) {
                downloadCompleteCount++;
                if (urlList.size() == downloadCompleteCount) {
                    stopSelf();
                    Log.i(TAG, "下载完成,停止服务");
                }
            }

            @Override
            public void onError(DownloadTask downloadTask, int errorCode) {
                Log.d(TAG, "onError");
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


}
