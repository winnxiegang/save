package com.android.banana.commlib.liveScore;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.bean.liveScoreBean.LiveScore5MatchRecently;
import com.android.banana.commlib.bean.liveScoreBean.PathAnimEventResultBean;
import com.android.banana.commlib.bean.liveScoreBean.PathAnimResult;
import com.android.banana.commlib.http.AppParam;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.liveScore.livescoreEnum.AnimTypeEnum;
import com.android.banana.commlib.liveScore.livescoreEnum.FtRaceStatusEnum;
import com.android.banana.commlib.liveScore.livescoreEnum.LiveScoreUrlEnum;
import com.android.banana.commlib.liveScore.view.GsLiveMatchTextView;
import com.android.banana.commlib.liveScore.view.LinearLayoutParent;
import com.android.banana.commlib.liveScore.view.PathAnimView;
import com.android.banana.commlib.liveScore.view.ThumbSeekBar;
import com.android.banana.commlib.utils.AnimationUtils;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaomu on 2017/8/16.
 */

public class PathAnimHeaderController extends BaseController4JCZJ<BaseActivity> implements IHttpResponseListener, View.OnClickListener {
    LinearLayoutParent linearLayoutParent;

    TextView mTitleTv, mStatusTv, mGsLiveMatchTip, mLeftTipTv, mRightTipTv, mTeamLeftTv, mTeamRightTv,
            mAnimActionTv, playBackTv, mTimeLeft, mTimeRight, teamInfoTip;
    ImageView mCloseIv, mStatusFtIv, mPlayPreviousIv, mPlayPauseIv, mPlayResumeTv,
            mPlayForwardIv, mTeamLeftIv, mTeamRightIv, mAnimActionIv;
    LinearLayout mRecentLayout, mPlayEventLayout, mTeamInfoLayout, mStatusLayout;
    GsLiveMatchTextView mGsLiveMatchLeftTV, mGsLiveMatchRightTv;
    FrameLayout mPlayLayout;
    ThumbSeekBar mSeekbar;
    PathAnimView mPathAnimView;

/*----外部传递过来的----*/

    private String mRaceId;
    private JczqDataBean mRaceDataBean;
    private NormalObject mStatus;
    // 网络请求

    private WrapperHttpHelper httpHelper;
    //handler

    private final static int POST_DELAY_BEGIN = 0;
    private final static int POST_DELAY_HIDE_UI = 1;
    private final static int POST_DELAY_LOOP = 2;
    private final static long INTERVAL = Conf.LOOP_INTERVAL;//轮询间隔时长
    private final static long ANIM_REAL_DURATION = 0L;//0代表每个动画的真实时间

    //动画播放

    private List<PathAnimEventResultBean.AnimEventBean> eventList = new ArrayList<>();

    private long duration = INTERVAL;//默认动画时长
    private long timestamp;//头部轮询第一次传0,之后传上一次成功返回的时间戳
    private boolean isReverse;//是否反转{近五场,以及比分显示}

    private boolean isPlayMode = false;//是否是回放模式
    private int curPlayIndex = 0;//回放模式下当前播放动作下标
    private boolean isPause = false;//回放模式下是否被暂停

    private final String mJczqDataId;
    private String mFullScore;
    private boolean isTeam, isLeft, isNextTeam, isNextLeft;//当前，和下一个动作的标识

    public PathAnimHeaderController(String raceId, JczqDataBean raceDetail) {
        mRaceId = raceId;
        mRaceDataBean = raceDetail;
        mJczqDataId = mRaceDataBean.getId();
        isReverse = mRaceDataBean.innerTeamReverse;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case POST_DELAY_BEGIN://如果还差5分钟开赛，那么 5分钟之后自动开始拉取比赛数据
                    switchLiveModeUI();
                case POST_DELAY_LOOP:
                    getAnimEventData();
                    break;
                case POST_DELAY_HIDE_UI://5s头部没点击动作 隐藏进度条
                    switchPlayModeUI(false, true);
                    break;
            }
        }
    };

    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.activity_jczq_analysis_path_anim_controller_header);
    }

    private void setListener() {
        mPlayPreviousIv.setOnClickListener(this);
        mPlayPauseIv.setOnClickListener(this);
        mPlayResumeTv.setOnClickListener(this);
        mPlayForwardIv.setOnClickListener(this);
        mCloseIv.setOnClickListener(this);
        playBackTv.setOnClickListener(this);
    }

    private void initView() {
        linearLayoutParent = findViewOfId(R.id.linearLayoutParent);
        mTitleTv = findViewOfId(R.id.titleTv);
        mStatusTv = findViewOfId(R.id.statusTv);
        mGsLiveMatchTip = findViewOfId(R.id.gsLiveMatchTip);
        mLeftTipTv = findViewOfId(R.id.left_tip);
        mRightTipTv = findViewOfId(R.id.right_tip);
        mTeamLeftTv = findViewOfId(R.id.teamLeftTv);
        mTeamRightTv = findViewOfId(R.id.teamRightTv);
        mAnimActionTv = findViewOfId(R.id.animActionTv);
        playBackTv = findViewOfId(R.id.playBackTv);
        mTimeLeft = findViewOfId(R.id.time_left);
        mTimeRight = findViewOfId(R.id.time_right);
        teamInfoTip = findViewOfId(R.id.teamInfoTip);

        mCloseIv = findViewOfId(R.id.closeIv);
        mStatusFtIv = findViewOfId(R.id.statusFtIv);
        mPlayPreviousIv = findViewOfId(R.id.play_previous);
        mPlayPauseIv = findViewOfId(R.id.play_pause);
        mPlayResumeTv = findViewOfId(R.id.play_resume);
        mPlayForwardIv = findViewOfId(R.id.play_next);
        mTeamLeftIv = findViewOfId(R.id.teamLeftIv);
        mTeamRightIv = findViewOfId(R.id.teamRightIv);
        mAnimActionIv = findViewOfId(R.id.animActionIv);

        mRecentLayout = findViewOfId(R.id.recent_view);
        mPlayEventLayout = findViewOfId(R.id.playEventLayout);
        mTeamInfoLayout = findViewOfId(R.id.teamInfoLayout);
        mStatusLayout = findViewOfId(R.id.statusLayout);

        mGsLiveMatchLeftTV = findViewOfId(R.id.gsLiveMatchLeftTV);
        mGsLiveMatchRightTv = findViewOfId(R.id.gsLiveMatchRightTv);

        mPlayLayout = findViewOfId(R.id.playLayout);
        mSeekbar = findViewOfId(R.id.seekbar);
        mPathAnimView = findViewOfId(R.id.pathAnimView);
    }

    @Override
    public void onSetUpView() {
        initView();
        setListener();
        onQueryStatus();
        createCircularReveal(true);
        //底部队伍信息一栏
        PicUtils.load(getContext(), mTeamLeftIv, mRaceDataBean.getFTHomeLogoUrl());
        PicUtils.load(getContext(), mTeamRightIv, mRaceDataBean.getFTGuestLogoUrl());
        mTeamLeftTv.setText(mRaceDataBean.getHomeTeamName());
        mTeamRightTv.setText(mRaceDataBean.getGuestTeamName());
        mTitleTv.setText(mRaceDataBean.matchName + " " + TimeUtils.formatStringDate(mRaceDataBean.startDate, TimeUtils.MATCH_FORMAT));

        getContentView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                createCircularReveal(true);
                getContentView().removeOnLayoutChangeListener(this);
            }
        });
        getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switchPlayModeUI(true, true);
                return false;
            }
        });
        linearLayoutParent.setOndispatchtouchEventListener(new LinearLayoutParent.ondispatchtouchEventListener() {
            @Override
            public void onTouch(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    mHandler.removeMessages(POST_DELAY_HIDE_UI, null);
                if (event.getAction() == MotionEvent.ACTION_UP)
                    mHandler.sendEmptyMessageDelayed(POST_DELAY_HIDE_UI, 5000);
            }
        });

        //mSeekbar.setDragEnable(false);
        boolean isLollopop = Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, isLollopop ? -1 : -2);
        params.weight = 1;
        mSeekbar.setLayoutParams(params);
        mSeekbar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        mSeekbar.setThumbOnSeekBarChangeListener(new ThumbSeekBar.OnThumbSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar, PathAnimEventResultBean.AnimEventBean animEventBean, int stopTrackingTouchIndex) {
                mHandler.sendEmptyMessageDelayed(POST_DELAY_HIDE_UI, 5000);
                curPlayIndex = stopTrackingTouchIndex--;
                onPlayResume();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeMessages(POST_DELAY_HIDE_UI, null);
                onPlayPause();
            }
        });

        mPathAnimView.setAnimatorUpdateCallback(new PathAnimView.AnimatorCallback() {
            @Override
            public void onUpdate(float percent) {
                if (percent >= 1.0f) {
                    onPlayNext();
                }
            }
        });

    }


    public void onQueryStatus() {
        mAnimActionIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.icon_no_sign_large));
        mAnimActionTv.setText(getString(R.string.no_sign_now));

        httpHelper = new WrapperHttpHelper(this);
        RequestFormBody formBody = new RequestFormBody(LiveScoreUrlEnum.SEASON_RACE_STATUS_QUERY);
        formBody.put("raceId", mRaceId);
        formBody.setRequestUrl(AppParam.FT_API_DOMAIN + AppParam.FT_API_S_URL);
        httpHelper.startRequest(formBody);
    }

    //切换到直播UI
    private void switchLiveModeUI() {
        mAnimActionIv.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.icon_no_sign_large));
        mAnimActionTv.setText(getString(R.string.no_sign_now));
        com.android.banana.commlib.utils.AnimationUtils.animateScale(mRecentLayout, 0, 0, 300, 0, false);
        AnimationUtils.animateScale(mStatusLayout, 1, 1, 0, 0, true);
        AnimationUtils.animateScale(mTeamInfoLayout, 1, 1, 300, 0, true);
        AnimationUtils.doLiveScoreFtAnimation(mStatusFtIv);
    }

    //切换 回放模式，比赛模式的ui   [includeTv] 是否包括回放【按钮/返回】 一起做动画
    private void switchPlayModeUI(boolean visible, boolean includeTv) {
        // Log.e(TAG, "switchPlayModeUI: " + visible);
        if (mSeekbar.isInTouch() || !isPlayMode)
            return;
        if (visible)
            mHandler.sendEmptyMessageDelayed(POST_DELAY_HIDE_UI, 5000);

        AnimationUtils.animateTranslate(mTeamInfoLayout, 0, visible ? mTeamInfoLayout.getHeight() : 0, 0, !visible);
        AnimationUtils.animateTranslate(mPlayEventLayout, 0, visible ? 0 : mPlayEventLayout.getHeight(), 0, visible);

        float scale = visible ? 1f : 0f;
        int duration = 500 / 4;
        AnimationUtils.animateScale(mPlayPreviousIv, scale, scale, duration, (visible ? 0 : 3) * duration, visible);
        AnimationUtils.animateScale(isPause ? mPlayResumeTv : mPlayPauseIv, scale, scale, duration, (visible ? 1 : 2) * duration, visible);
        AnimationUtils.animateScale(mPlayForwardIv, scale, scale, duration, (visible ? 2 : 1) * duration, visible);

        if (includeTv)
            AnimationUtils.animateScale(playBackTv, scale, scale, duration, (visible ? 3 : 0) * duration, visible);

    }

    private void setMatchStatus() {
        if (mStatus != null) {
            String mStatusName = mStatus.getName();
            if (TextUtils.equals(mStatusName, "FIRST_HALF_END"))
                mStatusName = "PAUSE";
            else if (TextUtils.equals(mStatusName, "MATCH_ENDED"))
                mStatusName = "FINISH";

            FtRaceStatusEnum statusEnum = FtRaceStatusEnum.saveValueOf(mStatusName);
            switch (statusEnum) {
                case FINISH: {  //完场
                    AnimationUtils.clearAnimation(mStatusFtIv);
                    AnimationUtils.animateScale(playBackTv, 1f, 1f, 300, 0, true);
                    AnimationUtils.animate(mStatusLayout, 0, 0, 0, 0, 0, 0, 4);
                    mAnimActionIv.setImageResource(R.drawable.icon_finish_large);
                    mAnimActionTv.setText(R.string.time_over);
                    playBackTv.setText(getString(R.string.turn_play_back));
                    mHandler.removeCallbacksAndMessages(null);
                    mPathAnimView.reset();
                }
                break;
                //case WAIT:  //等待开始
                case PLAY_F://上半场
                case PLAY_S://下半场
                case OVERTIME://加时
                    AnimationUtils.animateScale(mStatusLayout, 1, 1, 0, 0, true);
                    AnimationUtils.doLiveScoreFtAnimation(mStatusFtIv);
                    break;
                case DELAY:     //延时(比赛有效，以后还会开始)
                case DEC:       //待定(继续中状态,比分可能无效)
                case PAUSE: {
                    AnimationUtils.clearAnimation(mStatusFtIv);
                    mAnimActionIv.setImageResource(R.drawable.icon_pause_break);
                    mAnimActionTv.setText(mStatus.getMessage());
                }
                break;
                case CUT:       //腰斩(比赛进行中,比赛无效)
                case CANCEL:    //取消(比赛开始前,比赛无效)
                case POSTPONE: {
                    AnimationUtils.clearAnimation(mStatusFtIv);
                    mHandler.removeCallbacksAndMessages(null);
                    mAnimActionIv.setImageResource(R.drawable.icon_position_large);
                    mAnimActionTv.setText(mStatus.getMessage());
                }
                break;
            }
        }
    }

    //1.如果开赛时间已到,那么不断拉取赛事动作接口{FLASH_SCORE_QUERY}，
    //2.如果未赛未开始情况下 获取近五场战绩
    private void setMatchStatus4BeforeFinish() {
        long betweenTime = TimeUtils.date1SubDate2MsWithNoAbs(mRaceDataBean.startDate, mRaceDataBean.getNowDate());
        if (betweenTime > 0) {
            mHandler.sendEmptyMessageDelayed(POST_DELAY_BEGIN, betweenTime);
            getRecent5Match();
        } else {
            getAnimEventData();
        }
    }

    //设置头部结果
    private void setAnimEventDataResult(PathAnimResult PathAnimResult) {
        if (PathAnimResult == null)
            return;

        ArrayList<PathAnimEventResultBean.AnimEventBean> flashScoreList = PathAnimResult.flashScoreQueryResult.flashScoreList;
        if (flashScoreList == null || flashScoreList.size() == 0)
            return;
        eventList.clear();
        eventList.addAll(flashScoreList);
        AnimationUtils.doLiveScoreFtAnimation(mStatusFtIv);
        AnimationUtils.animateScale(mStatusLayout, 1, 1, 0, 0, true);
        //1.回放状态
        if (isPlayMode) {
            switchPlayModeUI(true, true);
            mTimeRight.setText(eventList.get(eventList.size() - 1).raceTime);
            mSeekbar.setDragEnable(true);//数据请求回来后,允许拖动
            mSeekbar.setHomeTeamId(mRaceDataBean.getInnerHomeTeamId());
            mSeekbar.setPathAnimEventList(eventList);
            duration = ANIM_REAL_DURATION;
        } else {
            //1.直播状态下设置比赛状态，动画播放时间
            //switchLiveModeUI();
            PathAnimEventResultBean.AnimEventBean animEventLast = eventList.get(eventList.size() - 1);
            if (timestamp == 0 || eventList.size() <= 1) {
                //2.第一次的播放因为只播放最后一个所以是播放它的真实时间,duration=0
                //3.之后的每次播放如果列表只有1个 那么也是播放它的真实时间,
                duration = ANIM_REAL_DURATION;

            } else if (eventList.size() > 1) {
                //4.如果之后的每次播放如果列表动作个数大于1 ,那么播放时间为3/size
                duration = INTERVAL / eventList.size();
            }
            //5.设置状态
            mStatus = animEventLast.actionType;
            setMatchStatus();
        }

        startAnimEventAnimation(isPlayMode ? true : timestamp != 0);
        this.timestamp = PathAnimResult.flashScoreQueryResult.timestamp;
    }

    //1.第一次请求接口 timestamp传0,那么请求回来的播放列表最后一条，并且清空掉多余的
    //2.之后的每次请求，包括回放模式都是按顺序播放
    private void startAnimEventAnimation(boolean order) {
        if (eventList.size() <= 0)
            return;

        PathAnimEventResultBean.AnimEventBean animEvent = null;
        if (isPlayMode) {
            animEvent = eventList.get(curPlayIndex);
        } else {
            animEvent = eventList.remove(order ? 0 : eventList.size() - 1);
        }
        if (!order)
            eventList.clear();
        LogUtils.e("PathAnim", "isPlayMode" + isPlayMode + " order=" + order +
                "  animEvent.guestTeamScore= " + animEvent.guestTeamScore + " animEvent.homeTeamScore=" + animEvent.homeTeamScore);
        mStatusTv.setText(animEvent.raceTime);
        teamInfoTip.setText(isReverse ? animEvent.guestTeamScore + ":" + animEvent.homeTeamScore : animEvent.homeTeamScore + ":" + animEvent.guestTeamScore);

        isTeam = TextUtils.equals("TEAM", animEvent.subjectType.getName());
        isLeft = isTeam && animEvent.actionSubject == mRaceDataBean.getInnerHomeTeamId();
        AnimTypeEnum typeEnum = AnimTypeEnum.safeValueOf(animEvent.actionType.getName());

        AnimTypeEnum nextAnim = checkNextAnim(typeEnum, animEvent.actionTime);
        //1.直播状态下集合最后的一个动作才允许循环,直到下一个动作播放
        //2.回放状态下不允许循环播放动画
        showMatchAnimTips(typeEnum, nextAnim);
        mSeekbar.updateProgress(animEvent.actionTime);
        mTimeLeft.setText(animEvent.raceTime);
        mPathAnimView.startAnimEvents(duration, !isPlayMode, isLeft, isNextLeft, typeEnum, nextAnim);
    }

    //检查下一个是可以一起播放
    private AnimTypeEnum checkNextAnim(AnimTypeEnum lastAnimType, long actionTime) {
        if (AnimTypeEnum.shouldSingle(lastAnimType))
            return null;
        if (eventList.size() <= 0 || (isPlayMode && curPlayIndex + 1 >= eventList.size()))
            return null;
        PathAnimEventResultBean.AnimEventBean animEvent = null;
        if (isPlayMode) {
            animEvent = eventList.get(curPlayIndex + 1);
        } else {
            animEvent = eventList.remove(0);
        }
        if (animEvent.actionTime != actionTime)
            return null;

        if (isPlayMode)
            curPlayIndex++;

        isNextTeam = TextUtils.equals("TEAM", animEvent.subjectType.getName());
        isNextLeft = isNextTeam && animEvent.actionSubject == mRaceDataBean.getInnerHomeTeamId();
        AnimTypeEnum typeEnum = AnimTypeEnum.safeValueOf(animEvent.actionType.getName());

        return typeEnum;
    }

    private void showMatchAnimTips(AnimTypeEnum... typeEnums) {
        AnimTypeEnum typeEnum1 = typeEnums[0];
        AnimTypeEnum typeEnum2 = typeEnums[1];
        if (typeEnum2 != null) {
            if (!isTeam && !isNextTeam) {
                AnimationUtils.animateScale(mLeftTipTv, 0, 0, 0, false);
                AnimationUtils.animateScale(mRightTipTv, 0, 0, 0, false);
                return;
            }
            boolean hideOne = isLeft == isNextLeft;
            showMatchAnimTip(typeEnum1, isLeft, hideOne);
            showMatchAnimTip(typeEnum2, isNextLeft, hideOne);
            return;
        }
        showMatchAnimTip(typeEnum1, isLeft, true);
    }

    private void showMatchAnimTip(AnimTypeEnum typeEnum1, boolean isLeft, boolean hideOne) {
        if (!isTeam) {
            AnimationUtils.animateScale(mLeftTipTv, 0, 0, 0, false);
            AnimationUtils.animateScale(mRightTipTv, 0, 0, 0, false);
            return;
        }

        if (isLeft) {
            AnimationUtils.animateScale(mLeftTipTv, 1, 1, 0, true);
            if (hideOne) AnimationUtils.animateScale(mRightTipTv, 0, 0, 0, false);
            mLeftTipTv.setText(mTeamLeftTv.getText() + " " + typeEnum1.getAction());
        } else {
            if (hideOne) AnimationUtils.animateScale(mLeftTipTv, 0, 0, 0, false);
            AnimationUtils.animateScale(mRightTipTv, 1, 1, 0, true);
            mRightTipTv.setText(mTeamRightTv.getText() + " " + typeEnum1.getAction());
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.play_previous) {
            onPlayPrevious();
        } else if (i == R.id.play_pause) {
            onPlayPause();
        } else if (i == R.id.play_resume) {
            onPlayResume();
        } else if (i == R.id.play_next) {
            onPlayNext();
        } else if (i == R.id.closeIv) {
            onCloseIvClick();
        } else if (i == R.id.playBackTv) {
            onPlayBackClick();
        }
    }

    private void onPlayPrevious() {
        if (!isPlayMode || curPlayIndex <= 0)
            return;
        curPlayIndex -= 2;
        onPlayNext();
    }

    private void onPlayPause() {
        if (!isPlayMode)
            return;
        isPause = true;
        AnimationUtils.animateScale(mPlayPauseIv, 0, 0, 300, 0, false);
        AnimationUtils.animateScale(mPlayResumeTv, 1, 1, 300, 0, true);
        AnimationUtils.clearAnimation(mStatusFtIv);
        mPathAnimView.cancelAnimation();
    }

    private void onPlayResume() {
        onPlayNext();
    }

    private void onPlayNext() {
        if (!isPlayMode)
            return;

        curPlayIndex++;
        if (curPlayIndex >= eventList.size()) {
            onPlayBackClick();
            return;
        }
        if (isPause) {
            AnimationUtils.animateScale(mPlayPauseIv, 1, 1, 300, 0, true);
            AnimationUtils.animateScale(mPlayResumeTv, 0, 0, 300, 0, false);
            AnimationUtils.doLiveScoreFtAnimation(mStatusFtIv);
        }
        startAnimEventAnimation(true);
        isPause = false;
    }

    private void onCloseIvClick() {
        createCircularReveal(false);
        //调用父Activity()的 方法 允许上滑列表手势拦截
        // getActivity().requestInterceptGesture(true);
        resetUI();
    }

    private void onPlayBackClick() {
        mAnimActionIv.setImageDrawable(!isPlayMode ? null : ContextCompat.getDrawable(getContext(), R.drawable.icon_finish_large));
        mAnimActionTv.setText(!isPlayMode ? "" : getString(R.string.time_over));
        playBackTv.setText(getString(!isPlayMode ? R.string.turn_back : R.string.turn_play_back));
        if (isPlayMode) {
            resetUI();
        } else {
            isPlayMode = true;
            switchPlayModeUI(true, false);
            mAnimActionTv.setText(getString(R.string.no_sign_now));
            mAnimActionIv.setImageResource(R.drawable.icon_no_sign_large);
            //1.如果是回放模式,如果回访事件集合为空,那么请求接口
            //2.如果不为空，那么按顺序播放了
            if (eventList.size() != 0) {
                AnimationUtils.doLiveScoreFtAnimation(mStatusFtIv);
                AnimationUtils.animateScale(mStatusLayout, 1, 1, 0, 0, true);
                startAnimEventAnimation(true);
            } else {
                getPlayBackEventList();
            }
        }

    }

    /*private void createCircularReveal(boolean expand) {
        if (getContentView().getHeight() == 0 || getContentView().getWidth() == 0)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int width = getContentView().getWidth();
            int height = getContentView().getHeight();
            float endRadius = (float) Math.sqrt(width * width + height * height);
            final Animator animator = ViewAnimationUtils.createCircularReveal(getContentView(), 0, 0, expand ? 20 : endRadius, expand ? endRadius : 0);
            animator.setDuration(1000);
            animator.start();
            if (expand) {
                getContentView().setVisibility(View.VISIBLE);
            } else {
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        getContentView().setVisibility(View.GONE);
                        animator.removeAllListeners();
                    }
                });
            }
        } else if (!expand)
            getContentView().setVisibility(View.GONE);
    }*/

    @Override
    public void onSuccess(RequestContainer request, Object result) {
        LiveScoreUrlEnum anEnum = (LiveScoreUrlEnum) request.getRequestEnum();
        switch (anEnum) {
            case SEASON_RACE_STATUS_QUERY:
                try {
                    JSONObject status = ((JSONObject) result).getJSONObject("status");
                    mStatus = new NormalObject(status.getString("name"), status.getString("message"));
                    setMatchStatus();
                    boolean beforeFinish = FtRaceStatusEnum.isBeforeFinish(FtRaceStatusEnum.saveValueOf(mStatus.getName()));
                    if (beforeFinish)
                        setMatchStatus4BeforeFinish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case SEASON_RACE_LASTEST5_RESULT_QUERY:
                setRecent5MatchResult((LiveScore5MatchRecently) result);
                break;
            case FLASH_SCORE_QUERY:
                setAnimEventDataResult((PathAnimResult) result);
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        getActivity().operateErrorResponseMessage(jsonObject);
    }

    //获取近五场
    private void getRecent5Match() {
        RequestFormBody formBody = new RequestFormBody(LiveScoreUrlEnum.SEASON_RACE_LASTEST5_RESULT_QUERY);
        formBody.put("raceId", mRaceId);
        formBody.setRequestUrl(AppParam.FT_API_DOMAIN + AppParam.FT_API_S_URL);
        formBody.setGenericClaz(LiveScore5MatchRecently.class);
        httpHelper.startRequest(formBody);
    }

    //设置近五场
    private void setRecent5MatchResult(LiveScore5MatchRecently matchRecently) {
        mAnimActionTv.setText("");
        mAnimActionIv.setImageDrawable(null);

        mRecentLayout.setVisibility(View.VISIBLE);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_path_anim_5recent);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mRecentLayout.getLayoutParams();
        params.height = LibAppUtil.dip2px(getContext(), 98);
        params.topMargin = LibAppUtil.dip2px(getContext(), -18);
        mRecentLayout.setLayoutParams(params);
        mRecentLayout.setBackgroundDrawable(drawable);

        ViewGroup.MarginLayoutParams params1 = (ViewGroup.MarginLayoutParams) mGsLiveMatchLeftTV.getLayoutParams();
        params1.height = LibAppUtil.dip2px(getContext(), 55);
        params1.topMargin = LibAppUtil.dip2px(getContext(), 18);

        mGsLiveMatchLeftTV.setLayoutParams(params1);
        mGsLiveMatchRightTv.setLayoutParams(params1);

        mGsLiveMatchLeftTV.setMatchScore(isReverse ? matchRecently.guestAllResult : matchRecently.homeAllResult);
        mGsLiveMatchRightTv.setMatchScore(isReverse ? matchRecently.homeAllResult : matchRecently.guestAllResult);
        mGsLiveMatchTip.setText(TextUtils.isEmpty(mRaceDataBean.startDate) ? "" : TimeUtils.formatStringDate(mRaceDataBean.startDate, "MM-dd HH:mm") + "开赛");

    }

    //获取头部直播事件数据
    private void getAnimEventData() {
        RequestFormBody formBody = new RequestFormBody(LiveScoreUrlEnum.FLASH_SCORE_QUERY);
        formBody.put("raceId", mRaceId);
        formBody.put("timestamp", timestamp);
        formBody.setRequestUrl(AppParam.FT_API_DOMAIN + AppParam.FT_API_S_URL);
        formBody.setGenericClaz(PathAnimResult.class);
        httpHelper.startRequest(formBody);
        //定时请求
        mHandler.sendEmptyMessageDelayed(POST_DELAY_LOOP, INTERVAL);
    }

    //获取回放事件集合
    private void getPlayBackEventList() {
        RequestFormBody formBody = new RequestFormBody(LiveScoreUrlEnum.FLASH_SCORE_QUERY);
        formBody.put("raceId", mRaceId);
        formBody.put("timestamp", 0);
        formBody.put("type", "REPLAY");
        formBody.setRequestUrl(AppParam.FT_API_DOMAIN + AppParam.FT_API_S_URL);
        formBody.setGenericClaz(PathAnimResult.class);
        httpHelper.startRequest(formBody);
    }

    @Override
    public void onDestroy() {
        httpHelper.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mPathAnimView.reset();
        super.onDestroy();
    }

    private void resetUI() {
        if (isPlayMode)
            AnimationUtils.animateScale(playBackTv, 1, 1, 50, 0, true);
        AnimationUtils.clearAnimation(mStatusFtIv);
        AnimationUtils.animateScale(mLeftTipTv, 0, 0, 1, 0, false);
        AnimationUtils.animateScale(mRightTipTv, 0, 0, 1, 0, false);
        AnimationUtils.animate(mStatusLayout, 0, 0, 0, 0, 0, 0, 4);
        switchPlayModeUI(false, !isPlayMode);
        teamInfoTip.setText(mFullScore);
        mHandler.removeCallbacksAndMessages(null);
        mPathAnimView.reset();
        mStatusTv.setText("00:00");
        isPlayMode = false;
        isPause = false;
        curPlayIndex = 0;
    }

    public void setFullScore(String fullScore) {
        mFullScore = fullScore;
        teamInfoTip.setText(fullScore);
    }

}
