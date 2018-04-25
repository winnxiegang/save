package com.android.xjq.dialog.popupwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.banana.commlib.utils.Money;
import com.android.xjq.R;
import com.android.xjq.utils.live.SpannableStringHelper;

/**
 * Created by lingjiu on 2017/6/27.
 */

public class LookWalletPop {


    private int popupHeight;
    private View parentView;
    private View mDropDownView;
    private PopupWindow mPopupWindow;
    private TextView goldTv;
    private TextView pointTv;
    private TextView giftGoldTv;

    private Context context;

    public LookWalletPop(Context context, View dropDownView) {
        this.mDropDownView = dropDownView;
        this.context = context;
        initView(context);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ((CheckBox) mDropDownView).setChecked(false);
            }
        });
    }

    /**
     * 初始化控件
     *
     * @param context
     */
    private void initView(Context context) {
        parentView = View.inflate(context, R.layout.layout_popupwindow_look_wallet, null);
        goldTv = ((TextView) parentView.findViewById(R.id.goldTv));
        pointTv = ((TextView) parentView.findViewById(R.id.pointTv));
        giftGoldTv = ((TextView) parentView.findViewById(R.id.giftGoldTv));
        setPopConfig();
        mPopupWindow.setContentView(parentView);
    }


    /**
     * 配置弹出框属性
     */
    private void setPopConfig() {
        mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /*setWidth(mDropDownView.getScreenWidth() + 25);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);*/
        // 设置弹出窗体可点击
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
        mPopupWindow.setOutsideTouchable(true);// 设置外部触摸会关闭窗口
        //获取自身的长宽高
        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = parentView.getMeasuredHeight();
    }

    /**
     * 设置显示在v上方(以v的左边距为开始位置)
     *
     * @param
     */
    public void showUp() {
        ((CheckBox) mDropDownView).setChecked(true);
        mPopupWindow.showAsDropDown(mDropDownView, 0, -popupHeight - mDropDownView.getHeight() +
                context.getResources().getDimensionPixelOffset(R.dimen.dp15));
    }

    private void setGoldTv(double remainStamp) {
        SpannableStringBuilder ssb = new SpannableStringBuilder("金锭余额:");
        ssb.append(SpannableStringHelper.changeTextColor(new Money(remainStamp).toSimpleString(), Color.BLACK));
        goldTv.setText(ssb);
    }

    private void setGiftGoldTv(double remainGiftGold) {
        SpannableStringBuilder ssb = new SpannableStringBuilder("礼金余额:");
        ssb.append(SpannableStringHelper.changeTextColor(new Money(remainGiftGold).toSimpleString(), Color.BLACK));
        giftGoldTv.setText(ssb);
    }

    private void setPointTv(double remainPoint) {
        SpannableStringBuilder ssb = new SpannableStringBuilder("香蕉币余额:");
        ssb.append(SpannableStringHelper.changeTextColor(new Money(remainPoint).toSimpleString() + "", Color.BLACK));
        pointTv.setText(ssb);
    }

    public void setGoldNum(double goldCoinAmount, double remainGiftGold, double remainStamp) {
        setGoldTv(goldCoinAmount);
        setGiftGoldTv(remainGiftGold);
        setPointTv(remainStamp);
    }

}
