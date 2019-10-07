package com.ziaan.msidepar;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

import com.ziaan.goyong.*;
import com.ziaan.propose.*;
import com.ziaan.library.*;
import com.ziaan.system.*;
import com.ziaan.common.*;


public class AdminMsideParBean {

	public AdminMsideParBean() {}
	
	/**
	* 다면진단대상자관리메인페이지
	* @param box          receive from the form object and session
	* @return ArrayList
	* @throws Exception
	*/
	public ArrayList selectmsidetargetList(RequestBox box) throws Exception{
		  DBConnectionManager connMgr = null;
		  DataBox dbox                = null;
		  ListSet ls                  = null;
		  ArrayList list              = null;
		  String sql                  = "";
			
		  String v_gubuncd  		= box.getString("p_gubuncd");
		  String v_gubuncddt  		= box.getString("p_gubuncddt");
		  String v_msidecode  		= box.getString("p_msidecode");
		  String v_edustart  		= box.getString("s_edustart");
		  String v_eduend  			= box.getString("s_eduend");
		    String v_order      = box.getString("p_order");
		    String v_orderType  = box.getString("p_orderType");                 //정렬할 순서
		  
		  if(!v_edustart.equals("")){
			  v_edustart = v_edustart.substring(0,4)+v_edustart.substring(5,7)+v_edustart.substring(8,10)+"000000";
		  }
		  if(!v_eduend.equals("")){
			  v_eduend = v_eduend.substring(0,4)+v_eduend.substring(5,7)+v_eduend.substring(8,10)+"235959";
		  }
		
		  try {
		      connMgr = new DBConnectionManager();
		      list = new ArrayList();

		      sql +=" select a.*, b.gubuncdnm, c.gubuncddtnm, e.userid, e.name, get_compnm(e.comp) compnm from tz_msidepar_mas a,	\n";
			  sql +=" tz_ability_code b, tz_ability_code_dt c, tz_msidepar_user d, tz_member e 	\n";
			  sql +=" where a.gubuncd=b.gubuncd   and a.gubuncddt=c.gubuncddt   and b.gubuncd=c.gubuncd  	\n";
			  sql +=" and a.msidecode = d.msidecode and d.tuserid=e.userid 	\n";
			  if(!v_gubuncd.equals("ALL")){
				  sql +=" and   a.gubuncd='"+v_gubuncd+"'    \n";
			  }
			  if(!v_gubuncddt.equals("ALL")){
				  sql +=" and a.gubuncddt='"+v_gubuncddt+"'  \n";
			  }
			  if(!v_msidecode.equals("ALL")){
				  sql +=" and a.msidecode='"+v_msidecode+"'  \n";
			  }
			  if(!v_edustart.equals("")){
				  sql +=" and a.startdt >= '"+v_edustart+"'  \n";
			  }
			  if(!v_eduend.equals("")){
				  sql +=" and a.enddt <= '"+v_eduend+"'  \n";
			  }

		      if(v_order.equals("")) {
		    	  sql +=" order by a.startdt asc, e.name ";
		      } else {
		    	  sql +=" order by " + v_order + v_orderType ;
		      }
			  //sql +=" order by a.startdt asc 		\n";
			  
			  //System.out.println(sql);

		      ls = connMgr.executeQuery(sql);
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
		      if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		  }
		  return list;
	}

	

	/**
	* 다면진단대상자관리메인페이지
	* @param box          receive from the form object and session
	* @return ArrayList
	* @throws Exception
	*/
	public ArrayList selectmsidetargetList2(RequestBox box) throws Exception{
		  DBConnectionManager connMgr = null;
		  DataBox dbox                = null;
		  ListSet ls                  = null;
		  ArrayList list              = null;
		  String sql                  = "";
			
		  String v_userid  		= box.getString("ss_userid");

		  String v_msidecode  	= box.getString("p_msidecode");


		  try {
		      connMgr = new DBConnectionManager();
		      list = new ArrayList();

			  sql = "  SELECT  a.msidecode, a.tuserid, a.suserid, get_compnm(b.comp) compnm, b.name, b.position_nm, decode(a.isexam,'Y','평가완료','N','미완료',a.isexam)isexam, " +
			  "  DECODE (a.idgubun, 				"+
              " '1', '상사',							"+
              " '2', '동료',							"+
              " '3', '부하',							"+
              " a.idgubun							"+
              " ) idgubun							"+
              " FROM tz_msidepar_target a, tz_member b	"+
              " WHERE a.suserid = b.userid			"+
              " AND tuserid = '"+v_userid+"' AND msidecode = '"+v_msidecode+"'	" +
              "	order by b.position_nm,	a.suserid	";
 
			  
		      ls = connMgr.executeQuery(sql);
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
		      if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		  }
		  return list;
	}	
	
	
	
	/**
	* 다면진단일정관리메인페이지
	* @param box          receive from the form object and session
	* @return ArrayList
	* @throws Exception
	*/
	public ArrayList selectmsideList(RequestBox box) throws Exception{
		  DBConnectionManager connMgr = null;
		  DataBox dbox                = null;
		  ListSet ls                  = null;
		  ArrayList list              = null;
		  String sql                  = "";
			
		  String v_gubuncd  		= box.getString("p_gubuncd");
		  String v_gubuncddt  		= box.getString("p_gubuncddt");
		  String v_edustart  		= box.getString("s_edustart");
		  String v_eduend  			= box.getString("s_eduend");
		  
		  if(!v_edustart.equals("")){
			  v_edustart = v_edustart.substring(0,4)+v_edustart.substring(5,7)+v_edustart.substring(8,10)+"000000";
		  }
		  if(!v_eduend.equals("")){
			  v_eduend = v_eduend.substring(0,4)+v_eduend.substring(5,7)+v_eduend.substring(8,10)+"235959";
		  }
		
		  

		  try {
		      connMgr = new DBConnectionManager();
		      list = new ArrayList();

		      sql +=" select a.*, b.gubuncdnm, c.gubuncddtnm from tz_msidepar_mas a, tz_ability_code b, tz_ability_code_dt c   		\n";
			  sql +=" where a.gubuncd=b.gubuncd and a.isdel != 'Y'  and a.gubuncddt=c.gubuncddt   and b.gubuncd=c.gubuncd 	\n";				
			  if(!v_gubuncd.equals("ALL")){
				  sql +=" and   a.gubuncd='"+v_gubuncd+"'    \n";
			  }
			  if(!v_gubuncddt.equals("ALL")){
				  sql +=" and a.gubuncddt='"+v_gubuncddt+"'  \n";
			  }
			  if(!v_edustart.equals("")){
				  sql +=" and a.startdt >= '"+v_edustart+"'  \n";
			  }
			  if(!v_eduend.equals("")){
				  sql +=" and a.enddt <= '"+v_eduend+"'  \n";
			  }
			  
			  sql +=" order by a.startdt asc 		\n";
			  
			  //System.out.println(sql);

		      ls = connMgr.executeQuery(sql);
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
		      if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		  }
		  return list;
	}

	/**
	 * 다면진단일정등록
	 * @param box          receive from the form object and session
	 * @return int
	 * @throws Exception
	 */
	public int InsertSch(RequestBox box) throws Exception{
		DBConnectionManager	connMgr	= null;
		
		DBConnectionManager	connMgr1	= null;
		
		DataBox dbox                = null;
		ListSet ls                  = null;
		
		PreparedStatement   pstmt     = null;
		StringBuffer        sbSQL     = new  StringBuffer();
		String sql = "";
		int isOk = 1;
		int ischeck = 1;
		int idx = 1;
		
		
		String s_userid 		= box.getSession("userid");
		String v_gubuncd  		= box.getString("p_gubuncd");
		String v_gubuncddt  	= box.getString("p_gubuncddt");
		String v_edustart  		= box.getString("s_edustart");
		String v_eduend  		= box.getString("s_eduend");
		String v_title  		= box.getString("p_title");
		String v_contents  		= box.getString("p_contents");
		
		v_edustart = v_edustart.substring(0,4)+v_edustart.substring(5,7)+v_edustart.substring(8,10)+"000000"; 
		v_eduend = v_eduend.substring(0,4)+v_eduend.substring(5,7)+v_eduend.substring(8,10)+"235959"; 
		
		
		//System.out.println(v_edustart+":"+v_eduend);
		
		int v_msidecode = 0;
		int cnt = 0;
		
		try { 
			connMgr1 = new DBConnectionManager();
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			/* 자가진단수행 이력이 있는 경우 다면진단 생성불가.. 현재 시스템으로는 데이터 충돌. 추후 수정 필요 */
			sql += " select count(*) as cnt from TZ_ABILITY_RSLT_DT where gubuncd = "+v_gubuncd+" and gubuncddt = "+v_gubuncddt;
			ls = connMgr1.executeQuery(sql);
			while (ls.next()) {
				cnt = ls.getInt("cnt");
			} 
			
			if(ls != null){ls.close();}
			
		    if(cnt > 0){
		    	return isOk = -1;
		    }
			
		    sql = "";
			sql +=" select max(msidecode)msidecode from tz_msidepar_mas 	\n";
			
			ls = connMgr1.executeQuery(sql);
			while (ls.next()) {
				v_msidecode = ls.getInt("msidecode");
			}  
			
			
			sbSQL.append (" insert into tz_msidepar_mas (msidecode, startdt,enddt,title,contents,luserid,ldate,gubuncd,gubuncddt,isdel)  \n")                   
			.append ("  values(?,?,?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'),?,?,'N')                                         \n");
			
			
			
			pstmt = connMgr.prepareStatement(sbSQL.toString());
			
			idx = 1;
			pstmt.setInt(idx++, v_msidecode+1);
			pstmt.setString(idx++, v_edustart);
			pstmt.setString(idx++, v_eduend);
			pstmt.setString(idx++, v_title);
			pstmt.setString(idx++, v_contents);
			pstmt.setString(idx++, s_userid );
			pstmt.setString(idx++, v_gubuncd);
			pstmt.setString(idx++, v_gubuncddt );
			
			isOk = pstmt.executeUpdate();
			
			
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			
			if ( isOk > 0) connMgr.commit();
			else connMgr.rollback();
			
		} catch ( Exception ex ) { 
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
			throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
		} finally { 
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
			if ( connMgr1 != null ) try { connMgr1.setAutoCommit(true); } catch ( Exception e ) { }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return isOk;
	}
	
	/**
    다면진단일정관리 팝업에서 필요한 정보 가져오기...
    @param box      receive from the form object and session
    @return ArrayList
	 */
	public DataBox selectupdatepagebox(RequestBox box) throws Exception {
		DBConnectionManager	connMgr	= null;
		ListSet ls1         = null;
		ArrayList list1     = null;
		String sql1         = "";
		DataBox             dbox    = null;
		
		
		String  v_msidecode= box.getString("s_msidecode");
		
		String startdt = "";
		String enddt = "";
		
		String  v_stop= box.getString("p_stop");
		
		
		try {
			connMgr = new DBConnectionManager();
			list1 = new ArrayList();
			
			sql1  = "";
			sql1 += "	select * from tz_msidepar_mas						";
			sql1 += "		where msidecode='"+v_msidecode+"'	and isdel !='Y'			";
			
			
			ls1 = connMgr.executeQuery(sql1);
			
			while ( ls1.next() ) {
				dbox = ls1.getDataBox();
				if(!v_stop.equals("stop")){
					box.put("p_gubuncd",ls1.getString("gubuncd"));
					box.put("p_gubuncddt",ls1.getString("gubuncddt"));
				}
				
				startdt = ls1.getString("startdt");
				enddt = ls1.getString("enddt");
				
				startdt = startdt.substring(0,4)+"-"+startdt.substring(4,6)+"-"+startdt.substring(6,8);
				enddt = enddt.substring(0,4)+"-"+enddt.substring(4,6)+"-"+enddt.substring(6,8);
				
				box.put("s_edustart",startdt);
				box.put("s_eduend",enddt);
				
			}
		} catch ( Exception ex ) {
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql1 += " + sql1 + "\r\n" + ex.getMessage() );
		} finally {
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return dbox;
	}

	/**
	* 다면진단일정수정
	* @param box          receive from the form object and session
	* @return int
	* @throws Exception
	*/
	public int UpdateSch(RequestBox box) throws Exception{
	    DBConnectionManager	connMgr	= null;
		
		DataBox dbox                = null;
	    ListSet ls                  = null;
		
	    PreparedStatement   pstmt     = null;
	    StringBuffer        sbSQL     = new  StringBuffer();
	    int isOk = 1;
	    int idx = 1;
	   
	  
	    String s_userid 		= box.getSession("userid");

		String v_msidecode  		= box.getString("s_msidecode");
		String v_gubuncd  		= box.getString("p_gubuncd");
		String v_gubuncddt  	= box.getString("p_gubuncddt");
		String v_edustart  		= box.getString("s_edustart");
		String v_eduend  		= box.getString("s_eduend");
		String v_title  		= box.getString("p_title");
		String v_contents  		= box.getString("p_contents");
		
		v_edustart = v_edustart.substring(0,4)+v_edustart.substring(5,7)+v_edustart.substring(8,10)+"000000"; 
		v_eduend = v_eduend.substring(0,4)+v_eduend.substring(5,7)+v_eduend.substring(8,10)+"235959"; 
		
		
		//System.out.println(v_edustart+":"+v_eduend);
		
	  
	    
	    try { 
	        connMgr = new DBConnectionManager();
	        connMgr.setAutoCommit(false);

			
	        
	    sbSQL.append (" update tz_msidepar_mas set startdt = ?,enddt = ?,title = ?," +
	    		" contents = ?,luserid = ?,ldate = to_char(sysdate,'YYYYMMDDHH24MISS') " +
	    		",gubuncd = ? ,gubuncddt = ? where msidecode = ?  \n");                 


	   

			pstmt = connMgr.prepareStatement(sbSQL.toString());
			
	  		idx = 1;
	  		pstmt.setString(idx++, v_edustart);
			pstmt.setString(idx++, v_eduend);
	  		pstmt.setString(idx++, v_title);
	  		pstmt.setString(idx++, v_contents);
	  		pstmt.setString(idx++, s_userid );
	  		pstmt.setString(idx++, v_gubuncd);
	  		pstmt.setString(idx++, v_gubuncddt );
			pstmt.setString(idx++, v_msidecode );
			
			isOk = pstmt.executeUpdate();
	      

	        if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
	        
	        if ( isOk > 0) connMgr.commit();
				 else connMgr.rollback();
	        
	    } catch ( Exception ex ) { 
	   	 connMgr.rollback();
	        ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
	        throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
	    } finally { 
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
	        if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
	        if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
	    }

	    return isOk;
	}
	
	/**
	* 다면진단일정삭제
	* @param box          receive from the form object and session
	* @return int
	* @throws Exception
	*/
	public int DeleteSch(RequestBox box) throws Exception{
	    DBConnectionManager	connMgr	= null;
		
		DataBox dbox                = null;
	    ListSet ls                  = null;
		
	    PreparedStatement   pstmt     = null;
	    StringBuffer        sbSQL     = new  StringBuffer();
	    int isOk = 1;
	    int idx = 1;
	   
	  
	    String s_userid 		= box.getSession("userid");

		String v_msidecode  		= box.getString("s_msidecode");
		
	  
	    
	    try { 
	        connMgr = new DBConnectionManager();
	        connMgr.setAutoCommit(false);

			
	        
	    sbSQL.append (" update tz_msidepar_mas set         " +
	    		"  luserid = ?,ldate = to_char(sysdate,'YYYYMMDDHH24MISS'), " +
	    		" isdel = 'Y' where msidecode = ?  \n");                 


	   

			pstmt = connMgr.prepareStatement(sbSQL.toString());
			
	  		idx = 1;

	  		pstmt.setString(idx++, s_userid );
			pstmt.setString(idx++, v_msidecode );
			
			isOk = pstmt.executeUpdate();
	      

	        if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
	        
	        if ( isOk > 0) connMgr.commit();
				 else connMgr.rollback();
	        
	    } catch ( Exception ex ) { 
	   	 connMgr.rollback();
	        ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
	        throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
	    } finally { 
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
	        if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
	        if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
	    }

	    return isOk;
	}
	
    /**
    다면진단  Select Box 조회
    @param box          receive from the form object and session
    @return ArrayList   리스트
    */
    public ArrayList selectmsideCdDt(RequestBox box) throws Exception {
        DBConnectionManager	connMgr		= null;
        DataBox             dbox    	= null;
        ListSet							ls				= null;
        ArrayList						list			= null;
        String							sql				= "";
        String 							v_gubuncd		= box.getString("p_gubuncd");
		String 							v_gubuncddt		= box.getString("p_gubuncddt");

        try {
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();

            sql  = "SELECT	msidecode, title			";
            sql += "FROM		tz_msidepar_mas					";
            sql += "WHERE	isdel != 'Y' and	GUBUNCD	= '"+ v_gubuncd +"' and	gubuncddt= '"+v_gubuncddt+"' ";
            sql += "ORDER BY title ASC							";
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) {
              dbox = ls.getDataBox();
              list.add(dbox);
            }
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, sql);
            throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) {  try { ls.close();  } catch ( Exception e ) { }  }
            if ( connMgr != null ) {  try {  connMgr.freeConnection();  } catch (Exception e ) { } }
        }

        return list;
    }

	/**
	* 다면피진단대상자삭제
	* @param box          receive from the form object and session
	* @return int
	* @throws Exception
	*/
	public int deletetargetuser(RequestBox box) throws Exception{
		  DBConnectionManager	connMgr	= null;
		  PreparedStatement   pstmt     = null;
		  StringBuffer        sbSQL     = new  StringBuffer();
		  int isOk = 1;
		  int idx = 1;
		 
	
		  String v_msidecode		= box.getString("p_msidecode");
		  
		  Vector vc_outsel = box.getVector("p_outsel");
			
		  String chk = "";
		  String vv_userid = "";
		  String vv_msidecode = "";

		  
		  try { 
		      connMgr = new DBConnectionManager();
		      connMgr.setAutoCommit(false);

		      
		  sbSQL.append (" delete tz_msidepar_user where msidecode = ? and tuserid=?  \n");                   	 

		 pstmt = connMgr.prepareStatement(sbSQL.toString());
		 
		  for (int i=0; i<vc_outsel.size();i++) {  
		    
			  	chk = vc_outsel.elementAt(i).toString();
				
                StringTokenizer st 	= new StringTokenizer(chk, ",");
				vv_userid     		= st.nextToken();
				vv_msidecode     	= st.nextToken();
				
			//System.out.println("StringTokenizer:"+vv_userid+":"+vv_msidecode);
				
				idx = 1;
				pstmt.setString(idx++, vv_msidecode);
				pstmt.setString(idx++, vv_userid);
				isOk = pstmt.executeUpdate();
		    
			}
		  

		      if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
		      
		      if ( isOk > 0) connMgr.commit();
					 else connMgr.rollback();
		      
		  } catch ( Exception ex ) { 
		 	 connMgr.rollback();
		      ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
		      throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
		  } finally { 
		      if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
				if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
		      if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		  }

		  return isOk;
	}

	
	

	/**
	* 다면진단대상자삭제
	* @param box          receive from the form object and session
	* @return int
	* @throws Exception
	*/
	public int deletetargetuser2(RequestBox box) throws Exception{
		  DBConnectionManager	connMgr	= null;
		  PreparedStatement   pstmt     = null;
		  StringBuffer        sbSQL     = new  StringBuffer();
		  int isOk = 1;
		  int idx = 1;
		  
		  Vector vc_outsel = box.getVector("p_outsel2");
			
		  String chk = "";
		  String v_msidecode = "";
		  String v_tuserid = "";
		  String v_suserid = "";
		  String v_idgubun  = "";

		  
		  try { 
		      connMgr = new DBConnectionManager();
		      connMgr.setAutoCommit(false);

		      
		  sbSQL.append (" delete tz_msidepar_target where msidecode = ? and tuserid=? and suserid= ? and idgubun = ?  \n");                   	 

		 pstmt = connMgr.prepareStatement(sbSQL.toString());
		 
		
		 
		  for (int i=0; i<vc_outsel.size();i++) {  
		    
			  	chk = vc_outsel.elementAt(i).toString();
				
                StringTokenizer st 	= new StringTokenizer(chk, ",");
				v_msidecode     = st.nextToken();
				v_tuserid     	= st.nextToken();
				v_suserid     	= st.nextToken();
				v_idgubun     	= st.nextToken();
				/*
				if(v_idgubun.equals("상사")){v_idgubun="1";}
				else if(v_idgubun.equals("동료")){v_idgubun="2";}
				else if(v_idgubun.equals("부하")){v_idgubun="3";}
				*/
			//	 System.out.println(v_msidecode+":"+v_tuserid+":"+v_suserid+":"+v_idgubun);
		
				idx = 1;
				pstmt.setString(idx++, v_msidecode);
				pstmt.setString(idx++, v_tuserid);
				pstmt.setString(idx++, v_suserid);
				pstmt.setString(idx++, v_idgubun);
				isOk = pstmt.executeUpdate();
		    
			}
		  

		      if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
		      
		      if ( isOk > 0) connMgr.commit();
					 else connMgr.rollback();
		      
		  } catch ( Exception ex ) { 
		 	 connMgr.rollback();
		      ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
		      throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
		  } finally { 
		      if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
				if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
		      if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		  }

		  return isOk;
	}
	
	/**
    다면평가피진단자 (Excel 일괄등록)
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
	 */
	public int inserttargetuser(Hashtable data, RequestBox box) throws Exception { 
		
		//System.out.println(" 다면평가대상자 (Excel 일괄등록)");
		
		DBConnectionManager	connMgr	= null;
		PreparedStatement   pstmt     = null;
		StringBuffer        sbSQL     = new  StringBuffer();
		
		StringBuffer        sql = new StringBuffer();
		int isOk = 1;
		int idx = 1;
		int cnt = 0;
		
		int v_result = 0;
		
		ListSet ls = null;
		
		String s_userid 	= box.getSession("userid");
		String v_msidecode 	= box.getString("p_msidecode");
		
		//String v_name 		= (String)data.get("name");							//성명
//        String v_msidecode 	= (String)data.get("msidecode");					//다면평가코드
		String v_userid 	= (String)data.get("userid");						    //아이디
		String v_jikup      = (String)data.get("jikup");                            //직급
		
		
		try { 
			
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			sbSQL.append("INSERT INTO tz_msidepar_user  ( msidecode, tuserid, luserid, ldate, jikup \n")  
			     .append(")values(                                                       	    \n")
			     .append("	?, ? ,? , to_char(sysdate,'YYYYMMDDHH24MISS'), ? )                                                           	\n");
			
			pstmt = connMgr.prepareStatement(sbSQL.toString());
			
			idx = 1;
			
			pstmt.setString(idx++, v_msidecode );
			pstmt.setString(idx++, v_userid );
			pstmt.setString(idx++, s_userid );
			pstmt.setString(idx++, v_jikup );
			
			isOk = pstmt.executeUpdate();
			
			connMgr.commit();
			
			
		} catch ( Exception ex ) { 
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
			throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
		} finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return isOk;
	}
  
	 
	/**
	    다면평가진단자 (Excel 일괄등록)
	    @param box      receive from the form object and session
	    @return isOk    1:update success,0:update fail
	 */
	public int inserttargetuser2(Hashtable data, RequestBox box) throws Exception { 
		
		DBConnectionManager	connMgr	= null;
		PreparedStatement   pstmt     = null;
		StringBuffer        sbSQL     = new  StringBuffer();
		
		StringBuffer        sql = new StringBuffer();
		int isOk = 1;
		int idx = 1;
		int cnt = 0;
		
		int v_result = 0;
		
		ListSet ls = null;
		
		String s_userid 	= box.getSession("userid");
		
		String v_msidecode 	= box.getString("p_msidecode");
		
//	        String v_msidecode 	= (String)data.get("v_msidecode");					//다면평가코드
		String v_userid 	= (String)data.get("v_userid");						//피진단자아이디
		String v_useridp 	= (String)data.get("v_useridp");					//진단자아이디
		String v_grade 		= (String)data.get("v_grade");						//유형
		
		/*
			if(v_grade.equals("상사")){v_grade = "1";}
			else if(v_grade.equals("동료")){v_grade = "2";}
			else {v_grade = "3";}			
		 */
		try { 
			
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			sbSQL.append("INSERT INTO tz_msidepar_target  ( msidecode, tuserid,suserid,idgubun, luserid, ldate 	\n")  
			.append(")values(                                                       	\n")
			.append("	?, ? ,? ,?,?, to_char(sysdate,'YYYYMMDDHH24MISS') )                                                           	\n");
			
			pstmt = connMgr.prepareStatement(sbSQL.toString());
			
			idx = 1;
			
			pstmt.setString(idx++, v_msidecode );
			pstmt.setString(idx++, v_userid );
			pstmt.setString(idx++, v_useridp );
			pstmt.setString(idx++, v_grade );
			pstmt.setString(idx++, s_userid );
			
			isOk = pstmt.executeUpdate();
			
			
			connMgr.commit();
			
			
		} catch ( Exception ex ) { 
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
			throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
		} finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return isOk;
	}	 
	 
	
	    /**
	    다면평가피진단자 (중복확인)
	    @param box      receive from the form object and session
	    @return isOk    1:update success,0:update fail
	    */
	     public int selecttargetuser(Hashtable data, RequestBox box) throws Exception { 
			 
		       DBConnectionManager	connMgr	= null;
		        PreparedStatement   pstmt     = null;
		        
		        String sql = "";
		        int isOk = 0;
		        int idx = 1;
		        int cnt = 0;
		        
		        int v_result = 0;
		       
		        ListSet ls = null;

		
				String v_msidecode 	= box.getString("p_msidecode");
		        String v_name 		= (String)data.get("name");							//성명
	//	        String v_msidecode 	= (String)data.get("msidecode");					//다면평가코드
		        String v_userid 	= (String)data.get("userid");						//아이디
		      
		        
		        try { 
		        
		            connMgr     = new DBConnectionManager();
		       

		            sql  = "SELECT	msidecode			";
		            sql += "FROM		tz_msidepar_user					";
		            sql += "WHERE	msidecode	= '"+ v_msidecode +"' and	tuserid= '"+v_userid+"' ";
					
		            ls = connMgr.executeQuery(sql);

		            while ( ls.next() ) {
						isOk++;
		            }
		            
		        } catch ( Exception ex ) { 
		       	 connMgr.rollback();
		            ErrorManager.getErrorStackTrace(ex, box, sql.toString());
		            throw new Exception("sql = " + sql.toString() + "\r\n" + ex.getMessage() );
		        } finally { 
		            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
		 			 if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
		            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		        }

		        return isOk;
	     }
	

		 
		    /**
		    다면평가진단자 (중복확인)
		    @param box      receive from the form object and session
		    @return isOk    1:update success,0:update fail
		    */
		     public int selecttargetuser2(Hashtable data, RequestBox box) throws Exception { 
				 
			       DBConnectionManager	connMgr	= null;
			        PreparedStatement   pstmt     = null;
			        
			        String sql = "";
			        int isOk = 0;
			        int idx = 1;
			        int cnt = 0;
			        
			        int v_result = 0;
			       
			        ListSet ls = null;

		
					String v_msidecode 	= box.getString("p_msidecode");
	//		        String v_msidecode 	= (String)data.get("v_msidecode");					//다면평가코드
			        String v_userid 	= (String)data.get("v_userid");						//피진단자아이디
					String v_useridp	= (String)data.get("v_useridp");		//진단자아이디
					String v_grade		= (String)data.get("v_grade");		//유형
			        /*
					if(v_grade.equals("상사")){v_grade = "1";}
					else if(v_grade.equals("동료")){v_grade = "2";}
					else if(v_grade.equals("부하")){v_grade = "3";}
					*/
			        try { 
			        
			            connMgr     = new DBConnectionManager();
			       

			            sql  = "SELECT	msidecode			";
			            sql += "FROM		tz_msidepar_target					";
			            sql += "WHERE	msidecode	= '"+ v_msidecode +"' and	tuserid= '"+v_userid+"' and " +
			            		" suserid='"+v_useridp+"' and idgubun = '"+v_grade+"' ";
						
			            ls = connMgr.executeQuery(sql);

			            while ( ls.next() ) {
							isOk++;
			            }
			            
			        } catch ( Exception ex ) { 
			       	 connMgr.rollback();
			            ErrorManager.getErrorStackTrace(ex, box, sql.toString());
			            throw new Exception("sql = " + sql.toString() + "\r\n" + ex.getMessage() );
			        } finally { 
			            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			 			 if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
			            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
			        }

			        return isOk;
		     }		 
		 
		 
}
