package com.android.banana.commlib.view.expandtv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.android.banana.commlib.R;

/**
 * 自定义带字数统计的editText
 * Created by lingjiu on 2018/2/2.
 */

public class NumLimitEditText extends AppCompatEditText {

    public static final int LOCATION_RIGHT_BOTTOM = 0;
    public static final int LOCATION_RIGHT_CENTER = 1;

    private Paint mPaint;//画笔
    private Rect mBound;//文本绘制的范围
    private int editWidth;//输入框的宽高
    private int editHeight;
    private int numLength = 0;
    private String numStr = "0/" + numLength;
    private int mLocation = LOCATION_RIGHT_BOTTOM;//0代表右下角，1代表右中间
    private boolean isShowLimitText;

    public NumLimitEditText(Context context) {
        this(context, null);
    }

    public NumLimitEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumLimitEditText);
        numLength = array.getInt(R.styleable.NumLimitEditText_maxLength, 0);
        isShowLimitText = array.getBoolean(R.styleable.NumLimitEditText_show_limit_text, true);
        array.recycle();
        initView();
    }

    private void initView() {
        //设置最多输入的字数
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(numLength)});
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int colorGray = getResources().getColor(R.color.colorTextG2);
        mPaint.setColor(colorGray);
        mPaint.setTextSize(getTextSize());
        mBound = new Rect();
        //初始化时候获取统计str的最小矩形的长宽。
        mPaint.getTextBounds(numStr, 0, numStr.length(), mBound);
        setPadding(10, 0, 10, 20);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        editWidth = getWidth();
        editHeight = getHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("onDraw: ", getPaddingBottom() + "");
        //绘制字数统计
        if (!isShowLimitText) return;
        if (TextUtils.isEmpty(getText()))
            return;
        if (mLocation == LOCATION_RIGHT_BOTTOM) {
            canvas.drawText(numStr, editWidth - mBound.width() - 10, getHeight() - mBound.height() + getScrollY() - 5, mPaint);
        } else {
            canvas.drawText(numStr, editWidth - mBound.width() - 10, getHeight() / 2 + mBound.height() / 2, mPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start,
                                 int lengthBefore, int lengthAfter) {
        //      super.onTextChanged(text, start, lengthBefore, lengthAfter);
        editHeight = getHeight();//目的在edit输入到第二行的时候刷新一下edit的高度重绘字数统计的高度
        int num = text.length();
        numStr = num + "/" + numLength;
        //初始化时候获取统计str的最小矩形的长宽。
        if (mPaint != null)
            mPaint.getTextBounds(numStr, 0, numStr.length(), mBound);
        invalidate();
    }

    public void setMaxLength(int maxLength) {
        numLength = maxLength;
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(numLength)});
        initView();
        invalidate();
    }

    public void setCountLocation(int location) {
        mLocation = location;
        if (location == LOCATION_RIGHT_BOTTOM) {
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom() + mBound.height() * 2);
        }
        invalidate();
    }

    public void setEditHint(String str) {
        setHint(str);
        //重绘
        requestLayout();
    }
}
