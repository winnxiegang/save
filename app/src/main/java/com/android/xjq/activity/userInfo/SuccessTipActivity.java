package com.android.xjq.activity.userInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.xjq.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lingjiu on 2017/11/1.
 */

public class SuccessTipActivity extends BaseActivity {

    @BindView(R.id.successTxt)
    TextView successTxt;

    @OnClick(R.id.successBtn)
    public void successBtn() {
        finish();
    }

    public static void startSuccessTipActivity(Activity activity, String successTip) {
        Intent intent = new Intent(activity, SuccessTipActivity.class);
        intent.putExtra("successTip", successTip);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_success_tip);
        ButterKnife.bind(this);
        setTitleBar(true, "成功提示");

        String successTip = getIntent().getStringExtra("successTip");
        successTxt.setText(successTip);
    }
}
