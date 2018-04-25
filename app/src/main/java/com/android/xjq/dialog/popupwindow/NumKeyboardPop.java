package com.android.xjq.dialog.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TableLayout;

import com.android.xjq.R;

/**
 * 数字键盘
 * <p>
 * Created by lingjiu on 2017/3/28.
 */

public class NumKeyboardPop {

    private int popupHeight;

    private View parentView;

    private View mDropDownView;

    private PopupWindow mPopupWindow;

    private TableLayout tableLayout;

    public interface NumKeyboardSelectListener {
        void numKeyBoardSelectListener(int num);
        void dismissListener();
    }

    public void setNumKeyboardSelectListener(NumKeyboardSelectListener numKeyboardSelectListener) {
        this.numKeyboardSelectListener = numKeyboardSelectListener;
    }

    private NumKeyboardSelectListener numKeyboardSelectListener;

    public NumKeyboardPop(Context context, View dropDownView) {
        this.mDropDownView = dropDownView;

        initView(context);

        setListener();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (numKeyboardSelectListener!=null) {
                numKeyboardSelectListener.numKeyBoardSelectListener(Integer.valueOf((String) v.getTag()));
            }
        }
    };

    private void setListener() {
        parentView.findViewById(R.id.tv1).setOnClickListener(onClickListener);
        parentView.findViewById(R.id.tv2).setOnClickListener(onClickListener);
        parentView.findViewById(R.id.tv3).setOnClickListener(onClickListener);
        parentView.findViewById(R.id.tv4).setOnClickListener(onClickListener);
        parentView.findViewById(R.id.tv5).setOnClickListener(onClickListener);
        parentView.findViewById(R.id.tv6).setOnClickListener(onClickListener);
        parentView.findViewById(R.id.tv7).setOnClickListener(onClickListener);
        parentView.findViewById(R.id.tv8).setOnClickListener(onClickListener);
        parentView.findViewById(R.id.tv9).setOnClickListener(onClickListener);
        parentView.findViewById(R.id.tv0).setOnClickListener(onClickListener);
        parentView.findViewById(R.id.deleteTv).setOnClickListener(onClickListener);
        parentView.findViewById(R.id.confirmTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }

    private void initView(Context context) {
        parentView = View.inflate(context, R.layout.layout_popupwindow_num_keyboard, null);

        setPopConfig();

        mPopupWindow.setContentView(parentView);

        tableLayout = (TableLayout) parentView.findViewById(R.id.tableLayout);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (numKeyboardSelectListener!=null) {
                    numKeyboardSelectListener.dismissListener();
                }
            }
        });
    }


    /**
     * 配置弹出框属性
     */
    private void setPopConfig() {
        mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
        mPopupWindow.setOutsideTouchable(true);// 设置外部触摸会关闭窗口
        //获取自身的长宽高
        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = parentView.getMeasuredHeight();
    }

    public void show() {
        mPopupWindow.showAsDropDown(mDropDownView, 0, -popupHeight - mDropDownView.getHeight() + 15);
    }


}
