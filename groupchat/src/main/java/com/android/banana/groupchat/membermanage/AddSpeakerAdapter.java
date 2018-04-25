package com.android.banana.groupchat.membermanage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.groupchat.bean.ChatRoomMemberBean;
import com.squareup.picasso.Picasso;
import com.android.banana.R;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zaozao on 2017/5/31.
 */

public class AddSpeakerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private List<ChatRoomMemberBean.GroupMemberSimpleBean> list;

    private OnItemClickListener onItemClickListener;

    private int type = SpeakSettingActivity.ADD_STATE;

    public void setType(int type) {
        this.type = type;
    }

    public AddSpeakerAdapter(Context context, List<ChatRoomMemberBean.GroupMemberSimpleBean> list) {
        this.context = context;

        this.list = list;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onDeleteClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_add_speaker, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, (int) v.getTag());
                }
            }
        });

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        ChatRoomMemberBean.GroupMemberSimpleBean speaker = list.get(position);

        if (list.get(position).getLoginName() == null) {
            ((MyViewHolder) holder).userNameTv.setText(list.get(position).getUserName());
        } else {
            ((MyViewHolder) holder).userNameTv.setText(list.get(position).getLoginName());
        }
        if (speaker.getUserLogoUrl() != null) {
            Picasso.with(context.getApplicationContext())
                    .load(speaker.getUserLogoUrl())
                    .into(((MyViewHolder) holder).photoIv);
        } else {
            ((MyViewHolder) holder).photoIv.setImageResource(list.get(position).getResId());
        }

        if (speaker.isVip()) {
            ((MyViewHolder) holder).vipIv.setVisibility(View.VISIBLE);
        } else {
            ((MyViewHolder) holder).vipIv.setVisibility(View.GONE);
        }
        //可添加状态
        if (type == SpeakSettingActivity.ADD_STATE) {
            ((MyViewHolder) holder).deleteIv.setVisibility(View.GONE);
        } else if (list.get(position).isGoneDelete()) {
            ((MyViewHolder) holder).deleteIv.setVisibility(View.GONE);
        } else {
            ((MyViewHolder) holder).deleteIv.setVisibility(View.VISIBLE);
        }

        ((MyViewHolder) holder).deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onDeleteClick(v, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView photoIv;
        ImageView vipIv;
        TextView userNameTv;
        ImageView deleteIv;

        public MyViewHolder(View itemView) {
            super(itemView);
            photoIv = (CircleImageView) itemView.findViewById(R.id.photoIv);
            vipIv = (ImageView) itemView.findViewById(R.id.vipIv);
            userNameTv = (TextView) itemView.findViewById(R.id.userNameTv);
            deleteIv = (ImageView) itemView.findViewById(R.id.deleteIv);
        }
    }
}
