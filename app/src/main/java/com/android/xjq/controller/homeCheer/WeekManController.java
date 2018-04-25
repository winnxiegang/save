package com.android.xjq.controller.homeCheer;

/**
 * Created by zhouyi on 2018/4/19.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;

/**
 * 周榜达人
 */
public class WeekManController extends CheerBaseController {

    private WeekManAdapter mAdapter;

    public WeekManController(Context context) {
        super(context);
    }

    @Override
    public void setView(View view) {
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        mAdapter = new WeekManAdapter(mContext);
        gridView.setAdapter(mAdapter);

    }

    static class WeekManAdapter extends MyBaseAdapter {


        public WeekManAdapter(Context context) {
            super(context);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = layoutInflater.inflate(R.layout.layout_cheer_fragment_weekday_item,null);
            return view;
        }
    }
}
