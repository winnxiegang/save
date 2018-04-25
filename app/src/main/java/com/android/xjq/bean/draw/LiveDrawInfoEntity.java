package com.android.xjq.bean.draw;

import com.android.xjq.bean.UserMedalLevelBean;

import java.util.List;

/**
 * Created by lingjiu on 2018/3/7.
 * <p>
 * 具体活动信息实体类
 */

public class LiveDrawInfoEntity {

    /**
     * giftName : 公牛
     * giftUrl : http://mapi1.xjq.net/giftImageUrl.htm?giftConfigId=40103&timestamp=1513062346000
     * issuePrizeItemList : [{"calculatePrizeOrderNumber":0,"id":"4000626974829800000980059961","issueId":"4000626974827800000980051650","prizeItemName":"0909","prizeItemTypeCode":"AVERAGE_PRIZE","prizeItemTypeId":"4000324312128600000000003859","prizeItemTypeMessage":"平均奖","totalAmount":2,"totalCount":2}]
     * jumpLogin : false
     * luckyDrawParticipateConditionSimple : [{"currencyTypeAndPrice":{"GIFT_COIN":1,"POINT_COIN":0.01},"defaultCurrencyType":"GIFT_COIN","id":"4000626974829300000980054420","linkObjectId":"40103","linkObjectType":"GIFT","perCount":1,"perPrice":1,"supportSide":"NEUTRAL"}]
     * nowDate : 2018-03-07 13:44:49
     * patronLoginName : Mickey
     * patronUrl : http://mapi1.xjq.net/userLogoUrl.htm?userId=8201712058726021&timestamp=1520401489318
     * success : true
     * userParticipateCount : 0
     */

    public String giftName;
    public String giftUrl;
    public String nowDate;
    public String patronLoginName;
    public String patronUrl;
    public int userParticipateCount;
    public int participateCount;
    public List<IssuePrizeItemListBean> issuePrizeItemList;
    public List<LuckyDrawParticipateConditionSimpleBean> luckyDrawParticipateConditionSimple;
    public String content;//火箭发射内容
    public String gmtEnd;//火箭结束时间
    //开奖方式  BY_TIME倒计时开奖 , BY_ANCHOR手动开奖
    public String awardType;
    public List<Integer> multipleList;
    //个人勋章列表
    public List<UserMedalLevelBean> userMedalLevelList;

    public @interface AwardType {
        //BY_TIME倒计时开奖
        String BY_TIME = "BY_TIME";
        // BY_ANCHOR手动开奖
        String BY_ANCHOR = "BY_ANCHOR";
    }
}
