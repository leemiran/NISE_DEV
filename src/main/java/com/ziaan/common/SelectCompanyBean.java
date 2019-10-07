// **********************************************************
//  1. f      ��: Sample �ڷ��
//  2. �wα׷���: BulletinBeanjava
//  3. ��      ��: Sample �ڷ��
//  4. ȯ      ��: JDK 1.3
//  5. ��      o: 1.0
//  6. ��      ��: ��d�� 2003. 4. 26
//  7. ��      d: ��d�� 2003. 4. 26
// **********************************************************

package com.ziaan.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * �ڷ��(HomePage) ��� Sample Class
 *
 * @date   : 2003. 5
 * @author : j.h. lee
 */
public class SelectCompanyBean { 
  
   /**
   * �ڷ�� listȭ�� select
   */

    public ArrayList getCompany(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();           
            
            if ( v_gadmin.equals("H") ) { 		// 	��0�׷����
                sql  = "\n select distinct a.comp, a.compnm as companynm";
                sql += "\n from   tz_compclass a, tz_grcomp b,";
                sql += "\n        (select grcode from tz_grcodeman ";
                sql += "\n         where  userid = " + SQLString.Format(s_userid);
                sql += "\n         and    gadmin = " + SQLString.Format(s_gadmin) + ") c ";
                sql += "\n where  a.comp = b.comp ";
                sql += "\n and    b.grcode = c.grcode ";
                sql += "\n order  by a.compnm";  		
            }
            else if ( v_gadmin.equals("K") ) { 		// 	ȸ�����, �μ�����
                sql  = "\n select distinct a.comp, a.compnm as companynm";
                sql += "\n from   tz_compclass a, tz_compman b";
                sql += "\n where  a.comp = b.comp ";
                sql += "\n and    b.userid = " + SQLString.Format(s_userid);
                sql += "\n and    b.gadmin = " + SQLString.Format(s_gadmin);
                sql += "\n order  by a.compnm"; 		
            }		
            else { 		// 	Ultravisor, Supervisor, ������, ����
                sql  = "\n select distinct comp, compnm as companynm";
                sql += "\n from   tz_compclass ";	
                sql += "\n order  by compnm";  	 		
            }
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);       
            
            if ( v_gadmin.equals("A") || v_gadmin.equals("F") || v_gadmin.equals("P") || v_gadmin.equals("H") ) {     
                 //      Ultra/Super or ������ or ���� or ��0�׷���� �� ��� 'ALL' ȸ�� ��� 
                dbox = this.setAllSelectBox( ls);
                list.add(dbox);
            }
            else { 
                dbox = this.setSpaceSelectBox( ls);
                list.add(dbox);
            }

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }    
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }  
    
    
    
    public ArrayList getAesCompany(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
	    connMgr = new DBConnectionManager();            
            
            list = new ArrayList();           
            
            if ( v_gadmin.equals("H") ) { 		// 	��0�׷����
                sql  = "\n select distinct a.comp, a.compnm as companynm";
                sql += "\n from   tz_compclass a, tz_grcomp b,";
                sql += "\n        (select grcode from tz_grcodeman ";
                sql += "\n         where  userid = " + SQLString.Format(s_userid);
                sql += "\n         and    gadmin = " + SQLString.Format(s_gadmin) + ") c ";
                sql += "\n where  a.comp = b.comp ";
                sql += "\n and    b.grcode = c.grcode ";
                sql += "\n order  by a.compnm";  		
            }
            else if ( v_gadmin.equals("K") ) { 		// 	ȸ�����, �μ�����
                sql  = "\n select distinct a.comp, a.compnm as companynm";
                sql += "\n from   tz_compclass a, tz_compman b";
                sql += "\n where  a.comp = b.comp ";
                sql += "\n and    b.userid = " + SQLString.Format(s_userid);
                sql += "\n and    b.gadmin = " + SQLString.Format(s_gadmin);
                sql += "\n order  by a.compnm"; 		
            }		
            else { 		// 	Ultravisor, Supervisor, ������, ����
                sql  = "\n select distinct comp, compnm as companynm";
                sql += "\n from   tz_compclass ";	
                sql += "\n order  by compnm";  	 		
            }
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);       
            
            if ( v_gadmin.equals("A") || v_gadmin.equals("F") || v_gadmin.equals("P") || v_gadmin.equals("H") ) {     
                 //      Ultra/Super or ������ or ���� or ��0�׷���� �� ��� 'ALL' ȸ�� ��� 
                dbox = this.setAllSelectBox( ls);
                list.add(dbox);
            }
            else { 
                dbox = this.setSpaceSelectBox( ls);
                list.add(dbox);
            }

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }    
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }  
    
    public ArrayList getGpm(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_company  = box.getStringDefault("s_company","ALL");      //  �ش� ȸ���� comp code
            
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();
            
            if ( s_gadmin.equals("K6") || s_gadmin.equals("K7") ) { 		// 	�μ����� or �μ���
                sql  = "\n select distinct a.comp, '' as gpmnm";
                sql += "\n from   tz_compclass a, tz_compman b";
                sql += "\n where  a.comp = b.comp ";
                sql += "\n and    b.userid = " + SQLString.Format(s_userid);
                sql += "\n and    b.gadmin = " + SQLString.Format(s_gadmin);
                
                if ( !ss_company.equals("ALL") ) { 
                    sql += "\n and    a.comp like '" + GetCodenm.get_compval(ss_company) + "'";
                }
                //sql += "\n order  by a.gpmnm";  		
            }
            else { 
                sql  = "\n select distinct comp, '' as gpmnm";
                sql += "\n from   tz_compclass ";
                
                if ( !ss_company.equals("ALL") ) { 
                    sql += "\n and    comp like '" + GetCodenm.get_compval(ss_company) + "'";
                }
                //sql += "\n order by gpmnm";  	
            }

            ls = connMgr.executeQuery(sql); 
            
            if ( !s_gadmin.equals("K6") && !s_gadmin.equals("K7") ) { 		// 	�μ����� or �μ��� �� �ƴѰ�� 'ALL'  ���� ��� 
                dbox = this.setAllSelectBox( ls);
                list.add(dbox);
            }

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }  
        
    public ArrayList getDept(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_seltext  = box.getStringDefault("s_seltext","ALL");      //  �ش� ������ comp code

            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();
            
            if ( s_gadmin.equals("K6") || s_gadmin.equals("K7") ) { 		// 	�μ����� or �μ���
                sql  = "\n select distinct a.comp, '' as deptnm ";
                sql += "\n from   tz_compclass a, tz_compman b";
                sql += "\n where  a.comp = b.comp ";
                sql += "\n and    b.userid = " + SQLString.Format(s_userid);
                sql += "\n and    b.gadmin = " + SQLString.Format(s_gadmin);
                
                if ( !ss_seltext.equals("ALL") ) { 
                    sql += "\n and    a.comp like '" + GetCodenm.get_compval(ss_seltext) + "'";
                }
                //sql += "\n order  by a.deptnm";  		
            }
            else { 
                sql  = "\n select distinct comp, '' as deptnm";
                sql += "\n from   tz_compclass ";
                
                if ( !ss_seltext.equals("ALL") ) { 
                    sql += "\n and    comp like '" + GetCodenm.get_compval(ss_seltext) + "'";
                }
                //sql += "\n order by deptnm";  	
            }

            ls = connMgr.executeQuery(sql); 
            
            if ( !s_gadmin.equals("K6") && !s_gadmin.equals("K7") ) { 		// 	�μ����� or �μ��� �� �ƴѰ�� 'ALL'  ���� ��� 
                dbox = this.setAllSelectBox( ls);
                list.add(dbox);
            }

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    } 
    
    public ArrayList getJikwi(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
            
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();

            sql  = "select distinct jikwi, jikwinm";
            sql += " from tz_jikwi";
            sql += " order by jikwinm";  	
            
            ls = connMgr.executeQuery(sql); 
            
            dbox = this.setAllSelectBox( ls);
            list.add(dbox);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }
    
    public ArrayList getJikup(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
            
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();

            sql = "select distinct jikup, jikupnm";
            sql += " from tz_jikup";
            sql += " order by jikupnm";  	
            
            ls = connMgr.executeQuery(sql); 
            
            dbox = this.setAllSelectBox( ls);
            list.add(dbox);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }
    
    public ArrayList getJikun(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
            
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();

            sql = "select distinct jikun, jikunnm";
            sql += " from tz_jikun";
            sql += " order by jikunnm";  	
            
            ls = connMgr.executeQuery(sql); 
            
            dbox = this.setAllSelectBox( ls);
            list.add(dbox);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }

   public DataBox setAllSelectBox(ListSet ls) throws Exception { 
        DataBox             dbox    = null;
        int columnCount = 0;
        try { 
            dbox = new DataBox("selectbox");
        
            ResultSetMetaData meta = ls.getMetaData();
            columnCount = meta.getColumnCount();// System.out.println("columnCount : " + columnCount);
            for ( int i = 1; i <= columnCount; i++ ) { 
                String columnName = meta.getColumnName(i).toLowerCase();// System.out.println("columnName : " + columnName);
                
                dbox.put("d_" + columnName, "ALL");
            }
        }
        catch ( Exception ex ) { ex.printStackTrace();
            throw new Exception("SelectBean.setAllDataBox()\r\n\"" + ex.getMessage() );
        }
        return dbox;
    }  
     
    public DataBox setSpaceSelectBox(ListSet ls) throws Exception { 
        DataBox             dbox    = null;
        int columnCount = 0;
        try { 
            dbox = new DataBox("selectbox");
        
            ResultSetMetaData meta = ls.getMetaData();
            columnCount = meta.getColumnCount();// System.out.println("columnCount : " + columnCount);
            for ( int i = 1; i <= columnCount; i++ ) { 
                String columnName = meta.getColumnName(i).toLowerCase();// System.out.println("columnName : " + columnName);
                
                dbox.put("d_" + columnName, "----");
            }
        }
        catch ( Exception ex ) { ex.printStackTrace();
            throw new Exception("SelectBean.setAllDataBox()\r\n\"" + ex.getMessage() );
        }
        return dbox;
    }
    
    
    /**
   * ��0�׷캰ȸ��
   */
    public ArrayList getGrcomp(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            String ss_grcode = box.getString("s_grcode");

	        connMgr = new DBConnectionManager();            
            
            list = new ArrayList();           
            
            sql += " select                                      \n";
            sql += "   b.comp,                                   \n";
            sql += "   b.compnm                                  \n";
            sql += " from                                        \n";
            sql += "   tz_grcomp a, tz_compclass b               \n";
            sql += " where  a.comp = b.comp                      \n";
            sql += "   and grcode = " +SQLString.Format(ss_grcode);
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);       
            
            // if ( v_gadmin.equals("A") || v_gadmin.equals("F") || v_gadmin.equals("P") || v_gadmin.equals("H") ) {     
            //      Ultra/Super or ������ or ���� or ��0�׷���� �� ��� 'ALL' ȸ�� ��� 
            
            if ( !ss_grcode.equals("ALL") ) { 
              dbox = this.setAllSelectBox( ls);
              list.add(dbox);
            }
            
            // }
            // else { 
            //    dbox = this.setSpaceSelectBox( ls);
            //    list.add(dbox);
            // }

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }    
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }  
    
    
    
    
    
    
// ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == 
     public static String getCompany(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "<span class='search_table07'>ȸ��</span></label> ";
        
        try { 
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            String ss_company = box.getStringDefault("s_company","ALL");    
            
	    	connMgr = new DBConnectionManager();                 
            
            if ( v_gadmin.equals("H") ) { 		// 	��0�׷����
                sql  = "\n select distinct a.comp, a.compnm as companynm";
                sql += "\n from   tz_compclass a, tz_grcomp b,";
                sql += "\n        (select grcode from tz_grcodeman ";
                sql += "\n         where  userid = " + SQLString.Format(s_userid);
                sql += "\n         and    gadmin = " + SQLString.Format(s_gadmin) + ") c ";
                sql += "\n where  a.comp = b.comp ";
                sql += "\n and    b.grcode = c.grcode ";
                if(!box.getString("s_grcode").equals("") && !box.getString("s_grcode").equals("ALL")) {
                	sql += "\n and    b.grcode = " + SQLString.Format(box.getString("s_grcode")) + " ";
                }
                sql += "\n order  by a.compnm";  		
            }
            else if ( v_gadmin.equals("K") ) { 		// 	ȸ�����, �μ�����
                sql  = "\n select distinct a.comp, a.compnm as companynm";
                sql += "\n from   tz_compclass a, tz_compman b, tz_grcomp c ";
                sql += "\n where  a.comp = b.comp ";
                sql += "\n and    a.comp = c.comp ";
                sql += "\n and    b.userid = " + SQLString.Format(s_userid);
                sql += "\n and    b.gadmin = " + SQLString.Format(s_gadmin);
                if(!box.getString("s_grcode").equals("") && !box.getString("s_grcode").equals("ALL")) {
                	sql += "\n and    c.grcode = " + SQLString.Format(box.getString("s_grcode")) + " ";
                }
                sql += "\n order  by a.compnm"; 		
            }		
            else { 		// 	Ultravisor, Supervisor, ������, ����
                sql  = "\n select distinct a.comp, compnm as companynm";
                sql += "\n from   tz_compclass a, tz_grcomp b ";
                sql += "\n where  a.comp = b.comp (+) ";
                if(!box.getString("s_grcode").equals("") && !box.getString("s_grcode").equals("ALL")) {
                	sql += "\n and    b.grcode = " + SQLString.Format(box.getString("s_grcode")) + " ";
                }
                sql += "\n order  by compnm";  	 		
            }
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);       
            
            if ( v_gadmin.equals("A") || v_gadmin.equals("F") || v_gadmin.equals("P") || v_gadmin.equals("H") ) {     
                 //      Ultra/Super or ������ or ���� or ��0�׷���� �� ��� 'ALL' ȸ�� ��� 
                result += getSelectTag( ls, isChange, isALL, "s_company", ss_company);
            }
            else { 
                result += getSelectTag( ls, isChange, false, "s_company", ss_company);
            }
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

     public static String getCompany2(RequestBox box, boolean isChange, boolean isALL, String comp) throws Exception { 
         DBConnectionManager	connMgr	= null;
         PreparedStatement   pstmt   = null;        
         ListSet             ls      = null;
         String              sql     = "";
         String result = "";
         
         try { 
             String s_gadmin = box.getSession("gadmin");
             String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
             String s_userid = box.getSession("userid");
             String ss_company = box.getStringDefault("s_company","ALL");    
             
 	    	connMgr = new DBConnectionManager();                 
             
             if ( v_gadmin.equals("H") ) { 		// 	��0�׷����
                 sql  = "\n select distinct a.comp, a.compnm as companynm";
                 sql += "\n from   tz_compclass a, tz_grcomp b,";
                 sql += "\n        (select grcode from tz_grcodeman ";
                 sql += "\n         where  userid = " + SQLString.Format(s_userid);
                 sql += "\n         and    gadmin = " + SQLString.Format(s_gadmin) + ") c ";
                 sql += "\n where  a.comp = b.comp ";
                 sql += "\n and    b.grcode = c.grcode ";
                 if(!box.getString("s_grcode").equals("") && !box.getString("s_grcode").equals("ALL")) {
                 	sql += "\n and    b.grcode = " + SQLString.Format(box.getString("s_grcode")) + " ";
                 }
                 sql += "\n order  by a.compnm";  		
             }
             else if ( v_gadmin.equals("K") ) { 		// 	ȸ�����, �μ�����
                 sql  = "\n select distinct a.comp, a.compnm as companynm";
                 sql += "\n from   tz_compclass a, tz_compman b, tz_grcomp c ";
                 sql += "\n where  a.comp = b.comp ";
                 sql += "\n and    a.comp = c.comp ";
                 sql += "\n and    b.userid = " + SQLString.Format(s_userid);
                 sql += "\n and    b.gadmin = " + SQLString.Format(s_gadmin);
                 if(!box.getString("s_grcode").equals("") && !box.getString("s_grcode").equals("ALL")) {
                 	sql += "\n and    c.grcode = " + SQLString.Format(box.getString("s_grcode")) + " ";
                 }
                 sql += "\n order  by a.compnm"; 		
             }		
             else if ( v_gadmin.equals("Z") || v_gadmin.equals("") ) { 		// 	�н���
            	 sql  = "\n select distinct a.comp, compnm as companynm";
            	 sql += "\n from   tz_compclass a, tz_grcomp b ";
            	 sql += "\n where  a.comp = b.comp (+) ";
            	 if(!box.getString("s_grcode").equals("") && !box.getString("s_grcode").equals("ALL")) {
            		 sql += "\n and    b.grcode = " + SQLString.Format(box.getString("s_grcode")) + " ";
            	 }
            	 sql += "\n     and a.show_yn = 'Y'";  	 		
            	 sql += "\n order  by compnm";  	 		
             }
             else { 		// 	Ultravisor, Supervisor, ������, ����
                 sql  = "\n select distinct a.comp, compnm as companynm";
                 sql += "\n from   tz_compclass a, tz_grcomp b ";
                 sql += "\n where  a.comp = b.comp (+) ";
                 if(!box.getString("s_grcode").equals("") && !box.getString("s_grcode").equals("ALL")) {
                 	sql += "\n and    b.grcode = " + SQLString.Format(box.getString("s_grcode")) + " ";
                 }
                 sql += "\n order  by comp";  	 		
             }
             
             pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             
             ls = new ListSet(pstmt);       
             
             if ( v_gadmin.equals("A") || v_gadmin.equals("F") || v_gadmin.equals("P") || v_gadmin.equals("H") ) {     
                  //      Ultra/Super or ������ or ���� or ��0�׷���� �� ��� 'ALL' ȸ�� ��� 
                 result += getSelectTag( ls, isChange, isALL, "s_company", comp);
             }
             else { 
                 result += getSelectTag( ls, isChange, false, "s_company", comp);
             }
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
    
    public static String getAesCompany(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "ȸ�� ";
        
        try { 
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            String ss_company = box.getStringDefault("s_aes_code","ALL");    
            
            connMgr = new DBConnectionManager();                 
            
            if ( v_gadmin.equals("H") ) { 		// 	��0�׷����
                sql  = "\n select distinct a.comp, a.compnm as companynm";
                sql += "\n from   tz_compclass a, tz_grcomp b,";
                sql += "\n        (select grcode from tz_grcodeman ";
                sql += "\n         where  userid = " + SQLString.Format(s_userid);
                sql += "\n         and    gadmin = " + SQLString.Format(s_gadmin) + ") c ";
                sql += "\n where  a.comp = b.comp ";
                sql += "\n and    b.grcode = c.grcode ";
                sql += "\n order  by a.compnm";  		
            }
            else if ( v_gadmin.equals("K") ) { 		// 	ȸ�����, �μ�����
                sql  = "\n select distinct a.comp, a.compnm as companynm";
                sql += "\n from   tz_compclass a, tz_compman b";
                sql += "\n where  a.comp = b.comp ";
                sql += "\n and    b.userid = " + SQLString.Format(s_userid);
                sql += "\n and    b.gadmin = " + SQLString.Format(s_gadmin);
                sql += "\n order  by a.compnm"; 		
            }		
            else { 		// 	Ultravisor, Supervisor, ������, ����
                sql  = "\n select distinct comp, compnm as companynm";
                sql += "\n from   tz_compclass ";	
                sql += "\n order  by compnm";  	 		
            }
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);       
            
            if ( v_gadmin.equals("A") || v_gadmin.equals("F") || v_gadmin.equals("P") || v_gadmin.equals("H") ) {     
                 //      Ultra/Super or ������ or ���� or ��0�׷���� �� ��� 'ALL' ȸ�� ��� 
                result += getSelectTag( ls, isChange, isALL, "s_aes_code", ss_company);
            }
            else { 
                result += getSelectTag( ls, isChange, false, "s_aes_code", ss_company);
            }
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
    
    public static String getSelgubun(RequestBox box) throws Exception { 
        StringBuffer sb = new StringBuffer();  
        
        try { 
            String ss_company = box.getStringDefault("s_company","ALL");    
            
            if ( !ss_company.equals("ALL") && !ss_company.equals("") && !ss_company.equals("----") ) {                
                ConfigSet conf = new ConfigSet();
                boolean isGpm = conf.getBoolean("selgubun.gpm");
                boolean isJikwi = conf.getBoolean("selgubun.jikwi");
                boolean isJikup = conf.getBoolean("selgubun.jikup");
                boolean isJikun = conf.getBoolean("selgubun.jikun");
                
                String s_gadmin = box.getSession("gadmin");
                String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
                String s_userid = box.getSession("userid");
                String  ss_selgubun  = box.getStringDefault("s_selgubun","ALL");
    
                sb.append("<select name = \"s_selgubun\"");
                sb.append(" onChange = \"whenSelection('change')\"");  
                sb.append(" > \r\n");
                
                sb.append("<option value = \"----\"");
                if ( ss_selgubun.equals("----")) sb.append(" selected");
                sb.append(" > ---- </option > \r\n");
                    
                if ( isGpm) {             
                    sb.append("<option value = \"GPM\"");
                    if ( ss_selgubun.equals("GPM")) sb.append(" selected");
                    sb.append(" > ���κ� </option > \r\n");
                }
                if ( isJikwi && !v_gadmin.equals("K6") && !v_gadmin.equals("K7") ) {             
                    sb.append("<option value = \"JIKWI\"");
                    if ( ss_selgubun.equals("JIKWI")) sb.append(" selected");
                    sb.append(" > ��'�� </option > \r\n");
                }
                if ( isJikup && !v_gadmin.equals("K6") && !v_gadmin.equals("K7") ) {             
                    sb.append("<option value = \"JIKUP\"");
                    if ( ss_selgubun.equals("JIKUP")) sb.append(" selected");
                    sb.append(" > ��޺� </option > \r\n");
                }
                if ( isJikun && !v_gadmin.equals("K6") && !v_gadmin.equals("K7") ) {             
                    sb.append("<option value = \"JIKUN\"");
                    if ( ss_selgubun.equals("JIKUN")) sb.append(" selected");
                    sb.append(" > ��� </option > \r\n");
                }
                sb.append("</select > \r\n");
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage() );
        }
        return sb.toString();
    }  
    
    public static String getSeltext(RequestBox box) throws Exception { 
        String result = "";
        
        try { 
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_company  = box.getStringDefault("s_company","ALL");      //  �ش� ȸ���� comp code
            String  ss_selgubun  = box.getStringDefault("s_selgubun","ALL");      //  �ش� ȸ���� comp code    
            
            if ( !ss_company.equals("ALL") && !ss_company.equals("") && !ss_company.equals("----") ) {             
            
                if ( ss_selgubun.equals("GPM") || ss_selgubun.equals("") ) { 
                    result = getGpm(box, true, false);
                }
                else if ( ss_selgubun.equals("JIKWI") ) {    
                    result = getJikwi(box, true, false);
                }
                else if ( ss_selgubun.equals("JIKUP") ) { 
                    result = getJikup(box, true, false);
                }
                else if ( ss_selgubun.equals("JIKUN") ) { 
                    result = getJikun(box, true, false);
                }
                
                if ( ss_selgubun.equals("GPM") || ss_selgubun.equals("") ) { 
                    result += getDept(box, true, false);
                }
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage() );
        }
        return result;
    }  
    
    public static String getGpm(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "����/���� ";
        
        try { 
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_company  = box.getStringDefault("s_company","ALL");      //  �ش� ȸ���� comp code
            String  ss_seltext  = box.getStringDefault("s_seltext","ALL");      //  �ش� ȸ���� comp code
            
            connMgr = new DBConnectionManager();            
            
            // if ( s_gadmin.equals("K6") || s_gadmin.equals("K7") ) { 		// 	�μ����� or �μ���
            //    sql = "select distinct a.comp, a.gpmnm";
            //    sql += " from tz_comp a, tz_compman b";
            //    sql += " where substr(a.comp, 1, 6) = substr(b.comp, 1, 6) and b.userid = " + SQLString.Format(s_userid) + " and b.gadmin = " + SQLString.Format(s_gadmin);
            //    
            //    if ( !ss_company.equals("ALL") ) { 
            //        sql += " and a.comp like '" + GetCodenm.get_compval(ss_company) + "'";
            //    }
            //    sql += " and a.comptype = '3' order by a.comp";  		
            // }
            // else { 
            //    
            //    // if ( !ss_company.equals("ALL") ) { 
            //    //    sql += " and comp like '" + GetCodenm.get_compval(ss_company) + "'";
            //    // }
            //    
               // ss_company= ""�ΰ�� ����Ÿ�� �������� ��=
               if ( ss_company.equals("ALL") ) { 
               		sql  = " select distinct comp, compnm as deptnm";
    	            sql += " from   tz_compclass ";
               } else { 
	                sql  = " select distinct comp, compnm as gpmnm ";
    	            sql += " from   tz_compclass ";
               		sql += " where  comp = " + StringManager.makeSQL(ss_company);
               		sql += " order  by compnm";  	
               }
            
                
                
            // }

            ls = connMgr.executeQuery(sql); 
            
            // if ( !s_gadmin.equals("K6") && !s_gadmin.equals("K7") ) { 		// 	�μ����� or �μ��� �� �ƴѰ�� 'ALL'  ���� ��� 
            //    result = getSelectTag( ls, isChange, true, "s_seltext", ss_seltext);
            // }
            
            if ( !s_gadmin.equals("K6") && !s_gadmin.equals("K7") ) { 		// 	�μ����� or �μ��� �� �ƴѰ�� 'ALL'  ���� ��� 
            // if ( !ss_company.equals("ALL") ) { 
                result += getSelectTag( ls, isChange, true, "s_seltext", ss_seltext);
            }
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
    
    public static String getDept(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "�μ� ";
        
        try { 
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_seltext  = box.getStringDefault("s_seltext","ALL");
            String  ss_seldept  = box.getStringDefault("s_seldept","ALL");      

            connMgr = new DBConnectionManager();            
            
            // if ( s_gadmin.equals("K6") || s_gadmin.equals("K7") ) { 		// 	�μ����� or �μ���
            //    sql = "select distinct a.comp, a.deptnm";
            //    sql += " from tz_comp a, tz_compman b";
            //    sql += " where substr(a.comp, 1, 8) = substr(b.comp, 1, 8) and b.userid = " + SQLString.Format(s_userid) + " and b.gadmin = " + SQLString.Format(s_gadmin);
            //    
            //    if ( !ss_seltext.equals("ALL") ) { 
            //        sql += " and a.comp like '" + GetCodenm.get_compval(ss_seltext) + "'";
            //    }
            //    sql += " and a.comptype = '4'  order by a.comp";  		
            // }
            // else { 
    			
                // if ( !ss_seltext.equals("ALL") ) { 
                // 	sql += " and comp like '" + GetCodenm.get_compval(ss_seltext) + "'";               	 	
                // }	
                
 			   // ss_dept= ""�ΰ�� �ڱ� �Ҽ�d��
               if ( ss_seltext.equals("ALL") ) { 
               		// sql += " and substr(comp,1,6) = '" + StringManager.substring(box.getSession("comp"), 0, 6) + "'";
               		sql = "select distinct comp, compnm as deptnm";
    	            //sql += " from tz_comp where comptype = '6'";
    	            sql += " from tz_compclass ";
               } else { 
	                sql = "select distinct comp, compnm as deptnm";
    	            //sql += " from tz_comp where comptype = '4'";
    	            sql += " from tz_compclass ";

               		sql += " and comp = " + StringManager.makeSQL(ss_seltext) ;
					sql += " order by compnm ";  
               }
                               
                
            // } 
            ls = connMgr.executeQuery(sql); 
                
            // if ( !s_gadmin.equals("K6") && !s_gadmin.equals("K7") ) { 		// 	�μ����� or �μ��� �� �ƴѰ�� 'ALL'  ���� ��� 
            // if ( !ss_seltext.equals("ALL") ) { 
                result += getSelectTag( ls, isChange, true, "s_seldept", ss_seldept);
            // }
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
    
    public static String getJikwi(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;       
        ListSet             ls      = null; 
        String              sql     = "";
        String result = "��' ";
        
		String  ss_company  = box.getStringDefault("s_company","ALL");      //  �ش� ȸ���� comp code
        String  ss_seltext  = box.getStringDefault("s_jikwi","ALL");

        try { 
            
            connMgr = new DBConnectionManager();            
            
            // ss_company= ""�ΰ�� ����Ÿ�� �������� ��=
            // if ( ss_company.equals("ALL") ) { 
            //   	// sql += " and substr(comp,1,4) = '" + StringManager.substring(box.getSession("comp"), 0, 4) + "'";
            //   	sql = "select distinct comp, deptnm";
    	    // 	sql += " from tz_comp where comptype = '6'";
    	    // 	sql += " order by deptnm";  	
            // } else { 
	            sql = "select distinct jikwi, jikwinm";
	            sql += " from tz_jikwi";
                // sql += " where substr(grpcomp,1,4) = '" + StringManager.substring(ss_company, 0, 4) + "'";
	            sql += " order by jikwinm";  	
	        // }
            
            ls = connMgr.executeQuery(sql); 

            result += getSelectTag( ls, isChange, true, "s_jikwi", ss_seltext);
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
    
    public static String getJikup(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;       
        ListSet             ls      = null;
        String              sql     = "";
        String result = "��� ";
        
        String  ss_company  = box.getStringDefault("s_company","ALL");      //  �ش� ȸ���� comp code
        String  ss_jikup    = box.getStringDefault("s_jikup","ALL");
        
        try { 

            connMgr = new DBConnectionManager();            
            
            // ss_company= ""�ΰ�� ����Ÿ�� �������� ��=
            if ( ss_company.equals("ALL") ) { 
               	// sql += " and substr(comp,1,4) = '" + StringManager.substring(box.getSession("comp"), 0, 4) + "'";
               	sql = "select distinct comp, compnm as deptnm";
    	    	//sql += " from tz_comp where comptype = '6'";
    	    	sql += " from tz_compclass ";
    	    	sql += " order by compnm";  	
            } else { 
	            sql = "select distinct jikup, jikupnm";
	            sql += " from tz_jikup";
	            sql += " where grpcomp = " + StringManager.makeSQL(ss_company);
	            sql += " order by jikupnm";  
	        }
            
            ls = connMgr.executeQuery(sql); 

            result += getSelectTag( ls, isChange, true, "s_jikup", ss_jikup);
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

    public static String getJikun(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "�� ";
        
        String  ss_company  = box.getStringDefault("s_company","ALL");      //  �ش� ȸ���� comp code
        String  ss_seltext  = box.getStringDefault("s_jikun","ALL");
        
        try { 
            
            
            connMgr = new DBConnectionManager();            
            
            // ss_company= ""�ΰ�� ����Ÿ�� �������� ��=
            if ( ss_company.equals("ALL") ) { 
               	// sql += " and substr(comp,1,4) = '" + StringManager.substring(box.getSession("comp"), 0, 4) + "'";
               	sql = "select distinct comp, compnm as deptnm";
    	    	//sql += " from tz_comp where comptype = '6'";
    	    	sql += " from tz_compclass ";
    	    	sql += " order by compnm";  	
            } else { 
	            sql = "select distinct jikun, jikunnm";
	            sql += " from tz_jikun";
	            sql += " where grpcomp = " + StringManager.makeSQL(ss_company) ;
	            sql += " order by jikunnm";  
	        }
            
            ls = connMgr.executeQuery(sql); 

            result += getSelectTag( ls, isChange, true, "s_jikun", ss_seltext);
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
    
    public static String getJikmu(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "�� ";
        
        String  ss_company  = box.getStringDefault("s_company","ALL");      //  �ش� ȸ���� comp code
        String  ss_seltext  = box.getStringDefault("s_jikmu","ALL");
        
        try { 
            
            
            connMgr = new DBConnectionManager();            
            
            // ss_company= ""�ΰ�� ����Ÿ�� �������� ��=
            if ( ss_company.equals("ALL") ) { 
               	// sql += " and substr(comp,1,4) = '" + StringManager.substring(box.getSession("comp"), 0, 4) + "'";
               	sql = "select distinct comp, compnm as deptnm";
    	    	//sql += " from tz_comp where comptype = '6'";
               	sql += " from tz_compclass ";
    	    	sql += " order by compnm";  	
            } else { 
	            sql = "select distinct jikmu, jikmunm";
	            sql += " from tz_jikmu";
	            sql += " where grpcomp = '" + StringManager.makeSQL(ss_company);
	            sql += " order by jikmunm";  
	        }
            
            ls = connMgr.executeQuery(sql); 

            result += getSelectTag( ls, isChange, true, "s_jikmu", ss_seltext);
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
    
    public static String getWorkplc(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "�ٹ��� ";
        
        String  ss_company  = box.getStringDefault("s_company","ALL");      //  �ش� ȸ���� comp code
        String  ss_seltext  = box.getStringDefault("s_workplc","ALL");
        
        try { 
            
            
            connMgr = new DBConnectionManager();            
            
            // ss_company= ""�ΰ�� ����Ÿ�� �������� ��=
            if ( ss_company.equals("ALL") ) { 
               	// sql += " and substr(comp,1,4) = '" + StringManager.substring(box.getSession("comp"), 0, 4) + "'";
               	sql = "select distinct comp, compnm as deptnm";
    	    	//sql += " from tz_comp where comptype = '6'";
    	    	sql += " from tz_compclass ";
    	    	sql += " order by compnm";  
            } else { 
	            sql = "select distinct work_plc, work_plcnm";
	            sql += " from tz_workplc";
	            sql += " where grpcomp = " + StringManager.makeSQL(ss_company);
	            sql += " order by work_plcnm";  
	        }
            
            ls = connMgr.executeQuery(sql); 

            result += getSelectTag( ls, isChange, true, "s_workplc", ss_seltext);
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
    
    
    public static String getGrcomp(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "ȸ�� ";

        try { 
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            String ss_company = box.getStringDefault("s_company","ALL");    
            String ss_grcode  = box.getString("s_grcode");
            String ss_grcomp  = box.getString("s_grcomp");
            
            connMgr = new DBConnectionManager();                 

            /*
            if ( v_gadmin.equals("H") ) { 		// 	��0�׷����
                sql  = "\n select distinct a.comp, a.compnm as companynm";
                sql += "\n from   tz_comp a, tz_grcomp b,";
                sql += "\n        (select grcode from tz_grcodeman ";
                sql += "\n         where  userid = " + SQLString.Format(s_userid);
                sql += "\n         and    gadmin = " + SQLString.Format(s_gadmin) + ") c ";
                sql += "\n where  a.comp = b.comp ";
                sql += "\n and    b.grcode = c.grcode ";
                sql += "\n order  by a.compnm";  		
            }
            else if ( v_gadmin.equals("K") ) { 		// 	ȸ�����, �μ�����
                sql  = "\n select distinct a.comp, a.compnm as companynm";
                sql += "\n from   tz_comp a, tz_compman b";
                sql += "\n where  a.comp = b.comp ";
                sql += "\n and    b.userid = " + SQLString.Format(s_userid);
                sql += "\n and    b.gadmin = " + SQLString.Format(s_gadmin);
                sql += "\n order  by a.compnm"; 		
            }		
            else { 		// 	Ultravisor, Supervisor, ������, ����
                sql  = "\n select distinct comp, compnm as companynm";
                sql += "\n from   tz_comp ";	
                sql += "\n order  by compnm";  	 		
            }
            */
            
            sql += " select                                       \n";
            sql += "   b.comp,                                    \n";
            sql += "   b.compnm companynm                         \n";
            sql += " from                                         \n";
            sql += "   tz_grcomp a, tz_compclass b                \n";
            sql += " where                                        \n";
            sql += "   grcode = " +SQLString.Format(ss_grcode) + " \n";
            sql += "   and a.comp = b.comp                        \n";
            
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);       
            
            // if ( v_gadmin.equals("A") || v_gadmin.equals("F") || v_gadmin.equals("P") || v_gadmin.equals("H") ) {     
                 //      Ultra/Super or ������ or ���� or ��0�׷���� �� ��� 'ALL' ȸ�� ��� 
            result += getSelectTag( ls, isChange, isALL, "s_grcomp", ss_grcomp);
            // }
            // else { 
            //    result += getSelectTag( ls, isChange, false, "s_company", ss_company);
            // }
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
    
    
    
    
            
    public static String getSelectTag(ListSet ls, boolean isChange, boolean isALL, String selname, String optionselected) throws Exception { 
        StringBuffer sb = null;
        
        try { 
            sb = new StringBuffer();  

            sb.append("<select name = \"" + selname + "\"");
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
               
                sb.append(" > " + ls.getString(columnCount) + "</option > \r\n");
            }
            sb.append("</select > \r\n");
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage() );
        }
        return sb.toString();
    }
    
    public static String getCompanyCommonAdm(RequestBox box, boolean isChange, boolean isALL) throws Exception {
    	DBConnectionManager connMgr = null;
    	PreparedStatement pstmt = null;
    	ListSet ls = null;
    	String sql = "";
    	String result = "";
    	
    	try {
    		/*
    	       String s_gadmin = box.getSession("gadmin");
    	       String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
    	       String s_userid = box.getSession("userid");
    		 */
    		String ss_company = box.getStringDefault("s_company","ALL");
    		
    		connMgr = new DBConnectionManager();
    		
    		sql = "select distinct a.comp, a.compnm as companynm \n";
    		sql +=" from TZ_COMPCLASS a, TZ_SUBJ b\n";
    		sql +=" where isdeleted = 'N' and a.comp=b.cpsubj order by a.compnm ";
    		
    		/*
    	       pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

    	       ls = new ListSet(pstmt);
    		 */
    		ls = connMgr.executeQuery(sql);
    		/*
    	       if(v_gadmin.equals("A") || v_gadmin.equals("F") || v_gadmin.equals("P") || v_gadmin.equals("H")) {
    	            //      Ultra/Super or ��d���� or ���� or ��0�׷���� �� ��� 'ALL' ȸ�� ���
    	           result += getSelectTag(ls, isChange, isALL, "s_company", ss_company);
    	       }
    	       else {
    	           result += getSelectTag(ls, isChange, false, "s_company", ss_company);
    	       }
    		 */
    		result += getSelectTag(ls, isChange, isALL, "s_company", ss_company);
    	}
    	catch (Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, true);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
    	}
    	finally {
    		if(ls != null) { try { ls.close(); }catch (Exception e) {} }
    		if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
    	}
    	return result;
    }    
}
