package com.android.banana.commlib.coupon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.bean.SendCouponConfig;
import com.android.banana.commlib.coupon.couponenum.CouponEnum;
import com.android.banana.commlib.coupon.couponenum.CouponPlatformType;
import com.android.banana.commlib.coupon.couponenum.CouponPublishType;
import com.android.banana.commlib.coupon.couponenum.CouponUrlEnum;
import com.android.banana.commlib.dialog.BottomPayPop;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.eventBus.EventBusMessage;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.HhsUtils;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.CommonStatusLayout;
import com.android.banana.commlib.view.expandtv.PayPsdInputView;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.android.banana.commlib.coupon.couponenum.CouponUrlEnum.GET_COUPON_CONFIG;


/**
 * Created by lingjiu on 2017/7/21.
 */

public class SendCouponPop extends BottomAndSidePop implements OnHttpResponseListener, View.OnClickListener {
    RadioButton normalRb;
    RadioButton luckyRb;
    RadioGroup tabRadioGroup;
    TextView couponNumTv;
    EditText inputCouponNumEt;
    LinearLayout couponNumLayout;
    TextView couponMoneyTv;
    EditText inputCouponMoneyEt;
    TextView exchangeGoldTv;
    LinearLayout couponMoneyLayout;
    EditText messageEt;
    LinearLayout messageLayout;
    RadioButton nowSendRb;
    RadioButton timingSendRb;
    RadioGroup sendTimeRg;
    TextView timingTv;
    Button sendCouponBtn;
    TextView expireTv;
    FrameLayout contentLayout;
    CommonStatusLayout statusLayout;

    private HttpRequestHelper httpRequestHelper;
    //红包配置
    private SendCouponConfig sendCouponConfig;
    private ArrayList<Date> timeList;
    //当前选择的发红包时间
    private Date sendCouponDate;
    //当前红包数量
    private int couponCount;
    //当前红包金额
    private int couponMoney;
    //当前发红包所在的平台id以及type
    private String objectId;
    private String objectType;

    private SendCouponSuccessListener sendCouponSuccessListener;
    //是否设置过密码
    private boolean accountPasswordSet;
    private String title;
    private BottomPayPop bottomPayPop;

    public void setSendCouponSuccessListener(SendCouponSuccessListener sendCouponSuccessListener) {
        this.sendCouponSuccessListener = sendCouponSuccessListener;
    }


    public void close() {
        dismiss();
    }


    public void exchangeGold() {
        Intent intent = new Intent("com.android.xjq.betting_game");
        mContext.startActivity(intent);
    }

    public void selectTimeSendCoupon() {
        SendCouponTimePop pop = new SendCouponTimePop(getContext(), timingTv, sendCouponTimeListener);
        pop.showUp(timeList);
    }

    public void sendCoupon() {
        // 没设置支付密码,要先设置支付密码
        if (!accountPasswordSet) {
            goSetPassword();
        } else {
            inputPassword();
        }

    }

    private void createCoupon(String psd) {
        if (sendCouponConfig == null) return;

        if (!validate()) return;

        XjqRequestContainer map = new XjqRequestContainer(CouponUrlEnum.COUPON_CREATE, true);
        //map.put("accountPassword", psd);
        map.put("sendUserId", LoginInfoHelper.getInstance().getUserId());
        map.put("couponFetchType", "ALL");//FOLLOW_USER
        map.put("amountAllocateType", normalRb.isChecked() ? "AVERAGE" : "RANDOM");//AVERAGE
        map.put("couponType", normalRb.isChecked() ? CouponEnum.NORMAL_GROUP_COUPON.name() : CouponEnum.LUCKY_GROUP_COUPON.name());
        map.put("couponPublishType", timingSendRb.isChecked() ? CouponPublishType.TIMED.name() : CouponPublishType.IMMEDIATE.name());
        if (timingSendRb.isChecked()) {
            map.put("gmtPublish", TimeUtils.getFormatTime(sendCouponDate, TimeUtils.LONG_DATEFORMAT));
        }
        map.put("platformObjectType", objectType);
        map.put("platformObjectId", objectId);//channelId
        map.put("objectType", objectType);
        if (TextUtils.isEmpty(messageEt.getText())) {
            title = messageEt.getHint().toString();
        } else {
            title = messageEt.getText().toString();
        }
        map.put("title", title);
        String totalAmount;
        if (normalRb.isChecked()) {
            totalAmount = "" + Integer.valueOf(inputCouponMoneyEt.getText().toString()) * Integer.valueOf(inputCouponNumEt.getText().toString());
        } else {
            totalAmount = inputCouponMoneyEt.getText().toString();
        }
        map.put("totalAmount", totalAmount);
        map.put("totalCount", inputCouponNumEt.getText().toString());
        httpRequestHelper.startRequest(map, false);
    }

    private void inputPassword() {
        if (bottomPayPop == null) {
            bottomPayPop = new BottomPayPop(mContext, new PayPsdInputView.OnPasswordListener() {
                @Override
                public void inputListener(boolean isInputComplete, String psd) {
                    createCoupon(psd);
                }
            });
        }
        bottomPayPop.showPop();
    }

    private void goSetPassword() {
        ShowMessageDialog dialog = new ShowMessageDialog(mContext, R.drawable.icon_dialog_title_warning, "去设置", "取消", new OnMyClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.android.xjq.initsetpassword");
                mContext.startActivity(intent);
            }
        }, null, mContext.getString(R.string.account_password_set));
    }

    public SendCouponPop(Context context, int orientation, String objectType, String objectId) {
        super(context, R.layout.pop_send_coupon, orientation);
        this.objectType = objectType;
        this.objectId = objectId;
    }

    @Override
    protected void onCreate() {
        httpRequestHelper = new HttpRequestHelper(mContext, this);

        normalRb = (RadioButton) parentView.findViewById(R.id.normalRb);
        luckyRb = (RadioButton) parentView.findViewById(R.id.luckyRb);
        tabRadioGroup = (RadioGroup) parentView.findViewById(R.id.tabRadioGroup);
        couponNumTv = (TextView) parentView.findViewById(R.id.couponNumTv);
        inputCouponNumEt = (EditText) parentView.findViewById(R.id.inputCouponNumEt);
        exchangeGoldTv = (TextView) parentView.findViewById(R.id.exchangeGoldTv);
        couponMoneyLayout = (LinearLayout) parentView.findViewById(R.id.couponMoneyLayout);
        messageEt = (EditText) parentView.findViewById(R.id.messageEt);
        messageLayout = (LinearLayout) parentView.findViewById(R.id.messageLayout);
        sendTimeRg = (RadioGroup) parentView.findViewById(R.id.sendTimeRg);
        sendCouponBtn = (Button) parentView.findViewById(R.id.sendCouponBtn);
        expireTv = (TextView) parentView.findViewById(R.id.expireTv);
        contentLayout = (FrameLayout) parentView.findViewById(R.id.contentLayout);
        statusLayout = (CommonStatusLayout) parentView.findViewById(R.id.statusLayout);
        timingTv = (TextView) parentView.findViewById(R.id.timingTv);
        couponMoneyTv = (TextView) parentView.findViewById(R.id.couponMoneyTv);
        inputCouponMoneyEt = (EditText) parentView.findViewById(R.id.inputCouponMoneyEt);
        nowSendRb = (RadioButton) parentView.findViewById(R.id.nowSendRb);
        timingSendRb = (RadioButton) parentView.findViewById(R.id.timingSendRb);
        couponNumLayout = ((LinearLayout) parentView.findViewById(R.id.couponNumLayout));
        exchangeGoldTv = (TextView) parentView.findViewById(R.id.exchangeGoldTv);

        // parentView.findViewById(R.id.closeIv).setOnClickListener(this);
        sendCouponBtn.setOnClickListener(this);
        exchangeGoldTv.setOnClickListener(this);
        timingTv.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        LogUtils.e("SendCouponPop", "onResume");
        httpRequestHelper.startRequest(new XjqRequestContainer(GET_COUPON_CONFIG, true), false);
        if (CouponPlatformType.GROUP_CHAT.name().equals(objectType))
            parentView.findViewById(R.id.sendTimeLayout).setVisibility(View.INVISIBLE);
        setListener();
    }

    private void setListener() {
        tabRadioGroup.setOnCheckedChangeListener(tabRgCheckListener);
        sendTimeRg.setOnCheckedChangeListener(sendTimeRgCheckListener);
        inputCouponNumEt.setOnFocusChangeListener(focusChangedListener);
        inputCouponMoneyEt.setOnFocusChangeListener(focusChangedListener);
        messageEt.setOnFocusChangeListener(focusChangedListener);
        inputCouponNumEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sendCouponBtn.setEnabled(false);
                couponCount = 0;
                if (!inputValid(inputCouponNumEt)) {
                    couponMoney = 0;
                } else {
                    couponCount = Integer.valueOf(s.toString());

                    if (inputValid(inputCouponMoneyEt)) {
                        sendCouponBtn.setEnabled(true);
                        couponMoney = Integer.valueOf(inputCouponMoneyEt.getText().toString());
                        if (normalRb.isChecked()) {
                            couponMoney = couponCount * Integer.valueOf(inputCouponMoneyEt.getText().toString());
                        }
                    }
                }

                sendCouponBtn.setText("立即支付" + HhsUtils.formatMoney(couponMoney) + "礼金");
            }
        });

        inputCouponMoneyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sendCouponBtn.setEnabled(false);
                if (!inputValid(inputCouponMoneyEt)) {
                    couponMoney = 0;
                } else {
                    if (inputValid(inputCouponNumEt)) {
                        sendCouponBtn.setEnabled(true);
                        couponMoney = Integer.valueOf(s.toString());
                        if (normalRb.isChecked()) {
                            couponMoney = couponCount * Integer.valueOf(s.toString());
                        }
                    }
                }

                sendCouponBtn.setText("立即支付" + HhsUtils.formatMoney(couponMoney) + "礼金");
            }
        });
        messageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (sendCouponConfig != null && !TextUtils.isEmpty(s) && s.length() > sendCouponConfig.getTitleLength()) {
                    messageEt.setText(s.subSequence(0, sendCouponConfig.getTitleLength()));
                    ToastUtil.showLong(mContext.getApplicationContext(), "标题不超过" + sendCouponConfig.getTitleLength() + "个字");
                }
            }
        });
    }

    private boolean validate() {
        if (couponCount == 0) {
            ToastUtil.showLong(mContext.getApplicationContext(), "红包个数为正整数");
            return false;
        }

        if (timingSendRb.isChecked()) {
            if (TimeUtils.isAfterNow(sendCouponDate, Calendar.getInstance().getTime())) {
                ToastUtil.showLong(mContext.getApplicationContext(), "发红包时间已过,请重新设置");
                return false;
            }
        }

        if (couponCount > Integer.valueOf(sendCouponConfig.getCouponTotalCount())) {
            ToastUtil.showLong(mContext.getApplicationContext(), "红包个数不能超过" + sendCouponConfig.getCouponTotalCount() + "个");
            return false;
        }

        if (luckyRb.isChecked()) {
            if (couponMoney > Integer.valueOf(sendCouponConfig.getCouponMaxTotalAmount())) {
                ToastUtil.showLong(mContext.getApplicationContext(), "红包金额不超过" + HhsUtils.formatMoney(Long.valueOf(sendCouponConfig.getCouponMaxTotalAmount())));
                return false;
            }
            if (couponMoney < Integer.valueOf(sendCouponConfig.getCouponMinTotalAmount())) {
                ToastUtil.showLong(mContext.getApplicationContext(), "红包金额不少于" + sendCouponConfig.getCouponMinTotalAmount() + "礼金");
                return false;
            }
            if (couponMoney / couponCount <
                    Integer.valueOf(sendCouponConfig.getCouponAverageMinAmount())) {
                ToastUtil.showLong(mContext.getApplicationContext(), "单个红包金额不能少于" + sendCouponConfig.getCouponAverageMinAmount() + "礼金");
                return false;
            }
        } else {
            if (couponMoney > Integer.valueOf(sendCouponConfig.getNormalGroupMaxAmount())) {
                ToastUtil.showLong(mContext.getApplicationContext(), "单个红包金额不超过" + HhsUtils.formatMoney(Long.valueOf(sendCouponConfig.getNormalGroupMaxAmount())));
                return false;
            }
            if (couponMoney < Integer.valueOf(sendCouponConfig.getNormalGroupMinAmount())) {
                ToastUtil.showLong(mContext.getApplicationContext(), "单个红包金额不少于" + sendCouponConfig.getNormalGroupMinAmount() + "礼金");
                return false;
            }
        }

        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage message) {
        super.onMessageEvent(message);
        if (message.isInitPasswordOk()) {
            accountPasswordSet = true;
        }
    }

    private boolean inputValid(EditText et) {
        return !TextUtils.isEmpty(et.getText())/* &&
                0 != Integer.valueOf(et.getText().toString())*/;
    }

    private SendCouponTimePop.SendTimeSelectListener sendCouponTimeListener = new SendCouponTimePop.SendTimeSelectListener() {
        @Override
        public void sendTimeSelect(int position, String time) {
            if (time != null) timingTv.setText(time);
            sendCouponDate = timeList.get(position);
        }
    };

    /**
     * 切换的时候改变tab的高度
     */
    private void setRbHeight() {

        for (int i = 0; i < tabRadioGroup.getChildCount(); i++) {
            RadioButton rb = (RadioButton) tabRadioGroup.getChildAt(i);
            RadioGroup.LayoutParams params = (RadioGroup.LayoutParams) rb.getLayoutParams();
            if (rb.isChecked()) {
                params.height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, mContext.getResources().getDisplayMetrics()));
                params.gravity = Gravity.NO_GRAVITY;
            } else {
                params.height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38, mContext.getResources().getDisplayMetrics()));
                params.gravity = Gravity.BOTTOM;
            }
            rb.setLayoutParams(params);
        }

    }

    private View.OnFocusChangeListener focusChangedListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (!hasFocus) return;
            couponNumLayout.setBackgroundResource(R.drawable.shape_unselected_edit_bg);
            couponMoneyLayout.setBackgroundResource(R.drawable.shape_unselected_edit_bg);
            messageLayout.setBackgroundResource(R.drawable.shape_unselected_edit_bg);
            couponMoneyTv.setTextColor(mContext.getResources().getColor(R.color.colorTextG4));
            couponNumTv.setTextColor(mContext.getResources().getColor(R.color.colorTextG4));
            if (view.getId() == R.id.messageEt) {
                messageLayout.setBackgroundResource(R.drawable.shape_selected_edit_bg);
            } else if (view.getId() == R.id.inputCouponMoneyEt) {
                couponMoneyLayout.setBackgroundResource(R.drawable.shape_selected_edit_bg);
                couponMoneyTv.setTextColor(mContext.getResources().getColor(R.color.main_red));
            } else if (view.getId() == R.id.inputCouponNumEt) {
                couponNumLayout.setBackgroundResource(R.drawable.shape_selected_edit_bg);
                couponNumTv.setTextColor(mContext.getResources().getColor(R.color.main_red));
            }

        }
    };

    /**
     * 时间选择
     */
    private RadioGroup.OnCheckedChangeListener sendTimeRgCheckListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            if (checkedId == R.id.nowSendRb) {

            } else if (checkedId == R.id.timingSendRb) {

            }

        }
    };

    /**
     * tab的切换
     */
    private RadioGroup.OnCheckedChangeListener tabRgCheckListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
//            initializeData();
            setRbHeight();
            if (checkedId == R.id.luckyRb) {
                couponMoneyTv.setText("总金额");
                inputCouponMoneyEt.setText(String.valueOf(couponMoney == 0 ? "" : couponMoney));
                if (sendCouponConfig != null)
                    inputCouponMoneyEt.setHint(String.format(mContext.getString(R.string.coupon_min_money_reminder2), sendCouponConfig.getCouponMinTotalAmount()));
            } else if (checkedId == R.id.normalRb) {
                couponMoneyTv.setText("单个金额");
                inputCouponMoneyEt.setText(couponCount == 0 ? "" : (couponMoney / couponCount == 0 ? "" : couponMoney / couponCount + ""));
                if (sendCouponConfig != null)
                    inputCouponMoneyEt.setHint(String.format(mContext.getString(R.string.coupon_min_money_reminder2), sendCouponConfig.getNormalGroupMinAmount()));
            }
        }
    };

    @Override
    protected void onDestroy() {
        httpRequestHelper.onDestroy();
        super.onDestroy();
    }

    @SuppressLint("WrongConstant")
    private void responseSuccessCouponConfig(JSONObject jsonObject) throws JSONException {
        /*if (jsonObject.has("accountPasswordSet")) {
            accountPasswordSet = jsonObject.getBoolean("accountPasswordSet");
        }*/
        if (jsonObject.has("couponConfig")) {
            statusLayout.hideStatusView();
            contentLayout.setVisibility(View.VISIBLE);
            JSONObject couponConfig = jsonObject.getJSONObject("couponConfig");
            sendCouponConfig = new Gson().fromJson(couponConfig.toString(), SendCouponConfig.class);
            timeList = TimeUtils.getRecentlyIntegralPointTime(Integer.valueOf(sendCouponConfig.getSendTimeLimit()));
            sendCouponDate = timeList.get(0);
            timingTv.setText(TimeUtils.getFormatTime(sendCouponDate, "HH时mm分"));
            expireTv.setText(String.format(mContext.getResources().getString(R.string.coupon_expire_desc), String.valueOf((int) sendCouponConfig.getExpiredHours())));
            inputCouponMoneyEt.setHint(String.format(mContext.getString(R.string.coupon_min_money_reminder2), sendCouponConfig.getCouponMinTotalAmount()));
        }
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            switch (((CouponUrlEnum) requestContainer.getRequestEnum())) {
                case GET_COUPON_CONFIG:
                    responseSuccessCouponConfig(jsonObject);
                    break;
                case COUPON_CREATE:
                    responseSuccessCouponCreate(jsonObject);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessCouponCreate(JSONObject jsonObject) throws JSONException {
        if (bottomPayPop != null) bottomPayPop.setPayResult(true);
        dismiss();
        String groupCouponId = null;
        if (jsonObject.has("groupCouponId")) {
            groupCouponId = jsonObject.getString("groupCouponId");
        }
        if (sendCouponSuccessListener != null) {
            sendCouponSuccessListener.sendCouponSuccess(normalRb.isChecked() ? "AVERAGE" : "RANDOM", groupCouponId, title);
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            if (CouponUrlEnum.COUPON_CREATE == ((CouponUrlEnum) requestContainer.getRequestEnum())) {
                ErrorBean errorBean = new ErrorBean(jsonObject);
                String name = errorBean.getError().getName();

                if (bottomPayPop != null) bottomPayPop.setPayResult(false);

                if ("AVAIABLE_AMOUNT_NOT_ENOUGH".equals(name)) {
                    ToastUtil.showLong(mContext.getApplicationContext(), "您的礼金不够");
                    return;
                }
            }
            ((BaseActivity) mContext).operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {
        if (GET_COUPON_CONFIG == ((CouponUrlEnum) requestContainer.getRequestEnum())) {
            statusLayout.showRetry();
        } else if (CouponUrlEnum.COUPON_CREATE == requestContainer.getRequestEnum()) {
            if (bottomPayPop != null) bottomPayPop.setPayResult(false);
        }
    }

    @Override
    public void executorFinish() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.timingTv) {
            selectTimeSendCoupon();
        } else if (id == R.id.exchangeGoldTv) {
            exchangeGold();
        } else if (id == R.id.sendCouponBtn) {
            //sendCoupon();
            createCoupon(null);
        } else if (id == R.id.closeIv) {
            close();
        }
    }
}
