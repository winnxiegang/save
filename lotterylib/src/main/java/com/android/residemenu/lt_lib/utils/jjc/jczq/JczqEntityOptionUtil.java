package com.android.residemenu.lt_lib.utils.jjc.jczq;


import com.android.residemenu.lt_lib.enumdata.core.LotterySubTypeEnum;
import com.android.residemenu.lt_lib.model.jjc.EntityOption;
import com.android.residemenu.lt_lib.model.jjc.PrizedResult;
import com.android.residemenu.lt_lib.utils.JsonAppParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 竞彩足球投注数据处理
 *
 * @author leslie
 * @version $Id: JczqEntityOptionUtil.java, v 0.1 2014年7月11日 下午5:30:41 leslie
 *          Exp $
 */
public class JczqEntityOptionUtil {

    /**
     * 比分投注数据处理
     */
    public static Map<String, EntityOption> optionBF(JSONArray optionList) throws JSONException {

        int len = optionList.length();

        Map<String, EntityOption> map = null;

        if (len > 0) {
            map = new TreeMap<String, EntityOption>();
        }

        for (int i = 0; i < len; i++) {

            JSONObject jo = optionList.getJSONObject(i);

            String sp = jo.getString(JsonAppParam.sp);

            JSONObject option = jo.getJSONObject("option");

            EntityOption entityOption = new EntityOption(option.getString(JsonAppParam.name),
                    option.getString(JsonAppParam.message), "", sp);

            entityOption.setSubTypeName(LotterySubTypeEnum.JCZQ_BF.name());

            entityOption.setSubType("比分");

            map.put(entityOption.getName(), entityOption);

        }

        return map;

    }

    /**
     * 半全场投注数据
     */
    public static Map<String, EntityOption> optionBQC(JSONArray optionList) throws JSONException {

        int len = optionList.length();

        Map<String, EntityOption> map = null;

        if (len > 0) {
            map = new TreeMap<String, EntityOption>();

        }

        for (int i = 0; i < len; i++) {

            JSONObject jo = optionList.getJSONObject(i);

            String sp = jo.getString(JsonAppParam.sp);

            JSONObject option = jo.getJSONObject("option");

            EntityOption entityOption = new EntityOption(option.getString(JsonAppParam.name),
                    option.getString(JsonAppParam.message), "", sp);
            entityOption.setSubTypeName(LotterySubTypeEnum.JCZQ_BQC.name());
            entityOption.setSubType("半全场");
            map.put(entityOption.getName(), entityOption);

        }

        return map;

    }

    /**
     * 进球数投注数据
     */
    public static Map<String, EntityOption> optionJQS(JSONArray optionList) throws JSONException {

        int len = optionList.length();

        Map<String, EntityOption> map = null;

        if (len > 0) {
            map = new TreeMap<String, EntityOption>();

        }

        for (int i = 0; i < len; i++) {

            JSONObject jo = optionList.getJSONObject(i);

            String sp = jo.getString(JsonAppParam.sp);

            JSONObject option = jo.getJSONObject("option");

            EntityOption entityOption = new EntityOption(option.getString(JsonAppParam.name),
                    option.getString(JsonAppParam.message), option.getString(JsonAppParam.value), sp);
            entityOption.setSubTypeName(LotterySubTypeEnum.JCZQ_JQS.name());
            entityOption.setSubType("进球数");
            map.put(entityOption.getName(), entityOption);

        }

        return map;

    }

    /**
     * 胜平负投注数据
     */
    public static Map<String, EntityOption> optionSPF(JSONArray optionList) throws JSONException {

        int len = optionList.length();

        Map<String, EntityOption> map = null;

        if (len > 0) {
            map = new TreeMap<String, EntityOption>();

        }

        for (int i = 0; i < len; i++) {

            JSONObject jo = optionList.getJSONObject(i);

            String sp = jo.getString(JsonAppParam.sp);

            JSONObject option = jo.getJSONObject("option");

            EntityOption entityOption = new EntityOption(option.getString(JsonAppParam.name),
                    option.getString(JsonAppParam.message), option.getString(JsonAppParam.value), sp);
            entityOption.setSubTypeName(LotterySubTypeEnum.JCZQ_SPF.name());
            entityOption.setSubType("胜平负");
            map.put(entityOption.getName(), entityOption);

        }

        return map;
    }

    /**
     * 让球胜平负投注数据
     */
    public static Map<String, EntityOption> optionRQSPF(JSONArray optionList,int concede) throws JSONException {

        int len = optionList.length();

        Map<String, EntityOption> map = null;

        if (len > 0) {

            map = new TreeMap<String, EntityOption>();

        }

        for (int i = 0; i < len; i++) {

            JSONObject jo = optionList.getJSONObject(i);

            String sp = jo.getString(JsonAppParam.sp);

            JSONObject option = jo.getJSONObject("option");

            EntityOption entityOption = new EntityOption(option.getString(JsonAppParam.name),
                    option.getString(JsonAppParam.message), option.getString(JsonAppParam.value), sp);

            entityOption.setSubTypeName(LotterySubTypeEnum.JCZQ_RQSPF.name());

            if(concede>0){
                entityOption.setSubType("让球"+"("+"+"+concede+")");
            }else{
                entityOption.setSubType("让球"+"("+concede+")");
            }

            map.put(entityOption.getName(), entityOption);

        }

        return map;
    }

    /**
     * 比分彩果
     */
    public static void prizedResultBF(List<PrizedResult> prizedResultList, JSONObject resultJson)
            throws JSONException {

        PrizedResult bfPrizedResult = new PrizedResult(resultJson.getString(JsonAppParam.name),
                resultJson.getString(JsonAppParam.message));
        bfPrizedResult.setSubTypeName(LotterySubTypeEnum.JCZQ_BF.name());
        bfPrizedResult.setSubType("比分");

        prizedResultList.add(bfPrizedResult);

    }

    /**
     * 半全场彩果
     */
    public static void prizedResultBQC(List<PrizedResult> prizedResultList, JSONObject resultJson)
            throws JSONException {

        PrizedResult bqcPrizedResult = new PrizedResult(resultJson.getString(JsonAppParam.name),
                resultJson.getString(JsonAppParam.message));
        bqcPrizedResult.setSubTypeName(LotterySubTypeEnum.JCZQ_BQC.name());
        bqcPrizedResult.setSubType("半全场");
        prizedResultList.add(bqcPrizedResult);
    }

    /**
     * 进球数彩果
     */
    public static void prizedResultJQS(List<PrizedResult> prizedResultList, JSONObject resultJson)
            throws JSONException {

        PrizedResult jqsPrizedResult = new PrizedResult(resultJson.getString(JsonAppParam.name),
                resultJson.getString(JsonAppParam.message));
        jqsPrizedResult.setSubTypeName(LotterySubTypeEnum.JCZQ_JQS.name());
        jqsPrizedResult.setSubType("进球数");
        prizedResultList.add(jqsPrizedResult);

    }

    /**
     * 胜平负彩果
     */
    public static void prizedResultSPF(List<PrizedResult> prizedResultList, JSONObject resultJson)
            throws JSONException {
        PrizedResult spfpPrizedResult = new PrizedResult(resultJson.getString(JsonAppParam.name),
                resultJson.getString(JsonAppParam.message));
        spfpPrizedResult.setSubTypeName(LotterySubTypeEnum.JCZQ_SPF.name());
        spfpPrizedResult.setSubType("胜平负");
        prizedResultList.add(spfpPrizedResult);
    }

    /**
     * 让球胜平负彩果
     */
    public static void prizedResultRQSPF(List<PrizedResult> prizedResultList, JSONObject resultJson)
            throws JSONException {

        PrizedResult rqspfPrizedResult = new PrizedResult(resultJson.getString(JsonAppParam.name),
                resultJson.getString(JsonAppParam.message));
        rqspfPrizedResult.setSubTypeName(LotterySubTypeEnum.JCZQ_RQSPF.name());
        rqspfPrizedResult.setSubType("让球");
        prizedResultList.add(rqspfPrizedResult);

    }

}
