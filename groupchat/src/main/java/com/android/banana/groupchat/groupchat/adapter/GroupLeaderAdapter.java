package com.android.banana.groupchat.groupchat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.groupchat.bean.ChatRoomMemberBean;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.view.DragLinearLayout;
import com.android.library.Utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kokuma on 2017/11/1.
 */

public class GroupLeaderAdapter extends MyBaseAdapter<ChatRoomMemberBean.GroupMemberSimpleBean> {

    public View.OnClickListener onClickListener;
    public boolean isOpenCheckbox;
    public ArrayList<String> listId;

    public GroupLeaderAdapter(Context context, List<ChatRoomMemberBean.GroupMemberSimpleBean> list) {
        super(context, list);
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size() - 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        GroupLeaderAdapter.ViewHolder viewHolder = null;
        int type = getItemViewType(i);
        LogUtils.i("xxl", "GroupLeaderAdapter-getView-" + type);
        if (type == 1) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_leader_add, viewGroup, false);
                viewHolder = new GroupLeaderAdapter.ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (GroupLeaderAdapter.ViewHolder) convertView.getTag();
            }
        } else {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_leader_set, viewGroup, false);
                viewHolder = new GroupLeaderAdapter.ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (GroupLeaderAdapter.ViewHolder) convertView.getTag();
            }
        }
        if (type == 1) {
            convertView.setOnClickListener(onClickListener);
        } else {
            convertView.setOnClickListener(null);
            setItemView(viewHolder, i);
        }
        return convertView;
    }

    private void setItemView(final GroupLeaderAdapter.ViewHolder viewHolder, final int position) {
        final ChatRoomMemberBean.GroupMemberSimpleBean bean = list.get(position);

        if (isOpenCheckbox) {
            viewHolder.ivSelect.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivSelect.setVisibility(View.GONE);
        }
        viewHolder.slideDelete.setTag(bean);
        viewHolder.ivSelect.setTag(bean);
        viewHolder.ivSelect.setOnClickListener(onClickListener);
        viewHolder.slideDelete.setOnClickListener(onClickListener);

        PicUtils.load(context, viewHolder.ivPortrait, bean.getUserLogoUrl());
        viewHolder.tvName.setText(bean.getLoginName());

        if (position == getCount() - 1) {
            viewHolder.bottomLine.setVisibility(View.GONE);
        } else {
            viewHolder.bottomLine.setVisibility(View.VISIBLE);
        }

    }

    static class ViewHolder {
        ImageView ivSelect;
        CircleImageView ivPortrait;
        TextView tvName, slideDelete;
        DragLinearLayout dragLinearLayout;
        View bottomLine;

        ViewHolder(View view) {
            ivSelect = (ImageView) view.findViewById(R.id.ivSelect);
            ivPortrait = (CircleImageView) view.findViewById(R.id.ivPortrait);
            tvName = (TextView) view.findViewById(R.id.tvName);
            slideDelete = (TextView) view.findViewById(R.id.slideDelete);
            dragLinearLayout = (DragLinearLayout) view.findViewById(R.id.dragLinearLayout);
            bottomLine = view.findViewById(R.id.bottomLine);
        }
    }
}
