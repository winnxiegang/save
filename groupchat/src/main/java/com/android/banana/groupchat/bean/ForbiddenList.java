package com.android.banana.groupchat.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by qiaomu on 2017/6/7.
 */

public class ForbiddenList implements Parcelable {
    public ArrayList<UserBean> userForbiddenActionList;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.userForbiddenActionList);
    }

    public ForbiddenList() {
    }

    protected ForbiddenList(Parcel in) {
        this.userForbiddenActionList = in.createTypedArrayList(UserBean.CREATOR);
    }

    public static final Creator<ForbiddenList> CREATOR = new Creator<ForbiddenList>() {
        @Override
        public ForbiddenList createFromParcel(Parcel source) {
            return new ForbiddenList(source);
        }

        @Override
        public ForbiddenList[] newArray(int size) {
            return new ForbiddenList[size];
        }
    };


    public static class UserBean implements Parcelable {

        /**
         * loginName: "我是陈信宏",
         * userLogoUrl: "http://jchapi.huored.net/userLogo.resource?userId=8201611154825777&mt=1480055286000",
         * vip: false,
         * id: 981106,
         * userId: "8201611154825777"
         */
        private String id;

        private String userId;

        private boolean vip;

        private String userLogoUrl;

        private String loginName;

        @Expose
        private boolean follow;

        @Expose
        private boolean tempFollowed;

        @Expose
        private boolean tempEachFollowed;

        public boolean isTempFollowed() {
            return tempFollowed;
        }

        public boolean isTempEachFollowed() {
            return tempEachFollowed;
        }

        public void setTempEachFollowed(boolean tempEachFollowed) {
            this.tempEachFollowed = tempEachFollowed;
        }

        public void setTempFollowed(boolean tempFollowed) {
            this.tempFollowed = tempFollowed;
        }

        public boolean isFollow() {
            return follow;
        }

        public void setFollow(boolean follow) {
            this.follow = follow;
        }

        public boolean isVip() {
            return vip;
        }

        public void setVip(boolean vip) {
            this.vip = vip;
        }

        public String getUserLogoUrl() {
            return userLogoUrl;
        }

        public void setUserLogoUrl(String userLogoUrl) {
            this.userLogoUrl = userLogoUrl;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.userId);
            dest.writeByte(this.vip ? (byte) 1 : (byte) 0);
            dest.writeString(this.userLogoUrl);
            dest.writeString(this.loginName);
            dest.writeByte(this.follow ? (byte) 1 : (byte) 0);
            dest.writeByte(this.tempFollowed ? (byte) 1 : (byte) 0);
            dest.writeByte(this.tempEachFollowed ? (byte) 1 : (byte) 0);
        }

        public UserBean() {
        }

        protected UserBean(Parcel in) {
            this.id = in.readString();
            this.userId = in.readString();
            this.vip = in.readByte() != 0;
            this.userLogoUrl = in.readString();
            this.loginName = in.readString();
            this.follow = in.readByte() != 0;
            this.tempFollowed = in.readByte() != 0;
            this.tempEachFollowed = in.readByte() != 0;
        }

        public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
            @Override
            public UserBean createFromParcel(Parcel source) {
                return new UserBean(source);
            }

            @Override
            public UserBean[] newArray(int size) {
                return new UserBean[size];
            }
        };
    }

}
