package com.android.xjq.controller.schduledetail.injury;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.android.xjq.adapter.schduledatail.JclqInjuryAdapter;
import com.android.xjq.bean.scheduledetail.JclqInjuryBean;
import com.android.xjq.utils.XjqUrlEnum;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaozao on 2018/2/6.
 * 用处：篮球伤停
 */

public class JclqInjuryController extends BaseController4JCZJ implements OnHttpResponseListener{

    private Context context;

    private HttpRequestHelper homeHttpHelper;

    private JclqInjuryAdapter adapter;

    private SwipyRefreshLayout swipeRefreshLayout;

    private TextView tvEmpty;

    private ScrollListView injuryListView;

    private JczqDataBean jczqDataBean;

    private List<JclqInjuryBean.PlayInjuryBean> list = new ArrayList<>();

//    435314（篮球）

    public JclqInjuryController(Context context,JczqDataBean jczqDataBean) {
        this.context = context;
        this.jczqDataBean = jczqDataBean;
    }

    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.layout_schedule_detail_rank);
    }

    @Override
    public void showContentView(boolean hiddenChanged) {
        super.showContentView(hiddenChanged);
    }
    @Override
    public void onSetUpView() {

        homeHttpHelper = new HttpRequestHelper(context,this);
        adapter = new JclqInjuryAdapter(context,list);

        swipeRefreshLayout = (SwipyRefreshLayout) findViewOfId(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.main_red1));

        tvEmpty = (TextView) findViewOfId(R.id.emptyTv);
        injuryListView = (ScrollListView) findViewOfId(R.id.rankListView);
        injuryListView.setAdapter(adapter);
        getInjuryData();
        swipeRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                getInjuryData();
            }
        });
    }
    public void clearAnimation(){
        if (swipeRefreshLayout!=null)
        swipeRefreshLayout.clearAnimation();
    }
    private void getInjuryData(){
        RequestFormBody requestFormBody = new RequestFormBody(XjqUrlEnum.RACE_INJURY_QUERY,false);
        requestFormBody.setRequestUrl(AppParam.BT_API_DOMAIN + AppParam.FT_API_S_URL);
        requestFormBody.put("raceId", jczqDataBean.getInnerRaceId());
        homeHttpHelper.startRequest(requestFormBody);
    }

    /**
     * 是否显示空
     */
    private void showEmpty(boolean empty) {
        if (empty) {
            tvEmpty.setVisibility(View.VISIBLE);
            injuryListView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            injuryListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        swipeRefreshLayout.setRefreshing(false);

        JclqInjuryBean jclqInjuryBean = new Gson().fromJson(jsonObject.toString(),JclqInjuryBean.class);

        jclqInjuryBean.operatorData(jczqDataBean);

        if(list.size()>0){
            list.clear();
        }

        list.addAll(jclqInjuryBean.getInjuryBeanList());
        //没有数据显示空
        if (list.size() == 0) {
            showEmpty(true);
        } else {
            showEmpty(false);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        swipeRefreshLayout.setRefreshing(false);
        try {
            ((BaseActivity)context).operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void executorFinish() {

    }
}
