package com.android.xjq.test;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;

import com.android.xjq.XjqApplication;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;


/**
 * Created by zhouyi on 2016/11/25 11:42.
 */

public class DebugDownloadUtils {

    public void showDialog(final Activity activity) {

        final IPAddressEnum ip = getIpAddress();

        ShowMessageDialog dialog = new ShowMessageDialog(activity, new OnMyClickListener() {
            @Override
            public void onClick(View v) {
                startDownload(activity, ip.getIp());
            }
        }, null, "检查更新" + ip.getDeveloperName() + "电脑上的最新版app");

    }

    private void startDownload(Activity activity, String url) {

        Uri uri = Uri.parse(url + "/hhs/app-release.apk");

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        activity.startActivity(intent);

    }

    private IPAddressEnum getIpAddress() {
        try {
            ApplicationInfo appInfo = XjqApplication.getContext().getPackageManager().getApplicationInfo(XjqApplication.getContext().getPackageName(), PackageManager.GET_META_DATA);
            int developer = appInfo.metaData.getInt("developer", 1);
            switch (developer) {
                case 1:
                    return IPAddressEnum.ZHOU_YI;
                case 2:
                    return IPAddressEnum.LING_JIU;
                case 3:
                    return IPAddressEnum.ZAO_ZAO;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return IPAddressEnum.ZHOU_YI;
    }
}
