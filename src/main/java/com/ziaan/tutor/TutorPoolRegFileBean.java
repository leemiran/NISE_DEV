//**********************************************************
//  1. 제      목: 강사Pool개인정보등록 - Excel일괄등록 (인재개발원 추가)
//  2. 프로그램명 : TutorPoolRegFileBean.java
//  3. 개      요: 강사Pool개인정보등록 - Excel일괄등록
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 원영탁 2008. 8. 28
//  7. 수      정:
//**********************************************************

package com.ziaan.tutor;

import javax.servlet.http.*;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.net.*;

import com.ziaan.library.*;

/**
 * 코드 관리(ADMIN)
 *
 * @date   : 2008. 8
 * @author : s.j Jung
 */
public class TutorPoolRegFileBean {

  public TutorPoolRegFileBean() {}

  
   
     /**
     강사 등록 (Excel 일괄등록)
     @param box      receive from the form object and session
     @return isOk    1:update success,0:update fail
     */
      public int insertTutor(Hashtable data, RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         PreparedStatement   pstmt     = null;
         StringBuffer        sbSQL     = new  StringBuffer();
         
         StringBuffer        sql = new StringBuffer();
         int isOk = 1;
         int idx = 1;
         int cnt = 0;
         
         int v_result = 0;
        
         ListSet ls = null;
        
         String s_userid = box.getSession("userid");
  
         /*
         String v_ismanager = (String)data.get("ismanager");				//강사권한여부
         String v_isgubuntype = (String)data.get("isgubuntype");			//강사구분
         String v_tutorgrade = (String)data.get("tutorgrade");				//강사등급코드       (1-공무원  2-일반)
         */
         
         String v_seq = "";													//강사관리번호
         String v_name = (String)data.get("name");							//성명
  
         
         String v_birth_date = (String)data.get("birth_date");						//주민등록번호
         String v_userid = (String)data.get("userid");						//아이디
         //String v_pwd = (String)data.get("pwd");							//비밀번호
         String v_lessonGubunCode = (String)data.get("lessonCodes");		//과목분류코드 (강의분야: ,로 구분 )
      
         String v_post = (String)data.get("post1");						    //우편번호 (자택)
         String vv_post1 = "";
         String vv_post2 = "";
         
         String v_addr = (String)data.get("addr");							//주소 (자택)
         String v_addr2 = (String)data.get("addr2");						//상세주소 (자택)
         
         String v_corppost = (String)data.get("corppost");					//우편번호 (직장)
         String vv_corppost1 = "";
         String vv_corppost2 = "";
       
         String v_corpaddr = (String)data.get("corpaddr");					//주소 (직장)
         String v_corpaddr2 = (String)data.get("corpaddr2");				//상세주소 (직장)
         
         //String v_deptnam = (String)data.get("deptnam");					//현소속 (하위부서)
         String v_comp = (String)data.get("comp");   						//현소속
         String v_corptel = (String)data.get("corptel");                    //직장전화
         String v_fax = (String)data.get("fax");                            //팩스
         String v_handphone = (String)data.get("handphone");				//휴대전화
         String v_bank = (String)data.get("bank");							//은행명
         String v_account = (String)data.get("account");					//계좌번호
         String v_email = (String)data.get("email");						//이메일
         String v_academic = (String)data.get("academic");					//학력(학위)
         String v_career = (String)data.get("career");						//주요강의과목
         String v_book = (String)data.get("book");							//저서/논문
    
         String v_tutorInOutGubun = (String)data.get("tutorinoutgubun");	//강사구분(내,외구분)
         String v_tutorgrade = (String)data.get("tutorgrade");				//강사등급
         String v_ismanager = (String)data.get("ismanager");				//강사권한여부
         String v_jikup = (String)data.get("jikup");
         
         String v_etc = (String)data.get("etc");                            //기타사항
 
         if(!"".equals(v_post)){
        	 String v_postArr[] = v_post.split("-");
        	 vv_post1 = v_postArr[0];
        	 vv_post2 = v_postArr[1];
         }
         
         if(!"".equals(v_corppost)){
        	 String v_corppostArr[] = v_corppost.split("-");
        	 vv_corppost1 = v_corppostArr[0];
        	 vv_corppost2 = v_corppostArr[1];
         }
                
         String v_isMemberYn = "N";										    //회원여부구분
                  
         try { 
         
        	 connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false);

            
             //강사일련번호      		
      		sbSQL.append("select lpad(max(to_number(nvl(seq, 0))) + 1,8,0) maxseq from TZ_TUTOR_FORMER ");
      		
      		ls = connMgr.executeQuery(sbSQL.toString());
      		
      		if(ls.next()){      				
      			v_seq = ls.getString("maxseq");      			
      		}	     		
            sbSQL.setLength(0); 
      		
             //강사테이블(TZ_TUTOR) 등록
             
              sbSQL.append("INSERT INTO TZ_TUTOR_FORMER  (            						\n")  
             		.append("  SEQ, USERID, NAME, SEX, POST1, POST2   						\n")
             		.append(", ADD1, ADD2, PHONE, HANDPHONE, FAX   							\n")
             		.append(", EMAIL, COMPCD, COMP, DEPT, JIK   							\n")
             		.append(", ACADEMIC, MAJOR, ISADD, ISCYBER, ISGUBUN    					\n")
             		.append(", ISGUBUNTYPE, ISSTATUS, ISTUTOR, CAREERYEAR, LICENSE   		\n")
             		.append(", CAREER, BOOK, GRCODE, PROFESSIONAL, CHARGE 					\n")
             		.append(", ISINFO, ETC, PHOTOTERMS, INDATE, LUSERID   					\n")
             		.append(", LDATE, birth_date, UPPERCLASS, MIDDLECLASS, LOWERCLASS   			\n")
             		.append(", SUBJCLASS, TUTORGUBUN, MAJORBOOK, INTRO, ISMANAGER     		\n")
             		.append(", AESID, HOMEPOST1, HOMEPOST2, HOMEADD1, HOMEADD2     			\n")
             		.append(", HOMEPHONE, REALFILE, SAVEFILE, DELIBERATION, TUTORGRADE     	\n")
             		.append(", BANK, ACCOUNT, CHINESE_NAME, PHOTO, CARNO     				\n")
             		.append(", CARGUBUN, SCHOOLGRADE, OILGRADE, JOB     					\n")
             		.append(", CORPPOST1, CORPPOST2, CORPADD1, CORPADD2     				\n")
             		.append(", INOUTGUBUN, JIKUPNM   				                                \n")
             		.append(")values(                                                       \n")
             		.append("?,?,?,?,?,?                         \n")
             		.append(",?,?,?,?,?                                                     \n")
             		.append(",?,?,?,?,?                                                     \n")
             		.append(",empty_clob(),?,?,?,?                                                     \n")
             		.append(",?,?,?,?,?                                                     \n")
             		.append(",empty_clob(),empty_clob(),?,?,?                                                     \n")
             		.append(",?,?,?,to_char(sysdate, 'YYYYMMDD'),?                  \n")
             		.append(",to_char(sysdate, 'YYYYMMDDHH24MISS'),pkg_secu.encrypt(" + SQLString.Format(v_birth_date) +  "),?,?,?  \n")
             		.append(",?,?,?,?,?                                                     \n")
             		.append(",?,?,?,?,?                                                     \n")
             		.append(",?,?,?,?,?                                                     \n")
             		.append(",?,?,?,?,?                                                     \n")
             		.append(",?,?,?,?                                                       \n")
             		.append(",?,?,?,?                                                       \n")
              		.append(",?,? )                                                           \n");
              
             		pstmt = connMgr.prepareStatement(sbSQL.toString());
             	
             		idx = 1;
             		
             		
             		//시퀀스 추가             		
             		pstmt.setString(idx++, v_seq );
             		
             		pstmt.setString(idx++, v_userid );
             		pstmt.setString(idx++, v_name );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, vv_post1 );
             		pstmt.setString(idx++, vv_post2 );
             		pstmt.setString(idx++, v_addr );
             		pstmt.setString(idx++, v_addr2 );
             		pstmt.setString(idx++, v_corptel );
             		pstmt.setString(idx++, v_handphone );
             		pstmt.setString(idx++, v_fax );
             		
             		pstmt.setString(idx++, v_email );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, v_comp );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		
             		//pstmt.setString(idx++, v_academic );
             		
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "2" );
             		
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		//pstmt.setString(idx++, v_career );
             		//pstmt.setString(idx++, v_book );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             	
             		
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, v_etc );
             		pstmt.setString(idx++, "" );
             		//pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, s_userid );
             		//pstmt.setString(idx++, "" );
             		//pstmt.setString(idx++, v_birth_date ); 
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             	
             		
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "N" );        //강사권한 (ISMANAGER)
             		pstmt.setString(idx++, "" );
             		
             		pstmt.setString(idx++, vv_post1 );
             		pstmt.setString(idx++, vv_post2 );
             		pstmt.setString(idx++, v_addr );
             		pstmt.setString(idx++, v_addr2 );
             		
             		
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, v_tutorgrade );
             		pstmt.setString(idx++, v_bank );
             		pstmt.setString(idx++, v_account );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             		pstmt.setString(idx++, "" );
             	
             		pstmt.setString(idx++, vv_corppost1 );
             		pstmt.setString(idx++, vv_corppost2 );
             		pstmt.setString(idx++, v_corpaddr );
             		pstmt.setString(idx++, v_corpaddr2 );
             		
             		pstmt.setString(idx++, v_tutorInOutGubun );
             		pstmt.setString(idx++, v_jikup );
             		//pstmt.setString(idx++, v_tutorgrade );
             		//pstmt.setString(idx++, v_ismanager );
             		
             		isOk = pstmt.executeUpdate();

             		if(pstmt != null){ pstmt.close(); }
             		
             		sbSQL.setLength(0);
             		
             		//학력입력
             			sql.append("select academic from tz_tutor_former where seq='" +v_seq+ "'\n");
             			connMgr.setOracleCLOB(sql.toString(), v_academic);
             			sql.setLength(0);
             		//경력입력
             			sql.append("select career from tz_tutor_former where seq='" +v_seq+ "'\n");
             			connMgr.setOracleCLOB(sql.toString(), v_career);
             			sql.setLength(0);
             		
             		//저서입력
             			sql.append("select book from tz_tutor_former where seq='" +v_seq+ "'\n");
             			connMgr.setOracleCLOB(sql.toString(), v_book);
             			sql.setLength(0);
             		
             	
             		
             		// -기존과목유형 삭제
                   
                    sbSQL.append("DELETE  FROM TZ_SUBJMAN								\n")
            	     	 .append("where   seq        = ? 								\n");
                    
                    pstmt = connMgr.prepareStatement(sbSQL.toString());
               
                    idx = 1;
               		
                    pstmt.setString(idx++, v_seq );
                    pstmt.executeUpdate();
                    if(pstmt != null){ pstmt.close(); }
                    sbSQL.setLength(0);    		
        
             		
         //강의과목유형 등록 (TZ_SUBJMAN)
             	
         if(!"".equals(v_lessonGubunCode)){               //엑셀파일에 입력값이 있을 경우
             	
        	 
        	 String v_lessonGubunArr[] = StringManager.stringSplit(v_lessonGubunCode,",");
        	 
             		sbSQL.append("INSERT INTO TZ_SUBJMAN  (            	\n")  
             		.append(" SEQ 									\n")
             		.append(", GADMIN   								\n")
             		.append(", LESSONCLASS   							\n")
             		.append(", LUSERID    								\n")
             		.append(", LDATE   									\n")
             		.append(")values(                                  	\n")
             		.append("?                                          \n")
             		.append(",?                                         \n")
             		.append(",?                                         \n")
             		.append(",?                                         \n")
             		.append(",to_char(sysdate, 'YYYYMMDDHH24MISS'))     \n");
             		
             		
             		pstmt = connMgr.prepareStatement(sbSQL.toString());
             	
             	for (int i=0;i<v_lessonGubunArr.length;i++){	
             		
             		idx = 1;
		
             		pstmt.setString(idx++, v_seq );
             		pstmt.setString(idx++, "P01" );
             		pstmt.setString(idx++, v_lessonGubunArr[i] );
             		pstmt.setString(idx++, s_userid );
             		
             		isOk = pstmt.executeUpdate();
             	}       
             	if(pstmt != null){ pstmt.close(); }
             		sbSQL.setLength(0);
             
         } 
          
       		if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
             
             if ( isOk > 0) connMgr.commit();
  			 else connMgr.rollback();
             
         } catch ( Exception ex ) { 
        	 connMgr.rollback();
             ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
             throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
         } finally { 
             if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
  			 if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return isOk;
     }
   
   
     
      
      /**
       * 입력한 강사의 중복여부 조회
       * 
       * @param v_userid
       *            강사 아이디
       * @return v_result 1:중복됨 ,0:중복되지 않음
       */
      public int overlapping(String v_userid) throws Exception {
          DBConnectionManager connMgr = null;
          ListSet ls = null;
          String sql = "";
          int v_result = 0;

          try {
              connMgr = new DBConnectionManager();

              sql = "select name from TZ_TUTOR where userid ='" + v_userid + "'";

              ls = connMgr.executeQuery(sql);

              // 중복된 경우 1을 return한다
              if (ls.next())
                  v_result = 1;
          } catch (Exception ex) {
              throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
          } finally {
              if (ls != null) {
                  try {
                      ls.close();
                  } catch (Exception e) {
                  }
              }
              if (connMgr != null) {
                  try {
                      connMgr.freeConnection();
                  } catch (Exception e10) {
                  }
              }
          }

          return v_result;
      }
 

  
}