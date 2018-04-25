package com.android.xjq.controller.homeCheer;

import android.content.Context;
import android.view.View;

/**
 * Created by zhouyi on 2018/4/19.
 */

public abstract class CheerBaseController {

    protected Context mContext;

    public CheerBaseController(Context context) {
        this.mContext = context;
    }

    public abstract void setView(View view);


}
