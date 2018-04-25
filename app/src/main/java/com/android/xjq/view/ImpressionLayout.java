package com.android.xjq.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.adapter.dynamic.ImpressionShowAdapter;
import com.android.xjq.bean.dynamic.ImpressionDataBean;
import com.android.xjq.controller.maincontroller.SubjectMultiAdapterCallback;
import com.android.xjq.controller.maincontroller.SubjectMultiAdapterHelper;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaozao on 2018/1/29.
 * 用处：印象标签layout
 */

public class ImpressionLayout extends FrameLayout implements IHttpResponseListener<ImpressionDataBean>, SubjectMultiAdapterCallback {

    private View impressionView;

    private ImageView addImpressionIv;

    private RecyclerView impressionRecyclerView;

    private FrameLayout parentLayout;

    private WrapperHttpHelper httpRequestHelper;

    private ImageView yinXiangIv;

    private ImpressionShowAdapter adapter;

    List<ImpressionDataBean.ImpressionTagSimple> list = new ArrayList<>();

    //设置布局管理器
    private LinearLayoutManager linearLayoutManager;

    private int currentPage = 1;

    private int maxPages = 500;

    private boolean isLoading = false;

    private boolean loadMore = false;

    private String detailObjectId;

    public ImpressionLayout(Context context) {
        this(context, null);
    }

    public ImpressionLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private SubjectMultiAdapterHelper mAdapterHelper;

    public ImpressionLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addView(getView());
        setBackgroundColor(Color.BLUE);
        mAdapterHelper = new SubjectMultiAdapterHelper(this);
    }

    public View getView() {
        if (impressionView == null) {

            impressionView = View.inflate(XjqApplication.getContext(), R.layout.horizon_impressionlayout, null);

            addImpressionIv = (ImageView) impressionView.findViewById(R.id.addImpressionIv);

            yinXiangIv = (ImageView) impressionView.findViewById(R.id.yinXiangIv);

            parentLayout = (FrameLayout) impressionView.findViewById(R.id.parentLayout);

            impressionRecyclerView = (RecyclerView) impressionView.findViewById(R.id.impressionRecyclerView);

            //设置布局管理器
            linearLayoutManager = new LinearLayoutManager(XjqApplication.getContext());

            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            impressionRecyclerView.setLayoutManager(linearLayoutManager);

            adapter = new ImpressionShowAdapter(list);

            impressionRecyclerView.setAdapter(adapter);

            httpRequestHelper = new WrapperHttpHelper(this);
        }

        impressionRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //是否是最后一个显示的item位置
                int lastVisiableItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisiableItemPosition + 1 == adapter.getItemCount()) {
                    if (!isLoading) {
                        currentPage++;
                        if (currentPage > maxPages) {
                            if (!loadMore)
                                return;
                            isLoading = false;
                            loadMore = false;
                            adapter.setLoadMoreLayoutEnable(false);
                            Toast.makeText(XjqApplication.getContext(), "没有更多了", Toast.LENGTH_LONG).show();
                            adapter.notifyItemRemoved(adapter.getItemCount());
                        } else {
                            isLoading = true;
                            getImpressionTag(true);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        adapter.setOnItemClickListener(new ImpressionShowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (list.get(position).liked) {
                    mAdapterHelper.unLike(position, list.get(position).id, "TAG");
                } else {
                    mAdapterHelper.like(position, list.get(position).id, "TAG");
                }
            }
        });
        return impressionView;
    }

    public void setParentLayoutBg(int resId) {
        parentLayout.setBackgroundResource(resId);
    }

    public void setShowAddLayout(boolean showAddLayout, final OnMyClickListener addClickListener) {
        if (showAddLayout && addClickListener != null) {
            addImpressionIv.setVisibility(View.VISIBLE);
            yinXiangIv.setVisibility(View.VISIBLE);
            addImpressionIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addClickListener != null) addClickListener.onClick(v);
                }
            });
        } else {
            addImpressionIv.setVisibility(View.GONE);
            yinXiangIv.setVisibility(View.GONE);
        }
    }


    public void notifyDataView(List<ImpressionDataBean.ImpressionTagSimple> tagSimpleList) {
        if (tagSimpleList == null || tagSimpleList.size() <= 0)
            return;
        if (!loadMore && list.size() > 0)
            list.clear();
        list.addAll(tagSimpleList);
        adapter.notifyDataSetChanged();
    }


    public void setLoadMoreEnable(boolean enable) {
        this.loadMore = enable;
        adapter.setLoadMoreLayoutEnable(loadMore);
    }

    @Override
    public void onSuccess(RequestContainer request, ImpressionDataBean impressionDataBean) {
        switch ((XjqUrlEnum) request.getRequestEnum()) {
            case TAG_PAGE_QUERY:
                isLoading = false;
                maxPages = impressionDataBean.getPaginator().getPages();
                if (impressionDataBean.getList() != null && impressionDataBean.getList().size() > 0) {
                    if (list.size() < impressionDataBean.getPaginator().getItemsPerPage()) {
                        adapter.setLoadMoreLayoutEnable(false);
                    }
                    notifyDataView(impressionDataBean.getList());
                }
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {

    }

    /**
     * http://mapi1.xjq.net/client/service.json?service=TAG_PAGE_QUERY
     * &objectId=转发的对象id
     * &objectType=SUBJECT
     * &currentPage=当前页码&
     * timestamp=1517294378854
     * &authedUserId=8201712118726118
     * &loginKey=USL9924e0bcab7c4d64a6d87fa826e69ffe&sign=9ca89f635ef98106374e8c861b278705
     */
    public void getImpressionTag(boolean loadMoreTag) {
        loadMore = loadMoreTag;
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.TAG_PAGE_QUERY, true);
        map.put("objectId", detailObjectId);//"15622865"
        map.put("objectType", "SUBJECT");
        map.put("currentPage", currentPage);
        httpRequestHelper.startRequest(map, true);
    }

    public void getImpressionTagInit(boolean loadMoreTag, String subjectId) {
        detailObjectId = subjectId;
        currentPage = 1;
        getImpressionTag(loadMoreTag);
    }

    @Override
    public void onLikeOrSetTopResult(JSONObject jsonObject, int position, boolean success, boolean setTopRequest) {

        if (!setTopRequest) {
            ImpressionDataBean.ImpressionTagSimple composeBean = list.get(position);
            composeBean.liked = true;
            composeBean.likeCount++;
            adapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onUnLikeResult(JSONObject jsonObject, int position, boolean success) {

    }
}
