// **********************************************************
//  1. 제      목: STUDY STATUS DATA
//  2. 프로그램명: StudyStatusData.java
//  3. 개      요: 학습현황 data bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 박진희 2003. 7. 30
//  7. 수      정:
// **********************************************************
package com.ziaan.study;

public class StudyStatusData { 

	private String grcode;
	private String gyear;
	private String grseq;
	private String grcodenm;
	private String grseqnm;
    private String course;
    private String cyear;
    private String courseseq;
	private String subj;
    private String year;
	private String subjseq;
	private String subjseqgr;
	private String subjnm;
	private String compnm;      /* 소속명  	*/
	private String jikwi;       /* 직위     */
	private String jikwinm;     /* 직위명   */
	private String jikup;       /* 직급     */
	private String jikupnm;     /* 직급명   */
	private String userid ;     /* ID       */
	private String cono;        /* 사번     */
	private String name;   	    /* 이름     */
	private String appdate;	    /* 신청일   */
	private String edustart;    /* 교육 시작일시 	*/
	private String eduend;		/* 교육 종료일시 	*/
	private String chkfirst;    /* 1차 승인 여부 	*/
	private String isproposeapproval;    /* 1차 승인 여부 	*/
	private String chkfinal;    /* 최종 승인 여부 	*/
	private String isonoff;
	private String coursenm;
	private String comptel;
	private String email;
	private String company;
	private String companynm;
    private String isnewcourse;
	private String firstedu;
	private String totaltime;
	private String totalminute;
	private String totalsec;
	private String ldatestart;
	private String ldateend;
	private String classnm;
	private String lesson;
	private String sdesc;
	private String ldate;
	private String projscore;
	private String actscore;
	private String isgraduated;
	private String work_plcnm;
	private String sdate;
    private String tuserid;     /* 강사 아이디       */
    private String tname;       /* 강사 이름         */
//노동부관련 추가
    private String man_cnt;     
    private String woman_cnt;     
    private String age30_cnt;     
    private String age40_cnt;     
    private String age50_cnt;     
    private String age60_cnt;     
    private String school1_cnt;     
    private String school2_cnt;     
    private String school3_cnt;     
    private String school4_cnt;     
    private String school5_cnt;     
    private String title_nm1;     
    private String title_nm2;     
    private String title_nm3;     
    private String title_nm4;     
    private String title_nm5;     
    private String title_nm6;     
    private String title_nm7;     
    private String title_nm8;     

    private String a_cnt;     
    private String b_cnt;     
    private String c_cnt;     
    private String d_cnt;     
    private String e_cnt;     
    private String f_cnt;     
    private String g_cnt;     
    private String total_cnt;     
    
	private int mtest;
	private int ftest;
	private int score;
	private int point;
	private int educnt;
	private int tstep;
	private int avtstep;
	private int report;
	private int act;
	private int etc1;
	private int etc2;
	private int gradcnt;
	private int samtotal;
    private int rowspan;
    private int mcnt;
    private int fcnt;
    private int rcnt;
    
    private double distcode1avg;
    
    private String deptnm;
    
    private int study_count;
    private int study_time;
    
    private String position_nm;
    
    private String isoutsourcing;
    
	public String getIsoutsourcing() {
		return isoutsourcing;
	}

	public void setIsoutsourcing(String isoutsourcing) {
		this.isoutsourcing = isoutsourcing;
	}

	public String getPosition_nm() {
		return position_nm;
	}

	public void setPosition_nm(String position_nm) {
		this.position_nm = position_nm;
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

	public StudyStatusData() { };

    public void   setDistcode1avg(double distcode1avg)      { this.distcode1avg = distcode1avg;     }
    public double getDistcode1avg()                   { return distcode1avg;            }
    
    public void   setGrcode(String grcode)      { this.grcode = grcode;     }
    public String getGrcode()                   { return grcode;            }

    public void   setGyear(String gyear)        { this.gyear = gyear;       }
    public String getGyear()                    { return gyear;             }

    public void   setGrseq(String grseq)        { this.grseq = grseq;       }
    public String getGrseq()                    { return grseq;             }

    public void   setGrcodenm(String grcodenm)  { this.grcodenm = grcodenm; }
    public String getGrcodenm()                 { return grcodenm;          }

    public void   setGrseqnm(String grseqnm)   { this.grseqnm = grseqnm; }
    public String getGrseqnm()                  { return grseqnm;          }

    public void   setCourse(String course)      { this.course = course;     }
    public String getCourse()                   { return course;            }

    public void   setCyear(String cyear)        { this.cyear = cyear;       }
    public String getCyear()                    { return cyear;             }

    public void   setCourseseq(String courseseq) { this.courseseq = courseseq; }
    public String getCourseseq()                { return courseseq;         }

    public void   setSubj(String subj)          { this.subj = subj;         }
    public String getSubj()                     { return subj;              }

    public void   setYear(String year)          { this.year = year;         }
    public String getYear()                     { return year;              }

    public void   setSubjseq(String subjseq)    { this.subjseq = subjseq;   }
    public String getSubjseq()                  { return subjseq;           }

    public void   setSubjseqgr(String subjseqgr)    { this.subjseqgr = subjseqgr;   }
    public String getSubjseqgr()                  { return subjseqgr;           }

    public void   setSubjnm(String subjnm)      { this.subjnm = subjnm;     }
    public String getSubjnm()                   { return subjnm;            }

    public void   setCompnm(String compnm)      { this.compnm = compnm;     }
    public String getCompnm()                   { return compnm;            }

    public void   setJikwi(String jikwi)        { this.jikwi = jikwi;       }
    public String getJikwi()                    { return jikwi;             }

    public void   setJikwinm(String jikwinm)    { this.jikwinm = jikwinm;   }
    public String getJikwinm()                  { return jikwinm;           }

	public void   setJikup(String jikup)        { this.jikup = jikup;       }
	public String getJikup()                    { return jikup;             }

	public void   setJikupnm(String jikupnm)    { this.jikupnm = jikupnm;   }
	public String getJikupnm()                  { return jikupnm;           }

    public void   setUserid(String userid)      { this.userid = userid;     }
    public String getUserid()                   { return userid;            }

    public void   setCono(String cono)          { this.cono = cono;         }
    public String getCono()                     { return cono;              }

    public void   setName(String name)          { this.name = name;         }
    public String getName()                     { return name;              }

    public void   setAppdate(String appdate)    { this.appdate = appdate;   }
    public String getAppdate()                  { return appdate;           }

    public void   setEdustart(String edustart)  { this.edustart = edustart; }
    public String getEdustart()                 { return edustart;          }

    public void   setEduend(String eduend)      { this.eduend = eduend;     }
    public String getEduend()                   { return eduend;            }

    public void   setChkfirst(String chkfirst)  { this.chkfirst = chkfirst; }
    public String getChkfirst()                 { return chkfirst;          }

    public void   setIsproposeapproval(String chkfirst)  { 
//    	this.isproposeapproval = isproposeapproval; 
    }
    public String getIsproposeapproval()                 { return isproposeapproval;          }

    public void   setChkfinal(String chkfinal)  { this.chkfinal = chkfinal; }
    public String getChkfinal()                 { return chkfinal;          }

    public void   setIsonoff(String isonoff)    { this.isonoff = isonoff;   }
    public String getIsonoff()                  { return isonoff;           }

    public void   setCoursenm(String coursenm)  { this.coursenm = coursenm; }
    public String getCoursenm()                 { return coursenm;          }

    public void   setComptel(String comptel)    { this.comptel = comptel;   }
    public String getComptel()                  { return comptel;           }

    public void   setEmail(String email)        { this.email = email;       }
    public String getEmail()                    { return email;             }

    public void   setCompany(String company)    { this.company = company;   }
    public String getCompany()                  { return company;           }

    public void   setCompanynm(String companynm) { this.companynm = companynm; }
    public String getCompanynm()                { return companynm;         }

    public void   setIsnewcourse(String isnewcourse) { this.isnewcourse=isnewcourse; }
    public String getIsnewcourse()              { return isnewcourse;       }

    public void   setFirstedu(String firstedu)  { this.firstedu = firstedu; }
    public String getFirstedu()                 { return firstedu;          }

    public void   setTotaltime(String totaltime) { this.totaltime=totaltime; }
    public String getTotaltime()                { return totaltime;         }

    public void   setTotalminute(String totalminute) { this.totalminute=totalminute; }
    public String getTotalminute()              { return totalminute;       }

    public void   setTotalsec(String totalsec)  { this.totalsec=totalsec;    }
    public String getTotalsec()                 { return totalsec;          }

    public void   setLdatestart(String ldatestart) { this.ldatestart=ldatestart; }
    public String getLdatestart()               { return ldatestart;        }

    public void   setLdateend(String ldateend)  { this.ldateend = ldateend; }
    public String getLdateend()                 { return ldateend;          }

    public void   setClassnm(String classnm)    { this.classnm = classnm;   }
    public String getClassnm()                  { return classnm;           }

    public void   setLesson(String lesson)      { this.lesson = lesson;     }
    public String getLesson()                   { return lesson;            }

    public void   setSdesc(String sdesc)        { this.sdesc = sdesc;       }
    public String getSdesc()                    { return sdesc;             }

    public void   setLdate(String ldate)        { this.ldate = ldate;       }
    public String getLdate()                    { return ldate;             }

    public void   setProjscore(String projscore) { this.projscore=projscore; }
    public String getProjscore()                { return projscore;         }

    public void   setActscore(String actscore)  { this.actscore = actscore; }
    public String getActscore()                 { return actscore;          }

    public void   setWork_plcnm(String work_plcnm) { this.work_plcnm = work_plcnm; }
    public String getWork_plcnm()                  { return work_plcnm;            }

    public void   setSdate(String sdate)        { this.sdate = sdate;      }
    public String getSdate()                    { return sdate;            }

    public void setTuserid(String tuserid)      { this.tuserid = tuserid;  }
    public String getTuserid()                  { return tuserid;          }

    public void setTname(String tname)          { this.tname = tname;      }
    public String getTname()                    { return tname;            }


    public void setMan_cnt(String man_cnt)          { this.man_cnt = man_cnt;      }
    public String getMan_cnt()                    { return man_cnt;            }
    
    public void setWoman_cnt(String woman_cnt)          { this.woman_cnt = woman_cnt;      }
    public String getWoman_cnt()                    { return woman_cnt;            }

    public void setAge30_cnt(String age30_cnt)          { this.age30_cnt = age30_cnt;      }
    public String getAge30_cnt()                    { return age30_cnt;            }

    public void setAge40_cnt(String age40_cnt)          { this.age40_cnt = age40_cnt;      }
    public String getAge40_cnt()                    { return age40_cnt;            }

    public void setAge50_cnt(String age50_cnt)          { this.age50_cnt = age50_cnt;      }
    public String getAge50_cnt()                    { return age50_cnt;            }
 
    public void setAge60_cnt(String age60_cnt)          { this.age60_cnt = age60_cnt;      }
    public String getAge60_cnt()                    { return age60_cnt;            }

    public void setSchool1_cnt(String school1_cnt)          { this.school1_cnt = school1_cnt;      }
    public String getSchool1_cnt()                    { return school1_cnt;            }
    
    public void setSchool2_cnt(String school2_cnt)          { this.school2_cnt = school2_cnt;      }
    public String getSchool2_cnt()                    { return school2_cnt;            }

    public void setSchool3_cnt(String school3_cnt)          { this.school3_cnt = school3_cnt;      }
    public String getSchool3_cnt()                    { return school3_cnt;            }

    public void setSchool4_cnt(String school4_cnt)          { this.school4_cnt = school4_cnt;      }
    public String getSchool4_cnt()                    { return school4_cnt;            }

    public void setSchool5_cnt(String school5_cnt)          { this.school5_cnt = school5_cnt;      }
    public String getSchool5_cnt()                    { return school5_cnt;            }

    public void setTitle_nm1(String title_nm1)          { this.title_nm1 = title_nm1;      }
    public String getTitle_nm1()                    { return title_nm1;            }
    
    public void setTitle_nm2(String title_nm2)          { this.title_nm2 = title_nm2;      }
    public String getTitle_nm2()                    { return title_nm2;            }

    public void setTitle_nm3(String title_nm3)          { this.title_nm3 = title_nm3;      }
    public String getTitle_nm3()                    { return title_nm3;            }

    public void setTitle_nm4(String title_nm4)          { this.title_nm4 = title_nm4;      }
    public String getTitle_nm4()                    { return title_nm4;            }

    public void setTitle_nm5(String title_nm5)          { this.title_nm5 = title_nm5;      }
    public String getTitle_nm5()                    { return title_nm5;            }

    public void setTitle_nm6(String title_nm6)          { this.title_nm6 = title_nm6;      }
    public String getTitle_nm6()                    { return title_nm6;            }

    public void setTitle_nm7(String title_nm7)          { this.title_nm7 = title_nm7;      }
    public String getTitle_nm7()                    { return title_nm7;            }

    public void setTitle_nm8(String title_nm8)          { this.title_nm8 = title_nm8;      }
    public String getTitle_nm8()                    { return title_nm8;            }
    
    public void setA_cnt(String a_cnt)          { this.a_cnt = a_cnt;      }
    public String getA_cnt()                    { return a_cnt;            }

    public void setB_cnt(String b_cnt)          { this.b_cnt = b_cnt;      }
    public String getB_cnt()                    { return b_cnt;            }

    public void setC_cnt(String c_cnt)          { this.c_cnt = c_cnt;      }
    public String getC_cnt()                    { return c_cnt;            }
    
    public void setD_cnt(String d_cnt)          { this.d_cnt = d_cnt;      }
    public String getD_cnt()                    { return d_cnt;            }

    public void setE_cnt(String e_cnt)          { this.e_cnt = e_cnt;      }
    public String getE_cnt()                    { return e_cnt;            }

    public void setF_cnt(String f_cnt)          { this.f_cnt = f_cnt;      }
    public String getF_cnt()                    { return f_cnt;            }

    public void setG_cnt(String g_cnt)          { this.g_cnt = g_cnt;      }
    public String getG_cnt()                    { return g_cnt;            }

    public void setTotal_cnt(String total_cnt)          { this.total_cnt = total_cnt;      }
    public String getTotal_cnt()                    { return total_cnt;            }
   
    
    public void setMtest(int mtest)             { this.mtest = mtest;       }
    public int  getMtest()                      { return mtest;             }

    public void setFtest(int ftest)             { this.ftest = ftest;       }
    public int  getFtest()                      { return ftest;             }

    public void setScore(int score)             { this.score = score;       }
    public int  getScore()                      { return score;             }

    public void setPoint(int point)             { this.point = point;       }
    public int  getPoint()                      { return point;             }

    public void setEducnt(int educnt)           { this.educnt = educnt;     }
    public int  getEducnt()                     { return educnt;            }

    public void setTstep(int tstep)             { this.tstep = tstep;       }
    public int  getTstep()                      { return tstep;             }

    public void setAvtstep(int avtstep)         { this.avtstep = avtstep;   }
    public int  getAvtstep()                    { return avtstep;           }

    public void setReport(int report)           { this.report = report;     }
    public int  getReport()                     { return report;            }

    public void setAct(int act)                 { this.act = act;           }
    public int  getAct()                        { return act;               }
    
    public void setEtc1(int etc1)                 { this.etc1 = etc1;           }
    public int  getEtc1()                        { return etc1;               }
    
    public void setEtc2(int etc2)                 { this.etc2 = etc2;           }
    public int  getEtc2()                        { return etc2;               }

    public void setGradcnt(int gradcnt)         { this.gradcnt = gradcnt;   }
    public int  getGradcnt()                    { return gradcnt;           }

    public void setSamtotal(int samtotal)       { this.samtotal = samtotal;   }
    public int  getSamtotal()                   { return samtotal;           }

    public void setRowspan(int rowspan)         { this.rowspan = rowspan;   }
    public int  getRowspan()                    { return rowspan;           }
    
    public void setMcnt(int mcnt)         { this.mcnt = mcnt;   }
    public int  getMcnt()                    { return mcnt;           }
    
    public void setFcnt(int fcnt)         { this.fcnt = fcnt;   }
    public int  getFcnt()                    { return fcnt;           }
    
    public void setRcnt(int rcnt)         { this.rcnt = rcnt;   }
    public int  getRcnt()                    { return rcnt;           }

    
    
	/**
	 * @return
	 */
	public String getIsgraduated() { 
		return isgraduated;
	}

	/**
	 * @param string
	 */
	public void setIsgraduated(String string) { 
		isgraduated = string;
	}
	
	/**
	 * @return
	 */
	public String getDeptnm() { 
		return deptnm;
	}

	/**
	 * @param string
	 */
	public void setDeptnm(String string) { 
		deptnm = string;
	}

} 