// **********************************************************
//  1. f      ��: �ſ�d�����0
//  2. �wα׷���: SelectSubjMmBean
//  3. ��      ��: �ſ�d�����0
//  4. ȯ      ��: JDK 1.4
//  5. ��      o: 1.0
//  6. ��      ��: ����� 2008.11.10
//  7. ��      d: 
// **********************************************************

package com.ziaan.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * �ſ�d�����0
 *
 * @date   : 2008.11.10
 * @author : �����
 */
public class SelectSubjMmBean { 
 
    public static String getSubj(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        return getSubj(box, isChange, isALL, "s_subjcourse", "");
    }
    
    public static String getSubj(RequestBox box, boolean isChange, boolean isALL, String selName, String optionselected) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        
        try {           
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            connMgr = new DBConnectionManager();            

            if ( v_gadmin.equals("F") || v_gadmin.equals("P") ) {      //  ������, ����
            	sql = "\n select distinct a.subj "
            		+ "\n      , '[' || a.subj || '] ' || a.subjnm as subjnm "
            		+ "\n from   tz_subj a "
            		+ "\n      , tz_subjman c, tz_grsubj d "
                	+ "\n where  a.subj = c.subj "
                	+ "\n and    a.subj = d.subjcourse "
                	+ "\n and    c.userid = " + SQLString.Format(s_userid)
                	+ "\n and    c.gadmin = " + SQLString.Format(s_gadmin)
                	+ "\n and    a.isuse='Y' "
            		+ "\n and    a.isonoff = 'ON' " 
            		+ "\n and    a.subj_gu = 'M' "
            		+ "\n order  by subjnm ";
            }
            else if ( v_gadmin.equals("H") ) {     //  ��0�׷����
            	sql = "\n select distinct a.subj "
            		+ "\n      , '[' || a.subj || '] ' || a.subjnm as subjnm "
            		+ "\n from   tz_subj a "
            		+ "\n      , (select e.subjcourse as subj, e.grcode as grcode "
            		+ "\n         from   tz_grsubj e, tz_grcodeman f "
            		+ "\n         where  f.userid = " + SQLString.Format(s_userid)
            		+ "\n         and    f.gadmin = " + SQLString.Format(s_gadmin)
            		+ "\n         and    e.grcode = f.grcode) c "
                	+ "\n where  a.subj = c.subj "
                	+ "\n and    a.isuse='Y' "
            		+ "\n and    a.isonoff = 'ON' " 
            		+ "\n and    a.subj_gu = 'M' "
            		+ "\n order  by subjnm ";
            }
            else if ( v_gadmin.equals("K") ) {     //  ȸ�����, �μ�����
            	sql = "\n select distinct a.subj "
            		+ "\n      , '[' || a.subj || '] ' || a.subjnm as subjnm "
            		+ "\n from   tz_subj a, tz_grsubj c "
            		+ "\n      , (select e.grcode "
            		+ "\n         from   tz_grcomp e "
            		+ "\n              , tz_compman f "
            		+ "\n         where  f.userid = " + SQLString.Format(s_userid)
            		+ "\n         and    f.gadmin = " + SQLString.Format(s_gadmin)
            		+ "\n         and    e.comp = f.comp) d "
                	+ "\n where  a.subj = c.subjcourse "
                	+ "\n and    c.grcode = d.grcode "
                	+ "\n and    a.isuse='Y' "
            		+ "\n and    a.isonoff = 'ON' " 
            		+ "\n and    a.subj_gu = 'M' "
            		+ "\n order  by subjnm ";
            }
            else {      //  Ultravisor, Supervisor
            	sql = "\n select distinct a.subj "
            		+ "\n      , '[' || a.subj || '] ' || a.subjnm as subjnm "
            		+ "\n from   tz_subj a "
                	+ "\n where  a.isuse='Y' "
            		+ "\n and    a.isonoff = 'ON' " 
            		+ "\n and    a.subj_gu = 'M' "
            		+ "\n order  by subjnm ";
            }

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);     
            
            result += getSelectTag( ls, isChange, isALL, selName, optionselected);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }    
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return result;
    }  

    public static String getSubjMm(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        return getSubjMm(box, isChange, isALL, "s_subjcourse");
    }
    
    public static String getSubjMm(RequestBox box, boolean isChange, boolean isALL, String selName) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        
        try {           
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  s_year   = box.getStringDefault("s_gyear",FormatDate.getDate("yyyy"));    // ����
            String  s_subj   = box.getString(selName);
            
            connMgr = new DBConnectionManager();            

            if ( v_gadmin.equals("F") || v_gadmin.equals("P") ) {      //  ������, ����
            	sql = "\n select distinct a.subj "
            		+ "\n      , '[' || a.subj || '] ' || a.subjnm as subjnm "
            		+ "\n from   tz_subj a, tz_subjseq b "
            		+ "\n      , tz_subjman c, tz_grsubj d "
                	+ "\n where  a.subj = b.subj "
                	+ "\n and    a.subj = c.subj "
                	+ "\n and    a.subj = d.subjcourse "
                	+ "\n and    c.userid = " + SQLString.Format(s_userid)
                	+ "\n and    c.gadmin = " + SQLString.Format(s_gadmin)
                	+ "\n and    a.isuse='Y' "
            		+ "\n and    a.isonoff = 'ON' " 
            		+ "\n and    a.subj_gu = 'M' ";
            }
            else if ( v_gadmin.equals("H") ) {     //  ��0�׷����
            	sql = "\n select distinct a.subj "
            		+ "\n      , '[' || a.subj || '] ' || a.subjnm as subjnm "
            		+ "\n from   tz_subj a, tz_subjseq b "
            		+ "\n      , (select e.subjcourse as subj, e.grcode as grcode "
            		+ "\n         from   tz_grsubj e, tz_grcodeman f "
            		+ "\n         where  f.userid = " + SQLString.Format(s_userid)
            		+ "\n         and    f.gadmin = " + SQLString.Format(s_gadmin)
            		+ "\n         and    e.grcode = f.grcode) c "
                	+ "\n where  a.subj = b.subj "
                	+ "\n and    a.subj = c.subj "
                	+ "\n and    a.isuse='Y' "
            		+ "\n and    a.isonoff = 'ON' " 
            		+ "\n and    a.subj_gu = 'M' ";
            }
            else if ( v_gadmin.equals("K") ) {     //  ȸ�����, �μ�����
            	sql = "\n select distinct a.subj "
            		+ "\n      , '[' || a.subj || '] ' || a.subjnm as subjnm "
            		+ "\n from   tz_subj a, tz_subjseq b, tz_grsubj c "
            		+ "\n      , (select e.grcode "
            		+ "\n         from   tz_grcomp e "
            		+ "\n              , tz_compman f "
            		+ "\n         where  f.userid = " + SQLString.Format(s_userid)
            		+ "\n         and    f.gadmin = " + SQLString.Format(s_gadmin)
            		+ "\n         and    e.comp = f.comp) d "
                	+ "\n where  a.subj = b.subj "
                	+ "\n and    a.subj = c.subjcourse "
                	+ "\n and    c.grcode = d.grcode "
                	+ "\n and    a.isuse='Y' "
            		+ "\n and    a.isonoff = 'ON' " 
            		+ "\n and    a.subj_gu = 'M' ";
            }
            else {      //  Ultravisor, Supervisor
            	sql = "\n select distinct a.subj "
            		+ "\n      , '[' || a.subj || '] ' || a.subjnm as subjnm"
            		+ "\n from   tz_subj a, tz_subjseq b "
                	+ "\n where  a.subj = b.subj "
                	+ "\n and    a.isuse='Y' "
            		+ "\n and    a.isonoff = 'ON' " 
            		+ "\n and    a.subj_gu = 'M' ";
            }
            
        	if (!s_year.equals("")) {
        		sql += "\n and    b.year = " + StringManager.makeSQL(s_year);
        	}
            
            sql += "\n order  by subjnm ";

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);     
            
            result += getSelectTag( ls, isChange, isALL, selName, s_subj);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }    
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return result;
    }  
    
    /**
     * ���� selectbox
     * @param box
     * @param isChange
     * @param isALL
     * @param isStatisticalPage
     * @return
     * @throws Exception
     */
    public static String getYear(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
    	return getYear(box, isChange, isALL, "s_gyear");
	}
   
	public static String getYear(RequestBox box, boolean isChange, boolean isALL, String selName) throws Exception { 
		DBConnectionManager	connMgr	= null;
		PreparedStatement   pstmt   = null;        
		ListSet             ls      = null;
		String              sql     = "";
		String result = ""; 
		try { 
   
            String  s_year   = box.getStringDefault(selName, FormatDate.getDate("yyyy"));    // ����
            
			connMgr = new DBConnectionManager();

			sql = " select distinct gyear year "
				+ " from   tz_grseq "
				+ " order  by gyear desc  ";
           
			pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           
			ls = new ListSet(pstmt);     
           
			result += getSelectTag( ls, isChange, isALL, selName, s_year);
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, true);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }    
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return result;
	}  


    public static String getSubjseq(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt           = null;        
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        String result          = "";

        String s_gadmin = box.getSession("gadmin");
        String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
        String s_userid = box.getSession("userid");

        String ss_gyear        = box.getStringDefault("s_gyear"        , "ALL");   // ��0�⵵
        String ss_subjcourse   = box.getStringDefault("s_subjcourse"   , "ALL");   // ���
        String ss_subjseq      = box.getStringDefault("s_subjseq"      , "ALL");   // �����
        
        try {
            connMgr = new DBConnectionManager();
            
            if ( v_gadmin.equals("P") ) {      // ����
            	sbSQL.append("\n select distinct									");
            	sbSQL.append("\n        a.subjseq									");
            	sbSQL.append("\n      , a.subjseq									");
            	sbSQL.append("\n from   tz_subjseq a								");
            	sbSQL.append("\n      , tz_classtutor b								");
            	sbSQL.append("\n where  a.subj = b.subj(+)							");
            	sbSQL.append("\n and    a.year = b.year(+)							");
            	sbSQL.append("\n and    a.subjseq = b.subjseq(+)					");
            	sbSQL.append("\n and    a.subj = " + SQLString.Format(ss_subjcourse) );
            	sbSQL.append("\n and    a.year = " + SQLString.Format(ss_gyear)		 );
            	sbSQL.append("\n and    b.tuserid = " + SQLString.Format(s_userid)   );
            	sbSQL.append("\n order  by a.subjseq								");

            } else {
            	sbSQL.append("\n select distinct                                   	");                                       
            	sbSQL.append("\n        a.subjseq as subjseq                       	");
            	sbSQL.append("\n      , a.subjseq                                  	");
            	sbSQL.append("\n from   tz_subjseq      a                          	");
            	sbSQL.append("\n where  a.subj = " + SQLString.Format(ss_subjcourse) );
            	sbSQL.append("\n and    a.year = " + SQLString.Format(ss_gyear     ) );
            	sbSQL.append("\n order  by a.subjseq								");
            }

            pstmt   = connMgr.prepareStatement(sbSQL.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls      = new ListSet(pstmt);
            
            result += getSelectTag( ls, isChange, isALL, "s_subjseq", ss_subjseq);
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls         != null ) { try { ls.close();               } catch ( Exception e   ) { } }
            if ( pstmt      != null ) { try { pstmt.close();            } catch ( Exception e   ) { } }    
            if ( connMgr    != null ) { try { connMgr.freeConnection(); } catch ( Exception e   ) { } }
        }

        return result;
    }  

    public static String getSubjClass(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String              sql     = "";
        String 				result 	= "";

        try { 
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
        	
            String  ss_gyear = box.getString("s_gyear");     						//  ��0�⵵
            String  ss_subjcourse = box.getStringDefault("s_subjcourse","ALL");    	//  ��d
            String  ss_subjseq = box.getStringDefault("s_subjseq","ALL");    		//  ��d���
            String  ss_class = box.getStringDefault("s_class","ALL");    			//  Ŭ����

            connMgr = new DBConnectionManager();

            if ( v_gadmin.equals("P") ) {      //  ������, ����
            	sql = "\n select a.class "
            		+ "\n      , a.classnm "
            		+ "\n from   tz_class a "
            		+ "\n      , tz_classtutor b "
            		+ "\n where  a.subj = b.subj "
            		+ "\n and    a.year = b.year "
            		+ "\n and    a.subjseq = b.subjseq "
            		+ "\n and    a.class = b.class "
            		+ "\n and    b.tuserid = "+ SQLString.Format(s_userid)
	            	+ "\n and    a.subj    = " + SQLString.Format(ss_subjcourse)
	            	+ "\n and    a.year    = " + SQLString.Format(ss_gyear)
	            	+ "\n and    a.subjseq = " + SQLString.Format(ss_subjseq);
            } else {
            
	            sql = " select class , classnm "
	            	+ " from   tz_class "
	            	+ " where  subj    = " + SQLString.Format(ss_subjcourse)
	            	+ " and    year    = " + SQLString.Format(ss_gyear)
	            	+ " and    subjseq = " + SQLString.Format(ss_subjseq)
	            	+ " order  by classnm  " ;
            }
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ls = new ListSet(pstmt);     
            
            result += getSelectTag( ls, isChange, isALL, "s_class", ss_class);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return result;
    }

    public static String getGyear(DBConnectionManager connMgr, String ss_grcode) throws Exception { 
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        
        try { 

            sql  = " select distinct gyear ";
            sql += "   from tz_grseq       ";
            sql += "   where grcode = " + SQLString.Format(ss_grcode);
            sql += "  order by gyear desc  ";

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);     
            
            if ( ls.next() ) result = ls.getString(1);
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }    
        }
        return result;
    }  
    
	public static String getSelectTag(ListSet ls, boolean isChange, boolean isALL, String selname, String optionselected) throws Exception { 
		StringBuffer sb = null;
		try { 
			sb = new StringBuffer();  

			sb.append("<select id = \"" + selname + "\" name = \"" + selname + "\"");
			if ( isChange) sb.append(" onChange = \"whenSelection('change')\"");  
			sb.append(" > \r\n");
			if ( isALL) { 
				sb.append("<option value = \"ALL\" > ALL</option > \r\n");  
			}
			else if ( isChange) { 
				if ( selname.indexOf("year") == -1) 
					sb.append("<option value = \"----\" >== ���� == </option > \r\n");  
			}

			while ( ls.next() ) {    
				ResultSetMetaData meta = ls.getMetaData();
				int columnCount = meta.getColumnCount();
               
				sb.append("<option value = \"" + ls.getString(1) + "\"");

				if ( optionselected.equals( ls.getString(1))) sb.append(" selected");

                if ( selname.equals("s_subjseq")) sb.append(" > " + StringManager.cutZero( ls.getString(columnCount)) + "��</option > \r\n");
                else sb.append(" > " + ls.getString(columnCount) + "</option > \r\n");
			}
			sb.append("</select > \r\n");
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, true);
			throw new Exception(ex.getMessage() );
		}
		return sb.toString();
	}
   
   
}
