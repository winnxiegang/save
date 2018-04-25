package com.android.xjq.dialog.live;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.MedalLayout;
import com.android.banana.groupchat.chat.SimpleChatActivity;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.homepage.HomePageActivity;
import com.android.xjq.bean.UserMedalLevelBean;
import com.android.xjq.bean.homepage.UserInfoData;
import com.android.xjq.dialog.base.BaseDialog;
import com.android.xjq.dialog.base.ViewHolder;
import com.android.xjq.utils.SocialTools;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人信息弹窗
 * <p>
 * Created by lingjiu on 2018/2/2.
 */

public class PersonalInfoDialog extends BaseDialog implements IHttpResponseListener<UserInfoData> {

    @BindView(R.id.reportTv)
    TextView reportTv;
    @BindView(R.id.userNameTv)
    TextView userNameTv;
    @BindView(R.id.fansTv)
    TextView fansTv;
    @BindView(R.id.userInfoLayout)
    LinearLayout userInfoLayout;
    @BindView(R.id.homeTv)
    TextView homeTv;
    @BindView(R.id.chatTv)
    TextView chatTv;
    @BindView(R.id.focusTv)
    TextView focusTv;
    @BindView(R.id.headIv)
    ImageView headIv;
    @BindView(R.id.medalLayout)
    MedalLayout medalLayout;

    private WrapperHttpHelper httpHelper;
    private String userId;
    private boolean hasFollow;
    private UserInfoData userInfoData;

    private Callback callback;

    @OnClick({R.id.homeTv, R.id.chatTv, R.id.focusTv})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.homeTv:
                HomePageActivity.startHomepageActivity(mContext, userId);
                break;
            case R.id.chatTv:
                if (userInfoData == null) return;
                SimpleChatActivity.startSimpleChatActivity(mContext, userInfoData.getUserId(), userInfoData.getLoginName(), null);
                break;
            case R.id.focusTv:
                if (userInfoData == null) return;
                operateFollow();
                break;
        }
    }

    private void operateFollow() {
        if (hasFollow) {
            SocialTools.cancelAttention(userInfoData.getUserId(), "FOLLOWCANCEL", new SocialTools.onSocialCallback() {
                @Override
                public void onResponse(JSONObject response, boolean success) {
                    if (success) {
                        focusTv.setText(R.string.title_attention);
                        hasFollow = !hasFollow;
                        ToastUtil.showShort(mContext.getApplicationContext(), mContext.getString(R.string.cancel_focus));
                        if (callback != null)
                            callback.focusStatusChanged(userId, hasFollow);
                    } else {
                        try {
                            ((BaseActivity) mContext).operateErrorResponseMessage(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            SocialTools.payAttention(userInfoData.getUserId(), new SocialTools.onSocialCallback() {
                @Override
                public void onResponse(JSONObject response, boolean success) {
                    if (success) {
                        focusTv.setText(R.string.cancel_focus);
                        hasFollow = !hasFollow;
                        ToastUtil.showShort(mContext.getApplicationContext(), mContext.getString(R.string.focus_success));
                        if (callback != null)
                            callback.focusStatusChanged(userId, hasFollow);
                    } else {
                        try {
                            ((BaseActivity) mContext).operateErrorResponseMessage(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public PersonalInfoDialog(Context context, String userId) {
        super(context);
        setMargin(50);
        setAnimStyle(R.style.dialogWindowAnim);
        this.userId = userId;
        httpHelper = new WrapperHttpHelper(this);
    }

    public PersonalInfoDialog(Context context, String userId, Callback callback) {
        super(context);
        setMargin(50);
        setAnimStyle(R.style.dialogWindowAnim);
        this.userId = userId;
        httpHelper = new WrapperHttpHelper(this);
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryUserInfo();
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_personal_info_layout;
    }

    private void queryUserInfo() {
        RequestFormBody map = new RequestFormBody(XjqUrlEnum.USER_INFO_QUERY, true);
        map.put("userId", userId);
        httpHelper.startRequest(map);
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialog dialog) {

    }

    @Override
    public void dismiss() {
        super.dismiss();
        httpHelper.onDestroy();
    }

    @Override
    public void onSuccess(RequestContainer request, UserInfoData userInfoData) {
        try {
            this.userInfoData = userInfoData;
            PicUtils.load(mContext, headIv, userInfoData.getUserLogoUrl(), R.drawable.user_default_logo);
            userNameTv.setText(userInfoData.getLoginName());
            fansTv.setText(String.format(mContext.getString(R.string.focus_and_fans_number), userInfoData.getUserAttentionNum(),
                    userInfoData.getUserFollowsNum()));
            chatTv.setVisibility(userInfoData.isCanInitiatePrivateChat() ? View.VISIBLE : View.GONE);
            hasFollow = userInfoData.isFollow();
            focusTv.setText(hasFollow ? mContext.getString(R.string.cancel_focus) : mContext.getString(R.string.title_attention));
            // setFollowView();
            if (userInfoData.userMedalLevelList != null) {
                for (UserMedalLevelBean userMedalLevelBean : userInfoData.userMedalLevelList) {
                    medalLayout.addMedal(SubjectMedalEnum.getMedalResourceId(mContext,
                            userMedalLevelBean.medalConfigCode, userMedalLevelBean.currentMedalLevelConfigCode));
                }
            }
            boolean isHideFocus = TextUtils.equals(userInfoData.getUserId(), LoginInfoHelper.getInstance().getUserId());
            focusTv.setVisibility(isHideFocus ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        ((BaseActivity) mContext).operateErrorResponseMessage(jsonObject);
    }

    public interface Callback {
        void focusStatusChanged(String userId, boolean isFocus);
    }
}
