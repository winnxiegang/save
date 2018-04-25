package com.android.xjq.fragment.input;

import com.android.banana.commlib.emoji.EmojBean;
import com.android.xjq.bean.medal.UserMedalBean;

/**
 * Created by lingjiu on 2017/9/12.
 */

public interface InputCallback {
    void onEmojAdd(EmojBean emojBean);

    void onEmojDelete();

    void onBubbleSelected(String fontColor);

    void onFansMedalSelected(UserMedalBean userMedalBean);
}
