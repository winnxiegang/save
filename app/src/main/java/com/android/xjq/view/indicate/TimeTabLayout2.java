package com.android.xjq.view.indicate;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.banana.commlib.utils.TimeUtils;
import com.android.xjq.R;

import java.util.List;

import static com.android.banana.commlib.utils.TimeUtils.DATEFORMAT;

/**
 * Created by lingjiu on 2017/10/30.
 */

public class TimeTabLayout2 extends FrameLayout {
    private List<String> mDays;
    private List<String> mValueDays;
    private TabLayout tabLayout;
    private IonCheckTime mListener;
    private int mBackGroundColor;
    private int mTextColor;
    private ColorStateList colorStateList;

    public TimeTabLayout2(@NonNull Context context) {
        this(context,null);

    }

    public TimeTabLayout2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeTabLayout2(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TimeTabLayout2);
        mBackGroundColor = ta.getColor(R.styleable.TimeTabLayout2_tb_backgroundColor, getResources().getColor(R.color.white));
//        mTextColor = ta.getColor(R.styleable.TimeTabLayout2_tb_textColor, getResources().getColorStateList(R.color.color_selector_time_filter));
        colorStateList = ta.getColorStateList(R.styleable.TimeTabLayout2_tb_textColor);
        ta.recycle();

        View childView = LayoutInflater.from(getContext()).inflate(R.layout.tablayout_time, null);
        addView(childView);
        tabLayout = (TabLayout) childView.findViewById(R.id.tabLayout);
        tabLayout.setBackgroundColor(mBackGroundColor);
        this.tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
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
            TextView weekDayTxt = (TextView) view.findViewById(R.id.week_day_txt);
            //weekDayTxt.setTextColor(mTextColor);
            weekDayTxt.setTextColor(colorStateList);
            TextView dateTxt = (TextView) view.findViewById(R.id.date_txt);
            //dateTxt.setTextColor(mTextColor);
            dateTxt.setTextColor(colorStateList);
            TextView todayTxt = (TextView)view.findViewById(R.id.today_txt);
            dateTxt.setText(mDays.get(i));
            view.setTag(mValueDays.get(i));
            //设置默认当天选中
            weekDayTxt.setText("周" + TimeUtils.getDate(mValueDays.get(i), DATEFORMAT));
            if (i == 7) {
                todayTxt.setVisibility(View.VISIBLE);
                this.tabLayout.addTab(this.tabLayout.newTab().setCustomView(view), true);
            } else {
                todayTxt.setVisibility(View.GONE);
                this.tabLayout.addTab(this.tabLayout.newTab().setCustomView(view));
            }
        }
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.week_day_txt)).setSelected(true);
                ((TextView) tab.getCustomView().findViewById(R.id.date_txt)).setSelected(true);
/*                TextView selectedWeekDay = (TextView) tab.getCustomView().findViewById(R.id.week_day_txt);
                selectedWeekDay.setTextColor(getResources().getColor(R.color.orange_red));
                TextView selectedDate = (TextView) tab.getCustomView().findViewById(R.id.date_txt);
                selectedDate.setTextColor(getResources().getColor(R.color.orange_red));*/
                if (mListener != null) {
                    mListener.onCheckTime(tab.getCustomView().getTag().toString());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.week_day_txt)).setSelected(false);
                ((TextView) tab.getCustomView().findViewById(R.id.date_txt)).setSelected(false);
               /* TextView selectedWeekDay = (TextView) tab.getCustomView().findViewById(R.id.week_day_txt);
                selectedWeekDay.setTextColor(getResources().getColor(R.color.white));
                TextView selectedDate = (TextView) tab.getCustomView().findViewById(R.id.date_txt);
                selectedDate.setTextColor(getResources().getColor(R.color.white));*/
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
    }

    public interface IonCheckTime {
        public void onCheckTime(String time);
    }

    public void setIonCheckTimeListener(IonCheckTime listener) {
        mListener = listener;
    }


}
