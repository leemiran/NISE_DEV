package com.ziaan.sulmun;

import java.util.ArrayList;

import com.ziaan.library.DataBox;

public class Question {
	private int sulnum;
	private String subj;
	private String distcode;
	private String sultype;	
	private String sultext;
	private int selcount;
	private int selmax;
	private String sulreturn;
	private int scalecode;
	private String distcodenm;
	private String sultypenm;
	
	private ArrayList optionArrayList = new ArrayList();
	
	//private Scale scale;
	
	private ArrayList answerArrayList = new ArrayList();
	
	public Question (){
	}
	
	public Question (Object o) {
		DataBox dataBox=(DataBox)o;
		
		setSulnum(dataBox.getInt("d_sulnum"));
		setSubj(dataBox.getString("d_subj"));
		setDistcode(dataBox.getString("d_distcode"));
		setSultype(dataBox.getString("d_sultype"));
		setSultext(dataBox.getString("d_sultext"));
		setSelcount(dataBox.getInt("d_selcount"));
		setSelmax(dataBox.getInt("d_selmax"));
		setSulreturn(dataBox.getString("d_sulreturn"));
		setScalecode(dataBox.getInt("d_scalenum"));
		setDistcodenm(dataBox.getString("d_distcodenm"));
		setSultypenm(dataBox.getString("d_sultypenm"));
	}
	
	public void addAnswer(String answer) {
		answerArrayList.add(answer);
	}
	
	public ArrayList getAnswerArrayList() {
		return this.answerArrayList;
	}
	
	public void addOption(Option option) {
		optionArrayList.add(option);
	}
		
	public ArrayList getOptionArrayList() {
		return this.optionArrayList;
	}
	
	/**
	 * @return Returns the distcode.
	 */
	public String getDistcode() {
		return distcode;
	}

//	/**
//	 * @return Returns the scale.
//	 */
//	public Scale getScale() {
//		return scale;
//	}
//
//	/**
//	 * @param scale The scale to set.
//	 */
//	public void setScale(Scale scale) {
//		this.scale = scale;
//	}

	/**
	 * @param distcode The distcode to set.
	 */
	public void setDistcode(String distcode) {
		this.distcode = distcode;
	}

	/**
	 * @return Returns the distcodenm.
	 */
	public String getDistcodenm() {
		return distcodenm;
	}

	/**
	 * @param distcodenm The distcodenm to set.
	 */
	public void setDistcodenm(String distcodenm) {
		this.distcodenm = distcodenm;
	}

	/**
	 * @return Returns the scalenum.
	 */
	public int getScalecode() {
		return scalecode;
	}

	/**
	 * @param scalenum The scalenum to set.
	 */
	public void setScalecode(int scalenum) {
		this.scalecode = scalenum;
	}

	/**
	 * @return Returns the selcount.
	 */
	public int getSelcount() {
		return selcount;
	}

	/**
	 * @param selcount The selcount to set.
	 */
	public void setSelcount(int selcount) {
		this.selcount = selcount;
	}

	/**
	 * @return Returns the selmax.
	 */
	public int getSelmax() {
		return selmax;
	}

	/**
	 * @param selmax The selmax to set.
	 */
	public void setSelmax(int selmax) {
		this.selmax = selmax;
	}

	/**
	 * @return Returns the subj.
	 */
	public String getSubj() {
		return subj;
	}

	/**
	 * @param subj The subj to set.
	 */
	public void setSubj(String subj) {
		this.subj = subj;
	}

	/**
	 * @return Returns the sulnum.
	 */
	public int getSulnum() {
		return sulnum;
	}

	/**
	 * @param sulnum The sulnum to set.
	 */
	public void setSulnum(int sulnum) {
		this.sulnum = sulnum;
	}

	/**
	 * @return Returns the sulreturn.
	 */
	public String getSulreturn() {
		return sulreturn;
	}

	/**
	 * @param sulreturn The sulreturn to set.
	 */
	public void setSulreturn(String sulreturn) {
		this.sulreturn = sulreturn;
	}

	/**
	 * @return Returns the sultext.
	 */
	public String getSultext() {
		return sultext;
	}

	/**
	 * @param sultext The sultext to set.
	 */
	public void setSultext(String sultext) {
		this.sultext = sultext;
	}

	/**
	 * @return Returns the sultype.
	 */
	public String getSultype() {
		return sultype;
	}

	/**
	 * @param sultype The sultype to set.
	 */
	public void setSultype(String sultype) {
		this.sultype = sultype;
	}

	/**
	 * @return Returns the sultypenm.
	 */
	public String getSultypenm() {
		return sultypenm;
	}

	/**
	 * @param sultypenm The sultypenm to set.
	 */
	public void setSultypenm(String sultypenm) {
		this.sultypenm = sultypenm;
	}
	
	
}