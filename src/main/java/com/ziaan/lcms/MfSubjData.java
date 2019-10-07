/*
 * @(#)MfSubjData.java	1.0 2008. 11. 03
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */
package com.ziaan.lcms;

/**
 * M/F 메뉴정보
 *
 * @version		1.0, 2008/11/03
 * @author		Chung Jin-pil
 */
public class MfSubjData {

	private String subj;
	private String menu;
	private String menunm;
	private String pgm;
	private String pgram1;
	private String pgram2;
	private String pgram3;
	private String pgram4;
	private String pgram5;
	private int orders;
	private String pgmType;
	private String isRequired;
	private String luserid;
	private String ldate;
	
	public MfSubjData() {
	}

	public String getSubj() {
		return subj;
	}

	public void setSubj(String subj) {
		this.subj = subj;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getMenunm() {
		return menunm;
	}

	public void setMenunm(String menunm) {
		this.menunm = menunm;
	}

	public String getPgm() {
		return pgm;
	}

	public void setPgm(String pgm) {
		this.pgm = pgm;
	}

	public String getPgram1() {
		return pgram1;
	}

	public void setPgram1(String pgram1) {
		this.pgram1 = pgram1;
	}

	public String getPgram2() {
		return pgram2;
	}

	public void setPgram2(String pgram2) {
		this.pgram2 = pgram2;
	}

	public String getPgram3() {
		return pgram3;
	}

	public void setPgram3(String pgram3) {
		this.pgram3 = pgram3;
	}

	public String getPgram4() {
		return pgram4;
	}

	public void setPgram4(String pgram4) {
		this.pgram4 = pgram4;
	}

	public String getPgram5() {
		return pgram5;
	}

	public void setPgram5(String pgram5) {
		this.pgram5 = pgram5;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public String getPgmType() {
		return pgmType;
	}

	public void setPgmType(String pgmType) {
		this.pgmType = pgmType;
	}

	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

	public String getIsRequiredText() {
		if ( isRequired != null && isRequired.equals("Y") ) {
			return "[필수] ";
		} else {
			return "";
		}
	}

	public String getLuserid() {
		return luserid;
	}

	public void setLuserid(String luserid) {
		this.luserid = luserid;
	}

	public String getLdate() {
		return ldate;
	}

	public void setLdate(String ldate) {
		this.ldate = ldate;
	}
}
