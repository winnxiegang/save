package com.android.residemenu.lt_lib.utils.jjc.jczq;

import com.android.residemenu.lt_lib.model.jjc.football.AbstractInfoItem;
import com.android.residemenu.lt_lib.model.jjc.football.InfoFutureItem;
import com.android.residemenu.lt_lib.model.jjc.football.InfoOddsItem;
import com.android.residemenu.lt_lib.model.jjc.football.InfoTeamPoint;
import com.android.residemenu.lt_lib.model.jjc.football.InfoVsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author leslie
 * 
 * @version $Id: FbInfoUtil.java, v 0.1 2015-1-14 下午2:41:54 leslie Exp $
 */
public class FbInfoUtil {

	private static final String TAG = FbInfoUtil.class.getSimpleName();

	public static List<AbstractInfoItem> warpVs(JSONArray ja, String homeId, String guestId)
			throws JSONException {

		int len = ja.length();

		List<AbstractInfoItem> items = new ArrayList<AbstractInfoItem>(len);

		for (int i = 0; i < len; i++) {

			JSONObject jo = ja.getJSONObject(i);

			int hs = jo.getInt("homeScore");

			int gs = jo.getInt("guestScore");
			String gmtStart = jo.getString("gmtStart");
			InfoVsItem item = new InfoVsItem();
			item.setLeagueName(jo.getString("matchName"));
			item.setBeginTime(gmtStart.substring(0, 10));
			item.setHomeName(jo.getString("homeTeamName"));
			item.setScore(hs + ":" + gs);
			item.setGuestName(jo.getString("guestTeamName"));

			String hId = jo.getString("homeTeamId");

			String gId = jo.getString("guestTeamId");

			String result = "平";

			if (homeId.equals(hId) && guestId.equals(gId)) {// 两队历史交锋 主是主

				if (hs > gs) {
					result = "胜";
				} else if (hs < gs) {
					result = "负";
				}

			} else if (homeId.equals(gId) && guestId.equals(hId)) {// 两队历史交锋 主是客

				if (hs > gs) {
					result = "负";
				} else if (hs < gs) {
					result = "胜";
				}

			} else {// 主客队跟其它队比较

				if (homeId.equals(hId) || guestId.equals(hId)) {// 比赛的两对都是主

					if (hs > gs) {
						result = "胜";
					} else if (hs < gs) {
						result = "负";
					}
				} else {

					if (hs > gs) {
						result = "负";
					} else if (hs < gs) {
						result = "胜";
					}
				}

			}

			item.setResult(result);
			items.add(item);
		}

		return items;

	}

	/**
	 * 未来赛事
	 * 
	 */
	public static List<AbstractInfoItem> warpFutreVs(JSONArray ja, Date now) throws JSONException {

		int len = ja.length();

		List<AbstractInfoItem> items = new ArrayList<AbstractInfoItem>(len);

		for (int i = 0; i < len; i++) {

			JSONObject jo = ja.getJSONObject(i);

			String gmtStart = jo.getString("gmtStart");
			InfoFutureItem item = new InfoFutureItem();
			item.setLeagueName(jo.getString("matchName"));
			item.setBeginTime(gmtStart.substring(0, 10));
			item.setHomeName(jo.getString("homeTeamName"));
			item.setGuestName(jo.getString("guestTeamName"));

			//Date date = TimeUtil.stringToDate(gmtStart, null);

			//String n = TimeUtil.date1SubDate2(now, date);

			item.setDifferTime("1" + "天");
			items.add(item);
		}

		return items;

	}

	/**
	 * 欧赔数据
	 *
	 * @param company
	 * @param ja
	 * @return
	 * @throws JSONException
	 */
	public static List<InfoOddsItem> warpEurOdds(JSONObject company, JSONArray ja) throws JSONException {

		int len = ja.length();

		List<InfoOddsItem> items = null;

		if (len > 0) {
			items = new ArrayList<InfoOddsItem>(len);

			for (int i = 0; i < len; i++) {

				InfoOddsItem infoOddsItem = new InfoOddsItem();

				JSONObject jo = ja.getJSONObject(i);

				String name = company.getString(jo.getString("ci"));

				infoOddsItem.setCompany(name);

				infoOddsItem.setFirstHomeSp(jo.getString("fho"));
				infoOddsItem.setFirstDrawSp(jo.getString("fd"));
				infoOddsItem.setFirstGuestSp(jo.getString("fgo"));

				infoOddsItem.setNowHomeSp(jo.getString("ho"));
				infoOddsItem.setNowDrawSp(jo.getString("d"));
				infoOddsItem.setNowGuestSp(jo.getString("go"));

				items.add(infoOddsItem);
			}

		}

		return items;

	}

	/**
	 * 亚赔数据
	 *
	 * @param company
	 * @param ja
	 * @return
	 * @throws JSONException
	 */
	public static List<InfoOddsItem> warpAsiaOdds(JSONObject company, JSONArray ja) throws JSONException {

		int len = ja.length();

		List<InfoOddsItem> items = null;

		if (len > 0) {
			items = new ArrayList<InfoOddsItem>(len);

			for (int i = 0; i < len; i++) {

				InfoOddsItem infoOddsItem = new InfoOddsItem();

				JSONObject jo = ja.getJSONObject(i);

				String name = company.getString(jo.getString("ci"));

				infoOddsItem.setCompany(name);

				//infoOddsItem.setFirstHomeSp(AppUtil.formatAsianOdds((jo.getDouble("fho") - 1)));

				infoOddsItem.setFirstDrawSp(jo.getString("fChinesePlate"));
				//infoOddsItem.setFirstGuestSp(AppUtil.formatAsianOdds((jo.getDouble("fgo") - 1)));

				//infoOddsItem.setNowHomeSp(AppUtil.formatAsianOdds((jo.getDouble("ho") - 1)));
				infoOddsItem.setNowDrawSp(jo.getString("chinesePlate"));
				//infoOddsItem.setNowGuestSp(AppUtil.formatAsianOdds((jo.getDouble("go") - 1)));

				items.add(infoOddsItem);
			}

		}

		return items;

	}

	/**
	 * 大小盘
	 *
	 * @param company
	 * @param ja
	 * @return
	 * @throws JSONException
	 */
	public static List<InfoOddsItem> warpDxpOdds(JSONObject company, JSONArray ja) throws JSONException {

		int len = ja.length();

		List<InfoOddsItem> items = null;

		if (len > 0) {
			items = new ArrayList<InfoOddsItem>(len);

			for (int i = 0; i < len; i++) {

				InfoOddsItem infoOddsItem = new InfoOddsItem();

				JSONObject jo = ja.getJSONObject(i);

				String name = company.getString(jo.getString("ci"));

				infoOddsItem.setCompany(name);


				infoOddsItem.setFirstHomeSp((new DecimalFormat("#0.00").format((jo.getDouble("foo")) - 1)) + "");
				infoOddsItem.setFirstDrawSp(jo.getString("fp"));
				infoOddsItem.setFirstGuestSp((new DecimalFormat("#0.00").format((jo.getDouble("fdo")) - 1)) + "");

				infoOddsItem.setNowHomeSp((new DecimalFormat("#0.00").format((jo.getDouble("oo")) - 1)) + "");
				infoOddsItem.setNowDrawSp(jo.getString("p"));
				infoOddsItem.setNowGuestSp((new DecimalFormat("#0.00").format((jo.getDouble("uo")) - 1)) + "");

				items.add(infoOddsItem);
			}

		}

		return items;

	}

	/**
	 * 积分
	 *
	 * @param jo
	 * @return
	 * @throws JSONException
	 */
	public static InfoTeamPoint wrapPointItem(JSONObject jo) throws JSONException {

		InfoTeamPoint teamPoint = new InfoTeamPoint();

		if (jo == null) {
			teamPoint.setRank("-");
			teamPoint.setPoint("-");
			teamPoint.setCt("-");
			teamPoint.setWinN("-");
			teamPoint.setDrawN("-");
			teamPoint.setLostN("-");
			teamPoint.setInGoal("-");
			teamPoint.setLostGoal("-");
			teamPoint.setRealGoal("-");
		} else {
			teamPoint.setRank(jo.getString("rk"));
			teamPoint.setPoint(jo.getString("pt"));
			teamPoint.setCt(jo.getString("ct"));
			teamPoint.setWinN(jo.getString("wn"));
			teamPoint.setDrawN(jo.getString("dw"));
			teamPoint.setLostN(jo.getString("lt"));
			teamPoint.setInGoal(jo.getString("ig"));
			teamPoint.setLostGoal(jo.getString("lg"));
			teamPoint.setRealGoal(jo.getString("rl"));
		}

		return teamPoint;

	}

	public static List<AbstractInfoItem> warpTeamPoint(JSONObject jo) throws JSONException {

		List<AbstractInfoItem> items = new ArrayList<AbstractInfoItem>(4);

		JSONObject jsonObj;
		if (jo.has("SR_AAR")) {

			jsonObj = jo.getJSONObject("SR_AAR");
		} else {
			jsonObj = null;
		}

		InfoTeamPoint teamPoint = wrapPointItem(jsonObj);
		teamPoint.setItemTitle("总");
		items.add(teamPoint);

		if (jo.has("SR_HAR")) {
			jsonObj = jo.getJSONObject("SR_HAR");
		} else {
			jsonObj = null;
		}

		teamPoint = wrapPointItem(jsonObj);
		teamPoint.setItemTitle("主");
		items.add(teamPoint);

		if (jo.has("SR_GAR")) {
			jsonObj = jo.getJSONObject("SR_GAR");
		} else {
			jsonObj = null;
		}

		teamPoint = wrapPointItem(jsonObj);
		teamPoint.setItemTitle("客");
		items.add(teamPoint);

		if (jo.has("lastSix")) {
			jsonObj = jo.getJSONObject("lastSix");
		} else {
			jsonObj = null;
		}

		teamPoint = wrapPointItem(jsonObj);
		teamPoint.setItemTitle("近6场");
		teamPoint.setRank("-");
		items.add(teamPoint);

		return items;

	}

}
