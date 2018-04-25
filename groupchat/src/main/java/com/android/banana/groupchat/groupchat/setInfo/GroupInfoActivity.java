package com.android.banana.groupchat.groupchat.setInfo;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.event.GroupChatJoinExitEvent;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.groupchat.bean.GroupBean;
import com.android.banana.groupchat.bean.GroupManageInfoBean;
import com.android.banana.groupchat.groupchat.GroupShirtDetailActivity;
import com.android.banana.groupchat.groupchat.chat.GroupAnnounceActivity;
import com.android.banana.groupchat.groupchat.widget.MembersView;
import com.android.banana.groupchat.membermanage.GroupChatMembersManageActivity;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.view.LabelTextView;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;
import com.google.gson.JsonElement;
import com.jl.jczj.im.callback.IMComCallback;
import com.jl.jczj.im.im.ImGroupManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.banana.groupchat.groupchat.chat.GroupChatActivity.ACTIVITY_RESULT_GROUP_SETTING;

/**
 * Created by kokuma on 2017/10/25.
 */

public class GroupInfoActivity extends BaseActivity4Jczj implements IHttpResponseListener, View.OnClickListener {

    private WrapperHttpHelper httpHelper;

    private String groupId = "", groupChatId;

    GroupManageInfoBean manageInfoBean;

    CircleImageView ivPortrait;
    TextView tvName, tvLeaderName, tvIntro, tvNotice, tvBottomBtn, tvCopy;
    LabelTextView tvMemberTitle, tvManagerGroup, tvTheme, tvCheck, tvNoticeTitle, groupCard;
    TextView groupZoneTv;
    ImageView checkBoxTheme;
    MembersView membersView;
    RelativeLayout groupShirtLayout;
    int identity;

    private boolean isThemeEnable;

    public static void startGroupInfoActivity(AppCompatActivity from, String groupId, String groupChatId) {//, String groupCode
        Intent intent = new Intent(from, GroupInfoActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupChatId", groupChatId);
        from.startActivityForResult(intent, ACTIVITY_RESULT_GROUP_SETTING);
    }

    void setUI() {
        setUpToolbar("", -1, MODE_BACK);
        mToolbar.setNavigationIcon(R.drawable.ic_back_black);
        mToolbar.setBackgroundColor(Color.WHITE);
        mToolbarTitle.setTextColor(Color.BLACK);

        if (manageInfoBean != null) {
            setUpTitle(manageInfoBean.getGroupChatName());
            isThemeEnable = manageInfoBean.isOpenThemeMode();
            PicUtils.load(this, ivPortrait, manageInfoBean.getGroupLogoURL());
            String name = manageInfoBean.getGroupNo() == null ? "" : manageInfoBean.getGroupNo();
            tvName.setText("群号  " + name);
            tvLeaderName.setText(manageInfoBean.getCreateUserName());
            tvMemberTitle.setLabelTextRight(manageInfoBean.getCount() + "");
            groupCard.setText(manageInfoBean.getNickName());

            int resId = manageInfoBean.isOpenThemeMode() ? R.drawable.select_on : R.drawable.select_off;
            checkBoxTheme.setImageResource(resId);
            tvNotice.setText(manageInfoBean.getNotice());
            membersView.update(manageInfoBean.getUserInfoList());
            if (!TextUtils.isEmpty(manageInfoBean.getMemo()))
                tvIntro.setText(manageInfoBean.getMemo());

            //是否为管理员设置UI
            if (manageInfoBean.getUserRoleCode() != null) {
                if (manageInfoBean.getUserRoleCode().equals("GROUP_OWNER")) {
                    identity = 1;
                    initUIByisAdmin(true);
                    membersView.isManager = true;
                    membersView.isOwner = true;
                    tvBottomBtn.setVisibility(View.GONE);
                } else if (manageInfoBean.getUserRoleCode().equals("GROUP_ADMIN")) {
                    identity = 2;
                    initUIByisAdmin(true);
                    membersView.isManager = true;
                    membersView.isOwner = false;
                } else {
                    identity = 0;
                    membersView.isOwner = false;
                    initUIByisAdmin(false);
                }
            } else {
                initUIByisAdmin(false);
            }

            if (identity > 0) {
                //主题开启设置UI
                if (manageInfoBean.isAdminModifyThemeType() || identity == 1) {
                    findViewById(R.id.layoutThemeCheckbox).setVisibility(View.VISIBLE);
                    if (manageInfoBean.isOpenThemeMode()) {
                        tvTheme.setVisibility(View.VISIBLE);
                    } else {
                        tvTheme.setVisibility(View.GONE);
                    }
                } else {
                    findViewById(R.id.layoutThemeCheckbox).setVisibility(View.GONE);
                    tvTheme.setVisibility(View.GONE);
                }
            }
            LogUtils.e("xxl", "setUI-" + groupChatId);

        }

    }


    void initUIByisAdmin(boolean isAdmin) {
        if (!isAdmin) {
            // tvMemberTitle.setOnClickListener(null);
            findViewById(R.id.layoutManager).setVisibility(View.GONE);
            findViewById(R.id.layoutTheme).setVisibility(View.GONE);
            tvNoticeTitle.setOnClickListener(null);
            tvNoticeTitle.setCompoundDrawables(null, null, null, null);
        } else {
            // tvMemberTitle.setOnClickListener(this);
            findViewById(R.id.layoutManager).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutTheme).setVisibility(View.VISIBLE);
            tvNoticeTitle.setOnClickListener(this);
        }
    }


    @Override
    public void onSuccess(RequestContainer request, Object result) {
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        if (requestEnum == JczjURLEnum.GROUP_CHAT_SET_PAGE_QUERY && result != null) {
            GroupBean bean = (GroupBean) result;
            if (bean != null && bean.isSuccess()) {
                manageInfoBean = bean.getGroupSetPageInfo();
                setUI();
            }
        } else if (requestEnum == JczjURLEnum.GROUP_CHAT_CTL_OPERATE && result != null) {
            GroupManageInfoBean bean = (GroupManageInfoBean) result;
            if (bean != null && bean.isSuccess()) {
                // boolean openThemeMode=  bean.getBoolean("openThemeMode");
                manageInfoBean.setOpenThemeMode(!manageInfoBean.isOpenThemeMode());
                isThemeEnable = manageInfoBean.isOpenThemeMode();
                int resId = manageInfoBean.isOpenThemeMode() ? R.drawable.select_on : R.drawable.select_off;
                checkBoxTheme.setImageResource(resId);
                if (manageInfoBean.isOpenThemeMode()) {
                    tvTheme.setVisibility(View.VISIBLE);
                } else {
                    tvTheme.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        operateErrorResponseMessage(jsonObject);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_group_info);
    }

    @Override
    protected void setUpView() {
        membersView = (MembersView) findViewById(R.id.membersView);
        ivPortrait = (CircleImageView) findViewById(R.id.ivPortrait);
        tvCopy = (TextView) findViewById(R.id.tvCopy);
        tvName = (TextView) findViewById(R.id.tvName);
        tvLeaderName = (TextView) findViewById(R.id.tvLeaderName);
        tvIntro = (TextView) findViewById(R.id.tvIntro);
        tvNotice = (TextView) findViewById(R.id.tvNotice);
        tvBottomBtn = (TextView) findViewById(R.id.tvBottomBtn);
        groupShirtLayout = ((RelativeLayout) findViewOfId(R.id.groupShirtLayout));
        groupZoneTv = findViewOfId(R.id.groupZoneTv);
        groupCard = findViewOfId(R.id.group_card);

        tvMemberTitle = (LabelTextView) findViewById(R.id.tvMemberTitle);
        tvManagerGroup = (LabelTextView) findViewById(R.id.tvManagerGroup);
        tvTheme = (LabelTextView) findViewById(R.id.tvTheme);
        tvCheck = (LabelTextView) findViewById(R.id.tvCheck);
        tvNoticeTitle = (LabelTextView) findViewById(R.id.tvNoticeTitle);

        checkBoxTheme = (ImageView) findViewById(R.id.checkBoxTheme);

        tvCopy.setOnClickListener(this);
        tvMemberTitle.setOnClickListener(this);
        tvManagerGroup.setOnClickListener(this);
        tvTheme.setOnClickListener(this);
        tvCheck.setOnClickListener(this);
        tvNoticeTitle.setOnClickListener(this);
        tvBottomBtn.setOnClickListener(this);
        groupShirtLayout.setOnClickListener(this);
        groupZoneTv.setOnClickListener(this);
        groupCard.setOnClickListener(this);

        checkBoxTheme.setOnClickListener(this);
    }

    @Override
    protected void setUpData() {
        Intent intent = getIntent();
        if (intent != null) {
            groupId = intent.getStringExtra("groupId");
            groupChatId = intent.getStringExtra("groupChatId");
        }
        setUpToolbar(getString(R.string.group_info), -1, MODE_BACK);
        httpHelper = new WrapperHttpHelper(this);
        membersView.groupId = groupId;

    }

    void getGroupInfo() {
        if (TextUtils.isEmpty(groupChatId))
            return;

        RequestFormBody container = new RequestFormBody(JczjURLEnum.GROUP_CHAT_SET_PAGE_QUERY, true);
        container.put("groupChatId", groupChatId);
        container.setGenericClaz(GroupBean.class);
        httpHelper.startRequest(container, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9 && resultCode == RESULT_OK && data != null&&manageInfoBean!=null) {
            boolean isAdminModifyThemeType = manageInfoBean.isAdminModifyThemeType();
            manageInfoBean.setAdminModifyThemeType(data.getBooleanExtra("isAllowChange", isAdminModifyThemeType));
            GroupManageInfoBean.MedalLabelApplyStatusBean coatArmorApplyStatus = manageInfoBean.getCoatArmorApplyStatus();
            if (coatArmorApplyStatus != null)
                coatArmorApplyStatus.setName(data.getStringExtra("coatArmorApplyStatus"));
            setUI();
        } else if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            setIntent(data);
            String notice = data.getStringExtra("notice");
            manageInfoBean.setNotice(notice);
            tvNotice.setText(notice);
        } else if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            String nickResult = data.getStringExtra("result");
            manageInfoBean.setNickName(nickResult);
            groupCard.setText(nickResult);
        }
    }

    @Override
    public void onClick(View view) {
        if (TextUtils.isEmpty(groupId) || manageInfoBean == null) {
            return;
        }
        int id = view.getId();
        if (id == R.id.tvMemberTitle) {
            GroupChatMembersManageActivity.startGroupChatMembersManageActivity(this, groupId, identity > 0,identity==1);
        } else if (id == R.id.tvManagerGroup) {
            toGroupManage();
        } else if (id == R.id.tvTheme) {
            toSetTheme();
        } else if (id == R.id.tvCheck) {
            toGroupMemberCheck();
        } else if (id == R.id.tvNoticeTitle) {
            toGroupAnnounce();
        } else if (id == R.id.tvBottomBtn) {
            showExitGroupDialog();
        } else if (id == R.id.checkBoxTheme) {
            toChangeIsOpenThemeSet();
        } else if (id == R.id.tvCopy) {
            ClipboardManager cm = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            //将文本数据复制到剪贴板
            String name = manageInfoBean.getGroupNo() == null ? "" : manageInfoBean.getGroupNo();
            cm.setText(name);
            LibAppUtil.showTip(this, "复制成功");
        } else if (id == R.id.groupShirtLayout) {
            GroupShirtDetailActivity.startGroupShirtDetailActivity(this, groupChatId);
        } else if (id == R.id.groupZoneTv) {
            Intent intent = new Intent("com.android.banana.zone");
            intent.putExtra("groupId", groupId);
            intent.putExtra("groupName", manageInfoBean.getGroupChatName());
            intent.putExtra("groupMemo", manageInfoBean.getMemo());
            intent.putExtra("groupLogoUrl", manageInfoBean.getGroupLogoURL());
            startActivity(intent);
        } else if (id == R.id.group_card) {
            Intent intent = new Intent("com.android.xjq.simple_modify_activity");
            intent.putExtra("hint", groupCard.getText().toString());
            intent.putExtra("max_Length", 12);
            intent.putExtra("post_card_group_id", groupId);
            intent.putExtra("post_card_user_id", LoginInfoHelper.getInstance().getUserId());
            intent.putExtra("operate", 0);
            startActivityForResult(intent, 1000);
        }
    }

    void showExitGroupDialog() {
        ShowMessageDialog dialog = new ShowMessageDialog(this, new OnMyClickListener() {
            @Override
            public void onClick(View v) {
                exitGroup();
            }
        }, new OnMyClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, "退出聊天室后，如有战袍也将失效，确定退出?");
    }

    void exitGroup() {
        // 关掉，黑底白字提示：您已成功退出该群
        ImGroupManager.getInstance().exitGroup(new IMComCallback<JsonElement>() {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtils.e("kk", "EventBus.getDefault().post(new GroupChatJoinExitEvent(groupId, true)); ");
                EventBus.getDefault().post(new GroupChatJoinExitEvent(groupId, true));
                closeProgressDialog();
                LibAppUtil.showTip(GroupInfoActivity.this, "退出成功", R.drawable.icon_subscribe_success, Gravity.CENTER);
                setResult(RESULT_OK);
                GroupInfoActivity.this.finish();
            }

            @Override
            public void onError(ErrorBean errorBean) {
                closeProgressDialog();
                showToast(errorBean.error.message);
            }
        });
    }


    void toSetTheme() {
        Intent intent = new Intent("com.android.banana.theme");
        intent.putExtra("groupChatId", groupChatId);
        startActivity(intent);
    }

    void toChangeIsOpenThemeSet() {
        if (manageInfoBean == null) {
            return;
        }
        RequestFormBody container = new RequestFormBody(JczjURLEnum.GROUP_CHAT_CTL_OPERATE, true);
        container.put("groupId", groupId);
        container.put("groupChatId", manageInfoBean.getGroupChatId());
        container.put("openThemeMode", !manageInfoBean.isOpenThemeMode());
        container.setGenericClaz(GroupManageInfoBean.class);
        httpHelper.startRequest(container, false);
    }

    void toGroupMemberCheck() {
        if (manageInfoBean == null) {
            return;
        }
        Intent intent = new Intent(this, GroupMemberCheckActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupChatId", manageInfoBean.getGroupChatId());
        startActivity(intent);
    }

    void toGroupAnnounce() {
        if (manageInfoBean == null) {
            return;
        }
        Intent intent = new Intent(this, GroupAnnounceActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupCode", manageInfoBean.getGroupCode());
        intent.putExtra("notice", manageInfoBean.getNotice());
        startActivityForResult(intent, 100);
    }

    void toGroupManage() {
        if (manageInfoBean == null) {
            return;
        }
        Intent intent = new Intent(this, GroupManageActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("isAllowChange", manageInfoBean.isAdminModifyThemeType());
        intent.putExtra("groupChatId", manageInfoBean.getGroupChatId());
        intent.putExtra("isCreator", identity == 1);
        intent.putExtra("coatArmorName", manageInfoBean.getCoatArmorName());
        intent.putExtra("groupName", manageInfoBean.getGroupChatName());
        intent.putExtra("memo", manageInfoBean.getMemo());
        intent.putExtra("logoUrl", manageInfoBean.getGroupLogoURL());
        if (manageInfoBean.getPermissionType() != null) {
            intent.putExtra("permissionType", manageInfoBean.getPermissionType().getMessage());
        }
        intent.putExtra("coatArmorName", manageInfoBean.getCoatArmorName());
        if (manageInfoBean.getCoatArmorApplyStatus() != null) {
            intent.putExtra("coatArmorApplyStatus", manageInfoBean.getCoatArmorApplyStatus().getName());
        }

        startActivityForResult(intent, 9);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroupInfo();
    }

    @Override
    public void onNavigationBtnClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("notice", tvNotice.getText().toString());
        intent.putExtra("theme_enable", isThemeEnable);
        intent.putExtra("theme_query", manageInfoBean != null);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

}
