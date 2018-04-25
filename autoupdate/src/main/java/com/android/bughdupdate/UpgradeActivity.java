package com.android.bughdupdate;

/**
 * Created by lingjiu on 2016/10/31 9:49.
 */

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

// TODO: 2018/4/24  更新界面查看学习 Bugly Android 应用升级 SDK 使用指南应用升级使用指南 - Bugly 文档
// TODO https://bugly.qq.com/docs/user-guide/instruction-manual-android-upgrade/?v=20180119105842


/**
 * 更新弹窗
 */
public class UpgradeActivity extends Activity {
    private TextView tv;
    private TextView title;
    private TextView version;
    private TextView size;
    private TextView time;
    private TextView content;
    private ImageView cancel;
    private TextView start;
//    private View cutLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upgrade);
        tv = getView(R.id.tv);
        title = getView(R.id.title);
        version = getView(R.id.version);
        size = getView(R.id.size);
        time = getView(R.id.time);
        content = getView(R.id.content);
        cancel = getView(R.id.cancel);
        start = getView(R.id.start);
//        cutLine=getView(R.id.cutLine);
            /*获取下载任务，初始化界面信息*/
        updateBtn(Beta.getStrategyTask());
        //下载进度
//        tv.setText(tv.getText().toString() + Beta.getStrategyTask().getSavedLength() + "");

            /*获取策略信息，初始化界面信息*/
        //更新副标题
        title.setText(title.getText().toString() + Beta.getUpgradeInfo().title);
        //版本
        version.setText(version.getText().toString() + Beta.getUpgradeInfo().versionName);
        //大小
        size.setText(size.getText().toString() + Formatter.formatFileSize(this, Beta.getUpgradeInfo().fileSize) + "");
        //时间
        time.setText(time.getText().toString() + getDateToString(Beta.getUpgradeInfo().publishTime) + "");
        //内容
        content.setText(content.getText().toString() + Beta.getUpgradeInfo().newFeature);

        //强制更新
        if (Beta.getUpgradeInfo().upgradeType == 2) {
            cancel.setVisibility(View.GONE);
//            cutLine.setVisibility(View.GONE);
        }

        /*为下载按钮设置监听*/
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*DownloadTask task = Beta.startDownload();
                updateBtn(task);
                if (task.getStatus() == DownloadTask.DOWNLOADING) {
                    startActivity(new Intent(UpgradeActivity.this, UpdateDownloadActivity.class));
                    finish();
                }*/
                DownloadTask task = Beta.startDownload();
                if (task == null) return;
                updateBtn(task);
                if (task.getStatus() == DownloadTask.DOWNLOADING) {
                    finish();
                }
            }
        });


        /*为取消按钮设置监听*/
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Beta.cancelDownload();
                finish();
            }
        });

            /*注册下载监听，监听下载事件*/
        Beta.registerDownloadListener(new DownloadListener() {
            @Override
            public void onReceive(DownloadTask task) {
                updateBtn(task);
                tv.setText(task.getSavedLength() + "");
            }

            @Override
            public void onCompleted(DownloadTask task) {
                updateBtn(task);
                tv.setText(task.getSavedLength() + "");
            }

            @Override
            public void onFailed(DownloadTask task, int code, String extMsg) {
                updateBtn(task);
                tv.setText("failed");

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

            /*注销下载监听*/
        Beta.unregisterDownloadListener();
    }


    public void updateBtn(DownloadTask task) {

            /*根据下载任务状态设置按钮*/
        switch (task.getStatus()) {
            case DownloadTask.INIT:
            case DownloadTask.DELETED:
            case DownloadTask.FAILED: {
                start.setText("开始下载");
            }
            break;
            case DownloadTask.COMPLETE: {
                start.setText("安装");
            }
            break;
            case DownloadTask.DOWNLOADING: {
                start.setText("暂停");
            }
            break;
            case DownloadTask.PAUSED: {
                start.setText("继续下载");
            }
            break;
        }
    }

    public <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }
}