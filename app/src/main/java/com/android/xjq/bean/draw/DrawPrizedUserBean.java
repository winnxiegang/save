package com.android.xjq.bean.draw;

import android.text.TextUtils;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.view.VerticalScrollTextView2;
import com.android.xjq.bean.live.BaseOperator;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by lingjiu on 2018/3/7.
 * <p>
 * 当前中奖人信息实体类
 */

public class DrawPrizedUserBean implements BaseOperator {

    /**
     * drawStatus : WAIT_DRAW
     * jumpLogin : false
     * nowDate : 2018-03-07 20:46:41
     * prizeItemName : 广州机票一张
     * success : true
     * userPrizedSimpleList : [{"issuePrizeId":"4000629525332500000980053617","loginName":"lingjiu02","prizedCount":2,"userId":"8201712078726062"}]
     */

    public String drawStatus;
    public boolean jumpLogin;
    public String nowDate;
    public String prizeItemName;
    public boolean success;
    public List<UserPrizedSimpleListBean> userPrizedSimpleList;

    @Override
    public void operatorData() {
        if (userPrizedSimpleList != null && userPrizedSimpleList.size() > 0) {
            for (UserPrizedSimpleListBean userPrizedSimpleListBean : userPrizedSimpleList) {
                userPrizedSimpleListBean.prizeItemName = prizeItemName;
            }
        }
    }

    public class UserPrizedSimpleListBean implements VerticalScrollTextView2.IGeneralNotice {
        /**
         * issuePrizeId : 4000629525332500000980053617
         * loginName : lingjiu02
         * prizedCount : 2
         * userId : 8201712078726062
         */
        public String issuePrizeId;
        public String loginName;
        public String prizedCount;
        public String userId;
        @Expose
        public String prizeItemName;

        @Override
        public CharSequence getText() {
            String userName = TextUtils.equals(userId, LoginInfoHelper.getInstance().getUserId()) ?
                    "您" : loginName;
            return String.format("恭喜!%1$s抢到%2$s  %3$s次",
                    userName, prizeItemName == null ? "" : prizeItemName, prizedCount);
        }
    }
}
