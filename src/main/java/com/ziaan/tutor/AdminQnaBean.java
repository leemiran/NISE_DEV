	// **********************************************************
// 	1. ��	   ��: ���� ������
// 	2. ���α׷���: AdminQnaBean.java
// 	3. ��	   ��: ���� ������
// 	4. ȯ	   ��: JDK 1.4
// 	5. ��	   ��: 1.0
// 	6. ��	   ��: ������ 2005.	9. 20
// 	7. ��	   ��:
// **********************************************************

package	com.ziaan.tutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.common.BoardBean;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * �ڷ�� ����(ADMIN)
 *
 * @date   : 2005. 9
 * @author : S.W.Kang
 */
public class AdminQnaBean { 
	private	static final String	FILE_TYPE =	"p_file";			// 		���Ͼ��ε�Ǵ� tag name
	private	static final int FILE_LIMIT	= 5;					// 	  �������� ���õ� ����÷�� ����
	private	ConfigSet config;
	private	int	row;

	public AdminQnaBean() { 
		try { 
			config = new ConfigSet();
			row	= Integer.parseInt(config.getProperty("page.bulletin.row") );		// 		  �� �����	�������� row ���� �����Ѵ�
		}
		catch(Exception	e) { 
			e.printStackTrace();
		}
	}

	/**
	* �ڷ�� ����Ʈȭ�� select
	* @param	box			 receive from the form object and session
	* @return ArrayList	 �ڷ��	����Ʈ
	* @throws Exception
	*/
	public ArrayList selectQnaList(RequestBox box) throws Exception	{ 
		DBConnectionManager	connMgr	= null;
		Connection conn	= null;
		ListSet	ls = null;
		ArrayList           list    = null;
		String              sql     = "";
		// PdsData	data = null;
		DataBox             dbox    = null;

		int	v_tabseq = box.getInt("p_tabseq");
		int	v_pageno = box.getInt("p_pageno");

		String v_searchtext	= box.getString("p_searchtext");
		String v_search	    = box.getString("p_search");
		String s_userid			= box.getSession("userid");
		String s_gadmin			= box.getSession("gadmin");


		try	{ 
			connMgr	= new DBConnectionManager();

			list = new ArrayList();

			sql	= "select a.seq, a.userid, a.name, a.title,	count(b.realfile) filecnt, a.indate, a.cnt, a.refseq, a.levels, a.position  ";
			sql	 += " from TZ_BOARD a, TZ_BOARDFILE b                                              ";
			sql	 += " where a.tabseq = b.tabseq( +)                                                       ";
			sql += "   and a.seq    = b.seq( +)                                                          ";
			sql += "   and a.tabseq = " + v_tabseq;
			if ( s_gadmin.equals("P1") ) { 
				sql += " and a.refseq in (select seq from tz_board where levels=1 and tabseq=" + v_tabseq + " and userid =" +SQLString.Format(s_userid) + ") ";
			}
			if ( !v_searchtext.equals("") ) { 				// 	  �˻�� ������
				if ( v_search.equals("name") ) { 				// 	  �̸����� �˻��Ҷ�
					sql	 += " and lower(a.name)	like lower(" + StringManager.makeSQL("%" + v_searchtext +	"%") + ")";
				}
				else if (v_search.equals("title") ) { 		// 	  �������� �˻��Ҷ�
					sql	 += " and lower(a.title) like lower("	 + StringManager.makeSQL("%"	 + v_searchtext + "%") + ")";
				}
				else if (v_search.equals("content") ) { 		// 	  �������� �˻��Ҷ�
					sql += " and a.content like "	 + StringManager.makeSQL("%"	 + v_searchtext + "%");
				}
			}
			sql	 += " group by a.seq, a.userid, a.name, a.title,	a.indate, a.cnt, a.refseq, a.levels, a.position   ";
			sql	 += " order by a.refseq desc, a.position asc                                                               ";

			ls = connMgr.executeQuery(sql);

			ls.setPageSize(row);			              // 	 �������� row ������ �����Ѵ�
			ls.setCurrentPage(v_pageno);				  // 	   ������������ȣ��	�����Ѵ�.
			int	totalpagecount = ls.getTotalPage();		  // 	 ��ü ������ ���� ��ȯ�Ѵ�
			int	totalrowcount =	ls.getTotalCount();	      // 	  ��ü row ����	��ȯ�Ѵ�

			while ( ls.next() ) { 
				dbox = ls.getDataBox();

				list.add(dbox);
			}
		}
		catch ( Exception ex ) { 
			   ErrorManager.getErrorStackTrace(ex, box,	sql);
			throw new Exception("sql = " + sql + "\r\n"	 + ex.getMessage() );
		}
		finally	{ 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e )	{ } }
			if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e10 ) { }	}
		}
		return list;
	}
	
   /**
   * ���õ�	�ڷ�� �Խù� �󼼳��� select
   * @param box          receive from the form object and session
   * @return ArrayList   ��ȸ�� ������
   * @throws Exception
   */   
   public DataBox selectQna(RequestBox box)	throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet	ls = null;
		String              sql     = "";

		DataBox             dbox    = null;

		int	v_tabseq = box.getInt("p_tabseq");
		int	v_seq =	box.getInt("p_seq");
		int	v_upfilecnt	= (box.getInt("p_upfilecnt") > 0?box.getInt("p_upfilecnt"):1);


		Vector realfileVector = new Vector();
		Vector savefileVector = new Vector();
		Vector fileseqVector  = new Vector();        


		int	[] fileseq = new int [v_upfilecnt];

		try	{ 
			connMgr	= new DBConnectionManager();

			sql	= "select a.seq, a.userid, a.name, a.title, a.content, b.fileseq, b.realfile, b.savefile, a.indate, a.cnt ";
			sql	 += " from TZ_BOARD a, TZ_BOARDFILE b                                              ";
			sql	 += " where a.tabseq = b.tabseq( +)                                                 ";
			sql += "   and a.seq    = b.seq( +)                                                    ";
			sql += "   and a.tabseq = " + v_tabseq;
			sql += "   and a.seq    = " + v_seq;			
			ls = connMgr.executeQuery(sql);

			while ( ls.next() ) { 
            // -------------------   2004.12.25  ����     -------------------------------------------------------------------
                dbox = ls.getDataBox();

   			realfileVector.addElement( ls.getString("realfile") );
				savefileVector.addElement( ls.getString("savefile") );
				fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));

			}
        if ( realfileVector 	 != null ) dbox.put("d_realfile", realfileVector);
				if ( savefileVector 	 != null ) dbox.put("d_savefile", savefileVector);
				if ( fileseqVector 	 != null ) dbox.put("d_fileseq", fileseqVector);

				connMgr.executeUpdate("update TZ_BOARD set cnt = cnt + 1 where tabseq = " + v_tabseq + " and seq = " +v_seq);
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex,	box, sql);
			throw new Exception("sql = " + sql + "\r\n"	 + ex.getMessage() );
		}
		finally	{ 
			if ( ls != null ) { try	{ ls.close(); } catch ( Exception e ) { }}
			if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e10 ) { }	}
		}
		return dbox;
	}


	/**
	* ���ο� �ڷ�� ���� ���
	* @param box      receive from the form object and session
	* @return isOk    1:insert success,0:insert fail
	* @throws Exception
	*/    
	 public int insertQna(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet	ls = null;
		PreparedStatement pstmt1 = null;
		String              sql     = "";
		String sql1	= "",sql2 = "";
		int	isOk1 =	1;
		int	isOk2 =	1;

		int v_tabseq = box.getInt("p_tabseq");
		
		String v_title    = box.getString("p_title");
		String v_content  = box.getString("p_content");
		String v_content1 = "";

		String s_userid = box.getSession("userid");
		String s_usernm = box.getSession("name");

		try	{ 
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			// ----------------------   �Խ��� ��ȣ �����´�	----------------------------
			sql	= "select nvl(max(seq),	0) from	TZ_BOARD where tabseq = " + v_tabseq;
			ls = connMgr.executeQuery(sql);
			ls.next();
			int	v_seq =	ls.getInt(1) + 1;
			ls.close();
			// ------------------------------------------------------------------------------------
			/*********************************************************************************************/
				// ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ���ε�)
/*            
				ConfigSet conf = new ConfigSet();
				SmeNamoMime namo = new SmeNamoMime(v_content); // ��ü���� 
				boolean result = namo.parse(); // ���� �Ľ� ���� 
				if ( !result ) { // �Ľ� ���н� 
					System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
					return 0;
				}
				if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ� 
					String v_server = conf.getProperty("autoever.url.value");
					String fPath = conf.getProperty("dir.namo");   // ���� ���� ��� ����
					String refUrl = conf.getProperty("url.namo");; // ������ ����� ������ �����ϱ� ���� ���
					String prefix =  "cpCompany" + v_seq;         // ���ϸ� ���ξ�
					result = namo.saveFile(fPath, v_server +refUrl, prefix); // ���� ���� ���� 
				}
				if ( !result ) { // �������� ���н� 
					System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
					return 0;
				}
				v_content1 = namo.getContent(); // ���� ����Ʈ ���
*/                
			/*********************************************************************************************/
			
			
			//// //// //// //// //// //// //// //// // 	 �Խ���	table �� �Է�  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
			sql1 =	"insert	into TZ_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate)               ";
			sql1 +=	" values (?, ?, ?, ?, to_char(sysdate,	'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
//			sql1 +=	" values (?, ?, ?, ?, to_char(sysdate,	'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";

			pstmt1 = connMgr.prepareStatement(sql1);

			pstmt1.setInt(1, v_tabseq);
			pstmt1.setInt(2, v_seq);
			pstmt1.setString(3,	s_userid);
			pstmt1.setString(4,	s_usernm);
			pstmt1.setString(5,	v_title);
			pstmt1.setString(6,	v_content);
			pstmt1.setInt(7, 0);
			pstmt1.setInt(8, v_seq);
			pstmt1.setInt(9, 1);
			pstmt1.setInt(10, 1);
			pstmt1.setString(11,	s_userid);

			isOk1 =	pstmt1.executeUpdate();
			
			sql2 = "select content from tz_board where tabseq = " + v_tabseq + " and seq = " + v_seq ;
			
//			connMgr.setOracleCLOB(sql2, v_content);       //      (��Ÿ ���� ���)       
			
			isOk2 = this.insertUpFile(connMgr,v_tabseq, v_seq, box);
			

			if ( isOk1 > 0 && isOk2 > 0) connMgr.commit();
			else 		               connMgr.rollback();
		}
		catch ( Exception ex ) { 
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex,	box, sql1);
			throw new Exception("sql = " + sql1	 + "\r\n" + ex.getMessage() );
		}
		finally	{ 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }	}
			if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
			if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e10 ) { }	}
		}
		return isOk1*isOk2;
	}


	/**
	* ���õ� �ڷ� �󼼳��� ����
	* @param box      receive from the form object and session
	* @return isOk    1:update success,0:update fail
	* @throws Exception
	*/  	
	 public	int	updateQna(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		Connection conn	= null;
		PreparedStatement pstmt1 = null;
		String sql1	= "",sql2= "";
		int	isOk1 =	1;
		int	isOk2 =	1;
		int	isOk3 =	1;

		int	v_tabseq =	box.getInt("p_tabseq");
		int	v_seq =	box.getInt("p_seq");
		int	v_upfilecnt	= box.getInt("p_upfilecnt");	// 	������ ������ִ� ���ϼ�
		String v_title = box.getString("p_title");
		String v_content = box.getString("p_content");

		Vector v_savefile =	new	Vector();
		Vector v_filesequence =	new	Vector();
System.out.println("upfilecnt ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == = > " +v_upfilecnt);
		for ( int	i =	0; i < v_upfilecnt;	i++ ) { 
			if ( 	!box.getString("p_fileseq" + i).equals(""))	{ 
				v_savefile.addElement(box.getString("p_savefile" + i));			// 		������ ������ִ� ���ϸ� �߿���	������ ���ϵ�
				v_filesequence.addElement(box.getString("p_fileseq"	 + i));		 // 		 ������	������ִ� ���Ϲ�ȣ	�߿��� ������ ���ϵ�
			}
		}

		String s_userid	= box.getSession("userid");
		String s_usernm	= box.getSession("name");
		
		/*********************************************************************************************/
        // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ���ε�)
/*        
        ConfigSet conf = new ConfigSet();
        SmeNamoMime namo = new SmeNamoMime(v_content); // ��ü���� 
        boolean result = namo.parse(); // ���� �Ľ� ���� 
        System.out.println(result);
        
        if ( !result ) { // �Ľ� ���н� 
            System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
            return 0;
        }
        if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ� 
            String v_server = conf.getProperty("autoever.url.value");
            String fPath = conf.getProperty("dir.namo");   // ���� ���� ��� ����
            String refUrl = conf.getProperty("url.namo");; // ������ ����� ������ �����ϱ� ���� ���
            String prefix = "cpCompany" + v_seq;         // ���ϸ� ���ξ�
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // ���� ���� ���� 
            System.out.println(result);
        }
        if ( !result ) { // �������� ���н� 
            System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
            return 0;
        }
        v_content = namo.getContent(); // ���� ����Ʈ ���
*/        
        /*********************************************************************************************/
		
		try	{ 
			connMgr	= new DBConnectionManager();
			connMgr.setAutoCommit(false);

			sql1 = "update TZ_BOARD set title = ?, content = ?, userid =	?, name	= ?, luserid = ?, indate = to_char(sysdate,	'YYYYMMDDHH24MISS')";
//			sql1 = "update TZ_BOARD set title = ?, content = empty_clob(), userid =	?, name	= ?, luserid = ?, indate = to_char(sysdate,	'YYYYMMDDHH24MISS')";
			sql1 +=	"  where tabseq = ? and seq = ?";

			pstmt1 = connMgr.prepareStatement(sql1);

			pstmt1.setString(1,	v_title);
			pstmt1.setString(2,	v_content);
			pstmt1.setString(3,	s_userid);
			pstmt1.setString(4,	s_usernm);
			pstmt1.setString(5,	s_userid);
			pstmt1.setInt(6, v_tabseq);
			pstmt1.setInt(7, v_seq);

			isOk1 =	pstmt1.executeUpdate();

			sql2 = "select content from tz_board where tabseq = " + v_tabseq + " and seq = " + v_seq;

//	    	connMgr.setOracleCLOB(sql2, v_content);       //      (��Ÿ ���� ���)       
			
			isOk2 =	this.insertUpFile(connMgr, v_tabseq, v_seq,	box);		// 		����÷���ߴٸ� ����table��	insert

			isOk3 =	this.deleteUpFile(connMgr, box,	v_filesequence);		// 	   ������ ������ �ִٸ�	����table���� ����
			
			if ( isOk1 > 0 &&	isOk2 > 	0 && isOk3 > 0)	{ 
				connMgr.commit();
				if ( v_savefile != null )	{ 
					FileManager.deleteFile(v_savefile);			// 	 DB	���� ���ó����	�Ϸ�Ǹ� �ش� ÷������ ����
				}
			} else connMgr.rollback();
		}
		catch(Exception	ex)	{ 
			connMgr.rollback();			
			ErrorManager.getErrorStackTrace(ex,	box, sql1);
			throw new Exception("sql = " + sql1	 + "\r\n" + ex.getMessage() );
		}
		finally	{ 
			if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { }	}
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
			if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e10 ) { }	}
		}
		return isOk1*isOk2*isOk3;
	}


	/**
	* ���õ� �Խù�	����
	* @param box      receive from the form object and session
	* @return isOk    1:delete success,0:delete fail
	* @throws Exception
	*/    	
	public int deleteQna(RequestBox	box) throws	Exception { 
		DBConnectionManager	connMgr	= null;
		Connection conn	= null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		String sql1	= "";
		String sql2	= "";
		int	isOk1 =	0;
		int	isOk2 =	0;

		int	v_tabseq =	box.getInt("p_tabseq");
		int	v_seq =	box.getInt("p_seq");
		int	v_upfilecnt	= box.getInt("p_upfilecnt");	// 	������ ������ִ� ���ϼ�		
		Vector v_savefile  = box.getVector("p_savefile");
		
		// �亯 ���� üũ(�亯 ������ �����Ұ�)
    if ( this.selectBoard(v_tabseq, v_seq) == 0 ) { 
        	
			try	{ 
				connMgr	= new DBConnectionManager();
				connMgr.setAutoCommit(false);
    	
				sql1 = "delete from	TZ_BOARD	where tabseq = ? and seq = ? ";
    	
				pstmt1 = connMgr.prepareStatement(sql1);
    	
				pstmt1.setInt(1, v_tabseq);
				pstmt1.setInt(2, v_seq);
    	
				isOk1 =	pstmt1.executeUpdate();
    	
				if ( v_upfilecnt > 0 ) { 
					sql2 = "delete from	TZ_BOARDFILE where tabseq = ? and seq =	?";
	  	
					pstmt2 = connMgr.prepareStatement(sql2);
	  	
					pstmt2.setInt(1, v_tabseq);
					pstmt2.setInt(2, v_seq);
	  	
					isOk2 =	pstmt2.executeUpdate();
    	
				} else { 
					isOk2 = 1;
				}
    	        
				if ( isOk1 > 	0 && isOk2 > 0)	{ 
					connMgr.commit();
					if ( v_savefile != null )	{ 
						FileManager.deleteFile(v_savefile);			// 	 ÷������ ����
					}
				} else connMgr.rollback();
			}
			catch ( Exception ex ) { 
				connMgr.rollback();
				ErrorManager.getErrorStackTrace(ex, box,	sql1);
				throw new Exception("sql = " + sql1	 + "\r\n" + ex.getMessage() );
			}
			finally	{ 
				if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { }	}
				if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { }	}
    	        if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
				if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e10 ) { }	}
			}            
		}
		return isOk1*isOk2;
	}
	
	/**
	* ���� �亯 ���
	* @param box      receive from the form object and session
	* @return isOk    1:reply success,0:reply fail
	* @throws Exception
	*/    
	 public int replyQna(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet	ls = null;
		Statement stmt = null;
		PreparedStatement pstmt  = null;
		PreparedStatement pstmt1 = null;
		String              sql     = "";
		String sql1	= "",sql2 = "", sql3 = "";
		int isOk  = 1;
		int	isOk1 =	1;
		int	isOk2 =	1;

		int v_tabseq = box.getInt("p_tabseq");
		String v_title    = box.getString("p_title");
		String v_content  = box.getString("p_content");
		String v_content1 = "";

		String s_userid 	= box.getSession("userid");
		String s_usernm 	= box.getSession("name");
		int    v_refseq   = box.getInt("p_refseq");
	    int    v_levels   = box.getInt("p_levels");
	    int    v_position = box.getInt("p_position");

		try	{ 
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			// ���� �亯�� ��ġ ��ĭ������ ����
		      sql  = "update TZ_BOARD ";
		      sql += "   set position = position + 1 ";
		      sql += " where tabseq   = ? ";
		      sql += "   and refseq   = ? ";
		      sql += "   and position > ? ";
		
		      pstmt = connMgr.prepareStatement(sql);
		      pstmt.setInt(1, v_tabseq);
		      pstmt.setInt(2, v_refseq);
		      pstmt.setInt(3, v_position);
		      isOk = pstmt.executeUpdate();
		
		      stmt = connMgr.createStatement();
			
			// ----------------------   �Խ��� ��ȣ �����´�	----------------------------
			sql1	= "select nvl(max(seq),	0) from	TZ_BOARD where tabseq = " + v_tabseq;
			ls = connMgr.executeQuery(sql1);
			ls.next();
			int	v_seq =	ls.getInt(1) + 1;
			ls.close();
			// ------------------------------------------------------------------------------------
			/*********************************************************************************************/
				// ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ���ε�)
/*            
				ConfigSet conf = new ConfigSet();
				SmeNamoMime namo = new SmeNamoMime(v_content); // ��ü���� 
				boolean result = namo.parse(); // ���� �Ľ� ���� 
				if ( !result ) { // �Ľ� ���н� 
					System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
					return 0;
				}
				if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ� 
					String v_server = conf.getProperty("autoever.url.value");
					String fPath = conf.getProperty("dir.namo");   // ���� ���� ��� ����
					String refUrl = conf.getProperty("url.namo");; // ������ ����� ������ �����ϱ� ���� ���
					String prefix =  "cpCompany" + v_seq;         // ���ϸ� ���ξ�
					result = namo.saveFile(fPath, v_server +refUrl, prefix); // ���� ���� ���� 
				}
				if ( !result ) { // �������� ���н� 
					System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
					return 0;
				}
				v_content = namo.getContent(); // ���� ����Ʈ ���
*/                
			/*********************************************************************************************/
			
			
			//// //// //// //// //// //// //// //// // 	 �Խ���	table �� �Է�  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
			sql2 =	"insert	into TZ_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate)               ";
			sql2 +=	" values (?, ?, ?, ?, to_char(sysdate,	'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
//			sql2 +=	" values (?, ?, ?, ?, to_char(sysdate,	'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";

			pstmt1 = connMgr.prepareStatement(sql2);

			pstmt1.setInt(1, v_tabseq);
			pstmt1.setInt(2, v_seq);
			pstmt1.setString(3,	s_userid);
			pstmt1.setString(4,	s_usernm);
			pstmt1.setString(5,	v_title);
			pstmt1.setString(6,	v_content);
			pstmt1.setInt(7, 0);
			pstmt1.setInt(8, v_refseq);
			pstmt1.setInt(9, v_levels + 1);
			pstmt1.setInt(10, v_position + 1);
			pstmt1.setString(11,	s_userid);

			isOk1 =	pstmt1.executeUpdate();
			
			sql3 = "select content from tz_board where tabseq = " + v_tabseq + " and seq = " + v_seq ;
			
//			connMgr.setOracleCLOB(sql3, v_content);       //      (��Ÿ ���� ���)       
			
			isOk2 = this.insertUpFile(connMgr,v_tabseq, v_seq, box);
			
			if ( isOk1 > 0 && isOk2 > 0) connMgr.commit();
			else 		               connMgr.rollback();
		}
		catch ( Exception ex ) { 
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex,	box, sql1);
			throw new Exception("sql = " + sql1	 + "\r\n" + ex.getMessage() );
		}
		finally	{ 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }	}
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
			if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
			if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e10 ) { }	}
		}
		return isOk1*isOk2;
	}

//// //// //// //// //// //// //// //// //// //// //// //// //// ///	 ���� ���̺�   //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

	/**
	* ���ο� �ڷ����� ���
	* @param connMgr  DB Connection Manager
	* @param p_tabseq �Խ��� �Ϸù�ȣ
	* @param p_seq    �Խù� �Ϸù�ȣ
	* @param box      receive from the form object and session
	* @return isOk    1:insert success,0:insert fail
	* @throws Exception
	*/ 	  
	 public	int	insertUpFile(DBConnectionManager connMgr, int p_tabseq, int p_seq, RequestBox	box) throws	Exception { 
		ListSet	ls = null;
		PreparedStatement pstmt2 = null;
		String              sql     = "";
		String sql2	= "";
		int	isOk2 =	1;

		// ----------------------   ���ε�Ǵ� ������ ������	�˰� �ڵ��ؾ��Ѵ�  --------------------------------

		String [] v_realFileName = new String [FILE_LIMIT];
		String [] v_newFileName	= new String [FILE_LIMIT];

		for ( int	i =	0; i < FILE_LIMIT; i++ )	{ 
			v_realFileName [i] = box.getRealFileName(FILE_TYPE + (i +1));
			v_newFileName [i] =	box.getNewFileName(FILE_TYPE + (i +1));
		}
		// ----------------------------------------------------------------------------------------------------------------------------

		String s_userid	= box.getSession("userid");

		try	{ 
			 // ----------------------	�ڷ� ��ȣ �����´� ----------------------------
			sql	= "select nvl(max(fileseq),	0) from	TZ_BOARDFILE	where tabseq = " + p_tabseq + " and seq =	" +	p_seq;
			ls = connMgr.executeQuery(sql);
			ls.next();
			int	v_fileseq =	ls.getInt(1) + 1;
			ls.close();
			// ------------------------------------------------------------------------------------

			//// //// //// //// //// //// //// //// // 	 ���� table	�� �Է�	 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
			sql2 =	"insert	into TZ_BOARDFILE(tabseq, seq, fileseq, realfile, savefile, luserid,	ldate)";
			sql2 +=	" values (?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";

			pstmt2 = connMgr.prepareStatement(sql2);

			for ( int	i =	0; i < FILE_LIMIT; i++ )	{ 
				if ( 	!v_realFileName	[i].equals(""))	{ 		// 		���� ���ε�	�Ǵ� ���ϸ�	üũ�ؼ� db�� �Է��Ѵ�
					pstmt2.setInt(1, p_tabseq);
					pstmt2.setInt(2, p_seq);
					pstmt2.setInt(3, v_fileseq);
					pstmt2.setString(4,	v_realFileName[i]);
					pstmt2.setString(5,	v_newFileName[i]);
					pstmt2.setString(6,	s_userid);

					isOk2 =	pstmt2.executeUpdate();
					v_fileseq++;
				}
			}
		}
		catch ( Exception ex ) { 
			FileManager.deleteFile(v_newFileName, FILE_LIMIT);		// 	�Ϲ�����, ÷������ ������ ����..
			ErrorManager.getErrorStackTrace(ex,	box, sql2);
			throw new Exception("sql = " + sql2	 + "\r\n" + ex.getMessage() );
		}
		finally	{ 
		    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }	}
			if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
		}
		return isOk2;
	}


	/**
	 * ���õ� �ڷ����� DB���� ����
	 * @param connMgr			DB Connection Manager
	 * @param box				receive from the form object and session
	 * @param p_filesequence    ���� ���� ����
	 * @return
	 * @throws Exception
	 */
	public int deleteUpFile(DBConnectionManager	connMgr, RequestBox box, Vector p_filesequence)	throws Exception { 
		PreparedStatement pstmt3 = null;
		String sql3	= "";
		int	isOk3 =	1;

		int	v_tabseq = box.getInt("p_tabseq");

		int	v_seq =	box.getInt("p_seq");

		try	{ 
			sql3 = "delete from TZ_BOARDFILE where tabseq = ? and seq =? and fileseq = ?";

			pstmt3 = connMgr.prepareStatement(sql3);

			for ( int	i =	0; i < p_filesequence.size(); i++ ) { 
				int	v_fileseq =	Integer.parseInt((String)p_filesequence.elementAt(i));

				pstmt3.setInt(1, v_tabseq);
				pstmt3.setInt(2, v_seq);
				pstmt3.setInt(3, v_fileseq);

				isOk3 =	pstmt3.executeUpdate();
			}
		}
		catch ( Exception ex ) { 
			   ErrorManager.getErrorStackTrace(ex, box,	sql3);
			throw new Exception("sql = " + sql3	 + "\r\n" + ex.getMessage() );
		}
		finally	{ 
			if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e ) { }	}
		}
		return isOk3;
	}
	
	/**
   * ������ ���� �亯 ���� üũ
   * @param seq          �Խ��� ��ȣ
   * @return result      0 : �亯 ����,    1 : �亯 ����
   * @throws Exception
   */
   public int selectBoard(int tabseq, int seq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        int result = 0;

        try { 
            connMgr = new DBConnectionManager();

            sql  = "  select count(*) cnt                         ";
            sql += "  from                                        ";
            sql += "    (select tabseq, refseq, levels, position  ";
            sql += "       from TZ_BOARD                          ";
            sql += "      where tabseq = " + tabseq;
            sql += "        and seq = " + seq;
            sql += "     ) a, TZ_BOARD b                          ";
            sql += " where a.tabseq = b.tabseq                    ";
            sql += "   and a.refseq = b.refseq                    ";
            sql += "   and b.levels = (a.levels +1)                ";
            sql += "   and b.position = (a.position +1)            ";
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }
	
}
