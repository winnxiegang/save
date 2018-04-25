package com.android.xjq.dialog.live;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.DimensionUtils;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.Money;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.expandtv.NumLimitEditText;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.live.main.gift.GiftInfoBean;
import com.android.xjq.bean.live.main.gift.SendGiftBean;
import com.android.xjq.dialog.base.BaseDialog;
import com.android.xjq.dialog.base.ViewHolder;
import com.android.xjq.model.gift.PayType;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.xjq.utils.XjqUrlEnum;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 礼物附加信息弹窗
 * <p>
 * Created by lingjiu on 2018/2/1.
 */

public class LiveGiftSupplementDialog extends BaseDialog implements IHttpResponseListener {
    private static final int SEND_STATUS_START = 0;//发起ing
    private static final int SEND_STATUS_COMPLETED = 1;//发起成功
    private static final int SEND_STATUS_FAILED = 2;//发起失败

    @BindView(R.id.headerIv)
    ImageView headerIv;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.descTv)
    TextView descTv;
    @BindView(R.id.portraitIv)
    CircleImageView portraitIv;
    @BindView(R.id.descEt)
    NumLimitEditText descEt;
    @BindView(R.id.giftIv)
    ImageView giftIv;
    @BindView(R.id.giftTv)
    TextView giftTv;
    @BindView(R.id.giftValueTv)
    TextView giftValueTv;
    @BindView(R.id.confirmBtn)
    TextView confirmBtn;

    private GiftInfoBean giftInfoBean;
    private String payType;
    private WrapperHttpHelper httpHelper;
    private List<SendGiftBean> sendGiftList = new ArrayList<>();

    @OnClick(R.id.confirmBtn)
    public void sendGift() {
        changeSendStatus(SEND_STATUS_START);
        SendGiftBean sendGiftBean = new SendGiftBean();
        sendGiftBean.setGiftConfigId(giftInfoBean.getId());
        sendGiftBean.setTotalCount(1);
        sendGiftBean.setPayType(payType);
        sendGiftBean.setTotalAmount(giftInfoBean.getPrice() * 1 + "");
        sendGiftList.clear();
        sendGiftList.add(sendGiftBean);
        String title = TextUtils.isEmpty(descEt.getText()) ?
                TextUtils.equals(giftInfoBean.getCode(), "IMPERIAL_EDICT") ? mContext.getString(R.string.live_send_imperial_default_txt) :
                        mContext.getString(R.string.live_send_rocket_default_txt) : descEt.getText().toString();
        RequestFormBody map = new RequestFormBody(XjqUrlEnum.PLAY_CREATE_GIFT_PURCHASE, true);
        //参数为giftConfigId, totalCount, payType, totalAmount
        map.put("giftPurchaseParams", new Gson().toJson(sendGiftList));
        map.put("platformObjectId", CurLiveInfo.channelAreaId);
        map.put("title", title);
        httpHelper.startRequest(map);
    }

    /* 圣旨默认文案：快来接旨抢红包！
       火箭默认文案：火箭升空，倒计时！*/
    public LiveGiftSupplementDialog(Context context, GiftInfoBean giftInfoBean, String payType, boolean isLandscape) {
        super(context);
        //setShowBottom(true);
        //横竖屏时分别设定不同的宽度值、弹出方位以及动画
        if (!isLandscape) {
            setShowBottom(true);
        } else {
            setIsNeedConverPx(false);
            setWidth(DimensionUtils.getScreenHeight(context));
            setAnimStyle(R.style.dialog_anim_bottom);
            setGravity(Gravity.CENTER);
        }
        this.giftInfoBean = giftInfoBean;
        this.payType = payType;
        httpHelper = new WrapperHttpHelper(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (giftInfoBean != null) {
            giftTv.setText(giftInfoBean.getName());
            giftValueTv.setText(new Money(giftInfoBean.getPrice()).toSimpleString());
            PicUtils.load(mContext.getApplicationContext(), giftIv, giftInfoBean.getGiftImageUrl());
        }
        PicUtils.load(mContext.getApplicationContext(), portraitIv, LoginInfoHelper.getInstance().getUserLogoUrl());

        Drawable drawable = null;
        if (PayType.POINT_COIN.name().equals(payType)) {
            drawable = ContextCompat.getDrawable(mContext, R.drawable.icon_silver_banana_coin);
        } else if (PayType.GIFT_COIN.name().equals(payType)) {
            drawable = ContextCompat.getDrawable(mContext, R.drawable.icon_silver_gift_coin);
        }
        giftValueTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

        if (TextUtils.equals(giftInfoBean.getCode(), "IMPERIAL_EDICT")) {
            headerIv.setVisibility(View.GONE);
            titleTv.setText("");
            titleTv.setBackgroundResource(R.drawable.icon_send_imperial_edict_bg);
            descTv.setText("全平台广播你的圣旨,直播间一起接旨!");
        }
        descEt.requestFocus();
    }

    private void changeSendStatus(int status) {
        switch (status) {
            case SEND_STATUS_START:
                confirmBtn.setEnabled(false);
                confirmBtn.setText("发送中..");
                confirmBtn.setAlpha(0.5f);
                break;
            case SEND_STATUS_COMPLETED:
                confirmBtn.setEnabled(true);
                confirmBtn.setText("发送成功");
                confirmBtn.setAlpha(1f);
                confirmBtn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showLong(mContext, "礼物赠送成功");
                        dismiss();
                    }
                }, 200);
                break;
            case SEND_STATUS_FAILED:
                confirmBtn.setEnabled(true);
                confirmBtn.setText("发送失败");
                confirmBtn.setAlpha(1f);
                break;
        }
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_live_gift_supplement;
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialog dialog) {
        final EditText descEt = holder.getView(R.id.descEt);
        descEt.post(new Runnable() {
            @Override
            public void run() {
                LibAppUtil.showSoftKeyboard(mContext, descEt);
            }
        });
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        changeSendStatus(SEND_STATUS_COMPLETED);
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        changeSendStatus(SEND_STATUS_FAILED);
        ((BaseActivity) mContext).operateErrorResponseMessage(jsonObject);
    }
}
