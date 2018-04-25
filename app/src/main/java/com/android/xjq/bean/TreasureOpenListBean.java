package com.android.xjq.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by qiaomu on 2018/3/8.
 * 宝箱打开后返回的礼物列表
 */

public class TreasureOpenListBean {
    @SerializedName("packageEntryStatisticsSimpleList")
    public ArrayList<treasureOpenBean> treasureChestList;

    public static class treasureOpenBean implements Parcelable {

        /**
         * totalAmount : 6
         * objectId : 9
         * objectType : ENTERTAINMENT
         * typeCode : GIFT
         * validityPeriod : 48
         * imageUrl : http://mapi1.xjq.net/giftImage.resource?giftConfigId=9
         */

        public double totalAmount;
        public String objectId;
        public String objectType;
        public String typeCode;   //GOLDCOIN 金锭， GIFTCOIN 礼金 ，GIFT道具
        public int validityPeriod;
        public String imageUrl;
        public String name;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(this.totalAmount);
            dest.writeString(this.objectId);
            dest.writeString(this.objectType);
            dest.writeString(this.typeCode);
            dest.writeInt(this.validityPeriod);
            dest.writeString(this.imageUrl);
            dest.writeString(this.name);
        }

        public treasureOpenBean() {
        }

        protected treasureOpenBean(Parcel in) {
            this.totalAmount = in.readDouble();
            this.objectId = in.readString();
            this.objectType = in.readString();
            this.typeCode = in.readString();
            this.validityPeriod = in.readInt();
            this.imageUrl = in.readString();
            this.name = in.readString();
        }

        public static final Creator<treasureOpenBean> CREATOR = new Creator<treasureOpenBean>() {
            @Override
            public treasureOpenBean createFromParcel(Parcel source) {
                return new treasureOpenBean(source);
            }

            @Override
            public treasureOpenBean[] newArray(int size) {
                return new treasureOpenBean[size];
            }
        };
    }
}
