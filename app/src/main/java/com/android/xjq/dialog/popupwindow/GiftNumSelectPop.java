package com.android.xjq.dialog.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.android.xjq.R;
import com.android.xjq.adapter.main.GiftNumSelectAdapter;
import com.android.xjq.bean.live.main.gift.GiftCountInfo;
import com.android.library.Utils.LibAppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2017/3/8.
 */

public class GiftNumSelectPop {

    private Context context;

    private int popupHeight;

    private ListView listView;

    private View parentView;

    private View mDropDownView;

    private List<GiftCountInfo> popBeans = new ArrayList<>();

    private GiftNumSelectListener giftNumSelectListener;

    /**
     * 当前选择的礼物类型(礼物/包裹)
     */
    private int currentGiftType;

    private PopupWindow mPopupWindow;

    private List<GiftCountInfo> packageInfoList;

    private List<GiftCountInfo> giftInfoList;

    private NumKeyboardPop numKeyBoardPop;

    public interface GiftNumSelectListener {
        void giftNumSelect(int position, GiftCountInfo countInfo, boolean isAll);
    }

    public GiftNumSelectPop(Context context, View dropDownView, GiftNumSelectListener giftNumSelectListener) {
        this.mDropDownView = dropDownView;
        this.giftNumSelectListener = giftNumSelectListener;
        this.context = context;
        initView(context);
        initData();
    }

    public void setNumKeyBoardListener(NumKeyboardPop.NumKeyboardSelectListener numKeyBoardListener) {
        if (numKeyBoardPop == null) return;

        numKeyBoardPop.setNumKeyboardSelectListener(numKeyBoardListener);

    }


    /**
     * 初始化控件
     *
     * @param context
     */
    private void initView(Context context) {

        numKeyBoardPop = new NumKeyboardPop(context, mDropDownView);

        parentView = View.inflate(context, R.layout.layout_popupwindow_gift_num_select, null);

        listView = (ListView) parentView.findViewById(R.id.listView);

        setPopConfig();

        mPopupWindow.setContentView(parentView);
    }

    /**
     * 初始化数据
     */
    private void initData() {
//        popBeans.addAll(giftInfoList);

        //给ListView添加数据
        GiftNumSelectAdapter popListViewAdapter = new GiftNumSelectAdapter(context, popBeans);

        listView.setAdapter(popListViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*if (popBeans.get(position).getNum() == 0) {
                    NumKeyboardPop numKeyboardPop = new NumKeyboardPop(context, mDropDownView);
                    numKeyboardPop.show();
                }*/
               /* for (GiftCountInfo popBean : popBeans) {
                    popBean.setSelected(false);
                }
                popBeans.get(position).setSelected(true);*/
                if (position == popBeans.size()) {
                    numKeyBoardPop.show();
                } else {
                    if (giftNumSelectListener != null) {
                        giftNumSelectListener.giftNumSelect(position, popBeans.get(position), popBeans.get(position).isAll());
                    }
                }

                mPopupWindow.dismiss();
            }
        });
    }

    /**
     * 配置弹出框属性
     */
    private void setPopConfig() {
        mPopupWindow = new PopupWindow(mDropDownView.getWidth() + 25, ViewGroup.LayoutParams.WRAP_CONTENT);
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
     * @param giftCountInfoList
     * @param giftCountInfo
     */
    public void showUp(int currentGiftType, List<GiftCountInfo> giftCountInfoList, GiftCountInfo giftCountInfo) {

        this.currentGiftType = currentGiftType;
        popBeans.clear();
        for (GiftCountInfo countInfo : giftCountInfoList) {
            if (countInfo.equals(giftCountInfo)) {
                countInfo.setSelected(true);
            } else {
                countInfo.setSelected(false);
            }
        }
        popBeans.addAll(giftCountInfoList);

        initData();

        ViewGroup parent = (ViewGroup) listView.getParent();

        ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();

        if (giftCountInfoList != null && giftCountInfoList.size() > 0) {

            layoutParams.height = LibAppUtil.dip2px(context, 55 + giftCountInfoList.size() * 40);
        }

        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = parentView.getMeasuredHeight();

        mPopupWindow.showAsDropDown(mDropDownView, -12, -popupHeight - mDropDownView.getHeight() + 22);
    }

}