package com.android.banana.commlib.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by qiaomu on 2018/3/16.
 * <p>
 * 寫這個類的原因 {@link XjqVideoView#setOnInfoListener(MediaPlayer.OnInfoListener)}有api的限制
 */

public class XjqVideoView extends VideoView {
    public XjqVideoView(Context context) {
        this(context, null);
    }

    public XjqVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XjqVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
    }

    @Override
    public void setOnInfoListener(MediaPlayer.OnInfoListener l) {
        super.setOnInfoListener(l);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthSize = getDefaultSize(0, widthMeasureSpec);
//        int heightSize = getDefaultSize(0, heightMeasureSpec);
//        setMeasuredDimension(widthSize, heightSize);
    }
}
