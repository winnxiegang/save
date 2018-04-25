package com.jzxiang.pickerview;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jzxiang.pickerview.config.PickerConfig;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.data.WheelCalendar;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.util.Calendar;

/**
 * Created by lingjiu on 2017/1/12 11:29.
 */
public class MyTimePickerDialog extends Dialog implements View.OnClickListener {

    PickerConfig mPickerConfig;
    private TimeWheel mTimeWheel;
    private long mCurrentMillSeconds;
    private OnMyDateSetListener listener;
    private OnCalerndarSetListener calendarListener;

    public MyTimePickerDialog(Context context, PickerConfig pickerConfig, OnMyDateSetListener listener) {

        super(context, R.style.Dialog_NoTitle);
        this.listener = listener;
        mPickerConfig = pickerConfig;
        initialize(context, mPickerConfig);
    }

    public MyTimePickerDialog(Context context, PickerConfig pickerConfig, OnCalerndarSetListener calendarListener, OnMyDateSetListener listener) {
        super(context, R.style.Dialog_NoTitle);
        this.listener = listener;
        this.calendarListener = calendarListener;
        mPickerConfig = pickerConfig;
        initialize(context, mPickerConfig);
    }


    private void initialize(Context context, PickerConfig pickerConfig) {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(initView());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        lp.width = getScreenWidth(context);
        dialogWindow.setAttributes(lp);


    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();//屏幕宽度
        return screenWidth;
    }


    View initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.timepicker_layout, null);
        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(this);
        TextView sure = (TextView) view.findViewById(R.id.tv_sure);
        sure.setOnClickListener(this);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        View toolbar = view.findViewById(R.id.toolbar);

        title.setText(mPickerConfig.mTitleString);
        cancel.setText(mPickerConfig.mCancelString);
        sure.setText(mPickerConfig.mSureString);
        toolbar.setBackgroundColor(mPickerConfig.mThemeColor);

        mTimeWheel = new TimeWheel(view, mPickerConfig);
        return view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            dismiss();
        } else if (i == R.id.tv_sure) {
            sureClicked();
        }
    }

    /*
    * @desc This method returns the current milliseconds. If current milliseconds is not set,
    *       this will return the system milliseconds.
    * @param none
    * @return long - the current milliseconds.
    */
    public long getCurrentMillSeconds() {
        if (mCurrentMillSeconds == 0)
            return System.currentTimeMillis();

        return mCurrentMillSeconds;
    }

    /*
    * @desc This method is called when onClick method is invoked by sure button. A Calendar instance is created and
    *       initialized.
    * @param none
    * @return none
    */
    void sureClicked() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        calendar.set(Calendar.YEAR, mTimeWheel.getCurrentYear());
        calendar.set(Calendar.MONTH, mTimeWheel.getCurrentMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, mTimeWheel.getCurrentDay());
        calendar.set(Calendar.HOUR_OF_DAY, mTimeWheel.getCurrentHour());
        calendar.set(Calendar.MINUTE, mTimeWheel.getCurrentMinute());
        mCurrentMillSeconds = calendar.getTimeInMillis();

        if (calendarListener != null) {
            calendarListener.onDateSet(mTimeWheel.getCurrentYear(),mTimeWheel.getCurrentMonth() - 1,mTimeWheel.getCurrentDay(), mTimeWheel.getCurrentHour(),mTimeWheel.getCurrentMinute());
        }

        if (listener != null) {
            listener.onDateSet(this, mCurrentMillSeconds);
        }
        dismiss();
    }


    public static class Builder {
        PickerConfig mPickerConfig;
        OnMyDateSetListener mListener;
        OnCalerndarSetListener mCalendarListener;
        Calendar c = Calendar.getInstance();

        public Builder() {
            mPickerConfig = new PickerConfig();
        }

        public Builder setType(Type type) {
            mPickerConfig.mType = type;
            return this;
        }

        public Builder setThemeColor(int color) {
            mPickerConfig.mThemeColor = color;
            return this;
        }

        public Builder setCancelStringId(String left) {
            mPickerConfig.mCancelString = left;
            return this;
        }

        public Builder setSureStringId(String right) {
            mPickerConfig.mSureString = right;
            return this;
        }

        public Builder setTitleStringId(String title) {
            mPickerConfig.mTitleString = title;
            return this;
        }

        public Builder setToolBarTextColor(int color) {
            mPickerConfig.mToolBarTVColor = color;
            return this;
        }

        public Builder setWheelItemTextNormalColor(int color) {
            mPickerConfig.mWheelTVNormalColor = color;
            return this;
        }

        public Builder setWheelItemTextSelectorColor(int color) {
            mPickerConfig.mWheelTVSelectorColor = color;
            return this;
        }

        public Builder setWheelItemTextSize(int size) {
            mPickerConfig.mWheelTVSize = size;
            return this;
        }

        public Builder setCyclic(boolean cyclic) {
            mPickerConfig.cyclic = cyclic;
            return this;
        }

        public Builder setMinMillseconds(int year) {
            c.set(year, 0, 1, 0, 0);
            mPickerConfig.mMinCalendar = new WheelCalendar(c.getTimeInMillis());
            return this;
        }

        public Builder setMaxMillseconds(int year) {
            c.set(year, 11, 31, 0, 0);
            mPickerConfig.mMaxCalendar = new WheelCalendar(c.getTimeInMillis());
            return this;
        }

        public Builder setCurrentMillseconds(long millseconds) {
            mPickerConfig.mCurrentCalendar = new WheelCalendar(millseconds);
            return this;
        }

        public Builder setYearText(String year) {
            mPickerConfig.mYear = year;
            return this;
        }

        public Builder setMonthText(String month) {
            mPickerConfig.mMonth = month;
            return this;
        }

        public Builder setDayText(String day) {
            mPickerConfig.mDay = day;
            return this;
        }

        public Builder setHourText(String hour) {
            mPickerConfig.mHour = hour;
            return this;
        }

        public Builder setMinuteText(String minute) {
            mPickerConfig.mMinute = minute;
            return this;
        }

        public Builder setCallBack(OnDateSetListener listener) {
            mPickerConfig.mCallBack = listener;
            return this;
        }

        public Builder setDateSetListener(OnMyDateSetListener listener) {
            mListener = listener;
            return this;
        }

        public Builder setCalendarSetListener(OnCalerndarSetListener listener) {
            mCalendarListener = listener;
            return this;
        }

        public MyTimePickerDialog build(Context context) {
            return new MyTimePickerDialog(context, mPickerConfig, mCalendarListener, mListener);
        }

        public Builder startFromZero() {
            mPickerConfig.mStartFromZero = true;
            return this;
        }
    }

}
