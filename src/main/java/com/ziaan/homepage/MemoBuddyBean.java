//**********************************************************
//	1. ��	   ��: �����ּҷ� ����
//	2. ���α׷��� :	MemoBuddyBean.java
//	3. ��	   ��: �����ּҷ� ����
//	4. ȯ	   ��: JDK 1.3
//	5. ��	   ��: 1.0
//	6. ��	   ��:  2003.	7. 13
//	7. ��	   ��:
//**********************************************************

package	com.ziaan.homepage;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code	and	Comments
 */
public class MemoBuddyBean {

	public MemoBuddyBean ()	{}


	/**
	* �ּҷ�ȭ�� ����Ʈ
	* @param box		  receive from the form	object and session
	* @return ArrayList	  �ּҷ�ȭ�� ����Ʈ
	* @throws Exception
	*/
	public ArrayList selectListMemoBuddy(RequestBox	box) throws	Exception {
		DBConnectionManager	connMgr	= null;
		ListSet	ls = null;
		ArrayList list = null;
		String sql = "";
		MemoBuddyData data = null;

		String v_searchtext	= box.getString("p_searchtext");
		String v_search		= box.getString("p_search");

		int	   v_pageno	= box.getInt("p_pageno");
		String s_userid	= box.getSession("userid");


		try	{
			connMgr	= new DBConnectionManager();

			list = new ArrayList();

			//sql	+= " select	a.seq seq, a.buddy buddy, b.name buddynm, b.cono cono, b.userid, b.comp comp ,	   ";
			sql	+= " select	a.seq seq, a.buddy buddy, b.email email, b.name buddynm, '' cono, b.userid, '' comp ,  ";
			//sql	+= "		get_compnm(comp,2,4) compnm, b.jikwi jikwi, get_jikupnm(b.jikup,b.comp) jikwinm ";
			sql	+= "		c.compnm, '' jikwi, '' jikwinm, b.position_nm 						";
			sql	+= " from TZ_MEMOBUDDY a, TZ_MEMBER	b, TZ_COMPCLASS c			";
			sql	+= "  where	a.buddy = b.userid									";
			sql	+= "  and b.comp = c.comp										";
			sql	+= "  and a.userid = " + StringManager.makeSQL(s_userid);

			if ( !v_searchtext.equals("")) {	  //	�˻�� ������
				if (v_search.equals("adtitle"))	{						   //	 �������� �˻��Ҷ�
					sql	+= " and adtitle like "	+ StringManager.makeSQL("%"	+ v_searchtext + "%");
				} else if (v_search.equals("adcontents")) {				   //	 �������� �˻��Ҷ�
					sql	+= " and adcontents	like " + StringManager.makeSQL("%" + v_searchtext +	"%");
				}
			}
			System.out.println(sql);
			sql	+= " order by a.seq	desc ";

			ls = connMgr.executeQuery(sql);

			ls.setPageSize(9);						//	�������� row ������	�����Ѵ�
			ls.setCurrentPage(v_pageno);			//	   ������������ȣ��	�����Ѵ�.
			int	totalpagecount = ls.getTotalPage();	  //	 ��ü ������ ���� ��ȯ�Ѵ�
			int	totalrowcount  = ls.getTotalCount();  //	 ��ü row ���� ��ȯ�Ѵ�

			while (ls.next()) {
				data = new MemoBuddyData();

				data.setSeq(ls.getInt("seq"));
				data.setBuddy(ls.getString("buddy"));
				data.setBuddynm(ls.getString("buddynm"));
				data.setComp(ls.getString("comp"));
				data.setCompnm(ls.getString("compnm"));
				data.setJikwi(ls.getString("jikwi"));
				data.setJikwinm(ls.getString("jikwinm"));
				data.setCono(ls.getString("cono"));
   				data.setUserid(ls.getString("userid"));
   				data.setEmail(ls.getString("email"));
				data.setPositionNm(ls.getString("position_nm"));
				data.setDispnum(totalrowcount -	ls.getRowNum() + 1);
				data.setTotalpagecount(totalpagecount);
				list.add(data);
			}
		}
		catch (Exception ex) {
			   ErrorManager.getErrorStackTrace(ex, box,	sql);
			throw new Exception("sql = " + sql + "\r\n"	+ ex.getMessage());
		}
		finally	{
			if(ls != null) { try { ls.close(); }catch (Exception e)	{} }
			if(connMgr != null)	{ try {	connMgr.freeConnection(); }catch (Exception	e10) {}	}
		}
		return list;
	}


	/**
	* ģ��ã�� ����Ʈ
	* @param box		  receive from the form	object and session
	* @return ArrayList	  ģ��ã�� ����Ʈ
	* @throws Exception
	*/
	public ArrayList searchListMemoBuddy(RequestBox box) throws Exception {
		DBConnectionManager	connMgr	= null;
		ListSet	ls = null;
		ArrayList list = null;
		String sql = "";
		MemoBuddyData data = null;

		String v_searchtext	= box.getString("p_searchtext");
		String v_search		= box.getString("p_search");
		int	   v_pageno	= box.getInt("p_pageno");
		String s_userid     = box.getSession("userid");
		String v_comp       = box.getSession("comp");
		String s_gadmin   = box.getSession("gadmin");
		String v_gadmin = "";
		
		if ( !s_gadmin.equals("") ) { 
		  v_gadmin = s_gadmin.substring(0,1);
		}
		
		try	{
			connMgr	= new DBConnectionManager();

			list = new ArrayList();

			sql	+= "\n select a.name, a.position_nm, a.userid, get_Compnm(a.comp) as compnm          ";                           
			sql	+= "\n from TZ_MEMBER a, TZ_COMPCLASS b		      ";
			sql	+= "\n where a.comp = b.comp			      ";
		    /*
		     * if ( !v_searchtext.equals("")) {	  //	�˻�� ������
				sql	+= "\n AND    a. " + v_search + " = " + StringManager.makeSQL(v_searchtext);
			}*/
			 if ( !v_searchtext.equals("") ) {                            //    �˻�� ������
                 sql += " and  a. " + v_search + " like   '%" + v_searchtext+ "%'";
             }
		    /*	 
			sql	+= "\n   and a.userid != " + StringManager.makeSQL(s_userid) + "			      ";
			sql	+= "\n minus 					      ";
			sql	+= "\n select b.name, b.position_nm, a.buddy, get_Compnm(b.comp) as compnm	      ";
			sql	+= "\n from tz_MemoBuddy a, tz_member b		      ";
			sql	+= "\n where a.buddy = b.userid			      ";
			sql	+= "\n   and a.userid = " + StringManager.makeSQL(s_userid) + " 			      ";
            */
			ls = connMgr.executeQuery(sql);
			ls.setPageSize(8);						 // �������� row ������	�����Ѵ�
			ls.setCurrentPage(v_pageno);			 // ������������ȣ�� �����Ѵ�.
			int	totalpagecount = ls.getTotalPage();	 // ��ü ������ ���� ��ȯ�Ѵ�
			int	totalrowcount  = ls.getTotalCount(); // ��ü row ���� ��ȯ�Ѵ�

			while (ls.next()) {
				data = new MemoBuddyData();

				data.setBuddy(ls.getString("userid"));
				data.setBuddynm(ls.getString("name"));
				data.setCompnm(ls.getString("compnm"));
				data.setDispnum(totalrowcount -	ls.getRowNum() + 1);
				data.setPositionNm(ls.getString("position_nm"));
				data.setTotalpagecount(totalpagecount);
				list.add(data);
			}
		}
		catch (Exception ex) {
			   ErrorManager.getErrorStackTrace(ex, box,	sql);
			throw new Exception("sql = " + sql + "\r\n"	+ ex.getMessage());
		}
		finally	{
			if(ls != null) { try { ls.close(); }catch (Exception e)	{} }
			if(connMgr != null)	{ try {	connMgr.freeConnection(); }catch (Exception	e10) {}	}
		}
		return list;
	}

	/**
	* ģ�� ����Ҷ�
	* @param box	  receive from the form	object and session
	* @return isOk	  1:insert success,0:insert	fail
	* @throws Exception
	*/
	public int insertMemoBuddy(RequestBox box) throws Exception	{
		DBConnectionManager	connMgr	= null;
		ListSet	ls1	= null;
		ListSet	ls2	= null;
		PreparedStatement pstmt	= null;
		String sql1	 = "";
		String sql2	= "";
		String sql3	= "";
		int	isOk  =	0;
		int isOk_check = 0;
		int	v_cnt =	0;
		int	v_seq =	0;

		Vector v_vecbuddy  = new Vector();
		String v_buddy	   = "";

		v_vecbuddy	= box.getVector("p_buddy");

		String s_userid	  =	box.getSession("userid");

		try	{
			connMgr	= new DBConnectionManager();
			connMgr.setAutoCommit(false);
			isOk = 1;

			sql3 =	"insert	into TZ_MemoBuddy(userid, buddy, seq)	";
			sql3 +=	"			   values (?, ?, ?)					";
			pstmt =	connMgr.prepareStatement(sql3);

			for(int	i =	0; i < v_vecbuddy.size() ; i++){
				v_buddy	= (String)v_vecbuddy.elementAt(i);

				// �ߺ�	üũ
				sql1  =	" select count(*) from TZ_MemoBuddy				   ";
				sql1 +=	"  where userid	= "	+ StringManager.makeSQL(s_userid);
				sql1 +=	"	 and buddy	= "	+ StringManager.makeSQL(v_buddy);
				ls1	= connMgr.executeQuery(sql1);
				if (ls1.next())	{
					v_cnt =	ls1.getInt(1);
				}
				ls1.close();

				//�ߺ��� �ƴҰ��
				if (v_cnt	== 0) {
					// seq value  =  max + 1
					sql2  =	"select	max(seq) from TZ_MemoBuddy	where userid = " + StringManager.makeSQL(s_userid);
					ls2	= connMgr.executeQuery(sql2);
					if (ls2.next())	{
						v_seq =	ls2.getInt(1) +	1;
					} else {
						v_seq =	1;
					}
					ls2.close();

					// ģ�����
					pstmt.setString(1,	s_userid);
					pstmt.setString(2,	v_buddy);
					pstmt.setInt(3,	 v_seq);
					isOk_check =  pstmt.executeUpdate();
					if (isOk_check == 0) isOk = 0;
				}
			}
			if ( isOk > 0) connMgr.commit();
			else connMgr.rollback();
		}
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex,	box, sql1);
			throw new Exception("sql = " + sql3	+ "\r\n" + ex.getMessage());
		}
		finally	{
			if(ls1 != null) { try { ls1.close(); } catch (Exception e) {} }
			if(ls2 != null) { try { ls2.close(); } catch (Exception e) {} }
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch (Exception e) {} 
			if(connMgr != null)	{ try {	connMgr.freeConnection(); }catch (Exception	e10) {}	}
		}
		return isOk;
	}

	/**
	* ģ�� �����Ҷ�
	* @param box      receive from the form object and session
	* @return isOk    1:delete success,0:delete fail
	* @throws Exception
	*/
	public int deleteMemoBuddy(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;
		String sql = "";
		int isOk_check = 0;
		int isOk = 0;

		Vector v_vecbuddy  =  new Vector();
		String v_buddy     = "";

		v_vecbuddy = box.getVector("p_buddy");
		String s_userid	  =	box.getSession("userid");

		try {
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			isOk = 1;

			sql  = " delete from TZ_MEMOBUDDY         ";
			sql += "  where userid = ? and buddy = ? ";
			pstmt = connMgr.prepareStatement(sql);

			for(int i = 0; i < v_vecbuddy.size() ; i++){
				v_buddy = (String)v_vecbuddy.elementAt(i);

				pstmt.setString(1, s_userid);
				pstmt.setString(2, v_buddy);

				isOk_check =  pstmt.executeUpdate();
				if (isOk_check == 0) isOk = 0;
			}
			if ( isOk > 0) connMgr.commit();
			else connMgr.rollback();
		}
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql+ "\r\n");
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch (Exception e) {}
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return isOk;
	}

}