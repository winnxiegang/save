package com.android.xjq.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import com.android.banana.commlib.utils.TimeUtils;
import com.android.jjx.sdk.listener.OnMyClickListener;
import com.android.xjq.bean.userInfo.DateModel;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Locale;

public class TimeAlertDialogUtil {


    public TimeAlertDialogUtil(Context context, String tip, final OnMyClickListener myClickListener,
                               boolean showCancel, final String tag, String initValue) {

       if(("结束日期").equals(initValue)||
               "开始日期".equals(initValue)){
           initValue = null;
       }

		Calendar cal;
		if (StringUtils.isBlank(initValue)) {
			cal = Calendar.getInstance(Locale.CHINA);
		} else {

			// datePicker.set
			cal = TimeUtils.stringToCalendar(initValue, TimeUtils.DATEFORMAT);

		}



        DatePickerDialog datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                StringBuilder sb = new StringBuilder();

                sb.append(year);

                sb.append("-");

                sb.append(numAddFrontZero(monthOfYear+1));

                sb.append("-");

                sb.append(numAddFrontZero(dayOfMonth));

                DateModel dateModel = new DateModel(tag, sb.toString());

                view.setTag(dateModel);

                myClickListener.onClick(view);
            }
        },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();


	}

    /**
     * 如果数字小于10，则增加一个0
     *
     * @param i
     * @return
     */
    public static String numAddFrontZero(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return Integer.toString(i);
        }
    }
}
