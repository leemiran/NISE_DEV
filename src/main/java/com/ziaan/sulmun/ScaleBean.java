//**********************************************************
//1. 제      목: Scale Bean
//2. 프로그램명: ScaleBean.java
//3. 개      요:
//4. 환      경: JDK 1.3
//5. 버      젼: 0.1
//6. 작      성: Administrator 2003-08-18
//7. 수      정:
//
//**********************************************************

package com.ziaan.sulmun;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import com.ziaan.library.*;
import com.ziaan.system.*;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ScaleBean {
    public final static String SPLIT_COMMA = ",";
    public final static String SPLIT_COLON = ":";

    public final static String DEFAULT_GRCODE = "N000001";
    public final static String DEFAULT_TYPE   = "S";

    public ScaleBean() {}

    /**
    @param box          receive from the form object and session
    @return ArrayList   설문문제 리스트
    */
    public ArrayList selectScaleList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ArrayList list = null;

        String v_grcode = box.getString("p_grcode");
        String v_scaletype = box.getStringDefault("p_scaletype", ScaleBean.DEFAULT_TYPE);

        try {
                connMgr = new DBConnectionManager();
                list = selectScaleList(connMgr, v_grcode, v_scaletype);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        finally {
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }



    /**
    @param box          receive from the form object and session
    @return ArrayList   설문문제 리스트
    */
    public ArrayList selectScaleList(DBConnectionManager connMgr, String p_grcode, String p_scaletype) throws Exception {
        ArrayList list = new ArrayList();
        ListSet ls = null;
        String sql  = "";
        DataBox dbox = null;

        try {
            sql = "select a.scalecode,  a.grcode, ";
            sql+= "       a.s_gubun, a.scaletype, a.scalename ";
            sql+= "  from tu_scale    a ";
            sql+= " where a.isdel = 'N' ";
   //         sql+= "   and a.grcode    = " + SQLString.Format(p_grcode);
			sql+= "   and a.scaletype    = " + SQLString.Format(p_scaletype);
            sql+= " order by a.ldate desc ";

            ls = connMgr.executeQuery(sql);

			while (ls.next()) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        return list;
    }

    /**
    @param box          receive from the form object and session
    @return QuestionExampleData   설문문제
    */
    public ArrayList selectScaleExample(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ArrayList list = null;

        String v_grcode = box.getString("p_grcode");
        String v_scaletype = box.getStringDefault("p_scaletype", ScaleBean.DEFAULT_TYPE);
        int    v_scalecode   = box.getInt("p_scalecode");
        
		try {
            if (v_scalecode > 0) {
                connMgr = new DBConnectionManager();
                list = getSelnums(connMgr, v_scalecode);
            } else {
                list = new ArrayList();
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        finally {
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

    /**
    @param box          receive from the form object and session
    @return QuestionExampleData   설문문제
    */
    public ArrayList selectScaleGubunExample(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ArrayList list = null;

        int    v_scalecode   = box.getInt("p_scalecode");
        
		try {
            if (v_scalecode > 0) {
                connMgr = new DBConnectionManager();
                list = getSelnums(connMgr, v_scalecode);
            } else {
                list = new ArrayList();
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        finally {
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

    public ArrayList getSelnums(DBConnectionManager connMgr, int p_scalecode) throws Exception {
        ArrayList list = new ArrayList();
        ListSet ls = null;
        String sql  = "";
        DataBox dbox = null;
		
        try {
            sql = "select a.scalecode, a.s_gubun, a.scalename, b.selnum, b.selpoint, b.seltext ";
            sql+= "  from tu_scale     a, ";
            sql+= "       tu_scalesel  b  ";
            sql+= " where a.scalecode   = b.scalecode(+)    ";
            sql+= "   and a.scalecode = " +   SQLString.Format(p_scalecode);
            sql+= " order by b.selnum ";

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {

                dbox = ls.getDataBox();
                list.add(dbox);
            }      
		}
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        return list;
    }




    public int insertTZ_scale(DBConnectionManager connMgr, int p_scalecode, String p_grcode, String p_sgubun, String p_scaletype, String p_scalename, String p_luserid) throws Exception {
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;

        try {
            //insert tu_scale table
            sql =  "insert into tu_scale(scalecode, grcode, s_gubun, scaletype, scalename, luserid, ldate, isdel) ";
            sql+=  " values (?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setInt(1, p_scalecode);
            pstmt.setString(2, p_grcode);
            pstmt.setString(3, p_sgubun);
            pstmt.setString(4, p_scaletype);
            pstmt.setString(5, p_scalename);
            pstmt.setString(6, p_luserid);
            pstmt.setString(7, FormatDate.getDate("yyyyMMddHHmmss"));
            pstmt.setString(8, "N");

            isOk = pstmt.executeUpdate();
       }
       catch(Exception ex) {
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
       }
       finally {
           if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
       }
       return isOk;
    }

    public int updateTZ_scale(DBConnectionManager connMgr, int p_scalecode, String p_grcode, String p_sgubun, String p_scaletype, String p_scalename, String p_luserid) throws Exception {
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;

        try {
            //update tu_scale table
            sql =  " update tu_scale ";
            sql+=  "    set s_gubun = ?, ";
            sql+=  "        scaletype  = ?, ";
            sql+=  "        scalename  = ?, ";
            sql+=  "        luserid  = ?, ";
            sql+=  "        ldate    = ?  ";
            sql+=  "  where scalecode     = ?  ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, p_sgubun);
            pstmt.setString(2, p_scaletype);
            pstmt.setString(3, p_scalename);
            pstmt.setString(4, p_luserid);
            pstmt.setString(5, FormatDate.getDate("yyyyMMddHHmmss"));
            pstmt.setInt   (6, p_scalecode);

            isOk = pstmt.executeUpdate();
       }
       catch(Exception ex) {
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
       }
       finally {
           if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
       }
       return isOk;
    }

    public int deleteTZ_scale(DBConnectionManager connMgr, int p_scalecode, String duserid) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;  
        String sql = "";
        int isOk = 0;

        try {
            //update tu_scale table
        /*    sql =  " update tu_scale ";
            sql+=  "    set isdel = ?, ";
            sql+=  "        duserid  = ?, ";
            sql+=  "        ddate    = ?  ";
            sql+=  "  where scalecode     = ?  ";*/
            sql = " select sulnum from tu_sul where scalecode=? ";
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setInt   (1, p_scalecode);
            rs = pstmt.executeQuery();            
    		if (rs.next()) {
    		    isOk = -2;
    		}
            
            if(isOk==0){            
                sql = " delete tu_scale";
                sql+=  "  where scalecode     = ?  ";
    
                pstmt = connMgr.prepareStatement(sql);
      //          pstmt.setString(1, "Y");
      //          pstmt.setString(2, duserid);
      //          pstmt.setString(3, FormatDate.getDate("yyyyMMddHHmmss"));
                pstmt.setInt   (1, p_scalecode);
    
                isOk = pstmt.executeUpdate();
            }
       }
       catch(Exception ex) {
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
       }
       finally {
           if(rs != null) { try { rs.close(); } catch (Exception e) {} }        
           if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
       }
       return isOk;
    }

    public int insertTZ_scalesel(PreparedStatement pstmt, int p_scalecode, int p_selnum, int p_selpoint, String p_seltext, String p_luserid) throws Exception {
        int isOk = 0;

        try {
            pstmt.setInt(1, p_scalecode);
            pstmt.setInt   (2, p_selnum);
            pstmt.setInt   (3, p_selpoint);
			pstmt.setString(4, p_seltext);
  			pstmt.setString(5, p_luserid);
            pstmt.setString(6, FormatDate.getDate("yyyyMMddHHmmss"));
            isOk = pstmt.executeUpdate();
       }
       catch(Exception ex) {
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception(ex.getMessage());
       }
       return isOk;
    }

    public int deleteTZ_scalesel(DBConnectionManager connMgr,  int p_scalecode) throws Exception {
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;

        try {
            //delete TZ_SUL table
            sql =  " delete from tu_scaleSEL ";
            sql+=  "  where scalecode     = ?  ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setInt   (1, p_scalecode);

            isOk = pstmt.executeUpdate();
       }
       catch(Exception ex) {
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception(ex.getMessage());
       }
       finally {
           if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
       }
       return isOk;
    }

    public int insertScale(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;

        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;
		int v_scalecode    = 0;

        String v_scaletype    = box.getStringDefault("p_scaletype", ScaleBean.DEFAULT_TYPE);
        String v_grcode       = box.getString("p_grcode");

		String v_sgubun    = box.getString("p_sgubun");
        String v_scalename    = box.getString("p_scalename");

        int    v_selnum     = 0;
		int v_selpoint    = 0;
        Vector v_selpoints   = null;
        String v_seltext    = "";
        Vector v_seltexts   = null;
		Enumeration em1      = null;
		Enumeration em2     = null;
        
		if (v_sgubun.equals("5")) {
        v_seltexts   = box.getVector("p_seltext1");
        v_selpoints   = box.getVector("p_selpoint1");
        em1      = v_seltexts.elements();
        em2      = v_selpoints.elements();
		} else if (v_sgubun.equals("7")) {
        v_seltexts   = box.getVector("p_seltext2");
        v_selpoints   = box.getVector("p_selpoint2");
        em1      = v_seltexts.elements();
        em2      = v_selpoints.elements();
		} 
        
		String v_luserid    = box.getSession("userid");

        try {
            v_scalecode = getScaleSeq();

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
			
            isOk = insertTZ_scale(connMgr, v_scalecode, v_grcode,  v_sgubun, v_scaletype, v_scalename, v_luserid);

            sql =  "insert into tu_scaleSEL(scalecode, selnum, selpoint, seltext, luserid, ldate) ";
            sql+=  " values (?, ?, ?, ?, ?, ?)";
            pstmt = connMgr.prepareStatement(sql);
            while(em1.hasMoreElements() && em2.hasMoreElements()){
                v_seltext   = (String)em1.nextElement();
                v_selpoint   = Integer.parseInt((String)em2.nextElement());

                if (!v_seltext.trim().equals("")) {
                    v_selnum++;
                    isOk = insertTZ_scalesel(pstmt, v_scalecode, v_selnum, v_selpoint, v_seltext, v_luserid);
                }
            }
       }
       catch(Exception ex) {
           isOk = 0;
           connMgr.rollback();
           ErrorManager.getErrorStackTrace(ex, box, sql);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
       }
       finally {
           if (isOk > 0) {connMgr.commit();}
           if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
           if(connMgr != null) { try { connMgr.setAutoCommit(true);  connMgr.freeConnection(); }catch (Exception e10) {} }
       }
       return isOk;
   }

   public int updateScale(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;

        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;

		int v_scalecode    = box.getInt("p_scalecode");

        String v_scaletype    = box.getString("p_scaletype");
        String v_grcode       = box.getString("p_grcode");

		String v_sgubun    = box.getString("p_sgubun");
        String v_scalename    = box.getString("p_scalename");

        int    v_selnum     = 0;
		int v_selpoint    = 0;
        Vector v_selpoints   = null;
        String v_seltext    = "";
        Vector v_seltexts   = null;
		Enumeration em1      = null;
		Enumeration em2     = null;
        
		if (v_sgubun.equals("5")) {
        v_seltexts   = box.getVector("p_seltext1");
        v_selpoints   = box.getVector("p_selpoint1");
        em1      = v_seltexts.elements();
        em2      = v_selpoints.elements();
		} else if (v_sgubun.equals("7")) {
        v_seltexts   = box.getVector("p_seltext2");
        v_selpoints   = box.getVector("p_selpoint2");
        em1      = v_seltexts.elements();
        em2      = v_selpoints.elements();
		} 
        
		String v_luserid    = box.getSession("userid");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

			isOk = updateTZ_scale(connMgr, v_scalecode, v_grcode,  v_sgubun, v_scaletype, v_scalename, v_luserid);
            isOk = deleteTZ_scalesel(connMgr, v_scalecode);

            sql =  "insert into tu_scaleSEL(scalecode, selnum, selpoint, seltext, luserid, ldate) ";
            sql+=  " values (?, ?, ?, ?, ?, ?)";
            pstmt = connMgr.prepareStatement(sql);
            while(em1.hasMoreElements() && em2.hasMoreElements()){
                v_seltext   = (String)em1.nextElement();
                v_selpoint   = Integer.parseInt((String)em2.nextElement());

                if (!v_seltext.trim().equals("")) {
                    v_selnum++;
                    isOk = insertTZ_scalesel(pstmt, v_scalecode, v_selnum, v_selpoint, v_seltext, v_luserid);
                }
            }
		}
        catch(Exception ex) {
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if (isOk > 0) {connMgr.commit();}
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }

    public int deleteScale(RequestBox box) throws Exception {
         DBConnectionManager connMgr = null;
         int isOk = 0;

		int v_scalecode    = box.getInt("p_scalecode");
		String v_duserid    = box.getSession("userid");

         try {
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false);

             isOk = deleteTZ_scale(connMgr, v_scalecode, v_duserid);
             if(isOk>0){
                isOk = deleteTZ_scalesel(connMgr, v_scalecode);
             }   
         }
         catch(Exception ex) {
             isOk = 0;
             connMgr.rollback();
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception(ex.getMessage());
         }
         finally {
             if (isOk > 0) {connMgr.commit(); box.put("p_scalecode", String.valueOf("0"));}
             if (connMgr != null) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); }catch (Exception e10) {} }
         }
         return isOk;
     }

    public int getScaleSeq() throws Exception {
        Hashtable maxdata = new Hashtable();
        maxdata.put("seqcolumn","scalecode");
        maxdata.put("seqtable","tu_scale");
        maxdata.put("paramcnt","0");

        return SelectionUtil.getSeq(maxdata);
    }

    public String getGrcode(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq)  throws Exception {
        String v_grcode = "";
        ListSet ls = null;
        String sql  = "";
        try {
            sql = "select grcode ";
            sql+= "  from tz_subjseq  ";
            sql+= " where subj    = " + SQLString.Format(p_subj);
            sql+= "   and year    = " + SQLString.Format(p_year);
            sql+= "   and subjseq = " + SQLString.Format(p_subjseq);

            ls = connMgr.executeQuery(sql);
            while (ls.next()) {
                v_grcode = ls.getString("grcode");
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        return v_grcode;
    }

    /**
    *  척도 코드 셀렉트박스 (GUBUN,교육주관,TYPE,셀렉트박스명,선택값,이벤트명)
	*  TZ_SCALE 이용
    */
    public static String getScaleCodeSelect (String s_gubun, String grcode, String scaletype, String name, int selected, String event) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String result = null;
        String sql = "";
        DataBox dbox = null;

        result = "  <SELECT name=" + name + " " + event + " > ";

        result += " <option value='0'>척도를 선택하세요.</option> ";

        try {
            connMgr = new DBConnectionManager();

            sql  = " select scalecode, scalename from tu_scale            ";
            sql += "  where s_gubun  = " + StringManager.makeSQL(s_gubun);
            sql += "    and scaletype = " + StringManager.makeSQL(scaletype);
       //     sql += "    and grcode = " + StringManager.makeSQL(grcode);
            sql += "    and isdel = 'N'" ;
            sql += " order by scalecode asc";

			ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();

                result += " <option value=" + dbox.getInt("d_scalecode");
                if (selected==dbox.getInt("d_scalecode")) {
                    result += " selected ";
                }
                
                result += ">" + dbox.getString("d_scalename") + "</option> ";
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }

        result += "  </SELECT> ";
        return result;
    } 
    
    /**
    @param box          receive from the form object and session
    @return ArrayList   척도문제
    */
    public DataBox selectScale(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        DataBox dbox = null;
        String sql = "";

        String v_sulpapernum = box.getString("p_sulpapernum");

        try {
          db = new DatabaseExecute();

          sql = "select scalecode, s_gubun, scalename ";
          sql+= "  from tu_scale  ";
          sql+= " where scalecode = " +   SQLString.Format(box.getInt("p_scalecode"));
          
          dbox = db.executeQuery(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return dbox;
    }
    
    //척도 보기 목록
    public ArrayList selectScaleOption(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
    	ArrayList list = new ArrayList();
        ListSet ls = null;
        String sql  = "";
        DataBox dbox = null;

        try {
            sql = "select scalecode, seltext, selpoint ";
            sql+= "  from tu_scalesel  ";
            sql+= " where scalecode = " +   SQLString.Format(box.getInt("p_scalecode"));
            sql+= " order by b.selnum ";

            ls = connMgr.executeQuery(sql);
 
		   while (ls.next()) {
                    dbox = ls.getDataBox();
                    list.add(dbox);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        return list;
    }
}