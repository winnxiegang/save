package com.android.xjq.view.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.banana.commlib.utils.DimensionUtils;

/**
 * 设置RecyclerView LinearLayoutManager spacing
 * Created by lingjiu on 2018/3/16.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;


    public SpacesItemDecoration(Context context, int space) {
        this.space = (int) DimensionUtils.dpToPx(space, context);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}
