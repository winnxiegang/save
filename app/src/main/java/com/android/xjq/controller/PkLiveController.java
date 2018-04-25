package com.android.xjq.controller;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.SpannableStringHelper;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.pullrecycler.ilayoutmanager.MyLinearLayoutManager;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.banana.pullrecycler.recyclerview.PullRecycler;
import com.android.banana.pullrecycler.recyclerview.onRefreshListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.gamePK.PkGameBoarInfoBean;
import com.android.xjq.bean.gamePK.PkGameBoardEntity;
import com.android.xjq.bean.gamePK.PkOptionEntryBean;
import com.android.xjq.dialog.InvitedGuestDialog;
import com.android.xjq.dialog.ShareGroupListDialogFragment;
import com.android.xjq.dialog.live.PersonalInfoDialog;
import com.android.xjq.model.SaleStatusEnum;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.xjq.utils.PkGameUtils;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.view.recyclerview.DashLineItemDecoration;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.xjq.utils.XjqUrlEnum.PURCHASE_PK_NORMAL;

/**
 * Created by lingjiu on 2018/3/1.
 */

public class PkLiveController implements onRefreshListener, IHttpResponseListener {

    private Context context;
    private PullRecycler mRecycler;
    private PkAdapter mAdapter;
    private List<PkGameBoarInfoBean> mDatas = new ArrayList<>();
    private int mCurPage;
    private boolean mEnableLoadMore;
    public WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);
    private boolean isQueryNow;
    private List<Integer> multipleList;

    public PkLiveController(Context context, boolean isQueryNow) {
        this.context = context;
        this.isQueryNow = isQueryNow;
    }

    public View getPkTabView() {
        View view = View.inflate(context, R.layout.fragment_base_list, null);
        mRecycler = (PullRecycler) view.findViewById(R.id.pullRecycler);
        setUpRecyclerView();
        return view;
    }


    private void setUpRecyclerView() {
        mRecycler.setOnRefreshListener(this);
        mRecycler.setEmptyView(R.drawable.icon_no_content_pk, "", "");
        mRecycler.setLayoutManger(new MyLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mRecycler.addItemDecoration(getItemDecoration());
        mAdapter = new PkAdapter(context, mDatas, 0, getSupportMultiType());
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setOnRefreshListener(new onRefreshListener() {
            @Override
            public void onRefresh(boolean refresh) {
                PkLiveController.this.onRefresh(refresh);
            }
        });
        mRecycler.setRefresh();
        mRecycler.setEnableLoadMore(false);
    }

    private RecyclerView.ItemDecoration getItemDecoration() {
        return new DashLineItemDecoration();
    }

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
    public void onRefresh(boolean refresh) {
        if (refresh) {
            mCurPage = 1;
        }
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.PK_GAME_BOARD_QUERY, true);
        map.put("channelAreaId", CurLiveInfo.channelAreaId);
        map.put("queryNow", isQueryNow);
        map.setGenericClaz(PkGameBoardEntity.class);
        mHttpHelper.startRequest(map);
    }

    @Override
    public void onSuccess(RequestContainer request, Object obj) {
        switch (((XjqUrlEnum) request.getRequestEnum())) {
            case PK_GAME_BOARD_QUERY:
                PkGameBoardEntity pkGameBoardEntity = (PkGameBoardEntity) obj;
                multipleList = pkGameBoardEntity.multipleList;
                loadCompleted(pkGameBoardEntity == null ? null : pkGameBoardEntity.getPkGameBoardList());
                break;
            case PURCHASE_PK_NORMAL:
                ToastUtil.showLong(context, "赠送成功");
                break;
        }

    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        loadCompleted(null);
        ErrorBean errorBean = new ErrorBean(jsonObject);
        if (errorBean != null && errorBean.getError() != null && "BOARD_DATA_STOP_SELL".equals(errorBean.getError().name)) {
            ToastUtil.showShort(context, "当前PK局已停止，试试别的PK局吧~");
            return;
        }
        ((BaseActivity) context).operateErrorResponseMessage(jsonObject);
    }

    private void loadCompleted(ArrayList<PkGameBoarInfoBean> list) {
        mRecycler.showContentView();
        //刷新
        if (mCurPage == 1) {
            //空数据
            if ((list == null || list.size() == 0) && mDatas.size() == 0) {
                //设置空白页面,如果刷新之前有数据还是显示旧的数据
                mRecycler.showEmptyView(SpannableStringHelper.changeTextColor(context.getString(R.string.live_pk_empty), Color.WHITE));
            } else if (list != null && list.size() > 0) {
                mDatas.clear();
                mDatas.addAll(list);
            }
        } else {
            //加载更多
            if (list != null && list.size() > 0)
                mDatas.addAll(list);
        }
        mRecycler.refreshCompleted();
        mCurPage++;
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
            mHttpHelper.startRequest(map, true);
        }

        @Override
        public void sharePk(PkGameBoarInfoBean infoBean) {
            ShareGroupListDialogFragment.newInstance(true, infoBean.id, "PK_GAME_BOARD", "我在直播间怒砸N金锭参与PK")
                    .show(((BaseActivity) context).getSupportFragmentManager());
        }

        @Override
        public void showPurchaseList(PkOptionEntryBean optionEntryBean) {
            new InvitedGuestDialog.Builder(context).setTitle("投注人员").create().
                    generateGuestData(optionEntryBean.boardId, optionEntryBean.id, CurLiveInfo.channelAreaId).show();
        }

        @Override
        public void showPersonCard(List<PkOptionEntryBean.RankUserListBean> userList, int position) {
            try {
                PkOptionEntryBean.RankUserListBean rankUserListBean = userList.get(position);
                new PersonalInfoDialog(context, rankUserListBean.getUserId()).show();
            } catch (Exception e) {
            }
        }
    };

    private class PkAdapter extends MultiTypeSupportAdapter<PkGameBoarInfoBean> {

        public PkAdapter(Context context, List<PkGameBoarInfoBean> list, int layoutRes, MultiTypeSupport typeSupport) {
            super(context, list, layoutRes, typeSupport);
        }

        @Override
        public void onBindNormalHolder(ViewHolder holder, PkGameBoarInfoBean pkGameBoarInfoBean, int position) {
            PkGameUtils.bindViewHolder(context, holder, pkGameBoarInfoBean, multipleList, position, purchasePkListener);
        }
    }
}
