package com.android.xjq.activity.myzhuwei;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.groupchat.bean.GroupChatTopic1;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.myzhuwei.widget.BannerView;
import com.android.xjq.activity.myzhuwei.widget.ThemeExpandableAdapter;
import com.android.xjq.bean.myzhuwei.BasketballRacesBean;
import com.android.xjq.bean.myzhuwei.FootballRacesBean;
import com.android.xjq.bean.myzhuwei.ThemeChildBean;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.view.indicate.TimeTabLayout2;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kokuma on 2017/11/6.
 */

public class ThemeSetActivity extends BaseActivity4Jczj implements IHttpResponseListener, View.OnClickListener, ExpandableListView.OnGroupClickListener, ExpandableListView.OnGroupExpandListener {

    List<String> listThemeIds;
    List<Object> listThemeObjs;
    GroupChatTopic1 groupTopic;
    private WrapperHttpHelper httpHelper;
    String groupChatId = "", mQueryType;
    String date;

    @BindView(R.id.ballFilterRg)
    RadioGroup ballFilterRg;
    @BindView(R.id.timeLayout)
    TimeTabLayout2 timeTabLayout;
    @BindView(R.id.back_ibtn)
    ImageButton navBackIbtn;

    @BindView(R.id.filterBtn)
    ImageButton filterBtn;
    @BindView(R.id.tvComplete)
    TextView tvComplete;
    List<FootballRacesBean.FootballRaceClientSimpleListBean> listFoot;
    List<BasketballRacesBean.BasketballRaceClientSimpleListBean> listBasket;

    ThemeExpandableAdapter adapter;

    @BindView(R.id.bannerView)
    BannerView bannerView;
    @BindView(R.id.expandableView)
    ExpandableListView expandableView;
    int MAX_COUNT;
    final String txtToast = "主题最大数量为";

    int clickGroupPos = -1;

    //2648782581300200970025600640
    public static void startThemeSetActivity(Context context, String groupChatId) {
        Intent intent = new Intent(context, ThemeSetActivity.class);
        intent.putExtra("groupChatId", groupChatId);
        context.startActivity(intent);
    }


    @Override
    public void onSuccess(RequestContainer request, Object result) {
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        if (requestEnum == XjqUrlEnum.FOOTBALL_RACE_BETTING_PAGE_INFO_BY_DATE && result != null) {
            if (mQueryType.equals("BASKETBALL")) {
                LogUtils.i("xxl", "ThemeSet-FOOTBALL-back");
                return;
            }
            FootballRacesBean bean = (FootballRacesBean) result;
            listFoot.clear();
            if (bean != null && bean.isSuccess()) {
                handleData(bean);
                timeTabLayout.setNowData(bean.getNowDate());
            }
            LogUtils.i("xxl", "ThemeSet-FOOTBALL" + listFoot.size());
            adapter.update(listFoot);
        } else if (requestEnum == XjqUrlEnum.BASKETBALL_RACE_BETTING_PAGE_INFO_BY_DATE && result != null) {
            if (mQueryType.equals("FOOTBALL")) {
                LogUtils.i("xxl", "ThemeSet-BASKETBALL-back");
                return;
            }
            BasketballRacesBean bean = (BasketballRacesBean) result;
            listBasket.clear();
            if (bean != null && bean.isSuccess()) {
                try {
                    timeTabLayout.setNowData(bean.getNowDate());
                    handleData(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.i("xxl", "ThemeSetbug111");
                }
            }
            LogUtils.i("xxl", "ThemeSet-BASKETBALL" + listFoot.size());
            adapter.update(listBasket);
        } else if (requestEnum == XjqUrlEnum.GAME_BOARD_QUERY && result != null) {
            ThemeChildBean bean = (ThemeChildBean) result;
            if (bean != null && bean.isSuccess()) {
                adapter.mapChild.clear();
                if (bean.getGameBoardList() != null && bean.getGameBoardList().size() > 0) {
                    FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean gameBoardBean = bean.getGameBoardList().get(0);
                    if (gameBoardBean != null && gameBoardBean.getRaceId() != null) {
                        adapter.mapChild.put(gameBoardBean.getRaceId(), bean.getGameBoardList());
                        adapter.countChild = bean.getGameBoardList().size();
                    }
                }
                adapter.groupPosSelect = -1;
                adapter.notifyDataSetChanged();
                if (clickGroupPos >= 0) {
                    expandableView.expandGroup(clickGroupPos);
                }

            }

        } else if (requestEnum == XjqUrlEnum.THEME_QUERY && result != null) {
            groupTopic = (GroupChatTopic1) result;
            initThemeBean();

        } else if (requestEnum == XjqUrlEnum.THEME_OPERATE && result != null) {
            ThemeChildBean bean = (ThemeChildBean) result;
            if (bean != null && bean.isSuccess()) {
                finish();
            }
        }
    }

    void initThemeBean() {
        if (groupTopic != null) {
            MAX_COUNT = groupTopic.themeMaxMum;
            if (groupTopic.gameBoardList != null) {
                for (GroupChatTopic1.RaceIdAndGameBoard bean : groupTopic.gameBoardList) {
                    if (bean != null) {
                        String raceId = bean.raceId;
                        if (!TextUtils.isEmpty(raceId)) {
                            JczqDataBean beanBall = null;
                            if ("FOOTBALL".equals(bean.raceType) && groupTopic.footballRaceMap != null) {
                                beanBall = groupTopic.footballRaceMap.get(raceId);
                            } else if ("BASKETBALL".equals(bean.raceType) && groupTopic.basketballRaceMap != null) {
                                beanBall = groupTopic.basketballRaceMap.get(raceId);
                            }
                            if (beanBall != null) {
                                bean.homeName = beanBall.homeTeamName;
                                bean.guestName = beanBall.guestTeamName;
                            }
                            addData(bean);
                            adapter.notifyDataSetChanged();
                            bannerView.update(listThemeObjs);
                        }
                    }
                }
                tvComplete.setText("(完成" + listThemeObjs.size() + ")");
            }
        }
    }

    void handleData(Object obj) {
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
                        listFoot.add(bean1);
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
                        listBasket.add(bean1);
                    }
                }
            }
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {

    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_theme_select);
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpView() {
        setUITitle();
        bannerView.onClickListener = this;
    }

    void getData_theme(String groupChatId) {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.THEME_QUERY, true);
        container.put("objectType", "GROUP_CHAT");
        container.put("objectId", groupChatId);
        container.setGenericClaz(GroupChatTopic1.class);
        httpHelper.startRequest(container, false);
    }

    void getData_football() {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.FOOTBALL_RACE_BETTING_PAGE_INFO_BY_DATE, true);
        container.put("date", date);
        container.put("matchScheduleQueryType", "ALL");
        container.setGenericClaz(FootballRacesBean.class);
        httpHelper.startRequest(container, true);
    }

    void getData_basketball() {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.BASKETBALL_RACE_BETTING_PAGE_INFO_BY_DATE, true);
        container.put("date", date);
        container.put("matchScheduleQueryType", "ALL");
        container.setGenericClaz(BasketballRacesBean.class);
        httpHelper.startRequest(container, true);
    }

    void getData_child(String raceId, String raceStatus, String gameBoardId) {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.GAME_BOARD_QUERY, true);
        map.put("raceId", raceId); //"2650349721107890980015446700"
        map.put("raceType", mQueryType); //足球FOOTBALL 籃球 BASKETBALL
        map.put("raceStatus", raceStatus);
        map.put("gameBoardId", gameBoardId);
        if (mQueryType.equals("FOOTBALL")) {
            map.setGenericClaz(ThemeChildBean.class);
        } else if (mQueryType.equals("BASKETBALL")) {
            map.setGenericClaz(ThemeChildBean.class);
        }
        httpHelper.startRequest(map, true);
    }

    void toSetTheme(String groupChatId) {
        LogUtils.i("xxl", "groupChatId-" + groupChatId);
        LogUtils.i("xxl", "themeIds-" + listThemeIds.size());
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.THEME_OPERATE, true);
        container.put("themeType", "GAME_BOARD");
        container.put("objectType", "GROUP_CHAT");
        container.put("objectId", groupChatId);
        container.putStringList("themeIds", listThemeIds);
        container.setGenericClaz(ThemeChildBean.class);
        httpHelper.startRequest(container, true);
    }

    @Override
    protected void setUpData() {
        mQueryType = "FOOTBALL";
        date = TimeUtils.getCurrentTime();
        if (getIntent() != null) {
            groupChatId = getIntent().getStringExtra("groupChatId");
        }
        listThemeIds = new ArrayList<String>();
        listThemeObjs = new ArrayList<>();
        listFoot = new ArrayList<FootballRacesBean.FootballRaceClientSimpleListBean>();
        listBasket = new ArrayList<BasketballRacesBean.BasketballRaceClientSimpleListBean>();
        httpHelper = new WrapperHttpHelper(this);
        adapter = new ThemeExpandableAdapter(this);
        adapter.expandableListView = expandableView;
        adapter.listThemeObjs = listThemeObjs;
        adapter.listThemeIds = listThemeIds;
        adapter.onClickListener = this;
        expandableView.setAdapter(adapter);
        expandableView.setOnGroupClickListener(this);
        expandableView.setOnGroupExpandListener(this);
        timeTabLayout.setIonCheckTimeListener(new TimeTabLayout2.IonCheckTime() {
            @Override
            public void onCheckTime(String time) {
                date = time;
                if (mQueryType.equals("FOOTBALL")) {
                    getData_football();
                } else {
                    getData_basketball();
                }

            }
        });
        ballFilterRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
                if (id == R.id.footballRb) {
                    mQueryType = "FOOTBALL";
                    getData_football();
                } else if (id == R.id.basketballRb) {
                    mQueryType = "BASKETBALL";
                    getData_basketball();
                }
            }
        });
        getData_theme(groupChatId);
        getData_football();
        //getData_basketball();
    }

    void addData(Object obj) {
        String idSelectId = getId(obj);
        if (!TextUtils.isEmpty(idSelectId) && !listThemeIds.contains(idSelectId)) {
            int index = indexId(idSelectId);
            if (index < 0) {
                listThemeObjs.add(obj);
            } else {
                LogUtils.i("xxl", "Clickidselect_yes-bug-bug22!" + idSelectId);
            }
            listThemeIds.add(idSelectId);
        }
        // tvSelect.setImageResource(R.drawable.icon_contact_selected);
        LogUtils.i("xxl", "Clickidselect_yes-" + idSelectId);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Object obj = view.getTag();
        if (id == R.id.tvSelect) {
            boolean isSelect = (boolean) view.getTag(R.id.id_tag);
            if (isSelect) {
                String idSelectId = getId(obj);
                listThemeIds.remove(idSelectId);
                int index = indexId(idSelectId);
                if (index >= 0) {
                    listThemeObjs.remove(index);
                } else {
                    LogUtils.i("xxl", "Clickidselect_no-bug-bug11!" + idSelectId);
                }

                LogUtils.i("xxl", "Clickidselect_no-" + idSelectId);
            } else {
                if (listThemeIds.size() >= MAX_COUNT) {
                    Toast.makeText(this, txtToast + MAX_COUNT, Toast.LENGTH_SHORT).show();
                    return;
                }
                addData(obj);
            }
        } else if (id == R.id.btnDelete) {
            listThemeObjs.remove(obj);
            String idSelect = getId(obj);
            listThemeIds.remove(idSelect);
        } else if (id == R.id.expandLayout) {
            if (obj instanceof Integer) {
                adapter.groupPosSelect = -1;
                expandableView.collapseGroup((Integer) obj);
            }
            return;
        }
        adapter.notifyDataSetChanged();
        bannerView.update(listThemeObjs);

        tvComplete.setText("(完成" + listThemeIds.size() + ")");
    }

    boolean isOverCount() {
        if (listThemeIds.size() > MAX_COUNT) {
            return true;
        }
        return false;
    }

    public int indexId(String gameBoardId) {
        for (int i = 0; i < listThemeObjs.size(); i++) {
            String id = getId(listThemeObjs.get(i));
            if (id.equals(gameBoardId)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
        Object obj = view.getTag();
        if (adapter.groupPosSelect == i) {
            adapter.groupPosSelect = -1;
            LogUtils.i("xxl", "bugonGroupClick-isGroupExpanded");
        } else {
            clickGroupPos = i;
            if (ThemeExpandableAdapter.isExistedOtherGameBoard(obj)) {
                String raceId = getRaceId(obj);
                String raceStatus = getRaceStatus(obj);
                String gameBoardId = getId(obj);
                getData_child(raceId, raceStatus, gameBoardId);
                LogUtils.i("xxl", "bugonGroupClick-raceId-" + raceId + "-" + raceStatus);
            } else {
                LogUtils.i("xxl", "bugonGroupClick-wuZhankai2");
            }
        }

        return false;
    }

    void setUITitle() {
        navBackIbtn.setVisibility(View.VISIBLE);
        filterBtn.setVisibility(View.GONE);
        tvComplete.setVisibility(View.VISIBLE);
    }

    public static FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean getGameBoardBean(Object obj) {
        if (obj != null) {
            if (obj instanceof FootballRacesBean.FootballRaceClientSimpleListBean) {
                FootballRacesBean.FootballRaceClientSimpleListBean bean = (FootballRacesBean.FootballRaceClientSimpleListBean) obj;
                return bean.gameBoardBean;
            } else if (obj instanceof BasketballRacesBean.BasketballRaceClientSimpleListBean) {
                BasketballRacesBean.BasketballRaceClientSimpleListBean bean = (BasketballRacesBean.BasketballRaceClientSimpleListBean) obj;
                return bean.gameBoardBean;
            } else if (obj instanceof FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean) {
                FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean bean = (FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean) obj;
                return bean;
            }
        }
        return null;
    }

    public static String getId(Object obj) {
        String id = "";
        if (obj != null) {
            FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean bean = getGameBoardBean(obj);
            if (bean != null) {
                id = bean.getGameBoardId();
            }
        }

        if (obj instanceof GroupChatTopic1.RaceIdAndGameBoard) {
            GroupChatTopic1.RaceIdAndGameBoard bean = (GroupChatTopic1.RaceIdAndGameBoard) obj;
            return bean.gameBoardId;
        }
        return id;
    }

    public static String getRaceStatus(Object obj) {
        String id = "";
        if (obj != null) {
            if (obj instanceof FootballRacesBean.FootballRaceClientSimpleListBean) {
                FootballRacesBean.FootballRaceClientSimpleListBean bean = (FootballRacesBean.FootballRaceClientSimpleListBean) obj;
                if (bean.getRaceStatus() != null) {
                    id = bean.getRaceStatus().getName();
                }

            } else if (obj instanceof BasketballRacesBean.BasketballRaceClientSimpleListBean) {
                BasketballRacesBean.BasketballRaceClientSimpleListBean bean = (BasketballRacesBean.BasketballRaceClientSimpleListBean) obj;
                if (bean.getRaceStatus() != null) {
                    id = bean.getRaceStatus().getName();
                }
            }
//            else if (obj instanceof FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean) {
//                FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean bean = (FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean) obj;
//                id = bean.getRaceId();
//            }
        }
        return id;
    }

    public static String getRaceId(Object obj) {
        String id = "";
        if (obj != null) {
            if (obj instanceof FootballRacesBean.FootballRaceClientSimpleListBean) {
                FootballRacesBean.FootballRaceClientSimpleListBean bean = (FootballRacesBean.FootballRaceClientSimpleListBean) obj;
                id = bean.getId();
            } else if (obj instanceof BasketballRacesBean.BasketballRaceClientSimpleListBean) {
                BasketballRacesBean.BasketballRaceClientSimpleListBean bean = (BasketballRacesBean.BasketballRaceClientSimpleListBean) obj;
                id = bean.getId();
            } else if (obj instanceof FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean) {
                FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean bean = (FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean) obj;
                id = bean.getRaceId();
            }
        }
        return id;
    }

    @OnClick(R.id.tvComplete)
    void complete() {
//        listThemeIds.clear();
//        if(listThemeObjs!=null){
//            for(Object obj:listThemeObjs){
//                if(obj!=null){
//                    String id = getId(obj);
//                    if(!TextUtils.isEmpty(id)){
//                        listThemeIds.add(id);
//                    }
//                }
//            }
//        }
        if (isOverCount()) {
            Toast.makeText(this, txtToast + MAX_COUNT, Toast.LENGTH_SHORT).show();
            return;
        }
        toSetTheme(groupChatId);
    }

    @OnClick(R.id.back_ibtn)
    void back() {
        finish();
    }

    @Override
    public void onGroupExpand(int i) {
        adapter.groupPosSelect = i;
        adapter.notifyDataSetChanged();
    }
}
