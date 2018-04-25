package com.android.xjq.dialog.live;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.dialog.CustomDialog;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.DimensionUtils;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.Money;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.RechargeActivity;
import com.android.xjq.dialog.base.BaseDialog;
import com.android.xjq.dialog.base.ViewHolder;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.xjq.utils.XjqUrlEnum;
import com.qiniu.pili.droid.shortvideo.PLMediaFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lingjiu on 2018/3/2.
 */

public class PublishVideoDialog extends BaseDialog implements IHttpResponseListener, EditVideoDialog.OnTrimCompletedListener {
    private String videoPath = Config.SCREEN_RECORD_FILE_PATH;
    @BindView(R.id.memoTv)
    TextView memoTv;
    @BindView(R.id.coverIv)
    ImageView coverIv;
    @BindView(R.id.videoInfoLayout)
    LinearLayout videoInfoLayout;
    @BindView(R.id.descEt)
    EditText descEt;
    @BindView(R.id.previewTv)
    TextView previewTv;
    @BindView(R.id.trimTv)
    TextView trimTv;
    @BindView(R.id.publishTv)
    TextView publishTv;
    @BindView(R.id.videoTimeTv)
    TextView videoTimeTv;
    private WrapperHttpHelper httpHelper;
    private long videoDuration;
    private String mUploadVideoFilePath = videoPath;
    private File mUploadPicFilePath;
    private PLMediaFile mMediaFile;
    private EditVideoDialog previewVideoDialog;
    private CustomDialog dialog;

    @OnClick({R.id.trimTv, R.id.publishTv, R.id.previewTv, R.id.closeIv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trimTv:
                previewVideoDialog = new EditVideoDialog(mContext, videoPath);
                previewVideoDialog.show();
                previewVideoDialog.setOnTrimCompletedListener(this);
                break;
            case R.id.closeIv:
                showConfirmDialog();
                break;
            case R.id.publishTv:
                uploadVideo();
                break;
            case R.id.previewTv:
                new PreviewVideoDialog(mContext, videoPath).show();
                break;
        }
    }

    private void showConfirmDialog() {
        ShowMessageDialog dialog = new ShowMessageDialog(mContext, new OnMyClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        }, null, mContext.getString(R.string.cancel_publish));

    }

    public PublishVideoDialog(Context context) {
        super(context);
        setShowBottom(true);
        setDimAmount(0.5f);
        setOutCancel(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpHelper = new WrapperHttpHelper(this);
        publishVideoConfigQuery();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpView();
    }

    private void setUpView() {
        //获取视频信息
        mMediaFile = new PLMediaFile(videoPath);
        videoDuration = mMediaFile.getDurationMs();
        videoTimeTv.setText(TimeUtils.timeFormat(videoDuration));
        new MyTask(coverIv.getWidth(), coverIv.getHeight()).execute();
    }

    @Override
    public void trimCompleted(boolean success) {
        if (success) {
            videoPath = Config.TRIM_FILE_PATH;
            setUpView();
        }
    }

    private void uploadVideo() {
        if (mUploadPicFilePath == null || mUploadVideoFilePath == null) {
            return;
        }
        if (dialog == null)
            dialog = new CustomDialog(mContext);

        dialog.showDialog("正在上传...");
        String content = TextUtils.isEmpty(descEt.getText()) ? "" : descEt.getText().toString();
        File cropFile = new File(mUploadVideoFilePath);
        RequestFormBody map = new RequestFormBody(XjqUrlEnum.VIDEO_UPLOAD, true, true);
        map.putFormDataPart("firstFrameImage", mUploadPicFilePath.getName(), mUploadPicFilePath);
        map.putFormDataPart("video", cropFile.getName(), cropFile);
        map.put("content", content);
        map.put("channelAreaId", CurLiveInfo.channelAreaId);
        httpHelper.startRequest(map);
    }

    private void publishVideoConfigQuery() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.VIDEO_CONFIG_QUERY, true);
        httpHelper.startRequest(map);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_publish_video_layout;
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialog dialog) {
        final EditText descEt = holder.getView(R.id.descEt);
        descEt.post(new Runnable() {
            @Override
            public void run() {
                LibAppUtil.showSoftKeyboard(mContext, descEt);
            }
        });
    }

    public Bitmap createVideoThumbnail() {
        // 获取视频的最大关键帧(不是第一个关键帧)
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
       /* bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                DimensionUtils.getScreenWidth(mContext), DimensionUtils.getScreenHeight(mContext), ThumbnailUtils.OPTIONS_RECYCLE_INPUT);*/
        // 获取第一个关键帧
       /* MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        Bitmap bmp = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);*/
        return bitmap;
    }


    class MyTask extends AsyncTask<Void, Bitmap, File> {
        int width, height;

        MyTask(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        protected File doInBackground(Void... voids) {
            File file = new File(Config.VIDEO_FRAME_FILE_PATH);
            try {
               /* PLVideoFrame frame = mMediaFile.getVideoFrameByTime(1, true);
                // PLVideoFrame frame = mMediaFile.getVideoFrameByTime(2, true, width, height);
                Bitmap bitmap = frame.toBitmap();*/
                Bitmap bitmap = createVideoThumbnail();
                publishProgress(bitmap);
                compressImage(file, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return file;
        }

        @Override
        protected void onProgressUpdate(Bitmap... values) {
            Bitmap bitmap = values[0];
            if (bitmap != null) {
                coverIv.setImageBitmap(bitmap);
            }
        }

        @Override
        protected void onPostExecute(File outFile) {
            mUploadPicFilePath = outFile;
        }

    }

    /**
     * 压缩图片（质量压缩）
     *
     * @param file
     * @param bitmap
     */
    public static File compressImage(File file, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //recycleBitmap(bitmap);
        return file;
    }

    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }


    @Override
    public void onSuccess(RequestContainer request, Object o) {
        switch (((XjqUrlEnum) request.getRequestEnum())) {
            case VIDEO_CONFIG_QUERY:
                responseSuccessConfigQuery(((JSONObject) o));
                break;
            case VIDEO_UPLOAD:
                dialog.dismiss();
                ToastUtil.showShort(mContext, "上传成功");
                dismiss();
                break;
        }
    }

    private void responseSuccessConfigQuery(JSONObject jo) {
        try {
            double pointCoinAmount = 0;
            if (jo.has("pointCoinAmount")) {
                pointCoinAmount = jo.getDouble("pointCoinAmount");
            }
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append("发布(");
            SpannableString moneySpan = new SpannableString(new Money(pointCoinAmount).toSimpleString());
            moneySpan.setSpan(new AbsoluteSizeSpan((int) DimensionUtils.spToPx(12, mContext)), 0, moneySpan.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(moneySpan);
            SpannableString ss = new SpannableString("图片");
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.icon_banana_gold_coin);
            drawable.setBounds(0, 0, drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth());
            ss.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), 0, ss.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(ss);
            ssb.append(")");
            publishTv.setText(ssb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        try {
            if (dialog != null)
                dialog.dismiss();

            ErrorBean errorBean = new ErrorBean(jsonObject);
            if ("AVAIABLE_AMOUNT_NOT_ENOUGH".equals(errorBean.error.name)) {
                new ShowMessageDialog(mContext, "去充值", "取消", new OnMyClickListener() {
                    @Override
                    public void onClick(View v) {
                        RechargeActivity.startRechargeActivity((Activity) mContext);
                    }
                }, null, "余额不足,请立即充值");
                return;
            }
        } catch (Exception e) {
        }
        ((BaseActivity) mContext).operateErrorResponseMessage(jsonObject);
    }
}
