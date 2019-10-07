// **********************************************************
//  1. 제      목: COMPLETE STATUS DATA
//  2. 프로그램명: CompleteStatusData.java
//  3. 개      요: 수료 현황 관리자 data bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 박진희 2003. 8. 21
//  7. 수      정:
// **********************************************************
package com.ziaan.complete;

public class CompleteStatusData {       
    private String  grseq           ;
    private String  course          ;
    private String  cyear           ;
    private String  courseseq       ;
    private String  coursenm        ;
    private String  subj            ;
    private String  year            ;
    private String  subjseq         ;
    private String  subjseqgr       ;
    private String  subjnm          ;
    private String  isonoff         ;
    private String  compnm          ; 
    private String  companynm       ;
    private String  jikwinm         ;
    private String  jikupnm         ;
    private String  userid          ;
    private String  cono            ;   
    private String  name            ; 
    private String  edustart        ;   
    private String  eduend          ;  
    private String  isgraduated     ;
    private String  email           ;
    private String  ismailing       ;
    private String  isnewcourse     ;    
    private String  place           ;    
    private String  birth_date           ;
    private String  serno           ;
    private String  codenm          ;
    private String  className       ;
    private String  grseqNm         ;
        
    private int     tstep           ;
    private int     avtstep         ;
    private int     mtest           ;
    private int     ftest           ;
	private int     htest           ;  
    private int     report          ;
    private int     act             ;
    private int     etc1            ;
    private int     score           ;
    private int     educnt          ;
    private int     oldeducnt       ;   // 전기수 수강생 수
    private int     gradcnt1        ;
    private int     oldgradcnt      ;   // 전기수 수료생 수
    private int     gradcnt2        ;
    private int     rowspan         ;
    private int     dispnum         ;
    private int     totalpagecount  ;
    private int     rowcount        ;    
    private int     edudays         ;
    private int     edutimes        ;
    private int     goyongprice     ;
    private String  hometel         ;
    private String  handphone       ;
    
    private double  distcode1avg    ;
    
    private String  tuserid         ;	// 강사ID
    private String  tname           ;	// 강사명
    private int     tincome         ;	// 강사료
    private int     edudate         ;	// 교육일수
    private String  tpositionnm		;	// 강사소속
    private String  taccount		;	// 계좌번호
    private String  tbank			;	// 계좌은행
    private String  cmuserid		;	// CM ID
    private String  cmname			;	// CM명
    private String  subjclassnm	;	// 과정분류명
    
    public CompleteStatusData() { };
 
    public void 	setDistcode1avg(double distcode1avg)  	{ this.distcode1avg = distcode1avg; }
    public double 	getDistcode1avg()                		{ return distcode1avg;   			}

    /**
     * @param string
     */
    public void setGrseqNm(String string) { 
        grseqNm = string;
    }
        
    /**
     * @return
     */
    public String getGrseqNm() { 
        return grseqNm;
    }
    
    /**
     * @param string
     */
    public void setClassName(String string) { 
        className = string;
    }
        
    /**
     * @return
     */
    public String getClassName() { 
        return className;
    }

     /**
     * @param string
     */
    public void setCodenm(String string) { 
        codenm = string;
    }
        
    /**
     * @return
     */
    public String getCodenm() { 
        return codenm;
    }
        
           
    /**
     * @param string
     */
    public void setPlace(String string) { 
        place = string;
    }
        
    /**
     * @return
     */
    public int getAct() { 
        return act;
    }

    /**
     * @return
     */
    public int getAvtstep() { 
        return avtstep;
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
    public int getEducnt() { 
        return educnt;
    }

    /**
     * @return
     */
    public int getOldEducnt() { 
        return oldeducnt;
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
    public int getEtc1() { 
        return etc1;
    }

    /**
     * @return
     */
    public int getFtest() { 
        return ftest;
    }
	
    /**
     * @return
     */
    public int getHtest() { 
        return htest;
    }

    /**
     * @return
     */
    public int getGradcnt1() { 
        return gradcnt1;
    }

    /**
     * @return
     */
    public int getOldGradcnt() { 
        return oldgradcnt;
    }
    
    /**
     * @return
     */
    public int getGradcnt2() { 
        return gradcnt2;
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
    public String getIsgraduated() { 
        return isgraduated;
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
    public String getIsonoff() { 
        return isonoff;
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
    public String getPlace() { 
        return place;
    }


    /**
     * @return
     */
    public int getMtest() { 
        return mtest;
    }

    /**
     * @return
     */
    public String getName() { 
        return name;
    }

    /**
     * @return
     */
    public int getReport() { 
        return report;
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
    public int getScore() { 
        return score;
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
    public int getTstep() { 
        return tstep;
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
     * @param i
     */
    public void setAct(int i) { 
        act = i;
    }

    /**
     * @param i
     */
    public void setAvtstep(int i) { 
        avtstep = i;
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
     * @param i
     */
    public void setEducnt(int i) { 
        educnt = i;
    }

    /**
     * @param i
     */
    public void setOldEducnt(int i) { 
        oldeducnt = i;
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
     * @param i
     */
    public void setEtc1(int i) { 
        etc1 = i;
    }

    /**
     * @param i
     */
    public void setFtest(int i) { 
        ftest = i;
    }
	
    /**
     * @param i
     */
    public void setHtest(int i) { 
        htest = i;
    }

    /**
     * @param i
     */
    public void setGradcnt1(int i) { 
        gradcnt1 = i;
    }

    /**
     * @param i
     */
    public void setOldGradcnt(int i) { 
        oldgradcnt = i;
    }
    
    /**
     * @param i
     */
    public void setGradcnt2(int i) { 
        gradcnt2 = i;
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
    public void setIsgraduated(String string) { 
        isgraduated = string;
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
    public void setIsonoff(String string) { 
        isonoff = string;
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
     * @param i
     */
    public void setMtest(int i) { 
        mtest = i;
    }

    /**
     * @param string
     */
    public void setName(String string) { 
        name = string;
    }

    /**
     * @param i
     */
    public void setReport(int i) { 
        report = i;
    }

    /**
     * @param i
     */
    public void setRowspan(int i) { 
        rowspan = i;
    }

    /**
     * @param i
     */
    public void setScore(int i) { 
        score = i;
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
     * @param i
     */
    public void setTstep(int i) { 
        tstep = i;
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
    
    public void     setDispnum (int dispnum)                { this.dispnum = dispnum;               }
    public int      getDispnum()                            {   return dispnum;                     }   
    
    public void     setTotalPageCount(int totalpagecount)   { this.totalpagecount = totalpagecount; }
    public int      getTotalPageCount()                     {   return totalpagecount;              }   
    
    public void     setRowCount(int rowcount)               { this.rowcount = rowcount;             }
    public int      getRowCount()                           {   return rowcount;                    }
    
    public void     setbirth_date(String birth_date)                  { this.birth_date = birth_date;                   }
    public String   getbirth_date()                              {   return birth_date;                       }   
    
    public void     setSerno(String serno)                  { this.serno = serno;                   }
    public String   getSerno()                              {   return serno;                       }
    
    public void     setEdudays(int edudays)                 { this.edudays = edudays;               }
    public int      getEdudays()                            {   return edudays;                     }
        
    public void     setEdutimes(int edutimes)               { this.edutimes = edutimes;             }
    public int      getEdutimes()                           { return edutimes;                      }

    public void     setGoyongprice(int goyongprice)         { this.goyongprice = goyongprice;       }
    public int      getGoyongprice()                        {   return goyongprice;                 }

    public void     setCompanynm(String companynm)          { this.companynm = companynm;           }
    public String   getCompanynm()                          { return companynm;                     }
    
    public void     setHometel(String hometel)              { this.hometel = hometel;               }
    public String   getHometel()                            { return hometel;                       }

    public void     setHandphone(String handphone)          { this.handphone = handphone;           }
    public String   getHandphone()                          { return handphone;                     }

	public String getTuserid() {
		return tuserid;
	}

	public void setTuserid(String tuserid) {
		this.tuserid = tuserid;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public int getTincome() {
		return tincome;
	}

	public void setTincome(int tincome) {
		this.tincome = tincome;
	}

	public int getEdudate() {
		return edudate;
	}

	public void setEdudate(int edudate) {
		this.edudate = edudate;
	}

	public int getTotalpagecount() {
		return totalpagecount;
	}

	public void setTotalpagecount(int totalpagecount) {
		this.totalpagecount = totalpagecount;
	}

	public String getTpositionnm() {
		return tpositionnm;
	}

	public void setTpositionnm(String tpositionnm) {
		this.tpositionnm = tpositionnm;
	}

	public String getTbank() {
		return tbank;
	}

	public void setTbank(String tbank) {
		this.tbank = tbank;
	}

	public String getCmuserid() {
		return cmuserid;
	}

	public void setCmuserid(String cmuserid) {
		this.cmuserid = cmuserid;
	}

	public String getCmname() {
		return cmname;
	}

	public void setCmname(String cmname) {
		this.cmname = cmname;
	}

	public String getTaccount() {
		return taccount;
	}

	public void setTaccount(String taccount) {
		this.taccount = taccount;
	}

	public String getSubjclassnm() {
		return subjclassnm;
	}

	public void setSubjclassnm(String subjclassnm) {
		this.subjclassnm = subjclassnm;
	}
}
