package com.android.xjq.presenters;

import android.util.Log;

import com.android.banana.commlib.base.BasePresenter;
import com.android.banana.commlib.bean.liveScoreBean.TeamImageUrlUtils;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.jjx.sdk.utils.LogUtils;
import com.android.xjq.bean.DynamicBean;
import com.android.xjq.bean.MatchScheduleBean;
import com.android.xjq.bean.matchLive.MatchScheduleEntity;
import com.android.xjq.bean.matchschedule.ChannelAreaBean;
import com.android.xjq.presenters.viewinface.MatchScheduleView;
import com.android.xjq.utils.XjqUrlEnum;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.banana.commlib.http.AppParam.BT_API_DOMAIN;
import static com.android.banana.commlib.http.AppParam.FT_API_DOMAIN;
import static com.android.banana.commlib.http.AppParam.FT_API_S_URL;
import static com.android.xjq.utils.XjqUrlEnum.BASKETBALL_RACE_WITH_CHANNEL_AREA_INFO_BY_DATE;
import static com.android.xjq.utils.XjqUrlEnum.CHANNEL_AREA_USER_ORDER_CANCEL;
import static com.android.xjq.utils.XjqUrlEnum.CHANNEL_AREA_USER_ORDER_CREATE;
import static com.android.xjq.utils.XjqUrlEnum.DYNAMIC_DATA_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.DYNAMIC_SCORE_DATA;
import static com.android.xjq.utils.XjqUrlEnum.FOOTBALL_RACE_WITH_CHANNEL_AREA_INFO_BY_DATE;
import static tencent.tls.report.QLog.TAG;

/**
 * Created by ajiao on 2018\2\27 0027.
 */

public class CommonReqHelper extends BasePresenter<MatchScheduleView> implements OnHttpResponseListener {
    private HttpRequestHelper mHttpRequestHelper;
    private List<DynamicBean> mDynamicDataList = null;
    private List<String> mRaceIds = null;
    private long mBasketBallTimestamp = 0;
    private long mFootBallTimeStamp = 0;
    public static final int BEFORE_START_TIME = 10 * 60 * 1000;
    public static final int DATE_NUM = 10;
    public static final String RACE_STATUS_FINISH = "FINISH";
    public static String QUERY_TYPE = "ALL";
    public static int MATCH_TYPE_FOOTBALL = 1;
    public static int MATCH_TYPE_BASKETBALL = 2;
    private List<MatchScheduleBean> mListData = null;
    private int mMatchType = MATCH_TYPE_FOOTBALL;
    private String mRequestDate;

    public CommonReqHelper(MatchScheduleView view) {
        super(view);
        mHttpRequestHelper = new HttpRequestHelper(this, getName());
        mListData = new ArrayList<>();
        mDynamicDataList = new ArrayList<>();
        mRaceIds = new ArrayList<>();
        mRequestDate = TimeUtils.dateToString2(new Date(), TimeUtils.DATEFORMAT);
    }

    public void reqOrder(long channelAreaId) {
        XjqRequestContainer map = new XjqRequestContainer(CHANNEL_AREA_USER_ORDER_CREATE, true);
        map.put("channelAreaId", channelAreaId);
        mHttpRequestHelper.startRequest(map, true);
    }

    public void reqCancelOrder(long channelAreaId) {
        XjqRequestContainer map = new XjqRequestContainer(CHANNEL_AREA_USER_ORDER_CANCEL, true);
        map.put("channelAreaId", channelAreaId);
        mHttpRequestHelper.startRequest(map, true);
    }

    public void requestBetQuery() {
        //commonStatusLayout.showLoading();
        resetTimeStamp();
        XjqRequestContainer map = null;
        if (mMatchType == MATCH_TYPE_BASKETBALL) {
            map = new XjqRequestContainer(BASKETBALL_RACE_WITH_CHANNEL_AREA_INFO_BY_DATE, true);
        } else if (mMatchType == MATCH_TYPE_FOOTBALL) {
            map = new XjqRequestContainer(FOOTBALL_RACE_WITH_CHANNEL_AREA_INFO_BY_DATE, true);
        }
        map.put("date", mRequestDate);
        map.put("matchScheduleQueryType", QUERY_TYPE);
        mHttpRequestHelper.startRequest(map, true);
    }

    public void requestDynamicDataQuery() {
        String raceIdStr = getTransferRaceIds();
        XjqRequestContainer map = null;
        if (mMatchType == MATCH_TYPE_BASKETBALL) {
            map = new XjqRequestContainer(DYNAMIC_SCORE_DATA, true);
            map.setRequestUrl(BT_API_DOMAIN + FT_API_S_URL);
            map.put("timestamp", String.valueOf(mBasketBallTimestamp));
        } else if (mMatchType == MATCH_TYPE_FOOTBALL) {
            map = new XjqRequestContainer(DYNAMIC_DATA_QUERY, true);
            map.setRequestUrl(FT_API_DOMAIN + FT_API_S_URL);
            map.put("timestamp", String.valueOf(mFootBallTimeStamp));
        }
        map.put("raceIds", raceIdStr);
        mHttpRequestHelper.startRequest(map, false);
    }

    public void resetTimeStamp() {
        if (mMatchType == CommonReqHelper.MATCH_TYPE_BASKETBALL) {
            mBasketBallTimestamp = 0;
        } else if (mMatchType == CommonReqHelper.MATCH_TYPE_FOOTBALL) {
            mFootBallTimeStamp = 0;
        }
    }

    private String getTransferRaceIds() {
        LogUtils.i("yjj", "getTransferRaceIds" + mRaceIds.toString());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mRaceIds.size(); i++) {
            if (i == mRaceIds.size() - 1) {
                sb.append(mRaceIds.get(i));
            } else {
                sb.append(mRaceIds.get(i)).append(",");
            }

        }
        return sb.toString();
    }


    public void filterRaceId(List<String> selectedIdList) {
        if (mRaceIds != null && mRaceIds.size() > 0) {
            List<String> temp = new ArrayList<>();
            for (String mRaceId : mRaceIds) {
                if (selectedIdList.contains(mRaceId)) {
                    temp.add(mRaceId);
                }
            }
            mRaceIds.clear();
            mRaceIds.addAll(temp);
        }
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
            case BASKETBALL_RACE_WITH_CHANNEL_AREA_INFO_BY_DATE:
                responseSuccessBetQuery(jsonObject);
                break;
            case DYNAMIC_SCORE_DATA:
                responseGetDynamicScore(jsonObject, MATCH_TYPE_BASKETBALL);
                break;
            case FOOTBALL_RACE_WITH_CHANNEL_AREA_INFO_BY_DATE:
                responseSuccessBetQuery(jsonObject);
                break;
            case DYNAMIC_DATA_QUERY:
                responseGetDynamicScore(jsonObject, MATCH_TYPE_FOOTBALL);
                break;
            case CHANNEL_AREA_USER_ORDER_CANCEL:
                responseCancelOrderSuccess();
                break;
            case CHANNEL_AREA_USER_ORDER_CREATE:
                responseOrderSuccess();
                break;
            default:
                break;
        }
    }

    private void responseOrderSuccess() {
        if (mvpView == null) return;
        mvpView.orderSucces();
    }

    private void responseCancelOrderSuccess() {
        mvpView.cancelOrderSuccess();
    }

    private void responseGetDynamicScore(JSONObject jsonObject, int tag) {
        mDynamicDataList.clear();
        Date date = new Date(System.currentTimeMillis());
        String curStr = TimeUtils.dateToString(date, TimeUtils.LONG_DATEFORMAT);
        try {
            curStr = jsonObject.optString("nowDate");
            JSONArray array = (JSONArray) jsonObject.get("dynamicDataList");
            if (array != null && array.length() > 0) {
                mDynamicDataList = new Gson().fromJson(array.toString(), new TypeToken<List<DynamicBean>>() {
                }.getType());
            }
            if (tag == MATCH_TYPE_BASKETBALL) {
                if (jsonObject.has("maxTimeStamp")) {
                    mBasketBallTimestamp = jsonObject.getLong("maxTimeStamp");
                }
            } else if (tag == MATCH_TYPE_FOOTBALL) {
                if (jsonObject.has("timestamp")) {
                    mFootBallTimeStamp = jsonObject.getLong("timestamp");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置足球服务器返回的时间
        if (tag == MATCH_TYPE_FOOTBALL) {
            setFootBallServerTime(curStr);
        }
        if (mDynamicDataList != null && mDynamicDataList.size() > 0) {
            if (tag == MATCH_TYPE_BASKETBALL) {
                transferBasketBallData();
            } else if (tag == MATCH_TYPE_FOOTBALL) {
                transferFootBallData();
            }
        }
        if (mvpView == null) return;
        mvpView.refreshList(mListData);
    }

    private void setFootBallServerTime(String serverTime) {
        if (mListData != null && mListData.size() > 0) {
            for (int i = 0; i < mListData.size(); i++) {
                MatchScheduleBean bean = mListData.get(i);
                bean.setServerCurrentTime(serverTime);
            }
        }
    }

    public boolean isAllMatchFinished() {
        boolean isAllFinished = true;
        for (int i = 0; i < mListData.size(); i++) {
            if (!mListData.get(i).getRaceStatus().getName().equals(RACE_STATUS_FINISH)) {
                isAllFinished = false;
            }
        }
        return isAllFinished;
    }

    private void transferFootBallData() {
        for (int i = 0; i < mDynamicDataList.size(); i++) {
            DynamicBean dynamicBean = mDynamicDataList.get(i);
            int id = dynamicBean.getId();
            String seasonMsg = "";
            String seasonStatusName = "";
            if (dynamicBean.getS() != null) {
                seasonMsg = dynamicBean.getS().getMessage();
                seasonStatusName = dynamicBean.getS().getName();
            }
            //如果已经结束的赛事，就从mRaceIds里移除
            if (seasonStatusName.equals(RACE_STATUS_FINISH)) {
                String seasonId = String.valueOf(id);
                if (mRaceIds.contains(seasonId)) {
                    mRaceIds.remove(seasonId);
                }
            }
            for (int j = 0; j < mListData.size(); j++) {
                MatchScheduleBean bean = mListData.get(j);
                if (bean.getInnerRaceId() == id) {
                    bean.getRaceStatus().setName(seasonStatusName);
                    bean.getRaceStatus().setMessage(seasonMsg);
                    bean.setFullGuestScore(dynamicBean.getGs());
                    bean.setFullHomeScore(dynamicBean.getHs());
                    bean.setHr(dynamicBean.getHr());
                    bean.setGr(dynamicBean.getGr());
                    bean.setHy(dynamicBean.getHy());
                    bean.setGy(dynamicBean.getGy());
                    bean.setFst(dynamicBean.getFst());
                    bean.setSst(dynamicBean.getSst());
                    bean.setHalfHomeScore(dynamicBean.getHhs());
                    bean.setHalfGuestScore(dynamicBean.getHgs());
                }
            }

        }

    }

    private void transferBasketBallData() {
        for (int i = 0; i < mDynamicDataList.size(); i++) {
            DynamicBean dynamicBean = mDynamicDataList.get(i);
            int id = dynamicBean.getId();
            String seasonMsg = "";
            String seasonStatusName = "";
            if (dynamicBean.getStatus() != null) {
                seasonMsg = dynamicBean.getStatus().getMessage();
                seasonStatusName = dynamicBean.getStatus().getName();
            }
            //如果已经结束的赛事，就从mRaceIds里移除
            if (seasonStatusName.equals(RACE_STATUS_FINISH)) {
                String seasonId = String.valueOf(id);
                if (mRaceIds.contains(seasonId)) {
                    mRaceIds.remove(seasonId);
                }
            }
            for (int j = 0; j < mListData.size(); j++) {
                MatchScheduleBean bean = mListData.get(j);
                if (bean.getInnerRaceId() == id) {
                    bean.getRaceStatus().setName(seasonStatusName);
                    bean.getRaceStatus().setMessage(seasonMsg);
                    bean.setFullGuestScore(dynamicBean.getGs());
                    bean.setFullHomeScore(dynamicBean.getHs());
                    bean.setLeaveTime(dynamicBean.getRt());
                }
            }

        }
    }

    private void addRaceId(String nowDate) {
        //Date currentDate = new Date();
        //String curTime = TimeUtils.dateToString(currentDate, TimeUtils.LONG_DATEFORMAT);
        mRaceIds.clear();
        for (int i = 0; i < mListData.size(); i++) {
            MatchScheduleBean matchSchedualBean = mListData.get(i);
            long gmtSeconds = TimeUtils.timeStrToLong(matchSchedualBean.getGmtStart()) - BEFORE_START_TIME;
            Date date = new Date(gmtSeconds);
            String gmtTime = TimeUtils.dateToString(date, TimeUtils.LONG_DATEFORMAT);
            boolean isStartAtOnce = TimeUtils.isBeforeNow(nowDate, gmtTime);
            String raceStatus = matchSchedualBean.getRaceStatus().getName();
            boolean b = raceStatus.equals(RACE_STATUS_FINISH) || raceStatus.equals("DELAY")
                    || raceStatus.equals("CUT") || raceStatus.equals("BREAK_OFF")
                    || raceStatus.equals("DEC") || raceStatus.equals("CANCEL")
                    || raceStatus.equals("UNKNOWN") || raceStatus.equals("POSTPONE");
            if (!b) { //不用判断是否与当前时间相差10分钟
                mRaceIds.add(String.valueOf(mListData.get(i).getInnerRaceId()));
            }
        }
        LogUtils.i("yjj", "addRaceId size = " + mRaceIds.toString());
    }

    private void responseSuccessBetQuery(JSONObject jsonObject) {
        try {
            if (mvpView == null) return;
            mvpView.setTimeTabLayout(jsonObject.optString("nowDate"));
            mListData.clear();
            MatchScheduleEntity matchScheduleEntity = new Gson().fromJson(jsonObject.toString(), MatchScheduleEntity.class);
            if (MATCH_TYPE_FOOTBALL == mMatchType) {
                mListData.addAll(matchScheduleEntity.getFootballRaceClientSimpleList());
            } else {
                mListData.addAll(matchScheduleEntity.getBasketballRaceClientSimpleList());
            }
            addIconUrls();
            addChannelPrograms(matchScheduleEntity);
            mvpView.setReqBetQuery(mListData, matchScheduleEntity);
            mvpView.refreshList(mListData);
            addRaceId(jsonObject.optString("nowDate"));
            mvpView.startActTimer();
        } catch (Exception e) {
            mvpView.showEmptyMsg();
            Log.e(TAG, e.toString());
        }
    }

    private void addIconUrls() {
        for (int i = 0; i < mListData.size(); i++) {
            String guestIconUrl = "";
            String homeIconUrl = "";
            if (mMatchType == MATCH_TYPE_FOOTBALL) {
                guestIconUrl = TeamImageUrlUtils.getFTGuestLogoUrl(mListData.get(i).getInnerGuestTeamId());
                homeIconUrl = TeamImageUrlUtils.getFTHomeLogoUrl(mListData.get(i).getInnerHomeTeamId());
            } else {
                guestIconUrl = TeamImageUrlUtils.getBTGuestLogoUrl(mListData.get(i).getInnerGuestTeamId());
                homeIconUrl = TeamImageUrlUtils.getBTHomeLogoUrl(mListData.get(i).getInnerHomeTeamId());
            }
            mListData.get(i).setGuestIconUrl(guestIconUrl);
            mListData.get(i).setHomeIconUrl(homeIconUrl);
        }
    }

    private void addChannelPrograms(MatchScheduleEntity matchScheduleEntity) {
        HashMap<String, List<ChannelAreaBean>> raceIdAndChannelAreaMap = matchScheduleEntity.getRaceIdAndChannelAreaMap();
        for (Map.Entry<String, List<ChannelAreaBean>> entry : raceIdAndChannelAreaMap.entrySet()) {
            for (int j = 0; j < mListData.size(); j++) {
                if (entry.getKey().equals(mListData.get(j).getId())) {
                    mListData.get(j).setChannelAreaBeanList(entry.getValue());
                }
            }
        }
    }


    private void responseCancelOderSuccess(JSONObject jsonObject) {

    }

    private void responseCreateOrderSuccess(JSONObject jsonObject) {

    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        if (mvpView == null) return;
        mvpView.orderFailed(jsonObject);
        /*try {
            //clearListData();
            ((BaseActivity) activity).operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }

    public void setMatchType(int matchType) {
        mMatchType = matchType;
    }

    public void setReqDate(String time) {
        mRequestDate = time;
    }

    public void setFilteredData(List<MatchScheduleBean> data) {
        mListData = data;
    }
}
