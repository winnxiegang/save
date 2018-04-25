package com.android.residemenu.lt_lib.model.user;

import android.os.Parcel;
import android.os.Parcelable;

public class ThirdUserInfo implements Parcelable {

	/**
	 * 第三方用户id
	 */
	private String thirdUserId;

	/**
	 * 第三方通道
	 */

	private String thirdChannel;

	/**
	 * 第三方登录名
	 */
	private String thirdLoginName;

	/**
	 * 第三方用户名
	 */
	private String thirdUserName;

	/**
	 * 属性集合
	 */
	private String thirdProperties;

	private String accessToken;

	private String expiresIn;

	public ThirdUserInfo() {

	}

	public ThirdUserInfo(Parcel p) {

		thirdUserId = p.readString();
		thirdChannel = p.readString();
		thirdLoginName = p.readString();
		thirdUserName = p.readString();
		thirdProperties = p.readString();
		accessToken = p.readString();
		expiresIn = p.readString();

	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(thirdUserId);
		dest.writeString(thirdChannel);
		dest.writeString(thirdLoginName);
		dest.writeString(thirdUserName);
		dest.writeString(thirdProperties);
		dest.writeString(accessToken);
		dest.writeString(expiresIn);
	}

	public static final Creator<ThirdUserInfo> CREATOR = new Creator<ThirdUserInfo>() {
		public ThirdUserInfo createFromParcel(Parcel p) {
			return new ThirdUserInfo(p);
		}

		public ThirdUserInfo[] newArray(int size) {
			return new ThirdUserInfo[size];
		}
	};

	public String getThirdUserId() {
		return thirdUserId;
	}

	public void setThirdUserId(String thirdUserId) {
		this.thirdUserId = thirdUserId;
	}

	public String getThirdChannel() {
		return thirdChannel;
	}

	public void setThirdChannel(String thirdChannel) {
		this.thirdChannel = thirdChannel;
	}

	public String getThirdLoginName() {
		return thirdLoginName;
	}

	public void setThirdLoginName(String thirdLoginName) {
		this.thirdLoginName = thirdLoginName;
	}

	public String getThirdUserName() {
		return thirdUserName;
	}

	public void setThirdUserName(String thirdUserName) {
		this.thirdUserName = thirdUserName;
	}

	public String getThirdProperties() {
		return thirdProperties;
	}

	public void setThirdProperties(String thirdProperties) {
		this.thirdProperties = thirdProperties;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	


}
