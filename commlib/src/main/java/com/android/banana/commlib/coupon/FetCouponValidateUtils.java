package com.android.banana.commlib.coupon;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.bean.GroupCouponInfoBean;
import com.android.banana.commlib.coupon.couponenum.GroupCouponAllocateStatusEnum;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowSimpleMessageDialog;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;


/**
 * Created by lingjiu on 2016/11/10 18:27.
 */
public class FetCouponValidateUtils {
    private static final String TAG = "FetCouponValidateUtils";

    private WeakReference<Activity> mReference;
    private GroupCouponInfoBean couponInfoBean;

    public FetCouponValidateUtils(Activity activity, GroupCouponInfoBean couponInfoBean) {

        mReference = new WeakReference<>(activity);

        this.couponInfoBean = couponInfoBean;
    }

    public void handleFalse(JSONObject jo) throws JSONException {

        if (jo.has("error")) {
            handleError(jo, jo.getJSONObject("error").getString("name"));
        } else {
            operateErrorResponseMessage(jo, true);
        }
    }


    private void handleError(JSONObject jo, String errorName) throws JSONException {
        if (errorName != null) {
            switch (errorName) {
                case "COUPON_EXPIRED":
                    couponInfoBean.getAmountAllocateType().setName(GroupCouponAllocateStatusEnum.UNALLOCATABLE.name());
                    EventBus.getDefault().post(couponInfoBean);
                case "COUPON_REMAIN_NUM_EMPTY":
                    if (mReference != null && mReference.get() != null) {
                        new FetchCouponResultDialog(mReference.get(), errorName, couponInfoBean.getCouponNo()).show();
                        couponInfoBean.getAmountAllocateType().setName(GroupCouponAllocateStatusEnum.ALL_ALLOCATED.name());
                        EventBus.getDefault().post(couponInfoBean);
                    }

                    break;
                case "COUPON_ITEM_ROBBED":
                    if (mReference != null && mReference.get() != null) {
                        couponInfoBean.setIsOwnAllocated(true);
                        EventBus.getDefault().post(couponInfoBean);
                        new CouponDetailDialog(mReference.get(), couponInfoBean.getCouponNo()).show();
                    }

                    break;

                case "TIMED_COUPON_CAN_NOT_FETCH":
                    if (mReference != null && mReference.get() != null) {
                        ToastUtil.showLong(mReference.get().getApplicationContext(),"再等一下咩~~");
                    }
                    break;
                default:
                    operateErrorResponseMessage(jo, true);
                    break;
            }
        }
    }


    public void operateErrorResponseMessage(JSONObject jo, boolean showTip) throws JSONException {
        if (jo == null)
            return;
        ErrorBean bean = new ErrorBean(jo);
        if (bean.getError() == null)
            return;
        String name = bean.getError().getName();
        String tip = bean.getError().getMessage();

        if ("USER_LOGIN_EXPIRED".equals(name)) {
            try {
                if (mReference != null && mReference.get() != null) {
                    ShowSimpleMessageDialog dialog1 = new ShowSimpleMessageDialog(mReference.get(), "该账号已在另一处登录,需要重新登录!", toLoginMainActivityListener);
                }
            } catch (Exception e) {
                if (e instanceof WindowManager.BadTokenException) {
                    LogUtils.e(TAG, e.getMessage());
                }
            }
        } else if ("POST_FORBIDDEN".equals(name)) {
            //禁言
            ShowGagDialog(jo);
        } else if ("USER_ID_REQUIRED".equals(name)) {
            if (mReference != null && mReference.get() != null) {
                LibAppUtil.showTip(mReference.get().getApplicationContext(), tip, Toast.LENGTH_SHORT, -1);
            }
        } else if ("USER_LOGIN_FORBID".equals(name)) {
            //在竞彩之家内账号禁止登录
            showForbidDialog();
        } else if ("ROLE_LOGIN_FORBID".equals(name)) {
            //在竞彩之家内角色禁止登录
            showForbidDialog();
        } else if ("GROUP_HAS_DISMISSED".equals(name)
                || TextUtils.equals("USER_HAD_REMOVE_CHAT_ROOM", name)) {
            //聊天室已解散，被移除聊天室
            if (mReference != null && mReference.get() != null) {
                new ShowSimpleMessageDialog(mReference.get(), tip, new OnMyClickListener() {
                    @Override
                    public void onClick(View v) {
                        mReference.get().finish();
                    }
                });
            }

        } else {
            if (showTip && mReference != null && mReference.get() != null) {
                LibAppUtil.showTip(mReference.get().getApplicationContext(), tip, Toast.LENGTH_SHORT, -1);
            }
        }
    }


    private void showForbidDialog() {

        ShowSimpleMessageDialog showSimpleMessageDialog = new ShowSimpleMessageDialog(mReference.get(), toLoginMainActivityListener, Gravity.CENTER);
    }

    private OnMyClickListener toLoginMainActivityListener = new OnMyClickListener() {
        @Override
        public void onClick(View v) {
            if (mReference != null && mReference.get() != null) {
                mReference.get().startActivity(new Intent("com.android.xjq.login"));
            }
        }
    };

    private void ShowGagDialog(JSONObject jo) {
        try {
            String gmtExpired = null;
            String forbiddenReason = null;
            if (jo.has("gmtExpired")) {
                gmtExpired = jo.getString("gmtExpired");
            }
            if (jo.has("forbiddenReason")) {
                forbiddenReason = jo.getString("forbiddenReason");
            }
            if (mReference != null && mReference.get() != null) {
                ShowSimpleMessageDialog simpleMessageDialog = new ShowSimpleMessageDialog(mReference.get(), "您已被禁言" + "\n" + "禁言原因:" + forbiddenReason + "\n" + "解封时间:" + gmtExpired);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
