package com.android.xjq.fragment;

import android.os.Bundle;
import android.view.View;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayout;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayoutDirection;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.bean.SubjectComposeListBean2;
import com.android.xjq.bean.SubjectsComposeBean2;
import com.android.xjq.utils.SubjectUtils2;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.singleVideo.SinglePlayCallback;
import com.android.xjq.utils.singleVideo.SinglePlayManager;
import com.android.xjq.utils.singleVideo.VideoViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.xjq.utils.XjqUrlEnum.HOT_TOPIC_SUBJECT_RECOMMEND;
import static com.android.xjq.utils.XjqUrlEnum.MY_TOPIC_QUERY;

/**
 * Created by qiaomu on 2018/2/5.
 * <p>
 * 首页 我的关注  和 推荐 页面,
 * 个人主页动态，事件，
 * 助威详情页：文章，态度
 */

public class TabHomeFragment extends BaseListFragment<SubjectsComposeBean2> implements IHttpResponseListener<SubjectComposeListBean2>, SinglePlayCallback {

    private boolean isAttention;//首页动态和个人主页动态-----区分是否显示我关注的
    private boolean isHomePageEvent;//是不是个人主页事件
    public boolean isHomeRecommend;//是否是首页推荐
    private String userId = "";

    private boolean isRefresh = true;

    public WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);

    public SwipyRefreshLayout.OnRefreshListener onRefreshListener;
    private SinglePlayManager mSinglePlayManager = new SinglePlayManager();

    private ArrayList<SubjectsComposeBean2> hotTopicList = new ArrayList<>();

    public void setOnRefreshListener(SwipyRefreshLayout.OnRefreshListener listener) {
        this.onRefreshListener = listener;
    }

    public static TabHomeFragment newInstance(boolean isHomeRecommend, boolean isAttention, boolean isHomePageEvent) {
        return newInstance(isHomeRecommend, isAttention, isHomePageEvent, LoginInfoHelper.getInstance().getUserId());
    }


    public static TabHomeFragment newInstance(boolean isHomeRecommend, boolean isAttention, boolean isHomePageEvent,
                                              String userId) {
        Bundle args = new Bundle();
        args.putBoolean("isAttention", isAttention);
        args.putBoolean("isHomePageEvent", isHomePageEvent);
        args.putBoolean("isHomeRecommend", isHomeRecommend);
        args.putString("userId", userId);
        TabHomeFragment fragment = new TabHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewLayoutRes() {
        return R.layout.fragment_home2_tab;
    }

    @Override
    public MultiTypeSupport getSupportMultiType() {
        return new MultiTypeSupport<SubjectsComposeBean2>() {
            @Override
            public int getTypeLayoutRes(SubjectsComposeBean2 composeBean2, int pos) {
                return composeBean2.getLayoutViewType();
            }
        };
    }


    @Override
    protected void onSetUpView() {
        setLoadMoreEnable(true);
        mRecycler.setEmptyView(R.drawable.ic_empty_dynamic, getString(R.string.no_data), null);

        mSinglePlayManager.attach(mRecycler.getRecyclerView(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        isAttention = getArguments().getBoolean("isAttention");
        isHomePageEvent = getArguments().getBoolean("isHomePageEvent");
        isHomeRecommend = getArguments().getBoolean("isHomeRecommend");
        userId = getArguments().getString("userId");
    }



    @Override
    public void onBindHolder(ViewHolder holder, SubjectsComposeBean2 composeBean2, int position) {
        LogUtils.e("kk","onBindHolder");
        SubjectUtils2.bindViewHolder(((BaseActivity) getActivity()), holder, composeBean2, position, this);
    }


    //区分跳转
    @Override
    public void onItemClick(View view, int position) {
        SubjectsComposeBean2 bean2 = mDatas.get(position);
        SubjectUtils2.onItemClick(getActivity(), bean2);
    }

    @Override
    public void onRefresh(boolean refresh) {
        mSinglePlayManager.stopAllVideoHolder();

        isRefresh = refresh;
        if (onRefreshListener != null && refresh) {
            onRefreshListener.onRefresh(SwipyRefreshLayoutDirection.TOP);
        }
        if (isHomeRecommend) {//首页推荐
            RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.INDEX_CONTENT_RECOMMEND, true);
            formBody.put("currentPage", mCurPage);
            formBody.put("userId", userId);
            mHttpHelper.startRequest(formBody);
        } else if (isHomePageEvent) {//个人主页事件
            RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.BEHAVIOR_GENERATE_EVENT_QUERY, true);
            formBody.put("currentPage", mCurPage);
            formBody.put("userId", userId);
            mHttpHelper.startRequest(formBody);

        } else {//两个地方用一个接口-------(首页我的关注showFollowers=true，个人主页动态showFollowers = false)
            if (refresh && !isAttention) {//只有个人主页动态，刷新的时候请求热门动态
                getHotTopicRequest();
            } else {
                getDynamicDataRequest();
            }
        }
    }

    private void getDynamicDataRequest() {
        RequestFormBody formBody = new RequestFormBody(MY_TOPIC_QUERY, true);
        formBody.put("currentPage", mCurPage);
        formBody.put("showFollowers", isAttention);//-----区别
        formBody.put("userId", userId);
        mHttpHelper.startRequest(formBody);
    }

    private void getHotTopicRequest() {
        RequestFormBody formBody = new RequestFormBody(HOT_TOPIC_SUBJECT_RECOMMEND, true);
        formBody.put("userId", userId);
        mHttpHelper.startRequest(formBody);
    }

    @Override
    public void onSuccess(RequestContainer request, SubjectComposeListBean2 composeListBean2) {
        mPaginator = composeListBean2 == null ? null : composeListBean2.paginator;
        if(composeListBean2!=null){
            XjqUrlEnum urlEnum = (XjqUrlEnum) request.getRequestEnum();
            switch (urlEnum) {
                case HOT_TOPIC_SUBJECT_RECOMMEND:
                    if (isRefresh) {
                        hotTopicList.clear();
                        if (composeListBean2.topicSimpleList != null && composeListBean2.topicSimpleList.size() > 0) {
                            hotTopicList.addAll(composeListBean2.topicSimpleList);
                            hotTopicList.get(0).showHotIcon = true;
                        }
                    }
                    getDynamicDataRequest();
                    break;
                case MY_TOPIC_QUERY:
                    if (!isAttention && composeListBean2.topicSimpleList != null && composeListBean2.topicSimpleList.size() > 0) {
                        if (isRefresh) {

                            composeListBean2.topicSimpleList.get(0).showNewsDynamicIcon = true;
                            composeListBean2.topicSimpleList.addAll(0, hotTopicList);
                        }
                        for (int i = 0; i < composeListBean2.topicSimpleList.size(); i++) {
                            composeListBean2.topicSimpleList.get(i).inChannelArea = false;
                        }

                    }
                    loadCompleted(composeListBean2.topicSimpleList);
                    break;
                case BEHAVIOR_GENERATE_EVENT_QUERY:
                    if (composeListBean2.topicSimpleList != null && composeListBean2.topicSimpleList.size() > 0) {
                        for (int i = 0; i < composeListBean2.topicSimpleList.size(); i++) {
                            composeListBean2.topicSimpleList.get(i).inChannelArea = false;
                        }
                    }
                default:
                    loadCompleted(composeListBean2 == null ? null : composeListBean2.topicSimpleList);
                    break;
            }
        }else{
            loadCompleted(null);
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        loadCompleted(null);
        ((BaseActivity) getActivity()).operateErrorResponseMessage(jsonObject);
    }

    @Override
    public void onPlayStatusChanged(VideoViewHolder videoViewHolder, int playStatus) {

        mSinglePlayManager.onPlayStatusChanged(videoViewHolder, playStatus);
    }

    public void onStopAllVideoViewHolder() {
        mSinglePlayManager.stopAllVideoHolder();
        LogUtils.e("kk","关闭");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSinglePlayManager.stopAllVideoHolder();
    }

}
