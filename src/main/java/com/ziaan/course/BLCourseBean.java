// **********************************************************
//  1. 제      목: B/L 관련 BEAN
//  2. 프로그램명: BLCourseBean.java
//  3. 개      요:
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 
//  7. 수      정:
// **********************************************************
package com.ziaan.course;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.system.SelectionData;

public class BLCourseBean { 
    public BLCourseBean() { }

	/**
	* B/L 과정 목록
	*/
    public ArrayList selectCourseList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;        
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        DataBox dbox = null;
        String              ss_grcode       = box.getStringDefault("s_grcode"       , "ALL");   // 교육주관
        String              v_orderColumn   = box.getString       ("p_orderColumn"         );   // 정렬할 컬럼명
        String              v_orderType     = box.getString       ("p_orderType"           );   // 정렬할 순서
        String              v_searchtext    = box.getString       ("p_searchtext"          );
        
        try {
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();
			
			sql = "select coursecode, coursenm, isuse, to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyy.mm.dd') indate, to_char(to_date(a.ldate, 'yyyymmddhh24miss'), 'yyyy.mm.dd') ldate, a.inuserid, a.luserid, c.name lname, d.name inname  \n" +
                  "from tz_bl_course a, tz_grblcourse b, tz_member c, tz_member d   \n" +
                  "where a.coursecode = b.subjcourse(+)          \n" +
                  "and a.luserid  = c.userid(+)                   \n" +
                  "and a.inuserid = d.userid(+)                   \n" ;
            
//          교육그룹
            if ( !ss_grcode.equals("ALL") ) { 
                sql +=" and b.grcode = " + StringManager.makeSQL(ss_grcode);
            }
            
            if ( !v_searchtext.equals("") ) { 
                sql +=" and a.coursenm like '%' || " + StringManager.makeSQL(v_searchtext) + "|| '%'   \n";
            }

            if ( v_orderColumn.equals("") ) { 
                sql += " order by a.coursenm                                                           \n";
            } else { 
                sql += " order by " + v_orderColumn + v_orderType + "                                  \n";
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
    * B/L 과정 등록
    */
    public int insertCourse(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        PreparedStatement pstmt1 = null;        
        PreparedStatement pstmt2 = null;
        String sql = "";
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        
        String v_coursenm = box.getString("p_coursenm");
        String v_isuse = box.getString("p_isuse");
        
        String v_courseintro = box.getString("p_courseintro");
        String v_tutorintro = box.getString("p_tutorintro");
        String v_target = box.getString("p_target");
        String v_objective = box.getString("p_objective");
        String v_content = box.getString("p_content");
        int v_availableperiod = box.getInt("p_availableperiod");
        String v_addpointname1 = box.getString("p_addpointname1");
        String v_addpointname2 = box.getString("p_addpointname2");
        String v_adminid = box.getString("p_adminid");
        String v_adminname = box.getString("p_adminname");
        String v_admintel = box.getString("p_admintel");
        double v_wonlinetest = box.getDouble("p_wonlinetest");
        double v_wofflinetest = box.getDouble("p_wofflinetest");
        double v_wportfolio = box.getDouble("p_wportfolio");
        String v_wname = box.getString("p_wname");
        double v_wvalue = box.getDouble("p_wvalue");
        
//        double v_woffofflinetest  = box.getDouble("p_woffofflinetest");
//        String v_woffoffname1  = box.getString("p_woffoffname1");
//        String v_woffoffname2  = box.getString("p_woffoffname2");
//        double v_woffoffvalue1 = box.getDouble("p_woffoffvalue1");
//        double v_woffoffvalue2 = box.getDouble("p_woffoffvalue2");
//        double v_wofftotal  = box.getDouble("p_wofftotal");
        
        
        double v_wtotal = box.getDouble("p_wtotal");
        int v_offselectedsubjnum = box.getInt("p_offselectedsubjnum");
        double v_offonlinetest = box.getDouble("p_offonlinetest");
        double v_offinterview = box.getDouble("p_offinterview");
        double v_offofflinetest = box.getDouble("p_offofflinetest");
        String v_offname1 = box.getString("p_offname1");
        String v_offname2 = box.getString("p_offname2");
        double v_offvalue1 = box.getDouble("p_offvalue1");
        double v_offvalue2 = box.getDouble("p_offvalue2");
        String s_userid = box.getSession("userid");
        
        try {
            connMgr = new DBConnectionManager();         
            connMgr.setAutoCommit(false);
            
            //----------------------   게시판 번호 가져온다 ----------------------------
            sql = "select nvl(max(TO_NUMBER(trim(coursecode))), 0) from tz_bl_course";
            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_seq = ls.getInt(1) + 1;
            
            ls.close();
            
            sql1 =  "insert into tz_bl_course(                                                                                              \n" +
                    "  coursecode                                                                                                           \n" +
                    ", coursenm                                                                                                             \n" +
                    ", isuse                                                                                                                \n" +
                    ", courseintro                                                                                                          \n" +
                    ", tutorintro                                                                                                           \n" +
                    ", target                                                                                                               \n" +
                    ", objective                                                                                                            \n" +
                    ", content                                                                                                              \n" +
                    ", availableperiod                                                                                                      \n" +
                    ", addpointname1                                                                                                        \n" +
                    ", addpointname2                                                                                                        \n" +
                    ", adminid                                                                                                              \n" +
                    ", adminname                                                                                                            \n" +
                    ", admintel                                                                                                             \n" +
                    ", wonlinetest                                                                                                          \n" +
                    ", wofflinetest                                                                                                         \n" +
                    ", wportfolio                                                                                                           \n" +
                    ", wname                                                                                                               \n" +
                    ", wvalue                                                                                                               \n" +
//                    ", woffofflinetest                                                                                                      \n" +
//                    ", woffoffname1                                                                                                         \n" +
//                    ", woffoffname2                                                                                                         \n" +
//                    ", woffoffvalue1                                                                                                        \n" +
//                    ", woffoffvalue2                                                                                                        \n" +
//                    ", wofftotal                                                                                                            \n" +
                    ", wtotal                                                                                                               \n" +
                    ", offselectedsubjnum                                                                                                   \n" +
                    ", offonlinetest                                                                                                        \n" +
                    ", offinterview                                                                                                         \n" +
                    ", offofflinetest                                                                                                       \n" +
                    ", offname1                                                                                                             \n" +
                    ", offname2                                                                                                             \n" +
                    ", offvalue1                                                                                                            \n" +
                    ", offvalue2                                                                                                            \n" +
                    ", luserid                                                                                                              \n" +
                    ", ldate                                                                                                                \n" +
                    ", inuserid                                                                                                             \n" +
                    ") values                                                                                                               \n" +
                    " ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ? ) \n";
            
            pstmt1 = connMgr.prepareStatement(sql1);
            System.out.println(sql1);
            int params = 1;
            pstmt1.setString(params++, v_seq+"");
            pstmt1.setString(params++, v_coursenm); 
            pstmt1.setString(params++, v_isuse); 
            pstmt1.setString(params++, v_courseintro); 
            pstmt1.setString(params++, v_tutorintro);
            pstmt1.setString(params++, v_target); 
            pstmt1.setString(params++, v_objective); 
            pstmt1.setString(params++, v_content); 
            pstmt1.setDouble(params++, v_availableperiod); 
            pstmt1.setString(params++, v_addpointname1); 
            pstmt1.setString(params++, v_addpointname2); 
            pstmt1.setString(params++, v_adminid); 
            pstmt1.setString(params++, v_adminname); 
            pstmt1.setString(params++, v_admintel); 
            pstmt1.setDouble(params++, v_wonlinetest); 
            pstmt1.setDouble(params++, v_wofflinetest); 
            pstmt1.setDouble(params++, v_wportfolio); 
            pstmt1.setString(params++, v_wname); 
            pstmt1.setDouble(params++, v_wvalue); 
//            pstmt1.setDouble(params++, v_woffofflinetest); 
//            pstmt1.setString(params++, v_woffoffname1); 
//            pstmt1.setString(params++, v_woffoffname2); 
//            pstmt1.setDouble(params++, v_woffoffvalue1); 
//            pstmt1.setDouble(params++, v_woffoffvalue2); 
//            pstmt1.setDouble(params++, v_wofftotal); 
            pstmt1.setDouble(params++, v_wtotal); 
            pstmt1.setDouble(params++, v_offselectedsubjnum); 
            pstmt1.setDouble(params++, v_offonlinetest); 
            pstmt1.setDouble(params++, v_offinterview); 
            pstmt1.setDouble(params++, v_offofflinetest); 
            pstmt1.setString(params++, v_offname1); 
            pstmt1.setString(params++, v_offname2); 
            pstmt1.setDouble(params++, v_offvalue1); 
            pstmt1.setDouble(params++, v_offvalue2); 
            pstmt1.setString(params++, s_userid);                   
            pstmt1.setString(params++, s_userid);                   
            isOk1 = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
            
            if(isOk1 > 0) {
                sql2 =  "insert into tz_grblcourse(                 \n" +
                "  grcode                                           \n" +
                ", subjcourse                                       \n" +
                ", luserid                                          \n" +
                ", ldate                                            \n" +
                ") values                                           \n" +
                " ( ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))  \n";
        
                pstmt2 = connMgr.prepareStatement(sql2);
                
                params = 1;
                pstmt2.setString(params++, "N000001");
                pstmt2.setString(params++, v_seq+"");
                pstmt2.setString(params++, s_userid);                   
                isOk2 = pstmt2.executeUpdate();
                if ( pstmt2 != null ) { pstmt2.close(); }
            }
            
            if(isOk2 > 0) {
                if(connMgr != null) { try { connMgr.commit(); }catch (Exception e10) {} }  
            }
        }
        catch (Exception ex) {
            if(connMgr != null) { try { connMgr.rollback(); }catch (Exception e10) {} }  
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
        }
        finally {
            if(connMgr != null) { try { connMgr.setAutoCommit(true); }catch (Exception e10) {} }            
            if(ls != null) { try { ls.close(); } catch (Exception e) {} }
            if(pstmt1 != null) { try { pstmt1.close(); } catch (Exception e1) {} }
            if(pstmt2 != null) { try { pstmt2.close(); } catch (Exception e1) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }            
        }
        return isOk2;
    }
    
    /**
    * B/L과정 상세조회
    */
    public DataBox selectCourse(RequestBox box) throws Exception {
          DBConnectionManager connMgr = null;
          ListSet ls = null;
          String sql = "";
          DataBox dbox = null;
                  
         String v_coursecode = box.getString("p_coursecode");
     
         try {
              connMgr = new DBConnectionManager();

              sql = "select coursecode                                                                                                      \n" +
                    ", coursenm                                                                                                             \n" +
                    ", isuse                                                                                                                \n" +
                    ", courseintro                                                                                                          \n" +
                    ", tutorintro                                                                                                           \n" +
                    ", target                                                                                                               \n" +
                    ", objective                                                                                                            \n" +
                    ", content                                                                                                              \n" +
                    ", availableperiod                                                                                                      \n" +
                    ", addpointname1                                                                                                        \n" +
                    ", addpointname2                                                                                                        \n" +
                    ", adminid                                                                                                              \n" +
                    ", adminname                                                                                                            \n" +
                    ", admintel                                                                                                             \n" +
                    ", wonlinetest                                                                                                          \n" +
                    ", wofflinetest                                                                                                         \n" +
//                    ", woffofflinetest                                                                                                      \n" +
//                    ", woffoffname1                                                                                                         \n" +
//                    ", woffoffname2                                                                                                         \n" +
//                    ", woffoffvalue1                                                                                                        \n" +
//                    ", woffoffvalue2                                                                                                        \n" +
//                    ", wofftotal                                                                                                            \n" +
                    ", wportfolio                                                                                                           \n" +
                    ", wname                                                                                                                \n" +
                    ", wvalue                                                                                                               \n" +
                    ", wtotal                                                                                                               \n" +
                    ", offselectedsubjnum                                                                                                   \n" +
                    ", offonlinetest                                                                                                        \n" +
                    ", offinterview                                                                                                         \n" +
                    ", offofflinetest                                                                                                       \n" +
                    ", offname1                                                                                                             \n" +
                    ", offname2                                                                                                             \n" +
                    ", offvalue1                                                                                                            \n" +
                    ", offvalue2                                                                                                            \n" +
                    ", (                                                                                                                    \n" +
                    "   select decode(count(*), 0, 'Y', 'N')                                                                                \n" +
                    "   from tz_blcourseseq                                                                                                 \n" +
                    "   where coursecode = a.coursecode                                                                                     \n" +
                    ") delyn                                                                                                                \n" + 
                    "from tz_bl_course a                                                                                                    \n" +
                    "where coursecode = " + StringManager.makeSQL(v_coursecode);
              ls = connMgr.executeQuery(sql); 
          
              while(ls.next()) {
                  dbox = ls.getDataBox();
                  dbox.put("d_wonlinetest", new Double(ls.getDouble("wonlinetest")));
                  dbox.put("d_wofflinetest", new Double(ls.getDouble("wofflinetest")));
                  dbox.put("d_wportfolio", new Double(ls.getDouble("wportfolio")));
                  dbox.put("d_wvalue", new Double(ls.getDouble("wvalue")));
                  dbox.put("d_wtotal", new Double(ls.getDouble("wtotal")));
                  dbox.put("d_offonlinetest", new Double(ls.getDouble("offonlinetest")));
                  dbox.put("d_offinterview", new Double(ls.getDouble("offinterview")));
                  dbox.put("d_offofflinetest", new Double(ls.getDouble("offofflinetest")));
                  dbox.put("d_offvalue1", new Double(ls.getDouble("offvalue1")));
                  dbox.put("d_offvalue2", new Double(ls.getDouble("offvalue2")));
              }   
          }
          catch (Exception ex) {
              ErrorManager.getErrorStackTrace(ex, box, sql);
              throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
          }
          finally {
              if(ls != null) {try {ls.close();} catch(Exception e){}}
              if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
          }
          return dbox;
      }  
    
     /**
     * 선택된 과정 수정
     */
      public int updateCourse(RequestBox box) throws Exception {
         DBConnectionManager connMgr = null;
         PreparedStatement pstmt1 = null;   
         String sql1 = "";
         int isOk1 = 1;
         
         String v_coursecode = box.getString("p_coursecode");
         String v_coursenm = box.getString("p_coursenm");
         String v_isuse = box.getString("p_isuse");
         String v_courseintro = box.getString("p_courseintro");
         String v_tutorintro = box.getString("p_tutorintro");
         String v_target = box.getString("p_target");
         String v_objective = box.getString("p_objective");
         String v_content = box.getString("p_content");
         int v_availableperiod = box.getInt("p_availableperiod");
         String v_addpointname1 = box.getString("p_addpointname1");
         String v_addpointname2 = box.getString("p_addpointname2");
         String v_adminid = box.getString("p_adminid");
         String v_adminname = box.getString("p_adminname");
         String v_admintel = box.getString("p_admintel");
         double v_wonlinetest = box.getDouble("p_wonlinetest");
         double v_wofflinetest = box.getDouble("p_wofflinetest");
         double v_wtotal = box.getDouble("p_wtotal");
         int v_offselectedsubjnum = box.getInt("p_offselectedsubjnum");
         double v_offonlinetest = box.getDouble("p_offonlinetest");
         double v_offinterview = box.getDouble("p_offinterview");
         double v_offofflinetest = box.getDouble("p_offofflinetest");
         String v_offname1 = box.getString("p_offname1");
         String v_offname2 = box.getString("p_offname2");
         double v_offvalue1 = box.getDouble("p_offvalue1");
         double v_offvalue2 = box.getDouble("p_offvalue2");
//         double v_woffofflinetest = box.getDouble("p_woffofflinetest");
//         String v_woffoffname1 = box.getString("p_woffoffname1");
//         String v_woffoffname2 = box.getString("p_woffoffname2");
//         double v_woffoffvalue1 = box.getDouble("p_woffoffvalue1");
//         double v_woffoffvalue2 = box.getDouble("p_woffoffvalue2");
//         double v_wofftotal = box.getDouble("p_wofftotal");
         double v_wportfolio = box.getDouble("p_wportfolio");
         String v_wname = box.getString("p_wname");
         double v_wvalue = box.getDouble("p_wvalue");                                                                                                           
         String s_userid = box.getSession("userid");
         try {
             connMgr = new DBConnectionManager();     
             connMgr.setAutoCommit(false);
             
            sql1 = "update tz_bl_course set                             \n" +
                "coursenm = ?                                           \n" +
                ", isuse = ?                                            \n" +
                ", courseintro  = ?                                     \n" +
                ", tutorintro  = ?                                      \n" +
                ", target = ?                                           \n" +
                ", objective = ?                                        \n" +
                ", content = ?                                          \n" +
                ", availableperiod = ?                                  \n" +
                ", addpointname1 = ?                                    \n" +
                ", addpointname2 = ?                                    \n" +
                ", adminid = ?                                          \n" +
                ", adminname = ?                                        \n" +
                ", admintel = ?                                         \n" +
                ", wonlinetest = ?                                      \n" +
                ", wofflinetest = ?                                     \n" +
//                ", woffofflinetest = ?                                  \n" +
//                ", woffoffname1 = ?                                     \n" +
//                ", woffoffname2 = ?                                     \n" +
//                ", woffoffvalue1 = ?                                    \n" +
//                ", woffoffvalue2 = ?                                    \n" +
//                ", wofftotal = ?                                        \n" +
                ", wportfolio = ?                                       \n" +
                ", wname = ?                                            \n" +
                ", wvalue = ?                                           \n" +
                ", wtotal = ?                                           \n" +
                ", offselectedsubjnum = ?                               \n" +
                ", offonlinetest = ?                                    \n" +
                ", offinterview = ?                                     \n" +
                ", offofflinetest = ?                                   \n" +
                ", offname1 = ?                                         \n" +
                ", offname2 = ?                                         \n" +
                ", offvalue1 = ?                                        \n" +
                ", offvalue2 = ?                                        \n" + 
                ", luserid = ?                                          \n" +
                ", ldate = to_char(sysdate, 'YYYYMMDDHH24MISS')         \n" +
                "where coursecode = ?                                   \n";  
             pstmt1 = connMgr.prepareStatement(sql1);
             
             int params = 1;
             pstmt1.setString(params++, v_coursenm); 
             pstmt1.setString(params++, v_isuse); 
             pstmt1.setString(params++, v_courseintro); 
             pstmt1.setString(params++, v_tutorintro);
             pstmt1.setString(params++, v_target); 
             pstmt1.setString(params++, v_objective); 
             pstmt1.setString(params++, v_content); 
             pstmt1.setDouble(params++, v_availableperiod); 
             pstmt1.setString(params++, v_addpointname1); 
             pstmt1.setString(params++, v_addpointname2); 
             pstmt1.setString(params++, v_adminid); 
             pstmt1.setString(params++, v_adminname); 
             pstmt1.setString(params++, v_admintel); 
             pstmt1.setDouble(params++, v_wonlinetest); 
             pstmt1.setDouble(params++, v_wofflinetest); 
//             pstmt1.setDouble(params++, v_woffofflinetest); 
//             pstmt1.setString(params++, v_woffoffname1); 
//             pstmt1.setString(params++, v_woffoffname2); 
//             pstmt1.setDouble(params++, v_woffoffvalue1); 
//             pstmt1.setDouble(params++, v_woffoffvalue2); 
//             pstmt1.setDouble(params++, v_wofftotal); 
             pstmt1.setDouble(params++, v_wportfolio);
             pstmt1.setString(params++, v_wname);
             pstmt1.setDouble(params++, v_wvalue);
             pstmt1.setDouble(params++, v_wtotal); 
             pstmt1.setInt(params++, v_offselectedsubjnum); 
             pstmt1.setDouble(params++, v_offonlinetest); 
             pstmt1.setDouble(params++, v_offinterview); 
             pstmt1.setDouble(params++, v_offofflinetest); 
             pstmt1.setString(params++, v_offname1); 
             pstmt1.setString(params++, v_offname2); 
             pstmt1.setDouble(params++, v_offvalue1); 
             pstmt1.setDouble(params++, v_offvalue2); 
             pstmt1.setString(params++, s_userid);    
             pstmt1.setString(params++, v_coursecode);
                     
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
       * 선택된 과정 삭제
       */
       public int deleteCourse(RequestBox box) throws Exception {
           DBConnectionManager connMgr = null;
           PreparedStatement pstmt1 = null;        
           PreparedStatement pstmt2 = null;        
           String sql1 = "";
           String sql2 = "";
           int isOk1 = 1;
           int isOk2 = 1;       
           String v_coursecode = box.getString("p_coursecode");    
           try {
               connMgr = new DBConnectionManager();           
               connMgr.setAutoCommit(false);
         
               sql2 = "delete from tz_grblcourse where subjcourse = ?";
                   pstmt2 = connMgr.prepareStatement(sql2);
                   pstmt2.setString(1, v_coursecode);
                   isOk2 = pstmt2.executeUpdate();
                   if ( pstmt2 != null ) { pstmt2.close(); }
                   sql1 = "delete from tz_bl_course where coursecode = ?";
               pstmt1 = connMgr.prepareStatement(sql1);
               pstmt1.setString(1, v_coursecode);
               isOk1 = pstmt1.executeUpdate();
               if ( pstmt1 != null ) { pstmt1.close(); }
               if (isOk1 > 0 && isOk2 > 0) {                     
                   if(connMgr != null) { try { connMgr.commit(); }catch (Exception e10) {} }  
               }
           }
           catch (Exception ex) {
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
           return isOk1*isOk2;
       }     
       
       /**
       선택된 교육그룹 조회
       @param box          receive from the form object and session
       @return ArrayList   교육그룹 리스트
       */
       public ArrayList SelectedGrcodeList(RequestBox box) throws Exception { 
           DBConnectionManager  connMgr = null;
           ListSet          ls      = null;
           ArrayList            list    = null;
           String               sql     = "";
           SelectionData        data    = null;
           String               v_subj  = box.getString("p_subj");

           try { 
               sql = "select a.grcode code, a.grcodenm name ";
               sql += "  from tz_grcode a, ";
               sql += "       tz_grblcourse b  ";
               sql += " where a.grcode = b.grcode ";
               sql += "   and b.subjcourse = " + SQLString.Format(v_subj);
               sql += " order by a.grcode " ;

               connMgr = new DBConnectionManager();
               list     = new ArrayList();
               ls       = connMgr.executeQuery(sql);

               while ( ls.next() ) { 
                   data = new SelectionData();

                   data.setCode( ls.getString("code") );
                   data.setName( ls.getString("name") );

                   list.add(data);
               }
           } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
               throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
           } finally { 
               if ( ls != null   ) { try { ls.close(); } catch ( Exception e ) { } }
               if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
           }
           
           return list;
       }

       
       /**
       새로운 코스코드 등록
       @param box      receive from the form object and session
       @return isOk    1:insert success,0:insert fail
       */
        public int RelatedGrcodeInsert(RequestBox box) throws Exception { 
           DBConnectionManager  connMgr = null;

           PreparedStatement    pstmt   = null;
           PreparedStatement    pstmt2      = null;
           PreparedStatement    pstmt3      = null;
           String               sql         = "";
           String               sql2        = "";
           String               sql3        = "";
           int              isOk        = 0;
           int              isOk2       = 0;
           int              isOk3       = 0;

           String               v_subj      = box.getString("p_subj");
           String               v_grcode    = "";
           String               v_luserid   = box.getSession("userid");

           try { 
               connMgr                  = new DBConnectionManager();
               connMgr.setAutoCommit(false);

               // delete tz_grblcourse table
               sql  = "delete from tz_grblcourse where subjcourse = ? ";
               pstmt    = connMgr.prepareStatement(sql);
               pstmt.setString(1, v_subj);
               
               isOk     = pstmt.executeUpdate();

               if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }

               String           v_selectedgrcodes   = box.getString("p_selectedgrcodes");
               StringTokenizer v_token          = new StringTokenizer(v_selectedgrcodes, ";");

               // insert TZ_COURSESUBJ table
               sql =  "insert into tz_grblcourse(grcode, subjcourse, isnew, disseq, grpcode, grpname, luserid, ldate) ";
               sql +=  " values (?, ?, ?, ?, ?, ?, ?, ?)";

               pstmt    = connMgr.prepareStatement(sql);
               
               while ( v_token.hasMoreTokens() ) { 
                   v_grcode = v_token.nextToken();

                   pstmt.setString(1, v_grcode      );
                   pstmt.setString(2, v_subj        );
                   pstmt.setString(3, "Y"           );
                   pstmt.setInt   (4, 0         );
                   pstmt.setString(5, ""            );
                   pstmt.setString(6, ""            );
                   pstmt.setString(7, v_luserid );
                   pstmt.setString(8, FormatDate.getDate("yyyyMMddHHmmss") );
                   isOk = pstmt.executeUpdate();
                   if ( isOk == 1 ) { 
                       connMgr.commit();
                   } else { 
                       connMgr.rollback();
                   }
               }
               if ( pstmt != null ) { pstmt.close(); }
           } catch ( Exception ex ) { 
               isOk = 0;
               connMgr.rollback();
               ErrorManager.getErrorStackTrace(ex, box, sql);
               throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
           } finally { 
               if ( isOk > 0 ) { connMgr.commit(); }
               if ( pstmt != null   ) { try { pstmt.close(); } catch ( Exception e ) { } }
               if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
           }
           
           return isOk;
       }
}