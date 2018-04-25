package com.android.xjq.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.banana.commlib.dialog.DialogBase;
import com.android.banana.commlib.view.CountdownTextView;
import com.android.library.Utils.LibAppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 当前主播下播后,推荐给当前房间观众的直播间飞机票
 * <p>
 * Created by lingjiu on 2017/8/17.
 */

public class TransmitsLiveDialog extends DialogBase {

    private Context context;
    @BindView(R.id.descTv)
    TextView descTv;
    @BindView(R.id.countdownTv)
    CountdownTextView countdownTv;

    @OnClick(R.id.confirmBtn)
    public void confirm(){
//        ((LiveActivity) context).switchRoom(toChannelId);
    }
    @OnClick(R.id.cancelBtn)
    public void cancel(){
        dialog.dismiss();
    }
    @OnClick(R.id.closeIv)
    public void close(){
        dialog.dismiss();
    }

    public TransmitsLiveDialog(Context context) {
        super(context, R.layout.dialog_transmis_live, R.style.MyDialog);
        adjustDialogSize(context);
        this.context = context;
        ButterKnife.bind(this, dialog);
        dialog.getWindow().setWindowAnimations(R.style.dialog_anim_bottom);
        initView();
    }

    private void initView() {
        countdownTv.start(3000);
        countdownTv.setOnCountdownListener(new CountdownTextView.OnCountdownListener() {
            @Override
            public void countdownDuring(long countdownTime) {
                countdownTv.setText(countdownTime/1000+"秒后自动进入");
            }

            @Override
            public void countdownEnd() {

            }
        });

    }

    private void adjustDialogSize(Context ctx) {
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        int width = LibAppUtil.getScreenHeight(ctx) > LibAppUtil.getScreenWidth(ctx) ? LibAppUtil.getScreenWidth(ctx) : LibAppUtil.getScreenHeight(ctx);
        lp.width = width;//宽高可设置具体大小
        lp.height = width * 1176 / 1471;
        dialog.getWindow().setAttributes(lp);
    }
}
