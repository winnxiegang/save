package com.android.banana.commlib.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.commlib.R;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;
import static android.os.Build.VERSION_CODES.HONEYCOMB_MR1;

/**
 * Created by zhouyi on 2015/11/3 10:10.
 */
public class LibAppUtil {

    private static Toast mCurrentToast;

    public static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            Log.e("getPackageInfo: ", pi.versionCode + "--" + pi.versionName);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * dp转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px 转dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {

        float scale = context.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);

    }

    public static String imageUrlSmallToLarge(String imageUrl) {
        return imageUrl.replace("Mid", "");
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 对网络连接状态进行判断
     *
     * @param context
     * @return true 可用；false 不可用
     */
    public static boolean isOpenNetwork(Context context, boolean showTip) {

        boolean result = false;

        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            result = connManager.getActiveNetworkInfo().isAvailable();
        }

        if (!result && showTip) {
            Toast.makeText(context, "无网络，请检查后重试.",
                    Toast.LENGTH_LONG).show();

            // toast.setGravity(Gravity.CENTER, 0, 0);
        }

        return result;
    }

    /**
     * @param context
     * @param tip
     * @param duration
     * @param gravity
     */
    public static void showTip(Context context, String tip, int duration, int gravity) {

        if (mCurrentToast != null) {
            mCurrentToast.cancel();
            mCurrentToast = null;
        }

        mCurrentToast = Toast.makeText(context, tip, duration);

        if (gravity > -1) {
            mCurrentToast.setGravity(gravity, 0, 0);
        }
        mCurrentToast.show();

    }

    public static void showTip(Context context, CharSequence tip) {
        if (mCurrentToast != null) {
            mCurrentToast.cancel();
            mCurrentToast = null;
        }
        mCurrentToast = Toast.makeText(context.getApplicationContext(), tip, Toast.LENGTH_LONG);
        mCurrentToast.show();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 去除以指定符号结尾后面的所有数据
     *
     * @param sb
     * @param symbol
     */
    public static void rTrim(StringBuilder sb, String symbol) {

        int index = sb.lastIndexOf(symbol);

        if (index != -1) {

            sb.delete(index, sb.length());
        }

    }

    public static String trimSymbol(String str, String symbol) {

        if (StringUtils.isBlank(str)) {
            return str;
        }

        if (str.startsWith(symbol)) {
            int start = str.indexOf(symbol);
            if (start != -1) {

                str = str.substring(start + 1);

            }
        }

        if (str.endsWith(symbol)) {
            int end = str.lastIndexOf(symbol);

            if (end != -1) {
                str = str.substring(0, end);
            }
        }

        return str;

    }

    public static final String maskUserName(String text, String maskChar) {

        if (StringUtils.isBlank(text)) {
            return text;
        }
        int len = text.length();
        String str = "";
        if (len == 1) {
            str = text;
        } else if (len == 2) {
            str = text.substring(0, 1).concat(maskChar);
        } else if (len == 3) {
            str = text.substring(0, 1).concat(maskChar)
                    .concat(text.substring(2, 3));
        } else if (len == 4) {
            str = text.substring(0, 1).concat(fill(maskChar, 2))
                    .concat(text.substring(len - 1, len));
        } else if (len == 5) {
            str = text.substring(0, 2).concat(fill(maskChar, len - 3))
                    .concat(text.substring(len - 1, len));
        } else if (len >= 6) {
            str = text.substring(0, 2).concat(fill(maskChar, len - 4))
                    .concat(text.substring(len - 2, len));
        }

        return str;

    }

    private static String fill(String value, int length) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {

            sb.append(value);

        }

        return sb.toString();
    }


    public static String warpParams(Map<String, String> params) {

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {

            if (!entry.getKey().startsWith("temp_")) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
            }

        }

        return sb.toString();

    }


    /**
     * 将金额格式化为每三位添加一个逗号
     */
    public static String formatMoney(long n, boolean isYuan) {
        if (n == -1) {
            return Long.toString(0);
        }

        if (isYuan) {
            StringBuffer sb = new StringBuffer(Long.toString(n));
            for (int i = sb.length(); i > 0; i -= 3) {
                if (i != sb.length()) {
                    sb.insert(i, ",");
                }

            }
            return sb.toString();
        } else {
            return Long.toString(n);
        }

    }

    /**
     * 将金额格式化为每三位添加一个逗号
     */
    public static String formatMoney(long n) {
        if (n == -1) {
            return Long.toString(0);
        }
        return convert(n);


    }

    public static String convert(long money) {
        int danYi = 100000000;
        int danWan = 10000;
        int yi = (int) (money / danYi);
        int wan = (int) ((money - yi * danYi) / danWan);
        int yuan = (int) (money - (yi * danYi) - (wan * danWan));
        StringBuffer sb = new StringBuffer();
        if (yi > 0) {
            sb.append(yi + "亿");
        }
        if (wan > 0) {
            sb.append(wan + "万");
        }
        sb.append(yuan + "元");
        return sb.toString();

    }

    /**
     * 截取到单位万
     *
     * @param s
     * @return
     */
    public static String getWan(String s) {
        int index = s.indexOf("万");
        if (index > 0) {
            return s.substring(0, index + 1);
        }
        return s;
    }

    /**
     * 格式化亚赔数据
     * 四舍六入5显示
     */
    public static String formatAsianOdds(double d) {

        String str = Double.toString(d);
        if (str.length() >= 5) {
            String b = str.substring(str.length() - 1);
            if (Integer.parseInt(b) == 5) {
                return new DecimalFormat("#0.000").format(d);
            } else {
                return new DecimalFormat("#0.00").format(d);
            }
        } else {
            return new DecimalFormat("#0.00").format(d);
        }
    }

    public static String formatScore(int homeScore, int guestScore) {
        if (homeScore < 0 || guestScore < 0) {
            return "-";
        } else {
            return homeScore + ":" + guestScore;
        }
    }


    /**
     * 设置EditText最大长度
     *
     * @param et
     * @param maxlength
     */
    public static void setEditTextMaxLength(EditText et, int maxlength) {
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxlength)});
    }

    /**
     * 通过id名,获取id
     */
    public static int getId(Context context, String name) {
        Resources res = context.getResources();
        final String packageName = context.getPackageName();
        int id = res.getIdentifier(name, "id", packageName);
        return id;
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


    /**
     * check availability of Internet
     *
     * @param context
     * @return true or false
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }

    public static StringBuffer deleteLastSpecialChar(StringBuffer sb, String s) {

        if (sb.lastIndexOf(s) != -1) {

            sb.deleteCharAt(sb.length() - 1);

        }
        return sb;

    }

    public static void hideSoftKeyboard(Activity context) {
        /**隐藏软键盘**/
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static Double maxValue(double[] ds) {
        double maxValue = Double.MIN_VALUE;
        for (Double d : ds) {
            if (d > maxValue) {
                maxValue = d;
            }
        }
        return maxValue;
    }

    public static Double minValue(double[] ds) {
        double minValue = Double.MAX_VALUE;
        for (Double d : ds) {
            if (d < minValue) {
                minValue = d;
            }
        }
        return minValue;
    }

    public static SpannableString setSpan(String value) {

        SpannableString ss = new SpannableString(value);

        BackgroundColorSpan span = new BackgroundColorSpan(Color.parseColor("#f63f3f"));

        ss.setSpan(span, 0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan span1 = new ForegroundColorSpan(Color.WHITE);

        ss.setSpan(span1, 0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    public static void showSoftKeyboard(Context context, EditText view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
    }

    public static String getFromRaw(Context context, String fileName) {

        StringBuilder buf = new StringBuilder();

        InputStream json = null;

        BufferedReader in = null;

        String mainContent = "";

        try {

            json = context.getAssets().open(fileName);

            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            mainContent = buf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (json != null) {
                try {
                    json.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mainContent;
    }

    /**
     * 判断文件是否存在
     *
     * @param strFile
     * @return
     */
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {

        boolean isWork = false;

        ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);

        if (myList.size() <= 0) {
            return false;
        }

        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }

        return isWork;

    }

    public static int calculateMemoryCacheSize(Context context) {

        ActivityManager am = getService(context, ACTIVITY_SERVICE);

        boolean largeHeap = (context.getApplicationInfo().flags & FLAG_LARGE_HEAP) != 0;

        int memoryClass = am.getMemoryClass();

        if (largeHeap && SDK_INT >= HONEYCOMB) {
            memoryClass = ActivityManagerHoneycomb.getLargeMemoryClass(am);
        }

        // Target ~15% of the available heap.
        return 1024 * 1024 * memoryClass / 7;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getService(Context context, String service) {
        return (T) context.getSystemService(service);
    }

    static int getBitmapBytes(Bitmap bitmap) {
        int result;
        if (SDK_INT >= HONEYCOMB_MR1) {
            result = BitmapHoneycombMR1.getByteCount(bitmap);
        } else {
            result = bitmap.getRowBytes() * bitmap.getHeight();
        }
        if (result < 0) {
            throw new IllegalStateException("Negative size: " + bitmap);
        }
        return result;
    }

    public static void showTip(Context context, String msg, int icon) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_customer_toast, null);

        ImageView collectIv = (ImageView) view.findViewById(R.id.collectIv);

        TextView collectTv = (TextView) view.findViewById(R.id.collectTv);

        collectIv.setImageResource(icon);

        collectTv.setText(msg);

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.CENTER, 0, 0);

        toast.setView(view);

        toast.show();

    }

    public static int getTextWidth(Context context, String text) {

        TextPaint paint = new TextPaint();

        paint.setTextSize(context.getResources().getDimension(R.dimen.sp14));

        return (int) paint.measureText(text);

    }

    /**
     * 将dp值转换为px值。
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int getDpToPxValue(Context context, int dpValue) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics()));
    }

    @TargetApi(HONEYCOMB)
    private static class ActivityManagerHoneycomb {
        static int getLargeMemoryClass(ActivityManager activityManager) {
            return activityManager.getLargeMemoryClass();
        }
    }

    @TargetApi(HONEYCOMB_MR1)
    private static class BitmapHoneycombMR1 {
        static int getByteCount(Bitmap bitmap) {
            return bitmap.getByteCount();
        }
    }

    /**
     * 格式化让球信息，如果让球数大于0，则加上一个"+"号
     *
     * @param plate
     * @return
     */
    public static String getFormatPlateMessage(int plate) {
        if (plate > 0) {
            return "+" + plate;
        } else {
            return String.valueOf(plate);
        }
    }

    public static float measureTextSize(String content, TextView tv) {

        TextPaint newPaint = new TextPaint();

        newPaint.setTextSize(tv.getTextSize());

        float newPaintWidth = newPaint.measureText(content);

        return newPaintWidth;

    }

    public static boolean isListBlank(List<Object> list) {
        if (list == null) {
            return true;
        }
        if (list.size() == 0) {
            return true;
        }
        return false;
    }


    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Point size = new Point();

        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);

        return size.x;

    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Point size = new Point();

        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);

        return size.y;

    }

    public static String getFirstAlphabet(String str) {

//        if (StringUtils.isBlank(str)) {
//            return "#";
//        }
//
//        char[] charArray = str.trim().toCharArray();
//
//        char firstChar = charArray[0];
//
//        if (firstChar >= 65 && firstChar <= 90 || firstChar >= 97 && firstChar <= 122) {
//            return String.valueOf(firstChar);
//        }
//
//        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
//
//        // UPPERCASE：大写  (ZHONG)
//        // LOWERCASE：小写  (zhong)
//        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//
//        // WITHOUT_TONE：无音标  (zhong)
//        // WITH_TONE_NUMBER：1-4数字表示英标  (zhong4)
//        // WITH_TONE_MARK：直接用音标符（必须WITH_U_UNICODE否则异常）  (zhòng)
//        format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
//
//        // WITH_V：用v表示ü  (nv)
//        // WITH_U_AND_COLON：用"u:"表示ü  (nu:)
//        // WITH_U_UNICODE：直接用ü (nü)
//        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
//
//        try {
//            String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(firstChar, format);
//            if (pinyin == null) {
//                return "#";
//            } else {
//                return pinyin[0].substring(0, 1);
//            }
//        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
//            badHanyuPinyinOutputFormatCombination.printStackTrace();
//        }
//
//        return "#";
        return "";
    }
}
