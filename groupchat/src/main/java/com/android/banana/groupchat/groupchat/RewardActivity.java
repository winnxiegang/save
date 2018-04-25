package com.android.banana.groupchat.groupchat;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.dialog.BottomPayPop;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.view.ToggleEditTextView;
import com.android.banana.commlib.view.expandtv.PayPsdInputView;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.groupchat.bean.GiftCoinAcountBean;
import com.android.banana.groupchat.chat.SimpleChatActivity;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.utils.KeyboardHelper;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by qiaomu on 2017/11/2.
 * <p>
 * ReceiveUserId	String	否	接受人用户id
 * payType	CurrencyTypeEnum	否	货币类型
 * 枚举字段如下：
 * POINT_COIN => 点券
 * GOLD_COIN => 金币
 * GIFT_COIN => 礼金
 * rewardAmount	Money	否	打赏金额
 */

public class RewardActivity extends BaseActivity4Jczj implements IHttpResponseListener, View.OnClickListener {
    private ImageView mAvartImg;
    private TextView userName, goldTv, rewardTv;
    private ToggleEditTextView amountEt, rewardDesEt;

    private WrapperHttpHelper mHttpHelper;
    private GiftCoinAcountBean giftCoinAcount;
    private String reciverUserId, identifyId;
    private GiftCoinAcountBean mGiftCoinAcount;
    private BottomPayPop payPop;
    private boolean isFromLiveRoom;

    /**
     * @param from
     * @param reciverUserId 接受打赏的userid
     * @param identifyId    私聊认证identifyId，传null的话会自动获取identifyId
     */
    public static void startRewardActivity(Context from, String reciverUserId, String identifyId) {
        Intent intent = new Intent(from, RewardActivity.class);
        intent.putExtra("reciverUserId", reciverUserId);
        intent.putExtra("identifyId", identifyId);
        from.startActivity(intent);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        reciverUserId = getIntent().getStringExtra("reciverUserId");
        identifyId = getIntent().getStringExtra("identifyId");
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_reward, getString(R.string.reward_title), -1, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        mAvartImg = findViewOfId(R.id.portraitIv);
        userName = findViewOfId(R.id.user_name);
        goldTv = findViewOfId(R.id.reward_gold_tv);
        rewardTv = findViewOfId(R.id.reward_tv);

        rewardDesEt = findViewOfId(R.id.reward_des);
        amountEt = findViewOfId(R.id.reward_clear_et);
        rewardTv.setOnClickListener(this);
        amountEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onClick(v);
                return false;
            }
        });
    }

    @Override
    protected void setUpData() {

        mHttpHelper = new WrapperHttpHelper(this);
        showProgressDialog();
        doDirectRewardRequest();
        doGiftAcountQuery();
    }

    private void doGiftAcountQuery() {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.COIN_REWARD_CONFIG_QUERY, true);
        formBody.put("timestamp", System.currentTimeMillis());
        formBody.put("receiveUserId", reciverUserId);
        formBody.setGenericClaz(GiftCoinAcountBean.class);
        mHttpHelper.startRequest(formBody, true);
    }

    private void doDirectRewardRequest() {
        isFromLiveRoom = TextUtils.isEmpty(identifyId);
        if (!isFromLiveRoom)
            return;
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.COIN_REWARD_INIT, true);
        formBody.put("timestamp", System.currentTimeMillis());
        formBody.put("receiveUserId", reciverUserId);
        mHttpHelper.startRequest(formBody, true);
    }

    private void onQueryGiftAcountSuccess(GiftCoinAcountBean giftCoinAcount) {
        closeProgressDialog();
        if (giftCoinAcount == null) {
            return;
        }
        mGiftCoinAcount = giftCoinAcount;
        BigDecimal bigDecimal = new BigDecimal(mGiftCoinAcount.goldCoinAmount);
        String result = bigDecimal.toString();
        String normal = getString(R.string.reward_amount);
        String span = String.format(getString(R.string.reward_amount_span), result);

        SpannableStringBuilder builder = new SpannableStringBuilder(span);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.light_text_color));
        builder.setSpan(colorSpan, 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.8f);
        builder.setSpan(sizeSpan, 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        goldTv.setText(normal);
        goldTv.append(builder);

        PicUtils.load(this, mAvartImg, giftCoinAcount.receiveUserLogoUrl);
        userName.setText(giftCoinAcount.receiveUserLoginName);
    }

    @Override
    public void onSuccess(RequestContainer request, Object result) {
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        switch (requestEnum) {
            case COIN_REWARD_CONFIG_QUERY:
                onQueryGiftAcountSuccess((GiftCoinAcountBean) result);
                break;
            case COIN_REWARD_INIT:
                try {
                    JSONObject object = new JSONObject((result).toString());
                    identifyId = object.getString("identifyId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case COIN_REWARD:
                payPop.setPayResult(true);
                KeyboardHelper.hideSoftInput(amountEt);
                showToast(getString(R.string.reward_sucess));
                //如果从直播间直接进的打赏页面，打赏成功后要进入私聊页面
                if (isFromLiveRoom) {
                    SimpleChatActivity.startSimpleChatActivity(this, reciverUserId, mGiftCoinAcount.receiveUserLoginName, null);
                }
                payPop.dismiss();
                this.finish();
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        switch (requestEnum) {
            case COIN_REWARD_CONFIG_QUERY:
                operateErrorResponseMessage(jsonObject);
                onQueryGiftAcountSuccess(null);
                break;
            case COIN_REWARD_INIT:
                operateErrorResponseMessage(jsonObject);
                break;
            case COIN_REWARD:
                payPop.setPayResult(false);
                ErrorBean errorBean = new ErrorBean(jsonObject);
                if (errorBean != null) {
                    if (TextUtils.equals(errorBean.getError().getName(), "ACCOUNT_PASSWORD_NOT_SET")) {
                        toInitSetPayPwdActivity();
                    }
                }
                operateErrorResponseMessage(jsonObject);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (mGiftCoinAcount == null)
            return;
        String amount = amountEt.getText().toString();
        if (TextUtils.isEmpty(amount))
            return;

        double amountNum = Double.parseDouble(amount);
        if (amountNum < mGiftCoinAcount.minRewardAmount) {
            showToast(String.format(getString(R.string.min_reward_amount_request), mGiftCoinAcount.minRewardAmount));
            return;
        }

        if (amountNum > mGiftCoinAcount.maxRewardAmount) {
            showToast(String.format(getString(R.string.max_reward_amount_request), mGiftCoinAcount.maxRewardAmount));
            return;
        }

        if (!mGiftCoinAcount.accountPasswordSet) {
            ShowMessageDialog dialog = new ShowMessageDialog(this, R.drawable.icon_dialog_title_warning, null, null, new OnMyClickListener() {
                @Override
                public void onClick(View v) {
                    toInitSetPayPwdActivity();
                }
            }, null, getString(R.string.init_pwd_first));

        } else {
            payPop = new BottomPayPop(this, new PayPsdInputView.OnPasswordListener() {

                @Override
                public void inputListener(boolean isInputComplete, String psd) {
                    doReward(psd);
                }
            });
            hideSoftKeyboard();
            payPop.showPop();
        }
    }

    private void doReward(String pwd) {
        RequestFormBody formBody = new RequestFormBody(JczjURLEnum.COIN_REWARD, true);
        formBody.put("receiveUserId", reciverUserId);
        formBody.put("payType", "GOLD_COIN");
        LogUtils.e("kk", "identifyId+___" + identifyId);
        formBody.put("identifyId", identifyId);
        formBody.put("rewardAmount", amountEt.getText().toString());
        formBody.put("accountPassword", pwd);
        mHttpHelper.startRequest(formBody);
    }

    private void toInitSetPayPwdActivity() {
        Intent intent = new Intent("com.android.xjq.initsetpassword");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            mGiftCoinAcount.accountPasswordSet = true;
        }
    }
}
