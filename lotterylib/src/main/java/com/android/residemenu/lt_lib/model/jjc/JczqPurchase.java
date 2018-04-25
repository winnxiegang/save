package com.android.residemenu.lt_lib.model.jjc;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.residemenu.lt_lib.model.AbstractPurchase;


/**
 * 
 * 
 * @author leslie
 * 
 * @version $Id: JczqPurchase.java, v 0.1 2014年6月11日 上午11:55:26 leslie Exp $
 */
public class JczqPurchase extends AbstractPurchase<MixtureData> {

	public JczqPurchase() {
		super();
    }

	public JczqPurchase(Parcel p) {
		super();
		payMode = p.readString();
		multiple = p.readInt();
		payFee = p.readInt();
		dataList = p.readArrayList(MixtureData.class.getClassLoader());
		mixTypes = p.readString();
		playType = p.readString();
        buyType = p.readString();
        checkedDataList=p.readArrayList(MixtureData.class.getClassLoader());

	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(payMode);
		dest.writeInt(multiple);
		dest.writeInt(payFee);
		dest.writeList(dataList);
		dest.writeString(mixTypes);
		dest.writeString(playType);
        dest.writeString(buyType);
        dest.writeList(checkedDataList);

	}

	public static final Parcelable.Creator<JczqPurchase> CREATOR = new Parcelable.Creator<JczqPurchase>() {
		public JczqPurchase createFromParcel(Parcel p) {
			return new JczqPurchase(p);
		}

		public JczqPurchase[] newArray(int size) {
			return new JczqPurchase[size];
		}
	};

}
