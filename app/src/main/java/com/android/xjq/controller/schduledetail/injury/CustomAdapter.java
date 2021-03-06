package com.android.xjq.controller.schduledetail.injury;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * fuction:封装的adapter，用于继承，只需重写setConvert方法
 * creator:fangshu
 * time:2016/05/06 12:58
 */
public abstract class CustomAdapter<T> extends  ArrayAdapter<T>{
    //listview的item资源id
    public int resourceId;

    private List<T> list = new ArrayList<>();
    public Context context;

    public CustomAdapter(Context context, int resource, List<T> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        this.resourceId = resource;
    }
    @Override
    public T getItem(int position) {
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BaseViewHolder viewHolder = BaseViewHolder.get(getContext(), parent, resourceId, position, convertView);
        final T t = getItem(viewHolder.getPosition());
        // 设置每个item控件
        setConvert(viewHolder,t);
        return viewHolder.getConvertView();
    }

  /*
      fuction:抽象方法，由子类去实现每个itme如何设置
      parameters:
      creator:fangshu
      time:2016/5/6 13:02
  */
    public abstract void setConvert(BaseViewHolder viewHolder, T t);

    // public abstract void setConvertByPosition(BaseViewHolder viewHolder, T t,int position);
    /*
        fuction:设置adapter中的数据集合
        parameters:
        creator:fangshu
        time:2016/5/6 13:04
    */
    public void setList(List<T> list) {
        this.list = list;
    }
    /*
        fuction:
        parameters:
        creator:fangshu
        time:2016/5/6 13:04
    */
    public List<T> getList() {
        return list;
    }

    public void addListToLast(List<T> list1) {
        list.addAll(list1);
    }
}

