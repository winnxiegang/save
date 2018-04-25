package com.android.xjq.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import com.android.banana.commlib.utils.DimensionUtils;
import com.android.library.Utils.LogUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2017/9/7.
 */

public class PathAnimView extends View {

    private Context mContext;
    private Paint mPaint;
    private Paint drawTextPaint;
    private int giftPicWidth;
    private int giftPicHeight;
    private List<GiftViewHolder> viewHolders;
    private Point startPosition;
    private Point endPosition;
    private Point controlPoint;
    private Point textEndPosition;
    private static Point ANIM_START_POSITION_PORTRAIT;
    private static Point ANIM_END_POSITION_PORTRAIT;
    private static Point ANIM_START_POSITION_LANDSCAPE;
    private static Point ANIM_END_POSITION_LANDSCAPE;
    private static long ANIM_DURATION = 1200;
    private boolean isPortrait = true;
    private float textSize;
    //由于礼物发送成功时批量返回的,一次返回多个礼物成功,这边做个延时
    private static long spacingTime = 2 * 100;
    private long preTime;
    private long delayTime;


    public PathAnimView(Context context) {
        super(context);
        init(context);
    }


    public PathAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        viewHolders = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);
        drawTextPaint = new Paint();
        drawTextPaint.setStrokeWidth(3);
        textSize = DimensionUtils.spToPx(24, mContext);
        drawTextPaint.setTextSize(textSize);
        drawTextPaint.setAntiAlias(true);
        drawTextPaint.setStyle(Paint.Style.FILL);
        drawTextPaint.setColor(Color.parseColor("#ec4432"));
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC);
        drawTextPaint.setTypeface(font);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        giftPicWidth = (int) DimensionUtils.dpToPx(50, mContext);
        giftPicHeight = (int) DimensionUtils.dpToPx(50, mContext);
        ANIM_START_POSITION_PORTRAIT = new Point(DimensionUtils.getScreenWidth(mContext) - giftPicWidth / 2, (int) (DimensionUtils.dpToPx(120, mContext)));
        ANIM_END_POSITION_PORTRAIT = new Point(DimensionUtils.getScreenWidth(mContext) / 2 - giftPicWidth / 2, (int) (DimensionUtils.dpToPx(120, mContext)));
        ANIM_START_POSITION_LANDSCAPE = new Point(DimensionUtils.getScreenWidth(mContext) / 2 - giftPicWidth / 2, DimensionUtils.getScreenHeight(mContext) / 2 - giftPicWidth / 2);
        ANIM_END_POSITION_LANDSCAPE = new Point(DimensionUtils.getScreenWidth(mContext) / 5, DimensionUtils.getScreenHeight(mContext) / 2 - giftPicWidth / 2);
        startPosition = isPortrait ? ANIM_START_POSITION_PORTRAIT : ANIM_START_POSITION_LANDSCAPE;
        endPosition = isPortrait ? ANIM_END_POSITION_PORTRAIT : ANIM_END_POSITION_LANDSCAPE;
        int pointX = (int) (endPosition.x + DimensionUtils.dpToPx(30, mContext));//(startPosition.x + endPosition.x) / 2;
        int pointY = (int) (startPosition.y - DimensionUtils.dpToPx(160, mContext));
        controlPoint = new Point(pointX, pointY);
        int textPointY = (int) (endPosition.y + giftPicHeight + DimensionUtils.dpToPx(28, mContext));
        textEndPosition = new Point(endPosition.x, textPointY);
    }

    public void sendGiftSuccess(String imgUrl, final int number) {
        Picasso.with(mContext).load(imgUrl).into(new MyTarget(number));
    }

    class MyTarget implements Target {
        int number;

        public MyTarget(int number) {
            this.number = number;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
           /* long l = System.currentTimeMillis();
            if (l - preTime < spacingTime) {
                delayTime += spacingTime - (l - preTime);
            }
            preTime = l;
            LogUtils.e("onBitmapLoaded", "delayTime=" + delayTime);
            postDelayed(getAnimActionRunnable(bitmap, number), delayTime);*/

            Bitmap giftBitmap = bitmap.createScaledBitmap(bitmap, giftPicWidth, giftPicHeight, true);
            final GiftViewHolder viewHolder = new GiftViewHolder(number, giftBitmap);
            if (viewHolders != null && viewHolders.size() > 0) {
                if (viewHolders.get(viewHolders.size() - 1).number == viewHolder.number) {
                    viewHolder.needDrawNum = false;
                    viewHolder.isTrackPath = false;
                }
            }
            viewHolders.add(viewHolder);
            startAnimation(viewHolder);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }

    private Runnable getAnimActionRunnable(final Bitmap bitmap, final int number) {
        return new Runnable() {
            @Override
            public void run() {
                Bitmap giftBitmap = bitmap.createScaledBitmap(bitmap, giftPicWidth, giftPicHeight, true);
                final GiftViewHolder viewHolder = new GiftViewHolder(number, giftBitmap);
                if (viewHolders != null && viewHolders.size() > 0) {
                    if (viewHolders.get(viewHolders.size() - 1).number == viewHolder.number) {
                        viewHolder.needDrawNum = false;
                        viewHolder.isTrackPath = false;
                    }
                }
                viewHolders.add(viewHolder);
                startAnimation(viewHolder);
            }
        };
    }


    public void startAnimation(final GiftViewHolder viewHolder) {

        CurveEvaluator curveEvaluator = new CurveEvaluator(controlPoint);
        ValueAnimator anim = ValueAnimator.ofObject(curveEvaluator, startPosition, endPosition);
        anim.setDuration(ANIM_DURATION);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                viewHolder.percent = animation.getAnimatedFraction();
                viewHolder.currentPosition = point;
                if (viewHolder.isTrackPath)
                    viewHolder.numPosition = viewHolder.currentPosition;
                invalidate();

                LogUtils.e("onAnimationEnd", "animation = " + animation.getAnimatedFraction());
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                LogUtils.e("onAnimationEnd+++", "animation = " + ((ValueAnimator) animation).getAnimatedFraction());
//                viewHolder.bitmap.recycle();
                viewHolder.bitmap = null;
                viewHolders.remove(viewHolder);
                if (viewHolders.size() > 0) {
                    viewHolders.get(0).needDrawNum = true;
                } else {
                    delayTime = 0;
                }
            }
        });
        anim.setInterpolator(new OvershootInterpolator(0.8f));
//        anim.setInterpolator(new BounceInterpolator());
        anim.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            doPathAnim(canvas);
        } catch (Exception e) {
        }
    }

    private void canvasNumber(GiftViewHolder viewHolder, Canvas canvas) {
        LogUtils.e("canvasNumber", "" + viewHolder.needDrawNum + viewHolder.isTrackPath + viewHolder.percent);
        if (viewHolder.needDrawNum) {
            String text = "x " + String.valueOf(viewHolder.number);
            float textWidth = drawTextPaint.measureText(text);
            if (!viewHolder.isTrackPath) {
                canvas.drawText("x " + String.valueOf(viewHolder.number), endPosition.x + giftPicWidth / 2 - textWidth / 2, textEndPosition.y, drawTextPaint);
            } else {
                if (viewHolder.percent >= 0.3) {
                    int alpha = (int) (255 * viewHolder.percent);
                    drawTextPaint.setAlpha(alpha > 255 ? 255 : alpha);
                    drawTextPaint.setTextSize(textSize * viewHolder.percent);
                    canvas.drawText(text, viewHolder.numPosition.x + giftPicWidth / 2 - textWidth / 2, textEndPosition.y, drawTextPaint);
                }
            }
        }
    }

    private void doPathAnim(Canvas canvas) {
        for (GiftViewHolder viewHolder : viewHolders) {
            if (viewHolder.bitmap == null) continue;
            canvas.drawBitmap(viewHolder.bitmap, viewHolder.getMatrix(), mPaint);
            canvasNumber(viewHolder, canvas);
        }

    }

    public void changeOrientation(boolean portrait) {
        this.isPortrait = portrait;
    }


    public class CurveEvaluator implements TypeEvaluator<Point> {

        private Point controlPoint;

        public CurveEvaluator(Point controlPoint) {
            this.controlPoint = controlPoint;
        }

        @Override
        public Point evaluate(float t, Point startValue, Point endValue) {
            int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * controlPoint.x + t * t * endValue.x);
            int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * controlPoint.y + t * t * endValue.y);

            return new Point(x, y);
        }
    }

    public static class GiftViewHolder {
        public Point currentPosition;
        public float percent;
        public Matrix matrix;
        public Bitmap bitmap;
        public int number;
        public Point numPosition;
        public boolean needDrawNum;
        public boolean isTrackPath;

        public GiftViewHolder(int number, Bitmap bitmap) {
            this.number = number;
            this.bitmap = bitmap;
            this.needDrawNum = true;
            this.isTrackPath = true;
            this.numPosition = new Point();
        }

        public Matrix getMatrix() {
            if (matrix == null) matrix = new Matrix();
            matrix.reset();
            /*if (percent >= 0.8f) {
                matrix.postScale(percent + 0.2f, percent + 0.2f);
                matrix.postTranslate(currentPosition.x, currentPosition.y);
            } else {*/
            float scalePercent = (percent + 0.3f) > 1 ? 1 : (percent + 0.3f);
            matrix.postScale(scalePercent, scalePercent);
            matrix.postTranslate(currentPosition.x, currentPosition.y);
//            }
            return matrix;
        }
    }
}
