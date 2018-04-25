package com.android.banana.groupchat.groupchat.setInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.http.JczjURLEnum;
import com.android.httprequestlib.RequestContainer;

import org.json.JSONObject;

/**
 * 群战袍名设置
 * Created by lingjiu on 2017/12/8.
 */

public class GroupShirtLabelSettingActivity extends BaseActivity4Jczj implements IHttpResponseListener, View.OnClickListener {
    public static final int REQUEST_SET_GROUP_SHIRT_LABEL = 0;
    private WrapperHttpHelper httpHelper;
    private String groupId;
    private EditText editText;

    public static void startGroupShitLabelSettingActivityForResult(Activity activity, String groupId, String preApplyFailLabel) {
        Intent intent = new Intent(activity, GroupShirtLabelSettingActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("preApplyFailLabel", preApplyFailLabel);
        activity.startActivityForResult(intent, REQUEST_SET_GROUP_SHIRT_LABEL);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.at_group_shit_label_setting);
    }


    @Override
    protected void setUpView() {
        setTitleBar(true, getString(R.string.family_fight_shirt), null);
        TextView titleTv = findViewOfId(R.id.titleTv);
        titleTv.setTextColor(Color.BLACK);
        //titleTv.setText(R.string.family_fight_shirt);
        ((ImageView) findViewOfId(R.id.backIv)).setImageResource(R.drawable.ic_back_black);
        ((ImageView) findViewOfId(R.id.backIv)).setVisibility(View.VISIBLE);
        TextView okTv = findViewOfId(R.id.okTv);
        ((RelativeLayout) findViewOfId(R.id.ViewTitleLayout)).setBackgroundColor(Color.WHITE);
        okTv.setOnClickListener(this);
        okTv.setText(R.string.confirm);
        okTv.setTextColor(Color.BLACK);
        editText = findViewOfId(R.id.editView);
        isSupportTouchHideKeyBoard = true;
        httpHelper = new WrapperHttpHelper(this);
        groupId = getIntent().getStringExtra("groupId");
        String preApplyFailLabel = getIntent().getStringExtra("preApplyFailLabel");
        if (!TextUtils.isEmpty(preApplyFailLabel)) {
            editText.setText(preApplyFailLabel);
            editText.setSelection(preApplyFailLabel.length());
        }

    }

    @Override
    protected void setUpData() {

    }

    private void requestSetLabel() {
        if (TextUtils.isEmpty(editText.getText())) {
            showToast("还没有填写战袍名~");
            return;
        }
        RequestFormBody container = new RequestFormBody(JczjURLEnum.MEDAL_LABEL_APPLY, true);
        container.put("applyLabelContent", editText.getText().toString().trim());
        container.put("ownerId", groupId);
        container.put("ownerType", "GROUP_CHAT");
        httpHelper.startRequest(container, true);
        showProgressDialog();
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        hideProgressDialog();
        showToast("战袍名已提交审核！");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        operateErrorResponseMessage(jsonObject);
        hideProgressDialog();
    }

    @Override
    public void onClick(View v) {
        requestSetLabel();
    }
}
