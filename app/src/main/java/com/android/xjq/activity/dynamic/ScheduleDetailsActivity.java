package com.android.xjq.activity.dynamic;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.view.MyTabLayout;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.program.ProgramListFragment;
import com.android.xjq.adapter.schduledatail.ViewpagerAdapter;
import com.android.xjq.controller.live.LiveHeaderController;
import com.android.xjq.fragment.schdulefragment.ArticleAttitudeFragment;
import com.android.xjq.fragment.schdulefragment.DataFragment;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.view.NoScrollViewPager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleDetailsActivity extends BaseActivity4Jczj implements OnHttpResponseListener {

    public final static int POS_ARTICLE = 0;//文章
    public final static int POS_PROGRAM = 1;//节目
    public final static int POS_ATTITUDE = 2;//态度
    public final static int POS_DATA = 3;//数据

    @BindView(R.id.headerFrameLayout)
    FrameLayout headerFrameLayout;
    @BindView(R.id.myTabLayout)
    MyTabLayout myTabLayout;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;

    @OnClick(R.id.backIv)
    public void back() {
        finish();
    }

    private ViewpagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private ArticleAttitudeFragment articleFragment;
    private ArticleAttitudeFragment attitudeFragment;
    private ProgramListFragment programFragment;
    private DataFragment dataFragment;
    private HttpRequestHelper httpRequestHelper;
    private String raceId;
    private String raceType;
    private JczqDataBean jczqDataBean;
    private int currentSelectTab = POS_ARTICLE;


    private LiveHeaderController liveHeaderController;

    public static void startScheduleDetailsActivity(Context context, String raceId, String raceType) {
        startScheduleDetailsActivity(context, raceId, raceType, POS_ARTICLE);
    }

    public static void startScheduleDetailsActivity(Context context, String raceId, String raceType, int currentSelectTab) {
        Intent intent = new Intent();
        intent.putExtra("raceId", raceId);
        intent.putExtra("raceType", raceType);
        intent.putExtra("currentSelectTab", currentSelectTab);
        intent.setClass(context, ScheduleDetailsActivity.class);
        context.startActivity(intent);
    }


    //1
    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_schedule_details);
        ButterKnife.bind(this);
        setTitleBar(true, "");
        fitsSystemWindows(findViewById(R.id.titleLayout));
        raceId = getIntent().getStringExtra("raceId");
        raceType = getIntent().getStringExtra("raceType");
        currentSelectTab = getIntent().getIntExtra("currentSelectTab", 0);
        httpRequestHelper = new HttpRequestHelper(this, this);
        getHeaderDataRequest();
    }

    //2
    @Override
    protected void setUpView() {
        myTabLayout.addTabs("专栏", "节目", "态度", "数据")
                .setTabTextColors(getResources().getColor(R.color.text_black), getResources().getColor(R.color.main_red))
                .setTabTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                .setSelectedTabIndicatorColor(getResources().getColor(R.color.main_red))
                .setTabSelectedListener(new MyTabLayout.TabSelectedListener() {
                    @Override
                    public void onTabSelected(MyTabLayout.Tab tab, boolean reSelected) {
                        if (reSelected)
                            return;
                        viewPager.setCurrentItem(tab.getPosition(), false);
                    }
                });
        viewPager.setOffscreenPageLimit(4);
        liveHeaderController = new LiveHeaderController(this);
        liveHeaderController.init(headerFrameLayout);

        View childAt = headerFrameLayout.getChildAt(headerFrameLayout.getChildCount() - 1);
        headerFrameLayout.removeView(childAt);
        childAt.setPadding(childAt.getPaddingLeft(), childAt.getPaddingTop() + getStatusBarHeight(), childAt.getPaddingRight(), childAt.getPaddingBottom());
        headerFrameLayout.addView(childAt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        liveHeaderController.onDestroy();
    }

    //3
    @Override
    protected void setUpData() {

    }

    private void getHeaderDataRequest() {
        RequestFormBody map = new RequestFormBody(XjqUrlEnum.RACE_QUERY_BY_RACE_TYPE_AND_ID, true);
        map.put("raceId", raceId);
        map.put("raceType", raceType);
        httpRequestHelper.startRequest(map, true);
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        if (jsonObject.has("raceDataSimple")) {
            try {
                jczqDataBean = new Gson().fromJson(String.valueOf(jsonObject.getJSONObject("raceDataSimple")), JczqDataBean.class);
                liveHeaderController.setMatchData(jczqDataBean);
                if (jczqDataBean != null) {
                    articleFragment = ArticleAttitudeFragment.newInstance(true, raceId, raceType);
                    attitudeFragment = ArticleAttitudeFragment.newInstance(false, raceId, raceType);
                    dataFragment = DataFragment.newInstance(jczqDataBean, raceType);
                    programFragment = ProgramListFragment.newInstance(false, raceId, raceType, true);
                    mFragments.add(articleFragment);
                    mFragments.add(programFragment);
                    mFragments.add(attitudeFragment);
                    mFragments.add(dataFragment);
                    mAdapter = new ViewpagerAdapter(mFragments, getSupportFragmentManager());
                    viewPager.setAdapter(mAdapter);
                    myTabLayout.setSelectTab(currentSelectTab);
                    programFragment.setProgramType(ProgramListFragment.PROGRAM_TYPE_ALL);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }
}
