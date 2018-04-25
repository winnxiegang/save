package com.android.xjq.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.widget.Toast;

import com.android.banana.commlib.utils.DimensionUtils;
import com.android.banana.commlib.utils.NetworkUtils;
import com.android.library.Utils.LogUtils;
import com.android.xjq.BuildConfig;
import com.android.xjq.XjqApplication;
import com.umeng.analytics.AnalyticsConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zaozao on 2018/4/9.
 * 反作弊
 */

public class AntiSpamUtils {

    // 获取已经安装的所有应用, PackageInfo　系统类，包含应用信息
    private void getPackages() {
        List<PackageInfo> packages = XjqApplication.getContext().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { //非系统应用
                LogUtils.e("kk", "已安装：appName::::" + packageInfo.applicationInfo.loadLabel(XjqApplication.getContext().getPackageManager()).toString());
            } else { // 系统应用
            }
        }
    }

    //获取网络接入点，这里一般为cmwap和cmnet
    public static String getNetWorkInfo() {
        String netStr = null;
        ConnectivityManager con = (ConnectivityManager) XjqApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = con.getActiveNetworkInfo();
        if (info != null) {
            netStr = info.getExtraInfo();
        }
        LogUtils.e("kk", "接入方式：：" + netStr);
        return netStr;
    }

    public static Map<String, String> getAntiData(Context context, String urlEnum, String isAuto) {
        Map<String, String> map = new HashMap<>();
        //渠道号 -->1092gs
        String channel = getAppMetaData(context, "CHANNEL");//查找manifest里meta-data key为CHANNEL的值
        map.put("channel", channel);
        Toast.makeText(context, channel, Toast.LENGTH_SHORT).show();
        //产品版本 --> xjq2.0
        String version = String.valueOf(BuildConfig.VERSION_NAME);
        map.put("version", version);
        //接口名称 --> get_live_content: 获取直播内容
        map.put("urlEnum", urlEnum);
        //是否客户端自主发出请求 --> 是否自动请求 1轮询接口 0其他
        map.put("是否自动请求", isAuto);
        //是否激活请求 --> 激活or活跃  激活1 活跃0
        map.put("是否激活请求", "0");
        //是否激活成功 --> 激活成功 1成功 0失败
        map.put("是否激活成功", "0");
        //用户唯一标识 --> 8B4C63E483A75E026029C5BA346571F9%7C911407110557068
        map.put("用户唯一标识", android.os.Build.FINGERPRINT);
        //IMEI --> 8B4C63E483A75E026029C5BA346571F9
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();
            map.put("deviceId", deviceId);
        } catch (Exception e) {
        }
        //MEID --> a0000076g81d4a
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method method = manager.getClass().getMethod("getDeviceId", int.class);
            String meid = (String) method.invoke(manager, 2);
            map.put("MEID", meid);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //手机品牌 --> samsung
        String brand = android.os.Build.BRAND;
        map.put("brand", brand);
        //手机型号 --> GT-I9308
        String model = Build.MODEL;
        map.put("model", model);
        //手机版本号 --> GT-I9308-al20
//        map.put("model", model);
        //手机分辨率 --> 1440x2560
        String resolution = DimensionUtils.getScreenWidth(context) + "x" + DimensionUtils.getScreenHeight(context);
        map.put("resolution", resolution);
        //cpu信息 --> hisilicon kirin 960
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            String cpu = array[1];
            map.put("cpu", cpu);
            map.put("cpu", android.os.Build.CPU_ABI);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //运行内存 --> 16G
        String path = "/proc/meminfo";
        String firstLine = null;
        int totalRam = 0;
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader, 8192);
            firstLine = br.readLine().split("\\s+")[1];
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (firstLine != null) {
            totalRam = (int) Math.ceil((new Float(Float.valueOf(firstLine) / (1024 * 1024)).doubleValue()));
        }
        String ram = totalRam + "G";
        map.put("ram", ram);
        //总存储空间 --> 64G
        String totalSize = getTotalSize(context);
        map.put("totalSize", totalSize);
        //可用空间 --> 25G
        String availableSize = getSDAvailableSize(context);
        map.put("availableSize", availableSize);
        //手机平台 --> Android
        map.put("phone", "Android");
        //手机系统版本 --> 7.0
        String releaseVersion = android.os.Build.VERSION.RELEASE;
        map.put("releaseVersion", releaseVersion);
        //内核版本 --> 4.1.18-gd9f2367
        map.put("kernelVersion", getKernelVersion());
        //IP --> 10.0.0.165
        map.put("ip", getIPAddress(context));
        //经纬度 -->
        String location = getLocation(context);
        if (null != location)
            map.put("ll", location);
        //运营商 --> 898600（中国移动），898601（中国联通）中国电信460030
        String operator = getOperator(context);
        if (!TextUtils.isEmpty(operator))
            map.put("operator", operator);
        //网络类型 --> 2g、3g、4g、wifi
        String network = getNetworkType(context);
        if (!TextUtils.isEmpty(network))
            map.put("network", network);
        //接入方式 --> cmnet、cmwap
        String netWorkInfo = getNetWorkInfo();
        map.put("接入方式", netWorkInfo);
        //安装app列表 --> ['微信','支付宝','香蕉球','携程']
        String list = getAppList(context);
        map.put("applist", list);

        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String value = (String) entry.getValue();
            String key = (String) entry.getKey();
            System.out.println(key + ";" + value + "\n");
        }

        return map;
    }

    /**
     * 获得SD卡总大小
     */
    private static String getTotalSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 获得SD卡剩余大小
     */
    private static String getSDAvailableSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    /**
     * 获取内核版本
     */
    public static String getKernelVersion() {
        String kernelVersion = "";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("/proc/version");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return kernelVersion;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8 * 1024);
        String info = "";
        String line = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                info += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (info != "") {
                final String keyword = "version ";
                int index = info.indexOf(keyword);
                line = info.substring(index + keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return kernelVersion;
    }

    /**
     * 获取当前的运营商
     */
    public static String getOperator(Context context) {
        try {
            String ProvidersName = "";
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String IMSI = telephonyManager.getSubscriberId();
            if (IMSI != null) {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                    ProvidersName = "中国移动";
                } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
                    ProvidersName = "中国联通";
                } else if (IMSI.startsWith("46003")) {
                    ProvidersName = "中国电信";
                }
                return ProvidersName;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取网络类型
     */
    public static String getNetworkType(Context context) {
        int state = NetworkUtils.getNetworkState(context);
        if (state == NetworkUtils.NETWORK_WIFI)
            return "wifi";
        else if (state == NetworkUtils.NETWORK_MOBILE)
            return "4g";
        else return "";
    }

    public static String getAppList(Context context) {
        ArrayList<String> appList = new ArrayList<>(); //用来存储获取的应用信息数据
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appList.add(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());//如果非系统应用，则添加至appList
            }
        }
        return appList.toString();
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static String getLocation(Context context) {
        Location location = LocationUtils.getInstance(context).showLocation();
        if (location != null) {
            String address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude();
            System.out.println(address);
            return address;
        }
        return null;
    }

    /**
     * 获取application中指定的meta-data。本例中，调用方法时key就是UMENG_CHANNEL
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }

}
