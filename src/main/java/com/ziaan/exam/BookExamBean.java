package com.ziaan.exam;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class BookExamBean {
	public final String TYPE_CODE = "0110";
	public final String LEVEL_CODE = "0111";
	private ConfigSet config;
	
    public BookExamBean() {
        try {
        	
        } catch(Exception e) { 
            e.printStackTrace();
        }
    }
    
    /**
	 * 도서목록
	 * @param box          receive from the form object and session
	 * @return ArrayList   도서목록
     **/
    public ArrayList selectBookInfoList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT BOOKCODE  \n");
            sbSQL.append("     , BOOKNAME  \n");
            sbSQL.append("FROM   TZ_BOOKINFO  \n");
            sbSQL.append("ORDER BY BOOKNAME  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }
    
    /**
	 * 도서목록 Select Box
	 * @param box          receive from the form object and session
	 * @return ArrayList   도서목록 Select Box
     **/
    public static String selectBookInfoSelect(RequestBox box, boolean isALL, String p_innerText, int p_bookcode) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	String result = "";
    	
    	try {
    		result = "<select " + p_innerText + ">";
    		if(isALL) {
    			result += "    <option value=\"ALL\">ALL</option>";
    		} else {
    			result += "    <option value=\"----\">-- 선택 --</option>";
    		}
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT BOOKCODE  \n");
            sbSQL.append("     , BOOKNAME  \n");
            sbSQL.append("FROM   TZ_BOOKINFO  \n");
            sbSQL.append("ORDER BY BOOKNAME  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    			result += "<option value=\"" + dbox.getInt("d_bookcode") + "\"";
    			if(dbox.getInt("d_bookcode") == p_bookcode) {
    				result += " selected";
    			}
    			result += ">" + dbox.getString("d_bookname") + "</option>";
    		}
    		result += "</select>";
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return result;
    }
    
    /**
	 * 도서별 평가문제 목록
	 * @param box          receive from the form object and session
	 * @return ArrayList   도서별 평가문제 목록
     **/
    public ArrayList selectBookExamList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT BOOKCODE  \n");
            sbSQL.append("     , EXAMNUM  \n");
            sbSQL.append("     , EXAMTEXT  \n");
            sbSQL.append("     , EXAMTYPE  \n");
            sbSQL.append("     , DECODE(EXAMTYPE, '1', 'OX', '2', '객관식', '3', '단답형', '4', '열거형', '5', '약술형', '6', '서술형') AS TYPENM  \n");
            sbSQL.append("     , EXAMLEVEL  \n");
            sbSQL.append("     , DECODE(EXAMLEVEL, '1', '상', '2', '중', '3', '하') AS LEVELNM  \n");
            sbSQL.append("     , LISTTYPECNT  \n");
            sbSQL.append("     ,(SELECT COUNT(SELNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAMSEL  \n");
            sbSQL.append("       WHERE  BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMNUM = A.EXAMNUM  \n");
            sbSQL.append("      ) SELCNT  \n");
            sbSQL.append("     , USE_YN  \n");
            sbSQL.append("FROM   TZ_BOOKEXAM A  \n");
            sbSQL.append("WHERE  BOOKCODE = " + SQLString.Format(box.getInt("s_bookcode")) + "  \n");
            sbSQL.append("ORDER BY EXAMNUM  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }
    
    /**
	 * 도서별 평가문제 새로운 EXAMNUM
	 * @param box receive from the form object and session
	 * @return int 도서별 평가문제 새로운 EXAMNUM
     **/
    public int selectNewExamNum(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	StringBuffer sbSQL = null;
    	int v_seq = 0;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT NVL(MAX(EXAMNUM), 0) + 1 AS NEW_SEQ  \n");
            sbSQL.append("FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("WHERE  BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			v_seq = ls.getInt("new_seq");
    		}
    		ls.close();
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return v_seq;
    }
    
    /**
	 * 도서별 평가문제 등록
	 * @param box receive from the form object and session
	 * @return int 등록결과
     **/
    public int insertBookExamInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            int v_examnum = selectNewExamNum(box);

            sbSQL = new StringBuffer();
            sbSQL.append("INSERT INTO TZ_BOOKEXAM  \n");
            sbSQL.append("      (BOOKCODE  \n");
            sbSQL.append("     , EXAMNUM  \n");
            sbSQL.append("     , EXAMTEXT  \n");
            sbSQL.append("     , EXAMCONTS  \n");
            sbSQL.append("     , EXAMTYPE  \n");
            sbSQL.append("     , EXAMLEVEL  \n");
            sbSQL.append("     , LISTTYPECNT  \n");
            sbSQL.append("     , REAL_IMAGEFILE  \n");
            sbSQL.append("     , SAVE_IMAGEFILE  \n");
            sbSQL.append("     , REAL_AUDIOFILE  \n");
            sbSQL.append("     , SAVE_AUDIOFILE  \n");
            sbSQL.append("     , REAL_MOVIEFILE  \n");
            sbSQL.append("     , SAVE_MOVIEFILE  \n");
            sbSQL.append("     , REAL_FLASHFILE  \n");
            sbSQL.append("     , SAVE_FLASHFILE  \n");
            sbSQL.append("     , EXAMEXP  \n");
            sbSQL.append("     , SIMILARCHECK_YN  \n");
            sbSQL.append("     , USE_YN  \n");
            sbSQL.append("     , LUSERID  \n");
            sbSQL.append("     , LDATE  \n");
            sbSQL.append("      )  \n");
            sbSQL.append("VALUES(?, ?, ?, ?, ?  \n");
            sbSQL.append("     , ?, ?, ?, ?, ?  \n");
            sbSQL.append("     , ?, ?, ?, ?, ?  \n");
            sbSQL.append("     , ?, ?, ?, ?  \n");
            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("      )  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setInt(index++, box.getInt("p_bookcode"));
            pstmt.setInt(index++, v_examnum);
            pstmt.setString(index++, box.getString("p_examtext"));
            pstmt.setCharacterStream(index++, new StringReader(box.getString("p_examconts")),box.getString("p_examconts").length());
            pstmt.setString(index++, box.getString("p_examtype"));
            pstmt.setString(index++, box.getString("p_examlevel"));
            pstmt.setInt(index++, box.getString("p_examtype").equals("4") ? box.getInt("p_listtypecnt") : 0);
            pstmt.setString(index++, box.getRealFileName("p_imgame"));
            pstmt.setString(index++, box.getNewFileName("p_imgame"));
            pstmt.setString(index++, box.getRealFileName("p_audio"));
            pstmt.setString(index++, box.getNewFileName("p_audio"));
            pstmt.setString(index++, box.getRealFileName("p_movie"));
            pstmt.setString(index++, box.getNewFileName("p_movie"));
            pstmt.setString(index++, box.getRealFileName("p_flash"));
            pstmt.setString(index++, box.getNewFileName("p_flash"));
            pstmt.setCharacterStream(index++, new StringReader(box.getString("p_examexp")),box.getString("p_examexp").length());
            pstmt.setString(index++, box.getString("p_similarcheck_yn"));
            pstmt.setString(index++, box.getString("p_use_yn"));
            pstmt.setString(index++, box.getSession("userid"));
            isOk = pstmt.executeUpdate();
            
            sbSQL.setLength(0);
            sbSQL.append("INSERT INTO TZ_BOOKEXAMSEL  \n");
            sbSQL.append("      (BOOKCODE  \n");
            sbSQL.append("     , EXAMNUM  \n");
            sbSQL.append("     , SELNUM  \n");
            sbSQL.append("     , SELTEXT  \n");
            sbSQL.append("     , ANSWER_YN  \n");
            sbSQL.append("     , REAL_IMAGEFILE  \n");
            sbSQL.append("     , SAVE_IMAGEFILE  \n");
            sbSQL.append("     , LUSERID  \n");
            sbSQL.append("     , LDATE  \n");
            sbSQL.append("      )  \n");
            sbSQL.append("SELECT ?, ?  \n");
            sbSQL.append("     , NVL(MAX(SELNUM), 0) + 1  \n");
            sbSQL.append("     , ?, ?, ?, ?, ?  \n");
            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("FROM   TZ_BOOKEXAMSEL  \n");
            sbSQL.append("WHERE  BOOKCODE = ?  \n");
            sbSQL.append("AND    EXAMNUM = ?  \n");
            
            for(int i = 1 ; i < 11 ; i++) {
            	if(!box.getString("p_seltext" + box.getString("p_examtype") + i).equals("")) {
	            	pstmt = connMgr.prepareStatement(sbSQL.toString());
	                index = 1;
	                pstmt.setInt(index++, box.getInt("p_bookcode"));
	                pstmt.setInt(index++, v_examnum);
	                pstmt.setString(index++, box.getString("p_seltext" + box.getString("p_examtype") + i));
	                pstmt.setString(index++, box.getString("p_isanswer" + box.getString("p_examtype") + i));
	                pstmt.setString(index++, box.getRealFileName("p_selimage" + box.getString("p_examtype") + i));
	                pstmt.setString(index++, box.getNewFileName("p_selimage" + box.getString("p_examtype") + i));
	                pstmt.setString(index++, box.getSession("userid"));
	                pstmt.setInt(index++, box.getInt("p_bookcode"));
	                pstmt.setInt(index++, v_examnum);
	                isOk += pstmt.executeUpdate();
            	}
            }
            
            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 도서별 평가문제 정보
	 * @param box receive from the form object and session
	 * @return DataBox 도서별 평가문제 정보
     **/
    public DataBox selectBookExamInfo(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		dbox = new DataBox("");
    		
            sbSQL.append("SELECT BOOKCODE  \n");
            sbSQL.append("     , EXAMNUM  \n");
            sbSQL.append("     , EXAMTEXT  \n");
            sbSQL.append("     , EXAMCONTS  \n");
            sbSQL.append("     , EXAMTYPE  \n");
            sbSQL.append("     , EXAMLEVEL  \n");
            sbSQL.append("     , LISTTYPECNT  \n");
            sbSQL.append("     , REAL_IMAGEFILE  \n");
            sbSQL.append("     , SAVE_IMAGEFILE  \n");
            sbSQL.append("     , REAL_AUDIOFILE  \n");
            sbSQL.append("     , SAVE_AUDIOFILE  \n");
            sbSQL.append("     , REAL_MOVIEFILE  \n");
            sbSQL.append("     , SAVE_MOVIEFILE  \n");
            sbSQL.append("     , REAL_FLASHFILE  \n");
            sbSQL.append("     , SAVE_FLASHFILE  \n");
            sbSQL.append("     , EXAMEXP  \n");
            sbSQL.append("     , SIMILARCHECK_YN  \n");
            sbSQL.append("     , USE_YN  \n");
            sbSQL.append("FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("WHERE  BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
            sbSQL.append("AND    EXAMNUM = " + SQLString.Format(box.getInt("p_examnum")) + "  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return dbox;
    }
    
    /**
	 * 도서별 평가문제 보기목록
	 * @param box          receive from the form object and session
	 * @return ArrayList   도서별 평가문제 보기목록
     **/
    public ArrayList selectBookExamSelList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT BOOKCODE  \n");
            sbSQL.append("     , EXAMNUM  \n");
            sbSQL.append("     , SELNUM  \n");
            sbSQL.append("     , SELTEXT  \n");
            sbSQL.append("     , ANSWER_YN  \n");
            sbSQL.append("     , REAL_IMAGEFILE  \n");
            sbSQL.append("     , SAVE_IMAGEFILE  \n");
            sbSQL.append("FROM   TZ_BOOKEXAMSEL  \n");
            sbSQL.append("WHERE  BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
            sbSQL.append("AND    EXAMNUM = " + SQLString.Format(box.getInt("p_examnum")) + "  \n");
            sbSQL.append("ORDER BY SELNUM  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }
    
    /**
	 * 도서별 평가문제 수정
	 * @param box receive from the form object and session
	 * @return int 수정결과
     **/
    public int updateBookExamInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL = new StringBuffer();
            sbSQL.append("SELECT COUNT(*) AS CNT  \n");
            sbSQL.append("FROM   TZ_BOOKEXAMEACH_RESULT A  \n");
            sbSQL.append("     , TZ_SUBJSEQ B  \n");
            sbSQL.append("WHERE  A.SUBJ = B.SUBJ  \n");
            sbSQL.append("AND    A.YEAR = B.YEAR  \n");
            sbSQL.append("AND    A.SUBJSEQ = B.SUBJSEQ  \n");
            sbSQL.append("AND    A.BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
            sbSQL.append("AND    A.EXAMNUM = " + SQLString.Format(box.getInt("p_examnum")) + "  \n");
            sbSQL.append("AND    B.ISCLOSED != 'Y'  \n");

            ls = connMgr.executeQuery(sbSQL.toString());
            if(ls.next()) {
            	if(ls.getInt("cnt") > 0) {
            		isOk = -9;
            	}
            }
            
            if(isOk != -9) {
	            boolean changeImage = false;
	            boolean changeAudio = false;
	            boolean changeMovie = false;
	            boolean changeFlash = false;
	            
	            if(!box.getRealFileName("p_imgame").equals("")) {
	            	changeImage =true;
	            } else {
	            	if(box.getString("p_image_del").equals("Y")) {
	            		changeImage = true;
	            	}
	            }
	            
	            if(!box.getRealFileName("p_audio").equals("")) {
	            	changeAudio =true;
	            } else {
	            	if(box.getString("p_audio_del").equals("Y")) {
	            		changeAudio = true;
	            	}
	            }
	            
	            if(!box.getRealFileName("p_movie").equals("")) {
	            	changeMovie =true;
	            } else {
	            	if(box.getString("p_movie_del").equals("Y")) {
	            		changeMovie = true;
	            	}
	            }
	            
	            if(!box.getRealFileName("p_flash").equals("")) {
	            	changeFlash =true;
	            } else {
	            	if(box.getString("p_flash_del").equals("Y")) {
	            		changeFlash = true;
	            	}
	            }
            
	            sbSQL.setLength(0);
	            sbSQL.append("UPDATE TZ_BOOKEXAM  \n");
	            sbSQL.append("SET    EXAMTEXT = ?  \n");
	            sbSQL.append("     , EXAMCONTS = ?  \n");
	            sbSQL.append("     , EXAMLEVEL = ?  \n");
	            sbSQL.append("     , LISTTYPECNT = ?  \n");
	            if(changeImage) {
		            sbSQL.append("     , REAL_IMAGEFILE = ?  \n");
		            sbSQL.append("     , SAVE_IMAGEFILE = ?  \n");
	            }
	            if(changeAudio) {
		            sbSQL.append("     , REAL_AUDIOFILE = ?  \n");
		            sbSQL.append("     , SAVE_AUDIOFILE = ?  \n");
	            }
	            if(changeMovie) {
		            sbSQL.append("     , REAL_MOVIEFILE = ?  \n");
		            sbSQL.append("     , SAVE_MOVIEFILE = ?  \n");
	            }
	            if(changeFlash) {
		            sbSQL.append("     , REAL_FLASHFILE = ?  \n");
		            sbSQL.append("     , SAVE_FLASHFILE = ?  \n");
	            }
	            sbSQL.append("     , EXAMEXP = ?  \n");
	            sbSQL.append("     , SIMILARCHECK_YN = ?  \n");
	            sbSQL.append("     , USE_YN = ?  \n");
	            sbSQL.append("     , LUSERID = ?  \n");
	            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
	            sbSQL.append("WHERE  BOOKCODE = ?  \n");
	            sbSQL.append("AND    EXAMNUM = ?  \n");
	
	            pstmt = connMgr.prepareStatement(sbSQL.toString());
	            int index = 1;
	            pstmt.setString(index++, box.getString("p_examtext"));
	            pstmt.setCharacterStream(index++, new StringReader(box.getString("p_examconts")),box.getString("p_examconts").length());
	            pstmt.setString(index++, box.getString("p_examlevel"));
	            pstmt.setInt(index++, box.getString("p_examtype").equals("4") ? box.getInt("p_listtypecnt") : 0);
	            if(changeImage) {
		            pstmt.setString(index++, box.getRealFileName("p_imgame"));
		            pstmt.setString(index++, box.getNewFileName("p_imgame"));
	            }
	            if(changeAudio) {
		            pstmt.setString(index++, box.getRealFileName("p_audio"));
		            pstmt.setString(index++, box.getNewFileName("p_audio"));
	            }
	            if(changeMovie) {
		            pstmt.setString(index++, box.getRealFileName("p_movie"));
		            pstmt.setString(index++, box.getNewFileName("p_movie"));
	            }
	            if(changeFlash) {
		            pstmt.setString(index++, box.getRealFileName("p_flash"));
		            pstmt.setString(index++, box.getNewFileName("p_flash"));
	            }
	            pstmt.setCharacterStream(index++, new StringReader(box.getString("p_examexp")),box.getString("p_examexp").length());
	            pstmt.setString(index++, box.getString("p_similarcheck_yn"));
	            pstmt.setString(index++, box.getString("p_use_yn"));
	            pstmt.setString(index++, box.getSession("userid"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setInt(index++, box.getInt("p_examnum"));
	            isOk = pstmt.executeUpdate();
	            
	            sbSQL.setLength(0);
	            sbSQL.append("DELETE FROM TZ_BOOKEXAMSEL  \n");
	            sbSQL.append("WHERE  BOOKCODE = ?  \n");
	            sbSQL.append("AND    EXAMNUM = ?  \n");
	            pstmt = connMgr.prepareStatement(sbSQL.toString());
	            
	            index = 1;
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setInt(index++, box.getInt("p_examnum"));
	            isOk += pstmt.executeUpdate();
	            
	            sbSQL.setLength(0);
	            sbSQL.append("INSERT INTO TZ_BOOKEXAMSEL  \n");
	            sbSQL.append("      (BOOKCODE  \n");
	            sbSQL.append("     , EXAMNUM  \n");
	            sbSQL.append("     , SELNUM  \n");
	            sbSQL.append("     , SELTEXT  \n");
	            sbSQL.append("     , ANSWER_YN  \n");
	            sbSQL.append("     , REAL_IMAGEFILE  \n");
	            sbSQL.append("     , SAVE_IMAGEFILE  \n");
	            sbSQL.append("     , LUSERID  \n");
	            sbSQL.append("     , LDATE  \n");
	            sbSQL.append("      )  \n");
	            sbSQL.append("SELECT ?, ?  \n");
	            sbSQL.append("     , NVL(MAX(SELNUM), 0) + 1  \n");
	            sbSQL.append("     , ?, ?, ?, ?, ?  \n");
	            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
	            sbSQL.append("FROM   TZ_BOOKEXAMSEL  \n");
	            sbSQL.append("WHERE  BOOKCODE = ?  \n");
	            sbSQL.append("AND    EXAMNUM = ?  \n");
	            
	            for(int i = 1 ; i < 11 ; i++) {
	            	if(!box.getString("p_seltext" + box.getString("p_examtype") + i).equals("")) {
	            		boolean changeFile = false;
	            		if(!box.getRealFileName("p_selimage" + box.getString("p_examtype") + i).equals("")) {
	            			changeFile = true;
	            		} else {
	            			if(box.getString("p_selimage" + box.getString("p_examtype") + i + "_del").equals("Y")) {
	            				changeFile = true;
	            			}
	            		}
		            	pstmt = connMgr.prepareStatement(sbSQL.toString());
		                index = 1;
		                pstmt.setInt(index++, box.getInt("p_bookcode"));
		                pstmt.setInt(index++, box.getInt("p_examnum"));
		                pstmt.setString(index++, box.getString("p_seltext" + box.getString("p_examtype") + i));
		                pstmt.setString(index++, box.getString("p_isanswer" + box.getString("p_examtype") + i));
		                if(changeFile) {
			                pstmt.setString(index++, box.getRealFileName("p_selimage" + box.getString("p_examtype") + i));
			                pstmt.setString(index++, box.getNewFileName("p_selimage" + box.getString("p_examtype") + i));
		                } else {
		                	pstmt.setString(index++, box.getString("p_real_imagefile" + box.getString("p_examtype") + i + "_org"));
		                	pstmt.setString(index++, box.getString("p_save_imagefile" + box.getString("p_examtype") + i + "_org"));
		                }
		                pstmt.setString(index++, box.getSession("userid"));
		                pstmt.setInt(index++, box.getInt("p_bookcode"));
		                pstmt.setInt(index++, box.getInt("p_examnum"));
		                isOk += pstmt.executeUpdate();
	            	}
	            }
            }
            
            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 도서별 평가문제 삭제
	 * @param box receive from the form object and session
	 * @return int 결과
     **/
    public int deleteBookExamInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL = new StringBuffer();
            sbSQL.append("SELECT COUNT(*) AS CNT  \n");
            sbSQL.append("FROM   TZ_BOOKEXAMEACH_RESULT A  \n");
            sbSQL.append("     , TZ_SUBJSEQ B  \n");
            sbSQL.append("WHERE  A.SUBJ = B.SUBJ  \n");
            sbSQL.append("AND    A.YEAR = B.YEAR  \n");
            sbSQL.append("AND    A.SUBJSEQ = B.SUBJSEQ  \n");
            sbSQL.append("AND    A.BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
            sbSQL.append("AND    A.EXAMNUM = " + SQLString.Format(box.getInt("p_examnum")) + "  \n");
            sbSQL.append("AND    B.ISCLOSED != 'Y'  \n");

            ls = connMgr.executeQuery(sbSQL.toString());
            if(ls.next()) {
            	if(ls.getInt("cnt") > 0) {
            		isOk = -9;
            	}
            }
            
            if(isOk != -9) {
            	sbSQL.setLength(0);
	            sbSQL.append("DELETE FROM TZ_BOOKEXAM  \n");
	            sbSQL.append("WHERE  BOOKCODE = ?  \n");
	            sbSQL.append("AND    EXAMNUM = ?  \n");
	
	            pstmt = connMgr.prepareStatement(sbSQL.toString());
	            int index = 1;
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setInt(index++, box.getInt("p_examnum"));
	            isOk = pstmt.executeUpdate();
	            
	            sbSQL.setLength(0);
	            sbSQL.append("DELETE FROM TZ_BOOKEXAMSEL  \n");
	            sbSQL.append("WHERE  BOOKCODE = ?  \n");
	            sbSQL.append("AND    EXAMNUM = ?  \n");
	            pstmt = connMgr.prepareStatement(sbSQL.toString());
	            
	            index = 1;
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setInt(index++, box.getInt("p_examnum"));
	            isOk += pstmt.executeUpdate();
            }

            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 도서별 평가 마스터 목록
	 * @param box          receive from the form object and session
	 * @return ArrayList   도서별 평가 마스터 목록
     **/
    public ArrayList selectBookExamMasterList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT A.BOOKCODE  \n");
            sbSQL.append("     , A.BOOKNAME  \n");
            sbSQL.append("     , B.BOOKCODE AS MASTERCODE  \n");
            sbSQL.append("     , TO_CHAR((B.OXTYPE_CNT + CHOICE_CNT + WORDS_CNT + LIST_CNT + SHORT_CNT + DESCRIPT_CNT)) TOTALCNT  \n");
            sbSQL.append("     , TO_CHAR(TOTALSCORE) AS TOTALSCORE  \n");
            sbSQL.append("FROM   TZ_BOOKINFO A  \n");
            sbSQL.append("     , TZ_BOOKEXAM_MASTER B  \n");
            sbSQL.append("WHERE  A.BOOKCODE = B.BOOKCODE (+)  \n");
            sbSQL.append("AND    A.BOOKNAME LIKE '%' || " + StringManager.makeSQL(box.getString("s_searhText")) + " || '%'  \n");
            sbSQL.append("ORDER BY BOOKNAME  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }
    
    /**
	 * 도서별 평가 유형별 문제수
	 * @param box receive from the form object and session
	 * @return DataBox 도서별 평가문제 정보
     **/
    public DataBox selectBookExamTypeInfo(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		dbox = new DataBox("");
    		
            sbSQL.append("SELECT BOOKCODE  \n");
            sbSQL.append("     , BOOKNAME  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '1'  \n");
            sbSQL.append("      ) AS OXCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '2'  \n");
            sbSQL.append("      ) AS CHOICECNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '3'  \n");
            sbSQL.append("      ) AS WORDSCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '4'  \n");
            sbSQL.append("      ) AS LISTCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '5'  \n");
            sbSQL.append("      ) AS SHORTCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '6'  \n");
            sbSQL.append("      ) AS DESCRIPTCNT  \n");
            sbSQL.append("FROM   TZ_BOOKINFO A  \n");
            sbSQL.append("WHERE  BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return dbox;
    }
    
    /**
	 * 도서별 평가마스터 등록
	 * @param box receive from the form object and session
	 * @return int 결과
     **/
    public int insertBookExamMasterInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("INSERT INTO TZ_BOOKEXAM_MASTER  \n");
            sbSQL.append("      (BOOKCODE  \n");
            sbSQL.append("     , TOTALSCORE  \n");
            sbSQL.append("     , EXTRA_DAYS  \n");
            sbSQL.append("     , EXTRA_MINUS  \n");
            sbSQL.append("     , OXTYPE_CNT  \n");
            sbSQL.append("     , OXTYPE_SCORE  \n");
            sbSQL.append("     , CHOICE_CNT  \n");
            sbSQL.append("     , CHOICE_SCORE  \n");
            sbSQL.append("     , WORDS_CNT  \n");
            sbSQL.append("     , WORDS_SCORE  \n");
            sbSQL.append("     , LIST_CNT  \n");
            sbSQL.append("     , LIST_SCORE  \n");
            sbSQL.append("     , SHORT_CNT  \n");
            sbSQL.append("     , SHORT_SCORE  \n");
            sbSQL.append("     , DESCRIPT_CNT  \n");
            sbSQL.append("     , DESCRIPT_SCORE  \n");
            sbSQL.append("     , LUSERID  \n");
            sbSQL.append("     , LDATE  \n");
            sbSQL.append("      )  \n");
            sbSQL.append("VALUES(?, ?, ?, ?, ?  \n");
            sbSQL.append("     , ?, ?, ?, ?, ?  \n");
            sbSQL.append("     , ?, ?, ?, ?, ?  \n");
            sbSQL.append("     , ?, ?  \n");
            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("      )  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setInt(index++, box.getInt("p_bookcode"));
            pstmt.setInt(index++, box.getInt("p_totalscore"));
            pstmt.setInt(index++, box.getInt("p_extra_days"));
            pstmt.setInt(index++, box.getInt("p_extra_minus"));
            pstmt.setInt(index++, box.getInt("p_oxtype_cnt"));
            pstmt.setInt(index++, box.getInt("p_oxtype_score"));
            pstmt.setInt(index++, box.getInt("p_choice_cnt"));
            pstmt.setInt(index++, box.getInt("p_choice_score"));
            pstmt.setInt(index++, box.getInt("p_words_cnt"));
            pstmt.setInt(index++, box.getInt("p_words_score"));
            pstmt.setInt(index++, box.getInt("p_list_cnt"));
            pstmt.setInt(index++, box.getInt("p_list_score"));
            pstmt.setInt(index++, box.getInt("p_short_cnt"));
            pstmt.setInt(index++, box.getInt("p_short_score"));
            pstmt.setInt(index++, box.getInt("p_descript_cnt"));
            pstmt.setInt(index++, box.getInt("p_descript_score"));
            pstmt.setString(index++, box.getSession("userid"));
            isOk = pstmt.executeUpdate();

            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 도서별 평가 마스터 정보
	 * @param box receive from the form object and session
	 * @return DataBox 도서별 평가 마스터 정보
     **/
    public DataBox selectBookExamMasterInfo(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		dbox = new DataBox("");
    		
            sbSQL.append("SELECT A.BOOKCODE  \n");
            sbSQL.append("     , A.BOOKNAME  \n");
            sbSQL.append("     , B.TOTALSCORE  \n");
            sbSQL.append("     , B.EXTRA_DAYS  \n");
            sbSQL.append("     , B.EXTRA_MINUS  \n");
            sbSQL.append("     , B.OXTYPE_CNT  \n");
            sbSQL.append("     , B.OXTYPE_SCORE  \n");
            sbSQL.append("     , B.CHOICE_CNT  \n");
            sbSQL.append("     , B.CHOICE_SCORE  \n");
            sbSQL.append("     , B.WORDS_CNT  \n");
            sbSQL.append("     , B.WORDS_SCORE  \n");
            sbSQL.append("     , B.LIST_CNT  \n");
            sbSQL.append("     , B.LIST_SCORE  \n");
            sbSQL.append("     , B.SHORT_CNT  \n");
            sbSQL.append("     , B.SHORT_SCORE  \n");
            sbSQL.append("     , B.DESCRIPT_CNT  \n");
            sbSQL.append("     , B.DESCRIPT_SCORE  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '1'  \n");
            sbSQL.append("      ) AS OXCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '2'  \n");
            sbSQL.append("      ) AS CHOICECNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '3'  \n");
            sbSQL.append("      ) AS WORDSCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '4'  \n");
            sbSQL.append("      ) AS LISTCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '5'  \n");
            sbSQL.append("      ) AS SHORTCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '6'  \n");
            sbSQL.append("      ) AS DESCRIPTCNT  \n");
            sbSQL.append("FROM   TZ_BOOKINFO A  \n");
            sbSQL.append("     , TZ_BOOKEXAM_MASTER B  \n");
            sbSQL.append("WHERE  A.BOOKCODE = B.BOOKCODE  \n");
            sbSQL.append("AND    A.BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return dbox;
    }
    
    /**
	 * 도서별 평가마스터 수정
	 * @param box receive from the form object and session
	 * @return int 결과
     **/
    public int updateBookExamMasterInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_BOOKEXAM_MASTER  \n");
            sbSQL.append("SET    TOTALSCORE = ?  \n");
            sbSQL.append("     , EXTRA_DAYS = ?  \n");
            sbSQL.append("     , EXTRA_MINUS = ?  \n");
            sbSQL.append("     , OXTYPE_CNT = ?  \n");
            sbSQL.append("     , OXTYPE_SCORE = ?  \n");
            sbSQL.append("     , CHOICE_CNT = ?  \n");
            sbSQL.append("     , CHOICE_SCORE = ?  \n");
            sbSQL.append("     , WORDS_CNT = ?  \n");
            sbSQL.append("     , WORDS_SCORE = ?  \n");
            sbSQL.append("     , LIST_CNT = ?  \n");
            sbSQL.append("     , LIST_SCORE = ?  \n");
            sbSQL.append("     , SHORT_CNT = ?  \n");
            sbSQL.append("     , SHORT_SCORE = ?  \n");
            sbSQL.append("     , DESCRIPT_CNT = ?  \n");
            sbSQL.append("     , DESCRIPT_SCORE = ?  \n");
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("WHERE  BOOKCODE = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setInt(index++, box.getInt("p_totalscore"));
            pstmt.setInt(index++, box.getInt("p_extra_days"));
            pstmt.setInt(index++, box.getInt("p_extra_minus"));
            pstmt.setInt(index++, box.getInt("p_oxtype_cnt"));
            pstmt.setInt(index++, box.getInt("p_oxtype_score"));
            pstmt.setInt(index++, box.getInt("p_choice_cnt"));
            pstmt.setInt(index++, box.getInt("p_choice_score"));
            pstmt.setInt(index++, box.getInt("p_words_cnt"));
            pstmt.setInt(index++, box.getInt("p_words_score"));
            pstmt.setInt(index++, box.getInt("p_list_cnt"));
            pstmt.setInt(index++, box.getInt("p_list_score"));
            pstmt.setInt(index++, box.getInt("p_short_cnt"));
            pstmt.setInt(index++, box.getInt("p_short_score"));
            pstmt.setInt(index++, box.getInt("p_descript_cnt"));
            pstmt.setInt(index++, box.getInt("p_descript_score"));
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setInt(index++, box.getInt("p_bookcode"));
            isOk = pstmt.executeUpdate();

            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 도서별 평가마스터 삭제
	 * @param box receive from the form object and session
	 * @return int 결과
     **/
    public int deleteBookExamMasterInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("DELETE FROM TZ_BOOKEXAM_MASTER  \n");
            sbSQL.append("WHERE  BOOKCODE = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setInt(index++, box.getInt("p_bookcode"));
            isOk = pstmt.executeUpdate();

            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 도서별 평가 마스터 목록
	 * @param box          receive from the form object and session
	 * @return ArrayList   도서별 평가 마스터 목록
     **/
    public ArrayList selectBookExamPaperList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT Z.SUBJ  \n");
            sbSQL.append("     , Z.YEAR  \n");
            sbSQL.append("     , Z.SUBJSEQ  \n");
            sbSQL.append("     , Z.SUBJNM  \n");
            sbSQL.append("     , Z.BOOKCODE  \n");
            sbSQL.append("     , Z.MONTH  \n");
            sbSQL.append("     , Z.BOOKNAME  \n");
            sbSQL.append("     , Y.BOOKCODE AS PAPER  \n");
            sbSQL.append("     , Y.EXAMSTART  \n");
            sbSQL.append("     , Y.EXAMEND  \n");
            sbSQL.append("     , Y.TOTALSCORE  \n");
            sbSQL.append("     , Y.OXTYPE_CNT  \n");
            sbSQL.append("     , Y.OXTYPE_SCORE  \n");
            sbSQL.append("     , Y.CHOICE_CNT  \n");
            sbSQL.append("     , Y.CHOICE_SCORE  \n");
            sbSQL.append("     , Y.WORDS_CNT  \n");
            sbSQL.append("     , Y.WORDS_SCORE  \n");
            sbSQL.append("     , Y.LIST_CNT  \n");
            sbSQL.append("     , Y.LIST_SCORE  \n");
            sbSQL.append("     , Y.SHORT_CNT  \n");
            sbSQL.append("     , Y.SHORT_SCORE  \n");
            sbSQL.append("     , Y.DESCRIPT_CNT  \n");
            sbSQL.append("     , Y.DESCRIPT_SCORE  \n");
            sbSQL.append("     , Y.OXTYPE_CNT + Y.CHOICE_CNT + Y.WORDS_CNT + Y.LIST_CNT + Y.SHORT_CNT + Y.DESCRIPT_CNT AS EXAMCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_SUBJBOOK  \n");
            sbSQL.append("       WHERE  SUBJ = Z.SUBJ) AS ROWSPAN  \n");
            sbSQL.append("FROM  (SELECT A.SUBJ  \n");
            sbSQL.append("            , A.YEAR  \n");
            sbSQL.append("            , A.SUBJSEQ  \n");
            sbSQL.append("            , A.SUBJNM  \n");
            sbSQL.append("            , B.BOOKCODE  \n");
            sbSQL.append("            , B.MONTH  \n");
            sbSQL.append("            , C.BOOKNAME  \n");
            sbSQL.append("       FROM   TZ_SUBJSEQ A  \n");
            sbSQL.append("            , TZ_SUBJBOOK B  \n");
            sbSQL.append("            , TZ_BOOKINFO C  \n");
            sbSQL.append("       WHERE  A.SUBJ = B.SUBJ  \n");
            sbSQL.append("       AND    B.BOOKCODE = C.BOOKCODE  \n");
            sbSQL.append("      ) Z  \n");
            sbSQL.append("     , TZ_BOOKEXAM_PAPER Y  \n");
            sbSQL.append("WHERE  Z.SUBJ = Y.SUBJ (+)  \n");
            sbSQL.append("AND    Z.YEAR = Y.YEAR (+)  \n");
            sbSQL.append("AND    Z.SUBJSEQ = Y.SUBJSEQ (+)  \n");
            sbSQL.append("AND    Z.BOOKCODE = Y.BOOKCODE (+)  \n");
            sbSQL.append("AND    Z.MONTH = Y.MONTH (+)  \n");
            if(!box.getString("s_subjcourse").equals("") && !box.getString("s_subjcourse").equals("ALL")) {
            	sbSQL.append("AND    Z.SUBJ = " + SQLString.Format(box.getString("s_subjcourse")) + "  \n");
            }
            if(!box.getString("s_gyear").equals("") && !box.getString("s_gyear").equals("ALL")) {
            	sbSQL.append("AND    Z.YEAR = " + SQLString.Format(box.getString("s_gyear")) + "  \n");
            }
            if(!box.getString("s_subjseq").equals("") && !box.getString("s_subjseq").equals("ALL")) {
            	sbSQL.append("AND    Z.SUBJSEQ = " + SQLString.Format(box.getString("s_subjseq")) + "  \n");
            }
            sbSQL.append("ORDER BY  \n");
            sbSQL.append("       Z.SUBJ  \n");
            sbSQL.append("     , Z.YEAR  \n");
            sbSQL.append("     , Z.SUBJSEQ  \n");
            sbSQL.append("     , Z.MONTH  \n");
            sbSQL.append("     , Z.BOOKNAME  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }
    
    /**
	 * 도서별 평가지 정보
	 * @param box receive from the form object and session
	 * @return DataBox 도서별 평가 마스터 정보
     **/
    public DataBox selectBookExamPaperInfo(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		dbox = new DataBox("");
    		
            sbSQL.append("SELECT Z.SUBJ  \n");
            sbSQL.append("     , Z.YEAR  \n");
            sbSQL.append("     , Z.SUBJSEQ  \n");
            sbSQL.append("     , Z.MONTH  \n");
            sbSQL.append("     , Z.BOOKCODE  \n");
            sbSQL.append("     , Z.MASTER  \n");
            sbSQL.append("     , Y.BOOKCODE AS PAPER  \n");
            sbSQL.append("     , Y.EXAMSTART  \n");
            sbSQL.append("     , Y.EXAMEND  \n");
            sbSQL.append("     , NVL(Y.TOTALSCORE, Z.TOTALSCORE) AS TOTALSCORE  \n");
            sbSQL.append("     , NVL(Y.EXTRA_DAYS, Z.EXTRA_DAYS) AS EXTRA_DAYS  \n");
            sbSQL.append("     , NVL(Y.EXTRA_MINUS, Z.EXTRA_MINUS) AS EXTRA_MINUS  \n");
            sbSQL.append("     , NVL(Y.OXTYPE_CNT, Z.OXTYPE_CNT) AS OXTYPE_CNT  \n");
            sbSQL.append("     , NVL(Y.OXTYPE_SCORE, Z.OXTYPE_SCORE) AS OXTYPE_SCORE  \n");
            sbSQL.append("     , NVL(Y.CHOICE_CNT, Z.CHOICE_CNT) AS CHOICE_CNT  \n");
            sbSQL.append("     , NVL(Y.CHOICE_SCORE, Z.CHOICE_SCORE) AS CHOICE_SCORE  \n");
            sbSQL.append("     , NVL(Y.WORDS_CNT, Z.WORDS_CNT) AS WORDS_CNT  \n");
            sbSQL.append("     , NVL(Y.WORDS_SCORE, Z.WORDS_SCORE) AS WORDS_SCORE  \n");
            sbSQL.append("     , NVL(Y.LIST_CNT, Z.LIST_CNT) AS LIST_CNT  \n");
            sbSQL.append("     , NVL(Y.LIST_SCORE, Z.LIST_SCORE) AS LIST_SCORE  \n");
            sbSQL.append("     , NVL(Y.SHORT_CNT, Z.SHORT_CNT) AS SHORT_CNT  \n");
            sbSQL.append("     , NVL(Y.SHORT_SCORE, Z.SHORT_SCORE) AS SHORT_SCORE  \n");
            sbSQL.append("     , NVL(Y.DESCRIPT_CNT, Z.DESCRIPT_CNT) AS DESCRIPT_CNT  \n");
            sbSQL.append("     , NVL(Y.DESCRIPT_SCORE, Z.DESCRIPT_SCORE) AS DESCRIPT_SCORE  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = Z.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '1'  \n");
            sbSQL.append("      ) AS OXCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = Z.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '2'  \n");
            sbSQL.append("      ) AS CHOICECNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = Z.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '3'  \n");
            sbSQL.append("      ) AS WORDSCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = Z.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '4'  \n");
            sbSQL.append("      ) AS LISTCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = Z.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '5'  \n");
            sbSQL.append("      ) AS SHORTCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(EXAMNUM)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM  \n");
            sbSQL.append("       WHERE  BOOKCODE = Z.BOOKCODE  \n");
            sbSQL.append("       AND    EXAMTYPE = '6'  \n");
            sbSQL.append("      ) AS DESCRIPTCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(USERID)  \n");
            sbSQL.append("       FROM   TZ_BOOKEXAM_RESULT  \n");
            sbSQL.append("       WHERE  SUBJ = Z.SUBJ  \n");
            sbSQL.append("       AND    YEAR = Z.YEAR  \n");
            sbSQL.append("       AND    SUBJSEQ = Z.SUBJSEQ  \n");
            sbSQL.append("       AND    MONTH = Z.MONTH  \n");
            sbSQL.append("       AND    BOOKCODE = Z.BOOKCODE) AS STUCNT  \n");
            sbSQL.append("FROM  (SELECT A.SUBJ  \n");
            sbSQL.append("            , A.YEAR  \n");
            sbSQL.append("            , A.SUBJSEQ  \n");
            sbSQL.append("            , B.MONTH  \n");
            sbSQL.append("            , B.BOOKCODE  \n");
            sbSQL.append("            , C.BOOKCODE AS MASTER  \n");
            sbSQL.append("            , C.TOTALSCORE  \n");
            sbSQL.append("            , C.EXTRA_DAYS  \n");
            sbSQL.append("            , C.EXTRA_MINUS  \n");
            sbSQL.append("            , C.OXTYPE_CNT  \n");
            sbSQL.append("            , C.OXTYPE_SCORE  \n");
            sbSQL.append("            , C.CHOICE_CNT  \n");
            sbSQL.append("            , C.CHOICE_SCORE  \n");
            sbSQL.append("            , C.WORDS_CNT  \n");
            sbSQL.append("            , C.WORDS_SCORE  \n");
            sbSQL.append("            , C.LIST_CNT  \n");
            sbSQL.append("            , C.LIST_SCORE  \n");
            sbSQL.append("            , C.SHORT_CNT  \n");
            sbSQL.append("            , C.SHORT_SCORE  \n");
            sbSQL.append("            , C.DESCRIPT_CNT  \n");
            sbSQL.append("            , C.DESCRIPT_SCORE  \n");
            sbSQL.append("       FROM   TZ_SUBJSEQ A  \n");
            sbSQL.append("            , TZ_SUBJBOOK B  \n");
            sbSQL.append("            , TZ_BOOKEXAM_MASTER C  \n");
            sbSQL.append("       WHERE  A.SUBJ = B.SUBJ  \n");
            sbSQL.append("       AND    B.BOOKCODE = C.BOOKCODE (+)  \n");
            sbSQL.append("      ) Z  \n");
            sbSQL.append("     , TZ_BOOKEXAM_PAPER Y  \n");
            sbSQL.append("WHERE  Z.SUBJ = Y.SUBJ (+)  \n");
            sbSQL.append("AND    Z.YEAR = Y.YEAR (+)  \n");
            sbSQL.append("AND    Z.SUBJSEQ = Y.SUBJSEQ(+)  \n");
            sbSQL.append("AND    Z.MONTH = Y.MONTH (+)  \n");
            sbSQL.append("AND    Z.BOOKCODE = Y.BOOKCODE (+)  \n");
            sbSQL.append("AND    Z.SUBJ = " + SQLString.Format(box.getString("p_subj")) + "  \n");
            sbSQL.append("AND    Z.YEAR = " + SQLString.Format(box.getString("p_year")) + "  \n");
            sbSQL.append("AND    Z.SUBJSEQ = " + SQLString.Format(box.getString("p_subjseq")) + "  \n");
            sbSQL.append("AND    Z.MONTH = " + SQLString.Format(box.getInt("p_month")) + "  \n");
            sbSQL.append("AND    Z.BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return dbox;
    }
    
    /**
	 * 도서별 평가마스터 등록
	 * @param box receive from the form object and session
	 * @return int 결과
     **/
    public int insertBookExamPaperInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("INSERT INTO TZ_BOOKEXAM_PAPER  \n");
            sbSQL.append("      (BOOKCODE  \n");
            sbSQL.append("     , MONTH  \n");
            sbSQL.append("     , SUBJ  \n");
            sbSQL.append("     , YEAR  \n");
            sbSQL.append("     , SUBJSEQ  \n");
            sbSQL.append("     , TOTALSCORE  \n");
            sbSQL.append("     , EXTRA_DAYS  \n");
            sbSQL.append("     , EXTRA_MINUS  \n");
            sbSQL.append("     , OXTYPE_CNT  \n");
            sbSQL.append("     , OXTYPE_SCORE  \n");
            sbSQL.append("     , CHOICE_CNT  \n");
            sbSQL.append("     , CHOICE_SCORE  \n");
            sbSQL.append("     , WORDS_CNT  \n");
            sbSQL.append("     , WORDS_SCORE  \n");
            sbSQL.append("     , LIST_CNT  \n");
            sbSQL.append("     , LIST_SCORE  \n");
            sbSQL.append("     , SHORT_CNT  \n");
            sbSQL.append("     , SHORT_SCORE  \n");
            sbSQL.append("     , DESCRIPT_CNT  \n");
            sbSQL.append("     , DESCRIPT_SCORE  \n");
            sbSQL.append("     , EXAMSTART  \n");
            sbSQL.append("     , EXAMEND  \n");
            sbSQL.append("     , LUSERID  \n");
            sbSQL.append("     , LDATE  \n");
            sbSQL.append("      )  \n");
            sbSQL.append("VALUES(?, ?, ?, ?, ?  \n");
            sbSQL.append("     , ?, ?, ?, ?, ?  \n");
            sbSQL.append("     , ?, ?, ?, ?, ?  \n");
            sbSQL.append("     , ?, ?, ?, ?, ?  \n");
            sbSQL.append("     , ?, ?, ?  \n");
            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("      )  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setInt(index++, box.getInt("p_bookcode"));
            pstmt.setInt(index++, box.getInt("p_month"));
            pstmt.setString(index++, box.getString("p_subj"));
            pstmt.setString(index++, box.getString("p_year"));
            pstmt.setString(index++, box.getString("p_subjseq"));
            pstmt.setInt(index++, box.getInt("p_totalscore"));
            pstmt.setInt(index++, box.getInt("p_extra_days"));
            pstmt.setInt(index++, box.getInt("p_extra_minus"));
            pstmt.setInt(index++, box.getInt("p_oxtype_cnt"));
            pstmt.setInt(index++, box.getInt("p_oxtype_score"));
            pstmt.setInt(index++, box.getInt("p_choice_cnt"));
            pstmt.setInt(index++, box.getInt("p_choice_score"));
            pstmt.setInt(index++, box.getInt("p_words_cnt"));
            pstmt.setInt(index++, box.getInt("p_words_score"));
            pstmt.setInt(index++, box.getInt("p_list_cnt"));
            pstmt.setInt(index++, box.getInt("p_list_score"));
            pstmt.setInt(index++, box.getInt("p_short_cnt"));
            pstmt.setInt(index++, box.getInt("p_short_score"));
            pstmt.setInt(index++, box.getInt("p_descript_cnt"));
            pstmt.setInt(index++, box.getInt("p_descript_score"));
            pstmt.setInt(index++, box.getInt("p_examstart"));
            pstmt.setInt(index++, box.getInt("p_examend"));
            pstmt.setString(index++, box.getSession("userid"));
            isOk = pstmt.executeUpdate();
            
            if(box.getString("p_master").equals("0")) {
            	sbSQL.setLength(0);
                sbSQL.append("INSERT INTO TZ_BOOKEXAM_MASTER  \n");
                sbSQL.append("      (BOOKCODE  \n");
                sbSQL.append("     , TOTALSCORE  \n");
                sbSQL.append("     , EXTRA_DAYS  \n");
                sbSQL.append("     , EXTRA_MINUS  \n");
                sbSQL.append("     , OXTYPE_CNT  \n");
                sbSQL.append("     , OXTYPE_SCORE  \n");
                sbSQL.append("     , CHOICE_CNT  \n");
                sbSQL.append("     , CHOICE_SCORE  \n");
                sbSQL.append("     , WORDS_CNT  \n");
                sbSQL.append("     , WORDS_SCORE  \n");
                sbSQL.append("     , LIST_CNT  \n");
                sbSQL.append("     , LIST_SCORE  \n");
                sbSQL.append("     , SHORT_CNT  \n");
                sbSQL.append("     , SHORT_SCORE  \n");
                sbSQL.append("     , DESCRIPT_CNT  \n");
                sbSQL.append("     , DESCRIPT_SCORE  \n");
                sbSQL.append("     , LUSERID  \n");
                sbSQL.append("     , LDATE  \n");
                sbSQL.append("      )  \n");
                sbSQL.append("VALUES(?, ?, ?, ?, ?  \n");
                sbSQL.append("     , ?, ?, ?, ?, ?  \n");
                sbSQL.append("     , ?, ?, ?, ?, ?  \n");
                sbSQL.append("     , ?, ?  \n");
                sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
                sbSQL.append("      )  \n");

                pstmt = connMgr.prepareStatement(sbSQL.toString());
                index = 1;
                pstmt.setInt(index++, box.getInt("p_bookcode"));
                pstmt.setInt(index++, box.getInt("p_totalscore"));
                pstmt.setInt(index++, box.getInt("p_extra_days"));
                pstmt.setInt(index++, box.getInt("p_extra_minus"));
                pstmt.setInt(index++, box.getInt("p_oxtype_cnt"));
                pstmt.setInt(index++, box.getInt("p_oxtype_score"));
                pstmt.setInt(index++, box.getInt("p_choice_cnt"));
                pstmt.setInt(index++, box.getInt("p_choice_score"));
                pstmt.setInt(index++, box.getInt("p_words_cnt"));
                pstmt.setInt(index++, box.getInt("p_words_score"));
                pstmt.setInt(index++, box.getInt("p_list_cnt"));
                pstmt.setInt(index++, box.getInt("p_list_score"));
                pstmt.setInt(index++, box.getInt("p_short_cnt"));
                pstmt.setInt(index++, box.getInt("p_short_score"));
                pstmt.setInt(index++, box.getInt("p_descript_cnt"));
                pstmt.setInt(index++, box.getInt("p_descript_score"));
                pstmt.setString(index++, box.getSession("userid"));
                isOk = pstmt.executeUpdate();            	
            }

            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 도서별 평가마스터 수정
	 * @param box receive from the form object and session
	 * @return int 결과
     **/
    public int updateBookExamPaperInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_BOOKEXAM_PAPER  \n");
            sbSQL.append("SET    TOTALSCORE = ?  \n");
            sbSQL.append("     , EXTRA_DAYS = ?  \n");
            sbSQL.append("     , EXTRA_MINUS = ?  \n");
            sbSQL.append("     , OXTYPE_CNT = ?  \n");
            sbSQL.append("     , OXTYPE_SCORE = ?  \n");
            sbSQL.append("     , CHOICE_CNT = ?  \n");
            sbSQL.append("     , CHOICE_SCORE = ?  \n");
            sbSQL.append("     , WORDS_CNT = ?  \n");
            sbSQL.append("     , WORDS_SCORE = ?  \n");
            sbSQL.append("     , LIST_CNT = ?  \n");
            sbSQL.append("     , LIST_SCORE = ?  \n");
            sbSQL.append("     , SHORT_CNT = ?  \n");
            sbSQL.append("     , SHORT_SCORE = ?  \n");
            sbSQL.append("     , DESCRIPT_CNT = ?  \n");
            sbSQL.append("     , DESCRIPT_SCORE = ?  \n");
            sbSQL.append("     , EXAMSTART = ?  \n");
            sbSQL.append("     , EXAMEND = ?  \n");
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("WHERE  BOOKCODE = ?  \n");
            sbSQL.append("AND    MONTH = ?  \n");
            sbSQL.append("AND    SUBJ = ?  \n");
            sbSQL.append("AND    YEAR = ?  \n");
            sbSQL.append("AND    SUBJSEQ = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setInt(index++, box.getInt("p_totalscore"));
            pstmt.setInt(index++, box.getInt("p_extra_days"));
            pstmt.setInt(index++, box.getInt("p_extra_minus"));
            pstmt.setInt(index++, box.getInt("p_oxtype_cnt"));
            pstmt.setInt(index++, box.getInt("p_oxtype_score"));
            pstmt.setInt(index++, box.getInt("p_choice_cnt"));
            pstmt.setInt(index++, box.getInt("p_choice_score"));
            pstmt.setInt(index++, box.getInt("p_words_cnt"));
            pstmt.setInt(index++, box.getInt("p_words_score"));
            pstmt.setInt(index++, box.getInt("p_list_cnt"));
            pstmt.setInt(index++, box.getInt("p_list_score"));
            pstmt.setInt(index++, box.getInt("p_short_cnt"));
            pstmt.setInt(index++, box.getInt("p_short_score"));
            pstmt.setInt(index++, box.getInt("p_descript_cnt"));
            pstmt.setInt(index++, box.getInt("p_descript_score"));
            pstmt.setString(index++, box.getString("p_examstart"));
            pstmt.setString(index++, box.getString("p_examend"));
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setInt(index++, box.getInt("p_bookcode"));
            pstmt.setInt(index++, box.getInt("p_month"));
            pstmt.setString(index++, box.getString("p_subj"));
            pstmt.setString(index++, box.getString("p_year"));
            pstmt.setString(index++, box.getString("p_subjseq"));
            isOk = pstmt.executeUpdate();
            
            if(box.getString("p_isMaster").equals("Y")) {
	            sbSQL.setLength(0);
	            sbSQL.append("UPDATE TZ_BOOKEXAM_MASTER  \n");
	            sbSQL.append("SET    TOTALSCORE = ?  \n");
	            sbSQL.append("     , EXTRA_DAYS = ?  \n");
	            sbSQL.append("     , EXTRA_MINUS = ?  \n");
	            sbSQL.append("     , OXTYPE_CNT = ?  \n");
	            sbSQL.append("     , OXTYPE_SCORE = ?  \n");
	            sbSQL.append("     , CHOICE_CNT = ?  \n");
	            sbSQL.append("     , CHOICE_SCORE = ?  \n");
	            sbSQL.append("     , WORDS_CNT = ?  \n");
	            sbSQL.append("     , WORDS_SCORE = ?  \n");
	            sbSQL.append("     , LIST_CNT = ?  \n");
	            sbSQL.append("     , LIST_SCORE = ?  \n");
	            sbSQL.append("     , SHORT_CNT = ?  \n");
	            sbSQL.append("     , SHORT_SCORE = ?  \n");
	            sbSQL.append("     , DESCRIPT_CNT = ?  \n");
	            sbSQL.append("     , DESCRIPT_SCORE = ?  \n");
	            sbSQL.append("     , LUSERID = ?  \n");
	            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
	            sbSQL.append("WHERE  BOOKCODE = ?  \n");
	
	            pstmt = connMgr.prepareStatement(sbSQL.toString());
	            index = 1;
	            pstmt.setInt(index++, box.getInt("p_totalscore"));
	            pstmt.setInt(index++, box.getInt("p_extra_days"));
	            pstmt.setInt(index++, box.getInt("p_extra_minus"));
	            pstmt.setInt(index++, box.getInt("p_oxtype_cnt"));
	            pstmt.setInt(index++, box.getInt("p_oxtype_score"));
	            pstmt.setInt(index++, box.getInt("p_choice_cnt"));
	            pstmt.setInt(index++, box.getInt("p_choice_score"));
	            pstmt.setInt(index++, box.getInt("p_words_cnt"));
	            pstmt.setInt(index++, box.getInt("p_words_score"));
	            pstmt.setInt(index++, box.getInt("p_list_cnt"));
	            pstmt.setInt(index++, box.getInt("p_list_score"));
	            pstmt.setInt(index++, box.getInt("p_short_cnt"));
	            pstmt.setInt(index++, box.getInt("p_short_score"));
	            pstmt.setInt(index++, box.getInt("p_descript_cnt"));
	            pstmt.setInt(index++, box.getInt("p_descript_score"));
	            pstmt.setString(index++, box.getSession("userid"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            isOk = pstmt.executeUpdate();
            }

            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 도서별 평가마스터 삭제
	 * @param box receive from the form object and session
	 * @return int 결과
     **/
    public int deleteBookExamPaperInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("DELETE FROM TZ_BOOKEXAM_PAPER  \n");
            sbSQL.append("WHERE  BOOKCODE = ?  \n");
            sbSQL.append("AND    MONTH = ?  \n");
            sbSQL.append("AND    SUBJ = ?  \n");
            sbSQL.append("AND    YEAR = ?  \n");
            sbSQL.append("AND    SUBJSEQ = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setInt(index++, box.getInt("p_bookcode"));
            pstmt.setInt(index++, box.getInt("p_month"));
            pstmt.setString(index++, box.getString("p_subj"));
            pstmt.setString(index++, box.getString("p_year"));
            pstmt.setString(index++, box.getString("p_subjseq"));
            isOk = pstmt.executeUpdate();

            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 도서별 평가 최초 응시
	 * @param box receive from the form object and session
	 * @return int 결과
     **/
    public int insertBookExamFirstApply(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        int index = 1;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("SELECT SUBJ  \n");
            sbSQL.append("FROM   TZ_BOOKEXAM_RESULT  \n");
            sbSQL.append("WHERE  SUBJ = " + SQLString.Format(box.getString("p_subj")) + "  \n");
            sbSQL.append("AND    YEAR = " + SQLString.Format(box.getString("p_year")) + "  \n");
            sbSQL.append("AND    SUBJSEQ = " + SQLString.Format(box.getString("p_subjseq")) + "  \n");
            sbSQL.append("AND    MONTH = " + SQLString.Format(box.getInt("p_month")) + "  \n");
            sbSQL.append("AND    BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
            sbSQL.append("AND    USERID = " + SQLString.Format(box.getSession("userid")) + "  \n");
            ls = connMgr.executeQuery(sbSQL.toString());
            
            if(!ls.next()) {            
	            sbSQL.setLength(0);
	            sbSQL.append("INSERT INTO TZ_BOOKEXAM_RESULT  \n");
	            sbSQL.append("      (SUBJ  \n");
	            sbSQL.append("     , YEAR  \n");
	            sbSQL.append("     , SUBJSEQ  \n");
	            sbSQL.append("     , MONTH  \n");
	            sbSQL.append("     , BOOKCODE  \n");
	            sbSQL.append("     , USERID  \n");
	            sbSQL.append("     , APPLYSTART  \n");
	            sbSQL.append("     , LUSERID  \n");
	            sbSQL.append("     , LDATE)  \n");
	            sbSQL.append("VALUES(?  \n");
	            sbSQL.append("     , ?  \n");
	            sbSQL.append("     , ?  \n");
	            sbSQL.append("     , ?  \n");
	            sbSQL.append("     , ?  \n");
	            sbSQL.append("     , ?  \n");
	            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
	            sbSQL.append("     , ?  \n");
	            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
	            sbSQL.append("      )  \n");
	
	            pstmt = connMgr.prepareStatement(sbSQL.toString());
	            index = 1;
	            pstmt.setString(index++, box.getString("p_subj"));
	            pstmt.setString(index++, box.getString("p_year"));
	            pstmt.setString(index++, box.getString("p_subjseq"));
	            pstmt.setInt(index++, box.getInt("p_month"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setString(index++, box.getSession("userid"));
	            pstmt.setString(index++, box.getSession("userid"));
	            isOk += pstmt.executeUpdate();
            }
            
            sbSQL.setLength(0);
            sbSQL.append("SELECT SUBJ  \n");
            sbSQL.append("FROM   TZ_BOOKEXAMEACH_RESULT  \n");
            sbSQL.append("WHERE  SUBJ = " + SQLString.Format(box.getString("p_subj")) + "  \n");
            sbSQL.append("AND    YEAR = " + SQLString.Format(box.getString("p_year")) + "  \n");
            sbSQL.append("AND    SUBJSEQ = " + SQLString.Format(box.getString("p_subjseq")) + "  \n");
            sbSQL.append("AND    MONTH = " + SQLString.Format(box.getInt("p_month")) + "  \n");
            sbSQL.append("AND    BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
            sbSQL.append("AND    USERID = " + SQLString.Format(box.getSession("userid")) + "  \n");
            sbSQL.append("AND    ROWNUM = 1  \n");
            ls = connMgr.executeQuery(sbSQL.toString());
            
            if(!ls.next()) {
	            sbSQL.setLength(0);
	            sbSQL.append("INSERT INTO TZ_BOOKEXAMEACH_RESULT  \n");
	            sbSQL.append("      (SUBJ  \n");
	            sbSQL.append("     , YEAR  \n");
	            sbSQL.append("     , SUBJSEQ  \n");
	            sbSQL.append("     , MONTH  \n");
	            sbSQL.append("     , BOOKCODE  \n");
	            sbSQL.append("     , EXAMNUM  \n");
	            sbSQL.append("     , USERID  \n");
	            sbSQL.append("     , ORDERS  \n");
	            sbSQL.append("      )  \n");
	            sbSQL.append("SELECT Z.SUBJ  \n");
	            sbSQL.append("     , Z.YEAR  \n");
	            sbSQL.append("     , Z.SUBJSEQ  \n");
	            sbSQL.append("     , Z.MONTH  \n");
	            sbSQL.append("     , Z.BOOKCODE  \n");
	            sbSQL.append("     , Z.EXAMNUM  \n");
	            sbSQL.append("     , ? AS USERID  \n");
	            sbSQL.append("     , RANK() OVER(ORDER BY Z.SORT, Z.EXAMNUM) AS RN  \n");
//	            sbSQL.append("     , RANK() OVER(ORDER BY DBMS_RANDOM.VALUE * 1000, Z.EXAMNUM) AS RN  \n");
	            sbSQL.append("FROM  (SELECT B.SUBJ  \n");
	            sbSQL.append("            , B.YEAR  \n");
	            sbSQL.append("            , B.SUBJSEQ  \n");
	            sbSQL.append("            , B.MONTH  \n");
	            sbSQL.append("            , B.BOOKCODE  \n");
	            sbSQL.append("            , A.EXAMNUM  \n");
	            sbSQL.append("            , 3 AS SORT  \n");
	            sbSQL.append("       FROM  (SELECT BOOKCODE  \n");
	            sbSQL.append("                   , EXAMNUM  \n");
	            sbSQL.append("                   , ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.VALUE * 1000, EXAMNUM) AS RN  \n");
	            sbSQL.append("              FROM   TZ_BOOKEXAM  \n");
	            sbSQL.append("              WHERE  USE_YN = 'Y'  \n");
	            sbSQL.append("              AND    BOOKCODE = ?  \n");
	            sbSQL.append("              AND    EXAMTYPE = 1  \n");
	            sbSQL.append("             ) A  \n");
	            sbSQL.append("            , TZ_BOOKEXAM_PAPER B  \n");
	            sbSQL.append("       WHERE  A.BOOKCODE = B.BOOKCODE  \n");
	            sbSQL.append("       AND    B.SUBJ = ?  \n");
	            sbSQL.append("       AND    B.YEAR = ?  \n");
	            sbSQL.append("       AND    B.SUBJSEQ = ?  \n");
	            sbSQL.append("       AND    B.MONTH = ?  \n");
	            sbSQL.append("       AND    B.BOOKCODE = ?  \n");
	            sbSQL.append("       AND    A.RN <= B.OXTYPE_CNT  \n");
	            sbSQL.append("       UNION ALL  \n");
	            sbSQL.append("       SELECT B.SUBJ  \n");
	            sbSQL.append("            , B.YEAR  \n");
	            sbSQL.append("            , B.SUBJSEQ  \n");
	            sbSQL.append("            , B.MONTH  \n");
	            sbSQL.append("            , B.BOOKCODE  \n");
	            sbSQL.append("            , A.EXAMNUM  \n");
	            sbSQL.append("            , 1 AS SORT  \n");
	            sbSQL.append("       FROM  (SELECT BOOKCODE  \n");
	            sbSQL.append("                   , EXAMNUM  \n");
	            sbSQL.append("                   , ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.VALUE * 1000, EXAMNUM) AS RN  \n");
	            sbSQL.append("              FROM   TZ_BOOKEXAM  \n");
	            sbSQL.append("              WHERE  USE_YN = 'Y'  \n");
	            sbSQL.append("              AND    BOOKCODE = ?  \n");
	            sbSQL.append("              AND    EXAMTYPE = 2  \n");
	            sbSQL.append("             ) A  \n");
	            sbSQL.append("            , TZ_BOOKEXAM_PAPER B  \n");
	            sbSQL.append("       WHERE  A.BOOKCODE = B.BOOKCODE  \n");
	            sbSQL.append("       AND    B.SUBJ = ?  \n");
	            sbSQL.append("       AND    B.YEAR = ?  \n");
	            sbSQL.append("       AND    B.SUBJSEQ = ?  \n");
	            sbSQL.append("       AND    B.MONTH = ?  \n");
	            sbSQL.append("       AND    B.BOOKCODE = ?  \n");
	            sbSQL.append("       AND    A.RN <= B.CHOICE_CNT  \n");
	            sbSQL.append("       UNION ALL  \n");
	            sbSQL.append("       SELECT B.SUBJ  \n");
	            sbSQL.append("            , B.YEAR  \n");
	            sbSQL.append("            , B.SUBJSEQ  \n");
	            sbSQL.append("            , B.MONTH  \n");
	            sbSQL.append("            , B.BOOKCODE  \n");
	            sbSQL.append("            , A.EXAMNUM  \n");
	            sbSQL.append("            , 2 AS SORT  \n");
	            sbSQL.append("       FROM  (SELECT BOOKCODE  \n");
	            sbSQL.append("                   , EXAMNUM  \n");
	            sbSQL.append("                   , ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.VALUE * 1000, EXAMNUM) AS RN  \n");
	            sbSQL.append("              FROM   TZ_BOOKEXAM  \n");
	            sbSQL.append("              WHERE  USE_YN = 'Y'  \n");
	            sbSQL.append("              AND    BOOKCODE = ?  \n");
	            sbSQL.append("              AND    EXAMTYPE = 3  \n");
	            sbSQL.append("             ) A  \n");
	            sbSQL.append("            , TZ_BOOKEXAM_PAPER B  \n");
	            sbSQL.append("       WHERE  A.BOOKCODE = B.BOOKCODE  \n");
	            sbSQL.append("       AND    B.SUBJ = ?  \n");
	            sbSQL.append("       AND    B.YEAR = ?  \n");
	            sbSQL.append("       AND    B.SUBJSEQ = ?  \n");
	            sbSQL.append("       AND    B.MONTH = ?  \n");
	            sbSQL.append("       AND    B.BOOKCODE = ?  \n");
	            sbSQL.append("       AND    A.RN <= B.WORDS_CNT  \n");
	            sbSQL.append("       UNION ALL  \n");
	            sbSQL.append("       SELECT B.SUBJ  \n");
	            sbSQL.append("            , B.YEAR  \n");
	            sbSQL.append("            , B.SUBJSEQ  \n");
	            sbSQL.append("            , B.MONTH  \n");
	            sbSQL.append("            , B.BOOKCODE  \n");
	            sbSQL.append("            , A.EXAMNUM  \n");
	            sbSQL.append("            , 4 AS SORT  \n");
	            sbSQL.append("       FROM  (SELECT BOOKCODE  \n");
	            sbSQL.append("                   , EXAMNUM  \n");
	            sbSQL.append("                   , ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.VALUE * 1000, EXAMNUM) AS RN  \n");
	            sbSQL.append("              FROM   TZ_BOOKEXAM  \n");
	            sbSQL.append("              WHERE  USE_YN = 'Y'  \n");
	            sbSQL.append("              AND    BOOKCODE = ?  \n");
	            sbSQL.append("              AND    EXAMTYPE = 4  \n");
	            sbSQL.append("             ) A  \n");
	            sbSQL.append("            , TZ_BOOKEXAM_PAPER B  \n");
	            sbSQL.append("       WHERE  A.BOOKCODE = B.BOOKCODE  \n");
	            sbSQL.append("       AND    B.SUBJ = ?  \n");
	            sbSQL.append("       AND    B.YEAR = ?  \n");
	            sbSQL.append("       AND    B.SUBJSEQ = ?  \n");
	            sbSQL.append("       AND    B.MONTH = ?  \n");
	            sbSQL.append("       AND    B.BOOKCODE = ?  \n");
	            sbSQL.append("       AND    A.RN <= B.LIST_CNT  \n");
	            sbSQL.append("       UNION ALL  \n");
	            sbSQL.append("       SELECT B.SUBJ  \n");
	            sbSQL.append("            , B.YEAR  \n");
	            sbSQL.append("            , B.SUBJSEQ  \n");
	            sbSQL.append("            , B.MONTH  \n");
	            sbSQL.append("            , B.BOOKCODE  \n");
	            sbSQL.append("            , A.EXAMNUM  \n");
	            sbSQL.append("            , 5 AS SORT  \n");
	            sbSQL.append("       FROM  (SELECT BOOKCODE  \n");
	            sbSQL.append("                   , EXAMNUM  \n");
	            sbSQL.append("                   , ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.VALUE * 1000, EXAMNUM) AS RN  \n");
	            sbSQL.append("              FROM   TZ_BOOKEXAM  \n");
	            sbSQL.append("              WHERE  USE_YN = 'Y'  \n");
	            sbSQL.append("              AND    BOOKCODE = ?  \n");
	            sbSQL.append("              AND    EXAMTYPE = 5  \n");
	            sbSQL.append("             ) A  \n");
	            sbSQL.append("            , TZ_BOOKEXAM_PAPER B  \n");
	            sbSQL.append("       WHERE  A.BOOKCODE = B.BOOKCODE  \n");
	            sbSQL.append("       AND    B.SUBJ = ?  \n");
	            sbSQL.append("       AND    B.YEAR = ?  \n");
	            sbSQL.append("       AND    B.SUBJSEQ = ?  \n");
	            sbSQL.append("       AND    B.MONTH = ?  \n");
	            sbSQL.append("       AND    B.BOOKCODE = ?  \n");
	            sbSQL.append("       AND    A.RN <= B.SHORT_CNT  \n");
	            sbSQL.append("       UNION ALL  \n");
	            sbSQL.append("       SELECT B.SUBJ  \n");
	            sbSQL.append("            , B.YEAR  \n");
	            sbSQL.append("            , B.SUBJSEQ  \n");
	            sbSQL.append("            , B.MONTH  \n");
	            sbSQL.append("            , B.BOOKCODE  \n");
	            sbSQL.append("            , A.EXAMNUM  \n");
	            sbSQL.append("            , 6 AS SORT  \n");
	            sbSQL.append("       FROM  (SELECT BOOKCODE  \n");
	            sbSQL.append("                   , EXAMNUM  \n");
	            sbSQL.append("                   , ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.VALUE * 1000, EXAMNUM) AS RN  \n");
	            sbSQL.append("              FROM   TZ_BOOKEXAM  \n");
	            sbSQL.append("              WHERE  USE_YN = 'Y'  \n");
	            sbSQL.append("              AND    BOOKCODE = ?  \n");
	            sbSQL.append("              AND    EXAMTYPE = 6  \n");
	            sbSQL.append("             ) A  \n");
	            sbSQL.append("            , TZ_BOOKEXAM_PAPER B  \n");
	            sbSQL.append("       WHERE  A.BOOKCODE = B.BOOKCODE  \n");
	            sbSQL.append("       AND    B.SUBJ = ?  \n");
	            sbSQL.append("       AND    B.YEAR = ?  \n");
	            sbSQL.append("       AND    B.SUBJSEQ = ?  \n");
	            sbSQL.append("       AND    B.MONTH = ?  \n");
	            sbSQL.append("       AND    B.BOOKCODE = ?  \n");
	            sbSQL.append("       AND    A.RN <= B.DESCRIPT_CNT  \n");
	            sbSQL.append("      ) Z  \n");
	            
	            pstmt = connMgr.prepareStatement(sbSQL.toString());
	            index = 1;
	            pstmt.setString(index++, box.getSession("userid"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setString(index++, box.getString("p_subj"));
	            pstmt.setString(index++, box.getString("p_year"));
	            pstmt.setString(index++, box.getString("p_subjseq"));
	            pstmt.setInt(index++, box.getInt("p_month"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setString(index++, box.getString("p_subj"));
	            pstmt.setString(index++, box.getString("p_year"));
	            pstmt.setString(index++, box.getString("p_subjseq"));
	            pstmt.setInt(index++, box.getInt("p_month"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setString(index++, box.getString("p_subj"));
	            pstmt.setString(index++, box.getString("p_year"));
	            pstmt.setString(index++, box.getString("p_subjseq"));
	            pstmt.setInt(index++, box.getInt("p_month"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setString(index++, box.getString("p_subj"));
	            pstmt.setString(index++, box.getString("p_year"));
	            pstmt.setString(index++, box.getString("p_subjseq"));
	            pstmt.setInt(index++, box.getInt("p_month"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setString(index++, box.getString("p_subj"));
	            pstmt.setString(index++, box.getString("p_year"));
	            pstmt.setString(index++, box.getString("p_subjseq"));
	            pstmt.setInt(index++, box.getInt("p_month"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            pstmt.setString(index++, box.getString("p_subj"));
	            pstmt.setString(index++, box.getString("p_year"));
	            pstmt.setString(index++, box.getString("p_subjseq"));
	            pstmt.setInt(index++, box.getInt("p_month"));
	            pstmt.setInt(index++, box.getInt("p_bookcode"));
	            isOk += pstmt.executeUpdate();
            }

            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 도서별 평가 학습자 문제 목록
	 * @param box          receive from the form object and session
	 * @return ArrayList   도서별 평가 학습자 문제 목록
     **/
    public ArrayList selectBookExamUserExamList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
    		if(box.getString("p_isAdmin").equals("Y")) {
                sbSQL.append("SELECT Z.BOOKCODE  \n");
                sbSQL.append("     , Z.EXAMNUM  \n");
                sbSQL.append("     , Z.EXAMTEXT  \n");
                sbSQL.append("     , Z.EXAMCONTS  \n");
                sbSQL.append("     , Z.EXAMTYPE  \n");
                sbSQL.append("     , Z.LISTTYPECNT  \n");
                sbSQL.append("     , Z.REAL_IMAGEFILE  \n");
                sbSQL.append("     , Z.SAVE_IMAGEFILE  \n");
                sbSQL.append("     , Z.REAL_AUDIOFILE  \n");
                sbSQL.append("     , Z.SAVE_AUDIOFILE  \n");
                sbSQL.append("     , Z.REAL_MOVIEFILE  \n");
                sbSQL.append("     , Z.SAVE_MOVIEFILE  \n");
                sbSQL.append("     , Z.REAL_FLASHFILE  \n");
                sbSQL.append("     , Y.SELNUM  \n");
                sbSQL.append("     , Y.SELTEXT  \n");
                sbSQL.append("     , Y.REAL_IMAGEFILE AS REAL_SELIMAGE  \n");
                sbSQL.append("     , Y.SAVE_IMAGEFILE AS SAVE_SELIMAGE  \n");
                sbSQL.append("FROM  (SELECT A.BOOKCODE  \n");
                sbSQL.append("            , A.EXAMNUM  \n");
                sbSQL.append("            , A.EXAMTEXT  \n");
                sbSQL.append("            , A.EXAMCONTS  \n");
                sbSQL.append("            , A.EXAMTYPE  \n");
                sbSQL.append("            , A.LISTTYPECNT  \n");
                sbSQL.append("            , A.REAL_IMAGEFILE  \n");
                sbSQL.append("            , A.SAVE_IMAGEFILE  \n");
                sbSQL.append("            , A.REAL_AUDIOFILE  \n");
                sbSQL.append("            , A.SAVE_AUDIOFILE  \n");
                sbSQL.append("            , A.REAL_MOVIEFILE  \n");
                sbSQL.append("            , A.SAVE_MOVIEFILE  \n");
                sbSQL.append("            , A.REAL_FLASHFILE  \n");
                sbSQL.append("            , 3 AS SORT  \n");
                sbSQL.append("            , DBMS_RANDOM.VALUE * 1000 AS RAN  \n");
                sbSQL.append("       FROM  (SELECT BOOKCODE  \n");
                sbSQL.append("                   , EXAMNUM  \n");
                sbSQL.append("                   , EXAMTEXT  \n");
                sbSQL.append("                   , EXAMCONTS  \n");
                sbSQL.append("                   , EXAMTYPE  \n");
                sbSQL.append("                   , LISTTYPECNT  \n");
                sbSQL.append("                   , REAL_IMAGEFILE  \n");
                sbSQL.append("                   , SAVE_IMAGEFILE  \n");
                sbSQL.append("                   , REAL_AUDIOFILE  \n");
                sbSQL.append("                   , SAVE_AUDIOFILE  \n");
                sbSQL.append("                   , REAL_MOVIEFILE  \n");
                sbSQL.append("                   , SAVE_MOVIEFILE  \n");
                sbSQL.append("                   , REAL_FLASHFILE  \n");
                sbSQL.append("                   , SAVE_FLASHFILE  \n");
                sbSQL.append("                   , ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.VALUE * 1000, EXAMNUM) AS RN  \n");
                sbSQL.append("              FROM   TZ_BOOKEXAM  \n");
                sbSQL.append("              WHERE  USE_YN = 'Y'  \n");
                sbSQL.append("              AND    BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
                sbSQL.append("              AND    EXAMTYPE = 1  \n");
                sbSQL.append("             ) A  \n");
                sbSQL.append("            , TZ_BOOKEXAM_PAPER B  \n");
                sbSQL.append("       WHERE  A.BOOKCODE = B.BOOKCODE  \n");
                sbSQL.append("       AND    B.SUBJ = " + SQLString.Format(box.getString("p_subj")) + "  \n");
                sbSQL.append("       AND    B.YEAR = " + SQLString.Format(box.getString("p_year")) + "  \n");
                sbSQL.append("       AND    B.SUBJSEQ = " + SQLString.Format(box.getString("p_subjseq")) + "  \n");
                sbSQL.append("       AND    B.BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
                sbSQL.append("       AND    B.MONTH = " + SQLString.Format(box.getInt("p_month")) + "  \n");
                sbSQL.append("       AND    A.RN <= B.OXTYPE_CNT  \n");
                sbSQL.append("       UNION ALL  \n");
                sbSQL.append("       SELECT A.BOOKCODE  \n");
                sbSQL.append("            , A.EXAMNUM  \n");
                sbSQL.append("            , A.EXAMTEXT  \n");
                sbSQL.append("            , A.EXAMCONTS  \n");
                sbSQL.append("            , A.EXAMTYPE  \n");
                sbSQL.append("            , A.LISTTYPECNT  \n");
                sbSQL.append("            , A.REAL_IMAGEFILE  \n");
                sbSQL.append("            , A.SAVE_IMAGEFILE  \n");
                sbSQL.append("            , A.REAL_AUDIOFILE  \n");
                sbSQL.append("            , A.SAVE_AUDIOFILE  \n");
                sbSQL.append("            , A.REAL_MOVIEFILE  \n");
                sbSQL.append("            , A.SAVE_MOVIEFILE  \n");
                sbSQL.append("            , A.REAL_FLASHFILE  \n");
                sbSQL.append("            , 1 AS SORT  \n");
                sbSQL.append("            , DBMS_RANDOM.VALUE * 1000 AS RAN  \n");
                sbSQL.append("       FROM  (SELECT BOOKCODE  \n");
                sbSQL.append("                   , EXAMNUM  \n");
                sbSQL.append("                   , EXAMTEXT  \n");
                sbSQL.append("                   , EXAMCONTS  \n");
                sbSQL.append("                   , EXAMTYPE  \n");
                sbSQL.append("                   , LISTTYPECNT  \n");
                sbSQL.append("                   , REAL_IMAGEFILE  \n");
                sbSQL.append("                   , SAVE_IMAGEFILE  \n");
                sbSQL.append("                   , REAL_AUDIOFILE  \n");
                sbSQL.append("                   , SAVE_AUDIOFILE  \n");
                sbSQL.append("                   , REAL_MOVIEFILE  \n");
                sbSQL.append("                   , SAVE_MOVIEFILE  \n");
                sbSQL.append("                   , REAL_FLASHFILE  \n");
                sbSQL.append("                   , SAVE_FLASHFILE  \n");
                sbSQL.append("                   , ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.VALUE * 1000, EXAMNUM) AS RN  \n");
                sbSQL.append("              FROM   TZ_BOOKEXAM  \n");
                sbSQL.append("              WHERE  USE_YN = 'Y'  \n");
                sbSQL.append("              AND    BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
                sbSQL.append("              AND    EXAMTYPE = 2  \n");
                sbSQL.append("             ) A  \n");
                sbSQL.append("            , TZ_BOOKEXAM_PAPER B  \n");
                sbSQL.append("       WHERE  A.BOOKCODE = B.BOOKCODE  \n");
                sbSQL.append("       AND    B.SUBJ = " + SQLString.Format(box.getString("p_subj")) + "  \n");
                sbSQL.append("       AND    B.YEAR = " + SQLString.Format(box.getString("p_year")) + "  \n");
                sbSQL.append("       AND    B.SUBJSEQ = " + SQLString.Format(box.getString("p_subjseq")) + "  \n");
                sbSQL.append("       AND    B.BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
                sbSQL.append("       AND    B.MONTH = " + SQLString.Format(box.getInt("p_month")) + "  \n");
                sbSQL.append("       AND    A.RN <= B.CHOICE_CNT  \n");
                sbSQL.append("       UNION ALL  \n");
                sbSQL.append("       SELECT A.BOOKCODE  \n");
                sbSQL.append("            , A.EXAMNUM  \n");
                sbSQL.append("            , A.EXAMTEXT  \n");
                sbSQL.append("            , A.EXAMCONTS  \n");
                sbSQL.append("            , A.EXAMTYPE  \n");
                sbSQL.append("            , A.LISTTYPECNT  \n");
                sbSQL.append("            , A.REAL_IMAGEFILE  \n");
                sbSQL.append("            , A.SAVE_IMAGEFILE  \n");
                sbSQL.append("            , A.REAL_AUDIOFILE  \n");
                sbSQL.append("            , A.SAVE_AUDIOFILE  \n");
                sbSQL.append("            , A.REAL_MOVIEFILE  \n");
                sbSQL.append("            , A.SAVE_MOVIEFILE  \n");
                sbSQL.append("            , A.REAL_FLASHFILE  \n");
                sbSQL.append("            , 2 AS SORT  \n");
                sbSQL.append("            , DBMS_RANDOM.VALUE * 1000 AS RAN  \n");
                sbSQL.append("       FROM  (SELECT BOOKCODE  \n");
                sbSQL.append("                   , EXAMNUM  \n");
                sbSQL.append("                   , EXAMTEXT  \n");
                sbSQL.append("                   , EXAMCONTS  \n");
                sbSQL.append("                   , EXAMTYPE  \n");
                sbSQL.append("                   , LISTTYPECNT  \n");
                sbSQL.append("                   , REAL_IMAGEFILE  \n");
                sbSQL.append("                   , SAVE_IMAGEFILE  \n");
                sbSQL.append("                   , REAL_AUDIOFILE  \n");
                sbSQL.append("                   , SAVE_AUDIOFILE  \n");
                sbSQL.append("                   , REAL_MOVIEFILE  \n");
                sbSQL.append("                   , SAVE_MOVIEFILE  \n");
                sbSQL.append("                   , REAL_FLASHFILE  \n");
                sbSQL.append("                   , SAVE_FLASHFILE  \n");
                sbSQL.append("                   , ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.VALUE * 1000, EXAMNUM) AS RN  \n");
                sbSQL.append("              FROM   TZ_BOOKEXAM  \n");
                sbSQL.append("              WHERE  USE_YN = 'Y'  \n");
                sbSQL.append("              AND    BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
                sbSQL.append("              AND    EXAMTYPE = 3  \n");
                sbSQL.append("             ) A  \n");
                sbSQL.append("            , TZ_BOOKEXAM_PAPER B  \n");
                sbSQL.append("       WHERE  A.BOOKCODE = B.BOOKCODE  \n");
                sbSQL.append("       AND    B.SUBJ = " + SQLString.Format(box.getString("p_subj")) + "  \n");
                sbSQL.append("       AND    B.YEAR = " + SQLString.Format(box.getString("p_year")) + "  \n");
                sbSQL.append("       AND    B.SUBJSEQ = " + SQLString.Format(box.getString("p_subjseq")) + "  \n");
                sbSQL.append("       AND    B.BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
                sbSQL.append("       AND    B.MONTH = " + SQLString.Format(box.getInt("p_month")) + "  \n");
                sbSQL.append("       AND    A.RN <= B.WORDS_CNT  \n");
                sbSQL.append("       UNION ALL  \n");
                sbSQL.append("       SELECT A.BOOKCODE  \n");
                sbSQL.append("            , A.EXAMNUM  \n");
                sbSQL.append("            , A.EXAMTEXT  \n");
                sbSQL.append("            , A.EXAMCONTS  \n");
                sbSQL.append("            , A.EXAMTYPE  \n");
                sbSQL.append("            , A.LISTTYPECNT  \n");
                sbSQL.append("            , A.REAL_IMAGEFILE  \n");
                sbSQL.append("            , A.SAVE_IMAGEFILE  \n");
                sbSQL.append("            , A.REAL_AUDIOFILE  \n");
                sbSQL.append("            , A.SAVE_AUDIOFILE  \n");
                sbSQL.append("            , A.REAL_MOVIEFILE  \n");
                sbSQL.append("            , A.SAVE_MOVIEFILE  \n");
                sbSQL.append("            , A.REAL_FLASHFILE  \n");
                sbSQL.append("            , 4 AS SORT  \n");
                sbSQL.append("            , DBMS_RANDOM.VALUE * 1000 AS RAN  \n");
                sbSQL.append("       FROM  (SELECT BOOKCODE  \n");
                sbSQL.append("                   , EXAMNUM  \n");
                sbSQL.append("                   , EXAMTEXT  \n");
                sbSQL.append("                   , EXAMCONTS  \n");
                sbSQL.append("                   , EXAMTYPE  \n");
                sbSQL.append("                   , LISTTYPECNT  \n");
                sbSQL.append("                   , REAL_IMAGEFILE  \n");
                sbSQL.append("                   , SAVE_IMAGEFILE  \n");
                sbSQL.append("                   , REAL_AUDIOFILE  \n");
                sbSQL.append("                   , SAVE_AUDIOFILE  \n");
                sbSQL.append("                   , REAL_MOVIEFILE  \n");
                sbSQL.append("                   , SAVE_MOVIEFILE  \n");
                sbSQL.append("                   , REAL_FLASHFILE  \n");
                sbSQL.append("                   , SAVE_FLASHFILE  \n");
                sbSQL.append("                   , ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.VALUE * 1000, EXAMNUM) AS RN  \n");
                sbSQL.append("              FROM   TZ_BOOKEXAM  \n");
                sbSQL.append("              WHERE  USE_YN = 'Y'  \n");
                sbSQL.append("              AND    BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
                sbSQL.append("              AND    EXAMTYPE = 4  \n");
                sbSQL.append("             ) A  \n");
                sbSQL.append("            , TZ_BOOKEXAM_PAPER B  \n");
                sbSQL.append("       WHERE  A.BOOKCODE = B.BOOKCODE  \n");
                sbSQL.append("       AND    B.SUBJ = " + SQLString.Format(box.getString("p_subj")) + "  \n");
                sbSQL.append("       AND    B.YEAR = " + SQLString.Format(box.getString("p_year")) + "  \n");
                sbSQL.append("       AND    B.SUBJSEQ = " + SQLString.Format(box.getString("p_subjseq")) + "  \n");
                sbSQL.append("       AND    B.BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
                sbSQL.append("       AND    B.MONTH = " + SQLString.Format(box.getInt("p_month")) + "  \n");
                sbSQL.append("       AND    A.RN <= B.LIST_CNT  \n");
                sbSQL.append("       UNION ALL  \n");
                sbSQL.append("       SELECT A.BOOKCODE  \n");
                sbSQL.append("            , A.EXAMNUM  \n");
                sbSQL.append("            , A.EXAMTEXT  \n");
                sbSQL.append("            , A.EXAMCONTS  \n");
                sbSQL.append("            , A.EXAMTYPE  \n");
                sbSQL.append("            , A.LISTTYPECNT  \n");
                sbSQL.append("            , A.REAL_IMAGEFILE  \n");
                sbSQL.append("            , A.SAVE_IMAGEFILE  \n");
                sbSQL.append("            , A.REAL_AUDIOFILE  \n");
                sbSQL.append("            , A.SAVE_AUDIOFILE  \n");
                sbSQL.append("            , A.REAL_MOVIEFILE  \n");
                sbSQL.append("            , A.SAVE_MOVIEFILE  \n");
                sbSQL.append("            , A.REAL_FLASHFILE  \n");
                sbSQL.append("            , 5 AS SORT  \n");
                sbSQL.append("            , DBMS_RANDOM.VALUE * 1000 AS RAN  \n");
                sbSQL.append("       FROM  (SELECT BOOKCODE  \n");
                sbSQL.append("                   , EXAMNUM  \n");
                sbSQL.append("                   , EXAMTEXT  \n");
                sbSQL.append("                   , EXAMCONTS  \n");
                sbSQL.append("                   , EXAMTYPE  \n");
                sbSQL.append("                   , LISTTYPECNT  \n");
                sbSQL.append("                   , REAL_IMAGEFILE  \n");
                sbSQL.append("                   , SAVE_IMAGEFILE  \n");
                sbSQL.append("                   , REAL_AUDIOFILE  \n");
                sbSQL.append("                   , SAVE_AUDIOFILE  \n");
                sbSQL.append("                   , REAL_MOVIEFILE  \n");
                sbSQL.append("                   , SAVE_MOVIEFILE  \n");
                sbSQL.append("                   , REAL_FLASHFILE  \n");
                sbSQL.append("                   , SAVE_FLASHFILE  \n");
                sbSQL.append("                   , ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.VALUE * 1000, EXAMNUM) AS RN  \n");
                sbSQL.append("              FROM   TZ_BOOKEXAM  \n");
                sbSQL.append("              WHERE  USE_YN = 'Y'  \n");
                sbSQL.append("              AND    BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
                sbSQL.append("              AND    EXAMTYPE = 5  \n");
                sbSQL.append("             ) A  \n");
                sbSQL.append("            , TZ_BOOKEXAM_PAPER B  \n");
                sbSQL.append("       WHERE  A.BOOKCODE = B.BOOKCODE  \n");
                sbSQL.append("       AND    B.SUBJ = " + SQLString.Format(box.getString("p_subj")) + "  \n");
                sbSQL.append("       AND    B.YEAR = " + SQLString.Format(box.getString("p_year")) + "  \n");
                sbSQL.append("       AND    B.SUBJSEQ = " + SQLString.Format(box.getString("p_subjseq")) + "  \n");
                sbSQL.append("       AND    B.BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
                sbSQL.append("       AND    B.MONTH = " + SQLString.Format(box.getInt("p_month")) + "  \n");
                sbSQL.append("       AND    A.RN <= B.SHORT_CNT  \n");
                sbSQL.append("       UNION ALL  \n");
                sbSQL.append("       SELECT A.BOOKCODE  \n");
                sbSQL.append("            , A.EXAMNUM  \n");
                sbSQL.append("            , A.EXAMTEXT  \n");
                sbSQL.append("            , A.EXAMCONTS  \n");
                sbSQL.append("            , A.EXAMTYPE  \n");
                sbSQL.append("            , A.LISTTYPECNT  \n");
                sbSQL.append("            , A.REAL_IMAGEFILE  \n");
                sbSQL.append("            , A.SAVE_IMAGEFILE  \n");
                sbSQL.append("            , A.REAL_AUDIOFILE  \n");
                sbSQL.append("            , A.SAVE_AUDIOFILE  \n");
                sbSQL.append("            , A.REAL_MOVIEFILE  \n");
                sbSQL.append("            , A.SAVE_MOVIEFILE  \n");
                sbSQL.append("            , A.REAL_FLASHFILE  \n");
                sbSQL.append("            , 6 AS SORT  \n");
                sbSQL.append("            , DBMS_RANDOM.VALUE * 1000 AS RAN  \n");
                sbSQL.append("       FROM  (SELECT BOOKCODE  \n");
                sbSQL.append("                   , EXAMNUM  \n");
                sbSQL.append("                   , EXAMTEXT  \n");
                sbSQL.append("                   , EXAMCONTS  \n");
                sbSQL.append("                   , EXAMTYPE  \n");
                sbSQL.append("                   , LISTTYPECNT  \n");
                sbSQL.append("                   , REAL_IMAGEFILE  \n");
                sbSQL.append("                   , SAVE_IMAGEFILE  \n");
                sbSQL.append("                   , REAL_AUDIOFILE  \n");
                sbSQL.append("                   , SAVE_AUDIOFILE  \n");
                sbSQL.append("                   , REAL_MOVIEFILE  \n");
                sbSQL.append("                   , SAVE_MOVIEFILE  \n");
                sbSQL.append("                   , REAL_FLASHFILE  \n");
                sbSQL.append("                   , SAVE_FLASHFILE  \n");
                sbSQL.append("                   , ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.VALUE * 1000, EXAMNUM) AS RN  \n");
                sbSQL.append("              FROM   TZ_BOOKEXAM  \n");
                sbSQL.append("              WHERE  USE_YN = 'Y'  \n");
                sbSQL.append("              AND    BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
                sbSQL.append("              AND    EXAMTYPE = 6  \n");
                sbSQL.append("             ) A  \n");
                sbSQL.append("            , TZ_BOOKEXAM_PAPER B  \n");
                sbSQL.append("       WHERE  A.BOOKCODE = B.BOOKCODE  \n");
                sbSQL.append("       AND    B.SUBJ = " + SQLString.Format(box.getString("p_subj")) + "  \n");
                sbSQL.append("       AND    B.YEAR = " + SQLString.Format(box.getString("p_year")) + "  \n");
                sbSQL.append("       AND    B.SUBJSEQ = " + SQLString.Format(box.getString("p_subjseq")) + "  \n");
                sbSQL.append("       AND    B.BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
                sbSQL.append("       AND    B.MONTH = " + SQLString.Format(box.getInt("p_month")) + "  \n");
                sbSQL.append("       AND    A.RN <= B.DESCRIPT_CNT  \n");
                sbSQL.append("      ) Z  \n");
                sbSQL.append("     , TZ_BOOKEXAMSEL Y  \n");
                sbSQL.append("WHERE  Z.BOOKCODE = Y.BOOKCODE (+)  \n");
                sbSQL.append("AND    Z.EXAMNUM = Y.EXAMNUM (+)  \n");
                sbSQL.append("ORDER BY Z.SORT, Z.EXAMNUM, Y.SELNUM  \n");
    		} else {
	            sbSQL.append("SELECT A.SUBJ  \n");
	            sbSQL.append("     , A.YEAR  \n");
	            sbSQL.append("     , A.SUBJSEQ  \n");
	            sbSQL.append("     , A.MONTH  \n");
	            sbSQL.append("     , A.BOOKCODE  \n");
	            sbSQL.append("     , A.USERID  \n");
	            sbSQL.append("     , B.EXAMNUM  \n");
	            sbSQL.append("     , B.EXAMTEXT  \n");
	            sbSQL.append("     , B.EXAMCONTS  \n");
	            sbSQL.append("     , B.EXAMTYPE  \n");
	            sbSQL.append("     , B.LISTTYPECNT  \n");
	            sbSQL.append("     , B.REAL_IMAGEFILE  \n");
	            sbSQL.append("     , B.SAVE_IMAGEFILE  \n");
	            sbSQL.append("     , B.REAL_AUDIOFILE  \n");
	            sbSQL.append("     , B.SAVE_AUDIOFILE  \n");
	            sbSQL.append("     , B.REAL_MOVIEFILE  \n");
	            sbSQL.append("     , B.SAVE_MOVIEFILE  \n");
	            sbSQL.append("     , B.REAL_FLASHFILE  \n");
	            sbSQL.append("     , B.SAVE_FLASHFILE  \n");
	            sbSQL.append("     , C.SELNUM  \n");
	            sbSQL.append("     , C.SELTEXT  \n");
	            sbSQL.append("     , C.REAL_IMAGEFILE AS REAL_SELIMAGE  \n");
	            sbSQL.append("     , C.SAVE_IMAGEFILE AS SAVE_SELIMAGE  \n");
	            sbSQL.append("     , A.CHOICE_ANSWER  \n");
	            sbSQL.append("     , A.SCORE  \n");
	            sbSQL.append("     , A.EXAMEXP_YN  \n");
	            sbSQL.append("     , A.CORRECTION  \n");
	            sbSQL.append("     , B.EXAMEXP  \n");
	            sbSQL.append("     , A.TEXT_ANSWER  \n");
	            sbSQL.append("     , C.ANSWER_YN  \n");
	            sbSQL.append("FROM   TZ_BOOKEXAMEACH_RESULT A  \n");
	            sbSQL.append("     , TZ_BOOKEXAM B  \n");
	            sbSQL.append("     , TZ_BOOKEXAMSEL C  \n");
	            sbSQL.append("WHERE  A.BOOKCODE = B.BOOKCODE  \n");
	            sbSQL.append("AND    A.EXAMNUM = B.EXAMNUM  \n");
	            sbSQL.append("AND    A.BOOKCODE = C.BOOKCODE (+)  \n");
	            sbSQL.append("AND    A.EXAMNUM = C.EXAMNUM (+)  \n");
	            sbSQL.append("AND    A.SUBJ = " + SQLString.Format(box.getString("p_subj")) + "  \n");
	            sbSQL.append("AND    A.YEAR = " + SQLString.Format(box.getString("p_year")) + "  \n");
	            sbSQL.append("AND    A.SUBJSEQ = " + SQLString.Format(box.getString("p_subjseq")) + "  \n");
	            sbSQL.append("AND    A.MONTH = " + SQLString.Format(box.getInt("p_month")) + "  \n");
	            sbSQL.append("AND    A.BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
	            sbSQL.append("AND    A.USERID = " + SQLString.Format(box.getSession("userid")) + "  \n");
	            sbSQL.append("ORDER BY A.ORDERS, C.SELNUM  \n");
    		}
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }
    
    /**
	 * 도서별 평가 응시
	 * @param box receive from the form object and session
	 * @return int 결과
     **/
    public int updateBookExamUserInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_BOOKEXAM_RESULT A  \n");
            sbSQL.append("SET    FINAL_YN = ?  \n");
            if(box.getString("p_isfinal").equals("Y")) {
	            sbSQL.append("     , APPLYEND = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
	            sbSQL.append("     , EXTRA_YN = NVL((SELECT 'N'  \n");
	            sbSQL.append("                       FROM   TZ_BOOKEXAM_PAPER  \n");
	            sbSQL.append("                       WHERE  SUBJ = A.SUBJ  \n");
	            sbSQL.append("                       AND    YEAR = A.YEAR  \n");
	            sbSQL.append("                       AND    SUBJSEQ = A.SUBJSEQ  \n");
	            sbSQL.append("                       AND    MONTH = A.MONTH  \n");
	            sbSQL.append("                       AND    BOOKCODE = A.BOOKCODE  \n");
	            sbSQL.append("                       AND    TO_CHAR(SYSDATE, 'YYYYMMDD') BETWEEN EXAMSTART AND EXAMEND), 'Y')  \n");
            }
            if(!box.getRealFileName("p_file").equals("")) {
            	sbSQL.append("     , REAL_FILENAME = ?  \n");
            	sbSQL.append("     , SAVE_FILENAME = ?  \n");
            }
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("WHERE  SUBJ = ?  \n");
            sbSQL.append("AND    YEAR = ?  \n");
            sbSQL.append("AND    SUBJSEQ = ?  \n");
            sbSQL.append("AND    MONTH = ?  \n");
            sbSQL.append("AND    BOOKCODE = ?  \n");
            sbSQL.append("AND    USERID = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getStringDefault("p_isfinal", "N"));
            if(!box.getRealFileName("p_file").equals("")) {
            	pstmt.setString(index++, box.getRealFileName("p_file"));
            	pstmt.setString(index++, box.getNewFileName("p_file"));
            }
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setString(index++, box.getString("p_subj"));
            pstmt.setString(index++, box.getString("p_year"));
            pstmt.setString(index++, box.getString("p_subjseq"));
            pstmt.setInt(index++, box.getInt("p_month"));
            pstmt.setInt(index++, box.getInt("p_bookcode"));
            pstmt.setString(index++, box.getSession("userid"));
            isOk = pstmt.executeUpdate();
            
            Vector v_examnums = box.getVector("p_examnum");
            Vector v_examtypes = box.getVector("p_examtype");
            
            sbSQL.setLength(0);
            sbSQL.append("UPDATE TZ_BOOKEXAMEACH_RESULT  \n");
            sbSQL.append("SET    CHOICE_ANSWER = ?  \n");
            sbSQL.append("     , TEXT_ANSWER = ?  \n");
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("WHERE  SUBJ = ?  \n");
            sbSQL.append("AND    YEAR = ?  \n");
            sbSQL.append("AND    SUBJSEQ = ?  \n");
            sbSQL.append("AND    MONTH = ?  \n");
            sbSQL.append("AND    BOOKCODE = ?  \n");
            sbSQL.append("AND    USERID = ?  \n");
            sbSQL.append("AND    EXAMNUM = ?  \n");
            pstmt = connMgr.prepareStatement(sbSQL.toString());
            
            for(int i = 0 ; i < v_examnums.size() ; i++) {
            	int v_choice = 0;
            	String v_text = "";
            	if(v_examtypes.elementAt(i).toString().equals("1") || v_examtypes.elementAt(i).toString().equals("2")) {
            		v_choice = box.getInt("p_" + v_examnums.elementAt(i).toString());
            	} else if(v_examtypes.elementAt(i).toString().equals("4")) {
            		Vector v_list = box.getVector("p_" + v_examnums.elementAt(i).toString());
            		for(int j = 0 ; j < v_list.size() ; j++) {
            			if(!v_list.elementAt(j).toString().equals("")) {
            				v_text += v_list.elementAt(j).toString() + "\n";
            			}
            		}
            	} else {
            		v_text = box.getString("p_" + v_examnums.elementAt(i).toString());
            	}
            	index = 1;
            	pstmt.setInt(index++, v_choice);
            	pstmt.setCharacterStream(index++, new StringReader(v_text), v_text.length());
            	pstmt.setString(index++, box.getSession("userid"));
                pstmt.setString(index++, box.getString("p_subj"));
                pstmt.setString(index++, box.getString("p_year"));
                pstmt.setString(index++, box.getString("p_subjseq"));
                pstmt.setInt(index++, box.getInt("p_month"));
                pstmt.setInt(index++, box.getInt("p_bookcode"));
                pstmt.setString(index++, box.getSession("userid"));
                pstmt.setInt(index++, Integer.parseInt(v_examnums.elementAt(i).toString()));
                isOk += pstmt.executeUpdate();
            }
            
            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 도서별 평가문제 정보
	 * @param box receive from the form object and session
	 * @return DataBox 도서별 평가문제 정보
     **/
    public DataBox selectBookExamPaperUserInfo(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		dbox = new DataBox("");
    		
            sbSQL.append("SELECT A.SUBJ  \n");
            sbSQL.append("     , A.YEAR  \n");
            sbSQL.append("     , A.SUBJSEQ  \n");
            sbSQL.append("     , A.MONTH  \n");
            sbSQL.append("     , A.BOOKCODE  \n");
            sbSQL.append("     , B.BOOKNAME  \n");
            sbSQL.append("     , A.TOTALSCORE  \n");
            sbSQL.append("     , A.OXTYPE_CNT  \n");
            sbSQL.append("     , A.CHOICE_CNT  \n");
            sbSQL.append("     , A.WORDS_CNT  \n");
            sbSQL.append("     , A.LIST_CNT  \n");
            sbSQL.append("     , A.SHORT_CNT  \n");
            sbSQL.append("     , A.DESCRIPT_CNT  \n");
            sbSQL.append("     , A.OXTYPE_CNT + A.CHOICE_CNT + A.WORDS_CNT + A.LIST_CNT + A.SHORT_CNT + A.DESCRIPT_CNT AS TOTAL_CNT  \n");
            sbSQL.append("     , C.TOTALSCORE AS USERSCORE  \n");
            sbSQL.append("     , C.REAL_FILENAME  \n");
            sbSQL.append("     , C.SAVE_FILENAME  \n");
            sbSQL.append("FROM   TZ_BOOKEXAM_PAPER A  \n");
            sbSQL.append("     , TZ_BOOKINFO B  \n");
            sbSQL.append("     , TZ_BOOKEXAM_RESULT C  \n");
            sbSQL.append("WHERE  A.BOOKCODE = B.BOOKCODE  \n");
            sbSQL.append("AND    A.SUBJ = C.SUBJ  \n");
            sbSQL.append("AND    A.YEAR = C.YEAR  \n");
            sbSQL.append("AND    A.SUBJSEQ = C.SUBJSEQ  \n");
            sbSQL.append("AND    A.MONTH = C.MONTH  \n");
            sbSQL.append("AND    A.BOOKCODE = C.BOOKCODE  \n");
            sbSQL.append("AND    A.SUBJ = " + SQLString.Format(box.getString("p_subj")) + "  \n");
            sbSQL.append("AND    A.YEAR = " + SQLString.Format(box.getString("p_year")) + "  \n");
            sbSQL.append("AND    A.SUBJSEQ = " + SQLString.Format(box.getString("p_subjseq")) + "  \n");
            sbSQL.append("AND    A.MONTH = " + SQLString.Format(box.getInt("p_month")) + "  \n");
            sbSQL.append("AND    A.BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
            sbSQL.append("AND    C.USERID = " + SQLString.Format(box.getSession("userid")) + "  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return dbox;
    }
    
    /**
	 * 도서별 평가 마스터 목록
	 * @param box          receive from the form object and session
	 * @return ArrayList   도서별 평가 마스터 목록
     **/
    public ArrayList selectBookExamPaperUserList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT A.SUBJ  \n");
            sbSQL.append("     , A.YEAR  \n");
            sbSQL.append("     , A.SUBJSEQ  \n");
            sbSQL.append("     , A.MONTH  \n");
            sbSQL.append("     , A.BOOKCODE  \n");
            sbSQL.append("     , C.BOOKNAME  \n");
            sbSQL.append("     , B.EXAMSTART  \n");
            sbSQL.append("     , B.EXAMEND  \n");
            sbSQL.append("     , D.APPLYEND  \n");
            sbSQL.append("     , D.FINAL_YN  \n");
            sbSQL.append("     , D.MARKING_YN  \n");
            sbSQL.append("     , B.TOTALSCORE  \n");
            sbSQL.append("     , D.TOTALSCORE AS USERSCORE  \n");
            sbSQL.append("     , B.OXTYPE_CNT + B.CHOICE_CNT + B.WORDS_CNT + B.LIST_CNT + B.SHORT_CNT + B.DESCRIPT_CNT AS TOTALCNT  \n");
            sbSQL.append("     , D.USERID  \n");
            sbSQL.append("     , CASE WHEN TRUNC(SYSDATE) BETWEEN TO_DATE(B.EXAMSTART,'YYYYMMDD') AND TO_DATE(B.EXAMEND,'YYYYMMDD') + B.EXTRA_DAYS THEN 'Y'   \n");
            sbSQL.append("            ELSE 'N'  \n");
            sbSQL.append("       END AS ISPERIOD  \n");
            sbSQL.append("FROM   TZ_PROPOSEBOOK A  \n");
            sbSQL.append("     , TZ_BOOKEXAM_PAPER B  \n");
            sbSQL.append("     , TZ_BOOKINFO C  \n");
            sbSQL.append("     , TZ_BOOKEXAM_RESULT D  \n");
            sbSQL.append("WHERE  A.SUBJ = B.SUBJ  \n");
            sbSQL.append("AND    A.YEAR = B.YEAR  \n");
            sbSQL.append("AND    A.SUBJSEQ = B.SUBJSEQ  \n");
            sbSQL.append("AND    A.MONTH = B.MONTH  \n");
            sbSQL.append("AND    A.BOOKCODE = B.BOOKCODE  \n");
            sbSQL.append("AND    A.BOOKCODE = C.BOOKCODE  \n");
            sbSQL.append("AND    A.SUBJ = D.SUBJ (+)  \n");
            sbSQL.append("AND    A.YEAR = D.YEAR (+)  \n");
            sbSQL.append("AND    A.SUBJSEQ = D.SUBJSEQ (+)  \n");
            sbSQL.append("AND    A.MONTH = D.MONTH (+)  \n");
            sbSQL.append("AND    A.BOOKCODE = D.BOOKCODE (+)  \n");
            sbSQL.append("AND    A.USERID = D.USERID (+)  \n");
            sbSQL.append("AND    A.SUBJ = " + SQLString.Format(box.getString("p_subj")) + "  \n");
            sbSQL.append("AND    A.YEAR = " + SQLString.Format(box.getString("p_year")) + "  \n");
            sbSQL.append("AND    A.SUBJSEQ = " + SQLString.Format(box.getString("p_subjseq")) + "  \n");
            sbSQL.append("AND    A.USERID = " + SQLString.Format(box.getSession("userid")) + "  \n");
            sbSQL.append("ORDER BY MONTH, BOOKCODE  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }
    
    /**
	 * 도서별 평가 결과 조회 목록
	 * @param box          receive from the form object and session
	 * @return ArrayList   도서별 평가 마스터 목록
     **/
    public ArrayList selectBookExamResultList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT Z.SUBJ  \n");
            sbSQL.append("     , Z.YEAR  \n");
            sbSQL.append("     , Z.SUBJSEQ  \n");
            sbSQL.append("     , Z.MONTH  \n");
            sbSQL.append("     , Z.BOOKCODE  \n");
            sbSQL.append("     , Z.SUBJNM  \n");
            sbSQL.append("     , Z.USERID  \n");
            sbSQL.append("     , Z.NAME  \n");
            sbSQL.append("     , Z.POSITION_NM  \n");
            sbSQL.append("     , Z.BOOKNAME  \n");
            sbSQL.append("     , Z.MONTHCNT  \n");
            sbSQL.append("     , Y.USERID AS RESULTID  \n");
            sbSQL.append("     , Y.APPLYEND  \n");
            sbSQL.append("     , Y.FINAL_YN  \n");
            sbSQL.append("     , Y.MARKING_YN  \n");
            sbSQL.append("     , Y.TOTALSCORE  \n");
            sbSQL.append("FROM  (SELECT A.SUBJ  \n");
            sbSQL.append("            , A.YEAR  \n");
            sbSQL.append("            , A.SUBJSEQ  \n");
            sbSQL.append("            , B.MONTH  \n");
            sbSQL.append("            , B.BOOKCODE  \n");
            sbSQL.append("            , C.SUBJNM  \n");
            sbSQL.append("            , A.USERID  \n");
            sbSQL.append("            , D.NAME  \n");
            sbSQL.append("            , D.POSITION_NM  \n");
            sbSQL.append("            , E.BOOKNAME  \n");
            sbSQL.append("            ,(SELECT COUNT(DISTINCT MONTH)  \n");
            sbSQL.append("              FROM   TZ_SUBJBOOK  \n");
            sbSQL.append("              WHERE  SUBJ = A.SUBJ) AS MONTHCNT  \n");
            sbSQL.append("       FROM   TZ_STUDENT A  \n");
            sbSQL.append("            , TZ_PROPOSEBOOK B  \n");
            sbSQL.append("            , TZ_SUBJSEQ C  \n");
            sbSQL.append("            , TZ_MEMBER D  \n");
            sbSQL.append("            , TZ_BOOKINFO E  \n");
            sbSQL.append("       WHERE  A.SUBJ = B.SUBJ  \n");
            sbSQL.append("       AND    A.YEAR = B.YEAR  \n");
            sbSQL.append("       AND    A.SUBJSEQ = B.SUBJSEQ  \n");
            sbSQL.append("       AND    A.USERID = B.USERID  \n");
            sbSQL.append("       AND    A.SUBJ = C.SUBJ  \n");
            sbSQL.append("       AND    A.YEAR = C.YEAR  \n");
            sbSQL.append("       AND    A.SUBJSEQ = C.SUBJSEQ  \n");
            sbSQL.append("       AND    A.USERID = D.USERID  \n");
            sbSQL.append("       AND    B.BOOKCODE = E.BOOKCODE  \n");
            sbSQL.append("       AND    A.SUBJ = " + SQLString.Format(box.getString("s_subjcourse")) + "  \n");
            sbSQL.append("       AND    A.YEAR = " + SQLString.Format(box.getString("s_gyear")) + "  \n");
            sbSQL.append("       AND    A.SUBJSEQ = " + SQLString.Format(box.getString("s_subjseq")) + "  \n");
            sbSQL.append("      ) Z  \n");
            sbSQL.append("    ,  TZ_BOOKEXAM_RESULT Y  \n");
            sbSQL.append("WHERE  Z.SUBJ = Y.SUBJ (+)  \n");
            sbSQL.append("AND    Z.YEAR = Y.YEAR (+)  \n");
            sbSQL.append("AND    Z.SUBJSEQ = Y.SUBJSEQ (+)  \n");
            sbSQL.append("AND    Z.MONTH = Y.MONTH (+)  \n");
            sbSQL.append("AND    Z.BOOKCODE = Y.BOOKCODE (+)  \n");
            sbSQL.append("AND    Z.USERID = Y.USERID (+)  \n");
            sbSQL.append("ORDER BY Z.USERID, Z.MONTH  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }
    
    /**
	 * 도서별 평가 결과 조회 목록
	 * @param box          receive from the form object and session
	 * @return ArrayList   도서별 평가 마스터 목록
     **/
    public ArrayList selectBookExamUserResultList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT A.SUBJ  \n");
            sbSQL.append("     , A.YEAR  \n");
            sbSQL.append("     , A.SUBJSEQ  \n");
            sbSQL.append("     , A.MONTH  \n");
            sbSQL.append("     , A.BOOKCODE  \n");
            sbSQL.append("     , A.USERID  \n");
            sbSQL.append("     , A.EXAMNUM  \n");
            sbSQL.append("     , A.SCORE  \n");
            sbSQL.append("     , A.CHOICE_ANSWER  \n");
            sbSQL.append("     , A.TEXT_ANSWER  \n");
            sbSQL.append("     , A.CORRECTION  \n");
            sbSQL.append("     , A.EXAMEXP_YN  \n");
            sbSQL.append("     , B.EXAMTYPE  \n");
            sbSQL.append("     , B.EXAMTEXT  \n");
            sbSQL.append("     , B.EXAMCONTS  \n");
            sbSQL.append("     , B.EXAMEXP  \n");
            sbSQL.append("     , B.LISTTYPECNT  \n");
            sbSQL.append("     , C.SELNUM  \n");
            sbSQL.append("     , C.SELTEXT  \n");
            sbSQL.append("     , C.ANSWER_YN  \n");
            sbSQL.append("     , C.REAL_IMAGEFILE  \n");
            sbSQL.append("     , C.SAVE_IMAGEFILE  \n");
            sbSQL.append("     , DECODE(B.EXAMTYPE, 1, D.OXTYPE_SCORE, 2, D.CHOICE_SCORE, 3, D.WORDS_SCORE, 4, LIST_SCORE, 5, SHORT_SCORE, 6, DESCRIPT_SCORE) AS PAPER_SCORE  \n");
            sbSQL.append("     , NVL((SELECT 'Y'  \n");
            sbSQL.append("            FROM   TZ_BOOKEXAMSEL  \n");
            sbSQL.append("            WHERE  BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("            AND    EXAMNUM = A.EXAMNUM  \n");
            sbSQL.append("            AND    SELNUM = A.CHOICE_ANSWER  \n");
            sbSQL.append("            AND    ANSWER_YN = 'Y'), 'N') AS CORRECT_YN  \n");
            sbSQL.append("     , E.MARKING_YN  \n");
            sbSQL.append("FROM   TZ_BOOKEXAMEACH_RESULT A  \n");
            sbSQL.append("     , TZ_BOOKEXAM B  \n");
            sbSQL.append("     , TZ_BOOKEXAMSEL C  \n");
            sbSQL.append("     , TZ_BOOKEXAM_PAPER D  \n");
            sbSQL.append("     , TZ_BOOKEXAM_RESULT E  \n");
            sbSQL.append("WHERE  A.BOOKCODE = B.BOOKCODE  \n");
            sbSQL.append("AND    A.EXAMNUM = B.EXAMNUM  \n");
            sbSQL.append("AND    A.BOOKCODE = C.BOOKCODE (+)  \n");
            sbSQL.append("AND    A.EXAMNUM = C.EXAMNUM (+)  \n");
            sbSQL.append("AND    A.SUBJ = D.SUBJ  \n");
            sbSQL.append("AND    A.YEAR = D.YEAR  \n");
            sbSQL.append("AND    A.SUBJSEQ = D.SUBJSEQ  \n");
            sbSQL.append("AND    A.MONTH = D.MONTH  \n");
            sbSQL.append("AND    A.BOOKCODE = D.BOOKCODE  \n");
            sbSQL.append("AND    A.SUBJ = E.SUBJ  \n");
            sbSQL.append("AND    A.YEAR = E.YEAR  \n");
            sbSQL.append("AND    A.SUBJSEQ = E.SUBJSEQ  \n");
            sbSQL.append("AND    A.MONTH = E.MONTH  \n");
            sbSQL.append("AND    A.BOOKCODE = E.BOOKCODE  \n");
            sbSQL.append("AND    A.USERID = E.USERID  \n");
            sbSQL.append("AND    A.SUBJ = " + SQLString.Format(box.getString("p_subj")) + "  \n");
            sbSQL.append("AND    A.YEAR = " + SQLString.Format(box.getString("p_year")) + "  \n");
            sbSQL.append("AND    A.SUBJSEQ = " + SQLString.Format(box.getString("p_subjseq")) + "  \n");
            sbSQL.append("AND    A.MONTH = " + SQLString.Format(box.getInt("p_month")) + "  \n");
            sbSQL.append("AND    A.BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
            sbSQL.append("AND    A.USERID = " + SQLString.Format(box.getString("p_userid")) + "  \n");
            sbSQL.append("ORDER BY A.ORDERS, SELNUM  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }
    
    /**
	 * 도서별 평가문제 정보
	 * @param box receive from the form object and session
	 * @return DataBox 도서별 평가문제 정보
     **/
    public DataBox selectBookExamUserResultInfo(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		dbox = new DataBox("");
    		
            sbSQL.append("SELECT A.SUBJ  \n");
            sbSQL.append("     , A.YEAR  \n");
            sbSQL.append("     , A.SUBJSEQ  \n");
            sbSQL.append("     , A.MONTH  \n");
            sbSQL.append("     , A.BOOKCODE  \n");
            sbSQL.append("     , A.USERID  \n");
            sbSQL.append("     , A.APPLYEND  \n");
            sbSQL.append("     , A.EXTRA_YN  \n");
            sbSQL.append("     , A.FINAL_YN  \n");
            sbSQL.append("     , A.REAL_FILENAME  \n");
            sbSQL.append("     , A.SAVE_FILENAME  \n");
            sbSQL.append("     , A.TOTALSCORE  \n");
            sbSQL.append("     , B.BOOKNAME  \n");
            sbSQL.append("     , C.SUBJNM  \n");
            sbSQL.append("     , C.ISCLOSED  \n");
            sbSQL.append("FROM   TZ_BOOKEXAM_RESULT A  \n");
            sbSQL.append("     , TZ_BOOKINFO B  \n");
            sbSQL.append("     , TZ_SUBJSEQ C  \n");
            sbSQL.append("WHERE  A.BOOKCODE = B.BOOKCODE  \n");
            sbSQL.append("AND    A.SUBJ = C.SUBJ  \n");
            sbSQL.append("AND    A.YEAR = C.YEAR  \n");
            sbSQL.append("AND    A.SUBJSEQ = C.SUBJSEQ  \n");
            sbSQL.append("AND    A.SUBJ = " + SQLString.Format(box.getString("p_subj")) + "  \n");
            sbSQL.append("AND    A.YEAR = " + SQLString.Format(box.getString("p_year")) + "  \n");
            sbSQL.append("AND    A.SUBJSEQ = " + SQLString.Format(box.getString("p_subjseq")) + "  \n");
            sbSQL.append("AND    A.MONTH = " + SQLString.Format(box.getInt("p_month")) + "  \n");
            sbSQL.append("AND    A.BOOKCODE = " + SQLString.Format(box.getInt("p_bookcode")) + "  \n");
            sbSQL.append("AND    A.USERID = " + SQLString.Format(box.getString("p_userid")) + "  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return dbox;
    }
    
    /**
	 * 도서별 평가 채점
	 * @param box receive from the form object and session
	 * @return int 결과
     **/
    public int updateBookExamResultMarking(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        int index = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_BOOKEXAMEACH_RESULT  \n");
            sbSQL.append("SET    SCORE = ?  \n");
            sbSQL.append("     , EXAMEXP_YN = ?  \n");
            sbSQL.append("     , CORRECTION = ?  \n");
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("WHERE  SUBJ = ?  \n");
            sbSQL.append("AND    YEAR = ?  \n");
            sbSQL.append("AND    SUBJSEQ = ?  \n");
            sbSQL.append("AND    MONTH = ?  \n");
            sbSQL.append("AND    BOOKCODE = ?  \n");
            sbSQL.append("AND    USERID = ?  \n");
            sbSQL.append("AND    EXAMNUM = ?  \n");
            pstmt = connMgr.prepareStatement(sbSQL.toString());
            
            Vector v_examnums = box.getVector("p_examnum");
            Vector v_scores = box.getVector("p_score");
            Vector v_corrections = box.getVector("p_correction");
            double totalscore = 0.00;
            
            for(int i = 0 ; i < v_examnums.size() ; i++) {
            	index = 1;
            	pstmt.setString(index++, v_scores.elementAt(i).toString());
            	pstmt.setString(index++, box.getStringDefault("p_examexp_yn" + v_examnums.elementAt(i).toString(), "N"));
            	pstmt.setCharacterStream(index++, new StringReader(v_corrections.elementAt(i).toString()), v_corrections.elementAt(i).toString().length());
            	pstmt.setString(index++, box.getSession("userid"));
                pstmt.setString(index++, box.getString("p_subj"));
                pstmt.setString(index++, box.getString("p_year"));
                pstmt.setString(index++, box.getString("p_subjseq"));
                pstmt.setInt(index++, box.getInt("p_month"));
                pstmt.setInt(index++, box.getInt("p_bookcode"));
                pstmt.setString(index++, box.getString("p_userid"));
                pstmt.setInt(index++, Integer.parseInt(v_examnums.elementAt(i).toString()));
                isOk += pstmt.executeUpdate();
                totalscore += Double.parseDouble(v_scores.elementAt(i).toString());
            }
            
            sbSQL.setLength(0);
            sbSQL.append("UPDATE TZ_BOOKEXAM_RESULT A  \n");
            sbSQL.append("SET    TOTALSCORE = (SELECT ? - DECODE(A.EXTRA_YN, 'N', 0  \n");
            sbSQL.append("                                                  ,(EXTRA_MINUS  \n");
            sbSQL.append("                                                    * (TO_DATE(SUBSTR(A.APPLYEND, 1, 8), 'YYYYMMDD')  \n");
            sbSQL.append("                                                       - TO_DATE(EXAMEND, 'YYYYMMDD')  \n");
            sbSQL.append("                                                      )  \n");
            sbSQL.append("                                                   )  \n");
            sbSQL.append("                                       )  \n");
            sbSQL.append("                     FROM   TZ_BOOKEXAM_PAPER  \n");
            sbSQL.append("                     WHERE  SUBJ = A.SUBJ  \n");
            sbSQL.append("                     AND    YEAR = A.YEAR  \n");
            sbSQL.append("                     AND    SUBJSEQ = A.SUBJSEQ  \n");
            sbSQL.append("                     AND    MONTH = A.MONTH  \n");
            sbSQL.append("                     AND    BOOKCODE = A.BOOKCODE  \n");
            sbSQL.append("                    )  \n");
            sbSQL.append("     , MARKING_YN = 'Y'  \n");
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("WHERE  SUBJ = ?  \n");
            sbSQL.append("AND    YEAR = ?  \n");
            sbSQL.append("AND    SUBJSEQ = ?  \n");
            sbSQL.append("AND    MONTH = ?  \n");
            sbSQL.append("AND    BOOKCODE = ?  \n");
            sbSQL.append("AND    USERID = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            index = 1;
            pstmt.setDouble(index++, totalscore);
        	pstmt.setString(index++, box.getSession("userid"));
            pstmt.setString(index++, box.getString("p_subj"));
            pstmt.setString(index++, box.getString("p_year"));
            pstmt.setString(index++, box.getString("p_subjseq"));
            pstmt.setInt(index++, box.getInt("p_month"));
            pstmt.setInt(index++, box.getInt("p_bookcode"));
            pstmt.setString(index++, box.getString("p_userid"));
            isOk += pstmt.executeUpdate();
            
            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 도서별 평가마스터 삭제
	 * @param box receive from the form object and session
	 * @return int 결과
     **/
    public int updateBookExamUserStatus(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_BOOKEXAM_RESULT  \n");
            sbSQL.append("SET    FINAL_YN = ?  \n");
            if(box.getString("p_final_yn").equals("Y")) {
            	sbSQL.append("     , APPLYEND = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            } else {
            	sbSQL.append("     , APPLYEND = ''  \n");
            }
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("WHERE  SUBJ = ?  \n");
            sbSQL.append("AND    YEAR = ?  \n");
            sbSQL.append("AND    SUBJSEQ = ?  \n");
            sbSQL.append("AND    MONTH = ?  \n");
            sbSQL.append("AND    BOOKCODE = ?  \n");
            sbSQL.append("AND    USERID = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getString("p_final_yn"));
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setString(index++, box.getString("p_subj"));
            pstmt.setString(index++, box.getString("p_year"));
            pstmt.setString(index++, box.getString("p_subjseq"));
            pstmt.setInt(index++, box.getInt("p_month"));
            pstmt.setInt(index++, box.getInt("p_bookcode"));
            pstmt.setString(index++, box.getString("p_userid"));
            isOk = pstmt.executeUpdate();

            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
}