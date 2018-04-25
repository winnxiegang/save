package com.android.xjq.controller;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.xjq.R;
import com.android.xjq.adapter.main.AudienceListViewAdapter;
import com.android.xjq.bean.live.ChannelUserEntity;
import com.android.banana.commlib.LoginInfoHelper;

import java.util.ArrayList;
import java.util.List;

import static com.android.xjq.dialog.CurrentAudienceDialog.AUDIENCE_TAB;
import static com.android.xjq.dialog.CurrentAudienceDialog.MIC_TAB;

/**
 * Created by Admin on 2017/3/7.
 */

public class AudienceListController {


    private Context context;

    private List<ChannelUserEntity.ChannelUserBean> channelUserList = new ArrayList<>();

    private List<ChannelUserEntity.ChannelUserBean> channelMicList = new ArrayList<>();

    private AudienceListViewAdapter userAdapter;

    private AudienceListViewAdapter micAdapter;

    public AudienceListController(Context context) {
        this.context = context;
    }

    public void setChannelUserList(List<ChannelUserEntity.ChannelUserBean> list) {
        channelUserList.clear();

        if (list != null && list.size() > 0) {

//            sortList(list);

            channelUserList.addAll(list);
        }

        userAdapter.notifyDataSetChanged();
    }

    public void setChannelMicList(List<ChannelUserEntity.ChannelUserBean> list) {
        channelMicList.clear();

        if (list != null && list.size() > 0) {

            channelMicList.addAll(list);
        }

        micAdapter.notifyDataSetChanged();
    }

    public List<View> getView() {
        List<View> mViews = new ArrayList<>();

        mViews.add(getAudienceTabView());

        mViews.add(getMicTabView());

        return mViews;
    }

    private View getMicTabView() {
        View view = View.inflate(context, R.layout.current_audience_content, null);

        ListView listView = (ListView) view.findViewById(R.id.listView);

        View emptyView = view.findViewById(R.id.emptyLayout);

        ((ImageView) emptyView.findViewById(R.id.emptyIv)).setImageResource(R.drawable.icon_no_content_about_match_schedule_detail);

        micAdapter = new AudienceListViewAdapter(context, channelMicList, MIC_TAB);

        listView.setEmptyView(emptyView);

        listView.setAdapter(micAdapter);
        return view;
    }

    private View getAudienceTabView() {
        View view = View.inflate(context, R.layout.current_audience_content, null);

        View micHandleLayout = view.findViewById(R.id.micHandleLayout);

        ListView listView = (ListView) view.findViewById(R.id.listView);

        micHandleLayout.setVisibility(View.GONE);

        userAdapter = new AudienceListViewAdapter(context, channelUserList, AUDIENCE_TAB);

        listView.setAdapter(userAdapter);
        return view;
    }

    private void sortList(List<ChannelUserEntity.ChannelUserBean> list) {
        //排序,将自己排序到第一个
        ChannelUserEntity.ChannelUserBean selfUser = null;
        for (ChannelUserEntity.ChannelUserBean channelUserBean : list) {
            if (channelUserBean.getUserId() != null && channelUserBean.getUserId().equals(LoginInfoHelper.getInstance().getUserId())) {
                selfUser = channelUserBean;
                list.remove(channelUserBean);
                break;
            }
        }
        if (selfUser != null) {
            list.add(0, selfUser);
        }
    }
}
