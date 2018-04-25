package com.android.banana.commlib.coupon;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.banana.commlib.bean.SendCouponConfig;
import com.android.banana.commlib.coupon.couponenum.CouponEnum;
import com.android.banana.commlib.coupon.couponenum.CouponPublishType;
import com.android.banana.commlib.dialog.BottomPayPop;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.utils.HhsUtils;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.expandtv.PayPsdInputView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lingjiu on 2018/2/6.
 */

public class SendCouponController extends BaseController4JCZJ<BaseActivity> implements View.OnClickListener {
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
    TextView obtainGiftGoldTv;
    Button sendCouponBtn;
    TextView expireTv;
    FrameLayout contentLayout;
    LiveCouponCallback couponCallback;
    private boolean accountPasswordSet;
    //红包配置
    private SendCouponConfig sendCouponConfig;
    private ArrayList<Date> timeList;
    //当前选择的发红包时间
    private Date sendCouponDate;
    //当前红包数量
    private int couponCount;
    //当前红包金额
    private int couponMoney;
    private BottomPayPop bottomPayPop;

    public SendCouponController(Activity activity, LiveCouponCallback couponCallback) {
        super(activity);
        this.couponCallback = couponCallback;
    }

    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.layout_send_coupon_view);
    }

    @Override
    public void onSetUpView() {
        normalRb = (RadioButton) contentView.findViewById(R.id.normalRb);
        luckyRb = (RadioButton) contentView.findViewById(R.id.luckyRb);
        tabRadioGroup = (RadioGroup) contentView.findViewById(R.id.tabRadioGroup);
        couponNumTv = (TextView) contentView.findViewById(R.id.couponNumTv);
        inputCouponNumEt = (EditText) contentView.findViewById(R.id.inputCouponNumEt);
        exchangeGoldTv = (TextView) contentView.findViewById(R.id.exchangeGoldTv);
        couponMoneyLayout = (LinearLayout) contentView.findViewById(R.id.couponMoneyLayout);
        messageEt = (EditText) contentView.findViewById(R.id.messageEt);
        messageLayout = (LinearLayout) contentView.findViewById(R.id.messageLayout);
        sendTimeRg = (RadioGroup) contentView.findViewById(R.id.sendTimeRg);
        sendCouponBtn = (Button) contentView.findViewById(R.id.sendCouponBtn);
        expireTv = (TextView) contentView.findViewById(R.id.expireTv);
        contentLayout = (FrameLayout) contentView.findViewById(R.id.sendCouponLayout);
        timingTv = (TextView) contentView.findViewById(R.id.timingTv);
        couponMoneyTv = (TextView) contentView.findViewById(R.id.couponMoneyTv);
        inputCouponMoneyEt = (EditText) contentView.findViewById(R.id.inputCouponMoneyEt);
        nowSendRb = (RadioButton) contentView.findViewById(R.id.nowSendRb);
        timingSendRb = (RadioButton) contentView.findViewById(R.id.timingSendRb);
        couponNumLayout = ((LinearLayout) contentView.findViewById(R.id.couponNumLayout));
        obtainGiftGoldTv = (TextView) contentView.findViewById(R.id.obtainGiftGoldTv);


        sendCouponBtn.setOnClickListener(this);
        //exchangeGoldTv.setOnClickListener(this);
        timingTv.setOnClickListener(this);
        obtainGiftGoldTv.setOnClickListener(this);

        tabRadioGroup.setOnCheckedChangeListener(tabRgCheckListener);
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
                    ToastUtil.showLong(context.getApplicationContext(), "标题不超过" + sendCouponConfig.getTitleLength() + "个字");
                }
            }
        });
    }

    private void createCoupon(String psd) {
        if (sendCouponConfig == null) return;

        if (!validate()) return;

        String amountAllocateType = normalRb.isChecked() ? "AVERAGE" : "RANDOM";
        String couponType = normalRb.isChecked() ? CouponEnum.NORMAL_GROUP_COUPON.name() : CouponEnum.LUCKY_GROUP_COUPON.name();
        String couponPublishType = timingSendRb.isChecked() ? CouponPublishType.TIMED.name() : CouponPublishType.IMMEDIATE.name();
        String gmtPublish = TimeUtils.getFormatTime(sendCouponDate, TimeUtils.LONG_DATEFORMAT);
        String title = TextUtils.isEmpty(messageEt.getText()) ? messageEt.getHint().toString() : messageEt.getText().toString();
        String totalAmount = normalRb.isChecked() ? "" + Integer.valueOf(inputCouponMoneyEt.getText().toString()) * Integer.valueOf(inputCouponNumEt.getText().toString()) :
                inputCouponMoneyEt.getText().toString();
        String totalCount = inputCouponNumEt.getText().toString();

        couponCallback.createCoupon(psd, amountAllocateType, couponType, couponPublishType, gmtPublish, title, totalAmount, totalCount);

    }

    private void inputPassword() {
        if (bottomPayPop == null) {
            bottomPayPop = new BottomPayPop(context, new PayPsdInputView.OnPasswordListener() {
                @Override
                public void inputListener(boolean isInputComplete, String psd) {
                    createCoupon(psd);
                }
            });
        }
        bottomPayPop.showPop();
    }

    private void goSetPassword() {
        ShowMessageDialog dialog = new ShowMessageDialog(context, R.drawable.icon_dialog_title_warning, "去设置", "取消", new OnMyClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.android.xjq.initsetpassword");
                context.startActivity(intent);
            }
        }, null, context.getString(R.string.account_password_set));
    }


    private boolean validate() {
        if (couponCount == 0) {
            ToastUtil.showLong(context.getApplicationContext(), "红包个数为正整数");
            return false;
        }

        if (timingSendRb.isChecked()) {
            if (TimeUtils.isAfterNow(sendCouponDate, Calendar.getInstance().getTime())) {
                ToastUtil.showLong(context.getApplicationContext(), "发红包时间已过,请重新设置");
                return false;
            }
        }

        if (couponCount > Integer.valueOf(sendCouponConfig.getCouponTotalCount())) {
            ToastUtil.showLong(context.getApplicationContext(), "红包个数不能超过" + sendCouponConfig.getCouponTotalCount() + "个");
            return false;
        }

        if (luckyRb.isChecked()) {
            if (couponMoney > Integer.valueOf(sendCouponConfig.getCouponMaxTotalAmount())) {
                ToastUtil.showLong(context.getApplicationContext(), "红包金额不超过" + HhsUtils.formatMoney(Long.valueOf(sendCouponConfig.getCouponMaxTotalAmount())));
                return false;
            }
            if (couponMoney < Integer.valueOf(sendCouponConfig.getCouponMinTotalAmount())) {
                ToastUtil.showLong(context.getApplicationContext(), "红包金额不少于" + sendCouponConfig.getCouponMinTotalAmount() + "礼金");
                return false;
            }
            if (couponMoney / couponCount <
                    Integer.valueOf(sendCouponConfig.getCouponAverageMinAmount())) {
                ToastUtil.showLong(context.getApplicationContext(), "单个红包金额不能少于" + sendCouponConfig.getCouponAverageMinAmount() + "礼金");
                return false;
            }
        } else {
            if (couponMoney > Integer.valueOf(sendCouponConfig.getNormalGroupMaxAmount())) {
                ToastUtil.showLong(context.getApplicationContext(), "单个红包金额不超过" + HhsUtils.formatMoney(Long.valueOf(sendCouponConfig.getNormalGroupMaxAmount())));
                return false;
            }
            if (couponMoney < Integer.valueOf(sendCouponConfig.getNormalGroupMinAmount())) {
                ToastUtil.showLong(context.getApplicationContext(), "单个红包金额不少于" + sendCouponConfig.getNormalGroupMinAmount() + "礼金");
                return false;
            }
        }

        return true;
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


    public void selectTimeSendCoupon() {
        SendCouponTimePop pop = new SendCouponTimePop(getContext(), timingTv, sendCouponTimeListener);
        pop.showUp(timeList);
    }


    private View.OnFocusChangeListener focusChangedListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (!hasFocus) return;
            couponNumLayout.setBackgroundResource(R.drawable.shape_unselected_edit_bg);
            couponMoneyLayout.setBackgroundResource(R.drawable.shape_unselected_edit_bg);
            messageLayout.setBackgroundResource(R.drawable.shape_unselected_edit_bg);
            couponMoneyTv.setTextColor(context.getResources().getColor(R.color.colorTextG4));
            couponNumTv.setTextColor(context.getResources().getColor(R.color.colorTextG4));
            if (view.getId() == R.id.messageEt) {
                messageLayout.setBackgroundResource(R.drawable.shape_selected_edit_bg);
            } else if (view.getId() == R.id.inputCouponMoneyEt) {
                couponMoneyLayout.setBackgroundResource(R.drawable.shape_selected_edit_bg);
                couponMoneyTv.setTextColor(context.getResources().getColor(R.color.main_red));
            } else if (view.getId() == R.id.inputCouponNumEt) {
                couponNumLayout.setBackgroundResource(R.drawable.shape_selected_edit_bg);
                couponNumTv.setTextColor(context.getResources().getColor(R.color.main_red));
            }

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
                params.height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, context.getResources().getDisplayMetrics()));
                params.gravity = Gravity.NO_GRAVITY;
            } else {
                params.height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38, context.getResources().getDisplayMetrics()));
                params.gravity = Gravity.BOTTOM;
            }
            rb.setLayoutParams(params);
        }

    }


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
                    inputCouponMoneyEt.setHint(String.format(context.getString(R.string.coupon_min_money_reminder2), sendCouponConfig.getCouponMinTotalAmount()));
            } else if (checkedId == R.id.normalRb) {
                couponMoneyTv.setText("单个金额");
                inputCouponMoneyEt.setText(couponCount == 0 ? "" : (couponMoney / couponCount == 0 ? "" : couponMoney / couponCount + ""));
                if (sendCouponConfig != null)
                    inputCouponMoneyEt.setHint(String.format(context.getString(R.string.coupon_min_money_reminder2), sendCouponConfig.getNormalGroupMinAmount()));
            }
        }
    };

    public void sendCoupon() {
       /* // 没设置支付密码,要先设置支付密码
        if (!accountPasswordSet) {
            goSetPassword();
        } else {
            inputPassword();
        }*/
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.timingTv) {
            selectTimeSendCoupon();
        } else if (id == R.id.exchangeGoldTv) {
            //exchangeGold();
        } else if (id == R.id.sendCouponBtn) {
            createCoupon(null);
        } else if (id == R.id.obtainGiftGoldTv) {
            Intent intent = new Intent("com.android.xjq.betting_game");
            context.startActivity(intent);
        }
    }

    public void setConfigData(JSONObject jsonObject) throws JSONException {
        /*if (jsonObject.has("accountPasswordSet")) {
            accountPasswordSet = jsonObject.getBoolean("accountPasswordSet");
        }*/
        if (jsonObject.has("couponConfig")) {
            contentLayout.setVisibility(View.VISIBLE);
            JSONObject couponConfig = jsonObject.getJSONObject("couponConfig");
            sendCouponConfig = new Gson().fromJson(couponConfig.toString(), SendCouponConfig.class);
            timeList = TimeUtils.getRecentlyIntegralPointTime(Integer.valueOf(sendCouponConfig.getSendTimeLimit()));
            sendCouponDate = timeList.get(0);
            timingTv.setText(TimeUtils.getFormatTime(sendCouponDate, "HH时mm分"));
            expireTv.setText(String.format(context.getResources().getString(R.string.coupon_expire_desc), String.valueOf((int) sendCouponConfig.getExpiredHours())));
            inputCouponMoneyEt.setHint(String.format(context.getString(R.string.coupon_min_money_reminder2), sendCouponConfig.getCouponMinTotalAmount()));
        }

    }

    public void setCreateCouponResult(boolean result) {
        // bottomPayPop.setPayResult(result);
    }

    public void setPasswordSuccess() {
        accountPasswordSet = true;
    }
}
