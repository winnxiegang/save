package com.android.banana.commlib.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import com.android.banana.commlib.R;

import java.util.ArrayList;

/**
 * Created by mrs on 2017/4/5.
 */

public class BaseController<T extends BaseActivity> {

    protected ViewGroup mParent;
    protected T mActivity;
    protected Context context;
    private boolean isvisible;//当前controller的contentview 时可见

    public BaseController() {

    }

    public BaseController(Activity activity) {
        this.mActivity = (T) activity;
    }

    protected void onAttach(@Nullable ViewGroup parent) {
        context = parent.getContext();

        GenericControllerContext();

        mParent = parent;
    }

    protected void GenericControllerContext() {
        if (mActivity == null) {
            if (context instanceof Activity) {
                mActivity = (T) context;
            }
        }
    }


    public ViewGroup getParent() {
        return mParent;
    }

    public void checkSelfPermissions(int requestCode, String... permissions) {
        /**6.0以下的申请检测权限只要在清单中配置了的，运行时机制 直接走允许
         * @see ActivityCompat#requestPermissions(Activity, String[], int)
         */
        if (permissions == null)
            return;
        if (mActivity == null) {
            throw new IllegalStateException("controller " + this + " not attached to Activity");
        }
        ArrayList<String> denied = new ArrayList<>();
        ArrayList<String> granted = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                denied.add(permission);
            } else {
                //do what you want
                granted.add(permission);
            }
        }
        //所有待申请的权限 ，已经获得权限的直接走onRequestPermissionsResult
        if (granted != null && granted.size() != 0) {
            String[] grantedStr = new String[granted.size()];
            onRequestPermissionsResult(requestCode, granted.toArray(grantedStr), new int[granted.size()]);
        }

        //所有待申请的权限 ，未获得权限的需要弹框申请  申请结果走onRequestPermissionsResult
        if (denied == null || denied.size() == 0)
            return;
        String[] deniedStr = new String[denied.size()];
        ActivityCompat.requestPermissions(mActivity, denied.toArray(deniedStr), requestCode);
    }

    //在Activity的调用 controller的 onActivityResult方法，做传递
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public String getString(@StringRes int stringRes) {
        return getContext().getString(stringRes);
    }

    //在Activity的调用 controller的 onRequestPermissionsResult，做传递
    // 权限请求结果 将会走这个方法，跟onActivityResult一样
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (!hasAllPermissionsGranted(grantResults)) {
            showMissingPermissionDialog();
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    //弹窗申请授权
    private void showMissingPermissionDialog() {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(getContext().getString(R.string.tips_permission_request_title))
                .setMessage(getContext().getString(R.string.tips_permission_request_content))
                .setPositiveButton(R.string.go_and_grant_permission, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startAppSettings();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.deny_and_quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    /**
     * Call {@link Activity#startActivity(Intent)} from the fragment's
     * containing Activity.
     */
    public void startActivity(Intent intent) {
        if (context == null) {
            throw new IllegalStateException("controller " + this + " not attached to Activity");
        }
        context.startActivity(intent);
    }

    /**
     * Call {@link Activity#startActivityForResult(Intent, int)} from the fragment's
     * containing Activity.
     */
    public void startActivityForResult(Intent intent, int requestCode) {
        if (mActivity == null) {
            throw new IllegalStateException("controller " + this + " not attached to Activity");
        }
        mActivity.startActivityForResult(intent, requestCode);
    }

    //跳转到应用设置权限界面
    private void startAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getContext().getPackageName()));
        startActivity(intent);
    }

    public void startService(Intent intent) {
        getContext().startService(intent);
    }

    public Context getContext() {
        return mActivity == null ? context : mActivity;
    }

    public T getActivity() {
        return mActivity;
    }

    public Resources getResouce() {
        return mActivity == null ? context.getResources() : mActivity.getResources();
    }

    public View getContentView() {
        return null;
    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onDestroy() {

    }

    public void setIsvisible(boolean isVisible) {
        this.isvisible = isVisible;
    }
    //当前controller是否显示

    public boolean isIsvisible() {
        return isvisible;
    }


    //这个解释下，当切换Controller的显示隐藏时调用，如同fragment的onHiddenChanged
    public void onHiddenChanged(boolean isVisiable) {

    }

    public boolean onBackPressed() {
        return false;
    }

    public int dp2px(float dipValue) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (density * dipValue + 0.5f);
    }


    public int sp2px(float spValue) {
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (scaledDensity * spValue + 0.5f);
    }
}
