package com.android.xjq.utils;

import com.android.library.Utils.LogUtils;

import java.lang.reflect.Field;
import java.util.TimerTask;

/**
 * Created by lingjiu on 2017/6/29.
 * <p>
 * 修改TimerTask的执行时间
 */

public class MyTimerTask extends TimerTask {
    //标志任务是否开始执行，默认不执行
    private boolean flag = false;

    public void setPeriod(long period) {
        //缩短周期，执行频率就提高
        setDeclaredField(TimerTask.class, this, "period", period);
    }

    //通过反射修改字段的值
    static boolean setDeclaredField(Class<?> clazz, Object obj, String name, Object value) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public void run() {
        if (flag) {
            LogUtils.e("", "任务开始执行!!!");
        } else {
            LogUtils.e("", "任务已被暂停!!!");
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}