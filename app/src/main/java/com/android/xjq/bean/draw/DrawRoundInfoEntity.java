package com.android.xjq.bean.draw;

import android.text.TextUtils;

import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.view.VerticalScrollTextView2;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by lingjiu on 2018/3/9.
 * 抽奖轮次信息实体类
 */

public class DrawRoundInfoEntity {
    //已中奖的轮次
    public List<LuckyDrawRoundSimple> prizedRoundList;
    //当前轮次
    public LuckyDrawRoundSimple currentRound;
    //是否结束，如果全部抽完 则此字段为true
    public boolean allAllocated;

    public static class LuckyDrawRoundSimple implements VerticalScrollTextView2.IGeneralNotice {
        //	轮次值
        public int roundValue;
        //中奖用户id
        public String prizedUserId;
        //中奖用户名
        public String prizedUserName;
        public String id;
        public NormalObject status;
        public IssuePrizeItemListBean issuePrizeItem;
        @Expose
        private CharSequence text;

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof LuckyDrawRoundSimple) {
                return TextUtils.equals(text, ((LuckyDrawRoundSimple) obj).getText());
            }
            return super.equals(obj);

        }

        @Override
        public CharSequence getText() {
            return text = issuePrizeItem.prizeItemTypeMessage + "获得者" + prizedUserName;
        }
    }
}
