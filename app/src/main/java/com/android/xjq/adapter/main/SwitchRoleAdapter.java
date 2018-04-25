package com.android.xjq.adapter.main;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.bean.live.main.UserRoleInfo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/3/6 12:01.
 */
public class SwitchRoleAdapter extends MyBaseAdapter<UserRoleInfo> {

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public SwitchRoleAdapter(Context context, List<UserRoleInfo> list) {

        super(context,list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.item_switch_role_listview, null);
        }
        ViewHolder holder = ViewHolder.getTag(convertView);

        setItemView(holder,position);

        return convertView;
    }

    private void setItemView(ViewHolder holder, final int position) {

        UserRoleInfo bean =  list.get(position);

        setImageShow(holder.portraitIv,bean.getAvatarUrl());

        setUserMedalShow(bean.getUserMedalList(),holder.userInfoLayout,1);

        holder.selectRoleCb.setChecked(bean.isCurrentRole());

        holder.userNameTv.setText(bean.getLoginName());

        if (bean.isDefaultRole()) {
            holder.setDefaultRoleTv.setText("默认");
            holder.setDefaultRoleTv.setTextColor(Color.BLACK);
        } else {
            holder.setDefaultRoleTv.setText("设为默认");
            holder.setDefaultRoleTv.setTextColor(Color.parseColor("#2098ed"));
        }


        holder.setDefaultRoleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < list.size(); i++) {

                    list.get(i).setDefaultRole(false);
                }

                list.get(position).setDefaultRole(true);

                notifyDataSetChanged();

                if (onClickListener!=null) {

                    onClickListener.setDefaultListener(position);
                }
            }
        });

        holder.selectRoleCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {

                    list.get(i).setCurrentRole(false);
                }

                list.get(position).setCurrentRole(true);

                notifyDataSetChanged();

                if (onClickListener!=null) {

                    onClickListener.selectorRoleListener(position);
                }
            }
        });
    }

    public interface OnClickListener{
        void setDefaultListener(int position);

        void selectorRoleListener(int position);
    }


    static class ViewHolder {
        @BindView(R.id.portraitIv)
        SimpleDraweeView portraitIv;
        @BindView(R.id.userInfoLayout)
        LinearLayout userInfoLayout;
        @BindView(R.id.userNameTv)
        TextView userNameTv;
        @BindView(R.id.setDefaultRoleTv)
        TextView setDefaultRoleTv;
        @BindView(R.id.selectRoleCb)
        CheckBox selectRoleCb;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }

        public static ViewHolder getTag(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
            }
            return holder;
        }
    }
}
