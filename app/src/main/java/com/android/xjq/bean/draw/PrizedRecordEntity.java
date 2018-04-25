package com.android.xjq.bean.draw;

import com.android.library.Utils.LogUtils;
import com.android.xjq.bean.UserMedalLevelBean;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2018/3/8.
 * <p>
 * 中奖记录数据实体类
 */

public class PrizedRecordEntity {

    /**
     * giftUrl : http://mapi.xjq.net/giftImageUrl.htm?giftConfigId=40103&timestamp=1513062346000
     * jumpLogin : false
     * nowDate : 2018-03-08 11:27:50
     * patronLoginName : 超凶的倩姐
     * patronUrl : http://mapi.xjq.net/userLogoUrl.htm?userId=8201711228710459&timestamp=1520479671245
     * patronUserId : 8201711228710459
     * prizeItemAndRecordMap : {"AVERAGE_PRIZE":[{"loginName":"lingjiu02","participateCount":121,"userId":"8201712078726062"},{"loginName":"lingjiu02","participateCount":121,"userId":"8201712078726062"}]}
     * prizeItemList : [{"calculatePrizeOrderNumber":0,"prizeItemName":"广州机票一张","prizeItemTypeCode":"AVERAGE_PRIZE","prizeItemTypeMessage":"平均奖"}]
     * success : true
     */

    public String giftUrl;
    public boolean jumpLogin;
    public String nowDate;
    public String patronLoginName;
    public String patronUrl;
    public String patronUserId;
    public HashMap<String, List<PirzedUserBean>> prizeItemAndRecordMap;
    public List<PrizeItemListBean> prizeItemList;
    //历史记录url
    public String prizeRecordHistoryUrl;
    //个人勋章列表
    public List<UserMedalLevelBean> userMedalLevelList;

    /**
     * @return 获奖名单以及对应获奖项(用于幸运抽奖)
     */
    public List<PirzedUserBean> getPrizedInfoList() {
        List<PirzedUserBean> list = new ArrayList<>();
        if (prizeItemList != null && prizeItemList.size() > 0) {
            for (PrizeItemListBean prizeItemListBean : prizeItemList) {
                if (prizeItemAndRecordMap.containsKey(prizeItemListBean.prizeItemTypeCode)) {
                    List<PirzedUserBean> pirzedUserBeans = prizeItemAndRecordMap.get(prizeItemListBean.prizeItemTypeCode);
                    PirzedUserBean titleBean = new PirzedUserBean();
                    titleBean.title = prizeItemListBean.prizeItemTypeMessage + ":" + prizeItemListBean.prizeItemName;
                    list.add(titleBean);
                    list.addAll(pirzedUserBeans);
                } else {
                    PirzedUserBean titleBean = new PirzedUserBean();
                    titleBean.title = prizeItemListBean.prizeItemTypeMessage + ":" + prizeItemListBean.prizeItemName;
                    titleBean.isNoBodyPrized = true;
                    list.add(titleBean);
                }
            }
        }
        return list;
    }

    /**
     * @return 返回当前中奖名单的userName集合(用于极限手速)
     */
    public List<String> getPrizedNameList() {
        LogUtils.e("ACTIVITY_ISSUE_PRIZED_QUERY", "prizeItemList" + prizeItemList.toString() + "prizeItemAndRecordMap = " + prizeItemAndRecordMap.size());
        List<String> nameList = new ArrayList<>();
        if (prizeItemList != null && prizeItemList.size() > 0) {
            List<PrizedRecordEntity.PirzedUserBean> pirzedUserBeans = prizeItemAndRecordMap.get(prizeItemList.get(0).prizeItemTypeCode);
            for (PirzedUserBean pirzedUserBean : pirzedUserBeans) {
                nameList.add(pirzedUserBean.loginName);
            }
        }

        return nameList;
    }

    public static class PirzedUserBean {

        /**
         * loginName : lingjiu02
         * participateCount : 121
         * userId : 8201712078726062
         */
        public String loginName;
        public int participateCount;
        public String userId;
        @Expose
        public String title;
        @Expose
        public boolean isNoBodyPrized;
    }

    public static class PrizeItemListBean {
        /**
         * calculatePrizeOrderNumber : 0.0
         * prizeItemName : 广州机票一张
         * prizeItemTypeCode : AVERAGE_PRIZE
         * prizeItemTypeMessage : 平均奖
         */

        public double calculatePrizeOrderNumber;
        public String prizeItemName;
        public String prizeItemTypeCode;
        public String prizeItemTypeMessage;
    }
}
