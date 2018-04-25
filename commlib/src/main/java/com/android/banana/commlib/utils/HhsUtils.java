package com.android.banana.commlib.utils;

import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by lingjiu on 2017/4/16.
 */

public class HhsUtils {
    public final static String PHONE_PATTERN = "^1[0-9]{10}$";

    public static boolean isMatchered(String patternStr, CharSequence input) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * 按字节截取
     * 汉字不可以截半,汉字截半的话对应字节的ASC码为小于0的数值
     */
    public static String splitString2(String src, int len) {
        int byteNum = 0;
        if (null == src) {
            return null;
        }
        byteNum = src.length();
        byte bt[] = new byte[0]; // 将String转换成byte字节数组
        bt = src.getBytes();
        if (len >= bt.length) {
            return src;
        }
        // 判断是否出现了截半，截半的话字节对于的ASC码是小于0的值
        String subStrx = null;
//        try {
        if (bt[len] < 0) {
            if (bt[--len] < 0) {
                subStrx = new String(bt, 0, --len);
            } else {
                subStrx = new String(bt, 0, len);
            }
        } else {
            subStrx = new String(bt, 0, len);
        }
        Log.i("", "subStrx==" + subStrx);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return subStrx + "...";
    }

    /**
     * @param original
     * @param count
     * @return
     */
    public static String splitString(String original, int count) {

        StringBuilder sb = new StringBuilder();
        // 这儿是 count 和 i 两头并进
        // i 每次都 +1 ,每次都会至少找到1个字符（英文1个，中文2个）
        // 如果是中文字符，count 就-1
        // 对于半个中文
        for (int i = 0; i < count - 1; i++) {
            if (i >= original.length()) break;
            char c = original.charAt(i);
            if (String.valueOf(c).getBytes().length > 1) {
                count--;
            }
            sb.append(c);
        }
        if (!original.equals(sb.toString())) sb.append("...");
        return sb.toString();
    }

    public static int spToPx(Context context, int sp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics()));
    }

    public static String formatMoney(long money) {
        String s;
        if (money > 10000) {
//         NumberFormat nf = NumberFormat.getNumberInstance();
            DecimalFormat df = new DecimalFormat("0.00");
            double wan = (double) money / 10000;
            s = df.format(wan) + "万";
        } else {
            s = String.valueOf(money);
        }
        return s;
    }

    public static void showToast(final Toast toast, long duration) {
        final long SHORT = 2000;
        final long LONG = 3500;
        final long ONE_SECOND = 1000;
        final long d = duration <= SHORT ? SHORT : duration > LONG ? duration : LONG;
        toast.setGravity(Gravity.CENTER, 0, 0);
        //使textView换行也保持内容居中
        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
        tv.setGravity(Gravity.CENTER);
        new CountDownTimer(Math.max(d, duration), ONE_SECOND) {

            @Override
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            @Override
            public void onFinish() {
                toast.cancel();
            }
        }.start();
    }


    public static String getAppDeviceId() {
        String appDeviceId = (String) SharePreferenceUtils.getData( SharePreferenceUtils.APP_DEVICE_ID, null);
        if (!TextUtils.isEmpty(appDeviceId)) return appDeviceId;
        appDeviceId = getUniquePsuedoID();
        SharePreferenceUtils.putData( SharePreferenceUtils.APP_DEVICE_ID, appDeviceId);
        return appDeviceId;
    }

    public static String getUniquePsuedoID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static String getPesudoUniqueID() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10 +//13 digits
                Build.SERIAL.length() % 10;
        return m_szDevIDShort;
    }

    public static String getMacAddress() {
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return macAddress;
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return macAddress;
        }
        return macAddress;
    }

}
