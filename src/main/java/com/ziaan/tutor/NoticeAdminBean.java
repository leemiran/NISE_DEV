// **********************************************************
// 	1. 제	   목: 강사 공지사항
// 	2. 프로그램명: NoticeAdminBean.java
// 	3. 개	   요: 강사 공지사항
// 	4. 환	   경: JDK 1.4
// 	5. 버	   젼: 1.0
// 	6. 작	   성: 강성욱 2005.	1. 2
// 	7. 수	   정:
// **********************************************************

package	com.ziaan.tutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.common.GetCodenm;
import com.ziaan.course.SubjGongData;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.FreeMailBean;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * 공지사항 관리(ADMIN)
 *
 * @date   : 2005. 1
 * @author : S.W.Kang
 */
public class NoticeAdminBean { 
	private	static final String	FILE_TYPE =	"p_file";			// 		파일업로드되는 tag name
	private	static final int FILE_LIMIT	= 5;					// 	  페이지에 세팅된 파일첨부 갯수
	private	ConfigSet config;
	private	int	row;

	public NoticeAdminBean() { 
		try { 
			config = new ConfigSet();
			row	= Integer.parseInt(config.getProperty("page.bulletin.row") );		// 		  이 모듈의	페이지당 row 수를 셋팅한다
		}
		catch(Exception	e) { 
			e.printStackTrace();
		}
	}


	/**
	* 공지사항 강사메인 리스트화면 select
	* @param	box			 receive from the form object and session
	* @return ArrayList	 공지사항	리스트
	* @throws Exception
	*/
	public ArrayList selectNoticeMain(RequestBox box) throws Exception	{ 
		DBConnectionManager	connMgr	= null;
		Connection conn	= null;
		ListSet	ls = null;
		ArrayList           list    = null;
		String              sql     = "";
		// NoticeData	data = null;
		DataBox             dbox    = null;
		int i	=	0;
		int	v_tabseq = box.getInt("p_tabseq");
		String s_userid = box.getSession("userid");
        String s_gadmin = box.getSession("gadmin");
        String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
		try	{ 
			connMgr	= new DBConnectionManager();

			list = new ArrayList();

			/*sql	= "select seq, userid, name, title,	indate, cnt	";
			sql	 += " from TZ_BOARD								";
			sql	 += " where tabseq = 56							";
			if ( box.getSession("gadmin").equals("P1") ) { 
              sql += " and (                                                                \n";
              sql += " select                                                               \n";
              sql += "   count(*) cnt                                                       \n";
              sql += " from                                                                 \n";
              sql += "   tz_classtutor x,                                                   \n";
              sql += "   tz_subjseq y                                                       \n";
              sql += " where                                                                \n";
              sql += "   tuserid = '" +box.getSession("userid") + "'                        \n";
              sql += "   and x.subj = y.subj                                                \n";
              sql += "   and x.year = y.year                                                \n";
              sql += "   and x.subjseq = y.subjseq                                          \n";
              sql += "   and (substr(y.edustart, 0 , 8) = tz_board.edustart and substr(y.eduend, 0, 8 ) = tz_board.eduend)\n";
              sql += " ) > 0                                                                \n";
            }
			sql	 += " order by seq desc                          ";*/
			
			sql = "\n select a.seq, a.userid, a.title , a.subj, a.year, a.subjseq , b.subjnm, a.ldate, "
				+ "\n        b.grcode, b.edustart, b.eduend, isclosed, get_name(a.userid) name "
				+ "\n from   tz_gong a, tz_subjseq b, tz_subjman c, tz_classtutor d "
				+ "\n where  1=1 "
				+ "\n and    a.subj = b.subj "
				+ "\n and    a.year = b.year "
				+ "\n and    a.subjseq = b.subjseq "
				+ "\n and    a.subj = c.subj "
				+ "\n and    c.userid = " + StringManager.makeSQL(s_userid)
				+ "\n and    b.subj = d.subj "
				+ "\n and    b.year = d.year "
				+ "\n and    b.subjseq = d.subjseq "
				+ "\n and    to_char(sysdate, 'yyyymmdd') between substr(b.edustart,0,8) and substr(b.eduend, 0, 8)  "
				+ "\n and    d.tuserid = " + SQLString.Format(s_userid)
				+ "\n and    c.userid = " + SQLString.Format(s_userid)
				+ "\n and    c.gadmin = " + SQLString.Format(s_gadmin);
				sql	 += " order by ldate desc";
			ls = connMgr.executeQuery(sql);

			while ( ls.next() ) { 
				if ( i<5) { 
					dbox = ls.getDataBox();
					list.add(dbox);
				}
				i++;
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


 
	/**   09/04/09 20:05 추가
	* 강사공지사항 리스트화면 select
	* @param	box			 receive from the form object and session
	* @return ArrayList	 공지사항	리스트
	* @throws Exception
	*/
	public ArrayList selectNoticeList(RequestBox box) throws Exception	{ 
		DBConnectionManager	connMgr	= null;
		Connection conn	= null;
		ListSet	ls = null;
		ArrayList           list    = null;
		String              sql     = "";
		// NoticeData	data = null;
		DataBox             dbox    = null;

		int	v_tabseq = box.getInt("p_tabseq");
		int	v_pageno = box.getInt("p_pageno");

		String v_searchtext	= box.getString("p_searchtext");
		String v_search	    = box.getString("p_search");


		try	{ 
			connMgr	= new DBConnectionManager();

			list = new ArrayList();

			sql	= "select a.seq, a.userid, a.name, a.title,	count(b.realfile) upfilecnt, a.indate, a.cnt, a.edustart, a.eduend  ";
			sql	 += " from TZ_BOARD a, TZ_BOARDFILE b                                              ";
			sql	 += " where a.tabseq = b.tabseq( +)                                                       ";
			sql += "   and a.seq    = b.seq( +)                                                          ";
			sql += "   and a.tabseq = " + v_tabseq;

			if ( !v_searchtext.equals("") ) { 				// 	  검색어가 있으면
				if ( v_search.equals("name") ) { 				// 	  이름으로 검색할때
					sql	 += " and a.name	like " + StringManager.makeSQL("%" + v_searchtext +	"%");
				}
				else if	(v_search.equals("title") ) { 		// 	  제목으로 검색할때
					sql	 += " and a.title like "	 + StringManager.makeSQL("%"	 + v_searchtext + "%");
				}
				else if ( v_search.equals("content") ) { 		// 	  내용으로 검색할때
					sql += " and a.content like "	 + StringManager.makeSQL("%"	 + v_searchtext + "%");
				}
			}
			
			/*
			if ( box.getSession("gadmin").equals("P1") ) { 
              sql += " and (                                                                    \n";
              sql += " select                                                               \n";
              sql += "   count(*) cnt                                                       \n";
              sql += " from                                                                 \n";
              sql += "   tz_classtutor x,                                                   \n";
              sql += "   tz_subjseq y                                                       \n";
              sql += " where                                                                \n";
              sql += "   tuserid = '" +box.getSession("userid") + "'                           \n";
              sql += "   and x.subj = y.subj                                                \n";
              sql += "   and x.year = y.year                                                \n";
              sql += "   and x.subjseq = y.subjseq                                          \n";
              sql += "   and (substr(y.edustart, 0 , 8) = a.edustart and substr(y.eduend, 0, 8 ) = a.eduend)\n";
              sql += " ) > 0                                                                \n";
            }
            */
			
			sql	 += " group by a.seq, a.userid, a.name, a.title,	a.indate, a.cnt, a.edustart, a.eduend ";
			sql	 += " order by a.seq desc                                                               ";
			
			
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
   * 선택된	공지사항 게시물 상세내용 select
   * @param box          receive from the form object and session
   * @return ArrayList   조회한 상세정보
   * @throws Exception
   */   
   public DataBox selectNotice(RequestBox box)	throws Exception { 
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

			sql	 = "select           ";
			sql += "  a.seq,         ";
			sql += "  a.userid,      ";
			sql += "  a.name,        ";
			sql += "  a.title,       ";
			sql += "  a.content,     ";
			sql += "  b.fileseq,     ";
			sql += "  b.realfile,    ";
			sql += "  b.savefile,    ";
			sql += "  a.indate,      ";
			sql += "  a.edustart,      ";
			sql += "  a.eduend,      ";
			sql += "  a.cnt          ";    
			sql	 += "from TZ_BOARD a, TZ_BOARDFILE b                                              ";
			sql	 += "where a.tabseq = b.tabseq( +)                                                 ";
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
	* 새로운 공지사항 내용 등록
	* @param box      receive from the form object and session
	* @return isOk    1:insert success,0:insert fail
	* @throws Exception
	*/    
	 public int insertNotice(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet	ls = null;
		PreparedStatement pstmt1 = null;
		String              sql     = "";
		String sql1	= "",sql2 = "";
		int	isOk1 =	1;
		int	isOk2 =	1;

		int v_tabseq = box.getInt("p_tabseq");
		String v_title   = box.getString("p_title");
		String v_content = box.getString("p_content");
		String v_edustart = box.getString("p_edustart");
		String v_eduend = box.getString("p_eduend");

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
		   
		   // System.out.println("result == = > " +result);
           
		   if ( !result ) { // 파싱 실패시 
		   	System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
		   	return 0;
		   }
           
		   if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
		   	String v_server = conf.getProperty("autoever.url.value");
		   	String fPath = conf.getProperty("dir.namo");   // 파일 저장 경로 지정
		   	String refUrl = conf.getProperty("url.namo");; // 웹에서 저장된 파일을 접근하기 위한 경로
		   	String prefix =  "TutorNotice" + v_seq;         // 파일명 접두어
		   	result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
		   }
		   
		   System.out.println("result == = > " +result);
           
		   if ( !result ) { // 파일저장 실패시 
		   	System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
		   	return 0;
		   }
		   
		   v_content = namo.getContent(); // 최종 컨텐트 얻기
*/           
		   /*********************************************************************************************/


			//// //// //// //// //// //// //// //// // 	 게시판	table 에 입력  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
			sql1 =	"insert	into TZ_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, luserid, ldate, edustart, eduend) "; 
			sql1 +=	" values (?, ?, ?, ?, to_char(sysdate,	'YYYYMMDDHH24MISS'), ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?) ";
//			sql1 +=	" values (?, ?, ?, ?, to_char(sysdate,	'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?) ";

			pstmt1 = connMgr.prepareStatement(sql1);

			pstmt1.setInt(1, v_tabseq);
			pstmt1.setInt(2, v_seq);
			pstmt1.setString(3,	s_userid);
			pstmt1.setString(4,	s_usernm);
			pstmt1.setString(5,	v_title);
			pstmt1.setString(6,	v_content);
			pstmt1.setInt(7, 0);
			pstmt1.setString(8,	s_userid);
			pstmt1.setString(9,	v_edustart);
			pstmt1.setString(10,	v_eduend);

			isOk1 =	pstmt1.executeUpdate();
			
			sql2 = "select content from tz_board where tabseq = " + v_tabseq + " and  seq = " + v_seq;            
			
//            connMgr.setOracleCLOB(sql2, v_content);       //      (기타 서버 경우)      
			
			isOk2 =	this.insertUpFile(connMgr, v_tabseq, v_seq,	box);

			if ( isOk1 > 0 && isOk2 > 0) {
				// 강사에게 메일발송
				//sendMail(connMgr, box);

				connMgr.commit();
			} else {
				connMgr.rollback();
			}
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
	 * 강사공지사항 등록시, 강사에게 메일발송을 보내기 위한 정보
	 * 강사권한이면서 권한기간이 아직 남아있고, 학습기간안에 포함된 과정의 강사일 경우
	 * @param box
	 * @return
	 * @throws Exception
	 */  
    public int sendMail(DBConnectionManager connMgr, RequestBox box) throws Exception { 
        ListSet             ls      = null;
        String              sql     = "";
        ConfigSet			conf	= new ConfigSet();
        int					isOk	= 0;

        try { 

    		String v_title   = box.getString("p_title");
    		String v_content = box.getString("p_content");
    		String v_edustart = box.getString("p_edustart");
    		String v_eduend = box.getString("p_eduend");
    		String v_email = "";

            sql = "\n select a.tuserid as userid, c.name, c.email "
            	+ "\n from   tz_classtutor a "
            	+ "\n      , tz_manager b "
            	+ "\n      , tz_member c "
            	+ "\n      , tz_subjseq d "
            	+ "\n where  a.tuserid = b.userid "
            	+ "\n and    b.userid = c.userid "
            	+ "\n and    a.subj = d.subj "
            	+ "\n and    a.year = d.year "
            	+ "\n and    a.subjseq = d.subjseq "
            	+ "\n and    b.gadmin like 'P%' "
            	+ "\n and    to_char(sysdate, 'yyyymmdd') between b.fmon and b.tmon "
            	+ "\n and    d.edustart <= " + StringManager.makeSQL(v_edustart) 
            	+ "\n and    d.eduend >= " + StringManager.makeSQL(v_eduend)
            	+ "\n and    d.isdeleted = 'N' ";
            
            ls = connMgr.executeQuery(sql);

            box.put("p_mail_code", "003");
            box.put("from_name", conf.getProperty("mail.admin.name"));
            box.put("from_email", conf.getProperty("mail.admin.email"));
            box.put("p_title", "[e-Eureka] " + v_title);
            box.put("p_content", v_content);
            box.put("p_map1", "강사공지사항");
            box.put("p_map2", v_edustart + "~" + v_eduend);

            while(ls.next()) {
             	v_email = ls.getString("email");
            	if (!"".equals(StringManager.trim(v_email))) {
	 	            Vector v_to = new Vector();
	 	            v_to.addElement(ls.getString("userid") + "|" + ls.getString("name") + "|" + v_email);
	 	            box.put("to", v_to);
	 	            
	 	            FreeMailBean bean = new FreeMailBean();
	 	            isOk = bean.amailSendMail(box);
            	}
            }
        
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }

	/**
	* 선택된 자료 상세내용 수정
	* @param box      receive from the form object and session
	* @return isOk    1:update success,0:update fail
	* @throws Exception
	*/  	
	 public	int	updateNotice(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		Connection conn	= null;
		PreparedStatement pstmt1 = null;
		String sql1	= "",sql2= "";
		int	isOk1 =	1;
		int	isOk2 =	1;
		int	isOk3 =	1;

		int v_tabseq = box.getInt("p_tabseq");
		int	v_seq =	box.getInt("p_seq");
		int	v_upfilecnt	= box.getInt("p_upfilecnt");	// 	서버에 저장되있는 파일수
		String v_title = box.getString("p_title");
		String v_content = box.getString("p_content");
		String v_edustart = box.getString("p_edustart");
		String v_eduend   = box.getString("p_eduend");

		Vector v_savefile =	new	Vector();
		Vector v_filesequence =	new	Vector();
		
		// System.out.println("upfilecnt == = >>  >> " +v_upfilecnt);

		for ( int	i =	0; i < v_upfilecnt;	i++ ) { 
			// System.out.println("i == = >>  >> " +i);
			// System.out.println("box.getString(p_fileseq + i) == = >>  >> " +box.getString("p_fileseq" +i));
			if ( 	!box.getString("p_fileseq" +i).equals(""))	{ 
				// System.out.println("i == = >>  >> " +i);
				v_savefile.addElement(box.getString("p_savefile" + i));			// 		서버에 저장되있는 파일명 중에서	삭제할 파일들
				v_filesequence.addElement(box.getString("p_fileseq"	 + i));		 // 		 서버에	저장되있는 파일번호	중에서 삭제할 파일들
			}
		}

		String s_userid	= box.getSession("userid");
		String s_usernm	= box.getSession("name");

		try	{ 
			connMgr	= new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			
			
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
		   	String prefix =  "TutorNotice" + v_seq;         // 파일명 접두어
		   	result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
		   }

		   if ( !result ) { // 파일저장 실패시 
		   	System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
		   	return 0;
		   }
		   v_content = namo.getContent(); // 최종 컨텐트 얻기
*/           
		   /*********************************************************************************************/

			sql1 = "update TZ_BOARD set ";
			sql1 +=	"title = ?, ";
			sql1 +=	"content = ?, ";
//			sql1 +=	"content = empty_clob(), ";
			sql1 +=	"userid =	?, ";
			sql1 +=	"name	= ?, ";
			sql1 +=	"luserid = ?, ";
			sql1 +=	"indate = to_char(sysdate,	'YYYYMMDDHH24MISS'),";
			sql1 +=	"edustart = ?, ";
			sql1 +=	"eduend = ? ";
			sql1 +=	"  where tabseq = ? and seq = ?";

			pstmt1 = connMgr.prepareStatement(sql1);

			pstmt1.setString(1,	v_title);
			pstmt1.setString(2,	v_content);
			pstmt1.setString(3,	s_userid);
			pstmt1.setString(4,	s_usernm);
			pstmt1.setString(5,	s_userid);
			pstmt1.setString(6,	v_edustart);
			pstmt1.setString(7,	v_eduend);
			pstmt1.setInt(8, v_tabseq);
			pstmt1.setInt(9, v_seq);

			isOk1 =	pstmt1.executeUpdate();

			sql2 = "select content from tz_board where tabseq = " + v_tabseq + " and seq = " + v_seq;

//	    	connMgr.setOracleCLOB(sql2, v_content);       //      (기타 서버 경우)       
			
			isOk3 =	this.deleteUpFile(connMgr, box,	v_filesequence);		// 	   삭제할 파일이 있다면	파일table에서 삭제
			
			isOk2 =	this.insertUpFile(connMgr, v_tabseq, v_seq,	box);		// 		파일첨부했다면 파일table에	insert

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
	public int deleteNotice(RequestBox	box) throws	Exception { 
		DBConnectionManager	connMgr	= null;
		Connection conn	= null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		String sql1	= "";
		String sql2	= "";
		int	isOk1 =	1;
		int	isOk2 =	1;

		int v_tabseq = box.getInt("p_tabseq");
		int	v_seq =	box.getInt("p_seq");
		int	v_upfilecnt	= box.getInt("p_upfilecnt");	// 	서버에 저장되있는 파일수		
		Vector v_savefile  = box.getVector("p_savefile");
		
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
				            System.out.println("isOk2=" +isOk2);
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
			sql	= "select nvl(max(fileseq),	0) from	TZ_BOARDFILE	where tabseq = " +p_tabseq + " and seq =	" +	p_seq;
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
			// System.out.println("deletefile_sql == >> " +sql3);

			pstmt3 = connMgr.prepareStatement(sql3);

			for ( int	i =	0; i < p_filesequence.size(); i++ ) { 
				int	v_fileseq =	Integer.parseInt((String)p_filesequence.elementAt(i));
				pstmt3.setInt(1, v_tabseq);
				pstmt3.setInt(2, v_seq);
				pstmt3.setInt(3, v_fileseq);
				// System.out.println("v_seq == " +v_seq);    
				// System.out.println("v_fileseq == " +v_fileseq);
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

}
