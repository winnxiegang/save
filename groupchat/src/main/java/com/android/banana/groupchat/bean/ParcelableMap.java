package com.android.banana.groupchat.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by qiaomu on 2017/5/27.
 *
 * 因为 HashMap不支持Parcelable
 *
 * 所以 bundle传递不过去
 *
 * 利用这个封装就可以了
 */

public class ParcelableMap implements Parcelable,Serializable {
    public HashMap pracMap;

    public ParcelableMap(HashMap pracMap) {
        this.pracMap = pracMap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.pracMap);
    }

    public ParcelableMap() {
    }

    protected ParcelableMap(Parcel in) {
        this.pracMap = (HashMap<Object, Object>) in.readSerializable();
    }

    public static final Parcelable.Creator<ParcelableMap> CREATOR = new Parcelable.Creator<ParcelableMap>() {
        @Override
        public ParcelableMap createFromParcel(Parcel source) {
            return new ParcelableMap(source);
        }

        @Override
        public ParcelableMap[] newArray(int size) {
            return new ParcelableMap[size];
        }
    };
}
