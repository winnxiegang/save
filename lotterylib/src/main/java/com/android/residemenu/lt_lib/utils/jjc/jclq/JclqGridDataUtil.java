package com.android.residemenu.lt_lib.utils.jjc.jclq;


import com.android.residemenu.lt_lib.enumdata.JclqHhggEnum;
import com.android.residemenu.lt_lib.enumdata.LotterySelecterEnum;
import com.android.residemenu.lt_lib.enumdata.core.jclq.JclqDxEnum;
import com.android.residemenu.lt_lib.enumdata.core.jclq.JclqRaceResultEnum;
import com.android.residemenu.lt_lib.enumdata.core.jclq.JclqSfcEnum;
import com.android.residemenu.lt_lib.model.jjc.GridData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 竞彩篮球期次数据处理
 * 
 * @author leslie
 * 
 * @version $Id: JclqGridDataUtil.java, v 0.1 2014年9月20日 下午3:43:40 leslie Exp $
 */
public final class JclqGridDataUtil {

	/**
	 * 让分
	 * 
	 * @param gridList
	 * @param ggSp
	 * @param enable
	 * @throws JSONException
	 */
	public static void rfData(List<GridData> gridList, JSONObject ggSp, boolean enable) throws JSONException {

		for (int k = 0; k < 2; k++) {

			GridData gridData = new GridData();

			gridData.setIndex(gridList.size());

			gridData.setEnabled(enable);

			gridData.setSelecterEnum(LotterySelecterEnum.JCLQ_RF.name());
			gridData.setPlayType(LotterySelecterEnum.JCLQ_RF.name());
			switch (k) {

			case 0:
                gridData.setOdds(ggSp.getString("hl"));
                gridData.setOption(JclqRaceResultEnum.LOST.name());
                gridData.setBonusOptimizationOption(JclqHhggEnum.RF_LOST.name());
                gridData.setTitle("让客胜");
				break;
			case 1:
                gridData.setOdds(ggSp.getString("hw"));
                gridData.setOption(JclqRaceResultEnum.WIN.name());
                gridData.setBonusOptimizationOption(JclqHhggEnum.RF_WIN.name());
                gridData.setTitle("让主胜");
				break;

			}

			gridList.add(gridData);
		}

	}

	/**
	 * 胜负
	 *
	 * @param gridList
	 * @param ggSp
	 * @param enable
	 * @throws JSONException
	 */
	public static void sfData(List<GridData> gridList, JSONObject ggSp, boolean enable) throws JSONException {

		for (int k = 0; k < 2; k++) {

			GridData gridData = new GridData();

			gridData.setIndex(gridList.size());

			gridData.setEnabled(enable);

			gridData.setSelecterEnum(LotterySelecterEnum.JCLQ_SF.name());
			gridData.setPlayType(LotterySelecterEnum.JCLQ_SF.name());
			switch (k) {

			case 0:
                gridData.setOdds(ggSp.getString("hl"));
                gridData.setOption(JclqRaceResultEnum.LOST.name());
                gridData.setBonusOptimizationOption(JclqHhggEnum.SF_LOST.name());
                gridData.setTitle("客胜");
                break;
			case 1:
                gridData.setOdds(ggSp.getString("hw"));
                gridData.setOption(JclqRaceResultEnum.WIN.name());
                gridData.setBonusOptimizationOption(JclqHhggEnum.SF_WIN.name());
                gridData.setTitle("主胜");
				break;

			}

			gridList.add(gridData);
		}
	}

	/**
	 * 大小分
	 *
	 * @param gridList
	 * @param ggSp
	 * @param enable
	 * @throws JSONException
	 */
	public static void dxData(List<GridData> gridList, JSONObject ggSp, boolean enable) throws JSONException {

		for (int k = 0; k < 2; k++) {

			GridData gridData = new GridData();

			gridData.setIndex(gridList.size());

			gridData.setEnabled(enable);

			gridData.setSelecterEnum(LotterySelecterEnum.JCLQ_DX.name());

			gridData.setPlayType(LotterySelecterEnum.JCLQ_DX.name());

			switch (k) {

			case 0:

                gridData.setOdds(ggSp.getString("big"));
                gridData.setOption(JclqDxEnum.BIG.name());
                gridData.setBonusOptimizationOption(JclqHhggEnum.DX_BIG.name());
                gridData.setTitle("大分");
                break;
			case 1:
                gridData.setOdds(ggSp.getString("small"));
                gridData.setOption(JclqDxEnum.SMALL.name());
                gridData.setBonusOptimizationOption(JclqHhggEnum.DX_SMALL.name());
                gridData.setTitle("小分");
				break;

			}

			gridList.add(gridData);
		}
	}

	/**
	 * 胜分差
	 *
	 * @param gridList
	 * @param sfcGgSp
	 * @param enable
	 * @throws JSONException
	 */
	public static void sfcData(List<GridData> gridList, JSONObject sfcGgSp, boolean enable)
			throws JSONException {

		for (int k = 0; k < 12; k++) {

			GridData gridData = new GridData();

			gridData.setIndex(gridList.size());

			gridData.setSelecterEnum(LotterySelecterEnum.JCLQ_SFC.name());
			gridData.setPlayType(LotterySelecterEnum.JCLQ_SFC.name());
			String title = "";

			String odds = "";

			String option = "";

            String bonusOptimizationOption = "";

			switch (k) {

			case 0:
				odds = sfcGgSp.getString("g1b5");
				option = JclqSfcEnum.G1B5.name();
                bonusOptimizationOption = JclqHhggEnum.SFC_G1B5.name();
				title = JclqSfcEnum.G1B5.message();
				break;
			case 1:
				odds = sfcGgSp.getString("g6b10");
				option = JclqSfcEnum.G6B10.name();
                bonusOptimizationOption = JclqHhggEnum.SFC_G6B10.name();
				title = JclqSfcEnum.G6B10.message();
				break;
			case 2:
				odds = sfcGgSp.getString("g11b15");
				option = JclqSfcEnum.G11B15.name();
                bonusOptimizationOption = JclqHhggEnum.SFC_G11B15.name();
				title = JclqSfcEnum.G11B15.message();
				break;
			case 3:
				odds = sfcGgSp.getString("g16b20");
				option = JclqSfcEnum.G16B20.name();
                bonusOptimizationOption = JclqHhggEnum.SFC_G16B20.name();
				title = JclqSfcEnum.G16B20.message();
				break;
			case 4:
				odds = sfcGgSp.getString("g21b25");
				option = JclqSfcEnum.G21B25.name();
                bonusOptimizationOption = JclqHhggEnum.SFC_G21B25.name();
				title = JclqSfcEnum.G21B25.message();
				break;
			case 5:
				odds = sfcGgSp.getString("g26");
				option = JclqSfcEnum.G26.name();
                bonusOptimizationOption = JclqHhggEnum.SFC_G26.name();
				title = JclqSfcEnum.G26.message();
				break;
			case 6:
				odds = sfcGgSp.getString("h1b5");
				option = JclqSfcEnum.H1B5.name();
                bonusOptimizationOption = JclqHhggEnum.SFC_H1B5.name();
				title = JclqSfcEnum.H1B5.message();
				break;
			case 7:
				odds = sfcGgSp.getString("h6b10");
				option = JclqSfcEnum.H6B10.name();
                bonusOptimizationOption = JclqHhggEnum.SFC_H6B10.name();
				title = JclqSfcEnum.H6B10.message();
				break;
			case 8:
				odds = sfcGgSp.getString("h11b15");
				option = JclqSfcEnum.H11B15.name();
                bonusOptimizationOption = JclqHhggEnum.SFC_H11B15.name();
				title = JclqSfcEnum.H11B15.message();
				break;

			case 9:
				odds = sfcGgSp.getString("h16b20");
				option = JclqSfcEnum.H16B20.name();
                bonusOptimizationOption = JclqHhggEnum.SFC_H16B20.name();

                title = JclqSfcEnum.H16B20.message();
				break;

			case 10:
				odds = sfcGgSp.getString("h21b25");
				option = JclqSfcEnum.H21B25.name();
                bonusOptimizationOption = JclqHhggEnum.SFC_H21B25.name();
                title = JclqSfcEnum.H21B25.message();
				break;

			case 11:
				odds = sfcGgSp.getString("h26");
				option = JclqSfcEnum.H26.name();
                bonusOptimizationOption = JclqHhggEnum.SFC_H26.name();
				title = JclqSfcEnum.H26.message();
				break;
			}

			gridData.setOdds(odds);
			gridData.setOption(option);
            gridData.setBonusOptimizationOption(bonusOptimizationOption);
			gridData.setTitle(title);
			gridData.setEnabled(enable);
			gridList.add(gridData);

		}
	}

}
