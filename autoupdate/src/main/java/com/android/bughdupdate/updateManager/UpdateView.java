package com.android.bughdupdate.updateManager;

/**
 * Created by DaChao on 2017/12/6.
 */

public interface UpdateView {

    int STATUS_START = 1, STATUS_PAUSE = 2, STATUS_CANCEL = 3, STATUS_ERROR = 4;

    interface OnStatusChangedListener {
        void onChanged(int newStatus);
    }

    /**
     * 设置版本号描述
     */
    void setNewVersionStr(String versionStr);

    /**
     * 设置更新时间
     */
    void setUpdateTimeStr(String updateTimeStr);

    /**
     * 设置更新内容
     */
    void setUpdateContent(String updateContent);

    /**
     * 需要开始、暂停、取消任务的监听器，需要在状态变更的地方调用{@link OnStatusChangedListener#onChanged(int)}
     */
    void setOnStatusChangedListener(OnStatusChangedListener listener);

    void pauseUpdate();

    /**
     * 设置更新进度
     */
    void setUpdateProgress(int progress);

    /**
     * 隐藏到后台，一般是在通知栏上显示
     */
    void showInBackground();

    /**
     * 前台显示，一般是显示一个对话框
     */
    void showInForeground();

    /**
     * 显示更新视图
     */
    void showView();

    /**
     * 关闭更新视图
     */
    void cancelView();

    void showFail();

}
