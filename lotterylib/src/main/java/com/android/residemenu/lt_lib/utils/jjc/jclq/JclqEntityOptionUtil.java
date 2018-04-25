package com.android.residemenu.lt_lib.utils.jjc.jclq;


import com.android.residemenu.lt_lib.enumdata.core.LotterySubTypeEnum;
import com.android.residemenu.lt_lib.model.PlateBean;
import com.android.residemenu.lt_lib.model.jjc.EntityOption;
import com.android.residemenu.lt_lib.model.jjc.PrizedResult;
import com.android.residemenu.lt_lib.utils.JsonAppParam;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 竞彩篮球投注数据处理
 *
 * @author leslie
 * @version $Id: JclqEntityOptionUtil.java, v 0.1 2014年8月21日 下午3:59:44 leslie
 *          Exp $
 */
public class JclqEntityOptionUtil {

    /**
     * 胜负投注数据
     *
     * @return
     * @throws JSONException "optionList": [ { "option": { "message": "主胜", "name": "WIN",
     *                       "value": 1 }, "sp": 1.24 }, { "option": { "message": "主负",
     *                       "name": "LOST", "value": 2 }, "sp": 2.68 } ]
     */
    public static Map<String, EntityOption> optionSF(JSONArray optionList) throws JSONException {

        Map<String, EntityOption> map = null;

        int len = optionList.length();

        if (len > 0) {

            map = new TreeMap<String, EntityOption>();

        }

        for (int i = 0; i < len; i++) {

            JSONObject jo = optionList.getJSONObject(i);

            String sp = jo.getString(JsonAppParam.sp);

            JSONObject option = jo.getJSONObject("option");

            EntityOption entityOption = new EntityOption(
                    option.getString(JsonAppParam.name),
                    option.getString(JsonAppParam.message),
                    option.getString(JsonAppParam.value), sp);
            entityOption.setSubTypeName(LotterySubTypeEnum.JCLQ_SF.name());
            entityOption.setSubType("胜负");
            map.put(entityOption.getName(), entityOption);

        }

        return map;
    }

    /**
     * 大小分
     *
     * @return
     * @throws JSONException
     */
    public static Map<String, EntityOption> optionDX(JSONArray optionList,
                                                     String plate, HashMap<String, List<PlateBean>> plateListMap) throws JSONException {

        Map<String, EntityOption> map = null;

        int len = optionList.length();

        if (len > 0) {

            map = new TreeMap<String, EntityOption>();

        }

        if (StringUtils.isNotBlank(plate)) {
            plate = "\n" + "(" + plate + ")";
        } else {
            plate = "";
        }

        List<String> spList = new ArrayList<>();

        List<String> optionNameList = new ArrayList<>();

        List<String> optionMessageList = new ArrayList<>();

        for (int i = 0; i < len; i++) {

            JSONObject jo = optionList.getJSONObject(i);

            String sp = jo.getString(JsonAppParam.sp);

            JSONObject option = jo.getJSONObject("option");

            EntityOption entityOption = new EntityOption(
                    option.getString(JsonAppParam.name),
                    option.getString(JsonAppParam.message),
                    option.getString(JsonAppParam.value), sp);
            entityOption.setSubTypeName(LotterySubTypeEnum.JCLQ_DX.name());
            entityOption.setSubType("大小分" + plate);
            map.put(entityOption.getName(), entityOption);

            spList.add(sp);
            optionNameList.add(option.getString(JsonAppParam.name));
            optionMessageList.add(option.getString(JsonAppParam.message));

        }

        if (plateListMap != null && map != null) {
            Set<String> keys = plateListMap.keySet();

            for (String key : keys) {
                List<PlateBean> plateList = plateListMap.get(key);
                for (int i = 0; i < spList.size(); i++) {
                    PlateBean plateBean = new PlateBean();
                    plateBean.setSp(spList.get(i));
                    plateBean.setPlayTypeName("大小分");
                    plateBean.setPlate(key);
                    plateBean.setOptionName(optionNameList.get(i));
                    plateBean.setOptionMessage(optionMessageList.get(i));
                    plateList.add(plateBean);
                }
            }


        }

        return map;

    }

    /**
     * 让分
     *
     * @return
     * @throws JSONException
     */
    public static Map<String, EntityOption> optionRF(JSONArray optionList,
                                                     String plate, HashMap<String, List<PlateBean>> plateListMap) throws JSONException {

        Map<String, EntityOption> map = null;

        int len = optionList.length();

        if (len > 0) {

            map = new TreeMap<String, EntityOption>();

        }

        if (StringUtils.isNotBlank(plate)) {
            try {
                double plateNum = Double.parseDouble(plate);

                plate=plateNum>0?"\n"+"("+"+"+plateNum+")":"\n" + "(" + plate + ")";

            }catch (Exception e){
                plate = "\n" + "(" + plate + ")";
                e.printStackTrace();
            }
            //
        } else {
            plate = "";
        }

        List<String> spList = new ArrayList<>();

        List<String> optionNameList = new ArrayList<>();

        List<String> optionMessageList = new ArrayList<>();

        for (int i = 0; i < len; i++) {

            JSONObject jo = optionList.getJSONObject(i);

            String sp = jo.getString(JsonAppParam.sp);

            JSONObject option = jo.getJSONObject("option");

            EntityOption entityOption = new EntityOption(
                    option.getString(JsonAppParam.name),
                    option.getString(JsonAppParam.message),
                    option.getString(JsonAppParam.value), sp);

            entityOption.setSubTypeName(LotterySubTypeEnum.JCLQ_RF.name());

            entityOption.setSubType("让分" + plate);

            map.put(entityOption.getName(), entityOption);

            spList.add(sp);

            optionNameList.add(option.getString(JsonAppParam.name));

            optionMessageList.add(option.getString(JsonAppParam.message));

        }

        if (plateListMap != null && map != null) {

            Set<String> keys = plateListMap.keySet();
            for (String key : keys) {
                List<PlateBean> plateList = plateListMap.get(key);
                for (int i = 0; i < spList.size(); i++) {
                    PlateBean plateBean = new PlateBean();
                    plateBean.setSp(spList.get(i));
                    plateBean.setPlayTypeName("主");
                    plateBean.setPlate(key);
                    plateBean.setOptionName(optionNameList.get(i));
                    plateBean.setOptionMessage(optionMessageList.get(i));
                    plateList.add(plateBean);
                }
            }

        }

        return map;
    }

    /**
     * 胜分差
     *
     * @return
     * @throws JSONException
     */
    public static Map<String, EntityOption> optionSFC(JSONArray optionList) throws JSONException {

        Map<String, EntityOption> map = null;

        int len = optionList.length();

        if (len > 0) {

            map = new TreeMap<String, EntityOption>();

        }

        for (int i = 0; i < len; i++) {

            JSONObject jo = optionList.getJSONObject(i);

            String sp = jo.getString(JsonAppParam.sp);

            JSONObject option = jo.getJSONObject("option");

            EntityOption entityOption = new EntityOption(option.getString(JsonAppParam.name), option.getString(JsonAppParam.message), "", sp);

            entityOption.setSubTypeName(LotterySubTypeEnum.JCLQ_SFC.name());

            entityOption.setSubType(LotterySubTypeEnum.JCLQ_SFC.message());

            map.put(entityOption.getName(), entityOption);

        }

        return map;

    }

    /**
     * 让分胜负彩果
     *
     * @param prizedResultList
     * @param resultJson
     * @throws JSONException
     */
    public static void prizedResultRF(List<PrizedResult> prizedResultList,
                                      JSONObject resultJson, String plate) throws JSONException {

        PrizedResult rqspfPrizedResult = new PrizedResult(
                resultJson.getString(JsonAppParam.name),
                resultJson.getString(JsonAppParam.message));
        rqspfPrizedResult.setSubTypeName(LotterySubTypeEnum.JCLQ_RF.name());
        rqspfPrizedResult.setSubType("让分");
        prizedResultList.add(rqspfPrizedResult);

    }

    /**
     * 胜分差彩果
     *
     * @param prizedResultList
     * @param resultJson
     * @throws JSONException
     */
    public static void prizedResultSFC(List<PrizedResult> prizedResultList,
                                       JSONObject resultJson) throws JSONException {

        PrizedResult rqspfPrizedResult = new PrizedResult(
                resultJson.getString(JsonAppParam.name),
                resultJson.getString(JsonAppParam.message));
        rqspfPrizedResult.setSubTypeName(LotterySubTypeEnum.JCLQ_SFC.name());
        rqspfPrizedResult.setSubType("胜分差");
        prizedResultList.add(rqspfPrizedResult);

    }

    /**
     * 胜负彩果
     *
     * @param prizedResultList
     * @param resultJson
     * @throws JSONException
     */
    public static void prizedResultSF(List<PrizedResult> prizedResultList,
                                      JSONObject resultJson) throws JSONException {

        PrizedResult rqspfPrizedResult = new PrizedResult(
                resultJson.getString(JsonAppParam.name),
                resultJson.getString(JsonAppParam.message));
        rqspfPrizedResult.setSubTypeName(LotterySubTypeEnum.JCLQ_SF.name());
        rqspfPrizedResult.setSubType("胜负");
        prizedResultList.add(rqspfPrizedResult);

    }

    /**
     * 大小分彩果
     *
     * @param prizedResultList
     * @param resultJson
     * @param plate
     * @throws JSONException
     */
    public static void prizedResultDX(List<PrizedResult> prizedResultList,
                                      JSONObject resultJson, String plate) throws JSONException {

        PrizedResult rqspfPrizedResult = new PrizedResult(resultJson.getString(JsonAppParam.name),
                resultJson.getString(JsonAppParam.message));
        rqspfPrizedResult.setSubTypeName(LotterySubTypeEnum.JCLQ_DX.name());
        rqspfPrizedResult.setSubType("大小分");
        prizedResultList.add(rqspfPrizedResult);

    }

}
