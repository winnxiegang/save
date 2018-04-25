package com.android.xjq.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.banana.commlib.eventBus.EventBusMessage;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by lingjiu on 2017/3/4 12:37.
 * <p>
 * 适应横竖屏的对话框基类
 */
public abstract class DialogBase extends Dialog {
    protected View rootView;
    protected Context context;
    protected boolean isLandScape;
    private float mDimAmount = 0.3f;
    private boolean mIsAlignMainScreen;

    public DialogBase(Context context, int resId, int theme) {
        this(context, resId, theme, context.getResources().getConfiguration().ORIENTATION_PORTRAIT, false);
    }

    public DialogBase(Context context, int resId, int theme, int orientation) {
        this(context, resId, theme, orientation, false);
    }

    //isAlignMainScreen是否对齐公屏
    public DialogBase(Context context, int resId, int theme, int orientation, boolean isAlignMainScreen) {
        super(context, theme);
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(resId, null);
        setContentView(rootView);
        mIsAlignMainScreen = isAlignMainScreen;
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            isLandScape = false;
        } else {
            isLandScape = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = mDimAmount;
        //横竖屏时分别设定不同的宽度值、弹出方位以及动画
        if (isLandScape) {
            //dialog弹出时全屏显示
            ((LiveActivity) context).full(true);
            lp.width = getScreenWidth(context) / 5 * 3;
            lp.height = getScreenHeight(context);
            dialogWindow.setGravity(Gravity.RIGHT);
            dialogWindow.setWindowAnimations(R.style.dialog_anim_side);
            isLandScape = true;
        } else {
            lp.width = getScreenWidth(context);
            lp.height = getScreenHeight(context) - (mIsAlignMainScreen ? context.getResources().getDimensionPixelOffset(R.dimen.live_dialog_margin_top) : context.getResources().getDimensionPixelOffset(R.dimen.live_dialog_margin_top));
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.dialog_anim_bottom);
        }
        dialogWindow.setAttributes(lp);
    }

    protected void setDimAmount(float dimAmount) {
        mDimAmount = dimAmount;
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();//屏幕宽度
        return screenWidth;
    }


    protected int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = wm.getDefaultDisplay().getHeight();//屏幕高度
        return screenHeight;
    }

    public interface OnDialogStateListener {
        void dialogStateChangeListener(boolean isShow);
    }

    public void setOnDialogStateListener(OnDialogStateListener dialogStateListener) {
        this.dialogStateListener = dialogStateListener;
    }

    private OnDialogStateListener dialogStateListener;

    private boolean isShow;


    @Override
    public void show() {
        isShow = true;
        if (dialogStateListener != null) {
            dialogStateListener.dialogStateChangeListener(isShow);
        }
        EventBus.getDefault().register(this);
        super.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage message) {
        if (message.isRefreshLiveRoom() && isShow) dismiss();
    }

    @Override
    public void dismiss() {
        isShow = false;
        if (dialogStateListener != null) {
            dialogStateListener.dialogStateChangeListener(isShow);
        }
        EventBus.getDefault().unregister(this);
        super.dismiss();
    }

    private int getStatusBarHeight() {
        int statusBarHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

}
