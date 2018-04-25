package com.android.xjq.adapter.live;

import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.xjq.R;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.model.live.LiveCommentMessageTypeEnum;

/**
 * Created by lingjiu on 2017/7/5.
 */

public class LiveCommentMultiType implements MultiTypeSupport<LiveCommentBean> {

    @Override
    public int getTypeLayoutRes(LiveCommentBean data, int pos) {
        int layoutRes = R.layout.item_live_comment_message;
        switch (LiveCommentMessageTypeEnum.safeValueOf(data.getType())) {
            case DECREE_USER_PRIZED_TEXT:
            case TEXT:
            case NORMAL:
            case SPECIAL_EFFECT_ENTER_NOTICE:
            case GIFTCORE_GIFT_GIVE_TEXT:
            case COUPON_CREATE_SUCCESS_NOTICE_TEXT:
                layoutRes = R.layout.item_live_comment_message;
                break;
           /* case COUPON_CREATE_SUCCESS_NOTICE_TEXT:
                layoutRes = R.layout.item_live_comment_coupon_message;
                break;*/
        }

        return layoutRes;
    }
}
