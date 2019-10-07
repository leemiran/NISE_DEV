// **********************************************************
// 1. 제      목:
// 2. 프로그램명: SulmunSubjUserBean.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: Administrator 2003-08-18
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.research;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class SulmunSubjUserBean { 


    public SulmunSubjUserBean() { }

	  /**
    과목 설문 등록
    @param box          receive from the form object and session
    @return int
    */
    public int InsertSulmunUserResult(RequestBox box) throws Exception { 
  
        DBConnectionManager	connMgr	= null;        
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        ListSet             ls      = null;
        String sql  = "";
        String sql1 = "";
        String sql2 = "";
        int isOk = 2;
        int isOk1 = 0;

		String v_grcode     = box.getString("p_grcode");
        String v_subj       = box.getString("p_subj");
        String v_gyear       = box.getString("p_gyear");
        String v_subjseq    = box.getString("p_subjseq");
        int    v_sulpapernum= box.getInt("p_sulpapernum");
        //String v_userid     = box.getString("p_userid");
        String v_userid     = box.getSession("userid");
        String v_sulnums    = box.getString("p_sulnums"); // 문제조합
		String v_answers    = box.getString("p_answers"); // 정답조합
        String v_luserid    = box.getSession("userid");
		int i_code1 = 0;  // 과목만족도
		int i_code2 = 0;  // 내용이해도
		int i_code3 = 0;  // 과목난이도
		int i_code4 = 0;  // 업무활용도
		int i_code5 = 0;  // 질문대응  
		int i_code6 = 0;  // 장애대응  
		int i_code7 = 0;  // 강사만족도
		int i_code8 = 0;  // 기타
		double i_code1_avg = 0;
		double i_code2_avg = 0;
		double i_code3_avg = 0;	
		double i_code4_avg = 0;
		double i_code5_avg = 0;
		double i_code6_avg = 0;			
		double i_code7_avg = 0;			
		double i_code8_avg = 0;			
		int i_code1_cnt = 0;
		int i_code2_cnt = 0;
		int i_code3_cnt = 0;					
		int i_code4_cnt = 0;
		int i_code5_cnt = 0;
		int i_code6_cnt = 0;		
		int i_code7_cnt = 0;		
		int i_code8_cnt = 0;		
		String s_distcode10 = "";	// 소감	

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

			// 설문기간 확인
            //   isOk = getSulmunGigan(box);

			// 설문 분류별로 척도점수 합계
			String[] arr_sulnums = v_sulnums.split("\\,");
			String[] arr_answers = v_answers.split("\\,");
			
            for ( int i = 0; i<arr_sulnums.length; i++ ) { 
                // 문제당 분류코드 
				String ss_distcode = "";
			    sql = " select distcode from TZ_SUL where subj='ALL' AND GRCODE='ALL' and sulnum = ? ";
                
				pstmt1 = connMgr.prepareStatement(sql);   
				pstmt1.setInt(1, Integer.parseInt(arr_sulnums[i]));
				rs = pstmt1.executeQuery();
				if ( rs.next() ) { 
					ss_distcode = rs.getString("distcode");
				}
				if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }    
				if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }    

                // 10.소감
				if ( ss_distcode.equals("10") ) { 
                    s_distcode10 = arr_answers[i];                
                }
                
                // 객관식 답 
                int i_arr_answers = 0;
                try { 
                    i_arr_answers = Integer.parseInt(arr_answers[i]);
                } catch ( Exception e ) { }

                if ( i_arr_answers != 0) { 
                 
                    // 문제,보기당 보기점수 
    				int ss_selpoint = 0;                
    			    sql = " select selpoint from TZ_SULSEL where subj='ALL' AND GRCODE='ALL' AND SULNUM=? AND SELNUM=?  ";
    				pstmt1 = connMgr.prepareStatement(sql);   
    				pstmt1.setInt(1, Integer.parseInt(arr_sulnums[i]));
    				pstmt1.setInt(2, i_arr_answers);
                    
    				rs = pstmt1.executeQuery();
    				if ( rs.next() ) { 
    					ss_selpoint = rs.getInt("selpoint");
    				}
    				if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }    
    				if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }    
    				
    				// 계산
    				if ( ss_distcode.equals("1") ) { 
    				    i_code1 +=ss_selpoint;
    				    i_code1_cnt++;
    				}    
    				if ( ss_distcode.equals("2") ) { 
    				    i_code2 +=ss_selpoint;
    				    i_code2_cnt++;
    				}
    				if ( ss_distcode.equals("3") ) { 
    				    i_code3 +=ss_selpoint;
    				    i_code3_cnt++;
    				}
    				if ( ss_distcode.equals("4") ) { 
    				    i_code4 +=ss_selpoint;
    				    i_code4_cnt++;
    				}    
    				if ( ss_distcode.equals("5") ) { 
    				    i_code5 +=ss_selpoint;
    				    i_code5_cnt++;
    				}
    				if ( ss_distcode.equals("6") ) { 
    				    i_code6 +=ss_selpoint;
    				    i_code6_cnt++;
    				}	
    				if ( ss_distcode.equals("7") ) { 
    				    i_code7 +=ss_selpoint;
    				    i_code7_cnt++;
    				}
    				if ( ss_distcode.equals("8") ) { 
    				    i_code8 +=ss_selpoint;
    				    i_code8_cnt++;
    				}
    			}
									
			}
			// for문종료
			
			if ( i_code1_cnt != 0)i_code1_avg = i_code1/(double)i_code1_cnt;
			if ( i_code2_cnt != 0)i_code2_avg = i_code2/(double)i_code2_cnt; 
			if ( i_code3_cnt != 0)i_code3_avg = i_code3/(double)i_code3_cnt; 
			if ( i_code4_cnt != 0)i_code4_avg = i_code4/(double)i_code4_cnt;
			if ( i_code5_cnt != 0)i_code5_avg = i_code5/(double)i_code5_cnt; 
			if ( i_code6_cnt != 0)i_code6_avg = i_code6/(double)i_code6_cnt; 
			if ( i_code7_cnt != 0)i_code7_avg = i_code7/(double)i_code7_cnt; 			
			if ( i_code8_cnt != 0)i_code8_avg = i_code8/(double)i_code8_cnt; 			

			if ( isOk == 2) { 

				sql1 = "select userid from TZ_SULEACH";
				sql1 +=  " where subj = ? and grcode = ? and year = ? and subjseq = ? and sulpapernum = ?  and  userid = ? ";
                
				pstmt1 = connMgr.prepareStatement(sql1);   
				
				pstmt1.setString( 1, v_subj);      
				pstmt1.setString( 2, v_grcode);       
				pstmt1.setString( 3, v_gyear);    
				pstmt1.setString( 4, v_subjseq);
				pstmt1.setInt( 5, v_sulpapernum);
				pstmt1.setString( 6, v_userid);

                try { 
                    rs = pstmt1.executeQuery();

                    if ( !rs.next() ) {     //     과거에 등록된 userid 를 확인하고 없을 경우에만 등록
                        isOk1 = InsertTZ_suleach(connMgr, v_subj , v_grcode, v_gyear, v_subjseq, v_sulpapernum, v_userid, v_sulnums, v_answers, v_luserid, 
                                                 i_code1, i_code2, i_code3, i_code4, i_code5, i_code6, i_code7, i_code8, i_code1_avg, i_code2_avg, i_code3_avg, i_code4_avg, i_code5_avg, i_code6_avg,  i_code7_avg, i_code8_avg, s_distcode10);
                        
                    } else { 
                      isOk1 = 4;  // 이미 설문에 응시하였습니다.
                    }

                } catch ( Exception e ) { }
                finally { if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }}           

			}

       } catch ( Exception ex ) { 
            isOk1 = 0;
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( isOk1 > 0 ) { connMgr.commit(); }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }      
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1;
    }


    /**
    설문기간 확인 
    @param box          receive from the form object and session
    @return int   
    */  
    public int getSulmunGigan(RequestBox box) throws Exception { 

		SulmunSubjPaperBean bean = null;
		DataBox  dbox0 = null; 

        String v_sulstart = "";
        String v_sulend = "";
		int v_update = 0;

        try { 
			bean = new SulmunSubjPaperBean();
			dbox0 = bean.getPaperData(box);

            v_sulstart = FormatDate.getFormatDate(dbox0.getString("d_sulstart"),"yyyy.MM.dd");
            v_sulend = FormatDate.getFormatDate(dbox0.getString("d_sulend"),"yyyy.MM.dd");
		    
			if ( dbox0.getInt("d_sulpapernum") > 0) { 
          
		      long v_fstart = Long.parseLong(dbox0.getString("d_sulstart") );
              long v_fend = Long.parseLong(dbox0.getString("d_sulend") );
              
                 java.util.Date d_now = new java.util.Date();
                 String d_year = String.valueOf(d_now.getYear() +1900);
				 String d_month = String.valueOf(d_now.getMonth() +1);
				 String d_day = String.valueOf(d_now.getDate() );

                 if ( d_month.length() == 1) { 
				        d_month = "0" + d_month; 
				 }
				 if ( d_day.length() == 1) { 
				        d_day = "0" + d_day; 				 
				 }
		         long v_now = Long.parseLong(d_year +d_month +d_day); 

		         if ( v_fstart > v_now) { 
					v_update = 1;                                         // 설문 전
		         } else if ( v_now > v_fend) { 
					v_update = 3;                                         // 설문 완료
		         } else if ( v_fstart <= v_now && v_now < v_fend) { 
					v_update = 2;                                          // 설문 중
		         }
			}
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }

        return v_update;

	}

	  /**
    과목 설문 등록
    @param box          receive from the form object and session
    @return int
    */
    public int InsertTZ_suleach(DBConnectionManager connMgr, String p_subj,  String p_grcode,  String p_gyear,    String p_subjseq, int p_sulpapernum,
                            String p_userid, String  p_sulnums, String p_answers, String p_luserid, 
                            int i_code1, int i_code2, int i_code3, int i_code4, int i_code5,int i_code6,int i_code7, int i_code8,
                            double i_code1_avg, double i_code2_avg, double i_code3_avg, double i_code4_avg, double i_code5_avg, double i_code6_avg, double i_code7_avg, double i_code8_avg, String s_distcode10) throws Exception { 

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // insert TZ_SULEACH table
            sql =  "insert into TZ_SULEACH ";
            sql +=  " (subj,  grcode,   year,    subjseq, sulpapernum, ";
            sql +=  "  userid,  sulnums, answers,  luserid, ldate,     ";
            sql +=  "  distcode1, distcode2, distcode3, distcode4, distcode5, distcode6, distcode7, distcode8, ";
            sql +=  "  distcode1_avg, distcode2_avg, distcode3_avg , distcode4_avg, distcode5_avg, distcode6_avg , distcode7_avg, distcode8_avg, distcode10 ";
            sql +=  " )values( ";
            sql +=  "  ?,       ?,       ?,       ?,      ?,                                       ";
            sql +=  "  ?,       ?,       ?,       ?,      ?,                                       ";
            sql +=  "  ?,       ?,       ?,       ?,      ?,      ?,       ?,      ? ,             ";
            sql +=  "  ?,       ?,       ?,       ?,      ?,      ?,       ?,      ? ,    ? )      ";            
            
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, p_subj);
            pstmt.setString( 2, p_grcode);
			pstmt.setString( 3, p_gyear);
            pstmt.setString( 4, p_subjseq);
            pstmt.setInt   ( 5, p_sulpapernum);
            pstmt.setString( 6, p_userid);
            pstmt.setString( 7, p_sulnums);
            pstmt.setString( 8, p_answers);
            pstmt.setString( 9, p_luserid);
            pstmt.setString(10, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setInt   (11, i_code1);
            pstmt.setInt   (12, i_code2);
            pstmt.setInt   (13, i_code3);
            pstmt.setInt   (14, i_code4);
            pstmt.setInt   (15, i_code5);
            pstmt.setInt   (16, i_code6);            
            pstmt.setInt   (17, i_code7);            
            pstmt.setInt   (18, i_code8);            
            pstmt.setDouble(19, i_code1_avg);
            pstmt.setDouble(20, i_code2_avg);
            pstmt.setDouble(21, i_code3_avg);
            pstmt.setDouble(22, i_code4_avg);
            pstmt.setDouble(23, i_code5_avg);
            pstmt.setDouble(24, i_code6_avg);
            pstmt.setDouble(25, i_code7_avg);            
            pstmt.setDouble(26, i_code8_avg);            
            pstmt.setString(27, s_distcode10);                        
            
            isOk = pstmt.executeUpdate();
            
       }
       catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
       }
       finally { 
           if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
       }
       return isOk;
    }

    /**
    설문 대상자 리스트
    @param box          receive from the form object and session
    @return ArrayList   설문 대상자
    */
    public DataBox SelectUserPaperResult(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        DataBox             dbox    = null;
        String              sql     = "";        

        String v_grcode    = box.getString("p_grcode");
        String v_subj      = box.getStringDefault("p_subj", "TARGET");
        String v_gyear    = box.getString("p_gyear");
        String v_subjseq   = box.getStringDefault("p_subjseq","0001");
        int    v_sulpapernum = box.getInt("p_sulpapernum");
        String v_userid  = box.getString("p_userid");		
        try { 
                connMgr = new DBConnectionManager();
                
			sql = "select sulnums, answers    ";
            sql += "  from tz_suleach ";
            sql += " where grcode = " + SQLString.Format(v_grcode);
            sql += "   and subj   = " + SQLString.Format(v_subj);
            sql += "   and year   = " + SQLString.Format(v_gyear);
            sql += "   and subjseq   = " + SQLString.Format(v_subjseq);
            sql += "   and sulpapernum   = " + SQLString.Format(v_sulpapernum);
            sql += "   and userid   = " + SQLString.Format(v_userid);
			
                ls = connMgr.executeQuery(sql);
                
                while ( ls.next() ) { 
                    dbox = ls.getDataBox();
                }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }

    /**
    설문 대상자 리스트
    @param box          receive from the form object and session
    @return ArrayList   설문 대상자
    */
    public DataBox selectSulmunUser(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        DataBox             dbox    = null;
        String              sql     = "";        

        String v_userid  = box.getString("p_userid");		
        try {      
                connMgr = new DBConnectionManager();
                
/*
                sql += "select       b.comp  asgn,  get_compnm(b.comp,2,4)       asgnnm, ";
                sql += "	   	  b.jikup,       get_jikupnm(b.jikup, b.comp) jikupnm, ";
                sql += "	   	  b.cono,     b.name ";
                sql += "  from tz_member   b ";
                sql += "   where b.userid    = " + SQLString.Format(v_userid);
*/                
                
                sql += "select       b.comp  asgn,  '' asgnnm, ";
                sql += "          b.jikup,       '' jikupnm, ";
                sql += "          '' cono,     b.name ";
                sql += "  from tz_member   b ";
                sql += "   where b.userid    = " + SQLString.Format(v_userid);
                
     
                ls = connMgr.executeQuery(sql);
                
                while ( ls.next() ) { 
                    dbox = ls.getDataBox();
                }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }


    /**
    교육그룹코드 구하기 
    @param box          receive from the form object and session
    @return String   
    */  
    public String getGrcode(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq)  throws Exception { 
        String v_grcode = "";
        ListSet             ls      = null;
        String sql  = "";
        try { 
            sql = "select grcode ";
            sql += "  from tz_subjseq  ";
            sql += " where subj    = " + SQLString.Format(p_subj);
            sql += "   and year    = " + SQLString.Format(p_year);
            sql += "   and subjseq = " + SQLString.Format(p_subjseq);

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_grcode = ls.getString("grcode");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_grcode;
    }


    /**
    과목 설문 리스트 
    @param box      receive from the form object and session
    @return ArrayList
    **/
     public ArrayList selectEducationSubjectList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ArrayList list1     = null;
		DataBox dbox1 = null;
        String sql1         = "";
        String  v_subj      = ""; // 과목
        String  v_year      = ""; // 년도
        String  v_subjseq   = ""; // 과목기수
        String  v_user_id   = box.getSession("userid");

		try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            sql1  ="\r\n select x.*, get_codenm('0004',isonoff) isonoffvalue from (";
            sql1 +="\r\n select a.subjnm, a.grcode, a.edustart, a.eduend, a.subj, a.year, a.subjseq, b.userid, b.tstep, c.sulpapernum, c.sulpapernm, c.progress, c.sulnums, c.sultype,  '' AS STDT, '' AS ENDT, "
                 + "\r\n (select isonoff from tz_subj where subj=a.subj) isonoff,  "
                 + "\r\n (select count(userid) from tz_suleach where subj=a.subj and year=a.year and subjseq=a.subjseq and sulpapernum = a.sulpapernum and userid='" +v_user_id + "') eachcnt "
                 + "\r\n from tz_subjseq a, tz_student b, tz_sulpaper c "
                 + "\r\n where a.subj=b.subj and a.year=b.year and a.subjseq=b.subjseq and a.sulpapernum = c.sulpapernum and b.userid='" +v_user_id + "' "
                 + "\r\n       and c.subj='ALL' and c.grcode='ALL' and a.isclosed!='Y'";
            sql1 +="\r\n ) x";
            sql1 +="\r\n order by x.subj,x.year,x.subjseq ";
            ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 
                dbox1 = ls1.getDataBox();
                
                list1.add(dbox1);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;        
    }
    
     /**
     과목 설문 리스트 
     @param box      receive from the form object and session
     @return ArrayList
     **/
      public ArrayList selectSulmunList(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet ls1         = null;
         ArrayList list1     = null;
 		DataBox dbox1 = null;
         String sql1         = "";
         String  v_subj      = box.getString("p_subj"); // 과목
         String  v_year      = box.getString("p_year"); // 년도
         String  v_subjseq   = box.getString("p_subjseq"); // 과목기수
         String  v_user_id   = box.getSession("userid");

 		try { 
             connMgr = new DBConnectionManager();
             list1 = new ArrayList();
             sql1  ="\r\n select * from (";
             sql1 +="\r\n select a.subjnm, a.grcode, a.edustart, a.eduend, a.subj, a.year, a.subjseq, b.userid, b.tstep, c.sulpapernum, c.sulpapernm, c.progress, c.sulnums, c.sultype, a.PRESULSDATE STDT, a.PRESULEDATE ENDT,"
                  + "\r\n (select isonoff from tz_subj where subj=a.subj) isonoff, "
                  + "\r\n (select count(userid) from tz_suleach where subj=a.subj and year=a.year and subjseq=a.subjseq and sulpapernum = a.presulpapernum and userid='" +v_user_id + "') eachcnt "
                  + "\r\n from tz_subjseq a, tz_student b, tz_sulpaper c "
                  + "\r\n where a.subj=b.subj and a.year=b.year and a.subjseq=b.subjseq and a.presulpapernum = c.sulpapernum and b.userid='" +v_user_id + "' "
                  + "\r\n       and c.subj='ALL' and c.grcode='ALL'  and a.isclosed!='Y' "
                  + "\r\n       and a.subj="+StringManager.makeSQL(v_subj)+" and a.year="+StringManager.makeSQL(v_year)+"  and a.subjseq="+StringManager.makeSQL(v_subjseq)+" ";
             sql1 +="\r\n union ";            
             sql1 +="\r\n select a.subjnm, a.grcode, a.edustart, a.eduend, a.subj, a.year, a.subjseq, b.userid, b.tstep, c.sulpapernum, c.sulpapernm, c.progress, c.sulnums, c.sultype, a.AFTERSULSDATE STDT, a.AFTERSULEDATE ENDT,"
                  + "\r\n (select isonoff from tz_subj where subj=a.subj) isonoff, "
                  + "\r\n (select count(userid) from tz_suleach where subj=a.subj and year=a.year and subjseq=a.subjseq and sulpapernum = a.aftersulpapernum and userid='" +v_user_id + "') eachcnt "
                  + "\r\n from tz_subjseq a, tz_student b, tz_sulpaper c "
                  + "\r\n where a.subj=b.subj and a.year=b.year and a.subjseq=b.subjseq and a.aftersulpapernum = c.sulpapernum and b.userid='" +v_user_id + "' "
                  + "\r\n       and c.subj='ALL' and c.grcode='ALL'  and a.isclosed!='Y'"
	              + "\r\n       and a.subj="+StringManager.makeSQL(v_subj)+" and a.year="+StringManager.makeSQL(v_year)+"  and a.subjseq="+StringManager.makeSQL(v_subjseq)+" ";
             sql1 +="\r\n ) x";
             sql1 += " order by x.subj,x.year,x.subjseq ";

             ls1 = connMgr.executeQuery(sql1);

             while ( ls1.next() ) { 
                 dbox1 = ls1.getDataBox();
                 
                 list1.add(dbox1);
             }
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql1);
             throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return list1;        
     }
      
     /**
     교육그룹코드 구하기 
     @param box          receive from the form object and session
     @return String   
     */  
     public int getSulCnt(String p_subj, String p_year, String p_subjseq)  throws Exception {
         DBConnectionManager	connMgr	= null;    	 
         ListSet             ls      = null;
         String sql  	= "";
         int sulcnt		= 0;
         try { 
             connMgr = new DBConnectionManager();        	 
             sql += "  select  ";
             sql += "  (case when nvl(sulpapernum,0) > 0 then 1 else 0 end)+  ";             
             sql += "  (case when nvl(presulpapernum,0) > 0 then 1 else 0 end)+  ";     
             sql += "  (case when nvl(aftersulpapernum,0) > 0 then 1 else 0 end) sulcnt  ";        
             sql += "  from tz_subjseq  ";                 
             sql += " where subj    = " + SQLString.Format(p_subj);
             sql += "   and year    = " + SQLString.Format(p_year);
             sql += "   and subjseq = " + SQLString.Format(p_subjseq);             
             
             ls = connMgr.executeQuery(sql);
             while ( ls.next() ) { 
            	 sulcnt = ls.getInt("sulcnt");
             }
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return sulcnt;
     }
     
     
    
    /**
    수강중인 과목 리스트
    @param box      receive from the form object and session
    @return ArrayList
    
     public ArrayList selectEducationSubjectList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ArrayList list1     = null;
		ArrayList list2 = null;
		DataBox dbox1 = null;
		DataBox dbox2 = null;
        String sql1         = "";
        String  v_subj      = ""; // 과목
        String  v_year      = ""; // 년도
        String  v_subjseq   = ""; // 과목기수
        String  v_user_id   = box.getSession("userid");

		try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            // select upperclass,isonoff,course,cyear,courseseq,coursenm,subj,year,subjseq,subjnm,edustart,
            // eduend,eduurl,classnm,subjtarget
            sql1 = "select A.scupperclass,A.isonoff,A.course,A.cyear,A.courseseq,A.coursenm,A.subj,A.year,A.subjseq,A.subjseqgr, ";
            sql1 += "A.subjnm,A.edustart,A.eduend,A.eduurl,A.subjtarget, A.isoutsourcing ";
            // sql1 += "(select classnm from TZ_CLASS where A.subj=subj and A.year=year and A.subjseq=subjseq) classnm ";
            sql1 += "from VZ_SCSUBJSEQ A,TZ_STUDENT B ";
            sql1 += "where A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq and B.userid=" +SQLString.Format(v_user_id);
//            sql1 += " and B.isgraduated='N' ";
            sql1 += " and to_char(sysdate,'YYYYMMDDHH24') between A.edustart and A.eduend ";  
//            sql1 += " and  A.isoutsourcing = 'N'";
            sql1 += " order by A.course,A.cyear,A.courseseq,A.subj,A.year,A.subjseq,A.edustart,A.eduend ";
            
            ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
                    dbox1 = ls1.getDataBox();

                    box.put("p_subj", dbox1.getString("d_subj") );
                    box.put("p_year", dbox1.getString("d_year") );
                    box.put("p_subjseq", dbox1.getString("d_subjseq") );
					list2 = this.SelectUserList(connMgr, box);
					if ( list2.size() > 0)	{ 
                        dbox2 = (DataBox)list2.get(0);
					} else { 
					    dbox2 = new DataBox("resoponsebox");
					}
						dbox2.put("d_subjnm", dbox1.getString("d_subjnm") );
						dbox2.put("d_isonoff", dbox1.getString("d_isonoff") );

                    /* ==  ==  ==  ==  ==   과목설문 응시여부 ==  ==  ==  ==  == 			
			       int suldata = this.getUserData(connMgr, box);	
			       dbox2.put("d_suldata",String.valueOf(suldata));			

                    /* ==  ==  ==  ==  ==   권장진도율, 자기진도율 시작 ==  ==  ==  ==  == 
                   SubjGongAdminBean sbean = new SubjGongAdminBean();

        		   String promotion  = sbean.getPromotion(box);
			       dbox2.put("p_promotion",promotion);		
			       String progress = sbean.getProgress(box);
			       dbox2.put("p_progress",progress);	

				   list1.add(dbox2);
 					
                }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;        
    }*/

    /**
    사용자 해당과목리스트
    @param box          receive from the form object and session
    @return ArrayList   해당과목리스트
    */
    public ArrayList SelectUserList(DBConnectionManager connMgr, RequestBox box) throws Exception { 

        ListSet             ls      = null;
        ArrayList           list    = null;
        DataBox             dbox    = null;
        String              sql     = "";
        try { 
        String s_userid     = box.getSession("userid");
        String v_subj     = box.getString("p_subj"); 
        String v_year     = box.getString("p_year"); 
        String v_subjseq     = box.getString("p_subjseq");

            list = new ArrayList();

	    sql = "select a.grcode,       a.subj,   a.subjseq,      ";
            sql += "       a.sulpapernum,  a.sulpapernm, a.year, ";
            sql += "       a.totcnt,       a.sulnums, a.sulmailing, a.sulstart, a.sulend, a.progress ";
            sql += "  from tz_sulpaper a ";
            sql += "   where a.subj   = " + SQLString.Format(v_subj);
            sql += "   and a.year   = " + SQLString.Format(v_year);
            sql += "   and a.subjseq   = " + SQLString.Format(v_subjseq);
            sql += "   and rownum <= 1 ";
            sql += " order by a.subj, a.sulpapernum";

            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return list;
    }
    /**
    사용자 해당과목 설문 리스트
    @param box          receive from the form object and session
    @return ArrayList   해당과목리스트
    */
    public ArrayList SelectUserSubjSulnum(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        DataBox             dbox    = null;
        String              sql     = "";
        int v_sulpapernum 					= 0;                
        try { 
        String s_userid     = box.getSession("userid");
        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq     = box.getString("p_subjseq");


			connMgr = new DBConnectionManager();
            
            list = new ArrayList();
            
            sql  = " SELECT distinct (SELECT CODENM   																				\n";
            sql += "  		   FROM TZ_CODE    																				\n";
            sql += " 		  WHERE GUBUN = '0097' 																			\n";
            sql += " 			AND CODE = X.SULTYPE) AS SULTYPE 															\n";
            sql += " 	, X.SULPAPERNM 																						\n";
            sql += " 	, X.SULPAPERNUM, X.TOTCNT, X.SULNUMS 																\n";
            sql += "    , DECODE((SELECT COUNT(*) 																			\n";
            sql += "   			    FROM TZ_SULEACH Z  																		\n";
            sql += "  			   WHERE Z.SUBJ = Y.SUBJ 																	\n";
            sql += " 				 AND Z.YEAR = Y.YEAR 																	\n";
            sql += " 				 AND Z.SUBJSEQ = Y.SUBJSEQ 																\n";
            sql += " 				 AND Z.SULPAPERNUM = X.SULPAPERNUM 														\n";
            sql += " 				 AND Z.USERID = '" +s_userid + "'), 0, 'N', 'Y') AS ISSUL 								\n"; // N : 응시함, Y : 응시안함 
            sql += "    , (SELECT TSTEP 																					\n";
            sql += "  	     FROM TZ_STUDENT W 																				\n";
            sql += " 		WHERE W.SUBJ = Y.SUBJ 																			\n";
            sql += " 		  AND W.YEAR = Y.YEAR 																			\n";
            sql += "  		  AND W.SUBJSEQ = Y.SUBJSEQ 																	\n";
            sql += " 		  AND W.USERID = '" +s_userid + "') AS NPROGRESS												\n";
            sql += "    , X.PROGRESS 																						\n";
            sql += "    , DECODE(X.SULTYPE, '1', 'N', 'Y') AS LIMITDT														\n";
            sql += "    , Y.STDT     																						\n";
            sql += "    , Y.ENDT																							\n";
            sql += "    , Y.SUBJ																							\n";
            sql += "    , Y.SUBJSEQ																							\n";
            sql += "    , Y.YEAR																							\n";
            sql += "    , Y.SUBJNM																							\n";
            sql += "    , Y.GRCODE																							\n";
            
            sql += " 	, (SELECT ISONOFF 																					\n";
            sql += "  	     FROM TZ_SUBJ W 																				\n";
            sql += " 		WHERE W.SUBJ = Y.SUBJ ) AS ISONOFF																\n";
            
            sql += " FROM TZ_SULPAPER X , ( 																				\n";
            sql += " 		SELECT A.SULPAPERNUM, '' AS STDT, '' AS ENDT, B.SUBJ, B.YEAR, B.SUBJSEQ, B.SUBJNM, B.GRCODE		\n";
            sql += " 		FROM TZ_SULPAPER A, TZ_SUBJSEQ B 																\n";
            sql += " 		WHERE A.SULPAPERNUM = B.SULPAPERNUM 															\n";
            sql += " 			AND B.SUBJ = " + SQLString.Format(v_subj);
            sql += "       		AND B.YEAR = " + SQLString.Format(v_year);
            sql += " 			AND B.SUBJSEQ = " + SQLString.Format(v_subjseq);
            sql += " 		 ) Y 																							\n";
            sql += "  WHERE X.SULPAPERNUM = Y.SULPAPERNUM 																	\n";
            sql += "    AND X.SUBJ = 'ALL' 																					\n";
            sql += "    AND X.GRCODE = 'ALL' 																				\n";
            
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                box.put("p_num", dbox.getInt("d_sulpapernum")) ;
                list.add(dbox);
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
    사용자 해당과목 설문 리스트
    @param box          receive from the form object and session
    @return ArrayList   해당과목리스트
    */
    public ArrayList SelectUserList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        DataBox             dbox    = null;
        String              sql     = "";
                        
        try { 
        String s_userid     = box.getSession("userid");
        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq     = box.getString("p_subjseq");


			connMgr = new DBConnectionManager();
            
            list = new ArrayList();

			// sql = "select a.grcode,       a.subj,   a.subjseq,      ";
            // sql += "       a.sulpapernum,  a.sulpapernm, a.year, ";
            // sql += "       a.totcnt,       a.sulnums, a.sulmailing, a.sulstart, a.sulend, a.progress ";
            // sql += "  from tz_sulpaper a ";
            // sql += "   where a.subj   = " + SQLString.Format(v_subj);
            // sql += "   and a.year   = " + SQLString.Format(v_year);
            // sql += "   and a.subjseq   = " + SQLString.Format(v_subjseq);
            // sql += "   and rownum <= 1 ";
            // sql += " order by a.subj, a.sulpapernum desc ";
// 2008. 12.2 쿼리변경
//			sql = " select  a.subjnm, a.grcode, a.edustart, a.eduend, a.subj, a.year, a.subjseq,   			\n";
//            sql += " b.userid, b.tstep, a.sulpapernum,c.sulpapernm, c.progress, c.sulnums,           		\n"; 
//            sql += " a.presulpapernum, a.presulsdate, a.presuledate,										\n";
//            sql += " a.aftersulpapernum, a.aftersulsdate, a.aftersuledate, 									\n";
//            sql += " ( 																						\n";
//            sql += "  select sulpapernm from tz_sulpaper where sulpapernum = a.presulpapernum 				\n";
//            sql += " ) presulpapername, 																	\n";
//            sql += " ( 																						\n";
//            sql += "  select sulpapernm from tz_sulpaper where sulpapernum = a.aftersulpapernum 			\n";
//            sql += " ) aftersulpapername, 																	\n";
//            sql += "      (select count(userid) from tz_suleach                                     \n";
//            sql += "       where subj=a.subj and year=a.year and subjseq=a.subjseq                  \n"; 
//            sql += "       and sulpapernum = a.sulpapernum and userid='lee1') eachcnt               \n";
//            sql += " from TZ_SUBJSEQ a, TZ_STUDENT b, tz_sulpaper c                                 \n";
//            sql += " where A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq   and a.sulpapernum = c.sulpapernum  \n";
//            sql += "   and B.userid='" +s_userid + "'   and c.subj='ALL' and c.grcode='ALL'  \n";
//            // sql += "               and to_char(sysdate,'YYYYMMDDHH24') between A.edustart and A.eduend  ";
//            sql += " and a.subj=" +SQLString.Format(v_subj) + " and a.year=" +SQLString.Format(v_year) + " and a.subjseq=" +SQLString.Format(v_subjseq) + " ";
//            sql += " order by A.subj,A.year,A.subjseq,A.edustart,A.eduend     \n";
                                      
            
            sql  = " SELECT distinct (SELECT CODENM   																				\n";
            sql += "  		   FROM TZ_CODE    																				\n";
            sql += " 		  WHERE GUBUN = '0097' 																			\n";
            sql += " 			AND CODE = X.SULTYPE) AS SULTYPE 															\n";
            sql += " 	, X.SULPAPERNM 																						\n";
            sql += " 	, X.SULPAPERNUM, X.TOTCNT, X.SULNUMS 																\n";
            sql += "    , DECODE((SELECT COUNT(*) 																			\n";
            sql += "   			    FROM TZ_SULEACH Z  																		\n";
            sql += "  			   WHERE Z.SUBJ = Y.SUBJ 																	\n";
            sql += " 				 AND Z.YEAR = Y.YEAR 																	\n";
            sql += " 				 AND Z.SUBJSEQ = Y.SUBJSEQ 																\n";
            sql += " 				 AND Z.SULPAPERNUM = X.SULPAPERNUM 														\n";
            sql += " 				 AND Z.USERID = " +StringManager.makeSQL(s_userid) + "), 0, 'N', 'Y') AS ISSUL 								\n"; // N : 응시함, Y : 응시안함 
            sql += "    , (SELECT TSTEP 																					\n";
            sql += "  	     FROM TZ_STUDENT W 																				\n";
            sql += " 		WHERE W.SUBJ = Y.SUBJ 																			\n";
            sql += " 		  AND W.YEAR = Y.YEAR 																			\n";
            sql += "  		  AND W.SUBJSEQ = Y.SUBJSEQ 																	\n";
            sql += " 		  AND W.USERID = " +StringManager.makeSQL(s_userid) + ") AS NPROGRESS												\n";
            sql += "    , X.PROGRESS 																						\n";
            sql += "    , DECODE(X.SULTYPE, '1', 'N', 'Y') AS LIMITDT														\n";
            sql += "    , Y.STDT     																						\n";
            sql += "    , Y.ENDT																							\n";
            sql += "    , Y.SUBJ																							\n";
            sql += "    , Y.SUBJSEQ																							\n";
            sql += "    , Y.YEAR																							\n";
            sql += "    , Y.SUBJNM																							\n";
            sql += "    , Y.GRCODE																							\n";
            sql += "	, decode((SELECT COUNT(ANSWER) FROM TZ_EXAMRESULT \n";
            sql += "	   WHERE SUBJ = Y.SUBJ AND YEAR = Y.YEAR AND SUBJSEQ = Y.SUBJSEQ AND USERID = "+StringManager.makeSQL(s_userid)+" AND EXAMTYPE = 'E' ";
            sql += "	   ),'1','Y','N') isexam , x.sulgubun \n";
            sql += " FROM TZ_SULPAPER X , ( 																				\n";
            sql += " 		SELECT A.SULPAPERNUM, '' AS STDT, '' AS ENDT, B.SUBJ, B.YEAR, B.SUBJSEQ, B.SUBJNM, B.GRCODE				\n";
            sql += " 		FROM TZ_SULPAPER A, TZ_SUBJSEQ B 																\n";
            sql += " 		WHERE A.SULPAPERNUM = B.SULPAPERNUM 															\n";
            sql += " 			AND B.SUBJ = " + StringManager.makeSQL(v_subj);
            sql += "       		AND B.YEAR = " + StringManager.makeSQL(v_year);
            sql += " 			AND B.SUBJSEQ = " + StringManager.makeSQL(v_subjseq);
            sql += " 		UNION ALL 																						\n";
            sql += " 		SELECT PRESULPAPERNUM, B.PRESULSDATE, B.PRESULEDATE, B.SUBJ, B.YEAR, B.SUBJSEQ, B.SUBJNM, B.GRCODE 		\n";
            sql += " 		FROM TZ_SULPAPER A, TZ_SUBJSEQ B 																\n";
            sql += " 		WHERE A.SULPAPERNUM = B.SULPAPERNUM 															\n";
            sql += " 		AND B.SUBJ = " + StringManager.makeSQL(v_subj);
            sql += " 		AND B.YEAR = " + StringManager.makeSQL(v_year);
            sql += " 		AND B.SUBJSEQ = " + StringManager.makeSQL(v_subjseq);
            sql += " 		UNION ALL 																						\n";
            sql += " 		SELECT AFTERSULPAPERNUM, B.AFTERSULSDATE, B.AFTERSULEDATE, B.SUBJ, B.YEAR, B.SUBJSEQ, B.SUBJNM, B.GRCODE 	\n";
            sql += " 		FROM TZ_SULPAPER A, TZ_SUBJSEQ B 																\n";
            sql += " 		WHERE A.SULPAPERNUM = B.SULPAPERNUM 															\n";
            sql += " 		AND B.SUBJ = " + StringManager.makeSQL(v_subj);
            sql += " 		AND B.YEAR = " + StringManager.makeSQL(v_year);
            sql += " 		AND B.SUBJSEQ = " + StringManager.makeSQL(v_subjseq);
            sql += " 		 ) Y 																							\n";
            sql += "  WHERE X.SULPAPERNUM = Y.SULPAPERNUM 																	\n";
            sql += "    AND X.SUBJ = 'ALL' 																					\n";
            sql += "    AND X.GRCODE = 'ALL' 																				\n";
            
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                list.add(dbox);
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
    사용자 해당과목리스트
    @param box          receive from the form object and session
    @return ArrayList   해당과목리스트
    */
    public int getUserData(DBConnectionManager connMgr, RequestBox box) throws Exception { 

		ListSet             ls      = null;
        DataBox             dbox    = null;
        String              sql     = "";
        int v_research = 0;
						
        try { 
        String s_userid     = box.getSession("userid");
        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq     = box.getString("p_subjseq");

			sql = "select count(a.answers) researchcnt  ";
            sql += "  from tz_suleach a ";
            sql += "   where a.subj   = " + SQLString.Format(v_subj);
            sql += "   and a.year   = " + SQLString.Format(v_year);
            sql += "   and a.subjseq   = " + SQLString.Format(v_subjseq);
            sql += "   and a.userid   = " + SQLString.Format(s_userid);
            sql += "   and a.grcode  ! = 'ALL' ";

            ls = connMgr.executeQuery(sql);
            
            if ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                v_research = dbox.getInt("d_researchcnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_research;
    }

    /**
     * 과정별 설문갯수
     * @param box
     * @return
     * @throws Exception
     */
    public int getSulData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        DataBox             dbox    = null;
        String              sql     = "";
        int v_suldata = 0;
						
        try { 
	        String s_userid     = box.getSession("userid");
	        String v_subj     = box.getString("p_subj");
	        String v_year     = box.getString("p_year");
	        String v_subjseq     = box.getString("p_subjseq");

			connMgr = new DBConnectionManager();
			
            sql = "\n select decode(nvl(a.sulpapernum, 0), 0, 0, 1) "
            	+ "\n        + decode(nvl(a.presulpapernum, 0), 0, 0, 1) "
            	+ "\n        + decode(nvl(a.aftersulpapernum, 0), 0, 0, 1) as suldata "
            	+ "\n from   tz_subjseq a "
            	+ "\n where  a.subj = " + SQLString.Format(v_subj)
            	+ "\n and    a.year = " + SQLString.Format(v_year)
            	+ "\n and    a.subjseq = " + SQLString.Format(v_subjseq);

            ls = connMgr.executeQuery(sql);
            
            if ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                v_suldata = dbox.getInt("d_suldata");
            }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_suldata;
    }
    /**
    과정별 설문기간
    @param box          receive from the form object and session
    @return ArrayList   해당과목리스트
    */
    public ArrayList getSulDate( RequestBox box) throws Exception { 
    	DBConnectionManager connMgr = null;
		ListSet             ls      = null;
		DataBox             dbox    = null;
        String              sql     = "";
        ArrayList           list    = null;
        
        list = new ArrayList();		
        
        try { 
        	connMgr = new DBConnectionManager();
        	list = new ArrayList();
	        
        	String v_subj     = box.getString("p_subj");
	        String v_year     = box.getString("p_year");
	        String v_subjseq     = box.getString("p_subjseq");

			sql = "select aftersulsdate, aftersuledate  ";
            sql += "  from tz_subjseq a ";
            sql += "   where a.subj   = " + SQLString.Format(v_subj);
            sql += "   and a.year   = " + SQLString.Format(v_year);
            sql += "   and a.subjseq   = " + SQLString.Format(v_subjseq);
            sql += "   and a.grcode  ! = 'ALL' ";
System.out.println("-------------???----------" + sql);
            ls = connMgr.executeQuery(sql);
           
            
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                
                list.add(dbox);
            }
System.out.println("-------------list-------" + list);           
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
    사용자 해당과목리스트
    @param box          receive from the form object and session
    @return ArrayList   해당과목리스트
    */
    public int getUserData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        DataBox             dbox    = null;
        String              sql     = "";
        int v_research = 0;
        int v_suldata = 0;
						
        try { 
	        String s_userid     = box.getSession("userid");
	        String v_subj     = box.getString("p_subj");
	        String v_year     = box.getString("p_year");
	        String v_subjseq     = box.getString("p_subjseq");

			connMgr = new DBConnectionManager();
			
            sql = "\n select decode(nvl(a.sulpapernum, 0), 0, 0, 1) "
            	+ "\n        + decode(nvl(a.presulpapernum, 0), 0, 0, 1) "
            	+ "\n        + decode(nvl(a.aftersulpapernum, 0), 0, 0, 1) as suldata "
            	+ "\n from   tz_subjseq a "
            	+ "\n where  a.subj = " + SQLString.Format(v_subj)
            	+ "\n and    a.year = " + SQLString.Format(v_year)
            	+ "\n and    a.subjseq = " + SQLString.Format(v_subjseq);

            ls = connMgr.executeQuery(sql);
            
            if ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                v_suldata = dbox.getInt("d_suldata");
            }
            if ( ls != null ) { 
            	try { ls.close(); } 
            	catch ( Exception e ) {
	                ErrorManager.getErrorStackTrace(e, box, sql);
	                throw new Exception("sql = " + sql + "\r\n" + e.getMessage() );
            	} 
            }

            sql = "\n select count(a.answers) researchcnt  "
            	+ "\n from   tz_suleach a "
            	+ "\n where  a.subj   = " + SQLString.Format(v_subj)
            	+ "\n and    a.year   = " + SQLString.Format(v_year)
            	+ "\n and    a.subjseq   = " + SQLString.Format(v_subjseq)
            	+ "\n and    a.userid   = " + SQLString.Format(s_userid)
            	+ "\n and    a.grcode  ! = 'ALL' ";

            ls = connMgr.executeQuery(sql);
            
            if ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                v_research = dbox.getInt("d_researchcnt");
            }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_research;
    }
}