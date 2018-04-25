package com.android.residemenu.lt_lib.utils.handler;

import android.util.Log;

import com.android.residemenu.lt_lib.enumdata.core.LotterySubTypeEnum;
import com.android.residemenu.lt_lib.model.jjc.EntityOption;
import com.android.residemenu.lt_lib.model.jjc.PrizedResult;
import com.android.residemenu.lt_lib.model.jjc.PrizedResults;
import com.android.residemenu.lt_lib.model.lt.LotterySubType;
import com.android.residemenu.lt_lib.model.lt.OrderEntity;
import com.android.residemenu.lt_lib.utils.JsonAppParam;
import com.android.residemenu.lt_lib.utils.jjc.zucai.ZucaiEntityOptionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 足彩投注和彩果处理
 *
 * @author leslie
 * @version $Id: ZuicaiDetailHandler.java, v 0.1 2014年9月1日 上午10:55:27 leslie Exp
 *          $
 */
public class ZucaiDetailHandler {

    public static final String TAG = ZucaiDetailHandler.class.getSimpleName();

    public static List<OrderEntity> analyOptionList(JSONArray entries, LotterySubType lotterySubType) throws JSONException {

        String subTypeName = lotterySubType.getName();

        int len = entries.length();

        List<OrderEntity> detailsList = new ArrayList<OrderEntity>(len);

        for (int i = 0; i < len; i++) {

            JSONObject entity = entries.getJSONObject(i);

            OrderEntity orderEntity = new OrderEntity();

            Map<String, Map<String, EntityOption>> entityOptionMap = new LinkedHashMap<String, Map<String, EntityOption>>();

            if (LotterySubTypeEnum.ZUCAI_SFC.name().equals(subTypeName) || LotterySubTypeEnum.ZUCAI_R9.name().equals(subTypeName)) {

                JSONArray optionList = entity.getJSONArray("optionList");

                Map<String, EntityOption> map = ZucaiEntityOptionUtil.option(optionList, lotterySubType);

                if (map != null) {
                    entityOptionMap.put(subTypeName, map);
                }

            } else if (LotterySubTypeEnum.ZUCAI_JQC.name().equals(subTypeName)) {

                JSONArray homeOptionList = entity.getJSONArray("homeOptionList");

                JSONArray guestOptionList = entity.getJSONArray("guestOptionList");

                Map<String, EntityOption> mapHome = ZucaiEntityOptionUtil.optionJQS(homeOptionList, lotterySubType.getName() + "_a", "主");

                if (mapHome != null) {
                    entityOptionMap.put(lotterySubType.getName() + "_a", mapHome);
                }

                Map<String, EntityOption> mapGuest = ZucaiEntityOptionUtil
                        .optionJQS(guestOptionList, lotterySubType.getName() + "_b", "客");

                if (mapGuest != null) {
                    entityOptionMap.put(lotterySubType.getName() + "_b", mapGuest);
                }

            } else if (LotterySubTypeEnum.ZUCAI_BQC.name().equals(subTypeName)) {

                JSONArray optionList = entity.getJSONArray("optionList");

                String message;
                if (i % 2 == 0) {
                    message = "半";
                } else {
                    message = "全";
                }

                Map<String, EntityOption> map = ZucaiEntityOptionUtil.optionBQC(optionList, lotterySubType, message);

                if (map != null) {
                    entityOptionMap.put(subTypeName, map);

                }

            }
            orderEntity.setEntityOptionMap(entityOptionMap);

            orderEntity.setDan(entity.getBoolean("dan"));

            boolean prized = entity.getBoolean("prized");

            JSONObject properties = entity.getJSONObject("properties");

            orderEntity.setGameNo(properties.getString("gno"));

            orderEntity.setHomeTeamName(properties.getString("ht"));

            orderEntity.setGuestTeamName(properties.getString("gt"));

            orderEntity.setPrized(prized);

            if (prized) {

                PrizedResults prizedResults = new PrizedResults();

                int halfHomeScore = entity.getInt("halfHomeScore");
                int halfGuestScore = entity.getInt("halfGuestScore");
                int fullHomeScore = entity.getInt("fullHomeScore");
                int fullGuestScore = entity.getInt("fullGuestScore");

                prizedResults.setHalfHomeScore(halfHomeScore);
                prizedResults.setHalfGuestScore(halfGuestScore);
                prizedResults.setFullHomeScore(fullHomeScore);
                prizedResults.setFullGuestScore(fullGuestScore);

                JSONObject raceResult = entity.getJSONObject("raceResult");

                List<PrizedResult> prizedResultList = new ArrayList<PrizedResult>();

                if (LotterySubTypeEnum.ZUCAI_SFC.name().equals(subTypeName) || LotterySubTypeEnum.ZUCAI_R9.name().equals(subTypeName)) {

                    JSONObject bfResultJson;

                    bfResultJson = raceResult.getJSONObject("raceResult");

                    Log.d(TAG, "bfResultJson=" + bfResultJson.toString());

                    ZucaiEntityOptionUtil.prizedResult(prizedResultList, bfResultJson, lotterySubType);

                    Map<String, EntityOption> optionMap = entityOptionMap.get(subTypeName);

                    if (optionMap != null) {

                        EntityOption entityOption = optionMap.get(bfResultJson.getString(JsonAppParam.name));

                        if (entityOption != null) {
                            entityOption.setSelected(true);
                        }
                    }

                } else if (LotterySubTypeEnum.ZUCAI_JQC.name().equals(subTypeName)) {

                    JSONObject homeResultJson = raceResult.getJSONObject("homeRaceResult");

                    JSONObject guestResultJson = raceResult.getJSONObject("guestRaceResult");

                    ZucaiEntityOptionUtil.prizedResultJQS(prizedResultList, homeResultJson, lotterySubType.getName() + "_a", "主");

                    Map<String, EntityOption> optionMap = entityOptionMap.get(lotterySubType.getName() + "_a");

                    if (optionMap != null) {

                        EntityOption entityOption = optionMap.get(homeResultJson.get(JsonAppParam.name));

                        if (entityOption != null) {

                            entityOption.setSelected(true);

                        }

                    }

                    ZucaiEntityOptionUtil.prizedResultJQS(prizedResultList, guestResultJson, lotterySubType.getName() + "_b", "客");

                    optionMap = entityOptionMap.get(lotterySubType.getName() + "_b");

                    if (optionMap != null) {

                        EntityOption entityOption = optionMap.get(guestResultJson.get(JsonAppParam.name));

                        if (entityOption != null) {

                            entityOption.setSelected(true);

                        }

                    }

                } else if (LotterySubTypeEnum.ZUCAI_BQC.name().equals(subTypeName)) {

                    String message;

                    if (i % 2 == 0) {
                        message = "半";
                    } else {
                        message = "全";
                    }

                    JSONObject resultJson = raceResult.getJSONObject("raceResult");

                    ZucaiEntityOptionUtil.prizedResultBQC(prizedResultList, resultJson, lotterySubType, message);

                    Map<String, EntityOption> optionMap = entityOptionMap.get(subTypeName);

                    if (optionMap != null) {

                        EntityOption entityOption = optionMap.get(resultJson.get(JsonAppParam.name));

                        if (entityOption != null) {
                            entityOption.setSelected(true);
                        }

                    }

                }

                prizedResults.setPrizedResultList(prizedResultList);

                orderEntity.setPrizedResults(prizedResults);



            }

            detailsList.add(orderEntity);



        }

        return detailsList;

    }

}
