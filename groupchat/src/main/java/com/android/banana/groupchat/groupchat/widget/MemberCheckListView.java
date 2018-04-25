package com.android.banana.groupchat.groupchat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.groupchat.bean.GroupUserApplyJoinSimpleBean;
import com.android.banana.groupchat.view.baselist.BaseListView;
import com.android.banana.groupchat.view.baselist.base.BaseViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kokuma on 2017/10/27.
 * 参照GroupChatRoomMemberAdapter
 */

public class  MemberCheckListView extends BaseListView<GroupUserApplyJoinSimpleBean> {

    public OnClickListener onClickListener;

    public MemberCheckListView(Context context) {
        super(context);
        initUI(R.layout.item_member_check);
    }

    public MemberCheckListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(R.layout.item_member_check);
    }

    public MemberCheckListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI(R.layout.item_member_check);
    }

    @Override
    protected void setConvert(BaseViewHolder viewHolder, GroupUserApplyJoinSimpleBean bean) {

        CircleImageView portraitIv = viewHolder.getView(R.id.portraitIv);
        TextView usernameTv = viewHolder.getView(R.id.usernameTv);

        TextView tvTime  = viewHolder.getView(R.id.tvTime);
        TextView tvAgree = viewHolder.getView(R.id.tvAgree);
        TextView tvRefuse = viewHolder.getView(R.id.tvRefuse);
        TextView tvCompleteInfo = viewHolder.getView(R.id.tvCompleteInfo);

        if (bean==null) {
            return;
        }
        PicUtils.load(getContext(),portraitIv,bean.getUserLogoUrl());
        usernameTv.setText(bean.getUserName());
       tvTime.setText(TimeUtils.formatTime(TimeUtils.getCurrentTime(),bean.getGmtCreate()));

        if(bean.getAduitStatus()!=null&&bean.getAduitStatus().getName()!=null){
            String status = bean.getAduitStatus().getName();
            if(status.equals("WAIT_ADUIT")){
                tvAgree.setVisibility(VISIBLE);
                tvRefuse.setVisibility(VISIBLE);
                tvCompleteInfo.setVisibility(GONE);
                tvAgree.setTag(bean);
                tvRefuse.setTag(bean);
                tvAgree.setOnClickListener(onClickListener);
                tvRefuse.setOnClickListener(onClickListener);
            }else if(status.equals("AGREEED")||status.equals("REFUSED")){
                tvAgree.setVisibility(GONE);
                tvRefuse.setVisibility(GONE);
                tvCompleteInfo.setVisibility(VISIBLE);
                String  info = status.equals("AGREEED")?"已同意":"已拒绝";
                tvCompleteInfo.setText(info);
            }
        }
    }
}
