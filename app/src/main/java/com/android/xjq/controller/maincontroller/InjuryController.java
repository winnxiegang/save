package com.android.xjq.controller.maincontroller;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.adapter.main.InjuryAdapter;
import com.android.xjq.bean.injury.BasketballInjuryBean;
import com.android.xjq.bean.injury.FootballInjuryBean;
import com.android.xjq.model.injury.BasketballPlayer;
import com.android.xjq.model.injury.FootballPlayer;
import com.android.xjq.view.LoadMoreRecyclerView;
import com.android.xjq.view.OvalTabView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 首页伤停
 */

public class InjuryController extends BaseController4JCZJ<BaseActivity> implements HomeInterface<Object> {

    public static final int CATEGORY_FOOTBALL = 0, CATEGORY_BASKETBALL = 1;

    private OvalTabView.OnTabChangedListener onTabChangedListener;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    private LoadMoreRecyclerView.OnLoadMoreListener onLoadMoreListener;

    //当前显示的分类，足球和篮球
    private int curCategory = CATEGORY_FOOTBALL;
    private HomeHttpHelper homeHttpHelper;
    private InjuryAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OvalTabView ovalTabView;
    private TextView tvUpdateTime;
    private TextView tvEmpty;
    private LoadMoreRecyclerView recyclerView;
    private TextView tvTitleScore;

    //当前已经加载完成的页码，第一页为1
    private int curPageIndex;
    private int pageCount;

    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.layout_home_injury);
    }

    private void initListeners() {
        onTabChangedListener = new OvalTabView.OnTabChangedListener() {
            @Override
            public void onChanged(int oldPosition, int newPosition) {
                changeCategory(newPosition);
            }
        };

        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if (curCategory == CATEGORY_FOOTBALL) {
                    curPageIndex = 0;
                    recyclerView.setNoData(false);
                    homeHttpHelper.queryFootballInjuryInfo(1);
                } else {
                    homeHttpHelper.queryBasketballInjuryInfo();
                }
            }
        };

        onLoadMoreListener = new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (curCategory == CATEGORY_FOOTBALL) {
                    homeHttpHelper.queryFootballInjuryInfo(curPageIndex + 1);
                }
            }
        };
    }

    @Override
    public void onSetUpView() {

        initListeners();

        homeHttpHelper = new HomeHttpHelper(this);
        adapter = new InjuryAdapter(getContext(), new ArrayList<FootballPlayer>(), new ArrayList<BasketballPlayer>());

        recyclerView = findViewOfId(R.id.rv_injury_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnLoadMoreListener(onLoadMoreListener);
        recyclerView.addItemDecoration(new InjuryDecoration());

        swipeRefreshLayout = findViewOfId(R.id.srl_injury);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.main_red1));
        onRefreshListener.onRefresh();

        tvEmpty = findViewOfId(R.id.tv_empty);

        ovalTabView = findViewOfId(R.id.tab_injury_switcher);
        List<String> tagList = new ArrayList<>();
        tagList.add("足球");
        tagList.add("篮球");
        ovalTabView.setOvalColor(getResouce().getColor(R.color.main_red1));
        ovalTabView.setTextColor(getResouce().getColor(R.color.main_red1));
        ovalTabView.setTextSize(14);
        ovalTabView.setTagList(tagList);
        ovalTabView.setSelectedPosition(0);
        ovalTabView.setOnTabChangedListener(onTabChangedListener);

        tvUpdateTime = findViewOfId(R.id.tv_injury_update_time);

        tvTitleScore = findViewOfId(R.id.tv_title_score);
    }

    @Override
    public void responseSuccessHttp(Object jo, RequestContainer requestContainer) {
        //足球数据
        if (curCategory == CATEGORY_FOOTBALL && jo instanceof FootballInjuryBean) {
            curPageIndex++;
            FootballInjuryBean footballInjuryBean = (FootballInjuryBean) jo;
            int requestPageIndex = Integer.parseInt(requestContainer.getFiledsMap().get("currentPage"));
            if (requestPageIndex == 1) {
                pageCount = footballInjuryBean.paginator.getPages();
                adapter.getFootballPlayerList().clear();
                adapter.getFootballPlayerList().addAll(FootballPlayer.parseBean(footballInjuryBean));
                adapter.notifyDataSetChanged();
            } else {
                List<FootballPlayer> footballPlayers = FootballPlayer.parseBean(footballInjuryBean);
                if (footballPlayers.size() > 0) {
                    adapter.getFootballPlayerList().addAll(footballPlayers);
                    recyclerView.setLoadMoreFinished(footballPlayers.size());
                }
            }
            //已经加载到最后一页
            if (requestPageIndex >= pageCount) {
                recyclerView.setNoData(true);
            }

            //设置更新时间
            setUpdateTime(footballInjuryBean.nowDate);

            //没有数据显示空
            if (adapter.getFootballPlayerList().size() == 0) {
                showEmpty(true);
            } else {
                showEmpty(false);
            }

            //篮球数据
        } else if (curCategory == CATEGORY_BASKETBALL && jo instanceof BasketballInjuryBean) {
            BasketballInjuryBean basketballInjuryBean = (BasketballInjuryBean) jo;
            adapter.getBasketballPlayerList().clear();
            adapter.getBasketballPlayerList().addAll(BasketballPlayer.parseBean(basketballInjuryBean));
            adapter.notifyDataSetChanged();

            //设置更新时间
            setUpdateTime(basketballInjuryBean.nowDate);

            //没有数据显示空
            if (adapter.getBasketballPlayerList().size() == 0) {
                showEmpty(true);
            } else {
                showEmpty(false);
            }
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void responseFalseHttp(JSONObject jo, RequestContainer requestContainer) {
        swipeRefreshLayout.setRefreshing(false);
        if (curCategory == CATEGORY_FOOTBALL && requestContainer.getGenericClaz() == FootballInjuryBean.class) {
            //没有数据显示空
            if (adapter.getFootballPlayerList().size() == 0) {
                showEmpty(true);
            } else {
                showEmpty(false);
                recyclerView.setLoadMoreError();
            }
        } else if (curCategory == CATEGORY_BASKETBALL && requestContainer.getGenericClaz() == BasketballInjuryBean.class) {
            //没有数据显示空
            if (adapter.getBasketballPlayerList().size() == 0) {
                showEmpty(true);
            } else {
                showEmpty(false);
            }
        }
    }

    /**
     * 改变标签分组
     */
    private void changeCategory(int newTabPosition) {
        switch (newTabPosition) {
            case 0:
                curCategory = CATEGORY_FOOTBALL;
                recyclerView.setLoadMoreEnable(true);
                tvTitleScore.setText("进球");
                adapter.setCategory(CATEGORY_FOOTBALL);
                break;
            case 1:
                curCategory = CATEGORY_BASKETBALL;
                recyclerView.setLoadMoreEnable(false);
                tvTitleScore.setText("场均得分");
                adapter.setCategory(CATEGORY_BASKETBALL);
                break;
        }
        onRefreshListener.onRefresh();
    }

    /**
     * 是否显示空
     */
    private void showEmpty(boolean empty) {
        if (empty) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setUpdateTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date date = simpleDateFormat.parse(time);
            simpleDateFormat = new SimpleDateFormat("MM/dd", Locale.CHINA);
            tvUpdateTime.setText(simpleDateFormat.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class InjuryDecoration extends RecyclerView.ItemDecoration {

        private Paint paint;

        InjuryDecoration() {
            paint = new Paint();
            paint.setColor(getResouce().getColor(R.color.colorGray10));
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                if (shouldDrawDecoration(child, parent)) {
                    c.drawRect(child.getLeft(), child.getBottom(), child.getRight(), child.getBottom() + 50, paint);
                }
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (shouldDrawDecoration(view, parent)) {
                outRect.bottom = 50;
            }
        }

        private boolean shouldDrawDecoration(View view, RecyclerView parent) {
            int position = parent.getChildAdapterPosition(view);
            if (curCategory == CATEGORY_FOOTBALL) {
                if (position == adapter.getFootballPlayerList().size() - 1) {
                    return true;
                }
                if (position < adapter.getFootballPlayerList().size()) {
                    FootballPlayer thisPlayer = adapter.getFootballPlayerList().get(position);
                    FootballPlayer nextPlayer = adapter.getFootballPlayerList().get(position + 1);
                    if (thisPlayer.getIsHost() == 0 && nextPlayer.getIsHost() == 1) {
                        return true;
                    }
                }
            } else {
                if (position == adapter.getBasketballPlayerList().size() - 1) {
                    return true;
                }
                if (position < adapter.getBasketballPlayerList().size()) {
                    BasketballPlayer thisPlayer = adapter.getBasketballPlayerList().get(position);
                    BasketballPlayer nextPlayer = adapter.getBasketballPlayerList().get(position + 1);
                    if (thisPlayer.getTeamId() != nextPlayer.getTeamId()) {
                        return true;
                    }
                }
            }
            return false;
        }

    }

}
