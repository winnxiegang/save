package com.android.banana.commlib.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 * Created by MrS on 2016/8/15.
 */
public class PermissionUtil {
    public static PermissionUtil permissionUtil;
    private static final int REQUEST_CODE = 1;

    public static boolean isPermissionEnable(@NonNull Activity context, @NonNull String permission){
       return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


    public static void checkPermission(Activity context, String... permission) {
        ActivityCompat.requestPermissions(context, permission, REQUEST_CODE);
    }


    public interface PermissionChecker {
        void onGrant(String grantPermission, int index);

        /*deniedPermission  被禁止的那个权限名字  该权限在 String[] permissions 属于第几个位置 因为有可能一次申请好几个*/
        void onDenied(String deniedPermission, int index);
    }

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults,PermissionChecker checker) {
        if (checker == null)
            return;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                if (checker != null)
                    checker.onGrant(permissions[i], i);
            } else {
                if (checker != null)
                    checker.onDenied(permissions[i], i);
            }
        }
    }
}

