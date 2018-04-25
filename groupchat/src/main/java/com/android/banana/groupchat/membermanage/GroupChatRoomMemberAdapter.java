package com.android.banana.groupchat.membermanage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.groupchat.bean.ChatRoomMemberBean;
import com.android.banana.groupchat.chatenum.ChatRoomMemberLevelEnum;
import com.android.banana.groupchat.ilistener.onSelectClickListener;
import com.android.banana.view.DragLinearLayout;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.banana.groupchat.membermanage.GroupChatMembersManageActivity.ADD_MEMBER_STATE;
import static com.android.banana.groupchat.membermanage.GroupChatMembersManageActivity.DELETE_MEMBER_STATE;

/**
 * Created by zaozao on 2017/6/1.
 */

public class GroupChatRoomMemberAdapter extends MyBaseAdapter<ChatRoomMemberBean.GroupMemberSimpleBean> {

    private onSelectClickListener onSelectListener;

    private int operateType = ADD_MEMBER_STATE;

    private boolean deleteEnable = false;

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public void setDelete(boolean deleteEnable) {
        this.deleteEnable = deleteEnable;
    }

    public GroupChatRoomMemberAdapter(Context context, List<ChatRoomMemberBean.GroupMemberSimpleBean> list) {
        super(context, list);
    }

    public void setOnSelectListener(onSelectClickListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.item_chat_room_member, parent,false);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        setItemView(viewHolder, position);

        return convertView;
    }

    private void setItemView(ViewHolder viewHolder, final int position) {
        viewHolder.chatRoomMemberLL.setDragEnable(deleteEnable);

        final ChatRoomMemberBean.GroupMemberSimpleBean bean = list.get(position);

        viewHolder.usernameTv.setText(bean.getNickName()!=null?bean.getNickName():bean.getUserName());

        viewHolder.selectIv.setVisibility(View.GONE);

        if (operateType == ADD_MEMBER_STATE) {

            viewHolder.selectIv.setVisibility(View.VISIBLE);
            if (bean.isHaveAddComplete()) {
                viewHolder.selectIv.setImageResource(R.drawable.icon_have_add_complete);
            } else {
                if (bean.isSelected()) {
                    viewHolder.selectIv.setImageResource(R.drawable.icon_contact_selected);
                } else {
                    viewHolder.selectIv.setImageResource(R.drawable.icon_contact_normal);
                }
            }

        } else if (operateType == DELETE_MEMBER_STATE) {

            viewHolder.selectIv.setVisibility(View.VISIBLE);

            viewHolder.selectIv.setImageResource(R.drawable.icon_red_delete);
        } else {

        }

        setIconView(viewHolder.portraitIv, bean.getUserLogoUrl());


        if (bean.isVip()) {
            viewHolder.vipIv.setVisibility(View.VISIBLE);
        } else {
            viewHolder.vipIv.setVisibility(View.GONE);
        }

        if(bean.showCutLine){
            viewHolder.cutLine.setVisibility(View.VISIBLE);
        }else{
            viewHolder.cutLine.setVisibility(View.GONE);
        }
        viewHolder.selectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operateType == DELETE_MEMBER_STATE) {
                    onSelectListener.onSelectClick(v, position, true);//点击减号删除
                } else {
                    onSelectListener.onSelectClick(v, position, false);//点击加号选中
                }
            }
        });

        viewHolder.slideDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectListener.onSelectClick(v, position, true);//侧滑删除
            }
        });

        viewHolder.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectListener.onSelectClick(v, position, false);
            }
        });


        if (bean.isShowIdentifyTitle()) {
            viewHolder.identifyTitleTv.setVisibility(View.VISIBLE);
            if(position==0){
                viewHolder.identifyTitleTv.setText("群主/管理员");
                viewHolder.chatRoomMemberLL.setDragEnable(false);
                viewHolder.selectIv.setVisibility(View.GONE);
            }else{
                viewHolder.identifyTitleTv.setText("其他成员");
            }
        } else {
            viewHolder.identifyTitleTv.setVisibility(View.GONE);
        }

        if(bean.isShowIdentify()){
            viewHolder.identifyIv.setVisibility(View.VISIBLE);
            switch (ChatRoomMemberLevelEnum.saveValueOf(bean.getLevelCode())){
                case GROUP_OWNER:
                    viewHolder.identifyIv.setImageResource(R.drawable.icon_group_owner);
                    break;
                case GROUP_ADMIN:
                    viewHolder.identifyIv.setImageResource(R.drawable.icon_group_admin);
                    break;
                default:
                    viewHolder.identifyIv.setVisibility(View.GONE);
                    break;
            }
        }else{
            viewHolder.identifyIv.setVisibility(View.GONE);
        }

        viewHolder.chatRoomMemberLL.setOnViewDragListener(new DragLinearLayout.ViewDragListener() {
            @Override
            public void onOpen() {
                list.get(position).setOpen(true);
            }

            @Override
            public void onClose() {
                list.get(position).setOpen(false);
            }

            @Override
            public void onDrag(float percent) {

            }
        });

        if (bean.isOpen()) {
            viewHolder.chatRoomMemberLL.openImmi();
        } else {
            viewHolder.chatRoomMemberLL.close();
        }
    }

    static class ViewHolder {
        ImageView selectIv;
        CircleImageView portraitIv;
        ImageView vipIv;
        TextView usernameTv;
        LinearLayout contentLayout;
        TextView slideDelete;
        ImageView identifyIv;
        TextView identifyTitleTv;
        DragLinearLayout chatRoomMemberLL;
        View cutLine;

        ViewHolder(View view) {
            selectIv = (ImageView) view.findViewById(R.id.selectIv);
            portraitIv = (CircleImageView) view.findViewById(R.id.portraitIv);
            vipIv = (ImageView) view.findViewById(R.id.vipIv);
            usernameTv = ( TextView) view.findViewById(R.id.usernameTv);
            contentLayout = (LinearLayout) view.findViewById(R.id.contentLayout);
            slideDelete = (TextView) view.findViewById(R.id.slideDelete);
            chatRoomMemberLL = (DragLinearLayout) view.findViewById(R.id.chatRoomMemberLL);
            identifyIv = (ImageView) view.findViewById(R.id.identifyIv);
            cutLine = view.findViewById(R.id.cutLine);
            identifyTitleTv = (TextView) view.findViewById(R.id.identifyTitleTv);
        }
    }
}
