package com.android.banana.groupchat.bean;


import com.jl.jczj.im.bean.ChatMsgBody;

/**
 * Created by zaozao on 2017/7/14.
 * 自己进入聊天室，返回更新最后一条数据信息
 */

public class LastMessageBean {

    /**
     * couponNum : 2
     * jumpLogin : false
     * latestMessage : {"bodies":[{"bodySequence":0,"id":"1671970341404610980038171869","parameters":{"amount":"0.04","messageType":"coupon","receiveUserId":"8201705126435987","receiveUserName":"qiaomu","sendUserId":"8201704196270376","sendUserName":"狗包子4号"},"properties":{}}],"gmtCreate":"2017-07-13 12:21:44","gmtModified":"2017-07-13 12:21:44","groupId":"1647501694904610980037516134","id":"1671970340504610980035090168","messageSequence":1671970398003422,"properties":{"medalcodes":"MANAGER"},"sendUserId":"8201704196270376","sendUserLoginName":"狗包子4号","sendUserLogoUrl":"userId=8201704196270376&mt=1498445422000","systemDeleted":false,"systemMessage":false,"typeCode":"COUPON_FETCH_SUCCESS_NOTICE_TEXT","typeId":3,"uniqueId":"10190965_8201705126435987"}
     * latestMessageSequence : 1671970398003422
     * nowDate : 2017-07-14 15:58:14
     * orderValueByUser : 0
     * success : true
     */

    private int couponNum;
    private ChatMsgBody latestMessage;
    private long latestMessageSequence;
    private long orderValueByUser;

    public int getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(int couponNum) {
        this.couponNum = couponNum;
    }


    public ChatMsgBody getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(ChatMsgBody latestMessage) {
        this.latestMessage = latestMessage;
    }

    public long getLatestMessageSequence() {
        return latestMessageSequence;
    }

    public void setLatestMessageSequence(long latestMessageSequence) {
        this.latestMessageSequence = latestMessageSequence;
    }

    public long getOrderValueByUser() {
        return orderValueByUser;
    }

    public void setOrderValueByUser(long orderValueByUser) {
        this.orderValueByUser = orderValueByUser;
    }
}
