package com.android.xjq.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.android.xjq.R;

public class FlowLayout extends ViewGroup {

    private int horizontalSpacing = 0;
    private int verticalSpacing = 0;

    public FlowLayout(Context context) {
        super(context);

        this.readStyleParameters(context, null);
    }

    public FlowLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        this.readStyleParameters(context, attributeSet);
    }

    public FlowLayout(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);

        this.readStyleParameters(context, attributeSet);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
                - this.getPaddingRight() - this.getPaddingLeft();
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
                - this.getPaddingTop() - this.getPaddingBottom();

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int size;
        int mode;

        size = sizeWidth;
        mode = modeWidth;

        int lineThicknessWithSpacing = 0;
        int lineThickness = 0;
        int lineLengthWithSpacing = 0;
        int lineLength;

        int prevLinePosition = 0;

        int controlMaxLength = 0;
        int controlMaxThickness = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            child.measure(
                    getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft()
                            + this.getPaddingRight(), lp.width),
                    getChildMeasureSpec(heightMeasureSpec, this.getPaddingTop()
                            + this.getPaddingBottom(), lp.height));

            int hSpacing = this.getHorizontalSpacing(lp);
            int vSpacing = this.getVerticalSpacing(lp);

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            int childLength;
            int childThickness;
            int spacingLength;
            int spacingThickness;

            childLength = childWidth;
            childThickness = childHeight;
            spacingLength = hSpacing;
            spacingThickness = vSpacing;

            lineLength = lineLengthWithSpacing + childLength;
            lineLengthWithSpacing = lineLength + spacingLength;

            boolean newLine = lp.newLine
                    || (mode != MeasureSpec.UNSPECIFIED && lineLength > size);
            if (newLine) {
                prevLinePosition = prevLinePosition + lineThicknessWithSpacing;

                lineThickness = childThickness;
                lineLength = childLength;
                lineThicknessWithSpacing = childThickness + spacingThickness;
                lineLengthWithSpacing = lineLength + spacingLength;
            }

            lineThicknessWithSpacing = Math.max(lineThicknessWithSpacing,
                    childThickness + spacingThickness);
            lineThickness = Math.max(lineThickness, childThickness);

            int posX;
            int posY;
            posX = getPaddingLeft() + lineLength - childLength;
            posY = getPaddingTop() + prevLinePosition;
            lp.setPosition(posX, posY);

            controlMaxLength = Math.max(controlMaxLength, lineLength);
            controlMaxThickness = prevLinePosition + lineThickness;
        }

		/* need to take paddings into account */
        controlMaxLength += getPaddingLeft() + getPaddingRight();
        controlMaxThickness += getPaddingBottom() + getPaddingTop();

        this.setMeasuredDimension(
                resolveSize(controlMaxLength, widthMeasureSpec),
                resolveSize(controlMaxThickness, heightMeasureSpec));
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }

    private int getVerticalSpacing(LayoutParams lp) {
        int vSpacing;
        if (lp.verticalSpacingSpecified()) {
            vSpacing = lp.verticalSpacing;
        } else {
            vSpacing = this.verticalSpacing;
        }
        return vSpacing;
    }

    private int getHorizontalSpacing(LayoutParams lp) {
        int hSpacing;
        if (lp.horizontalSpacingSpecified()) {
            hSpacing = lp.horizontalSpacing;
        } else {
            hSpacing = this.horizontalSpacing;
        }
        return hSpacing;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y
                    + child.getMeasuredHeight());
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean more = super.drawChild(canvas, child, drawingTime);
        return more;
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    private void readStyleParameters(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet,
                R.styleable.FlowLayout);
        try {
            horizontalSpacing = a.getDimensionPixelSize(
                    R.styleable.FlowLayout_horizontalSpacing, 0);
            verticalSpacing = a.getDimensionPixelSize(
                    R.styleable.FlowLayout_verticalSpacing, 0);
        } finally {
            a.recycle();
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        private static int NO_SPACING = -1;
        private int x;
        private int y;
        private int horizontalSpacing = NO_SPACING;
        private int verticalSpacing = NO_SPACING;
        private boolean newLine = false;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.readStyleParameters(context, attributeSet);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public boolean horizontalSpacingSpecified() {
            return horizontalSpacing != NO_SPACING;
        }

        public boolean verticalSpacingSpecified() {
            return verticalSpacing != NO_SPACING;
        }

        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private void readStyleParameters(Context context, AttributeSet attributeSet) {
            horizontalSpacing = NO_SPACING;
            verticalSpacing = NO_SPACING;
            newLine = false;
        }
    }
}
