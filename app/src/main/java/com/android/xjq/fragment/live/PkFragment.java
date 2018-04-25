package com.android.xjq.fragment.live;

import android.os.Bundle;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.gamePK.PkGameBoarInfoBean;
import com.android.xjq.bean.gamePK.PkGameBoardEntity;
import com.android.xjq.fragment.BaseListFragment;
import com.android.xjq.model.SaleStatusEnum;
import com.android.xjq.utils.PkGameUtils;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

/**
 * Created by lingjiu on 2018/2/26.
 */

public class PkFragment extends BaseListFragment<PkGameBoarInfoBean> implements IHttpResponseListener<PkGameBoardEntity> {
    public WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);
    private boolean isQueryNow;


    public static PkFragment newInstance(boolean isQueryNow) {
        PkFragment fragment = new PkFragment();
        Bundle args = new Bundle();
        args.putBoolean("isQueryNow", isQueryNow);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        isQueryNow = arguments.getBoolean("isQueryNow");
    }

    @Override
    protected void onSetUpView() {
        setLoadMoreEnable(true);
    }

    @Override
    public MultiTypeSupport getSupportMultiType() {
        // R.layout.list_item_pk_progressing; list_item_pk_pause,list_item_pk_invalid
        return new MultiTypeSupport<PkGameBoarInfoBean>() {
            @Override
            public int getTypeLayoutRes(PkGameBoarInfoBean data, int pos) {
                switch (SaleStatusEnum.safeValueOf(data.saleStatus != null ? null : data.saleStatus.getName())) {
                    case PROGRESSING:
                        return R.layout.list_item_pk_progressing;
                    case WAIT:
                    case PAUSE:
                        return R.layout.list_item_pk_pause;
                    case FINISH:
                        //"PRIZEING"表示派奖中"FINISH"表示结束派奖
                        if ("FINISH".equals(data.getPrizeStatus())) {
                            return R.layout.list_item_pk_completed;
                        }
                        return R.layout.list_item_pk_pause;
                    case CANCEL:
                        return R.layout.list_item_pk_invalid;
                }
                return 0;
            }
        };
    }

    @Override
    public void onBindHolder(ViewHolder holder, PkGameBoarInfoBean infoBean, int position) {
        PkGameUtils.bindViewHolder(getContext(),holder,infoBean,null,position,null);
    }


    @Override
    public void onRefresh(boolean refresh) {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.PK_GAME_BOARD_QUERY, true);
        map.put("channelAreaId", "8201711028675848");
        map.put("queryNow", isQueryNow);
        mHttpHelper.startRequest(map);
    }

    @Override
    public void onSuccess(RequestContainer request, PkGameBoardEntity pkGameBoardEntity) {
        loadCompleted(pkGameBoardEntity == null ? null : pkGameBoardEntity.getPkGameBoardList());
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        loadCompleted(null);
        ((BaseActivity) getActivity()).operateErrorResponseMessage(jsonObject);
    }
}
