// **********************************************************
//  1. 제      목: 차세대 Masterform Bean
//  2. 프로그램명: NewEduStartBean.java
//  3. 개      요: 차세대 Masterform Bean
//  4. 환      경: JDK 1.4
//  5. 버      젼:
//  6. 작      성: Chung Jin-pil 2007/05/14
//  7. 수      정:
// **********************************************************
package com.ziaan.lcms;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.EduEtc1Bean;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class NewEduStartBean { 

    public static final char DELIMITER_CHAR = (char)2;
    
    public NewEduStartBean() {}
    
    /**
     * Preview Object 정보를 가져온다. 
     *
     * @param box receive from the form object and session
     * @return List
     * @throws Exception
     */ 
    public List selectPreviewObject( RequestBox box ) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        List list = new ArrayList();
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            String s_subjseq = box.getSession("s_subjseq");
            String s_userid = box.getSession("userid");
            
            sql =
                "\n  SELECT a.module, a.lesson, a.OID, b.starting, b.npage, " +
                "\n         (SELECT stu_page FROM tz_progress " +
                "\n          WHERE subj = " + SQLString.Format( s_subj ) + 
                "\n            AND YEAR = " + SQLString.Format( s_year ) +
                "\n            AND subjseq = " + SQLString.Format( s_subjseq ) +
                "\n            AND OID = a.OID AND userid = " + SQLString.Format( s_userid ) + ") isstudy " +
                "\n  FROM tz_subjobj a, tz_object b, tz_previewobj c  " +
                "\n  WHERE a.subj = "  + SQLString.Format( s_subj ) +
                "\n     AND a.OID = b.OID " +
                "\n     AND a.OID = c.OID " +
                "\n     AND a.subj = c.subj " +
                "\n     AND a.TYPE IN ('SC', 'TM', 'TT') " +
                "\n  ORDER BY a.module, a.lesson, a.ordering ASC ";
            
            ls = connMgr.executeQuery( sql );

            while ( ls.next() )
            {
                list.add( ls.getDataBox() );
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

    /**
     * Object 정보를 가져온다. 
     *
     * @param box receive from the form object and session
     * @return List
     * @throws Exception
     */ 
    public List selectObject( RequestBox box ) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        List list = new ArrayList();
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            String s_subjseq = box.getSession("s_subjseq");
            String s_userid = box.getSession("userid");
            
            sql =
                "\n  SELECT a.module, a.lesson, a.TYPE TYPE, a.OID OID, " +
                "\n         a.sdesc sdesc, a.ordering ordering, b.starting starting, " +
                "\n         NVL (a.TYPES, '1001') TYPES, b.npage, " +
                "\n         NVL ((SELECT stu_page FROM tz_progress " +
                "\n               WHERE subj = " + SQLString.Format( s_subj ) +
                "\n                  AND YEAR = " + SQLString.Format( s_year ) +
                "\n                  AND subjseq = " + SQLString.Format( s_subjseq ) +
                "\n                  AND OID = a.OID " +
                "\n                  AND userid = " +  SQLString.Format( s_userid ) + "), 0) isstudy " +
                "\n  FROM tz_subjobj a, tz_object b, tz_subjmodule c, tz_subjlesson d  " +
                "\n  WHERE 1=1 " +
                "\n     AND a.subj = c.subj " +
                "\n     AND c.subj = d.subj " +
                "\n     AND a.module = c.module " +
                "\n     AND c.module = d.module " +
                "\n     AND a.lesson = d.lesson " +
                "\n     AND a.subj = " + SQLString.Format( s_subj ) +
                "\n     AND a.OID = b.OID " +
                "\n     AND a.TYPE IN ('SC', 'TM', 'TT') " +
                "\n  ORDER BY a.module, a.lesson, a.ordering ASC ";
            
            ls = connMgr.executeQuery( sql );

            while ( ls.next() )
            {
                list.add( ls.getDataBox() );
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
    
    
    
    
    /**
     * 과정명 가져오기 
     * @param box receive from the form object and session
     * @return Stringt
     * @throws Exception
     */    
    public String selectSubjnm(RequestBox box) throws Exception {
        
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";    
        String subjnm = "";

        try { 
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");        

            sql = 
                "SELECT subjnm FROM tz_subj WHERE subj = " + StringManager.makeSQL(s_subj);
            
            ls = connMgr.executeQuery(sql);
            
            if ( ls.next() )
            {
                subjnm = ls.getString( "subjnm" );
            }
            ls.close();
            
        }
        catch ( Exception ex )
        { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
        finally
        { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return subjnm;
    }
    
    /**
     * 진도정보를 가져온다 
     *
     * @param box receive from the form object and session
     * @return Map
     * @throws Exception
     */ 
    public Map selectProgressData(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        Map resultMap = new HashMap();
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            String s_subjseq = box.getSession("s_subjseq");
            String s_userid = box.getSession("userid");
            
            sql =
                "\n  SELECT lesson, oid, lesson_count FROM tz_progress" +
                "\n  WHERE 1 = 1 " +
                "\n      AND subj = " + SQLString.Format( s_subj ) +
                "\n      AND year = " + SQLString.Format( s_year ) +
                "\n      AND subjseq = "+ SQLString.Format( s_subjseq ) +
                "\n      AND LESSON_COUNT > 0 " +
                "\n      AND userid = " + SQLString.Format( s_userid );

            ls = connMgr.executeQuery( sql );

            while ( ls.next() )
            {
                resultMap.put( ls.getString("lesson") + "_" + ls.getString("oid"), ls.getString("lesson_count") );
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
    
    /**
     * LiveShare Parameter 정보를 가져온다 
     *
     * @param box receive from the form object and session
     * @return Map
     * @throws Exception
     */    
    public Map getLiveShareParams(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        Map resultMap = new HashMap();
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_userid = box.getSession("userid");
            
            String v_subj = box.getString("p_subj");
            String v_year = box.getString("p_year");
            String v_subjseq = box.getString("p_subjseq");
            
            String v_module = box.getString("p_module");
            String v_lesson = box.getString("p_lesson");
            String v_object = box.getString("p_object");
            String v_type = box.getString("p_type");
            
            sql =
                "\n  SELECT session_time, first_end     " +
                "\n  FROM tz_progress                   " +
                "\n  WHERE 1 = 1                        " +
                "\n     AND subj = " + SQLString.Format( v_subj ) +
                "\n     AND year = " + SQLString.Format( v_year ) +
                "\n     AND subjseq = " + SQLString.Format( v_subjseq ) +
                "\n     AND lesson = " + SQLString.Format( v_lesson ) +
                "\n     AND OID = " + SQLString.Format( v_object ) +
                "\n     AND userid = " + SQLString.Format( s_userid );
            
            ls = connMgr.executeQuery( sql );

            String v_isstudycomp = "";
            String v_session_time = "";
            
            if ( ls.next() )
            {
                v_session_time = convertSecond( ls.getString( "session_time" ) );
                if ( ls.getString( "first_end" ).equals("") )
                {
                    v_isstudycomp = "view";
                }
                else
                {
                    v_isstudycomp = "study";
                }
            }
            else
            {
                v_isstudycomp = "view";
                v_session_time = "0";
            }
            
            resultMap.put( "isstudycomp", v_isstudycomp );
            resultMap.put( "session_time", v_session_time );
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
    
    /*
     * 00:00:00 (시:분:초)를 초 단위 값으로 변경한다.
     *
     */
    private String convertSecond( String inputTime )
    {
        String resultTime = "0";
        
        String[] time = inputTime.split(":");
        
        if ( time.length != 3 )
        {
            resultTime = "0";
        }
        else
        {
            try
            {
                resultTime = String.valueOf((Integer.parseInt(time[0])*60*60) + (Integer.parseInt(time[1])*60) + (Integer.parseInt(time[2])));
            }
            catch ( Exception e )
            {
                resultTime = "0";
            }
        }
        
        return resultTime;
    }

    /**
     * Lesson 리스트
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public List selectLesson(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        List list = new ArrayList();
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");
            
            sql =
                "\n  SELECT a.module, b.lesson, b.sdesc lessonnm, " +
                "\n         (SELECT oid from tz_subjobj where subj=a.subj and module=b.module and lesson=b.lesson and rownum=1) oid " +
                "\n  FROM tz_subjmodule a, tz_subjlesson b " +
                "\n  WHERE a.subj = " + SQLString.Format( s_subj ) +
                "\n     AND a.module = b.module " +
                "\n     AND a.subj = b.subj " +
                "\n  ORDER BY a.module, b.lesson ";
            
            ls = connMgr.executeQuery( sql );

            while ( ls.next() )
            {
                list.add( ls.getDataBox() );
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


    public List selectPreviewTree(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        List list = new ArrayList();
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");
            
            sql =
                "\n  SELECT OID, OIDNM, ORDERING " +
                "\n  FROM TZ_PREVIEWOBJ " +
                "\n  WHERE SUBJ = " + SQLString.Format( s_subj ) +
                "\n  ORDER BY ORDERING ";            
            
            ls = connMgr.executeQuery( sql );

            while ( ls.next() )
            {
                list.add( ls.getDataBox() );
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
    
    public List selectTree(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        List list = new ArrayList();
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");
            
            sql =
                "\n  SELECT  " +
                "\n     A.MODULE MODULE, A.SDESC MODULENM, NVL(A.TYPES,'1001') MTYPES,  " +
                "\n     B.LESSON LESSON, B.SDESC LESSONNM, NVL(B.TYPES,'1001') LTYPES,  " +
                "\n     C.TYPE TYPE, C.OID OID, C.SDESC SDESC, C.ORDERING ORDERING, " +
                "\n     D.STARTING STARTING, NVL(C.TYPES,'1001') OTYPES, D.NPAGE " +
                "\n  FROM " +
                "\n     TZ_SUBJMODULE A, " +
                "\n     TZ_SUBJLESSON B,  " +
                "\n     (SELECT SUBJ, TYPES, MODULE, LESSON, TYPE, OID, SDESC, ORDERING FROM TZ_SUBJOBJ " +
                "\n      WHERE SUBJ = " + SQLString.Format( s_subj ) +
                "\n         AND TYPE IN ('SC','TM','TT') ) C, " +
                "\n     TZ_OBJECT D " +
                "\n  WHERE 1=1 " +
                "\n     AND A.SUBJ = " + SQLString.Format( s_subj ) +
                "\n     AND A.MODULE = B.MODULE " +
                "\n     AND A.SUBJ = B.SUBJ(+) " +
                "\n     AND B.SUBJ = C.SUBJ(+)  " +
                "\n     AND B.MODULE = C.MODULE(+) " +
                "\n     AND B.LESSON = C.LESSON(+) " +
                "\n     AND C.OID = D.OID(+) " +
                "\n  ORDER BY A.MODULE, B.LESSON ";                
            
            ls = connMgr.executeQuery( sql );

            while ( ls.next() )
            {
                list.add( ls.getDataBox() );
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

    public Map selectJindo(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        Map map = new HashMap();
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            String s_subjseq = box.getSession("s_subjseq");
            String s_userid = box.getSession("userid");
            
            sql =
                "\n  SELECT a.lesson, a.oid, ROUND((a.stu_page/b.npage)*100,0) jindo " +
                "\n  FROM TZ_PROGRESS a, TZ_OBJECT b " +
                "\n  WHERE a.subj = " + SQLString.Format( s_subj ) +
                "\n     AND a.year = " + SQLString.Format( s_year ) +
                "\n     AND a.subjseq = " + SQLString.Format( s_subjseq ) +
                "\n     AND a.userid = " + SQLString.Format( s_userid ) +
                "\n     AND a.oid = b.oid ";
            
            ls = connMgr.executeQuery( sql );

            String key = "";
            while ( ls.next() )
            {
                key = ls.getString("lesson") + ls.getString("oid"); 
                map.put( key, ls.getString("jindo") );
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
        
        return map;        
    }

    public String getSubjnm(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        String subjnm = "";
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");
            
            sql =
                "\n  SELECT subjnm FROM tz_subj WHERE subj = " + SQLString.Format( s_subj );
            
            ls = connMgr.executeQuery( sql );

            while ( ls.next() )
            {
                subjnm = ls.getString( "subjnm" ); 
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
        
        return subjnm;         
    }

    public Map selectSubjInfo(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        Map map = new HashMap();
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            String s_subjseq = box.getSession("s_subjseq");
            
            sql =
            	"\n  SELECT NVL (TO_CHAR (TO_DATE (edustart, 'YYYYMMDDHH24MISS'), 'YYYY'), '----' ) startedyear,  " +
            	"\n         NVL (TO_CHAR (TO_DATE (edustart, 'YYYYMMDDHH24MISS'), 'MM.DD'), '--.--' ) started,  " +
            	"\n         NVL (TO_CHAR (TO_DATE (eduend, 'YYYYMMDDHH24MISS'), 'YYYY'), '----' ) endedyear,  " +
            	"\n         NVL (TO_CHAR (TO_DATE (eduend, 'YYYYMMDDHH24MISS'), 'MM.DD'), '--.--' ) ended,  " +
            	"\n         ROUND (TO_CHAR (TO_DATE (eduend, 'YYYYMMDDHH24MISS') - SYSDATE) ) + 1 remainday,  " +
            	"\n         TO_CHAR (TO_DATE (eduend, 'YYYYMMDDHH24MISS'), 'YYYYMMDD') endday  " +
            	"\n    FROM tz_subjseq  " +
                "\n   WHERE subj = " + SQLString.Format( s_subj ) +
                "\n     AND year = " + SQLString.Format( s_year ) +
                "\n     AND subjseq = " + SQLString.Format( s_subjseq );  
            
            ls = connMgr.executeQuery( sql );

            if ( ls.next() )
            {
                map.put( "startedyear", ls.getString("startedyear") );
                map.put( "started", ls.getString("started") );
                map.put( "endedyear", ls.getString("endedyear") );
                map.put( "ended", ls.getString("ended") );
                map.put( "remainday", ls.getString("remainday") );
                map.put( "endday", ls.getString("endday") );
            }
            else
            {
                map.put( "startedyear", "----" );
                map.put( "started", "--.--" );
                map.put( "endedyear", "----" );
                map.put( "ended", "--.--" );
                map.put( "remainday", "-" );
                map.put( "endday", "-" );
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
        
        return map;           
    }

    public Map getStartingOid(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        Map map = new HashMap();
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");
            String s_year = box.getSession("s_year");
            String s_subjseq = box.getSession("s_subjseq");
            String s_userid = box.getSession("userid");
            
            sql =
                "\n  SELECT a.subj, a.type, a.module, a.lesson, a.ordering, a.OID,  " +
                "\n         DECODE (b.first_end, NULL, 'N', 'Y') isend, b.stu_page " +
                "\n  FROM tz_subjobj a, tz_progress b " +
                "\n  WHERE a.subj = " + SQLString.Format( s_subj ) +
                "\n     AND b.YEAR(+) = " + SQLString.Format( s_year ) +
                "\n     AND b.subjseq(+) = " + SQLString.Format( s_subjseq ) +
                "\n     AND a.subj = b.subj(+) " +
                "\n     AND a.lesson = b.lesson(+) " +
                "\n     AND a.OID = b.OID(+) " +
                "\n     AND b.userid(+) = " + SQLString.Format( s_userid ) +
                "\n  ORDER BY a.lesson, a.ordering ";                
                
            ls = connMgr.executeQuery( sql );

            String key = "";
            while ( ls.next() )
            {
                if ( !ls.getString("isend").equals("Y") )
                {
                    key = "1" + ls.getString("module") + ls.getString("lesson") + ls.getString("oid");
                    map.put( "oid", key );
                    map.put( "stu_page", ls.getString("stu_page") );
                    
                    break;
                }
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
        
        return map; 
    }

    public Map getStartingPrevOid(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql = "";
        Map map = new HashMap();
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_subj = box.getSession("s_subj");
            
            sql =
                "\n  SELECT OID FROM tz_previewobj  " +
                "\n  WHERE subj = " + SQLString.Format( s_subj ) +
                "\n     AND ordering = 1 ";
                
            ls = connMgr.executeQuery( sql );

            String key = "";
            if ( ls.next() )
            {
                key = "3" + ls.getString("oid");
                map.put( "oid", key );
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
        
        return map;         
    }    
    
    /**
     * 진도 Check
     * 
     * @param box
     * @return AJAX Client로 Return 될 값
     * @throws Exception
     */
    public String getStudyProgressObj( RequestBox box ) throws Exception
    {
        DBConnectionManager connMgr = null;
        String sql = "";
        
        String returnValue = "";
        
        try 
        {
            connMgr = new DBConnectionManager();
            
            // Create ProgressDataInfo POJO.
            ProgressInfoData pid = new ProgressInfoData();
            
            pid.setSubj( box.getSession("s_subj") );
            pid.setYear( box.getSession("s_year") );
            pid.setSubjseq( box.getSession("s_subjseq") );
            pid.setUserid( box.getSession("userid") );
            
            pid.setModule( box.getString("p_module") );
            pid.setLesson( box.getString("p_lesson") );
            pid.setObject( box.getString("p_object") );
            pid.setOid( box.getString("p_oid") );
            pid.setPage( box.getInt("p_page") );
            
            // Total Page, Current Page Set
            int[] pages = getPages( connMgr, pid );
            pid.setTotalPage( pages[0] );
            pid.setCurrentPage( pages[1] );
            
            // 진도 체크 여부
            pid.setCompletedObject( isCompletedObject(connMgr, pid) );

            // System.out.println( pid.toString() );

            
            // 진도 안나간 Object 일때만 제한
            if ( !pid.isCompletedObject() )
            {
                // 학습자 권한 Check
    
                // 학습기간 Check
                if ( !isPeriodOfEducation( connMgr, pid ) )
                {
                    // System.out.println( "학습기간Check" );
                    return pid.getOid() + DELIMITER_CHAR + "학습기간이 아니므로 진도체크가 되지 않습니다." + DELIMITER_CHAR + "OK";
                }
    
                
                // TODO KT 요청사항 - userid : contents 일 경우 제한 무시 (테스트 위해)
                boolean ignoreLimit = box.getSession("emp_id").equals("contents");

                if ( pid.isLastPage() && !ignoreLimit )
                {
                    // 차시 진도제한 
                    if ( isLimitOfLesson(connMgr, pid) )
                    {
                        // System.out.println( "차시진도제한" );
                        return pid.getOid() + DELIMITER_CHAR + "본 과정은 1일 수강한도가 제한된 과정으로,교육생께서는 금일 차시분을 마치셨으므로 앞으로는 진도체크가 되지 않습니다."
                               + DELIMITER_CHAR + "ERROR";
                    }
                    
                    // 시간 진도제한
                    if ( isLimitOfTime(connMgr, pid) )
                    {
                        // System.out.println( "시간진도제한" );
                        return pid.getOid() + DELIMITER_CHAR + "각 차시(페이지)마다 정상적으로 학습하셔야 학습진도(완료)에 반영됩니다. 차근차근 학습해 주시길 바랍니다!"
                               + DELIMITER_CHAR + "ERROR";
                    }
                }
            }
            
            // 진도 Check
            returnValue = doProgressCheck( connMgr, pid );

            if ( !pid.isCompletedObject() && pid.isLastPage() )
            {
                // 총 진도율 update
                if ( !updateTotalProgress(connMgr, pid) )
                {
                    // System.out.println( "총진도율 Update Error" );
                    // return pid.getOid() + DELIMITER_CHAR + "진도 체크 후 총 진도율 갱신 작업중에 오류가 발생했습니다. 운영자에게 문의바랍니다." + DELIMITER_CHAR + "ERROR";
                }
            }
        }
        catch ( Exception ex ) 
        {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally
        { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }        
        
        return returnValue;
    }
    
    private boolean isCompletedObject(DBConnectionManager connMgr, ProgressInfoData pid) throws Exception
    {
        ListSet ls = null;
        String sql = "";
        
        boolean isCompletedObject = false; 
        
        try 
        {
            sql =
                "\n  SELECT 'Y' isProgressedObject " +
                "\n  FROM tz_progress " +
                "\n  WHERE subj = " + SQLString.Format( pid.getSubj() ) +
                "\n     AND YEAR = " + SQLString.Format( pid.getYear() ) +
                "\n     AND subjseq = " + SQLString.Format( pid.getSubjseq() ) +
                "\n     AND userid = " + SQLString.Format( pid.getUserid() ) +
                "\n     AND lesson = " + SQLString.Format( pid.getLesson() ) +
                "\n     AND OID = " + SQLString.Format( pid.getObject() ) +
                "\n     AND first_end is not null ";
                
            ls = connMgr.executeQuery( sql );


            if ( ls.next() )
            {
                isCompletedObject = true;
            }
            ls.close();
        }
        catch ( Exception ex ) 
        {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally 
        { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return isCompletedObject;
    }

    private int[] getPages(DBConnectionManager connMgr, ProgressInfoData pid) throws Exception
    {
        ListSet ls = null;
        String sql = "";
        
        int[] pages = { 0, 0 };

        try 
        {
            sql =
                "\n  SELECT " +
                "\n  ( " +
                "\n    SELECT nvl(npage,0) FROM tz_object " +
                "\n    WHERE 1=1 " +
                "\n      AND oid = " + SQLString.Format( pid.getObject() ) +
                "\n  ) totalPage, " +
                "\n  ( " +
                "\n    SELECT nvl(stu_page,0) FROM tz_progress " +
                "\n    WHERE 1=1 " +
                "\n     AND subj = " + SQLString.Format( pid.getSubj() ) +
                "\n     AND year = " + SQLString.Format( pid.getYear() ) +
                "\n     AND subjseq = " + SQLString.Format( pid.getSubjseq() ) +
                "\n     AND userid = " + SQLString.Format( pid.getUserid() ) +
                "\n     AND lesson = " + SQLString.Format( pid.getLesson() ) +
                "\n     AND oid = " + SQLString.Format( pid.getObject() ) +
                "\n  ) currentPage " +
                "\n  from dual ";
            
            ls = connMgr.executeQuery( sql );
            if ( ls.next() )
            {
                pages[0] = ls.getInt("totalPage");
                pages[1] = ls.getInt("currentPage");
            }
            ls.close();
        }
        catch ( Exception ex ) 
        {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally 
        { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }
        
        return pages;
    }

    private boolean updateTotalProgress(DBConnectionManager connMgr, ProgressInfoData pid) throws Exception
    {
        String sql = "";
        
        boolean isUpdate = false;
        
        try 
        {
            sql =
                "\n  UPDATE curriculum_user a  " +
                "\n  SET chasi = " +
                "\n    ( SELECT COUNT(*) FROM tz_progress  " +
                "\n      WHERE subj = a.poi_cd " +
                "\n         AND YEAR = a.poi_year " +
                "\n         AND subjseq = a.poi_round " +
                "\n         AND userid = a.emp_id " +
                "\n         AND first_end IS NOT NULL) " +
                "\n  WHERE a.poi_cd = " + SQLString.Format( pid.getSubj() ) +
                "\n     AND a.poi_year = " + SQLString.Format( pid.getYear() ) +
                "\n     AND a.poi_round = " + SQLString.Format( pid.getSubjseq() ) +
                "\n     AND a.emp_id = " + SQLString.Format( pid.getUserid() );
                
            int isOk = connMgr.executeUpdate(sql);
            
            if ( isOk == 1 )
            {
                isUpdate = true;
            }
        }
        catch ( Exception ex ) 
        {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally 
        { 
        }

        return isUpdate;          
    }

    private String doProgressCheck(DBConnectionManager connMgr, ProgressInfoData pid) throws Exception
    {
        PreparedStatement pstmt = null;
        ListSet ls = null;
        String sql = "";
        
        String returnValue = "";
        int isOk = 0;
        
        try 
        {
            sql =
                "\n  SELECT 'Y' isProgressedObject, lesson_count, first_end, last_edu, ldate, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') sdate, total_time, session_time " +
                "\n  FROM tz_progress " +
                "\n  WHERE subj = " + SQLString.Format( pid.getSubj() ) +
                "\n     AND YEAR = " + SQLString.Format( pid.getYear() ) +
                "\n     AND subjseq = " + SQLString.Format( pid.getSubjseq() ) +
                "\n     AND userid = " + SQLString.Format( pid.getUserid() ) +
                "\n     AND lesson = " + SQLString.Format( pid.getLesson() ) +
                "\n     AND OID = " + SQLString.Format( pid.getObject() );                
                
            ls = connMgr.executeQuery( sql );

            boolean isProgressedObject = false; 
            int lesson_count = 0;
            String first_end = "";
            String last_edu = "";
            String first_field = "";
            String last_field = "";
            String ldate = "";
            String sdate = "";
            String total_time = "";
            String session_time = "";
            
            int stu_page = 0;

            if ( ls.next() )
            {
                isProgressedObject = (ls.getString("isProgressedObject").equals("Y"))? true : false;
                
                lesson_count = ls.getInt("lesson_count");
                first_end = ls.getString("first_end");
                last_edu = ls.getString("last_edu");
                ldate = ls.getString("ldate");
                sdate = ls.getString("sdate");
                total_time = ls.getString("total_time");
                session_time = ls.getString("session_time");
            }
            ls.close();
            

            if ( isProgressedObject )
            {
                
                if ( pid.getTotalPage() == pid.getPage() )      // 현재 page가 마지막 page이면,
                {
                    lesson_count = lesson_count + 1;
                    
                    if ( first_end == null || first_end.equals("") )
                    {
                        first_field = ",first_end = TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') ";
                    }
                    last_field = ",last_end = TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') ";

                    if ( pid.getPage() == 1 )
                    {
                        last_field += ",last_edu = TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') ";
                        last_edu = sdate;
                    }

                    session_time = EduEtc1Bean.get_duringtime(last_edu, sdate);
                    total_time = EduEtc1Bean.add_duringtime(total_time,session_time);
                    stu_page = pid.getTotalPage();
                }
                else if ( pid.getPage() == 1 )                  // 현재 page가 첫 page이면,
                {
                    stu_page = getStuPage( pid.getCurrentPage(), pid.getPage() );
                    last_field = ",last_edu = TO_CHAR( sysdate, 'YYYYMMDDHH24MISS') ";
                }
                else                                            // 현재 page가 첫 page, 마지막 page가 아니면,
                {
                    stu_page = getStuPage( pid.getCurrentPage(), pid.getPage() );
                    
                }
                
                sql = 
                    "\n  UPDATE tz_progress  " +
                    "\n  SET ldate = TO_CHAR( sysdate, 'YYYYMMDDHH24MISS' ) " +
                    "\n      :first_field " +
                    "\n      :last_field " +
                    "\n      ,lesson_count = " + lesson_count +
                    "\n      ,session_time = " + SQLString.Format( session_time ) +
                    "\n      ,total_time = " + SQLString.Format( total_time ) +
                    "\n      ,stu_page = " + stu_page +
                    "\n  WHERE subj = " + SQLString.Format( pid.getSubj() ) +
                    "\n     AND YEAR = " + SQLString.Format( pid.getYear() ) +
                    "\n     AND subjseq = " + SQLString.Format( pid.getSubjseq() ) +
                    "\n     AND userid = " + SQLString.Format( pid.getUserid() ) +
                    "\n     AND lesson = " + SQLString.Format( pid.getLesson() ) +
                    "\n     AND OID = " + SQLString.Format( pid.getObject() );                   
                
                sql = sql.replaceAll( ":first_field", first_field );
                sql = sql.replaceAll( ":last_field", last_field );
                
                // update
                isOk = connMgr.executeUpdate( sql );
            }
            else
            {
                sql =
                    "\n  INSERT INTO tz_progress " +
                    "\n     (SUBJ, YEAR, SUBJSEQ, LESSON, OID, USERID, " +
                    "\n      SESSION_TIME, TOTAL_TIME, FIRST_EDU, LAST_EDU," +
                    "\n      :add_field," +
                    "\n      LESSON_COUNT, LDATE, STU_PAGE) " +
                    "\n  VALUES " +
                    "\n     ( ?, ?, ?, ?, ?, ?, " +
                    "\n       '00:00:00.00', '00:00:00.00', TO_CHAR( sysdate, 'YYYYMMDDHH24MISS'), TO_CHAR( sysdate, 'YYYYMMDDHH24MISS'), " +
                    "\n       :add_value, " +
                    "\n       ?, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS'), ? ) ";
                
                stu_page = 1;

                if ( pid.getTotalPage() == 1 )
                {
                    // If 1 page then Setting first_end, last_end, lesson_count
                    sql = sql.replaceAll( ":add_field,", "first_end, last_end," );
                    sql = sql.replaceAll( ":add_value,", "TO_CHAR( sysdate, 'YYYYMMDDHH24MISS'), TO_CHAR( sysdate, 'YYYYMMDDHH24MISS')," );
                    lesson_count = 1;
                }
                else
                {
                    sql = sql.replaceAll( ":add_field,", "" );
                    sql = sql.replaceAll( ":add_value,", "" );
                    lesson_count = 0;
                }
                
                pstmt = connMgr.prepareStatement( sql );
                pstmt.setString( 1, pid.getSubj() );
                pstmt.setString( 2, pid.getYear() );
                pstmt.setString( 3, pid.getSubjseq() );
                pstmt.setString( 4, pid.getLesson() );
                pstmt.setString( 5, pid.getObject() );
                pstmt.setString( 6, pid.getUserid() );
                pstmt.setInt( 7, lesson_count );
                pstmt.setInt( 8, stu_page );

                isOk = pstmt.executeUpdate();
            }
            
            
            if ( isOk == 1 )
            {
                double percentOfprogressedLesson = ((double)stu_page / (double)pid.getTotalPage()) * 100.0;
                returnValue = pid.getOid() + DELIMITER_CHAR + Math.round(percentOfprogressedLesson) + DELIMITER_CHAR + "OK";
            }
            else
            {
                returnValue = pid.getOid() + DELIMITER_CHAR + "진도 체크 중에 오류가 발생했습니다." + DELIMITER_CHAR + "ERROR";
            }
        }
        catch ( Exception ex ) 
        {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally 
        { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return returnValue;
    }

    private int getStuPage(int stu_page, int p_page)
    {
        int page = 0;
        
        if ( stu_page >= p_page )
        {
            page = stu_page;
        }
        else
        {
            page = p_page;
        }
        
        return page;
    }

    /**
     * 시간 진도제한 
     * : page당 초(설정값) * 총 page수 = 최소학습시간 초 
     *   ex) 20초 * 10 = 200초
     * 
     * @param connMgr
     * @param pid
     * @return
     */
    private boolean isLimitOfTime(DBConnectionManager connMgr, ProgressInfoData pid) throws Exception
    {
        ListSet ls = null;
        String sql = "";
        
        boolean isLimitOfTime = false;
        
        try 
        {
            sql =
                "\n  SELECT time_limit limitedTime FROM curriculum " +
                "\n  WHERE poi_cd = " + SQLString.Format( pid.getSubj() ) +
                "\n     AND poi_year = " + SQLString.Format( pid.getYear() ) +
                "\n     AND poi_round = " + SQLString.Format( pid.getSubjseq() );
            
            ls = connMgr.executeQuery( sql );

            int limitedTime = 0;       // 차시 제한으로 설정된  갯수
            if ( ls.next() )
            {
                limitedTime = ls.getInt("limitedTime");
            }
            ls.close();
            
            if ( limitedTime != 0 )
            {
                
                int progressedTime = 0; 
                sql = 
                    "\n  SELECT ROUND((SYSDATE - TO_DATE(last_edu,'YYYYMMDDHH24MISS')) * 24 * 60 * 60) progressedTime " + 
                    "\n  FROM tz_progress " +
                    "\n  WHERE subj = " + StringManager.makeSQL( pid.getSubj() ) +
                    "\n     AND year = " + StringManager.makeSQL( pid.getYear() ) +
                    "\n     AND subjseq = " + StringManager.makeSQL( pid.getSubjseq() ) +
                    "\n     AND lesson = " + StringManager.makeSQL( pid.getLesson() ) +
                    "\n     AND userid = " + StringManager.makeSQL( pid.getUserid() ) +
                    "\n     AND oid = " + StringManager.makeSQL( pid.getObject() );

                ls = connMgr.executeQuery(sql);
                if ( ls.next() ) 
                { 
                    progressedTime = ls.getInt("progressedTime");
                }
                ls.close();
                
                if ( pid.getTotalPage() > 2 && progressedTime < limitedTime*pid.getTotalPage() ) 
                {
                    isLimitOfTime = true;

                    /*
                    String returnStr = "현재 학습시간은 " + progressedTime + "초입니다.\\r\\n\\r\\n" + 
                                       "본 단원의 최소 학습시간은 " + limitedTime*pid.getTotalPage() +"초" +
                                       "(페이지 당 "+ limitedTime +"초)이며,\\r\\n" +
                                       "최소 학습시간이 경과하여야 단원진도가 인정됩니다.\\r\\n\\r\\n" +
                                       "뒤로 돌아가 다시 학습하여 주십시오.";
                    */
                }
            }
        }
        catch ( Exception ex ) 
        {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally 
        { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return isLimitOfTime;  
    }

    /**
     * 차시 진도제한 ( 고용보험일 경우 7차시)
     * 
     * @param connMgr
     * @param pid
     * @return
     * @throws Exception
     */
    private boolean isLimitOfLesson(DBConnectionManager connMgr, ProgressInfoData pid) throws Exception
    {
        ListSet ls = null;
        String sql = "";
        
        boolean isLimitOfLesson = false;
        
        try 
        {
            sql =
                "\n  SELECT jindo_limit countOfLimitLesson FROM curriculum " +
                "\n  WHERE poi_cd = " + SQLString.Format( pid.getSubj() ) +
                "\n     AND poi_year = " + SQLString.Format( pid.getYear() ) +
                "\n     AND poi_round = " + SQLString.Format( pid.getSubjseq() );
            
            ls = connMgr.executeQuery( sql );

            int countOfLimitLesson = 0;       // 차시 제한으로 설정된  갯수
            if ( ls.next() )
            {
                countOfLimitLesson = ls.getInt("countOfLimitLesson");
            }
            ls.close();
            
            if ( countOfLimitLesson != 0 )
            {
                sql =
                    "\n  SELECT count(distinct lesson) countOfProgressedLesson " +
                    "\n  FROM tz_progress a " +
                    "\n  WHERE subj = " + SQLString.Format( pid.getSubj() ) +
                    "\n     AND YEAR = " + SQLString.Format( pid.getYear() ) +
                    "\n     AND subjseq = " + SQLString.Format( pid.getSubjseq() ) +
                    "\n     AND userid = " + SQLString.Format( pid.getUserid() ) +
                    "\n     AND first_end IS NOT NULL " +
                    "\n     AND substr(first_end, 1, 8) = substr(to_char(sysdate,'YYYYMMDD'),1,8) ";
                
                    // "\n     AND (SELECT COUNT (*) FROM tz_progress " +
                    // "\n          WHERE subj = a.subj  AND YEAR = a.YEAR AND subjseq = a.subjseq AND userid = a.userid AND lesson = a.lesson AND first_end IS NOT NULL " +
                    // "\n         ) = (SELECT COUNT (*) FROM tz_subjobj WHERE subj = a.subj AND YEAR = a.YEAR AND subjseq = a.subjseq AND lesson = a.lesson) ";
    
                ls = connMgr.executeQuery( sql );
                
                int countOfProgressedLesson = 0;       // 학습자가 학습나간 차시 갯수
                if ( ls.next() )
                {
                    countOfProgressedLesson = ls.getInt("countOfProgressedLesson");
                }
                ls.close();
                
    
                sql = 
                    "\n  SELECT decode(count(*),0,'N','Y') isProgressedLesson " +
                    "\n  FROM tz_progress " +
                    "\n  WHERE subj = " + SQLString.Format( pid.getSubj() ) +
                    "\n     AND year = " + SQLString.Format( pid.getYear() ) +
                    "\n     AND subjseq = " + SQLString.Format( pid.getSubjseq() ) +
                    "\n     AND userid = " + SQLString.Format( pid.getUserid() ) +
                    "\n     AND oid = substr( " + SQLString.Format( pid.getOid() ) + " ,6,15) ";
                
                ls = connMgr.executeQuery( sql );
                
                boolean isProgressedLesson = false;       // 진도나갔는지 여부 체크
                if ( ls.next() )
                {
                    isProgressedLesson = (ls.getString("isProgressedLesson").equals("Y")) ? true : false;;
                }
                ls.close();
                
                if ( countOfProgressedLesson >= countOfLimitLesson && !isProgressedLesson )
                {
                    isLimitOfLesson = true;
                }
            }
        }
        catch ( Exception ex ) 
        {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally 
        { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return isLimitOfLesson;        
    }

    /**
     * 학습기간 여부 Check
     * 
     * @param connMgr
     * @param pid
     * @return true : 학습기간 중, false : 학습기간 아님 
     * @throws Exception
     */
    private boolean isPeriodOfEducation(DBConnectionManager connMgr, ProgressInfoData pid) throws Exception
    {
        ListSet ls = null;
        String sql = "";
        
        boolean isPeriodOfEducation = false;
        
        try 
        {
            sql =
                "\n  SELECT 'Y' isPeriodOfEducation " +
                "\n  FROM curriculum " +
                "\n  WHERE poi_cd = " + SQLString.Format( pid.getSubj() ) +
                "\n     AND poi_year = " + SQLString.Format( pid.getYear() ) +
                "\n     AND poi_round = " + SQLString.Format( pid.getSubjseq() ) +
                "\n     AND TO_CHAR(SYSDATE, 'YYYY-MM-DD') " +
                "\n         BETWEEN TO_CHAR (round_fr_dt, 'YYYY-MM-DD') AND TO_CHAR (round_to_dt, 'YYYY-MM-DD') ";

            ls = connMgr.executeQuery( sql );
            if ( ls.next() )
            {
                isPeriodOfEducation = (ls.getString("isPeriodOfEducation").equals("Y")) ? true : false;
            }
        }
        catch ( Exception ex ) 
        {
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally 
        { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return isPeriodOfEducation;
    }
}
