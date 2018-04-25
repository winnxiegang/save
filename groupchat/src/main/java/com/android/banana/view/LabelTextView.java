package com.android.banana.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputFilter;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;


import com.android.banana.R;

import java.lang.reflect.Field;

/**
 * Created by mrs on 17/5/26.
 * <p>
 * 1.设置标签
 * [左LEFT 上TOP 右RIGHT 下BOTTOM] 四个方向的 label 均为居中的
 * 如果统一设置label跟左上右下的间隔属性----->label_padding
 * <p>
 * 2.分割线（底部水平下划线，右侧垂直分割线）
 * 如果统一设置分割线颜色------------------>line_color
 * 如果单一设置分割线颜色------------------>line_bottom_color,line_right_color(line_right_color暂时没用到)
 * <p>
 * 如果统一设置分割线离边上的距间距--------------line_padding
 * 如果单一设置分割线离边上的距间距------------------>line_bottom_padding,line_right_padding
 * <p>
 * <p>
 * 3.方法
 *
 * @see #setEllipsizeText  当你设置signle的时候  会发现ondraw的汉字不显示了
 * 解决办法  设置最大显示字符数并且setEllipsizeText替代settext
 */
public class LabelTextView extends AppCompatTextView {
    private Paint labelPaint, linePaint;
    private float textBaseLine, textHeight;//文字基线,文本高度

    private String labelTextTop;
    private String labelTextLeft;

    private String labelTextRight;
    private String labelTextBottom;

    private float labelPadding;
    private float labelPaddingLeft;
    private float labelPaddingRight;
    private float labelPaddingTop;
    private float labelPaddingBottom;
    private float linePaddingRight;
    private float linePaddingBottom;
    private boolean draw_bottom_line;
    private boolean draw_right_line;
    private boolean draw_bottom_line_without_right;
    private int bottom_line_color;
    private int right_line_color;

    private float labelTextSize;
    private float labelTextLeftSize;
    private float labelTextTopSize;
    private float labelTextRightSize;
    private float labelTextBottomSize;
    private float defaultTextSize = 14;

    private int labelTextColor;
    private int labelTextColorLeft;
    private int labelTextColorTop;
    private int labelTextColorRight;
    private int labelTextColorBottom;


    private static final int INVALIDATE_VALUE = -1;
    private StaticLayout mLayout;

    public LabelTextView(Context context) {
        super(context);
    }


    public LabelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public LabelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public void init(AttributeSet attrs) {

        setImeOptions(EditorInfo.IME_ACTION_NEXT);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LabelTextView);

        labelTextTop = typedArray.getString(R.styleable.LabelTextView_label_text_top);
        labelTextLeft = typedArray.getString(R.styleable.LabelTextView_label_text_left);
        labelTextRight = typedArray.getString(R.styleable.LabelTextView_label_text_right);
        labelTextBottom = typedArray.getString(R.styleable.LabelTextView_label_text_bottom);


        labelTextSize = typedArray.getDimension(R.styleable.LabelTextView_label_text_size, defaultTextSize);
        labelTextLeftSize = typedArray.getDimension(R.styleable.LabelTextView_label_text_size_left, labelTextSize);
        labelTextTopSize = typedArray.getDimension(R.styleable.LabelTextView_label_text_size_top, labelTextSize);
        labelTextRightSize = typedArray.getDimension(R.styleable.LabelTextView_label_text_size_right, labelTextSize);
        labelTextBottomSize = typedArray.getDimension(R.styleable.LabelTextView_label_text_size_bottom, labelTextSize);

        labelTextColor = typedArray.getColor(R.styleable.LabelTextView_label_text_color, getCurrentTextColor());
        labelTextColorLeft = typedArray.getColor(R.styleable.LabelTextView_label_text_color_left, labelTextColor);
        labelTextColorTop = typedArray.getColor(R.styleable.LabelTextView_label_text_color_top, labelTextColor);
        labelTextColorRight = typedArray.getColor(R.styleable.LabelTextView_label_text_color_right, labelTextColor);
        labelTextColorBottom = typedArray.getColor(R.styleable.LabelTextView_label_text_color_bottom, labelTextColor);

        labelPadding = typedArray.getDimension(R.styleable.LabelTextView_label_padding, 0);
        labelPaddingLeft = typedArray.getDimension(R.styleable.LabelTextView_label_padding_left, 0);
        labelPaddingRight = typedArray.getDimension(R.styleable.LabelTextView_label_padding_right, 0);
        labelPaddingTop = typedArray.getDimension(R.styleable.LabelTextView_label_padding_top, 0);
        labelPaddingBottom = typedArray.getDimension(R.styleable.LabelTextView_label_padding_bottom, 0);
        draw_bottom_line_without_right = typedArray.getBoolean(R.styleable.LabelTextView_draw_bottom_line_without_right, false);
        draw_bottom_line = typedArray.getBoolean(R.styleable.LabelTextView_draw_bottom_line, false);
        draw_right_line = typedArray.getBoolean(R.styleable.LabelTextView_draw_right_line, false);
        bottom_line_color = typedArray.getColor(R.styleable.LabelTextView_bottom_line_color, 0);
        right_line_color = typedArray.getColor(R.styleable.LabelTextView_right_line_color, 0);
        linePaddingRight = typedArray.getDimension(R.styleable.LabelTextView_line_padding_right, 0);
        linePaddingBottom = typedArray.getDimension(R.styleable.LabelTextView_line_padding_bottom, 0);

        typedArray.recycle();

        labelPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setTextSize(labelTextSize);
        labelPaint.setColor(labelTextColor);


        if (draw_bottom_line || draw_right_line || draw_bottom_line_without_right) {
            linePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            linePaint.setStyle(Paint.Style.FILL);
            if (bottom_line_color == 0)
                bottom_line_color = Color.parseColor("#d9dadd");
            linePaint.setColor(bottom_line_color);
            linePaint.setStrokeWidth(1);

        }

        setGravity(getGravity());
    }

    private void makeBaseLine() {
        Paint.FontMetrics fontMetrics = labelPaint.getFontMetrics();
        textHeight = fontMetrics.descent - fontMetrics.ascent;
        textBaseLine = (fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
    }

    public void setLabelTextLeft(String labelTextLeft) {
        this.labelTextLeft = labelTextLeft;
        invalidate();
    }

    public void setLabelTextTop(String labelTextTop1) {
        this.labelTextTop = labelTextTop1;
        invalidate();
    }

    public void setLabelTextRight(String labelTextRight) {
        this.labelTextRight = labelTextRight;
        invalidate();
    }

    public void setLabelTextBottom(String labelTextBottom1) {
        this.labelTextBottom = labelTextBottom1;
        invalidate();
    }

    public void setLabelTexts(String labelTextLeft, String labelTextTop, String labelTextRight, String labelTextBottom) {
        if (!TextUtils.isEmpty(labelTextLeft))
            this.labelTextLeft = labelTextLeft;
        if (!TextUtils.isEmpty(labelTextTop))
            this.labelTextTop = labelTextTop;
        if (!TextUtils.isEmpty(labelTextRight))
            this.labelTextRight = labelTextRight;
        if (!TextUtils.isEmpty(labelTextBottom))
            this.labelTextBottom = labelTextBottom;
        invalidate();
    }

    public void setLabelLeftSize(int size) {
        this.labelTextLeftSize = sp2px(getContext(), size);
        invalidate();
    }

    public void setLabelTopSize(int size) {
        this.labelTextTopSize = sp2px(getContext(), size);
        invalidate();
    }

    public void setLabelRightSize(int size) {
        this.labelTextRightSize = sp2px(getContext(), size);
        invalidate();
    }

    public void setLabelBottomSize(int size) {
        this.labelTextBottomSize = sp2px(getContext(), size);
        invalidate();
    }

    public void setLabelPadding(int left, int top, int right, int bottom) {
        if (left != INVALIDATE_VALUE)
            this.labelPaddingLeft = left;
        if (right != INVALIDATE_VALUE)
            this.labelPaddingRight = right;
        if (top != INVALIDATE_VALUE)
            this.labelPaddingTop = top;
        if (bottom != INVALIDATE_VALUE)
            this.labelPaddingBottom = bottom;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();


        if (!TextUtils.isEmpty(labelTextLeft)) {
            labelPaint.setTextSize(labelTextLeftSize);
            labelPaint.setColor(labelTextColorLeft);
            makeBaseLine();
            float baseline = height / 2 + textBaseLine / 2;
            canvas.drawText(labelTextLeft, getPaddingLeft() + (labelPaddingLeft == 0 ? labelPadding : labelPaddingLeft), baseline, labelPaint);
        }

        if (!TextUtils.isEmpty(labelTextTop)) {
            labelPaint.setTextSize(labelTextTopSize);
            labelPaint.setColor(labelTextColorTop);
            makeBaseLine();
            float baseline = height / 2 + textBaseLine / 2;
            canvas.drawText(labelTextTop, (width - labelPaint.measureText(labelTextTop)) / 2, (labelPaddingTop == 0 ? labelPadding : labelPaddingTop) + getPaddingTop() + textBaseLine, labelPaint);
        }
        if (!TextUtils.isEmpty(labelTextRight)) {
            labelPaint.setTextSize(labelTextRightSize);
            labelPaint.setColor(labelTextColorRight);
            makeBaseLine();
            float baseline = height / 2 + textBaseLine / 2;
            canvas.drawText(labelTextRight, (width - labelPaint.measureText(labelTextRight) - (labelPaddingRight == 0 ? labelPadding : labelPaddingRight)) - getPaddingRight(), baseline, labelPaint);
        }

        if (!TextUtils.isEmpty(labelTextBottom)) {
            labelPaint.setTextSize(labelTextBottomSize);
            labelPaint.setColor(labelTextColorBottom);
            makeBaseLine();
            float baseline = height / 2 + textBaseLine / 2;
            canvas.drawText(labelTextBottom, (width - labelPaint.measureText(labelTextBottom)) / 2, height - (labelPaddingBottom == 0 ? labelPadding : labelPaddingBottom) - getPaddingBottom(), labelPaint);
        }

        /*绘制底部下划线 */
        if (draw_bottom_line) {
            if (linePaddingBottom == 0)
                linePaddingBottom = labelPadding;
            if (bottom_line_color != 0) linePaint.setColor(bottom_line_color);
            canvas.drawLine(linePaddingBottom, getMeasuredHeight(), getMeasuredWidth() - linePaddingBottom, getMeasuredHeight(), linePaint);

        }

         /*绘制底部下划线 */
        if (draw_bottom_line_without_right) {
            if (linePaddingBottom == 0)
                linePaddingBottom = labelPadding;
            if (bottom_line_color != 0) linePaint.setColor(bottom_line_color);
            canvas.drawLine(linePaddingBottom, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), linePaint);

        }

         /*绘制右侧划线 */
        if (draw_right_line) {
            if (linePaddingRight == 0)
                linePaddingRight = labelPadding;
            if (right_line_color != 0) linePaint.setColor(right_line_color);
            canvas.drawLine(getMeasuredWidth(), linePaddingRight, getMeasuredWidth(), getMeasuredHeight() - linePaddingRight, linePaint);
        }
    }


    public void setEllipsizeText(String text, int maxLength) {
        if (TextUtils.isEmpty(text)) {
            setText("");
            return;
        }
        setText(ellipsizeString(text, maxLength));
    }


    public void setEllipsizeText(String text) {
        if (TextUtils.isEmpty(text)) {
            setText("");
            return;
        }
        int maxLen = getMaxLength();
        setText(ellipsizeString(text, maxLen));
    }

    private String ellipsizeString(String text, int maxLen) {
        float width = getPaint().measureText(text);
        return (width > getMeasuredWidth() - getPaddingLeft() - getPaddingRight() || text.length() >= maxLen) ? text.substring(0, maxLen - 3) + "..." : text;
    }

    public int getMaxLength() {
        int length = 0;
        try {
            InputFilter[] inputFilters = getFilters();
            for (InputFilter filter : inputFilters) {
                Class<?> c = filter.getClass();
                if (c.getName().equals("android.text.InputFilter$LengthFilter")) {
                    Field[] f = c.getDeclaredFields();
                    for (Field field : f) {
                        if (field.getName().equals("mMax")) {
                            field.setAccessible(true);
                            length = (Integer) field.get(filter);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private int dp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * fontScale + 0.5f);
    }
}
