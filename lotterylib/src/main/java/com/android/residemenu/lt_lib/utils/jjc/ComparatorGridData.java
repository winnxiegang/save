package com.android.residemenu.lt_lib.utils.jjc;


import com.android.residemenu.lt_lib.model.jjc.GridData;

import java.util.Comparator;

public class ComparatorGridData implements Comparator<GridData> {

	@Override
	public int compare(GridData lhs, GridData rhs) {

		return lhs.getOption().compareTo(rhs.getOption());
	}

}
