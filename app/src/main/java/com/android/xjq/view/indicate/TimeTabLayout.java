package com.android.xjq.view.indicate;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.banana.commlib.utils.TimeUtils;
import com.android.jjx.sdk.utils.LogUtils;
import com.android.xjq.R;

import java.util.List;

import static com.android.banana.commlib.utils.TimeUtils.DATEFORMAT;

/**
 * Created by lingjiu on 2017/10/30.
 */

public class TimeTabLayout extends FrameLayout {
    private List<String> mDays;
    private List<String> mValueDays;
    private TabLayout tabLayout;
    private IonCheckTime mListener;

    public TimeTabLayout(@NonNull Context context) {
        super(context);
    }

    public TimeTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("onAttachedToWindow: ", "onAttachedToWindow");
        View childView = LayoutInflater.from(getContext()).inflate(R.layout.tablayout_time, null);
        addView(childView);
        tabLayout = (TabLayout) childView.findViewById(R.id.tabLayout);
        this.tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    public void recomputeTlOffset1(int index) {
        if (tabLayout.getTabCount() <= index)
            return;

        if (tabLayout.getTabAt(index) != null)
            tabLayout.getTabAt(index).select();
        final int width = (int) (getTablayoutOffsetWidth(index) * getResources().getDisplayMetrics().density);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.smoothScrollTo(width, 0);
            }
        });
    }


//重中之重是这个计算偏移量的方法，各位看官看好了。

    /**
     * 根据字符个数计算偏移量
     *
     * @param index 选中tab的下标
     * @return 需要移动的长度
     */
    private int getTablayoutOffsetWidth(int index) {
        String str = "";
        for (int i = 0; i < index; i++) {
            //channelNameList是一个List<String>的对象，里面转载的是30个词条
            //取出直到index的tab的文字，计算长度
            str += mValueDays.get(i);
        }
        return str.length() * 14 + index * 12;
    }

    private void setView(String nowDate) {
        initData(nowDate);
        for (int i = 0; i < mDays.size(); i++) {
            View view = View.inflate(getContext(), R.layout.item_time_tablayout2, null);
            TextView text1 = (TextView) view.findViewById(R.id.text1);
            TextView dateTxt = (TextView) view.findViewById(R.id.date_txt);
            dateTxt.setText(mDays.get(i));
            view.setTag(mValueDays.get(i));
            //设置默认当天选中
            text1.setText("周" + TimeUtils.getDate(mValueDays.get(i), DATEFORMAT));
            if (i == 7) {
                dateTxt.setText("今天");
                this.tabLayout.addTab(this.tabLayout.newTab().setCustomView(view), true);
            } else {
                this.tabLayout.addTab(this.tabLayout.newTab().setCustomView(view));
            }
        }
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.date_txt)).setSelected(true);
                if (mListener != null) {
                    mListener.onCheckTime(tab.getCustomView().getTag().toString());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.date_txt)).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        recomputeTlOffset1(7);
    }

  /*  public void setChecked (int pos) {
        View v = this.tabLayout.getTabAt(pos).getCustomView();
        TextView txt = (TextView) v.findViewById(R.id.date_txt);
        txt.setSelected(true);
    }*/

    public void setNowData(String nowDate) {
        if (mDays == null || mDays.size() == 0) {
            setView(nowDate);
        }
    }

    private void initData(String nowDate) {
        //后续可能从服务端获取
        mDays = TimeUtils.getTimeList(nowDate, TimeUtils.FORMAT_M_D, 7, 3);
        mValueDays = TimeUtils.getTimeList(nowDate, TimeUtils.DATEFORMAT, 7, 3);
        LogUtils.i("yjj", "前三天后七天" + mDays.toString());
    }

    public interface IonCheckTime {
        public void onCheckTime(String time);
    }

    public void setIonCheckTimeListener(IonCheckTime listener) {
        mListener = listener;
    }

}
