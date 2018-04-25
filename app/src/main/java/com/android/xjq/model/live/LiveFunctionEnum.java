package com.android.xjq.model.live;

import com.android.xjq.R;

/**
 * Created by lingjiu on 2018/2/7.
 */

public enum LiveFunctionEnum {
    LUCK_DRAW("幸运抽奖", R.drawable.icon_lucky_draw),
    GAME_PK("pk互动", R.drawable.icon_game_pk),
    GAME_CHEER("助威", R.drawable.icon_game_cheer),
    COUPON("红包", R.drawable.icon_coupon);

    String message;
    int resId;

    public String getMessage() {
        return message;
    }

    public int getResId() {
        return resId;
    }

    LiveFunctionEnum(String message, int resId) {
        this.message = message;
        this.resId = resId;
    }
}
