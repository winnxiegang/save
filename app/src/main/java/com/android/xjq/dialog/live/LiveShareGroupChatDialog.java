package com.android.xjq.dialog.live;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.xjq.R;
import com.android.xjq.dialog.base.BaseDialog;
import com.android.xjq.dialog.base.ViewHolder;

/**
 * 分享到群聊的弹窗
 *
 * Created by lingjiu on 2018/2/2.
 */

public class LiveShareGroupChatDialog extends BaseDialog {

    public LiveShareGroupChatDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_live_share_group_chat_layout;
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialog dialog) {

    }
}
