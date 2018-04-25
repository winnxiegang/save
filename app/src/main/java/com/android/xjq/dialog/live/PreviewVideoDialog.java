package com.android.xjq.dialog.live;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.android.banana.commlib.utils.DimensionUtils;
import com.android.xjq.R;
import com.android.xjq.dialog.base.BaseDialog;
import com.android.xjq.dialog.base.ViewHolder;
import com.android.xjq.view.MySeekBar;
import com.qiniu.pili.droid.shortvideo.PLMediaFile;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lingjiu on 2018/3/2.
 */

public class PreviewVideoDialog extends BaseDialog {
    private static final String TAG = EditVideoDialog.class.getName();
    private String videoPath = Config.SCREEN_RECORD_FILE_PATH;

    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.seekbar)
    MySeekBar mSeekbar;
    @BindView(R.id.playFlagIv)
    ImageView playFlagIv;
    private PLMediaFile mMediaFile;
    private long mDurationMs;
    private long mSelectedEndMs;

    @OnClick({R.id.closeIv, R.id.playFlagIv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closeIv:
                videoView.stopPlayback();
                videoView = null;
                dismiss();
                break;
            case R.id.playFlagIv:
                playFlagIv.setVisibility(View.GONE);
                videoView.seekTo(0);
                videoView.start();
                mSeekbar.startCountDown(mSelectedEndMs);
                break;
        }
    }


    public PreviewVideoDialog(Context context, String url) {
        super(context);
        setShowBottom(false);
        int screenHeight = DimensionUtils.getScreenHeight(mContext);
        int screenWidth = DimensionUtils.getScreenWidth(mContext);
        setIsNeedConverPx(false);
        setWidth((int) (screenWidth * 0.8));
        setHeight((int) (screenHeight * 0.8));
        videoPath = url;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_preview_video_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpView();
    }

    private void setUpView() {
        preparePlayVideo();
        //获取视频信息
        mMediaFile = new PLMediaFile(videoPath);
        mSelectedEndMs = mDurationMs = mMediaFile.getDurationMs();
        mSeekbar.setTotalTime(mSelectedEndMs);
        Log.e(TAG, "video duration: " + mDurationMs);
    }

    private void preparePlayVideo() {
        videoView.setVisibility(View.VISIBLE);
        /*RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        //设置相对于父布局四边对齐
        LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //为VideoView添加属性
        videoView.setLayoutParams(LayoutParams);*/
        Uri uri = Uri.parse(videoPath);
        //设置视频路径
        videoView.setVideoURI(uri);
        //播放完成回调
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playFlagIv.setVisibility(View.VISIBLE);
                playFlagIv.setImageResource(R.drawable.icon_video_restart_flag);
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

            }
        });

        videoView.seekTo(1);
    }


    @Override
    public void convertView(ViewHolder holder, BaseDialog dialog) {


    }
}
