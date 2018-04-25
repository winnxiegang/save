package com.android.banana.commlib.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.banana.commlib.R;


/**
 * Created by ajiao on 2017/10/23 0023.
 */

public class CustomDialog extends ProgressDialog {
    private boolean mCancelable = false;

    public CustomDialog(Context context) {
        this(context, R.style.CustomDialog);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(null);
    }

    public void setCancelable(boolean b) {
        mCancelable = b;
    }

    public void showDialog(String loadingText) {
        show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private void init(String loadingText) {
        setCancelable(mCancelable);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.widget_load_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        if (!TextUtils.isEmpty(loadingText)) {
            ((TextView) findViewById(R.id.load_dialog_tv)).setText(loadingText);
        }
    }

}
