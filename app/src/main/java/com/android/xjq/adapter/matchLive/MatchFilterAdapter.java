package com.android.xjq.adapter.matchLive;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.android.xjq.R;
import com.android.xjq.adapter.main.MyBaseAdapter;
import com.android.xjq.bean.matchLive.LeagueInfo;

import java.util.ArrayList;

public class MatchFilterAdapter extends MyBaseAdapter<LeagueInfo> {


    public MatchFilterAdapter(Context context, ArrayList<LeagueInfo> list) {
        super(context, list);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = View.inflate(context, R.layout.item_filter_option, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setData(holder, list.get(position));

        holder.nameTb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    list.get(position).setSelected(true);
                } else {
                    list.get(position).setSelected(false);
                }

            }
        });

        return convertView;
    }

    private void setData(ViewHolder holder, LeagueInfo bean) {

        if (bean.isSelected()) {
            holder.nameTb.setChecked(true);
        } else {
            holder.nameTb.setChecked(false);
        }
        holder.nameTb.setText(bean.getCnShortName());
        holder.nameTb.setTextOff(bean.getCnShortName());
        holder.nameTb.setTextOn(bean.getCnShortName());
        holder.nameTb.setTag(bean.getCnShortName());
    }

    private final class ViewHolder {

        private ToggleButton nameTb;

        public ViewHolder(View view) {

            nameTb = (ToggleButton) view.findViewById(R.id.nameTb);

        }

    }

}
