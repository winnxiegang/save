package com.android.xjq.controller.schduledetail.injury;

import android.app.Activity;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.bean.liveScoreBean.TeamImageUrlUtils;
import com.android.banana.commlib.http.AppParam;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayout;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayoutDirection;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.scheduledetail.InjuryListBean;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kokuma on 2017/8/14.
 */


public class LayOffController extends BaseController4JCZJ implements IHttpResponseListener {

    List listHome;
    List listGuest;
    List listTotal;
    LayOffListView listView;
    SwipyRefreshLayout refreshLayout;
    private JczqDataBean jczqDataBean;

    private WrapperHttpHelper httpHelper;
//    String raceId ; //25248657 足球   435314（篮球）

    public LayOffController(Activity activity,JczqDataBean jczqDataBean) {
        super(activity);
        this.jczqDataBean = jczqDataBean;

        httpHelper = new WrapperHttpHelper(this);
        listHome = new ArrayList();
        listGuest = new ArrayList();
        listTotal = new ArrayList();
    }

    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.layout_lay_off);
    }

    @Override
    public void showContentView(boolean hiddenChanged) {
        super.showContentView(hiddenChanged);
        if (listView != null && (listHome == null || listHome.isEmpty()) && (listGuest == null || listGuest.isEmpty())) {
            getData();
        }
    }

    @Override
    public void onSetUpView() {
        listView = (LayOffListView) findViewOfId(R.id.listView);
        refreshLayout = (SwipyRefreshLayout) findViewOfId(R.id.refreshLayout);
        refreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
        refreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.main_red1));
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    getData();
                }
            }
        });
    }

    public void stopRefresh() {
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
            refreshLayout.clearAnimation();
        }
    }

    @Override
    public void onSetUpData() {
        getData();
    }


    void update() {
        String homeTeamLogoUrl = TeamImageUrlUtils.getFTHomeLogoUrl(jczqDataBean.getInnerHomeTeamId());

        String guestLogoUrl =TeamImageUrlUtils.getFTGuestLogoUrl(jczqDataBean.getInnerGuestTeamId());

        String homeInfo =  jczqDataBean.getHomeTeamName()+"," + homeTeamLogoUrl;
        String guestInfo =  jczqDataBean.getGuestTeamName()+","+ guestLogoUrl;

        listTotal.clear();
        //
        listTotal.add(homeInfo);
        listTotal.add(null);
        if (listHome != null) {
            listTotal.addAll(listHome);
        }
        listTotal.add(guestInfo);
        listTotal.add(null);
        if (listGuest != null) {
            listTotal.addAll(listGuest);
        }
        listView.update(listTotal);
    }

    void getData() {
        if (TextUtils.isEmpty(jczqDataBean.getInnerRaceId())) {
            return;
        }
        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.SEASON_RACE_INJURY_QUERY, true);
        formBody.setRequestUrl(AppParam.FT_API_DOMAIN + AppParam.FT_API_S_URL);
        formBody.put("raceId", jczqDataBean.getInnerRaceId());
        formBody.setGenericClaz(InjuryListBean.class);
        httpHelper.startRequest(formBody);
    }


    @Override
    public void onSuccess(RequestContainer request, Object result) {
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        if (requestEnum == XjqUrlEnum.SEASON_RACE_INJURY_QUERY && result != null) {
            InjuryListBean listBean = (InjuryListBean) result;
            if (listBean != null) {
                listHome = listBean.getHomeTeamInjuryList();
                listGuest = listBean.getGuestTeamInjuryList();
                update();
            }
        }
        stopRefresh();
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        stopRefresh();
        ((BaseActivity)context).operateErrorResponseMessage(jsonObject);
    }

}
