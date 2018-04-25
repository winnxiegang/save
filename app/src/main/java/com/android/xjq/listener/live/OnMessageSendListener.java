package com.android.xjq.listener.live;

import com.android.xjq.bean.medal.MedalInfoBean;

import java.util.List;

/**
 * Created by zhouyi on 2017/4/6.
 */

public interface OnMessageSendListener {

    void onMessageSendSuccess(List<String> roleCodesList, List<String> fontColorList, String userVotedContent, List<MedalInfoBean> medalInfoBeanList);

    void onMessageSendFailed();
}
