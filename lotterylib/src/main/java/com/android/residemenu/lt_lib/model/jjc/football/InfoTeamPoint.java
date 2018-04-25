package com.android.residemenu.lt_lib.model.jjc.football;

/**
 * 积分数据
 * 
 * @author leslie
 * 
 * @version $Id: InfoTeamPoint.java, v 0.1 2015-1-16 下午12:32:40 leslie Exp $
 */
public class InfoTeamPoint extends AbstractInfoItem {
	
	
	private String itemTitle;

	/**
	 * 比赛场数
	 */
	private String ct;
	/**
	 * 胜数量
	 */
	private String winN;
	/**
	 * 平数量
	 */
	private String drawN;

	/**
	 * 负数量
	 */
	private String lostN;
	/**
	 * 进球数
	 */
	private String inGoal;
	/**
	 * 失球数
	 */
	private String lostGoal;

	/**
	 * 净胜球
	 */
	private String realGoal;
	/**
	 * 得分
	 */
	private String point;
	/**
	 * 排名
	 */
	private String rank;
	/**
	 * 胜率
	 */
	private String winRate;

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getWinN() {
		return winN;
	}

	public void setWinN(String winN) {
		this.winN = winN;
	}

	public String getDrawN() {
		return drawN;
	}

	public void setDrawN(String drawN) {
		this.drawN = drawN;
	}

	public String getLostN() {
		return lostN;
	}

	public void setLostN(String lostN) {
		this.lostN = lostN;
	}

	public String getInGoal() {
		return inGoal;
	}

	public void setInGoal(String inGoal) {
		this.inGoal = inGoal;
	}

	public String getLostGoal() {
		return lostGoal;
	}

	public void setLostGoal(String lostGoal) {
		this.lostGoal = lostGoal;
	}

	public String getRealGoal() {
		return realGoal;
	}

	public void setRealGoal(String realGoal) {
		this.realGoal = realGoal;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getWinRate() {
		return winRate;
	}

	public void setWinRate(String winRate) {
		this.winRate = winRate;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	
}
