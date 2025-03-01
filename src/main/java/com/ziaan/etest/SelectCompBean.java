//**********************************************************
//1. f      ��: ȸ�� v�� ����
//2. �wα׷���: SelectCompBean.java
//3. ��      ��:
//4. ȯ      ��: JDK 1.3
//5. ��      o: 0.1
//6. ��      ��: Administrator 2003-08-29
//7. ��      d: 
//                 
//********************************************************** 
 
package com.ziaan.etest;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;

import com.ziaan.common.GetCodenm;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SelectCompBean {


    /**
	ȸ�纰 
    @param box          receive from the form object and session
    @return String
    */
public static String getCompany(RequestBox box, boolean isChange, boolean isALL) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;        
        ListSet ls = null;
        String sql = "";
        String result = "";
        
        try {
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");        
            
            String ss_company = box.getStringDefault("s_company","ALL");   
            
            connMgr = new DBConnectionManager();            
            
            if(v_gadmin.equals("H")) {		//	��0�׷����
                sql = "select distinct a.comp, a.companynm";
                sql += " from tz_comp a, tz_grcomp b,";
                sql += " (select grcode from tz_grcodeman where userid = " + SQLString.Format(s_userid) + " and gadmin = " + SQLString.Format(s_gadmin) + ") c";
                sql += " where a.comp = b.comp and b.grcode = c.grcode and a.comptype = '2'";
                sql += " order by a.comp";  		
            }
            else if(v_gadmin.equals("K")) {		//	ȸ�����, �μ�����
                sql = "select distinct a.comp, a.companynm";
                sql += " from tz_comp a, tz_compman b";
                sql += " where substr(a.comp, 1, 4) = substr(b.comp, 1, 4) and b.userid = " + SQLString.Format(s_userid) + " and b.gadmin = " + SQLString.Format(s_gadmin);
                sql += "  and a.comptype = '2' order by a.comp"; 		
            }		
            else {		//	Ultravisor, Supervisor, ��d����, ����
                sql = "select distinct comp, companynm";
                sql += " from tz_comp where comptype = '2'";	
                sql += " order by comp";  	 		
            }
            
            ls = connMgr.executeQuery(sql);  
            
            if(v_gadmin.equals("A") || v_gadmin.equals("F") || v_gadmin.equals("P") || v_gadmin.equals("H")) {     
                 //      Ultra/Super or ��d���� or ���� or ��0�׷���� �� ��� 'ALL' ȸ�� ��� 
                result = getSelectTag(ls, isChange, true, "s_company", ss_company);
            }
            else {
                result = getSelectTag(ls, isChange, false, "s_company", ss_company);
            }
        }
        catch (Exception ex) {ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }    
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return result;
    }  
    
    public static String getGpm(RequestBox box, boolean isChange, boolean isALL) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;        
        ListSet ls = null;
        String sql = "";
        String result = "";
        
        try {
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_company  = box.getStringDefault("s_company", "ALL");      //  �ش� ȸ���� comp code
            String  ss_gpm  = box.getStringDefault("s_gpm", "ALL");      //  �ش� ȸ���� comp code
            
            connMgr = new DBConnectionManager();            
            
            if(s_gadmin.equals("K6") || s_gadmin.equals("K7")) {		//	�μ����� or �μ���
                sql = "select distinct a.comp, a.gpmnm";
                sql += " from tz_comp a, tz_compman b";
                sql += " where substr(a.comp, 1, 6) = substr(b.comp, 1, 6) and b.userid = " + SQLString.Format(s_userid) + " and b.gadmin = " + SQLString.Format(s_gadmin);
                
                if( !ss_company.equals("ALL")) {
                    sql += " and a.comp like '" + GetCodenm.get_compval(ss_company) + "'";
                }
                sql += " and a.comptype = '3' order by a.comp";  		
            }
            else {
                sql = "select distinct comp, gpmnm";
                sql += " from tz_comp where comptype = '3'";
                
                if( !ss_company.equals("ALL")) {
                    sql += " and comp like '" + GetCodenm.get_compval(ss_company) + "'";
                }
                sql += " order by comp";  	
            }

            ls = connMgr.executeQuery(sql); 
            
            if( !s_gadmin.equals("K6") && !s_gadmin.equals("K7")) {		//	�μ����� or �μ��� �� �ƴѰ�� 'ALL'  ���� ��� 
                result = getSelectTag(ls, isChange, true, "s_gpm", ss_gpm);
            }
            else {
                result = getSelectTag(ls, isChange, false, "s_gpm", ss_gpm);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return result;
    }  
    

    /**
	�μ��� 
    @param box          receive from the form object and session
    @return String
    */
    public static String getDept(RequestBox box, boolean isChange, boolean isALL) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;        
        ListSet ls = null;
        String sql = "";
        String result = "";
        
        try {
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_company  = box.getStringDefault("s_company", "ALL");      //  �ش� ȸ���� comp code
            String  ss_gpm  = box.getStringDefault("s_gpm", "ALL");     
            String  ss_dept  = box.getStringDefault("s_dept", "ALL");     

            connMgr = new DBConnectionManager();            
            
            if(s_gadmin.equals("K6") || s_gadmin.equals("K7")) {		//	�μ����� or �μ���
                sql = "select distinct a.comp, a.deptnm";
                sql += " from tz_comp a, tz_compman b";
                sql += " where substr(a.comp, 1, 8) = substr(b.comp, 1, 8) and b.userid = " + SQLString.Format(s_userid) + " and b.gadmin = " + SQLString.Format(s_gadmin);
                
                if( !ss_gpm.equals("ALL")) {
                    sql += " and a.comp like '" + GetCodenm.get_compval(ss_gpm) + "'";
                }
                sql += " and a.comptype = '4'  order by a.comp";  		
            }
            else {
                sql = "select distinct comp, deptnm";
                sql += " from tz_comp where comptype = '4'";
    			
                if( !ss_gpm.equals("ALL")) {
                	sql += " and comp like '" + GetCodenm.get_compval(ss_gpm) + "'";               	 	
                }	
                sql += " order by comp";  
            }

            ls = connMgr.executeQuery(sql); 
            
            if( !s_gadmin.equals("K6") && !s_gadmin.equals("K7")) {		//	�μ����� or �μ��� �� �ƴѰ�� 'ALL'  ���� ��� 
                result = getSelectTag(ls, isChange, true, "s_dept", ss_dept);
            }
            else {
                result = getSelectTag(ls, isChange, false, "s_dept", ss_dept);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return result;
    } 
    
    public static String getPart(RequestBox box, boolean isChange, boolean isALL) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;        
        ListSet ls = null;
        String sql = "";
        String result = "";
        
        try {
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_company  = box.getStringDefault("s_company", "ALL");      //  �ش� ȸ���� comp code
            String  ss_gpm  = box.getStringDefault("s_gpm", "ALL");     
            String  ss_dept  = box.getStringDefault("s_dept", "ALL");     
            String  ss_part  = box.getStringDefault("s_part", "ALL");    

            connMgr = new DBConnectionManager();            
            
            if(s_gadmin.equals("K6") || s_gadmin.equals("K7")) {		//	�μ����� or �μ���
                sql = "select distinct a.comp, a.partnm";
                sql += " from tz_comp a, tz_compman b";
                sql += " where substr(a.comp, 1, 8) = substr(b.comp, 1, 8) and b.userid = " + SQLString.Format(s_userid) + " and b.gadmin = " + SQLString.Format(s_gadmin);
                
                if( !ss_part.equals("ALL")) {
                    sql += " and a.comp like '" + GetCodenm.get_compval(ss_part) + "'";
                }
                sql += " and a.comptype = '5'  order by a.comp";  		
            }
            else {
                sql = "select distinct comp, partnm";
                sql += " from tz_comp where comptype = '5'";
    			
                if( !ss_dept.equals("ALL")) {
                	sql += " and comp like '" + GetCodenm.get_compval(ss_dept) + "'";               	 	
                }	
                sql += " order by comp";  
            }

            ls = connMgr.executeQuery(sql); 
            
            if( !s_gadmin.equals("K6") && !s_gadmin.equals("K7")) {		//	�μ����� or �μ��� �� �ƴѰ�� 'ALL'  ���� ��� 
                result = getSelectTag(ls, isChange, true, "s_part", ss_part);
            }
            else {
                result = getSelectTag(ls, isChange, false, "s_part", ss_part);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return result;
    } 
    
    public static String getJikwi(RequestBox box, boolean isChange, boolean isALL) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;        
        ListSet ls = null;
        String sql = "";
        String result = "";
        
        try {
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String  ss_jikwi  = box.getStringDefault("s_jikwi", "ALL");     
            
            connMgr = new DBConnectionManager();            

            sql = "select distinct jikwi, jikwinm";
            sql += " from tz_jikwi";
            sql += " order by jikwi";  	
            
            ls = connMgr.executeQuery(sql); 
            
            if( v_gadmin.equals("A")) {		//	Ultra, SuperVisor 'ALL'  ���� ��� 
                result = getSelectTag(ls, isChange, true, "s_jikwi", ss_jikwi);
            }
            else {
                result = getSelectTag(ls, isChange, false, "s_jikwi", ss_jikwi);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return result;
    }

	/**
	��޺� 
    @param box          receive from the form object and session
    @return String
    */
    public static String getJikup(RequestBox box, boolean isChange, boolean isALL) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;        
        ListSet ls = null;
        String sql = "";
        String result = "";
        
        try {
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String  ss_jikup = box.getStringDefault("s_jikup", "ALL");     
            
            connMgr = new DBConnectionManager();            

            sql = "select distinct jikup, jikupnm";
            sql += " from tz_jikup";
            sql += " order by jikup";  	
            
            ls = connMgr.executeQuery(sql); 
            
            if( v_gadmin.equals("A")) {		//	Ultra, SuperVisor 'ALL'  ���� ��� 
                result = getSelectTag(ls, isChange, true, "s_jikup", ss_jikup);
            }
            else {
                result = getSelectTag(ls, isChange, false, "s_jikup", ss_jikup);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return result;
    }
    
    public static String getJikun(RequestBox box, boolean isChange, boolean isALL) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;        
        ListSet ls = null;
        String sql = "";
        String result = "";
        
        try {
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String  ss_jikun  = box.getStringDefault("s_jikun", "ALL");     
            
            connMgr = new DBConnectionManager();            

            sql = "select distinct jikun, jikunnm";
            sql += " from tz_jikun";
            sql += " order by jikun";  	
            
            ls = connMgr.executeQuery(sql); 
            
            if( v_gadmin.equals("A")) {		//	Ultra, SuperVisor 'ALL'  ���� ��� 
                result = getSelectTag(ls, isChange, true, "s_jikun", ss_jikun);
            }
            else {
                result = getSelectTag(ls, isChange, false, "s_jikun", ss_jikun);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return result;
    }

		
    /**
	SELECT HTML 
    @param box          receive from the form object and session
    @return String
    */
    public static String getSelectTag(ListSet ls, boolean isChange, boolean isALL, String selname, String optionselected) throws Exception {
        StringBuffer sb = null;
        
        try {
            sb = new StringBuffer();  

            sb.append("<select name = \"" + selname + "\"");
            if(isChange) sb.append(" onChange = \"whenSelection('change')\"");  
            sb.append(">\r\n");
            if(isALL) {
                sb.append("<option value = \"ALL\">ALL</option>\r\n");  
            }
            else if(isChange) {
                sb.append("<option value = \"----\">----</option>\r\n");  
            }

            while (ls.next()) {    
                ResultSetMetaData meta = ls.getMetaData();
                int columnCount = meta.getColumnCount();
                
                sb.append("<option value = \"" + ls.getString(1) + "\"");

                if (optionselected.equals(ls.getString(1))) sb.append(" selected");
               
                sb.append(">" + ls.getString(columnCount) + "</option>\r\n");
            }
            sb.append("</select>\r\n");
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage());
        }
        return sb.toString();
    }
}