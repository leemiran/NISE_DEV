// **********************************************************
//  1. 제      목: SUBJECT INFORMATION USER BEAN
//  2. 프로그램명: ProposeCourseBean.java
//  3. 개      요: 과정안내 사용자 bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 2004.01.14
//  7. 수      정:
// **********************************************************
package com.ziaan.propose;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
//import java.util.StringTokenizer;
import java.util.TimeZone;


import com.ziaan.common.GetCodenm;
import com.ziaan.library.AutoMailBean;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.FreeMailBean;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.propose.email.SendRegisterCoursesEMail;
import com.ziaan.propose.email.SendRegisterCoursesEMailImplBean;
import com.ziaan.propose.email.log.EmailLog;
import com.ziaan.propose.email.log.impl.EmailLogImplBean;
import com.ziaan.scorm2004.ScormCourseBean;
import com.ziaan.system.CodeAdminBean;
import com.ziaan.system.SelectionData;

public class ProposeCourseBean { 
	public final static String GUBUN_CODE = "0049";
	public final static String WORK_CODE  = "W";
	public final static String LANG_CODE  = "L";
	
	//    private ConfigSet config;
	//    private int row; 

	public ProposeCourseBean() { 
		try { 
			//config = new ConfigSet();
			//row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        이 모듈의 페이지당 row 수를 셋팅한다
		} catch( Exception e ) { 
			e.printStackTrace();
		}      
	}

	
	  /**
	   * 새로운 자료실 내용 등록
	   * @param box      receive from the form object and session
	   * @return isOk    1:insert success,0:insert fail
	   * @throws Exception
	   */
	  public int insertPds(RequestBox box) throws Exception {
	    DBConnectionManager	connMgr	= null;
	    ListSet	ls = null;
	    PreparedStatement pstmt1 = null;
	    String              sql1     = "";
	    int	isOk1 =	1;

	    String v_title    	= box.getString("p_title");
	    String v_content  	= box.getString("p_content");
	    String v_content1 	= "";
	    String v_gubun 		=  "02";//box.getString("p_gubun");
	    String v_enddate	= box.getString("p_enddate");
	    String v_subj	= box.getString("p_wantsubj");
	    String v_seq	= box.getString("p_seq");

	    String s_userid = box.getSession("userid");
	    String s_usernm = box.getSession("name");
	    String s_orgnm = box.getSession("orgnm");

	    try	{
	      connMgr = new DBConnectionManager();
	      connMgr.setAutoCommit(false);

	      //// //// //// //// //// //// //// //// // 	table 에 입력  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
	      sql1 =	"insert	into tz_subjwant_user(subj, year, userid,seq)               ";
	      sql1 +=	" values (?, to_char(sysdate,	'YYYY'), ?, ?) ";

	      pstmt1 = connMgr.prepareStatement(sql1);

	      pstmt1.setString(1, v_subj);
	      pstmt1.setString(2, s_userid);
	      pstmt1.setString(3, v_seq);

	      isOk1 =	pstmt1.executeUpdate();

	      if ( isOk1 > 0) connMgr.commit();
	      else 		               connMgr.rollback();
	    }
	    catch ( Exception ex ) {
	      connMgr.rollback();
	      ErrorManager.getErrorStackTrace(ex,	box, sql1);
	      throw new Exception("sql = " + sql1	 + "\r\n" + ex.getMessage() );
	    }
	    finally	{
	      if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }	}
	      if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
	      if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
	      if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e10 ) { }	}
	    }
	    return isOk1;
	  }

	  
	/** 사용자 - 수요조사 리스트보기 
    @param box      receive from the form object and session
    @return ArrayList
	 */
	public ArrayList selectWantDateSubList(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		ListSet ls1             = null;
		DataBox dbox 						= null;
		ArrayList list1         = null;
		String sql1             = "";
		
		String ss_edustart 	= StringManager.replace(box.getStringDefault("s_edustart", ""), "-", ""); // 학습시작
		String ss_eduend 	= StringManager.replace(box.getStringDefault("s_eduend", ""), "-", ""); // 학습종료
		String ss_gyear     = box.getStringDefault("s_gyear", FormatDate.getDate("yyyy"));
		//String ss_title     = box.getString("s_title");
		String v_searchtext     = box.getString("p_searchtext");
		
		try {
			connMgr = new DBConnectionManager();
			list1 = new ArrayList();
			
			sql1 =  " select year , seq , startdate , enddate, title from tz_subjwant_master \n";
			sql1 += " where gubun = 'Y' \n"; 
			sql1 += " 	and startdate <= to_char(sysdate, 'yyyyMMdd') ";
			sql1 += " 	and enddate >= to_char(sysdate, 'yyyyMMdd') ";
			
			if (!ss_gyear.equals("ALL")) {
				sql1 += "   and year  ='"+ss_gyear+"' \n";
				
				// 시작일
				if (!ss_edustart.equals("")) {
					sql1 += "   and startdate  >="+ss_edustart+" \n";
					
					// 종료일
					if (!ss_eduend.equals("")) {
						sql1 += "   and enddate  <='"+ss_eduend+"' \n";
					}
				}
			}
			
			// 조사명
			if (!v_searchtext.equals("")) {
				sql1 += "   and title like '%" + v_searchtext + "%' \n";
			}
			
			
			ls1 = connMgr.executeQuery(sql1);
			while(ls1.next()) {
				dbox = ls1.getDataBox();
				
				list1.add(dbox);
			}
		}
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
		}
		finally {
			if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return list1;
	}	
	
	  /** 사용자 - 수요조사
    @param box      receive from the form object and session
    @return ArrayList
    */
  public ArrayList selectSubjWantList(RequestBox box) throws Exception {
    DBConnectionManager connMgr = null;
    ListSet ls1             = null;
    DataBox dbox 			= null;
    ArrayList list1         = null;
    String sql1             = "";
    String  v_user_id       = box.getSession("userid");
    
    String v_startdate		= box.getString("s_startdate");
    String v_enddate			= box.getString("s_enddate");

    // 개인의 회사 기준으로 과정 리스트
    String  v_comp      = box.getSession("comp");

    // 사이트의 GRCODE 로 과정 리스트
    String v_grcode = box.getSession("tem_grcode");

    String v_upperclass    	= box.getStringDefault("s_upperclass", "ALL");
    String v_middleclass   	= box.getStringDefault("s_middleclass", "ALL");
    String v_subjsearchkey	= box.getString("p_searchtext");
    String v_company		= box.getStringDefault("s_company", "ALL");
    String v_isessential	= box.getStringDefault("s_isessential", "ALL");

    String v_year			= box.getString("p_year");
    String v_seq			= box.getString("p_seq");
    
    String v_isonoff      = box.getStringDefault("p_searchGu1", "ALL"); //교육구분

    try {
      connMgr = new DBConnectionManager();
      list1 = new ArrayList();
      
      /*
      sql1 = "select al.subj, subjnm, upperclass, middleclass, lowerclass, isonoff, \n";
      sql1 +=" 	   upperclassname, middleclassname,lowerclassname, subjcodenm, \n";
      sql1 +=" 	   edutimes, score, isessential , decode(gg.subj, '', 'N', 'Y') gubun \n";
      sql1 +=" from ( select a.subj, a.subjnm, a.upperclass, a.lowerclass, a.isonoff, \n";
      sql1 +=" 			  		c.classname as upperclassname, d.classname as middleclassname, a.middleclass, GET_SUBJCLASSNM(a.upperclass, a.middleclass, a.lowerclass) lowerclassname, e.codenm as subjcodenm, \n";
      sql1 +=" 			  		a.cpsubj, a.edutimes, a.score, isessential \n";
      sql1 +=" 				from tz_subj a \n";
      sql1 +=" 			 		, (select upperclass, classname from tz_subjatt where middleclass = '000' and lowerclass = '000') c \n";
      sql1 +=" 			 		, (select upperclass, middleclass, classname from tz_subjatt where lowerclass = '000') d \n";
      sql1 +=" 					, (select code, codenm from tz_code where gubun = '0004') e \n";
      //sql1 +="				, (select comp, compnm from tz_compclass) f \n";
      sql1 +=" 				where a.upperclass = c.upperclass \n";
      sql1 +=" 					  and a.upperclass = d.upperclass \n";
      sql1 +=" 					  and a.middleclass = d.middleclass \n";
      sql1 +=" 					  and a.isonoff = e.code \n";
      //sql1 +=" 					  and a.cpsubj = f.comp(+) \n";
      sql1 +="					  and a.isvisible = 'Y' \n";
      
      sql1 +="     union all \n";
      sql1 +=" 				select b.course, b.coursenm, b.upperclass, b.middleclass, b.lowerclass) lowerclass, b.isonoff, \n";
      sql1 +=" 					   c.classname as upperclassname, d.classname as middleclassname, a.middleclass, GET_SUBJCLASSNM(b.upperclass, b.middleclass, b.lowerclass) lowerclass, e.codenm as subjcodenm, \n";
      sql1 +=" 					   b.cpsubj, 0 edutimes, 0 score, '' isessential \n";
      sql1 +=" 				from tz_course b \n";
      sql1 +=" 					, (select upperclass, classname from tz_subjatt where middleclass = '000' and lowerclass = '000') c \n";
      sql1 +=" 					 , (select upperclass, middleclass, classname from tz_subjatt where lowerclass = '000') d \n";
      sql1 +=" 					 , (select code, codenm from tz_code where gubun = '0004') e \n";
      //sql1 +="				, (select comp, compnm from tz_compclass) f \n";
      sql1 +=" 				where b.upperclass = c.upperclass \n";
      sql1 +=" 					  and b.upperclass = d.upperclass \n";
      sql1 +=" 					  and b.middleclass = d.middleclass \n";
      sql1 +=" 					  and b.isonoff = e.code \n";
      //sql1 +=" 					  and b.cpsubj = f.comp(+) \n";
      
      sql1 +=" 	) al ,(select subj from tz_subjwant_user where userid = "+SQLString.Format(v_user_id)+") gg, \n";
      sql1 +="	(select subj from tz_subjwant_SUBJ where year = "+SQLString.Format(v_year)+" and seq = " + v_seq +") bl \n";
      sql1 +=" where al.subj = gg.subj(+) \n";
      sql1 +=" 	and al.subj = bl.subj ";
      */
      
      sql1  =  " select al.subj, subjnm, upperclass, middleclass, lowerclass, isonoff, \n "; 
      sql1 +=  " 	   upperclassname, middleclassname,lowerclassname, subjcodenm, \n "; 
      sql1 +=  " 	   edutimes, score, isessential , decode(gg.subj, '', 'N', 'Y') gubun \n "; 
      sql1 +=  " from ( select a.subj, a.subjnm, a.upperclass, a.lowerclass, a.isonoff, \n "; 
   	  sql1 +=  " 			  		c.classname as upperclassname, d.classname as middleclassname, a.middleclass, GET_SUBJCLASSNM(a.upperclass, a.middleclass, a.lowerclass) lowerclassname, e.codenm as subjcodenm, \n "; 
      sql1 +=  " 			  		a.cpsubj, a.edutimes, a.score, isessential \n "; 
      sql1 +=  " 				from tz_subj a \n "; 
      sql1 +=  " 			 		, (select upperclass, classname from tz_subjatt where middleclass = '000' and lowerclass = '000') c \n "; 
      sql1 +=  " 			 		, (select upperclass, middleclass, classname from tz_subjatt where lowerclass = '000') d \n "; 
      sql1 +=  " 					, (select code, codenm from tz_code where gubun = '0004') e \n "; 
      sql1 +=  " 				where a.upperclass = c.upperclass \n "; 
      sql1 +=  " 					  and a.upperclass = d.upperclass \n "; 
      sql1 +=  " 					  and a.middleclass = d.middleclass \n "; 
      sql1 +=  " 					  and a.isonoff = e.code \n "; 
      sql1 +=  "					  and a.isvisible = 'Y' \n "; 
      sql1 +=  "     union all \n "; 
      sql1 +=  " 				select b.course, b.coursenm, b.upperclass, b.lowerclass lowerclass, '' isonoff, \n "; 
      sql1 +=  " 					   c.classname as upperclassname, d.classname as middleclassname, b.middleclass, GET_SUBJCLASSNM(b.upperclass, b.middleclass, b.lowerclass) lowerclassname, '' as subjcodenm, \n "; 
      sql1 +=  " 					   '' cpsubj, 0 edutimes, 0 score, '' isessential \n "; 
      sql1 +=  " 				from tz_course b \n "; 
      sql1 +=  " 					, (select upperclass, classname from tz_subjatt where middleclass = '000' and lowerclass = '000') c \n "; 
      sql1 +=  " 					, (select upperclass, middleclass, classname from tz_subjatt where lowerclass = '000') d \n "; 
      //sql1 +=  " 					 , (select code, codenm from tz_code where gubun = '0004') e \n "; 
      sql1 +=  " 				where b.upperclass = c.upperclass \n "; 
      sql1 +=  " 					  and b.upperclass = d.upperclass \n "; 
      sql1 +=  " 					  and b.middleclass = d.middleclass \n "; 
      //sql1 +=  " 					  and b.isonoff = e.code \n "; 
      sql1 +=  " 	) al ,(select subj from tz_subjwant_user where userid = "+SQLString.Format(v_user_id)+" and year = "+SQLString.Format(v_year)+" and seq = "+v_seq+" ) gg, \n "; 
      sql1 +=  "	(select subj from tz_subjwant_SUBJ where year = "+SQLString.Format(v_year)+" and seq = " + v_seq +") bl \n "; 
      sql1 +=  " where al.subj = bl.subj \n "; 
      sql1 +=  "   and bl.subj = gg.subj(+) \n ";
    	  
      //대분류
      if(!v_upperclass.equals("ALL")) {
        sql1 += "		and upperclass = " + SQLString.Format(v_upperclass);
      }
      //중분류
      if(!v_middleclass.equals("ALL")) {
        sql1 += "		and middleclass = " + SQLString.Format(v_middleclass);
      }
      //교육기관
      if(!v_company.equals("ALL")) {
        sql1 += "		and cpsubj = " + SQLString.Format(v_company);
      }
      //과정명
      if(!v_subjsearchkey.equals("")) {
        sql1 += "		and subjnm like '%" + v_subjsearchkey + "%'";
      }
      //필수여부
      if(!v_isessential.equals("ALL")) {
        sql1 += "		and isessential = " + SQLString.Format(v_isessential);
      }
      
      //교육구분
      if(!v_isonoff.equals("ALL")) {
        sql1 += "		and isonoff = " + SQLString.Format(v_isonoff);
      }
      
      //과정유형
//      if(!v_isonoff.equals("ALL")) {
//        sql1 += "		and isonoff = " + SQLString.Format(v_isonoff);
//      }     
//    교육기간 - 시작
//      if(!v_startdate.equals("")) {
//        sql1 += "		and startdate <= '" + v_startdate+ "23' \n";
//      }
      //교육기간 - 종료
//      if(!v_enddate.equals("")) {
//        sql1 += "		and enddate >= '" + v_enddate+ "00' \n";
//      }
      


      sql1 +=" order by al.subj, al.subjnm \n";

      ls1 = connMgr.executeQuery(sql1);

      while(ls1.next()) {
        dbox = ls1.getDataBox();

        list1.add(dbox);
      }
    }
    catch (Exception ex) {
      ErrorManager.getErrorStackTrace(ex, box, sql1);
      throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
    }
    finally {
      if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
      if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
    }
    return list1;
  }
	
	/**
	수강과정 리스트 (온라인)
	@param box      receive from the form object and session
	@return ArrayList
	*/
	public ArrayList selectSubjectList(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	    = null;
		DataBox             dbox1       = null;
		ListSet             ls1         = null;
		ArrayList           list1       = null;
		StringBuffer        sbSQL       = new StringBuffer("");

		//        int                 iSysAdd     = 0;  

		String              v_select    = box.getStringDefault("p_select"   , "ON"      );
		String              v_gubun     = box.getStringDefault("p_gubun"    , WORK_CODE );
		String              v_user_id   = box.getSession("userid");
		String              v_gadmin    = box.getSession("gadmin");

		// 개인의 회사 기준으로 과정 리스트
		//        String              v_comp      = box.getSession("comp"  );

		// 사이트의 GRCODE 로 과정 리스트
		String              v_grcode    = box.getSession("tem_grcode");
		//        String              gyear       = box.getStringDefault("gyear",FormatDate.getDate("yyyy"));

		try { 
			connMgr = new DBConnectionManager();
			list1   = new ArrayList();

			// 수강신청 가능한 과정
			sbSQL.append(" select distinct                                                                                 	\n")
				.append("         a.subj                                                                                   	\n")
				.append("     ,   a.subjnm                                                                                 	\n")
				.append("     ,   a.isonoff                                                                                	\n")
				.append("     ,   a.scupperclass                                                                           	\n")
				.append("     ,   a.scmiddleclass                                                                          	\n")
				.append("     ,   a.sclowerclass                                                                           	\n")
				.append("     ,   usebook                                                                                  	\n")
				.append("     ,   substr(a.specials, 1, 1)    isnew                                                     	\n")
				.append("     ,   substr(a.specials, 2, 1)    ishit                                                     	\n")
				.append("     ,   substr(a.specials, 3, 1)    isrecom                                                   	\n")
				.append("     ,   (                                                                                        	\n")
				.append("             select  classname                                                                    	\n")
				.append("             from    tz_subjatt x                                                                 	\n")
				.append("             where   x.upperclass    = a.scupperclass                                             	\n")
				.append("             and     x.middleclass   = '000'                                                      	\n")
				.append("             and     x.lowerclass    = '000'                                                      	\n")
				.append("         )                           uclassnm                                                     	\n")
				.append("     ,   (                                                                                        	\n")
				.append("             select  classname                                                                    	\n")
				.append("             from    tz_subjatt x                                                                 	\n")
				.append("             where   x.upperclass    = a.scupperclass                                             	\n")
				.append("             and     x.middleclass   = a.scmiddleclass                                            	\n")
				.append("             and     x.lowerclass    = '000'                                                      	\n")
				.append("         )                           mclassnm                                                     	\n")
				.append(" from    vz_scsubjseq        a                                                                    	\n")
				.append(" where   a.grcode            = " + SQLString.Format(v_grcode ) + "                                	\n")
				.append(" and     a.isuse             = 'Y'                                                                	\n")
				.append(" and     a.isonoff           = " +SQLString.Format(v_select  ) + "                                	\n")
				.append(" and     a.scupperclass      in (                                                                 	\n")
				.append("                                 select  upperclass                                               	\n")
				.append("                                 from    tz_classfymatch                                          	\n")
				.append("                                 where   matchcode = "  + SQLString.Format(v_gubun) + "           	\n")
				.append("                                )                                                                 	\n")
				.append(" and     to_char(sysdate,'yyyymmddhh24miss') between a.propstart and a.propend              		\n");

			if ( !v_gadmin.equals("A1") ) {
				sbSQL.append(" and subjvisible  = 'Y'                                                                      \n");
			}

			// 지정된 마스터과정만 출력
			sbSQL.append(" and     a.subj + a.year + a.subjseq																\n")
				.append("         not in (																					\n")
				.append("                 select  distinct scsubj || scyear || scsubjseq									\n")
				.append("                 from    vz_mastersubjseq															\n")
				.append("                 where   scsubj + scyear + scsubjseq												\n")
				.append("                         not in (																	\n")
				.append("                                 select  a.subjcourse || a.year || a.subjseq						\n")
				.append("                                 from    tz_mastersubj   a											\n")
				.append("                                     ,   tz_edutarget    b											\n")
				.append("                                 where   a.mastercd  = b.mastercd									\n")                               
				.append("                                 and     a.grcode    = " + SQLString.Format(v_grcode)  + "			\n")
				.append("                                 and     b.userid    = " + SQLString.Format(v_user_id) + "			\n")
				.append("                                )																	\n")
				.append("                 )																					\n")
				.append(" order by a.scupperclass, a.scmiddleclass, a.subjnm												\n");

			ls1         = connMgr.executeQuery(sbSQL.toString());

			while ( ls1.next() ) { 
				dbox1   = ls1.getDataBox();

				list1.add(dbox1);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}

		return list1;
	}


	/**
	수강과정 리스트 (오프라인)
	@param box      receive from the form object and session
	@return ArrayList
	*/
	public ArrayList selectSubjectOffList(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr     = null;
		DataBox             dbox        = null;
		ListSet             ls1         = null;
		ArrayList           list1       = null;
		StringBuffer        sbSQL       = new StringBuffer("");

		String              v_select    = box.getStringDefault("p_select","OFF");
		String              v_user_id   = box.getSession("userid");
		String              v_gadmin    = box.getSession("gadmin");


		// 사이트의 GRCODE 로 과정 리스트
		String              v_grcode    = box.getSession("tem_grcode");
		String              gyear       = box.getStringDefault("gyear", FormatDate.getDate("yyyy"));

		try { 
			connMgr     = new DBConnectionManager();
			list1       = new ArrayList();

			// 수강신청 가능한 과정                                                                                         
			sbSQL.append(" select  distinct                                                                                 \n")
				.append("         subj                                                                                    	\n")
				.append("     ,   subjnm                                                                                   	\n")
				.append("     ,   isonoff                                                                                  	\n")
				.append("     ,   scupperclass                                                                             	\n")
				.append("     ,   scmiddleclass                                                                            	\n")
				.append("     ,   sclowerclass                                                                             	\n")
				.append("     ,   usebook                                                                                  	\n")
				.append("     ,   substr(a.specials, 1 ,1)    isnew                                                     	\n")
				.append("     ,   substr(a.specials, 2 ,1)    ishit                                                     	\n")
				.append("     ,   substr(a.specials, 3 ,1)    isrecom                                                   	\n")
				.append("     ,   (                                                                                        	\n")
				.append("             select  classname                                                                    	\n")
				.append("             from    tz_subjatt  x                                                                	\n")                    
				.append("             where   x.upperclass    = a.scupperclass                                             	\n")
				.append("             and     x.middleclass   = '000'                                                      	\n")
				.append("             and     x.lowerclass    = '000'                                                      	\n")
				.append("         )                           uclassnm                                                     	\n")
				.append("     ,   (                                                                                        	\n")
				.append("             select  classname                                                                    	\n")
				.append("             from    tz_subjatt  x                                                                	\n")                    
				.append("             where   x.upperclass    = a.scupperclass                                             	\n")
				.append("             and     x.middleclass   = a.scmiddleclass                                            	\n")
				.append("             and     x.lowerclass    = '000'                                                      	\n")
				.append("         )                           mclassnm                                                     	\n")
				.append("     ,   (                                                                                        	\n")
				.append("             select  count(subjseq)                                                               	\n")
				.append("             from    tz_subjseq  x                                                                	\n")
				.append("             where   x.grcode        = " + SQLString.Format(v_grcode) + "                         	\n")
				.append("             and     x.subj          = a.subj                                                     	\n")
				.append("             and     x.gyear         = a.gyear                                                    	\n")
				.append("         )                           totcnt                                                       	\n")
				.append(" from    vz_scsubjseq            a                                                                	\n")                                         
				.append(" where   grcode          = " + SQLString.Format(v_grcode ) + "                                    	\n")
				.append(" and     isuse           = 'Y'                                                                    	\n")
				.append(" and     a.scyear        = " +SQLString.Format(gyear     ) + "                                    	\n")
				.append(" and     a.isonoff       = " +SQLString.Format(v_select  ) + "                                    	\n")
				.append(" and     to_char(sysdate,'yyyymmddhh24miss') between a.propstart and a.propend              		\n");                               

			// 총괄관리자의 경우에는 보여준다
			if ( !v_gadmin.equals("A1") ) {
				sbSQL.append(" and subjvisible = 'Y'                                                                       	\n");
			}

			// 지정된 마스터과정만 출력
			sbSQL.append(" and     a.subj || a.year || a.subjseq															\n")
				.append("         not in (																					\n")
				.append("                 select  distinct																	\n")
				.append("                         scsubj || scyear || scsubjseq												\n")
				.append("                 from    vz_mastersubjseq															\n")
				.append("                 where   scsubj || scyear || scsubjseq												\n")
				.append("                         not in (																	\n")
				.append("                                     select  a.subjcourse || a.year || a.subjseq					\n")
				.append("                                     from    tz_mastersubj   a										\n")
				.append("                                         ,   tz_edutarget    b										\n")
				.append("                                     where   a.mastercd  = b.mastercd								\n")
				.append("                                     and     a.grcode    = " + SQLString.Format(v_grcode ) + "		\n")
				.append("                                     and     b.userid    = " + SQLString.Format(v_user_id) + "		\n")
				.append("                                 )																	\n")
				.append("                )																					\n")
				.append(" order by scupperclass, scmiddleclass, subjnm														\n");

			ls1          = connMgr.executeQuery(sbSQL.toString());

			while ( ls1.next() ) { 
				dbox     = ls1.getDataBox();

				list1.add(dbox);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}

		return list1;
	}

	/**
	전체 과정 리스트 (온라인)
	@param box      receive from the form object and session
	@return ArrayList
	*/
	public ArrayList selectSubjectListAll(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		DataBox dbox1       = null;
		ListSet ls1         = null;
		ArrayList list1     = null;
		String sql1         = "";
		MainSubjSearchBean bean = new MainSubjSearchBean();

		String  v_select    = "ON";     // 온라인 전체과정
		String  v_user_id   = box.getSession("userid");
		String  v_gadmin    = box.getSession("gadmin");

		// 개인의 회사 기준으로 과정 리스트
		String  v_comp      = box.getSession("comp");

		// 사이트의 GRCODE 로 과정 리스트
		String v_grcode = box.getSession("tem_grcode");

		//        String  gyear          = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );

		try { 
			connMgr = new DBConnectionManager();
			list1 = new ArrayList();


			// 전체과정 리스트
			sql1 = " select distinct a.subj, a.year, a.subjseq, a.subjnm, a.isonoff, a.scupperclass, a.scmiddleclass, a.sclowerclass, a.usebook, "; // a.aescontentid, ";
			sql1 += "        a.propstart,  a.propend,  a.edustart,  a.eduend,  a.studentlimit,  a.subjseqgr, a.preurl, contenttype, ";
			sql1 += "        substr(a.specials,1,1) isnew, substr(a.specials,2,1) ishit, substr(a.specials,3,1) isrecom,                         ";
			sql1 += "        (select classname from tz_subjatt x                                                                                 ";
			sql1 += "          where x.upperclass = a.scupperclass and x.middleclass  = '000' and x.lowerclass = '000' ) uclassnm,               ";
			sql1 += "        (select classname from tz_subjatt x                                                                                 ";
			sql1 += "          where x.upperclass = a.scupperclass and x.middleclass = a.scmiddleclass and x.lowerclass = '000' ) mclassnm       ";
			sql1 += "   from VZ_SCSUBJSEQ a                                                                                                      ";
			sql1 += "  where a.grcode       = " + SQLString.Format(v_grcode);
			// 개인기준인지
			//            sql1 += "  where grcode in (select grcode from TZ_GRCOMP where comp like '" +v_comp + "%')                                         ";
			sql1 += "    and (select comp from tz_grseq where grcode  =a.grcode and gyear = a.gyear and grseq = a.grseq) = " + SQLString.Format(v_comp);
			sql1 += "    and a.isuse        = 'Y'                                                                                                ";
			sql1 += "    and a.isonoff      = " +SQLString.Format(v_select);
			sql1 += "    and to_char(sysdate,'yyyymmddhh24miss') between a.propstart and a.propend                                                   ";
			if ( !v_gadmin.equals("A1")) sql1 += " and subjvisible = 'Y'                                                                           ";
			// 지정된 마스터과정만 출력
			sql1 += "    and a.subj||a.year||a.subjseq not in (                                                                                  ";
			sql1 += "                select distinct scsubj||scyear||scsubjseq from vz_mastersubjseq                                             ";
			sql1 += "                 where scsubj||scyear||scsubjseq not in (                                                                   ";
			sql1 += "                              select a.subjcourse||a.year||a.subjseq from tz_mastersubj a , tz_edutarget b                  ";
			sql1 += "                               where a.mastercd = b.mastercd                                                                ";
			sql1 += "                                 and a.grcode = " + SQLString.Format(v_grcode);
			sql1 += "                                 and b.userid = " + SQLString.Format(v_user_id) + "  )                                      ";
			sql1 += "                                           )                                                                                ";

			sql1 += " order by a.scupperclass , a.scmiddleclass, a.subjnm ";

			ls1 = connMgr.executeQuery(sql1);

			while ( ls1.next() ) { 
				dbox1 = ls1.getDataBox();

				dbox1.put("d_ispropose", bean.getPropseStatus(box,ls1.getString("subj"),ls1.getString("subjseq"),ls1.getString("year"),v_user_id, "3") );
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
	인기과정소개
	@param box        receive from the form object and session
	@return ArrayList 과정 리스트
	*/
	public ArrayList selectSubjectListBest(RequestBox box) throws Exception { 
		DBConnectionManager    connMgr         = null;
		ListSet             ls1             = null;
		DataBox             dbox1           = null;
		ArrayList           list1           = null;
		StringBuffer        sbSQL           = new StringBuffer("");
		//         DataBox             dbox            = null;

		String              v_select        = "ON";     // 온라인 전체과정
		String              v_gubun         = box.getStringDefault("p_gubun", WORK_CODE);
		//         String              v_user_id       = box.getSession("userid");
		//         String              v_gadmin        = box.getSession("gadmin");

		String              v_comp          = box.getSession("comp"         );  // 개인의 회사 기준으로 과정 리스트
		String              v_grcode        = box.getSession("tem_grcode"   );  // 사이트의 GRCODE 로 과정 리스트
		//         String              gyear           = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );

		try { 
			connMgr = new DBConnectionManager();
			list1   = new ArrayList();

			// 전체과정 리스트
			sbSQL.append(" select  rownum                                                                                      \n") 
				.append("     ,   subj                                                                                        \n")
				.append("     ,   subjnm                                                                                      \n")
				.append("     ,   isonoff                                                                                     \n")
				.append("     ,   scupperclass                                                                                \n")
				.append("     ,   scmiddleclass                                                                               \n")
				.append("     ,   sclowerclass                                                                                \n")
				.append("     ,   uclassnm                                                                                    \n")
				.append("     ,   mclassnm                                                                                    \n")
				.append("     ,   cnt                                                                                         \n")
				.append(" from    (                                                                                           \n")                    
				.append("             select  distinct                                                                        \n")
				.append("                     a.subj                                                                          \n")
				.append("                 ,   a.subjnm                                                                        \n")
				.append("                 ,   a.isonoff                                                                       \n")
				.append("                 ,   a.scupperclass                                                                  \n")
				.append("                 ,   a.scmiddleclass                                                                 \n")
				.append("                 ,   a.sclowerclass                                                                  \n")
				.append("                 ,   (                                                                               \n")
				.append("                         select  classname                                                           \n")
				.append("                         from    tz_subjatt x                                                        \n")                    
				.append("                         where   x.upperclass    = a.scupperclass                                    \n")
				.append("                         and     x.middleclass   = '000'                                             \n")
				.append("                         and     x.lowerclass    = '000'                                             \n")
				.append("                     )                           uclassnm                                            \n")
				.append("                 ,   (                                                                               \n")
				.append("                         select  classname                                                           \n")
				.append("                         from    tz_subjatt x                                                        \n")                    
				.append("                         where   x.upperclass    = a.scupperclass                                    \n")
				.append("                         and     x.middleclass   = a.scmiddleclass                                   \n")
				.append("                         and     x.lowerclass    = '000'                                             \n")
				.append("                     )                           mclassnm                                            \n")
				.append("                 ,   b.cnt                                                                           \n")                              
				.append("             from      vz_scsubjseq    a                                                             \n")
				.append("                 ,   (                                                                               \n")
				.append("                         select  subj                                                                \n")
				.append("                             ,   count(userid) cnt                                                   \n")
				.append("                         from    tz_propose                                                          \n")
				.append("                         where   cancelkind is null                                                  \n")
				.append("                         group by subj                                                               \n")
				.append("                     )               b                                                               \n")
				.append("             where   a.subj      = b.subj                                                            \n")                                    
				.append("             and     a.grcode    = " + SQLString.Format(v_grcode ) + "                               \n")
				.append("             and     (                                                                               \n")
				.append("                         select  comp                                                                \n")
				.append("                         from    tz_grseq                                                            \n")
				.append("                         where   grcode  = a.grcode                                                  \n")
				.append("                         and     gyear   = a.gyear                                                   \n")
				.append("                         and     grseq   = a.grseq                                                   \n")
				.append("                     )           = " + SQLString.Format( v_comp  ) + "                               \n")
				.append("            and      a.isuse     = 'Y'                                                               \n")
				.append("            and      a.isonoff   = " + SQLString.Format(v_select ) + "                               \n")
				.append("            and      a.scupperclass in (                                                             \n")
				.append("                                         select  upperclass                                          \n")
				.append("                                         from    TZ_CLASSFYMATCH                                     \n")
				.append("                                         where   matchcode = " + SQLString.Format(v_gubun) + "       \n")
				.append("                                         )                                                           \n")
				.append("             order by b.cnt                                                                          \n")
				.append("         )                                                                                           \n")        
				.append(" where   rownum <= 5                                                                                 \n");        

			ls1     = connMgr.executeQuery(sbSQL.toString());

			while ( ls1.next() ) { 
				dbox1   = ls1.getDataBox();

				list1.add(dbox1);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}

		return list1;
	}


	/**
	과정상세보기
	@param box      receive from the form object and session
	@return ProposeCourseData
	*/
	public DataBox selectSubjectPreview(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	    = null;
		DataBox             dbox        = null;
		ListSet             ls1         = null;
		StringBuffer        sbSQL       = new StringBuffer("");

		String              v_subj      = box.getString("p_subj");

		try { 
			connMgr     = new DBConnectionManager();

			sbSQL.append(" select  subj                                             \n")
				.append("     ,   subjnm                                           \n")
				.append("     ,   contenttype                                          \n")
				.append("     ,   conturl                                          \n")
				.append("     ,   subjtarget                                       \n")
				.append("     ,   edudays                                          \n")
				.append("     ,   edutimes                                         \n")
				.append("     ,   place                                            \n")
				.append("     ,   placejh                                          \n")
				.append("     ,   edutype                                          \n")
				.append("     ,   subjtarget                                       \n")
				.append("     ,   edumans                                          \n")
				.append("     ,   intro                                            \n")
				.append("     ,   explain                                          \n")
				.append("     ,   owner                                            \n")
				.append("     ,   ownerman                                         \n")
				.append("     ,   ownertel                                         \n")
				.append("     ,   preurl                                           \n")
				.append("     ,   bookfilenamereal                                 \n")
				.append("     ,   bookfilenamenew                                  \n")
				.append("     ,   muserid                                          \n")
				.append("     ,   musertel                                         \n")
				.append("     ,   isoutsourcing                                    \n")
				.append("     ,   introducefilenamereal                            \n")
				.append("     ,   introducefilenamenew                             \n")
				.append("     ,   contenttype                                      \n")
				.append("     ,   tutor                                            \n")
				.append("     ,   tm.name tutornm                                  \n")
				.append("     ,   nvl((                                            \n")
				.append("              select betacpnm                             \n")
				.append("              from   tz_betacpinfo                        \n")
				.append("              where  betacpno = ts.producer               \n")
				.append("             ), nvl((                                     \n")
				.append("                 select  compnm                           \n")
				.append("                 from    tz_compclass                     \n")
				.append("                 where   comp = ts.producer               \n")
				.append("                ), (                                      \n")
				.append("                 select  cpnm                             \n")
				.append("                 from    tz_cpinfo                        \n")
				.append("                 where   cpseq = ts.producer              \n")
				.append("                )))           producernm                  \n")
				.append("     ,   nvl((                                            \n")
				.append("              select cpnm                                 \n")
				.append("              from   tz_cpinfo                            \n")
				.append("              where  cpseq = ts.owner                     \n")
				.append("              ), (                                        \n")
				.append("                  select compnm      		               \n")
				.append("                  from   tz_compclass                     \n")
				.append("                  where  comp = ts.owner                  \n")
				.append("                  ))         ownernm                      \n")
				.append("     , biyong                                             \n")
				.append("     , graduatednote                                      \n")
				.append("     , usebook                                            \n")
				.append("     , bookname                                           \n")
				.append("     , isgoyong                                           \n")
				.append(" from    tz_subj ts, tz_member tm        				   \n")
				.append(" where   ts.tutor = tm.userid(+)                          \n")
				.append(" and     ts.subj  = " +SQLString.Format(v_subj) + "       \n");

			ls1         = connMgr.executeQuery(sbSQL.toString());

			if ( ls1.next() ) { 
				dbox    = ls1.getDataBox();
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}

		return dbox;
	}


	/**
	기수정보상세보기
	@param box      receive from the form object and session
	@return ProposeCourseData
	*/
	public DataBox selectSubjSeqPreview(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		DataBox dbox        = null;
		ListSet ls1         = null;
		String sql          = "";
		String  v_subj      = box.getString("p_subj");
		String  v_subjseq   = box.getString("p_subjseq");
		String  v_year   = box.getString("p_year");
		//        String  gyear       = box.getString("gyear");
		//        String  v_isonoff   = box.getString("p_isonoff");

		try { 
			connMgr = new DBConnectionManager();
			sql = "  select                 \n";
			sql += "    a.propstart,           \n";
			sql += "    a.propend,             \n";
			sql += "    a.edustart,            \n";
			sql += "    a.eduend,              \n";
			sql += "    a.studentlimit,        \n";
			sql += "    a.subjseq,             \n";
			sql += "    a.subjseqgr,           \n";
			sql += "    a.subjnm,              \n";
			sql += "    b.edutimes,            \n";
			sql += "    a.biyong,              \n";
			sql += "    a.gradscore,           \n";               /* 수료기준-점수     */
			sql += "    a.gradstep,            \n";               /* 수료기준-진도율  */
			sql += "    a.gradreport,          \n";               /* 수료기준-과제     */
			sql += "    a.gradreportyn,        \n";               /* 수료기준-과제제출미제출*/
			sql += "    a.gradexam,            \n";               /* 수료기준-평가      */
			sql += "    a.gradftest,           \n";               /* 수료기준-평가      */
			sql += "    a.gradhtest,           \n";               /* 수료기준-평가      */
			sql += "    a.wstep,               \n";               /* 가중치-진도율      */
			sql += "    a.wmtest,              \n";               /* 가중치-중간평가   */
			sql += "    a.wftest,              \n";               /* 가중치-최종평가   */
			sql += "    a.whtest,              \n";               /* 가중치-형성평가   */
			sql += "    a.wreport,             \n";               /* 가중치-REPORT  */
			sql += "    a.wetc1,               \n";               /* 가중치-REPORT  */
			sql += "    a.wetc2,               \n";               /* 가중치-REPORT  */
			sql += "    a.isgoyong,            \n";
			sql += "    a.place,               \n";
			sql += "    a.placejh              \n";
			sql += "  from                      \n";
			sql += "    tz_subjseq a, tz_subj b \n";
			sql += "  where                    \n";
			sql += "    a.subj = b.subj\n";
			sql += "    and a.subj = " +SQLString.Format(v_subj) + "  \n";
			sql += "    and a.subjseq = " +SQLString.Format(v_subjseq) + "  \n";
			sql += "    and a.year = " +SQLString.Format(v_year) + "  \n";
			// sql += "    and gyear = " +SQLString.Format(gyear) + "  \n";

			ls1 = connMgr.executeQuery(sql);

			if ( ls1.next() ) { 
				dbox = ls1.getDataBox();
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return dbox;
	}


	/**
	개설기수 리스트
	@param box      receive from the form object and session
	@return ProposeCourseData
	*/
	public ArrayList selectSubjSeqList(RequestBox box) throws Exception { 
		DBConnectionManager connMgr         = null;
		DataBox             dbox            = null;
		//        DataBox             dbox1           = null;
		ListSet             ls1             = null;
		ArrayList           list1           = null;
		StringBuffer        sbSQL           = new StringBuffer("");

		MainSubjSearchBean  bean            = null;

		String              v_subj          = box.getString("p_subj"    );
		String              v_subjseq       = box.getString("p_subjseq" );
		String              v_year          = box.getString("p_year"    );
		//        String              ss_upperclass   = box.getStringDefault("p_upperclass", "A01");

		String              v_user_id       = box.getSession("userid"       );
		String              v_gadmin        = box.getSession("gadmin"       );
		// 개인의 회사 기준으로 과정 리스트
		//        String              v_comp          = box.getSession("comp"         );
		// 사이트의 GRCODE 로 과정 리스트
		String              v_grcode        = box.getSession("tem_grcode"   );

		String ss_userid = box.getSession("userid");
		String ss_comp = box.getSession("comp");
		try { 
			connMgr = new DBConnectionManager();
			list1   = new ArrayList();
			bean    = new MainSubjSearchBean();

			sbSQL.append(" select  subj                                                                                     \n")
				.append("		, (																							\n")
				.append("			select case when count(*) > 0 then 'Y' else 'N' end										\n")
				.append("		from tz_propose																				\n")
				.append("		where subj = a.subj																			\n")
				.append("		and year = a.year																			\n")
				.append("		and subjseq = a.subjseq																		\n")
				.append("		and userid = " + StringManager.makeSQL(ss_userid) + "										\n")
				.append("		group by subj, year, subjseq, userid														\n")
				.append("		) proposeyn																					\n")
				.append("     ,   subjseq                                                                                  \n")
				.append("     ,   year                                                                                     \n")
				.append("     ,   propstart                                                                                \n")
				.append("     ,   propend                                                                                  \n")
				.append("     ,   edustart                                                                                 \n")
				.append("     ,   eduend                                                                                   \n")
				.append("     ,   studentlimit                                                                             \n")
				.append("     ,   subjseqgr                                                                                \n")
				.append("     ,   isonoff                                                                                  \n")
				.append("     ,   contenttype                                                                              \n")
				.append("     ,   (                                                                                        \n")
				.append("             select  proposetype                                                                  \n")
				.append("             from    vz_mastersubjseq                                                             \n")
				.append("             where   subj    = a.subj                                                             \n")
				.append("             and     subjseq = a.subjseq                                                          \n")
				.append("             and     year    = a.year                                                             \n")
				.append("         )                               proposetype                                              \n")
				.append("     ,   (                                                                                        \n")
				.append("             select  mastercd                                                                     \n")
				.append("             from    vz_mastersubjseq                                                             \n")
				.append("             where   subj    = a.subj                                                             \n")
				.append("             and     subjseq = a.subjseq                                                          \n")
				.append("             and     year    = a.year                                                             \n")
				.append("         )                               mastercd                                                 \n")
				.append("     ,   (                                                                                        \n")
				.append("             select  count(*) cnt                                                                 \n")
				.append("             from    tz_propose                                                                   \n")
				.append("             where   subj    = a.subj                                                             \n")
				.append("             and     year    = a.year                                                             \n")
				.append("             and     subjseq = a.subjseq                                                          \n")
				.append("             and     nvl(cancelkind,' ') not in ('F','P')                                         \n")
				.append("         )                               propcnt                                                  \n")
				.append("     ,   (                                                                                        \n")
				.append("             select  ldate propose_date    \n") //TO_CHAR(TO_DATE(ldate, 'YYYYMMDDHH24MISS'), 'MM\"월\" DD\"일\"')
				.append("             from    tz_propose                                                                   \n")
				.append("             where   subj    = a.subj                                                             \n")
				.append("             and     year    = a.year                                                             \n")
				.append("             and     subjseq = a.subjseq                                                          \n")
				.append("             and     userid  = " + SQLString.Format(v_user_id) + "                                \n")
				.append("             and     nvl(cancelkind,' ') not in ('F','P')                                         \n")
				.append("         )                               propose_date                                             \n")
				.append("     ,  CASE WHEN WStep      > 0 THEN WStep ||  '%'                                                        \n")
				.append("             ELSE                      '0%'                                                               \n")
				.append("        END                                                                   WStep_Name                  \n")
				.append("     ,  CASE WHEN WMTest     > 0 THEN WMTest ||  '%'                                                       \n")
				.append("             ELSE                      '0%'                                                                  \n")
				.append("        END                                                                   WMTest_Name                 \n")
				.append("     ,  CASE WHEN WHTest     > 0 THEN WHTest || '%'                                                       \n")
				.append("             ELSE                      '0%'                                                                 \n")
				.append("        END                                                                   WHTest_Name                 \n")
				.append("     ,  CASE WHEN WFTest     > 0 THEN WFTest || '%'                                                       \n")
				.append("             ELSE                      '0%'                                                                  \n")
				.append("        END                                                                   WFTest_Name                 \n")
				.append("     ,  CASE WHEN WReport > 0 THEN WReport || '%'                                                         \n")
				.append("             ELSE                      '0%'                                                                \n")
				.append("        END                                                                   WReport_Name                \n")
				.append("     ,  CASE WHEN WEtc1       > 0 THEN WEtc1 || '%'                                                     \n")
				.append("             ELSE                      '0%'                                                                \n")
				.append("        END                                                                   WEtc1_Name                  \n")
				.append("     ,  CASE WHEN WEtc2       > 0 THEN WEtc2 || '%'                                                     \n")
				.append("             ELSE                      '0%'                                                              \n")
				.append("        END                                                                   WEtc2_Name                  \n")
				.append("     ,  CASE WHEN GradStep   > 0 THEN GradStep || '% 이상 진행 필수'                                      \n")
				.append("             ELSE                      ''                                                                 \n")
				.append("        END                                                                   GradStep_Name               \n")
				.append("     ,  CASE WHEN SGradScore > 0 THEN SGradScore || '점 이상 필수'                                        \n")
				.append("             ELSE ''                                                                                      \n")
				.append("        END                                                                   SGradScore_Name             \n")
				.append("     ,  (case when GradExam_Flag = 'R' then '평가 필수' else (                                                      \n")
				.append("                                                  CASE WHEN WMTest > 0 THEN '평가 선택'                   \n")
				.append("                                                       ELSE                 ''                            \n")
				.append("                                                  END                                                     \n")
				.append("                                                ) end                                                        \n")
				.append("               )                                                              GradExam_Name               \n")
				.append("     ,  (case when GradHTest_Flag = 'R' then '평가 필수' else (                                                      \n")
				.append("                                                  CASE WHEN WHTest > 0 THEN '평가 선택'                   \n")
				.append("                                                       ELSE                 ''                            \n")
				.append("                                                  END                                                     \n")
				.append("                                                ) end                                                        \n")
				.append("               )                                                              GradHTest_Name              \n")
				.append("     ,  (case when GradFTest_Flag = 'R' then '평가 필수' else (                                                      \n")
				.append("                                                  CASE WHEN WFTest > 0 THEN '평가 선택'                   \n")
				.append("                                                       ELSE                 ''                            \n")
				.append("                                                  END                                                     \n")
				.append("                                                ) end                                                         \n")
				.append("               )                                                              GradFTest_Name              \n")
				.append("     ,  (case when GradReport_Flag = 'R' then '과제 제출 필수' else (                                                 \n")
				.append("                                                       CASE WHEN WFTest > 0 THEN '과제 제출 선택'         \n")
				.append("                                                       ELSE                 ''                            \n")
				.append("                                                       END                                                \n")
				.append("                                                      ) end                                                \n")
				.append("               )                                                              GradReport_Name             \n")
				.append("     , sgradscore                                                                                           \n")
				.append("     , gradstep                                                                                           \n")
				.append("     , gradexam                                                                                           \n")
				.append("     , gradftest                                                                                          \n")
				.append("     , gradreport                                                                                         \n")
				.append("     , biyong                                                                                             \n")
				.append("     , a.usebook, a.bookname, a.isgoyong                                                                  \n")
				.append("     , a.placejh                                                                                            \n")
				.append(" from    vz_scsubjseq a, tz_grcomp b                                                                      \n")
				.append(" where   scsubj = " + SQLString.Format(v_subj    ) + "                                            \n");

			if ( !v_subjseq.equals("") ) {
				sbSQL.append(" and     subjseq= " + SQLString.Format(v_subjseq ) + "                                        \n");
			}

//			sbSQL.append(" and     grcode       = " + SQLString.Format(v_grcode  ) + "                                            \n");
			sbSQL.append(" and a.grcode = b.grcode                                                                          \n");
			sbSQL.append(" and b.comp = " + SQLString.Format(ss_comp) + "                                                   \n");
//				.append(" and     a.isblended  = 'N'                                                                             \n")
//				.append(" and     a.isexpert   = 'N'                                                                             \n");
									
			// 총괄관리자인경우에는 보여준다.                                                                               
			if ( !v_gadmin.equals("A1") ) {                                                                               
				sbSQL.append(" and seqvisible = 'Y'                                                                         \n");
			}

			if ( !v_year.equals("") ) {
				sbSQL.append(" and     gyear  = " + SQLString.Format(v_year   ) + "                                         \n");
			}

			// 지정된 마스터과정만 출력                                                                                     
			sbSQL.append(" and     a.subj || a.year || a.subjseq                                                           \n")
				.append("         not in (                                                                                 \n")
				.append("                     select  distinct                                                             \n")
				.append("                             scsubj || scyear || scsubjseq                                        \n")
				.append("                     from    vz_mastersubjseq                                                     \n")
				.append("                     where   scsubj || scyear || scsubjseq                                        \n")
				.append("                             not in (                                                             \n")          
				.append("                                     select  a.subjcourse || a.year || a.subjseq                  \n")
				.append("                                     from    tz_mastersubj   a                                    \n")
				.append("                                         ,   tz_edutarget    b                                    \n")
				.append("                                         ,   tz_grcomp       c                                    \n")
				.append("                                     where   a.mastercd  = b.mastercd                             \n")                                       
//				.append("                                     and     a.grcode    = " + SQLString.Format(v_grcode ) + "    \n")
				.append("                                     and    a.grcode = c.grcode                                   \n")
				.append("                                     and    c.comp = " + SQLString.Format(ss_comp) + "            \n")
				.append("                                     and     b.userid    = " + SQLString.Format(v_user_id) + "    \n")  
				.append("                                    )                                                             \n")
				.append("                 )                                                                                \n")
				.append(" order by subjseqgr                                                                               \n");

			ls1         = connMgr.executeQuery(sbSQL.toString());
			//System.out.println(sbSQL.toString());
			while ( ls1.next() ) { 
				dbox    = ls1.getDataBox();

				dbox.put("d_ispropose", bean.getPropseStatus(box,ls1.getString("subj"), ls1.getString("subjseq"), ls1.getString("year"), v_user_id, "3"));
				list1.add(dbox);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}

		return list1;
	}


	/**
	선수과정 리스트
	@param box      receive from the form object and session
	@return ArrayList 선수과정 리스트
	*/
	public ArrayList selectListPre(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ArrayList list      = null;
		ListSet ls          = null;
		String sql          = "";
		DataBox dbox        = null;
		String  v_subj      = box.getString("p_subj");

		try { 
			connMgr = new DBConnectionManager();
			list = new ArrayList();

			sql = " select a.gubun, b.subjnm from tz_subjrelate a, tz_subj b  ";
			sql += "  where a.rsubj = b.subj                                   ";
			sql += "    and gubun   = 'PRE'                                    ";
			sql += "    and a.subj  = " + SQLString.Format(v_subj);

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
	후수과정 리스트
	@param box      receive from the form object and session
	@return ArrayList 후수과정 리스트
	*/
	public ArrayList selectListNext(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ArrayList list      = null;
		ListSet ls          = null;
		String sql          = "";
		DataBox dbox        = null;
		String  v_subj      = box.getString("p_subj");

		try { 
			connMgr = new DBConnectionManager();
			list = new ArrayList();

			sql = " select a.gubun, b.subjnm from tz_subjrelate a, tz_subj b  ";
			sql += "  where a.rsubj = b.subj                                   ";
			sql += "    and gubun   = 'NEXT'                                     ";
			sql += " and a.subj     = " + SQLString.Format(v_subj);

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
	일차 리스트
	@param box      receive from the form object and session
	@return ArrayList
	*/
	public ArrayList selectLessonList(RequestBox box) throws Exception {
		DBConnectionManager connMgr     = null;
		DataBox             dbox        = null;
		ListSet             ls          = null;          
		ArrayList           list        = null;
		StringBuffer        sbSQL       = new StringBuffer("");

		//        ProposeCourseData   data        = null;
		String              v_subj      = box.getString("p_subj");          // 과정

		try { 
			connMgr = new DBConnectionManager();
			list    = new ArrayList();

			// select lesson,sdesc

			sbSQL.append(" select  lesson                                       \n")
				.append("     ,   sdesc                                        \n")
				.append(" from    tz_subjlesson                                \n")
				.append(" where   subj = " + SQLString.Format(v_subj) + "      \n")
				.append(" order by lesson                                      \n");

			ls = connMgr.executeQuery(sbSQL.toString());

			while ( ls.next() ) { 
				dbox    = ls.getDataBox();
				list.add(dbox);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls != null ) { 
				try { 
					ls.close();  
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
	강좌 리스트
	@param box      receive from the form object and session
	@return ArrayList
	*/
	public ArrayList selectLectureList(RequestBox box) throws Exception { 
		DBConnectionManager connMgr         = null;
		ListSet             ls              = null;
		ArrayList           list            = null;
		StringBuffer        sbSQL           = new StringBuffer("");
		ProposeCourseData   data            = null;

		String              v_subj          = box.getString("p_subj"    );
		String              v_year          = box.getString("p_year"    );
		String              v_subjseq       = box.getString("p_subjseq" );
		String              v_lectdate      = "";
		String              v_lectsttime    = "";

		try { 
			connMgr     = new DBConnectionManager();
			list        = new ArrayList();

			sbSQL.append(" select  lecture                                              \n")
				.append("     ,   sdesc                                                \n")
				.append("     ,   lectdate                                             \n")
				.append("     ,   lectsttime                                           \n")
				.append("     ,   (                                                    \n")
				.append("             select  name                                     \n")
				.append("             from    tz_tutor                                 \n")
				.append("             where   userid = a.tutorid                       \n")
				.append("         )   tutor                                            \n")
				.append(" from    tz_offsubjlecture   a                                \n")
				.append(" where   subj    = " + SQLString.Format(v_subj    ) + "       \n")
				.append(" and     year    = " + SQLString.Format(v_year    ) + "       \n")
				.append(" and     subjseq = " + SQLString.Format(v_subjseq ) + "       \n")
				.append(" order by lecture                                             \n");

			ls      = connMgr.executeQuery(sbSQL.toString());

			while ( ls.next() ) { 
				data        = new ProposeCourseData();

				v_lectdate  = FormatDate.getFormatDate( ls.getString("lectdate"),"yyyy.MM.dd");
				v_lectsttime= FormatDate.getFormatTime( ls.getString("lectsttime"),"HH:mm");

				data.setLecture     ( ls.getString("lecture")           );
				data.setSdesc       ( ls.getString("sdesc"  )           );
				data.setLecturedate ( v_lectdate + " " +v_lectsttime    );
				data.setTutor       ( ls.getString("tutor"  )           );

				list.add(data);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls != null ) { 
				try { 
					ls.close();  
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
	신청자 리스트
	@param box      receive from the form object and session
	@return ArrayList
	*/
	public ArrayList selectProposeList(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr     = null;
		DataBox             dbox        = null;
		ListSet             ls1         = null;
		//        ListSet             ls2         = null;
		ArrayList           list1       = null;
		//        ArrayList           list2       = null;
		StringBuffer        sbSQL       = new StringBuffer("");


		String              v_subj      = box.getString("p_subj");
		String              v_comp      = box.getSession("comp");
		String              gyear       = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );
		v_comp                          = v_comp.substring(0,4);

		try { 
			connMgr = new DBConnectionManager();
			list1   = new ArrayList();

			sbSQL.append(" select  c.subjseqgr                                                                      \n")
				.append("     ,   b.name                                                                           \n")
				.append("     ,   a.appdate                                                                        \n")
				.append(" from    tz_propose      a                                                                \n")
				.append("     ,   tz_member       b                                                                \n")
				.append("     ,   vz_scsubjseq    c                                                                \n")
				.append(" where   a.userid    = b.userid                                                           \n")
				.append(" and     a.subj      = c.scsubj                                                           \n")
				.append(" and     a.subjseq   = c.scsubjseq                                                        \n")
				.append(" and     a.year      = c.year                                                             \n")
				.append(" and     a.subj      = " + SQLString.Format( v_subj ) + "                                 \n")
				.append(" and     a.year      = " + SQLString.Format( gyear  ) + "                                 \n")
				.append(" and     c.grcode    in (                                                                 \n")
				.append("                         select  grcode                                                   \n")
				.append("                         from    tz_grcomp                                                \n")
				.append("                         where   comp like " + SQLString.Format(v_comp + "%") + "         \n")
				.append("                        )                                                                 \n")
				.append(" and     substr(to_char(sysdate,'yyyymmddhh24miss'), 1, 10) between c.propstart and c.edustart                \n")
				.append(" order by c.subjseqgr, b.comp, b.name                                                     \n");


			ls1 = connMgr.executeQuery(sbSQL.toString());

			while ( ls1.next() ) { 
				dbox     = ls1.getDataBox();

				list1.add(dbox);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}

		return list1;
	}


	/**
	수강신청
	@param box      receive from the form object and session
	@return int
	*/
	public int insertSubjectEduPropose(RequestBox box) throws Exception { 
		EmailLog emailLog=new EmailLogImplBean();
		DBConnectionManager connMgr             = null;
		PreparedStatement   pstmt               = null;
		ListSet             ls                  = null;
		ListSet ls2         = null;   
		StringBuffer        sbSQL               = new StringBuffer("");
		int                 isOk                = 0;

		String              v_subj              = box.getString("p_subj"        );
		String              v_year              = box.getString("p_year"        );
		String              v_subjseq           = box.getString("p_subjseq"     );
		String              v_user_id           = box.getSession("userid"       );
		String              v_comp              = box.getSession("comp"         );
		String              v_jik               = box.getSession("jikup"        );
		String              v_jit               = box.getString("p_jit");
		String              v_proposetype       = box.getString("p_proposetype");

		String              v_chkfinal          = "B";	// 미처리(신청승인전, 수강신청시 셋팅값)
		
		String v_lec_sel_no = box.getString("p_lec_sel_no");
		String v_is_attend  = box.getString("p_is_attend");
		if ("undefined".equals(v_is_attend)) {
			v_is_attend = "";
		}
		
		int p_tabseq=0;
		try { 
			connMgr         = new DBConnectionManager();
			p_tabseq=emailLog.getMaxTabseq();
	        box.put("p_tabseq", p_tabseq);
	        //System.out.println("p_tabseq===========>"+p_tabseq);

        if ( GetCodenm.chkIsSubj(connMgr,v_subj).equals("S")){
			sbSQL.setLength(0);
			sbSQL.append(" select  subj                                                 \n")
				 .append(" from    tz_propose                                           \n")
				 .append(" where   subj        = " + SQLString.Format(v_subj   ) + "    \n")
				 .append(" and     year        = " + SQLString.Format(v_year   ) + "    \n")
				 .append(" and     subjseq     = " + SQLString.Format(v_subjseq) + "    \n")
				 .append(" and     userid      = " + SQLString.Format(v_user_id) + "    \n");

			ls      = connMgr.executeQuery(sbSQL.toString());

			if("RC".equals(box.getString("p_isonoff"))) {	// 독서 통신 교육일 때 수강신청한 교재정보 넣어줌
				int v_bookcnt = box.getInt("p_bookcnt");
				for(int i=1; i <= v_bookcnt; i++) {
					int v_bookcode= box.getInt("p_" + i + "_radio");
					// INSERT tz_proposesubj table
					sbSQL.setLength(0);
					sbSQL.append(" insert into tz_proposebook                                   \n")
						 .append(" (                                                            \n")
						 .append("         subj                                                 \n")
						 .append("     ,   year                                                 \n")
						 .append("     ,   subjseq                                              \n")
						 .append("     ,   userid                                               \n")
						 .append("     ,   month                                                \n")
						 .append("     ,   bookcode                                             \n")
						 .append("     ,   status                                               \n")
						 .append(" ) values (                                                   \n")
						 .append("         ?                                                    \n")
						 .append("     ,   ?                                                    \n")
						 .append("     ,   ?                                                    \n")
						 .append("     ,   ?                                                    \n")
						 .append("     ,   ?                                                    \n")
						 .append("     ,   ?                                                    \n")
						 .append("     ,   ?                                                    \n")
						 .append(" )                                                            \n");

					pstmt   = connMgr.prepareStatement(sbSQL.toString());

					pstmt.setString(1, v_subj);
					pstmt.setString(2, v_year);
					pstmt.setString(3, v_subjseq);
					pstmt.setString(4, v_user_id);
					pstmt.setInt   (5, i);
					pstmt.setInt   (6, v_bookcode);
					pstmt.setString(7, "B"); // 상태값  B:미승인(사용자가 수강신청시), W:대기(운영자가 일괄처리시)
					isOk = pstmt.executeUpdate();

					if ( pstmt != null ) pstmt.close();
					
				
					
				}
				//System.out.println("수강신청시 sql===> "+sbSQL.toString());
				
				
			}
			
			if("J".equals(v_jit)) {	// JIT일 경우에는 수강신청 시 바로 승인 처리까지 되어야 함
				v_chkfinal = "Y";
				// INSERT Tz_Propose Table
				sbSQL.setLength(0);
				sbSQL.append(" insert into tz_propose                                      \n")
					.append(" (                                                            \n")
					.append("         subj                                                 \n")
					.append("     ,   year                                                 \n")
					.append("     ,   subjseq                                              \n")
					.append("     ,   userid                                               \n")
					.append("     ,   comp                                                 \n")
					.append("     ,   jik                                                  \n")
					.append("     ,   appdate                                              \n")
					.append("     ,   chkfirst                                             \n")
					.append("     ,   chkfinal                                             \n")
					.append("     ,   luserid                                              \n")
					.append("     ,   ldate                                                \n")
					.append("     ,   lec_sel_no                                                \n")
					.append("     ,   is_attend                                                \n")
					.append("     ,   is_attend_dt                                                \n")
					.append("     ,   gubun                                                \n")
					.append(" ) values (                                                   \n")
					.append("         ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   'Y'                                                  \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   ?		           									   \n")
					.append("     ,   ?		         									   \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   ?		           \n")
					.append(" )                                                            \n");

				pstmt   = connMgr.prepareStatement(sbSQL.toString());

				pstmt.setString(1, v_subj       );
				pstmt.setString(2, v_year       );
				pstmt.setString(3, v_subjseq    );
				pstmt.setString(4, v_user_id    );
				pstmt.setString(5, v_comp       );
				pstmt.setString(6, v_jik        );
				pstmt.setString(7, v_chkfinal   );
				pstmt.setString(8, v_user_id    );
				pstmt.setString(9, v_lec_sel_no    );
				pstmt.setString(10, v_is_attend    );
				pstmt.setString(11, box.getString("p_pay_sel"));

				isOk    = pstmt.executeUpdate();
				
				if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
				
				
			

				if(isOk > 0) {
					Hashtable insertData = new Hashtable();
                    
                    insertData.put("connMgr"    , connMgr       );
                    insertData.put("subj"       , v_subj        );
                    insertData.put("year"       , v_year        );
                    insertData.put("subjseq"    , v_subjseq     );
                    insertData.put("userid"     , v_user_id      );
                    insertData.put("isdinsert"  , "N"           );
                    insertData.put("chkfirst"   , ""            );
                    insertData.put("chkfinal"   , "Y"   );
                    insertData.put("box"        , box           );
                    
                    ProposeBean propBean = new ProposeBean();
                    isOk = propBean.insertStudent(insertData);
				}
			} else {
				if(v_proposetype.equals("Y")){
					v_chkfinal = "Y";
					// INSERT Tz_Propose Table
					sbSQL.setLength(0);
					sbSQL.append(" insert into tz_propose                                      \n")
						.append(" (                                                            \n")
						.append("         subj                                                 \n")
						.append("     ,   year                                                 \n")
						.append("     ,   subjseq                                              \n")
						.append("     ,   userid                                               \n")
						.append("     ,   comp                                                 \n")
						.append("     ,   jik                                                  \n")
						.append("     ,   appdate                                              \n")
						.append("     ,   chkfirst                                             \n")
						.append("     ,   chkfinal                                             \n")
						.append("     ,   luserid                                              \n")
						.append("     ,   ldate                                                \n")
						.append("     ,   lec_sel_no                                                \n")
						.append("     ,   is_attend                                                \n")
						.append("     ,   is_attend_dt                                                \n")
						.append("     ,   gubun                                                \n")
						.append(" ) values (                                                   \n")
						.append("         ?                                                    \n")
						.append("     ,   ?                                                    \n")
						.append("     ,   ?                                                    \n")
						.append("     ,   ?                                                    \n")
						.append("     ,   ?                                                    \n")
						.append("     ,   ?                                                    \n")
						.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
						.append("     ,   'Y'                                                  \n")
						.append("     ,   ?                                                    \n")
						.append("     ,   ?                                                    \n")
						.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
						.append("     ,   ?		                                               \n")
						.append("     ,   ?		                                               \n")
						.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
						.append("     ,   ?		                                               \n")
						.append(" )                                                            \n");

					pstmt   = connMgr.prepareStatement(sbSQL.toString());

					pstmt.setString(1, v_subj       );
					pstmt.setString(2, v_year       );
					pstmt.setString(3, v_subjseq    );
					pstmt.setString(4, v_user_id    );
					pstmt.setString(5, v_comp       );
					pstmt.setString(6, v_jik        );
					pstmt.setString(7, v_chkfinal   );
					pstmt.setString(8, v_user_id    );
					pstmt.setString(9, v_lec_sel_no    );
					pstmt.setString(10, v_is_attend    );
					pstmt.setString(11, box.getString("p_pay_sel"));
					isOk    = pstmt.executeUpdate();

					if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }

					if(isOk > 0) {
						Hashtable insertData = new Hashtable();
	                    
	                    insertData.put("connMgr"    , connMgr       );
	                    insertData.put("subj"       , v_subj        );
	                    insertData.put("year"       , v_year        );
	                    insertData.put("subjseq"    , v_subjseq     );
	                    insertData.put("userid"     , v_user_id      );
	                    insertData.put("isdinsert"  , "N"           );
	                    insertData.put("chkfirst"   , ""            );
	                    insertData.put("chkfinal"   , "Y"   );
	                    insertData.put("box"        , box           );
	                    
	                    ProposeBean propBean = new ProposeBean();
	                    isOk = propBean.insertStudent(insertData);
					}
				}
				else{
					//	INSERT Tz_Propose Table
					sbSQL.setLength(0);
					sbSQL.append(" insert into tz_propose                                      \n")
					.append(" (                                                            \n")
					.append("         subj                                                 \n")
					.append("     ,   year                                                 \n")
					.append("     ,   subjseq                                              \n")
					.append("     ,   userid                                               \n")
					.append("     ,   comp                                                 \n")
					.append("     ,   jik                                                  \n")
					.append("     ,   appdate                                              \n")
					.append("     ,   chkfirst                                             \n")
					.append("     ,   chkfinal                                             \n")
					.append("     ,   luserid                                              \n")
					.append("     ,   ldate                                                \n")
					.append("     ,   lec_sel_no                                                \n")
					.append("     ,   is_attend                                                \n")
					.append("     ,   is_attend_dt                                                \n")
					.append("     ,   gubun                                                \n")
					.append(" ) values (                                                   \n")
					.append("         ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   'Y'                                                  \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   ?                                                    \n")
					.append(" )                                                            \n");
					
					pstmt   = connMgr.prepareStatement(sbSQL.toString());
					
					pstmt.setString(1, v_subj       );
					pstmt.setString(2, v_year       );
					pstmt.setString(3, v_subjseq    );
					pstmt.setString(4, v_user_id    );
					pstmt.setString(5, v_comp       );
					pstmt.setString(6, v_jik        );
					pstmt.setString(7, v_chkfinal   );
					pstmt.setString(8, v_user_id    );
					pstmt.setString(9, v_lec_sel_no    );
					pstmt.setString(10, v_is_attend    );
					pstmt.setString(11, box.getString("p_pay_sel"));
					isOk    = pstmt.executeUpdate();
					
					if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
				}
			}
			if("Y".equals(CodeAdminBean.getIsScorm(v_subj))) {
            	try {
                     // SCORM2004 : 사용자별 SeqInfo 객체 생성
            		ScormCourseBean scb = new ScormCourseBean();
                	String [] emp_id = { v_user_id };
 	                boolean result = scb.processTreeObjectForUsers(v_subj, v_year, v_subjseq, emp_id );
                 } catch( IOException ioe ) {
                     isOk  = -3;
                 } catch( Exception e ) {
                     isOk  = -3;
                 }
            }
			
			if(isOk > 0) {
				// 메일 발송
				if ( GetCodenm.chkIsSubj(connMgr,box.getString("p_subj")).equals("S")){
//		            AutoMailBean        bean2       = new AutoMailBean();
//                    bean2.EnterSubjSendMail(box);
                    
                  //사용자가 수강신청시 자동메일 발송(승인대기)
    				//SendRegisterCoursesEMail mail=new SendRegisterCoursesEMailImplBean();
    				
    				//mail.sendRegisterCoursesMail(box);
            	}                    
			}
			
        } else {
			sbSQL.setLength(0);
			sbSQL.append(" select subj, year, subjseq, isonoff                                               \n");        	
			sbSQL.append("from vz_scsubjseq                                  	 	\n");
			sbSQL.append("where course = " + SQLString.Format(v_subj) + "     		\n");
			sbSQL.append("and cyear = " + SQLString.Format(v_year) + "        		\n");
			sbSQL.append("and courseseq = " + SQLString.Format(v_subjseq) + " 		\n");
        	
        	ls2 = connMgr.executeQuery(sbSQL.toString());
        		
        	while(ls2.next()) {
        		v_subj = ls2.getString("subj");
        		v_year = ls2.getString("year");
        		v_subjseq = ls2.getString("subjseq");
        		
        		if("RC".equals(ls2.getString("isonoff"))) { // 독서 통신 교육일 때 수강신청한 교재정보 넣어줌
            		int v_bookcnt = box.getInt("p_bookcnt");
                	for(int i=1; i <= v_bookcnt; i++) {
                		int v_bookcode= box.getInt("p_" + i + "_radio");
                		// INSERT tz_proposesubj table
                    	sbSQL.setLength(0);
                        sbSQL.append(" INSERT INTO TZ_PROPOSEBOOK                                   \n")
                             .append(" (                                                            \n")
                             .append("         subj                                                 \n")
                             .append("     ,   year                                                 \n")
                             .append("     ,   subjseq                                              \n")
                             .append("     ,   userid                                               \n")
                             .append("     ,   month                                                \n")
                             .append("     ,   bookcode                                             \n")
                             .append(" ) VALUES (                                                   \n")
                             .append("         ?                                                    \n")
                             .append("     ,   ?                                                    \n")
                             .append("     ,   ?                                                    \n")
                             .append("     ,   ?                                                    \n")
                             .append("     ,   ?                                                    \n")
                             .append("     ,   ?                                                    \n")
                             .append(" )                                                            \n");
                             
                        pstmt   = connMgr.prepareStatement(sbSQL.toString());
                        
                        pstmt.setString(1, v_subj);
                        pstmt.setString(2, v_year);
                        pstmt.setString(3, v_subjseq);
                        pstmt.setString(4, v_user_id);
                        pstmt.setInt(5, i);
                        pstmt.setInt(6, v_bookcode);
                        isOk = pstmt.executeUpdate();
                        
                        if ( pstmt != null ) pstmt.close(); 
                	}
        		}
        		
        	   // INSERT Tz_Propose Table
        	   sbSQL.setLength(0);
               sbSQL.append(" INSERT INTO Tz_Propose                                       \n")
                    .append(" (                                                            \n")
                    .append("         subj                                                 \n")
                    .append("     ,   year                                                 \n")
                    .append("     ,   subjseq                                              \n")
                    .append("     ,   userid                                               \n")
                    .append("     ,   comp                                                 \n")
                    .append("     ,   jik                                                  \n")
                    .append("     ,   appdate                                              \n")
                    .append("     ,   chkfirst                                             \n")
                    .append("     ,   chkfinal                                             \n")
                    .append("     ,   luserid                                              \n")
                    .append("     ,   ldate                                                \n")
                    .append("     ,   lec_sel_no                                                \n")
                    .append("     ,   is_attend                                                \n")
                    .append("     ,   is_attend_dt                                                \n")
                    .append("     ,   gubun                                                \n")
                    .append(" ) VALUES (                                                   \n")
                    .append("         ?                                                    \n")
                    .append("     ,   ?                                                    \n")
                    .append("     ,   ?                                                    \n")
                    .append("     ,   ?                                                    \n")
                    .append("     ,   ?                                                    \n")
                    .append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   'Y'                                                  \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   ?		           \n")
                    .append(" )                                                            \n");
                    
               pstmt   = connMgr.prepareStatement(sbSQL.toString());
               
               pstmt.setString(1, v_subj       );
               pstmt.setString(2, v_year       );
               pstmt.setString(3, v_subjseq    );
               pstmt.setString(4, v_user_id    );
               pstmt.setString(5, v_comp       );
               pstmt.setString(6, v_jik        );
               pstmt.setString(7, v_chkfinal   );
               pstmt.setString(8, v_user_id    );
               pstmt.setString(9, v_lec_sel_no    );
			   pstmt.setString(10, v_is_attend    );
			   pstmt.setString(11, box.getString("p_pay_sel"));
               isOk    = pstmt.executeUpdate();
               if ( pstmt != null ) { 
                   try { 
                       pstmt.close();  
                   } catch ( Exception e ) { } 
               }
               
        	}
        }
       //정상 저장되면 결제정보에 넣어준다.(추가 - 서지한) - 무통장이나 교육청 일괄일때만 적용됨.(카드등은 다른 로직탐)
		if(isOk > 0){
			PreparedStatement   pstmt1               = null;
			String sql =" insert into pa_payment(order_id,userid,leccode,lecnumb,type,auth_date,year,enterance_dt, amount)";
			       sql+=" values(?,?,?,?,?,to_char(sysdate,'yyyymmddhh24miss'),to_char(sysdate,'yyyy'),?,?)";
			       
			       pstmt1   = connMgr.prepareStatement(sql);   
			       int idx = 1;
			       pstmt1.setString(idx++,box.getString("p_order_id"));//주문번호
			       pstmt1.setString(idx++,v_user_id);//회원아이디
			       pstmt1.setString(idx++,v_subj);//강좌코드
			       pstmt1.setString(idx++,v_subjseq);//차수
			       pstmt1.setString(idx++,box.getString("p_pay_sel"));//결제종류
			       pstmt1.setString(idx++,box.getString("p_enterance_dt"));//입금예정일
			       pstmt1.setString(idx++,box.getString("p_amount"));//금액
			       isOk = pstmt1.executeUpdate();
		}
		
		//주문번호를 신청 테이블에 넣는다.(추가 - 서지한) - 무통장이나 교육청 일괄일때만 적용됨.(카드등은 다른 로직탐)
		if(isOk > 0){
			PreparedStatement   pstmt1               = null;
			String sql =" update tz_propose set order_id = ? ";
				   sql+=" where subj = ? ";
				   sql+=" and year = ? ";
				   sql+=" and userid = ? ";
				   sql+=" and subjseq = ? ";
						       
			       pstmt1   = connMgr.prepareStatement(sql);   
			       int idx = 1;
			       pstmt1.setString(idx++,box.getString("p_order_id"));//주문번호
			       pstmt1.setString(idx++,v_subj);//강좌코드
			       pstmt1.setString(idx++,v_year);//회원아이디
			       pstmt1.setString(idx++,v_user_id);//회원아이디
			       pstmt1.setString(idx++,v_subjseq);//차수
			       isOk = pstmt1.executeUpdate();
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
			if ( ls2 != null )  { try { ls2.close(); } catch ( Exception e1 ) { } }       
			
			if ( pstmt != null ) { 
				try { 
					pstmt.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}

		return isOk;        
	}

	/**
	수강신청
	@param box      receive from the form object and session
	@return int
	*/
	public int insertSubjectEduProposePg(RequestBox box) throws Exception { 
		EmailLog emailLog=new EmailLogImplBean();
		DBConnectionManager connMgr             = null;
		PreparedStatement   pstmt               = null;
		ListSet             ls                  = null;
		ListSet ls2         = null;   
		StringBuffer        sbSQL               = new StringBuffer("");
		int                 isOk                = 0;

		String              v_subj              = box.getString("p_subj"        );
		String              v_year              = box.getString("p_year"        );
		String              v_subjseq           = box.getString("p_subjseq"     );
		String              v_user_id           = box.getString("p_userid"       );
		String              v_comp              = box.getString("p_comp"         );
		String              v_jik               = box.getString("p_jikup"        );
		String              v_jit               = box.getString("p_jit");
		String              v_proposetype       = box.getString("p_proposetype");

		String              v_chkfinal          = "B";	// 미처리(신청승인전, 수강신청시 셋팅값)
		
		String v_lec_sel_no = box.getString("p_lec_sel_no");
		String v_is_attend  = box.getString("p_is_attend");
		if ("undefined".equals(v_is_attend)) {
			v_is_attend = "";
		}
		
		int p_tabseq=0;
		try { 
			connMgr         = new DBConnectionManager();
			p_tabseq=emailLog.getMaxTabseq();
	        box.put("p_tabseq", p_tabseq);
	        //System.out.println("p_tabseq===========>"+p_tabseq);

        if ( GetCodenm.chkIsSubj(connMgr,v_subj).equals("S")){
			sbSQL.setLength(0);
			sbSQL.append(" select  subj                                                 \n")
				 .append(" from    tz_propose                                           \n")
				 .append(" where   subj        = " + SQLString.Format(v_subj   ) + "    \n")
				 .append(" and     year        = " + SQLString.Format(v_year   ) + "    \n")
				 .append(" and     subjseq     = " + SQLString.Format(v_subjseq) + "    \n")
				 .append(" and     userid      = " + SQLString.Format(v_user_id) + "    \n");

			ls      = connMgr.executeQuery(sbSQL.toString());

			if("RC".equals(box.getString("p_isonoff"))) {	// 독서 통신 교육일 때 수강신청한 교재정보 넣어줌
				int v_bookcnt = box.getInt("p_bookcnt");
				for(int i=1; i <= v_bookcnt; i++) {
					int v_bookcode= box.getInt("p_" + i + "_radio");
					// INSERT tz_proposesubj table
					sbSQL.setLength(0);
					sbSQL.append(" insert into tz_proposebook                                   \n")
						 .append(" (                                                            \n")
						 .append("         subj                                                 \n")
						 .append("     ,   year                                                 \n")
						 .append("     ,   subjseq                                              \n")
						 .append("     ,   userid                                               \n")
						 .append("     ,   month                                                \n")
						 .append("     ,   bookcode                                             \n")
						 .append("     ,   status                                               \n")
						 .append(" ) values (                                                   \n")
						 .append("         ?                                                    \n")
						 .append("     ,   ?                                                    \n")
						 .append("     ,   ?                                                    \n")
						 .append("     ,   ?                                                    \n")
						 .append("     ,   ?                                                    \n")
						 .append("     ,   ?                                                    \n")
						 .append("     ,   ?                                                    \n")
						 .append(" )                                                            \n");

					pstmt   = connMgr.prepareStatement(sbSQL.toString());

					pstmt.setString(1, v_subj);
					pstmt.setString(2, v_year);
					pstmt.setString(3, v_subjseq);
					pstmt.setString(4, v_user_id);
					pstmt.setInt   (5, i);
					pstmt.setInt   (6, v_bookcode);
					pstmt.setString(7, "B"); // 상태값  B:미승인(사용자가 수강신청시), W:대기(운영자가 일괄처리시)
					isOk = pstmt.executeUpdate();

					if ( pstmt != null ) pstmt.close();
					
				
					
				}
				//System.out.println("수강신청시 sql===> "+sbSQL.toString());
				
				
			}
			
			if("J".equals(v_jit)) {	// JIT일 경우에는 수강신청 시 바로 승인 처리까지 되어야 함
				v_chkfinal = "Y";
				// INSERT Tz_Propose Table
				sbSQL.setLength(0);
				sbSQL.append(" insert into tz_propose                                      \n")
					.append(" (                                                            \n")
					.append("         subj                                                 \n")
					.append("     ,   year                                                 \n")
					.append("     ,   subjseq                                              \n")
					.append("     ,   userid                                               \n")
					.append("     ,   comp                                                 \n")
					.append("     ,   jik                                                  \n")
					.append("     ,   appdate                                              \n")
					.append("     ,   chkfirst                                             \n")
					.append("     ,   chkfinal                                             \n")
					.append("     ,   luserid                                              \n")
					.append("     ,   ldate                                                \n")
					.append("     ,   lec_sel_no                                                \n")
					.append("     ,   is_attend                                                \n")
					.append("     ,   is_attend_dt                                                \n")
					.append("     ,   gubun                                                \n")
					.append(" ) values (                                                   \n")
					.append("         ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   'Y'                                                  \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   ?		           									   \n")
					.append("     ,   ?		         									   \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   ?		           \n")
					.append(" )                                                            \n");

				pstmt   = connMgr.prepareStatement(sbSQL.toString());

				pstmt.setString(1, v_subj       );
				pstmt.setString(2, v_year       );
				pstmt.setString(3, v_subjseq    );
				pstmt.setString(4, v_user_id    );
				pstmt.setString(5, v_comp       );
				pstmt.setString(6, v_jik        );
				pstmt.setString(7, v_chkfinal   );
				pstmt.setString(8, v_user_id    );
				pstmt.setString(9, v_lec_sel_no    );
				pstmt.setString(10, v_is_attend    );
				pstmt.setString(11, box.getString("p_pay_sel"));

				isOk    = pstmt.executeUpdate();
				
				if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
				
				
			

				if(isOk > 0) {
					Hashtable insertData = new Hashtable();
                    
                    insertData.put("connMgr"    , connMgr       );
                    insertData.put("subj"       , v_subj        );
                    insertData.put("year"       , v_year        );
                    insertData.put("subjseq"    , v_subjseq     );
                    insertData.put("userid"     , v_user_id      );
                    insertData.put("isdinsert"  , "N"           );
                    insertData.put("chkfirst"   , ""            );
                    insertData.put("chkfinal"   , "Y"   );
                    insertData.put("box"        , box           );
                    
                    ProposeBean propBean = new ProposeBean();
                    isOk = propBean.insertStudent(insertData);
				}
			} else {
				if(v_proposetype.equals("Y")){
					v_chkfinal = "Y";
					// INSERT Tz_Propose Table
					sbSQL.setLength(0);
					sbSQL.append(" insert into tz_propose                                      \n")
						.append(" (                                                            \n")
						.append("         subj                                                 \n")
						.append("     ,   year                                                 \n")
						.append("     ,   subjseq                                              \n")
						.append("     ,   userid                                               \n")
						.append("     ,   comp                                                 \n")
						.append("     ,   jik                                                  \n")
						.append("     ,   appdate                                              \n")
						.append("     ,   chkfirst                                             \n")
						.append("     ,   chkfinal                                             \n")
						.append("     ,   luserid                                              \n")
						.append("     ,   ldate                                                \n")
						.append("     ,   lec_sel_no                                                \n")
						.append("     ,   is_attend                                                \n")
						.append("     ,   is_attend_dt                                                \n")
						.append("     ,   gubun                                                \n")
						.append(" ) values (                                                   \n")
						.append("         ?                                                    \n")
						.append("     ,   ?                                                    \n")
						.append("     ,   ?                                                    \n")
						.append("     ,   ?                                                    \n")
						.append("     ,   ?                                                    \n")
						.append("     ,   ?                                                    \n")
						.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
						.append("     ,   'Y'                                                  \n")
						.append("     ,   ?                                                    \n")
						.append("     ,   ?                                                    \n")
						.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
						.append("     ,   ?		                                               \n")
						.append("     ,   ?		                                               \n")
						.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
						.append("     ,   ?		                                               \n")
						.append(" )                                                            \n");

					pstmt   = connMgr.prepareStatement(sbSQL.toString());

					pstmt.setString(1, v_subj       );
					pstmt.setString(2, v_year       );
					pstmt.setString(3, v_subjseq    );
					pstmt.setString(4, v_user_id    );
					pstmt.setString(5, v_comp       );
					pstmt.setString(6, v_jik        );
					pstmt.setString(7, v_chkfinal   );
					pstmt.setString(8, v_user_id    );
					pstmt.setString(9, v_lec_sel_no    );
					pstmt.setString(10, v_is_attend    );
					pstmt.setString(11, box.getString("p_pay_sel"));
					isOk    = pstmt.executeUpdate();

					if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }

					if(isOk > 0) {
						Hashtable insertData = new Hashtable();
	                    
	                    insertData.put("connMgr"    , connMgr       );
	                    insertData.put("subj"       , v_subj        );
	                    insertData.put("year"       , v_year        );
	                    insertData.put("subjseq"    , v_subjseq     );
	                    insertData.put("userid"     , v_user_id      );
	                    insertData.put("isdinsert"  , "N"           );
	                    insertData.put("chkfirst"   , ""            );
	                    insertData.put("chkfinal"   , "Y"   );
	                    insertData.put("box"        , box           );
	                    
	                    ProposeBean propBean = new ProposeBean();
	                    isOk = propBean.insertStudent(insertData);
					}
				}
				else{
					//	INSERT Tz_Propose Table
					sbSQL.setLength(0);
					sbSQL.append(" insert into tz_propose                                      \n")
					.append(" (                                                            \n")
					.append("         subj                                                 \n")
					.append("     ,   year                                                 \n")
					.append("     ,   subjseq                                              \n")
					.append("     ,   userid                                               \n")
					.append("     ,   comp                                                 \n")
					.append("     ,   jik                                                  \n")
					.append("     ,   appdate                                              \n")
					.append("     ,   chkfirst                                             \n")
					.append("     ,   chkfinal                                             \n")
					.append("     ,   luserid                                              \n")
					.append("     ,   ldate                                                \n")
					.append("     ,   lec_sel_no                                                \n")
					.append("     ,   is_attend                                                \n")
					.append("     ,   is_attend_dt                                                \n")
					.append("     ,   gubun                                                \n")
					.append(" ) values (                                                   \n")
					.append("         ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   'Y'                                                  \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   ?                                                    \n")
					.append(" )                                                            \n");
					
					pstmt   = connMgr.prepareStatement(sbSQL.toString());
					
					pstmt.setString(1, v_subj       );
					pstmt.setString(2, v_year       );
					pstmt.setString(3, v_subjseq    );
					pstmt.setString(4, v_user_id    );
					pstmt.setString(5, v_comp       );
					pstmt.setString(6, v_jik        );
					pstmt.setString(7, v_chkfinal   );
					pstmt.setString(8, v_user_id    );
					pstmt.setString(9, v_lec_sel_no    );
					pstmt.setString(10, v_is_attend    );
					pstmt.setString(11, box.getString("p_pay_sel"));
					isOk    = pstmt.executeUpdate();
					
					if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
				}
			}
			if("Y".equals(CodeAdminBean.getIsScorm(v_subj))) {
            	try {
                     // SCORM2004 : 사용자별 SeqInfo 객체 생성
            		ScormCourseBean scb = new ScormCourseBean();
                	String [] emp_id = { v_user_id };
 	                boolean result = scb.processTreeObjectForUsers(v_subj, v_year, v_subjseq, emp_id );
                 } catch( IOException ioe ) {
                     isOk  = -3;
                 } catch( Exception e ) {
                     isOk  = -3;
                 }
            }
			if(isOk > 0) {
				// 메일 발송
				if ( GetCodenm.chkIsSubj(connMgr,box.getString("p_subj")).equals("S")){
//		            AutoMailBean        bean2       = new AutoMailBean();
//                    bean2.EnterSubjSendMail(box);
                    
                  //사용자가 수강신청시 자동메일 발송(승인대기)
    				//SendRegisterCoursesEMail mail=new SendRegisterCoursesEMailImplBean();
    				
    				//mail.sendRegisterCoursesMail(box);
            	}                    
			}
			
        } else {
			sbSQL.setLength(0);
			sbSQL.append(" select subj, year, subjseq, isonoff                                               \n");        	
			sbSQL.append("from vz_scsubjseq                                  	 	\n");
			sbSQL.append("where course = " + SQLString.Format(v_subj) + "     		\n");
			sbSQL.append("and cyear = " + SQLString.Format(v_year) + "        		\n");
			sbSQL.append("and courseseq = " + SQLString.Format(v_subjseq) + " 		\n");
        	
        	ls2 = connMgr.executeQuery(sbSQL.toString());
        		
        	while(ls2.next()) {
        		v_subj = ls2.getString("subj");
        		v_year = ls2.getString("year");
        		v_subjseq = ls2.getString("subjseq");
        		
        		if("RC".equals(ls2.getString("isonoff"))) { // 독서 통신 교육일 때 수강신청한 교재정보 넣어줌
            		int v_bookcnt = box.getInt("p_bookcnt");
                	for(int i=1; i <= v_bookcnt; i++) {
                		int v_bookcode= box.getInt("p_" + i + "_radio");
                		// INSERT tz_proposesubj table
                    	sbSQL.setLength(0);
                        sbSQL.append(" INSERT INTO TZ_PROPOSEBOOK                                   \n")
                             .append(" (                                                            \n")
                             .append("         subj                                                 \n")
                             .append("     ,   year                                                 \n")
                             .append("     ,   subjseq                                              \n")
                             .append("     ,   userid                                               \n")
                             .append("     ,   month                                                \n")
                             .append("     ,   bookcode                                             \n")
                             .append(" ) VALUES (                                                   \n")
                             .append("         ?                                                    \n")
                             .append("     ,   ?                                                    \n")
                             .append("     ,   ?                                                    \n")
                             .append("     ,   ?                                                    \n")
                             .append("     ,   ?                                                    \n")
                             .append("     ,   ?                                                    \n")
                             .append(" )                                                            \n");
                             
                        pstmt   = connMgr.prepareStatement(sbSQL.toString());
                        
                        pstmt.setString(1, v_subj);
                        pstmt.setString(2, v_year);
                        pstmt.setString(3, v_subjseq);
                        pstmt.setString(4, v_user_id);
                        pstmt.setInt(5, i);
                        pstmt.setInt(6, v_bookcode);
                        isOk = pstmt.executeUpdate();
                        
                        if ( pstmt != null ) pstmt.close(); 
                	}
        		}
        		
        	   // INSERT Tz_Propose Table
        	   sbSQL.setLength(0);
               sbSQL.append(" INSERT INTO Tz_Propose                                       \n")
                    .append(" (                                                            \n")
                    .append("         subj                                                 \n")
                    .append("     ,   year                                                 \n")
                    .append("     ,   subjseq                                              \n")
                    .append("     ,   userid                                               \n")
                    .append("     ,   comp                                                 \n")
                    .append("     ,   jik                                                  \n")
                    .append("     ,   appdate                                              \n")
                    .append("     ,   chkfirst                                             \n")
                    .append("     ,   chkfinal                                             \n")
                    .append("     ,   luserid                                              \n")
                    .append("     ,   ldate                                                \n")
                    .append("     ,   lec_sel_no                                                \n")
                    .append("     ,   is_attend                                                \n")
                    .append("     ,   is_attend_dt                                                \n")
                    .append("     ,   gubun                                                \n")
                    .append(" ) VALUES (                                                   \n")
                    .append("         ?                                                    \n")
                    .append("     ,   ?                                                    \n")
                    .append("     ,   ?                                                    \n")
                    .append("     ,   ?                                                    \n")
                    .append("     ,   ?                                                    \n")
                    .append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   'Y'                                                  \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   ?                                                    \n")
					.append("     ,   to_char(sysdate,'yyyymmddhh24miss')		           \n")
					.append("     ,   ?		           \n")
                    .append(" )                                                            \n");
                    
               pstmt   = connMgr.prepareStatement(sbSQL.toString());
               
               pstmt.setString(1, v_subj       );
               pstmt.setString(2, v_year       );
               pstmt.setString(3, v_subjseq    );
               pstmt.setString(4, v_user_id    );
               pstmt.setString(5, v_comp       );
               pstmt.setString(6, v_jik        );
               pstmt.setString(7, v_chkfinal   );
               pstmt.setString(8, v_user_id    );
               pstmt.setString(9, v_lec_sel_no    );
			   pstmt.setString(10, v_is_attend    );
			   pstmt.setString(11, box.getString("p_pay_sel"));
               isOk    = pstmt.executeUpdate();
               if ( pstmt != null ) { 
                   try { 
                       pstmt.close();  
                   } catch ( Exception e ) { } 
               }
               
        	}
        }
		
        //주문번호를 신청 테이블에 넣는다.
		if(isOk > 0){
			PreparedStatement   pstmt1               = null;
			String sql =" update tz_propose set order_id = ? ";
				   sql+=" where subj = ? ";
				   sql+=" and year = ? ";
				   sql+=" and userid = ? ";
				   sql+=" and subjseq = ? ";


			       pstmt1   = connMgr.prepareStatement(sql);   
			       int idx = 1;
			       pstmt1.setString(idx++,box.getString("p_oid"));//주문번호
			       pstmt1.setString(idx++,v_subj);//강좌코드
			       pstmt1.setString(idx++,v_year);//회원아이디
			       pstmt1.setString(idx++,v_user_id);//회원아이디
			       pstmt1.setString(idx++,v_subjseq);//차수
			       isOk = pstmt1.executeUpdate();
			       
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
			if ( ls2 != null )  { try { ls2.close(); } catch ( Exception e1 ) { } }       
			
			if ( pstmt != null ) { 
				try { 
					pstmt.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}

		return isOk;        
	}
	/**
	수강신청 제약 체크 (별도 사용)
	@param box      receive from the form object and session
	@return int
	*/
	public int checkSubjectEduPropose(RequestBox box) throws Exception { 
		DBConnectionManager connMgr             = null;
		StringBuffer        sbSQL               = new StringBuffer("");

		String              v_subj              = box.getString("p_subj"        );
		String              v_year              = box.getString("p_year"        );
		String              v_subjseq           = box.getString("p_subjseq"     );
		String              v_user_id           = box.getSession("userid"       );
		String              v_comp              = box.getSession("comp"         );

		int                 v_jeyak             = 0;    // 제약조건 결과값

		try { 
			connMgr         = new DBConnectionManager();
			v_jeyak         = jeyakCheck(connMgr, v_subj, v_year, v_subjseq, v_user_id, v_comp);

		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e ) { } }
		}

		return v_jeyak;        
	}
	/**
	수강신청 제약 체크 (별도 사용)
	pg사 결제시 session값 없어 별도처리
	@param box      receive from the form object and session
	@return int
	*/
	public int checkSubjectEduProposePg(RequestBox box) throws Exception { 
		DBConnectionManager connMgr             = null;
		StringBuffer        sbSQL               = new StringBuffer("");

		String              v_subj              = box.getString("p_subj"        );
		String              v_year              = box.getString("p_year"        );
		String              v_subjseq           = box.getString("p_subjseq"     );
		String              v_user_id           = box.getString("p_userid"       );
		String              v_comp              = box.getString("p_comp"         );

		int                 v_jeyak             = 0;    // 제약조건 결과값

		try { 
			connMgr         = new DBConnectionManager();
			v_jeyak         = jeyakCheck(connMgr, v_subj, v_year, v_subjseq, v_user_id, v_comp);

		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e ) { } }
		}

		return v_jeyak;        
	}

	/**
	연간 교육 일정 리스트
	@param box      receive from the form object and session
	@return ArrayList
	*/
	public ArrayList selectEducationYearlyList(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet ls1             = null;
		ListSet ls2             = null;
		ArrayList list          = null;
		ArrayList list2          = null;
		String sql1             = "";
		//        String sql2             = "";
//		ProposeCourseData data1 = null;
		DataBox dbox= null;
		DataBox dbox2= null;
		//        ProposeCourseData data2 = null;
		String  v_gyear         = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );
		//        String  v_user_id       = box.getSession("userid");

		// 개인의 회사 기준으로 과정 리스트
		String  v_comp      = box.getSession("comp");

		// 사이트의 GRCODE 로 과정 리스트
		String v_grcode = box.getSession("tem_grcode");

		String  v_select        = box.getString("p_select");
		String  v_proposetype   = "";
		String  v_subj          = "";
		String  v_year          = "";
		String  v_subjseq       = "";
		String  v_subjseqgr     = "";
		int     v_propstart     =  0;
		int     v_propend       =  0;
		int     v_studentlimit  =  0;
		int     v_stucnt        =  0;

		try { 
			connMgr = new DBConnectionManager();
			list = new ArrayList();
			list2 = new ArrayList();

//			sql1 = "select scupperclass, course, cyear, courseseq, coursenm, subj, year, subjseq, subjnm, isonoff,   \n";
//			sql1 += "       edustart, eduend, subjseqgr, proposetype, studentlimit, propstart, propend,               \n";
//			sql1 += "       (select count(subj) from TZ_PROPOSE where subj=A.subj and year=A.year and subjseq=A.subjseq) stucnt \n";
//			sql1 += "from vz_scsubjseq A \n";
//			sql1 += " where 1=1 \n"; //gyear=" +SQLString.Format(v_gyear);
//			// 개인기준인지
//			sql1 += "  and grcode in (select grcode from TZ_GRCOMP where comp =  " + StringManager.makeSQL(v_comp) + ") \n";
//			//sql1 += "    and (select comp from tz_grseq where grcode  =a.grcode and gyear = a.gyear and grseq = a.grseq) = " + SQLString.Format(v_comp);
//			//            sql1 += "    and a.grcode = " +SQLString.Format(v_grcode);
//
//			sql1 += " and isuse = 'Y'       \n";
//			sql1 += " and subjvisible = 'Y' \n";
//			sql1 += " and seqvisible= 'Y'   \n";
//			sql1 += " AND a.edustart is not null \n";
//			sql1 += " and a.eduend is not null   \n";
//			sql1 += " and (substr(edustart,1,4) = " + SQLString.Format(v_gyear) + " or substr(eduend, 1,4) = " + SQLString.Format(v_gyear) + " ) \n";
//			
//			/* // 화면 출력 위한 조건제한, 테스트 후 주석해제
//			sql1 += " and nvl(edustart, '') != '' \n";
//			sql1 += " and nvl(eduend, '') != ''   \n";
//			*/
//			
//			//            sql1 += " and len(replace(edustart,chr(32),'')) > 0        ";
//			if ( v_select.equals("ON") || v_select.equals("OFF")  || v_select.equals("RC") ) { 
//				sql1 += " and A.isonoff = " +SQLString.Format(v_select);
//			}
//			/*---------------------------------- 코스 무시 -----------------------------------------*/
//			sql1 += " order by scsubjnm, edustart, eduend \n";
			
			sql1 = "select isonoff, subj, subjnm													\n"
				+ "from vz_scsubjseq A													\n" 
				+ "where 1=1															\n" 
				+ "and grcode in (select grcode from TZ_GRCOMP where comp =  " + StringManager.makeSQL(v_comp) + ") \n"
				+ "and isuse = 'Y'														\n"       
				+ "and subjvisible = 'Y'												\n" 
				+ "and seqvisible= 'Y'													\n"
				+ "and edustart is not null 											\n"
				+ "and eduend is not null												\n"
				+ "and (substr(edustart,1,4) = " + SQLString.Format(v_gyear) + " or substr(eduend, 1,4) = " + SQLString.Format(v_gyear) + " )	\n"
		        + "and (GRCODE <> 'N000001' or GYEAR <> '2009' or GRSEQ <> '0031')		\n"
		        + "and (GRCODE <> 'N000007' or GYEAR <> '2009' or GRSEQ <> '0001')		\n";
			
				if ( v_select.equals("ON") || v_select.equals("OFF")  || v_select.equals("RC") ) { 
					sql1 += " and A.isonoff = " +SQLString.Format(v_select);
				}
					
				sql1+= "group by isonoff, subj, subjnm												\n";
		
			ls1 = connMgr.executeQuery(sql1);

			while ( ls1.next() ) { 

				dbox = ls1.getDataBox();
				
				sql1 = "select subj, year, subjseq, edustart, eduend		\n"
					+ "from vz_scsubjseq A											\n" 
					+ "where 1=1													\n" 
					+ "and grcode in (select grcode from TZ_GRCOMP where comp =  " + StringManager.makeSQL(v_comp) + ") \n"
					+ "and isuse = 'Y'														\n"       
					+ "and subjvisible = 'Y'												\n" 
					+ "and seqvisible= 'Y'													\n"   
					+ "and edustart is not null 											\n"
					+ "and eduend is not null												\n"
					+ "and (substr(edustart,1,4) = " + SQLString.Format(v_gyear) + " or substr(eduend, 1,4) = " + SQLString.Format(v_gyear) + " )	\n" 
					+ "and subj =  " + StringManager.makeSQL(ls1.getString("subj")) + "		\n"
				    + "and (GRCODE <> 'N000001' or GYEAR <> '2009' or GRSEQ <> '0031')		\n"
				    + "and (GRCODE <> 'N000007' or GYEAR <> '2009' or GRSEQ <> '0001')		\n";
				
				if ( v_gyear.equals("2009") && v_grcode.equals("")) { 
				sql1 += " and A.grseq not in ('0016','0017','0018','0019','0020','0021','0022','0023')";
				}					
				sql1 += "order by subj, year, subjseq                                         \n";
				//System.out.println("sql1=\n"+sql1);		
				ls2 = connMgr.executeQuery(sql1);
				while ( ls2.next() ) { 
					dbox2 = ls2.getDataBox();
					list2.add(dbox2);
				}
				dbox.put("d_periodList", list2);
				list.add(dbox);
				list2 = new ArrayList();
				if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
			if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return list;
	}
	
	/**
    월간 교육 일정 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectEducationMonthlyList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1             = null;
        ArrayList list          = null;
        String sql1             = "";
        ProposeCourseData data1 = null;
        String  v_gyear         = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );
        String  v_selmonth      = box.getStringDefault("p_month",FormatDate.getDate("MM") );

        String  v_select        = box.getStringDefault("p_select", "TOTAL");
        String  v_condition     = v_gyear + v_selmonth;
        DataBox dbox = null;
        
        String ss_comp = box.getSession("comp");
        
//        String v_placejh = box.getStringDefault("p_placejh", "ALL");
        
        StringBuffer        sbSQL       = new StringBuffer("");
        int v_cnt = 0;
        
//        String v_grcode = box.getSession("tem_grcode");
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

//            sbSQL.append("select count(comp) cnt, comp									\n")
//		       	 .append("from tz_grcomp												\n")
//		       	 .append("where comp = " + StringManager.makeSQL(ss_comp) + "           \n")
//		       	 .append("group by comp													\n");
//		       ls1 = connMgr.executeQuery(sbSQL.toString());       
//		       if(ls1.next()) {
//		       	v_cnt = ls1.getInt("cnt");
//		       }
//		
//		       if ( ls1 != null ) { 
//		           try { 
//		               ls1.close();  
//		           } catch ( Exception e ) { } 
//		       }
            
//            sql1 = "select distinct x.subj, x.subjnm, x.subjseq, x.edustart, x.eduend, x.placejh, x.year, x.isonoff				\n"
//            	+ "      , day1, day2, day3, day4, day5, day6, day7, day8, day9, day10											\n"
//            	+ "      , day11, day12, day13, day14, day15, day16, day17, day18, day19, day20									\n"
//            	+ "      , day21, day22, day23, day24, day25, day26, day27, day28, day29, day30, day31							\n"
//            	+ "from (																										\n"
//            	+ "	select a.subj, a.subjnm, a.subjseq, a.edustart, a.eduend, b.placejh, a.year, a.isonoff						\n"
//            	+ "			, case when '01' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day1	\n"
//            	+ "			, case when '02' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day2	\n"
//            	+ "			, case when '03' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day3	\n"
//            	+ "			, case when '04' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day4	\n"
//            	+ "			, case when '05' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day5	\n"
//            	+ "			, case when '06' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day6	\n"
//            	+ "			, case when '07' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day7	\n"
//            	+ "			, case when '08' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day8	\n"
//            	+ "			, case when '09' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day9	\n"
//            	+ "			, case when '10' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day10	\n"
//            	+ "			, case when '11' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day11	\n"
//            	+ "			, case when '12' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day12	\n"
//            	+ "			, case when '13' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day13	\n"
//            	+ "			, case when '14' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day14	\n"
//            	+ "			, case when '15' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day15	\n"
//            	+ "			, case when '16' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day16	\n"
//            	+ "			, case when '17' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day17	\n"
//            	+ "			, case when '18' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day18	\n"
//            	+ "			, case when '19' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day19	\n"
//            	+ "			, case when '20' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day20	\n"
//            	+ "			, case when '21' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day21	\n"
//            	+ "			, case when '22' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day22	\n"
//            	+ "			, case when '23' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day23	\n"
//            	+ "			, case when '24' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day24	\n"
//            	+ "			, case when '25' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day25	\n"
//            	+ "			, case when '26' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day26	\n"
//            	+ "			, case when '27' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day27	\n"
//            	+ "			, case when '28' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day28	\n"
//            	+ "			, case when '29' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day29	\n"
//            	+ "			, case when '30' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day30	\n"
//            	+ "			, case when '31' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day31	\n"
//            	+ "		from vz_scsubjseq a inner join tz_subj b	\n"
//            	+ "     on a.subj = b.subj                          \n"
//            	+ "     left outer join tz_grcomp c					\n"
//            	+ "     on a.grcode = c.grcode						\n"
//            	+ "		where a.isuse = 'Y'							\n"
//            	+ "		and a.subjvisible = 'Y'						\n"
//            	+ "		and a.seqvisible = 'Y'						\n"
//            	+ "		and length(a.edustart) > 7					\n"
//            	+ "		and length(a.eduend) > 7					\n";
//            		
////            	+ "     and c.comp = " + SQLString.Format(ss_comp) + " 			\n"
//	             if(!"".equals(v_grcode)) {
//	         		sql1 += " and a.grcode = " + SQLString.Format(v_grcode);
//	         	} else {
//	             	 if(v_cnt > 0) {
//	             		sql1+= "and a.grcode in (select grcode from TZ_GRCOMP where comp =  " + StringManager.makeSQL(ss_comp) + ")";
//	                 } else {
//	                	 sql1 +=" and a.grcode        = " + SQLString.Format("N000001") + "                    \n";
//	                 }
//	         	}
//            	sql1+= "		and a.gyear = " +SQLString.Format(v_gyear) + "												\n"
//            	+ "		and " +SQLString.Format(v_condition) + " between SUBSTR(edustart,1,6) and SUBSTR(eduend,1,6)		\n";
//            	if(!"TOTAL".equals(v_select)) {
//            		sql1+= " 	and b.isonoff = " +SQLString.Format(v_select) + "											\n";
//            	}
//            	sql1+= ") x																								    \n"
//                + "where 1 = 1                                                                                              \n";
//            	sql1 += "order by x.edustart, x.eduend, subjnm, subjseq                                                                                  \n";
		       
		       sql1 = "select distinct x.subj, x.subjnm, x.subjseq, x.edustart, x.eduend, x.year, x.isonoff						\n"
	            	+ "      , day1, day2, day3, day4, day5, day6, day7, day8, day9, day10											\n"
	            	+ "      , day11, day12, day13, day14, day15, day16, day17, day18, day19, day20									\n"
	            	+ "      , day21, day22, day23, day24, day25, day26, day27, day28, day29, day30, day31							\n"
	            	+ "from (																										\n"
	            	+ "	select a.subj, a.subjnm, a.subjseq, a.edustart, a.eduend, a.year, b.isonoff									\n"
//	            	+ "			, case when '01' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day1	\n"
//	            	+ "			, case when '02' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day2	\n"
//	            	+ "			, case when '03' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day3	\n"
//	            	+ "			, case when '04' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day4	\n"
//	            	+ "			, case when '05' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day5	\n"
//	            	+ "			, case when '06' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day6	\n"
//	            	+ "			, case when '07' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day7	\n"
//	            	+ "			, case when '08' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day8	\n"
//	            	+ "			, case when '09' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day9	\n"
//	            	+ "			, case when '10' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day10	\n"
//	            	+ "			, case when '11' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day11	\n"
//	            	+ "			, case when '12' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day12	\n"
//	            	+ "			, case when '13' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day13	\n"
//	            	+ "			, case when '14' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day14	\n"
//	            	+ "			, case when '15' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day15	\n"
//	            	+ "			, case when '16' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day16	\n"
//	            	+ "			, case when '17' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day17	\n"
//	            	+ "			, case when '18' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day18	\n"
//	            	+ "			, case when '19' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day19	\n"
//	            	+ "			, case when '20' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day20	\n"
//	            	+ "			, case when '21' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day21	\n"
//	            	+ "			, case when '22' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day22	\n"
//	            	+ "			, case when '23' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day23	\n"
//	            	+ "			, case when '24' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day24	\n"
//	            	+ "			, case when '25' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day25	\n"
//	            	+ "			, case when '26' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day26	\n"
//	            	+ "			, case when '27' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day27	\n"
//	            	+ "			, case when '28' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day28	\n"
//	            	+ "			, case when '29' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day29	\n"
//	            	+ "			, case when '30' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day30	\n"
//	            	+ "			, case when '31' between SUBSTR(edustart, 7,2) and SUBSTR(eduend, 7,2) then 'Y' else 'N' end day31	\n"
	            	+ "         , case when substr(edustart,1,6) ||'01' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day1 \n"
	            	+ "         , case when substr(edustart,1,6) ||'02' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day2 \n"
	            	+ "         , case when substr(edustart,1,6) ||'03' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day3 \n"
	            	+ "         , case when substr(edustart,1,6) ||'04' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day4 \n"
	            	+ "         , case when substr(edustart,1,6) ||'05' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day5 \n"
	            	+ "         , case when substr(edustart,1,6) ||'06' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day6 \n"
	            	+ "         , case when substr(edustart,1,6) ||'07' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day7 \n"
	            	+ "         , case when substr(edustart,1,6) ||'08' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day8 \n"
	            	+ "         , case when substr(edustart,1,6) ||'09' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day9 \n"
	            	+ "         , case when substr(edustart,1,6) ||'10' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day10 \n"
	            	+ "         , case when substr(edustart,1,6) ||'11' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day11 \n"
	            	+ "         , case when substr(edustart,1,6) ||'12' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day12 \n"
	            	+ "         , case when substr(edustart,1,6) ||'13' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day13 \n"
	            	+ "         , case when substr(edustart,1,6) ||'14' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day14 \n"
	            	+ "         , case when substr(edustart,1,6) ||'15' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day15 \n"
	            	+ "         , case when substr(edustart,1,6) ||'16' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day16 \n"
	            	+ "         , case when substr(edustart,1,6) ||'17' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day17 \n"
	            	+ "         , case when substr(edustart,1,6) ||'18' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day18 \n"
	            	+ "         , case when substr(edustart,1,6) ||'19' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day19 \n"
	            	+ "         , case when substr(edustart,1,6) ||'20' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day20 \n"
	            	+ "         , case when substr(edustart,1,6) ||'21' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day21 \n"
	            	+ "         , case when substr(edustart,1,6) ||'22' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day22 \n"
	            	+ "         , case when substr(edustart,1,6) ||'23' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day23 \n"
	            	+ "         , case when substr(edustart,1,6) ||'24' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day24 \n"
	            	+ "         , case when substr(edustart,1,6) ||'25' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day25 \n"
	            	+ "         , case when substr(edustart,1,6) ||'26' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day26 \n"
	            	+ "         , case when substr(edustart,1,6) ||'27' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day27 \n"
	            	+ "         , case when substr(edustart,1,6) ||'28' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day28 \n"
	            	+ "         , case when substr(edustart,1,6) ||'29' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day29 \n"
	            	+ "         , case when substr(edustart,1,6) ||'30' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day30 \n"
	            	+ "         , case when substr(edustart,1,6) ||'31' between substr(edustart,1,8) and substr(eduend, 1,8) then 'Y' else 'N' end day31 \n"
	            	+ "		from tz_subjseq a inner join tz_subj b	\n"
	            	+ "     on a.subj = b.subj                          \n"
	            	+ "		where b.isuse = 'Y'							\n"
	            	+ "		and a.isvisible = 'Y'						\n"
	            	+ "		and a.isvisible = 'Y'						\n"
	            	+ "		and length(a.edustart) > 7					\n"
	            	+ "		and length(a.eduend) > 7					\n"
	            	+ "     and ((edustart between " +SQLString.Format(v_condition) + " || '01' and " +SQLString.Format(v_condition) + " || '31')	or ( eduend between " +SQLString.Format(v_condition) + " || '01' and " +SQLString.Format(v_condition) + " || '31' 	)) \n"
	            	+ "		and a.grcode in (select grcode from TZ_GRCOMP where comp =  " + StringManager.makeSQL(ss_comp) + ")		\n"
	            	+ "		and a.gyear = " +SQLString.Format(v_gyear) + "												\n";
	            	if(!"TOTAL".equals(v_select)) {
	            		sql1+= " 	and b.isonoff = " +SQLString.Format(v_select) + "											\n";
	            	}
	            	sql1+= ") x																								    \n"
	                + "where 1 = 1                                                                                              \n";
	            	sql1 += "order by subjnm, subjseq                                                                           \n";
	            	
            ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 

                		dbox = ls1.getDataBox();
                		list.add(dbox);
                }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
     

	/**
	월간 교육 일정 리스트
	@param box      receive from the form object and session
	@return ArrayList
	*/
	/*
	public ArrayList selectEducationMonthlyList(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet ls1             = null;
		ArrayList list          = null;
		String sql1             = "";
		//        String sql2             = "";
		ProposeCourseData data1 = null;
		//        ProposeCourseData data2 = null;
		String  v_gyear         = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );
		String  v_selmonth      = box.getStringDefault("p_month",FormatDate.getDate("MM") );
		//        String  v_user_id       = box.getSession("userid");

		// 개인의 회사 기준으로 과정 리스트
		String  v_comp      = box.getSession("comp");

		// 사이트의 GRCODE 로 과정 리스트
		String v_grcode = box.getSession("tem_grcode");

		String  v_select        = box.getString("p_select");
		String  v_proposetype   = "";
		String  v_subj          = "";
		String  v_year          = "";
		String  v_subjseq       = "";
		String  v_subjseqgr     = "";
		String  v_condition     = v_gyear + v_selmonth;
		int     v_propstart     =  0;
		int     v_propend       =  0;
		int     v_studentlimit  =  0;
		int     v_stucnt        =  0;

		try { 
			connMgr = new DBConnectionManager();
			list = new ArrayList();

			sql1 = "select scupperclass,course,cyear,courseseq,coursenm,subj,year,subjseq,subjnm,isonoff,               ";
			sql1 += "       edustart, eduend, subjseqgr, proposetype,studentlimit,propstart,propend,                     ";
			sql1 += "(select count(subj) from TZ_PROPOSE where subj=A.subj and year=A.year and subjseq=A.subjseq) stucnt ";
			sql1 += "from VZ_SCSUBJSEQ A ";
			sql1 += " where gyear=" +SQLString.Format(v_gyear);
			sql1 += " and " +SQLString.Format(v_condition) + " between substr(edustart,1,6) and substr(eduend,1,6)  ";
			// 개인기준인지
			sql1 += "  and grcode in (select grcode from TZ_GRCOMP where comp =" +StringManager.makeSQL(v_comp) + ")	\n";
			//sql1 += "    and (select comp from tz_grseq where grcode  =a.grcode and gyear = a.gyear and grseq = a.grseq) = " + SQLString.Format(v_comp);

			//            sql1 += "    and a.grcode = " +SQLString.Format(v_grcode);
			sql1 += " and isuse = 'Y' ";
			sql1 += " and subjvisible = 'Y' ";
			sql1 += " and seqvisible= 'Y' ";
			//sql1 += " and len(edustart) > 7	\n";
			//sql1 += " and len(eduend) > 7	\n";
			sql1 += " and length(edustart) > 7	\n";
			sql1 += " and length(eduend) > 7	\n";
			//            sql1 += " and len(replace(edustart,chr(32),'')) > 0        ";

			if ( v_select.equals("ON") || v_select.equals("OFF") || v_select.equals("RC")  ) { 
				sql1 += " and A.isonoff = " +SQLString.Format(v_select);
				//                sql1 += " and INSTR(A.subjnm,'[통신]') = 0 ";
			} else if ( v_select.equals("COURSE") ) { 
				sql1 += " and A.course != '000000' ";
				//                sql1 += " and INSTR(A.subjnm,'[통신]') = 0 ";
			}

			if ( v_select.equals("REP") ) { 
				sql1 += " and A.subjnm like '[통신]%'  ";
			}

			//            sql1 += " order by scupperclass,course,cyear,courseseq, subj,subjseq,edustart,eduend ";
			---------------------------------- 코스 무시 -----------------------------------------
			sql1 += " order by scsubjnm ";

			ls1 = connMgr.executeQuery(sql1);

			while ( ls1.next() ) { 
				v_subj          = ls1.getString("subj");
				v_year          = ls1.getString("year");
				v_subjseq       = ls1.getString("subjseq");
				v_subjseqgr     = ls1.getString("subjseqgr");
				v_proposetype   = ls1.getString("proposetype");
				v_studentlimit  = ls1.getInt("studentlimit");
				v_stucnt        = ls1.getInt("stucnt");
				if ( ls1.getString("propstart").equals("") ) {  
					v_propstart = 0000000000;
				} else {                                      
					v_propstart = Integer.parseInt( ls1.getString("propstart") );   
				}
				if ( ls1.getString("propend").equals("") ) {    
					v_propend   = 0000000000;
				} else {                                      
					v_propend   = Integer.parseInt( ls1.getString("propend") );     
				}

				data1 = new ProposeCourseData();
				data1.setCourse( ls1.getString("course") );
				data1.setCyear( ls1.getString("cyear") );
				data1.setCourseseq( ls1.getString("courseseq") );
				data1.setCoursenm( ls1.getString("coursenm") );
				data1.setSubj(v_subj);
				data1.setYear(v_year);
				data1.setSubjseq(v_subjseq);
				data1.setSubjseqgr(v_subjseqgr);
				data1.setSubjnm( ls1.getString("subjnm") );
				data1.setEdustart( ls1.getString("edustart") );
				data1.setEduend( ls1.getString("eduend") );
				data1.setIsonoff( ls1.getString("isonoff") );
				data1.setProposetype(v_proposetype);
				data1.setStudentlimit(v_studentlimit);
				data1.setStucnt(v_stucnt);
				list.add(data1);

				// 신청기간 전 여부
				if ( v_propstart > Integer.parseInt(FormatDate.getDate("yyyyMMddHH"))) { 
					data1.setProposestart("N");
				} else {  
					data1.setProposestart("Y");                   
				}
				// 신청마감여부
				if ( (v_stucnt < v_studentlimit) &&
					(v_propstart <= Integer.parseInt(FormatDate.getDate("yyyyMMddHH")) &&
					v_propend >= Integer.parseInt(FormatDate.getDate("yyyyMMddHH")))) { 
					data1.setProposeend("N");
				} else {  data1.setProposeend("Y");                   }
					// 수강완료여부
				}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return list;
	}*/

	/**
	코스에 해당하는 과정 리스트
	@param box      receive from the form object and session
	@return ArrayList
	*/
	public ArrayList selectCourseSubjList(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet ls1             = null;
		ListSet ls2             = null;
		ArrayList list          = null;
		String sql1             = "";
		//        String sql2             = "";
		ProposeCourseData data1 = null;
		//        ProposeCourseData data2 = null;
		//        String  v_user_id       = box.getSession("userid");
		String  v_course        = box.getString("p_course");
		String  v_cyear         = box.getString("p_cyear");
		String  v_courseseq     = box.getString("p_courseseq");
		String  v_proposetype   = "";
		String  v_subj          = "";
		String  v_year          = "";
		String  v_subjseq       = "";
		int     v_studentlimit  =  0;
		int     v_stucnt        =  0;

		try { 
			connMgr = new DBConnectionManager();
			list = new ArrayList();

			// select subj,year,subjseq,subjnm,edustart,eduend,isonoff,
			// proposetype,studentlimit,stucnt
			sql1 = "select subj,year,subjseq,subjnm,isonoff,";
			sql1 += "edustart,eduend,";
			sql1 += "proposetype,studentlimit, ";
			sql1 += "(select count(subj) from TZ_PROPOSE where subj=A.subj and year=A.year and subjseq=A.subjseq) stucnt ";
			sql1 += "from VZ_SCSUBJSEQ A ";
			sql1 += " where course=" +SQLString.Format(v_course);
			sql1 += " and cyear=" +SQLString.Format(v_cyear);
			sql1 += " and courseseq=" +SQLString.Format(v_courseseq);
			sql1 += " and isuse = 'Y' ";
			sql1 += " order by subj,subjseq,edustart,eduend ";

			ls1 = connMgr.executeQuery(sql1);

			while ( ls1.next() ) { 
				v_subj          = ls1.getString("subj");
				v_year          = ls1.getString("year");
				v_subjseq       = ls1.getString("subjseq");
				v_proposetype   = ls1.getString("proposetype");
				v_studentlimit  = ls1.getInt("studentlimit");
				v_stucnt        = ls1.getInt("stucnt");

				data1 = new ProposeCourseData();
				data1.setSubj(v_subj);
				data1.setYear(v_year);
				data1.setSubjseq(v_subjseq);
				data1.setSubjnm( ls1.getString("subjnm") );
				data1.setEdustart( ls1.getString("edustart") );
				data1.setEduend( ls1.getString("eduend") );
				data1.setIsonoff( ls1.getString("isonoff") );
				data1.setProposetype(v_proposetype);
				data1.setStudentlimit(v_studentlimit);
				data1.setStucnt(v_stucnt);
				list.add(data1);
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
			if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return list;
	}

	public int datediff(String stdt, String eddt) { 
		int returnValue = 0;
		long temp = 0;

		int year = Integer.parseInt(stdt.substring(0,4));
		int month = Integer.parseInt(stdt.substring(4,6));
		int day = Integer.parseInt(stdt.substring(6,8));

		int year1 = Integer.parseInt(eddt.substring(0,4));
		int month1 = Integer.parseInt(eddt.substring(4,6));
		int day1 = Integer.parseInt(eddt.substring(6,8));

		TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
		Calendar cal=Calendar.getInstance(tz);

		cal.set((year-1900),(month-1),day);

		Calendar cal2=Calendar.getInstance(tz);
		cal2.set((year1-1900),(month1-1),day1);

		java.util.Date temp1 = cal.getTime();

		java.util.Date temp2 = cal2.getTime();

		temp = temp2.getTime() - temp1.getTime();

		if ( (temp % 10) < 5)
		temp = temp - (temp % 10);
		else
		temp = temp + (10 - (temp % 10));

		returnValue = (int)(temp / ( 1000 * 60 * 60 * 24));

		return returnValue;
	}

	/**
	교육그룹 리스트
	@param box      receive from the form object and session
	@return ArrayList
	*/
	public ArrayList selectGrcodeList(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet ls1         = null;
		ArrayList list1     = null;
		String sql1         = "";
		SelectionData data1= null;
		//String  v_user_id   = box.getSession("userid");
		String  v_comp      = box.getSession("comp");
		v_comp              = v_comp.substring(0,4);
		//int     l           = 0;
		try { 
			connMgr = new DBConnectionManager();
			list1 = new ArrayList();

			// select  code,name
			sql1 = "select distinct a.grcode code, substr(a.grcodenm,4,50) name, a.code_order ";
			sql1 += "  from TZ_GRCODE a";
			sql1 += "  , tz_grcomp b";
			sql1 += " where a.grcode=b.grcode ";
			sql1 += "   and b.comp like " +SQLString.Format(v_comp+"%");
			sql1 += " order by a.code_order ";

			ls1 = connMgr.executeQuery(sql1);

			while ( ls1.next() ) { 
				data1 = new SelectionData();
				data1.setCode( ls1.getString("code") );
				data1.setName( ls1.getString("name") );
				list1.add(data1);
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
	임시 comp, jikup 세션부여
	@param box      receive from the form object and session
	@return int
	*/
	public int setCreduSession(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet ls1         = null;
		String  sql1         = "";
		int  result       = 0;
		String  v_userid   = box.getSession("userid");

		String  v_comp     = "";
		String  v_jik      = "";

		try { 
			connMgr = new DBConnectionManager();

			// select  code,name
			sql1 = "select comp, jikup  ";
			sql1 += "  from TZ_MEMBER    ";
			sql1 += " where userid="+SQLString.Format(v_userid);

			ls1 = connMgr.executeQuery(sql1);

			while ( ls1.next() ) { 
				v_comp = ls1.getString("comp");
				v_jik  = ls1.getString("jikup");
				box.setSession("comp",  v_comp);
				box.setSession("jikup", v_jik);
				result = 1;
			}

		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return result;
	}

	/**
	팀장정보 리턴
	@param box      receive from the form object and session
	@return ProposeCourseData
	*/
	public DataBox getSelectChief(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		DataBox dbox        = null;
		ListSet ls1         = null;
		String sql          = "";

		String  v_comp      = box.getSession("comp");

		try { 
			connMgr = new DBConnectionManager();
			// sql = "  select deptmbirth_date,deptmname,deptmjikwi from tz_comp \n";
			// sql += "  where                  \n";
			//sql = "  select email, userid, name, comp, userid as cono from tz_member where  userid in (select deptmbirth_date from tz_comp where comp = " +SQLString.Format(v_comp) + " ) \n";

			ls1 = connMgr.executeQuery(sql);

			if ( ls1.next() ) { 
				dbox = ls1.getDataBox();
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return dbox;
	}

	/**
	팀장정보 리턴
	@param box      receive from the form object and session
	@return ProposeCourseData
	*/
	public DataBox getSelectChief(String userid) throws Exception { 
		DBConnectionManager	connMgr	= null;
		DataBox dbox        = null;
		ListSet ls1         = null;
		String sql          = "";
		String  v_comp      = "";

		Hashtable outputData = new Hashtable();
		ProposeBean probean = new ProposeBean();


		try { 
			connMgr = new DBConnectionManager();

			outputData = probean.getMeberInfo(userid);
			v_comp = (String)outputData.get("comp");

			// sql = "  select deptmbirth_date,deptmname,deptmjikwi from tz_comp \n";
			// sql += "  where                  \n";
			//sql = "  select email, userid, name, comp, cono from tz_member where  userid in (select deptmbirth_date from tz_comp where comp = " +SQLString.Format(v_comp) + " ) \n";

			ls1 = connMgr.executeQuery(sql);

			if ( ls1.next() ) { 
				dbox = ls1.getDataBox();
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
				if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
				if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return dbox;
	}


	/**
	* 대분류 SELECT (ALL 제외)
	* @param box          receive from the form object and session
	* @return ArrayList   대분류 리스트
	*/
	public ArrayList getOnlyUpperClass(RequestBox box) throws Exception { 
	DBConnectionManager	connMgr	= null;
		PreparedStatement   pstmt   = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		DataBox             dbox    = null;
		String  v_usergubun   = box.getSession("usergubun");
		//String  v_isonoff     = "";
		ConfigSet conf = new ConfigSet();

		if ( (v_usergubun.equals("RH")||v_usergubun.equals("RK")) && (!v_usergubun.equals(""))) { 
			//v_isonoff = box.getStringDefault("p_isonoff", "OFF");
		} else { 
			//v_isonoff = box.getStringDefault("p_isonoff", "ON");
		}

		try { 

			String v_upperclass1 = conf.getProperty("main.cyber.upperclass1");
			String v_upperclass2 = conf.getProperty("main.cyber.upperclass2");
			String v_upperclass3 = conf.getProperty("main.cyber.upperclass3");

			String isCourse = "N";        //      코스가 있어야 하는지 여부
			connMgr = new DBConnectionManager();

			list = new ArrayList();


			sql = "select upperclass, classname";
			sql += " from tz_subjatt";
			sql += " where ";

			if ( (v_usergubun.equals("RH")||v_usergubun.equals("RK")) ) { 
				sql += " substr(upperclass,0,1) = 'R'";
			} else { 
				sql += "(upperclass = " +SQLString.Format(v_upperclass1) + " or upperclass = " +SQLString.Format(v_upperclass2) + " or upperclass = " +SQLString.Format(v_upperclass3) + ")";
			}

			sql += " and middleclass = '000'";
			sql += " and lowerclass = '000'";

			if ( isCourse.equals("N") ) {      //     코스분류도 없다
				sql += " and upperclass != 'COUR'";
			}

			sql += " order by upperclass";

			pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			ls = new ListSet(pstmt);

			// dbox = this.setAllSelectBox( ls);
			// list.add(dbox);

			while ( ls.next() ) { 
				dbox = ls.getDataBox();

				list.add(dbox);
			}
		} catch ( Exception ex ) { 
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return list;
	}

	/**
	* 대분류 SELECT (ALL 제외)
	* @param box          receive from the form object and session
	* @return ArrayList   대분류 리스트
	*/
	public ArrayList getMainUpperClass(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		PreparedStatement   pstmt   = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		DataBox             dbox    = null;
		ConfigSet conf = new ConfigSet();

		try { 
			// String isCourse = box.getStringDefault("isCourse", "N");        //      코스가 있어야 하는지 여부

			// String v_indexclass  = conf.getProperty("index.main.defaultclass");
			String v_upperclass1 = conf.getProperty("main.cyber.upperclass1");
			String v_upperclass2 = conf.getProperty("main.cyber.upperclass2");
			String v_upperclass3 = conf.getProperty("main.cyber.upperclass3");

			//            String isCourse = "N";        //      코스가 있어야 하는지 여부
			connMgr = new DBConnectionManager();

			list = new ArrayList();


			sql = "select upperclass, classname";
			sql += " from tz_subjatt";
			sql += " where ";
			sql += "(upperclass = " +SQLString.Format(v_upperclass1) + " or upperclass = " +SQLString.Format(v_upperclass2) + " or upperclass = " +SQLString.Format(v_upperclass3) + ")";
			// sql += "upperclass in(" +v_indexclass + ")";
			sql += " and middleclass = '000'";
			sql += " and lowerclass = '000'";
			sql += " order by upperclass";

			pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			ls = new ListSet(pstmt);

			// dbox = this.setAllSelectBox( ls);
			// list.add(dbox);

			while ( ls.next() ) { 
				dbox = ls.getDataBox();

				list.add(dbox);
			}
		} catch ( Exception ex ) { 
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return list;
	}



	/**
	* 구분 리스트 (온라인 / 오프라인)
	* @param box          receive from the form object and session
	* @return ArrayList   대분류 리스트
	*/
	public ArrayList getGubun(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;

		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		DataBox             dbox    = null;

		try { 
			connMgr = new DBConnectionManager();
			list = new ArrayList();

			sql  = " select code, codenm from tz_code ";
			sql += " where gubun = " +SQLString.Format(GUBUN_CODE);
			sql += " order by code desc";

			ls = connMgr.executeQuery(sql);

			while ( ls.next() ) { 
				dbox = ls.getDataBox();
				list.add(dbox);
			}
		} catch ( Exception ex ) { 
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return list;
	}

	
	/**
	* 제약조건 체크 
	* @param connMgr       DBConnectionManager
	* @param p_subj        과정코드
	* @param p_year        과정년도
	* @param p_subjseq     과정기수
	* @param p_userid      유저아이디
	* @param p_comp        회사코드
	* @param p_tem_grcode  현재교육그룹코드
	* @return result       제약조건결과 코드
	*/
	public int jeyakCheck(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
		ListSet             ls      = null;
		int result = 0;
		//        String msg = "";
		String v_subjtype = GetCodenm.chkIsSubj(connMgr,p_subj);

		try {
			// 비계열사는 개인별로 수강신청 무조건 안됨.	-17 
			//result =jeyakCompYn(connMgr, p_subj, p_year, p_subjseq, p_userid, p_comp);
			//if ( result < 0) return result;

			// 수강신청기간 지남 -10
			result =jeyakEndPeriod(connMgr, p_subj, p_year, p_subjseq, p_userid, p_comp);
			if ( result < 0) return result;
			
			// 정원초과           - 1
			if ( v_subjtype.equals("S")){
				result =jeyakStudentlimit(connMgr, p_subj, p_year, p_subjseq, p_userid, p_comp);
				//if ( result < 0) return result;
			}
			
			// 이미수료한과정이면 안됨	-14
			if ( v_subjtype.equals("S")){
				result =jeyakIsgraduated(connMgr, p_subj, p_year, p_subjseq, p_userid, p_comp);
				if ( result < 0) return result;
			}
			
			// 다른기수를 학습하고 있는 과정이면 안됨	-15
			if ( v_subjtype.equals("S")){
				result =jeyakStudentYn(connMgr, p_subj, p_year, p_subjseq, p_userid, p_comp);
				if ( result < 0) return result;
			}
			
			// 다른기수를 신청한 과정이면 안됨	-16 
			result =jeyakProposeYn(connMgr, p_subj, p_year, p_subjseq, p_userid, p_comp);
			if ( result < 0) return result;
			
			// 이미신청한과정  - 2
			//result =jeyakPropose(connMgr, p_subj, p_year, p_subjseq, p_userid, p_comp);
			//if ( result < 0) return result;
			
			if ( v_subjtype.equals("S")){
			result =jeyakRejectkind(connMgr, p_subj, p_year, p_subjseq, p_userid, p_comp); //수강신청데이터중에 반려데이터가있을경우
			if ( result < 0) return result;
			}
			
			// 수강신청 과정수 제한
			/*
			 수강신청시 제약 과정수
			 - 이러닝, 독서교육으로 구분됨.
			 - 제약과정수는 공통코드의 '0105'에 등록해서 사용.
			 - 이러닝은 'ON', 독서교육은 'RC'
			 - 이러닝중에서 subj_gu 이 'J','M','E' 인거는 제외.
			 - 기준은 교육시작월. 
			 - 교육시작월이 중복되면 안됨. (일반과정은 12개 초과될수 없음)
			*/
			if ( v_subjtype.equals("S")){
				result =jeyakSubjCnt(connMgr, p_subj, p_year, p_subjseq, p_userid);
				if ( result < 0) return result;
			}
			
			// 고용보험 환급 과정인데 주민등록번호가 입력되어 있지 않은지
			if ( v_subjtype.equals("S")){
				result =jeyakNotbirth_date(connMgr, p_subj, p_year, p_subjseq, p_userid);
				if ( result < 0) return result;
			}
			
			// 직무제한 과정일 경우 해당 직무인 사람만 신청가능
			//if ( GetCodenm.chkIsSubj(connMgr,p_subj).equals("S")){
			//	result =jeyakJikmu(connMgr, p_subj, p_year, p_userid);
			//	if ( result < 0) return result;
			//}
			
//			// 수료한과정  - 3 (제약조건 제거) 
//			if ( GetCodenm.chkIsSubj(connMgr,p_subj).equals("S")){
//				result =jeyakStold(connMgr, p_subj, p_year, p_subjseq, p_userid, p_comp);
//				if ( result < 0) return result;
//			}
//
//			// 마스터과정인지, 대상자인지 - 4
//			if ( GetCodenm.chkIsSubj(connMgr,p_subj).equals("S")){
//				result =jeyakMasterSubj(connMgr, p_subj, p_year, p_subjseq, p_userid, p_comp);
//				if ( result < 0) return result;
//			}
//			// 예산초과 - 5 (제약조건 제거)
//			//             result =jeyakBudget(connMgr, p_subj, p_year, p_subjseq, p_userid, p_comp);
//			//             if ( result < 0) return result;
//
//			// 삼진아웃 대상인지 -5
			if ( v_subjtype.equals("S")){			
				result =jeyakStrOut(connMgr, p_subj, p_year, p_subjseq, p_userid, p_comp);
				if ( result < 0) return result;
			}
//
//			// 동시 수강 가능 과정수 초과  - 7
//			if ( GetCodenm.chkIsSubj(connMgr,p_subj).equals("S")){
//				result =jeyakSyncPropose(connMgr, p_userid, p_subj );
//				if ( result < 0) return result;
//			}
//
//			// 현재 복습이 가능한 과정인지 7
//			if ( GetCodenm.chkIsSubj(connMgr,p_subj).equals("S")){
//				result =jeyakIsReEdu(connMgr, p_subj, p_year, p_userid);
//				if ( result < 0) return result;
//			}
		} catch ( Exception ex ) { 
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;
	}


	/**
	* 정원초과
	* @param connMgr      DBConnectionManager
	* @param p_subj       과정코드
	* @param p_year       과정년도
	* @param p_subjseq    과정기수
	* @param p_userid     유저아이디
	* @param p_comp       회사코드
	* @return result      0 : 정상, 1 : 정원초과
	*/
	public int jeyakStudentlimit(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
		ListSet             ls      = null;
		String sql  = "";
		int result = 0;

		try { 
			sql = " select count(*) cnt     ";
			sql += " from   tz_subjseq a   ";
			sql += " where a.subj    = " +SQLString.Format(p_subj);
			sql += "   and a.year    = " +SQLString.Format(p_year);
			sql += "   and a.subjseq = " +SQLString.Format(p_subjseq);
			sql += "   and a.studentlimit > (select count(userid) from tz_propose where subj = a.subj and subjseq = a.subjseq and year = a.year and chkfinal != 'N') ";
//			sql += "   and (case when a.studentlimit = 0 then 1000000 else a.studentlimit end) > (select count(userid) from tz_propose where subj = a.subj and subjseq = a.subjseq and year = a.year and chkfinal != 'N') ";

			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				if ( ls.getInt("cnt") == 0) result = -1;
			}
		} catch ( Exception ex ) { 
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;
	}


	/**
	* 정원초과
	* @param connMgr      DBConnectionManager
	* @param p_subj       과정코드
	* @param p_year       과정년도
	* @param p_subjseq    과정기수
	* @param p_userid     유저아이디
	* @param p_comp       회사코드
	* @return result      0 : 정상, 1 : 정원초과
	*/
	public int jeyakEndPeriod(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
		ListSet             ls      = null;
		String sql  = "";
		int result = 0;

		try { 
			if ( GetCodenm.chkIsSubj(connMgr,p_subj).equals("S")){
				sql = " select count(*) cnt     ";
				sql += " from   tz_subjseq a   ";
				sql += " where a.subj    = " +SQLString.Format(p_subj);
				sql += "   and a.year    = " +SQLString.Format(p_year);
				sql += "   and a.subjseq = " +SQLString.Format(p_subjseq);
				sql += "   and to_char(sysdate, 'yyyymmddhh24') between a.propstart and a.propend "; //a.propend
			} else {
				sql = " select count(*) cnt     ";
				sql += " from   tz_courseseq a   ";
				sql += " where a.course    = " +SQLString.Format(p_subj);
				sql += "   and a.cyear    = " +SQLString.Format(p_year);
				sql += "   and a.courseseq = " +SQLString.Format(p_subjseq);
				sql += "   and to_char(sysdate, 'yyyymmddhh24') between a.propstart and a.propend ";
			}
			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				if ( ls.getInt("cnt") == 0) result = -10;
			}
		} catch ( Exception ex ) { 
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;
	}

	/**
	* 이미신청한과정
	* @param connMgr      DBConnectionManager
	* @param p_subj       과정코드
	* @param p_year       과정년도
	* @param p_subjseq    과정기수
	* @param p_userid     유저아이디
	* @param p_comp       회사코드
	* @return result      0 : 정상, 2 : 이미신청한과정
	*/
	public int jeyakPropose(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
		ListSet             ls      = null;
		String sql  = "";
		int result = 0;

		try { 
			if ( GetCodenm.chkIsSubj(connMgr,p_subj).equals("S")){
				sql  = " select count(*) cnt            ";
				sql += " from   TZ_PROPOSE              ";
				sql += " where  subj    = " + SQLString.Format(p_subj   );
				sql += " and    year    = " + SQLString.Format(p_year   );
				sql += " and    subjseq = " + SQLString.Format(p_subjseq);
				sql += " and    userid  = " + SQLString.Format(p_userid );
				sql += " and    chkfinal != 'N'";
			} else {
				sql = "select count(*) cnt			  \n"
					+ "from tz_propose a              \n"
					+ "where exists (                 \n"
					+ "	select 'X'                    \n"
					+ "	from vz_scsubjseq  b          \n"
					+ "	where a.subj = b.subj         \n"
					+ "	and a.year = b.year           \n"
					+ "	and a.subjseq = b.subjseq     \n"
					+ "	and course =" + SQLString.Format(p_subj   )
					+ "	and cyear =" + SQLString.Format(p_year   )
					+ "	and courseseq = " + SQLString.Format(p_subjseq)
					+ "	and userid =  " + SQLString.Format(p_userid )
					+ ")                              \n"
					+ "and a.chkfinal != 'N'          \n";
			}

			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				if ( ls.getInt("cnt") > 0) result = -2;
			}
		} catch ( Exception ex ) { 
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;
	}
	
	/**
	 * 반려된 수강정보가 있을경우
	 * @param connMgr      DBConnectionManager
	 * @param p_subj       과정코드
	 * @param p_year       과정년도
	 * @param p_subjseq    과정기수
	 * @param p_userid     유저아이디
	 * @param p_comp       회사코드
	 * @return result      0 : 정상, 2 : 이미신청한과정
	 */
	public int jeyakRejectkind(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
		ListSet             ls      = null;
		String sql  = "";
		int result = 0;
		
		try { 
			if ( GetCodenm.chkIsSubj(connMgr,p_subj).equals("S")){
				sql  = " select count(*) cnt            ";
				sql += " from   TZ_PROPOSE              ";
				sql += " where  subj    = " + SQLString.Format(p_subj   );
				sql += " and    year    = " + SQLString.Format(p_year   );
				sql += " and    subjseq = " + SQLString.Format(p_subjseq);
				sql += " and    userid  = " + SQLString.Format(p_userid );
				sql += " and    chkfinal = 'N'";
				sql += " and    CANCELKIND is not null";
				
				//System.out.println("jeyakPropose === >"+sql);
			}
			
			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				if ( ls.getInt("cnt") > 0) result = -20;
			}
		} catch ( Exception ex ) { 
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}
		
		return result;
	}


	/**
	* 재수강
	* @param connMgr      DBConnectionManager
	* @param p_subj       과정코드
	* @param p_year       과정년도
	* @param p_subjseq    과정기수
	* @param p_userid     유저아이디
	* @param p_comp       회사코드
	* @return result      0 : 정상, 1 : 정원초과
	*/
	public int jeyakIsReEdu(DBConnectionManager connMgr, String p_subj, String p_year, String p_userid) throws Exception { 
		ListSet     ls          = null;
		String      sql         = "";
		int         result      = 0 ;
		String      v_rtnvalue  = "";

		try { 
			sql += " SELECT  nvl(MAX(tsd.isgraduated), 'N')  isReview                   \n";
			sql += " FROM    tz_stold       tsd                                         \n";
			sql += "      ,  tz_subjseq     tss                                         \n";                      
			sql += " WHERE   tsd.userid         =  " + SQLString.Format(p_userid ) + "  \n";
			sql += " AND     tsd.subj           =  " + SQLString.Format(p_subj   ) + "  \n";
			sql += " AND     tsd.subj           =  tss.subj                             \n";                          
			sql += " AND     tsd.year           =  tss.year                             \n";                          
			sql += " AND     tsd.subjseq        =  tss.subjseq                          \n";                          
			sql += " AND     tsd.eduend         >= to_char(sysdate - 365, 'yyyymmdd')   \n";

			ls = connMgr.executeQuery(sql);

			if ( ls.next() ) { 
				v_rtnvalue  = ls.getString("isreview");
			}

			if ( v_rtnvalue.equals("Y") ) {
				result  = 7;
			} else {
				result  = 0; 
			}
		} catch ( Exception ex ) { 
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;
	}



	/**
	* 수료한과정
	* @param connMgr      DBConnectionManager
	* @param p_subj       과정코드
	* @param p_year       과정년도
	* @param p_subjseq    과정기수
	* @param p_userid     유저아이디
	* @param p_comp       회사코드
	* @return result      0 : 정상, 3 : 수료한과정
	*/
	public int jeyakStold(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
		ListSet             ls      = null;
		String sql  = "";
		int result = 0;

		try { 
			sql = " select nvl(MAX(isgraduated), 'N')  isgraduated ";
			sql += " from TZ_STOLD        ";
			sql += " where subj    = " +SQLString.Format(p_subj);
			//            sql += "   and year    = " +SQLString.Format(p_year);
			//            sql += "   and subjseq = " +SQLString.Format(p_subjseq);
			sql += "   and userid  = " +SQLString.Format(p_userid);


			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				if ( ls.getString("isgraduated").equals("Y") ) result = -3;
			}
		} catch ( Exception ex ) { 
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;
	}


	/**
	* 마스터과정인지, 대상자인지
	* @param connMgr      DBConnectionManager
	* @param p_subj       과정코드
	* @param p_year       과정년도
	* @param p_subjseq    과정기수
	* @param p_userid     유저아이디
	* @param p_comp       회사코드
	* @return result      0 : 정상, 4 :마스터과정인지, 대상자인지
	*/
	public int jeyakMasterSubj(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
		ListSet ls1 = null;
		ListSet ls2 = null;
		String sql1  = "";
		String sql2  = "";
		int result = 0;

		try { 
			sql1 = " select count(*) cnt        \n";
			sql1 += " from TZ_MASTERSUBJ        \n";
			sql1 += " where subjcourse = " +SQLString.Format(p_subj     ) + " \n";
			sql1 += "   and year       = " +SQLString.Format(p_year     ) + " \n";
			sql1 += "   and subjseq    = " +SQLString.Format(p_subjseq  ) + " \n";

			ls1 = connMgr.executeQuery(sql1);
			if ( ls1.next() ) { 
				// 마스터과정일경우
				if ( ls1.getInt("cnt") > 0 ) { 
					sql2 = " select count(*) cnt                     ";
					sql2 += "   from tz_edutarget a, tz_mastersubj b  ";
					sql2 += " where a.mastercd = b.mastercd           ";
					sql2 += "   and b.subjcourse    = " +SQLString.Format(p_subj);
					sql2 += "   and b.year    = " +SQLString.Format(p_year);
					sql2 += "   and b.subjseq = " +SQLString.Format(p_subjseq);
					sql2 += "   and a.userid  = " +SQLString.Format(p_userid);

					ls2 = connMgr.executeQuery(sql2);
					if ( ls2.next() ) { 
						if ( ls2.getInt("cnt") == 0) result = -4;
					}

					ls2.close();
				}
			}
		} catch ( Exception ex ) { 
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
			if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
		}

		return result;
	}


	/**
	* 예산초과
	* @param connMgr      DBConnectionManager
	* @param p_subj       과정코드
	* @param p_year       과정년도
	* @param p_subjseq    과정기수
	* @param p_userid     유저아이디
	* @param p_comp       회사코드
	* @return result      0 : 정상, 5 :예산초과
	*/
	public int jeyakBudget(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
	ListSet ls1 = null;
		ListSet ls2 = null;
		String sql1  = "";
		String sql2  = "";
		int result = 0;

		String v_propstart = "";
		String v_propend   = "";
		String v_gubun     = "";
		long   v_budget    = 0;
		long   v_totbiyong = 0;

		try { 
			// 예산설정관련정보
			sql1 = " select b.propstart, b.propend, b.budget, b.gubun                                           ";
			sql1 += "   from tz_subjseq a, tz_budget b, tz_subj c                                                ";
			sql1 += "  where a.grcode = b.grcode                                                                 ";
			sql1 += "    and a.gyear  = b.gryear                                                                 ";
			sql1 += "    and a.grseq  = b.grseq                                                                  ";
			sql1 += "    and a.subj   = c.subj																																		";
			sql1 += "    and b.gubun = (select matchcode from TZ_CLASSFYMATCH where upperclass = c.upperclass) ";
			sql1 += "    and b.isuse ='Y'                                                                        ";
			sql1 += "    and substr(to_char(sysdate,'yyyymmddhh24miss'), 1, 10) between b.propstart and b.propend                    ";
			sql1 += "    and a.subj    = " +SQLString.Format(p_subj);
			sql1 += "    and a.year    = " +SQLString.Format(p_year);
			sql1 += "    and a.subjseq = " +SQLString.Format(p_subjseq);

			ls1 = connMgr.executeQuery(sql1);
			while ( ls1.next() ) { 
				// 예산설정 돼 있는경우
				v_propstart = ls1.getString("propstart");
				v_propend   = ls1.getString("propend");
				v_gubun     = ls1.getString("gubun");
				v_budget    = ls1.getLong("budget");

				sql2 = " select  sum(c.biyong) totbiyong                                       ";
				sql2 += "   from tz_propose a, tz_member b, vz_scsubjseq c                      ";
				sql2 += "  where a.userid = b.userid                                            ";
				sql2 += "    and c.grcode in (select grcode from TZ_GRCOMP where comp like " +SQLString.Format(p_subj) + " )";
				sql2 += "    and a.subj = c.subj and a.year = c.year and a.subjseq = c.subjseq  ";
				sql2 += "    and c.scupperclass in (select upperclass from TZ_CLASSFYMATCH where matchcode=" +SQLString.Format(v_gubun) + " )";
				sql2 += "    and appdate between " +SQLString.Format(v_propstart) + " and " +SQLString.Format(v_propend);

				ls2 = connMgr.executeQuery(sql2);
				if ( ls2.next() ) { 
					v_totbiyong = ls2.getLong("totbiyong");
				}
				// 예산 <= 현재비용
				if ( v_budget <= v_totbiyong) result =-5;
			}
		} catch ( Exception ex ) { 
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
			if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
		}

		return result;
	}


	/**
	* 회사별 수강신청 제약설정
	* @param connMgr       DBConnectionManager
	* @param p_subj        과정코드
	* @param p_year        과정년도
	* @param p_subjseq     과정기수
	* @param p_userid      유저아이디
	* @param p_comp        회사코드
	* @return result       0 : 정상, 6 : 제약대상
	*/
	public int jeyakCompcondition(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
		ListSet ls1 = null;
		ListSet ls2 = null;
		ListSet ls3 = null;
		String sql1  = "";
		String sql2  = "";
		String sql3  = "";
		int result = 0;

		String v_matchcode = ""; // 현재 신청한 과정의 어학,직무 구분코드 (W:직무, L:어학)
		// tz_compcondition에 설정된 값
		int v_duty1         = 0;
		int v_language1     = 0;
		int v_allcondition1 = 0;
		int v_yearduty1     = 0;
		int v_yearlanguage1 = 0;

		// 사용자 값
		int v_duty2         = 0;
		int v_language2     = 0;
		int v_allcondition2 = 0;
		int v_yearduty2     = 0;
		int v_yearlanguage2 = 0;

		try { 


			// 신청한 과정의 어학, 직무 구분
			sql1  = " select b.matchcode from tz_subjseq a, tz_classfymatch b, tz_subj c ";
			sql1 += " where b.upperclass = c.upperclass                       ";
			sql1 += "   and a.subj    = c.subj ";
			sql1 += "    and a.subj    = " +SQLString.Format(p_subj);
			sql1 += "    and a.year    = " +SQLString.Format(p_year);
			sql1 += "    and a.subjseq = " +SQLString.Format(p_subjseq);

			ls1 = connMgr.executeQuery(sql1);
			if ( ls1.next() ) v_matchcode = ls1.getString("matchcode");
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }

			// 제약내용
			sql1  = " select duty, language, allcondition, yearduty, yearlanguage ";
			sql1 += "   from tz_compcondition                                     ";
			sql1 += " where comp = " +SQLString.Format(p_comp);

			ls1 = connMgr.executeQuery(sql1);

			if ( ls1.next() ) { 
				v_duty1         = ls1.getInt("duty");
				v_language1     = ls1.getInt("language");
				v_allcondition1 = ls1.getInt("allcondition");
				v_yearduty1     = ls1.getInt("yearduty");
				v_yearlanguage1 = ls1.getInt("yearlanguage");
				if ( v_duty1 == 0)              v_duty1 = 10000;           // 0일경우 체크를 안함
				else if ( v_language1 == 0)     v_language1 = 10000;      // 0일경우 체크를 안함
				else if ( v_allcondition1 == 0) v_allcondition1 = 10000;  // 0일경우 체크를 안함
				else if ( v_yearduty1 == 0)     v_yearduty1 = 10000;      // 0일경우 체크를 안함
				else if ( v_yearlanguage1 == 0) v_yearlanguage1 = 10000;  // 0일경우 체크를 안함

				// 현재기수에 신청한 갯수
				sql2 = " select sum(decode(c.matchcode, 'W', 1, 0)) duty,                       ";
				sql2 += "        sum(decode(c.matchcode, 'L', 1, 0)) language                    ";
				sql2 += "   from tz_propose a, tz_subjseq  b, tz_classfymatch c, tz_subj d       ";
				sql2 += "                                                       ";
				sql2 += "  where a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq   ";
				sql2 += "    and c.upperclass = d.upperclass                                   ";
				sql2 += "    and b.subj = d.subj ";
				sql2 += "    and b.grcode+b.gyear+b.grseq = (select grcode+gyear+grseq       ";
				sql2 += "          from tz_subjseq                                             ";
				sql2 += "         where subj    = " +SQLString.Format(p_subj);
				sql2 += "           and year    = " +SQLString.Format(p_year); 
				sql2 += "           and subjseq = " +SQLString.Format(p_subjseq) + " )          ";
				sql2 += "    and a.cancelkind is null                                            ";
				sql2 += "    and a.userid = " +SQLString.Format(p_userid);

				ls2 = connMgr.executeQuery(sql2);
				if ( ls2.next() ) { 
					v_duty2         = ls2.getInt("duty");
					v_language2     = ls2.getInt("language");
				} else { 
					v_duty2         = 0;
					v_language2     = 0;
				}
				v_allcondition2     = v_duty2 + v_language2;

				if ( v_allcondition1 <= v_allcondition2 ) {                               // 총신청갯수가 같은경우
					result = -6;
					return result;
				} else if ( v_matchcode.equals("W") && (v_duty1 <= v_duty2) ) {          // 직무신청갯수가 같은경우
					result = -6;
					return result;
				} else if ( v_matchcode.equals("L") && (v_language1 <= v_language2)) {   // 어학신청갯수가 같은경우
					result = -6;
					return result;
				}

				// 현재년도에 신청한 갯수
				sql3 = " select sum(decode(c.matchcode, 'W', 1, 0)) yearduty,                    ";
				sql3 += "        sum(decode(c.matchcode, 'L', 1, 0)) yearlanguage                 ";
				sql3 += "   from tz_propose a,                                                    ";
				sql3 += "       (select grcode, gyear, grseq,scupperclass, subj, year, subjseq    ";
				sql3 += "          from vz_scsubjseq                                              ";
				sql3 += "         where subj    = " +SQLString.Format(p_subj);
				sql3 += "           and year    = " +SQLString.Format(p_year) +   " ) b,           ";
				sql3 += "        tz_classfymatch c                                                ";
				sql3 += "  where a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq    ";
				sql3 += "    and b.scupperclass = c.upperclass                                    ";
				sql3 += "    and a.cancelkind is null                                             ";
				sql3 += "    and a.userid =  " +SQLString.Format(p_userid);

				ls3 = connMgr.executeQuery(sql3);
				if ( ls3.next() ) { 
					v_yearduty2         = ls3.getInt("yearduty");
					v_yearlanguage2     = ls3.getInt("yearlanguage");
				} else { 
					v_yearduty2         = 0;
					v_yearlanguage2     = 0;
				}

				if ( v_matchcode.equals("W") && (v_yearduty1 <= v_yearduty2) ) {          // 직무신청갯수가 같은경우
					result = -6;
					return result;
				} else if ( v_matchcode.equals("L") && (v_yearlanguage1 <= v_yearlanguage2)) {   // 어학신청갯수가 같은경우
					result = -6;
					return result;
				}
			}
		} catch ( Exception ex ) { 
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
			if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
			if ( ls3 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
		}

		return result;
	}


	/**
	* 블랙리스트 제약설정
	* @param connMgr       DBConnectionManager
	* @param p_subj        과정코드
	* @param p_year        과정년도
	* @param p_subjseq     과정기수
	* @param p_userid      유저아이디
	* @param p_comp        회사코드
	* @return result       제약대상 - 메세지
	*/
	public String jeyakBlackcondition(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
		//        PreparedStatement   pstmt           = null;
		ListSet             ls1             = null;
		ListSet             ls2             = null;
		StringBuffer        sbSQL           = new StringBuffer("");
		String              result          = "";

		String              v_conditioncode = "";

		try { 
			sbSQL.append(" select  a.conditioncode                                      \n")               
			.append(" from    tz_blacklist    a                                    \n")
			.append("     ,   tz_subjseq      b                                    \n")
			.append(" where   a.grcode    = b.grcode                               \n")
			.append(" and     a.gryear    = b.gyear                                \n")
			.append(" and     a.grseq     = b.grseq                                \n")
			.append(" and     b.subj      = " + SQLString.Format(p_subj   ) + "    \n")
			.append(" and     b.year      = " + SQLString.Format(p_year   ) + "    \n")
			.append(" and     b.subjseq   = " + SQLString.Format(p_subjseq) + "    \n");


			ls1                 = connMgr.executeQuery(sbSQL.toString());

			sbSQL.setLength(0);

			// 제약대상이면
			if ( ls1.next() ) { 
				v_conditioncode = ls1.getString("conditioncode");

				// 제약대상 메세지 SELECT
				sbSQL.append(" select  a.alertmsg                                                       \n")            
				.append(" from    tz_BlackCondition   a                                            \n")
				.append("     ,   tz_subjseq          b                                            \n")
				.append(" where   a.grcode        = b.grcode                                       \n")
				.append(" and     a.gryear        = b.gyear                                        \n")
				.append(" and     a.grseq         = b.grseq                                        \n")
				.append(" and     b.subj          = " + SQLString.Format(p_subj           ) + "    \n")
				.append(" and     b.year          = " + SQLString.Format(p_year           ) + "    \n")
				.append(" and     b.subjseq       = " + SQLString.Format(p_subjseq        ) + "    \n")
				.append(" and     a.conditioncode = " + SQLString.Format(v_conditioncode  ) + "    \n");

				ls2     = connMgr.executeQuery(sbSQL.toString());

				if ( ls2.next() ) { 
					result  = ls2.getString("alertmsg");

					return result;
				}
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}

			if ( ls2 != null ) { 
				try { 
					ls2.close();  
				} catch ( Exception e ) { } 
			}
		}

		return result;        
	}



	/**
	* 삼진 아웃 대상에 걸렸는지 여부
	* @param connMgr       DBConnectionManager
	* @param p_userid      유저아이디
	* @return result       0 : 정상, -5 : 제약대상
	*/
	public int jeyakStrOut(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
		ListSet             ls              = null;
		StringBuffer        sbSQL           = new StringBuffer("");
		int					result          = 0;
		String				sql 			= "";

		int v_cnt = 0;
		//String v_grcode = "N000001"; 
		String v_gu = "";

		try {
			/*
			sbSQL.append(" SELECT      COUNT(*)     Cnt                                 \n")
				 .append(" FROM        Tz_Strout    Ts                                  \n")
				 .append(" WHERE       UserId   =  " + SQLString.Format(p_userid) + "   \n")
				 .append(" AND         Grcode   =  " + SQLString.Format(v_grcode) + "   \n")
				 .append(" AND         isstrout = 'Y'                                   \n");
			ls = connMgr.executeQuery(sbSQL.toString());

			if ( ls.next() ) {
				v_cnt  = ls.getInt("Cnt");
			}
			if ( v_cnt > 0 ) {
				result  = -5;
			}
			*/

			// 삼진아웃이 되는 과정의 교육종료일부터 수강신청할 교육시작월까지의 제약기간동안은 학습을 하지 못한다.
			sql = "\n select case when to_date(edustart,'yyyymmdd') < eduend then "
			    + "\n                  'N' "
			    + "\n             else 'Y' "
			    + "\n        end as gu "
			    + "\n from   ( "
			    + "\n         select ( "
			    + "\n                 select add_months(to_date(substr(b.eduend,0,8),'yyyymmdd'), c.duemonth) "
			    + "\n                 from   tz_strout a "
			    + "\n                      , tz_subjseq b "
			    + "\n                      , tz_strout_setup c "
			    + "\n                 where  a.subj = b.subj "
			    + "\n                 and    a.year = b.year "
			    + "\n                 and    a.subjseq = b.subjseq "
			    + "\n                 and    a.isstrout = 'Y' "
			    + "\n                 and    a.userid = " + StringManager.makeSQL(p_userid)
			    + "\n                ) as eduend "
			    + "\n              , ( "
			    + "\n                 select substr(edustart,0,8) "
			    + "\n                 from   tz_subjseq a, tz_subj b "
			    + "\n                 where  a.subj = " + StringManager.makeSQL(p_subj)
			    + "\n                 and    a.year = " + StringManager.makeSQL(p_year)
			    + "\n                 and    a.subjseq = " + StringManager.makeSQL(p_subjseq)
			    + "\n                 and    a.subj = b.subj  "
			    + "\n                 and   (b.subj_gu is null or b.subj_gu = '' )  "
			    + "\n                ) as edustart "
			    + "\n         from   dual "
			    + "\n        )  ";
			ls = connMgr.executeQuery(sql);

			if ( ls.next() ) {
				v_gu  = ls.getString("gu");
			}
			if ( "N".equals(v_gu) ) {
				result  = -5;
			}
		} catch ( SQLException e ) {
			result  = -5;
			ErrorManager.getErrorStackTrace(e, null, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			result  = -5;
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;        
	}    


	/**
	* 동시 수강 가능한 항목수 제약 설정
	* @param connMgr       DBConnectionManager
	* @param p_subj        과정코드
	* @param p_year        과정년도
	* @param p_subjseq     과정기수
	* @param p_userid      유저아이디
	* @param p_comp        회사코드
	* @return result       0 : 정상, -7 : 제약대상
	*/
	public int jeyakSyncPropose(DBConnectionManager connMgr, String p_userid, String p_subj) throws Exception { 
		ListSet             ls              = null;
		//        StringBuffer        sbSQL           = new StringBuffer("");
		int                 result          = 0;

		int                 v_syncpropcnt   = 2;
		int                 v_cnt           = 0;

		SubjLimitBean       bean            = new SubjLimitBean();
		DataBox             dbox            = bean.selectSubjLimit(null);
		String 				sql             = "";

		try {
			v_syncpropcnt               = dbox.getInt("d_cnt");

			sql = "select count(*) cnt															\n"
				+ "from tz_propose a, vz_scsubjseq b											\n"
				+ "where a.subj = b.subj														\n"
				+ "and a.year =b.year															\n"
				+ "and a.subjseq = b.subjseq													\n"
				+ "and to_char(sysdate, 'yyyymmddhh24') between b.propstart and b.propend	\n"
				+ "and  (a.cancelkind is null or a.cancelkind not in ('F', 'P'))				\n"
				+ "and a.userid = "+ SQLString.Format(p_userid)+ "								\n";


			ls = connMgr.executeQuery(sql);

			if ( ls.next() ) {
				v_cnt  = ls.getInt("cnt");
			}
			// 동시 수강 가능 과정수 초과 여부
			if( v_syncpropcnt > 0) {
				if ( v_cnt >= v_syncpropcnt ) {
					result  = -7;
				}
			}
		} catch ( SQLException e ) {
			result  = -7;
			ErrorManager.getErrorStackTrace(e, null, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			result  = -7;
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;        
	}
	
	/**
	* 고용보험 환급 과정인데 주민등록번호가 입력되어 있지 않은 경우
	* @param connMgr      DBConnectionManager
	* @param p_subj       과정코드
	* @param p_year       과정년도
	* @param p_subjseq    과정기수
	* @param p_userid     유저아이디
	* @param p_comp       회사코드
	* @return result      0 : 정상, 1 : 정원초과
	*/
	public int jeyakNotbirth_date(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
		ListSet             ls      = null;
		String sql  = "";
		int result = 0;

		try { 
			sql = " select 'Y'												\n"
				+ "from tz_subjseq a, tz_member b							\n"
				+ "where subj = " + StringManager.makeSQL(p_subj) + " 		\n"
				+ "and year= " +StringManager.makeSQL(p_year) + "     		\n"
				+ "and subjseq = " + StringManager.makeSQL(p_subjseq) + "  	\n"
				+ "and b.userid = " + StringManager.makeSQL(p_userid) + "   \n"
				+ "and (b.birth_date is null or trim(b.birth_date) is null)			\n";

			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				result = -11;
			}
		} catch ( SQLException e ) {
			result  = -11;
			ErrorManager.getErrorStackTrace(e, null, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception ex ) { 
			result  = -11;
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;
	}
	
	/**
	* 독서교육 과정의 경우 부서(tz_subj sel_dept), 직급(tz_subj sel_post)와 get_orggu(userid), post 값과 일치하는 사람으로 제한
	* @param connMgr      DBConnectionManager
	* @param p_subj       과정코드
	* @param p_year       과정년도
	* @param p_subjseq    과정기수
	* @param p_userid     유저아이디
	* @param p_comp       회사코드
	* @return result      0 : 정상, -12 : 
	*/
	public int jeyakRC(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
		ListSet             ls      = null;
		String sql  = "";
		int result = 0;
		String v_isonoff = "";
		try {
			sql = "select isonoff											\n"
				+ "from tz_subj a											\n" 
				+ "where a.subj = " + StringManager.makeSQL(p_subj) + " 	\n";
				
			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				v_isonoff = ls.getString("isonoff");
			}
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			
			if("RC".equals(v_isonoff)) {
				sql = "select 'Y'												\n"
					+ "from tz_subj a, tz_member b								\n" 
					+ "where a.subj = " + StringManager.makeSQL(p_subj) + " 	\n"
					+ "and b.userid = " + StringManager.makeSQL(p_userid) + " 	\n"
					+ "and sel_dept like '%' || get_orggu(userid) || '%'		\n"
					+ "and sel_post like '%' || post || '%'						\n";
				
				ls = connMgr.executeQuery(sql);
				if ( !ls.next() ) { 
					result = -12;
				}
			} 
		} catch ( SQLException e ) {
			result  = -12;
			ErrorManager.getErrorStackTrace(e, null, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception ex ) { 
			result  = -12;
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;
	}

	/**
	* 수강신청시 제약 과정수
	 - 이러닝, 독서교육으로 구분됨.
	 - 제약과정수는 공통코드의 '0105'에 등록해서 사용.
	 - 이러닝은 'ON', 독서교육은 'RC'
	 - 이러닝중에서 subj_gu 이 'J','M','E' 인거는 제외.
	 - 기준은 교육시작월. 
	 - 교육시작월이 중복되면 안됨. (일반과정은 12개 초과될수 없음)
	* @return result      
	*/
	public int jeyakSubjCnt(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception { 
		ListSet             ls      = null;
		String sql  = "";
		int result = 0;
		String v_codegubun = "0105";

		try { 
			sql = " select isonoff, cnt, nvl(y.codenm, 999999999) comparecnt			\n"
				+ "from (																\n"
				+ "    select isonoff, count(*) cnt										\n"
				+ "    from tz_propose a, vz_scsubjseq b								\n"
				+ "    where a.subj = b.subj											\n"
				+ "    and a.year= b.year												\n"
				+ "    and a.subjseq= b.subjseq											\n"
				+ "    and (subj_gu is null or subj_gu not in ('J','M','E'))			\n"
				+ "    and (substr(b.edustart, 1,6), isonoff) in (						\n"
				+ "        select substr(edustart, 1, 6), isonoff						\n"
				+ "        from vz_scsubjseq											\n"
				+ "        where subj = " + StringManager.makeSQL(p_subj) + "           \n"
				+ "        and year= " + StringManager.makeSQL(p_year) + "              \n"
				+ "        and subjseq= " + StringManager.makeSQL(p_subjseq) + "        \n"
				+ "        and (subj_gu is null or subj_gu not in ('J','M','E'))		\n"
				+ "    )																\n"
				+ "    and userid = " + StringManager.makeSQL(p_userid) + "             \n"
				+ "    and  (a.cancelkind is null or a.cancelkind not in ('F', 'P'))	\n"
				+ "    group by isonoff													\n"
				+ ") x, (																\n"
				+ "    select code, codenm												\n"
				+ "    from tz_code														\n"
				+ "    where gubun = " + StringManager.makeSQL(v_codegubun) + "			\n"
				+ ") y																	\n"
				+ "where x.isonoff = y.code(+)											\n"
				+ "and cnt >= nvl(y.codenm, 999999999)     								\n";	

			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				result = -13;
			}
		} catch ( SQLException e ) {
			result  = -13;
			ErrorManager.getErrorStackTrace(e, null, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception ex ) { 
			result  = -13;
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;
	}

	/**
	* 제약메세지
	* @param p_jeyak       제약코드
	* @return result       메세지
	*/
	public String jeyakMsg(int p_jeyak) { 
		String  msg = "";
		if ( p_jeyak == -2)  msg ="이미 신청한 과정입니다";                    // 이미신청한과정  - 2
		else if      ( p_jeyak == -1)  msg ="정원 초과입니다";                         // 정원초과  - 1
		else if ( p_jeyak == -3)  msg ="수료한 과정입니다";                       // 수료한과정  - 3
		else if ( p_jeyak == -4)  msg ="대상 과정이 아닙니다";                     // 마스터과정인지, 대상자인지 - 4
		else if ( p_jeyak == -5)  msg ="삼진 아웃 대상입니다.";                    // 삼진 아웃 대상입니다. -5
		else if ( p_jeyak == -6)  msg ="이미 수강 신청하신 과정이 있습니다.";         //  
//		else if ( p_jeyak == -7)  msg ="동시 수강이 가능한 과정수를 초과하셨습니다.";   // 동시 수강이 가능한 과정수 추가 -7 
		else if ( p_jeyak == -10) msg ="수강신청이 마감되었습니다.";                // 수강신청이 마감되었습니다. -1 
		else if ( p_jeyak == -11) msg ="고용보험 환급과정인데 주민등록번호가 없습니다."; // 고용보험 환급과정인데 주민등록번호가 없습니다. -11
		else if ( p_jeyak == -12) msg ="특정 부서, 직급의 사람만 수강신청 가능한 과정입니다."; // 특정 부서, 직급의 사람만 수강신청 가능  -12
		else if ( p_jeyak == -13) msg ="해당 교육월에 교육받을 수 있는 과정수를 초과하였습니다."; // 해당 교육월에 교육받을 수 있는 과정수 초과  -13
		else if ( p_jeyak == -14) msg ="이미 수료한 과정이므로 수강신청 하실 수 없습니다.";
		else if ( p_jeyak == -15) msg ="이미 수강중인 과정입니다.";
		else if ( p_jeyak == -16) msg ="이미 수강신청하신 과정입니다.";
		else if ( p_jeyak == -17) msg ="단체 수강신청만 가능합니다.";
		else if ( p_jeyak == -18) msg ="대상직무만 신청할 수 있는 과정입니다.";
		return msg;
	}

	/**
	* 제약메세지
	* @param p_jeyak       제약코드
	* @return result       메세지
	*/
	public String jeyakMsg(int p_jeyak, String p_subjnm, String p_name, String p_proposedate) { 
		String  msg = "";
		
		if ( p_jeyak == -2)  msg = "<span class=\"text_blueB\">" + p_name + "</span>"  + "님께서는 이미 " + p_proposedate + " 수강 신청하셨습니다.";    // 이미신청한과정  - 2
		else if ( p_jeyak == -1)  msg = "<span class=\"text_orange\">" + p_subjnm + "</span>" + " 과정은<br>정원 초과입니다.";                              // 정원초과  - 1
		else if ( p_jeyak == -3)  msg = "<span class=\"text_orange\">" + p_subjnm + "</span>" + "과정은<br>수료한 과정입니다";                             // 수료한과정  - 3
		else if ( p_jeyak == -4)  msg = "<span class=\"text_orange\">" + p_subjnm + "</span>" + "과정은<br>대상 과정이 아닙니다";                           // 마스터과정인지, 대상자인지 - 4
	    else if ( p_jeyak == -5)  msg = "삼진아웃 대상입니다.<br>삼진 아웃 제약기간에 포함된 과정은 신청할 수 없습니다.";                                               // -5
		//          else if ( p_jeyak == -6)  msg = "이미 수강 신청하신 과정이 있습니다.";                                    //  
		else if ( p_jeyak == -7)  msg = p_name   + "님께서는 이미 동시 수강이 가능한<br>과정수를 초과하셨습니다.";    // 동시 수강이 가능한 과정수 추가 -7 
		else if ( p_jeyak == -10) msg = "<span class=\"text_orange\">" + p_subjnm + "</span>" + "과정은<br>수강신청이 마감되었습니다.";                      // 수강신청이 마감되었습니다. -1 
		else if ( p_jeyak == -11) msg = "<span class=\"text_orange\">" + p_subjnm + "</span>" + "과정은<br>고용보험 환급과정이어서 주민등록번호 입력이 필요합니다.";  // 고용보험 환급과정, 주민등록번호 필요 -11
		else if ( p_jeyak == -12) msg = "<span class=\"text_orange\">" + p_subjnm + "</span>" + "과정은<br>특정 부서, 직급의 사람만이 수강신청이 가능한 과정입니다.";  // 특정 부서, 직급
		else if ( p_jeyak == -13) msg = "한달동안 교육받을 수 있는 과정수를 초과하여<br><span class=\"text_orange\">" + p_subjnm + "</span>" + "과정을<br>수강신청 하실 수 없습니다.";  // 해당 교육월에 교육받을 수 있는 과정수 초과
		else if ( p_jeyak == -14) msg = "<span class=\"text_orange\">" + p_subjnm + "</span>" + "과정은<br>이미 수료한 과정이므로 수강신청 하실 수 없습니다.";  // 수료한과정은 다시 수강할 수 없음.
		else if ( p_jeyak == -15) msg = "<span class=\"text_orange\">" + p_subjnm + "</span>" + "과정은<br>이미 수강중인 과정입니다. ";  // 미수료한과정은 다시 수강할 수 있음. 다른기수 수강중이면 신청할 수 없음.
		else if ( p_jeyak == -16) msg = "<span class=\"text_orange\">" + p_subjnm + "</span>" + "과정은<br>이미 수강신청하신 과정입니다. ";  // 미수료한과정은 다시 수강할 수 있음. 다른기수 수강신청중이면 신청할 수 없음.
		else if ( p_jeyak == -17) msg = "단체 수강신청만 가능합니다. ";  // 비계열사는 개별신청 불가능.
		else if ( p_jeyak == -18) msg = "대상직무만 신청할 수 있는 과정입니다.";
		else if ( p_jeyak == -20) msg = "수강신청과정중에 반려된과정이 있습니다.<br>운영장에게 문의하세요.";
		//else if ( p_jeyak == 7 )  msg = "<span class=\"text_orange\">" + p_subjnm + "</span>" + "과정은<br>현재 복습이 가능한 과정입니다.<br>다시 수강신청하시겠습니까?"; 

		return msg;
	}


	/**
	맛보기 로그 등록
	@param box      receive from the form object and session
	@return isOk    1:insert success,0:insert fail
	*/
	public int insertPreviewLog(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls          = null;
		PreparedStatement   pstmt       = null;
		StringBuffer        sbSQL       = new StringBuffer("");
		int                 isOk        = 0;

		int                 v_seq       = 0;
		String              v_subj      = box.getString("p_subj"    );
		String              s_userid    = box.getSession("userid"   );
		String              tem_grcode  = box.getStringDefault("tem_grcode", box.getSession("tem_grcode"));        

		try { 
			connMgr = new DBConnectionManager();

			sbSQL.append(" select nvl(max(seq) + 1, 1) from tz_preview_log    \n");

			ls       = connMgr.executeQuery(sbSQL.toString());

			if ( ls.next() ) { 
				v_seq   = ls.getInt(1);
			} else {
				v_seq   = 1;
			}

			ls.close();

			sbSQL.setLength(0);

			sbSQL.append(" insert into tz_preview_log                      \n")
				.append(" (       seq                                      \n")
				.append("     ,   addate                                   \n")
				.append("     ,   subj                                     \n")
				.append("     ,   userid                                   \n")
				.append("     ,   luserid                                  \n")
				.append("     ,   ldate                                    \n")
				.append("     ,   grcode                                   \n")
				.append(" ) values (                                       \n")
				.append("         ?                                        \n")
				.append("     ,   to_char(sysdate,'yyyymmddhh24miss')      \n")
				.append("     ,   ?                                        \n")
				.append("     ,   ?                                        \n")
				.append("     ,   ?                                        \n")
				.append("     ,   to_char(sysdate,'yyyymmddhh24miss')      \n")
				.append("     ,   ?                                        \n")
				.append(" )                                                \n");

			pstmt   = connMgr.prepareStatement(sbSQL.toString());

			pstmt.setInt   (1, v_seq        );
			pstmt.setString(2, v_subj       );
			pstmt.setString(3, s_userid     );
			pstmt.setString(4, s_userid     );
			pstmt.setString(5, tem_grcode   );

			isOk     = pstmt.executeUpdate();
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

			if ( pstmt != null ) { 
				try { 
					pstmt.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}

		return isOk;
	}


	/**
	수강 신청을 위한 과정검색
	@param box      receive from the form object and session
	@return ArrayList 검색 결과 리스트
	*/
	public ArrayList selectSubjForApply(RequestBox box) throws Exception { 
		DBConnectionManager connMgr         = null;
		ListSet             ls1             = null;
		ArrayList           list1           = null;
		//        ArrayList           list2           = null;
		DataBox             dbox            = null;
		StringBuffer        sbSQL       = new StringBuffer("");

		//String              gyear           = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );
		//        String              gyear           = box.getString("p_gyear");
		String              v_user_id       = box.getSession("userid"           );

		// 개인의 회사 기준으로 과정 리스트
		//        String              v_comp          = box.getSession("comp"             );
		// 사이트의 GRCODE 로 과정 리스트
		String              v_grcode        = box.getSession("tem_grcode"       );
		//        String              v_lsearch       = box.getString ("p_lsearch"        );
		String              v_lsearchtext   = box.getString ("p_lsearchtext"    );
		String              v_basis_upperclass = box.getString("p_basis_upperclass");

		//        int                 v_propstart     = 0;
		//        int                 v_propend       = 0;
		//        boolean             ispossible      = false;

		int v_pageno        = box.getInt("p_pageno");
		String ss_comp = box.getSession("comp");

		try { 
			connMgr = new DBConnectionManager();
			list1   = new ArrayList();

			sbSQL.append(" select  distinct a.subj                                                      \n")
				.append("     ,   a.scupperclass                                                       \n")
				.append("     ,   a.scmiddleclass                                                      \n")
				.append("     ,   a.isonoff                                                            \n")
				.append("     ,   b.classname                                                          \n")
				.append("     ,   a.subjnm                                                             \n")
				.append("     ,   a.edustart                                                           \n")
				.append("     ,   a.eduend                                                             \n")
				.append("     ,   a.studentlimit                                                       \n") 
				.append("     ,   a.scyear                                                             \n")   
				.append("     ,   a.scsubjseq                                                          \n")    
				.append("     ,   a.propose_date                                                       \n")
				.append("     ,   a.usebook                                                            \n")
				.append("     ,   a.bookname                                                           \n")
				.append("     ,   (case when len(propstart) = 0 then propstart else substr(propstart, 0, 10) end) propstart\n")
				.append("     ,   (case when len(propend) = 0 then propend else substr(propend, 0, 10) end) propend   \n")                                                     
				//                 .append("     ,   (                                                                    \n")
				//                 .append("             select  substr(isnull(indate,'00000000000000'),0,4 )                \n")
				//                 .append("             from    TZ_SUBJ                                                  \n")
				//                 .append("             where   subj = a.subj                                            \n")
				//                 .append("         )                               indate                               \n")
				//                 .append("     ,   (                                                                    \n")
				//                 .append("             select  isnull(avg(distcode1_avg), 0.0)                             \n")
				//                 .append("             from    tz_suleach                                               \n")                  
				//                 .append("             where   subj    = a.subj                                         \n")
				//                 .append("             and     grcode <> 'ALL'                                          \n")
				//                 .append("         )                               distcode1_avg                        \n")
				//                 .append("     ,   (                                                                    \n")
				//                 .append("             select  isnull(avg(distcode_avg), 0.0)                              \n")
				//                 .append("             from    tz_suleach                                               \n")                   
				//                 .append("             where   subj= a.subj                                             \n")
				//                 .append("             and     grcode = 'ALL'                                           \n")
				//                 .append("         )                               distcode_avg                         \n")
				.append("     ,   (                                                                    	\n")
				.append("             select count(userid) CNTS                                        	\n")
				.append("             from tz_propose c                                                	\n")
				.append("             where c.subj = a.scsubj                                          	\n")
				.append("             and c.year = a.scyear                                            	\n")
				.append("             and c.subjseq = a.scsubjseq                                      	\n")
				.append("         ) proposecnt                                                         	\n")
				.append("     ,   nvl(c.haspreviewobj, 'N')      haspreviewobj                         	\n")
				.append(" from    (                                                                   	\n")
				.append("          select tp.ldate propose_date,          								\n")
				.append("              tp.userid                                                        \n")
				.append("              ,  vs.*                                                          \n")
				.append("              , gr.comp                                                        \n")
				.append("          from   vz_scsubjseq vs, tz_grcomp gr, tz_propose tp					\n")
				.append("          where  vs.grcode = gr.grcode                                         \n")
				.append("          and    vs.scsubj        = tp.subj(+)                                 \n")
				.append("          and    vs.scyear        = tp.year(+)                                 \n")
				.append("          and    vs.scsubjseq     = tp.subjseq(+)                              \n")
				.append("          and    tp.userid        = " + SQLString.Format(v_user_id) + "        \n")
				.append("          and    gr.comp          =  " + SQLString.Format(ss_comp ) + "        \n")
				.append("          and    vs.isuse         = 'Y'                                        \n")
				.append("          and    vs.subjvisible   = 'Y'                                        \n")
				.append("          and    vs.isblended     = 'N'                                        \n")
				.append("          and    vs.isexpert      = 'N'                                        \n")
				.append("          and    substr(to_char(sysdate,'yyyymmddhh24miss'), 1, 10) between substr(propstart, 0, 10) and substr(eduend, 0, 10)         \n")
				.append("          and    nvl(tp.cancelkind, ' ')   not in ('F', 'P')                                                   \n")
				.append("         ) a                                                                   \n")
				.append("        ,(  select  upperclass                                               	\n")
				.append("                 ,   middleclass                                              	\n")
				.append("                 ,   classname                                               	\n")
				.append("             from    tz_subjatt                                               	\n")
				.append("             where   middleclass <> '000'                                     	\n")
				.append("             and     lowerclass  =  '000'                                     	\n")
				.append("         ) b                                                    				\n")
				.append("       , (                                                            			\n")
				.append("             select  subj, (case when count(*)= 0 then 'N' else 'Y' end)  haspreviewobj       \n")
				.append("             from    tz_previewobj                                            	\n")
				.append("             group by subj                                                    	\n")
				.append("         ) c                                                    				\n")
				.append(" where     a.scupperclass  = b.upperclass                                      \n")
				.append(" and     a.scmiddleclass = b.middleclass                                      	\n")
				.append(" and     a.subj          = c.subj(+)                                           \n")
				.append(" and     a.comp        = " + SQLString.Format(ss_comp) + "                    	\n")
				.append(" and     a.isuse         = 'Y'                                                	\n")
				.append(" and     a.SEQVISIBLE    = 'Y'                                                	\n")
				.append(" and     substr(to_char(sysdate,'yyyymmddhh24miss'), 1, 10) between substr(propstart, 0, 10) and substr(a.eduend, 0, 10)\n");

				//.append(" and     a.gyear         = " + SQLString.Format(gyear    ) + "                \n");

				if( "global".equals(v_basis_upperclass) ) {
					sbSQL.append(" and     a.scupperclass   = 'A04'                                         \n");
					//sbSQL.append(" and     a.contenttype   != 'L'                                         \n");
				} else if( "common".equals(v_basis_upperclass) ) {
					sbSQL.append(" and     a.scupperclass   = 'A02'                                         \n");
					//sbSQL.append(" and     a.contenttype   != 'L'                                         \n");
				} else if( "future".equals(v_basis_upperclass) ) {
					sbSQL.append(" and     a.scupperclass   = 'A03'                                         \n");
					//sbSQL.append(" and     a.contenttype   != 'L'                                         \n");
				} else if( "cp".equals(v_basis_upperclass) ) {
					sbSQL.append(" and     a.contenttype   = 'L'                                         \n");
				} else { //business
					sbSQL.append(" and     a.scupperclass   = 'A01'                                         \n");
					//sbSQL.append(" and     a.contenttype   != 'L'                                         \n");
				}   


				// ON/OFF 구분
				/*
				if ( v_lsearch.equals("isonoff") ) { 
					if ( v_lsearchtext.equals("온라인") ) { 
						sbSQL.append(" and a.isonoff = 'ON'                                                                 \n");
					} else if ( v_lsearchtext.equals("오프라인") ) { 
						sbSQL.append(" and a.isonoff = 'OFF'                                                                \n");
					} else { 
						sbSQL.append(" and a.isonoff like " + SQLString.Format( "%" + v_lsearchtext + "%" )          + "    \n");
					}
				} else
					if ( v_lsearch.equals("upperclass"   ))  {       // 분류
					sbSQL.append(" and upper(b.classname) like upper(" + SQLString.Format("%" + v_lsearchtext + "%") + ")   \n");
				} else
				*/
				//if ( v_lsearch.equals("subjnm"       ))  {       // 과정명

				if ( !"".equals(v_lsearchtext))  {       // 과정명
					sbSQL.append(" and upper(a.scsubjnm ) like upper(" + SQLString.Format("%" + v_lsearchtext + "%") + ")   \n");
				}

				//sbSQL.append(" order by propstart desc, isonoff desc, scupperclass, scmiddleclass, subjnm               \n");
				sbSQL.append(" order by propstart desc, isonoff, scupperclass, scmiddleclass, subjnm               \n");

			ls1 = connMgr.executeQuery(sbSQL.toString());
			//            ls1.setPageSize(15);             //  페이지당 row 갯수를 세팅한다
			//            ls1.setCurrentPage(v_pageno);                    //     현재페이지번호를 세팅한다.
			//            int total_page_count = ls1.getTotalPage();       //     전체 페이지 수를 반환한다
			//            int total_row_count = ls1.getTotalCount();    //     전체 row 수를 반환한다
			MainSubjSearchBean bean = new MainSubjSearchBean();
			while ( ls1.next() ) { 
				dbox    = ls1.getDataBox();
				////                dbox.put("d_dispnum", new Integer(total_row_count - ls1.getRowNum() + 1));
				////                dbox.put("d_totalpage", new Integer(total_page_count));
				//                box.put("d_rowcount", new Integer(15));
				//                dbox.put("distcode1_avg", new Double( ls1.getDouble("distcode1_avg" )));
				//                dbox.put("distcode_avg" , new Double( ls1.getDouble("distcode_avg"  )));
				dbox.put("d_ispropose", bean.getPropseStatus(box,ls1.getString("subj"), ls1.getString("scsubjseq"), ls1.getString("scyear"), v_user_id, "3"));
				list1.add(dbox);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}

		return list1;
	}


	/**
	수강신청을 위한 코스
	@param box      receive from the form object and session
	@return ArrayList 코스 리스트
	*/
	public ArrayList selectCourseForApply(RequestBox box) throws Exception { 
		DBConnectionManager connMgr         = null;
		ListSet             ls1             = null;
		ListSet             ls2             = null;
		ArrayList           list1           = null;
		ArrayList           list2           = null;
		DataBox             dbox            = null;
		DataBox             dbox2            = null;
		String sql = "";
		String sql2 = "";

		//String              gyear           = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );
		//         String              gyear           = box.getString("p_gyear");
		String              v_user_id       = box.getSession("userid"           );

		// 개인의 회사 기준으로 과정 리스트
		//         String              v_comp          = box.getSession("comp"             );
		// 사이트의 GRCODE 로 과정 리스트
		String              v_grcode        = box.getSession("tem_grcode"       );
		//         String              v_lsearch       = box.getString ("p_lsearch"        );
		String              v_lsearchtext   = box.getString ("p_lsearchtext"    );
		String              v_basis_upperclass = box.getString("p_basis_upperclass");

		//         int                 v_propstart     = 0;
		//         int                 v_propend       = 0;
		//         boolean             ispossible      = false;

		int v_pageno        = box.getInt("p_pageno");
		String ss_comp      = box.getSession("comp");
		try { 
			connMgr = new DBConnectionManager();
			list1   = new ArrayList();
			list2   = new ArrayList();

			sql ="select a.course, a.cyear, a.courseseq, max(coursenm) coursenm, max(edustart) edustart, max(eduend) eduend, max(propstart) propstart, max(propend) propend    \n"
				+ "       ,  case when sum(cnts) > 0 then 'Y' else 'N' end ispropose, max(propose_date) propose_date																							\n"
				+ "from vz_scsubjseq a, (                                                                                                                                       \n"
				+ "	select  x.course, x.cyear, x.courseseq, max(y.ldate) propose_date, count(y.userid) CNTS                                                                     \n"
				+ "	from vz_scsubjseq x, tz_propose y                                                                                                            \n"
				+ "	where x.subj = y.subj(+)                                                                                                                                          \n"
				+ "	and x.year = y.year(+)                                                                                                                                         \n"
				+ "	and x.subjseq = y.subjseq(+)                                                                                                                                   \n"
				+ "    and nvl(y.cancelkind, ' ')   NOT IN ('F', 'P')  																										\n"
				+ "    and y.userid = " + SQLString.Format(v_user_id) + "     																								\n"	             
				+ "	and substr(to_char(sysdate,'yyyymmddhh24miss'), 1, 10) between substr(propstart, 0, 10) and substr(eduend, 0, 10)                          \n"
				+ "	and x.course !='000000'                                                                                                                                     \n"
				//	             + "	and grcode = " + StringManager.makeSQL(v_grcode) + "                                                                                                        \n"
				+ "	and isuse = 'Y'                                                                                                                                             \n"
				+ "	and subjvisible = 'Y'                                                                                                                                       \n"
				+ "	group by x.course, x.cyear, x.courseseq                                                                                                                     \n"
				+ ") b, tz_grcomp c                                                                                                                                                \n"
				+ "where a.course != '000000'                                                                                                                                   \n"
				+ "and    substr(to_char(sysdate,'yyyymmddhh24miss'), 1, 10) between substr(propstart, 0, 10) and substr(eduend, 0, 10)                          \n"
				+ "and a.grcode  = c.grcode 																																		\n"
				+ "and c.comp = " + SQLString.Format(ss_comp) + "                                                                                                                                     \n"
				+ "and isuse = 'Y'                                                                                                                                              \n"
				+ "and seqvisible = 'Y'                                                                                                                                        \n"
				+ "and a.course = b.course                                                                                                                                      \n"
				+ "and a.cyear = b.cyear                                                                                                                                        \n"
				+ "and a.courseseq = b.courseseq                                                                                                                                \n"
				+ "group by a.course, a.cyear, a.courseseq                                                                                                                      \n";


			ls1 = connMgr.executeQuery(sql);

			while ( ls1.next() ) { 
				dbox    = ls1.getDataBox();
				sql2 = "select subj, year, subjseq, subjnm, isonoff						\n"
				+ "from vz_scsubjseq													\n"
				+ "where course = " + SQLString.Format(ls1.getString("course") ) + "  	\n"
				+ "and cyear = " + SQLString.Format(ls1.getString("cyear")) + "  		\n"
				+ "and courseseq = " + SQLString.Format(ls1.getString("courseseq")) + "\n";

				ls2 = connMgr.executeQuery(sql2);
				while(ls2.next()) {
					dbox2 = ls2.getDataBox();
					list2.add(dbox2);
				}

				dbox.put("subjList", list2);
				list2   = new ArrayList();
				list1.add(dbox);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}

			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}

		return list1;
	}


	/**
	배송지 정보
	@param box      receive from the form object and session
	@return 
	*/
	public DataBox selectDeliveryInfo(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	    = null;
		DataBox             dbox        	= null;
		ListSet             ls        	= null;
		String 			  sql 			= "";
		String 			  ss_userid 	= box.getSession("userid");
		String              v_subj        = box.getString("p_subj");
		String              v_year        = box.getString("p_year");
		String              v_subjseq     = box.getString("p_subjseq");
	
		try { 
			connMgr     = new DBConnectionManager();
			
			/*
			sql = "select b.userid, b.name, a.delivery_handphone \n"
				+ "     , a.delivery_post1, a.delivery_post2, a.delivery_address1	\n"
				+ "		, b.handphone,  b.zip_cd, b.address  \n"
				+ "     , get_compnm(b.comp) companynm \n"
				+ "     , b.position_nm as deptnm \n" 
				+ "     , b.lvl_nm as jikwinm \n"
				+ "from  tz_delivery a, tz_member b \n"
				+ "where a.userid = b.userid \n"
				+ "and   a.subj = " + StringManager.makeSQL(v_subj) + " \n"
				+ "and   a.year = " + StringManager.makeSQL(v_year) + " \n"
				+ "and   a.subjseq = " + StringManager.makeSQL(v_subjseq) + " \n"
				+ "and   a.userid = " + StringManager.makeSQL(ss_userid) + " \n";
			*/
			
			sql = "select b.userid, b.name, a.delivery_handphone \n"
				+ "     , a.delivery_post1, a.delivery_post2, a.delivery_address1	\n"
				+ "		, b.handphone,  b.zip_cd, b.address  \n"
				+ "     , get_compnm(b.comp) companynm \n"
				+ "     , b.position_nm as deptnm \n" 
				+ "     , b.lvl_nm as jikwinm \n"
				+ "from  tz_delivery a, tz_member b \n"
				+ "where a.userid = b.userid \n"
				+ "and   a.userid = " + StringManager.makeSQL(ss_userid) + " \n"
				+ "and   a.ldate = (select max(ldate) from tz_delivery where userid = " + StringManager.makeSQL(ss_userid) + ") \n"
				+ "and   rownum = 1 \n";

			ls         = connMgr.executeQuery(sql);
		
			if ( ls.next() ) { 
				dbox    = ls.getDataBox();
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls != null ) { 
				try { 
					ls.close();  
				} catch ( Exception e ) { } 
			}
		
			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}
	
		return dbox;
	}
	
	/**
	배송지 정보
	@param box      receive from the form object and session
	@return 
	*/
	public DataBox selectMemberInfo(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	    = null;
		DataBox             dbox        	= null;
		ListSet             ls        	= null;
		String 			  sql 			= "";
		String 			  ss_userid 	= box.getSession("userid");
		
		try { 
			connMgr     = new DBConnectionManager();
		
			sql = "select b.userid, b.name \n"
				+ "		, b.handphone,  b.zip_cd, b.address  \n"
				+ "     , get_compnm(b.comp) companynm \n"
				+ "     , b.position_nm as deptnm \n" 
				+ "     , b.lvl_nm as jikwinm \n"
				+ "from  tz_member b \n"
				+ "where userid = " + StringManager.makeSQL(ss_userid) + " \n";
			ls         = connMgr.executeQuery(sql);
		
			if ( ls.next() ) { 
				dbox    = ls.getDataBox();
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls != null ) { 
				try { 
					ls.close();  
				} catch ( Exception e ) { } 
			}
		
			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}
	
		return dbox;
	}



	/**
	배송지 정보 수정
	@param box      receive from the form object and session
	@return isOk   
	*/
	public int UpdateDelivery(RequestBox box) throws Exception {
		DBConnectionManager    connMgr                     = null;
		PreparedStatement      pstmt                       = null;
		String                 sql                         = "";
		int                    isOk                        = 0;
	
		String ss_userid = box.getSession("userid");
		try {
			connMgr    = new DBConnectionManager();
			connMgr.setAutoCommit(false);
		
			sql = "delete from TZ_DELIVERY   		\n"
				+ "where   subj    = ?      			\n"
				+ "and     year    = ?      			\n"
				+ "and     subjseq = ?      			\n"
				+ "and     userid  = ?      			\n";
		
			pstmt   = connMgr.prepareStatement(sql);
			pstmt.setString(1, box.getString("p_subj"));
			pstmt.setString(2, box.getString("p_year"));
			pstmt.setString(3, box.getString("p_subjseq"));
			pstmt.setString(4, ss_userid);
			isOk   = pstmt.executeUpdate();
		
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }

	        sql = "insert into tz_delivery( "
	        	+ "       subj "
	        	+ "     , year "
	        	+ "     , subjseq "
	        	+ "     , userid "
	        	+ "     , delivery_post1 "
	        	+ "     , delivery_post2 "
	        	+ "     , delivery_address1 "
	        	+ "     , delivery_address2 "
	        	+ "     , delivery_handphone "
	        	+ "     , luserid "
	        	+ "     , ldate "
	        	+ "      ) "
	            + "values(? "
	            + "     , ? "
	            + "     , ? "
	            + "     , ? "
	            + "     , ? "
	            + "     , ? "
	            + "     , ? "
	            + "     , ? "
	            + "     , ? "
	            + "     , ? "
	            + "     , to_char(sysdate, 'yyyymmddhh24miss') "
	            + "      ) ";
	        
			pstmt = connMgr.prepareStatement(sql);
			pstmt.setString(1, box.getString("p_subj"));
			pstmt.setString(2, box.getString("p_year"));
			pstmt.setString(3, box.getString("p_subjseq"));
			pstmt.setString(4, ss_userid);
			pstmt.setString(5, box.getString("p_post1"));
			pstmt.setString(6, box.getString("p_post2"));
			pstmt.setString(7, box.getString("p_address1"));
			if("".equals(box.getString("p_post1"))) {
				pstmt.setString(8, box.getString("p_address"));
			} else {
				pstmt.setString(8, box.getString("p_address2"));
			}	
			pstmt.setString(9, box.getString("p_delivery_handphone"));
			pstmt.setString(10, ss_userid);
			
			isOk   = pstmt.executeUpdate();
		
			if ( isOk > 0) { 
				if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e ) { } }
			} else { 
				if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e ) { } }
			}
	
		} catch(Exception ex) {
			isOk = 0;
			if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		
			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}
		return isOk;
	}	

	/**
	개월차 도서 
	@param box      receive from the form object and session
	@return ArrayList
	*/
	public ArrayList selectMonthlyBookList(RequestBox box) throws Exception { 
		DBConnectionManager connMgr         = null;
		ListSet             ls              = null;
		ListSet             ls2              = null;
		ArrayList           list            = null;
		ArrayList           list2           = null;
		String              sql             = "";
		String              sql2            = "";
		DataBox             dbox            = null;
	
		String              v_subj          = box.getString("p_subj"    );
	
		String              ss_userid       = box.getSession("userid");
		String              v_year          = box.getString("p_year"); 
		String              v_subjseq       = box.getString("p_subjseq");
	
		try { 
			connMgr     = new DBConnectionManager();
			list        = new ArrayList();
			list2        = new ArrayList();
		
			sql = "select subj, month										\n"
				+ "from tz_subjbook											\n"
				+ "where subj = " + StringManager.makeSQL(v_subj) + "		\n"
				+ "group by subj, month										\n"	
				+ "order by subj, month                                      \n";
			ls      = connMgr.executeQuery(sql);
		//System.out.println("sql:"+sql);
			while ( ls.next() ) { 
				dbox = ls.getDataBox();
			
				sql2 = "select a.subj, a.month, b.bookname, author, b.bookcode				\n"
				+ "    , (															\n"
				+ "			select (case when count(*) > 0 then 'Y' else 'N' end)	\n"
				+ "			from tz_proposebook										\n"
				+ "			where subj = a.subj										\n"
				+ "			and userid = " + StringManager.makeSQL(ss_userid) + "	\n"
				+ "			and month = a.month										\n"
				+ "			and bookcode = a.bookcode								\n"
				+ "           and year = " + StringManager.makeSQL(v_year) + "        \n"
				+ "           and subjseq = " + StringManager.makeSQL(v_subjseq) + "  \n"
				+ ") checkgubun														\n"		
				+ " 	, (																\n"
				+ "      		nvl(( select  distinct 'Y'												\n"
				+ "           from tz_proposebook                                         \n"
				+ "           where subj = a.subj                                     \n"
				+ "           and year = " + StringManager.makeSQL(v_year) + "        \n"
				+ "           and subjseq = " + StringManager.makeSQL(v_subjseq) + "  \n"
				+ "           and userid = " + StringManager.makeSQL(ss_userid) + "   \n"
				+ "   ),'N')  ) disablegubun                                                    \n"
				+ "from tz_subjbook a, tz_bookinfo b									\n"
	// 독서 과정별 도서 변경관련 임시 추가... and nvl(a.use_yn,'Y')='Y'
				+ "where a.bookcode = b.bookcode	 and nvl(a.use_yn,'Y')='Y'									\n"
				+ "and a.subj = " + StringManager.makeSQL(ls.getString("subj"))  + " 	\n"
				+ "and a.month = " + SQLString.Format(ls.getString("month"))  + 	  " \n";
				//System.out.println("sql2:"+sql2);
				ls2      = connMgr.executeQuery(sql2);
				while(ls2.next()) {
					DataBox dbox2 = ls2.getDataBox();
					list2.add(dbox2);
				}
			
				dbox.put("d_bookinfo", list2);
				list2        = new ArrayList();
				list.add(dbox);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
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
	도서 정보
	@param box      receive from the form object and session
	@return ProposeCourseData
	*/
	public DataBox selectBookInfo(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	    = null;
		DataBox             dbox        = null;
		ListSet             ls1         = null;
		String              sql         = "";
	
		int              v_bookcode      = box.getInt("p_bookcode");
	
		try { 
			connMgr     = new DBConnectionManager();
		
			sql = "select bookcode, bookname, author, publisher, price, core_contents, book_contents, realfile, savefile, refbook1, refbook2, bookimgurl  \n" 
				+ "from tz_bookinfo																								\n"
				+ "where bookcode = " + SQLString.Format(v_bookcode) + "                                                    		\n";	
		
			ls1         = connMgr.executeQuery(sql);
		
			if ( ls1.next() ) { 
				dbox    = ls1.getDataBox();
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}
		
			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}
	
		return dbox;
	}

	/**
	과정 상세에서 수강신청 받을 수 있는 목록
	@param box      receive from the form object and session
	@return ProposeCourseData
	*/
	public ArrayList selectEduList(RequestBox box) throws Exception { 
		DBConnectionManager connMgr         = null;
		DataBox             dbox            = null;
		ListSet             ls1             = null;
		ArrayList           list1           = null;
		String sql = "";
	
		String v_subj = box.getString("p_subj");
		String ss_userid = box.getSession("userid");
		String ss_comp      = box.getSession("comp");
	
		try { 
			connMgr = new DBConnectionManager();
			list1   = new ArrayList();
		
			sql = "select a.subj, a.year, a.subjseq, a.subjnm, b.appdate propose_date \n"
				+ "     , a.edustart, a.eduend, a.propstart, a.propend, a.isonoff, usebook, bookname \n"
				+ "from   vz_scsubjseq a, tz_propose b \n"
				+ "where  a.subj = b.subj(+) \n"
				+ "and    a.year = b.year(+) \n"
				+ "and    a.subjseq = b.subjseq(+) \n"
				+ "and    b.userid= " + StringManager.makeSQL(ss_userid) + " \n"
				+ "and    substr(to_char(sysdate,'yyyymmddhh24miss'), 1, 10) between substr(propstart, 0, 10) and substr(propend, 0, 10) \n"
				+ "and    a.subj = " + StringManager.makeSQL(v_subj) + " \n"
				+ "and    grcode in ( select grcode from tz_grcomp where comp = " + StringManager.makeSQL(ss_comp) + ") \n"
				+ "and    isuse = 'Y' \n"
				+ "and    seqvisible = 'Y' \n";         
		
			ls1         = connMgr.executeQuery(sql);
		
			while ( ls1.next() ) { 
				dbox    = ls1.getDataBox();
				list1.add(dbox);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}
		
			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}
	
		return list1;
	}
	
	/**
	* 이미 수료한 과정이면 수강신청 안됨.
	* @param connMgr      DBConnectionManager
	* @param p_subj       과정코드
	* @param p_year       과정년도
	* @param p_subjseq    과정기수
	* @param p_userid     유저아이디
	* @param p_comp       회사코드
	* @return result      0 : 정상, -14 : 안됨
	*/
	public int jeyakIsgraduated(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
		ListSet             ls      = null;
		String sql  = "";
		int result = 0;

		try { 
			sql = "\n select decode(sign(count(*)),1,'N','Y') as gu "
				+ "\n from   tz_stold "
				+ "\n where  subj        = " + SQLString.Format(p_subj)
				+ "\n and    userid      = " + SQLString.Format(p_userid)
				+ "\n and    isgraduated = 'Y'";
			
			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				if ( "N".equals(ls.getString("gu"))) result = -14;
			}
		} catch ( SQLException e ) {
			result  = -14;
			ErrorManager.getErrorStackTrace(e, null, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception ex ) { 
			result  = -14;
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;
	}

	/**
	* 다른기수를 학습하고 있는 과정이면 수강신청 안됨.
	* @param connMgr      DBConnectionManager
	* @param p_subj       과정코드
	* @param p_year       과정년도
	* @param p_subjseq    과정기수
	* @param p_userid     유저아이디
	* @param p_comp       회사코드
	* @return result      0 : 정상,  -15 : 안됨
	*/
	public int jeyakStudentYn(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
		ListSet             ls      = null;
		String sql  = "";
		int result = 0;

		try { 
			sql = "\n select decode(sign(count(*)),1,'N','Y') as gu "
				+ "\n from   tz_student a "
				+ "\n      , (select subj "
				+ "\n              , year "
				+ "\n              , subjseq "
				+ "\n              , userid "
				+ "\n         from   tz_stold "
				+ "\n         where  isgraduated = 'N' "
				+ "\n         and    subj   = " + SQLString.Format(p_subj)
				+ "\n         and    userid = " + SQLString.Format(p_userid) + ") b "
				+ "\n where  a.subj    = " + SQLString.Format(p_subj)
				+ "\n and    a.userid  = " + SQLString.Format(p_userid)
				+ "\n and    a.subj    = b.subj(+) "
				+ "\n and    a.year    = b.year(+) "
				+ "\n and    a.subjseq = b.subjseq(+) "
				+ "\n and    a.userid  = b.userid(+) "
				+ "\n and    b.userid is null ";
			
			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				if ( "N".equals(ls.getString("gu"))) result = -15;
			}
		} catch ( SQLException e ) {
			result  = -15;
			ErrorManager.getErrorStackTrace(e, null, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception ex ) { 
			result  = -15;
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;
	}

	/**
	* 다른기수를 신청한 과정이면 수강신청 안됨. 
	* @param connMgr      DBConnectionManager
	* @param p_subj       과정코드
	* @param p_year       과정년도
	* @param p_subjseq    과정기수
	* @param p_userid     유저아이디
	* @param p_comp       회사코드
	* @return result      0 : 정상,  -16 : 안됨
	*/
	public int jeyakProposeYn(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
		ListSet             ls      = null;
		String sql  = "";
		int result = 0;

		try { 
			
			if ( GetCodenm.chkIsSubj(connMgr,p_subj).equals("S")){
				sql = "\n select decode(sign(count(*)),1,'N','Y') as gu "
					+ "\n from   tz_propose a "
					+ "\n      , (select subj "
					+ "\n              , year "
					+ "\n              , subjseq "
					+ "\n              , userid "
					+ "\n         from   tz_stold "
					+ "\n         where  isgraduated = 'N' "
					+ "\n         and    subj   = " + SQLString.Format(p_subj)
					+ "\n         and    userid = " + SQLString.Format(p_userid) + ") b "
					+ "\n where  a.subj    = " + SQLString.Format(p_subj)
					+ "\n and    a.userid  = " + SQLString.Format(p_userid)
					+ "\n and    a.subj    = b.subj(+) "
					+ "\n and    a.year    = b.year(+) "
					+ "\n and    a.subjseq = b.subjseq(+) "
					+ "\n and    a.userid  = b.userid(+) "
					+ "\n and    b.userid is null "
					+ "\n and    a.chkfinal != 'N' ";
			} else {
				sql = "select decode(sign(count(*)),1,'N','Y') as gu	 \n"
					+ "from tz_propose a              \n"
					+ "where exists (                 \n"
					+ "	select 'X'                    \n"
					+ "	from vz_scsubjseq  b          \n"
					+ "	where a.subj = b.subj         \n"
					+ "	and a.year = b.year           \n"
					+ "	and a.subjseq = b.subjseq     \n"
					+ "	and course =" + SQLString.Format(p_subj   )
					+ "	and cyear =" + SQLString.Format(p_year   )
					+ "	and courseseq = " + SQLString.Format(p_subjseq)
					+ "	and userid =  " + SQLString.Format(p_userid )
					+ ")                              \n"
					+ "and a.chkfinal != 'N'          \n";
				
				//System.out.println("sql====\n"+sql);
			}			
			
			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				if ( "N".equals(ls.getString("gu"))) result = -16;
			}
		} catch ( SQLException e ) {
			result  = -16;
			ErrorManager.getErrorStackTrace(e, null, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception ex ) { 
			result  = -16;
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;
	}

	/**
	* 비계열사는 개인별 수강신청 안됨. 
	* @param connMgr      DBConnectionManager
	* @param p_subj       과정코드
	* @param p_year       과정년도
	* @param p_subjseq    과정기수
	* @param p_userid     유저아이디
	* @param p_comp       회사코드
	* @return result      0 : 정상,  -17 : 안됨
	*/
	public int jeyakCompYn(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_comp) throws Exception { 
		ListSet             ls      = null;
		String sql  = "";
		int result = 0;

		try { 
			sql = "\n select decode(sign(count(*)),1,'N','Y') as gu "
				+ "\n from   tz_member a, tz_compclass b "
				+ "\n where  a.userid = " + SQLString.Format(p_userid)
				+ "\n and    a.comp   = b.comp "
				+ "\n and    b.gubun  = '3' ";
			
			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				if ( "N".equals(ls.getString("gu"))) result = -17;
			}
		} catch ( SQLException e ) {
			result  = -17;
			ErrorManager.getErrorStackTrace(e, null, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception ex ) { 
			result  = -17;
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;
	}	

	/**
	 * 직무제한된 과정일 경우 해당 직무인 사람만 신청가능
	 * @param connMgr
	 * @param p_subj
	 * @param p_year
	 * @param p_subjseq
	 * @param p_userid
	 * @return
	 * @throws Exception
	 */
	public int jeyakJikmu(DBConnectionManager connMgr, String p_subj, String p_year, String p_userid) throws Exception { 
		ListSet             ls      = null;
		String sql  = "";
		int result = 0;
		String v_limit = "N";
		
		try {
			sql = "\n select nvl(jikmu_limit,'N') as limit "
				+ "\n from   tz_subj "
				+ "\n where  subj = " + StringManager.makeSQL(p_subj);
			
			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				v_limit = ls.getString("limit");
			}
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			
			if ("Y".equals(v_limit)) {
				sql = "\n select decode(sign(count(*)),1,'Y','N') as gu "
					+ "\n from   tz_subjjikmu t1 "
					+ "\n      , tz_member t2 "
					+ "\n where  t1.job_cd = t2.job_cd "
					+ "\n and    t1.subj = " + StringManager.makeSQL(p_subj)
					+ "\n and    t1.year = " + StringManager.makeSQL(p_year)
					+ "\n and    t2.userid = " + StringManager.makeSQL(p_userid);
				
				ls = connMgr.executeQuery(sql);
				if ( ls.next() ) { 
					if ( "N".equals(ls.getString("gu"))) result = -18;
				}
			}
			
		} catch ( SQLException e ) {
			result  = -18;
			ErrorManager.getErrorStackTrace(e, null, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception ex ) { 
			result = -18;
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}

		return result;
	}

	public ArrayList selectAttendCd(RequestBox box) throws Exception { 
		DBConnectionManager connMgr         = null;
		DataBox             dbox            = null;
		ListSet             ls             = null;
		ListSet             ls1             = null;
		ArrayList           list1           = null;
		StringBuffer strSQL = null;
		String sql="";
	
		String v_subj = box.getString("p_subj");
		String v_subjseq = box.getString("p_subjseq");
		String ss_userid = box.getSession("userid");
		String ss_comp      = box.getSession("comp");
		
		String v_edustart = box.getString("p_edustart");
		String v_eduend = box.getString("p_eduend");
	    String v_neweroom ="";
	    String v_neweroom2 ="";
	    
	    String v_neweroom3 [] = null;
		
		try { 
			connMgr = new DBConnectionManager();
			list1   = new ArrayList();
			
			sql ="select neweroom from tz_subjseq  where subj ='"+v_subj+"' and subjseq='"+v_subjseq+"' and edustart='"+v_edustart+"' and eduend='"+v_eduend+"'  ";
			
			System.out.println("sql======>"+sql);
			
			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				v_neweroom = ls.getString(1);
			}
			ls.close();
			
			if(!"".equals(v_neweroom)){
				
				v_neweroom2 = v_neweroom.substring(0,(v_neweroom.length()-1));
			
				
			}
			
			
			if("".equals(v_neweroom)){
			
					strSQL = new StringBuffer();
					
					strSQL.append("\n select '['||low_edumin||']'||school_nm codenm, seq code ") ;
					strSQL.append("\n   from TZ_ATTEND_CD ") ;
					strSQL.append("\n  where ISUSE = 'Y' ") ;
				if(!"0008".equals(v_subjseq) && !"0009".equals(v_subjseq)){	// 8,9기일때만 순천금당중학교 리스트 뿌려주도록 2010-05-18
					strSQL.append("\n  and seq <> 50 ") ;
				}
					strSQL.append("\n  order by seq ") ;
		System.out.println("---------고사장1---" +strSQL.toString() );			
					ls1 = connMgr.executeQuery(strSQL.toString());
				
					while ( ls1.next() ) { 
						dbox = ls1.getDataBox();
						list1.add(dbox);
					}
			}else{
				
				strSQL = new StringBuffer();
				
				strSQL.append("\n select '['||low_edumin||']'||school_nm codenm, seq code ") ;
				strSQL.append("\n   from TZ_ATTEND_CD ") ;
				strSQL.append("\n  where ISUSE = 'Y' ") ;
				strSQL.append("\n  and seq in ("+v_neweroom2+" ) ");
				strSQL.append("\n  order by seq ") ;
				ls1 = connMgr.executeQuery(strSQL.toString());
				System.out.println("---------고사장2---" +strSQL.toString() );			
				while ( ls1.next() ) { 
					dbox = ls1.getDataBox();
					list1.add(dbox);
				}
				
			}
			
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, strSQL.toString());
			throw new Exception("\n SQL : [\n" + strSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}
		
			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}
	
		return list1;
	}

	public String MakeOrderNo(RequestBox box) throws Exception{
		DBConnectionManager	connMgr	    = null;
		ListSet             ls1         = null;
		String              sql         = "";
		String             order_id        = "";
		
		try { 
			connMgr     = new DBConnectionManager();
			//밀리세컨드까지를 아이디로 본다..
			sql = "SELECT TO_CHAR(SYSTIMESTAMP,'YYYYMMDDHH24MISSFF') AS ORDER_ID FROM DUAL ";
			ls1 = connMgr.executeQuery(sql);
		
			if( ls1.next() ) { 
				order_id = ls1.getString("order_id");
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}
		
			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}
	
		return order_id;
	}
	public int insertPGData(RequestBox box) throws Exception{
		DBConnectionManager connMgr = null;
		PreparedStatement   pstmt   = null;
		String strSQL = "";
		int isOk = 0;
		

		String p_oid = box.getString("p_oid");//주문번호
		String p_userid = box.getString("p_userid");//아이디
		String p_subj = box.getString("p_subj");//과목코드
		String p_subjseq = box.getString("p_subjseq");//차수
		String p_pay_sel = box.getString("p_pay_sel");//결제방법
		String p_tid = box.getString("p_tid");//거래번호
		String p_amount = box.getString("p_amount");//금액
		String p_cardnumber = box.getString("p_cardnumber");//카드번호
		String p_financename = box.getString("p_financename");//카드종류(이름)
		String p_cardperiod = box.getString("p_cardperiod");//할부개월수
		String p_financecode = box.getString("p_financecode");//카드종류(숫자)
		String p_authnumber = box.getString("p_authnumber");//승인번호
		//승인일자
		String p_respcode = box.getString("p_respcode");//응답코드
		//결제년도
		
//		System.out.println("##################################################################################");
//		System.out.println("##################################################################################");
//		System.out.println("##################################################################################");
//		System.out.println("##################################################################################");
//		System.out.println("##################################################################################");
//		System.out.println("p_oid 			::::::"+p_oid 				);
//		System.out.println("p_userid 		::::::"+p_userid 		  );
//		System.out.println("p_subj 			::::::"+p_subj 			  );
//		System.out.println("p_subjseq 		::::::"+p_subjseq 		);
//		System.out.println("p_pay_sel 		::::::"+p_pay_sel 		);
//		System.out.println("p_tid			::::::"+p_tid				  );
//		System.out.println("p_amount 		::::::"+p_amount 		  );
//		System.out.println("p_cardnumber  	::::::"+p_cardnumber  );
//		System.out.println("p_financename 	::::::"+p_financename );
//		System.out.println("p_cardperiod  	::::::"+p_cardperiod  );
//		System.out.println("p_financecode 	::::::"+p_financecode );
//		System.out.println("p_authnumber	::::::"+p_authnumber	);
//		System.out.println("p_respcode 	  	::::::"+p_respcode 	  );
//		System.out.println("##################################################################################");
//		System.out.println("##################################################################################");
//		System.out.println("##################################################################################");
//		System.out.println("##################################################################################");
		
		int preIdx = 1;
		
		try { 
			connMgr = new DBConnectionManager();
			
			strSQL = " insert into pa_payment(" +
				 	 " order_id,userid,leccode,lecnumb,type," +
					 " transaction_id,amount,card_no,card_nm, card_period," +
					 " card_type,auth_no, auth_date,response_code,year ) " +
					 " values(?,?,?,?,?," +
					 "        ?,?,?,?,?," +
					 "        ?,?,to_char(sysdate,'yyyymmddhh24miss'),?,to_char(sysdate,'yyyy'))";
			
			pstmt = connMgr.prepareStatement(strSQL.toString());			
			
			preIdx = 1;			
			pstmt.setString(preIdx++, p_oid);
			pstmt.setString(preIdx++, p_userid);
			pstmt.setString(preIdx++, p_subj);
			pstmt.setString(preIdx++, p_subjseq);
			pstmt.setString(preIdx++, p_pay_sel);
			pstmt.setString(preIdx++, p_tid);
			pstmt.setString(preIdx++, p_amount);
			pstmt.setString(preIdx++, p_cardnumber);
			pstmt.setString(preIdx++, p_financename);
			pstmt.setString(preIdx++, p_cardperiod);
			pstmt.setString(preIdx++, p_financecode);
			pstmt.setString(preIdx++, p_authnumber);
			pstmt.setString(preIdx++, p_respcode);
			
			isOk = pstmt.executeUpdate();
			
		}
		
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return isOk;
	}

	
	public DataBox selectAttendInfo(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	    = null;
		DataBox             dbox        = null;
		ListSet             ls1         = null;
		String              sql         = "";
		
		String v_subj = box.getString("p_subj"); 
		String v_year = box.getString("p_year"); 
		String v_subjseq = box.getString("p_subjseq"); 
		String s_userid = box.getSession("userid"); 
	
		try { 
			connMgr     = new DBConnectionManager();
		
			sql = "select lec_sel_no, is_attend  \n" 
				+ " from tz_propose		\n"
				+ "where subj = "+StringManager.makeSQL(v_subj)+"	 \n"	
			    + "  and year = "+StringManager.makeSQL(v_year)+"	 \n"	
			    + "  and subjseq = "+StringManager.makeSQL(v_subjseq)+" 	\n"	
		     	+ "  and userid = "+StringManager.makeSQL(s_userid)+" 	\n";	
		
			ls1         = connMgr.executeQuery(sql);
		
			while ( ls1.next() ) { 
				dbox    = ls1.getDataBox();
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}
		
			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}
	
		return dbox;
	}
	
	
	
	
	
	public ArrayList selectPayCd(RequestBox box) throws Exception { 
		DBConnectionManager connMgr         = null;
		DataBox             dbox            = null;
		ListSet             ls1             = null;
		ArrayList           list1           = null;
		StringBuffer strSQL = null;
	
		      String  v_user_id   = box.getSession("userid");
	         String v_subj = box.getString("p_subj");
	         String v_year = box.getString("p_year");
	         String v_subjseq = box.getString("p_subjseq");
	
		try { 
			connMgr = new DBConnectionManager();
			list1   = new ArrayList();
		
			strSQL = new StringBuffer();
			
			strSQL.append("\n select order_id,leccode,type,lecnumb,TRANSACTION_ID from pa_payment where userid="+StringManager.makeSQL(v_user_id)+" and leccode="+StringManager.makeSQL(v_subj)+" and year="+StringManager.makeSQL(v_year)+"  ") ;
		
			ls1 = connMgr.executeQuery(strSQL.toString());
			System.out.println(strSQL);
			
			
			while ( ls1.next() ) { 
				dbox = ls1.getDataBox();
				list1.add(dbox);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, strSQL.toString());
			throw new Exception("\n SQL : [\n" + strSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}
		
			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}
	
		return list1;
	}
	
	
	public int UpdateSubjPropose(RequestBox box) throws Exception { 
		DBConnectionManager connMgr = null;
		PreparedStatement   pstmt   = null;
		StringBuffer strSQL = null;
		int isOk = 0;
		

		String v_subj = box.getString("p_subj");
		String v_year = box.getString("p_year");
		String v_subjseq = box.getString("p_subjseq");
		String v_lec_sel_no = box.getString("p_lec_sel_no");
		String v_is_attend = box.getString("p_is_attend");
		String s_userid = box.getSession("userid");
		
		int preIdx = 1;
		
		try { 
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			strSQL = new StringBuffer();
			
			strSQL.append(" update tz_propose set lec_sel_no = ? ");
			strSQL.append("                     , is_attend = ? ");
			strSQL.append("                     , is_attend_dt = to_char(sysdate, 'YYYYDDMMHH24MISS') ");
			strSQL.append(" where subj = ? ");
			strSQL.append("   and year = ? ");
			strSQL.append("   and subjseq = ? ");
			strSQL.append("   and userid = ? ");
			
			pstmt = connMgr.prepareStatement(strSQL.toString());
			
			preIdx = 1;
			
			pstmt.setString(preIdx++, v_lec_sel_no);
			pstmt.setString(preIdx++, v_is_attend);
			pstmt.setString(preIdx++, v_subj);
			pstmt.setString(preIdx++, v_year);
			pstmt.setString(preIdx++, v_subjseq);
			pstmt.setString(preIdx++, s_userid);
			
			isOk = pstmt.executeUpdate();
		}
		
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return isOk;
	}
	
	
	public int UpdateLecselOn(RequestBox box) throws Exception { 
		DBConnectionManager connMgr = null;
		PreparedStatement   pstmt   = null;
		StringBuffer strSQL = null;
		int isOk = 0;
		

		String v_subj = box.getString("p_subj");
		String v_year = box.getString("p_year");
		String v_subjseq = box.getString("p_subjseq");
		String v_lec_sel_no = box.getString("p_lec_sel_no");
		String v_is_attend = box.getString("p_is_attend");
		String s_userid = box.getSession("userid");
		
		int preIdx = 1;
		
		try { 
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			strSQL = new StringBuffer();
			
			strSQL.append(" update tz_propose set lec_sel_no = ? ");
			strSQL.append("                     , is_attend = ? ");
			strSQL.append("                     , is_attend_dt = to_char(sysdate, 'YYYYDDMMHH24MISS') ");
			strSQL.append(" where subj = ? ");
			strSQL.append("   and year = ? ");
			strSQL.append("   and subjseq = ? ");
			strSQL.append("   and userid = ? ");
			
			pstmt = connMgr.prepareStatement(strSQL.toString());
			
			preIdx = 1;
			
			pstmt.setString(preIdx++, v_lec_sel_no);
			pstmt.setString(preIdx++, v_is_attend);
			pstmt.setString(preIdx++, v_subj);
			pstmt.setString(preIdx++, v_year);
			pstmt.setString(preIdx++, v_subjseq);
			pstmt.setString(preIdx++, s_userid);
			
			isOk = pstmt.executeUpdate();
		}
		
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return isOk;
	}
	public int UpdateStatus(RequestBox box) throws Exception { 
		DBConnectionManager connMgr = null;
		PreparedStatement   pstmt   = null;
		StringBuffer strSQL = null;
		int isOk = 0;
		

		String v_subj = box.getString("p_subj");
		String v_year = box.getString("p_year");
		String v_subjseq = box.getString("p_subjseq");
		String s_userid = box.getSession("userid");
		
		int preIdx = 1;
		
		try { 
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			strSQL = new StringBuffer();
			
			strSQL.append(" update TZ_BOOKDELIVERY			");
			strSQL.append("         set DELIVERY_STATUS='F' ");
			strSQL.append(" where subj = ? ");
			strSQL.append("   and year = ? ");
			strSQL.append("   and subjseq = ? ");
			strSQL.append("   and userid = ? ");
			
			pstmt = connMgr.prepareStatement(strSQL.toString());
			
			preIdx = 1;
			
			pstmt.setString(preIdx++, v_subj);
			pstmt.setString(preIdx++, v_year);
			pstmt.setString(preIdx++, v_subjseq);
			pstmt.setString(preIdx++, s_userid);
			
			isOk = pstmt.executeUpdate();
		}
		
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return isOk;
	}
	
	public ArrayList selectCashPrint(RequestBox box) throws Exception { 
		DBConnectionManager connMgr         = null;
		DataBox             dbox            = null;
		ListSet             ls1             = null;
		ArrayList           list1           = null;
		StringBuffer strSQL = null;
	
	
		String ss_userid = box.getSession("userid");
		String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
	
		try { 
			connMgr = new DBConnectionManager();
			list1   = new ArrayList();
		
			strSQL = new StringBuffer();
			
			strSQL.append("\n select                                                                                                                                                    ");
			strSQL.append("\n  (select mm.name from tz_member mm where  tst.userid = mm.userid) as uname                                                                                ");
			strSQL.append("\n ,(select mm.user_path from tz_member mm where  tst.userid = mm.userid) as upass                                                                           ");
			strSQL.append("\n ,(select fn_crypt('2', mm.birth_date, 'knise') birth_date from tz_member mm where  tst.userid = mm.userid) as ujumin                                                  ");
			strSQL.append("\n ,tsj.subjnm                                                                                                                                               ");
			//strSQL.append("\n ,(select proptxt from  tz_propose p where tst.subj = p.subj and tst.year = p.year and tst.subjseq = p.subjseq and tst.userid = p.userid) as lecselno      ");
			strSQL.append("\n ,substr(tsj.subjclass,0,3) subjclassnm                                                                                                                                               ");
			strSQL.append("\n ,tsj.biyong                                                                                                                                               ");
			strSQL.append("\n ,tss.edustart, tss.eduend                                                                                                                                 ");
			strSQL.append("\n ,(select appdate from  tz_propose p where tst.subj = p.subj and tst.year = p.year and tst.subjseq = p.subjseq and tst.userid = p.userid) as appdate       ");   
			strSQL.append("\n ,(select mm.cert from tz_member mm where  tst.userid = mm.userid) as cert                                                                                ");
			strSQL.append("\n from  tz_student tst, tz_subj tsj, vz_scsubjseq tss                                                                                                       ");
			strSQL.append("\n where tst.subj     = "+StringManager.makeSQL(v_subj)+"       ");
			strSQL.append("\n  and tst.subjseq     = "+StringManager.makeSQL(v_subjseq)+"                        ");
			strSQL.append("\n  and tst.year     = "+StringManager.makeSQL(v_year)+"      ");
			strSQL.append("\n  and tst.userid     = "+StringManager.makeSQL(ss_userid)+"    ");
			strSQL.append("\n  and tst.subj            = tsj.subj                                                                                                                       ");
			strSQL.append("\n  and tst.subj          = tss.subj                                                                                                                         ");
			strSQL.append("\n  and tst.year          = tss.year                                                                                                                         ");
			strSQL.append("\n  and tst.subjseq       = tss.subjseq                                                                                                                      ");
			strSQL.append("\n order by tss.course, tss.edustart desc , tss.subjnm                                                                                                       ");
			
			//System.out.println("===>영수증\n"+strSQL.toString());
		
			ls1 = connMgr.executeQuery(strSQL.toString());
		
			while ( ls1.next() ) { 
				dbox = ls1.getDataBox();
				list1.add(dbox);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, strSQL.toString());
			throw new Exception("\n SQL : [\n" + strSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls1 != null ) { 
				try { 
					ls1.close();  
				} catch ( Exception e ) { } 
			}
		
			if ( connMgr != null ) { 
				try { 
					connMgr.freeConnection(); 
				} catch ( Exception e ) { } 
			}
		}
	
		return list1;
	}
	
	//결제 승인시 수강승인처리 되도록 
	public int updateApprovalData(RequestBox box) throws Exception { 
		DBConnectionManager connMgr = null;
		PreparedStatement   pstmt   = null;
		String sql = "";
		int isOk = 0;
		

		String v_subj = box.getString("p_subj");
		String v_year = box.getString("p_year");
		String v_subjseq = box.getString("p_subjseq");
		String v_userid = box.getString("p_userid");
		String v_comp = box.getString("p_comp");
		
		int preIdx = 1;
		
		try { 
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			PreparedStatement pstmt1 = null;
			//신청 테이블 먼저 업데이트
			sql =" update tz_propose set chkfinal = 'Y', ldate=to_char(sysdate,'yyyymmddhh24miss'), luserid= ? ";
			sql+=" where subj= ?  and year = ?  and subjseq = ? and userid = ? ";
			pstmt1 = connMgr.prepareStatement(sql);
			int index = 1;
			pstmt1.setString(index++, v_userid);
			pstmt1.setString(index++, v_subj);
			pstmt1.setString(index++, v_year);
			pstmt1.setString(index++, v_subjseq);
			pstmt1.setString(index++, v_userid);
			
			isOk = pstmt1.executeUpdate();
			//북테이블도 업뎃
			if(isOk > 0){
				sql =" update TZ_PROPOSEBOOK set status = 'W' ";
				sql+=" where subj= ?  and year = ?  and subjseq = ? and userid = ? ";
				pstmt1 = connMgr.prepareStatement(sql);
				index = 1;
				pstmt1.setString(index++, v_subj);
				pstmt1.setString(index++, v_year);
				pstmt1.setString(index++, v_subjseq);
				pstmt1.setString(index++, v_userid);
				
				pstmt1.executeUpdate();
			}
			
			//student 테이블 insert
			if(isOk > 0){
				sql  ="insert into tz_student ( " ;
		        sql +=" subj,        year,     subjseq,     userid,    ";
		        sql +=" class,       comp,     isdinsert,   score,     ";
		        sql +=" tstep,       mtest,    ftest,       report,    ";
		        sql +=" act,         etc1,     etc2,        avtstep,   ";
		        sql +=" avmtest,     avftest,  avreport,    avact,     ";
		        sql +=" avetc1,      avetc2,   isgraduated, isrestudy, ";
		        sql +=" isb2c,       edustart, eduend,      branch,    ";
		        sql +=" confirmdate, eduno,    luserid,     ldate,     ";
		        sql +=" stustatus )  ";
		        sql +=" values ( ";
		        sql +=" ?, ?, ?, ?, ";
		        sql +=" ?, ?, ?, ?, ";
		        sql +=" ?, ?, ?, ?, ";
		        sql +=" ?, ?, ?, ?, ";
		        sql +=" ?, ?, ?, ?, ";
		        sql +=" ?, ?, ?, ?, ";
		        sql +=" ?, ?, ?, ?, ";
		        sql +=" ?, ?, ?, ?, ";
		        sql +=" ?) ";
		        pstmt1 = connMgr.prepareStatement(sql);
		       
		        index = 1;
		        pstmt1.setString(1, v_subj);
				pstmt1.setString(2, v_year);
				pstmt1.setString(3, v_subjseq);
				pstmt1.setString(4, v_userid);
				
				pstmt1.setString( 5, "0001");   // v_class
				pstmt1.setString( 6, v_comp);
				pstmt1.setString( 7, "Y");   // v_isdinsert
				pstmt1.setDouble( 8, 0);     // v_score
				
				pstmt1.setDouble( 9, 0);     // v_tstep
				pstmt1.setDouble(10, 0);     // v_mtest
				pstmt1.setDouble(11, 0);     // v_ftest
				pstmt1.setDouble(12, 0);     // v_report
				
				pstmt1.setDouble(13, 0);     // v_act
				pstmt1.setDouble(14, 0);     // v_etc1
				pstmt1.setDouble(15, 0);     // v_etc2
				pstmt1.setDouble(16, 0);     // v_avtstep

				pstmt1.setDouble(17, 0);     // v_avmtest
				pstmt1.setDouble(18, 0);     // v_avftest
				pstmt1.setDouble(19, 0);     // v_avreport
				pstmt1.setDouble(20, 0);     // v_avact

				pstmt1.setDouble(21, 0);     // v_avetc1
				pstmt1.setDouble(22, 0);     // v_avetc2
				pstmt1.setString(23, "N");   // v_isgraduated
				pstmt1.setString(24, "N");   // v_isrestudy)
				
				pstmt1.setString(25, "N");
				pstmt1.setString(26, "");
				pstmt1.setString(27, "");
				pstmt1.setInt(28, 99);  // v_branch

				pstmt1.setString(29, "");    // v_confirmdate
				pstmt1.setInt   (30, 0);     // v_eduno
				pstmt1.setString(31, v_userid);
				pstmt1.setString(32, FormatDate.getDate("yyyyMMddHHmmss") ); // ldate

				pstmt1.setString(33, "Y"); // stustatus
				isOk = pstmt1.executeUpdate();
			}
			
			if(isOk > 0){
				connMgr.commit();
			}
		}
		
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql.toString());
			throw new Exception("sql = " + sql.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return isOk;
	}
	
	
	
	public ArrayList selectMemberAddr(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		ListSet ls1             = null;
		DataBox dbox 						= null;
		ArrayList list1         = null;
		String sql1             = "";
		
		String v_userid 	= box.getSession("userid");
		
		try {
			connMgr = new DBConnectionManager();
			list1 = new ArrayList();
			
			sql1 =  " select hrdc,zip_cd,address,zip_cd1,address1 from tz_member  where userid ='"+v_userid+"' ";
			
			//System.out.println(sql1);
			
			
			ls1 = connMgr.executeQuery(sql1);
			while(ls1.next()) {
				dbox = ls1.getDataBox();
				
				list1.add(dbox);
			}
		}
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
		}
		finally {
			if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return list1;
	}
	
	public int modifyUserPop(RequestBox box) throws Exception {
        DBConnectionManager connMgr  = null;
        PreparedStatement   pstmt    = null;
        
        String              sql      = "";
        int                 isOk     = 0;
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
                        
            String    userid          = box.getSession("userid");
            String    post1           = box.getString("p_post1").trim();
            String    post2           = box.getString("p_post2").trim();
            String	  post			  = post1 + "-" + post2;
            String    address1        = box.getString("p_address1");
            String    hrdc		      = box.getString("p_hrdc2"); 
            
        /*    System.out.println("userid==========>"+userid);
            System.out.println("post1==========>"+post1);
            System.out.println("post2==========>"+post2);
            System.out.println("post==========>"+post);
            System.out.println("address1==========>"+address1);
            System.out.println("hrdc==========>"+hrdc);*/

             
            sql ="update TZ_MEMBER set  \n";
            
            if( "C".equals(hrdc)){
            	sql+=" zip_cd1 = ?, address1 = ?, hrdc=?  \n";
            }else{
            	sql+=" zip_cd = ?, address = ? , hrdc=?  \n";
            }
            
            sql+=" where userid = ? \n";
//     System.out.println("sql----주소 업데이트::::" + sql);       
            pstmt = connMgr.prepareStatement(sql);
  
            int params = 1;
           
            pstmt.setString(params++, post);
            pstmt.setString(params++, address1);
            pstmt.setString(params++, hrdc);
            pstmt.setString(params++, userid);
            
             isOk  = pstmt.executeUpdate();
            
            if ( isOk > 0 ) {    connMgr.commit();      }
            else            {    connMgr.rollback();    }
            
        } catch ( Exception ex ) {
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e1 )  { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e10 ) { } }
        }
        
        return isOk;        
    }
	
	

}