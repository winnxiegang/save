package com.android.xjq.adapter.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.controller.maincontroller.InjuryController;
import com.android.xjq.model.injury.BasketballPlayer;
import com.android.xjq.model.injury.FootballPlayer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DaChao on 2017/11/30.
 */

public class InjuryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    //记录处于展开状态的position的集合
    private List<Integer> expandList;
    private List<FootballPlayer> footballPlayerList;
    private List<BasketballPlayer> basketballPlayerList;

    public InjuryAdapter(Context context, List<FootballPlayer> footballPlayerList,
                         List<BasketballPlayer> basketballPlayerList) {
        this.context = context;
        this.footballPlayerList = footballPlayerList;
        this.basketballPlayerList = basketballPlayerList;
        expandList = new ArrayList<>();
    }

    //当前显示的分类，足球和篮球
    private int curCategory = InjuryController.CATEGORY_FOOTBALL;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_injury, parent, false);
        return new InjuryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InjuryViewHolder viewHolder = (InjuryViewHolder) holder;
        switch (curCategory) {
            case InjuryController.CATEGORY_FOOTBALL:
                FootballPlayer footballPlayer = footballPlayerList.get(position);
                viewHolder.tvMatchName.setText(footballPlayer.getMatchName() + " " + footballPlayer.getTime());
                String teamName;
                if (footballPlayer.getIsHost() == 1) {
                    teamName = footballPlayer.getTeamName() + "(主)";
                } else {
                    teamName = footballPlayer.getTeamName() + "(客)";
                }
                viewHolder.tvTeamName.setText(teamName);
                viewHolder.tvPlayerName.setText(footballPlayer.getPlayerName());
                viewHolder.tvPosition.setText(footballPlayer.getPositionInfo());
                viewHolder.tvMatchIndex.setText(footballPlayer.getMatchIndex() + "");
                viewHolder.tvScore.setText(footballPlayer.getScore() + "");
                viewHolder.tvStatus.setText(footballPlayer.getStatus());
                viewHolder.tvDescription.setText(footballPlayer.getStatusInfo());

                //控制是否显示比赛名称
                if (position == 0) {
                    viewHolder.vDecoration.setVisibility(View.VISIBLE);
                    viewHolder.tvMatchName.setVisibility(View.VISIBLE);
                } else if (footballPlayerList.get(position - 1).getIsHost() == 0 && footballPlayer.getIsHost() == 1) {
                    viewHolder.vDecoration.setVisibility(View.VISIBLE);
                    viewHolder.tvMatchName.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.vDecoration.setVisibility(View.GONE);
                    viewHolder.tvMatchName.setVisibility(View.GONE);
                }

                //控制是否显示球队名称
                if (position > 0 && footballPlayerList.get(position - 1).getTeamId() == footballPlayer.getTeamId()) {
                    viewHolder.ivTeamIcon.setVisibility(View.GONE);
                    viewHolder.tvTeamName.setVisibility(View.GONE);
                } else {
                    viewHolder.ivTeamIcon.setVisibility(View.VISIBLE);
                    viewHolder.tvTeamName.setVisibility(View.VISIBLE);
                    //设置球队图标
                    String imageUrl = footballPlayerList.get(position).getIsHost() == 1 ? footballPlayer.getHomeLogoUrl() :
                            footballPlayer.getGuestLogoUrl();
                    LogUtils.e("InjuryAdapter", "imageUrl=" + imageUrl);
                    Picasso.with(context)
                            .load(imageUrl)
                            .into(viewHolder.ivTeamIcon);
                }

                //如果该球队没有人受伤，不显示队员信息
                if (footballPlayer.getPlayerName() == null) {
                    viewHolder.tvEmpty.setVisibility(View.VISIBLE);
                    viewHolder.vDivider2.setVisibility(View.VISIBLE);
                    viewHolder.llContent.setVisibility(View.GONE);
                } else {
                    viewHolder.tvEmpty.setVisibility(View.GONE);
                    viewHolder.vDivider2.setVisibility(View.GONE);
                    viewHolder.llContent.setVisibility(View.VISIBLE);
                }

                break;

            case InjuryController.CATEGORY_BASKETBALL:
                viewHolder.vDecoration.setVisibility(View.GONE);
                viewHolder.tvMatchName.setVisibility(View.GONE);
                viewHolder.tvEmpty.setVisibility(View.GONE);
                viewHolder.vDivider2.setVisibility(View.GONE);
                viewHolder.llContent.setVisibility(View.VISIBLE);

                BasketballPlayer basketballPlayer = basketballPlayerList.get(position);
                viewHolder.tvTeamName.setText(basketballPlayer.getTeamName());
                viewHolder.tvPlayerName.setText(basketballPlayer.getPlayerName());
                viewHolder.tvPosition.setText(basketballPlayer.getPosition());
                viewHolder.tvMatchIndex.setText(basketballPlayer.getMatchIndex() + "");
                viewHolder.tvScore.setText(basketballPlayer.getScore() + "");
                viewHolder.tvStatus.setText(basketballPlayer.getStatus());
                viewHolder.tvDescription.setText(basketballPlayer.getStatusInfo());

                //控制是否显示球队名称
                if (position > 0 && basketballPlayerList.get(position - 1).getTeamId() == basketballPlayer.getTeamId()) {
                    viewHolder.ivTeamIcon.setVisibility(View.GONE);
                    viewHolder.tvTeamName.setVisibility(View.GONE);
                } else {
                    viewHolder.ivTeamIcon.setVisibility(View.VISIBLE);
                    viewHolder.tvTeamName.setVisibility(View.VISIBLE);

                    //设置球队图标
                    Picasso.with(context)
                            .load(basketballPlayer.getTeamLogoUrl())
                            .into(viewHolder.ivTeamIcon);
                }

                break;
        }
        //设置是否展开更多信息
        viewHolder.setExpand(expandList.contains(position));
    }

    @Override
    public int getItemCount() {
        switch (curCategory) {
            case InjuryController.CATEGORY_FOOTBALL:
                return footballPlayerList.size();
            case InjuryController.CATEGORY_BASKETBALL:
                return basketballPlayerList.size();
        }
        return 0;
    }


    /**
     * 设置分组
     */
    public void setCategory(int category) {
        curCategory = category;
        expandList.clear();
        notifyDataSetChanged();
    }

    public List<FootballPlayer> getFootballPlayerList() {
        return footballPlayerList;
    }

    public List<BasketballPlayer> getBasketballPlayerList() {
        return basketballPlayerList;
    }

    private class InjuryViewHolder extends RecyclerView.ViewHolder {

        View vDecoration;
        TextView tvMatchName;
        ImageView ivTeamIcon;
        TextView tvTeamName;
        TextView tvEmpty;
        View vDivider2;
        LinearLayout llContent;
        TextView tvPlayerName;
        TextView tvPosition;
        TextView tvMatchIndex;
        TextView tvScore;
        TextView tvStatus;
        ImageView ivExpand;
        LinearLayout llDescription;
        TextView tvDescription;

        boolean isExpand = false;

        InjuryViewHolder(View itemView) {
            super(itemView);
            vDecoration = itemView.findViewById(R.id.v_decoration);
            tvMatchName = (TextView) itemView.findViewById(R.id.tv_match_name);
            ivTeamIcon = (ImageView) itemView.findViewById(R.id.iv_team_icon);
            tvTeamName = (TextView) itemView.findViewById(R.id.tv_team_name);
            tvEmpty = (TextView) itemView.findViewById(R.id.tv_empty);
            vDivider2 = itemView.findViewById(R.id.v_divider2);
            llContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
            tvPlayerName = (TextView) itemView.findViewById(R.id.tv_player_name);
            tvPosition = (TextView) itemView.findViewById(R.id.tv_position);
            tvMatchIndex = (TextView) itemView.findViewById(R.id.tv_match_index);
            tvScore = (TextView) itemView.findViewById(R.id.tv_score);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            ivExpand = (ImageView) itemView.findViewById(R.id.iv_expand);
            llDescription = (LinearLayout) itemView.findViewById(R.id.ll_description);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);

            ivExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpand) {
                        setExpand(false);
                    } else {
                        setExpand(true);
                    }
                }
            });

        }

        void setExpand(boolean expand) {
            if (expand) {
                //展开
                if (!expandList.contains(getAdapterPosition())) {
                    expandList.add(getAdapterPosition());
                }
                if (!isExpand) {
                    isExpand = true;
                    ivExpand.setRotationX(180);
                    llDescription.setVisibility(View.VISIBLE);
                }
            } else {
                //折叠
                int index = expandList.indexOf(getAdapterPosition());
                if (index != -1) {
                    expandList.remove(index);
                }
                if (isExpand) {
                    isExpand = false;
                    ivExpand.setRotationX(0);
                    llDescription.setVisibility(View.GONE);
                }
            }
        }
    }

}
