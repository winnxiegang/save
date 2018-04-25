package com.android.residemenu.lt_lib.model.number;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.residemenu.lt_lib.model.AbstractPurchase;


/**
 * 数字彩
 * 
 * @author leslie
 * 
 * @version $Id: NumberPurchase.java, v 0.1 2014年8月19日 下午5:49:51 leslie Exp $
 */
public class NumberPurchase extends AbstractPurchase<NumberData> {

	private String issueNo;

	private boolean superAdd;

	public NumberPurchase() {

		super();
	}

	public NumberPurchase(Parcel p) {

		super();
		issueNo = p.readString();
		payMode = p.readString();
		multiple = p.readInt();
		payFee = p.readInt();
		dataList = p.readArrayList(NumberData.class.getClassLoader());
		superAdd = p.readByte() != 0;
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(issueNo);
		dest.writeString(payMode);
		dest.writeInt(multiple);
		dest.writeInt(payFee);
		dest.writeList(dataList);
		dest.writeByte((byte) (superAdd ? 1 : 0));

    }

	public static final Parcelable.Creator<NumberPurchase> CREATOR = new Parcelable.Creator<NumberPurchase>() {
		public NumberPurchase createFromParcel(Parcel p) {
			return new NumberPurchase(p);
		}

		public NumberPurchase[] newArray(int size) {
			return new NumberPurchase[size];
		}
	};

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public boolean isSuperAdd() {
		return superAdd;
	}

	public void setSuperAdd(boolean superAdd) {
		this.superAdd = superAdd;
	}

}
