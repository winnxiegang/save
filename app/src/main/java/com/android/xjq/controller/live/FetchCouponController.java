package com.android.xjq.controller.live;

import android.content.Context;
import android.view.View;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.activity.login.LoginActivity;
import com.android.xjq.view.CouponFrameLayout;

import org.json.JSONObject;

/**
 * 抢红包入口控制器
 * Created by lingjiu on 2016/11/14 11:58.
 */
public class FetchCouponController {

    private LiveActivity mContext;

    private CouponFrameLayout mFloatingLayout;

    private int canFetchCouponNum;

    private int totalCouponNum;

    //当前只有一个红包时,红包id
    private String groupCouponId = "";
    private boolean isLandscape;

    public FetchCouponController(Context context, CouponFrameLayout floatingLayout) {

        this.mContext = (LiveActivity) context;

        this.mFloatingLayout = floatingLayout;
    }

    /*public void updateData(FetCouponInfoBean.GroupCouponInfo event) {

        if (canFetchCouponNum > 0) {

            canFetchCouponNum -= 1;

            //重新排序
            showViewByNum();
        }
    }*/


    public void setCouponView(JSONObject jo) {
        try {
            if (jo.has("num")) {

                canFetchCouponNum = jo.getInt("num");
            }
            if (jo.has("totalNum")) {
                totalCouponNum = jo.getInt("totalNum");
            }
            if (jo.has("groupCouponId")) {
                groupCouponId = jo.getString("groupCouponId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* totalCouponNum = 3;
        canFetchCouponNum = 3;*/

        showViewByNum();

        mFloatingLayout.setFloatingLayoutClickListener(new CouponFrameLayout.FloatingLayoutClickListener() {
            @Override
            public void onClickListener() {
                if (LoginInfoHelper.getInstance().getUserId() == null) {
                    LoginActivity.startLoginActivity(mContext, false);
                    return;
                }
                if (totalCouponNum > 1) {
                    mContext.showGroupCoupon();
                } else if (totalCouponNum == 1) {
                    if (canFetchCouponNum == 0) {
                        mContext.showGroupCoupon();
                    } else {
                        mContext.getHttpController().queryAvailableCoupon(groupCouponId);
                    }
                } else {
                    mContext.showGroupCoupon();
                }

            }
        });
    }

    private void showViewByNum() {

        if (canFetchCouponNum == 0) {

            mFloatingLayout.setShakeAnim(false);
        } else {

            mFloatingLayout.setShakeAnim(true);
        }

        //根据红包总数来显示红包
        if (totalCouponNum > 0) {
            mFloatingLayout.setCouponCount(canFetchCouponNum);
            if (isLandscape && canFetchCouponNum == 0) {
                mFloatingLayout.hideAnimator();
            } else {
                mFloatingLayout.sendMessage();
            }
        } else {
            mFloatingLayout.hideAnimator();
        }
    }

    public void onDestroy() {
        mContext = null;
    }

    public void changeOrientation(boolean isLandscape) {
        this.isLandscape = isLandscape;

        if (isLandscape) {
            if (canFetchCouponNum == 0) {
                mFloatingLayout.hideAnimator();
            }
        } else if (CouponFrameLayout.LayoutState.HIDE == mFloatingLayout.getCurrentState() && totalCouponNum > 0) {
            mFloatingLayout.showAnimator();
        }
    }

    public void isHideCoupon(boolean isHide) {
        if (CouponFrameLayout.LayoutState.SHOW == mFloatingLayout.getCurrentState()) {
            mFloatingLayout.setCouponViewVisibility(isHide ? View.GONE : View.VISIBLE);
        }
    }
}
