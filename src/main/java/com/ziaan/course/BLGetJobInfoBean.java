// **********************************************************
//  1. 제      목: B/L 과정 취업현황 관리
//  2. 프로그램명: BLGetJobInfoBean.java
//  3. 개      요:
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 
//  7. 수      정:
// **********************************************************
package com.ziaan.course;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class BLGetJobInfoBean { 
    public BLGetJobInfoBean() { }

	/**
	* B/L 과정 취업현황 관리 목록
	*/
    public ArrayList selectGetJobInfoList(RequestBox box) throws Exception {
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
        
        try {
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();
			
			sql = "select c.coursenm                                                                \n"
                + ", e.branchnm                                                                     \n"
                + ", d.coursecode                                                                   \n"
                + ", d.courseyear                                                                   \n"
                + ", d.courseseq                                                                    \n"
                + ", b.userid                                                                       \n"
                + ", b.name                                                                         \n"
                + ", b.hometel                                                                      \n"
                + ", b.handphone                                                                    \n"
                + ", b.email                                                                        \n"
                + ", isgetjob                                                                       \n"
                + ", note                                                                           \n"
                + "from tz_blstold a, tz_member b, tz_bl_course c, tz_blcourseseq d, tz_branch e    \n"
                + "where a.userid = b.userid                                                        \n"
                + "and a.coursecode = c.coursecode                                                  \n"
                + "and a.courseyear = d.courseyear                                                  \n"
                + "and a.courseseq = d.courseseq                                                    \n"
                + "and c.coursecode = d.coursecode                                                  \n"
                + "and a.branchcode = e.branchcode                                                  \n"
                + "and a.status = 'L'                                                               \n"
                + "and d.grcode = " + StringManager.makeSQL(ss_grcode) + "                          \n"
                + "and d.courseyear = " + StringManager.makeSQL(ss_blcourseyear) + "                  \n";
                
                if ( !ss_blcourse.equals("ALL") ) { 
                    sql += "and d.coursecode = " + StringManager.makeSQL(ss_blcourse) + "         \n";
                }
                
                if ( !ss_blcourseseq.equals("ALL") ) { 
                    sql += "and d.courseseq = " + StringManager.makeSQL(ss_blcourseseq) + "             \n";
                }
                
                if( !ss_branch.equals("ALL")) {
                    sql += "and a.branchcode = " + StringManager.makeSQL(ss_branch) + "             \n";
                }
                
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
     * 취업 여부 입력
     */
      public int saveIsGetJob(RequestBox box) throws Exception {
         DBConnectionManager connMgr = null;
         PreparedStatement pstmt1 = null;   
         String sql1 = "";
         int isOk1 = 1;
         
         String v_coursecode = box.getString("p_coursecode");
         String v_courseyear = box.getString("p_courseyear");
         String v_courseseq = box.getString("p_courseseq");
         String v_userid = box.getString("p_userid");
         int v_index = box.getInt("p_index");
         
         String v_isgetjob = box.getString("p_isgetjob" + v_index );
         String v_note = box.getString("p_note" + v_index);
         try {
             connMgr = new DBConnectionManager();     
             connMgr.setAutoCommit(false);
             
             sql1 = "update tz_blstold set                                 \n" +
                 "  isgetjob = ?                                           \n" +
                 ",  note = ?                                              \n" +
                 "where coursecode = ?                                     \n" +
                 "and courseyear = ?                                       \n" +
                 "and courseseq = ?                                        \n" + 
                 "and userid = ?                                           \n";
         
              pstmt1 = connMgr.prepareStatement(sql1);
              int params = 1;
              pstmt1.setString(params++, v_isgetjob);
              pstmt1.setString(params++, v_note);
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
}