package com.android.xjq.bean.draw;

import android.text.TextUtils;

import com.android.banana.commlib.bean.NormalObject;

import java.util.List;

/**
 * Created by lingjiu on 2018/3/7.
 * <p>
 * 当前活动信息实体类
 */

public class DrawIssueEntity {

    /**
     * issueSimple : [{"activityCode":"HAND_SPEED","activityMessage":"极限手速","drawStatus":{"message":"等待抽奖","name":"WAIT_DRAW"},"gmtEndDraw":"2018-03-07 13:46:21","gmtEndParticipate":"2018-03-07 13:46:21","gmtStartDraw":"2018-03-07 13:45:51","gmtStartParticipate":"2018-03-07 13:45:51","id":"4000626974827800000980051650","participateStatus":{"message":"参与中","name":"PARTICIPATING"},"status":{"message":"正常","name":"NORMAL"}}]
     * jumpLogin : false
     * nowDate : 2018-03-07 13:42:30
     * success : true
     */

    public boolean jumpLogin;
    public String nowDate;
    public boolean success;
    public List<IssueSimpleBean> issueSimple;


    public static class IssueSimpleBean {
        /**
         * activityCode : HAND_SPEED
         * activityMessage : 极限手速
         * drawStatus : {"message":"等待抽奖","name":"WAIT_DRAW"}
         * gmtEndDraw : 2018-03-07 13:46:21
         * gmtEndParticipate : 2018-03-07 13:46:21
         * gmtStartDraw : 2018-03-07 13:45:51
         * gmtStartParticipate : 2018-03-07 13:45:51
         * id : 4000626974827800000980051650
         * participateStatus : {"message":"参与中","name":"PARTICIPATING"}
         * status : {"message":"正常","name":"NORMAL"}
         */
        public String activityCode;
        public String activityMessage;
        public NormalObject drawStatus;
        public String gmtEndDraw;
        public String gmtEndParticipate;
        public String gmtStartDraw;
        public String gmtStartParticipate;
        public String id;
        public NormalObject participateStatus;
        public NormalObject status;
        public String awardType;

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof IssueSimpleBean)
                return TextUtils.equals(id, ((IssueSimpleBean) obj).id);
            return super.equals(obj);
        }
    }
}
