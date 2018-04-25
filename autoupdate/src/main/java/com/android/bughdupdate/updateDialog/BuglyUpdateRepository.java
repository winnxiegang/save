package com.android.bughdupdate.updateDialog;

import com.android.bughdupdate.updateManager.UpdateDataSource;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.download.DownloadTask;


/**
 * Created by DaChao on 2017/12/6.
 */

public class BuglyUpdateRepository implements UpdateDataSource {

    private OnErrorListener onErrorListener;

    public BuglyUpdateRepository() {
    }

    @Override
    public String getVersionStr() {
        return null;
    }

    @Override
    public String getUpdateTimeStr() {
        return null;
    }

    @Override
    public String getUpdateContent() {
        return null;
    }


    @Override
    public int getProgress() {
        long save = Beta.getStrategyTask().getSavedLength();
        long total = Beta.getStrategyTask().getTotalLength();
        if (save != 0 && total != 0) {
            return (int) (Beta.getStrategyTask().getSavedLength() * 100 / Beta.getStrategyTask().getTotalLength());
        }
        return 0;
    }

    @Override
    public int getStatus() {
        switch (Beta.getStrategyTask().getStatus()) {
            case DownloadTask.PAUSED:
                return UpdateDataSource.STATUS_PAUSE;
            case DownloadTask.COMPLETE:
                return UpdateDataSource.STATUS_FINISH;
            case DownloadTask.FAILED:
                return UpdateDataSource.STATUS_ERROR;
            case DownloadTask.DOWNLOADING:
                return UpdateDataSource.STATUS_LOADING;
        }
        return 0;
    }

    @Override
    public void startUpdate() {
        Beta.getStrategyTask().download();
    }

    @Override
    public void pauseUpdate() {

    }

    @Override
    public void cancelUpdate() {
        Beta.getStrategyTask().stop();
    }

    @Override
    public void downloadFinish() {

    }

    @Override
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }


}
