package com.android.xjq.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.android.banana.commlib.view.MyTabLayout;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.activity.dynamic.MessageAndNotifyActivity;
import com.android.xjq.adapter.schduledatail.ViewpagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qiaomu on 2018/2/5.
 * <p>
 * 香蕉球二期首页
 */

public class HomeFragment2 extends BaseFragment {
    @BindView(R.id.tabLayout)
    MyTabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    List<Fragment> mFragments = new ArrayList<>();
    TabHomeFragment mAttentionFragment;
    TabHomeFragment mSuggestionFragment;

    TabHomeFragment currentTabFragment;

    @OnClick(R.id.messageIv)
    public void toMessage() {
        startActivity(new Intent(getActivity(), MessageAndNotifyActivity.class));
    }

    @OnClick(R.id.clickBtn)
    public void clickMe() {
        LiveActivity.startLiveActivity(getActivity(), 100785);
    }

    Unbinder unbinder;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_home2, null);
        fitsSystemWindows(view.findViewById(R.id.titleLayout));
        return view;
    }

    @Override
    protected void initData() {

        mAttentionFragment = TabHomeFragment.newInstance(false, true, false);
        mSuggestionFragment = TabHomeFragment.newInstance(true, false, false);

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.my_attention));
        titles.add(getString(R.string.my_suggestion));

        mFragments.add(mAttentionFragment);
        mFragments.add(mSuggestionFragment);
        mViewPager.setAdapter(new ViewpagerAdapter(mFragments, titles, getChildFragmentManager()));

        mTabLayout.setBottomLineColor(Color.TRANSPARENT);
        mTabLayout.setTabMargin(30)
                .setupWithViewPager(mViewPager)
                .setTabSelectedListener(new MyTabLayout.TabSelectedListener() {
                    @Override
                    public void onTabSelected(MyTabLayout.Tab tab, boolean reSelected) {
                        mViewPager.setCurrentItem(tab.getPosition());

                        if (tab.getPosition() == 1) {//切换到推荐
                            currentTabFragment = mSuggestionFragment;
                            mAttentionFragment.onStopAllVideoViewHolder();
                        } else {//切换到关注
                            currentTabFragment = mAttentionFragment;
                            mSuggestionFragment.onStopAllVideoViewHolder();
                        }
                    }
                }).setSelectTab(0);
    }

    @Override
    public void onPause() {
        currentTabFragment.onStopAllVideoViewHolder();
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            currentTabFragment.onStopAllVideoViewHolder();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) unbinder.unbind();
    }
}
