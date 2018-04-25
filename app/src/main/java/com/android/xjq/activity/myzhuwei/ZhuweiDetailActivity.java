package com.android.xjq.activity.myzhuwei;

import android.text.Html;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.myzhuwei.widget.GiftListView;
import com.android.xjq.activity.myzhuwei.widget.MyZhuweiListView;
import com.android.xjq.bean.myzhuwei.ZhuweiDetailBean;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kokuma on 2017/11/5.
 */

public class ZhuweiDetailActivity extends BaseActivity4Jczj implements IHttpResponseListener {
    //
    private WrapperHttpHelper httpHelper;

    String id = "";

    ZhuweiDetailBean detailBean;

    @BindView(R.id.layoutTopInfo)
    View layoutTopInfo;

    @BindView(R.id.tvHotNum)
    TextView tvHotNum;

    @BindView(R.id.tvRewardNum)
    TextView tvRewardNum;

    @BindView(R.id.changci_txt)
    TextView changci_txt;

    @BindView(R.id.tvTeams)
    TextView tvTeams;

    @BindView(R.id.tvFeng)
    TextView tvFeng;

    @BindView(R.id.tvMyTeam)
    TextView tvMyTeam;
    //头像部分
    @BindView(R.id.portraitIv)
    CircleImageView portraitIv;

    @BindView(R.id.ivStatus)
    ImageView ivStatus;

    @BindView(R.id.tvUsername)
    TextView tvUsername;

    @BindView(R.id.tvTime)
    TextView tvTime;

    @BindView(R.id.listviewGift)
    GiftListView listviewGift;


    @Override
    public void onSuccess(RequestContainer request, Object result) {
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        if (requestEnum == XjqUrlEnum.PURCHASE_ORDER_SUMMARY_DETAIL_QUERY && result != null) {
            ZhuweiDetailBean bean = (ZhuweiDetailBean) result;
            this.detailBean = bean;
            if (bean != null && bean.isSuccess()) {
                listviewGift.setPrizeStatus(bean.getPurchaseOrderSummaryClientSimple().getPrizeStatus().getName());
                listviewGift.update(bean.getOrderFundBillDetailClientSimples());
            }
            setUI();
        }
    }

    void setUI() {
        if (detailBean == null) {
            return;
        }
        ZhuweiDetailBean.PurchaseOrderBean purchaseBean = detailBean.getPurchaseOrderSummaryClientSimple();
        ZhuweiDetailBean.GameBoardBean gameBoardBean = detailBean.getGameBoard();
        if (purchaseBean != null && gameBoardBean != null&&gameBoardBean.getHomeTeamName()!=null) {
            tvHotNum.setText(MyZhuweiListView.getIntNum("" + purchaseBean.getTotalFee()));
            //   tvRewardNum.setText(""+purchaseBean.getPrizeFee());
            PicUtils.load(this, portraitIv, purchaseBean.getUserLogoUrl());
            tvUsername.setText(purchaseBean.getUserAlias());
            tvTime.setText(purchaseBean.getGmtCreate());
            //让球让分
            String txtFeng = gameBoardBean.getHomeTeamName() ;  //+ "让"
            if (gameBoardBean.getPlate() > 0) {
                txtFeng += "<font color='#f7453d'>" + "+" + gameBoardBean.getPlate() + "</font>" ;
            } else {
                txtFeng += "<font color='#009900'>" + gameBoardBean.getPlate()+ "</font>";
            }
            if (gameBoardBean.getRaceType() != null && gameBoardBean.getRaceType().equals("FOOTBALL")) {
                txtFeng += "球";
            } else {
                txtFeng += "分";
            }
            tvFeng.setText(Html.fromHtml(txtFeng+"，你支持哪支球队获胜？"));

            //让球让分
            if (gameBoardBean.getRaceStageType() != null&&gameBoardBean.getRaceStageType().getName()!=null) {
                String nameChang = "[" + gameBoardBean.getRaceStageType().getName() + "]比分";
                changci_txt.setText(nameChang);
            }

            if(gameBoardBean.getHomeTeamName()!=null&&gameBoardBean.getGuestTeamName()!=null){
                tvTeams.setText(gameBoardBean.getHomeTeamName()+"VS"+gameBoardBean.getGuestTeamName());
            }

            //赢平负
            if (purchaseBean.getEntryValue() != null  && purchaseBean.getEntryValue().equals("HOME_WIN")) {
                tvMyTeam.setText("助威："+gameBoardBean.getHomeTeamName());
            } else if (purchaseBean.getEntryValue() != null  && purchaseBean.getEntryValue().equals("GUEST_WIN")) {
                tvMyTeam.setText("助威："+gameBoardBean.getGuestTeamName());
            }

            if ("WIN".equals(purchaseBean.getOrderStatus())) {
                ivStatus.setImageResource(R.drawable.bg_success);
                tvRewardNum.setTextColor(getResources().getColor(R.color.main_red));
                tvRewardNum.setText(purchaseBean.getPrizeFee() + "");
            } else if ("LOSE".equals(purchaseBean.getOrderStatus())) {
                ivStatus.setImageResource(R.drawable.bg_loss);
                tvRewardNum.setTextColor(getResources().getColor(R.color.text_black));
                tvRewardNum.setText(purchaseBean.getPrizeFee() + "");
            } else if ("CANCEL_FINISH".equals(purchaseBean.getOrderStatus())) {
                ivStatus.setImageResource(R.drawable.bg_cancel);
                tvRewardNum.setTextColor(getResources().getColor(R.color.text_black));
                tvRewardNum.setText("-");
            } else if ("PROGRESSING".equals(purchaseBean.getOrderStatus())) {
                ivStatus.setImageBitmap(null);
                tvRewardNum.setText("");
            } else if ("WAIT_PRIZE".equals(purchaseBean.getOrderStatus())) {
                ivStatus.setImageBitmap(null);
                tvRewardNum.setText("");
            }else {
                ivStatus.setImageBitmap(null);
            }
            listviewGift.orderStatus = purchaseBean.getOrderStatus();
            ((BaseAdapter)(listviewGift.getAdapter())).notifyDataSetChanged();
        }

    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {

    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_zhuwei_detail);
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpView() {
        //
    }

    /*@OnClick(R.id.ivBack)
    void back(){
        finish();
    }*/

    @Override
    protected void setUpData() {
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
        }
        setUpToolbar(getString(R.string.zhuwei_detail), -1, MODE_BACK);
        httpHelper = new WrapperHttpHelper(this);
        getData();
    }

    void getData() {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.PURCHASE_ORDER_SUMMARY_DETAIL_QUERY, true);
        container.put("id", id);
        container.setGenericClaz(ZhuweiDetailBean.class);
        httpHelper.startRequest(container, false);
    }

}
