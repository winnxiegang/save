package com.android.xjq.activity.program;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.android.banana.commlib.view.MyTabLayout;
import com.android.xjq.R;
import com.android.xjq.adapter.schduledatail.ViewpagerAdapter;
import com.android.xjq.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ajiao on 2018\1\30 0030.
 */

public class ProgramFragment extends BaseFragment {

    @BindView(R.id.sliding_tab_layout)
    MyTabLayout slidingTabLayout;

    @BindView(R.id.vp)
    ViewPager viewPager;

    private final String[] mTitles = new String[]{"直播中", "全部", "已预约"};
    private final int[] mTypeTags = {ProgramListFragment.PROGRAM_TYPE_LIVE, ProgramListFragment.PROGRAM_TYPE_ALL, ProgramListFragment.PROGRAM_TYPE_RESERVED};
    private ArrayList<Fragment> mFragments;

    protected void initData() {

    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_program, null, false);
        ButterKnife.bind(this, view);
        fitsSystemWindows(view.findViewById(R.id.title_tab_lay));
        addFragment();
        initSlidingLay();
        return view;
    }

    private void addFragment() {
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            ProgramListFragment payWayFragment = null;
            int type = mTypeTags[i];
            if (i == ProgramListFragment.PROGRAM_TYPE_ALL) {
                payWayFragment = ProgramListFragment.newInstance(true, true, type);
            } else {
                payWayFragment = ProgramListFragment.newInstance(true, true, type);
            }
            mFragments.add(payWayFragment);
        }
    }

    private void initSlidingLay() {
       /* slidingTabLayout.setViewPager(viewPager, mTitles, getActivity(), mFragments);

        slidingTabLayout.setIndicatorMargin(0, 0, 0, 5);
        slidingTabLayout.setIndicatorWidth(0);
        slidingTabLayout.setIndicatorHeight(0);*/

        List<String> list = new ArrayList<>();
        for (String s : mTitles) list.add(s);
        viewPager.setAdapter(new ViewpagerAdapter(mFragments, list, getChildFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int type = mTypeTags[position];
                ProgramListFragment programListFragment = (ProgramListFragment) mFragments.get(position);
                programListFragment.setProgramType(type);
                programListFragment.requestProgramData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        slidingTabLayout.setTabTextColors(getResources().getColor(R.color.tab_selected), getResources().getColor(R.color.white));

        slidingTabLayout.setupWithViewPager(viewPager);

    }


}
