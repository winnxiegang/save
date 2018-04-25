package com.android.xjq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.DetailsHtmlShowUtils;
import com.android.banana.commlib.utils.RefreshEmptyViewHelper;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.view.ScrollListView;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.adapter.comment.CommentAdapter;
import com.android.xjq.bean.comment.UserCommentBean;
import com.android.xjq.bean.news.NewsDetailsBean;
import com.android.xjq.model.comment.CommentOrderByEnum;
import com.android.xjq.model.comment.CommentTypeEnum;
import com.android.xjq.model.comment.ObjectTypeEnum;
import com.android.xjq.utils.SharePopUpWindowHelper;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.details.DetailBottomViewUtils;
import com.android.xjq.utils.details.DetailsWebViewShowUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetailActivity extends BaseActivity implements IHttpResponseListener {

    @BindView(R.id.refreshListView)
    ScrollListView mListView;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.emptyIv)
    ImageView emptyIv;
    @BindView(R.id.shareIv)
    ImageView shareIv;

    @BindView(R.id.emptyTv)
    TextView emptyTipTv;
    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;

    @BindView(R.id.mainContentLayout)
    LinearLayout mainContentLayout;

    @OnClick(R.id.shareIv)
    public void share(){
        if(builder==null){
            //分享
            builder=  new SharePopUpWindowHelper.Builder(this)
                    .setLandscape(false)
                    .setShareUrl(bean.getShareUrl())
                    .setHostImageUrl(null)
                    .setShowTitle(false)
                    .setObjectType(ObjectTypeEnum.CMS_NEWS)
                    .setContent(bean.getInfoClientSimple().getSummary())
                    .setTitle(bean.getInfoClientSimple().getTitle());
        }
            builder.builder().show();


    }

    private SharePopUpWindowHelper.Builder builder;
    private CommentOrderByEnum mOrderBy = CommentOrderByEnum.SET_TOP_AND_GMT_DESC;

    private String infoId;

    private WrapperHttpHelper httpRequestHelper;

    private ViewHolder holder;

    private SecondViewHolder secondViewHolder;

    private List<UserCommentBean.CommentListBean> commentList = new ArrayList<>();

    private CommentAdapter mAdapter;

    private String mNewsContent;

    private DetailBottomViewUtils detailBottomViewUtil;

    /**
     * 第一次加载
     */
    private boolean mFirstLoad = true;

    private NewsDetailsBean bean;

    public static void startNewsDetailActivity(Context from, String newsId) {

        Intent intent = new Intent(from,NewsDetailActivity.class);

        intent.putExtra("infoId", newsId);

        from.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_details);

        ButterKnife.bind(this);

        setTitleBar(true, "资讯详情",null);

        infoId = getIntent().getStringExtra("infoId");

        initView();

        getNewsInfo();


    }

    private void initView() {

        httpRequestHelper = new WrapperHttpHelper(this);

        detailBottomViewUtil = new DetailBottomViewUtils();

        setRefreshLayout();

        mListView.addHeaderView(getHeaderView());

        mListView.addHeaderView(getSecondHeadView());

        mAdapter = new CommentAdapter(this, commentList);

        mListView.setAdapter(mAdapter);

        emptyIv.setImageResource(R.drawable.icon_no_content_about_match_schedule_detail);

    }

    private void getNewsInfo() {

        RequestFormBody map = new RequestFormBody(XjqUrlEnum.CMS_INFO_DETAIL, true);

        map.put("infoId", infoId);

        httpRequestHelper.startRequest(map,true);
    }

    private void getComment() {

        RequestFormBody map = new RequestFormBody(XjqUrlEnum.COMMENT_QUERY, true);

        map.put("objectId", infoId);

        map.put("objectType", CommentTypeEnum.CMS_NEWS.name());

        map.put("currentPage", String.valueOf(currentPage));

        map.put("orderBy", mOrderBy.name());

        httpRequestHelper.startRequest(map);

    }


    /**
     * 设置刷新布局
     */
    private void setRefreshLayout() {

        mRefreshEmptyViewHelper = new RefreshEmptyViewHelper(this, new RefreshEmptyViewHelper.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mRequestType = REFRESH;

                currentPage = DEFAULT_PAGE;

                getNewsInfo();
            }

            @Override
            public void onLoadMore() {

                mRequestType = LOAD_MORE;

                if (currentPage >= maxPages) {
                    ToastUtil.showLong(XjqApplication.getContext(),"已经到最后一页了!!");

                    mRefreshEmptyViewHelper.closeRefresh();

                } else {

                    currentPage++;

                    getComment();
                }
            }

            @Override
            public void onEmptyRefresh() {

            }
        }, getResources().getDrawable(com.android.banana.R.drawable.icon_no_details), "暂无回复");
    }

    private View getHeaderView() {

        View view = getLayoutInflater().inflate(R.layout.layout_news_detail_header, null);

        holder = new ViewHolder(view);

        WebView webView = new WebView(getApplicationContext());

        webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dp80)));

        holder.mainLayout.addView(webView, 1);

        holder.webView = webView;

        DetailsWebViewShowUtils.setWebView(NewsDetailActivity.this,holder.webView);

        return view;

    }
    private View getSecondHeadView() {
        //评论头部
        View secondHeaderView = getLayoutInflater().inflate(R.layout.layout_all_comment_top, null);

        secondViewHolder = new SecondViewHolder(secondHeaderView);
        secondViewHolder.sortLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOrderBy == CommentOrderByEnum.SET_TOP_AND_GMT_AES) {

                    mOrderBy = CommentOrderByEnum.SET_TOP_AND_GMT_DESC;

                    secondViewHolder.sortTv.setText("最早回复优先");

                    secondViewHolder.replyTimeIv.setImageResource(R.drawable.icon_reply_first);

                } else {

                    secondViewHolder.sortTv.setText("最晚回复优先");

                    secondViewHolder.replyTimeIv.setImageResource(R.drawable.icon_reply_last);

                    mOrderBy = CommentOrderByEnum.SET_TOP_AND_GMT_AES;
                }

                mRequestType = REFRESH;

                getComment();

            }
        });

        return secondHeaderView;
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        XjqUrlEnum urlEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (urlEnum) {
            case CMS_INFO_DETAIL:
                responseSuccessCmsInfoDetail((JSONObject) o);

                break;
            case COMMENT_QUERY:
                responseSuccessCommentQuery((JSONObject) o);
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        LogUtils.e("kk",jsonObject.toString());
        mRefreshEmptyViewHelper.closeRefresh();
        try {
            ErrorBean bean = new ErrorBean(jsonObject);
            if ("CMS_HAS_BEEN_DELETED".equals(bean.getError().getName()) || "CMS_NOT_EXISTS".equals(bean.getError().getName())) {
                mainContentLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyTipTv.setText("抱歉，该资讯已删除");
            } else {
                operateErrorResponseMessage(jsonObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void responseSuccessCmsInfoDetail(JSONObject jo) {

        bean = new Gson().fromJson(jo.toString(), NewsDetailsBean.class);

        if(bean.getShareUrl()!=null){
            shareIv.setVisibility(View.VISIBLE);
        }

        holder.titleTv.setText(bean.getInfoClientSimple().getTitle());

        holder.createTv.setText(TimeUtils.formatStringDate(bean.getNowDate(), bean.getInfoClientSimple().getGmtPublish()));

        mAdapter.setObjectType(CommentTypeEnum.CMS_NEWS.name());

        mAdapter.setSubjectId(String.valueOf(bean.getInfoClientSimple().getId()));

        mNewsContent = DetailsHtmlShowUtils.getFormatHtmlContent(this, bean.getInfoClientSimple().getContent());

        detailBottomViewUtil.setData(infoId,bean.getInfoClientSimple().getReplyCount(),
                bean.getInfoClientSimple().getLikeCount(),bean.getInfoClientSimple().isLiked(),
                ObjectTypeEnum.CMS_NEWS.name(),String.valueOf(bean.getInfoClientSimple().getId()),bean.getInfoClientSimple().isCommentOff());

        getComment();
    }


    private void responseSuccessCommentQuery(JSONObject jo) {

        UserCommentBean userCommentBean= new UserCommentBean(jo);

        maxPages = userCommentBean.getPaginator().getPages();

        for (int i = 0; i < userCommentBean.getCommentList().size(); i++) {

            UserCommentBean.CommentListBean commentBean = userCommentBean.getCommentList().get(i);
            commentBean.setContent(DetailsHtmlShowUtils.getFormatHtmlContent(this, commentBean.getContent()));
        }

        if (mRequestType == REFRESH) {

            commentList.clear();
        }
        commentList.addAll(userCommentBean.getCommentList());

        mAdapter.notifyDataSetChanged();

        //第一次进入这个界面，只做一次操作，之后都不做这个操作了。
        if (mFirstLoad) {

            mFirstLoad = false;

            holder.webView.loadDataWithBaseURL(null, mNewsContent, "text/html", "utf-8", null);
            detailBottomViewUtil.setRequestFailedListener(requestFailedListener);
            detailBottomViewUtil.setReplyLocationListener(locationListener);
            container.addView(detailBottomViewUtil.getView(this));
        }

        mRefreshEmptyViewHelper.closeRefresh();
    }
    DetailBottomViewUtils.RequestFailedListener requestFailedListener = new DetailBottomViewUtils.RequestFailedListener() {
        @Override
        public void requestFailed(JSONObject jsonObject) {
            try {
                operateErrorResponseMessage(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    OnMyClickListener locationListener = new OnMyClickListener() {
        @Override
        public void onClick(View v) {
            mListView.setSelection(1);
        }
    };

    static class SecondViewHolder {
        @BindView(R.id.lookAllRedTv)
        TextView lookAllRedTv;
        @BindView(R.id.lookAllLayout)
        LinearLayout lookAllLayout;
        @BindView(R.id.lookAuthorRedTv)
        TextView lookAuthorRedTv;
        @BindView(R.id.onlyLookAuthorLayout)
        LinearLayout onlyLookAuthorLayout;
        @BindView(R.id.replyTimeIv)
        ImageView replyTimeIv;
        @BindView(R.id.sortTv)
        TextView sortTv;
        @BindView(R.id.sortLayout)
        LinearLayout sortLayout;

        SecondViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder {
        @BindView(R.id.titleTv)
        TextView titleTv;
        @BindView(R.id.createTv)
        TextView createTv;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;
        WebView webView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
