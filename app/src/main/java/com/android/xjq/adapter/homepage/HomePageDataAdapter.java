package com.android.xjq.adapter.homepage;

import android.content.Context;
import android.widget.TextView;

import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.xjq.R;

import java.util.List;

/**
 * Created by zaozao on 2018/1/26.
 * 用处：个人主页数据适配
 */

public class HomePageDataAdapter extends MultiTypeSupportAdapter<String>{

    public HomePageDataAdapter(Context context, List list, int layoutRes, MultiTypeSupport typeSupport) {
        super(context, list, layoutRes, typeSupport);
    }


    @Override
    public void onBindNormalHolder(ViewHolder holder, String s, int position) {
        ((TextView)holder.getView(R.id.text)).setText(s);
    }
}
