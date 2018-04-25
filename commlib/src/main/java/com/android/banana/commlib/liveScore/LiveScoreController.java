package com.android.banana.commlib.liveScore;

import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.bean.liveScoreBean.JclqDynamicData;
import com.android.banana.commlib.bean.liveScoreBean.JclqMatchLiveBean;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.bean.liveScoreBean.JczqDynamicDataBean;
import com.android.banana.commlib.bean.liveScoreBean.JczqMatchLiveBean;
import com.android.banana.commlib.liveScore.livescoreEnum.BtRaceStatusEnum;
import com.android.banana.commlib.liveScore.livescoreEnum.FtRaceStatusEnum;
import com.android.banana.commlib.utils.DimensionUtils;
import com.android.banana.commlib.utils.HhsUtils;
import com.android.banana.commlib.utils.StringUtils;
import com.android.banana.commlib.utils.TimeUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by lingjiu on 2018/1/31.
 */

public class LiveScoreController extends BaseController4JCZJ<BaseActivity> {


    ImageView guestTeamIv, homeTeamIv, secondsIv;
    TextView guestTeamTv, fullScoreTv, halfScoreTv, homeTeamTv, liveTimeTv, matchTitleTv;
    LinearLayout halfScoreLayout, liveTimeLayout;
    RelativeLayout headerContent, matchScoreLayout;
    private JczqDataBean mRaceDataBean;
    private AnimationDrawable mAnimationDrawable;

    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.view_live_score_controller);
        initView();
    }

    private void initView() {
        guestTeamIv = findViewOfId(R.id.guestTeamIv);
        homeTeamIv = findViewOfId(R.id.homeTeamIv);
        secondsIv = findViewOfId(R.id.secondsIv);
        guestTeamTv = findViewOfId(R.id.guestTeamTv);
        fullScoreTv = findViewOfId(R.id.fullScoreTv);
        halfScoreTv = findViewOfId(R.id.halfScoreTv);
        homeTeamTv = findViewOfId(R.id.homeTeamTv);
        liveTimeTv = findViewOfId(R.id.liveTimeTv);
        matchTitleTv = findViewOfId(R.id.matchTitleTv);
        halfScoreLayout = findViewOfId(R.id.halfScoreLayout);
        matchScoreLayout = findViewOfId(R.id.matchScoreLayout);
        liveTimeLayout = findViewOfId(R.id.liveTimeLayout);
        headerContent = findViewOfId(R.id.headerContent);
        mAnimationDrawable = ((AnimationDrawable) secondsIv.getBackground());
    }

    @Override
    public void onSetUpView() {

    }

    private boolean isRaceIdEmpty(JczqDataBean bean) {
        return TextUtils.equals("0", bean.innerRaceId);
    }

    public void setDownloadImage(ImageView iv, String url, int placeHolder) {
        if (StringUtils.isBlank(url)) {
            Picasso.with(context.getApplicationContext())
                    .load(placeHolder)
                    .into(iv);
        } else {
            Picasso.with(context.getApplicationContext())
                    .load(Uri.parse(url))
                    .placeholder(placeHolder)
                    .error(placeHolder)
                    .into(iv);
        }
    }

    /**
     * 赛事数据返回
     **/
    public void setMatchData(boolean isFootball, JczqDataBean bean) {
        mRaceDataBean = bean;
        headerContent.setBackgroundResource(isFootball ? R.drawable.icon_score_living_ft_bg : R.drawable.icon_score_living_bt_bg);
        //主客队基本信息设置
        matchTitleTv.setText(bean.matchName + "\u3000" + TimeUtils.format(TimeUtils.LONG_DATEFORMAT, TimeUtils.MATCH_FORMAT, bean.getGmtStart()));
        setDownloadImage(homeTeamIv, isFootball ? mRaceDataBean.getFTHomeLogoUrl() : mRaceDataBean.getBTHomeLogoUrl(),
                isFootball ? R.drawable.ft_ho_icon : R.drawable.bt_gs_icon);
        setDownloadImage(guestTeamIv, isFootball ? mRaceDataBean.getFTGuestLogoUrl() : mRaceDataBean.getBTGuestLogoUrl(),
                isFootball ? R.drawable.ft_gs_icon : R.drawable.bt_gs_icon);
        NormalObject status = mRaceDataBean.getRaceStatus();
        if (isFootball) {
            homeTeamTv.setText(HhsUtils.splitString(mRaceDataBean.getHomeTeamName(), 10) + "(主)");
            guestTeamTv.setText(mRaceDataBean.getGuestTeamName());
            //比赛状态设置
            if (status != null) {
                String statusName = status.getName();
                switch (FtRaceStatusEnum.valueOf(statusName)) {
                    case WAIT:    //等待开始
                    {
                        fullScoreTv.setText("VS");
                        if (isRaceIdEmpty(bean)) {
                            halfScoreTv.setText("-");
                        } else {
                            halfScoreTv.setText(status.getMessage());
                        }
                    }
                    break;
                    case FINISH://完场
                    {
                        fullScoreTv.setText(bean.getFullHomeScore() + ":" + bean.getFullGuestScore());
                        halfScoreTv.setText(bean.getHalfHomeScore() + ":" + bean.getHalfGuestScore());
                    }
                    break;
                    case PLAY_F://上半场
                    case PLAY_S://下半场
                    case OVERTIME://加时
                    case PAUSE://中场
                    {
                        //fullScoreTv.setText(bean.getFullHomeScore() + ":" + bean.getFullGuestScore());
                        //liveTimeLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                    case CUT:       //腰斩(比赛进行中,比赛无效)
                    case DELAY:     //延时(比赛有效，以后还会开始)
                    case UNKNOWN:    //未知
                    case CANCEL:    //取消(比赛开始前,比赛无效)
                    case DEC:       //待定(继续中状态,比分可能无效)
                    case POSTPONE:  //推迟
                    {
                        fullScoreTv.setText("VS");
                        halfScoreTv.setText(status.getMessage());
                    }
                    break;
                }
            }
        } else {
            homeTeamTv.setText(bean.getHomeTeamName() + "(主)");
            guestTeamTv.setText(bean.getGuestTeamName());
            if (BtRaceStatusEnum.valueOf(status.getName()) == BtRaceStatusEnum.FINISH) {
                showFullScore(fullScoreTv, bean.getFullHomeScore(), bean.getFullGuestScore());
                showHalfScoreAndPointSpread(halfScoreTv, bean.getFullHomeScore(), bean.getFullGuestScore());
            } else {
                fullScoreTv.setText("VS");
                halfScoreTv.setText("未赛");
            }
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) liveTimeLayout.getLayoutParams();
            params.topMargin = (int) DimensionUtils.dpToPx(22, context);
        }

    }


    public void setFTLiveScoreData(JczqMatchLiveBean bean) {
        bean.operatorData();
        if (bean.getDynamicDataList().size() == 1) {
            JczqDynamicDataBean liveBean = bean.getDynamicDataList().get(0);
            fullScoreTv.setText(liveBean.getHs() + ":" + liveBean.getGs());
            halfScoreTv.setText(liveBean.getS().getMessage());
            FtRaceStatusEnum status = FtRaceStatusEnum.saveValueOf(liveBean.getS().getName());
            switch (status) {
                case PLAY_F://上半场
                case PLAY_S://下半场
                case OVERTIME://加时
                case PAUSE://中场
                {
                    if (FtRaceStatusEnum.saveValueOf(liveBean.getS().getName()) == FtRaceStatusEnum.PAUSE) {
                        liveTimeLayout.setVisibility(View.GONE);
                        mAnimationDrawable.stop();
                    } else {
                        liveTimeLayout.setVisibility(View.VISIBLE);
                        liveTimeTv.setText(liveBean.getLiveHaveStartTime());
                        if (!mAnimationDrawable.isRunning()) {
                            mAnimationDrawable.start();
                        }
                    }
                    //refreshData();
                }
                break;
                case FINISH: {
                    if (mAnimationDrawable.isRunning()) {
                        mAnimationDrawable.stop();
                    }
                    halfScoreTv.setText(liveBean.getHhs() + ":" + liveBean.getHgs());
                    liveTimeLayout.setVisibility(View.GONE);
                    //比赛完场
                    //if (mActivity != null) mActivity.setMatchFinish();
                }
                break;
                default: {
                    fullScoreTv.setText("VS");
                    halfScoreTv.setText(status.message());
                    if (mAnimationDrawable.isRunning()) {
                        mAnimationDrawable.stop();
                    }
                    liveTimeLayout.setVisibility(View.GONE);
                }
                break;
            }
        }
    }


    public void setBTLiveScoreData(JclqMatchLiveBean bean) {
        if (bean.getDynamicDataList() != null && bean.getDynamicDataList().size() == 1) {
            JclqDynamicData data = bean.getDynamicDataList().get(0);
            switch (BtRaceStatusEnum.valueOf(data.getStatus().getName())) {
                case BREAK_OFF:
                case CUT:
                case DEC:
                case DELAY:
                case POSTPONE:
                case CANCEL:
                case WAIT:
                case UNKNOWN:
                    fullScoreTv.setText("VS");
                    halfScoreTv.setText(data.getStatus().getMessage());
                    break;
                case PLAY_OT_4://第4'OT
                case PLAY_OT_3://第3'OT
                case PLAY_OT_2://第2'OT
                case PLAY_OT_1://第1'OT
                case PLAY_4://第4节
                case PLAY_3://第3节
                case PLAY_2://第2节
                case PLAY_1://第1节
                case PLAY_F://上半场
                case PLAY_S://下半场
                case OVERTIME://加时
                case PAUSE://中场
                    if (BtRaceStatusEnum.safeValueOf(data.getStatus().getName()) == BtRaceStatusEnum.PAUSE) {
                        mAnimationDrawable.stop();
                    } else {
                        liveTimeLayout.setVisibility(View.VISIBLE);
                        liveTimeTv.setVisibility(View.GONE);
                        if (!mAnimationDrawable.isRunning()) {
                            mAnimationDrawable.start();
                        }
                        secondsIv.setVisibility(View.VISIBLE);
                    }
                    fullScoreTv.setText(data.getHomeScore() + ":" + data.getGuestScore());
                    showFullScore(fullScoreTv, data.getHomeScore(), data.getGuestScore());
                    halfScoreTv.setText(data.getStatus().getMessage() + " " + data.getRemainTime());

                    break;
                case FINISH:
                    liveTimeLayout.setVisibility(View.GONE);
                    fullScoreTv.setText(data.getGuestScore() + ":" + data.getHomeScore());
                    halfScoreTv.setText("总分" + (data.getGuestScore() + data.getHomeScore()) + "\n" +
                            "分差" + (Math.abs(data.getGuestScore() - data.getHomeScore())));
                    showFullScore(fullScoreTv, data.getHomeScore(), data.getGuestScore());
                    showHalfScoreAndPointSpread(halfScoreTv, data.getHomeScore(), data.getGuestScore());
                    //mActivity.requestLayoutRefresh();
                    break;
            }
        }
    }

    public void showHalfScoreAndPointSpread(TextView textView, int HomeTeamScore, int GuestTeamScore) {
        textView.setText(context.getResources().getString(R.string.total_score) + (HomeTeamScore + GuestTeamScore + "分")
                + "\n" + context.getResources().getString(R.string.point_spread) + (HomeTeamScore - GuestTeamScore) + "分");
    }

    public void setScore(TextView textView, int score) {
        textView.setText("" + score);
    }

    public void showFullScore(TextView textView, int HomeTeamScore, int GuestTeamScore) {
        textView.setText(HomeTeamScore + ":" + GuestTeamScore);
    }

}
