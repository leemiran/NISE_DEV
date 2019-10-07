package com.ziaan.system;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;


public class QueryStatisticsBean {
	
    public QueryStatisticsBean() {
    }
    
    /**
	 * 종합통계 리스트
	 * @param box          receive from the form object and session
	 * @return ArrayList   종합통계 리스트
     **/
    public ArrayList selectQueryStatisticsList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		
            sbSQL.append("SELECT STATISTIC_SEQ  \n");
            sbSQL.append("     , TITLE  \n");
            sbSQL.append("     , SUMMARY  \n");
            sbSQL.append("     , SEARCH_CONDITION  \n");
            sbSQL.append("     , ESSENTIAL_CONDITION  \n");
            sbSQL.append("FROM   TZ_STATISTIC_QUERY  \n");
            sbSQL.append("WHERE  ISDELETED != 'Y'  \n");
            sbSQL.append("AND    DECODE(NVL(GUBUN, '1'), '', '1', NVL(GUBUN, '1')) = " + box.getStringDefault("p_gubun", "1") + "  \n");
            sbSQL.append("ORDER BY STATISTIC_SEQ DESC  \n");
    		
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
	 * 종합통계 정보
	 * @param box          receive from the form object and session
	 * @return DataBox   	종합통계 정보
     **/
    public DataBox selectQueryStatisticInfo(DBConnectionManager connMgr, RequestBox box) throws Exception {
    	ListSet ls = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		sbSQL = new StringBuffer();
    		dbox = new DataBox("");
    		
            sbSQL.append("SELECT STATISTIC_SEQ  \n");
            sbSQL.append("     , RUN_QUERY  \n");
            sbSQL.append("     , SEARCH_CONDITION  \n");
            sbSQL.append("FROM   TZ_STATISTIC_QUERY  \n");
            sbSQL.append("WHERE  STATISTIC_SEQ = " + SQLString.Format(box.getString("p_statistic_seq")) + "  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			dbox = ls.getDataBox();
    		}
    	} catch(Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
    		throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); } catch(Exception e) {} }
    	}
    	return dbox;
    }
    
    /**
	 * 종합통계 정보
	 * @param box          receive from the form object and session
	 * @return DataBox   	종합통계 정보
     **/
    public DataBox selectQueryStatisticInfo(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		sbSQL = new StringBuffer();
    		dbox = new DataBox("");
    		
            sbSQL.append("SELECT STATISTIC_SEQ  \n");
            sbSQL.append("     , TITLE  \n");
            sbSQL.append("     , SUMMARY  \n");
            sbSQL.append("     , RUN_QUERY  \n");
            sbSQL.append("     , SEARCH_CONDITION  \n");
            sbSQL.append("     , ESSENTIAL_CONDITION  \n");
            sbSQL.append("FROM   TZ_STATISTIC_QUERY  \n");
            sbSQL.append("WHERE  STATISTIC_SEQ = " + SQLString.Format(box.getString("p_statistic_seq")) + "  \n");
    		
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
	 * 종합통계 리스트
	 * @param box          receive from the form object and session
	 * @return ArrayList   종합통계 리스트
     **/
    public ArrayList selectStatisticsList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		dbox = selectQueryStatisticInfo(connMgr, box);
    		if(dbox != null && !dbox.getString("d_run_query").equals("")) {
	    		sbSQL.append(dbox.getString("d_run_query") + "\n");
	    		String v_search_condition = dbox.getString("d_search_condition");
	    		
	    		if(v_search_condition.indexOf("grcode") > -1 && !(box.getString("s_grcode").equals("") || box.getString("s_grcode").equals("ALL"))) {
	    			sbSQL.append("AND    GRCODE = " + SQLString.Format(box.getString("s_grcode")) + "  \n");
	    		}
	    		
	    		if(v_search_condition.indexOf("gyear") > -1 && !(box.getString("s_gyear").equals("") || box.getString("s_gyear").equals("ALL"))) {
	    			sbSQL.append("AND    GYEAR = " + SQLString.Format(box.getString("s_gyear")) + "  \n");
	    		}
	    		
	    		if(v_search_condition.indexOf("grseq") > -1 && !(box.getString("s_grseq").equals("") || box.getString("s_grseq").equals("ALL"))) {
	    			sbSQL.append("AND    GRSEQ = " + SQLString.Format(box.getString("s_grseq")) + "  \n");
	    		}
	    		
	    		if(v_search_condition.indexOf("subjclass") > -1 && !(box.getString("s_upperclass").equals("") || box.getString("s_upperclass").equals("ALL"))) {
	    			sbSQL.append("AND    UPPERCLASS = " + SQLString.Format(box.getString("s_upperclass")) + "  \n");
	    		}
	    		
	    		if(v_search_condition.indexOf("subjclass") > -1 && !(box.getString("s_middleclass").equals("") || box.getString("s_middleclass").equals("ALL"))) {
	    			sbSQL.append("AND    MIDDLECLASS = " + SQLString.Format(box.getString("s_middleclass")) + "  \n");
	    		}
	    		
	    		if(v_search_condition.indexOf("subjclass") > -1 && !(box.getString("s_lowerclass").equals("") || box.getString("s_lowerclass").equals("ALL"))) {
	    			sbSQL.append("AND    LOWERCLASS = " + SQLString.Format(box.getString("s_lowerclass")) + "  \n");
	    		}
	    		
	    		if(v_search_condition.indexOf("subjsearch") > -1 && !(box.getString("s_subjcourse").equals("") || box.getString("s_subjcourse").equals("ALL"))) {
	    			sbSQL.append("AND    SUBJ = " + SQLString.Format(box.getString("s_subjcourse")) + "  \n");
	    		}
	    		
	    		if(v_search_condition.indexOf("grseq") > -1 && !(box.getString("s_gyear").equals("") || box.getString("s_gyear").equals("ALL"))) {
	    			sbSQL.append("AND    YEAR = " + SQLString.Format(box.getString("s_gyear")) + "  \n");
	    		}
	    		
	    		if(v_search_condition.indexOf("subjseq") > -1 && !(box.getString("s_subjseq").equals("") || box.getString("s_subjseq").equals("ALL"))) {
	    			sbSQL.append("AND    SUBJSEQ = " + SQLString.Format(box.getString("s_subjseq")) + "  \n");
	    		}
	    		
	    		if(v_search_condition.indexOf("compgubun") > -1 && !(box.getString("s_compgubun").equals("") || box.getString("s_compgubun").equals("ALL"))) {
	    			sbSQL.append("AND    COMPGUBUN = " + SQLString.Format(box.getString("s_compgubun")) + "  \n");
	    		}
	    		
	    		if(v_search_condition.indexOf("compsearch") > -1 && !(box.getString("s_company").equals("") || box.getString("s_company").equals("ALL"))) {
	    			sbSQL.append("AND    COMP = " + SQLString.Format(box.getString("s_company")) + "  \n");
	    		}
	    		
	    		if(v_search_condition.indexOf("startfrom") > -1 && !box.getString("s_startfrom").equals("")) {
	    			sbSQL.append("AND    SUBSTR(EDUSTART, 1, 8) >= " + SQLString.Format(box.getString("s_startfrom")) + "  \n");
	    		}
	    		
	    		if(v_search_condition.indexOf("startto") > -1 && !box.getString("s_startto").equals("")) {
	    			sbSQL.append("AND    SUBSTR(EDUSTART, 1, 8) <= " + SQLString.Format(box.getString("s_startto")) + "  \n");
	    		}
	    		
	    		if(v_search_condition.indexOf("endfrom") > -1 && !box.getString("s_endfrom").equals("")) {
	    			sbSQL.append("AND    SUBSTR(EDUEND, 1, 8) >= " + SQLString.Format(box.getString("s_endfrom")) + "  \n");
	    		}
	    		
	    		if(v_search_condition.indexOf("endto") > -1 && !box.getString("s_endto").equals("")) {
	    			sbSQL.append("AND    SUBSTR(EDUEND, 1, 8) <= " + SQLString.Format(box.getString("s_endto")) + "  \n");
	    		}
	    		
	    		if(!box.getString("p_orderCol").equals("")) {
	    			sbSQL.append("ORDER BY " + box.getString("p_orderCol") + " " + box.getString("p_orderType") + "  \n");
	    		}
	    		System.out.println("aaaaaaaaaaaa:"+sbSQL);
	    		ls = connMgr.executeQuery(sbSQL.toString());
	            ResultSetMetaData meta = ls.getMetaData();
	            int columnCount = meta.getColumnCount();
	            String[] columNames = new String[columnCount];
	            for ( int i = 1; i <= columnCount; i++ ) {
	                String columnName = meta.getColumnName(i).toLowerCase();
	                columNames[(i - 1)] = columnName;
	            }
	            list.add(columNames);
	    		while(ls.next()) {
	    			dbox = ls.getDataBox();
	    			list.add(dbox);
	    		}
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
	 * 종합통계 쿼리 수정
	 * @param box receive from the form object and session
	 * @return int 등록결과
     **/
    public int updateStatisticQueryInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            Vector v_search_conditions = box.getVector("p_search_condition");
            Vector v_essential_conditions = box.getVector("p_essential_condition");
            String v_search_condition = "";
            String v_essential_condition = "";
            
            for(int i = 0 ; i < v_search_conditions.size() ; i++) {
            	v_search_condition += v_search_conditions.elementAt(i) + ",";
            }
            
            for(int i = 0 ; i < v_essential_conditions.size() ; i++) {
            	v_essential_condition += v_essential_conditions.elementAt(i) + ",";
            }

            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_STATISTIC_QUERY  \n");
            sbSQL.append("SET    TITLE = ?  \n");
            sbSQL.append("     , SUMMARY = ?  \n");
            sbSQL.append("     , RUN_QUERY = ?  \n");
//            sbSQL.append("     , RUN_QUERY = EMPTY_CLOB()  \n");
            sbSQL.append("     , SEARCH_CONDITION = ?  \n");
            sbSQL.append("     , ESSENTIAL_CONDITION = ?  \n");
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("WHERE  STATISTIC_SEQ = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getString("p_title"));
            pstmt.setString(index++, box.getString("p_summary"));
            
            pstmt.setString(index++, box.getString("p_run_query"));
            
            pstmt.setString(index++, v_search_condition);
            pstmt.setString(index++, v_essential_condition);
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setInt(index++, box.getInt("p_statistic_seq"));
            isOk = pstmt.executeUpdate();
            
            sbSQL.setLength(0);
            sbSQL.append("SELECT RUN_QUERY  \n");
            sbSQL.append("FROM   TZ_STATISTIC_QUERY  \n");
            sbSQL.append("WHERE  STATISTIC_SEQ = " + SQLString.Format(box.getInt("p_statistic_seq")) + "  \n");
//            connMgr.setOracleCLOB(sbSQL.toString(), box.getString("p_run_query"));

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
	 * 종합통계 쿼리 삭제
	 * @param box receive from the form object and session
	 * @return int 등록결과
     **/
    public int deleteStatisticQueryInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL = new StringBuffer();
            sbSQL.append("UPDATE TZ_STATISTIC_QUERY  \n");
            sbSQL.append("SET    ISDELETED = 'Y'  \n");
            sbSQL.append("     , LUSERID = ?  \n");
            sbSQL.append("     , LDATE = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')  \n");
            sbSQL.append("WHERE  STATISTIC_SEQ = ?  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setString(index++, box.getSession("userid"));
            pstmt.setInt(index++, box.getInt("p_statistic_seq"));
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
	 * 종합통계 쿼리 등록
	 * @param box receive from the form object and session
	 * @return int 등록결과
     **/
    public int insertStatisticQueryInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        StringBuffer sbSQL = null;
        int isOk = 0;
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            Vector v_search_conditions = box.getVector("p_search_condition");
            Vector v_essential_conditions = box.getVector("p_essential_condition");
            String v_search_condition = "";
            String v_essential_condition = "";
            int v_statistic_seq = 0;
            
            for(int i = 0 ; i < v_search_conditions.size() ; i++) {
            	v_search_condition += v_search_conditions.elementAt(i) + ",";
            }
            
            for(int i = 0 ; i < v_essential_conditions.size() ; i++) {
            	v_essential_condition += v_essential_conditions.elementAt(i) + ",";
            }
            
            sbSQL = new StringBuffer();
            sbSQL.append("SELECT NVL(MAX(STATISTIC_SEQ), 0) + 1 AS STATISTIC_SEQ  \n");
            sbSQL.append("FROM   TZ_STATISTIC_QUERY  \n");
    		
    		ls = connMgr.executeQuery(sbSQL.toString());
    		
    		while(ls.next()) {
    			v_statistic_seq = ls.getInt("statistic_seq");
    		}
            
            sbSQL.setLength(0);
            sbSQL.append("INSERT INTO TZ_STATISTIC_QUERY  \n");
            sbSQL.append("      (STATISTIC_SEQ  \n");
            sbSQL.append("     , GUBUN  \n");
            sbSQL.append("     , TITLE  \n");
            sbSQL.append("     , SUMMARY  \n");
            sbSQL.append("     , RUN_QUERY  \n");
            sbSQL.append("     , SEARCH_CONDITION  \n");
            sbSQL.append("     , ESSENTIAL_CONDITION  \n");
            sbSQL.append("     , ISDELETED  \n");
            sbSQL.append("     , LUSERID  \n");
            sbSQL.append("     , LDATE)  \n");
            sbSQL.append("VALUES(?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
//            sbSQL.append("     , EMPTY_CLOB()  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , 'N'  \n");
            sbSQL.append("     , ?  \n");
            sbSQL.append("     , TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))  \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            int index = 1;
            pstmt.setInt(index++, v_statistic_seq);
            pstmt.setString(index++, box.getStringDefault("p_gubun", "1"));
            pstmt.setString(index++, box.getString("p_title"));
            pstmt.setString(index++, box.getString("p_summary"));

            pstmt.setString(index++, box.getString("p_run_query"));
            
            pstmt.setString(index++, v_search_condition);
            pstmt.setString(index++, v_essential_condition);
            pstmt.setString(index++, box.getSession("userid"));
            isOk = pstmt.executeUpdate();
            
            sbSQL.setLength(0);
            sbSQL.append("SELECT RUN_QUERY  \n");
            sbSQL.append("FROM   TZ_STATISTIC_QUERY  \n");
            sbSQL.append("WHERE  STATISTIC_SEQ = " + SQLString.Format(v_statistic_seq) + "  \n");
//            connMgr.setOracleCLOB(sbSQL.toString(), box.getString("p_run_query"));

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
	 * 종합통계 리스트
	 * @param box          receive from the form object and session
	 * @return ArrayList   종합통계 리스트
     **/
    public ArrayList selectStatisticsList2(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls = null;
    	ArrayList list = null;
    	DataBox dbox = null;
    	StringBuffer sbSQL = null;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sbSQL = new StringBuffer();
    		if(!box.getString("p_run_query").equals("")) {
	    		sbSQL.append(box.getString("p_run_query") + "\n");
	    		ls = connMgr.executeQuery(sbSQL.toString());
	            ResultSetMetaData meta = ls.getMetaData();
	            int columnCount = meta.getColumnCount();
	            String[] columNames = new String[columnCount];
	            for ( int i = 1; i <= columnCount; i++ ) {
	                String columnName = meta.getColumnName(i).toLowerCase();
	                columNames[(i - 1)] = columnName;
	            }
	            list.add(columNames);
	    		while(ls.next()) {
	    			dbox = ls.getDataBox();
	    			list.add(dbox);
	    		}
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