package com.android.xjq.controller.schduledetail.injury;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



/**
 * fuction: 封装holder
 * creator:kokuma
 * time:2016/05/06 12:10
 */
public class BaseViewHolder {
    /**
     * 视图容器
     */
    private SparseArray<View> mViews;

    /**
     * 位置标识
     */
    public int mPosition;

    /**
     * 视图
     */
    private View mConvertView;

    private BaseViewHolder(Context context,ViewGroup parent,int layoutId,final int position){
        this.mViews = new SparseArray<>();
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        this.mPosition = position;
        mConvertView.setTag(this);
    }
    /*
        fuction:静态方法获取到viewholder类实例
        parameters:layoutId加载的布局resid
        creator:fangshu
        time:2016/5/6 12:48
    */
    public static BaseViewHolder get(Context context,ViewGroup parent,int layoutId,int position,View convertView){
        BaseViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new BaseViewHolder(context,parent,layoutId,position);
            return viewHolder;
        }
        else {
            viewHolder = (BaseViewHolder) convertView.getTag();
        }
        viewHolder.mPosition = position;
        return viewHolder;
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

    /*
        fuction:返回视图类
        parameters:
        creator:fangshu
        time:2016/5/6 12:51
    */
    public View getConvertView()
    {
        return mConvertView;
    }

    /*
     fuction:返回当前的position
     parameters:
     creator:fangshu
     time:2016/5/6 12:52
     */
    public int getPosition()
    {
        return mPosition;
    }

    /*
        fuction:TextView设置文字
        parameters:文本内容
        creator:fangshu
        time:2016/5/6 12:46
    **/
    public BaseViewHolder setTextView(int viewId, String content)
    {
        TextView tv = getView(viewId);
        tv.setText(content);
        return this;
    }

    /*
        fuction:设置button文字
        parameters:文本内容
        creator:fangshu
        time:2016/5/6 12:54
    */
    public BaseViewHolder setTexButton(int viewId,String content)
    {
        Button btn = getView(viewId);
        btn.setText(content);
        return this;
    }


    public BaseViewHolder setViewVisible(int viewId,int visible){
        View view = getView(viewId);
        view.setVisibility(visible);
        return  this;
    }

}
