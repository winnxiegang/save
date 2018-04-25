package com.android.xjq.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.AppParam;
import com.android.httprequestlib.okhttp.HttpUtil;
import com.android.xjq.R;
import com.android.xjq.activity.login.LoginActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by zhouyi on 2017/3/6.
 */

public abstract class BaseFragment extends Fragment {

    protected final String TAG = getClass().getSimpleName();

    protected final int DEFAULT_PAGE_SIZE = 8;

    protected final int DEFAULT_PAGE = 1;

    protected String requestType = AppParam.REFRESH_DATA;

    protected BaseActivity activity;
    private Unbinder mBind;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initData();
        super.onActivityCreated(savedInstanceState);
        Log.i("program", "onActivityCreated");
    }

    @Override
    public void onAttach(Activity parent) {
        super.onAttach(parent);
        activity = (BaseActivity) parent;
        Log.i("program", "onAttach");
    }

    protected abstract void initData();

    protected abstract View initView(LayoutInflater inflater);


    //头像
    protected void setLogo(ImageView iv, String url) {
        if (url == null) {
            return;
        }
        Picasso.with(activity.getApplicationContext())
                .load(Uri.parse(url))
                .error(R.drawable.user_default_logo)
                .into(iv);
    }

    protected void setLevelLogo(ImageView iv, String url) {
        if (TextUtils.isEmpty(url)) {
            iv.setVisibility(View.GONE);
            return;
        }

        iv.setVisibility(View.VISIBLE);

        Picasso.with(activity.getApplicationContext())
                .load(Uri.parse(url))
                .into(iv);
    }

    protected void setAnimImageShow(SimpleDraweeView simpleDraweeView, String url, boolean enable) {

        if (url == null) {
            return;
        }
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(url))
                .setAutoPlayAnimations(enable)
                .build();
        simpleDraweeView.setController(draweeController);
    }

    public boolean validateIsLogin() {

        if (LoginInfoHelper.getInstance().getUserId() == null) {

            LoginActivity.startLoginActivity(activity);

            return false;
        }

        return true;
    }


    /*给顶部view增加一个高度和padding,使得布局显示正常*/
    public void fitsSystemWindows(View view) {
        if (view == null || SDK_INT < Build.VERSION_CODES.KITKAT)
            return;
        int statusBarHeight = getStatusBarHeight();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params.height != -1)
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

    @Override
    public void onDetach() {
        super.onDetach();
        HttpUtil.cancelOkHttpCall(TAG);
        Log.i("program", "onDetach");
    }

    /**
     * 登录是否要带返回值
     */
    protected void toLogin() {

        //clearAllData();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        //将之前打开的activity都销毁掉
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        Log.i("program", "onDestroy");
    }

}
