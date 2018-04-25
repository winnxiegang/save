package com.android.xjq.controller.live;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.android.banana.commlib.bean.PaginatorBean;
import com.android.banana.pullrecycler.ilayoutmanager.MyLinearLayoutManager;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.banana.pullrecycler.recyclerview.PullRecycler;
import com.android.banana.pullrecycler.recyclerview.onRefreshListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.bean.SubjectComposeListBean2;
import com.android.xjq.bean.SubjectsComposeBean2;
import com.android.xjq.utils.SubjectUtils2;
import com.android.xjq.utils.singleVideo.SinglePlayCallback;
import com.android.xjq.utils.singleVideo.SinglePlayManager;
import com.android.xjq.utils.singleVideo.VideoViewHolder;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2018/1/30.
 */

public class LiveAttitudeController extends BaseLiveController<LiveActivity> implements onRefreshListener, SinglePlayCallback {

    private LiveAttitudeController.ViewHolder mViewHolder;
    private MultiTypeSupportAdapter mAdapter;
    private List<SubjectsComposeBean2> mDatas = new ArrayList<>();
    private int mCurPage = 1;
    public PaginatorBean mPaginator;
    private boolean mEnableLoadMore = false;
    private SinglePlayManager mPlayManager = new SinglePlayManager();

    public LiveAttitudeController(LiveActivity context) {
        super(context);
    }

    @Override
    public void init(View view) {
        if (mViewHolder == null) {
            mViewHolder = new LiveAttitudeController.ViewHolder(view);
        }
        initView();
    }

    private void initView() {
        mViewHolder.pullRecycler.setEmptyView(R.drawable.icon_no_content_about_take_wall_attitude, "", context.getString(R.string.live_attitude_empty));
        mViewHolder.pullRecycler.setLayoutManger(new MyLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MultiTypeSupportAdapter<SubjectsComposeBean2>(context, mDatas, 0, getSupportMultiType()) {
            @Override
            public void onBindNormalHolder(com.android.banana.pullrecycler.multisupport.ViewHolder holder, SubjectsComposeBean2 composeBean2, int position) {
                SubjectUtils2.bindViewHolder(context, holder, composeBean2, position, LiveAttitudeController.this);
            }

            @Override
            public void onItemClick(View view, int position) {
                SubjectUtils2.onItemClick(getActivity(), mDatas.get(position));
            }
        };
        mViewHolder.pullRecycler.setHasFixedSize(true);
        mViewHolder.pullRecycler.setAdapter(mAdapter);
        mViewHolder.pullRecycler.setOnRefreshListener(new onRefreshListener() {
            @Override
            public void onRefresh(boolean refresh) {
                if (refresh) {
                    mCurPage = 1;
                }
                LiveAttitudeController.this.onRefresh(refresh);
            }
        });
    }

    @Override
    public void onRefresh(boolean refresh) {
        if (refresh) {
            mCurPage = 1;
        }
        context.getHttpController().queryMarkedAttitude(mCurPage);
    }

    @Override
    public void responseSuccessHttp(RequestContainer requestContainer, JSONObject jsonObject) {
        SubjectComposeListBean2 composeListBean2 = new Gson().fromJson(jsonObject.toString(), SubjectComposeListBean2.class);
        mPaginator = composeListBean2.paginator;
        loadCompleted(composeListBean2 == null ? null : composeListBean2.topicSimpleList);
    }

    private void loadCompleted(ArrayList<SubjectsComposeBean2> list) {
        mViewHolder.pullRecycler.showContentView();
        //刷新
        if (mCurPage == 1) {
            //空数据
            if ((list == null || list.size() == 0) && mDatas.size() == 0) {
                //设置空白页面,如果刷新之前有数据还是显示旧的数据
                mViewHolder.pullRecycler.showEmptyView();
            } else if (list != null && list.size() > 0) {
                mDatas.clear();
                mDatas.addAll(list);
            }
        } else {
            //加载更多
            if (list != null && list.size() > 0)
                mDatas.addAll(list);
        }
        judeUI(list);
        mViewHolder.pullRecycler.refreshCompleted();
        mCurPage++;
    }

    private void judeUI(List list) {
        //如果是第二页 而且返回的数据条目少于默认数量，则认为没有更多数据了，禁掉加载更多
        if (list == null || mPaginator != null && mPaginator.getPage() >= mPaginator.getPages()) {
            mViewHolder.pullRecycler.setEnableLoadMore(false);
            if (mDatas.size() > 0)//&& mPullRecycler.canScrollVertical()
                mViewHolder.pullRecycler.showOverLoadView();
            else if (mDatas.size() <= 0) {
                mViewHolder.pullRecycler.showEmptyView();
                mViewHolder.pullRecycler.hideOverLoadView();
            }
        }
    }


    public MultiTypeSupport getSupportMultiType() {
        return new MultiTypeSupport<SubjectsComposeBean2>() {
            @Override
            public int getTypeLayoutRes(SubjectsComposeBean2 composeBean2, int pos) {
                return composeBean2.getLayoutViewType();
            }
        };
    }

    @Override
    public void setView() {
        //context.getHttpController().queryMarkedAttitude(mCurPage);
    }

    @Override
    public void onHiddenChanged(boolean isHide) {
        if (isHide) {
            mPlayManager.stopAllVideoHolder();
        } else {
            onRefresh(true);
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPlayStatusChanged(VideoViewHolder videoViewHolder, int playStatus) {
        mPlayManager.onPlayStatusChanged(videoViewHolder, playStatus);
    }


    static class ViewHolder {
        @BindView(R.id.pullRecycler)
        PullRecycler pullRecycler;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
