package com.android.banana.commlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by lingjiu on 2017/4/1.
 */

public class SharePreferenceUtils {


    private static final String DATA = "hhs_data";

    public static final String HISTORY_SEARCH_RECORD = "history_search_record";

    public static final String IS_PLAY_VIDEO = "is_play_video";

    public static final String APP_DEVICE_ID = "app_device_id";

    public static final String SOFT_KEYBOARD_HEIGHT = "soft_keyboard_height";

    public static final String GET_NEW_FANS_MEDAL_ID = "get_new_fans_medal_id";

    public static final String TEST_NETWORK_URL = "test_network_url";

    private static Context sContext;

    /**
     * @param key
     * @param object
     */
    public static void putData(String key, Object object) {

        SharedPreferences sp = getSharePreference();

        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {

            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {

            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {

            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {

            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {

            editor.putLong(key, (Long) object);
        } else {

            editor.putString(key, object.toString());
        }

        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getData( String key, Object defaultObject) {
        SharedPreferences sp = getSharePreference();

        if (defaultObject == null || defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences sp = getSharePreference();
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     *
     */
    public static void clear() {
        SharedPreferences sp = getSharePreference();
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        SharedPreferences sp = getSharePreference();
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public static Map<String, ?> getAll() {
        SharedPreferences sp = getSharePreference();
        return sp.getAll();
    }

    public static void init(Context context){
        sContext = context;
    }

    private static SharedPreferences getSharePreference() {

        SharedPreferences sp = sContext.getSharedPreferences(DATA, Context.MODE_PRIVATE);

        return sp;

    }


}
