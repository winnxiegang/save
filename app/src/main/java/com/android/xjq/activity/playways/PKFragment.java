package com.android.xjq.activity.playways;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.bean.payway.PKBean;
import com.android.xjq.fragment.BaseListFragment;
import com.android.xjq.utils.XjqUrlEnum;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ajiao on 2018\3\7 0007.
 */

public class PKFragment extends BaseListFragment<PKBean.PurchaseListBean> implements IHttpResponseListener<PKBean> {

    private int mMaxPage = 0;
    public WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);

    @Override
    public int getItemLayoutRes() {
        return R.layout.list_item_pk;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getContext(),R.drawable.base_divider_list_10dp);
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        LiveActivity.startLiveActivity(getActivity(), Integer.valueOf(mDatas.get(position).getSourceId()));

    }

    @Override
    public void onSuccess(RequestContainer request, PKBean pkBean) {
        XjqUrlEnum urlEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (urlEnum) {
            case PURCHASE_ORDER_SUMMARY_QUERY_BY_PK_USER:
                mMaxPage = pkBean.getPaginator().getPages();
                loadCompleted(pkBean == null ? null : pkBean.getPurchaseList());
                break;
            default:
                loadCompleted(pkBean == null ? null : pkBean.getPurchaseList());
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
        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.PURCHASE_ORDER_SUMMARY_QUERY_BY_PK_USER, true);
        formBody.put("currentPage", mCurPage);
        formBody.put("playType", "PK");
        mHttpHelper.startRequest(formBody);
    }

    @Override
    public void onBindHolder(ViewHolder holder, PKBean.PurchaseListBean bean, int position) {
        TextView title = holder.getView(R.id.title);
        title.setText(bean.getTitle());
        TextView time_m_y_r = holder.getView(R.id.time_m_y_r);
        time_m_y_r.setText(TimeUtils.getNianYueRuiStr(bean.getGmtCreate()));
        TextView time_h_f_s = holder.getView(R.id.time_h_f_s);
        time_h_f_s.setText(TimeUtils.getShiFenMiaoStr(bean.getGmtCreate()));
        CircleImageView person_icon = holder.getView(R.id.person_icon);
        Picasso.with(getActivity()).load(bean.getUserLogoUrl()).error(R.drawable.user_default_logo).into(person_icon);
        if (bean.getUserLogoUrl() == null || "".equals(bean.getUserLogoUrl())) {
            person_icon.setVisibility(View.GONE);
        } else {
            person_icon.setVisibility(View.VISIBLE);
        }
        TextView person_name = holder.getView(R.id.person_name);
        person_name.setText(bean.getUserAlias());
        TextView result_txt = holder.getView(R.id.result_txt);
        PayWayFragment.setResultStr(bean.getOrderStatus(), result_txt);
        ImageView treasureBoxImg = holder.getView(R.id.bao_xiang_img);
        if (bean.getPkgCode() != null && !"".equals(bean.getPkgCode())) {
            if (bean.getOrderStatus().equals("WIN") || bean.getOrderStatus().equals("LOSE")) {
                treasureBoxImg.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(bean.getPkgImageUrl()).error(R.drawable.user_default_logo).into(treasureBoxImg);
            }
        } else {
            treasureBoxImg.setVisibility(View.GONE);
        }
        TextView choose_txt = holder.getView(R.id.choose_txt);
        choose_txt.setText("我选择: " + bean.getEntryValue());
        TextView pk_value = holder.getView(R.id.pk_value);
        int value = (int)bean.getTotalPaidFee();
        pk_value.setText("PK值: " + String.valueOf(value));
    }
}
