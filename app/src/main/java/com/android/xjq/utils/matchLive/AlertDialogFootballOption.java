package com.android.xjq.utils.matchLive;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.android.xjq.R;
import com.android.xjq.adapter.matchLive.MatchFilterAdapter;
import com.android.xjq.bean.matchLive.LeagueInfo;
import com.android.xjq.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 竞彩足球投注
 *
 * @author leslie
 * @version $Id: AlertDialogFootballOption.java, v 0.1 2014年7月21日 上午11:06:37
 *          leslie Exp $
 */
public class AlertDialogFootballOption {

    private static final String TAG = AlertDialogFootballOption.class
            .getSimpleName();


    public static void filterDialog(Context context,
                                    final View.OnClickListener clickListener,
                                    final ArrayList<LeagueInfo> filterList,
                                    boolean isBasketballFilter,
                                    final List<String> matchFiveIds) {

        final AlertDialog dialog = new AlertDialog.Builder(context).create();

        dialog.show();

        Window window = dialog.getWindow();

        window.setContentView(R.layout.dialog_view_match_filter);

        final Button selectAll = (Button) window.findViewById(R.id.select_all);
        Button deselectAll = (Button) window.findViewById(R.id.deselect);
        final Button match5 = (Button) window.findViewById(R.id.match5);
        MyGridView bfWinGv = (MyGridView) window.findViewById(R.id.bfDrawGv);
        Button btnCancel = (Button) window.findViewById(R.id.cancelBtn);
        Button btnOk = (Button) window.findViewById(R.id.okBtn);
        final MatchFilterAdapter optionAdapterWin = new MatchFilterAdapter(context, filterList);
        bfWinGv.setAdapter(optionAdapterWin);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterList == null) return;
                for (int i = 0; i < filterList.size(); i++) {
                    filterList.get(i).setSelected(true);
                }
                optionAdapterWin.notifyDataSetChanged();
            }
        });
        if (isBasketballFilter) {
            deselectAll.setVisibility(View.GONE);
            match5.setText("反选");
            match5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (filterList == null) return;
                    for (int i = 0; i < filterList.size(); i++) {
                        if (filterList.get(i).isSelected()) {
                            filterList.get(i).setSelected(false);
                        } else {
                            filterList.get(i).setSelected(true);
                        }
                    }
                    optionAdapterWin.notifyDataSetChanged();
                }
            });
        } else {
            if (matchFiveIds == null || matchFiveIds.size() == 0) match5.setVisibility(View.GONE);
            deselectAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (filterList == null) return;
                    for (int i = 0; i < filterList.size(); i++) {
                        if (filterList.get(i).isSelected()) {
                            filterList.get(i).setSelected(false);
                        } else {
                            filterList.get(i).setSelected(true);
                        }
                    }
                    optionAdapterWin.notifyDataSetChanged();
                }
            });

            match5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (matchFiveIds == null) {
                        match5.setVisibility(View.GONE);
                        return;
                    }
                    match5.setVisibility(View.VISIBLE);
                    if (filterList == null) return;
                    for (int i = 0; i < filterList.size(); i++) {
                        filterList.get(i).setSelected(false);
                    }
                    for (int i = 0; i < filterList.size(); i++) {
                        if (matchFiveIds.contains(filterList.get(i).getId())) {
                            filterList.get(i).setSelected(true);
                            continue;
                        }
                    }
                    optionAdapterWin.notifyDataSetChanged();
                }
            });
        }


        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<String> selectedIdList = new ArrayList<>();
                if (filterList != null && filterList.size() > 0) {
                    for (int i = 0; i < filterList.size(); i++) {
                        if (filterList.get(i).isSelected()) {
                            selectedIdList.add(filterList.get(i).getId());
                        }
                    }
                }
                v.setTag(selectedIdList);
                clickListener.onClick(v);
                dialog.dismiss();
            }
        });

    }


    /**
     * 混合过关投注
     *
     * @param context
     * @param homeTeamName
     * @param guestTeamName
     * @param concede
     * @param showCancel
     * @param dataList
     * @param myClickAndGetDataListener
     *//*
    public static void hhggDialog(
            MixtureData mixtureData,
            Context context,
            String homeTeamName,
            String guestTeamName,
            int concede,
            boolean showCancel,
            final List<OptionData> dataList,
            final OnMyClickAndGetDataListener<OptionData> myClickAndGetDataListener,
            boolean isGDDG_NotSupportSpf) {

        final AlertDialog dialog = new AlertDialog.Builder(context).create();

        dialog.show();

        Window window = dialog.getWindow();

        window.setContentView(R.layout.view_popup_fb_hhgg);

        TextView homeTeamTv = (TextView) window.findViewById(R.id.homeTeamTv);

        View jqsLayout = window.findViewById(R.id.jqsLayout);

        View bqcLayout = window.findViewById(R.id.bqcLayout);

        View bfLayout = window.findViewById(R.id.bfLayout);

        homeTeamTv.setText(homeTeamName);

        TextView guestTeamTv = (TextView) window.findViewById(R.id.guestTeamTv);

        guestTeamTv.setText(guestTeamName);

        // 点击弹窗里选项处理
        final OnMyCheckedChangeListener myCheckedChangeListener = new OnMyCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                String tag = buttonView.getTag().toString();
                dataList.get(Integer.parseInt(tag)).setSelected(isChecked);
            }
        };
        OnCheckedChangeListener changeListener = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                String code = buttonView.getTag().toString();

                Log.d(TAG, "onCheckedChanged " + code);

                // dataList.get(Integer.parseInt(code)).setSelected(isChecked);

                if (myCheckedChangeListener != null) {
                    myCheckedChangeListener.onCheckedChanged(buttonView,
                            isChecked);
                }

            }
        };
        // 让球胜平负
        if (!isGDDG_NotSupportSpf) {
            window.findViewById(R.id.dialog_view_spf).setVisibility(View.GONE);
            TextView concedeTv = (TextView) window.findViewById(R.id.concedeTv);

            SpannableStringBuilder builder = new SpannableStringBuilder("让\n");
            SpannableStringBuilder concedeBuilder = new SpannableStringBuilder("");
            if (concede > 0) {

                concedeBuilder.append("+" + concede);

                concedeBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, concedeBuilder.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            } else {
                concedeBuilder.append("" + concede);
                concedeBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), 0, concedeBuilder.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

            builder.append(concedeBuilder).append("\n球");

            concedeTv.setText(builder);

            ToggleButton[] htTbButtons = new ToggleButton[LotterySelecterEnum.JCZQ_RQSPF.getSize()];

            htTbButtons[0] = (ToggleButton) window.findViewById(R.id.gridTb0);

            htTbButtons[1] = (ToggleButton) window.findViewById(R.id.gridTb1);

            htTbButtons[2] = (ToggleButton) window.findViewById(R.id.gridTb2);

            htTbButtons[3] = (ToggleButton) window.findViewById(R.id.gridTb3);

            htTbButtons[4] = (ToggleButton) window.findViewById(R.id.gridTb4);

            htTbButtons[5] = (ToggleButton) window.findViewById(R.id.gridTb5);

            List<OptionData> rqspfList = JingCaiPlayUtil.initOptionData(dataList, LotterySelecterEnum.JCZQ_RQSPF);

            setOptionTb(rqspfList, htTbButtons, "spf");

            // 让球胜平负选项监听
            for (int i = 0; i < htTbButtons.length; i++) {
                htTbButtons[i].setOnCheckedChangeListener(changeListener);
            }
        } else {
            window.findViewById(R.id.dialog_view_spf).setVisibility(View.GONE);
        }
        // 进球数
        ToggleButton[] jqTbButtons = new ToggleButton[LotterySelecterEnum.JCZQ_JQS.getSize()];

        jqTbButtons[0] = (ToggleButton) window.findViewById(R.id.gridJqTb0);
        jqTbButtons[1] = (ToggleButton) window.findViewById(R.id.gridJqTb1);
        jqTbButtons[2] = (ToggleButton) window.findViewById(R.id.gridJqTb2);
        jqTbButtons[3] = (ToggleButton) window.findViewById(R.id.gridJqTb3);
        jqTbButtons[4] = (ToggleButton) window.findViewById(R.id.gridJqTb4);
        jqTbButtons[5] = (ToggleButton) window.findViewById(R.id.gridJqTb5);
        jqTbButtons[6] = (ToggleButton) window.findViewById(R.id.gridJqTb6);
        jqTbButtons[7] = (ToggleButton) window.findViewById(R.id.gridJqTb7);

        List<OptionData> jqsList = JingCaiPlayUtil.initOptionData(dataList, LotterySelecterEnum.JCZQ_JQS);

        setOptionTb(jqsList, jqTbButtons, "jqs");

        // 比分主胜

        MyGridView bfWinGv = (MyGridView) window.findViewById(R.id.bfWinGv);

        List<OptionData> bfList = JingCaiPlayUtil.initOptionData(dataList,
                LotterySelecterEnum.JCZQ_BF);

        List<OptionData> winList = bfList.subList(0, 13);

        final BbOptionAdapter optionAdapterWin = new BbOptionAdapter(context,
                winList, LotterySelecterEnum.JCZQ_BF, myCheckedChangeListener);

        bfWinGv.setAdapter(optionAdapterWin);

        // 比分平
        MyGridView bfDrawGv = (MyGridView) window.findViewById(R.id.bfDrawGv);

        List<OptionData> drawList = bfList.subList(13, 18);

        final BbOptionAdapter optionAdapterDraw = new BbOptionAdapter(context,
                drawList, LotterySelecterEnum.JCZQ_BF, myCheckedChangeListener);

        bfDrawGv.setAdapter(optionAdapterDraw);

        // 比分负

        MyGridView bfLostGv = (MyGridView) window.findViewById(R.id.bfLostGv);

        List<OptionData> lostList = bfList.subList(18, 31);

        final BbOptionAdapter optionAdapterLost = new BbOptionAdapter(context,
                lostList, LotterySelecterEnum.JCZQ_BF, myCheckedChangeListener);

        bfLostGv.setAdapter(optionAdapterLost);

        // 半全场

        MyGridView bqcGv = (MyGridView) window.findViewById(R.id.bqcGv);

        List<OptionData> bqcList = JingCaiPlayUtil.initOptionData(dataList,
                LotterySelecterEnum.JCZQ_BQC);

        final BbOptionAdapter optionAdapterBqc = new BbOptionAdapter(context,
                bqcList, LotterySelecterEnum.JCZQ_BQC, myCheckedChangeListener);

        bqcGv.setAdapter(optionAdapterBqc);

        // 进球数选项监听

        for (int i = 0; i < jqTbButtons.length; i++) {
            jqTbButtons[i].setOnCheckedChangeListener(changeListener);
        }

        if (showCancel) {

            Button cancelBtn = (Button) window.findViewById(R.id.cancelBtn);

            cancelBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    dialog.cancel();

                }
            });

        }

        Button okBtn = (Button) window.findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (myClickAndGetDataListener != null) {

                    // 所有的data数据
                    myClickAndGetDataListener.onClick(v, dataList);
                }

            }
        });

        CZAppUtil.setMoreViewBackground(jqsLayout, mixtureData.isJqsSupportGddg(), mixtureData.isShowRedBg());

        CZAppUtil.setMoreViewBackground(bfLayout, mixtureData.isBfSupportGddg(), mixtureData.isShowRedBg());

        CZAppUtil.setMoreViewBackground(bqcLayout, mixtureData.isBqcSupportGddg(), mixtureData.isShowRedBg());

    }


    *//**
     * @param dataList
     * @param buttons
     *//*
    static void setOptionTb(List<OptionData> dataList, ToggleButton[] buttons, String playType) {

        for (int i = 0; i < dataList.size(); i++) {

            OptionData data = dataList.get(i);
            SpannableStringBuilder title = new SpannableStringBuilder(data.getTitle() + "球");
            title.append("\n" + data.getOdds());
            buttons[i].setText(title);
            buttons[i].setTextOff(title);
            buttons[i].setTextOn(title);
            buttons[i].setTag(data.getIndex());
            buttons[i].setChecked(data.isSelected());
            buttons[i].setEnabled(data.isEnabled());

        }

    }

    private static List<OptionData> initOptionData(
            List<OptionData> optionDataList, JczqSpfEnum jczqSpfEnum) {

        List<OptionData> dataList = new ArrayList<OptionData>();

        for (OptionData optionData : optionDataList) {

            JczqScoreEnum jczqScoreEnum = JczqScoreEnum.valueOf(optionData.getCode());

            if (jczqSpfEnum == JczqSpfEnum.SPF_WIN) {

                if (jczqScoreEnum.homeScore() > jczqScoreEnum.guestScore()) {

                    dataList.add(optionData);
                } else {
                    continue;
                }

            } else if (jczqSpfEnum == JczqSpfEnum.SPF_DRAW) {
                if (jczqScoreEnum.homeScore() == jczqScoreEnum.guestScore()) {
                    dataList.add(optionData);
                } else {
                    continue;
                }
            } else {

                if (jczqScoreEnum.homeScore() < jczqScoreEnum.guestScore()) {
                    dataList.add(optionData);
                } else {
                    continue;
                }

            }

        }

        return dataList;
    }*/

}
