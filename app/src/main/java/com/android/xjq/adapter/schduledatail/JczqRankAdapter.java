package com.android.xjq.adapter.schduledatail;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.bean.liveScoreBean.TeamImageUrlUtils;
import com.android.xjq.R;
import com.android.xjq.bean.scheduledetail.RankCountBean;
import com.android.xjq.bean.scheduledetail.RankInfoBean;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhouyi on 2016/5/9 16:18.
 *
 */
public class JczqRankAdapter extends MyBaseAdapter<RankCountBean> {
    private  JczqDataBean jczqDataBean;

    public JczqRankAdapter(Context context, List<RankCountBean> list, JczqDataBean jczqDataBean) {

        super(context, list);
        this.jczqDataBean = jczqDataBean;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_analysis_rank_count_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setItemView(position, viewHolder);

        return convertView;

    }

    private void setItemView(int position, ViewHolder viewHolder) {
        RankCountBean bean = list.get(position);

        viewHolder.rankCountLayout.setVisibility(View.VISIBLE);
        viewHolder.rankSpaceView.setVisibility(View.VISIBLE);
        viewHolder.rcTitleLayout.setVisibility(View.GONE);
        setRankCountView(bean, viewHolder);
        if (bean != null) {
            bean.setDataChanged(false);
        }
    }


    /**
     * 设置排名统计
     *
     * @param rankCountBean
     * @param viewHolder
     */
    private void setRankCountView(RankCountBean rankCountBean, ViewHolder viewHolder) {

        String homeTeamLogoUrl = TeamImageUrlUtils.getFTHomeLogoUrl(jczqDataBean.getInnerHomeTeamId());

        String guestLogoUrl =TeamImageUrlUtils.getFTGuestLogoUrl(jczqDataBean.getInnerGuestTeamId());
        setJczqTeamLogo(viewHolder.rcHomeTeamIv, homeTeamLogoUrl, true);

        setJczqTeamLogo(viewHolder.rcGuestTeamIv,guestLogoUrl, false);

        viewHolder.rcHomeTeamNameTv.setText(jczqDataBean.getHomeTeamName());

        viewHolder.rcGuestTeamNameTv.setText(jczqDataBean.getGuestTeamName());

        if (rankCountBean != null) {

            viewHolder.rcHomeTeamTip.setText(rankCountBean.getHomeTips());

            viewHolder.rcGuestTeamTip.setText(rankCountBean.getGuestTips());
        }

        viewHolder.rcArrowIv.setVisibility(View.GONE);

        setVisibleOrGoneListener(viewHolder.rcArrowIv, viewHolder.rcContentLayout);

        if (viewHolder.rcHomeTeamTitleLayout.getChildCount() == 0) {
            viewHolder.rcHomeTeamTitleLayout.addView(addRankCountTitleView());
        }

        if (viewHolder.rcGuestTeamTitleLayout.getChildCount() == 0) {
            viewHolder.rcGuestTeamTitleLayout.addView(addRankCountTitleView());
        }

        if (rankCountBean == null) {

            viewHolder.rcHomeNotDataTv.setVisibility(View.VISIBLE);

            viewHolder.rcGuestNotDataTv.setVisibility(View.VISIBLE);

        } else {

            if (rankCountBean.isDataChanged() ||
                    (!rankCountBean.isHomeRankDataNull() && viewHolder.rcHomeTeamLayout.getChildCount() == 0) ||
                    (!rankCountBean.isGuestRankDataNull() && viewHolder.rcGuestTeamLayout.getChildCount() == 0)) {

                viewHolder.rcHomeTeamLayout.removeAllViews();

                viewHolder.rcGuestTeamLayout.removeAllViews();

                if (rankCountBean.isHomeRankDataNull()) {
                    viewHolder.rcHomeNotDataTv.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.rcHomeNotDataTv.setVisibility(View.GONE);

                    viewHolder.rcHomeTeamLayout.addView(addRankCountDataView(rankCountBean.getHomeRankDataMap().getAll(), "总"));

                    viewHolder.rcHomeTeamLayout.addView(getCutLine());

                    viewHolder.rcHomeTeamLayout.addView(addRankCountDataView(rankCountBean.getHomeRankDataMap().getHome(), "主"));

                    viewHolder.rcHomeTeamLayout.addView(getCutLine());

                    viewHolder.rcHomeTeamLayout.addView(addRankCountDataView(rankCountBean.getHomeRankDataMap().getGuest(), "客"));

                    viewHolder.rcHomeTeamLayout.addView(getCutLine());

                    viewHolder.rcHomeTeamLayout.addView(addRankCountDataView(rankCountBean.getHomeRankDataMap().getLastSix(), "近6场"));
                }

                if (rankCountBean.isGuestRankDataNull()) {

                    viewHolder.rcGuestNotDataTv.setVisibility(View.VISIBLE);

                } else {

                    viewHolder.rcGuestNotDataTv.setVisibility(View.GONE);

                    viewHolder.rcGuestTeamLayout.addView(addRankCountDataView(rankCountBean.getGuestRankDataMap().getAll(), "总"));

                    viewHolder.rcGuestTeamLayout.addView(getCutLine());

                    viewHolder.rcGuestTeamLayout.addView(addRankCountDataView(rankCountBean.getGuestRankDataMap().getHome(), "主"));

                    viewHolder.rcGuestTeamLayout.addView(getCutLine());

                    viewHolder.rcGuestTeamLayout.addView(addRankCountDataView(rankCountBean.getGuestRankDataMap().getGuest(), "客"));

                    viewHolder.rcGuestTeamLayout.addView(getCutLine());

                    viewHolder.rcGuestTeamLayout.addView(addRankCountDataView(rankCountBean.getGuestRankDataMap().getLastSix(), "近6场"));

                }
            }
        }
    }

    private View getCutLine() {

        View view = new View(context);

        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.dpPoint5)));

        view.setBackgroundColor(context.getResources().getColor(R.color.line_color));

        return view;
    }

    private View addRankCountDataView(RankInfoBean data, String rankName) {

        String[] methodName = new String[]{"Ct", "Wn", "Dw", "Lt", "Ig", "Lg", "Rl", "Pt", "Rk", "Re"};

        LinearLayout contentLayout = new LinearLayout(context);

        contentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.dp35)));

        contentLayout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < 11; i++) {

            TextView tv = new TextView(context);

            LinearLayout.LayoutParams llps = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);

            if (i == 0) {
                llps.weight = 1.5f;
            } else if (i == 10) {
                llps.weight = 2f;
            } else {
                llps.weight = 1f;
            }

            tv.setLayoutParams(llps);

            tv.setGravity(Gravity.CENTER);

            if (i == 0) {
                tv.setText(rankName);
            } else {
                try {
                    if (data == null) {
                        tv.setText("-");
                    } else {
                        if ("Rk".equals(methodName[i - 1]) && rankName.equals("近6天")) {
                            tv.setText("-");
                        } else {
                            Object value = RankInfoBean.class.getMethod("get" + methodName[i - 1]).invoke(data);
                            String txt = (TextUtils.equals(String.valueOf(value), "0") && i == methodName.length - 1) ? "-" : String.valueOf(value);
                            tv.setText(txt);
                            tv.setTextColor(Color.parseColor("#333333"));
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            contentLayout.addView(tv);
        }
        return contentLayout;
    }

    private View addRankCountTitleView() {

        String[] titles = new String[]{"全场", "赛", "胜", "平", "负", "得", "失", "净", "积分", "排名", "胜率"};

        LinearLayout titleLayout = new LinearLayout(context);

        titleLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.dp35)));

        titleLayout.setOrientation(LinearLayout.HORIZONTAL);

        titleLayout.setBackgroundColor(Color.parseColor("#e2e2e2"));

        for (int i = 0; i < titles.length; i++) {
            TextView tv = new TextView(context);
            LinearLayout.LayoutParams llps = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            if (i == 0) {
                llps.weight = 1.5f;
            } else if (i == titles.length - 1) {
                llps.weight = 2f;
            } else {
                llps.weight = 1f;
            }
            tv.setLayoutParams(llps);
            tv.setText(titles[i]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            titleLayout.addView(tv);
        }
        return titleLayout;
    }



    private void setVisibleOrGoneListener(final ImageView titleLayout, final View contentLayout) {
        titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentLayout.getVisibility() == View.GONE) {
                    titleLayout.setImageResource(R.drawable.icon_top_arrow);
                    contentLayout.setVisibility(View.VISIBLE);
                } else {
                    titleLayout.setImageResource(R.drawable.icon_bottom_arrow);
                    contentLayout.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public int getCount() {
//        return list == null ? 0 : 1;
        return list.size();
    }

    static class ViewHolder {
        @BindView(R.id.rankSpaceView)
        View rankSpaceView;
        @BindView(R.id.rcArrowIv)
        ImageView rcArrowIv;
        @BindView(R.id.rcTitleLayout)
        LinearLayout rcTitleLayout;
        @BindView(R.id.rcHomeTeamIv)
        ImageView rcHomeTeamIv;
        @BindView(R.id.rcHomeTeamNameTv)
        TextView rcHomeTeamNameTv;
        @BindView(R.id.rcHomeTeamTip)
        TextView rcHomeTeamTip;
        @BindView(R.id.rcHomeTeamTitleLayout)
        LinearLayout rcHomeTeamTitleLayout;
        @BindView(R.id.rcHomeTeamLayout)
        LinearLayout rcHomeTeamLayout;
        @BindView(R.id.rcHomeNotDataTv)
        TextView rcHomeNotDataTv;
        @BindView(R.id.rcGuestTeamIv)
        ImageView rcGuestTeamIv;
        @BindView(R.id.rcGuestTeamNameTv)
        TextView rcGuestTeamNameTv;
        @BindView(R.id.rcGuestTeamTip)
        TextView rcGuestTeamTip;
        @BindView(R.id.rcGuestTeamTitleLayout)
        LinearLayout rcGuestTeamTitleLayout;
        @BindView(R.id.rcGuestTeamLayout)
        LinearLayout rcGuestTeamLayout;
        @BindView(R.id.rcGuestNotDataTv)
        TextView rcGuestNotDataTv;
        @BindView(R.id.rcContentLayout)
        LinearLayout rcContentLayout;
        @BindView(R.id.rankCountLayout)
        LinearLayout rankCountLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
