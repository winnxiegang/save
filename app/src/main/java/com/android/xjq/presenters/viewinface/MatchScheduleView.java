package com.android.xjq.presenters.viewinface;

import com.android.banana.commlib.base.IMvpView;
import com.android.xjq.bean.MatchScheduleBean;
import com.android.xjq.bean.matchLive.MatchScheduleEntity;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by ajiao on 2018\2\27 0027.
 */

public interface MatchScheduleView extends IMvpView {
    void setTimeTabLayout(String nowDate);
    void refreshList(List<MatchScheduleBean> data);
    void setReqBetQuery(List<MatchScheduleBean> data, MatchScheduleEntity matchScheduleEntity);
    void startActTimer();
    void orderSucces();
    void orderFailed(JSONObject jsonObject);
    void cancelOrderSuccess();
    void showEmptyMsg();
}
