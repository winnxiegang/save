package com.android.xjq.dialog;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.banana.commlib.dialog.DialogBase;
import com.android.xjq.utils.live.SpannableStringHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lingjiu on 2016/11/11 10:26.
 */
public class RechargeResultShowDialog extends DialogBase {


    @BindView(R.id.descTv)
    TextView descTv;
    @BindView(R.id.confirmLayout)
    LinearLayout confirmLayout;
    @BindView(R.id.mainLayout)
    FrameLayout mainLayout;
    private Context context;

    @OnClick(R.id.confirmLayout)
    public void confirm() {
        dialog.dismiss();
    }


    public RechargeResultShowDialog(Context context, double rechargePoint) {

        super(context, R.layout.dialog_recharge_result, R.style.MyDialog);

        this.context = context;

        ButterKnife.bind(this, window);

        if (rechargePoint > 0) {
            //充值成功
            mainLayout.setBackgroundResource(R.drawable.icon_recharge_success);
            SpannableStringBuilder ssb = new SpannableStringBuilder("您已成功充值");
            ssb.append(SpannableStringHelper.changeTextColor(String.valueOf(rechargePoint), context.getResources().getColor(R.color.main_red1)));
            ssb.append("香蕉币");
            descTv.setText(ssb);
        } else {
            //充值失败
            mainLayout.setBackgroundResource(R.drawable.icon_recharge_fail);
            descTv.setText("充值异常,请稍后查看自己的钱包");
        }

    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

}
