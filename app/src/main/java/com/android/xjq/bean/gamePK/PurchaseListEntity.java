package com.android.xjq.bean.gamePK;

import com.android.xjq.bean.live.BaseOperator;
import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2018/3/5.
 */

public class PurchaseListEntity implements BaseOperator {


    /**
     * jumpLogin : false
     * nowDate : 2018-03-05 15:38:10
     * success : true
     * userIdMultipleMap : {"8201712078726062":32}
     * userInfos : [{"attentionNum":0,"fansNum":0,"fidleMedal":{"currentMedalLevelConfigCode":"Lv1","currentMedalLevelConfigValue":100,"medalConfigCode":"CONSUME_MEDAL","userId":"8201712078726062"},"focus":false,"globalGradeMedal":{"currentMedalLevelConfigCode":"Lv1","currentMedalLevelConfigValue":100,"medalConfigCode":"GLOBAL_GRADE_MEDAL","userId":"8201712078726062"},"userId":"8201712078726062","userLogoUrl":"http://mapi1.xjq.net/userLogoUrl.htm?userId=8201712078726062&timestamp=1512628771000","userName":"lingjiu02"}]
     */

    public boolean success;
    public HashMap<String, Integer> userIdMultipleMap;
    public List<PurchaseInfoBean> userInfos;

    @Override
    public void operatorData() {
        if (userIdMultipleMap != null && userIdMultipleMap.size() > 0 &&
                userInfos != null && userInfos.size() > 0) {
            for (PurchaseInfoBean userInfo : userInfos) {
                if (userIdMultipleMap.containsKey(userInfo.userId)) {
                    userInfo.multiple = userIdMultipleMap.get(userInfo.userId);
                }
            }
        }
    }

    public static class PurchaseInfoBean {
        /**
         * attentionNum : 0
         * fansNum : 0
         * fidleMedal : {"currentMedalLevelConfigCode":"Lv1","currentMedalLevelConfigValue":100,"medalConfigCode":"CONSUME_MEDAL","userId":"8201712078726062"}
         * focus : false
         * globalGradeMedal : {"currentMedalLevelConfigCode":"Lv1","currentMedalLevelConfigValue":100,"medalConfigCode":"GLOBAL_GRADE_MEDAL","userId":"8201712078726062"}
         * userId : 8201712078726062
         * userLogoUrl : http://mapi1.xjq.net/userLogoUrl.htm?userId=8201712078726062&timestamp=1512628771000
         * userName : lingjiu02
         */

        public int attentionNum;
        public int fansNum;
        public MedalInfoBean fidleMedal;
        public boolean focus;
        public MedalInfoBean globalGradeMedal;
        public String userId;
        public String userLogoUrl;
        public String userName;
        @Expose
        public int multiple;

        public static class MedalInfoBean {
            /**
             * currentMedalLevelConfigCode : Lv1
             * currentMedalLevelConfigValue : 100
             * medalConfigCode : CONSUME_MEDAL
             * userId : 8201712078726062
             */

            public String currentMedalLevelConfigCode;
            public int currentMedalLevelConfigValue;
            public String medalConfigCode;
            public String userId;
        }
    }
}
