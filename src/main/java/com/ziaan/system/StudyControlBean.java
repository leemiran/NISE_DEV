// **********************************************************
//  1. ��      ��: �������� BEAN
//  2. ���α׷���: StudyControlBean.java
//  3. ��      ��: �н��������� BEAN
//  4. ȯ      ��: JDK 1.5
//  5. ��      ��: 1.0
//  6. ��      ��: ������ 2008. 10. 13
//  7. ��      ��: �谭�� 2008. 12. 13
// **********************************************************
package com.ziaan.system;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class StudyControlBean { 

	/**
	�ð� �������� ���� ��ȸ
	@param box      receive from the form object and session
	@return SelectStudyControl
	*/
	public ArrayList SelectTimeList(RequestBox box) throws Exception { 
	
		DBConnectionManager	connMgr	= null;
		ListSet ls          = null;
		
		ArrayList list 	    = null;
		String sql          = "";
		DataBox dbox		= null;
		
		try { 
		    connMgr = new DBConnectionManager();
			list = new ArrayList();
		
			sql  = " SELECT SEQ,  \n";
			sql += "		STHH, \n";
			sql += "		ENHH, \n";
			sql += "		WW, \n";
			sql += "		LDATE, \n";
			sql += "		ISUSE \n";
			sql += " FROM TZ_TIMELIMIT ";
				
		    ls = connMgr.executeQuery(sql);
		
		    while ( ls.next() ) { 
			dbox = ls.getDataBox();
			list.add(dbox);
		    }
		} catch ( Exception ex ) { 
		    ErrorManager.getErrorStackTrace(ex, box, sql);
		    throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
		    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
		    if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return list;
	}
	
	/**
	�ð��������� �����Ҷ�
	@param box      receive from the form object and session
	@return isOk    1:update success,0:update fail
	*/
	public int updateStudyControl(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet             ls      = null;
		PreparedStatement   pstmt   = null;
		String              sql     = "";
		int isOk = 0;
		
		String v_sthh		= box.getString("p_sthh");
		String v_enhh		= box.getString("p_enhh");
		Vector v_vww		= box.getVector("p_ww");
		String v_use		= box.getString("p_timeuse");
		String v_ww			= "";
		int	   v_seq		= box.getInt("p_tseq");
		Enumeration em = v_vww.elements();
		
		while (em.hasMoreElements()) {
			v_ww += (String) em.nextElement() + "|";
		}
		
		try { 
		    connMgr = new DBConnectionManager();
		    connMgr.setAutoCommit(false);
		    
		    sql  = "UPDATE TZ_TIMELIMIT \n";
		    sql += "SET    STHH = ? \n";
		    sql += "      ,ENHH = ? \n";
		    sql += "      ,WW = ? \n";
		    sql += "      ,ISUSE = ? \n";
		    sql += "		 ,ldate=to_char(sysdate, 'YYYYMMDDHH24MISS') \n";
		    sql += "WHERE SEQ = ? ";
	
		    pstmt = connMgr.prepareStatement(sql);
		
		    pstmt.setString(1,  v_sthh);
		    pstmt.setString(2,  v_enhh);
		    pstmt.setString(3,  v_ww);
		    pstmt.setString(4,  v_use);
		    pstmt.setInt(5,  v_seq);
		
		    isOk = pstmt.executeUpdate();
		    
			if ( isOk > 0)	{ 
				connMgr.commit();
			} else connMgr.rollback();
		   
		} catch ( Exception ex ) { 
		    ErrorManager.getErrorStackTrace(ex, box, sql);
		    throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
		    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		    if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
		    if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return isOk;
	}
	
	/**
	������������ ��ȸ
	@param box      receive from the form object and session
	@return SelectStudyControl
	*/
	public ArrayList SelectBreakList(RequestBox box) throws Exception { 
	
		DBConnectionManager	connMgr	= null;
		ListSet ls          = null;
		
		ArrayList list 	    = null;
		String sql          = "";
		DataBox dbox		= null;
		
		try { 
		    connMgr = new DBConnectionManager();
				list = new ArrayList();
		
				sql  = " SELECT SEQ,  \n";
				sql += "		BREAKDT, \n";
				sql += "		BREAKNM, \n";
				sql += "		LDATE, \n";
				sql += "		ISUSE \n";
				sql += " FROM TZ_BREAKINFO \n";
				sql += " ORDER BY BREAKDT ASC ";
				
		    ls = connMgr.executeQuery(sql);
		
		    while ( ls.next() ) { 
		    	dbox = ls.getDataBox();
				list.add(dbox);
		    }
		} catch ( Exception ex ) { 
		    ErrorManager.getErrorStackTrace(ex, box, sql);
		    throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
		    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
		    if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return list;
	}
	
	
	/**
	���������� ��Ͻ� �ߺ�����
	@param box      receive from the form object and session
	@return SelectStudyControl
	*/
	public int isDuplication(RequestBox box) throws Exception { 
	
		DBConnectionManager	connMgr	= null;
		ListSet ls          = null;
	
		int isOk 	    = 0;
		String sql     = "";
		DataBox dbox	= null;
	
		String v_breakdt = box.getString("p_breakdt");
		if(!(v_breakdt.indexOf("-") < 0)) {
		v_breakdt = v_breakdt.replaceAll("-", ""); // '-' �Ǵ� '.'���� �Է¹޴´�.
		}
		if(!(v_breakdt.indexOf(".") < 0)) {
		v_breakdt = v_breakdt.replaceAll(".", ""); // '-' �Ǵ� '.'���� �Է¹޴´�.
		}
		v_breakdt = v_breakdt.substring(4,v_breakdt.length());//20081215
	
		try { 
			connMgr = new DBConnectionManager();
		
			sql  = " SELECT COUNT(SEQ)  \n";
			sql += " FROM TZ_BREAKINFO \n";
			sql += " WHERE BREAKDT  = '" + v_breakdt + "'";
				
			ls = connMgr.executeQuery(sql);
		
			while ( ls.next() ) { 
			 isOk = ls.getInt(1);
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
	
		return isOk;
	}
	
	/**
	������ �������� ����Ҷ�
	@param box      receive from the form object and session
	@return isOk    1:update success,0:update fail
	*/
	public int insertBreakInfo(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet             ls      = null;
		PreparedStatement   pstmt   = null;
		String              sql     = "";
		int isOk = 0;
		
		String v_breakdt = box.getString("p_breakdt");
		if(!(v_breakdt.indexOf("-") < 0)) {
			v_breakdt = v_breakdt.replaceAll("-", ""); // '-' �Ǵ� '.'���� �Է¹޴´�.
		}
		if(!(v_breakdt.indexOf(".") < 0)) {
			v_breakdt = v_breakdt.replaceAll(".", ""); // '-' �Ǵ� '.'���� �Է¹޴´�.
		}
		v_breakdt = v_breakdt.substring(4,v_breakdt.length());//1215 �� �� �� �Է¹޴�
		String v_breaknm = box.getString("p_breaknm");
		String v_breakuse = box.getString("p_breakuse");
		
		try { 
			connMgr = new DBConnectionManager();
		    connMgr.setAutoCommit(false);
		    
		    sql = "SELECT MAX(SEQ) FROM TZ_BREAKINFO";
		   
		    ls = connMgr.executeQuery(sql);
		    int v_seq = 0;
		    while(ls.next()) {
		    	v_seq = ls.getInt(1) + 1;
		    }
		   
		    sql  = "INSERT INTO TZ_BREAKINFO(SEQ, BREAKDT, BREAKNM, ISUSE, LDATE) \n";
		    sql += "VALUES(?,?,?,?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MMSS'))";
						   
		    pstmt = connMgr.prepareStatement(sql);
		
		    pstmt.setInt(1,  v_seq);
		    pstmt.setString(2,  v_breakdt);
		    pstmt.setString(3,  v_breaknm);
		    pstmt.setString(4,  v_breakuse);
		
		    isOk = pstmt.executeUpdate();
		   
			if ( isOk > 0)	{ 
				connMgr.commit();
			} else connMgr.rollback();
		} catch ( Exception ex ) { 
		    ErrorManager.getErrorStackTrace(ex, box, sql);
		    throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
		    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		    if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
		    if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return isOk;
	}
	
	/**
	������ �������� �����Ҷ�
	@param box      receive from the form object and session
	@return isOk    1:update success,0:update fail
	*/
	public int updateBreakInfo(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet             ls      = null;
		PreparedStatement   pstmt   = null;
		String              sql     = "";
		int isOk = 0;
		
		String v_seq = box.getString("p_seq");
		String v_breakdt = box.getString("p_dbreakdt_" + v_seq);
		String v_breaknm = box.getString("p_dbreaknm_" + v_seq);
		String v_breakuse = box.getString("p_dbreakuse_" + v_seq);
		
		if(v_breakdt.indexOf("-") > -1) {
			v_breakdt = v_breakdt.replace("-", ""); // '-' �Ǵ� '.'���� �Է¹޴´�.
		}
		if(v_breakdt.indexOf(".") > -1) {
			v_breakdt = v_breakdt.replace(".", ""); // '-' �Ǵ� '.'���� �Է¹޴´�.
		}
		v_breakdt = v_breakdt.substring(4,v_breakdt.length());//1215 �� �� �� �Է¹޴�
		
		try { 
			connMgr = new DBConnectionManager();
		    connMgr.setAutoCommit(false);		  
		   
		    sql  = "UPDATE TZ_BREAKINFO \n";
		    sql += "   SET BREAKDT = ? \n";
		    sql += "     , BREAKNM = ? \n";
		    sql += "     , ISUSE = ? \n";
		    sql += "WHERE SEQ = ? \n";
						   
		    pstmt = connMgr.prepareStatement(sql);
		
		    pstmt.setString(1,  v_breakdt);
		    pstmt.setString(2,  v_breaknm);
		    pstmt.setString(3,  v_breakuse);
		    pstmt.setInt(4,  Integer.parseInt(v_seq));
		
		    isOk = pstmt.executeUpdate();
		  
			if ( isOk > 0)	{ 
				connMgr.commit();
			} else connMgr.rollback();
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return isOk;
	}
	
	
	/**
	������ �������� �����Ҷ�
	@param box      receive from the form object and session
	@return isOk    1:update success,0:update fail
	*/
	public int deleteBreakInfo(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet             ls      = null;
		PreparedStatement   pstmt   = null;
		String              sql     = "";
		int isOk = 0;

		String v_seq = box.getString("p_seq");
		
		try { 
			connMgr = new DBConnectionManager();
		    connMgr.setAutoCommit(false);	
		    
			sql  = "DELETE FROM TZ_BREAKINFO WHERE SEQ = ?  \n";
						   
			pstmt = connMgr.prepareStatement(sql);
		
			pstmt.setInt(1,  Integer.parseInt(v_seq));
		
			isOk = pstmt.executeUpdate();
			
			if ( isOk > 0)	{ 
				connMgr.commit();
			} else connMgr.rollback();
		 
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return isOk;
	}

}