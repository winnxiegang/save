package com.android.xjq.activity.playways;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.view.swipetab.SlidingTabLayout;
import com.android.banana.commlib.view.swipetab.listener.OnTabSelectListener;
import com.android.xjq.R;
import com.android.xjq.fragment.BaseListFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ajiao on 2018\1\26 0026.
 */

public class PlayWaysRecordActivity extends BaseActivity implements OnTabSelectListener {

    @BindView(R.id.backIv)
    ImageView backIv;

    @BindView(R.id.titleTv)
    TextView title;

    @BindView(R.id.sliding_tab_layout)
    SlidingTabLayout slidingTabLayout;

    @BindView(R.id.vp)
    ViewPager viewPager;
    private ArrayList<BaseListFragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "助威", "PK", "幸运抽奖", "极限手速", "圣旨到"
    };
    private MyPagerAdapter mAdapter;

    public static void startPlayWaysRecordActivity(Context from) {
        Intent intent = new Intent(from,PlayWaysRecordActivity.class);
        from.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_ways_record);
        ButterKnife.bind(this);
        backIv.setVisibility(View.VISIBLE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayWaysRecordActivity.this.finish();
            }
        });
        title.setText("玩法记录");
     /*   for (String title : mTitles) {
            PKFragment payWayFragment = new PKFragment();
            mFragments.add(payWayFragment);
        }*/
        PayWayFragment payWayFragment = new PayWayFragment();
        mFragments.add(payWayFragment);
        PKFragment pkFragment = new PKFragment();
        mFragments.add(pkFragment);
        RecordQueryFragment handSpeedFragment = RecordQueryFragment.newInstance(RecordQueryFragment.LUCKY_DRAW);
        mFragments.add(handSpeedFragment);
        RecordQueryFragment decreeFragment = RecordQueryFragment.newInstance(RecordQueryFragment.HAND_SPEED);
        mFragments.add(decreeFragment);
        RecordQueryFragment luckyDranFragment = RecordQueryFragment.newInstance(RecordQueryFragment.DECREE);
        mFragments.add(luckyDranFragment);

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public void onTabSelect(int position) {
        Toast.makeText(PlayWaysRecordActivity.this, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselect(int position) {
        Toast.makeText(PlayWaysRecordActivity.this, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

    }

}
