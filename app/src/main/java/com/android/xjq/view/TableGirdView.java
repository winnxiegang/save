package com.android.xjq.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.android.xjq.R;
import com.android.library.Utils.LogUtils;

/**
 * 给gridView绘制边框(像表格一样)
 * 需要设置一定的padding,否则最下面一行边框可能绘制不出来
 * <p>
 * Created by lingjiu on 2017/9/18.
 */

public class TableGirdView extends GridView {

    private Paint mPaint;

    public TableGirdView(Context context) {
        super(context);
    }

    public TableGirdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TableGirdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (getChildCount() == 0) return;
        View firstChildView = getChildAt(0);
        int column = getWidth() / firstChildView.getWidth();//计算出一共有多少列，假设有3列
        int childCount = getChildCount();//子view的总数
        LogUtils.e("TableGirdView", "子view的总数childCount==" + childCount);
        mPaint = getPaint();
        for (int i = 0; i < childCount; i++) {//遍历子view
            View cellView = getChildAt(i);//获取子view
            if (i < 3) {//第一行
                canvas.drawLine(cellView.getLeft(), cellView.getTop(), cellView.getRight(), cellView.getTop(), mPaint);
            }
            if (i % column == 0) {//第一列
                canvas.drawLine(cellView.getLeft(), cellView.getTop(), cellView.getLeft(), cellView.getBottom(), mPaint);
            }
            if ((i + 1) % column == 0) {//第三列
                //画子view底部横线
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), mPaint);
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), mPaint);
            } else if ((i + 1) > (childCount - (childCount % column))) {//如果view是最后一行
                //画子view的右边竖线
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), mPaint);
                //画子view的底部横线
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), mPaint);
            } else {//如果view不是最后一行
                //画子view的右边竖线
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), mPaint);
                //画子view的底部横线
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), mPaint);
            }
        }
    }


    private Paint getPaint() {
        Paint mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getContext().getResources().getColor(R.color.line));
        return mPaint;
    }

}
