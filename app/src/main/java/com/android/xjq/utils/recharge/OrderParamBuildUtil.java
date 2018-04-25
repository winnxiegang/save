package com.android.xjq.utils.recharge;


import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderParamBuildUtil {

    /**
     * 构造支付订单参数信息
     *
     * @param map 支付订单参数
     * @return
     */
    public static String buildOrderParam(Map<String, String> map, String charset) {
        String encodeCharset = charset;
        if (TextUtils.isEmpty(charset)) encodeCharset = "utf-8";
        List<String> keys = new ArrayList<>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, encodeCharset));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, encodeCharset));

        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @return
     */
    public static String buildKeyValue(String key, String value, String charset) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        try {
            sb.append(URLEncoder.encode(value, charset));
        } catch (UnsupportedEncodingException e) {
            sb.append(value);
        }
        return sb.toString();
    }

}
