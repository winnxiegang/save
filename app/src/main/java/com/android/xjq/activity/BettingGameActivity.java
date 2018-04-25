package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.bean.GameChildBean;
import com.android.xjq.bean.GameParentBean;
import com.android.xjq.bean.MyGameChildBean;
import com.android.xjq.bean.myzhuwei.BasketballRacesBean;
import com.android.xjq.bean.myzhuwei.FootballRacesBean;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.utils.matchLive.FilterMatchHelper;
import com.android.xjq.view.FooterTabView;
import com.android.xjq.view.expandablelistview.ExpandableListAdapter;
import com.android.xjq.view.indicate.TimeTabLayout2;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.xjq.activity.myzhuwei.widget.ThemeExpandableAdapter.getGmtFormatedTime;
import static com.android.xjq.utils.XjqUrlEnum.BASKETBALL_RACE_BETTING_PAGE_INFO_BY_DATE;
import static com.android.xjq.utils.XjqUrlEnum.FOOTBALL_RACE_BETTING_PAGE_INFO_BY_DATE;
import static com.android.xjq.utils.XjqUrlEnum.GAME_BOARD_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.PURCHASE_BASKETBALL_NORMAL;
import static com.android.xjq.utils.XjqUrlEnum.PURCHASE_FOOTBALL_NORMAL;
import static com.android.xjq.utils.matchLive.FilterMatchHelper.OPERATE_MATCH_LIVE;

/**
 * Created by ajiao on 2017/11/1 0001.
 */

public class BettingGameActivity extends BaseActivity implements OnHttpResponseListener {
    //全部、进行中、已参与
    public static final int TAG_HOST = 0;
    public static final int TAG_GUEST = 1;
    public static final int TAG_PARENT = 2;
    public static final int TAG_CHILD = 3;
    public static final String QUERY_TYPE_ALL = "ALL";
    public static final String QUERY_TYPE_UNDERWAY_GAME = "UNDERWAY_GAME";
    public static final String QUERY_TYPE_INVOLVED_GAME = "INVOLVED_GAME";
    private static final String FOOTBALL = "FOOTBALL";
    private static final String BASKETBALL = "BASKETBALL";
    @BindView(R.id.timeLayout2)
    TimeTabLayout2 timeTabLayout;
    @BindView(R.id.footer_tab_view)
    FooterTabView footerTabView;
    @BindView(R.id.all_txt)
    TextView allTxt;
    @BindView(R.id.doing_txt)
    TextView onDoingTxt;
    @BindView(R.id.already_txt)
    TextView alreadyTxt;
    @BindView(R.id.betting_game_expandable_list)
    ExpandableListView mListView;
    @BindView(R.id.list_content_lay)
    RelativeLayout listContentLay;
    @BindView(R.id.title_lay)
    LinearLayout titleLay;
    @BindView(R.id.empty_lay)
    LinearLayout emptyLinearLay;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private String[] tabs = {"全部", "进行中", "已参与"};
    private HttpRequestHelper httpRequestHelper;
    private String mQueryType;

    private int mTimestamp = 0;
    private String mMatchType;
    private String mRequestDate = "";

    private List<GameParentBean> mGroupData = null;
    private List<MyGameChildBean> mChildData = null;
    private ExpandableListAdapter mAdapter = null;
    private int mGroupExpandPos = 0;
    private int mChildCheerPos = 0;
    private int mGroupCheerPos = 0;
    private int mCheerType = TAG_HOST;
    private int mGroupType = TAG_PARENT;
    private FilterMatchHelper filterMatchHelper;
    private HashMap<String, Long> allMatchMap;
    private HashMap<String, String> matchFiveMap;
    private List<String> mSelectedNamesList;
    private View mListFooterView;
    private List<GameParentBean> mAllMatchList;

    @OnClick(R.id.close_img)
    public void close() {
        this.finish();
    }

    @BindView(R.id.filter_img)
    ImageView filterImg;

    @OnClick(R.id.filter_img)
    public void filter() {
        if (FOOTBALL == mMatchType) {
            filterMatchHelper.startFilter(false, allMatchMap, matchFiveMap, mSelectedNamesList);
        } else {
            filterMatchHelper.startFilter(true, allMatchMap, matchFiveMap, mSelectedNamesList);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting_new_game);
        ButterKnife.bind(this);
        setTitleBar(true, null);
        initData();
        initView();
        requestFootBallData();
    }

    private void initData() {
        httpRequestHelper = new HttpRequestHelper(this, this);
        mAdapter = new ExpandableListAdapter(BettingGameActivity.this);
        mGroupData = new ArrayList<>();
        mChildData = new ArrayList<>();
        mAllMatchList = new ArrayList<>();
        mSelectedNamesList = new ArrayList<>();
        mQueryType = QUERY_TYPE_ALL;
        mMatchType = FOOTBALL;
        filterMatchHelper = new FilterMatchHelper(this, OPERATE_MATCH_LIVE, filterMatchListener);
    }

    private void requestBascketBallData() {
        //statusLayout.showLoading();
        mProgressBar.setVisibility(View.VISIBLE);
        emptyLinearLay.setVisibility(View.GONE);
        XjqRequestContainer map = new XjqRequestContainer(BASKETBALL_RACE_BETTING_PAGE_INFO_BY_DATE, true);
        map.put("date", mRequestDate);
        map.put("matchScheduleQueryType", mQueryType);
        map.setGenericClaz(BasketballRacesBean.class);
        httpRequestHelper.startRequest(map, false);
    }

    private void requestFootBallData() {
        //statusLayout.showLoading();
        mProgressBar.setVisibility(View.VISIBLE);
        emptyLinearLay.setVisibility(View.GONE);
        XjqRequestContainer map = new XjqRequestContainer(FOOTBALL_RACE_BETTING_PAGE_INFO_BY_DATE, true);
        map.put("date", mRequestDate);
        map.put("matchScheduleQueryType", mQueryType);
        map.setGenericClaz(FootballRacesBean.class);
        httpRequestHelper.startRequest(map, false);
    }

    private void initView() {
        initListeners();
        initExpandableListView();
        setUIBgType();
    }

    private void setUIBgType() {
        if (mMatchType == FOOTBALL) {
            allTxt.setTextColor(getResources().getColorStateList(R.color.color_selector_green_title_txt));
            onDoingTxt.setTextColor(getResources().getColorStateList(R.color.color_selector_green_title_txt));
            alreadyTxt.setTextColor(getResources().getColorStateList(R.color.color_selector_green_title_txt));
            listContentLay.setBackground(getResources().getDrawable(R.drawable.basketball_bg));
            titleLay.setBackground(getResources().getDrawable(R.drawable.basket_ball_bg));
            filterImg.setVisibility(View.VISIBLE);
        } else if (mMatchType == BASKETBALL) {
            allTxt.setTextColor(getResources().getColorStateList(R.color.color_selector_yellow_title_txt));
            onDoingTxt.setTextColor(getResources().getColorStateList(R.color.color_selector_yellow_title_txt));
            alreadyTxt.setTextColor(getResources().getColorStateList(R.color.color_selector_yellow_title_txt));
            listContentLay.setBackground(getResources().getDrawable(R.drawable.football_bg));
            titleLay.setBackground(getResources().getDrawable(R.drawable.football_title));
            filterImg.setVisibility(View.GONE);
        }
        int resBgResId = mMatchType == FOOTBALL ? R.drawable.shape_light_green_solid_radius_bg : R.drawable.shape_light_yellow_solid_radius_bg;
        switch (mQueryType) {
            case QUERY_TYPE_ALL:
                allTxt.setBackground(getResources().getDrawable(resBgResId));
                break;
            case QUERY_TYPE_UNDERWAY_GAME:
                onDoingTxt.setBackground(getResources().getDrawable(resBgResId));
                break;
            case QUERY_TYPE_INVOLVED_GAME:
                alreadyTxt.setBackground(getResources().getDrawable(resBgResId));
                break;
        }
    }

    private void initExpandableListView() {
        mAdapter.setGroupData(mGroupData);
        mAdapter.setType(mMatchType);
        mAdapter.setOnExpandChildItem(new ExpandableListAdapter.IOnExpandChildItem() {
            @Override
            public void onExpandChildItem(int groupPosition) {
                //清空缓存防止请求失败复用之前的数据
                mChildData.clear();
                expandOnlyOne(groupPosition);
                mGroupExpandPos = groupPosition;
                getChildItemData(mGroupData.get(groupPosition).getId(), mGroupData.get(groupPosition).getRaceStatus());
            }
        });
        mAdapter.setOnCollapseChildItem(new ExpandableListAdapter.IOnCollapseChildItem() {
            @Override
            public void onCollapseChildItem(int groupPosition) {
                mListView.collapseGroup(groupPosition);
                mGroupData.get(groupPosition).setExpanded(false);
                mAdapter.notifyDataSetChanged();
            }
        });
        mListFooterView = LayoutInflater.from(BettingGameActivity.this).inflate(R.layout.footer_expandable_listview, null, false);
        mListView.addFooterView(mListFooterView);
        mListView.setGroupIndicator(null);
        mListView.setAdapter(mAdapter);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        mAdapter.setOnGroupSendGiftClick(new ExpandableListAdapter.IOnGroupSendGiftClick() {
            @Override
            public void onGroupHostGift(int pos, String payTypeNo, String boardId, String keyAndOption, String playType) {
                mGroupType = TAG_PARENT;
                mGroupCheerPos = pos;
                mCheerType = TAG_HOST;
                reqSendGift(mGroupData.get(pos).getHomeRate(), payTypeNo, boardId, keyAndOption, playType);
            }

            @Override
            public void onGroupGuestGift(int pos, String payTypeNo, String boardId, String keyAndOption, String playType) {
                mGroupType = TAG_PARENT;
                mGroupCheerPos = pos;
                mCheerType = TAG_GUEST;
                reqSendGift(mGroupData.get(pos).getGuestRate(), payTypeNo, boardId, keyAndOption, playType);
            }
        });

        mAdapter.setOnChildSendGiftClick(new ExpandableListAdapter.IOnChildSendGiftClick() {
            @Override
            public void onChildHostGift(int pos, String payTypeNo, String boardId, String keyAndOption, String playType) {
                mGroupType = TAG_CHILD;
                mChildCheerPos = pos;
                mCheerType = TAG_HOST;
                reqSendGift(mChildData.get(pos).getHomeRate(), payTypeNo, boardId, keyAndOption, playType);
            }

            @Override
            public void onChildGuestGift(int pos, String payTypeNo, String boardId, String keyAndOption, String playType) {
                mGroupType = TAG_CHILD;
                mChildCheerPos = pos;
                mCheerType = TAG_GUEST;
                reqSendGift(mChildData.get(pos).getGuestRate(), payTypeNo, boardId, keyAndOption, playType);
            }
        });

    }

    private void reqSendGift(int rate, String payTypeNo, String boardId, String keyAndOption, String playType) {
        XjqRequestContainer map = null;
        if (FOOTBALL == mMatchType) {
            map = new XjqRequestContainer(PURCHASE_FOOTBALL_NORMAL, true);
        } else {
            map = new XjqRequestContainer(PURCHASE_BASKETBALL_NORMAL, true);
        }
        map.put("frontType", "M_CLIENT");
        map.put("createrType", "USER_CREATE");
        map.put("payCoinType", "GOLD_COIN");
        map.put("payType", "GIFT");
        map.put("payTypeNo", payTypeNo);
        map.put("boardId", boardId);
        map.put("keyAndOptions", keyAndOption);
        map.put("playType", "CHEER");
        map.put("playSubType", playType);
        map.put("multiple", rate);
        httpRequestHelper.startRequest(map, false);
    }

    private void initListeners() {
        timeTabLayout.setIonCheckTimeListener(new TimeTabLayout2.IonCheckTime() {
            @Override
            public void onCheckTime(String time) {
                mRequestDate = time;
                if (FOOTBALL.equals(mMatchType)) {
                    requestFootBallData();
                } else if (BASKETBALL.equals(mMatchType)) {
                    requestBascketBallData();
                }
            }
        });

        footerTabView.setOnTabClickListener(new FooterTabView.onTabClick() {
            @Override
            public void onLeftTabClick() {
                mMatchType = FOOTBALL;
                setUIBgType();
                requestFootBallData();
                mAdapter.setType(mMatchType);

            }

            @Override
            public void onRightTabClick() {
                mMatchType = BASKETBALL;
                setUIBgType();
                requestBascketBallData();
                mAdapter.setType(mMatchType);
            }
        });
        setSelectAllUI();
        allTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectAllUI();
                mQueryType = QUERY_TYPE_ALL;
                reqListData();
            }
        });
        onDoingTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectOnUI();
                mQueryType = QUERY_TYPE_UNDERWAY_GAME;
                reqListData();
            }
        });
        alreadyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectAlReadyUI();
                mQueryType = QUERY_TYPE_INVOLVED_GAME;
                reqListData();
            }
        });
    }


    private void setSelectAlReadyUI() {
        alreadyTxt.setSelected(true);
        int resBgResId = mMatchType == FOOTBALL ? R.drawable.shape_light_green_solid_radius_bg : R.drawable.shape_light_yellow_solid_radius_bg;
        alreadyTxt.setBackground(getResources().getDrawable(resBgResId));
        onDoingTxt.setBackground(null);
        onDoingTxt.setSelected(false);
        allTxt.setBackground(null);
        allTxt.setSelected(false);
    }

    private void setSelectOnUI() {
        onDoingTxt.setSelected(true);
        int resBgResId = mMatchType == FOOTBALL ? R.drawable.shape_light_green_solid_radius_bg : R.drawable.shape_light_yellow_solid_radius_bg;
        onDoingTxt.setBackground(getResources().getDrawable(resBgResId));
        allTxt.setBackground(null);
        allTxt.setSelected(false);
        alreadyTxt.setBackground(null);
        alreadyTxt.setSelected(false);
    }

    private void setSelectAllUI() {
        allTxt.setSelected(true);
        int resBgResId = mMatchType == FOOTBALL ? R.drawable.shape_light_green_solid_radius_bg : R.drawable.shape_light_yellow_solid_radius_bg;
        allTxt.setBackground(getResources().getDrawable(resBgResId));
        onDoingTxt.setBackground(null);
        onDoingTxt.setSelected(false);
        alreadyTxt.setBackground(null);
        alreadyTxt.setSelected(false);
    }

    private void reqListData() {
        if (FOOTBALL.equals(mMatchType)) {
            requestFootBallData();
        } else if (BASKETBALL.equals(mMatchType)) {
            requestBascketBallData();
        }
    }

    /**
     * 筛选赛事类型
     */
    private View.OnClickListener filterMatchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSelectedNamesList = (List<String>) v.getTag();
            if (mSelectedNamesList != null) {
                if (mAllMatchList != null && mAllMatchList.size() > 0) {
                    List<GameParentBean> filterList = new ArrayList<>();
                    for (GameParentBean gameParentBean : mAllMatchList) {
                        if (mSelectedNamesList.contains(String.valueOf(gameParentBean.getMatchName()))) {
                            filterList.add(gameParentBean);
                        }
                    }
                    mGroupData.clear();
                    mGroupData.addAll(filterList);
                    mAdapter.setGroupData(filterList);
                    mAdapter.notifyDataSetChanged();
                }
            }

        }
    };


    /**
     * 设置Listview的EmptyView
     */
    private void setListViewEmptyView(ListView listView) {
        View emptyView = View.inflate(BettingGameActivity.this, R.layout.empty_view_temp_no_match, null);
        ViewGroup parentView = (ViewGroup) listView.getParent();
        parentView.addView(emptyView, 1);
        listView.setEmptyView(emptyView);
    }


    private void clearCheckedBtnState() {
        if (mGroupData.size() > 0) {
            for (int i = 0; i < mGroupData.size(); i++) {
               mGroupData.get(i).setGuestSelected(false);
               mGroupData.get(i).setHostSelected(false);
            }
        }
        if (mChildData.size() > 0) {
            for (int i = 0; i < mChildData.size(); i++) {
                mChildData.get(i).setGuestSelected(false);
                mChildData.get(i).setHostSelected(false);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void getChildItemData(String id, String raceStatus) {
        //statusLayout.showLoading();
        XjqRequestContainer map = new XjqRequestContainer(GAME_BOARD_QUERY, true);
        map.put("raceId", id);
        map.put("raceType", mMatchType); //足球FOOTBALL 籃球 BASKETBALL
        map.put("raceStatus", raceStatus);
        map.put("matchScheduleQueryType", mQueryType);
        map.put("gameBoardId", mGroupData.get(mGroupExpandPos).getBoardId());
        map.setGenericClaz(GameChildBean.class);
        httpRequestHelper.startRequest(map, false);
    }

    // 每次展开一个分组后，关闭其他的分组
    private void expandOnlyOne(int expandedPosition) {
        for (int i = 0; i < mGroupData.size(); i++) {
            if (i != expandedPosition && mListView.isGroupExpanded(i)) {
                mListView.collapseGroup(i);
            } else if (i == expandedPosition && !mListView.isGroupExpanded(i)) {
                mListView.expandGroup(i);
            }
        }
    }


    private void responseReqBacketBRaceBetting(JSONObject jsonObject) {
        BasketballRacesBean bean = new Gson().fromJson(jsonObject.toString(), BasketballRacesBean.class);
        //BasketballRacesBean bean = (BasketballRacesBean) object;
        mAdapter.reSetExpandPos(-1);
        mAdapter.setPopSelectorData(bean.getMultipleList());
        mGroupData.clear();
        timeTabLayout.setNowData(bean.getNowDate());
        handleData(bean);
        allMatchMap = bean.getMatchNameAndInnerMatchIdMap();
        matchFiveMap = bean.getMatchGroupAndInnerMatchIdsMap();
        List<BasketballRacesBean.BasketballRaceClientSimpleListBean> datas = bean.getBasketballRaceClientSimpleList();
        if (datas != null && datas.size() > 0) {
            mAllMatchList.clear();
            for (int i = 0; i < datas.size(); i++) {
                BasketballRacesBean.BasketballRaceClientSimpleListBean brcsBean = datas.get(i);
                addListItemData(brcsBean);
            }
            mAdapter.notifyDataSetChanged();
            emptyLinearLay.setVisibility(View.GONE);
        } else {
            emptyLinearLay.setVisibility(View.VISIBLE);
        }
        if (mGroupData.size() > 0) {
            //默认选中全部比赛
            for (GameParentBean gameParentBean : mGroupData) {
                mSelectedNamesList.add(gameParentBean.getMatchName());
            }
        }

    }

    private void responseReqFootBRaceBetting(JSONObject jsonObject) {
        FootballRacesBean bean = new Gson().fromJson(jsonObject.toString(), FootballRacesBean.class);
        //FootballRacesBean bean = (FootballRacesBean) object;
        mGroupData.clear();
        mAdapter.reSetExpandPos(-1);
        mAdapter.setPopSelectorData(bean.getMultipleList());
        timeTabLayout.setNowData(bean.getNowDate());
        handleData(bean);
        allMatchMap = bean.getMatchNameAndInnerMatchIdMap();
        matchFiveMap = bean.getMatchGroupAndInnerMatchIdsMap();
        List<FootballRacesBean.FootballRaceClientSimpleListBean> datas = bean.getFootballRaceClientSimpleList();
        if (datas != null && datas.size() > 0) {
            mAllMatchList.clear();
            for (int i = 0; i < datas.size(); i++) {
                FootballRacesBean.FootballRaceClientSimpleListBean frcsBean = datas.get(i);
                addListItemData(frcsBean);
            }
            mAdapter.notifyDataSetChanged();
            emptyLinearLay.setVisibility(View.GONE);
        } else {
            emptyLinearLay.setVisibility(View.VISIBLE);
        }
        //默认选中全部比赛
        for (GameParentBean gameParentBean : mGroupData) {
            mSelectedNamesList.add(gameParentBean.getMatchName());
        }
    }

    public void addListItemData(Object bean) {
        FootballRacesBean.FootballRaceClientSimpleListBean footBean = null;
        BasketballRacesBean.BasketballRaceClientSimpleListBean basketBean = null;
        String name1 = "";
        GameParentBean gameParentBean = new GameParentBean();
        FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean gameBoardBean = null;
        if (bean instanceof FootballRacesBean.FootballRaceClientSimpleListBean) {
            footBean = (FootballRacesBean.FootballRaceClientSimpleListBean) bean;
            gameBoardBean = footBean.gameBoardBean;
            gameParentBean.setInnerMatchId("" + footBean.getInnerMatchId());
            gameParentBean.setHostName(footBean.getHomeTeamName());
            gameParentBean.setGmtTime(getGmtFormatedTime(footBean.getGmtStart()));
            gameParentBean.setGuestName(footBean.getGuestTeamName());
            gameParentBean.setMatchName(footBean.getMatchName());
            gameParentBean.setHasChild(footBean.isExistedOtherGameBoard());
            gameParentBean.setId(footBean.getId());
            gameParentBean.setRaceStatus(footBean.getRaceStatus().getName());
            gameParentBean.setExistedDefaultGameBoard(footBean.isExistedDefaultGameBoard());
            gameParentBean.setGameBoardAllPrized(footBean.isGameBoardAllPrized());
        } else if (bean instanceof BasketballRacesBean.BasketballRaceClientSimpleListBean) {
            basketBean = (BasketballRacesBean.BasketballRaceClientSimpleListBean) bean;
            gameBoardBean = basketBean.gameBoardBean;
            gameParentBean.setInnerMatchId("" + basketBean.getInnerMatchId());
            gameParentBean.setHostName(basketBean.getHomeTeamName());
            gameParentBean.setGmtTime(getGmtFormatedTime(basketBean.getGmtStart()));
            gameParentBean.setGuestName(basketBean.getGuestTeamName());
            gameParentBean.setMatchName(basketBean.getMatchName());
            gameParentBean.setHasChild(basketBean.isExistedOtherGameBoard());
            gameParentBean.setId(basketBean.getId());
            gameParentBean.setRaceStatus(basketBean.getRaceStatus().getName());
            gameParentBean.setExistedDefaultGameBoard(basketBean.isExistedDefaultGameBoard());
            gameParentBean.setGameBoardAllPrized(basketBean.isGameBoardAllPrized());
        }
        try {
            if (gameBoardBean != null && gameBoardBean.getRaceStageType() != null) {
                name1 = "【" + gameBoardBean.getRaceStageType().getName() + "】";
                if (gameBoardBean.getRaceType() != null && "FOOTBALL".equals(gameBoardBean.getRaceType())) {
                    name1 += "比分";
                } else {
                    name1 += "比分";
                }
                gameParentBean.setPlate(gameBoardBean.getPlate());
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("xxl", "bug222");
        }
        //赛局名称让球让分
        gameParentBean.setChangci(name1);
        GameParentBean withExtraBean = getGroupViewHot(bean, gameParentBean);
        mGroupData.add(withExtraBean);
        mAllMatchList.add(withExtraBean);
    }

    private void handleData(Object obj) {
        if (obj instanceof FootballRacesBean) {
            FootballRacesBean bean = (FootballRacesBean) obj;
            if (bean.getFootballRaceClientSimpleList() != null) {
                for (FootballRacesBean.FootballRaceClientSimpleListBean bean1 : bean.getFootballRaceClientSimpleList()) {
                    if (bean1 != null) {
                        if (bean.getRaceIdAndNearestGameBoardMap() != null) {
                            bean1.gameBoardBean = bean.getRaceIdAndNearestGameBoardMap().get(bean1.getId());
                        }
                        if (bean.getRaceIdAndSumInfoMap() != null) {
                            bean1.sumInfoList = bean.getRaceIdAndSumInfoMap().get(bean1.getId());
                        }
                    }
                }

            }

        } else if (obj instanceof BasketballRacesBean) {
            BasketballRacesBean bean = (BasketballRacesBean) obj;
            if (bean.getBasketballRaceClientSimpleList() != null) {
                for (BasketballRacesBean.BasketballRaceClientSimpleListBean bean1 : bean.getBasketballRaceClientSimpleList()) {
                    if (bean1 != null) {
                        if (bean.getRaceIdAndNearestGameBoardMap() != null) {
                            bean1.gameBoardBean = bean.getRaceIdAndNearestGameBoardMap().get(bean1.getId());
                        }
                        if (bean.getRaceIdAndSumInfoMap() != null) {
                            bean1.sumInfoList = bean.getRaceIdAndSumInfoMap().get(bean1.getId());
                        }
                    }
                }
            }
        }
    }

    public GameParentBean getGroupViewHot(Object bean, GameParentBean gameParentBean) {
        handleData(bean);
        String[] hots = new String[3];
        FootballRacesBean.FootballRaceClientSimpleListBean footBean = null;
        BasketballRacesBean.BasketballRaceClientSimpleListBean basketBean = null;
        List<BasketballRacesBean.SumInfoBean> sumInfoBeanList = null;
        double hotZhu = 0.0;
        double hotke = 0.0;
        FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean gameBoardBean = null;
        if (bean instanceof FootballRacesBean.FootballRaceClientSimpleListBean) {
            footBean = (FootballRacesBean.FootballRaceClientSimpleListBean) bean;
            gameBoardBean = footBean.gameBoardBean;
            sumInfoBeanList = footBean.sumInfoList;
        } else if (bean instanceof BasketballRacesBean.BasketballRaceClientSimpleListBean) {
            basketBean = (BasketballRacesBean.BasketballRaceClientSimpleListBean) bean;
            gameBoardBean = basketBean.gameBoardBean;
            sumInfoBeanList = basketBean.sumInfoList;
        }
        //raceIdAndNearestGameBoardMap 中如果找不到id则这场比赛没有局，不显示助威。
        try {
            if (gameBoardBean != null && gameBoardBean.getLeftGameBoardOptionEntry() != null) {
                hotZhu = gameBoardBean.getLeftGameBoardOptionEntry().getTotalFee();
                gameParentBean.setHostPriceFee(gameBoardBean.getLeftGameBoardOptionEntry().getTotalPaidFee());
            }

            if (gameBoardBean != null && gameBoardBean.getRightGameBoardOptionEntry() != null) {
                hotke = gameBoardBean.getRightGameBoardOptionEntry().getTotalFee();
                gameParentBean.setGuestPriceFee(gameBoardBean.getRightGameBoardOptionEntry().getTotalPaidFee());
            }

            if (sumInfoBeanList != null) {
                for (BasketballRacesBean.SumInfoBean sumInfoBean : sumInfoBeanList) {
                    if ("HOME_WIN".equals(sumInfoBean.getOptionCode())) {
                        hotZhu = sumInfoBean.getSumFee();
                    } else if ("GUEST_WIN".equals(sumInfoBean.getOptionCode())) {
                        hotke = sumInfoBean.getSumFee();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        gameParentBean.setHostHotScore(hotZhu);
        gameParentBean.setGuestHotScore(hotke);
        boolean isShowZhuWei = false;
        if (gameBoardBean != null) {
            gameParentBean.setBoardId(gameBoardBean.getGameBoardId());
            gameParentBean.setPlayType(gameBoardBean.getOptionGroup());
            gameParentBean.setRaceId(gameBoardBean.getRaceId());
            gameParentBean.setRaceType(gameBoardBean.getRaceType());
            gameParentBean.setGiftHomeEntry(gameBoardBean.getLeftGameBoardOptionEntry());
            gameParentBean.setGiftGuestEntry(gameBoardBean.getRightGameBoardOptionEntry());
            isShowZhuWei = "PROGRESSING".equals(gameBoardBean.getSaleStatus().getName()) ? true : false;
        }
        gameParentBean.setShowZhuWei(isShowZhuWei);

        return gameParentBean;
    }

    private void responseReqQueryGameBoard(JSONObject object) {
        mGroupData.get(mGroupExpandPos).setExpanded(true);
        mChildData.clear();
        GameChildBean bean = new Gson().fromJson(object.toString(), GameChildBean.class);
        //GameChildBean bean = (GameChildBean) object;
        String hostName = bean.getRaceDataSimple().getHomeTeamName();
        String guestName = bean.getRaceDataSimple().getGuestTeamName();
        List<GameChildBean.GameBoardListBean> gameBoardList = bean.getGameBoardList();
        if (gameBoardList != null && gameBoardList.size() > 0) {
            for (int i = 0; i < gameBoardList.size(); i++) {
                MyGameChildBean myGameChildBean = new MyGameChildBean();
                GameChildBean.GameBoardListBean gameBoardListBean = gameBoardList.get(i);
                String status = gameBoardListBean.getSaleStatus().getName();
                myGameChildBean.setShowZhuWei("PROGRESSING".equals(status) ? true : false);
                String changci = "【" + gameBoardListBean.getRaceStageType().getName() + "】";
                String optionGroup = gameBoardListBean.getOptionGroup();
                if ("RFSF".equals(optionGroup)) {
                    changci = changci + "比分";
                } else if ("RQSF".equals(optionGroup)) {
                    changci = changci + "比分";
                }
                myGameChildBean.setOptionGroup(optionGroup);
                myGameChildBean.setPlate(gameBoardListBean.getPlate());
                myGameChildBean.setChangci(changci);
                myGameChildBean.setMatchName(bean.getRaceDataSimple().getMatchName());
                myGameChildBean.setGmtStart(getGmtFormatedTime(bean.getRaceDataSimple().getGmtStart()));
                double guestScore = gameBoardListBean.getRightGameBoardOptionEntry().getTotalFee();
                myGameChildBean.setGusetScore(guestScore);
                double guestPrice = gameBoardListBean.getRightGameBoardOptionEntry().getTotalPaidFee();
                myGameChildBean.setGuestPriceFee(guestPrice);

                double hostScore = gameBoardListBean.getLeftGameBoardOptionEntry().getTotalFee();
                myGameChildBean.setHostScore(hostScore);
                double hostPrice = gameBoardListBean.getLeftGameBoardOptionEntry().getTotalPaidFee();
                myGameChildBean.setHostPriceFee(hostPrice);

                myGameChildBean.setRaceType(gameBoardListBean.getRaceType());
                myGameChildBean.setBoardId(gameBoardListBean.getGameBoardId());
                myGameChildBean.setRaceId(gameBoardListBean.getRaceId());
                myGameChildBean.setPlayType(gameBoardListBean.getOptionGroup());
                myGameChildBean.setParentGuestName(guestName);
                myGameChildBean.setParentHostName(hostName);
                myGameChildBean.setGiftHomeEntry(gameBoardListBean.getLeftGameBoardOptionEntry());
                myGameChildBean.setGiftGuestEntry(gameBoardListBean.getRightGameBoardOptionEntry());
                mChildData.add(myGameChildBean);
            }
        }
        mAdapter.setChildItemData(mChildData, mGroupExpandPos);
    }

    private void refreshList(boolean isExpandChild) {
        if (mGroupData.size() == 0) {
            mListView.removeFooterView(mListFooterView);
        }
        LogUtils.e(TAG, "mGroupData=" + mGroupData.size());
        mAdapter.setGroupData(mGroupData);
        mAdapter.setRefreshCompleted(true);
        // 收起所有的子项,并且滚动到第一项
        if (!isExpandChild) {
            expandOnlyOne( -1);
            // mListView.requestFocusFromTouch();
            mListView.setSelection(0);
        }
    }

    private void clearListData() {
        mGroupData.clear();
        mChildData.clear();
        //mAdapter.notifyDataSetChanged();
    }


    public static void startBettingGameActivity(Activity activity) {
        activity.startActivity(new Intent(activity, BettingGameActivity.class));
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        //statusLayout.hideStatusView();
        boolean isExpandChild = false;
        switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
            case BASKETBALL_RACE_BETTING_PAGE_INFO_BY_DATE:
                mProgressBar.setVisibility(View.GONE);
                responseReqBacketBRaceBetting(jsonObject);
                break;
            case FOOTBALL_RACE_BETTING_PAGE_INFO_BY_DATE:
                mProgressBar.setVisibility(View.GONE);
                responseReqFootBRaceBetting(jsonObject);
                break;
            case GAME_BOARD_QUERY:
                isExpandChild = true;
                responseReqQueryGameBoard(jsonObject);
                break;
            case PURCHASE_BASKETBALL_NORMAL:
                isExpandChild = true;
                String btotalFee = jsonObject.optString("totalFee");
                //Toast.makeText(BettingGameActivity.this, "助威礼物成功" + btotalFee, Toast.LENGTH_SHORT).show();
                refreshCheerPrices(btotalFee);
                break;
            case PURCHASE_FOOTBALL_NORMAL:
                isExpandChild = true;
                String ftotalFee = jsonObject.optString("totalFee");
                //Toast.makeText(BettingGameActivity.this, "助威礼物成功" + ftotalFee, Toast.LENGTH_SHORT).show();
                refreshCheerPrices(ftotalFee);
                break;
            default:
                break;
        }
        refreshList(isExpandChild);
    }

    private void refreshCheerPrices(String btotalFee) {
        if (!"".equals(btotalFee)) {
            if (mGroupType == TAG_PARENT) {
                if (mCheerType == TAG_HOST) {
                    double curHostPrice = mGroupData.get(mGroupCheerPos).getHostPriceFee() + Double.valueOf(btotalFee);
                    mGroupData.get(mGroupCheerPos).setHostPriceFee(curHostPrice);
                    double curHostHotScore = mGroupData.get(mGroupCheerPos).getHostHotScore() + Double.valueOf(btotalFee);
                    mGroupData.get(mGroupCheerPos).setHostHotScore(curHostHotScore);
                } else if (mCheerType == TAG_GUEST) {
                    double curGuestPrice = mGroupData.get(mGroupCheerPos).getGuestPriceFee() + Double.valueOf(btotalFee);
                    mGroupData.get(mGroupCheerPos).setGuestPriceFee(curGuestPrice);
                    double curGuestHotScore = mGroupData.get(mGroupCheerPos).getGuestHotScore() + Double.valueOf(btotalFee);
                    mGroupData.get(mGroupCheerPos).setGuestHotScore(curGuestHotScore);
                }
            } else if (mGroupType == TAG_CHILD) {
                if (mCheerType == TAG_HOST) {
                    double curHostPrice = mChildData.get(mChildCheerPos).getHostPriceFee() + Double.valueOf(btotalFee);
                    mChildData.get(mChildCheerPos).setHostPriceFee(curHostPrice);
                    double curHostHotScore = mChildData.get(mChildCheerPos).getHostScore() + Double.valueOf(btotalFee);
                    mChildData.get(mChildCheerPos).setHostScore(curHostHotScore);
                } else if (mCheerType == TAG_GUEST) {
                    double curGuestPrice = mChildData.get(mChildCheerPos).getGuestPriceFee() + Double.valueOf(btotalFee);
                    mChildData.get(mChildCheerPos).setGuestPriceFee(curGuestPrice);
                    double curGuestHotScore = mChildData.get(mChildCheerPos).getGusetScore() + Double.valueOf(btotalFee);
                    mChildData.get(mChildCheerPos).setGusetScore(curGuestHotScore);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ((((XjqUrlEnum) requestContainer.getRequestEnum())) == GAME_BOARD_QUERY) {
            // mGroupData.get(mExpandPos).setHasChild(false);
            mAdapter.setRefreshCompleted(false);
            mAdapter.reSetExpandPos(-1);
        }

    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }
}
