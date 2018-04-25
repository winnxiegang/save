package com.android.xjq.fragment.input;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import com.android.xjq.R;
import com.android.xjq.adapter.main.BubblePanelAdapter;
import com.android.xjq.bean.live.ChannelUserConfigBean;
import com.android.xjq.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/9/13.
 */

public class BubblePanelFragment extends BaseFragment {

    @BindView(R.id.gridView)
    GridView gridView;

    private InputCallback callback;
    private BubblePanelAdapter mAdapter;
    private List<ChannelUserConfigBean.ChannelBubbleConfigBean> mList = new ArrayList<>();

    public void setCallback(InputCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_bubble_panel, null);
        ButterKnife.bind(this, view);
        setGridView();
        return view;
    }

    private void setGridView() {
        mAdapter = new BubblePanelAdapter(activity, mList);
        gridView.setAdapter(mAdapter);
        mAdapter.setBubbleSelectedListener(new BubblePanelAdapter.BubbleSelectedListener() {
            @Override
            public void selectedBubble(int position) {
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setSelected(false);
                    if (position == i) {
                        mList.get(i).setSelected(true);
                    }
                }
                mAdapter.notifyDataSetChanged();
                if (callback != null) callback.onBubbleSelected(mList.get(position).getFontColor());
            }
        });
    }

    public void setBubbleData(List<ChannelUserConfigBean.ChannelBubbleConfigBean> channelBubbleConfigBeanList) {
        mList.clear();
        mList.addAll(channelBubbleConfigBeanList);
    }

    public void setBubbleEnable(List<String> fontList) {
        if (fontList == null) return;
        for (ChannelUserConfigBean.ChannelBubbleConfigBean bean : mList) {
            if (fontList.contains(bean.getFontColor())) {
                bean.setEnable(true);
            }
        }
    }

    public void setCurrentBubble(String fontColor) {
        for (ChannelUserConfigBean.ChannelBubbleConfigBean bean : mList) {
            bean.setSelected(false);
            if (fontColor.equals(bean.getFontColor())) {
                bean.setSelected(true);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }
}
