package com.android.banana.commlib.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.android.banana.commlib.R;

/**
 * Created by qiaomu on 2018/3/1.
 */

public class ShareDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private Button rbtn1, rbtn2, rbtn3, btnCancel;

    public static final int SHARE_HOME_PAGER = 0;

    public static final int SHARE_GROUP_CHAT = 1;

    public static final int SHARE_GROUP_ZONE = 2;
    private View.OnClickListener mListener;

    public static ShareDialogFragment newInstance() {

        Bundle args = new Bundle();

        ShareDialogFragment fragment = new ShareDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getDialogTheme() {
        return Theme.SLIDE_FROM_TO_BOTTOM_THEME;
    }

    @Override
    protected int getDialogLayoutId() {
        return R.layout.dialog_share_subject;
    }

    @Override
    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected void onDialogCreate() {
        rbtn1 = (Button) rootView.findViewById(R.id.rbtn_share_home_pager);
        rbtn2 = (Button) rootView.findViewById(R.id.rbtn_share_group_chat);
        rbtn3 = (Button) rootView.findViewById(R.id.rbtn_share_group_zone);
        btnCancel = (Button) rootView.findViewById(R.id.btn_share_cancel);

        rbtn1.setOnClickListener(this);
        rbtn2.setOnClickListener(this);
        rbtn3.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_share_cancel) {
            this.dismiss();
            return;
        } else if (v.getId() == R.id.rbtn_share_home_pager) {
            v.setTag(SHARE_HOME_PAGER);
        } else if (v.getId() == R.id.rbtn_share_group_chat) {
            v.setTag(SHARE_GROUP_CHAT);
        } else if (v.getId() == R.id.rbtn_share_group_zone) {
            v.setTag(SHARE_GROUP_ZONE);
        }
        if (mListener != null)
            mListener.onClick(v);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mListener = listener;
    }
}
