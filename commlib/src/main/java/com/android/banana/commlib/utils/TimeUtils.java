package com.android.banana.commlib.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by lingjiu on 2017/3/29.
 */

public class TimeUtils {
    /**
     * 长时间格式
     */
    public static String LONG_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String FORMAT_Y_M_D = "yyyy/MM/dd";

    public static String FORMAT_M_D = "MM/dd";

    public static String DATEFORMAT = "yyyy-MM-dd";

    public static String TIMEFORMAT = "HH:mm";

    public static String MATCH_FORMAT = "MM-dd HH:mm";

    public static String COLLECT_FORMAT = "MM月dd日 HH:mm";

    public static String PROGRAM_FORMAT = "MM月dd日";
    public static String CHANNEL_FORMAT = "MM-dd";

    public static String NIAN_YUE_RI = "yyyy-MM-dd";
    public static String SHI_FEN_MIAO = "HH:mm:ss";

    public static String timeStrFormat(String time) {

        String str = time.substring(0, 4).concat("-")
                .concat(time.substring(4, 6)).concat("-")
                .concat(time.substring(6));

        return str;

    }

    public static String StrToYMD(String time) {

        String str = time.substring(0, 10);

        return str;

    }

    public static String getFormatTime(Date date, String format) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String timeStrFormatSplitDay(String time) {

        String str = time.substring(0, 4).concat("-")
                .concat(time.substring(4, 6));

        return str;
    }

    public static int differMinutes(String now, String gmtEnd) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int differMinutes = 0;
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(gmtEnd);
            long differ = d1.getTime() - d2.getTime();
            long min = (differ) / (1000 * 60);
            differMinutes = (int) min;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return differMinutes;
    }


    /**
     * @param time 传入年份与月份
     * @return 得到一个该月的最后一天
     */
    public static ArrayList<String> getFristLastDay(String time) {
        ArrayList<String> list = new ArrayList<>();
        list.add(timeStrFormatSplitDay(time).concat("-").concat("01"));
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.valueOf(time.substring(0, 4)));
        c.set(Calendar.MONTH, Integer.valueOf(time.substring(4, 6)) - 1);//1月是0,从0开始计算
        list.add(timeStrFormatSplitDay(time).concat("-").concat(String.valueOf(c.getActualMaximum(Calendar.DAY_OF_MONTH))));
        return list;
    }

    public static boolean isRangeContainTime(List<Integer> timePointList, int keepMinutes) {
        if (timePointList == null && timePointList.size() == 0) return false;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date(System.currentTimeMillis());
        for (Integer integer : timePointList) {
            //keepMinutes分钟后的时间
            Calendar c = Calendar.getInstance();
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), integer, 0, 0);
            Date lastTime = c.getTime();
            c.add(Calendar.MINUTE, keepMinutes);
            Date firstTime = c.getTime();
           /* //keepMinutes分钟前的时间
            c.add(Calendar.MINUTE, -2 * keepMinutes);
            Date lastTime = c.getTime();*/
//            Log.e("TimeUtils", sdf.format(firstTime) + " " + sdf.format(lastTime));
            if (currentTime.before(firstTime) && currentTime.after(lastTime)) {
                return true;
            }
        }
        return false;
    }


    public static long timeStrToLong(String time) {
        Date date = stringToDate(time, LONG_DATEFORMAT);
        if (date == null) {
            return System.currentTimeMillis();
        }
        return date.getTime();
    }


    /**
     * 群聊消息列表时间规则
     */
    public static String formatMessageTime(String nowStr, String createStr) {

        if (TextUtils.isEmpty(nowStr) || TextUtils.isEmpty(createStr)) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat();

        sdf.applyPattern(LONG_DATEFORMAT);

        Date nowDate = sdf.parse(nowStr, new ParsePosition(0));

        Date createDate = sdf.parse(createStr, new ParsePosition(0));

        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(nowDate);

        Calendar createCal = Calendar.getInstance();
        createCal.setTime(createDate);

        int nowYear = nowCal.get(Calendar.YEAR);
        int nowMonth = nowCal.get(Calendar.MONTH) + 1;
        int nowDay = nowCal.get(Calendar.DAY_OF_MONTH);

        int createYear = createCal.get(Calendar.YEAR);
        int createMonth = createCal.get(Calendar.MONTH) + 1;
        int createDay = createCal.get(Calendar.DAY_OF_MONTH);

        if (nowYear == createYear && nowMonth == createMonth && nowDay == createDay) {
            return LibAppUtil.numAddFrontZero(createCal.get(Calendar.HOUR_OF_DAY)) + ":" + LibAppUtil.numAddFrontZero(createCal.get(Calendar.MINUTE));
        }


        if (nowYear == createYear && nowMonth == createMonth && nowDay == (createDay + 1)) {
            return "昨天";
        }

        if (nowYear == createYear) {

            if (nowMonth == createMonth && nowDay <= createDay + 6) {

                return "星期" + getDate(createStr, LONG_DATEFORMAT);

            } else if (nowMonth <= createMonth + 1) {

                nowCal.add(Calendar.MONTH, -1);// 6.30   7.5  30

                int maximum = nowCal.getActualMaximum(Calendar.DAY_OF_MONTH);

                if (nowDay + maximum <= createDay + 6) {

                    return "星期" + getDate(createStr, LONG_DATEFORMAT);
                } else {
                    return LibAppUtil.numAddFrontZero(createMonth) + "-" + LibAppUtil.numAddFrontZero(createDay);
                }
            } else {
                return LibAppUtil.numAddFrontZero(createMonth) + "-" + LibAppUtil.numAddFrontZero(createDay);
            }
        }


        return createYear + "-" + LibAppUtil.numAddFrontZero(createMonth) + "-" + LibAppUtil.numAddFrontZero(createDay);
    }

    public static String getDate(String str, String format) {
        Date date;
        try {
            date = new SimpleDateFormat(format).parse(str);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            switch ((cal.get(Calendar.DAY_OF_WEEK) - 1)) {
                case -1:
                    return "日";
                case 1:
                    return "一";
                case 2:
                    return "二";
                case 3:
                    return "三";
                case 4:
                    return "四";
                case 5:
                    return "五";
                case 6:
                    return "六";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "日";
    }

    public static long date1SubDate2MS(String now, String gmtEnd) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(gmtEnd);
            long dif = d1.getTime() - d2.getTime();
            return dif;
        } catch (Exception e) {

        }
        return 0;
    }

    /**
     * 适用于服务器给的禁言时间距离当前时间  几天 几小时 几分
     * <p>
     * 如果剩余时间不足一天但大于1小时，则显示小时 分
     * 如果剩余时间不足1小时 则只显示分钟即可
     * 当剩余时间为0 也即gmtExpired 小于等于当前时间时，解禁。
     *
     * @param gmtExpired
     * @return
     */
    public static String getForbiddenTime(String gmtExpired) {
        long forbiddenTimeMills = getTimeUnix(gmtExpired);
        long nowTimeMills = Calendar.getInstance().getTimeInMillis();
        if (forbiddenTimeMills <= nowTimeMills)
            return "0";
        long diff = (forbiddenTimeMills - nowTimeMills) / 1000;
        int day = (int) (diff / 24 / 3600);
        int hour = (int) ((diff - day * 24 * 3600) / 3600);
        int min = (int) (diff - (day * 24 * 3600) - (hour * 3600)) / 60;
        String h = hour > 9 ? +hour + "" : ("0" + hour);
        if (day > 0 && hour > 0)
            return day + "天" + h + "小时" + min + "分";
        if (day > 0)
            return day + "天" + min + "分";
        if (hour > 0)
            return h + "小时" + min + "分";
        else if (min > 0)
            return min + "分";
        else if (min <= 0 && diff > 0)
            return "1分";
        else return "0";
    }

    /**
     * 将当前时间减去开始时间返回的毫秒数
     *
     * @param dateStr
     * @return
     */
    public static long getTimeUnix(String dateStr) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return df.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getDiffSecTwoDateStr(String now, String gmtEnd) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (TextUtils.isEmpty(now) || TextUtils.isEmpty(gmtEnd))
            return 0;
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(gmtEnd);
            long diff = d1.getTime() - d2.getTime();
            return diff / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 1.先判断两个消息创建时间 是否属于同一天，如果是同一天且时间戳相差1分钟------>返回格式：HH:mm（10:20）
     * 2.如果不属于同一天且时间戳相差小于24*3600*1000 毫秒,返回格式：昨天 HH:mm (昨天10:20)
     * 3.如果时间戳相差大于1天，且小于7天24*3600*1000*7 毫秒，返回格式:周一 HH:mm  \周二 HH:mm  \周三 HH:mm (周三10:20)
     * 4.如果时间戳相差大于7天且小于一年24*3600*1000*当年总天数 毫秒 返回 MM-dd HH：mm(04-10 10:20)
     * 5.如果时间戳相差大于一年 返回 yyyy-MM-dd HH：mm(2017-04-10 10:20)
     *
     * @param timeCreated 消息创建时间
     * @return
     */

    public static String getTimeLine(String timeCreated) {
        if (TextUtils.isEmpty(timeCreated))
            return "";
        Calendar curInstance = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        try {
            SimpleDateFormat format = new SimpleDateFormat(LONG_DATEFORMAT);
            Date date = format.parse(timeCreated);
            calendar.setTime(date);

            int year = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH) + 1;
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);


            String minStr = (min > 9 ? min + "" : "0" + min);
            String hourStr = (h > 9 ? h + "" : "0" + h);
            String monthStr = (m > 9 ? m + "" : "0" + m);
            String dayStr = (d > 9 ? d + "" : "0" + d);

            int yearNow = curInstance.get(Calendar.YEAR);
            int mNow = curInstance.get(Calendar.MONTH) + 1;
            int dNow = curInstance.get(Calendar.DAY_OF_MONTH);

            if (year == yearNow && m == mNow && dNow == d) {
                return hourStr + ":" + minStr;
            }

            if (year == yearNow && m == mNow && mNow == m && (d + 1) == dNow) {//1  8
                return "昨天 " + hourStr + ":" + minStr;

            } else if (year == yearNow) {//&& mNow == m && (dNow + 6) >= d
                if (mNow == m && dNow <= d + 6) {
                    String week = "星期" + getDate(timeCreated, LONG_DATEFORMAT);
                    return week + hourStr + ":" + minStr;
                } else if (mNow <= m + 1) {
                    curInstance.add(Calendar.MONTH, -1);// 6.30   7.5  30
                    int maximum = curInstance.getActualMaximum(Calendar.DAY_OF_MONTH);
                    if (dNow + maximum <= d + 6) {
                        String week = "星期" + getDate(timeCreated, LONG_DATEFORMAT);
                        return week + hourStr + ":" + minStr;
                    } else {
                        return monthStr
                                + "-"
                                + dayStr
                                + " "
                                + hourStr + ":" + minStr;
                    }
                } else {
                    return monthStr
                            + "-"
                            + dayStr
                            + " "
                            + hourStr + ":" + minStr;
                }

            } else
                return year
                        + "-"
                        + monthStr
                        + "-"
                        + dayStr
                        + " "
                        + hourStr + ":" + minStr;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @NonNull
    public static String getMonthDayStr(String formatStr) {// "HH"
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat(LONG_DATEFORMAT);
            Date date = format.parse(formatStr);
            calendar.setTime(date);

            int year = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH) + 1;
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);

            return m + "月" + d + "日";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMonthDayStr2(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATEFORMAT);
            long time = sdf.parse(date_str).getTime();
            SimpleDateFormat sdf2 = new SimpleDateFormat(FORMAT_M_D);
            return sdf2.format(new Date(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getHourMinute(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATEFORMAT);
            long time = sdf.parse(date_str).getTime();
            SimpleDateFormat sdf2 = new SimpleDateFormat(TIMEFORMAT);
            return sdf2.format(new Date(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getYueRuiStr(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATEFORMAT);
            long time = sdf.parse(date_str).getTime();
            SimpleDateFormat sdf2 = new SimpleDateFormat(PROGRAM_FORMAT);
            return sdf2.format(new Date(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getYueRuiStr2(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATEFORMAT);
            long time = sdf.parse(date_str).getTime();
            SimpleDateFormat sdf2 = new SimpleDateFormat(CHANNEL_FORMAT);
            String todayFormat = sdf2.format(new Date(System.currentTimeMillis()));
            String dateFormat = sdf2.format(new Date(time));
            dateFormat = dateFormat.equals(todayFormat) ? "今天" : dateFormat;
            return dateFormat;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getNianYueRuiStr(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATEFORMAT);
            long time = sdf.parse(date_str).getTime();
            SimpleDateFormat sdf2 = new SimpleDateFormat(NIAN_YUE_RI);
            return sdf2.format(new Date(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getShiFenMiaoStr(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATEFORMAT);
            long time = sdf.parse(date_str).getTime();
            SimpleDateFormat sdf2 = new SimpleDateFormat(SHI_FEN_MIAO);
            return sdf2.format(new Date(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String dateToString2(Date date, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    /**
     * 获取当前日期前多少天后多少天
     */
    public static ArrayList<String> getTimeList(String currentDate, String formatStr, int pre, int next) {
        ArrayList<String> list = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr); //"yyyy-MM-dd"
            //Date dt = sdf.parse(currentDate, new ParsePosition(0));
            SimpleDateFormat formatter = new SimpleDateFormat(LONG_DATEFORMAT);
            Date dt = formatter.parse(currentDate);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            for (int i = 0; i < pre; i++) {
                rightNow.add(Calendar.DAY_OF_MONTH, -1);// 你要减的日期
                Date dt1 = rightNow.getTime();
                String reStr = sdf.format(dt1);
                list.add(0, reStr);
            }

            list.add(sdf.format(dt));
            rightNow.setTime(dt);
            for (int i = 0; i < next; i++) {
                rightNow.add(Calendar.DAY_OF_MONTH, 1);// 你要加的日期
                Date dt1 = rightNow.getTime();
                String reStr = sdf.format(dt1);
                list.add(reStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据格式把字符转成时间
     *
     * @param strTime
     * @param format
     * @return
     */

    public static Date stringToDate(String strTime, String format) {
        if (strTime == null || "".equals(strTime.trim())) {

            return null;
        }

        if (format == null || "".equals(format.trim())) {
            format = LONG_DATEFORMAT;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);

        Date date = null;

        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {

        }

        return date;
    }

    /**
     * @param strTime
     * @param format
     * @return
     */
    public static Calendar stringToCalendar(String strTime, String format) {

        Date date = stringToDate(strTime, format);

        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);

        return calendar;

    }

    public static boolean isAfterNow(Date now, String time, String format) {

        if (now == null) {
            return true;
        }
        Date date2 = stringToDate(time, format);

        if (date2 == null) {
            return true;
        }

        return date2.after(now);
    }

    public static boolean isAfterNow(Date nowDate, Date time) {

       /* Date date1 = stringToDate(nowDate, LONG_DATEFORMAT);

        Date date2 = stringToDate(time, LONG_DATEFORMAT);*/

        if (nowDate == null || time == null) {
            return true;
        }

        return time.after(nowDate);
    }

    public static boolean isAfterNowNoTime(String nowDate, String time) {

        if (nowDate == null || time == null) {
            return true;
        }

        Date date1 = stringToDate(nowDate, DATEFORMAT);

        Date date2 = stringToDate(time, DATEFORMAT);

        if (date2 == null || date1 == null) {
            return true;
        }

        return date2.after(date1);
    }

    public static boolean isBeforeNow(String nowDate, String time) {

        if (nowDate == null || time == null) {
            return true;
        }

        Date date1 = stringToDate(nowDate, LONG_DATEFORMAT);

        Date date2 = stringToDate(time, LONG_DATEFORMAT);

        if (date2 == null || date1 == null) {
            return true;
        }

        return date2.before(date1);
    }


    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        return sdf.format(date);
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String date1SubDate2(String now, String gmtEnd) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(gmtEnd);
            long diff = d2.getTime() - d1.getTime();// 这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);

            long hours = (diff - days * (1000 * 60 * 60 * 24))
                    / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60))
                    / (1000 * 60);
            long second = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60) - minutes * (1000 * 60 * 60))
                    / (1000);
            return "" + days + "天" + hours + "小时" + minutes + "分";

        } catch (Exception e) {

        }
        return null;
    }

    public static String future5MatchesBetweenTime(String startTime, String nowDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(startTime);
            Date d2 = df.parse(nowDate);
            long diff = d2.getTime() - d1.getTime();
            long betweenHour = diff / 1000 / 3600;
            long day = betweenHour / 24;
            long leftDay = betweenHour % 24;
            if (leftDay >= 12) {
                return String.valueOf(day + 1);
            } else {
                return String.valueOf(day);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long parseTime(String time, String format) {
        if (time == null) return 0;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        try {
            date = sdf.parse(time);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String date1SubDate2(Date d1, Date d2) {
        try {
            double diff = d2.getTime() - d1.getTime();// 这样得到的差值是微秒级别
            double days = diff / (86400000);
            double formatDate = Math.ceil(days);
            if (formatDate < 0) {
                return Integer.toString(0);
            } else {
                return Integer.toString((int) formatDate);
            }

        } catch (Exception e) {

        }
        return null;
    }

    public static int date1SubDate2M(String now, String gmtEnd) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(gmtEnd);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);

            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60))
                    / (1000 * 60);
            return Integer.valueOf(String.valueOf(minutes));

        } catch (Exception e) {

        }
        return 0;
    }

    /**
     * 将当前时间减去开始时间返回的毫秒数
     *
     * @param now
     * @param start
     * @return
     */
    public static long date1SubDate2Ms(String now, String start) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(start);
            long diff = Math.abs(d1.getTime() - d2.getTime());// 这样得到的差值是微秒级别
            return diff;

        } catch (Exception e) {

        }
        return 0;
    }

    public static long date1SubDate2MsWithNoAbs(String now, String start) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(start);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            return diff;

        } catch (Exception e) {

        }
        return 0;
    }

    /**
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compareDate(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static long dateSubDate(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            LogUtils.e("prizeInfoQuery", "dt1=" + dt1.getTime() + " dt2=" + dt2.getTime());
            return dt1.getTime() - dt2.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 两个日期之间的差值
     */
    public final static int daysBetween(Date now, Date date) {

        long between_days = (date.getTime() - now.getTime())
                / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 两个日期之间的差值
     */
    public final static int daysBetween(String now, String date) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = stringToDate(now, null);
        Date d2 = stringToDate(date, null);
        long between_days = (d1.getTime() - d2.getTime()) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static String formatStringDate(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATEFORMAT);
        try {
            Date tempDate = sdf.parse(date);
            sdf.applyPattern(pattern);
            return sdf.format(tempDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取当前日期前多少天后多少天
     */
    public static ArrayList<String> getTimeList(String currentDate, int pre,
                                                int next) {
        ArrayList<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = sdf.parse(currentDate, new ParsePosition(0));
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        for (int i = 0; i < next; i++) {
            rightNow.add(Calendar.DAY_OF_MONTH, 1);// 你要加的日期
            Date dt1 = rightNow.getTime();
            String reStr = sdf.format(dt1);
            list.add(reStr);
        }
        list.add(currentDate);
        rightNow.setTime(dt);
        for (int i = 0; i < pre; i++) {
            rightNow.add(Calendar.DAY_OF_MONTH, -1);// 你要减的日期
            Date dt1 = rightNow.getTime();
            String reStr = sdf.format(dt1);
            list.add(reStr);
        }
        return list;
    }

    /**
     * 获取最近一段时间的整点时间(每30分钟)
     *
     * @return
     */
    public static ArrayList<Date> getRecentlyIntegralPointTime(int sendTimeLimit) {
        ArrayList<Date> list = new ArrayList<>();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH时mm分");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        if (System.currentTimeMillis() - c.getTime().getTime() > 30 * 60 * 1000) {
            c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + 1);
        } else {
            c.set(Calendar.MINUTE, 30);
        }
//        String time = sdf.format(c.getTime());
        list.add(c.getTime());
        for (int i = 0; i < sendTimeLimit * 2 - 1; i++) {
            c.add(Calendar.MINUTE, 30);
//            String s = sdf.format(c.getTime());
            list.add(c.getTime());
        }
        return list;
    }


    /**
     * @param currentDate 当前的时间
     * @param changeValue 正数表示"后多少天"，负数表示"前多少天"
     * @return 返回变化后的时间
     */
    public static String getChangeTime(String currentDate, int changeValue) {
        if (changeValue == 0) {
            return currentDate;
        } else if (changeValue > 0) {
            return getTimeList(currentDate, 0, changeValue).get(changeValue - 1);
        } else {
            return getTimeList(currentDate, -changeValue, 0).get(-changeValue);
        }
    }

    public static String getDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch ((cal.get(Calendar.DAY_OF_WEEK) - 1)) {
            case -1:
                return "日";
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
        }
        return "日";
    }

    public static String getDate(String str) {
        Date date;
        try {
            date = new SimpleDateFormat(LONG_DATEFORMAT).parse(str);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            switch ((cal.get(Calendar.DAY_OF_WEEK) - 1)) {
                case -1:
                    return "日";
                case 1:
                    return "一";
                case 2:
                    return "二";
                case 3:
                    return "三";
                case 4:
                    return "四";
                case 5:
                    return "五";
                case 6:
                    return "六";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "日";
    }

    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATEFORMAT);
        return sdf.format(date);
    }

    /**
     * 当前时间减去12小时的时间
     */
    public static String getSub12Date(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(LONG_DATEFORMAT);
        Date date = sdf.parse(str, new ParsePosition(0));
        long temp = date.getTime() - (12 * 3600 * 1000);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(temp);
        Date date2 = cal.getTime();
        sdf.applyPattern(DATEFORMAT);
        String format = sdf.format(date2);
        return format;
    }

    public static String formatTime(String nowStr, String createStr) {

        if (TextUtils.isEmpty(nowStr) || TextUtils.isEmpty(createStr)) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat();

        sdf.applyPattern(LONG_DATEFORMAT);

        Date nowDate = sdf.parse(nowStr, new ParsePosition(0));

        Date createDate = sdf.parse(createStr, new ParsePosition(0));

        long nowL = nowDate.getTime();

        long createL = createDate.getTime();

        long temp = (nowL - createL) / 1000;

        if (temp <= 0) {
            return "刚刚";
        }

        if (temp <= 60) {

            return temp + "秒前";
        }


        if (temp < 60 * 60) {
            return temp / 60 + "分钟前";
        }

        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(nowDate);

        Calendar createCal = Calendar.getInstance();
        createCal.setTime(createDate);

        int nowYear = nowCal.get(Calendar.YEAR);
        int nowMonth = nowCal.get(Calendar.MONTH) + 1;
        int nowDay = nowCal.get(Calendar.DAY_OF_MONTH);

        int createYear = createCal.get(Calendar.YEAR);
        int createMonth = createCal.get(Calendar.MONTH) + 1;
        int createDay = createCal.get(Calendar.DAY_OF_MONTH);
        String hourAndMin = LibAppUtil.numAddFrontZero(createCal.get(Calendar.HOUR_OF_DAY)) + ":" + LibAppUtil.numAddFrontZero(createCal.get(Calendar.MINUTE));
        if (nowYear == createYear && nowMonth == createMonth && nowDay == createDay) {
            return "今天  "+hourAndMin;
        }

        if (nowYear == createYear && nowMonth == createMonth && nowDay == (createDay + 1)) {
            return "昨天  "+hourAndMin;
        }

        if (nowYear == createYear) {
            return LibAppUtil.numAddFrontZero(createMonth) + "-" + LibAppUtil.numAddFrontZero(createDay)+"  "+hourAndMin;
        }

        return createYear + "-" + LibAppUtil.numAddFrontZero(createMonth) + "-" + LibAppUtil.numAddFrontZero(createDay)+"  "+hourAndMin;
    }

    public static boolean isSameDate(String date1Str, String date2Str) {

        Date date1 = stringToDate(date1Str, LONG_DATEFORMAT);

        Date date2 = stringToDate(date2Str, LONG_DATEFORMAT);

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    public static int getNumberDate(String str) {
        Date date = stringToDate(str, LONG_DATEFORMAT);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static String getNumberMonth(String str) {
        Date date = stringToDate(str, LONG_DATEFORMAT);
        String[] dateStr = new String[]{"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        switch (cal.get(Calendar.MONTH) + 1) {
            case 1:
                return dateStr[0];
            case 2:
                return dateStr[1];
            case 3:
                return dateStr[2];
            case 4:
                return dateStr[3];
            case 5:
                return dateStr[4];
            case 6:
                return dateStr[5];
            case 7:
                return dateStr[6];
            case 8:
                return dateStr[7];
            case 9:
                return dateStr[8];
            case 10:
                return dateStr[9];
            case 11:
                return dateStr[10];
            case 12:
                return dateStr[11];
        }
        return null;
    }

    public static Boolean compareTime(String nowDate, String startTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(nowDate);
            Date d2 = df.parse(startTime);
            if (d1.getTime() >= d2.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String calculateTime(String startTime, String updateTime) {
        long dif;
        long days;
        long hours;
        long mins;
        long seconds;
        long resHours;
        long resMinsOne;
        long resMinsTwo;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = "";
        try {
            Date d1 = df.parse(startTime);
            Date d2 = df.parse(updateTime);
            dif = d1.getTime() - d2.getTime();
            days = dif / (24 * 1000 * 60 * 60);
            hours = dif / (1000 * 60 * 60);
            mins = dif / (60 * 1000);
            seconds = dif / 1000;

            if (seconds < 60) {
                result = seconds + "秒";
                return result;
            }

            if (mins >= 60) {
                if (hours >= 24) {
                    resHours = (hours - days * 24);
                    resMinsOne = (mins - (hours - days * 24) * 60 - days * 24 * 60);
                    if (resHours == 0 && resMinsOne != 0) {
                        result = days + "天" + resMinsOne + "分";
                    } else if (resHours != 0 && resMinsOne == 0) {
                        result = days + "天" + resHours + "小时";
                    } else if (resHours == 0 && resMinsOne == 0) {
                        result = days + "天";
                    } else {
                        result = days + "天" + resHours + "小时" + resMinsOne + "分";
                    }
                } else {
                    resMinsTwo = (mins - hours * 60);
                    if (resMinsTwo == 0) {
                        result = hours + "小时";
                    } else {
                        result = hours + "小时" + resMinsTwo + "分钟";
                    }
                }
            } else {
                if (mins != 0) {
                    result = mins + "分钟";
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String updateTimeFormat(String updateTime) {
        String result = "";
        String cutedTime = "";
        Date date;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            df.parse(updateTime);
            date = df.parse(updateTime);
            cutedTime = df.format(date);
            int index = cutedTime.indexOf("-");
            result = cutedTime.substring(index + 1, (cutedTime.length()));
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 将毫秒格式化:00:00
     *
     * @param time
     * @return
     */
    public static String timeFormat(long time) {
        String sBuffer = "";
        int shi = (int) (time / 1000 / 60 / 60);
        int fen = (int) (time / 1000 / 60 % 60);
        int miao = (int) (time / 1000 % 60);
        /*if (shi < 10) {
            sBuffer += "0" + shi;
        } else {
            sBuffer += "0" + shi;
        }*/
        if (fen < 10) {
            sBuffer += "0" + fen;
        } else {
            sBuffer += "" + fen;
        }
        if (miao < 10) {
            sBuffer += ":0" + miao;
        } else {
            sBuffer += ":" + miao;
        }
        return sBuffer;

    }

    public static long caculateMinsDiff(String now, String gmtEnd) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(gmtEnd);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别

            return diff / 1000 / 60;

        } catch (Exception e) {

        }
        return 0;
    }


    /**
     * 可以格式化成任何格式
     * @param defaultPattern 传入的字符时间格式
     * @param targetPattern  目标时间格式
     * @param formatTime     需要格式化的时间字符串
     * @return
     */
    public static String format(String defaultPattern, String targetPattern, String formatTime) {
        if (TextUtils.isEmpty(formatTime))
            return "";
        SimpleDateFormat format = new SimpleDateFormat(defaultPattern);
        try {
            Date parse = format.parse(formatTime);
            format.applyPattern(targetPattern);
            return format.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatTime;
    }

    public static long caculateMnisDiff(String now, String gmtEnd) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(now);
            Date d2 = df.parse(gmtEnd);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别

            return diff / 1000 / 60;

        } catch (Exception e) {

        }
        return 0;
    }


}
