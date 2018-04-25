package com.android.xjq.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShareDialogFragment;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.DetailsHtmlShowUtils;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.RefreshEmptyViewHelper;
import com.android.banana.commlib.utils.StringUtils;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.view.MedalLayout;
import com.android.banana.utils.KeyboardHelper;
import com.android.banana.view.ScrollListView;
import com.android.httprequestlib.RequestContainer;
import com.android.residemenu.lt_lib.enumdata.FtRaceStatusEnum;
import com.android.xjq.R;
import com.android.xjq.activity.homepage.HomePageActivity;
import com.android.xjq.adapter.comment.CommentAdapter;
import com.android.xjq.bean.SubjectBean2;
import com.android.xjq.bean.SubjectProperties;
import com.android.xjq.bean.UserMedalLevelBean;
import com.android.xjq.bean.comment.UserCommentBean;
import com.android.xjq.bean.subject.SubjectDetailHeadBean;
import com.android.xjq.model.SubjectPropertiesType;
import com.android.xjq.model.comment.CommentOrderByEnum;
import com.android.xjq.model.comment.CommentTypeEnum;
import com.android.xjq.model.comment.ObjectTypeEnum;
import com.android.xjq.model.jcdx.JCDXArticleTypeEnum;
import com.android.xjq.utils.SharePopUpWindowHelper;
import com.android.xjq.utils.SubjectUtils2;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.details.DetailBottomViewUtils;
import com.android.xjq.utils.details.DetailReportPopupWindow;
import com.android.xjq.utils.details.DetailsWebViewShowUtils;
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

/**
 * 话题详情页
 */
public class SubjectDetailActivity extends BaseActivity implements IHttpResponseListener {

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
    @OnClick(R.id.moreIv)
    public void moreClick() {
        mDetailReportPopupWindow.showPopupWindow();
    }

    private SharePopUpWindowHelper.Builder builder;

    private String subjectId;

    private List<UserCommentBean.CommentListBean> commentList = new ArrayList<>();

    private CommentAdapter mAdapter;

    private WrapperHttpHelper httpRequestHelper;

    private ViewHolder viewHolder;

    private SecondViewHolder SecondViewHolder;

    private String userId;

    private SubjectDetailHeadBean mDetailBean;

    private CommentOrderByEnum mOrderBy;

    //todo
    private String mSubjectContent;

    private DetailBottomViewUtils detailBottomViewUtil;
    /**
     * 第一次加载
     */
    private boolean mFirstLoad = true;

    private boolean onlyLookLouZhuComment = false;
    @OnClick(R.id.shareIv)
    public void share() {
//        if (builder == null) {
//            //分享
//            builder = new SharePopUpWindowHelper.Builder(this)
//                    .setLandscape(false)
//                    .setShareUrl(mDetailBean.getShareUrl())
//                    .setHostImageUrl(null)
//                    .setShowTitle(false)
//                    .setObjectType(ObjectTypeEnum.SUBJECT)
//                    .setContent(mDetailBean.getSubjectSimple().summary)
//                    .setTitle(mDetailBean.getSubjectSimple().title);
//        }
//        builder.builder().show();


        String  articleUrl = null;
        SubjectProperties properties = mDetailBean.getSubjectSimple().properties;
        if(properties!=null){
            String firstCardType = properties.subjectFirstCardType;
            if (TextUtils.equals(SubjectPropertiesType.INSERT_PICTURE, firstCardType)) {
                //普通话题插入图片
                articleUrl =properties.midImageUrl == null ? "" : (properties.midImageUrl.get(0));
            }
        }

        final ShareDialogFragment shareDialog = ShareDialogFragment.newInstance();
        shareDialog.show(getSupportFragmentManager());
        final String finalArticleUrl = articleUrl;

        shareDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SubjectUtils2.showShareDialog(SubjectDetailActivity.this,
                        v,
                        mDetailBean.getSubjectSimple().loginName,subjectId,
                        "SUBJECT",
                        mDetailBean.getSubjectSimple().title,
                        mDetailBean.getSubjectSimple().summary,
                        finalArticleUrl,
                        mDetailBean.getSubjectSimple().title,
                        mDetailBean.getSubjectSimple().objectType.getName().equals("NORMAL")?
                                R.drawable.ic_dynmic_speak: R.drawable.ic_dynmic_article);

                shareDialog.dismiss();
            }
        });

    }



    public static void startSubjectDetailActivity(Context context, String subjectId) {

        Intent intent = new Intent();

        intent.setClass(context, SubjectDetailActivity.class);

        intent.putExtra("subjectId", subjectId);

        context.startActivity(intent);
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

        mDetailReportPopupWindow = new DetailReportPopupWindow(this,moreIv);

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
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    addImpressionEt.clearFocus();
                    KeyboardHelper.hideSoftInput(addImpressionEt);
                    if (!TextUtils.isEmpty(addImpressionEt.getText().toString())){
                        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.TAG_CREATE, true);
                        map.put("objectId", subjectId);
                        map.put("objectType", "SUBJECT");
                        map.put("tagName", addImpressionEt.getText().toString());
                        httpRequestHelper.startRequest(map, true);
                        addImpressionEt.setText("");
                    }
                    addImpressionLayout.setVisibility(View.GONE);
                }
                return false;
            }
        });

        getSubject();

        viewHolder.impressionLayout.setShowAddLayout(true, addImpressionListener);

        viewHolder.impressionLayout.getImpressionTagInit(false,subjectId);

        viewHolder.authorIv.setVisibility(VISIBLE);
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

        SecondViewHolder.onlyLookAuthorLayout.setVisibility(VISIBLE);
        SecondViewHolder.lookAllRedTv.setVisibility(VISIBLE);
        SecondViewHolder.lookAuthorRedTv.setVisibility(View.GONE);

        SecondViewHolder.onlyLookAuthorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onlyLookLouZhuComment = true;
                setSelectView();
            }
        });

        SecondViewHolder.lookAllLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlyLookLouZhuComment = false;
                setSelectView();
            }
        });

        return secondHeaderView;
    }

    private View getHeadView() {
        //评论头部
        View headerView = getLayoutInflater().inflate(R.layout.layout_subject_detail_header, null);

        viewHolder = new ViewHolder(headerView);

        WebView webView = new WebView(getApplicationContext());

        webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dp50)));

        viewHolder.mainLayout.addView(webView, 3);

        viewHolder.impressionLayout.setParentLayoutBg(R.color.normal_bg);

        viewHolder.webView = webView;

        DetailsWebViewShowUtils.setWebView(SubjectDetailActivity.this, viewHolder.webView);

        return headerView;
    }

    OnMyClickListener addImpressionListener = new OnMyClickListener() {
        @Override
        public void onClick(View v) {
            if(addImpressionLayout.getVisibility()==View.GONE){
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


    private void getSubject() {

        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.SUBJECT_DETAIL, true);

        map.put("subjectId", subjectId);

        httpRequestHelper.startRequest(map, true);
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

                viewHolder.impressionLayout.getImpressionTagInit(false,subjectId);
            }

            @Override
            public void onLoadMore() {

                mRequestType = LOAD_MORE;

                if (currentPage >= maxPages) {

                    LibAppUtil.showTip(SubjectDetailActivity.this, "已经到最后一页了!!");

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


    /**
     * 竞彩大学名片的显示
     */
    private void setArticleView() {

        setArticleType();

        boolean isBtRace = true;

        JczqDataBean raceDataBean = null;

        if (mDetailBean.getBasketballRace() != null) {

            raceDataBean = mDetailBean.getBasketballRace();

            isBtRace = true;

        } else if (mDetailBean.getFootballRace() != null) {

            raceDataBean = mDetailBean.getFootballRace();
            isBtRace = false;
        }
        if (raceDataBean != null) {

            viewHolder.JcdxCardLayout.setVisibility(VISIBLE);
            boolean isFinish = raceDataBean == null ? true : FtRaceStatusEnum.saveValueOf(raceDataBean.getRaceStatus().getName()) == FtRaceStatusEnum.FINISH;

            String homeTeamName = raceDataBean.getHomeTeamName();

            if (isBtRace && raceDataBean.getHomeTeamName() != null && raceDataBean.getHomeTeamName().length() >= 3) {
                homeTeamName = raceDataBean.getHomeTeamName().substring(0, 2);
            }
            String rightName = isBtRace ? homeTeamName + "[主]" : raceDataBean.getGuestTeamName();

            String leftName = isBtRace ? raceDataBean.getGuestTeamName() : raceDataBean.getHomeTeamName();
            String leftUrl = isBtRace ? raceDataBean.getBTGuestLogoUrl() : raceDataBean.getFTHomeLogoUrl();
            String rightUrl = isBtRace ? raceDataBean.getBTHomeLogoUrl() : raceDataBean.getFTGuestLogoUrl();

            String total = "总分:" + (raceDataBean.getFullHomeScore() + raceDataBean.getFullGuestScore());
            String diff = "分差:" + (raceDataBean.getFullHomeScore() - raceDataBean.getFullGuestScore());
            String fullScore = isBtRace ? (raceDataBean.getFullGuestScore() + ":" + raceDataBean.getFullHomeScore())
                    : (raceDataBean.getFullHomeScore() + ":" + raceDataBean.getFullGuestScore());

            viewHolder.matchName.setText(raceDataBean.getMatchName() + "\n" + TimeUtils.format(TimeUtils.LONG_DATEFORMAT, TimeUtils.MATCH_FORMAT, raceDataBean.getGmtStart()));

            viewHolder.articleTvnameLeft.setText(leftName);
            viewHolder.articleTvnameRight.setText(rightName);

            Picasso.with(this).load(leftUrl).into(viewHolder.articleIvLeft);
            Picasso.with(this).load(rightUrl).into(viewHolder.articleIvRight);

            if (isBtRace) {
                viewHolder.articleMatchTotal.setText(isFinish ? total : "");
                viewHolder.articleMatchDiff.setText(isFinish ? diff : "");
                viewHolder.articleMatchStatus.setText(isFinish ? fullScore : "VS");
            } else {
                viewHolder.articleMatchStatus.setText(isFinish ? fullScore : "VS");
                viewHolder.articleMatchTotal.setText("");
                viewHolder.articleMatchDiff.setText("");
            }
        } else {
            viewHolder.JcdxCardLayout.setVisibility(View.GONE);
        }
    }

    private void setArticleType() {

        if (mDetailBean.getJcdxType() != null) {
            viewHolder.jcdxArticleType.setVisibility(VISIBLE);
            switch (JCDXArticleTypeEnum.safeValueOf(mDetailBean.getJcdxType())) {

                case ARTICLE_ANALYSIS://分析
                    viewHolder.jcdxArticleType.setText("分析");
                    break;
                case ARTICLE_REPLAY://复盘
                    viewHolder.jcdxArticleType.setText("复盘");
                    break;

                case ARTICLE_PREDICT://预测
                    viewHolder.jcdxArticleType.setText("预测");
                    break;

                case ARTICLE_PERSONAL_EXP://经验
                    viewHolder.jcdxArticleType.setText("经验");
                    break;
                default:
                    break;
            }
        }
    }

    private void setHeadView( SubjectBean2 subjectSimpleBean){
        subjectSimpleBean.gmtCreate=(TimeUtils.formatTime(mDetailBean.getNowDate(), subjectSimpleBean.gmtCreate));

        viewHolder.timeTv.setText(subjectSimpleBean.gmtCreate);

        Picasso.with(this).load(subjectSimpleBean.userLogoUrl).into(viewHolder.portraitIv);

        viewHolder.timeTv.setText(subjectSimpleBean.gmtCreate);
        viewHolder.userNameTv.setText(subjectSimpleBean.loginName);
        viewHolder.medalLayout.removeAllViews();
        for (UserMedalLevelBean medalLevel : mDetailBean.userMedalLevelList) {
            viewHolder.medalLayout.addMedal(SubjectMedalEnum.getMedalResourceId(this, medalLevel.medalConfigCode, medalLevel.currentMedalLevelConfigCode));
        }
        viewHolder.userNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageActivity.startHomepageActivity(SubjectDetailActivity.this, userId);
            }
        });

        viewHolder.portraitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageActivity.startHomepageActivity(SubjectDetailActivity.this, userId);
            }
        });

        if (StringUtils.isBlank(subjectSimpleBean.title)) {
            viewHolder.titleTv.setVisibility(View.GONE);
        } else {
            viewHolder.titleTv.setVisibility(VISIBLE);

//            SpannableStringBuilder ssb = new SpannableStringBuilder();
//
//            ssb.append(SubjectUtils.getDingEliteHotIcon(this, subjectSimpleBean.isSetTop(), subjectSimpleBean.isElite()));

//            viewHolder.titleTv.setText(ssb.append(subjectSimpleBean.getTitle()));
            viewHolder.titleTv.setText(subjectSimpleBean.title);
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

        final String userId = subjectSimpleBean.userId;

        setArticleView();

        mAdapter.setLouZhuId(userId);

        this.userId = userId;


        setHeadView(subjectSimpleBean);

//        if (subjectSimpleBean.getAnalysisCode() != null) {
//            viewHolder.analysisTv.setVisibility(VISIBLE);
//
//            viewHolder.analysisTv.setText(SubjectTypeEnum.SubjectSubTypeEnum.valueOf(subjectSimpleBean.getAnalysisCode()).getMessage());
//
//        } else {
//
//            viewHolder.analysisTv.setVisibility(View.GONE);
//        }
        mAdapter.setObjectType(CommentTypeEnum.SUBJECT.name());

        mAdapter.setSubjectId(subjectSimpleBean.subjectId);

        String content = "";

        if (subjectSimpleBean.content != null) {

            content = subjectSimpleBean.content;
        }

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        if (StringUtils.isBlank(subjectSimpleBean.title)) {

            if (subjectSimpleBean.setTop) {
                ssb.append("<img class=\"icon-label\" src=\"file:///android_asset/icon_ding.png\"/>");
            }

        }
        mSubjectContent = DetailsHtmlShowUtils.getFormatHtmlContent(this, ssb.append(content).toString());

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
            viewHolder.webView.loadDataWithBaseURL(null, mSubjectContent, "text/html", "utf-8", null);
            detailBottomViewUtil.setReplyLocationListener(locationListener);
            detailBottomViewUtil.setRequestFailedListener(requestFailedListener);
            container.addView(detailBottomViewUtil.getView(this),0);
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
                viewHolder.impressionLayout.getImpressionTagInit(false,subjectId);
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
                emptyTipTv.setText("抱歉，该话题已删除");
                emptyLayout.setVisibility(View.VISIBLE);

            } else if ("ARTICLE_HAS_BEEN_DELETED".equals(bean.getError().getName())) {
                mainContentLayout.setVisibility(View.GONE);
                emptyTipTv.setText("抱歉，该文章已删除");
                emptyLayout.setVisibility(View.VISIBLE);

            } else {
                operateErrorResponseMessage(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_UP){
            if(mAdapter!=null){
                mAdapter.clickScreen();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    static class ViewHolder {
        @BindView(R.id.jcdxArticleType)
        TextView jcdxArticleType;
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
        @BindView(R.id.titleTv)
        TextView titleTv;
        @BindView(R.id.match_name)
        TextView matchName;
        @BindView(R.id.artical_iv_left)
        CircleImageView articleIvLeft;
        @BindView(R.id.artical_tvname_left)
        TextView articleTvnameLeft;
        @BindView(R.id.artical_match_total)
        TextView articleMatchTotal;
        @BindView(R.id.artical_match_status)
        TextView articleMatchStatus;
        @BindView(R.id.artical_match_diff)
        TextView articleMatchDiff;
        @BindView(R.id.artical_iv_right)
        CircleImageView articleIvRight;
        @BindView(R.id.artical_tvname_right)
        TextView articleTvnameRight;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;
        @BindView(R.id.JcdxCardLayout)
        LinearLayout JcdxCardLayout;
        @BindView(R.id.impressionLayout)
        ImpressionLayout impressionLayout;
        @BindView(R.id.medalLayout)
        MedalLayout medalLayout;
        WebView webView;
        @BindView(R.id.authorIv)
        ImageView authorIv;

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

