package com.android.banana.groupchat.groupchat.setInfo;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;

import com.android.banana.R;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.SpannableStringHelper;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.groupchat.bean.GroupManageInfoBean;
import com.android.banana.groupchat.chatenum.MedalLabelApplyStatusEnum;
import com.android.banana.groupchat.groupchat.forbidden.ForbiddenListActivity;
import com.android.banana.groupchat.groupchat.groupcreat.FamilyCreatActivity;
import com.android.banana.groupchat.membermanage.SpeakSettingActivity;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.view.LabelTextView;
import com.android.httprequestlib.RequestContainer;

import org.json.JSONObject;

import static com.android.banana.groupchat.chatenum.MedalLabelApplyStatusEnum.APPLYING;
import static com.android.banana.groupchat.chatenum.MedalLabelApplyStatusEnum.AUDIT_PASS;
import static com.android.banana.groupchat.chatenum.MedalLabelApplyStatusEnum.AUDIT_REFUSE;
import static com.android.banana.groupchat.chatenum.MedalLabelApplyStatusEnum.NOT_APPLYING;

/**
 * Created by kokuma on 2017/10/25.
 */

public class GroupManageActivity extends BaseActivity4Jczj implements IHttpResponseListener, View.OnClickListener {
    private String groupId = "", groupChatId, permissionType, coatArmorName, coatArmorApplyStatus, memo, logoUrl, groupChatName;
    boolean isAllowChange, isCreator;

    private WrapperHttpHelper httpHelper;
    private LabelTextView viewLeaderSet, viewSpeechSet, viewForbid, updateGroupInfoTv;
    private ImageView checkBoxTheme;
    private LabelTextView viewShirtLabel;

    @Override
    public void onSuccess(RequestContainer request, Object result) {
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        if (requestEnum == JczjURLEnum.GROUP_CHAT_CTL_OPERATE && result != null) {
            GroupManageInfoBean bean = (GroupManageInfoBean) result;
            if (bean != null && bean.isSuccess()) {
                groupChatName = bean.getGroupChatName();
                isAllowChange = !isAllowChange;
                int resId = isAllowChange ? R.drawable.select_on : R.drawable.select_off;
                checkBoxTheme.setImageResource(resId);
            }

        }

    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        operateErrorResponseMessage(jsonObject);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_group_manage);
    }

    @Override
    protected void setUpView() {
        setUpToolbar(getString(R.string.group_manage), -1, MODE_BACK);
        mToolbar.setNavigationIcon(R.drawable.ic_back_black);
        mToolbar.setBackgroundColor(Color.WHITE);
        mToolbarTitle.setTextColor(Color.BLACK);

        viewLeaderSet = (LabelTextView) findViewById(R.id.viewLeaderSet);
        viewSpeechSet = (LabelTextView) findViewById(R.id.viewSpeechSet);
        viewForbid = (LabelTextView) findViewById(R.id.viewForbid);
        checkBoxTheme = (ImageView) findViewById(R.id.checkBoxTheme);
        viewShirtLabel = ((LabelTextView) findViewById(R.id.viewShirtLabel));
        updateGroupInfoTv = findViewOfId(R.id.update_group_info);

        viewLeaderSet.setOnClickListener(this);
        viewSpeechSet.setOnClickListener(this);
        viewForbid.setOnClickListener(this);
        viewShirtLabel.setOnClickListener(this);
        updateGroupInfoTv.setOnClickListener(this);

    }

    @Override
    protected void setUpData() {
        Intent intent = getIntent();
        if (intent != null) {
            isCreator = intent.getBooleanExtra("isCreator", false);
            isAllowChange = intent.getBooleanExtra("isAllowChange", false);
            groupId = intent.getStringExtra("groupId");
            groupChatId = intent.getStringExtra("groupChatId");
            permissionType = intent.getStringExtra("permissionType");
            coatArmorName = intent.getStringExtra("coatArmorName");
            memo = intent.getStringExtra("memo");
            coatArmorApplyStatus = intent.getStringExtra("coatArmorApplyStatus");
            logoUrl = intent.getStringExtra("logoUrl");
            groupChatName = getIntent().getStringExtra("groupName");
        }

        if (isCreator) {
            viewLeaderSet.setVisibility(View.VISIBLE);
            viewShirtLabel.setVisibility(View.VISIBLE);
            findViewById(R.id.layoutTheme).setVisibility(View.VISIBLE);
        } else {
            viewLeaderSet.setVisibility(View.GONE);
            findViewById(R.id.layoutTheme).setVisibility(View.GONE);
        }
        switch (MedalLabelApplyStatusEnum.safeValueOf(coatArmorApplyStatus)) {
            case AUDIT_PASS:
                setApplyPassText();
                break;
            case APPLYING:
                setApplyingText();
                break;
            case AUDIT_REFUSE:
                setApplyRefuseText();
                break;
            case NOT_APPLYING:
                viewShirtLabel.setText(NOT_APPLYING.getMessage());
                break;
        }

        viewSpeechSet.setLabelTextRight(permissionType);
        int resId = isAllowChange ? R.drawable.select_on : R.drawable.select_off;
        checkBoxTheme.setImageResource(resId);

        httpHelper = new WrapperHttpHelper(this);
        checkBoxTheme.setOnClickListener(this);
    }

    private void setApplyRefuseText() {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableString ss = new SpannableString("[图片]");
        Drawable drawable = ContextCompat.getDrawable(GroupManageActivity.this, R.drawable.icon_apply_status_failed);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ss.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(ss);
        ssb.append(SpannableStringHelper.changeTextColor(AUDIT_REFUSE.getMessage(), ContextCompat.getColor(this, R.color.light_red5)));
        viewShirtLabel.setText(ssb);
    }

    private void setApplyPassText() {
        if (TextUtils.isEmpty(coatArmorName)) return;
        viewShirtLabel.setText(coatArmorName);
        viewShirtLabel.setLabelPadding(getResources().getDimensionPixelOffset(R.dimen.dp20), 0,
                getResources().getDimensionPixelOffset(R.dimen.dp30), 0);
        viewShirtLabel.setPadding(viewShirtLabel.getPaddingLeft(), 0, getResources().getDimensionPixelOffset(R.dimen.dp30), 0);
        viewShirtLabel.setCompoundDrawables(viewShirtLabel.getCompoundDrawables()[0], null, null, null);
    }

    private void setApplyingText() {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableString ss = new SpannableString("[图片]");
        Drawable drawable = ContextCompat.getDrawable(GroupManageActivity.this, R.drawable.icon_apply_status_applying);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ss.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(ss);
        ssb.append(APPLYING.getMessage());
        viewShirtLabel.setPadding(viewShirtLabel.getPaddingLeft(), 0, getResources().getDimensionPixelOffset(R.dimen.dp35), 0);
        viewShirtLabel.setText(ssb);
        viewShirtLabel.setLabelPadding(getResources().getDimensionPixelOffset(R.dimen.dp20), 0, 0, 0);
        viewShirtLabel.setCompoundDrawables(viewShirtLabel.getCompoundDrawables()[0], null, null, null);
    }


    void toChangeThemeAllowMode() {
        if (TextUtils.isEmpty(groupId)) {
            return;
        }
        RequestFormBody container = new RequestFormBody(JczjURLEnum.GROUP_CHAT_CTL_OPERATE, true);
        container.put("groupId", groupId);
        container.put("groupChatId", groupChatId);
        container.put("allowedModifyThemeType", !isAllowChange);
        container.setGenericClaz(GroupManageInfoBean.class);

        httpHelper.startRequest(container, false);
    }

    @Override
    public void onClick(View view) {
        if (TextUtils.isEmpty(groupId)) {
            return;
        }
        int id = view.getId();
        if (id == R.id.viewLeaderSet) {
            toAcitivity(GroupLeaderSettingActivity.class);
        } else if (id == R.id.viewSpeechSet) {
            SpeakSettingActivity.startSpeakSettingActivity(this, groupId);
        } else if (id == R.id.viewForbid) {
            toAcitivity(ForbiddenListActivity.class);
        } else if (id == R.id.checkBoxTheme) {
            if (httpHelper != null) {
                toChangeThemeAllowMode();
            }
        } else if (id == R.id.viewShirtLabel) {
            MedalLabelApplyStatusEnum medalLabelApplyStatusEnum = MedalLabelApplyStatusEnum.safeValueOf(coatArmorApplyStatus);
            if (APPLYING == medalLabelApplyStatusEnum) {
                showToast("正在审核中...");
            }
            if (APPLYING != medalLabelApplyStatusEnum && AUDIT_PASS != medalLabelApplyStatusEnum) {
                GroupShirtLabelSettingActivity.startGroupShitLabelSettingActivityForResult(this, groupChatId, coatArmorName);
            }
        } else if (id == R.id.update_group_info) {
            FamilyCreatActivity.startFamilyCreatActivity(this, logoUrl, groupId, groupChatName, memo, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            permissionType = data.getStringExtra("currentSelectType");
            viewSpeechSet.setLabelTextRight(permissionType);
        } else if (GroupShirtLabelSettingActivity.REQUEST_SET_GROUP_SHIRT_LABEL == requestCode && resultCode == RESULT_OK) {
            setApplyingText();
            coatArmorApplyStatus = APPLYING.name();
        }
    }

    void toAcitivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.putExtra("groupId", groupId);
        startActivity(intent);
    }


    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("isAllowChange", isAllowChange);
        intent.putExtra("coatArmorApplyStatus", coatArmorApplyStatus);
        setResult(RESULT_OK, intent);
        super.finish();

    }
}
