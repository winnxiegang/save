package com.android.banana.commlib.bean.liveScoreBean;

import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.android.banana.commlib.view.CenterVerticalImageSpan;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaomu on 2017/8/16.
 */

public class PathAnimLiveSourceBean {

    /**
     * addressType : {"message":"h5页面","name":"H5","value":0}
     * enabled : true
     * id : 26
     * la : http://www.jczj123.com/lottery/jczq/jczqSpfHhgg.htm
     * ln : TEST21
     * pt : {"message":"火红社直播","name":"HUO_HONG_SHE","value":0}
     * rid : 25307884
     */

    public AddressType addressType;
    public boolean enabled;
    public int id;
    @SerializedName("la")
    public String h5Link;
    @SerializedName("ln")
    public String h5LinkName;
    @SerializedName("pt")
    public Title mTitle;
    public int rid;


    public static class AddressType {
        /**
         * message : h5页面
         * name : H5
         * value : 0
         */

        public String message;
        public String name;
        public int value;
    }

    public static class Title {
        /**
         * message : 火红社直播
         * name : HUO_HONG_SHE
         * value : 0
         */

        public String message;
        public String name;
        public int value;
    }

    public static List<PathAnimLiveSourceBean> parse(JSONObject jo) {
        if (jo.has("liveList")) {
            try {
                JSONArray array = jo.getJSONArray("liveList");
                Type type = new TypeToken<ArrayList<PathAnimLiveSourceBean>>() {
                }.getType();
                List<PathAnimLiveSourceBean> liveList = new Gson().fromJson(array.toString(), type);
                return liveList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static List<CharSequence> getList(List<PathAnimLiveSourceBean> list, Drawable spanDrawable, String spanString) {
        if (list == null || list.size() == 0)
            return null;
        List<CharSequence> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Title title = list.get(i).mTitle;
            newList.add(title.message + "(" + list.get(i).h5LinkName + ")");
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();

        SpannableString ss = new SpannableString("source");
        spanDrawable.setBounds(0, 0, spanDrawable.getIntrinsicWidth(), spanDrawable.getIntrinsicHeight());
        ss.setSpan(new CenterVerticalImageSpan(spanDrawable), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(ss);
        ssb.append("  "+spanString);
        newList.add(ssb);

        return newList;
    }
}
