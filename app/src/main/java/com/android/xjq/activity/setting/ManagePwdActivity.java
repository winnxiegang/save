package com.android.xjq.activity.setting;

import android.os.Bundle;

import com.android.xjq.R;
import com.android.banana.commlib.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by ajiao on 2017/10/25 0025.
 */

public class ManagePwdActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_pwd);
        setTitleBar(true, getString(R.string.set_pwd));
        ButterKnife.bind(this);
    }
}
