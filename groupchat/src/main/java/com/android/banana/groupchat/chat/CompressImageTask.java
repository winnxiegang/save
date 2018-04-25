package com.android.banana.groupchat.chat;

import android.os.AsyncTask;

import com.android.banana.commlib.utils.CompressImageUtil;

/**
 * Created by zaozao on 2018/3/16.
 */

public class CompressImageTask extends AsyncTask<String, Void, String> {
    private int mDstWidth;
    private int mDstHeight;
    private CompressSuccessListener successListener;

    public CompressImageTask(int dstWidth, int dstHeight,CompressSuccessListener successListener) {
        mDstWidth = dstWidth;
        mDstHeight = dstHeight;
        this.successListener  = successListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        int result = CompressImageUtil.compress(strings[0], strings[1], mDstWidth, mDstHeight);
        return result == 0 ? strings[0] : strings[1];
    }

    @Override
    protected void onPostExecute(String targetPath) {
        successListener.compressSuccess();

    }
}