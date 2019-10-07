// ***************************************************************************
// 1. 제          목 : 환경설정 - 교육구분
// 2. 프로그램명 : LearnTypeBean.java
// 3. 개          요 : 환경설정 - 교육구분을 검색/등록/수정/삭제한다.
// 4. 환          경 : JDK 1.4
// 5. 버          전 : 1.0
// 6. 작          성 : 김민수 2006.08.03
// 7. 수          정 : 
// ***************************************************************************

package com.ziaan.system;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;

public class LearnTypeBean
{
    public LearnTypeBean()
    {}
    
    /*
     * 환경설정 - 교육구분 리스트
     * @param box   receive from the form object and session
     * @return ArrayList
     * */
    public ArrayList performList(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;        
        String sql = "";
        
        try
        {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql += "select                                                                       \n";
            sql += "       decode(type, '1', '능력개발', '2', '경력개발', '') type,                 \n";
            sql += "       year,                                                                 \n";
            sql += "       decode(k_pay,      '1', '무료', '2', '유료', '')        k_pay,          \n";
            sql += "       decode(k_apply,    '1', '없음', '2', 'O', '3', 'X', '') k_apply,       \n";
            sql += "       decode(k_offline,  '1', '없음', '2', 'O', '3', 'X', '') k_offline,     \n";
            sql += "       decode(k_3out,     '1', '없음', '2', 'O', '3', 'X', '') k_3out,        \n";
            sql += "       decode(nk_pay,     '1', '무료', '2', '유료', '')        nk_pay,         \n";
            sql += "       decode(nk_apply,   '1', '없음', '2', 'O', '3', 'X', '') nk_apply,      \n";
            sql += "       decode(nk_offline, '1', '없음', '2', 'O', '3', 'X', '') nk_offline,    \n";
            sql += "       decode(nk_3out,    '1', '없음', '2', 'O', '3', 'X', '') nk_3out,       \n";
            sql += "       luserid,                                                              \n";
            sql += "       ldate,                                                                \n";
            sql += "       inuserid,                                                             \n";
            sql += "       indate                                                                \n";
            sql += "  from                                                                       \n";
            sql += "       tz_learntype                                                          \n";
            sql += " order by                                                                    \n";
            sql += "       year, type                                                            \n";
            
            sql  = "select                                                                       \n";
            sql += "       rownum,                 \n";
            sql += "       type,                 \n";
            sql += "       year,                                                                 \n";
            sql += "       k_pay,          \n";
            sql += "       k_apply,       \n";
            sql += "       k_offline,     \n";
            sql += "       k_3out,        \n";
            sql += "       nk_pay,         \n";
            sql += "       nk_apply,      \n";
            sql += "       nk_offline,    \n";
            sql += "       nk_3out,        \n";
            sql += "       luserid,                                                              \n";
            sql += "       ldate,                                                                \n";
            sql += "       inuserid,                                                             \n";
            sql += "       indate                                                                \n";
            sql += "  from                                                                       \n";
            sql += "       tz_learntype                                                          \n";
            if(!box.getString("p_search_type").equals("0"))
            {
                sql += " where                                                                       \n";
                sql += "       type = '"+box.getString("p_search_type")+"'                           \n";
            }
            sql += " order by                                                                    \n";
            sql += "       year, type                                                            \n";
            
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() )
            {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        }
        catch ( Exception ex )
        {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql : \r\n" + sql + "\r\n ErrorMessage : \r\n" + ex.getMessage());
        }
        finally
        {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } };
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }        
        
        return list;
    }
    
    public int performInsert(RequestBox box) throws Exception
    {
        int iResult = 0;
        int v_cnt = 0;
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;
        String sql = "";
        
        try
        {
            connMgr = new DBConnectionManager(); 
            connMgr.setAutoCommit(false);
            
            sql  = "select count(*) from tz_learntype where type='"+box.getString("p_type")+"' and year='"+box.getString("p_year")+"'";
            ListSet ls = connMgr.executeQuery(sql);
            
            if ( ls.next() ) { v_cnt = ls.getInt(1); }

            if ( v_cnt > 0 ) 
            {     // 기존 등록되어있으면
                iResult = 0;
            } 
            else 
            {             
                sql  = "insert into                 \n";
                sql += "       tz_learntype         \n";
                sql += "       (                    \n";
                sql += "        type,               \n";
                sql += "        year,               \n";
                sql += "        k_pay,              \n";
                sql += "        k_apply,            \n";
                sql += "        k_offline,          \n";
                sql += "        k_3out,             \n";
                sql += "        nk_pay,             \n";
                sql += "        nk_apply,           \n";
                sql += "        nk_offline,         \n";
                sql += "        nk_3out,            \n";
                sql += "        luserid,            \n";
                sql += "        ldate,              \n";
                sql += "        inuserid,           \n";
                sql += "        indate              \n";
                sql += "       )                    \n";
                sql += "values                      \n";
                sql += "       (                    \n";
                sql += "        ?,                  \n";
                sql += "        ?,                  \n";
                sql += "        ?,                  \n";
                sql += "        ?,                  \n";
                sql += "        ?,                  \n";
                sql += "        ?,                  \n";
                sql += "        ?,                  \n";
                sql += "        ?,                  \n";
                sql += "        ?,                  \n";
                sql += "        ?,                  \n";
                sql += "        ?,                  \n";
                sql += "        to_char(sysdate, 'yyyymmddhh24miss'),                  \n";
                sql += "        ?,                  \n";
                sql += "        to_char(sysdate, 'yyyymmddhh24miss')                  \n";
                sql += "       )                    \n";
                
                           
                pstmt = connMgr.prepareStatement(sql);
                
                int seq = 1;
                pstmt.setString(seq++,  box.getString("p_type"));   
                pstmt.setString(seq++,  box.getString("p_year"));
                pstmt.setString(seq++,  box.getString("p_k_pay"));
                pstmt.setString(seq++,  box.getString("p_k_apply"));
                pstmt.setString(seq++,  box.getString("p_k_offline"));
                pstmt.setString(seq++,  box.getString("p_k_3out"));
                pstmt.setString(seq++,  box.getString("p_nk_pay"));
                pstmt.setString(seq++,  box.getString("p_nk_apply"));
                pstmt.setString(seq++,  box.getString("p_nk_offline"));
                pstmt.setString(seq++,  box.getString("p_nk_3out"));
                pstmt.setString(seq++,  box.getSession("userid"));
                //pstmt.setString(seq++,  box.getString("p_ldate"));
                pstmt.setString(seq++,  box.getSession("userid"));
                //pstmt.setString(seq++,  box.getString("p_indate"));            
                
                iResult = pstmt.executeUpdate();
            }
            
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
        int iResult = 0;
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;
        String sql = "";
        
        try
        {
            sql += "update                                                      \n";
            sql += "       tz_learntype                                         \n";
            sql += "   set                                                      \n";
            sql += "       k_pay       = ?,                                     \n";
            sql += "       k_apply     = ?,                                     \n";
            sql += "       k_offline   = ?,                                     \n";
            sql += "       k_3out      = ?,                                     \n";
            sql += "       nk_pay      = ?,                                     \n";
            sql += "       nk_apply    = ?,                                     \n";
            sql += "       nk_offline  = ?,                                     \n";
            sql += "       nk_3out     = ?,                                     \n";
            sql += "       luserid     = ?,                                     \n";
            sql += "       ldate       = to_char(sysdate, 'yyyymmddhh24miss')   \n";
            sql += " where                                                      \n";
            sql += "       type = ?                                             \n";
            sql += "   and year = ?                                             \n";
            
            connMgr = new DBConnectionManager();            
            pstmt = connMgr.prepareStatement(sql);
            
            int seq = 1;            
            pstmt.setString(seq++,  box.getString("p_k_pay"));
            pstmt.setString(seq++,  box.getString("p_k_apply"));
            pstmt.setString(seq++,  box.getString("p_k_offline"));
            pstmt.setString(seq++,  box.getString("p_k_3out"));
            pstmt.setString(seq++,  box.getString("p_nk_pay"));
            pstmt.setString(seq++,  box.getString("p_nk_apply"));
            pstmt.setString(seq++,  box.getString("p_nk_offline"));
            pstmt.setString(seq++,  box.getString("p_nk_3out"));
            pstmt.setString(seq++,  box.getSession("userid"));
            pstmt.setString(seq++,  box.getString("p_type"));
            pstmt.setString(seq++,  box.getString("p_year"));
            
            iResult = pstmt.executeUpdate();
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
    
    public int performDelete(RequestBox box) throws Exception
    {
        int iResult = 0;
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;
        String sql = "";
        
        try
        {
            sql += "delete                  \n";
            sql += "       tz_learntype     \n";
            sql += " where                  \n";
            sql += "       type = ?         \n";
            sql += "   and year = ?         \n";
            
            connMgr = new DBConnectionManager();            
            pstmt = connMgr.prepareStatement(sql);
            
            int seq = 1;
            pstmt.setString(seq++,  box.getString("p_type"));
            pstmt.setString(seq++,  box.getString("p_year"));  
            
            iResult = pstmt.executeUpdate();
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
