package com.android.xjq.model.dynamic;

import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.xjq.bean.homepage.HomePageDynamicDataBean;

/**
 * Created by zaozao on 2018/1/26.
 * 用处：动态对应的多种布局
 */

public class DynamicLayoutSupport implements MultiTypeSupport<HomePageDynamicDataBean> {


    @Override
    public int getTypeLayoutRes(HomePageDynamicDataBean data, int pos) {
        return 0;
    }
}
