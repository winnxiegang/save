package com.android.banana.groupchat.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.banana.commlib.bean.BaseOperator;
import com.android.banana.commlib.bean.PaginatorBean;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by zaozao on 2017/6/1.
 */

public class ChatRoomMemberBean implements BaseOperator{
    /**
     * jumpLogin : false
     * nowDate : 2017-11-01 15:29:15
     * paginator : {"items":16,"itemsPerPage":20,"page":1,"pages":1}
     * relationGroupMemberClientSimpleList : [{"groupId":"2339202477004610980035563301","userId":"8201705046405912","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201705046405912&mt=","userName":"回老家了","vip":false},{"groupId":"2339202477004610980035563301","userId":"8201606274175767","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201606274175767&mt=1503976271000","userName":"球球","vip":false},{"groupId":"2339202477004610980035563301","userId":"8201702245805778","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201702245805778&mt=1503976271000","userName":"韶华","vip":false},{"groupId":"2339202477004610980035563301","userId":"8201707256585859","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201707256585859&mt=1503976271000","userName":"情非得已","vip":false},{"groupId":"2339202477004610980035563301","userId":"8201710176845820","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201710176845820&mt=1508207676000","userName":"游客0000000205","vip":false},{"groupId":"2339202477004610980035563301","userId":"8201710226915892","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201710226915892&mt=1508675148000","userName":"地方的是多少","vip":false},{"groupId":"2339202477004610980035563301","userId":"8201511040115772","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201511040115772&mt=1503976271000","userName":"bvbvdcdsdfd","vip":false},{"groupId":"2339202477004610980035563301","userId":"8201704126175774","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201704126175774&mt=1506655355000","userName":"狗包子","vip":false},{"groupId":"2339202477004610980035563301","userId":"8201606224095767","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201606224095767&mt=1503976271000","userName":"皂皂","vip":true},{"groupId":"2339202477004610980035563301","userId":"8201709216705958","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201709216705958&mt=","userName":"Rrrrr","vip":false},{"groupId":"2339202477004610980035563301","userId":"8201510110075819","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201510110075819&mt=1503976271000","userName":"OAP_qb6","vip":false},{"groupId":"2339202477004610980035563301","userId":"8201510160076087","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201510160076087&mt=1503976271000","userName":"占氏家用保姆智能机器人","vip":true},{"groupId":"2339202477004610980035563301","userId":"8201705116435971","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201705116435971&mt=","userName":"单关配佣金1","vip":false},{"groupId":"2339202477004610980035563301","userId":"8201706126496934","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201706126496934&mt=","userName":"苜蓿222","vip":false},{"groupId":"2339202477004610980035563301","userId":"8201610314805889","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201610314805889&mt=1506657285000","userName":"kaishui","vip":false},{"groupId":"2339202477004610980035563301","userId":"8201705126435987","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=8201705126435987&mt=1507517587000","userName":"qiaomu","vip":false}]
     * success : true
     * userRoleInfoSimpleList : [{"levelCode":"GROUP_ADMIN","userId":"qiaomu","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=&mt=","vip":false},{"levelCode":"GROUP_ADMIN","userId":"bvbvdcdsdfd","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=&mt=","vip":false},{"levelCode":"GROUP_ADMIN","userId":"占氏家用保姆智能机器人","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=&mt=","vip":false},{"levelCode":"GROUP_ADMIN","userId":"球球","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=&mt=","vip":false},{"levelCode":"GROUP_ADMIN","userId":"OAP_qb6","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=&mt=","vip":false},{"levelCode":"GROUP_ADMIN","userId":"韶华","userLogoUrl":"http://jchapi3.huored.net/userLogoUrl.htm?userId=&mt=","vip":false}]
     */

    private PaginatorBean paginator;
    private List<GroupMemberSimpleBean> groupMemberClientSimpleList;
    private List<GroupMemberSimpleBean> userRoleInfoSimpleList;
    private GroupMemberSimpleBean userInfoSimple;

    public PaginatorBean getPaginator() {
        return paginator;
    }

    public void setPaginator(PaginatorBean paginator) {
        this.paginator = paginator;
    }

    public GroupMemberSimpleBean getUserInfoSimple() {
        return userInfoSimple;
    }

    public void setUserInfoSimple(GroupMemberSimpleBean userInfoSimple) {
        this.userInfoSimple = userInfoSimple;
    }

    public List<GroupMemberSimpleBean> getGroupMemberClientSimpleList() {
        return groupMemberClientSimpleList;
    }

    public void setGroupMemberClientSimpleList(List<GroupMemberSimpleBean> groupMemberClientSimpleList) {
        this.groupMemberClientSimpleList = groupMemberClientSimpleList;
    }


    public List<GroupMemberSimpleBean> getUserRoleInfoSimpleList() {
        return userRoleInfoSimpleList;
    }

    public void setUserRoleInfoSimpleList(List<GroupMemberSimpleBean> userRoleInfoSimpleList) {
        this.userRoleInfoSimpleList = userRoleInfoSimpleList;
    }

    @Override
    public void operatorData() {
        if(userRoleInfoSimpleList!=null&&userRoleInfoSimpleList.size()>0){
            userRoleInfoSimpleList.get(0).showCutLine = false;
            userRoleInfoSimpleList.get(0).setShowIdentifyTitle(true);
            if(groupMemberClientSimpleList!=null&&groupMemberClientSimpleList.size()>0){
                groupMemberClientSimpleList.get(0).setShowIdentifyTitle(true);
            }
            for (int i = 0; i <userRoleInfoSimpleList.size() ; i++) {
                userRoleInfoSimpleList.get(i).setShowIdentify(true);
            }
        }
        if(groupMemberClientSimpleList!=null&&groupMemberClientSimpleList.size()>0){
            groupMemberClientSimpleList.get(0).showCutLine = false;
        }
    }

    public static class GroupMemberSimpleBean implements Parcelable {

        private int id;
        private String groupId;
        private boolean vip;

        //发言人设置里面用到的一部分
        private String userId;
        private String userLogoUrl;
        private String userName;
        private String loginName;
        private String nickName;
        private String levelCode;

        @Expose
        private boolean selected;
        @Expose
        private boolean showIdentifyTitle;
        @Expose
        private boolean showIdentify;
        @Expose
        private boolean open;
        @Expose
        private boolean goneDelete;//加号和减号不显示删除按钮
        @Expose
        private int resId;
        @Expose
        private boolean fromWeb;
        @Expose
        private boolean haveAddComplete;
        @Expose
        private boolean searchAndDelete;
        @Expose
        public boolean showCutLine = true;

        public boolean isSearchAndDelete() {
            return searchAndDelete;
        }

        public void setSearchAndDelete(boolean searchAndDelete) {
            this.searchAndDelete = searchAndDelete;
        }

        public boolean isHaveAddComplete() {
            return haveAddComplete;
        }

        public void setHaveAddComplete(boolean haveAddComplete) {
            this.haveAddComplete = haveAddComplete;
        }

        public boolean isShowIdentifyTitle() {
            return showIdentifyTitle;
        }

        public void setShowIdentifyTitle(boolean showIdentifyTitle) {
            this.showIdentifyTitle = showIdentifyTitle;
        }

        public String getLoginName() {
            return loginName;
        }

        public boolean isShowIdentify() {
            return showIdentify;
        }

        public void setShowIdentify(boolean showIdentify) {
            this.showIdentify = showIdentify;
        }

        public boolean isGoneDelete() {
            return goneDelete;
        }

        public void setGoneDelete(boolean goneDelete) {
            this.goneDelete = goneDelete;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public void seLoginName(String loginName) {
            this.loginName = loginName;
        }

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }


        public boolean isFromWeb() {
            return fromWeb;
        }

        public void setFromWeb(boolean fromWeb) {
            this.fromWeb = fromWeb;
        }

        public String getLevelCode() {
            return levelCode;
        }

        public void setLevelCode(String levelCode) {
            this.levelCode = levelCode;
        }

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserLogoUrl() {
            return userLogoUrl;
        }

        public void setUserLogoUrl(String userLogoUrl) {
            this.userLogoUrl = userLogoUrl;
        }

        public boolean isVip() {
            return vip;
        }

        public void setVip(boolean vip) {
            this.vip = vip;
        }

        public GroupMemberSimpleBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.groupId);
            dest.writeByte(this.vip ? (byte) 1 : (byte) 0);
            dest.writeString(this.userId);
            dest.writeString(this.userLogoUrl);
            dest.writeString(this.userName);
            dest.writeString(this.loginName);
            dest.writeString(this.nickName);
            dest.writeString(this.levelCode);
            dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
            dest.writeByte(this.showIdentifyTitle ? (byte) 1 : (byte) 0);
            dest.writeByte(this.showIdentify ? (byte) 1 : (byte) 0);
            dest.writeByte(this.open ? (byte) 1 : (byte) 0);
            dest.writeByte(this.goneDelete ? (byte) 1 : (byte) 0);
            dest.writeInt(this.resId);
            dest.writeByte(this.fromWeb ? (byte) 1 : (byte) 0);
            dest.writeByte(this.haveAddComplete ? (byte) 1 : (byte) 0);
            dest.writeByte(this.searchAndDelete ? (byte) 1 : (byte) 0);
            dest.writeByte(this.showCutLine ? (byte) 1 : (byte) 0);
        }

        protected GroupMemberSimpleBean(Parcel in) {
            this.id = in.readInt();
            this.groupId = in.readString();
            this.vip = in.readByte() != 0;
            this.userId = in.readString();
            this.userLogoUrl = in.readString();
            this.userName = in.readString();
            this.loginName = in.readString();
            this.nickName = in.readString();
            this.levelCode = in.readString();
            this.selected = in.readByte() != 0;
            this.showIdentifyTitle = in.readByte() != 0;
            this.showIdentify = in.readByte() != 0;
            this.open = in.readByte() != 0;
            this.goneDelete = in.readByte() != 0;
            this.resId = in.readInt();
            this.fromWeb = in.readByte() != 0;
            this.haveAddComplete = in.readByte() != 0;
            this.searchAndDelete = in.readByte() != 0;
            this.showCutLine = in.readByte() != 0;
        }

        public static final Creator<GroupMemberSimpleBean> CREATOR = new Creator<GroupMemberSimpleBean>() {
            @Override
            public GroupMemberSimpleBean createFromParcel(Parcel source) {
                return new GroupMemberSimpleBean(source);
            }

            @Override
            public GroupMemberSimpleBean[] newArray(int size) {
                return new GroupMemberSimpleBean[size];
            }
        };
    }
}
