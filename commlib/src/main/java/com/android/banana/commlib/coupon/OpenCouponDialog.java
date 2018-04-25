package com.android.banana.commlib.coupon;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.banana.commlib.bean.GroupCouponInfoBean;
import com.android.banana.commlib.coupon.couponenum.CouponEnum;
import com.android.banana.commlib.coupon.couponenum.CouponUrlEnum;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lingjiu on 2017/7/25.
 */

public class OpenCouponDialog extends CouponDialogBase implements OnHttpResponseListener {


    ImageView dialogCloseIv;
    CircleImageView portraitIv;
    TextView userNameTv;
    TextView couponTitleTv;
    ImageView openCouponBtn;
    FrameLayout obtainFailedDialogLL;
    ImageView couponTypeIv;

    private HttpRequestHelper httpRequestHelper;
    private GroupCouponInfoBean couponInfoBean;
    private Context mContext;

    public OpenCouponDialog(Context context, GroupCouponInfoBean couponInfo) {
        super(context, R.layout.dialog_open_coupon);

        adjustDialogSize(context);

        this.couponInfoBean = couponInfo;

        this.mContext = context;

        initView(context);

        dialogCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        openCouponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fetchCoupon();
            }
        });
    }

    private void initView(Context context) {

        dialogCloseIv = findViewOfId(R.id.dialogCloseIv);
        portraitIv = findViewOfId(R.id.portraitIv);
        userNameTv = findViewOfId(R.id.userNameTv);
        couponTitleTv = findViewOfId(R.id.couponTitleTv);
        openCouponBtn = findViewOfId(R.id.openCouponBtn);
        obtainFailedDialogLL = findViewOfId(R.id.obtainFailedDialogLL);
        couponTypeIv = findViewOfId(R.id.couponTypeIv);


        httpRequestHelper = new HttpRequestHelper(context, this);

        if (CouponEnum.NORMAL_GROUP_COUPON.getMessage().equals(couponInfoBean.getAmountAllocateType().getName())) {
            couponTypeIv.setImageResource(R.drawable.icon_normal_coupon);
        } else {
            couponTypeIv.setImageResource(R.drawable.icon_lucky_coupon);
        }
        setImageShow(portraitIv, couponInfoBean.getLogoUrl());
        userNameTv.setText(couponInfoBean.getLoginName());
        couponTitleTv.setText(couponInfoBean.getTitle());
    }

    private void fetchCoupon() {
        XjqRequestContainer map = new XjqRequestContainer(CouponUrlEnum.USER_FETCH_COUPON, true);
        map.put("groupCouponId", couponInfoBean.getCouponNo());
        httpRequestHelper.startRequest(map, false);
    }


    private void handleFetchCouponSuccess(JSONObject jo) {

        try {
            int fetchedAmount = jo.getInt("fetchedAmount");

//            String groupCouponId = jo.getString("groupCouponId");

            new FetchCouponResultDialog(mContext, fetchedAmount, couponInfoBean.getCouponNo()).show();

            //刷新红包数据
            couponInfoBean.setIsOwnAllocated(true);
            EventBus.getDefault().post(couponInfoBean);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        dismiss();
        handleFetchCouponSuccess(jsonObject);
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            dismiss();

            FetCouponValidateUtils fetCouponValidateUtils = new FetCouponValidateUtils((Activity) mContext, couponInfoBean);

            fetCouponValidateUtils.handleFalse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {
    }

    @Override
    public void executorFinish() {
    }

    private void adjustDialogSize(Context ctx) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        int height = LibAppUtil.getScreenHeight(ctx) > LibAppUtil.getScreenWidth(ctx) ? LibAppUtil.getScreenWidth(ctx) : LibAppUtil.getScreenHeight(ctx);
        lp.height = height;
        lp.width = (height - LibAppUtil.dip2px(ctx, 20)) * 838 / 1118;//宽高可设置具体大小
        getWindow().setAttributes(lp);
    }

}
