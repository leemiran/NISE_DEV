// **********************************************************
// 1. 제      목:
// 2. 프로그램명: CommunityFrPollBean.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: Administrator 2003-08-29
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.community;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class CommunityFrPollBean { 
    private ConfigSet config;
    private int row;

    public CommunityFrPollBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.manage.row") );  // 이 모듈의 페이지당 row 수를 셋팅한다
            row = 10; // 강제로 지정
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
 
     /**
    * 왼쪽에문자붙이기
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public String LPAD(String str, int len, char pad) { 
        String result = str;
        int templen = len - result.getBytes().length;

        for ( int i = 0; i < templen; i++ )
            result = pad + result;

        return result;
    }

    /**
    * 설문등록
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertFrPoll(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet     ls          = null;
        String      sql         = "";
        String      sql1        = "";
//        String      createCmuno = "";
        int         isOk        = 1;
//        int         v_calno     = 0;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        String v_pollno        = box.getString("p_pollno");
        String v_questno      = box.getString("p_questno");
//        String v_need_question      = box.getString("p_need_question");
        String v_background      = box.getString("p_background");
        
        Vector v_fieldno       = box.getVector("p_fieldno");
        Vector v_field_name    = box.getVector("p_field_name");

        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");
        String SRTDT = box.getString("p_fdte").replaceAll("-","");
        String ENDDT = box.getString("p_tdte").replaceAll("-","") ;	
        
        try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false); 
             
             if ( v_pollno.length() <1) {  // 등록일때
               // pollno 값을 구한다.
               sql1  = " select max(pollno)  from tz_cmupollmst where cmuno = " + StringManager.makeSQL(v_cmuno);
               
               //////System.out.println("========등록전에 여부=============");
               //////System.out.println(sql1);
               //////System.out.println("=====================");
               ls = connMgr.executeQuery(sql1);
               if ( ls.next() ) 
            	   v_pollno = String.valueOf( ls.getInt(1) +1);  

               // 설문 마스터 정보를 넣어준다.
               sql = "insert into tz_cmupollmst (  					\n"
            	   + "	  cmuno										\n"
            	   + "	, pollno									\n"
            	   + "	, title										\n"
            	   + "	, fdte 										\n"
            	   + "	, tdte										\n"
            	   + "	, tot_poll_cnt								\n"
            	   + "	, background								\n"
            	   + "	, register_userid							\n"
            	   + "	, register_dte								\n"
            	   + "	, modifier_userid							\n"
            	   + "	, modifier_dte								\n"
            	   + "	, del_fg									\n"
            	   + ") values (									\n"
            	   + "	  ?											\n" // 1. cmuno
            	   + "	, ?											\n" // 2. pollno
            	   + "	, ?											\n" // 3. title
            	   + "	, ?											\n" // 4. fdte
            	   + "	, ?                 						\n" // 5. tdte
            	   + "	, 0											\n" //    tot_poll_cnt
            	   + "	, ?											\n" // 6. background
            	   + "	, ?											\n" // 7. register_userid
            	   + "	, to_char(sysdate,'YYYYMMDDHH24MISS')	\n" //    register_dte
            	   + "	, ?											\n" // 8. modifier_userid
            	   + "	, to_char(sysdate,'YYYYMMDDHH24MISS')	\n" //    modifier_dte
            	   + "	, 'N'										\n" //    del_fg
            	   + ")												\n";

               //////System.out.println("========설문제목 테이블 등록=============");
               //////System.out.println(sql);
               //////System.out.println("=====================");
               //////System.out.println(v_cmuno);
               //////System.out.println(v_pollno);
               //////System.out.println(box.getString("p_title"));
               //////System.out.println(StringManager.replace(SRTDT, ".", "") );
               //////System.out.println(StringManager.replace(ENDDT, ".", "") );
               //////System.out.println(v_background);
               //////System.out.println(s_userid);
               //////System.out.println(s_userid);

               //////System.out.println("========insertFrPoll=========");

               pstmt = connMgr.prepareStatement(sql);
               pstmt.setString(1, v_cmuno);
               pstmt.setString(2, v_pollno);
               pstmt.setString(3, box.getString("p_title") );
               pstmt.setString(4, StringManager.replace(SRTDT, ".", "") );
               pstmt.setString(5, StringManager.replace(ENDDT, ".", "") );
               pstmt.setString(6, v_background);
               pstmt.setString(7, s_userid);
               pstmt.setString(8, s_userid);
               isOk=pstmt.executeUpdate();
               if ( pstmt != null ) { pstmt.close(); }
               //////System.out.println("========여기 넘어가나 ?=========");
               // 오라클에서 MS-SQL로 변환하면서 주석처리
               /*
               sql1 = "select background from tz_cmupollmst where cmuno = '" + v_cmuno  + "' and pollno =" +v_pollno;
               connMgr.setOracleCLOB(sql1, v_background);
               */  
             } else { // 수정일때 
               sql = "update tz_cmupollmst set											\n"
            	   + "		  title = ?													\n"
            	   + "		, fdte = ?													\n"
            	   + "		, tdte = ?													\n"
            	   + "		, background = ?											\n"
            	   + "		, modifier_userid = ?										\n"
            	   + "		, modifier_dte = to_char(sysdate,'YYYYMMDDHH24MISS')	\n"
            	   + "where cmuno = ?													\n"
            	   + "and pollno = ?													\n";
               
               
               //////System.out.println("========설문제목 테이블  수정 =============");
               //////System.out.println(sql); 
               //////System.out.println("=====================");
               pstmt = connMgr.prepareStatement(sql);
               pstmt.setString(1, box.getString("p_title") );
               pstmt.setString(2, StringManager.replace(SRTDT, ".", "") );
               pstmt.setString(3, StringManager.replace(ENDDT, ".", "") );
               pstmt.setString(4, v_background);
               pstmt.setString(5, s_userid);
               pstmt.setString(6, v_cmuno);
               pstmt.setString(7, v_pollno);
               isOk = pstmt.executeUpdate();
               if ( pstmt != null ) { pstmt.close(); }
               	// 오라클에서 MS-SQL로 변환하면서 주석처리
               /*
               sql1 = "select background from tz_cmupollmst where cmuno = '" + v_cmuno  + "' and pollno =" +v_pollno;
               connMgr.setOracleCLOB(sql1, v_background);
               */  
             }

             if ( v_questno.length() <1) { // 질문항목정보 등록일때
            	 // questno값을 구한다.
            	 sql1 = "select max(questno)   									\n"
            		  + "from tz_cmupollquest 									\n"
            		  + "where cmuno = " + StringManager.makeSQL(v_cmuno) + " 	\n"
            		  + "and pollno = " + StringManager.makeSQL(v_pollno) + "   \n";
            	 
            	 
            	 //////System.out.println("========질문항목정보 등록 =============");
                 //////System.out.println(sql1);
                 //////System.out.println("=====================");
                 
               ls = connMgr.executeQuery(sql1);
               if ( ls.next() ) 
            	   v_questno= String.valueOf( ls.getInt(1) +1);
   
               // 질문정보를 넣어준다.
               sql = "insert into tz_cmupollquest (						\n"
            	   + "		  cmuno										\n"
            	   + "		, pollno									\n"
            	   + "		, questno									\n"
            	   + "		, need_question 							\n"
            	   + "		, need_fg									\n"
            	   + "		, re_gen_fg									\n"
            	   + "		, re_spe_fg									\n"
            	   + "		, item_cnt 									\n"
            	   + "		, register_userid							\n"
            	   + "		, register_dte								\n"
            	   + "		, modifier_userid							\n"
            	   + "		, modifier_dte								\n"
            	   + ") values ( 										\n"
            	   + "		  ?											\n" // 1. cmuno
            	   + "		, ?											\n" // 2. pollno
            	   + "		, ?											\n" // 3. questno
            	   + "		, ?											\n" // 4. need_question
            	   + "		, ?											\n" // 5. need_fg
            	   + "		, ?											\n" // 6. re_gen_fg
            	   + "		, ?											\n" // 7. re_spe_fg
            	   + "		, ?											\n" // 8. item_cnt
            	   + "		, ?											\n" // 9. register_userid
            	   + "		, to_char(sysdate,'YYYYMMDDHH24MISS')	\n" //    regiester_dte
            	   + "		, ?											\n" //10. modifier_userid
            	   + "		, to_char(sysdate,'YYYYMMDDHH24MISS'))\n";//    modifier_dte
               
               
               //////System.out.println("========질문정보를 넣어준다. =============");
               ///////System.out.println(sql);
               //////System.out.println("=====================");
               
               pstmt = connMgr.prepareStatement(sql);
               pstmt.setString(1 , v_cmuno);
               pstmt.setString(2 , v_pollno);
               pstmt.setString(3 , v_questno);
               pstmt.setString(4 , box.getString("p_need_question") );
               pstmt.setString(5 , box.getString("p_need_fg") );
               pstmt.setString(6 , box.getString("p_re_gen_fg") );
               pstmt.setString(7 , box.getString("p_re_spe_fg") );
               pstmt.setString(8 , "0");
               pstmt.setString(9 , s_userid);
               pstmt.setString(10 , s_userid);
               isOk = pstmt.executeUpdate();
               if ( pstmt != null ) { pstmt.close(); }
             } else { // 질문항목정보 수정일때
               sql = "update tz_cmupollquest set										\n"
            	   + "		  need_question = ?											\n" // 1. need_question
            	   + "		, modifier_userid = ? 										\n" // 2. s_userid
            	   + "		, modifier_dte = to_char(sysdate,'YYYYMMDDHH24MISS') 	\n" 
            	   + "where cmuno = ?													\n" // 3. cmuno
            	   + "and pollno = ?													\n" // 4. pollno
            	   + "and questno = ? 													\n";// 5. questno
               
               //////System.out.println("========질문항목정보 수정일때. =============");
               //////System.out.println(sql);
               //////System.out.println("=====================");
               
               pstmt = connMgr.prepareStatement(sql);
               pstmt.setString(1 , box.getString("p_need_question") );               
               pstmt.setString(2 , s_userid);
               pstmt.setString(3 , v_cmuno);
               pstmt.setString(4 , v_pollno);
               pstmt.setString(5 , v_questno);
               isOk = pstmt.executeUpdate();
               if ( pstmt != null ) { pstmt.close(); }
             }

             // 기타의견란 등록
             String v_etc_fg = box.getStringDefault("p_etc_fg","N");
             String v_etcno = box.getStringDefault("p_etcno","");
             String v_etc_nm = box.getString("p_etc_nm");

             if ( v_etc_nm.length() > 1) { // 기타의견명을 설정했을 경우
                  int v_fieldcnt=0;
                  
                  sql1 = "select count(*)										\n"
                	   + "from tz_cmupollfield  								\n"
                	   + "where cmuno = " + StringManager.makeSQL(v_cmuno) + " 	\n"
                	   + "and pollno = " + StringManager.makeSQL(v_pollno) + "  \n"
                	   + "and questno = " + StringManager.makeSQL(v_questno) + "\n"
                	   + "and fieldno = " + StringManager.makeSQL(v_etcno) + "  \n";
                  
                  //////System.out.println("========기타의견란 등록. =============");
                  //////System.out.println(sql1);
                  //////System.out.println("=====================");
                  
                  
                  ls = connMgr.executeQuery(sql1);
                  
                  if ( ls.next() ) 
                	  v_fieldcnt= ls.getInt(1);
                  ls.close();
                  
                  if ( v_fieldcnt == 0) { // 등록일 때 
                    sql = "insert into tz_cmupollfield (					\n"
                    	+ "		  cmuno										\n"
                    	+ "		, pollno									\n"
                    	+ "		, questno									\n"
                    	+ "		, fieldno									\n"
                    	+ "		, field_name								\n"
                    	+ "		, poll_cnt									\n"
                    	+ "		, etc_fg									\n"
                    	+ "		, etc_nm									\n"
                    	+ "		, register_userid							\n"
                    	+ "		, register_dte								\n"
                    	+ "		, modifier_userid							\n"
                    	+ "		, modifier_dte								\n"
                    	+ ") values (										\n"
                    	+ "		  ?											\n" // 1. cmuno
                    	+ "		, ?											\n" // 2. pollno
                    	+ "		, ?											\n" // 3. questno
                    	+ "		, ?											\n" // 4. fieldno
                    	+ "		, ?											\n" // 5. field_name
                    	+ "		, ?											\n" // 6. poll_cnt
                    	+ "		, ?											\n" // 7. etc_fg
                    	+ "		, ?											\n" // 8. etc_nm
                    	+ "		, ?											\n" // 9. register_userid
                    	+ "		, to_char(sysdate,'YYYYMMDDHH24MISS')	\n" //    register_dte
                    	+ "		, ?											\n" //10. modifier_userid
                    	+ "		, to_char(sysdate,'YYYYMMDDHH24MISS'))\n";//    modifier_dte
                    
                    //////System.out.println("========기타의견란 등록. =============");
                    //////System.out.println(sql);
                    //////System.out.println("=====================");
                    
                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, v_cmuno);
                    pstmt.setString(2, v_pollno);
                    pstmt.setString(3, v_questno);
                    pstmt.setString(4, "1");
                    pstmt.setString(5, v_etc_nm);
                    pstmt.setString(6, "0");
                    pstmt.setString(7, v_etc_fg);
                    pstmt.setString(8, v_etc_nm);
                    pstmt.setString(9, s_userid);
                    pstmt.setString(10, s_userid);
                    isOk = pstmt.executeUpdate();
                    if ( pstmt != null ) { pstmt.close(); }
                  } else { // 수정일때
                    sql = "update tz_cmupollfield set										\n"
                    	+ "		field_name = ? 												\n"
                    /*
                         + "                            , etc_fg         =?"
                   */
                         + "    , etc_nm   =?                         \n"
                         + "	, modifier_userid = ?										\n"
                         + "	, modifier_dte = to_char(sysdate,'YYYYMMDDHH24MISS')	\n"
                         + "where cmuno = ?													\n"
                         + "and pollno = ?													\n"
                         + "and questno = ?													\n"
                         + "and fieldno = ?													\n";
                    
                    
                    //////System.out.println("========기타의견란 수정일때. =============");
                    //////System.out.println(sql);
                    //////System.out.println("=====================");
                    
                    
                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1,"");
                    /*
                    pstmt.setString(2 , v_etc_fg );
                    */
                    pstmt.setString(2 , "" );
                    
                    pstmt.setString(3, s_userid);
                    pstmt.setString(4, v_cmuno);
                    pstmt.setString(5, v_pollno);
                    pstmt.setString(6, v_questno);
                    pstmt.setString(7, v_etcno);
                    isOk=pstmt.executeUpdate();
                    if ( pstmt != null ) { pstmt.close(); }
                 }
             }

            // 설문상세항목 등록
             if ( "1".equals(box.getString("p_re_gen_fg"))||"2".equals(box.getString("p_re_gen_fg"))||"3".equals(box.getString("p_re_gen_fg"))) { 
                for ( int i = 0;i<v_fieldno.size();i++ ) { 
                    String v_tmp_fieldno    =(String)v_fieldno.elementAt(i);
                    String v_tmp_field_name =(String)v_field_name.elementAt(i);
                    
                    if ( v_tmp_field_name.length() > 0) { // 지문 항목 내용 입력한 게 있을 때
                       if ( v_tmp_fieldno.length() <1) { // 등록일 때
                         sql1 = "select max(fieldno)									\n"
                        	  + "from tz_cmupollfield  									\n"
                        	  + "where cmuno = " + StringManager.makeSQL(v_cmuno) + " 	\n"
                        	  + "and pollno = " + StringManager.makeSQL(v_pollno) + "   \n"
                        	  + "and questno = " + StringManager.makeSQL(v_questno) + " \n";
                         ls = connMgr.executeQuery(sql1);
                         if ( ls.next() ) 
                        	 v_tmp_fieldno = String.valueOf( ls.getInt(1) +1);

                         if ( "1".equals(v_tmp_fieldno)) v_tmp_fieldno="2";
                         sql = "insert into tz_cmupollfield (							\n"
                        	 + "	  cmuno												\n"
                        	 + "	, pollno											\n"
                        	 + "	, questno											\n"
                        	 + "	, fieldno											\n"
                        	 + "	, field_name										\n"
                        	 + "	, poll_cnt											\n"
                        	 + "	, etc_fg											\n"
                        	 + "	, etc_nm											\n"
                        	 + "	, register_userid									\n"
                        	 + "	, register_dte										\n"
                        	 + "	, modifier_userid									\n"
                        	 + "	, modifier_dte										\n"
                        	 + ") values (												\n"
                        	 + "	  ?													\n" // 1. cmuno
                        	 + "	, ?													\n" // 2. pollno
                        	 + "	, ?													\n" // 3. questno
                        	 + "	, ?													\n" // 4. fieldno
                        	 + "	, ?													\n" // 5. field_name
                        	 + "	, ?													\n" // 6. poll_cnt
                        	 + "	, ?													\n" // 7. etc_fg
                        	 + "	, ?													\n" // 8. etc_nm
                        	 + "	, ?													\n" // 9. register_userid
                        	 + "	, to_char(sysdate,'YYYYMMDDHH24MISS')			\n" //    register_dte
                        	 + "	, ?													\n" //10. modifier_userid
                        	 + "	, to_char(sysdate,'YYYYMMDDHH24MISS')			\n" //    modifier_dte
                        	 + ")														\n";
                         
                         
                         
                        // ////System.out.println("========설문상세항목 등록=============");
                         //////System.out.println(sql);
                         //////System.out.println("=====================");
                         
                         pstmt = connMgr.prepareStatement(sql);
                         pstmt.setString(1, v_cmuno);
                         pstmt.setString(2, v_pollno);
                         pstmt.setString(3, v_questno);
                         pstmt.setString(4, v_tmp_fieldno);
                         pstmt.setString(5, v_tmp_field_name);
                         pstmt.setString(6, "0");
                         pstmt.setString(7, "N");
                         pstmt.setString(8, "");
                         pstmt.setString(9, s_userid);
                         pstmt.setString(10, s_userid);
                         isOk = pstmt.executeUpdate();
                         if ( pstmt != null ) { pstmt.close(); }
                       } else { // 수정일때
                         sql = "update tz_cmupollfield set											\n"
                        	 + "		field_name = ?												\n"
                             /*
                             + "                            , etc_fg         =?"
                             */
                             + "        , etc_nm       =?    \n"
                            
                             + "		, modifier_userid = ? 										\n"
                             + "		, modifier_dte = to_char(sysdate,'YYYYMMDDHH24MISS')	\n"
                             + "where cmuno = ? 													\n"
                             + "and pollno = ?														\n"
                             + "and questno = ?														\n"
                             + "and fieldno = ? 													\n";
                         
                         //////System.out.println("========설문상세항목 수정일때=============");
                         //////System.out.println(sql);
                         //////System.out.println("=====================");
                         
                         pstmt = connMgr.prepareStatement(sql);
                         pstmt.setString(1, v_tmp_field_name);
                         /*
                         pstmt.setString(2 , "N" );
                         */
                         pstmt.setString(2 , v_tmp_field_name );
                         
                         pstmt.setString(3, s_userid);
                         pstmt.setString(4, v_cmuno);
                         pstmt.setString(5, v_pollno);
                         pstmt.setString(6, v_questno);
                         pstmt.setString(7, v_tmp_fieldno);
                         isOk=pstmt.executeUpdate();
                         if ( pstmt != null ) { pstmt.close(); }
                       }
                    }
                }// end for
             }
             if ( isOk > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
             }

        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql - > " +FormatDate.getDate("yyyyMMdd") + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }



    /**
    * 설문삭제
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int deleteFrPoll(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet     ls          = null;
        String      sql         = "";
//        String      sql1        = "";
//        String      createCmuno = "";
        int         isOk        = 1;
//        int         v_calno     = 0;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        String v_pollno        = box.getString("p_pollno");

        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");




        try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false); 

             sql  =" update tz_cmupollmst set  del_fg           ='Y'"
                  + "                         , modifier_userid =?"
                  + "                         , modifier_dte    =to_char(sysdate,'YYYYMMDDHH24MISS')"
                  + "                  where  cmuno   =?"
                  + "                    and  pollno  =?";
             pstmt = connMgr.prepareStatement(sql);
             pstmt.setString(1 , s_userid                );
             pstmt.setString(2 , v_cmuno                 );
             pstmt.setString(3 , v_pollno                );
             isOk=pstmt.executeUpdate();
             if ( pstmt != null ) { pstmt.close(); }
             if ( isOk > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
             }

        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql - > " +FormatDate.getDate("yyyyMMdd") + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }


    /**
    * 문항삭제
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int deleteFrPollField(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet     ls          = null;
        String      sql         = "";
//        String      sql1        = "";
//        String      createCmuno = "";
        int         isOk        = 1;
//        int         v_calno     = 0;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        String v_pollno        = box.getString("p_pollno");
        String v_questno        = box.getString("p_questno");
//        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");




        try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false); 
               sql  =" delete from  tz_cmupollquest"
                    + "                  where  cmuno    =?"
                    + "                    and  pollno   =?"
                    + "                    and  questno  =?";
             pstmt = connMgr.prepareStatement(sql);
             pstmt.setString(1 , v_cmuno                 );
             pstmt.setString(2 , v_pollno                );
             pstmt.setString(3 , v_questno                );
             isOk=pstmt.executeUpdate();
             if ( pstmt != null ) { pstmt.close(); }
             if ( isOk > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
             }

        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql - > " +FormatDate.getDate("yyyyMMdd") + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }
    /**
    * 답변등록
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int replyFrPoll(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet     ls          = null;
        String      sql         = "";
//        String      sql1        = "";
//        String      createCmuno = "";
        int         isOk        = 1;
        int         isOk1       = 0;
        
//        int         v_calno     = 0;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        String v_pollno        = box.getString("p_pollno");

        Vector v_questno       = box.getVector("p_questno");
        Vector v_re_gen_fg     = box.getVector("p_re_gen_fg");

        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");
        
        //////System.out.println("v_cmuno : "+v_cmuno);
        //////System.out.println("v_pollno : "+v_pollno);
        //////System.out.println("v_questno : "+v_questno);
        //////System.out.println("v_re_gen_fg : "+v_re_gen_fg);
        
        try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false); 

             // 총투표자수 증가
             sql  =" update tz_cmupollmst set  tot_poll_cnt    =tot_poll_cnt +1"
                  + "                  where  cmuno   =?"
                  + "                    and  pollno  =?";
             pstmt = connMgr.prepareStatement(sql);
             pstmt.setString(1 , v_cmuno                 );
             pstmt.setString(2 , v_pollno                );
             isOk=pstmt.executeUpdate();
             if ( pstmt != null ) { pstmt.close(); }

             for ( int i = 0;i<v_questno.size();i++ ) { 
                 String v_tmp_fquestno     =(String)v_questno.elementAt(i);
                 String v_tmp_re_gen_fg    =(String)v_re_gen_fg.elementAt(i);
                 
                 
                 //////System.out.println("v_tmp_fquestno :: "+v_tmp_fquestno);
                 //////System.out.println("v_tmp_re_gen_fg :: "+v_tmp_re_gen_fg);
                 
                 
                 if ( "1".equals(v_tmp_re_gen_fg)) { // 선택형질문인경우
                	 
                	 
                   ////System.out.println("선택형질문인경우");
                	 
                   String v_tmp_fieldno       = box.getString("p_fieldno" +v_tmp_fquestno + "a");
                   
                   String v_tmp_etc_nm        = box.getString("p_etc_nm" +v_tmp_fquestno + "a");
                   sql  =" update tz_cmupollfield set   poll_cnt   =poll_cnt +1"
                        + "                  where  cmuno    =?"
                        + "                    and  pollno   =?"
                        + "                    and  questno  =?"
                        + "                    and  fieldno  =?";
                   ////System.out.println("선택형질문인경우" + sql);
                   pstmt = connMgr.prepareStatement(sql);
                   pstmt.setString(1 , v_cmuno                 );
                   pstmt.setString(2 , v_pollno                );
                   pstmt.setString(3 , v_tmp_fquestno          );
                   pstmt.setString(4 , v_tmp_fieldno           );
                   isOk1=pstmt.executeUpdate();
                   if ( pstmt != null ) { pstmt.close(); }

                   sql  =" insert into tz_cmupollticket (  cmuno, pollno, questno, userid "
                        + "                              , sel_text, question, shot_text, register_dte) "
                        + "             values (       ?                 , ?           , ?         ,?"
                        + "                          , ?                 , ?           , ? "
                        + "                          , to_char(sysdate,'YYYYMMDDHH24MISS'))";
                   
                   ////System.out.println("선택형질문인경우" + sql);
                   pstmt = connMgr.prepareStatement(sql);
                   pstmt.setString(1 , v_cmuno                 );
                   pstmt.setString(2 , v_pollno                );
                   pstmt.setString(3 , v_tmp_fquestno                );
                   pstmt.setString(4 , s_userid               );
                   pstmt.setString(5 , v_tmp_fieldno);
                   pstmt.setString(6 , "" );
                   pstmt.setString(7 , v_tmp_etc_nm );
                   isOk1=pstmt.executeUpdate();
                   if ( pstmt != null ) { pstmt.close(); }
                 } else if ( "2".equals(v_tmp_re_gen_fg)||"3".equals(v_tmp_re_gen_fg)) { // 멀택인경우
                	 
                	 
                	 ////System.out.println("멀택인경우2");
                   Vector vv_tmp_fieldno       = box.getVector("p_fieldno" +v_tmp_fquestno + "a");
                   String v_tmp_etc_nm        = box.getString("p_etc_nm" +v_tmp_fquestno + "a");
                   String v_tmp_set_text= "";
                   for ( int j=0;j<vv_tmp_fieldno.size();j++ ) { 
                      String v_sub_tt_fieldno=(String)vv_tmp_fieldno.elementAt(j);
                      v_tmp_set_text +=v_sub_tt_fieldno + ",";
                      sql  =" update tz_cmupollfield set   poll_cnt   =poll_cnt +1"
                           + "                  where  cmuno    =?"
                           + "                    and  pollno   =?"
                           + "                    and  questno  =?"
                           + "                    and  fieldno  =?";
                      ////System.out.println("멀택인경우2" + sql);
                      pstmt = connMgr.prepareStatement(sql);
                      pstmt.setString(1 , v_cmuno                 );
                      pstmt.setString(2 , v_pollno                );
                      pstmt.setString(3 , v_tmp_fquestno          );
                      pstmt.setString(4 , v_sub_tt_fieldno           );
                      isOk1=pstmt.executeUpdate();
                      if ( pstmt != null ) { pstmt.close(); }
                   }
                   sql  =" insert into tz_cmupollticket (  cmuno, pollno, questno, userid "
                        + "                              , sel_text, question, shot_text, register_dte) "
                        + "             values (       ?                 , ?           , ?         ,?"
                        + "                          , ?                 , ?           , ? "
                        + "                          , to_char(sysdate,'YYYYMMDDHH24MISS'))";
                   
                   ////System.out.println("멀택인경우2" + sql);
                   
                   pstmt = connMgr.prepareStatement(sql);
                   pstmt.setString(1 , v_cmuno                 );
                   pstmt.setString(2 , v_pollno                );
                   pstmt.setString(3 , v_tmp_fquestno                );
                   pstmt.setString(4 , s_userid               );
                   pstmt.setString(5 , v_tmp_set_text);
                   pstmt.setString(6 , "" );
                   pstmt.setString(7 , v_tmp_etc_nm );
                   isOk1=pstmt.executeUpdate();
                   if ( pstmt != null ) { pstmt.close(); }
                 } else if ( "4".equals(v_tmp_re_gen_fg)||"5".equals(v_tmp_re_gen_fg)) { // 멀택인경우
                	 
                	 ////System.out.println("멀택인경우4");
                   String v_tmp_reply       = box.getString("p_reply" +v_tmp_fquestno + "a");
                   sql  =" insert into tz_cmupollticket (  cmuno, pollno, questno, userid "
                        + "                              , sel_text, question, shot_text, register_dte) "
                        + "             values (       ?                 , ?           , ?         ,?"
                        + "                          , ?                 , ?           , ? "
                        + "                          , to_char(sysdate,'YYYYMMDDHH24MISS'))";
                   
                   
                   ////System.out.println("멀택인경우4" + sql);
                   pstmt = connMgr.prepareStatement(sql);
                   pstmt.setString(1 , v_cmuno                 );
                   pstmt.setString(2 , v_pollno                );
                   pstmt.setString(3 , v_tmp_fquestno                );
                   pstmt.setString(4 , s_userid               );
                   pstmt.setString(5 , "");
                   pstmt.setString(6 , "" );
                   pstmt.setString(7 , v_tmp_reply );
                   isOk1=pstmt.executeUpdate();
                   if ( pstmt != null ) { pstmt.close(); }
                 }
             } 

             if ( isOk*isOk1 > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
             }

        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql - > " +FormatDate.getDate("yyyyMMdd") + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk*isOk1;
    }

    /**
    * 리스트
    * @param box          receive from the form object and session
    * @return ArrayList   리스트
    * @throws Exception
    */
    public ArrayList selectPollList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
        String              sql1    = "";
        DataBox             dbox    = null;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");

        String v_searchtext = box.getString("p_searchtext");
        String v_select     = box.getString("p_select");
//        String s_userid             = box.getSession("userid");
        int v_pageno        = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();

            sql1 ="\n  select   a.cmuno   cmuno  , a.pollno    pollno   , a.title   title  , a.fdte fdte"
                 + "\n         , a.tdte tdte, a.tot_poll_cnt tot_poll_cnt, a.background background,a.str_fg str_fg "
                 + "\n         , a.register_userid register_userid, a.register_dte register_dte"
                 + "\n         ,b.userid          userid                 ,fn_crypt('2', b.birth_date, 'knise')        birth_date "
                 + "\n         ,c.kor_name        name                   ,c.email        email "
                 //+ "\n         ,b.deptnam         deptnam                ,b.jikupnm      jikupnm ,b.jikwinm         jikwinm                "
                 + "\n         ,c.tel          tel"
                 + "\n         ,c.mobile          mobile                 ,c.office_tel   office_tel "
                 + "\n         ,c.wk_area         wk_area                ,c.grade        grade "
                 + "\n         ,e.kor_nm          grade_nm               "
                 + "\n    from tz_cmupollmst a,tz_member b,tz_cmuusermst c "
                 + "\n         ,tz_cmugrdcode  e  "
                 + "\n   where a.register_userid  = b.userid "  
                 + "\n     and a.cmuno            = c.cmuno "  
                 + "\n     and a.register_userid  = c.userid " 
                 + "\n     and c.cmuno            = e.cmuno  "
                 + "\n     and c.grade            = e.grcode "
                 + "\n     and a.cmuno            = '" +v_cmuno + "'"
                 + "\n     and a.del_fg           = 'N'"
                 ; 

            if ( !v_searchtext.equals("") ) {      // 검색어가 있으면
                 if ( v_select.equals("title"))   sql1 += "\n  and lower(a.title)    like lower (" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 if ( v_select.equals("name"))    sql1 += "\n  and lower(c.kor_name) like lower (" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
            }
            sql  ="\n  select a.*, ( select count(*) from ("
            	+ sql1 + " ) b where b.cmuno = a.cmuno and b.pollno <=a.pollno \n"
            	+ ") as rowseq from ( \n"
                //+ "\n  select a.* from (" 
                + sql1
                + "\n   ) a"
                + "\n  order by a.pollno desc";
//                 + "\n  ) a";
              
              
            ls = connMgr.executeQuery(sql);
            

            ls.setPageSize(row);                         // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                 // 현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();    // 전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();    // 전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum"  , new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount" , new Integer(row));
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }


    /**
    * 마스타조회
    * @param box          receive from the form object and session
    * @return ArrayList   마스타조회
    * @throws Exception
    */
    public ArrayList selectPollMst(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
        DataBox             dbox    = null;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        String v_pollno         = box.getString("p_pollno");
        v_pollno         = "";
//        String s_userid             = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            sql ="\n  select   a.cmuno   cmuno  , a.pollno    pollno   , a.title   title  , a.fdte fdte"
                 + "\n         , a.tdte tdte, a.tot_poll_cnt tot_poll_cnt, a.background background,a.str_fg str_fg "
                 + "\n         , a.register_userid register_userid, a.register_dte register_dte"
                 + "\n         ,b.userid          userid                 ,fn_crypt('2', b.birth_date, 'knise')        birth_date "
                 + "\n         ,c.kor_name        name                   ,c.email        email "
                 //+ "\n         ,b.deptnam         deptnam                ,b.jikupnm      jikupnm ,b.jikwinm         jikwinm                "
                 + "\n         ,c.tel          tel"
                 + "\n         ,c.mobile          mobile                 ,c.office_tel   office_tel "
                 + "\n         ,c.wk_area         wk_area                ,c.grade        grade "
                 + "\n         ,e.kor_nm          grade_nm               "
                 + "\n    from tz_cmupollmst a,tz_member b,tz_cmuusermst c "
                 + "\n         ,tz_cmugrdcode  e  "
                 + "\n   where a.register_userid  = b.userid "  
                 + "\n     and a.cmuno            = c.cmuno "  
                 + "\n     and a.register_userid  = c.userid " 
                 + "\n     and c.cmuno            = e.cmuno  "
                 + "\n     and c.grade            = e.grcode "
                 + "\n     and a.cmuno            = '" +v_cmuno + "'"
                 + "\n     and a.pollno            = '" +v_pollno + "'"
                 + "\n     and a.del_fg           = 'N'"
                 ; 

            ls = connMgr.executeQuery(sql);
            

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }

    /**
    * 조회
    * @param box          receive from the form object and session
    * @return ArrayList   조회
    * @throws Exception
    */
    public ArrayList selectPollView(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        ArrayList           list1    = new ArrayList();
        ArrayList           list2    = new ArrayList();
        ArrayList           list3    = new ArrayList();
        ArrayList           list4    = new ArrayList();

        String              sql     = "";
//        String              sql1    = "";
        DataBox             dbox    = null;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        String v_pollno        = box.getString("p_pollno");
//        String s_userid             = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            // 마스타
            sql ="\n  select   a.cmuno   cmuno  , a.pollno    pollno   , a.title   title  , a.fdte fdte"
                 + "\n         , a.tdte tdte, a.tot_poll_cnt tot_poll_cnt, a.background background,a.str_fg str_fg "
                 + "\n         , a.register_userid register_userid, a.register_dte register_dte"
                 + "\n         ,b.userid          userid                 ,fn_crypt('2', b.birth_date, 'knise')        birth_date "
                 + "\n         ,c.kor_name        name                   ,c.email        email "
                 //+ "\n         ,b.deptnam         deptnam                ,b.jikupnm      jikupnm ,b.jikwinm         jikwinm                "
                 + "\n         ,c.tel          tel"
                 + "\n         ,c.mobile          mobile                 ,c.office_tel   office_tel "
                 + "\n         ,c.wk_area         wk_area                ,c.grade        grade "
                 + "\n         ,e.kor_nm          grade_nm               "
                 + "\n    from tz_cmupollmst a,tz_member b,tz_cmuusermst c "
                 + "\n         ,tz_cmugrdcode  e  "
                 + "\n   where a.register_userid  = b.userid "  
                 + "\n     and a.cmuno            = c.cmuno "  
                 + "\n     and a.register_userid  = c.userid " 
                 + "\n     and c.cmuno            = e.cmuno  "
                 + "\n     and c.grade            = e.grcode "
                 + "\n     and a.cmuno            = '" +v_cmuno + "'"
                 + "\n     and a.pollno            = '" +v_pollno + "'"
                 + "\n     and a.del_fg           = 'N'"
                 ; 

            ////System.out.println("====조회        마스타============");
            ////System.out.println(sql);
            ////System.out.println("=======selectPollView=========");

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list1.add(dbox);
            }
            ls.close();

            // 질문항목
            sql ="\n  select   cmuno, pollno, questno, need_question "
                 + "\n        , need_fg, re_gen_fg, re_spe_fg, item_cnt "
                 + "\n        , register_userid , register_dte  "
                 + "\n        , modifier_userid , modifier_dte"
                 + "\n    from tz_cmupollquest "
                 + "\n   where cmuno            = '" +v_cmuno + "'"
                 + "\n     and pollno           = '" +v_pollno + "'"
                 ; 
            
            ////System.out.println("====조회        질문항목============");
            ////System.out.println(sql);
            ////System.out.println("=======selectPollView=========");

            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list2.add(dbox);
            }
            ls.close();


            // 상세항목
            sql ="\n  select   cmuno, pollno, questno, fieldno, field_name, poll_cnt "
                 + "\n         , etc_fg, etc_nm, register_userid, register_dte, modifier_userid, modifier_dte "
                 + "\n    from tz_cmupollfield "
                 + "\n   where cmuno            = '" +v_cmuno + "'"
                 + "\n     and pollno           = '" +v_pollno + "'"
                 ; 

            ////System.out.println("====조회        상세항목============");
            ////System.out.println(sql);
            ////System.out.println("=======selectPollView=========");

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list3.add(dbox);
            }
            ls.close();
            // 투표항목
            sql ="\n  select   cmuno, pollno, questno, userid, sel_text, question, shot_text, register_dte "
                 + "\n    from tz_cmupollticket "
                 + "\n   where cmuno            = '" +v_cmuno + "'"
                 + "\n     and pollno           = '" +v_pollno + "'"
                 ; 
            
            ////System.out.println("====조회        투표항목============");
            ////System.out.println(sql);
            ////System.out.println("=======selectPollView=========");

            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list4.add(dbox);
            }
            ls.close();
            list.add(list1);
            list.add(list2);
            list.add(list3);
            list.add(list4);

        }
        catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }



    /**
    * 커뮤니티 홈에서 조회
    * @param box          receive from the form object and session
    * @return ArrayList   커뮤니티 홈에서 조회
    * @throws Exception
    */
    public ArrayList selectPollIndexView(String v_cmuno) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        ArrayList           list1    = new ArrayList();
        ArrayList           list2    = new ArrayList();
        ArrayList           list3    = new ArrayList();
        ArrayList           list4    = new ArrayList();

        String              sql     = "";
//        String              sql1    = "";
        DataBox             dbox    = null;

        int v_pollno        = 0;


        try { 
            connMgr = new DBConnectionManager();

            // 마스타
            sql ="\n  select a.pollno    pollno" //select a.pollno pollno from (
                 + "\n    from tz_cmupollmst a,tz_member b,tz_cmuusermst c "
                 + "\n         ,tz_cmugrdcode  e  "
                 + "\n   where a.register_userid  = b.userid "  
                 + "\n     and a.cmuno            = c.cmuno "  
                 + "\n     and a.register_userid  = c.userid " 
                 + "\n     and c.cmuno            = e.cmuno  "
                 + "\n     and c.grade            = e.grcode "
                 + "\n     and a.cmuno            = '" +v_cmuno + "'"
                 + "\n     and a.fdte             <= '" +FormatDate.getDate("yyyyMMdd") + "'"
                 + "\n     and a.tdte             >= '" +FormatDate.getDate("yyyyMMdd") + "'"
                 + "\n     and a.del_fg           = 'N'"
                 + "\n     and rownum = 1 "
                 + "\n   order by a.pollno desc "; // ) a
//                 + "\n  where rownum =1"
                 ; 

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_pollno = ls.getInt(1);
            }            
            ls.close();
            
            // 마스타
            sql ="\n  select   a.cmuno   cmuno  , a.pollno    pollno   , a.title   title  , a.fdte fdte"
                 + "\n         , a.tdte tdte, a.tot_poll_cnt tot_poll_cnt, a.background background,a.str_fg str_fg "
                 + "\n         , a.register_userid register_userid, a.register_dte register_dte"
                 + "\n         ,b.userid          userid                 ,fn_crypt('2', b.birth_date, 'knise')        birth_date "
                 + "\n         ,c.kor_name        name                   ,c.email        email "
                 //+ "\n         ,b.deptnam         deptnam                ,b.jikupnm      jikupnm ,b.jikwinm         jikwinm                "
                 + "\n         ,c.tel          tel"
                 + "\n         ,c.mobile          mobile                 ,c.office_tel   office_tel "
                 + "\n         ,c.wk_area         wk_area                ,c.grade        grade "
                 + "\n         ,e.kor_nm          grade_nm               "
                 + "\n    from tz_cmupollmst a,tz_member b,tz_cmuusermst c "
                 + "\n         ,tz_cmugrdcode  e  "
                 + "\n   where a.register_userid  = b.userid "  
                 + "\n     and a.cmuno            = c.cmuno "  
                 + "\n     and a.register_userid  = c.userid " 
                 + "\n     and c.cmuno            = e.cmuno  "
                 + "\n     and c.grade            = e.grcode "
                 + "\n     and a.cmuno            = '" +v_cmuno + "'"
                 + "\n     and a.pollno            = " +v_pollno + " \n"
                 + "\n     and a.del_fg           = 'N'"
                 ; 

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list1.add(dbox);
            }
            ls.close();

            // 질문항목
            sql ="\n  select   cmuno, pollno, questno, need_question "
                 + "\n        , need_fg, re_gen_fg, re_spe_fg, item_cnt "
                 + "\n        , register_userid , register_dte  "
                 + "\n        , modifier_userid , modifier_dte"
                 + "\n    from tz_cmupollquest "
                 + "\n   where cmuno            = '" +v_cmuno + "'"
                 + "\n     and pollno           = " +v_pollno + " \n"
                 ; 
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list2.add(dbox);
            }
            ls.close();

            
            // 상세항목
            sql ="\n  select   cmuno, pollno, questno, fieldno, field_name, poll_cnt "
                 + "\n         , etc_fg, etc_nm, register_userid, register_dte, modifier_userid, modifier_dte "
                 + "\n    from tz_cmupollfield "
                 + "\n   where cmuno            = '" +v_cmuno + "'"
                 + "\n     and pollno           = " +v_pollno + " \n"
                 ; 
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list3.add(dbox);
            }
            ls.close();

            // 투표항목
            sql ="\n  select   cmuno, pollno, questno, userid, sel_text, question, shot_text, register_dte "
                 + "\n    from tz_cmupollticket "
                 + "\n   where cmuno            = '" +v_cmuno + "'"
                 + "\n     and pollno           = " +v_pollno + " \n"
                 ; 
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list4.add(dbox);
            }
            list.add(list1);
            list.add(list2);
            list.add(list3);
            list.add(list4);

        }
        catch ( Exception ex ) { 
//               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }


    /**
    * 투표상태조회
    * @param box          receive from the form object and session
    * @return ArrayList   투표상태조회
    * @throws Exception
    */
    public int selectPollTicketCnt(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
//        ArrayList           list    = new ArrayList();
//        ArrayList           list1    = new ArrayList();
//        ArrayList           list2    = new ArrayList();
//        ArrayList           list3    = new ArrayList();
//        ArrayList           list4    = new ArrayList();

        String              sql     = "";
//        String              sql1    = "";
//        DataBox             dbox    = null;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        int v_pollno        = Integer.parseInt(box.getStringDefault("p_pollno", "0"));
        String s_userid             = box.getSession("userid");
        int    v_rowcnt        =0;
        try { 
            connMgr = new DBConnectionManager();

            // 투표항목
            sql ="\n  select    count(*) cnt"
                 + "\n    from tz_cmupollticket "
                 + "\n   where cmuno            = '" +v_cmuno + "'"
                 + "\n     and pollno           = " +v_pollno + " \n"
                 + "\n     and userid           = '" +s_userid + "'"
                 ; 
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_rowcnt = ls.getInt(1);
            }

        }
        catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return v_rowcnt;
    }


    /**
    * 투표상태조회
    * @param box          receive from the form object and session
    * @return ArrayList   투표상태조회
    * @throws Exception
    */
    public int selectPollTickeIndextCnt(String v_cmuno,String v_pollno,String s_userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;

        String              sql     = "";
//        String              sql1    = "";
//        DataBox             dbox    = null;

        int    v_rowcnt        =0;
        try { 
            connMgr = new DBConnectionManager();

            // 투표항목
            sql ="\n  select    count(*) cnt"
                 + "\n    from tz_cmupollticket "
                 + "\n   where cmuno            = '" +v_cmuno + "'"
                 + "\n     and pollno           = '" +v_pollno + "'"
                 + "\n     and userid           = '" +s_userid + "'"
                 ; 
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_rowcnt = ls.getInt(1);
            }

        }
        catch ( Exception ex ) { 

            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return v_rowcnt;
    }



    /**
    * 투표번호조회
    * @param box          receive from the form object and session
    * @return ArrayList   투표번호조회
    * @throws Exception
    */
    public int getPollno(String v_cmuno) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;

        String              sql     = "";
//        String              sql1    = "";
//        DataBox             dbox    = null;

        int    v_pollno        =0;
        try { 
            connMgr = new DBConnectionManager();

            sql  = " select max(pollno)  from tz_cmupollmst where cmuno ='" +v_cmuno + "'";
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) v_pollno= ls.getInt(1) +1;


        }
        catch ( Exception ex ) { 

            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return v_pollno;
    }
}