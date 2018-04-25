package com.android.banana.groupchat.groupchat.groupcreat;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
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
 * Created by qiaomu on 2017/10/24.
 * <p>
 * <p>
 * 家族创建 -----口令验证
 */

public class FamilyVerifyCommandActivity extends BaseActivity4Jczj implements IHttpResponseListener {

    private EditText verify_edit;

    private WrapperHttpHelper mHttpHelper;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_verify_command, getString(R.string.creat_family_title), R.menu.menu_next_step, MODE_BACK);
    }

    @Override
    protected void setUpView() {

        int themeColor = ContextCompat.getColor(this, R.color.creat_family_next_step);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        mToolbar.setNavigationIcon(R.drawable.ic_back_green);
        mToolbarTitle.setTextColor(themeColor);

        MenuItem menuItem = mToolbar.getMenu().getItem(0);
        TextView actionView = (TextView) menuItem.getActionView();
        if (actionView != null)
            actionView.setTextColor(themeColor);


        verify_edit = findViewOfId(R.id.verify_edit);
        //verify_edit.setText("3039163674200200980059140104");
    }

    @Override
    protected void setUpData() {
        mHttpHelper = new WrapperHttpHelper(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (TextUtils.isEmpty(verify_edit.getText().toString())) {
            verify_edit.setError(getString(R.string.pelease_input_valid_commad));
            return false;
        }

        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.GROUP_CREATE_BEFORE_CODE_VALIDATE, true);
        formBody.put("activationCode", verify_edit.getText().toString());
        showProgressDialog();
        mHttpHelper.startRequest(formBody);
        return super.onMenuItemClick(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && resultCode == RESULT_OK)
            this.finish();
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        closeProgressDialog();
        FamilyCreatActivity.startFamilyCreatActivity(this, verify_edit.getText().toString(), 100);
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        closeProgressDialog();
        operateErrorResponseMessage(jsonObject);
    }
}
