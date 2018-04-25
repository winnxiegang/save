package com.android.residemenu.lt_lib.model.lt;

/**
 * 
 * 可卖彩票种类
 * 
 * @author leslie
 * 
 * @version $Id: SellData.java, v 0.1 2014年5月14日 下午5:17:35 leslie Exp $
 */
public class SellData {

	private String subLotteryType=null;
	

	private int resId;
	

	/**
	 * 类别编号
	 */
	private String code;

	/**
	 * 名称
	 */
	private String name;
	/**
	 * 期次
	 */
	private String issueNo;

	/**
	 * 截止时间
	 */
	private long timeLimt;

	/**
	 * 中间提示
	 */
	private String tip;
	/**
	 * 最下面提示
	 */
	private String footTip;

	public SellData() {

	}
	

	public String getSubLotteryType() {
		return subLotteryType;
	}

	public void setSubLotteryType(String subLotteryType) {
		this.subLotteryType = subLotteryType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public long getTimeLimt() {
		return timeLimt;
	}

	public void setTimeLimt(long timeLimt) {
		this.timeLimt = timeLimt;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getFootTip() {
		return footTip;
	}

	public void setFootTip(String footTip) {
		this.footTip = footTip;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
