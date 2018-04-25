package com.android.banana.commlib.coupon;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.android.banana.commlib.R;
import com.android.banana.commlib.eventBus.EventBusMessage;
import com.android.library.Utils.LibAppUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 直播间内适应横竖屏的popwindow
 * Created by lingjiu on 2017/7/19.
 */

public class BottomAndSidePop extends BasePopupWindow {

    private int gravity;

    public BottomAndSidePop(Context context, int resId, int orientation) {
        super(context, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setContentView(View.inflate(context, resId, null));
        if (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == orientation) {
            setHeight(LibAppUtil.getScreenHeight(context) - context.getResources().getDimensionPixelOffset(R.dimen.live_dialog_margin_top));
            setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            setAnimationStyle(R.style.dialog_anim_bottom);
            gravity = Gravity.BOTTOM;
            setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        } else if (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == orientation) {
            setHeight(LibAppUtil.getScreenHeight(context));
            setWidth(LibAppUtil.getScreenWidth(context) * 3 / 5);
            setAnimationStyle(R.style.dialog_anim_side);
            gravity = Gravity.RIGHT;
        }
        onCreate();
    }

    protected void onCreate() {
    }

    protected void onResume() {
    }

    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    public void show() {
        showAtLocation(parentView, gravity, 0, 0);
        onResume();
        EventBus.getDefault().register(this);
    }

    public void show(View parentView) {
        showAtLocation(parentView, gravity, 0, 0);
        onResume();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage message) {
        if (message.isRefreshLiveRoom() && isShowing()) dismiss();
    }

    @Override
    public void dismiss() {
        onDestroy();
        super.dismiss();
    }


}
