package com.android.banana.commlib.coupon;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.android.banana.commlib.R;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.library.Utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lingjiu on 2017/7/24.
 */

public class SendCouponTimePop {

    private Context context;

    private int popupHeight;

    private int popupWidth;

    private ListView listView;

    private View parentView;

    private View mDropDownView;

    private SendTimeSelectListener sendTimeSelectListener;

    private PopupWindow mPopupWindow;

    private ArrayList<String> popBeans = new ArrayList<>();

    public interface SendTimeSelectListener {
        void sendTimeSelect(int position, String time);
    }

    public SendCouponTimePop(Context context, View dropDownView, SendTimeSelectListener sendTimeSelectListener) {
        this.mDropDownView = dropDownView;
        this.sendTimeSelectListener = sendTimeSelectListener;
        this.context = context;
        initView(context);
        initData();
    }

    /**
     * 初始化控件
     *
     * @param context
     */
    private void initView(Context context) {
        parentView = View.inflate(context, R.layout.layout_popupwindow_coupon_time_select, null);
        listView = (ListView) parentView.findViewById(R.id.listView);
        setPopConfig();
        mPopupWindow.setContentView(parentView);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //给ListView添加数据
//        GiftNumSelectAdapter popListViewAdapter = new GiftNumSelectAdapter(context, popBeans);

        listView.setAdapter(new ArrayAdapter<>(context, R.layout.item_simple_listview, R.id.tv, popBeans));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (sendTimeSelectListener != null) {
                    sendTimeSelectListener.sendTimeSelect(position, popBeans.get(position));
                }
                mPopupWindow.dismiss();
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

    /**
     * 设置显示在v上方(以v的左边距为开始位置)
     * `
     *
     * @param
     */
    public void showUp(List<Date> dates) {
        wrapData(dates);
        int[] location = new int[2];
        mDropDownView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        LogUtils.e("top", "top=" + x + "y=" + y + "  " + (y - context.getResources().getDimensionPixelOffset(R.dimen.live_video_height)));
        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWidth = parentView.getMeasuredWidth();
        LogUtils.e("top", "popupWidth=" + popupWidth);
        mPopupWindow.setWidth(popupWidth);

        // mPopupWindow.showAtLocation(mDropDownView, Gravity.NO_GRAVITY, x - LibAppUtil.dip2px(context, 15), 0);

        mPopupWindow.showAsDropDown(mDropDownView, -LibAppUtil.dip2px(context, 15), -LibAppUtil.dip2px(context, 165));
    }

    private void wrapData(List<Date> dates) {
        ArrayList<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH时mm分");
        if (dates != null && dates.size() > 0) {
            for (Date date : dates) {
                list.add(sdf.format(date));
            }
        }
        popBeans.clear();
        popBeans.addAll(list);
    }
}
