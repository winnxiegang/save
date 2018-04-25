package com.android.banana.commlib.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.android.library.Utils.LibAppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaomu on 2017/11/10.
 */

public class PageIndicatorView extends LinearLayout {

    private Context mContext = null;
    private int dotSize = 6; // 指示器的大小（dp）
    private int margins = 4; // 指示器间距（dp）
    private List<View> indicatorViews = null; // 存放指示器
    private int indicatorSelectColor =  Color.GRAY;//选中后的颜色
    private int indicatorUnSelectColor = Color.LTGRAY;//没被选中时 的颜色
    private int mSelected;

    public PageIndicatorView(Context context) {
        this(context, null);
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);

        dotSize = LibAppUtil.dip2px(context, dotSize);
        margins = LibAppUtil.dip2px(context, margins);
    }

    // 初始化指示器，默认选中第一页

    public void initIndicator(int count) {
        if (indicatorViews == null) {
            indicatorViews = new ArrayList<>();
        } else {
            indicatorViews.clear();
            removeAllViews();
        }
        if (count <= 1)
            return;
        View view;
        LayoutParams params = new LayoutParams(dotSize, dotSize);
        params.setMargins(margins, margins, margins, margins);
        for (int i = 0; i < count; i++) {
            view = new View(mContext);
            ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
            drawable.setColorFilter(indicatorUnSelectColor, PorterDuff.Mode.SRC);
            view.setBackgroundDrawable(drawable);
            addView(view, params);
            indicatorViews.add(view);
        }
    }

    //设置选中页
    public void setSelectedPage(int selected) {
        mSelected = selected;
        if (indicatorViews == null)
            return;
        for (int i = 0; i < indicatorViews.size(); i++) {
            if (i == selected) {
                indicatorViews.get(i).getBackground().setColorFilter(indicatorSelectColor, PorterDuff.Mode.SRC);
            } else {
                indicatorViews.get(i).getBackground().setColorFilter(indicatorUnSelectColor, PorterDuff.Mode.SRC);
            }
        }
    }

    public void setIndicatorColor(int selectColor, int unSelectColor) {
        if (this.indicatorSelectColor == selectColor && indicatorUnSelectColor == unSelectColor)
            return;

        this.indicatorSelectColor = selectColor;
        this.indicatorUnSelectColor = unSelectColor;

        if (indicatorViews == null || indicatorViews.size() == 0)
            return;

        initIndicator(indicatorViews.size());
        setSelectedPage(mSelected);
    }

    public void setIndicatorSizeAndMargin(int size, int margin) {
        if (this.dotSize == size && this.margins == margin)
            return;

        this.dotSize = size;
        this.margins = margin;

        if (indicatorViews == null || indicatorViews.size() == 0)
            return;

        initIndicator(indicatorViews.size());
        setSelectedPage(mSelected);
    }

    public void clear() {
        if (indicatorViews == null || indicatorViews.size() == 0)
            return;
        indicatorViews.clear();
        removeAllViews();
    }
}
