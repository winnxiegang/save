package com.android.residemenu.lt_lib.model.user;

/**
 * 
 * 
 * @author leslie
 * 
 * @version $Id: FinanceData.java, v 0.1 2014年7月14日 下午12:10:45 leslie Exp $
 */
public class FinanceData {

	private String transAmount;

	private String type;

	private String gmtCreate;

	private String memo;

	private String balanceAfter;

	private TransDirection transDirection;

	private String subTransMsg;

	public FinanceData() {

	}

	public String getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public TransDirection getTransDirection() {
		return transDirection;
	}

	public void setTransDirection(TransDirection transDirection) {
		this.transDirection = transDirection;
	}

	public String getSubTransMsg() {
		return subTransMsg;
	}

	public void setSubTransMsg(String subTransMsg) {
		this.subTransMsg = subTransMsg;
	}

	public String getBalanceAfter() {
		return balanceAfter;
	}

	public void setBalanceAfter(String balanceAfter) {
		this.balanceAfter = balanceAfter;
	}
	
	

}
