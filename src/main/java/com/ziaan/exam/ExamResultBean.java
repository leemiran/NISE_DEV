
// **********************************************************
// 1. 제      목: 평가 결과조회
// 2. 프로그램명: ExamResultBean.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: Administrator 2003-08-29
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.exam;

import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.system.ManagerAdminBean;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class ExamResultBean { 

    public ExamResultBean() { }

    /**
    평가결과분석
    @param box          receive from the form object and session
    @return ArrayList   
    */
    public ArrayList SelectReaultList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;
//System.out.println("===============평가결과분석box================ " +box);
        String v_action = box.getStringDefault("p_action",  "change");

        String v_grcode    = box.getString("s_grcode");
        String v_gyear     = box.getString("s_gyear");
        String v_grseq     = box.getString("s_grseq");
        String v_upperclass  = box.getStringDefault("s_upperclass", "ALL");
        String v_middleclass = box.getStringDefault("s_middleclass","ALL");
        String v_lowerclass  = box.getStringDefault("s_lowerclass", "ALL");
        String v_uclass    = box.getString("s_uclass");
        String v_subjcourse= box.getString("s_subjcourse");
        String v_subjseq   = box.getString("s_subjseq");
        String v_lesson    = box.getString("s_lesson");
        String v_papernum    = box.getString("s_papernum");

        String v_grpcomp   = box.getString("s_company");

        String v_selgubun  = box.getString("s_selgubun");
        String v_seltext   = box.getString("s_seltext");
        String v_seldept   = box.getString("s_seldept");
        String v_examtype  = box.getStringDefault("s_examtype","0");

        String s_userid    = box.getSession("userid");
        String s_gadmin    = box.getSession("gadmin");

        String  v_orderColumn   = box.getString("p_orderColumn");           	// 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");           		// 정렬할 순서 
           
        try { 
            if ( v_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list = getResultList(connMgr, v_grcode, v_gyear, v_grseq, v_uclass, v_subjcourse, v_subjseq,
                                     v_grpcomp, v_selgubun, v_seltext, v_seldept, v_lesson, s_userid, s_gadmin, v_papernum, v_examtype,
                                     v_orderColumn, v_orderType);

            } else { 
                list = new ArrayList();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    평가결과분석
    @return ArrayList   
    */
    public ArrayList getResultList(DBConnectionManager connMgr,
                    String p_grcode, String p_gyear, String p_grseq, String p_uclass,
                    String p_subjcourse, String p_subjseq, String p_grpcomp,
                    String p_selgubun, String p_seltext, String p_seldept, String p_lesson, String p_userid, String p_gadmin, String p_papernum,String p_examtype,
                    String p_orderColumn, String p_orderType) throws Exception { 

        ArrayList list = new ArrayList();
        ListSet             ls      = null;
        String sql  = "";
        ExamResultData data = null;
        ManagerAdminBean bean = null;
        String  v_sql_add   = "";

        try {                  
            //2008.10.23 sql = "select '' jikwi,      '' jikwinm, d.userid, '' cono,   d.name, ";
        	sql = "select a.isclosed,'' jikwi, '' jikwinm, d.userid, d.name, ";
        	//2008.10.23 sql += "       d.comp asgn,  get_compnm(d.comp,2,2) companynm,  '' asgnnm, ";
            sql += "       d.comp asgn,  get_compnm(d.comp) companynm,  '' asgnnm, ";
            sql += "       b.subj,  b.year,   b.subjseq, b.lesson,  b.examtype,  b.papernum, ";
            sql += "       c.exam, c.examcnt, c.exampoint, c.score, ";
            sql += "		  c.answercnt, c.started, c.ended,  c.time, ";
            sql += "		  c.answer, c.corrected, a.subjnm, a.subjseqgr,";
            sql += "		  nvl(c.userretry,nvl(b.retrycnt,-1) + 1 )  userretry, ";
            // sql += "		  c.userretry-1  userretry, ";
            sql += "		  b.retrycnt, a.eduend, d.handphone, d.hometel, d.email, c.indate ";
            sql += "  from vz_scsubjseq  a, ";
            sql += "       (select a.subj, a.year, a.subjseq, b.lesson, b.examtype, b.papernum, a.userid, b.retrycnt ";
            sql += "          from tz_student    a, ";
            sql += "               tz_exampaper  b  ";
            sql += "         where a.subj = b.subj  ";
            sql += "           and a.year = b.year   ";
            sql += "           and a.subjseq = b.subjseq ) b,  ";
            sql += "       tz_examresult c, ";
            sql += "       tz_member     d, ";
            sql += "       tz_compclass  e  ";
            //sql += "       tz_comp  e  ";
            sql += " where a.subj    = b.subj ";
            sql += "   and a.year    = b.year ";
            sql += "   and a.subjseq = b.subjseq ";
            sql += "   and b.subj    = c.subj( +) ";
            sql += "   and b.year    = c.year( +) ";
            sql += "   and b.subjseq = c.subjseq( +) ";
            sql += "   and b.lesson  = c.lesson( +)  ";
            sql += "   and b.examtype   = c.examtype( +)   ";
            sql += "   and b.papernum= c.papernum( +) ";
            sql += "   and b.userid  = c.userid( +) ";
            sql += "   and b.userid  = d.userid ";
            sql += "   and d.comp    = e.comp ";
            if ( !p_grcode.equals("ALL") ) { 
                sql += " and a.grcode = " + SQLString.Format(p_grcode);
            }
            if ( !p_gyear.equals("ALL") ) { 
                sql += " and a.gyear = " + SQLString.Format(p_gyear);
            }
     //       if ( !p_grseq.equals("ALL") ) { 
     //           sql += " and a.grseq = " + SQLString.Format(p_grseq);
      //      }
      //      if ( !p_uclass.equals("ALL") ) { 
      //          sql += " and a.scupperclass = " + SQLString.Format(p_uclass);
       //     }
            if ( !p_subjcourse.equals("----") && !p_subjcourse.equals("ALL") ) { 
                sql += " and a.scsubj = " + SQLString.Format(p_subjcourse);
            }
            if ( !p_subjseq.equals("----") && !p_subjseq.equals("ALL") ) { 
                sql += " and a.scsubjseq = " + SQLString.Format(p_subjseq);
            }

            // 부서장일경우
      /*      if ( p_gadmin.equals("K7") ) { 
                bean = new ManagerAdminBean();
                v_sql_add   = bean.getManagerDept(p_userid, p_gadmin);
                if ( !v_sql_add.equals("")) sql += " and d.comp in " + v_sql_add;       // 관리부서검색조건쿼리
            }

            if ( p_selgubun.equals("JIKUN") && !p_seltext.equals("ALL"))      {  // 직군별
                sql += " and d.jikun = " +SQLString.Format(p_seltext);
            } else if ( p_selgubun.equals("JIKUP") && !p_seltext.equals("ALL") ) {  // 직급별
                sql += " and d.jikup = " +SQLString.Format(p_seltext);
            } else if ( p_selgubun.equals("GPM") && !p_seltext.equals("ALL") ) {  // 사업부별
                sql += " and d.comp like " +SQLString.Format(GetCodenm.get_compval(p_seltext));
                if ( !p_seldept.equals("ALL") ) {  
                    sql += " and e.comp like " +SQLString.Format(GetCodenm.get_compval(p_seldept));  
                 }
            }

            if ( !p_lesson.equals("ALL") ) { 
                sql += " and b.lesson = " + SQLString.Format(p_lesson);
            }*/
            
            if ( !p_papernum.equals("0") ) {
            	sql += " and b.papernum = " + SQLString.Format(p_papernum);
            }
            // sql += " order by a.subj, a.year, a.subjseq, b.userid ";

			if ( p_orderColumn.equals("") ) { 
            	sql += " order by a.subj, a.year, a.subjseq, b.userid  ";
			} else { 
			    sql += " order by " + p_orderColumn + p_orderType;
			}
//System.out.println("수료 여부sql=========================" +sql);
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                data = new ExamResultData();
                
                data.setIsclosed ( ls.getString("isclosed"));
                data.setSubj   ( ls.getString("subj") );
                data.setYear   ( ls.getString("year") );
                data.setSubjseq( ls.getString("subjseq") );
                data.setSubjseqgr( ls.getString("subjseqgr") );
                data.setLesson( ls.getString("lesson") );
                data.setExamtype  ( ls.getString("examtype") );
                data.setPapernum( ls.getInt("papernum") );
                data.setUserid ( ls.getString("userid") );
                data.setExam ( ls.getString("exam") );
                data.setExamcnt( ls.getInt("examcnt") );
                data.setExampoint( ls.getInt("exampoint") );
                data.setScore( ls.getInt("score") );
                data.setAnswercnt( ls.getInt("answercnt") );
                data.setStarted( ls.getString("started") );
                data.setEnded  ( ls.getString("ended") );
                data.setTime  ( ls.getDouble("time") );
                data.setAnswer ( ls.getString("answer") );
                data.setCorrected ( ls.getString("corrected") );

                data.setSubjnm ( ls.getString("subjnm") );
                data.setCompanynm ( ls.getString("companynm") );    // 회사명                
                data.setAsgnnm ( ls.getString("asgnnm") );
                data.setJikwinm( ls.getString("jikwinm") );
                //data.setCono   ( ls.getString("cono") );
                data.setName   ( ls.getString("name") );
                
                data.setUserretry( ls.getInt("userretry") );
                data.setRetrycnt( ls.getInt("retrycnt") );
                data.setHometel ( ls.getString("hometel") );
                data.setHandphone( ls.getString("handphone") );
                data.setEmail   ( ls.getString("email") );
                data.setEduEnd( ls.getString("eduend") );
                data.setIndate( ls.getString("indate") );
                if ( !(data.getStarted() == null || data.getStarted().equals("")) && (data.getEnded() == null || data.getEnded().equals("")) ) { 
                    data.setStatus("응시(미제출)");
                } else if ( data.getAnswer() == null || data.getAnswer().equals("") ) { 
                    data.setStatus("미응시");
                } else { 
                    data.setStatus("완료");
                }
                list.add(data);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return list;
    }


	/**
	평가자 결과 평균 보기 
	@param 
	@return Vector
	*/
    public Vector SelectResultAverage(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
		Vector v_average = null;

        String v_grcode    = box.getString("s_grcode");
        String v_gyear     = box.getString("s_gyear");
        String v_grseq     = box.getString("s_grseq");
        String  v_upperclass  = box.getStringDefault("s_upperclass", "ALL");
        String  v_middleclass = box.getStringDefault("s_middleclass","ALL");
        String  v_lowerclass  = box.getStringDefault("s_lowerclass", "ALL");
        String v_uclass    = box.getString("s_uclass");
        String v_subjcourse= box.getString("s_subjcourse");
        String v_subjseq   = box.getString("s_subjseq");
        String v_lesson    = box.getString("s_lesson");
        int v_papernum    = box.getInt("s_papernum");

        String v_grpcomp   = box.getString("s_company");

        String v_selgubun  = box.getString("s_selgubun");
        String v_seltext   = box.getString("s_seltext");
        String v_seldept   = box.getString("s_seldept");

        String s_userid    = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            v_average = getAverage(connMgr, v_subjcourse, v_gyear, v_subjseq, v_lesson, v_papernum );
   
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_average;
    }


	/**
	평가자 결과 평균 보기 
	@param 
	@return Vector
	*/
    public Vector SelectResultAverage2(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
		Vector v_average = null;

        String v_grcode    = box.getString("p_grcode");
        String v_gyear     = box.getString("p_gyear");
        String v_grseq     = box.getString("p_grseq");
        String  v_upperclass  = box.getStringDefault("s_upperclass", "ALL");
        String  v_middleclass = box.getStringDefault("s_middleclass","ALL");
        String  v_lowerclass  = box.getStringDefault("s_lowerclass", "ALL");
        String v_uclass    = box.getString("s_uclass");
        String v_subjcourse= box.getString("p_subj");
        String v_subjseq   = box.getString("p_subjseq");
        String v_lesson    = box.getString("p_lesson");
        int v_papernum    = box.getInt("p_papernum");

        String v_grpcomp   = box.getString("s_company");

        String v_selgubun  = box.getString("s_selgubun");
        String v_seltext   = box.getString("s_seltext");
        String v_seldept   = box.getString("s_seldept");

        String s_userid    = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            v_average = getAverage(connMgr, v_subjcourse, v_gyear, v_subjseq, v_lesson, v_papernum );
   
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_average;
    }


	/**
	평가자 결과 평균 구하기  
	@param 
	@return Vector
	*/
    public Vector getAverage(DBConnectionManager connMgr, String p_subjcourse, String p_gyear, String p_subjseq, String p_lesson, int p_papernum ) throws Exception { 

		Vector v_average = null;
		String sql  = "";
        DataBox             dbox    = null;
        ListSet             ls      = null;

        int totalscore =0;
		int examcnt = 0;
		int usercnt = 0;
		double averscore = 0;
		int maxscore = 0;
		int minscore = 0;
		int usercnt1 = 0;
		int usercnt2 = 0;
		int usercnt3 = 0;
		int usercnt4 = 0;
		int usercnt5 = 0;
		int usercnt6 = 0;
		int usercnt7 = 0;
		int usercnt8 = 0;
		int usercnt9 = 0;
		int usercnt10 = 0;

        try { 
			sql += "   select  k.examcnt, k.score  ";
            sql += "    from   tz_examresult k ";
			sql += "    where k.subj = " + SQLString.Format(p_subjcourse);
            sql += "    and k.year = " + SQLString.Format(p_gyear);
            sql += "    and k.subjseq = " + SQLString.Format(p_subjseq);
         //   sql += "    and k.lesson = " + SQLString.Format(p_lesson);
       //     sql += "    and k.papernum = " + SQLString.Format(p_papernum);
            sql += " order by k.subj, k.year, k.subjseq, k.userid ";
 
// System.out.println("평가자 결과 평균 ====="+sql);
 
			ls = connMgr.executeQuery(sql);
            v_average = new Vector();

            while ( ls.next() ) { 

                dbox = ls.getDataBox();
                totalscore += dbox.getInt("d_score");
				examcnt = dbox.getInt("d_examcnt");
				usercnt++;
				if ( dbox.getInt("d_score") > maxscore) { 
				    maxscore = dbox.getInt("d_score");
				}
				if ( dbox.getInt("d_score") < minscore) { 
				    minscore = dbox.getInt("d_score");				
				}

				if ( dbox.getInt("d_score") <= 10 ) { 
					usercnt1++;
				} else if ( dbox.getInt("d_score") > 10 && dbox.getInt("d_score") <= 20 ) { 
					usercnt2++;
				} else if ( dbox.getInt("d_score") > 20 && dbox.getInt("d_score") <= 30 ) { 
					usercnt3++;
				} else if ( dbox.getInt("d_score") > 30 && dbox.getInt("d_score") <= 40 ) { 
					usercnt4++;
				} else if ( dbox.getInt("d_score") > 40 && dbox.getInt("d_score") <= 50 ) { 
					usercnt5++;
				} else if ( dbox.getInt("d_score") > 50 && dbox.getInt("d_score") <= 60 ) { 
					usercnt6++;
				} else if ( dbox.getInt("d_score") > 60 && dbox.getInt("d_score") <= 70 ) { 
					usercnt7++;
				} else if ( dbox.getInt("d_score") > 70 && dbox.getInt("d_score") <= 80 ) { 
					usercnt8++;
				} else if ( dbox.getInt("d_score") > 80 && dbox.getInt("d_score") <= 90 ) { 
					usercnt9++;
				} else if ( dbox.getInt("d_score") > 90 && dbox.getInt("d_score") <= 100 ) { 
					usercnt10++;
				}
			}

            if ( usercnt > 0 ) { 
			averscore = totalscore / usercnt;

            v_average.add(String.valueOf(examcnt));
            v_average.add(String.valueOf(usercnt));
            v_average.add(String.valueOf(averscore));
            v_average.add(String.valueOf(maxscore));
            v_average.add(String.valueOf(minscore));
            v_average.add(String.valueOf(usercnt1));
            v_average.add(String.valueOf(usercnt2));
            v_average.add(String.valueOf(usercnt3));
            v_average.add(String.valueOf(usercnt4));
            v_average.add(String.valueOf(usercnt5));
            v_average.add(String.valueOf(usercnt6));
            v_average.add(String.valueOf(usercnt7));
            v_average.add(String.valueOf(usercnt8));
            v_average.add(String.valueOf(usercnt9));
            v_average.add(String.valueOf(usercnt10));
			}

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_average;
    }


	/**
	평가결과 재응시 리스트 
	@param 
	@return ArrayList
	*/
    public ArrayList SelectUserRetryList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        DataBox             dbox    = null;
        String              sql     = "";
	String v_result = "";
                        
        try { 
        String s_userid     = box.getSession("userid");
        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq     = box.getString("p_subjseq");


			connMgr = new DBConnectionManager();
            
            list = new ArrayList();

            sql = "select b.subj,   a.year,    a.subjseq,   a.lesson, ";
            sql += "       a.examtype,  a.papernum,   a.lessonstart, a.lessonend,  a.examtime, a.exampoint, a.examcnt, a.totalscore,  ";
            sql += "       a.cntlevel1, a.cntlevel2, a.cntlevel3, a.isopenanswer, ";
            sql += "       a.isopenexp,  a.retrycnt,  b.subjnm,  GET_CODENM(" + SQLString.Format(ExamBean.PTYPE) + ", nvl(a.examtype, '')) examtypenm ";
            sql += "  from tz_exampaper a, ";
            sql += "       tz_subj       b  ";
            sql += " where a.subj( +)        = b.subj  ";
            sql += "   and a.subj   = " + SQLString.Format(v_subj);
            sql += "   and a.year   = " + SQLString.Format(v_year);
            sql += "   and a.subjseq   = " + SQLString.Format(v_subjseq);
//            sql += "   and rownum <= 1 ";
            sql += " order by a.subj, a.year, a.subjseq, a.lesson, a.examtype  ";

            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
          //      System.out.println("retrycnt : "  + dbox.getInt("d_retrycnt") );
                if ( dbox.getInt("d_retrycnt") != 0 ) { 
                    v_result = String.valueOf(SelectUserRetryList(connMgr, v_subj, v_year, v_subjseq, s_userid, dbox.getString("d_examtype"), dbox.getInt("d_papernum")));
                }
                else { 
                    v_result = "X";         //      재응시가 없는 평가
                }
           //     System.out.println(dbox.getString("d_examtype") + " : "  + v_result);
                list.add(String.valueOf(v_result));
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


	/**
	평가자  재응시횟수 구하기 
	@param 
	@return int
	*/
    public int SelectUserRetryList(DBConnectionManager connMgr,
                    String p_subj, String p_year, String p_subjseq, String p_userid, String p_examtype, int p_papernum) throws Exception { 

        ArrayList QuestionExampleDataList  = null;
        int v_result = -1;
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;


        try { 
            sql = "select userretry ";
            sql += "  from tz_examresult  ";
            sql += " where subj = " + SQLString.Format(p_subj);
            sql += " and year = " + SQLString.Format(p_year);
            sql += " and subjseq = " + SQLString.Format(p_subjseq);
            sql += " and examtype = " + SQLString.Format(p_examtype);
            sql += " and userid = " + SQLString.Format(p_userid);
            sql += " and papernum = " + SQLString.Format(p_papernum);
// System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox = ls.getDataBox();
                v_result = dbox.getInt("d_userretry");
			}
		
		} catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_result;
    }


    
		/**
		대상자 설문지 셀렉트박스 (RequestBox, 셀렉트박스명,선택값,이벤트명) TZ_SULPAPER 이용
		@param 
		@return String
		*/
    public static String getPaperSelect (String p_subj, String p_gyear, String p_subjseq, String p_lesson, String name, int selected, String event) throws Exception { 

        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String result = null;
        String              sql     = "";
        DataBox     dbox = null;

        result = "  <SELECT name=" + name + " " + event + " > \n";

        result += " <option value='0' > 평가를 선택하세요.</option > \n";

        try { 
            connMgr = new DBConnectionManager();

            sql = "select b.subj,   a.year,    a.subjseq,   a.lesson, ";
            sql += "       a.examtype,  a.papernum,   a.lessonstart, a.lessonend,  a.examtime, a.exampoint, a.examcnt, a.totalscore,  ";
            sql += "       a.cntlevel1, a.cntlevel2, a.cntlevel3, a.isopenanswer, ";
            sql += "       a.isopenexp,  a.retrycnt,  b.subjnm,  GET_CODENM(" + SQLString.Format(ExamBean.PTYPE) + ", nvl(a.examtype, '')) examtypenm ";
            sql += "  from tz_exampaper a, ";
            sql += "       tz_subj       b  ";
            sql += " where a.subj( +)        = b.subj  ";
            sql += "   and a.subj   = " + SQLString.Format(p_subj);
            sql += "   and a.year   = " + SQLString.Format(p_gyear);
            sql += "   and a.subjseq   = " + SQLString.Format(p_subjseq);
   //         sql += "   and a.lesson   = " + SQLString.Format(p_lesson);
            sql += " order by a.subj, a.year, a.subjseq, a.lesson, a.examtype, a.papernum asc  ";

            System.out.println(sql);
            
            ls = connMgr.executeQuery(sql);

                String v_null_test = "";
                String v_subj_bef = "";

            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                result += " <option value=" + dbox.getInt("d_papernum");
                if ( selected == dbox.getInt("d_papernum") ) { 
                    result += " selected ";
                }
                
                result += " > " + dbox.getString("d_examtypenm") + " [" + dbox.getString("d_papernum") + "]" + "</option > \n";
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        result += "  </SELECT > \n";
        return result;
    } 
    
	/**
	대상자 설문지 셀렉트박스 (RequestBox, 셀렉트박스명,선택값,이벤트명) TZ_SULPAPER 이용
	@param 
	@return String
	*/
    public static String getPaperTypeSelect (String p_subj, String p_gyear, String p_subjseq, String p_lesson, String name, String selected, String event) throws Exception { 
    	
    	DBConnectionManager	connMgr	= null;
    	ListSet             ls      = null;
    	String result = null;
    	String              sql     = "";
    	DataBox     dbox = null;
    	
    	result = "  <SELECT name=" + name + " " + event + " > \n";
    	
    	result += " <option value='0' > 평가를 선택하세요.</option > \n";
    	
    	try { 
    		connMgr = new DBConnectionManager();
    		
    		sql = "select b.subj,   a.year,    a.subjseq,   a.lesson, ";
    		sql += "       a.examtype,  a.papernum,   a.lessonstart, a.lessonend,  a.examtime, a.exampoint, a.examcnt, a.totalscore,  ";
    		sql += "       a.cntlevel1, a.cntlevel2, a.cntlevel3, a.isopenanswer, ";
    		sql += "       a.isopenexp,  a.retrycnt,  b.subjnm,  GET_CODENM(" + SQLString.Format(ExamBean.PTYPE) + ", nvl(a.examtype, '')) examtypenm ";
    		sql += "  from tz_exampaper a, ";
    		sql += "       tz_subj       b  ";
    		sql += " where a.subj( +)        = b.subj  ";
    		sql += "   and a.subj   = " + SQLString.Format(p_subj);
    		sql += "   and a.year   = " + SQLString.Format(p_gyear);
    		sql += "   and a.subjseq   = " + SQLString.Format(p_subjseq);
//         sql += "   and a.lesson   = " + SQLString.Format(p_lesson);
    		sql += " order by a.subj, a.year, a.subjseq, a.lesson, a.examtype, a.papernum asc  ";
    		
    		System.out.println(sql);
    		
    		ls = connMgr.executeQuery(sql);
    		
    		String v_null_test = "";
    		String v_subj_bef = "";
    		
    		while ( ls.next() ) { 
    			dbox = ls.getDataBox();
    			
    			result += " <option value=" + dbox.getString("d_examtype");
    			if ( selected.equals(dbox.getString("d_examtype"))) { 
    				result += " selected ";
    			}
    			
    			result += " > " + dbox.getString("d_examtypenm") + " [" + dbox.getString("d_papernum") + "]" + "</option > \n";
    		}
    	} catch ( Exception ex ) { 
    		ErrorManager.getErrorStackTrace(ex);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	} finally { 
    		if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
    		if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	}
    	
    	result += "  </SELECT > \n";
    	return result;
    } 
}