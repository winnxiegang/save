package com.android.xjq.controller.maincontroller;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.bean.CmsInfoListBean;
import com.android.xjq.bean.NewsInfoBean;
import com.android.xjq.bean.SubjectComposeListBean;
import com.android.xjq.bean.injury.BasketballInjuryBean;
import com.android.xjq.bean.injury.FootballInjuryBean;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import static com.android.banana.commlib.http.AppParam.BT_API_DOMAIN;
import static com.android.banana.commlib.http.AppParam.BT_API_S_URL;
import static com.android.banana.commlib.http.AppParam.FT_API_DOMAIN;
import static com.android.banana.commlib.http.AppParam.FT_API_S_URL;

/**
 * Created by zaozao on 2017/11/26.
 * <p>
 * 首页 推荐，咨询 ，精彩大学，伤停请求方法的helper类
 */

class HomeHttpHelper implements IHttpResponseListener {
    private WrapperHttpHelper mHttpHelper;
    private HomeInterface mHttpCallback;

    public HomeHttpHelper(HomeInterface callback) {
        mHttpCallback = callback;
        mHttpHelper = new WrapperHttpHelper(this);
    }

    //资讯活动查询
    public void queryNewsListInfo(int currentPage) {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.NEWS_INFO, true);
        container.put("childChannel", "xjq_activity ");
        container.put("currentPage", currentPage);
        container.setGenericClaz(NewsInfoBean.class);
        mHttpHelper.startRequest(container);
    }

    /**
     * 足球的伤停的网络请求方法
     *
     * @param requestPageIndex 表示请求第几页的数据
     */
    public void queryFootballInjuryInfo(int requestPageIndex) {

        //每页加载的条目数
        int pageSize = 5;

        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.RACE_INFO_AND_INJURY_QUERY, true);
        container.setRequestUrl(FT_API_DOMAIN + FT_API_S_URL);
        container.put("currentPage", requestPageIndex);
        container.put("pageSize", pageSize);
        container.setGenericClaz(FootballInjuryBean.class);
        mHttpHelper.startRequest(container);
    }

    /**
     * 篮球的伤停的网络请求方法
     */
    public void queryBasketballInjuryInfo() {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.INJURY_QUERY, true);
        container.setRequestUrl(BT_API_DOMAIN + BT_API_S_URL);
        container.setGenericClaz(BasketballInjuryBean.class);
        mHttpHelper.startRequest(container);
    }

    //首页轮播图查询
    public void queryBannerInfo() {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.CMS_INFO_QUERY, true);
        container.setGenericClaz(CmsInfoListBean.class);
        mHttpHelper.startRequest(container, true);

    }

    //首页新鲜事
    public void queryTopicRefresh(int curPager) {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.FRESH_TOPIC, true);
        container.put("currentPage", curPager);
        container.setGenericClaz(SubjectComposeListBean.class);
        mHttpHelper.startRequest(container, true);
    }

    //竞彩大学
    public void queryJcdxArticleList(int curPager, boolean elite) {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.JCDX_ARTICLE, true);
        container.put("currentPage", curPager);
        container.put("setElite", elite);
        container.setGenericClaz(SubjectComposeListBean.class);
        mHttpHelper.startRequest(container, true);
    }

    public void queryJcdxArticleRecommandList(int curPager, boolean elite) {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.EDITOR_RECOMMEND, true);
        container.put("currentPage", curPager);
        container.put("setElite", elite);
        container.put("recommendType", "ARTICLE");
        container.put("queryRecommend", 1);
        container.put("tagGroupCodes", "BACKSTAGE_MANAGE,ARTICLE");
        container.setGenericClaz(SubjectComposeListBean.class);
        mHttpHelper.startRequest(container, true);
    }

    @Override
    public void onSuccess(RequestContainer request, Object object) {
        if (mHttpCallback == null)
            return;
        mHttpCallback.responseSuccessHttp(object, request);
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        if (mHttpCallback == null)
            return;
        mHttpCallback.responseFalseHttp(jsonObject, request);
    }
}
