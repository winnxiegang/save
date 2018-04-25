package com.android.residemenu.lt_lib.model.jjc;

import java.util.List;

/**
 * 比赛结果集
 * 
 * @author leslie
 * 
 * @version $Id: PrizedResults.java, v 0.1 2014年7月12日 下午3:50:18 leslie Exp $
 */
public class PrizedResults {

	private int halfHomeScore;

	private int halfGuestScore;

	private int fullHomeScore;

	private int fullGuestScore;

	/**
	 * 所有投注彩果
	 */
	private List<PrizedResult> prizedResultList;

	public PrizedResults() {

	}

	public int getHalfHomeScore() {
		return halfHomeScore;
	}

	public void setHalfHomeScore(int halfHomeScore) {
		this.halfHomeScore = halfHomeScore;
	}

	public int getHalfGuestScore() {
		return halfGuestScore;
	}

	public void setHalfGuestScore(int halfGuestScore) {
		this.halfGuestScore = halfGuestScore;
	}

	public int getFullHomeScore() {
		return fullHomeScore;
	}

	public void setFullHomeScore(int fullHomeScore) {
		this.fullHomeScore = fullHomeScore;
	}

	public int getFullGuestScore() {
		return fullGuestScore;
	}

	public void setFullGuestScore(int fullGuestScore) {
		this.fullGuestScore = fullGuestScore;
	}

	public List<PrizedResult> getPrizedResultList() {
		return prizedResultList;
	}

	public void setPrizedResultList(List<PrizedResult> prizedResultList) {
		this.prizedResultList = prizedResultList;
	}

}
