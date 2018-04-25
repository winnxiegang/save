package com.android.xjq.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.view.MedalLayout;
import com.android.banana.groupchat.bean.medal.GroupChatMedalBean;
import com.android.banana.groupchat.groupchat.GroupJoinListActivity;
import com.android.banana.groupchat.groupchat.GroupShirtDetailActivity;
import com.android.banana.groupchat.groupchat.widget.GroupShirtMedalDrawable;
import com.android.banana.view.LabelTextView;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.AddressModifyActivity;
import com.android.xjq.activity.PackageActivity;
import com.android.xjq.activity.RechargeActivity;
import com.android.xjq.activity.SettingActivity;
import com.android.xjq.activity.ThirdWebActivity;
import com.android.xjq.activity.homepage.HomePageActivity;
import com.android.xjq.activity.mall.MallActivity;
import com.android.xjq.activity.playways.PlayWaysRecordActivity;
import com.android.xjq.activity.userInfo.PasswordManagerActivity;
import com.android.xjq.activity.userInfo.UserInfoActivity;
import com.android.xjq.bean.UserMedalLevelBean;
import com.android.xjq.bean.userInfo.UserInfoClientBean;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhouyi on 2017/3/3.
 */

public class MeFragment extends BaseFragment implements IHttpResponseListener<UserInfoClientBean> {

    @BindView(R.id.headLayout)
    RelativeLayout headLayout;
    @BindView(R.id.portraitIv)
    ImageView portraitIv;
    @BindView(R.id.userNameTv)
    TextView userNameTv;
    @BindView(R.id.attentionTv)
    TextView attentionTv;
    @BindView(R.id.item_shirt)
    LabelTextView shirtView;
    @BindView(R.id.medal_layout)
    MedalLayout mMedalLayout;
    @BindView(R.id.shirt_iv)
    ImageView shirtIv;
    @BindView(R.id.contentView)
    ViewGroup contentView;

    private WrapperHttpHelper httpRequestHelper;

    @OnClick(R.id.item_shirt)
    public void myGroupShirt() {
        GroupShirtDetailActivity.startGroupShirtDetailActivity(activity);
    }

    @OnClick(R.id.changeAccountTv)
    public void myAccountTv() {
        UserInfoActivity.startUserInfoActivity4Self(getActivity(), LoginInfoHelper.getInstance().getUserId());
    }

    @OnClick(R.id.item_safepay)
    public void safePay() {
        PasswordManagerActivity.startPasswordManagerActivity(activity);
    }

    @OnClick(R.id.item_appstore)
    public void goToMyInfo() {
        MallActivity.startMallActivity(activity);
    }

    @OnClick(R.id.guide)
    public void settingCenter() {
        ThirdWebActivity.startThirdWebActivityByType(getActivity(), ThirdWebActivity.PLAY_GUIDANCE, LoginInfoHelper.getInstance().getUserId());
        //SettingActivity.startSettingActivity(activity);
    }

    @OnClick(R.id.rechargeLayout)
    public void recharge() {

        if (validateIsLogin())
            RechargeActivity.startRechargeActivity(activity);
    }

    @OnClick(R.id.walletLayout)
    public void myWallet() {
        if (validateIsLogin())
            PackageActivity.startActivity(getContext());
        //MyWalletActivity.startMyWalletActivity(activity);
    }

    @OnClick(R.id.item_group)
    public void onGroupItemClick() {
        GroupJoinListActivity.startGroupJoinListActivity(getContext());
    }

    @OnClick(R.id.item_achievements)
    public void onItemDomainClick() {
        ThirdWebActivity.startThirdWebActivityByType(getActivity(), ThirdWebActivity.MEDAL_QUERY, LoginInfoHelper.getInstance().getUserId());
    }

    @OnClick(R.id.item_play)
    public void onItemPlayClick() {
        PlayWaysRecordActivity.startPlayWaysRecordActivity(getContext());
    }

    @OnClick(R.id.item_setting)
    public void onSettingClick() {
        SettingActivity.startSettingActivity(getActivity());
    }

    @OnClick(R.id.item_address)
    public void onAddressClick() {
        AddressModifyActivity.startAddressModifyActivity4Result(getActivity());
    }

    @OnClick({R.id.userInfoLayout, R.id.portraitIv})
    public void onAttentionClick() {
        HomePageActivity.startHomepageActivity(getContext(), LoginInfoHelper.getInstance().getUserId());
    }


    @Override
    protected void initData() {

    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragemnt_me, null);
        ButterKnife.bind(this, view);
        httpRequestHelper = new WrapperHttpHelper(this);
        fitsSystemWindows(contentView);
        return view;
    }

    private void requestData() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.MY_USER_INFO_QUERY, true);
        httpRequestHelper.startRequest(map);
    }

    @Override
    public void onResume() {
        super.onResume();
        initUserInfo();
        requestData();
    }

    private void initUserInfo() {
        setLogo(portraitIv, LoginInfoHelper.getInstance().getUserLogoUrl());
        userNameTv.setText(LoginInfoHelper.getInstance().getNickName());
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            requestData();
        }
    }

    @Override
    public void onSuccess(RequestContainer request, UserInfoClientBean userInfoClientBean) {
        PicUtils.load(getContext(), portraitIv, userInfoClientBean.getUserInfo().getUserLogoUrl(), R.drawable.user_default_logo);

        userNameTv.setText(userInfoClientBean.getUserInfo().getLoginName());
        attentionTv.setVisibility(View.VISIBLE);
        attentionTv.setText("关注 " + userInfoClientBean.getMyFollowers() + "    |    粉丝 " + userInfoClientBean.getFollowMyUsers());

        mMedalLayout.removeAllViews();
        if (userInfoClientBean.getUserMedalLevelList() != null) {
            for (UserMedalLevelBean medalLevel : userInfoClientBean.getUserMedalLevelList()) {
                mMedalLayout.addMedal(SubjectMedalEnum.getMedalResourceId(getContext(), medalLevel.medalConfigCode, medalLevel.currentMedalLevelConfigCode));
            }
        }

        GroupChatMedalBean coatArmorDetail = userInfoClientBean.getCoatArmorDetail();
        if (coatArmorDetail == null) {

        } else if (coatArmorDetail.getMedalLabelConfigList() != null && coatArmorDetail.getMedalLabelConfigList().size() > 0) {
            GroupShirtMedalDrawable drawable = new GroupShirtMedalDrawable(coatArmorDetail.getMedalLabelConfigList().get(0).getContent(), getResources());
            shirtIv.setImageDrawable(drawable);
        } else {
            shirtIv.setImageResource(R.drawable.icon_group_shirt_no_txt_bg);
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        ((BaseActivity) activity).operateErrorResponseMessage(jsonObject);
    }
}
