package com.android.banana.groupchat.bean;

import com.android.banana.commlib.bean.NormalObject;

/**
 * Created by zaozao on 2017/10/31.
 */

public class NewGroupBean {


    /**
     * success : true
     * nowDate : 2017-11-01 10:33:44
     * jumpLogin : false
     * groupChatSimple : {"id":"2614574153300200000007271424","name":"测试家族1","creatorName":"伊莎贝尔狗剩","userCount":3,"joined":false,"applied":false,"typeCode":{"name":"FAMILY","message":"家族"},"groupLogoUrl":"http://jchapi3.huored.net/groupLogo.resource?ownerId=2614574153300200000007271424&ownerType=GROUP&logoType=GROUP&sizeType=LARGE&timestamp=1509503625025"}
     */

    private boolean success;
    private String nowDate;
    private boolean jumpLogin;
    private GroupChatSimpleBean groupChatSimple;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }

    public GroupChatSimpleBean getGroupChatSimple() {
        return groupChatSimple;
    }

    public void setGroupChatSimple(GroupChatSimpleBean groupChatSimple) {
        this.groupChatSimple = groupChatSimple;
    }

    public static class GroupChatSimpleBean {
        /**
         * id : 2614574153300200000007271424
         * name : 测试家族1
         * creatorName : 伊莎贝尔狗剩
         * userCount : 3
         * joined : false
         * applied : false
         * typeCode : {"name":"FAMILY","message":"家族"}
         * groupLogoUrl : http://jchapi3.huored.net/groupLogo.resource?ownerId=2614574153300200000007271424&ownerType=GROUP&logoType=GROUP&sizeType=LARGE&timestamp=1509503625025
         */

        private String id;
        private String memo;
        private String name;
        private String creatorName;
        private String groupId;
        private int userCount;
        private boolean joined;
        private boolean applied;
        private NormalObject typeCode;
        private String groupLogoUrl;

        public String getId() {
            return id;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }

        public int getUserCount() {
            return userCount;
        }

        public void setUserCount(int userCount) {
            this.userCount = userCount;
        }

        public boolean isJoined() {
            return joined;
        }

        public void setJoined(boolean joined) {
            this.joined = joined;
        }

        public boolean isApplied() {
            return applied;
        }

        public void setApplied(boolean applied) {
            this.applied = applied;
        }


        public String getGroupLogoUrl() {
            return groupLogoUrl;
        }

        public void setGroupLogoUrl(String groupLogoUrl) {
            this.groupLogoUrl = groupLogoUrl;
        }

        public NormalObject getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(NormalObject typeCode) {
            this.typeCode = typeCode;
        }
    }
}
