//**********************************************************
//1. 제      목: 고용보험 관리자페이지
//2. 프로그램명: 고용보험 훈련실시신고
//3. 개      요:
//4. 환      경:
//5. 버      젼:
//6. 작      성: 2005.07.10 이연정
//7. 수      정:
//**********************************************************
package com.ziaan.msidepar;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;

import com.ziaan.goyong.*;
import com.ziaan.propose.*;
import com.ziaan.library.*;
import com.ziaan.system.*;

public class UserMsideParBean {

public UserMsideParBean() {}

/**
* 다면평가 유저별 리스트
* @param box          receive from the form object and session
* @return ArrayList
* @throws Exception
*/
public ArrayList selectmsidelist(RequestBox box) throws Exception {
  DBConnectionManager connMgr = null;
  DataBox dbox                = null;
  ListSet ls                  = null;
  ArrayList list              = null;
  String sql                  = "";
	
  String  s_userid    = box.getSession("userid");  //유저아이디

  try {
      connMgr = new DBConnectionManager();
      list = new ArrayList();

      /*
      sql += " select t.*, r.gubuncd, r.gubuncddt from(										\n";
		sql += " select x.msidecode, x.title, x.startdt, x.enddt, x.tuserid,x.isopen		\n";
		sql += " ,(select count(y.idgubun) from  TZ_MSIDEPAR_TARGET where   idgubun='1'  and msidecode =x.msidecode  and tuserid ='"+s_userid+"')ishigh			\n";
		sql += " ,(select count(y.idgubun) from  TZ_MSIDEPAR_TARGET where   idgubun='2'  and msidecode =x.msidecode  and tuserid ='"+s_userid+"')ismid			\n";
		sql += " ,(select count(y.idgubun) from  TZ_MSIDEPAR_TARGET where   idgubun='3'  and msidecode =x.msidecode  and tuserid ='"+s_userid+"')islow			\n";
		sql += " ,(SELECT COUNT (y.idgubun) FROM tz_msidepar_target  WHERE idgubun = '1'   AND msidecode = x.msidecode  AND tuserid = 'lee1' and isexam='Y') okhigh		\n";
		sql += "  ,(SELECT COUNT (y.idgubun) FROM tz_msidepar_target  WHERE idgubun = '2'   AND msidecode = x.msidecode  AND tuserid = 'lee1' and isexam='Y') okmid		\n";
		sql += "  ,(SELECT COUNT (y.idgubun) FROM tz_msidepar_target  WHERE idgubun = '3'   AND msidecode = x.msidecode  AND tuserid = 'lee1' and isexam='Y') oklow		\n";
		sql += " from		(select a.msidecode, a.title, a.startdt, a.enddt, b.tuserid,				\n";
		sql += " (case  WHEN  (to_char(a.startdt) <=to_char(sysdate,'YYYYMMDDHH24MISS')) 				\n";
		sql += " 		and (to_char(a.enddt) >=to_char(sysdate,'YYYYMMDDHH24MISS')) THEN 'Y' else 'N' END  )isopen  	\n";
		sql += " from tz_msidepar_mas a, tz_msidepar_user b									\n";
		sql += " where 1=1 and a.isdel !='Y' and a.msidecode = b.msidecode and b.tuserid = '"+s_userid+"' )x,			\n";
		sql += " (select idgubun from TZ_MSIDEPAR_TARGET where tuserid='"+s_userid+"')y )t, tz_msidepar_mas r where t.msidecode = r.msidecode			\n";
		sql += " group by t.msidecode, t.title, t.startdt, t.enddt, t.tuserid, t.isopen, t.ishigh, t.ismid, t.islow , r.gubuncd, r.gubuncddt order by t.title, t.startdt asc	\n";
		*/

      sql = " select x.msidecode, x.title, x.startdt, x.enddt,x.gubuncd, x.gubuncddt,  \n "
    	  +" 	   (CASE                                                               \n "
    	  +"             WHEN (TO_CHAR (x.startdt) <=                                  \n "
    	  +"                         TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS')             \n "
    	  +"                  )                                                        \n "
    	  +"             AND (TO_CHAR (x.enddt) >=                                     \n "
    	  +"                         TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS')             \n "
    	  +"                 )                                                         \n "
    	  +"                THEN 'Y'                                                   \n "
    	  +"             ELSE 'N'                                                      \n "
    	  +"          END                                                              \n "
    	  +"         ) ISOPEN,                                                         \n "
    	  +" 	   ishigh,ismid,islow,okhigh,okmid,oklow                               \n "
    	  +" from  tz_msidepar_mas x,                                                  \n "
    	  +" (                                                                         \n "
    	  +" 	select a.msidecode,a.tuserid,ishigh,ismid,islow,okhigh,okmid,oklow     \n "
    	  +" 	from tz_msidepar_user a ,                                              \n "
    	  +" 	(                                                                      \n "
    	  +" 	select msidecode,tuserid,                                              \n "
    	  +" 	 sum(decode(idgubun,1,1,0))ishigh,                                     \n "
    	  +" 		   sum(decode(idgubun,2,1,0))ismid,                                \n "
    	  +" 		   sum(decode(idgubun,3,1,0)) islow,                               \n "
    	  +" 		   sum(decode(idgubun||isexam,'1Y',1,0))okhigh,                    \n "
    	  +" 		   sum(decode(idgubun||isexam,'2Y',1,0))okmid,                     \n "
    	  +" 		   sum(decode(idgubun||isexam,'3Y',1,0))oklow                      \n "
    	  +" 	from tz_msidepar_target                                                \n "
    	  +" 	where tuserid='"+s_userid+"'                                           \n "
    	  +" 	group by msidecode,tuserid                                             \n "
    	  +" 	)b                                                                     \n "
    	  +" 	where a.msidecode = b.msidecode(+) and   a.tuserid=b.tuserid(+)        \n "
    	  +" )b                                                                        \n "
    	  +" where x.msidecode=b.msidecode(+) and b.tuserid='"+s_userid+"'             \n ";

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
* 다면평가 유저별 리스트
* @param box          receive from the form object and session
* @return ArrayList
* @throws Exception
*/
public ArrayList targetselectmsidelist(RequestBox box) throws Exception {
  DBConnectionManager connMgr = null;
  DataBox dbox                = null;
  ListSet ls                  = null;
  ArrayList list              = null;
  String sql                  = "";
	
  String  s_userid    = box.getSession("userid");  //유저아이디

  try {
      connMgr = new DBConnectionManager();
      list = new ArrayList();
/*
		sql += " SELECT b.msidecode, b.title, b.CONTENTS, b.startdt, b.enddt, decode(a.idgubun,'1','상사','2','동료','3','부하')idgubun,		\n";
		sql += "       a.tuserid, c.NAME														\n";
		sql += "   FROM tz_msidepar_target a, tz_msidepar_mas b, tz_member c						\n";
		sql += "  WHERE 1 = 1		and b.isdel !='Y'															\n";
		sql += "    AND a.msidecode = b.msidecode		\n";
		sql += "    AND a.tuserid = c.userid				\n";
		sql += "   AND a.suserid = '"+s_userid+"'	order by b.title, b.startdt asc	\n";
*/
	  
	  
	  sql += "   SELECT distinct a.*,				\n";
	  sql += "      DECODE (a.isexam,'Y',100,0) AS PERCENT					\n";
	  sql += " FROM (SELECT b.gubuncd, b.gubuncddt, b.msidecode, b.title, b.CONTENTS,	\n";
	  sql += "           b.startdt, b.enddt,		\n";
	  sql += "           DECODE (a.idgubun,		\n";
	  sql += "                  '1', '상사',		\n";
	  sql += "                  '2', '동료',		\n";
	  sql += "                  '3', '부하'		\n";
	  sql += "                 ) idgubun,			\n";
	  sql += "          a.tuserid, c.NAME	,a.isexam		\n";
	  sql += "     FROM tz_msidepar_target a, tz_msidepar_mas b, tz_member c	\n";
	  sql += "    WHERE 1 = 1							\n";
	  sql += "      AND b.isdel != 'Y'				\n";
	  sql += "     AND a.msidecode = b.msidecode		\n";
	  sql += "     AND a.tuserid = c.userid			\n";
	  sql += "     AND a.suserid = '"+s_userid+"') a	\n";
	  sql += "  LEFT OUTER JOIN						\n";
	  sql += "  (SELECT   a.resultseq, a.gubuncd, a.gubuncddt, COUNT (a.userid) AS cnt,a.puserid \n";
	  sql += "     FROM tz_ability_rslt_dt a, tz_msidepar_target b		\n";
	  sql += "  WHERE 			\n";
	  sql += "    a.puserid='"+s_userid+"'		\n";
	  sql += "   and  a.userid=b.tuserid		\n";
	  sql += "   and  a.puserid=b.suserid		\n";
	  sql += "  GROUP BY resultseq, gubuncd, gubuncddt,puserid,userid) e		\n";
	  sql += "    ON a.gubuncd = e.gubuncd				\n";
	  sql += "   AND a.gubuncddt = e.gubuncddt			\n";
	  sql += "  AND a.msidecode = e.resultseq			\n";
	  sql += "   LEFT OUTER JOIN						\n";
	  sql += "   (SELECT   gubuncd, gubuncddt, COUNT (gubuncd) AS totcnt	\n";
	  sql += "        FROM tz_ability_pro				\n";
	  sql += "   GROUP BY gubuncd, gubuncddt) d		\n";
	  sql += "  ON a.gubuncd = d.gubuncd AND a.gubuncddt = d.gubuncddt	\n";
	  
		
      ls = connMgr.executeQuery(sql);
      while (ls.next()) {
          dbox = ls.getDataBox();
		  dbox.put("d_percent"	,new Double(ls.getDouble("percent"))); 
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
* 유저별 대상자 리스트
* @param box          receive from the form object and session
* @return ArrayList
* @throws Exception
*/
public ArrayList selectSerchUserlist(RequestBox box) throws Exception{
  DBConnectionManager connMgr = null;
  DataBox dbox                = null;
  ListSet ls                  = null;
  ArrayList list              = null;
  String sql                  = "";
	
    String  s_userid    = box.getSession("userid");  //유저아이디
	String  v_isgubun   = box.getString("p_isgubun");
	String  v_msidecode = box.getString("p_msidecode");

  try {
      connMgr = new DBConnectionManager();
      list = new ArrayList();
      
    /*
	//sql +="	SELECT   userid, NAME, jikwi, email, cono, comp, hometel, handphone,	\n";
	//sql +="	    get_compnm (comp, userid) compnm,											\n";
	//sql +="	    get_jikupnm (jikryul, jikup, comp) jikupnm,									\n";
	//sql +="	    get_jikryulnm (jikryul) jikryulnm, deptnam, jikryul, jikup					\n";
	//sql +="	 FROM tz_member a, tz_msidepar_target b	,tz_msidepar_user c						\n";
	//sql +="	 WHERE a.userid = b.suserid		and c.tuserid = b.tuserid   	and c.msidecode = b.msidecode				\n";
	//sql +="  and c.tuserid='"+s_userid+"' and c.MSIDECODE = '"+v_msidecode+"' and b.idgubun='"+v_isgubun+"'					\n";
	//sql +="	ORDER BY comp ASC, jikwi ASC, NAME ASC											\n";
	*/
	
	sql +="	SELECT   userid, NAME, '' jikwi, email, '' cono, comp, hometel, handphone,	\n";
	sql +="	    get_compnm (comp) compnm,											\n";
	sql +="	    lvl_nm jikupnm,									\n";
	sql +="	    '' jikryulnm, '' deptnam, '' jikryul, jikup					\n";
	sql +="	 FROM tz_member a, tz_msidepar_target b	,tz_msidepar_user c						\n";
	sql +="	 WHERE a.userid = b.suserid		and c.tuserid = b.tuserid   	and c.msidecode = b.msidecode				\n";
	sql +="  and c.tuserid='"+s_userid+"' and c.MSIDECODE = '"+v_msidecode+"' and b.idgubun='"+v_isgubun+"'					\n";
	sql +="	ORDER BY comp ASC, jikwi ASC, NAME ASC											\n";

	System.out.println(sql);
	
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
* 회원검색
* @param box          receive from the form object and session
* @return ArrayList
* @throws Exception
*/
public ArrayList selectUserinfolist(RequestBox box) throws Exception{
  DBConnectionManager connMgr = null;
  DataBox dbox                = null;
  ListSet ls                  = null;
  ArrayList list              = null;
  String sql                  = "";
	
	String  s_userid	= box.getSession("userid");
//   String  v_userid    = box.getStringDefault("p_userid","NOT");  //유저아이디
//	String  v_name		= box.getStringDefault("p_name","NOT");    //이름
	String  v_selbox    = box.getString("p_selbox");
	String  v_searchtext    = box.getString("p_searchtext");

  try {
      connMgr = new DBConnectionManager();
      list = new ArrayList();

      sql += " SELECT userid, name, '' jikwi, email, '' cono, comp, hometel, handphone  		\n";
		sql += " ,get_compnm(comp) compnm	      									\n";
		sql += " ,lvl_nm jikupnm									\n";
		sql += " ,'' jikryulnm											\n";
		sql += " ,'' deptnam , '' jikryul	, '' jikup													\n";
		sql += "  FROM TZ_MEMBER															\n";
		sql += "  WHERE 1 = 1																\n";
		if(v_selbox.equals("id")){
			sql += " 	and userid = '"+v_searchtext+"'				\n";
		}else if(v_selbox.equals("name")){		
			sql += " 	and name like '%"+v_searchtext+"%'					\n";
		}else{
		sql += "  and userid = '"+v_searchtext+"' and name = '"+v_searchtext+"'						\n";
		}
		sql += "  order by comp asc, jikwi asc, name asc	  								\n";


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
대상자 등록
@param box          receive from the form object and session
@return int
*/
public int insertmem(RequestBox box) throws Exception{
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
	
	String v_isgubun		= box.getString("p_isgubun");
	
	String v_msidecode		= box.getString("p_msidecode");
	
	Vector vc_insel = box.getVector("p_insel");
	
	String vv_userid = "";
	
	
	try { 
		connMgr1 = new DBConnectionManager();
		connMgr = new DBConnectionManager();
		connMgr.setAutoCommit(false);
		
		//1.대상자 테이블(TZ_MSIDEPAR_TARGET) 등록처리  
		
		sbSQL.append (" insert into TZ_MSIDEPAR_TARGET (msidecode,tuserid,suserid,idgubun,luserid,ldate)  \n")                   
		.append (" values( ?, ? ,? ,? ,?, to_char(sysdate,'YYYYMMDDHH24MISS')  )                                          \n");
		
		
		
		
		pstmt = connMgr.prepareStatement(sbSQL.toString());
		
		for (int i=0; i<vc_insel.size();i++) {  
			
			vv_userid = vc_insel.elementAt(i).toString();
			
			//System.out.println(v_msidecode+":"+s_userid+":"+vv_userid+":"+v_isgubun+":"+s_userid);
			sql = "";
			sql +=" select * from TZ_MSIDEPAR_TARGET where msidecode = '"+v_msidecode+"'	\n";
			sql +=" and    tuserid = '"+s_userid+"'  and suserid ='"+vv_userid+"' \n";	
			
			ls = connMgr1.executeQuery(sql);
			while (ls.next()) {
				ischeck = -100;
			}  
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			
			idx = 1;
			pstmt.setString(idx++, v_msidecode);
			pstmt.setString(idx++, s_userid);
			pstmt.setString(idx++, vv_userid);
			pstmt.setString(idx++, v_isgubun);
			pstmt.setString(idx++, s_userid );
			isOk = pstmt.executeUpdate();
			
		}
		isOk = ischeck;
		
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
대상자 삭제
@param box          receive from the form object and session
@return int
*/
public int deletemem(RequestBox box) throws Exception{
	DBConnectionManager	connMgr	  = null;
	DBConnectionManager	connMgr1  = null;
	PreparedStatement   pstmt     = null;
	StringBuffer        sbSQL     = new  StringBuffer();
	ListSet ls                    = null;
	
	int ischeck = 1;
	int isOk = 1;
	int idx = 1;
	
	
	String s_userid 		= box.getSession("userid");
	String v_isgubun		= box.getString("p_isgubun");
	String v_msidecode		= box.getString("p_msidecode");
	String v_gubuncd        = box.getString("p_gubuncd");    //역량분류
	String v_gubuncddt      = box.getString("p_gubuncddt");  //역량군
	
	Vector vc_outsel = box.getVector("p_outsel");
	String vv_userid = "";
	String sql = "";
	
	try { 
		connMgr = new DBConnectionManager();
		connMgr1 = new DBConnectionManager();
		connMgr.setAutoCommit(false);
		 
		
		//1.대상자 테이블(TZ_MSIDEPAR_TARGET) 삭제처리  
		
		sbSQL.append (" delete TZ_MSIDEPAR_TARGET where msidecode = ? and tuserid=? and suserid=?  \n");                   
		pstmt = connMgr.prepareStatement(sbSQL.toString());
		
		for (int i=0; i<vc_outsel.size();i++) {  
			
			vv_userid = vc_outsel.elementAt(i).toString();
			
			sql = " select count(*) cnt from TZ_ABILITY_RSLT_DT where gubuncd = '"+v_gubuncd+"' and gubuncddt = '"+v_gubuncddt+"' and puserid = '" +vv_userid+"'";
			
			ls = connMgr1.executeQuery(sql);
			while (ls.next()) {
				if(ls.getInt("cnt") > 0){
					ischeck = -100;
				}
			}  
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			
			//System.out.println(v_msidecode+":"+s_userid+":"+vv_userid+":"+v_isgubun+":"+s_userid);
			
			idx = 1;
			pstmt.setString(idx++, v_msidecode);
			pstmt.setString(idx++, s_userid);
			pstmt.setString(idx++, vv_userid);
			isOk = pstmt.executeUpdate();
			
		}
		isOk = ischeck;
		
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

public int tsideparinsertok(RequestBox box) throws Exception{
    DBConnectionManager	connMgr	= null;
	
	DataBox dbox                = null;
    ListSet ls                  = null;
	
    PreparedStatement   pstmt     = null;
    StringBuffer        sbSQL     = new  StringBuffer();
    int isOk = 1;
    int idx = 1;
   
  
    String v_userid 	= box.getSession("userid");

	String s_userid 	= box.getString("p_userid"); 
	
	String v_resultseq		= box.getString("p_resultseq");

	
  
    
    try { 
        connMgr = new DBConnectionManager();
        connMgr.setAutoCommit(false);

		
        
    sbSQL.append (" update tz_msidepar_target set isexam = 'Y' where  tuserid='"+s_userid+"' and suserid = '"+v_userid+"'    and msidecode = '"+v_resultseq+"'  \n");                 
	
		pstmt = connMgr.prepareStatement(sbSQL.toString());
		
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





}
