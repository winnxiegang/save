package com.android.bughdupdate.updateManager;

/**
 * Created by DaChao on 2017/12/6.
 */

public interface UpdateDataSource {

    int STATUS_START = 1, STATUS_PAUSE = 2, STATUS_CANCEL = 3,
            STATUS_FINISH = 4, STATUS_ERROR = 5, STATUS_LOADING = 6;

    interface OnErrorListener {
        void error();
    }

    /**
     * 获取更新版本号
     */
    String getVersionStr();

    /**
     * 获取更新时间描述
     */
    String getUpdateTimeStr();

    /**
     * 获取更新内容
     */
    String getUpdateContent();


    int getProgress();

    int getStatus();

    /**
     * 开始更新
     */
    void startUpdate();

    /**
     * 暂停更新
     */
    void pauseUpdate();

    /**
     * 取消更新
     */
    void cancelUpdate();

    void downloadFinish();

    void setOnErrorListener(OnErrorListener onErrorListener);

}
