package com.android.residemenu.lt_lib.model.jjc.football;

/**
 * 未来赛事
 * 
 * @author leslie
 * 
 * @version $Id: InfoFutureItem.java, v 0.1 2015-1-14 下午6:36:24 leslie Exp $
 */
public class InfoFutureItem extends AbstractInfoItem {

	private String leagueName;

	private String homeName;

	private String guestName;

	private String beginTime;

	private String differTime;

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public String getHomeName() {
		return homeName;
	}

	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getDifferTime() {
		return differTime;
	}

	public void setDifferTime(String differTime) {
		this.differTime = differTime;
	}

}
