package com.android.xjq.adapter.main;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Admin on 2017/3/7.
 */

public class CurrentAudienceAdapter extends PagerAdapter {
    private Context context;

    private List<View> mViews;

    public CurrentAudienceAdapter(Context context, List<View> mViews) {

        this.context = context;

        this.mViews = mViews;

    }

    public View getCurrentView(int position) {
        return mViews.get(position);
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
