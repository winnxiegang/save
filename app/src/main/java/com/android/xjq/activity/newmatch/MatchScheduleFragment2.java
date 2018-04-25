package com.android.xjq.activity.newmatch;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.pullrecycler.recyclerview.DividerItemDecoration;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.activity.dynamic.ScheduleDetailsActivity;
import com.android.xjq.adapter.main.MatchScheduleAdapter2;
import com.android.xjq.bean.MatchScheduleBean;
import com.android.xjq.bean.matchLive.MatchScheduleEntity;
import com.android.xjq.bean.matchschedule.ChannelAreaBean;
import com.android.xjq.fragment.BaseFragment;
import com.android.xjq.presenters.CommonReqHelper;
import com.android.xjq.presenters.viewinface.MatchScheduleView;
import com.android.xjq.utils.matchLive.FilterMatchHelper;
import com.android.xjq.view.OverlapTabWithDrawableView;
import com.android.xjq.view.indicate.TimeTabLayout2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.xjq.utils.matchLive.FilterMatchHelper.OPERATE_MATCH_LIVE;

public class MatchScheduleFragment2 extends BaseFragment implements MatchScheduleView {
    public static final int TERMINAL_TIME = 5 * 1000;
    public static final int MSG_REPEAT_REQ = 0;
    private int mMatchType = CommonReqHelper.MATCH_TYPE_FOOTBALL;
    @BindView(R.id.over_lap_tab_with_drawable_view)
    OverlapTabWithDrawableView mOverlapTabWithDrawableView;
    @BindView(R.id.match_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.filterBtn)
    ImageButton filterBtn;
    @BindView(R.id.timeLayout)
    TimeTabLayout2 timeLayout;
    @BindView(R.id.no_content_schedule_img)
    ImageView emptyImg;
    @BindView(R.id.back_ibtn)
    ImageButton backIbtn;
    /*    @BindView(R.id.emptyLayout)
        LinearLayout emptyLinearLay;*/
/*    @BindView(R.id.statusLayout)
    CommonStatusLayout commonStatusLayout;*/
    private TimerTask mTimeTask;
    private Timer mTimer;
    private MatchScheduleAdapter2 mAdapter;
    private Handler mHandler;
    private HashMap<String, Long> allMatchMap;
    private HashMap<String, String> matchFiveMap;
    private List<String> mSelectedNamesList;
    private FilterMatchHelper mFilterMatchHelper;
    private List<MatchScheduleBean> mAllMatchList;
    private CommonReqHelper mCommonReqHelper;
    private int mPos;
    private int mOperateOrderSubPos;
    private int mOperateOrderPos;

    @Override
    protected void initData() {
    }

    @OnClick(R.id.filterBtn)
    public void filterBtn() {
        if (mMatchType == CommonReqHelper.MATCH_TYPE_FOOTBALL) {
            mFilterMatchHelper.startFilter(false, allMatchMap, matchFiveMap, mSelectedNamesList);
        } else if (mMatchType == CommonReqHelper.MATCH_TYPE_BASKETBALL) {
            mFilterMatchHelper.startFilter(true, allMatchMap, matchFiveMap, mSelectedNamesList);
        }
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_match_list, null);
        fitsSystemWindows(view.findViewById(R.id.title_lay));
        ButterKnife.bind(this, view);
        initViewData();
        initViews();
        return view;
    }

    private void initViews() {
        backIbtn.setVisibility(View.GONE);
        mAdapter = new MatchScheduleAdapter2(getActivity());
        //mAdapter.setData(mListData);
        mAdapter.setType(mMatchType);
        mAdapter.setOnClickCallBack(new MatchScheduleAdapter2.onClickCallBack() {
            @Override
            public void onClickMore(int pos) {
                mPos = pos;
                goToProgramType();
            }

            @Override
            public void onClickChnlStatus(boolean isUserOrderChannelArea, int channelAreaId, int subPos, int pos) {
                mOperateOrderSubPos = subPos;
                mOperateOrderPos = pos;
                if (isUserOrderChannelArea) {
                    showConfirmDialog(channelAreaId);
                } else {
                    mCommonReqHelper.reqOrder(channelAreaId);
                }
            }

            @Override
            public void onItemContentClick(int pos) {
                mPos = pos;
                String raceType = mMatchType == CommonReqHelper.MATCH_TYPE_FOOTBALL ? "FOOTBALL" : "BASKETBALL";
                ScheduleDetailsActivity.startScheduleDetailsActivity(getActivity(), mAdapter.getData().get(mPos).getId(), raceType);
            }

            @Override
            public void onGoToLive(int channelId) {
                LiveActivity.startLiveActivity(getActivity(), channelId);
            }

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.base_divider_list_light_gray_10dp));


        mOverlapTabWithDrawableView.setOnSwitchTabListener(new OverlapTabWithDrawableView.IonSwitchTab() {
            @Override
            public void onSwitchLeftTab() {
                mMatchType = CommonReqHelper.MATCH_TYPE_FOOTBALL;
                mCommonReqHelper.setMatchType(mMatchType);
                resetListView();
            }

            @Override
            public void onSwitchRightTab() {
                mMatchType = CommonReqHelper.MATCH_TYPE_BASKETBALL;
                mCommonReqHelper.setMatchType(mMatchType);
                resetListView();
            }
        });

        timeLayout.setIonCheckTimeListener(new TimeTabLayout2.IonCheckTime() {
            @Override
            public void onCheckTime(String time) {
                mCommonReqHelper.setReqDate(time);
                resetListView();
            }
        });

    }

    private void goToProgramType() {
        String raceType = mMatchType == CommonReqHelper.MATCH_TYPE_FOOTBALL ? "FOOTBALL" : "BASKETBALL";
        ScheduleDetailsActivity.startScheduleDetailsActivity(getActivity(), mAdapter.getData().get(mPos).getId(),
                raceType, ScheduleDetailsActivity.POS_PROGRAM);
    }

    private void showConfirmDialog(final long channelAreaId) {
        ShowMessageDialog dialog = new ShowMessageDialog(getActivity(), "确定", "取消",
                new OnMyClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCommonReqHelper.reqCancelOrder(channelAreaId);
                    }
                },
                new OnMyClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("", "on cancel click");
                    }
                }, "确定要取消预约该节目吗?");
    }

    private void resetListView() {
        mAdapter.setType(mMatchType);
        mCommonReqHelper.requestBetQuery();
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
            mSelectedNamesList = (List<String>) v.getTag();
            if (mSelectedNamesList != null) {
                List<MatchScheduleBean> filterList = new ArrayList<>();
                if (mAllMatchList != null && mAllMatchList.size() > 0) {
                    List<String> selectedRaceIds = new ArrayList<>();
                    for (MatchScheduleBean matchScheduleBean : mAllMatchList) {
                        if (mSelectedNamesList.contains(matchScheduleBean.getMatchName())) {
                            filterList.add(matchScheduleBean);
                            selectedRaceIds.add(String.valueOf(matchScheduleBean.getInnerRaceId()));
                        }
                    }
                    mAdapter.setData(filterList);
                    mCommonReqHelper.setFilteredData(filterList);
                    mCommonReqHelper.filterRaceId(selectedRaceIds);
                }
            }
        }
    };


    private void initViewData() {
        mCommonReqHelper = new CommonReqHelper(this);
        mFilterMatchHelper = new FilterMatchHelper(activity, OPERATE_MATCH_LIVE, filteredClickListener);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_REPEAT_REQ:
                        mCommonReqHelper.requestDynamicDataQuery();
                        break;
                }
            }
        };

        mSelectedNamesList = new ArrayList<>();
        mCommonReqHelper.requestBetQuery();
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
                    if (mAdapter.getListDataSize() > 0 && mCommonReqHelper.isAllMatchFinished()) {
                        stopTimer();
                    } else if (mAdapter.getListDataSize() == 0) {
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
        mCommonReqHelper.detachView();
    }


    private void clearListData() {
        mAdapter.clearList();
    }


    @Override
    public void orderSucces() {
        Toast.makeText(getActivity(), "预约成功", Toast.LENGTH_SHORT).show();
        ChannelAreaBean channelAreaBean = mAdapter.getData().get(mOperateOrderPos).getChannelAreaBeanList().get(mOperateOrderSubPos);
        channelAreaBean.setStatus("INIT");
        channelAreaBean.setUserOrderChannelArea(true);
        mAdapter.notifyDataSetChanged();
        //mCommonReqHelper.requestBetQuery();
    }

    @Override
    public void orderFailed(JSONObject jsonObject) {
        try {
            ((BaseActivity) getActivity()).operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelOrderSuccess() {
        Toast.makeText(getActivity(), "预约已取消", Toast.LENGTH_SHORT).show();
        ChannelAreaBean channelAreaBean = mAdapter.getData().get(mOperateOrderPos).getChannelAreaBeanList().get(mOperateOrderSubPos);
        channelAreaBean.setStatus("INIT");
        channelAreaBean.setUserOrderChannelArea(false);
        mAdapter.notifyDataSetChanged();
        //mCommonReqHelper.requestBetQuery();
    }

    @Override
    public void showEmptyMsg() {
        mAdapter.setData(new ArrayList<MatchScheduleBean>());
        mAdapter.notifyDataSetChanged();
        emptyImg.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTimeTabLayout(String nowDate) {
        timeLayout.setNowData(nowDate);
    }

    @Override
    public void refreshList(List<MatchScheduleBean> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setReqBetQuery(List<MatchScheduleBean> data, MatchScheduleEntity matchScheduleEntity) {
        emptyImg.setVisibility(data.size() > 0 ? View.GONE : View.VISIBLE);
        mAllMatchList = data;
        //默认选中全部比赛
        for (MatchScheduleBean matchScheduleBean : data) {
            mSelectedNamesList.add(matchScheduleBean.getMatchName());
        }
        allMatchMap = matchScheduleEntity.getMatchNameAndInnerMatchIdMap();
        matchFiveMap = matchScheduleEntity.getMatchGroupAndInnerMatchIdsMap();
    }

    @Override
    public void startActTimer() {
        startTimer();
    }


    @Override
    public void showProgressDialog(String loadingTxt) {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void showErrorMsg(JSONObject jsonObject) {

    }
}
