package com.android.residemenu.lt_lib.utils.jjc.zucai;


import com.android.residemenu.lt_lib.model.jjc.EntityOption;
import com.android.residemenu.lt_lib.model.jjc.PrizedResult;
import com.android.residemenu.lt_lib.model.lt.LotterySubType;
import com.android.residemenu.lt_lib.utils.JsonAppParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ZucaiEntityOptionUtil {

	/**
	 * 
	 * 
	 * @param optionList
	 * @return
	 * @throws JSONException
	 */

	public static Map<String, EntityOption> option(JSONArray optionList, LotterySubType lotterySubType)
			throws JSONException {

		int len = optionList.length();

		if(len==0){

			return null;
		}

		Map<String, EntityOption> map = null;

		if (len > 0) {
			map = new HashMap<String, EntityOption>();

		}

		for (int i = 0; i < len; i++) {

			JSONObject jo = optionList.getJSONObject(i);

			JSONObject option = jo.getJSONObject("option");

			EntityOption entityOption = new EntityOption(option.getString(JsonAppParam.name),
					option.getString(JsonAppParam.message), option.getString(JsonAppParam.value));
			entityOption.setSubTypeName(lotterySubType.getName());
			// entityOption.setSubType("让球");
			map.put(entityOption.getName(), entityOption);

		}

		return map;
	}


	public static Map<String, EntityOption> optionJQS(JSONArray homeOptionList, String name, String message)
			throws JSONException {

		int len = homeOptionList.length();

		Map<String, EntityOption> map = null;

		if (len > 0) {
			map = new HashMap<String, EntityOption>();

		}

		for (int i = 0; i < len; i++) {// 主队

			JSONObject jo = homeOptionList.getJSONObject(i);

			JSONObject option = jo.getJSONObject("option");

			EntityOption entityOption = new EntityOption(option.getString(JsonAppParam.name),
					option.getString("message"), "");
			entityOption.setSubTypeName(name);
			entityOption.setSubType(message);
			map.put(entityOption.getName(), entityOption);

		}

		return map;
	}

	/**
	 * 半全场
	 *
	 * @param optionList
	 * @param lotterySubType
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, EntityOption> optionBQC(JSONArray optionList, LotterySubType lotterySubType,
			String message) throws JSONException {

		int len = optionList.length();

		Map<String, EntityOption> map = null;

		if (len > 0) {
			map = new HashMap<String, EntityOption>();

		}

		for (int i = 0; i < len; i++) {

			JSONObject jo = optionList.getJSONObject(i);

			JSONObject option = jo.getJSONObject("option");

			EntityOption entityOption = new EntityOption(option.getString(JsonAppParam.name),
					option.getString(JsonAppParam.message), option.getString(JsonAppParam.value));
			entityOption.setSubTypeName(lotterySubType.getName());
			entityOption.setSubType(message);
			map.put(entityOption.getName(), entityOption);

		}

		return map;
	}

	/**
	 *
	 * @param prizedResultList
	 * @param resultJson
	 * @param lotterySubType
	 * @throws JSONException
	 */
	public static void prizedResult(List<PrizedResult> prizedResultList, JSONObject resultJson,
			LotterySubType lotterySubType) throws JSONException {

		PrizedResult bfPrizedResult = new PrizedResult(resultJson.getString(JsonAppParam.name),
				resultJson.getString(JsonAppParam.message));
		bfPrizedResult.setSubTypeName(lotterySubType.getName());
		// bfPrizedResult.setSubType("比分");

		prizedResultList.add(bfPrizedResult);

	}

	/**
	 * 进球数彩果
	 *
	 * @param prizedResultList
	 * @param resultJson
	 * @param name
	 * @param message
	 *            主|客
	 * @throws JSONException
	 */
	public static void prizedResultJQS(List<PrizedResult> prizedResultList, JSONObject resultJson,
			String name, String message) throws JSONException {

		PrizedResult bfPrizedResult = new PrizedResult(resultJson.getString(JsonAppParam.name),
				resultJson.getString(JsonAppParam.message));
		bfPrizedResult.setSubTypeName(name);
		bfPrizedResult.setSubType(message);

		prizedResultList.add(bfPrizedResult);

	}

	/**
	 * 半全场彩果
	 *
	 * @param prizedResultList
	 * @param resultJson
	 * @param lotterySubType
	 * @param message
	 * @throws JSONException
	 */
	public static void prizedResultBQC(List<PrizedResult> prizedResultList, JSONObject resultJson,
			LotterySubType lotterySubType, String message) throws JSONException {

		PrizedResult bfPrizedResult = new PrizedResult(resultJson.getString(JsonAppParam.name), resultJson.getString(JsonAppParam.message));
		bfPrizedResult.setSubTypeName(lotterySubType.getName());
		bfPrizedResult.setSubType(message);

		prizedResultList.add(bfPrizedResult);

	}

}
