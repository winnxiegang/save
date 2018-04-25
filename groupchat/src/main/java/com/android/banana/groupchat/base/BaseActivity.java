//package com.android.banana.groupchat.base;
//
//import android.app.ActionBar;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.banana.R;
//import com.android.banana.commlib.bean.ErrorBean;
//import com.android.banana.commlib.dialog.OnMyClickListener;
//import com.android.banana.commlib.dialog.ShowSimpleMessageDialog;
//import com.android.banana.groupchat.chatenum.ForbiddenActionType;
//import com.android.banana.commlib.utils.RefreshEmptyViewHelper;
//import com.android.library.Utils.LibAppUtil;
//import com.android.library.Utils.LogUtils;
//
//import org.greenrobot.eventbus.EventBus;
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Created by zhouyi on 2015/11/4 12:08.
// */
//public abstract class BaseActivity extends AppCompatActivity {
//    protected final int DEFAULT_PAGE_SIZE = 10;
//
//    protected final int DEFAULT_PAGE = 1;
//
//    private final String TAG = getClass().getSimpleName();
//
//    protected final int LOAD_MORE = 1;
//
//    protected final int REFRESH = 2;
//
//    protected int mRequestType = REFRESH;
//
//    private ProgressDialog mProgressDialog;
//
//    private PopupWindow mPopupWindow;
//
//    private View mPopupWindowView;
//    // 刷新相关
//    protected RefreshEmptyViewHelper mRefreshEmptyViewHelper;
//
//    protected int maxPages = 100;
//
//    protected int currentPage = 1;
//
//    protected Context context;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//
////        EventBus.getDefault().register(this);
//
//        context = this;
//
//        mProgressDialog = new ProgressDialog(this);
//
//        mProgressDialog.setMessage("数据加载中");
//
//        mProgressDialog.setCancelable(false);
//
//    }
//
//    public void showProgressDialog() {
//
//        if (mProgressDialog != null) mProgressDialog.show();
//
//    }
//
//    public void closeProgressDialog() {
//
//        if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();
//
//    }
//
//    @Override
//    public void setContentView(int layoutResID) {
//
//        View view = getLayoutInflater().inflate(layoutResID, null);
//
//        Drawable background = view.getBackground();
//
//        if (background == null) {
//            view.setBackgroundColor(getResources().getColor(R.color.normal_bg));
//        }
//
//        super.setContentView(view);
//
//    }
//
//    public void setTitleBar(boolean isShowBackIcon, String title) {
//
//        setTitleBar(isShowBackIcon, title, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//    }
//
//    public <T> T findViewOfId(int id) {
//        return (T) findViewById(id);
//    }
//
//    public void setTitleBar(boolean isShowBackIcon, String title, View.OnClickListener backClickListener) {
//
//        TextView titleTv = (TextView) findViewById(R.id.titleTv);
//
//        if (isShowBackIcon) {
//            setBackIv(backClickListener);
//        }
//
//        if (titleTv != null) {
//            titleTv.setText(title);
//        }
//    }
//
//    public void updateTitle(String title) {
//        TextView titleTv = (TextView) findViewById(R.id.titleTv);
//        if (titleTv != null) {
//            titleTv.setText(title);
//        }
//    }
//
//    public void setBackIv(final View.OnClickListener backClickListener) {
//        ImageView backIv = (ImageView) findViewById(R.id.backIv);
//        if (backIv != null) {
//            backIv.setVisibility(View.VISIBLE);
//            backIv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (backClickListener != null) {
//                        backClickListener.onClick(v);
//                    } else {
//                        finish();
//                    }
//                }
//            });
//        }
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        EventBus.getDefault().unregister(this);
//        setContentView(new View(this));
//        super.onDestroy();
//
//    }
//
//
//    public void operateErrorResponseMessage(JSONObject jo) throws JSONException {
//
//        operateErrorResponseMessage(jo, true);
//
//    }
//
//    public void operateErrorResponseMessage(JSONObject jo, boolean showTip) throws JSONException {
//        if (jo == null)
//            return;
//        ErrorBean bean = new ErrorBean(jo);
//        if (bean.getError() == null)
//            return;
//        String name = bean.getError().getName();
//        String tip = bean.getError().getMessage();
//
//        if ("USER_LOGIN_EXPIRED".equals(name) || "LOGIN_ELSEWHERE".equals(name)) {
//            try {
//                ShowSimpleMessageDialog dialog1 = new ShowSimpleMessageDialog(this, "该账号已在另一处登录,需要重新登录!", new OnMyClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        toLogin();
//                    }
//                });
//            } catch (Exception e) {
//                if (e instanceof WindowManager.BadTokenException) {
//                    LogUtils.e(TAG, e.getMessage());
//                }
//            }
//        } else if ("POST_FORBIDDEN".equals(name)) {
//            //禁言
//            ShowGagDialog(jo);
//        } else if ("USER_ID_REQUIRED".equals(name)) {
//            LibAppUtil.showTip(getApplicationContext(), tip, Toast.LENGTH_SHORT, -1);
//        } else if ("USER_LOGIN_FORBID".equals(name)) {
//            //在竞彩之家内账号禁止登录
//            showForbidDialog();
//        } else if ("ROLE_LOGIN_FORBID".equals(name)) {
//            //在竞彩之家内角色禁止登录
//            showForbidDialog();
//        } else if (ForbiddenActionType.Type.GROUP_HAS_DISMISSED.equals(name)
//                || TextUtils.equals("USER_HAD_REMOVE_GROUP", name)) {
//            //聊天室已解散，被移除聊天室
//            new ShowSimpleMessageDialog(this, tip, new OnMyClickListener() {
//                @Override
//                public void onClick(View v) {
//                    BaseActivity.this.finish();
//                }
//            });
//        } else {
//            if (showTip) {
//                LibAppUtil.showTip(getApplicationContext(), tip, Toast.LENGTH_SHORT, -1);
//            }
//        }
//    }
//
//    private void showForbidDialog() {
//
//        ShowSimpleMessageDialog showSimpleMessageDialog = new ShowSimpleMessageDialog(BaseActivity.this, toLoginMainActivityListener, Gravity.CENTER);
//    }
//
//    private void ShowGagDialog(JSONObject jo) {
//        try {
//            String gmtExpired = null;
//            String forbiddenReason = null;
//            if (jo.has("gmtExpired")) {
//                gmtExpired = jo.getString("gmtExpired");
//            }
//            if (jo.has("forbiddenReason")) {
//                forbiddenReason = jo.getString("forbiddenReason");
//            }
//            ShowSimpleMessageDialog simpleMessageDialog = new ShowSimpleMessageDialog(BaseActivity.this, "您已被禁言" + "\n" + "禁言原因:" + forbiddenReason + "\n" + "解封时间:" + gmtExpired);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private OnMyClickListener toLoginMainActivityListener = new OnMyClickListener() {
//        @Override
//        public void onClick(View v) {
//            toLogin();
//        }
//    };
//
//    public void toLogin() {
//        Intent intent = new Intent("com.android.xjq.loginmain");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        this.finish();
//    }
//
//    private void initPopupWindow() {
//
//        mPopupWindow = new PopupWindow(mPopupWindowView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
//
//        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
//
//        // 点击其他地方消失
//        mPopupWindowView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if (mPopupWindow != null && mPopupWindow.isShowing()) {
//                    mPopupWindow.dismiss();
//                    mPopupWindow = null;
//                }
//                return false;
//            }
//        });
//    }
//
//    protected void setPopupWindowView(View view) {
//
//        mPopupWindowView = view;
//
//    }
//
//    protected void getPopupWindow() {
//
//        if (null != mPopupWindow) {
//
//            mPopupWindow.dismiss();
//
//            return;
//
//        } else {
//            initPopupWindow();
//        }
//
//    }
//
//    protected String getStringExtra(String str) {
//        if (getIntent() == null) {
//            return "";
//        } else {
//            return getIntent().getStringExtra(str);
//        }
//    }
//
//    protected void showJsonError(JSONException e) {
//        LogUtils.e("JSON解析错误", e.toString());
//    }
//
//
//    public ProgressDialog getProgressDialog() {
//
//        ProgressDialog dialog = new ProgressDialog(this);
//
//        dialog.setCancelable(false);
//
//        dialog.setMessage("数据加载中...");
//
//        return dialog;
//
//    }
//
//    @Override
//    protected void onStop() {
//
//        super.onStop();
//
//    }
//
//}
