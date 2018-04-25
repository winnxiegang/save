package com.android.xjq.activity.myzhuwei;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayout;
import com.android.banana.commlib.view.swipyrefreshlayout.SwipyRefreshLayoutDirection;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.myzhuwei.widget.MyZhuweiListView;
import com.android.xjq.bean.myzhuwei.ZhuweiDetailBean.GameBoardBean;
import com.android.xjq.bean.myzhuwei.ZhuweiDetailBean.PurchaseOrderBean;
import com.android.xjq.bean.myzhuwei.ZhuweiListBean;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kokuma on 2017/11/4.
 */

public class MyZhuweiActivity extends BaseActivity4Jczj implements IHttpResponseListener {

    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;

    MyZhuweiListView  listview;
    View viewNodata;

    private WrapperHttpHelper httpHelper;
    private int currentPage=1;
    List<PurchaseOrderBean>  listPurchaseOrder;

    public static void startMyZhuweiActivity(Context context) {
        Intent intent = new Intent(context, MyZhuweiActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onSuccess(RequestContainer request, Object result) {
        stopRefresh();
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        if (requestEnum == XjqUrlEnum.PURCHASE_ORDER_SUMMARY_QUERY_BY_USER && result != null) {
            ZhuweiListBean bean = (ZhuweiListBean) result;
            if (bean != null && bean.isSuccess()) {
                if(bean.getPaginator()!=null&&bean.getPaginator().getPages()<currentPage){
                    return;
                }
               if(currentPage==1){
                   listPurchaseOrder.clear();
               }
                hanlderData(bean);
            }
            listview.update(listPurchaseOrder);
        }
        setViewNodata();
    }

    void  hanlderData(ZhuweiListBean bean){
        Map<String,GameBoardBean> gameBoardMap = bean.getGameBoardMap();
        for(PurchaseOrderBean data : bean.getPurchaseOrderSummaryClientSimpleList()){
            if (gameBoardMap!=null) {
                data.gameBoardBean = gameBoardMap.get(data.getEntryBizId());
            }
            listPurchaseOrder.add(data);
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        stopRefresh();
        setViewNodata();
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_zhuwei_list);
        ButterKnife.bind(this);
    }


    @Override
    protected void setUpView() {
        viewNodata = findViewById(R.id.viewNodata);
        listview = (MyZhuweiListView) findViewById(R.id.listview);
        refreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    currentPage =1;
                    getData();
                }else if (direction == SwipyRefreshLayoutDirection.BOTTOM){
                    currentPage++;
                    getData();
                }
            }
        });
    }

    @Override
    protected void setUpData() {
        currentPage =1;
        setUpToolbar(getString(R.string.my_zhuwei), -1, MODE_BACK);
        httpHelper = new WrapperHttpHelper(this);
        listPurchaseOrder = new ArrayList<>();
        getData();
    }

    void getData(){
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.PURCHASE_ORDER_SUMMARY_QUERY_BY_USER, true);
        container.put("currentPage", currentPage);
        container.put("pageSize", "20");
        container.setGenericClaz(ZhuweiListBean.class);
        httpHelper.startRequest(container, false);
    }

    void setViewNodata(){
        if(listview.getData().size()>0){
            viewNodata.setVisibility(View.GONE);
        }else {
            viewNodata.setVisibility(View.VISIBLE);
        }

    }
    public void stopRefresh() {
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
            refreshLayout.clearAnimation();
        }
    }
}
