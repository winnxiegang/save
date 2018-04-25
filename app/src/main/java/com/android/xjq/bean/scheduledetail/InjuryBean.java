package com.android.xjq.bean.scheduledetail;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by kokuma on 2017/8/15.
 */

public class InjuryBean implements Parcelable {
    private long id;
    // 赛事id
    private long ri;
    //球队id
    private long ti;
    //球队名字
    private String tn;
    //球员名字
    private String pn;
    //球员id
    private long pid;
    //位置
    private MessageType pos;
    //场次
    private int cc;
    //进球数
    private int goal;
    //状态
    private String status;
    //备注
    private String detail;
    //平均评分
    private double as;
    //球衣号
    private int sn;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRi() {
        return ri;
    }

    public void setRi(long ri) {
        this.ri = ri;
    }

    public long getTi() {
        return ti;
    }

    public void setTi(long ti) {
        this.ti = ti;
    }

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public MessageType getPos() {
        return pos;
    }

    public void setPos(MessageType pos) {
        this.pos = pos;
    }

    public int getCc() {
        return cc;
    }

    public void setCc(int cc) {
        this.cc = cc;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getAs() {
        return as;
    }

    public void setAs(double as) {
        this.as = as;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.ri);
        dest.writeLong(this.ti);
        dest.writeString(this.tn);
        dest.writeString(this.pn);
        dest.writeLong(this.pid);
        dest.writeParcelable(this.pos, flags);
        dest.writeInt(this.cc);
        dest.writeInt(this.goal);
        dest.writeString(this.status);
        dest.writeString(this.detail);
        dest.writeDouble(this.as);
        dest.writeInt(this.sn);
    }

    public InjuryBean() {
    }

    protected InjuryBean(Parcel in) {
        this.id = in.readLong();
        this.ri = in.readLong();
        this.ti = in.readLong();
        this.tn = in.readString();
        this.pn = in.readString();
        this.pid = in.readLong();
        this.pos = in.readParcelable(MessageType.class.getClassLoader());
        this.cc = in.readInt();
        this.goal = in.readInt();
        this.status = in.readString();
        this.detail = in.readString();
        this.as = in.readDouble();
        this.sn = in.readInt();
    }

    public static final Creator<InjuryBean> CREATOR = new Creator<InjuryBean>() {
        @Override
        public InjuryBean createFromParcel(Parcel source) {
            return new InjuryBean(source);
        }

        @Override
        public InjuryBean[] newArray(int size) {
            return new InjuryBean[size];
        }
    };
}

