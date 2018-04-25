package com.android.banana.utils.fileupload;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.httprequestlib.BaseRequestHttpName;
import com.android.httprequestlib.RequestContainer;
import com.android.httprequestlib.okhttp.ProgressRequestBody;
import com.android.library.Utils.LogUtils;
import com.etiennelawlor.imagegallery.library.entity.Photo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaomu on 2017/12/8.
 */

public class FileUploadManager {
    private WrapperHttpHelper uploadHttpHelper;
    private int upLoadingIndex = -1;

    private List<Photo> mFilePathList;
    private List<Integer> mPosInAdapterList;
    private BaseRequestHttpName mHttpName;
    private UploadCallback mUploadCallback;
    private int mFileCount;

    protected FileUploadManager() {
        uploadHttpHelper = new WrapperHttpHelper(new FileUploadHttpListener());
    }

    public static void upload(@NonNull Photo filePath, @Nullable int posInAdapter, @NonNull final BaseRequestHttpName httpName, @Nullable final UploadCallback callback) {
        List<Photo> filePathList = new ArrayList<>();
        filePathList.add(filePath);

        List<Integer> posInAdapterList = new ArrayList<>();
        posInAdapterList.add(posInAdapter);

        upload(filePathList, posInAdapterList, httpName, callback);
    }

    public static void upload(@NonNull final List<Photo> filePathList, @Nullable final List<Integer> posInAdapterList, @NonNull final BaseRequestHttpName httpName, @Nullable final UploadCallback callback) {

        new FileUploadManager().readyUpload(filePathList, posInAdapterList, httpName, callback);
    }

    void readyUpload(@NonNull final List<Photo> filePathList, @Nullable final List<Integer> posInAdapterList, @NonNull final BaseRequestHttpName httpName, @Nullable UploadCallback uploadCallback) {
        mFilePathList = filePathList;
        mPosInAdapterList = posInAdapterList;
        mHttpName = httpName;
        mUploadCallback = uploadCallback;
        mFileCount = filePathList.size();

        if (posInAdapterList != null && posInAdapterList.size() != filePathList.size()) {
            throw new RuntimeException("posInAdapterList 的长度必须跟 filePathList 一致");
        }

        realUpload();
    }

    void realUpload() {

        upLoadingIndex++;
        if (mFilePathList.size() <= 0 || upLoadingIndex >= mFilePathList.size()) {
            release();
            return;
        }

        int curPosition = upLoadingIndex;//当前需要被上传文件的下表
        int posInAdapter = mPosInAdapterList == null ? 0 : mPosInAdapterList.get(upLoadingIndex);//当前文件在列表适配器中的位置
        String filePath = mFilePathList.get(upLoadingIndex).getPath();//当前需要被上传文件的路径

        File file = new File(filePath);
        if (!file.exists()) {
            //上传的时候文件可能被删除,那么跳过，继续上传下一张
            realUpload();
            if (mUploadCallback != null)
                mUploadCallback.onUploadError(null, posInAdapter, curPosition);
            return;
        }

        RequestFormBody formBody = new RequestFormBody(mHttpName, true, true);
        ProgressRequestBody requestBody = new ProgressRequestBody(filePath, posInAdapter, new FileUploadListener(mUploadCallback, curPosition));
        formBody.putFormDataPart("file", file.getName(), requestBody);

        formBody.put("posInAdapter", posInAdapter);
        formBody.put("curPosition", curPosition);
        uploadHttpHelper.startRequest(formBody, true);

    }

    private void release() {
        uploadHttpHelper.onDestroy();
        mHttpName = null;
        mUploadCallback = null;
        upLoadingIndex = -1;
        mFileCount = 0;
    }

    class FileUploadHttpListener implements IHttpResponseListener {

        @Override
        public void onSuccess(RequestContainer request, Object object) {
            int posInAdapter = request.getInt("posInAdapter");
            int curPosition = request.getInt("curPosition");

            JSONObject response = (JSONObject) object;

            LogUtils.e("fileUploadManager: ", response.toString());
            try {
                boolean success = response.getBoolean("success");
                String fileName = response.getString("fileName");

                mFilePathList.get(curPosition).setUploadSuccess(true);
                mFilePathList.get(curPosition).setUploadSuccessFileName(fileName);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //一张图片上传成功了
            if (mUploadCallback != null)
                mUploadCallback.onUploaded(posInAdapter, curPosition);

            //继续下一张

            realUpload();
        }

        @Override
        public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
            int posInAdapter = request.getInt("posInAdapter");
            int curPosition = request.getInt("curPosition");

            if (mUploadCallback != null)
                mUploadCallback.onUploadError(jsonObject, posInAdapter, curPosition);

            //一张图片上传失败了  继续下一张
            realUpload();
        }
    }

    class FileUploadListener implements ProgressRequestBody.ProgressListener {
        private UploadCallback mUploadCallback;
        private int mCurPosition;

        //void update(long totalLength, long progress, boolean ok, int posInAdapter);
        public FileUploadListener(UploadCallback uploadCallback, int curPosition) {
            mUploadCallback = uploadCallback;
            mCurPosition = curPosition;
        }

        @Override
        public void update(long totalLength, long progress, boolean uploadOK, int posInAdapter) {
            if (mUploadCallback == null)
                return;
            mUploadCallback.onUpdate(totalLength, progress, posInAdapter, mCurPosition);
        }
    }
}
