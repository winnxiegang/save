package com.android.banana.commlib.emoji;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyi on 2016/1/6 12:11.
 */
public class EmojUtils {


    public static String list = "[{'name':'红单','url':'emotion/normal/hongdan.gif'},\n" +
            "\t\t\t\t          {'name':'黑单','url':'emotion/normal/heidan.gif'},\n" +
            "\t\t\t\t          {'name':'打脸','url':'emotion/normal/dl.gif'},\n" +
            "\t\t\t\t          {'name':'笑着哭','url':'emotion/normal/xzk.gif'},\n" +
            "\t\t\t\t          {'name':'拜托','url':'emotion/normal/bt.gif'},\n" +
            "\t\t\t\t{'name':'得意','url':'emotion/normal/dy.gif'},\n" +
            "\t\t\t\t{'name':'惊讶','url':'emotion/normal/jy.gif'},\n" +
            "\t\t\t\t{'name':'mmp','url':'emotion/normal/mmp.gif'},\n" +
            "\t\t\t\t{'name':'国旗','url':'emotion/normal/guoqi.gif'},\n" +
            "\t\t\t\t{'name':'喜欢','url':'emotion/normal/like.gif'},\n" +
            "\t\t\t\t{'name':'哭','url':'emotion/normal/cry.gif'},\n" +
            "\t\t\t\t{'name':'XJBT','url':'emotion/normal/xjbt.gif'},\n" +
            "\t\t\t\t{'name':'抠鼻','url':'emotion/normal/kb.gif'},\n" +
            "\t\t\t\t{'name':'生气','url':'emotion/normal/angry.gif'},\n" +
            "\t\t\t\t{'name':'稳如狗','url':'emotion/normal/wrg.gif'},\n" +
            "\t\t\t\t{'name':'红牌','url':'emotion/normal/rc.gif'},\n" +
            "\t\t\t\t{'name':'黄牌','url':'emotion/normal/yc.gif'},\n" +
            "\t\t\t\t{'name':'红包','url':'emotion/normal/coupon.gif'},\n" +
            "\t\t\t\t{'name':'刀','url':'emotion/normal/knife.gif'},\n" +
            "\t\t\t\t{'name':'进球','url':'emotion/normal/goal.gif'},\n" +
            "\t\t\t\t{'name':'炸弹','url':'emotion/normal/boom.gif'},\n" +
            "\t\t\t\t{'name':'篮球','url':'emotion/normal/lq.gif'},\n" +
            "\t\t\t\t{'name':'足球','url':'emotion/normal/zq.gif'},\n" +
            "\t\t\t\t{'name':'吃瓜群众','url':'emotion/normal/cgqz.gif'},\n" +
            "\t\t\t\t{'name':'发财','url':'emotion/normal/facai.gif'},\n" +
            "\t\t\t\t{'name':'红中','url':'emotion/normal/hongzhong.gif'},\n" +
            "\t\t\t\t{'name':'v5','url':'emotion/normal/v5.gif'},\n" +
            "\t\t\t\t{'name':'啤酒','url':'emotion/normal/beer.gif'},\n" +
            "\t\t\t\t{'name':'庆祝','url':'emotion/normal/qz.gif'},\n" +
            "\t\t\t\t{'name':'明灯','url':'emotion/normal/md.gif'},\n" +
            "\t\t\t\t{'name':'厉害','url':'emotion/normal/good.gif'},\n" +
            "\t\t\t\t{'name':'举手','url':'emotion/normal/js.gif'},\n" +
            "\t\t\t\t{'name':'假球','url':'emotion/normal/jiaqiu.gif'},\n" +
            "\t\t\t\t{'name':'国','url':'emotion/normal/guo.gif'},\n" +
            "\t\t\t\t{'name':'足','url':'emotion/normal/zu.gif'},\n" +
            "\t\t\t\t{'name':'必','url':'emotion/normal/bi.gif'},\n" +
            "\t\t\t\t{'name':'胜','url':'emotion/normal/sheng.gif'},\n" +
            "\t\t\t\t{'name':'加','url':'emotion/normal/jia.gif'},\n" +
            "\t\t\t\t{'name':'油','url':'emotion/normal/you.gif'},\n" +
            "\t\t\t\t{'name':'撸串','url':'emotion/normal/lc.gif'},\n" +
            "\t\t\t\t{'name':'鸡腿','url':'emotion/normal/chicken.gif'},\n" +
            "\t\t\t\t{'name':'跪滑','url':'emotion/normal/guihua.gif'}]";

    public static List<List<EmojBean>> getEmojList(Context context) {

        List<EmojBean> emojList = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(EmojUtils.list);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jo = jsonArray.getJSONObject(i);

                String name = jo.getString("name");

                String url = jo.getString("url");

                String drawableName = url.substring(15, url.indexOf(".gif"));

                String identifier = "emoj_" + drawableName;

                String packageName = context.getPackageName();

                int resId = context.getResources().getIdentifier(identifier, "drawable", packageName);

                EmojBean bean = new EmojBean();

                bean.setMessage(name);

                bean.setDrawableName("[" + drawableName + "]");

                bean.setName("normal/" + drawableName + ".png");

                bean.setResId(resId);

                bean.setUploadMessage(getReplaceMessage(bean.getMessage(), bean.getName()));

                emojList.add(bean);
            }
        } catch (JSONException e) {

            e.printStackTrace();

        }

        List<List<EmojBean>> multi = new ArrayList<>();

        for (int i = 0; i < emojList.size() / 20; i++) {

            List<EmojBean> list1 = new ArrayList<>();

            for (int j = i * 20; j < 20 * (i + 1); j++) {
                list1.add(emojList.get(j));
            }

            multi.add(list1);

        }

        int left = emojList.size() % 20;

        List<EmojBean> tempList = new ArrayList<>();

        for (int i = emojList.size() - left; i < emojList.size(); i++) {

            tempList.add(emojList.get(i));

        }

        multi.add(tempList);

        return multi;
    }

    private static String getReplaceMessage(String message, String name) {

        String str = "<emotion image_name=\"[NAME]\" image_alt=\"[MESSAGE]\">[MESSAGE]</emotion>";

        return str.replace("[NAME]", name).replace("[MESSAGE]", message);

    }

    public static String getReplaceDrawableName(String name) {
        try {
            JSONArray jsonArray = new JSONArray(EmojUtils.list);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jo = jsonArray.getJSONObject(i);
                if (jo.getString("name").equals(name)) {
                    String url = jo.getString("url");

                    String drawableName = url.substring(15, url.indexOf(".gif"));

                    name = drawableName;

                    return name;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static List<EmojBean> getNameList(Context context) {

        List<EmojBean> emojList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(EmojUtils.list);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jo = jsonArray.getJSONObject(i);

                String name = jo.getString("name");

                String url = jo.getString("url");

                String drawableName = url.substring(15, url.indexOf(".gif"));

                String identifier = "emoj_" + drawableName;

                String packageName = context.getPackageName();

                int resId = context.getResources().getIdentifier(identifier, "drawable", packageName);

                EmojBean bean = new EmojBean();

                bean.setMessage(name);

                bean.setName("normal/" + drawableName + ".gif");

                bean.setResId(resId);

                emojList.add(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return emojList;
    }
}
