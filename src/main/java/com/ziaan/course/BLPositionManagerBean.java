// **********************************************************
//  1. 제      목: 인사DB 검색
//  2. 프로그램명: MemberSearchBean.java
//  3. 개      요: 인사DB 검색
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성:  2004. 12. 20
//  7. 수      정:
// **********************************************************

package com.ziaan.course;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;


public class BLPositionManagerBean { 
    
    // private 
    
    public BLPositionManagerBean() { }
  
    /**
    조직 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList performList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet     ls              = null;
        ArrayList   list            = null;
        DataBox     dbox            = null;
 
        String      sql             = "";       
        String v_branchcode = box.getString("p_branchcode");
        
        sql += "select                                                  \n"
            +  "    branchcode,                                         \n"
            +  "    branchnm,                                           \n"
            +  "    adminid,                                            \n"
            +  "    adminname,                                          \n"
            +  "    admintel,                                           \n"
            +  "    accountno,                                          \n"
            +  "    banknm,                                             \n"
            +  "    savefile,                                           \n"
            +  "    (                                                   \n"
            +  "        select decode(count(*), 0, 'Y', 'N')            \n"
            +  "        from tz_coursebranchinfo                        \n"
            +  "        where branchcode = a.branchcode                 \n"
            +  "    ) delyn,                                           \n"
            +  "    realfile                                            \n"
            +  "from                                                    \n"
            +  "    tz_branch a                                         \n";
        
        if(!"".equals(v_branchcode))
            sql += "where branchcode = " + StringManager.makeSQL(v_branchcode);
        
        sql += " order by                                      \n";
        sql += "       branchnm asc                      \n";
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
                      
            System.out.println(sql);
            ls = connMgr.executeQuery(sql);
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
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

    /**
    조직 등록
    @param box      receive from the form object and session
    @return ArrayList
    */
    public int performInsert(RequestBox box) throws Exception
    {
        int iResult     = 0;        
        int seq         = 0;
        String v_branchcode = "";        
        String v_branchnm   = box.getString("p_branchnm");
        String v_adminid    = box.getString("p_adminid");
        String v_adminname  = box.getString("p_adminname");
        String v_admintel   = box.getString("p_admintel");
        String v_accountno  = box.getString("p_accountno");
        String v_banknm     = box.getString("p_banknm");
        
        String v_realfilename = box.getRealFileName("p_map");
        String v_savefilename = box.getNewFileName("p_map");
        
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;
        
        String sql = "";
        
        try
        {
            connMgr = new DBConnectionManager(); 
            connMgr.setAutoCommit(false);
            
            sql  = "select                                  \n";
            sql += "       max(branchcode) branchcode       \n";
            sql += "  from                                  \n";
            sql += "       tz_branch                        \n";
            
            ListSet ls = connMgr.executeQuery(sql);
            if ( ls.next() ) 
            {                 
                v_branchcode = ls.getInt(1) + 1 < 10 ? "000"+(ls.getInt(1) + 1) 
                             : ls.getInt(1) + 1 < 100 ? "00"+(ls.getInt(1) + 1)
                             : ls.getInt(1) + 1 < 1000 ? "0"+(ls.getInt(1) + 1)
                             : ""+(ls.getInt(1) + 1);
            }

            sql  = "insert into tz_branch                                   \n";
            sql += "(                                                       \n";
            sql += " branchcode,                                            \n";
            sql += " branchnm,                                              \n";
            sql += " adminid,                                               \n";
            sql += " adminname,                                             \n";
            sql += " admintel,                                              \n";
            sql += " accountno,                                             \n";
            sql += " banknm,                                                \n";
            sql += " savefile,                                              \n";
            sql += " realfile                                               \n";  
            sql += ")                                                       \n";
            sql += "values                                                  \n";
            sql += "(                                                       \n";
            sql += " ?,                                                     \n";
            sql += " ?,                                                     \n";
            sql += " ?,                                                     \n";
            sql += " ?,                                                     \n";
            sql += " ?,                                                     \n";
            sql += " ?,                                                     \n";
            sql += " ?,                                                     \n";
            sql += " ?,                                                     \n";
            sql += " ?                                                      \n";
            sql += ")                                                       \n";

            pstmt = connMgr.prepareStatement(sql);
            
            seq = 1;
            pstmt.setString(seq++,  v_branchcode);
            pstmt.setString(seq++,  v_branchnm);
            pstmt.setString(seq++,  v_adminid);
            pstmt.setString(seq++,  v_adminname);
            pstmt.setString(seq++,  v_admintel);
            pstmt.setString(seq++,  v_accountno);
            pstmt.setString(seq++,  v_banknm);
            pstmt.setString(seq++,  v_savefilename);            
            pstmt.setString(seq++,  v_realfilename);            
            
            iResult = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            
            if ( iResult > 0 ) { 
                connMgr.commit();
            } else { 
                connMgr.rollback();
            }
        }
        catch ( Exception ex )
        {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql : \n " + sql + "\n ErrorMessage : \n" + ex.getMessage() );
        }
        finally
        {
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return iResult;
    }

    public int performUpdate(RequestBox box) throws Exception
    {
        int iResult     = 0;
        PreparedStatement   pstmt   = null;
        String v_branchcode = box.getString("p_branchcode");
        String v_branchnm   = box.getString("p_branchnm");
        String v_adminid    = box.getString("p_adminid");
        String v_adminname  = box.getString("p_adminname");
        String v_admintel   = box.getString("p_admintel");
        String v_accountno  = box.getString("p_accountno");
        String v_banknm     = box.getString("p_banknm");
        
        String v_realfilename = box.getRealFileName("p_map");
        String v_savefilename = box.getNewFileName("p_map");
        String v_del = box.getString("p_del");
        Vector v_savefile  = box.getVector("p_savefile");
        
        DBConnectionManager connMgr = null;        
        String sql = "";
        
        try
        {
            connMgr = new DBConnectionManager(); 
            connMgr.setAutoCommit(false);
            
            sql += "update tz_branch                                                            \n";
            sql += "   set                                                                      \n";
            sql += " branchnm = ?,                                                              \n";
            sql += " adminid = ?,                                                               \n";
            sql += " adminname = ?,                                                             \n";
            sql += " admintel = ?,                                                              \n";
            sql += " accountno = ?,                                                             \n";
            if(!"".equals(v_savefilename)||"1".equals(v_del)) {
            sql += " realfile = ?,                                                               \n";
            sql += " savefile = ?,                                                               \n";
            }
            sql += " banknm = ?                                                                 \n";  
            sql += " where                                                                      \n";
            sql += "       branchcode = ?                                                       \n";
            
            pstmt = connMgr.prepareStatement(sql);
            
            int seq = 1;
            pstmt.setString(seq++,  v_branchnm);
            pstmt.setString(seq++,  v_adminid);
            pstmt.setString(seq++,  v_adminname);
            pstmt.setString(seq++,  v_admintel);
            pstmt.setString(seq++,  v_accountno);
            if(!"".equals(v_savefilename)||"1".equals(v_del)) {
                pstmt.setString(seq++,  v_realfilename);
                pstmt.setString(seq++,  v_savefilename);
            }
            pstmt.setString(seq++,  v_banknm);
            pstmt.setString(seq++,  v_branchcode);
            iResult = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            if ( iResult > 0 ) { 
                connMgr.commit();
                if(!"".equals(v_savefilename)||"1".equals(v_del)) {
                    FileManager.deleteFile(v_savefile);         // 기존 이미지 삭제
                }
            } else { 
                connMgr.rollback();
            }
        }
        catch ( Exception ex )
        {
            FileManager.deleteFile(v_savefilename);       
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql : \n " + sql + "\n ErrorMessage : \n" + ex.getMessage() );
        }
        finally
        {
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return iResult;
    }
    
    public int performDelete(RequestBox box) throws Exception
    {
        int iResult     = 0;
        String v_branchcode = box.getString("p_branchcode");
        String v_savefile = box.getString("p_savefile");
     
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;     //tz_organization테이블용
        
        String sql = "";
        
        try
        {
            connMgr = new DBConnectionManager(); 
            connMgr.setAutoCommit(false);
            
            sql += "delete tz_branch                                                        \n";
            sql += " where                                                                  \n";
            sql += "       branchcode = " + StringManager.makeSQL(v_branchcode) + "         \n";
            
            connMgr = new DBConnectionManager();            
            
            iResult = connMgr.executeUpdate(sql);
            
            if ( iResult > 0 ) { 
                connMgr.commit();
                FileManager.deleteFile(v_savefile);         // 기존 이미지 삭제
            } else { 
                connMgr.rollback();
            }
        }
        catch ( Exception ex )
        {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql : \n " + sql + "\n ErrorMessage : \n" + ex.getMessage() );
        }
        finally
        {
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return iResult;
    }
    
}