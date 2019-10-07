/*
 * @(#)AttendanceAdminBean.java
 *
 * Copyright(c) 2006, Jin-pil Chung
 * All rights reserved.
 */

package com.ziaan.study;

import java.util.ArrayList;

import com.ziaan.common.SubjComBean;
import com.ziaan.complete.FinishBean;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;

public class AttendanceAdminBean {

    public AttendanceAdminBean() 
    {
    }
    
    /**
     * 과정,연도,기수에 해당하는 클래스의 수를 반환한다.
     * 
     * @param box
     * @return int  class 갯수를 반환
     * @throws Exception
     */
    public int getClassid( RequestBox box ) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        StringBuffer sql = new StringBuffer();
        int classid = 0;

        try 
        {
            String ss_gyear = box.getString("s_gyear");
            String ss_subj = box.getStringDefault("s_subjcourse","ALL");
            String ss_subjseq = box.getStringDefault("s_subjseq","ALL");
            
            connMgr = new DBConnectionManager();
            
            sql.append( "SELECT classid FROM tz_subjseq " );
            sql.append( "WHERE 1=1 " );
            sql.append( "     AND subj=" );
            sql.append( SQLString.Format(ss_subj) );
            sql.append( "     AND YEAR=" );
            sql.append( SQLString.Format(ss_gyear) );
            sql.append( "     AND subjseq=" );
            sql.append( SQLString.Format(ss_subjseq) );
            
            ls = connMgr.executeQuery( sql.toString() );
            
            ArrayList dayList = new ArrayList();
            
            if ( ls.next() )
            {
                classid = ls.getInt("classid");
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql.toString());
            throw new Exception("sql = " + sql.toString() + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return classid;           
    }

    /**
     * 로그인한 사용자의 출석 리스트
     * 
     * @param box
     * @param dayInfo
     * @return ArrayList
     * @throws Exception
     */
    public ArrayList selectAttendanceListForId(RequestBox box, String[] dayInfo ) throws Exception 
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        ArrayList list = null;
        StringBuffer sql = new StringBuffer();

        try 
        {
            //검색 조건
            String ss_subj = box.getStringDefault("s_subj","");         //과정
            String ss_subjseq = box.getStringDefault("s_subjseq","");   //과정 차수
            String ss_gyear = box.getStringDefault("s_year","");        //과정 차수
            String v_userid = box.getSession("userid");
            
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            String subQuery = "";
            subQuery = generateQuery( dayInfo );
            
            sql.append( "SELECT c.subj, c.YEAR, c.subjseq, c.subjnm, c.edustart, c.eduend, " );
            sql.append( "  get_compnm (b.comp, 2, 2) companynm, get_compnm (b.comp, 2, 4) compnm, " );
            sql.append( "  '' jikupnm, b.userid, " );
            sql.append( "  b.NAME, b.email, c.isonoff " );
            
            sql.append( subQuery );
            
            sql.append( " FROM tz_student a, tz_member b, vz_scsubjseq c " );
            sql.append( " WHERE A.userid=B.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq " );
            sql.append(" and c.scsubj = " );
            sql.append( SQLString.Format(ss_subj) );
            sql.append( " and c.year = " );
            sql.append( SQLString.Format(ss_gyear) );
            sql.append( " and c.subjseq=" );
            sql.append( SQLString.Format(ss_subjseq) );
            sql.append( " and A.userid=" );
            sql.append( SQLString.Format(v_userid) );
            
            ls = connMgr.executeQuery( sql.toString() );
            
            DataBox dbox = null;
            while( ls.next() )
            {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace(ex, box, sql.toString());
            throw new Exception("sql1 = " + sql.toString() + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;             
	}

    /**
     * 출석부 리스트
     * 
     * @param box
     * @param dayInfo
     * @return ArrayList
     * @throws Exception
     */
    public ArrayList selectAttendanceList( RequestBox box, String[] dayInfo ) throws Exception
    {
		DBConnectionManager connMgr = null;
        ListSet ls = null;

        ArrayList list = null;
        StringBuffer sql = new StringBuffer();

        try 
        {
            if(box.getString("s_action").equals("go"))
            {
                //검색 조건
                String ss_gyear = box.getString("s_gyear");
                String ss_uclass = box.getStringDefault("s_upperclass","ALL");
                String ss_mclass = box.getStringDefault("s_middleclass","ALL");
                String ss_lclass = box.getStringDefault("s_lowerclass","ALL");
                String ss_subj = box.getStringDefault("s_subjcourse","ALL");
                String ss_subjseq = box.getStringDefault("s_subjseq","ALL");
                String ss_classno = box.getStringDefault("s_classno", "ALL" );
                
                String  v_orderColumn= box.getString("p_orderColumn");           //정렬할 컬럼명
                String  v_orderType     = box.getString("p_orderType");          //정렬할 순서

                connMgr = new DBConnectionManager();
                list = new ArrayList();
                
                String subQuery = "";
                subQuery = generateQuery( dayInfo );
                
                sql.append( "SELECT c.subj, c.YEAR, c.subjseq, c.subjnm, c.edustart, c.eduend, " );
                sql.append( "  get_compnm (b.comp, 2, 2) companynm, get_compnm (b.comp, 2, 4) compnm, " );
                sql.append( "  '' jikupnm, b.userid, " );
                sql.append( "  b.NAME, b.email, c.isonoff " );
                
                sql.append( subQuery );
              
                sql.append( " FROM tz_student a, tz_member b, vz_scsubjseq c " );
                sql.append( " WHERE A.userid=B.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq " );
                sql.append( " and c.gyear = " );
                sql.append( SQLString.Format(ss_gyear) );
                
                if ( !ss_classno.equals("ALL") )
                {
                    sql.append( " and a.CLASS=" );                    
                    sql.append( SQLString.Format(insertPad(ss_classno)) );
                }
                
                if (!ss_uclass.equals("ALL"))
                {
                    sql.append( " and C.scupperclass = " );
                    sql.append( SQLString.Format(ss_uclass) );
                }
                if (!ss_mclass.equals("ALL"))
                {
                    sql.append( " and C.scmiddleclass = " );
                    sql.append( SQLString.Format(ss_mclass) );
                }
                if (!ss_lclass.equals("ALL"))
                {
                    sql.append( " and C.sclowerclass = " );
                    sql.append( SQLString.Format(ss_lclass) );
                }
                if (!ss_subj.equals("ALL"))
                {
                    sql.append(" and C.scsubj = " );
                    sql.append( SQLString.Format(ss_subj) );
                }
                if (!ss_subjseq.equals("ALL"))
                {
                    sql.append( " and c.subjseq=" );
                    sql.append( SQLString.Format(ss_subjseq) );
                }
                
                if (v_orderColumn.equals("subj"))    v_orderColumn = "C.subj";
                if (v_orderColumn.equals("compnm1")) v_orderColumn = "get_compnm(b.comp,2,2)";
                if (v_orderColumn.equals("compnm2")) v_orderColumn = "get_compnm(B.comp,2,4)";
                if (v_orderColumn.equals("userid"))  v_orderColumn = "b.userid ";
                if (v_orderColumn.equals("name"))    v_orderColumn = "b.name ";
                if (v_orderColumn.equals("jiknm"))   v_orderColumn = "get_jikwinm(b.jikwi,b.comp)";
                
                if( v_orderColumn.equals("") )
                {
                    sql.append( " order by C.subj,C.year,C.subjseq" );
                }
                else 
                {
                    sql.append( " order by " );
                    sql.append( v_orderColumn );
                    sql.append( v_orderType );
                }
                
                ls = connMgr.executeQuery( sql.toString() );

                DataBox dbox = null;
                while( ls.next() )
                {
                    dbox = ls.getDataBox();
                    list.add(dbox);
                }
            }
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace(ex, box, sql.toString());
            throw new Exception("sql1 = " + sql.toString() + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;        
    }
    
    /**
     * 날짜정보를 반환 ( tz_offsubjlecture 테이블에 등록 기준)
     * 
     * @param box
     * @param isExcel 엑셀 출력일 경우 전체, 아닐 경우 5개만 출력 
     * @return String[]
     * @throws Exception
     */
    public String[] getDayInfo( RequestBox box, boolean isExcel ) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        StringBuffer sql = new StringBuffer();
        String[] dayInfo = null;

        try {
            //검색 조건
            String ss_gyear = box.getString("s_gyear");
            String ss_subj = box.getStringDefault("s_subjcourse","ALL");
            String ss_subjseq = box.getStringDefault("s_subjseq","ALL");
            String ss_classno = box.getStringDefault("s_classno", "ALL" );
            
            if ( ss_classno.equals("ALL") )
                ss_classno = "1";
            
            connMgr = new DBConnectionManager();
            
            sql.append( "SELECT  " );
            sql.append( "  DISTINCT lectdate " );
            sql.append( "FROM tz_offsubjlecture " );
            sql.append( "WHERE 1=1 " );
            sql.append( "  AND subj=" );
            sql.append( SQLString.Format(ss_subj) );
            sql.append( "  AND YEAR=" );
            sql.append( SQLString.Format(ss_gyear) );
            sql.append( "  AND subjseq=" );
            sql.append( SQLString.Format(ss_subjseq) );
            sql.append( "  AND classno=" );
            sql.append( SQLString.Format(ss_classno) );
            sql.append( "ORDER BY lectdate " );
            
            ls = connMgr.executeQuery( sql.toString() );
            
            ArrayList dayList = new ArrayList();
            
            while ( ls.next() )
            {
                dayList.add( ls.getString("lectdate") );
            }
            
            int count = 0 ;
            
            if ( !isExcel )
            {
                if ( dayList.size() > 5 )
                    dayInfo = new String[5];
                else
                    dayInfo = new String[dayList.size()];
            }
            else
            {
                dayInfo = new String[dayList.size()];
            }
            
            for ( int i=0; i<dayInfo.length; i++ )
            {
                dayInfo[i] = (String) dayList.get(i);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql.toString());
            throw new Exception("sql = " + sql.toString() + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return dayInfo;            
    }

    /**
     * 특정ID의 날짜정보를 반환 (오늘 이전 날짜까지만 반환 -오늘 포함)
     * 
     * @param box
     * @return String[]
     * @throws Exception
     */
    public String[] getDayInfoToday( RequestBox box ) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        StringBuffer sql = new StringBuffer();
        ArrayList dayList = null; 

        try {
            //검색 조건
            String ss_subj = box.getString("s_subj");
            String ss_subjseq = box.getString("s_subjseq");
            String ss_gyear = box.getString("s_year");
            String v_userid = box.getSession("userid");

            connMgr = new DBConnectionManager();
            
            sql.append( "SELECT  " );
            sql.append( "  DISTINCT lectdate " );
            sql.append( "FROM tz_offsubjlecture offsl " );
            sql.append( "WHERE lectdate <= to_char(sysdate, 'YYYYMMDD') " );
            sql.append( "  AND subj=" );
            sql.append( SQLString.Format(ss_subj) );
            sql.append( "  AND YEAR=" );
            sql.append( SQLString.Format(ss_gyear) );
            sql.append( "  AND subjseq=" );
            sql.append( SQLString.Format(ss_subjseq) );
            sql.append( "  AND class=(select class from tz_student where subj=offsl.subj and year=offsl.year and subjseq=offsl.subjseq and userid=" );
            sql.append( SQLString.Format(v_userid) );
            sql.append( ") ORDER BY lectdate " );
            
            ls = connMgr.executeQuery( sql.toString() );

            dayList = new ArrayList();
            
            while ( ls.next() )
            {
                dayList.add( ls.getString("lectdate") );
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql.toString());
            throw new Exception("sql = " + sql.toString() + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        
        return (String[]) dayList.toArray( new String[dayList.size()] );
    }    
    
    /*
     * 3자리 이하 문자열의 좌측을 0으로 채운다.
     * 
     * @param str 숫자 문자열
     * @return 좌측이 0으로 채워진 4자리 문자열
     */
    private String insertPad( String str ) 
    {
        String[] pad = { "000", "00", "0" };
        int len = str.length()-1;
        
        if ( len < pad.length )
        {
            str = pad[len] + str;
        }
        
        return str;
    }

    private String insertPad( int i ) 
    {
        return insertPad( String.valueOf(i) );
    }

    /*
     * 날짜정보를 가지고 출석여부 값을 가져오는 서브쿼리를 만든다. 
     * @param dayInfo
     * @return
     */
    private String generateQuery( String[] dayInfo )
    {
        StringBuffer sb = new StringBuffer();
        
        for ( int i=0; i<dayInfo.length; i++ )
        {
            sb.append( getQuery(dayInfo[i]) );
        }
        
        return sb.toString();
    }
    
    private String getQuery( String day )
    {
        StringBuffer sb = new StringBuffer();
        sb.append(",DECODE( SIGN(TO_CHAR(SYSDATE,'YYYYMMDD')-'");
        sb.append( day );
        sb.append("'),'-1','-', ");
        sb.append("nvl((SELECT DECODE (isattend, 'Y', 'O', SUBSTR (atttime,1,2) || ':'  || SUBSTR (atttime,3,4) ) ");
        sb.append("from tz_attendance where subj=a.subj and year=a.year and subjseq=a.subjseq and userid=a.userid and attdate ='" );
        sb.append( day );
        sb.append("'),'X') ) D");
        sb.append(  day  );
        
        return sb.toString();
    }
    
    /**
	점수재계산
	@param box      receive from the form object and session
	@return isOk    1:insert success,0:insert fail
	**/
	 private int exeReCalculate(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		int isOk = 0;
        
        String  ss_gyear      = box.getStringDefault("s_gyear","ALL");     //년도
        String  ss_subjcourse = box.getStringDefault("s_subjcourse","ALL");//과정&코스
        String  ss_subjseq    = box.getStringDefault("s_subjseq","ALL");   //과정 차수
        String v_eduterm = "";
        //점수재계산
        FinishBean fbean = new FinishBean();
        SubjComBean scbean = new SubjComBean();

		try {
			connMgr = new DBConnectionManager();
			v_eduterm = scbean.getEduTermClosedGubun(connMgr, ss_subjcourse, ss_subjseq , ss_gyear);
			//System.out.println("this v_eduterm=============............................."+v_eduterm);
			if(v_eduterm.equals("4") || v_eduterm.equals("5")){
			  if(!ss_gyear.equals("ALL") && !ss_subjcourse.equals("ALL") && !ss_subjseq.equals("ALL")){
                box.put("p_subj", ss_subjcourse);
                box.put("p_year", ss_gyear);
                box.put("p_subjseq", ss_subjseq);
                //System.out.println("########################################재계산시작########################################");
                isOk = fbean.OffSubjectCompleteRerating(box);
                //System.out.println("########################################재계산  끝########################################");
              }
            }

		}
		catch(Exception ex) {
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage());
		}
		finally {
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return isOk;
	}
}
