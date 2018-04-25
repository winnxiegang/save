package com.android.banana.pullrecycler.recyclerview;

/**
 * Created by qiaomu on 2017/5/27.
 * <p>
 * Created by mrs on 2017/4/10.
 * * RecyclerView中实现divider功能
 * 只支持LinearLayoutManager
 * <p>
 * 指定位置处添加Decoration
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PositionDividerDecoration extends RecyclerView.ItemDecoration {

    private Drawable dividerDrawable;
    private int orientation = LinearLayoutManager.VERTICAL;
    private int position;

    public PositionDividerDecoration(Context context, int resId, int orientation, int position) {
        this.position = position;
        dividerDrawable = context.getResources().getDrawable(resId);
        dividerDrawable = context.getResources().getDrawable(resId);
        this.orientation = orientation;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (dividerDrawable == null) {
            return;
        }

        int positionInAdapter = parent.getChildAdapterPosition(view);
        //相当于给itemView设置margin，给divider预留空间
        if (orientation == LinearLayoutManager.VERTICAL && positionInAdapter == position) {
            outRect.bottom = dividerDrawable.getIntrinsicHeight();
        } else if (orientation == LinearLayoutManager.HORIZONTAL && positionInAdapter == position) {
            outRect.left = dividerDrawable.getIntrinsicWidth();
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (dividerDrawable == null) {
            return;
        }
        LinearLayoutManager manager = (LinearLayoutManager) parent.getLayoutManager();
        int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        if (lastVisibleItemPosition < position)
            return;
        Rect rectF = new Rect();
        dividerDrawable.getPadding(rectF);//17  16  10
        View child = parent.getChildAt(position - firstVisibleItemPosition);
        if (child == null)
            return;
        if (orientation == LinearLayoutManager.VERTICAL) {
            int right = parent.getWidth() - rectF.right;


            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = parent.getPaddingLeft() + child.getPaddingLeft() + rectF.left;
            int top = child.getBottom();
            int bottom = top + dividerDrawable.getIntrinsicHeight();
            // bottom - dividerDrawable.getIntrinsicHeight() + rectF.top;
            dividerDrawable.setBounds(left, top, right, bottom);
            dividerDrawable.draw(c);

        } else if (orientation == LinearLayoutManager.HORIZONTAL) {

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

