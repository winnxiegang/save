package com.android.xjq.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.xjq.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 2017/3/6.
 */

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.versionNameTv)
    TextView versionNameTv;

    public static void startAboutUsActivity(Activity activity) {
        activity.startActivity(new Intent(activity, AboutUsActivity.class));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.at_about_us);

        ButterKnife.bind(this);

        setTitleBar(true, "关于我们");

        PackageInfo packageInfo = getPackageInfo(this);

        if (packageInfo != null) {
            try {

                String version = "V" + packageInfo.versionName;

                versionNameTv.setText(version);

            } catch (Exception e) {

            }

        }
    }

    private PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
