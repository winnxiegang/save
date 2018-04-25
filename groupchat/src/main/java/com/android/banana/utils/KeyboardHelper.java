package com.android.banana.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import com.android.banana.R;
import com.android.banana.pullrecycler.recyclerview.PullRecycler;
import com.android.banana.view.ImLayout;
import com.android.banana.view.TapLinearLayout;

import java.lang.ref.WeakReference;

/**
 * Created by mrs on 2017/4/1.
 */

public class KeyboardHelper {


    /*虚拟导航栏高度*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getSoftButtonsBarHeight(Activity mActivity) {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /*屏幕高度但不包括虚拟导航键*/
    // 可能你的设备屏幕是1920px ，这个方法获取的 是去掉虚拟导航键高度之后的，估计也就有1794px
    //但如果 设备没有虚拟导航按钮 那么这个就是设备屏幕高度
    public static int getScreenHeight(Context context) {
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        return heightPixels;
    }

    public static int getScreenWidth(Context context) {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        return widthPixels;
    }

    public static void showSoftInput(View focusView) {
        if (focusView == null) return;
        WeakReference<View> reference = new WeakReference<>(focusView);
        if (reference.get() != null) {
            InputMethodManager manager = (InputMethodManager) focusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.showSoftInput(reference.get(), 0);
        }
    }

    public static void hideSoftInput(View focusView) {
        if (focusView == null) return;
        WeakReference<View> reference = new WeakReference<>(focusView);
        if (reference.get() != null) {
            InputMethodManager manager = (InputMethodManager) focusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(reference.get().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void toggleInput(Context context, View focusView) {
        if (focusView == null) return;
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean isInputMethodActive(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return manager.isActive();
    }

    public static void updateSoftInputMode(Activity activity, int softInputMode) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.softInputMode = softInputMode;
        activity.getWindow().setAttributes(params);
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(identifier);
    }


    /**
     * 适用于聊天界面弹起键盘的操作(群聊 ，比分直播)
     * <p>
     * 1.点击任意位置 软键盘消失，表情或者菜单面板也消失,同时会清除已选中的  RadioGroup inputLayout的radiobutton
     * <p>
     * 2.可避免软键盘跟表情或者菜单切换时闪屏问题
     *
     * @param activity  聊天界面
     * @param imLayout  聊天输入框
     * @param imLayout  输入框所在直接父布局（radioGroup）
     * @param imLayout  表情或者菜单面板
     * @param tapParent LinearLayoutParent点击任意位置 隐藏键盘,表情或则菜单面板
     */
    public static void postDelayAvoidFlashingScreen4Input(@NonNull final AppCompatActivity activity,
                                                          @NonNull final ImLayout imLayout,
                                                          @NonNull final TapLinearLayout tapParent,
                                                          @NonNull final PullRecycler pullRecycler) {
        //1.点击输入框,也即代表软键盘要弹起,此时要清空inputLayout的选中radioButton
        //2.postDelayed 设置软键盘模式为SOFT_INPUT_ADJUST_RESIZE，否则界面不会被自动挤压弹起
        //3.如果列表当前不是isStackFromEnd,列表的itemCount大于0，那么键盘弹起，列表自动滚动到最后一个
        //这个情形自行想象
        if (imLayout.emojiEditv != null)
            imLayout.emojiEditv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    imLayout.inputLayout.clearCheck();
                    imLayout.menuLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            KeyboardHelper.updateSoftInputMode(activity, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                            imLayout.menuLayout.setVisibility(View.GONE);
                            boolean isStackFromEnd = pullRecycler.isStackFromEnd();
                            int position = pullRecycler.adapter.getItemCount() - 1;
                            if (!isStackFromEnd && position > 0) {
                                pullRecycler.getRecyclerView().smoothScrollToPosition(position);
                            }
                        }
                    }, 300);
                    return false;
                }
            });
        /*点击任何地方键盘消失*/
        if (tapParent != null) {
            tapParent.setonTapListener(new TapLinearLayout.onTapListener() {
                @Override
                public void onTouch(float x, float y) {
                    dispatchViewTouch(x, y, imLayout);
                }
            });
            //监听软键盘的弹起 收回
            //1.当软键盘打开 且列表不是isStackFromEnd,那么列表滚动到最后一条
            //2.如果软键盘关闭,且不是表不是isStackFromEnd且表情面板或展开菜单不是显示的，延迟个300毫秒,因为键盘的动画时间大约是285毫秒
            //判断列表内容是否可以上下滑动，如果可以那么设置列表setStackFromEnd，并且固定到之前的位置
            tapParent.setOnSizeChangedListener(new TapLinearLayout.onSizeChangedListener() {
                @Override
                public void onSizeChanged(boolean kbOpened, int kbHeight) {
                    if (kbOpened && !pullRecycler.isStackFromEnd()) {
                        pullRecycler.scrollToPosition(false);
                    } else if (!kbOpened && !pullRecycler.isStackFromEnd() && imLayout.menuLayout.getVisibility() != View.VISIBLE) {
                        pullRecycler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                boolean canScrollVertically = pullRecycler.canScrollVertical();
                                int position = pullRecycler.getLastVisibleItemPosition();
                                if (canScrollVertically != pullRecycler.isStackFromEnd()) {
                                    pullRecycler.setStackFromEnd(true);
                                    pullRecycler.getLayoutManager().scrollPositionWithOffset(position, 0);
                                }
                            }
                        }, 300);
                    }
                }
            });
        }
    }

    private static boolean dispatchViewTouch(float x, float y, ImLayout imLayout) {
        boolean isMenuShow = dispatchViewTouch(x, y, imLayout.menuLayout);
        boolean contains = dispatchViewTouch(x, y, imLayout.inputLayout);
        if (contains == false && isMenuShow == false) {
            imLayout.inputLayout.clearCheck();
            imLayout.menuLayout.startVisibilityAnimation(View.GONE);
            KeyboardHelper.hideSoftInput(imLayout.emojiEditv);
        }
        return contains == false && isMenuShow == false;
    }

    public static boolean dispatchViewTouch(float x, float y, View excludeView) {
        if (excludeView.getVisibility() == View.VISIBLE) {
            Rect rect1 = new Rect();
            excludeView.getGlobalVisibleRect(rect1);
            return rect1.contains((int) x, (int) y);
        }
        return false;
    }


    /**
     * 适用于聊天界面显示菜单面板(群聊 ，比分直播) 隐藏软键盘的操作
     * <p>
     * <p>
     * 同样是为了避免表情或者展开菜单跟软键盘切换时的闪屏
     *
     * @param //menuItemClickListener 展开菜单Item点击回调
     * @param //emojClickListener     表情点击回调
     * @param activity                聊天界面所在activity
     * @param imLayout                情或者展开菜单
     * @param imLayout                输入框,或者焦点View
     * @param clickViewId             点击事件ViewId,(表情,展开菜单...)
     * @param pullRecycler
     */
    public static void postDelayAvoidFlashingScreen4Menu(@NonNull AppCompatActivity activity,
                                                         @NonNull final ImLayout imLayout,
                                                         @NonNull final int clickViewId,
                                                         @NonNull final PullRecycler pullRecycler) {

        if (activity == null || imLayout == null || clickViewId < 0)
            return;
        //如果当时软键盘已经打开，把window属性设置为WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING  可以有效避免键盘收回闪动效果
        final boolean active = isActive(activity);
        if (isInputMethodActive(activity)) {
            KeyboardHelper.updateSoftInputMode(activity, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            KeyboardHelper.hideSoftInput(imLayout.emojiEditv);
        }
        imLayout.menuLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (clickViewId == R.id.emoji) {
                    boolean showEmoji = imLayout.menuLayout.isShowEmoji();
                    if (!showEmoji) {
                        imLayout.menuLayout.showEmojiPanel(active);
                    } else {
                        imLayout.menuLayout.startVisibilityAnimation(View.GONE);
                        imLayout.inputLayout.clearCheck();
                    }
                } else if (clickViewId == R.id.more) {
                    boolean showMenu = imLayout.menuLayout.isShowMenu();
                    if (!showMenu) {
                        imLayout.menuLayout.showMenuMore(active);
                    } else {
                        imLayout.menuLayout.startVisibilityAnimation(View.GONE);
                        imLayout.inputLayout.clearCheck();
                    }
                } else {
                    imLayout.menuLayout.startVisibilityAnimation(View.GONE);
                    imLayout.inputLayout.clearCheck();
                }
            }
        }, 10);

        pullRecycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!pullRecycler.isStackFromEnd())
                    pullRecycler.scrollToPosition(false);
            }
        }, 285);
    }

    // 可视界面底部 不等于屏幕高度（不包含虚拟导航栏）
    public static boolean isActive(@NonNull Activity mActivity) {
        Rect r = new Rect();
        int screenHeight = getScreenHeight(mActivity);
        // int barHeight = getSoftButtonsBarHeight(mActivity);
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        return r.bottom != screenHeight;
    }
}
