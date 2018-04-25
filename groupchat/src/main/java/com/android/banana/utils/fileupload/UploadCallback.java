package com.android.banana.utils.fileupload;

import com.android.httprequestlib.BaseRequestHttpName;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by qiaomu on 2017/12/8.
 */

public interface UploadCallback {
    /**
     * @param posInAdapter 被成功上传的文件在列表中的位置，如果{@link FileUploadManager#upload(List, List, BaseRequestHttpName, UploadCallback)
     *                     --------------------------第二个list传NULL,那么posInAdapter 一直是0 }
     * @param curPosition  被成功上传文件的在本次任务中的下标位置
     */
    void onUploaded(int posInAdapter, int curPosition);


    /**
     * @param totalLength  正在被上传的文件的总长度
     * @param progress     正在被上传文件的进度
     * @param curPosition  正在被上传文件的在本次任务中的下标位置
     * @param posInAdapter 正在被上传的文件在列表中的位置，如果{@link FileUploadManager#upload(List, List, BaseRequestHttpName, UploadCallback)
     *                     --------------------------第二个list传NULL,那么posInAdapter 一直是0 }
     */
    void onUpdate(long totalLength, long progress, int posInAdapter, int curPosition);


    /**
     * @param jsonObject   错误信息
     * @param posInAdapter 发生错误的文件在列表中的位置如果{@link FileUploadManager#upload(List, List, BaseRequestHttpName, UploadCallback)
     *                     --------------------------第二个list传NULL,那么posInAdapter 一直是0}
     * @param curPosition  发生错误的文件在本次上传任务中的下表位置
     */
    void onUploadError(JSONObject jsonObject, int posInAdapter, int curPosition);
}
