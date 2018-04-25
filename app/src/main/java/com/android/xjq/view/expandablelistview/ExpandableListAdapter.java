package com.android.xjq.view.expandablelistview;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.game.ShowPopListSelectorView;
import com.android.banana.commlib.loadmore.CommonLoadMoreView;
import com.android.banana.commlib.loadmore.OnRetryClickListener;
import com.android.banana.commlib.view.PercentProgressView;
import com.android.xjq.R;
import com.android.xjq.bean.GameParentBean;
import com.android.xjq.bean.MyGameChildBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "ExpandableListAdapter";
    private List<GameParentBean> mGroupData = null;
    private int mPos = 0;
    private List<MyGameChildBean> mChildItemData = new ArrayList<>();
    private IOnCollapseChildItem mCollapseListener;
    private IOnGroupSendGiftClick mGroupSendGiftListener;
    private IOnChildSendGiftClick mChildSendGiftListener;
    private IOnExpandChildItem mExpandListener;
    private Context mCtx;
    private Boolean isRequestSuccess;
    private int expandPosition = -1;
    private List<Integer> mSelectorData;
    private String mMatchType;
    private Animation btnAnim;

    public ExpandableListAdapter(Context context) {
        mCtx = context;
        btnAnim = AnimationUtils.loadAnimation(context, com.android.banana.R.anim.rocket_button_click);
    }

    public void setGroupData(List<GameParentBean> groupData) {
        this.mGroupData = groupData;
    }
    public void setType(String matchType) {
        mMatchType = matchType;
    }

    public void setChildItemData(List<MyGameChildBean> childData, int pos) {
        mChildItemData = childData;
        mPos = pos;
    }


    @Override
    public int getGroupCount() {
        return mGroupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildItemData.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildItemData;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View
            convertView, ViewGroup parent) {
        Log.e(TAG, "getGroupView: ");
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cheer_betting_game_group, parent, false);
        }
        GroupViewHolder holder = GroupViewHolder.getTag(convertView);
        setGroupData(holder, groupPosition);
        return convertView;
    }


    public static class GroupViewHolder {
        @BindView(R.id.left_per_money)
        TextView homePerPrice;
        @BindView(R.id.right_per_money)
        TextView guestPerPrice;
        @BindView(R.id.left_gift_lay)
        FrameLayout leftGiftLay;
        @BindView(R.id.right_gift_lay)
        FrameLayout rightGiftLay;
        @BindView(R.id.home_lay)
        RelativeLayout homeLay;
        @BindView(R.id.guest_lay)
        RelativeLayout guestLay;
        @BindView(R.id.match_name_and_time)
        TextView matchNameAndTime;
        @BindView(R.id.home_team_name)
        TextView hostTeamNameTxt;
        @BindView(R.id.guest_team_name)
        TextView guestTeamNameTxt;
        @BindView(R.id.home_icon)
        CircleImageView homeIcon;
        @BindView(R.id.guest_icon)
        CircleImageView guestIcon;
        @BindView(R.id.differ_score_changci)
        TextView scoreAndChangci;
        @BindView(R.id.home_gold_coin)
        TextView hostCoinTxt;
        @BindView(R.id.guest_gold_coin)
        TextView guestCoinTxt;
        @BindView(R.id.expandLayout)
        LinearLayout expandLayout;
        @BindView(R.id.customer_progress_bar)
        PercentProgressView customProgress;
        @BindView(R.id.loadMoreView)
        CommonLoadMoreView loadMoreView;
         @BindView(R.id.guest_show_pop_list_selector_view)
         ShowPopListSelectorView guestShowPopListSelector;
         @BindView(R.id.home_show_pop_list_selector_view)
         ShowPopListSelectorView homeShowPopListSelector;
         @BindView(R.id.content_lay)
         LinearLayout contentLay;
         @BindView(R.id.group_main_lay)
         RelativeLayout mainLay;


        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public static GroupViewHolder getTag(View convertView) {
            GroupViewHolder holder = (GroupViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new GroupViewHolder(convertView);
            }
            return holder;
        }
    }


    public void reSetExpandPos (int pos) {
        expandPosition = pos;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cheer_betting_game_child, parent, false);
        }
        ChildViewHolder holder = ChildViewHolder.getTag(convertView);
        setChildData(holder, childPosition, groupPosition);
        return convertView;
    }

    private void setGroupData(final GroupViewHolder holder, final int groupPosition) {
        if (mGroupData.size() > 0) {
            final GameParentBean bean = mGroupData.get(groupPosition);
            double hostScore = bean.getHostHotScore();
            double guestScore = bean.getGuestHotScore();
            //int resBgId = "BASKETBALL".equals(bean.getRaceType()) ? R.drawable.item_basket_ball_bg : R.drawable.item_football_ball_bg;
            int resBgId = mMatchType == "FOOTBALL" ? R.drawable.item_football_ball_bg : R.drawable.item_basket_ball_bg;
            holder.mainLay.setBackground(mCtx.getResources().getDrawable(resBgId));
            String hostName = bean.getHostName().length() > 4 ? bean.getHostName().substring(0, 4) + "..." : bean.getHostName();
            holder.guestTeamNameTxt.setText(bean.getGuestName());
            holder.hostTeamNameTxt.setText(bean.getHostName());
            //holder.changciTxt.setText(bean.getChangci());
            String dangWei = "BASKETBALL".equals(bean.getRaceType()) ? "分" : "球";
            String plate = bean.getPlate() > 0 ? "+" + bean.getPlate() : String.valueOf(bean.getPlate());
            String color = bean.getPlate() > 0 ? "#ff0000" : "#009900";
            String score = "[" + "<font color='" + color + "'>" + plate + "</font>" + "<font color='#505050'>" + dangWei + "]";
            String scoreAndChangciStr = score + " " + bean.getChangci();
            holder.scoreAndChangci.setText(Html.fromHtml(scoreAndChangciStr));
            if (bean.isGameBoardAllPrized()) {
                holder.scoreAndChangci.setText(mCtx.getResources().getString(R.string.match_finish_info));
            } else if (!bean.isExistedDefaultGameBoard()) {
                holder.scoreAndChangci.setText(mCtx.getResources().getString(R.string.all_match_finish_info_wait));
            }
            holder.matchNameAndTime.setText(bean.getMatchName() + " " + bean.getGmtTime());
            int mainBgResId = bean.isHasChild() ? bean.isExpanded() ? R.drawable.shape_white_radius_top_bg : R.drawable.shape_white_radius_bg
                    : R.drawable.shape_white_radius_bg;
            holder.contentLay.setBackground(mCtx.getResources().getDrawable(mainBgResId));
            //如果没有子局就不显示展开，只能展开一条Item, isRequestSuccess字段控制请求成功后才隐藏group的展开，并控制显示网络请求进度条，关闭wifi显示网络请求失败。
            boolean bIsHideExpand = bean.isHasChild() ? expandPosition == groupPosition ? isRequestSuccess != null : false : true;
            holder.expandLayout.setVisibility(!bIsHideExpand ? View.VISIBLE : View.GONE);

            if (isRequestSuccess == null) {
            } else if (!isRequestSuccess) {
                holder.loadMoreView.showRetry();
            } else if (isRequestSuccess && bean.isHasChild()) {
                holder.loadMoreView.showExpandMore();
            }
            boolean b = bean.isShowZhuWei();
            int visibility = b ? View.VISIBLE : View.GONE;
            holder.homeLay.setVisibility(visibility);
            holder.guestLay.setVisibility(visibility);
            holder.homeShowPopListSelector.setVisibility(visibility);
            holder.guestShowPopListSelector.setVisibility(visibility);
            holder.guestShowPopListSelector.setClickable(true);
            holder.guestShowPopListSelector.setData(mSelectorData);
            holder.guestShowPopListSelector.setOnSelectRateListener(new ShowPopListSelectorView.OnSelectRateListener() {
                @Override
                public void onSelectRate(int rate) {
                    bean.setGuestRate(rate);
                }
            });
            //holder.homeShowPopListSelector.setVisibility(visibility);
            holder.homeShowPopListSelector.setClickable(true);
            holder.homeShowPopListSelector.setData(mSelectorData);
            holder.homeShowPopListSelector.setOnSelectRateListener(new ShowPopListSelectorView.OnSelectRateListener() {
                @Override
                public void onSelectRate(int rate) {
                    bean.setHomeRate(rate);
                }
            });

            if (bean.getGiftHomeEntry() != null) {
                Picasso.with(mCtx).load(bean.getGiftHomeEntry().getBetFormImageUrl()).error(R.drawable.user_default_logo).into(holder.homeIcon);
                holder.leftGiftLay.setClickable(true);
                holder.leftGiftLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mGroupSendGiftListener != null) {
                            holder.leftGiftLay.startAnimation(btnAnim);
                            String keyAndOption = bean.getGiftHomeEntry().getBoardId() + "@" + bean.getGiftHomeEntry().getOptionCode();
                            mGroupSendGiftListener.onGroupHostGift(groupPosition, bean.getGiftHomeEntry().getBetFormNo(), bean.getGiftHomeEntry().getBoardId(), keyAndOption, bean.getPlayType());
                        }
                    }
                });

                int perPrice = (int)bean.getGiftHomeEntry().getBetFormSingleFee();
                holder.homePerPrice.setText(String.valueOf(perPrice));
            }
            if (bean.getGiftGuestEntry() != null) {
                Picasso.with(mCtx).load(bean.getGiftGuestEntry().getBetFormImageUrl()).error(R.drawable.user_default_logo).into(holder.guestIcon);
                holder.rightGiftLay.setClickable(true);
                holder.rightGiftLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mGroupSendGiftListener != null) {
                            holder.rightGiftLay.startAnimation(btnAnim);
                            String keyAndOption = bean.getGiftGuestEntry().getBoardId() + "@" + bean.getGiftGuestEntry().getOptionCode();
                            mGroupSendGiftListener.onGroupGuestGift(groupPosition, bean.getGiftGuestEntry().getBetFormNo(), bean.getGiftGuestEntry().getBoardId(), keyAndOption, bean.getPlayType());
                        }
                    }
                });
                int perPrice = (int)bean.getGiftGuestEntry().getBetFormSingleFee();
                holder.guestPerPrice.setText(String.valueOf(perPrice));
            }

            if (bean.getHostPriceFee() == 0) {
                holder.hostCoinTxt.setVisibility(View.INVISIBLE);
            } else {
                holder.hostCoinTxt.setVisibility(View.VISIBLE);
                holder.hostCoinTxt.setText(String.valueOf((int) bean.getHostPriceFee()));
            }

            if (bean.getGuestPriceFee() == 0) {
                holder.guestCoinTxt.setVisibility(View.INVISIBLE);
            } else {
                holder.guestCoinTxt.setVisibility(View.VISIBLE);
                holder.guestCoinTxt.setText(String.valueOf((int) bean.getGuestPriceFee()));
            }

            holder.customProgress.setProgressValue((int) hostScore, (int) guestScore, false);
            holder.expandLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mExpandListener != null) {
                        mExpandListener.onExpandChildItem(groupPosition);
                        holder.loadMoreView.showLoading();
                        isRequestSuccess = null;
                        //holder.loadMoreView.showRetry();
                        expandPosition = groupPosition;
                    }
                }
            });

            holder.loadMoreView.setOnRetryClickListener(new OnRetryClickListener() {
                @Override
                public void OnRetryClick() {
                    if (mExpandListener != null) {
                        mExpandListener.onExpandChildItem(groupPosition);
                        holder.loadMoreView.showLoading();
                        isRequestSuccess = null;
                        //holder.loadMoreView.showRetry();
                        expandPosition = groupPosition;
                    }
                }
            });

        }
    }


    public void setRefreshCompleted(Boolean isSuccess) {
        this.isRequestSuccess = isSuccess;
        notifyDataSetChanged();
    }


    private void setChildData(final ChildViewHolder holder, final int childPosition, final int groupPosition) {
        if (mChildItemData.size() > 0) {
            final MyGameChildBean bean = mChildItemData.get(childPosition);
            //holder.childChangciTxt.setText(bean.getChangci());
            int resBgId = "BASKETBALL".equals(bean.getRaceType()) ? R.drawable.item_basket_ball_bg : R.drawable.item_football_ball_bg;
            holder.mainLay.setBackground(mCtx.getResources().getDrawable(resBgId));
            String plate = bean.getPlate() > 0 ? "+" + bean.getPlate() : String.valueOf(bean.getPlate());
            String color = bean.getPlate() > 0 ? "#ff0000" : "#009900";
            String dangWei = "BASKETBALL".equals(bean.getRaceType()) ? "分" : "球";

            String content = "[" + "<font color='" + color + "'>" + plate + "</font>" + "<font color='#505050'>" + dangWei + "</font>" + "]" + " " + bean.getChangci();
            holder.differScoreAndChangCi.setText(Html.fromHtml(content));
            holder.matchTypeTime.setText(bean.getMatchName() + " " + bean.getGmtStart());
            boolean b = bean.isShowZhuWei();
            int visibility = b ? View.VISIBLE : View.GONE;
            holder.childHomeLay.setVisibility(visibility);
            holder.childGuestLay.setVisibility(visibility);
            holder.homeShowPopSelector.setVisibility(visibility);
            holder.guestShowPopSelector.setVisibility(visibility);
            holder.guestShowPopSelector.setClickable(true);
            holder.guestShowPopSelector.setData(mSelectorData);
            holder.guestShowPopSelector.setOnSelectRateListener(new ShowPopListSelectorView.OnSelectRateListener() {
                @Override
                public void onSelectRate(int rate) {
                    bean.setGuestRate(rate);
                }
            });
            //holder.homeShowPopSelector.setVisibility(visibility);
            holder.homeShowPopSelector.setClickable(true);
            holder.homeShowPopSelector.setData(mSelectorData);
            holder.homeShowPopSelector.setOnSelectRateListener(new ShowPopListSelectorView.OnSelectRateListener() {
                @Override
                public void onSelectRate(int rate) {
                    bean.setHomeRate(rate);
                }
            });

            if (bean.getGiftHomeEntry() != null) {
                Picasso.with(mCtx).load(bean.getGiftHomeEntry().getBetFormImageUrl()).error(R.drawable.user_default_logo).into(holder.childHostGiftImg);
                holder.childHomeLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mChildSendGiftListener != null) {
                            holder.childHomeLay.startAnimation(btnAnim);
                            String keyAndOption = bean.getGiftHomeEntry().getBoardId() + "@" + bean.getGiftHomeEntry().getOptionCode();
                            mChildSendGiftListener.onChildHostGift(childPosition, bean.getGiftHomeEntry().getBetFormNo(), bean.getGiftHomeEntry().getBoardId(), keyAndOption, bean.getPlayType());
                        }
                    }
                });
                int perPrice = (int)bean.getGiftHomeEntry().getBetFormSingleFee();
                holder.homePerPrice.setText(String.valueOf(perPrice));
            }
            if (bean.getGiftGuestEntry() != null) {
                Picasso.with(mCtx).load(bean.getGiftGuestEntry().getBetFormImageUrl()).error(R.drawable.user_default_logo).into(holder.childGuestGiftImg);
                holder.childGuestLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mChildSendGiftListener != null) {
                            holder.childGuestLay.startAnimation(btnAnim);
                            String keyAndOption = bean.getGiftGuestEntry().getBoardId() + "@" + bean.getGiftGuestEntry().getOptionCode();
                            mChildSendGiftListener.onChildGuestGift(childPosition, bean.getGiftGuestEntry().getBetFormNo(), bean.getGiftGuestEntry().getBoardId(), keyAndOption, bean.getPlayType());
                        }
                    }
                });
                int perPrice = (int)bean.getGiftGuestEntry().getBetFormSingleFee();
                holder.guestPerPrice.setText(String.valueOf(perPrice));
            }


            if (bean.getGuestPriceFee() == 0) {
                holder.childGuestCoinTxt.setVisibility(View.INVISIBLE);
            } else {
                holder.childGuestCoinTxt.setVisibility(View.VISIBLE);
                holder.childGuestCoinTxt.setText(String.valueOf((int) bean.getGuestPriceFee()));
            }
            if (bean.getHostPriceFee() == 0) {
                holder.childHostCoinTxt.setVisibility(View.INVISIBLE);
            } else {
                holder.childHostCoinTxt.setVisibility(View.VISIBLE);
                holder.childHostCoinTxt.setText(String.valueOf((int) bean.getHostPriceFee()));
            }
            boolean isGone = holder.childGuestCoinTxt.getVisibility() == View.INVISIBLE && holder.childHostCoinTxt.getVisibility() == View.INVISIBLE;
            //holder.childCoinsLay.setVisibility(isGone ? View.GONE : View.VISIBLE);
            double hostScore = bean.getHostScore();
            double guestScore = bean.getGusetScore();
            holder.childCustomProgress.setProgressValue((int) hostScore, (int) guestScore, false);
            if (childPosition == mChildItemData.size() - 1) {
                holder.childCloseLayout.setVisibility(View.VISIBLE);
                int mainBgResId = R.drawable.shape_white_radius_bottom_bg;
                holder.contentLay.setBackground(mCtx.getResources().getDrawable(mainBgResId));
            } else {
                holder.childCloseLayout.setVisibility(View.GONE);
                holder.contentLay.setBackground(null);
                holder.contentLay.setBackgroundColor(mCtx.getResources().getColor(R.color.white));
            }

            holder.childCloseLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCollapseListener != null) {
                        mCollapseListener.onCollapseChildItem(groupPosition);
                        ExpandableListAdapter.this.notifyDataSetChanged();
                        expandPosition = -1;
                    }
                }
            });

        }

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        Log.d(TAG, "onGroupExpanded() called with: groupPosition = [" + groupPosition + "]");

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        Log.d(TAG, "onGroupCollapsed() called with: groupPosition = [" + groupPosition + "]");
    }


    public static class ChildViewHolder {
        @BindView(R.id.left_per_money)
        TextView homePerPrice;
        @BindView(R.id.right_per_money)
        TextView guestPerPrice;
        @BindView(R.id.home_lay)
        RelativeLayout childHomeLay;
        @BindView(R.id.guest_lay)
        RelativeLayout childGuestLay;
        @BindView(R.id.home_show_pop_list_selector_view)
        ShowPopListSelectorView homeShowPopSelector;
        @BindView(R.id.guest_show_pop_list_selector_view)
        ShowPopListSelectorView guestShowPopSelector;
        @BindView(R.id.child_home_icon)
        CircleImageView childHostGiftImg;
        @BindView(R.id.child_guest_icon)
        CircleImageView childGuestGiftImg;
        @BindView(R.id.differ_score_chang_ci)
        TextView differScoreAndChangCi;
        @BindView(R.id.match_type_time)
        TextView matchTypeTime;
        @BindView(R.id.home_gold_coin)
        TextView childHostCoinTxt;
        @BindView(R.id.guest_gold_coin)
        TextView childGuestCoinTxt;
        @BindView(R.id.childCloseLayout)
        LinearLayout childCloseLayout;
        @BindView(R.id.child_customer_progress_bar)
        PercentProgressView childCustomProgress;
        @BindView(R.id.child_main_lay)
        LinearLayout childMainLay;
        @BindView(R.id.content_lay)
        LinearLayout contentLay;
        @BindView(R.id.main_content)
        RelativeLayout mainLay;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public static ChildViewHolder getTag(View view) {
            ChildViewHolder holder = (ChildViewHolder) view.getTag();
            if (holder == null) {
                holder = new ChildViewHolder(view);
            }
            return holder;
        }

    }

    public void setOnCollapseChildItem(IOnCollapseChildItem listener) {
        mCollapseListener = listener;
    }

    public void setOnExpandChildItem(IOnExpandChildItem listener) {
        mExpandListener = listener;
    }

    public void setOnGroupSendGiftClick(IOnGroupSendGiftClick listener) {
        this.mGroupSendGiftListener = listener;
    }

    public void setOnChildSendGiftClick(IOnChildSendGiftClick listener) {
        mChildSendGiftListener = listener;
    }

    public void setPopSelectorData(List<Integer> data) {
        mSelectorData = data;
    }


    public interface IOnGroupSendGiftClick {
        public void onGroupHostGift(int pos, String payTypeNo, String boardId, String keyAndOption, String playType);
        public void onGroupGuestGift(int pos, String payTypeNo, String boardId, String keyAndOption, String playType);
    }

    public interface IOnChildSendGiftClick {
        public void onChildHostGift(int pos, String payTypeNo, String boardId, String keyAndOption, String playType);
        public void onChildGuestGift(int pos, String payTypeNo, String boardId, String keyAndOption, String playType);
    }


    public interface IOnCollapseChildItem {
        public void onCollapseChildItem(int groupPosition);
    }

    public interface IOnExpandChildItem {
        public void onExpandChildItem(int groupPosition);
    }

}
