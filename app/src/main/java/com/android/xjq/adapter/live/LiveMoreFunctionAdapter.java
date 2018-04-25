package com.android.xjq.adapter.live;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.view.OnMyClickListener;
import com.android.xjq.R;
import com.android.xjq.adapter.main.MyBaseAdapter;
import com.android.xjq.model.live.LiveFunctionEnum;

import java.util.Arrays;

/**
 * Created by lingjiu on 2018/2/7.
 */

public class LiveMoreFunctionAdapter extends MyBaseAdapter<LiveFunctionEnum> {

    private OnMyClickListener<LiveFunctionEnum> onMyClickListener;

    public void setOnMyClickListener(OnMyClickListener<LiveFunctionEnum> onMyClickListener) {
        this.onMyClickListener = onMyClickListener;
    }

    public LiveMoreFunctionAdapter(Context context) {
        super(context);
        initData();
    }

    private void initData() {
        LiveFunctionEnum[] values = LiveFunctionEnum.values();
        list = Arrays.asList(values);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_live_more_function, null);
        }
        final LiveFunctionEnum liveFunctionEnum = list.get(i);
        ((ImageView) convertView.findViewById(R.id.functionIv)).setImageResource(liveFunctionEnum.getResId());
        ((TextView) convertView.findViewById(R.id.functionTv)).setText(liveFunctionEnum.getMessage());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMyClickListener != null)
                    onMyClickListener.onClick(v, i, liveFunctionEnum);
            }
        });
        return convertView;
    }

}
