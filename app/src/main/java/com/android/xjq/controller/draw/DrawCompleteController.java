package com.android.xjq.controller.draw;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.view.MedalLayout;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.xjq.R;
import com.android.xjq.activity.ThirdWebActivity;
import com.android.xjq.bean.UserMedalLevelBean;
import com.android.xjq.bean.draw.PrizedRecordEntity;
import com.android.xjq.view.recyclerview.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2018/3/9.
 */

public class DrawCompleteController extends BaseController4JCZJ<BaseActivity> {

    private RecyclerView recyclerView;
    private ImageView hostPortraitIv;
    private TextView historyRecordTv;
    private TextView hostNameTv;
    private List<PrizedRecordEntity.PirzedUserBean> mList = new ArrayList<>();
    private String giftUrl;
    private HistoryRecordAdapter mAdapter;
    private String prizeRecordHistoryUrl;
    private MedalLayout medalLayout;
    private TextView noPrizedView;
    private TextView prizedListTv;

    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.layout_draw_completed);
    }

    @Override
    public void onSetUpView() {
        createCircularReveal(true);
        prizedListTv = findViewOfId(R.id.prizedListTv);
        medalLayout = findViewOfId(R.id.medalLayout);
        recyclerView = findViewOfId(R.id.recyclerView);
        hostPortraitIv = findViewOfId(R.id.hostPortraitIv);
        hostNameTv = findViewOfId(R.id.hostNameTv);
        noPrizedView = findViewOfId(R.id.noPrizedView);
        historyRecordTv = findViewOfId(R.id.historyRecordTv);
        recyclerView.addItemDecoration(new SpacesItemDecoration(context, 6));
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mAdapter = new HistoryRecordAdapter(context, mList, 0, new MultiTypeSupport<PrizedRecordEntity.PirzedUserBean>() {
            @Override
            public int getTypeLayoutRes(PrizedRecordEntity.PirzedUserBean data, int pos) {
                if (!TextUtils.isEmpty(data.title)) {
                    return R.layout.item_prized_title;
                }
                return R.layout.item_prized_user_list;
            }
        });
        recyclerView.setAdapter(mAdapter);
        historyRecordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThirdWebActivity.startThirdWebActivity((Activity) context, prizeRecordHistoryUrl);
            }
        });
    }

    public void setPrizedIssueData(PrizedRecordEntity prizedRecordEntity) {
        try {
            List<PrizedRecordEntity.PirzedUserBean> prizedInfoList = prizedRecordEntity.getPrizedInfoList();
            PicUtils.load(context.getApplicationContext(), hostPortraitIv, prizedRecordEntity.patronUrl);
            hostNameTv.setText(prizedRecordEntity.patronLoginName);
            if (prizedInfoList != null && prizedInfoList.size() > 0) {
                mList.clear();
                mList.addAll(prizedInfoList);
                mAdapter.notifyDataSetChanged();
                noPrizedView.setVisibility(View.GONE);
                prizedListTv.setVisibility(View.VISIBLE);
            } else {
                prizedListTv.setVisibility(View.GONE);
                noPrizedView.setVisibility(View.VISIBLE);
            }
            giftUrl = prizedRecordEntity.giftUrl;
            prizeRecordHistoryUrl = prizedRecordEntity.prizeRecordHistoryUrl;

            if (prizedRecordEntity.userMedalLevelList != null && prizedRecordEntity.userMedalLevelList.size() > 0) {
                for (UserMedalLevelBean userMedalLevelBean : prizedRecordEntity.userMedalLevelList) {
                    medalLayout.addMedal(SubjectMedalEnum.getMedalResourceId(context,
                            userMedalLevelBean.medalConfigCode, userMedalLevelBean.currentMedalLevelConfigCode));
                }
            }
        } catch (Exception e) {
        }
    }


    public class HistoryRecordAdapter extends MultiTypeSupportAdapter<PrizedRecordEntity.PirzedUserBean> {


        public HistoryRecordAdapter(Context context, List list, int layoutRes, MultiTypeSupport typeSupport) {
            super(context, list, layoutRes, typeSupport);
        }

        @Override
        public void onBindNormalHolder(ViewHolder holder, PrizedRecordEntity.PirzedUserBean pirzedUserBean, int position) {
            if (holder.getItemViewType() == R.layout.item_prized_title) {
                holder.setText(R.id.titleTv, pirzedUserBean.title);
                holder.setViewVisibility(R.id.noPrizedTv, pirzedUserBean.isNoBodyPrized ? View.VISIBLE : View.GONE);
            } else {
                holder.setText(R.id.userNameTv, pirzedUserBean.loginName);
                holder.setText(R.id.giftCountTv, "x" + pirzedUserBean.participateCount);
                holder.setImageByUrl(context, R.id.giftIv, giftUrl);
            }
        }
    }
}
