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

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;


public class OrganizationBean { 
    
    // private 
    
    public OrganizationBean() { }

    /**
    zipcode 시도리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList performSidoList(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet     ls              = null;
        ArrayList   list            = null;
        DataBox     dbox            = null;
 
        String      sql             = "";       

        sql  = "";
        sql += "select sido from tz_zipcode group by sido \n";      
        
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
    zipcode 구군리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList performGugunList(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet     ls              = null;
        ArrayList   list            = null;
        DataBox     dbox            = null;
 
        String      sql             = "";       

        sql  = "";
        sql += "select gugun from tz_zipcode where sido = '"+box.getString("p_sido")+"' group by gugun \n";      
        
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

        sql  = "";
        sql += "select                                  \n";
        sql += "       orgcode,                             \n";
        sql += "       title,                               \n";
        sql += "       sido,                                \n";
        sql += "       luserid,                             \n";
        sql += "       ldate                                \n";
        sql += "  from                                      \n";
        sql += "       tz_organization                      \n";
        
        if(!box.getString("p_orgcode").equals(""))
            sql += " where orgcode = "+box.getString("p_orgcode");
        
        sql += " order by                                      \n";
        sql += "       title asc                      \n";
        
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
        int iResult2    = 0; 
        int v_orgcode   = 0;    //조직코드
        int seq         = 0;
        
        Vector v_gugun  = box.getVector("p_gugun");
        String v_gugunText = "";                //백터에서 받을 텍스트
        
        String s_userid = box.getSession("userid");
        String v_title  = box.getString("p_title");
        String v_sido  = box.getString("p_sido");
        
        
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;     //tz_organization테이블용
        PreparedStatement   pstmt2   = null;    //tz_organization_mat테이블용
        
        String sql = "";
        
        try
        {
            connMgr = new DBConnectionManager(); 
            connMgr.setAutoCommit(false);
            
            sql  = "select                                  \n";
            sql += "       max(orgcode) maxcode             \n";
            sql += "  from                                  \n";
            sql += "       tz_organization                  \n";
            
            ListSet ls = connMgr.executeQuery(sql);
            if ( ls.next() ) { v_orgcode = ls.getInt(1) + 1; }

            sql  = "insert into tz_organization                         \n";
            sql += "(                                                   \n";
            sql += " orgcode,                                           \n";
            sql += " title,                                             \n";
            sql += " sido,                                              \n";
            sql += " luserid,                                           \n";
            sql += " ldate                                              \n";
            sql += ")                                                   \n";
            sql += "values                                              \n";
            sql += "(                                                   \n";
            sql += " ?,                                                 \n";
            sql += " ?,                                                 \n";
            sql += " ?,                                                 \n";
            sql += " ?,                                                 \n";
            sql += " to_char(sysdate, 'yyyymmddhh24miss')               \n";
            sql += ")                                                   \n";

            pstmt = connMgr.prepareStatement(sql);
            
            seq = 1;
            pstmt.setInt(seq++,  v_orgcode);   
            pstmt.setString(seq++,  v_title);
            pstmt.setString(seq++,  v_sido);
            pstmt.setString(seq++,  s_userid);
            
            iResult = pstmt.executeUpdate();
            
            for ( int i = 0;i<v_gugun.size();i++ )
            { 
                v_gugunText = (String)v_gugun.elementAt(i);                
                
                System.out.println("\n"+v_orgcode);
                System.out.println("\n"+v_gugunText);
                System.out.println("\n"+s_userid);
                
                sql  = "insert into tz_organization_mat                 \n";
                sql += "(                                               \n";
                sql += " orgcode,                                       \n";
                sql += " gugun,                                         \n";
                sql += " luserid,                                       \n";
                sql += " ldate                                          \n";
                sql += ")                                               \n";
                sql += "values                                          \n";
                sql += "(                                               \n";
                sql += " ?,                                             \n";
                sql += " ?,                                             \n";
                sql += " ?,                                             \n";
                sql += " to_char(sysdate, 'yyyymmddhh24miss')           \n";
                sql += ")                                               \n";
                
                pstmt2 = connMgr.prepareStatement(sql);
                
                seq = 1;
                pstmt2.setInt(seq++,  v_orgcode);   
                pstmt2.setString(seq++,  v_gugunText);
                pstmt2.setString(seq++,  s_userid);
                
                System.out.println("\n"+sql);
                iResult2 = pstmt2.executeUpdate();
            }

            
            if ( iResult > 0 && iResult2 > 0 ) { 
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
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return iResult;
    }

    /**
    조직 구군 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList performGugunOrg(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet     ls              = null;
        ArrayList   list            = null;
        DataBox     dbox            = null;
 
        String      sql             = "";       

        sql  = "";
        sql += "select                                  \n";
        sql += "       orgcode, gugun, luserid, ldate   \n";
        sql += "  from                                  \n";
        sql += "       tz_organization_mat              \n";
        sql += " where                                  \n";
        sql += "       orgcode = "+box.getInt("p_orgcode")+"              \n";
        
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
    
    public int performUpdate(RequestBox box) throws Exception
    {
        int iResult     = 0;
        int iResult1     = 0;
        int iResult2    = 0;   
        
        Vector v_gugun  = box.getVector("p_gugun");
        String v_gugunText = "";                //백터에서 받을 텍스트
        
        String s_userid = box.getSession("userid");
        String v_orgcode  = box.getString("p_orgcode");
        
        
        DBConnectionManager connMgr = null;        
        String sql = "";
        
        try
        {
            connMgr = new DBConnectionManager(); 
            connMgr.setAutoCommit(false);
            
            sql += "update tz_organization                                  \n";
            sql += "   set                                                  \n";
            sql += "       title = '"+box.getString("p_title")+"',                                       \n";
            sql += "       luserid = '"+box.getSession("userid")+"',                                     \n";
            sql += "       ldate = to_char(sysdate, 'yyyymmddhh24miss')     \n";
            sql += " where                                                  \n";
            sql += "       orgcode = "+box.getInt("p_orgcode")+"                                      \n";
            
            connMgr = new DBConnectionManager();            
            
            System.out.println(sql);
            iResult = connMgr.executeUpdate(sql);
            
            sql = "delete tz_organization_mat where orgcode = " + box.getInt("p_orgcode");
            
            System.out.println(sql);
            iResult1 = connMgr.executeUpdate(sql);
            
            for ( int i = 0;i<v_gugun.size();i++ )
            { 
                v_gugunText = (String)v_gugun.elementAt(i);                
                
                sql  = "insert into tz_organization_mat                 \n";
                sql += "(                                               \n";
                sql += " orgcode,                                       \n";
                sql += " gugun,                                         \n";
                sql += " luserid,                                       \n";
                sql += " ldate                                          \n";
                sql += ")                                               \n";
                sql += "values                                          \n";
                sql += "(                                               \n";
                sql += " "+v_orgcode+",                                             \n";
                sql += " '"+v_gugunText+"',                                             \n";
                sql += " '"+s_userid+"',                                             \n";
                sql += " to_char(sysdate, 'yyyymmddhh24miss')           \n";
                sql += ")                                               \n";
                
                System.out.println("\n"+sql);
                iResult2 = connMgr.executeUpdate(sql);
            }

            
            if ( iResult > 0 && iResult1 > 0 && iResult2 > 0 ) { 
                connMgr.commit();
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
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return iResult;
    }
    
    public int performDelete(RequestBox box) throws Exception
    {
        int iResult     = 0;
        int iResult1     = 0;
     
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;     //tz_organization테이블용
        PreparedStatement   pstmt2   = null;    //tz_organization_mat테이블용
        
        String sql = "";
        
        try
        {
            connMgr = new DBConnectionManager(); 
            connMgr.setAutoCommit(false);
            
            sql += "delete tz_organization                                  \n";
            sql += " where                                                  \n";
            sql += "       orgcode = "+box.getInt("p_orgcode")+"                                      \n";
            
            connMgr = new DBConnectionManager();            
            
            System.out.println(sql);
            iResult = connMgr.executeUpdate(sql);
            
            sql = "delete tz_organization_mat where orgcode = " + box.getInt("p_orgcode");
            
            System.out.println(sql);
            iResult1 = connMgr.executeUpdate(sql);
            
            if ( iResult > 0 && iResult1 > 0 ) { 
                connMgr.commit();
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
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return iResult;
    }
    
}