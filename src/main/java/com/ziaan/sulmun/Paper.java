package com.ziaan.sulmun;

import java.util.ArrayList;

import com.ziaan.library.*;
import com.ziaan.system.*;

public class Paper {
	private int sulpapernum; //설문지번호
	private String sulpapernm; //설문지이름
	private String sulstart; //설문시작일
	private String sulend; //설문종료일
	private int suljoinusertype; //설문대상
	
	private int totalNumberOfJoiner; //설문 총 응답자 수
	
	private ArrayList questionArrayList = new ArrayList();

	public Paper() {
	}
	
	public Paper(DataBox dataBox) {
        setSulpapernum(dataBox.getInt("d_sulpapernum"));
        setSulpapernm(dataBox.getString("d_sulpapernm"));
        setSulstart(dataBox.getString("d_sulstart"));
        setSulend(dataBox.getString("d_sulend"));
        setSuljoinusertype(dataBox.getInt("d_suljoinusertype"));        
	}
	
	public void addQuestion(Question question) {
		questionArrayList.add(question);
	}

	public ArrayList getQuestionArrayList() {
		return this.questionArrayList;
	}
	
	/**
	 * @return Returns the totalNumberOfJoiner.
	 */
	public int getTotalNumberOfJoiner() {
		return totalNumberOfJoiner;
	}

	/**
	 * @param totalNumberOfJoiner The totalNumberOfJoiner to set.
	 */
	public void setTotalNumberOfJoiner(DataBox dataBox) {
		this.totalNumberOfJoiner = dataBox.getInt("d_total");
	}

	/**
	 * @param totalNumberOfJoiner The totalNumberOfJoiner to set.
	 */
	public void setTotalNumberOfJoiner(int totalNumberOfJoiner) {
		this.totalNumberOfJoiner = totalNumberOfJoiner;
	}
	
	/**
	 * @return Returns the suljoinusertype.
	 */
	public int getSuljoinusertype() {
		return suljoinusertype;
	}

	/**
	 * @param suljoinusertype The suljoinusertype to set.
	 */
	public void setSuljoinusertype(int suljoinusertype) {
		this.suljoinusertype = suljoinusertype;
	}

	/**
	 * @return Returns the sulend.
	 */
	public String getSulend() {
		return sulend;
	}

	/**
	 * @param sulend The sulend to set.
	 */
	public void setSulend(String sulend) {
		this.sulend = sulend;
	}

	/**
	 * @return Returns the sulpapernm.
	 */
	public String getSulpapernm() {
		return sulpapernm;
	}

	/**
	 * @param sulpapernm The sulpapernm to set.
	 */
	public void setSulpapernm(String sulpapernm) {
		this.sulpapernm = sulpapernm;
	}

	/**
	 * @return Returns the sulpapernum.
	 */
	public int getSulpapernum() {
		return sulpapernum;
	}

	/**
	 * @param sulpapernum The sulpapernum to set.
	 */
	public void setSulpapernum(int sulpapernum) {
		this.sulpapernum = sulpapernum;
	}

	/**
	 * @return Returns the sulstart.
	 */
	public String getSulstart() {
		return sulstart;
	}

	/**
	 * @param sulstart The sulstart to set.
	 */
	public void setSulstart(String sulstart) {
		this.sulstart = sulstart;
	}

	
}