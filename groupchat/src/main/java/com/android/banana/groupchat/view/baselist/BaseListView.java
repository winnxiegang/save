package com.android.banana.groupchat.view.baselist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;


import com.android.banana.groupchat.view.baselist.base.BaseBindManager;
import com.android.banana.groupchat.view.baselist.base.BaseViewHolder;
import com.android.banana.groupchat.view.baselist.base.CustomAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kokuma on 2017/3/24.
 */
public abstract class BaseListView<T> extends ListView implements AdapterView.OnItemClickListener,AbsListView.OnScrollListener {


    public View baseHeader;
    protected List<T> data;
    protected BaseBindManager bindManager;
    public View  baseFooter;


    public boolean autoMesure;  //用来改变onMeasure 的方式
    int scrolly;
    public MyOnScrollListener myOnScrollListener;

    public BaseListView(Context context) {
        super(context);
    }

    public BaseListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseListView initUI(int resourceId) {
        BaseBindManager.init(getContext().getApplicationContext());
        setSelector(android.R.color.transparent);
        // EventBus.getDefault().register(this);
        setOnItemClickListener(this);
        setOnScrollListener(this);
        bindManager = BaseBindManager.getInstance();
        this.data = new ArrayList<>();
        CustomAdapter customAdapter = new CustomAdapter<T>(getContext(), resourceId, data) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = BaseListView.this.getView(position, convertView, parent);
                if (v != null) {
                    return v;
                }
                return super.getView(position, convertView, parent);
            }

            @Override
            public void setConvert(BaseViewHolder viewHolder, T bean) {
                BaseListView.this.setConvert(viewHolder, bean);
            }

            @Override
            public int getViewTypeCount() {
                if (BaseListView.this.getViewTypeCount() > 1) {
                    return BaseListView.this.getViewTypeCount();
                }
                return super.getViewTypeCount();
            }

            @Override
            public int getItemViewType(int position) {
                if (BaseListView.this.getItemViewType(position) >= 0) {
                    return BaseListView.this.getItemViewType(position);
                }
                return super.getItemViewType(position);
            }
        };
        setAdapter(customAdapter);
        setVerticalScrollBarEnabled(false);
        return this;
    }

    protected abstract void setConvert(BaseViewHolder viewHolder, T bean);

    protected boolean isSelfAdaptive() {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState){
        //
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount){
        //
    }

    protected int getViewTypeCount() {
        return 1;
    }

    protected int getItemViewType(int position) {
        return -1;
    }

    protected View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }


    public void update(Object obj) {
        if (obj instanceof List) {
            update((List) obj);
        } else {
            Adapter adapter = getAdapter();
            if (adapter instanceof HeaderViewListAdapter) {
                ((CustomAdapter) ((HeaderViewListAdapter) adapter).getWrappedAdapter()).notifyDataSetChanged();
            } else {
                ((CustomAdapter) adapter).notifyDataSetChanged();
            }
        }

    }

    public void update(List<T> list) {
        data.clear();
        update_add(list);
    }


    public void update_add(List<T> list) {
        if (list != null) {
            data.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        Adapter adapter = getAdapter();
        if (adapter instanceof HeaderViewListAdapter) {
            ((CustomAdapter) ((HeaderViewListAdapter) adapter).getWrappedAdapter()).notifyDataSetChanged();
        } else {
            ((CustomAdapter) adapter).notifyDataSetChanged();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isSelfAdaptive() && !autoMesure) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    public void addHeader(int resId) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        baseHeader = inflater.inflate(resId, null);
        BaseAdapter adapter = (BaseAdapter) getAdapter();
        setAdapter(null);
        addHeaderView(baseHeader);
        setAdapter(adapter);
    }

    public void addFooter() {
//        getLoadMoreView();
//        BaseAdapter adapter = (BaseAdapter) getAdapter();
//        setAdapter(null);
//        addFooterView(baseFooter);
//        setAdapter(adapter);
    }

    public List getData() {
        return data;
    }

//    @Event
//    public void updateUI(Object event) {
//       // S.print(getClass().getSimpleName()+"-updateUI-" + event.getMsgType());
//
//    }



    public boolean isLoading(){
        boolean isLoading = false;
        if(getFooterViewsCount()>0&&baseFooter!=null&&baseFooter.getVisibility()==View.VISIBLE){
            isLoading = true;
        }
        return isLoading;
    }

    @Override
    protected void onDetachedFromWindow() {
        //   EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //  EventBus.getDefault().register(this);
    }

    public static interface MyOnScrollListener {
        //orientation>0  向下滚动
        void onScroll(int orientation, int position);
    }


}
