
package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.MyTabLayout;
import com.android.banana.groupchat.base.BaseListActivity;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.banana.pullrecycler.recyclerview.DividerItemDecoration;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.InsertMatchBean;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.android.xjq.utils.XjqUrlEnum.BASKETBALL_RACE_QUERY_BY_DATE;
import static com.android.xjq.utils.XjqUrlEnum.FOOTBALL_RACE_QUERY_BY_DATE;

/**
 * Created by qiaomu on 2018/2/28.
 */

public class MatchSelectListActivity extends BaseListActivity<JczqDataBean> implements IHttpResponseListener<InsertMatchBean>, View.OnClickListener {
    MyTabLayout mTabLayout;
    TextView mDateTv;
    TextView mDayTv;

    public static final int SELECTED_REQUEST_CODE = 1000;
    private WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);
    private static final int DAY_PRE = -7;
    private static final int DAY_NEXT = 3;

    private int mCurDay = 0;
    private String mCurrentDate;

    private int mSelectedCount;
    private int mMaxSelectCount;

    private ArrayList<JczqDataBean> btList = new ArrayList<>();
    private ArrayList<JczqDataBean> ftList = new ArrayList<>();

    private ArrayList<JczqDataBean> selectedList = new ArrayList<>();


    public static void startMatchSelectListActivity(Activity activity, int maxSelectCount, ArrayList<JczqDataBean> selectedList) {
        Intent intent = new Intent(activity, MatchSelectListActivity.class);
        intent.putExtra("maxSelectCount", maxSelectCount);
        intent.putParcelableArrayListExtra("selectedList", selectedList);
        activity.startActivityForResult(intent, SELECTED_REQUEST_CODE);
    }

    @Override
    protected void initEnv() {
        mMaxSelectCount = getIntent().getIntExtra("maxSelectCount", 1);
        List<JczqDataBean> preSelectList = getIntent().getParcelableArrayListExtra("selectedList");
        if (preSelectList != null && preSelectList.size() > 0) {
            for (JczqDataBean dataBean : preSelectList) {
                dataBean.setSelected(true);
                selectedList.add(dataBean);
                mSelectedCount++;
            }
        }
    }

    @Override
    public int getContentViewLayoutRes() {
        return R.layout.activity_match_select;
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.item_insert_match;
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(this, R.drawable.base_divider_list_10dp, LinearLayoutManager.VERTICAL, true);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        setLoadMoreEnable(false);

        setUpToolbar(getString(R.string.select_match_title), R.menu.menu_completed, MODE_BACK);

        mTabLayout = (MyTabLayout) findViewById(R.id.tabLayout);
        mDateTv = (TextView) findViewById(R.id.dateTv);
        mDayTv = (TextView) findViewById(R.id.dayTv);
        findViewById(R.id.rightArrowIv).setOnClickListener(this);
        findViewById(R.id.leftArrowIv).setOnClickListener(this);
        changeSelectDate();

        mPullRecycler.setEmptyView(R.drawable.icon_no_content_about_match_schedule_detail, "暂无数据", null);

        mTabLayout.addTabs(getString(R.string.jczq_race), getString(R.string.jclq_race)).setTabMargin(40).setTabSelectedListener(new MyTabLayout.TabSelectedListener() {
            @Override
            public void onTabSelected(MyTabLayout.Tab tab, boolean reSelected) {
                if (reSelected)
                    return;
                if (tab.getPosition() == 1) {
                    if (btList == null || btList.size() == 0) {
                        mPullRecycler.setRefresh();
                        mPullRecycler.showEmptyView("暂无数据");
                    } else {
                        mDatas.clear();
                        loadCompleted(btList);
                    }

                } else {
                    if (ftList == null || ftList.size() == 0) {
                        mPullRecycler.setRefresh();
                        mPullRecycler.showEmptyView("暂无数据");
                    } else {
                        mDatas.clear();
                        loadCompleted(ftList);
                    }
                }
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (mSelectedCount <= 0)
            return false;

        selectedList.clear();
        for (JczqDataBean dataBean : mDatas) {
            if (dataBean.isSelected())
                selectedList.add(dataBean);
        }
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("result_list", selectedList);
        setResult(RESULT_OK, intent);
        this.finish();
        return false;
    }

    @Override
    public void onBindHolder(final ViewHolder holder, final JczqDataBean bean, int position) {
        final int tabPosition = mTabLayout.getSelectedTabPosition();
        if (tabPosition == 1) {
            setJclqMatchShow(holder, position);
        } else {
            setJczqMatchShow(holder, position);
        }

        holder.setText(R.id.matchTypeTv, mTabLayout.getTabAt(tabPosition).getText());
        holder.setText(R.id.issueNoTv, bean.getGameNo());
        holder.setText(R.id.startTimeTv, TimeUtils.formatStringDate(bean.getGmtStart(), TimeUtils.MATCH_FORMAT));
        holder.setText(R.id.matchNameTv, bean.getMatchName());
        holder.setText(R.id.homeNameTv, bean.getHomeTeamName());
        holder.setText(R.id.guestNameTv, bean.getGuestTeamName());

        holder.setImageResource(R.id.selectIv, bean.isSelected() ? R.drawable.icon_contact_selected : R.drawable.icon_contact_normal);

        holder.setOnClickListener(R.id.selectIv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.isSelected()) {
                    removeSelectedWhenClick(tabPosition, bean);
                } else {
                    addSelectedWhenClick(tabPosition, bean);
                }
                bean.setSelected(!bean.isSelected());
                //holder.setImageResource(R.id.selectIv, bean.isSelected() ? R.drawable.icon_contact_selected : R.drawable.icon_contact_normal);

                changeMenuItem();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void changeMenuItem() {
        MenuItem item = mToolbar.getMenu().getItem(0);
        item.setTitle(mSelectedCount == 0 ? getString(R.string.str_completed) : String.format(getString(R.string.match_select_completed), mSelectedCount));
    }

    private void addSelectedWhenClick(int tabPosition, JczqDataBean bean) {
        if (mSelectedCount >= mMaxSelectCount && mMaxSelectCount > 1) {
            ToastUtil.showShort(MatchSelectListActivity.this, getString(R.string.match_select_max_limit));
            return;
        }

        mSelectedCount = mMaxSelectCount > 1 ? mSelectedCount + 1 : 1;

        //不能同时选择足球篮球，清除 足球中所有选中的
        clearPreSelected(tabPosition != 0);
        selectedList.add(bean);
    }

    private void clearPreSelected(boolean clearBasketball) {
        if (mMaxSelectCount == 1) {
            if (selectedList.size() > 0) {
                if (ftList != null) {
                    for (JczqDataBean dataBean : ftList) {
                        dataBean.setSelected(false);
                    }
                }
                if (btList != null) {
                    for (JczqDataBean dataBean : btList) {
                        dataBean.setSelected(false);
                    }
                }
                selectedList.clear();
            }

            return;
        }

        List<JczqDataBean> delete = new ArrayList();
        for (JczqDataBean dataBean : selectedList) {
            if (clearBasketball) {
                if (dataBean.isBasketballRace()) {
                    dataBean.setSelected(false);
                    delete.add(dataBean);
                }
            } else {
                if (!dataBean.isBasketballRace()) {
                    dataBean.setSelected(false);
                    delete.add(dataBean);
                }
            }

        }
        selectedList.removeAll(delete);
    }


    private void removeSelectedWhenClick(int tabPosition, JczqDataBean bean) {
        mSelectedCount--;

        if (tabPosition == 0) {
            selectedList.remove(bean);
        } else {
            selectedList.remove(bean);
        }
    }

    private void setJclqMatchShow(ViewHolder holder, int position) {
        JczqDataBean bean = mDatas.get(position);

        int fullGuestScore = bean.getFullGuestScore();
        int fullHomeScore = bean.getFullHomeScore();

        if ("FINISH".equals(bean.raceStatus.getName())) {
            holder.setText(R.id.fullScoreTv, +fullHomeScore + ":" + fullGuestScore);
            holder.setText(R.id.halfScoreTv, getString(R.string.total_score) + (fullHomeScore + fullGuestScore) + "分" + "  " + getString(R.string.point_spread) + (fullHomeScore - fullGuestScore) + "分");
            holder.setViewVisibility(R.id.halfScoreTv, View.VISIBLE);
        } else {
            holder.setText(R.id.fullScoreTv, "VS");
            holder.setViewVisibility(R.id.halfScoreTv, View.GONE);
        }

        holder.setImageByUrl(this, R.id.guestIconIv, bean.getBTGuestLogoUrl());
        holder.setImageByUrl(this, R.id.homeIconIv, bean.getBTHomeLogoUrl());

    }


    private void setJczqMatchShow(ViewHolder holder, int position) {
        JczqDataBean bean = mDatas.get(position);

        if ("FINISH".equals(bean.raceStatus.getName())) {
            holder.setText(R.id.fullScoreTv, bean.getFullHomeScore() + ":" + bean.getFullGuestScore());
            holder.setText(R.id.halfScoreTv, "半场" + bean.getHalfHomeScore() + ":" + bean.getHalfGuestScore());
            holder.setViewVisibility(R.id.halfScoreTv, View.VISIBLE);
        } else {
            holder.setText(R.id.fullScoreTv, "VS");
            holder.setViewVisibility(R.id.halfScoreTv, View.GONE);
        }

        holder.setImageByUrl(this, R.id.guestIconIv, bean.getFTGuestLogoUrl());
        holder.setImageByUrl(this, R.id.homeIconIv, bean.getFTHomeLogoUrl());

    }


    @Override
    public void onRefresh(boolean refresh) {
        RequestFormBody formBody = new RequestFormBody(mTabLayout.getSelectedTabPosition() == 1 ? BASKETBALL_RACE_QUERY_BY_DATE : FOOTBALL_RACE_QUERY_BY_DATE);
        formBody.put("date", mCurrentDate);
        formBody.put("curPos", mTabLayout.getSelectedTabPosition());
        mHttpHelper.startRequest(formBody, false);
    }

    private void changeSelectDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, mCurDay);
        mCurrentDate = TimeUtils.dateToString(calendar.getTime(), TimeUtils.DATEFORMAT);

        mDateTv.setText(mCurrentDate);
        String day = "星期" + TimeUtils.getDate(mCurrentDate + " 00:00:00");
        mDayTv.setText(day);

    }

    @Override
    public void onSuccess(RequestContainer request, InsertMatchBean matchBean) {
        int requestPos = request.getInt("curPos");
        int curPos = mTabLayout.getSelectedTabPosition();
        int selectCount = 0;
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (requestEnum) {
            case BASKETBALL_RACE_QUERY_BY_DATE:
                btList = matchBean.basketballRaceList;
                if (btList != null && selectedList != null) {
                    for (int i = 0; i < btList.size(); i++) {
                        JczqDataBean dataBean = btList.get(i);
                        dataBean.setIsBasketballRace(true);
                        dataBean.setSelected(selectedList.contains(dataBean));
                        if (dataBean.isSelected()) {
                            selectCount++;
                        }
                    }
                    mSelectedCount = selectCount;
                }

                if (requestPos == curPos) {
                    mDatas.clear();
                    changeMenuItem();
                    loadCompleted(btList);
                }

                break;
            case FOOTBALL_RACE_QUERY_BY_DATE:
                ftList = matchBean.footballRaceList;
                if (ftList != null && selectedList != null) {
                    for (int i = 0; i < ftList.size(); i++) {
                        JczqDataBean dataBean = ftList.get(i);
                        dataBean.setIsBasketballRace(false);
                        dataBean.setSelected(selectedList.contains(dataBean));
                        if (dataBean.isSelected()) {
                            selectCount++;
                        }
                    }
                    mSelectedCount = selectCount;
                }

                if (requestPos == curPos) {
                    mDatas.clear();
                    changeMenuItem();
                    loadCompleted(ftList);
                }
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        operateErrorResponseMessage(jsonObject);
        if (mPullRecycler.isRefreshing()) {
            mPullRecycler.setRefreshing(false);
            loadCompleted(null);
            return;
        }

        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (requestEnum) {
            case BASKETBALL_RACE_QUERY_BY_DATE:
                loadCompleted(null);
                break;
            case FOOTBALL_RACE_QUERY_BY_DATE:
                loadCompleted(null);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.leftArrowIv:
                if (mCurDay == DAY_PRE)
                    return;

                mCurDay--;
                selectedList.clear();
                changeSelectDate();
                mPullRecycler.setRefresh();

                break;
            case R.id.rightArrowIv:
                if (mCurDay == DAY_NEXT)
                    return;

                mCurDay++;
                selectedList.clear();
                changeSelectDate();
                mPullRecycler.setRefresh();
                break;
        }

    }
}
