package com.android.xjq.view.expandtv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by lingjiu on 2017/8/16.
 */

public class LevelTextView extends TextView {

    private Paint paint;
    private String srcStr = ":";
    private Bitmap bitmap;
    private int txtWid;//文字本来的宽度
    private int iconWidth;//图片的宽度
    private int txtHeight;
    private int srcWidth;//补充字的宽度
    private int iconHeight;
    private int iconLeftAndRightpadd;

    public LevelTextView(Context context) {
        this(context, null);
    }

    public LevelTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LevelTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(getTextSize());
        paint.setColor(Color.parseColor("#30a9ef"));
        paint.setFilterBitmap(true);
        paint.setDither(true);
        srcWidth = (int) paint.measureText(srcStr);
        iconLeftAndRightpadd = LibAppUtil.dip2px(getContext(), 2);
        iconWidth = (int) getContext().getResources().getDimension(R.dimen.sp14);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (text == null || text.length() == 0) return;
        Rect rect = new Rect();
        paint.getTextBounds(getText().toString(), 0, getText().length(), rect);
        txtWid = rect.width();
        txtHeight = rect.height();
        setWidth(txtWid + iconWidth + srcWidth + iconLeftAndRightpadd * 2);
//        LogUtils.e("LevelTextView=="," "+txtWid+" "+iconWidth+" "+srcWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (srcStr == null || srcStr.length() == 0) return;
       /* Rect rect = new Rect();
        paint.getTextBounds(srcStr, 0, srcStr.length(), rect);
        txtWid = rect.width();*/
    }

    @Override
    public void setWidth(int pixels) {
        super.setWidth(pixels);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setImageUrl(String url) {
        if (url == null) return;
        Picasso.with(getContext()).load(url).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                //图片加载完成之后设置宽度重绘界面
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, iconWidth, iconWidth, true);
                LevelTextView.this.bitmap = scaledBitmap;
                /*iconWidth = scaledBitmap.getScreenWidth();
                iconHeight = scaledBitmap.getHeight();*/
                setWidth(txtWid + iconWidth + srcWidth + iconLeftAndRightpadd * 2);
                invalidate();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            canvas.save();
            canvas.drawBitmap(bitmap, txtWid + iconLeftAndRightpadd, txtHeight / 3, paint);
            canvas.restore();
        }
        canvas.drawText(srcStr, txtWid + iconWidth + iconLeftAndRightpadd * 2, getBaseline(), paint);

        LogUtils.e("LevelTextView", " " + getBaseline() + " " + txtHeight + " " + iconHeight);
    }
}
