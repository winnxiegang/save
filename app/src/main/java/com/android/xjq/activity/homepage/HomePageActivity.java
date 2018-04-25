package com.android.xjq.activity.homepage;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.utils.picasso.PicassoLoadCallback;
import com.android.banana.commlib.view.MedalLayout;
import com.android.banana.commlib.view.MyTabLayout;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayout;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayoutDirection;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.groupchat.chat.SimpleChatActivity;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.FansFollowListActivity;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.activity.ThirdWebActivity;
import com.android.xjq.adapter.schduledatail.ViewpagerAdapter;
import com.android.xjq.bean.UserMedalLevelBean;
import com.android.xjq.bean.homepage.UserInfoData;
import com.android.xjq.fragment.TabHomeFragment;
import com.android.xjq.utils.SocialTools;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.live.BitmapBlurHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zaozao on 2018/01/25
 */
public class HomePageActivity extends BaseActivity4Jczj implements IHttpResponseListener<UserInfoData>, SwipyRefreshLayout.OnRefreshListener {
    @BindView(R.id.userNameTv)
    TextView userNameTv;
    @BindView(R.id.photoIv)
    CircleImageView photoIv;
    @BindView(R.id.simpleChatTv)
    TextView simpleChatTv;
    @BindView(R.id.tabLayout)
    MyTabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.livingIv)
    ImageView livingIv;
    @BindView(R.id.attentionCountTv)
    TextView attentionCountTv;
    @BindView(R.id.fansCountTv)
    TextView fansCountTv;
    @BindView(R.id.attentionTv)
    CheckedTextView attentionTv;
    @BindView(R.id.headLayout)
    FrameLayout headLayout;
    @BindView(R.id.medalLayout)
    MedalLayout medalLayout;


    @OnClick(R.id.toAttentionLayout)
    public void toAttention() {
        FansFollowListActivity.start2FansFollowListActivity(this, FansFollowListActivity.OPERATE_FOLLOW, userId);
    }

    @OnClick(R.id.toFansLayout)
    public void toFans() {
        FansFollowListActivity.start2FansFollowListActivity(this, FansFollowListActivity.OPERATE_FANS, userId);
    }

    @OnClick(R.id.backIv)
    public void back() {
        finish();
    }

    List<Fragment> mFragments = new ArrayList<>();
    TabHomeFragment dynamicFragment;
    TabHomeFragment eventFragment;

    private WrapperHttpHelper httpRequestHelper;

    private String userId;

    private boolean haveFollow;

    public static void startHomepageActivity(Context activity, String userId) {
        Intent intent = new Intent();
        intent.putExtra("userId", userId);
        intent.setClass(activity, HomePageActivity.class);
        activity.startActivity(intent);
    }

    //step 1
    @Override
    protected void setUpContentView() {

        setContentView(R.layout.at_home_page);

        ButterKnife.bind(this);

        setTitleBar(true, "");
        fitsSystemWindows(findViewById(R.id.titleLayout));

        userId = getIntent().getStringExtra("userId");
    }


    //step 2
    @Override
    protected void setUpView() {
        dynamicFragment = TabHomeFragment.newInstance(false, false,
                false, userId);
        eventFragment = TabHomeFragment.newInstance(false, false,
                true, userId);
        dynamicFragment.setOnRefreshListener(this);
        mFragments.add(dynamicFragment);
        mFragments.add(eventFragment);
        List<String> titles = new ArrayList<>();
        titles.add("动态");
        titles.add("事件");
        viewPager.setAdapter(new ViewpagerAdapter(mFragments, titles, getSupportFragmentManager()));
        tabLayout.setBottomLineColor(Color.TRANSPARENT)
                .setTabMargin(70)
                .setTabTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                .setTabTextColors(getResources().getColor(R.color.cashier_text_black), getResources().getColor(R.color.main_red))
                .setupWithViewPager(viewPager)
                .setTabSelectedListener(new MyTabLayout.TabSelectedListener() {
                    @Override
                    public void onTabSelected(MyTabLayout.Tab tab, boolean reSelected) {
                        if (reSelected)
                            return;
                        viewPager.setCurrentItem(tab.getPosition());
                        if (tab.getPosition() != 0) {
                            //从【动态】切换到【事件】TAB如果有播放的视频 要停止他们的
                            dynamicFragment.onStopAllVideoViewHolder();
                        }
                    }
                }).setSelectTab(0);

        viewPager.setOffscreenPageLimit(4);


        attentionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (haveFollow) {
                    SocialTools.cancelAttention(userId, "FOLLOWCANCEL", onSocialCallback);
                } else {
                    SocialTools.payAttention(userId, onSocialCallback);
                }
            }
        });
    }

    SocialTools.onSocialCallback onSocialCallback = new SocialTools.onSocialCallback() {
        @Override
        public void onResponse(JSONObject response, boolean success) {
            if (success) {
                haveFollow = !haveFollow;
                setFollowView();
            } else {
                try {
                    operateErrorResponseMessage(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void setFollowView() {
        if (haveFollow) {
            attentionTv.setChecked(true);
        } else {
            attentionTv.setChecked(false);
        }
    }

    //step 3
    @Override
    protected void setUpData() {
        httpRequestHelper = new WrapperHttpHelper(this);
    }


    @Override
    public void onSuccess(RequestContainer request, final UserInfoData userInfoData) {

        PicUtils.loadWithBitmap(this, userInfoData.getUserLogoUrl(), new PicassoLoadCallback() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap) {
                if (bitmap == null)
                    return;
                Bitmap blur = BitmapBlurHelper.doBlur(HomePageActivity.this, bitmap, 4);
                headLayout.setBackground(new BitmapDrawable(blur));
                photoIv.setImageBitmap(bitmap);
            }
        });

        userNameTv.setText(userInfoData.getLoginName());

        attentionCountTv.setText(userInfoData.getUserAttentionNum());

        fansCountTv.setText(userInfoData.getUserFollowsNum());

        if (userInfoData.isCanInitiatePrivateChat()) {
            simpleChatTv.setVisibility(View.VISIBLE);
            simpleChatTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleChatActivity.startSimpleChatActivity(HomePageActivity.this, userInfoData.getUserId(), userInfoData.getLoginName(), "");
                }
            });
        } else {
            simpleChatTv.setVisibility(View.GONE);
        }


        if (userInfoData.inChannelArea) {
            livingIv.setVisibility(View.VISIBLE);
            livingIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LiveActivity.startLiveActivity(HomePageActivity.this, userInfoData.channelAreaId);
                }
            });
        } else {
            livingIv.setVisibility(View.GONE);
        }

        if (userInfoData.getUserId().equals(LoginInfoHelper.getInstance().getUserId())) {
            attentionTv.setVisibility(View.GONE);
        }


        haveFollow = userInfoData.isFollow();

        setFollowView();

        medalLayout.removeAllViews();

        /**
         * GRADE_MEDAL("等级勋章",1),

         FIELD_MEDAL("领域勋章",2),

         ACHIEVE_MEDAL("成就勋章",3)
         */
        for (final UserMedalLevelBean medalLevel : userInfoData.userMedalLevelList) {
            medalLayout.addMedal(SubjectMedalEnum.getMedalResourceId(this,
                    medalLevel.medalConfigCode, medalLevel.currentMedalLevelConfigCode), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.equals(medalLevel.medalType.getName(), "FIELD_MEDAL")) {//领域标签
                        ThirdWebActivity.startThirdWebActivityByType(HomePageActivity.this, ThirdWebActivity.USER_SPECIAL_MEDAL_QUERY, userId);
                    } else if (TextUtils.equals(medalLevel.medalType.getName(), "ACHIEVE_MEDAL")) {//等级标签
                        ThirdWebActivity.startThirdWebActivityByType(HomePageActivity.this, ThirdWebActivity.MEDAL_QUERY, userId);
                    } else {
                        return;
                    }
                }
            });
        }

    }


    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {

    }

    //刷新的时候头部数据也需要刷新
    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        RequestFormBody requestFormBody = new RequestFormBody(XjqUrlEnum.USER_INFO_QUERY, true);
        requestFormBody.put("userId", userId);
        httpRequestHelper.startRequest(requestFormBody);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //从【动态】切换到【事件】TAB如果有播放的视频 要停止他们的
        dynamicFragment.onStopAllVideoViewHolder();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //从【动态】切换到【事件】TAB如果有播放的视频 要停止他们的
        dynamicFragment.onStopAllVideoViewHolder();
    }
}
