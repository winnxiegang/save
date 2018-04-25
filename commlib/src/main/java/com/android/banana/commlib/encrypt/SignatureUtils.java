package com.android.banana.commlib.encrypt;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 签名工具类
 *
 * @author chenbug
 * @version $Id: SignatureUtils.java, v 0.1 2011-7-27 下午04:24:13 chenbug Exp $
 */
public class SignatureUtils {

    public static String getSignatureContent(Map<String, ?> dataMap) {
        return getSignatureContent(dataMap, null, false);
    }

    public static String getSignatureContent(Map<String, ?> dataMap, String[] filterKeys, boolean appendNull) {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<>();
        keys.addAll(dataMap.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = toString(dataMap.get(key));
            if (!appendNull && TextUtils.isEmpty(value)) {
                continue;
            }
            if (filterKeys != null && contains(filterKeys, key)) {
                continue;
            }
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }

        return content.toString();
    }

    private static String toString(Object key) {
        return key == null ? null : key.toString();
    }

    private static boolean contains(final Object[] array, final Object objectToFind) {
        return indexOf(array, objectToFind) != -1;
    }

    private static int indexOf(final Object[] array, final Object objectToFind) {
        return indexOf(array, objectToFind, 0);
    }

    private static int indexOf(final Object[] array, final Object objectToFind, int startIndex) {
        if (array == null) {
            return -1;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (objectToFind == null) {
            for (int i = startIndex; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else if (array.getClass().getComponentType().isInstance(objectToFind)) {
            for (int i = startIndex; i < array.length; i++) {
                if (objectToFind.equals(array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }
}