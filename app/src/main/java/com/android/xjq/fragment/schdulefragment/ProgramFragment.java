package com.android.xjq.fragment.schdulefragment;

import android.view.LayoutInflater;
import android.view.View;

import com.android.xjq.R;
import com.android.xjq.fragment.BaseFragment;

import butterknife.ButterKnife;

public class ProgramFragment extends BaseFragment {

    @Override
    protected void initData() {

    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = (View) inflater.inflate(R.layout.fragment_program, null);
        ButterKnife.bind(this, view);
        //recyclerView.addItemDecoration();
        return view;
    }
}
