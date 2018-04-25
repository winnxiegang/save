package com.android.banana.commlib.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by qiaomu on 2017/6/1.
 */

public class StatusBarCompat {
    public static int alphaColor(int color, int alpha) {
        color &= ~(0xFF << 24);
        color |= alpha << 24;
        return color;
    }

    public static int color(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red | green << 8 | blue;
    }

    /**
     * << 16
     * activity全屏，伸入statusbar在下面
     * 需要设置 toolbar paddingTop
     *
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, @ColorInt int color) {

        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            if (color >= 0) {

                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } else if (SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }

    }

    /*给一个VIew曾加顶部 间距=状态栏高度,使得布局显示正常*/

    /**
     * @param view 记住了  这个 必须在布局里有固定的高度。
     */
    public static void fitsSystemWindows(@NonNull View view) {
        if (view == null || SDK_INT < 19 )
            return;
        int statusBarHeight = getStatusBarHeight(view.getContext());
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height += statusBarHeight;
        view.setLayoutParams(params);
        view.setPadding(view.getPaddingLeft(),
                view.getPaddingTop() + statusBarHeight,
                view.getPaddingRight(),
                view.getPaddingBottom());
    }

    /*恢复setUpToolbarLayoutParams（）设置的间距*/
    public static void unFitsSystemWindows(@NonNull View view) {
        if (view == null)
            return;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height -= getStatusBarHeight(view.getContext());
        view.setLayoutParams(params);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //开启全屏模式
    public static void hideSystemUI(Activity activity) {
        View mDecorView = activity.getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    //取消全屏模式
    private static void showSystemUI(Activity activity) {
        View mDecorView = activity.getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /*解决 沉浸式状态栏  + 输入框Editext   + adjustResize 输入框不能被弹起的问题*/
    public static void fix4ImmersiveStatusBar(final View rootView) {
        if (SDK_INT < 19)
            return;
        WeakReference<View> mViewReference = new WeakReference<>(rootView);
        if (mViewReference == null || mViewReference.get() == null)
            return;
        mViewReference.get().getViewTreeObserver().addOnGlobalLayoutListener(new GlobalLayoutListener(mViewReference));
    }

    private static class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private ViewGroup.LayoutParams frameLayoutParams;
        private int usableHeightPrevious;
        private WeakReference<View> mViewReference;

        public GlobalLayoutListener(WeakReference<View> mViewReference) {
            this.mViewReference = mViewReference;
            if (mViewReference == null || mViewReference.get() == null)
                return;
            frameLayoutParams = mViewReference.get().getLayoutParams();
            if (frameLayoutParams == null)
                frameLayoutParams = new ViewGroup.LayoutParams(-1, -1);
        }

        @Override
        public void onGlobalLayout() {
            if (mViewReference == null)
                return;
            View view = mViewReference.get();
            if (view == null)
                return;
            Rect r = new Rect();
            view.getWindowVisibleDisplayFrame(r);
            int usableHeightNow = r.bottom;
            if (usableHeightNow != usableHeightPrevious) {
                //如果两次高度不一致
                //将计算的可视高度设置成视图的高度
                frameLayoutParams.height = usableHeightNow;
                view.requestLayout();//请求重新布局
                usableHeightPrevious = usableHeightNow;
            }
        }
    }
}