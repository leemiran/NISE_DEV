// **********************************************************
// 1. 제      목: 교육기수관리화면용 DATA
// 2. 프로그램명: GrseqRefData.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: LeeSuMin 2003. 07. 22
// 7. 수      정:
// 
// **********************************************************
package com.ziaan.propose;


public class ApprovalScreenData { 
	private String  course = "" ;
	private String  cyear  = "" ;
	private String  courseseq = "" ;
	private String  coursenm = "" ;	

    private String  grcode      ;
    private String  grcodenm    ;
    private String  grseq       ;
    private String  grseqnm     ;
    private String  gyear       ;
    private String  scsubj      ;
    private String  scsubjnm    ;
    private String  scyear      ;
    private String  scsubjseq   ;
    private String  subjseqgr   ;

    private String  propstart   ;
    private String  propend     ;
    private String  edustart    ;
    private String  eduend      ;
    private String  luserid     ;
    private String  ldate       ;
    private int     point       ;
    private int     biyong      ;
    private int     edulimit    ;
    private int     rowspan =1  ;     // Rowspan

    private String  isonoff     ;
    private String  subjtypenm  ;

    private String  userid;
    private String  name;
    private String position_nm;
    private String lvl_nm;
    private String hometel;
    private String handphone;
    private String email;
    
    
    private String  comp;
    private String  compnm;
    private String  jikwinm;
    private String  cono;
    private String  comptel;
    private String  jik             ;
    private String  jiknm           ;
    private String  appdate         ;
    private String  isdinsert       ;
    private String  isb2c           ;
    private String  ischkfirst      ;
    private String  isproposeapproval;
    private String  useproposeapproval;
    private String  isproposeapprovaltxt;
    private String  useproposeapprovaltxt;
    private String  chkfirst        ;
    private String  chkfinal        ;
    private String  chkfirstTxt     ;
    private String  chkfinalTxt     ;
    private String  proptxt         ;
    private String  chkfirsttxt     ;
    private String  chkfirstmail    ;
    private String  chkfirstuserid  ;
    private String  chkfirstdate    ;
    private String  billstat        ;
    private String  ordcode         ;
    private String  cancelkind      ;
    private String  rejectkind      ;
    private String  rejectedreason  ;
	private String  b_cnt           ;
	private String  isclosed        ;

	private String  mastercd        ;
    private String  masternm        ;
    private String  isedutarget     ;
    private String  companynm       ;	
    private String  deptnm			;
    private String  isgoyong		;
    private String  cpnm		    ;
    
    
    private String HRDC;
    private String ZIP_CD;
    private String ADDRESS;
    private String ZIP_CD1;
    private String ADDRESS1;
    private String USER_PATH;
    private String ISMAILLING;
    private String ISSMS;
    private String  ipayType;
    private String  paycd;
    private String order_id;
    private String cert;
    private String birth_date;
    private String etc;
    
    public String getEtc() {
		return etc;
	}




	public void setEtc(String etc) {
		this.etc = etc;
	}

    
    
    public String getCert() {
		return cert;
	}




	public void setCert(String cert) {
		this.cert = cert;
	}




	public String getbirth_date() {
		return birth_date;
	}




	public void setbirth_date(String birth_date) {
		this.birth_date = birth_date;
	}




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




	public String getIpayType() {
		return ipayType;
	}




	public void setIpayType(String ipayType) {
		this.ipayType = ipayType;
	}
	private String  enterance_dt;
    private String  enter_dt;
    
    
    
    private int  goyongpricemajor;
    private int  goyongpriceminor;
    private int  goyongpricestand;
    private String approvaldate;







	public String getApprovaldate() {
		return approvaldate;
	}




	public void setApprovaldate(String approvaldate) {
		this.approvaldate = approvaldate;
	}




	public String getEnterance_dt() {
		return enterance_dt;
	}




	public void setEnterance_dt(String enterance_dt) {
		this.enterance_dt = enterance_dt;
	}




	public String getEnter_dt() {
		return enter_dt;
	}




	public void setEnter_dt(String enter_dt) {
		this.enter_dt = enter_dt;
	}




	public  ApprovalScreenData() { }


    
    
	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public void setCpnm(String cpnm) {
		this.cpnm = cpnm;
	}
    
    
    
    
    public String getCpnm() {
		return cpnm;
	}
    
    
    
    
    
    public String getCourse() {
		return course;
	}





	public void setCourse(String course) {
		this.course = course;
	}





	public String getCyear() {
		return cyear;
	}





	public void setCyear(String cyear) {
		this.cyear = cyear;
	}





	public String getCourseseq() {
		return courseseq;
	}





	public void setCourseseq(String courseseq) {
		this.courseseq = courseseq;
	}

	

    public String getCoursenm() {
		return coursenm;
	}





	public void setCoursenm(String coursenm) {
		this.coursenm = coursenm;
	}


	public String getGrcode() {
		return grcode;
	}





	public void setGrcode(String grcode) {
		this.grcode = grcode;
	}





	public String getGrcodenm() {
		return grcodenm;
	}





	public void setGrcodenm(String grcodenm) {
		this.grcodenm = grcodenm;
	}





	public String getGrseq() {
		return grseq;
	}





	public void setGrseq(String grseq) {
		this.grseq = grseq;
	}





	public String getGrseqnm() {
		return grseqnm;
	}





	public void setGrseqnm(String grseqnm) {
		this.grseqnm = grseqnm;
	}





	public String getGyear() {
		return gyear;
	}





	public void setGyear(String gyear) {
		this.gyear = gyear;
	}





	public String getScsubj() {
		return scsubj;
	}





	public void setScsubj(String scsubj) {
		this.scsubj = scsubj;
	}





	public String getScsubjnm() {
		return scsubjnm;
	}





	public void setScsubjnm(String scsubjnm) {
		this.scsubjnm = scsubjnm;
	}





	public String getScyear() {
		return scyear;
	}





	public void setScyear(String scyear) {
		this.scyear = scyear;
	}





	public String getScsubjseq() {
		return scsubjseq;
	}





	public void setScsubjseq(String scsubjseq) {
		this.scsubjseq = scsubjseq;
	}





	public String getSubjseqgr() {
		return subjseqgr;
	}





	public void setSubjseqgr(String subjseqgr) {
		this.subjseqgr = subjseqgr;
	}





	public String getPropstart() {
		return propstart;
	}





	public void setPropstart(String propstart) {
		this.propstart = propstart;
	}





	public String getPropend() {
		return propend;
	}





	public void setPropend(String propend) {
		this.propend = propend;
	}





	public String getEdustart() {
		return edustart;
	}





	public void setEdustart(String edustart) {
		this.edustart = edustart;
	}





	public String getEduend() {
		return eduend;
	}





	public void setEduend(String eduend) {
		this.eduend = eduend;
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





	public int getPoint() {
		return point;
	}





	public void setPoint(int point) {
		this.point = point;
	}





	public int getBiyong() {
		return biyong;
	}





	public void setBiyong(int biyong) {
		this.biyong = biyong;
	}





	public int getEdulimit() {
		return edulimit;
	}





	public void setEdulimit(int edulimit) {
		this.edulimit = edulimit;
	}





	public int getRowspan() {
		return rowspan;
	}





	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}





	public String getIsonoff() {
		return isonoff;
	}





	public void setIsonoff(String isonoff) {
		this.isonoff = isonoff;
	}





	public String getSubjtypenm() {
		return subjtypenm;
	}





	public void setSubjtypenm(String subjtypenm) {
		this.subjtypenm = subjtypenm;
	}





	public String getUserid() {
		return userid;
	}





	public void setUserid(String userid) {
		this.userid = userid;
	}





	public String getName() {
		return name;
	}





	public void setName(String name) {
		this.name = name;
	}





	public String getComp() {
		return comp;
	}





	public void setComp(String comp) {
		this.comp = comp;
	}





	public String getCompnm() {
		return compnm;
	}





	public void setCompnm(String compnm) {
		this.compnm = compnm;
	}





	public String getJikwinm() {
		return jikwinm;
	}





	public void setJikwinm(String jikwinm) {
		this.jikwinm = jikwinm;
	}





	public String getCono() {
		return cono;
	}





	public void setCono(String cono) {
		this.cono = cono;
	}





	public String getComptel() {
		return comptel;
	}





	public void setComptel(String comptel) {
		this.comptel = comptel;
	}





	public String getJik() {
		return jik;
	}





	public void setJik(String jik) {
		this.jik = jik;
	}





	public String getJiknm() {
		return jiknm;
	}





	public void setJiknm(String jiknm) {
		this.jiknm = jiknm;
	}





	public String getAppdate() {
		return appdate;
	}





	public void setAppdate(String appdate) {
		this.appdate = appdate;
	}





	public String getIsdinsert() {
		return isdinsert;
	}





	public void setIsdinsert(String isdinsert) {
		this.isdinsert = isdinsert;
	}





	public String getIsb2c() {
		return isb2c;
	}





	public void setIsb2c(String isb2c) {
		this.isb2c = isb2c;
	}





	public String getIschkfirst() {
		return ischkfirst;
	}





	public void setIschkfirst(String ischkfirst) {
		this.ischkfirst = ischkfirst;
	}





	public String getIsproposeapprovaltxt() {
		return isproposeapprovaltxt;
	}





	public void setIsproposeapprovaltxt(String isproposeapprovaltxt) {
		this.isproposeapprovaltxt = isproposeapprovaltxt;
	}	





	public String getUseproposeapprovaltxt() {
		return useproposeapprovaltxt;
	}





	public void setUseproposeapprovaltxt(String useproposeapprovaltxt) {
		this.useproposeapprovaltxt = useproposeapprovaltxt;
	}





	public String getChkfirstTxt() {
		return chkfirstTxt;
	}





	public void setChkfirstTxt(String chkfirstTxt) {
		this.chkfirstTxt = chkfirstTxt;
	}





	public String getChkfinalTxt() {
		return chkfinalTxt;
	}





	public void setChkfinalTxt(String chkfinalTxt) {
		this.chkfinalTxt = chkfinalTxt;
	}





	public String getProptxt() {
		return proptxt;
	}





	public void setProptxt(String proptxt) {
		this.proptxt = proptxt;
	}





	public String getChkfirsttxt() {
		return chkfirsttxt;
	}





	public void setChkfirsttxt(String chkfirsttxt) {
		this.chkfirsttxt = chkfirsttxt;
	}





	public String getChkfirstmail() {
		return chkfirstmail;
	}





	public void setChkfirstmail(String chkfirstmail) {
		this.chkfirstmail = chkfirstmail;
	}





	public String getChkfirstuserid() {
		return chkfirstuserid;
	}





	public void setChkfirstuserid(String chkfirstuserid) {
		this.chkfirstuserid = chkfirstuserid;
	}





	public String getChkfirstdate() {
		return chkfirstdate;
	}





	public void setChkfirstdate(String chkfirstdate) {
		this.chkfirstdate = chkfirstdate;
	}





	public String getBillstat() {
		return billstat;
	}





	public void setBillstat(String billstat) {
		this.billstat = billstat;
	}





	public String getOrdcode() {
		return ordcode;
	}





	public void setOrdcode(String ordcode) {
		this.ordcode = ordcode;
	}





	public String getCancelkind() {
		return cancelkind;
	}





	public void setCancelkind(String cancelkind) {
		this.cancelkind = cancelkind;
	}





	public String getRejectkind() {
		return rejectkind;
	}





	public void setRejectkind(String rejectkind) {
		this.rejectkind = rejectkind;
	}





	public String getRejectedreason() {
		return rejectedreason;
	}





	public void setRejectedreason(String rejectedreason) {
		this.rejectedreason = rejectedreason;
	}





	public String getB_cnt() {
		return b_cnt;
	}





	public void setB_cnt(String b_cnt) {
		this.b_cnt = b_cnt;
	}





	public String getIsclosed() {
		return isclosed;
	}





	public void setIsclosed(String isclosed) {
		this.isclosed = isclosed;
	}





	public String getMastercd() {
		return mastercd;
	}





	public void setMastercd(String mastercd) {
		this.mastercd = mastercd;
	}





	public String getMasternm() {
		return masternm;
	}





	public void setMasternm(String masternm) {
		this.masternm = masternm;
	}





	public String getIsedutarget() {
		return isedutarget;
	}





	public void setIsedutarget(String isedutarget) {
		this.isedutarget = isedutarget;
	}





	public String getCompanynm() {
		return companynm;
	}





	public void setCompanynm(String companynm) {
		this.companynm = companynm;
	}





	public String getDeptnm() {
		return deptnm;
	}





	public void setDeptnm(String deptnm) {
		this.deptnm = deptnm;
	}





	public String getIsgoyong() {
		return isgoyong;
	}





	public void setIsgoyong(String isgoyong) {
		this.isgoyong = isgoyong;
	}





	public int getGoyongpricemajor() {
		return goyongpricemajor;
	}





	public void setGoyongpricemajor(int goyongpricemajor) {
		this.goyongpricemajor = goyongpricemajor;
	}





	public int getGoyongpriceminor() {
		return goyongpriceminor;
	}





	public void setGoyongpriceminor(int goyongpriceminor) {
		this.goyongpriceminor = goyongpriceminor;
	}





	public String getIsproposeapproval() {
		return isproposeapproval;
	}





	public String getUseproposeapproval() {
		return useproposeapproval;
	}





	public String getChkfirst() {
		return chkfirst;
	}





	public String getChkfinal() {
		return chkfinal;
	}





	/**
     * @param string
     */
    public void setChkfinal(String string) { 
        chkfinal = string;

        if ( chkfinal.equals("Y"))   setChkfinalTxt("승인");
        else if ( chkfinal.equals("N"))  setChkfinalTxt("반려");
        else    setChkfinalTxt("미처리");
    }
    


    /**
     * @param string
     */
    public void setIsproposeapproval(String string) { 
        isproposeapproval = string;

        if ( isproposeapproval.equals("Y"))   setIsproposeapprovaltxt("승인");
        else if ( isproposeapproval.equals("N"))  setIsproposeapprovaltxt("반려");
        else if ( isproposeapproval.equals("L"))  setIsproposeapprovaltxt("-");
        else if ( isproposeapproval.equals("B"))  setIsproposeapprovaltxt("상신중");
    }
    

    /**
     * @param string
     */
    public void setUseproposeapproval(String string) { 
        useproposeapproval = string;

        if ( useproposeapproval.equals("Y"))   setUseproposeapprovaltxt("필요");
        else if ( useproposeapproval.equals("N"))  setUseproposeapprovaltxt("불필요");
    }
    
    /**
     * @param string
     */
    public void setChkfirst(String string) { 
        chkfirst = string;
        if ( chkfirst.equals("Y"))   setChkfirstTxt("승인");
        else if ( chkfirst.equals("N"))  setChkfirstTxt("반려");
        else    setChkfirstTxt("미처리");

    }


	public int getGoyongpricestand() {
		return goyongpricestand;
	}


	public void setGoyongpricestand(int goyongpricestand) {
		this.goyongpricestand = goyongpricestand;
	}


	public String getPosition_nm() {
		return position_nm;
	}


	public void setPosition_nm(String position_nm) {
		this.position_nm = position_nm;
	}


	public String getLvl_nm() {
		return lvl_nm;
	}


	public void setLvl_nm(String lvl_nm) {
		this.lvl_nm = lvl_nm;
	}


	public String getHometel() {
		return hometel;
	}


	public void setHometel(String hometel) {
		this.hometel = hometel;
	}


	public String getHandphone() {
		return handphone;
	}


	public void setHandphone(String handphone) {
		this.handphone = handphone;
	}
	

	public void setHRDC(String vHRDC) {
		this.HRDC = vHRDC;
	}
	public void setZIP_CD(String vZIP_CD) {
		this.ZIP_CD = vZIP_CD;
	}
	public void setADDRESS(String vADDRESS) {
		this.ADDRESS = vADDRESS;
	}
	public void setZIP_CD1(String vZIP_CD1) {
		this.ZIP_CD1 = vZIP_CD1;
	}
	public void setADDRESS1(String vADDRESS1) {
		this.ADDRESS1 = vADDRESS1;
	}
	public void setUSER_PATH(String vUSER_PATH) {
		this.USER_PATH = vUSER_PATH;
	}
	public void setISMAILLING(String vISMAILLING) {
		this.ISMAILLING = vISMAILLING;
	}
	public void setISSMS(String vISSMS) {
		this.ISSMS = vISSMS;
	}


    
	public String getHRDC() {
		return HRDC;
	}
	public String getZIP_CD() {
		return ZIP_CD;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public String getZIP_CD1() {
		return ZIP_CD1;
	}
	public String getADDRESS1() {
		return ADDRESS1;
	}
	public String getUSER_PATH() {
		return USER_PATH;
	}
	public String getISMAILLING() {
		return ISMAILLING;
	}
	public String getISSMS() {
		return ISSMS;
	}
}
