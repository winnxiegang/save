package com.android.xjq.activity.playways;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.banana.pullrecycler.recyclerview.DividerItemDecoration;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.myzhuwei.ZhuweiDetailActivity;
import com.android.xjq.bean.payway.CheerBean;
import com.android.xjq.fragment.BaseListFragment;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

/**
 * Created by ajiao on 2018\1\26 0026.
 */

public class PayWayFragment extends BaseListFragment<CheerBean.PurchaseOrderBean> implements IHttpResponseListener<CheerBean> {

    public static final String HOME_WIN = "HOME_WIN";
    public static final String GUEST_WIN = "GUEST_WIN";
    private int mMaxPage = 0;
    public WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);

    @Override
    public int getItemLayoutRes() {
        return R.layout.list_item_pay_way_cheer;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getContext(),R.drawable.base_divider_list_10dp);
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        Intent intent = new Intent(getContext(), ZhuweiDetailActivity.class);
        intent.putExtra("id", mDatas.get(position).getId());
        getContext().startActivity(intent);
    }


    @Override
    public void onSuccess(RequestContainer request, CheerBean cheerBean) {
        XjqUrlEnum urlEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (urlEnum) {
            case PURCHASE_ORDER_SUMMARY_QUERY_BY_USER:
                mMaxPage = cheerBean.getPaginator().getPages();
                loadCompleted(cheerBean == null ? null : cheerBean.getPurchaseOrderList());
                break;
            default:
                loadCompleted(cheerBean == null ? null : cheerBean.getPurchaseOrderList());
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        loadCompleted(null);
        ((BaseActivity) getActivity()).operateErrorResponseMessage(jsonObject);
    }

    @Override
    protected void initData() {
        if (mDatas != null) {
            mDatas.clear();
            mCurPage = 1;
        }
        reqPayData();
    }

    @Override
    protected void onSetUpView() {
        if (mDatas != null) {
            mDatas.clear();
        }
        setLoadMoreEnable(true);
        mRecycler.setEmptyView(R.drawable.icon_no_content_about_match_schedule_detail, "", "");
    }

    @Override
    public void onRefresh(boolean refresh) {
        if (mCurPage > mMaxPage) {
            mRecycler.refreshCompleted();
            return;
        } else {
            reqPayData();
        }

    }

    private void reqPayData() {
        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.PURCHASE_ORDER_SUMMARY_QUERY_BY_USER, true);
        formBody.put("currentPage", mCurPage);
        formBody.put("playType", "CHEER");
        mHttpHelper.startRequest(formBody);
    }

    @Override
    public void onBindHolder(ViewHolder holder, CheerBean.PurchaseOrderBean bean, int position) {
        String hostName = bean.getGameBoardClientSimple().getHomeTeamName();
        TextView host_team_name = holder.getView(R.id.host_team_name);
        host_team_name.setText(hostName.length() > 6 ? hostName.substring(0, 6) : hostName);
        String guestName = bean.getGameBoardClientSimple().getGuestTeamName();
        TextView guest_team_name = holder.getView(R.id.guest_team_name);
        guest_team_name.setText(guestName.length() > 6? guestName.substring(0, 6) : guestName);
        TextView time_m_y_r = holder.getView(R.id.time_m_y_r);
        time_m_y_r.setText(TimeUtils.getNianYueRuiStr(bean.getGmtCreate()));
        TextView time_h_f_s = holder.getView(R.id.time_h_f_s);
        time_h_f_s.setText(TimeUtils.getShiFenMiaoStr(bean.getGmtCreate()));
        TextView chang_ci_bi_fen = holder.getView(R.id.chang_ci_bi_fen);
        chang_ci_bi_fen.setText("[" + bean.getGameBoardClientSimple().getRaceStageType().getName() + "]" + "比分");
        String supportStr =bean.getGameBoardClientSimple().getHomeTeamName() + bean.getGameBoardClientSimple().getPlate() + "球, 你支持哪支球队获胜？";
        TextView support_for = holder.getView(R.id.support_for);
        support_for.setText(supportStr);
        TextView support_team = holder.getView(R.id.support_team);
        String supportName = "";
        if (bean.getEntryValue().equals("GUEST_WIN")) {
            supportName = bean.getGameBoardClientSimple().getGuestTeamName();
        } else if (bean.getEntryValue().equals("HOME_WIN")) {
            supportName = bean.getGameBoardClientSimple().getHomeTeamName();
        }
        support_team.setText("我助威:" + supportName);
        TextView support_value = holder.getView(R.id.support_value);
        support_value.setText("助威值:" + bean.getTotalFee());
        TextView result = holder.getView(R.id.result);
        setResultStr(bean.getOrderStatus(), result);
        TextView value = holder.getView(R.id.value);
        int prizeFee = (int)bean.getPrizeFee();
        int visibility = "0".equals(String.valueOf(prizeFee)) ? View.GONE : View.VISIBLE;
        value.setText(String.valueOf(prizeFee));
        value.setVisibility(visibility);
    }

    public static void setResultStr(String orderStatus, TextView txt) {
        //CANCEL_FINISH = 流局 WIN=成功 LOSE=惜败 PROGRESSING = 进行中  WAIT_PRIZE =等待派奖
        String result = "";
        switch (orderStatus) {
            case "CANCEL_FINISH":
                result = "流局";
                break;
            case "WIN":
                result = "成功";
                break;
            case "LOSE":
                result = "惜败";
                break;
            case "PROGRESSING":
                result = "进行中";
                break;
            case "WAIT_PRIZE":
                result = "等待派奖";
                break;
                default:
                    break;
        }
        txt.setText(result);
    }

}
