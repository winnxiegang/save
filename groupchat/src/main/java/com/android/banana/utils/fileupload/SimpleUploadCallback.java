package com.android.banana.utils.fileupload;

import org.json.JSONObject;

/**
 * Created by qiaomu on 2017/12/8.
 * 文件上传接口适配类，方法可选择性复写
 */

public class SimpleUploadCallback implements UploadCallback {
    @Override
    public void onUploaded(int posInAdapter, int curPosition) {

    }

    @Override
    public void onUpdate(long totalLength, long progress, int posInAdapter, int curPosition) {

    }

    @Override
    public void onUploadError(JSONObject jsonObject, int posInAdapter, int curPosition) {

    }
}
