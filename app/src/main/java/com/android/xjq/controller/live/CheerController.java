package com.android.xjq.controller.live;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.view.PageIndicatorView;
import com.android.banana.groupchat.bean.GroupChatTopic1;
import com.android.banana.groupchat.view.baselist.TopicRecyclerView3;
import com.android.xjq.R;


/**
 * Created by lingjiu on 2018/1/31.
 */

public class CheerController extends BaseController4JCZJ<BaseActivity> {

    private TopicRecyclerView3 topicRecyclerView;
    private PageIndicatorView pageIndicatorView;
    private FrameLayout contentView;
    private View emptyTv;

    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.layout_live_zhuwei);
    }

    @Override
    public void onSetUpView() {
        topicRecyclerView = findViewOfId(R.id.topicRecyclerView);
        pageIndicatorView = findViewOfId(R.id.indicatorView);
        topicRecyclerView.attachToIndicatorView(pageIndicatorView);
        contentView = findViewOfId(R.id.contentLayout);
        emptyTv = findViewOfId(R.id.emptyTv);
    }


    public void setMatchData(boolean isFootball, JczqDataBean matchData) {
        topicRecyclerView.setMatch(matchData);
        contentView.setBackgroundResource(isFootball ? R.drawable.icon_header_cheer_ft_bg : R.drawable.icon_header_cheer_bt_bg);
    }

    public void setTopicData(GroupChatTopic1 chatTopic) {
        if (chatTopic.gameBoardList == null || chatTopic.gameBoardList.size() == 0) {
            emptyTv.setVisibility(View.VISIBLE);
            topicRecyclerView.setVisibility(View.GONE);
            pageIndicatorView.setVisibility(View.GONE);
        } else {
            emptyTv.setVisibility(View.GONE);
            topicRecyclerView.setVisibility(View.VISIBLE);
            pageIndicatorView.setVisibility(View.VISIBLE);
            topicRecyclerView.setTopic(chatTopic);
        }
    }
}
