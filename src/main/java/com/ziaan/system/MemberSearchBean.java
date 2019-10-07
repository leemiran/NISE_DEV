// **********************************************************
//  1. 제      목: 인사DB 검색
//  2. 프로그램명: MemberSearchBean.java
//  3. 개      요: 인사DB 검색
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성:  2008. 10. 10
//  7. 수      정:
// **********************************************************

package com.ziaan.system;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;


public class MemberSearchBean { 
    
    // private 
    
    public MemberSearchBean() { }

    /**
    인사DB검색결과 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList searchMemberList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet     ls              = null;
        ArrayList   list            = null;
        DataBox     dbox            = null;
 
        String      sql             = "";     
        String      sql1             = "";     
        String      sql2             = "";     
        PreparedStatement pstmt = null;
        
        //String      v_company   = box.getString("s_company");       // 해당 회사의 comp code
        /*
        String      v_userid    = box.getString("pp_userid");       // ID
        String      v_username  = box.getString("p_username");      // 성명
        String      v_birth_date     = box.getString("p_birth_date");         //주민등록번호 
        String      v_handphone = box.getString("p_handphone");     //핸드폰     
        String      v_email     = box.getString("p_email");         //이메일
         */
        
        String v_search     = box.getString("p_gubun");
        String v_searchtext = box.getString("p_key1");

        

        String      v_orderColumn   = box.getString("p_orderColumn");               // 정렬할 컬럼명
        String      v_orderType     = box.getString("p_orderType");                 // 정렬할 순서

        String s_gadmin = box.getSession("gadmin");
        
        int row = 50;  // 페이지당 row 개수
        
        int v_pageno   = box.getInt("p_pageno");
        
        String s_userid= box.getSession("userid");
        String s_name = box.getSession("name");
        
        String v_company = box.getString("p_company");
        
        StringTokenizer tokenizer = null;
		
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            if(!v_company.equals("")){
	            tokenizer = new StringTokenizer(v_company, ",");
	            v_company = "";
	    		while(tokenizer.hasMoreTokens()){
	    			v_company	+= "'"+tokenizer.nextToken()+"',";
	    		}
	    		v_company = v_company.substring(0, v_company.length()-1);
            }

            sql  =   "SELECT                                                 \n"
                   + "    a.comp,                      --회사	             \n"
                   + "    fn_crypt('2', a.pwd, 'knise') pwd,                                             \n"
                   + "    a.userid,                                          \n"
                   + "    a.name,                                            \n"
                   + "    a.hometel,                                         \n"
                   + "    a.handphone,                                       \n"
                   + "    a.lglast,                                          \n"
                   + "    fn_crypt('2', a.birth_date, 'knise') birth_date,                                           \n"
                   + "    get_compnm(comp) compnm,                           \n"
                   + "    a.post_nm as jikwinm,        --직급 	             \n"
                   + "    a.position_nm,               --소속       	         	 \n"
                   + "    a.indate,                                          \n"
                   + "    a.ldate,                                           \n"
                   + "    a.lvl_nm,                                           \n"
                   + "    a.isretire,                                           \n"
                   + "    a.email                                           \n"

                   + "   ,DECODE(EMP_GUBUN,'T','교원/보조원','E','보조원','학부모') AS GB,  \n"
                   + "    DECODE(EMP_GUBUN,'T',USER_PATH,'E',USER_PATH,'학부모') AS SNM,  \n"
                   + "    DECODE(hrdc,'C','직장(학교)','자택') AS RV ,cert                     \n"

                   + "FROM                                                   \n"
                   + "    tz_member a		             \n"
                   + "WHERE                                                  \n"
                   + "        1 = 1                                          \n";

            if ( !v_searchtext.equals("") ) {                             //    검색어가 있으면
            	
                if ( v_search.equals("userid") ) {                        //    ID로 검색할때
                	sql += " and upper(a.userid) like upper('%" + v_searchtext + "%')";
                } else if ( v_search.equals("name") ) {                   //    이름으로 검색할때
                	sql += " and upper(a.name) like upper('%" + v_searchtext + "%')";
                } else if ( v_search.equals("birth_date") ) {                  //    주민등록번호로  검색할때
                	sql += " and upper(substr(fn_crypt('2', a.birth_date, 'knise'),1,6)) like upper('%" + v_searchtext + "%')";
                } else if ( v_search.equals("handphone") ) {              //    핸드폰번호로  검색할때
                	v_searchtext = v_searchtext.replace("-", "");
                	sql += " and replace(handphone,'-','') like ('%" + v_searchtext + "%')";
                } else if ( v_search.equals("email") ) {                  //    이메일로  검색할때
                	sql += " and upper(a.email) like upper('%" + v_searchtext + "%')";
                }else if ( v_search.equals("user_path") ) {                  //    학교명으로  검색할때
                	sql += " and upper(a.user_path) like upper('%" + v_searchtext + "%')";
                }

            } 
            
            if ( !v_company.equals("ALL") ) { 
                // 회사 검색
                sql += "and a.comp in (" + v_company + ") ";
            }

            if ( v_orderColumn.equals("") ) { 
                sql += " order by a.comp, a.name";
            } else { 
                sql += " order by " + v_orderColumn + v_orderType;
            }
            
            // ID
            /*
            if ( !v_userid.equals("") ) { 
                sql += " and upper(a.userid) like upper('%" + v_userid + "%')";
//            	sql += " and a.userid = " + StringManager.makeSQL(v_userid) + "\n";
            }
            
            // 성명
            if ( !v_username.equals("") ) { 
                sql += " and upper(a.name) like upper('%" + v_username + "%')";
//            	sql += " and a.name = " + StringManager.makeSQL(v_username) + "\n";
            }
            */
            
            System.out.println("회원검색===>"+sql);
            
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(row);                       //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);               //     현재페이지번호를 세팅한다.
            int totalpagecount = ls.getTotalPage();    //     전체 페이지 수를 반환한다
            int totalrowcount = ls.getTotalCount();    //     전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("userid"       ,ls.getString("userid") );
                dbox.put("name"         ,ls.getString("name") );
                dbox.put("d_birth_date"        ,ls.getString("birth_date") );
                dbox.put("pwd"          ,ls.getString("pwd") );
                dbox.put("d_handphone"    ,ls.getString("handphone") );
                dbox.put("d_hometel"      ,ls.getString("hometel") );
                dbox.put("d_comp"			,ls.getString("comp"));
                dbox.put("lglast"       ,ls.getString("lglast") );
                dbox.put("d_jikwinm"	,ls.getString("jikwinm"));
                dbox.put("d_position_nm"	,ls.getString("position_nm"));
                dbox.put("d_gb"	,ls.getString("gb"));
                dbox.put("d_snm"	,ls.getString("snm"));
                dbox.put("d_rv"	,ls.getString("rv"));
// 2006.06.20 추가
                //dbox.put("isgyeonggi"   ,ls.getString("isgyeonggi") );
                //dbox.put("useuserid"    ,ls.getString("useuserid") );
                //dbox.put("type"         ,ls.getString("type") );
                //dbox.put("birth_date"        ,ls.getString("birth_date") );
                //dbox.put("ldatepayment" ,ls.getString("ldatepayment") );
                
// 2006.06.13 추가
                //dbox.put("handphone"    ,ls.getString("handphone") );
                //dbox.put("hometel"      ,ls.getString("hometel") );
                //dbox.put("addr"      ,ls.getString("addr") );
                //dbox.put("post"      ,ls.getString("post") );
// 2006.06.13 제거                 dbox.put("d_compnm"    ,ls.getString("compnm") );
// 2006.06.13 제거                 dbox.put("deptnam"     ,ls.getString("deptnam") );
// 2006.06.13 제거                 dbox.put("d_jikwinm"   ,ls.getString("jikwinm") );
                dbox.put("d_Dispnum"        ,new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_Totalpagecount" ,new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(row));
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e1 )  { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
    
    /**
    개인정보 조회
    @param box      receive from the form object and session
    @return ArrayList
    */
     public DataBox selectMemberInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet     ls              = null;
        DataBox     dbox            = null;
 
        String      sql             = "";       

        String      v_userid        = box.getString("p_userid");

        try { 
            connMgr = new DBConnectionManager();

//            sql = "select   get_compnm(a.comp,2,2) compnm,";
//            sql += "            a.userid,";
//            sql += "            a.name,";
//            sql += "            a.pwd,";
//            sql += "            a.cono,";
//            sql += "            a.deptcod, ";
//            sql += "            a.deptnam,";
//            sql += "            a.email,";
//            sql += "            substr(a.birth_date,1,6) || '-' || substr(a.birth_date,7,13) birth_date,";
//            sql += "            a.addr,";
//            sql += "            a.addr2,";
//            sql += "            a.comptel,";
//            sql += "            a.handphone,";
//            sql += "            a.jikup,";
//            sql += "            get_jikupnm(a.jikup, a.comp, a.jikupnm) jikupnm,";
//            sql += "            a.jikwi,";
//            sql += "            get_jikwinm(a.jikwi, a.comp) jikwinm,";
//            sql += "            a.office_gbn,";
//            sql += "            decode(a.office_gbn,'Y','재직','퇴직') office_gbndesc, ";
//            sql += "            a.work_plcnm,";
//            sql += "            a.indate, ";
//            sql += "            a.ldate, ";
//            sql += "            a.lgfail, ";
//            sql += "            (select max(ldate) from tz_loginid x where a.userid = x.userid group by x.userid) lgdate";
//            sql += "    from    tz_member a ";
//            sql += "    where   a.userid = '" + v_userid + "'";

            sql  = "SELECT                                                  \n" +
                   "    a.isgyeonggi,                                       \n" +
                   "    a.useuserid,                                        \n" +
                   "    a.type,                                             \n" +
                   "    a.ldatepayment,                                     \n" +
                   "    a.userid,                                           \n" +
                   "    a.name,                                             \n" +
                   "    fn_crypt('2', pwd, 'knise') pwd,                                              \n" +
                   "    a.email,                                            \n" +
                   "    fn_crypt('2', a.birth_date, 'knise') birth_date,                                            \n" +
                   "    a.post1,                                            \n" +
                   "    a.post2,                                            \n" +
                   "    a.address1                       addr,              \n" +
                   "    a.address2                       addr2,             \n" +
                   "    a.comptel,                                          \n" +
                   "    a.hometel,                                          \n" +
                   "    a.handphone,                                        \n" +
                   "    a.compnm,                                           \n" +
                   "    a.jikup,                                            \n" +
                   "    a.indate,                                           \n" +
                   "    a.ldate,                                            \n" +
                   "    a.lglast,                                           \n" +
                   "    a.lgfail,                                           \n" +
                   "    (SELECT                                             \n" +
                   "         MAX(ldate)                                     \n" +
                   "     FROM                                               \n" +
                   "         tz_loginid         x                           \n" +
                   "     WHERE                                              \n" +
                   "         a.userid = x.userid                            \n" +
                   "     GROUP BY                                           \n" +
                   "         x.userid                                       \n" +
                   "     )                               lgdate,            \n" +
                   "    ''                               cono,              \n" +
                   "    ''                               deptcod,           \n" +
                   "    ''                               deptnam,           \n" +
                   "    ''                               jikupnm,           \n" +
                   "    ''                               jikwi,             \n" +
                   "    b.jik                            jikwinm,           \n" +
                   "    ''                               office_gbn,        \n" +
                   "    ''                               office_gbndesc,    \n" +
                   "    ''                               work_plcnm         \n" +
                   "FROM                                                    \n" +
                   "    tz_member                        a,                 \n" +
                   "    tz_tutor                         b                  \n" +
                   "WHERE                                                   \n" +
                   "        a.userid = '" + v_userid + "'                   \n" +
                   "    AND a.userid = b.userid(+)                          \n" ;
            
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                dbox.put("userid"           ,ls.getString("userid") );
                dbox.put("type"             ,ls.getString("type") );
                dbox.put("isgyeonggi"       ,ls.getString("isgyeonggi") );
                dbox.put("useuserid"        ,ls.getString("useuserid") );
                dbox.put("ldatepayment"     ,ls.getString("ldatepayment") );
                dbox.put("compnm"           ,ls.getString("compnm") );
                dbox.put("name"             ,ls.getString("name") );
                dbox.put("pwd"              ,ls.getString("pwd") );
                dbox.put("email"            ,ls.getString("email") );
                dbox.put("birth_date"            ,ls.getString("birth_date") );
                dbox.put("post1"            ,ls.getString("post1") );
                dbox.put("post2"            ,ls.getString("post2") );
                dbox.put("addr"             ,ls.getString("addr") );
                dbox.put("addr2"            ,ls.getString("addr2") );
                dbox.put("hometel"          ,ls.getString("hometel") );
                dbox.put("comptel"          ,ls.getString("comptel") );
                dbox.put("handphone"        ,ls.getString("handphone") );
                dbox.put("lglast"           ,ls.getString("lglast") );
                dbox.put("lgfail"           ,ls.getString("lgfail") );
                dbox.put("ldate"            ,ls.getString("ldate") ); // 스케줄작업일
                
                dbox.put("cono"             ,ls.getString("cono") );
                dbox.put("deptcod"          ,ls.getString("deptcod") );
                dbox.put("deptnam"          ,ls.getString("deptnam") );
                dbox.put("d_jikupnm"        ,ls.getString("jikupnm") );
                dbox.put("d_jikup"          ,ls.getString("jikup") );
                dbox.put("d_jikwinm"        ,ls.getString("jikwinm") );
                dbox.put("d_jikwi"          ,ls.getString("jikwi") );
                dbox.put("office_gbn"       ,ls.getString("office_gbn") );
                dbox.put("d_office_gbndesc" ,ls.getString("office_gbndesc") );
                dbox.put("work_plcnm"       ,ls.getString("work_plcnm") );

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
    인사DB검색결과 리스트 엑셀출력
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList searchMemberListExcel(RequestBox box) throws Exception { 
//        DBConnectionManager connMgr = null;
//        PreparedStatement pstmtInsert = null;
//        ListSet     ls              = null;
//        ArrayList   list            = null;
//        DataBox     dbox            = null;
// 
//        String      sql             = "";       
//        String      sql2            = "";       
//        
//        String      v_company  = box.getString("s_company");    // 해당 회사의 comp code
//        String      v_seldept  = box.getString("s_seldept");    // 부서
//        
//        String      v_jikwi    = box.getString("s_jikwi");      // 직위
//        String      v_jikup    = box.getString("s_jikup");      // 직급
//        
//        String      v_workplc  = box.getString("s_workplc");    // 근무지
//        String      v_cono     = box.getString("p_cono");       // 사번
//        String      v_username = box.getString("p_username");   // 성명
//        
//        String      v_officegbn = box.getString("s_officegbn"); // 재직여부
//                
//        // 로그정보 기록하기
        String      v_userid    = box.getSession("userid");
        String      v_srchword  = box.getString("p_srchword");      // 검색어
//
//        String      v_orderColumn   = box.getString("p_orderColumn");               // 정렬할 컬럼명
//        String      v_orderType     = box.getString("p_orderType");                 // 정렬할 순서
//
//        try { 
//            connMgr = new DBConnectionManager();
//            list = new ArrayList();
//
//            sql = "select   replace(get_compnm(a.comp,2,2),'/','') compnm,";
//            sql += "            a.userid,";
//            sql += "            a.name,";
//            sql += "            a.pwd,";
//            sql += "            a.cono,";
//            sql += "            a.deptcod, ";
//            sql += "            a.deptnam,";
//            sql += "            a.email,";
//            sql += "            substr(a.birth_date,1,6) || '-' || substr(a.birth_date,7,13) birth_date,";
//            sql += "            a.addr,";
//            sql += "            a.addr2,";
//            sql += "            a.hometel,";
//            sql += "            a.comptel,";
//            sql += "            a.tel_line,";
//            sql += "            a.handphone,";
//            sql += "            a.jikup,";
//            sql += "            get_jikupnm(a.jikup, a.comp, a.jikupnm) jikupnm,";
//            sql += "            a.jikwi,";
//            sql += "            get_jikwinm(a.jikwi, a.comp) jikwinm,";
//            sql += "            a.office_gbn,";
//            sql += "            decode(a.office_gbn,'Y','재직','퇴직') office_gbndesc, ";
//            sql += "            a.work_plc,";
//            sql += "            a.work_plcnm";
//            sql += "    from    tz_member a ";
//            sql += "    where   1=1 ";
//            
//            if ( !v_company.equals("ALL") ) { 
//                // 회사 검색
//                sql += "and substr(comp,1,4) = '" + StringManager.substring(v_company, 0, 4) + "' ";
//            } 
//                    
//            // 사번
//            if ( !v_cono.equals("") ) { 
//                sql += " and cono = '" + v_cono + "'";
//            }
//            
//            // 성명
//            if ( !v_username.equals("") ) { 
//                sql += " and name like '" + v_username + "%'";
//            }
//            
//            // 재직여부
//            if ( !v_officegbn.equals("ALL") ) { 
//                sql += " and office_gbn = '" + v_officegbn + "'";
//            }
//                        
//            if ( v_orderColumn.equals("") ) { 
//                sql += " order by comp, name";
//            } else { 
//                sql += " order by " + v_orderColumn + v_orderType;
//            }
//        
//            
//            ls = connMgr.executeQuery(sql);
//
//            double rowcnt = 0;
//    
//            while ( ls.next() ) { 
//                dbox = ls.getDataBox();
//                dbox.put("d_compnm"         ,ls.getString("compnm") );
//                dbox.put("userid"           ,ls.getString("userid") );
//                dbox.put("name"             ,ls.getString("name") );
//                dbox.put("pwd"              ,ls.getString("pwd") );
//                dbox.put("cono"             ,ls.getString("cono") );
//                dbox.put("deptcod"          ,ls.getString("deptcod") );
//                dbox.put("deptnam"          ,ls.getString("deptnam") );
//                dbox.put("email"            ,ls.getString("email") );
//                dbox.put("d_birth_date"          ,ls.getString("birth_date") );
//                dbox.put("addr"             ,ls.getString("addr") );
//                dbox.put("hoemtel"          ,ls.getString("hometel") );
//                dbox.put("comptel"          ,ls.getString("comptel") );
//                dbox.put("tel_line"         ,ls.getString("tel_line") );
//                dbox.put("handphone"        ,ls.getString("handphone") );
//                dbox.put("jikup"            ,ls.getString("jikup") );
//                dbox.put("d_jikupnm"        ,ls.getString("jikupnm") );
//                dbox.put("jikwi"            ,ls.getString("jikwi") );
//                dbox.put("d_jikwinm"        ,ls.getString("jikwinm") );
//                dbox.put("office_gbn"       ,ls.getString("office_gbn") );
//                dbox.put("d_office_gbndesc" ,ls.getString("office_gbndesc") );
//                dbox.put("work_plc"         ,ls.getString("work_plc") );
//                dbox.put("d_work_plcnm"     ,ls.getString("work_plcnm") );      
//                
//                rowcnt++;
//                list.add(dbox);
//            }
//            
//            // 로그정보에 남김
//            sql2 = "Insert into tz_srchmemberlog ( ";
//            sql2 += "           logdate,userid,srchword,rowcnt) ";
//            sql2 += "values(to_char(sysdate, 'YYYYMMDDHH24MISS') , ? , ? , ?)";
//
//            pstmtInsert = connMgr.prepareStatement(sql2);
//            
//            pstmtInsert.setString(1, v_userid);
//            pstmtInsert.setString(2, v_srchword);
//            pstmtInsert.setDouble(3, rowcnt);
//            pstmtInsert.executeUpdate();
//            
//            connMgr.commit();
//        } catch ( Exception ex ) { 
//            ErrorManager.getErrorStackTrace(ex, box, sql);
//            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
//        } finally { 
//            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
//            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
//        }
//
//        return list;
         PreparedStatement pstmtInsert = null;
         DBConnectionManager connMgr = null;
         ListSet     ls              = null;
         ArrayList   list            = null;
         DataBox     dbox            = null;
  
         String      sql             = "";     
         String      sql1             = "";     
         String      sql2             = "";     
         PreparedStatement pstmt = null;
         
         String      v_company  = box.getString("s_company");    // 해당 회사의 comp code
         //String      v_comp  = box.getString("p_comp");    // 해당 회사의 comp code
         
         String      v_jikwi    = box.getString("s_jikwi");      // 직위
         String      v_jikup    = box.getString("s_jikup");      // 직급
         
         String      v_cono     = box.getString("pp_userid");       // ID
         String      v_username = box.getString("p_username");   // 성명
         
         String      v_officegbn= box.getString("s_officegbn");  // 재직여부
         String      p_seldept   = box.getString("p_seldept");       // 부서
         
         String      v_orderColumn   = box.getString("p_orderColumn");               // 정렬할 컬럼명
         String      v_orderType     = box.getString("p_orderType");                 // 정렬할 순서

         
       // 2006.06.22 추가 - 검색 조건이 추가됨.
         //String      v_typeU         = box.getString("p_typeU");
         //String      v_isgyeonggi    = box.getString("p_isgyeonggi");
         //String      v_useuserid     = box.getString("p_useuserid");
         
         String s_gadmin = box.getSession("gadmin");
         
         String s_userid= box.getSession("userid");
         String s_name = box.getSession("name");
         try { 
             connMgr = new DBConnectionManager();
             
             list = new ArrayList();

 /* 2006.06.13 - 변경 [표시될 항목이 틀려서 아예 빼버림]
             sql = "select   replace(get_compnm(comp,2,2),'/','') compnm,";
             sql += "            userid,";
             sql += "            name,";
             sql += "            pwd,";
             sql += "            cono, ";
             sql += "            get_deptnm(deptnam,userid) deptnam, ";
             sql += "            get_jikwinm(jikwi,comp) jikwinm, ";
             sql += "            work_plcnm,";
             sql += "            indate, ";
             sql += "            ldate ";
             sql += "from    tz_member ";
             sql += "where   1=1 ";
  */
             sql  =   "SELECT                                                 	\n"
                    + "    comp,                                  				\n"
                    + "    fn_crypt('2', pwd, 'knise') pwd,                                               	\n"
                    + "    userid,                                            	\n"
                    + "    name,                                              	\n"
                    + "    hometel,                          		  			\n"
                    + "    address as addr,                                   	\n"
                    + "    zip_cd as post,                                    	\n"
                    + "    handphone, handphone_no,                				\n"
                    + "    email,	                              				\n"
                    + "    ismailling,	                         				\n"
                    + "    fn_crypt('2', birth_date, 'knise') birth_date,	                          					\n"
                    + "    get_compnm(comp) compnm,	                          	\n"
                    + "    position_nm,	                          				\n"
                    + "    lvl_nm,	                          					\n"
                    + "    (SELECT CODENM FROM TZ_CODE WHERE GUBUN = '0115' AND CODE = A.JOB_CD) AS LIC,	\n"                    
                    + "    DECODE(EMP_GUBUN,'T','교원/보조원','E','보조원','학부모') AS GB,	                    \n"
                    + "    DECODE(EMP_GUBUN,'T',USER_PATH,'E',USER_PATH,'학부모') AS SNM,	   					\n"
                    + "    zip_cd, address,                    					\n"
                    + "    zip_cd1, address1,                  					\n"
                    + "    DECODE(hrdc,'C','직장(학교)','자택') AS RV,            \n"
                    + "    issms,                                             \n"
                    + "    lglast,                                            \n"
                    + "    indate,                                            \n"
                    + "    ldate                                              \n"
                    + "FROM                                                   \n"
                    + "    tz_member  A                                        \n"
                    + "WHERE                                                  \n"
                    + "        1 = 1                                          \n";
                    
             // ID
             if ( !v_cono.equals("") ) { 
                 sql += " and upper(userid) like upper('%" + v_cono + "%')";
             }
             
             // 성명
             if ( !v_username.equals("") ) { 
                 sql += " and upper(name) like upper('%" + v_username + "%')";
             }
        
             if ( !v_company.equals("ALL") ) { 
                 // 회사 검색
                 sql += " and comp = '" + v_company + "' ";
             }

             
             if ( v_orderColumn.equals("") ) { 
                 sql += " order by comp, name";
             } else { 
                 sql += " order by " + v_orderColumn + v_orderType;
             }
                       
             ls = connMgr.executeQuery(sql);

             double rowcnt = 0;

             while ( ls.next() ) { 
                 dbox = ls.getDataBox();
                 /*
                 dbox.put("userid"       ,ls.getString("userid") );
                 dbox.put("name"         ,ls.getString("name") );
                 dbox.put("pwd"          ,ls.getString("pwd") );
//  2006.06.20 추가
                 //dbox.put("isgyeonggi"   ,ls.getString("isgyeonggi") );
                 //dbox.put("useuserid"    ,ls.getString("useuserid") );
                 //dbox.put("type"         ,ls.getString("type") );
                 dbox.put("birth_date"        ,ls.getString("birth_date") );
                 dbox.put("lglast"       ,ls.getString("lglast") );
                 //dbox.put("ldatepayment" ,ls.getString("ldatepayment") );
                 
//  2006.06.13 추가
                 dbox.put("handphone"    ,ls.getString("handphone") );
                 dbox.put("hometel"      ,ls.getString("hometel") );
                 dbox.put("email"        ,ls.getString("email") );
                 dbox.put("ismailling"   ,ls.getString("ismailling") );
                 //dbox.put("issms"        ,ls.getString("issms") );
//  2006.06.13 제거                 dbox.put("d_compnm"    ,ls.getString("compnm") );
//  2006.06.13 제거                 dbox.put("deptnam"     ,ls.getString("deptnam") );
//  2006.06.13 제거                 dbox.put("d_jikwinm"   ,ls.getString("jikwinm") );
                 */
                 rowcnt++;
                 list.add(dbox);
             }
             
           //로그정보에 남김
           sql2 = "Insert into tz_srchmemberlog ( ";
           sql2 += "           logdate,userid,srchword,rowcnt) ";
           sql2 += "values(to_char(sysdate, 'YYYYMMDDHH24MISS') , ? , ? , ?)";

           pstmtInsert = connMgr.prepareStatement(sql2);
           
           pstmtInsert.setString(1, v_userid);
           pstmtInsert.setString(2, v_srchword);
           pstmtInsert.setDouble(3, rowcnt);
           pstmtInsert.executeUpdate();
           
           connMgr.commit();
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e1 )  { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return list;
    }
    

    /**
    인사DB검색결과 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectLog(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet     ls              = null;
        ArrayList   list            = null;
        DataBox     dbox            = null;
 
        int         v_pageno   = box.getInt("p_pageno");
        String              sql     = "";
        int row = 10;   // 페이지당 row 개수
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = "select       a.logdate,";
            sql += "            a.userid,";
            sql += "            (select name from tz_member where userid=a.userid) name,";
            sql += "            a.srchword,";
            sql += "            a.rowcnt ";
            sql += "from        tz_srchmemberlog a ";
            sql += "order by a.logdate desc ";
                                    
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(row);                       // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);               // 현재페이지번호를 세팅한다.
            int totalpagecount = ls.getTotalPage();    // 전체 페이지 수를 반환한다
            int totalrowcount = ls.getTotalCount();    // 전체 row수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("logdate"          ,ls.getString("logdate") );
                dbox.put("userid"       ,ls.getString("userid") );
                dbox.put("d_name"           ,ls.getString("name") );
                dbox.put("srchword"         ,ls.getString("srchword") );
                dbox.put("rowcnt"       ,new Integer( ls.getInt("rowcnt")));
                
                
                dbox.put("d_Dispnum"        ,new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_Totalpagecount" ,new Integer(totalpagecount));

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
     
     
     
    public int userValidation( RequestBox box ) throws Exception {
        DBConnectionManager connMgr      = null;
        PreparedStatement   pstmt        = null;
        
        Vector              v_check1     = new Vector();
        v_check1                         = box.getVector("p_checks");
        Enumeration         em           = v_check1.elements();
        
        String              sql          = "";
        String              v_userid     = "";
        String              v_isgyeonggi = box.getString("p_isgyeonggi");
        int                 isOk         = 1;

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            while ( em.hasMoreElements() ) {
                v_userid    = (String)em.nextElement();
                
                sql  = "UPDATE                  \n" +
                       "    TZ_MEMBER           \n" +
                       "SET                     \n" +
                       "    useuserid  = 'Y',   \n" +
                       "    isgyeonggi = ?      \n" +
                       "WHERE                   \n" +
                       "    userid     = ?      \n" ;
                
                pstmt = connMgr.prepareStatement(sql);
                pstmt.setString(1, v_isgyeonggi);
                pstmt.setString(2, v_userid);

                System.out.println( sql + " / '" + v_userid + "' / '" +  v_isgyeonggi + "'");
                isOk  = pstmt.executeUpdate();
                
            }
            
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
    
	/**
	 * 직무 목록
	 * @param RequestBox box receive from the form object and session
	 * @return ArrayList
	 **/
	public ArrayList selectJikmuList(RequestBox box) throws Exception { 
		DBConnectionManager connMgr = null;
		ListSet     	ls		= null;
		ArrayList   	list	= null;
		DataBox     	dbox	= null;
		StringBuffer	sbSQL	= new StringBuffer();
		
		try { 
			connMgr = new DBConnectionManager();
			
			list = new ArrayList();
			
            sbSQL.append("SELECT JOB_CD  \n");
            sbSQL.append("     , JOBNM  \n");
            sbSQL.append("     , LOCATION  \n");
            sbSQL.append("     , LVL  \n");
            sbSQL.append("     ,(SELECT COUNT(JOB_CD)  \n");
            sbSQL.append("       FROM   TZ_JIKMU  \n");
            sbSQL.append("       WHERE  A.JOB_CD = UPPER_JOBCD) AS CHILDCNT  \n");
            sbSQL.append("FROM   TZ_JIKMU A  \n");
            sbSQL.append("WHERE  ISDELETED != 'Y'  \n");
            sbSQL.append("ORDER BY LOCATION  \n");
			
			ls = connMgr.executeQuery(sbSQL.toString());
			
			while ( ls.next() ) { 
				dbox = ls.getDataBox();
				list.add(dbox);
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
			throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
		} finally { 
			if(ls != null) { try { ls.close(); } catch ( Exception e ) { } }
			if(connMgr != null) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return list;
	}
}