package com.android.residemenu.lt_lib.model.jjc;

import java.util.Date;

/**
 * 
 * 
 * @author leslie
 * 
 * @version $Id: RaceData.java, v 0.1 2014年4月28日 上午11:45:33 leslie Exp $
 */
public class RaceData {

	/**
	 * 期次号
	 */
	protected String issueNo;

	/**
	 * 赛事名称
	 */
	protected String gameName;

	/**
	 * 开始日期
	 */
	protected Date startDate;
	
	
	/**
	 * 截止销售时间
	 */
	protected String stopSellDate;

	/**
	 * 主队名
	 */
	protected String homeTeamName;

	/**
	 * 客队名
	 */
	protected String guestTeamName;

	/**
	 * 内部赛事ID
	 */
	protected long innerRaceId;

	/**
	 * 内部主队ID
	 */
	protected long innerHomeTeamId;

	/**
	 * 内部客队ID
	 */
	protected long innerGuestTeamId;

	/**
	 * 内部比赛ID
	 */
	protected long innerMatchId;

	/**
	 * 赛事编号
	 */
	protected String gameNo;

	public RaceData() {

	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStopSellDate() {
		return stopSellDate;
	}

	public void setStopSellDate(String stopSellDate) {
		this.stopSellDate = stopSellDate;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}

	public String getGuestTeamName() {
		return guestTeamName;
	}

	public void setGuestTeamName(String guestTeamName) {
		this.guestTeamName = guestTeamName;
	}

	public long getInnerRaceId() {
		return innerRaceId;
	}

	public void setInnerRaceId(long innerRaceId) {
		this.innerRaceId = innerRaceId;
	}

	public long getInnerHomeTeamId() {
		return innerHomeTeamId;
	}

	public void setInnerHomeTeamId(long innerHomeTeamId) {
		this.innerHomeTeamId = innerHomeTeamId;
	}

	public long getInnerGuestTeamId() {
		return innerGuestTeamId;
	}

	public void setInnerGuestTeamId(long innerGuestTeamId) {
		this.innerGuestTeamId = innerGuestTeamId;
	}

	public long getInnerMatchId() {
		return innerMatchId;
	}

	public void setInnerMatchId(long innerMatchId) {
		this.innerMatchId = innerMatchId;
	}

	public String getGameNo() {
		return gameNo;
	}

	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}


	
	

}
