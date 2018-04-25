package com.android.xjq.controller.maincontroller;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.banana.commlib.bean.PaginatorBean;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.view.MyTabLayout;
import com.android.banana.pullrecycler.ilayoutmanager.MyLinearLayoutManager;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.recyclerview.DividerItemDecoration;
import com.android.banana.pullrecycler.recyclerview.PullRecycler;
import com.android.banana.pullrecycler.recyclerview.onRefreshListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.adapter.JcdxListAdapter;
import com.android.xjq.bean.SubjectComposeListBean;
import com.android.xjq.bean.SubjectTag;
import com.android.xjq.bean.SubjectsComposeBean;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zaozao on 2017/11/24.
 * 首页竞彩大学
 */

public class JcdxController extends BaseController4JCZJ<BaseActivity> implements HomeInterface<SubjectComposeListBean>, onRefreshListener {
    private MyTabLayout mTabLayout;

    private FrameLayout mContainer;
    private HomeHttpHelper mHelper;

    private PullRecycler mAllRecycler;
    private List<SubjectsComposeBean> mAllArtList = new ArrayList<>();
    private HashMap<String, List<SubjectTag>> mAllTagList = new HashMap<>();
    private HashMap<String, JczqDataBean> mRaceAllMap = new HashMap<>();//插入的足球,篮球赛集合

    private JcdxListAdapter mAllArtAdapter;
    private int mAllArtPager = 1;

    private PullRecycler mEliteRecycler;
    private List<SubjectsComposeBean> mEliteArtList = new ArrayList<>();
    private HashMap<String, List<SubjectTag>> mEliteTagList = new HashMap<>();
    private HashMap<String, JczqDataBean> mRaceEliteMap = new HashMap<>();
    private JcdxListAdapter mEliteArtAdapter;
    private int mEliteArtPager = 1;


    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.layout_home_jcdx);
    }

    @Override
    public void onSetUpView() {
        mTabLayout = findViewOfId(R.id.tabLayout);
        mContainer = findViewOfId(R.id.containerLayout);

        mTabLayout.addTabs(getString(R.string.all), getString(R.string.elite))
                .setTabSelectedListener(new MyTabLayout.TabSelectedListener() {
                    @Override
                    public void onTabSelected(MyTabLayout.Tab tab, boolean reSelected) {
                        if (reSelected)
                            return;
                        makePullRecyclerIfNeed(tab.getPosition());
                        doQueryOfCurrentTab(tab.getPosition(), true);
                    }
                });

    }

    @Override
    public void onSetUpData() {
        mHelper = new HomeHttpHelper(this);
        mTabLayout.setSelectTab(0);
    }

    @Override
    public void onRefresh(boolean refresh) {
        int tabPosition = mTabLayout.getSelectedTabPosition();
        if (refresh) {
            if (tabPosition == 0) {
                mAllArtPager = 1;
                mAllRecycler.setEnableLoadMore(true);
            } else {
                mEliteArtPager = 1;
                mEliteRecycler.setEnableLoadMore(true);
            }
        }
        if (refresh && tabPosition == 0)
            mHelper.queryJcdxArticleRecommandList(tabPosition == 0 ? mAllArtPager : mEliteArtPager, tabPosition != 0);
        else
            mHelper.queryJcdxArticleList(tabPosition == 0 ? mAllArtPager : mEliteArtPager, tabPosition != 0);
    }


    //点击对应tab 时 如果数据为空,那么才会发起请求

    private void doQueryOfCurrentTab(int position, boolean refresh) {

        if (refresh) {
            if (position == 0) {
                if (mAllArtList.size() != 0) {
                    return;
                } else {
                    mAllRecycler.setPostRefresh();
                }

            } else {
                if (mEliteArtList.size() != 0) {
                    return;
                } else {
                    mEliteRecycler.setPostRefresh();
                }
            }
        }
        if (position == 0) {
            mHelper.queryJcdxArticleRecommandList(position == 0 ? mAllArtPager : mEliteArtPager, position != 0);
        } else {
            mHelper.queryJcdxArticleList(position == 0 ? mAllArtPager : mEliteArtPager, position != 0);
        }

    }

    //初始化对应的tab位置列表view
    private void makePullRecyclerIfNeed(int tabPosition) {

        if (tabPosition == 0 && mAllRecycler != null) {
            showCurrentTabUI(tabPosition);
            return;
        }
        if (tabPosition != 0 && mEliteRecycler != null) {
            showCurrentTabUI(tabPosition);
            return;
        }
        PullRecycler recycler = null;
        if (tabPosition == 0) {
            mAllRecycler = new PullRecycler(context);
            recycler = mAllRecycler;
            mAllArtAdapter = new JcdxListAdapter(getContext(), mAllArtList, mAllTagList, 0, new MultiTypeSupport<SubjectsComposeBean>() {
                @Override
                public int getTypeLayoutRes(SubjectsComposeBean data, int pos) {
                    return data.getLayoutViewType(mRaceAllMap);
                }
            });
        } else {
            mEliteRecycler = new PullRecycler(getContext());
            recycler = mEliteRecycler;
            mEliteArtAdapter = new JcdxListAdapter(getContext(), mEliteArtList, mEliteTagList, 0, new MultiTypeSupport<SubjectsComposeBean>() {
                @Override
                public int getTypeLayoutRes(SubjectsComposeBean data, int pos) {
                    return data.getLayoutViewType(mRaceEliteMap);
                }
            });
        }
        recycler.setLayoutManger(new MyLinearLayoutManager(getContext()));
        recycler.setItemAnimator(null);
        recycler.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.base_divider_list_10dp, 1, true));
        recycler.setAdapter(tabPosition == 0 ? mAllArtAdapter : mEliteArtAdapter);
        recycler.setEnableLoadMore(true);
        recycler.setOnRefreshListener(this);
        mContainer.addView(recycler);
        showCurrentTabUI(tabPosition);
    }

    //显示当前对应tab位置的view
    private void showCurrentTabUI(int position) {
        for (int i = 0; i < mContainer.getChildCount(); i++) {
            if (position == i) {
                mContainer.getChildAt(i).setVisibility(View.VISIBLE);
            } else {
                mContainer.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }

    //更新精华tab列表
    private void updateEliteUI(SubjectComposeListBean composeListBean) {
        if (composeListBean == null || composeListBean.articleList == null || composeListBean.articleList.size() == 0) {
            if (mEliteArtAdapter.getItemCount() == 0)
                mEliteRecycler.showEmptyView();
            else
                mEliteRecycler.showOverLoadView();
            mEliteRecycler.refreshCompleted();
            return;
        }
        boolean refreshing = mEliteRecycler.isRefreshing();
        if (refreshing) {
            mEliteArtList.clear();
            mEliteTagList.clear();
            mRaceEliteMap.clear();
        }

        mEliteArtList.addAll(composeListBean.articleList);

        if (composeListBean.subjectTagMap != null)
            mEliteTagList.putAll(composeListBean.subjectTagMap);

        if (composeListBean.basketballRaceDataMap != null)
            mRaceEliteMap.putAll(composeListBean.basketballRaceDataMap);

        if (composeListBean.footballRaceDataMap != null)
            mRaceEliteMap.putAll(composeListBean.footballRaceDataMap);

        PaginatorBean paginator = composeListBean.paginator;
        if (paginator != null && paginator.getPage() >= paginator.getPages()) {
            mEliteRecycler.setEnableLoadMore(false);
            mEliteRecycler.showOverLoadView();
        }
        mEliteRecycler.refreshCompleted();
    }

    //更新全部tab列表
    private void updateAllArticleUI(SubjectComposeListBean composeListBean, boolean recommend) {
        if (composeListBean == null || composeListBean.articleList == null || composeListBean.articleList.size() == 0) {
            if (recommend)
                return;
            refreshAllArticleCompleted();
            return;
        }
        boolean refreshing = mAllRecycler.isRefreshing();
        if (refreshing) {
            mAllArtList.clear();
            mAllTagList.clear();
            mRaceAllMap.clear();
        }
        mAllArtList.addAll(composeListBean.articleList);

        if (composeListBean.subjectTagMap != null)
            mAllTagList.putAll(composeListBean.subjectTagMap);

        if (composeListBean.basketballRaceDataMap != null)
            mRaceAllMap.putAll(composeListBean.basketballRaceDataMap);

        if (composeListBean.footballRaceDataMap != null)
            mRaceAllMap.putAll(composeListBean.footballRaceDataMap);

        PaginatorBean paginator = composeListBean.paginator;
        if (paginator != null && paginator.getPage() >= paginator.getPages()) {
            mAllRecycler.setEnableLoadMore(false);
            mAllRecycler.showOverLoadView();
        }
        mAllRecycler.refreshCompleted();
    }

    private void refreshAllArticleCompleted() {
        mAllRecycler.showContentView();

        if (mAllArtAdapter.getItemCount() == 0)
            mAllRecycler.showEmptyView();
        else
            mAllRecycler.showOverLoadView();
        mAllRecycler.refreshCompleted();
    }

    private void refreshEliteArticleCompleted() {
        mEliteRecycler.showContentView();

        if (mEliteArtAdapter.getItemCount() == 0)
            mEliteRecycler.showEmptyView();
        else
            mEliteRecycler.showOverLoadView();
        mEliteRecycler.refreshCompleted();
    }

    @Override
    public void responseSuccessHttp(SubjectComposeListBean composeListBean, RequestContainer requestContainer) {
        XjqUrlEnum anEnum = (XjqUrlEnum) requestContainer.getRequestEnum();
        int tabPosition = mTabLayout.getSelectedTabPosition();
        if (anEnum == XjqUrlEnum.EDITOR_RECOMMEND) {
            updateUI(composeListBean, true);
            mHelper.queryJcdxArticleList(tabPosition == 0 ? mAllArtPager : mEliteArtPager, tabPosition != 0);
        } else if (anEnum == XjqUrlEnum.JCDX_ARTICLE) {
            updateUI(composeListBean, false);
            if (tabPosition == 0) {
                mAllArtPager++;
            } else {
                mEliteArtPager++;
            }
        }
    }

    void updateUI(SubjectComposeListBean composeListBean, boolean recommend) {
        int tabPosition = mTabLayout.getSelectedTabPosition();

        if (tabPosition == 0) {
            updateAllArticleUI(composeListBean, recommend);

        } else {
            updateEliteUI(composeListBean);
        }
    }

    @Override
    public void responseFalseHttp(JSONObject jo, RequestContainer requestContainer) {
        int tabPosition = mTabLayout.getSelectedTabPosition();
        XjqUrlEnum anEnum = (XjqUrlEnum) requestContainer.getRequestEnum();
        if (anEnum == XjqUrlEnum.EDITOR_RECOMMEND) {
            mHelper.queryJcdxArticleList(tabPosition == 0 ? mAllArtPager : mEliteArtPager, tabPosition != 0);
        } else if (anEnum == XjqUrlEnum.JCDX_ARTICLE) {

        }

        if (tabPosition == 0) {
            refreshAllArticleCompleted();
        } else {
            refreshEliteArticleCompleted();
        }
        try {
            getActivity().operateErrorResponseMessage(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
