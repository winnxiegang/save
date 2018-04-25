package com.android.xjq.activity.dynamic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShareDialogFragment;
import com.android.banana.commlib.escapeUtils.StringEscapeUtils;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.DetailsHtmlShowUtils;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.RefreshEmptyViewHelper;
import com.android.banana.commlib.utils.StringUtils;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.HtmlEmojiTextView;
import com.android.banana.commlib.view.MedalLayout;
import com.android.banana.utils.KeyboardHelper;
import com.android.banana.view.ScrollListView;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.activity.SubjectDetailActivity;
import com.android.xjq.activity.homepage.HomePageActivity;
import com.android.xjq.activity.wall.LiveWallDetailActivity;
import com.android.xjq.adapter.comment.CommentAdapter;
import com.android.xjq.bean.SubjectBean2;
import com.android.xjq.bean.SubjectProperties;
import com.android.xjq.bean.UserMedalLevelBean;
import com.android.xjq.bean.comment.UserCommentBean;
import com.android.xjq.bean.subject.SubjectDetailHeadBean;
import com.android.xjq.model.SubjectObjectSubType2;
import com.android.xjq.model.SubjectPropertiesType;
import com.android.xjq.model.comment.CommentOrderByEnum;
import com.android.xjq.model.comment.CommentTypeEnum;
import com.android.xjq.model.comment.ObjectTypeEnum;
import com.android.xjq.utils.SharePopUpWindowHelper;
import com.android.xjq.utils.SubjectUtils2;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.details.DetailBottomViewUtils;
import com.android.xjq.utils.details.DetailReportPopupWindow;
import com.android.xjq.view.ImpressionLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.VISIBLE;

public class TransmitDetailsActivity extends BaseActivity implements IHttpResponseListener {


    @BindView(R.id.refreshListView)
    ScrollListView refreshListView;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.emptyIv)
    ImageView emptyIv;
    @BindView(R.id.shareIv)
    ImageView shareIv;
    @BindView(R.id.moreIv)
    ImageView moreIv;
    @BindView(R.id.emptyTv)
    TextView emptyTipTv;
    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;

    @BindView(R.id.mainContentLayout)
    LinearLayout mainContentLayout;
    @BindView(R.id.addImpressionEt)
    EditText addImpressionEt;
    @BindView(R.id.addImpressionLayout)
    LinearLayout addImpressionLayout;
    private DetailReportPopupWindow mDetailReportPopupWindow;
    private String cardShowSourceTitle;
    private String cardShowSourceSummary;
    private String cardShowSourceImageUrl;
    private int cardShowSourceDefaultResId = 0;
    private String firstFrameUrl = null;
    private String videoUrl = null;


    @OnClick(R.id.shareIv)
    public void share() {

        final ShareDialogFragment shareDialog = ShareDialogFragment.newInstance();
        shareDialog.show(getSupportFragmentManager());
        final String finalArticleUrl = cardShowSourceImageUrl;

        shareDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDetailBean == null)
                    return;
                SubjectUtils2.showShareDialog(TransmitDetailsActivity.this,
                        v,
                        mDetailBean.getSubjectSimple().loginName, subjectId,
                        "SUBJECT",
                        cardShowSourceTitle,
                        cardShowSourceSummary,
                        finalArticleUrl,
                        mDetailBean.getSubjectSimple().summary,
                        cardShowSourceDefaultResId);
                shareDialog.dismiss();
            }
        });
    }

    @OnClick(R.id.moreIv)
    public void moreClick() {
        mDetailReportPopupWindow.showPopupWindow();
    }

    private SharePopUpWindowHelper.Builder builder;
    private String subjectId;

    private List<UserCommentBean.CommentListBean> commentList = new ArrayList<>();

    private CommentAdapter mAdapter;

    private WrapperHttpHelper httpRequestHelper;

    private ViewHolder firstHeaderViewHolder;

    private SecondViewHolder SecondViewHolder;

    private String userId;

    private SubjectDetailHeadBean mDetailBean;

    private CommentOrderByEnum mOrderBy;

    private DetailBottomViewUtils detailBottomViewUtil;
    /**
     * 第一次加载
     */
    private boolean mFirstLoad = true;

    private boolean onlyLookLouZhuComment = false;

    public static void startTransmitDetailsActivity(Context activity, String subjectId) {

        Intent intent = new Intent();

        intent.setClass(activity, TransmitDetailsActivity.class);

        intent.putExtra("subjectId", subjectId);

        activity.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subject_detail);

        httpRequestHelper = new WrapperHttpHelper(this);

        detailBottomViewUtil = new DetailBottomViewUtils();

        ButterKnife.bind(this);

        setTitleBar(true, "详情", null);

        subjectId = getIntent().getStringExtra("subjectId");
        mDetailReportPopupWindow = new DetailReportPopupWindow(this, moreIv);

        setRefreshLayout(this);

        mOrderBy = CommentOrderByEnum.SET_TOP_AND_GMT_DESC;

        refreshListView.addHeaderView(getHeadView());

        refreshListView.addHeaderView(getSecondHeadView());

        mAdapter = new CommentAdapter(this, commentList);

        refreshListView.setAdapter(mAdapter);

        emptyIv.setImageResource(R.drawable.icon_no_content_about_match_schedule_detail);

        mAdapter.setRefreshListener(new OnMyClickListener() {
            @Override
            public void onClick(View v) {

                mRequestType = REFRESH;

                currentPage = DEFAULT_PAGE;

                getComment();

            }
        });

        addImpressionEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addImpressionEt.clearFocus();
                    KeyboardHelper.hideSoftInput(addImpressionEt);
                    addImpressionLayout.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(addImpressionEt.getText().toString())) {
                        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.TAG_CREATE, true);
                        map.put("objectId", subjectId);
                        map.put("objectType", "SUBJECT");
                        map.put("tagName", addImpressionEt.getText().toString());
                        httpRequestHelper.startRequest(map, true);
                        addImpressionEt.setText("");
                    }
                }
                return false;
            }
        });

        getSubject();

        firstHeaderViewHolder.impressionLayout.setShowAddLayout(true, addImpressionListener);

        firstHeaderViewHolder.impressionLayout.getImpressionTagInit(false, subjectId);
    }

    private void getSubject() {

        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.SUBJECT_DETAIL, true);

        map.put("subjectId", subjectId);

        map.put("objectType", "TRANSMIT");

        httpRequestHelper.startRequest(map, true);
    }

    private View getSecondHeadView() {
        //评论头部
        View secondHeaderView = getLayoutInflater().inflate(R.layout.layout_all_comment_top, null);

        SecondViewHolder = new SecondViewHolder(secondHeaderView);
        SecondViewHolder.sortLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOrderBy == CommentOrderByEnum.SET_TOP_AND_GMT_AES) {

                    mOrderBy = CommentOrderByEnum.SET_TOP_AND_GMT_DESC;

                    SecondViewHolder.sortTv.setText("最早回复优先");

                    SecondViewHolder.replyTimeIv.setImageResource(R.drawable.icon_reply_first);

                } else {

                    SecondViewHolder.sortTv.setText("最晚回复优先");

                    SecondViewHolder.replyTimeIv.setImageResource(R.drawable.icon_reply_last);

                    mOrderBy = CommentOrderByEnum.SET_TOP_AND_GMT_AES;
                }

                selectComment();

            }
        });

        return secondHeaderView;
    }

    private View getHeadView() {
        //评论头部
        View headerView = getLayoutInflater().inflate(R.layout.layout_transmit_detail_header, null);

        firstHeaderViewHolder = new ViewHolder(headerView);

        firstHeaderViewHolder.impressionLayout.setParentLayoutBg(R.color.normal_bg);

        return headerView;
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

    private void selectComment() {
        mRequestType = REFRESH;
        currentPage = 1;
        getComment();
    }

    private void setSelectView() {
        if (onlyLookLouZhuComment) {
            SecondViewHolder.lookAllRedTv.setVisibility(View.GONE);
            SecondViewHolder.lookAuthorRedTv.setVisibility(View.VISIBLE);
            SecondViewHolder.lookAllTv.setTextColor(getResources().getColor(R.color.colorTextG3));
            SecondViewHolder.lookAuthorTv.setTextColor(getResources().getColor(R.color.colorTextG4));
        } else {
            SecondViewHolder.lookAllRedTv.setVisibility(VISIBLE);
            SecondViewHolder.lookAuthorRedTv.setVisibility(View.GONE);
            SecondViewHolder.lookAllTv.setTextColor(getResources().getColor(R.color.colorTextG4));
            SecondViewHolder.lookAuthorTv.setTextColor(getResources().getColor(R.color.colorTextG3));
        }
        selectComment();
    }


    private void getComment() {

        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.COMMENT_QUERY, true);

        map.put("objectId", subjectId);

        map.put("objectType", CommentTypeEnum.SUBJECT.name());

        map.put("currentPage", String.valueOf(currentPage));

        map.put("orderBy", mOrderBy.name());

        if (onlyLookLouZhuComment) {
            map.put("userId", userId);
        }
        httpRequestHelper.startRequest(map);
    }


    /**
     * 设置刷新布局
     */
    private void setRefreshLayout(Activity activity) {

        mRefreshEmptyViewHelper = new RefreshEmptyViewHelper(activity, new RefreshEmptyViewHelper.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestType = REFRESH;

                currentPage = DEFAULT_PAGE;

                onlyLookLouZhuComment = false;

                setSelectView();

                getSubject();

                firstHeaderViewHolder.impressionLayout.getImpressionTagInit(false, subjectId);
            }

            @Override
            public void onLoadMore() {

                mRequestType = LOAD_MORE;

                if (currentPage >= maxPages) {

                    LibAppUtil.showTip(TransmitDetailsActivity.this, "已经到最后一页了!!");

                    mRefreshEmptyViewHelper.closeRefresh();

                } else {

                    currentPage++;

                    getComment();
                }
            }

            @Override
            public void onEmptyRefresh() {

            }
        }, getResources().getDrawable(R.drawable.icon_no_content_about_match_schedule_detail));

        refreshListView.setDividerHeight(0);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (mAdapter != null) {
                mAdapter.clickScreen();
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 设置headView的显示
     *
     * @param subjectSimpleBean
     */
    private void setHeadView(final SubjectBean2 subjectSimpleBean) {

        subjectSimpleBean.gmtCreate = (TimeUtils.formatTime(mDetailBean.getNowDate(), subjectSimpleBean.gmtCreate));

        firstHeaderViewHolder.timeTv.setText(subjectSimpleBean.gmtCreate);

        Picasso.with(this).load(subjectSimpleBean.userLogoUrl).into(firstHeaderViewHolder.portraitIv);

        firstHeaderViewHolder.portraitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageActivity.startHomepageActivity(TransmitDetailsActivity.this, subjectSimpleBean.userId);
            }
        });
        firstHeaderViewHolder.timeTv.setText(subjectSimpleBean.gmtCreate);

        firstHeaderViewHolder.userNameTv.setText(subjectSimpleBean.loginName);
        firstHeaderViewHolder.medalLayout.removeAllViews();
        for (UserMedalLevelBean medalLevel : mDetailBean.userMedalLevelList) {
            firstHeaderViewHolder.medalLayout.addMedal(SubjectMedalEnum.getMedalResourceId(this, medalLevel.medalConfigCode, medalLevel.currentMedalLevelConfigCode));
        }
        if (subjectSimpleBean.title != null) {
            firstHeaderViewHolder.titleTv.setVisibility(View.VISIBLE);
            firstHeaderViewHolder.titleTv.setText(subjectSimpleBean.title);
        } else {
            firstHeaderViewHolder.titleTv.setVisibility(View.GONE);
        }

        if (StringUtils.isBlank(subjectSimpleBean.content)) {
            firstHeaderViewHolder.contentTv.setVisibility(View.GONE);
        } else {
            firstHeaderViewHolder.contentTv.setVisibility(VISIBLE);
            DetailsHtmlShowUtils.setHtmlText(firstHeaderViewHolder.contentTv, StringEscapeUtils.unescapeHtml(subjectSimpleBean.content));
        }

        SubjectObjectSubType2 subType = SubjectObjectSubType2.safeEnumOf(subjectSimpleBean.objectType.getName());

        View.OnClickListener clickListener = null;

        View.OnClickListener toLiveRoomClickListener = null;
        boolean showCardLayout = true;
        switch (subType){
            case MARK_ATTITUDE_MSG_SCREEN://弹幕被标记为态度
                showCardLayout = false;
            case MARK_ATTITUDE_COMMENT://评论被标记为态度
            case MARK_ATTITUDE_PERSONAL_ARTICLE://个人文章被标记为态度
                //处理去该节目
                final String channelId = subjectSimpleBean.properties.channelId != null ?
                        subjectSimpleBean.properties.channelId : subjectSimpleBean.sourceSubjectSimple.properties.channelId;
                if (channelId != null) {
                    toLiveRoomClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LiveActivity.startLiveActivity(TransmitDetailsActivity.this,
                                    Integer.parseInt(channelId));
                        }
                    };
                }
                break;
            case TRANSMIT_SUBJECT://转发话题
                break;
        }

        subType = SubjectObjectSubType2.safeEnumOf(subjectSimpleBean.sourceSubjectSimple != null ?
                subjectSimpleBean.sourceSubjectSimple.objectType.getName() : subjectSimpleBean.objectType.getName());

        switch (subType) {
            case NORMAL:
                cardShowSourceDefaultResId = R.drawable.ic_dynmic_speak;//普通说说
            case ARTICLE://文章，
            case PERSONAL_ARTICLE:  //个人文章
            case MARK_ATTITUDE_PERSONAL_ARTICLE://个人文章被标记态度                //---
            case MARK_ATTITUDE_COMMENT://评论被标记态度
                cardShowSourceTitle = subjectSimpleBean.sourceSubjectSimple.title;
                cardShowSourceSummary = subjectSimpleBean.sourceSubjectSimple.summary;
                if (cardShowSourceDefaultResId == 0) {
                    cardShowSourceDefaultResId = R.drawable.ic_dynmic_article;
                }
                if (mDetailBean.getSubjectSimple().sourceSubjectSimple != null) {
                    SubjectProperties properties = mDetailBean.getSubjectSimple().sourceSubjectSimple.properties;
                    if (properties != null) {
                        String firstCardType = properties.subjectFirstCardType;
                        if (TextUtils.equals(SubjectPropertiesType.INSERT_PICTURE, firstCardType)) {
                            //普通话题插入图片
                            cardShowSourceImageUrl = properties.midImageUrl == null ? "" : (properties.midImageUrl.get(0));
                        }
                    }
                }
                clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SubjectDetailActivity.startSubjectDetailActivity(TransmitDetailsActivity.this, subjectSimpleBean.sourceSubjectSimple.subjectId);
                    }
                };

                break;
            case MARK_ATTITUDE_MSG_SCREEN://弹幕被标记上态度
                cardShowSourceDefaultResId = R.drawable.ic_dynmic_comment;
                break;
            case MARK_ATTITUDE_ROCKETS: //火箭
                cardShowSourceDefaultResId = R.drawable.ic_dynmic_rocket;
                break;
            case EVENT_WIN_PK://PK
                cardShowSourceDefaultResId = R.drawable.ic_dynmic_pk;
                break;
            case EVENT_WIN_CHEER: //助威
                cardShowSourceDefaultResId = R.drawable.ic_dynmic_cheer;
                SubjectProperties properties = null;
                if (mDetailBean.getSubjectSimple() != null) {
                    if (mDetailBean.getSubjectSimple().sourceSubjectSimple != null) {
                        properties = mDetailBean.getSubjectSimple().sourceSubjectSimple.properties;
                    } else {
                        properties = mDetailBean.getSubjectSimple().properties;
                    }
                    if (properties != null) {
                        final String raceId = properties.basketBallRaceId != null ? properties.basketBallRaceId : properties.footBallRaceId;

                        final String raceType = properties.basketBallRaceId != null ? "BASKETBALL" : "FOOTBALL";

                        clickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ScheduleDetailsActivity.startScheduleDetailsActivity(TransmitDetailsActivity.this, raceId, raceType);
                            }
                        };
                    }
                }
                break;
            case EVENT_SEND_COUPON://发红包
                cardShowSourceDefaultResId = R.drawable.ic_dynmic_coupon;
                break;
            case EVENT_SPONSOR_DRAW://赞助抽奖
                cardShowSourceDefaultResId = R.drawable.ic_dynmic_lucky_draw;
                break;
            case EVENT_REWARD_ANCHOR: //打赏主播
                if (subjectSimpleBean.sourceSubjectSimple != null && subjectSimpleBean.sourceSubjectSimple.properties != null) {
                    cardShowSourceImageUrl = subjectSimpleBean.sourceSubjectSimple.properties.giftUrl;
                } else if (subjectSimpleBean.properties != null) {
                    cardShowSourceImageUrl = subjectSimpleBean.properties.giftUrl;
                }
                cardShowSourceDefaultResId = R.drawable.ic_dynamic_gift_default;
                break;
            case XJQ_VIDEO:
                cardShowSourceDefaultResId = R.drawable.ic_trans_dynmic_video;

                if (mDetailBean.getSubjectSimple() != null) {
                    if (mDetailBean.getSubjectSimple().sourceSubjectSimple != null) {
                        firstFrameUrl = mDetailBean.getSubjectSimple().sourceSubjectSimple.properties.videoFirstFrameImageUrl;
                        videoUrl = mDetailBean.getSubjectSimple().sourceSubjectSimple.properties.videoUrl;
                    } else {
                        firstFrameUrl = mDetailBean.getSubjectSimple().properties.videoFirstFrameImageUrl;
                        videoUrl = mDetailBean.getSubjectSimple().properties.videoUrl;
                    }
                }
                clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LiveWallDetailActivity.startLiveWallDetailActivity(TransmitDetailsActivity.this, mDetailBean.getSubjectSimple().sourceSubjectSimple.subjectId);
                    }
                };
                break;
        }

        if (toLiveRoomClickListener != null) {
            firstHeaderViewHolder.toLiveIv.setVisibility(VISIBLE);
            firstHeaderViewHolder.toLiveIv.setOnClickListener(toLiveRoomClickListener);
        } else {
            firstHeaderViewHolder.toLiveIv.setVisibility(View.GONE);
        }

        if(!showCardLayout){//只有弹幕上态度什么都不显示
            firstHeaderViewHolder.transCardLayout.setVisibility(View.GONE);
            firstHeaderViewHolder.deleteLayout.setVisibility(View.GONE);
        }
        else if (subjectSimpleBean.sourceSubjectSimple != null && subjectSimpleBean.sourceSubjectSimple.deleted) {
            firstHeaderViewHolder.transCardLayout.setVisibility(View.GONE);
            firstHeaderViewHolder.deleteLayout.setVisibility(VISIBLE);
            firstHeaderViewHolder.deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(TransmitDetailsActivity.this, "抱歉,该内容以被删除", 1000);
                }
            });

        } else {
            firstHeaderViewHolder.transCardLayout.setVisibility(VISIBLE);
            firstHeaderViewHolder.deleteLayout.setVisibility(View.GONE);
            if (clickListener == null) {
                clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SubjectProperties properties = subjectSimpleBean.sourceSubjectSimple != null ?
                                subjectSimpleBean.sourceSubjectSimple.properties != null ? subjectSimpleBean.sourceSubjectSimple.properties : null
                                :
                                subjectSimpleBean.properties != null ? subjectSimpleBean.properties : null;
                        if (properties != null && properties.channelId != null) {
                            LiveActivity.startLiveActivity(TransmitDetailsActivity.this, Integer.parseInt(properties.channelId));
                        }
                    }
                };
            }
            firstHeaderViewHolder.transCardLayout.setOnClickListener(clickListener);

            if (cardShowSourceImageUrl != null) {
                Picasso.with(this).load(cardShowSourceImageUrl).into(firstHeaderViewHolder.transIv);
            } else {
                if (cardShowSourceDefaultResId != 0) {
                    Picasso.with(this).load(cardShowSourceDefaultResId).into(firstHeaderViewHolder.transIv);
                }
            }

            if (!TextUtils.isEmpty(cardShowSourceTitle)) {
                firstHeaderViewHolder.transTitleTv.setVisibility(VISIBLE);
                firstHeaderViewHolder.transTitleTv.setText(cardShowSourceTitle);
                firstHeaderViewHolder.transContentTv.setMaxLines(1);
            } else {
                firstHeaderViewHolder.transContentTv.setMaxLines(2);
                firstHeaderViewHolder.transTitleTv.setVisibility(View.GONE);
            }

            if (cardShowSourceSummary == null) {
                cardShowSourceSummary = (subjectSimpleBean.sourceSubjectSimple != null) ? subjectSimpleBean.sourceSubjectSimple.memo : subjectSimpleBean.memo;
            }

            DetailsHtmlShowUtils.setHtmlText(firstHeaderViewHolder.transContentTv, cardShowSourceSummary, false, null);
        }
    }


    private void responseSuccessSubjectDetail(JSONObject jo) {
        mDetailBean = new Gson().fromJson(jo.toString(), SubjectDetailHeadBean.class);

        mDetailBean.operatorData();

        if (mDetailBean.getShareUrl() != null) {
            shareIv.setVisibility(VISIBLE);
            moreIv.setVisibility(VISIBLE);
        }

        final SubjectBean2 subjectSimpleBean = mDetailBean.getSubjectSimple();

        mAdapter.setLouZhuId(subjectSimpleBean.userId);

        this.userId = subjectSimpleBean.userId;

        setHeadView(subjectSimpleBean);

        firstHeaderViewHolder.userNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageActivity.startHomepageActivity(TransmitDetailsActivity.this, subjectSimpleBean.userId);
            }
        });

        firstHeaderViewHolder.portraitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageActivity.startHomepageActivity(TransmitDetailsActivity.this, subjectSimpleBean.userId);
            }
        });
        mAdapter.setObjectType(CommentTypeEnum.SUBJECT.name());
        mAdapter.setSubjectId(subjectSimpleBean.subjectId);

        detailBottomViewUtil.setData(subjectId,
                mDetailBean.getSubjectSimple().replyCount,
                mDetailBean.getSubjectSimple().likeCount,
                mDetailBean.getSubjectSimple().liked,
                ObjectTypeEnum.SUBJECT.name(),
                String.valueOf(mDetailBean.getSubjectSimple().subjectId),
                mDetailBean.getSubjectSimple().commentOff);

        getComment();
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

    private void responseSuccessCommentQuery(JSONObject jo) {

        UserCommentBean bean = new UserCommentBean(jo);

        maxPages = bean.getPaginator().getPages();

        for (int i = 0; i < bean.getCommentList().size(); i++) {

            UserCommentBean.CommentListBean commentBean = bean.getCommentList().get(i);

            commentBean.setContent(DetailsHtmlShowUtils.getFormatHtmlContent(this, commentBean.getContent()));
        }

        if (mRequestType == REFRESH) {
            commentList.clear();
        }

        commentList.addAll(bean.getCommentList());

        mAdapter.notifyDataSetChanged();
        //第一次进入这个界面，只做一次操作，之后都不做这个操作了。
        if (mFirstLoad) {
            mFirstLoad = false;
//          viewHolder.webView.loadDataWithBaseURL(null, mSubjectContent, "text/html", "utf-8", null);
            detailBottomViewUtil.setReplyLocationListener(locationListener);
            detailBottomViewUtil.setRequestFailedListener(requestFailedListener);
            container.addView(detailBottomViewUtil.getView(this), 0);
        }

        mRefreshEmptyViewHelper.closeRefresh();
    }

    OnMyClickListener locationListener = new OnMyClickListener() {
        @Override
        public void onClick(View v) {
            refreshListView.setSelection(1);
        }
    };


    @Override
    public void onSuccess(RequestContainer request, Object o) {
        mainContentLayout.setVisibility(View.VISIBLE);
        switch ((XjqUrlEnum) request.getRequestEnum()) {
            case SUBJECT_DETAIL:
                responseSuccessSubjectDetail((JSONObject) o);
                break;
            case COMMENT_QUERY:
                responseSuccessCommentQuery((JSONObject) o);
                break;
            case TAG_CREATE:
                firstHeaderViewHolder.impressionLayout.getImpressionTagInit(false, subjectId);
                break;
        }
    }


    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        mRefreshEmptyViewHelper.closeRefresh();
        try {
            ErrorBean bean = new ErrorBean(jsonObject);

            if ("SUBJECT_HAS_BEEN_DELETED".equals(bean.getError().getName())) {

                mainContentLayout.setVisibility(View.GONE);
                emptyTipTv.setText("抱歉，该详情已删除");
                emptyLayout.setVisibility(View.VISIBLE);

            } else if ("ARTICLE_HAS_BEEN_DELETED".equals(bean.getError().getName())) {

                mainContentLayout.setVisibility(View.GONE);
                emptyTipTv.setText("抱歉，该详情已删除");
                emptyLayout.setVisibility(View.VISIBLE);
            } else {
                operateErrorResponseMessage(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    static class ViewHolder {
        @BindView(R.id.portraitIv)
        CircleImageView portraitIv;
        @BindView(R.id.vipIv)
        ImageView vipIv;
        @BindView(R.id.userNameTv)
        TextView userNameTv;
        @BindView(R.id.userInfoLayout)
        LinearLayout userInfoLayout;
        @BindView(R.id.analysisTv)
        TextView analysisTv;
        @BindView(R.id.timeTv)
        TextView timeTv;
        @BindView(R.id.item_title)
        TextView titleTv;
        @BindView(R.id.item_summary)
        HtmlEmojiTextView contentTv;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;
        @BindView(R.id.impressionLayout)
        ImpressionLayout impressionLayout;
        @BindView(R.id.transCard_container_layout)
        LinearLayout transCardLayout;
        @BindView(R.id.transform_iv)
        ImageView transIv;
        @BindView(R.id.transform_title)
        TextView transTitleTv;
        @BindView(R.id.transform_content)
        TextView transContentTv;
        @BindView(R.id.medalLayout)
        MedalLayout medalLayout;
        @BindView(R.id.authorIv)
        ImageView authorIv;
        @BindView(R.id.toLiveIv)
        ImageView toLiveIv;
        @BindView(R.id.transCard_delete_layout)
        LinearLayout deleteLayout;


        // WebView webView;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class SecondViewHolder {
        @BindView(R.id.lookAllRedTv)
        TextView lookAllRedTv;
        @BindView(R.id.lookAllLayout)
        LinearLayout lookAllLayout;
        @BindView(R.id.lookAuthorRedTv)
        TextView lookAuthorRedTv;
        @BindView(R.id.lookAllTv)
        TextView lookAllTv;
        @BindView(R.id.lookAuthorTv)
        TextView lookAuthorTv;
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
}

