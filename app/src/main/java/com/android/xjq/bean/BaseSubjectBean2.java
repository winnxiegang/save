package com.android.xjq.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.banana.commlib.bean.NormalObject;
import com.android.xjq.bean.dynamic.ImpressionDataBean;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaomu on 2018/2/7.
 * 香蕉球二期对象 包含话题信息 转发信息
 */

public class BaseSubjectBean2 implements Parcelable {


    /**
     * id : 34960558
     * userId : 8201712158726308
     * gmtCreate : 2018-01-30 21:03:04
     * <p>
     * <p>
     * subject : {"loginName":"1w","userId":"8201712158726308","userLogoUrl":"http://mapi.xjq.net/userLogoUrl.htm?userId=8201712158726308&mt=1513323962000","commentOff":false,"subjectId":"15682864","objectId":"0f3d1544-c0e1-bff9-6375-46dcff06f6ff","title":"测试mapi创建文章接口","objectType":{"name":"NORMAL","message":"普通"},"delete":false,"likeCount":0,"replyCount":1,"liked":false,"collectId":0,"gmtCreate":"2018-01-30 21:03:04","setTop":false,"transmited":false,"transmitCount":2,"summary":"<p>测试mapi创建文章接口<\/p>","filterHtmlSummary":"测试mapi创建文章接口"}
     * <p>
     * <p>
     * objectType : {"name":"SUBJECT","message":"话题"}
     * objectSubType : {"name":"NORMAL","message":"普通话题"}
     * mainType : {"name":"SUBJECT","message":"话题"}
     */

    public String userId;
    public String id;
    public boolean inChannelArea;//: false,
    public int channelAreaId;//: 0,
    public String gmtCreate;

    public SubjectBean2 subject;

    public NormalObject mainType;
    public NormalObject objectType;
    public NormalObject objectSubType;

    public TransmitSubject2 sourceSubjectSimple;
    public SubjectProperties properties;//图片类型
    public List<UserMedalLevelBean> userMedalLevelList;//动态标签


    @Expose
    public boolean showHotIcon = false;
    @Expose
    public boolean showNewsDynamicIcon = false;


    //---------------下面一些属性是SubjectBean2里面的内容   有些接口直接返回，有些是以subject对象返回的
    public String loginName;
    //    public String userId;
    public String userLogoUrl;
    public boolean commentOff;
    public String subjectId;
    public String objectId;
    public String title;
    //    public NormalObject objectType;
    public boolean deleted;
    public int likeCount;
    public int replyCount;
    public boolean liked;
    public int collectId;
    public boolean setTop;
    public boolean transmited;
    public int transmitCount;
    public String summary;
    public String filterHtmlSummary;
    public String memo;
    public List<ImpressionDataBean.ImpressionTagSimple> tagViewDatas;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.id);
        dest.writeString(this.gmtCreate);
        dest.writeParcelable(this.subject, flags);
        dest.writeParcelable(this.mainType, flags);
        dest.writeParcelable(this.objectType, flags);
        dest.writeParcelable(this.objectSubType, flags);
        dest.writeParcelable(this.sourceSubjectSimple, flags);
        dest.writeParcelable(this.properties, flags);
        dest.writeList(this.userMedalLevelList);
        dest.writeByte(this.showHotIcon ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showNewsDynamicIcon ? (byte) 1 : (byte) 0);
        dest.writeString(this.loginName);
        dest.writeString(this.userLogoUrl);
        dest.writeByte(this.commentOff ? (byte) 1 : (byte) 0);
        dest.writeString(this.subjectId);
        dest.writeString(this.objectId);
        dest.writeString(this.title);
        dest.writeByte(this.deleted ? (byte) 1 : (byte) 0);
        dest.writeInt(this.likeCount);
        dest.writeInt(this.replyCount);
        dest.writeByte(this.liked ? (byte) 1 : (byte) 0);
        dest.writeInt(this.collectId);
        dest.writeByte(this.setTop ? (byte) 1 : (byte) 0);
        dest.writeByte(this.transmited ? (byte) 1 : (byte) 0);
        dest.writeInt(this.transmitCount);
        dest.writeString(this.summary);
        dest.writeString(this.filterHtmlSummary);
        dest.writeString(this.memo);
        dest.writeList(this.tagViewDatas);
    }

    public BaseSubjectBean2() {
    }

    protected BaseSubjectBean2(Parcel in) {
        this.userId = in.readString();
        this.id = in.readString();
        this.gmtCreate = in.readString();
        this.subject = in.readParcelable(SubjectBean2.class.getClassLoader());
        this.mainType = in.readParcelable(NormalObject.class.getClassLoader());
        this.objectType = in.readParcelable(NormalObject.class.getClassLoader());
        this.objectSubType = in.readParcelable(NormalObject.class.getClassLoader());
        this.sourceSubjectSimple = in.readParcelable(TransmitSubject2.class.getClassLoader());
        this.properties = in.readParcelable(SubjectProperties.class.getClassLoader());
        this.userMedalLevelList = new ArrayList<>();
        in.readList(this.userMedalLevelList, UserMedalLevelBean.class.getClassLoader());
        this.showHotIcon = in.readByte() != 0;
        this.showNewsDynamicIcon = in.readByte() != 0;
        this.loginName = in.readString();
        this.userLogoUrl = in.readString();
        this.commentOff = in.readByte() != 0;
        this.subjectId = in.readString();
        this.objectId = in.readString();
        this.title = in.readString();
        this.deleted = in.readByte() != 0;
        this.likeCount = in.readInt();
        this.replyCount = in.readInt();
        this.liked = in.readByte() != 0;
        this.collectId = in.readInt();
        this.setTop = in.readByte() != 0;
        this.transmited = in.readByte() != 0;
        this.transmitCount = in.readInt();
        this.summary = in.readString();
        this.filterHtmlSummary = in.readString();
        this.memo = in.readString();
        this.tagViewDatas = new ArrayList<>();
        in.readList(this.tagViewDatas, ImpressionDataBean.ImpressionTagSimple.class.getClassLoader());
    }

    public static final Creator<BaseSubjectBean2> CREATOR = new Creator<BaseSubjectBean2>() {
        @Override
        public BaseSubjectBean2 createFromParcel(Parcel source) {
            return new BaseSubjectBean2(source);
        }

        @Override
        public BaseSubjectBean2[] newArray(int size) {
            return new BaseSubjectBean2[size];
        }
    };
}
