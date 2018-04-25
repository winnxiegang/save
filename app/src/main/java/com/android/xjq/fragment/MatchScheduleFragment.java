package com.android.xjq.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.view.CommonStatusLayout;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.adapter.main.InfoPagerAdapter;
import com.android.xjq.adapter.main.MatchScheduleAdapter;
import com.android.xjq.bean.DynamicBean;
import com.android.xjq.bean.MatchScheduleBean;
import com.android.xjq.bean.matchLive.MatchScheduleEntity;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.matchLive.FilterMatchHelper;
import com.android.xjq.view.indicate.TimeTabLayout2;
import com.android.xjq.view.indicate.ViewPagerIndicator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.banana.commlib.http.AppParam.BT_API_DOMAIN;
import static com.android.banana.commlib.http.AppParam.FT_API_DOMAIN;
import static com.android.banana.commlib.http.AppParam.FT_API_S_URL;
import static com.android.xjq.utils.XjqUrlEnum.BASKETBALL_RACE_JOIN_BET_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.DYNAMIC_DATA_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.DYNAMIC_SCORE_DATA;
import static com.android.xjq.utils.XjqUrlEnum.FOOTBALL_RACE_JOIN_SET_QUERY;
import static com.android.xjq.utils.matchLive.FilterMatchHelper.OPERATE_MATCH_LIVE;

public class MatchScheduleFragment extends BaseFragment implements OnHttpResponseListener {
    public static final String RACE_STATUS_FINISH = "FINISH";
    public static final int TERMINAL_TIME = 5 * 1000;
    public static final int BEFORE_START_TIME = 10 * 60 * 1000;
    //全部、进行中、已参与
    public static final String QUERY_TYPE_ALL = "ALL";
    public static final String QUERY_TYPE_UNDERWAY_GAME = "UNDERWAY_GAME";
    public static final String QUERY_TYPE_INVOLVED_GAME = "INVOLVED_GAME";
    public static final int MSG_REPEAT_REQ = 0;
    private String mQueryType = QUERY_TYPE_ALL;
    private int mIntQueryType = 0;
    private long mBasketBallTimestamp = 0;
    private long mFootBallTimeStamp = 0;
    private int MATCH_TYPE_FOOTBALL = 1;
    private int MATCH_TYPE_BASKETBALL = 2;
    private int mMatchType = MATCH_TYPE_FOOTBALL;
    @BindView(R.id.footballRb)
    RadioButton footballRb;
    @BindView(R.id.basketballRb)
    RadioButton basketballRb;
    @BindView(R.id.ballFilterRg)
    RadioGroup ballFilterRg;
    @BindView(R.id.filterBtn)
    ImageButton filterBtn;
    @BindView(R.id.vpi)
    ViewPagerIndicator vpi;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.timeLayout)
    TimeTabLayout2 timeLayout;
    @BindView(R.id.emptyLayout)
    LinearLayout emptyLinearLay;
    @BindView(R.id.statusLayout)
    CommonStatusLayout commonStatusLayout;
    private HttpRequestHelper mHttpRequestHelper;
    private String[] mTabs = {"全部", "进行中", "已参与"};
    private String mRequestDate = "";
    private TimerTask mTimeTask;
    private Timer mTimer;
    private List<List<MatchScheduleBean>> mVpListData = null;
    private MatchScheduleAdapter[] mVpAdapters = new MatchScheduleAdapter[3];
    private List<String> mRaceIds = null;
    private List<DynamicBean> mDynamicDataList = null;
    private Handler mHandler;
    private HashMap<String, String> allMatchMap;
    private HashMap<String, String> matchFiveMap;
    private List<String> mSelectedIdList;
    private FilterMatchHelper mFilterMatchHelper;
    private List<MatchScheduleBean> mAllMatchList;

    @Override
    protected void initData() {
    }

    @OnClick(R.id.filterBtn)
    public void filterBtn() {
        if (mMatchType == MATCH_TYPE_FOOTBALL) {
            //mFilterMatchHelper.startFilter(false, allMatchMap, matchFiveMap, mSelectedIdList);
        } else if (mMatchType == MATCH_TYPE_BASKETBALL) {
            //mFilterMatchHelper.startFilter(true, allMatchMap, matchFiveMap, mSelectedIdList);
        }
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_schedule, null);
        ButterKnife.bind(this, view);
        initViewData();
        initViews();
        return view;
    }

    private void initViews() {
        vpi.setTabItemTitles(Arrays.asList(mTabs));
        //设置关联的ViewPager
        vpi.setViewPager(viewPager, 0);
        ArrayList<View> mViewList = new ArrayList<>();
        for (int i = 0; i < mTabs.length; i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.content_match_schedule_listview, null, false);
            ListView listView = (ListView) view.findViewById(R.id.macth_schedule_list);
            listView.setDivider(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.light_text_color)));
            listView.setDividerHeight(1);
            MatchScheduleAdapter adapter = new MatchScheduleAdapter(getActivity());
            List<MatchScheduleBean> data = new ArrayList<>();
            //setListViewEmptyView(listView);
            adapter.setData(data);
            adapter.setType(mMatchType);
            listView.setAdapter(adapter);
            mViewList.add(view);
            mVpAdapters[i] = adapter;
            mVpListData.add(data);
        }
        InfoPagerAdapter adapter = new InfoPagerAdapter(mViewList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mQueryType = QUERY_TYPE_ALL;
                        break;
                    case 1:
                        mQueryType = QUERY_TYPE_UNDERWAY_GAME;
                        break;
                    case 2:
                        mQueryType = QUERY_TYPE_INVOLVED_GAME;
                        break;
                }
                mIntQueryType = position;
                mVpAdapters[mIntQueryType].setType(mMatchType);
                resetTimeStamp();
                requestBetQuery();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        timeLayout.setIonCheckTimeListener(new TimeTabLayout2.IonCheckTime() {
            @Override
            public void onCheckTime(String time) {
                mRequestDate = time;
                resetTimeStamp();
                requestBetQuery();
            }
        });
        ballFilterRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
                if (id == R.id.footballRb) {
                    mMatchType = MATCH_TYPE_FOOTBALL;
                } else if (id == R.id.basketballRb) {
                    mMatchType = MATCH_TYPE_BASKETBALL;
                }
                mVpAdapters[mIntQueryType].setType(mMatchType);
                resetTimeStamp();
                requestBetQuery();
            }
        });
    }

    private void resetTimeStamp() {
        if (mMatchType == MATCH_TYPE_BASKETBALL) {
            mBasketBallTimestamp = 0;
        } else if (mMatchType == MATCH_TYPE_FOOTBALL) {
            mFootBallTimeStamp = 0;
        }
    }

    /**
     * 设置Listview的EmptyView
     */
    private void setListViewEmptyView(ListView listView) {
        View emptyView = View.inflate(getContext(), R.layout.empty_view_temp_no_match, null);
        ViewGroup parentView = (ViewGroup) listView.getParent();
        parentView.addView(emptyView, 1);
        listView.setEmptyView(emptyView);
    }

    /**
     * 比赛筛选回调
     */
    private View.OnClickListener filteredClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSelectedIdList = (List<String>) v.getTag();
            if (mSelectedIdList != null) {
                List<MatchScheduleBean> filterList = new ArrayList<>();
                if (mAllMatchList != null && mAllMatchList.size() > 0) {
                    for (MatchScheduleBean matchScheduleBean : mAllMatchList) {
                        if (mSelectedIdList.contains(String.valueOf(matchScheduleBean.getInnerMatchId()))) {
                            filterList.add(matchScheduleBean);
                        }
                    }
                    mVpListData.get(mIntQueryType).clear();
                    mVpListData.get(mIntQueryType).addAll(filterList);
                    mVpAdapters[mIntQueryType].setData(mVpListData.get(mIntQueryType));
                    mVpAdapters[mIntQueryType].notifyDataSetChanged();
                    filterRaceId(mSelectedIdList);
                }
            }
        }
    };

    private void filterRaceId(List<String> selectedIdList) {
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


    private void initViewData() {
        mVpListData = new ArrayList<>();
        mFilterMatchHelper = new FilterMatchHelper(activity, OPERATE_MATCH_LIVE, filteredClickListener);
        mRequestDate = TimeUtils.dateToString2(new Date(), TimeUtils.DATEFORMAT);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_REPEAT_REQ:
                        requestDynamicDataQuery();
                        break;
                }
            }
        };
        mRaceIds = new ArrayList<>();
        mDynamicDataList = new ArrayList<>();
        mSelectedIdList = new ArrayList<>();
        mHttpRequestHelper = new HttpRequestHelper(getActivity(), this);
        requestBetQuery();
    }

    private void startTimer() {
        stopTimer();
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimeTask == null) {
            mTimeTask = new TimerTask() {
                @Override
                public void run() {
                    if (mVpListData.get(mIntQueryType).size() > 0 && isAllMatchFinished()) {
                        stopTimer();
                    } else if (mVpListData.get(mIntQueryType).size() == 0) {
                        stopTimer();
                    } else {
                        Message msg = mHandler.obtainMessage(MSG_REPEAT_REQ);
                        msg.sendToTarget();
                    }
                }
            };
        }

        if (mTimer != null && mTimer != null) {
            mTimer.schedule(mTimeTask, 100, TERMINAL_TIME);
        }

    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        if (mTimeTask != null) {
            mTimeTask.cancel();
            mTimeTask = null;
        }
    }


    private void requestBetQuery() {
        commonStatusLayout.showLoading();
        XjqRequestContainer map = null;
        if (mMatchType == MATCH_TYPE_BASKETBALL) {
            map = new XjqRequestContainer(BASKETBALL_RACE_JOIN_BET_QUERY, true);
        } else if (mMatchType == MATCH_TYPE_FOOTBALL) {
            map = new XjqRequestContainer(FOOTBALL_RACE_JOIN_SET_QUERY, true);
        }
        map.put("date", mRequestDate);
        map.put("matchScheduleQueryType", mQueryType);
        mHttpRequestHelper.startRequest(map, false);
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

    private String getTransferRaceIds() {
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

    public boolean isAllMatchFinished() {
        boolean isAllFinished = true;
        for (int i = 0; i < mVpListData.get(mIntQueryType).size(); i++) {
            if (!mVpListData.get(mIntQueryType).get(i).getRaceStatus().getName().equals(RACE_STATUS_FINISH)) {
                isAllFinished = false;
            }
        }
        return isAllFinished;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        commonStatusLayout.hideStatusView();
        switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
            case BASKETBALL_RACE_JOIN_BET_QUERY:
                responseSuccessBetQuery(jsonObject);
                break;
            case DYNAMIC_SCORE_DATA:
                responseGetDynamicScore(jsonObject, MATCH_TYPE_BASKETBALL);
                break;
            case FOOTBALL_RACE_JOIN_SET_QUERY:
                responseSuccessBetQuery(jsonObject);
                break;
            case DYNAMIC_DATA_QUERY:
                responseGetDynamicScore(jsonObject, MATCH_TYPE_FOOTBALL);
                break;
        }
    }

    private void responseGetDynamicScore(JSONObject jsonObject, int tag) {
        Log.e("onAttachedToWindow: 1", "onAttachedToWindow1");
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
        refreshListData();
    }

    private void setFootBallServerTime(String serverTime) {
        if (mVpListData.get(mIntQueryType) != null && mVpListData.get(mIntQueryType).size() > 0) {
            for (int i = 0; i < mVpListData.get(mIntQueryType).size(); i++) {
                MatchScheduleBean bean = mVpListData.get(mIntQueryType).get(i);
                bean.setServerCurrentTime(serverTime);
            }
        }
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
            for (int j = 0; j < mVpListData.get(mIntQueryType).size(); j++) {
                MatchScheduleBean bean = mVpListData.get(mIntQueryType).get(j);
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
            for (int j = 0; j < mVpListData.get(mIntQueryType).size(); j++) {
                MatchScheduleBean bean = mVpListData.get(mIntQueryType).get(j);
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

    private void responseSuccessBetQuery(JSONObject jsonObject) {
        try {
            timeLayout.setNowData(jsonObject.optString("nowDate"));
            MatchScheduleEntity matchScheduleEntity = new Gson().fromJson(jsonObject.toString(), MatchScheduleEntity.class);
            mVpListData.get(mIntQueryType).clear();
            if (MATCH_TYPE_FOOTBALL == mMatchType) {
                mVpListData.get(mIntQueryType).addAll(matchScheduleEntity.getFootballRaceClientSimpleList());
                mAllMatchList = matchScheduleEntity.getFootballRaceClientSimpleList();
            } else {
                mVpListData.get(mIntQueryType).addAll(matchScheduleEntity.getBasketballRaceClientSimpleList());
                mAllMatchList = matchScheduleEntity.getBasketballRaceClientSimpleList();
            }
            emptyLinearLay.setVisibility(mAllMatchList.size() > 0 ? View.GONE : View.VISIBLE);
            //默认选中全部比赛
            for (MatchScheduleBean matchScheduleBean : mVpListData.get(mIntQueryType)) {
                mSelectedIdList.add(matchScheduleBean.getInnerMatchId() + "");
            }

            //allMatchMap = matchScheduleEntity.getInnerMatchIdAndMatchNameMap();
            matchFiveMap = matchScheduleEntity.getMatchGroupAndInnerMatchIdsMap();
            refreshListData();
            addRaceId();
            startTimer();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void refreshListData() {
        mVpAdapters[mIntQueryType].setData(mVpListData.get(mIntQueryType));
        mVpAdapters[mIntQueryType].notifyDataSetChanged();
    }

    private void addRaceId() {
        Date currentDate = new Date();
        String curTime = TimeUtils.dateToString(currentDate, TimeUtils.LONG_DATEFORMAT);
        mRaceIds.clear();
        for (int i = 0; i < mVpListData.get(mIntQueryType).size(); i++) {
            MatchScheduleBean matchSchedualBean = mVpListData.get(mIntQueryType).get(i);
            long gmtSeconds = TimeUtils.timeStrToLong(matchSchedualBean.getGmtStart()) - BEFORE_START_TIME;
            Date date = new Date(gmtSeconds);
            String gmtTime = TimeUtils.dateToString(date, TimeUtils.LONG_DATEFORMAT);
            boolean isStartAtOnce = TimeUtils.isBeforeNow(curTime, gmtTime);
            String raceStatus = matchSchedualBean.getRaceStatus().getName();
            boolean b = raceStatus.equals(RACE_STATUS_FINISH) || raceStatus.equals("DELAY")
                    || raceStatus.equals("CUT") || raceStatus.equals("BREAK_OFF")
                    || raceStatus.equals("DEC") || raceStatus.equals("CANCEL")
                    || raceStatus.equals("UNKNOWN") || raceStatus.equals("POSTPONE");
            if (!b) { //不用判断是否与当前时间相差10分钟
                mRaceIds.add(String.valueOf(mVpListData.get(mIntQueryType).get(i).getInnerRaceId()));
            }
        }
    }

    private void clearListData() {
        mVpListData.get(mIntQueryType).clear();
        mVpAdapters[mIntQueryType].notifyDataSetChanged();
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            //clearListData();
            ((BaseActivity) activity).operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void executorFailed(RequestContainer requestContainer) {
    }

    @Override
    public void executorFinish() {
    }


}
