/*
 * mywebsite.com Inc.
 * Copyright (c) 2004-2007 All Rights Reserved.
 */
package com.android.banana.commlib.encrypt;


import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5签名算法
 */
public class Md5Encrypt {

    private static final String DEFAULT_CHARSET = "GBK";


    public static String md5Simple(String content) {
        return md5("", content);
    }

    public static String md5Simple(String content, String charset) {
        return md5("", content, charset);
    }

    public static String md5(String key, String text) {
        return md5(key, text, null);
    }

    public static String md5(byte[] key, String text) {
        return md5(key, text, null);
    }

    public static String md5(String key, String text, String charset) {
        charset = defaultIfEmpty(charset, DEFAULT_CHARSET);
        return md5(TextUtils.isEmpty(key) ? null : getBytes(key, charset), text, charset);
    }

    private static byte[] getBytes(String key, String charset) {
        try {
            return TextUtils.isEmpty(key) ? null : key.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String defaultIfEmpty(String charset, String defaultCharset) {
        return TextUtils.isEmpty(charset) ? defaultCharset : charset;
    }

    public static String md5(byte[] key, String text, String charset) {
        charset = defaultIfEmpty(charset, DEFAULT_CHARSET);
        if (TextUtils.isEmpty(text))
            return null;
        byte[] data = getBytes(text, charset);
        return md5(key, data);
    }

    public static String md5(byte[] key, byte[] content) {

        MessageDigest msgDigest = null;

        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("System doesn't support MD5 algorithm.");
        }
        if (key != null && key.length > 0) {
            msgDigest.update(key);
        }
        try {
            msgDigest.update(content);
        } catch (Exception e) {
            // 不会发生

            return null;
        }

        byte[] bytes = msgDigest.digest();

        byte tb;
        char low;
        char high;
        char tmpChar;

        String md5Str = new String();

        for (byte aByte : bytes) {
            tb = aByte;

            tmpChar = (char) ((tb >>> 4) & 0x000f);

            if (tmpChar >= 10) {
                high = (char) (('a' + tmpChar) - 10);
            } else {
                high = (char) ('0' + tmpChar);
            }

            md5Str += high;
            tmpChar = (char) (tb & 0x000f);

            if (tmpChar >= 10) {
                low = (char) (('a' + tmpChar) - 10);
            } else {
                low = (char) ('0' + tmpChar);
            }

            md5Str += low;
        }

        return md5Str;
    }

    /******************
     * 以下为16位MD5算法
     ****************/
    private final static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
            'C', 'D', 'E', 'F'};

    private static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        int t;
        for (int i = 0; i < 16; i++) {
            t = bytes[i];
            if (t < 0)
                t += 256;
            sb.append(hexDigits[(t >>> 4)]);
            sb.append(hexDigits[(t % 16)]);
        }
        return sb.toString();
    }

    public static String md5(String input) {
        return md5(input, null, 32);
    }

    public static String md5(String input, int bit) {
        return md5(input, null, bit);
    }

    public static byte[] md5ToBytes(byte[] input) {

        try {
            MessageDigest md = MessageDigest.getInstance(System.getProperty("MD5.algorithm", "MD5"));
            return md.digest(input);
        } catch (Exception e) {
            // 不会发生

            return null;
        }
    }

    public static String md5(String input, String charset, int bit) {
        charset = TextUtils.isEmpty(charset) ? DEFAULT_CHARSET : charset;
        if (TextUtils.isEmpty(input)) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance(System.getProperty("MD5.algorithm", "MD5"));
            if (bit == 16)
                return bytesToHex(md.digest(input.getBytes(charset))).substring(8, 24);
            return bytesToHex(md.digest(input.getBytes(charset)));
        } catch (Exception e) {
            // 不会发生

            return null;
        }
    }
}