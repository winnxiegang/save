package com.android.banana.commlib.view.loading;

/**
 * Created by lingjiu on 2018/2/27.
 */

public interface ProgressListener {
    void pause(long mCurrentProgress, long maxProgress);

    void progressing(long currentProgress, long maxProgress);
}
