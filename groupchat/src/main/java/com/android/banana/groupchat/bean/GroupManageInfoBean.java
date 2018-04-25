package com.android.banana.groupchat.bean;

import com.android.banana.groupchat.bean.ChatRoomMemberBean.GroupMemberSimpleBean;

import java.util.List;

/**
 * Created by kokuma on 2017/10/31.
 */

public class GroupManageInfoBean {
    private String nickName;

    /**
     * 群号
     */
    private String groupNo;

    /**
     * 群聊ID
     */
    private String groupChatId;

    /**
     * 群聊Code
     */
    private String groupCode;
    /**
     * 群名称
     */
    private String groupChatName;

    /**
     * 公告
     */
    private String notice;

    /**
     * 人数
     */
    private long count;

    /**
     * 人员等级编码
     */
    private String userRoleCode;
    /**
     * 权限类型
     */
    private MessageTypeEnum permissionType;
    /**
     * 消息是否置顶
     */
    private boolean sticked;

    /**
     * 允许管理员修改主题
     */
    private boolean adminModifyThemeType;

    /**
     * 开启主题模式
     */
    private boolean openThemeMode;
    /**
     * 成员列表列
     */
    private List<GroupMemberSimpleBean> userInfoList;


    private boolean success;


    //备用（允许管理员修改主题）
    private Boolean allowedModifyThemeType;
    //简介
    private String memo;
    //群主名字
    private String createUserName;
    //群头像
    private String groupLogoURL;
    //群战袍名称
    private String coatArmorName;
    //群战袍申请状态
    private MedalLabelApplyStatusBean coatArmorApplyStatus;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCoatArmorName() {
        return coatArmorName;
    }

    public void setCoatArmorName(String coatArmorName) {
        this.coatArmorName = coatArmorName;
    }

    public MedalLabelApplyStatusBean getCoatArmorApplyStatus() {
        return coatArmorApplyStatus;
    }

    public void setCoatArmorApplyStatus(MedalLabelApplyStatusBean coatArmorApplyStatus) {
        this.coatArmorApplyStatus = coatArmorApplyStatus;
    }

    public boolean isAdminModifyThemeType() {
        return adminModifyThemeType;
    }

    public void setAdminModifyThemeType(boolean adminModifyThemeType) {
        this.adminModifyThemeType = adminModifyThemeType;
    }

    public List<GroupMemberSimpleBean> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<GroupMemberSimpleBean> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public String getGroupLogoURL() {
        return groupLogoURL;
    }

    public void setGroupLogoURL(String groupLogoURL) {
        this.groupLogoURL = groupLogoURL;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    public Boolean getAllowedModifyThemeType() {
        return allowedModifyThemeType;
    }

    public void setAllowedModifyThemeType(Boolean allowedModifyThemeType) {
        this.allowedModifyThemeType = allowedModifyThemeType;
    }


    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }


    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getGroupChatId() {
        return groupChatId;
    }

    public void setGroupChatId(String groupChatId) {
        this.groupChatId = groupChatId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupChatName() {
        return groupChatName;
    }

    public void setGroupChatName(String groupChatName) {
        this.groupChatName = groupChatName;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getUserRoleCode() {
        return userRoleCode;
    }

    public void setUserRoleCode(String userRoleCode) {
        this.userRoleCode = userRoleCode;
    }

    public MessageTypeEnum getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(MessageTypeEnum permissionType) {
        this.permissionType = permissionType;
    }

    public boolean isSticked() {
        return sticked;
    }

    public void setSticked(boolean sticked) {
        this.sticked = sticked;
    }


    public boolean isOpenThemeMode() {
        return openThemeMode;
    }

    public void setOpenThemeMode(boolean openThemeMode) {
        this.openThemeMode = openThemeMode;
    }


    public class MedalLabelApplyStatusBean {

        private String name;

        private String message;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


}
