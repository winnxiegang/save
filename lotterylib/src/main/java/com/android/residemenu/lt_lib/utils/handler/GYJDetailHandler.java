package com.android.residemenu.lt_lib.utils.handler;
import com.android.library.Utils.LibAppUtil;
import com.android.residemenu.lt_lib.model.lt.OrderEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duzaoqiu on 2016/7/14 17:50.
 * 冠亚军选号详情显示数据
 */
public class GYJDetailHandler {

    private static final String TAG = GYJDetailHandler.class.getSimpleName();

    public static List<OrderEntity> applyOptionList(JSONArray entries) {

        int len = entries.length();

        List<OrderEntity> detailsList = null;

        if (len > 0) {

            for (int i = 0; i < len; i++) {

                try {

                    JSONObject entity = entries.getJSONObject(i);

                    JSONArray ja = entity.getJSONArray("dataList");

                    if (ja.length() > 0) {

                        int length = ja.length();

                        detailsList = new ArrayList<OrderEntity>(length);

                        for (int j = 0; j < length; j++) {

                            OrderEntity orderEntity = new OrderEntity();

                            JSONObject jo = ja.getJSONObject(j);

                            JSONObject status = jo.getJSONObject("status");

                            String statusName = status.getString("name");

                            if (jo.has("raceResult") && ("WIN".equals(statusName) || "BET_OUT".equals(statusName))) {
                                orderEntity.setRaceResult(jo.getString("raceResult"));
                            } else {
                                orderEntity.setRaceResult("-");
                            }

                            orderEntity.setHomeTeamName(jo.getString("teamName"));

                            if (jo.has("team2Name")) {

                                orderEntity.setGuestTeamName(jo.getString("team2Name"));
                            }

                            if ("WIN".equals(status.getString("name"))) {

                                orderEntity.setShowRed(true);

                            }

                            String gameNo = jo.getString("gameNo");

                            orderEntity.setGameNo(LibAppUtil.numAddFrontZero(Integer.parseInt(gameNo)));

                            orderEntity.setOdds(jo.getString("sp"));

                            detailsList.add(orderEntity);
                        }

                    }
                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }

        }

        return detailsList;
    }
}
