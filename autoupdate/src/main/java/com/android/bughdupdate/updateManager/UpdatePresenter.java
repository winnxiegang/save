package com.android.bughdupdate.updateManager;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by DaChao on 2017/12/6.
 */

public class UpdatePresenter {

    private UpdateView updateView;
    private UpdateDataSource updateDataSource;
    private Handler handler;

    private Runnable refreshRunnable;

    public UpdatePresenter(final UpdateView updateView, final UpdateDataSource updateDataSource) {
        this.updateView = updateView;
        this.updateDataSource = updateDataSource;
        handler = new Handler(Looper.getMainLooper());

        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                updateView.setUpdateProgress(updateDataSource.getProgress());
                switch (updateDataSource.getStatus()) {
                    case UpdateDataSource.STATUS_START:
                        break;
                    case UpdateDataSource.STATUS_PAUSE:
                        updateView.pauseUpdate();
                        break;
                    case UpdateDataSource.STATUS_FINISH:
                        updateView.cancelView();
//                        Beta.startDownload();
                        break;
                    case UpdateDataSource.STATUS_CANCEL:
                        break;
                    case UpdateDataSource.STATUS_ERROR:
                        updateView.showFail();
                        break;
                    case UpdateDataSource.STATUS_LOADING:
                        handler.postDelayed(this, 500);
                        break;
                }
            }
        };

        updateView.setOnStatusChangedListener(new UpdateView.OnStatusChangedListener() {
            @Override
            public void onChanged(int newStatus) {
                switch (newStatus) {
                    case UpdateView.STATUS_START:
                        updateDataSource.startUpdate();
                        break;
                    case UpdateView.STATUS_PAUSE:
                        updateDataSource.pauseUpdate();
                        break;
                    case UpdateView.STATUS_CANCEL:
                        updateDataSource.cancelUpdate();
                        break;
                }
            }
        });

        updateDataSource.setOnErrorListener(new UpdateDataSource.OnErrorListener() {
            @Override
            public void error() {
                handler.removeCallbacks(refreshRunnable);
                updateView.showFail();
            }
        });

    }

    private void getProgress() {
        handler.post(refreshRunnable);
    }

    public void release() {
        handler.removeCallbacks(refreshRunnable);
    }

    public void startUpdate() {
        updateDataSource.startUpdate();
        getProgress();
    }

    public void cancelUpdate() {
        updateDataSource.cancelUpdate();
        updateView.cancelView();
    }

}
