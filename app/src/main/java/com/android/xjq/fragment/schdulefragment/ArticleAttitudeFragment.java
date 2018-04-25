package com.android.xjq.fragment.schdulefragment;

import android.os.Bundle;

import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayoutDirection;
import com.android.xjq.bean.SubjectComposeListBean2;
import com.android.xjq.fragment.TabHomeFragment;
import com.android.xjq.utils.XjqUrlEnum;

public class ArticleAttitudeFragment extends TabHomeFragment {
    private boolean isArticle;
    private String raceId;
    private String raceType;

    public static ArticleAttitudeFragment newInstance(boolean isArticle,String raceId,String raceType) {

        Bundle args = new Bundle();
        args.putBoolean("isArticle", isArticle);
        args.putString("raceId",raceId);
        args.putString("raceType",raceType);
        ArticleAttitudeFragment fragment = new ArticleAttitudeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * http://mapi1.xjq.net/client/service.json?
     * authedUserId=8201711218690398
     * &currentPage=1&
     * loginKey=USL1b7157f2b6bd4b40b8aafb9bdbb920e8&
     * pageSize=1&
     * raceId=4000334507018207890980016647&
     * raceType=FOOTBALL
     * &service=RACE_RELATION_SUBJECT_QUERY
     * &sign=3670752af9eb029a6bbc6bc5c5cf7254&timestamp=1519439783419
     * @param refresh
     *
     * --------某一场赛事下的态度栏目（赛事详情）
    http://mapi1.xjq.net/client/service.json?service=RACE_RELATION_MARKED_ATTITUDE_QUERY
    &raceId=赛事id&
    raceType=赛事类型（FOOTBALL/BASKETBALL)&
    curreantPage=当前页码&
    timestamp=1517294378854
    &authedUserId=8201712118726118&loginKey=USL9924e0bcab7c4d64a6d87fa826e69ffe&sign=9ca89f635ef98106374e8c861b278705
     */

    @Override
    public void onRefresh(boolean refresh) {
        if(onRefreshListener!=null&&refresh){
            onRefreshListener.onRefresh(SwipyRefreshLayoutDirection.TOP);
        }
        RequestFormBody formBody = null;
        if(isArticle){
            formBody = new RequestFormBody(XjqUrlEnum.RACE_RELATION_SUBJECT_QUERY, true);
        }else {
            formBody = new RequestFormBody(XjqUrlEnum.RACE_RELATION_MARKED_ATTITUDE_QUERY, true);
        }
        formBody.put("currentPage", mCurPage);
        formBody.put("raceId", raceId);//"4000334507018207890980016647"
        formBody.put("raceType",raceType);
        formBody.put("pageSize", 20);
        formBody.setGenericClaz(SubjectComposeListBean2.class);
        mHttpHelper.startRequest(formBody);
    }


    @Override
    protected void initData() {
        isArticle = getArguments().getBoolean("isArticle");
        raceId = getArguments().getString("raceId");
        raceType = getArguments().getString("raceType");
    }
}
