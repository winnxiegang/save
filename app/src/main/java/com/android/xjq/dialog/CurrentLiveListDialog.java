package com.android.xjq.dialog;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.android.xjq.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.xjq.adapter.main.CurrentLiveAdapter;
import com.android.xjq.bean.live.main.channel.ChildChannelListInfo;
import com.android.xjq.bean.live.main.homelive.ChannelListEntity;
import com.android.xjq.dialog.base.DialogBase;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.banana.commlib.loadmore.LoadMoreListView;
import com.android.banana.commlib.loadmore.OnLoadMoreListener;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
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

public class CurrentLiveListDialog extends DialogBase implements OnHttpResponseListener {


    @BindView(R.id.listView)
    LoadMoreListView listView;
    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;
    @BindView(R.id.contentView)
    LinearLayout contentView;

    private HttpRequestHelper httpRequestHelper;

    private int currentPage = 1;

    private int maxPages;

    private List<ChannelListEntity> channelList = new ArrayList<>();

    private CurrentLiveAdapter mAdapter;

    public CurrentLiveListDialog(Context context) {
        super(context, R.layout.dialog_current_live_list, R.style.dialog_base);

        ButterKnife.bind(this, rootView);

        init();

        requestData();
    }

    private void requestData() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.CHANNEL_LIST_QUERY_BY_ROOM_ID, false);

        map.put("currentPage", currentPage + "");

        map.put("pageSize", String.valueOf(8));

        map.put("roomId", CurLiveInfo.getRoomId() + "");

        httpRequestHelper.startRequest(map, false);
    }

    private void init() {
        httpRequestHelper = new HttpRequestHelper(context, this);

        listView.initBottomView(true);

        mAdapter = new CurrentLiveAdapter(context, channelList);

        listView.setAdapter(mAdapter);

        listView.setEmptyView(emptyLayout);

        listView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (currentPage == maxPages) {

                    listView.getFooterView().showEnd();

                    return;
                }
                currentPage++;

                requestData();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        LogUtils.e("CurrentLiveListDialog","executorSuccess");
        contentView.setVisibility(View.VISIBLE);

        ChildChannelListInfo childChannelListInfo = new Gson().fromJson(jsonObject.toString(), ChildChannelListInfo.class);

        maxPages = childChannelListInfo.getPaginator().getPages();

        List<ChannelListEntity> channelInfos = childChannelListInfo.getChannelInfos();

        channelList.addAll(channelInfos);

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            ((BaseActivity) context).operateErrorResponseMessage(jsonObject);
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
