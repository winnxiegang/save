package com.android.xjq.adapter.main;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lingjiu on 2017/3/7.
 */

public class InfoPagerAdapter extends PagerAdapter {

    private List<View> mViews;
    private List<String> titles;

    public InfoPagerAdapter(List<View> mViews) {

        this.mViews = mViews;

    }

    public InfoPagerAdapter(List<View> mViews, List<String> titles) {
        this.mViews = mViews;
        this.titles = titles;
    }

    public View getCurrentView(int position) {
        return mViews.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles == null ? super.getPageTitle(position) : titles.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(mViews.get(position));

        return mViews.get(position);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }
}
