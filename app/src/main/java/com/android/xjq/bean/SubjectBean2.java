package com.android.xjq.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.banana.commlib.bean.NormalObject;
import com.android.xjq.bean.dynamic.ImpressionDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaomu on 2018/2/7.
 * <p>
 * 香蕉球二期  话题 基础信息对象
 */

public class SubjectBean2 implements Parcelable {
    /**
     * loginName : 1w
     * userId : 8201712158726308
     * userLogoUrl : http://mapi.xjq.net/userLogoUrl.htm?userId=8201712158726308&mt=1513323962000
     * commentOff : false
     * subjectId : 15682864
     * objectId : 0f3d1544-c0e1-bff9-6375-46dcff06f6ff
     * title : 测试mapi创建文章接口
     * objectType : {"name":"NORMAL","message":"普通"}
     * delete : false
     * likeCount : 0
     * replyCount : 1
     * liked : false
     * collectId : 0
     * gmtCreate : 2018-01-30 21:03:04
     * setTop : false
     * transmited : false
     * transmitCount : 2
     * summary : <p>测试mapi创建文章接口</p>
     * filterHtmlSummary : 测试mapi创建文章接口
     */

    public String loginName;
    public String userId;
    public String userLogoUrl;
    public boolean commentOff;
    public String subjectId;
    public String objectId;
    public String title;
    public NormalObject objectType;
    public boolean deleted;
    public int likeCount;
    public int replyCount;
    public boolean liked;
    public int collectId;
    public String gmtCreate;
    public boolean setTop;
    public boolean transmited;
    public int transmitCount;
    public String summary;
    public String filterHtmlSummary;
    public String content;
    public String memo;
    public int channelAreaId;

    public TransmitSubject2 sourceSubjectSimple;
    public SubjectProperties properties;//图片类型
    public List<ImpressionDataBean.ImpressionTagSimple> tagViewDatas;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.loginName);
        dest.writeString(this.userId);
        dest.writeString(this.userLogoUrl);
        dest.writeByte(this.commentOff ? (byte) 1 : (byte) 0);
        dest.writeString(this.subjectId);
        dest.writeString(this.objectId);
        dest.writeString(this.title);
        dest.writeParcelable(this.objectType, flags);
        dest.writeByte(this.deleted ? (byte) 1 : (byte) 0);
        dest.writeInt(this.likeCount);
        dest.writeInt(this.replyCount);
        dest.writeByte(this.liked ? (byte) 1 : (byte) 0);
        dest.writeInt(this.collectId);
        dest.writeString(this.gmtCreate);
        dest.writeByte(this.setTop ? (byte) 1 : (byte) 0);
        dest.writeByte(this.transmited ? (byte) 1 : (byte) 0);
        dest.writeInt(this.transmitCount);
        dest.writeString(this.summary);
        dest.writeString(this.filterHtmlSummary);
        dest.writeString(this.content);
        dest.writeString(this.memo);
        dest.writeParcelable(this.sourceSubjectSimple, flags);
        dest.writeParcelable(this.properties, flags);
        dest.writeList(this.tagViewDatas);
    }

    public SubjectBean2() {
    }

    protected SubjectBean2(Parcel in) {
        this.loginName = in.readString();
        this.userId = in.readString();
        this.userLogoUrl = in.readString();
        this.commentOff = in.readByte() != 0;
        this.subjectId = in.readString();
        this.objectId = in.readString();
        this.title = in.readString();
        this.objectType = in.readParcelable(NormalObject.class.getClassLoader());
        this.deleted = in.readByte() != 0;
        this.likeCount = in.readInt();
        this.replyCount = in.readInt();
        this.liked = in.readByte() != 0;
        this.collectId = in.readInt();
        this.gmtCreate = in.readString();
        this.setTop = in.readByte() != 0;
        this.transmited = in.readByte() != 0;
        this.transmitCount = in.readInt();
        this.summary = in.readString();
        this.filterHtmlSummary = in.readString();
        this.content = in.readString();
        this.memo = in.readString();
        this.sourceSubjectSimple = in.readParcelable(TransmitSubject2.class.getClassLoader());
        this.properties = in.readParcelable(SubjectProperties.class.getClassLoader());
        this.tagViewDatas = new ArrayList<>();
        in.readList(this.tagViewDatas, ImpressionDataBean.ImpressionTagSimple.class.getClassLoader());
    }

    public static final Creator<SubjectBean2> CREATOR = new Creator<SubjectBean2>() {
        @Override
        public SubjectBean2 createFromParcel(Parcel source) {
            return new SubjectBean2(source);
        }

        @Override
        public SubjectBean2[] newArray(int size) {
            return new SubjectBean2[size];
        }
    };
}
