package com.android.residemenu.lt_lib.utils.handler;


import com.android.residemenu.lt_lib.enumdata.core.LotterySubTypeEnum;
import com.android.residemenu.lt_lib.model.PlateBean;
import com.android.residemenu.lt_lib.model.jjc.EntityOption;
import com.android.residemenu.lt_lib.model.jjc.PrizedResult;
import com.android.residemenu.lt_lib.model.jjc.PrizedResults;
import com.android.residemenu.lt_lib.model.lt.LotterySubType;
import com.android.residemenu.lt_lib.model.lt.OrderEntity;
import com.android.residemenu.lt_lib.utils.JsonAppParam;
import com.android.residemenu.lt_lib.utils.jjc.ComparatorJjcResult;
import com.android.residemenu.lt_lib.utils.jjc.jclq.JclqEntityOptionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


/**
 * 竞彩篮球投注和彩果分析
 *
 * @author leslie
 * @version $Id: JclqDetailhandler.java, v 0.1 2014年8月29日 上午11:24:01 leslie Exp
 *          $
 */
public class JclqDetailhandler {

    private static final String TAG = JclqDetailhandler.class.getSimpleName();

    /**
     * @param entries
     * @param lotterySubType
     * @return
     * @throws org.json.JSONException
     */
    public static List<OrderEntity> analyOptionList(JSONArray entries, LotterySubType lotterySubType) throws JSONException {

        String subTypeName = lotterySubType.getName();

        int len = entries.length();

        List<OrderEntity> detailsList = new ArrayList<OrderEntity>(len);

        boolean showDxResult = true;// 大小分是否可显示彩果，有可能没有盘口

        boolean showRfResult = true; // 让分是否可显示彩果，有可能没有盘口

        for (int i = 0; i < len; i++) {

            JSONObject entity = entries.getJSONObject(i);

            OrderEntity orderEntity = new OrderEntity();

            Map<String, Map<String, EntityOption>> entityOptionMap = new TreeMap<String, Map<String, EntityOption>>();

            JSONObject properties = entity.getJSONObject("properties");

            //让分盘口列表
            HashMap<String, List<PlateBean>> rfPlateListMap = new LinkedHashMap<>();

            //大小分盘口列表
            HashMap<String, List<PlateBean>> dxPlateListMap = new LinkedHashMap<>();

            if (LotterySubTypeEnum.JCLQ_HHGG.name().equals(subTypeName)) {

                if (entity.has("rfOptionList")) {

                    JSONArray optionList = entity.getJSONArray("rfOptionList");

                    String plate = null;

                    if (entity.has("rfPlates")) {

                        String plates = entity.getString("rfPlates"); // 让分[-2.5,-3.5]

                        plates = plates.substring(1, plates.length() - 1);

                        String[] plateArray = plates.split(",");

                        plate = plateArray[0];

                        JSONArray platesArray = entity.getJSONArray("rfPlates");

                        for (int j = 0; j < plateArray.length; j++) {

                            rfPlateListMap.put(platesArray.getString(j), new ArrayList<PlateBean>());

                        }

                    } else {

                        showRfResult = false;

                    }

                    //TODO
                    Map<String, EntityOption> map = JclqEntityOptionUtil.optionRF(optionList, plate, rfPlateListMap);

                    if (map != null) {

                        entityOptionMap.put(LotterySubTypeEnum.JCLQ_RF.name(), map);

                        Set<String> optionKeySet = map.keySet();

                        for (String key : optionKeySet) {
                            map.get(key).setPlateListMap(rfPlateListMap);
                        }

                    }

                }


                if (entity.has("sfOptionList")) {

                    JSONArray optionList = entity.getJSONArray("sfOptionList");

                    Map<String, EntityOption> map = JclqEntityOptionUtil.optionSF(optionList);

                    if (map != null) {
                        entityOptionMap.put(LotterySubTypeEnum.JCLQ_SF.name(), map);
                    }

                }

                if (entity.has("dxOptionList")) {//

                    JSONArray optionList = entity.getJSONArray("dxOptionList");

                    String plate = null;

                    if (entity.has("dxPlates")) {

                        String plates = entity.getString("dxPlates");// 让分[-2.5,-3.5]

                        plates = plates.substring(1, plates.length() - 1);

                        String[] plateArray = plates.split(",");

                        plate = plateArray[0];

                        JSONArray platesArray = entity.getJSONArray("dxPlates");

                        for (int j = 0; j < plateArray.length; j++) {

                            dxPlateListMap.put(platesArray.getString(j), new ArrayList<PlateBean>());

                        }

                    } else {
                        showDxResult = false;
                    }

                    Map<String, EntityOption> map = JclqEntityOptionUtil.optionDX(optionList, plate, dxPlateListMap);

                    if (map != null) {

                        entityOptionMap.put(LotterySubTypeEnum.JCLQ_DX.name(), map);

                        Set<String> optionKeySet = map.keySet();

                        for (String key : optionKeySet) {
                            map.get(key).setPlateListMap(dxPlateListMap);
                        }
                    }

                }

                if (entity.has("sfcOptionList")) {
                    JSONArray optionList = entity.getJSONArray("sfcOptionList");

                    Map<String, EntityOption> map = JclqEntityOptionUtil.optionSFC(optionList);

                    if (map != null) {

                        entityOptionMap.put(LotterySubTypeEnum.JCLQ_SFC.name(), map);
                    }
                }

            } else if (LotterySubTypeEnum.JCLQ_RF.name().equals(subTypeName)) {

                JSONArray optionList = entity.getJSONArray("optionList");

                String plate = null;

                if (entity.has("plates")) {// 有盘口

                    String plates = entity.getString("plates");// 让分[-2.5,-3.5]

                    plates = plates.substring(1, plates.length() - 1);

                    String[] plateArray = plates.split(",");

                    int plen = plateArray.length;

                    plate = plateArray[plen - 1];

                } else {
                    // 没有盘口，彩果也不用显示

                    showRfResult = false;

                }

                Map<String, EntityOption> map = JclqEntityOptionUtil.optionRF(optionList, plate, null);

                if (map != null) {
                    entityOptionMap.put(LotterySubTypeEnum.JCLQ_RF.name(), map);
                }

            } else if (LotterySubTypeEnum.JCLQ_SF.name().equals(subTypeName)) {

                JSONArray optionList = entity.getJSONArray("optionList");

                Map<String, EntityOption> map = JclqEntityOptionUtil.optionSF(optionList);

                if (map != null) {
                    entityOptionMap.put(LotterySubTypeEnum.JCLQ_SF.name(), map);
                }

            } else if (LotterySubTypeEnum.JCLQ_DX.name().equals(subTypeName)) {

                JSONArray optionList = entity.getJSONArray("optionList");

                String plate = null;

                if (entity.has("plates")) {

                    String plates = entity.getString("plates");// [-2.5,-3.5]

                    plates = plates.substring(1, plates.length() - 1);

                    String[] plateArray = plates.split(",");

                    int plen = plateArray.length;

                    plate = plateArray[plen - 1];
                } else {
                    showDxResult = false;
                }

                Map<String, EntityOption> map = JclqEntityOptionUtil.optionDX(optionList, plate, null);

                if (map != null) {
                    entityOptionMap.put(LotterySubTypeEnum.JCLQ_DX.name(), map);
                }

            } else if (LotterySubTypeEnum.JCLQ_SFC.name().equals(subTypeName)) {

                JSONArray optionList = entity.getJSONArray("optionList");

                Map<String, EntityOption> map = JclqEntityOptionUtil.optionSFC(optionList);

                if (map != null) {
                    entityOptionMap.put(LotterySubTypeEnum.JCLQ_SFC.name(), map);
                }

            } else {

            }

            orderEntity.setEntityOptionMap(entityOptionMap);

            orderEntity.setDan(entity.getBoolean("dan"));

            boolean prized = entity.getBoolean("prized");

            orderEntity.setGameNo(properties.getString("gno"));

            // 竞篮客队要放前面

            orderEntity.setHomeTeamName(entity.getString("guestTeamShortName"));

            orderEntity.setGuestTeamName(entity.getString("homeTeamShortName"));

          /*  orderEntity.setHomeTeamName(properties.getString("gt"));

            orderEntity.setGuestTeamName(properties.getString("ht"));*/

            orderEntity.setIssueNo(entity.getString("issueNo"));

            orderEntity.setPrized(prized);

            if (prized) {

                PrizedResults prizedResults = new PrizedResults();

                int halfHomeScore = entity.getInt("halfHomeScore");
                int halfGuestScore = entity.getInt("halfGuestScore");
                int fullHomeScore = entity.getInt("fullHomeScore");
                int fullGuestScore = entity.getInt("fullGuestScore");

                // prizedResults.setHalfHomeScore(halfHomeScore);
                // prizedResults.setHalfGuestScore(halfGuestScore);
                // prizedResults.setFullHomeScore(fullHomeScore);
                // prizedResults.setFullGuestScore(fullGuestScore);

                // 竞篮客队要放前面

                prizedResults.setHalfHomeScore(halfGuestScore);
                prizedResults.setHalfGuestScore(halfHomeScore);
                prizedResults.setFullHomeScore(fullGuestScore);
                prizedResults.setFullGuestScore(fullHomeScore);

                JSONObject raceResult = entity.getJSONObject("raceResult");

                // 只要处理投注过的，有中奖的，要标识出来

                List<PrizedResult> prizedResultList = new ArrayList<PrizedResult>();

                if (entityOptionMap.containsKey(LotterySubTypeEnum.JCLQ_RF.name()) && showRfResult) {

                    JSONArray raceResultJa;

                    if (LotterySubTypeEnum.JCLQ_HHGG.name().equals(subTypeName)) {
                        raceResultJa = raceResult.getJSONObject("rfRaceResult").getJSONArray("raceResultAndPlates");
                    } else {

                        raceResultJa = raceResult.getJSONArray("raceResultAndPlates");
                    }

                    int lenJa = raceResultJa.length();

                    JSONObject raceResultAndPlates = raceResultJa.getJSONObject(0);

                    String plate = raceResultAndPlates.getString("plate");

                    JSONObject rfResultJson = raceResultAndPlates.getJSONObject("raceResult");

                    JclqEntityOptionUtil.prizedResultRF(prizedResultList, rfResultJson, plate);

                    Map<String, EntityOption> optionMap = entityOptionMap.get(LotterySubTypeEnum.JCLQ_RF.name());

                    EntityOption entityOption = optionMap.get(rfResultJson.getString(JsonAppParam.name));

                    if (entityOption != null) {
                        entityOption.setSelected(true);
                    }

                    HashMap<String, String> winList = new HashMap<>();

                    for (int j = 0; j < raceResultJa.length(); j++) {

                        JSONObject temp = raceResultJa.getJSONObject(j);

                        String template = temp.getString("plate");

                        JSONObject tempRaceResult = temp.getJSONObject("raceResult");

                        String name = tempRaceResult.getString("name");

                        if (optionMap.get(name) != null) {

                            winList.put(template, name);

                        }
                    }


                    if (winList.size() != 0 && rfPlateListMap.size() > 1) {
                        Set<String> keys = winList.keySet();
                        for (String key : keys) {
                            String resultName = winList.get(key);
                            List<PlateBean> plateBeans = rfPlateListMap.get(key);
                            if (plateBeans != null) {
                                for (int j = 0; j < plateBeans.size(); j++) {
                                    if (resultName.equals(plateBeans.get(j).getOptionName())) {
                                        plateBeans.get(j).setIsRed(true);
                                    }
                                }
                            }
                        }
                    }

                    Set<String> optionKeySet = optionMap.keySet();

                    for (String key : optionKeySet) {
                        optionMap.get(key).setPlateListMap(rfPlateListMap);
                    }
                }

                if (entityOptionMap.containsKey(LotterySubTypeEnum.JCLQ_SFC.name())) {

                    JSONObject sfcResultJson;
                    if (LotterySubTypeEnum.JCLQ_HHGG.name().equals(subTypeName)) {

                        sfcResultJson = raceResult.getJSONObject("sfcRaceResult").getJSONObject("raceResult");

                    } else {
                        sfcResultJson = raceResult.getJSONObject("raceResult");
                    }

                    JclqEntityOptionUtil.prizedResultSFC(prizedResultList, sfcResultJson);

                    Map<String, EntityOption> optionMap = entityOptionMap.get(LotterySubTypeEnum.JCLQ_SFC
                            .name());

                    EntityOption entityOption = optionMap.get(sfcResultJson.getString(JsonAppParam.name));

                    if (entityOption != null) {
                        entityOption.setSelected(true);
                    }

                }

                if (entityOptionMap.containsKey(LotterySubTypeEnum.JCLQ_SF.name())) {

                    JSONObject sfResultJson;

                    if (LotterySubTypeEnum.JCLQ_HHGG.name().equals(subTypeName)) {
                        sfResultJson = raceResult.getJSONObject("sfRaceResult").getJSONObject("raceResult");
                    } else {
                        sfResultJson = raceResult.getJSONObject("raceResult");
                    }

                    JclqEntityOptionUtil.prizedResultSF(prizedResultList, sfResultJson);

                    Map<String, EntityOption> optionMap = entityOptionMap.get(LotterySubTypeEnum.JCLQ_SF
                            .name());

                    EntityOption entityOption = optionMap.get(sfResultJson.getString(JsonAppParam.name));

                    if (entityOption != null) {
                        entityOption.setSelected(true);
                    }

                }

                if (entityOptionMap.containsKey(LotterySubTypeEnum.JCLQ_DX.name()) && showDxResult) {

                    JSONArray raceResultJa;

                    if (LotterySubTypeEnum.JCLQ_HHGG.name().equals(subTypeName)) {
                        raceResultJa = raceResult.getJSONObject("dxRaceResult").getJSONArray(
                                "raceResultAndPlates");
                    } else {

                        raceResultJa = raceResult.getJSONArray("raceResultAndPlates");
                    }

                    int lenJa = raceResultJa.length();

                    JSONObject raceResultAndPlates = raceResultJa.getJSONObject(0);

                    String plate = raceResultAndPlates.getString("plate");

                    JSONObject rfResultJson = raceResultAndPlates.getJSONObject("raceResult");

                    JclqEntityOptionUtil.prizedResultDX(prizedResultList, rfResultJson, plate);

                    Map<String, EntityOption> optionMap = entityOptionMap.get(LotterySubTypeEnum.JCLQ_DX.name());

                    EntityOption entityOption = optionMap.get(rfResultJson.getString(JsonAppParam.name));

                    if (entityOption != null) {

                        entityOption.setSelected(true);

                    }

                    HashMap<String, String> winList = new HashMap<>();

                    for (int j = 0; j < raceResultJa.length(); j++) {

                        JSONObject temp = raceResultJa.getJSONObject(j);

                        String template = temp.getString("plate");

                        JSONObject tempRaceResult = temp.getJSONObject("raceResult");

                        String name = tempRaceResult.getString("name");

                        if (optionMap.get(name) != null) {

                            winList.put(template, name);

                        }
                    }


                    if (winList.size() != 0 && dxPlateListMap.size() > 1) {

                        Set<String> keys = winList.keySet();
                        for (String key : keys) {
                            String s = winList.get(key);
                            List<PlateBean> plateBeans = dxPlateListMap.get(key);
                            if (plateBeans != null) {
                                for (int j = 0; j < plateBeans.size(); j++) {
                                    if (s.equals(plateBeans.get(j).getOptionName())) {
                                        plateBeans.get(j).setIsRed(true);
                                    }
                                }
                            }

                        }
                    }

                    Set<String> optionKeySet = optionMap.keySet();

                    for (String key : optionKeySet) {
                        optionMap.get(key).setPlateListMap(dxPlateListMap);
                    }
                }


                ComparatorJjcResult comparator = new ComparatorJjcResult();

                Collections.sort(prizedResultList, comparator);

                prizedResults.setPrizedResultList(prizedResultList);

                orderEntity.setPrizedResults(prizedResults);

            }

            detailsList.add(orderEntity);

        }

        return detailsList;
    }
}
