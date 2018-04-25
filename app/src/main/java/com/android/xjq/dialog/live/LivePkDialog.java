package com.android.xjq.dialog.live;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.android.banana.commlib.view.MyTabLayout;
import com.android.xjq.R;
import com.android.xjq.adapter.main.InfoPagerAdapter;
import com.android.xjq.controller.PkLiveController;
import com.android.xjq.dialog.base.DialogBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2018/2/26.
 */

public class LivePkDialog extends DialogBase {

    private MyTabLayout tabLayout;
    private ViewPager mViewPager;
    private ArrayList<String> titles;

    public LivePkDialog(Context context, int orientation) {
        super(context, R.layout.dialog_pk_live, R.style.dialog_base, orientation);
        initView();
    }

    @Override
    protected void onStart() {
    }

    private void initView() {
        tabLayout = (MyTabLayout) rootView.findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);

        titles = new ArrayList<>();
        titles.add(context.getString(R.string.current_pk));
        titles.add(context.getString(R.string.pk_record));
        //dialog中使用fragment中有问题,tabLayout会有计算不准位置
       /* PkFragment fragment1 = PkFragment.newInstance(true);
        PkFragment fragment2 = PkFragment.newInstance(false);
        mFragments.add(fragment1);
        mFragments.add(fragment2);
        mViewPager.setAdapter(new ViewpagerAdapter(mFragments, titles, ((BaseActivity) context).getSupportFragmentManager()));*/
        List<View> mViews = new ArrayList<>();
        final List<PkLiveController> pkControllers = new ArrayList<>();
        pkControllers.add(new PkLiveController(context, true));
        pkControllers.add(new PkLiveController(context, false));
        for (PkLiveController pkController : pkControllers) {
            mViews.add(pkController.getPkTabView());
        }
        mViewPager.setAdapter(new InfoPagerAdapter(mViews, titles));
        tabLayout.setTabMargin(50)
                .setupWithViewPager(mViewPager)
                .setTabSelectedListener(new MyTabLayout.TabSelectedListener() {
                    @Override
                    public void onTabSelected(MyTabLayout.Tab tab, boolean reSelected) {
                        mViewPager.setCurrentItem(tab.getPosition());
                        pkControllers.get(tab.getPosition()).onRefresh(true);
                    }
                });
    }

}
