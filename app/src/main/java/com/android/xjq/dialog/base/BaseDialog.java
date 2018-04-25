package com.android.xjq.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.banana.commlib.utils.DimensionUtils;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.xjq.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lingjiu on 2018/2/2.
 */

public abstract class BaseDialog extends Dialog {

    private static final String MARGIN = "margin";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String DIM = "dim_amount";
    private static final String BOTTOM = "show_bottom";
    private static final String CANCEL = "out_cancel";
    private static final String ANIM = "anim_style";
    private static final String LAYOUT = "layout_id";
    private Unbinder unBind;
    private int margin;//左右边距
    private int verticalMargin;//距下边距
    private int width;//宽度
    private int height;//高度
    private float dimAmount = 0.5f;//灰度深浅
    private boolean showBottom;//是否底部显示
    private boolean outCancel = true;//是否点击外部取消
    @StyleRes
    private int animStyle;
    @LayoutRes
    protected int layoutId;
    protected Context mContext;
    private boolean isConverPx = true;
    private int gravity = Gravity.NO_GRAVITY;

    public abstract int intLayoutId();

    public abstract void convertView(ViewHolder holder, BaseDialog dialog);

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.dialog_base);
        layoutId = intLayoutId();
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(getContext(), layoutId, null);
        convertView(ViewHolder.create(view), this);
        setContentView(view);
        unBind = ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initParams();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unBind.unbind();
    }

    private void initParams() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount;
            //是否在底部显示
            if (showBottom) {
                lp.gravity = Gravity.BOTTOM;
                if (animStyle == 0) {
                    animStyle = R.style.dialog_anim_bottom;
                }
                lp.y = (int) DimensionUtils.dpToPx(verticalMargin, mContext);
            } else {
                lp.gravity = gravity;
            }
            //设置dialog宽度
            if (width == 0) {
                lp.width = (int) (LibAppUtil.getScreenWidth(mContext) - 2 * DimensionUtils.dpToPx(margin, mContext));
            } else if (width == -1) {
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                lp.width = isConverPx ? (int) DimensionUtils.dpToPx(width, getContext()) : width;
            }

            //设置dialog高度
            if (height == 0) {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                lp.height = isConverPx ? (int) DimensionUtils.dpToPx(height, getContext()) : height;
            }

            //设置dialog进入、退出的动画
            window.setWindowAnimations(animStyle);
            window.setAttributes(lp);
        }
        setCancelable(outCancel);
        setCanceledOnTouchOutside(outCancel);
    }

    /***
     *
     * @param margin  dp值
     * @return
     */
    public BaseDialog setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    //距上边距
    public BaseDialog setVerticalMargin(int margin) {
        this.verticalMargin = margin;
        return this;
    }


    public BaseDialog setWidth(int width) {
        this.width = width;
        return this;
    }

    public BaseDialog setHeight(int height) {
        this.height = height;
        return this;
    }

    public BaseDialog setIsNeedConverPx(boolean isConverPx) {
        this.isConverPx = isConverPx;
        return this;
    }

    /**
     * dialog灰度深浅
     *
     * @param dimAmount 0-1 (0代表无灰色背景)
     * @return
     */
    public BaseDialog setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    public BaseDialog setShowBottom(boolean showBottom) {
        this.showBottom = showBottom;
        return this;
    }

    public BaseDialog setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    /**
     * @param outCancel 是否点击外部取消 默认true
     * @return
     */
    public BaseDialog setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    public BaseDialog setAnimStyle(@StyleRes int animStyle) {
        this.animStyle = animStyle;
        return this;
    }
}
