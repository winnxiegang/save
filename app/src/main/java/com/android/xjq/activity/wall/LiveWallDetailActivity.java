package com.android.xjq.activity.wall;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.NetworkUtils;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.pullrecycler.recyclerview.onRefreshListener;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.bean.SubjectBean2;
import com.android.xjq.bean.SubjectProperties;
import com.android.xjq.bean.subject.SubjectDetailHeadBean;
import com.android.xjq.dialog.live.CommentDetailDialog;
import com.android.xjq.model.comment.CommentTypeEnum;
import com.android.xjq.utils.SocialTools;
import com.android.xjq.utils.XjqUrlEnum;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.banana.commlib.utils.NetworkUtils.NETWORK_MOBILE;

/**
 * Created by lingjiu on 2018/3/3.
 */

public class LiveWallDetailActivity extends BaseActivity implements OnHttpResponseListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.backIv)
    ImageView backIv;
    @BindView(R.id.likeTv)
    TextView likeTv;
    @BindView(R.id.commentTv)
    TextView commentTv;
    @BindView(R.id.videoMemoTv)
    TextView videoMemoTv;
    @BindView(R.id.videoPlayRb)
    RadioButton mVideoPlayIv;
    @BindView(R.id.video_thumb)
    ImageView mVideoThumbIv;
    @BindView(R.id.video_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.likeIv)
    ImageView likeIv;
    @BindView(R.id.portraitIv)
    ImageView portraitIv;
    private String videoUrl;
    private String firstFrameUrl;
    private int curPlayStatus;
    private HttpRequestHelper httpRequestHelper;
    public static final int PLAY_EVT_PREPARE = 0;
    public static final int PLAY_EVT_BEGIN = 1;
    public static final int PLAY_EVT_PROGRESS = 2;
    public static final int PLAY_EVT_PAUSED = 3;
    public static final int PLAY_EVT_RESUMED = 4;
    public static final int PLAY_EVT_STOPPED = 5;
    private String subjectId;
    private SubjectDetailHeadBean mDetailBean;
    private CommentDetailDialog commentDetailDialog;
    private int currentPosition;

    @OnClick({R.id.backIv, R.id.likeIv, R.id.commentTv, R.id.videoPlayRb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backIv:
                finish();
                break;
            case R.id.likeIv:
                if (mDetailBean != null) {
                    if (mDetailBean.getSubjectSimple().liked) {
                        SocialTools.unLike(subjectId, CommentTypeEnum.SUBJECT.name(), new SocialTools.onSocialCallback() {
                            @Override
                            public void onResponse(JSONObject response, boolean success) {
                                mDetailBean.getSubjectSimple().liked = false;
                                likeIv.setSelected(false);
                                mDetailBean.getSubjectSimple().likeCount = mDetailBean.getSubjectSimple().likeCount - 1;
                                likeTv.setText(String.valueOf(mDetailBean.getSubjectSimple().likeCount));
                            }
                        });
                    } else {
                        SocialTools.like(subjectId, CommentTypeEnum.SUBJECT.name(), new SocialTools.onSocialCallback() {
                            @Override
                            public void onResponse(JSONObject response, boolean success) {
                                mDetailBean.getSubjectSimple().liked = true;
                                likeIv.setSelected(true);
                                mDetailBean.getSubjectSimple().likeCount = mDetailBean.getSubjectSimple().likeCount + 1;
                                likeTv.setText(String.valueOf(mDetailBean.getSubjectSimple().likeCount));
                            }
                        });
                    }
                }
                break;
            case R.id.commentTv:
                if (mDetailBean != null && mDetailBean.getSubjectSimple() != null) {
                    if (commentDetailDialog == null) {
                        commentDetailDialog = new CommentDetailDialog(LiveWallDetailActivity.this, mDetailBean, new onRefreshListener() {
                            @Override
                            public void onRefresh(boolean refresh) {
                                if (refresh)
                                    getSubject();
                            }
                        });
                    }

                    commentDetailDialog.show();
                }
                break;
            case R.id.videoPlayRb:
                startPlay();
                break;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.at_live_wall_detail);
        ButterKnife.bind(this);
        subjectId = getIntent().getStringExtra("subjectId");
        httpRequestHelper = new HttpRequestHelper(LiveWallDetailActivity.this, this);
    }

    /**
     * @param activity
     */
    public static void startLiveWallDetailActivity(Activity activity, String subjectId) {
        Intent intent = new Intent(activity, LiveWallDetailActivity.class);
        intent.putExtra("subjectId", subjectId);
        activity.startActivity(intent);
    }

    private void setView() {
        if (curPlayStatus != PLAY_EVT_PREPARE) return;
        PicUtils.load(getApplicationContext(), mVideoThumbIv, firstFrameUrl);
        /*FrameLayout.LayoutParams LayoutParams = new FrameLayout.LayoutParams(-1, -1);
        //设置相对于父布局四边对齐
        LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(LayoutParams);*/
        try {
            Uri uri = Uri.parse(videoUrl);
            //设置视频路径
            videoView.setVideoURI(uri);
        } catch (Exception e) {
            LogUtils.e("kk", "e=" + e.toString());
            ToastUtil.showLong(this.getApplicationContext(), "播放失败");
            finish();
        }
        //播放完成回调
        videoView.setOnCompletionListener(this);
        videoView.setOnPreparedListener(this);
        videoView.start();
        changeVideoHolderEvent(PLAY_EVT_PROGRESS);


        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && (curPlayStatus == PLAY_EVT_PROGRESS || curPlayStatus == PLAY_EVT_RESUMED)) {
                    mVideoPlayIv.setVisibility(View.VISIBLE);
                    mVideoPlayIv.animate().setStartDelay(2000).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            mVideoPlayIv.setVisibility(View.GONE);
                        }
                    }).start();
                }
                return true;
            }
        });
    }


    private void getSubject() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.SUBJECT_DETAIL, true);
        map.put("subjectId", subjectId);
        httpRequestHelper.startRequest(map, true);
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        changeVideoHolderEvent(PLAY_EVT_STOPPED);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        changeVideoHolderEvent(PLAY_EVT_BEGIN);
    }


    public void changeVideoHolderEvent(int playStatus) {
        if (playStatus == curPlayStatus)
            return;
        curPlayStatus = playStatus;

        switch (curPlayStatus) {
            case PLAY_EVT_PREPARE://准备
                mVideoThumbIv.setVisibility(View.INVISIBLE);
                mVideoPlayIv.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                break;
            case PLAY_EVT_BEGIN://开始播放
                mProgressBar.setVisibility(View.GONE);
                mVideoPlayIv.setChecked(false);
                break;
            case PLAY_EVT_PROGRESS://播放中
                break;
            case PLAY_EVT_PAUSED://被暂停
                mVideoPlayIv.animate().cancel();
                videoView.pause();
                currentPosition = videoView.getCurrentPosition();
                mVideoPlayIv.setVisibility(View.VISIBLE);
                mVideoPlayIv.setChecked(true);
                break;
            case PLAY_EVT_RESUMED://由暂停恢复播放
                videoView.seekTo(currentPosition);
                videoView.start();
                mVideoPlayIv.animate().cancel();
                mVideoPlayIv.setChecked(false);
                mVideoPlayIv.animate().setStartDelay(2000).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mVideoPlayIv.setVisibility(View.GONE);
                    }
                }).start();
                break;
            case PLAY_EVT_STOPPED://停止，结束
                mVideoThumbIv.setVisibility(View.VISIBLE);
                mVideoPlayIv.setVisibility(View.VISIBLE);
                mVideoPlayIv.setSelected(true);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initNetJudge();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (videoView != null)
            videoView.stopPlayback();
        videoView = null;
    }

    private void initNetJudge() {
        LogUtils.e("kk", videoUrl + "----------------");
        if (NetworkUtils.getNetworkState(this) == NETWORK_MOBILE) {
            new ShowMessageDialog(this, "继续观看", "退出房间", new OnMyClickListener() {
                @Override
                public void onClick(View v) {

                    if (videoUrl == null) {
                        getSubject();
                    } else {
                        tryStart();
                    }
                }
            }, new OnMyClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }, "您正在使用移动网络观看直播");

        } else {
            if (videoUrl == null) {
                getSubject();
            } else {
                tryStart();
            }
        }
    }


    private void tryStart() {
        if (curPlayStatus == PLAY_EVT_STOPPED) {
            changeVideoHolderEvent(PLAY_EVT_BEGIN);
            videoView.seekTo(0);
            videoView.start();
        } else if (curPlayStatus == PLAY_EVT_PAUSED) {
            //正在暂停 恢复它
            changeVideoHolderEvent(PLAY_EVT_RESUMED);
        } else if (curPlayStatus < PLAY_EVT_PAUSED) {
            //正在播放 暂停它
            changeVideoHolderEvent(PLAY_EVT_PAUSED);
        }
    }


    private void startPlay() {
        mVideoPlayIv.setSelected(false);

        if (curPlayStatus < PLAY_EVT_PAUSED || curPlayStatus == PLAY_EVT_RESUMED) {
            //正在播放 暂停它
            changeVideoHolderEvent(PLAY_EVT_PAUSED);
        } else {
            initNetJudge();
        }
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {

        mDetailBean = new Gson().fromJson(jsonObject.toString(), SubjectDetailHeadBean.class);
        if (mDetailBean != null && mDetailBean.getSubjectSimple() != null) {
            commentTv.setText(String.valueOf(mDetailBean.getSubjectSimple().replyCount));
            likeTv.setText(String.valueOf(mDetailBean.getSubjectSimple().likeCount));
            PicUtils.load(getApplicationContext(), portraitIv, mDetailBean.getSubjectSimple().userLogoUrl);
            if (commentDetailDialog != null) {
                commentDetailDialog.update(mDetailBean);
            }
        }
        SubjectBean2 subjectSimple = mDetailBean.getSubjectSimple();
        if (subjectSimple != null) {
            videoMemoTv.setText(mDetailBean.getSubjectSimple().summary);
            SubjectProperties properties = subjectSimple.properties;
            if (properties != null) {
                videoUrl = properties.videoUrl;
                firstFrameUrl = properties.videoFirstFrameImageUrl;
                setView();
            }
        }

    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            operateErrorResponseMessage(jsonObject);
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

}
