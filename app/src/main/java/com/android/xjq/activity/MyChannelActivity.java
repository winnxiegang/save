package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.xjq.R;
import com.android.xjq.adapter.main.ChannelAdapter;
import com.android.xjq.bean.live.main.MyChannelInfoBean;
import com.android.xjq.bean.live.main.homelive.ChannelListEntity;

import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/3/7.
 */

public class MyChannelActivity extends BaseActivity implements OnHttpResponseListener {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;
    @BindView(R.id.contentLayout)
    FrameLayout contentLayout;

    private HttpRequestHelper httpRequestHelper;

    private List<ChannelListEntity> list = new ArrayList<>();

    private ChannelAdapter channelAdapter;

    public static void startMyChannelActivity(Activity activity) {

        activity.startActivity(new Intent(activity, MyChannelActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.at_my_channel);

        ButterKnife.bind(this);

        setTitleBar(true, "我的频道",null);

        init();

        getChannelInfoList();

    }

    private void init() {
        listView.setEmptyView(emptyLayout);

        channelAdapter = new ChannelAdapter(this, list);

        listView.setAdapter(channelAdapter);

        httpRequestHelper = new HttpRequestHelper(this, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LiveActivity.startLiveActivity(MyChannelActivity.this,list.get(position).getId());
            }
        });
    }

    private void getChannelInfoList() {

        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.MY_CHANNEL_QUERY, true);

        httpRequestHelper.startRequest(map, true);

    }

    private void responseSuccessMyChannelInfoList(JSONObject jsonObject) {

        contentLayout.setVisibility(View.VISIBLE);

        MyChannelInfoBean myChannelInfoBean = new Gson().fromJson(jsonObject.toString(), MyChannelInfoBean.class);

        myChannelInfoBean.operatorData();

        if (myChannelInfoBean.getChannelInfoSimpleList() != null) {

            list.addAll(myChannelInfoBean.getChannelInfoSimpleList());
        }

        channelAdapter.notifyDataSetChanged();

    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
            case MY_CHANNEL_QUERY:

                responseSuccessMyChannelInfoList(jsonObject);
                break;

        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }
}
