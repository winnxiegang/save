package com.android.residemenu.lt_lib.model.jjc.football;

public class InfoOddsItem {

	

	private String company;

	private String firstDrawSp;

	private String firstHomeSp;

	private String firstGuestSp;

	private String nowDrawSp;

	private String nowHomeSp;

	private String nowGuestSp;

	public InfoOddsItem() {

	}

	public String getFirstHomeSp() {
		return firstHomeSp;
	}

	public void setFirstHomeSp(String firstHomeSp) {
		this.firstHomeSp = firstHomeSp;
	}

	public String getFirstGuestSp() {
		return firstGuestSp;
	}

	public void setFirstGuestSp(String firstGuestSp) {
		this.firstGuestSp = firstGuestSp;
	}

	public String getNowHomeSp() {
		return nowHomeSp;
	}

	public void setNowHomeSp(String nowHomeSp) {
		this.nowHomeSp = nowHomeSp;
	}

	public String getNowGuestSp() {
		return nowGuestSp;
	}

	public void setNowGuestSp(String nowGuestSp) {
		this.nowGuestSp = nowGuestSp;
	}

	public String getNowDrawSp() {
		return nowDrawSp;
	}

	public void setNowDrawSp(String nowDrawSp) {
		this.nowDrawSp = nowDrawSp;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getFirstDrawSp() {
		return firstDrawSp;
	}

	public void setFirstDrawSp(String firstDrawSp) {
		this.firstDrawSp = firstDrawSp;
	}
	
	
	@Override
	public String toString() {
		return "InfoOddsItem [company=" + company + ", firstDrawSp="
				+ firstDrawSp + ", firstHomeSp=" + firstHomeSp
				+ ", firstGuestSp=" + firstGuestSp + ", nowDrawSp=" + nowDrawSp
				+ ", nowHomeSp=" + nowHomeSp + ", nowGuestSp=" + nowGuestSp
				+ "]";
	}
	

}
