// **********************************************************
//  1. 제      목: B/L 평가 등록/조회 및 수료처리
//  2. 프로그램명: BLCompleteActionBean.java
//  3. 개      요:
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 
//  7. 수      정:
// **********************************************************
package com.ziaan.course;

import java.io.File;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class BLCompleteActionBean { 
    public BLCompleteActionBean() { }

	/**
	* B/L 평가 등록/조회 및 수료처리
	*/
    public ArrayList selectCompleteActionList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;        
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        DataBox dbox = null;
        
        String ss_grcode = box.getString("s_grcode");           // 교육그룹
        String ss_blcourseyear = box.getString("s_blcourseyear");   // B/L 과정 연도
        String ss_blcourse = box.getString("s_blcourse");   // B/L 과정 
        String ss_blcourseseq = box.getString("s_blcourseseq");         // B/L 기수

        String s_gadmin = box.getSession("gadmin");
        String ss_branch = "";
        if("K2".equals(s_gadmin)) {
            ss_branch = box.getSession("branch");
        } else {
            ss_branch = box.getString("s_branch");           // 거점
        }
        
        String v_eduStatus = box.getString("p_eduStatus");
        try {
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();
			
			sql = "select a.coursenm                                                                    \n"
                + ", b.coursecode                                                                       \n"
                + ", b.courseyear                                                                       \n"
                + ", b.courseseq                                                                        \n"
                + ", d.userid                                                                           \n"
                + ", d.name                                                                             \n"
                + ", e.branchnm                                                                         \n"
                + ", c.status                                                                           \n"
                + ", b.subjnumforcomplete                                                               \n"
                + ", b.wonlinetest                                                                      \n"
                + ", b.wofflinetest                                                                     \n"
                + ", b.wportfolio                                                                       \n"
                + ", b.wvalue                                                                           \n"
                + ", c.portfolio                                                                        \n"
                + ", c.valuation                                                                        \n"
                + ", c.addpointvalue                                                                    \n"
                + ", c.addpointvalue1                                                                   \n"
                + ", c.addpointvalue2                                                                   \n"
                + ", c.isgraduated                                                                      \n"
                + ", nvl(round(online_score, 2), 0) online_score                                        \n"
                + ", nvl(online_iscomplete_y, 0) online_iscomplete_y                                    \n"
                + ", nvl(online_iscomplete_n, 0) online_iscomplete_n                                    \n"
                + ", nvl(offline_score, 0) offline_score                                                \n"
                + ", nvl(offline_iscomplete_y, 0) offline_iscomplete_y                                  \n"
                + ", nvl(offline_iscomplete_n, 0) offline_iscomplete_n                                  \n"
                + ", online_subjnum                                                                     \n"
                + ", offline_subjnum                                                                    \n"
                + ", round((nvl(online_score, 0) * b.wonlinetest/100) + (nvl(offline_score, 0) * b.wofflinetest/100) + (nvl(c.portfolio, 0) * b.wportfolio/100) + (nvl(c.valuation, 0) * b.wvalue/100) + nvl(addpointvalue, 0) + nvl(addpointvalue1, 0) + nvl(addpointvalue2, 0), 1) total  \n"
                + ", case when (b.subjnumforcomplete <= (online_iscomplete_y + offline_iscomplete_y)) then 'Y' else 'N' end gubun                                                                                                                                                   \n"
                + ", case when (b.subjnumforcomplete <= (online_iscomplete_y + offline_iscomplete_y)) then rank() over (partition by c.courseyear, c.courseseq, c.coursecode order by round((nvl(online_score, 0) * b.wonlinetest/100) + (nvl(offline_score, 0) * b.wofflinetest/100) + (nvl(c.portfolio, 0) * b.wportfolio/100) + (nvl(c.valuation, 0) * b.wvalue/100) + nvl(addpointvalue, 0) + nvl(addpointvalue1, 0) + nvl(addpointvalue2, 0), 1) desc) else 0 end rank   \n"
                + "from tz_bl_course a, tz_blcourseseq b, tz_blstudent c, tz_member d, tz_branch e, (   \n"
                + "     select courseyear, courseseq, coursecode, avg(nvl(score, 0)) online_score, userid, sum(decode(isgraduated, 'Y', 1, 0)) online_iscomplete_y, sum(decode(isgraduated, 'N', 1, 0)) online_iscomplete_n \n"
                + "     from                                                                             \n"
                + "     (                                                                                \n"
                + "         select a.courseyear, a.courseseq, a.coursecode, d.subj, max(score) score, max(d.isgraduated) isgraduated, max(c.userid) userid            \n"
                + "         from tz_blstudent a, tz_blcourseseq b, tz_blproposesubj c, tz_stold d                            \n"
                + "         where a.coursecode = b.coursecode                                                                \n"
                + "         and a.courseyear = b.courseyear                                                                  \n"
                + "         and a.courseseq = b.courseseq                                                                    \n"
                + "         and a.coursecode = c.coursecode                                                                      \n"
                + "         and a.courseyear = c.courseyear                                                                      \n"
                + "         and a.courseseq = c.courseseq                                                                        \n"
                + "         and a.userid    = c.userid                                                                                                                                        \n"
                + "         and c.subj = d.subj                                                                              \n"
                + "         and c.userid = d.userid                                                                          \n"
                + "         and c.status = 'RV'                                                                              \n"
                + "         and c.isrequired = 'Y'                                                                           \n"
                + "         and add_months(to_date(d.eduend, 'YYYYMMDD'), b.availableperiod * 12) >= to_date(b.onlinepropstart)  \n"
                + "         group by a.courseyear, a.courseseq, a.coursecode, d.subj                                                                                      \n"
                + "         union                                                                                                \n"
                + "         select a.courseyear, a.courseseq, a.coursecode, d.subj, d.score, d.isgraduated, a.userid             \n"
                + "         from tz_blstudent a, tz_blcourseseq b, tz_blproposesubj c, tz_stold d                                \n"
                + "         where a.coursecode = b.coursecode                                                                    \n"
                + "         and a.courseyear = b.courseyear                                                                      \n"
                + "         and a.courseseq = b.courseseq                                                                        \n"
                + "         and a.coursecode = c.coursecode                                                                      \n"
                + "         and a.courseyear = c.courseyear                                                                      \n"
                + "         and a.courseseq = c.courseseq                                                                        \n"
                + "         and a.userid    = c.userid                                                                                                                                        \n"
                + "         and c.subj = d.subj                                                                                  \n"
                + "         and c.userid = d.userid                                                                              \n"
                + "         and c.year = d.year                                                                                  \n"
                + "         and c.subjseq = d.subjseq                                                                            \n"
                + "         and (c.status = 'RE' or c.status = 'FE')                                                             \n"
                + "         and c.isrequired = 'Y'                                                                               \n"
                + "     )                                                                                                      \n"
                + "     group by courseyear, courseseq, coursecode, userid                                                                                          \n"
                + ") f, (                                                                                                       \n"
                + "     select coursecode, courseseq, courseyear, ROUND(avg(nvl(score, 0)), 2) offline_score, userid, sum(decode(isgraduated, 'Y', 1, 0)) offline_iscomplete_y, sum(decode(isgraduated, 'N', 1, 0)) offline_iscomplete_n      \n"
                + "     from                                                                                                     \n"
                + "     (                                                                                                        \n" 
                + "         select a.courseseq, a.courseyear, a.coursecode, d.subj, d.score, d.isgraduated, a.userid                                                      \n"
                + "         from tz_blstudent a, tz_blcourseseq b, tz_blcourseseqsubj c, tz_student d                            \n"
                + "         where a.coursecode = b.coursecode                                                                    \n"
                + "         and a.courseyear = b.courseyear                                                                      \n"
                + "         and a.courseseq = b.courseseq                                                                        \n"
                + "         and a.coursecode = c.coursecode                                                                    \n"
                + "         and a.courseyear = c.courseyear                                                                      \n"
                + "         and a.courseseq = c.courseseq                                                                        \n"
                + "         and c.subj = d.subj                                                                                  \n"
                + "         and a.userid = d.userid                                                                              \n"
                + "         and c.year = d.year                                                                                  \n"
                + "         and c.subjseq = d.subjseq                                                                            \n"
                + "         and isonoff = 'OFF'                                                                                  \n"
                + "     )                                                                                                        \n"
                + "     group by courseyear, courseseq, coursecode, userid                                                                                          \n"
                + ") g, (                                                                                                       \n"
                + "     select coursecode, courseyear, courseseq, sum(decode(isonoff, 'ON', 1, 0)) online_subjnum , sum(decode(isonoff, 'OFF', 1, 0)) offline_subjnum        \n"
                + "     from tz_blcourseseqsubj                                                                                  \n"
                + "     where (isrequired = 'Y' or isrequired is null)                                                           \n"
                + "     group by coursecode, courseyear, courseseq                                                               \n"
                + ") h                                                                                                          \n"
                + "where a.coursecode = b.coursecode                                                                            \n"
                + "and b.coursecode = c.coursecode                                                                              \n"
                + "and b.courseyear = c.courseyear                                                                              \n"
                + "and b.courseseq = c.courseseq                                                                                \n"
                + "and c.userid = d.userid                                                                                      \n"
                + "and c.branchcode = e.branchcode                                                                              \n"
                + "and c.userid = f.userid(+)                                                                                   \n"
                + "and c.coursecode = f.coursecode(+)                                                                                   \n"
                + "and c.courseyear = f.courseyear(+)                                                                                   \n"
                + "and c.courseseq = f.courseseq(+)                                                                                   \n"
                + "and c.coursecode = g.coursecode(+)                                                                              \n"
                + "and c.courseyear = g.courseyear(+)                                                                              \n"
                + "and c.courseseq = g.courseseq(+)                                                                                \n"
                + "and c.userid = g.userid(+)                                                                                   \n"
                + "and b.coursecode = h.coursecode                                                                              \n"
                + "and b.courseyear = h.courseyear                                                                              \n"
                + "and b.courseseq = h.courseseq                                                                                \n"
                + "and b.grcode = " + StringManager.makeSQL(ss_grcode) + "          \n"
                + "and b.courseyear = " + StringManager.makeSQL(ss_blcourseyear) + "  \n";
                
                if ( !ss_blcourse.equals("ALL") ) { 
                    sql += "and b.coursecode = " + StringManager.makeSQL(ss_blcourse) + "  \n";
                }
                
                if ( !ss_blcourseseq.equals("ALL") ) { 
                    sql += "and b.courseseq = " + StringManager.makeSQL(ss_blcourseseq) + "      \n";
                }
                
                if( !ss_branch.equals("ALL")) {
                    sql += "and c.branchcode = " + StringManager.makeSQL(ss_branch) + "      \n";
                }
                
                if( !v_eduStatus.equals("ALL")) {
                    sql += "and c.status = " + StringManager.makeSQL(v_eduStatus) + "         \n";
                } else {
                    sql += "and c.status in ('G', 'I', 'J', 'K', 'L')                         \n";
                }
                
            System.out.println("============" + sql);    
                
            ls          = connMgr.executeQuery(sql);
            
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
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }        //      꼭 닫아준다
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
    
    
    /**
    * B/L 평가 등록/조회 및 수료처리
    */
    public DataBox selectStatusCompleteActionList(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt           = null;        
        ListSet             ls              = null;
        ArrayList           list            = null;
        String              sql             = "";
        DataBox             dbox            = null;
        
        String              v_grcode        = box.getStringDefault("p_grcode", "N000001");   // 교육그룹
        String              v_courseyear    = box.getString("p_courseyear");   // B/L 과정 연도
        String              v_coursecode    = box.getString("p_coursecode");   // B/L 과정 
        String              v_courseseq     = box.getString("p_courseseq" );   // B/L 기수
        String              v_userid        = box.getString("p_userid");
        
        try {
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();
            
            sql = "select a.coursenm                                                                    \n"
                + ", b.coursecode                                                                       \n"
                + ", b.courseyear                                                                       \n"
                + ", b.courseseq                                                                        \n"
                + ", min(b.subjnumforcomplete) over()   subjnumforcomplete                         \n"                
                + ", d.userid                                                                           \n"
                + ", d.name                                                                             \n"
                + ", e.branchnm                                                                         \n"
                + ", c.status                                                                           \n"
                + ", b.subjnumforcomplete                                                               \n"
                + ", b.wonlinetest                                                                      \n"
                + ", b.wofflinetest                                                                     \n"
                + ", b.wportfolio                                                                       \n"
                + ", b.wvalue                                                                           \n"
                + ", c.portfolio                                                                        \n"
                + ", c.valuation                                                                        \n"
                + ", c.addpointvalue                                                                    \n"
                + ", c.addpointvalue1                                                                   \n"
                + ", c.addpointvalue2                                                                   \n"
                + ", c.isgraduated                                                                      \n"
                + ", nvl(round(online_score, 2), 0) online_score                                        \n"
                + ", nvl(online_iscomplete_y, 0) online_iscomplete_y                                    \n"
                + ", nvl(online_iscomplete_n, 0) online_iscomplete_n                                    \n"
                + ", nvl(offline_score, 0) offline_score                                                \n"
                + ", nvl(offline_iscomplete_y, 0) offline_iscomplete_y                                  \n"
                + ", nvl(offline_iscomplete_n, 0) offline_iscomplete_n                                  \n"
                + ", online_subjnum                                                                     \n"
                + ", offline_subjnum                                                                    \n"
                + ", round((nvl(online_score, 0) * b.wonlinetest/100) + (nvl(offline_score, 0) * b.wofflinetest/100) + (nvl(c.portfolio, 0) * b.wportfolio/100) + (nvl(c.valuation, 0) * b.wvalue/100) + nvl(addpointvalue, 0) + nvl(addpointvalue1, 0) + nvl(addpointvalue2, 0), 1) total  \n"
                + ", case when (b.subjnumforcomplete <= (online_iscomplete_y + offline_iscomplete_y)) then 'Y' else 'N' end gubun                                                                                                                                                   \n"
                + ", case when (b.subjnumforcomplete <= (online_iscomplete_y + offline_iscomplete_y)) then rank() over (partition by c.courseyear, c.courseseq, c.coursecode order by round((nvl(online_score, 0) * b.wonlinetest/100) + (nvl(offline_score, 0) * b.wofflinetest/100) + (nvl(c.portfolio, 0) * b.wportfolio/100) + (nvl(c.valuation, 0) * b.wvalue/100) + nvl(addpointvalue, 0) + nvl(addpointvalue1, 0) + nvl(addpointvalue2, 0), 1) desc) else 0 end rank   \n"
                + "from tz_bl_course a, tz_blcourseseq b, tz_blstudent c, tz_member d, tz_branch e, (   \n"
                + "     select courseyear, courseseq, coursecode, avg(nvl(score, 0)) online_score, userid, sum(decode(isgraduated, 'Y', 1, 0)) online_iscomplete_y, sum(decode(isgraduated, 'N', 1, 0)) online_iscomplete_n \n"
                + "     from                                                                             \n"
                + "     (                                                                                \n"
                + "         select a.courseyear, a.courseseq, a.coursecode, d.subj, max(score) score, max(d.isgraduated) isgraduated, max(c.userid) userid            \n"
                + "         from tz_blstudent a, tz_blcourseseq b, tz_blproposesubj c, tz_stold d                            \n"
                + "         where a.coursecode = b.coursecode                                                                \n"
                + "         and a.courseyear = b.courseyear                                                                  \n"
                + "         and a.courseseq = b.courseseq                                                                    \n"
                + "         and a.coursecode = c.coursecode                                                                      \n"
                + "         and a.courseyear = c.courseyear                                                                      \n"
                + "         and a.courseseq = c.courseseq                                                                        \n"
                + "         and a.userid    = c.userid                                                                                                                                        \n"
                + "         and c.subj = d.subj                                                                              \n"
                + "         and c.userid = d.userid                                                                          \n"
                + "         and c.status = 'RV'                                                                              \n"
                + "         and c.isrequired = 'Y'                                                                           \n"
                + "         and add_months(to_date(d.eduend, 'YYYYMMDD'), b.availableperiod * 12) >= to_date(b.onlinepropstart)  \n"
                + "         group by a.courseyear, a.courseseq, a.coursecode, d.subj                                                                                      \n"
                + "         union                                                                                                \n"
                + "         select a.courseyear, a.courseseq, a.coursecode, d.subj, d.score, d.isgraduated, a.userid             \n"
                + "         from tz_blstudent a, tz_blcourseseq b, tz_blproposesubj c, tz_stold d                                \n"
                + "         where a.coursecode = b.coursecode                                                                    \n"
                + "         and a.courseyear = b.courseyear                                                                      \n"
                + "         and a.courseseq = b.courseseq                                                                        \n"
                + "         and a.coursecode = c.coursecode                                                                      \n"
                + "         and a.courseyear = c.courseyear                                                                      \n"
                + "         and a.courseseq = c.courseseq                                                                        \n"
                + "         and a.userid    = c.userid                                                                                                                                        \n"
                + "         and c.subj = d.subj                                                                                  \n"
                + "         and c.userid = d.userid                                                                              \n"
                + "         and c.year = d.year                                                                                  \n"
                + "         and c.subjseq = d.subjseq                                                                            \n"
                + "         and (c.status = 'RE' or c.status = 'FE')                                                             \n"
                + "         and c.isrequired = 'Y'                                                                               \n"
                + "     )                                                                                                      \n"
                + "     group by courseyear, courseseq, coursecode, userid                                                                                          \n"
                + ") f, (                                                                                                       \n"
                + "     select coursecode, courseseq, courseyear, ROUND(avg(nvl(score, 0)), 2) offline_score, userid, sum(decode(isgraduated, 'Y', 1, 0)) offline_iscomplete_y, sum(decode(isgraduated, 'N', 1, 0)) offline_iscomplete_n      \n"
                + "     from                                                                                                     \n"
                + "     (                                                                                                        \n" 
                + "         select a.courseseq, a.courseyear, a.coursecode, d.subj, d.score, d.isgraduated, a.userid                                                      \n"
                + "         from tz_blstudent a, tz_blcourseseq b, tz_blcourseseqsubj c, tz_student d                            \n"
                + "         where a.coursecode = b.coursecode                                                                    \n"
                + "         and a.courseyear = b.courseyear                                                                      \n"
                + "         and a.courseseq = b.courseseq                                                                        \n"
                + "         and a.coursecode = c.coursecode                                                                    \n"
                + "         and a.courseyear = c.courseyear                                                                      \n"
                + "         and a.courseseq = c.courseseq                                                                        \n"
                + "         and c.subj = d.subj                                                                                  \n"
                + "         and a.userid = d.userid                                                                              \n"
                + "         and c.year = d.year                                                                                  \n"
                + "         and c.subjseq = d.subjseq                                                                            \n"
                + "         and isonoff = 'OFF'                                                                                  \n"
                + "     )                                                                                                        \n"
                + "     group by courseyear, courseseq, coursecode, userid                                                                                          \n"
                + ") g, (                                                                                                       \n"
                + "     select coursecode, courseyear, courseseq, sum(decode(isonoff, 'ON', 1, 0)) online_subjnum , sum(decode(isonoff, 'OFF', 1, 0)) offline_subjnum        \n"
                + "     from tz_blcourseseqsubj                                                                                  \n"
                + "     where (isrequired = 'Y' or isrequired is null)                                                           \n"
                + "     group by coursecode, courseyear, courseseq                                                               \n"
                + ") h                                                                                                          \n"
                + "where a.coursecode = b.coursecode                                                                            \n"
                + "and b.coursecode = c.coursecode                                                                              \n"
                + "and b.courseyear = c.courseyear                                                                              \n"
                + "and b.courseseq = c.courseseq                                                                                \n"
                + "and c.userid = d.userid                                                                                      \n"
                + "and c.branchcode = e.branchcode                                                                              \n"
                + "and c.userid = f.userid(+)                                                                                   \n"
                + "and c.coursecode = f.coursecode(+)                                                                                   \n"
                + "and c.courseyear = f.courseyear(+)                                                                                   \n"
                + "and c.courseseq = f.courseseq(+)                                                                                   \n"
                + "and c.coursecode = g.coursecode(+)                                                                              \n"
                + "and c.courseyear = g.courseyear(+)                                                                              \n"
                + "and c.courseseq = g.courseseq(+)                                                                                \n"
                + "and c.userid = g.userid(+)                                                                                   \n"
                + "and b.coursecode = h.coursecode                                                                              \n"
                + "and b.courseyear = h.courseyear                                                                              \n"
                + "and b.courseseq = h.courseseq                                                                                \n"
                + "and b.grcode = " + StringManager.makeSQL(v_grcode)               + "     \n"
                + "and b.courseyear = " + StringManager.makeSQL(v_courseyear)       + "     \n";
                sql += "and b.coursecode = " + StringManager.makeSQL(v_coursecode)  + "     \n";
                sql += "and b.courseseq = " + StringManager.makeSQL(v_courseseq)    + "     \n";
                sql += "and c.status in ('G', 'I', 'J', 'K', 'L')                           \n";
                sql += "and c.userid    = " + StringManager.makeSQL(v_userid)       + "     \n";
                
            System.out.println("============" + sql);    
                
            ls          = connMgr.executeQuery(sql);
            
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
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }        //      꼭 닫아준다
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return dbox;
    }
    
    
    /**
     * B/L 과정 평가 등록
     */
      public int saveLastScore(RequestBox box) throws Exception {
         DBConnectionManager connMgr = null;
         PreparedStatement pstmt1 = null;   
         String sql1 = "";
         int isOk1 = 1;
         
         String v_coursecode = box.getString("p_coursecode");
         String v_courseyear = box.getString("p_courseyear");
         String v_courseseq = box.getString("p_courseseq");
         String v_userid = box.getString("p_userid");
         int v_index = box.getInt("p_index");
         
         double v_onlinetest = 0;
         double v_offlinetest = 0;
         double v_portfolio = 0;
         double v_valuation = 0;
         double v_addpointvalue = 0;
         double v_addpointvalue1 = 0;
         double v_addpointvalue2 = 0;
         double v_wonlinetest = 0;
         double v_wofflinetest = 0;
         double v_wportfolio = 0;
         double v_wvaluation = 0;
         try {
             connMgr = new DBConnectionManager();     
             connMgr.setAutoCommit(false);
             
             v_onlinetest = box.getDouble("p_onlinetest" + v_index);
             v_offlinetest = box.getDouble("p_offlinetest" + v_index);
             v_portfolio = box.getDouble("p_portfolio" + v_index);
             v_valuation = box.getDouble("p_valuation" + v_index);
             v_addpointvalue = box.getDouble("p_addpointvalue" + v_index);
             v_addpointvalue1 = box.getDouble("p_addpointvalue01" + v_index);
             v_addpointvalue2 = box.getDouble("p_addpointvalue02" + v_index);
             v_wonlinetest = box.getDouble("p_wonlinetest" + v_index);
             v_wofflinetest = box.getDouble("p_wofflinetest" + v_index);
             v_wportfolio = box.getDouble("p_wportfolio" + v_index);
             v_wvaluation = box.getDouble("p_wvaluation" + v_index);
             
             sql1 = "update tz_blstudent set                                    \n" +
                 "   onlinetest = ?                                             \n" +
                 ",  offlinetest = ?                                            \n" +
                 ",  portfolio  = ?                                             \n" +
                 ",  valuation   = ?                                            \n" +
                 ",  addpointvalue = ?                                          \n" + 
                 ",  addpointvalue1 = ?                                         \n" +
                 ",  addpointvalue2 = ?                                         \n" +
                 ",  wonlinetest = ?                                            \n" +
                 ",  wofflinetest = ?                                           \n" +
                 ",  wportfolio = ?                                             \n" +
                 ",  wvaluation = ?                                             \n" +
                 "where coursecode = ?                                          \n" +
                 "and courseyear = ?                                            \n" +
                 "and courseseq = ?                                             \n" + 
                 "and userid = ?                                                \n";
         
              pstmt1 = connMgr.prepareStatement(sql1);
              
              int params = 1;
              pstmt1.setDouble(params++, v_onlinetest);
              pstmt1.setDouble(params++, v_offlinetest);
              pstmt1.setDouble(params++, v_portfolio);
              pstmt1.setDouble(params++, v_valuation);
              pstmt1.setDouble(params++, v_addpointvalue);
              pstmt1.setDouble(params++, v_addpointvalue1);
              pstmt1.setDouble(params++, v_addpointvalue2);
              pstmt1.setDouble(params++, v_wonlinetest);
              pstmt1.setDouble(params++, v_wofflinetest);
              pstmt1.setDouble(params++, v_wportfolio);
              pstmt1.setDouble(params++, v_wvaluation);
              pstmt1.setString(params++, v_coursecode);
              pstmt1.setString(params++, v_courseyear);
              pstmt1.setString(params++, v_courseseq);
              pstmt1.setString(params++, v_userid);        
              isOk1 = pstmt1.executeUpdate();
              if ( pstmt1 != null ) { pstmt1.close(); }
             
             if(isOk1 > 0) {
                 if(connMgr != null) { try { connMgr.commit(); }catch (Exception e10) {} }  
             }
         }
         catch(Exception ex) {
             if(connMgr != null) { try { connMgr.rollback(); }catch (Exception e10) {} }  
             ErrorManager.getErrorStackTrace(ex, box, sql1);            
             throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
         }
         finally {
             if(connMgr != null) { try { connMgr.setAutoCommit(true); }catch (Exception e10) {} }            
             if(pstmt1 != null) { try { pstmt1.close(); } catch (Exception e) {} }
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }
         return isOk1;
     }
      
      /**
       * 교육생 수료처리 ( 일정 기준을 넘어서고 상태값이 교육생인 사람만을 대상으로 처리)
       */
        public int completeStudent(RequestBox box) throws Exception {
           DBConnectionManager connMgr = null;
           PreparedStatement pstmt1 = null;   
           String sql1 = "";
           int isOk1 = 1;
           PreparedStatement pstmt2 = null;   
           String sql2 = "";
           int isOk2 = 1;
           
           PreparedStatement pstmt3 = null;   
           String sql3 = "";
           int isOk3 = 1;
           
           Vector v_checks  = box.getVector("p_checks");
           String v_coursecode = "";
           String v_courseyear = "";
           String v_courseseq = "";
           String v_userid = "";
           String v_status = "";
           String v_gubun = "";
           String v_isgraduated = "";
           String v_orgin_status = "";
           try {
               connMgr = new DBConnectionManager();     
               connMgr.setAutoCommit(false);
               
               for(int i = 0; i < v_checks.size(); i++) {
                   String v_index = (String)v_checks.elementAt(i);
                   
                   System.out.println( " v_index        : " + v_index     + " i : " + i );
                   
                   v_coursecode = box.getString("p_coursecode" + v_index);
                   v_courseyear = box.getString("p_courseyear" + v_index);
                   v_courseseq = box.getString("p_courseseq" + v_index);
                   v_userid = box.getString("p_userid" + v_index);
                   v_gubun = box.getString("p_gubun" + v_index);
                   v_status = "Y".equals(v_gubun) ? "L" : "K";
                   v_isgraduated = "L".equals(v_status) ? "Y" : "N";
                   v_orgin_status = box.getString("p_origin_status" + v_index);
                   
                   
                   System.out.println( " v_index        : " + v_index        );
                   System.out.println( " v_coursecode   : " + v_coursecode   );
                   System.out.println( " v_courseyear   : " + v_courseyear   );
                   System.out.println( " v_courseseq    : " + v_courseseq    );
                   System.out.println( " v_userid       : " + v_userid       );
                   System.out.println( " v_gubun        : " + v_gubun        );
                   System.out.println( " v_status       : " + v_status       );
                   System.out.println( " v_isgraduated  : " + v_isgraduated  );
                   System.out.println( " v_orgin_status : " + v_orgin_status );
                   
                   if("G".equals(v_orgin_status)) {
                       sql1 = "update tz_blstudent set                             \n" +
                       "  status = ?                                               \n" +
                       ", isgraduated = ?                                          \n" +
                       "where coursecode = ?                                       \n" +
                       "and courseyear = ?                                         \n" +
                       "and courseseq = ?                                          \n" + 
                       "and userid = ?                                             \n";
                       
                   System.out.println( " ====================== 1" );
                       
                        pstmt1 = connMgr.prepareStatement(sql1);
                        
                        int params = 1;
                        pstmt1.setString(params++, v_status);
                        pstmt1.setString(params++, v_isgraduated);
                        pstmt1.setString(params++, v_coursecode);
                        pstmt1.setString(params++, v_courseyear);
                        pstmt1.setString(params++, v_courseseq);
                        pstmt1.setString(params++, v_userid);        
                        isOk1 *= pstmt1.executeUpdate();
                        if(pstmt1 != null) { try { pstmt1.close(); } catch (Exception e) {} }
                        
                        if(isOk1> 0) {
                            sql2 = "insert into tz_blstold(coursecode, courseyear, courseseq, userid, status, branchcode, exceptionreason, chargetype, ischarge, isrefund, duedate, refunddate, chargenote, addpoint1, addpoint2, ldate, luserid, addpoint3, woffonlinetest, woffinterview, woffofflinetest, woffvalue1, woffvalue2, offlineelectiondate, addpointvalue, addpointvalue1, addpointvalue2, isgraduated, onlinetest, offlinetest, portfolio, valuation, wonlinetest, wofflinetest, wportfolio, wvaluation, additionreason)                              \n" //offlineelectiontype, total,  
                            + "select coursecode, courseyear, courseseq, userid, status, branchcode, exceptionreason, chargetype, ischarge, isrefund, duedate, refunddate, chargenote, addpoint1, addpoint2, ldate, luserid, addpoint3, woffonlinetest, woffinterview, woffofflinetest, woffvalue1, woffvalue2, offlineelectiondate, addpointvalue, addpointvalue1, addpointvalue2, isgraduated, onlinetest, offlinetest, portfolio, valuation, wonlinetest, wofflinetest, wportfolio, wvaluation, additionreason                     \n" 
                            + "from tz_blstudent                                                            \n"
                            + "where coursecode = ?                                       \n"
                            + "and courseyear = ?                                         \n" 
                            + "and courseseq = ?                                          \n"  
                            + "and userid = ?                                             \n";
                            
                   System.out.println( " ====================== 2" );
                            
                   System.out.println( " ====================== 2" + sql2 );
                   
                             System.out.println(" v_coursecode  : " + v_coursecode  );
                             System.out.println(" v_courseyear  : " + v_courseyear  );
                             System.out.println(" v_courseseq   : " + v_courseseq   );
                             System.out.println(" v_userid      : " + v_userid      );        
                   
                            
                             pstmt2 = connMgr.prepareStatement(sql2);
                             
                             params = 1;
                             pstmt2.setString(params++, v_coursecode);
                             pstmt2.setString(params++, v_courseyear);
                             pstmt2.setString(params++, v_courseseq);
                             pstmt2.setString(params++, v_userid);        
                             isOk2 = pstmt2.executeUpdate();
                             if(pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
                             
                             /*
                             if(isOk2 > 0) {
                                 sql3 = "delete from tz_blstudent                            \n" +
                                 "where coursecode = ?                                       \n" +
                                 "and courseyear = ?                                         \n" +
                                 "and courseseq = ?                                          \n" + 
                                 "and userid = ?                                             \n";
                                 
                                  pstmt3 = connMgr.prepareStatement(sql3);
                                  
                                  params = 1;
                                  pstmt3.setString(params++, v_coursecode);
                                  pstmt3.setString(params++, v_courseyear);
                                  pstmt3.setString(params++, v_courseseq);
                                  pstmt3.setString(params++, v_userid);    
                                  isOk3 = pstmt3.executeUpdate();
                             }*/
                        }
                   }
               }
               
               if(isOk3 > 0) {
                   if(connMgr != null) { try { connMgr.commit(); }catch (Exception e10) {} }  
               }
           }
           catch(Exception ex) {
               if(connMgr != null) { try { connMgr.rollback(); }catch (Exception e10) {} }  
               ErrorManager.getErrorStackTrace(ex, box, sql1);            
               throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
           }
           finally {
               if(connMgr != null) { try { connMgr.setAutoCommit(true); }catch (Exception e10) {} }            
               if(pstmt1 != null) { try { pstmt1.close(); } catch (Exception e) {} }
               if(pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
               if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
           }
           return isOk1;
       }
 
        /**
         * B/L과정 개인별 오프라인 성적 상세보기
         */
         public ArrayList selectOnlineScoreByPersonList(RequestBox box) throws Exception {
             DBConnectionManager connMgr = null;
             PreparedStatement pstmt = null;        
             ListSet ls = null;
             ArrayList list = null;
             String sql = "";
             DataBox dbox = null;
             
             String ss_grcode = box.getString("s_grcode");           // 교육그룹
             String ss_blcourseyear = box.getString("s_blcourseyear");   // B/L 과정 연도
//             String ss_blcourse = box.getString("s_blcourse");   // B/L 과정 
//             String ss_blcourseseq = box.getString("s_blcourseseq");         // B/L 기수
             String ss_branch = box.getString("s_branch");           // 거점
             
             String v_coursecode    = box.getString("p_coursecode");
             String v_courseyear    = box.getString("p_courseyear");
             String v_courseseq     = box.getString("p_courseseq");
             String v_select        = box.getString("p_select");
             String v_text          = box.getString("p_text");
             
             try {
                 connMgr = new DBConnectionManager();            
                 
                 list = new ArrayList();
                 
                 sql = "select b.grcode                                                                              \n"
                     + ", a.coursenm                                                                                 \n"
                     + ", b.coursecode                                                                               \n"
                     + ", b.courseyear                                                                               \n"
                     + ", b.courseseq                                                                                \n"
                     + ", d.userid                                                                                   \n"
                     + ", MIN(MIN(SUBSTR(i.edustart, 1, 8))) over() minedustart                                          \n"
                     + ", MAX(MAX(SUBSTR(i.eduend  , 1, 8))) over() maxeduend                                            \n"
                     + ", d.name                                                                                     \n"
                     + ", e.subj                                                                                     \n"
                     + ", e.subjnm                                                                                   \n"
                     + ", c.isrequired                                                                               \n"
                     + ", DECODE(NVL(i.isclosed, 'N'), 'Y', f.isgraduated, 'N') isgraduated                          \n"
                     + ", f.score                                                                                    \n"
                     + ", f.tstep                                                                                    \n"
                     + ", f.mtest                                                                                    \n"
                     + ", f.ftest                                                                                    \n"
                     + ", f.report                                                                                   \n"
                     + ", f.act                                                                                      \n"
                     + ", f.etc1                                                                                     \n"
                     + ", f.etc2                                                                                     \n"
                     + ", i.wstep                                                                                    \n"
                     + ", i.wmtest                                                                                    \n"
                     + ", i.wftest                                                                                    \n"
                     + ", i.wreport                                                                                   \n"
                     + ", i.wact                                                                                      \n"
                     + ", i.wetc1                                                                                     \n"
                     + ", i.wetc2                                                                                     \n"
                     + ", f.avtstep                                                                                  \n"
                     + ", f.avmtest                                                                                  \n"
                     + ", f.avftest                                                                                  \n"
                     + ", f.avreport                                                                                 \n"
                     + ", f.avact                                                                                    \n"
                     + ", f.avetc1                                                                                   \n"
                     + ", f.avetc2                                                                                   \n"
                     + ", f.htest                                                                                    \n"
                     + ", f.avhtest                                                                                  \n"
                     + ", max(g.status) status                                                                                   \n"
                     + "from tz_bl_course a                                                                          \n"
                     + "     , tz_blcourseseq b                                                                      \n"
                     + "     , tz_blcourseseqsubj c                                                                  \n"
                     + "     , tz_member    d                                                                           \n"
                     + "     , tz_subj      e                                                                             \n"
                     + "     , tz_student   f                                                                            \n"
                     + "     , tz_blstudent g                                                                        \n"
                     + "     , tz_subjseq   i                                                                        \n"
                     + "where a.coursecode = b.coursecode                                                            \n"
                     + "and b.coursecode = c.coursecode                                                              \n"
                     + "and b.courseyear = c.courseyear                                                              \n"
                     + "and b.courseseq = c.courseseq                                                                \n"
                     + "and c.isonoff = 'OFF'                                                                        \n"
                     + "and c.subj = e.subj                                                                          \n"
                     + "and c.subj = f.subj(+)                                                                       \n"
                     + "and b.coursecode = g.coursecode                                                              \n"
                     + "and b.courseyear = g.courseyear                                                              \n"
                     + "and b.courseseq = g.courseseq                                                                \n"
                     + "and c.year      = i.year   (+)                                                              \n"
                     + "and c.subj      = i.subj   (+)                                                              \n"
                     + "and c.subjseq   = i.subjseq(+)                                                               \n"
                     + "and d.userid    = g.userid                                                                   \n"
//                     + "and g.status in ('G', 'I', 'J', 'K', 'L')                                                  \n"                     
                     + "and c.subj = f.subj(+)                                                                       \n"
                     + "and c.year = f.year(+)                                                                       \n"
                     + "and c.subjseq = f.subjseq(+)                                                                 \n"
                     + "and b.grcode = " + StringManager.makeSQL(ss_grcode) + "          \n"
                     + "and b.courseyear = " + StringManager.makeSQL(v_courseyear) + "  \n"
                     + "and d.userid = " + StringManager.makeSQL(v_text) + "     \n"
                     + "and f.userid(+) = " + StringManager.makeSQL(v_text) + "  \n";
                 
                 if ( !( v_coursecode.equals("") || v_coursecode.equals("ALL") ) ) { 
                     sql += "and b.coursecode = " + StringManager.makeSQL(v_coursecode) + "  \n";
                 }
                 
                 if ( !( v_courseyear.equals("") || v_courseyear.equals("ALL") ) ) { 
                     sql += "and b.courseyear = " + StringManager.makeSQL(v_courseyear) + "  \n";
                 }
                 
                 if ( !( v_courseseq.equals("") || v_courseseq.equals("ALL") ) ) { 
                     sql += "and b.courseseq = " + StringManager.makeSQL(v_courseseq) + "      \n";
                 }
                 
                 if( !( ss_branch.equals("") || ss_branch.equals("ALL") ) ) {
                     sql += "and g.branchcode = " + StringManager.makeSQL(ss_branch) + "      \n";
                 }
                 
                     sql +="group by b.grcode                                                                            \n"
                         + "     , b.coursecode                                                                          \n"
                         + "     , b.courseyear                                                                          \n"
                         + "     , b.courseseq                                                                           \n"
                         + "     , a.coursenm                                                                            \n"
                         + "     , d.userid                                                                              \n"
                         + "     , d.name                                                                                \n"
                         + "     , e.subj                                                                                \n"
                         + "     , e.subjnm                                                                              \n"
                         + "     , c.isrequired                                                                          \n"
                         + "     , f.isgraduated                                                                         \n"
                         + "     , f.score                                                                               \n"
                         + "     , f.tstep                                                                               \n"
                         + "     , f.mtest                                                                               \n"
                         + "     , f.ftest                                                                               \n"
                         + "     , f.report                                                                              \n"
                         + "     , f.act                                                                                 \n"
                         + "     , f.etc1                                                                                \n"
                         + "     , f.etc2                                                                                \n"
                         + "     , i.wstep                                                                               \n"
                         + "     , i.wmtest                                                                               \n"
                         + "     , i.wftest                                                                               \n"
                         + "     , i.wreport                                                                              \n"
                         + "     , i.wact                                                                                 \n"
                         + "     , i.wetc1                                                                                \n"
                         + "     , i.wetc2                                                                                \n"
                         + "     , f.avtstep                                                                             \n"
                         + "     , f.avmtest                                                                             \n"
                         + "     , f.avftest                                                                             \n"
                         + "     , f.avreport                                                                            \n"
                         + "     , f.avact                                                                               \n"
                         + "     , f.avetc1                                                                              \n"
                         + "     , f.avetc2                                                                              \n"
                         + "     , f.htest                                                                               \n"
                         + "     , f.avhtest                                                                             \n"
                         + "     , i.isclosed                                                                            \n"
                         //+ "     , g.status                                                                              \n"
                         + "order by name, isrequired desc                                                               \n";
                     
                 System.out.println("===========sql : " + sql);        
                     
                 ls          = connMgr.executeQuery(sql);
                 
                 while (ls.next()) {
                     dbox = ls.getDataBox();
                     dbox.put("d_score", new Double(ls.getDouble("score")));
                     dbox.put("d_tstep", new Double(ls.getDouble("tstep")));
                     
                     dbox.put("d_mtest", new Double(ls.getDouble("mtest")));
                     dbox.put("d_ftest", new Double(ls.getDouble("ftest")));
                     dbox.put("d_report", new Double(ls.getDouble("report")));
                     dbox.put("d_act", new Double(ls.getDouble("act")));
                     dbox.put("d_etc1", new Double(ls.getDouble("etc1")));
                     dbox.put("d_etc2", new Double(ls.getDouble("etc2")));
                     dbox.put("d_wstep", new Double(ls.getDouble("wstep")));
                     dbox.put("d_wmtest", new Double(ls.getDouble("wmtest")));
                     dbox.put("d_wftest", new Double(ls.getDouble("wftest")));
                     dbox.put("d_wreport", new Double(ls.getDouble("wreport")));
                     dbox.put("d_wact", new Double(ls.getDouble("wact")));
                     dbox.put("d_wetc1", new Double(ls.getDouble("wetc1")));
                     dbox.put("d_wetc2", new Double(ls.getDouble("wetc2")));
                     dbox.put("d_avtstep", new Double(ls.getDouble("avtstep")));
                     dbox.put("d_avmtest", new Double(ls.getDouble("avmtest")));
                     dbox.put("d_avftest", new Double(ls.getDouble("avftest")));
                     dbox.put("d_avreport", new Double(ls.getDouble("avreport")));
                    
                     dbox.put("d_avact", new Double(ls.getDouble("avact")));
                     dbox.put("d_avetc1", new Double(ls.getDouble("avetc1")));
                     dbox.put("d_avetc2", new Double(ls.getDouble("avetc2")));
                     dbox.put("d_htest", new Double(ls.getDouble("htest")));
                     dbox.put("d_avhtest", new Double(ls.getDouble("avhtest")));
                     list.add(dbox);
                 }
             }
             catch (Exception ex) {
                 ErrorManager.getErrorStackTrace(ex, box, sql);
                 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
             }
             finally {
                 if(ls != null) { try { ls.close(); }catch (Exception e) {} }
                 if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }        //      꼭 닫아준다
                 if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
             }
             return list;
         }
         
         /**
          * B/L과정 개인별 온라인 성적 상세보기 rowspan 카운트 
          */
          public ArrayList selectRowspanCount(RequestBox box) throws Exception {
              DBConnectionManager connMgr = null;
              PreparedStatement pstmt = null;        
              ListSet ls = null;
              ArrayList list = null;
              String sql = "";
              DataBox dbox = null;
              
              String ss_grcode = box.getString("s_grcode");           // 교육그룹
              String ss_blcourseyear = box.getString("s_blcourseyear");   // B/L 과정 연도
              
              String v_coursecode = box.getString("p_coursecode");
              String v_courseseq = box.getString("p_courseseq");
              String v_courseyear = box.getString("p_courseyear");
              String v_text = box.getString("p_text");
              
              try {
                  connMgr = new DBConnectionManager();            
                  
                  list = new ArrayList();
                  
                  sql = "select c.userid, count(*) count                                 \n"
                      + "from tz_blcourseseqsubj a, tz_blcourseseq b, tz_member c          \n"
                      + "where a.coursecode = b.coursecode                               \n"
                      + "and a.courseyear = b.courseyear                                 \n"
                      + "and a.courseseq = b.courseseq                                   \n"
                      + "and a.isonoff = 'OFF'                                           \n"
                      + "and b.grcode = " + StringManager.makeSQL(ss_grcode) + "         \n"
                      + "and b.courseyear = " + StringManager.makeSQL(v_courseyear) + " \n"
                      + "and b.coursecode = " + StringManager.makeSQL(v_coursecode) +  " \n"
                      + "and b.courseseq = " + StringManager.makeSQL(v_courseseq) +  "   \n"
                      + "and c.userid = " + StringManager.makeSQL(v_text) +  "           \n"
                      + "group by c.userid, c.name                                       \n"
                      + "order by c.name                                                 \n";
                      
                  ls          = connMgr.executeQuery(sql);
                  
                  while (ls.next()) {
                      dbox = ls.getDataBox();
                      list.add(dbox);
                  }
              }
              catch (Exception ex) {
                  ErrorManager.getErrorStackTrace(ex, box, sql);
                  throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
              }
              finally {
                  if(ls != null) { try { ls.close(); }catch (Exception e) {} }
                  if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }        //      꼭 닫아준다
                  if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
              }
              return list;
          }

        /**
         * B/L 평가 등록/조회 및 수료처리 Excel Upload
         */
           public int ListPageExcelUpload(RequestBox box) throws Exception {
               DBConnectionManager connMgr = null;
               PreparedStatement pstmt1 = null;  

               String sql = "";
               int i = 0;
               int isOk1 = 1;

               ConfigSet conf = new ConfigSet();
               
               String  v_newFileName  = box.getNewFileName("p_file");
               
               String v_coursecode = "";
               String v_courseyear = "";
               String v_courseseq = "";
               String v_coursenm = "";
               String v_courseseq1 = "";
               String v_userid = "";
               String v_name = "";
               String v_branch = "";
               String v_onlinetest = "";
               String v_offlinetest = "";
               String v_portfolio = "";
               String v_valuation = "";
               String v_addpointvalue = "";
               String v_addpointvalue1 = "";
               String v_addpointvalue2 = "";
               
               String v_wonlinetest = "";
               String v_wofflinetest = "";
               String v_wportfolio = "";
               String v_wvaluation = "";
               
               Cell cell = null;
               Sheet sheet = null;
               Workbook workbook = null;
               
               try 
               {
                   workbook = Workbook.getWorkbook(new File(conf.getProperty("dir.upload.blcompleteaction")+v_newFileName));
                   sheet = workbook.getSheet(0);

                   connMgr = new DBConnectionManager();     
                   connMgr.setAutoCommit(false);
                   
                   for (i=1; i < sheet.getRows() ; i++ ) {
                       v_coursecode = StringManager.trim( sheet.getCell(0,i).getContents());
                       v_courseyear = StringManager.trim( sheet.getCell(1,i).getContents());
                       v_courseseq = StringManager.trim( sheet.getCell(2,i).getContents());
                       v_coursenm = StringManager.trim( sheet.getCell(3,i).getContents());
                       v_courseseq1 = StringManager.trim( sheet.getCell(4,i).getContents());
                       v_userid = StringManager.trim( sheet.getCell(5,i).getContents());
                       v_name = StringManager.trim( sheet.getCell(6,i).getContents());
                       v_branch = StringManager.trim( sheet.getCell(7,i).getContents());
                       v_onlinetest = StringManager.trim( sheet.getCell(8,i).getContents());
                       v_offlinetest = StringManager.trim( sheet.getCell(9,i).getContents());
                       v_wonlinetest = StringManager.trim( sheet.getCell(10,i).getContents());
                       v_wofflinetest = StringManager.trim( sheet.getCell(11,i).getContents());
                       v_wportfolio = StringManager.trim( sheet.getCell(12,i).getContents());
                       v_wvaluation = StringManager.trim( sheet.getCell(13,i).getContents());
                       v_portfolio = StringManager.trim( sheet.getCell(14,i).getContents());
                       v_valuation = StringManager.trim( sheet.getCell(15,i).getContents());
                       v_addpointvalue = StringManager.trim( sheet.getCell(16,i).getContents());
                       v_addpointvalue1 = StringManager.trim( sheet.getCell(17,i).getContents());
                       v_addpointvalue2 = StringManager.trim( sheet.getCell(18,i).getContents());
                     
             
                       sql  = "update tz_blstudent                      \n";
                       sql += "   set                                   \n";
                       sql += "       onlinetest = ?,                   \n";
                       sql += "       offlinetest = ?,                  \n";
                       sql += "       portfolio = ?,                    \n";
                       sql += "       valuation = ?,                    \n";
                       sql += "       addpointvalue = ?,                \n";
                       sql += "       addpointvalue1 = ?,               \n";
                       sql += "       addpointvalue2 = ?,               \n";                
                       sql += "       wonlinetest = ?,                  \n";
                       sql += "       wofflinetest = ?,                 \n";
                       sql += "       wportfolio = ?,                   \n";
                       sql += "       wvaluation = ?                    \n";
                       sql += " where                                   \n";
                       sql += "       coursecode = ?                    \n";
                       sql += "   and courseyear = ?                    \n";
                       sql += "   and courseseq = ?                     \n";
                       sql += "   and userid = ?                        \n";
                 
                       pstmt1 = connMgr.prepareStatement(sql);       
                       int params = 1;                
                       pstmt1.setDouble(params++, Double.parseDouble(v_onlinetest));
                       pstmt1.setDouble(params++, Double.parseDouble(v_offlinetest));
                       pstmt1.setDouble(params++, Double.parseDouble(v_portfolio));
                       pstmt1.setDouble(params++, Double.parseDouble(v_valuation));
                       pstmt1.setDouble(params++, Double.parseDouble(v_addpointvalue));
                       pstmt1.setDouble(params++, Double.parseDouble(v_addpointvalue1));
                       pstmt1.setDouble(params++, Double.parseDouble(v_addpointvalue2));
                       pstmt1.setDouble(params++, Double.parseDouble(v_wonlinetest));
                       pstmt1.setDouble(params++, Double.parseDouble(v_wofflinetest));
                       pstmt1.setDouble(params++, Double.parseDouble(v_wportfolio));
                       pstmt1.setDouble(params++, Double.parseDouble(v_wvaluation));
                       pstmt1.setString(params++, v_coursecode);
                       pstmt1.setString(params++, v_courseyear);
                       pstmt1.setString(params++, v_courseseq);
                       pstmt1.setString(params++, v_userid);
                       isOk1 += pstmt1.executeUpdate();
                       
                       if(pstmt1 != null) { try { pstmt1.close(); } catch (Exception e) {} }
                       if(isOk1 < 1) throw new Exception("등록중 에러발생 \n\n");
                   }
                   
                   if(isOk1 > 0) {
                       if(connMgr != null) { try { connMgr.commit(); }catch (Exception e10) {} }  
                   }
               }
               catch(Exception ex) {
                   if(connMgr != null) { try { connMgr.rollback(); }catch (Exception e10) {} }  
                   ErrorManager.getErrorStackTrace(ex, box, sql);            
                   throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
               }
               finally {
                   if(connMgr != null) { try { connMgr.setAutoCommit(true); }catch (Exception e10) {} }            
                   if(pstmt1 != null) { try { pstmt1.close(); } catch (Exception e) {} }
                   if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
               }
               return isOk1;
           }
}