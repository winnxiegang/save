package com.android.banana.groupchat.view.baselist.recylerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.android.banana.R;
import com.android.banana.groupchat.view.baselist.base.BaseBindManager;

import java.util.ArrayList;
import java.util.List;


/**
 * function:
 * creator:XiongXiaolu
 * time:2016/11/7 10:10
 */
public abstract class BaseRecylerView<T> extends RecyclerView implements View.OnClickListener {
    public List<T> dataArray;

    public BaseRecylerView(Context context) {
        super(context);
        init(null);
    }

    public BaseRecylerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        BaseBindManager.init(getContext().getApplicationContext());
        dataArray = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(layoutManager);
        //      int space = Utils.dp2Px(30);
//        addItemDecoration(new SpaceItemDecoration(space, 0));
        setAdapter(new MyAdapter());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void update(List<T> mList) {
        dataArray.clear();
        if (mList != null) {
            dataArray.addAll(mList);
        }
        getAdapter().notifyDataSetChanged();
    }
    public void updateAdd(List<T> mList) {
       // dataArray.clear();
        if (mList != null) {
            dataArray.addAll(mList);
        }
        getAdapter().notifyDataSetChanged();
    }

    public abstract int setMyItemId();

    public abstract int setItemWidth();

    public abstract void bindMyViewHolder(RecyclerViewHolder holder, int position);


    class MyAdapter extends Adapter<RecyclerViewHolder> {

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(setMyItemId(), null);
            if (setItemWidth() > 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(setItemWidth(), ViewGroup.LayoutParams.MATCH_PARENT));
            }
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            View root = holder.getView(R.id.root);
            if(root!=null){
                root.setTag(position);
                root.setOnClickListener(BaseRecylerView.this);
            }
            bindMyViewHolder(holder, position);
        }

        @Override
        public int getItemCount() {
            return dataArray.size();
        }
    }


    @Override
    protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();
    }
}
