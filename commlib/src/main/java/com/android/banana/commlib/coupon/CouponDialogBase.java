package com.android.banana.commlib.coupon;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


import com.android.banana.commlib.R;
import com.android.banana.commlib.eventBus.EventBusMessage;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by lingjiu on 2017/8/18.
 */

public abstract class CouponDialogBase extends Dialog {

    protected Context context;

    private View rootView;

    public CouponDialogBase(Context context, int resId) {
        super(context, R.style.MyDialog);

        this.context = context;
        rootView = LayoutInflater.from(context).inflate(resId, null);
        setContentView(rootView);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage message) {
        if (message.isRefreshLiveRoom() && isShowing()) dismiss();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    protected void setImageShow(ImageView imageView, String url) {

        if (url == null) return;

        Picasso.with(getContext().getApplicationContext())
                .load(Uri.parse(url))
                .error(R.drawable.user_default_logo)
                .into(imageView);
    }

    public <T> T findViewOfId(int id) {
        return (T) rootView.findViewById(id);
    }

}
