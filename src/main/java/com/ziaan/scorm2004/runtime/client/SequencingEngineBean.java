package com.ziaan.scorm2004.runtime.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class SequencingEngineBean
{
    public List selectLaunchLocation( String courseID, String activityID ) throws Exception
    {
        DBConnectionManager connMgr = null;

        ListSet ls = null;
        DataBox dbox = null;
        
        String sql = "";
        List list = null;
        
        try 
        {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql =
                "\n  SELECT"+
                "\n       a.course_code, a.org_id, a.item_id, a.item_parameters,"+
                "\n       a.previous, a.next, a.exit, a.abandon,"+
                "\n       b.res_href"+
                "\n  FROM"+
                "\n       tys_item a, tys_resource b"+
                "\n  WHERE 1=1"+
                "\n       AND a.course_code = b.course_code"+
                "\n       AND a.org_id = b.org_id"+
                "\n       AND a.item_id = b.item_id"+
                "\n       AND a.course_code = ':course_code'"+
                "\n       AND a.item_id = ':item_id'";
            
            sql = sql.replaceAll( ":course_code", StringManager.chkNull( courseID ) );
            sql = sql.replaceAll( ":item_id", StringManager.chkNull( activityID ) );
            
            ls = connMgr.executeQuery( sql );
            
            while ( ls.next() )
            {
                dbox = ls.getDataBox();
                list.add( dbox );
            }
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return list;        
    }

    public String selectUploadPath( String courseID ) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        
        String sql = "";
        String webPath = "";
        
        try 
        {
            connMgr = new DBConnectionManager();
            
            sql = 
                "\n  SELECT upload_path"+
                "\n  FROM tys_course"+
                "\n  WHERE course_code = ':course_code'";

            sql = sql.replaceAll( ":course_code", courseID );
            
            ls = connMgr.executeQuery( sql );
            
            if ( ls.next() )
            {
                webPath = ls.getString("upload_path");
            }
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return webPath;        
    }

    public Map selectProgressDataMap( String lmsID, String courseID, String orgID, String userID ) throws Exception 
    {
        String tableName = "";
        if ( lmsID.endsWith( "0000" ) )
        {
            tableName = "tz_beta_progress";
        }
        else
        {
            tableName = "tz_progress";
        }
        
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        Map resultMap = new HashMap();
        
        try 
        {
            connMgr = new DBConnectionManager();
            
            String[] lmsKey = lmsID.split( "_" );
            
            sql =
                "\n  SELECT oid, lesson_count FROM " + tableName +
                "\n  WHERE 1 = 1 " +
                "\n      AND subj = " + SQLString.Format( lmsKey[0] ) +
                "\n      AND year = " + SQLString.Format( lmsKey[1] ) +
                "\n      AND subjseq = "+ SQLString.Format( lmsKey[2] ) +
                "\n      AND userid = " + SQLString.Format( userID ) +
                "\n      AND lesson = '" + courseID + "_" + orgID + "'" +
                "\n      AND lessonstatus = 'complete'";
            
            ls = connMgr.executeQuery( sql );

            while ( ls.next() )
            {
                resultMap.put( ls.getString("oid"), ls.getString("lesson_count") );
            }
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return resultMap;
    }
}
