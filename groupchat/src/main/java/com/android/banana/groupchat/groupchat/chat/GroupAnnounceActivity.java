package com.android.banana.groupchat.groupchat.chat;

import android.content.Intent;
import android.text.TextUtils;
import android.view.MenuItem;

import com.android.banana.R;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.utils.KeyboardHelper;
import com.android.banana.view.CountEditText;
import com.android.banana.view.TapLinearLayout;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qiaomu on 2017/6/1.
 */

public class GroupAnnounceActivity extends BaseActivity4Jczj implements IHttpResponseListener<JsonElement>, TapLinearLayout.onTapListener {
    CountEditText countTv;
    TapLinearLayout tapView;

    private WrapperHttpHelper helper;
    private String notice;
    private String groupId;
    private String groupCode;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_group_chat_announcement, R.string.group_chat_announcement_title, R.menu.menu_completed, MODE_BACK);
        countTv = findViewOfId(R.id.countTv);
        tapView = findViewOfId(R.id.tapView);
    }

    @Override
    protected void setUpView() {
        helper = new WrapperHttpHelper(this);
        tapView.setonTapListener(this);
    }

    @Override
    protected void setUpData() {
        groupId = getIntent().getStringExtra("groupId");
        groupCode = getIntent().getStringExtra("groupCode");
        notice = getIntent().getStringExtra("notice");

        countTv.setText(notice);
        if (!TextUtils.isEmpty(notice) && notice.length() <= countTv.getMaxLength())
            countTv.setSelection(notice.length());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        notice = countTv.getText().toString();
        onTouch(0, 0);

        RequestFormBody container = new RequestFormBody(JczjURLEnum.MODIFY_NOTICE, true);
        container.put("groupId", groupId);
        container.put("notice", notice);
        container.put("groupCode", groupCode);
        showProgressDialog();
        helper.startRequest(container, false);
        return false;
    }

    @Override
    public void onSuccess(RequestContainer request, JsonElement result) {
        closeProgressDialog();
        LibAppUtil.showTip(this, getString(R.string.perform_success));
        Intent intent = new Intent();
        intent.putExtra("notice", notice);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject object, boolean netFailed) {
        closeProgressDialog();
        try {
            operateErrorResponseMessage(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTouch(float x, float y) {
        boolean contain = KeyboardHelper.dispatchViewTouch(x, y, countTv);
        if (!contain)
            KeyboardHelper.hideSoftInput(countTv);
    }
}
