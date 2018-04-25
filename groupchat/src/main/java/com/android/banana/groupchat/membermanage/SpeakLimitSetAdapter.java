package com.android.banana.groupchat.membermanage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.groupchat.bean.SpeakSetTypeBean;

import java.util.List;


/**
 * Created by zaozao on 2017/5/31.
 */

public class SpeakLimitSetAdapter extends MyBaseAdapter<SpeakSetTypeBean> {

    public SpeakLimitSetAdapter(Context context, List<SpeakSetTypeBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_speak_set, null);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        setItemView(viewHolder,position);

        return convertView;
    }

    private void setItemView(ViewHolder viewHolder,int position){

        if(list.get(position).isSelect()){
            viewHolder.selectIv.setVisibility(View.VISIBLE);
        }else{
            viewHolder.selectIv.setVisibility(View.GONE);
        }
        viewHolder.titleTv.setText(list.get(position).getMessage());

        if(position == list.size()-1){
            viewHolder.cutLine.setVisibility(View.GONE);
        }else{
            viewHolder.cutLine.setVisibility(View.VISIBLE);
        }
    }



    static class ViewHolder {
        TextView titleTv;

        View cutLine;

        ImageView selectIv;

        ViewHolder(View view) {
            selectIv = (ImageView) view.findViewById(R.id.selectIv);
            cutLine = (View)view.findViewById(R.id.cutLine);
            titleTv = (TextView) view.findViewById(R.id.titleTv);
        }
    }
}
