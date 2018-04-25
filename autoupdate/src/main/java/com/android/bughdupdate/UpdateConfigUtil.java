package com.android.bughdupdate;

import android.app.ProgressDialog;
import android.content.Context;

import com.tencent.bugly.beta.Beta;

/**
 * Created by lingjiu on 2016/10/31 11:02.
 */
public class UpdateConfigUtil {

    private boolean showNoVersionTip = false;

    private ProgressDialog progressDialog;

    private Context context;

    /**
     * @param context
     * @param showNoVersionTip 如果没有更新，是否提示没有更新
     */
    public void checkUpdate(Context context, boolean showNoVersionTip) {

        if (showNoVersionTip) {

            progressDialog = new ProgressDialog(context);

            progressDialog.setMessage("检查更新中");

            progressDialog.show();

            this.context = context;
        }

        this.showNoVersionTip = showNoVersionTip;

        buglyCheckUpdate();
    }


    private void buglyCheckUpdate() {

        BuglyUpdateUtils.showNoVersionTip = showNoVersionTip;

        if (showNoVersionTip) {
            BuglyUpdateUtils.listener = new OnBuglyCheckUpdateListener() {
                @Override
                public void onCheckFinish() {
                    dismissDialog();
                }
            };
        }

        Beta.checkUpgrade();

    }

    private void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void onDestroy() {
        context = null;
        progressDialog = null;
        BuglyUpdateUtils.listener = null;
    }
}
