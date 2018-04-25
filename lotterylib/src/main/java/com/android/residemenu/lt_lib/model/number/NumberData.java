package com.android.residemenu.lt_lib.model.number;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.residemenu.lt_lib.model.ssq.Ball;

import java.util.List;


/**
 * 
 * 数字彩数据
 * 
 * @author leslie
 * 
 * @version $Id: NumberData.java, v 0.1 2014年8月13日 下午5:22:57 leslie Exp $
 */
public class NumberData implements Parcelable {

	private String issueNo;

	private List<Ball> redList;

	private List<Ball> blueList;

	/**
	 * 倍数
	 */
	private int multiple = 1;

	/**
	 * 注数
	 */
	private int amount;
	/**
	 * 总额
	 */
	private int payFee;

	public NumberData() {

	}

	public NumberData(Parcel p) {
		issueNo = p.readString();
		multiple = p.readInt();
		redList = p.readArrayList(Ball.class.getClassLoader());
		blueList = p.readArrayList(Ball.class.getClassLoader());
		payFee = p.readInt();
		amount = p.readInt();
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(issueNo);
		dest.writeInt(multiple);
		dest.writeList(redList);
		dest.writeList(blueList);
		dest.writeInt(payFee);
		dest.writeInt(amount);

	}

	public static final Creator<NumberData> CREATOR = new Creator<NumberData>() {
		public NumberData createFromParcel(Parcel p) {
			return new NumberData(p);
		}

		public NumberData[] newArray(int size) {
			return new NumberData[size];
		}
	};

	public List<Ball> getRedList() {
		return redList;
	}

	public void setRedList(List<Ball> redList) {
		this.redList = redList;
	}

	public List<Ball> getBlueList() {
		return blueList;
	}

	public void setBlueList(List<Ball> blueList) {
		this.blueList = blueList;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getPayFee() {
		return payFee;
	}

	public void setPayFee(int payFee) {
		this.payFee = payFee;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

}
