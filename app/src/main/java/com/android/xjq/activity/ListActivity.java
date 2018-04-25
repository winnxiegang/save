package com.android.xjq.activity;

/**
 * Created by kokuma on 2017/11/28.
 */

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.PaginatorBean;
import com.android.banana.commlib.utils.RefreshEmptyViewHelper;
import com.android.banana.commlib.utils.toast.ToastUtil;

import java.util.List;

/**
 * Created by zhouyi on 2016/6/14 21:42.
 */
public abstract class ListActivity extends BaseActivity {

    protected RefreshEmptyViewHelper mRefreshHelper;

    private Pagers mCurrentPagers;

    protected RefreshEmptyViewHelper.Builder getRefreshHelperBuilder() {

        RefreshEmptyViewHelper.Builder builder = new RefreshEmptyViewHelper.Builder();

        builder.setListener(new RefreshEmptyViewHelper.OnRefreshListener() {

            @Override
            public void onRefresh() {

                mRequestType = REFRESH;

                mCurrentPagers.currentPages = DEFAULT_PAGE;

                loadDataFromNetwork();

            }

            @Override
            public void onLoadMore() {

                mRequestType = LOAD_MORE;

                if (mCurrentPagers.currentPages == mCurrentPagers.maxPages) {

                    closeRefresh();

                    ToastUtil.showLong(ListActivity.this.getApplicationContext(),"已经到最后一页了!!");

                } else {

                    mCurrentPagers.currentPages++;

                    loadDataFromNetwork();

                }
            }

            @Override
            public void onEmptyRefresh() {

            }
        });

        return builder;
    }

    protected void closeRefresh() {

        mRefreshHelper.closeRefresh();

    }

    protected void changePagers(Pagers pagers){
        mCurrentPagers = pagers;
    }

    protected void resetRefresh() {

        mCurrentPagers.currentPages = DEFAULT_PAGE;

        mRequestType = REFRESH;

    }

    /**
     * 如果当前是刷新数据，则清空数组，如果下拉加载更多，则在原有基础上添加更多内容
     *
     * @param sourceList 原始数据
     * @param newList    新的数据
     * @param <E>
     */
    protected <E> void formatList(List<E> sourceList, List<E> newList) {

        if (mRequestType == REFRESH) {
            sourceList.clear();
        }
        if(newList!=null){

            sourceList.addAll(newList);
        }

    }

    /**
     * 通过下来刷新更多或者上来加载更多从服务端加载数据
     */
    protected abstract void loadDataFromNetwork();

    /**
     * 分页器属性
     *
     * @param
     */
    public static class Pagers {

        public int maxPages = 1;//最大页数

        public int currentPages = 1;//当前页数

        public void changeMaxPagers(PaginatorBean bean){
            if(bean!= null){
                if(bean.getPages()!=maxPages){
                    maxPages = bean.getPages();
                }
            }
        }
        public void changeCurrentPage(int currentPage){
            currentPages = currentPage;
        }
    }
}
