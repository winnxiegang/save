package com.android.banana.commlib.base;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.R;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.dialog.ShowSimpleMessageDialog;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.NetworkUtils;
import com.android.banana.commlib.utils.RefreshEmptyViewHelper;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.httprequestlib.okhttp.HttpUtil;
import com.android.library.Utils.LogUtils;
import com.android.library.Utils.encryptUtils.StringUtils;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by zhouyi on 2017/3/2.
 */

public class BaseActivity extends AppCompatActivity implements PresentationLayerFunc {

    private PresentationLayerFuncHelper presentationLayerFuncHelper;

    protected final int DEFAULT_PAGE_SIZE = 10;

    protected final int DEFAULT_PAGE = 1;

    public final String TAG = getClass().getSimpleName();

    protected final int LOAD_MORE = 1;

    protected final int REFRESH = 2;

    protected int mRequestType = REFRESH;

    // 刷新相关
    protected RefreshEmptyViewHelper mRefreshEmptyViewHelper;

    protected int maxPages = 100;

    protected int currentPage = 1;
    //触摸空白区域隐藏键盘(非EditView区域)
    protected boolean isSupportTouchHideKeyBoard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        //在Android7.0之后不支持在manifest中注册,这里统一动态注册,
        registerReceiver(netBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        presentationLayerFuncHelper = new PresentationLayerFuncHelper(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void compat(View view) {
        compat(view, ContextCompat.getColor(this, R.color.transparent));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void compat(View view, @ColorInt int color) {
        Window window = getWindow();
        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (color >= 0) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } else if (SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //fitsSystemWindows(view);
    }

    /*给顶部view增加一个高度和padding,使得布局显示正常*/
    public void fitsSystemWindows(View view) {
        if (view == null || SDK_INT < Build.VERSION_CODES.KITKAT)
            return;
        int statusBarHeight = getStatusBarHeight();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height += statusBarHeight;
        view.setLayoutParams(params);
        view.setPadding(view.getPaddingLeft(),
                view.getPaddingTop() + statusBarHeight,
                view.getPaddingRight(),
                view.getPaddingBottom());
    }

    public int getStatusBarHeight() {
        // 获得状态栏高度
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        return statusBarHeight;
    }


    public void setTitleBar(boolean isShowBackIcon, String title, int color) {
        setTitleBar(isShowBackIcon, title, null);
        compat(findViewById(R.id.titleLayout), color);
    }

    public void setTitleBar(boolean isShowBackIcon, String title) {
        setTitleBar(isShowBackIcon, title, null);
        compat(findViewById(R.id.titleLayout), ContextCompat.getColor(this, R.color.transparent));
    }

    public void setTitleBar(boolean isShowBackIcon, String title, boolean isNeedCharge) {
        setTitleBar(isShowBackIcon, title, null);

        TextView rechargeTv = (TextView) findViewById(R.id.rechargeTv);

        if (isNeedCharge) rechargeTv.setVisibility(View.VISIBLE);

        rechargeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.android.xjq.recharge");
                startActivity(intent);
            }
        });
    }

    public void setTitleBar(boolean isShowBackIcon, String title, View.OnClickListener backClickListener) {
        TextView titleTv = (TextView) findViewById(R.id.titleTv);

        if (isShowBackIcon) {
            setBackIv(backClickListener);
        }

        if (titleTv != null) {
            titleTv.setText(title);
        }

    }

    public void setTitleNone() {
        TextView titleTv = (TextView) findViewById(R.id.titleTv);
        titleTv.setVisibility(View.GONE);
        ImageView backIv = (ImageView) findViewById(R.id.backIv);
        backIv.setVisibility(View.GONE);
    }

    private void setBackIv(final View.OnClickListener backClickListener) {
        ImageView backIv = (ImageView) findViewById(R.id.backIv);
        if (backIv != null) {
            backIv.setVisibility(View.VISIBLE);
            backIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (backClickListener != null) {
                        backClickListener.onClick(v);
                    } else {
                        finish();
                    }
                }
            });
        }
    }

    public void operateErrorResponseMessage(JSONObject jo) throws JSONException {
        operateErrorResponseMessage(jo, true);
    }

    public void operateErrorResponseMessage(JSONObject jo, boolean showTip) throws JSONException {

        if (jo == null)
            return;
        ErrorBean bean = new ErrorBean(jo);
        if (bean.getError() == null)
            return;
        String name = bean.getError().getName();
        String tip = bean.detailMessage != null && !"".equals(bean.getDetailMessage()) ? bean.detailMessage : bean.getError().getMessage();

        if ("USER_LOGIN_EXPIRED".equals(name) || "LOGIN_ELSEWHERE".equals(name)) {
            showReLoginDialog();
        } else if ("POST_FORBIDDEN".equals(name)) {
            //禁言
            ShowGagDialog(jo);
        } else if ("USER_ID_REQUIRED".equals(name)) {
            LibAppUtil.showTip(getApplicationContext(), tip, Toast.LENGTH_SHORT, -1);
        } else if ("LOGIN_FORBIDDEN".equals(name)) {
            //在竞彩之家内账号禁止登录
            showForbidDialog();
        } else if ("ROLE_LOGIN_FORBID".equals(name)) {
            //在竞彩之家内角色禁止登录
            showForbidDialog();
        } else if ("GROUP_HAS_DISMISSED".equals(name) || TextUtils.equals("USER_HAD_REMOVE_GROUP", name)) {
            //聊天室已解散，被移除聊天室
            new ShowSimpleMessageDialog(this, tip, new OnMyClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity.this.finish();
                }
            });
        } else if ("DATA_LOCK_FAILED".equals(name)) {

        } else if ("ACCOUNT_ERROR".equals(name)) {
        } else {
            if (TextUtils.equals("CAN_NOT_SOLE", name)) {
                new ShowSimpleMessageDialog(this, tip, new OnMyClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseActivity.this.finish();
                    }
                });
            } else {
                if (showTip) {
                    ToastUtil.show(getApplicationContext(), tip, Toast.LENGTH_SHORT);
               /* if (Build.VERSION.SDK_INT >= 25) {
                    showMessageDialog(tip);
                } else {
                }*/
                }
            }
        }
    }

    public void showRechargeView() {
        new ShowMessageDialog(this, "去充值", "取消", new OnMyClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.android.xjq.recharge"));
            }
        }, null, "余额不足,请立即充值");
        return;
    }


    public void showMessageDialog(String message) {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage(message)
                    .setPositiveButton("确定", null)
                    .setNegativeButton("取消", null)
                    .show();
        } catch (Exception e) {
            if (e instanceof WindowManager.BadTokenException) {
                LogUtils.e(TAG, e.getMessage());
            }
        }
    }

    public void showReLoginDialog() {
        try {
            ShowSimpleMessageDialog dialog1 = new ShowSimpleMessageDialog(this, "该账号已在另一处登录,需要重新登录!", new OnMyClickListener() {
                @Override
                public void onClick(View v) {
                    toLogin(false);
                }
            });
        } catch (Exception e) {
            if (e instanceof WindowManager.BadTokenException) {
                LogUtils.e(TAG, e.getMessage());
            }
        }
    }


    private void showForbidDialog() {
//        ShowSimpleMessageDialog dialog = new ShowSimpleMessageDialog(this, R.drawable.icon_dialog_title_warning,
//                getString(R.string.relogin), toLoginMainActivityListener, getString(R.string.account_abnormality));
    }

//    protected OnMyClickListener toLoginMainActivityListener = new OnMyClickListener() {
//        @Override
//        public void onClick(View v) {
//            toLogin(false);
//        }
//    };

    private void ShowGagDialog(JSONObject jo) {
        try {
            String gmtExpired = null;
            String forbiddenReason = null;
            if (jo.has("gmtExpired")) {
                gmtExpired = jo.getString("gmtExpired");
            }
            if (jo.has("forbiddenReason")) {
                forbiddenReason = jo.getString("forbiddenReason");
            }
            ShowSimpleMessageDialog simpleMessageDialog = new ShowSimpleMessageDialog(BaseActivity.this, "您已被禁言" + "\n" + "禁言原因:" + forbiddenReason + "\n" + "解封时间:" + gmtExpired);

            //ShowMessageDialog dialog = new ShowMessageDialog(BaseActivity.this, R.drawable.icon_dialog_title_warning,)
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object event) {/* Do something */}

    protected boolean isLogin() {
        return LoginInfoHelper.getInstance().getUserId() != null;
    }


    protected void setImageShow(ImageView imageView, String url) {
        if (url == null) return;

        Picasso.with(getApplicationContext())
                .load(Uri.parse(url))
                .error(R.drawable.user_default_logo)
                .into(imageView);
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        unregisterReceiver(netBroadcastReceiver);
        HttpUtil.cancelOkHttpCall(TAG);
        if (presentationLayerFuncHelper != null) presentationLayerFuncHelper.onDestroy();
        super.onDestroy();
    }

    /**
     * 初始化时判断有没有网络
     */
    public boolean inspectNet() {
        int netWorkState = NetworkUtils.getNetworkState(BaseActivity.this);
        if (NetworkUtils.NETWORK_NONE == netWorkState) return false;
        return true;
    }

    private BroadcastReceiver netBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int netWorkState = NetworkUtils.getNetworkState(context);
            onNetChange(NetworkUtils.NETWORK_NONE != netWorkState);
        }

    };

    /**
     * 网络变化回调
     *
     * @param isNetConnected
     */
    protected void onNetChange(boolean isNetConnected) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected boolean isBlank(EditText editText, String message) {
        if (StringUtils.isBlank(editText.getText().toString().trim())) {
            LibAppUtil.showTip(this, message);
            return true;
        }
        return false;
    }

    /**
     * VideoView内部的AudioManager会对Activity持有一个强引用,
     * 而AudioManager的生命周期比较长，导致这个Activity始终无法被回收,
     * 这边用到AudioManager服务的时候改用applicationContext去获取音频服务
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new ContextWrapper(newBase) {
            @Override
            public Object getSystemService(String name) {
                if (Context.AUDIO_SERVICE.equals(name))
                    return getApplicationContext().getSystemService(name);
                return super.getSystemService(name);
            }
        });
    }

    /**
     * 登录是否要带返回值
     */
    protected void toLogin(boolean isLogout) {
        clearAllData(isLogout);
        Intent intent = new Intent("com.android.xjq.login");
        //将之前打开的activity都销毁掉\
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void clearAllData(boolean isLogout) {
        LoginInfoHelper.getInstance().clearUserInfoCache();
        if (isLogout) LoginInfoHelper.getInstance().clearUserName();
        LoginInfoHelper.getInstance().getCache();
    }

    @Override
    public void showToast(String msg) {
        presentationLayerFuncHelper.showToast(msg);
    }

    @Override
    public void showProgressDialog(String loadingTxt) {
        presentationLayerFuncHelper.showProgressDialog(loadingTxt);
    }

    public void showProgressDialog() {
        presentationLayerFuncHelper.showProgressDialog(getString(R.string.loading));
    }

    public void closeProgressDialog() {
        hideProgressDialog();
    }

    @Override
    public void hideProgressDialog() {
        presentationLayerFuncHelper.hideProgressDialog();
    }

    @Override
    public void showErrorMsg(JSONObject jsonObject) {
        try {
            operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showSoftKeyboard(View focusView) {
        presentationLayerFuncHelper.showSoftKeyboard(focusView);
    }

    @Override
    public void hideSoftKeyboard() {
        presentationLayerFuncHelper.hideSoftKeyboard();
    }

    public <T> T findViewOfId(int id) {
        return (T) findViewById(id);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideSoftKeyboard();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (!isSupportTouchHideKeyBoard) return false;

        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }
}
