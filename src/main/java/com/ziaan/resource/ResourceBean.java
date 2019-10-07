package com.ziaan.resource;

import java.io.File;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.FileDelete;
import com.ziaan.library.FileMove;
import com.ziaan.library.FileUnzip;
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

public class ResourceBean {
	
	public final String CATEGORY_CODE = "0093";
	private ConfigSet config;
	private int row;
	
    public ResourceBean() {
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.resource.row")); // �� ����� �������� row ���� �����Ѵ�.
        } catch(Exception e) { 
            e.printStackTrace();
        }
    }
    
    /**
	 * �н��ڿ� �Խ��� ����Ʈ
	 * @param box          receive from the form object and session
	 * @return ArrayList   �н��ڿ� �Խ��� ����Ʈ
     **/
    public ArrayList selectResourceBoardList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT BUNRU_SEQ  \n");
            sbSQL.append("     , BUNRU  \n");
            sbSQL.append("     , BOARD_TYPE  \n");
            sbSQL.append("FROM   TZ_RESOURCES_TYPE  \n");
            sbSQL.append("WHERE  BOARD_TYPE != 'B'  \n");
    		sbSQL.append("ORDER BY BUNRU_SEQ ASC  \n");
    		
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
	 * �н��ڿ� �Խ��� ����
	 * @param box          receive from the form object and session
	 * @return DataBox   	�н��ڿ� �Խ��� ����
     **/
    public DataBox selectResourceBoardInfo(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	int v_bunru_seq = box.getInt("p_bunru_seq");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		dbox = new DataBox("");
    		
            sbSQL.append("SELECT BUNRU_SEQ  \n");
            sbSQL.append("     , BUNRU  \n");
            sbSQL.append("     , BOARD_TYPE  \n");
            sbSQL.append("FROM   TZ_RESOURCES_TYPE  \n");
            sbSQL.append("WHERE  BUNRU_SEQ = " + SQLString.Format(v_bunru_seq) + "  \n");
    		
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
	 * �н��ڿ� ����Ʈ(������)
	 * @param box          receive from the form object and session
	 * @return ArrayList   �н��ڿ� ����Ʈ
     **/
    public ArrayList selectResourceList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	int v_pageno = box.getInt("p_pageno");
    	int v_bunru_seq = box.getInt("p_bunru_seq");
    	String v_category = box.getStringDefault("s_category", "ALL");
    	String v_grcode = box.getStringDefault("s_grcode", "ALL");
    	String v_search_type = box.getStringDefault("p_search_type", "titleanddesc");
    	String v_search_text = box.getString("p_search_text");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT A.BUNRU_SEQ  \n");
            sbSQL.append("     , A.SEQ  \n");
            sbSQL.append("     , A.GRCODE  \n");
         //   sbSQL.append("     , A.CATEGORY  \n");
            sbSQL.append("     , GET_CODENM(" + SQLString.Format(CATEGORY_CODE) + ", A.CATEGORY) AS CATEGORYNM  \n");
            sbSQL.append("     , A.TITLE  \n");
            sbSQL.append("     , A.CONTENT  \n");
            sbSQL.append("     , A.VISIT  \n");
            sbSQL.append("     , A.LUSERID  \n");
            sbSQL.append("     , B.NAME  \n");
            sbSQL.append("     , A.LDATE  \n");
            sbSQL.append("     , A.MAIN_IMG  \n");
            sbSQL.append("     , A.MAIN_PAGE  \n");
            sbSQL.append("     , A.WIDTH  \n");
            sbSQL.append("     , A.HEIGHT  \n");
            sbSQL.append("     , A.FILE_DIRECTORY  \n");
            sbSQL.append("     , A.AUTHOR  \n");
            sbSQL.append("     , A.PUBLISH_NAME  \n");
            sbSQL.append("     ,(SELECT UP_FILENAME  \n");
            sbSQL.append("       FROM   TZ_RESOURCES_FILE  \n");
            sbSQL.append("       WHERE  SEQ = A.SEQ  \n");
            sbSQL.append("       AND    BUNRU_SEQ = A.BUNRU_SEQ  \n");
            sbSQL.append("       AND    FILESEQ = 1) AS UP_FILENAME  \n");
            sbSQL.append("     ,(SELECT REAL_FILENAME  \n");
            sbSQL.append("       FROM   TZ_RESOURCES_FILE  \n");
            sbSQL.append("       WHERE  SEQ = A.SEQ  \n");
            sbSQL.append("       AND    BUNRU_SEQ = A.BUNRU_SEQ  \n");
            sbSQL.append("       AND    FILESEQ = 1) AS REAL_FILENAME  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_RESOURCES_INTEREST  \n");
            sbSQL.append("       WHERE  SEQ = A.SEQ  \n");
            sbSQL.append("       AND    BUNRU_SEQ = A.BUNRU_SEQ) AS INTEREST_CNT  \n");
            sbSQL.append("     , ROUND(SYSDATE - TO_DATE(A.LDATE, 'YYYYMMDDHH24MISS')) AS DATEDIFF  \n");
            sbSQL.append("FROM   TZ_RESOURCES_INFO A  \n");
            sbSQL.append("     , TZ_MEMBER B  \n");
            sbSQL.append("WHERE  A.LUSERID = B.USERID (+)  \n");
            sbSQL.append("AND    A.DEL_FLAG != 'Y'  \n");
            sbSQL.append("AND    A.BUNRU_SEQ = " + SQLString.Format(v_bunru_seq) + "  \n");
          //  sbSQL.append("AND    A.CATEGORY = CASE WHEN 'ALL' = " + SQLString.Format(v_category) + " THEN A.CATEGORY  \n");
          //  sbSQL.append("                         ELSE " + SQLString.Format(v_category) + "  \n");
         //   sbSQL.append("                    END  \n");
            sbSQL.append("AND    INSTR(A.GRCODE, CASE WHEN " + SQLString.Format(v_grcode) + " = 'ALL' THEN ','  \n");
            sbSQL.append("                            ELSE " + SQLString.Format(v_grcode) + "  \n");
            sbSQL.append("                       END) > 0  \n");
            
            if(v_search_type.equals("name")) {
            	sbSQL.append("AND    B.NAME LIKE '%' || " + SQLString.Format(v_search_text) + " || '%'  \n");
            } else if(v_search_type.equals("titleanddesc")) {
            	sbSQL.append("AND   (A.TITLE LIKE '%' || " + SQLString.Format(v_search_text) + " || '%' OR A.CONTENT LIKE '%' || " + SQLString.Format(v_search_text) + " || '%')  \n");
            }
            
            sbSQL.append("ORDER BY SEQ DESC  \n");
            
            System.out.println(sbSQL.toString());
    		ls = connMgr.executeQuery(sbSQL.toString());
            ls.setPageSize(row);					// �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);			// ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();	// ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount = ls.getTotalCount();	// ��ü row ���� ��ȯ�Ѵ�
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
                dbox.put("d_dispnum",   new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(row));
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
	 * �н��ڿ� ����Ʈ(�����)
	 * @param box          receive from the form object and session
	 * @return ArrayList   �н��ڿ� ����Ʈ
     **/
    public ArrayList selectResourceListForUser(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	int v_pageno = box.getInt("p_pageno");
    	int v_bunru_seq = box.getInt("p_bunru_seq");
    	String v_category = box.getStringDefault("s_category", "ALL");
    	String s_userid = box.getSession("userid");
    	String v_search_type = box.getStringDefault("p_search_type", "titleanddesc");
    	String v_search_text = box.getString("p_search_text");
    	String v_audio_yn = box.getString("p_audio_yn");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT A.BUNRU_SEQ  \n");
            sbSQL.append("     , A.SEQ  \n");
            sbSQL.append("     , A.GRCODE  \n");
        //  sbSQL.append("     , A.CATEGORY  \n");
            sbSQL.append("     , GET_CODENM(" + SQLString.Format(CATEGORY_CODE) + ", A.CATEGORY) AS CATEGORYNM  \n");
            sbSQL.append("     , A.TITLE  \n");
            sbSQL.append("     , A.CONTENT  \n");
            sbSQL.append("     , A.VISIT  \n");
            sbSQL.append("     , A.LUSERID  \n");
            sbSQL.append("     , B.NAME  \n");
            sbSQL.append("     , A.LDATE  \n");
            sbSQL.append("     , A.MAIN_IMG  \n");
            sbSQL.append("     , A.MAIN_PAGE  \n");
            sbSQL.append("     , A.WIDTH  \n");
            sbSQL.append("     , A.HEIGHT  \n");
            sbSQL.append("     , A.FILE_DIRECTORY  \n");
            sbSQL.append("     , A.AUTHOR  \n");
            sbSQL.append("     , A.PUBLISH_NAME  \n");
            sbSQL.append("     ,(SELECT UP_FILENAME  \n");
            sbSQL.append("       FROM   TZ_RESOURCES_FILE  \n");
            sbSQL.append("       WHERE  SEQ = A.SEQ  \n");
            sbSQL.append("       AND    BUNRU_SEQ = A.BUNRU_SEQ  \n");
            sbSQL.append("       AND    FILESEQ = 1) AS UP_FILENAME  \n");
            sbSQL.append("     ,(SELECT REAL_FILENAME  \n");
            sbSQL.append("       FROM   TZ_RESOURCES_FILE  \n");
            sbSQL.append("       WHERE  SEQ = A.SEQ  \n");
            sbSQL.append("       AND    BUNRU_SEQ = A.BUNRU_SEQ  \n");
            sbSQL.append("       AND    FILESEQ = 1) AS REAL_FILENAME  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_RESOURCES_INTEREST  \n");
            sbSQL.append("       WHERE  SEQ = A.SEQ  \n");
            sbSQL.append("       AND    BUNRU_SEQ = A.BUNRU_SEQ) AS INTEREST_CNT  \n");
            sbSQL.append("     , ROUND(SYSDATE - TO_DATE(A.LDATE, 'YYYYMMDDHH24MISS')) AS DATEDIFF  \n");
            sbSQL.append("     ,(SELECT JOB_CD  \n");
            sbSQL.append("       FROM   TZ_RESOURCES_JIKMU  \n");
            sbSQL.append("       WHERE  JOB_CD = C.JOB_CD  \n");
            sbSQL.append("       AND    SEQ = A.SEQ  \n");
            sbSQL.append("       AND    BUNRU_SEQ = A.BUNRU_SEQ) AS JIKMU  \n");
            sbSQL.append("     , DECODE(D.FILESEQ, NULL, 'N', 'Y') AS AUDIO_YN  \n");
            sbSQL.append("FROM   TZ_RESOURCES_INFO A  \n");
            sbSQL.append("     , TZ_MEMBER B  \n");
            sbSQL.append("     ,(SELECT DISTINCT T3.BUNRU_SEQ  \n");
            sbSQL.append("            , T3.SEQ  \n");
            sbSQL.append("            , T1.JOB_CD  \n");
            sbSQL.append("       FROM   TZ_MEMBER T1  \n");
            sbSQL.append("            , TZ_GRCOMP T2  \n");
            sbSQL.append("            , TZ_RESOURCES_INFO T3  \n");
            sbSQL.append("       WHERE  T1.COMP = T2.COMP  \n");
            sbSQL.append("       AND    T1.USERID = " + SQLString.Format(s_userid) + "  \n");
            sbSQL.append("       AND    INSTR(T3.GRCODE, T2.GRCODE) > 0  \n");
            sbSQL.append("       AND    T3.BUNRU_SEQ = " + SQLString.Format(v_bunru_seq) + ") C  \n");
            sbSQL.append("     ,(SELECT A.SEQ  \n");
            sbSQL.append("            , A.FILESEQ  \n");
            sbSQL.append("            , A.BUNRU_SEQ  \n");
            sbSQL.append("       FROM   TZ_RESOURCES_FILE A  \n");
            sbSQL.append("            , TZ_RESOURCES_TYPE B  \n");
            sbSQL.append("       WHERE  A.BUNRU_SEQ = B.BUNRU_SEQ  \n");
            sbSQL.append("       AND    A.FILESEQ = 5  \n");
            sbSQL.append("       AND    B.BOARD_TYPE = 'B') D  \n");
            sbSQL.append("WHERE  A.LUSERID = B.USERID (+)  \n");
            sbSQL.append("AND    A.SEQ = D.SEQ (+)  \n");
            sbSQL.append("AND    A.BUNRU_SEQ = D.BUNRU_SEQ (+)  \n");
            sbSQL.append("AND    A.DEL_FLAG != 'Y'  \n");
            sbSQL.append("AND    A.BUNRU_SEQ = " + SQLString.Format(v_bunru_seq) + "  \n");
          //  sbSQL.append("AND    A.CATEGORY = CASE WHEN 'ALL' = " + SQLString.Format(v_category) + " THEN A.CATEGORY  \n");
          //  sbSQL.append("                         ELSE " + SQLString.Format(v_category) + "  \n");
          //  sbSQL.append("                    END  \n");
            if(v_search_type.equals("name")) {
            	sbSQL.append("AND    B.NAME LIKE '%' || " + SQLString.Format(v_search_text) + " || '%'  \n");
            } else if(v_search_type.equals("titleanddesc")) {
            	sbSQL.append("AND   (A.TITLE LIKE '%' || " + SQLString.Format(v_search_text) + " || '%' OR A.CONTENT LIKE '%' || " + SQLString.Format(v_search_text) + " || '%')  \n");
            }
            if(v_audio_yn.equals("Y")) {
            	sbSQL.append("AND    D.FILESEQ IS NOT NULL  \n");
            }
            sbSQL.append("AND    A.BUNRU_SEQ = C.BUNRU_SEQ  \n");
            sbSQL.append("AND    A.SEQ = C.SEQ  \n");
            sbSQL.append("ORDER BY SEQ DESC  \n");

    		/*
            sbSQL.append("SELECT A.BUNRU_SEQ  \n");
            sbSQL.append("     , A.SEQ  \n");
            sbSQL.append("     , A.GRCODE  \n");
            sbSQL.append("     , A.CATEGORY  \n");
            sbSQL.append("     , GET_CODENM(" + SQLString.Format(CATEGORY_CODE) + ", A.CATEGORY) AS CATEGORYNM  \n");
            sbSQL.append("     , A.TITLE  \n");
            sbSQL.append("     , A.CONTENT  \n");
            sbSQL.append("     , A.VISIT  \n");
            sbSQL.append("     , A.LUSERID  \n");
            sbSQL.append("     , B.NAME  \n");
            sbSQL.append("     , A.LDATE  \n");
            sbSQL.append("     , A.MAIN_IMG  \n");
            sbSQL.append("     , A.MAIN_PAGE  \n");
            sbSQL.append("     , A.WIDTH  \n");
            sbSQL.append("     , A.HEIGHT  \n");
            sbSQL.append("     , A.FILE_DIRECTORY  \n");
            sbSQL.append("     , A.AUTHOR  \n");
            sbSQL.append("     , A.PUBLISH_NAME  \n");
            sbSQL.append("     ,(SELECT UP_FILENAME  \n");
            sbSQL.append("       FROM   TZ_RESOURCES_FILE  \n");
            sbSQL.append("       WHERE  SEQ = A.SEQ  \n");
            sbSQL.append("       AND    BUNRU_SEQ = A.BUNRU_SEQ  \n");
            sbSQL.append("       AND    FILESEQ = 1) AS UP_FILENAME  \n");
            sbSQL.append("     ,(SELECT REAL_FILENAME  \n");
            sbSQL.append("       FROM   TZ_RESOURCES_FILE  \n");
            sbSQL.append("       WHERE  SEQ = A.SEQ  \n");
            sbSQL.append("       AND    BUNRU_SEQ = A.BUNRU_SEQ  \n");
            sbSQL.append("       AND    FILESEQ = 1) AS REAL_FILENAME  \n");
            sbSQL.append("     ,(SELECT COUNT(*)  \n");
            sbSQL.append("       FROM   TZ_RESOURCES_INTEREST  \n");
            sbSQL.append("       WHERE  SEQ = A.SEQ  \n");
            sbSQL.append("       AND    BUNRU_SEQ = A.BUNRU_SEQ) AS INTEREST_CNT  \n");
            sbSQL.append("     , ROUND(SYSDATE - TO_DATE(A.LDATE, 'YYYYMMDDHH24MISS')) AS DATEDIFF  \n");
            sbSQL.append("     ,(SELECT JOB_CD  \n");
            sbSQL.append("       FROM   TZ_RESOURCES_JIKMU  \n");
            sbSQL.append("       WHERE  JOB_CD = C.JOB_CD) AS JIKMU  \n");
            sbSQL.append("FROM   TZ_RESOURCES_INFO A  \n");
            sbSQL.append("     , TZ_MEMBER B  \n");
            sbSQL.append("     , TZ_MEMBER C  \n");
            sbSQL.append("     , TZ_GRCOMP D  \n");
            sbSQL.append("WHERE  A.LUSERID = B.USERID  \n");
            sbSQL.append("AND    A.DEL_FLAG != 'Y'  \n");
            sbSQL.append("AND    A.BUNRU_SEQ = " + SQLString.Format(v_bunru_seq) + "  \n");
            sbSQL.append("AND    A.CATEGORY = CASE WHEN 'ALL' = " + SQLString.Format(v_category) + " THEN A.CATEGORY  \n");
            sbSQL.append("                         ELSE " + SQLString.Format(v_category) + "  \n");
            sbSQL.append("                    END  \n");
            sbSQL.append("AND    C.USERID = " + SQLString.Format(s_userid) + "  \n");
            sbSQL.append("AND    C.COMP = D.COMP  \n");
            sbSQL.append("AND    INSTR(A.GRCODE, D.GRCODE) > 0  \n");
            sbSQL.append("ORDER BY LDATE DESC  \n");
            */
            
    		ls = connMgr.executeQuery(sbSQL.toString());
            ls.setPageSize(row);					// �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);			// ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();	// ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount = ls.getTotalCount();	// ��ü row ���� ��ȯ�Ѵ�
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
                dbox.put("d_dispnum",   new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(row));
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
	 * �н��ڿ� Count
	 * @param box          receive from the form object and session
	 * @return ArrayList   �н��ڿ� Count
     **/
    public static String selectResourceCnt(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	String result = "";
    	StringBuffer sbSQL = null;
    	int v_bunru_seq = box.getInt("p_bunru_seq");
    	String v_category = box.getStringDefault("s_category", "ALL");
    	String s_userid = box.getSession("userid");
    	String v_search_type = box.getStringDefault("p_search_type", "titleanddesc");
    	String v_search_text = box.getString("p_search_text");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT COUNT(*) AS CNT  \n");
            sbSQL.append("FROM   TZ_RESOURCES_INFO A  \n");
            sbSQL.append("     ,(SELECT DISTINCT T3.BUNRU_SEQ  \n");
            sbSQL.append("            , T3.SEQ  \n");
            sbSQL.append("       FROM   TZ_MEMBER T1  \n");
            sbSQL.append("            , TZ_GRCOMP T2  \n");
            sbSQL.append("            , TZ_RESOURCES_INFO T3  \n");
            sbSQL.append("       WHERE  T1.COMP = T2.COMP  \n");
            sbSQL.append("       AND    T1.USERID = " + SQLString.Format(s_userid) + "  \n");
            sbSQL.append("       AND    INSTR(T3.GRCODE, T2.GRCODE) > 0  \n");
            sbSQL.append("       AND    T3.BUNRU_SEQ = " + SQLString.Format(v_bunru_seq) + ") C  \n");
            sbSQL.append("WHERE  A.DEL_FLAG != 'Y'  \n");
            sbSQL.append("AND    A.BUNRU_SEQ = " + SQLString.Format(v_bunru_seq) + "  \n");
            //sbSQL.append("AND    A.CATEGORY = CASE WHEN 'ALL' = " + SQLString.Format(v_category) + " THEN A.CATEGORY  \n");
            //sbSQL.append("                         ELSE " + SQLString.Format(v_category) + "  \n");
            //sbSQL.append("                    END  \n");
            
            if(v_search_type.equals("name")) {
            	sbSQL.append("AND    GET_NAME(A.LUSERID) LIKE '%' || " + SQLString.Format(v_search_text) + " || '%'  \n");
            } else if(v_search_type.equals("titleanddesc")) {
            	sbSQL.append("AND   (A.TITLE LIKE '%' || " + SQLString.Format(v_search_text) + " || '%' OR A.CONTENT LIKE '%' || " + SQLString.Format(v_search_text) + " || '%')  \n");
            }
            
            sbSQL.append("AND    A.BUNRU_SEQ = C.BUNRU_SEQ  \n");
            sbSQL.append("AND    A.SEQ = C.SEQ  \n");
            sbSQL.append("ORDER BY A.SEQ DESC  \n");

    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			result = ls.getString("cnt");
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
	 * �н��ڿ� ī�װ� ����Ʈ ��ȸ
	 * @param box          receive from the form object and session
	 * @return ArrayList   ���� UCC(������) ����Ʈ
     **/
    public ArrayList selectResourceCategoryList(RequestBox box) throws Exception {
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
	 * �н��ڿ� ���ο� SEQ
	 * @param box receive from the form object and session
	 * @return int �н��ڿ� ���ο� SEQ
     **/
    public int selectResourceNewSeq(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	StringBuffer sbSQL = null;
    	int v_seq = 0;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT NVL(MAX(SEQ), 0) + 1 AS NEW_SEQ  \n");
            sbSQL.append("FROM   TZ_RESOURCES_INFO  \n");
    		
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
	 * �н��ڿ� ���
	 * @param box receive from the form object and session
	 * @return int ��ϰ��
     **/
    public int insertResourceInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        String results = "OK";
        String v_file_path = "";
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            int v_seq = selectResourceNewSeq(box);
        	String v_contentkey = "";
        	
        	if(box.getString("p_board_type").equals("M")) {
	        	v_contentkey = FormatDate.getDate("yyyyMMddHHmmss");
	        	for (int i = 0; i < 6 ; i++) {
	        		char ch = (char) ((Math.random() * 26) + 97);
	        		v_contentkey += ch;
	        	}
        	}
        	
        	Vector v_grcodes = box.getVector("p_grcodes");
        	String v_grcode = "";
        	for(int i = 0 ; i < v_grcodes.size(); i ++) {
        		v_grcode += v_grcodes.elementAt(i) + ",";
        	}

            sbSQL = new StringBuffer();
            sbSQL.append("INSERT INTO TZ_RESOURCES_INFO  \n");
            sbSQL.append("      (SEQ  \n");
            sbSQL.append("     , BUNRU_SEQ  \n");
            sbSQL.append("     , GRCODE  \n");
            sbSQL.append("     , TITLE  \n");
            sbSQL.append("     , CONTENT  \n");
            sbSQL.append("     , VISIT  \n");
            sbSQL.append("     , LUSERID  \n");
            sbSQL.append("     , LDATE  \n");
            sbSQL.append("     , MAIN_IMG  \n");
            sbSQL.append("     , MAIN_PAGE  \n");
            sbSQL.append("     , WIDTH  \n");
            sbSQL.append("     , HEIGHT  \n");
            sbSQL.append("     , FILE_DIRECTORY  \n");
            sbSQL.append("     , DEL_FLAG  \n");
            sbSQL.append("     , AUTHOR  \n");
            sbSQL.append("     , BUNYA  \n");
            sbSQL.append("     , PUBLISH_NAME  \n");
            sbSQL.append("     , PUBLISH_DATE  \n");
            sbSQL.append("     , REGDATE  ) \n");
                       sbSQL.append("VALUES(?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , EMPTY_CLOB()  \n");
            sbSQL.append("     , 0  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , 'N'  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') )  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setInt(index++, v_seq);
            pstmt.setInt(index++, box.getInt("p_bunru_seq"));
            pstmt.setString(index++, v_grcode);
            pstmt.setString(index++, box.getString("p_title"));
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setString(index++, box.getNewFileName("p_main_img"));
            pstmt.setString(index++, box.getString("p_main_page"));
            pstmt.setInt(index++, box.getInt("p_width"));
            pstmt.setInt(index++, box.getInt("p_height"));
            pstmt.setString(index++, v_contentkey);
            pstmt.setString(index++, box.getString("p_author"));
            pstmt.setString(index++, box.getString("p_bunya"));
            pstmt.setString(index++, box.getString("p_publish_name"));
            pstmt.setString(index++, box.getString("p_publish_date"));
            isOk = pstmt.executeUpdate();
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            
            sbSQL.setLength(0);
            sbSQL.append("SELECT CONTENT  \n");
            sbSQL.append("FROM   TZ_RESOURCES_INFO  \n");
            sbSQL.append("WHERE  SEQ = " + SQLString.Format(v_seq) + "  \n");
            sbSQL.append("AND    BUNRU_SEQ = " + SQLString.Format(box.getInt("p_bunru_seq")) + "  \n");
            connMgr.setOracleCLOB(sbSQL.toString(), box.getString("p_content"));
            
            sbSQL.setLength(0);
            sbSQL.append("SELECT GRCODE  \n");
            sbSQL.append("FROM   TZ_RESOURCES_INFO  \n");
            sbSQL.append("WHERE  SEQ = " + SQLString.Format(v_seq) + "  \n");
            sbSQL.append("AND    BUNRU_SEQ = " + SQLString.Format(box.getInt("p_bunru_seq")) + "  \n");
//            connMgr.setOracleCLOB(sbSQL.toString(), v_grcode);
            
            results = insertResouceJikmuInfo(box, connMgr, v_seq);

            if(isOk > 0 && results.equals("OK")) {
            	if(box.getString("p_board_type").equals("M")) {
	                v_file_path = config.getProperty("dir.upload.resource") + "movies\\" + v_contentkey + "\\";
	                results = controlFile("insert", config.getProperty("dir.upload.resource"), box.getNewFileName("p_file1"), v_file_path);
            	} else {
            		results = insertResouceFileInfo(box, connMgr, v_seq);
            	}
                if(results.equals("OK")) {
                	connMgr.commit();
                } else {
                	isOk = -1;
                	connMgr.rollback();
                }
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("results = " + results + "\r\n" + "sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * �н��ڿ� ���� ���
	 * @param box receive from the form object and session
	 * @param connMgr DBConnectionManager
	 * @param v_seq �н��ڿ� SEQ
	 * @return String ��ϰ��
     **/
    public String insertResouceJikmuInfo(RequestBox box, DBConnectionManager connMgr, int v_seq) throws Exception { 
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        String results = "OK";
        
        try {
        	sbSQL = new StringBuffer();
        	
        	Vector v_jikmus = box.getVector("p_jikmu");
            sbSQL.setLength(0);
            sbSQL.append("DELETE FROM TZ_RESOURCES_JIKMU  \n");
            sbSQL.append("WHERE  SEQ = ?  \n");
            sbSQL.append("AND    BUNRU_SEQ = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setInt(index++, v_seq);
            pstmt.setInt(index++, box.getInt("p_bunru_seq"));
            isOk = pstmt.executeUpdate();
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
        	
        	for(int i = 1 ; i < v_jikmus.size() ; i++) {
	            sbSQL.setLength(0);
	            sbSQL.append("INSERT INTO TZ_RESOURCES_JIKMU  \n");
	            sbSQL.append("      (SEQ  \n");
	            sbSQL.append("     , BUNRU_SEQ  \n");
	            sbSQL.append("     , JOB_CD  \n");
	            sbSQL.append("     , LUSERID  \n");
	            sbSQL.append("     , LDATE)  \n");
	            sbSQL.append("VALUES(?  \n");
	            sbSQL.append("     , ?  \n");
	            sbSQL.append("     , ?  \n");
	            sbSQL.append("     , ?  \n");
	            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))  \n");
	
	            pstmt = connMgr.prepareStatement(sbSQL.toString());
	            index = 1;
	            pstmt.setInt(index++, v_seq);
	            pstmt.setInt(index++, box.getInt("p_bunru_seq"));
	            pstmt.setString(index++, v_jikmus.elementAt(i).toString());
	            pstmt.setString(index++, box.getSession("userid"));
	            isOk += pstmt.executeUpdate();
	            pstmt.close();
        	}

            if(isOk > 0) {
            	results = "OK";
            }
        } catch(Exception ex) {
            connMgr.rollback();
            results = "ERROR";
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("results = " + results + "\r\n" + "sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
        }
        return results;
    }
    
    /**
	 * �н��ڿ� ���� ���
	 * @param box receive from the form object and session
	 * @param connMgr DBConnectionManager
	 * @param v_seq �н��ڿ� SEQ
	 * @return String ��ϰ��
     **/
    public String insertResouceFileInfo(RequestBox box, DBConnectionManager connMgr, int v_seq) throws Exception { 
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        String results = "OK";
        
        try {
        	sbSQL = new StringBuffer();
        	
        	for(int i = 1 ; i < 7 ; i++) {
        		if(!box.getNewFileName("p_file" + i).equals("")) {
		            sbSQL.setLength(0);
		            sbSQL.append("INSERT INTO TZ_RESOURCES_FILE  \n");
		            sbSQL.append("      (SEQ  \n");
		            sbSQL.append("     , FILESEQ  \n");
		            sbSQL.append("     , BUNRU_SEQ  \n");
		            sbSQL.append("     , REAL_FILENAME  \n");
		            sbSQL.append("     , UP_FILENAME  \n");
		            sbSQL.append("     , LUSERID  \n");
		            sbSQL.append("     , LDATE)  \n");
		            sbSQL.append("VALUES(?  \n");
		            sbSQL.append("     , ?  \n");
		            sbSQL.append("     , ?  \n");
		            sbSQL.append("     , ?  \n");
		            sbSQL.append("     , ?  \n");
		            sbSQL.append("     , ?  \n");
		            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))  \n");
		
		            pstmt = connMgr.prepareStatement(sbSQL.toString());
		            int index = 1;
		            pstmt.setInt(index++, v_seq);
		            pstmt.setInt(index++, i);
		            pstmt.setInt(index++, box.getInt("p_bunru_seq"));
		            pstmt.setString(index++, box.getRealFileName("p_file" + i));
		            pstmt.setString(index++, box.getNewFileName("p_file" + i));
		            pstmt.setString(index++, box.getSession("userid"));
		            isOk += pstmt.executeUpdate();
					if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
        		}
        	}

            if(isOk > 0) {
            	results = "OK";
            }
        } catch(Exception ex) {
            connMgr.rollback();
            results = "ERROR";
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("results = " + results + "\r\n" + "sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
        }
        return results;
    }
    
    /**
     * Object ZIP ���Ϸ� Directory ����
     * @param String p_job ���/��������(insert/update)
     * @param String p_tempPath ���ε� ����
     * @param String p_tempFile ���ε� ����
     * @param String p_realPath ������ ����
     * @return String resuts ����޼���
     */
    public String controlFile(String p_job, String p_tempPath, String p_tempFile, String p_realPath) throws Exception {
        String results = "OK";
        boolean move_success = false;
        boolean extract_success = false;
        boolean allDelete_success = false;

        File tempFile = new File(p_tempPath + p_tempFile);

        // ÷�������� ���� ��쿡�� ����.
        if(tempFile.exists()) {
            results = p_job;
            results += "\\n\\n 0. v_realPath=" + p_realPath;
            results += "\\n\\n 0. v_tempPath=" + p_tempPath;
            try {
                if(p_job.equals("insert")) { 
                    // 1. ���� ����
                    File newDir = new File(p_realPath);
                    newDir.mkdir();
                    allDelete_success = true;
                    results += "\\n\\n 1. makeDirecotry OK. ";
                } else if(p_job.equals("update")) { 
                    // 1. ������ ���Ϲ� ���� ��� ����
                    FileDelete fd = new FileDelete();
                    allDelete_success = fd.allDelete(p_realPath);
                    results += "\\n\\n 1. allDelete =  " + (new Boolean(allDelete_success).toString());
                    // 1. ���� ����
                    File newDir = new File(p_realPath);
                    newDir.mkdirs();
                    allDelete_success = true;
                    results += "\\n\\n 1. makeDirecotry OK. ";
                }

                // 2. ���� �̵�
                if(allDelete_success) {
                    FileMove fc = new FileMove();
                    move_success = fc.move(p_realPath, p_tempPath, p_tempFile);
                }
                results += "\\n\\n 2. move to [" + p_realPath + "] =  " + (new Boolean(move_success).toString());

                // 3. ���� Ǯ��
                if(move_success) {
                    FileUnzip unzip = new FileUnzip();
                    extract_success = unzip.extract(p_realPath, p_tempFile);
                    results += "\\n\\n 3. unzip to [" + p_realPath + "] =  " + (new Boolean(extract_success).toString());
                    if(!extract_success) {
                        FileDelete fd = new FileDelete();
                        fd.allDelete(p_realPath);
                    }
                    results += "\\n\\n END of controlObjectFile() ";
                    results = "OK";
                }
            } catch(Exception ex) {
                FileDelete fd = new FileDelete();
                fd.allDelete(p_realPath);
                throw new Exception("ERROR results=" + results + "\r\n" + ex.getMessage());
            } finally {
                FileManager.deleteFile(p_realPath + "\\" + p_tempFile);
            }
        } else {
            FileManager.deleteFile(p_tempPath + "\\" + p_tempFile);
            results = "���� ���ε� ���ѿ뷮�� �ʰ��Ͽ����ϴ�. Ȯ�����ּ���.";
        }
        return results;
    }
    
    /**
	 * �н��ڿ� ����
	 * @param box receive from the form object and session
	 * @return DataBox �н��ڿ� ����
     **/
    public DataBox selectResourceInfo(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	int v_seq = box.getInt("p_seq");
    	int v_bunru_seq = box.getInt("p_bunru_seq");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		dbox = new DataBox("");
    		
            sbSQL.append("SELECT A.SEQ  \n");
            sbSQL.append("     , A.BUNRU_SEQ  \n");
            sbSQL.append("     , A.GRCODE  \n");
            sbSQL.append("     , A.CATEGORY  \n");
            sbSQL.append("     , A.TITLE  \n");
            sbSQL.append("     , A.CONTENT  \n");
            sbSQL.append("     , A.VISIT  \n");
            sbSQL.append("     , A.LUSERID  \n");
            sbSQL.append("     , A.LDATE  \n");
            sbSQL.append("     , A.MAIN_IMG  \n");
            sbSQL.append("     , A.MAIN_PAGE  \n");
            sbSQL.append("     , A.WIDTH  \n");
            sbSQL.append("     , A.HEIGHT  \n");
            sbSQL.append("     , A.FILE_DIRECTORY  \n");
            sbSQL.append("     , A.AUTHOR  \n");
            sbSQL.append("     , A.BUNYA  \n");
            sbSQL.append("     , A.PUBLISH_NAME  \n");
            sbSQL.append("     , A.PUBLISH_DATE  \n");
            sbSQL.append("     , B.NAME  \n");
            sbSQL.append("FROM   TZ_RESOURCES_INFO A  \n");
            sbSQL.append("     , TZ_MEMBER B  \n");
            sbSQL.append("WHERE  A.LUSERID = B.USERID  \n");
            sbSQL.append("AND    A.SEQ = " + SQLString.Format(v_seq) + "  \n");
            sbSQL.append("AND    A.BUNRU_SEQ = " + SQLString.Format(v_bunru_seq) + "  \n");
    		
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
	 * �н��ڿ� ���� ����Ʈ
	 * @param box          receive from the form object and session
	 * @return ArrayList   �н��ڿ� ���� ����Ʈ
     **/
    public ArrayList selectResourceFileList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	int v_seq = box.getInt("p_seq");
    	int v_bunru_seq = box.getInt("p_bunru_seq");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT SEQ  \n");
            sbSQL.append("     , BUNRU_SEQ  \n");
            sbSQL.append("     , FILESEQ  \n");
            sbSQL.append("     , REAL_FILENAME  \n");
            sbSQL.append("     , UP_FILENAME  \n");
            sbSQL.append("FROM   TZ_RESOURCES_FILE  \n");
            sbSQL.append("WHERE  SEQ = " + SQLString.Format(v_seq) + "  \n");
            sbSQL.append("AND    BUNRU_SEQ = " + SQLString.Format(v_bunru_seq) + "  \n");
            sbSQL.append("ORDER BY FILESEQ  \n");
    		
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
	 * �н��ڿ� ���� ����Ʈ
	 * @param box          receive from the form object and session
	 * @return ArrayList   �н��ڿ� ���� ����Ʈ
     **/
    public ArrayList selectResourceJikmuList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	int v_seq = box.getInt("p_seq");
    	int v_bunru_seq = box.getInt("p_bunru_seq");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
            
            sbSQL.append("SELECT A.JOB_CD  \n");
            sbSQL.append("     , B.JOBNM  \n");
            sbSQL.append("FROM   TZ_RESOURCES_JIKMU A  \n");
            sbSQL.append("     , TZ_JIKMU B  \n");
            sbSQL.append("WHERE  A.JOB_CD = B.JOB_CD  \n");
            sbSQL.append("AND    A.SEQ = " + SQLString.Format(v_seq) + "  \n");
            sbSQL.append("AND    A.BUNRU_SEQ = " + SQLString.Format(v_bunru_seq) + "  \n");
            sbSQL.append("ORDER BY B.LOCATION  \n");
    		
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
	 * �н��ڿ� ����
	 * @param box receive from the form object and session
	 * @return int ���
     **/
    public int updateResourceInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        String results = "OK";
        String v_file_path = "";
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
        	Vector v_grcodes = box.getVector("p_grcodes");
        	String v_grcode = "";
        	for(int i = 0 ; i < v_grcodes.size(); i ++) {
        		v_grcode += v_grcodes.elementAt(i) + ",";
        	}            
            String v_main_img = box.getNewFileName("p_main_img");

            sbSQL = new StringBuffer();
            sbSQL.append(" UPDATE TZ_RESOURCES_INFO  \n");
            sbSQL.append(" SET    GRCODE = ?  \n");
//            sbSQL.append("SET    GRCODE = EMPTY_CLOB()  \n");
            sbSQL.append("     , CATEGORY = ?  \n");
            sbSQL.append("     , TITLE = ?  \n");
            sbSQL.append("     , CONTENT = EMPTY_CLOB() \n");
//            sbSQL.append("     , CONTENT = EMPTY_CLOB()  \n");
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            if(!v_main_img.equals("")) {
            	sbSQL.append("     , MAIN_IMG = ?  \n");
            }
            sbSQL.append("     , MAIN_PAGE = ?  \n");
            sbSQL.append("     , WIDTH = ?  \n");
            sbSQL.append("     , HEIGHT = ?  \n");
            sbSQL.append("     , AUTHOR = ?  \n");
            sbSQL.append("     , BUNYA = ?  \n");
            sbSQL.append("     , PUBLISH_NAME = ?  \n");
            sbSQL.append("     , PUBLISH_DATE = ?  \n");
            sbSQL.append(" WHERE  SEQ = ?  \n");
            sbSQL.append(" AND    BUNRU_SEQ = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, v_grcode);
            pstmt.setString(index++, box.getString("p_category"));
            pstmt.setString(index++, box.getString("p_title"));
            pstmt.setString(index++, box.getSession("userid"));
            if(!v_main_img.equals("")) {
            	pstmt.setString(index++, box.getNewFileName("p_main_img"));
            }
            pstmt.setString(index++, box.getString("p_main_page"));
            pstmt.setString(index++, box.getString("p_width"));
            pstmt.setString(index++, box.getString("p_height"));
            pstmt.setString(index++, box.getString("p_author"));
            pstmt.setString(index++, box.getString("p_bunya"));
            pstmt.setString(index++, box.getString("p_publish_name"));
            pstmt.setString(index++, box.getString("p_publish_date"));
            pstmt.setString(index++, box.getString("p_seq"));
            pstmt.setString(index++, box.getString("p_bunru_seq"));
            isOk = pstmt.executeUpdate();
            
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            
            sbSQL.setLength(0);
            sbSQL.append("SELECT CONTENT  \n");
            sbSQL.append("FROM   TZ_RESOURCES_INFO  \n");
            sbSQL.append("WHERE  SEQ = " + SQLString.Format(box.getInt("p_seq")) + "  \n");
            sbSQL.append("AND    BUNRU_SEQ = " + SQLString.Format(box.getInt("p_bunru_seq")) + "  \n");
            connMgr.setOracleCLOB(sbSQL.toString(), box.getString("p_content"));
            
            sbSQL.setLength(0);
            sbSQL.append("SELECT GRCODE  \n");
            sbSQL.append("FROM   TZ_RESOURCES_INFO  \n");
            sbSQL.append("WHERE  SEQ = " + SQLString.Format(box.getInt("p_seq")) + "  \n");
            sbSQL.append("AND    BUNRU_SEQ = " + SQLString.Format(box.getInt("p_bunru_seq")) + "  \n");
//            connMgr.setOracleCLOB(sbSQL.toString(), v_grcode);
            
            results = insertResouceJikmuInfo(box, connMgr, box.getInt("p_seq"));

            if(isOk > 0) {
            	if(box.getString("p_board_type").equals("M")) {
	                String v_file = box.getNewFileName("p_file1");
	                if(!v_file.equals("")) {
	                    v_file_path = config.getProperty("dir.upload.resource") + "movies\\" + box.getString("p_file_directory") + "\\";
	                    results = controlFile("update", config.getProperty("dir.upload.resource"), box.getNewFileName("p_file1"), v_file_path);            	
	                }
            	} else {
            		results = updateResouceFileInfo(box, connMgr);
            	}
                if(results.equals("OK")) {
                	connMgr.commit();
                } else {
                	connMgr.rollback();
                }
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("results = " + results + "\r\n" + "sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }
    
    /**
	 * �н��ڿ� ���� ����
	 * @param box receive from the form object and session
	 * @param connMgr DBConnectionManager
	 * @param v_seq �н��ڿ� SEQ
	 * @return String ��ϰ��
     **/
    public String updateResouceFileInfo(RequestBox box, DBConnectionManager connMgr) throws Exception { 
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        int isOk2 = 0;
        String results = "OK";
        
        try {
        	sbSQL = new StringBuffer();
        	
        	for(int i = 1 ; i < 7 ; i++) {
        		if(!box.getNewFileName("p_file" + i).equals("")) {
		            sbSQL.setLength(0);
		            sbSQL.append("UPDATE TZ_RESOURCES_FILE  \n");
		            sbSQL.append("SET    REAL_FILENAME = ?  \n");
		            sbSQL.append("     , UP_FILENAME = ?  \n");
		            sbSQL.append("     , LUSERID = ?  \n");
		            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
		            sbSQL.append("WHERE  SEQ = ?  \n");
		            sbSQL.append("AND    FILESEQ = ?  \n");
		            sbSQL.append("AND    BUNRU_SEQ = ?  \n");
		
		            pstmt = connMgr.prepareStatement(sbSQL.toString());
		            int index = 1;
		            pstmt.setString(index++, box.getRealFileName("p_file" + i));
		            pstmt.setString(index++, box.getNewFileName("p_file" + i));
		            pstmt.setString(index++, box.getSession("userid"));
		            pstmt.setInt(index++, box.getInt("p_seq"));
		            pstmt.setInt(index++, i);
		            pstmt.setInt(index++, box.getInt("p_bunru_seq"));
		            isOk2 = pstmt.executeUpdate();
		            
					if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		            
		            if(isOk2 <= 0) {
			            sbSQL.setLength(0);
			            sbSQL.append("INSERT INTO TZ_RESOURCES_FILE  \n");
			            sbSQL.append("      (SEQ  \n");
			            sbSQL.append("     , FILESEQ  \n");
			            sbSQL.append("     , BUNRU_SEQ  \n");
			            sbSQL.append("     , REAL_FILENAME  \n");
			            sbSQL.append("     , UP_FILENAME  \n");
			            sbSQL.append("     , LUSERID  \n");
			            sbSQL.append("     , LDATE)  \n");
			            sbSQL.append("VALUES(?  \n");
			            sbSQL.append("     , ?  \n");
			            sbSQL.append("     , ?  \n");
			            sbSQL.append("     , ?  \n");
			            sbSQL.append("     , ?  \n");
			            sbSQL.append("     , ?  \n");
			            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))  \n");
			
			            pstmt = connMgr.prepareStatement(sbSQL.toString());
			            index = 1;
			            pstmt.setInt(index++, box.getInt("p_seq"));
			            pstmt.setInt(index++, i);
			            pstmt.setInt(index++, box.getInt("p_bunru_seq"));
			            pstmt.setString(index++, box.getRealFileName("p_file" + i));
			            pstmt.setString(index++, box.getNewFileName("p_file" + i));
			            pstmt.setString(index++, box.getSession("userid"));
			            isOk2 = pstmt.executeUpdate();
			            
						if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		            }
		            isOk += isOk2;
        		}
        	}

            if(isOk > 0) {
            	results = "OK";
            }
        } catch(Exception ex) {
            connMgr.rollback();
            results = "ERROR";
            ErrorManager.getErrorStackTrace(ex, box,    sbSQL.toString());
            throw new Exception("results = " + results + "\r\n" + "sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
        }
        return results;
    }
    
    /**
	 * �н��ڿ� ����
	 * @param box receive from the form object and session
	 * @return int ���
     **/
    public int deleteResourceInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_RESOURCES_INFO  \n");
            sbSQL.append("SET    DEL_FLAG = 'Y'  \n");
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("WHERE  SEQ = ?  \n");
            sbSQL.append("AND    BUNRU_SEQ = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setString(index++, box.getString("p_seq"));
            pstmt.setString(index++, box.getString("p_bunru_seq"));
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
	 * �н��ڿ� ��ȸ�� ����
	 * @param box receive from the form object and session
	 * @return int ���
     **/
    public int updateResourceVisitCnt(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_RESOURCES_INFO  \n");
            sbSQL.append("SET    VISIT = VISIT + 1  \n");
            sbSQL.append("WHERE  SEQ = ?  \n");
            sbSQL.append("AND    BUNRU_SEQ = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getString("p_seq"));
            pstmt.setString(index++, box.getString("p_bunru_seq"));
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
	 * �н��ڿ� �����н��ڿ� ���
	 * @param box receive from the form object and session
	 * @return int ���
     **/
    public int insertResourceInterests(RequestBox box) throws Exception { 
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
                sbSQL.append("DELETE FROM TZ_RESOURCES_INTEREST  \n");
                sbSQL.append("WHERE  SEQ = ?  \n");
                sbSQL.append("AND    BUNRU_SEQ = ?  \n");
                sbSQL.append("AND    USERID = ?  \n");

                pstmt = connMgr.prepareStatement(sbSQL.toString());
                index = 1;
                pstmt.setString(index++, (String)v_interests.elementAt(i));
                pstmt.setString(index++, box.getString("p_bunru_seq"));
                pstmt.setString(index++, box.getSession("userid"));
                isOk += pstmt.executeUpdate();
                
    			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
                
            	sbSQL.setLength(0);
                sbSQL.append("INSERT INTO TZ_RESOURCES_INTEREST  \n");
                sbSQL.append("      (SEQ  \n");
                sbSQL.append("     , BUNRU_SEQ  \n");
                sbSQL.append("     , USERID  \n");
                sbSQL.append("     , LDATE)  \n");
                sbSQL.append("VALUES(?  \n");
                sbSQL.append("     , ?  \n");
                sbSQL.append("     , ?  \n");
                sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))  \n");

                pstmt = connMgr.prepareStatement(sbSQL.toString());
                index = 1;
                pstmt.setString(index++, (String)v_interests.elementAt(i));
                pstmt.setString(index++, box.getString("p_bunru_seq"));
                pstmt.setString(index++, box.getSession("userid"));
                isOk += pstmt.executeUpdate();
                
    			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
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
	 * �н��ڿ� �α׵��
	 * @param box receive from the form object and session
	 * @return int ���
     **/
    public int insertResourceLog(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sbSQL = new StringBuffer();
            sbSQL.append("INSERT INTO TZ_RESOURCES_LOG  \n");
            sbSQL.append("SELECT NVL(MAX(LOG_SEQ), 0) + 1  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("FROM   TZ_RESOURCES_LOG  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getString("p_seq"));
            pstmt.setString(index++, box.getString("p_bunru_seq"));
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
	 * �н��ڿ� ����Ʈ (����)
	 * @param box          receive from the form object and session
	 * @return ArrayList   �н��ڿ� ����Ʈ
     **/
    public ArrayList selectResourceMainList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	String v_userid = box.getSession("userid");
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT *										  										\n");
            sbSQL.append("FROM (										  										\n");
            sbSQL.append("	SELECT BUNRU_SEQ								  									\n");
            sbSQL.append("	     , SEQ								  											\n");
            sbSQL.append("	     , (SELECT BUNRU FROM TZ_RESOURCES_TYPE WHERE A.BUNRU_SEQ = BUNRU_SEQ) BUNRUNM  \n");
            sbSQL.append("	     , A.CATEGORY  								  									\n");
            sbSQL.append("	     , GET_CODENM('0093', A.CATEGORY) AS CATEGORYNM  				  				\n");
            sbSQL.append("	     , TITLE  									  									\n");
            sbSQL.append("		 , DECODE(MAIN_PAGE, '', 'N', 'Y') AS POPYN				  						\n");
            sbSQL.append("	     , MAIN_PAGE  								  									\n");
            sbSQL.append("	     , WIDTH  									  									\n");
            sbSQL.append("	     , HEIGHT  									  									\n");
            sbSQL.append("	     , FILE_DIRECTORY  								  								\n");
            sbSQL.append("	     ,(SELECT UP_FILENAME  							  								\n");
            sbSQL.append("	       FROM   TZ_RESOURCES_FILE  						  							\n");
            sbSQL.append("	       WHERE  SEQ = A.SEQ  							  								\n");
            sbSQL.append("	       AND    BUNRU_SEQ = A.BUNRU_SEQ  						  						\n");
            sbSQL.append("	       AND    FILESEQ = 1) AS UP_FILENAME  					 					 	\n");
            sbSQL.append("	     ,(SELECT REAL_FILENAME  							  							\n");
            sbSQL.append("	       FROM   TZ_RESOURCES_FILE  						  							\n");
            sbSQL.append("	       WHERE  SEQ = A.SEQ  							  								\n");
            sbSQL.append("	       AND    BUNRU_SEQ = A.BUNRU_SEQ  						  						\n");
            sbSQL.append("	       AND    FILESEQ = 1) AS REAL_FILENAME					  						\n");
            sbSQL.append("	     , RANK() OVER (PARTITION BY A.CATEGORY ORDER BY SEQ DESC) AS RANK 			\n");
            sbSQL.append("	FROM TZ_RESOURCES_INFO A, (SELECT NVL(GRCODE,',') AS GRCODE							\n");
            sbSQL.append("								FROM (									   				\n");
            sbSQL.append("									SELECT Q.GRCODE							   			\n");
            sbSQL.append("									FROM TZ_MEMBER P, TZ_GRCOMP Q					   	\n");
            sbSQL.append("									WHERE P.COMP = Q.COMP						   		\n");
            sbSQL.append("									AND P.USERID = " + StringManager.makeSQL(v_userid) + "\n");
            sbSQL.append("									UNION ALL							   				\n");
            sbSQL.append("									SELECT ''							   				\n");
            sbSQL.append("									FROM DUAL							   				\n");
            sbSQL.append("									)								   					\n");
            sbSQL.append("								) B									   					\n");
            sbSQL.append("							, (SELECT CODE AS CATEGORY FROM TZ_CODE WHERE GUBUN = '0093' AND ROWNUM < 5) C	   \n");
            sbSQL.append("	WHERE INSTR(A.GRCODE, B.GRCODE) > 0												    \n");
            sbSQL.append("	  AND A.CATEGORY = C.CATEGORY													    \n");
            sbSQL.append("	  AND A.DEL_FLAG = 'N'															    \n");
            sbSQL.append("	GROUP BY BUNRU_SEQ								  									\n");
            sbSQL.append("		   , SEQ								  										\n");
            sbSQL.append("		   , A.CATEGORY								  									\n");
            sbSQL.append("		   , TITLE								  										\n");
            sbSQL.append("		   , MAIN_PAGE								  									\n");
            sbSQL.append("		   , WIDTH								  										\n");
            sbSQL.append("		   , HEIGHT								  										\n");
            sbSQL.append("		   , FILE_DIRECTORY							  									\n");
            sbSQL.append("		   , LDATE								  										\n");
            sbSQL.append("	)										  											\n");
            sbSQL.append("WHERE RANK < 4									  									\n");
            
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
	 * �������� ����Ʈ
	 * @param box          receive from the form object and session
	 * @return ArrayList   �������� ����Ʈ
     **/
    public ArrayList selectOpenSubjList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	String sql1 = null;
    	String sql2 = null;
    	String v_grcode = box.getStringDefault("s_grcode", "ALL");
    	int v_pageno = box.getInt("p_pageno");
    	String v_userid = box.getSession("userid");
    	String v_jobcd = box.getSession("jobcd");
    	String v_upperclass = box.getStringDefault("s_upperclass","ALL");
    	String v_middleclass = box.getStringDefault("s_middleclass","ALL");
    	String v_lowerclass = box.getStringDefault("s_lowerclass","ALL");
    	String v_subSql = "";
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		
    		if (!v_upperclass.equals("ALL")) {
    			v_subSql = "\n and    c.upperclass = " + SQLString.Format(v_upperclass);
    		}
    		if (!v_middleclass.equals("ALL")) {
    			v_subSql += "\n and    c.upperclass = " + SQLString.Format(v_middleclass);
    		}
    		if (!v_lowerclass.equals("ALL")) {
    			v_subSql += "\n and    c.lowerclass = " + SQLString.Format(v_lowerclass);
    		}
    		
    		v_subSql = 
    		sql1= "\n select c.subj "
    			+ "\n      , c.subjnm "
    			+ "\n      , c.indate "
    			+ "\n      , c.introducefilenamereal "
    			+ "\n      , c.introducefilenamenew "
    			+ "\n      , c.isoutsourcing "
    			+ "\n      , c.contenttype "
    			+ "\n      , c.eduurl "
    			+ "\n      , c.intro "
    			+ "\n      , c.explain "
    			+ "\n      , get_subjclassnm(c.upperclass,'000','000') as upperclassnm "
    			+ "\n      , get_subjclassnm(c.upperclass,c.middleclass,'000') as middleclassnm "
    			+ "\n      , get_subjclassnm(c.upperclass,c.middleclass,c.lowerclass) as lowerclassnm "
    			+ "\n      , (select job_cd "
    			+ "\n         from   tz_subjjikmu "
    			+ "\n         where  year = '" + FormatDate.getDate("yyyy") + "' "
    			+ "\n         and    subj = c.subj "
    			+ "\n         and    job_cd = '" + v_jobcd + "') as job_cd "
    			+ "\n      , (select cnt "
    			+ "\n         from   tz_subj_cnt "
    			+ "\n         where  subj = c.subj) as cnt "
    			+ "\n from   tz_grcomp a, tz_grsubj b, tz_subj c "
    			+ "\n where  a.comp in (select comp from tz_member where userid = '" + v_userid + "') "
    			+ "\n and    a.grcode = b.grcode "
    			+ "\n and    b.subjcourse = c.subj "
    			+ "\n and    c.isopenedu = 'Y' "
    			+ "\n and    c.isuse = 'Y' "
    			+ "\n and    c.isvisible = 'Y' "
    			+ "\n and    c.isonoff = 'ON' "
    			+ v_subSql
    			+ "\n order  by c.indate desc ";

    		ls = connMgr.executeQuery(sql1);
            ls.setPageSize(row);					// �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);			// ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();	// ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount = ls.getTotalCount();	// ��ü row ���� ��ȯ�Ѵ�

            while(ls.next()) {
    			dbox = ls.getDataBox();
                dbox.put("d_dispnum",   new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(row));
                dbox.put("d_totalrowcount",  new Integer(totalrowcount));
                
    			list.add(dbox);
    		}
    		
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sql1);
    		throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) {} }
    	}
    	return list;
    }

    /**
	 * ���ɿ������� ���
	 * @param box receive from the form object and session
	 * @return int ���
     **/
    public int saveConcernOpenSubj(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = null;
        int isOk = 0;
        int index = 1;
        Vector v_checks = box.getVector("p_checks");
        String v_userid = box.getSession("userid");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            for(int i = 0 ; i < v_checks.size() ; i++) {
            	
            	sql = "\n delete from tz_subj_concern "
            		+ "\n where  subj = ? "
            		+ "\n and    userid = ? ";
            	
                pstmt = connMgr.prepareStatement(sql);
                
                index = 1;
                pstmt.setString(index++, (String)v_checks.elementAt(i));
                pstmt.setString(index++, v_userid);
                isOk += pstmt.executeUpdate();
                
                sql = "\n insert into tz_subj_concern "
                	+ "\n       ( "
                	+ "\n        userid "
                	+ "\n      , subj " 
                	+ "\n      , luserid "
                	+ "\n      , ldate "
                	+ "\n       ) "
                	+ "\n values( "
                	+ "\n        ? "
                	+ "\n      , ? "
                	+ "\n      , ? "
                	+ "\n      , to_char(sysdate, 'yyyymmddhh24miss') "
                	+ "\n       ) ";
                
                pstmt = connMgr.prepareStatement(sql);
                index = 1;
                pstmt.setString(index++, v_userid);
                pstmt.setString(index++, (String)v_checks.elementAt(i));
                pstmt.setString(index++, v_userid);
                isOk += pstmt.executeUpdate();
    			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            }

            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }

    /**
	 * �������� ��ȸ�� ����
	 * @param box receive from the form object and session
	 * @return int ���
     **/
    public int saveOpenSubjCnt(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        String sql = null;
        int isOk = 0;

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql = "\n merge into "
            	+ "\n     tz_subj_cnt a "
            	+ "\n using "
            	+ "\n     (select '" + box.getString("p_subj") + "' as subj from dual) b "
            	+ "\n on "
            	+ "\n     (a.subj = b.subj) "
            	+ "\n when matched then "
            	+ "\n     update set cnt = nvl(cnt,0) + 1 "
            	+ "\n when not matched then "
            	+ "\n     insert (subj, cnt) values('" + box.getString("p_subj") + "',1) ";

            pstmt = connMgr.prepareStatement(sql);
            
            isOk = pstmt.executeUpdate();

            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
        } catch(Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if(pstmt != null) { try { pstmt.close(); } catch(Exception e) { } }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch(Exception e) { }
            if(connMgr != null) { try { connMgr.freeConnection(); } catch(Exception e10) { } }
        }
        return isOk;
    }

    
    /**
	 * �н��ڿ� �˻� ����Ʈ
	 * @param box          receive from the form object and session
	 * @return ArrayList   �н��ڿ� �˻� ����Ʈ
     **/
    public ArrayList searchresourcelist(RequestBox box) throws Exception { 
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	int v_pageno = box.getInt("p_pageno");
    	String v_bunru_seq = box.getString("p_bunru_seq");
    	String v_keyword = box.getString("main_search");
    	String v_userid = box.getSession("userid");
    	
    	System.out.println("Bean ~~~ : "+v_keyword);
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();

            sbSQL.append("\n SELECT DISTINCT GUBUN			");
            sbSQL.append("\n      , BUNRU_SEQ				");
            sbSQL.append("\n      , BUNRUNM				");
            sbSQL.append("\n      , TITLE				");
            sbSQL.append("\n      , CATEGORYNM				");
            sbSQL.append("\n      , SUBJ_GU				");
            sbSQL.append("\n      , WIDTH				");
            sbSQL.append("\n      , HEIGHT				");
            sbSQL.append("\n 	 , LDATE				");
            sbSQL.append("\n FROM   VZ_RESOURCES_INFO A			");
            sbSQL.append("\n 	 , (SELECT Q.GRCODE 			");
            sbSQL.append("\n 	    FROM TZ_MEMBER P, TZ_GRCOMP Q	");
            sbSQL.append("\n 		WHERE P.COMP = Q.COMP		");
            sbSQL.append("\n 		  AND P.USERID = " + SQLString.Format(v_userid) + "		");
            sbSQL.append("\n 		) B				");
            sbSQL.append("\n WHERE  INSTR(A.GRCODE, B.GRCODE) > 0	");

            v_keyword = StringManager.replace(v_keyword, "'", "''");
            if(v_keyword.indexOf("%") > 0 || v_keyword.indexOf("_") > 0) {
            	v_keyword = StringManager.replace(v_keyword, "%", "/%");
            	v_keyword = StringManager.replace(v_keyword, "_", "/_");
            	sbSQL.append("\n AND    UPPER(TITLE) LIKE '%" + v_keyword.toUpperCase() + "%' escape '/' \n");
            } else {
            	sbSQL.append("\n AND    UPPER(TITLE) LIKE '%" + v_keyword.toUpperCase() + "%'  \n");
            }
            if(!(v_bunru_seq.equals("ALL")||v_bunru_seq.equals(""))) {
            	sbSQL.append("\n AND GUBUN = " + SQLString.Format(v_bunru_seq) + " \n");
            }
            sbSQL.append("ORDER BY SEQ DESC  \n");    		

            System.out.println(sbSQL.toString());
    		ls = connMgr.executeQuery(sbSQL.toString());
            ls.setPageSize(row);					// �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);			// ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();	// ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount = ls.getTotalCount();	// ��ü row ���� ��ȯ�Ѵ�
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
                dbox.put("d_dispnum",   new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(row));
                dbox.put("d_totalrowcount", new Integer(totalrowcount));
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
}