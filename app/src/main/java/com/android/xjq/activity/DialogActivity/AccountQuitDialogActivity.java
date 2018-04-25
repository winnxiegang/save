package com.android.xjq.activity.DialogActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.presenters.InitBusinessHelper;
import com.tencent.ilivesdk.ILiveCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountQuitDialogActivity extends Activity {

    @BindView(R.id.titleTv)
    TextView titleTv;

    @BindView(R.id.messageTv)
    TextView messageTv;

    @BindView(R.id.cancelBtn)
    TextView cancelBtn;

    @BindView(R.id.okBtn)
    TextView okBtn;

    private static boolean mStarted = false;

    /**
     * @param shouldLogin 是否需要重新登录
     * @param message     提示信息
     */
    public static void startAccountQuitDialogActivity(boolean shouldLogin, String message) {
        if (mStarted) {
            return;
        }
        mStarted = true;
        Intent intent = new Intent();
        intent.setClass(XjqApplication.getContext(), AccountQuitDialogActivity.class);
        intent.putExtra("message", message);
        intent.putExtra("shouldLogin", shouldLogin);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        XjqApplication.getContext().startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_quit_dialog);

        ButterKnife.bind(this);

        String message = getIntent().getStringExtra("message");

        boolean shouldLogin = getIntent().getBooleanExtra("shouldLogin", false);

        messageTv.setText(message);

        if (shouldLogin) {
            // LoginInfoHelper.getInstance().clearUserInfoCache();
            // LoginInfoHelper.getInstance().getCache();
            LoginInfoHelper.getInstance().clearUserInfoCache();
            LoginInfoHelper.getInstance().getCache();
            okBtn.setText("重新登录");
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("com.android.xjq.login");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    InitBusinessHelper.reLoginLive(LoginInfoHelper.getInstance().getGuestIdentifier(),
                            LoginInfoHelper.getInstance().getGuestAppUserSign(),
                            new ILiveCallBack() {
                                @Override
                                public void onSuccess(Object data) {
                                    finish();
                                }

                                @Override
                                public void onError(String module, int errCode, String errMsg) {
                                    finish();
                                }
                            });
                }
            });
        } else {
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStarted = false;
    }
}
