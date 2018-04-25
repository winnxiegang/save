package com.android.residemenu.lt_lib.model.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 快捷支付信息
 * 
 * @author leslie
 * 
 * @version $Id: QuickPayData.java, v 0.1 2014年12月3日 下午5:28:12 leslie Exp $
 */
public class QuickPayData implements Parcelable {

	private String depositNo;

	public QuickPayData() {

	}

	public QuickPayData(Parcel p) {

		depositNo = p.readString();
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(depositNo);
	}

	public static final Creator<QuickPayData> CREATOR = new Creator<QuickPayData>() {
		public QuickPayData createFromParcel(Parcel p) {
			return new QuickPayData(p);
		}

		public QuickPayData[] newArray(int size) {
			return new QuickPayData[size];
		}
	};

	public String getDepositNo() {
		return depositNo;
	}

	public void setDepositNo(String depositNo) {
		this.depositNo = depositNo;
	}

}
