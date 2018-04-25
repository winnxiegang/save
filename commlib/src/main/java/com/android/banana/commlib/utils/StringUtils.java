package com.android.banana.commlib.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenbug
 * @version $Id: StringUtils.java,v 0.1 2009-5-21 上午12:04:01 chenbug Exp $
 */
public class StringUtils {

    public static final String MARK_SIGN = "`";

    public static final String ASTERRISK_SIGN = "*";

    public static final String VERTICAL_BAR_SIGN = "|";

    public static final String COMMA_SIGN = ",";

    public static final String CHN_COMMA_SIGN = "，";

    public static final String SEMICOLON_SIGN = ";";

    public static final String CARET_SIGN = "^";

    public static final String COLON_SIGN = ":";

    public static final String DOLLAR_SIGN = "$";

    public static final String AND_SIGN = "&";

    public static final String SLASH_SIGN = "/";

    public static final String AT_SIGN = "@";

    public static final String UNDERSCORE_SIGN = "_";

    public static final String TILDE_SIGN = "~";

    public static final String EQUAL_SIGN = "=";

    public static final String PLUS_SIGN = "+";

    public static final String SUB_SIGN = "-";

    public static final String DOT_SIGN = ".";

    public static final String DOUBLE_QUOTE_SIGN = "\"";

    public static final String SIGNLE_QUOTE_SIGN = "'";

    public static final String LEFT_PARENTHESIS_SIGN = "(";

    public static final String RIGHT_PARENTHESIS_SIGN = ")";

    public static final String[] LINE_SPLITER = new String[]{"\r\n", "\r",
            "\n"};

    public static final String RIGHT_ANGLE_QUOTE = ">";

    public static final String LEFT_ANGLE_QUOTE = "<";

    public static final String PERCENTAGE_SIGN = "%";

    public static final String LEFT_SQUARE_BRACKET = "[";

    public static final String RIGHT_SQUARE_BRACKET = "]";

    public static final String POUND_SIGN = "#";

    public static final StringUtils INSTANCE = new StringUtils();

    public static final String HTML_BLANK = new String(
            new char[]{(char) 160});

    private static final String HTML_INVISIBLE_FILTER_REGEXP = "[\\u007f-\\u009f]|\\u00ad|[\\u0483-\\u0489]|[\\u0559-\\u055a]|\\u058a|[\\u0591-\\u05bd]|\\u05bf|[\\u05c1-\\u05c2]|[\\u05c4-\\u05c7]|[\\u0606-\\u060a]|[\\u063b-\\u063f]|\\u0674|[\\u06e5-\\u06e6]|\\u070f|[\\u076e-\\u077f]|\\u0a51|\\u0a75|\\u0b44|[\\u0b62-\\u0b63]|[\\u0c62-\\u0c63]|[\\u0ce2-\\u0ce3]|[\\u0d62-\\u0d63]|\\u135f|[\\u200b-\\u200f]|[\\u2028-\\u202e]|\\u2044|\\u2071|[\\uf701-\\uf70e]|[\\uf710-\\uf71a]|\\ufb1e|[\\ufc5e-\\ufc62]|\\ufeff|\\ufffc";
    private static final Pattern htmlInvisibleFilterPattern = Pattern
            .compile(HTML_INVISIBLE_FILTER_REGEXP);

    /*
     * ==========================================================================
     * ==
     */
    /* 常量和singleton。 */
    /*
     * ==========================================================================
     */

    /**
     * 空字符串。
     */
    public static final String EMPTY_STRING = "";

    /**
     * 空格字符串。
     */
    public static final String BLANK_STRING = " ";

    /*
     * ==========================================================================
     * ==
     */
    /* 判空函数。 */
    /*                                                                              */
    /* 以下方法用来判定一个字符串是否为： */
    /* 1. null */
    /* 2. empty - "" */
    /* 3. blank - "全部是空白" - 空白由Character.isWhitespace所定义。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 检查字符串是否为<code>null</code>或空字符串<code>""</code>。
     * <p>
     * <pre>
     * StringUtil.isEmpty(null)      = true
     * StringUtil.isEmpty(&quot;&quot;)        = true
     * StringUtil.isEmpty(&quot; &quot;)       = false
     * StringUtil.isEmpty(&quot;bob&quot;)     = false
     * StringUtil.isEmpty(&quot;  bob  &quot;) = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果为空, 则返回<code>true</code>
     */
    public static boolean isEmpty(String str) {
        return ((str == null) || (str.length() == 0));
    }

    public static StringBuffer newStringBuffer() {
        return new StringBuffer();
    }

    public static StringBuffer newStringBuffer(String string) {
        return new StringBuffer(string);
    }

    /**
     * 过滤不可见的HTML
     *
     * @param content
     * @return
     */
    public static String filterInvisibleHtml(String content) {
        if (StringUtils.isEmpty(content)) {
            return content;
        }
        Matcher matcher = htmlInvisibleFilterPattern.matcher(content);
        return matcher.replaceAll("");
    }

    /**
     * Delete all occurrences of the given substring.
     *
     * @param inString the original String
     * @param pattern  the pattern to delete all occurrences of
     * @return the resulting String
     */
    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    /**
     * 判断数组是否是空的
     *
     * @param strs
     * @return
     */
    public static boolean isEmpty(String[] strs) {
        if (ArrayUtils.isEmpty(strs))
            return true;
        for (String str : strs) {
            if (!StringUtils.isEmpty(StringUtils.trim(str))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查字符串是否不是<code>null</code>和空字符串<code>""</code>。
     * <p>
     * <pre>
     * StringUtil.isEmpty(null)      = false
     * StringUtil.isEmpty(&quot;&quot;)        = false
     * StringUtil.isEmpty(&quot; &quot;)       = true
     * StringUtil.isEmpty(&quot;bob&quot;)     = true
     * StringUtil.isEmpty(&quot;  bob  &quot;) = true
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果不为空, 则返回<code>true</code>
     */
    public static boolean isNotEmpty(String str) {
        return ((str != null) && (str.length() > 0));
    }

    /**
     * @param str
     * @param matchs
     * @return
     */
    public static final boolean containsAny(String str, String[] matchs) {
        if (matchs == null || matchs.length <= 0)
            return false;
        for (String match : matchs) {
            if (contains(str, match))
                return true;
        }
        return false;
    }

    /**
     * 检查字符串是否是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符。
     * <p>
     * <pre>
     * StringUtil.isBlank(null)      = true
     * StringUtil.isBlank(&quot;&quot;)        = true
     * StringUtil.isBlank(&quot; &quot;)       = true
     * StringUtil.isBlank(&quot;bob&quot;)     = false
     * StringUtil.isBlank(&quot;  bob  &quot;) = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果为空白, 则返回<code>true</code>
     */
    public static boolean isBlank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查字符串是否不是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符。
     * <p>
     * <pre>
     * StringUtil.isBlank(null)      = false
     * StringUtil.isBlank(&quot;&quot;)        = false
     * StringUtil.isBlank(&quot; &quot;)       = false
     * StringUtil.isBlank(&quot;bob&quot;)     = true
     * StringUtil.isBlank(&quot;  bob  &quot;) = true
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果为空白, 则返回<code>true</code>
     */
    public static boolean isNotBlank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 默认值函数。 */
    /*                                                                              */
    /* 当字符串为null、empty或blank时，将字符串转换成指定的默认字符串。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 如果字符串是<code>null</code>，则返回空字符串<code>""</code>，否则返回字符串本身。
     * <p>
     * <pre>
     * StringUtil.defaultIfNull(null)  = &quot;&quot;
     * StringUtil.defaultIfNull(&quot;&quot;)    = &quot;&quot;
     * StringUtil.defaultIfNull(&quot;  &quot;)  = &quot;  &quot;
     * StringUtil.defaultIfNull(&quot;bat&quot;) = &quot;bat&quot;
     * </pre>
     *
     * @param str 要转换的字符串
     * @return 字符串本身或空字符串<code>""</code>
     */
    public static String defaultIfNull(String str) {
        return (str == null) ? EMPTY_STRING : str;
    }

    /**
     * 如果字符串是<code>null</code>，则返回指定默认字符串，否则返回字符串本身。
     * <p>
     * <pre>
     * StringUtil.defaultIfNull(null, &quot;default&quot;)  = &quot;default&quot;
     * StringUtil.defaultIfNull(&quot;&quot;, &quot;default&quot;)    = &quot;&quot;
     * StringUtil.defaultIfNull(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
     * StringUtil.defaultIfNull(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     */
    public static String defaultIfNull(String str, String defaultStr) {
        return (str == null) ? defaultStr : str;
    }

    /**
     * 如果字符串是<code>null</code>或空字符串<code>""</code>，则返回空字符串<code>""</code>
     * ，否则返回字符串本身。
     * <p>
     * <p>
     * 此方法实际上和<code>defaultIfNull(String)</code>等效。
     * <p>
     * <pre>
     * StringUtil.defaultIfEmpty(null)  = &quot;&quot;
     * StringUtil.defaultIfEmpty(&quot;&quot;)    = &quot;&quot;
     * StringUtil.defaultIfEmpty(&quot;  &quot;)  = &quot;  &quot;
     * StringUtil.defaultIfEmpty(&quot;bat&quot;) = &quot;bat&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要转换的字符串
     * @return 字符串本身或空字符串<code>""</code>
     */
    public static String defaultIfEmpty(String str) {
        return (str == null) ? EMPTY_STRING : str;
    }

    /**
     * 如果字符串是<code>null</code>或空字符串<code>""</code>，则返回指定默认字符串，否则返回字符串本身。
     * <p>
     * <pre>
     * StringUtil.defaultIfEmpty(null, &quot;default&quot;)  = &quot;default&quot;
     * StringUtil.defaultIfEmpty(&quot;&quot;, &quot;default&quot;)    = &quot;default&quot;
     * StringUtil.defaultIfEmpty(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
     * StringUtil.defaultIfEmpty(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     */
    public static String defaultIfEmpty(String str, String defaultStr) {
        return ((str == null) || (str.length() == 0)) ? defaultStr : str;
    }

    /**
     * 如果字符串是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符，则返回空字符串
     * <code>""</code>，否则返回字符串本身。
     * <p>
     * <pre>
     * StringUtil.defaultIfBlank(null)  = &quot;&quot;
     * StringUtil.defaultIfBlank(&quot;&quot;)    = &quot;&quot;
     * StringUtil.defaultIfBlank(&quot;  &quot;)  = &quot;&quot;
     * StringUtil.defaultIfBlank(&quot;bat&quot;) = &quot;bat&quot;
     * </pre>
     *
     * @param str 要转换的字符串
     * @return 字符串本身或空字符串<code>""</code>
     */
    public static String defaultIfBlank(String str) {
        return isBlank(str) ? EMPTY_STRING : str;
    }

    /**
     * 如果字符串是<code>null</code>或空字符串<code>""</code>，则返回指定默认字符串，否则返回字符串本身。
     * <p>
     * <pre>
     * StringUtil.defaultIfBlank(null, &quot;default&quot;)  = &quot;default&quot;
     * StringUtil.defaultIfBlank(&quot;&quot;, &quot;default&quot;)    = &quot;default&quot;
     * StringUtil.defaultIfBlank(&quot;  &quot;, &quot;default&quot;)  = &quot;default&quot;
     * StringUtil.defaultIfBlank(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     */
    public static String defaultIfBlank(String str, String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 去空白（或指定字符）的函数。 */
    /*                                                                              */
    /* 以下方法用来除去一个字串中的空白或指定字符。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 除去字符串头尾部的空白，如果字符串是<code>null</code>，依然返回<code>null</code>。
     * <p>
     * <p>
     * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code>
     * 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p>
     * <pre>
     * StringUtil.trim(null)          = null
     * StringUtil.trim(&quot;&quot;)            = &quot;&quot;
     * StringUtil.trim(&quot;     &quot;)       = &quot;&quot;
     * StringUtil.trim(&quot;abc&quot;)         = &quot;abc&quot;
     * StringUtil.trim(&quot;    abc    &quot;) = &quot;abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
     */
    public static String trim(String str) {
        return trim(str, null, 0);
    }

    public static String[] trim(String[] strs) {
        if (strs == null) {
            return null;
        }
        for (int i = 0; i < strs.length; i++) {
            strs[i] = trim(strs[i]);
        }
        return strs;
    }

    /**
     * trim集合
     *
     * @param stringList
     */
    public static void trim(List<String> stringList) {
        if (stringList == null) {
            return;
        }
        for (int i = 0; i < stringList.size(); i++) {
            if (stringList.get(i) == null) {
                continue;
            } else {
                stringList.set(i, stringList.get(i).trim());
            }
        }

    }

    /**
     * 除去字符串头尾部的指定字符，如果字符串是<code>null</code>，依然返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.trim(null, *)          = null
     * StringUtil.trim(&quot;&quot;, *)            = &quot;&quot;
     * StringUtil.trim(&quot;abc&quot;, null)      = &quot;abc&quot;
     * StringUtil.trim(&quot;  abc&quot;, null)    = &quot;abc&quot;
     * StringUtil.trim(&quot;abc  &quot;, null)    = &quot;abc&quot;
     * StringUtil.trim(&quot; abc &quot;, null)    = &quot;abc&quot;
     * StringUtil.trim(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
     * </pre>
     *
     * @param str        要处理的字符串
     * @param stripChars 要除去的字符，如果为<code>null</code>表示除去空白字符
     * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
     */
    public static String trim(String str, String stripChars) {
        return trim(str, stripChars, 0);
    }

    /**
     * 除去字符串头部的空白，如果字符串是<code>null</code>，则返回<code>null</code>。
     * <p>
     * <p>
     * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code>
     * 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p>
     * <pre>
     * StringUtil.trimStart(null)         = null
     * StringUtil.trimStart(&quot;&quot;)           = &quot;&quot;
     * StringUtil.trimStart(&quot;abc&quot;)        = &quot;abc&quot;
     * StringUtil.trimStart(&quot;  abc&quot;)      = &quot;abc&quot;
     * StringUtil.trimStart(&quot;abc  &quot;)      = &quot;abc  &quot;
     * StringUtil.trimStart(&quot; abc &quot;)      = &quot;abc &quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回
     * <code>null</code>
     */
    public static String trimStart(String str) {
        return trim(str, null, -1);
    }

    /**
     * 除去字符串头部的指定字符，如果字符串是<code>null</code>，依然返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.trimStart(null, *)          = null
     * StringUtil.trimStart(&quot;&quot;, *)            = &quot;&quot;
     * StringUtil.trimStart(&quot;abc&quot;, &quot;&quot;)        = &quot;abc&quot;
     * StringUtil.trimStart(&quot;abc&quot;, null)      = &quot;abc&quot;
     * StringUtil.trimStart(&quot;  abc&quot;, null)    = &quot;abc&quot;
     * StringUtil.trimStart(&quot;abc  &quot;, null)    = &quot;abc  &quot;
     * StringUtil.trimStart(&quot; abc &quot;, null)    = &quot;abc &quot;
     * StringUtil.trimStart(&quot;yxabc  &quot;, &quot;xyz&quot;) = &quot;abc  &quot;
     * </pre>
     *
     * @param str        要处理的字符串
     * @param stripChars 要除去的字符，如果为<code>null</code>表示除去空白字符
     * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
     */
    public static String trimStart(String str, String stripChars) {
        return trim(str, stripChars, -1);
    }

    /**
     * 除去字符串尾部的空白，如果字符串是<code>null</code>，则返回<code>null</code>。
     * <p>
     * <p>
     * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code>
     * 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p>
     * <pre>
     * StringUtil.trimEnd(null)       = null
     * StringUtil.trimEnd(&quot;&quot;)         = &quot;&quot;
     * StringUtil.trimEnd(&quot;abc&quot;)      = &quot;abc&quot;
     * StringUtil.trimEnd(&quot;  abc&quot;)    = &quot;  abc&quot;
     * StringUtil.trimEnd(&quot;abc  &quot;)    = &quot;abc&quot;
     * StringUtil.trimEnd(&quot; abc &quot;)    = &quot; abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回
     * <code>null</code>
     */
    public static String trimEnd(String str) {
        return trim(str, null, 1);
    }

    /**
     * 除去字符串尾部的指定字符，如果字符串是<code>null</code>，依然返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.trimEnd(null, *)          = null
     * StringUtil.trimEnd(&quot;&quot;, *)            = &quot;&quot;
     * StringUtil.trimEnd(&quot;abc&quot;, &quot;&quot;)        = &quot;abc&quot;
     * StringUtil.trimEnd(&quot;abc&quot;, null)      = &quot;abc&quot;
     * StringUtil.trimEnd(&quot;  abc&quot;, null)    = &quot;  abc&quot;
     * StringUtil.trimEnd(&quot;abc  &quot;, null)    = &quot;abc&quot;
     * StringUtil.trimEnd(&quot; abc &quot;, null)    = &quot; abc&quot;
     * StringUtil.trimEnd(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
     * </pre>
     *
     * @param str        要处理的字符串
     * @param stripChars 要除去的字符，如果为<code>null</code>表示除去空白字符
     * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
     */
    public static String trimEnd(String str, String stripChars) {
        return trim(str, stripChars, 1);
    }

    /**
     * 除去字符串头尾部的空白，如果结果字符串是空字符串<code>""</code>，则返回<code>null</code>。
     * <p>
     * <p>
     * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code>
     * 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p>
     * <pre>
     * StringUtil.trimToNull(null)          = null
     * StringUtil.trimToNull(&quot;&quot;)            = null
     * StringUtil.trimToNull(&quot;     &quot;)       = null
     * StringUtil.trimToNull(&quot;abc&quot;)         = &quot;abc&quot;
     * StringUtil.trimToNull(&quot;    abc    &quot;) = &quot;abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回
     * <code>null</code>
     */
    public static String trimToNull(String str) {
        return trimToNull(str, null);
    }

    /**
     * 除去字符串头尾部的空白，如果结果字符串是空字符串<code>""</code>，则返回<code>null</code>。
     * <p>
     * <p>
     * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code>
     * 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p>
     * <pre>
     * StringUtil.trim(null, *)          = null
     * StringUtil.trim(&quot;&quot;, *)            = null
     * StringUtil.trim(&quot;abc&quot;, null)      = &quot;abc&quot;
     * StringUtil.trim(&quot;  abc&quot;, null)    = &quot;abc&quot;
     * StringUtil.trim(&quot;abc  &quot;, null)    = &quot;abc&quot;
     * StringUtil.trim(&quot; abc &quot;, null)    = &quot;abc&quot;
     * StringUtil.trim(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str        要处理的字符串
     * @param stripChars 要除去的字符，如果为<code>null</code>表示除去空白字符
     * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回
     * <code>null</code>
     */
    public static String trimToNull(String str, String stripChars) {
        String result = trim(str, stripChars);

        if ((result == null) || (result.length() == 0)) {
            return null;
        }

        return result;
    }

    /**
     * 除去字符串头尾部的空白，如果字符串是<code>null</code>，则返回空字符串<code>""</code>。
     * <p>
     * <p>
     * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code>
     * 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p>
     * <pre>
     * StringUtil.trimToEmpty(null)          = &quot;&quot;
     * StringUtil.trimToEmpty(&quot;&quot;)            = &quot;&quot;
     * StringUtil.trimToEmpty(&quot;     &quot;)       = &quot;&quot;
     * StringUtil.trimToEmpty(&quot;abc&quot;)         = &quot;abc&quot;
     * StringUtil.trimToEmpty(&quot;    abc    &quot;) = &quot;abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回
     * <code>null</code>
     */
    public static String trimToEmpty(String str) {
        return trimToEmpty(str, null);
    }

    /**
     * 除去字符串头尾部的空白，如果字符串是<code>null</code>，则返回空字符串<code>""</code>。
     * <p>
     * <p>
     * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code>
     * 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p>
     * <pre>
     * StringUtil.trim(null, *)          = &quot;&quot;
     * StringUtil.trim(&quot;&quot;, *)            = &quot;&quot;
     * StringUtil.trim(&quot;abc&quot;, null)      = &quot;abc&quot;
     * StringUtil.trim(&quot;  abc&quot;, null)    = &quot;abc&quot;
     * StringUtil.trim(&quot;abc  &quot;, null)    = &quot;abc&quot;
     * StringUtil.trim(&quot; abc &quot;, null)    = &quot;abc&quot;
     * StringUtil.trim(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回
     * <code>null</code>
     */
    public static String trimToEmpty(String str, String stripChars) {
        String result = trim(str, stripChars);

        if (result == null) {
            return EMPTY_STRING;
        }

        return result;
    }

    /**
     * 除去字符串头尾部的指定字符，如果字符串是<code>null</code>，依然返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.trim(null, *)          = null
     * StringUtil.trim(&quot;&quot;, *)            = &quot;&quot;
     * StringUtil.trim(&quot;abc&quot;, null)      = &quot;abc&quot;
     * StringUtil.trim(&quot;  abc&quot;, null)    = &quot;abc&quot;
     * StringUtil.trim(&quot;abc  &quot;, null)    = &quot;abc&quot;
     * StringUtil.trim(&quot; abc &quot;, null)    = &quot;abc&quot;
     * StringUtil.trim(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
     * </pre>
     *
     * @param str        要处理的字符串
     * @param stripChars 要除去的字符，如果为<code>null</code>表示除去空白字符
     * @param mode       <code>-1</code>表示trimStart，<code>0</code>表示trim全部，
     *                   <code>1</code>表示trimEnd
     * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
     */
    private static String trim(String str, String stripChars, int mode) {
        if (str == null) {
            return null;
        }

        int length = str.length();
        int start = 0;
        int end = length;

        // 扫描字符串头部
        if (mode <= 0) {
            if (stripChars == null) {
                while ((start < end)
                        && (str.charAt(start) == (char) 160 || Character
                        .isWhitespace(str.charAt(start)))) {
                    start++;
                }
            } else if (stripChars.length() == 0) {
                return str;
            } else {
                while ((start < end)
                        && (stripChars.indexOf(str.charAt(start)) != -1)) {
                    start++;
                }
            }
        }

        // 扫描字符串尾部
        if (mode >= 0) {
            if (stripChars == null) {
                while ((start < end)
                        && (Character.isWhitespace(str.charAt(end - 1)))) {
                    end--;
                }
            } else if (stripChars.length() == 0) {
                return str;
            } else {
                while ((start < end)
                        && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
                    end--;
                }
            }
        }

        if ((start > 0) || (end < length)) {
            return str.substring(start, end);
        }

        return str;
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 比较函数。 */
    /*                                                                              */
    /* 以下方法用来比较两个字符串是否相同。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 比较两个字符串（大小写敏感）。
     * <p>
     * <pre>
     * StringUtil.equals(null, null)   = true
     * StringUtil.equals(null, &quot;abc&quot;)  = false
     * StringUtil.equals(&quot;abc&quot;, null)  = false
     * StringUtil.equals(&quot;abc&quot;, &quot;abc&quot;) = true
     * StringUtil.equals(&quot;abc&quot;, &quot;ABC&quot;) = false
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equals(str2);
    }

    /**
     * 空或null相等
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equalsNullEmpty(String str1, String str2) {
        return StringUtils.equals(StringUtils.defaultIfNull(str1),
                StringUtils.defaultIfNull(str2));
    }

    public static boolean notEquals(String str1, String str2) {
        return !equals(str1, str2);
    }

    /**
     * 与数组str1中的任何一个相等
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equalsAny(String str2, String[] str1) {
        if (ArrayUtils.getLength(str1) <= 0)
            return false;
        for (String value : str1) {
            if (StringUtils.equals(str2, value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 与数组str1中的任何一个都不相等
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean notEqualsAny(String str2, String[] str1) {
        if (ArrayUtils.getLength(str1) <= 0)
            return false;
        for (String value : str1) {
            if (StringUtils.equals(str2, value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比较两个字符串（大小写敏感）。忽略null
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equalsIgnoreNull(String str1, String str2) {
        return StringUtils.equals(StringUtils.defaultIfNull(str1),
                StringUtils.defaultIfNull(str2));
    }

    /**
     * 比较两个字符串（大小写不敏感）。
     * <p>
     * <pre>
     * StringUtil.equalsIgnoreCase(null, null)   = true
     * StringUtil.equalsIgnoreCase(null, &quot;abc&quot;)  = false
     * StringUtil.equalsIgnoreCase(&quot;abc&quot;, null)  = false
     * StringUtil.equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
     * StringUtil.equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equalsIgnoreCase(str2);
    }

    public static boolean notEqualsIgnoreCase(String str1, String str2) {
        return !equalsIgnoreCase(str1, str2);
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 字符串类型判定函数。 */
    /*                                                                              */
    /* 判定字符串的类型是否为：字母、数字、空白等 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 判断字符串是否只包含unicode字母。
     * <p>
     * <p>
     * <code>null</code>将返回<code>false</code>，空字符串<code>""</code>将返回
     * <code>true</code>。
     * </p>
     * <p>
     * <pre>
     * StringUtil.isAlpha(null)   = false
     * StringUtil.isAlpha(&quot;&quot;)     = true
     * StringUtil.isAlpha(&quot;  &quot;)   = false
     * StringUtil.isAlpha(&quot;abc&quot;)  = true
     * StringUtil.isAlpha(&quot;ab2c&quot;) = false
     * StringUtil.isAlpha(&quot;ab-c&quot;) = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果字符串非<code>null</code>并且全由unicode字母组成，则返回<code>true</code>
     */
    public static boolean isAlpha(String str) {
        if (str == null) {
            return false;
        }

        int length = str.length();

        for (int i = 0; i < length; i++) {
            if (!Character.isLetter(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符串是否只包含unicode字母和空格<code>' '</code>。
     * <p>
     * <p>
     * <code>null</code>将返回<code>false</code>，空字符串<code>""</code>将返回
     * <code>true</code>。
     * </p>
     * <p>
     * <pre>
     * StringUtil.isAlphaSpace(null)   = false
     * StringUtil.isAlphaSpace(&quot;&quot;)     = true
     * StringUtil.isAlphaSpace(&quot;  &quot;)   = true
     * StringUtil.isAlphaSpace(&quot;abc&quot;)  = true
     * StringUtil.isAlphaSpace(&quot;ab c&quot;) = true
     * StringUtil.isAlphaSpace(&quot;ab2c&quot;) = false
     * StringUtil.isAlphaSpace(&quot;ab-c&quot;) = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果字符串非<code>null</code>并且全由unicode字母和空格组成，则返回<code>true</code>
     */
    public static boolean isAlphaSpace(String str) {
        if (str == null) {
            return false;
        }

        int length = str.length();

        for (int i = 0; i < length; i++) {
            if (!Character.isLetter(str.charAt(i)) && (str.charAt(i) != ' ')) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符串是否只包含unicode字母和数字。
     * <p>
     * <p>
     * <code>null</code>将返回<code>false</code>，空字符串<code>""</code>将返回
     * <code>true</code>。
     * </p>
     * <p>
     * <pre>
     * StringUtil.isAlphanumeric(null)   = false
     * StringUtil.isAlphanumeric(&quot;&quot;)     = true
     * StringUtil.isAlphanumeric(&quot;  &quot;)   = false
     * StringUtil.isAlphanumeric(&quot;abc&quot;)  = true
     * StringUtil.isAlphanumeric(&quot;ab c&quot;) = false
     * StringUtil.isAlphanumeric(&quot;ab2c&quot;) = true
     * StringUtil.isAlphanumeric(&quot;ab-c&quot;) = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果字符串非<code>null</code>并且全由unicode字母数字组成，则返回<code>true</code>
     */
    public static boolean isAlphanumeric(String str) {
        if (str == null) {
            return false;
        }

        int length = str.length();

        for (int i = 0; i < length; i++) {
            if (!Character.isLetterOrDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符串是否只包含unicode字母数字和空格<code>' '</code>。
     * <p>
     * <p>
     * <code>null</code>将返回<code>false</code>，空字符串<code>""</code>将返回
     * <code>true</code>。
     * </p>
     * <p>
     * <pre>
     * StringUtil.isAlphanumericSpace(null)   = false
     * StringUtil.isAlphanumericSpace(&quot;&quot;)     = true
     * StringUtil.isAlphanumericSpace(&quot;  &quot;)   = true
     * StringUtil.isAlphanumericSpace(&quot;abc&quot;)  = true
     * StringUtil.isAlphanumericSpace(&quot;ab c&quot;) = true
     * StringUtil.isAlphanumericSpace(&quot;ab2c&quot;) = true
     * StringUtil.isAlphanumericSpace(&quot;ab-c&quot;) = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果字符串非<code>null</code>并且全由unicode字母数字和空格组成，则返回<code>true</code>
     */
    public static boolean isAlphanumericSpace(String str) {
        if (str == null) {
            return false;
        }

        int length = str.length();

        for (int i = 0; i < length; i++) {
            if (!Character.isLetterOrDigit(str.charAt(i))
                    && (str.charAt(i) != ' ')) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符串是否只包含unicode数字。
     * <p>
     * <p>
     * <code>null</code>将返回<code>false</code>，空字符串<code>""</code>将返回
     * <code>true</code>。
     * </p>
     * <p>
     * <pre>
     * StringUtil.isNumeric(null)   = false
     * StringUtil.isNumeric(&quot;&quot;)     = true
     * StringUtil.isNumeric(&quot;  &quot;)   = false
     * StringUtil.isNumeric(&quot;123&quot;)  = true
     * StringUtil.isNumeric(&quot;12 3&quot;) = false
     * StringUtil.isNumeric(&quot;ab2c&quot;) = false
     * StringUtil.isNumeric(&quot;12-3&quot;) = false
     * StringUtil.isNumeric(&quot;12.3&quot;) = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果字符串非<code>null</code>并且全由unicode数字组成，则返回<code>true</code>
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }

        int length = str.length();

        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符串是否只包含unicode数字和空格<code>' '</code>。
     * <p>
     * <p>
     * <code>null</code>将返回<code>false</code>，空字符串<code>""</code>将返回
     * <code>true</code>。
     * </p>
     * <p>
     * <pre>
     * StringUtil.isNumericSpace(null)   = false
     * StringUtil.isNumericSpace(&quot;&quot;)     = true
     * StringUtil.isNumericSpace(&quot;  &quot;)   = true
     * StringUtil.isNumericSpace(&quot;123&quot;)  = true
     * StringUtil.isNumericSpace(&quot;12 3&quot;) = true
     * StringUtil.isNumericSpace(&quot;ab2c&quot;) = false
     * StringUtil.isNumericSpace(&quot;12-3&quot;) = false
     * StringUtil.isNumericSpace(&quot;12.3&quot;) = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果字符串非<code>null</code>并且全由unicode数字和空格组成，则返回<code>true</code>
     */
    public static boolean isNumericSpace(String str) {
        if (str == null) {
            return false;
        }

        int length = str.length();

        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i)) && (str.charAt(i) != ' ')) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符串是否只包含unicode空白。
     * <p>
     * <p>
     * <code>null</code>将返回<code>false</code>，空字符串<code>""</code>将返回
     * <code>true</code>。
     * </p>
     * <p>
     * <pre>
     * StringUtil.isWhitespace(null)   = false
     * StringUtil.isWhitespace(&quot;&quot;)     = true
     * StringUtil.isWhitespace(&quot;  &quot;)   = true
     * StringUtil.isWhitespace(&quot;abc&quot;)  = false
     * StringUtil.isWhitespace(&quot;ab2c&quot;) = false
     * StringUtil.isWhitespace(&quot;ab-c&quot;) = false
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果字符串非<code>null</code>并且全由unicode空白组成，则返回<code>true</code>
     */
    public static boolean isWhitespace(String str) {
        if (str == null) {
            return false;
        }

        int length = str.length();

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 大小写转换。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 将字符串转换成大写。
     * <p>
     * <p>
     * 如果字符串是<code>null</code>则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.toUpperCase(null)  = null
     * StringUtil.toUpperCase(&quot;&quot;)    = &quot;&quot;
     * StringUtil.toUpperCase(&quot;aBc&quot;) = &quot;ABC&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要转换的字符串
     * @return 大写字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String toUpperCase(String str) {
        return toUpperCase(str, -1);
    }

    /**
     * 前index个字母变成大写
     *
     * @param str
     * @param index
     * @return
     */
    public static String toUpperCase(String str, int index) {
        if (str == null) {
            return null;
        }
        if (index < 0 || str.length() <= index) {
            return str.toUpperCase();
        }

        return str.substring(0, index + 1).toUpperCase()
                + str.substring(index + 1);

    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String toFirstCharUpperCase(String str) {
        return toUpperCase(str, 0);
    }

    /**
     * 将字符串转换成小写。
     * <p>
     * <p>
     * 如果字符串是<code>null</code>则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.toLowerCase(null)  = null
     * StringUtil.toLowerCase(&quot;&quot;)    = &quot;&quot;
     * StringUtil.toLowerCase(&quot;aBc&quot;) = &quot;abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要转换的字符串
     * @return 大写字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String toLowerCase(String str) {
        if (str == null) {
            return null;
        }

        return str.toLowerCase();
    }

    /**
     * 将字符串的首字符转成大写（<code>Character.toTitleCase</code>），其它字符不变。
     * <p>
     * <p>
     * 如果字符串是<code>null</code>则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.capitalize(null)  = null
     * StringUtil.capitalize(&quot;&quot;)    = &quot;&quot;
     * StringUtil.capitalize(&quot;cat&quot;) = &quot;Cat&quot;
     * StringUtil.capitalize(&quot;cAt&quot;) = &quot;CAt&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要转换的字符串
     * @return 首字符为大写的字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String capitalize(String str) {
        int strLen;

        if ((str == null) || ((strLen = str.length()) == 0)) {
            return str;
        }

        return new StringBuffer(strLen)
                .append(Character.toTitleCase(str.charAt(0)))
                .append(str.substring(1)).toString();
    }

    /**
     * 将字符串的首字符转成小写，其它字符不变。
     * <p>
     * <p>
     * 如果字符串是<code>null</code>则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.uncapitalize(null)  = null
     * StringUtil.uncapitalize(&quot;&quot;)    = &quot;&quot;
     * StringUtil.uncapitalize(&quot;Cat&quot;) = &quot;cat&quot;
     * StringUtil.uncapitalize(&quot;CAT&quot;) = &quot;cAT&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要转换的字符串
     * @return 首字符为小写的字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String uncapitalize(String str) {
        int strLen;

        if ((str == null) || ((strLen = str.length()) == 0)) {
            return str;
        }

        return new StringBuffer(strLen)
                .append(Character.toLowerCase(str.charAt(0)))
                .append(str.substring(1)).toString();
    }

    /**
     * 反转字符串的大小写。
     * <p>
     * <p>
     * 如果字符串是<code>null</code>则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.swapCase(null)                 = null
     * StringUtil.swapCase(&quot;&quot;)                   = &quot;&quot;
     * StringUtil.swapCase(&quot;The dog has a BONE&quot;) = &quot;tHE DOG HAS A bone&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要转换的字符串
     * @return 大小写被反转的字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String swapCase(String str) {
        int strLen;

        if ((str == null) || ((strLen = str.length()) == 0)) {
            return str;
        }

        StringBuffer buffer = new StringBuffer(strLen);

        char ch = 0;

        for (int i = 0; i < strLen; i++) {
            ch = str.charAt(i);

            if (Character.isUpperCase(ch)) {
                ch = Character.toLowerCase(ch);
            } else if (Character.isTitleCase(ch)) {
                ch = Character.toLowerCase(ch);
            } else if (Character.isLowerCase(ch)) {
                ch = Character.toUpperCase(ch);
            }

            buffer.append(ch);
        }

        return buffer.toString();
    }

    /**
     * 将字符串转换成camel case。
     * <p>
     * <p>
     * 如果字符串是<code>null</code>则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.toCamelCase(null)  = null
     * StringUtil.toCamelCase(&quot;&quot;)    = &quot;&quot;
     * StringUtil.toCamelCase(&quot;aBc&quot;) = &quot;aBc&quot;
     * StringUtil.toCamelCase(&quot;aBc def&quot;) = &quot;aBcDef&quot;
     * StringUtil.toCamelCase(&quot;aBc def_ghi&quot;) = &quot;aBcDefGhi&quot;
     * StringUtil.toCamelCase(&quot;aBc def_ghi 123&quot;) = &quot;aBcDefGhi123&quot;
     * </pre>
     * <p>
     * </p>
     * <p>
     * <p>
     * 此方法会保留除了下划线和空白以外的所有分隔符。
     * </p>
     *
     * @param str 要转换的字符串
     * @return camel case字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String toCamelCase(String str) {
        return CAMEL_CASE_TOKENIZER.parse(str);
    }

    /**
     * 将字符串转换成pascal case。
     * <p>
     * <p>
     * 如果字符串是<code>null</code>则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.toPascalCase(null)  = null
     * StringUtil.toPascalCase(&quot;&quot;)    = &quot;&quot;
     * StringUtil.toPascalCase(&quot;aBc&quot;) = &quot;ABc&quot;
     * StringUtil.toPascalCase(&quot;aBc def&quot;) = &quot;ABcDef&quot;
     * StringUtil.toPascalCase(&quot;aBc def_ghi&quot;) = &quot;ABcDefGhi&quot;
     * StringUtil.toPascalCase(&quot;aBc def_ghi 123&quot;) = &quot;aBcDefGhi123&quot;
     * </pre>
     * <p>
     * </p>
     * <p>
     * <p>
     * 此方法会保留除了下划线和空白以外的所有分隔符。
     * </p>
     *
     * @param str 要转换的字符串
     * @return pascal case字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String toPascalCase(String str) {
        return PASCAL_CASE_TOKENIZER.parse(str);
    }

    /**
     * 将字符串转换成下划线分隔的大写字符串。
     * <p>
     * <p>
     * 如果字符串是<code>null</code>则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.toUpperCaseWithUnderscores(null)  = null
     * StringUtil.toUpperCaseWithUnderscores(&quot;&quot;)    = &quot;&quot;
     * StringUtil.toUpperCaseWithUnderscores(&quot;aBc&quot;) = &quot;A_BC&quot;
     * StringUtil.toUpperCaseWithUnderscores(&quot;aBc def&quot;) = &quot;A_BC_DEF&quot;
     * StringUtil.toUpperCaseWithUnderscores(&quot;aBc def_ghi&quot;) = &quot;A_BC_DEF_GHI&quot;
     * StringUtil.toUpperCaseWithUnderscores(&quot;aBc def_ghi 123&quot;) = &quot;A_BC_DEF_GHI_123&quot;
     * StringUtil.toUpperCaseWithUnderscores(&quot;__a__Bc__&quot;) = &quot;__A__BC__&quot;
     * </pre>
     * <p>
     * </p>
     * <p>
     * <p>
     * 此方法会保留除了空白以外的所有分隔符。
     * </p>
     *
     * @param str 要转换的字符串
     * @return 下划线分隔的大写字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String toUpperCaseWithUnderscores(String str) {
        return UPPER_CASE_WITH_UNDERSCORES_TOKENIZER.parse(str);
    }

    /**
     * 将字符串转换成下划线分隔的小写字符串。
     * <p>
     * <p>
     * 如果字符串是<code>null</code>则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.toLowerCaseWithUnderscores(null)  = null
     * StringUtil.toLowerCaseWithUnderscores(&quot;&quot;)    = &quot;&quot;
     * StringUtil.toLowerCaseWithUnderscores(&quot;aBc&quot;) = &quot;a_bc&quot;
     * StringUtil.toLowerCaseWithUnderscores(&quot;aBc def&quot;) = &quot;a_bc_def&quot;
     * StringUtil.toLowerCaseWithUnderscores(&quot;aBc def_ghi&quot;) = &quot;a_bc_def_ghi&quot;
     * StringUtil.toLowerCaseWithUnderscores(&quot;aBc def_ghi 123&quot;) = &quot;a_bc_def_ghi_123&quot;
     * StringUtil.toLowerCaseWithUnderscores(&quot;__a__Bc__&quot;) = &quot;__a__bc__&quot;
     * </pre>
     * <p>
     * </p>
     * <p>
     * <p>
     * 此方法会保留除了空白以外的所有分隔符。
     * </p>
     *
     * @param str 要转换的字符串
     * @return 下划线分隔的小写字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String toLowerCaseWithUnderscores(String str) {
        return LOWER_CASE_WITH_UNDERSCORES_TOKENIZER.parse(str);
    }

    /**
     * 解析单词的解析器。
     */
    private static final WordTokenizer CAMEL_CASE_TOKENIZER = new WordTokenizer() {
        protected void startSentence(StringBuffer buffer, char ch) {
            buffer.append(Character.toLowerCase(ch));
        }

        protected void startWord(StringBuffer buffer, char ch) {
            if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                buffer.append(Character.toUpperCase(ch));
            } else {
                buffer.append(Character.toLowerCase(ch));
            }
        }

        protected void inWord(StringBuffer buffer, char ch) {
            buffer.append(Character.toLowerCase(ch));
        }

        protected void startDigitSentence(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        protected void startDigitWord(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        protected void inDigitWord(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        protected void inDelimiter(StringBuffer buffer, char ch) {
            if (ch != UNDERSCORE) {
                buffer.append(ch);
            }
        }
    };

    private static final WordTokenizer PASCAL_CASE_TOKENIZER = new WordTokenizer() {
        protected void startSentence(StringBuffer buffer, char ch) {
            buffer.append(Character.toUpperCase(ch));
        }

        protected void startWord(StringBuffer buffer, char ch) {
            buffer.append(Character.toUpperCase(ch));
        }

        protected void inWord(StringBuffer buffer, char ch) {
            buffer.append(Character.toLowerCase(ch));
        }

        protected void startDigitSentence(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        protected void startDigitWord(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        protected void inDigitWord(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        protected void inDelimiter(StringBuffer buffer, char ch) {
            if (ch != UNDERSCORE) {
                buffer.append(ch);
            }
        }
    };

    private static final WordTokenizer UPPER_CASE_WITH_UNDERSCORES_TOKENIZER = new WordTokenizer() {
        protected void startSentence(StringBuffer buffer, char ch) {
            buffer.append(Character.toUpperCase(ch));
        }

        protected void startWord(StringBuffer buffer, char ch) {
            if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                buffer.append(UNDERSCORE);
            }

            buffer.append(Character.toUpperCase(ch));
        }

        protected void inWord(StringBuffer buffer, char ch) {
            buffer.append(Character.toUpperCase(ch));
        }

        protected void startDigitSentence(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        protected void startDigitWord(StringBuffer buffer, char ch) {
            if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                buffer.append(UNDERSCORE);
            }

            buffer.append(ch);
        }

        protected void inDigitWord(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        protected void inDelimiter(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }
    };

    private static final WordTokenizer LOWER_CASE_WITH_UNDERSCORES_TOKENIZER = new WordTokenizer() {
        protected void startSentence(StringBuffer buffer, char ch) {
            buffer.append(Character.toLowerCase(ch));
        }

        protected void startWord(StringBuffer buffer, char ch) {
            if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                buffer.append(UNDERSCORE);
            }

            buffer.append(Character.toLowerCase(ch));
        }

        protected void inWord(StringBuffer buffer, char ch) {
            buffer.append(Character.toLowerCase(ch));
        }

        protected void startDigitSentence(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        protected void startDigitWord(StringBuffer buffer, char ch) {
            if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                buffer.append(UNDERSCORE);
            }

            buffer.append(ch);
        }

        protected void inDigitWord(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }

        protected void inDelimiter(StringBuffer buffer, char ch) {
            buffer.append(ch);
        }
    };

    /**
     * 解析出下列语法所构成的<code>SENTENCE</code>。
     * <p>
     * <pre>
     *  SENTENCE = WORD (DELIMITER* WORD)*
     *
     *  WORD = UPPER_CASE_WORD | LOWER_CASE_WORD | TITLE_CASE_WORD | DIGIT_WORD
     *
     *  UPPER_CASE_WORD = UPPER_CASE_LETTER+
     *  LOWER_CASE_WORD = LOWER_CASE_LETTER+
     *  TITLE_CASE_WORD = UPPER_CASE_LETTER LOWER_CASE_LETTER+
     *  DIGIT_WORD      = DIGIT+
     *
     *  UPPER_CASE_LETTER = Character.isUpperCase()
     *  LOWER_CASE_LETTER = Character.isLowerCase()
     *  DIGIT             = Character.isDigit()
     *  NON_LETTER_DIGIT  = !Character.isUpperCase() &amp;&amp; !Character.isLowerCase() &amp;&amp; !Character.isDigit()
     *
     *  DELIMITER = WHITESPACE | NON_LETTER_DIGIT
     * </pre>
     */
    private abstract static class WordTokenizer {
        protected static final char UNDERSCORE = '_';

        /**
         * Parse sentence。
         */
        public String parse(String str) {
            if (isEmpty(str)) {
                return str;
            }

            int length = str.length();
            StringBuffer buffer = new StringBuffer(length);

            for (int index = 0; index < length; index++) {
                char ch = str.charAt(index);

                // 忽略空白。
                if (Character.isWhitespace(ch)) {
                    continue;
                }

                // 大写字母开始：UpperCaseWord或是TitleCaseWord。
                if (Character.isUpperCase(ch)) {
                    int wordIndex = index + 1;

                    while (wordIndex < length) {
                        char wordChar = str.charAt(wordIndex);

                        if (Character.isUpperCase(wordChar)) {
                            wordIndex++;
                        } else if (Character.isLowerCase(wordChar)) {
                            wordIndex--;
                            break;
                        } else {
                            break;
                        }
                    }

                    // 1. wordIndex == length，说明最后一个字母为大写，以upperCaseWord处理之。
                    // 2. wordIndex == index，说明index处为一个titleCaseWord。
                    // 3. wordIndex > index，说明index到wordIndex -
                    // 1处全部是大写，以upperCaseWord处理。
                    if ((wordIndex == length) || (wordIndex > index)) {
                        index = parseUpperCaseWord(buffer, str, index,
                                wordIndex);
                    } else {
                        index = parseTitleCaseWord(buffer, str, index);
                    }

                    continue;
                }

                // 小写字母开始：LowerCaseWord。
                if (Character.isLowerCase(ch)) {
                    index = parseLowerCaseWord(buffer, str, index);
                    continue;
                }

                // 数字开始：DigitWord。
                if (Character.isDigit(ch)) {
                    index = parseDigitWord(buffer, str, index);
                    continue;
                }

                // 非字母数字开始：Delimiter。
                inDelimiter(buffer, ch);
            }

            return buffer.toString();
        }

        private int parseUpperCaseWord(StringBuffer buffer, String str,
                                       int index, int length) {
            char ch = str.charAt(index++);

            // 首字母，必然存在且为大写。
            if (buffer.length() == 0) {
                startSentence(buffer, ch);
            } else {
                startWord(buffer, ch);
            }

            // 后续字母，必为小写。
            for (; index < length; index++) {
                ch = str.charAt(index);
                inWord(buffer, ch);
            }

            return index - 1;
        }

        private int parseLowerCaseWord(StringBuffer buffer, String str,
                                       int index) {
            char ch = str.charAt(index++);

            // 首字母，必然存在且为小写。
            if (buffer.length() == 0) {
                startSentence(buffer, ch);
            } else {
                startWord(buffer, ch);
            }

            // 后续字母，必为小写。
            int length = str.length();

            for (; index < length; index++) {
                ch = str.charAt(index);

                if (Character.isLowerCase(ch)) {
                    inWord(buffer, ch);
                } else {
                    break;
                }
            }

            return index - 1;
        }

        private int parseTitleCaseWord(StringBuffer buffer, String str,
                                       int index) {
            char ch = str.charAt(index++);

            // 首字母，必然存在且为大写。
            if (buffer.length() == 0) {
                startSentence(buffer, ch);
            } else {
                startWord(buffer, ch);
            }

            // 后续字母，必为小写。
            int length = str.length();

            for (; index < length; index++) {
                ch = str.charAt(index);

                if (Character.isLowerCase(ch)) {
                    inWord(buffer, ch);
                } else {
                    break;
                }
            }

            return index - 1;
        }

        private int parseDigitWord(StringBuffer buffer, String str, int index) {
            char ch = str.charAt(index++);

            // 首字符，必然存在且为数字。
            if (buffer.length() == 0) {
                startDigitSentence(buffer, ch);
            } else {
                startDigitWord(buffer, ch);
            }

            // 后续字符，必为数字。
            int length = str.length();

            for (; index < length; index++) {
                ch = str.charAt(index);

                if (Character.isDigit(ch)) {
                    inDigitWord(buffer, ch);
                } else {
                    break;
                }
            }

            return index - 1;
        }

        protected boolean isDelimiter(char ch) {
            return !Character.isUpperCase(ch) && !Character.isLowerCase(ch)
                    && !Character.isDigit(ch);
        }

        protected abstract void startSentence(StringBuffer buffer, char ch);

        protected abstract void startWord(StringBuffer buffer, char ch);

        protected abstract void inWord(StringBuffer buffer, char ch);

        protected abstract void startDigitSentence(StringBuffer buffer, char ch);

        protected abstract void startDigitWord(StringBuffer buffer, char ch);

        protected abstract void inDigitWord(StringBuffer buffer, char ch);

        protected abstract void inDelimiter(StringBuffer buffer, char ch);
    }

    /**
     * 将str的每一个字符转换成string,返回数组
     *
     * @param str
     * @return
     */
    public static String[] splitEachChar(String str) {
        if (isEmpty(str)) {
            return null;
        }
        List<String> splitList = new ArrayList<>(str.length());
        for (char c : str.toCharArray()) {
            splitList.add(new String(new char[]{c}));
        }
        return splitList.toArray(new String[splitList.size()]);
    }

    /**
     * @param str
     * @return
     */
    public static String[] splitEachChar(int integerValue) {
        return splitEachChar(Integer.toString(integerValue));
    }

    /**
     * @param str
     * @return
     */
    public static String[] splitEachChar(long longValue) {
        return splitEachChar(Long.toString(longValue));
    }

    /**
     * @param str
     * @return
     */
    public static String[] splitEachChar(double doubleValue) {
        return splitEachChar(Double.toString(doubleValue));
    }

    /**
     * @param str
     * @return
     */
    public static String[] splitEachChar(float floatValue) {
        return splitEachChar(Float.toString(floatValue));
    }

    /**
     * 将字符串按指定字符分割。
     * <p>
     * <p>
     * 分隔符不会出现在目标数组中，连续的分隔符就被看作一个。如果字符串为<code>null</code>，则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.split(null, *)         = null
     * StringUtil.split(&quot;&quot;, *)           = []
     * StringUtil.split(&quot;a.b.c&quot;, '.')    = [&quot;a&quot;, &quot;b&quot;, &quot;c&quot;]
     * StringUtil.split(&quot;a..b.c&quot;, '.')   = [&quot;a&quot;, &quot;b&quot;, &quot;c&quot;]
     * StringUtil.split(&quot;a:b:c&quot;, '.')    = [&quot;a:b:c&quot;]
     * StringUtil.split(&quot;a b c&quot;, ' ')    = [&quot;a&quot;, &quot;b&quot;, &quot;c&quot;]
     * </pre>
     * <p>
     * </p>
     *
     * @param str           要分割的字符串
     * @param separatorChar 分隔符
     * @return 分割后的字符串数组，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String[] split(String str, char separatorChar) {
        if (str == null) {
            return null;
        }

        int length = str.length();

        if (length == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        List<String> list = new ArrayList<>();
        int i = 0;
        int start = 0;
        boolean match = false;

        while (i < length) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }

                start = ++i;
                continue;
            }

            match = true;
            i++;
        }

        if (match) {
            list.add(str.substring(start, i));
        }

        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * 将字符串按指定字符分割。
     * <p>
     * <p>
     * 分隔符不会出现在目标数组中，连续的分隔符就被看作一个。如果字符串为<code>null</code>，则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.split(null, *)                = null
     * StringUtil.split(&quot;&quot;, *)                  = []
     * StringUtil.split(&quot;abc def&quot;, null)        = [&quot;abc&quot;, &quot;def&quot;]
     * StringUtil.split(&quot;abc def&quot;, &quot; &quot;)         = [&quot;abc&quot;, &quot;def&quot;]
     * StringUtil.split(&quot;abc  def&quot;, &quot; &quot;)        = [&quot;abc&quot;, &quot;def&quot;]
     * StringUtil.split(&quot; ab:  cd::ef  &quot;, &quot;:&quot;)  = [&quot;ab&quot;, &quot;cd&quot;, &quot;ef&quot;]
     * StringUtil.split(&quot;abc.def&quot;, &quot;&quot;)          = [&quot;abc.def&quot;]
     * </pre>
     * <p>
     * </p>
     *
     * @param str            要分割的字符串
     * @param separatorChars 分隔符
     * @return 分割后的字符串数组，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String[] split(String str, String separatorChars) {
        return split(str, separatorChars, -1);
    }

    /**
     * 将字符串按指定字符分割。
     * <p>
     * <p>
     * 分隔符不会出现在目标数组中，连续的分隔符就被看作一个。如果字符串为<code>null</code>，则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.split(null, *, *)                 = null
     * StringUtil.split(&quot;&quot;, *, *)                   = []
     * StringUtil.split(&quot;ab cd ef&quot;, null, 0)        = [&quot;ab&quot;, &quot;cd&quot;, &quot;ef&quot;]
     * StringUtil.split(&quot;  ab   cd ef  &quot;, null, 0)  = [&quot;ab&quot;, &quot;cd&quot;, &quot;ef&quot;]
     * StringUtil.split(&quot;ab:cd::ef&quot;, &quot;:&quot;, 0)        = [&quot;ab&quot;, &quot;cd&quot;, &quot;ef&quot;]
     * StringUtil.split(&quot;ab:cd:ef&quot;, &quot;:&quot;, 2)         = [&quot;ab&quot;, &quot;cdef&quot;]
     * StringUtil.split(&quot;abc.def&quot;, &quot;&quot;, 2)           = [&quot;abc.def&quot;]
     * </pre>
     * <p>
     * </p>
     *
     * @param str            要分割的字符串
     * @param separatorChars 分隔符
     * @param max            返回的数组的最大个数，如果小于等于0，则表示无限制
     * @return 分割后的字符串数组，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String[] split(String str, String separatorChars, int max) {
        if (str == null) {
            return null;
        }

        int length = str.length();

        if (length == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        List<String> list = new ArrayList<>();
        int sizePlus1 = 1;
        int i = 0;
        int start = 0;
        boolean match = false;

        if (separatorChars == null) {
            // null表示使用空白作为分隔符
            while (i < length) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = length;
                        }

                        list.add(str.substring(start, i));
                        match = false;
                    }

                    start = ++i;
                    continue;
                }

                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // 优化分隔符长度为1的情形
            char sep = separatorChars.charAt(0);

            while (i < length) {
                if (str.charAt(i) == sep) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = length;
                        }

                        list.add(str.substring(start, i));
                        match = false;
                    }

                    start = ++i;
                    continue;
                }

                match = true;
                i++;
            }
        } else {
            // 一般情形
            while (i < length) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = length;
                        }

                        list.add(str.substring(start, i));
                        match = false;
                    }

                    start = ++i;
                    continue;
                }

                match = true;
                i++;
            }
        }

        if (match) {
            list.add(str.substring(start, i));
        }

        return (String[]) list.toArray(new String[list.size()]);
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 字符串连接函数。 */
    /*                                                                              */
    /* 将多个对象按指定分隔符连接成字符串。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 将数组中的元素连接成一个字符串。
     * <p>
     * <pre>
     * StringUtil.join(null)            = null
     * StringUtil.join([])              = &quot;&quot;
     * StringUtil.join([null])          = &quot;&quot;
     * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;]) = &quot;abc&quot;
     * StringUtil.join([null, &quot;&quot;, &quot;a&quot;]) = &quot;a&quot;
     * </pre>
     *
     * @param array 要连接的数组
     * @return 连接后的字符串，如果原数组为<code>null</code>，则返回<code>null</code>
     */
    public static String join(Object[] array) {
        return join(array, null);
    }

    /**
     * 将数组中的元素连接成一个字符串。
     * <p>
     * <pre>
     * StringUtil.join(null, *)               = null
     * StringUtil.join([], *)                 = &quot;&quot;
     * StringUtil.join([null], *)             = &quot;&quot;
     * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], ';')  = &quot;a;b;c&quot;
     * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], null) = &quot;abc&quot;
     * StringUtil.join([null, &quot;&quot;, &quot;a&quot;], ';')  = &quot;;;a&quot;
     * </pre>
     *
     * @param array     要连接的数组
     * @param separator 分隔符
     * @return 连接后的字符串，如果原数组为<code>null</code>，则返回<code>null</code>
     */
    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }

        int arraySize = array.length;
        int bufSize = (arraySize == 0) ? 0 : ((((array[0] == null) ? 16
                : array[0].toString().length()) + 1) * arraySize);
        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }

            if (array[i] != null) {
                buf.append(array[i]);
            }
        }

        return buf.toString();
    }

    /**
     * 将数组中的元素连接成一个字符串。
     * <p>
     * <pre>
     * StringUtil.join(null, *)                = null
     * StringUtil.join([], *)                  = &quot;&quot;
     * StringUtil.join([null], *)              = &quot;&quot;
     * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;--&quot;)  = &quot;a--b--c&quot;
     * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], null)  = &quot;abc&quot;
     * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;&quot;)    = &quot;abc&quot;
     * StringUtil.join([null, &quot;&quot;, &quot;a&quot;], ',')   = &quot;,,a&quot;
     * </pre>
     *
     * @param array     要连接的数组
     * @param separator 分隔符
     * @return 连接后的字符串，如果原数组为<code>null</code>，则返回<code>null</code>
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }

        if (separator == null) {
            separator = EMPTY_STRING;
        }

        int arraySize = array.length;

        // ArraySize == 0: Len = 0
        // ArraySize > 0: Len = NofStrings *(len(firstString) + len(separator))
        // (估计大约所有的字符串都一样长)
        int bufSize = (arraySize == 0) ? 0
                : (arraySize * (((array[0] == null) ? 16 : array[0].toString()
                .length()) + ((separator != null) ? separator.length()
                : 0)));

        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if ((separator != null) && (i > 0)) {
                buf.append(separator);
            }

            if (array[i] != null) {
                buf.append(array[i]);
            }
        }

        return buf.toString();
    }

    /**
     * 将<code>Iterator</code>中的元素连接成一个字符串。
     * <p>
     * <pre>
     * StringUtil.join(null, *)                = null
     * StringUtil.join([], *)                  = &quot;&quot;
     * StringUtil.join([null], *)              = &quot;&quot;
     * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;--&quot;)  = &quot;a--b--c&quot;
     * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], null)  = &quot;abc&quot;
     * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;&quot;)    = &quot;abc&quot;
     * StringUtil.join([null, &quot;&quot;, &quot;a&quot;], ',')   = &quot;,,a&quot;
     * </pre>
     *
     * @param iterator  要连接的<code>Iterator</code>
     * @param separator 分隔符
     * @return 连接后的字符串，如果原数组为<code>null</code>，则返回<code>null</code>
     */
    public static String join(Iterator<?> iterator, char separator) {
        if (iterator == null) {
            return null;
        }

        StringBuffer buf = new StringBuffer(256); // Java默认值是16, 可能偏小

        while (iterator.hasNext()) {
            Object obj = iterator.next();

            if (obj != null) {
                buf.append(obj);
            }

            if (iterator.hasNext()) {
                buf.append(separator);
            }
        }

        return buf.toString();
    }

    /**
     * 将<code>Iterator</code>中的元素连接成一个字符串。
     * <p>
     * <pre>
     * StringUtil.join(null, *)                = null
     * StringUtil.join([], *)                  = &quot;&quot;
     * StringUtil.join([null], *)              = &quot;&quot;
     * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;--&quot;)  = &quot;a--b--c&quot;
     * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], null)  = &quot;abc&quot;
     * StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;&quot;)    = &quot;abc&quot;
     * StringUtil.join([null, &quot;&quot;, &quot;a&quot;], ',')   = &quot;,,a&quot;
     * </pre>
     *
     * @param iterator  要连接的<code>Iterator</code>
     * @param separator 分隔符
     * @return 连接后的字符串，如果原数组为<code>null</code>，则返回<code>null</code>
     */
    public static String join(Iterator<?> iterator, String separator) {
        if (iterator == null) {
            return null;
        }

        StringBuffer buf = new StringBuffer(256); // Java默认值是16, 可能偏小

        while (iterator.hasNext()) {
            Object obj = iterator.next();

            if (obj != null) {
                buf.append(obj);
            }

            if ((separator != null) && iterator.hasNext()) {
                buf.append(separator);
            }
        }

        return buf.toString();
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 字符串查找函数 —— 字符或字符串。 */
    /*                                                                              */
    /* 在字符串中查找指定字符或字符串。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 在字符串中查找指定字符，并返回第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>。
     * <p>
     * <pre>
     * StringUtil.indexOf(null, *)         = -1
     * StringUtil.indexOf(&quot;&quot;, *)           = -1
     * StringUtil.indexOf(&quot;aabaabaa&quot;, 'a') = 0
     * StringUtil.indexOf(&quot;aabaabaa&quot;, 'b') = 2
     * </pre>
     *
     * @param str        要扫描的字符串
     * @param searchChar 要查找的字符
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int indexOf(String str, char searchChar) {
        if ((str == null) || (str.length() == 0)) {
            return -1;
        }

        return str.indexOf(searchChar);
    }

    /**
     * 在字符串中查找指定字符，并返回第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>。
     * <p>
     * <pre>
     * StringUtil.indexOf(null, *, *)          = -1
     * StringUtil.indexOf(&quot;&quot;, *, *)            = -1
     * StringUtil.indexOf(&quot;aabaabaa&quot;, 'b', 0)  = 2
     * StringUtil.indexOf(&quot;aabaabaa&quot;, 'b', 3)  = 5
     * StringUtil.indexOf(&quot;aabaabaa&quot;, 'b', 9)  = -1
     * StringUtil.indexOf(&quot;aabaabaa&quot;, 'b', -1) = 2
     * </pre>
     *
     * @param str        要扫描的字符串
     * @param searchChar 要查找的字符
     * @param startPos   开始搜索的索引值，如果小于0，则看作0
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int indexOf(String str, char searchChar, int startPos) {
        if ((str == null) || (str.length() == 0)) {
            return -1;
        }

        return str.indexOf(searchChar, startPos);
    }

    /**
     * 在字符串中查找指定字符串，并返回第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>。
     * <p>
     * <pre>
     * StringUtil.indexOf(null, *)          = -1
     * StringUtil.indexOf(*, null)          = -1
     * StringUtil.indexOf(&quot;&quot;, &quot;&quot;)           = 0
     * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;a&quot;)  = 0
     * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;)  = 2
     * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;ab&quot;) = 1
     * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;&quot;)   = 0
     * </pre>
     *
     * @param str       要扫描的字符串
     * @param searchStr 要查找的字符串
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int indexOf(String str, String searchStr) {
        if ((str == null) || (searchStr == null)) {
            return -1;
        }

        return str.indexOf(searchStr);
    }

    /**
     * 在字符串中查找指定字符串，并返回第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>。
     * <p>
     * <pre>
     * StringUtil.indexOf(null, *, *)          = -1
     * StringUtil.indexOf(*, null, *)          = -1
     * StringUtil.indexOf(&quot;&quot;, &quot;&quot;, 0)           = 0
     * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;a&quot;, 0)  = 0
     * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 0)  = 2
     * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;ab&quot;, 0) = 1
     * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 3)  = 5
     * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 9)  = -1
     * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;, -1) = 2
     * StringUtil.indexOf(&quot;aabaabaa&quot;, &quot;&quot;, 2)   = 2
     * StringUtil.indexOf(&quot;abc&quot;, &quot;&quot;, 9)        = 3
     * </pre>
     *
     * @param str       要扫描的字符串
     * @param searchStr 要查找的字符串
     * @param startPos  开始搜索的索引值，如果小于0，则看作0
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int indexOf(String str, String searchStr, int startPos) {
        if ((str == null) || (searchStr == null)) {
            return -1;
        }

        // JDK1.3及以下版本的bug：不能正确处理下面的情况
        if ((searchStr.length() == 0) && (startPos >= str.length())) {
            return str.length();
        }

        return str.indexOf(searchStr, startPos);
    }

    /**
     * 在字符串中查找指定字符集合中的字符，并返回第一个匹配的起始索引。 如果字符串为<code>null</code>，则返回
     * <code>-1</code>。 如果字符集合为<code>null</code>或空，也返回<code>-1</code>。
     * <p>
     * <pre>
     * StringUtil.indexOfAny(null, *)                = -1
     * StringUtil.indexOfAny(&quot;&quot;, *)                  = -1
     * StringUtil.indexOfAny(*, null)                = -1
     * StringUtil.indexOfAny(*, [])                  = -1
     * StringUtil.indexOfAny(&quot;zzabyycdxx&quot;,['z','a']) = 0
     * StringUtil.indexOfAny(&quot;zzabyycdxx&quot;,['b','y']) = 3
     * StringUtil.indexOfAny(&quot;aba&quot;, ['z'])           = -1
     * </pre>
     *
     * @param str         要扫描的字符串
     * @param searchChars 要搜索的字符集合
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int indexOfAny(String str, char[] searchChars) {
        if ((str == null) || (str.length() == 0) || (searchChars == null)
                || (searchChars.length == 0)) {
            return -1;
        }

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            for (char searchChar : searchChars) {
                if (searchChar == ch) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * 在字符串中查找指定字符集合中的字符，并返回第一个匹配的起始索引。 如果字符串为<code>null</code>，则返回
     * <code>-1</code>。 如果字符集合为<code>null</code>或空，也返回<code>-1</code>。
     * <p>
     * <pre>
     * StringUtil.indexOfAny(null, *)            = -1
     * StringUtil.indexOfAny(&quot;&quot;, *)              = -1
     * StringUtil.indexOfAny(*, null)            = -1
     * StringUtil.indexOfAny(*, &quot;&quot;)              = -1
     * StringUtil.indexOfAny(&quot;zzabyycdxx&quot;, &quot;za&quot;) = 0
     * StringUtil.indexOfAny(&quot;zzabyycdxx&quot;, &quot;by&quot;) = 3
     * StringUtil.indexOfAny(&quot;aba&quot;,&quot;z&quot;)          = -1
     * </pre>
     *
     * @param str         要扫描的字符串
     * @param searchChars 要搜索的字符集合
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int indexOfAny(String str, String searchChars) {
        if ((str == null) || (str.length() == 0) || (searchChars == null)
                || (searchChars.length() == 0)) {
            return -1;
        }

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            for (int j = 0; j < searchChars.length(); j++) {
                if (searchChars.charAt(j) == ch) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * 在字符串中查找指定字符串集合中的字符串，并返回第一个匹配的起始索引。 如果字符串为<code>null</code>，则返回
     * <code>-1</code>。 如果字符串集合为<code>null</code>或空，也返回<code>-1</code>。
     * 如果字符串集合包括<code>""</code>，并且字符串不为<code>null</code>，则返回
     * <code>str.length()</code>
     * <p>
     * <pre>
     * StringUtil.indexOfAny(null, *)                     = -1
     * StringUtil.indexOfAny(*, null)                     = -1
     * StringUtil.indexOfAny(*, [])                       = -1
     * StringUtil.indexOfAny(&quot;zzabyycdxx&quot;, [&quot;ab&quot;,&quot;cd&quot;])   = 2
     * StringUtil.indexOfAny(&quot;zzabyycdxx&quot;, [&quot;cd&quot;,&quot;ab&quot;])   = 2
     * StringUtil.indexOfAny(&quot;zzabyycdxx&quot;, [&quot;mn&quot;,&quot;op&quot;])   = -1
     * StringUtil.indexOfAny(&quot;zzabyycdxx&quot;, [&quot;zab&quot;,&quot;aby&quot;]) = 1
     * StringUtil.indexOfAny(&quot;zzabyycdxx&quot;, [&quot;&quot;])          = 0
     * StringUtil.indexOfAny(&quot;&quot;, [&quot;&quot;])                    = 0
     * StringUtil.indexOfAny(&quot;&quot;, [&quot;a&quot;])                   = -1
     * </pre>
     *
     * @param str        要扫描的字符串
     * @param searchStrs 要搜索的字符串集合
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int indexOfAny(String str, String[] searchStrs) {
        if ((str == null) || (searchStrs == null)) {
            return -1;
        }

        int sz = searchStrs.length;

        // String's can't have a MAX_VALUEth index.
        int ret = Integer.MAX_VALUE;

        int tmp = 0;

        for (String search : searchStrs) {
            if (search == null) {
                continue;
            }

            tmp = str.indexOf(search);

            if (tmp == -1) {
                continue;
            }

            if (tmp < ret) {
                ret = tmp;
            }
        }

        return (ret == Integer.MAX_VALUE) ? (-1) : ret;
    }

    /**
     * 在字符串中查找不在指定字符集合中的字符，并返回第一个匹配的起始索引。 如果字符串为<code>null</code>，则返回
     * <code>-1</code>。 如果字符集合为<code>null</code>或空，也返回<code>-1</code>。
     * <p>
     * <pre>
     * StringUtil.indexOfAnyBut(null, *)             = -1
     * StringUtil.indexOfAnyBut(&quot;&quot;, *)               = -1
     * StringUtil.indexOfAnyBut(*, null)             = -1
     * StringUtil.indexOfAnyBut(*, [])               = -1
     * StringUtil.indexOfAnyBut(&quot;zzabyycdxx&quot;,'za')   = 3
     * StringUtil.indexOfAnyBut(&quot;zzabyycdxx&quot;, 'by')  = 0
     * StringUtil.indexOfAnyBut(&quot;aba&quot;, 'ab')         = -1
     * </pre>
     *
     * @param str         要扫描的字符串
     * @param searchChars 要搜索的字符集合
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int indexOfAnyBut(String str, char[] searchChars) {
        if ((str == null) || (str.length() == 0) || (searchChars == null)
                || (searchChars.length == 0)) {
            return -1;
        }

        outer:
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            for (char searchChar : searchChars) {
                if (searchChar == ch) {
                    continue outer;
                }
            }

            return i;
        }

        return -1;
    }

    /**
     * 在字符串中查找不在指定字符集合中的字符，并返回第一个匹配的起始索引。 如果字符串为<code>null</code>，则返回
     * <code>-1</code>。 如果字符集合为<code>null</code>或空，也返回<code>-1</code>。
     * <p>
     * <pre>
     * StringUtil.indexOfAnyBut(null, *)            = -1
     * StringUtil.indexOfAnyBut(&quot;&quot;, *)              = -1
     * StringUtil.indexOfAnyBut(*, null)            = -1
     * StringUtil.indexOfAnyBut(*, &quot;&quot;)              = -1
     * StringUtil.indexOfAnyBut(&quot;zzabyycdxx&quot;, &quot;za&quot;) = 3
     * StringUtil.indexOfAnyBut(&quot;zzabyycdxx&quot;, &quot;by&quot;) = 0
     * StringUtil.indexOfAnyBut(&quot;aba&quot;,&quot;ab&quot;)         = -1
     * </pre>
     *
     * @param str         要扫描的字符串
     * @param searchChars 要搜索的字符集合
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int indexOfAnyBut(String str, String searchChars) {
        if ((str == null) || (str.length() == 0) || (searchChars == null)
                || (searchChars.length() == 0)) {
            return -1;
        }

        for (int i = 0; i < str.length(); i++) {
            if (searchChars.indexOf(str.charAt(i)) < 0) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 从字符串尾部开始查找指定字符，并返回第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回
     * <code>-1</code>。
     * <p>
     * <pre>
     * StringUtil.lastIndexOf(null, *)         = -1
     * StringUtil.lastIndexOf(&quot;&quot;, *)           = -1
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'a') = 7
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'b') = 5
     * </pre>
     *
     * @param str        要扫描的字符串
     * @param searchChar 要查找的字符
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int lastIndexOf(String str, char searchChar) {
        if ((str == null) || (str.length() == 0)) {
            return -1;
        }

        return str.lastIndexOf(searchChar);
    }

    /**
     * 从字符串尾部开始查找指定字符，并返回第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回
     * <code>-1</code>。
     * <p>
     * <pre>
     * StringUtil.lastIndexOf(null, *, *)          = -1
     * StringUtil.lastIndexOf(&quot;&quot;, *,  *)           = -1
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'b', 8)  = 5
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'b', 4)  = 2
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'b', 0)  = -1
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'b', 9)  = 5
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'b', -1) = -1
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'a', 0)  = 0
     * </pre>
     *
     * @param str        要扫描的字符串
     * @param searchChar 要查找的字符
     * @param startPos   从指定索引开始向前搜索
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int lastIndexOf(String str, char searchChar, int startPos) {
        if ((str == null) || (str.length() == 0)) {
            return -1;
        }

        return str.lastIndexOf(searchChar, startPos);
    }

    /**
     * 从字符串尾部开始查找指定字符串，并返回第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回
     * <code>-1</code>。
     * <p>
     * <pre>
     * StringUtil.lastIndexOf(null, *)         = -1
     * StringUtil.lastIndexOf(&quot;&quot;, *)           = -1
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'a') = 7
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, 'b') = 5
     * </pre>
     *
     * @param str       要扫描的字符串
     * @param searchStr 要查找的字符串
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int lastIndexOf(String str, String searchStr) {
        if ((str == null) || (searchStr == null)) {
            return -1;
        }

        return str.lastIndexOf(searchStr);
    }

    /**
     * 从字符串尾部开始查找指定字符串，并返回第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回
     * <code>-1</code>。
     * <p>
     * <pre>
     * StringUtil.lastIndexOf(null, *, *)          = -1
     * StringUtil.lastIndexOf(*, null, *)          = -1
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;a&quot;, 8)  = 7
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 8)  = 5
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;ab&quot;, 8) = 4
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 9)  = 5
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, -1) = -1
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;a&quot;, 0)  = 0
     * StringUtil.lastIndexOf(&quot;aabaabaa&quot;, &quot;b&quot;, 0)  = -1
     * </pre>
     *
     * @param str       要扫描的字符串
     * @param searchStr 要查找的字符串
     * @param startPos  从指定索引开始向前搜索
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int lastIndexOf(String str, String searchStr, int startPos) {
        if ((str == null) || (searchStr == null)) {
            return -1;
        }

        return str.lastIndexOf(searchStr, startPos);
    }

    /**
     * 从字符串尾部开始查找指定字符串集合中的字符串，并返回第一个匹配的起始索引。 如果字符串为<code>null</code>，则返回
     * <code>-1</code>。 如果字符串集合为<code>null</code>或空，也返回<code>-1</code>。
     * 如果字符串集合包括<code>""</code>，并且字符串不为<code>null</code>，则返回
     * <code>str.length()</code>
     * <p>
     * <pre>
     * StringUtil.lastIndexOfAny(null, *)                   = -1
     * StringUtil.lastIndexOfAny(*, null)                   = -1
     * StringUtil.lastIndexOfAny(*, [])                     = -1
     * StringUtil.lastIndexOfAny(*, [null])                 = -1
     * StringUtil.lastIndexOfAny(&quot;zzabyycdxx&quot;, [&quot;ab&quot;,&quot;cd&quot;]) = 6
     * StringUtil.lastIndexOfAny(&quot;zzabyycdxx&quot;, [&quot;cd&quot;,&quot;ab&quot;]) = 6
     * StringUtil.lastIndexOfAny(&quot;zzabyycdxx&quot;, [&quot;mn&quot;,&quot;op&quot;]) = -1
     * StringUtil.lastIndexOfAny(&quot;zzabyycdxx&quot;, [&quot;mn&quot;,&quot;op&quot;]) = -1
     * StringUtil.lastIndexOfAny(&quot;zzabyycdxx&quot;, [&quot;mn&quot;,&quot;&quot;])   = 10
     * </pre>
     *
     * @param str        要扫描的字符串
     * @param searchStrs 要搜索的字符串集合
     * @return 第一个匹配的索引值。如果字符串为<code>null</code>或未找到，则返回<code>-1</code>
     */
    public static int lastIndexOfAny(String str, String[] searchStrs) {
        if ((str == null) || (searchStrs == null)) {
            return -1;
        }

        int searchStrsLength = searchStrs.length;
        int index = -1;
        int tmp = 0;

        for (String search : searchStrs) {
            if (search == null) {
                continue;
            }

            tmp = str.lastIndexOf(search);

            if (tmp > index) {
                index = tmp;
            }
        }

        return index;
    }

    /**
     * 检查字符串中是否包含指定的字符。如果字符串为<code>null</code>，将返回<code>false</code>。
     * <p>
     * <pre>
     * StringUtil.contains(null, *)    = false
     * StringUtil.contains(&quot;&quot;, *)      = false
     * StringUtil.contains(&quot;abc&quot;, 'a') = true
     * StringUtil.contains(&quot;abc&quot;, 'z') = false
     * </pre>
     *
     * @param str        要扫描的字符串
     * @param searchChar 要查找的字符
     * @return 如果找到，则返回<code>true</code>
     */
    public static boolean contains(String str, char searchChar) {
        if ((str == null) || (str.length() == 0)) {
            return false;
        }

        return str.indexOf(searchChar) >= 0;
    }

    /**
     * 检查字符串中是否包含指定的字符串。如果字符串为<code>null</code>，将返回<code>false</code>。
     * <p>
     * <pre>
     * StringUtil.contains(null, *)     = false
     * StringUtil.contains(*, null)     = false
     * StringUtil.contains(&quot;&quot;, &quot;&quot;)      = true
     * StringUtil.contains(&quot;abc&quot;, &quot;&quot;)   = true
     * StringUtil.contains(&quot;abc&quot;, &quot;a&quot;)  = true
     * StringUtil.contains(&quot;abc&quot;, &quot;z&quot;)  = false
     * </pre>
     *
     * @param str       要扫描的字符串
     * @param searchStr 要查找的字符串
     * @return 如果找到，则返回<code>true</code>
     */
    public static boolean contains(String str, String searchStr) {
        if ((str == null) || (searchStr == null)) {
            return false;
        }

        return str.indexOf(searchStr) >= 0;
    }

    /**
     * 检查字符串是是否只包含指定字符集合中的字符。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>false</code>。 如果字符集合为<code>null</code>
     * 则返回<code>false</code>。 但是空字符串永远返回<code>true</code>.
     * </p>
     * <p>
     * <pre>
     * StringUtil.containsOnly(null, *)       = false
     * StringUtil.containsOnly(*, null)       = false
     * StringUtil.containsOnly(&quot;&quot;, *)         = true
     * StringUtil.containsOnly(&quot;ab&quot;, '')      = false
     * StringUtil.containsOnly(&quot;abab&quot;, 'abc') = true
     * StringUtil.containsOnly(&quot;ab1&quot;, 'abc')  = false
     * StringUtil.containsOnly(&quot;abz&quot;, 'abc')  = false
     * </pre>
     *
     * @param str   要扫描的字符串
     * @param valid 要查找的字符串
     * @return 如果找到，则返回<code>true</code>
     */
    public static boolean containsOnly(String str, char[] valid) {
        if ((valid == null) || (str == null)) {
            return false;
        }

        if (str.length() == 0) {
            return true;
        }

        if (valid.length == 0) {
            return false;
        }

        return indexOfAnyBut(str, valid) == -1;
    }

    /**
     * 检查字符串是是否只包含指定字符集合中的字符。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>false</code>。 如果字符集合为<code>null</code>
     * 则返回<code>false</code>。 但是空字符串永远返回<code>true</code>.
     * </p>
     * <p>
     * <pre>
     * StringUtil.containsOnly(null, *)       = false
     * StringUtil.containsOnly(*, null)       = false
     * StringUtil.containsOnly(&quot;&quot;, *)         = true
     * StringUtil.containsOnly(&quot;ab&quot;, &quot;&quot;)      = false
     * StringUtil.containsOnly(&quot;abab&quot;, &quot;abc&quot;) = true
     * StringUtil.containsOnly(&quot;ab1&quot;, &quot;abc&quot;)  = false
     * StringUtil.containsOnly(&quot;abz&quot;, &quot;abc&quot;)  = false
     * </pre>
     *
     * @param str   要扫描的字符串
     * @param valid 要查找的字符串
     * @return 如果找到，则返回<code>true</code>
     */
    public static boolean containsOnly(String str, String valid) {
        if ((str == null) || (valid == null)) {
            return false;
        }

        return containsOnly(str, valid.toCharArray());
    }

    /**
     * 检查字符串是是否不包含指定字符集合中的字符。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>false</code>。 如果字符集合为<code>null</code>
     * 则返回<code>true</code>。 但是空字符串永远返回<code>true</code>.
     * </p>
     * <p>
     * <pre>
     * StringUtil.containsNone(null, *)       = true
     * StringUtil.containsNone(*, null)       = true
     * StringUtil.containsNone(&quot;&quot;, *)         = true
     * StringUtil.containsNone(&quot;ab&quot;, '')      = true
     * StringUtil.containsNone(&quot;abab&quot;, 'xyz') = true
     * StringUtil.containsNone(&quot;ab1&quot;, 'xyz')  = true
     * StringUtil.containsNone(&quot;abz&quot;, 'xyz')  = false
     * </pre>
     *
     * @param str     要扫描的字符串
     * @param invalid 要查找的字符串
     * @return 如果找到，则返回<code>true</code>
     */
    public static boolean containsNone(String str, char[] invalid) {
        if ((str == null) || (invalid == null)) {
            return true;
        }

        int strSize = str.length();
        int validSize = invalid.length;

        for (int i = 0; i < strSize; i++) {
            char ch = str.charAt(i);

            for (char anInvalid : invalid) {
                if (anInvalid == ch) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 检查字符串是是否不包含指定字符集合中的字符。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>false</code>。 如果字符集合为<code>null</code>
     * 则返回<code>true</code>。 但是空字符串永远返回<code>true</code>.
     * </p>
     * <p>
     * <pre>
     * StringUtil.containsNone(null, *)       = true
     * StringUtil.containsNone(*, null)       = true
     * StringUtil.containsNone(&quot;&quot;, *)         = true
     * StringUtil.containsNone(&quot;ab&quot;, &quot;&quot;)      = true
     * StringUtil.containsNone(&quot;abab&quot;, &quot;xyz&quot;) = true
     * StringUtil.containsNone(&quot;ab1&quot;, &quot;xyz&quot;)  = true
     * StringUtil.containsNone(&quot;abz&quot;, &quot;xyz&quot;)  = false
     * </pre>
     *
     * @param str          要扫描的字符串
     * @param invalidChars 要查找的字符串
     * @return 如果找到，则返回<code>true</code>
     */
    public static boolean containsNone(String str, String invalidChars) {
        if ((str == null) || (invalidChars == null)) {
            return true;
        }

        return containsNone(str, invalidChars.toCharArray());
    }

    /**
     * 取得指定子串在字符串中出现的次数。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>或空，则返回<code>0</code>。
     * <p>
     * <pre>
     * StringUtil.countMatches(null, *)       = 0
     * StringUtil.countMatches(&quot;&quot;, *)         = 0
     * StringUtil.countMatches(&quot;abba&quot;, null)  = 0
     * StringUtil.countMatches(&quot;abba&quot;, &quot;&quot;)    = 0
     * StringUtil.countMatches(&quot;abba&quot;, &quot;a&quot;)   = 2
     * StringUtil.countMatches(&quot;abba&quot;, &quot;ab&quot;)  = 1
     * StringUtil.countMatches(&quot;abba&quot;, &quot;xxx&quot;) = 0
     * </pre>
     * <p>
     * </p>
     *
     * @param str    要扫描的字符串
     * @param subStr 子字符串
     * @return 子串在字符串中出现的次数，如果字符串为<code>null</code>或空，则返回<code>0</code>
     */
    public static int countMatches(String str, String subStr) {
        if ((str == null) || (str.length() == 0) || (subStr == null)
                || (subStr.length() == 0)) {
            return 0;
        }

        int count = 0;
        int index = 0;

        while ((index = str.indexOf(subStr, index)) != -1) {
            count++;
            index += subStr.length();
        }

        return count;
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 取子串函数。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 取指定字符串的子串。
     * <p>
     * <p>
     * 负的索引代表从尾部开始计算。如果字符串为<code>null</code>，则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.substring(null, *)   = null
     * StringUtil.substring(&quot;&quot;, *)     = &quot;&quot;
     * StringUtil.substring(&quot;abc&quot;, 0)  = &quot;abc&quot;
     * StringUtil.substring(&quot;abc&quot;, 2)  = &quot;c&quot;
     * StringUtil.substring(&quot;abc&quot;, 4)  = &quot;&quot;
     * StringUtil.substring(&quot;abc&quot;, -2) = &quot;bc&quot;
     * StringUtil.substring(&quot;abc&quot;, -4) = &quot;abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str   字符串
     * @param start 起始索引，如果为负数，表示从尾部查找
     * @return 子串，如果原始串为<code>null</code>，则返回<code>null</code>
     */
    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }

        if (start < 0) {
            start = str.length() + start;
        }

        if (start < 0) {
            start = 0;
        }

        if (start > str.length()) {
            return EMPTY_STRING;
        }

        return str.substring(start);
    }

    /**
     * 取指定字符串的子串。
     * <p>
     * <p>
     * 负的索引代表从尾部开始计算。如果字符串为<code>null</code>，则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.substring(null, *, *)    = null
     * StringUtil.substring(&quot;&quot;, * ,  *)    = &quot;&quot;;
     * StringUtil.substring(&quot;abc&quot;, 0, 2)   = &quot;ab&quot;
     * StringUtil.substring(&quot;abc&quot;, 2, 0)   = &quot;&quot;
     * StringUtil.substring(&quot;abc&quot;, 2, 4)   = &quot;c&quot;
     * StringUtil.substring(&quot;abc&quot;, 4, 6)   = &quot;&quot;
     * StringUtil.substring(&quot;abc&quot;, 2, 2)   = &quot;&quot;
     * StringUtil.substring(&quot;abc&quot;, -2, -1) = &quot;b&quot;
     * StringUtil.substring(&quot;abc&quot;, -4, 2)  = &quot;ab&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str   字符串
     * @param start 起始索引，如果为负数，表示从尾部计算
     * @param end   结束索引（不含），如果为负数，表示从尾部计算
     * @return 子串，如果原始串为<code>null</code>，则返回<code>null</code>
     */
    public static String substring(String str, int start, int end) {
        return substring(str, start, end, StringUtils.EMPTY_STRING);
    }

    public static String substring(String str, int start, int end,
                                   String moreFiller) {
        if (str == null) {
            return null;
        }

        if (end < 0) {
            end = str.length() + end;
        }

        if (start < 0) {
            start = str.length() + start;
        }

        if (end > str.length()) {
            end = str.length();
        }

        if (start > end) {
            return EMPTY_STRING;
        }

        if (start < 0) {
            start = 0;
        }

        if (end < 0) {
            end = 0;
        }

        String returnString = str.substring(start, end);
        if (!StringUtils.isEmpty(moreFiller)) {
            if (returnString.length() < str.length()) {
                if (start >= 0) {
                    returnString += moreFiller;
                } else {
                    returnString = moreFiller + returnString;
                }
            }
        }
        return returnString;
    }

    /**
     * 取得长度为指定字符数的最左边的子串。
     * <p>
     * <pre>
     * StringUtil.left(null, *)    = null
     * StringUtil.left(*, -ve)     = &quot;&quot;
     * StringUtil.left(&quot;&quot;, *)      = &quot;&quot;
     * StringUtil.left(&quot;abc&quot;, 0)   = &quot;&quot;
     * StringUtil.left(&quot;abc&quot;, 2)   = &quot;ab&quot;
     * StringUtil.left(&quot;abc&quot;, 4)   = &quot;abc&quot;
     * </pre>
     *
     * @param str 字符串
     * @param len 最左子串的长度
     * @return 子串，如果原始字串为<code>null</code>，则返回<code>null</code>
     */
    public static String left(String str, int len) {
        if (str == null) {
            return null;
        }

        if (len < 0) {
            return EMPTY_STRING;
        }

        if (str.length() <= len) {
            return str;
        } else {
            return str.substring(0, len);
        }
    }

    /**
     * 取得长度为指定字符数的最右边的子串。
     * <p>
     * <pre>
     * StringUtil.right(null, *)    = null
     * StringUtil.right(*, -ve)     = &quot;&quot;
     * StringUtil.right(&quot;&quot;, *)      = &quot;&quot;
     * StringUtil.right(&quot;abc&quot;, 0)   = &quot;&quot;
     * StringUtil.right(&quot;abc&quot;, 2)   = &quot;bc&quot;
     * StringUtil.right(&quot;abc&quot;, 4)   = &quot;abc&quot;
     * </pre>
     *
     * @param str 字符串
     * @param len 最右子串的长度
     * @return 子串，如果原始字串为<code>null</code>，则返回<code>null</code>
     */
    public static String right(String str, int len) {
        if (str == null) {
            return null;
        }

        if (len < 0) {
            return EMPTY_STRING;
        }

        if (str.length() <= len) {
            return str;
        } else {
            return str.substring(str.length() - len);
        }
    }

    /**
     * 取得从指定索引开始计算的、长度为指定字符数的子串。
     * <p>
     * <pre>
     * StringUtil.mid(null, *, *)    = null
     * StringUtil.mid(*, *, -ve)     = &quot;&quot;
     * StringUtil.mid(&quot;&quot;, 0, *)      = &quot;&quot;
     * StringUtil.mid(&quot;abc&quot;, 0, 2)   = &quot;ab&quot;
     * StringUtil.mid(&quot;abc&quot;, 0, 4)   = &quot;abc&quot;
     * StringUtil.mid(&quot;abc&quot;, 2, 4)   = &quot;c&quot;
     * StringUtil.mid(&quot;abc&quot;, 4, 2)   = &quot;&quot;
     * StringUtil.mid(&quot;abc&quot;, -2, 2)  = &quot;ab&quot;
     * </pre>
     *
     * @param str 字符串
     * @param pos 起始索引，如果为负数，则看作<code>0</code>
     * @param len 子串的长度，如果为负数，则看作长度为<code>0</code>
     * @return 子串，如果原始字串为<code>null</code>，则返回<code>null</code>
     */
    public static String mid(String str, int pos, int len) {
        if (str == null) {
            return null;
        }

        if ((len < 0) || (pos > str.length())) {
            return EMPTY_STRING;
        }

        if (pos < 0) {
            pos = 0;
        }

        if (str.length() <= (pos + len)) {
            return str.substring(pos);
        } else {
            return str.substring(pos, pos + len);
        }
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 搜索并取子串函数。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 取得第一个出现的分隔子串之前的子串。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>null</code>。 如果分隔子串为<code>null</code>
     * 或未找到该子串，则返回原字符串。
     * <p>
     * <pre>
     * StringUtil.substringBefore(null, *)      = null
     * StringUtil.substringBefore(&quot;&quot;, *)        = &quot;&quot;
     * StringUtil.substringBefore(&quot;abc&quot;, &quot;a&quot;)   = &quot;&quot;
     * StringUtil.substringBefore(&quot;abcba&quot;, &quot;b&quot;) = &quot;a&quot;
     * StringUtil.substringBefore(&quot;abc&quot;, &quot;c&quot;)   = &quot;ab&quot;
     * StringUtil.substringBefore(&quot;abc&quot;, &quot;d&quot;)   = &quot;abc&quot;
     * StringUtil.substringBefore(&quot;abc&quot;, &quot;&quot;)    = &quot;&quot;
     * StringUtil.substringBefore(&quot;abc&quot;, null)  = &quot;abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str       字符串
     * @param separator 要搜索的分隔子串
     * @return 子串，如果原始串为<code>null</code>，则返回<code>null</code>
     */
    public static String substringBefore(String str, String separator) {
        if ((str == null) || (separator == null) || (str.length() == 0)) {
            return str;
        }

        if (separator.length() == 0) {
            return EMPTY_STRING;
        }

        int pos = str.indexOf(separator);

        if (pos == -1) {
            return str;
        }

        return str.substring(0, pos);
    }

    /**
     * 取得第一个出现的分隔子串之后的子串。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>null</code>。 如果分隔子串为<code>null</code>
     * 或未找到该子串，则返回原字符串。
     * <p>
     * <pre>
     * StringUtil.substringAfter(null, *)      = null
     * StringUtil.substringAfter(&quot;&quot;, *)        = &quot;&quot;
     * StringUtil.substringAfter(*, null)      = &quot;&quot;
     * StringUtil.substringAfter(&quot;abc&quot;, &quot;a&quot;)   = &quot;bc&quot;
     * StringUtil.substringAfter(&quot;abcba&quot;, &quot;b&quot;) = &quot;cba&quot;
     * StringUtil.substringAfter(&quot;abc&quot;, &quot;c&quot;)   = &quot;&quot;
     * StringUtil.substringAfter(&quot;abc&quot;, &quot;d&quot;)   = &quot;&quot;
     * StringUtil.substringAfter(&quot;abc&quot;, &quot;&quot;)    = &quot;abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str       字符串
     * @param separator 要搜索的分隔子串
     * @return 子串，如果原始串为<code>null</code>，则返回<code>null</code>
     */
    public static String substringAfter(String str, String separator) {
        if ((str == null) || (str.length() == 0)) {
            return str;
        }

        if (separator == null) {
            return EMPTY_STRING;
        }

        int pos = str.indexOf(separator);

        if (pos == -1) {
            return EMPTY_STRING;
        }

        return str.substring(pos + separator.length());
    }

    /**
     * 取得最后一个的分隔子串之前的子串。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>null</code>。 如果分隔子串为<code>null</code>
     * 或未找到该子串，则返回原字符串。
     * <p>
     * <pre>
     * StringUtil.substringBeforeLast(null, *)      = null
     * StringUtil.substringBeforeLast(&quot;&quot;, *)        = &quot;&quot;
     * StringUtil.substringBeforeLast(&quot;abcba&quot;, &quot;b&quot;) = &quot;abc&quot;
     * StringUtil.substringBeforeLast(&quot;abc&quot;, &quot;c&quot;)   = &quot;ab&quot;
     * StringUtil.substringBeforeLast(&quot;a&quot;, &quot;a&quot;)     = &quot;&quot;
     * StringUtil.substringBeforeLast(&quot;a&quot;, &quot;z&quot;)     = &quot;a&quot;
     * StringUtil.substringBeforeLast(&quot;a&quot;, null)    = &quot;a&quot;
     * StringUtil.substringBeforeLast(&quot;a&quot;, &quot;&quot;)      = &quot;a&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str       字符串
     * @param separator 要搜索的分隔子串
     * @return 子串，如果原始串为<code>null</code>，则返回<code>null</code>
     */
    public static String substringBeforeLast(String str, String separator) {
        if ((str == null) || (separator == null) || (str.length() == 0)
                || (separator.length() == 0)) {
            return str;
        }

        int pos = str.lastIndexOf(separator);

        if (pos == -1) {
            return str;
        }

        return str.substring(0, pos);
    }

    /**
     * 取得最后一个的分隔子串之后的子串。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>null</code>。 如果分隔子串为<code>null</code>
     * 或未找到该子串，则返回原字符串。
     * <p>
     * <pre>
     * StringUtil.substringAfterLast(null, *)      = null
     * StringUtil.substringAfterLast(&quot;&quot;, *)        = &quot;&quot;
     * StringUtil.substringAfterLast(*, &quot;&quot;)        = &quot;&quot;
     * StringUtil.substringAfterLast(*, null)      = &quot;&quot;
     * StringUtil.substringAfterLast(&quot;abc&quot;, &quot;a&quot;)   = &quot;bc&quot;
     * StringUtil.substringAfterLast(&quot;abcba&quot;, &quot;b&quot;) = &quot;a&quot;
     * StringUtil.substringAfterLast(&quot;abc&quot;, &quot;c&quot;)   = &quot;&quot;
     * StringUtil.substringAfterLast(&quot;a&quot;, &quot;a&quot;)     = &quot;&quot;
     * StringUtil.substringAfterLast(&quot;a&quot;, &quot;z&quot;)     = &quot;&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str       字符串
     * @param separator 要搜索的分隔子串
     * @return 子串，如果原始串为<code>null</code>，则返回<code>null</code>
     */
    public static String substringAfterLast(String str, String separator) {
        if ((str == null) || (str.length() == 0)) {
            return str;
        }

        if ((separator == null) || (separator.length() == 0)) {
            return EMPTY_STRING;
        }

        int pos = str.lastIndexOf(separator);

        if ((pos == -1) || (pos == (str.length() - separator.length()))) {
            return EMPTY_STRING;
        }

        return str.substring(pos + separator.length());
    }

    /**
     * 取得指定分隔符的前两次出现之间的子串。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>null</code>。 如果分隔子串为<code>null</code>
     * ，则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.substringBetween(null, *)            = null
     * StringUtil.substringBetween(&quot;&quot;, &quot;&quot;)             = &quot;&quot;
     * StringUtil.substringBetween(&quot;&quot;, &quot;tag&quot;)          = null
     * StringUtil.substringBetween(&quot;tagabctag&quot;, null)  = null
     * StringUtil.substringBetween(&quot;tagabctag&quot;, &quot;&quot;)    = &quot;&quot;
     * StringUtil.substringBetween(&quot;tagabctag&quot;, &quot;tag&quot;) = &quot;abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 字符串
     * @param tag 要搜索的分隔子串
     * @return 子串，如果原始串为<code>null</code>或未找到分隔子串，则返回<code>null</code>
     */
    public static String substringBetween(String str, String tag) {
        return substringBetween(str, tag, tag, 0);
    }

    /**
     * 取得两个分隔符之间的子串。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>null</code>。 如果分隔子串为<code>null</code>
     * ，则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.substringBetween(null, *, *)          = null
     * StringUtil.substringBetween(&quot;&quot;, &quot;&quot;, &quot;&quot;)          = &quot;&quot;
     * StringUtil.substringBetween(&quot;&quot;, &quot;&quot;, &quot;tag&quot;)       = null
     * StringUtil.substringBetween(&quot;&quot;, &quot;tag&quot;, &quot;tag&quot;)    = null
     * StringUtil.substringBetween(&quot;yabcz&quot;, null, null) = null
     * StringUtil.substringBetween(&quot;yabcz&quot;, &quot;&quot;, &quot;&quot;)     = &quot;&quot;
     * StringUtil.substringBetween(&quot;yabcz&quot;, &quot;y&quot;, &quot;z&quot;)   = &quot;abc&quot;
     * StringUtil.substringBetween(&quot;yabczyabcz&quot;, &quot;y&quot;, &quot;z&quot;)   = &quot;abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str   字符串
     * @param open  要搜索的分隔子串1
     * @param close 要搜索的分隔子串2
     * @return 子串，如果原始串为<code>null</code>或未找到分隔子串，则返回<code>null</code>
     */
    public static String substringBetween(String str, String open, String close) {
        return substringBetween(str, open, close, 0);
    }

    /**
     * 取得两个分隔符之间的子串。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>null</code>。 如果分隔子串为<code>null</code>
     * ，则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.substringBetween(null, *, *)          = null
     * StringUtil.substringBetween(&quot;&quot;, &quot;&quot;, &quot;&quot;)          = &quot;&quot;
     * StringUtil.substringBetween(&quot;&quot;, &quot;&quot;, &quot;tag&quot;)       = null
     * StringUtil.substringBetween(&quot;&quot;, &quot;tag&quot;, &quot;tag&quot;)    = null
     * StringUtil.substringBetween(&quot;yabcz&quot;, null, null) = null
     * StringUtil.substringBetween(&quot;yabcz&quot;, &quot;&quot;, &quot;&quot;)     = &quot;&quot;
     * StringUtil.substringBetween(&quot;yabcz&quot;, &quot;y&quot;, &quot;z&quot;)   = &quot;abc&quot;
     * StringUtil.substringBetween(&quot;yabczyabcz&quot;, &quot;y&quot;, &quot;z&quot;)   = &quot;abc&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str       字符串
     * @param open      要搜索的分隔子串1
     * @param close     要搜索的分隔子串2
     * @param fromIndex 从指定index处搜索
     * @return 子串，如果原始串为<code>null</code>或未找到分隔子串，则返回<code>null</code>
     */
    public static String substringBetween(String str, String open,
                                          String close, int fromIndex) {
        if ((str == null) || (open == null) || (close == null)) {
            return null;
        }

        int start = str.indexOf(open, fromIndex);

        if (start != -1) {
            int end = str.indexOf(close, start + open.length());

            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }

        return null;
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 删除字符。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 删除所有在<code>Character.isWhitespace(char)</code>中所定义的空白。
     * <p>
     * <pre>
     * StringUtil.deleteWhitespace(null)         = null
     * StringUtil.deleteWhitespace(&quot;&quot;)           = &quot;&quot;
     * StringUtil.deleteWhitespace(&quot;abc&quot;)        = &quot;abc&quot;
     * StringUtil.deleteWhitespace(&quot;   ab  c  &quot;) = &quot;abc&quot;
     * </pre>
     *
     * @param str 要处理的字符串
     * @return 去空白后的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String deleteWhitespace(String str) {
        if (str == null) {
            return null;
        }

        int sz = str.length();
        StringBuffer buffer = new StringBuffer(sz);

        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                buffer.append(str.charAt(i));
            }
        }

        return buffer.toString();
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 替换子串。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 替换指定的子串，只替换第一个出现的子串。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>则返回<code>null</code>，如果指定子串为<code>null</code>
     * ，则返回原字符串。
     * <p>
     * <pre>
     * StringUtil.replaceOnce(null, *, *)        = null
     * StringUtil.replaceOnce(&quot;&quot;, *, *)          = &quot;&quot;
     * StringUtil.replaceOnce(&quot;aba&quot;, null, null) = &quot;aba&quot;
     * StringUtil.replaceOnce(&quot;aba&quot;, null, null) = &quot;aba&quot;
     * StringUtil.replaceOnce(&quot;aba&quot;, &quot;a&quot;, null)  = &quot;aba&quot;
     * StringUtil.replaceOnce(&quot;aba&quot;, &quot;a&quot;, &quot;&quot;)    = &quot;ba&quot;
     * StringUtil.replaceOnce(&quot;aba&quot;, &quot;a&quot;, &quot;z&quot;)   = &quot;zba&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param text 要扫描的字符串
     * @param repl 要搜索的子串
     * @param with 替换字符串
     * @return 被替换后的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String replaceOnce(String text, String repl, String with) {
        return replace(text, repl, with, 1);
    }

    /**
     * 替换指定的子串，替换所有出现的子串。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>则返回<code>null</code>，如果指定子串为<code>null</code>
     * ，则返回原字符串。
     * <p>
     * <pre>
     * StringUtil.replace(null, *, *)        = null
     * StringUtil.replace(&quot;&quot;, *, *)          = &quot;&quot;
     * StringUtil.replace(&quot;aba&quot;, null, null) = &quot;aba&quot;
     * StringUtil.replace(&quot;aba&quot;, null, null) = &quot;aba&quot;
     * StringUtil.replace(&quot;aba&quot;, &quot;a&quot;, null)  = &quot;aba&quot;
     * StringUtil.replace(&quot;aba&quot;, &quot;a&quot;, &quot;&quot;)    = &quot;b&quot;
     * StringUtil.replace(&quot;aba&quot;, &quot;a&quot;, &quot;z&quot;)   = &quot;zbz&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param text 要扫描的字符串
     * @param repl 要搜索的子串
     * @param with 替换字符串
     * @return 被替换后的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String replace(String text, String repl, String with) {
        return replace(text, repl, with, -1);
    }

    /**
     * 替换
     *
     * @param texts
     * @param repl
     * @param with
     * @return
     */
    public static void replace(String[] texts, String repl, String with) {
        if (texts == null) {
            return;
        }
        for (int i = 0; i < texts.length; i++) {
            texts[i] = replace(texts[i], repl, with);
        }
    }

    /**
     * 替换
     *
     * @param texts
     * @param repl
     * @param with
     * @return
     */
    public static void replace(List<String> texts, String repl, String with) {
        if (texts == null) {
            return;
        }
        for (int i = 0; i < texts.size(); i++) {
            texts.set(i, replace(texts.get(i), repl, with));
        }
    }

    /**
     * 字符串替换
     *
     * @param text
     * @param repls
     * @param with
     * @return
     */
    public static String replace(String text, String[] repls, String with) {
        for (String repl : repls) {
            text = replace(text, repl, with, -1);
        }
        return text;
    }

    /**
     * 字符串替换
     *
     * @param text
     * @param replaceMap
     * @return
     */
    public static String replace(String text, Map<?, ?> replaceMap) {
        if (replaceMap == null) {
            return text;

        }
        for (Map.Entry<?, ?> entry : replaceMap.entrySet()) {
            text = replace(text, ObjectUtils.toString(entry.getKey()),
                    ObjectUtils.toString(entry.getValue()));
        }
        return text;
    }

    /**
     * 替换指定的子串，替换指定的次数。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>则返回<code>null</code>，如果指定子串为<code>null</code>
     * ，则返回原字符串。
     * <p>
     * <pre>
     * StringUtil.replace(null, *, *, *)         = null
     * StringUtil.replace(&quot;&quot;, *, *, *)           = &quot;&quot;
     * StringUtil.replace(&quot;abaa&quot;, null, null, 1) = &quot;abaa&quot;
     * StringUtil.replace(&quot;abaa&quot;, null, null, 1) = &quot;abaa&quot;
     * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, null, 1)  = &quot;abaa&quot;
     * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;&quot;, 1)    = &quot;baa&quot;
     * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, 0)   = &quot;abaa&quot;
     * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, 1)   = &quot;zbaa&quot;
     * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, 2)   = &quot;zbza&quot;
     * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, -1)  = &quot;zbzz&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param text 要扫描的字符串
     * @param repl 要搜索的子串
     * @param with 替换字符串
     * @param max  maximum number of values to replace, or <code>-1</code> if no
     *             maximum
     * @return 被替换后的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String replace(String text, String repl, String with, int max) {
        if ((text == null) || (repl == null) || (with == null)
                || (repl.length() == 0) || (max == 0)) {
            return text;
        }

        StringBuffer buf = new StringBuffer(text.length());
        int start = 0;
        int end = 0;

        while ((end = text.indexOf(repl, start)) != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();

            if (--max == 0) {
                break;
            }
        }

        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * 将字符串中所有指定的字符，替换成另一个。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>则返回<code>null</code>。
     * <p>
     * <pre>
     * StringUtil.replaceChars(null, *, *)        = null
     * StringUtil.replaceChars(&quot;&quot;, *, *)          = &quot;&quot;
     * StringUtil.replaceChars(&quot;abcba&quot;, 'b', 'y') = &quot;aycya&quot;
     * StringUtil.replaceChars(&quot;abcba&quot;, 'z', 'y') = &quot;abcba&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str         要扫描的字符串
     * @param searchChar  要搜索的字符
     * @param replaceChar 替换字符
     * @return 被替换后的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String replaceChars(String str, char searchChar,
                                      char replaceChar) {
        if (str == null) {
            return null;
        }

        return str.replace(searchChar, replaceChar);
    }

    /**
     * 将字符串中所有指定的字符，替换成另一个。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>则返回<code>null</code>。如果搜索字符串为<code>null</code>
     * 或空，则返回原字符串。
     * </p>
     * <p>
     * <p>
     * 例如：
     * <code>replaceChars(&quot;hello&quot;, &quot;ho&quot;, &quot;jy&quot;) = jelly</code>
     * 。
     * </p>
     * <p>
     * <p>
     * 通常搜索字符串和替换字符串是等长的，如果搜索字符串比替换字符串长，则多余的字符将被删除。 如果搜索字符串比替换字符串短，则缺少的字符将被忽略。
     * <p>
     * <pre>
     * StringUtil.replaceChars(null, *, *)           = null
     * StringUtil.replaceChars(&quot;&quot;, *, *)             = &quot;&quot;
     * StringUtil.replaceChars(&quot;abc&quot;, null, *)       = &quot;abc&quot;
     * StringUtil.replaceChars(&quot;abc&quot;, &quot;&quot;, *)         = &quot;abc&quot;
     * StringUtil.replaceChars(&quot;abc&quot;, &quot;b&quot;, null)     = &quot;ac&quot;
     * StringUtil.replaceChars(&quot;abc&quot;, &quot;b&quot;, &quot;&quot;)       = &quot;ac&quot;
     * StringUtil.replaceChars(&quot;abcba&quot;, &quot;bc&quot;, &quot;yz&quot;)  = &quot;ayzya&quot;
     * StringUtil.replaceChars(&quot;abcba&quot;, &quot;bc&quot;, &quot;y&quot;)   = &quot;ayya&quot;
     * StringUtil.replaceChars(&quot;abcba&quot;, &quot;bc&quot;, &quot;yzx&quot;) = &quot;ayzya&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str          要扫描的字符串
     * @param searchChars  要搜索的字符串
     * @param replaceChars 替换字符串
     * @return 被替换后的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String replaceChars(String str, String searchChars,
                                      String replaceChars) {
        if ((str == null) || (str.length() == 0) || (searchChars == null)
                || (searchChars.length() == 0)) {
            return str;
        }

        char[] chars = str.toCharArray();
        int len = chars.length;
        boolean modified = false;

        for (int i = 0, isize = searchChars.length(); i < isize; i++) {
            char searchChar = searchChars.charAt(i);

            if ((replaceChars == null) || (i >= replaceChars.length())) {
                // 删除
                int pos = 0;

                for (int j = 0; j < len; j++) {
                    if (chars[j] != searchChar) {
                        chars[pos++] = chars[j];
                    } else {
                        modified = true;
                    }
                }

                len = pos;
            } else {
                // 替换
                for (int j = 0; j < len; j++) {
                    if (chars[j] == searchChar) {
                        chars[j] = replaceChars.charAt(i);
                        modified = true;
                    }
                }
            }
        }

        if (!modified) {
            return str;
        }

        return new String(chars, 0, len);
    }

    /**
     * 将指定的子串用另一指定子串覆盖。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>null</code>。 负的索引值将被看作<code>0</code>
     * ，越界的索引值将被设置成字符串的长度相同的值。
     * <p>
     * <pre>
     * StringUtil.overlay(null, *, *, *)            = null
     * StringUtil.overlay(&quot;&quot;, &quot;abc&quot;, 0, 0)          = &quot;abc&quot;
     * StringUtil.overlay(&quot;abcdef&quot;, null, 2, 4)     = &quot;abef&quot;
     * StringUtil.overlay(&quot;abcdef&quot;, &quot;&quot;, 2, 4)       = &quot;abef&quot;
     * StringUtil.overlay(&quot;abcdef&quot;, &quot;&quot;, 4, 2)       = &quot;abef&quot;
     * StringUtil.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, 2, 4)   = &quot;abzzzzef&quot;
     * StringUtil.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, 4, 2)   = &quot;abzzzzef&quot;
     * StringUtil.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, -1, 4)  = &quot;zzzzef&quot;
     * StringUtil.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, 2, 8)   = &quot;abzzzz&quot;
     * StringUtil.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, -2, -3) = &quot;zzzzabcdef&quot;
     * StringUtil.overlay(&quot;abcdef&quot;, &quot;zzzz&quot;, 8, 10)  = &quot;abcdefzzzz&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str     要扫描的字符串
     * @param overlay 用来覆盖的字符串
     * @param start   起始索引
     * @param end     结束索引
     * @return 被覆盖后的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String overlay(String str, String overlay, int start, int end) {
        if (str == null) {
            return null;
        }

        if (overlay == null) {
            overlay = EMPTY_STRING;
        }

        int len = str.length();

        if (start < 0) {
            start = 0;
        }

        if (start > len) {
            start = len;
        }

        if (end < 0) {
            end = 0;
        }

        if (end > len) {
            end = len;
        }

        if (start > end) {
            int temp = start;

            start = end;
            end = temp;
        }

        return new StringBuffer((len + start) - end + overlay.length() + 1)
                .append(str.substring(0, start)).append(overlay)
                .append(str.substring(end)).toString();
    }

    /*
     * ==========================================================================
     * ==
     */
    /* Perl风格的chomp和chop函数。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 删除字符串末尾的换行符。如果字符串不以换行结尾，则什么也不做。
     * <p>
     * <p>
     * 换行符有三种情形：&quot;<code>\n</code>&quot;、&quot;<code>\r</code>&quot;、&quot;
     * <code>\r\n</code>&quot;。
     * <p>
     * <pre>
     * StringUtil.chomp(null)          = null
     * StringUtil.chomp(&quot;&quot;)            = &quot;&quot;
     * StringUtil.chomp(&quot;abc \r&quot;)      = &quot;abc &quot;
     * StringUtil.chomp(&quot;abc\n&quot;)       = &quot;abc&quot;
     * StringUtil.chomp(&quot;abc\r\n&quot;)     = &quot;abc&quot;
     * StringUtil.chomp(&quot;abc\r\n\r\n&quot;) = &quot;abc\r\n&quot;
     * StringUtil.chomp(&quot;abc\n\r&quot;)     = &quot;abc\n&quot;
     * StringUtil.chomp(&quot;abc\n\rabc&quot;)  = &quot;abc\n\rabc&quot;
     * StringUtil.chomp(&quot;\r&quot;)          = &quot;&quot;
     * StringUtil.chomp(&quot;\n&quot;)          = &quot;&quot;
     * StringUtil.chomp(&quot;\r\n&quot;)        = &quot;&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 不以换行结尾的字符串，如果原始字串为<code>null</code>，则返回<code>null</code>
     */
    public static String chomp(String str) {
        if ((str == null) || (str.length() == 0)) {
            return str;
        }

        if (str.length() == 1) {
            char ch = str.charAt(0);

            if ((ch == '\r') || (ch == '\n')) {
                return EMPTY_STRING;
            } else {
                return str;
            }
        }

        int lastIdx = str.length() - 1;
        char last = str.charAt(lastIdx);

        if (last == '\n') {
            if (str.charAt(lastIdx - 1) == '\r') {
                lastIdx--;
            }
        } else if (last == '\r') {
        } else {
            lastIdx++;
        }

        return str.substring(0, lastIdx);
    }

    /**
     * 删除字符串末尾的指定字符串。如果字符串不以该字符串结尾，则什么也不做。
     * <p>
     * <pre>
     * StringUtil.chomp(null, *)         = null
     * StringUtil.chomp(&quot;&quot;, *)           = &quot;&quot;
     * StringUtil.chomp(&quot;foobar&quot;, &quot;bar&quot;) = &quot;foo&quot;
     * StringUtil.chomp(&quot;foobar&quot;, &quot;baz&quot;) = &quot;foobar&quot;
     * StringUtil.chomp(&quot;foo&quot;, &quot;foo&quot;)    = &quot;&quot;
     * StringUtil.chomp(&quot;foo &quot;, &quot;foo&quot;)   = &quot;foo &quot;
     * StringUtil.chomp(&quot; foo&quot;, &quot;foo&quot;)   = &quot; &quot;
     * StringUtil.chomp(&quot;foo&quot;, &quot;foooo&quot;)  = &quot;foo&quot;
     * StringUtil.chomp(&quot;foo&quot;, &quot;&quot;)       = &quot;foo&quot;
     * StringUtil.chomp(&quot;foo&quot;, null)     = &quot;foo&quot;
     * </pre>
     *
     * @param str       要处理的字符串
     * @param separator 要删除的字符串
     * @return 不以指定字符串结尾的字符串，如果原始字串为<code>null</code>，则返回<code>null</code>
     */
    public static String chomp(String str, String separator) {
        if ((str == null) || (str.length() == 0) || (separator == null)) {
            return str;
        }

        if (str.endsWith(separator)) {
            return str.substring(0, str.length() - separator.length());
        }

        return str;
    }

    /**
     * 删除最后一个字符。
     * <p>
     * <p>
     * 如果字符串以<code>\r\n</code>结尾，则同时删除它们。
     * <p>
     * <pre>
     * StringUtil.chop(null)          = null
     * StringUtil.chop(&quot;&quot;)            = &quot;&quot;
     * StringUtil.chop(&quot;abc \r&quot;)      = &quot;abc &quot;
     * StringUtil.chop(&quot;abc\n&quot;)       = &quot;abc&quot;
     * StringUtil.chop(&quot;abc\r\n&quot;)     = &quot;abc&quot;
     * StringUtil.chop(&quot;abc&quot;)         = &quot;ab&quot;
     * StringUtil.chop(&quot;abc\nabc&quot;)    = &quot;abc\nab&quot;
     * StringUtil.chop(&quot;a&quot;)           = &quot;&quot;
     * StringUtil.chop(&quot;\r&quot;)          = &quot;&quot;
     * StringUtil.chop(&quot;\n&quot;)          = &quot;&quot;
     * StringUtil.chop(&quot;\r\n&quot;)        = &quot;&quot;
     * </pre>
     * <p>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 删除最后一个字符的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String chop(String str) {
        if (str == null) {
            return null;
        }

        int strLen = str.length();

        if (strLen < 2) {
            return EMPTY_STRING;
        }

        int lastIdx = strLen - 1;
        String ret = str.substring(0, lastIdx);
        char last = str.charAt(lastIdx);

        if (last == '\n') {
            if (ret.charAt(lastIdx - 1) == '\r') {
                return ret.substring(0, lastIdx - 1);
            }
        }

        return ret;
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 重复/对齐字符串。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 将指定字符串重复n遍。
     * <p>
     * <pre>
     * StringUtil.repeat(null, 2)   = null
     * StringUtil.repeat(&quot;&quot;, 0)     = &quot;&quot;
     * StringUtil.repeat(&quot;&quot;, 2)     = &quot;&quot;
     * StringUtil.repeat(&quot;a&quot;, 3)    = &quot;aaa&quot;
     * StringUtil.repeat(&quot;ab&quot;, 2)   = &quot;abab&quot;
     * StringUtil.repeat(&quot;abcd&quot;, 2) = &quot;abcdabcd&quot;
     * StringUtil.repeat(&quot;a&quot;, -2)   = &quot;&quot;
     * </pre>
     *
     * @param str    要重复的字符串
     * @param repeat 重复次数，如果小于<code>0</code>，则看作<code>0</code>
     * @return 重复n次的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String repeat(String str, int repeat) {
        if (str == null) {
            return null;
        }

        if (repeat <= 0) {
            return EMPTY_STRING;
        }

        int inputLength = str.length();

        if ((repeat == 1) || (inputLength == 0)) {
            return str;
        }

        int outputLength = inputLength * repeat;

        switch (inputLength) {
            case 1:

                char ch = str.charAt(0);
                char[] output1 = new char[outputLength];

                for (int i = repeat - 1; i >= 0; i--) {
                    output1[i] = ch;
                }

                return new String(output1);

            case 2:

                char ch0 = str.charAt(0);
                char ch1 = str.charAt(1);
                char[] output2 = new char[outputLength];

                for (int i = (repeat * 2) - 2; i >= 0; i--, i--) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                }

                return new String(output2);

            default:

                StringBuffer buf = new StringBuffer(outputLength);

                for (int i = 0; i < repeat; i++) {
                    buf.append(str);
                }

                return buf.toString();
        }
    }

    /**
     * 扩展并左对齐字符串，用空格<code>' '</code>填充右边。
     * <p>
     * <pre>
     * StringUtil.alignLeft(null, *)   = null
     * StringUtil.alignLeft(&quot;&quot;, 3)     = &quot;   &quot;
     * StringUtil.alignLeft(&quot;bat&quot;, 3)  = &quot;bat&quot;
     * StringUtil.alignLeft(&quot;bat&quot;, 5)  = &quot;bat  &quot;
     * StringUtil.alignLeft(&quot;bat&quot;, 1)  = &quot;bat&quot;
     * StringUtil.alignLeft(&quot;bat&quot;, -1) = &quot;bat&quot;
     * </pre>
     *
     * @param str  要对齐的字符串
     * @param size 扩展字符串到指定宽度
     * @return 扩展后的字符串，如果字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String alignLeft(String str, int size) {
        return alignLeft(str, size, ' ');
    }

    /**
     * 扩展并左对齐字符串，用指定字符填充右边。
     * <p>
     * <pre>
     * StringUtil.alignLeft(null, *, *)     = null
     * StringUtil.alignLeft(&quot;&quot;, 3, 'z')     = &quot;zzz&quot;
     * StringUtil.alignLeft(&quot;bat&quot;, 3, 'z')  = &quot;bat&quot;
     * StringUtil.alignLeft(&quot;bat&quot;, 5, 'z')  = &quot;batzz&quot;
     * StringUtil.alignLeft(&quot;bat&quot;, 1, 'z')  = &quot;bat&quot;
     * StringUtil.alignLeft(&quot;bat&quot;, -1, 'z') = &quot;bat&quot;
     * </pre>
     *
     * @param str     要对齐的字符串
     * @param size    扩展字符串到指定宽度
     * @param padChar 填充字符
     * @return 扩展后的字符串，如果字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String alignLeft(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }

        int pads = size - str.length();

        if (pads <= 0) {
            return str;
        }

        return alignLeft(str, size, String.valueOf(padChar));
    }

    /**
     * 扩展并左对齐字符串，用指定字符串填充右边。
     * <p>
     * <pre>
     * StringUtil.alignLeft(null, *, *)      = null
     * StringUtil.alignLeft(&quot;&quot;, 3, &quot;z&quot;)      = &quot;zzz&quot;
     * StringUtil.alignLeft(&quot;bat&quot;, 3, &quot;yz&quot;)  = &quot;bat&quot;
     * StringUtil.alignLeft(&quot;bat&quot;, 5, &quot;yz&quot;)  = &quot;batyz&quot;
     * StringUtil.alignLeft(&quot;bat&quot;, 8, &quot;yz&quot;)  = &quot;batyzyzy&quot;
     * StringUtil.alignLeft(&quot;bat&quot;, 1, &quot;yz&quot;)  = &quot;bat&quot;
     * StringUtil.alignLeft(&quot;bat&quot;, -1, &quot;yz&quot;) = &quot;bat&quot;
     * StringUtil.alignLeft(&quot;bat&quot;, 5, null)  = &quot;bat  &quot;
     * StringUtil.alignLeft(&quot;bat&quot;, 5, &quot;&quot;)    = &quot;bat  &quot;
     * </pre>
     *
     * @param str    要对齐的字符串
     * @param size   扩展字符串到指定宽度
     * @param padStr 填充字符串
     * @return 扩展后的字符串，如果字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String alignLeft(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }

        if ((padStr == null) || (padStr.length() == 0)) {
            padStr = " ";
        }

        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;

        if (pads <= 0) {
            return str;
        }

        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();

            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }

            return str.concat(new String(padding));
        }
    }

    /**
     * 扩展并右对齐字符串，用空格<code>' '</code>填充左边。
     * <p>
     * <pre>
     * StringUtil.alignRight(null, *)   = null
     * StringUtil.alignRight(&quot;&quot;, 3)     = &quot;   &quot;
     * StringUtil.alignRight(&quot;bat&quot;, 3)  = &quot;bat&quot;
     * StringUtil.alignRight(&quot;bat&quot;, 5)  = &quot;  bat&quot;
     * StringUtil.alignRight(&quot;bat&quot;, 1)  = &quot;bat&quot;
     * StringUtil.alignRight(&quot;bat&quot;, -1) = &quot;bat&quot;
     * </pre>
     *
     * @param str  要对齐的字符串
     * @param size 扩展字符串到指定宽度
     * @return 扩展后的字符串，如果字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String alignRight(String str, int size) {
        return alignRight(str, size, ' ');
    }

    /**
     * 扩展并右对齐字符串，用指定字符填充左边。
     * <p>
     * <pre>
     * StringUtil.alignRight(null, *, *)     = null
     * StringUtil.alignRight(&quot;&quot;, 3, 'z')     = &quot;zzz&quot;
     * StringUtil.alignRight(&quot;bat&quot;, 3, 'z')  = &quot;bat&quot;
     * StringUtil.alignRight(&quot;bat&quot;, 5, 'z')  = &quot;zzbat&quot;
     * StringUtil.alignRight(&quot;bat&quot;, 1, 'z')  = &quot;bat&quot;
     * StringUtil.alignRight(&quot;bat&quot;, -1, 'z') = &quot;bat&quot;
     * </pre>
     *
     * @param str     要对齐的字符串
     * @param size    扩展字符串到指定宽度
     * @param padChar 填充字符
     * @return 扩展后的字符串，如果字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String alignRight(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }

        int pads = size - str.length();

        if (pads <= 0) {
            return str;
        }

        return alignRight(str, size, String.valueOf(padChar));
    }

    /**
     * 扩展并右对齐字符串，用指定字符串填充左边。
     * <p>
     * <pre>
     * StringUtil.alignRight(null, *, *)      = null
     * StringUtil.alignRight(&quot;&quot;, 3, &quot;z&quot;)      = &quot;zzz&quot;
     * StringUtil.alignRight(&quot;bat&quot;, 3, &quot;yz&quot;)  = &quot;bat&quot;
     * StringUtil.alignRight(&quot;bat&quot;, 5, &quot;yz&quot;)  = &quot;yzbat&quot;
     * StringUtil.alignRight(&quot;bat&quot;, 8, &quot;yz&quot;)  = &quot;yzyzybat&quot;
     * StringUtil.alignRight(&quot;bat&quot;, 1, &quot;yz&quot;)  = &quot;bat&quot;
     * StringUtil.alignRight(&quot;bat&quot;, -1, &quot;yz&quot;) = &quot;bat&quot;
     * StringUtil.alignRight(&quot;bat&quot;, 5, null)  = &quot;  bat&quot;
     * StringUtil.alignRight(&quot;bat&quot;, 5, &quot;&quot;)    = &quot;  bat&quot;
     * </pre>
     *
     * @param str    要对齐的字符串
     * @param size   扩展字符串到指定宽度
     * @param padStr 填充字符串
     * @return 扩展后的字符串，如果字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String alignRight(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }

        if ((padStr == null) || (padStr.length() == 0)) {
            padStr = " ";
        }

        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;

        if (pads <= 0) {
            return str;
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();

            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }

            return new String(padding).concat(str);
        }
    }

    /**
     * 扩展并居中字符串，用空格<code>' '</code>填充两边。
     * <p>
     * <pre>
     * StringUtil.center(null, *)   = null
     * StringUtil.center(&quot;&quot;, 4)     = &quot;    &quot;
     * StringUtil.center(&quot;ab&quot;, -1)  = &quot;ab&quot;
     * StringUtil.center(&quot;ab&quot;, 4)   = &quot; ab &quot;
     * StringUtil.center(&quot;abcd&quot;, 2) = &quot;abcd&quot;
     * StringUtil.center(&quot;a&quot;, 4)    = &quot; a  &quot;
     * </pre>
     *
     * @param str  要对齐的字符串
     * @param size 扩展字符串到指定宽度
     * @return 扩展后的字符串，如果字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String center(String str, int size) {
        return center(str, size, ' ');
    }

    /**
     * 扩展并居中字符串，用指定字符填充两边。
     * <p>
     * <pre>
     * StringUtil.center(null, *, *)     = null
     * StringUtil.center(&quot;&quot;, 4, ' ')     = &quot;    &quot;
     * StringUtil.center(&quot;ab&quot;, -1, ' ')  = &quot;ab&quot;
     * StringUtil.center(&quot;ab&quot;, 4, ' ')   = &quot; ab &quot;
     * StringUtil.center(&quot;abcd&quot;, 2, ' ') = &quot;abcd&quot;
     * StringUtil.center(&quot;a&quot;, 4, ' ')    = &quot; a  &quot;
     * StringUtil.center(&quot;a&quot;, 4, 'y')    = &quot;yayy&quot;
     * </pre>
     *
     * @param str     要对齐的字符串
     * @param size    扩展字符串到指定宽度
     * @param padChar 填充字符
     * @return 扩展后的字符串，如果字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String center(String str, int size, char padChar) {
        if ((str == null) || (size <= 0)) {
            return str;
        }

        int strLen = str.length();
        int pads = size - strLen;

        if (pads <= 0) {
            return str;
        }

        str = alignRight(str, strLen + (pads / 2), padChar);
        str = alignLeft(str, size, padChar);
        return str;
    }

    /**
     * 扩展并居中字符串，用指定字符串填充两边。
     * <p>
     * <pre>
     * StringUtil.center(null, *, *)     = null
     * StringUtil.center(&quot;&quot;, 4, &quot; &quot;)     = &quot;    &quot;
     * StringUtil.center(&quot;ab&quot;, -1, &quot; &quot;)  = &quot;ab&quot;
     * StringUtil.center(&quot;ab&quot;, 4, &quot; &quot;)   = &quot; ab &quot;
     * StringUtil.center(&quot;abcd&quot;, 2, &quot; &quot;) = &quot;abcd&quot;
     * StringUtil.center(&quot;a&quot;, 4, &quot; &quot;)    = &quot; a  &quot;
     * StringUtil.center(&quot;a&quot;, 4, &quot;yz&quot;)   = &quot;yayz&quot;
     * StringUtil.center(&quot;abc&quot;, 7, null) = &quot;  abc  &quot;
     * StringUtil.center(&quot;abc&quot;, 7, &quot;&quot;)   = &quot;  abc  &quot;
     * </pre>
     *
     * @param str    要对齐的字符串
     * @param size   扩展字符串到指定宽度
     * @param padStr 填充字符串
     * @return 扩展后的字符串，如果字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String center(String str, int size, String padStr) {
        if ((str == null) || (size <= 0)) {
            return str;
        }

        if ((padStr == null) || (padStr.length() == 0)) {
            padStr = " ";
        }

        int strLen = str.length();
        int pads = size - strLen;

        if (pads <= 0) {
            return str;
        }

        str = alignRight(str, strLen + (pads / 2), padStr);
        str = alignLeft(str, size, padStr);
        return str;
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 反转字符串。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 反转字符串中的字符顺序。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>null</code>。
     * </p>
     * <p>
     * <pre>
     * StringUtil.reverse(null)  = null
     * StringUtil.reverse(&quot;&quot;)    = &quot;&quot;
     * StringUtil.reverse(&quot;bat&quot;) = &quot;tab&quot;
     * </pre>
     *
     * @param str 要反转的字符串
     * @return 反转后的字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String reverse(String str) {
        if ((str == null) || (str.length() == 0)) {
            return str;
        }

        return new StringBuffer(str).reverse().toString();
    }

    /**
     * 反转指定分隔符分隔的各子串的顺序。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>null</code>。
     * </p>
     * <p>
     * <pre>
     * StringUtil.reverseDelimited(null, *)      = null
     * StringUtil.reverseDelimited(&quot;&quot;, *)        = &quot;&quot;
     * StringUtil.reverseDelimited(&quot;a.b.c&quot;, 'x') = &quot;a.b.c&quot;
     * StringUtil.reverseDelimited(&quot;a.b.c&quot;, '.') = &quot;c.b.a&quot;
     * </pre>
     *
     * @param str           要反转的字符串
     * @param separatorChar 分隔符
     * @return 反转后的字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String reverseDelimited(String str, char separatorChar) {
        if (str == null) {
            return null;
        }

        String[] strs = split(str, separatorChar);

        ArrayUtils.reverse(strs);

        return join(strs, separatorChar);
    }

    /**
     * 反转指定分隔符分隔的各子串的顺序。
     * <p>
     * <p>
     * 如果字符串为<code>null</code>，则返回<code>null</code>。
     * </p>
     * <p>
     * <pre>
     * StringUtil.reverseDelimited(null, *, *)          = null
     * StringUtil.reverseDelimited(&quot;&quot;, *, *)            = &quot;&quot;
     * StringUtil.reverseDelimited(&quot;a.b.c&quot;, null, null) = &quot;a.b.c&quot;
     * StringUtil.reverseDelimited(&quot;a.b.c&quot;, &quot;&quot;, null)   = &quot;a.b.c&quot;
     * StringUtil.reverseDelimited(&quot;a.b.c&quot;, &quot;.&quot;, &quot;,&quot;)   = &quot;c,b,a&quot;
     * StringUtil.reverseDelimited(&quot;a.b.c&quot;, &quot;.&quot;, null)  = &quot;c b a&quot;
     * </pre>
     *
     * @param str            要反转的字符串
     * @param separatorChars 分隔符，如果为<code>null</code>，则默认使用空白字符
     * @param separator      用来连接子串的分隔符，如果为<code>null</code>，默认使用空格
     * @return 反转后的字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String reverseDelimited(String str, String separatorChars,
                                          String separator) {
        if (str == null) {
            return null;
        }

        String[] strs = split(str, separatorChars);

        ArrayUtils.reverse(strs);

        if (separator == null) {
            return join(strs, ' ');
        }

        return join(strs, separator);
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 取得字符串的缩略。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 将字符串转换成指定长度的缩略，例如：
     * 将"Now is the time for all good men"转换成"Now is the time for..."。
     * <p>
     * <ul>
     * <li>
     * 如果<code>str</code>比<code>maxWidth</code>短，直接返回；</li>
     * <li>
     * 否则将它转换成缩略：<code>substring(str, 0, max-3) + "..."</code>；</li>
     * <li>
     * 如果<code>maxWidth</code>小于<code>4</code>抛出
     * <code>IllegalArgumentException</code>；</li>
     * <li>
     * 返回的字符串不可能长于指定的<code>maxWidth</code>。</li>
     * </ul>
     * <p>
     * <pre>
     * StringUtil.abbreviate(null, *)      = null
     * StringUtil.abbreviate(&quot;&quot;, 4)        = &quot;&quot;
     * StringUtil.abbreviate(&quot;abcdefg&quot;, 6) = &quot;abc...&quot;
     * StringUtil.abbreviate(&quot;abcdefg&quot;, 7) = &quot;abcdefg&quot;
     * StringUtil.abbreviate(&quot;abcdefg&quot;, 8) = &quot;abcdefg&quot;
     * StringUtil.abbreviate(&quot;abcdefg&quot;, 4) = &quot;a...&quot;
     * StringUtil.abbreviate(&quot;abcdefg&quot;, 3) = IllegalArgumentException
     * </pre>
     *
     * @param str      要检查的字符串
     * @param maxWidth 最大长度，不小于<code>4</code>，如果小于<code>4</code>，则看作<code>4</code>
     * @return 字符串缩略，如果原始字符串为<code>null</code>则返回<code>null</code>
     */
    public static String abbreviate(String str, int maxWidth) {
        return abbreviate(str, 0, maxWidth);
    }

    /**
     * 将字符串转换成指定长度的缩略，例如：
     * 将"Now is the time for all good men"转换成"...is the time for..."。
     * <p>
     * <p>
     * 和<code>abbreviate(String, int)</code>类似，但是增加了一个“左边界”偏移量。
     * 注意，“左边界”处的字符未必出现在结果字符串的最左边，但一定出现在结果字符串中。
     * </p>
     * <p>
     * <p>
     * 返回的字符串不可能长于指定的<code>maxWidth</code>。
     * <p>
     * <pre>
     * StringUtil.abbreviate(null, *, *)                = null
     * StringUtil.abbreviate(&quot;&quot;, 0, 4)                  = &quot;&quot;
     * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, -1, 10) = &quot;abcdefg...&quot;
     * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 0, 10)  = &quot;abcdefg...&quot;
     * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 1, 10)  = &quot;abcdefg...&quot;
     * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 4, 10)  = &quot;abcdefg...&quot;
     * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 5, 10)  = &quot;...fghi...&quot;
     * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 6, 10)  = &quot;...ghij...&quot;
     * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 8, 10)  = &quot;...ijklmno&quot;
     * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 10, 10) = &quot;...ijklmno&quot;
     * StringUtil.abbreviate(&quot;abcdefghijklmno&quot;, 12, 10) = &quot;...ijklmno&quot;
     * StringUtil.abbreviate(&quot;abcdefghij&quot;, 0, 3)        = IllegalArgumentException
     * StringUtil.abbreviate(&quot;abcdefghij&quot;, 5, 6)        = IllegalArgumentException
     * </pre>
     * <p>
     * </p>
     *
     * @param str      要检查的字符串
     * @param offset   左边界偏移量
     * @param maxWidth 最大长度，不小于<code>4</code>，如果小于<code>4</code>，则看作<code>4</code>
     * @return 字符串缩略，如果原始字符串为<code>null</code>则返回<code>null</code>
     */
    public static String abbreviate(String str, int offset, int maxWidth) {
        if (str == null) {
            return null;
        }

        // 调整最大宽度
        if (maxWidth < 4) {
            maxWidth = 4;
        }

        if (str.length() <= maxWidth) {
            return str;
        }

        if (offset > str.length()) {
            offset = str.length();
        }

        if ((str.length() - offset) < (maxWidth - 3)) {
            offset = str.length() - (maxWidth - 3);
        }

        if (offset <= 4) {
            return str.substring(0, maxWidth - 3) + "...";
        }

        // 调整最大宽度
        if (maxWidth < 7) {
            maxWidth = 7;
        }

        if ((offset + (maxWidth - 3)) < str.length()) {
            return "..." + abbreviate(str.substring(offset), maxWidth - 3);
        }

        return "..." + str.substring(str.length() - (maxWidth - 3));
    }

    /*
     * ==========================================================================
     * ==
     */
    /* 比较两个字符串的异同。 */
    /*                                                                              */
    /* 查找字符串之间的差异，比较字符串的相似度。 */
    /*
     * ==========================================================================
     * ==
     */

    /**
     * 比较两个字符串，取得第二个字符串中，和第一个字符串不同的部分。
     * <p>
     * <pre>
     * StringUtil.difference(&quot;i am a machine&quot;, &quot;i am a robot&quot;)  = &quot;robot&quot;
     * StringUtil.difference(null, null)                        = null
     * StringUtil.difference(&quot;&quot;, &quot;&quot;)                            = &quot;&quot;
     * StringUtil.difference(&quot;&quot;, null)                          = &quot;&quot;
     * StringUtil.difference(&quot;&quot;, &quot;abc&quot;)                         = &quot;abc&quot;
     * StringUtil.difference(&quot;abc&quot;, &quot;&quot;)                         = &quot;&quot;
     * StringUtil.difference(&quot;abc&quot;, &quot;abc&quot;)                      = &quot;&quot;
     * StringUtil.difference(&quot;ab&quot;, &quot;abxyz&quot;)                     = &quot;xyz&quot;
     * StringUtil.difference(&quot;abcde&quot;, &quot;abxyz&quot;)                  = &quot;xyz&quot;
     * StringUtil.difference(&quot;abcde&quot;, &quot;xyz&quot;)                    = &quot;xyz&quot;
     * </pre>
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 第二个字符串中，和第一个字符串不同的部分。如果两个字符串相同，则返回空字符串<code>""</code>
     */
    public static String difference(String str1, String str2) {
        if (str1 == null) {
            return str2;
        }

        if (str2 == null) {
            return str1;
        }

        int index = indexOfDifference(str1, str2);

        if (index == -1) {
            return EMPTY_STRING;
        }

        return str2.substring(index);
    }

    /**
     * 比较两个字符串，取得两字符串开始不同的索引值。
     * <p>
     * <pre>
     * StringUtil.indexOfDifference(&quot;i am a machine&quot;, &quot;i am a robot&quot;)   = 7
     * StringUtil.indexOfDifference(null, null)                         = -1
     * StringUtil.indexOfDifference(&quot;&quot;, null)                           = -1
     * StringUtil.indexOfDifference(&quot;&quot;, &quot;&quot;)                             = -1
     * StringUtil.indexOfDifference(&quot;&quot;, &quot;abc&quot;)                          = 0
     * StringUtil.indexOfDifference(&quot;abc&quot;, &quot;&quot;)                          = 0
     * StringUtil.indexOfDifference(&quot;abc&quot;, &quot;abc&quot;)                       = -1
     * StringUtil.indexOfDifference(&quot;ab&quot;, &quot;abxyz&quot;)                      = 2
     * StringUtil.indexOfDifference(&quot;abcde&quot;, &quot;abxyz&quot;)                   = 2
     * StringUtil.indexOfDifference(&quot;abcde&quot;, &quot;xyz&quot;)                     = 0
     * </pre>
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 两字符串开始产生差异的索引值，如果两字符串相同，则返回<code>-1</code>
     */
    public static int indexOfDifference(String str1, String str2) {
        if ((str1 == str2) || (str1 == null) || (str2 == null)) {
            return -1;
        }

        int i;

        for (i = 0; (i < str1.length()) && (i < str2.length()); ++i) {
            if (str1.charAt(i) != str2.charAt(i)) {
                break;
            }
        }

        if ((i < str2.length()) || (i < str1.length())) {
            return i;
        }

        return -1;
    }

    /**
     * 取得两个字符串的相似度，<code>0</code>代表字符串相等，数字越大表示字符串越不像。
     * <p>
     * <p>
     * 这个算法取自<a
     * href="http://www.merriampark.com/ld.htm">http://www.merriampark.com
     * /ld.htm</a>。 它计算的是从字符串1转变到字符串2所需要的删除、插入和替换的步骤数。
     * </p>
     * <p>
     * <pre>
     * StringUtil.getLevenshteinDistance(null, *)             = IllegalArgumentException
     * StringUtil.getLevenshteinDistance(*, null)             = IllegalArgumentException
     * StringUtil.getLevenshteinDistance(&quot;&quot;,&quot;&quot;)               = 0
     * StringUtil.getLevenshteinDistance(&quot;&quot;,&quot;a&quot;)              = 1
     * StringUtil.getLevenshteinDistance(&quot;aaapppp&quot;, &quot;&quot;)       = 7
     * StringUtil.getLevenshteinDistance(&quot;frog&quot;, &quot;fog&quot;)       = 1
     * StringUtil.getLevenshteinDistance(&quot;fly&quot;, &quot;ant&quot;)        = 3
     * StringUtil.getLevenshteinDistance(&quot;elephant&quot;, &quot;hippo&quot;) = 7
     * StringUtil.getLevenshteinDistance(&quot;hippo&quot;, &quot;elephant&quot;) = 7
     * StringUtil.getLevenshteinDistance(&quot;hippo&quot;, &quot;zzzzzzzz&quot;) = 8
     * StringUtil.getLevenshteinDistance(&quot;hello&quot;, &quot;hallo&quot;)    = 1
     * </pre>
     *
     * @param s 第一个字符串，如果是<code>null</code>，则看作空字符串
     * @param t 第二个字符串，如果是<code>null</code>，则看作空字符串
     * @return 相似度值
     */
    public static int getLevenshteinDistance(String s, String t) {
        s = defaultIfNull(s);
        t = defaultIfNull(t);

        int[][] d; // matrix
        int n; // length of s
        int m; // length of t
        int i; // iterates through s
        int j; // iterates through t
        char s_i; // ith character of s
        char t_j; // jth character of t
        int cost; // cost

        // Step 1
        n = s.length();
        m = t.length();

        if (n == 0) {
            return m;
        }

        if (m == 0) {
            return n;
        }

        d = new int[n + 1][m + 1];

        // Step 2
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        // Step 3
        for (i = 1; i <= n; i++) {
            s_i = s.charAt(i - 1);

            // Step 4
            for (j = 1; j <= m; j++) {
                t_j = t.charAt(j - 1);

                // Step 5
                if (s_i == t_j) {
                    cost = 0;
                } else {
                    cost = 1;
                }

                // Step 6
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1]
                        + cost);
            }
        }

        // Step 7
        return d[n][m];
    }

    /**
     * 取得最小数。
     *
     * @param a 整数1
     * @param b 整数2
     * @param c 整数3
     * @return 三个数中的最小值
     */
    private static int min(int a, int b, int c) {
        if (b < a) {
            a = b;
        }

        if (c < a) {
            a = c;
        }

        return a;
    }

    /**
     * 取出换行回车空格
     *
     * @param str
     * @return
     */
    public static String trimWithLine(String str) {
        str = StringUtils.replace(str, "\r", "");
        str = StringUtils.replace(str, "\n", "");
        return trim(str);
    }

    public static List<String> trimWithLine(List<String> strList) {
        if (strList == null)
            return null;
        for (int i = 0; i < strList.size(); i++) {
            strList.set(i, trimWithLine(strList.get(i)));
        }
        return strList;
    }

    public static Set<String> trimWithLine(Set<String> strList) {
        if (strList == null)
            return null;
        Iterator<String> iterator = strList.iterator();
        Set<String> returnSet = new HashSet<>();
        while (iterator.hasNext()) {
            returnSet.add(StringUtils.trim(iterator.next()));
        }
        return returnSet;
    }

    public static String[] trimWithLine(String[] strs) {
        if (strs == null) {
            return null;
        }
        for (int i = 0; i < strs.length; i++) {
            strs[i] = trimWithLine(strs[i]);
        }
        return strs;
    }

    public static final String FILL_TYPE_PREFIX = "prefix";

    public static final String FILL_TYPE_SUFFIX = "suffix";

    public static String filterHtml(String str) {
        return StringUtils.defaultIfEmpty(str).replaceAll("<[.[^<]]*>", "");
    }

    /**
     * 过滤非文本的HTML标签
     *
     * @param str
     * @return
     */
    public static String filterHtmlNoneTextElement(String str) {
        return StringUtils
                .defaultIfEmpty(str)
                .replaceAll(
                        "(?i)<script.*?>.*?</script>|</?iframe[^>]*>|<style.*?>.*?</style>",
                        "");
    }

    public static String trimWithHtml(String str) {
        str = replace(str, "&nbsp;", StringUtils.EMPTY_STRING);
        str = replace(str, HTML_BLANK, StringUtils.EMPTY_STRING);
        return trim(str);
    }

    public static int length(String str) {
        return str == null ? 0 : str.length();
    }

    // endsWith
    // -----------------------------------------------------------------------

    /**
     * <p>
     * Check if a String ends with a specified suffix.
     * </p>
     * <p>
     * <p>
     * <code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case sensitive.
     * </p>
     * <p>
     * <pre>
     * StringUtils.endsWith(null, null)      = true
     * StringUtils.endsWith(null, "abcdef")  = false
     * StringUtils.endsWith("def", null)     = false
     * StringUtils.endsWith("def", "abcdef") = true
     * StringUtils.endsWith("def", "ABCDEF") = false
     * </pre>
     *
     * @param str    the String to check, may be null
     * @param suffix the suffix to find, may be null
     * @return <code>true</code> if the String ends with the suffix, case
     * sensitive, or both <code>null</code>
     * @see String#endsWith(String)
     * @since 2.4
     */
    public static boolean endsWith(String str, String suffix) {
        return endsWith(str, suffix, false);
    }

    public static boolean endsWithAny(String str, String... suffixs) {
        for (String suffix : suffixs) {
            if (endsWith(str, suffix)) {
                return true;
            }
        }
        return false;
    }

    public static boolean endsWithAny(String str, Collection<String> suffixs) {
        for (String suffix : suffixs) {
            if (endsWith(str, suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Case insensitive check if a String ends with a specified suffix.
     * </p>
     * <p>
     * <p>
     * <code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case
     * insensitive.
     * </p>
     * <p>
     * <pre>
     * StringUtils.endsWithIgnoreCase(null, null)      = true
     * StringUtils.endsWithIgnoreCase(null, "abcdef")  = false
     * StringUtils.endsWithIgnoreCase("def", null)     = false
     * StringUtils.endsWithIgnoreCase("def", "abcdef") = true
     * StringUtils.endsWithIgnoreCase("def", "ABCDEF") = false
     * </pre>
     *
     * @param str    the String to check, may be null
     * @param suffix the suffix to find, may be null
     * @return <code>true</code> if the String ends with the suffix, case
     * insensitive, or both <code>null</code>
     * @see String#endsWith(String)
     * @since 2.4
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return endsWith(str, suffix, true);
    }

    /**
     * <p>
     * Check if a String ends with a specified suffix (optionally case
     * insensitive).
     * </p>
     *
     * @param str        the String to check, may be null
     * @param suffix     the suffix to find, may be null
     * @param ignoreCase inidicates whether the compare should ignore case (case
     *                   insensitive) or not.
     * @return <code>true</code> if the String starts with the prefix or both
     * <code>null</code>
     * @see String#endsWith(String)
     */
    private static boolean endsWith(String str, String suffix,
                                    boolean ignoreCase) {
        if (str == null || suffix == null) {
            return (str == null && suffix == null);
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        int strOffset = str.length() - suffix.length();
        return str.regionMatches(ignoreCase, strOffset, suffix, 0,
                suffix.length());
    }

    // startsWith
    // -----------------------------------------------------------------------

    /**
     * <p>
     * Check if a String starts with a specified prefix.
     * </p>
     * <p>
     * <p>
     * <code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case sensitive.
     * </p>
     * <p>
     * <pre>
     * StringUtils.startsWith(null, null)      = true
     * StringUtils.startsWith(null, "abcdef")  = false
     * StringUtils.startsWith("abc", null)     = false
     * StringUtils.startsWith("abc", "abcdef") = true
     * StringUtils.startsWith("abc", "ABCDEF") = false
     * </pre>
     *
     * @param str    the String to check, may be null
     * @param prefix the prefix to find, may be null
     * @return <code>true</code> if the String starts with the prefix, case
     * sensitive, or both <code>null</code>
     * @see String#startsWith(String)
     * @since 2.4
     */
    public static boolean startsWith(String str, String prefix) {
        return startsWith(str, prefix, false);
    }

    /**
     * @param str
     * @param prefixs
     * @return
     */
    public static boolean startsWithAny(String str, String[] prefixs) {
        if (ArrayUtils.getLength(prefixs) == 0) {
            return false;
        }
        for (String prefix : prefixs) {
            if (startsWith(str, prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Case insensitive check if a String starts with a specified prefix.
     * </p>
     * <p>
     * <p>
     * <code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case
     * insensitive.
     * </p>
     * <p>
     * <pre>
     * StringUtils.startsWithIgnoreCase(null, null)      = true
     * StringUtils.startsWithIgnoreCase(null, "abcdef")  = false
     * StringUtils.startsWithIgnoreCase("abc", null)     = false
     * StringUtils.startsWithIgnoreCase("abc", "abcdef") = true
     * StringUtils.startsWithIgnoreCase("abc", "ABCDEF") = true
     * </pre>
     *
     * @param str    the String to check, may be null
     * @param prefix the prefix to find, may be null
     * @return <code>true</code> if the String starts with the prefix, case
     * insensitive, or both <code>null</code>
     * @see String#startsWith(String)
     * @since 2.4
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return startsWith(str, prefix, true);
    }

    /**
     * <p>
     * Check if a String starts with a specified prefix (optionally case
     * insensitive).
     * </p>
     *
     * @param str        the String to check, may be null
     * @param prefix     the prefix to find, may be null
     * @param ignoreCase inidicates whether the compare should ignore case (case
     *                   insensitive) or not.
     * @return <code>true</code> if the String starts with the prefix or both
     * <code>null</code>
     * @see String#startsWith(String)
     */
    private static boolean startsWith(String str, String prefix,
                                      boolean ignoreCase) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
    }

    /**
     * 返回text的从0开始的length长度内容,<br>
     * 如果text长度小于等于length,则直接返回text, <br>
     * 大于,则截取length长度，并填充text
     *
     * @param text
     * @param length
     * @param fill
     * @return
     */
    public static String front(String text, int length, String fill) {
        if (StringUtils.length(text) <= length) {
            return text;
        }
        return StringUtils.substring(text, 0, length) + fill;
    }

    /**
     * 是否DB字段名
     *
     * @param fieldName
     * @return
     */
    public static final boolean isDbFiledName(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            return false;
        }

        for (char c : fieldName.toCharArray()) {
            if (!(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_' || c >= '0'
                    && c <= '9')) {
                return false;
            }
        }
        return true;
    }

    /**
     * 转换为小写
     *
     * @param collection
     * @return
     */
    public static List<String> toLowerCase(List<String> collection) {
        if (collection == null)
            return null;
        List<String> returnList = new ArrayList<>();
        for (String value : collection) {
            returnList.add(StringUtils.toLowerCase(value));
        }
        return returnList;
    }

    /**
     * 转换为小写
     *
     * @param collection
     * @return
     */
    public static Set<String> toLowerCase(Set<String> collection) {
        if (collection == null)
            return null;
        Set<String> returnSet = new HashSet<>();
        for (String value : collection) {
            returnSet.add(StringUtils.toLowerCase(value));
        }
        return returnSet;
    }

    /**
     * 转换为小写
     *
     * @param collection
     * @return
     */
    public static String[] toLowerCase(String[] values) {
        if (values == null)
            return null;
        String[] returnValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            returnValues[i] = StringUtils.toLowerCase(values[i]);
        }
        return returnValues;
    }

    /**
     * 转换为大写
     *
     * @param collection
     * @return
     */
    public static List<String> toUpperCase(List<String> collection) {
        if (collection == null)
            return null;
        List<String> returnList = new ArrayList<>();
        for (String value : collection) {
            returnList.add(StringUtils.toUpperCase(value));
        }
        return returnList;
    }

    /**
     * 转换为大写
     *
     * @param collection
     * @return
     */
    public static Set<String> toUpperCase(Set<String> collection) {
        if (collection == null)
            return null;
        Set<String> returnSet = new HashSet<>();
        for (String value : collection) {
            returnSet.add(StringUtils.toUpperCase(value));
        }
        return returnSet;
    }

    /**
     * 转换为小写
     *
     * @param collection
     * @return
     */
    public static String[] toUpperCase(String[] values) {
        if (values == null)
            return null;
        String[] returnValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            returnValues[i] = StringUtils.toUpperCase(values[i]);
        }
        return returnValues;
    }

    /**
     * 判断value中是否全部是containValue
     *
     * @param value
     * @param containValue
     * @return
     */
    public static boolean isAll(String value, String containValue) {
        if (StringUtils.equals(value, containValue)) {
            return true;
        }
        if (StringUtils.isEmpty(value) || StringUtils.isEmpty(containValue)) {
            return false;
        }

        if (value.length() % containValue.length() != 0) {
            return false;
        }

        int count = value.length() / containValue.length();
        for (int i = 0; i < count; i++) {
            if (!StringUtils.equals(
                    containValue,
                    value.substring(i * containValue.length(), (i + 1)
                            * containValue.length()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 16进制字符串转字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 连接字符换
     *
     * @param strings
     * @return
     */
    public static String joinString(Object... strings) {
        if (strings == null || strings.length == 0) {
            return null;
        }
        StringBuffer retBuffer = new StringBuffer();
        for (Object string : strings) {
            retBuffer.append(string);
        }
        return retBuffer.toString();
    }

    /**
     * 连接字符换
     *
     * @param strings
     * @return
     */
    public static String joinString(String... strings) {
        if (strings == null || strings.length == 0) {
            return null;
        }
        StringBuffer retBuffer = new StringBuffer();
        for (Object string : strings) {
            retBuffer.append(string);
        }
        return retBuffer.toString();
    }

    /**
     * 连接字符换
     *
     * @param strings
     * @return
     */
    public static String joinWithSeparator(Object... strings) {
        if (strings == null || strings.length == 0) {
            return null;
        }
        StringBuffer retBuffer = new StringBuffer();
        for (int i = 0; i < strings.length - 1; i++) {
            retBuffer.append(strings[i]);
            if (i <= strings.length - 3)
                retBuffer.append(strings[strings.length - 1]);
        }
        return retBuffer.toString();
    }

    /**
     * <p>
     * Replaces all occurrences of Strings within another String.
     * </p>
     * <p>
     * <p>
     * A <code>null</code> reference passed to this method is a no-op, or if any
     * "search string" or "string to replace" is null, that replace will be
     * ignored. This will not repeat. For repeating replaces, call the
     * overloaded method.
     * </p>
     * <p>
     * <pre>
     *  StringUtils.replaceEach(null, *, *)        = null
     *  StringUtils.replaceEach("", *, *)          = ""
     *  StringUtils.replaceEach("aba", null, null) = "aba"
     *  StringUtils.replaceEach("aba", new String[0], null) = "aba"
     *  StringUtils.replaceEach("aba", null, new String[0]) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, null)  = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, new String[]{""})  = "b"
     *  StringUtils.replaceEach("aba", new String[]{null}, new String[]{"a"})  = "aba"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"})  = "wcte"
     *  (example of how it does not repeat)
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"})  = "dcte"
     * </pre>
     *
     * @param text            text to search and replace in, no-op if null
     * @param searchList      the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     * @return the text with any replacements processed, <code>null</code> if
     * null String input
     * @throws IndexOutOfBoundsException if the lengths of the arrays are not the same (null is ok,
     *                                   and/or size 0)
     * @since 2.4
     */
    public static String replaceEach(String text, String[] searchList,
                                     String[] replacementList) {
        return replaceEach(text, searchList, replacementList, false, 0);
    }

    /**
     * <p>
     * Replaces all occurrences of Strings within another String.
     * </p>
     * <p>
     * <p>
     * A <code>null</code> reference passed to this method is a no-op, or if any
     * "search string" or "string to replace" is null, that replace will be
     * ignored.
     * </p>
     * <p>
     * <pre>
     *  StringUtils.replaceEach(null, *, *, *) = null
     *  StringUtils.replaceEach("", *, *, *) = ""
     *  StringUtils.replaceEach("aba", null, null, *) = "aba"
     *  StringUtils.replaceEach("aba", new String[0], null, *) = "aba"
     *  StringUtils.replaceEach("aba", null, new String[0], *) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, null, *) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
     *  StringUtils.replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
     *  (example of how it repeats)
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, *) = IllegalArgumentException
     * </pre>
     *
     * @param text            text to search and replace in, no-op if null
     * @param searchList      the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     * @param repeat          if true, then replace repeatedly until there are no more
     *                        possible replacements or timeToLive < 0
     * @param timeToLive      if less than 0 then there is a circular reference and endless
     *                        loop
     * @return the text with any replacements processed, <code>null</code> if
     * null String input
     * @throws IllegalArgumentException  if the search is repeating and there is an endless loop due
     *                                   to outputs of one being inputs to another
     * @throws IndexOutOfBoundsException if the lengths of the arrays are not the same (null is ok,
     *                                   and/or size 0)
     * @since 2.4
     */
    private static String replaceEach(String text, String[] searchList,
                                      String[] replacementList, boolean repeat, int timeToLive) {

        // mchyzer Performance note: This creates very few new objects (one
        // major goal)
        // let me know if there are performance requests, we can create a
        // harness to measure

        if (text == null || text.length() == 0 || searchList == null
                || searchList.length == 0 || replacementList == null
                || replacementList.length == 0) {
            return text;
        }

        // if recursing, this shouldnt be less than 0
        if (timeToLive < 0) {
            throw new IllegalStateException("TimeToLive of " + timeToLive
                    + " is less than 0: " + text);
        }

        int searchLength = searchList.length;
        int replacementLength = replacementList.length;

        // make sure lengths are ok, these need to be equal
        if (searchLength != replacementLength) {
            throw new IllegalArgumentException(
                    "Search and Replace array lengths don't match: "
                            + searchLength + " vs " + replacementLength);
        }

        // keep track of which still have matches
        boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

        // index on index that the match was found
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex = -1;

        // index of replace array that will replace the search string found
        // NOTE: logic duplicated below START
        for (int i = 0; i < searchLength; i++) {
            if (noMoreMatchesForReplIndex[i] || searchList[i] == null
                    || searchList[i].length() == 0
                    || replacementList[i] == null) {
                continue;
            }
            tempIndex = text.indexOf(searchList[i]);

            // see if we need to keep searching for this
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            } else {
                if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
        // NOTE: logic mostly below END

        // no search strings found, we are done
        if (textIndex == -1) {
            return text;
        }

        int start = 0;

        // get a good guess on the size of the result buffer so it doesnt have
        // to double if it goes over a bit
        int increase = 0;

        // count the replacement text elements that are larger than their
        // corresponding text being replaced
        for (int i = 0; i < searchList.length; i++) {
            int greater = replacementList[i].length() - searchList[i].length();
            if (greater > 0) {
                increase += 3 * greater; // assume 3 matches
            }
        }
        // have upper-bound at 20% increase, then let Java take over
        increase = Math.min(increase, text.length() / 5);

        StringBuffer buf = new StringBuffer(text.length() + increase);

        while (textIndex != -1) {

            for (int i = start; i < textIndex; i++) {
                buf.append(text.charAt(i));
            }
            buf.append(replacementList[replaceIndex]);

            start = textIndex + searchList[replaceIndex].length();

            textIndex = -1;
            replaceIndex = -1;
            tempIndex = -1;
            // find the ok earliest match
            // NOTE: logic mostly duplicated above START
            for (int i = 0; i < searchLength; i++) {
                if (noMoreMatchesForReplIndex[i] || searchList[i] == null
                        || searchList[i].length() == 0
                        || replacementList[i] == null) {
                    continue;
                }
                tempIndex = text.indexOf(searchList[i], start);

                // see if we need to keep searching for this
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i] = true;
                } else {
                    if (textIndex == -1 || tempIndex < textIndex) {
                        textIndex = tempIndex;
                        replaceIndex = i;
                    }
                }
            }
            // NOTE: logic duplicated above END

        }
        int textLength = text.length();
        for (int i = start; i < textLength; i++) {
            buf.append(text.charAt(i));
        }
        String result = buf.toString();
        if (!repeat) {
            return result;
        }

        return replaceEach(result, searchList, replacementList, repeat,
                timeToLive - 1);
    }

    /**
     * <p>
     * Replaces all occurrences of Strings within another String.
     * </p>
     * <p>
     * <p>
     * A <code>null</code> reference passed to this method is a no-op, or if any
     * "search string" or "string to replace" is null, that replace will be
     * ignored. This will not repeat. For repeating replaces, call the
     * overloaded method.
     * </p>
     * <p>
     * <pre>
     *  StringUtils.replaceEach(null, *, *, *) = null
     *  StringUtils.replaceEach("", *, *, *) = ""
     *  StringUtils.replaceEach("aba", null, null, *) = "aba"
     *  StringUtils.replaceEach("aba", new String[0], null, *) = "aba"
     *  StringUtils.replaceEach("aba", null, new String[0], *) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, null, *) = "aba"
     *  StringUtils.replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
     *  StringUtils.replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
     *  (example of how it repeats)
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, true) = IllegalArgumentException
     *  StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, false) = "dcabe"
     * </pre>
     *
     * @param text            text to search and replace in, no-op if null
     * @param searchList      the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     * @return the text with any replacements processed, <code>null</code> if
     * null String input
     * @throws IllegalArgumentException  if the search is repeating and there is an endless loop due
     *                                   to outputs of one being inputs to another
     * @throws IndexOutOfBoundsException if the lengths of the arrays are not the same (null is ok,
     *                                   and/or size 0)
     * @since 2.4
     */
    public static String replaceEachRepeatedly(String text,
                                               String[] searchList, String[] replacementList) {
        // timeToLive should be 0 if not used or nothing to replace, else it's
        // the length of the replace array
        int timeToLive = searchList == null ? 0 : searchList.length;
        return replaceEach(text, searchList, replacementList, true, timeToLive);
    }

    /**
     * Check whether the given CharSequence contains any whitespace characters.
     *
     * @param str the CharSequence to check (may be <code>null</code>)
     * @return <code>true</code> if the CharSequence is not empty and contains
     * at least 1 whitespace character
     * @see Character#isWhitespace
     */
    public static boolean containsWhitespace(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given String contains any whitespace characters.
     *
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not empty and contains at
     * least 1 whitespace character
     * @see #containsWhitespace(CharSequence)
     */
    public static boolean containsWhitespace(String str) {
        return containsWhitespace((CharSequence) str);
    }

    /**
     * Trim leading and trailing whitespace from the given String.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    public static String trimWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str);
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
            buf.deleteCharAt(0);
        }
        while (buf.length() > 0
                && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    /**
     * Trim <i>all</i> whitespace from the given String: leading, trailing, and
     * inbetween characters.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str);
        int index = 0;
        while (buf.length() > index) {
            if (Character.isWhitespace(buf.charAt(index))) {
                buf.deleteCharAt(index);
            } else {
                index++;
            }
        }
        return buf.toString();
    }

    /**
     * Trim leading whitespace from the given String.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    public static String trimLeadingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str);
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
            buf.deleteCharAt(0);
        }
        return buf.toString();
    }

    /**
     * Trim trailing whitespace from the given String.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    public static String trimTrailingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str);
        while (buf.length() > 0
                && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    /**
     * Trim all occurences of the supplied leading character from the given
     * String.
     *
     * @param str              the String to check
     * @param leadingCharacter the leading character to be trimmed
     * @return the trimmed String
     */
    public static String trimLeadingCharacter(String str, char leadingCharacter) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str);
        while (buf.length() > 0 && buf.charAt(0) == leadingCharacter) {
            buf.deleteCharAt(0);
        }
        return buf.toString();
    }

    /**
     * Trim all occurences of the supplied trailing character from the given
     * String.
     *
     * @param str               the String to check
     * @param trailingCharacter the trailing character to be trimmed
     * @return the trimmed String
     */
    public static String trimTrailingCharacter(String str,
                                               char trailingCharacter) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuffer buf = new StringBuffer(str);
        while (buf.length() > 0
                && buf.charAt(buf.length() - 1) == trailingCharacter) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    /**
     * Check that the given CharSequence is neither <code>null</code> nor of
     * length 0. Note: Will return <code>true</code> for a CharSequence that
     * purely consists of whitespace.
     * <p>
     * <p>
     * <pre>
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength("Hello") = true
     * </pre>
     *
     * @param str the CharSequence to check (may be <code>null</code>)
     * @return <code>true</code> if the CharSequence is not null and has length
     * @see #hasText(String)
     */
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Check that the given String is neither <code>null</code> nor of length 0.
     * Note: Will return <code>true</code> for a String that purely consists of
     * whitespace.
     *
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not null and has length
     * @see #hasLength(CharSequence)
     */
    public static boolean hasLength(String str) {
        return hasLength((CharSequence) str);
    }

    /**
     * Check whether the given CharSequence has actual text. More specifically,
     * returns <code>true</code> if the string not <code>null</code>, its length
     * is greater than 0, and it contains at least one non-whitespace character.
     * <p>
     * <p>
     * <pre>
     * StringUtils.hasText(null) = false
     * StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false
     * StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
     * </pre>
     *
     * @param str the CharSequence to check (may be <code>null</code>)
     * @return <code>true</code> if the CharSequence is not <code>null</code>,
     * its length is greater than 0, and it does not contain whitespace
     * only
     * @see Character#isWhitespace
     */
    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given String has actual text. More specifically,
     * returns <code>true</code> if the string not <code>null</code>, its length
     * is greater than 0, and it contains at least one non-whitespace character.
     *
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not <code>null</code>, its
     * length is greater than 0, and it does not contain whitespace only
     * @see #hasText(CharSequence)
     */
    public static boolean hasText(String str) {
        return hasText((CharSequence) str);
    }

    /**
     * Match a String against the given pattern, supporting the following simple
     * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy" matches (with an
     * arbitrary number of pattern parts), as well as direct equality.
     *
     * @param pattern the pattern to match against
     * @param str     the String to match
     * @return whether the String matches the given pattern
     */
    public static boolean isSimpleMatch(String pattern, String str) {
        if (pattern == null || str == null) {
            return false;
        }
        int firstIndex = pattern.indexOf('*');
        if (firstIndex == -1) {
            return pattern.equals(str);
        }
        if (firstIndex == 0) {
            if (pattern.length() == 1) {
                return true;
            }
            int nextIndex = pattern.indexOf('*', firstIndex + 1);
            if (nextIndex == -1) {
                return str.endsWith(pattern.substring(1));
            }
            String part = pattern.substring(1, nextIndex);
            int partIndex = str.indexOf(part);
            while (partIndex != -1) {
                if (isSimpleMatch(pattern.substring(nextIndex),
                        str.substring(partIndex + part.length()))) {
                    return true;
                }
                partIndex = str.indexOf(part, partIndex + 1);
            }
            return false;
        }
        return (str.length() >= firstIndex
                && pattern.substring(0, firstIndex).equals(
                str.substring(0, firstIndex)) && isSimpleMatch(
                pattern.substring(firstIndex), str.substring(firstIndex)));
    }

    /**
     * Match a String against the given patterns, supporting the following
     * simple pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy" matches
     * (with an arbitrary number of pattern parts), as well as direct equality.
     *
     * @param patterns the patterns to match against
     * @param str      the String to match
     * @return whether the String matches any of the given patterns
     */
    public static boolean isSimpleMatch(String[] patterns, String str) {
        if (patterns != null) {
            for (String pattern : patterns) {
                if (isSimpleMatch(pattern, str)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否合法的变量名，字母数字下划线组成，且不可以数字开头
     *
     * @param variableName
     * @return
     */

    public static boolean isVariableName(String variableName) {
        if (StringUtils.isEmpty(variableName)) {
            return false;
        }
        return variableName.matches("[_a-zA-Z][a-zA-Z0-9_]*");
    }

    /**
     * 返回startFlag,endFlag之间的字符串
     *
     * @param sourceString
     * @param startFlag
     * @param endFlag
     * @return
     */
    public static String find(String sourceString, String startFlag,
                              String endFlag) {
        int startIndex = 0;
        if (!StringUtils.isEmpty(startFlag)) {
            startIndex = sourceString.indexOf(startFlag);
            if (startIndex < 0) {
                return null;
            }
        }

        int endIndex = sourceString.length();
        if (!StringUtils.isEmpty(endFlag)) {
            endIndex = sourceString.indexOf(endFlag,
                    startIndex + startFlag.length());
            if (endIndex < 0) {
                return null;
            }
        }
        return sourceString
                .substring(startIndex + startFlag.length(), endIndex);
    }

    /**
     * 根据开始字符串从数组中获取以这个字符串开始的字符串
     *
     * @param values
     * @param startsWith
     * @return
     */
    public static List<String> getStartsWith(String[] values, String startsWith) {
        if (values == null) {
            return null;
        }
        List<String> returnList = new ArrayList<>();
        for (String value : values) {
            if (StringUtils.startsWith(value, startsWith)) {
                returnList.add(value);
            }
        }
        return returnList;
    }

    /**
     * @param string
     * @param charset
     * @return
     */
    public static byte[] getBytes(String string, String charset) {
        if (StringUtils.isEmpty(string)) {
            return null;
        }
        try {
            return string.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }


}
