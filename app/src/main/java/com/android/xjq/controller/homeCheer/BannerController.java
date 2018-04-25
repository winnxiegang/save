package com.android.xjq.controller.homeCheer;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.xjq.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyi on 2018/4/19.
 */

//Banner条
public class BannerController {

    private Context mContext;

    private List<View> list = new ArrayList<>();

    public BannerController(Context context) {
        this.mContext = context;
    }

    public View getView() {

        initPagerView();

        FrameLayout frameLayout = new FrameLayout(mContext);
        FrameLayout.LayoutParams llps = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        frameLayout.setLayoutParams(llps);

        ViewPager viewPager = new ViewPager(mContext);
        llps = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(100));
        viewPager.setLayoutParams(llps);
        frameLayout.addView(viewPager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view = list.get(position);
                container.removeView(view);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = list.get(position);
                container.addView(view);
                return view;
            }
        });

        CirclePageIndicator indicator = new CirclePageIndicator(mContext);
        indicator.setFillColor(mContext.getResources().getColor(R.color.main_red));
        indicator.setPageColor(mContext.getResources().getColor(R.color.gray));
        indicator.setStrokeColor(mContext.getResources().getColor(R.color.gray));
        llps = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llps.gravity = Gravity.BOTTOM | Gravity.CENTER;

        indicator.setPadding(0, 10, 0, 10);
        indicator.setLayoutParams(llps);
        indicator.setViewPager(viewPager);
        frameLayout.addView(indicator);
        return frameLayout;
    }

    private void initPagerView() {
        list.clear();
        for (int i = 0; i < 4; i++) {

            LinearLayout linearLayout = getLinearLayout();
            linearLayout.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.blue_600));

            TextView view = new TextView(mContext);
            LinearLayout.LayoutParams llps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setGravity(Gravity.CENTER);
            view.setLayoutParams(llps);
            view.setText("Hello World");
            linearLayout.addView(view);

            list.add(linearLayout);

        }

    }

    private LinearLayout getLinearLayout() {
        LinearLayout linearLayout = new LinearLayout(mContext);
        ViewPager.LayoutParams llps = new ViewPager.LayoutParams();

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(llps);
        return linearLayout;
    }

    public int dp2px(float dipValue) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return (int) (density * dipValue + 0.5f);
    }
}
