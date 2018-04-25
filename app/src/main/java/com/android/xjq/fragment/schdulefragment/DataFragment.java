package com.android.xjq.fragment.schdulefragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.xjq.R;
import com.android.xjq.controller.schduledetail.RankController;
import com.android.xjq.controller.schduledetail.injury.JclqInjuryController;
import com.android.xjq.controller.schduledetail.injury.LayOffController;
import com.android.xjq.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataFragment extends BaseFragment{

    @BindView(R.id.scoreRankRb)
    RadioButton scoreRankRb;
    @BindView(R.id.injuryRb)
    RadioButton injuryRb;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.contentFrameLayout)
    ViewGroup mContentLayout;

    private LayOffController jczqInjuryController;//足球伤停

    private RankController rankController;

    private JclqInjuryController jclqInjuryController;//篮球伤停

    private JczqDataBean jczqDataBean;

    private String raceType;

    public static DataFragment newInstance(JczqDataBean jczqDataBean, String raceType) {

        Bundle args = new Bundle();
        args.putString("raceType",raceType);
        args.putParcelable("jczqData",jczqDataBean);
        DataFragment fragment = new DataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {

        jczqDataBean = getArguments().getParcelable("jczqData");

        raceType= getArguments().getString("raceType");

        jczqInjuryController = new LayOffController(getActivity(),jczqDataBean);

        jclqInjuryController = new JclqInjuryController(getActivity(),jczqDataBean);

        rankController = new RankController((BaseActivity) getActivity(),jczqDataBean, TextUtils.equals(raceType,"FOOTBALL"));

        rankController.setContentView(mContentLayout);

        if(TextUtils.equals("BASKETBALL",raceType)){
            scoreRankRb.setText("排名");
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case  R.id.injuryRb:
                        rankController.clearAnimation();
                        if(TextUtils.equals(raceType,"FOOTBALL")){
                            jczqInjuryController.stopRefresh();
                            jczqInjuryController.setContentView(mContentLayout);
                        }else{
                            jclqInjuryController.setContentView(mContentLayout);
                        }
                        break;
                    case R.id.scoreRankRb:
                        if(jclqInjuryController!=null){
                            jclqInjuryController.clearAnimation();
                        }
                        if(jczqInjuryController!=null){
                            jczqInjuryController.stopRefresh();
                        }
                        rankController.setContentView(mContentLayout);
                        break;
                }
            }
        });
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = (View) inflater.inflate(R.layout.fragment_data, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
