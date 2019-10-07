// **********************************************************
//  1. 제      목: Report BEAN
//  2. 프로그램명: ReportBean.java
//  3. 개      요: 과제 bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 박진희 2003. 9. 01
//  7. 수      정:
// **********************************************************
package com.ziaan.study;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import com.ziaan.library.CalcUtil;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileMove;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class ReportBean { 

    public ReportBean() { }

    /**
    과제와 액티비티 존재 여부
    @param box      receive from the form object and session
    @return ReportData
    */
     public int selectChoicePage(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;
        String sql1         = "";
        String sql2         = "";
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_user_id   = box.getSession("userid");
        int     result      = 0;     // 1:과제 있음
        try { 
            connMgr = new DBConnectionManager();

            // select from TZ_PROJORD
            sql1 = "select count(A.subj) cnt ";
            sql1 += "from TZ_PROJGRP A ";
            sql1 += "where A.subj=" +StringManager.makeSQL(v_subj) + " and A.year=" +StringManager.makeSQL(v_year) + " and A.subjseq=" +StringManager.makeSQL(v_subjseq) + " ";

            ls1 = connMgr.executeQuery(sql1);
            if ( ls1.next() && ls1.getInt("cnt") > 0 ) { 
                result = 1;     // 과제가 있는 경우
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }

    /**
    학습창 과제 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectReportList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        
        String sql          = "";
        DataBox dbox   		= null;
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_user_id   = box.getSession("userid");
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            sql  = " SELECT 																	\n";
            sql += " 	A.GRPSEQNM title, A.GRPSEQ, A.SUBJ			 							\n";
            sql += "  , A.SUBJSEQ, A.YEAR					 									\n";
            sql += "  , (SELECT COUNT(*) FROM TZ_PROJMAP C 										\n";
            sql += "      WHERE C.GRPSEQ = A.GRPSEQ  											\n";
            sql += "        AND C.SUBJ 	 = A.SUBJ 												\n";
            sql += "        AND C.YEAR 	 = A.YEAR		 										\n";
            sql += "        AND C.SUBJSEQ = A.SUBJSEQ		 									\n";
            sql += "     ) qcnt 																\n";
            sql += "  , DECODE(PROJGUBUN, 'F' ,'기말','중간') projgubunstr, A.PROJGUBUN			\n";
            sql += "  -- 진도율 																\n";
            sql +=" ,(SELECT TSTEP FROM TZ_STUDENT  \n" ;
            sql +=	"WHERE SUBJ ="+StringManager.makeSQL(v_subj)+" \n";
            sql +=" AND  YEAR ="+StringManager.makeSQL(v_year)+" \n";
            sql +=" AND  SUBJSEQ = "+StringManager.makeSQL(v_subjseq)+"\n";
            sql +=" AND  USERID ="+StringManager.makeSQL(v_user_id)+") TSTEP \n";
            sql += "  -- 제출여부 																\n";
            sql += "  , ( SELECT COUNT(GRPSEQ) 													\n";
            sql += "  	  FROM TZ_PROJREP	 													\n";
            sql += "  	  WHERE GRPSEQ = A.GRPSEQ 												\n";
            sql += "  		AND SUBJ = A.SUBJ													\n";
            sql += "  		AND YEAR = A.YEAR													\n";
            sql += "  		AND SUBJSEQ = A.SUBJSEQ												\n";
            sql += "  		AND SUBJ = " +StringManager.makeSQL(v_subj) + "						\n";
            sql += "  		AND SUBJSEQ = " +StringManager.makeSQL(v_subjseq) + "				\n";
            sql += "  		AND YEAR = " +StringManager.makeSQL(v_year) + " 					\n";
            sql += "  		AND projid = " +StringManager.makeSQL(v_user_id) + "				\n";
            sql += "  		AND isfinal = 'Y'				 									\n";
            sql += "  	) acnt 																	\n"; 
            sql += "   -- isscore Y, N									 						\n";
            sql += "  , DECODE((SELECT count(*)													\n";
            sql += "  	   FROM TZ_PROJREP 														\n";
            sql += "  	  WHERE GRPSEQ = A.GRPSEQ												\n";
            sql += "  	    AND SUBJ = A.SUBJ													\n";
            sql += "  		AND YEAR = A.YEAR													\n";
            sql += " 		AND SUBJSEQ = A.SUBJSEQ  											\n";
            sql += "  		AND SUBJ = " +StringManager.makeSQL(v_subj) + "						\n";
            sql += "  		AND SUBJSEQ = " +StringManager.makeSQL(v_subjseq) + "				\n";
            sql += "  		AND YEAR = " +StringManager.makeSQL(v_year) + "						\n";
            sql += "  		AND projid = " +StringManager.makeSQL(v_user_id) + "				\n";
            sql += "  		AND isfinal = 'Y'				 									\n";
            sql += "  		AND score is not null			 									\n";
            sql += " 	),0,'N','Y') isscore		 											\n";
            sql += " , A.reptype					 											\n";
            sql += " , B.userid						 											\n";
            sql += " , CASE WHEN PROJGUBUN = 'M' THEN MREPORT_START 							\n";
            sql += "    ELSE FREPORT_START 														\n";
            sql += "    END SDATE 																\n";
            sql += " , CASE WHEN PROJGUBUN = 'M' THEN MREPORT_END 								\n";
            sql += "    ELSE FREPORT_END 														\n";
            sql += "    END EDATE 																\n";
            sql += " , CASE WHEN TO_CHAR(SYSDATE,'YYYYMMDD') BETWEEN " +
            	   "    (CASE WHEN PROJGUBUN = 'M' THEN MREPORT_START ELSE FREPORT_START END)" +
            	   "    AND " +
            	   "    (CASE WHEN PROJGUBUN = 'M' THEN MREPORT_END ELSE FREPORT_END END) " +
            	   "   THEN 'Y'  	\n";
            sql += "   ELSE 'N' 																\n";
            sql += "   END ISOPENYN 															\n";
       		sql += " 		, (SELECT RETDATE    											\n";
       		sql += " 		   FROM TZ_PROJREP     											\n";
       		sql += " 		   WHERE SUBJ = A.SUBJ    										\n";
       		sql += " 		     AND YEAR = A.YEAR    										\n";
       		sql += " 		     AND SUBJSEQ = A.SUBJSEQ		    						\n";
       		sql += " 		     AND GRPSEQ = A.GRPSEQ		    							\n";
       		sql += " 		     AND PROJID = B.USERID		    							\n";
       		sql += " 		     AND ISFINAL = 'Y'		   	 								\n";
       		sql += " 		     AND ROWNUM =1		   		 								\n";
       		sql += " 		   ) RETDATE -- 반려일		    								\n";
            sql += " FROM TZ_PROJGRP A , TZ_PROJASSIGN B , TZ_SUBJSEQ C							\n";
            sql += " WHERE A.GRPSEQ = B.GRPSEQ(+)					 								\n";
            sql += " AND A.SUBJ = B.SUBJ(+)							 							\n";
            sql += " AND A.YEAR = B.YEAR(+) 														\n";
            sql += " AND A.SUBJSEQ = B.SUBJSEQ (+)													\n";
            sql += " AND B.SUBJ = C.SUBJ 														\n";
            sql += " AND B.YEAR = C.YEAR 														\n";
            sql += " AND B.SUBJSEQ = C.SUBJSEQ 													\n";
            sql += " AND A.SUBJ = " +StringManager.makeSQL(v_subj) + "							\n";
            sql += "  AND A.SUBJSEQ = " +StringManager.makeSQL(v_subjseq) + "					\n";
            sql += "  AND A.YEAR = " +StringManager.makeSQL(v_year) + "							\n";
            sql += "  AND B.USERID= " +StringManager.makeSQL(v_user_id) + "						\n";
            System.out.println("리포트 빈 sql: "+sql);
            
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
     출제 과제 정보보기
     @param box      receive from the form object and session
     @return ReportData
     */
      public ArrayList selectReportOrd(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet ls          = null;
         String sql          = "";
         ArrayList list		= null;
         DataBox   dbox 		= null;
         String  v_subj      = box.getString("p_subj");          // 과목
         String  v_year      = box.getString("p_year");          // 년도
         String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
         String  v_reptype   = box.getString("p_reptype");
         String  v_projgrp   = box.getString("p_projgrp");
         String  v_grpseq 	= box.getString("p_grpseq");

         try { 
             connMgr = new DBConnectionManager();
            
             sql  = " SELECT A.TITLE, A.CONTENTS, A.ORDSEQ 							\n";
             sql += " 	  , A.REALFILE, UPFILE -- 출제파일							\n";
             sql += " 	  , A.REALFILE2, UPFILE2 -- 정답파일 							\n";
             sql += "	  , B.SUBJ, B.YEAR, B.SUBJSEQ, B.GRPSEQ						\n";
             sql += " FROM TZ_PROJORD A , TZ_PROJMAP B								\n";
             sql += " WHERE A.ORDSEQ 	= B.ORDSEQ									\n";
             sql += "   AND A.SUBJ 		= B.SUBJ 									\n";
             sql += "   AND B.GRPSEQ 	= " +v_grpseq + " 							\n";
             sql += "   AND B.YEAR 		= " +StringManager.makeSQL(v_year) + "		\n";
             sql += "   AND B.SUBJ 		= " +StringManager.makeSQL(v_subj) + "		\n";
             sql += "   AND B.SUBJSEQ 	= " +StringManager.makeSQL(v_subjseq) + "	\n";
             
             ls = connMgr.executeQuery(sql);
             list = new ArrayList();
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
    해당 과정에 출제된 중간 리포트가 있는지 검사하고 그 리스트 번호를 가져온다.(중간)
    @param box      receive from the form object and session
    @return ReportData
       */
      public ArrayList MReport(RequestBox box) throws Exception { 
    	  DBConnectionManager	connMgr	= null;
    	  ListSet ls          = null;
    	  String sql          = "";
    	  ArrayList list		= null;
    	  DataBox   dbox 		= null;
    	  String  v_subj      = box.getString("p_subj");          // 과목
    	  String  v_year      = box.getString("p_year");          // 년도
    	  String  v_subjseq   = box.getString("p_subjseq");       // 과목기수

    	  try { 
    		  connMgr = new DBConnectionManager();
    		  
    		  sql  = " SELECT GRPSEQ 										\n";
    		  sql += " FROM TZ_PROJGRP 										\n";
    		  sql += " WHERE 1=1 											\n";
    		  sql += "   AND PROJGUBUN = 'M' 								\n";	// 해당 과정의 중간리포트가 생성되었는지 검사.
    		  sql += "   AND SUBJ = "+StringManager.makeSQL(v_subj)+"		\n";
    		  sql += "   AND YEAR = "+StringManager.makeSQL(v_year)+"		\n";
    		  sql += "   AND SUBJSEQ = "+StringManager.makeSQL(v_subjseq)+"	\n";
    		  
    		  ls = connMgr.executeQuery(sql);
    		  list = new ArrayList();
    		  while ( ls.next() ) { 
    			  String v_grpseq = ls.getString("grpseq");
    			  list.add(v_grpseq);
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
    해당 과정에 출제된 기말 리포트가 있는지 검사하고 그 리스트 번호를 가져온다.(기말)  
    @param box      receive from the form object and session
    @return ReportData
       */
      public ArrayList FReport(RequestBox box) throws Exception { 
    	  DBConnectionManager	connMgr	= null;
    	  ListSet ls          = null;
    	  String sql          = "";
    	  ArrayList list		= null;
    	  DataBox   dbox 		= null;
    	  String  v_subj      = box.getString("p_subj");          // 과목
    	  String  v_year      = box.getString("p_year");          // 년도
    	  String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
    	  
    	  try { 
    		  connMgr = new DBConnectionManager();
    		  sql  = " SELECT GRPSEQ 										\n";
    		  sql += " FROM TZ_PROJGRP 										\n";
    		  sql += " WHERE 1=1 											\n";
    		  sql += "   AND PROJGUBUN = 'F' 								\n";	// 해당 과정의 기말 리포트가 생성되었는지 검사.
    		  sql += "   AND SUBJ = "+StringManager.makeSQL(v_subj)+"		\n";
    		  sql += "   AND YEAR = "+StringManager.makeSQL(v_year)+"		\n";
    		  sql += "   AND SUBJSEQ = "+StringManager.makeSQL(v_subjseq)+"	\n";
    		  
    		  ls = connMgr.executeQuery(sql);
    		  list = new ArrayList();
    		  while ( ls.next() ) { 
    			  String v_grpseq = ls.getString("grpseq");
    			  list.add(v_grpseq);
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
    출제 과제 정보 보기1
    @param box      receive from the form object and session
    @return ReportData
    */
     public ArrayList selectReportOrd1(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        String sql          = "";
        ArrayList list		= null;
        DataBox   dbox 		= null;
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_reptype   = box.getString("p_reptype");
        //String  v_projgrp   = box.getString("p_projgrp");
        String  v_userid    = box.getString("p_projid");
        String  v_grpseq 	= box.getString("p_grpseq");
//System.out.println("v_projgrp::>"+v_projgrp);
//System.out.println("v_userid:::>"+v_userid);
        try { 
            connMgr = new DBConnectionManager();
           
            sql  = " SELECT A.TITLE, A.CONTENTS, A.ORDSEQ 							\n";
            sql += " 	  , A.REALFILE, UPFILE -- 출제파일							\n";
            sql += " 	  , A.REALFILE2, UPFILE2 -- 정답파일 							\n";
            sql += "	  , B.SUBJ, B.YEAR, B.SUBJSEQ, B.GRPSEQ						\n";
            sql += "	  , C.CONTENTS repcontents, c.seq							\n";
            sql += " FROM TZ_PROJORD A , TZ_PROJMAP B, TZ_PROJREP C					\n";
            sql += " WHERE A.ORDSEQ 	= B.ORDSEQ									\n";
            sql += "   AND A.SUBJ 		= B.SUBJ 									\n";
            sql += "   AND B.SUBJ       = C.SUBJ									\n";
            sql += "   AND B.SUBJSEQ    = C.SUBJSEQ									\n";
            sql += "   AND B.YEAR       = C.YEAR									\n";
            sql += "   AND B.GRPSEQ     = C.GRPSEQ									\n";
            sql += "   AND B.ORDSEQ     = C.ORDSEQ									\n";
            sql += "   AND C.ISFINAL 	= 'Y'										\n";
            sql += "   AND B.GRPSEQ 	= " +v_grpseq + " 							\n";
            sql += "   AND B.YEAR 		= " +StringManager.makeSQL(v_year) + " 		\n";
            sql += "   AND B.SUBJ 		= " +StringManager.makeSQL(v_subj) + " 		\n";
            sql += "   AND B.SUBJSEQ 	= " +StringManager.makeSQL(v_subjseq) + " 	\n";
            sql += "   AND c.PROJID 	= " +StringManager.makeSQL(v_userid)  + " 	\n";
            
            ls = connMgr.executeQuery(sql);
            list = new ArrayList();
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
    출제 과제 정보 보기2
    @param box      receive from the form object and session
    @return ReportData
    */
     public ArrayList selectReportOrd2(RequestBox box) throws Exception { 
    	 DBConnectionManager	connMgr	= null;
    	 ListSet ls          = null;
    	 String sql          = "";
    	 ArrayList list		= null;
    	 DataBox   dbox 		= null;
    	 String  v_subj      = box.getString("p_subj");          // 과목
    	 String  v_year      = box.getString("p_year");          // 년도
    	 String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
    	 String  v_reptype   = box.getString("p_reptype");
    	 String  v_projgrp   = box.getString("p_projgrp");
    	 String  v_userid    = box.getString("p_projid");
    	 String  v_grpseq 	= box.getString("p_grpseq");
    	 
    	 try { 
    		 connMgr = new DBConnectionManager();
    		 
    		 sql  = " SELECT A.TITLE, A.CONTENTS, A.ORDSEQ 							\n";
    		 sql += " 	  , A.REALFILE, UPFILE -- 출제파일							\n";
    		 sql += " 	  , A.REALFILE2, UPFILE2 -- 정답파일 						\n";
    		 sql += "	  , B.SUBJ, B.YEAR, B.SUBJSEQ, B.GRPSEQ						\n";
    		 sql += "	  , C.CONTENTS repcontents, c.seq							\n";
    		 sql += "	  , A.SCORE qscore, C.SCORE repscore, c.correction						\n";
    		 sql += "	  , C.ISOPENEXP												\n";
    		 sql += " FROM TZ_PROJORD A , TZ_PROJMAP B, TZ_PROJREP C				\n";
    		 sql += " WHERE A.ORDSEQ 	= B.ORDSEQ									\n";
    		 sql += "   AND A.SUBJ 		= B.SUBJ 									\n";
    		 sql += "   AND B.SUBJ      = C.SUBJ									\n";
    		 sql += "   AND B.SUBJSEQ   = C.SUBJSEQ									\n";
    		 sql += "   AND B.YEAR      = C.YEAR									\n";
    		 sql += "   AND B.GRPSEQ    = C.GRPSEQ									\n";
    		 sql += "   AND B.ORDSEQ    = C.ORDSEQ									\n";
    		 sql += "   AND C.ISFINAL 	= 'Y'										\n";
    		 sql += "   AND B.GRPSEQ 	= " +v_grpseq + " 							\n";
    		 sql += "   AND B.YEAR 		= " +StringManager.makeSQL(v_year) + " 		\n";
    		 sql += "   AND B.SUBJ 		= " +StringManager.makeSQL(v_subj) + " 		\n";
    		 sql += "   AND B.SUBJSEQ 	= " +StringManager.makeSQL(v_subjseq) + " 	\n";
    		 sql += "   AND C.PROJID 	= " +StringManager.makeSQL(v_userid) + " 	\n";
    		 
    		 ls = connMgr.executeQuery(sql);
    		 list = new ArrayList();
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
     과제 제출내용 보기2
     @param box      receive from the form object and session
     @return ReportData
     */
      public DataBox selectReportRepFile2(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet ls          = null;
         String sql          = "";
         DataBox dbox   	 = null;
         String  s_userid    = box.getSession("userid");		// 사용자아이디
         String  v_subj      = box.getString("p_subj");         // 과목
         String  v_year      = box.getString("p_year");         // 년도
         String  v_subjseq   = box.getString("p_subjseq");      // 과목기수
         String  v_reptype   = box.getString("p_reptype");
         String  v_projgrp   = box.getString("p_projgrp");
         String  v_grpseq 	= box.getString("p_grpseq");
         
         
         try { 
             connMgr = new DBConnectionManager();
             
             sql   = " SELECT  															\n";
             sql  += " 		  A.GRPSEQ, A.SUBJ, A.SUBJSEQ   							\n";
             sql  += " 		, A.YEAR, A.USERID, A.ADDRESS   							\n";
             sql  += " 		, A.SUCCESS, A.FAIL, A.REPDATE   							\n";
             sql  += " 		, A.TOTALSCORE --채점 총점  								\n";
             sql  += " 		, B.GRPTOTALSCORE AS SCORE --문제 총점 	 					\n";
             sql  += " 		, A.REALREPFILE --실제답안파일		 	 					\n";
             sql  += " 		, A.UPREPFILE   --업로드답안파일		 	 				\n";
             sql  += " 		, A.REALREPFILE2 --실제답안파일		 	 					\n";
             sql  += " 		, A.UPREPFILE2   --업로드답안파일		 	 				\n";
             sql  += " 		, A.REVIEW       --총평		 	 				\n";
             sql  += " FROM TZ_PROJASSIGN A, TZ_PROJGRP B  								\n";
             sql  += " WHERE 1 = 1   													\n";
             sql  += "   AND A.GRPSEQ = B.GRPSEQ  										\n";
             sql  += "   AND A.SUBJ = B.SUBJ 											\n";
             sql  += "   AND A.SUBJSEQ = B.SUBJSEQ 										\n";
             sql  += "   AND A.YEAR = B.YEAR  											\n";
             sql  += "   AND A.SUBJ     = " +StringManager.makeSQL(v_subj)    + "		\n";
             sql  += "   AND A.SUBJSEQ  = " +StringManager.makeSQL(v_subjseq) + " 		\n";
             sql  += "   AND A.YEAR     = " +StringManager.makeSQL(v_year)    + "		\n";
             sql  += "   AND A.GRPSEQ   = " +v_grpseq  + " 								\n";
             sql  += "   AND A.userid   = " +StringManager.makeSQL(s_userid)  + "		\n";             

             ls = connMgr.executeQuery(sql);
             if ( ls.next() ) { 
                 dbox = ls.getDataBox();
             }
             
             // System.out.println("data.setIssubmit(N) == = >> " +data.getIssubmit() );
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return dbox;
     }
      /**
     과제 제출내용 보기1
     @param box      receive from the form object and session
     @return ReportData
       */
      public DataBox selectReportRepFile(RequestBox box) throws Exception { 
    	  DBConnectionManager	connMgr	= null;
    	  ListSet ls          = null;
    	  String sql          = "";
    	  DataBox dbox   	 = null;
    	  String  s_userid    = box.getSession("userid");			// 사용자아이디
    	  String  v_subj      = box.getString("p_subj");          // 과목
    	  String  v_year      = box.getString("p_year");          // 년도
    	  String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
    	  String  v_reptype   = box.getString("p_reptype");
    	  String  v_projgrp   = box.getString("p_projgrp");
    	  String  v_grpseq 	= box.getString("p_grpseq");
    	  
    	  
    	  try { 
    		  connMgr = new DBConnectionManager();

    		  // 제출내용 보여줌
    		          
    		  sql  = " SELECT REALREPFILE, UPREPFILE, A.SUBJ, A.SUBJSEQ, A.GRPSEQ, A.YEAR			\n";
    		  sql += " , REALREPFILE2, UPREPFILE2													\n";
    		  sql += " , CASE WHEN PROJGUBUN = 'M' THEN MREPORT_START 								\n";
              sql += "    ELSE FREPORT_START 														\n";
              sql += "    END SDATE 																\n";
              sql += " , CASE WHEN PROJGUBUN = 'M' THEN MREPORT_END 								\n";
              sql += "    ELSE FREPORT_END 															\n";
              sql += "    END EDATE 																\n";
              sql += " , CASE WHEN TO_CHAR(SYSDATE,'YYYYMMDD') BETWEEN " +
              	     "    (CASE WHEN PROJGUBUN = 'M' THEN MREPORT_START ELSE FREPORT_START END)" +
              	     "    AND " +
              	     "    (CASE WHEN PROJGUBUN = 'M' THEN MREPORT_END ELSE FREPORT_END END) " +
              	     "   THEN 'Y'  	\n";
              sql += "   ELSE 'N' 																	\n";
              sql += "   END ISOPENYN 																\n";
    		  sql += " FROM TZ_PROJASSIGN A , TZ_PROJGRP B, TZ_SUBJSEQ C 							\n";
    		  sql += " WHERE A.GRPSEQ = B.GRPSEQ 													\n";
    		  sql += " AND A.YEAR = B.YEAR 															\n";
    		  sql += " AND A.SUBJ = B.SUBJ 															\n";
    		  sql += " AND A.SUBJSEQ = B.SUBJSEQ 													\n";
    		  sql += " AND B.SUBJ = C.SUBJ 															\n";
              sql += " AND B.YEAR = C.YEAR 															\n";
              sql += " AND B.SUBJSEQ = C.SUBJSEQ 													\n";
    		  sql += " AND A.SUBJ = " +StringManager.makeSQL(v_subj) + "							\n";
    		  sql += " AND A.SUBJSEQ = " +StringManager.makeSQL(v_subjseq) + " 						\n";
    		  sql += " AND A.YEAR    = " +StringManager.makeSQL(v_year) + "							\n";
    		  sql += " AND A.GRPSEQ  = " +v_grpseq + " 												\n";
    		  sql += " AND A.USERID = "+StringManager.makeSQL(s_userid)+"							\n";
    		  ls = connMgr.executeQuery(sql);
    		  if ( ls.next() ) { 
    			  dbox = ls.getDataBox();
    		  }
    		  
    		  // System.out.println("data.setIssubmit(N) == = >> " +data.getIssubmit() );
    	  } catch ( Exception ex ) { 
    		  ErrorManager.getErrorStackTrace(ex, box, sql);
    		  throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	  } finally { 
    		  if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
    		  if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	  }
    	  
    	  return dbox;
      }
    /**
    과제 제출내용 보기
    @param box      receive from the form object and session
    @return ReportData
    */
     public ReportData selectReportRep(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        String sql          = "";
        ReportData data   = null;
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_reptype   = box.getString("p_reptype");
        String  v_projgrp   = box.getString("p_projgrp");
        String  v_lesson    = box.getString("p_lesson");
        int     v_ordseq    = box.getInt("p_ordseq");
        int     v_getcnt    = box.getInt("p_getcnt"); // 제출갯수
        int     v_ishalf    = 0;  // 중간데이타 존재여부
        int     v_cnt       = 0;
        try { 
            connMgr = new DBConnectionManager();

            // 중간저장 테이블에 데이타 여부
            // if ( v_getcnt < 1) { 
            //    sql  = " select count(*) from tz_projrephalf ";
            //    sql += " where subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' and ordseq='" +v_ordseq + "' and projid = '" +v_projgrp + "' ";
            //    ls = connMgr.executeQuery(sql);
            //    if ( ls.next() ) v_ishalf = ls.getInt(1);
            // }
						
					 sql  = " select count(*) ";
					 sql += "   from TZ_PROJREP ";
					 sql += "  where subj = " +StringManager.makeSQL(v_subj) + " \n";
					 sql += "    and year = " +StringManager.makeSQL(v_year) + " \n";
					 sql += "    and subjseq = " +StringManager.makeSQL(v_subjseq) + " \n";
					 sql +=  "    and ordseq=" +v_ordseq + " and projid=" +StringManager.makeSQL(v_projgrp) + " \n";
					 //System.out.println("sql == >> " +sql);
					 
					 ls = connMgr.executeQuery(sql);
					 if ( ls.next() ) { 
					 	v_cnt = ls.getInt(1);
					 }
					 ls.close();
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

            if ( v_cnt > 0 ) { 
                // 제출내용 보여줌
                sql = "select A.title,A.contents,A.score,A.upfile,A.realfile,A.upfilesize,A.tucontents,A.score_mas,a.isret,a.retreason, 'Y' issubmit ";
                sql += "from TZ_PROJREP A ";
                sql += "where A.subj=" +StringManager.makeSQL(v_subj) + " and A.year=" +StringManager.makeSQL(v_year) + " and A.subjseq=" +StringManager.makeSQL(v_subjseq) + " ";
                sql += "and A.ordseq=" +v_ordseq + " and A.projid=" +StringManager.makeSQL(v_projgrp) + "";// and A.lesson='" +v_lesson + "' ";
                ls = connMgr.executeQuery(sql);
                if ( ls.next() ) { 
                    data = new ReportData();
                    data.setTitle( ls.getString("title") );
                    data.setContents( ls.getCharacterStream("contents") );
                    data.setScore( ls.getInt("score") );
                    data.setUpfile( ls.getString("upfile") );
                    data.setRealfile( ls.getString("realfile") );
                    data.setUpfilesize( ls.getString("upfilesize") );
                    data.setTucontents( ls.getString("tucontents") );
                    data.setScore_mas( ls.getInt("score_mas") );
                    data.setIsret( ls.getString("isret") );
                    data.setRetreason( ls.getString("retreason") );
                    data.setIssubmit("Y");
                }
            } else { 
                // 중간저장 데이타 보여줌
                sql  = " select title, contents, upfile, realfile, upfilesize, ldate, 'N' issubmit  from tz_projrephalf ";
                sql += " where subj=" +StringManager.makeSQL(v_subj) + " and year=" +StringManager.makeSQL(v_year) + " and subjseq=" +StringManager.makeSQL(v_subjseq) + " and ordseq=" +v_ordseq + " and projid = " +StringManager.makeSQL(v_projgrp) + " ";
                ls = connMgr.executeQuery(sql);
                if ( ls.next() ) { 
                    data = new ReportData();
                    data.setTitle( ls.getString("title") );
                    data.setContents( ls.getCharacterStream("contents") );
                    data.setScore(0);
                    data.setUpfile( ls.getString("upfile") );
                    data.setRealfile( ls.getString("realfile") );
                    data.setUpfilesize( ls.getString("upfilesize") );
                    data.setTucontents("");
                    data.setScore_mas(0);
                    data.setIsret("");
                    data.setRetreason("");
                    data.setIssubmit("N");
                }
            }
            
            // System.out.println("data.setIssubmit(N) == = >> " +data.getIssubmit() );
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data;
    }
    
    /**
    과제 제출갯수 반환
    @param box      receive from the form object and session
    @return ReportData
    */
     public int selectReportRepCount(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        String sql          = "";
        ReportData data   = null;
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_reptype   = box.getString("p_reptype");
        String  v_projgrp   = box.getString("p_projgrp");
        String  v_lesson    = box.getString("p_lesson");
        int     v_ordseq    = box.getInt("p_ordseq");
        int     v_getcnt    = box.getInt("p_getcnt"); // 제출갯수
        int     v_cnt       = 0;
        try { 
            connMgr = new DBConnectionManager();

			 sql  = " select count(*) ";
			 sql += "   from TZ_PROJREP ";
			 sql += "  where subj = " +StringManager.makeSQL(v_subj) + "";
			 sql += "    and year = " +StringManager.makeSQL(v_year) + "";
			 sql += "    and subjseq = " +StringManager.makeSQL(v_subjseq) + "";
			 sql +=  "    and ordseq='" +v_ordseq + " and projid=" +StringManager.makeSQL(v_projgrp) + "";
			 //System.out.println("sql == >> " +sql);
			 
			 ls = connMgr.executeQuery(sql);
			 if ( ls.next() ) { 
			 	v_cnt = ls.getInt(1);
			 }
			 ls.close();
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_cnt;
    }

    /**
    동료평가점수 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectCoprepList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        String sql          = "";
        ReportData data   = null;
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_projgrp   = box.getString("p_projgrp");          // 제출자 ID
        String  v_lesson    = box.getString("p_lesson");
        int     v_ordseq    = box.getInt("p_ordseq");
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            // select couserid,score,ldate,coname
            sql = "select couserid,score,ldate, ";
            sql += "(select name from TZ_MEMBER where userid=A.couserid) coname ";
            sql += "from TZ_COPREP A ";
            sql += "where subj=" +StringManager.makeSQL(v_subj);
            sql += " and year=" +StringManager.makeSQL(v_year) + " and subjseq=" +StringManager.makeSQL(v_subjseq);
            sql += " and ordseq=" +SQLString.Format(v_ordseq) + " and userid=" +StringManager.makeSQL(v_projgrp);
            // order by A.lesson) ";
            ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    data = new ReportData();
                    data.setCouserid( ls.getString("couserid") );
                    data.setConame( ls.getString("coname") );
                    data.setScore( ls.getInt("score") );
                    data.setLdate( ls.getString("ldate") );
                    list.add(data);
                }
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
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
     과제 등록 (서술형 등록)
     @param box      receive from the form object and session
     @return int
     */
      public int ReportInsertP(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet ls                  = null;
         PreparedStatement pstmt     = null;
         PreparedStatement pstmt2     = null;
         String sql 				 = "";
         String sql2 				 = "";
         int isOk                    = 0;
         int isOk1                   = 0;

         String  s_userid           = box.getSession("userid");			// 사용자아이디
         String  s_userip           = box.getSession("userip");			// 사용자IP
         String  v_subj              = box.getString("p_subj");         // 과정
         String  v_year              = box.getString("p_year");         // 년도
         String  v_subjseq           = box.getString("p_subjseq");      // 과정기수        
         String  v_grpseq            = box.getString("p_grpseq");		// 문제지번호
         
         Vector  ordseq			    = box.getVector("p_ordseq");		//문제번호
         
         String v_realFileName1      = box.getRealFileName("p_file1");	//서술형 답변 파일
         String v_newFileName1       = box.getNewFileName ("p_file1");	//서술형 답변파일

         try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false);
             
             sql =  " insert into TZ_PROJREP(seq, subj, year, subjseq, grpseq, ordseq, projid, luserid, ldate, ISFINAL , isret, retmailing ) 	\n";
             sql += " values ( ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'),'Y','N','N', ? )		 					\n";
   														
             
             for ( int i=0; i < ordseq.size();i++ ) {
            	 pstmt = connMgr.prepareStatement(sql);
                 int v_ordseq = Integer.parseInt((String)ordseq.elementAt(i));
                 pstmt.setInt   (1, 1);
                 pstmt.setString(2, v_subj);
                 pstmt.setString(3, v_year);
                 pstmt.setString(4, v_subjseq);
                 pstmt.setInt   (5, Integer.parseInt(v_grpseq));
                 pstmt.setInt   (6, v_ordseq);
                 pstmt.setString(7, s_userid);
                 pstmt.setString(8, s_userid);
                 pstmt.setString(9, s_userip);
                 
                 isOk = pstmt.executeUpdate();
             
                 if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e ) { } }
             }
             if ( isOk >0 ) {
 	            sql2  = " UPDATE TZ_PROJASSIGN SET REALREPFILE = ?, UPREPFILE =? 			\n";
 	            sql2 += " 		, repdate = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')				\n";
 	            sql2 += " 		, LUSERID = ?	\n";
 	            sql2 += " 		, user_ip = ?	\n";
 	            sql2 += " 		, INDATE = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')	\n";
 	            sql2 += " WHERE SUBJ = " + StringManager.makeSQL(v_subj) + " \n";
 	            sql2 += "	AND YEAR= " + StringManager.makeSQL(v_year) + " \n";
 	            sql2 += "	AND SUBJSEQ= " + StringManager.makeSQL(v_subjseq) + " \n";
 	            sql2 += "	AND grpseq= " + v_grpseq + " \n";
 	            sql2 += "	AND userid= " + StringManager.makeSQL(s_userid) + " \n";
 	            pstmt2 = null;
 	            pstmt2 = connMgr.prepareStatement(sql2);
 	            pstmt2.setString(1, v_realFileName1 );
 	            pstmt2.setString(2, v_newFileName1  );
 	            pstmt2.setString(3, s_userid );
 	            pstmt2.setString(4, s_userip );
 	            isOk1 = pstmt2.executeUpdate();
 	            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
             }
             
             // 파일 해당 경로로 이동 
             ConfigSet conf = new ConfigSet();
             String system_slash = File.separator;    //   해당 시스템의 분리자를 얻는다
             File dir = new File(conf.getProperty("dir.upload.report")+v_year);
             if(!dir.exists()) {
           	  dir.mkdir();
             }
             dir = new File(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj);
             if(!dir.exists()) {
           	  dir.mkdir();
             }
             dir = new File(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj+system_slash+v_subjseq);
             if(!dir.exists()) {
           	  dir.mkdir();
             }
             dir = new File(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj+system_slash+v_subjseq+system_slash+v_grpseq);
             if(!dir.exists()) {
           	  dir.mkdir();
             }
             FileMove fm = new FileMove();
             fm.move(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj+system_slash+v_subjseq+system_slash+v_grpseq
             		, conf.getProperty("dir.upload.report") 
             		, v_newFileName1);
             
             File dir1 = new File(conf.getProperty("dir.upload.report")+v_newFileName1);
             dir1.delete();
             
 			if ( isOk > 0 && isOk1 >0) { 
 				connMgr.commit();
 			} else { 
 				connMgr.rollback();
 			}
             
         } catch ( Exception ex ) { 
             if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
             if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e ) { } }
             if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return isOk;
     }
    /**
    과제 등록 (답변형 등록)
    @param box      receive from the form object and session
    @return int
    */
     public int ReportInsertS(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls                  = null;
        PreparedStatement pstmt     = null;
        PreparedStatement pstmt2    = null;
        String sql 					= "";
        String sql2					= "";
        int isOk                    = 0;
        int isOk1                   = 0;

        String  s_userid           = box.getSession("userid");			// 사용자아이디
        String  s_userip           = box.getSession("userip");			// 사용자IP
        String  v_subj              = box.getString("p_subj");          // 과정
        String  v_year              = box.getString("p_year");          // 년도
        String  v_subjseq           = box.getString("p_subjseq");       // 과정기수        
        String  v_grpseq            = box.getString("p_grpseq");		// 문제지번호
        
        Vector  ordseq			    = box.getVector("p_ordseq");		// 문제번호
        Vector  answer 				= box.getVector("p_answer");		// 답변
        
        try { 
        // 파일 생성 부분 추가
        // 주의 : 파일이 생성되지 않는다면 에러메세지 형태로 아래의 부분이 수행되면 안됩니다.
        	String answerFile = "";
       	 	String upfilename = "";
            String v_currentDate = FormatDate.getDate("yyyyMMddHHmmss");
            ConfigSet conf = new ConfigSet();
            
            upfilename = "_" +v_currentDate + "_" + s_userid ;       // 새로운 파일명을 만든다
            
			for ( int j=0; j < answer.size();j++ ) { 
				answerFile += (String)answer.elementAt(j);
				answerFile += "\r\n";

			}
			String str = answerFile; // 문자입력을 받는다. 
			String newfilename = "Report_file"+ upfilename +".txt";
            File save_txt = new File(conf.getProperty("dir.upload.report")+ newfilename); // 저장 될 파일명
            PrintWriter pw = new PrintWriter(new FileWriter(save_txt,true));

            pw.println(str); // 파일 쓰기과정

            pw.flush(); // 버퍼에 남아 있을 수 있는 데이터 찌거기 제거.
            pw.close(); // 파일을 쓰고 나면 끝을 알려야 한다(파일을 다 쓰고 닫기).
        // -------------

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sql =  " insert into TZ_PROJREP(seq,subj,year,subjseq,grpseq,ordseq,projid,luserid,ldate, contents, ISFINAL, isret, retmailing, user_ip  ) 	\n";
            sql += " values ( ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?, 'Y','N','N',? )		 					\n";
            for ( int i=0; i < answer.size();i++ ) { 
                String  v_answer = (String)answer.elementAt(i);
                int     v_ordseq = Integer.parseInt((String)ordseq.elementAt(i));
                
                pstmt = connMgr.prepareStatement(sql);
                pstmt.setInt   (1,1);
                pstmt.setString(2,v_subj);
                pstmt.setString(3,v_year);
                pstmt.setString(4,v_subjseq);
                pstmt.setInt   (5,Integer.parseInt(v_grpseq));
                pstmt.setInt   (6,v_ordseq);
                pstmt.setString(7,s_userid);
                pstmt.setString(8,s_userid);
                pstmt.setCharacterStream(9, new StringReader(v_answer), v_answer.length());
                pstmt.setString(10,s_userip);

                isOk = pstmt.executeUpdate();
                if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e ) { } }
            
            }
            
            // 파일이 생성되면 파일명을 저장하도록 합니다.
            if ( isOk >0 ) {
	            sql2  = " UPDATE TZ_PROJASSIGN SET REALREPFILE = ?, UPREPFILE =? 			\n";
	            sql2 += " 		, REALREPFILE2 = ?, UPREPFILE2 = ?                          \n";
	            sql2 += " 		, UPFILE_SIZE = ?, CONTENTS_BYTE = ?                  \n";	
	            sql2 += " 		, CONTENTS = ?                  \n";	 	            
	            sql2 += " 		, repdate = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')				\n";
	            sql2 += " 		, LUSERID = ?	\n";
	            sql2 += " 		, USER_IP = ?	\n";
	            sql2 += " 		, INDATE = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')	\n";
	            sql2 += " WHERE SUBJ = '" + v_subj + "'\n";
	            sql2 += "	AND YEAR= '" + v_year + "'\n";
	            sql2 += "	AND SUBJSEQ= '" + v_subjseq + "'\n";
	            sql2 += "	AND grpseq= '" + v_grpseq + "'\n";
	            sql2 += "	AND userid= '" + s_userid + "'\n";
	            pstmt2 = connMgr.prepareStatement(sql2);
	            System.out.println("파일명 저장  sql ==========>  "+sql2);


	            byte byt[] = answerFile.getBytes();
	            pstmt2.setString(1, newfilename );
	            pstmt2.setString(2, newfilename );
	            pstmt2.setString(3, box.getRealFileName("p_file1"));
	            pstmt2.setString(4, box.getNewFileName("p_file1"));
	            pstmt2.setInt(5,box.getNewFileName("p_file1").length());
	            pstmt2.setInt(6,answerFile.getBytes().length );	     
	            pstmt2.setString(7,answerFile );	    	            
	            pstmt2.setString(8, s_userid );
	            pstmt2.setString(9, s_userip );
	            isOk1 = pstmt2.executeUpdate();
	           
	            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
            }
            System.out.println("report 파일 크기======>   " + box.getNewFileName("p_file1").length());
            // 파일 해당 경로로 이동 
            String system_slash = File.separator;    //   해당 시스템의 분리자를 얻는다
            File dir = new File(conf.getProperty("dir.upload.report")+v_year);
            if(!dir.exists()) {
          	  dir.mkdir();
            }
            dir = new File(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj);
            if(!dir.exists()) {
          	  dir.mkdir();
            }
            dir = new File(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj+system_slash+v_subjseq);
            if(!dir.exists()) {
          	  dir.mkdir();
            }
            dir = new File(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj+system_slash+v_subjseq+system_slash+v_grpseq);
            if(!dir.exists()) {
          	  dir.mkdir();
            }
            FileMove fm = new FileMove();
            fm.move(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj+system_slash+v_subjseq+system_slash+v_grpseq
            		, conf.getProperty("dir.upload.report") 
            		, newfilename);
			
            if ( isOk > 0 && isOk1 >  0 ) {
				connMgr.commit();
			} else { 
				connMgr.rollback();
			}            
        } catch ( Exception ex ) { 
            if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk*isOk1;
    }
     
     /**
     과제 등록 (답변형 수정) - 강사 채점 전에만 수정가능하다. 
     @param box      receive from the form object and session
     @return int
     */
      public int ReportUpdateS(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet ls                  = null;
         PreparedStatement pstmt     = null;
         PreparedStatement pstmt2    = null;
         String sql 					= "";
         String sql2					= "";
         int isOk                    = 0;
         int isOk1                   = 1;

         String  s_userid           = box.getSession("userid");			// 사용자아이디
         String  s_userip           = box.getSession("userip");			// 사용자IP

         String  v_subj             = box.getString("p_subj");          // 과정
         String  v_year             = box.getString("p_year");          // 년도
         String  v_subjseq          = box.getString("p_subjseq");       // 과정기수        
         String  v_grpseq           = box.getString("p_grpseq");		// 문제지번호
         
         Vector  ordseq			    = box.getVector("p_ordseq");//문제번호
         Vector  seq                = box.getVector("p_seq");	// 문제지순번
         Vector  answer 			= box.getVector("p_answer");//답변
         
         String v_preFile			  = box.getString("p_prefile");		// 답변형 이전 답변파일
         
         try { 
         // 파일 생성 부분 추가
         // 주의 : 파일이 생성되지 않는다면 에러메세지 형태로 아래의 부분이 수행되면 안됩니다.
        	 
        	 String answerFile = "";
        	 String upfilename = "";
             String v_currentDate = FormatDate.getDate("yyyyMMddHHmmss");
             ConfigSet conf = new ConfigSet();
             
             upfilename = "_" +v_currentDate + "_" + s_userid ;       // 새로운 파일명을 만든다

        	 for ( int j=0; j < answer.size();j++ ) { 
        		 answerFile += (String)answer.elementAt(j);
        		 answerFile += "\r\n";
        	 }
        	 String str = answerFile; // 문자입력을 받는다. 
        	 String newfilename = "Report_file"+ upfilename +".txt";
             File save_txt = new File(conf.getProperty("dir.upload.report")+ newfilename); // 저장 될 파일명
             PrintWriter pw = new PrintWriter(new FileWriter(save_txt,true));

             pw.println(str); // 파일 쓰기과정

             pw.flush(); // 버퍼에 남아 있을 수 있는 데이터 찌거기 제거.
             pw.close(); // 파일을 쓰고 나면 끝을 알려야 한다(파일을 다 쓰고 닫기).

         // -------------

             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false);
             
             sql = " update tz_projrep \n";
             sql += " set ldate = to_char(sysdate,'YYYYMMDDHH24MISS') \n";
             sql += "   , luserid = ? \n";
             sql += "  , contents = ? \n"; 
             sql += "  , user_ip = ? \n"; 
             sql += "where seq = ?   \n";
             sql += "  and subj = ?   \n";
             sql += "  and year = ?   \n";
             sql += "  and subjseq = ?   \n";
             sql += "  and grpseq = ?    \n";
             sql += "  and ordseq = ?    \n";
             sql += "  and projid = ?   \n";
             sql += "  and isfinal = 'Y'   \n";
									
             for ( int i=0; i < answer.size();i++ ) { 
                 String  v_answer = (String)answer.elementAt(i);
                 int     v_ordseq = Integer.parseInt((String)ordseq.elementAt(i));
                 int     v_seq    = Integer.parseInt((String)seq.elementAt(i));

                 pstmt = connMgr.prepareStatement(sql);
                 pstmt.setString(1,s_userid);                 
                 pstmt.setCharacterStream(2, new StringReader(v_answer), v_answer.length());
                 pstmt.setString(3, s_userip);
                 pstmt.setInt   (4,v_seq);
                 pstmt.setString(5,v_subj);
                 pstmt.setString(6,v_year);
                 pstmt.setString(7,v_subjseq);
                 pstmt.setInt   (8,Integer.parseInt(v_grpseq));
                 pstmt.setInt   (9,v_ordseq);
                 pstmt.setString(10,s_userid);
                 
                 isOk = pstmt.executeUpdate();
                 if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e ) { } }
             
             }
             
             // 파일이 생성되면 파일명을 저장하도록 합니다.
             if ( isOk >0 ) {
 	            sql2  = " UPDATE TZ_PROJASSIGN SET REALREPFILE = ?, UPREPFILE =? 			\n";
 	            if(!box.getRealFileName("p_file1").equals("")) {
 	            	sql2 += " 		, REALREPFILE2 = ?, UPREPFILE2 = ?							\n";
 	            }
 	            sql2 += " 		, repdate = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')				\n";
 	            sql2 += " 		, LUSERID = ?	\n";
 	            sql2 += " 		, user_ip = ?	\n";
 	            sql2 += " WHERE SUBJ = " + StringManager.makeSQL(v_subj) + "				\n";
 	            sql2 += "	AND YEAR= " + StringManager.makeSQL(v_year) + "					\n";
 	            sql2 += "	AND SUBJSEQ= " + StringManager.makeSQL(v_subjseq) + "			\n";
 	            sql2 += "	AND grpseq= " + v_grpseq + "				\n";
 	            sql2 += "	AND userid= " + StringManager.makeSQL(s_userid) + "				\n";
 	            pstmt2 = connMgr.prepareStatement(sql2);
 	            
 	            int index = 1;
 	            pstmt2.setString(index++, newfilename );
 	            pstmt2.setString(index++, newfilename  );
 	            if(!box.getRealFileName("p_file1").equals("")) {
 	        	   pstmt2.setString(index++, box.getRealFileName("p_file1"));
 	        	   pstmt2.setString(index++, box.getNewFileName("p_file1"));
 	            }
 	            pstmt2.setString(index++, s_userid );
 	            pstmt2.setString(index++, s_userip );
 	            isOk1 = pstmt2.executeUpdate();
 	            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
             }

             // 파일 해당 경로로 이동 
             String system_slash = File.separator;    //   해당 시스템의 분리자를 얻는다
             File dir = new File(conf.getProperty("dir.upload.report")+v_year);
             if(!dir.exists()) {
           	  dir.mkdir();
             }
             dir = new File(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj);
             if(!dir.exists()) {
           	  dir.mkdir();
             }
             dir = new File(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj+system_slash+v_subjseq);
             if(!dir.exists()) {
           	  dir.mkdir();
             }
             dir = new File(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj+system_slash+v_subjseq+system_slash+v_grpseq);
             if(!dir.exists()) {
           	  dir.mkdir();
             }
             FileMove fm = new FileMove();
             fm.move(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj+system_slash+v_subjseq+system_slash+v_grpseq
             		, conf.getProperty("dir.upload.report") 
             		, newfilename);
             
             File orgFile = new File(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj+system_slash+v_subjseq+system_slash+v_grpseq+system_slash+v_preFile);
	         orgFile.delete();
	         File dir1 = new File(conf.getProperty("dir.upload.report")+newfilename);
             
 			if ( isOk > 0 && isOk1 > 0) { 
 				connMgr.commit();
 			} else { 
 				connMgr.rollback();
 			}            
         } catch ( Exception ex ) { 
        	 ex.printStackTrace();
             if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
             if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e ) { } }
             if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return isOk;
     }
      /**
      과제 수정 (서술형 수정) - 강사 채점 전에만 수정가능하다. 
      @param box      receive from the form object and session
      @return int
      */
       public int ReportUpdateP(RequestBox box) throws Exception { 
          DBConnectionManager connMgr = null;
          ListSet ls                  = null;
          PreparedStatement pstmt     = null;
          PreparedStatement pstmt2    = null;
          String sql 				  = "";
          String sql2 				  = "";
          int isOk                    = 0;
          int isOk1                   = 0;

          String  s_userid            = box.getSession("userid");		// 사용자아이디
          String  s_userip            = box.getSession("userip");		// 사용자IP
          String  v_subj              = box.getString("p_subj");        // 과정
          String  v_year              = box.getString("p_year");        // 년도
          String  v_subjseq           = box.getString("p_subjseq");     // 과정기수        
          String  v_grpseq            = box.getString("p_grpseq");		// 문제지번호
          
          Vector  ordseq			  = box.getVector("p_ordseq");	// 문제번호
          
          String v_realFileName1      = box.getRealFileName("p_file1");	// 서술형 답변 파일
          String v_newFileName1       = box.getNewFileName ("p_file1");	// 서술형 답변파일
          
          String v_preFile			  = box.getString("p_prefile");		// 서술형 이전 답변파일
          
          try { 
              connMgr = new DBConnectionManager();
              connMgr.setAutoCommit(false);
              //서술형은 tz_projrep 에 수정할게 ldate랑 luserid 밖에 없넹..ㅋ
              sql = " update tz_projrep set 									\n";
              sql += "    luserid = ? 											\n";
              sql += "   ,ldate = to_char(sysdate,'YYYYMMDDHH24MISS') 			\n";
              sql += "   ,user_ip = ? 											\n";
              sql += " where subj 		= ? 									\n";
              sql += "   AND subjseq 	= ? 									\n";
              sql += "   AND year 		= ?  									\n";
              sql += "	 AND grpseq     = ?  									\n";
              sql += "	 AND ORDSEQ     = ?										\n";
              sql += "	 AND projid= " + StringManager.makeSQL(s_userid) + "	\n";
              sql += "	 AND isfinal= 'Y'										\n";
                   
            
              //sql =  " insert into TZ_PROJREP(seq,subj,year,subjseq,grpseq,ordseq,projid,luserid,ldate ) 	\n";
              //sql += " values ( ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS') ) 				\n";
    														
              for ( int i=0; i < ordseq.size();i++ ) {
                  int     v_ordseq = Integer.parseInt((String)ordseq.elementAt(i));

                  pstmt = connMgr.prepareStatement(sql);
                  pstmt.setString(1,s_userid);
                  pstmt.setString(2,s_userip);
                  pstmt.setString(3,v_subj);
                  pstmt.setString(4,v_subjseq);
                  pstmt.setString(5,v_year);
                  pstmt.setInt   (6,Integer.parseInt(v_grpseq));
                  pstmt.setInt   (7,v_ordseq);
                  
                  isOk = pstmt.executeUpdate();
                  if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e ) { } }
              
              }
              if ( isOk >0 ) {
  	            sql2  = " UPDATE TZ_PROJASSIGN SET REALREPFILE = ?, UPREPFILE = ? 			\n";
  	            sql2 += " 		, repdate = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')				\n";
  	            sql2 += " 		, LUSERID = ?												\n";
  	            sql2 += " 		, user_ip = ?												\n";
  	            sql2 += " WHERE SUBJ = " + StringManager.makeSQL(v_subj) + "				\n";
  	            sql2 += "	AND YEAR= " + StringManager.makeSQL(v_year) + "					\n";
  	            sql2 += "	AND SUBJSEQ= " + StringManager.makeSQL(v_subjseq) + "			\n";
  	            sql2 += "	AND grpseq= " + StringManager.makeSQL(v_grpseq) + "				\n";
  	            sql2 += "	AND userid= " + StringManager.makeSQL(s_userid) + "				\n";
  	            pstmt2 = connMgr.prepareStatement(sql2);
  	            
  	            pstmt2.setString(1, v_realFileName1 );
  	            pstmt2.setString(2, v_newFileName1  );
  	            pstmt2.setString(3, s_userid );
  	            pstmt2.setString(4, s_userip );
  	            isOk1 = pstmt2.executeUpdate();
  	            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
              }
              
              
              
              // 파일 해당 경로로 이동 
              ConfigSet conf = new ConfigSet();
              String system_slash = File.separator;    //   해당 시스템의 분리자를 얻는다
              File dir = new File(conf.getProperty("dir.upload.report")+v_year);
              if(!dir.exists()) {
            	  dir.mkdir();
              }
              dir = new File(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj);
              if(!dir.exists()) {
            	  dir.mkdir();
              }
              dir = new File(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj+system_slash+v_subjseq);
              if(!dir.exists()) {
            	  dir.mkdir();
              }
              dir = new File(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj+system_slash+v_subjseq+system_slash+v_grpseq);
              if(!dir.exists()) {
            	  dir.mkdir();
              }
              FileMove fm = new FileMove();
              fm.move(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj+system_slash+v_subjseq+system_slash+v_grpseq
              		, conf.getProperty("dir.upload.report") 
              		, v_newFileName1);
              File orgFile = new File(conf.getProperty("dir.upload.report")+v_year+system_slash+v_subj+system_slash+v_subjseq+system_slash+v_grpseq+system_slash+v_preFile);
	          orgFile.delete();
	          
			if ( isOk > 0 && isOk1 >0) { 
				connMgr.commit();
			} else { 
				connMgr.rollback();
			}
          } catch ( Exception ex ) { 
              if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
              throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
          } finally { 
              if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
              if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e ) { } }
              if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
              if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }

          return isOk;
      }

    /**
    COP 동료평가 페이지 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList sReportojectCopList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        String sql          = "";
        ReportData data   = null;
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_reptype   = box.getString("p_reptype");
        String  v_projgrp   = box.getString("p_projgrp");       // 제출자 ID
        String  v_lesson    = box.getString("p_lesson");
        int     v_ordseq    = box.getInt("p_ordseq");
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            // select name,projgrp,title,indate,score,score2
            sql = "select   A.projid,A.title,A.indate,A.score,";
            sql += "         (select score from TZ_COPREP where subj=A.subj and year=A.year and subjseq=A.subjseq ";
            sql += "                 and ordseq=A.ordseq and userid=A.projid and couserid=" +SQLString.Format(v_projgrp) + ") score2, ";
            sql += "         (select name from TZ_MEMBER where userid=B.userid) name ";
            sql += "from     TZ_PROJREP A,TZ_PROJGRP B ";
            sql += "where    A.subj=" +StringManager.makeSQL(v_subj);
            sql += " and A.year=" +StringManager.makeSQL(v_year) + " and A.subjseq=" +StringManager.makeSQL(v_subjseq);
            sql += " and A.ordseq=" +SQLString.Format(v_ordseq) + " and A.subj=B.subj and A.year=B.year ";
            sql += " and A.subjseq=B.subjseq and A.ordseq=B.ordseq and A.projid=B.userid and B.userid < > " +StringManager.makeSQL(v_projgrp);
            sql += " order by B.userid";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new ReportData();
                data.setName( ls.getString("name") );
                data.setProjgrp( ls.getString("projgrp") );
                data.setTitle( ls.getString("title") );
                data.setIndate( ls.getString("indate") );
                data.setScore( ls.getInt("score") );
                data.setScore2( ls.getInt("score2") );
                list.add(data);
            }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
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
    COP 학습자 과제 평가
    @param box      receive from the form object and session
    @return int
    */
     public int updateReportJudge(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        PreparedStatement pstmt3 = null;
        String sql1         = "";
        String sql2         = "";
        String sql3         = "";
        String sql4         = "";
        ReportData data1  = null;
        int isOk1           = 0;
        int isOk2           = 0;
        int isOk3           = 0;
        int isOk4           = 0;
        String  v_userid    = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        String  v_reptype   = box.getString("p_reptype");
        String  v_lesson    = box.getString("p_lesson");
        int     v_ordseq    = box.getInt("p_ordseq");


        // p_supcheck로 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector v_score      = new Vector();
        Vector v_chk        = new Vector();
        v_score             = box.getVector("p_score");
        v_chk               = box.getVector("p_chk");
        Enumeration em1     = v_chk.elements();
        Enumeration em2     = v_score.elements();
        String v_projgrp    = "";   // 실제 넘어온 각각의 value
        int    v_chkscore   =  0;   // 실제 넘어온 각각의 value
        int    v_cnt        =  0;
        long   v_finalscore =  0;
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // insert TZ_COPREP table
            sql3 = "insert into TZ_COPREP(subj,year,subjseq,ordseq,userid,seq,couserid,score,luserid,ldate) ";
            sql3 += "values(?,?,?,?,?,1,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'))";
            pstmt3 = connMgr.prepareStatement(sql3);

            while ( em1.hasMoreElements() ) { 

                v_projgrp   = (String)em1.nextElement();
                v_chkscore  = Integer.parseInt((String)em2.nextElement() );

                // select cnt,score,score_mas
                sql1 = "select (select count(subj) from TZ_PROJGRP where subj=A.subj and year=A.year and subjseq=A.subjseq and ordseq=A.ordseq) cnt,";
                sql1 += "(select sum(score) from TZ_COPREP where subj=A.subj and year=A.year and subjseq=A.subjseq and ordseq=A.ordseq ";
                sql1 += "and userid=A.projid and couserid < > " +SQLString.Format(v_userid) + ") score, score_mas ";
                sql1 += "from TZ_PROJREP A ";
                sql1 += "where A.subj=" +StringManager.makeSQL(v_subj);
                sql1 += " and A.year=" +StringManager.makeSQL(v_year) + " and A.subjseq=" +StringManager.makeSQL(v_subjseq);
                sql1 += " and A.ordseq=" +SQLString.Format(v_ordseq) + " and A.projid=" +StringManager.makeSQL(v_projgrp);

                ls1 = connMgr.executeQuery(sql1);

                if ( ls1.next() ) { 
                    v_cnt = ls1.getInt("cnt");
                    v_finalscore = v_chkscore + ls1.getInt("score");
                    v_finalscore = v_finalscore / v_cnt;
                    v_finalscore = StrictMath.round( ls1.getInt("score_mas")*0.5 + v_finalscore*0.5);
                }

                // delete TZ_COPREP table
                sql2 = "delete TZ_COPREP  ";
                sql2 += "where subj=" +StringManager.makeSQL(v_subj);
                sql2 += " and year=" +StringManager.makeSQL(v_year) + " and subjseq=" +StringManager.makeSQL(v_subjseq);
                sql2 += " and ordseq=" +SQLString.Format(v_ordseq) + " and userid=" +StringManager.makeSQL(v_projgrp);
                sql2 += " and couserid=" +StringManager.makeSQL(v_userid);
                isOk2= connMgr.executeUpdate(sql2);

                pstmt3.setString(1, v_subj);
                pstmt3.setString(2, v_year);
                pstmt3.setString(3, v_subjseq);
                pstmt3.setInt(4, v_ordseq);
                pstmt3.setString(5, v_projgrp);
                pstmt3.setString(6, v_userid);
                pstmt3.setInt(7, v_chkscore);
                pstmt3.setString(8, v_userid);
                isOk3 = pstmt3.executeUpdate();

                // update TZ_PROJREP table
                sql4 = "update TZ_PROJREP set score=" +SQLString.Format((int)v_finalscore);
                sql4 += "where subj=" +StringManager.makeSQL(v_subj);
                sql4 += " and year=" +StringManager.makeSQL(v_year) + " and subjseq=" +StringManager.makeSQL(v_subjseq);
                sql4 += " and ordseq=" +SQLString.Format(v_ordseq) + " and projid=" +StringManager.makeSQL(v_projgrp);
                isOk4= connMgr.executeUpdate(sql4);

                if ( isOk3 > 0 && isOk4 > 0) connMgr.commit();
                else connMgr.rollback();

                // ==  ==  ==  == =과제 점수반영
                int isOk5 = CalcUtil.getInstance().calc_score(connMgr,CalcUtil.REPORT,v_subj,v_year,v_subjseq,v_projgrp);
            }
        } catch ( Exception ex ) { 
            if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( pstmt3 != null )  { try { pstmt3.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk3*isOk4;
    }


    /**
    문제풀에서 문제  자동 배정
    @param box      receive from the form object and session
    @return int
    */
     /*public void updateReportAssign(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        PreparedStatement   pstmt   = null;
        String sql         = "";
        // String sql2          = "";

        // ListSet ls          = null;

        String  v_isgoyong  = "N";

        int isOk           = 0;

        String  v_userid    = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);


            // sql2 = "select isgoyong from tz_subjseq where subj='" + v_subj + "' and year='" + v_year + "' and subjseq='" + v_subjseq + "' and isgoyong='Y'";
            // ls = connMgr.executeQuery(sql2);

            // if ( ls.next() ) { 
            //  v_isgoyong = "Y";
            // }

            // System.out.println("v_isgoyong=" +v_isgoyong);

            sql = "insert into tz_projassign(subj, year, subjseq, ordseq, userid, projseq, ldate) \n";
            sql += "select '" + v_subj + "','" + v_year + "','" + v_subjseq + "',ordseq, '" + v_userid + "', projseq, to_char(sysdate, 'YYYYMMDDHHMMss') \n";
            sql += "from ( \n";
            sql += "select projseq, \n";
            sql += "    min(assigncnt) assigncnt, \n";
            sql += "    min(ordseq) ordseq \n";
            sql += "from  ( \n";
            sql += "     \n";// --사용자에게 배정이 안된 과제기수(projseq) 중에서
            sql += "    select a.projseq, \n";
            sql += "           a.ordseq, \n";
            sql += "           (select count(*) \n";
            sql += "            from   tz_projassign \n";
            sql += "            where  subj    = a.subj and \n";
            sql += "                   year    = a.year and \n";
            sql += "                   subjseq = a.subjseq and \n";
            sql += "                   ordseq  = a.ordseq) assigncnt \n";
            sql += "       from   tz_projord a \n";
            sql += "    where  a.subj    = '" + v_subj + "' and \n";
            sql += "           a.year    = '" + v_year + "' and \n";
            sql += "           a.subjseq = '" + v_subjseq + "' and \n";
            sql += "           a.projseq in ( \n";
            sql += "                          \n";// --사용자에게 배정이 안된 과제기수(projseq) 구하기 시작
            sql += "                         select distinct projseq \n";
            sql += "                         from   tz_projord \n";
            sql += "                         where  subj    = '" + v_subj + "' and \n";
            sql += "                                year    = '" + v_year + "' and \n";
            sql += "                                subjseq = '" + v_subjseq + "' and \n";
            sql += "                                projseq not in ( \n";
            sql += "                                        \n ";// --사용자에게 배정된 과제기수(projseq) 구하기 시작
            sql += "                                        select (select projseq \n";
            sql += "                                                from   tz_projord \n";
            sql += "                                                where  a.subj  = subj and \n";
            sql += "                                                       year    = a.year and \n";
            sql += "                                                       subjseq = a.subjseq and \n";
            sql += "                                                       ordseq  = a.ordseq) projseq \n";
            sql += "                                        from   tz_projassign a \n";
            sql += "                                        where  a.subj    = '" + v_subj + "' and \n";
            sql += "                                               a.year    = '" + v_year + "' and \n";
            sql += "                                               a.subjseq = '" + v_subjseq + "' and \n";
            sql += "                                               a.userid  = '" + v_userid + "' \n";
            sql += "                                        \n";// --사용자에게 배정된 과제기수(projseq) 구하기 종료
            sql += "                                ) \n";
            sql += "                         \n";// --사용자에게 배정이 안된 과제기수(projseq) 구하기 종료

            // if ( v_isgoyong.equals("Y") ) { 
            //  // 고용보험 과목일 경우에 대해서만 5배수 체크함.
            //  sql += "                         and "; // --과제 기수에 대해 5배수 이상 등록되어 있는 과제 기수만 배정하기 시작
            //  sql += "                         ";
            //  sql += "                             (select count(*) ";
            //  sql += "                              from   tz_projord ";
            //  sql += "                              where  subj    = a.subj and ";
            //  sql += "                                     year    = a.year and ";
            //  sql += "                                     subjseq = a.subjseq and ";
            //  sql += "                                     projseq = a.projseq ) >= 5 ";
            //  sql += "                             ";// --과제 기수에 대해 5배수 이상 등록되어 있는 과제 기수만 배정하기 종료
            // }

            sql += "            ) \n";
            sql += " \n";
            sql += ") a \n";
            sql += "group by a.projseq, a.assigncnt  \n";

            sql += "having    (  \n";

            sql += "         a.assigncnt < \n";
            sql += "         \n";
            sql += "         decode ( \n";
            sql += "         (select nvl(max((select count(*) \n";
            sql += "                 from   tz_projassign \n";
            sql += "                 where  subj=a.subj and \n";
            sql += "                        year = a.year and \n";
            sql += "                        subjseq = a.subjseq and \n";
            sql += "                        projseq = a.projseq and \n";
            sql += "                        ordseq = a.ordseq)),0) \n";
            sql += "         from   tz_projord a \n";
            sql += "         where  subj    = '" + v_subj + "' and \n";
            sql += "                year    = '" + v_year + "' and \n";
            sql += "                subjseq = '" + v_subjseq + "' and \n";
            sql += "                                        projseq = a.projseq ) \n";
            sql += "                                        , \n";
            sql += "         \n";
            sql += "         (select nvl(min((select count(*) \n";
            sql += "                 from   tz_projassign \n";
            sql += "                 where  subj=a.subj and \n";
            sql += "                        year = a.year and \n";
            sql += "                        subjseq = a.subjseq and \n";
            sql += "                        projseq = a.projseq and \n";
            sql += "                        ordseq = a.ordseq)),0) \n";
            sql += "         from   tz_projord a \n";
            sql += "         where  subj    = '" + v_subj + "' and  \n";
            sql += "                year    = '" + v_year + "' and \n";
            sql += "                subjseq = '" + v_subjseq + "' and \n";
            sql += "                projseq = a.projseq ) \n";
            sql += "                                        , \n";
            sql += "                                        \n";
            sql += "         (select nvl(max((select count(*) \n";
            sql += "                 from   tz_projassign \n";
            sql += "                 where  subj=a.subj and \n";
            sql += "                        year = a.year and \n";
            sql += "                        subjseq = a.subjseq and \n";
            sql += "                        projseq = a.projseq and \n";
            sql += "                        ordseq = a.ordseq)),0) +1 \n";
            sql += "         from   tz_projord a \n";
            sql += "         where  subj    = '" + v_subj + "' and  \n";
            sql += "                year    = '" + v_year + "' and \n";
            sql += "                subjseq = '" + v_subjseq + "' and \n";
            sql += "                projseq = a.projseq ) \n";
            sql += "                                        , \n";
            sql += "         \n";
            sql += "         (select nvl(max((select count(*) \n";
            sql += "                 from   tz_projassign \n";
            sql += "                 where  subj=a.subj and \n";
            sql += "                        year = a.year and \n";
            sql += "                        subjseq = a.subjseq and \n";
            sql += "                        projseq = a.projseq and \n";
            sql += "                        ordseq = a.ordseq)),0) \n";
            sql += "         from   tz_projord a \n";
            sql += "         where  subj    = '" + v_subj + "' and \n";
            sql += "                year    = '" + v_year + "' and \n";
            sql += "                subjseq = '" + v_subjseq + "' and \n";
            sql += "                projseq = a.projseq ) \n";
            sql += "                                        \n";
            sql += "         \n";
            sql += "         ) \n";
            sql += "         \n";
            sql += "         \n";
            sql += "         ) \n";



            sql += ") \n";



            pstmt = connMgr.prepareStatement(sql);
            // System.out.println("assign sql = " + sql);
Log.info.println("배정 >> " +sql + "<<배정 >> ");
            pstmt.executeUpdate();

            connMgr.commit();

            }

        catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            // if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
    }*/

    /**
    문제풀에서 문제셋트  자동 배정
    @param box      receive from the form object and session
    @return int
    */
     public void updateReportAssign(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet ls          = null;
        ArrayList list      = null;
        String sql          = "";
        int isOk            = 0;

        String  v_userid    = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        int     v_projseq   = 1;                                // 셋트번호

        String  s_assign    = box.getString("p_assign");        // 배정flag(***운영자화면에서 재제출시..)
        int     s_projseq   = box.getInt("p_projseq");          // 셋트번호(***운영자화면에서 재제출시..)
        String  s_userid    = box.getString("p_userid");        // id      (***운영자화면에서 재제출시..)


        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);


            if ( s_assign.equals("") ) {        // 학습창에서 배정받음

            	sql  = " select a.subj, a.year, a.subjseq, a.grpseq, 							\n";
            	sql += " (select count(*) from tz_projassign x where a.subj = x.subj and a.year = x.year and a.subjseq = x.subjseq and a.grpseq = x.grpseq) pcnt \n";
            	sql += " from tz_projgrp a 														\n";
            	sql += " where a.subj = " +StringManager.makeSQL(v_subj) + "     				\n";
            	sql += " and a.subjseq = " +StringManager.makeSQL(v_subjseq) + "     			\n";
            	sql += " and a.year = " +StringManager.makeSQL(v_year) + "     					\n";
            	sql += " group by subj, year, subjseq, grpseq 									\n";
            	sql += " order by pcnt, grpseq	 												\n";	//가장 적게 배정받은 번호를 order by 해준다. 
            	
            	ls = connMgr.executeQuery(sql);
            	if ( ls.next() ) v_projseq = ls.getInt("grpseq");
            	if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }

            } else {                            // 운영자화면에서 재제출시 배정받음
                v_projseq= s_projseq;
                v_userid = s_userid;
            }

            // 배정정보 등록
            sql = " insert into tz_projassign(subj, year, subjseq, userid, grpseq) "
                + " select subj, year, subjseq, ?, grpseq "
                + " from (SELECT subj, year, subjseq, grpseq  "
                + "       FROM TZ_PROJGRP WHERE subj=? AND year=? AND subjseq=? and grpseq=? ) ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, v_userid);
            pstmt.setString(2, v_subj);
            pstmt.setString(3, v_year);
            pstmt.setString(4, v_subjseq);
            pstmt.setInt   (5, v_projseq);
            isOk = pstmt.executeUpdate();
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }

            if ( isOk > 0 ) { 
                connMgr.commit();
            }

        } catch ( Exception ex ) { 
            if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
    }
     /**
    문제풀에서 문제셋트  자동 배정(중간)
    @param box      receive from the form object and session
    @return int
      */
     public void updateReportMAssign(RequestBox box) throws Exception { 
    	 DBConnectionManager	connMgr	= null;
    	 PreparedStatement   pstmt   = null;
    	 ListSet ls          = null;
    	 ArrayList list      = null;
    	 String sql          = "";
    	 int isOk            = 0;
    	 
    	 String  v_userid    = box.getSession("userid");
    	 String  v_subj      = box.getString("p_subj");          // 과목
    	 String  v_year      = box.getString("p_year");          // 년도
    	 String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
    	 int     v_projseq   = 0;                                // 셋트번호
    	 
    	 String  s_assign    = box.getString("p_assign");        // 배정flag(***운영자화면에서 재제출시..)
    	 int     s_projseq   = box.getInt("p_projseq");          // 셋트번호(***운영자화면에서 재제출시..)
    	 String  s_userid    = box.getString("p_userid");        // id      (***운영자화면에서 재제출시..)
    	 
    	 int v_mreport_start = 0;
    	 int v_mreport_end = 0;
    	 int v_today = Integer.parseInt(FormatDate.getDate("yyyyMMdd"));
    	 
    	 try { 
    		 connMgr = new DBConnectionManager();
    		 connMgr.setAutoCommit(false);
    		 
    		 // 학습자권한일 경우만 배정받을 수 있다.
    		 if ("ZZ".equals(box.getSession("gadmin"))) {
	    		 // 기간에 해당될때만 리포트 배정을 받을수 있다.
	    		 sql = "\n select mreport_start, mreport_end "
	    			 + "\n from   tz_subjseq "
	    			 + "\n where  subj = " + StringManager.makeSQL(v_subj)
	    			 + "\n and    year = " + StringManager.makeSQL(v_year)
	    			 + "\n and    subjseq = " + StringManager.makeSQL(v_subjseq);
				 ls = connMgr.executeQuery(sql);
				 if ( ls.next() ) {
					 v_mreport_start = ls.getInt("mreport_start");
					 v_mreport_end   = ls.getInt("mreport_end");
				 }
				 if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
				 
				 //if (v_today >= v_mreport_start && v_today <= v_mreport_end) {
	
		    		 if ( s_assign.equals("") ) {        // 학습창에서 배정받음
		    			 
		    			 sql  = " select a.subj, a.year, a.subjseq, a.grpseq, 								\n";
		    			 sql += " (select count(*) from tz_projassign x where a.subj = x.subj and a.year = x.year and a.subjseq = x.subjseq and a.grpseq = x.grpseq) pcnt \n";
		    			 sql += " from tz_projgrp a 														\n";
		    			 sql += " where a.subj = " +StringManager.makeSQL(v_subj) + "     					\n";
		    			 sql += " and a.subjseq = " +StringManager.makeSQL(v_subjseq) + "     				\n";
		    			 sql += " and a.year = " +StringManager.makeSQL(v_year) + "     					\n";
		    			 sql += " and a.projgubun = 'M'		     											\n";
		    			 sql += " group by subj, year, subjseq, grpseq 										\n";
		    			 sql += " order by pcnt, grpseq	 													\n";	//가장 적게 배정받은 번호를 order by 해준다. 
		    			 
		    			 ls = connMgr.executeQuery(sql);
		    			 if ( ls.next() ) v_projseq = ls.getInt("grpseq");
		    			 if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
		    			 
		    		 } else {                            // 운영자화면에서 재제출시 배정받음
		    			 v_projseq= s_projseq;
		    			 v_userid = s_userid;
		    		 }
		    		 
		    		 // 배정정보 등록
		    		 sql = " insert into tz_projassign(subj, year, subjseq, userid, grpseq) "
		    			 + " select subj, year, subjseq, ?, grpseq "
		    			 + " from (SELECT subj, year, subjseq, grpseq  "
		    			 + "       FROM TZ_PROJGRP WHERE subj=? AND year=? AND subjseq=? and grpseq=? ) ";
		    		 
		    		 pstmt = connMgr.prepareStatement(sql);
		    		 pstmt.setString(1, v_userid);
		    		 pstmt.setString(2, v_subj);
		    		 pstmt.setString(3, v_year);
		    		 pstmt.setString(4, v_subjseq);
		    		 pstmt.setInt   (5, v_projseq);
		    		 isOk = pstmt.executeUpdate();
		    		 if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
		    		 if ( isOk > 0 ) { 
		    			 connMgr.commit();
		    		 }
	
				 //}
    		 }
    		 
    	 } catch ( Exception ex ) { 
             if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
    		 ErrorManager.getErrorStackTrace(ex, box, sql);
    		 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	 }finally { 
    		 if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
    		 if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
    		 if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
    		 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	 }
     }
     /**
    문제풀에서 문제셋트  자동 배정 (기말)
    @param box      receive from the form object and session
    @return int
      */
     public void updateReportFAssign(RequestBox box) throws Exception { 
    	 DBConnectionManager	connMgr	= null;
    	 PreparedStatement   pstmt   = null;
    	 ListSet ls          = null;
    	 ArrayList list      = null;
    	 String sql          = "";
    	 int isOk            = 0;
    	 
    	 String  v_userid    = box.getSession("userid");
    	 String  v_subj      = box.getString("p_subj");          // 과목
    	 String  v_year      = box.getString("p_year");          // 년도
    	 String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
    	 int     v_projseq   = 0;                                // 셋트번호
    	 
    	 String  s_assign    = box.getString("p_assign");        // 배정flag(***운영자화면에서 재제출시..)
    	 int     s_projseq   = box.getInt("p_projseq");          // 셋트번호(***운영자화면에서 재제출시..)
    	 String  s_userid    = box.getString("p_userid");        // id      (***운영자화면에서 재제출시..)
    	 
    	 int v_freport_start = 0;
    	 int v_freport_end = 0;
    	 int v_today = Integer.parseInt(FormatDate.getDate("yyyyMMdd"));

    	 try { 
    		 connMgr = new DBConnectionManager();
    		 connMgr.setAutoCommit(false);
    		 
    		 // 학습자권한일 경우만 배정받을 수 있다.
    		 if ("ZZ".equals(box.getSession("gadmin"))) {    		 
	    		 // 기간에 해당될때만 리포트 배정을 받을수 있다.
	    		 sql = "\n select freport_start, freport_end "
	    			 + "\n from   tz_subjseq "
	    			 + "\n where  subj = " + StringManager.makeSQL(v_subj)
	    			 + "\n and    year = " + StringManager.makeSQL(v_year)
	    			 + "\n and    subjseq = " + StringManager.makeSQL(v_subjseq);
	
	    		 ls = connMgr.executeQuery(sql);
				 if ( ls.next() ) {
					 v_freport_start = ls.getInt("freport_start");
					 v_freport_end   = ls.getInt("freport_end");
				 }
				 if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
				 
				 //if (v_today >= v_freport_start && v_today <= v_freport_end) {
		    		 
		    		 if ( s_assign.equals("") ) {        // 학습창에서 배정받음
		    			 
		    			 sql  = " select a.subj, a.year, a.subjseq, a.grpseq, 									\n";
		    			 sql += " (select count(*) from tz_projassign x where a.subj = x.subj and a.year = x.year and a.subjseq = x.subjseq and a.grpseq = x.grpseq) pcnt \n";
		    			 sql += " from tz_projgrp a 															\n";
		    			 sql += " where a.subj = " +StringManager.makeSQL(v_subj) + "     						\n";
		    			 sql += " and a.subjseq = " +StringManager.makeSQL(v_subjseq) + "     					\n";
		    			 sql += " and a.year = " +StringManager.makeSQL(v_year) + "     						\n";
		    			 sql += " and a.projgubun = 'F'		     												\n";	//기말 리포트 번호만 가져온다.
		    			 sql += " group by subj, year, subjseq, grpseq 											\n";
		    			 sql += " order by pcnt, grpseq	 														\n";	//가장 적게 배정받은 번호를 order by 해준다. 
		    			 
		    			 ls = connMgr.executeQuery(sql);
		    			 if ( ls.next() ) v_projseq = ls.getInt("grpseq");
		    			 if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
		    			 
		    		 } else {                            // 운영자화면에서 재제출시 배정받음
		    			 v_projseq= s_projseq;
		    			 v_userid = s_userid;
		    		 }
		    		 
		    		 // 배정정보 등록
		    		 sql = " insert into tz_projassign(subj, year, subjseq, userid, grpseq) "
		    			 + " select subj, year, subjseq, ?, grpseq "
		    			 + " from (SELECT subj, year, subjseq, grpseq  "
		    			 + "       FROM TZ_PROJGRP WHERE subj=? AND year=? AND subjseq=? and grpseq=? ) ";
		    		 
		    		 pstmt = connMgr.prepareStatement(sql);
		    		 pstmt.setString(1, v_userid);
		    		 pstmt.setString(2, v_subj);
		    		 pstmt.setString(3, v_year);
		    		 pstmt.setString(4, v_subjseq);
		    		 pstmt.setInt   (5, v_projseq);
		    		 isOk = pstmt.executeUpdate();
		    		 if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
		    		 if ( isOk > 0 ) { 
		    			 connMgr.commit();
		    		 }
				 //}
			 }
    	 } catch ( Exception ex ) { 
             if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
    		 ErrorManager.getErrorStackTrace(ex, box, sql);
    		 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	 }finally { 
    		 if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
    		 if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
    		 if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
    		 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	 }
     }

    /**
    학습자 배정여부 확인
    @param box      receive from the form object and session
    @return int     배정셋트갯수
    */
     public int IsReportAssign(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        String sql          = "";
        int isOk            = 0;

        String  v_userid    = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
        int     v_projseqcnt= 0;  // 셋트번호갯수

        try { 
            connMgr = new DBConnectionManager();

            sql = " SELECT count(grpseq) FROM TZ_PROJASSIGN WHERE SUBJ=" +StringManager.makeSQL(v_subj) + " AND year=" +StringManager.makeSQL(v_year) + " AND subjseq=" +StringManager.makeSQL(v_subjseq) + " and userid=" +StringManager.makeSQL(v_userid) + " ";

            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) v_projseqcnt = ls.getInt(1);
            if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_projseqcnt;
    }
     /**
    학습자 배정여부 확인(중간)
    @param box      receive from the form object and session
    @return int     배정셋트갯수
      */
     public int isReportMAssign(RequestBox box, ArrayList MList) throws Exception { 
    	 DBConnectionManager	connMgr	= null;
    	 ListSet ls          = null;
    	 String sql          = "";
    	 int isOk            = 0;
    	 
    	 String  v_userid    = box.getSession("userid");
    	 String  v_subj      = box.getString("p_subj");          // 과목
    	 String  v_year      = box.getString("p_year");          // 년도
    	 String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
    	 int     v_projseqcnt= 0;  // 셋트번호갯수
    	 int     v_grpseqcnt= 0;  // 누적갯수
    	 
    	 try { 
    		 connMgr = new DBConnectionManager();
    		 for ( int i=0; i < MList.size(); i++ ) {    			
    			 sql  = " SELECT COUNT(*) cnt 										\n";
                 sql += " FROM TZ_PROJASSIGN 										\n";
                 sql += " WHERE SUBJ = "   + StringManager.makeSQL(v_subj)     +"	\n";
                 sql += "   AND YEAR = "   + StringManager.makeSQL(v_year)     +"	\n";
                 sql += "   AND SUBJSEQ = "+ StringManager.makeSQL(v_subjseq)  +"	\n";
                 sql += "   AND USERID = " + StringManager.makeSQL(v_userid)   +"	\n";
                 sql += "   AND GRPSEQ = " + MList.get(i).toString()           +"	\n";
                 
                 ls = connMgr.executeQuery(sql);
                 if ( ls.next() ) v_projseqcnt = ls.getInt(1);
                 v_grpseqcnt += v_projseqcnt;	//해당번호의 문제지를 배정받았으면 값이 1 보다 크다. 
                 if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
    		 }
    		 
    		 
    	 } catch ( Exception ex ) { 
    		 ErrorManager.getErrorStackTrace(ex, box, sql);
    		 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	 }finally { 
    		 if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
    		 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	 }
    	 
    	 return v_grpseqcnt;
     }
     /**
    학습자 배정여부 확인(기말)
    @param box      receive from the form object and session
    @return int     배정셋트갯수
      */
     public int isReportFAssign(RequestBox box, ArrayList FList) throws Exception { 
    	 DBConnectionManager	connMgr	= null;
    	 ListSet ls          = null;
    	 ArrayList list      = null;
    	 String sql          = "";
    	 int isOk            = 0;
    	 
    	 String  v_userid    = box.getSession("userid");
    	 String  v_subj      = box.getString("p_subj");          // 과목
    	 String  v_year      = box.getString("p_year");          // 년도
    	 String  v_subjseq   = box.getString("p_subjseq");       // 과목기수
    	 int     v_projseqcnt= 0;  // 셋트번호갯수
    	 int     v_grpseqcnt = 0;  // 누적갯수

    	 try { 
    		 connMgr = new DBConnectionManager();
    		 
    		 for ( int i=0; i < FList.size();i++ ) {                 
                 sql  = " SELECT COUNT(*) cnt 										\n";
                 sql += " FROM TZ_PROJASSIGN 										\n";
                 sql += " WHERE SUBJ = "   + StringManager.makeSQL(v_subj)     +"	\n";
                 sql += "   AND YEAR = "   + StringManager.makeSQL(v_year)     +"	\n";
                 sql += "   AND SUBJSEQ = "+ StringManager.makeSQL(v_subjseq)  +"	\n";
                 sql += "   AND USERID = " + StringManager.makeSQL(v_userid)   +"	\n";
                 sql += "   AND GRPSEQ = " + FList.get(i).toString()           +"	\n";
                 
                 ls = connMgr.executeQuery(sql);
                 if ( ls.next() ) v_projseqcnt = ls.getInt(1);
                 v_grpseqcnt += v_projseqcnt;	//해당번호의 문제지를 배정받았으면 값이 1 보다 크다. 
                 if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
    		 }
    		 
    	 } catch ( Exception ex ) { 
    		 ErrorManager.getErrorStackTrace(ex, box, sql);
    		 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	 }finally { 
    		 if ( ls != null ) { try { ls.close(); } catch ( Exception e2 ) { } }
    		 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	 }
    	 
    	 return v_grpseqcnt;
     }


    /**
    과제 중간 등록 및 수정
    @param box      receive from the form object and session
    @return int
    */
     public int ReportHalfSave(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls                  = null;
        ListSet ls3                 = null;
        PreparedStatement pstmt2    = null;
        String sql1                 = "";
        String sql2                 = "";
        String sql3                 = "";
        int isOk                    = 0;
        String  v_user_id           = box.getSession("userid");
        String  v_subj              = box.getString("p_subj");          // 과목
        String  v_year              = box.getString("p_year");          // 년도
        String  v_subjseq           = box.getString("p_subjseq");       // 과목기수
        String  v_projgrp           = box.getString("p_projgrp");
        String  v_lesson            = box.getString("p_lesson");
        int     v_ordseq            = box.getInt("p_ordseq");  // 문제번호
        int     v_projseq           = box.getInt("p_projseq"); // 문제셋트번호

        String v_title              = box.getString("p_title");
        String v_contents           = box.getString("p_content");
        String v_contentsbyte       = box.getString("p_contentsbyte");  // contents byte
        String v_realFileName1      = box.getRealFileName("p_file1");
        String v_newFileName1       = box.getNewFileName("p_file1");
        String v_upfile1            = box.getString("p_upfile1");
        String v_check              = box.getString("p_check");

        if ( v_newFileName1.length() == 0) {   v_newFileName1 = v_upfile1;   }

        ConfigSet conf = new ConfigSet();
        File reportFile = new File(conf.getProperty("dir.upload.report"), v_year + File.separator + v_subj  + File.separator + v_newFileName1);

        String fileSize = (reportFile.length() ) + "";

        // 기존 파일정보
        String v_oldupfile = "";
        String v_oldrealfile = "";
        String v_oldupfilesize = "";

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            // 기존파일 정보 불러오기
            sql3 = "select  upfile,";
            sql3 += "        upfilesize,";
            sql3 += "        realfile  ";
            sql3 += "from    TZ_PROJREPHALF ";
            sql3 += "where   subj    = " + StringManager.makeSQL(v_subj)     + " and ";
            sql3 += "        year    = " + StringManager.makeSQL(v_year)     + " and ";
            sql3 += "        subjseq = " + StringManager.makeSQL(v_subjseq)  + " and ";
            sql3 += "        ordseq  = " + v_ordseq							 + " and ";
            sql3 += "        projid  = " + StringManager.makeSQL(v_projgrp)  + " ";

            ls3 = connMgr.executeQuery(sql3);
            if ( ls3.next() ) { 
                v_oldupfile   = ls3.getString("upfile");
                v_oldrealfile = ls3.getString("realfile");
                v_oldupfilesize  = ls3.getString("upfilesize");
            }

            // 업로드한 파일이 없을 경우
            if ( v_realFileName1.equals("") ) { 
                // 기존파일 삭제
                if ( v_check.equals("Y") ) { 
                    v_newFileName1   = "";
                    fileSize         = "";
                    v_realFileName1  = "";
                } else { 
                // 기존파일 유지
                    v_newFileName1   = v_oldupfile;
                    fileSize         = v_oldupfilesize;
                    v_realFileName1  = v_oldrealfile;
                }
            }

            // 중간저장 테이블에 데이타 여부
            int v_ishalf = 0;
            sql2  = " select count(*) cnt from tz_projrephalf ";
            sql2 += " where subj=" +StringManager.makeSQL(v_subj) + " and year=" +StringManager.makeSQL(v_year) + " and subjseq=" +StringManager.makeSQL(v_subjseq) + " and ordseq=" +v_ordseq + " and projid = " +StringManager.makeSQL(v_projgrp) + " ";
            //System.out.println("sql_halfReport ==  ==  == = >>  >>  >>  >>  >> " +sql2);
            ls = connMgr.executeQuery(sql2);
            if ( ls.next() ) v_ishalf = ls.getInt("cnt");
            if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
            
            //System.out.println("v_ishalf ==  ==  == = >>  >>  >>  >>  >> " +v_ishalf);

            // 기존 중간저장 데이타가 있으면
            if ( v_ishalf > 0) { 
                sql2  = "update TZ_PROJREPHALF set title=?,contents=empty_clob(),upfile=?,upfilesize=?, realfile=?, contentsbyte=?, projseq=?, luserid=?,ldate=to_char(sysdate,'YYYYMMDDHH24MISS')  ";
                sql2 += "where subj=? and year=? and subjseq=? and ordseq=? and projid=? ";
                
                //System.out.println("sql_update ==  ==  == = >>  >>  >>  >>  >> " +sql2);
                pstmt2 = connMgr.prepareStatement(sql2);
                pstmt2.setString(1,v_title);
                pstmt2.setString(2,v_newFileName1);
                pstmt2.setString(3,fileSize);
                pstmt2.setString(4,v_realFileName1);
                pstmt2.setString(5,v_contentsbyte);
                pstmt2.setInt   (6,v_projseq);
                pstmt2.setString(7,v_user_id);
                pstmt2.setString(8,v_subj);
                pstmt2.setString(9,v_year);
                pstmt2.setString(10,v_subjseq);
                pstmt2.setInt   (11,v_ordseq);
                pstmt2.setString(12,v_projgrp);

                isOk = pstmt2.executeUpdate();
                if (pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
               // System.out.println("Update_IsOk ==  ==  == = >>  >>  >>  >>  >> " +isOk);
            } else { 
                sql2  = " insert into TZ_PROJREPHALF( subj, year, subjseq, ordseq, projid, title, contents, luserid, ldate,    ";
                sql2 += "         upfile, upfilesize, realfile, contentsbyte, projseq   ) ";
                sql2 += "values( ?,?,?,?,?,?,?,?, to_char(sysdate, 'YYYYMMDDHHMMSS'), ?,?,?,?,? )";
//                sql2 += "values( ?,?,?,?,?,?,empty_clob(),?, to_char(sysdate, 'YYYYMMDDHHMMSS'), ?,?,?,?,? )";
                // System.out.println("sql2 == = >> " +sql2);
                pstmt2 = connMgr.prepareStatement(sql2);
                pstmt2.setString(1,v_subj);
                pstmt2.setString(2,v_year);
                pstmt2.setString(3,v_subjseq);
                pstmt2.setInt   (4,v_ordseq);
                pstmt2.setString(5,v_projgrp);
                pstmt2.setString(6,v_title);
                pstmt2.setString(7,v_contents);
                
                pstmt2.setString(8,v_user_id);
                pstmt2.setString(9,v_newFileName1);
                pstmt2.setString(10,fileSize);
                pstmt2.setString(11,v_realFileName1);
                pstmt2.setString(12,v_contentsbyte);
                pstmt2.setInt   (13,v_projseq);
                isOk = pstmt2.executeUpdate();
                if (pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
                //System.out.println("isOk == = >> " +isOk);
            }

            sql1  = "select contents from TZ_PROJREPHALF ";
           	sql1 += "where subj=" +StringManager.makeSQL(v_subj) + "";
           	sql1 += "  and year=" +StringManager.makeSQL(v_year) + "";
           	sql1 += "  and subjseq=" +StringManager.makeSQL(v_subjseq) + "";
           	sql1 += "  and ordseq=" +v_ordseq;
           	sql1 += "  and projid=" +StringManager.makeSQL(v_projgrp) + "";
           	
//           	connMgr.setOracleCLOB(sql1, v_contents);       // (ORACLE 9i 서버)
            
            if ( isOk > 0) { 
				connMgr.commit();
			} else { 
				connMgr.rollback();
			}
            

        } catch ( Exception ex ) { 
            if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
            throw new Exception("sql2 = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls3 != null )     { try { ls3.close(); } catch ( Exception e ) { } }
            if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    학습창 과제 리스트(과거이력)
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectReportListOld(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        String sql          = "";
        DataBox dbox   = null;

        String  v_pkey     = box.getString("p_pkey");
        String  v_userid   = box.getString("p_userid");
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = " select pkey, userid, filenm, title, submitdate, markingdate, rscore, redpen  ";
            sql += "   from cc_reportredpen                                                      ";
            sql += "  where pkey   = " + SQLString.Format(v_pkey);
            sql += "    and userid = " + SQLString.Format(v_userid);
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
    학습창 과제 리스트(과거이력)
    @param box      receive from the form object and session
    @return ArrayList
    */
     public DataBox selectReportOld(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        String sql          = "";
        DataBox dbox   = null;

        String  v_pkey     = box.getString("p_pkey");
        String  v_userid   = box.getString("p_userid");
		String  v_title    = box.getString("p_title");
        try { 
            connMgr = new DBConnectionManager();

            sql = " select pkey, userid, filenm, title, submitdate, markingdate, rscore, redpen  ";
            sql += "   from cc_reportredpen                                                     ";
            sql += "  where pkey   = " + StringManager.makeSQL(v_pkey);
            sql += "    and userid = " + StringManager.makeSQL(v_userid);
            sql += "    and title  = " + StringManager.makeSQL(v_title);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox = ls.getDataBox();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }
    
    /**
    교육완료 후 재제출 등록
    @param box      receive from the form object and session
    @return int
    */
     public int insertReportEndSubmit(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;     
        ListSet ls                  = null; 
        ListSet ls3                 = null;                  
        PreparedStatement pstmt2    = null;        
        String sql1                 = "";
        String sql2                 = "";
        String sql3                 = "";        
        int isOk                    = 0;
        String  v_user_id           = box.getString("p_userid");        
        String  v_subj              = box.getString("p_subj");          // 과목
        String  v_year              = box.getString("p_year");          // 년도
        String  v_subjseq           = box.getString("p_subjseq");       // 과목기수
        int     v_projseq           = box.getInt("p_projseq");
        int     v_ordseq            = box.getInt("p_ordseq");

        String v_title              = box.getString("p_title");
        String v_contents           = box.getString("p_contents");
        String v_contentsbyte       = box.getString("p_contentsbyte");        
        String v_realFileName1      = box.getRealFileName("p_file1");
        String v_newFileName1       = box.getNewFileName("p_file1");
        String v_upfile1            = box.getString("p_upfile1");
        String v_check              = box.getString("p_check");
                
        if ( v_newFileName1.length() == 0) {   v_newFileName1 = v_upfile1;   }
        
        ConfigSet conf = new ConfigSet();
        File reportFile = new File(conf.getProperty("dir.upload.report"), v_year + File.separator + v_subj  + File.separator + v_newFileName1);


        String fileSize = (reportFile.length() ) + "";

        // 기존 파일정보
        String v_oldupfile = "";
        String v_oldrealfile = "";
        String v_oldupfilesize = "";
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sql1  = "select seq from TZ_PROJREP ";
            sql1 += " where subj=" +StringManager.makeSQL(v_subj);
            sql1 += "   and year=" +StringManager.makeSQL(v_year);
            sql1 += "   and subjseq=" +StringManager.makeSQL(v_subjseq);
            sql1 += "   and ordseq=" +SQLString.Format(v_ordseq);
            sql1 += "   and projid=" +StringManager.makeSQL(v_user_id);
            ls = connMgr.executeQuery(sql1);
//            System.out.println("sql1 ==  ==  ==  ==  == > " +sql1);

            // 기존파일 정보 불러오기
            sql3 = "select  upfile,";
            sql3 += "        upfilesize,";
            sql3 += "        realfile  ";
            sql3 += "from    TZ_PROJREP ";
            sql3 += "where   subj    = " + StringManager.makeSQL(v_subj)    + " and ";
            sql3 += "        year    = " + StringManager.makeSQL(v_year)    + " and ";
            sql3 += "        subjseq = " + StringManager.makeSQL(v_subjseq) + " and ";
            sql3 += "        ordseq  = " + v_ordseq  + " and ";
            sql3 += "        projid  = " + StringManager.makeSQL(v_user_id)  + " ";
            ls3 = connMgr.executeQuery(sql3);
            if ( ls3.next() ) { 
                v_oldupfile     = ls3.getString("upfile");
                v_oldrealfile   = ls3.getString("realfile");
                v_oldupfilesize = ls3.getString("upfilesize");
            }
            
            // 업로드한 파일이 없을 경우
            if ( v_realFileName1.equals("") ) { 
                // 기존파일 삭제
                if ( v_check.equals("Y") ) { 
                    v_newFileName1   = "";
                    fileSize         = "";
                    v_realFileName1  = "";
                } else { 
                // 기존파일 유지
                    v_newFileName1   = v_oldupfile;
                    fileSize         = v_oldupfilesize;
                    v_realFileName1  = v_oldrealfile;
                }
            }
            
            if ( ls.next() ) { 

                sql2  = "update TZ_PROJREP set ";
                sql2 += "resend=1, ";
                sql2 += "title=?,";
                sql2 += "contents=?,";
//                sql2 += "contents=empty_clob(),";
                sql2 += "upfile=?,";
                sql2 += "upfilesize=?, ";
                sql2 += "realfile=?, ";
                sql2 += "isret='N', ";
                sql2 += "luserid=?,";
                sql2 += "ldate=to_char(sysdate,'YYYYMMDDHH24MISS'),";
                // sql2 += "indate=to_char(sysdate,'YYYYMMDDHH24MISS') ";
                sql2 += "indate=(select eduend from tz_subjseq where tz_subjseq.subj=TZ_PROJREP.subj and tz_subjseq.year=TZ_PROJREP.year and tz_subjseq.subjseq=TZ_PROJREP.subjseq)";
                sql2 += "where subj=? and year=? and subjseq=? ";
                sql2 += "and ordseq=? and projid=? ";                

                pstmt2 = connMgr.prepareStatement(sql2);
                pstmt2.setString(1,v_title);
                pstmt2.setString(2,v_contents);
                pstmt2.setString(3,v_newFileName1);
                pstmt2.setString(4,fileSize);
                pstmt2.setString(5,v_realFileName1);
                pstmt2.setString(6,v_user_id);
                pstmt2.setString(7,v_subj);
                pstmt2.setString(8,v_year);
                pstmt2.setString(9,v_subjseq);
                pstmt2.setInt(10,v_ordseq);
                pstmt2.setString(11,v_user_id);
                isOk = pstmt2.executeUpdate();  
                if (pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
            } else { 
            
                sql2  = "insert into TZ_PROJREP(subj,year,subjseq,ordseq,projid,seq,lesson,title,contents,  upfile,upfilesize,realfile,luserid,ldate, ";
                sql2 += "                       indate, contentsbyte, projseq ) ";
                sql2 += "values(?,?,?,?,?,?,?,?,?,  ?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'), ";
//                sql2 += "values(?,?,?,?,?,?,?,?,empty_clob(),  ?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'), ";
                sql2 += "       (select eduend from tz_subjseq where subj=? and year=? and subjseq=?) , ?, ? )";
                pstmt2 = connMgr.prepareStatement(sql2);

                pstmt2.setString(1,v_subj);
                pstmt2.setString(2,v_year);
                pstmt2.setString(3,v_subjseq);
                pstmt2.setInt   (4,v_ordseq);
                pstmt2.setString(5,v_user_id);
                pstmt2.setInt   (6,1);
                pstmt2.setString(7,"000");                                
                pstmt2.setString(8,v_title);
                pstmt2.setString(9,v_contents);

                pstmt2.setString(10,v_newFileName1);
                pstmt2.setString(11,fileSize);
                pstmt2.setString(12,v_realFileName1);
                pstmt2.setString(13,v_user_id);

                pstmt2.setString(14,v_subj);
                pstmt2.setString(15,v_year);
                pstmt2.setString(16,v_subjseq);                

                pstmt2.setString(17,v_contentsbyte);
                pstmt2.setInt   (18,v_projseq);                
                isOk = pstmt2.executeUpdate();      
                if (pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
            }
            
            sql2 = "select contents from TZ_PROJREP ";
            sql2 += "  where SUBJ = " +StringManager.makeSQL(v_subj) + " AND YEAR = " +StringManager.makeSQL(v_year) + " AND SUBJSEQ = " +StringManager.makeSQL(v_subjseq) + " ";
            sql2 += "  AND ORDSEQ = " +v_ordseq + " AND PROJID = " +StringManager.makeSQL(v_user_id) + "";
//			connMgr.setOracleCLOB(sql2, v_contents);	// 오라클
            
            if ( isOk > 0) { 
              connMgr.commit();
            } else { 
              connMgr.rollback();
            }
           
        } catch ( Exception ex ) {                        
            if ( connMgr != null ) { try {	connMgr.rollback(); } catch ( Exception e ) { } }
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls3 != null )      { try { ls3.close(); } catch ( Exception e ) { } }
            if ( ls != null )      { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
     
     
}