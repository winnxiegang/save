package git.dzc.downloadmanagerlib.download.service;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by lingjiu on 2017/4/11.
 */

public class DownloadHelper {

    private String saveDirPath;

    public DownloadHelper(Context context) {

        saveDirPath = getDiskCacheRootDir(context);
    }

    /**
     * 获取app的根目录
     *
     * @return 文件缓存根路径
     */
    public String getDiskCacheRootDir(Context context) {
        File diskRootFile;
        if (existsSdcard()) {
            diskRootFile = context.getExternalCacheDir();
        } else {
            diskRootFile = context.getCacheDir();
        }
        String cachePath;
        if (diskRootFile != null) {
            cachePath = diskRootFile.getPath();
        } else {
            throw new IllegalArgumentException("disk is invalid");
        }
        return cachePath;
    }

    /**
     * 判断外置sdcard是否可以正常使用
     *
     * @return
     */
    public static Boolean existsSdcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * @param fileName
     * @return 返回文件路径，如果为null则说明不存在
     */
    public String getFile(String fileName) {
        File file = new File(saveDirPath + "/" + fileName);
        if (file.exists()) {
            return file.getPath();
        }
        return null;
    }

    /**
     * @return 返回文件路径，如果为null则说明不存在
     */
    public String getFileByGiftName(String giftConfigId, int resourceVersion) {
        String fileName = giftConfigId + "_V" + resourceVersion;
        String filePath = getFile(fileName);
        //如果没有第二版,则检查第一版是否可用
        if (filePath == null) {
            fileName = giftConfigId + "_V" + 1;
            return getFile(fileName);
        }
        return filePath;
    }

}
