package com.android.banana.pullrecycler.recyclerview;

/**
 * Created by mrs on 2017/4/10.
 * * RecyclerView中实现divider功能
 * 只支持LinearLayoutManager
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable dividerDrawable;
    private int orientation = LinearLayoutManager.VERTICAL;
    private boolean includeFirstPos;

    public DividerItemDecoration(Drawable divider) {
        dividerDrawable = divider;
    }

    public DividerItemDecoration(Context context, int resId) {
        this(context, resId, LinearLayoutManager.VERTICAL);
    }

    public DividerItemDecoration(Context context, int resId, int orientation) {
        this(context, resId, orientation, false);
    }

    public DividerItemDecoration(Context context, int resId, int orientation, boolean includeFirstPos) {
        this.includeFirstPos = includeFirstPos;
        this.orientation = orientation;
        this.dividerDrawable = context.getResources().getDrawable(resId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (dividerDrawable == null) {
            return;
        }

        //如果是不包括第一个item，不需要divider，所以直接return
        if (!includeFirstPos && parent.getChildLayoutPosition(view) <= 0) {
            return;
        }

        //相当于给itemView设置margin，给divider预留空间
        if (orientation == LinearLayoutManager.VERTICAL) {
            outRect.top = dividerDrawable.getIntrinsicHeight();
        } else if (orientation == LinearLayoutManager.HORIZONTAL) {
            outRect.left = dividerDrawable.getIntrinsicWidth();
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (dividerDrawable == null) {
            return;
        }

        int childCount = parent.getChildCount();
        Rect rectF = new Rect();
        dividerDrawable.getPadding(rectF);
        if (orientation == LinearLayoutManager.VERTICAL) {
            int right = parent.getWidth() - rectF.right;
            for (int i = 0; i < childCount; i++) {
                if (!includeFirstPos && i == 0)
                    continue;

                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int left = rectF.left;
                int bottom = child.getTop() - params.topMargin - rectF.bottom;
                int top = bottom - dividerDrawable.getIntrinsicHeight() + rectF.top;
                dividerDrawable.setBounds(left, top, right, bottom);
                dividerDrawable.draw(c);
            }
        } else if (orientation == LinearLayoutManager.HORIZONTAL) {
            for (int i = 0; i < childCount; i++) {
                if (!includeFirstPos && i == 0)
                    continue;
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int top = parent.getPaddingTop() + child.getPaddingTop();
                int bottom = child.getHeight() + parent.getPaddingTop();
                int right = child.getLeft() - params.leftMargin;
                int left = right - dividerDrawable.getIntrinsicWidth();
                dividerDrawable.setBounds(left, top, right, bottom);
                dividerDrawable.draw(c);
            }
        }
    }

    public int getItemOffsets() {
        //相当于给itemView设置margin，给divider预留空间
        if (orientation == LinearLayoutManager.VERTICAL) {
            return dividerDrawable.getIntrinsicHeight();
        } else if (orientation == LinearLayoutManager.HORIZONTAL) {
            return dividerDrawable.getIntrinsicWidth();
        }
        return 0;
    }
}
