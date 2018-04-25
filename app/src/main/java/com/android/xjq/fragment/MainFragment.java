package com.android.xjq.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.AppParam;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.view.CommonStatusLayout;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.adapter.main.MainAdapter;
import com.android.xjq.adapter.main.RefreshHelper;
import com.android.xjq.bean.live.main.homelive.ChannelListBean;
import com.android.xjq.bean.live.main.homelive.ChannelListEntity;
import com.android.xjq.controller.BannerController;
import com.android.xjq.model.RefreshLayoutDirection;
import com.android.xjq.utils.XjqUrlEnum;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lumenghz.com.pullrefresh.PullToRefreshView;

import static com.android.xjq.utils.XjqUrlEnum.CHANNEL_LIST_QUERY;

/**
 * Created by zhouyi on 2017/3/3.
 * 直播
 */

public class MainFragment extends BaseFragment implements OnHttpResponseListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    PullToRefreshView refreshLayout;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.statusLayout)
    CommonStatusLayout statusLayout;


    private MainAdapter mAdapter;
    private HttpRequestHelper httpRequestHelper;
    private int currentPage = DEFAULT_PAGE;
    private int maxPages;

    private List<ChannelListEntity> channelList = new ArrayList<>();
    private RefreshHelper refreshHelper;
    private BannerController bannerController;
    private HeadViewHolder headViewHolder;
    private boolean isFirstRequest;

    @Override
    protected void initData() {
        httpRequestHelper = new HttpRequestHelper(getContext(), this);
        requestData();
        isFirstRequest = true;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragemnt_main, null);
        ButterKnife.bind(this, view);
        setRecyclerView();
        //activity.compat(headLayout);
        titleTv.setText("直播");
        return view;
    }

    private void requestData() {

        List<RequestContainer> list = new ArrayList<>();

        //list.add(getBannerData());

        list.add(getChannelList());

        httpRequestHelper.startRequest(list, false);
    }

    /**
     * 首页列表数据获取
     *
     * @return
     */
    private RequestContainer getChannelList() {

        XjqRequestContainer map = new XjqRequestContainer(CHANNEL_LIST_QUERY, false);

        map.put("currentPage", String.valueOf(currentPage));

        map.put("pageSize", String.valueOf(8));

        return map;
    }

    /**
     * banner数据获取
     *
     * @return
     */
    private RequestContainer getBannerData() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.CLIENT_BANNER_QUERY, false);

        return map;
    }

    private void setRecyclerView() {

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new MainAdapter(getActivity(), channelList, null);

      /*  bannerController = new BannerController(getActivity(), headViewHolder.bannerView,
                headViewHolder.ads_indicator, headViewHolder.mainBannerLL, headViewHolder.bannerDefault);*/

        setEmpty();
        //gridLayoutManager.getSpanCount()可让其独占一行
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                return mAdapter.isHeader(position) ? gridLayoutManager.getSpanCount() : (mAdapter.isFooter(position) ? gridLayoutManager.getSpanCount() : 1);
            }
        });

        recyclerView.setAdapter(mAdapter);

        refreshHelper = new RefreshHelper(refreshLayout, RefreshLayoutDirection.BOTH, new RefreshHelper.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = DEFAULT_PAGE;

                requestType = AppParam.REFRESH_DATA;

                requestData();

                isFirstRequest = true;
            }

            @Override
            public void onLoadMore() {
                LogUtils.e(TAG, "onLoadMore");
                if (currentPage == maxPages) {

                    mAdapter.showLoadMoreEnd();

                    return;
                }
                currentPage++;

                requestType = AppParam.LOAD_MORE_DATA;

                httpRequestHelper.startRequest(getChannelList(), false);
            }
        });

    }

    private void setEmpty() {
        View emptyView = View.inflate(getActivity(), R.layout.layout_empty_view, null);
        ((ImageView) emptyView.findViewById(R.id.emptyIv)).setImageResource(R.drawable.icon_no_live);
        ((TextView) emptyView.findViewById(R.id.emptyTv)).setText(getString(R.string.no_live));
        mAdapter.setEmptyView(emptyView);
    }

    private View addHeader() {

        View view = View.inflate(getActivity(), R.layout.item_main_header, null);

        headViewHolder = new HeadViewHolder(view);

        return view;
    }

    private void handleChannelListQuery(JSONObject jsonObject) {
        statusLayout.hideStatusView();

        ChannelListBean channelListBean = new Gson().fromJson(jsonObject.toString(), ChannelListBean.class);

        channelListBean.operatorData();

        maxPages = channelListBean.getPaginator().getPages();

        if (requestType.equals(AppParam.REFRESH_DATA)) {
            channelList.clear();
        }

        channelList.addAll(channelListBean.getResultList());

        separateRepeatElement(channelList);

        mAdapter.notifyDataSetChanged();
    }

    //部分直播间会出现重复的情况
    private void separateRepeatElement(List<ChannelListEntity> channelList) {
        if (channelList == null || channelList.size() == 0) return;
        for (int i = 0; i < channelList.size() - 1; i++) {
            for (int j = channelList.size() - 1; j > i; j--) {
                if (channelList.get(j).getId() == channelList.get(i).getId()) {
                    channelList.remove(j);
                }
            }
        }
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        switch ((XjqUrlEnum) requestContainer.getRequestEnum()) {
            case CHANNEL_LIST_QUERY:
                handleChannelListQuery(jsonObject);
                break;
            case CLIENT_BANNER_QUERY:
                //bannerController.handleData(jsonObject, requestType);
                break;
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            ((BaseActivity) getActivity()).operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {
        if (isFirstRequest)
            statusLayout.showRetry();
        mAdapter.showLoadMoreRetry();
    }

    @Override
    public void executorFinish() {
        isFirstRequest = false;
        refreshHelper.closeRefresh(requestType);
    }

    static class HeadViewHolder {
       /* @BindView(R.id.bannerView)
        ImageCycleView bannerView;

        @BindView(R.id.bannerDefault)
        ImageView bannerDefault;

        @BindView(R.id.ads_indicator)
        LinearLayout ads_indicator;

        @BindView(R.id.bannerTitleLL)
        LinearLayout bannerTitleLL;

        @BindView(R.id.mainBanner)
        FrameLayout mainBannerLL;*/

        HeadViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void onDestroy() {
        httpRequestHelper.onDestroy();
        refreshLayout.onDestroy();
       /* if (headViewHolder != null && headViewHolder.bannerView != null) {
            headViewHolder.bannerView.pushImageCycle();
        }*/
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        refreshLayout.setRefreshing(false);
        /*if (headViewHolder != null && headViewHolder.bannerView != null) {
            headViewHolder.bannerView.pushImageCycle();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (headViewHolder != null && headViewHolder.bannerView != null) {
            headViewHolder.bannerView.startImageCycle();
        }*/
    }
}
