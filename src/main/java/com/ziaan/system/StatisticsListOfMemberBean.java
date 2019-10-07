

// **********************************************************
//  1. 제      목: 인사DB 검색
//  2. 프로그램명: MemberSearchBean.java
//  3. 개      요: 인사DB 검색
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성:  2004. 12. 20
//  7. 수      정:
// **********************************************************

package com.ziaan.system;

import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class StatisticsListOfMemberBean {
    // private
    private ConfigSet config;
    private int         row;
    
    public StatisticsListOfMemberBean() {
    }

    public ArrayList perform_01_01_01_MemberJoin_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String              sql        = "";        
        
        String p_name = box.getString("p_name");
        
        String v_subQry1 = "";
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            if ("-".equals(p_name)) {
            	v_subQry1 = "\n and    comp is null ";
            } else {
            	v_subQry1 = "\n and    comp = " + SQLString.Format(p_name);
            }
            
            sql = "\n select userid "
                + "\n      , name "
                + "\n      , email "
                + "\n      , handphone "
                + "\n      , hometel "
                + "\n      , zip_cd as post "
                + "\n      , address as addr "
                + "\n      , ismailling "
                + "\n      , to_char(to_date(substr(indate,1,8),'yyyymmdd'),'yyyy.mm.dd') "
                + "\n from   tz_member "
                + "\n where  1=1 "
                + v_subQry1;
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }

    public ArrayList perform_01_01_01_Sido_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String ss_company = box.getStringDefault("s_company", "ALL");  // 회사
        String              sql        = "";        
        
        String p_name = box.getString("p_name");    // 시도 
        String v_subQry1= "";
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            if ( !ss_company.equals("ALL")) {
            	v_subQry1 = "\n and    b.comp = " + SQLString.Format(ss_company);
            }

            if ("-".equals(p_name)) {
	            sql = "\n select b.userid "
	                + "\n      , b.name "
	                + "\n      , b.email "
	                + "\n      , b.handphone "
	                + "\n      , b.hometel "
	                + "\n      , b.zip_cd as post "
	                + "\n      , b.address as addr "
	                + "\n      , b.ismailling "
	                + "\n      , to_char(to_date(substr(b.indate,1,8),'yyyymmdd'),'yyyy.mm.dd') "
	                + "\n from   tz_member b "
	                + "\n where  b.zip_cd is null "
	                + v_subQry1;
            	
            } else {
	            sql = "\n select b.userid "
	                + "\n      , b.name "
	                + "\n      , b.email "
	                + "\n      , b.handphone "
	                + "\n      , b.hometel "
	                + "\n      , b.zip_cd as post "
	                + "\n      , b.address as addr "
	                + "\n      , b.ismailling "
	                + "\n      , to_char(to_date(substr(b.indate,1,8),'yyyymmdd'),'yyyy.mm.dd') "
	                + "\n from   tz_zipcode a "
	                + "\n      , tz_member b "
	                + "\n where  a.zipcode(+) = b.zip_cd "
	                + v_subQry1
	                + "\n and    a.sido = " + StringManager.makeSQL(p_name);
            }            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Gugun_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String ss_company = box.getStringDefault("s_company", "ALL");  // 회사
        String p_name = box.getString("p_name");    // 구군
        String v_sido = box.getString("p_sido");
        
        String v_subQry1 = ""; 
        	
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            if ( !ss_company.equals("ALL")) {
            	v_subQry1 = "\n and    b.comp = " + SQLString.Format(ss_company);
            }

            sql = "\n select b.userid "
                + "\n      , b.name "
                + "\n      , b.email "
                + "\n      , b.handphone "
                + "\n      , b.hometel "
                + "\n      , b.zip_cd as post "
                + "\n      , b.address as addr "
                + "\n      , b.ismailling "
                + "\n      , to_char(to_date(substr(b.indate,1,8),'yyyymmdd'),'yyyy.mm.dd') "
                + "\n from   tz_zipcode a "
                + "\n      , tz_member b "
                + "\n where  a.zipcode(+) = b.zip_cd "
                + v_subQry1
                + "\n and    a.sido = " + StringManager.makeSQL(v_sido)
                + "\n and    a.gugun = " + StringManager.makeSQL(p_name);

            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Dong_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String              sql        = "";        
        
        String ss_company = box.getStringDefault("s_company", "ALL");  // 회사
        String v_name = box.getString("p_name");
        String v_sido = box.getString("p_sido");
        String v_gugun = box.getString("p_gugun");

        String v_subQry1 = "";
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            if ( !ss_company.equals("ALL")) {
            	v_subQry1 = "\n and    b.comp = " + SQLString.Format(ss_company);
            }

            sql = "\n select b.userid "
                + "\n      , b.name "
                + "\n      , b.email "
                + "\n      , b.handphone "
                + "\n      , b.hometel "
                + "\n      , b.zip_cd as post "
                + "\n      , b.address as addr "
                + "\n      , b.ismailling "
                + "\n      , to_char(to_date(substr(b.indate,1,8),'yyyymmdd'),'yyyy.mm.dd') "
                + "\n from   tz_zipcode a "
                + "\n      , tz_member b "
                + "\n where  a.zipcode(+) = b.zip_cd "
                + v_subQry1
                + "\n and    a.sido = " + StringManager.makeSQL(v_sido)
                + "\n and    a.gugun = " + StringManager.makeSQL(v_gugun)
                + "\n and    a.dong = " + StringManager.makeSQL(v_name);
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Sex_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");
        if(v_name.equals("여성")) v_name = "F";
        else if ( v_name.equals("남성") ) v_name = "M";
        else if (v_name.equals("기타")) v_name = " ";
        else v_name = "";
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.zip_cd    post                                                \n";
            sql += "      , a.address     addr                                        \n";
            sql += "      , '' issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                                \n";
            sql += "         tz_member a                    \n";
            sql += "        ,vz_zipcode b   \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "       ,tz_organization d \n";
                sql += "       ,tz_organization_mat e             \n";
            }
            sql += " where  \n";
            sql += "       a.zip_cd = b.zipcode(+)       \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "   and b.sido = d.sido\n";
                sql += "   and b.gugun = e.gugun\n";
                sql += "   and d.orgcode = e.orgcode\n";
                sql += "   and d.orgcode = "+StringManager.makeSQL(v_orgcode)+"                                 \n";
            }
            if (!box.getString("p_sdate").equals("")
                    && !box.getString("p_ldate").equals("")) {
                if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                    sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                } else {
                    sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                            + box.getString("p_ldate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                }
            }
            
            if ( v_name.equals("") ) {
//                sql += " and (a.gender IS NULL OR a.gender NOT IN ('M', 'F') )             \n";
            } else {
                sql += " and NVL(a.gender, ' ') = "+SQLString.Format(v_name)+"               \n";
            }
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Age_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");

        String v_age1 = "";
        String v_age2 = "";
        
        if(v_name.length() == 6)
        {
            v_age1 = v_name.substring(0, 2);
            v_age2 = v_name.substring(3, 5);
        }
        else if(v_name.length() == 7)
        {
            v_age1 = v_name.substring(0, 2);
            v_age2 = v_name.substring(3, 6);
        }
        else
        {
            
        }

        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.zip_cd    post                                                \n";
            sql += "      , a.address     addr                                        \n";
            sql += "      , '' issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                                                                                                              \n";
            sql += "       tz_member a                                                                                                 \n";
            sql += "      ,vz_zipcode b   \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
            sql += "      ,tz_organization d \n";
            sql += "      ,tz_organization_mat e             \n";
            }
            sql += " where  \n";
            sql += "       a.zip_cd = b.zipcode(+)       \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
            sql += "   and b.sido = d.sido\n";
            sql += "   and b.gugun = e.gugun\n";
            sql += "   and d.orgcode = e.orgcode\n";
            sql += "   and d.orgcode = "+StringManager.makeSQL(v_orgcode)+"                                 \n";
            }
            if (!box.getString("p_sdate").equals("")
                    && !box.getString("p_ldate").equals("")) {
                if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                    sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                } else {
                    sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                            + box.getString("p_ldate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                }
            }
            
            if(!v_age1.equals(""))
            {
                sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
            }
            else if ( v_name.equals("기타") )
            {
                sql += "               and (nvL( to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)                     \n";
                sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)                                                  \n";
                sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)                                                  \n";
                sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)                                                  \n";
                sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)                                                  \n";
                sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)                                                  \n";
                sql += "                                               , '' ), 999 ) > 100                                                                      \n";
                sql += "                     or fn_crypt('2', a.birth_date, 'knise') is null )                                                                                          \n";
            }
            
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Nationality_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");

        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select b.userid,                                                                       \n";
            sql += "        b.name,                                                                         \n";
            sql += "        b.email,                                                                        \n";
            sql += "        b.handphone,                                                                    \n";
            sql += "       b.hometel,                                                                       \n";
            sql += "       b.zip_cd    post                                                \n";
            sql += "      , b.address     addr                                        \n";
            sql += "      , NVL(b.issms, 'Y') issms                                                         \n";
            sql += "      , NVL(b.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(b.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                              \n";
            sql += "        tz_code a,                  \n";
            sql += "        tz_member b                 \n";
            sql += "        ,vz_zipcode c   \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "       ,tz_organization d \n";
                sql += "       ,tz_organization_mat e             \n";
            }
            sql += " where  \n";
            sql += "       b.post1||'-'||b.post2 = c.zipcode(+)       \n";        
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "   and c.sido = d.sido\n";
                sql += "   and c.gugun = e.gugun\n";
                sql += "   and d.orgcode = e.orgcode\n";
                sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
            }
            if (!box.getString("p_sdate").equals("")
                    && !box.getString("p_ldate").equals("")) {
                if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                    sql += " and to_char(to_date(b.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                } else {
                    sql += "   and to_char(to_date(b.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                            + box.getString("p_ldate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                }
            }
            
            sql += "   and a.gubun = '0068'            \n";
            sql += "    and a.code(+) = b.NATIONALITY   \n";
            
            if ( !v_name.equals("") ) {
                sql += "    and a.codenm = "+SQLString.Format(v_name)+"         \n";
            }    
            
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_License_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            
            if(v_name.equals("미응답"))
            {
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                      \n";
                sql += "       tz_member a                                                                                            \n";
                sql += "        ,vz_zipcode b   \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";        
                sql += "   and license_choice is null                                                                               \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
            }
            else
            {
                sql += " select b.userid,                                                                       \n";
                sql += "        b.name,                                                                         \n";
                sql += "        b.email,                                                                        \n";
                sql += "        b.handphone,                                                                    \n";
                sql += "       b.hometel,                                                                       \n";
                sql += "       b.zip_cd    post                                                \n";
                sql += "      , b.address     addr                                        \n";
                sql += "      , NVL(b.issms, 'Y') issms                                                         \n";
                sql += "      , NVL(b.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(b.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "          from                                                                                              \n";
                sql += "               tz_code a                                                                                  \n";
                sql += "               ,tz_member b                                                                                  \n";
                sql += "               ,vz_zipcode c   \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "               ,tz_organization d \n";
                    sql += "               ,tz_organization_mat e             \n";
                }
                sql += "         where                                                                                              \n";
                sql += "               instr(b.license_choice, a.code) > 0                                                          \n";
                sql += "           and b.post1||'-'||b.post2 = c.zipcode(+)       \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "           and c.sido = d.sido\n";
                    sql += "           and c.gugun = e.gugun\n";
                    sql += "           and d.orgcode = e.orgcode\n";
                    sql += "           and d.orgcode = "+v_orgcode+"                                 \n";
                }
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                sql += "   and a.gubun = '0062'                                                                                     \n";
                
                if ( !v_name.equals("") ) {
                    sql += "   and a.codenm = "+SQLString.Format(v_name)+"                                                                                     \n";
                }    
            }
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Achievement_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");

        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            if(v_name.equals("미응답"))
            {
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }                
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }                                
                sql += "   and nvl(a.achievement, ' ') = ' '\n";                
            }
            else
            {
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "        ,tz_code f   \n";
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                
                if ( !v_name.equals("") ) {
                    sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                }    
                
                sql += "   and f.gubun = '0063'\n";
                sql += "   and a.achievement = f.code\n";
            }
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Jikup_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");

        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            if(v_name.equals("미응답"))
            {
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }                
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }                                
                sql += "   and nvl(a.jikup, ' ') = ' '\n";                
            }
            else
            {
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "        ,tz_code f   \n";
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                
                if ( !v_name.equals("") ) {
                    sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                }
                    
                sql += "   and f.gubun = '0064'\n";
                sql += "   and a.jikup = f.code\n";
            }
            
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Entrytype_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");

        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            if(v_name.equals("미응답"))
            {
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }                
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }                                
                sql += "   and nvl(a.entrytype, ' ') = ' '\n";                
            }
            else
            {
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "        ,tz_code f   \n";
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                
                if ( !v_name.equals("") ) {
                    sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                }
                    
                sql += "   and f.gubun = '0065'\n";
                sql += "   and a.entrytype = f.code\n";
            }
            
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Entrypath_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");        

        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            if(v_name.equals("미응답") || v_name.equals("기존회원"))
            {
                if(v_name.equals("기존회원")) v_name = "OM";
                else v_name = " ";
                
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }                
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }        
                
                if ( !v_name.equals("") ) {
                    sql += "   and nvl(a.entrypath, ' ') = "+SQLString.Format(v_name)+"   \n";       
                }    
            }
            else if(v_name.equals("기존회원"))
            {
                
            }
            else
            {
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "        ,tz_code f   \n";
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                
                if ( !v_name.equals("") ) {
                    sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                }
                    
                sql += "   and f.gubun = '0066'\n";
                sql += "   and a.entrypath = f.code\n";
            }
           
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Iswedding_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");
        if(v_name.equals("미응답")) v_name = " ";
        else if(v_name.equals("미혼")) v_name = "N";
        else if(v_name.equals("기혼")) v_name = "Y";

        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql = "";
            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.zip_cd    post                                                \n";
            sql += "      , a.address     addr                                        \n";
            sql += "      , '' issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                                                                                          \n";
            sql += "       tz_member a                                                                                \n";
            sql += "        ,vz_zipcode b   \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "       ,tz_organization d \n";
                sql += "       ,tz_organization_mat e             \n";
            }
            sql += " where  \n";
            sql += "       a.zip_cd = b.zipcode(+)       \n";
            if (!box.getString("p_sdate").equals("")
                    && !box.getString("p_ldate").equals("")) {
                if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                    sql += " and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                } else {
                    sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                            + box.getString("p_ldate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                }
            }
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "   and b.sido = d.sido\n";
                sql += "   and b.gugun = e.gugun\n";
                sql += "   and d.orgcode = e.orgcode\n";
                sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
            }
            
            if ( !v_name.equals("") ) {
                sql += "   and nvl(a.iswedding, ' ') = "+SQLString.Format(v_name)+"   \n";
            }    
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    
    public ArrayList perform_01_01_01_Date_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String              sql        = "";        
        
        String p_orgcode    = box.getStringDefault("p_orgcode", "ALL" );
        String p_name       = box.getString("p_name"    );
        String p_inmonth    = box.getString("p_inmonth" );
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.zip_cd    post                                                \n";
            sql += "      , a.address     addr                                        \n";
            sql += "      , '' issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                                                                                                  \n";
            sql += "       tz_member a                                                                                  \n";
            if (!p_orgcode.equals("ALL")) {
                sql += "       , vz_zipcode b, tz_organization c, tz_organization_mat d       \n";
            }
            sql += " where                                                                                                  \n";
            sql += "       1=1                                                                                              \n";
            if (!p_orgcode.equals("ALL")) {
                sql += "   and a.zip_cd = b.zipcode(+)           \n";
                sql += "   and b.gugun = d.gugun           \n";
                sql += "   and b.sido = c.sido           \n";
                sql += "   and c.orgcode = d.orgcode           \n";
                sql += "   and c.orgcode = " + SQLString.Format(p_orgcode) + "         \n";
            }
            
            if ( p_name.equals("기타") ) {
                sql += "   and a.indate is null           \n";                
            } else if ( !p_name.equals("") ) {
                sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                        + p_name
                        + "', 'yyyy.mm.dd'), 'yyyymmdd') \n";
            } 

            if( p_inmonth.equals("기타") ) {
                sql += "   and a.indate is null           \n";
            } else if ( !p_inmonth.equals("") )  {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyy')||'년'||to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'mm')||'월' = "+SQLString.Format(p_inmonth)+"           \n";    
            } 
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    
    public ArrayList perform_01_01_01_MemberJoin_Student(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String              sql        = "";        
        
        String p_orgcode = box.getStringDefault("p_orgcode","ALL");
        String p_name = box.getString("p_name");
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.zip_cd    post                                                \n";
            sql += "      , a.address     addr                                        \n";
            sql += "      , '' issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                                                                                                  \n";
            sql += "       tz_member a                                                                                  \n";
            sql += "      ,  (                                              \n";
            sql += "            SELECT b.UserId                             \n";
            sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
            sql += "                ,  Tz_Student            b              \n";
            sql += "            WHERE  a.grcode  = 'N000001'                \n";
            sql += "            AND    a.subj    = b.subj                   \n";
            sql += "            AND    a.subjseq = b.subjseq                \n";
            sql += "            AND    a.year    = b.Year                   \n";
            sql += "        )               zz                               \n";
            if (!p_orgcode.equals("ALL")) {
                sql += "       , vz_zipcode b, tz_organization c, tz_organization_mat d       \n";
            }
            sql += " where                                                                                                  \n";
            sql += "       1=1                                                                                              \n";
            sql += " and   a.userid = zz.userid                                                                             \n";
            if (!p_orgcode.equals("ALL")) {
                sql += "   and a.zip_cd = b.zipcode(+)           \n";
                sql += "   and b.gugun = d.gugun           \n";
                sql += "   and b.sido = c.sido           \n";
                sql += "   and c.orgcode = d.orgcode           \n";
                sql += "   and c.orgcode = " + SQLString.Format(p_orgcode) + "         \n";
            }

            if(p_name.equals("기타")) {
                sql += "   and a.indate is null           \n";                
            } else if ( !p_name.equals("") ) {
                ;
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyy')||'년'||to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'mm')||'월' = "+SQLString.Format(p_name)+"           \n";    
            }
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }

    public ArrayList perform_01_01_01_Sido_Student(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String p_name = box.getString("p_name");    // 시도 
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.zip_cd    post                                                \n";
            sql += "      , a.address     addr                                        \n";
            sql += "      , '' issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "   from                                                     \n";
            sql += "        tz_member a,                                        \n";
            sql += "        vz_zipcode b   \n";
            sql += "      ,  (                                              \n";
            sql += "            SELECT b.UserId                             \n";
            sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
            sql += "                ,  Tz_Student            b              \n";
            sql += "            WHERE  a.grcode  = 'N000001'                \n";
            sql += "            AND    a.subj    = b.subj                   \n";
            sql += "            AND    a.subjseq = b.subjseq                \n";
            sql += "            AND    a.year    = b.Year                   \n";
            sql += "        )               c                               \n";
            
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "       ,tz_organization d \n";
                sql += "       ,tz_organization_mat e             \n";
            }
            sql += "  where                                                     \n";
            sql += "        a.zip_cd = b.zipcode(+)                \n";
            
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "   and b.sido = d.sido\n";
                sql += "   and b.gugun = e.gugun\n";
                sql += "   and d.orgcode = e.orgcode\n";
                sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
            }
            
            if (!box.getString("p_sdate").equals("")
                    && !box.getString("p_ldate").equals("")) {
                if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                    sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                } else {
                    sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                            + box.getString("p_ldate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                }
            }
            
            if ( !p_name.equals("") ) {
                sql += "   and b.sido = "+SQLString.Format(p_name)+"            \n";
            }
                            
            sql += "    and a.userid = c.userid                                \n";
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Gugun_Student(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String p_name = box.getString("p_name");    // 구군
        String v_sido = box.getString("p_sido");
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.zip_cd    post                                                \n";
            sql += "      , a.address     addr                                        \n";
            sql += "      , '' issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                                         \n";
            sql += "      tz_member a,                             \n";
            sql += "      vz_zipcode b                             \n";
            sql += "      ,  (                                              \n";
            sql += "            SELECT b.UserId                             \n";
            sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
            sql += "                ,  Tz_Student            b              \n";
            sql += "            WHERE  a.grcode  = 'N000001'                \n";
            sql += "            AND    a.subj    = b.subj                   \n";
            sql += "            AND    a.subjseq = b.subjseq                \n";
            sql += "            AND    a.year    = b.Year                   \n";
            sql += "        )               c                               \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "       ,tz_organization d \n";
                sql += "       ,tz_organization_mat e             \n";
            }
            sql += " where                                         \n";
            sql += "       a.zip_cd = b.zipcode(+)       \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "   and b.sido = d.sido\n";
                sql += "   and b.gugun = e.gugun\n";
                sql += "   and d.orgcode = e.orgcode\n";
                sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
            }
            
            if (!box.getString("p_sdate").equals("")
                    && !box.getString("p_ldate").equals("")) {
                if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                    sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                } else {
                    sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                            + box.getString("p_ldate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                }
            }
            
            if ( !v_sido.equals("") ) {
                sql += "   and b.sido = " + SQLString.Format(v_sido) + "                        \n";
            }                
            
            if ( !p_name.equals("") ) {
                sql += "   and b.gugun = " + SQLString.Format(p_name) + "                        \n";
            }                
            
            sql += "    and a.userid = c.userid                                \n";
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Dong_Student(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");
        String v_sido = box.getString("p_sido");
        String v_gugun = box.getString("p_gugun");
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.zip_cd    post                                                \n";
            sql += "      , a.address     addr                                        \n";
            sql += "      , '' issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                                         \n";
            sql += "      tz_member a,                             \n";
            sql += "      vz_zipcode b                             \n";
            sql += "      ,  (                                              \n";
            sql += "            SELECT b.UserId                             \n";
            sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
            sql += "                ,  Tz_Student            b              \n";
            sql += "            WHERE  a.grcode  = 'N000001'                \n";
            sql += "            AND    a.subj    = b.subj                   \n";
            sql += "            AND    a.subjseq = b.subjseq                \n";
            sql += "            AND    a.year    = b.Year                   \n";
            sql += "        )               c                               \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "       ,tz_organization d \n";
                sql += "       ,tz_organization_mat e             \n";
            }
            
            sql += " where                                         \n";
            sql += "       a.zip_cd = b.zipcode(+)       \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "   and b.sido = d.sido\n";
                sql += "   and b.gugun = e.gugun\n";
                sql += "   and d.orgcode = e.orgcode\n";
                sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
            }
            
            if (!box.getString("p_sdate").equals("")
                    && !box.getString("p_ldate").equals("")) {
                if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                    sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                } else {
                    sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                            + box.getString("p_ldate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                }
            }
            
            if ( !v_sido.equals("") ) {
                sql += "   and b.sido  = " + SQLString.Format(v_sido) + "                        \n";
            }
                
            if ( !v_gugun.equals("") ) {
                sql += "   and b.gugun = " + SQLString.Format(v_gugun) + "                        \n";
            }
                
            if ( !v_name.equals("") ) {
                sql += "   and b.dong = " + SQLString.Format(v_name) + "                        \n";
            }
                            
            sql += "    and a.userid = c.userid                                \n";
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Sex_Student(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");
        if(v_name.equals("여성")) v_name = "F";
        else if (v_name.equals("남성")) v_name = "M";
        else if (v_name.equals("기타")) v_name = " ";
        else v_name = "";
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.zip_cd    post                                                \n";
            sql += "      , a.address     addr                                        \n";
            sql += "      , '' issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                                \n";
            sql += "         tz_member a                    \n";
            sql += "        ,vz_zipcode b   \n";
            sql += "      ,  (                                              \n";
            sql += "            SELECT b.UserId                             \n";
            sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
            sql += "                ,  Tz_Student            b              \n";
            sql += "            WHERE  a.grcode  = 'N000001'                \n";
            sql += "            AND    a.subj    = b.subj                   \n";
            sql += "            AND    a.subjseq = b.subjseq                \n";
            sql += "            AND    a.year    = b.Year                   \n";
            sql += "        )               c                               \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "       ,tz_organization d \n";
                sql += "       ,tz_organization_mat e             \n";
            }
            sql += " where  \n";
            sql += "       a.zip_cd = b.zipcode(+)       \n";
            sql += "    and a.userid = c.userid                                \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "   and b.sido = d.sido\n";
                sql += "   and b.gugun = e.gugun\n";
                sql += "   and d.orgcode = e.orgcode\n";
                sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
            }
            if (!box.getString("p_sdate").equals("")
                    && !box.getString("p_ldate").equals("")) {
                if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                    sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                } else {
                    sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                            + box.getString("p_ldate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                }
            }
            
            if ( v_name.equals("") ) {
//                sql += " and (a.gender IS NULL OR a.gender NOT IN ('M', 'F') )             \n";
            } else {
                sql += " and NVL(a.gender, ' ') = " + SQLString.Format(v_name) + "           \n";
            }
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Age_Student(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");

        String v_age1 = "";
        String v_age2 = "";
        
        if(v_name.length() == 6)
        {
            v_age1 = v_name.substring(0, 2);
            v_age2 = v_name.substring(3, 5);
        }
        else if(v_name.length() == 7)
        {
            v_age1 = v_name.substring(0, 2);
            v_age2 = v_name.substring(3, 6);
        }
        else
        {
            
        }

        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.zip_cd    post                                                \n";
            sql += "      , a.address     addr                                        \n";
            sql += "      , '' issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                                                                                                              \n";
            sql += "       tz_member a                                                                                                 \n";
            sql += "      ,vz_zipcode b   \n";
            sql += "      ,  (                                              \n";
            sql += "            SELECT b.UserId                             \n";
            sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
            sql += "                ,  Tz_Student            b              \n";
            sql += "            WHERE  a.grcode  = 'N000001'                \n";
            sql += "            AND    a.subj    = b.subj                   \n";
            sql += "            AND    a.subjseq = b.subjseq                \n";
            sql += "            AND    a.year    = b.Year                   \n";
            sql += "        )               c                               \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
            sql += "      ,tz_organization d \n";
            sql += "      ,tz_organization_mat e             \n";
            }
            sql += " where  \n";
            sql += "       a.zip_cd = b.zipcode(+)       \n";
            sql += "    and a.userid = c.userid                                \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
            sql += "   and b.sido = d.sido\n";
            sql += "   and b.gugun = e.gugun\n";
            sql += "   and d.orgcode = e.orgcode\n";
            sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
            }
            if (!box.getString("p_sdate").equals("")
                    && !box.getString("p_ldate").equals("")) {
                if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                    sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                } else {
                    sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                            + box.getString("p_ldate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                }
            }
            
            if(!v_age1.equals(""))
            {
                sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
            }
            else
            {
                sql += "               and (fn_crypt('2', a.birth_date, 'knise') is null or substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
            }
            
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Nationality_Student(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");

        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select b.userid,                                                                       \n";
            sql += "        b.name,                                                                         \n";
            sql += "        b.email,                                                                        \n";
            sql += "        b.handphone,                                                                    \n";
            sql += "       b.hometel,                                                                       \n";
            sql += "       b.zip_cd    post                                                \n";
            sql += "      , b.address     addr                                        \n";
            sql += "      , '' issms                                                         \n";
            sql += "      , NVL(b.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(b.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                              \n";
            sql += "        tz_code a,                  \n";
            sql += "        tz_member b                 \n";
            sql += "        ,vz_zipcode c   \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "       ,tz_organization d \n";
                sql += "       ,tz_organization_mat e             \n";
            }
            sql += "      ,  (                                              \n";
            sql += "            SELECT b.UserId                             \n";
            sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
            sql += "                ,  Tz_Student            b              \n";
            sql += "            WHERE  a.grcode  = 'N000001'                \n";
            sql += "            AND    a.subj    = b.subj                   \n";
            sql += "            AND    a.subjseq = b.subjseq                \n";
            sql += "            AND    a.year    = b.Year                   \n";
            sql += "        )               f                               \n";
            sql += " where  \n";
            sql += "       b.post1||'-'||b.post2 = c.zipcode(+)       \n";      
            sql += "    and b.userid = f.userid                                \n";
            sql += "   and a.gubun = '0068'            \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "   and c.sido = d.sido\n";
                sql += "   and c.gugun = e.gugun\n";
                sql += "   and d.orgcode = e.orgcode\n";
                sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
            }
            if (!box.getString("p_sdate").equals("")
                    && !box.getString("p_ldate").equals("")) {
                if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                    sql += " and to_char(to_date(b.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                } else {
                    sql += "   and to_char(to_date(b.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                            + box.getString("p_ldate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                }
            }
            sql += "    and a.code(+) = b.NATIONALITY   \n";
            
            if ( !v_name.equals("") ) {
                sql += "    and a.codenm = "+SQLString.Format(v_name)+"   \n";
            }                    
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_License_Student(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            
            if(v_name.equals("미응답"))
            {
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                      \n";
                sql += "       tz_member a                                                                                            \n";
                sql += "        ,vz_zipcode b   \n";
                sql += "      ,  (                                              \n";
                sql += "            SELECT b.UserId                             \n";
                sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
                sql += "                ,  Tz_Student            b              \n";
                sql += "            WHERE  a.grcode  = 'N000001'                \n";
                sql += "            AND    a.subj    = b.subj                   \n";
                sql += "            AND    a.subjseq = b.subjseq                \n";
                sql += "            AND    a.year    = b.Year                   \n";
                sql += "        )               c                               \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";    
                sql += "    and a.userid = c.userid                                \n";    
                sql += "   and license_choice is null                                                                               \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
            }
            else
            {
                sql += " select b.userid,                                                                       \n";
                sql += "        b.name,                                                                         \n";
                sql += "        b.email,                                                                        \n";
                sql += "        b.handphone,                                                                    \n";
                sql += "       b.hometel,                                                                       \n";
                sql += "       b.zip_cd    post                                                \n";
                sql += "      , b.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(b.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(b.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "          from                                                                                              \n";
                sql += "               tz_code a                                                                                  \n";
                sql += "               ,tz_member b                                                                                  \n";
                sql += "               ,vz_zipcode c   \n";
                sql += "      ,  (                                              \n";
                sql += "            SELECT b.UserId                             \n";
                sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
                sql += "                ,  Tz_Student            b              \n";
                sql += "            WHERE  a.grcode  = 'N000001'                \n";
                sql += "            AND    a.subj    = b.subj                   \n";
                sql += "            AND    a.subjseq = b.subjseq                \n";
                sql += "            AND    a.year    = b.Year                   \n";
                sql += "        )               f                               \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "               ,tz_organization d \n";
                    sql += "               ,tz_organization_mat e             \n";
                }
                sql += "         where                                                                                              \n";
                sql += "               instr(b.license_choice, a.code) > 0                                                          \n";
                sql += "           and b.post1||'-'||b.post2 = c.zipcode(+)       \n";
                sql += "    and b.userid = f.userid                                \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "           and c.sido = d.sido\n";
                    sql += "           and c.gugun = e.gugun\n";
                    sql += "           and d.orgcode = e.orgcode\n";
                    sql += "           and d.orgcode = "+v_orgcode+"                                 \n";
                }
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                sql += "   and a.gubun = '0062'                                                                                     \n";
                
                if ( !v_name.equals("") ) {
                    sql += "   and a.codenm = "+SQLString.Format(v_name)+"                                                                                     \n";
                }                    
            }
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Achievement_Student(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");

        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            if(v_name.equals("미응답"))
            {
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                sql += "      ,  (                                              \n";
                sql += "            SELECT b.UserId                             \n";
                sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
                sql += "                ,  Tz_Student            b              \n";
                sql += "            WHERE  a.grcode  = 'N000001'                \n";
                sql += "            AND    a.subj    = b.subj                   \n";
                sql += "            AND    a.subjseq = b.subjseq                \n";
                sql += "            AND    a.year    = b.Year                   \n";
                sql += "        )               c                               \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }                
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                sql += "    and a.userid = c.userid                                \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }                                
                sql += "   and nvl(a.achievement, ' ') = ' '\n";                
            }
            else
            {
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                sql += "      ,  (                                              \n";
                sql += "            SELECT b.UserId                             \n";
                sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
                sql += "                ,  Tz_Student            b              \n";
                sql += "            WHERE  a.grcode  = 'N000001'                \n";
                sql += "            AND    a.subj    = b.subj                   \n";
                sql += "            AND    a.subjseq = b.subjseq                \n";
                sql += "            AND    a.year    = b.Year                   \n";
                sql += "        )               c                               \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "        ,tz_code f   \n";
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                sql += "    and a.userid = c.userid                                \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                
                if ( !v_name.equals("") ) {
                    sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                }
                                    
                sql += "   and f.gubun = '0063'\n";
                sql += "   and a.achievement = f.code\n";
            }
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Jikup_Student(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");

        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            if(v_name.equals("미응답"))
            {
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                sql += "      ,  (                                              \n";
                sql += "            SELECT b.UserId                             \n";
                sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
                sql += "                ,  Tz_Student            b              \n";
                sql += "            WHERE  a.grcode  = 'N000001'                \n";
                sql += "            AND    a.subj    = b.subj                   \n";
                sql += "            AND    a.subjseq = b.subjseq                \n";
                sql += "            AND    a.year    = b.Year                   \n";
                sql += "        )               c                               \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }                
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                sql += "    and a.userid = c.userid                                \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }                                
                sql += "   and nvl(a.jikup, ' ') = ' '\n";                
            }
            else
            {
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                sql += "      ,  (                                              \n";
                sql += "            SELECT b.UserId                             \n";
                sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
                sql += "                ,  Tz_Student            b              \n";
                sql += "            WHERE  a.grcode  = 'N000001'                \n";
                sql += "            AND    a.subj    = b.subj                   \n";
                sql += "            AND    a.subjseq = b.subjseq                \n";
                sql += "            AND    a.year    = b.Year                   \n";
                sql += "        )               c                               \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "        ,tz_code f   \n";
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                sql += "    and a.userid = c.userid                                \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                
                if ( !v_name.equals("") ) {
                    sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                }
                                    
                sql += "   and f.gubun = '0064'\n";
                sql += "   and a.jikup = f.code\n";
            }
            
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Entrytype_Student(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");

        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            if(v_name.equals("미응답"))
            {
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                sql += "      ,  (                                              \n";
                sql += "            SELECT b.UserId                             \n";
                sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
                sql += "                ,  Tz_Student            b              \n";
                sql += "            WHERE  a.grcode  = 'N000001'                \n";
                sql += "            AND    a.subj    = b.subj                   \n";
                sql += "            AND    a.subjseq = b.subjseq                \n";
                sql += "            AND    a.year    = b.Year                   \n";
                sql += "        )               c                               \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }                
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                sql += "    and a.userid = c.userid                                \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }                                
                sql += "   and nvl(a.entrytype, ' ') = ' '\n";                
            }
            else
            {
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                sql += "      ,  (                                              \n";
                sql += "            SELECT b.UserId                             \n";
                sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
                sql += "                ,  Tz_Student            b              \n";
                sql += "            WHERE  a.grcode  = 'N000001'                \n";
                sql += "            AND    a.subj    = b.subj                   \n";
                sql += "            AND    a.subjseq = b.subjseq                \n";
                sql += "            AND    a.year    = b.Year                   \n";
                sql += "        )               c                               \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "        ,tz_code f   \n";
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                sql += "    and a.userid = c.userid                                \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                
                if ( !v_name.equals("") ) {
                    sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                }
                                    
                sql += "   and f.gubun = '0065'\n";
                sql += "   and a.entrytype = f.code\n";
            }
            
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Entrypath_Student(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");        

        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            if(v_name.equals("미응답") || v_name.equals("기존회원"))
            {
                if(v_name.equals("기존회원")) v_name = "OM";
                else v_name = " ";
                
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                sql += "      ,  (                                              \n";
                sql += "            SELECT b.UserId                             \n";
                sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
                sql += "                ,  Tz_Student            b              \n";
                sql += "            WHERE  a.grcode  = 'N000001'                \n";
                sql += "            AND    a.subj    = b.subj                   \n";
                sql += "            AND    a.subjseq = b.subjseq                \n";
                sql += "            AND    a.year    = b.Year                   \n";
                sql += "        )               c                               \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }                
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                sql += "    and a.userid = c.userid                                \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }                                
                sql += "   and nvl(a.entrypath, ' ') = "+SQLString.Format(v_name)+"   \n";       
            }
            else if(v_name.equals("기존회원"))
            {
                
            }
            else
            {
                sql = "";
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "  from                                                                                                                                  \n";
                sql += "       tz_member a                                                                                                                      \n";
                sql += "        ,vz_zipcode b   \n";
                sql += "      ,  (                                              \n";
                sql += "            SELECT b.UserId                             \n";
                sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
                sql += "                ,  Tz_Student            b              \n";
                sql += "            WHERE  a.grcode  = 'N000001'                \n";
                sql += "            AND    a.subj    = b.subj                   \n";
                sql += "            AND    a.subjseq = b.subjseq                \n";
                sql += "            AND    a.year    = b.Year                   \n";
                sql += "        )               c                               \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "        ,tz_code f   \n";
                sql += " where  \n";
                sql += "       a.zip_cd = b.zipcode(+)       \n";
                sql += "    and a.userid = c.userid                                \n";
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                
                if ( !v_name.equals("") ) {
                    sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                }
                                    
                sql += "   and f.gubun = '0066'\n";
                sql += "   and a.entrypath = f.code\n";
            }
           
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_01_01_01_Iswedding_Student(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");
        if(v_name.equals("미응답")) v_name = " ";
        else if(v_name.equals("미혼")) v_name = "N";
        else if(v_name.equals("기혼")) v_name = "Y";

        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql = "";
            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.zip_cd    post                                                \n";
            sql += "      , a.address     addr                                        \n";
            sql += "      , '' issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                                                                                          \n";
            sql += "       tz_member a                                                                                \n";
            sql += "        ,vz_zipcode b   \n";
            sql += "      ,  (                                              \n";
            sql += "            SELECT b.UserId                             \n";
            sql += "            FROM   VZ_SCSUBJSEQ          a              \n";
            sql += "                ,  Tz_Student            b              \n";
            sql += "            WHERE  a.grcode  = 'N000001'                \n";
            sql += "            AND    a.subj    = b.subj                   \n";
            sql += "            AND    a.subjseq = b.subjseq                \n";
            sql += "            AND    a.year    = b.Year                   \n";
            sql += "        )               c                               \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "       ,tz_organization d \n";
                sql += "       ,tz_organization_mat e             \n";
            }
            sql += " where  \n";
            sql += "       a.zip_cd = b.zipcode(+)       \n";
            sql += "    and a.userid = c.userid                                \n";
            if (!box.getString("p_sdate").equals("")
                    && !box.getString("p_ldate").equals("")) {
                if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                    sql += " and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                } else {
                    sql += "   and to_char(to_date(indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                            + box.getString("p_sdate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                            + box.getString("p_ldate")
                            + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                }
            }
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "   and b.sido = d.sido\n";
                sql += "   and b.gugun = e.gugun\n";
                sql += "   and d.orgcode = e.orgcode\n";
                sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
            }
            
            if ( !v_name.equals("") ) {
                sql += "   and nvl(a.iswedding, ' ') = "+SQLString.Format(v_name)+"   \n";
            }                
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_02_01_01_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String c_sido = box.getString("p_sido");
        String c_gugun = box.getString("p_gugun");
        String c_gender = box.getString("p_gender");
        String c_achievement = box.getString("p_achievement");
        String c_age = box.getString("p_age");

        if(c_gender.equals("여성")) c_gender = "F";
        else if(c_gender.equals("남성"))                     c_gender = "M";
        else if(c_gender.equals("기타"))                     c_gender = " ";

        String v_age1 = "";
        String v_age2 = "";
        
        if(c_age.length() == 6)
        {
            v_age1 = c_age.substring(0, 2);
            v_age2 = c_age.substring(3, 5);
        }
        else if(c_age.length() == 7)
        {
            v_age1 = c_age.substring(0, 2);
            v_age2 = c_age.substring(3, 6);
        }
        else
        {
            
        }
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            if(c_achievement.equals("무응답"))
            {
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "   from                                                     \n";
                sql += "        tz_member a,                                        \n";
                sql += "        vz_zipcode b   \n";           
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "  where                                                     \n";
                sql += "        a.zip_cd = b.zipcode(+)                \n";
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                if(!c_sido.equals("")) sql += "   and b.sido = "+SQLString.Format(c_sido)+"            \n";
                if(!c_gugun.equals("")) sql += "   and b.gugun = "+SQLString.Format(c_gugun)+"            \n";
                if(!c_gender.equals("")) sql += "   and NVL(a.gender, ' ') = "+SQLString.Format(c_gender)+"            \n";
                if(!v_age1.equals(""))
                {
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (fn_crypt('2', a.birth_date, 'knise') is null or substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
                }
                
                sql += "   and a.achievement is null \n";
            }
            else if(c_achievement.equals(""))
            {
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "   from                                                     \n";
                sql += "        tz_member a,                                        \n";
                sql += "        vz_zipcode b   \n";           
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "  where                                                     \n";
                sql += "        a.zip_cd = b.zipcode(+)                \n";
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                if(!c_sido.equals("")) sql += "   and b.sido = "+SQLString.Format(c_sido)+"            \n";
                if(!c_gugun.equals("")) sql += "   and b.gugun = "+SQLString.Format(c_gugun)+"            \n";
                if(!c_gender.equals("")) sql += "   and a.gender = "+SQLString.Format(c_gender)+"            \n";
                if(!v_age1.equals(""))
                {
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (fn_crypt('2', a.birth_date, 'knise') is null or substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
                }
            }
            else
            {
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "   from                                                     \n";
                sql += "        tz_member a,                                        \n";
                sql += "        vz_zipcode b   \n";
                sql += "        ,tz_code f   \n";                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "  where                                                     \n";
                sql += "        a.zip_cd = b.zipcode(+)                \n";
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                if(!c_sido.equals("")) sql += "   and b.sido = "+SQLString.Format(c_sido)+"            \n";
                if(!c_gugun.equals("")) sql += "   and b.gugun = "+SQLString.Format(c_gugun)+"            \n";
                if(!c_gender.equals("")) sql += "   and a.gender = "+SQLString.Format(c_gender)+"            \n";
                if(!v_age1.equals(""))
                {
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (fn_crypt('2', a.birth_date, 'knise') is null or substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
                }     
                sql += "   and f.codenm = "+SQLString.Format(c_achievement)+"            \n";
                sql += "   and f.gubun = '0063'\n";
                sql += "   and a.achievement = f.code\n";
            }
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }
    
    public ArrayList perform_02_01_01_Student(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getStringDefault("p_orgcode","ALL");  // 조직코드
        String              sql        = "";        
        
        String c_sido = box.getString("p_sido");
        String c_gugun = box.getString("p_gugun");
        String c_gender = box.getString("p_gender");
        String c_achievement = box.getString("p_achievement");
        String c_age = box.getString("p_age");

        if(c_gender.equals("여성")) c_gender = "F";
        else if(c_gender.equals("남성"))                     c_gender = "M";

        String v_age1 = "";
        String v_age2 = "";
        
        if(c_age.length() == 6)
        {
            v_age1 = c_age.substring(0, 2);
            v_age2 = c_age.substring(3, 5);
        }
        else if(c_age.length() == 7)
        {
            v_age1 = c_age.substring(0, 2);
            v_age2 = c_age.substring(3, 6);
        }
        else
        {
            
        }
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            if(c_achievement.equals("무응답"))
            {
                sql += " select distinct                                                    \n";
                sql += "        a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "   from                                                     \n";
                sql += "        tz_member a,                                        \n";
                sql += "        vz_zipcode b   \n";           
                sql += "        ,tz_student c \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "  where                                                     \n";
                sql += "        a.zip_cd = b.zipcode(+)                \n";
                sql += "    and a.userid = c.userid                \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                if(!c_sido.equals("")) sql += "   and b.sido = "+SQLString.Format(c_sido)+"            \n";
                if(!c_gugun.equals("")) sql += "   and b.gugun = "+SQLString.Format(c_gugun)+"            \n";
                if(!c_gender.equals("")) sql += "   and a.gender = "+SQLString.Format(c_gender)+"            \n";
                if(!v_age1.equals(""))
                {
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (fn_crypt('2', a.birth_date, 'knise') is null or substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
                }
                sql += "   and a.achievement is null \n";
            }
            else if(c_achievement.equals(""))
            {
                sql += " select distinct                                                    \n";
                sql += "        a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "   from                                                     \n";
                sql += "        tz_member a,                                        \n";
                sql += "        vz_zipcode b   \n";                  
                sql += "        ,tz_student c \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "  where                                                     \n";
                sql += "        a.zip_cd = b.zipcode(+)                \n";
                sql += "    and a.userid = c.userid                \n";
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                if(!c_sido.equals("")) sql += "   and b.sido = "+SQLString.Format(c_sido)+"            \n";
                if(!c_gugun.equals("")) sql += "   and b.gugun = "+SQLString.Format(c_gugun)+"            \n";
                if(!c_gender.equals("")) sql += "   and a.gender = "+SQLString.Format(c_gender)+"            \n";
                if(!v_age1.equals(""))
                {
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (fn_crypt('2', a.birth_date, 'knise') is null or substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
                }
            }
            else
            {
                sql += " select distinct                                                    \n";
                sql += "        a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.zip_cd    post                                                \n";
                sql += "      , a.address     addr                                        \n";
                sql += "      , '' issms                                                         \n";
                sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
                sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                sql += "   from                                                     \n";
                sql += "        tz_member a,                                        \n";
                sql += "        vz_zipcode b   \n";       
                sql += "        ,tz_student c \n";
                sql += "        ,tz_code f   \n";                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "  where                                                     \n";
                sql += "        a.zip_cd = b.zipcode(+)                \n";
                sql += "    and a.userid = c.userid                \n";
                
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "   and b.sido = d.sido\n";
                    sql += "   and b.gugun = e.gugun\n";
                    sql += "   and d.orgcode = e.orgcode\n";
                    sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
                }
                
                if (!box.getString("p_sdate").equals("")
                        && !box.getString("p_ldate").equals("")) {
                    if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                        sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') \n";
                    } else {
                        sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') between to_char(to_date('"
                                + box.getString("p_sdate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd') and to_char(to_date('"
                                + box.getString("p_ldate")
                                + "', 'yyyy-mm-dd'), 'yyyymmdd')                              \n";
                    }
                }
                if(!c_sido.equals("")) sql += "   and b.sido = "+SQLString.Format(c_sido)+"            \n";
                if(!c_gugun.equals("")) sql += "   and b.gugun = "+SQLString.Format(c_gugun)+"            \n";
                if(!c_gender.equals("")) sql += "   and a.gender = "+SQLString.Format(c_gender)+"            \n";
                if(!v_age1.equals(""))
                {
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1), '9', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(fn_crypt('2', a.birth_date, 'knise'), 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (fn_crypt('2', a.birth_date, 'knise') is null or substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
                }     
                sql += "   and f.codenm = "+SQLString.Format(c_achievement)+"            \n";
                sql += "   and f.gubun = '0063'\n";
                sql += "   and a.achievement = f.code\n";
            }
            
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }


    /**
    과정별 교육실적
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList perform_03_01_01(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
         
        DataBox             dbox            = null;
         
        String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
        String              v_year          = box.getStringDefault  ( "p_year"         , ""    ); // 년도
        String              v_subjseq       = box.getStringDefault  ( "p_subjseq"      , ""    ); // 과목 기수
        String              v_subj          = box.getStringDefault  ( "p_subj"         , ""    ); // 과목&코스
        String              s_gadmin        = box.getSession        ( "gadmin"                 );
        String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
        String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
        
        try { 
            connMgr                         = new DBConnectionManager();
            list                            = new ArrayList();
            
            sbSQL.append(" SELECT  b.examnum, (select org_nm from tz_eduorg cd where cd.orgid = a.DEPT_CD) as DEPT_CDNM                                                                                      \n") 
                 .append("      ,  (select org_nm from tz_eduorg cd where  cd.orgid = a.AGENCY_CD) as AGENCY_CDNM                                                                                     \n")
                 .append("     ,   a.user_path                                                                                   \n")
                 .append("     ,   a.userid                                                                                 \n")
                 .append("      ,   a.name                                                                                  \n")
                 .append("       ,    fn_crypt('2', a.birth_date, 'knise') birth_date                          \n")
                 .append("      ,   a.email                     \n")
				 .append("      ,    a.handphone               \n")
				 .append("      , a.address                                           	\n")
	   	    	 .append("      , a.zip_cd                                           	\n")
	   	    	 .append("      , a.address1                                           	\n")
	   	    	 .append("      , a.zip_cd1                                           	\n")
	   	    	 .append("      , a.hrdc	                                           	\n")
				 .append("  	 ,   a.hometel                 \n")                                                                  
				 .append(" 	 ,   a.zip_cd                                           post   \n")                 
				 .append(" 	 ,   a.address                                     addr         \n")            
				 .append(" 	 ,   ''                                          issms           \n")         
				 .append("	 ,   NVL(a.ismailling    , 'Y')                                          ismailling   \n")           
				 .append("	 ,   TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate       \n")            
				 .append("	     ,   c.subjnm                                                              \n")                      
				 .append("	     ,   c.subj                                                               \n")                        
				 .append("	     ,   c.subjseq                                                             \n")                       
				 .append("	     ,   c.year      \n") 
				 .append("	     ,   (select EDUTIMES from tz_subj ts where c.subj = ts.subj) edutimes      \n")    
				 .append("	     ,   b.avetc2																 \n")  
				 .append("	     ,   b.avftest														 \n")  
				 .append("	     ,   b.avmtest          \n") 
				 .append("	     ,   b.avreport    \n") 
				 .append("	     ,   b.score    \n") 
				 .append("	     ,   c.edustart    \n") 
				 .append("	     ,ROW_NUMBER() OVER(ORDER BY  b.score  DESC,b.avreport  DESC, b.avftest  DESC, b.avetc2  DESC, fn_crypt('2', a.birth_date, 'knise') asc   ) AS RANKING   \n")    
				 .append("  ,   d.isgraduated                                                                                  \n")
                // .append("  ,   DECODE(c.isclosed, 'N', '미수료', DECODE(b.isgraduated, 'N', '미수료', '수료'))     isgraduatedname            \n")
				 .append("  ,    DECODE(d.isgraduated, 'N', '미수료', '수료')     isgraduatedname            \n")
                 .append("  ,   b.serno        ,b.editscore                                                                            \n")
                 .append("     , (select get_subjclass_fullnm (subjclass) from tz_subj ts where  c.subj = ts.subj) upperclassnm \n")
                 .append(" FROM    tz_member       a                                                                           \n")
                 .append("     ,   tz_student      b                                                                           \n")
                 .append("     ,   VZ_SCSUBJSEQ    c                                                                           \n")
                 .append("     ,   tz_stold   d                                                                           \n");
                 
            if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                sbSQL.append("  ,  vz_orgmember     i                                                                                                                                                       \n");
            }
            
            sbSQL.append(" WHERE   1           = 1                                                                             \n")
                 .append(" AND     c.subj      = b.subj                                                                        \n")
                 .append(" AND     c.subj      = d.subj                                                                        \n")
                 .append(" AND     c.subjseq   = b.subjseq                                                                     \n")
                 .append(" AND     c.subjseq   = d.subjseq                                                                     \n")
                 .append(" AND     c.year      = b.year                                                                        \n")
                 .append(" AND     c.year      = d.year                                                                        \n")
                 .append(" AND     b.userid    = a.userid                                                                      \n")
                 .append(" AND     b.userid    = d.userid                                                                      \n");
            
            if ( !v_grcode.equals("") ) { 
                sbSQL.append(" and c.grcode        = " + StringManager.makeSQL(v_grcode  ) + "   \n");
            }
            
            if ( !v_year.equals("") ) { 
                sbSQL.append(" and c.year          = " + StringManager.makeSQL(v_year    ) + "   \n");
            }
            
            if ( !v_subj.equals("") ) { 
                sbSQL.append(" and c.subj          = " + StringManager.makeSQL(v_subj    ) + "   \n");
            }
             
            if ( !v_subjseq.equals("") ) { 
                sbSQL.append(" and c.subjseq       = " + StringManager.makeSQL(v_subjseq ) + "   \n");
            }
            
            if ( v_isgraduated.equals("Y") ) {
                sbSQL.append(" and d.isgraduated    = " + StringManager.makeSQL(v_isgraduated   ) + "   \n");
                  //   .append(" and c.isclosed       = 'Y'                                               \n");
            } else if ( v_isgraduated.equals("N") ) {
                sbSQL.append(" and   ( d.isgraduated   IS NULL OR d.isgraduated = " + StringManager.makeSQL(v_isgraduated   ) + " )     \n");
                 //    .append("     )                                                                                                    \n");
            }
             
            if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                sbSQL.append(" AND     i.orgcode       = " + v_orgcode + "                             \n")
                     .append(" and     a.userid        = i.userid                                      \n");
            }
            
            //sbSQL.append(" order by b.score desc, b.avreport desc, b.avftest desc, b.avetc2 desc, a.birth_date asc                     \n");
            
            sbSQL.append(" order by b.examnum, b.score desc, b.avreport desc, b.avftest desc, b.avetc2 desc, fn_crypt('2', a.birth_date, 'knise') asc                     \n");
            
System.out.println("수료여부::::::::::" + sbSQL.toString());           
            ls = connMgr.executeQuery(sbSQL.toString());
        
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
//                dbox.put("d_tstep"  , new Double(ls.getDouble("tstep"   )));
//                dbox.put("d_ftest"  , new Double(ls.getDouble("ftest"   )));
//                dbox.put("d_mtest"  , new Double(ls.getDouble("mtest"   )));
//                dbox.put("d_report" , new Double(ls.getDouble("report"  )));
                dbox.put("d_score"  , new Double(ls.getDouble("score"   )));
                
                list.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
     
     
    /**
    과정별 교육실적
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList perform_04_01_01(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
         
        DataBox             dbox            = null;
         
        String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
        String              v_year          = box.getStringDefault  ( "p_year"         , ""    ); // 년도
        String              v_subjseq       = box.getStringDefault  ( "p_subjseq"      , ""    ); // 과목 기수
        String              v_subj          = box.getStringDefault  ( "p_subj"         , ""    ); // 과목&코스
        String              s_gadmin        = box.getSession        ( "gadmin"                 );
        String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
        String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
        
        try { 
            connMgr = new DBConnectionManager();
            list    = new ArrayList();
            
            sbSQL.append(" SELECT  a.userid                                                                                    \n") 
                 .append("     ,   a.name                                                                                      \n")
                 .append("     ,   a.email                                                                                     \n")
                 .append("     ,   a.handphone                                                                                 \n")
                 .append("     ,   a.hometel                                                                                   \n")
                 .append("     ,   a.zip_cd                                           post                    \n")
                 .append("     ,   a.address                                     addr                    \n")
                 .append("     ,   ''                                          issms                   \n")
                 .append("     ,   NVL(a.ismailling    , 'Y')                                          ismailling              \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate                  \n")
                 .append("     ,   c.subjnm                                                                                    \n")
                 .append("     ,   c.subj                                                                                      \n")
                 .append("     ,   c.subjseq                                                                                   \n")
                 .append("     ,   c.year                                                                                      \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(c.edustart, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') EduStart               \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(c.eduend  , 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') EduEnd                 \n")
                 .append("     ,   ROUND(b.tstep    , 2)    tstep                                                              \n")
                 .append("     ,   ROUND(b.score    , 2)    score                                                              \n")
                 .append("     ,   ROUND(b.mtest    , 2)    mtest                                                              \n")
                 .append("     ,   ROUND(b.ftest    , 2)    ftest                                                              \n")
                 .append("     ,   ROUND(b.report   , 2)    report                                                             \n")
                 .append("  ,   b.isgraduated                                                                                  \n")
                 .append("  ,   DECODE(c.isclosed, 'N', '미수료', DECODE(b.isgraduated, 'N', '미수료', '수료'))     isgraduatedname            \n")
                 .append(" FROM    tz_member       a                                                                           \n")
                 .append("     ,   tz_student      b                                                                           \n")
                 .append("     ,   VZ_SCSUBJSEQ    c                                                                           \n");
                 
            if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                sbSQL.append("  ,  vz_orgmember     i                                                                                                                                                       \n");
            }
            
            sbSQL.append(" WHERE   1           = 1                                                                             \n")
                 .append(" AND     c.subj      = b.subj                                                                        \n")
                 .append(" AND     c.subjseq   = b.subjseq                                                                     \n")
                 .append(" AND     c.year      = b.year                                                                        \n")
                 .append(" AND     b.userid    = a.userid                                                                      \n");
            
            if ( !v_grcode.equals("") ) { 
                sbSQL.append(" and c.grcode        = " + StringManager.makeSQL(v_grcode  ) + "   \n");
            }
            
            if ( !v_year.equals("") ) { 
                sbSQL.append(" and c.year          = " + StringManager.makeSQL(v_year    ) + "   \n");
            }
            
            if ( !v_subj.equals("") ) { 
                sbSQL.append(" and c.subj          = " + StringManager.makeSQL(v_subj    ) + "   \n");
            }
             
            if ( !v_subjseq.equals("") ) { 
                sbSQL.append(" and c.subjseq       = " + StringManager.makeSQL(v_subjseq ) + "   \n");
            }
            
            if ( v_isgraduated.equals("Y") ) {
                sbSQL.append(" and b.isgraduated    = " + StringManager.makeSQL(v_isgraduated   ) + "   \n")
                     .append(" and c.isclosed       = 'Y'                                               \n");
            } else if ( v_isgraduated.equals("N") ) {
                sbSQL.append(" and ( c.isclosed       = 'N' OR                                                                          \n")
                     .append("       ( b.isgraduated   IS NULL OR b.isgraduated = " + StringManager.makeSQL(v_isgraduated   ) + " )     \n")
                     .append("     )                                                                                                    \n");
            }
             
            if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                sbSQL.append(" AND     i.orgcode       = " + v_orgcode + "                             \n")
                     .append(" and     a.userid        = i.userid                                      \n");
            }
            
            sbSQL.append(" ORDER BY c.subjnm, c.subj, c.subjseq, a.name, a.userid                      \n");
            
            ls = connMgr.executeQuery(sbSQL.toString());
     
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                dbox.put("d_tstep"  , new Double(ls.getDouble("tstep"   )));
                dbox.put("d_ftest"  , new Double(ls.getDouble("ftest"   )));
                dbox.put("d_mtest"  , new Double(ls.getDouble("mtest"   )));
                dbox.put("d_report" , new Double(ls.getDouble("report"  )));
                dbox.put("d_score"  , new Double(ls.getDouble("score"   )));
                
                list.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return list;
    }
      
      
    /**
     과정별 교육실적
     @param box      receive from the form object and session
     @return ArrayList
    */
    public ArrayList perform_05_01_01(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
         
        DataBox             dbox            = null;
         
        String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
        String              v_year          = box.getStringDefault  ( "p_year"         , ""    ); // 년도
        String              v_subjseq       = box.getStringDefault  ( "p_subjseq"      , ""    ); // 과목 기수
        String              v_subj          = box.getStringDefault  ( "p_subj"         , ""    ); // 과목&코스
        String              s_gadmin        = box.getSession        ( "gadmin"                 );
        String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
        String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
        
        try { 
            connMgr = new DBConnectionManager();
            list    = new ArrayList();
              
            sbSQL.append(" SELECT  a.userid                                                                                    \n") 
                 .append("     ,   a.name                                                                                      \n")
                 .append("     ,   a.email                                                                                     \n")
                 .append("     ,   a.handphone                                                                                 \n")
                 .append("     ,   a.hometel                                                                                   \n")
                 .append("     ,   a.zip_cd                                           post                    \n")
                 .append("     ,   a.address                                     addr                    \n")
                 .append("     ,   ''                                          issms                   \n")
                 .append("     ,   NVL(a.ismailling    , 'Y')                                          ismailling              \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate                  \n")
                 .append("     ,   c.subjnm                                                                                    \n")
                 .append("     ,   c.subj                                                                                      \n")
                 .append("     ,   c.subjseq                                                                                   \n")
                 .append("     ,   c.year                                                                                      \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(c.edustart, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') EduStart               \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(c.eduend  , 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') EduEnd                 \n")
                 .append("     ,   ROUND(b.tstep    , 2)    tstep                                                              \n")
                 .append("     ,   ROUND(b.score    , 2)    score                                                              \n")
                 .append("     ,   ROUND(b.mtest    , 2)    mtest                                                              \n")
                 .append("     ,   ROUND(b.ftest    , 2)    ftest                                                              \n")
                 .append("     ,   ROUND(b.report   , 2)    report                                                             \n")
                 .append("  ,   b.isgraduated                                                                                  \n")
                 .append("  ,   DECODE(c.isclosed, 'N', '미수료', DECODE(b.isgraduated, 'N', '미수료', '수료'))     isgraduatedname            \n")
                 .append(" FROM    tz_member       a                                                                           \n")
                 .append("     ,   tz_student      b                                                                           \n")
                 .append("     ,   VZ_SCSUBJSEQ    c                                                                           \n");
                 
            if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                sbSQL.append("  ,  vz_orgmember     i                                                                                                                                                       \n");
            }
            
            sbSQL.append(" WHERE   1           = 1                                                                             \n")
                 .append(" AND     c.subj      = b.subj                                                                        \n")
                 .append(" AND     c.subjseq   = b.subjseq                                                                     \n")
                 .append(" AND     c.year      = b.year                                                                        \n")
                 .append(" AND     b.userid    = a.userid                                                                      \n");
            
            if ( !v_grcode.equals("") ) { 
                sbSQL.append(" and c.grcode        = " + StringManager.makeSQL(v_grcode  ) + "   \n");
            }
            
            if ( !v_year.equals("") ) { 
                sbSQL.append(" and c.year          = " + StringManager.makeSQL(v_year    ) + "   \n");
            }
            
            if ( !v_subj.equals("") ) { 
                sbSQL.append(" and c.subj          = " + StringManager.makeSQL(v_subj    ) + "   \n");
            }
             
            if ( !v_subjseq.equals("") ) { 
                sbSQL.append(" and c.subjseq       = " + StringManager.makeSQL(v_subjseq ) + "   \n");
            }
            
            if ( v_isgraduated.equals("Y") ) {
                sbSQL.append(" and b.isgraduated    = " + StringManager.makeSQL(v_isgraduated   ) + "   \n")
                     .append(" and c.isclosed       = 'Y'                                               \n");
            } else if ( v_isgraduated.equals("N") ) {
                sbSQL.append(" and ( c.isclosed       = 'N' OR                                                                          \n")
                     .append("       ( b.isgraduated   IS NULL OR b.isgraduated = " + StringManager.makeSQL(v_isgraduated   ) + " )     \n")
                     .append("     )                                                                                                    \n");
            }
             
            if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                sbSQL.append(" AND     i.orgcode       = " + v_orgcode + "                             \n")
                     .append(" and     a.userid        = i.userid                                      \n");
            }
            
            sbSQL.append(" ORDER BY c.subjnm, c.subj, c.subjseq, a.name, a.userid                      \n");
            
            ls = connMgr.executeQuery(sbSQL.toString());
      
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                dbox.put("d_tstep"  , new Double(ls.getDouble("tstep"   )));
                dbox.put("d_ftest"  , new Double(ls.getDouble("ftest"   )));
                dbox.put("d_mtest"  , new Double(ls.getDouble("mtest"   )));
                dbox.put("d_report" , new Double(ls.getDouble("report"  )));
                dbox.put("d_score"  , new Double(ls.getDouble("score"   )));
                
                list.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return list;
    }
       
       
    /**
    과정별 교육실적
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList perform_06_01_01(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
         
        DataBox             dbox            = null;
         
        String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
        String              v_year          = box.getStringDefault  ( "p_year"         , ""    ); // 년도
        String              v_subjseq       = box.getStringDefault  ( "p_subjseq"      , ""    ); // 과목 기수
        String              v_subj          = box.getStringDefault  ( "p_subj"         , ""    ); // 과목&코스
        String              s_gadmin        = box.getSession        ( "gadmin"                 );
        String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , "ALL"    ); // 조직코드
        String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
        
       try { 
            connMgr = new DBConnectionManager();
            list    = new ArrayList();
           
            sbSQL.append(" SELECT  a.userid                                                                                    \n") 
                 .append("     ,   a.name                                                                                      \n")
                 .append("     ,   a.email                                                                                     \n")
                 .append("     ,   a.handphone                                                                                 \n")
                 .append("     ,   a.hometel                                                                                   \n")
                 .append("     ,   a.zip_cd                                           post                    \n")
                 .append("     ,   a.address                                     addr                    \n")
                 .append("     ,   ''                                          issms                   \n")
                 .append("     ,   NVL(a.ismailling    , 'Y')                                          ismailling              \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate                  \n")
                 .append("     ,   c.subjnm                                                                                    \n")
                 .append("     ,   c.subj                                                                                      \n")
                 .append("     ,   c.subjseq                                                                                   \n")
                 .append("     ,   c.year                                                                                      \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(c.edustart, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') EduStart               \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(c.eduend  , 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') EduEnd                 \n")
                 .append("     ,   ROUND(b.tstep    , 2)    tstep                                                              \n")
                 .append("     ,   ROUND(b.score    , 2)    score                                                              \n")
                 .append("     ,   ROUND(b.mtest    , 2)    mtest                                                              \n")
                 .append("     ,   ROUND(b.ftest    , 2)    ftest                                                              \n")
                 .append("     ,   ROUND(b.report   , 2)    report                                                             \n")
                 .append("  ,   b.isgraduated                                                                                  \n")
                 .append("  ,   DECODE(c.isclosed, 'N', '미수료', DECODE(b.isgraduated, 'N', '미수료', '수료'))     isgraduatedname            \n")
                 .append(" FROM    tz_member       a                                                                           \n")
                 .append("     ,   tz_student      b                                                                           \n")
                 .append("     ,   VZ_SCSUBJSEQ    c                                                                           \n");
                
            if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                sbSQL.append("  ,  vz_orgmember     i                                                                                                                                                       \n");
            }
           
            sbSQL.append(" WHERE   1           = 1                                                                             \n")
                 .append(" AND     c.subj      = b.subj                                                                        \n")
                 .append(" AND     c.subjseq   = b.subjseq                                                                     \n")
                 .append(" AND     c.year      = b.year                                                                        \n")
                 .append(" AND     b.userid    = a.userid                                                                      \n");
            
            if ( !v_grcode.equals("") ) { 
                sbSQL.append(" and c.grcode        = " + StringManager.makeSQL(v_grcode  ) + "   \n");
            }
            
            if ( !v_year.equals("") ) { 
                sbSQL.append(" and c.year          = " + StringManager.makeSQL(v_year    ) + "   \n");
            }
            
            if ( !v_subj.equals("") ) { 
                sbSQL.append(" and c.subj          = " + StringManager.makeSQL(v_subj    ) + "   \n");
            }
             
            if ( !v_subjseq.equals("") ) { 
                sbSQL.append(" and c.subjseq       = " + StringManager.makeSQL(v_subjseq ) + "   \n");
            }
            
            if ( v_isgraduated.equals("Y") ) {
                sbSQL.append(" and b.isgraduated    = " + StringManager.makeSQL(v_isgraduated   ) + "   \n")
                     .append(" and c.isclosed       = 'Y'                                               \n");
            } else if ( v_isgraduated.equals("N") ) {
                sbSQL.append(" and ( c.isclosed       = 'N' OR                                                                          \n")
                     .append("       ( b.isgraduated   IS NULL OR b.isgraduated = " + StringManager.makeSQL(v_isgraduated   ) + " )     \n")
                     .append("     )                                                                                                    \n");
            }
            
            if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                sbSQL.append(" AND     i.orgcode       = " + v_orgcode + "                             \n")
                     .append(" and     a.userid        = i.userid                                      \n");
            }
           
            sbSQL.append(" ORDER BY c.subjnm, c.subj, c.subjseq, a.name, a.userid                      \n");
           
            ls = connMgr.executeQuery(sbSQL.toString());
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
               
                dbox.put("d_tstep"  , new Double(ls.getDouble("tstep"   )));
                dbox.put("d_ftest"  , new Double(ls.getDouble("ftest"   )));
                dbox.put("d_mtest"  , new Double(ls.getDouble("mtest"   )));
                dbox.put("d_report" , new Double(ls.getDouble("report"  )));
                dbox.put("d_score"  , new Double(ls.getDouble("score"   )));
                
                list.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
       
        return list;
    }
       

    /**
    과정별 교육실적
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList perform_07_01_01(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
         
        DataBox             dbox            = null;
         
        String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
        String              v_year          = box.getStringDefault  ( "p_year"         , ""    ); // 년도
        String              v_subjseq       = box.getStringDefault  ( "p_subjseq"      , ""    ); // 과목 기수
        String              v_subj          = box.getStringDefault  ( "p_subj"         , ""    ); // 과목&코스
        String              s_gadmin        = box.getSession        ( "gadmin"                 );
        String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
        String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
        String              v_sido          = box.getString         ( "p_sido"                 );
        String              v_gugun         = box.getString         ( "p_gugun"                );
        String              v_gender        = box.getString         ( "p_gender"               );
        String              v_achievement   = box.getString         ( "p_achievement"          );
        String              v_age           = box.getString         ( "p_age"                  );
        String              v_gubun         = box.getString         ( "p_gubun"                );
        String              v_grseq         = box.getString         ( "p_grseq"                );
        
        try { 
            connMgr = new DBConnectionManager();
            list    = new ArrayList();
            
            sbSQL.append(" SELECT  a.userid                                                                                    \n") 
                 .append("     ,   a.name                                                                                      \n")
                 .append("     ,   a.email                                                                                     \n")
                 .append("     ,   a.handphone                                                                                 \n")
                 .append("     ,   a.hometel                                                                                   \n")
                 .append("     ,   a.zip_cd                                           post                    \n")
                 .append("     ,   a.address                                     addr                    \n")
                 .append("     ,   ''                                          issms                   \n")
                 .append("     ,   NVL(a.ismailling    , 'Y')                                          ismailling              \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate                  \n")
                 .append("     ,   c.subjnm                                                                                    \n")
                 .append("     ,   c.subj                                                                                      \n")
                 .append("     ,   c.subjseq                                                                                   \n")
                 .append("     ,   c.year                                                                                      \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(c.edustart, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') EduStart               \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(c.eduend  , 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') EduEnd                 \n")
                 .append("     ,   ROUND(b.tstep    , 2)    tstep                                                              \n")
                 .append("     ,   ROUND(b.score    , 2)    score                                                              \n")
                 .append("     ,   ROUND(b.mtest    , 2)    mtest                                                              \n")
                 .append("     ,   ROUND(b.ftest    , 2)    ftest                                                              \n")
                 .append("     ,   ROUND(b.report   , 2)    report                                                             \n")
                 .append("  ,   b.isgraduated                                                                                  \n")
                 .append("  ,   DECODE(c.isclosed, 'N', '미수료', DECODE(b.isgraduated, 'N', '미수료', '수료'))     isgraduatedname            \n")
                 .append(" FROM    tz_member       a                                                                           \n")
                 .append("     ,   tz_student      b                                                                           \n")
                 .append("     ,   VZ_SCSUBJSEQ    c                                                                           \n");
            
            if ( (!v_sido.equals("") || !v_age.equals("") || !v_achievement.equals("") || !v_gender.equals("") || !v_gugun.equals("")) && !(v_orgcode.equals("") || v_orgcode.equals("ALL")) ) { 
                sbSQL.append("  ,  vz_orgmember     i                                                                                                                                                       \n");
            } else if ( (!v_sido.equals("") || !v_age.equals("") || !v_achievement.equals("") || !v_gender.equals("") || !v_gugun.equals("")) && (v_orgcode.equals("") || v_orgcode.equals("ALL")) ) {
                sbSQL.append("  ,  vz_orgmember1     i                                                                                                                                                       \n");
            }
            
            sbSQL.append(" WHERE   1           = 1                                                                             \n")
                 .append(" AND     c.subj      = b.subj                                                                        \n")
                 .append(" AND     c.subjseq   = b.subjseq                                                                     \n")
                 .append(" AND     c.year      = b.year                                                                        \n")
                 .append(" AND     b.userid    = a.userid                                                                      \n");
            
            if ( !v_grcode.equals("") ) { 
                sbSQL.append(" and c.grcode        = " + StringManager.makeSQL(v_grcode  ) + "   \n");
            }
            
            if ( !(v_grseq.equals("ALL") || v_grseq.equals("")) ) { 
                sbSQL.append(" and c.grseq         = " + StringManager.makeSQL(v_grseq   ) + "   \n");
            }
            
            if ( !v_year.equals("") ) { 
                sbSQL.append(" and c.year          = " + StringManager.makeSQL(v_year    ) + "   \n");
            }
            
            if ( !v_subj.equals("") ) { 
                sbSQL.append(" and c.subj          = " + StringManager.makeSQL(v_subj    ) + "   \n");
            }
             
            if ( !v_subjseq.equals("") ) { 
                sbSQL.append(" and c.subjseq       = " + StringManager.makeSQL(v_subjseq ) + "   \n");
            }
            
            if ( v_isgraduated.equals("Y") ) {
                sbSQL.append(" and b.isgraduated    = " + StringManager.makeSQL(v_isgraduated   ) + "   \n")
                     .append(" and c.isclosed       = 'Y'                                               \n");
            } else if ( v_isgraduated.equals("N") ) {
                sbSQL.append(" and ( c.isclosed       = 'N' OR                                                                          \n")
                     .append("       ( b.isgraduated   IS NULL OR b.isgraduated = " + StringManager.makeSQL(v_isgraduated   ) + " )     \n")
                     .append("     )                                                                                                    \n");
            }
            
            if ( (!v_sido.equals("") || !v_age.equals("") || !v_achievement.equals("") || !v_gender.equals("") || !v_gugun.equals("") || !(v_orgcode.equals("ALL") || v_orgcode.equals("ALL")) ) ) { 
                sbSQL.append("                          AND     a.userid        = i.userid(+)                                                       \n");
            }
            
            if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL")) ) { 
                sbSQL.append("                          AND     i.orgcode       = " + v_orgcode                                 + "                 \n");
            }
            
            if ( !v_sido.equals("") ) {
                sbSQL.append("             AND trim(i.sido)               = " + StringManager.makeSQL(v_sido          ) + "                                       \n");
            }    

            if ( !v_age.equals("") ) {
                sbSQL.append("             AND trim(i.age)               = " + StringManager.makeSQL(v_age            ) + "                                       \n");               
            }
            
            if ( !v_achievement.equals("") ) {
                sbSQL.append("             AND trim(i.achievement)       = " + StringManager.makeSQL(v_achievement    ) + "                                       \n");               
            }
            
            if ( !v_gender.equals("") ) {
                sbSQL.append("             AND trim(i.gender)            = " + StringManager.makeSQL(v_gender         ) + "                                       \n");               
            }
            
            if ( !v_gugun.equals("") ) {
                sbSQL.append("             AND trim(i.gugun)             = " + StringManager.makeSQL(v_gugun          ) + "                                       \n");
            }
            
            if ( !box.getString("p_sdate").equals("") && !box.getString("p_ldate").equals("") ) { // 기간선택
                if ( box.getString("p_sdate").equals(box.getString("p_ldate")) ) {
                    sbSQL.append("          AND TO_CHAR(TO_DATE(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') \n");
                } else {
                    sbSQL.append("          AND TO_CHAR(TO_DATE(a.indate, 'yyyymmddhh24miss'), 'yyyymmdd') BETWEEN TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') AND TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_ldate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') \n");
                }
            }
             
            sbSQL.append(" ORDER BY c.subjnm, c.subj, c.subjseq, a.name, a.userid                      \n");
            
            ls = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                dbox.put("d_tstep"  , new Double(ls.getDouble("tstep"   )));
                dbox.put("d_ftest"  , new Double(ls.getDouble("ftest"   )));
                dbox.put("d_mtest"  , new Double(ls.getDouble("mtest"   )));
                dbox.put("d_report" , new Double(ls.getDouble("report"  )));
                dbox.put("d_score"  , new Double(ls.getDouble("score"   )));
                
                list.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return list;
    }
    

    /**
    과정별 교육실적
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList perform_08_01_01(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
         
        DataBox             dbox            = null;
         
        String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
        String              v_year          = box.getStringDefault  ( "p_year"         , ""    ); // 년도
        String              s_gadmin        = box.getSession        ( "gadmin"                 );
        String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
        String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
        String              v_upperclass    = box.getString         ( "p_upperclass"           );
        String              v_grseq         = box.getString         ( "p_grseq"                );
        
        try { 
            connMgr = new DBConnectionManager();
            list    = new ArrayList();
            
            sbSQL.append(" SELECT  a.userid                                                                                    \n") 
                 .append("     ,   a.name                                                                                      \n")
                 .append("     ,   a.email                                                                                     \n")
                 .append("     ,   a.handphone                                                                                 \n")
                 .append("     ,   a.hometel                                                                                   \n")
                 .append("     ,   a.zip_cd                                           post                    \n")
                 .append("     ,   a.address                                     addr                    \n")
                 .append("     ,   ''                                          issms                   \n")
                 .append("     ,   NVL(a.ismailling    , 'Y')                                          ismailling              \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate                  \n")
                 .append("     ,   c.subjnm                                                                                    \n")
                 .append("     ,   c.subj                                                                                      \n")
                 .append("     ,   c.subjseq                                                                                   \n")
                 .append("     ,   c.year                                                                                      \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(c.edustart, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') EduStart               \n")
                 .append("     ,   TO_CHAR(TO_DATE(SUBSTR(c.eduend  , 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') EduEnd                 \n")
                 .append("     ,   ROUND(b.tstep    , 2)    tstep                                                              \n")
                 .append("     ,   ROUND(b.score    , 2)    score                                                              \n")
                 .append("     ,   ROUND(b.mtest    , 2)    mtest                                                              \n")
                 .append("     ,   ROUND(b.ftest    , 2)    ftest                                                              \n")
                 .append("     ,   ROUND(b.report   , 2)    report                                                             \n")
                 .append("  ,   b.isgraduated                                                                                  \n")
                 .append("  ,   DECODE(c.isclosed, 'N', '미수료', DECODE(b.isgraduated, 'N', '미수료', '수료'))     isgraduatedname            \n")
                 .append(" FROM    tz_member       a                                                                           \n")
                 .append("     ,   tz_student      b                                                                           \n")
                 .append("     ,   VZ_SCSUBJSEQ    c                                                                           \n");
            
            if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                sbSQL.append("  ,  vz_orgmember     i                                                                                                                                                       \n");
            }
           
            sbSQL.append(" WHERE   1           = 1                                                                             \n")
                 .append(" AND     c.subj      = b.subj                                                                        \n")
                 .append(" AND     c.subjseq   = b.subjseq                                                                     \n")
                 .append(" AND     c.year      = b.year                                                                        \n")
                 .append(" AND     b.userid    = a.userid                                                                      \n");
            
            if ( !v_grcode.equals("") ) { 
                sbSQL.append(" and c.grcode        = " + StringManager.makeSQL(v_grcode     ) + "   \n");
            }
            
            if ( !v_year.equals("") ) { 
                sbSQL.append(" and c.year          = " + StringManager.makeSQL(v_year       ) + "   \n");
            }
            
            if ( !v_upperclass.equals("") ) { 
                sbSQL.append(" and c.scupperclass  = " + StringManager.makeSQL(v_upperclass ) + "   \n");
            }
             
            if ( !v_grseq.equals("") ) { 
                sbSQL.append(" and c.grseq         = " + StringManager.makeSQL(v_grseq      ) + "   \n");
            }
            
            if ( v_isgraduated.equals("Y") ) {
                sbSQL.append(" and b.isgraduated    = " + StringManager.makeSQL(v_isgraduated   ) + "   \n")
                     .append(" and c.isclosed       = 'Y'                                               \n");
            } else if ( v_isgraduated.equals("N") ) {
                sbSQL.append(" and ( c.isclosed       = 'N' OR                                                                          \n")
                     .append("       ( b.isgraduated   IS NULL OR b.isgraduated = " + StringManager.makeSQL(v_isgraduated   ) + " )     \n")
                     .append("     )                                                                                                    \n");
            }
            
            if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                sbSQL.append(" AND     i.orgcode       = " + v_orgcode + "                             \n")
                     .append(" and     a.userid        = i.userid                                      \n");
            }
           
            sbSQL.append(" ORDER BY c.subjnm, c.subj, c.subjseq, a.name, a.userid                      \n");
            
            ls = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                dbox.put("d_tstep"  , new Double(ls.getDouble("tstep"   )));
                dbox.put("d_ftest"  , new Double(ls.getDouble("ftest"   )));
                dbox.put("d_mtest"  , new Double(ls.getDouble("mtest"   )));
                dbox.put("d_report" , new Double(ls.getDouble("report"  )));
                dbox.put("d_score"  , new Double(ls.getDouble("score"   )));
                
                list.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return list;
    }
}

