package com.android.banana.groupchat.base;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.banana.R;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.utils.DialogHelper;
import com.android.library.Utils.LibAppUtil;

import java.lang.reflect.Field;

/**
 * Created by qiaomu on 17/5/31.
 */
public abstract class BaseActivity4Jczj<T> extends com.android.banana.commlib.base.BaseActivity implements Toolbar.OnMenuItemClickListener {
    protected Toolbar mToolbar;
    protected TextView mToolbarTitle;
    public static final int MODE_BACK = 0;      // 左侧返回键
    public static final int MODE_DRAWER = 1;    //侧滑栏
    public static final int MODE_NONE = 2;      //不需要返回键

    private ProgressDialog mDialog;
    private Snackbar snackbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initEnv();

        setUpContentView();

        bindView();

//        if (isImmersive() && mToolbar != null) {
//            StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.half_transparent_4statusbar));
//            StatusBarCompat.fitsSystemWindows(mToolbar);
//        }
        setUpView();

        setUpData();
    }

    public boolean isImmersive() {
        return true;
    }

    /**
     * 初始化 环境，intent数据
     */
    protected void initEnv() {
    }

    protected void bindView() {

    }

    /**
     * 初始化 content view
     */
    protected abstract void setUpContentView();

    /**
     * 初始化 view
     */
    protected abstract void setUpView();

    /**
     * view中填写数据
     */
    protected abstract void setUpData();


    /**
     * 默认带有返回图标
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        setContentView(layoutResID, -1, -1, MODE_BACK);
    }

    /**
     * @param layoutResID
     * @param titleResId  标题（ResourceId）
     */
    public void setContentView(int layoutResID, int titleResId) {
        setContentView(layoutResID, titleResId, -1, MODE_BACK);
    }

    /**
     * @param layoutResID
     * @param titleStr    标题（字符串）
     */
    public void setContentView(int layoutResID, String titleStr) {
        setContentView(layoutResID, titleStr, -1, MODE_BACK);
    }

    public void setContentView(int layoutResID, int titleResId, int mode) {
        setContentView(layoutResID, titleResId, -1, mode);
    }

    public void setContentView(int layoutResID, String titleStr, int mode) {
        setContentView(layoutResID, titleStr, -1, mode);
    }

    public void setContentView(int layoutResID, int titleResId, int menuId, int mode) {
        super.setContentView(layoutResID);
        setUpToolbar(titleResId, menuId, mode);
    }

    public void setContentView(int layoutResID, String titleStr, int menuId, int mode) {
        super.setContentView(layoutResID);
        setUpToolbar(titleStr, menuId, mode);
    }

    protected void setUpToolbar(int titleResId, int menuId, int mode) {
        String title = "";
        if (titleResId > 0) {
            title = getString(titleResId);
        }
        setUpToolbar(title, menuId, mode);
    }

    protected void setUpToolbar(String titleStr, int menuId, int mode) {
        if (mode != MODE_NONE) {
            if (mToolbar == null) mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                mToolbar.setTitle("");
                mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
                if (mode == MODE_BACK) {
                    mToolbar.setNavigationIcon(R.drawable.ic_back_white);
                }
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNavigationBtnClicked();
                    }
                });

                reflectToolbar();
            }
            setUpTitle(titleStr);
            setUpMenu(menuId);
        }
    }

    public void onNavigationBtnClicked() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /*修改导航返回按钮左边距不可调节的问题*/
    private void reflectToolbar() {
        Class<? extends Toolbar> mToolbarClass = mToolbar.getClass();
        try {
            Field field = mToolbarClass.getDeclaredField("mNavButtonView");
            field.setAccessible(true);
            ImageButton imageButton = (ImageButton) field.get(mToolbar);
            Toolbar.LayoutParams params = (Toolbar.LayoutParams) imageButton.getLayoutParams();
            params.width = getResources().getDimensionPixelOffset(R.dimen.mNavButtonView_width);
            imageButton.setLayoutParams(params);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param menuId
     */
    protected void setUpMenu(int menuId) {
        if (mToolbar != null) {
            mToolbar.getMenu().clear();
            if (menuId > 0) {
                mToolbar.inflateMenu(menuId);
                mToolbar.setOnMenuItemClickListener(this);
                Menu menu = mToolbar.getMenu();
                int size = menu.size();
                for (int i = 0; i < size; i++) {
                    final MenuItem menuItem = menu.getItem(i);
                    View actionView = menuItem.getActionView();
                    if (actionView == null) continue;
                    actionView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onMenuItemClick(menuItem);
                        }
                    });
                }
            }
        }

    }

    protected void setUpTitle(int titleResId) {
        if (titleResId > 0 && mToolbarTitle != null) {
            mToolbarTitle.setText(titleResId);
        }
    }

    protected void setUpTitle(String titleStr) {
        if (!TextUtils.isEmpty(titleStr) && mToolbarTitle != null) {
            mToolbarTitle.setText(titleStr);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    public boolean isActivityFinished() {
        if (Build.VERSION.SDK_INT >= 17) {
            return this == null || isFinishing() || isDestroyed();
        } else {
            return this == null || isFinishing();
        }
    }

    public void showToast(String s) {
        if (isActivityFinished()) {
            return;
        }
        LibAppUtil.showTip(this, s);
    }

    protected void showToast(int stringId) {
        showToast(getString(stringId));
    }

    protected void showConfirmDialog(final String error, final boolean toLogin) {
        DialogHelper.showConfirmDialog(this, error, new OnMyClickListener() {
            @Override
            public void onClick(View v) {
                if (toLogin)
                    toLogin(false);
                else
                    BaseActivity4Jczj.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

}
