package com.android.xjq.activity.DialogActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.banana.commlib.utils.HhsUtils;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.android.banana.commlib.view.CountdownTextView;
import com.android.library.Utils.LibAppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 当前主播下播后,推荐给当前房间观众的直播间飞机票
 * <p>
 * Created by lingjiu on 2017/8/18.
 */

public class LiveTransferNoticeActivity extends Activity {

    @BindView(R.id.descTv)
    TextView descTv;
    @BindView(R.id.countdownTv)
    CountdownTextView countdownTv;
    @BindView(R.id.contentView)
    RelativeLayout contentView;

    private static final int COUNTDOWN_TIME = 4 * 1000;
    private String channelId;

    @OnClick(R.id.confirmBtn)
    public void confirm() {
        LiveActivity.startLiveActivity(LiveTransferNoticeActivity.this, Integer.valueOf(channelId));
        finish();
    }

    @OnClick({R.id.cancelBtn, R.id.closeIv})
    public void close() {
        finish();
    }

    public static void startLiveTransferNoticeActivity(Activity activity, String channelTitle, String anchorName, String channelId) {
        Intent intent = new Intent();
        intent.setClass(activity, LiveTransferNoticeActivity.class);
        intent.putExtra("channelTitle", channelTitle);
        intent.putExtra("anchorName", anchorName);
        intent.putExtra("channelId", channelId);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_live_transfer_notice);
        ButterKnife.bind(this);
        adjustViewSize();
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        channelId = intent.getStringExtra("channelId");
        String channelTitle = intent.getStringExtra("channelTitle");
        String anchorName = intent.getStringExtra("anchorName");
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(CurLiveInfo.getHostName());
        ssb.append("即将下播\n现将你传送至");
        ssb.append(SpannableStringHelper.changeTextColor(HhsUtils.splitString(anchorName,18), Color.parseColor("#ffe100")));
        ssb.append("的直播间\n");
        ssb.append(SpannableStringHelper.changeTextColor("【" + HhsUtils.splitString(channelTitle,24) + "】", Color.parseColor("#ffe100")));
        ssb.append(",快去听吧~");
        descTv.setText(ssb);

        countdownTv.start(COUNTDOWN_TIME);
        countdownTv.setOnCountdownListener(new CountdownTextView.OnCountdownListener() {
            @Override
            public void countdownDuring(long countdownTime) {
                countdownTv.setText(countdownTime / 1000 + "秒后自动进入");
            }

            @Override
            public void countdownEnd() {
                LiveActivity.startLiveActivity(LiveTransferNoticeActivity.this, Integer.valueOf(channelId));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countdownTv.cancel();
    }

    //直播间横屏的时候要保持竖屏的大小且保证不变形
    private void adjustViewSize() {
        ViewGroup.LayoutParams lp = contentView.getLayoutParams();
        int width = LibAppUtil.getScreenHeight(this) > LibAppUtil.getScreenWidth(this) ? LibAppUtil.getScreenWidth(this) : LibAppUtil.getScreenHeight(this);
        lp.width = width;//宽高可设置具体大小
        lp.height = width * 1176 / 1471;
        contentView.setLayoutParams(lp);
    }
}
