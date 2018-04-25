package com.android.banana.commlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.android.banana.commlib.utils.toast.ToastUtil;

import java.io.File;

import static android.app.Activity.RESULT_OK;


/**
 * Created by qiaomu on 17/10/31.
 */

public class PhotoUtil {

    public static final int SELECT_SINGLE_IMAGE = 10;
    public static final int CROP_IMAGE = 11;
    public static final int CAPTURE = 12;


    public static void capture(Activity activity, String tempCapturePath) {
        File file = new File(tempCapturePath);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, parseUri(activity, tempCapturePath));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        activity.startActivityForResult(cameraIntent, CAPTURE);
    }

    public static void cropImage(Activity activity, Uri inUri, String tempOutPath, int aspectx, int aspecty, int outputX, int outputY) {
        File file = new File(tempOutPath);
        file.mkdir();
        if (file.exists())
            file.delete();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectx);
        intent.putExtra("aspectY", aspecty);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false); //true返回数据bitmap
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        activity.startActivityForResult(intent, CROP_IMAGE);
    }


    public static Uri parseUri(Context context, String filePath) {
        if (TextUtils.isEmpty(filePath))
            return null;
        File file = new File(filePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return FileProvider.getUriForFile(context, "com.android.xjq.fileProvider", file);
        else
            return Uri.fromFile(file);
    }


    public static void selectAlbums(Activity activity) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        activity.startActivityForResult(intent, SELECT_SINGLE_IMAGE);
    }


    public static boolean onActivityResult(Activity activity, String tempCapturePath, String tempCropPath, int requestCode, int resultCode, Intent data, Callback listenter) {
        return onActivityResult(activity, tempCapturePath, tempCropPath, requestCode, resultCode, 1, 1, 200, 200, data, listenter);
    }

    public static boolean onActivityResult(Activity activity, String tempCapturePath, String tempCropPath, int requestCode, int resultCode, int aspectx, int aspecty, int outputX, int outputY, Intent data, Callback listenter) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_SINGLE_IMAGE:
                    Uri inUri = data.getData();
                    cropImage(activity, inUri, tempCropPath, aspectx, aspecty, outputX, outputY);
                    return true;
                case CROP_IMAGE:
                    //如果文件不存在
                    if (tempCropPath == null || "".equals(tempCropPath) || !new File(tempCropPath).exists()) {
                        ToastUtil.showLong(activity.getApplicationContext(), "file not exists 1");
                        //通知裁剪完成了
                    } else if (listenter != null) {
                        listenter.onResult();
                    }
                    return true;
                case CAPTURE:
                    if (!TextUtils.isEmpty(tempCapturePath)) {
                        File file = new File(tempCapturePath);
                        if (file.exists())
                            cropImage(activity, parseUri(activity, tempCapturePath), tempCropPath, aspectx, aspecty, outputX, outputY);
                        else {
                            ToastUtil.showLong(activity.getApplicationContext(), "file not exists 2");
                        }
                    } else {
                        ToastUtil.showLong(activity.getApplicationContext(), "file not exists 3");
                    }
                    return true;
            }
        }
        return false;
    }




    public interface Callback {
        void onResult();
    }
}
