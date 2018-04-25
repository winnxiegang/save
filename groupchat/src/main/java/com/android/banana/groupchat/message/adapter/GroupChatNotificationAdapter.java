package com.android.banana.groupchat.message.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.groupchat.bean.GroupChatNotificationBean;
import com.android.banana.groupchat.message.GroupChatNotificationActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zaozao on 2017/6/1.
 */

public class GroupChatNotificationAdapter extends MyBaseAdapter<GroupChatNotificationBean.NoticesBean> {

    public GroupChatNotificationAdapter(Context context, List<GroupChatNotificationBean.NoticesBean> list,
                                        GroupChatNotificationActivity.NoticeClickListener noticeClickListener) {
        super(context, list);
        this.noticeClickListener = noticeClickListener;
    }
    private GroupChatNotificationActivity.NoticeClickListener noticeClickListener;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (null == convertView) {

            convertView = layoutInflater.inflate(R.layout.item_group_chat_notification, parent, false);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        setItemView(viewHolder, position);

        return convertView;
    }

    private void setItemView(ViewHolder viewHolder, final int position) {
        final GroupChatNotificationBean.NoticesBean bean = list.get(position);

        viewHolder.timeTv.setText(bean.getGmtCreate());

        viewHolder.newNotificationTv.setText(bean.getNoticeContent());

        String logo = null;
        if(bean.getMessageParamBean()!=null){
            if (bean.isUser()) {
                viewHolder.userCountTv.setVisibility(View.GONE);
                viewHolder.chatRoomName.setText(bean.getSenderName());
                logo = bean.getMessageParamBean().getUSER_LOGO();
            } else {
                viewHolder.userCountTv.setVisibility(View.VISIBLE);
                viewHolder.userCountTv.setText("("+bean.getMessageParamBean().getGROUP_USER_COUNT()+"人)");
                viewHolder.chatRoomName.setText(bean.getMessageParamBean().getGROUP_CHAT_NAME());
                logo = bean.getMessageParamBean().getGROUP_LOGO_URL();

                viewHolder.noticeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(bean.isClickToGroupChat()){
                            noticeClickListener.onclick(position,true,false);
                        }else{
                            return;
                        }
                    }
                });
            }
        }

        Picasso.with(context.getApplicationContext())
                .load(logo)
                .into(viewHolder.chatRoomPhotoIv);

        viewHolder.tvAgree.setVisibility(View.GONE);
        viewHolder.tvRefuse.setVisibility(View.GONE);
        viewHolder.resultTv.setVisibility(View.GONE);

        if (bean.isShowButton()) {
            viewHolder.tvAgree.setVisibility(View.VISIBLE);
            viewHolder.tvRefuse.setVisibility(View.VISIBLE);
            viewHolder.tvAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noticeClickListener.onclick(position,false,true);
                }
            });
            viewHolder.tvRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noticeClickListener.onclick(position,false,false);
                }
            });
        }
        if (bean.isShowResult()) {
            viewHolder.resultTv.setVisibility(View.VISIBLE);
            viewHolder.resultTv.setText(bean.getApplyResult());
        }


    }


    static class ViewHolder {
        CircleImageView chatRoomPhotoIv;
        TextView chatRoomName;
        TextView timeTv,tvAgree,tvRefuse,userCountTv,resultTv;
        TextView newNotificationTv;
        LinearLayout noticeLayout;

        ViewHolder(View view) {
            chatRoomPhotoIv = (CircleImageView) view.findViewById(R.id.chatRoomPhotoIv);
            chatRoomName = (TextView) view.findViewById(R.id.chatRoomName);
            timeTv = (TextView) view.findViewById(R.id.timeTv);
            newNotificationTv = (TextView) view.findViewById(R.id.newNotificationTv);
            userCountTv = (TextView) view.findViewById(R.id.userCountTv);
            tvAgree = (TextView) view.findViewById(R.id.tvAgree);
            tvRefuse = (TextView) view.findViewById(R.id.tvRefuse);
            resultTv = (TextView) view.findViewById(R.id.applyResultTv);
            noticeLayout = (LinearLayout) view.findViewById(R.id.noticeLayout);
        }
    }

}
