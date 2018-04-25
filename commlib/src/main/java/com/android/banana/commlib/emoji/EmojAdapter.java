package com.android.banana.commlib.emoji;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.banana.commlib.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhouyi on 2016/1/5.
 */
public class EmojAdapter extends BaseAdapter {

    private Context context;

    private List<EmojBean> list = new ArrayList<>();

    public EmojAdapter(Context context, List<EmojBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return 21;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_emoj, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position >= list.size()) {
            if (position == 20) {
                holder.imageView.setImageResource(R.drawable.icon_emoj_delete);
                holder.imageView.setVisibility(View.VISIBLE);
            } else {
                holder.imageView.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setImageResource(list.get(position).getResId());
        }
        return convertView;
    }


    static class ViewHolder {
        ImageView imageView;

        ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}
