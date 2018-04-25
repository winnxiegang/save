package com.android.xjq.activity.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.DetailsHtmlShowUtils;
import com.android.banana.commlib.utils.Money;
import com.android.banana.commlib.utils.RefreshEmptyViewHelper;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.http.JczjURLEnum;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.activity.NewsDetailActivity;
import com.android.xjq.activity.homepage.HomePageActivity;
import com.android.xjq.adapter.comment.CommentReplyDetailAdapter;
import com.android.xjq.bean.BaseObject;
import com.android.xjq.bean.CommentReplyDetailBean;
import com.android.xjq.bean.comment.CommentReplyBean;
import com.android.xjq.bean.comment.UserCommentBean;
import com.android.xjq.controller.maincontroller.SubjectMultiAdapterCallback;
import com.android.xjq.controller.maincontroller.SubjectMultiAdapterHelper;
import com.android.xjq.dialog.ShowReportDialog;
import com.android.xjq.fragment.WriteMySubjectEmojiListFragment;
import com.android.xjq.model.comment.CommentTypeEnum;
import com.android.xjq.model.comment.ObjectTypeEnum;
import com.android.xjq.utils.XjqUtils;
import com.android.xjq.utils.details.DetailsWebViewShowUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 评论楼层
 */
public class ReplyDetailActivity extends BaseActivity implements IHttpResponseListener, SubjectMultiAdapterCallback {

    private WrapperHttpHelper httpOperate;

    @BindView(R.id.refreshListView)
    ListView listView;

    @BindView(R.id.fragmentLayout)
    LinearLayout fragmentLayout;

    @BindView(R.id.showSubjectDetailTv)
    TextView showSubjectDetailTv;

    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;

    @BindView(R.id.mainContentLayout)
    LinearLayout mainLayout;

    @BindView(R.id.emptyIv)
    ImageView emptyIv;

    @BindView(R.id.emptyTv)
    TextView emptyTipTv;

    private ViewHolder mHeaderViewHolder;

    private CommentReplyDetailAdapter mAdapter;

    private List<CommentReplyBean> mList = new ArrayList<>();

    private String mUserId = null;

    private String userName = null;

    private boolean isLike = false;

    private int likeCount = 0;

    private ShowCommentDetailBean showCommentDetail;

    private WriteMySubjectEmojiListFragment emojListFragment;

    private int mCurrentPage = DEFAULT_PAGE;

    private SubjectMultiAdapterHelper mLikeUtil;
    /**
     * 第一次加载
     */
    private boolean mFirstLoad = true;

    private String mReplyContent;

    private boolean mCommentOff = false;

    private UserCommentBean.CommentListBean mComment;

    public static void startReplyDetailActivity(Context from, ShowCommentDetailBean bean) {

        Intent intent = new Intent(from, ReplyDetailActivity.class);

        intent.putExtra("commentDetail", bean);

        from.startActivity(intent);

    }

    @OnClick(R.id.showSubjectDetailTv)
    public void showSubjectDetail() {
        switch (showCommentDetail.getCommentType()) {
            case LOTTERY_PROJECT:
                //  OrderDetailsActivity.startOrderDetailsActivity(this, showCommentDetail.getObjectId());
                break;
            case SUBJECT:
//                CommentShowMatchDetail data = (CommentShowMatchDetail) showCommentDetail.baseObject;
//                if (data != null) {
//                    MatchDetailActivity.startMatchDetailActivity(this, data.getType(), data.getBizId(), data.getRaceId());
//                } else {
//                    SubjectDetailActivity.startSubjectDetailActivity(this, showCommentDetail.getObjectId());
//                }
                break;
            case CMS_NEWS:
                NewsDetailActivity.startNewsDetailActivity(this, showCommentDetail.getObjectId());
                break;
            case JCZQ_RACE:
                String objectId = null;
                if (showCommentDetail.getObjectId().contains(":")) {
                    objectId = showCommentDetail.getObjectId().split(":")[0];
                } else {
                    objectId = showCommentDetail.getObjectId();
                }
                //JczqAnalysisActivity.startJczjAnalysisActivity(this, objectId);
                break;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reply_detail);

        ButterKnife.bind(this);

        showCommentDetail = getIntent().getParcelableExtra("commentDetail");

        setTitleBar(true, showCommentDetail.getFloor() + "楼", null);

        initView();

        getCommentById();

        if (showCommentDetail.isShowDetail()) {

          //  showSubjectDetailTv.setVisibility(View.VISIBLE);

        }

        emptyIv.setImageResource(R.drawable.image_subject_deleted);

        emptyTipTv.setText("抱歉，该话题已删除");

    }

    private void initView() {
        httpOperate = new WrapperHttpHelper(this);
        mLikeUtil = new SubjectMultiAdapterHelper(this);

        mAdapter = new CommentReplyDetailAdapter(this, mList);

        mAdapter.setObjectType(showCommentDetail.getCommentType()!=null?showCommentDetail.getCommentType().name():null);

        listView.addHeaderView(getListViewHeaderView());

        listView.setAdapter(mAdapter);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        emojListFragment = WriteMySubjectEmojiListFragment.newInstance();

        emojListFragment.setListener(new WriteMySubjectEmojiListFragment.OnFragmentInteractionListener() {
            @Override
            public void onFragmentInteraction(boolean sendResult) {
                if (sendResult) {
                    mRequestType = REFRESH;
                    mCurrentPage = DEFAULT_PAGE;
                    getCommentReplyQuery(false);
                    LibAppUtil.hideSoftKeyboard(ReplyDetailActivity.this);
                }

            }
        });

        fragmentTransaction.add(R.id.container, emojListFragment);

        fragmentTransaction.commit();

        setRefreshLayout();

    }

    private View getListViewHeaderView() {

        View view = getLayoutInflater().inflate(R.layout.layout_reply_detail_header_view, null);

        mHeaderViewHolder = new ViewHolder(view);

        WebView webView = new WebView(getApplicationContext());

        webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dp50)));

        mHeaderViewHolder.webView = webView;

        mHeaderViewHolder.mainLayout.addView(webView, 1);

        DetailsWebViewShowUtils.setWebView(this, mHeaderViewHolder.webView);

        mHeaderViewHolder.moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });

        mHeaderViewHolder.replyImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReplyLayout(userName, null, true);
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (fragmentLayout.getVisibility() == View.GONE) {

                    return false;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {

                    emojListFragment.hideLayout();

                }

                return false;
            }
        });

        return view;
    }

    private void showPopUp(View v) {

        View view = LayoutInflater.from(this).inflate(R.layout.layout_comment2, null);

        TextView jubaoTv = (TextView) view.findViewById(R.id.juBaoTv);

        TextView zhiDingTv = (TextView) view.findViewById(R.id.zhiDingTv);

        LinearLayout zhiDingLayout = (LinearLayout) view.findViewById(R.id.zhiDingLayout);

        int addWidth = 0;

        //置顶的条件:1、话题详情和方案详情2、必须是自己发的方案或者话题
        if (LoginInfoHelper.getInstance().getUserId().equals(showCommentDetail.getLouZhuId())) { //判断是否是自己发的
            switch (showCommentDetail.getCommentType()) {
                case LOTTERY_PROJECT:
                case SUBJECT:
                    addWidth += LibAppUtil.getDpToPxValue(this, 0);
                    zhiDingLayout.setVisibility(View.VISIBLE);
                    String text = "";
                    if (mComment.isSetTop()) {
                        text = "取消置顶";
                    } else {
                        text = "置顶";
                    }
                    zhiDingTv.setText(text);
                    TextPaint paint = new TextPaint();
                    paint.setTextSize(getResources().getDimension(R.dimen.sp14));
                    int measureText = (int) paint.measureText(text);
                    addWidth += measureText;
                    break;
            }
        }

        final PopupWindow popupWindow = new PopupWindow(view, (int) getResources().getDimension(R.dimen.comment_popup_base_width) + addWidth, (int) getResources().getDimension(R.dimen.dp35));

        popupWindow.setFocusable(true);

        popupWindow.setOutsideTouchable(true);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];

        v.getLocationOnScreen(location);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - popupWindow.getWidth(), location[1] - 18);

        jubaoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();

                showReportDialog();

            }
        });

        zhiDingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mComment.isSetTop()) {
                    showCancelZhiDingDialog(mComment.getCommentId());
                } else {
                    createZhiDing(mComment.getCommentId(), true);
                }

                popupWindow.dismiss();

            }
        });
    }

    private void showCancelZhiDingDialog(final String commentId) {
        ShowMessageDialog.Builder builder = new ShowMessageDialog.Builder();
        builder.setMessage("确定要取消置顶该回复？");
        builder.setPositiveMessage("确定");
        builder.setNegativeMessage("取消");
        builder.setPositiveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createZhiDing(commentId, false);
            }
        });
        ShowMessageDialog dialog = new ShowMessageDialog(this, builder);
    }

    private void createZhiDing(String id, boolean setTop) {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.COMMENT_SET_TOP_OPERATE, true);
        formBody.put("setTop", setTop ? String.valueOf(1) : String.valueOf(0));
        formBody.put("commentId", id);

        httpOperate.startRequest(formBody, true);

    }


    /**
     * 设置刷新布局
     */
    private void setRefreshLayout() {

        mRefreshEmptyViewHelper = new RefreshEmptyViewHelper(this, new RefreshEmptyViewHelper.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mRequestType = REFRESH;

                mCurrentPage = DEFAULT_PAGE;

                getCommentReplyQuery(false);
            }

            @Override
            public void onLoadMore() {

                mRequestType = LOAD_MORE;

                if (mCurrentPage >= maxPages) {
                    ToastUtil.showLong(XjqApplication.getContext(),"已经到最后一页了!!");

                    mRefreshEmptyViewHelper.closeRefresh();

                } else {

                    mCurrentPage++;

                    getCommentReplyQuery(false);
                }
            }

            @Override
            public void onEmptyRefresh() {

            }
        }, getResources().getDrawable(R.drawable.icon_no_content_about_match_schedule_detail));

        listView.setDividerHeight(0);


        listView.setEmptyView(null);

    }


    @Override
    public void onSuccess(RequestContainer request, Object obj) {
        JczjURLEnum urlEnum = (JczjURLEnum) request.getRequestEnum();
        closeProgressDialog();
        switch (urlEnum) {
            case COMMENT_QUERY_BY_COMMENT_ID:
                responseSuccessCommentQueryById(((JSONObject) obj));
                if (showCommentDetail.getCommentReplyId() == null) {
                    getCommentReplyQuery(true);
                } else {
                    getCommentReplyPosition();
                }
                break;
            case COMMENT_REPLY_QUERY:
                responseSuccessCommentReplyQuery((JSONObject) obj);
                break;
            case COMMENT_REPLY_CREATE:
                ToastUtil.showLong(XjqApplication.getContext(),"回复发表成功");
                break;
            case BLACK_REPORT_MANAGE:
                com.android.banana.commlib.utils.LibAppUtil.showTip(this, "举报成功", R.drawable.icon_subscribe_success);
                break;
            //评论回复楼层查询
            case COMMENT_REPLY_QUERY_POSITION:
                try {
                    mCurrentPage = ((JSONObject) obj).getJSONObject("result").getInt("currentPageNum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getCommentReplyQuery(true);
                break;
            case COMMENT_SET_TOP_OPERATE: {//用户点击置顶或者取消置顶
                mComment.setSetTop(!mComment.isSetTop());
                if (!mComment.isSetTop()) {
                    com.android.banana.commlib.utils.LibAppUtil.showTip(this, "取消置顶成功", R.drawable.icon_subscribe_success);
                }
                setZhiDingView();
            }
            break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        mRefreshEmptyViewHelper.closeRefresh();
        closeProgressDialog();
        try {
            ErrorBean bean = new ErrorBean(jsonObject);
            if ("SUBJECT_HAS_BEEN_DELETED".equals(bean.getError().getName())) {
                mainLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                showSubjectDetailTv.setVisibility(View.GONE);
                emptyTipTv.setText("抱歉，该内容已删除");
            } else if ("COMMENT_HAS_BEEN_DELETED".equals(bean.getError().getName())) {
                mainLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                if (showCommentDetail.isShowDetail()) {
                  //  showSubjectDetailTv.setVisibility(View.VISIBLE);
                } else {
                    showSubjectDetailTv.setVisibility(View.GONE);
                }
                emptyTipTv.setText("抱歉，该内容已删除");
            } else {
                operateErrorResponseMessage(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUnLikeResult(JSONObject jsonObject, int position, boolean success) {
        if(success){
            likeCount = likeCount - 1;
            mHeaderViewHolder.supportStatusIv.setImageResource(R.drawable.ic_support_gray);

            if (0 == likeCount) {
                mHeaderViewHolder.supportCountTv.setText("");
                mHeaderViewHolder.supportCountTv.setVisibility(View.VISIBLE);
            } else {
                mHeaderViewHolder.supportCountTv.setVisibility(View.VISIBLE);
                mHeaderViewHolder.supportCountTv.setText(likeCount + "");
            }
        }else{
            try {
                operateErrorResponseMessage(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLikeOrSetTopResult(JSONObject jsonObject, int position, boolean success, boolean setTopRequest) {
       if(success){
           if (setTopRequest)
               return;
           isLike = success;
           likeCount = likeCount + 1;
           mHeaderViewHolder.supportStatusIv.setImageResource(R.drawable.ic_support_red);
           mHeaderViewHolder.supportCountTv.setVisibility(View.VISIBLE);
           mHeaderViewHolder.supportCountTv.setText(likeCount + "");
       }else{
           try {
               operateErrorResponseMessage(jsonObject);
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
    }



    private void showReportDialog() {

        ShowReportDialog.Builder builder = new ShowReportDialog.Builder();

        builder.setNegativeMessage("取消");
        builder.setReportClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibAppUtil.showTip(getApplicationContext(),"举报成功");
            }
        });
//        builder.setReportClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String reportMemoEnum = "";
//
//                switch (v.getTag().toString()) {
//
//                    case ShowReportDialog.SPAM:
//                        reportMemoEnum = "SPAM";
//                        blackReportManage(reportMemoEnum);
//                        break;
//
//                    case ShowReportDialog.SENSITIVE_MESSAGE:
//                        reportMemoEnum = "SENSITIVE_MESSAGE";
//                        blackReportManage(reportMemoEnum);
//                        break;
//
//                    case ShowReportDialog.PORNOGRAPHIC:
//                        reportMemoEnum = "PORNOGRAPHIC";
//                        blackReportManage(reportMemoEnum);
//                        break;
//
//                    case ShowReportDialog.FALSE_MESSAGE:
//                        reportMemoEnum = "FALSE_MESSAGE";
//                        blackReportManage(reportMemoEnum);
//                        break;
//
//                    case ShowReportDialog.FALSE_PRIZE:
//                        reportMemoEnum = "FALSE_PRIZE";
//                        blackReportManage(reportMemoEnum);
//                        break;
//
//                    case ShowReportDialog.PERSON_ATTACK:
//                        reportMemoEnum = "PERSON_ATTACK";
//                        blackReportManage(reportMemoEnum);
//                        break;
//
//                }
//            }
//        });

        ShowReportDialog dialog = new ShowReportDialog(ReplyDetailActivity.this, builder);

    }

    public void showReplyLayout(String userName, String commentReplyId, boolean isShowKeyboard) {

        if (mCommentOff) {
            LibAppUtil.showTip(this, getString(R.string.comment_off_tip));
            return;
        }
        emojListFragment.setValue(showCommentDetail.getCommentId(), commentReplyId, userName, isShowKeyboard);

    }

    /**
     * 举报
     *
     * @param reportMemo
     */
    private void blackReportManage(String reportMemo) {
        showProgressDialog();
        RequestFormBody map = new RequestFormBody(JczjURLEnum.BLACK_REPORT_MANAGE, true);
        map.put("reportUserId", mUserId);
        map.put("reportUserName", userName);
        map.put("reportItemId", showCommentDetail.getCommentId());
        map.put("reportItemType", "COMMENT");
        map.put("reportMemo", reportMemo);
        httpOperate.startRequest(map, true);
    }

    private void getCommentReplyQuery(boolean showPg) {
        if (showPg)
            showProgressDialog();
        RequestFormBody map = new RequestFormBody(JczjURLEnum.COMMENT_REPLY_QUERY, true);
        map.put("currentPage", String.valueOf(mCurrentPage));
        map.put("commentId", showCommentDetail.getCommentId());
        map.put("objectType", showCommentDetail.getCommentType().name());
        httpOperate.startRequest(map, true);

    }

    private void getCommentById() {
        showProgressDialog();
        RequestFormBody map = new RequestFormBody(JczjURLEnum.COMMENT_QUERY_BY_COMMENT_ID, true);
        map.put("commentId", showCommentDetail.getCommentId());
        if (showCommentDetail.getObjectId() != null && showCommentDetail.getObjectId().contains(":")) {
            map.put("objectId", showCommentDetail.getObjectId().split(":")[1]);
        } else {
            map.put("objectId", showCommentDetail.getObjectId());
        }
        map.put("commentObjectType", showCommentDetail.getCommentType().name());

        httpOperate.startRequest(map, true);
    }

    private void getCommentReplyPosition() {
        showProgressDialog();
        RequestFormBody map = new RequestFormBody(JczjURLEnum.COMMENT_REPLY_QUERY_POSITION, true);
        map.put("commentId", showCommentDetail.getCommentId());
        map.put("commentReplyId", showCommentDetail.getCommentReplyId());
        map.put("orderBys", "GMT_CREATE_DESC");
        httpOperate.startRequest(map, true);
    }


    private void setZhiDingView() {

        if (mComment.isSetTop()) {
            mHeaderViewHolder.zhiDingIv.setVisibility(View.VISIBLE);
        } else {

            mHeaderViewHolder.zhiDingIv.setVisibility(View.GONE);
        }

    }

    private void responseSuccessCommentReplyQuery(JSONObject jo) {

        final CommentReplyDetailBean bean = new Gson().fromJson(jo.toString(), CommentReplyDetailBean.class);

        maxPages = bean.getPaginator().getPages();

        List<CommentReplyBean> commentReplyList = bean.getCommentReplyList();

        for (CommentReplyBean tempBean : commentReplyList) {

            if (tempBean.getUserId().equals(showCommentDetail.getLouZhuId())) {
                tempBean.setLouZhu(true);
            }

            if (tempBean.getCommentReplyId().equals(showCommentDetail.getCommentReplyId())) {
                tempBean.setShowBlack(true);
            }

            tempBean.setGmtCreate(TimeUtils.formatTime(bean.getNowDate(), tempBean.getGmtCreate()));

            tempBean.setContent(XjqUtils.fullToHalf(XjqUtils.formatReplyString(tempBean.getContent())));

            //第二次请求二级评论勋章
            if (bean.getUserMedalClientSimpleMap() != null && bean.getUserMedalClientSimpleMap().containsKey(tempBean.getUserId())) {

                tempBean.setUserMedalBeanList(bean.getUserMedalClientSimpleMap().get(tempBean.getUserId()));
            }
        }

        if (mRequestType == REFRESH) {

            mList.clear();

            mList.addAll(commentReplyList);

        } else if (mRequestType == LOAD_MORE) {

            mList.addAll(commentReplyList);

        }

        mAdapter.notifyDataSetChanged();

        if (mFirstLoad) {

            mFirstLoad = false;
            LogUtils.e("kkkk",mReplyContent);
            mHeaderViewHolder.webView.loadDataWithBaseURL(null, mReplyContent, "text/html", "utf-8", null);

        }
        fragmentLayout.setVisibility(View.VISIBLE);

        showReplyLayout(userName, null, showCommentDetail.isShowKeyboard());

        mRefreshEmptyViewHelper.closeRefresh();



    }

    private void responseSuccessCommentQueryById(JSONObject jo) {

        final CommentReplyDetailBean bean = new CommentReplyDetailBean(jo);

        mComment = bean.getComment();

        mComment.setGmtCreate(TimeUtils.formatTime(bean.getNowDate(), mComment.getGmtCreate()));

        mReplyContent = DetailsHtmlShowUtils.getFormatHtmlContent(this, bean.getComment().getContent());

        mHeaderViewHolder.timeTv.setText(bean.getComment().getGmtCreate());

        setUserLogoView(mHeaderViewHolder.portraitIv, bean.getComment().getUserLogoUrl());


        mCommentOff = bean.getCommentObject().isCommentOff();

        mUserId = bean.getComment().getUserId();

        userName = bean.getComment().getUserName();

        isLike = bean.getComment().isLiked();

        likeCount = bean.getComment().getLikeCount();

        setLikeCount(mHeaderViewHolder.supportCountTv, mHeaderViewHolder.supportStatusIv, likeCount, isLike);

        setTitleBar(true, mComment.getFloor() + "楼", null);

        setUserLogoInfo(mComment.getUserId(),
                mComment.getUserLogoUrl(),
                mHeaderViewHolder.portraitIv,
                mComment.getUserName(),
                mHeaderViewHolder.userNameTv,
                mComment.isVip(),
                mHeaderViewHolder.vipIv);

        //评论详情页设置楼主
        if (ObjectTypeEnum.SUBJECT.name().equals(showCommentDetail.getCommentType().name()) && mComment.getUserId().equals(showCommentDetail.getLouZhuId())) {

            mHeaderViewHolder.louzhuIv.setVisibility(View.VISIBLE);

            mAdapter.setJczjUserMedalShow(mComment.getUserMedalBeanList(), mHeaderViewHolder.userInfoLayout, 2);

        } else {

            mHeaderViewHolder.louzhuIv.setVisibility(View.GONE);

            mAdapter.setJczjUserMedalShow(mComment.getUserMedalBeanList(), mHeaderViewHolder.userInfoLayout, 1);

        }

        if (mComment.isVip()) {
            mHeaderViewHolder.vipIv.setVisibility(View.VISIBLE);
        } else {
            mHeaderViewHolder.vipIv.setVisibility(View.GONE);
        }

        mHeaderViewHolder.portraitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageActivity.startHomepageActivity(ReplyDetailActivity.this,mComment.getUserId());
            }
        });
        mHeaderViewHolder.userNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageActivity.startHomepageActivity(ReplyDetailActivity.this,mComment.getUserId());
            }
        });

        mHeaderViewHolder.supportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLike)
                    mLikeUtil.unLike(0, showCommentDetail.getCommentId(), "COMMENT");
                else
                    mLikeUtil.like(0, showCommentDetail.getCommentId(), "COMMENT");
            }
        });

        if (bean.getUserIdAndMoney() != null && bean.getUserIdAndMoney().get(mComment.getUserId()) != null)
            mComment.setCouponAmount(new Money(bean.getUserIdAndMoney().get(mComment.getUserId())).toString());

        setZhiDingView();

        setCouponView();

    }

    private void setUserLogoView(CircleImageView portraitIv, String userLogoUrl) {
        PicUtils.load(this, portraitIv, userLogoUrl, R.drawable.user_default_logo);
    }

    private void setCouponView() {
        UserCommentBean.CommentListBean bean = mComment;
        switch (showCommentDetail.commentType) {
            case LOTTERY_PROJECT:
            case SUBJECT:
                if (bean.getCouponAmount() == null) {
                    mHeaderViewHolder.sendCouponLayout.setVisibility(View.GONE);
                    return;
                }
                mHeaderViewHolder.sendCouponAmountTv.setText("共发给作者" + bean.getCouponAmount() + "元");
                if (bean.getCouponAmount() != null) {
                    mHeaderViewHolder.sendCouponLayout.setVisibility(View.VISIBLE);
                }
                break;
            default:
                mHeaderViewHolder.sendCouponLayout.setVisibility(View.GONE);
                break;
        }
    }


    private void setLikeCount(TextView tv, ImageView iv, int likeCount, boolean liked) {

        if (liked) {
            iv.setImageResource(R.drawable.ic_support_red);
        } else {
            iv.setImageResource(R.drawable.ic_support_gray);
        }

        if (likeCount == 0) {
            tv.setText("支持");
            tv.setVisibility(View.VISIBLE);

        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(likeCount + "");
        }
    }


    @Override
    protected void onDestroy() {

        mHeaderViewHolder.mainLayout.removeView(mHeaderViewHolder.webView);

        mHeaderViewHolder.webView.destroy();

        mHeaderViewHolder.webView = null;

        super.onDestroy();

    }


    public static class ShowCommentDetailBean implements Parcelable {

        private String commentId;

        private String commentReplyId;

        //是否查看主题
        private boolean showDetail;

        //查看主题的ID
        private String objectId;

        private CommentTypeEnum commentType;

        private String louZhuId;

        private int floor;

        private boolean showReply;

        private boolean showKeyboard;

        public boolean isShowKeyboard() {
            return showKeyboard;
        }

        public void setShowKeyboard(boolean showKeyboard) {
            this.showKeyboard = showKeyboard;
        }

        //主要存放个性话数据
        private BaseObject baseObject;

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getCommentReplyId() {
            return commentReplyId;
        }

        public void setCommentReplyId(String commentReplyId) {
            this.commentReplyId = commentReplyId;
        }

        public boolean isShowDetail() {
            return showDetail;
        }

        public void setShowDetail(boolean showDetail) {
            this.showDetail = showDetail;
        }

        public CommentTypeEnum getCommentType() {
            return commentType;
        }

        public void setCommentType(CommentTypeEnum commentType) {
            this.commentType = commentType;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getLouZhuId() {
            return louZhuId;
        }

        public void setLouZhuId(String louZhuId) {
            this.louZhuId = louZhuId;
        }

        public int getFloor() {
            return floor;
        }

        public void setFloor(int floor) {
            this.floor = floor;
        }

        public boolean isShowReply() {
            return showReply;
        }

        public void setShowReply(boolean showReply) {
            this.showReply = showReply;
        }

        public BaseObject getBaseObject() {
            return baseObject;
        }

        public void setBaseObject(BaseObject baseObject) {
            this.baseObject = baseObject;
        }

        public ShowCommentDetailBean() {
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.commentId);
            dest.writeString(this.commentReplyId);
            dest.writeByte(this.showDetail ? (byte) 1 : (byte) 0);
            dest.writeString(this.objectId);
            dest.writeInt(this.commentType == null ? -1 : this.commentType.ordinal());
            dest.writeString(this.louZhuId);
            dest.writeInt(this.floor);
            dest.writeByte(this.showReply ? (byte) 1 : (byte) 0);
            dest.writeByte(this.showKeyboard ? (byte) 1 : (byte) 0);
            dest.writeParcelable(this.baseObject, flags);
        }

        protected ShowCommentDetailBean(Parcel in) {
            this.commentId = in.readString();
            this.commentReplyId = in.readString();
            this.showDetail = in.readByte() != 0;
            this.objectId = in.readString();
            int tmpCommentType = in.readInt();
            this.commentType = tmpCommentType == -1 ? null : CommentTypeEnum.values()[tmpCommentType];
            this.louZhuId = in.readString();
            this.floor = in.readInt();
            this.showReply = in.readByte() != 0;
            this.showKeyboard = in.readByte() != 0;
            this.baseObject = in.readParcelable(BaseObject.class.getClassLoader());
        }

        public static final Creator<ShowCommentDetailBean> CREATOR = new Creator<ShowCommentDetailBean>() {
            @Override
            public ShowCommentDetailBean createFromParcel(Parcel source) {
                return new ShowCommentDetailBean(source);
            }

            @Override
            public ShowCommentDetailBean[] newArray(int size) {
                return new ShowCommentDetailBean[size];
            }
        };
    }

    /**
     * @param userId      用户Id
     * @param userLogoUrl 用户头像URL
     * @param logoIv      显示头像的ImageView
     * @param userName    用户名
     * @param userNameTv  显示用户名的TextView
     * @param vip         是否是vip
     * @param vipIv       vip的ImageView
     */
    protected void setUserLogoInfo(final String userId,
                                   String userLogoUrl,
                                   CircleImageView logoIv,
                                   String userName,
                                   TextView userNameTv,
                                   boolean vip,
                                   ImageView vipIv) {

        setUserLogoView(logoIv, userLogoUrl);

        userNameTv.setText(userName);

        if (vip) {
            vipIv.setVisibility(View.VISIBLE);
        } else {
            vipIv.setVisibility(View.GONE);
        }

        logoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HomepageActivity.startHomePageActivity(ReplyDetailActivity.this, userId);
            }
        });

        userNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  HomepageActivity.startHomePageActivity(ReplyDetailActivity.this, userId);
            }
        });

    }

    /**
     * @param userId      用户Id
     * @param userLogoUrl 用户头像URL
     * @param logoIv      显示头像的ImageView
     * @param userName    用户名
     * @param userNameTv  显示用户名的TextView
     * @param vip         是否是vip
     * @param vipIv       vip的ImageView
     */
    protected void setUserLogoInfo(final String userId,
                                   String userLogoUrl,
                                   CircleImageView logoIv,
                                   String userName,
                                   TextView userNameTv,
                                   boolean vip,
                                   ImageView vipIv, boolean isClickble) {

        setUserLogoView(logoIv, userLogoUrl);

        userNameTv.setText(userName);

        if (vip) {
            vipIv.setVisibility(View.VISIBLE);
        } else {
            vipIv.setVisibility(View.GONE);
        }
        if (isClickble) {
            logoIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userId != null) {
                        //HomepageActivity.startHomePageActivity(ReplyDetailActivity.this, userId);
                    }
                }
            });

            userNameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userId != null) {
                        // HomepageActivity.startHomePageActivity(ReplyDetailActivity.this, userId);
                    }
                }
            });
        }


    }

    static class ViewHolder {
        @BindView(R.id.userInfoLayout)
        LinearLayout userInfoLayout;
        @BindView(R.id.louzhuIv)
        ImageView louzhuIv;
        @BindView(R.id.portraitIv)
        CircleImageView portraitIv;
        @BindView(R.id.vipIv)
        ImageView vipIv;
        @BindView(R.id.userNameTv)
        TextView userNameTv;
        @BindView(R.id.timeTv)
        TextView timeTv;
        @BindView(R.id.sendCouponAmountTv)
        TextView sendCouponAmountTv;
        @BindView(R.id.sendCouponLayout)
        LinearLayout sendCouponLayout;
        @BindView(R.id.moreIv)
        ImageView moreIv;
        @BindView(R.id.supportImageIv)
        ImageView supportStatusIv;
        @BindView(R.id.supportCountTv)
        TextView supportCountTv;
        @BindView(R.id.supportLayout)
        LinearLayout supportLayout;
        @BindView(R.id.zhiDingIv)
        ImageView zhiDingIv;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;
        @BindView(R.id.replyImageIv)
        ImageView replyImageIv;
        WebView webView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
