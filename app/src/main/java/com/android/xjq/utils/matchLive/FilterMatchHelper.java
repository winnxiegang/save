package com.android.xjq.utils.matchLive;

import android.content.Context;
import android.view.View;

import com.android.xjq.bean.matchLive.LeagueInfo;
import com.android.xjq.dialog.MatchFilterDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 周毅 on 2015/7/1 14:13.
 */
public class FilterMatchHelper {

    public static final int OPERATE_DEFAULT = 1;

    /**
     * 由于比分直播数据与其他类传过来的数据结构不一样，只能单独考虑
     */
    public static final int OPERATE_MATCH_LIVE = 2;

    private int mOperate = OPERATE_DEFAULT;

    private Context mContext;

    private ArrayList<LeagueInfo> allMatchList = new ArrayList<>();

    private View.OnClickListener mFilteredClickListener = null;

    private List<String> matchFiveIds;

    public FilterMatchHelper(Context context, int operate, View.OnClickListener filteredClickListener) {
        this.mContext = context;

        this.mOperate = operate;

        this.mFilteredClickListener = filteredClickListener;

    }

    public void startFilter(boolean isBasketBallFilter, HashMap<String, Long> allMatchMap, HashMap<String, String> matchFive, List<String> selectedNamesList) {
        getFilterList(allMatchMap, selectedNamesList);
        switch (mOperate) {
            case OPERATE_DEFAULT:
                break;
            case OPERATE_MATCH_LIVE:
                break;
        }
        if (matchFiveIds == null) {
            getLeagueFiveIds(matchFive);
        }
        /*AlertDialogFootballOption.filterDialog(mContext,
                mFilteredClickListener, allMatchList, isBasketBallFilter, matchFiveIds);*/

        new MatchFilterDialog.Builder(mContext)
                .setIsBasketballFilter(isBasketBallFilter)
                .setFilterList(allMatchList)
                .setMatchFiveIds(matchFiveIds)
                .setTouchOutside(true)
                .setOnSelectedListener(mFilteredClickListener)
                .build().show();

    }

    private void getFilterList(HashMap<String, Long> mFilterMap, List<String> selectedNamesList) {
        allMatchList.clear();
        if (mFilterMap == null) return;
        for (Map.Entry<String, Long> entry : mFilterMap.entrySet()) {
            LeagueInfo leagueInfo = new LeagueInfo();
            leagueInfo.setId(String.valueOf(entry.getValue()));
            leagueInfo.setCnShortName(entry.getKey());
            if (selectedNamesList != null && selectedNamesList.size() > 0 &&
                    selectedNamesList.contains(String.valueOf(entry.getKey()))) {
                leagueInfo.setSelected(true);
            }
            allMatchList.add(leagueInfo);
        }

    }

    private void getLeagueFiveIds(HashMap<String, String> matchFive) {
        if (matchFive == null) return;
        Set<String> keySet = matchFive.keySet();
        for (String key : keySet) {
            String matchFiveIdStr = matchFive.get(key);
            if (matchFiveIdStr != null) {
                String[] ids = matchFiveIdStr.split(",");
                matchFiveIds = Arrays.asList(ids);
            }
        }
    }

    /*public void startFilterBasketBall(Object value, ArrayList<LeagueInfo> filterList) {
        AlertDialogFootballOption.filterDialog(mContext, allLeagueName,
                mFilteredClickListener, filterList, true, null, null);

    }*/

}
