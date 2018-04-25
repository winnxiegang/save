package com.android.xjq.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.xjq.R;
import com.android.xjq.controller.homeCheer.BannerController;
import com.android.xjq.controller.homeCheer.FlipMessageController;
import com.android.xjq.controller.homeCheer.RecommendCheerController;
import com.android.xjq.controller.homeCheer.WeekManController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhouyi on 2018/4/18.
 */

public class CheerFragment extends BaseListFragment {

    @BindView(R.id.titleTv)
    TextView mTitleTv;

    @BindView(R.id.parentView)
    View parentView;

    private BannerController mBannerController;
    private FlipMessageController mFlipMessageController;
    private WeekManController mWeekManController;
    private RecommendCheerController mRCController;

    /**
     * 进入用户问腿界面
     */
    @OnClick(R.id.questionIv)
    public void toQuestion() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected View initView(LayoutInflater inflater) {

        View view = inflater.inflate(R.layout.fragment_cheer, null);
        ButterKnife.bind(this, view);
        mTitleTv.setText("助威");
        fitsSystemWindows(view.findViewById(R.id.titleLayout));

        initController();
        initView(inflater, (ViewGroup) parentView);

        mRecycler.post(new Runnable() {
            @Override
            public void run() {
                mRecycler.refreshCompleted();
            }
        });

        return view;
    }

    private void initController() {
        mBannerController = new BannerController(getActivity());
        mFlipMessageController = new FlipMessageController(getActivity());
        mWeekManController = new WeekManController(getActivity());
        mRCController = new RecommendCheerController(getActivity());

    }

    @Override
    protected void onSetUpView() {
        mRecycler.addHeaderView(mBannerController.getView());
        mRecycler.addHeaderView(createHeaderView());
    }

    private View createHeaderView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_cheer_fragment_header_view, null);
        mFlipMessageController.setView(view.findViewById(R.id.flipLayout));
        mWeekManController.setView(view.findViewById(R.id.weekdayManLayout));
        mRCController.setView(mRecycler);
        return view;
    }

    @Override
    public void onRefresh(boolean refresh) {
        mRecycler.refreshCompleted();
    }

    @Override
    public void onBindHolder(ViewHolder holder, Object item, int position) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
