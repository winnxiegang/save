package com.android.residemenu.lt_lib.utils.jjc;


import com.android.residemenu.lt_lib.enumdata.LotterySelecterEnum;
import com.android.residemenu.lt_lib.model.jjc.GridData;
import com.android.residemenu.lt_lib.model.lt.OptionData;

import java.util.ArrayList;
import java.util.List;

public final class JingjicaiOptionUtil {

	/**
	 * 竞彩数据转换
	 * 
	 * @param gridDatas
	 * @return
	 */
	public static List<OptionData> gridToOptionData(List<GridData> gridDatas) {

		List<OptionData> dataList = new ArrayList<OptionData>();

		for (GridData gridData : gridDatas) {

			OptionData data = new OptionData();

			data.setSelecterEnum(LotterySelecterEnum.valueOf(gridData.getSelecterEnum()));

			data.setIndex(gridData.getIndex());

			data.setCode(gridData.getOption());

			data.setTitle(gridData.getTitle());

			data.setOdds(gridData.getOdds());

			data.setSelected(gridData.isChecked());

			data.setEnabled(gridData.isEnabled());

			dataList.add(data);

		}

		return dataList;
	}

}
