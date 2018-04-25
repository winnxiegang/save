package com.android.residemenu.lt_lib.model.lt;


import com.android.residemenu.lt_lib.enumdata.LotterySelecterEnum;

/**
 * 选项数据
 * 
 * @author leslie
 * 
 * @version $Id: OptionData.java, v 0.1 2014年6月26日 上午10:55:04 leslie Exp $
 */
public class OptionData {

	private int index;

	private String code;

	private String title;

	private String odds;

	private boolean selected;

    private String id;

	private LotterySelecterEnum selecterEnum;

	/**
	 * 是否可点击
	 */
	private boolean enabled = true;

	public OptionData() {

	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOdds() {
		return odds;
	}

	public void setOdds(String odds) {
		this.odds = odds;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public LotterySelecterEnum getSelecterEnum() {
		return selecterEnum;
	}

	public void setSelecterEnum(LotterySelecterEnum selecterEnum) {
		this.selecterEnum = selecterEnum;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
