package com.android.banana.groupchat.groupchat.widget;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.groupchat.bean.ChatRoomMemberBean.GroupMemberSimpleBean;
import com.android.banana.groupchat.membermanage.GroupChatMembersManageActivity;
import com.android.banana.groupchat.view.baselist.recylerview.BaseRecylerView;
import com.android.banana.groupchat.view.baselist.recylerview.RecyclerViewHolder;
import com.android.banana.commlib.utils.picasso.PicUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kokuma on 2017/11/2.
 */

public class MembersView extends BaseRecylerView<GroupMemberSimpleBean> {
    public String groupId = "";
    public boolean isManager;
    public boolean isOwner;
    public MembersView(Context context) {
        super(context);
    }

    public MembersView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int setMyItemId() {
        return R.layout.item_user;
    }

    @Override
    public int setItemWidth() {
        return 0;
    }

    @Override
    public void update(List<GroupMemberSimpleBean> mList) {
        if (mList != null && mList.size() > 6) {
            List<GroupMemberSimpleBean> mList2 = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                mList2.add(mList.get(i));
            }
            super.update(mList2);
            return;
        }
        super.update(mList);
    }

    @Override
    public void bindMyViewHolder(RecyclerViewHolder holder, int position) {
        CircleImageView ivPortrait = holder.getView(R.id.ivPortrait);
        TextView tvName = holder.getView(R.id.tvName);
        GroupMemberSimpleBean bean = dataArray.get(position);
        if (bean != null) {
            PicUtils.load(getContext(), ivPortrait, bean.getUserLogoUrl(), R.drawable.user_default_logo);
            tvName.setText(TextUtils.isEmpty(bean.getNickName()) ? bean.getLoginName() : bean.getNickName());
        }
//        View root = holder.getView(R.id.root);
//        root.setOnClickListener(null);
    }

    @Override
    public void onClick(View view) {
        GroupChatMembersManageActivity.startGroupChatMembersManageActivity((Activity) getContext(), groupId, isManager,isOwner);
    }
}
