package com.ziaan.knowledgeucc;

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

public class KnowledgeUCCBean {
	
	public final String CATEGORY_CODE = "0092";
	private ConfigSet config;
	private int row_movie;
	private int row_normal;
	
    public KnowledgeUCCBean() {
        try { 
            config = new ConfigSet();
            row_movie = Integer.parseInt(config.getProperty("page.movieucc.row")); // 이 모듈의 페이지당 row 수를 셋팅한다.
            row_normal = Integer.parseInt(config.getProperty("page.normalucc.row")); // 이 모듈의 페이지당 row 수를 셋팅한다.
        } catch(Exception e) { 
            e.printStackTrace();
        }
    }
    
    /**
	 * 지식 UCC 리스트 조회
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/
    public ArrayList selectKnowledgeUCCList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	int v_pageno = box.getInt("p_pageno");
    	String v_search_type = box.getStringDefault("p_search_type", "titleanddesc");
    	String v_search_text = box.getString("p_search_text");
    	String v_category = box.getStringDefault("s_category", "ALL");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT A.CONTENTKEY  \n");
            sbSQL.append("     , A.CATEGORY  \n");
            sbSQL.append("     , GET_CODENM(" + SQLString.Format(CATEGORY_CODE) + ", A.CATEGORY) AS CATEGORYNM  \n");
            sbSQL.append("     , A.TITLE  \n");
            sbSQL.append("     , A.CONTENTSUM  \n");
            sbSQL.append("     , A.TAG  \n");
            sbSQL.append("     , A.READCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_KNOWLEDGEUCC_RECOMMEND  \n");
            sbSQL.append("       WHERE  CONTENTKEY = A.CONTENTKEY) AS RECOMCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_KNOWLEDGEUCC_COMMENT  \n");
            sbSQL.append("       WHERE  CONTENTKEY = A.CONTENTKEY  \n");
            sbSQL.append("       AND    ISDELETED != 'Y') AS COMMENTCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_KNOWLEDGEUCC_INTEREST  \n");
            sbSQL.append("       WHERE  CONTENTKEY = A.CONTENTKEY) AS INTERESTCNT  \n");
            sbSQL.append("     , A.CONTENTURL  \n");
            sbSQL.append("     , A.GROUPCODE  \n");
            sbSQL.append("     , A.IDATE  \n");
            sbSQL.append("     , A.IUSERID  \n");
            sbSQL.append("     , B.NAME  \n");
    		sbSQL.append("FROM   TZ_KNOWLEDGEUCC A  \n");
    		sbSQL.append("     , TZ_MEMBER B  \n");
    		sbSQL.append("WHERE  A.IUSERID = B.USERID  \n");
    		sbSQL.append("AND    A.ISDELETED != 'Y'  \n");
    		if(box.getString("p_ismovie").equals("Y")) {
	    		sbSQL.append("AND    A.ISMOVIE = 'Y'  \n");
	    		sbSQL.append("AND    A.CONTENTKEY IN (SELECT MAX(CONTENTKEY)  \n");
	    		sbSQL.append("                        FROM   TZ_KNOWLEDGEUCC  \n");
	    		sbSQL.append("                        WHERE  ISDELETED != 'Y'  \n");
	    		sbSQL.append("                        AND    ISMOVIE = 'Y'  \n");
	    		sbSQL.append("                        GROUP BY GROUPCODE)  \n");
    		} else {
	    		sbSQL.append("AND    A.ISMOVIE = 'N'  \n");
    		}
            sbSQL.append("AND    A.CATEGORY = CASE WHEN 'ALL' = " + SQLString.Format(v_category) + " THEN A.CATEGORY  \n");
            sbSQL.append("                         ELSE " + SQLString.Format(v_category) + "  \n");
            sbSQL.append("                    END  \n");
    		sbSQL.append("AND    CASE WHEN " + SQLString.Format(v_search_type) + " = 'name' THEN B.NAME  \n");
    		sbSQL.append("            ELSE A.TITLE || A.CONTENTDESC  \n");
    		sbSQL.append("       END LIKE '%' || " + SQLString.Format(v_search_text) + " || '%'  \n");
    		sbSQL.append("ORDER BY A.IDATE DESC  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
            ls.setPageSize(box.getString("p_ismovie").equals("Y") ? row_movie : row_normal);	// 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);               // 현재페이지번호를 세팅한다.
            int totalpagecount = ls.getTotalPage();    // 전체 페이지 수를 반환한다
            int totalrowcount = ls.getTotalCount();    // 전체 row 수를 반환한다
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
                dbox.put("d_dispnum",   new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(box.getString("p_ismovie").equals("Y") ? row_movie : row_normal));
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
	 * 지식 UCC(동영상) 동영상 UCC Count
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/
    public static String selectMovieUCCCnt(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	String result = "";
    	StringBuffer sbSQL = null;
    	
    	String v_search_type = box.getStringDefault("p_search_type", "titleanddesc");
    	String v_search_text = box.getString("p_search_text");
    	String v_category = box.getStringDefault("s_category", "ALL");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT COUNT(DISTINCT GROUPCODE) AS MOVIECNT  \n");
    		sbSQL.append("FROM   TZ_KNOWLEDGEUCC A  \n");
    		sbSQL.append("     , TZ_MEMBER B  \n");
    		sbSQL.append("WHERE  A.IUSERID = B.USERID  \n");
    		sbSQL.append("AND    A.ISDELETED != 'Y'  \n");
    		sbSQL.append("AND    A.ISMOVIE = 'Y'  \n");
            sbSQL.append("AND    A.CATEGORY = CASE WHEN 'ALL' = " + SQLString.Format(v_category) + " THEN A.CATEGORY  \n");
            sbSQL.append("                         ELSE " + SQLString.Format(v_category) + "  \n");
            sbSQL.append("                    END  \n");
    		sbSQL.append("AND    CASE WHEN " + SQLString.Format(v_search_type) + " = 'name' THEN B.NAME  \n");
    		sbSQL.append("            ELSE A.TITLE || A.CONTENTDESC  \n");
    		sbSQL.append("       END LIKE '%' || " + SQLString.Format(v_search_text) + " || '%'  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			result = ls.getString("moviecnt");
    		}
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
	 * 지식 UCC(일반) 일반 UCC Count
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/
    public static String selectNormalUCCCnt(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	String result = "";
    	StringBuffer sbSQL = null;
    	
    	String v_search_type = box.getStringDefault("p_search_type", "titleanddesc");
    	String v_search_text = box.getString("p_search_text");
    	String v_category = box.getStringDefault("s_category", "ALL");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT COUNT(DISTINCT CONTENTKEY) AS NORMALCNT  \n");
    		sbSQL.append("FROM   TZ_KNOWLEDGEUCC A  \n");
    		sbSQL.append("     , TZ_MEMBER B  \n");
    		sbSQL.append("WHERE  A.IUSERID = B.USERID  \n");
    		sbSQL.append("AND    A.ISDELETED != 'Y'  \n");
    		sbSQL.append("AND    A.ISMOVIE != 'Y'  \n");
            sbSQL.append("AND    A.CATEGORY = CASE WHEN 'ALL' = " + SQLString.Format(v_category) + " THEN A.CATEGORY  \n");
            sbSQL.append("                         ELSE " + SQLString.Format(v_category) + "  \n");
            sbSQL.append("                    END  \n");
    		sbSQL.append("AND    CASE WHEN " + SQLString.Format(v_search_type) + " = 'name' THEN B.NAME  \n");
    		sbSQL.append("            ELSE A.TITLE || A.CONTENTDESC  \n");
    		sbSQL.append("       END LIKE '%' || " + SQLString.Format(v_search_text) + " || '%'  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			result = ls.getString("normalcnt");
    		}
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
	 * 지식 UCC 카테고리 리스트 조회
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/
    public ArrayList selectKnowledgeUCCCategoryList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT GUBUN  \n");
            sbSQL.append("     , CODE  \n");
            sbSQL.append("     , CODENM  \n");
            sbSQL.append("FROM   TZ_CODE  \n");
            sbSQL.append("WHERE  GUBUN = " + SQLString.Format(CATEGORY_CODE) + "  \n");
            sbSQL.append("ORDER BY CODE  \n");
    		
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
	 * 지식 UCC 새로운 그룹코드 조회
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/
    public String selectNewGroupCode(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	String result = "";
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT NVL(MAX(GROUPCODE), 0) + 1 AS GROUPCODE  \n");
            sbSQL.append("FROM   TZ_KNOWLEDGEUCC  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			result = ls.getString("groupcode");
    		}
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
	 * 지식 UCC 등록
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/
    public int insertKnowledgeUCCInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("INSERT INTO TZ_KNOWLEDGEUCC  \n");
            sbSQL.append("      (CONTENTKEY  \n");
            sbSQL.append("     , CATEGORY  \n");
            sbSQL.append("     , ISMOVIE  \n");
            sbSQL.append("     , CONTENTTYPE  \n");
            sbSQL.append("     , TITLE  \n");
            sbSQL.append("     , CONTENTSUM  \n");
            sbSQL.append("     , CONTENTDESC  \n");
            sbSQL.append("     , TAG  \n");
            sbSQL.append("     , READCNT  \n");
            sbSQL.append("     , GROUPCODE  \n");
            sbSQL.append("     , CONTENTURL  \n");
            sbSQL.append("     , REALFILE  \n");
            sbSQL.append("     , SAVEFILE  \n");
            sbSQL.append("     , ISDELETED  \n");
            sbSQL.append("     , IUSERID  \n");
            sbSQL.append("     , IDATE  \n");
            sbSQL.append("     , IUSERIP)  \n");
            sbSQL.append("VALUES(?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , 0  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , 'N'  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("     , ?)  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getString("p_contentkey"));
            pstmt.setString(index++, box.getString("p_category"));
            pstmt.setString(index++, box.getString("p_ismovie"));
            pstmt.setString(index++, box.getString("p_ismovie").equals("Y") ? "Movie" : "Normal");
            pstmt.setString(index++, box.getString("p_title"));
            pstmt.setString(index++, box.getString("p_contentsum"));
            pstmt.setString(index++, box.getString("p_contentdesc"));
            pstmt.setString(index++, box.getString("p_tag"));
            pstmt.setString(index++, box.getString("p_groupcode"));
            pstmt.setString(index++, config.getProperty("dir.movieuccpath") + box.getString("p_contentkey"));
            pstmt.setString(index++, box.getRealFileName("p_file"));
            pstmt.setString(index++, box.getNewFileName("p_file"));
            pstmt.setString(index++, box.getSession("userid").equals("") ? box.getString("p_userid") : box.getSession("userid"));
            pstmt.setString(index++, box.getSession("userip"));
            isOk = pstmt.executeUpdate();

            if(isOk > 0) { 
            	connMgr.commit();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 지식 UCC 정보보기
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/
    public DataBox selectKnowledgeUCCInfo(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		dbox = new DataBox("");
    		
            sbSQL.append("SELECT A.CONTENTKEY  \n");
            sbSQL.append("     , A.CATEGORY  \n");
            sbSQL.append("     , GET_CODENM(" + SQLString.Format(CATEGORY_CODE) + ", A.CATEGORY) AS CATEGORYNM  \n");
            sbSQL.append("     , A.TITLE  \n");
            sbSQL.append("     , A.CONTENTSUM  \n");
            sbSQL.append("     , A.CONTENTDESC  \n");
            sbSQL.append("     , A.TAG  \n");
            sbSQL.append("     , A.READCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_KNOWLEDGEUCC_RECOMMEND  \n");
            sbSQL.append("       WHERE  CONTENTKEY = A.CONTENTKEY) AS RECOMCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_KNOWLEDGEUCC_COMMENT  \n");
            sbSQL.append("       WHERE  CONTENTKEY = A.CONTENTKEY  \n");
            sbSQL.append("       AND    ISDELETED != 'Y') AS COMMENTCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_KNOWLEDGEUCC_INTEREST  \n");
            sbSQL.append("       WHERE  CONTENTKEY = A.CONTENTKEY) AS INTERESTCNT  \n");
            sbSQL.append("     , A.GROUPCODE  \n");
            sbSQL.append("     , A.CONTENTURL  \n");
            sbSQL.append("     , A.REALFILE  \n");
            sbSQL.append("     , A.SAVEFILE  \n");
            sbSQL.append("     , A.IUSERID  \n");
            sbSQL.append("     , B.NAME  \n");
            sbSQL.append("     , A.IDATE  \n");
            sbSQL.append("FROM   TZ_KNOWLEDGEUCC A  \n");
            sbSQL.append("     , TZ_MEMBER B  \n");
            sbSQL.append("WHERE  A.IUSERID = B.USERID  \n");
            sbSQL.append("AND    CONTENTKEY = " + SQLString.Format(box.getStringDefault("p_contentkey", box.getString("p2"))) + "  \n");
    		
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
	 * 지식 UCC 댓글 리스트
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/    
    public ArrayList selectKnowledgeUCCCommentList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT A.CONTENTKEY  \n");
            sbSQL.append("     , A.COMMENTSEQ  \n");
            sbSQL.append("     , A.CONTENTPOINT  \n");
            sbSQL.append("     , A.COMMENTS  \n");
            sbSQL.append("     , A.LUSERID  \n");
            sbSQL.append("     , B.NAME  \n");
            sbSQL.append("     , A.LDATE  \n");
            sbSQL.append("FROM   TZ_KNOWLEDGEUCC_COMMENT A  \n");
            sbSQL.append("     , TZ_MEMBER B  \n");
            sbSQL.append("WHERE  A.LUSERID = B.USERID  \n");
            sbSQL.append("AND    A.ISDELETED != 'Y'  \n");
            sbSQL.append("AND    A.CONTENTKEY = " + SQLString.Format(box.getString("p_contentkey")) + "  \n");
            sbSQL.append("ORDER BY A.LDATE DESC  \n");
    		
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
	 * 지식 UCC(동영상) 편집 히스토리 리스트
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/
    public ArrayList selectKnowledgeUCCHistoryList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT A.CONTENTKEY  \n");
            sbSQL.append("     , A.CATEGORY  \n");
            sbSQL.append("     , GET_CODENM(" + SQLString.Format(CATEGORY_CODE) + ", A.CATEGORY) AS CATEGORYNM  \n");
            sbSQL.append("     , A.TITLE  \n");
            sbSQL.append("     , A.CONTENTSUM  \n");
            sbSQL.append("     , A.TAG  \n");
            sbSQL.append("     , A.READCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_KNOWLEDGEUCC_RECOMMEND  \n");
            sbSQL.append("       WHERE  CONTENTKEY = A.CONTENTKEY) AS RECOMCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_KNOWLEDGEUCC_COMMENT  \n");
            sbSQL.append("       WHERE  CONTENTKEY = A.CONTENTKEY  \n");
            sbSQL.append("       AND    ISDELETED != 'Y') AS COMMENTCNT  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_KNOWLEDGEUCC_INTEREST  \n");
            sbSQL.append("       WHERE  CONTENTKEY = A.CONTENTKEY) AS INTERESTCNT  \n");
            sbSQL.append("     , A.CONTENTURL  \n");
            sbSQL.append("     , A.GROUPCODE  \n");
            sbSQL.append("     , A.IDATE  \n");
            sbSQL.append("     , A.IUSERID  \n");
            sbSQL.append("     , B.NAME  \n");
            sbSQL.append("FROM   TZ_KNOWLEDGEUCC A  \n");
            sbSQL.append("     , TZ_MEMBER B  \n");
            sbSQL.append("WHERE  A.IUSERID = B.USERID  \n");
            sbSQL.append("AND    A.ISDELETED != 'Y'  \n");
            sbSQL.append("AND    A.ISMOVIE = 'Y'  \n");
            sbSQL.append("AND    A.GROUPCODE = " + SQLString.Format(box.getInt("p_groupcode")) + "  \n");
            sbSQL.append("ORDER BY A.IDATE DESC  \n");
    		
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
	 * 지식 UCC 조회수 증가
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/
    public int updateMovieUCCReadCnt(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_KNOWLEDGEUCC  \n");
            sbSQL.append("SET    READCNT = READCNT + 1  \n");
            sbSQL.append("WHERE  CONTENTKEY = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getString("p_contentkey"));
            isOk = pstmt.executeUpdate();

            if(isOk > 0) { 
            	connMgr.commit();
            }
        } catch(Exception ex) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 지식 UCC 추천하기
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/
    public int insertKnowledgeUCCRecommend(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            String v_recomyn = "";
            
            sbSQL = new StringBuffer();
            sbSQL.append("SELECT DECODE(COUNT(*), 0, 'N', 'Y') AS RECOMYN  \n");
            sbSQL.append("FROM   TZ_KNOWLEDGEUCC_RECOMMEND  \n");
            sbSQL.append("WHERE  CONTENTKEY = " + SQLString.Format(box.getString("p_contentkey")) + "  \n");
            sbSQL.append("AND    RECOMUSERID = " + SQLString.Format(box.getSession("userid")) + "  \n");
            
            ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			v_recomyn = ls.getString("recomyn");
    		}
    		
    		if(v_recomyn.equals("Y")) {
    			return 0;
    		}

            sbSQL.setLength(0);
            sbSQL.append("INSERT INTO TZ_KNOWLEDGEUCC_RECOMMEND  \n");
            sbSQL.append("      (CONTENTKEY  \n");
            sbSQL.append("     , RECOMUSERID  \n");
            sbSQL.append("     , RECOMDATE)  \n");
            sbSQL.append("VALUES(?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getString("p_contentkey"));
            pstmt.setString(index++, box.getSession("userid"));
            isOk = pstmt.executeUpdate();

            if(isOk > 0) { 
            	connMgr.commit();
            }
        } catch(Exception ex) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 지식 UCC 삭제하기
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/
    public int deleteKnowledgeUCCInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_KNOWLEDGEUCC  \n");
            sbSQL.append("SET    ISDELETED = 'Y'  \n");
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("     , LUSERIP = ?  \n");
            sbSQL.append("WHERE  CONTENTKEY = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setString(index++, box.getSession("userip"));
            pstmt.setString(index++, box.getString("p_contentkey"));
            isOk = pstmt.executeUpdate();

            if(isOk > 0) { 
            	connMgr.commit();
            }
        } catch(Exception ex) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 지식 UCC 댓글 등록
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/
    public int insertKnowledgeUCCComment(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("INSERT INTO TZ_KNOWLEDGEUCC_COMMENT  \n");
            sbSQL.append("      (CONTENTKEY  \n");
            sbSQL.append("     , COMMENTSEQ  \n");
            sbSQL.append("     , CONTENTPOINT  \n");
            sbSQL.append("     , COMMENTS  \n");
            sbSQL.append("     , ISDELETED  \n");
            sbSQL.append("     , LUSERID  \n");
            sbSQL.append("     , LDATE  \n");
            sbSQL.append("     , LUSERIP)  \n");
            sbSQL.append("SELECT ?  \n");
            sbSQL.append("     , NVL(MAX(COMMENTSEQ), 0) + 1  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , 'N'  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("FROM   TZ_KNOWLEDGEUCC_COMMENT  \n");
            sbSQL.append("WHERE  CONTENTKEY = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getString("p_contentkey"));
            pstmt.setString(index++, "0");
            pstmt.setString(index++, box.getString("p_comments"));
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setString(index++, box.getSession("userip"));
            pstmt.setString(index++, box.getString("p_contentkey"));
            isOk = pstmt.executeUpdate();

            if(isOk > 0) { 
            	connMgr.commit();
            }
        } catch(Exception ex) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 지식 UCC 댓글 삭제하기
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/
    public int deleteKnowledgeUCCComment(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_KNOWLEDGEUCC_COMMENT  \n");
            sbSQL.append("SET    ISDELETED = 'Y'  \n");
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("     , LUSERIP = ?  \n");
            sbSQL.append("WHERE  CONTENTKEY = ?  \n");
            sbSQL.append("AND    COMMENTSEQ = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setString(index++, box.getSession("userip"));
            pstmt.setString(index++, box.getString("p_contentkey"));
            pstmt.setString(index++, box.getString("p_commentseq"));
            isOk = pstmt.executeUpdate();

            if(isOk > 0) { 
            	connMgr.commit();
            }
        } catch(Exception ex) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 지식 UCC 수정
	 * @param box          receive from the form object and session
	 * @return ArrayList   지식 UCC(동영상) 리스트
     **/
    public int updateKnowledgeUCCInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_KNOWLEDGEUCC  \n");
            sbSQL.append("SET    CATEGORY = ?  \n");
            sbSQL.append("     , TITLE = ?  \n");
            sbSQL.append("     , CONTENTSUM = ?  \n");
            sbSQL.append("     , CONTENTDESC = ?  \n");
            sbSQL.append("     , TAG = ?  \n");
            if(!box.getRealFileName("p_file").equals("")) {
            	sbSQL.append("     , REALFILE = ?  \n");
            	sbSQL.append("     , SAVEFILE = ?  \n");
            }
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("     , LUSERIP = ?  \n");
            sbSQL.append("WHERE  CONTENTKEY = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getString("p_category"));
            pstmt.setString(index++, box.getString("p_title"));
            pstmt.setString(index++, box.getString("p_contentsum"));
            pstmt.setString(index++, box.getString("p_contentdesc"));
            pstmt.setString(index++, box.getString("p_tag"));
            if(!box.getRealFileName("p_file").equals("")) {
	            pstmt.setString(index++, box.getRealFileName("p_file"));
	            pstmt.setString(index++, box.getNewFileName("p_file"));
            }
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setString(index++, box.getSession("userip"));
            pstmt.setString(index++, box.getString("p_contentkey"));
            isOk = pstmt.executeUpdate();

            if(isOk > 0) { 
            	connMgr.commit();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * 관심 지식UCC 등록
	 * @param box receive from the form object and session
	 * @return int 결과
     **/
    public int insertKnowledgeUCCInterests(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        int index = 1;
        Vector v_interests = box.getVector("p_interests");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            for(int i = 0 ; i < v_interests.size() ; i++) {
            	sbSQL.setLength(0);
                sbSQL.append("DELETE FROM TZ_KNOWLEDGEUCC_INTEREST  \n");
                sbSQL.append("WHERE  CONTENTKEY = ?  \n");
                sbSQL.append("AND    USERID = ?  \n");

                pstmt = connMgr.prepareStatement(sbSQL.toString());
                index = 1;
                pstmt.setString(index++, (String)v_interests.elementAt(i));
                pstmt.setString(index++, box.getSession("userid"));
                isOk += pstmt.executeUpdate();
                if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            	sbSQL.setLength(0);
                sbSQL.append("INSERT INTO TZ_KNOWLEDGEUCC_INTEREST  \n");
                sbSQL.append("      (CONTENTKEY  \n");
                sbSQL.append("     , USERID  \n");
                sbSQL.append("     , LDATE)  \n");
                sbSQL.append("VALUES(?  \n");
                sbSQL.append("     , ?  \n");
                sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))  \n");

                pstmt = connMgr.prepareStatement(sbSQL.toString());
                index = 1;
                pstmt.setString(index++, (String)v_interests.elementAt(i));
                pstmt.setString(index++, box.getSession("userid"));
                isOk += pstmt.executeUpdate();
                if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
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
}