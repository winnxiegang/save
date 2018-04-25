package com.tencent.ilivesdk;

import android.util.Base64;

import com.tencent.ilivesdk.core.ILiveLog;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 通用方法类
 */
public class ILiveFunc {
    private final static String TAG = "ILVB-Func";

    static public long getCurrentSec(){
        return System.currentTimeMillis() / 1000;
    }
    static public int generateAVCallRoomID(){
        Random random = new Random();
        long uTime = getCurrentSec();
//        int roomid = (int)((uTime - 1465264500)*100 + random.nextInt(100));
        int roomid = (int)(uTime*100%(Integer.MAX_VALUE - 100) + random.nextInt(100));
        return roomid;
    }

    public static void notifySuccess(ILiveCallBack callBack, Object data){
        if (null != callBack){
            callBack.onSuccess(data);
        }
    }

    public static void notifyError(ILiveCallBack callBack, String module, int errCode, String errMsg){
        if (null != callBack){
            callBack.onError(module, errCode, errMsg);
        }
    }

    public static String getArrStr(String[] array) {
        String strRet = "";
        if (null != array) {
            for (String data : array) {
                strRet += (data + ",");
            }
        }
        return strRet;
    }

    public static String getExceptionInfo(Exception e){
        String info = e.toString();

        StackTraceElement[] eles = e.getStackTrace();
        if (null != eles){
            for (StackTraceElement ele : eles) {
                info = info + "\n\tat "
                        + ele.getClassName() + "." + ele.getMethodName() + "("
                        + ele.getFileName() + ":" + ele.getLineNumber() + ")";
            }
        }
        return info;
    }

    /**
     * 计算SHA值
     * @param strSource 源字符串
     * @return
     * @throws Exception
     */
    public static String getHmacSHA1(String secretKey, String strSource) throws Exception{
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes("UTF-8"), mac.getAlgorithm());
        mac.init(secret);

        byte[] sh1bytes = mac.doFinal(strSource.getBytes("UTF-8"));
        return Base64.encodeToString(sh1bytes, Base64.NO_WRAP);     // 避免自动添加换行符
    }

    public static String getMD5(String sourceStr, boolean bTotal){
        try{
            String result = "";
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (byte aB : b) {
                i = aB;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            if (bTotal)
                return result;
            else
                return result.substring(8, 24);
        }catch (Exception e){
            return sourceStr;
        }
    }

    /**
     * 计算云图sign
     * @param secretKey
     * @param path
     * @return
     */
    public static String getSignature(String secretKey, String path){
        String srcStr = "GET";
        try {
            int start = path.indexOf("//") + 2;
            int pos = path.indexOf("?") + 1;
            if (start < 2)
                start = 0;
            if (pos < 1)
                return "";
            // 添加路径
            srcStr += path.substring(start, pos);
            // 分割参数
            List<String> params = Arrays.asList(path.substring(pos).split("&"));
            Comparator<String> comparator = new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    return lhs.compareTo(rhs);
                }
            };
            Collections.sort(params, comparator);
            for (String param : params) {
                srcStr += (param + '&');
            }
            srcStr = srcStr.substring(0, srcStr.length() - 1);
            ILiveLog.d(TAG, "getSignature->src:" + srcStr);
            return getHmacSHA1(secretKey, srcStr);
        }catch (Exception e){
            ILiveLog.e(TAG, "getSignature->error:"+e.toString());
            return "";
        }
    }

    public static String byte2HexStr(byte[] bs){
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        for (byte b : bs) {
            sb.append(chars[(b & 0xF0) >> 4]);
            sb.append(chars[b & 0x0F]);
        }
        return sb.toString();
    }
}
