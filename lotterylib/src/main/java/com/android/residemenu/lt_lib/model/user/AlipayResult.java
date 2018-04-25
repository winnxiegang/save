package com.android.residemenu.lt_lib.model.user;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class AlipayResult {

	private static final Map<String, String> sResultStatus;

	private String mResult;

	private String resultStatus = null;
	private String memo = null;
	private String result = null;

	private String code;

	/**
	 * ￼resultStatus={9000};memo={};result={auth_code=
	 * "1aa58efba12040e3a31e4fc96d 9558e7"&alipay_user_id="2088202443801075"}
	 * 
	 * @param result
	 */

	public AlipayResult(String result) {
		this.mResult = result;
		parseResult();
	}

	static {
		sResultStatus = new HashMap<String, String>();
		sResultStatus.put("9000", "操作成功");
		sResultStatus.put("4000", "系统异常");
		sResultStatus.put("4001", "数据格式不正确");
		sResultStatus.put("4003", "该用户绑定的支付宝账户被冻结或不允许支付");
		sResultStatus.put("4004", "该用户已解除绑定");
		sResultStatus.put("4005", "绑定失败或没有绑定");
		sResultStatus.put("4006", "订单支付失败");
		sResultStatus.put("4010", "重新绑定账户");
		sResultStatus.put("6000", "支付服务正在进行升级操作");
		sResultStatus.put("6001", "用户中途取消操作");
		sResultStatus.put("7001", "网页支付失败");
	}

	public String getResultStatus() {
		return resultStatus;
	}
	
	
	

	// public String getResult() {
	// String src = mResult.replace("{", "");
	// src = src.replace("}", "");
	// return getContent(src, "memo=", ";result");
	// }

	public String getCode() {
		return code;
	}




	private void parseResult() {

		try {
			String src = mResult.replace("{", "");
			src = src.replace("}", "");
			code = getContent(src, "resultStatus=", ";memo");
			if (sResultStatus.containsKey(code)) {
				resultStatus = sResultStatus.get(code);
			} else {
				resultStatus = "其他错误";
			}
			resultStatus += "(" + code + ")";

			memo = getContent(src, "memo=", ";result");
			result = getContent(src, "result=", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getResult() {
		return result;
	}

//	public JSONObject string2JSON(String src, String split) {
//		JSONObject json = new JSONObject();
//
//		try {
//			String[] arr = src.split(split);
//			for (int i = 0; i < arr.length; i++) {
//				String[] arrKey = arr[i].split("=");
//				json.put(arrKey[0], arr[i].substring(arrKey[0].length() + 1));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return json;
//	}

	private String getContent(String src, String startTag, String endTag) {

		String content = src;

		int start = src.indexOf(startTag);

		start += startTag.length();

		try {
			if (endTag != null) {
				int end = src.indexOf(endTag);
				content = src.substring(start, end);
			} else {
				content = src.substring(start);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return content;
	}

}
