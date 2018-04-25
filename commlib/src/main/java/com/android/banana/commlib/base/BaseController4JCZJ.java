package com.android.banana.commlib.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.android.banana.commlib.R;


/**
 * Created by mrs on 2017/4/5.
 */

public abstract class BaseController4JCZJ<T extends BaseActivity> extends BaseController<T> {

    public String TAG = this.getClass().getSimpleName();

    protected View contentView;

    protected View errorView;

    protected View emptyView;

    protected BaseController4JCZJ parentController;

    public BaseController4JCZJ() {

    }

    public BaseController4JCZJ(Activity activity) {
        super(activity);
    }

    public BaseController4JCZJ(Activity activity, BaseController4JCZJ parentController) {
        super(activity);
        this.parentController = parentController;
    }

    public abstract void setContentView(ViewGroup parent);

    public void setContentView(@NonNull ViewGroup parent, @LayoutRes int layoutResId) {
        this.setContentView(parent, layoutResId, 0);
    }

    public void setContentView(@NonNull ViewGroup parent, @LayoutRes int layoutResId, int index) {

        if (contentView != null && contentView.getParent() != null) {
            showContentView(true);
            return;
        }

        /**@LayoutRes int resource,
         * @Nullable ViewGroup root,
         * boolean attachToRoot
         * 如果 root 不为空,attachToRoot为true，那么解析出来的xml布局将是根布局
         * 如果 root 不为空,attachToRoot为false，那么root只会构建一个默认的generateLayoutParams();
         * @see LayoutInflater#inflate(int, ViewGroup)
         */
        onAttach(parent);

        contentView = LayoutInflater.from(getContext()).inflate(layoutResId, parent, false);

        setContentView(parent, contentView);

        onSetUpView();

        onSetUpData();

    }

    public void setContentView(@NonNull ViewGroup parent, @NonNull View addView) {

        if (contentView != null && contentView.getParent() != null && contentView.getId() == addView.getId()) {
            showContentView(true);
            return;
        }
        onAttach(parent);
        contentView = addView;
        parent.addView(contentView);
        showContentView(false);
    }

    //初始化 View
    public abstract void onSetUpView();

    //数据加载
    public void onSetUpData() {

    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public void setEmptyView(View emptyV) {
        this.emptyView = emptyV;
        showEmptyView();
    }

    public void setErrorView(View errorV) {
        this.emptyView = errorV;
        showErrorView();
    }

    public void showContentView(boolean hiddenChanged) {
        showView(contentView, hiddenChanged);
    }

    public void showErrorView() {
        if (errorView == null) {
            errorView = LayoutInflater.from(getContext()).inflate(R.layout.base_widget_empty_layout, getParent(), false);
        }
        ViewParent parent = errorView.getParent();
        if (parent != null)
            ((ViewGroup) parent).removeView(errorView);
        getParent().addView(errorView);

        showView(errorView, false);
    }

    public void showEmptyView() {
        if (emptyView == null) {
            emptyView = LayoutInflater.from(getContext()).inflate(R.layout.base_widget_empty_layout, getParent(), false);
        }
        ViewParent parent = emptyView.getParent();
        if (parent != null)
            ((ViewGroup) parent).removeView(emptyView);
        getParent().addView(emptyView);
        showView(emptyView, false);
    }

    private void showView(View showView, boolean HiddenChanged) {
        if (showView == null)
            return;
        int count = getParent().getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getParent().getChildAt(i);
            if (child != showView) {
                child.setVisibility(View.GONE);
                setIsvisible(false);
                if (HiddenChanged) onHiddenChanged(false);
                // }
            } else {
                child.setVisibility(View.VISIBLE);
                //首次创建View 不会回调onHiddenChanged  setVisiable方法
                //只有当来回切换时才会回调
                setIsvisible(true);
                if (HiddenChanged) onHiddenChanged(true);
            }
        }
        showView.bringToFront();

    }

    @Override
    public View getContentView() {
        return contentView;
    }

    protected void createCircularReveal(boolean expand) {
        if (getContentView().getHeight() == 0 || getContentView().getWidth() == 0)
            return;
        frontViewVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int width = getContentView().getWidth();
            int height = getContentView().getHeight();
            float endRadius = (float) Math.sqrt(width * width + height * height);
            final Animator animator = ViewAnimationUtils.createCircularReveal(getContentView(), 0, 0, expand ? 20 : endRadius, expand ? endRadius : 0);
            animator.setDuration(1000);
            animator.start();
            if (expand) {
                getContentView().setVisibility(View.VISIBLE);
            } else {
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        getContentView().setVisibility(View.GONE);
                        animator.removeAllListeners();
                    }
                });
            }
        } else if (!expand) {
            getContentView().setVisibility(View.GONE);
        }
    }

    private void frontViewVisibility() {
        int count = getParent().getChildCount();
        if (count > 1) {
            getParent().getChildAt(count - 2).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean isIsvisible() {
        return contentView == null ? false : contentView.getVisibility() == View.VISIBLE;
    }

    /**
     * 1.如果调用了 {@link BaseController4JCZJ#setContentView(ViewGroup)},这是一种正常将controller的view装在到ViewGroup中
     * <p>
     * 2.如果 你的controller的view 需要装在到ViewPager中特殊ViewGroup中，上面方法将不能再使用，需要手动inflate出controller的view,
     * 那么可以使用这个快速方法，那么将用不到{@link BaseController#getParent()}
     * ---------------------------后续可能会使用到Context，所以另需要调用{@link #setControllerContext(Context)}
     *
     * @param container
     * @param layoutRes
     * @return
     */
    public View setNoAttachContentView(@NonNull ViewGroup container, @LayoutRes int layoutRes) {
        setControllerContext(container.getContext());
        mParent = container;
        contentView = LayoutInflater.from(context).inflate(layoutRes, container, false);
        onSetUpView();
        onSetUpData();
        return contentView;
    }

    private void setControllerContext(Context context) {
        this.context = context;
        GenericControllerContext();
    }

    public BaseController4JCZJ getParentController() {
        return parentController;
    }

    public <T> T findViewOfId(@IdRes int id) {
        return (T) contentView.findViewById(id);
    }
}
