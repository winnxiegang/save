package com.android.banana.commlib.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.banana.commlib.R;

import java.util.List;

public class SelectorListAdapter extends BaseAdapter {

    private Context mCtx;
    private List<Integer> mData;

    public SelectorListAdapter(Context context, List<Integer> list) {
        this.mCtx = context;
        this.mData = list;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.item_selector_list, null);
            vh.textview = (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.textview.setText("x" + mData.get(position));
       /* if (position == 0) {
            vh.textview.setBackground(ContextCompat.getDrawable(mCtx, R.drawable.shape_round_top_radius_bg));
        } else if (position == mData.size() - 1) {
            vh.textview.setBackground(ContextCompat.getDrawable(mCtx, R.drawable.shape_round_bottom_radius_bg));
        }*/
        return convertView;
    }

    static class ViewHolder {
        TextView textview;
    }
}