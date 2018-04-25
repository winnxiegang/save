package com.android.banana.commlib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 通用类型，主要用于Json对象里面包含（name,message）
 * Created by zhouyi on 2016/3/4 16:03.
 */
public class NormalObject implements Parcelable {

    private String name;

    private String message;

    private int value;

    public NormalObject(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public NormalObject() {}

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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.message);
        dest.writeInt(this.value);
    }

    protected NormalObject(Parcel in) {
        this.name = in.readString();
        this.message = in.readString();
        this.value = in.readInt();
    }

    public static final Parcelable.Creator<NormalObject> CREATOR = new Parcelable.Creator<NormalObject>() {
        @Override
        public NormalObject createFromParcel(Parcel source) {
            return new NormalObject(source);
        }

        @Override
        public NormalObject[] newArray(int size) {
            return new NormalObject[size];
        }
    };
}
