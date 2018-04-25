package com.android.banana.commlib.view;

import android.view.View;

/**
 * Created by lingjiu on 2018/2/23.
 */

public interface OnMyClickListener<T> {
    void onClick(View view, int position, T t);
}
