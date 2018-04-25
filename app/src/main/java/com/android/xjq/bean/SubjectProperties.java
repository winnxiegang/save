package com.android.xjq.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by qiaomu on 2017/12/4.
 * 话题 类型元素属性
 */

public class SubjectProperties implements Parcelable {
    public String videoFirstFrameImageUrl;//视频第一帧
    public String videoUrl;//视频

    public String giftUrl;//打赏的礼物url
    public String channelId;//: "100016",
    public String channelTitle;//: "火箭VS马刺"

    public String basketBallRaceId;
    public String footBallRaceId;
    public String subjectFirstCardType;

    public ArrayList<String> smallImageUrl;
    public ArrayList<String> midImageUrl;


    public String getBaseketBallRaceId() {
        return basketBallRaceId;
    }

    public String getFootballBallRaceId() {
        return footBallRaceId;
    }

    public String getSubjectFirstCardType() {
        return subjectFirstCardType;
    }

    public ArrayList<String> getSmallImageUrl() {
        return smallImageUrl;
    }

    public ArrayList<String> getMidImageUrl() {
        return midImageUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.videoFirstFrameImageUrl);
        dest.writeString(this.videoUrl);
        dest.writeString(this.channelId);
        dest.writeString(this.channelTitle);
        dest.writeString(this.basketBallRaceId);
        dest.writeString(this.footBallRaceId);
        dest.writeString(this.subjectFirstCardType);
        dest.writeStringList(this.smallImageUrl);
        dest.writeStringList(this.midImageUrl);
    }

    public SubjectProperties() {
    }

    protected SubjectProperties(Parcel in) {
        this.videoFirstFrameImageUrl = in.readString();
        this.videoUrl = in.readString();
        this.channelId = in.readString();
        this.channelTitle = in.readString();
        this.basketBallRaceId = in.readString();
        this.footBallRaceId = in.readString();
        this.subjectFirstCardType = in.readString();
        this.smallImageUrl = in.createStringArrayList();
        this.midImageUrl = in.createStringArrayList();
    }

    public static final Parcelable.Creator<SubjectProperties> CREATOR = new Parcelable.Creator<SubjectProperties>() {
        @Override
        public SubjectProperties createFromParcel(Parcel source) {
            return new SubjectProperties(source);
        }

        @Override
        public SubjectProperties[] newArray(int size) {
            return new SubjectProperties[size];
        }
    };
}
