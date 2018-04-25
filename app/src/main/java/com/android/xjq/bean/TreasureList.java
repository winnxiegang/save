package com.android.xjq.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.banana.commlib.bean.PaginatorBean;

import java.util.ArrayList;

/**
 * Created by qiaomu on 2018/3/8.
 * <p>
 * 包裹中的宝箱
 */

public class TreasureList {
    public String nowDate;
    public PaginatorBean paginator;
    public ArrayList<Treasure> treasureChestList;
    public ArrayList<Treasure> userGiftList;

    public static class Treasure implements Parcelable {
        /**
         * id
         * currentTotalCount : 1.5
         * title : 黑铁宝箱
         * subTypeId : 4000583256425107840980053226
         * subTypeCode : BLACK_IRON_TREASURE_CHEST
         * imageUrl : http://mapi1.xjq.net/packageSubTypeImage.resource?code=BLACK_IRON_TREASURE_CHEST
         */
        //宝箱
        public String id;
        public long currentTotalCount;
        public String title;
        public String subTypeId;
        public String subTypeCode;
        public String imageUrl;


        //道具
        //public String id;//: "2",
        public String userId;//: "8201711278725840",
        public String giftConfigId;//: 9,
        public String giftName;//: "稳如狗",
        public String giftImageUrl;//: "http://mapi1.xjq.net/giftImage.resource?giftConfigId=9",
        public String gmtExpired;
        //public int currentTotalCount;//: 100

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(this.currentTotalCount);
            dest.writeString(this.title);
            dest.writeString(this.subTypeId);
            dest.writeString(this.subTypeCode);
            dest.writeString(this.imageUrl);
            dest.writeString(this.id);
            dest.writeString(this.userId);
            dest.writeString(this.giftConfigId);
            dest.writeString(this.giftName);
            dest.writeString(this.giftImageUrl);
        }

        public Treasure() {
        }

        protected Treasure(Parcel in) {
            this.currentTotalCount = in.readLong();
            this.title = in.readString();
            this.subTypeId = in.readString();
            this.subTypeCode = in.readString();
            this.imageUrl = in.readString();
            this.id = in.readString();
            this.userId = in.readString();
            this.giftConfigId = in.readString();
            this.giftName = in.readString();
            this.giftImageUrl = in.readString();
        }

        public static final Creator<Treasure> CREATOR = new Creator<Treasure>() {
            @Override
            public Treasure createFromParcel(Parcel source) {
                return new Treasure(source);
            }

            @Override
            public Treasure[] newArray(int size) {
                return new Treasure[size];
            }
        };
    }
}
