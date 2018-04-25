package com.android.xjq.dialog.live;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.SpannableStringHelper;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.gamePK.PurchaseListEntity;
import com.android.xjq.dialog.base.BaseDialog;
import com.android.xjq.dialog.base.ViewHolder;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lingjiu on 2018/3/5.
 */

public class LivePurchaseListDialog extends BaseDialog implements IHttpResponseListener<PurchaseListEntity> {


    @BindView(R.id.share_title)
    TextView shareTitle;
    @BindView(R.id.closeIv)
    ImageView closeIv;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    //    private List<>
    private String gameBoardId;
    private List<PurchaseListEntity.PurchaseInfoBean> purchaseInfoBeans;

    public LivePurchaseListDialog(Context context, String gameBoardId) {
        super(context);
        this.gameBoardId = gameBoardId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryPurchaseList();
        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(new PurchasePkListAdapter());
    }

    private void queryPurchaseList() {
        WrapperHttpHelper httpHelper = new WrapperHttpHelper(this);
        RequestFormBody map = new RequestFormBody(XjqUrlEnum.PURCHASE_USER_LIST_QUERY_BY_OPTION, true);
        map.put("gameBoardOptionId", gameBoardId);
        httpHelper.startRequest(map);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_live_purchase_list_layout;
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialog dialog) {

    }

    @Override
    public void onSuccess(RequestContainer request, PurchaseListEntity purchaseListEntity) {
        purchaseListEntity.operatorData();
        purchaseInfoBeans = purchaseListEntity.userInfos;
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        ((BaseActivity) mContext).operateErrorResponseMessage(jsonObject);
    }

    class PurchasePkListAdapter extends RecyclerView.Adapter<PurchasePkListAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(View.inflate(parent.getContext(), R.layout.dialog_pk_purchase_list_item, null));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            PurchaseListEntity.PurchaseInfoBean bean = purchaseInfoBeans.get(position);
            PicUtils.load(mContext, holder.portraitIv, bean.userLogoUrl);
            holder.userNameTv.setText(bean.userName);
            String giftNum = String.format(mContext.getString(R.string.purchase_gift_num), String.valueOf(bean.multiple));
            holder.purchaseNumTv.setText(SpannableStringHelper.changeTextColor(giftNum,
                    3, giftNum.length(), ContextCompat.getColor(mContext, R.color.orange2)));
            holder.fansTv.setText(String.format(mContext.getString(R.string.focus_and_fans_number), String.valueOf(bean.attentionNum), String.valueOf(bean.fansNum)));
        }


        @Override
        public int getItemCount() {
            return purchaseInfoBeans == null ? 0 : purchaseInfoBeans.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.portraitIv)
            CircleImageView portraitIv;
            @BindView(R.id.userNameTv)
            TextView userNameTv;
            @BindView(R.id.fansTv)
            TextView fansTv;
            @BindView(R.id.purchaseNumTv)
            TextView purchaseNumTv;
            @BindView(R.id.focusTv)
            TextView focusTv;

            public ViewHolder(View itemView) {
                super(itemView);
                portraitIv = (CircleImageView) findViewById(R.id.portraitIv);
                userNameTv = (TextView) findViewById(R.id.userNameTv);
                fansTv = (TextView) findViewById(R.id.fansTv);
                purchaseNumTv = (TextView) findViewById(R.id.purchaseNumTv);
                focusTv = (TextView) findViewById(R.id.focusTv);
            }
        }

    }
}
