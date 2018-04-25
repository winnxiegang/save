package com.android.banana.groupchat.view.baselist.recylerview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by kokuma on 2017/3/26.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder{
    /**
     * 视图容器
     */
    private SparseArray<View> mViews;


    /**
     * 视图
     */
    private View mConvertView;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        this.mViews = new SparseArray<>();
        this.mConvertView =itemView;
    }

    /*
       fuction:根据控件id获取到控件
       parameters:控件id
       creator:fangshu
       time:2016/5/6 12:50
   */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId)
    {
        View view = mViews.get(viewId);
        if (view == null)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T)view;
    }
}
