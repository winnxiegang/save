package com.android.xjq.activity.myzhuwei.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.view.PercentProgressView;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.myzhuwei.ThemeSetActivity;
import com.android.xjq.bean.myzhuwei.BasketballRacesBean;
import com.android.xjq.bean.myzhuwei.FootballRacesBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kokuma on 2017/11/7.
 */

public class ThemeExpandableAdapter extends BaseExpandableListAdapter {

    List<Object> listParent;

    public ExpandableListView expandableListView;
    public View.OnClickListener onClickListener;
    public List<Object> listThemeObjs;
    public List<String> listThemeIds;
    public Map<String, List<? extends Object>> mapChild;
    public int countChild;

    Context context;

    public int groupPosSelect = -1;

    public ThemeExpandableAdapter(Context context) {
        this.listParent = new ArrayList<>();
        // this.listParent.addAll(list);
        this.context = context;
        mapChild = new HashMap<>();
    }


    public void update(List<? extends Object> list) {
        listParent.clear();
        listParent.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return listParent.size();
    }


    public static void setGroupViewHot(Object bean, TextView tvCoinZhu, TextView tvCoinKe, PercentProgressView bar) {
        FootballRacesBean.FootballRaceClientSimpleListBean footBean = null;
        BasketballRacesBean.BasketballRaceClientSimpleListBean basketBean = null;
        List<BasketballRacesBean.SumInfoBean> sumInfoBeanList = null;
        String hotZhu = "", hotke = "", coinFeeZhu = "", coinFeeKe = "";
        FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean gameBoardBean = null;
        if (bean instanceof FootballRacesBean.FootballRaceClientSimpleListBean) {
            footBean = (FootballRacesBean.FootballRaceClientSimpleListBean) bean;
            gameBoardBean = footBean.gameBoardBean;
            sumInfoBeanList = footBean.sumInfoList;
        } else if (bean instanceof BasketballRacesBean.BasketballRaceClientSimpleListBean) {
            basketBean = (BasketballRacesBean.BasketballRaceClientSimpleListBean) bean;
            gameBoardBean = basketBean.gameBoardBean;
            sumInfoBeanList = basketBean.sumInfoList;
        } else if (bean instanceof FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean) {
            gameBoardBean = (FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean) bean;
        }

        try {
            if (gameBoardBean != null && gameBoardBean.getLeftGameBoardOptionEntry() != null) {
                hotZhu = gameBoardBean.getLeftGameBoardOptionEntry().getTotalFee() + "";
                if(gameBoardBean.getLeftGameBoardOptionEntry().getTotalPaidFee()>0){
                    coinFeeZhu += gameBoardBean.getLeftGameBoardOptionEntry().getTotalPaidFee();
                }
            }

            if (gameBoardBean != null && gameBoardBean.getRightGameBoardOptionEntry() != null) {
                hotke = gameBoardBean.getRightGameBoardOptionEntry().getTotalFee() + "";
                if(gameBoardBean.getRightGameBoardOptionEntry().getTotalPaidFee()>0){
                    coinFeeKe += gameBoardBean.getRightGameBoardOptionEntry().getTotalPaidFee();
                }
            }




            if (sumInfoBeanList != null) {
                for (BasketballRacesBean.SumInfoBean sumInfoBean : sumInfoBeanList) {
                    if (sumInfoBean.getOptionCode().equals("HOME_WIN")) {
                        hotZhu = sumInfoBean.getSumFee() + "";
                    } else if (sumInfoBean.getOptionCode().equals("GUEST_WIN")) {
                        hotke = sumInfoBean.getSumFee() + "";
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("xxl", "bug111");
        }
        if(TextUtils.isEmpty(coinFeeZhu)){
            tvCoinZhu.setVisibility(View.INVISIBLE);
        }else {
            tvCoinZhu.setText(coinFeeZhu);
            tvCoinZhu.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(coinFeeKe)){
            tvCoinKe.setVisibility(View.INVISIBLE);
        }else {
            tvCoinKe.setText(coinFeeKe);
            tvCoinKe.setVisibility(View.VISIBLE);
        }
        if (hotZhu.length() > 0 && hotke.length() > 0) {
            try {
                double zhu = Double.parseDouble(hotZhu);
                double ke = Double.parseDouble(hotke);
                int intHotZhu = (int) zhu;
                int intHotKe = (int) ke;
                //
                if (intHotZhu + intHotKe > 0) {
                   // int progress = intHotZhu * 100 / (intHotZhu + intHotKe);
                    bar.setProgressValue(intHotZhu, intHotKe);
//                    tvCoinZhu.setVisibility(View.VISIBLE);
//                    tvCoinKe.setVisibility(View.VISIBLE);
                    bar.setVisibility(View.VISIBLE);
                } else {
//                    tvCoinZhu.setVisibility(View.GONE);
//                    tvCoinKe.setVisibility(View.GONE);
                    bar.setProgressValue(0, 0);
                    bar.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
//                tvCoinZhu.setVisibility(View.GONE);
//                tvCoinKe.setVisibility(View.GONE);
                bar.setProgressValue(0, 0);
                bar.setVisibility(View.GONE);
            }
        } else {
//            tvCoinZhu.setVisibility(View.GONE);
//            tvCoinKe.setVisibility(View.GONE);
            bar.setProgressValue(0, 0);
            bar.setVisibility(View.GONE);
        }
    }


    //  bean必须是FootballRacesBean.FootballRaceClientSimpleListBean  或者 BasketballRacesBean.BasketballRaceClientSimpleListBean 类型
    public static void setGroupViewMain(Object bean, TextView support_txt, TextView changci_txt, TextView tvZhuName, TextView tvKeName, @Nullable TextView ctr_name, @Nullable TextView ctr_time) {
        FootballRacesBean.FootballRaceClientSimpleListBean footBean = null;
        BasketballRacesBean.BasketballRaceClientSimpleListBean basketBean = null;
        String name1 = "";
        String content = "当前赛事所有助威已结束，请等待派奖";
        String nameZhu = "";
        FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean gameBoardBean = null;
        if (bean instanceof FootballRacesBean.FootballRaceClientSimpleListBean) {
            footBean = (FootballRacesBean.FootballRaceClientSimpleListBean) bean;
            gameBoardBean = footBean.gameBoardBean;
            nameZhu = footBean.getHomeTeamName();
            //名称
            if(tvZhuName!=null&&tvKeName!=null){
                tvZhuName.setText(footBean.getHomeTeamName());
                tvKeName.setText(footBean.getGuestTeamName());
            }
            //赛事名、时间
            if (ctr_name != null) {
                ctr_name.setText(footBean.getMatchName());
            }
            if (ctr_time != null) {
                ctr_time.setText(getGmtFormatedTime(footBean.getGmtStart()));
            }
        } else if (bean instanceof BasketballRacesBean.BasketballRaceClientSimpleListBean) {
            basketBean = (BasketballRacesBean.BasketballRaceClientSimpleListBean) bean;
            gameBoardBean = basketBean.gameBoardBean;
            nameZhu = basketBean.getHomeTeamName();
            //名称
            if(tvZhuName!=null&&tvKeName!=null){
                tvZhuName.setText(basketBean.getHomeTeamName());
                tvKeName.setText(basketBean.getGuestTeamName());
            }
            //赛事名、时间
            if (ctr_name != null) {
                ctr_name.setText(basketBean.getMatchName());
            }
            if (ctr_time != null) {
                ctr_time.setText(getGmtFormatedTime(basketBean.getGmtStart()));
            }
        } else if (bean instanceof FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean) {
            gameBoardBean = (FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean) bean;
            nameZhu = gameBoardBean.homeName;
            //名称
            if(tvZhuName!=null&&tvKeName!=null){
                tvZhuName.setText(gameBoardBean.homeName);
                tvKeName.setText(gameBoardBean.guestName);
            }
        }
        try {
            String plateTxt = "";
            String rangTxt = "";
            if (gameBoardBean != null && gameBoardBean.getRaceStageType() != null) {

                name1 = "[" + gameBoardBean.getRaceStageType().getName() + "]比分";
                if (gameBoardBean.getRaceType() != null && gameBoardBean.getRaceType().equals("FOOTBALL")) {
                    rangTxt += "球";
                } else {
                    rangTxt += "分";
                }

                if (gameBoardBean.getPlate() > 0) {
                    plateTxt += "+" + gameBoardBean.getPlate();
                    content = "<font color='#505050'>" + nameZhu
                            + "</font><font color='#f7453d'>" +  plateTxt
                            + "</font><font color='#505050'>" + rangTxt + "， 支持哪支球队获胜？</font>";
                } else {
                    plateTxt += gameBoardBean.getPlate();
                    content = "<font color='#505050'>" + nameZhu
                            + "</font><font color='#009900'>" +  plateTxt
                            + "</font><font color='#505050'>" + rangTxt + "， 支持哪支球队获胜？</font>";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("xxl", "bug222");
        }
        //赛局名称让球让分
        changci_txt.setText(name1);
        support_txt.setText(Html.fromHtml(content));
    }

    public static String getGmtFormatedTime(String time) {
        Date date = TimeUtils.stringToDate(time, "");
        return TimeUtils.dateToString2(date, TimeUtils.MATCH_FORMAT);
    }


    @Override
    public int getChildrenCount(int i) {
        Object object = getGroup(i);
        if (object != null) {
            List<? extends Object> list = mapChild.get(ThemeSetActivity.getRaceId(object));
            if (list != null) {
                return list.size();
            }
        }
        return 0;
    }

    @Override
    public Object getGroup(int i) {
        if (listParent.size() > i) {
            return listParent.get(i);
        }
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        Object object = getGroup(i);
        if (object != null) {
            List<? extends Object> list = mapChild.get(ThemeSetActivity.getRaceId(object));
            if (list != null) {
                return list.get(i1);
            }
        }
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View root, ViewGroup viewGroup) {
        ThemeExpandableAdapter.ViewHolder holder;
        if (root == null) {
            root = LayoutInflater.from(context).inflate(R.layout.item_theme_select, null);

            holder = new ViewHolder(root);

            root.setTag(R.id.tag_first, holder);
        } else {
            holder = (ThemeExpandableAdapter.ViewHolder) root.getTag(R.id.tag_first);
        }

        Object obj = listParent.get(i);
        root.setTag(obj);
        setGroupViewMain(obj, holder.support_txt, holder.changci_txt, holder.zhu_name, holder.ke_name, holder.ctr_name, holder.ctr_time);
        setGroupViewHot(obj, holder.left_value_txt, holder.right_value_txt, holder.progressBar);
        String id = ThemeSetActivity.getId(obj);
        holder.tvSelect.setOnClickListener(onClickListener);
        if (TextUtils.isEmpty(id)) {
            holder.tvSelect.setVisibility(View.GONE);
        } else {
            holder.tvSelect.setVisibility(View.VISIBLE);
            holder.tvSelect.setTag(obj);
            LogUtils.i("xxl", "getGroupView-id-" + id);
            if (listThemeIds.contains(id)) {
                holder.tvSelect.setTag(R.id.id_tag, true);
                holder.tvSelect.setImageResource(R.drawable.icon_contact_selected);
            } else {
                holder.tvSelect.setTag(R.id.id_tag, false);
                holder.tvSelect.setImageResource(R.drawable.item_checked_normal);
            }
        }
         boolean isGroupExpanded =  (groupPosSelect == i);

        if (ThemeExpandableAdapter.isExistedOtherGameBoard(obj)) {  //!isGroupExpanded&&
            List<? extends Object> list = mapChild.get(ThemeSetActivity.getRaceId(obj));
            if(isGroupExpanded&&list != null&&list.size()>0){
                holder.expandLayout.setVisibility(View.GONE);
            }else {
                holder.expandLayout.setVisibility(View.VISIBLE);
            }
            LogUtils.i("xxl", "getGroupView-ivFolder-VISIBLE");
        } else {
            holder.expandLayout.setVisibility(View.GONE);
            LogUtils.i("xxl", "getGroupView--ivFolder-GONE");
        }
        return root;
    }

    public static boolean isExistedOtherGameBoard(Object obj) {
        if (obj instanceof FootballRacesBean.FootballRaceClientSimpleListBean) {
            FootballRacesBean.FootballRaceClientSimpleListBean bean = (FootballRacesBean.FootballRaceClientSimpleListBean) obj;
            return bean.isExistedOtherGameBoard();

        } else if (obj instanceof BasketballRacesBean.BasketballRaceClientSimpleListBean) {
            BasketballRacesBean.BasketballRaceClientSimpleListBean bean = (BasketballRacesBean.BasketballRaceClientSimpleListBean) obj;
            return bean.isExistedOtherGameBoard();
        }
        return false;
    }

//    void  hideInfoViews(View root){
//      ViewGroup  layoutInfo  = (ViewGroup) root.findViewById(R.id.layoutInfo);
//        int count = layoutInfo.getChildCount();
//        for(int i=0;i<count;i++){
//          View child =  layoutInfo.getChildAt(i);
//            if(child.getId() != R.id.tvSelect){
//                child.setVisibility(View.GONE);
//            }
//        }
//    }

    @Override
    public View getChildView(int i, int i1, boolean b, View root, ViewGroup viewGroup) {
        ThemeExpandableAdapter.ViewHolder holder;
        if (root == null) {
            root = LayoutInflater.from(context).inflate(R.layout.item_theme_select_child, null);

            holder = new ViewHolder(root);

            root.setTag(R.id.tag_first, holder);
        } else {
            holder = (ThemeExpandableAdapter.ViewHolder) root.getTag(R.id.tag_first);
        }

        // hideInfoViews(root);
        // root.setBackgroundColor(context.getResources().getColor(R.color.background_gray2));
        // holder.viewDivider.setVisibility(View.GONE);
        //
        if (countChild - 1 == i1) {
            holder.expandLayout.setVisibility(View.VISIBLE);
        } else {
            holder.expandLayout.setVisibility(View.GONE);
        }
        holder.expandLayout.setTag(i);
        holder.expandLayout.setOnClickListener(onClickListener);
        Object obj = getChild(i, i1);

        if (obj != null) {
            FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean gameBoardBean = (FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean) obj;
            Object objG = getGroup(i);
            if (gameBoardBean != null && objG != null) {
                if (objG instanceof FootballRacesBean.FootballRaceClientSimpleListBean) {
                    FootballRacesBean.FootballRaceClientSimpleListBean bean = (FootballRacesBean.FootballRaceClientSimpleListBean) objG;
                    gameBoardBean.homeName = bean.getHomeTeamName();
                    gameBoardBean.guestName = bean.getGuestTeamName();
                } else if (objG instanceof BasketballRacesBean.BasketballRaceClientSimpleListBean) {
                    BasketballRacesBean.BasketballRaceClientSimpleListBean bean = (BasketballRacesBean.BasketballRaceClientSimpleListBean) objG;
                    gameBoardBean.homeName = bean.getHomeTeamName();
                    gameBoardBean.guestName = bean.getGuestTeamName();
                }
            }
            //
            setGroupViewMain(obj, holder.support_txt, holder.changci_txt, holder.zhu_name, holder.ke_name, holder.ctr_name, holder.ctr_time);
            setGroupViewHot(obj, holder.left_value_txt, holder.right_value_txt, holder.progressBar);
            //
            holder.tvSelect.setOnClickListener(onClickListener);
            String id = ThemeSetActivity.getId(obj);
            holder.tvSelect.setTag(obj);
            LogUtils.i("xxl", "getChildView-id1-" + id);
            if (TextUtils.isEmpty(id)) {
                holder.tvSelect.setVisibility(View.GONE);
            } else {
                holder.tvSelect.setVisibility(View.VISIBLE);
                holder.tvSelect.setTag(obj);
                LogUtils.i("xxl", "getChildView-id2-" + id);
                if (listThemeIds.contains(id)) {
                    holder.tvSelect.setTag(R.id.id_tag, true);
                    holder.tvSelect.setImageResource(R.drawable.icon_contact_selected);
                } else {
                    holder.tvSelect.setTag(R.id.id_tag, false);
                    holder.tvSelect.setImageResource(R.drawable.item_checked_normal);
                }
            }


        }
        return root;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    static class ViewHolder {
        TextView support_txt;
        TextView changci_txt;
        TextView zhu_name;
        TextView ke_name;
        TextView ctr_name;
        TextView ctr_time;
        TextView left_value_txt;
        TextView right_value_txt;
        ImageView tvSelect;
        View viewDivider;
        PercentProgressView progressBar;
        LinearLayout expandLayout;

        ViewHolder(View root) {
            support_txt = (TextView) root.findViewById(R.id.support_txt);
            changci_txt = (TextView) root.findViewById(R.id.changci_txt);
            zhu_name = (TextView) root.findViewById(R.id.zhu_name);
            ke_name = (TextView) root.findViewById(R.id.ke_name);
            ctr_name = (TextView) root.findViewById(R.id.ctr_name);
            ctr_time = (TextView) root.findViewById(R.id.ctr_time);
            left_value_txt = (TextView) root.findViewById(R.id.left_value_txt);
            right_value_txt = (TextView) root.findViewById(R.id.right_value_txt);
            tvSelect = (ImageView) root.findViewById(R.id.tvSelect);
            progressBar = (PercentProgressView) root.findViewById(R.id.progressBar);
            expandLayout = (LinearLayout) root.findViewById(R.id.expandLayout);
            viewDivider = root.findViewById(R.id.viewDivider);
        }
    }
}
