package com.android.xjq.controller.maincontroller;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.banana.commlib.bean.PaginatorBean;
import com.android.banana.commlib.view.BannerView;
import com.android.banana.pullrecycler.ilayoutmanager.MyLinearLayoutManager;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.recyclerview.DividerItemDecoration;
import com.android.banana.pullrecycler.recyclerview.OnScrolledYListener;
import com.android.banana.pullrecycler.recyclerview.PullRecycler;
import com.android.banana.pullrecycler.recyclerview.onRefreshListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.MainActivity;
import com.android.xjq.activity.NewsDetailActivity;
import com.android.xjq.bean.CmsInfoListBean;
import com.android.xjq.bean.NewsInfoBean;
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
 * 首页推荐
 */

public class RecommendController extends BaseController4JCZJ<MainActivity> implements HomeInterface<Object>,
        onRefreshListener, GroupChatMessageController.MessageChangedListener, BannerView.onClickListener {
    private HomeHttpHelper mHttpHelper;
    private PullRecycler mRecycler;
    private SubjectMultiAdapter mMultiAdapter;
    private int mCurPager = 1;
    private ArrayList<SubjectsComposeBean> mSubjectList = new ArrayList<>();
    private HashMap<String, List<SubjectTag>> subjectTagMap = new HashMap<>();

    private BannerView bannerView;
    private GroupChatMessageController mMessageController;//新版群聊列表
    private List<NewsInfoBean.NewsInfo> infoList;//轮播图
    private DividerItemDecoration dividerItemDecoration;

    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.layout_home_recommend);
    }

    @Override
    public void onSetUpView() {
        mRecycler = findViewOfId(R.id.pullRecycler);
        mMessageController = new GroupChatMessageController(this);
        mMessageController.setContentView(mRecycler);

        mMultiAdapter = new SubjectMultiAdapter(getContext(), mSubjectList, 0, new MultiTypeSupport<SubjectsComposeBean>() {
            @Override
            public int getTypeLayoutRes(SubjectsComposeBean composeBean, int pos) {
                return composeBean.getLayoutViewType(null);
            }
        });
        mRecycler.setLayoutManger(new MyLinearLayoutManager(getContext()));
        mRecycler.setItemAnimator(null);
        dividerItemDecoration = new DividerItemDecoration(getContext(), R.drawable.base_divider_list_10dp, 1, false);
        mRecycler.addItemDecoration(dividerItemDecoration);
        mRecycler.setAdapter(mMultiAdapter);
        mRecycler.setEnableLoadMore(true);
        mRecycler.setOnRefreshListener(this);

        bannerView = new BannerView(getContext());
        bannerView.setOnBannerClickListener(this);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.icon_room_cover);
        drawable.setAlpha(150);
        bannerView.setTipBackgroundDrawable(drawable);
        ((ViewGroup) mMessageController.getContentView()).addView(bannerView, 0);
        mRecycler.addHeaderView(mMessageController.getContentView());

        mHttpHelper = new HomeHttpHelper(this);

    }

    public void setOnScrolledYListener(OnScrolledYListener onScrolledYListener) {
        mRecycler.setOnScrollerYListener(onScrolledYListener);
    }

    @Override
    public void onSetUpData() {
        mHttpHelper.queryBannerInfo();
        mHttpHelper.queryTopicRefresh(mCurPager);
    }

    @Override
    public void onRefresh(final boolean refresh) {
        if (refresh) {
            mCurPager = 1;
            mRecycler.setEnableLoadMore(true);
            onSetUpData();
        } else {
            mHttpHelper.queryTopicRefresh(mCurPager);
        }
    }

    @Override
    public void responseSuccessHttp(Object object, RequestContainer request) {
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        mRecycler.showContentView();
        switch (requestEnum) {
            case CMS_INFO_QUERY:
                setUpBannerView((CmsInfoListBean) object);
                break;
            case FRESH_TOPIC:
                setupRefreshTopic((SubjectComposeListBean) object);
        }
    }


    private void setupRefreshTopic(SubjectComposeListBean composeListBean) {
        boolean refreshing = mRecycler.isRefreshing();
        if (composeListBean == null || composeListBean.subjectList == null || composeListBean.subjectList.size() == 0) {
            mRecycler.showOverLoadView();
            mRecycler.refreshCompleted();
            return;
        }
        mCurPager++;
        if (refreshing) {
            mSubjectList.clear();
            subjectTagMap.clear();
        }
        mSubjectList.addAll(composeListBean.subjectList);
        if (composeListBean.subjectTagMap != null)
            subjectTagMap.putAll(composeListBean.subjectTagMap);

        PaginatorBean paginator = composeListBean.paginator;
        if (paginator != null && paginator.getPage() >= paginator.getPages()) {
            mRecycler.setEnableLoadMore(false);
            mRecycler.showOverLoadView();
        }
        mMultiAdapter.setSubjectTagMap(subjectTagMap);
        mRecycler.refreshCompleted();
    }

    //展现轮播图
    private void setUpBannerView(CmsInfoListBean object) {
        CmsInfoListBean cmsList = object;
        if (cmsList == null || cmsList.infoList == null || cmsList.infoList.size() == 0)
            return;
        infoList = cmsList.infoList;
        List<String> urlList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        for (int i = 0; i < infoList.size(); i++) {
            urlList.add(infoList.get(i).imageUrl);
            titleList.add(infoList.get(i).title);
        }

        bannerView.setBannerUrls(urlList, titleList);

    }

    @Override
    public void onBannerClick(int position) {
        NewsDetailActivity.startNewsDetailActivity(getContext(), String.valueOf(infoList.get(position).id));
    }

    @Override
    public void responseFalseHttp(JSONObject jo, RequestContainer request) {
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (requestEnum) {
            case FRESH_TOPIC:
                mRecycler.refreshCompleted();
                break;
        }
        try {
            if (mMultiAdapter.getItemCount() == 0 && mRecycler.getHeaders() != null && mRecycler.getHeaders().size() == 0)
                mRecycler.showEmptyView();
            else {
                mRecycler.showOverLoadView();
            }
            mRecycler.refreshCompleted();
            getActivity().operateErrorResponseMessage(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        bannerView.onStop();
        mMessageController.onPause();
    }

    @Override
    public void onResume() {
        bannerView.onResume();
        mMessageController.onResume();
    }

    @Override
    public void onDestroy() {
        bannerView.onStop();
        mMessageController.onDestroy();
    }

    @Override
    public void onMsgCountChanged(int count) {
        getActivity().onMsgCountChanged(-1, count);
    }


}