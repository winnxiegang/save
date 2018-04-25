package com.android.bughdupdate.updateDialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bughdupdate.R;
import com.android.bughdupdate.updateManager.UpdatePresenter;
import com.android.bughdupdate.updateManager.UpdateView;

/**
 * Created by DaChao on 2017/12/6.
 */
// TODO: 2018/4/24  这个更新界面暂时不适用
public class UpdateDownloadActivity extends AppCompatActivity implements UpdateView {

    private View.OnClickListener onClickListener;

    private ImageView ivClose;
    private ProgressBar progressBar;
    private TextView tvProgress;
    private TextView tvCancel;
    private TextView tvBackground;

    private UpdatePresenter updatePresenter;

    private void init() {

        updatePresenter = new UpdatePresenter(this, new BuglyUpdateRepository());

        onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.tv_cancel) {
                    updatePresenter.cancelUpdate();
                } else if (v.getId() == R.id.tv_background) {
                    showInBackground();
                } else if (v.getId() == R.id.iv_close) {
                    finish();
                }
            }
        };
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_download);
        setFinishOnTouchOutside(false);

        init();

        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(onClickListener);
        progressBar = (ProgressBar) findViewById(R.id.pb_progress);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(onClickListener);
        tvBackground = (TextView) findViewById(R.id.tv_background);
        tvBackground.setOnClickListener(onClickListener);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        updatePresenter.startUpdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updatePresenter.release();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void setNewVersionStr(String versionStr) {

    }

    @Override
    public void setUpdateTimeStr(String updateTimeStr) {

    }

    @Override
    public void setUpdateContent(String updateContent) {

    }

    @Override
    public void setOnStatusChangedListener(OnStatusChangedListener listener) {

    }

    @Override
    public void pauseUpdate() {
        Toast.makeText(this, "已暂停", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUpdateProgress(int progress) {
        progressBar.setProgress(progress);
        tvProgress.setText(progress + "%");
    }

    @Override
    public void showInBackground() {
    }

    @Override
    public void showInForeground() {

    }

    @Override
    public void showView() {

    }

    @Override
    public void cancelView() {
        finish();
    }

    @Override
    public void showFail() {
        Toast.makeText(this, "下载失败，请检查网络", Toast.LENGTH_SHORT).show();
    }
}
