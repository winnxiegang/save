package com.android.banana.groupchat.view.baselist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HeaderViewListAdapter;


import com.android.banana.groupchat.view.baselist.base.BaseBindManager;
import com.android.banana.groupchat.view.baselist.base.BaseViewHolder;
import com.android.banana.groupchat.view.baselist.base.CustomAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kokuma on 2017/4/6.
 */
public abstract  class BaseGridView<T> extends GridView implements AdapterView.OnItemClickListener{
    protected  List<T> data;
    protected BaseBindManager bindManager;

    public BaseGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseGridView(Context context) {
        super(context);
    }

    public BaseGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseGridView initUI(int resourceId) {
        BaseBindManager.init(getContext().getApplicationContext());
        setSelector(android.R.color.transparent);
        setOnItemClickListener(this);
        bindManager= BaseBindManager.getInstance();
        this.data= new ArrayList<>();
        CustomAdapter customAdapter = new CustomAdapter<T>(getContext(),resourceId,data ) {
            @Override
            public void setConvert(BaseViewHolder viewHolder, T bean) {
                BaseGridView.this.setConvert(viewHolder,bean);
            }
        };
        setAdapter(customAdapter);

        return this;
    }

    protected abstract void setConvert(BaseViewHolder viewHolder, T bean);
    protected  boolean isSelfAdaptive(){
        return false;
    }


    public void update( List<T> list) {
        data.clear();
        if(list!=null){
            data.addAll(list);
        }
        Adapter adapter= getAdapter();
        if(adapter instanceof HeaderViewListAdapter){
            ( (CustomAdapter)  ((HeaderViewListAdapter) adapter).getWrappedAdapter()).notifyDataSetChanged();
        }else {
            ( (CustomAdapter)adapter).notifyDataSetChanged();
        }

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(isSelfAdaptive()){
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    public List<T> getData() {
        return data;
    }

}
