package com.android.xjq.adapter.live;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.xjq.adapter.main.MyBaseAdapter;
import com.android.xjq.bean.live.AnchorUserInfoClientSimple;

import java.util.List;

/**
 * Created by lingjiu on 2018/1/30.
 */

public class LiveDropHostAdapter extends MyBaseAdapter<AnchorUserInfoClientSimple> {


    public LiveDropHostAdapter(Context context, List<AnchorUserInfoClientSimple> list) {
        super(context, list);
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        convertView = View.inflate(context, R.layout.item_drop_host_layout, null);
        AnchorUserInfoClientSimple bean = list.get(i);
        ImageView hostIv = (ImageView) convertView.findViewById(R.id.hostIv);
        TextView hostNameTv = (TextView) convertView.findViewById(R.id.hostNameTv);
        TextView fansTv = (TextView) convertView.findViewById(R.id.fansTv);
        setImageShow(hostIv, bean.userLogoUrl);
        hostNameTv.setText(bean.userName);
        fansTv.setText(String.format(context.getString(R.string.fans_number), String.valueOf(bean.followMyUserCount)));
        TextView focusTv = (TextView) convertView.findViewById(R.id.focusTv);
        focusTv.setText(bean.focus ? "取消" : "关注");
        focusTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.onClickFocus(i);
            }
        });

        convertView.findViewById(R.id.anchorInfoLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.onClickUserInfo(i);
            }
        });

        return convertView;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private ClickListener clickListener;

    public interface ClickListener {
        void onClickFocus(int pos);

        void onClickUserInfo(int pos);
    }
}

