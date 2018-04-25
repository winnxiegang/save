package com.android.xjq.controller.schduledetail.injury;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.xjq.R;
import com.android.xjq.bean.scheduledetail.InjuryBean;

import java.util.Arrays;

/**
 * Created by kokuma on 2017/8/14.
 */

public class LayOffListView extends BaseListView {

    String[] titles = {"号码","球员","位置","出场","状态","原因","评分"};

    public LayOffListView(Context context) {
        super(context);
        initUI(R.layout.item_lay_off);
    }

    public LayOffListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(R.layout.item_lay_off);
    }

    public LayOffListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI(R.layout.item_lay_off);
    }

    @Override
    protected void setConvert(BaseViewHolder viewHolder, Object bean) {
        int type = getItemViewType(viewHolder.getPosition());
        if (type == 0) {
            View viewLine = viewHolder.getView(R.id.viewLine);
            TextView tvTeamName = viewHolder.getView(R.id.tvTeamName);
            ImageView ivTeamImg = viewHolder.getView(R.id.ivTeamImg);
            String  txt = (String) bean;
            String[] infos = txt==null?null:txt.split(",");
            if(viewHolder.getPosition()==0){
                viewLine.setBackgroundResource(R.color.main_red);
            }else {
                viewLine.setBackgroundColor(Color.parseColor("#1f91f2"));
            }
            if(infos!=null&&infos.length>=2){
                tvTeamName.setText(infos[0]);
                PicUtils.load(getContext(),ivTeamImg,infos[1]);
            }
        } else{
            RowTextView tvRow = viewHolder.getView(R.id.tvRow) ;
            tvRow.setDpCellWidths(0, 80, 0, 0, 0, 100,0);
            if(bean == null){
                TextView  tvNoData = viewHolder.getView(R.id.tvNoData) ;
                tvRow.colorBg = Color.parseColor("#dcdcdc");
                tvRow.setTxtPaintColor( Color.parseColor("#666666"));
                tvRow.setTextList(Arrays.asList(titles));
                if(isShowNoData(viewHolder.getPosition())){
                    tvNoData.setVisibility(VISIBLE);
                }else{
                    tvNoData.setVisibility(GONE);
                }
            }else {
                InjuryBean bean2 = (InjuryBean) bean;
                String pos = bean2.getPos()==null?" ":bean2.getPos().getMessage();
                String[] info ={bean2.getSn()+"" ,bean2.getPn() ,pos ,bean2.getCc()+"" ,bean2.getStatus(),bean2.getDetail(),bean2.getAs()+""};
                tvRow.colorBg = Color.WHITE;
                tvRow.setTxtPaintColor( Color.parseColor("#333333"));
                tvRow.setTextList(Arrays.asList(info));
            }
        }
    }

    @Override
    protected int getItemViewType(int position) {
        Object obj = data.get(position);
        if(obj instanceof  String){
            return 0;
        }else if(obj==null){
            return 1;
        }else{
            return 2;
        }
    }

    @Override
    protected int getViewTypeCount() {
        return 3;
    }

    @Override
    protected View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        int resourceId = R.layout.item_lay_off;
        if (type == 0) {
            resourceId = R.layout.item_lay_off_title;
        }else if (type == 1) {
            resourceId = R.layout.item_lay_off_title2;
        }else if(type == 2) {
            resourceId = R.layout.item_lay_off;
        }
        BaseViewHolder viewHolder = BaseViewHolder.get(getContext(), parent, resourceId, position, convertView);
        // 设置每个item控件
        setConvert(viewHolder, data.get(position));
        View root =viewHolder.getConvertView();

        return root;
    }


    boolean isShowNoData(int position){
        if(position+1<data.size()){
            Object obj = data.get(position+1);
            if(obj!=null&&(obj instanceof InjuryBean)){
                return false;
            }
        }
        return true;
    }
}
