package com.etiennelawlor.imagegallery.library.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by etiennelawlor on 1/8/15.
 */
public class GridSpacesItemDecoration extends RecyclerView.ItemDecoration {

    // region Member Variables
    private final int mSpace;
    private final int mNumOfColumns;
    private final int mVerticalSpace;
    // endregion

    // region Constructors
    public GridSpacesItemDecoration(int space, int numOfColumns) {
        mSpace = space;
        mVerticalSpace = space;
        mNumOfColumns = numOfColumns;
    }

    /**
     * @param horizontalSpace 横向的间距
     * @param verticalSpace   垂直的间距
     * @param numOfColumns
     */
    public GridSpacesItemDecoration(int horizontalSpace, int verticalSpace, int numOfColumns) {
        mSpace = horizontalSpace;
        mVerticalSpace = verticalSpace;
        mNumOfColumns = numOfColumns;
    }
    // endregion

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);
        if (position < mNumOfColumns) {
            outRect.top = mSpace;
        }

        if (mNumOfColumns == 2) {
            if (position % mNumOfColumns == 0) {
                outRect.left = mSpace;
                outRect.right = mSpace / 2;
            } else {
                outRect.left = mSpace / 2;
                outRect.right = mSpace;
            }
        } else if (mNumOfColumns == 3) {
            if (position % mNumOfColumns == 0) {
                outRect.left = mSpace;
                outRect.right = mSpace / 2;
            } else if (position % mNumOfColumns == 1) {
                outRect.left = mSpace / 2;
                outRect.right = mSpace / 2;
            } else {
                outRect.left = mSpace / 2;
                outRect.right = mSpace;
            }
        } else if (mNumOfColumns == 4) {
            if (position % mNumOfColumns == 0) {
                outRect.left = mSpace;
                outRect.right = mSpace / 2;
            } else if (position % mNumOfColumns == 1) {
                outRect.left = mSpace / 2;
                outRect.right = mSpace / 2;
            } else if (position % mNumOfColumns == 2) {
                outRect.left = mSpace / 2;
                outRect.right = mSpace / 2;
            } else {
                outRect.left = mSpace / 2;
                outRect.right = mSpace;
            }
        }

        outRect.bottom = mVerticalSpace;
    }
}

