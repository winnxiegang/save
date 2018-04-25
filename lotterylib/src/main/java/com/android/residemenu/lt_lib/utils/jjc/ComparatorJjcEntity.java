package com.android.residemenu.lt_lib.utils.jjc;


import com.android.residemenu.lt_lib.model.lt.OrderEntity;

import java.util.Comparator;

public class ComparatorJjcEntity implements Comparator<OrderEntity> {

	@Override
	public int compare(OrderEntity lhs, OrderEntity rhs) {

		int i = lhs.getIssueNo().compareTo(rhs.getIssueNo());

		if (i == 0) {
			return lhs.getGameNo().compareTo(rhs.getGameNo());
		} else {
			return i;
		}

	}

}
