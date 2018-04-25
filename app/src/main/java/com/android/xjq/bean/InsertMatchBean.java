package com.android.xjq.bean;

import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhouyi on 2015/12/21 14:57.
 */
public class InsertMatchBean {

    private ArrayList<JczqDataBean> dataList;
    public ArrayList<JczqDataBean> footballRaceList;
    public ArrayList<JczqDataBean> basketballRaceList;

    public InsertMatchBean(JSONObject jo) throws JSONException {

        dataList = new ArrayList<>();
        if (jo.has("dataList")) {
            JSONArray array = jo.getJSONArray("dataList");
            for (int i = 0; i < array.length(); i++) {
                String str = array.getJSONObject(i).toString();
                JczqDataBean bean = new Gson().fromJson(str, JczqDataBean.class);
                dataList.add(bean);
            }
        }

    }

    public InsertMatchBean() {
    }

    public ArrayList<JczqDataBean> getDataList() {
        return dataList;
    }

}
