//**********************************************************
//1. 제      목:
//2. 프로그램명: SulmunPaperBean.java
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
import com.ziaan.sulmun.*;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SulmunPaperBean {


    public SulmunPaperBean() {}

    /**
    @param box          receive from the form object and session
    @return ArrayList   설문문제 리스트
    */
    public ArrayList selectQuestionList(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        ArrayList list = null;
        String sql = "";

        String v_subj = box.getString("p_subj");
        
        String v_searchtext = box.getString("p_searchtext");
        String v_select = box.getString("p_select");

        try {
          db = new DatabaseExecute();

          sql+="\r\n select                ";
          sql+="\r\n   sulnum,  subj, distcode, sultype,";
          sql+="\r\n   sultext, selcount, selmax, sulreturn,";
          sql+="\r\n   scalecode, get_codenm('0008', distcode) distcodenm,";
          sql+="\r\n   get_codenm('0009', sultype) sultypenm";
          sql+="\r\n from tu_sul ";
          sql+="\r\n where subj='"+v_subj+"' ";          	  
          list = db.executeQueryList(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }
    


  /**
    @param box          receive from the form object and session
    @return ArrayList   설문문제 리스트
    */
    public ArrayList selectPaperQuestionList(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        ArrayList list = null;
        String sql = "";

        String v_select = box.getString("p_select");
        int    v_sulpapernum  = box.getInt("p_sulpapernum");

        try {
          db = new DatabaseExecute();
          
          
          sql+= " select     ";
          sql+= "   a.sulnum, a.subj, a.sultype, a.sultext, a.distcode, ";
          sql+= "   get_codenm('0008', a.distcode) distcodenm, get_codenm('0009', a.sultype) sultypenm, sulreturn, selmax    ";
          sql+= " from  ";
          sql+= "   tu_sul a, TU_SULPAPERQUESTION b   ";
          sql+= " where ";
          sql+= "   a.sulnum = b.sulnum ";
          sql+= "   and b.sulpapernum = "+v_sulpapernum;
          sql+= " order by b.orders ";
          
          
          list = db.executeQueryList(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }
    
    /**
    @param box          receive from the form object and session
    @return ArrayList   설문문제
    */
    public DataBox selectPaper(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        DataBox dbox = null;
        String sql = "";

        String v_sulpapernum = box.getString("p_sulpapernum");

        try {
          db = new DatabaseExecute();

          sql =" select                ";
          sql+="   sulpapernum, sulpapernm, sulstart, sulend, suljoinusertype ";
          sql+=" from tu_sulpaper";
          sql+=" where ";
          sql+="   sulpapernum ="+v_sulpapernum;
          
          dbox = db.executeQuery(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return dbox;
    }

    /**
    @param box          receive from the form object and session
    @return ArrayList   설문에 참여한 총인원수
    */
    public DataBox selectTotalNumberOfJoiner(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        DataBox dbox = null;
        String sql = "";

        String v_sulpapernum = box.getString("p_sulpapernum");

        try {
          db = new DatabaseExecute();

          sql =" select count(distinct userid) total from tu_suleach where sulpapernum='"+v_sulpapernum+"' ";
          
          dbox = db.executeQuery(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return dbox;
    }

    /**
    @param box          receive from the form object and session
    @return ArrayList   설문문제 리스트
    */
    public ArrayList selectPaperQuestionList(DBConnectionManager connMgr, String p_grcode, String p_subj, int p_sulpapernum, RequestBox box) throws Exception {
        ArrayList list = new ArrayList();
        Hashtable hash = new Hashtable();
        StringTokenizer st = null;

        ListSet ls = null;
        String sql  = "";
        DataBox dbox = null;

        String v_sulnums = "";
        String v_sulnum  = "";
        String v_sulpapernm = "";
		int v_progresslimit = 0;

        try {
            sql = "select sulpapernm, totcnt, sulnums, progresslimit";
            sql+= "  from tu_sulpaper ";
            sql+= " where grcode      = " + SQLString.Format(p_grcode);
            sql+= " and subj        = " + SQLString.Format(p_subj);
            sql+= "   and sulpapernum = " + SQLString.Format(p_sulpapernum);
			ls = connMgr.executeQuery(sql);
            while (ls.next()) {
                v_sulnums = ls.getString("sulnums");
                v_sulpapernm = ls.getString("sulpapernm");
                v_progresslimit = ls.getInt("progresslimit");
            }
            if (box != null) {
                box.put("p_sulpapernm",v_sulpapernm);//System.out.println("v_sulpapernm" +v_sulpapernm);
                box.put("p_progresslimit",String.valueOf(v_progresslimit));//System.out.println("v_progresslimit" +String.valueOf(v_progresslimit));
            }

            if (v_sulnums.length() > 0) {
                sql = "select a.subj,     a.sulnum,  ";
                sql+= "       a.distcode, b.codenm  distcodenm, ";
                sql+= "       a.sultype,  c.codenm  sultypenm,  ";
                sql+= "       a.sultext    ";
                sql+= "  from tu_sul    a, ";
                sql+= "       tu_code   b, ";
                sql+= "       tu_code   c  ";
                sql+= " where a.distcode = b.code ";
                sql+= "   and a.sultype  = c.code ";
                sql+= "   and b.gubun    = " + SQLString.Format(SulmunBean.DIST_CODE);
                sql+= "   and c.gubun    = " + SQLString.Format(SulmunBean.SUL_TYPE);
                sql+= "   and a.subj     = " + SQLString.Format(p_subj);
				sql+= "   and c.levels  =  1 ";
                if (v_sulnums.equals("")) v_sulnums = "-1";
                sql+= "   and a.sulnum in (" + v_sulnums + ")";

                sql+= " order by a.sulnum ";

                ls.close();
                ls = connMgr.executeQuery(sql);

                st = new StringTokenizer(v_sulnums,SulmunBean.SPLIT_COMMA);

                while (ls.next()) {
                    dbox = ls.getDataBox();

                    v_sulnum = String.valueOf(ls.getInt("sulnum"));

                    hash.put(v_sulnum, dbox);
                }

                while (st.hasMoreElements()) {
                    v_sulnum = (String)st.nextToken();
                    dbox = (DataBox)hash.get(v_sulnum);
                    if (dbox != null) {
                        list.add(dbox);
                    }
                }
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
    @return ArrayList   설문문제지 리스트
    */
    public ArrayList selectPaperList(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        ArrayList list =  new ArrayList();
        String sql  = "";
        DataBox     dbox = null;

        try {
          db = new DatabaseExecute();
        	
          sql =  getPaperListSQL(box);
          	      
          box.put("p_isPage", new Boolean(true));     //    리스트 검색시 페이지 나누기가 있는 경우
	      box.put("p_row", new Integer(10));     //    리스트 검색시 페이지 나누기가 있는 게시판 경우
        
	      list = db.executeQueryList(box, sql);

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }

    /**
    @param box          receive from the form object and session
    @return PaperData
    */
    public DataBox getPaperData(RequestBox box) throws Exception {

		DBConnectionManager connMgr = null;

        DataBox     dbox = null;

        String v_grcode    = box.getStringDefault("p_grcode", box.getString("s_grcode"));
        String v_subj      = box.getString("p_subj");
        java.util.Date d_now = new java.util.Date();
        String v_gyear    = box.getStringDefault("p_gyear", box.getString("s_gyear"));
        String v_subjsel   = box.getString("p_subjsel");
        String v_subjseq   = box.getString("p_subjseq");
        String v_upperclass= box.getStringDefault("p_upperclass","ALL");

        int    v_sulpapernum = box.getInt("p_sulpapernum");

        try {
            connMgr = new DBConnectionManager();
            dbox =  getPaperData(connMgr, v_grcode, v_subj, v_gyear, v_subjsel, v_subjseq, v_upperclass, v_sulpapernum, v_subj);

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        finally {
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return dbox;
    }

    /**
    @param box          receive from the form object and session
    @return PaperData
    */
    public DataBox getPaperData(DBConnectionManager connMgr, String p_grcode, String p_subj, String p_gyear, String p_subjsel, String p_subjseq, String p_upperclass, int p_sulpapernum, String v_subj) throws Exception {
        ListSet ls = null;
        String sql  = "";
        DataBox     dbox = null;
        
        try {
        	RequestBox box = new RequestBox("");
        	box.put("p_subj",v_subj);
        	
            sql = getPaperListSQL(box);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
				dbox = ls.getDataBox();

            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }

        if (dbox==null) dbox = new DataBox("resoponsebox");

        return dbox;
    }

    public int inserttu_sulpaper(DBConnectionManager connMgr, String p_grcode, String p_gyear, String p_subj, String p_subjseq, int p_sulpapernum, String p_sulpapernm, int p_totcnt,
                                                String p_sulnums, String p_sulmailing, String p_sulstart, String p_sulend, int p_progresslimit, String p_luserid) throws Exception {
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;


        try {
            //insert tu_SULPAPER table
            sql =  "insert into tu_SULPAPER ";
            sql+=  "(grcode,    subj,     sulpapernum, sulpapernm, ";
            sql+=  " year,      subjseq,     ";
            sql+=  " totcnt,       sulnums,     sulmailing,   ";
            sql+=  " sulstart, sulend,  progresslimit,  luserid,  ldate )   ";
            sql+=  " values ";
            sql+=  "(?,         ?,       ?,         ?,   ";
            sql+=  " ?,         ?,            ";
            sql+=  " ?,         ?,       ?, ";
            sql+=  " ?,         ?,       ?,          ?,     ?) ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, p_grcode);
            pstmt.setString( 2, p_subj);
            pstmt.setInt   ( 3, p_sulpapernum);
            pstmt.setString( 4, p_sulpapernm);
            pstmt.setString( 5, p_gyear);
            pstmt.setString( 6, p_subjseq);
            pstmt.setInt( 7, p_totcnt);
            pstmt.setString( 8, p_sulnums);
            pstmt.setString( 9, p_sulmailing);
            pstmt.setString(10, "");
            pstmt.setString(11, "");
            pstmt.setInt( 12, p_progresslimit);
            pstmt.setString(13, p_luserid);
            pstmt.setString(14, FormatDate.getDate("yyyyMMddHHmmss"));

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

    public int updatetu_sulpaper(DBConnectionManager connMgr, String p_grcode, String p_gyear, String p_subj, String p_subjseq, int p_sulpapernum, String p_sulpapernm, int p_totcnt,
                                                    String p_sulnums, String p_sulmailing, String p_sulstart, String p_sulend, int p_progresslimit, String p_luserid) throws Exception {
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;

        try {
            //update tu_SULPAPER table
            sql = " update tu_SULPAPER ";
            sql+= "    set sulpapernm = ?, ";
            sql+= "        totcnt       = ?, ";
            sql+= "        sulnums      = ?, ";
            sql+= "        progresslimit      = ?, ";
            sql+= "        luserid      = ?, ";
            sql+= "        ldate        = ?  ";
            sql+= "  where grcode       = ?  ";
            sql+= "    and subj         = ?  ";
            sql+= "    and sulpapernum  = ?  ";
            sql+= "    and year  = ?  ";
            sql+= "    and subjseq  = ?  ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, p_sulpapernm);
            pstmt.setInt   (2, p_totcnt);
            pstmt.setString(3, p_sulnums);
            pstmt.setInt   (4, p_progresslimit);
            pstmt.setString(5, p_luserid);
            pstmt.setString(6, FormatDate.getDate("yyyyMMddHHmmss"));
            pstmt.setString(7, p_grcode);
            pstmt.setString(8, p_subj);
            pstmt.setInt   (9, p_sulpapernum);
            pstmt.setString(10, p_gyear);
            pstmt.setString(11, p_subjseq);

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

    public int updatetu_sulpaper(DBConnectionManager connMgr, String p_grcode, String p_subj, int p_sulpapernum, String p_sulpapernm, int p_totcnt, String p_sulnums, String p_sulmailing, String p_sulstart, String p_sulend, int p_progresslimit, String p_luserid) throws Exception {
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;

        try {
            //update tu_SULPAPER table
            sql = " update tu_SULPAPER ";
            sql+= "    set sulpapernm = ?, ";
            sql+= "        totcnt       = ?, ";
            sql+= "        sulnums      = ?, ";
            sql+= "        progresslimit      = ?, ";
            sql+= "        luserid      = ?, ";
            sql+= "        ldate        = ?  ";
            sql+= "  where grcode       = ?  ";
            sql+= "    and subj         = ?  ";
            sql+= "    and sulpapernum  = ?  ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, p_sulpapernm);
            pstmt.setInt   (2, p_totcnt);
            pstmt.setString(3, p_sulnums);
            pstmt.setInt   (4, p_progresslimit);
            pstmt.setString(5, p_luserid);
            pstmt.setString(6, FormatDate.getDate("yyyyMMddHHmmss"));
            pstmt.setString(7, p_grcode);
            pstmt.setString(8, p_subj);
            pstmt.setInt   (9, p_sulpapernum);

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

    public int deletetu_sulpaper(DBConnectionManager connMgr, String p_grcode, String p_gyear, String p_subj, String p_subjseq, int p_sulpapernum, String p_duserid) throws Exception {
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;

        try {
            //delete tu_SULPAPER table

            sql =  "delete from tu_SULPAPER ";
            sql+=  " where grcode     = ?  ";
            sql+=  "   and subj       = ?  ";
            sql+=  "   and sulpapernum= ?  ";
            sql+= "    and year  = ?  ";
            sql+= "    and subjseq  = ?  ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, p_grcode);
            pstmt.setString(2, p_subj);
            pstmt.setInt   (3, p_sulpapernum);
            pstmt.setString(4, p_gyear);
            pstmt.setString(5, p_subjseq);

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



    public boolean insertPaper(RequestBox box) throws Exception {
        DataBox dbox = null;
        String sql = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        boolean isCommit = false;
        DatabaseExecute db = null;

        String v_sulpapernm = box.getString("p_sulpapernm");
        String v_subj       = box.getStringDefault("p_subj", SulmunBean.DEFAULT_SUBJ);
        int    v_totcnt     = box.getInt("p_totcnt");
        String v_luserid    = box.getSession("userid");
        String v_sulnums    = box.getString("p_sulnums");
        int    v_sulnum     = 0;
        Vector vec_sulnums  = new Vector();
        Vector vec_orders  = new Vector();

        StringTokenizer v_token = new StringTokenizer(v_sulnums, SulmunBean.SPLIT_COMMA);

        try {


            db = new DatabaseExecute(box);

            //----------------------   번호 가져온다 ----------------------------
            int v_sulpapernum = getPapernumSeq();
            //-----------------------------------------------------------------------------------

            //////////////////////////////////   게시판 table 에 입력  ///////////////////////////////////////////////////////////////////
            sql1 =  "insert into tu_sulpaper( ";
            sql1+= "   SULPAPERNUM    ,  SULPAPERNM     ,  SULPAPERTYPE   ,  SULJOINUSERTYPE,  ";
            sql1+= "   SUBJ           ,  TOTCNT         ,  PROGRESSLIMIT  ,  LUSERID, ";
            sql1+= "   LDATE, SULSTART, SULEND          ";
            sql1+= " )";
            sql1+= " values (    ";
            sql1+= " ?, ?, ?, ?, ";
            sql1+= " ?, ?, ?, ?, ";
            sql1+= " to_char(sysdate, 'YYYYMMDDHH24MISS'),?,? ";
            sql1+= " )";

            PreparedBox pbox1 = new PreparedBox("preparedbox");

            pbox1.setInt   (1, v_sulpapernum);
            pbox1.setString(2, v_sulpapernm);
            pbox1.setInt   (3, 0);
            pbox1.setInt   (4, Integer.parseInt(box.getString("p_target")));
            pbox1.setString(5, v_subj);
            pbox1.setInt   (6, v_totcnt);
            pbox1.setInt   (7, 0);
            pbox1.setString(8, v_luserid);
            pbox1.setString(9, box.getString("p_valid_sdate").substring(0,4)+box.getString("p_valid_sdate").substring(5,7)+box.getString("p_valid_sdate").substring(8,10));
            pbox1.setString(10, box.getString("p_valid_edate").substring(0,4)+box.getString("p_valid_edate").substring(5,7)+box.getString("p_valid_edate").substring(8,10));

            PreparedBox pbox2 = new PreparedBox("preparedbox");
            int i = 1;

            while(v_token.hasMoreTokens()){
              v_sulnum = Integer.parseInt((String)v_token.nextToken());
              System.out.println("v_sulnum====>>>>>>>>>>>>"+v_sulnum);
              vec_sulnums.add(new Integer(v_sulnum));
              vec_orders.add(new Integer(i));
              i++;
            }

            sql2 = "insert into TU_SULPAPERQUESTION(sulnum, sulpapernum, orders) values(?, ?, ?)";

            pbox2.setVector(1, vec_sulnums);
            pbox2.setInt   (2, v_sulpapernum);
            pbox2.setVector(3, vec_orders);

            isCommit = db.executeUpdate(new PreparedBox [] {pbox1, pbox2},  new String [] {sql1, sql2});

            String updateSQL = db.getSQLString();
            String updateCount = db.getIsCommitString();

            System.out.println(updateSQL);
            System.out.println(updateCount);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return isCommit;
    }

    public boolean updatePaper(RequestBox box) throws Exception {
        DataBox dbox = null;
        String sql = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        boolean isCommit = false;
        DatabaseExecute db = null;

        String v_sulpapernm  = box.getString("p_sulpapernm");
        String v_subj        = box.getStringDefault("p_subj", SulmunBean.DEFAULT_SUBJ);
        int    v_totcnt      = box.getInt("p_totcnt");
        int    v_sulpapernum = box.getInt("p_sulpapernum");
        String v_sulnums     = box.getString("p_sulnums");
        int    v_sulnum      = 0;
        
        Vector vec_sulnums   = new Vector();
        Vector vec_orders    = new Vector();

        StringTokenizer v_token = new StringTokenizer(v_sulnums, SulmunBean.SPLIT_COMMA);
        
        String v_luserid     = box.getSession("userid");

        try {

            db = new DatabaseExecute(box);

            //----------------------   번호 가져온다 ----------------------------
            
            //-----------------------------------------------------------------------------------

            //////////////////////////////////   게시판 table 에 입력  ///////////////////////////////////////////////////////////////////
            sql1 =  "update tu_sulpaper set ";
            sql1+= "   SULPAPERNM  = ?,  ";
            sql1+= "   TOTCNT      = ?,  ";
            sql1+= "   LUSERID     = ?,  ";
            sql1+= "   LDATE       = to_char(sysdate, 'YYYYMMDDHH24MISS'), ";
            sql1+= "   SULJOINUSERTYPE     = ?,  ";            
            sql1+= "   SULSTART     = ?,  ";            
            sql1+= "   SULEND     = ?  ";            
            sql1+= " where ";
            sql1+= "  sulpapernum = ? ";
            PreparedBox pbox1 = new PreparedBox("preparedbox");
            pbox1.setString(1, v_sulpapernm);
            pbox1.setInt   (2, v_totcnt);
            pbox1.setString(3, v_luserid);
            pbox1.setInt   (4, Integer.parseInt(box.getString("p_target")));          
            pbox1.setString(5, box.getString("p_valid_sdate").substring(0,4)+box.getString("p_valid_sdate").substring(5,7)+box.getString("p_valid_sdate").substring(8,10));
            pbox1.setString(6, box.getString("p_valid_edate").substring(0,4)+box.getString("p_valid_edate").substring(5,7)+box.getString("p_valid_edate").substring(8,10));
            pbox1.setInt   (7, v_sulpapernum);

            
            sql2 = "delete from TU_SULPAPERQUESTION where sulpapernum = ?";
            PreparedBox pbox2 = new PreparedBox("preparedbox");
            pbox2.setInt(1, v_sulpapernum);


            int i = 1;
            while(v_token.hasMoreTokens()){
              v_sulnum = Integer.parseInt((String)v_token.nextToken());

              vec_sulnums.add(new Integer(v_sulnum));
              vec_orders.add(new Integer(i));
              i++;
            }

            sql3 = "insert into TU_SULPAPERQUESTION(sulnum, sulpapernum, orders) values(?, ?, ?)";
            PreparedBox pbox3 = new PreparedBox("preparedbox");
            pbox3.setVector(1, vec_sulnums);
            pbox3.setInt   (2, v_sulpapernum);
            pbox3.setVector(3, vec_orders);

            isCommit = db.executeUpdate(new PreparedBox [] {pbox1, pbox2, pbox3},  new String [] {sql1, sql2, sql3});

            String updateSQL = db.getSQLString();
            String updateCount = db.getIsCommitString();

            System.out.println(updateSQL);
            System.out.println(updateCount);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return isCommit;
    }

    public boolean deletePaper(RequestBox box) throws Exception {
        DataBox dbox = null;
        String sql = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        boolean isCommit = false;
        DatabaseExecute db = null;

        String v_sulpapernm  = box.getString("p_sulpapernm");
        String v_subj        = box.getStringDefault("p_subj", SulmunBean.DEFAULT_SUBJ);
        int    v_totcnt      = box.getInt("p_totcnt");
        int    v_sulpapernum = box.getInt("p_sulpapernum");
        String v_sulnums     = box.getString("p_sulnums");
        int    v_sulnum      = 0;
        
        Vector vec_sulnums   = new Vector();
        Vector vec_orders    = new Vector();

        StringTokenizer v_token = new StringTokenizer(v_sulnums, SulmunBean.SPLIT_COMMA);
        
        String v_luserid     = box.getSession("userid");

        try {

            db = new DatabaseExecute(box);

            //----------------------   번호 가져온다 ----------------------------
            
            //-----------------------------------------------------------------------------------

            //////////////////////////////////   게시판 table 에 입력  ///////////////////////////////////////////////////////////////////
            sql1 = "delete from TU_SULPAPER where sulpapernum = ?";
            PreparedBox pbox1 = new PreparedBox("preparedbox");
            pbox1.setInt(1, v_sulpapernum);

            sql2 = "delete from TU_SULPAPERQUESTION where sulpapernum = ?";
            PreparedBox pbox2 = new PreparedBox("preparedbox");
            pbox2.setInt(1, v_sulpapernum);

            isCommit = db.executeUpdate(new PreparedBox [] {pbox1, pbox2},  new String [] {sql1, sql2});

            String updateSQL = db.getSQLString();
            String updateCount = db.getIsCommitString();

            System.out.println(updateSQL);
            System.out.println(updateCount);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return isCommit;
    }

    public int getPapernumSeq() throws Exception {
        Hashtable maxdata = new Hashtable();
        maxdata.put("seqcolumn","sulpapernum");
        maxdata.put("seqtable","tu_sulpaper");
        maxdata.put("paramcnt","0");

        return SelectionUtil.getSeq(maxdata);
    }

	public Vector getSulnums(DBConnectionManager connMgr, String p_grcode, String p_subj, int p_sulpapernum) throws Exception {
        ListSet ls = null;
        String sql  = "";
        Vector v_sulnums = new Vector();
        String v_tokens  = "";
        StringTokenizer st = null;

        try {
            sql = "select sulnums  ";
            sql+= "  from tu_sulpaper ";
            sql+= " where grcode      = " + SQLString.Format(p_grcode);
            sql+= "   and subj        = " + SQLString.Format(p_subj);
            sql+= "   and sulpapernum = " + SQLString.Format(p_sulpapernum);

            ls = connMgr.executeQuery(sql);
            while (ls.next()) {
                v_tokens = ls.getString("sulnums");
            }

            st = new StringTokenizer(v_tokens,SulmunBean.SPLIT_COMMA);
            while (st.hasMoreElements()) {
                v_sulnums.add((String)st.nextToken());
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        return v_sulnums;
    }

    public ArrayList getSelnums(DBConnectionManager connMgr, String p_subj, String p_grcode, Vector p_sulnums) throws Exception {
        Hashtable hash = new Hashtable();
        ArrayList blist = new ArrayList();
		ArrayList list = null;

        ListSet ls  = null;
        String sql  = "";
        DataBox dbox = null;
        StringTokenizer st = null;

        String v_sulnums = "";
        for (int i=0; i < p_sulnums.size(); i++) {
            v_sulnums += (String)p_sulnums.get(i);
            if (i<p_sulnums.size()-1) {
                v_sulnums += ",";
            }
        }
        if (v_sulnums.equals("")) v_sulnums = "-1";

        try {

			st = new StringTokenizer(v_sulnums, ",");

            while(st.hasMoreElements()) {

                int sulnum = Integer.parseInt(st.nextToken());
	            sql = "select a.subj,     a.sulnum, a.selcount, a.selmax,  ";
	            sql+="        a.distcode, c.codenm distcodenm, ";
	            sql+= "       a.sultype,  d.codenm sultypenm, ";
	            sql+= "       a.sultext,  b.selnum, b.seltext, b.selpoint, ";
	            sql+= "       a.sulreturn ";
	            sql+= "  from tu_sul     a, ";
	            sql+= "       tu_sulsel  b, ";
	            sql+= "       tu_code    c, ";
	            sql+= "       tu_code    d  ";
	            sql+= " where a.subj     = b.subj(+)    ";
	            sql+= "   and a.grcode   = b.grcode(+)  ";
				sql+= "   and a.sulnum   = b.sulnum(+)  ";
	            sql+= "   and a.distcode = c.code ";
	            sql+= "   and a.sultype  = d.code ";
	            sql+= "   and a.subj     = " + SQLString.Format(p_subj);
	            sql+= "   and a.grcode     = " + SQLString.Format(p_grcode);
	            sql+= "   and a.sulnum = " + sulnum ;
	            sql+= "   and c.gubun    = " + SQLString.Format(SulmunBean.DIST_CODE);
	            sql+= "   and d.gubun    = " + SQLString.Format(SulmunBean.SUL_TYPE);
	            sql+= "   and d.levels    =  1 ";
	            sql+= " order by a.subj, a.sulnum, b.selnum ";

				ls = connMgr.executeQuery(sql);
				list =  new ArrayList();
	
	            while (ls.next()) {
	                  dbox = ls.getDataBox();
					  list.add(dbox);
	            }
			    blist.add(list);
			    ls.close();
			}

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        return blist;
    }

    /**
    @param box          receive from the form object and session
    @return ArrayList   설문문제 리스트
    */
    public ArrayList selectPaperQuestionExampleList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ArrayList list = null;

        String v_grcode      = box.getString("p_grcode");
        String v_subj        = box.getString("p_subj");
        int    v_sulpapernum = box.getInt("p_sulpapernum");

        try {
            if (v_sulpapernum == 0) {
                v_sulpapernum = getPapernumSeq()-1;
                box.put("p_sulpapernum", String.valueOf(v_sulpapernum));
            }

            connMgr = new DBConnectionManager();
            list = selectPaperQuestionExampleList(connMgr, v_grcode, v_subj, v_sulpapernum, box);
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
    public ArrayList selectPaperQuestionExampleList(DBConnectionManager connMgr, String p_grcode, String p_subj, int p_sulpapernum, RequestBox box) throws Exception {
        Vector    v_sulnums = null;
        ArrayList QuestionExampleDataList  = null;

        try {
                // 설문지번호에 해당하는 설문번호를 vector로 받아온다. 벡터(설문번호1,3,5 ....)
            v_sulnums = getSulnums(connMgr, p_grcode, p_subj, p_sulpapernum);
            if (!v_sulnums.equals("")) {
                // 설문번호에 해당하는 문제리스트를 만든다. 리스트((설문번호1, 보기1,2,3..))
                QuestionExampleDataList = getSelnums(connMgr, p_subj, p_grcode, v_sulnums);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }

        return QuestionExampleDataList;
    }

    /**
    @param box          receive from the form object and session
    @return ArrayList   평가문제 리스트
    */
    public ArrayList selectPaperPool(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ArrayList list = null;

        String v_subj   = box.getString("p_subj");
        String v_grcode   = box.getString("p_grcode");
        String v_action = box.getStringDefault("p_action",  "change");

        try {
            if (v_action.equals("go")) {
                connMgr = new DBConnectionManager();
                list = getPaperPool(connMgr, v_subj, v_grcode);
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
    @return ArrayList   평가문제 리스트
    */
    public ArrayList getPaperPool(DBConnectionManager connMgr, String p_subj, String p_grcode) throws Exception {
        ArrayList list = new ArrayList();
        ListSet ls = null;
        String sql  = "";
        DataBox dbox = null;

        try {
	    sql = "select distinct a.grcode,      a.subj,         ";
            sql+="        a.sulpapernum, a.sulpapernm, a.year, ";
            sql+= "       a.totcnt,      a.sulnums,  a.sulmailing, a.sulstart, a.sulend,    ";
            sql+= "       b.subjnm        ";
            sql+= "  from (select distinct grcode, subj, sulpapernum, sulpapernm, year, totcnt, sulnums, sulmailing,";
            sql+= "  sulstart, sulend from tu_sulpaper  ";
            sql+= "   where grcode    = " + SQLString.Format(p_grcode);
            sql+= "   and subj    = " + SQLString.Format(p_subj);
            sql+= "   and subj    ! = 'COMMON' ";
            sql+= "   and subj    ! = 'TARGET' ";
            sql+= "   and subj    ! = 'CONTENTS') a, ";
            sql+= "       tu_subj   b  ";
            sql+= "   where a.subj  = b.subj ";
            sql+= " order by a.subj, a.sulpapernum ";

            ls = connMgr.executeQuery(sql);//System.out.println(sql);
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
            if (ls != null) { try { ls.close(); } catch (Exception e) {} }
        }
        return list;
    }

     /**
    평가 문제 를 찾기위한 Pool
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectPaperPoolList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ArrayList list = null;
        ListSet ls = null;
        DataBox dbox = null;
        String sql = "";

        try {

            String ss_searchtype = box.getString("s_searchtype");
            String ss_searchtext = box.getString("s_searchtext");

            String v_action  = box.getString("p_action");
            String v_subj  = box.getString("p_subj");
            String v_grcode  = box.getString("p_grcode");

            list = new ArrayList();

            if (v_action.equals("go")) {
                connMgr = new DBConnectionManager();

                sql = "select a.grcode,      a.subj,         ";
                sql+="        a.sulpapernum, a.sulpapernm, a.year, ";
                sql+= "       a.totcnt,      a.sulnums,  a.sulmailing, a.sulstart, a.sulend,    ";
                sql+= "       b.subjnm        ";
                sql+= "  from (select distinct grcode, subj, sulpapernum, sulpapernm, year, totcnt, sulnums, sulmailing,";
                sql+= "  sulstart, sulend from tu_sulpaper  ";
                sql+= "   where grcode    = " + SQLString.Format(v_grcode);
//                sql+= "   and subj    = " + SQLString.Format(v_subj);
                sql+= "   and subj    ! = 'COMMON' ";
                sql+= "   and subj    ! = 'TARGET' ";
                sql+= "   and subj    ! = 'CONTENTS') a, ";
                sql+= "       tu_subj   b  ";
                sql+= "   where a.subj  = b.subj ";

                if (ss_searchtype.equals("1")) {  // 과정명
                    sql+= "  and b.subjnm like " + SQLString.Format("%"+ss_searchtext+"%");
                }
                else if (ss_searchtype.equals("2")) {  // 설문지명
                    sql+= "  and a.sulpapernm like " + SQLString.Format("%"+ss_searchtext+"%");
                }
                sql+= " order by a.subj, a.sulpapernum ";

                ls = connMgr.executeQuery(sql);//System.out.println(sql);

                while (ls.next()) {
                    dbox = ls.getDataBox();

                    list.add(dbox);
                }
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

    public int insertPaperPool(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;

        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;
        StringTokenizer st = null;
        StringTokenizer st2 = null;
        String v_tokens  = "";
        ArrayList list = new ArrayList();
        ArrayList list2 = new ArrayList();
        DataBox dbox = null;
        DataBox dbox2 = null;

        String v_grcode    = box.getStringDefault("p_grcode", box.getString("s_grcode"));
        String v_gyear    = box.getStringDefault("p_gyear", box.getString("s_gyear"));
        String v_subj         = box.getStringDefault("p_subj", box.getString("s_subjcourse"));
        String v_subjseq         = box.getString("p_subjseq");

        String v_luserid    = box.getSession("userid");
        int v_sulpapernum    = 0;

	String s_subj = "";
	int s_sulpapernum = 0;

        Vector  v_checks    = box.getVector("p_checks");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            for(int i=0; i < v_checks.size(); i++){
                v_tokens = (String)v_checks.elementAt(i);
                st = new StringTokenizer(v_tokens,"|");
                s_subj = (String)st.nextToken();
                s_sulpapernum = Integer.parseInt((String)st.nextToken());

                list = getPaperData(connMgr, s_subj, v_grcode, s_sulpapernum);
                dbox = (DataBox)list.get(0);

                String v_sulpapernm = dbox.getString("d_sulpapernm");
                int v_totcnt = dbox.getInt("d_totcnt");
                String v_sulmailing = dbox.getString("d_sulmailing");
                String v_sulstart = dbox.getString("d_sulstart");
                String v_sulend = dbox.getString("d_sulend");
                int v_progresslimit = dbox.getInt("d_progresslimit");
                String s_sulnums = dbox.getString("d_sulnums");
                String v_sulnums = "";
                int s_sulnum =0;
                int v_sulnum = getSulnumSeq(v_subj, v_grcode);

                int v_next = 0;

                if(!v_subj.equals(s_subj)) {
                    v_sulpapernum = getPapernumSeq();//System.out.println("v_sulpapernum" + v_sulpapernum);

                    st2 = new StringTokenizer(s_sulnums, ",");
                    int v_token = st2.countTokens();

                    while (st2.hasMoreElements()) {

                        s_sulnum = Integer.parseInt((String)st2.nextToken());
                        list2 = getExampleData(connMgr, s_subj, v_grcode, s_sulnum);
                        dbox2 = (DataBox)list2.get(0);

                        String v_distcode = dbox2.getString("d_distcode");
                        String v_sultype = dbox2.getString("d_sultype");
                        String v_sultext = dbox2.getString("d_sultext");
                        int v_selcount = dbox2.getInt("d_selcount");
                        int v_selmax = dbox2.getInt("d_selmax");
                        String v_sulreturn = dbox2.getString("d_sulreturn");
                        int v_scalecode = dbox2.getInt("d_scalecode");

                        int isOk1 = 0;
                        int isOk2 = 0;


                        isOk1 = inserttu_sul(connMgr, v_subj, v_grcode,  v_sulnum, v_distcode, v_sultype, v_sultext, v_selcount, v_selmax, v_sulreturn, v_scalecode, v_luserid);

                        sql =  "insert into tu_SULSEL(subj, grcode, sulnum, selnum, seltext, selpoint, luserid, ldate) ";
                        sql+=  " values (?, ?, ?, ?, ?, ?, ?, ?)";

                        pstmt = connMgr.prepareStatement(sql);

                        for (int j=0; j<list2.size(); j++) {
                            dbox2  = (DataBox)list2.get(j);
                            int v_selnum = dbox2.getInt("d_selnum");
                            String v_seltext = dbox2.getString("d_seltext");
                            int v_selpoint = dbox2.getInt("d_selpoint");

                            isOk2 += inserttu_sulsel(pstmt, v_subj, v_grcode, v_sulnum, v_selnum, v_seltext, v_selpoint, v_luserid);

        	            }

                        pstmt.close();

                        if (isOk1 >0 && isOk2==list2.size()) {
                            v_next++;
                        }else{
                            break;
                        }
                        if (v_token == v_next) {
                            v_sulnums += String.valueOf(v_sulnum);
                        } else{
                            v_sulnums += String.valueOf(v_sulnum) + ",";
                        }
                        v_sulnum++;
                    }

                    if (v_next == v_token){
                        isOk = inserttu_sulpaper(connMgr,  v_grcode, v_gyear, v_subj, v_subjseq, v_sulpapernum, v_sulpapernm, v_totcnt, v_sulnums, v_sulmailing, v_sulstart, v_sulend, v_progresslimit, v_luserid);

                        if(isOk > 0){
                            connMgr.commit();
                        }else{
                            connMgr.rollback();
                        }
                        v_sulpapernum++;
                    }
                }
                else {
                    isOk = inserttu_sulpaper(connMgr,  v_grcode, v_gyear, v_subj, v_subjseq, s_sulpapernum, v_sulpapernm, v_totcnt, s_sulnums, v_sulmailing, v_sulstart, v_sulend, v_progresslimit, v_luserid);

                    if(isOk > 0){
                        connMgr.commit();
                    }else{
                        connMgr.rollback();
                    }
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

    public ArrayList getPaperData(DBConnectionManager connMgr, String p_subj,  String p_grcode, int p_sulpapernum) throws Exception {
        ArrayList list = new ArrayList();
        ListSet ls = null;
        String sql  = "";
        DataBox dbox = null;

        try {
	        sql = "select a.grcode,      b.subj,   a.subjseq,      ";
            sql+="        a.sulpapernum, a.sulpapernm, a.year, ";
            sql+= "       a.totcnt,      a.sulnums,  a.sulmailing, a.sulstart, a.sulend,    ";
            sql+= "       b.subjnm        ";
            sql+= "  from tu_sulpaper  a, ";
            sql+= "       tu_subj      b  ";
            sql+= " where a.subj(+) = b.subj ";
            sql+="    and (a.grcode  = " + SQLString.Format(p_grcode) + " or a.grcode is null) ";
            sql+= "   and a.subj   = " + SQLString.Format(p_subj);
            sql+= "   and a.sulpapernum   = " + SQLString.Format(p_sulpapernum);
            sql+= " order by b.subj, a.sulpapernum ";

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

    public ArrayList getExampleData(DBConnectionManager connMgr, String p_subj,  String p_grcode, int p_sulnum) throws Exception {
        ArrayList list = new ArrayList();
        ListSet ls = null;
        String sql  = "";
        DataBox dbox = null;

        try {
            sql = "select a.subj,     a.sulnum, a.selcount, a.selmax,  ";
            sql+="        a.distcode, c.codenm distcodenm, ";
            sql+= "       a.sultype,  d.codenm sultypenm, ";
            sql+= "       a.sultext,  b.selnum, b.seltext, b.selpoint ";
            sql+= "  from tu_sul     a, ";
            sql+= "       tu_sulsel  b, ";
            sql+= "       tu_code    c, ";
            sql+= "       tu_code    d  ";
            sql+= " where a.subj     = b.subj(+)    ";
            sql+= "   and a.grcode   = b.grcode(+)  ";
			sql+= "   and a.sulnum   = b.sulnum(+)  ";
            sql+= "   and a.distcode = c.code ";
            sql+= "   and a.sultype  = d.code ";
            sql+= "   and a.subj     = " + SQLString.Format(p_subj);
            sql+= "   and a.grcode     = " + SQLString.Format(p_grcode);
            sql+= "   and a.sulnum = " + SQLString.Format(p_sulnum);
            sql+= "   and c.gubun    = " + SQLString.Format(SulmunBean.DIST_CODE);
            sql+= "   and d.gubun    = " + SQLString.Format(SulmunBean.SUL_TYPE);
            sql+= "   and d.levels    =  1 ";
            sql+= " order by a.subj, a.sulnum, b.selnum ";

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

    public int getSulnumSeq(String p_subj, String p_grcode) throws Exception {
        Hashtable maxdata = new Hashtable();
        maxdata.put("seqcolumn","sulnum");
        maxdata.put("seqtable","tu_sul");
        maxdata.put("paramcnt","2");
        maxdata.put("param0","subj");
        maxdata.put("param1","grcode");
        maxdata.put("subj",   SQLString.Format(p_subj));
        maxdata.put("grcode",   SQLString.Format(p_grcode));

        return SelectionUtil.getSeq(maxdata);
    }

    public int inserttu_sul(DBConnectionManager connMgr, String p_subj, String p_grcode, int p_sulnum, String p_distcode, String p_sultype, String p_sultext, int p_selcount, int p_selmax, String p_sulreturn, int p_scalecode, String p_luserid) throws Exception {
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;

        try {
            //insert tu_SUL table
            sql =  "insert into tu_SUL(subj, grcode, sulnum, distcode, sultype, sultext, selcount, selmax, sulreturn, scalecode, luserid, ldate ) ";
            sql+=  " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, p_subj);
            pstmt.setString(2, p_grcode);
            pstmt.setInt   (3, p_sulnum);
            pstmt.setString(4, p_distcode);
            pstmt.setString(5, p_sultype);
            pstmt.setString(6, p_sultext);
            pstmt.setInt(7, p_selcount);
            pstmt.setInt(8, p_selmax);
            pstmt.setString(9, p_sulreturn);
            pstmt.setInt(10, p_scalecode);
            pstmt.setString(11, p_luserid);
            pstmt.setString(12, FormatDate.getDate("yyyyMMddHHmmss"));

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

    public int inserttu_sulsel(PreparedStatement pstmt, String p_subj, String p_grcode, int p_sulnum, int p_selnum, String p_seltext, int p_selpoint, String p_luserid) throws Exception {
        int isOk = 0;

        try {
            pstmt.setString(1, p_subj);
            pstmt.setString(2, p_grcode);
            pstmt.setInt   (3, p_sulnum);
            pstmt.setInt   (4, p_selnum);
            pstmt.setString(5, p_seltext);
            pstmt.setInt   (6, p_selpoint);
            pstmt.setString(7, p_luserid);
            pstmt.setString(8, FormatDate.getDate("yyyyMMddHHmmss"));
            isOk = pstmt.executeUpdate();
       }
       catch(Exception ex) {
           ErrorManager.getErrorStackTrace(ex);
           throw new Exception(ex.getMessage());
       }
       return isOk;
    }

    /**
    *  설문지 셀렉트박스 (RequestBox, 셀렉트박스명,선택값,이벤트명)
	*  tu_SULPAPER 이용
    */
    public static String getSulPaperSelect (String p_grcode, String p_gyear, String p_subj, String name, int selected, String event, String p_subjseq) throws Exception {

        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String redamunt = null;
        String sql = "";
        DataBox     dbox = null;

        redamunt = "  <SELECT name=" + name + " " + event + " > \n";

        //redamunt += " <option value='0'>설문지를 선택하세요.</option> \n";

        try {
            connMgr = new DBConnectionManager();

            sql = " select nvl(sulpapernum,0) sulpapernum,  "
                + "        (select sulpapernm from tu_sulpaper "
                + "         where subj='ALL' and grcode='ALL'  and sulpapernum=tu_subjseq.sulpapernum) sulpapernm "
                + " from tu_subjseq  "
                + " where subj="+StringManager.makeSQL(p_subj)+" and grcode="+StringManager.makeSQL(p_grcode)+" and  year="+StringManager.makeSQL(p_gyear)+" and subjseq="+SQLString.Format(p_subjseq)+" ";

    	    /*sql = "select distinct grcode,       subj,   year,       ";
            sql+= "       sulpapernum,  sulpapernm,  ";
            sql+= "       totcnt,       sulnums";
            sql+= "  from tu_sulpaper ";
            sql+= " where grcode = " + StringManager.makeSQL(p_grcode);
            sql+= "   and subj   = " +StringManager.makeSQL(p_subj);
            sql+= "   and year   = " + StringManager.makeSQL(p_gyear);
            if (!p_subjseq.equals("ALL")) {
                sql+= "   and subjseq   = " + SQLString.Format(p_subjseq);
            }
            sql+= " order by subj, sulpapernum asc";
            */

            ls = connMgr.executeQuery(sql);
Log.info.println("설문지>>>>>>>>>"+sql);
            String v_null_test = "";
            String v_subj_bef = "";

            //while (ls.next()) {
            if (ls.next()) {
                dbox = ls.getDataBox();

                if (dbox.getInt("d_sulpapernum")!=0) {
                    redamunt += " <option value=" + dbox.getInt("d_sulpapernum");
                    if (selected==dbox.getInt("d_sulpapernum")) {
                        redamunt += " selected ";
                    }

                    redamunt += ">" + dbox.getString("d_sulpapernm") + "</option> \n";
                }else{
                    redamunt += " <option value='0'>해당과정에 설문지가 없습니다.</option> \n";
                }

            }else{

                redamunt += " <option value='0'>설문지를 선택하세요.</option> \n";
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

        redamunt += "  </SELECT> \n";
        return redamunt;
    }


    /**
    @param box          receive from the form object and session
    @return ArrayList   설문문제 리스트
    */
    public ArrayList TutorQuestionExampleList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ArrayList list = null;
        ListSet ls = null;
        DataBox dbox = null;

        String v_grcode = box.getString("p_grcode");

        String v_subj   = box.getString("s_subj");
        String v_year   = box.getString("p_gyear");
        String v_subjseq = box.getString("p_subjseq");
        String v_userid  = box.getSession("userid");
        String v_isonoff = box.getString("p_isonoff");

        int    v_sulpapernum = box.getInt("p_sulpapernum");

        String sql = "";

        try {
            if (v_sulpapernum == 0) {
                v_sulpapernum = getPapernumSeq()-1;
                box.put("p_sulpapernum", String.valueOf(v_sulpapernum));
            }

            connMgr = new DBConnectionManager();
            list = new ArrayList();

            if(v_isonoff.equals("OFF")){
              sql+= " select                                                \n";
              sql+= "   a.subj, a.year, a.subjseq, a.class,                 \n";
              sql+= "   b.tutorid tuserid, a.userid,                        \n";
              sql+= "   decode(c.code, '06', b.sdesc, c.codenm) lecturenm, d.name,   \n";
              sql+= "   b.lecture  \n";
              sql+= " from                                                  \n";
              sql+= "   tu_student  a,                                      \n";
              sql+= "   tu_offsubjlecture b,                                \n";
              sql+= "   tu_code c,                                          \n";
              sql+= "   tu_tutor d                                          \n";
              sql+= " where                                                 \n";
              sql+= "   a.subj = b.subj                                     \n";
              sql+= "   and a.year = b.year                                 \n";
              sql+= "   and a.subjseq = b.subjseq                           \n";
              sql+= "   and a.class = '000'||to_char(b.classno)             \n";
              sql+= "   and b.sdesc_cd = c.code                             \n";
              sql+= "   and b.tutorid = d.userid                            \n";
              sql+= "   and c.gubun = '0063'                                \n";
              sql+="   and a.subj = '"+v_subj+"'                              \n";
              sql+="   and a.year = '"+v_year+"'                              \n";
              sql+="   and a.subjseq = '"+v_subjseq+"'                        \n";
              sql+="   and a.userid = '"+v_userid+"'                          \n";
            }else{
              sql =" select                                                   \n";
              sql+="  a.subj, a.year, a.subjseq, a.class, b.tuserid, a.userid, \n";
              sql+="  0 lecture, '' lecturenm, c.name \n";
              sql+=" from                                                     \n";
              sql+="   tu_student a, tu_classtutor b,                         \n";
              sql+= "   tu_tutor c                                          \n";
              sql+=" where                                                    \n";
              sql+="   a.subj = b.subj                                        \n";
              sql+="   and a.year = b.year                                    \n";
              sql+="   and a.subjseq = b.subjseq                              \n";
              sql+="   and a.class = b.class                                  \n";
              sql+="   and b.tuserid = c.userid                            \n";
              sql+="   and a.subj = '"+v_subj+"'                              \n";
              sql+="   and a.year = '"+v_year+"'                              \n";
              sql+="   and a.subjseq = '"+v_subjseq+"'                        \n";
              sql+="   and a.userid = '"+v_userid+"'                          \n";
            }
            System.out.println("강사sql===>>"+sql);
            ls = connMgr.executeQuery(sql);

            while(ls.next()){
              dbox = (DataBox)ls.getDataBox();
              dbox.put("d_isonoff", v_isonoff);
              list.add(dbox);
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


    public String getPaperListSQL(RequestBox box) throws Exception {

        String sql = "";

        sql +="\r\n  select                                             ";
        sql +="\r\n    a.sulpapernum, a.sulpapernm, a.year, a.totcnt,  ";
        sql +="\r\n    a.ismailing, a.sulstart, a.sulend,  ";
        sql +="\r\n    a.progresslimit, ";
        sql +="\r\n    (select count(distinct userid) ";
        sql +="\r\n    		  from tu_suleach b ";
        sql +="\r\n 		  where a.sulpapernum=b.sulpapernum) joiner   ";
        sql +="\r\n  from  ";
        sql +="\r\n    tu_sulpaper a ";
        sql +="\r\n  where subj='"+box.getString("p_subj")+"' ";
        
        if (!box.getString("p_searchtext").equals(""))
        	sql +="\r\n  and sulpapernm like '%"+box.getString("p_searchtext")+"%'";
        
        sql +="\r\n  order by sulpapernum  ";
        
         return sql;
    }
    
    
    /**
    @param box          receive from the form object and session
    @return ArrayList   설문문제 리스트
    */
    public ArrayList selectOnLinePoolList(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        ArrayList list = null;
        String sql = "";

        String v_subj = box.getString("p_subj");
        
        String v_searchtext = box.getString("p_searchtext");
        String v_select = box.getString("p_select");

        try {
          db = new DatabaseExecute();

            sql+=" select ";
            sql+="   a.sulpapernum, a.sulpapernm, a.year, a.totcnt,";
            sql+="   a.ismailing, a.sulstart, a.sulend,";
            sql+="   a.progresslimit,";
            sql+="   (select count(distinct userid) from tu_suleach b where a.sulpapernum=b.sulpapernum and b.userid = '"+box.getSession("userid")+"') isapply";
            sql+=" from";
            sql+="   tu_sulpaper a";
            sql+=" where subj='COMMON'";
            sql+=" order by sulstart desc";
            System.out.println(sql);


          list = db.executeQueryList(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }
}