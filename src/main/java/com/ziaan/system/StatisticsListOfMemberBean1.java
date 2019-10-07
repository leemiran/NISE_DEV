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

public class StatisticsListOfMemberBean1 {

    // private
    private ConfigSet config;
    private int         row;
    
    public StatisticsListOfMemberBean1() {
    }

    public ArrayList perform_01_01_01_MemberJoin_Member(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String p_name = box.getString("p_name");
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                                                                                                  \n";
            sql += "       tz_member a                                                                                  \n";
            if (!box.getString("p_orgcode").equals("ALL")) {
                sql += "       , vz_zipcode b, tz_organization c, tz_organization_mat d       \n";
            }
            sql += " where                                                                                                  \n";
            sql += "       1=1                                                                                              \n";
            if (!box.getString("p_orgcode").equals("ALL")) {
                sql += "   and a.post1||'-'||a.post2 = b.zipcode(+)           \n";
                sql += "   and b.gugun = d.gugun           \n";
                sql += "   and b.sido = c.sido           \n";
                sql += "   and c.orgcode = d.orgcode           \n";
                sql += "   and c.orgcode = " + SQLString.Format(p_orgcode) + "         \n";
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

            if(p_name.equals("기타")) {
                sql += "   and a.indate is null           \n";                
            } else if (p_name.equals("합계")) {
                ;
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyy')||'년'||to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'mm')||'월' = "+SQLString.Format(p_name)+"           \n";    
            }
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
            sql += "        a.post1||'-'||a.post2 = b.zipcode(+)                \n";
            
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
            
            sql += "   and b.sido = "+SQLString.Format(p_name)+"            \n";
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                                         \n";
            sql += "      tz_member a,                             \n";
            sql += "      vz_zipcode b                             \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "       ,tz_organization d \n";
                sql += "       ,tz_organization_mat e             \n";
            }
            sql += " where                                         \n";
            sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
            sql += "   and b.sido = " + SQLString.Format(v_sido) + "                        \n";
            sql += "   and b.gugun = " + SQLString.Format(p_name) + "                        \n";
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                                         \n";
            sql += "      tz_member a,                             \n";
            sql += "      vz_zipcode b                             \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "       ,tz_organization d \n";
                sql += "       ,tz_organization_mat e             \n";
            }
            
            sql += " where                                         \n";
            sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
            sql += "   and b.sido  = " + SQLString.Format(v_sido) + "                        \n";
            sql += "   and b.gugun = " + SQLString.Format(v_gugun) + "                        \n";
            sql += "   and b.dong = " + SQLString.Format(v_name) + "                        \n";

            if (box.getString("p_orderStr").equals("cnt")) {
                sql += " order by cnt " + box.getString("p_orderType")
                        + "        \n";
            }
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");
        if(v_name.equals("여성")) v_name = "F";
        else if ( v_name.equals("남성") ) v_name = "M";
        else v_name = "";
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
            sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
                sql += " and (a.gender IS NULL OR a.gender NOT IN ('M', 'F') )             \n";
            } else {
                sql += " and a.gender = "+SQLString.Format(v_name)+"                       \n";
            }
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
            sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
                sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
            }
            else
            {
                sql += "               and (a.birth_date is null or substr(a.birth_date, 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
            }
            
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
            sql += "       b.post1 || '-' || b.post2    post                                                \n";
            sql += "      , b.address1 || ' ' || b.address2     addr                                        \n";
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
            sql += "    and a.code = b.NATIONALITY(+)   \n";
            sql += "    and a.codenm = "+SQLString.Format(v_name)+"         \n";
            
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";        
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
                sql += "       b.post1 || '-' || b.post2    post                                                \n";
                sql += "      , b.address1 || ' ' || b.address2     addr                                        \n";
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
                sql += "   and a.codenm = "+SQLString.Format(v_name)+"                                                                                     \n";
            }
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
                
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
                
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
                sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                sql += "   and f.gubun = '0063'\n";
                sql += "   and a.achievement = f.code\n";
            }
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
                
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
                
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
                sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                sql += "   and f.gubun = '0064'\n";
                sql += "   and a.jikup = f.code\n";
            }
            
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
                
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
                
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
                sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                sql += "   and f.gubun = '0065'\n";
                sql += "   and a.entrytype = f.code\n";
            }
            
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
                
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
                
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
                sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                sql += "   and f.gubun = '0066'\n";
                sql += "   and a.entrypath = f.code\n";
            }
           
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
            sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
            sql += "   and nvl(a.iswedding, ' ') = "+SQLString.Format(v_name)+"   \n";
           
            
            System.out.println(sql);
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
        
        String p_orgcode = box.getString("p_orgcode");
        String p_name = box.getString("p_name");
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
            if (!box.getString("p_orgcode").equals("ALL")) {
                sql += "       , vz_zipcode b, tz_organization c, tz_organization_mat d       \n";
            }
            sql += " where                                                                                                  \n";
            sql += "       1=1                                                                                              \n";
            sql += " and   a.userid = zz.userid                                                                             \n";
            if (!box.getString("p_orgcode").equals("ALL")) {
                sql += "   and a.post1||'-'||a.post2 = b.zipcode(+)           \n";
                sql += "   and b.gugun = d.gugun           \n";
                sql += "   and b.sido = c.sido           \n";
                sql += "   and c.orgcode = d.orgcode           \n";
                sql += "   and c.orgcode = " + SQLString.Format(p_orgcode) + "         \n";
            }

            if(p_name.equals("기타")) {
                sql += "   and a.indate is null           \n";                
            } else if (p_name.equals("합계")) {
                ;
            } else {
                sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyy')||'년'||to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'mm')||'월' = "+SQLString.Format(p_name)+"           \n";    
            }
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
            sql += "        a.post1||'-'||a.post2 = b.zipcode(+)                \n";
            
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
            
            sql += "   and b.sido = "+SQLString.Format(p_name)+"            \n";
            sql += "    and a.userid = c.userid                                \n";
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
            sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
            sql += "   and b.sido = " + SQLString.Format(v_sido) + "                        \n";
            sql += "   and b.gugun = " + SQLString.Format(p_name) + "                        \n";
            sql += "    and a.userid = c.userid                                \n";
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
            sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
            sql += "   and b.sido  = " + SQLString.Format(v_sido) + "                        \n";
            sql += "   and b.gugun = " + SQLString.Format(v_gugun) + "                        \n";
            sql += "   and b.dong = " + SQLString.Format(v_name) + "                        \n";
            sql += "    and a.userid = c.userid                                \n";
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
        String              sql        = "";        
        
        String p_orgcode = box.getString("p_orgcode");
        String v_name = box.getString("p_name");
        if(v_name.equals("여성")) v_name = "F";
        else if (v_name.equals("남성")) v_name = "M";
        else v_name = "";
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += " select a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
            sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
                sql += " and (a.gender IS NULL OR a.gender NOT IN ('M', 'F') )             \n";
            } else {
                sql += " and a.gender = "+SQLString.Format(v_name)+"                       \n";
            }
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
            sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
                sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
            }
            else
            {
                sql += "               and (a.birth_date is null or substr(a.birth_date, 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
            }
            
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
            sql += "       b.post1 || '-' || b.post2    post                                                \n";
            sql += "      , b.address1 || ' ' || b.address2     addr                                        \n";
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
            sql += "    and a.code = b.NATIONALITY(+)   \n";
            sql += "    and a.codenm = "+SQLString.Format(v_name)+"   \n";
            
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";    
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
                sql += "       b.post1 || '-' || b.post2    post                                                \n";
                sql += "      , b.address1 || ' ' || b.address2     addr                                        \n";
                sql += "      , NVL(b.issms, 'Y') issms                                                         \n";
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
                sql += "   and a.codenm = "+SQLString.Format(v_name)+"                                                                                     \n";
            }
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
                sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                sql += "   and f.gubun = '0063'\n";
                sql += "   and a.achievement = f.code\n";
            }
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
                sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                sql += "   and f.gubun = '0064'\n";
                sql += "   and a.jikup = f.code\n";
            }
            
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
                sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                sql += "   and f.gubun = '0065'\n";
                sql += "   and a.entrytype = f.code\n";
            }
            
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
                sql += "    and f.codenm = "+SQLString.Format(v_name)+"   \n";
                sql += "   and f.gubun = '0066'\n";
                sql += "   and a.entrypath = f.code\n";
            }
           
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
            sql += "       a.post1||'-'||a.post2 = b.zipcode(+)       \n";
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
            sql += "   and nvl(a.iswedding, ' ') = "+SQLString.Format(v_name)+"   \n";
           
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "        a.post1||'-'||a.post2 = b.zipcode(+)                \n";
                
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
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (a.birth_date is null or substr(a.birth_date, 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "        a.post1||'-'||a.post2 = b.zipcode(+)                \n";
                
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
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (a.birth_date is null or substr(a.birth_date, 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
                }
            }
            else
            {
                sql += " select a.userid,                                                                       \n";
                sql += "        a.name,                                                                         \n";
                sql += "        a.email,                                                                        \n";
                sql += "        a.handphone,                                                                    \n";
                sql += "       a.hometel,                                                                       \n";
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "        a.post1||'-'||a.post2 = b.zipcode(+)                \n";
                
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
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (a.birth_date is null or substr(a.birth_date, 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
                }     
                sql += "   and f.codenm = "+SQLString.Format(c_achievement)+"            \n";
                sql += "   and f.gubun = '0063'\n";
                sql += "   and a.achievement = f.code\n";
            }
            
            System.out.println(sql);
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
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "        a.post1||'-'||a.post2 = b.zipcode(+)                \n";
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
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (a.birth_date is null or substr(a.birth_date, 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "        a.post1||'-'||a.post2 = b.zipcode(+)                \n";
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
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (a.birth_date is null or substr(a.birth_date, 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
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
                sql += "       a.post1 || '-' || a.post2    post                                                \n";
                sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
                sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
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
                sql += "        a.post1||'-'||a.post2 = b.zipcode(+)                \n";
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
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (a.birth_date is null or substr(a.birth_date, 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
                }     
                sql += "   and f.codenm = "+SQLString.Format(c_achievement)+"            \n";
                sql += "   and f.gubun = '0063'\n";
                sql += "   and a.achievement = f.code\n";
            }
            
            System.out.println(sql);
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
    
    public ArrayList perform_07_01_01(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
        String              sql        = "";        
        
        String c_sido = box.getString("p_sido");
        String c_gugun = box.getString("p_gugun");
        String c_gender = box.getString("p_gender");
        String c_achievement = box.getString("p_achievement");
        String c_age = box.getString("p_age");
        String c_subj = box.getString("p_subj");
        String c_year = box.getString("p_year");
        String c_subjseq = box.getString("p_subjseq");

        if(c_gender.equals("여성")) c_gender = "F";
        else if(c_gender.equals("남성")) c_gender = "M";

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
                sql += "       a.userid,                                        \n";
                sql += "       a.name,                                        \n";
                sql += "       a.email,                       \n";
                sql += "       a.handphone                       \n";
                sql += "   from                                                     \n";
                sql += "        tz_member a,                                        \n";
                sql += "        vz_zipcode b   \n";           
                sql += "        ,tz_student c \n";
                sql += "        ,vz_scsubjseq g \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "  where                                                     \n";
                sql += "        a.post1||'-'||a.post2 = b.zipcode(+)                \n";
                sql += "    and a.userid = c.userid                \n";
                sql += "    and c.subj = g.subj and c.year = g.year and c.subjseq = g.subjseq                \n";
                sql += "    and g.subj = "+SQLString.Format(c_subj)+"\n";
                sql += "    and g.year = "+SQLString.Format(c_year)+"\n";
                sql += "    and g.subjseq = "+SQLString.Format(c_subjseq)+"\n";
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
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (a.birth_date is null or substr(a.birth_date, 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
                }
                sql += "   and a.achievement is null \n";
            }
            else if(c_achievement.equals(""))
            {
                sql += " select distinct                                                    \n";
                sql += "       a.userid,                                        \n";
                sql += "       a.name,                                        \n";
                sql += "       a.email,                       \n";
                sql += "       a.handphone                       \n";
                sql += "   from                                                     \n";
                sql += "        tz_member a,                                        \n";
                sql += "        vz_zipcode b   \n";                  
                sql += "        ,tz_student c \n";
                sql += "        ,vz_scsubjseq g \n";
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "  where                                                     \n";
                sql += "        a.post1||'-'||a.post2 = b.zipcode(+)                \n";
                sql += "    and a.userid = c.userid                \n";
                sql += "    and c.subj = g.subj and c.year = g.year and c.subjseq = g.subjseq                \n";
                sql += "    and g.subj = "+SQLString.Format(c_subj)+"\n";
                sql += "    and g.year = "+SQLString.Format(c_year)+"\n";
                sql += "    and g.subjseq = "+SQLString.Format(c_subjseq)+"\n";
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
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (a.birth_date is null or substr(a.birth_date, 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
                }
            }
            else
            {
                sql += " select distinct                                                    \n";
                sql += "       a.userid,                                        \n";
                sql += "       a.name,                                        \n";
                sql += "       a.email,                       \n";
                sql += "       a.handphone                       \n";
                sql += "   from                                                     \n";
                sql += "        tz_member a,                                        \n";
                sql += "        vz_zipcode b   \n";       
                sql += "        ,tz_student c \n";
                sql += "        ,tz_code f   \n";       
                sql += "        ,vz_scsubjseq g \n";         
                if(!v_orgcode.equals("ALL")){   // 조직선택
                    sql += "       ,tz_organization d \n";
                    sql += "       ,tz_organization_mat e             \n";
                }
                sql += "  where                                                     \n";
                sql += "        a.post1||'-'||a.post2 = b.zipcode(+)                \n";
                sql += "    and a.userid = c.userid                \n";
                sql += "    and c.subj = g.subj and c.year = g.year and c.subjseq = g.subjseq                \n";
                sql += "    and g.subj = "+SQLString.Format(c_subj)+"\n";
                sql += "    and g.year = "+SQLString.Format(c_year)+"\n";
                sql += "    and g.subjseq = "+SQLString.Format(c_subjseq)+"\n";
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
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) >= "+SQLString.Format(v_age1)+" \n";
                    sql += "               and to_char(sysdate, 'yyyy') - decode(substr(a.birth_date, 7, 1), '9', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '0', '18'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '1', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '2', '19'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '3', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '4', '20'||substr(a.birth_date, 0, 2)\n";
                    sql += "                                               , '' ) <= "+SQLString.Format(v_age2)+" \n";
                }
                else if(c_age.equals("기타"))
                {
                    sql += "               and (a.birth_date is null or substr(a.birth_date, 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
                }     
                sql += "   and f.codenm = "+SQLString.Format(c_achievement)+"            \n";
                sql += "   and f.gubun = '0063'\n";
                sql += "   and a.achievement = f.code\n";
            }
            
            System.out.println(sql);
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
        DBConnectionManager connMgr     = null;
        ListSet             ls         = null;        
        ArrayList           list       = null;        
        DataBox dbox = null;
        String              sql        = "";        
        
        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_orgcode = box.getString("p_orgcode");  // 조직코드
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sql += "select                                                          \n";
            sql += "        a.userid,                                                                       \n";
            sql += "        a.name,                                                                         \n";
            sql += "        a.email,                                                                        \n";
            sql += "        a.handphone,                                                                    \n";
            sql += "       a.hometel,                                                                       \n";
            sql += "       a.post1 || '-' || a.post2    post                                                \n";
            sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
            sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
            sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
            sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
            sql += "  from                                                          \n";
            sql += "       tz_member a                                              \n";
            sql += "      ,tz_student b                                             \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "              ,vz_zipcode\n";
                sql += "              ,tz_organization\n";
                sql += "              ,tz_organization_mat\n";
            }
            sql += " where                                                          \n";
            sql += "       a.userid = b.userid                                      \n";
            sql += "   and b.subj = "+SQLString.Format(v_subj)+"                    \n";
            sql += "   and b.year = "+SQLString.Format(v_year)+"                    \n";
            sql += "   and b.subjseq = "+SQLString.Format(v_subjseq)+"              \n";
            if(!v_orgcode.equals("ALL")){   // 조직선택
                sql += "           and b.userid = a.userid\n";
                sql += "           and a.post1||'-'||a.post2 = vz_zipcode.zipcode(+)\n";
                sql += "           and vz_zipcode.sido = tz_organization.sido               \n";
                sql += "           and vz_zipcode.gugun = tz_organization_mat.gugun                        \n";
                sql += "           and tz_organization.orgcode = tz_organization_mat.orgcode\n";
                sql += "           and tz_organization.orgcode = "+SQLString.Format(v_orgcode)+"   \n";
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
     과정별 수료현황
     @param box      receive from the form object and session
     @return ArrayList
     */
      public ArrayList perform_04_01_01(RequestBox box) throws Exception { 
         DBConnectionManager connMgr     = null;
         ListSet             ls         = null;        
         ArrayList           list       = null;        
         DataBox dbox = null;
         String              sql        = "";        
         
         String v_subj = box.getString("p_subj");
         String v_year = box.getString("p_year");
         String v_subjseq = box.getString("p_subjseq");
         String v_orgcode = box.getString("p_orgcode");  // 조직코드
         
         try { 
             connMgr = new DBConnectionManager();
             list   = new ArrayList();    

             sql += "select                                                          \n";
             sql += "        a.userid,                                                                       \n";
             sql += "        a.name,                                                                         \n";
             sql += "        a.email,                                                                        \n";
             sql += "        a.handphone,                                                                    \n";
             sql += "       a.hometel,                                                                       \n";
             sql += "       a.post1 || '-' || a.post2    post                                                \n";
             sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
             sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
             sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
             sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
             sql += "  from                                                          \n";
             sql += "       tz_member a                                              \n";
             sql += "      ,tz_student b                                             \n";
             if(!v_orgcode.equals("ALL")){   // 조직선택
                 sql += "              ,vz_zipcode\n";
                 sql += "              ,tz_organization\n";
                 sql += "              ,tz_organization_mat\n";
             }
             sql += " where                                                          \n";
             sql += "       a.userid = b.userid                                      \n";
             sql += "   and b.subj = "+SQLString.Format(v_subj)+"                    \n";
             sql += "   and b.year = "+SQLString.Format(v_year)+"                    \n";
             sql += "   and b.subjseq = "+SQLString.Format(v_subjseq)+"              \n";
             if(!v_orgcode.equals("ALL")){   // 조직선택
                 sql += "           and b.userid = a.userid\n";
                 sql += "           and a.post1||'-'||a.post2 = vz_zipcode.zipcode(+)\n";
                 sql += "           and vz_zipcode.sido = tz_organization.sido               \n";
                 sql += "           and vz_zipcode.gugun = tz_organization_mat.gugun                        \n";
                 sql += "           and tz_organization.orgcode = tz_organization_mat.orgcode\n";
                 sql += "           and tz_organization.orgcode = "+SQLString.Format(v_orgcode)+"   \n";
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
      과정현황
      @param box      receive from the form object and session
      @return ArrayList
      */
       public ArrayList perform_05_01_01(RequestBox box) throws Exception { 
          DBConnectionManager connMgr     = null;
          ListSet             ls         = null;        
          ArrayList           list       = null;        
          DataBox dbox = null;
          String              sql        = "";        
          
          String v_subj = box.getString("p_subj");
          String v_year = box.getString("p_year");
          String v_subjseq = box.getString("p_subjseq");
          String v_orgcode = box.getString("p_orgcode");  // 조직코드
          
          try { 
              connMgr = new DBConnectionManager();
              list   = new ArrayList();    

              sql += "select                                                          \n";
              sql += "        a.userid,                                                                       \n";
              sql += "        a.name,                                                                         \n";
              sql += "        a.email,                                                                        \n";
              sql += "        a.handphone,                                                                    \n";
              sql += "       a.hometel,                                                                       \n";
              sql += "       a.post1 || '-' || a.post2    post                                                \n";
              sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
              sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
              sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
              sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
              sql += "  from                                                          \n";
              sql += "       tz_member a                                              \n";
              sql += "      ,tz_student b                                             \n";
              if(!v_orgcode.equals("ALL")){   // 조직선택
                  sql += "              ,vz_zipcode\n";
                  sql += "              ,tz_organization\n";
                  sql += "              ,tz_organization_mat\n";
              }
              sql += " where                                                          \n";
              sql += "       a.userid = b.userid                                      \n";
              sql += "   and b.subj = "+SQLString.Format(v_subj)+"                    \n";
              sql += "   and b.year = "+SQLString.Format(v_year)+"                    \n";
              sql += "   and b.subjseq = "+SQLString.Format(v_subjseq)+"              \n";
              if(!v_orgcode.equals("ALL")){   // 조직선택
                  sql += "           and b.userid = a.userid\n";
                  sql += "           and a.post1||'-'||a.post2 = vz_zipcode.zipcode(+)\n";
                  sql += "           and vz_zipcode.sido = tz_organization.sido               \n";
                  sql += "           and vz_zipcode.gugun = tz_organization_mat.gugun                        \n";
                  sql += "           and tz_organization.orgcode = tz_organization_mat.orgcode\n";
                  sql += "           and tz_organization.orgcode = "+SQLString.Format(v_orgcode)+"   \n";
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
       연도별교육실적
       @param box      receive from the form object and session
       @return ArrayList
       */
        public ArrayList perform_06_01_01(RequestBox box) throws Exception { 
           DBConnectionManager connMgr     = null;
           ListSet             ls         = null;        
           ArrayList           list       = null;        
           DataBox dbox = null;
           String              sql        = "";        
           
           String v_subj = box.getString("p_subj");
           String v_year = box.getString("p_year");
           String v_subjseq = box.getString("p_subjseq");
           String v_orgcode = box.getString("p_orgcode");  // 조직코드
           
           try { 
               connMgr = new DBConnectionManager();
               list   = new ArrayList();    

               sql += "select                                                          \n";
               sql += "        a.userid,                                                                       \n";
               sql += "        a.name,                                                                         \n";
               sql += "        a.email,                                                                        \n";
               sql += "        a.handphone,                                                                    \n";
               sql += "       a.hometel,                                                                       \n";
               sql += "       a.post1 || '-' || a.post2    post                                                \n";
               sql += "      , a.address1 || ' ' || a.address2     addr                                        \n";
               sql += "      , NVL(a.issms, 'Y') issms                                                         \n";
               sql += "      , NVL(a.ismailling, 'Y') ismailling                                               \n";
               sql += "      , TO_CHAR(TO_DATE(SUBSTR(a.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
               sql += "  from                                                          \n";
               sql += "       tz_member a                                              \n";
               sql += "      ,tz_student b                                             \n";
               if(!v_orgcode.equals("ALL")){   // 조직선택
                   sql += "              ,vz_zipcode\n";
                   sql += "              ,tz_organization\n";
                   sql += "              ,tz_organization_mat\n";
               }
               sql += " where                                                          \n";
               sql += "       a.userid = b.userid                                      \n";
               sql += "   and b.subj = "+SQLString.Format(v_subj)+"                    \n";
               sql += "   and b.year = "+SQLString.Format(v_year)+"                    \n";
               sql += "   and b.subjseq = "+SQLString.Format(v_subjseq)+"              \n";
               if(!v_orgcode.equals("ALL")){   // 조직선택
                   sql += "           and b.userid = a.userid\n";
                   sql += "           and a.post1||'-'||a.post2 = vz_zipcode.zipcode(+)\n";
                   sql += "           and vz_zipcode.sido = tz_organization.sido               \n";
                   sql += "           and vz_zipcode.gugun = tz_organization_mat.gugun                        \n";
                   sql += "           and tz_organization.orgcode = tz_organization_mat.orgcode\n";
                   sql += "           and tz_organization.orgcode = "+SQLString.Format(v_orgcode)+"   \n";
               }
               
               System.out.println(sql);
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
        분야별 교육 실적
        @param box      receive from the form object and session
        @return ArrayList
        */
         public ArrayList perform_08_01_01(RequestBox box) throws Exception { 
            DBConnectionManager connMgr     = null;
            ListSet             ls         = null;        
            ArrayList           list       = null;        
            DataBox dbox = null;
            String              sql        = "";        
            
            String v_name = box.getString("p_name");
            String v_type = box.getString("p_type");
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");   // 교육그룹
            String  ss_gyear    = box.getStringDefault("s_gyear","ALL");    // 년도
            String v_orgcode = box.getString("p_orgcode");  // 조직코드
            
            try { 
                connMgr = new DBConnectionManager();
                list   = new ArrayList();    

                if(v_type.equals("ALL"))
                {
                    sql += "                select                                        \n";
                    sql += "        tz_member.userid,                                                                       \n";
                    sql += "        tz_member.name,                                                                         \n";
                    sql += "        tz_member.email,                                                                        \n";
                    sql += "        tz_member.handphone,                                                                    \n";
                    sql += "       tz_member.hometel,                                                                       \n";
                    sql += "       tz_member.post1 || '-' || tz_member.post2    post                                                \n";
                    sql += "      , tz_member.address1 || ' ' || tz_member.address2     addr                                        \n";
                    sql += "      , NVL(tz_member.issms, 'Y') issms                                                         \n";
                    sql += "      , NVL(tz_member.ismailling, 'Y') ismailling                                               \n";
                    sql += "      , TO_CHAR(TO_DATE(SUBSTR(tz_member.indate, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD')  indate      \n";
                    sql += "                  from                                        \n";
                    sql += "                       TZ_STUDENT                             \n";
                    sql += "              ,tz_member\n";
                    if(!v_orgcode.equals("ALL")){   // 조직선택
                        sql += "              ,vz_zipcode\n";
                        sql += "              ,tz_organization\n";
                        sql += "              ,tz_organization_mat\n";
                    }
                    sql += "       ,tz_subjatt a                                          \n";
                    sql += "       ,VZ_SCSUBJSEQ b                                        \n";
                    sql += "       ,tz_grseq c                                             \n";
                    sql += "                 where                                        \n";
                    sql += "                       TZ_STUDENT.subj=b.subj                            \n";
                    sql += "                   and TZ_STUDENT.year=b.year                            \n";
                    sql += "                   and TZ_STUDENT.subjseq=b.subjseq                      \n";
                    sql += "           and TZ_STUDENT.userid = tz_member.userid\n";
                    if(!v_orgcode.equals("ALL")){   // 조직선택                        
                        sql += "           and tz_member.post1||'-'||tz_member.post2 = vz_zipcode.zipcode(+)\n";
                        sql += "           and vz_zipcode.sido = tz_organization.sido               \n";
                        sql += "           and vz_zipcode.gugun = tz_organization_mat.gugun                        \n";
                        sql += "           and tz_organization.orgcode = tz_organization_mat.orgcode\n";
                        sql += "           and tz_organization.orgcode = "+SQLString.Format(v_orgcode)+"   \n";
                    }
                    sql += "   and a.middleclass <> '000'                                 \n";
                    sql += "   and a.upperclass = b.scupperclass(+) and a.middleclass = b.scmiddleclass(+) and a.lowerclass = b.sclowerclass(+)\n";
                    sql += "   and b.grcode = c.grcode(+) and b.gyear = c.gyear(+) and b.grseq = c.grseq(+) and b.isclosed(+) = 'Y'\n";
                    sql += "   and a.upperclass = " +SQLString.Format(v_name);
                     
                    if ( !ss_grcode.equals("ALL") ) { 
                        sql += " and b.grcode(+) = " +SQLString.Format(ss_grcode);
                    }
                    
                    if ( !ss_gyear.equals("ALL") ) { 
                        sql += " and b.gyear(+) = " +SQLString.Format(ss_gyear);
                    }
                }
                else
                {
                    sql += "                select                                        \n";
                    sql += "       tz_member.userid                                                 \n";
                    sql += "      ,tz_member.name                                                   \n";
                    sql += "      ,tz_member.email                                                  \n";
                    sql += "      ,tz_member.handphone                                              \n";
                    sql += "                  from                                        \n";
                    sql += "                       TZ_STUDENT                             \n";
                    sql += "              ,tz_member\n";
                    if(!v_orgcode.equals("ALL")){   // 조직선택
                        sql += "              ,vz_zipcode\n";
                        sql += "              ,tz_organization\n";
                        sql += "              ,tz_organization_mat\n";
                    }
                    sql += "       ,tz_subjatt a                                          \n";
                    sql += "       ,VZ_SCSUBJSEQ b                                        \n";
                    sql += "       ,tz_grseq c                                             \n";
                    sql += "                 where                                        \n";
                    sql += "                       TZ_STUDENT.subj=b.subj                            \n";
                    sql += "                   and TZ_STUDENT.year=b.year                            \n";
                    sql += "                   and TZ_STUDENT.subjseq=b.subjseq                      \n";
                    sql += "           and TZ_STUDENT.userid = tz_member.userid\n";
                    if(!v_orgcode.equals("ALL")){   // 조직선택                        
                        sql += "           and tz_member.post1||'-'||tz_member.post2 = vz_zipcode.zipcode(+)\n";
                        sql += "           and vz_zipcode.sido = tz_organization.sido               \n";
                        sql += "           and vz_zipcode.gugun = tz_organization_mat.gugun                        \n";
                        sql += "           and tz_organization.orgcode = tz_organization_mat.orgcode\n";
                        sql += "           and tz_organization.orgcode = "+SQLString.Format(v_orgcode)+"   \n";
                    }
                    sql += "   and a.middleclass <> '000'                                 \n";
                    sql += "   and a.upperclass = b.scupperclass(+) and a.middleclass = b.scmiddleclass(+) and a.lowerclass = b.sclowerclass(+)\n";
                    sql += "   and b.grcode = c.grcode(+) and b.gyear = c.gyear(+) and b.grseq = c.grseq(+) and b.isclosed(+) = 'Y'\n";
                    sql += "   and a.upperclass = " +SQLString.Format(v_name);
                    sql += "   and c.grseq = " +SQLString.Format(v_type);
                     
                    if ( !ss_grcode.equals("ALL") ) { 
                        sql += " and b.grcode(+) = " +SQLString.Format(ss_grcode);
                    }
                    
                    if ( !ss_gyear.equals("ALL") ) { 
                        sql += " and b.gyear(+) = " +SQLString.Format(ss_gyear);
                    }
                }
                

                
                System.out.println(sql);
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
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
    
    
    
    
    
    
    
    
    
}