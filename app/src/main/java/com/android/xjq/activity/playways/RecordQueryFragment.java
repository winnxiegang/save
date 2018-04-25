package com.android.xjq.activity.playways;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.bean.payway.RecordQueryBean;
import com.android.xjq.fragment.BaseListFragment;
import com.android.xjq.utils.XjqUrlEnum;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ajiao on 2018\3\7 0007.
 */

public class RecordQueryFragment extends BaseListFragment<RecordQueryBean.PrizeRecordBean> implements IHttpResponseListener<RecordQueryBean> {

    public WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);
    public static final String HAND_SPEED = "HAND_SPEED";
    public static final String DECREE = "DECREE";
    public static final String LUCKY_DRAW = "LUCKY_DRAW";
    private String mActivityCode = "";
    private int mMaxPage = 0;

    @Override
    public int getItemLayoutRes() {
        return R.layout.list_item_record_query;
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        LiveActivity.startLiveActivity(getActivity(), mDatas.get(position).getChannelAreaId());
    }

    public static RecordQueryFragment newInstance(String acitvityCode) {
        Bundle args = new Bundle();
        args.putString("acitvityCode", acitvityCode);
        RecordQueryFragment fragment = new RecordQueryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getContext(),R.drawable.base_divider_list_10dp);
    }


    @Override
    public void onSuccess(RequestContainer request, RecordQueryBean recordQueryBean) {
        XjqUrlEnum urlEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (urlEnum) {
            case ACTIVITY_PRIZE_RECORD_QUERY:
                mMaxPage = recordQueryBean.getPaginator().getPages();
                loadCompleted(recordQueryBean == null ? null : recordQueryBean.getPrizeRecordList());
                break;
            default:
                loadCompleted(recordQueryBean == null ? null : recordQueryBean.getPrizeRecordList());
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
        mActivityCode = getArguments().getString("acitvityCode");
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
        //活动代码，极限手速HAND_SPEED，圣旨到：DECREE，幸运抽奖LUCKY_DRAW
        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.ACTIVITY_PRIZE_RECORD_QUERY, true);
        formBody.put("currentPage", mCurPage);
        formBody.put("acitvityCode", mActivityCode);
        mHttpHelper.startRequest(formBody);
    }

    @Override
    public void onBindHolder(ViewHolder holder, RecordQueryBean.PrizeRecordBean bean, int position) {
        TextView person_name = holder.getView(R.id.person_name);
        person_name.setText(bean.getAreaName());
        TextView time_m_y_r = holder.getView(R.id.time_m_y_r);
        time_m_y_r.setText(TimeUtils.getNianYueRuiStr(bean.getGmtCreate()));
        TextView time_h_f_s = holder.getView(R.id.time_h_f_s);
        time_h_f_s.setText(TimeUtils.getShiFenMiaoStr(bean.getGmtCreate()));
        CircleImageView person_icon = holder.getView(R.id.person_icon);
        Picasso.with(getActivity()).load(bean.getMasterAnchorUrl()).error(R.drawable.user_default_logo).into(person_icon);
        TextView support_person = holder.getView(R.id.support_person);

        TextView last_txt = holder.getView(R.id.last_txt);
        if (mActivityCode.equals(DECREE)) {
            int amount = (int)bean.getAmount();
            last_txt.setText(String.valueOf("+" + amount));
            last_txt.setVisibility(amount == 0 ? View.GONE : View.VISIBLE);
            Drawable drawableRight = getActivity().getResources().getDrawable(R.drawable.ic_package_dollar);
            drawableRight.setBounds(0,0,drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
            last_txt.setCompoundDrawables(drawableRight, null, null, null);
            support_person.setText("发起人: " + bean.getPatronLoginName());
        } else {
            last_txt.setText(bean.getPrizeItemName());
            last_txt.setCompoundDrawables(null, null, null, null);
            support_person.setText("赞助人: " + bean.getPatronLoginName());
        }

    }

}
