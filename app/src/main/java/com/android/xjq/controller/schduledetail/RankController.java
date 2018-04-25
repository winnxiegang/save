package com.android.xjq.controller.schduledetail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.http.AppParam;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayout;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayoutDirection;
import com.android.banana.view.ScrollListView;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.adapter.schduledatail.JczqRankAdapter;
import com.android.xjq.adapter.schduledatail.JclqAnalysisRankAdapter;
import com.android.xjq.bean.scheduledetail.JclqRankingBean;
import com.android.xjq.bean.scheduledetail.RankCountBean;
import com.android.xjq.utils.XjqUrlEnum;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.banana.commlib.http.AppParam.FT_API_S_URL;

/**
 * Created by zaozao on 2018/2/3.
 * 用处：
 */

public class RankController extends BaseController4JCZJ implements OnHttpResponseListener{
    private ScrollListView rankListView;
    SwipyRefreshLayout refreshLayout;

    private List<RankCountBean> rankList = new ArrayList<>();

    private List<JclqRankingBean> list = new ArrayList<>();

    private JclqAnalysisRankAdapter mJclqRankAdapter;
    private JczqRankAdapter mjczqRankAdapter;
    private JczqAnalysisPointController pointController;//积分榜

    private JczqDataBean jczqDataBean;

    private Context context;

    private boolean isJczq = true;

    private HttpRequestHelper httpRequestHelper;

    public RankController(BaseActivity context, JczqDataBean jczqDataBean, boolean isJczq) {
        this.context = context;
        this.jczqDataBean = jczqDataBean;
        this.isJczq = isJczq;
        mjczqRankAdapter = new JczqRankAdapter(context, rankList,jczqDataBean);
        mJclqRankAdapter = new JclqAnalysisRankAdapter(context, list);
        httpRequestHelper = new HttpRequestHelper(context,this);
    }



    //排名统计查询
    //http://ftapi.huored.net/service.json?raceId=25308033&service=TEAM_RANK_QUERY
    private void getJczqRankData(boolean showPg) {
        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.JCZQ_TEAM_RANK_QUERY, false);
        formBody.setRequestUrl(AppParam.FT_API_DOMAIN + FT_API_S_URL);
        formBody.put("raceId", jczqDataBean.getInnerRaceId());//"25308033"
        httpRequestHelper.startRequest(formBody,showPg);
        if (pointController != null) {
            pointController.getDefaultPoint(showPg);
        }
    }


    /**
     * 获取排名数据
     */
    public void getJclqRankData(boolean showPg) {
        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.JCLQ_TEAM_RANK_QUERY, false);
        formBody.setRequestUrl(AppParam.BT_API_DOMAIN + FT_API_S_URL);
        formBody.put("raceId", jczqDataBean.getInnerRaceId());

        httpRequestHelper.startRequest(formBody,showPg);
    }

    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.layout_schedule_detail_rank);
    }

    public void clearAnimation(){
        if (refreshLayout!=null)
            refreshLayout.clearAnimation();
    }


    @Override
    public void onSetUpView() {
        rankListView = (ScrollListView) findViewOfId(R.id.rankListView);

        if(isJczq){
            pointController = new JczqAnalysisPointController(context,jczqDataBean);
            View pointsView = pointController.getPointsView();
            rankListView.addFooterView(pointsView);
            rankListView.setAdapter(mjczqRankAdapter);
            getJczqRankData(true);
        }else{
            rankListView.setAdapter(mJclqRankAdapter);
            getJclqRankData(true);
        }
        refreshLayout = (SwipyRefreshLayout) findViewOfId(R.id.swipeRefreshLayout);
        refreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
        refreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.main_red1));
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    if(isJczq){
                        getJczqRankData(false);
                    }else{
                        rankListView.setAdapter(mJclqRankAdapter);
                        getJclqRankData(false);
                    }
                }
            }
        });
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        refreshLayout.setRefreshing(false);
        XjqUrlEnum urlEnum = (XjqUrlEnum) requestContainer.getRequestEnum();

        switch (urlEnum){
            case JCZQ_TEAM_RANK_QUERY:
                RankCountBean rankCountBean = new RankCountBean(jsonObject);

                rankCountBean.setDataChanged(true);

                if(rankList!=null&&rankList.size()>0){

                    rankList.clear();
                }
                rankList.add(rankCountBean);

                mjczqRankAdapter.notifyDataSetChanged();
                break;
            case JCLQ_TEAM_RANK_QUERY:

                JclqRankingBean bean = new Gson().fromJson(jsonObject.toString(), JclqRankingBean.class);

                bean.operateData(jczqDataBean.getInnerHomeTeamId(),jczqDataBean.getInnerGuestTeamId());

                list.clear();

                list.add(bean);

                mJclqRankAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            refreshLayout.setRefreshing(false);
            ((BaseActivity)context).operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {
        refreshLayout.setRefreshing(false);
    }
    @Override
    public void showContentView(boolean hiddenChanged) {
        super.showContentView(hiddenChanged);

    }
    @Override
    public void executorFinish() {

    }
}
