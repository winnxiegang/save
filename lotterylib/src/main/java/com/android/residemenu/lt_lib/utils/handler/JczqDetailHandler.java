package com.android.residemenu.lt_lib.utils.handler;


import com.android.residemenu.lt_lib.enumdata.core.LotterySubTypeEnum;
import com.android.residemenu.lt_lib.model.jjc.EntityOption;
import com.android.residemenu.lt_lib.model.jjc.PrizedResult;
import com.android.residemenu.lt_lib.model.jjc.PrizedResults;
import com.android.residemenu.lt_lib.model.lt.LotterySubType;
import com.android.residemenu.lt_lib.model.lt.OrderEntity;
import com.android.residemenu.lt_lib.utils.JsonAppParam;
import com.android.residemenu.lt_lib.utils.jjc.ComparatorJjcResult;
import com.android.residemenu.lt_lib.utils.jjc.jczq.JczqEntityOptionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 竞彩足球投注和彩果处理
 *
 * @author leslie
 * @version $Id: JczqDetailhandler.java, v 0.1 2014年8月29日 上午11:00:54 leslie Exp
 *          $
 */
public class JczqDetailHandler {

    private static final String TAG = JczqDetailHandler.class.getSimpleName();

    /**
     * @param entries
     * @param lotterySubType
     * @return
     * @throws org.json.JSONException
     */
    public static List<OrderEntity> applyOptionList(JSONArray entries, LotterySubType lotterySubType) throws JSONException {

        String subTypeName = lotterySubType.getName();

        int len = entries.length();

        List<OrderEntity> detailsList = new ArrayList<OrderEntity>(len);

        for (int i = 0; i < len; i++) {

            JSONObject entity = entries.getJSONObject(i);

            OrderEntity orderEntity = new OrderEntity();

            int concede = entity.getInt("concede");

            Map<String, Map<String, EntityOption>> entityOptionMap = new TreeMap<String, Map<String, EntityOption>>();

            if (LotterySubTypeEnum.JCZQ_HHGG.name().equals(subTypeName) || LotterySubTypeEnum.JCZQ_HHDG.name().equals(subTypeName)) {

                // 混合过关
                if (entity.has("bfOptionList")) {// 比分

                    JSONArray optionList = entity.getJSONArray("bfOptionList");

                    Map<String, EntityOption> map = JczqEntityOptionUtil.optionBF(optionList);

                    if (map != null) {
                        entityOptionMap.put(LotterySubTypeEnum.JCZQ_BF.name(), map);
                    }

                }

                if (entity.has("bqcOptionList")) {// 半全场

                    JSONArray optionList = entity.getJSONArray("bqcOptionList");

                    Map<String, EntityOption> map = JczqEntityOptionUtil.optionBQC(optionList);
                    if (map != null) {
                        entityOptionMap.put(LotterySubTypeEnum.JCZQ_BQC.name(), map);
                    }

                }

                if (entity.has("jqsOptionList")) {// 进球数

                    JSONArray optionList = entity.getJSONArray("jqsOptionList");
                    Map<String, EntityOption> map = JczqEntityOptionUtil.optionJQS(optionList);
                    if (map != null) {
                        entityOptionMap.put(LotterySubTypeEnum.JCZQ_JQS.name(), map);
                    }
                }

                if (entity.has("rqspfOptionList")) {// 让球胜平负

                    JSONArray optionList = entity.getJSONArray("rqspfOptionList");

                    Map<String, EntityOption> map = JczqEntityOptionUtil.optionRQSPF(optionList, concede);

                    if (map != null) {
                        entityOptionMap.put(LotterySubTypeEnum.JCZQ_RQSPF.name(), map);
                    }

                }

                if (entity.has("spfOptionList")) {// 胜平负

                    JSONArray optionList = entity.getJSONArray("spfOptionList");

                    Map<String, EntityOption> map = JczqEntityOptionUtil.optionSPF(optionList);
                    if (map != null) {
                        entityOptionMap.put(LotterySubTypeEnum.JCZQ_SPF.name(), map);
                    }

                }

            } else if (LotterySubTypeEnum.JCZQ_RQSPF.name().equals(subTypeName)) {

                JSONArray optionList = entity.getJSONArray("optionList");

                Map<String, EntityOption> map = JczqEntityOptionUtil.optionRQSPF(optionList, concede);

                if (map != null) {
                    entityOptionMap.put(LotterySubTypeEnum.JCZQ_RQSPF.name(), map);

                }

            } else if (LotterySubTypeEnum.JCZQ_JQS.name().equals(subTypeName)) {

                JSONArray optionList = entity.getJSONArray("optionList");

                Map<String, EntityOption> map = JczqEntityOptionUtil.optionJQS(optionList);
                if (map != null) {
                    entityOptionMap.put(LotterySubTypeEnum.JCZQ_JQS.name(), map);
                }

            } else if (LotterySubTypeEnum.JCZQ_BQC.name().equals(subTypeName)) {

                JSONArray optionList = entity.getJSONArray("optionList");
                Map<String, EntityOption> map = JczqEntityOptionUtil.optionBQC(optionList);
                if (map != null) {
                    entityOptionMap.put(LotterySubTypeEnum.JCZQ_BQC.name(), map);
                }

            } else if (LotterySubTypeEnum.JCZQ_BF.name().equals(subTypeName)) {

                JSONArray optionList = entity.getJSONArray("optionList");

                Map<String, EntityOption> map = JczqEntityOptionUtil.optionBF(optionList);

                if (map != null) {
                    entityOptionMap.put(LotterySubTypeEnum.JCZQ_BF.name(), map);
                }

            } else if (LotterySubTypeEnum.JCZQ_SPF.name().equals(subTypeName)) {
                JSONArray optionList = entity.getJSONArray("optionList");

                Map<String, EntityOption> map = JczqEntityOptionUtil.optionSPF(optionList);
                if (map != null) {
                    entityOptionMap.put(LotterySubTypeEnum.JCZQ_SPF.name(), map);
                }
            } else {

            }

            orderEntity.setEntityOptionMap(entityOptionMap);

            orderEntity.setDan(entity.getBoolean("dan"));

            boolean prized = entity.getBoolean("prized");


            orderEntity.setConcede(concede);

            orderEntity.setGameNo(entity.getString("gameNo"));

            if(entity.has("homeTeamShortName")){
                orderEntity.setHomeTeamName(entity.getString("homeTeamShortName"));
            }else{
                orderEntity.setHomeTeamName(entity.getString("homeTeamName"));
            }

            if(entity.has("guestTeamShortName")){
                orderEntity.setGuestTeamName(entity.getString("guestTeamShortName"));
            }else{
                orderEntity.setGuestTeamName(entity.getString("guestTeamName"));
            }

            //if(i==0){
            //orderEntity.setPrized(false);
            //}else{
            orderEntity.setPrized(prized);
            //}

            orderEntity.setIssueNo(entity.getString("issueNo"));

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

                if (entityOptionMap.containsKey(LotterySubTypeEnum.JCZQ_BF.name())) {

                    JSONObject bfResultJson;

                    if (LotterySubTypeEnum.JCZQ_HHGG.name().equals(subTypeName) || LotterySubTypeEnum.JCZQ_HHDG.name().equals(subTypeName)) {
                        bfResultJson = raceResult.getJSONObject("bfRaceResult").getJSONObject("raceResult");
                    } else {
                        bfResultJson = raceResult.getJSONObject("raceResult");
                    }

                    JczqEntityOptionUtil.prizedResultBF(prizedResultList, bfResultJson);

                    Map<String, EntityOption> optionMap = entityOptionMap.get(LotterySubTypeEnum.JCZQ_BF.name());

                    EntityOption entityOption = optionMap.get(bfResultJson.get(JsonAppParam.name));

                    if (entityOption != null) {
                        entityOption.setSelected(true);
                    }

                }

                if (entityOptionMap.containsKey(LotterySubTypeEnum.JCZQ_BQC.name())) {

                    JSONObject bqcResultJson;

                    if (LotterySubTypeEnum.JCZQ_HHGG.name().equals(subTypeName) || LotterySubTypeEnum.JCZQ_HHDG.name().equals(subTypeName)) {
                        bqcResultJson = raceResult.getJSONObject("bqcRaceResult").getJSONObject("raceResult");
                    } else {
                        bqcResultJson = raceResult.getJSONObject("raceResult");
                    }

                    JczqEntityOptionUtil.prizedResultBQC(prizedResultList, bqcResultJson);

                    Map<String, EntityOption> optionMap = entityOptionMap.get(LotterySubTypeEnum.JCZQ_BQC.name());

                    EntityOption entityOption = optionMap.get(bqcResultJson.get(JsonAppParam.name));
                    if (entityOption != null) {
                        entityOption.setSelected(true);
                    }

                }
                if (entityOptionMap.containsKey(LotterySubTypeEnum.JCZQ_JQS.name())) {

                    JSONObject jqsResultJson;

                    if (LotterySubTypeEnum.JCZQ_HHGG.name().equals(subTypeName) || LotterySubTypeEnum.JCZQ_HHDG.name().equals(subTypeName)) {
                        jqsResultJson = raceResult.getJSONObject("jqsRaceResult").getJSONObject("raceResult");
                    } else {
                        jqsResultJson = raceResult.getJSONObject("raceResult");
                    }

                    JczqEntityOptionUtil.prizedResultJQS(prizedResultList, jqsResultJson);

                    Map<String, EntityOption> optionMap = entityOptionMap.get(LotterySubTypeEnum.JCZQ_JQS
                            .name());

                    EntityOption entityOption = optionMap.get(jqsResultJson.get(JsonAppParam.name));
                    if (entityOption != null) {
                        entityOption.setSelected(true);
                    }

                }

                if (entityOptionMap.containsKey(LotterySubTypeEnum.JCZQ_RQSPF.name())) {

                    JSONObject rqspfResultJson;

                    if (LotterySubTypeEnum.JCZQ_HHGG.name().equals(subTypeName) || LotterySubTypeEnum.JCZQ_HHDG.name().equals(subTypeName)) {
                        rqspfResultJson = raceResult.getJSONObject("rqspfRaceResult").getJSONObject("raceResult");
                    } else {
                        rqspfResultJson = raceResult.getJSONObject("raceResult");
                    }

                    JczqEntityOptionUtil.prizedResultRQSPF(prizedResultList, rqspfResultJson);

                    Map<String, EntityOption> optionMap = entityOptionMap.get(LotterySubTypeEnum.JCZQ_RQSPF.name());

                    EntityOption entityOption = optionMap.get(rqspfResultJson.get(JsonAppParam.name));
                    if (entityOption != null) {
                        entityOption.setSelected(true);
                    }

                }

                if (entityOptionMap.containsKey(LotterySubTypeEnum.JCZQ_SPF.name())) {

                    JSONObject spfResultJson;

                    if (LotterySubTypeEnum.JCZQ_HHGG.name().equals(subTypeName) || LotterySubTypeEnum.JCZQ_HHDG.name().equals(subTypeName)) {
                        spfResultJson = raceResult.getJSONObject("spfRaceResult").getJSONObject("raceResult");
                    } else {
                        spfResultJson = raceResult.getJSONObject("raceResult");
                    }

                    JczqEntityOptionUtil.prizedResultSPF(prizedResultList, spfResultJson);

                    Map<String, EntityOption> optionMap = entityOptionMap.get(LotterySubTypeEnum.JCZQ_SPF
                            .name());

                    EntityOption entityOption = optionMap.get(spfResultJson.get(JsonAppParam.name));
                    if (entityOption != null) {
                        entityOption.setSelected(true);
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
