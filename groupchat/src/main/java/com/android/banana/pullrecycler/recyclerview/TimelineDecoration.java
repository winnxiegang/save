package com.android.banana.pullrecycler.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.android.banana.commlib.utils.TimeUtils;
import com.android.library.Utils.LibAppUtil;

/**
 * Created by mrs on 2017/5/9.
 * <p>
 * 列表当中 显示两条消息之间的时间
 */

public class TimelineDecoration extends RecyclerView.ItemDecoration {
    private Paint timePaint;
    private float textHeight;
    private int padding;

    private static final long TIME_GAG = 180L;

    public TimelineDecoration(Context context, TimeLineCallback callback) {
        timePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timePaint.setColor(Color.parseColor("#8e8e8e"));//""
        timePaint.setTextSize(30);
        padding = LibAppUtil.dip2px(context, 10);
        this.callback = callback;

        Paint.FontMetrics metrics = timePaint.getFontMetrics();
        textHeight = metrics.descent - metrics.ascent;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (callback == null || parent.getChildCount() == 0 || view.getVisibility() == View.GONE)
            return;

        if (position == 0) {
            outRect.top = (int) (padding + textHeight);
            return;
        }

        String preTime = callback.getPreMessageTime(position - 1);
        String time = callback.getMessageTime(position);
        long diffSeconds = TimeUtils.getDiffSecTwoDateStr(time, preTime);
        if (diffSeconds < TIME_GAG) {
            return;
        }
        outRect.top = (int) (padding + textHeight);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (callback == null || parent.getChildCount() == 0)
            return;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (child.getVisibility() == View.GONE)
                continue;
            int adapterPosition = parent.getChildAdapterPosition(child);

            boolean couponType = callback.isCouponMessage(adapterPosition);
            String time = callback.getMessageTime(adapterPosition);

            if (TextUtils.isEmpty(time))
                continue;
            String timeLine = TimeUtils.getTimeLine(time);
            String preTime = "";
            long diffSeconds = TIME_GAG;
            if (adapterPosition > 0) {
                preTime = callback.getPreMessageTime(adapterPosition - 1);
                diffSeconds = TimeUtils.getDiffSecTwoDateStr(time, preTime);
            }
            if (diffSeconds < TIME_GAG || TextUtils.isEmpty(timeLine))
                continue;
            c.drawText(timeLine,
                    parent.getWidth() / 2 - timePaint.measureText(timeLine) / 2,
                    (couponType ? child.getTop() - textHeight / 2 : child.getTop()),
                    timePaint);


        }
    }

    private TimeLineCallback callback;

    public interface TimeLineCallback {
        String getMessageTime(int position);

        String getPreMessageTime(int prePosition);

        boolean isCouponMessage(int position);
    }

}
