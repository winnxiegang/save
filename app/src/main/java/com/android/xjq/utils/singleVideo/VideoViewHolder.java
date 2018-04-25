package com.android.xjq.utils.singleVideo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.NetworkUtils;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.utils.picasso.PicassoLoadCallback;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.XjqVideoView;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.jjx.sdk.utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.bean.SubjectsComposeBean2;
import com.android.xjq.utils.live.BitmapBlurHelper;

import static android.media.MediaPlayer.MEDIA_INFO_BUFFERING_END;
import static android.media.MediaPlayer.MEDIA_INFO_BUFFERING_START;
import static android.media.MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START;
import static com.android.banana.commlib.utils.NetworkUtils.NETWORK_MOBILE;


/**
 * Created by qiaomu on 2018/2/26.
 * <p>
 * 单个视频播放的控制类
 * <p>
 * 腾讯播放器的使用api文档：https://cloud.tencent.com/document/product/584/9373?lang=en
 * <p>
 * <p>
 * 被注释的代码 都是一开始使用腾讯播放器的代码
 */

public class VideoViewHolder implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener, PicassoLoadCallback {
    private static final String TAG = "VideoViewHolder";

    private XjqVideoView mVideoView;
    private ProgressBar mProgressBar;
    private ImageView mVideoThumbIv, mVideoBlurIv;
    private RadioButton mVideoPlayIv;
    private FrameLayout mVideoContainer;

    private ViewHolder mHolder;
    private SinglePlayCallback mPlayCallback;
    private Context mContext;

    public static final int PLAY_EVT_QUEUE = 0;
    public static final int PLAY_EVT_PREPARE = 1;
    public static final int PLAY_EVT_PROGRESS = 2;
    public static final int PLAY_EVT_PAUSED = 3;
    public static final int PLAY_EVT_RESUMED = 4;
    public static final int PLAY_EVT_STOPPED = 5;

    private int curPlayStatus = PLAY_EVT_STOPPED;
    private int currentPosition;
    private SubjectsComposeBean2 mComposeBean;
    private boolean thumbLoadSuccess = false, autoBlur = false;//缩略图是否加载成功过,加载成功后 是否需要自动模糊化


    public VideoViewHolder(Context context, ViewHolder holder, SinglePlayCallback playCallback) {
        mContext = context;
        mHolder = holder;
        mPlayCallback = playCallback;
    }

    public void bindVideoItemView(final SubjectsComposeBean2 composeBean) {
        mComposeBean = composeBean;

        composeBean.makeVideoProperties();

        mVideoThumbIv = mHolder.getView(R.id.video_thumb);
        mProgressBar = mHolder.getView(R.id.video_progressBar);
        mVideoView = mHolder.getView(R.id.videoView);
        mVideoPlayIv = mHolder.getView(R.id.video_play);
        mVideoContainer = mHolder.getView(R.id.video_layout);
        mVideoBlurIv = mHolder.getView(R.id.video_blur);

        // mHolder.setImageByUrl(mContext, R.id.video_thumb, composeBean.firstFrameUrl);
        PicUtils.loadWithBitmap(mContext, composeBean.firstFrameUrl, this);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(mContext, R.color.pink_400);
            int[] colors = new int[]{color, color, color, color, color, color};
            int[][] states = new int[6][];
            states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
            states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
            states[2] = new int[]{android.R.attr.state_enabled};
            states[3] = new int[]{android.R.attr.state_focused};
            states[4] = new int[]{android.R.attr.state_window_focused};
            states[5] = new int[]{};
            ColorStateList colorList = new ColorStateList(states, colors);
            mProgressBar.setIndeterminateTintList(colorList);
            mProgressBar.setProgressTintMode(PorterDuff.Mode.SRC_IN);
        }

        setVideoViewOnTouch();
    }

    private void initNetJudge() {

        if (NetworkUtils.getNetworkState(mContext) == NETWORK_MOBILE) {
            ShowMessageDialog dialog = new ShowMessageDialog(mContext, "继续观看", "不看了", new OnMyClickListener() {
                @Override
                public void onClick(View v) {
                    tryStart();
                }
            }, null, "您正在使用移动网络观看直播");
        } else {
            tryStart();
        }
    }

    private void setVideoViewOnTouch() {
        mVideoPlayIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (curPlayStatus < PLAY_EVT_PAUSED) {
                    //正在播放 暂停它
                    changeVideoHolderEvent(PLAY_EVT_PAUSED);

                }
                else {

                    initNetJudge();
                }

            }
        });

        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && curPlayStatus == PLAY_EVT_PROGRESS) {
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

    private void tryStart() {
        if (curPlayStatus == PLAY_EVT_STOPPED) {
            changeVideoHolderEvent(PLAY_EVT_QUEUE);

            try {
                //设置视频路径
                mVideoView.setVideoURI(Uri.parse(mComposeBean.videoUrl));
                mVideoView.setOnPreparedListener(this);
                mVideoView.setOnErrorListener(this);
                mVideoView.setOnCompletionListener(this);
                mVideoView.setOnInfoListener(this);
                mVideoView.requestFocus();// 让VideoView获取焦点
                mVideoView.start();
               /* mVideoView.resume();
                mVideoView.seekTo(0);*/
            } catch (Exception e) {
                //播放失敗
                ToastUtil.showShort(mContext, "播放失败");
                changeVideoHolderEvent(PLAY_EVT_STOPPED);
            }

        }else if (curPlayStatus == PLAY_EVT_PAUSED) {
            //正在暂停 恢复它
            changeVideoHolderEvent(PLAY_EVT_RESUMED);
        }
        else if (curPlayStatus < PLAY_EVT_PAUSED) {
            //正在播放 暂停它
            changeVideoHolderEvent(PLAY_EVT_PAUSED);

        }
    }



    public void changeVideoHolderEvent(int playStatus) {
        LogUtils.e(TAG,( playStatus == curPlayStatus)+"--- playStatus == curPlayStatus");
        if (playStatus == curPlayStatus)

            return;
        LogUtils.e(TAG, "changeVideoHolderEvent当前位置Pos:" + mHolder.getLayoutPosition() + "__/当前播放状态:" + playStatus + "__/0:缓冲--__/1:播放中--__/2:播放中--__/3：恢复播放--__/4：暂停--__/5:停止");
        LogUtils.e(TAG, "__/当前播放状态:" + playStatus );
        curPlayStatus = playStatus;
        switch (curPlayStatus) {
            case PLAY_EVT_QUEUE://加入队列，在singleManager中被加入队列  0
                mProgressBar.setVisibility(View.VISIBLE);
                mVideoPlayIv.setVisibility(View.GONE);
                mVideoView.setVisibility(View.VISIBLE);
                break;
            case PLAY_EVT_PREPARE://准备完毕状态  1
                showBlurBackground(false);

                break;
            case PLAY_EVT_PROGRESS://播放中   2
                mVideoPlayIv.setChecked(false);
                mVideoThumbIv.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.GONE);
                break;
            case PLAY_EVT_PAUSED://被暂停  3
                currentPosition = mVideoView.getCurrentPosition();
                mVideoView.pause();
                mVideoPlayIv.animate().cancel();
                mVideoPlayIv.setVisibility(View.VISIBLE);
                mVideoPlayIv.setChecked(true);
                break;
            case PLAY_EVT_RESUMED://由暂停恢复播放   4
                mVideoView.seekTo(currentPosition);
                mVideoView.start();
                mVideoPlayIv.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                mVideoPlayIv.animate().cancel();
                mVideoPlayIv.setChecked(false);
                mVideoPlayIv.animate().setStartDelay(2000).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mVideoPlayIv.setVisibility(View.GONE);
                    }
                }).start();
                break;
            case PLAY_EVT_STOPPED://停止，结束  5
                if (mVideoView != null) {
                    mVideoView.stopPlayback();//停止播放,释放资源
                    mVideoView.suspend();
                }
                mVideoThumbIv.setVisibility(View.VISIBLE);
                mVideoPlayIv.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mVideoPlayIv.setChecked(true);
                break;
        }
        if (mPlayCallback != null) {
            mPlayCallback.onPlayStatusChanged(this, playStatus);
        }

    }


    public int getCurPlayStatus() {
        return curPlayStatus;
    }


    public boolean isDetachWindow(ViewGroup viewParent) {
        View userLayout = mHolder.getView(R.id.user_layout);
        View titleSummaryLayout = mHolder.getView(R.id.title_summary_layout);
        View footerLayout = mHolder.getView(R.id.footer_layout);

        LogUtils.e("isDetachWindow: ", (mHolder.itemView.getTop() + userLayout.getHeight() + titleSummaryLayout.getHeight()) + "-" + (mHolder.itemView.getBottom() + "--" + footerLayout.getHeight()));
        //计算 视频播放区域的[下边缘]是否超过了 recyclerView的顶部
        boolean isDetachOfBottomEdge = mHolder.itemView.getTop() + userLayout.getHeight() + titleSummaryLayout.getHeight() > viewParent.getHeight();
        //计算 视频播放区域的[上边缘]是否超过了 recyclerView的底部
        boolean isDetachOfTopEdge = mHolder.itemView.getBottom() <= footerLayout.getHeight();
        return isDetachOfBottomEdge || isDetachOfTopEdge;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        LogUtils.e(TAG, "onPrepared");
        changeVideoHolderEvent(PLAY_EVT_PREPARE);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        LogUtils.e(TAG, "onError:" + what);
        changeVideoHolderEvent(PLAY_EVT_PAUSED);
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        changeVideoHolderEvent(PLAY_EVT_STOPPED);
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        LogUtils.e(TAG, "onInfo" + what);

        switch (what) {
            case MEDIA_INFO_VIDEO_RENDERING_START://开始渲染播放ing
                mVideoView.setBackgroundColor(Color.TRANSPARENT);
                changeVideoHolderEvent(PLAY_EVT_PROGRESS);
                break;
            case MEDIA_INFO_BUFFERING_START://一次播放片段缓冲的开始
                break;
            case MEDIA_INFO_BUFFERING_END://一次播放片段缓冲的结束
                break;
        }
        return false;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap) {
        thumbLoadSuccess = true;
        mVideoThumbIv.setImageBitmap(bitmap);
    }

    private void showBlurBackground(boolean onBitmapLoaded) {
        if (onBitmapLoaded && thumbLoadSuccess) {
            BitmapDrawable bd = (BitmapDrawable) mVideoThumbIv.getDrawable();
            Bitmap bm = bd.getBitmap();
            if (bm == null)
                return;
            int width = bm.getWidth();
            int height = bm.getHeight();

            int dstWidth = width * 3;
            int dstHeight = LibAppUtil.dip2px(mContext, 220);

            Bitmap cropBitmap = Bitmap.createBitmap(bm, width / 2 - width / 5, height / 2 - height / 5, width / 5, height / 5);
            Bitmap scaleBitmap = Bitmap.createScaledBitmap(cropBitmap, dstWidth, dstHeight, true);
            Bitmap blur = BitmapBlurHelper.doBlur(mContext, scaleBitmap, 3);
            if (blur == null)
                return;

            mVideoBlurIv.setBackground(new BitmapDrawable(blur));
            mVideoBlurIv.setVisibility(View.VISIBLE);
        } else {
            autoBlur = true;
            PicUtils.loadWithBitmap(mContext, mComposeBean.firstFrameUrl, this);
        }
    }
}
