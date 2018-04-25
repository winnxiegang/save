package com.android.residemenu.lt_lib.utils.jjc;


import com.android.residemenu.lt_lib.model.jjc.PrizedResult;

import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;


public class ComparatorJjcResult implements Comparator<PrizedResult> {

	@Override
	public int compare(PrizedResult lhs, PrizedResult rhs) {

		if (StringUtils.isNotBlank(lhs.getSubTypeName()) && StringUtils.isNotBlank(rhs.getSubTypeName())) {
			return lhs.getSubTypeName().compareTo(rhs.getSubTypeName());
		} else {
			return 0;
		}

	}

}
