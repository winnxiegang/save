package com.android.banana.commlib.coupon;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.banana.commlib.R;
import com.android.library.Utils.LibAppUtil;

/**
 * Created by lingjiu  on 2017/7/25.
 */
public class FetchCouponResultDialog extends CouponDialogBase {


    ImageView dialogCloseIv;

    TextView couponMoneyTv;

    LinearLayout successContent;

    ImageView fetchCouponFailIv;

    TextView fetchCouponFailTv;

    RelativeLayout failContentLayout;

    Button confirmBtn;

    TextView lookCouponDetailTv;

    LinearLayout contentLayout;

    private ImageView priceTypeImg;

    private String groupCouponId;
    private Context mCtx;

    private void initView() {
        dialogCloseIv = findViewOfId(R.id.dialogCloseIv);
        couponMoneyTv = findViewOfId(R.id.couponMoneyTv);
        successContent = findViewOfId(R.id.successContent);
        fetchCouponFailIv = findViewOfId(R.id.fetchCouponFailIv);
        fetchCouponFailTv = findViewOfId(R.id.fetchCouponFailTv);
        failContentLayout = findViewOfId(R.id.failContentLayout);
        confirmBtn = findViewOfId(R.id.confirmBtn);
        lookCouponDetailTv = findViewOfId(R.id.lookCouponDetailTv);
        contentLayout = findViewOfId(R.id.contentLayout);
        priceTypeImg = findViewOfId(R.id.price_type_img);

        dialogCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    /**
     * 抢红包成功
     */
    public FetchCouponResultDialog(final Context context, final int amount, final String groupCouponId) {
        super(context, R.layout.dialog_get_coupon_failed);
        mCtx = context;
        adjustDialogSize(context);
        this.groupCouponId = groupCouponId;
        initView();
        contentLayout.setBackgroundResource(R.drawable.icon_fetch_coupon_success_bg);
        couponMoneyTv.setText("" + amount);
        failContentLayout.setVisibility(View.GONE);
        successContent.setVisibility(View.VISIBLE);

        lookCouponDetailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                new CouponDetailDialog(context, groupCouponId, amount).show();
            }
        });
    }

    public FetchCouponResultDialog(final Context context, final double totalPrice, final String moneyType) {
        super(context, R.layout.dialog_get_coupon_failed);
        mCtx = context;
        adjustDialogSize(context);
        initView();
        setPriceImg(moneyType);
        contentLayout.setBackgroundResource(R.drawable.icon_fetch_coupon_success_bg);
        String contentStr = "" + (int) totalPrice;
        couponMoneyTv.setText(contentStr);
        failContentLayout.setVisibility(View.GONE);
        successContent.setVisibility(View.VISIBLE);
        lookCouponDetailTv.setVisibility(View.GONE);
    }

    private void setPriceImg(String moneyType) {
        int redDrawableId = R.drawable.ic_package_dollar;
        switch (moneyType) {
            case "GIFTCOIN":
                redDrawableId = R.drawable.ic_package_dollar;
                break;
            case "POINTCOIN":
                redDrawableId = R.drawable.icon_banana_gold_coin;
                break;
            case "GOLDCOIN":
                redDrawableId = R.drawable.icon_gift_gold_coin;
                break;
        }
        priceTypeImg.setImageDrawable(mCtx.getResources().getDrawable(redDrawableId));
    }

    /**
     * 抢红包失败
     */
    public FetchCouponResultDialog(final Context context, String errorMsg, final String groupCouponId) {
        super(context, R.layout.dialog_get_coupon_failed);
        mCtx = context;
        adjustDialogSize(context);
        initView();
        this.groupCouponId = groupCouponId;
        contentLayout.setBackgroundResource(R.drawable.icon_fetch_coupon_fail_bg);
        failContentLayout.setVisibility(View.VISIBLE);
        successContent.setVisibility(View.GONE);
        if ("COUPON_EXPIRED".equals(errorMsg)) {
            fetchCouponFailIv.setImageResource(R.drawable.icon_coupon_expired_style);
            fetchCouponFailTv.setText("您来晚了,\n红包已过期");
        } else if ("COUPON_REMAIN_NUM_EMPTY".equals(errorMsg)) {
            fetchCouponFailIv.setImageResource(R.drawable.icon_coupon_all_empty_style);
            fetchCouponFailTv.setText("您来晚了,\n红包已领完");
        }
        lookCouponDetailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                new CouponDetailDialog(context, groupCouponId).show();
            }
        });
    }

    public FetchCouponResultDialog(final Context context, String errorMsg) {
        super(context, R.layout.dialog_get_coupon_failed);
        mCtx = context;
        adjustDialogSize(context);
        initView();
        contentLayout.setBackgroundResource(R.drawable.icon_fetch_coupon_fail_bg);
        failContentLayout.setVisibility(View.VISIBLE);
        successContent.setVisibility(View.GONE);
        if ("COUPON_EXPIRED".equals(errorMsg)) {
            fetchCouponFailIv.setImageResource(R.drawable.icon_coupon_expired_style);
            fetchCouponFailTv.setText("您来晚了,\n红包已过期");
        } else if ("COUPON_REMAIN_NUM_EMPTY".equals(errorMsg)) {
            fetchCouponFailIv.setImageResource(R.drawable.icon_coupon_all_empty_style);
            fetchCouponFailTv.setText("您来晚了,\n红包已领完");
        } else {
            fetchCouponFailIv.setImageResource(R.drawable.icon_coupon_all_empty_style);
            fetchCouponFailTv.setText("手气不好，抢了个空红包");
        }
        lookCouponDetailTv.setVisibility(View.GONE);
    }

    private void adjustDialogSize(Context ctx) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        int height = LibAppUtil.getScreenHeight(ctx) > LibAppUtil.getScreenWidth(ctx) ? LibAppUtil.getScreenWidth(ctx) : LibAppUtil.getScreenHeight(ctx);
        lp.height = height;
        lp.width = (height - LibAppUtil.dip2px(ctx, 20)) * 887 / 1189;//宽高可设置具体大小
        getWindow().setAttributes(lp);
    }

}
