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
	private static final String FILE_TYPE = "p_file"; // 파일업로드되는 tag name
	private static final int FILE_LIMIT = 5; // 페이지에 세팅된 파일첨부 갯수

	public AdminBoardBean() {
		try {
			config = new ConfigSet();
			row = Integer.parseInt(config.getProperty("page.bulletin.row")); // 이
																				// 모듈의
																				// 페이지당
																				// row
																				// 수를
																				// 셋팅한다
			adminrow = Integer.parseInt(config
					.getProperty("page.bulletin.adminrow")); // 이 모듈의 페이지당 row
																// 수를 셋팅한다
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 운영자 게시판 리스트
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
        String v_orderType     = box.getString("p_orderType");                 // 정렬할 순서

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

			if (!v_searchtext.equals("")) { // 검색어가 있으면
				if (v_search.equals("name")) { // 이름으로 검색할때
					sql += " and a.name like "
							+ StringManager.makeSQL("%" + v_searchtext + "%");
				} else if (v_search.equals("title")) { // 제목으로 검색할때
					sql += " and a.title like "
							+ StringManager.makeSQL("%" + v_searchtext + "%");
				} else if (v_search.equals("content")) { // 내용으로 검색할때
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
            
            
			//System.out.println("운영자 게시판 sql: "+sql);
			

			ls = connMgr.executeQuery(sql);
			ls.setPageSize(row); // 페이지당 row 갯수를 세팅한다
			ls.setCurrentPage(v_pageno); // 현재페이지번호를 세팅한다.
			int totalpagecount = ls.getTotalPage(); // 전체 페이지 수를 반환한다
			int totalrowcount = ls.getTotalCount(); // 전체 row 수를 반환한다

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

	// 게시판 data 저장
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
	        String v_content = box.getString("p_content"); // 내용 clob

	        String s_userid = box.getSession("userid");
	        String s_usernm = box.getSession("name"); 
	        String s_gadmin = box.getSession("gadmin");
	        String v_isimport = box.getStringDefault("p_isimport", "N");
	        String v_status = box.getString("p_status");
	        String v_incharge = box.getString("p_incharge");
	        String v_chkfinal = box.getString("p_chkfinal");
	        
	        System.out.println("p_tabseq: "+v_tabseq+", p_seq: "+v_seq);
	        /*********************************************************************************************/
	        // 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
	/*        
	        ConfigSet conf = new ConfigSet();
	        SmeNamoMime namo = new SmeNamoMime(v_content); // 객체생성 
	        boolean result = namo.parse(); // 실제 파싱 수행 
	        if ( !result ) { // 파싱 실패시 
	             
	            return 0;
	        }
	        if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
	            String v_server = conf.getProperty("autoever.url.value");
	            String fPath = conf.getProperty("dir.namo");   // 파일 저장 경로 지정
	            String refUrl = conf.getProperty("url.namo");; // 웹에서 저장된 파일을 접근하기 위한 경로
	            String prefix = "studyboard" + v_tabseq;         // 파일명 접두어
	            result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
	        }
	        if ( !result ) { // 파일저장 실패시 
	             
	            return 0;
	        }
	        v_content = namo.getContent(); // 최종 컨텐트 얻기
	*/        
	        /*********************************************************************************************/
	        //  
	        try { 
	            connMgr = new DBConnectionManager();
	            connMgr.setAutoCommit(false);

	            // ----------------------   게시판 번호 가져온다 ----------------------------
	            sql = " select nvl(max(seq),	0) from	TZ_BOARD where tabseq = 5";
	            ls1 = connMgr.executeQuery(sql);
	            if ( ls1.next() ) { 
	                v_seq = ls1.getInt(1) + 1;
	            }
	//  
	            // --------------------------------------------------------------------------

	            // ----------------------   게시판 table 에 입력  --------------------------
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
//				connMgr.setOracleCLOB(sql2, v_content);       //      (기타 서버 경우)       

	            // 파일업로드
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

	// 게시판의 글읽기 데이터를 검색하여 한개 가져온다.
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
			
			
			System.out.println("게시물 보기 seq: "+sql);
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
			
			
			System.out.println("게시물 보기 seq: "+sql);
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

	// 데이터 수정
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
		int v_upfilecnt = box.getInt("p_upfilecnt"); // 서버에 저장되있는 파일수
		
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
				v_savefile.addElement(box.getString("p_savefile" + i)); // 서버에
																		// 저장되있는
																		// 파일명
																		// 중에서
																		// 삭제할
																		// 파일들
				v_filesequence.addElement(box.getString("p_fileseq" + i)); // 서버에
																			// 저장되있는
																			// 파일번호
																			// 중에서
																			// 삭제할
																			// 파일들
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

			connMgr.setOracleCLOB(sql2, v_content); // (기타 서버 경우)
			System.out
			.println("filesequence ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == = > "
					+ v_filesequence);
			isOk3 = this.deleteUpFile(connMgr, box, v_filesequence); // 삭제할 파일이
																// 있다면
																// 파일table에서
																// 삭제


			isOk2 = this.insertUpFile(connMgr, 5, v_seq, box); // 파일첨부했다면
																		// 파일table에
																		// insert
	
			if (isOk1 > 0 && isOk2 > 0 && isOk3 > 0) {
				connMgr.commit();
				if (v_savefile != null) {
					FileManager.deleteFile(v_savefile); // DB 에서 모든처리가 완료되면 해당
														// 첨부파일 삭제
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
	   * 삭제시 하위 답변 유무 체크
	   * @param seq          게시판 번호
	   * @return result      0 : 답변 없음,    1 : 답변 있음
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
	// 데이터 삭제
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
	        int v_upfilecnt = box.getInt("p_upfilecnt");    //  서버에 저장되있는 파일수
	        Vector v_savefile  = box.getVector("p_savefile");

	        // 답변 유무 체크(답변 있을시 삭제불가)
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
	                        FileManager.deleteFile(v_savefile);         //   첨부파일 삭제
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

	// 답글쓰기
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

	            // 기존 답변글 위치 한칸밑으로 변경
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
	            // ----------------------   게시판 번호 가져온다 ----------------------------
	            sql2 = "select nvl(max(seq),	0) from	TZ_BOARD where tabseq =" +  v_tabseq;
	            rs1 = stmt1.executeQuery(sql2);
	            if ( rs1.next() ) { 
	                v_seq = rs1.getInt(1) + 1;
	            }
	            // ------------------------------------------------------------------------------------

	            //// //// //// //// //// //// //// //// /  게시판 table 에 입력  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
	            sql3 =  " insert into TZ_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate)  ";
	            sql3 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'))    ";
	            
	            pstmt2 = connMgr.prepareStatement(sql3);
				pstmt2.setInt(1, v_tabseq);            
	            pstmt2.setInt(2, v_seq);
	            pstmt2.setString(3, s_userid);
	            pstmt2.setString(4, s_usernm);
	            pstmt2.setString(5, v_title);
				connMgr.setCharacterStream(pstmt2, 6, v_content); //      Oracle 9i or Weblogic 6.1 인 경우
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

	// ////// //// //// //// //// //// //// //// //// //// //// //// /// 파일 테이블
	// //// //// //// //// //// //// //// //// //// //// //// //// //// ////
	// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

//////// //// //// //// //// //// //// //// //// //// //// //// ///	 파일 테이블   //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

	/**
	* 새로운 자료파일 등록
	* @param connMgr  DB Connection Manager
	* @param p_tabseq 게시판 일련번호
	* @param p_seq    게시물 일련번호
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

		// ----------------------   업로드되는 파일의 형식을	알고 코딩해야한다  --------------------------------

		String [] v_realFileName = new String [FILE_LIMIT];
		String [] v_newFileName	= new String [FILE_LIMIT];

		for ( int	i =	0; i < FILE_LIMIT; i++ )	{ 
			v_realFileName [i] = box.getRealFileName(FILE_TYPE + (i +1));
			v_newFileName [i] =	box.getNewFileName(FILE_TYPE + (i +1));
		}
		// ----------------------------------------------------------------------------------------------------------------------------

		String s_userid	= box.getSession("userid");

		try	{ 
			 // ----------------------	자료 번호 가져온다 ----------------------------
			sql	= "select nvl(max(fileseq),	0) from	TZ_BOARDFILE	where tabseq = 5 and seq =	" +	p_seq;
			ls = connMgr.executeQuery(sql);
			ls.next();
			int	v_fileseq =	ls.getInt(1) + 1;
			ls.close();
			// ------------------------------------------------------------------------------------

			//// //// //// //// //// //// //// //// // 	 파일 table	에 입력	 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
			sql2 =	"insert	into TZ_BOARDFILE(tabseq, seq, fileseq, realfile, savefile, luserid,	ldate)";
			sql2 +=	" values (?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";

			pstmt2 = connMgr.prepareStatement(sql2);

			for ( int	i =	0; i < FILE_LIMIT; i++ )	{ 
				if ( 	!v_realFileName	[i].equals(""))	{ 		// 		실제 업로드	되는 파일만	체크해서 db에 입력한다
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
			FileManager.deleteFile(v_newFileName, FILE_LIMIT);		// 	일반파일, 첨부파일 있으면 삭제..
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
	 * 선택된 자료파일 DB에서 삭제
	 * @param connMgr			DB Connection Manager
	 * @param box				receive from the form object and session
	 * @param p_filesequence    선택 파일 갯수
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
	
	// 데이터 삭제
	public int deleteReplyAdminBoard(RequestBox box) throws Exception {
	       DBConnectionManager	connMgr	= null;
	       PreparedStatement pstmt1 = null;
	       String sql1 = "";
	       String sql2 = "";
	       int isOk1 = 0;
	       
	       int v_tabseq = box.getInt("p_tabseq");
	       int v_seq = box.getInt("p_repseq");
	       int v_upfilecnt = box.getInt("p_upfilecnt");    //  서버에 저장되있는 파일수
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
