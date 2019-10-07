// **********************************************************
//  1. ��      ��: PROPOSE STATUS DATA
//  2. ���α׷���: ProposeStatusData.java
//  3. ��      ��: ��û ��Ȳ data bean
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��: ������ 2003. 8. 20.
//  7. ��      ��:
// **********************************************************
package com.ziaan.propose;

public class ProposeStatusData { 
    
	private String grseq;
	private String grseqnm;
    private String course;		
    private String cyear;		
    private String courseseq;
	private String coursenm;
	private String subj;
    private String year;	
	private String subjseq;
	private String subjseqgr;
	private String subjnm;	
	private String companynm;            /* ȸ���  	*/
	private String compnm;               /* �ҼӸ�  	*/
	private String jikwinm;              /* ������   */
	private String jikunnm;              /* ������   */
	private String jikupnm;              /* ���޸�   */
	                                     
	private String userid ;              /* ID       */
	private String cono;                 /* ���     */
	private String name;   	             /* �̸�     */		
	private String position_nm;   	     /* �μ�     */		
	private String lvl_nm;   	         /* ����     */		
	private String appdate;	             /* ��û��   */
    private String biyong;
    private String goyongpricemajor;
    private String goyongpriceminor;
	private String edustart;             /* ���� �����Ͻ� 	*/
	private String eduend;		         /* ���� �����Ͻ� 	*/
	private String isproposeapproval;    /* ����������� ���� ���� 	*/
	private String chkfirst;             /* 1�� ���� ���� 	*/
	private String chkfinal;             /* ���� ���� ���� 	*/
	private String email;                
	private String ismailing;            
    private String isnewcourse;             	
    private String canceldate;	         /* �����   */
    private String cancelkind;	         /* ��ұ���   */
    private String reason;	             /* ��һ���   */
    private String propstart;            /* ������û �����Ͻ� 	*/
    private String propend;              /* ������û �����Ͻ� 	*/
	private String isonoff;    
    private int studentlimit;            /* ���� 	*/
    private int procnt;                  /* ������û �ο� 	*/
    private int cancnt;                  /* ������û��� �ο�	*/
	private int rowspan;		
	private int dispnum;
	private int totalpagecount;
	private int rowcount;
	private String amount;
	
	private String order_id;
	private String paycd;
	private String paynm;
	public String getPaynm() {
		return paynm;
	}

	public void setPaynm(String paynm) {
		this.paynm = paynm;
	}

	private String enter_dt;
	private String enter_yn;
	private String repay_dt;
	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getPaycd() {
		return paycd;
	}

	public void setPaycd(String paycd) {
		this.paycd = paycd;
	}

	public String getEnter_dt() {
		return enter_dt;
	}

	public void setEnter_dt(String enter_dt) {
		this.enter_dt = enter_dt;
	}

	public String getEnter_yn() {
		return enter_yn;
	}

	public void setEnter_yn(String enter_yn) {
		this.enter_yn = enter_yn;
	}

	public String getRepay_dt() {
		return repay_dt;
	}

	public void setRepay_dt(String repay_dt) {
		this.repay_dt = repay_dt;
	}

	public String getRepay_yn() {
		return repay_yn;
	}

	public void setRepay_yn(String repay_yn) {
		this.repay_yn = repay_yn;
	}

	private String repay_yn;
	

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}


	public ProposeStatusData() { };
	
	/**
	 * @return
	 */
	public String getAppdate() { 
		return appdate;
	}
	
    public void setBiyong(String string) { 
    	biyong = string;
    }
    
    /**
     * @return
     */
    public String getBiyong() { 
    	return biyong;
    }
    
    public void setGoyongpricemajor(String string) { 
    	goyongpricemajor = string;
    }
    
    /**
     * @return
     */
    public String getGoyongpricemajor() { 
    	return goyongpricemajor;
    }
    
    public void setGoyongpriceminor(String string) { 
    	goyongpriceminor = string;
    }
    
    /**
     * @return
     */
    public String getGoyongpriceminor() { 
    	return goyongpriceminor;
    }
    
	/**
	 * @return
	 */
	public String getChkfinal() { 
		return chkfinal;
	}

	/**
	 * @return
	 */
	public String getChkfirst() { 
		return chkfirst;
	}
	
	/**
	 * @return
	 */
	public String getIsproposeapproval() { 
		return isproposeapproval;
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
	public String getCourse() { 
		return course;
	}

	/**
	 * @return
	 */
	public String getCoursenm() { 
		return coursenm;
	}

	/**
	 * @return
	 */
	public String getCourseseq() { 
		return courseseq;
	}

	/**
	 * @return
	 */
	public String getCyear() { 
		return cyear;
	}

	/**
	 * @return
	 */
	public String getEduend() { 
		return eduend;
	}

	/**
	 * @return
	 */
	public String getEdustart() { 
		return edustart;
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
	public String getGrseq() { 
		return grseq;
	}

	/**
	 * @return
	 */
	public String getGrseqnm() { 
		return grseqnm;
	}
	
	/**
	 * @return
	 */
	public String getIsmailing() { 
		return ismailing;
	}

	/**
	 * @return
	 */
	public String getIsnewcourse() { 
		return isnewcourse;
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
	public String getJikupnm() { 
		return jikupnm;
	}


	/**
	 * @return
	 */
	public String getName() { 
		return name;
	}
	
	public String getPosition_nm() { 
		return position_nm;
	}
	
	public String getLvl_nm() { 
		return lvl_nm;
	}

	/**
	 * @return
	 */
	public int getRowspan() { 
		return rowspan;
	}

	/**
	 * @return
	 */
	public String getSubj() { 
		return subj;
	}

	/**
	 * @return
	 */
	public String getSubjnm() { 
		return subjnm;
	}

	/**
	 * @return
	 */
	public String getSubjseq() { 
		return subjseq;
	}
	
	/**
	 * @return
	 */
	public String getSubjseqgr() { 
		return subjseqgr;
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
	public String getYear() { 
		return year;
	}

    /**
   	 * @return
	 */
	public String getCanceldate() { 
		return canceldate;
	}

    /**
	 * @return
	 */
	public String getCancelkind() { 
		return cancelkind;
	}

    /**
	 * @return
	 */
	public String getReason() { 
		return reason;
	}

    /**
	 * @return
	 */
	public String getPropstart() { 
		return propstart;
	}

    /**
	 * @return
	 */
	public String getPropend() { 
		return propend;
	}

   /**
	 * @return
	 */
	public int getStudentlimit() { 
		return studentlimit;
	}
	
   /**
	 * @return
	 */
	public int getProcnt() { 
		return procnt;
	}

   /**
	 * @return
	 */
	public int getCancnt() { 
		return cancnt;
	}

	/**
	 * @param string
	 */
	public void setAppdate(String string) { 
		appdate = string;
	}

	/**
	 * @param string
	 */
	public void setChkfinal(String string) { 
		chkfinal = string;
	}

	/**
	 * @param string
	 */
	public void setChkfirst(String string) { 
		chkfirst = string;
	}

     /**
	 * @param string
	 */
    public void setIsproposeapproval(String string) { 
		isproposeapproval = string;
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
	public void setCourse(String string) { 
		course = string;
	}

	/**
	 * @param string
	 */
	public void setCoursenm(String string) { 
		coursenm = string;
	}

	/**
	 * @param string
	 */
	public void setCourseseq(String string) { 
		courseseq = string;
	}

	/**
	 * @param string
	 */
	public void setCyear(String string) { 
		cyear = string;
	}

	/**
	 * @param string
	 */
	public void setEduend(String string) { 
		eduend = string;
	}

	/**
	 * @param string
	 */
	public void setEdustart(String string) { 
		edustart = string;
	}

	/**
	 * @param string
	 */
	public void setEmail(String string) { 
		email = string;
	}

	/**
	 * @param string
	 */
	public void setGrseq(String string) { 
		grseq = string;
	}

	/**
	 * @param string
	 */
	public void setGrseqnm(String string) { 
		grseqnm = string;
	}

	/**
	 * @param string
	 */
	public void setIsmailing(String string) { 
		ismailing = string;
	}

	/**
	 * @param string
	 */
	public void setIsnewcourse(String string) { 
		isnewcourse = string;
	}

	/**
	 * @param string
	 */
	public void setJikwinm(String string) { 
		jikwinm = string;
	}

	/**
	 * @param string
	 */
	public void setJikupnm(String string) { 
		jikupnm = string;
	}
	/**
	 * @param string
	 */
	public void setName(String string) { 
		name = string;
	}
	
	public void setPosition_nm(String string) { 
		position_nm = string;
	}
	
	public void setLvl_nm(String string) { 
		lvl_nm = string;
	}

	/**
	 * @param i
	 */
	public void setRowspan(int i) { 
		rowspan = i;
	}

	/**
	 * @param string
	 */
	public void setSubj(String string) { 
		subj = string;
	}

	/**
	 * @param string
	 */
	public void setSubjnm(String string) { 
		subjnm = string;
	}

	/**
	 * @param string
	 */
	public void setSubjseq(String string) { 
		subjseq = string;
	}
	
	/**
	 * @param string
	 */
	public void setSubjseqgr(String string) { 
		subjseqgr = string;
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
	public void setYear(String string) { 
		year = string;
	}
	
	/**
	 * @param string
	 */
	public void setCanceldate(String string) { 
		canceldate = string;
	}
	
	/**
	 * @param string
	 */
	public void setCancelkind(String string) { 
		cancelkind = string;
	}
	
	/**
	 * @param string
	 */
	public void setReason(String string) { 
		reason = string;
	}
	
	/**
	 * @param string
	 */
	public void setPropstart(String string) { 
		propstart = string;
	}
	
	/**
	 * @param string
	 */
	public void setPropend(String string) { 
		propend = string;
	}
		
	/**
	 * @param string
	 */
	public void setStudentlimit(int string) { 
		studentlimit = string;
	}

    /**
	 * @param string
	 */
	public void setProcnt(int string) { 
		procnt = string;
	}
	
	/**
	 * @param string
	 */
	public void setCancnt(int string) { 
		cancnt = string;
	}
	
	public void   setDispnum (int dispnum)	{ this.dispnum = dispnum; }
	public int    getDispnum()	{ 	return dispnum;	}	
	
	public void setTotalPageCount(int totalpagecount)	{ this.totalpagecount = totalpagecount; }
	public int    getTotalPageCount()	{ 	return totalpagecount;	}	
	
	public void setRowCount(int rowcount)	{ this.rowcount = rowcount; }
	public int    getRowCount()	{ 	return rowcount;	}		
	
	public void setJikunnm(String jikunnm)	{ this.jikunnm = jikunnm; }
	public String getJikunnm()	{ 	return jikunnm;	}				
	
	public void setCompanynm(String companynm)	{ this.companynm = companynm; }
	public String getCompanynm()	{ 	return companynm;	}				
	
	/**
	 * @param string
	 */
	public void setIsonoff(String string) { 
		isonoff = string;
	}
	
	/**
	 * @return
	 */
	public String getIsonoff() { 
		return isonoff;
	}		
}	