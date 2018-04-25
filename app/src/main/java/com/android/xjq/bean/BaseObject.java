package com.android.xjq.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhouyi on 2017/1/14 14:53.
 */

public class BaseObject implements Parcelable {


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public BaseObject() {
    }

    protected BaseObject(Parcel in) {
    }

    public static final Creator<BaseObject> CREATOR = new Creator<BaseObject>() {
        @Override
        public BaseObject createFromParcel(Parcel source) {
            return new BaseObject(source);
        }

        @Override
        public BaseObject[] newArray(int size) {
            return new BaseObject[size];
        }
    };
}
