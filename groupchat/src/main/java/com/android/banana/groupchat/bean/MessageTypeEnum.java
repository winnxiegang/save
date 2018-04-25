package com.android.banana.groupchat.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kokuma on 2017/10/31.
 */

public class MessageTypeEnum implements Parcelable{

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

    public MessageTypeEnum() {
    }

    protected MessageTypeEnum(Parcel in) {
        this.name = in.readString();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<MessageTypeEnum> CREATOR = new Parcelable.Creator<MessageTypeEnum>() {
        public MessageTypeEnum createFromParcel(Parcel source) {
            return new MessageTypeEnum(source);
        }

        public MessageTypeEnum[] newArray(int size) {
            return new MessageTypeEnum[size];
        }
    };
}
