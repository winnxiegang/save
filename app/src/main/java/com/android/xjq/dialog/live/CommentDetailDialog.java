package com.android.xjq.dialog.live;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.DetailsHtmlShowUtils;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.RefreshEmptyViewHelper;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.view.swipyrefreshlayout.VpSwipeRefreshLayout;
import com.android.banana.pullrecycler.recyclerview.onRefreshListener;
import com.android.banana.utils.KeyboardHelper;
import com.android.banana.view.ScrollListView;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.adapter.comment.CommentAdapter;
import com.android.xjq.bean.SubjectBean2;
import com.android.xjq.bean.comment.UserCommentBean;
import com.android.xjq.bean.subject.SubjectDetailHeadBean;
import com.android.xjq.dialog.base.DialogBase;
import com.android.xjq.model.comment.CommentOrderByEnum;
import com.android.xjq.model.comment.CommentTypeEnum;
import com.android.xjq.model.comment.ObjectTypeEnum;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.details.DetailBottomViewUtils;
import com.android.xjq.view.ImpressionLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by lingjiu on 2018/3/11.
 */

public class CommentDetailDialog extends DialogBase implements OnHttpResponseListener {

    @BindView(R.id.refreshListView)
    ScrollListView refreshListView;
    @BindView(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;
    @BindView(R.id.addImpressionEt)
    EditText addImpressionEt;
    @BindView(R.id.addImpressionLayout)
    LinearLayout addImpressionLayout;
    @BindView(R.id.container)
    FrameLayout container;
    /**
     * 第一次加载
     */
    private boolean mFirstLoad = true;
    private DetailBottomViewUtils detailBottomViewUtil;
    private HttpRequestHelper mHttpRequestHelper;
    private CommentAdapter mAdapter;
    private List<UserCommentBean.CommentListBean> commentList = new ArrayList<>();
    private SubjectDetailHeadBean mDetailBean;
    private String subjectId;
    private ViewHolder viewHolder;
    protected final int LOAD_MORE = 1;

    protected final int REFRESH = 2;

    protected int mRequestType = REFRESH;

    private boolean onlyLookLouZhuComment = false;

    private CommentOrderByEnum mOrderBy;
    private int maxPages = 100;

    private int currentPage = 1;

    // 刷新相关
    protected RefreshEmptyViewHelper mRefreshEmptyViewHelper;

    private onRefreshListener refreshListener;


    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (mAdapter != null) {
                mAdapter.clickScreen();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private View getHeadView() {
        //评论头部
        View headerView = getLayoutInflater().inflate(R.layout.layout_comment_dialog_header, null);

        viewHolder = new ViewHolder(headerView);

        viewHolder.impressionLayout.setParentLayoutBg(R.color.normal_bg);
        viewHolder.impressionLayout.getImpressionTagInit(false, subjectId);
        viewHolder.impressionLayout.setShowAddLayout(true, addImpressionListener);

        viewHolder.sortLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOrderBy == CommentOrderByEnum.SET_TOP_AND_GMT_AES) {

                    mOrderBy = CommentOrderByEnum.SET_TOP_AND_GMT_DESC;

                    viewHolder.sortTv.setText("最早回复优先");

                    viewHolder.replyTimeIv.setImageResource(R.drawable.icon_reply_first);

                } else {

                    viewHolder.sortTv.setText("最晚回复优先");

                    viewHolder.replyTimeIv.setImageResource(R.drawable.icon_reply_last);

                    mOrderBy = CommentOrderByEnum.SET_TOP_AND_GMT_AES;
                }

                selectComment();

            }
        });

        viewHolder.onlyLookAuthorLayout.setVisibility(View.VISIBLE);
        viewHolder.lookAllRedTv.setVisibility(View.VISIBLE);
        viewHolder.lookAuthorRedTv.setVisibility(View.GONE);

        viewHolder.onlyLookAuthorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onlyLookLouZhuComment = true;
                setSelectView();
            }
        });

        viewHolder.lookAllLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlyLookLouZhuComment = false;
                setSelectView();
            }
        });
        return headerView;
    }


    public CommentDetailDialog(Context context, SubjectDetailHeadBean detailBean, onRefreshListener refreshListener) {

        super(context, R.layout.dialog_comment_detail, R.style.dialog_base, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);

        this.mDetailBean = detailBean;

        subjectId = detailBean.getSubjectSimple().subjectId;

        mHttpRequestHelper = new HttpRequestHelper(context, this);

        this.refreshListener = refreshListener;

        detailBottomViewUtil = new DetailBottomViewUtils();

        refreshListView.addHeaderView(getHeadView());

        mAdapter = new CommentAdapter(context, commentList);

        refreshListView.setAdapter(mAdapter);

        mOrderBy = CommentOrderByEnum.SET_TOP_AND_GMT_DESC;

        setRefreshLayout();

        addImpressionEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addImpressionEt.clearFocus();
                    KeyboardHelper.hideSoftInput(addImpressionEt);
                    if (!TextUtils.isEmpty(addImpressionEt.getText().toString())) {
                        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.TAG_CREATE, true);
                        map.put("objectId", subjectId);
                        map.put("objectType", "SUBJECT");
                        map.put("tagName", addImpressionEt.getText().toString());
                        mHttpRequestHelper.startRequest(map, true);
                        addImpressionEt.setText("");
                    }
                    addImpressionLayout.setVisibility(View.GONE);
                }
                return false;
            }
        });
        responseSuccessSubjectDetail();
    }

    OnMyClickListener addImpressionListener = new OnMyClickListener() {
        @Override
        public void onClick(View v) {
            if (addImpressionLayout.getVisibility() == View.GONE) {
                addImpressionLayout.setVisibility(View.VISIBLE);
            }
            addImpressionEt.requestFocus();
            KeyboardHelper.showSoftInput(addImpressionEt);
        }
    };


    DetailBottomViewUtils.RequestFailedListener requestFailedListener = new DetailBottomViewUtils.RequestFailedListener() {
        @Override
        public void requestFailed(JSONObject jsonObject) {
            try {
                ((BaseActivity) context).operateErrorResponseMessage(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void getComment() {

        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.COMMENT_QUERY, true);

        map.put("objectId", subjectId);

        map.put("objectType", CommentTypeEnum.SUBJECT.name());

        map.put("currentPage", String.valueOf(currentPage));

        map.put("orderBy", mOrderBy.name());

        if (onlyLookLouZhuComment && mDetailBean != null) {
            map.put("userId", mDetailBean.getSubjectSimple().userId);
        }

        mHttpRequestHelper.startRequest(map);
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        switch ((XjqUrlEnum) requestContainer.getRequestEnum()) {
            case COMMENT_QUERY:
                responseSuccessCommentQuery(jsonObject);
                break;
            case TAG_CREATE:
                viewHolder.impressionLayout.getImpressionTagInit(false, subjectId);
                break;
        }

    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            ((BaseActivity) context).operateErrorResponseMessage(jsonObject);
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


    public void update(SubjectDetailHeadBean mDetailBean) {
        this.mDetailBean = mDetailBean;
        responseSuccessSubjectDetail();
        getComment();
    }

    private void responseSuccessSubjectDetail() {

        final SubjectBean2 subjectSimpleBean = mDetailBean.getSubjectSimple();

        String userId = subjectSimpleBean.userId;

        mAdapter.setLouZhuId(userId);

        subjectSimpleBean.gmtCreate = (TimeUtils.formatTime(mDetailBean.getNowDate(), subjectSimpleBean.gmtCreate));

        mAdapter.setObjectType(CommentTypeEnum.SUBJECT.name());

        mAdapter.setSubjectId(subjectSimpleBean.subjectId);

        detailBottomViewUtil.setData(subjectId,
                mDetailBean.getSubjectSimple().replyCount,
                mDetailBean.getSubjectSimple().likeCount,
                mDetailBean.getSubjectSimple().liked,
                ObjectTypeEnum.SUBJECT.name(),
                String.valueOf(mDetailBean.getSubjectSimple().subjectId),
                mDetailBean.getSubjectSimple().commentOff);

        //第一次进入这个界面，只做一次操作，之后都不做这个操作了。
        if (mFirstLoad) {
            mFirstLoad = false;
            detailBottomViewUtil.setReplyLocationListener(null);
            detailBottomViewUtil.setRequestFailedListener(requestFailedListener);
            container.addView(detailBottomViewUtil.getView(context), 0);
        }

        getComment();
    }

    private void responseSuccessCommentQuery(JSONObject jo) {

        UserCommentBean bean = new UserCommentBean(jo);

        maxPages = bean.getPaginator().getPages();

        for (int i = 0; i < bean.getCommentList().size(); i++) {

            UserCommentBean.CommentListBean commentBean = bean.getCommentList().get(i);

            commentBean.setContent(DetailsHtmlShowUtils.getFormatHtmlContent(context, commentBean.getContent()));
        }
        if (mRequestType == REFRESH) {
            commentList.clear();
        }

        commentList.addAll(bean.getCommentList());

        mAdapter.notifyDataSetChanged();
        //第一次进入这个界面，只做一次操作，之后都不做这个操作了。
        if (mFirstLoad) {
            mFirstLoad = false;
            detailBottomViewUtil.setReplyLocationListener(locationListener);
            detailBottomViewUtil.setRequestFailedListener(requestFailedListener);
            container.addView(detailBottomViewUtil.getView(context), 0);
        }
        LogUtils.e("kk", (refreshListView.getVisibility() == View.VISIBLE) + "--------------visible");
        mRefreshEmptyViewHelper.closeRefresh();
    }

    OnMyClickListener locationListener = new OnMyClickListener() {
        @Override
        public void onClick(View v) {
            refreshListView.setSelection(1);
        }
    };

    private void setSelectView() {
        if (onlyLookLouZhuComment) {
            viewHolder.lookAllRedTv.setVisibility(View.GONE);
            viewHolder.lookAuthorRedTv.setVisibility(View.VISIBLE);
            viewHolder.lookAllTv.setTextColor(context.getResources().getColor(R.color.colorTextG3));
            viewHolder.lookAuthorTv.setTextColor(context.getResources().getColor(R.color.colorTextG4));
        } else {
            viewHolder.lookAllRedTv.setVisibility(View.VISIBLE);
            viewHolder.lookAuthorRedTv.setVisibility(View.GONE);
            viewHolder.lookAllTv.setTextColor(context.getResources().getColor(R.color.colorTextG4));
            viewHolder.lookAuthorTv.setTextColor(context.getResources().getColor(R.color.colorTextG3));
        }
        selectComment();
    }

    private void selectComment() {
        mRequestType = REFRESH;
        currentPage = 1;
        getComment();
    }

    /**
     * 设置刷新布局
     */
    private void setRefreshLayout() {

        mRefreshEmptyViewHelper = new RefreshEmptyViewHelper(rootView, new RefreshEmptyViewHelper.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestType = REFRESH;

                currentPage = 1;

                onlyLookLouZhuComment = false;

                setSelectView();

                refreshListener.onRefresh(true);

               viewHolder.impressionLayout.getImpressionTagInit(false, subjectId);
            }
            @Override
            public void onLoadMore() {

                mRequestType = LOAD_MORE;

                if (currentPage >= maxPages) {

                    LibAppUtil.showTip(context, "已经到最后一页了!!");

                    mRefreshEmptyViewHelper.closeRefresh();

                } else {

                    currentPage++;

                    getComment();
                }
            }

            @Override
            public void onEmptyRefresh() {

            }
        }, context.getResources().getDrawable(R.drawable.icon_no_content_about_match_schedule_detail), "暂无评论");

        refreshListView.setDividerHeight(0);
    }


    static class ViewHolder {
        @BindView(R.id.impressionLayout)
        ImpressionLayout impressionLayout;
        @BindView(R.id.lookAllTv)
        TextView lookAllTv;
        @BindView(R.id.lookAllRedTv)
        TextView lookAllRedTv;
        @BindView(R.id.lookAllLayout)
        LinearLayout lookAllLayout;
        @BindView(R.id.lookAuthorTv)
        TextView lookAuthorTv;
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
