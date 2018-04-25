package com.android.xjq.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.banana.commlib.bean.NormalObject;

/**
 * Created by qiaomu on 2018/2/7.
 * <p>
 * 香蕉球二期 话题转发的信息
 */

public class TransmitSubject2 implements Parcelable {


    /**
     * loginName : 帝师大战五五开
     * userId : 8201712058726041
     * userLogoUrl : http://mapi.xjq.net/userLogoUrl.htm?userId=8201712058726041&mt=1512459804000
     * subjectId : 15682853
     * objectId : 193cf617-a978-cd10-13b6-1db306059850
     * title : 测试转发的文章
     * objectType : {"name":"ARTICLE","message":"文章"}
     * delete : false
     * summary : <p>顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风</p>
     * filterHtmlSummary : 顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风耳二二二二二二二二二二二二二二二顺风
     * gmtCreate : 2018-01-29 11:01:51
     */

    public String loginName;
    public String userId;
    public String userLogoUrl;
    public String subjectId;
    public String objectId;
    public String title;
    public NormalObject objectType;
    public String summary;
    public String filterHtmlSummary;
    public String memo;
    public String gmtCreate;
    public boolean deleted;
    public SubjectProperties properties;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.loginName);
        dest.writeString(this.userId);
        dest.writeString(this.userLogoUrl);
        dest.writeString(this.subjectId);
        dest.writeString(this.objectId);
        dest.writeString(this.title);
        dest.writeParcelable(this.objectType, flags);
        dest.writeByte(this.deleted ? (byte) 1 : (byte) 0);
        dest.writeString(this.summary);
        dest.writeString(this.filterHtmlSummary);
        dest.writeString(this.memo);
        dest.writeString(this.gmtCreate);
        dest.writeParcelable(this.properties, flags);
    }

    public TransmitSubject2() {
    }

    protected TransmitSubject2(Parcel in) {
        this.loginName = in.readString();
        this.userId = in.readString();
        this.userLogoUrl = in.readString();
        this.subjectId = in.readString();
        this.objectId = in.readString();
        this.title = in.readString();
        this.objectType = in.readParcelable(NormalObject.class.getClassLoader());
        this.summary = in.readString();
        this.filterHtmlSummary = in.readString();
        this.memo = in.readString();
        this.gmtCreate = in.readString();
        this.deleted = in.readByte() != 0;
        this.properties = in.readParcelable(SubjectProperties.class.getClassLoader());
    }

    public static final Creator<TransmitSubject2> CREATOR = new Creator<TransmitSubject2>() {
        @Override
        public TransmitSubject2 createFromParcel(Parcel source) {
            return new TransmitSubject2(source);
        }

        @Override
        public TransmitSubject2[] newArray(int size) {
            return new TransmitSubject2[size];
        }
    };
}
