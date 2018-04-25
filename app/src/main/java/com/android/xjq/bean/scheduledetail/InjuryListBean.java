package com.android.xjq.bean.scheduledetail;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kokuma on 2017/8/15.
 */

public class InjuryListBean implements Parcelable {

    private boolean success;
    private String detailMessage;
    private String nowDate;
    List<InjuryBean> homeTeamInjuryList;  //主队伤停
    List<InjuryBean> guestTeamInjuryList;  //客队伤停

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public List<InjuryBean> getHomeTeamInjuryList() {
        return homeTeamInjuryList;
    }

    public void setHomeTeamInjuryList(List<InjuryBean> homeTeamInjuryList) {
        this.homeTeamInjuryList = homeTeamInjuryList;
    }

    public List<InjuryBean> getGuestTeamInjuryList() {
        return guestTeamInjuryList;
    }

    public void setGuestTeamInjuryList(List<InjuryBean> guestTeamInjuryList) {
        this.guestTeamInjuryList = guestTeamInjuryList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.detailMessage);
        dest.writeString(this.nowDate);
        dest.writeTypedList(this.homeTeamInjuryList);
        dest.writeTypedList(this.guestTeamInjuryList);
    }

    public InjuryListBean() {
    }

    protected InjuryListBean(Parcel in) {
        this.success = in.readByte() != 0;
        this.detailMessage = in.readString();
        this.nowDate = in.readString();
        this.homeTeamInjuryList = in.createTypedArrayList(InjuryBean.CREATOR);
        this.guestTeamInjuryList = in.createTypedArrayList(InjuryBean.CREATOR);
    }

    public static final Creator<InjuryListBean> CREATOR = new Creator<InjuryListBean>() {
        @Override
        public InjuryListBean createFromParcel(Parcel source) {
            return new InjuryListBean(source);
        }

        @Override
        public InjuryListBean[] newArray(int size) {
            return new InjuryListBean[size];
        }
    };
}
