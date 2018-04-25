package com.android.xjq.view.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.gamePK.PkGameBoarInfoBean;
import com.android.xjq.bean.gamePK.PkOptionEntryBean;
import com.android.xjq.dialog.InvitedGuestDialog;
import com.android.xjq.dialog.ShareGroupListDialogFragment;
import com.android.xjq.dialog.live.PersonalInfoDialog;
import com.android.xjq.model.SaleStatusEnum;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.xjq.utils.PkGameUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.xjq.model.SaleStatusEnum.PROGRESSING;
import static com.android.xjq.utils.XjqUrlEnum.PURCHASE_PK_NORMAL;

/**
 * Created by lingjiu on 2018/3/18.
 */

public class PKRecyclerView extends RecyclerView implements IHttpResponseListener {


    private List<Integer> multipleList;
    private PKRecyclerView.PkAdapter mAdapter;
    private List<PkGameBoarInfoBean> mDatas = new ArrayList<>();
    private WrapperHttpHelper httpHelper;

    public PKRecyclerView(Context context) {
        this(context, null);
    }

    public PKRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PKRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        setLayoutManager(linearLayoutManager);
        setItemAnimator(null);
        new PagerSnapHelper().attachToRecyclerView(this);
        httpHelper = new WrapperHttpHelper(this);
    }

    public void setPkData(List<PkGameBoarInfoBean> pkGameBoardList, List<Integer> multipleList) {
        this.multipleList = multipleList;
        if (pkGameBoardList != null) {
            mDatas.clear();
            mDatas.addAll(pkGameBoardList);
        }
        if (getVisibility() == View.GONE) {
            return;
        }
        notifyData();
    }

    private void notifyData() {
        if (mAdapter == null) {
            mAdapter = new PkAdapter(getContext(), mDatas, 0, getSupportMultiType());
            setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    private PkGameUtils.PurchasePkListener purchasePkListener = new PkGameUtils.PurchasePkListener() {
        @Override
        public void purchasePk(PkOptionEntryBean infoBean, int count, String option) {
            RequestFormBody map = new RequestFormBody(PURCHASE_PK_NORMAL, true);
            map.put("createrType", "USER_CREATE");
            map.put("payCoinType", "GOLD_COIN");
            map.put("payType", "GIFT");
            map.put("playType", "PK");
            map.put("playSubType", "PK");
            map.put("payTypeNo", infoBean.betFormNo);
            map.put("boardId", infoBean.boardId);
            map.put("totalFee", count * infoBean.betFormSingleFee);
            map.put("singleTotalFee", infoBean.betFormSingleFee);
            map.put("multiple", count);
            map.put("keyAndOptions", infoBean.boardId + "@" + option);
            httpHelper.startRequest(map, true);
        }

        @Override
        public void sharePk(PkGameBoarInfoBean infoBean) {
            ShareGroupListDialogFragment.newInstance(true, infoBean.id, "PK_GAME_BOARD", infoBean.title)
                    .show(((BaseActivity) getContext()).getSupportFragmentManager());
        }

        @Override
        public void showPurchaseList(PkOptionEntryBean optionEntryBean) {
            new InvitedGuestDialog.Builder(getContext()).setTitle("投注人员").create().
                    generateGuestData(optionEntryBean.boardId, optionEntryBean.id, CurLiveInfo.channelAreaId).show();
        }

        @Override
        public void showPersonCard(List<PkOptionEntryBean.RankUserListBean> userList, int position) {
            try {
                PkOptionEntryBean.RankUserListBean rankUserListBean = userList.get(position);
                new PersonalInfoDialog(getContext(), rankUserListBean.getUserId()).show();
            } catch (Exception e) {
            }
        }
    };

    private MultiTypeSupport getSupportMultiType() {
        return new MultiTypeSupport<PkGameBoarInfoBean>() {
            @Override
            public int getTypeLayoutRes(PkGameBoarInfoBean data, int pos) {
                switch (SaleStatusEnum.safeValueOf(data.saleStatus == null ? null : data.saleStatus.getName())) {
                    case PROGRESSING:
                        return R.layout.list_item_pk_progressing;
                    case WAIT:
                    case PAUSE:
                        return R.layout.list_item_pk_pause;
                    case FINISH:
                        //"PRIZEING"表示派奖中"PRIZE_FINISH"表示结束派奖
                        //if ("PRIZE_FINISH".equals(data.getPrizeStatus())) {
                        return R.layout.list_item_pk_completed;
                    //}
                    //return R.layout.list_item_pk_pause;
                    case CANCEL:
                        return R.layout.list_item_pk_invalid;
                }
                return R.layout.list_item_pk_progressing;
            }
        };
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        ToastUtil.showLong(getContext().getApplicationContext(), "赠送成功");
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        ((BaseActivity) getContext()).operateErrorResponseMessage(jsonObject);
    }

    public void showView() {
        setVisibility(View.VISIBLE);
        //   setVisibility(getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        notifyData();
    }

    private class PkAdapter extends MultiTypeSupportAdapter<PkGameBoarInfoBean> {

        public PkAdapter(Context context, List<PkGameBoarInfoBean> list, int layoutRes, MultiTypeSupport typeSupport) {
            super(context, list, layoutRes, typeSupport);
        }

        @Override
        public void onBindNormalHolder(com.android.banana.pullrecycler.multisupport.ViewHolder holder, PkGameBoarInfoBean pkGameBoarInfoBean, int position) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_500));
            PkGameUtils.bindViewHolder(getContext(), holder, pkGameBoarInfoBean, multipleList, position, purchasePkListener);
            if (PROGRESSING == SaleStatusEnum.safeValueOf(pkGameBoarInfoBean.saleStatus == null ? null : pkGameBoarInfoBean.saleStatus.getName())) {
                holder.setViewVisibility(R.id.shareIv, View.GONE);
            }
        }
    }
}
