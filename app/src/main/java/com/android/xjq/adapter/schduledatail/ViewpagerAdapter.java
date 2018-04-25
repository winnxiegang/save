package com.android.xjq.adapter.schduledatail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaozao on 2018/2/1.
 * 用处：
 */

public class ViewpagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles;

    public ViewpagerAdapter(List<Fragment> fragments, FragmentManager fm) {
        this(fragments, null, fm);
    }

    public ViewpagerAdapter(List<Fragment> fragments, List<String> titles, FragmentManager fm) {
        super(fm);
        this.mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles == null ? null : mTitles.get(position);
    }
}
