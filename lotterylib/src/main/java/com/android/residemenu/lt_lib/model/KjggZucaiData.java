package com.android.residemenu.lt_lib.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhouyi on 2015/10/19 21:04.
 */
public class KjggZucaiData implements Parcelable {

    private String[] result;

    private String issueNo;

    public String getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo;
    }


    public String[] getResult() {
        return result;
    }

    public void setResult(String[] result) {
        this.result = result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(this.result);
        dest.writeString(this.issueNo);
    }

    public KjggZucaiData() {
    }

    protected KjggZucaiData(Parcel in) {
        this.result = in.createStringArray();
        this.issueNo = in.readString();
    }

    public static final Creator<KjggZucaiData> CREATOR = new Creator<KjggZucaiData>() {
        public KjggZucaiData createFromParcel(Parcel source) {
            return new KjggZucaiData(source);
        }

        public KjggZucaiData[] newArray(int size) {
            return new KjggZucaiData[size];
        }
    };
}
