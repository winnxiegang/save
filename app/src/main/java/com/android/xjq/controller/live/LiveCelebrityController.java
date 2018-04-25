package com.android.xjq.controller.live;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.adapter.live.CelebrityAdapter;
import com.android.xjq.bean.myzhuwei.OrderCheerListEntity;
import com.android.xjq.model.live.CurLiveInfo;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2018/1/30.
 */

public class LiveCelebrityController extends BaseLiveController<LiveActivity> {

    private LiveHostViewHolder mViewHolder;
    private CelebrityAdapter mAdapter;
    private List<OrderCheerListEntity.CheerInfoBean> guestWinlist = new ArrayList<>();
    private List<OrderCheerListEntity.CheerInfoBean> homeWinlist = new ArrayList<>();

    public LiveCelebrityController(LiveActivity context) {
        super(context);
    }

    @Override
    public void init(View view) {
        if (mViewHolder == null) {
            mViewHolder = new LiveHostViewHolder(view);
        }
        mAdapter = new CelebrityAdapter(context, homeWinlist, guestWinlist);
        mViewHolder.listView.setAdapter(mAdapter);
    }

    @Override
    public void setView() {
    }

    @Override
    public void onHiddenChanged(boolean isHide) {
        if (!isHide) {
            context.getHttpController().queryCheerList();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewHolder = null;
    }

    @Override
    public void responseSuccessHttp(RequestContainer requestContainer, JSONObject jsonObject) {
        OrderCheerListEntity orderCheerEntity = new Gson().fromJson(jsonObject.toString(), OrderCheerListEntity.class);
        if (orderCheerEntity.getGameBoardClientSimple() == null) {
            mViewHolder.noDataTv.setVisibility(View.VISIBLE);
            mViewHolder.matchInfoLayout.setVisibility(View.GONE);
            mViewHolder.progressBar.setVisibility(View.GONE);
            mViewHolder.listView.setVisibility(View.GONE);
            mViewHolder.noDataTv.setText(context.getString(R.string.has_no_cheer));
            return;
        }
        if (orderCheerEntity.getGuestWinlist() == null || orderCheerEntity.getGuestWinlist().size() == 0
                && (orderCheerEntity.getHomeWinlist() == null || orderCheerEntity.getHomeWinlist().size() == 0)) {
            mViewHolder.noDataTv.setVisibility(View.VISIBLE);
            mViewHolder.noDataTv.setText(context.getString(R.string.celebrity_empty));
            mViewHolder.matchInfoLayout.setVisibility(View.VISIBLE);
            mViewHolder.progressBar.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.noDataTv.setVisibility(View.GONE);
            mViewHolder.matchInfoLayout.setVisibility(View.VISIBLE);
            mViewHolder.progressBar.setVisibility(View.VISIBLE);
            mViewHolder.listView.setVisibility(View.VISIBLE);
            if (orderCheerEntity.getGuestWinlist() != null) {
                guestWinlist.clear();
                guestWinlist.addAll(orderCheerEntity.getGuestWinlist());
            }
            if (orderCheerEntity.getHomeWinlist() != null) {
                homeWinlist.clear();
                homeWinlist.addAll(orderCheerEntity.getHomeWinlist());
            }
            mAdapter.notifyDataSetChanged();
        }

        long totalHomePaid = orderCheerEntity.getTotalHomePaid();
        long totalGuestPaid = orderCheerEntity.getTotalGuestPaid();
        double percent = totalHomePaid == 0 && totalGuestPaid == 0 ? 50 : 100 * totalHomePaid / (totalHomePaid + totalGuestPaid);
        mViewHolder.progressBar.setProgress((int) percent);
        mViewHolder.leftCheerTv.setText(String.valueOf(totalHomePaid));
        mViewHolder.rightCheerTv.setText(String.valueOf(totalGuestPaid));

        String plateDescribe = orderCheerEntity.getPlateDescribe();
        if (!TextUtils.isEmpty(plateDescribe)) {
            mViewHolder.plateTv.setText(Html.fromHtml(plateDescribe));
        }
        // mViewHolder.celebrityTab.setCheerInfo(totalHomePaid, totalGuestPaid, orderCheerEntity.getPlateDescribe());
        //设置赛事信息
        JczqDataBean jczqDataBean = CurLiveInfo.jczqDataBean;
        if (jczqDataBean != null) {
            int homePlaceRes = jczqDataBean.isFootball() ? R.drawable.ft_ho_icon : R.drawable.bt_ho_icon;
            String homeUrl = jczqDataBean.isFootball() ? jczqDataBean.getFTHomeLogoUrl() : jczqDataBean.getBTHomeLogoUrl();
            int guestPlaceRes = jczqDataBean.isFootball() ? R.drawable.ft_gs_icon : R.drawable.bt_gs_icon;
            String guestUrl = jczqDataBean.isFootball() ? jczqDataBean.getFTGuestLogoUrl() : jczqDataBean.getBTGuestLogoUrl();
            PicUtils.load(context.getApplicationContext(), mViewHolder.homeTeamIv, homeUrl, homePlaceRes);
            PicUtils.load(context.getApplicationContext(), mViewHolder.guestTeamIv, guestUrl, guestPlaceRes);
            mViewHolder.homeTeamTv.setText(jczqDataBean.getHomeTeamName());
            mViewHolder.guestTeamTv.setText(jczqDataBean.getGuestTeamName());
        }
    }

    class LiveHostViewHolder {
        @BindView(R.id.homeTeamIv)
        ImageView homeTeamIv;
        @BindView(R.id.homeTeamTv)
        TextView homeTeamTv;
        @BindView(R.id.guestTeamIv)
        ImageView guestTeamIv;
        @BindView(R.id.guestTeamTv)
        TextView guestTeamTv;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.listView)
        ListView listView;
        @BindView(R.id.leftCheerTv)
        TextView leftCheerTv;
        @BindView(R.id.rightCheerTv)
        TextView rightCheerTv;
        @BindView(R.id.plateTv)
        TextView plateTv;
        @BindView(R.id.noDataTv)
        TextView noDataTv;
        @BindView(R.id.contentLayout)
        View contentLayout;
        @BindView(R.id.matchInfoLayout)
        LinearLayout matchInfoLayout;

        public LiveHostViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
