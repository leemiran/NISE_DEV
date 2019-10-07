package com.ziaan.homepage.adminBoard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class AdminBoardBean {
	private ConfigSet config;
	private int row;
	private int adminrow;
	private static final String FILE_TYPE = "p_file"; // ���Ͼ��ε�Ǵ� tag name
	private static final int FILE_LIMIT = 5; // �������� ���õ� ����÷�� ����

	public AdminBoardBean() {
		try {
			config = new ConfigSet();
			row = Integer.parseInt(config.getProperty("page.bulletin.row")); // ��
																				// �����
																				// ��������
																				// row
																				// ����
																				// �����Ѵ�
			adminrow = Integer.parseInt(config
					.getProperty("page.bulletin.adminrow")); // �� ����� �������� row
																// ���� �����Ѵ�
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��� �Խ��� ����Ʈ
	 * @param box
	 * @return
	 */
	public List AdminBoardList(RequestBox box) {
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		List adminList = null;
		String sql = "";
		DataBox dbox = null;

		int v_tabseq = box.getInt("p_tabseq");
		int v_pageno = box.getInt("p_pageno");

		String v_searchtext = box.getString("p_searchtext");
		String v_search = box.getString("p_search");

		Vector realfileVector = new Vector();
		Vector savefileVector = new Vector();
		Vector fileseqVector = new Vector();
		
        String v_order      = box.getString("p_order");
        String v_orderType     = box.getString("p_orderType");                 // ������ ����

		try {
			connMgr = new DBConnectionManager();
			adminList = new ArrayList();
			sql = " select a.seq seq, a.userid userid, a.name name, a.title title, count(b.realfile) filecnt, \n ";
			sql += "        a.indate indate, a.cnt cnt, a.refseq refseq, a.levels levels, a.position position, \n ";
			sql += "        get_codenm('0114',a.status) status, a.incharge, a.chkfinal,  \n ";
			sql += "        (select count(*) from tz_board where tabseq = "+v_tabseq+" and refseq = a.seq and seq <> a.seq) replycnt, expectday  \n ";
			sql += "   from TZ_BOARD a, TZ_BOARDFILE b   \n  ";
			sql += "  where a.tabseq = b.tabseq( +)      \n ";
			sql += "    and a.seq    = b.seq( +)      \n ";
			sql += "    and a.tabseq = " + v_tabseq + " \n ";
			sql += "    and a.levels = 1 \n ";
			sql += "    and a.position = 1 \n ";

			if (!v_searchtext.equals("")) { // �˻�� ������
				if (v_search.equals("name")) { // �̸����� �˻��Ҷ�
					sql += " and a.name like "
							+ StringManager.makeSQL("%" + v_searchtext + "%");
				} else if (v_search.equals("title")) { // �������� �˻��Ҷ�
					sql += " and a.title like "
							+ StringManager.makeSQL("%" + v_searchtext + "%");
				} else if (v_search.equals("content")) { // �������� �˻��Ҷ�
					sql += " and dbms_lob.instr(a.content, "
							+ StringManager.makeSQL(v_searchtext)
							+ ",1,1) < > 0";
				}

			}
			//sql += " group by a.seq, a.userid, a.name, a.title, a.indate, a.cnt, a.refseq, a.levels, a.position,b.realfile,b.savefile, b.fileseq, status, a.incharge, a.chkfinal  ";
			
			sql += " group by a.seq, a.userid, a.name, a.title, a.indate, a.cnt, a.refseq, a.levels, a.position, status, a.incharge, a.chkfinal, expectday  ";
			
            if ( v_order.equals("") ) { 
            	sql += " order by a.refseq desc, position asc "; 
            } else { 
                sql += " order by " + v_order + v_orderType;
            } 
            
            
			//System.out.println("��� �Խ��� sql: "+sql);
			

			ls = connMgr.executeQuery(sql);
			ls.setPageSize(row); // �������� row ������ �����Ѵ�
			ls.setCurrentPage(v_pageno); // ������������ȣ�� �����Ѵ�.
			int totalpagecount = ls.getTotalPage(); // ��ü ������ ���� ��ȯ�Ѵ�
			int totalrowcount = ls.getTotalCount(); // ��ü row ���� ��ȯ�Ѵ�

			while (ls.next()) {

				realfileVector = new Vector();
				savefileVector = new Vector();
				fileseqVector = new Vector();

				dbox = ls.getDataBox();
				dbox.put("d_dispnum", new Integer(totalrowcount
						- ls.getRowNum() + 1));
				dbox.put("d_totalpage", new Integer(totalpagecount));
				dbox.put("d_rowcount", new Integer(row));

				//realfileVector.addElement(ls.getString("realfile"));
				//savefileVector.addElement(ls.getString("savefile"));
				//fileseqVector.addElement(String.valueOf(ls.getInt("fileseq")));

				//if (realfileVector != null)
					//dbox.put("d_realfile", realfileVector);
				//if (savefileVector != null)
					//dbox.put("d_savefile", savefileVector);
				//if (fileseqVector != null)
					//dbox.put("d_fileseq", fileseqVector);

				adminList.add(dbox);
			}
		} catch (Exception e) {
			ErrorManager.getErrorStackTrace(e, box, sql);
		} finally {
			if (ls != null) {
				try {
					ls.close();
				} catch (Exception e) {
				}
			}
			if (connMgr != null) {
				try {
					connMgr.freeConnection();
				} catch (Exception e10) {
				}
			}
		}

		return adminList;
	}

	// �Խ��� data ����
	public int insertAdminBoardData(RequestBox box) throws Exception {
		 DBConnectionManager	connMgr	= null;

	        ResultSet rs1 = null;
	        Statement stmt1 = null;
	        PreparedStatement pstmt1  = null;
	        String              sql     = "";
	        String sql1 = "";
//	        String sql2 = "";
	        int isOk1 = 1;
	        int isOk2 = 1;
	        int v_seq = 0;
	        ListSet ls1         = null;

	        int    v_tabseq  = box.getInt("p_tabseq");
	        String v_title   = box.getString("p_title");
	        String v_content = box.getString("p_content"); // ���� clob

	        String s_userid = box.getSession("userid");
	        String s_usernm = box.getSession("name"); 
	        String s_gadmin = box.getSession("gadmin");
	        String v_isimport = box.getStringDefault("p_isimport", "N");
	        String v_status = box.getString("p_status");
	        String v_incharge = box.getString("p_incharge");
	        String v_chkfinal = box.getString("p_chkfinal");
	        
	        System.out.println("p_tabseq: "+v_tabseq+", p_seq: "+v_seq);
	        /*********************************************************************************************/
	        // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ���ε�)
	/*        
	        ConfigSet conf = new ConfigSet();
	        SmeNamoMime namo = new SmeNamoMime(v_content); // ��ü���� 
	        boolean result = namo.parse(); // ���� �Ľ� ���� 
	        if ( !result ) { // �Ľ� ���н� 
	             
	            return 0;
	        }
	        if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ� 
	            String v_server = conf.getProperty("autoever.url.value");
	            String fPath = conf.getProperty("dir.namo");   // ���� ���� ��� ����
	            String refUrl = conf.getProperty("url.namo");; // ������ ����� ������ �����ϱ� ���� ���
	            String prefix = "studyboard" + v_tabseq;         // ���ϸ� ���ξ�
	            result = namo.saveFile(fPath, v_server +refUrl, prefix); // ���� ���� ���� 
	        }
	        if ( !result ) { // �������� ���н� 
	             
	            return 0;
	        }
	        v_content = namo.getContent(); // ���� ����Ʈ ���
	*/        
	        /*********************************************************************************************/
	        //  
	        try { 
	            connMgr = new DBConnectionManager();
	            connMgr.setAutoCommit(false);

	            // ----------------------   �Խ��� ��ȣ �����´� ----------------------------
	            sql = " select nvl(max(seq),	0) from	TZ_BOARD where tabseq = 5";
	            ls1 = connMgr.executeQuery(sql);
	            if ( ls1.next() ) { 
	                v_seq = ls1.getInt(1) + 1;
	            }
	//  
	            // --------------------------------------------------------------------------

	            // ----------------------   �Խ��� table �� �Է�  --------------------------
	            sql1 =  " insert into TZ_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate, gadmin, isimport, status, incharge, chkfinal)  ";
	            sql1 += " values (?, ?, ?, ?, to_char(sysdate,	'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,	'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?)    ";

	            pstmt1 = connMgr.prepareStatement(sql1);
	            pstmt1.setInt(1, v_tabseq);
	            pstmt1.setInt(2, v_seq);
	            pstmt1.setString(3, s_userid);
	            pstmt1.setString(4, s_usernm);
	            pstmt1.setString(5, v_title);
	            pstmt1.setString(6, v_content);
	            pstmt1.setInt(7, 0);
	            pstmt1.setInt(8, v_seq);
	            pstmt1.setInt(9, 1);
	            pstmt1.setInt(10, 1);
	            pstmt1.setString(11, s_userid);
	            pstmt1.setString(12, s_gadmin);
	            pstmt1.setString(13, v_isimport);
	            pstmt1.setString(14, v_status);
	            pstmt1.setString(15, v_incharge);
	            pstmt1.setString(16, v_chkfinal);
	            
	            isOk1 = pstmt1.executeUpdate();
//				sql2 = "select content from TZ_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq;
//				connMgr.setOracleCLOB(sql2, v_content);       //      (��Ÿ ���� ���)       

	            // ���Ͼ��ε�
	            isOk2 = this.insertUpFile(connMgr, v_tabseq, v_seq, box);
	                    
	            if ( isOk1 > 0 && isOk2 > 0 ) { 
	                connMgr.commit();
	            } else { 
	                connMgr.rollback();
	            }
	        } catch ( Exception ex ) { 
	            connMgr.rollback();
	            ErrorManager.getErrorStackTrace(ex, box, sql1);
	            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
	        } finally { 
	            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
	            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
	            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
	            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
	            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
	        }

	        return isOk1*isOk2;
	}

	// �Խ����� ���б� �����͸� �˻��Ͽ� �Ѱ� �����´�.
	public DataBox retrieveAdminBoard(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		String sql = "";
		DataBox dbox = null;
		
		int v_tabseq = box.getInt("p_tabseq");
		int v_seq = box.getInt("p_seq");
		int	v_upfilecnt	= (box.getInt("p_upfilecnt") > 0?box.getInt("p_upfilecnt"):1);
		
		// String [] realfile = new String [v_upfilecnt];
		// String [] savefile= new String [v_upfilecnt];
		// int [] fileseq = new int [v_upfilecnt];
		Vector realfileVector = new Vector();
		Vector savefileVector = new Vector();
		Vector fileseqVector = new Vector();
		
		int	[] fileseq = new int [v_upfilecnt];
		
		try {
			connMgr = new DBConnectionManager();

			sql = " select a.seq seq, a.userid userid, a.name name, a.title title, b.fileseq fileseq, b.realfile realfile, a.content content,\n  ";
			sql += "        b.savefile savefile, a.indate indate, a.cnt cnt, a.refseq refseq, a.levels levels, a.position position,          \n    ";
			sql+=" (select count(realfile) from TZ_BOARDFILE where tabseq =" + v_tabseq+" and seq = "+ v_seq+") upfilecnt, get_codenm('0114',a.status) status, a.incharge, a.chkfinal, a.status statuscode \n ";
			sql += " from TZ_BOARD a, TZ_BOARDFILE b                                                                                            \n";
			sql += "  where a.tabseq = b.tabseq( +)                                                                                              \n";
			sql += "    and a.seq    = b.seq( +)                                                                                                 \n";
			sql += "    and a.tabseq = " + v_tabseq;
			sql += "    and a.seq    = " + v_seq;
			
			
			System.out.println("�Խù� ���� seq: "+sql);
			ls = connMgr.executeQuery(sql);

			for (int i = 0; ls.next(); i++) {

				dbox = ls.getDataBox();

				realfileVector.addElement(ls.getString("realfile"));
				savefileVector.addElement(ls.getString("savefile"));
				fileseqVector.addElement(String.valueOf(ls.getInt("fileseq")));
			}
			
			if (realfileVector != null){
				dbox.put("d_realfile", realfileVector);
				
			}
				
			if (savefileVector != null){
				dbox.put("d_savefile", savefileVector);
			}
				
			if (fileseqVector != null){
				dbox.put("d_fileseq", fileseqVector);
			}	

			connMgr.executeUpdate("update TZ_BOARD set cnt = cnt + 1 where tabseq =5 and seq = " +v_seq);
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			if (ls != null) {
				try {
					ls.close();
				} catch (Exception e) {
				}
			}
			if (connMgr != null) {
				try {
					connMgr.freeConnection();
				} catch (Exception e10) {
				}
			}
		}

		return dbox;
	}
	
	public ArrayList selectListResAdminBoard(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		String sql = "";
		DataBox dbox = null;
		ArrayList list = null;
		
		int v_tabseq = box.getInt("p_tabseq");
		int v_seq = box.getInt("p_seq");
		int	v_upfilecnt	= (box.getInt("p_upfilecnt") > 0?box.getInt("p_upfilecnt"):1);
		
		Vector realfileVector = new Vector();
		Vector savefileVector = new Vector();
		Vector fileseqVector = new Vector();
		
		int	[] fileseq = new int [v_upfilecnt];
		
		try {
			connMgr = new DBConnectionManager();
			list = new ArrayList();
			
			sql = " select a.seq seq, a.userid userid, a.name name, a.title title, b.fileseq fileseq, b.realfile realfile, a.content content,\n  ";
			sql += "        b.savefile savefile, a.indate indate, a.cnt cnt, a.refseq refseq, a.levels levels, a.position position,          \n    ";
			sql+=" (select count(realfile) from TZ_BOARDFILE where tabseq =" + v_tabseq+" and seq = "+ v_seq+") upfilecnt, get_codenm('0114',a.status) status, a.incharge, a.chkfinal, a.status statuscode \n ";
			sql += " from TZ_BOARD a, TZ_BOARDFILE b                                                                                            \n";
			sql += "  where a.tabseq = b.tabseq( +)                                                                                              \n";
			sql += "    and a.seq    = b.seq( +)                                                                                                 \n";
			sql += "    and a.tabseq = " + v_tabseq;
			sql += "    and a.refseq    = " + v_seq;
			sql += "    and a.seq    <> " + v_seq;
			sql += "    order by refseq, levels, position desc  ";
			
			
			System.out.println("�Խù� ���� seq: "+sql);
			ls = connMgr.executeQuery(sql);

			for (int i = 0; ls.next(); i++) {
				dbox = ls.getDataBox();
				realfileVector.addElement(ls.getString("realfile"));
				savefileVector.addElement(ls.getString("savefile"));
				fileseqVector.addElement(String.valueOf(ls.getInt("fileseq")));
			
				if (realfileVector != null){ dbox.put("d_realfile", realfileVector); }
				if (savefileVector != null){ dbox.put("d_savefile", savefileVector); }
				if (fileseqVector != null){ dbox.put("d_fileseq", fileseqVector); }	
				
				list.add(dbox);
			}

			//connMgr.executeUpdate("update TZ_BOARD set cnt = cnt + 1 where tabseq =5 and seq = " +v_seq);
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			if (ls != null) {
				try {
					ls.close();
				} catch (Exception e) {
				}
			}
			if (connMgr != null) {
				try {
					connMgr.freeConnection();
				} catch (Exception e10) {
				}
			}
		}

		return list;
	}	

	// ������ ����
	public int updateAdminBoardData(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		String sql1 = "", sql2 = "";
		int isOk1 = 1;
		int isOk2 = 1;
		int isOk3 = 1;

		int v_tabseq = box.getInt("p_tabseq");
		int v_seq = box.getInt("p_seq");
		int v_upfilecnt = box.getInt("p_upfilecnt"); // ������ ������ִ� ���ϼ�
		
		//System.out.println("p_tabseq: "+v_tabseq+", p_seq: "+v_seq);
		
		String v_title = box.getString("p_title");
		String v_content = box.getString("p_content");
		String v_status = box.getString("p_status");
		String v_incharge = box.getString("p_incharge");
		String v_chkfinal = box.getString("p_chkfinal");
		String v_expectday = box.getString("p_expectday")+"23";

		Vector v_savefile = new Vector();
		Vector v_filesequence = new Vector();
		System.out.println("upfilecnt ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == = > " + v_upfilecnt);
		for (int i = 0; i < v_upfilecnt; i++) {
			if (!box.getString("p_fileseq" + i).equals("")) {
				v_savefile.addElement(box.getString("p_savefile" + i)); // ������
																		// ������ִ�
																		// ���ϸ�
																		// �߿���
																		// ������
																		// ���ϵ�
				v_filesequence.addElement(box.getString("p_fileseq" + i)); // ������
																			// ������ִ�
																			// ���Ϲ�ȣ
																			// �߿���
																			// ������
																			// ���ϵ�
			}
		}

		String s_userid = box.getSession("userid");
		String s_usernm = box.getSession("name");

		try {
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			//sql1 = "update TZ_BOARD set title = ?, content = ?, userid =	?, name	= ?, luserid = ?, indate = to_char(sysdate,	'YYYYMMDDHH24MISS'), status = ?, incharge = ?, chkfinal = ? ";
			sql1 = "update TZ_BOARD set title = ?, content = empty_clob(), luserid = ?, ldate = to_char(sysdate,	'YYYYMMDDHH24MISS'), status = ?, incharge = ?, chkfinal = ?, expectday = ? ";
			// sql1 =
			// "update TZ_BOARD set title = ?, content = empty_clob(), userid =	?, name	= ?, luserid = ?, indate = to_char(sysdate,	'YYYYMMDDHH24MISS')";
			sql1 += "  where tabseq =? and seq = ?";

			pstmt1 = connMgr.prepareStatement(sql1);

			pstmt1.setString(1, v_title);
			//pstmt1.setString(2, v_content);
			pstmt1.setString(2, s_userid);
			pstmt1.setString(3, v_status);
			pstmt1.setString(4, v_incharge);
			pstmt1.setString(5, v_chkfinal);
			pstmt1.setString(6, v_expectday);
			pstmt1.setInt(7, v_tabseq);
			pstmt1.setInt(8, v_seq);

			isOk1 = pstmt1.executeUpdate();

			sql2 = "select content from tz_board where tabseq = " + v_tabseq
					+ " and seq = " + v_seq;

			connMgr.setOracleCLOB(sql2, v_content); // (��Ÿ ���� ���)
			System.out
			.println("filesequence ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == = > "
					+ v_filesequence);
			isOk3 = this.deleteUpFile(connMgr, box, v_filesequence); // ������ ������
																// �ִٸ�
																// ����table����
																// ����


			isOk2 = this.insertUpFile(connMgr, 5, v_seq, box); // ����÷���ߴٸ�
																		// ����table��
																		// insert
	
			if (isOk1 > 0 && isOk2 > 0 && isOk3 > 0) {
				connMgr.commit();
				if (v_savefile != null) {
					FileManager.deleteFile(v_savefile); // DB ���� ���ó���� �Ϸ�Ǹ� �ش�
														// ÷������ ����
				}
			} else
				connMgr.rollback();
		} catch (Exception ex) {
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
		} finally {
			if (pstmt1 != null) {
				try {
					pstmt1.close();
				} catch (Exception e) {
				}
			}
			if (connMgr != null)
				try {
					connMgr.setAutoCommit(true);
				} catch (Exception e) {
				}
			if (connMgr != null) {
				try {
					connMgr.freeConnection();
				} catch (Exception e10) {
				}
			}
		}
		return isOk1 * isOk2 * isOk3;
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
	// ������ ����
	public int deleteAdminBoard(RequestBox box) throws Exception {
	       DBConnectionManager	connMgr	= null;
//	        Connection conn = null;
	        PreparedStatement pstmt1 = null;
	        PreparedStatement pstmt2 = null;
	        String sql1 = "";
	        String sql2 = "";
	        int isOk1 = 0;
	        int isOk2 = 0;

	        int v_tabseq = box.getInt("p_tabseq");
	        int v_seq = box.getInt("p_seq");
	        int v_upfilecnt = box.getInt("p_upfilecnt");    //  ������ ������ִ� ���ϼ�
	        Vector v_savefile  = box.getVector("p_savefile");

	        // �亯 ���� üũ(�亯 ������ �����Ұ�)
	        if ( this.selectBoard(v_tabseq, v_seq) == 0 ) { 

	            try { 
	                connMgr = new DBConnectionManager();
	                connMgr.setAutoCommit(false);

	                isOk1 = 1;
	                isOk2 = 1;
	                sql1 = "delete from TZ_BOARD where tabseq = ? and seq = ? ";
	                pstmt1 = connMgr.prepareStatement(sql1);
					pstmt1.setInt(1, v_tabseq);                
	                pstmt1.setInt(2, v_seq);
	                isOk1 = pstmt1.executeUpdate();

	                if ( v_upfilecnt > 0 ) { 
	                    sql2 = "delete from TZ_BOARDFILE where tabseq = ? and seq =  ?";
	                    pstmt2 = connMgr.prepareStatement(sql2);
	                    pstmt2.setInt(1, v_tabseq);
						pstmt2.setInt(2, v_seq);                    
	                    isOk2 = pstmt2.executeUpdate();
	                }

	                if ( isOk1 > 0 && isOk2 > 0 ) { 
	                    connMgr.commit();
	                    if ( v_savefile != null ) { 
	                        FileManager.deleteFile(v_savefile);         //   ÷������ ����
	                    }
	                } else connMgr.rollback();
	            }
	            catch ( Exception ex ) { 
	                connMgr.rollback();
	                ErrorManager.getErrorStackTrace(ex, box,    sql1);
	                throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
	            }
	            finally { 
	                if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }  
	                if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
	                if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
	                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
	            }
	        }

	        return isOk1*isOk2;
	}

	// ��۾���
	public int replyAdminBoardData(RequestBox box) throws Exception {
		 DBConnectionManager	connMgr	= null;
	        ResultSet rs1 = null;
	        Statement stmt1 = null;
	        Statement stmt2 = null;
	        PreparedStatement pstmt1  = null;
	        PreparedStatement pstmt2 = null;
	        
	        String sql1 = "";
	        String sql2 = "";
	        String sql3 = "";
//	        int isOk1 = 1;
	        int isOk2 = 1;
	        int isOk3 = 1;
			int v_seq = 0;

	        int    v_tabseq   = box.getInt("p_tabseq");
	        int    v_refseq   = box.getInt("p_refseq");
	        int    v_levels   = box.getInt("p_levels");
	        int    v_position = box.getInt("p_position");
	        String v_title    = box.getString("p_title");
	        String v_content  = box.getString("p_content");

	        String s_userid = box.getSession("userid");
	        String s_usernm = box.getSession("name");

	        try { 
	            connMgr = new DBConnectionManager();
	            connMgr.setAutoCommit(false);

	            // ���� �亯�� ��ġ ��ĭ������ ����
	            sql1  = "update TZ_BOARD ";
	            sql1 += "   set position = position + 1 ";
	            sql1 += " where tabseq   = ? ";
				sql1 += "   and refseq   = ? ";
	            sql1 += "   and position > ? ";
	            
	           

	            pstmt1 = connMgr.prepareStatement(sql1);
				pstmt1.setInt(1, v_tabseq);            
	            pstmt1.setInt(2, v_refseq);
	            pstmt1.setInt(3, v_position);
	            pstmt1.executeUpdate(); //isOk1 = 
	            
	            
	            stmt1 = connMgr.createStatement();
	            // ----------------------   �Խ��� ��ȣ �����´� ----------------------------
	            sql2 = "select nvl(max(seq),	0) from	TZ_BOARD where tabseq =" +  v_tabseq;
	            rs1 = stmt1.executeQuery(sql2);
	            if ( rs1.next() ) { 
	                v_seq = rs1.getInt(1) + 1;
	            }
	            // ------------------------------------------------------------------------------------

	            //// //// //// //// //// //// //// //// /  �Խ��� table �� �Է�  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
	            sql3 =  " insert into TZ_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate)  ";
	            sql3 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'))    ";
	            
	            pstmt2 = connMgr.prepareStatement(sql3);
				pstmt2.setInt(1, v_tabseq);            
	            pstmt2.setInt(2, v_seq);
	            pstmt2.setString(3, s_userid);
	            pstmt2.setString(4, s_usernm);
	            pstmt2.setString(5, v_title);
				connMgr.setCharacterStream(pstmt2, 6, v_content); //      Oracle 9i or Weblogic 6.1 �� ���
	            pstmt2.setInt(7, 0);
	            pstmt2.setInt(8, v_refseq);
	            pstmt2.setInt(9, v_levels + 1);
	            pstmt2.setInt(10, v_position + 1);
	            pstmt2.setString(11, s_userid);
	            isOk2 = pstmt2.executeUpdate();


	            isOk3 = this.insertUpFile(connMgr, v_tabseq, v_seq, box);
//	            

	            if ( isOk2 > 0 && isOk3 > 0 ) { 
	                connMgr.commit();
	            } else { 
	                connMgr.rollback();
	            }
	        } catch ( Exception ex ) { 
	            connMgr.rollback();
	            ErrorManager.getErrorStackTrace(ex, box, sql1);
	            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
	        } finally { 
	            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
	            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
	            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
	            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
	            if ( stmt2 != null ) { try { stmt2.close(); } catch ( Exception e1 ) { } }
	            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
	            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
	        }

	        return isOk2*isOk3;
	}

	// ////// //// //// //// //// //// //// //// //// //// //// //// /// ���� ���̺�
	// //// //// //// //// //// //// //// //// //// //// //// //// //// ////
	// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

//////// //// //// //// //// //// //// //// //// //// //// //// ///	 ���� ���̺�   //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

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
			sql	= "select nvl(max(fileseq),	0) from	TZ_BOARDFILE	where tabseq = 5 and seq =	" +	p_seq;
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
					pstmt2.setInt(1, 5);
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

		int	v_tabseq = StringManager.toInt(box.getStringDefault("p_tabseq","5") );

		int	v_seq =	box.getInt("p_seq");

		try	{ 
			sql3 = "delete from TZ_BOARDFILE where tabseq = 5 and seq =? and fileseq = ?";

			pstmt3 = connMgr.prepareStatement(sql3);

			for ( int	i =	0; i < p_filesequence.size(); i++ ) { 
				int	v_fileseq =	Integer.parseInt((String)p_filesequence.elementAt(i));

				pstmt3.setInt(1, v_seq);
				pstmt3.setInt(2, v_fileseq);

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
	
	// ������ ����
	public int deleteReplyAdminBoard(RequestBox box) throws Exception {
	       DBConnectionManager	connMgr	= null;
	       PreparedStatement pstmt1 = null;
	       String sql1 = "";
	       String sql2 = "";
	       int isOk1 = 0;
	       
	       int v_tabseq = box.getInt("p_tabseq");
	       int v_seq = box.getInt("p_repseq");
	       int v_upfilecnt = box.getInt("p_upfilecnt");    //  ������ ������ִ� ���ϼ�
	       Vector v_savefile  = box.getVector("p_savefile");
	       
	       try { 
	    	   connMgr = new DBConnectionManager();
	    	   
	    	   sql1 = "delete from TZ_BOARD where tabseq = ? and seq = ? ";
	    	   pstmt1 = connMgr.prepareStatement(sql1);
	    	   pstmt1.setInt(1, 5);                
	    	   pstmt1.setInt(2, v_seq);
	    	   isOk1 = pstmt1.executeUpdate();
	    	   
	       } catch ( Exception ex ) { 
	    	   connMgr.rollback();
	    	   ErrorManager.getErrorStackTrace(ex, box,    sql1);
	    	   throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
	       }
	       finally { 
	    	   if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }  
	    	   if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
	    	   if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
	       }
	       
	       return isOk1;
	}
}
