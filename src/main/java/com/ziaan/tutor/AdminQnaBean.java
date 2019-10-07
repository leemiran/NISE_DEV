	// **********************************************************
// 	1. 제	   목: 강사 질문방
// 	2. 프로그램명: AdminQnaBean.java
// 	3. 개	   요: 강사 질문방
// 	4. 환	   경: JDK 1.4
// 	5. 버	   젼: 1.0
// 	6. 작	   성: 강성욱 2005.	9. 20
// 	7. 수	   정:
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
 * 자료실 관리(ADMIN)
 *
 * @date   : 2005. 9
 * @author : S.W.Kang
 */
public class AdminQnaBean { 
	private	static final String	FILE_TYPE =	"p_file";			// 		파일업로드되는 tag name
	private	static final int FILE_LIMIT	= 5;					// 	  페이지에 세팅된 파일첨부 갯수
	private	ConfigSet config;
	private	int	row;

	public AdminQnaBean() { 
		try { 
			config = new ConfigSet();
			row	= Integer.parseInt(config.getProperty("page.bulletin.row") );		// 		  이 모듈의	페이지당 row 수를 셋팅한다
		}
		catch(Exception	e) { 
			e.printStackTrace();
		}
	}

	/**
	* 자료실 리스트화면 select
	* @param	box			 receive from the form object and session
	* @return ArrayList	 자료실	리스트
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
			if ( !v_searchtext.equals("") ) { 				// 	  검색어가 있으면
				if ( v_search.equals("name") ) { 				// 	  이름으로 검색할때
					sql	 += " and lower(a.name)	like lower(" + StringManager.makeSQL("%" + v_searchtext +	"%") + ")";
				}
				else if (v_search.equals("title") ) { 		// 	  제목으로 검색할때
					sql	 += " and lower(a.title) like lower("	 + StringManager.makeSQL("%"	 + v_searchtext + "%") + ")";
				}
				else if (v_search.equals("content") ) { 		// 	  내용으로 검색할때
					sql += " and a.content like "	 + StringManager.makeSQL("%"	 + v_searchtext + "%");
				}
			}
			sql	 += " group by a.seq, a.userid, a.name, a.title,	a.indate, a.cnt, a.refseq, a.levels, a.position   ";
			sql	 += " order by a.refseq desc, a.position asc                                                               ";

			ls = connMgr.executeQuery(sql);

			ls.setPageSize(row);			              // 	 페이지당 row 갯수를 세팅한다
			ls.setCurrentPage(v_pageno);				  // 	   현재페이지번호를	세팅한다.
			int	totalpagecount = ls.getTotalPage();		  // 	 전체 페이지 수를 반환한다
			int	totalrowcount =	ls.getTotalCount();	      // 	  전체 row 수를	반환한다

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
   * 선택된	자료실 게시물 상세내용 select
   * @param box          receive from the form object and session
   * @return ArrayList   조회한 상세정보
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
            // -------------------   2004.12.25  변경     -------------------------------------------------------------------
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
	* 새로운 자료실 내용 등록
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

			// ----------------------   게시판 번호 가져온다	----------------------------
			sql	= "select nvl(max(seq),	0) from	TZ_BOARD where tabseq = " + v_tabseq;
			ls = connMgr.executeQuery(sql);
			ls.next();
			int	v_seq =	ls.getInt(1) + 1;
			ls.close();
			// ------------------------------------------------------------------------------------
			/*********************************************************************************************/
				// 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
/*            
				ConfigSet conf = new ConfigSet();
				SmeNamoMime namo = new SmeNamoMime(v_content); // 객체생성 
				boolean result = namo.parse(); // 실제 파싱 수행 
				if ( !result ) { // 파싱 실패시 
					System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
					return 0;
				}
				if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
					String v_server = conf.getProperty("autoever.url.value");
					String fPath = conf.getProperty("dir.namo");   // 파일 저장 경로 지정
					String refUrl = conf.getProperty("url.namo");; // 웹에서 저장된 파일을 접근하기 위한 경로
					String prefix =  "cpCompany" + v_seq;         // 파일명 접두어
					result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
				}
				if ( !result ) { // 파일저장 실패시 
					System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
					return 0;
				}
				v_content1 = namo.getContent(); // 최종 컨텐트 얻기
*/                
			/*********************************************************************************************/
			
			
			//// //// //// //// //// //// //// //// // 	 게시판	table 에 입력  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
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
			
//			connMgr.setOracleCLOB(sql2, v_content);       //      (기타 서버 경우)       
			
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
	* 선택된 자료 상세내용 수정
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
		int	v_upfilecnt	= box.getInt("p_upfilecnt");	// 	서버에 저장되있는 파일수
		String v_title = box.getString("p_title");
		String v_content = box.getString("p_content");

		Vector v_savefile =	new	Vector();
		Vector v_filesequence =	new	Vector();
System.out.println("upfilecnt ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == = > " +v_upfilecnt);
		for ( int	i =	0; i < v_upfilecnt;	i++ ) { 
			if ( 	!box.getString("p_fileseq" + i).equals(""))	{ 
				v_savefile.addElement(box.getString("p_savefile" + i));			// 		서버에 저장되있는 파일명 중에서	삭제할 파일들
				v_filesequence.addElement(box.getString("p_fileseq"	 + i));		 // 		 서버에	저장되있는 파일번호	중에서 삭제할 파일들
			}
		}

		String s_userid	= box.getSession("userid");
		String s_usernm	= box.getSession("name");
		
		/*********************************************************************************************/
        // 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
/*        
        ConfigSet conf = new ConfigSet();
        SmeNamoMime namo = new SmeNamoMime(v_content); // 객체생성 
        boolean result = namo.parse(); // 실제 파싱 수행 
        System.out.println(result);
        
        if ( !result ) { // 파싱 실패시 
            System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
            return 0;
        }
        if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
            String v_server = conf.getProperty("autoever.url.value");
            String fPath = conf.getProperty("dir.namo");   // 파일 저장 경로 지정
            String refUrl = conf.getProperty("url.namo");; // 웹에서 저장된 파일을 접근하기 위한 경로
            String prefix = "cpCompany" + v_seq;         // 파일명 접두어
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
            System.out.println(result);
        }
        if ( !result ) { // 파일저장 실패시 
            System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
            return 0;
        }
        v_content = namo.getContent(); // 최종 컨텐트 얻기
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

//	    	connMgr.setOracleCLOB(sql2, v_content);       //      (기타 서버 경우)       
			
			isOk2 =	this.insertUpFile(connMgr, v_tabseq, v_seq,	box);		// 		파일첨부했다면 파일table에	insert

			isOk3 =	this.deleteUpFile(connMgr, box,	v_filesequence);		// 	   삭제할 파일이 있다면	파일table에서 삭제
			
			if ( isOk1 > 0 &&	isOk2 > 	0 && isOk3 > 0)	{ 
				connMgr.commit();
				if ( v_savefile != null )	{ 
					FileManager.deleteFile(v_savefile);			// 	 DB	에서 모든처리가	완료되면 해당 첨부파일 삭제
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
	* 선택된 게시물	삭제
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
		int	v_upfilecnt	= box.getInt("p_upfilecnt");	// 	서버에 저장되있는 파일수		
		Vector v_savefile  = box.getVector("p_savefile");
		
		// 답변 유무 체크(답변 있을시 삭제불가)
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
						FileManager.deleteFile(v_savefile);			// 	 첨부파일 삭제
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
	* 질문 답변 등록
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
			
			// 기존 답변글 위치 한칸밑으로 변경
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
			
			// ----------------------   게시판 번호 가져온다	----------------------------
			sql1	= "select nvl(max(seq),	0) from	TZ_BOARD where tabseq = " + v_tabseq;
			ls = connMgr.executeQuery(sql1);
			ls.next();
			int	v_seq =	ls.getInt(1) + 1;
			ls.close();
			// ------------------------------------------------------------------------------------
			/*********************************************************************************************/
				// 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
/*            
				ConfigSet conf = new ConfigSet();
				SmeNamoMime namo = new SmeNamoMime(v_content); // 객체생성 
				boolean result = namo.parse(); // 실제 파싱 수행 
				if ( !result ) { // 파싱 실패시 
					System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
					return 0;
				}
				if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
					String v_server = conf.getProperty("autoever.url.value");
					String fPath = conf.getProperty("dir.namo");   // 파일 저장 경로 지정
					String refUrl = conf.getProperty("url.namo");; // 웹에서 저장된 파일을 접근하기 위한 경로
					String prefix =  "cpCompany" + v_seq;         // 파일명 접두어
					result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
				}
				if ( !result ) { // 파일저장 실패시 
					System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
					return 0;
				}
				v_content = namo.getContent(); // 최종 컨텐트 얻기
*/                
			/*********************************************************************************************/
			
			
			//// //// //// //// //// //// //// //// // 	 게시판	table 에 입력  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
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
			
//			connMgr.setOracleCLOB(sql3, v_content);       //      (기타 서버 경우)       
			
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

//// //// //// //// //// //// //// //// //// //// //// //// //// ///	 파일 테이블   //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

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
			sql	= "select nvl(max(fileseq),	0) from	TZ_BOARDFILE	where tabseq = " + p_tabseq + " and seq =	" +	p_seq;
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
	
}
