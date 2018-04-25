package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.bughdupdate.UpdateConfigUtil;
import com.android.xjq.R;
import com.android.xjq.presenters.InitBusinessHelper;
import com.android.xjq.test.DebugDownloadUtils;
import com.android.xjq.utils.live.DataCleanManager;
import com.tencent.ilivesdk.ILiveCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lingjiu on 2017/3/6 9:40.
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.cacheTv)
    TextView cacheTv;
    @BindView(R.id.logoutLayout)
    RelativeLayout logoutLayout;
    @BindView(R.id.debugUpdateLayout)
    RelativeLayout debugUpdateLayout;

    private UpdateConfigUtil updateConfigUtil;

    @OnClick(R.id.debugUpdateLayout)
    public void debugUpdate() {
        new DebugDownloadUtils().showDialog(this);
    }

    @OnClick(R.id.logoutLayout)
    public void logout() {
        ShowMessageDialog.Builder builder = new ShowMessageDialog.Builder();
        builder.setMessage("确定要退出登录吗?");
        builder.setPositiveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitBusinessHelper.iLiveLogoutOut(new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        toLogin(true);
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        toLogin(true);
                    }
                });
            }
        });
        builder.setTitle("提示");
        builder.setPositiveMessage("是");
        builder.setNegativeMessage("否");
        builder.setShowMessageMiddle(true);
        ShowMessageDialog dialog = new ShowMessageDialog(this, builder);

    }


    @OnClick(R.id.checkUpdateLayout)
    public void checkUpdate() {
        updateConfigUtil.checkUpdate(this, true);
    }


    @OnClick(R.id.aboutAppLayout)
    public void aboutApp() {
        AboutUsActivity.startAboutUsActivity(this);
    }

    @OnClick(R.id.clearCacheLayout)
    public void clearCache() {
        ShowMessageDialog.Builder builder = new ShowMessageDialog.Builder();

        builder.setMessage("确定清除缓存吗?");

        builder.setPositiveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCleanManager.cleanInternalCache(SettingActivity.this);
                getCacheSize();
            }
        });
        builder.setTitle("提示");
        builder.setPositiveMessage("是");
        builder.setNegativeMessage("否");
        builder.setShowMessageMiddle(true);
        ShowMessageDialog dialog = new ShowMessageDialog(this, builder);
    }

    public static void startSettingActivity(Activity activity) {

        activity.startActivity(new Intent(activity, SettingActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.at_seting);

        ButterKnife.bind(this);

        setTitleBar(true, "设置",null);

        getCacheSize();

        updateConfigUtil = new UpdateConfigUtil();

        if (LoginInfoHelper.getInstance().getUserId() == null) {

            logoutLayout.setVisibility(View.GONE);
        }

        // debugUpdateLayout.setVisibility(View.VISIBLE);
        debugUpdateLayout.setVisibility(View.GONE);

    }

    private void getCacheSize() {
        try {
            String cacheSize = DataCleanManager.getCacheSize(getCacheDir());
            cacheTv.setText(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        updateConfigUtil.onDestroy();
        super.onDestroy();
    }
}
