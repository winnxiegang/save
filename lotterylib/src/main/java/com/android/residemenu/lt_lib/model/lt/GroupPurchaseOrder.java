package com.android.residemenu.lt_lib.model.lt;

public class GroupPurchaseOrder {

	private String id;

	/**
	 * 输入值
	 */
	private String value;

	/**
	 * 总计
	 */
	private String total;
	/**
	 * 是否可手复制
	 */
	//private boolean copyable;

	private String ownerUserId;

	/**
	 * 当前方案是否可合买
	 */
	//private boolean combineable;
	/**
	 * 是要合买还是复制
	 */
	private String action;

	public GroupPurchaseOrder() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

//	public boolean isCopyable() {
//		return copyable;
//	}
//
//	public void setCopyable(boolean copyable) {
//		this.copyable = copyable;
//	}

	public String getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

//	public boolean isCombineable() {
//		return combineable;
//	}
//
//	public void setCombineable(boolean combineable) {
//		this.combineable = combineable;
//	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
}
