package com.android.residemenu.lt_lib.model.jjc.football;

/**
 * 已比赛信息
 * 
 * @author leslie
 * 
 * @version $Id: InfoVsItem.java, v 0.1 2015-1-15 上午11:35:27 leslie Exp $
 */
public class InfoVsItem extends AbstractInfoItem {

	private String leagueName;

	private String homeName;

	private String guestName;

	private String halfScore;

	private String score;

	private String result;

	private String beginTime;

	public InfoVsItem() {

	}

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

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getHalfScore() {
		return halfScore;
	}

	public void setHalfScore(String halfScore) {
		this.halfScore = halfScore;
	}

}
