package com.android.xjq.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 椭圆形状的可选择的标签，在“首页-伤停”中使用，可参考
 * 中的使用
 * <p>
 * Created by DaChao on 2017/11/29.
 */

public class OvalTabView extends View {

    public interface OnTabChangedListener {
        void onChanged(int oldPosition, int newPosition);
    }

    private RectF strokeRectF;
    private RectF ovalRectF;
    private Paint strokePaint;
    private Paint ovalPaint;
    private Paint textPaint;

    private int downX;
    private OnTabChangedListener listener;

    //用于显示的标签文字集合
    private List<String> tagList;

    //图标
    private List<Drawable> drawableList;

    private List<Integer> textWidthList;

    //选中的标签位置
    private int selectedPosition;

    //绘制每个标签内容区的矩形区域宽度
    private int contentRectWidth;

    //填充椭圆的半径
    private int ovalRadius;

    //////以下是样式相关参数//////

    //边框线条的宽度
    private int strokeWidth;

    //椭圆的颜色值
    private int ovalColor;

    //文字颜色值
    private int textColor;

    //标签字体大小
    private int textSize;

    //图标宽度
    private int iconWidth = 60;

    //图标高度
    private int iconHeight = 60;

    private int imageMargin = 20;

    private boolean tint = false;

    public OvalTabView(Context context) {
        this(context, null);
    }

    public OvalTabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OvalTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //设置默认值
        selectedPosition = 0;
        strokeWidth = dp2px(1);
        ovalColor = 0xFF3F51B5;
        textColor = 0xFF3F51B5;
        textSize = sp2px(10);

        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (tagList.size() == 0) {
            return;
        }

        //设定样式值
        strokePaint.setColor(ovalColor);
        ovalPaint.setColor(ovalColor);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        //绘制边框
        int strokeRadius = (getHeight() - strokeWidth) / 2;
        strokeRectF.left = strokeWidth / 2;
        strokeRectF.top = strokeWidth / 2;
        strokeRectF.right = getWidth() - strokeWidth / 2;
        strokeRectF.bottom = getHeight() - strokeWidth / 2;
        canvas.drawRoundRect(strokeRectF, strokeRadius, strokeRadius, strokePaint);

        //绘制填充的椭圆
        ovalRadius = (getHeight() - strokeWidth * 2) / 2;
        contentRectWidth = (getWidth() - strokeWidth * 2 - ovalRadius * (tagList.size() + 1)) / tagList.size();
        ovalRectF.left = strokeWidth - 1 + selectedPosition * (ovalRadius + contentRectWidth);
        ovalRectF.top = strokeWidth - 1;
        if (selectedPosition == tagList.size() - 1) {
            ovalRectF.right = getWidth();
        } else {
            ovalRectF.right = ovalRectF.left + 1 + contentRectWidth + ovalRadius * 2;
        }
        ovalRectF.bottom = getHeight() - strokeWidth + 1;
        canvas.drawRoundRect(ovalRectF, ovalRadius, ovalRadius, ovalPaint);

        //绘制标签图片和文字
        int contentCenterX;
        int contentCenterY;

        int contentWidth = 0;

        Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
        int offset = (-fontMetricsInt.top - (fontMetricsInt.bottom - fontMetricsInt.top) / 2);
        contentCenterY = getHeight() / 2 + offset;
        for (int i = 0; i < tagList.size(); i++) {
            boolean isSelected;
            contentCenterX = strokeWidth + ovalRadius * (i + 1) + contentRectWidth * i + contentRectWidth / 2;
            if (i == selectedPosition) {
                isSelected = true;
                textPaint.setColor(0xffffffff);
            } else {
                isSelected = false;
                textPaint.setColor(textColor);
            }
            contentWidth += textWidthList.get(i);
            if (i < drawableList.size()) {
                contentWidth += (iconWidth + imageMargin);
                Drawable drawable = drawableList.get(i);
                int left = contentCenterX - contentWidth / 2;
                int top = getHeight() / 2 - iconHeight / 2;
                int right = left + iconWidth;
                int bottom = top + iconHeight;
                drawable.setBounds(left, top, right, bottom);
                if (tint) {
                    if (isSelected) {
                        drawable.setColorFilter(0xffffffff, PorterDuff.Mode.SRC_ATOP);
                    } else {
                        drawable.setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
                    }
                } else {
                    drawable.clearColorFilter();
                }
                drawable.draw(canvas);
            }
            canvas.drawText(tagList.get(i), contentCenterX + (iconWidth + imageMargin - contentWidth / 2), contentCenterY, textPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                return true;

            case MotionEvent.ACTION_UP:
                int downTabPosition = getTouchTabPosition(downX);
                int upTabPosition = getTouchTabPosition((int) event.getX());
                if (downTabPosition == upTabPosition) {
                    setSelectedPosition(upTabPosition);
                    performClick();
                }
                return true;
        }

        return false;

    }

    /**
     * 获取触摸点所属的tag标签位置
     */
    private int getTouchTabPosition(int x) {
        return x / (ovalRadius + contentRectWidth);
    }

    /**
     * 设置描边的宽度，单位dp
     */
    public void setStrokeWidth(int dp) {
        strokeWidth = dp2px(dp);
        invalidate();
    }

    /**
     * 设置椭圆颜色值
     */
    public void setOvalColor(int color) {
        ovalColor = color;
        invalidate();
    }

    /**
     * 设置文字颜色
     */
    public void setTextColor(int color) {
        textColor = color;
        invalidate();
    }

    /**
     * 设置标签文字字体大小，单位sp
     */
    public void setTextSize(int sp) {
        textSize = sp2px(sp);
        invalidate();
    }

    /**
     * tint为true时图标会设置成ovalColor的颜色值
     */
    public void setImageTint(boolean tint) {
        this.tint = tint;
        invalidate();
    }

    /**
     * 设置标签页图标的宽度
     */
    public void setImageWidth(int dp) {
        iconWidth = dp2px(dp);
        invalidate();
    }

    /**
     * 设置标签页图标的高度
     */
    public void setImageHeight(int dp) {
        iconHeight = dp2px(dp);
        invalidate();
    }

    /**
     * 设置图标和文字的距离
     */
    public void setImageMargin(int dp) {
        imageMargin = dp2px(dp);
        invalidate();
    }

    /**
     * 设置选中状态的标签的position
     */
    public void setSelectedPosition(int position) {
        if (selectedPosition != position && position < tagList.size()) {
            int oldPosition = selectedPosition;
            selectedPosition = position;
            invalidate();
            if (listener != null) {
                listener.onChanged(oldPosition, position);
            }
        }
    }

    /**
     * 设置标签列表
     */
    public void setTagList(List<String> tagList) {
        this.tagList.clear();
        this.tagList.addAll(tagList);
        this.textWidthList.clear();

        //计算文字宽度
        for (String s : this.tagList) {
            float[] widths = new float[s.length()];
            textPaint.getTextWidths(s, widths);
            int textWidth = 0;
            for (float width : widths) {
                textWidth += width;
            }
            textWidthList.add(textWidth);
        }

        setSelectedPosition(0);
    }

    /**
     * 设置图片列表
     */
    public void setImageResourceList(List<Integer> resIdList) {
        drawableList.clear();
        for (int i = 0; i < resIdList.size(); i++) {
            drawableList.add(getContext().getResources().getDrawable(resIdList.get(i)));
        }
        invalidate();
    }

    public void setImageBitmapList(List<Bitmap> bitmapList) {
        drawableList.clear();
        for (int i = 0; i < bitmapList.size(); i++) {
            drawableList.add(new BitmapDrawable(getContext().getResources(), bitmapList.get(i)));
        }
        invalidate();
    }

    /**
     * 设置tab切换监听
     */
    public void setOnTabChangedListener(OnTabChangedListener listener) {
        this.listener = listener;
    }

    /**
     * 相关初始化操作
     */
    private void init() {

        tagList = new ArrayList<>();
        drawableList = new ArrayList<>();
        textWidthList = new ArrayList<>();

        strokeRectF = new RectF();
        ovalRectF = new RectF();

        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeWidth);
        strokePaint.setColor(ovalColor);

        ovalPaint = new Paint();
        ovalPaint.setAntiAlias(true);
        ovalPaint.setColor(ovalColor);

        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.LEFT);
    }

    private void updateDrawable() {

    }

    /**
     * dp单位转换为px
     */
    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /**
     * sp单位转换为px
     */
    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

}

