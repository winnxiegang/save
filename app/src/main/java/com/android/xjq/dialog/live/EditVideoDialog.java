package com.android.xjq.dialog.live;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.utils.DimensionUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.xjq.R;
import com.android.xjq.dialog.CustomProgressDialog;
import com.android.xjq.dialog.base.BaseDialog;
import com.android.xjq.dialog.base.ViewHolder;
import com.android.xjq.view.MySeekBar;
import com.qiniu.pili.droid.shortvideo.PLMediaFile;
import com.qiniu.pili.droid.shortvideo.PLShortVideoTrimmer;
import com.qiniu.pili.droid.shortvideo.PLVideoFrame;
import com.qiniu.pili.droid.shortvideo.PLVideoSaveListener;

import butterknife.BindView;
import butterknife.OnClick;

import static com.qiniu.pili.droid.shortvideo.PLErrorCode.ERROR_MULTI_CODEC_WRONG;

/**
 * Created by lingjiu on 2018/3/2.
 */

public class EditVideoDialog extends BaseDialog {
    private static final String TAG = EditVideoDialog.class.getName();
    private String videoPath;
    private String trimVideoOutPath = Config.TRIM_FILE_PATH;

    @BindView(R.id.frameIv)
    ImageView frameIv;
    @BindView(R.id.seekbar)
    MySeekBar mSeekbar;
    @BindView(R.id.cancelTv)
    TextView cancelTv;
    @BindView(R.id.confirmTv)
    TextView confirmTv;
    @BindView(R.id.closeIv)
    ImageView closeIv;
    private PLShortVideoTrimmer mShortVideoTrimmer;
    private PLMediaFile mMediaFile;

    private static final int SLICE_COUNT = 8;
    private long mSelectedBeginMs;
    private long mSelectedEndMs;
    private long mDurationMs;
    private int mVideoFrameCount;
    private MyTask myTask;
    private CustomProgressDialog mProcessingDialog;
    private int width;
    private int height;
    private static final int MAX_PROGRESS = 100;
    private boolean isTouchLeft;

    @OnClick({R.id.closeIv, R.id.cancelTv, R.id.confirmTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelTv:
                dismiss();
                break;
            case R.id.closeIv:
                dismiss();
                break;
            case R.id.confirmTv:
                onDone();
                break;
        }
    }

    public void setOnTrimCompletedListener(OnTrimCompletedListener onTrimCompletedListener) {
        this.onTrimCompletedListener = onTrimCompletedListener;
    }

    private OnTrimCompletedListener onTrimCompletedListener;

    public interface OnTrimCompletedListener {
        void trimCompleted(boolean success);
    }

    public EditVideoDialog(Context context, String url) {
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
        return R.layout.dialog_edit_video_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpView();
    }


    @Override
    public void dismiss() {
        super.dismiss();
        if (myTask != null) myTask.cancel(true);
    }

    private void setUpView() {

        mProcessingDialog = new CustomProgressDialog(mContext);
        //获取剪辑类
        mShortVideoTrimmer = new PLShortVideoTrimmer(mContext, videoPath, trimVideoOutPath);
        //获取视频信息
        mMediaFile = new PLMediaFile(videoPath);
        mSelectedEndMs = mDurationMs = mMediaFile.getDurationMs();
        mVideoFrameCount = mMediaFile.getVideoFrameCount(false);
        mSeekbar.setTotalTime(mSelectedEndMs);
        Log.e(TAG, "video duration: " + mDurationMs);
        width = frameIv.getWidth();
        height = frameIv.getHeight();
        myTask = new MyTask(width, height);
        myTask.execute();
        mSeekbar.setOnSeekBarChangeListener(new MySeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressBefore() {
            }

            @Override
            public void onProgressChanged(MySeekBar seekBar, boolean isTouchLeft, int progressLow, int progressHigh) {
                EditVideoDialog.this.isTouchLeft = isTouchLeft;
                mSelectedBeginMs = mSelectedEndMs * progressLow / MAX_PROGRESS;
                mSelectedEndMs = mSelectedEndMs * progressHigh / MAX_PROGRESS;
                getVideoFrame(isTouchLeft);
            }

            @Override
            public void onProgressAfter() {
            }
        });
    }

    public void onDone() {
        if (mSelectedBeginMs == 0 && mSelectedEndMs == mDurationMs) {
            ToastUtil.showShort(mContext, "请先选择截取范围");
            return;
        }
        Log.i(TAG, "trim to file path: " + Config.TRIM_FILE_PATH + " range: " + mSelectedBeginMs + " - " + mSelectedEndMs);
        mProcessingDialog.show();
        mShortVideoTrimmer.trim(mSelectedBeginMs, mSelectedEndMs, PLShortVideoTrimmer.TRIM_MODE.FAST, new PLVideoSaveListener() {
            @Override
            public void onSaveVideoSuccess(String path) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(mContext.getApplicationContext(), "截取成功");
                        if (onTrimCompletedListener != null)
                            onTrimCompletedListener.trimCompleted(true);
                        mProcessingDialog.dismiss();
                        dismiss();
                    }
                });
            }

            @Override
            public void onSaveVideoFailed(final int errorCode) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProcessingDialog.dismiss();
                        if (onTrimCompletedListener != null)
                            onTrimCompletedListener.trimCompleted(false);
                        if (errorCode == ERROR_MULTI_CODEC_WRONG) {
                            ToastUtil.showShort(mContext, "当前机型暂不支持该功能");
                        } else {
                            ToastUtil.showShort(mContext, "剪辑失败");
                        }
                        Log.e(TAG, "trim video failed, error code: " + errorCode);
                    }
                });
            }

            @Override
            public void onSaveVideoCanceled() {
                mProcessingDialog.dismiss();
            }

            @Override
            public void onProgressUpdate(final float percentage) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProcessingDialog.setProgress((int) (100 * percentage));
                    }
                });
            }
        });
    }


    class MyTask extends AsyncTask<Void, PLVideoFrame, PLVideoFrame> {
        int width, height;

        MyTask(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        protected PLVideoFrame doInBackground(Void... voids) {
            PLVideoFrame frame = mMediaFile.getVideoFrameByTime(isTouchLeft ? mSelectedBeginMs : mSelectedEndMs, true, width, height);
            publishProgress(frame);
            return frame;
        }

        @Override
        protected void onProgressUpdate(PLVideoFrame... values) {
            super.onProgressUpdate(values);
            PLVideoFrame frame = values[0];
            if (frameIv != null && frame != null) {
                frameIv.setImageBitmap(frame.toBitmap());
            }
        }
    }


    private void getVideoFrame(boolean isTouchLeft) {
        //myTask.execute();
        Log.e(TAG, "myTask執行了");
        myTask = new MyTask(width, height);
        myTask.execute();
       /* PLVideoFrame frame = mMediaFile.getVideoFrameByTime(isTouchLeft ? mSelectedBeginMs : mSelectedEndMs, true, width, height);
        if (frame != null) {
            frameIv.setImageBitmap(frame.toBitmap());
        }*/
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialog dialog) {


    }
}
