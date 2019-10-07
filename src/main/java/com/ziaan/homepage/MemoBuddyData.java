//**********************************************************
//  1. 제      목: 쪽지 친구 정보 Data
//  2. 프로그램명 : MemoBuddyData.java
//  3. 개      요: 쪽지 친구 정보 data bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 7.  2
//  7. 수      정:
//**********************************************************
package com.ziaan.homepage;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MemoBuddyData {

	private String userid  ;
	private String comp    ;
	private String compnm  ;
	private String jikwi   ;
	private String jikwinm ;
	private String cono    ;
	private String buddy   ;
	private String buddynm ;
	private String email   ;
	private String positionnm   ;
	
	private int seq      ;
	private int dispnum  ;            // 총게시물수
	private int totalpagecount ;     // 게시물총페이지수


	public MemoBuddyData() {}
	/**
	 * @return
	 */
	public String getBuddy() {
		return buddy;
	}

	/**
	 * @return
	 */
	public String getBuddynm() {
		return buddynm;
	}

	/**
	 * @return
	 */
	public String getComp() {
		return comp;
	}

	/**
	 * @return
	 */
	public String getCompnm() {
		return compnm;
	}

	/**
	 * @return
	 */
	public String getCono() {
		return cono;
	}

	/**
	 * @return
	 */
	public String getJikwi() {
		return jikwi;
	}

	/**
	 * @return
	 */
	public String getJikwinm() {
		return jikwinm;
	}

	/**
	 * @return
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @return
	 */
	public String getUserid() {
		return userid;
	}
	
	/**
	 * @return
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @return
	 */
	public String getPositionNm() {
		return positionnm;
	}

	/**
	 * @param string
	 */
	public void setBuddy(String string) {
		buddy = string;
	}

	/**
	 * @param string
	 */
	public void setBuddynm(String string) {
		buddynm = string;
	}

	/**
	 * @param string
	 */
	public void setComp(String string) {
		comp = string;
	}

	/**
	 * @param string
	 */
	public void setCompnm(String string) {
		compnm = string;
	}

	/**
	 * @param string
	 */
	public void setCono(String string) {
		cono = string;
	}

	/**
	 * @param string
	 */
	public void setJikwi(String string) {
		jikwi = string;
	}

	/**
	 * @param string
	 */
	public void setJikwinm(String string) {
		jikwinm = string;
	}

	/**
	 * @param i
	 */
	public void setSeq(int i) {
		seq = i;
	}

	/**
	 * @param string
	 */
	public void setUserid(String string) {
		userid = string;
	}
	
	/**
	 * @param string
	 */
	public void setEmail(String string) {
		email = string;
	}

	/**
	 * @return
	 */
	public int getDispnum() {
		return dispnum;
	}

	/**
	 * @return
	 */
	public int getTotalpagecount() {
		return totalpagecount;
	}

	/**
	 * @param i
	 */
	public void setDispnum(int i) {
		dispnum = i;
	}

	/**
	 * @param i
	 */
	public void setTotalpagecount(int i) {
		totalpagecount = i;
	}
	
	/**
	 * @param i
	 */
	public void setPositionNm(String string) {
		positionnm = string;
	}

}
