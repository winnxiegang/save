package com.android.xjq.bean.scheduledetail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhouyi on 2015/12/28 11:12.
 */
public class MessageType implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.message);
    }

    public MessageType() {

    }

    protected MessageType(Parcel in) {
        this.name = in.readString();
        this.message = in.readString();
    }

    public static final Creator<MessageType> CREATOR = new Creator<MessageType>() {
        public MessageType createFromParcel(Parcel source) {
            return new MessageType(source);
        }

        public MessageType[] newArray(int size) {
            return new MessageType[size];
        }
    };
}
