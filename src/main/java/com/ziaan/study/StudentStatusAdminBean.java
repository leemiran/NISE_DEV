// **********************************************************
//  1. 제      목: STUDENT STATUS ADMIN BEAN
//  2. 프로그램명: StudentStatusAdminBean.java
//  3. 개      요: 입과 현황 관리자 bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 김영만 2003. 9. 1
//  7. 수      정:
// **********************************************************
package com.ziaan.study;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.common.GetCodenm;
import com.ziaan.complete.FinishBean;
import com.ziaan.complete.StoldData;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormMail;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.MailSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.propose.ProposeBean;
import com.ziaan.system.ManagerAdminBean;

public class StudentStatusAdminBean { 
    private ConfigSet config;
    private int row;

    public StudentStatusAdminBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.manage.row") );  // 이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    
    /**
    학습진행자 명단 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectStudentMemberList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        DataBox             dbox            = null;
        DataBox             dbox2           = null;
        ListSet             ls              = null;
        ListSet             ls2             = null;
        ArrayList           list            = null;
        ArrayList           list1           = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        String				sql2				= "";
        StudentStatusData   data1           = null;
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_Bcourse       = ""; // 이전코스
        String              v_course        = ""; // 현재코스
        String              v_Bcourseseq    = ""; // 이전코스기수
        String              v_courseseq     = ""; // 현재코스기수
        String              v_Buserid       = "";
        String              v_guserid        = "";
        
        int                 v_pageno        = box.getInt("p_pageno");
        int                 l               = 0;
        String              ss_grcode       = box.getStringDefault("s_grcode"       , "ALL");   // 교육그룹
        String              ss_gyear        = box.getStringDefault("s_gyear"        , "ALL");   // 년도
        String              ss_grseq        = box.getStringDefault("s_grseq"        , "ALL");   // 교육기수

        String              ss_uclass       = box.getStringDefault("s_upperclass"   , "ALL");   // 과목분류
        String              ss_mclass       = box.getStringDefault("s_middleclass"  , "ALL");   // 과목분류
        String              ss_lclass       = box.getStringDefault("s_lowerclass"   , "ALL");   // 과목분류

        String              ss_subjcourse   = box.getStringDefault("s_subjcourse"   , "ALL");   // 과목&코스

        String              ss_subjseq      = box.getStringDefault("s_subjseq"      , "ALL");   // 과목 기수
        String              ss_company      = box.getStringDefault("s_company"      , "ALL");   // 회사
        String              ss_usebook      = box.getStringDefault("s_usebook"      , "ALL");   // 교재

        String              ss_action       = box.getString        ("s_action"             );
        String              v_orderColumn   = box.getString        ("p_orderColumn"        );   // 정렬할 컬럼명
        String              v_orderType     = box.getString        ("p_orderType"          );   // 정렬할 순서

        String              ss_edustart     = "ALL";
        if(!"".equals(box.getString("p_syear"))) {
        	ss_edustart = 	box.getString("p_syear") + box.getString("p_smon") + box.getString("p_sday");
        }
        
        String              ss_eduend     = "ALL";
        if(!"".equals(box.getString("p_eyear"))) {
        	ss_eduend = 	box.getString("p_eyear") + box.getString("p_emon") + box.getString("p_eday");
        }
        
        ManagerAdminBean    bean            = null;
        ProposeBean         probean         = new ProposeBean();
        Hashtable           outdata         = new Hashtable();
        String              v_sql_add       = "";
        String              v_userid        = box.getSession("userid");
        String              s_gadmin        = box.getSession("gadmin");
        String  			v_searchtxt 	= box.getStringDefault("p_searchtext1", "ZZZ");    // id or name 검색

        try { 
        	connMgr = new DBConnectionManager();
        	list    = new ArrayList();
        	list1    = new ArrayList();        	

/*
                sql1 += " select  C.grseq, C.course, C.cyear, C.courseseq, C.coursenm, C.subj, C.year, C.subjnm, C.subjseq, C.subjseqgr,         ";
                sql1 += "         get_compnm(B.comp,2,2) companynm, get_deptnm(B.deptnam,'') compnm,                                               ";
                sql1 += "         get_jikwinm(B.jikwi, B.comp) jikwinm,  get_jikupnm(B.jikup, B.comp, B.jikupnm) jikupnm,                        ";
                sql1 += "         B.userid, B.cono, B.name, A.tstep, A.avmtest mtest, A.avftest ftest, A.score, B.email, B.ismailing, C.isonoff, ";
                sql1 += "         (select grseqnm from tz_grseq where grcode=c.grcode and gyear=c.gyear and grseq = c.grseq) grseqnm             ";
                sql1 += " from TZ_STUDENT A, TZ_MEMBER B, VZ_SCSUBJSEQ C                                                                         ";
                sql1 += " where A.userid=B.userid                                                                                                ";
                sql1 += "   and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq                                                          ";
                // sql1 += "   and C.eduend > to_char(sysdate,'YYYYMMDDHH24')";
*/
                
        	sbSQL.append(" SELECT  a.examnum                                            \n")
        		 .append("     ,   C.grseq                                            \n")
	             .append("     ,   C.course                                            \n")
	             .append("     ,   C.cyear                                             \n")
	             .append("     ,   C.courseseq                                         \n")
	             .append("     ,   C.coursenm                                          \n")
	             .append("     ,   C.subj                                              \n")
	             .append("     ,   C.year                                              \n")
	             .append("     ,   C.subjnm                                            \n")
	             .append("     ,   C.subjseq                                           \n")
	             .append("     ,   C.subjseqgr                                         \n")
	             .append("     ,   C.isonoff                                           \n")
	             .append("     ,   get_codenm('0004',C.isonoff) isonoffvalue           \n")
	             .append("     ,   A.tstep                                             \n");
                
            if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // 가중치적용
            	sbSQL.append("     ,   A.avmtest                             mtest       \n")
	                 .append("     ,   A.avftest                             ftest       \n")
	                 .append("     ,   A.avhtest                             htest       \n")
	                 .append("     ,   A.avreport                            report      \n")
	                 .append("     ,   A.avetc1                              etc1        \n")
	                 .append("     ,   A.avetc2                              etc2        \n")
	                 .append("     ,   A.avact                               act         \n");
            } else {
            	sbSQL.append("     ,   A.mtest                               mtest       \n")
                	 .append("     ,   A.ftest                               ftest       \n")
                	 .append("     ,   A.htest                               htest       \n")
                	 .append("     ,   A.report                              report      \n")
                	 .append("     ,   A.etc1                                etc1        \n")
                	 .append("     ,   A.etc2                                etc2        \n")
                	 .append("     ,   A.act                                 act         \n");
            }
            sbSQL.append("     ,   A.study_count                                       	\n")
	             .append("     ,   A.study_time                                        	\n")
	             .append("     ,   A.score                                             	\n")
	             .append("     ,   B.userid                                            	\n")
	             .append("     ,   B.name                                              	\n")
	             .append("     ,   B.email                                             	\n")
	             .append("     ,   B.handphone                                         	\n")
	             .append("     ,   B.hometel                                           	\n")
	             .append("     ,   B.indate                                            	\n")
	             .append("     ,   fn_crypt('2', b.birth_date, 'knise') birth_date                                             	\n")
	             .append("     ,   B.cert                                             	\n")
	             .append("	   , (													   	\n") 
	             .append(" 		SELECT  grcodenm									   	\n")
	             .append(" 		FROM    tz_grcode									   	\n")
	             .append(" 		WHERE   grcode = c.grcode							   	\n")
	             .append("    ) grcodenm    										   	\n")
	             .append("     ,   (                                                   	\n")  
	             .append("             SELECT  grseqnm                                 	\n")
	             .append("             FROM    tz_grseq                                	\n")
	             .append("             WHERE   grcode = c.grcode                       	\n")
	             .append("             AND     gyear  = c.gyear                        	\n")
	             .append("             AND     grseq  = c.grseq                        	\n")
	             .append("         )                                       grseqnm     	\n")
	             .append("     ,   get_compnm(B.comp) as companynm                		\n")
	             .append("		, get_deptnm(b.dept_cd) as deptnm		   				\n")
	             .append("		, b.position_nm		   									\n")
	             .append("		, b.lvl_nm			   									\n")	             
	             .append("      , c.biyong                                             	\n")
	             .append("      , d.goyongpricemajor								   	\n")
		         .append("      , d.goyongpriceminor								   	\n")
		         .append("      , nvl(( 			                                   	\n")
	             .append("                 SELECT  compnm                           	\n")
	             .append("                 FROM    tz_compclass                         \n")
	             .append("                 WHERE   comp = e.producer                   	\n")
	             .append("                ), (                                         	\n")
	             .append("                 SELECT  cpnm                                	\n")
	             .append("                 FROM    tz_cpinfo                           	\n")
	             .append("                 WHERE   cpseq = e.producer                  	\n")
	             .append("                ))           producernm                     	\n")
	             .append("	    ,   nvl((										   		\n")
	       	     .append("                  SELECT cpnm								   	\n")
	       	     .append("                  FROM   tz_cpinfo						   	\n")
	       	     .append("                  WHERE  cpseq = c.owner					   	\n")
	       	     .append("                  ), (									   	\n")
	   	    	 .append("                      SELECT compnm					   		\n")
	   	    	 .append("                      FROM   tz_compclass						\n")
	   	    	 .append("                      WHERE  comp = c.owner				   	\n")
	   	    	 .append("                      ))         ownernm					   	\n")
	   	    	 .append("      , e.isoutsourcing                                       \n")
	   	    	 .append("      , c.usebook                                            	\n")
	   	    	 .append("      , c.bookname                                           	\n")
	   	    	 //.append("      , F.DELIVERY_ADDRESS1								   	\n")
	   	    	 //.append("      , F.DELIVERY_ADDRESS2									\n")
	   	    	 //.append("      , F.DELIVERY_HANDPHONE								\n")
	   	    	 //.append("      , F.DELIVERY_POST1									\n")
	   	    	 //.append("      , F.DELIVERY_POST2      								\n")
	   	    	 .append("      , b.handphone                                          	\n")
	   	    	 .append("      , b.address                                           	\n")
	   	    	 .append("      , b.zip_cd                                           	\n")
	   	    	 .append("      , b.user_path                                          	\n")
	   	    	 .append("      , b.address1                                           	\n")
	   	    	 .append("      , b.zip_cd1                                           	\n")
	   	    	 .append("      , b.hrdc	                                           	\n")
                 .append("      , (select SCHOOL_NM from TZ_ATTEND_CD where seq = (select to_number(nvl(trim(is_attend),0)) from tz_propose pp where pp.subj=a.subj and pp.year=a.year and pp.subjseq = a.subjseq and pp.userid=a.userid )) as gosa       	    	\n")
                 .append("      , (SELECT lec_sel_no FROM tz_propose p WHERE a.subj = p.subj AND a.YEAR = p.YEAR  AND a.subjseq = p.subjseq  AND a.userid = p.userid) lec_sel_no   	    	\n")
                 .append("		, (select decode(trim(type), 'OB','교육청일괄납부','PB','무통장','SC0010','신용카드','SC0030','계좌이체','SC0040','가상계좌',type) from pa_payment pa where pa.order_id = f.order_id and pa.useyn='Y') as pay                         	\n")
	   	    	 .append("      , (select decode(enterance_dt,null,'',to_char(to_date(enterance_dt,'yyyy-mm-dd'),'yyyy-mm-dd')) from pa_payment pa where pa.order_id = f.order_id and pa.useyn='Y') as enterance_dt                       	\n")
	   	    	 .append(" 		, (select decode(enter_dt,null,'',to_char(to_date(enter_dt,'yyyy-mm-dd'),'yyyy-mm-dd')) from pa_payment pa where pa.order_id = f.order_id and pa.useyn='Y') as enter_dt                         	\n")
                 .append(" FROM    TZ_STUDENT                A                         	\n")
                 .append("    ,    TZ_MEMBER                 B                         	\n")
                 .append("    ,    VZ_SCSUBJSEQ              C                         	\n")
                 .append("    ,    TZ_SUBJSEQ		         D                         	\n")
                 .append("    ,    TZ_SUBJ                   E                         	\n")
                 .append("    ,    tz_propose                F                         	\n")
                 //.append("    ,    TZ_DELIVERY               F                        \n")
                 .append(" WHERE   A.userid  = B.userid                                	\n")
                 .append(" AND     A.subj    = C.subj                                  	\n")
                 .append(" AND     A.year    = C.year                                  	\n")
                 .append(" AND     A.subjseq = C.subjseq                               	\n")
                 .append(" AND     C.SUBJ = D.SUBJ		                               	\n")
                 .append(" and     c.year = d.year                                     	\n")
                 .append(" and     c.subjseq = d.subjseq                               	\n")
                 .append(" and    a.userid = f.userid                           	   	\n")
                 .append(" and    a.subj = f.subj                              			\n")
                 .append(" and    a.year = f.year                           	    	\n")
                 .append(" and    a.subjseq = f.subjseq                                	\n")
                 .append(" and     d.subj = e.subj                                      \n");
            	//.append(" AND     B.USERID = F.USERID(+)								\n")
           	 	//.append(" AND     D.SUBJ = F.SUBJ(+)									\n")
           	 	//.append(" AND     D.YEAR = F.YEAR(+)									\n")
           	 	//.append(" AND     D.SUBJSEQ = F.SUBJSEQ(+)     						\n");
                           
            	//sbSQL.append(" AND      A.isgraduated = 'N'                           \n"); 

                if ( !ss_grcode.equals      ("ALL") )   sbSQL.append(" and C.grcode             = " + SQLString.Format(ss_grcode        ) + " \n");
                if ( !ss_gyear.equals       ("ALL") )   sbSQL.append(" and C.gyear              = " + SQLString.Format(ss_gyear         ) + " \n");
                if ( !ss_grseq.equals       ("ALL") )   sbSQL.append(" and C.grseq              = " + SQLString.Format(ss_grseq         ) + " \n");
                if ( !ss_uclass.equals      ("ALL") )   sbSQL.append(" and C.scupperclass       = " + SQLString.Format(ss_uclass        ) + " \n");
                if ( !ss_mclass.equals      ("ALL") )   sbSQL.append(" and C.scmiddleclass      = " + SQLString.Format(ss_mclass        ) + " \n");
                if ( !ss_lclass.equals      ("ALL") )   sbSQL.append(" and C.sclowerclass       = " + SQLString.Format(ss_lclass        ) + " \n");
                if ( !ss_subjcourse.equals  ("ALL") )   sbSQL.append(" and C.scsubj             = " + SQLString.Format(ss_subjcourse    ) + " \n");
                if ( !ss_subjseq.equals     ("ALL") )   sbSQL.append(" and C.scsubjseq          = " + SQLString.Format(ss_subjseq       ) + " \n");
                if ( !ss_company.equals     ("ALL") )   sbSQL.append(" and B.comp               = " + SQLString.Format(ss_company       ) + " \n");
                if ( !ss_usebook.equals     ("ALL") )   sbSQL.append(" and C.usebook            = " + SQLString.Format(ss_usebook       ) + " \n");
                
                if ( !ss_edustart.equals    ("ALL") )   sbSQL.append(" and substr(C.edustart, 1, 8)           >= " +SQLString.Format(ss_edustart      ) + "                               \n");
                if ( !ss_eduend.equals      ("ALL") )   sbSQL.append(" and substr(C.eduend, 1, 8)             <= " +SQLString.Format(ss_eduend        ) + "                               \n");

                // 교육시작일과 종료일이 선택되지 않은 경우에만 교육년도별도 검색함
                //if ( ss_edustart.equals("ALL") && ss_eduend.equals("ALL") ) { 
                //    sbSQL.append(" and C.gyear = " + SQLString.Format(ss_gyear) + "         \n");
                //}

                // 부서장일경우
                if ( s_gadmin.equals("K7") ) { 
                    bean        = new ManagerAdminBean();
                    v_sql_add   = bean.getManagerDept(v_userid, s_gadmin);
                    
                    if ( !v_sql_add.equals("") )
                        sbSQL.append(" and B.comp in " + v_sql_add + " \n");   // 관리부서검색조건쿼리
                }

                if ( v_orderColumn.equals("grseq"    ) )    v_orderColumn = " C.grseq       ";
                if ( v_orderColumn.equals("subj"     ) )    v_orderColumn = " C.subj        ";
                if ( v_orderColumn.equals("hometel"  ) )    v_orderColumn = " b.homtel      ";
                if ( v_orderColumn.equals("handphone") )    v_orderColumn = " b.handphone   ";
                if ( v_orderColumn.equals("userid"   ) )    v_orderColumn = " b.userid      ";
                if ( v_orderColumn.equals("name"     ) )    v_orderColumn = " b.name        ";

                //if ( v_orderColumn.equals("") ) { 
                //    sbSQL.append(" order by C.subj, C.year, C.subjseq           \n");
                //} else { 
                //    sbSQL.append(" order by " + v_orderColumn + v_orderType + " \n");
                //}

                if ( !v_searchtxt.equals("ZZZ") ) { 
                	sbSQL.append("   and (b.userid like  '%" +v_searchtxt + "%' or b.name like '%" +v_searchtxt+"%') \n");
	            } 
                
                if ( v_orderColumn.equals("") ) { 
                    sbSQL.append(" order by b.name, a.examnum, fn_crypt('2', b.birth_date, 'knise')  , b.userid, C.subj, C.year, C.subjseq           \n");
                } else { 
                    sbSQL.append(" order by b.name, a.examnum, fn_crypt('2', b.birth_date, 'knise')  , b.userid, " + v_orderColumn + v_orderType + " \n");
                }                
                //System.out.println(this.getClass().getName() + "." + "selectStudentMemberList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
//System.out.println("========================입과명단=====================\n" + sbSQL.toString());
                ls  = connMgr.executeQuery(sbSQL.toString());

                ls.setPageSize     ( row       );              // 페이지당 row 갯수를 세팅한다
                ls.setCurrentPage  ( v_pageno  );              // 현재페이지번호를 세팅한다.
                int total_page_count    = ls.getTotalPage  ();  // 전체 페이지 수를 반환한다
                int total_row_count     = ls.getTotalCount ();  // 전체 row 수를 반환한다

                while ( ls.next() ) { 
                    dbox    = ls.getDataBox();
                    
/*
                    outdata.clear();
                    outdata = probean.getMeberInfo( ls.getString("userid") );
                    

                    dbox.put("d_workplc",(String)outdata.get("work_plcnm") );
                    dbox.put("d_deptnam",(String)outdata.get("deptnam") );
                    dbox.put("d_officegbn",(String)outdata.get("officegbn") );
                    dbox.put("d_gubuntxt",(String)outdata.get("gubuntxt") );
*/
                    list.add(dbox);
                }
                
                for(int i=0;i < list.size(); i++){
                    dbox2       =   (DataBox)list.get(i);
                    v_course    =   dbox2.getString("d_course");
                    v_courseseq =   dbox2.getString("d_courseseq");
                    v_guserid   =   dbox2.getString("d_userid");
                    if(!v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq) && v_Buserid.equals(v_guserid))){
                        sql2 = "select count(*) cnt from VZ_SCSUBJSEQ A,TZ_STUDENT B,TZ_MEMBER C ";                        
                        sql2+= "where A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq ";
                        sql2+= "and B.userid=C.userid  ";

            			if (!ss_grcode.equals("ALL")) {
            				sql2+= " and A.grcode = "+SQLString.Format(ss_grcode);
            			}
            			if (!ss_grseq.equals("ALL")) {
            				sql2+= " and A.grseq = "+SQLString.Format(ss_grseq);
            			}
            			if (!ss_uclass.equals("ALL")) {
            			    sql2+= " and A.scupperclass = "+SQLString.Format(ss_uclass);
            			}
            			if (!ss_mclass.equals("ALL")) {
            			    sql2+= " and A.scmiddleclass = "+SQLString.Format(ss_mclass);
            			}
            			if (!ss_subjcourse.equals("ALL")) {
            				sql2+= " and A.scsubj = "+SQLString.Format(ss_subjcourse);
            			}
            			if (!ss_subjseq.equals("ALL")) {
            				sql2+= " and A.scsubjseq = "+SQLString.Format(ss_subjseq);
            			}            
                        if (!ss_company.equals("ALL")) {
                            sql2+= " and C.comp like '"+GetCodenm.get_compval(ss_company)+"'";
                        }
            			if (!ss_edustart.equals("ALL")){
            				sql2+= " and A.edustart >= "+SQLString.Format(ss_edustart);
            			}            
                        if (!ss_eduend.equals("ALL")) {
                            sql2+= " and A.edustart <= "+SQLString.Format(ss_eduend);
                        }    			
                        //교육시작일과 종료일이 선택되지 않은 경우에만 교육년도별도 검색함
            			if (ss_edustart.equals("ALL") && ss_eduend.equals("ALL")) { 
            				sql2+= " and A.gyear = "+SQLString.Format(ss_gyear);
            			}                        
                        sql2+= "and A.course = "+SQLString.Format(v_course)+" and A.courseseq = "+SQLString.Format(v_courseseq)
                            + " and b.userid = "+SQLString.Format(v_guserid);

						if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
                        ls2 = connMgr.executeQuery(sql2);	
                        if(ls2.next()){
                            dbox2.put("d_rowspan", new Integer(ls2.getInt("cnt")));
                            dbox2.put("d_isnewcourse", "Y");                            
                        }
                    }else{
                    	dbox2.put("d_rowspan", new Integer(0));
                        dbox2.put("d_isnewcourse", "N");
                    }
                    v_Bcourse   =   v_course;
                    v_Bcourseseq=   v_courseseq;
                    v_Buserid   =   v_guserid;
                    list1.add(dbox2);
                    if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
                }   
                
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( ls2 != null ) { 
                try { 
                    ls2.close();  
                } catch ( Exception e ) { } 
            }            
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection();  
                } catch ( Exception e ) { } 
            }
        }

        return list;
    }

     
    /**
    학습완료자 명단 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectCompleteMemberList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox        = null;
        DataBox dbox2        = null;
        ListSet ls1         = null;
        ListSet ls2         = null;
        ArrayList list1     = null;
        ArrayList list2     = null;
        String sql1         = "";
        String sql2         = "";
        StudentStatusData data1= null;
        StudentStatusData data2= null;
        String  v_Bcourse   = ""; // 이전코스
        String  v_course    = ""; // 현재코스
        String  v_Bcourseseq= ""; // 이전코스기수
        String  v_courseseq = ""; // 현재코스기수
        String v_Buserid = "";          
        String v_guserid = "";
      
        int v_pageno = box.getInt("p_pageno");
        int     l           = 0;
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수

        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");    // 과목분류
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류

        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_company  = box.getStringDefault("s_company","ALL");   // 회사
        //String  ss_edustart = box.getStringDefault("s_edustart","ALL");  // 교육시작일
        //String  ss_eduend   = box.getStringDefault("s_eduend","ALL");    // 교육종료일
        //String  ss_selgubun = box.getString("s_selgubun");               // 직군별:JIKUN,직급별:JIKUP,사업부별:GPM
        //String  ss_seltext  = box.getStringDefault("s_seltext","ALL");   // 검색분류별 검색내용
        //String  ss_seldept  = box.getStringDefault("s_seldept","ALL");   // 사업부별 부서 검색내용
        String  ss_action   = box.getString("s_action");
        String  v_orderColumn= box.getString("p_orderColumn");           // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");          // 정렬할 순서
        String  v_searchtxt = box.getStringDefault("p_searchtext1", "ZZZ");    // id or name 검색

        ManagerAdminBean bean = null;
        ProposeBean probean = new ProposeBean();
        Hashtable outdata = new Hashtable();

        String  v_sql_add   = "";
        String  v_userid    = box.getSession("userid");
        String  s_gadmin    = box.getSession("gadmin");

        String              ss_edustart     = "ALL";
        if(!"".equals(box.getString("p_syear"))) {
        	ss_edustart = 	box.getString("p_syear") + box.getString("p_smon") + box.getString("p_sday");
        }
        
        String              ss_eduend     = "ALL";
        if(!"".equals(box.getString("p_eyear"))) {
        	ss_eduend = 	box.getString("p_eyear") + box.getString("p_emon") + box.getString("p_eday");
        }

        try { 
            //if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                list2 = new ArrayList();

/* 2006.06.19 - 수정                
                // select grseq,course,cyear,courseseq,coursenm,subj,year,subjnm,subjseq,compnm,
                // jikwinm,userid,cono,name,isGraduated,avmtest,avftest,mtest,ftest,score,email,ismailing,isonoff
                sql1 = "select C.grseq,C.course,C.cyear,C.courseseq,C.coursenm,C.subj,C.year,C.subjnm,C.subjseq,C.subjseqgr,";
                sql1 += "       get_compnm(B.comp,2,2) companynm, get_deptnm(B.deptnam,'') compnm,";
                sql1 += "       get_jikwinm(B.jikwi,B.comp) jikwinm, get_jikupnm(B.jikup, B.comp, B.jikupnm) jikupnm, ";
                sql1 += "        B.userid,B.cono,B.name,A.isGraduated, A.tstep, ";
                if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // 가중치적용
                    sql1 += "A.avmtest mtest,A.avftest ftest, ";
                } else {                                                  // 가중치비적용
                    sql1 += "A.mtest mtest,A.ftest ftest, ";
                }
                sql1 += "        A.score,B.email,B.ismailing,C.isonoff, ";
                sql1 += "        (select grseqnm from tz_grseq where grcode=c.grcode and gyear=c.gyear and grseq = c.grseq) grseqnm ";
                // sql1 += "from TZ_STUDENT A,TZ_MEMBER B,VZ_SCSUBJSEQ C where // C.eduend<to_char(sysdate,'YYYYMMDDHH24') ";
                sql1 += "   from TZ_STUDENT A,TZ_MEMBER B,VZ_SCSUBJSEQ C ";
                sql1 += "  where  c.isclosed = 'Y' ";
                sql1 += " and A.userid=B.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq ";
*/
                
                
                sql1  = "SELECT                                                                \n" +
                        "    C.grseq,                                                          \n" +
                        "    C.course,                                                         \n" +
                        "    C.cyear,                                                          \n" +
                        "    C.courseseq,                                                      \n" +
                        "    C.coursenm,                                                       \n" +
                        "    C.subj,                                                           \n" +
                        "    C.year,                                                           \n" +
                        "    C.subjnm,                                                         \n" +
                        "    C.subjseq,                                                        \n" +
                        "    C.subjseqgr,                                                      \n" +
                        "    C.isonoff,                                                        \n" +
                        "    get_codenm('0004',C.isonoff) isonoffvalue,                        \n" +                        
                        "    B.email,                                                          \n" +
                        //"    B.ismailing,                                                      \n" +
                        "    B.userid,                                                         \n" +
                        "    B.name,                                                           \n" +
                        "    B.indate,                                                         \n" ;

                if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // 가중치적용
                    sql1+= "\n       a.avmtest mtest, a.avftest ftest, a.avhtest htest, "
                    	+  "\n       a.avreport report, "
                    	+  "\n       a.avetc1 etc1, a.avetc2 etc2, "
                    	+  "\n       a.avact act, ";
                } else {                                                  // 가중치비적용
                    sql1+= "\n       a.mtest, a.ftest, a.htest, "
                    	+  "\n       a.report, "
                    	+  "\n       a.etc1, a.etc2, "
                    	+  "\n       a.act, ";
                }

                sql1 += "    A.score,                                                          \n" +
                        "    A.isGraduated,                                                    \n" +
                        "    A.tstep,                                                          \n" +
                        "    A.study_count ,               									   \n" +
                        "    A.study_time  ,               									   \n" +
                        "    (																   \n" + 
                        " 		SELECT  grcodenm											   \n" +
                        " 		FROM    tz_grcode											   \n" +
                        " 		WHERE   grcode = c.grcode									   \n" +                       
                        "    ) grcodenm,    												   \n" +                        
                        "    (SELECT                                                           \n" +
                        "         grseqnm                                                      \n" +
                        "     FROM                                                             \n" +
                        "         tz_grseq                                                     \n" +
                        "     WHERE                                                            \n" +
                        "             grcode = c.grcode                                        \n" +
                        "         AND gyear  = c.gyear                                         \n" +
                        "         AND grseq  = c.grseq                                         \n" +
                        "    )                                   grseqnm                       \n" +
                        "    , get_compnm(B.comp) as companynm                            	   \n" +
                        "    , get_deptnm(b.dept_cd) as deptnm					  			   \n" +
                        "    , position_nm					   								   \n" +
                        "    , lvl_nm					   									   \n" +                        
                        "        , d.biyong												   	   \n" +
     	        	    "        , d.isgoyong												   \n" +
     	        	    "        , d.goyongpricemajor										   \n" +
     	        	    "        , d.goyongpriceminor										   \n" +
     	        	    "        , nvl((  		                                   		       \n" +
  		                "                 SELECT  compnm                           			   \n" +
	  		             "                 FROM    tz_compclass                             	\n" +
	  		             "                 WHERE   comp = e.producer                   			\n" +
	  		             "                ), (                                         			\n" +
	  		             "                 SELECT  cpnm                                			\n" +
	  		             "                 FROM    tz_cpinfo                           			\n" +
	  		             "                 WHERE   cpseq = e.producer                  			\n" +
	  		             "                ))           producernm                     			\n" +
     	        	    "	        ,   nvl((											   		\n" +
    	        	    "                  SELECT cpnm										   \n" +
    	        	    "                  FROM   tz_cpinfo									   \n" +
    	        	    "                  WHERE  cpseq = c.owner							   \n" +
    	        	    "                  ), (												   \n" +
    	        	    "                      SELECT compnm								   \n" +
    	        	    "                      FROM   tz_compclass							   \n" +
    	        	    "                      WHERE  comp = c.owner						   \n" +
    	        	    "                      ))         ownernm							   \n" +
    	        	    "    , e.isoutsourcing                                                 \n" +
    	        	    "    , c.usebook                                                       \n" + 
    	        	    "    , c.bookname                                                      \n" +
    	        		//"      , F.DELIVERY_ADDRESS1								   			\n" +
            	    	//"      , F.DELIVERY_ADDRESS2											\n" +
            	    	//"      , F.DELIVERY_HANDPHONE											\n" +
            	    	//"      , F.DELIVERY_POST1												\n" +
            	    	//"      , F.DELIVERY_POST2      										\n" +
            	    	"      , fn_crypt('2', b.birth_date, 'knise') birth_date                                                       \n" +
            	    	"      , b.user_path                                                      \n" +
            	    	"      , b.address                                           	\n"+
       	   	    	    "      , b.zip_cd                                           	\n"+
       	   	    	    "      , b.user_path                                          	\n"+
       	   	    	    "      , b.address1                                           	\n"+
       	   	    	    "      , b.zip_cd1                                           	\n"+
       	   	    	    "      , b.hrdc	                                           	\n" +
            	    	 "      , (select (select school_nm from TZ_ATTEND_CD  where ISUSE = 'Y' and to_char(seq) = nvl(pp.is_attend,0)) from tz_propose pp where subj=a.subj and year=a.year and subjseq = a.subjseq and userid=a.userid )as gosa \n"+
                        "FROM                                                                  \n" +
                        "    TZ_STUDENT                          A,                            \n" +
                        "    TZ_MEMBER                           B,                            \n" +
                        "    VZ_SCSUBJSEQ                        C,                            \n" +
                        "    TZ_SUBJSEQ                          D,                            \n" +
                        "    TZ_SUBJ                             E                             \n" +
                        //"    ,    TZ_DELIVERY               F                         	   \n" +
                        "WHERE                                                                 \n" +
                        "        c.isclosed = 'Y'                                              \n" +
                        "    AND A.userid   = B.userid                                         \n" +
                        "    AND A.subj     = C.subj                                           \n" +
                        "    AND A.year     = C.year                                           \n" +
                        "    AND A.subjseq  = C.subjseq                                        \n" +
                        "    AND C.SUBJ = D.SUBJ                                               \n" +
                		"    AND C.YEAR = D.YEAR                                               \n" +
                		"    AND C.SUBJSEQ = D.SUBJSEQ                                         \n" +
                		"    AND D.SUBJ = E.SUBJ                                               \n" ;
                        //" 	 AND     B.USERID *= F.USERID									   \n" +
                        //"    AND     D.SUBJ *= F.SUBJ										   \n" +
           	            //" AND     D.YEAR *= F.YEAR												\n" +
           	             //" AND     D.SUBJSEQ *= F.SUBJSEQ     									\n";
                
                if ( !ss_grcode.equals("ALL"))     sql1 += " and C.grcode = " +SQLString.Format(ss_grcode);
                if ( !ss_gyear.equals("ALL"))      sql1 += " and C.gyear = " +SQLString.Format(ss_gyear);
                if ( !ss_grseq.equals("ALL"))      sql1 += " and C.grseq = " +SQLString.Format(ss_grseq);
                if ( !ss_uclass.equals("ALL"))     sql1 += " and C.scupperclass = " +SQLString.Format(ss_uclass);
                if ( !ss_mclass.equals("ALL"))     sql1 += " and C.scmiddleclass = " +SQLString.Format(ss_mclass);
                if ( !ss_lclass.equals("ALL"))     sql1 += " and C.sclowerclass = " +SQLString.Format(ss_lclass);
                if ( !ss_subjcourse.equals("ALL")) sql1 += " and C.scsubj = " +SQLString.Format(ss_subjcourse);
                if ( !ss_subjseq.equals("ALL"))    sql1 += " and C.scsubjseq = " +SQLString.Format(ss_subjseq);
                if ( !ss_company.equals("ALL"))    sql1 += " and B.comp = '" + ss_company + "'";
                if ( !ss_edustart.equals("ALL"))   sql1 += " and substr(C.edustart,1,8) >= " +SQLString.Format(ss_edustart);
                if ( !ss_eduend.equals("ALL"))     sql1 += " and substr(C.eduend,1,8) <= " +SQLString.Format(ss_eduend);
                
                if ( !v_searchtxt.equals("ZZZ") ) { 
	            	sql1 += "   and (b.userid like  '%" +v_searchtxt + "%' or b.name like '%" +v_searchtxt+"%') \n";
	            }

                // 교육시작일과 종료일이 선택되지 않은 경우에만 교육년도별도 검색함
                //if ( ss_edustart.equals("ALL") && ss_eduend.equals("ALL") ) { 
                //    sql1 += " and C.gyear = " +SQLString.Format(ss_gyear);
                //}

                if ( v_orderColumn.equals("grseq")   )    v_orderColumn = "C.grseq";
                if ( v_orderColumn.equals("subj")    )    v_orderColumn = "C.subj";
                if ( v_orderColumn.equals("userid")  )    v_orderColumn = "b.userid ";
                if ( v_orderColumn.equals("name")    )    v_orderColumn = "b.name ";

                if ( v_orderColumn.equals("") ) { 
                    sql1 += " order by C.subj,C.year,C.subjseq";
                } else { 
                    sql1 += " order by " + v_orderColumn + v_orderType;
                }

                 //System.out.println("sql1 ==  ==  ==완료자명단  ==  ==  == > " +sql1);
                ls1 = connMgr.executeQuery(sql1);

                ls1.setPageSize(row);                       // 페이지당 row 갯수를 세팅한다
                ls1.setCurrentPage(v_pageno);               // 현재페이지번호를 세팅한다.
                int total_page_count = ls1.getTotalPage();  // 전체 페이지 수를 반환한다
                int total_row_count = ls1.getTotalCount();  // 전체 row 수를 반환한다

                while ( ls1.next() ) { 
                    dbox = ls1.getDataBox();
                    
                    list1.add(dbox);
                }
            //}
                
                for(int i=0;i < list1.size(); i++){
                    dbox2       =   (DataBox)list1.get(i);
                    v_course    =   dbox2.getString("d_course");
                    v_courseseq =   dbox2.getString("d_courseseq");
                    v_guserid    =   dbox2.getString("d_userid");
                    if(!v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq) && v_Buserid.equals(v_guserid))){
                        sql2 = "select count(*) cnt from VZ_SCSUBJSEQ A,TZ_STUDENT B,TZ_MEMBER C ";                        
                        sql2+= "where A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq ";
                        sql2+= "and B.userid=C.userid  ";

            			if (!ss_grcode.equals("ALL")) {
            				sql2+= " and A.grcode = "+SQLString.Format(ss_grcode);
            			}
            			if (!ss_grseq.equals("ALL")) {
            				sql2+= " and A.grseq = "+SQLString.Format(ss_grseq);
            			}
            			if (!ss_uclass.equals("ALL")) {
            			    sql2+= " and A.scupperclass = "+SQLString.Format(ss_uclass);
            			}
            			if (!ss_mclass.equals("ALL")) {
            			    sql2+= " and A.scmiddleclass = "+SQLString.Format(ss_mclass);
            			}
            			if (!ss_subjcourse.equals("ALL")) {
            				sql2+= " and A.scsubj = "+SQLString.Format(ss_subjcourse);
            			}
            			if (!ss_subjseq.equals("ALL")) {
            				sql2+= " and A.scsubjseq = "+SQLString.Format(ss_subjseq);
            			}            
                        if (!ss_company.equals("ALL")) {
                            sql2+= " and C.comp like '"+GetCodenm.get_compval(ss_company)+"'";
                        }
            			if (!ss_edustart.equals("ALL")){
            				sql2+= " and A.edustart >= "+SQLString.Format(ss_edustart);
            			}            
                        if (!ss_eduend.equals("ALL")) {
                            sql2+= " and A.edustart <= "+SQLString.Format(ss_eduend);
                        }    			
                        //교육시작일과 종료일이 선택되지 않은 경우에만 교육년도별도 검색함
            			if (ss_edustart.equals("ALL") && ss_eduend.equals("ALL")) { 
            				sql2+= " and A.gyear = "+SQLString.Format(ss_gyear);
            			}                        
                        sql2+= "and A.course = "+SQLString.Format(v_course)+" and A.courseseq = "+SQLString.Format(v_courseseq)
                            + " and b.userid = "+SQLString.Format(v_guserid);

						if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
                        ls2 = connMgr.executeQuery(sql2);	
                        if(ls2.next()){
                            dbox2.put("d_rowspan", new Integer(ls2.getInt("cnt")));
                            dbox2.put("d_isnewcourse", "Y");                            
                        }
                    }else{
                    	dbox2.put("d_rowspan", new Integer(0));
                        dbox2.put("d_isnewcourse", "N");
                    }
                    v_Bcourse   =   v_course;
                    v_Bcourseseq=   v_courseseq;
                    v_Buserid   =   v_guserid;
                    list2.add(dbox2);
                    if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
                } 
                
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }

    /**
    입과 인원 조회 리스트
    @param box      receive from the form object and session
    @return ArrayList 입과인원 리스트
    */
     public ArrayList selectStudentMemberCountList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;
        ArrayList list1     = null;
        ArrayList list2     = null;
        String sql1         = "";
        String sql2         = "";
        StudentStatusData data1= null;
        StudentStatusData data2= null;
        String  v_Bcourse   = ""; // 이전코스
        String  v_course    = ""; // 현재코스
        String  v_Bcourseseq= ""; // 이전코스기수
        String  v_courseseq = ""; // 현재코스기수

        int v_pageno = box.getInt("p_pageno");
        int     l             = 0;

        String  ss_grcode     = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear      = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq      = box.getStringDefault("s_grseq","ALL");     // 교육기수

        String  ss_uclass     = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_mclass     = box.getStringDefault("s_middleclass","ALL");    // 과목분류
        String  ss_lclass     = box.getStringDefault("s_lowerclass","ALL");    // 과목분류

        String  ss_subjcourse =box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq    = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_edustart   = box.getStringDefault("s_edustart","ALL");  // 교육시작일
        String  ss_eduend     = box.getStringDefault("s_eduend","ALL");    // 교육종료일
        String  ss_seltext    = box.getStringDefault("s_seltext","ALL");   // 검색분류별 검색내용
        String  ss_seldept    = box.getStringDefault("s_seldept","ALL");   // 사업부별 부서 검색내용
        String  ss_company    = box.getStringDefault("s_company","ALL");   // 사업부별 부서 검색내용
        
        String  ss_action     = box.getString("s_action");
        String  v_orderColumn= box.getString("p_orderColumn");           // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");          // 정렬할 순서
        String  v_selTab      = box.getString("p_selTab");               // 선택된 탭

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                list2 = new ArrayList();

                String v_subQry1 = "";
                String v_subQry2 = "";
                if ( !ss_company.equals("ALL")) {
                	v_subQry1 = " and comp = " + SQLString.Format(ss_company) + " ";
                	v_subQry2 = " and userid in (select userid from tz_member where comp = " + SQLString.Format(ss_company) + ") ";
                }

                // select grseq,course,cyear,coursenm,courseseq,subj,year,subjnm,subjseq,
                // propstart,propend,edustart,eduend,studentlimit,procnt,cancnt,isonoff
                sql1  = "select                                                                                                                                 \n" +
                        "       grseq,                                                                                                                          \n" +
                        "       course,                                                                                                                         \n" +
                        "       cyear,                                                                                                                          \n" +
                        "       coursenm,                                                                                                                       \n" +
                        "       courseseq,                                                                                                                      \n" +
                        "       subj,                                                                                                                           \n" +
                        "       year,                                                                                                                           \n" +
                        "       subjnm,                                                                                                                         \n" +
                        "       subjseq,                                                                                                                        \n" +
                        "       subjseqgr,                                                                                                                      \n";
                sql1 += "       propstart,                                                                                                                      \n" +
                        "       propend,                                                                                                                        \n" +
                        "       edustart,                                                                                                                       \n" +
                        "       eduend,                                                                                                                         \n" +
                        "       studentlimit,                                                                                                                   \n";
                sql1 += "       (select count(subj) from TZ_STUDENT where subj=B.subj and year=B.year and subjseq=B.subjseq and isGraduated in ('N')"+v_subQry1+") stucnt,   \n";
                sql1 += "       (select count(subj) from TZ_STUDENT where subj=B.subj and year=B.year and subjseq=B.subjseq and isGraduated in ('Y')"+v_subQry1+") comcnt,   \n";
                sql1 += "       (select count(subj) from TZ_CANCEL where subj=B.subj and year=B.year and subjseq=B.subjseq"+v_subQry2+") cancnt,                            \n";
                sql1 += "       (select count(subj) from TZ_PROPOSE where subj=B.subj and year=B.year and subjseq=B.subjseq"+v_subQry1+") procnt,                           \n";
                sql1 += "       (select count(subj) from TZ_Student where subj=B.subj and year=B.year and subjseq=B.subjseq"+v_subQry1+") proycnt,                         \n";
                sql1 += "       (select grseqnm from tz_grseq where grcode=B.grcode and gyear=B.gyear and grseq = B.grseq) grseqnm,                             \n";
                sql1 += "       get_codenm('0004',isonoff) isonoff                                                                                               \n";
                sql1 += "  from                                                                                                                                 \n" +
                        "       VZ_SCSUBJSEQ B                                                                                                                  \n";
                sql1 += " where                                                                                                                                 \n" +
                        "       B.gyear = " + SQLString.Format(ss_gyear);

                if ( !ss_grcode.equals("ALL"))     sql1 += " and B.grcode = "           + SQLString.Format(ss_grcode);
                if ( !ss_grseq.equals("ALL"))      sql1 += " and B.grseq = "            + SQLString.Format(ss_grseq);
                if ( !ss_uclass.equals("ALL"))     sql1 += " and B.scupperclass = "     + SQLString.Format(ss_uclass);
                if ( !ss_mclass.equals("ALL"))     sql1 += " and B.scmiddleclass = "    + SQLString.Format(ss_mclass);
                if ( !ss_lclass.equals("ALL"))     sql1 += " and B.sclowerclass = "     + SQLString.Format(ss_lclass);
                if ( !ss_subjcourse.equals("ALL")) sql1 += " and B.scsubj = "           + SQLString.Format(ss_subjcourse);
                if ( !ss_subjseq.equals("ALL"))    sql1 += " and B.scsubjseq = "        + SQLString.Format(ss_subjseq);

                // 학습대기중인과목선택시
                if ( v_selTab.equals("wait") ) { 
                    sql1 += " and B.edustart >= " + FormatDate.getDate("yyyyMMddHH");
                }
                // 학습진행중인과목선택시
                else if ( v_selTab.equals("progress") ) { 
                    sql1 += " and B.edustart < " + FormatDate.getDate("yyyyMMddHH");
                    sql1 += " and B.eduend >= " + FormatDate.getDate("yyyyMMddHH");
                }
                // 학습완료한과목선택시
                else if ( v_selTab.equals("finish") ) { 
                    sql1 += " and B.eduend < " + FormatDate.getDate("yyyyMMddHH");
                }

                // 교육시작일과 종료일이 선택되지 않은 경우에만 교육년도별도 검색함
                // if ( ss_edustart.equals("ALL") && ss_eduend.equals("ALL") ) { 
                //    sql1 += " and B.gyear = " +SQLString.Format(ss_gyear);
                // }
                // sql1 += " and userid=A.userid and subj=B.subj and year=B.year and subjseq=B.subjseq ";

                if ( v_orderColumn.equals("grseq"))   v_orderColumn = "b.grseq";
                if ( v_orderColumn.equals("subj"))    v_orderColumn = "b.subj";
                if ( v_orderColumn.equals("subjseq")) v_orderColumn = "b.subjseqgr";
                if ( v_orderColumn.equals("isonoff")) v_orderColumn = "b.isonoff";

                //if ( v_orderColumn.equals("") ) { 
                //    sql1 += " order by b.subj, b.year, b.subjseq";
                //} else { 
                //    sql1 += " order by " + v_orderColumn + v_orderType;
                //}
                
                if ( v_orderColumn.equals("") ) { 
                    sql1 += " order by b.course, b.cyear, b.courseseq, b.subj, b.year, b.subjseq";
                } else { 
                    sql1 += " order by b.course, b.cyear, b.courseseq, " + v_orderColumn + v_orderType;
                }                

                ls1 = connMgr.executeQuery(sql1);

                
                System.out.println("입과인원1 : " + sql1);
                
                
                ls1.setPageSize(row);                       // 페이지당 row 갯수를 세팅한다
                ls1.setCurrentPage(v_pageno);               // 현재페이지번호를 세팅한다.
                int total_page_count    = ls1.getTotalPage();  // 전체 페이지 수를 반환한다
                int total_row_count     = ls1.getTotalCount();  // 전체 row 수를 반환한다

                while ( ls1.next() ) { 
                    data1 = new StudentStatusData();
                    data1.setGrseq( ls1.getString("grseq") );
                    data1.setGrseqnm( ls1.getString("grseqnm") );
                    data1.setCourse( ls1.getString("course") );
                    data1.setCyear( ls1.getString("cyear") );
                    data1.setCourseseq( ls1.getString("courseseq") );
                    data1.setCoursenm( ls1.getString("coursenm") );
                    data1.setSubj( ls1.getString("subj") );
                    data1.setYear( ls1.getString("year") );
                    data1.setSubjseq( ls1.getString("subjseq") );
                    data1.setSubjseqgr( ls1.getString("subjseqgr") );
                    data1.setSubjnm( ls1.getString("subjnm") );
                    data1.setPropstart( ls1.getString("propstart") );
                    data1.setPropend( ls1.getString("propend") );
                    data1.setEdustart( ls1.getString("edustart") );
                    data1.setEduend( ls1.getString("eduend") );
                    data1.setStudentlimit( ls1.getInt("studentlimit") );
                    data1.setStucnt( ls1.getInt("stucnt") );
                    data1.setProcnt( ls1.getInt("procnt") );
                    data1.setProycnt( ls1.getInt("proycnt") );
                    data1.setComcnt( ls1.getInt("comcnt") );
                    data1.setCancnt( ls1.getInt("cancnt") );
                    data1.setIsonoff( ls1.getString("isonoff") );
                    data1.setDispnum(total_row_count - ls1.getRowNum() + 1);
                    data1.setTotalPageCount(total_page_count);
                    data1.setRowCount(row);

                    list1.add(data1);
                }
                
                for(int i=0;i < list1.size(); i++){
                    data2       =   (StudentStatusData)list1.get(i);
                    v_course    =   data2.getCourse();
                    v_courseseq =   data2.getCourseseq();
                    if(!v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq))){
                        sql2 = "select count(*) cnt from VZ_SCSUBJSEQ A where ";         // count(A.subj)              
                   
            			if (!ss_grcode.equals("ALL")) {
            				sql2+= " A.grcode = "+SQLString.Format(ss_grcode)+" and ";
            			}
            			if (!ss_gyear.equals("ALL")) {
            				sql2+= " A.gyear = "+SQLString.Format(ss_gyear)+" and ";
            			}
            			if (!ss_grseq.equals("ALL")) {
            				sql2+= " A.grseq = "+SQLString.Format(ss_grseq)+" and ";
            			}
            			if (!ss_uclass.equals("ALL")) {
            			    sql2+= " A.scupperclass = "+SQLString.Format(ss_uclass)+" and ";
            			}
            			if (!ss_mclass.equals("ALL")) {
            			    sql2+= " A.scmiddleclass = "+SQLString.Format(ss_mclass)+" and ";
            			}
            			if (!ss_subjcourse.equals("ALL")) {
            				sql2+= " A.scsubj = "+SQLString.Format(ss_subjcourse)+" and ";
            			}
            			if (!ss_subjseq.equals("ALL")) {
            				sql2+= " A.scsubjseq = "+SQLString.Format(ss_subjseq)+" and ";
            			}            
                        sql2+= " A.course = "+SQLString.Format(v_course)+" and A.courseseq = "+SQLString.Format(v_courseseq);

                        
                        System.out.println("입과인원1-" + i + " : " + sql2);
                        
                        ls2 = connMgr.executeQuery(sql2);	
                        if(ls2.next()){
                            data2.setRowspan(ls2.getInt("cnt"));
                            data2.setIsnewcourse("Y");
                        }
                    }else{
                        data2.setRowspan(0);
                        data2.setIsnewcourse("N");
                    }
                    v_Bcourse   =   v_course;
                    v_Bcourseseq=   v_courseseq;
                    list2.add(data2);
                    if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
                }   
                
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }


    /**
    폼메일 발송-사전예고메일
    @param box      receive from the form object and session
    @return int
    */
    public int sendStudyBeforeMail(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        ListSet             ls      = null;
        String              sql     = "";
        int cnt = 0;    //  메일발송이 성공한 사람수
        // p_checks로 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector v_check1     = new Vector();
        v_check1            = box.getVector("p_checks"); // userid,subj,year,subjseq

        Enumeration em1     = v_check1.elements();

        StringTokenizer st1 = null;
        String v_checks     = "";
        String v_subj       = "";
        String v_year       = "";
        String v_subjseq    = "";
        String v_userid     = "";

        try { 
            connMgr = new DBConnectionManager();

//// //// //// //// ////  폼메일 발송 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 
            String v_sendhtml = "mail3.html";
            FormMail fmail = new FormMail(v_sendhtml);     //      폼메일발송인 경우
            MailSet mset = new MailSet(box);        //      메일 세팅 및 발송
            String v_mailTitle = "안녕하세요? e-Eureka  운영자입니다.(진도율안내)-1";
//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

            while ( em1.hasMoreElements() ) { 
                v_checks    = (String)em1.nextElement();
                st1      = new StringTokenizer(v_checks,",");
                while ( st1.hasMoreElements() ) { 
                    // v_userid    = (String)st1.nextToken();
                    v_subj      = (String)st1.nextToken();
                    v_year      = (String)st1.nextToken();
                    v_subjseq   = (String)st1.nextToken();
                    break;
                }
                // select subjnm,passday,tstep,gradstep,gradscore,name,ismailing,cono,email
                sql = "select  B.subjnm,A.tstep,B.gradstep,B.sgradscore gradscore,D.name,D.ismailing,D.cono,D.email,";
                sql += "(to_number(to_char(sysdate, 'YYYYMMDD')) - to_number(substr(B.edustart,1,8))) passday ";
                sql += " from TZ_STUDENT A,VZ_SCSUBJSEQ B,TZ_MEMBER D ";
                sql += " where A.userid = " +SQLString.Format(v_userid);
                sql += " and A.subj = " +SQLString.Format(v_subj);
                sql += " and A.year = " +SQLString.Format(v_year);
                sql += " and A.subjseq = " +SQLString.Format(v_subjseq);
                sql += " and A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq and A.userid=D.userid ";
                sql += " group by B.subjnm,A.tstep,B.gradstep,B.sgradscore,D.name,D.ismailing,D.cono,D.email,B.edustart,B.eduend ";
                ls = connMgr.executeQuery(sql);
//                System.out.println("sql ==  ==  ==  == = > " +sql);

                while ( ls.next() ) { 
                    String v_toEmail =  ls.getString("email");
                    String v_toCono  =  ls.getString("cono");
                    String v_ismailing= ls.getString("ismailing");
                    // String v_toEmail =  "jj1004@dreamwiz.com";

                    mset.setSender(fmail);     //  메일보내는 사람 세팅

                    fmail.setVariable("tstep", ls.getString("tstep") );
                    fmail.setVariable("subjnm", ls.getString("subjnm") );
                    fmail.setVariable("passday", ls.getString("passday") );
                    fmail.setVariable("tstep", ls.getString("tstep") );
                    fmail.setVariable("gradstep", ls.getString("gradstep") );
                    fmail.setVariable("gradscore", ls.getString("gradscore") );
                    fmail.setVariable("toname", ls.getString("name") );

                    String v_mailContent = fmail.getNewMailContent();
//                    System.out.println("ismailing" + ls.getString("ismailing") );

                    boolean isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent,v_ismailing, v_sendhtml);
                    if ( isMailed) cnt++;     //      메일발송에 성공하면
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return cnt;
    }


    /**
    폼메일 발송-사후통보메일
    @param box      receive from the form object and session
    @return int
    */

    public int sendStudyAfterMail(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        ListSet             ls      = null;
        String              sql     = "";
        int cnt = 0;    //  메일발송이 성공한 사람수
        // p_checks로 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector v_check1     = new Vector();
        v_check1            = box.getVector("p_checks"); // userid,subj,year,subjseq

        Enumeration em1     = v_check1.elements();

        StringTokenizer st1 = null;
        String v_checks     = "";
        String v_subj       = "";
        String v_year       = "";
        String v_subjseq    = "";
        String v_userid     = "";

        try { 
            connMgr = new DBConnectionManager();

//// //// //// //// ////  폼메일 발송 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 
            String v_sendhtml = "mail3.html";
            FormMail fmail = new FormMail(v_sendhtml);     //      폼메일발송인 경우
            MailSet mset = new MailSet(box);        //      메일 세팅 및 발송
            String v_mailTitle = "안녕하세요? e-Eureka 운영자입니다.(진도율안내)-1";
//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

            while ( em1.hasMoreElements() ) { 
                v_checks    = (String)em1.nextElement();
                st1      = new StringTokenizer(v_checks,",");
                while ( st1.hasMoreElements() ) { 
                    v_userid    = (String)st1.nextToken();
                    v_subj      = (String)st1.nextToken();
                    v_year      = (String)st1.nextToken();
                    v_subjseq   = (String)st1.nextToken();
                    break;
                }
                // select subjnm,passday,tstep,gradstep,gradscore,name,ismailing,cono,email
                sql = "select  B.subjnm,A.tstep,B.gradstep,B.sgradscore gradscore,D.name,D.ismailing,D.cono,D.email,";
                sql += "(to_number(to_char(sysdate, 'YYYYMMDD')) - to_number(substr(B.edustart,1,8))) passday ";
                sql += " from TZ_STUDENT A,VZ_SCSUBJSEQ B,TZ_MEMBER D ";
                sql += " where A.userid = " +SQLString.Format(v_userid);
                sql += " and A.subj = " +SQLString.Format(v_subj);
                sql += " and A.year = " +SQLString.Format(v_year);
                sql += " and A.subjseq = " +SQLString.Format(v_subjseq);
                sql += " and A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq and A.userid=D.userid ";
                sql += " group by B.subjnm,A.tstep,B.gradstep,B.sgradscore,D.name,D.ismailing,D.cono,D.email,B.edustart,B.eduend ";
                ls = connMgr.executeQuery(sql);
//                System.out.println("sql ==  ==  ==  == = > " +sql);

                while ( ls.next() ) { 
                    String v_toEmail =  ls.getString("email");
                    String v_toCono  =  ls.getString("cono");
                    String v_ismailing= ls.getString("ismailing");
                    // String v_toEmail =  "jj1004@dreamwiz.com";

                    mset.setSender(fmail);     //  메일보내는 사람 세팅

                    fmail.setVariable("tstep", ls.getString("tstep") );
                    fmail.setVariable("subjnm", ls.getString("subjnm") );
                    fmail.setVariable("passday", ls.getString("passday") );
                    fmail.setVariable("tstep", ls.getString("tstep") );
                    fmail.setVariable("gradstep", ls.getString("gradstep") );
                    fmail.setVariable("gradscore", ls.getString("gradscore") );
                    fmail.setVariable("toname", ls.getString("name") );

                    String v_mailContent = fmail.getNewMailContent();
//                    System.out.println("ismailing" + ls.getString("ismailing") );

                    boolean isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent,v_ismailing, v_sendhtml);
                    if ( isMailed) cnt++;     //      메일발송에 성공하면
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return cnt;
    }




    /**
    폼메일 발송-독려메일
    @param box      receive from the form object and session
    @return int
    */
    public int sendFormMail(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        ListSet             ls      = null;
        String              sql     = "";
        int cnt = 0;    //  메일발송이 성공한 사람수
        // p_checks로 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector v_check1     = new Vector();
        v_check1            = box.getVector("p_checks"); // userid,subj,year,subjseq

        Enumeration em1     = v_check1.elements();

        StringTokenizer st1 = null;
        String v_checks     = "";
        String v_subj       = "";
        String v_year       = "";
        String v_subjseq    = "";
        String v_userid     = "";
        String v_ismailing  = box.getString("p_isMailing");


        String v_touch = box.getString("p_touch");
        String v_msubjnm = box.getString("p_msubjnm");
        String v_mseqgrnm = box.getString("p_mseqgrnm");
        String v_msubj = box.getString("p_msubj");
        String v_myear = box.getString("p_myear");
        String v_msubjseq = box.getString("p_msubjseq");

        int isOk = 0;

        DataBox             dbox    = null;


        try { 
            connMgr = new DBConnectionManager();

//// //// //// //// ////  폼메일 발송 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 
            // String v_sendhtml = "mail3.htm";
            String v_sendhtml = "mail_jindopush.html";
            FormMail fmail = new FormMail(v_sendhtml);     //      폼메일발송인 경우
            MailSet mset = new MailSet(box);        //      메일 세팅 및 발송
            // String v_mailTitle = "안녕하세요? HKMC 교육지원팀 입니다.(진도율안내) ";
            String v_mailTitle = v_msubjnm + "과정 학습진행 안내입니다.";
//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

            while ( em1.hasMoreElements() ) { 
                v_checks    = (String)em1.nextElement();
                st1      = new StringTokenizer(v_checks,",");
                while ( st1.hasMoreElements() ) { 
                    v_userid    = (String)st1.nextToken();
                    v_subj      = (String)st1.nextToken();
                    v_year      = (String)st1.nextToken();
                    v_subjseq   = (String)st1.nextToken();
                    break;
                }
                // select subjnm,passday,tstep,gradstep,gradscore,name,ismailing,cono,email
                sql = "select  B.subjnm,A.tstep,B.gradstep,B.gradscore,B.gradreport,B.gradexam,B.gradhtest,B.gradftest,B.wstep,B.wmtest,B.wftest,B.whtest,B.wreport,D.name,D.ismailing,D.cono,D.email,\n";
                sql += " B.edustart,B.eduend, \n";
                sql += " (to_number(to_char(sysdate, 'YYYYMMDD')) - to_number(substr(B.edustart,1,8))) passday \n";
                sql += " from TZ_STUDENT A,tZ_SUBJSEQ B,TZ_MEMBER D \n";
                sql += " where A.userid = " +SQLString.Format(v_userid);
                sql += " and A.subj = " +SQLString.Format(v_subj);
                sql += " and A.year = " +SQLString.Format(v_year);
                sql += " and A.subjseq = " +SQLString.Format(v_subjseq);
                sql += " and A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq and A.userid=D.userid \n";
                sql += " group by B.subjnm,A.tstep,B.gradstep,B.gradscore,B.gradreport,B.gradexam,B.gradhtest,B.gradftest,B.wstep,B.wmtest,B.wftest,B.whtest,B.wreport,D.name,D.ismailing,D.cono,D.email,B.edustart,B.eduend \n";


                ls = connMgr.executeQuery(sql);
                //System.out.println("sql ==  ==  ==  == = > " +sql);

                while ( ls.next() ) { 
                    String v_toEmail =  ls.getString("email");
                    String v_toCono  =  ls.getString("cono");
                    String v_suryo = "";
                    // String v_ismailing= ls.getString("ismailing");
                    // String v_toEmail =  "jj1004@dreamwiz.com";
                    int wmtest  = Integer.parseInt( ls.getString("wmtest") );
                    int wftest  = Integer.parseInt( ls.getString("wftest") );
                    int whtest  = Integer.parseInt( ls.getString("whtest") );

                    v_suryo = "-진도율 (" +ls.getString("wstep") + "%): " +ls.getString("gradstep") + "% 이상<br > ";
                    if ( whtest > 0) { 
                      v_suryo += "-형성평가 (" +whtest + "%): " +ls.getString("gradhtest") + "점 이상<br > ";
                    }
                    if ( wmtest > 0) { 
                      v_suryo += "-중간평가 (" +wmtest + "%): " +ls.getString("gradexam") + "점 이상<br > ";
                    }
                    if ( wftest > 0) { 
                      v_suryo += "-최종평가 (" +wftest + "%): " +ls.getString("gradftest") + "점 이상<br > ";
                    }
                    if ( !ls.getString("wreport").equals("0") ) { 
                      v_suryo += "-과제물 (" +ls.getString("wreport") + "%): " +ls.getString("gradreport") + "점 이상<br > ";
                    }
                    v_suryo += "-총점 : " +ls.getString("gradscore") + "점 이상시 수료가능<br > ";
                    v_suryo += "-수료점수 : " +ls.getString("gradscore") + "점";

                    mset.setSender(fmail);     //  메일보내는 사람 세팅

                    fmail.setVariable("tstep",     ls.getString("tstep") );
                    fmail.setVariable("subjnm",    ls.getString("subjnm") );
                    fmail.setVariable("passday",   ls.getString("passday") );
                    fmail.setVariable("tstep",     ls.getString("tstep") );
                    fmail.setVariable("gradstep",  v_suryo);
                    fmail.setVariable("gradscore", ls.getString("gradscore") );
                    fmail.setVariable("toname",    ls.getString("name") );
                    fmail.setVariable("edustart",  FormatDate.getFormatDate( ls.getString("edustart"), "yyyy.MM.dd") );
                    fmail.setVariable("eduend",    FormatDate.getFormatDate( ls.getString("eduend"), "yyyy.MM.dd") );

                    String v_mailContent = fmail.getNewMailContent();


                    boolean isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent,v_ismailing, v_sendhtml);
                    if ( isMailed) cnt++;     //      메일발송에 성공하면

                    dbox = ls.getDataBox();
                    dbox.put("d_subj", v_msubj);
                    dbox.put("d_year", v_myear);
                    dbox.put("d_subjseq", v_msubjseq);
                    dbox.put("d_userid", v_userid);
                    dbox.put("d_touch", v_touch);
                    dbox.put("d_ismail", "1");
                    dbox.put("d_title",v_mailTitle);
                    if ( isMailed) { 
                      dbox.put("d_isok", "Y");
                    } else { 
                      dbox.put("d_isok", "N");
                    }
                    dbox.put("d_ismailopen", "N");
                    dbox.put("d_subjnm", v_msubjnm);
                    dbox.put("d_seqgrnm", v_mseqgrnm);


                }

                isOk = mset.insertHumanTouch(dbox);

            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return cnt;
    }

    
    /**
     * 수험번호 생성
     * 
     * @param box
     * @return isOk 
     * @throws Exception
     */
    public int ExamNuminsert( RequestBox box ) throws Exception {
         
    	 DBConnectionManager connMgr	= null;
         ListSet ls1         			= null;
         String sql1        			= "";
         PreparedStatement   pstmt      = null;

         int                 isOk1           = 0;
       
         String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
         String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
         String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수
         String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
         String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수

         String              sqlu="";
         int                 count=1;
         String counts="";
         try { 
        	 
        	 connMgr = new DBConnectionManager();
        	 connMgr.setAutoCommit(false);
                 
                 sql1  = "SELECT                                                                \n" +
                         "    C.subj,                                                           \n" +
                         "    C.year,                                                           \n" +
                         "    C.subjnm,                                                         \n" +
                         "    C.subjseq,                                                        \n" +
                         "    B.userid,                                                         \n" +
                         "    B.name ,                                                          \n" +
                         "    fn_crypt('2', b.birth_date, 'knise') birth_date,                                                           \n" +
                         "    A.examnum                                                           \n" +
                         "FROM                                                                  \n" +
                         "    TZ_STUDENT                          A,                            \n" +
                         "    TZ_MEMBER                           B,                            \n" +
                         "    VZ_SCSUBJSEQ                        C,                            \n" +
                         "    TZ_SUBJSEQ                          D,                            \n" +
                         "    TZ_SUBJ                             E                             \n" +
                         "WHERE A.userid   = B.userid                                           \n" +
                         "    AND A.subj     = C.subj                                           \n" +
                         "    AND A.year     = C.year                                           \n" +
                         "    AND A.subjseq  = C.subjseq                                        \n" +
                         "    AND C.SUBJ = D.SUBJ                                               \n" +
                 		"    AND C.YEAR = D.YEAR                                               \n" +
                 		"    AND C.SUBJSEQ = D.SUBJSEQ                                         \n" +
                 		"    AND D.SUBJ = E.SUBJ                                               \n" + 
                 		" and C.grcode = " +SQLString.Format(ss_grcode)                        	+
                 		" and C.gyear = " +SQLString.Format(ss_gyear)                         	+
                 		" and C.grseq = " +SQLString.Format(ss_grseq)							+	
                 		" and C.scsubj = " +SQLString.Format(ss_subjcourse)						+
                 		" and C.scsubjseq = " +SQLString.Format(ss_subjseq)						;
                 sql1 += " order by b.name,a.examnum ,fn_crypt('2', b.birth_date, 'knise') , b.userid, C.subj, C.year, C.subjseq         					";
                 
//System.out.println("================수험번호 조회============================\n" + sql1);                  
                 ls1 = connMgr.executeQuery(sql1);

                 while (ls1.next()) {
                	 if (count < 10){
                		 counts ="000"+count;
                	 }else if(count >= 10 && count < 100){
                		 counts ="00"+count;
                	 }else if(count >= 100 && count < 1000){
                		 counts ="0"+count;
                	 }else{
                		 counts = Integer.toString(count);
                	 }
            	 sqlu = "update tz_student set ExamNum = '"+counts+
            	 		"' where  year = '"+ss_gyear+"' and subj='"+ss_subjcourse+
            	 		"' and subjseq ='"+ss_subjseq+"' and userid='"+ls1.getString("userid")+"' "; 
                	 count++;

                	 pstmt = connMgr.prepareStatement(sqlu);
                	 isOk1 = pstmt.executeUpdate();
                	 
                	 if(pstmt != null) {	
 	             	    pstmt.close();
 	                }
                 }
                 
                 if(isOk1 > 0)  {
                	 connMgr.commit();
                 }else {
                	 connMgr.rollback();
                 }
             
         }
         catch (Exception ex) {
             ErrorManager.getErrorStackTrace(ex, box, sql1.toString());
             throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
         }
         finally {
        	 if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
             if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }
         
         return isOk1;
     }
    
    


}