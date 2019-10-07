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

public class CompleteStatusData1 {       
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
    private String  courseisgraduated     ;
    private String  email           ;
    private String  ismailing       ;
    private String  isnewcourse     ;    
    private String  place           ;    
    private String  birth_date           ;
    private String  serno           ;
    private String  codenm          ;
    private String  biyong          ;
    private String  goyongpricemajor          ;
    private String  goyongpriceminor          ;
    private String  className       ;
    private String  grseqNm         ;
    private String  isgoyong         ;
        
    private double     tstep           ;
    private double     avtstep         ;
    private double     mtest           ;
    private double     ftest           ;
	private double     htest           ;  
    private double     report          ;
    private double     act             ;
    private double     etc1            ;
    private double     etc2            ;
    private double     score           ;
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
    
    private double  distcode1avg   ;
    private String  deptnm          ;
    
    private int study_count;
    private int study_time;
    
    public CompleteStatusData1() { };
 
    public void 	setDistcode1avg(double distcode1avg)  	{ this.distcode1avg = distcode1avg; }
    public double 	getDistcode1avg()                		{ return distcode1avg;   			}

    
    public double getEtc2() {
		return etc2;
	}

	public void setEtc2(double etc2) {
		this.etc2 = etc2;
	}

	public int getStudy_count() {
		return study_count;
	}

	public void setStudy_count(int study_count) {
		this.study_count = study_count;
	}

	public int getStudy_time() {
		return study_time;
	}

	public void setStudy_time(int study_time) {
		this.study_time = study_time;
	}

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
    
    //
    public void setIsGoyong(String string) { 
    	isgoyong = string;
    }
    
    /**
     * @return
     */
    public String getIsGoyong() { 
    	return isgoyong;
    }
    //
        
           
    /**
     * @param string
     */
    public void setPlace(String string) { 
        place = string;
    }
        
    /**
     * @return
     */
    public double getAct() { 
        return act;
    }

    /**
     * @return
     */
    public double getAvtstep() { 
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
    public double getEtc1() { 
        return etc1;
    }

    /**
     * @return
     */
    public double getFtest() { 
        return ftest;
    }
	
    /**
     * @return
     */
    public double getHtest() { 
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
    public String getCourseIsgraduated() { 
        return courseisgraduated;
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
    public double getMtest() { 
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
    public double getReport() { 
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
    public double getScore() { 
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
    public double getTstep() { 
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
    public void setAct(double i) { 
        act = i;
    }

    /**
     * @param i
     */
    public void setAvtstep(double i) { 
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
    public void setEtc1(double i) { 
        etc1 = i;
    }

    /**
     * @param i
     */
    public void setFtest(double i) { 
        ftest = i;
    }
	
    /**
     * @param i
     */
    public void setHtest(double i) { 
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
    public void setCourseIsgraduated(String string) { 
        courseisgraduated = string;
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
    public void setMtest(double i) { 
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
    public void setReport(double i) { 
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
    public void setScore(double i) { 
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
    public void setTstep(double i) { 
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
    
    public void     setDeptnm(String deptnm)          		{ this.deptnm = deptnm;           	 }
    public String   getDeptnm()                          	{ return deptnm;                     }
}
