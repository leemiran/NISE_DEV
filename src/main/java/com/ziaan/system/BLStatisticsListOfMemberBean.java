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

public class BLStatisticsListOfMemberBean {

    // private
    private ConfigSet config;
    private int         row;
    
    public BLStatisticsListOfMemberBean() {
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
                    sql += "               and (a.birth_date is null or substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
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
                    sql += "               and (a.birth_date is null or substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
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
                    sql += "               and (a.birth_date is null or substr(fn_crypt('2', a.birth_date, 'knise'), 7, 1) not in (9, 0, 1, 2, 3, 4)) \n";
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
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
         
        DataBox             dbox            = null;
         
        String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
        String              v_courseyear    = box.getStringDefault  ( "p_courseyear"   , ""    ); // 년도
        String              v_courseseq     = box.getStringDefault  ( "p_courseseq"    , ""    ); // 과목 기수
        String              v_coursecode    = box.getStringDefault  ( "p_coursecode"   , ""    ); // 과목&코스
        String              s_gadmin        = box.getSession        ( "gadmin"                 );
        String              v_branch        = box.getStringDefault  ( "p_branch"       , ""    );
        String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
        String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
        String              v_status        = box.getString         ( "p_status"               );   
         
        if("K2".equals(s_gadmin)) {
             v_branch                       = box.getSession("branch");
        } else {
             v_branch                       = box.getString("p_branch");           // 거점
        }

        try {
            connMgr     = new DBConnectionManager();            
            list        = new ArrayList();
            
            sbSQL.append(" SELECT  *  FROM (                                                                                                                                                                \n")
                 .append(" SELECT  a.coursenm                                                                                                                                                               \n")
                 .append("     ,   b.coursecode                                                                                                                                                             \n")
                 .append("     ,   b.courseyear                                                                                                                                                             \n")
                 .append("     ,   b.courseseq                                                                                                                                                              \n")
                 .append("     ,   d.userid                                                                                                                                                                 \n")
                 .append("     ,   d.name                                                                                                                                                                   \n")
                 .append("     ,   e.branchnm                                                                                                                                                               \n")
                 .append("     ,   c.status                                                                                                                                                                 \n")
                 .append("     ,   b.subjnumforcomplete                                                                                                                                                     \n")
                 .append("     ,   b.wonlinetest                                                                                                                                                            \n")
                 .append("     ,   b.wofflinetest                                                                                                                                                           \n")
                 .append("     ,   b.wportfolio                                                                                                                                                             \n")
                 .append("     ,   b.wvalue                                                                                                                                                                 \n")
                 .append("     ,   c.portfolio                                                                                                                                                              \n")
                 .append("     ,   c.valuation                                                                                                                                                              \n")
                 .append("     ,   c.addpointvalue                                                                                                                                                          \n")
                 .append("     ,   c.addpointvalue1                                                                                                                                                         \n")
                 .append("     ,   c.addpointvalue2                                                                                                                                                         \n")
                 .append("     ,   c.isgraduated                                                                                                                                                            \n")
                 .append("     ,   nvl(round(online_score, 2), 0)                              online_score                                                                                                 \n")
                 .append("     ,   nvl(online_iscomplete_y, 0)                                 online_iscomplete_y                                                                                          \n")
                 .append("     ,   nvl(online_iscomplete_n, 0)                                 online_iscomplete_n                                                                                          \n")
                 .append("     ,   nvl(offline_score, 0)                                       offline_score                                                                                                \n")
                 .append("     ,   nvl(offline_iscomplete_y, 0)                                offline_iscomplete_y                                                                                         \n")
                 .append("     ,   nvl(offline_iscomplete_n, 0)                                offline_iscomplete_n                                                                                         \n")
                 .append("     ,   online_subjnum                                                                                                                                                           \n")
                 .append("     ,   offline_subjnum                                                                                                                                                          \n")
                 .append("     ,   round((nvl(online_score, 0) * b.wonlinetest     / 100)                                                                                                                   \n")
                 .append("             + (nvl(offline_score, 0) * b.wofflinetest   / 100)                                                                                                                   \n")
                 .append("             + (nvl(c.portfolio, 0) * b.wportfolio       / 100)                                                                                                                   \n")
                 .append("             + (nvl(c.valuation, 0) * b.wvalue           / 100)                                                                                                                   \n")
                 .append("             + nvl(addpointvalue, 0)                                                                                                                                              \n")
                 .append("             + nvl(addpointvalue1, 0)                                                                                                                                             \n")
                 .append("             + nvl(addpointvalue2, 0)                                                                                                                                             \n")
                 .append("             , 1)                                                    total                                                                                                        \n")
                 .append("     ,   case when (b.subjnumforcomplete <= (online_iscomplete_y + offline_iscomplete_y))                                                                                         \n")
                 .append("              then 'Y'                                                                                                                                                            \n")
                 .append("              else 'N'                                                                                                                                                            \n")
                 .append("         end                                                         gubun                                                                                                        \n")                                           
                 .append("     ,   case when (b.subjnumforcomplete <= (    online_iscomplete_y                                                                                                              \n")
                 .append("                                             +   offline_iscomplete_y)                                                                                                            \n")
                 .append("                   )                                                                                                                                                              \n")
                 .append("              then rank() over (partition by c.courseyear, c.courseseq, c.coursecode                                                                                              \n")
                 .append("                                order by     round((nvl(online_score, 0) * b.wonlinetest/100)                                                                                     \n")
                 .append("                                                 + (nvl(offline_score, 0) * b.wofflinetest/100)                                                                                   \n")
                 .append("                                                 + (nvl(c.portfolio, 0) * b.wportfolio)                                                                                           \n")
                 .append("                                                 + (nvl(c.valuation, 0) * b.wvalue)                                                                                               \n")
                 .append("                                                 + nvl(addpointvalue, 0)                                                                                                          \n")
                 .append("                                                 + nvl(addpointvalue1, 0)                                                                                                         \n")
                 .append("                                                 + nvl(addpointvalue2, 0)                                                                                                         \n")
                 .append("                                                 , 1) desc                                                                                                                        \n")
                 .append("                               )                                                                                                                                                  \n")
                 .append("              else 0                                                                                                                                                              \n")
                 .append("         end                                                         rank                                                                                                         \n")
                 .append(" from    tz_bl_course    a                                                                                                                                                        \n")
                 .append("     ,   tz_blcourseseq  b                                                                                                                                                        \n")
                 .append("     ,   tz_blstudent    c                                                                                                                                                        \n")
                 .append("     ,   tz_member       d                                                                                                                                                        \n")
                 .append("     ,   tz_branch       e                                                                                                                                                        \n")
                 .append("     ,   (                                                                                                                                                                        \n")
                 .append("             select  courseyear                                                                                                                                                   \n")
                 .append("                 ,   courseseq                                                                                                                                                    \n")
                 .append("                 ,   coursecode                                                                                                                                                   \n")
                 .append("                 ,   avg(nvl(score, 0)) online_score                                                                                                                              \n")
                 .append("                 ,   userid                                                                                                                                                       \n")
                 .append("                 ,   sum(decode(isgraduated, 'Y', 1, 0)) online_iscomplete_y                                                                                                      \n")
                 .append("                 ,   sum(decode(isgraduated, 'N', 1, 0)) online_iscomplete_n                                                                                                      \n")
                 .append("             from    (                                                                                                                                                            \n")
                 .append("                         select  a.courseyear                                                                                                                                     \n")
                 .append("                             ,   a.courseseq                                                                                                                                      \n")
                 .append("                             ,   a.coursecode                                                                                                                                     \n")
                 .append("                             ,   d.subj                                                                                                                                           \n")
                 .append("                             ,   max(score)          score                                                                                                                        \n")
                 .append("                             ,   max(d.isgraduated)  isgraduated                                                                                                                  \n")
                 .append("                             ,   max(c.userid)       userid                                                                                                                       \n")
                 .append("                         from    tz_blstudent        a                                                                                                                            \n")
                 .append("                             ,   tz_blcourseseq      b                                                                                                                            \n")
                 .append("                             ,   tz_blproposesubj    c                                                                                                                            \n")
                 .append("                             ,   tz_student            d                                                                                                                            \n")
                 .append("                         where   a.coursecode    = b.coursecode                                                                                                                   \n")
                 .append("                         and     a.courseyear    = b.courseyear                                                                                                                   \n")
                 .append("                         and     a.courseseq     = b.courseseq                                                                                                                    \n")
                 .append("                         and     a.coursecode    = c.coursecode                                                                                                                   \n")
                 .append("                         and     a.courseyear    = c.courseyear                                                                                                                   \n")
                 .append("                         and     a.courseseq     = c.courseseq                                                                                                                    \n")
                 .append("                         and     a.userid        = c.userid                                                                                                                       \n")
                 .append("                         and     c.subj          = d.subj                                                                                                                         \n")
                 .append("                         and     c.userid        = d.userid                                                                                                                       \n")
                 .append("                         and     c.status        = 'RV'                                                                                                                           \n")
                 .append("                         and     c.isrequired    = 'Y'                                                                                                                            \n")
                 .append("                         and     add_months(to_date(d.eduend, 'YYYYMMDD'), b.availableperiod * 12) >= to_date(b.onlinepropstart)                                                  \n")
                 .append("                         group by a.courseyear, a.courseseq, a.coursecode, d.subj                                                                                                 \n")
                 .append("          union                                                                                                                                                                   \n")
                 .append("          select     a.courseyear                                                                                                                                                 \n")
                 .append("                 ,   a.courseseq                                                                                                                                                  \n")
                 .append("                 ,   a.coursecode                                                                                                                                                 \n")
                 .append("                 ,   d.subj                                                                                                                                                       \n")
                 .append("                 ,   d.score                                                                                                                                                      \n")
                 .append("                 ,   d.isgraduated                                                                                                                                                \n")
                 .append("                 ,   a.userid                                                                                                                                                     \n")
                 .append("          from       tz_blstudent        a                                                                                                                                        \n")
                 .append("                 ,   tz_blcourseseq      b                                                                                                                                        \n")
                 .append("                 ,   tz_blproposesubj    c                                                                                                                                        \n")
                 .append("                 ,   tz_student            d                                                                                                                                        \n")
                 .append("          where      a.coursecode    = b.coursecode                                                                                                                               \n")
                 .append("          and        a.courseyear    = b.courseyear                                                                                                                               \n")
                 .append("          and        a.courseseq     = b.courseseq                                                                                                                                \n")
                 .append("          and        a.coursecode    = c.coursecode                                                                                                                               \n")
                 .append("          and        a.courseyear    = c.courseyear                                                                                                                               \n")
                 .append("          and        a.courseseq     = c.courseseq                                                                                                                                \n")
                 .append("          and        a.userid        = c.userid                                                                                                                                   \n")
                 .append("          and        c.subj          = d.subj                                                                                                                                     \n")
                 .append("          and        c.userid        = d.userid                                                                                                                                   \n")
                 .append("          and        c.year          = d.year                                                                                                                                     \n")
                 .append("          and        c.subjseq       = d.subjseq                                                                                                                                  \n")
                 .append("          and        (c.status = 'RE' or c.status = 'FE')                                                                                                                         \n")
                 .append("          and        c.isrequired    = 'Y'                                                                                                                                        \n")
                 .append("         )                                                                                                                                                                        \n")
                 .append("         group by    courseyear, courseseq, coursecode, userid                                                                                                                    \n")
                 .append("     )           f                                                                                                                                                                \n")
                 .append(" ,   (                                                                                                                                                                            \n")
                 .append("         select  courseyear                                                                                                                                                       \n")
                 .append("             ,   courseseq                                                                                                                                                        \n")
                 .append("             ,   coursecode                                                                                                                                                       \n")
                 .append("             ,   avg(nvl(score, 0))      offline_score                                                                                                                            \n")
                 .append("             ,   userid                                                                                                                                                           \n")
                 .append("             ,   sum(decode(isgraduated, 'Y', 1, 0)) offline_iscomplete_y                                                                                                         \n")
                 .append("             ,   sum(decode(isgraduated, 'N', 1, 0)) offline_iscomplete_n                                                                                                         \n")
                 .append("         from    (                                                                                                                                                                \n")
                 .append("                     select  a.courseyear, a.courseseq, a.coursecode                                                                                                              \n")
                 .append("                         ,   d.subj, d.score, d.isgraduated, a.userid                                                                                                             \n")
                 .append("                     from    tz_blstudent        a                                                                                                                                \n")
                 .append("                         ,   tz_blcourseseq      b                                                                                                                                \n")
                 .append("                         ,   tz_blcourseseqsubj  c                                                                                                                                \n")
                 .append("                         ,   tz_student            d                                                                                                                                \n")
                 .append("                     where   a.coursecode    = b.coursecode                                                                                                                       \n")
                 .append("                     and     a.courseyear    = b.courseyear                                                                                                                       \n")
                 .append("                     and     a.courseseq     = b.courseseq                                                                                                                        \n")
                 .append("                     and     a.coursecode    = c.coursecode                                                                                                                       \n")
                 .append("                     and     a.courseyear    = c.courseyear                                                                                                                       \n")
                 .append("                     and     a.courseseq     = c.courseseq                                                                                                                        \n")
                 .append("                     and     c.subj          = d.subj                                                                                                                             \n")
                 .append("                     and     a.userid        = d.userid                                                                                                                           \n")
                 .append("                     and     c.year          = d.year                                                                                                                             \n")
                 .append("                     and     c.subjseq       = d.subjseq                                                                                                                          \n")
                 .append("                     and     isonoff         = 'OFF'                                                                                                                              \n")
                 .append("                 )                                                                                                                                                                \n")
                 .append("         group by courseyear, courseseq, coursecode, userid                                                                                                                       \n")
                 .append("     )           g                                                                                                                                                                \n")
                 .append(" ,   (                                                                                                                                                                            \n")
                 .append("         select  coursecode                                                                                                                                                       \n")
                 .append("             ,   courseyear                                                                                                                                                       \n")
                 .append("             ,   courseseq                                                                                                                                                        \n")
                 .append("             ,   sum(decode(isonoff, 'ON', 1, 0))    online_subjnum                                                                                                               \n")
                 .append("             ,   sum(decode(isonoff, 'OFF', 1, 0))   offline_subjnum                                                                                                              \n")
                 .append("         from    tz_blcourseseqsubj                                                                                                                                               \n")
                 .append("         where   ( isrequired = 'Y' or isrequired is null )                                                                                                                       \n")
                 .append("         group by coursecode, courseyear, courseseq                                                                                                                               \n")
                 .append("     )                h                                                                                                                                                           \n");
            
            if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                sbSQL.append("  ,  vz_orgmember     i                                                                                                                                                       \n");
            }
            
            sbSQL.append(" where   a.coursecode    = b.coursecode                                                                                                                                           \n")
                 .append(" and     b.coursecode    = c.coursecode                                                                                                                                           \n")
                 .append(" and     b.courseyear    = c.courseyear                                                                                                                                           \n")
                 .append(" and     b.courseseq     = c.courseseq                                                                                                                                            \n")
                 .append(" and     c.userid        = d.userid                                                                                                                                               \n")
                 .append(" and     c.branchcode    = e.branchcode                                                                                                                                           \n")
                 .append(" and     c.userid        = f.userid      (+)                                                                                                                                      \n")
                 .append(" and     c.courseyear    = f.courseyear  (+)                                                                                                                                      \n")
                 .append(" and     c.coursecode    = f.coursecode  (+)                                                                                                                                      \n")
                 .append(" and     c.courseseq     = f.courseseq   (+)                                                                                                                                      \n")
                 .append(" and     c.userid        = g.userid      (+)                                                                                                                                      \n")
                 .append(" and     c.courseyear    = g.courseyear  (+)                                                                                                                                      \n")
                 .append(" and     c.coursecode    = g.coursecode  (+)                                                                                                                                      \n")
                 .append(" and     c.courseseq     = g.courseseq   (+)                                                                                                                                      \n")
                 .append(" and     b.coursecode    = h.coursecode                                                                                                                                           \n")
                 .append(" and     b.courseyear    = h.courseyear                                                                                                                                           \n")
                 .append(" and     b.courseseq     = h.courseseq                                                                                                                                            \n")
                 .append(" and     b.grcode        = " + StringManager.makeSQL(v_grcode         ) + "                                                                                                       \n")
                .append(" and     b.courseyear    = " + StringManager.makeSQL(v_courseyear     ) + "                                                                                                        \n");

             if ( !v_coursecode.equals("") ) { 
                 sbSQL.append(" and b.coursecode    = " + StringManager.makeSQL(v_coursecode    ) + "   \n");
             }
             
             if ( !v_courseseq.equals("") ) { 
                 sbSQL.append(" and b.courseseq     = " + StringManager.makeSQL(v_courseseq     ) + "   \n");
             }
             
             if( !v_branch.equals("") ) {
                 sbSQL.append(" and c.branchcode    = " + StringManager.makeSQL(v_branch        ) + "   \n");
             }
             
             if ( v_isgraduated.equals("Y") ) {
                 sbSQL.append(" and c.isgraduated   = " + StringManager.makeSQL(v_isgraduated   ) + "   \n");
             } else if ( v_isgraduated.equals("N") ) {
                 sbSQL.append(" and ( c.isgraduated   IS NULL OR c.isgraduated = " + StringManager.makeSQL(v_isgraduated   ) + " ) \n");
             }
             
             if ( v_status.equals("A") ) { 
                 sbSQL.append("                          AND     c.STATUS        < 'G'                                                              \n");
            } else if ( v_status.equals("G") ) {
                sbSQL.append("                          AND     c.STATUS         >= 'G'                                                             \n");
            }    
             
             if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                 sbSQL.append(" AND     i.orgcode       = " + v_orgcode + "                             \n")
                      .append(" and     c.userid        = i.userid                                      \n");
             }
             
             sbSQL.append(" ) ORDER BY courseyear, coursecode, courseseq, rank, name                    \n");
             
             
             System.out.println("sql : " + sbSQL.toString());
                 
             ls          = connMgr.executeQuery(sbSQL.toString());
             
             while (ls.next()) {
                 dbox   = ls.getDataBox();
                 
                 dbox.put("d_wonlinetest"       , new Double(ls.getDouble("wonlinetest"))   );
                 dbox.put("d_wofflinetest"      , new Double(ls.getDouble("wofflinetest"))  );
                 dbox.put("d_wportfolio"        , new Double(ls.getDouble("wportfolio"))    );
                 dbox.put("d_wvalue"            , new Double(ls.getDouble("wvalue"))        );
                 dbox.put("d_portfolio"         , new Double(ls.getDouble("portfolio"))     );
                 dbox.put("d_valuation"         , new Double(ls.getDouble("valuation"))     );
                 dbox.put("d_addpointvalue"     , new Double(ls.getDouble("addpointvalue")) );
                 dbox.put("d_addpointvalue1"    , new Double(ls.getDouble("addpointvalue1")));
                 dbox.put("d_addpointvalue2"    , new Double(ls.getDouble("addpointvalue2")));
                 dbox.put("d_online_score"      , new Double(ls.getDouble("online_score"))  );
                 dbox.put("d_offline_score"     , new Double(ls.getDouble("offline_score")) );
                 dbox.put("d_total"             , new Double(ls.getDouble("total"))         );
                 
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
     과정별 수료현황
     @param box      receive from the form object and session
     @return ArrayList
     */
      public ArrayList perform_03_01_02(RequestBox box) throws Exception { 
          DBConnectionManager connMgr         = null;
          ListSet             ls              = null;
          ArrayList           list            = null;
          StringBuffer        sbSQL           = new StringBuffer("");
           
          DataBox             dbox            = null;
           
          String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
          String              v_courseyear    = box.getStringDefault  ( "p_courseyear"   , ""    ); // 년도
          String              v_courseseq     = box.getStringDefault  ( "p_courseseq"    , ""    ); // 과목 기수
          String              v_coursecode    = box.getStringDefault  ( "p_coursecode"   , ""    ); // 과목&코스
          String              s_gadmin        = box.getSession        ( "gadmin"                 );
          String              v_branch        = box.getStringDefault  ( "p_branch"       , ""    );
          String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
          String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
          String              v_status        = box.getString         ( "p_status"               );   
           
          if("K2".equals(s_gadmin)) {
               v_branch                       = box.getSession("branch");
          } else {
               v_branch                       = box.getString("p_branch");           // 거점
          }

          try {
              connMgr     = new DBConnectionManager();            
              list        = new ArrayList();
              
              sbSQL.append(" SELECT  *  FROM (                                                                                                                                                                \n")
                   .append(" SELECT  a.coursenm                                                                                                                                                               \n")
                   .append("     ,   b.coursecode                                                                                                                                                             \n")
                   .append("     ,   b.courseyear                                                                                                                                                             \n")
                   .append("     ,   b.courseseq                                                                                                                                                              \n")
                   .append("     ,   d.userid                                                                                                                                                                 \n")
                   .append("     ,   d.name                                                                                                                                                                   \n")
                   .append("     ,   e.branchnm                                                                                                                                                               \n")
                   .append("     ,   c.status                                                                                                                                                                 \n")
                   .append("     ,   b.subjnumforcomplete                                                                                                                                                     \n")
                   .append("     ,   b.wonlinetest                                                                                                                                                            \n")
                   .append("     ,   b.wofflinetest                                                                                                                                                           \n")
                   .append("     ,   b.wportfolio                                                                                                                                                             \n")
                   .append("     ,   b.wvalue                                                                                                                                                                 \n")
                   .append("     ,   c.portfolio                                                                                                                                                              \n")
                   .append("     ,   c.valuation                                                                                                                                                              \n")
                   .append("     ,   c.addpointvalue                                                                                                                                                          \n")
                   .append("     ,   c.addpointvalue1                                                                                                                                                         \n")
                   .append("     ,   c.addpointvalue2                                                                                                                                                         \n")
                   .append("     ,   c.isgraduated                                                                                                                                                            \n")
                   .append("     ,   nvl(round(online_score, 2), 0)                              online_score                                                                                                 \n")
                   .append("     ,   nvl(online_iscomplete_y, 0)                                 online_iscomplete_y                                                                                          \n")
                   .append("     ,   nvl(online_iscomplete_n, 0)                                 online_iscomplete_n                                                                                          \n")
                   .append("     ,   nvl(offline_score, 0)                                       offline_score                                                                                                \n")
                   .append("     ,   nvl(offline_iscomplete_y, 0)                                offline_iscomplete_y                                                                                         \n")
                   .append("     ,   nvl(offline_iscomplete_n, 0)                                offline_iscomplete_n                                                                                         \n")
                   .append("     ,   online_subjnum                                                                                                                                                           \n")
                   .append("     ,   offline_subjnum                                                                                                                                                          \n")
                   .append("     ,   round((nvl(online_score, 0) * b.wonlinetest     / 100)                                                                                                                   \n")
                   .append("             + (nvl(offline_score, 0) * b.wofflinetest   / 100)                                                                                                                   \n")
                   .append("             + (nvl(c.portfolio, 0) * b.wportfolio       / 100)                                                                                                                   \n")
                   .append("             + (nvl(c.valuation, 0) * b.wvalue           / 100)                                                                                                                   \n")
                   .append("             + nvl(addpointvalue, 0)                                                                                                                                              \n")
                   .append("             + nvl(addpointvalue1, 0)                                                                                                                                             \n")
                   .append("             + nvl(addpointvalue2, 0)                                                                                                                                             \n")
                   .append("             , 1)                                                    total                                                                                                        \n")
                   .append("     ,   case when (b.subjnumforcomplete <= (online_iscomplete_y + offline_iscomplete_y))                                                                                         \n")
                   .append("              then 'Y'                                                                                                                                                            \n")
                   .append("              else 'N'                                                                                                                                                            \n")
                   .append("         end                                                         gubun                                                                                                        \n")                                           
                   .append("     ,   case when (b.subjnumforcomplete <= (    online_iscomplete_y                                                                                                              \n")
                   .append("                                             +   offline_iscomplete_y)                                                                                                            \n")
                   .append("                   )                                                                                                                                                              \n")
                   .append("              then rank() over (partition by c.courseyear, c.courseseq, c.coursecode                                                                                              \n")
                   .append("                                order by     round((nvl(online_score, 0) * b.wonlinetest/100)                                                                                     \n")
                   .append("                                                 + (nvl(offline_score, 0) * b.wofflinetest/100)                                                                                   \n")
                   .append("                                                 + (nvl(c.portfolio, 0) * b.wportfolio)                                                                                           \n")
                   .append("                                                 + (nvl(c.valuation, 0) * b.wvalue)                                                                                               \n")
                   .append("                                                 + nvl(addpointvalue, 0)                                                                                                          \n")
                   .append("                                                 + nvl(addpointvalue1, 0)                                                                                                         \n")
                   .append("                                                 + nvl(addpointvalue2, 0)                                                                                                         \n")
                   .append("                                                 , 1)  desc                                                                                                                       \n")
                   .append("                               )                                                                                                                                                  \n")
                   .append("              else 0                                                                                                                                                              \n")
                   .append("         end                                                         rank                                                                                                         \n")
                   .append(" from    tz_bl_course    a                                                                                                                                                        \n")
                   .append("     ,   tz_blcourseseq  b                                                                                                                                                        \n")
                   .append("     ,   tz_blstudent    c                                                                                                                                                        \n")
                   .append("     ,   tz_member       d                                                                                                                                                        \n")
                   .append("     ,   tz_branch       e                                                                                                                                                        \n")
                   .append("     ,   (                                                                                                                                                                        \n")
                   .append("             select  courseyear                                                                                                                                                   \n")
                   .append("                 ,   courseseq                                                                                                                                                    \n")
                   .append("                 ,   coursecode                                                                                                                                                   \n")
                   .append("                 ,   avg(nvl(score, 0)) online_score                                                                                                                              \n")
                   .append("                 ,   userid                                                                                                                                                       \n")
                   .append("                 ,   sum(decode(isgraduated, 'Y', 1, 0)) online_iscomplete_y                                                                                                      \n")
                   .append("                 ,   sum(decode(isgraduated, 'N', 1, 0)) online_iscomplete_n                                                                                                      \n")
                   .append("             from    (                                                                                                                                                            \n")
                   .append("                         select  a.courseyear                                                                                                                                     \n")
                   .append("                             ,   a.courseseq                                                                                                                                      \n")
                   .append("                             ,   a.coursecode                                                                                                                                     \n")
                   .append("                             ,   d.subj                                                                                                                                           \n")
                   .append("                             ,   max(score)          score                                                                                                                        \n")
                   .append("                             ,   max(d.isgraduated)  isgraduated                                                                                                                  \n")
                   .append("                             ,   max(c.userid)       userid                                                                                                                       \n")
                   .append("                         from    tz_blstudent        a                                                                                                                            \n")
                   .append("                             ,   tz_blcourseseq      b                                                                                                                            \n")
                   .append("                             ,   tz_blproposesubj    c                                                                                                                            \n")
                   .append("                             ,   tz_student            d                                                                                                                            \n")
                   .append("                         where   a.coursecode    = b.coursecode                                                                                                                   \n")
                   .append("                         and     a.courseyear    = b.courseyear                                                                                                                   \n")
                   .append("                         and     a.courseseq     = b.courseseq                                                                                                                    \n")
                   .append("                         and     a.coursecode    = c.coursecode                                                                                                                   \n")
                   .append("                         and     a.courseyear    = c.courseyear                                                                                                                   \n")
                   .append("                         and     a.courseseq     = c.courseseq                                                                                                                    \n")
                   .append("                         and     a.userid        = c.userid                                                                                                                       \n")
                   .append("                         and     c.subj          = d.subj                                                                                                                         \n")
                   .append("                         and     c.userid        = d.userid                                                                                                                       \n")
                   .append("                         and     c.status        = 'RV'                                                                                                                           \n")
                   .append("                         and     c.isrequired    = 'Y'                                                                                                                            \n")
                   .append("                         and     add_months(to_date(d.eduend, 'YYYYMMDD'), b.availableperiod * 12) >= to_date(b.onlinepropstart)                                                  \n")
                   .append("                         group by a.courseyear, a.courseseq, a.coursecode, d.subj                                                                                                 \n")
                   .append("          union                                                                                                                                                                   \n")
                   .append("          select     a.courseyear                                                                                                                                                 \n")
                   .append("                 ,   a.courseseq                                                                                                                                                  \n")
                   .append("                 ,   a.coursecode                                                                                                                                                 \n")
                   .append("                 ,   d.subj                                                                                                                                                       \n")
                   .append("                 ,   d.score                                                                                                                                                      \n")
                   .append("                 ,   d.isgraduated                                                                                                                                                \n")
                   .append("                 ,   a.userid                                                                                                                                                     \n")
                   .append("          from       tz_blstudent        a                                                                                                                                        \n")
                   .append("                 ,   tz_blcourseseq      b                                                                                                                                        \n")
                   .append("                 ,   tz_blproposesubj    c                                                                                                                                        \n")
                   .append("                 ,   tz_student            d                                                                                                                                        \n")
                   .append("          where      a.coursecode    = b.coursecode                                                                                                                               \n")
                   .append("          and        a.courseyear    = b.courseyear                                                                                                                               \n")
                   .append("          and        a.courseseq     = b.courseseq                                                                                                                                \n")
                   .append("          and        a.coursecode    = c.coursecode                                                                                                                               \n")
                   .append("          and        a.courseyear    = c.courseyear                                                                                                                               \n")
                   .append("          and        a.courseseq     = c.courseseq                                                                                                                                \n")
                   .append("          and        a.userid        = c.userid                                                                                                                                   \n")
                   .append("          and        c.subj          = d.subj                                                                                                                                     \n")
                   .append("          and        c.userid        = d.userid                                                                                                                                   \n")
                   .append("          and        c.year          = d.year                                                                                                                                     \n")
                   .append("          and        c.subjseq       = d.subjseq                                                                                                                                  \n")
                   .append("          and        (c.status = 'RE' or c.status = 'FE')                                                                                                                         \n")
                   .append("          and        c.isrequired    = 'Y'                                                                                                                                        \n")
                   .append("         )                                                                                                                                                                        \n")
                   .append("         group by    courseyear, courseseq, coursecode, userid                                                                                                                    \n")
                   .append("     )           f                                                                                                                                                                \n")
                   .append(" ,   (                                                                                                                                                                            \n")
                   .append("         select  courseyear                                                                                                                                                       \n")
                   .append("             ,   courseseq                                                                                                                                                        \n")
                   .append("             ,   coursecode                                                                                                                                                       \n")
                   .append("             ,   avg(nvl(score, 0))      offline_score                                                                                                                            \n")
                   .append("             ,   userid                                                                                                                                                           \n")
                   .append("             ,   sum(decode(isgraduated, 'Y', 1, 0)) offline_iscomplete_y                                                                                                         \n")
                   .append("             ,   sum(decode(isgraduated, 'N', 1, 0)) offline_iscomplete_n                                                                                                         \n")
                   .append("         from    (                                                                                                                                                                \n")
                   .append("                     select  a.courseyear, a.courseseq, a.coursecode                                                                                                              \n")
                   .append("                         ,   d.subj, d.score, d.isgraduated, a.userid                                                                                                             \n")
                   .append("                     from    tz_blstudent        a                                                                                                                                \n")
                   .append("                         ,   tz_blcourseseq      b                                                                                                                                \n")
                   .append("                         ,   tz_blcourseseqsubj  c                                                                                                                                \n")
                   .append("                         ,   tz_student            d                                                                                                                                \n")
                   .append("                     where   a.coursecode    = b.coursecode                                                                                                                       \n")
                   .append("                     and     a.courseyear    = b.courseyear                                                                                                                       \n")
                   .append("                     and     a.courseseq     = b.courseseq                                                                                                                        \n")
                   .append("                     and     a.coursecode    = c.coursecode                                                                                                                       \n")
                   .append("                     and     a.courseyear    = c.courseyear                                                                                                                       \n")
                   .append("                     and     a.courseseq     = c.courseseq                                                                                                                        \n")
                   .append("                     and     c.subj          = d.subj                                                                                                                             \n")
                   .append("                     and     a.userid        = d.userid                                                                                                                           \n")
                   .append("                     and     c.year          = d.year                                                                                                                             \n")
                   .append("                     and     c.subjseq       = d.subjseq                                                                                                                          \n")
                   .append("                     and     isonoff         = 'OFF'                                                                                                                              \n")
                   .append("                 )                                                                                                                                                                \n")
                   .append("         group by courseyear, courseseq, coursecode, userid                                                                                                                       \n")
                   .append("     )           g                                                                                                                                                                \n")
                   .append(" ,   (                                                                                                                                                                            \n")
                   .append("         select  coursecode                                                                                                                                                       \n")
                   .append("             ,   courseyear                                                                                                                                                       \n")
                   .append("             ,   courseseq                                                                                                                                                        \n")
                   .append("             ,   sum(decode(isonoff, 'ON', 1, 0))    online_subjnum                                                                                                               \n")
                   .append("             ,   sum(decode(isonoff, 'OFF', 1, 0))   offline_subjnum                                                                                                              \n")
                   .append("         from    tz_blcourseseqsubj                                                                                                                                               \n")
                   .append("         where   ( isrequired = 'Y' or isrequired is null )                                                                                                                       \n")
                   .append("         group by coursecode, courseyear, courseseq                                                                                                                               \n")
                   .append("     )                h                                                                                                                                                           \n");
              
              if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                  sbSQL.append("  ,  vz_orgmember     i                                                                                                                                                       \n");
              }
              
              sbSQL.append(" where   a.coursecode    = b.coursecode                                                                                                                                           \n")
                   .append(" and     b.coursecode    = c.coursecode                                                                                                                                           \n")
                   .append(" and     b.courseyear    = c.courseyear                                                                                                                                           \n")
                   .append(" and     b.courseseq     = c.courseseq                                                                                                                                            \n")
                   .append(" and     c.userid        = d.userid                                                                                                                                               \n")
                   .append(" and     c.branchcode    = e.branchcode                                                                                                                                           \n")
                   .append(" and     c.userid        = f.userid      (+)                                                                                                                                      \n")
                   .append(" and     c.courseyear    = f.courseyear  (+)                                                                                                                                      \n")
                   .append(" and     c.coursecode    = f.coursecode  (+)                                                                                                                                      \n")
                   .append(" and     c.courseseq     = f.courseseq   (+)                                                                                                                                      \n")
                   .append(" and     c.userid        = g.userid      (+)                                                                                                                                      \n")
                   .append(" and     c.courseyear    = g.courseyear  (+)                                                                                                                                      \n")
                   .append(" and     c.coursecode    = g.coursecode  (+)                                                                                                                                      \n")
                   .append(" and     c.courseseq     = g.courseseq   (+)                                                                                                                                      \n")
                   .append(" and     b.coursecode    = h.coursecode                                                                                                                                           \n")
                   .append(" and     b.courseyear    = h.courseyear                                                                                                                                           \n")
                   .append(" and     b.courseseq     = h.courseseq                                                                                                                                            \n")
                   .append(" and     b.grcode        = " + StringManager.makeSQL(v_grcode         ) + "                                                                                                       \n")
                  .append(" and     b.courseyear    = " + StringManager.makeSQL(v_courseyear     ) + "                                                                                                        \n");

               if ( !v_coursecode.equals("") ) { 
                   sbSQL.append(" and b.coursecode    = " + StringManager.makeSQL(v_coursecode    ) + "   \n");
               }
               
               if ( !v_courseseq.equals("") ) { 
                   sbSQL.append(" and b.courseseq     = " + StringManager.makeSQL(v_courseseq     ) + "   \n");
               }
               
               if( !v_branch.equals("") ) {
                   sbSQL.append(" and c.branchcode    = " + StringManager.makeSQL(v_branch        ) + "   \n");
               }
               
               if ( v_isgraduated.equals("Y") ) {
                   sbSQL.append(" and c.isgraduated   = " + StringManager.makeSQL(v_isgraduated   ) + "   \n");
               } else if ( v_isgraduated.equals("N") ) {
                   sbSQL.append(" and ( c.isgraduated   IS NULL OR c.isgraduated = " + StringManager.makeSQL(v_isgraduated   ) + " ) \n");
               }
               
               if ( v_status.equals("A") ) { 
                   sbSQL.append("                          AND     c.STATUS        < 'G'                                                              \n");
               } else if ( v_status.equals("G") ) {
                  sbSQL.append("                          AND     c.STATUS         >= 'G'                                                             \n");
               }    
               
               if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                   sbSQL.append(" AND     i.orgcode       = " + v_orgcode + "                             \n")
                        .append(" and     c.userid        = i.userid                                      \n");
               }
               
               sbSQL.append(" ) ORDER BY courseyear, coursecode, courseseq, rank, name                    \n");
               
               
               System.out.println("sql : " + sbSQL.toString());
                   
               ls          = connMgr.executeQuery(sbSQL.toString());
               
               while (ls.next()) {
                   dbox   = ls.getDataBox();
                   
                   dbox.put("d_wonlinetest"       , new Double(ls.getDouble("wonlinetest"))   );
                   dbox.put("d_wofflinetest"      , new Double(ls.getDouble("wofflinetest"))  );
                   dbox.put("d_wportfolio"        , new Double(ls.getDouble("wportfolio"))    );
                   dbox.put("d_wvalue"            , new Double(ls.getDouble("wvalue"))        );
                   dbox.put("d_portfolio"         , new Double(ls.getDouble("portfolio"))     );
                   dbox.put("d_valuation"         , new Double(ls.getDouble("valuation"))     );
                   dbox.put("d_addpointvalue"     , new Double(ls.getDouble("addpointvalue")) );
                   dbox.put("d_addpointvalue1"    , new Double(ls.getDouble("addpointvalue1")));
                   dbox.put("d_addpointvalue2"    , new Double(ls.getDouble("addpointvalue2")));
                   dbox.put("d_online_score"      , new Double(ls.getDouble("online_score"))  );
                   dbox.put("d_offline_score"     , new Double(ls.getDouble("offline_score")) );
                   dbox.put("d_total"             , new Double(ls.getDouble("total"))         );
                   
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
      과정현황
      @param box      receive from the form object and session
      @return ArrayList
      */
       public ArrayList perform_03_01_03(RequestBox box) throws Exception { 
           DBConnectionManager connMgr         = null;
           ListSet             ls              = null;
           ArrayList           list            = null;
           StringBuffer        sbSQL           = new StringBuffer("");
           
           DataBox             dbox            = null;
           
           String              v_Bcourse       = ""; // 이전코스
           String              v_course        = ""; // 현재코스
           String              v_Bcourseseq    = ""; // 이전코스기수
           String              v_courseseq     = ""; // 현재코스기수
           
           String              ss_grcode       = box.getStringDefault  ( "s_grcode"        , "ALL" );  // 교육그룹
           String              ss_blcourseyear = box.getStringDefault  ( "s_blcourseyear"  , "ALL" );  // 년도
           String              ss_blcourseseq  = box.getStringDefault  ( "s_blcourseseq"   , "ALL" );  // 과목 기수
           String              ss_blcourse     = box.getStringDefault  ( "s_blcourse"      , "ALL" );  // 과목&코스
           String              ss_branch       = box.getStringDefault  ( "p_branch"        , "ALL" );
           String              ss_action       = box.getString         ( "s_action"                );
           String              v_orgcode       = box.getStringDefault  ( "p_orgcode"       , "ALL" );  // 조직코드
           String              c_sido          = box.getString         ( "c_sido"                  );
           String              c_gugun         = box.getString         ( "c_gugun"                 );
           String              c_gender        = box.getString         ( "c_gender"                );
           String              c_achievement   = box.getString         ( "c_achievement"           );
           String              c_age           = box.getString         ( "c_age"                   );
           String              c_gubun           = box.getString       ( "c_gubun"                 );
           
           String              v_iscomplete    = box.getString         ( "p_iscomplete"            );
           
           String              v_orderColumn   = box.getString( "p_orderColumn"  );  // 정렬할 컬럼명
           String              v_orderType     = box.getString( "p_orderType"    );  // 정렬할 순서
                
           try {
               connMgr     = new DBConnectionManager();            
               list        = new ArrayList();
            
            sbSQL.append("  select  a.coursenm                                                                                                                                                                              \n")                                                                                                                                                                                                  
                    .append("         ,   b.coursecode                                                                                                                                                                         \n")
                    .append("         ,   b.courseyear                                                                                                                                                                         \n")
                    .append("         ,   b.courseseq                                                                                                                                                                          \n")
                    .append("         ,   d.userid                                                                                                                                                                             \n")
                    .append("         ,   d.name                                                                                                                                                                               \n")
                    .append("         ,   e.branchnm                                                                                                                                                                           \n")
                    .append("         ,   c.status                                                                                                                                                                             \n")
                    .append("         ,   b.subjnumforcomplete                                                                                                                                                                 \n")
                    .append("         ,   b.wonlinetest                                                                                                                                                                        \n")
                    .append("         ,   b.wofflinetest                                                                                                                                                                       \n")
                    .append("         ,   b.wportfolio                                                                                                                                                                         \n")
                    .append("         ,   b.wvalue                                                                                                                                                                             \n")
                    .append("         ,   c.portfolio                                                                                                                                                                          \n")
                    .append("         ,   c.valuation                                                                                                                                                                          \n")
                    .append("         ,   c.addpointvalue                                                                                                                                                                      \n")
                    .append("         ,   c.addpointvalue1                                                                                                                                                                     \n")
                    .append("         ,   c.addpointvalue2                                                                                                                                                                     \n")
                    .append("         ,   c.isgraduated                                                                                                                                                                        \n")
                    .append("         ,   nvl(online_score, 0)            online_score                                                                                                                                         \n")
                    .append("         ,   nvl(online_iscomplete_y, 0)     online_iscomplete_y                                                                                                                                  \n")
                    .append("         ,   nvl(online_iscomplete_n, 0)     online_iscomplete_n                                                                                                                                  \n")
                    .append("         ,   nvl(offline_score, 0)           offline_score                                                                                                                                        \n")
                    .append("         ,   nvl(offline_iscomplete_y, 0)    offline_iscomplete_y                                                                                                                                 \n")
                    .append("         ,   nvl(offline_iscomplete_n, 0)    offline_iscomplete_n                                                                                                                                 \n")
                    .append("         ,   online_subjnum                                                                                                                                                                       \n")
                    .append("         ,   offline_subjnum                                                                                                                                                                      \n")
                    .append("         ,       round((nvl(online_score, 0) * b.wonlinetest/100)                                                                                                                                 \n")
                    .append("             +   (nvl(offline_score, 0) * b.wofflinetest/100)                                                                                                                                     \n")
                    .append("             +   (nvl(c.portfolio, 0) * b.wportfolio/100)                                                                                                                                         \n")
                    .append("             +   (nvl(c.valuation, 0) * b.wvalue/100)                                                                                                                                             \n")
                    .append("             +   nvl(addpointvalue, 0)                                                                                                                                                            \n")
                    .append("             +   nvl(addpointvalue1, 0)                                                                                                                                                           \n")
                    .append("             +   nvl(addpointvalue2, 0), 1)                          total                                                                                                                        \n")
                    .append("         ,   case when (b.subjnumforcomplete <= (online_iscomplete_y + offline_iscomplete_y)) then 'Y' else 'N' end gubun                                                                         \n")                                                                          
                    .append("         ,   case when (b.subjnumforcomplete <= (online_iscomplete_y + offline_iscomplete_y)) then rank() over (order by round((nvl(online_score, 0) * b.wonlinetest/100)                         \n")
                    .append("                                                                                                                         + (nvl(offline_score, 0) * b.wofflinetest/100)                           \n")
                    .append("                                                                                                                         + (nvl(c.portfolio, 0) * b.wportfolio)                                   \n")
                    .append("                                                                                                                         + (nvl(c.valuation, 0) * b.wvalue)                                       \n")
                    .append("                                                                                                                         + nvl(addpointvalue, 0)                                                  \n")
                    .append("                                                                                                                         + nvl(addpointvalue1, 0)                                                 \n")
                    .append("                                                                                                                         + nvl(addpointvalue2, 0), 1) desc ) else 0 end rank                      \n")
                    .append("     from    tz_bl_course    a                                                                                                                                                                    \n")
                    .append("         ,   tz_blcourseseq  b                                                                                                                                                                    \n")
                    .append("         ,   tz_blstudent    c                                                                                                                                                                    \n")
                    .append("         ,   tz_member       d                                                                                                                                                                    \n")
                    .append("         ,   tz_branch       e                                                                                                                                                                    \n")
                    .append("         ,   (                                                                                                                                                                                    \n")
                    .append("                 select  avg(nvl(score, 0)) online_score                                                                                                                                          \n")
                    .append("                     ,   userid                                                                                                                                                                   \n")
                    .append("                     ,   sum(decode(isgraduated, 'Y', 1, 0)) online_iscomplete_y                                                                                                                  \n")
                    .append("                     ,   sum(decode(isgraduated, 'N', 1, 0)) online_iscomplete_n                                                                                                                  \n")
                    .append("                 from                                                                                                                                                                             \n")
                    .append("                 (                                                                                                                                                                                \n")
                    .append("                     select  d.subj                                                                                                                                                               \n")
                    .append("                         ,   max(score) score                                                                                                                                                     \n")
                    .append("                         ,   max(d.isgraduated)  isgraduated                                                                                                                                      \n")
                    .append("                         ,   max(c.userid)       userid                                                                                                                                           \n")
                    .append("                     from    tz_blstudent        a                                                                                                                                                \n")
                    .append("                         ,   tz_blcourseseq      b                                                                                                                                                \n")
                    .append("                         ,   tz_blproposesubj    c                                                                                                                                                \n")
                    .append("                         ,   tz_student            d                                                                                                                                                \n")
                    .append("                     where   a.coursecode = b.coursecode                                                                                                                                          \n")
                    .append("                     and     a.courseyear = b.courseyear                                                                                                                                          \n")
                    .append("                     and     a.courseseq = b.courseseq                                                                                                                                            \n")
                    .append("                     and     c.subj = d.subj                                                                                                                                                      \n")
                    .append("                     and     c.userid = d.userid                                                                                                                                                  \n")
                    .append("                     and     c.status = 'RV'                                                                                                                                                      \n")
                    .append("                     and     c.isrequired = 'Y'                                                                                                                                                   \n")
                    .append("                     and     add_months(to_date(d.eduend, 'YYYYMMDD'), b.availableperiod * 12) >= to_date(b.onlinepropstart)                                                                      \n")
                    .append("                     group by d.subj                                                                                                                                                              \n")
                    .append("              union                                                                                                                                                                               \n")
                    .append("              select d.subj                                                                                                                                                                       \n")
                    .append("                 ,   d.score                                                                                                                                                                      \n")
                    .append("                 ,   d.isgraduated                                                                                                                                                                \n")
                    .append("                 ,   a.userid                                                                                                                                                                     \n")
                    .append("              from   tz_blstudent        a                                                                                                                                                        \n")
                    .append("                 ,   tz_blcourseseq      b                                                                                                                                                        \n")
                    .append("                 ,   tz_blproposesubj    c                                                                                                                                                        \n")
                    .append("                 ,   tz_student            d                                                                                                                                                        \n")
                    .append("              where  a.coursecode    = b.coursecode                                                                                                                                               \n")
                    .append("              and    a.courseyear    = b.courseyear                                                                                                                                               \n")
                    .append("              and    a.courseseq     = b.courseseq                                                                                                                                                \n")
                    .append("              and    c.subj          = d.subj                                                                                                                                                     \n")
                    .append("              and    c.userid        = d.userid                                                                                                                                                   \n")
                    .append("              and    c.year          = d.year                                                                                                                                                     \n")
                    .append("              and    c.subjseq       = d.subjseq                                                                                                                                                  \n")
                    .append("              and    (c.status = 'RE' or c.status = 'FE')                                                                                                                                         \n")
                    .append("              and    c.isrequired = 'Y'                                                                                                                                                           \n")
                    .append("          )                                                                                                                                                                                       \n")
                    .append("          group by userid                                                                                                                                                                         \n")
                    .append("     )       f                                                                                                                                                                                    \n")
                    .append(" ,   (                                                                                                                                                                                            \n")
                    .append("         select  avg(nvl(score, 0)) offline_score                                                                                                                                                 \n")
                    .append("             ,   userid                                                                                                                                                                           \n")
                    .append("             ,   sum(decode(isgraduated, 'Y', 1, 0)) offline_iscomplete_y                                                                                                                         \n")
                    .append("             ,   sum(decode(isgraduated, 'N', 1, 0)) offline_iscomplete_n                                                                                                                         \n")
                    .append("          from   (                                                                                                                                                                                \n")
                    .append("                     select  d.subj                                                                                                                                                               \n")
                    .append("                         ,   d.score                                                                                                                                                              \n")
                    .append("                         ,   d.isgraduated                                                                                                                                                        \n")
                    .append("                         ,   a.userid                                                                                                                                                             \n")
                    .append("                     from    tz_blstudent        a                                                                                                                                                \n")
                    .append("                         ,   tz_blcourseseq      b                                                                                                                                                \n")
                    .append("                         ,   tz_blcourseseqsubj  c                                                                                                                                                \n")
                    .append("                         ,   tz_student            d                                                                                                                                                \n")
                    .append("                     where   a.coursecode    = b.coursecode                                                                                                                                       \n")
                    .append("                     and     a.courseyear    = b.courseyear                                                                                                                                       \n")
                    .append("                     and     a.courseseq     = b.courseseq                                                                                                                                        \n")
                    .append("                     and     c.subj          = d.subj                                                                                                                                             \n")
                    .append("                     and     a.userid        = d.userid                                                                                                                                           \n")
                    .append("                     and     c.year          = d.year                                                                                                                                             \n")
                    .append("                     and     c.subjseq       = d.subjseq                                                                                                                                          \n")
                    .append("                     and     isonoff         = 'OFF'                                                                                                                                              \n")
                    .append("                 )                                                                                                                                                                                \n")
                    .append("         group by userid                                                                                                                                                                          \n")
                    .append("     )       g                                                                                                                                                                                    \n")
                    .append(" ,   (                                                                                                                                                                                            \n")
                    .append("         select  coursecode                                                                                                                                                                       \n")
                    .append("             ,   courseyear                                                                                                                                                                       \n")
                    .append("             ,   courseseq                                                                                                                                                                        \n")
                    .append("             ,   sum(decode(isonoff, 'ON', 1, 0))    online_subjnum                                                                                                                               \n")
                    .append("             ,   sum(decode(isonoff, 'OFF', 1, 0))   offline_subjnum                                                                                                                              \n")
                    .append("          from tz_blcourseseqsubj                                                                                                                                                                 \n")
                    .append("          where (isrequired = 'Y' or isrequired is null)                                                                                                                                          \n")
                    .append("          group by coursecode, courseyear, courseseq                                                                                                                                              \n")
                    .append("     )       h                                                                                                                                                                                    \n")
                    .append("  ,  vz_orgmember     i                                                                                                                                                                           \n")
                    .append(" where   a.coursecode    = b.coursecode                                                                                                                                                           \n")
                    .append(" and     b.coursecode    = c.coursecode                                                                                                                                                           \n")
                    .append(" and     b.courseyear    = c.courseyear                                                                                                                                                           \n")
                    .append(" and     b.courseseq     = c.courseseq                                                                                                                                                            \n")
                    .append(" and     c.userid        = d.userid                                                                                                                                                               \n")
                    .append(" and     c.branchcode    = e.branchcode                                                                                                                                                           \n")
                    .append(" and     c.userid        = f.userid(+)                                                                                                                                                            \n")
                    .append(" and     c.userid        = g.userid(+)                                                                                                                                                            \n")
                    .append(" and     b.coursecode    = h.coursecode                                                                                                                                                           \n")
                    .append(" and     b.courseyear    = h.courseyear                                                                                                                                                           \n")
                    .append(" and     b.courseseq     = h.courseseq                                                                                                                                                            \n")
                    .append(" and     b.grcode        = " + StringManager.makeSQL(ss_grcode       ) + "                                                                                                                        \n")
                    .append(" and     b.courseyear    = " + StringManager.makeSQL(ss_blcourseyear ) + "                                                                                                                        \n")
                    .append(" and     c.userid        = i.userid(+)                                                                                                                                                            \n");

               if ( !ss_blcourse.equals("ALL") ) { 
                   sbSQL.append(" and b.coursecode = " + StringManager.makeSQL(ss_blcourse     ) + "       \n");
               }
               
               if ( !ss_blcourseseq.equals("ALL") ) { 
                   sbSQL.append(" and b.courseseq = " + StringManager.makeSQL(ss_blcourseseq   ) + "       \n");
               }
               
               if( !ss_branch.equals("ALL")) {
                   sbSQL.append(" and c.branchcode = " + StringManager.makeSQL(ss_branch       ) + "       \n");
               }
               
               if ( !v_iscomplete.equals("") ) {
                   sbSQL.append(" and c.isgraduated = " + StringManager.makeSQL(v_iscomplete) + "         \n");
               } else {
                   sbSQL.append(" and c.status in ('G', 'I', 'J', 'K', 'L')                               \n");
               }

               if ( !box.getString("p_sdate").equals("") && !box.getString("p_ldate").equals("") ) { // 기간선택
                   if ( box.getString("p_sdate").equals(box.getString("p_ldate")) ) {
                       sbSQL.append("          AND TO_CHAR(TO_DATE(i.indate, 'yyyymmddhh24miss'), 'yyyymm') = TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymm') \n");
                   } else {
                       sbSQL.append("          AND TO_CHAR(TO_DATE(i.indate, 'yyyymmddhh24miss'), 'yyyymm') BETWEEN TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymm') AND TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_ldate")) + ", 'yyyy-mm-dd'), 'yyyymm') \n");
                   }
               }
               
               if ( !c_sido.equals("") ) {
                   sbSQL.append("             AND i.sido               = " + StringManager.makeSQL(c_sido          ) + "                                       \n");
               }    

               if ( !c_age.equals("") ) {
                   sbSQL.append("             AND i.age               = " + StringManager.makeSQL(c_age            ) + "                                       \n");               
               }
               
               if ( !c_achievement.equals("") ) {
                   sbSQL.append("             AND i.achievement       = " + StringManager.makeSQL(c_achievement    ) + "                                       \n");               
               }
               
               if ( !c_gender.equals("") ) {
                   sbSQL.append("             AND i.gender            = " + StringManager.makeSQL(c_gender         ) + "                                       \n");               
               }
               
               if ( !c_gugun.equals("") ) {
                   sbSQL.append("             AND i.gugun             = " + StringManager.makeSQL(c_gubun          ) + "                                       \n");
               }
               
               if ( !v_orgcode.equals("ALL") ) { 
                   sbSQL.append("            AND  i.orgcode           = " + v_orgcode                                + "                                       \n");
               }
               
               System.out.println("sql : " + sbSQL.toString());
                   
               ls          = connMgr.executeQuery(sbSQL.toString());
               
               while (ls.next()) {
                   dbox = ls.getDataBox();
                   
                   dbox.put("d_wonlinetest", new Double(ls.getDouble("wonlinetest")));
                   dbox.put("d_wofflinetest", new Double(ls.getDouble("wofflinetest")));
                   dbox.put("d_wportfolio", new Double(ls.getDouble("wportfolio")));
                   dbox.put("d_wvalue", new Double(ls.getDouble("wvalue")));
                   dbox.put("d_portfolio", new Double(ls.getDouble("portfolio")));
                   dbox.put("d_valuation", new Double(ls.getDouble("valuation")));
                   dbox.put("d_addpointvalue", new Double(ls.getDouble("addpointvalue")));
                   dbox.put("d_addpointvalue1", new Double(ls.getDouble("addpointvalue1")));
                   dbox.put("d_addpointvalue2", new Double(ls.getDouble("addpointvalue2")));
                   dbox.put("d_online_score", new Double(ls.getDouble("online_score")));
                   dbox.put("d_offline_score", new Double(ls.getDouble("offline_score")));
                   dbox.put("d_total", new Double(ls.getDouble("total")));
                   
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
       연도별교육실적
       @param box      receive from the form object and session
       @return ArrayList
       */
        public ArrayList perform_03_01_04(RequestBox box) throws Exception { 
            DBConnectionManager connMgr         = null;
            ListSet             ls              = null;
            ArrayList           list            = null;
            StringBuffer        sbSQL           = new StringBuffer("");
             
            DataBox             dbox            = null;
             
            String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
            String              v_courseyear    = box.getStringDefault  ( "p_courseyear"   , ""    ); // 년도
            String              v_courseseq     = box.getStringDefault  ( "p_courseseq"    , ""    ); // 과목 기수
            String              v_coursecode    = box.getStringDefault  ( "p_coursecode"   , ""    ); // 과목&코스
            String              s_gadmin        = box.getSession        ( "gadmin"                 );
            String              v_branch        = box.getStringDefault  ( "p_branch"       , ""    );
            String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
            String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
            String              v_status        = box.getString         ( "p_status"               );   
             
            if("K2".equals(s_gadmin)) {
                 v_branch                       = box.getSession("branch");
            } else {
                 v_branch                       = box.getString("p_branch");           // 거점
            }

            try {
                connMgr     = new DBConnectionManager();            
                list        = new ArrayList();
                
                sbSQL.append(" SELECT  *  FROM (                                                                                                                                                                \n")
                     .append(" SELECT  a.coursenm                                                                                                                                                               \n")
                     .append("     ,   b.coursecode                                                                                                                                                             \n")
                     .append("     ,   b.courseyear                                                                                                                                                             \n")
                     .append("     ,   b.courseseq                                                                                                                                                              \n")
                     .append("     ,   d.userid                                                                                                                                                                 \n")
                     .append("     ,   d.name                                                                                                                                                                   \n")
                     .append("     ,   e.branchnm                                                                                                                                                               \n")
                     .append("     ,   c.status                                                                                                                                                                 \n")
                     .append("     ,   b.subjnumforcomplete                                                                                                                                                     \n")
                     .append("     ,   b.wonlinetest                                                                                                                                                            \n")
                     .append("     ,   b.wofflinetest                                                                                                                                                           \n")
                     .append("     ,   b.wportfolio                                                                                                                                                             \n")
                     .append("     ,   b.wvalue                                                                                                                                                                 \n")
                     .append("     ,   c.portfolio                                                                                                                                                              \n")
                     .append("     ,   c.valuation                                                                                                                                                              \n")
                     .append("     ,   c.addpointvalue                                                                                                                                                          \n")
                     .append("     ,   c.addpointvalue1                                                                                                                                                         \n")
                     .append("     ,   c.addpointvalue2                                                                                                                                                         \n")
                     .append("     ,   c.isgraduated                                                                                                                                                            \n")
                     .append("     ,   nvl(round(online_score, 2), 0)                              online_score                                                                                                 \n")
                     .append("     ,   nvl(online_iscomplete_y, 0)                                 online_iscomplete_y                                                                                          \n")
                     .append("     ,   nvl(online_iscomplete_n, 0)                                 online_iscomplete_n                                                                                          \n")
                     .append("     ,   nvl(offline_score, 0)                                       offline_score                                                                                                \n")
                     .append("     ,   nvl(offline_iscomplete_y, 0)                                offline_iscomplete_y                                                                                         \n")
                     .append("     ,   nvl(offline_iscomplete_n, 0)                                offline_iscomplete_n                                                                                         \n")
                     .append("     ,   online_subjnum                                                                                                                                                           \n")
                     .append("     ,   offline_subjnum                                                                                                                                                          \n")
                     .append("     ,   round((nvl(online_score, 0) * b.wonlinetest     / 100)                                                                                                                   \n")
                     .append("             + (nvl(offline_score, 0) * b.wofflinetest   / 100)                                                                                                                   \n")
                     .append("             + (nvl(c.portfolio, 0) * b.wportfolio       / 100)                                                                                                                   \n")
                     .append("             + (nvl(c.valuation, 0) * b.wvalue           / 100)                                                                                                                   \n")
                     .append("             + nvl(addpointvalue, 0)                                                                                                                                              \n")
                     .append("             + nvl(addpointvalue1, 0)                                                                                                                                             \n")
                     .append("             + nvl(addpointvalue2, 0)                                                                                                                                             \n")
                     .append("             , 1)                                                    total                                                                                                        \n")
                     .append("     ,   case when (b.subjnumforcomplete <= (online_iscomplete_y + offline_iscomplete_y))                                                                                         \n")
                     .append("              then 'Y'                                                                                                                                                            \n")
                     .append("              else 'N'                                                                                                                                                            \n")
                     .append("         end                                                         gubun                                                                                                        \n")                                           
                     .append("     ,   case when (b.subjnumforcomplete <= (    online_iscomplete_y                                                                                                              \n")
                     .append("                                             +   offline_iscomplete_y)                                                                                                            \n")
                     .append("                   )                                                                                                                                                              \n")
                     .append("              then rank() over (partition by c.courseyear, c.courseseq, c.coursecode                                                                                              \n")
                     .append("                                order by     round((nvl(online_score, 0) * b.wonlinetest/100)                                                                                     \n")
                     .append("                                                 + (nvl(offline_score, 0) * b.wofflinetest/100)                                                                                   \n")
                     .append("                                                 + (nvl(c.portfolio, 0) * b.wportfolio)                                                                                           \n")
                     .append("                                                 + (nvl(c.valuation, 0) * b.wvalue)                                                                                               \n")
                     .append("                                                 + nvl(addpointvalue, 0)                                                                                                          \n")
                     .append("                                                 + nvl(addpointvalue1, 0)                                                                                                         \n")
                     .append("                                                 + nvl(addpointvalue2, 0)                                                                                                         \n")
                     .append("                                                 , 1) desc                                                                                                                        \n")
                     .append("                               )                                                                                                                                                  \n")
                     .append("              else 0                                                                                                                                                              \n")
                     .append("         end                                                         rank                                                                                                         \n")
                     .append(" from    tz_bl_course    a                                                                                                                                                        \n")
                     .append("     ,   tz_blcourseseq  b                                                                                                                                                        \n")
                     .append("     ,   tz_blstudent    c                                                                                                                                                        \n")
                     .append("     ,   tz_member       d                                                                                                                                                        \n")
                     .append("     ,   tz_branch       e                                                                                                                                                        \n")
                     .append("     ,   (                                                                                                                                                                        \n")
                     .append("             select  courseyear                                                                                                                                                   \n")
                     .append("                 ,   courseseq                                                                                                                                                    \n")
                     .append("                 ,   coursecode                                                                                                                                                   \n")
                     .append("                 ,   avg(nvl(score, 0)) online_score                                                                                                                              \n")
                     .append("                 ,   userid                                                                                                                                                       \n")
                     .append("                 ,   sum(decode(isgraduated, 'Y', 1, 0)) online_iscomplete_y                                                                                                      \n")
                     .append("                 ,   sum(decode(isgraduated, 'N', 1, 0)) online_iscomplete_n                                                                                                      \n")
                     .append("             from    (                                                                                                                                                            \n")
                     .append("                         select  a.courseyear                                                                                                                                     \n")
                     .append("                             ,   a.courseseq                                                                                                                                      \n")
                     .append("                             ,   a.coursecode                                                                                                                                     \n")
                     .append("                             ,   d.subj                                                                                                                                           \n")
                     .append("                             ,   max(score)          score                                                                                                                        \n")
                     .append("                             ,   max(d.isgraduated)  isgraduated                                                                                                                  \n")
                     .append("                             ,   max(c.userid)       userid                                                                                                                       \n")
                     .append("                         from    tz_blstudent        a                                                                                                                            \n")
                     .append("                             ,   tz_blcourseseq      b                                                                                                                            \n")
                     .append("                             ,   tz_blproposesubj    c                                                                                                                            \n")
                     .append("                             ,   tz_student            d                                                                                                                            \n")
                     .append("                         where   a.coursecode    = b.coursecode                                                                                                                   \n")
                     .append("                         and     a.courseyear    = b.courseyear                                                                                                                   \n")
                     .append("                         and     a.courseseq     = b.courseseq                                                                                                                    \n")
                     .append("                         and     a.coursecode    = c.coursecode                                                                                                                   \n")
                     .append("                         and     a.courseyear    = c.courseyear                                                                                                                   \n")
                     .append("                         and     a.courseseq     = c.courseseq                                                                                                                    \n")
                     .append("                         and     a.userid        = c.userid                                                                                                                       \n")
                     .append("                         and     c.subj          = d.subj                                                                                                                         \n")
                     .append("                         and     c.userid        = d.userid                                                                                                                       \n")
                     .append("                         and     c.status        = 'RV'                                                                                                                           \n")
                     .append("                         and     c.isrequired    = 'Y'                                                                                                                            \n")
                     .append("                         and     add_months(to_date(d.eduend, 'YYYYMMDD'), b.availableperiod * 12) >= to_date(b.onlinepropstart)                                                  \n")
                     .append("                         group by a.courseyear, a.courseseq, a.coursecode, d.subj                                                                                                 \n")
                     .append("          union                                                                                                                                                                   \n")
                     .append("          select     a.courseyear                                                                                                                                                 \n")
                     .append("                 ,   a.courseseq                                                                                                                                                  \n")
                     .append("                 ,   a.coursecode                                                                                                                                                 \n")
                     .append("                 ,   d.subj                                                                                                                                                       \n")
                     .append("                 ,   d.score                                                                                                                                                      \n")
                     .append("                 ,   d.isgraduated                                                                                                                                                \n")
                     .append("                 ,   a.userid                                                                                                                                                     \n")
                     .append("          from       tz_blstudent        a                                                                                                                                        \n")
                     .append("                 ,   tz_blcourseseq      b                                                                                                                                        \n")
                     .append("                 ,   tz_blproposesubj    c                                                                                                                                        \n")
                     .append("                 ,   tz_student            d                                                                                                                                        \n")
                     .append("          where      a.coursecode    = b.coursecode                                                                                                                               \n")
                     .append("          and        a.courseyear    = b.courseyear                                                                                                                               \n")
                     .append("          and        a.courseseq     = b.courseseq                                                                                                                                \n")
                     .append("          and        a.coursecode    = c.coursecode                                                                                                                               \n")
                     .append("          and        a.courseyear    = c.courseyear                                                                                                                               \n")
                     .append("          and        a.courseseq     = c.courseseq                                                                                                                                \n")
                     .append("          and        a.userid        = c.userid                                                                                                                                   \n")
                     .append("          and        c.subj          = d.subj                                                                                                                                     \n")
                     .append("          and        c.userid        = d.userid                                                                                                                                   \n")
                     .append("          and        c.year          = d.year                                                                                                                                     \n")
                     .append("          and        c.subjseq       = d.subjseq                                                                                                                                  \n")
                     .append("          and        (c.status = 'RE' or c.status = 'FE')                                                                                                                         \n")
                     .append("          and        c.isrequired    = 'Y'                                                                                                                                        \n")
                     .append("         )                                                                                                                                                                        \n")
                     .append("         group by    courseyear, courseseq, coursecode, userid                                                                                                                    \n")
                     .append("     )           f                                                                                                                                                                \n")
                     .append(" ,   (                                                                                                                                                                            \n")
                     .append("         select  courseyear                                                                                                                                                       \n")
                     .append("             ,   courseseq                                                                                                                                                        \n")
                     .append("             ,   coursecode                                                                                                                                                       \n")
                     .append("             ,   avg(nvl(score, 0))      offline_score                                                                                                                            \n")
                     .append("             ,   userid                                                                                                                                                           \n")
                     .append("             ,   sum(decode(isgraduated, 'Y', 1, 0)) offline_iscomplete_y                                                                                                         \n")
                     .append("             ,   sum(decode(isgraduated, 'N', 1, 0)) offline_iscomplete_n                                                                                                         \n")
                     .append("         from    (                                                                                                                                                                \n")
                     .append("                     select  a.courseyear, a.courseseq, a.coursecode                                                                                                              \n")
                     .append("                         ,   d.subj, d.score, d.isgraduated, a.userid                                                                                                             \n")
                     .append("                     from    tz_blstudent        a                                                                                                                                \n")
                     .append("                         ,   tz_blcourseseq      b                                                                                                                                \n")
                     .append("                         ,   tz_blcourseseqsubj  c                                                                                                                                \n")
                     .append("                         ,   tz_student            d                                                                                                                                \n")
                     .append("                     where   a.coursecode    = b.coursecode                                                                                                                       \n")
                     .append("                     and     a.courseyear    = b.courseyear                                                                                                                       \n")
                     .append("                     and     a.courseseq     = b.courseseq                                                                                                                        \n")
                     .append("                     and     a.coursecode    = c.coursecode                                                                                                                       \n")
                     .append("                     and     a.courseyear    = c.courseyear                                                                                                                       \n")
                     .append("                     and     a.courseseq     = c.courseseq                                                                                                                        \n")
                     .append("                     and     c.subj          = d.subj                                                                                                                             \n")
                     .append("                     and     a.userid        = d.userid                                                                                                                           \n")
                     .append("                     and     c.year          = d.year                                                                                                                             \n")
                     .append("                     and     c.subjseq       = d.subjseq                                                                                                                          \n")
                     .append("                     and     isonoff         = 'OFF'                                                                                                                              \n")
                     .append("                 )                                                                                                                                                                \n")
                     .append("         group by courseyear, courseseq, coursecode, userid                                                                                                                       \n")
                     .append("     )           g                                                                                                                                                                \n")
                     .append(" ,   (                                                                                                                                                                            \n")
                     .append("         select  coursecode                                                                                                                                                       \n")
                     .append("             ,   courseyear                                                                                                                                                       \n")
                     .append("             ,   courseseq                                                                                                                                                        \n")
                     .append("             ,   sum(decode(isonoff, 'ON', 1, 0))    online_subjnum                                                                                                               \n")
                     .append("             ,   sum(decode(isonoff, 'OFF', 1, 0))   offline_subjnum                                                                                                              \n")
                     .append("         from    tz_blcourseseqsubj                                                                                                                                               \n")
                     .append("         where   ( isrequired = 'Y' or isrequired is null )                                                                                                                       \n")
                     .append("         group by coursecode, courseyear, courseseq                                                                                                                               \n")
                     .append("     )                h                                                                                                                                                           \n");
                
                if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                    sbSQL.append("  ,  vz_orgmember     i                                                                                                                                                       \n");
                }
                
                sbSQL.append(" where   a.coursecode    = b.coursecode                                                                                                                                           \n")
                     .append(" and     b.coursecode    = c.coursecode                                                                                                                                           \n")
                     .append(" and     b.courseyear    = c.courseyear                                                                                                                                           \n")
                     .append(" and     b.courseseq     = c.courseseq                                                                                                                                            \n")
                     .append(" and     c.userid        = d.userid                                                                                                                                               \n")
                     .append(" and     c.branchcode    = e.branchcode                                                                                                                                           \n")
                     .append(" and     c.userid        = f.userid      (+)                                                                                                                                      \n")
                     .append(" and     c.courseyear    = f.courseyear  (+)                                                                                                                                      \n")
                     .append(" and     c.coursecode    = f.coursecode  (+)                                                                                                                                      \n")
                     .append(" and     c.courseseq     = f.courseseq   (+)                                                                                                                                      \n")
                     .append(" and     c.userid        = g.userid      (+)                                                                                                                                      \n")
                     .append(" and     c.courseyear    = g.courseyear  (+)                                                                                                                                      \n")
                     .append(" and     c.coursecode    = g.coursecode  (+)                                                                                                                                      \n")
                     .append(" and     c.courseseq     = g.courseseq   (+)                                                                                                                                      \n")
                     .append(" and     b.coursecode    = h.coursecode                                                                                                                                           \n")
                     .append(" and     b.courseyear    = h.courseyear                                                                                                                                           \n")
                     .append(" and     b.courseseq     = h.courseseq                                                                                                                                            \n")
                     .append(" and     b.grcode        = " + StringManager.makeSQL(v_grcode         ) + "                                                                                                       \n")
                    .append(" and     b.courseyear    = " + StringManager.makeSQL(v_courseyear     ) + "                                                                                                        \n");

                 if ( !v_coursecode.equals("") ) { 
                     sbSQL.append(" and b.coursecode    = " + StringManager.makeSQL(v_coursecode    ) + "   \n");
                 }
                 
                 if ( !v_courseseq.equals("") ) { 
                     sbSQL.append(" and b.courseseq     = " + StringManager.makeSQL(v_courseseq     ) + "   \n");
                 }
                 
                 if( !v_branch.equals("") ) {
                     sbSQL.append(" and c.branchcode    = " + StringManager.makeSQL(v_branch        ) + "   \n");
                 }
                 
                 if ( v_isgraduated.equals("Y") ) {
                     sbSQL.append(" and c.isgraduated   = " + StringManager.makeSQL(v_isgraduated   ) + "   \n");
                 } else if ( v_isgraduated.equals("N") ) {
                     sbSQL.append(" and ( c.isgraduated   IS NULL OR c.isgraduated = " + StringManager.makeSQL(v_isgraduated   ) + " ) \n");
                 }
                 
                 if ( v_status.equals("A") ) { 
                     sbSQL.append("                          AND     c.STATUS        < 'G'                                                              \n");
                 } else if ( v_status.equals("G") ) {
                    sbSQL.append("                          AND     c.STATUS         >= 'G'                                                             \n");
                 }    
                 
                 if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                     sbSQL.append(" AND     i.orgcode       = " + v_orgcode + "                             \n")
                          .append(" and     c.userid        = i.userid                                      \n");
                 }
                 
                 sbSQL.append(" ) ORDER BY courseyear, coursecode, courseseq, rank, name                    \n");
                 
                 
                 System.out.println("sql : " + sbSQL.toString());
                     
                 ls          = connMgr.executeQuery(sbSQL.toString());
                 
                 while (ls.next()) {
                     dbox   = ls.getDataBox();
                     
                     dbox.put("d_wonlinetest"       , new Double(ls.getDouble("wonlinetest"))   );
                     dbox.put("d_wofflinetest"      , new Double(ls.getDouble("wofflinetest"))  );
                     dbox.put("d_wportfolio"        , new Double(ls.getDouble("wportfolio"))    );
                     dbox.put("d_wvalue"            , new Double(ls.getDouble("wvalue"))        );
                     dbox.put("d_portfolio"         , new Double(ls.getDouble("portfolio"))     );
                     dbox.put("d_valuation"         , new Double(ls.getDouble("valuation"))     );
                     dbox.put("d_addpointvalue"     , new Double(ls.getDouble("addpointvalue")) );
                     dbox.put("d_addpointvalue1"    , new Double(ls.getDouble("addpointvalue1")));
                     dbox.put("d_addpointvalue2"    , new Double(ls.getDouble("addpointvalue2")));
                     dbox.put("d_online_score"      , new Double(ls.getDouble("online_score"))  );
                     dbox.put("d_offline_score"     , new Double(ls.getDouble("offline_score")) );
                     dbox.put("d_total"             , new Double(ls.getDouble("total"))         );
                     
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
        연도별교육실적
        @param box      receive from the form object and session
        @return ArrayList
        */
         public ArrayList perform_03_01_05(RequestBox box) throws Exception { 
             DBConnectionManager connMgr         = null;
             ListSet             ls              = null;
             ArrayList           list            = null;
             StringBuffer        sbSQL           = new StringBuffer("");
              
             DataBox             dbox            = null;
              
             String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
             String              v_courseyear    = box.getStringDefault  ( "p_courseyear"   , ""    ); // 년도
             String              v_courseseq     = box.getStringDefault  ( "p_courseseq"    , ""    ); // 과목 기수
             String              v_coursecode    = box.getStringDefault  ( "p_coursecode"   , ""    ); // 과목&코스
             String              s_gadmin        = box.getSession        ( "gadmin"                 );
             String              v_branch        = box.getStringDefault  ( "p_branch"       , ""    );
             String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
             String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
             String              v_status        = box.getString         ( "p_status"               );
             String              v_sido          = box.getString         ( "p_sido"                 );
             String              v_gugun         = box.getString         ( "p_gugun"                );
             String              v_gender        = box.getString         ( "p_gender"               );
             String              v_achievement   = box.getString         ( "p_achievement"          );
             String              v_age           = box.getString         ( "p_age"                  );
             String              v_gubun         = box.getString         ( "p_gubun"                );
              
             if("K2".equals(s_gadmin)) {
                  v_branch                       = box.getSession("branch");
             } else {
                  v_branch                       = box.getString("p_branch");           // 거점
             }

             try {
                 connMgr     = new DBConnectionManager();            
                 list        = new ArrayList();
                 
                 sbSQL.append(" SELECT  *  FROM (                                                                                                                                                                \n")
                      .append(" SELECT  a.coursenm                                                                                                                                                               \n")
                      .append("     ,   b.coursecode                                                                                                                                                             \n")
                      .append("     ,   b.courseyear                                                                                                                                                             \n")
                      .append("     ,   b.courseseq                                                                                                                                                              \n")
                      .append("     ,   d.userid                                                                                                                                                                 \n")
                      .append("     ,   d.name                                                                                                                                                                   \n")
                      .append("     ,   e.branchnm                                                                                                                                                               \n")
                      .append("     ,   c.status                                                                                                                                                                 \n")
                      .append("     ,   b.subjnumforcomplete                                                                                                                                                     \n")
                      .append("     ,   b.wonlinetest                                                                                                                                                            \n")
                      .append("     ,   b.wofflinetest                                                                                                                                                           \n")
                      .append("     ,   b.wportfolio                                                                                                                                                             \n")
                      .append("     ,   b.wvalue                                                                                                                                                                 \n")
                      .append("     ,   c.portfolio                                                                                                                                                              \n")
                      .append("     ,   c.valuation                                                                                                                                                              \n")
                      .append("     ,   c.addpointvalue                                                                                                                                                          \n")
                      .append("     ,   c.addpointvalue1                                                                                                                                                         \n")
                      .append("     ,   c.addpointvalue2                                                                                                                                                         \n")
                      .append("     ,   c.isgraduated                                                                                                                                                            \n")
                      .append("     ,   nvl(round(online_score, 2), 0)                              online_score                                                                                                 \n")
                      .append("     ,   nvl(online_iscomplete_y, 0)                                 online_iscomplete_y                                                                                          \n")
                      .append("     ,   nvl(online_iscomplete_n, 0)                                 online_iscomplete_n                                                                                          \n")
                      .append("     ,   nvl(offline_score, 0)                                       offline_score                                                                                                \n")
                      .append("     ,   nvl(offline_iscomplete_y, 0)                                offline_iscomplete_y                                                                                         \n")
                      .append("     ,   nvl(offline_iscomplete_n, 0)                                offline_iscomplete_n                                                                                         \n")
                      .append("     ,   online_subjnum                                                                                                                                                           \n")
                      .append("     ,   offline_subjnum                                                                                                                                                          \n")
                      .append("     ,   round((nvl(online_score, 0) * b.wonlinetest     / 100)                                                                                                                   \n")
                      .append("             + (nvl(offline_score, 0) * b.wofflinetest   / 100)                                                                                                                   \n")
                      .append("             + (nvl(c.portfolio, 0) * b.wportfolio       / 100)                                                                                                                   \n")
                      .append("             + (nvl(c.valuation, 0) * b.wvalue           / 100)                                                                                                                   \n")
                      .append("             + nvl(addpointvalue, 0)                                                                                                                                              \n")
                      .append("             + nvl(addpointvalue1, 0)                                                                                                                                             \n")
                      .append("             + nvl(addpointvalue2, 0)                                                                                                                                             \n")
                      .append("             , 1)                                                    total                                                                                                        \n")
                      .append("     ,   case when (b.subjnumforcomplete <= (online_iscomplete_y + offline_iscomplete_y))                                                                                         \n")
                      .append("              then 'Y'                                                                                                                                                            \n")
                      .append("              else 'N'                                                                                                                                                            \n")
                      .append("         end                                                         gubun                                                                                                        \n")                                           
                      .append("     ,   case when (b.subjnumforcomplete <= (    online_iscomplete_y                                                                                                              \n")
                      .append("                                             +   offline_iscomplete_y)                                                                                                            \n")
                      .append("                   )                                                                                                                                                              \n")
                      .append("              then rank() over (partition by c.courseyear, c.courseseq, c.coursecode                                                                                              \n")
                      .append("                                order by     round((nvl(online_score, 0) * b.wonlinetest/100)                                                                                     \n")
                      .append("                                                 + (nvl(offline_score, 0) * b.wofflinetest/100)                                                                                   \n")
                      .append("                                                 + (nvl(c.portfolio, 0) * b.wportfolio)                                                                                           \n")
                      .append("                                                 + (nvl(c.valuation, 0) * b.wvalue)                                                                                               \n")
                      .append("                                                 + nvl(addpointvalue, 0)                                                                                                          \n")
                      .append("                                                 + nvl(addpointvalue1, 0)                                                                                                         \n")
                      .append("                                                 + nvl(addpointvalue2, 0)                                                                                                         \n")
                      .append("                                                 , 1)    desc                                                                                                                     \n")
                      .append("                               )                                                                                                                                                  \n")
                      .append("              else 0                                                                                                                                                              \n")
                      .append("         end                                                         rank                                                                                                         \n")
                      .append(" from    tz_bl_course    a                                                                                                                                                        \n")
                      .append("     ,   tz_blcourseseq  b                                                                                                                                                        \n")
                      .append("     ,   tz_blstudent    c                                                                                                                                                        \n")
                      .append("     ,   tz_member       d                                                                                                                                                        \n")
                      .append("     ,   tz_branch       e                                                                                                                                                        \n")
                      .append("     ,   (                                                                                                                                                                        \n")
                      .append("             select  courseyear                                                                                                                                                   \n")
                      .append("                 ,   courseseq                                                                                                                                                    \n")
                      .append("                 ,   coursecode                                                                                                                                                   \n")
                      .append("                 ,   avg(nvl(score, 0)) online_score                                                                                                                              \n")
                      .append("                 ,   userid                                                                                                                                                       \n")
                      .append("                 ,   sum(decode(isgraduated, 'Y', 1, 0)) online_iscomplete_y                                                                                                      \n")
                      .append("                 ,   sum(decode(isgraduated, 'N', 1, 0)) online_iscomplete_n                                                                                                      \n")
                      .append("             from    (                                                                                                                                                            \n")
                      .append("                         select  a.courseyear                                                                                                                                     \n")
                      .append("                             ,   a.courseseq                                                                                                                                      \n")
                      .append("                             ,   a.coursecode                                                                                                                                     \n")
                      .append("                             ,   d.subj                                                                                                                                           \n")
                      .append("                             ,   max(score)          score                                                                                                                        \n")
                      .append("                             ,   max(d.isgraduated)  isgraduated                                                                                                                  \n")
                      .append("                             ,   max(c.userid)       userid                                                                                                                       \n")
                      .append("                         from    tz_blstudent        a                                                                                                                            \n")
                      .append("                             ,   tz_blcourseseq      b                                                                                                                            \n")
                      .append("                             ,   tz_blproposesubj    c                                                                                                                            \n")
                      .append("                             ,   tz_student            d                                                                                                                            \n")
                      .append("                         where   a.coursecode    = b.coursecode                                                                                                                   \n")
                      .append("                         and     a.courseyear    = b.courseyear                                                                                                                   \n")
                      .append("                         and     a.courseseq     = b.courseseq                                                                                                                    \n")
                      .append("                         and     a.coursecode    = c.coursecode                                                                                                                   \n")
                      .append("                         and     a.courseyear    = c.courseyear                                                                                                                   \n")
                      .append("                         and     a.courseseq     = c.courseseq                                                                                                                    \n")
                      .append("                         and     a.userid        = c.userid                                                                                                                       \n")
                      .append("                         and     c.subj          = d.subj                                                                                                                         \n")
                      .append("                         and     c.userid        = d.userid                                                                                                                       \n")
                      .append("                         and     c.status        = 'RV'                                                                                                                           \n")
                      .append("                         and     c.isrequired    = 'Y'                                                                                                                            \n")
                      .append("                         and     add_months(to_date(d.eduend, 'YYYYMMDD'), b.availableperiod * 12) >= to_date(b.onlinepropstart)                                                  \n")
                      .append("                         group by a.courseyear, a.courseseq, a.coursecode, d.subj                                                                                                 \n")
                      .append("          union                                                                                                                                                                   \n")
                      .append("          select     a.courseyear                                                                                                                                                 \n")
                      .append("                 ,   a.courseseq                                                                                                                                                  \n")
                      .append("                 ,   a.coursecode                                                                                                                                                 \n")
                      .append("                 ,   d.subj                                                                                                                                                       \n")
                      .append("                 ,   d.score                                                                                                                                                      \n")
                      .append("                 ,   d.isgraduated                                                                                                                                                \n")
                      .append("                 ,   a.userid                                                                                                                                                     \n")
                      .append("          from       tz_blstudent        a                                                                                                                                        \n")
                      .append("                 ,   tz_blcourseseq      b                                                                                                                                        \n")
                      .append("                 ,   tz_blproposesubj    c                                                                                                                                        \n")
                      .append("                 ,   tz_student            d                                                                                                                                        \n")
                      .append("          where      a.coursecode    = b.coursecode                                                                                                                               \n")
                      .append("          and        a.courseyear    = b.courseyear                                                                                                                               \n")
                      .append("          and        a.courseseq     = b.courseseq                                                                                                                                \n")
                      .append("          and        a.coursecode    = c.coursecode                                                                                                                               \n")
                      .append("          and        a.courseyear    = c.courseyear                                                                                                                               \n")
                      .append("          and        a.courseseq     = c.courseseq                                                                                                                                \n")
                      .append("          and        a.userid        = c.userid                                                                                                                                   \n")
                      .append("          and        c.subj          = d.subj                                                                                                                                     \n")
                      .append("          and        c.userid        = d.userid                                                                                                                                   \n")
                      .append("          and        c.year          = d.year                                                                                                                                     \n")
                      .append("          and        c.subjseq       = d.subjseq                                                                                                                                  \n")
                      .append("          and        (c.status = 'RE' or c.status = 'FE')                                                                                                                         \n")
                      .append("          and        c.isrequired    = 'Y'                                                                                                                                        \n")
                      .append("         )                                                                                                                                                                        \n")
                      .append("         group by    courseyear, courseseq, coursecode, userid                                                                                                                    \n")
                      .append("     )           f                                                                                                                                                                \n")
                      .append(" ,   (                                                                                                                                                                            \n")
                      .append("         select  courseyear                                                                                                                                                       \n")
                      .append("             ,   courseseq                                                                                                                                                        \n")
                      .append("             ,   coursecode                                                                                                                                                       \n")
                      .append("             ,   avg(nvl(score, 0))      offline_score                                                                                                                            \n")
                      .append("             ,   userid                                                                                                                                                           \n")
                      .append("             ,   sum(decode(isgraduated, 'Y', 1, 0)) offline_iscomplete_y                                                                                                         \n")
                      .append("             ,   sum(decode(isgraduated, 'N', 1, 0)) offline_iscomplete_n                                                                                                         \n")
                      .append("         from    (                                                                                                                                                                \n")
                      .append("                     select  a.courseyear, a.courseseq, a.coursecode                                                                                                              \n")
                      .append("                         ,   d.subj, d.score, d.isgraduated, a.userid                                                                                                             \n")
                      .append("                     from    tz_blstudent        a                                                                                                                                \n")
                      .append("                         ,   tz_blcourseseq      b                                                                                                                                \n")
                      .append("                         ,   tz_blcourseseqsubj  c                                                                                                                                \n")
                      .append("                         ,   tz_student            d                                                                                                                                \n")
                      .append("                     where   a.coursecode    = b.coursecode                                                                                                                       \n")
                      .append("                     and     a.courseyear    = b.courseyear                                                                                                                       \n")
                      .append("                     and     a.courseseq     = b.courseseq                                                                                                                        \n")
                      .append("                     and     a.coursecode    = c.coursecode                                                                                                                       \n")
                      .append("                     and     a.courseyear    = c.courseyear                                                                                                                       \n")
                      .append("                     and     a.courseseq     = c.courseseq                                                                                                                        \n")
                      .append("                     and     c.subj          = d.subj                                                                                                                             \n")
                      .append("                     and     a.userid        = d.userid                                                                                                                           \n")
                      .append("                     and     c.year          = d.year                                                                                                                             \n")
                      .append("                     and     c.subjseq       = d.subjseq                                                                                                                          \n")
                      .append("                     and     isonoff         = 'OFF'                                                                                                                              \n")
                      .append("                 )                                                                                                                                                                \n")
                      .append("         group by courseyear, courseseq, coursecode, userid                                                                                                                       \n")
                      .append("     )           g                                                                                                                                                                \n")
                      .append(" ,   (                                                                                                                                                                            \n")
                      .append("         select  coursecode                                                                                                                                                       \n")
                      .append("             ,   courseyear                                                                                                                                                       \n")
                      .append("             ,   courseseq                                                                                                                                                        \n")
                      .append("             ,   sum(decode(isonoff, 'ON', 1, 0))    online_subjnum                                                                                                               \n")
                      .append("             ,   sum(decode(isonoff, 'OFF', 1, 0))   offline_subjnum                                                                                                              \n")
                      .append("         from    tz_blcourseseqsubj                                                                                                                                               \n")
                      .append("         where   ( isrequired = 'Y' or isrequired is null )                                                                                                                       \n")
                      .append("         group by coursecode, courseyear, courseseq                                                                                                                               \n")
                      .append("     )                h                                                                                                                                                           \n");
                 
                 if ( (!v_sido.equals("") || !v_age.equals("") || !v_achievement.equals("") || !v_gender.equals("") || !v_gugun.equals("")) && !(v_orgcode.equals("") || v_orgcode.equals("ALL")) ) { 
                     sbSQL.append("  ,  vz_orgmember     i                                                                                                                                                       \n");
                 } else if ( (!v_sido.equals("") || !v_age.equals("") || !v_achievement.equals("") || !v_gender.equals("") || !v_gugun.equals("")) && (v_orgcode.equals("") || v_orgcode.equals("ALL")) ) {
                     sbSQL.append("  ,  vz_orgmember1     i                                                                                                                                                       \n");
                 }
                 
                 sbSQL.append(" where   a.coursecode    = b.coursecode                                                                                                                                           \n")
                      .append(" and     b.coursecode    = c.coursecode                                                                                                                                           \n")
                      .append(" and     b.courseyear    = c.courseyear                                                                                                                                           \n")
                      .append(" and     b.courseseq     = c.courseseq                                                                                                                                            \n")
                      .append(" and     c.userid        = d.userid                                                                                                                                               \n")
                      .append(" and     c.branchcode    = e.branchcode                                                                                                                                           \n")
                      .append(" and     c.userid        = f.userid      (+)                                                                                                                                      \n")
                      .append(" and     c.courseyear    = f.courseyear  (+)                                                                                                                                      \n")
                      .append(" and     c.coursecode    = f.coursecode  (+)                                                                                                                                      \n")
                      .append(" and     c.courseseq     = f.courseseq   (+)                                                                                                                                      \n")
                      .append(" and     c.userid        = g.userid      (+)                                                                                                                                      \n")
                      .append(" and     c.courseyear    = g.courseyear  (+)                                                                                                                                      \n")
                      .append(" and     c.coursecode    = g.coursecode  (+)                                                                                                                                      \n")
                      .append(" and     c.courseseq     = g.courseseq   (+)                                                                                                                                      \n")
                      .append(" and     b.coursecode    = h.coursecode                                                                                                                                           \n")
                      .append(" and     b.courseyear    = h.courseyear                                                                                                                                           \n")
                      .append(" and     b.courseseq     = h.courseseq                                                                                                                                            \n")
                      .append(" and     b.grcode        = " + StringManager.makeSQL(v_grcode         ) + "                                                                                                       \n")
                     .append(" and     b.courseyear    = " + StringManager.makeSQL(v_courseyear     ) + "                                                                                                        \n");

                  if ( !v_coursecode.equals("") ) { 
                      sbSQL.append(" and b.coursecode    = " + StringManager.makeSQL(v_coursecode    ) + "   \n");
                  }
                  
                  if ( !v_courseseq.equals("") ) { 
                      sbSQL.append(" and b.courseseq     = " + StringManager.makeSQL(v_courseseq     ) + "   \n");
                  }
                  
                  if( !v_branch.equals("") ) {
                      sbSQL.append(" and c.branchcode    = " + StringManager.makeSQL(v_branch        ) + "   \n");
                  }
                  
                  if ( v_isgraduated.equals("Y") ) {
                      sbSQL.append(" and c.isgraduated   = " + StringManager.makeSQL(v_isgraduated   ) + "   \n");
                  } else if ( v_isgraduated.equals("N") ) {
                      sbSQL.append(" and ( c.isgraduated   IS NULL OR c.isgraduated = " + StringManager.makeSQL(v_isgraduated   ) + " ) \n");
                  }
                  
                  if ( v_status.equals("A") ) { 
                      sbSQL.append("                          AND     c.STATUS        < 'G'                                                              \n");
                  } else if ( v_status.equals("G") ) {
                     sbSQL.append("                          AND     c.STATUS         >= 'G'                                                             \n");
                  }    
                  
                  if ( (!v_sido.equals("") || !v_age.equals("") || !v_achievement.equals("") || !v_gender.equals("") || !v_gugun.equals("") || !(v_orgcode.equals("ALL") || v_orgcode.equals("ALL")) ) ) { 
                      sbSQL.append("                          AND     c.userid        = i.userid(+)                                                       \n");
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
                          sbSQL.append("          AND TO_CHAR(TO_DATE(d.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') \n");
                      } else {
                          sbSQL.append("          AND TO_CHAR(TO_DATE(d.indate, 'yyyymmddhh24miss'), 'yyyymmdd') BETWEEN TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') AND TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_ldate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') \n");
                      }
                  }
                  
                  sbSQL.append(" ) ORDER BY courseyear, coursecode, courseseq, rank, name                    \n");
                  
                  
                  System.out.println("sql : " + sbSQL.toString());
                      
                  ls          = connMgr.executeQuery(sbSQL.toString());
                  
                  while (ls.next()) {
                      dbox   = ls.getDataBox();
                      
                      dbox.put("d_wonlinetest"       , new Double(ls.getDouble("wonlinetest"))   );
                      dbox.put("d_wofflinetest"      , new Double(ls.getDouble("wofflinetest"))  );
                      dbox.put("d_wportfolio"        , new Double(ls.getDouble("wportfolio"))    );
                      dbox.put("d_wvalue"            , new Double(ls.getDouble("wvalue"))        );
                      dbox.put("d_portfolio"         , new Double(ls.getDouble("portfolio"))     );
                      dbox.put("d_valuation"         , new Double(ls.getDouble("valuation"))     );
                      dbox.put("d_addpointvalue"     , new Double(ls.getDouble("addpointvalue")) );
                      dbox.put("d_addpointvalue1"    , new Double(ls.getDouble("addpointvalue1")));
                      dbox.put("d_addpointvalue2"    , new Double(ls.getDouble("addpointvalue2")));
                      dbox.put("d_online_score"      , new Double(ls.getDouble("online_score"))  );
                      dbox.put("d_offline_score"     , new Double(ls.getDouble("offline_score")) );
                      dbox.put("d_total"             , new Double(ls.getDouble("total"))         );
                      
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
        분야별 교육 실적
        @param box      receive from the form object and session
        @return ArrayList
        */
         public ArrayList perform_03_01_06(RequestBox box) throws Exception { 
             DBConnectionManager connMgr         = null;
             ListSet             ls              = null;
             ArrayList           list            = null;
             StringBuffer        sbSQL           = new StringBuffer("");
             
             DataBox             dbox            = null;
             
             String              v_Bcourse       = ""; // 이전코스
             String              v_course        = ""; // 현재코스
             String              v_Bcourseseq    = ""; // 이전코스기수
             String              v_courseseq     = ""; // 현재코스기수
             
             String              ss_grcode       = box.getStringDefault  ( "s_grcode"        , "ALL" );  // 교육그룹
             String              ss_blcourseyear = box.getStringDefault  ( "s_blcourseyear"  , "ALL" );  // 년도
             String              ss_blcourseseq  = box.getStringDefault  ( "s_blcourseseq"   , "ALL" );  // 과목 기수
             String              ss_blcourse     = box.getStringDefault  ( "s_blcourse"      , "ALL" );  // 과목&코스
             String              ss_branch       = box.getStringDefault  ( "p_branch"        , "ALL" );
             String              ss_action       = box.getString         ( "s_action"                );
             String              v_orgcode       = box.getStringDefault  ( "p_orgcode"       , "ALL" );  // 조직코드
             String              c_sido          = box.getString         ( "c_sido"                  );
             String              c_gugun         = box.getString         ( "c_gugun"                 );
             String              c_gender        = box.getString         ( "c_gender"                );
             String              c_achievement   = box.getString         ( "c_achievement"           );
             String              c_age           = box.getString         ( "c_age"                   );
             String              c_gubun           = box.getString       ( "c_gubun"                 );
             
             String              v_iscomplete    = box.getString         ( "p_iscomplete"            );
             
             String              v_orderColumn   = box.getString( "p_orderColumn"  );  // 정렬할 컬럼명
             String              v_orderType     = box.getString( "p_orderType"    );  // 정렬할 순서
                  
             try {
                 connMgr     = new DBConnectionManager();            
                 list        = new ArrayList();
                
                sbSQL.append("  select  a.coursenm                                                                                                                                                                              \n")                                                                                                                                                                                                  
                      .append("         ,   b.coursecode                                                                                                                                                                         \n")
                      .append("         ,   b.courseyear                                                                                                                                                                         \n")
                      .append("         ,   b.courseseq                                                                                                                                                                          \n")
                      .append("         ,   d.userid                                                                                                                                                                             \n")
                      .append("         ,   d.name                                                                                                                                                                               \n")
                      .append("         ,   e.branchnm                                                                                                                                                                           \n")
                      .append("         ,   c.status                                                                                                                                                                             \n")
                      .append("         ,   b.subjnumforcomplete                                                                                                                                                                 \n")
                      .append("         ,   b.wonlinetest                                                                                                                                                                        \n")
                      .append("         ,   b.wofflinetest                                                                                                                                                                       \n")
                      .append("         ,   b.wportfolio                                                                                                                                                                         \n")
                      .append("         ,   b.wvalue                                                                                                                                                                             \n")
                      .append("         ,   c.portfolio                                                                                                                                                                          \n")
                      .append("         ,   c.valuation                                                                                                                                                                          \n")
                      .append("         ,   c.addpointvalue                                                                                                                                                                      \n")
                      .append("         ,   c.addpointvalue1                                                                                                                                                                     \n")
                      .append("         ,   c.addpointvalue2                                                                                                                                                                     \n")
                      .append("         ,   c.isgraduated                                                                                                                                                                        \n")
                      .append("         ,   nvl(online_score, 0)            online_score                                                                                                                                         \n")
                      .append("         ,   nvl(online_iscomplete_y, 0)     online_iscomplete_y                                                                                                                                  \n")
                      .append("         ,   nvl(online_iscomplete_n, 0)     online_iscomplete_n                                                                                                                                  \n")
                      .append("         ,   nvl(offline_score, 0)           offline_score                                                                                                                                        \n")
                      .append("         ,   nvl(offline_iscomplete_y, 0)    offline_iscomplete_y                                                                                                                                 \n")
                      .append("         ,   nvl(offline_iscomplete_n, 0)    offline_iscomplete_n                                                                                                                                 \n")
                      .append("         ,   online_subjnum                                                                                                                                                                       \n")
                      .append("         ,   offline_subjnum                                                                                                                                                                      \n")
                      .append("         ,       round((nvl(online_score, 0) * b.wonlinetest/100)                                                                                                                                 \n")
                      .append("             +   (nvl(offline_score, 0) * b.wofflinetest/100)                                                                                                                                     \n")
                      .append("             +   (nvl(c.portfolio, 0) * b.wportfolio/100)                                                                                                                                         \n")
                      .append("             +   (nvl(c.valuation, 0) * b.wvalue/100)                                                                                                                                             \n")
                      .append("             +   nvl(addpointvalue, 0)                                                                                                                                                            \n")
                      .append("             +   nvl(addpointvalue1, 0)                                                                                                                                                           \n")
                      .append("             +   nvl(addpointvalue2, 0), 1)                          total                                                                                                                        \n")
                      .append("         ,   case when (b.subjnumforcomplete <= (online_iscomplete_y + offline_iscomplete_y)) then 'Y' else 'N' end gubun                                                                         \n")                                                                          
                      .append("         ,   case when (b.subjnumforcomplete <= (online_iscomplete_y + offline_iscomplete_y)) then rank() over (order by round((nvl(online_score, 0) * b.wonlinetest/100)                         \n")
                      .append("                                                                                                                         + (nvl(offline_score, 0) * b.wofflinetest/100)                           \n")
                      .append("                                                                                                                         + (nvl(c.portfolio, 0) * b.wportfolio)                                   \n")
                      .append("                                                                                                                         + (nvl(c.valuation, 0) * b.wvalue)                                       \n")
                      .append("                                                                                                                         + nvl(addpointvalue, 0)                                                  \n")
                      .append("                                                                                                                         + nvl(addpointvalue1, 0)                                                 \n")
                      .append("                                                                                                                         + nvl(addpointvalue2, 0), 1) desc ) else 0 end rank                      \n")
                      .append("     from    tz_bl_course    a                                                                                                                                                                    \n")
                      .append("         ,   tz_blcourseseq  b                                                                                                                                                                    \n")
                      .append("         ,   tz_blstudent    c                                                                                                                                                                    \n")
                      .append("         ,   tz_member       d                                                                                                                                                                    \n")
                      .append("         ,   tz_branch       e                                                                                                                                                                    \n")
                      .append("         ,   (                                                                                                                                                                                    \n")
                      .append("                 select  avg(nvl(score, 0)) online_score                                                                                                                                          \n")
                      .append("                     ,   userid                                                                                                                                                                   \n")
                      .append("                     ,   sum(decode(isgraduated, 'Y', 1, 0)) online_iscomplete_y                                                                                                                  \n")
                      .append("                     ,   sum(decode(isgraduated, 'N', 1, 0)) online_iscomplete_n                                                                                                                  \n")
                      .append("                 from                                                                                                                                                                             \n")
                      .append("                 (                                                                                                                                                                                \n")
                      .append("                     select  d.subj                                                                                                                                                               \n")
                      .append("                         ,   max(score) score                                                                                                                                                     \n")
                      .append("                         ,   max(d.isgraduated)  isgraduated                                                                                                                                      \n")
                      .append("                         ,   max(c.userid)       userid                                                                                                                                           \n")
                      .append("                     from    tz_blstudent        a                                                                                                                                                \n")
                      .append("                         ,   tz_blcourseseq      b                                                                                                                                                \n")
                      .append("                         ,   tz_blproposesubj    c                                                                                                                                                \n")
                      .append("                         ,   tz_student            d                                                                                                                                                \n")
                      .append("                     where   a.coursecode = b.coursecode                                                                                                                                          \n")
                      .append("                     and     a.courseyear = b.courseyear                                                                                                                                          \n")
                      .append("                     and     a.courseseq = b.courseseq                                                                                                                                            \n")
                      .append("                     and     c.subj = d.subj                                                                                                                                                      \n")
                      .append("                     and     c.userid = d.userid                                                                                                                                                  \n")
                      .append("                     and     c.status = 'RV'                                                                                                                                                      \n")
                      .append("                     and     c.isrequired = 'Y'                                                                                                                                                   \n")
                      .append("                     and     add_months(to_date(d.eduend, 'YYYYMMDD'), b.availableperiod * 12) >= to_date(b.onlinepropstart)                                                                      \n")
                      .append("                     group by d.subj                                                                                                                                                              \n")
                      .append("              union                                                                                                                                                                               \n")
                      .append("              select d.subj                                                                                                                                                                       \n")
                      .append("                 ,   d.score                                                                                                                                                                      \n")
                      .append("                 ,   d.isgraduated                                                                                                                                                                \n")
                      .append("                 ,   a.userid                                                                                                                                                                     \n")
                      .append("              from   tz_blstudent        a                                                                                                                                                        \n")
                      .append("                 ,   tz_blcourseseq      b                                                                                                                                                        \n")
                      .append("                 ,   tz_blproposesubj    c                                                                                                                                                        \n")
                      .append("                 ,   tz_student            d                                                                                                                                                        \n")
                      .append("              where  a.coursecode    = b.coursecode                                                                                                                                               \n")
                      .append("              and    a.courseyear    = b.courseyear                                                                                                                                               \n")
                      .append("              and    a.courseseq     = b.courseseq                                                                                                                                                \n")
                      .append("              and    c.subj          = d.subj                                                                                                                                                     \n")
                      .append("              and    c.userid        = d.userid                                                                                                                                                   \n")
                      .append("              and    c.year          = d.year                                                                                                                                                     \n")
                      .append("              and    c.subjseq       = d.subjseq                                                                                                                                                  \n")
                      .append("              and    (c.status = 'RE' or c.status = 'FE')                                                                                                                                         \n")
                      .append("              and    c.isrequired = 'Y'                                                                                                                                                           \n")
                      .append("          )                                                                                                                                                                                       \n")
                      .append("          group by userid                                                                                                                                                                         \n")
                      .append("     )       f                                                                                                                                                                                    \n")
                      .append(" ,   (                                                                                                                                                                                            \n")
                      .append("         select  avg(nvl(score, 0)) offline_score                                                                                                                                                 \n")
                      .append("             ,   userid                                                                                                                                                                           \n")
                      .append("             ,   sum(decode(isgraduated, 'Y', 1, 0)) offline_iscomplete_y                                                                                                                         \n")
                      .append("             ,   sum(decode(isgraduated, 'N', 1, 0)) offline_iscomplete_n                                                                                                                         \n")
                      .append("          from   (                                                                                                                                                                                \n")
                      .append("                     select  d.subj                                                                                                                                                               \n")
                      .append("                         ,   d.score                                                                                                                                                              \n")
                      .append("                         ,   d.isgraduated                                                                                                                                                        \n")
                      .append("                         ,   a.userid                                                                                                                                                             \n")
                      .append("                     from    tz_blstudent        a                                                                                                                                                \n")
                      .append("                         ,   tz_blcourseseq      b                                                                                                                                                \n")
                      .append("                         ,   tz_blcourseseqsubj  c                                                                                                                                                \n")
                      .append("                         ,   tz_student            d                                                                                                                                                \n")
                      .append("                     where   a.coursecode    = b.coursecode                                                                                                                                       \n")
                      .append("                     and     a.courseyear    = b.courseyear                                                                                                                                       \n")
                      .append("                     and     a.courseseq     = b.courseseq                                                                                                                                        \n")
                      .append("                     and     c.subj          = d.subj                                                                                                                                             \n")
                      .append("                     and     a.userid        = d.userid                                                                                                                                           \n")
                      .append("                     and     c.year          = d.year                                                                                                                                             \n")
                      .append("                     and     c.subjseq       = d.subjseq                                                                                                                                          \n")
                      .append("                     and     isonoff         = 'OFF'                                                                                                                                              \n")
                      .append("                 )                                                                                                                                                                                \n")
                      .append("         group by userid                                                                                                                                                                          \n")
                      .append("     )       g                                                                                                                                                                                    \n")
                      .append(" ,   (                                                                                                                                                                                            \n")
                      .append("         select  coursecode                                                                                                                                                                       \n")
                      .append("             ,   courseyear                                                                                                                                                                       \n")
                      .append("             ,   courseseq                                                                                                                                                                        \n")
                      .append("             ,   sum(decode(isonoff, 'ON', 1, 0))    online_subjnum                                                                                                                               \n")
                      .append("             ,   sum(decode(isonoff, 'OFF', 1, 0))   offline_subjnum                                                                                                                              \n")
                      .append("          from tz_blcourseseqsubj                                                                                                                                                                 \n")
                      .append("          where (isrequired = 'Y' or isrequired is null)                                                                                                                                          \n")
                      .append("          group by coursecode, courseyear, courseseq                                                                                                                                              \n")
                      .append("     )       h                                                                                                                                                                                    \n")
                      .append("  ,  vz_orgmember     i                                                                                                                                                                           \n")
                      .append(" where   a.coursecode    = b.coursecode                                                                                                                                                           \n")
                      .append(" and     b.coursecode    = c.coursecode                                                                                                                                                           \n")
                      .append(" and     b.courseyear    = c.courseyear                                                                                                                                                           \n")
                      .append(" and     b.courseseq     = c.courseseq                                                                                                                                                            \n")
                      .append(" and     c.userid        = d.userid                                                                                                                                                               \n")
                      .append(" and     c.branchcode    = e.branchcode                                                                                                                                                           \n")
                      .append(" and     c.userid        = f.userid(+)                                                                                                                                                            \n")
                      .append(" and     c.userid        = g.userid(+)                                                                                                                                                            \n")
                      .append(" and     b.coursecode    = h.coursecode                                                                                                                                                           \n")
                      .append(" and     b.courseyear    = h.courseyear                                                                                                                                                           \n")
                      .append(" and     b.courseseq     = h.courseseq                                                                                                                                                            \n")
                      .append(" and     b.grcode        = " + StringManager.makeSQL(ss_grcode       ) + "                                                                                                                        \n")
                      .append(" and     b.courseyear    = " + StringManager.makeSQL(ss_blcourseyear ) + "                                                                                                                        \n")
                      .append(" and     c.userid        = i.userid(+)                                                                                                                                                            \n");

                 if ( !ss_blcourse.equals("ALL") ) { 
                     sbSQL.append(" and b.coursecode = " + StringManager.makeSQL(ss_blcourse     ) + "       \n");
                 }
                 
                 if ( !ss_blcourseseq.equals("ALL") ) { 
                     sbSQL.append(" and b.courseseq = " + StringManager.makeSQL(ss_blcourseseq   ) + "       \n");
                 }
                 
                 if( !ss_branch.equals("ALL")) {
                     sbSQL.append(" and c.branchcode = " + StringManager.makeSQL(ss_branch       ) + "       \n");
                 }
                 
                 if ( !v_iscomplete.equals("") ) {
                     sbSQL.append(" and c.isgraduated = " + StringManager.makeSQL(v_iscomplete) + "         \n");
                 } else {
                     sbSQL.append(" and c.status in ('G', 'I', 'J', 'K', 'L')                               \n");
                 }
                           
                 if ( !box.getString("p_sdate").equals("") && !box.getString("p_ldate").equals("") ) { // 기간선택
                     if ( box.getString("p_sdate").equals(box.getString("p_ldate")) ) {
                         sbSQL.append("          AND TO_CHAR(TO_DATE(i.indate, 'yyyymmddhh24miss'), 'yyyymm') = TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymm') \n");
                     } else {
                         sbSQL.append("          AND TO_CHAR(TO_DATE(i.indate, 'yyyymmddhh24miss'), 'yyyymm') BETWEEN TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymm') AND TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_ldate")) + ", 'yyyy-mm-dd'), 'yyyymm') \n");
                     }
                 }
                 
                 if ( !c_sido.equals("") ) {
                     sbSQL.append("             AND i.sido               = " + StringManager.makeSQL(c_sido          ) + "                                       \n");
                 }    

                 if ( !c_age.equals("") ) {
                     sbSQL.append("             AND i.age               = " + StringManager.makeSQL(c_age            ) + "                                       \n");               
                 }
                 
                 if ( !c_achievement.equals("") ) {
                     sbSQL.append("             AND i.achievement       = " + StringManager.makeSQL(c_achievement    ) + "                                       \n");               
                 }
                 
                 if ( !c_gender.equals("") ) {
                     sbSQL.append("             AND i.gender            = " + StringManager.makeSQL(c_gender         ) + "                                       \n");               
                 }
                 
                 if ( !c_gugun.equals("") ) {
                     sbSQL.append("             AND i.gugun             = " + StringManager.makeSQL(c_gubun          ) + "                                       \n");
                 }
                 
                 if ( !v_orgcode.equals("ALL") ) { 
                     sbSQL.append("            AND  i.orgcode           = " + v_orgcode                                + "                                       \n");
                 }
                 
                 System.out.println("sql : " + sbSQL.toString());
                     
                 ls          = connMgr.executeQuery(sbSQL.toString());
                 
                 while (ls.next()) {
                     dbox = ls.getDataBox();
                     
                     dbox.put("d_wonlinetest", new Double(ls.getDouble("wonlinetest")));
                     dbox.put("d_wofflinetest", new Double(ls.getDouble("wofflinetest")));
                     dbox.put("d_wportfolio", new Double(ls.getDouble("wportfolio")));
                     dbox.put("d_wvalue", new Double(ls.getDouble("wvalue")));
                     dbox.put("d_portfolio", new Double(ls.getDouble("portfolio")));
                     dbox.put("d_valuation", new Double(ls.getDouble("valuation")));
                     dbox.put("d_addpointvalue", new Double(ls.getDouble("addpointvalue")));
                     dbox.put("d_addpointvalue1", new Double(ls.getDouble("addpointvalue1")));
                     dbox.put("d_addpointvalue2", new Double(ls.getDouble("addpointvalue2")));
                     dbox.put("d_online_score", new Double(ls.getDouble("online_score")));
                     dbox.put("d_offline_score", new Double(ls.getDouble("offline_score")));
                     dbox.put("d_total", new Double(ls.getDouble("total")));
                     
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