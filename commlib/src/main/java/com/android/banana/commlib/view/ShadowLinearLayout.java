package com.android.banana.commlib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by lingjiu on 2017/11/2.
 */
public class ShadowLinearLayout extends LinearLayout {

    public ShadowLinearLayout(Context context) {
        super(context);
    }

    public ShadowLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadowLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setShadowLayer(10f, 0, 0, Color.parseColor("#40060001"));
        canvas.drawRect(new RectF(0, getHeight(), getWidth(), getHeight() + 10), paint);
        //canvas.drawRoundRect(new RectF(0f, 0f, 40, getHeight()), getWidth(), getHeight(), paint);
    }

}

