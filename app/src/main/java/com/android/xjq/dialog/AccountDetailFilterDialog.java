package com.android.xjq.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.dialog.DialogBase;
import com.android.library.Utils.LibAppUtil;
import com.android.xjq.R;
import com.android.xjq.activity.accountdeatils.AccountDetailsActivity;


/**
 * Created by zaozao on 2017/11/2.
 */

public class AccountDetailFilterDialog extends DialogBase {
    TextView inTv,allTv,outTv;
    LinearLayout inLayout,allLayout,outLayout;
    ImageView inIv,outIv,allIv;
    public AccountDetailFilterDialog(Context context,
                                     final AccountDetailsActivity.SelectFilterOnClickListener onClickListener
            ,String selectAccountType) {
        super(context, R.layout.dialog_account_details_filter,R.style.MyDialog);
        dialog.setCancelable(true);
        Window dialogWindow = dialog.getWindow();

        dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = LibAppUtil.dip2px(context,45);
        //设置显示位置
        dialog.onWindowAttributesChanged(lp);
        inTv = (TextView)window.findViewById(R.id.inTv);
        outTv = (TextView) window.findViewById(R.id.outTv);
        allTv = (TextView) window.findViewById(R.id.allTv);
        inLayout = (LinearLayout) window.findViewById(R.id.inLayout);
        allLayout = (LinearLayout) window.findViewById(R.id.allLayout);
        outLayout = (LinearLayout) window.findViewById(R.id.outLayout);
        inIv = (ImageView) window.findViewById(R.id.inIv);
        outIv = (ImageView) window.findViewById(R.id.outIv);
        allIv = (ImageView) window.findViewById(R.id.allIv);
        if("ALL".equals(selectAccountType)){
            allTv.setTextColor(context.getResources().getColor(R.color.main_red));
            allIv.setImageResource(R.drawable.icon_all_selected);
        }else{

        }
        if("I".equals(selectAccountType)){
            inTv.setTextColor(context.getResources().getColor(R.color.main_red));
            inIv.setImageResource(R.drawable.icon_in_selected);
        }else{

        }



        if ("O".equals(selectAccountType)){
            outTv.setTextColor(context.getResources().getColor(R.color.main_red));
            outIv.setImageResource(R.drawable.icon_out_selected);

        }else{

        }

        inLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.select("I");
                dialog.dismiss();
            }
        });
        outLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.select("O");
                dialog.dismiss();
            }
        });
        allLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.select("ALL");
                dialog.dismiss();
            }
        });
    }
}
