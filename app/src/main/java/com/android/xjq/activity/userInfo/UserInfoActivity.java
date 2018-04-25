package com.android.xjq.activity.userInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.view.MedalLayout;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.groupchat.chat.SimpleChatActivity;
import com.android.banana.groupchat.chatenum.ChatRoomMemberLevelEnum;
import com.android.banana.groupchat.groupchat.chat.ChatAdapterHelper;
import com.android.banana.groupchat.groupchat.forbidden.ForbiddenActivitty;
import com.android.banana.groupchat.ilistener.ChatAdapterHelperCallbackImpl;
import com.android.banana.utils.DialogHelper;
import com.android.banana.utils.ListSelectDialog;
import com.android.banana.utils.fileupload.FileUploadManager;
import com.android.banana.utils.fileupload.SimpleUploadCallback;
import com.android.banana.view.LabelTextView;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.SimpleModifyActivity;
import com.android.xjq.activity.homepage.HomePageActivity;
import com.android.xjq.activity.setting.BindPhoneNumActivity;
import com.android.xjq.activity.setting.CertificationActivity;
import com.android.xjq.bean.GroupCardBean;
import com.android.xjq.bean.UserMedalLevelBean;
import com.android.xjq.bean.userInfo.UserInfoClientBean;
import com.android.xjq.utils.SocialTools;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.etiennelawlor.imagegallery.library.entity.Photo;
import com.etiennelawlor.imagegallery.library.entity.StoreSelectBean;
import com.etiennelawlor.imagegallery.library.fullscreen.EditPhotoActivity;
import com.etiennelawlor.imagegallery.library.fullscreen.ImageGalleryActivity;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends BaseActivity4Jczj implements OnHttpResponseListener {
    @BindView(R.id.realNameTv)
    LabelTextView realNameTv;
    @BindView(R.id.bind_phone_lay)
    LabelTextView phoneLableTV;
    @BindView(R.id.certificate_lay)
    LabelTextView certificate_lay;
    @BindView(R.id.user_avart)
    CircleImageView mUserAvart;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.pay_attention)
    CheckedTextView mPayAttention;
    @BindView(R.id.medal_layout)
    MedalLayout mMedalLayout;
    @BindView(R.id.camera_iv)
    ImageView mCameraIv;
    @BindView(R.id.info_layout)
    LinearLayout mInfoLayout;
    @BindView(R.id.edit_group_card)
    LabelTextView mEditGroupCardTv;
    @BindView(R.id.user_homePager)
    LabelTextView mHomePagerTv;
    @BindView(R.id.secretChatLayout)
    LinearLayout secretChatLayout;
    //用户个人基础信息
    private static final int PERSON_USER_INFO = 0;

    //他人的群名片
    private static final int PERSON_POST_CARD = 1;

    //默认会进入个人基础信息
    private int mCurOperate = PERSON_USER_INFO;

    private HttpRequestHelper httpRequestHelper = new HttpRequestHelper(this, TAG);

    private XjqUrlEnum mUrlEnum;
    private String mbindPhone, mUserId, mGroupId, mUploadFilePath;

    private ChatAdapterHelper mChatAdapterHelper;
    private boolean isForbidden;
    private boolean mIsManager;//管理员暂定包括群主和管理员
    private boolean mIsOwner;//是不是群主（可以判断是管理员还是群主）
    private String currentLevelCode;
    private Unbinder bind;

    public static void startUserInfoActivity4Self(Activity activity, String userId) {
        Intent intent = new Intent(activity, UserInfoActivity.class);
        intent.putExtra("userId", userId);
        activity.startActivity(intent);
    }

    @Override
    protected void initEnv() {
        mUserId = getIntent().getStringExtra("userId");
        mGroupId = getIntent().getStringExtra("groupId");
        mIsManager = getIntent().getBooleanExtra("isAdmin", false);
        mIsOwner = getIntent().getBooleanExtra("isOwner", false);
        currentLevelCode = getIntent().getStringExtra("mClickObjectRoleCode");
        mCurOperate = TextUtils.isEmpty(mGroupId) ? PERSON_USER_INFO : PERSON_POST_CARD;

        if (mCurOperate == PERSON_USER_INFO) {
            mUrlEnum = XjqUrlEnum.MY_USER_INFO_QUERY;//个人资料
        } else {
            mUrlEnum = XjqUrlEnum.GROUP_USER_INFO_QUERY;//群个人名片
        }
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_my_info);
        bind = ButterKnife.bind(this);
    }

    @Override
    protected void setUpView() {
        //自己的不显示点点点
        boolean show = judgeLimitEdit() &&(!TextUtils.equals(mUserId, LoginInfoHelper.getInstance().getUserId()));
        setUpToolbar("", (mCurOperate == PERSON_POST_CARD && show) ? R.menu.menu_setting : -1, MODE_BACK);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);

        boolean isShowInfo = mCurOperate == PERSON_USER_INFO;

        mCameraIv.setVisibility(isShowInfo ? View.VISIBLE : View.GONE);
        certificate_lay.setVisibility(isShowInfo ? View.VISIBLE : View.GONE);
        phoneLableTV.setVisibility(isShowInfo ? View.VISIBLE : View.GONE);

        mMedalLayout.setVisibility(isShowInfo ? View.GONE : View.VISIBLE);
        mInfoLayout.setVisibility(isShowInfo ? View.GONE : View.VISIBLE);
        mEditGroupCardTv.setVisibility(isShowInfo ? View.GONE : View.VISIBLE);

        mHomePagerTv.setLabelTextLeft(getString(isShowInfo ? R.string.home_pager : R.string.home_pager_ta));
    }

    @Override
    protected void setUpData() {
        XjqRequestContainer container = new XjqRequestContainer(mUrlEnum, true);
        container.put("userId", mUserId);
        container.put("groupId", mGroupId);
        httpRequestHelper.startRequest(container, false);
    }

    // des: 2017/11/20 绑定手机号
    @OnClick(R.id.bind_phone_lay)
    public void PhoneClick() {

        BindPhoneNumActivity.startBindPhoneNumActivity(UserInfoActivity.this, mbindPhone);
    }

    // des: 2017/11/20 验证身份证
    @OnClick(R.id.certificate_lay)
    public void toIdentify() {

        CertificationActivity.startCertificationActivity(UserInfoActivity.this);
    }

    // des :个人主页
    @OnClick(R.id.user_homePager)
    public void toHomePager() {

        HomePageActivity.startHomepageActivity(this, mUserId);
    }

    // des 修改头像
    @OnClick(R.id.user_avart)
    public void toImageGalleryActivity() {
        if (!TextUtils.isEmpty(mGroupId))
            return;
        ImageGalleryActivity.startImageGalleryActivity(this, 1, ImageGalleryActivity.INSERT_IMAGE);
    }

    //修改群名片
    @OnClick(R.id.edit_group_card)
    public void toModifyPostCardNickName() {
        SimpleModifyActivity.startModifyPostCard4Result(this, mEditGroupCardTv.getText().toString(), mGroupId, mUserId, 12);
    }

    //关注TA
    @OnClick(R.id.pay_attention)
    public void onPayAttentionClick() {
        if (!mPayAttention.isChecked()) {
            SocialTools.payAttention(mUserId, new SocialTools.onSocialCallback() {
                @Override
                public void onResponse(JSONObject response, boolean success) {
                    if (success) {
                        mPayAttention.setChecked(true);
                    } else {
                        try {
                            operateErrorResponseMessage(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            SocialTools.cancelAttention(mUserId, "FOLLOWCANCEL", new SocialTools.onSocialCallback() {
                @Override
                public void onResponse(JSONObject response, boolean success) {
                    if (success) {
                        mPayAttention.setChecked(false);
                    } else {
                        try {
                            operateErrorResponseMessage(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //CHAT_MEMBER_REMOVE_RELATION_GROUP
        DialogHelper.showListDialog(getSupportFragmentManager(), new ListSelectDialog.OnClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                switch (pos) {
                    case 0:
                        if (isForbidden) {
                            getChatHelper().cancelForbidden("", mUserId, mGroupId);
                        } else {
                            ForbiddenActivitty.start4GagActivity(UserInfoActivity.this, mUserId, mGroupId);
                        }
                        break;
                    case 1:
                        getChatHelper().removeChatRoomMember(mGroupId, mUserId);
                        break;
                }
            }
        }, (isForbidden ? getString(R.string.cancel_forbidden) : getString(R.string.forbidden)), getString(R.string.remove_out_group));
        return false;
    }

    private ChatAdapterHelper getChatHelper() {
        if (mChatAdapterHelper == null) {
            mChatAdapterHelper = new ChatAdapterHelper(ChatAdapterHelper.ChatType.GROUP_CHAT);
            mChatAdapterHelper.setHelperCallback(new ChatAdapterHelperCallbackImpl() {
                @Override
                public void onRemoveGroupChatMemberResult(int adapterPos, JSONObject jsonError) throws JSONException {
                    if (jsonError != null) {
                        operateErrorResponseMessage(jsonError);
                    } else {
                        showToast(getString(R.string.remove_group_chat_member_success));
                        UserInfoActivity.this.finish();
                    }
                }

                @Override
                public void onCancelForbiddenResult(boolean success, JSONObject jsonError) throws JSONException {
                    isForbidden = success ? false : isForbidden;
                    operateErrorResponseMessage(jsonError);
                    if (success)
                        showToast(getString(R.string.cancel_forbidden_success));
                }

            });
        }

        return mChatAdapterHelper;
    }

    private void responseUserInfo(JSONObject jsonObject) throws JSONException {
        UserInfoClientBean bean = new Gson().fromJson(jsonObject.toString(), UserInfoClientBean.class);
        UserInfoClientBean.UserInfoClientEntity userInfo = bean.getUserInfo();

        if (userInfo != null) {
            PicUtils.load(this, mUserAvart, userInfo.getUserLogoUrl(), R.drawable.user_default_logo);

            mbindPhone = userInfo.getCell();
            phoneLableTV.setText(mbindPhone);

            certificate_lay.setEnabled(!userInfo.isIdentifySetted());
            realNameTv.setText(userInfo.getLoginName());
            realNameTv.setTextColor(getResources().getColor(R.color.colorGray4));

            if (userInfo.isIdentifySetted()) {

                certificate_lay.setText(userInfo.getRealName() + "\n" + userInfo.getCertNo());
                certificate_lay.setTextColor(getResources().getColor(R.color.colorGray4));
                certificate_lay.setCompoundDrawables(certificate_lay.getCompoundDrawables()[0], null, null, null);

            } else {
                NormalObject identityStatus = bean.getIdentityStatus();
                if (bean.identifySetted) {
                    certificate_lay.setText(SpannableStringHelper.changeTextColor(identityStatus == null ? "" : identityStatus.getMessage()));
                } else {
                    certificate_lay.setText(SpannableStringHelper.changeTextColor("未认证"));
                }
                certificate_lay.setTextColor(Color.parseColor("#fb3f3f"));
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.icon_insert_match_right_arrow);
                certificate_lay.setCompoundDrawablesWithIntrinsicBounds(certificate_lay.getCompoundDrawables()[0], null, drawable, null);

            }
        }
    }

    private void responseGroupCardInfo(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        if (jsonObject.has("groupUserClientSimple")) {
            JSONObject userSimpleInfo = jsonObject.optJSONObject("groupUserClientSimple");
            final GroupCardBean bean = new Gson().fromJson(userSimpleInfo.toString(), GroupCardBean.class);

            realNameTv.setText(bean.userName);
            String nickName = bean.nickName!=null&&!TextUtils.equals("",bean.nickName)?bean.nickName:bean.userName;
            mUserName.setText(nickName);
            mUserName.setVisibility(TextUtils.isEmpty(nickName) ? View.GONE : View.VISIBLE);

            mPayAttention.setVisibility(TextUtils.equals(mUserId, LoginInfoHelper.getInstance().getUserId()) ? View.GONE : View.VISIBLE);
            mPayAttention.setChecked(bean.focus);

            mEditGroupCardTv.setText(bean.nickName);
            mEditGroupCardTv.setCompoundDrawables(mEditGroupCardTv.getCompoundDrawables()[0], null, mIsManager ? mEditGroupCardTv.getCompoundDrawables()[2] : null, null);
            //规则是这样的 群主可以修改所有人的，管理员可以修改普通用户加自己，普通用户只能该自己的
            mEditGroupCardTv.setEnabled(judgeLimitEdit());
            if (judgeLimitEdit()) {
                mEditGroupCardTv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_group_postcard), null, getResources().getDrawable(R.drawable.icon_insert_match_right_arrow), null);
            } else {
                mEditGroupCardTv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_group_postcard), null, null, null);
            }
            if (bean.userMedalLevelList != null) {
                mMedalLayout.setVisibility(View.VISIBLE);
                mMedalLayout.removeAllViews();
                for (UserMedalLevelBean medalLevel : bean.userMedalLevelList) {
                    mMedalLayout.addMedal(SubjectMedalEnum.getMedalResourceId(this, medalLevel.medalConfigCode, medalLevel.currentMedalLevelConfigCode));
                }
            }

            PicUtils.load(this, mUserAvart, bean.userLogoUrl);
            isForbidden = bean.forbidden;

            if (bean.canInitiatePrivateChat) {
                secretChatLayout.setVisibility(View.VISIBLE);
                secretChatLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleChatActivity.startSimpleChatActivity(UserInfoActivity.this,
                                mUserId, bean.userName, null, mGroupId, "GROUP");
                    }
                });
            }
        }
    }

    private boolean judgeLimitEdit() {
        //群主，或者是自己，都可以修改
        if (mIsOwner || TextUtils.equals(mUserId, LoginInfoHelper.getInstance().getUserId())) {
            return true;
        }
        //管理员，只能修改普通用户的
        if (mIsManager && currentLevelCode.equals(ChatRoomMemberLevelEnum.NORMAL.name())) {
            return true;
        }

        return false;
    }


    List<Photo> list = new ArrayList<>();

    @Subscribe
    public void onEvent(StoreSelectBean selectBean) {
        if (selectBean == null || selectBean.getList() == null)
            return;
        list = (List<Photo>) selectBean.getList();
        if (list == null || list.size() == 0)
            return;
        mUploadFilePath = list.get(0).getPath();
        EditPhotoActivity.startEditPhotoActivity(this, mUploadFilePath);
    }

    @Subscribe
    public void onEvent(final Bitmap selectBitmap) {
        LogUtils.e("kk", selectBitmap + "----------------");
        mUserAvart.setImageBitmap(selectBitmap);
        showProgressDialog();
        FileUploadManager.upload(list, null, XjqUrlEnum.USER_LOGO_EDIT, new SimpleUploadCallback() {
            @Override
            public void onUploaded(int posInAdapter, int curPosition) {
                closeProgressDialog();
                showToast(getString(R.string.avart_modify_success));
                selectBitmap.recycle();
            }

            @Override
            public void onUploadError(JSONObject jsonObject, int posInAdapter, int curPosition) {
                try {
                    closeProgressDialog();
                    selectBitmap.recycle();
                    operateErrorResponseMessage(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setUpData();
        }

        if (requestCode == ForbiddenActivitty.FORBIDDEN_REQUEST_CODE)
            isForbidden = true;

    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
                case MY_USER_INFO_QUERY:
                    responseUserInfo(jsonObject);
                    break;
                case GROUP_USER_INFO_QUERY:
                    responseGroupCardInfo(jsonObject);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

}
