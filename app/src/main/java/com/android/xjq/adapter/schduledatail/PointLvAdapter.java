package com.android.xjq.adapter.schduledatail;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.commlib.utils.DetailsHtmlShowUtils;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.view.MyListView;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.bean.scheduledetail.JczqRankingBean;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by lingjiu on 2017/1/20 11:14.
 */
public class PointLvAdapter extends MyBaseAdapter<JczqRankingBean> {
    private String matchRuleText;

    private boolean mHideFormat;//是否隐藏赛制
    private boolean mHideGuestList;//是否不显示客队列表        点击箭头
    private boolean mHideHomeList;//是否不显示主队列表

    public void setMatchRuleText(String matchRuleText) {
        this.matchRuleText = matchRuleText;
    }

    public PointLvAdapter(Context context, List<JczqRankingBean> seasonRaceList) {
        super(context, seasonRaceList);
    }

    @Override
    public int getCount() {

        if (list.size() == 0) {
            return 1;
        } else {
            return list.size();
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.analysis_points_list_view_item, null);
        }
        ViewHolder holder = ViewHolder.getTag(convertView);
        setItemView(position, holder);
        return convertView;
    }


    private void setItemView(int position, final ViewHolder holder) {

        if (list.size() == 0 || list.get(0) == null || list.get(0).isEmpty()) {
            initEmptyItem(holder);
        } else {
            initItem(position, holder);
        }

        //赛制的显示：有无数据都显示

        if (!TextUtils.isEmpty(matchRuleText) && !mHideFormat) {
            holder.formatDescLayout.setVisibility(View.VISIBLE);

            holder.formatDescTv.setVisibility(View.VISIBLE);

            holder.formatTabIv.setImageResource(R.drawable.points_up);

           DetailsHtmlShowUtils.setHtmlText(holder.formatDescTv, matchRuleText);
        } else {
            holder.formatTabIv.setImageResource(R.drawable.point_down);
            holder.formatDescLayout.setVisibility(View.GONE);
        }

        if (mHideFormat) {
            LogUtils.e("kk",mHideFormat+"----");
            holder.formatTabIv.setImageResource(R.drawable.point_down);
            holder.formatDescLayout.setVisibility(View.VISIBLE);
        }

        holder.formatTabIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHideFormat = !mHideFormat;
                notifyDataSetChanged();
                holder.formatTabIv.setImageResource(mHideFormat ? R.drawable.point_down : R.drawable.points_up);
            }
        });
        //  setVisibleOrGoneListener(holder.formatTabIv, holder.formatDescTv, holder.mGuestLayout);
    }

    private void setFlexBoxLayout(List<JczqRankingBean.RankColor> list, int position, ViewHolder holder) {

        boolean isEmpty = list == null || list.size() == 0;
        if (position == 0) {
            holder.mHomeLayout.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            holder.mHomeLayout.removeAllViews();
        } else {
            holder.mGuestLayout.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            holder.mGuestLayout.removeAllViews();
        }
        if (isEmpty) return;

        int size = list.size();
        int colorViewLength = LibAppUtil.dip2px(context, 14);
        int margin = LibAppUtil.dip2px(context, 4);
        for (int i = 0; i < size; i++) {
            JczqRankingBean.RankColor rankColor = list.get(i);

            LinearLayout layout = new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.gravity = Gravity.CLIP_VERTICAL;
            layout.setGravity(Gravity.CENTER);
            layout.setPadding(0, 0, 2 * margin, 2 * margin);
            layout.setLayoutParams(layoutParams);

            View colorView = new View(context);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(colorViewLength, colorViewLength);
            params1.gravity = Gravity.CLIP_VERTICAL;
            colorView.setLayoutParams(params1);
            colorView.setBackgroundColor(Color.parseColor(rankColor.color));

            TextView teamGrade = new TextView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -1);

            params.gravity = Gravity.CLIP_VERTICAL;
            params.leftMargin = margin;
            teamGrade.setLayoutParams(params);
            teamGrade.setTextColor(ContextCompat.getColor(context, R.color.gray_text_color));
            teamGrade.setGravity(Gravity.TOP);
            teamGrade.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            teamGrade.setText(rankColor.teamGrade);
            layout.addView(colorView);
            layout.addView(teamGrade);

            if (position == 0)
                holder.mHomeLayout.addView(layout);
            else
                holder.mGuestLayout.addView(layout);
        }

    }

    private void initEmptyItem(ViewHolder holder) {
        holder.homeLayout.setVisibility(View.GONE);
        holder.guestLayout.setVisibility(View.GONE);
        holder.emptyLayout.setVisibility(View.VISIBLE);
        holder.emptyTipTv.setText("暂无数据");
    }

    private void initItem(int position, final ViewHolder holder) {
        //holder.homeTabIv.setVisibility(View.GONE);
        // holder.guestTabIv.setVisibility(View.GONE);
        // holder.formatTabIv.setVisibility(View.GONE);

        holder.homeTabIv.setImageResource(R.drawable.points_up);

        holder.guestTabIv.setImageResource(R.drawable.points_up);

        holder.formatTabIv.setImageResource(R.drawable.points_up);

        holder.emptyLayout.setVisibility(View.GONE);

        JczqRankingBean bean = list.get(position);

        JczqRankingBean.RankTitleBean rankTitle = bean.getRankTitle();

        if (rankTitle.getSameTitle() != null) {
            holder.homeLayout.setVisibility(View.VISIBLE);

            holder.homeTabLayout.setVisibility(View.VISIBLE);

            holder.homeContentLayout.setVisibility(View.VISIBLE);

            holder.homeTabTitle.setText(rankTitle.getSameTitle());

            if (bean.getSameRankDataList() != null && !mHideHomeList) {
                setFlexBoxLayout((bean.getRankColor() == null || bean.getRankColor().size() == 0) ? bean.getHomeRankColor() : bean.getRankColor(), 0, holder);

                PointLvSubAdapter pointLvSubAdapter = new PointLvSubAdapter(context, bean.getSameRankDataList());

                holder.homeItemLv.setAdapter(pointLvSubAdapter);
            }

        }

        if (rankTitle.getHomeTitle() != null) {
            holder.homeLayout.setVisibility(View.VISIBLE);

            holder.homeTabLayout.setVisibility(View.VISIBLE);

            holder.homeContentLayout.setVisibility(View.VISIBLE);

            holder.homeTabTitle.setText(rankTitle.getHomeTitle());

            if (bean.getHomeList() != null && !mHideHomeList) {
                setFlexBoxLayout((bean.getRankColor() == null || bean.getRankColor().size() == 0) ? bean.getHomeRankColor() : bean.getRankColor(), 0, holder);

                PointLvSubAdapter pointLvSubAdapter = new PointLvSubAdapter(context, bean.getHomeList());

                holder.homeItemLv.setAdapter(pointLvSubAdapter);
            }
        }

        if (rankTitle.getGuestTitle() != null) {
            holder.guestLayout.setVisibility(View.VISIBLE);

            holder.guestTableLayout.setVisibility(View.VISIBLE);

            holder.guestContentLayout.setVisibility(View.VISIBLE);

            holder.guestTabTitle.setText(rankTitle.getGuestTitle());

            if (bean.getGuestList() != null && !mHideGuestList) {

                setFlexBoxLayout(bean.getGuestRankColor(), 1, holder);

                PointLvSubAdapter pointLvSubAdapter = new PointLvSubAdapter(context, bean.getGuestList());

                holder.guestItemLv.setAdapter(pointLvSubAdapter);

            }

        }
        if (mHideHomeList)
            holder.homeTabIv.setImageResource(R.drawable.point_down);

        if (mHideGuestList)
            holder.guestTabIv.setImageResource(R.drawable.point_down);

        setVisibleOrGoneListener(holder.homeTabIv, holder.homeContentLayout, holder.mHomeLayout);

        setVisibleOrGoneListener(holder.guestTabIv, holder.guestContentLayout, holder.mGuestLayout);
    }


    public void setVisibleOrGoneListener(final ImageView titleLayout, final View contentLayout, final FlexboxLayout mFlextLayout) {
        titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleLayout.getId() == R.id.homeTabIv) {
                    mHideHomeList = !mHideHomeList;
                } else {
                    mHideGuestList = !mHideGuestList;
                }
                notifyDataSetChanged();
            }
        });
    }


//
//
//    public static class ViewHolder {
//        @Bind(R.id.homeLayout)
//        LinearLayout homeLayout;
//        @Bind(R.id.guestLayout)
//        LinearLayout guestLayout;
//        @Bind(R.id.homeTabTitle)
//        TextView homeTabTitle;
//        @Bind(R.id.homeTabIv)
//        ImageView homeTabIv;
//        @Bind(R.id.homeTabLayout)
//        RelativeLayout homeTabLayout;
//        @Bind(R.id.homeTitleLl)
//        LinearLayout homeTitleLl;
//        @Bind(R.id.homeItemLv)
//        MyListView homeItemLv;
//        @Bind(R.id.guestTabTitle)
//        TextView guestTabTitle;
//        @Bind(R.id.guestTabIv)
//        ImageView guestTabIv;
//        @Bind(R.id.guestTableLayout)
//        RelativeLayout guestTableLayout;
//        @Bind(R.id.guestTitleLl)
//        LinearLayout guestTitleLl;
//        @Bind(R.id.guestItemLv)
//        MyListView guestItemLv;
//        @Bind(R.id.emptyTipTv)
//        TextView emptyTipTv;
//        @Bind(R.id.emptyLayout)
//        LinearLayout emptyLayout;
//        @Bind(R.id.homeContentLayout)
//        LinearLayout homeContentLayout;
//        @Bind(R.id.guestContentLayout)
//        LinearLayout guestContentLayout;
//        @Bind(R.id.formatDescLayout)
//        LinearLayout formatDescLayout;
//        @Bind(R.id.formatDescTv)
//        TextView formatDescTv;
//        @Bind(R.id.formatTabIv)
//        ImageView formatTabIv;
//        @Bind(R.id.flexBox)
//        FlexboxLayout mHomeLayout;
//        @Bind(R.id.guestflexBox)
//        FlexboxLayout mGuestLayout;
//
//
//    }

    static class ViewHolder {
        @BindView(R.id.homeTabTitle)
        TextView homeTabTitle;
        @BindView(R.id.homeTabIv)
        ImageView homeTabIv;
        @BindView(R.id.homeTabLayout)
        RelativeLayout homeTabLayout;
        @BindView(R.id.flexBox)
        FlexboxLayout mHomeLayout;
        @BindView(R.id.teamRankTv)
        TextView teamRankTv;
        @BindView(R.id.teamNameTv)
        TextView teamNameTv;
        @BindView(R.id.matchCountTv)
        TextView matchCountTv;
        @BindView(R.id.matchWinTv)
        TextView matchWinTv;
        @BindView(R.id.matchDrawTv)
        TextView matchDrawTv;
        @BindView(R.id.matchLostTv)
        TextView matchLostTv;
        @BindView(R.id.inGoalTv)
        TextView inGoalTv;
        @BindView(R.id.lostGoalTv)
        TextView lostGoalTv;
        @BindView(R.id.realGoaTv)
        TextView realGoaTv;
        @BindView(R.id.matchPointTv)
        TextView matchPointTv;
        @BindView(R.id.homeTitleLl)
        LinearLayout homeTitleLl;
        @BindView(R.id.homeItemLv)
        MyListView homeItemLv;
        @BindView(R.id.homeContentLayout)
        LinearLayout homeContentLayout;
        @BindView(R.id.homeLayout)
        LinearLayout homeLayout;
        @BindView(R.id.guestTabTitle)
        TextView guestTabTitle;
        @BindView(R.id.guestTabIv)
        ImageView guestTabIv;
        @BindView(R.id.guestTableLayout)
        RelativeLayout guestTableLayout;
        @BindView(R.id.guestflexBox)
        FlexboxLayout mGuestLayout;;
        @BindView(R.id.guestTitleLl)
        LinearLayout guestTitleLl;
        @BindView(R.id.guestItemLv)
        MyListView guestItemLv;
        @BindView(R.id.guestContentLayout)
        LinearLayout guestContentLayout;
        @BindView(R.id.guestLayout)
        LinearLayout guestLayout;
        @BindView(R.id.emptyTipTv)
        TextView emptyTipTv;
        @BindView(R.id.emptyLayout)
        LinearLayout emptyLayout;
        @BindView(R.id.formatTabIv)
        ImageView formatTabIv;
        @BindView(R.id.formatDescTv)
        TextView formatDescTv;
        @BindView(R.id.formatDescLayout)
        LinearLayout formatDescLayout;

        public ViewHolder(View rootView) {
            ButterKnife.bind(this, rootView);
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
