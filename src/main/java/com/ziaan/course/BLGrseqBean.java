// **********************************************************
//  1. 제      목: B/L 기수 관련 BEAN
//  2. 프로그램명: BLGrseqBean.java
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
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;
import com.ziaan.system.CodeConfigBean;

public class BLGrseqBean { 
    public BLGrseqBean() { }

	/**
	* B/L 기수 목록
	*/
    public ArrayList selectCourseseqList(RequestBox box) throws Exception {
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
        String              ss_blcourse     = box.getStringDefault("s_blcourse",  "ALL");
        String              ss_blcourseseq     = box.getStringDefault("s_blcourseseq",  "ALL");
        String              ss_blcourseyear     = box.getStringDefault("s_blcourseyear",  "ALL");
        
        
        try {
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();
			
			sql = "select a.coursecode, b.coursenm, a.courseseq, a.courseyear, a.onlineedustart, a.onlineeduend,         \n"
                + "(                                                                                                     \n"
                + "     select decode(count(*), 0, 'Y', 'N')                                                             \n"
                + "     from tz_coursebranchinfo                                                                         \n"
                + "     where coursecode = a.coursecode                                                                  \n"
                + "     and courseyear = a.courseyear                                                                    \n"
                + "     and courseseq = a.courseseq                                                                      \n"
                + ") delyn                                                                                               \n"
                + "from tz_blcourseseq a, tz_bl_course b                                                                 \n"
                + "where a.coursecode = b.coursecode                                                                     \n"
			    + " and a.grcode = " + StringManager.makeSQL(ss_grcode)                                             + "  \n"
                + " and a.courseyear = " + StringManager.makeSQL(ss_blcourseyear)                                   + "  \n";
            
            if ( !ss_blcourse.equals("ALL") ) { 
                sql +=" and a.coursecode = " + StringManager.makeSQL(ss_blcourse);
            }

            if ( !ss_blcourseseq.equals("ALL") ) { 
                sql +=" and a.courseseq = " + StringManager.makeSQL(ss_blcourseseq);
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
    * B/L 기수 등록
    */
    public int insertCourseseq(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        PreparedStatement pstmt1 = null;        
        PreparedStatement pstmt2 = null;
        String sql = "";
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        
        String v_coursecode = box.getString("s_blcourse");
        String v_courseyear = box.getString("p_courseyear");
        String v_grcode = box.getString("s_grcode");
        String v_copy_gyear = box.getString("p_copy_gyear");
        String v_copy_grseq = box.getString("p_copy_grseq");
        
        String v_selonlinemust = box.getString("p_onlinemustcodes");
        String v_selonlineoption = box.getString("p_onlineoptioncodes");
        String v_selofflinesubj = box.getString("p_offlinesubjcodes");
        
        String v_sulpaper = box.getString("p_sulpaper");
        String v_propstart = box.getString("p_propstart").replaceAll("-", "");
        String v_propend = box.getString("p_propend").replaceAll("-", "");
        String v_onlineedustart = box.getString("p_onlineedustart").replaceAll("-", "");
        String v_onlineeduend = box.getString("p_onlineeduend").replaceAll("-", "");
        int v_canceldays = box.getInt("p_canceldays");
        String v_isonlinecomplete = box.getString("p_isonlinecomplete");
        String s_userid = box.getSession("userid");
        int v_subjnumforcomplete = box.getInt("p_subjnumforcomplete");
        
        try {
            connMgr = new DBConnectionManager();         
            connMgr.setAutoCommit(false);
            
            //----------------------   게시판 번호 가져온다 ----------------------------
            sql = "select nvl(max(to_number(courseseq)), 0)                                 \n"
                + "from tz_blcourseseq                                           \n"
                + "where coursecode = " + StringManager.makeSQL(v_coursecode) + "\n"
                + "and courseyear = " + StringManager.makeSQL(v_courseyear);                                  
            
            ls = connMgr.executeQuery(sql);
            ls.next();
            //String v_courseseq_ = ls.getString(1);
            int v_courseseq = ls.getInt(1) + 1; //Integer.parseInt(v_courseseq_) + 1;
            ls.close();
            
            String v_courseseq_ = CodeConfigBean.addZero(v_courseseq, 4);
            
            sql1 =  "insert into tz_blcourseseq(                                                                                            \n" +
                    "   coursecode          \n"+
                    ",  courseyear                \n"+//1
                    ",  courseseq           \n"+//2
                    ",  grcode              \n"+//3
                    ",  coursenm            \n"+
                    ",  isuse               \n"+
                    ",  courseintro         \n"+
                    ",  tutorintro          \n"+
                    ",  target              \n"+
                    ",  objective           \n"+
                    ",  content             \n"+
                    ",  availableperiod     \n"+
                    ",  addpointname1       \n"+
                    ",  addpointname2       \n"+
                    ",  wonlinetest         \n"+
                    ",  wofflinetest        \n"+
                    ",  wtotal              \n"+
                    ",  offselectedsubjnum  \n"+
                    ",  offonlinetest       \n"+
                    ",  offinterview        \n"+
                    ",  offofflinetest      \n"+
                    ",  offname1            \n"+
                    ",  offname2            \n"+
                    ",  offvalue1           \n"+
                    ",  offvalue2           \n"+
                    ",  sul                 \n"+//4
                    ",  onlinepropstart     \n"+//5
                    ",  onlinepropend       \n"+//6
                    ",  onlineedustart      \n"+//7
                    ",  onlineeduend        \n"+//8
                    ",  canceldays          \n"+//9
                    ",  isonlinecomplete    \n"+//10
//                    ",  woffoffname1        \n" +
//                    ",  woffoffname2        \n" +
//                    ",  woffoffvalue1       \n" +
//                    ",  woffoffvalue2       \n" +
//                    ",  wofftotal           \n" +
                    ",  wportfolio          \n" +
                    ",  wname               \n" +
                    ",  wvalue              \n" +
                    ",  year                \n" +
                    ",  grseq               \n" +
                    //",  woffofflinetest     \n" +    
                    ",  ldate               \n"+
                    ",  luserid             \n"+
                    ",  subjnumforcomplete  \n" +
                    ")                      \n"+
                    " select                \n"+
                    "  coursecode                                                                                                           \n" +
                    ", ?                                                                                                                    \n" +//1
                    ", ?                                                                                                                    \n" +//2
                    ", ?                                                                                                                    \n" +//3
                    ", coursenm                                                                                                             \n" +
                    ", isuse                                                                                                                \n" +
                    ", courseintro                                                                                                          \n" +
                    ",  tutorintro                                                                                                          \n"+
                    ", target                                                                                                               \n" +
                    ", objective                                                                                                            \n" +
                    ", content                                                                                                              \n" +
                    ", availableperiod                                                                                                      \n" +
                    ", addpointname1                                                                                                        \n" +
                    ", addpointname2                                                                                                        \n" +
                    ", wonlinetest                                                                                                          \n" +
                    ", wofflinetest                                                                                                         \n" +
                    ", wtotal                                                                                                               \n" +
                    ", offselectedsubjnum                                                                                                   \n" +
                    ", offonlinetest                                                                                                        \n" +
                    ", offinterview                                                                                                         \n" +
                    ", offofflinetest                                                                                                       \n" +
                    ", offname1                                                                                                             \n" +
                    ", offname2                                                                                                             \n" +
                    ", offvalue1                                                                                                            \n" +
                    ", offvalue2                                                                                                            \n" +
                    ", ?                                                                                                                    \n" +//4
                    ", ?                                                                                                                    \n" +//5
                    ", ?                                                                                                                    \n" +//6
                    ", ?                                                                                                                    \n" +//7
                    ", ?                                                                                                                    \n" +//8
                    ", ?                                                                                                                    \n" +//9
                    ", ?                                                                                                                    \n" +//10
//                    ", woffoffname1                                                                                                         \n" +
//                    ", woffoffname2                                                                                                         \n" +
//                    ", woffoffvalue1                                                                                                        \n" +
//                    ", woffoffvalue2                                                                                                        \n" +
//                    ", wofftotal                                                                                                            \n" +
                    ", wportfolio                                                                                                           \n" +
                    ", wname                                                                                                                \n" +
                    ", wvalue                                                                                                               \n" +
                    ", ?                                                                                                                    \n" +
                    ", ?                                                                                                                    \n" +
                   // ",  woffofflinetest                                                                                                     \n" +
                    ", to_char(sysdate, 'YYYYMMDDHH24MISS')                                                                                 \n" + 
                    ", ?                                                                                                                    \n" +//11
                    ", ?                                                                                                                    \n" +
                    "from tz_bl_course                                                                                                      \n" +
                    "where coursecode = ?                                                                                                   \n";//12
                    
            pstmt1 = connMgr.prepareStatement(sql1);
            
            int params = 1;
            pstmt1.setString(params++, v_courseyear);
            pstmt1.setString(params++, v_courseseq_);
            pstmt1.setString(params++, v_grcode);
            pstmt1.setString(params++, v_sulpaper);
            pstmt1.setString(params++, v_propstart);
            pstmt1.setString(params++, v_propend);
            pstmt1.setString(params++, v_onlineedustart);
            pstmt1.setString(params++, v_onlineeduend);
            pstmt1.setInt(params++, v_canceldays);
            pstmt1.setString(params++, v_isonlinecomplete);
            pstmt1.setString(params++, v_copy_gyear);
            pstmt1.setString(params++, v_copy_grseq);
            pstmt1.setString(params++, s_userid);
            pstmt1.setInt(params++, v_subjnumforcomplete);
            pstmt1.setString(params++, v_coursecode);
            
            isOk1 = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
            
            if(isOk1 > 0) {
                this.insertSubj(connMgr, box, v_coursecode, v_courseyear, v_courseseq_, v_selonlinemust, "Y", "ON");
                this.insertSubj(connMgr, box, v_coursecode, v_courseyear, v_courseseq_, v_selonlineoption, "N", "ON");
                this.insertSubj(connMgr, box, v_coursecode, v_courseyear, v_courseseq_, v_selofflinesubj, "", "OFF");
            }
            
            if(isOk1 > 0) {
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
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }            
        }
        return isOk1;
    }
    
    /**
     * 과목 등록
     */
      public int insertSubj(DBConnectionManager connMgr, RequestBox box, String v_coursecode, String v_courseyear, String v_courseseq_, 
              String value, String isrequired, String isOnOff) throws Exception {
          String sql2 = "";
          PreparedStatement pstmt2 = null;     
          int isOk2 = 1;     
          String v_var1 = "";
          String v_var2 = "";
          
          String v_propstart = box.getString("p_propstart").replaceAll("-", "") + "00";
          String v_propend = box.getString("p_propend").replaceAll("-", "") + "23";
          String v_onlineedustart = box.getString("p_onlineedustart").replaceAll("-", "") + "00";
          String v_onlineeduend = box.getString("p_onlineeduend").replaceAll("-", "") + "23";
          String v_sulpaper = box.getString("p_sulpaper");
          try {
              StringTokenizer v_token = new StringTokenizer(value, ";");
              while ( v_token.hasMoreTokens() ) { 
                sql2 = "insert into tz_blcourseseqsubj (        \n" +
                "coursecode,                                    \n" +
                "courseyear,                                    \n" +
                "courseseq,                                     \n" +
                "subj,                                          \n" +
                "subjseq,                                       \n" +
                "year,                                          \n" +
                "isrequired,                                    \n" +
                "isonoff                                        \n" +
                ") values                                       \n" +
                " ( ?, ?, ?, ?, ?, ?, ?, ?)                     \n";
    
                pstmt2 = connMgr.prepareStatement(sql2);
                
                v_var1 = v_token.nextToken();
                StringTokenizer v_token2 = new StringTokenizer(v_var1, "|");
            
                int params = 1;
                pstmt2.setString(params++, v_coursecode);
                pstmt2.setString(params++, v_courseyear);
                pstmt2.setString(params++, v_courseseq_);                   
                while ( v_token2.hasMoreTokens() ) {
                    v_var2 = v_token2.nextToken();
                    pstmt2.setString(params++, v_var2);
                }
                pstmt2.setString(params++, isrequired);
                pstmt2.setString(params++, isOnOff);
                isOk2 = pstmt2.executeUpdate();
                if ( pstmt2 != null ) { pstmt2.close(); }
                if(isOk2 > 0) {
                    if("ON".equals(isOnOff)) {
                        sql2 = "update tz_subjseq set                     \n" +
                        "  propstart  = ?                                 \n" +
                        ", propend = ?                                    \n" +
                        ", edustart = ?                                   \n" +
                        ", eduend = ?                                     \n" +
                        ", sulpapernum = ?                                \n" +
                        "where subj = ?                                   \n" + 
                        "and subjseq = ?                                  \n" +
                        "and year = ?                                     \n";
            
                        pstmt2 = connMgr.prepareStatement(sql2);
                        
                        params = 1;
                        pstmt2.setString(params++, v_propstart);
                        pstmt2.setString(params++, v_propend);
                        pstmt2.setString(params++, v_onlineedustart);                   
                        pstmt2.setString(params++, v_onlineeduend);
                        pstmt2.setString(params++, v_sulpaper);
                        v_token2 = new StringTokenizer(v_var1, "|");
                        while ( v_token2.hasMoreTokens() ) {
                            v_var2 = v_token2.nextToken();
                            
                            System.out.println("===================" + v_var2 );
                            
                            pstmt2.setString(params++, v_var2);
                        }
                        isOk2 = pstmt2.executeUpdate();
                        if ( pstmt2 != null ) { pstmt2.close(); }
                    } else if ("OFF".equals(isOnOff)) {
                        sql2 = "update tz_subjseq set                     \n" +
                        "  propstart  = ?                                 \n" +
                        ", propend = ?                                    \n" +
                        ", sulpapernum = ?                                \n" +
                        "where subj = ?                                   \n" + 
                        "and subjseq = ?                                  \n" +
                        "and year = ?                                     \n";
            
                        pstmt2 = connMgr.prepareStatement(sql2);
                        
                        params = 1;
                        pstmt2.setString(params++, v_propstart);
                        pstmt2.setString(params++, v_propend);
                        pstmt2.setString(params++, v_sulpaper);
                        v_token2 = new StringTokenizer(v_var1, "|");
                        while ( v_token2.hasMoreTokens() ) {
                            v_var2 = v_token2.nextToken();
                            
                            System.out.println("===================" + v_var2 );
                            
                            pstmt2.setString(params++, v_var2);
                        }
                        isOk2 = pstmt2.executeUpdate();
                    }
                    if ( pstmt2 != null ) { pstmt2.close(); }
                }
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt2 != null) { try { pstmt2.close(); } catch (Exception e1) {} }
        }
        return isOk2;
     }
    
    /**
    * B/L기수 상세조회
    */
    public DataBox selectCourseseq(RequestBox box) throws Exception {
          DBConnectionManager connMgr = null;
          ListSet ls = null;
          String sql = "";
          DataBox dbox = null;
                  
         String v_coursecode = box.getString("p_coursecode");
         String v_courseyear = box.getString("p_courseyear");
         String v_courseseq = box.getString("p_courseseq");
         try {
              connMgr = new DBConnectionManager();

              sql = "select a.coursecode                                                                                                      \n" +
                    ", a.coursenm                                                                                                             \n" +
                    ", a.isuse                                                                                                                \n" +
                    ", a.courseintro                                                                                                          \n" +
                    ", a.tutorintro                                                                                                           \n" +
                    ", a.target                                                                                                               \n" +
                    ", a.objective                                                                                                            \n" +
                    ", a.content                                                                                                              \n" +
                    ", a.availableperiod                                                                                                      \n" +
                    ", a.addpointname1                                                                                                        \n" +
                    ", a.addpointname2                                                                                                        \n" +
                    ", a.wonlinetest                                                                                                          \n" +
                    ", a.wofflinetest                                                                                                         \n" +
//                    ", a.woffofflinetest                                                                                                      \n" +
//                    ", a.woffoffname1                                                                                                         \n" +
//                    ", a.woffoffname2                                                                                                         \n" +
//                    ", a.woffoffvalue1                                                                                                        \n" +
//                    ", a.woffoffvalue2                                                                                                        \n" +
//                    ", a.wofftotal                                                                                                            \n" +
                    ", wportfolio                                                                                                             \n" +
                    ", wname                                                                                                                  \n" +
                    ", wvalue                                                                                                                 \n" +
                    ", a.wtotal                                                                                                               \n" +
                    ", a.offselectedsubjnum                                                                                                   \n" +
                    ", a.offonlinetest                                                                                                        \n" +
                    ", a.offinterview                                                                                                         \n" +
                    ", a.offofflinetest                                                                                                       \n" +
                    ", a.offname1                                                                                                             \n" +
                    ", a.offname2                                                                                                             \n" +
                    ", a.offvalue1                                                                                                            \n" +
                    ", a.offvalue2                                                                                                            \n" +
                    ", a.courseyear                                                                                                           \n" + 
                    ", a.isonlinecomplete                                                                                                     \n" +
                    ", a.grcode                                                                                                               \n" +
                    ", a.sul                                                                                                                  \n" +
                    ", a.onlinepropstart                                                                                                      \n" +
                    ", a.onlinepropend                                                                                                        \n" +
                    ", a.onlineedustart                                                                                                       \n" +
                    ", a.onlineeduend                                                                                                         \n" +
                    ", a.canceldays                                                                                                           \n" +
                    ", a.courseyear                                                                                                           \n" +   
                    ", a.courseseq                                                                                                            \n" +
                    ", a.year                                                                                                                 \n" +
                    ", a.grseq                                                                                                                \n" +
                    ", b.grcodenm                                                                                                           \n" +
                    ", a.subjnumforcomplete                                                                                                 \n" +
                    "from tz_blcourseseq a, tz_grcode b                                                                                     \n" +
                    "where a.grcode = b.grcode                                                                                              \n" +
                    "and coursecode = " + StringManager.makeSQL(v_coursecode)                                                             +"\n" +
                    "and courseyear = " + StringManager.makeSQL(v_courseyear)                                                             +"\n" +
                    "and courseseq = " + StringManager.makeSQL(v_courseseq);
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
     * 선택된 기수 수정
     */
      public int updateCourseseq(RequestBox box) throws Exception {
         DBConnectionManager connMgr = null;
         PreparedStatement pstmt1 = null;   
         PreparedStatement pstmt2 = null;            
         String sql1 = "";
         String sql2 = "";
         int isOk1 = 1;
         int isOk2 = 1;
         
         String v_coursecode = box.getString("p_coursecode");
         String v_isuse = box.getString("p_isuse");
         String v_courseintro = box.getString("p_courseintro");
         String v_tutorintro = box.getString("p_tutorintro");
         String v_target = box.getString("p_target");
         String v_objective = box.getString("p_objective");
         String v_content = box.getString("p_content");
         int v_availableperiod = box.getInt("p_availableperiod");
         String v_addpointname1 = box.getString("p_addpointname1");
         String v_addpointname2 = box.getString("p_addpointname2");
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
         
         String v_courseyear = box.getString("p_courseyear");
         String v_courseseq = box.getString("p_courseseq");
         
         String v_sulpaper = box.getString("p_sulpaper");
         String v_propstart = box.getString("p_propstart").replaceAll("-", "");
         String v_propend = box.getString("p_propend").replaceAll("-", "");
         String v_onlineedustart = box.getString("p_onlineedustart").replaceAll("-", "");
         String v_onlineeduend = box.getString("p_onlineeduend").replaceAll("-", "");
         
         int v_canceldays = box.getInt("p_canceldays");
         String v_isonlinecomplete = box.getString("p_isonlinecomplete");
         
         String v_selonlinemust = box.getString("p_onlinemustcodes");
         String v_selonlineoption = box.getString("p_onlineoptioncodes");
         String v_selofflinesubj = box.getString("p_offlinesubjcodes");
         String v_copy_gyear = box.getString("p_copy_gyear");
         String v_copy_grseq = box.getString("p_copy_grseq");
         try {
             connMgr = new DBConnectionManager();     
             connMgr.setAutoCommit(false);
             
            sql1 = "update tz_blcourseseq set                               \n" +
                "  isuse = ?                                                \n" +
                ", courseintro  = ?                                         \n" +
                ", tutorintro  = ?                                          \n" +
                ", target = ?                                               \n" +
                ", objective = ?                                            \n" +
                ", content = ?                                              \n" +
                ", availableperiod = ?                                      \n" +
                ", addpointname1 = ?                                        \n" +
                ", addpointname2 = ?                                        \n" +
                ", wonlinetest = ?                                          \n" +
                ", wofflinetest = ?                                         \n" +
//                ", woffofflinetest = ?                                      \n" +
//                ", woffoffname1 = ?                                         \n" +
//                ", woffoffname2 = ?                                         \n" +
//                ", woffoffvalue1 = ?                                        \n" +
//                ", woffoffvalue2 = ?                                        \n" +
//                ", wofftotal = ?                                            \n" +
                ", wportfolio = ?                                           \n" +
                ", wname = ?                                                \n" +
                ", wvalue = ?                                               \n" +
                ", wtotal = ?                                               \n" +
                ", offselectedsubjnum = ?                                   \n" +
                ", offonlinetest = ?                                        \n" +
                ", offinterview = ?                                         \n" +
                ", offofflinetest = ?                                       \n" +
                ", offname1 = ?                                             \n" +
                ", offname2 = ?                                             \n" +
                ", offvalue1 = ?                                            \n" +
                ", offvalue2 = ?                                            \n" + 
                ", luserid = ?                                              \n" +
                ", sul = ?                                                  \n" +
                ", onlinepropstart = ?                                      \n" +
                ", onlinepropend = ?                                        \n" +
                ", onlineedustart = ?                                       \n" +
                ", onlineeduend = ?                                         \n" +
                ", canceldays = ?                                           \n" +
                ", isonlinecomplete = ?                                     \n" +
                ", year = ?                                                 \n" +
                ", grseq = ?                                                \n" +
                ", ldate = to_char(sysdate, 'YYYYMMDDHH24MISS')             \n" +
                "where coursecode = ?                                       \n" +
                "and courseyear = ?                                         \n" +
                "and courseseq = ?                                          \n";
             pstmt1 = connMgr.prepareStatement(sql1);
             
             int params = 1;
             pstmt1.setString(params++, v_isuse); 
             pstmt1.setString(params++, v_courseintro); 
             pstmt1.setString(params++, v_tutorintro);
             pstmt1.setString(params++, v_target); 
             pstmt1.setString(params++, v_objective); 
             pstmt1.setString(params++, v_content); 
             pstmt1.setDouble(params++, v_availableperiod); 
             pstmt1.setString(params++, v_addpointname1); 
             pstmt1.setString(params++, v_addpointname2); 
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
             pstmt1.setString(params++, v_sulpaper);
             pstmt1.setString(params++, v_propstart);
             pstmt1.setString(params++, v_propend);
             pstmt1.setString(params++, v_onlineedustart);
             pstmt1.setString(params++, v_onlineeduend);
             pstmt1.setInt(params++, v_canceldays);
             pstmt1.setString(params++, v_isonlinecomplete);
             pstmt1.setString(params++, v_copy_gyear);
             pstmt1.setString(params++, v_copy_grseq);             
             pstmt1.setString(params++, v_coursecode);
             pstmt1.setString(params++, v_courseyear);
             pstmt1.setString(params++, v_courseseq);
                     
             isOk1 = pstmt1.executeUpdate();
             if ( pstmt1 != null ) { pstmt1.close(); }
             
             if(isOk1 > 0) {
                 sql2 = "delete from tz_blcourseseqsubj where coursecode = ? and courseyear = ? and courseseq = ?";
                 pstmt2 = connMgr.prepareStatement(sql2);
                 pstmt2.setString(1, v_coursecode);
                 pstmt2.setString(2, v_courseyear);
                 pstmt2.setString(3, v_courseseq);
                 isOk2 = pstmt2.executeUpdate(); 
                 
                 if ( pstmt2 != null ) { pstmt2.close(); }
                 
                 this.insertSubj(connMgr, box, v_coursecode, v_courseyear, v_courseseq, v_selonlinemust, "Y", "ON");
                 this.insertSubj(connMgr, box, v_coursecode, v_courseyear, v_courseseq, v_selonlineoption, "N", "ON");
                 this.insertSubj(connMgr, box, v_coursecode, v_courseyear, v_courseseq, v_selofflinesubj, "", "OFF");
             }
             
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
             if(pstmt2 != null) { try { pstmt2.close(); } catch (Exception e1) {} }
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }
         return isOk1;
     }
      
      /**
       * 선택된 과정 삭제
       */
       public int deleteCourseseq(RequestBox box) throws Exception {
           DBConnectionManager connMgr = null;
           PreparedStatement pstmt1 = null;        
           PreparedStatement pstmt2 = null;      
           PreparedStatement pstmt3 = null;        
           String sql1 = "";
           String sql2 = "";
           String sql3 = "";
           int isOk1 = 1;
           int isOk2 = 1;       
           int isOk3 = 1;
           String v_coursecode = box.getString("p_coursecode");    
           String v_courseyear = box.getString("p_courseyear");
           String v_courseseq = box.getString("p_courseseq");
           try {
               connMgr = new DBConnectionManager();           
               connMgr.setAutoCommit(false);
               
               sql3 = "delete from tz_coursebranchinfo        \n"
                   + "where coursecode = ?                    \n"
                   + "and courseyear = ?                      \n"
                   + "and courseseq = ?                       \n";
               
               pstmt3 = connMgr.prepareStatement(sql3);
               pstmt3.setString(1, v_coursecode);
               pstmt3.setString(2, v_courseyear);
               pstmt3.setString(3, v_courseseq);
               
               isOk3 = pstmt3.executeUpdate();
               
               if ( pstmt3 != null ) { pstmt3.close(); }
               
               sql2 = "delete from tz_blcourseseqsubj where coursecode = ? and courseyear = ? and courseseq = ?";
               pstmt2 = connMgr.prepareStatement(sql2);
               pstmt2.setString(1, v_coursecode);
               pstmt2.setString(2, v_courseyear);
               pstmt2.setString(3, v_courseseq);
               isOk2 = pstmt2.executeUpdate();
               if ( pstmt2 != null ) { pstmt2.close(); }
               sql1 = "delete from tz_blcourseseq           \n"
                   + "where coursecode = ?                  \n"
                   + "and courseyear = ?                          \n"
                   + "and courseseq = ?                     \n";
               pstmt1 = connMgr.prepareStatement(sql1);
               pstmt1.setString(1, v_coursecode);
               pstmt1.setString(2, v_courseyear);
               pstmt1.setString(3, v_courseseq);
               isOk1 = pstmt1.executeUpdate();
               if ( pstmt1 != null ) { pstmt1.close(); }
               if (isOk1 > 0) {                     
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
               if(pstmt3 != null) { try { pstmt3.close(); } catch (Exception e) {} }
               if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
           }
           return isOk1;
       }     
       
       public static String selectSubj(String v_grcode, String v_year, String v_grseq, boolean isOn) throws Exception { 
           DBConnectionManager    connMgr = null;
           ListSet             ls      = null;
           String result = null;
           String              sql     = "";
           int cnt = 0;

           result = "";

           try { 
               connMgr = new DBConnectionManager();

               sql = "select b.subj, b.subjnm, b.subjseq, b.year                \n"
                   + "from tz_subj a, tz_subjseq b                              \n"
                   + "where a.subj = b.subj                                     \n"
                   + "and b.gyear = " + StringManager.makeSQL(v_year) + "       \n"
                   + "and b.grseq = " + StringManager.makeSQL(v_grseq) + "      \n"
                   + "and b.grcode = " + StringManager.makeSQL(v_grcode) + "    \n";
               
               if(isOn)
                   sql += "and a.isonoff = 'ON'             \n";
               else
                   sql += "and a.isonoff = 'OFF'             \n";
               
               ls = connMgr.executeQuery(sql);

               while ( ls.next() ) { 

                   result += " <option value=" + ls.getString("subj") + "|"+ls.getString("subjseq")+"|"+ls.getString("year");
                   result += " > " + ls.getString("subjnm") + "</option > \n";
                   cnt++;
               }
           }
           catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex);
               throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
           }
           finally { 
               if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
               if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
           }

           if ( cnt == 0) result = "";
           return result;
       }
       
       /**
        * B/L 기수별 거점 등록
        */
        public int insertBranchInfo(RequestBox box) throws Exception {
            DBConnectionManager connMgr = null;
            ListSet ls = null;
            PreparedStatement pstmt1 = null;        
            String sql1 = "";
            int isOk1 = 1;
            
            String v_coursecode = box.getString("p_coursecode");
            String v_courseyear = box.getString("p_courseyear");
            String v_courseseq = box.getString("p_courseseq");
            String v_branchcode = box.getString("p_branchcode");
            int v_edulimit = box.getInt("p_edulimit");
            int v_biyong = box.getInt("p_biyong");
            int v_monthbiyong = box.getInt("p_monthbiyong");
            int v_month = box.getInt("p_month");
            String v_offlinepropstart = box.getString("p_offlinepropstart").replaceAll("-", "");
            String v_offlinepropend = box.getString("p_offlinepropend").replaceAll("-", "");
            String v_offlineinterviewstart = box.getString("p_offlineinterviewstart").replaceAll("-", "");
            String v_offlineinterviewend = box.getString("p_offlineinterviewend").replaceAll("-", "");
            String v_offlineelectionday = box.getString("p_offlineelectionday").replaceAll("-", "");
            String v_biyongreceivedstart = box.getString("p_biyongreceivedstart").replaceAll("-", "");
            String v_biyongreceivedend = box.getString("p_biyongreceivedend").replaceAll("-", "");
            String v_offlineedustart = box.getString("p_offlineedustart").replaceAll("-", "");
            String v_offlineeduend = box.getString("p_offlineeduend").replaceAll("-", "");
            String v_offlineedustarttime = box.getString("p_offlineedustarttime");
            String v_offlineeduendtime = box.getString("p_offlineeduendtime");
            
            String s_userid = box.getSession("userid");
            
            try {
                connMgr = new DBConnectionManager();         
                connMgr.setAutoCommit(false);
                
                sql1 =  "insert into tz_coursebranchinfo(                   \n" +
                        "  coursecode                                       \n" +
                        ", courseyear                                       \n" +
                        ", courseseq                                        \n" +
                        ", branchcode                                       \n" +
                        ", edulimit                                         \n" +
                        ", biyong                                           \n" +
                        ", monthbiyong                                      \n" +
                        ", month                                            \n" +
                        ", offlinepropstart                                 \n" +
                        ", offlinepropend                                   \n" +
                        ", offlineinterviewstart                            \n" +
                        ", offlineinterviewend                              \n" +
                        ", offlineelectionday                               \n" +
                        ", biyongreceivedstart                              \n" +
                        ", biyongreceivedend                               \n" +
                        ", offlineedustart                                  \n" +
                        ", offlineeduend                                    \n" +
                        ", offlineedustarttime                              \n" +
                        ", offlineeduendtime                                \n" +
                        ", luserid                                          \n" +
                        ", ldate                                            \n" +
                        ") values (                                         \n" +
                        " ?                                                 \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", ?                                                \n" +
                        ", to_char(sysdate, 'YYYYMMDDHH24MISS'))            \n";
                
                pstmt1 = connMgr.prepareStatement(sql1);
                
                int params = 1;
                
                pstmt1.setString(params++, v_coursecode);
                pstmt1.setString(params++, v_courseyear);
                pstmt1.setString(params++, v_courseseq);
                pstmt1.setString(params++, v_branchcode);
                pstmt1.setInt(params++, v_edulimit);
                pstmt1.setInt(params++, v_biyong);
                pstmt1.setInt(params++, v_monthbiyong);
                pstmt1.setInt(params++, v_month);
                pstmt1.setString(params++, v_offlinepropstart);
                pstmt1.setString(params++, v_offlinepropend);
                pstmt1.setString(params++, v_offlineinterviewstart);
                pstmt1.setString(params++, v_offlineinterviewend);
                pstmt1.setString(params++, v_offlineelectionday);
                pstmt1.setString(params++, v_biyongreceivedstart);
                pstmt1.setString(params++, v_biyongreceivedend);
                pstmt1.setString(params++, v_offlineedustart);
                pstmt1.setString(params++, v_offlineeduend);
                pstmt1.setString(params++, v_offlineedustarttime);
                pstmt1.setString(params++, v_offlineeduendtime);
                pstmt1.setString(params++, s_userid);
                
                isOk1 = pstmt1.executeUpdate();
                if ( pstmt1 != null ) { pstmt1.close(); }
                if(isOk1 > 0) {
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
                if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }            
            }
            return isOk1;
        }
       
        /**
        * B/L 기수별 거점 정보 목록
        */
        public ArrayList selectCourseBrachInfoList(RequestBox box) throws Exception {
            DBConnectionManager connMgr = null;
            PreparedStatement pstmt = null;        
            ListSet ls = null;
            ArrayList list = null;
            String sql = "";
            DataBox dbox = null;
            
            String v_coursecode = box.getString("p_coursecode");
            String v_courseyear = box.getString("p_courseyear");
            String v_courseseq = box.getString("p_courseseq");
            
            try {
                connMgr = new DBConnectionManager();            
                
                list = new ArrayList();
                
                sql = "select                                           \n" +
                    "  a.coursecode                                       \n" +
                    ", a.courseyear                                       \n" +
                    ", a.courseseq                                        \n" +
                    ", a.branchcode                                       \n" +
                    ", a.edulimit                                         \n" +
                    ", a.biyong                                           \n" +
                    ", a.monthbiyong                                      \n" +
                    ", a.month                                            \n" +
                    ", a.offlinepropstart                                 \n" +
                    ", a.offlinepropend                                   \n" +
                    ", a.offlineinterviewstart                            \n" +
                    ", a.offlineinterviewend                              \n" +
                    ", a.offlineelectionday                               \n" +
                    ", a.biyongreceivedstart                              \n" +
                    ", a.biyongreceivedend                                \n" +
                    ", a.offlineedustart                                  \n" +
                    ", a.offlineeduend                                    \n" +
                    ", a.offlineedustarttime                              \n" +
                    ", a.offlineeduendtime                                \n" +
                    ", b.branchnm                                         \n" +
                    "from tz_coursebranchinfo a, tz_branch b              \n" +
                    "where a.branchcode = b.branchcode                    \n" +
                    "and coursecode = " + StringManager.makeSQL(v_coursecode) + "\n" +
                    "and courseyear = " + StringManager.makeSQL(v_courseyear)   + "\n" +
                    "and courseseq = " + StringManager.makeSQL(v_courseseq)     + "\n";
                
                /*if ( v_orderColumn.equals("") ) { 
                    sql += " order by a.coursenm                                                           \n";
                } else { 
                    sql += " order by " + v_orderColumn + v_orderType + "                                  \n";
                }*/
                
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
         * 선택된 기수별 거점 삭제
         */
         public int deleteCourseBranchInfo(RequestBox box) throws Exception {
             DBConnectionManager connMgr = null;
             PreparedStatement pstmt1 = null;        
             String sql1 = "";
             int isOk1 = 1;
             
             String v_coursecode = box.getString("p_coursecode");
             String v_courseyear = box.getString("p_courseyear");
             String v_courseseq = box.getString("p_courseseq");
             String v_branchcode = box.getString("p_selbranchcode");
             
             try {
                 connMgr = new DBConnectionManager();           
                 connMgr.setAutoCommit(false);
                 
                 sql1 = "delete from tz_coursebranchinfo        \n"
                     + "where coursecode = ?                    \n"
                     + "and courseyear = ?                      \n"
                     + "and courseseq = ?                       \n"
                     + "and branchcode = ?                      \n"; 
                 
                 pstmt1 = connMgr.prepareStatement(sql1);
                 pstmt1.setString(1, v_coursecode);
                 pstmt1.setString(2, v_courseyear);
                 pstmt1.setString(3, v_courseseq);
                 pstmt1.setString(4, v_branchcode);
                 
                 isOk1 = pstmt1.executeUpdate();
                 if ( pstmt1 != null ) { pstmt1.close(); }
                 if (isOk1 > 0) {                     
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
                 if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
             }
             return isOk1;
         }     
        
         /**
          * 선택된 기수별 거점 수정
          */
           public int updateCourseBranchInfo(RequestBox box) throws Exception {
              DBConnectionManager connMgr = null;
              PreparedStatement pstmt1 = null;   
              String sql1 = "";
              int isOk1 = 1;
              
              int v_index = box.getInt("p_index");
              String v_coursecode = box.getString("p_coursecode");
              String v_courseyear = box.getString("p_courseyear");
              String v_courseseq = box.getString("p_courseseq");
              String v_branchcode = box.getString("p_branchcode" + "_" + v_index);
              int v_edulimit = box.getInt("p_edulimit" + "_" + v_index);
              int v_biyong = box.getInt("p_biyong" + "_" + v_index);
              int v_monthbiyong = box.getInt("p_monthbiyong" + "_" + v_index);
              int v_month = box.getInt("p_month" + "_" + v_index);
              String v_offlinepropstart = box.getString("p_offlinepropstart" + "_" + v_index).replaceAll("-", "");
              String v_offlinepropend = box.getString("p_offlinepropend" + "_" + v_index).replaceAll("-", "");
              String v_offlineinterviewstart = box.getString("p_offlineinterviewstart" + "_" + v_index).replaceAll("-", "");
              String v_offlineinterviewend = box.getString("p_offlineinterviewend" + "_" + v_index).replaceAll("-", "");
              String v_offlineelectionday = box.getString("p_offlineelectionday" + "_" + v_index).replaceAll("-", "");
              String v_biyongreceivedstart = box.getString("p_biyongreceivedstart" + "_" + v_index).replaceAll("-", "");
              String v_biyongreceivedend = box.getString("p_biyongreceivedend" + "_" + v_index).replaceAll("-", "");
              String v_offlineedustart = box.getString("p_offlineedustart" + "_" + v_index).replaceAll("-", "");
              String v_offlineeduend = box.getString("p_offlineeduend" + "_" + v_index).replaceAll("-", "");
              String v_offlineedustarttime = box.getString("p_offlineedustarttime" + "_" + v_index);
              String v_offlineeduendtime = box.getString("p_offlineeduendtime" + "_" + v_index);
              
              String s_userid = box.getSession("userid");
              
              try {
                  connMgr = new DBConnectionManager();     
                  connMgr.setAutoCommit(false);
                  
                 sql1 = "update tz_coursebranchinfo set                      \n" +
                     "  edulimit  = ?                                        \n" +
                     ", biyong  = ?                                          \n" +
                     ", monthbiyong = ?                                      \n" +
                     ", month = ?                                            \n" +
                     ", offlinepropstart = ?                                 \n" +
                     ", offlinepropend = ?                                   \n" +
                     ", offlineinterviewstart = ?                            \n" +
                     ", offlineinterviewend = ?                              \n" +
                     ", offlineelectionday = ?                               \n" +
                     ", biyongreceivedstart = ?                              \n" +
                     ", biyongreceivedend = ?                                \n" +
                     ", offlineedustart = ?                                  \n" +
                     ", offlineeduend = ?                                    \n" +
                     ", offlineedustarttime = ?                              \n" +
                     ", offlineeduendtime = ?                                \n" +
                     ", luserid = ?                                          \n" +
                     ", ldate = to_char(sysdate, 'YYYYMMDDHH24MISS')         \n" +
                     "where coursecode = ?                                   \n" +
                     "and courseyear = ?                                     \n" +  
                     "and courseseq = ?                                      \n" +  
                     "and branchcode = ?                                     \n";  
                  pstmt1 = connMgr.prepareStatement(sql1);
                  
                  int params = 1;
                  
                  pstmt1.setInt(params++, v_edulimit);
                  pstmt1.setInt(params++, v_biyong);
                  pstmt1.setInt(params++, v_monthbiyong);
                  pstmt1.setInt(params++, v_month);
                  pstmt1.setString(params++, v_offlinepropstart);
                  pstmt1.setString(params++, v_offlinepropend);
                  pstmt1.setString(params++, v_offlineinterviewstart);
                  pstmt1.setString(params++, v_offlineinterviewend);
                  pstmt1.setString(params++, v_offlineelectionday);
                  pstmt1.setString(params++, v_biyongreceivedstart);
                  pstmt1.setString(params++, v_biyongreceivedend);
                  pstmt1.setString(params++, v_offlineedustart);
                  pstmt1.setString(params++, v_offlineeduend);
                  pstmt1.setString(params++, v_offlineedustarttime);
                  pstmt1.setString(params++, v_offlineeduendtime);
                  pstmt1.setString(params++, s_userid);
                  pstmt1.setString(params++, v_coursecode);
                  pstmt1.setString(params++, v_courseyear);
                  pstmt1.setString(params++, v_courseseq);
                  pstmt1.setString(params++, v_branchcode);
                  
                          
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
        * B/L 기수의 온라인/오프라인 과목
        */
       public ArrayList selectcourseseqsubj(RequestBox box, String v_isrequired, String v_isonoff) throws Exception {
           DBConnectionManager connMgr = null;
           PreparedStatement pstmt = null;        
           ListSet ls = null;
           ArrayList list = null;
           String sql = "";
           DataBox dbox = null;
           String v_coursecode = box.getString("p_coursecode");
           String v_courseyear = box.getString("p_courseyear");
           String v_courseseq = box.getString("p_courseseq");
           
           try {
               connMgr = new DBConnectionManager();            
               
               list = new ArrayList();
            
            sql = "select                                                           \n"
                + "a.subj                                                           \n"
                + ", a.year                                                         \n"
                + ", a.subjseq                                                      \n"
                + ", b.subjnm                                                       \n"
                + "from tz_blcourseseqsubj a, tz_subjseq b                          \n"
                + "where a.subj = b.subj                                            \n"
                + "and a.year = b.year                                              \n"
                + "and a.subjseq = b.subjseq                                        \n"
                + "and a.coursecode = " + StringManager.makeSQL(v_coursecode) + "   \n"
                + "and a.courseyear = " + StringManager.makeSQL(v_courseyear) + "   \n"
                + "and a.courseseq = " + StringManager.makeSQL(v_courseseq) + "     \n";
                if(!"".equals(v_isrequired)) 
                        sql+= "and a.isrequired = " + StringManager.makeSQL(v_isrequired) + "   \n";
                sql+= "and a.isonoff = " + StringManager.makeSQL(v_isonoff) + "         \n";
                  
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
        * B/L기수 과목별 평가배점 조회
        */
        public ArrayList selectSubjPercentage(RequestBox box) throws Exception {
              DBConnectionManager connMgr = null;
              ListSet ls = null;
              ArrayList list = null;
              DataBox dbox = null;
              String sql = "";
             
                      
             String v_coursecode = box.getString("p_coursecode");
             String v_courseyear = box.getString("p_courseyear");
             String v_courseseq = box.getString("p_courseseq");
             String v_isonoff = box.getString("p_isonoff");
             try {
                  connMgr = new DBConnectionManager();

                  list = new ArrayList();

                  sql = "select b.subjnm                                              \n"
                      + ", a.isrequired                                               \n"  
                      + ", wstep                                                      \n"
                      + ", wmtest                                                     \n"
                      + ", wftest                                                     \n"
                      + ", whtest                                                     \n"
                      + ", wreport                                                    \n"
                      + ", wetc1                                                      \n"
                      + ", wetc2                                                      \n"
                      + "from tz_blcourseseqsubj a, tz_subjseq b                      \n"
                      + "where a.subj = b.subj                                        \n"
                      + "and a.subjseq = b.subjseq                                    \n"
                      + "and a.year = b.year                                          \n"
                      + "and isonoff = " +  StringManager.makeSQL(v_isonoff)        + "\n"   
                      + "and a.coursecode = " + StringManager.makeSQL(v_coursecode) + "\n" 
                      + "and a.courseyear = " + StringManager.makeSQL(v_courseyear) + "\n" 
                      + "and a.courseseq = " + StringManager.makeSQL(v_courseseq)   + "\n";
                  
                  sql += "order by a.isrequired desc, b.subjnm                         \n";
                  ls = connMgr.executeQuery(sql); 
     
                  while(ls.next()) {
                      dbox = ls.getDataBox();
                      dbox.put("d_wstep", new Double(ls.getDouble("wstep")));
                      dbox.put("d_wmtest", new Double(ls.getDouble("wmtest")));
                      dbox.put("d_wftest", new Double(ls.getDouble("wftest")));
                      dbox.put("d_whtest", new Double(ls.getDouble("whtest")));
                      dbox.put("d_wreport", new Double(ls.getDouble("wreport")));
                      dbox.put("d_wetc1", new Double(ls.getDouble("wetc1")));
                      dbox.put("d_wetc2", new Double(ls.getDouble("wetc2")));
                      list.add(dbox);
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
              return list;
          }  
        
        
        /**
        * 거점 목록
        */
        public ArrayList selectBrachList(RequestBox box) throws Exception {
            DBConnectionManager connMgr = null;
            ListSet ls = null;
            ArrayList list = null;
            DataBox dbox = null;
            String sql = "";
             
            try {
                connMgr = new DBConnectionManager();

                list = new ArrayList();

                sql = "select branchcode, branchnm                  \n"
                    + "from tz_branch                               \n";
                    
                
                ls = connMgr.executeQuery(sql); 
  
                while(ls.next()) {
                    dbox = ls.getDataBox();
                    list.add(dbox);
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
            return list;
        }  
}