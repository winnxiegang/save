package com.android.residemenu.lt_lib.model.ssq;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.residemenu.lt_lib.enumdata.BallColorEnum;


/**
 * 
 * 
 * @author leslie
 * 
 * @version $Id: Ball.java, v 0.1 2014年5月22日 下午5:02:00 leslie Exp $
 */
public class Ball implements Comparable<Ball>, Parcelable {

	private int index;

	private String value;

	private BallColorEnum colorEnum;

	private boolean selected;

	public Ball() {

	}

	public Ball(Parcel p) {

		value = p.readString();
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(value);

	}

	public static final Creator<Ball> CREATOR = new Creator<Ball>() {
		public Ball createFromParcel(Parcel p) {
			return new Ball(p);
		}

		public Ball[] newArray(int size) {
			return new Ball[size];
		}
	};

	@Override
	public int compareTo(Ball another) {

		return value.compareTo(another.getValue());
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BallColorEnum getColorEnum() {
		return colorEnum;
	}

	public void setColorEnum(BallColorEnum colorEnum) {
		this.colorEnum = colorEnum;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
