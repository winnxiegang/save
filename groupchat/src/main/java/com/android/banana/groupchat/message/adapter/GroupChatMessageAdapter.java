package com.android.banana.groupchat.message.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.groupchat.bean.MessageListBean;
import com.android.banana.groupchat.ilistener.OnMyItemSlideClickListener;
import com.android.banana.commlib.escapeUtils.StringEscapeUtils;
import com.android.banana.view.BadgeView;
import com.android.banana.view.DragLinearLayout;
import com.android.library.Utils.LogUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zaozao on 2017/6/2.
 */

public class GroupChatMessageAdapter extends MyBaseAdapter<MessageListBean> {


    private OnMyItemSlideClickListener onMyItemSlideClickListener;

    public GroupChatMessageAdapter(Context context, List<MessageListBean> list) {
        super(context, list);
    }

    public void setOnMyItemSlideClickListener(OnMyItemSlideClickListener onMyItemSlideClickListener) {
        this.onMyItemSlideClickListener = onMyItemSlideClickListener;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.item_group_chat_message, parent, false);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        setItemView(viewHolder, position);

        return convertView;
    }


    private void setItemView(final ViewHolder viewHolder, final int position) {

        final MessageListBean bean = list.get(position);

        if(bean.isSystemMessage()){
//            viewHolder.dragLinearLayout.setDragEnable(false);
            viewHolder.chatRoomPhotoIv.setImageResource(R.drawable.icon_chat_room_system_notify);
        }else{
            Picasso.with(context)
                    .load(bean.getPhotoUrl())
                    .error(R.drawable.user_default_logo)
                    .placeholder(R.drawable.user_default_logo)
                    .into(viewHolder.chatRoomPhotoIv);
        }


        if(bean.getMemberNum()!=null){

            SpannableStringBuilder ssb = new SpannableStringBuilder();

            SpannableString ss = new SpannableString(bean.getRoomName()+bean.getMemberNum());

            ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.gray)), bean.getRoomName().length(), ss.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            ss.setSpan(new AbsoluteSizeSpan(12,true), bean.getRoomName().length(), ss.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            ssb.append(ss);
            viewHolder.chatRoomName.setText(ss);
        }else{
            viewHolder.chatRoomName.setText(bean.getRoomName());
        }

        viewHolder.newMessageTv.setText(StringEscapeUtils.unescapeHtml(bean.getLastMessageContent()));

        viewHolder.timeTv.setText(bean.getGmtCreate());

        viewHolder.badgeView.setBadgeNum(bean.getUnreadMessageCount());

//      viewHolder.noticeBadgeView.setBadgeNum(bean.getUnreadMessageCount());
        LogUtils.e("kk",""+bean.getOrderValueByUser());
        if (list.get(position).isSystemMessage()) {
            viewHolder.top.setVisibility(View.GONE);
        } else {
            viewHolder.top.setVisibility(View.VISIBLE);
            if ("0".equals(bean.getOrderValueByUser() + "")||bean.isSystemMessage()) {
                viewHolder.top.setText("置顶");
                viewHolder.dragLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            } else {
                viewHolder.top.setText("取消置顶");
                viewHolder.dragLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.main_background));
            }
        }

        if (bean.isOpen()) {
            viewHolder.dragLinearLayout.openImmi();
        } else {
            viewHolder.dragLinearLayout.closeImmi();
        }

        viewHolder.timeTv.setCompoundDrawablesWithIntrinsicBounds(bean.isShowCoupon() ? R.drawable.icon_qiubao_coupon : 0, 0, 0, 0);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.top) {
                    onMyItemSlideClickListener.onItemClick(position, false, false, true);
                } else if (v.getId() == R.id.deleteTv) {
                    onMyItemSlideClickListener.onItemClick(position, false, true, false);
                } else if (v.getId() == R.id.contentLayout) {
                    onMyItemSlideClickListener.onItemClick(position, true, false, false);
                }
            }
        };

        viewHolder.top.setOnClickListener(onClickListener);
        viewHolder.deleteTv.setOnClickListener(onClickListener);
        viewHolder.contentLayout.setOnClickListener(onClickListener);

//        viewHolder.dragLinearLayout.setOnViewDragListener(new DragLinearLayout.ViewDragListener() {
//            @Override
//            public void onOpen() {
//                if(position< list.size()){
//                    list.get(position).setOpen(true);
//                }
//
//            }
//
//            @Override
//            public void onClose() {
//                if(position< list.size()){
//                    list.get(position).setOpen(false);
//                }
//
//            }
//
//            @Override
//            public void onDrag(float percent) {
//
//            }
//        });
    }

    class ViewHolder {
        LinearLayout contentLayout;
        CircleImageView chatRoomPhotoIv;
        TextView chatRoomName;
        TextView timeTv;
        TextView newMessageTv;
        BadgeView badgeView;
        TextView top;
        TextView deleteTv;
        DragLinearLayout dragLinearLayout;
        LinearLayout messageLayout;

        ViewHolder(View view) {
            contentLayout = (LinearLayout) view.findViewById(R.id.contentLayout);
            chatRoomPhotoIv = (CircleImageView) view.findViewById(R.id.chatRoomPhotoIv);
            chatRoomName = (TextView) view.findViewById(R.id.chatRoomName);
            timeTv = (TextView) view.findViewById(R.id.timeTv);
            newMessageTv = (TextView) view.findViewById(R.id.newMessageTv);
            badgeView = (BadgeView) view.findViewById(R.id.badgeView);
            top = (TextView) view.findViewById(R.id.top);
            deleteTv = (TextView) view.findViewById(R.id.deleteTv);
            dragLinearLayout = (DragLinearLayout) view.findViewById(R.id.dragLinearLayout);
            messageLayout = (LinearLayout) view.findViewById(R.id.messageLayout);
        }
    }
}
