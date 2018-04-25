package com.tencent.ilivesdk.core;

import android.util.Log;

import com.tencent.ilivesdk.core.impl.ILVBLog;

/**
 * 日志输出
 */
public class ILiveLog {
    public enum TILVBLogLevel {
        OFF,
        ERROR,
        WARN,
        DEBUG,
        INFO
    }

    static private TILVBLogLevel level = TILVBLogLevel.INFO;

    public final static boolean DEBUG = true;

    static public String[] getStringValues() {
        TILVBLogLevel[] levels = TILVBLogLevel.values();
        String[] stringValuse = new String[levels.length];
        for (int i = 0; i < levels.length; i++) {
            stringValuse[i] = levels[i].toString();
        }
        return stringValuse;
    }

    /**
     * 设置写文件的日志级别
     *
     * @param newLevel
     */
    static public void setLogLevel(TILVBLogLevel newLevel) {
        level = newLevel;
        w("Log", "change log level: " + newLevel);
    }

    /**
     * 打印INFO级别日志
     *
     * @param strTag  TAG
     * @param strInfo 消息
     */
    public static void v(String strTag, String strInfo) {
        if (!DEBUG) return;
        Log.v(strTag, strInfo);
        if (level.ordinal() >= TILVBLogLevel.INFO.ordinal()) {
            ILVBLog.writeLog("V", strTag, strInfo, null);
        }
    }

    /**
     * 打印INFO级别日志
     *
     * @param strTag  TAG
     * @param strInfo 消息
     */
    public static void i(String strTag, String strInfo) {
        if (!DEBUG) return;
        Log.i(strTag, strInfo);
        if (level.ordinal() >= TILVBLogLevel.INFO.ordinal()) {
            ILVBLog.writeLog("I", strTag, strInfo, null);
        }
    }

    /**
     * 打印DEBUG级别日志
     *
     * @param strTag  TAG
     * @param strInfo 消息
     */
    public static void d(String strTag, String strInfo) {
        if (!DEBUG) return;
        Log.d(strTag, strInfo);
        if (level.ordinal() >= TILVBLogLevel.DEBUG.ordinal()) {
            ILVBLog.writeLog("D", strTag, strInfo, null);
        }
    }

    /**
     * 打印WARN级别日志
     *
     * @param strTag  TAG
     * @param strInfo 消息
     */
    public static void w(String strTag, String strInfo) {
        if (!DEBUG) return;
        Log.w(strTag, strInfo);
        if (level.ordinal() >= TILVBLogLevel.WARN.ordinal()) {
            ILVBLog.writeLog("W", strTag, strInfo, null);
        }
    }

    /**
     * 打印ERROR级别日志
     *
     * @param strTag  TAG
     * @param strInfo 消息
     */
    public static void e(String strTag, String strInfo) {
        if (!DEBUG) return;
        Log.e(strTag, strInfo);
        if (level.ordinal() >= TILVBLogLevel.ERROR.ordinal()) {
            ILVBLog.writeLog("E", strTag, strInfo, null);
        }
    }

    /**
     * 打印异常堆栈信息
     *
     * @param strTag
     * @param strInfo
     * @param tr
     */
    public static void writeException(String strTag, String strInfo, Exception tr) {
        ILVBLog.writeLog("C", strTag, strInfo, tr);
    }
}
