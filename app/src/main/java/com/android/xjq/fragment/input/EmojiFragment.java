package com.android.xjq.fragment.input;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.android.banana.commlib.emoji.EmojAdapter;
import com.android.banana.commlib.emoji.EmojBean;
import com.android.banana.commlib.emoji.EmojUtils;
import com.android.xjq.R;
import com.android.xjq.fragment.BaseFragment;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/9/12.
 */

public class EmojiFragment extends BaseFragment {
    private static final String LIST = "list";

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.circles)
    CirclePageIndicator circles;
    @BindView(R.id.emojLayout)
    LinearLayout emojLayout;
    private InputCallback callback;

    public EmojiFragment() {
    }

    public void setCallback(InputCallback callback) {
        this.callback = callback;
    }


    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_emoji, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }

    @Override
    protected void initData() {
        ArrayList<View> viewList = new ArrayList<>();
        List<List<EmojBean>> list = EmojUtils.getEmojList(getContext());
        for (int i = 0; i < list.size(); i++) {
            View gridView = createGridView((ArrayList<EmojBean>) list.get(i));
            viewList.add(gridView);
        }
        EmojiPagerAdapter adapter = new EmojiPagerAdapter(viewList);
        viewPager.setAdapter(adapter);
        circles.setViewPager(viewPager);
    }

    private View createGridView(final ArrayList<EmojBean> list) {
        //GridView gridView = new GridView(getContext());
        View view = View.inflate(getContext(), R.layout.emoj_lib_item_view, null);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        //gridView.setNumColumns(7);
        EmojAdapter mAdapter = new EmojAdapter(getContext(), list);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (callback != null) {
                    if (position >= list.size()) {
                        if (position != 20) {
                            return;
                        }

                        callback.onEmojDelete();
                    } else {
                        callback.onEmojAdd((EmojBean) list.get(position));
                    }

                }
            }
        });
        return view;
    }
}


class EmojiPagerAdapter extends PagerAdapter {
    private ArrayList<View> list;

    public EmojiPagerAdapter(ArrayList<View> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }
}


