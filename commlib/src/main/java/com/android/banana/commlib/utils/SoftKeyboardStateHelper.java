package com.android.banana.commlib.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import com.android.library.Utils.LogUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * 键盘开启与关闭监听检测类
 */
public class SoftKeyboardStateHelper implements ViewTreeObserver.OnGlobalLayoutListener {

    public interface SoftKeyboardStateListener {
        void onSoftKeyboardOpened(int keyboardHeightInPx);

        void onSoftKeyboardClosed();
    }

    private final List<SoftKeyboardStateListener> listeners = new LinkedList<>();
    private final View activityRootView;
    private int lastSoftKeyboardHeightInPx;
    private boolean isSoftKeyboardOpened;
    private Activity activity;

    public SoftKeyboardStateHelper(Activity activity, View activityRootView) {
        this(activityRootView, false);
        this.activity = activity;
    }

    public SoftKeyboardStateHelper(View activityRootView, boolean isSoftKeyboardOpened) {

        this.activityRootView = activityRootView;

        this.isSoftKeyboardOpened = isSoftKeyboardOpened;

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);

    }
    private int preHeightDiff;
    @Override
    public void onGlobalLayout() {

        final Rect r = new Rect();

        //r will be populated with the coordinates of your view that area still visible.
        activityRootView.getWindowVisibleDisplayFrame(r);

        int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);

//        LogUtils.e("r.bottom", r.bottom + " r.top"+r.top);

        LogUtils.e("heightDiff", heightDiff + " ");

        if (!isSoftKeyboardOpened && heightDiff - preHeightDiff > 0) {
            isSoftKeyboardOpened = true;
            notifyOnSoftKeyboardOpened(heightDiff);
        } else if (isSoftKeyboardOpened && heightDiff - preHeightDiff < 0) {
            isSoftKeyboardOpened = false;
            notifyOnSoftKeyboardClosed();
        }
        preHeightDiff = heightDiff;

    }

    public void setIsSoftKeyboardOpened(boolean isSoftKeyboardOpened) {
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
    }

    public boolean isSoftKeyboardOpened() {
        return isSoftKeyboardOpened;
    }

    /**
     * Default value is zero (0)
     *
     * @return last saved keyboard height in px
     */
    public int getLastSoftKeyboardHeightInPx() {
        return lastSoftKeyboardHeightInPx;
    }

    public void addSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        listeners.add(listener);
    }

    public void removeSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        listeners.remove(listener);
    }

    private void notifyOnSoftKeyboardOpened(int keyboardHeightInPx) {
        this.lastSoftKeyboardHeightInPx = keyboardHeightInPx;

        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardOpened(keyboardHeightInPx);
            }
        }
    }

    private void notifyOnSoftKeyboardClosed() {
        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardClosed();
            }
        }
    }
}