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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
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
public class SelectEduBean { 

   /**
   * �ڷ�� listȭ�� select
   */
    public ArrayList getGrcode(RequestBox box) throws Exception { 
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

            if ( v_gadmin.equals("F") || v_gadmin.equals("P") ) {      //  ������, ����
                sql  = "select distinct                                                     " +
                       "        a.grcode,                                                   " +
                       "        a.grcodenm                                                  ";
                sql += "  from                                                              " +
                       "        tz_grcode a,                                                " +
                       "        tz_grsubj b,                                                ";
                sql += "        (                                                           " +
                       "         select                                                     " +
                       "                subj                                                " +
                       "           from                                                     " +
                       "                tz_subjman                                          " +
                       "          where                                                     " +
                       "                userid = " + SQLString.Format(s_userid) + "         " +
                       "            and gadmin = " + SQLString.Format(s_gadmin) + "         " +
                       "        ) c                                                         ";
                sql += " where                                                              " +
                       "        a.grcode     = b.grcode                                     " +
                       "   and  b.subjcourse = c.subj                                       ";
                sql += " order by a.grcodenm                                                ";
            }
            else if ( v_gadmin.equals("H") ) {     //  ��0�׷����
                sql  = "select distinct                                                     " +
                       "        a.grcode,                                                   " +
                       "        a.grcodenm                                                  ";
                sql += " from                                                               " +
                       "        tz_grcode a,                                                " +
                       "        tz_grcodeman b                                              ";
                sql += " where                                                              " +
                       "        a.grcode = b.grcode                                         " +
                       "   and  b.userid = " + SQLString.Format(s_userid) + "               " +
                       "   and  b.gadmin = " + SQLString.Format(s_gadmin) + "               ";
                sql += " order by a.grcodenm                                                ";
            }
            else if ( v_gadmin.equals("K") ) {     //  ȸ�����, �μ�����
                sql  = "select distinct                                                     " +
                       "        a.grcode,                                                   " +
                       "        a.grcodenm                                                  ";
                sql += "  from                                                              " +
                       "        tz_grcode a,                                                " +
                       "        tz_grcomp b,                                                ";
                sql += "        (                                                           " +
                       "        select                                                      " +
                       "                comp                                                " +
                       "          from                                                      " +
                       "                tz_compman                                          " +
                       "         where                                                      " +
                       "                userid = " + SQLString.Format(s_userid) + "         " +
                       "           and  gadmin = " + SQLString.Format(s_gadmin) + "         " +
                       "        ) c                                                         ";
                sql += " where                                                              " +
                       "        a.grcode = b.grcode                                         " +
                       "   and  substr(b.comp, 1, 4) = substr(c.comp, 1, 4)                 ";
                sql += " order by a.grcodenm                                                ";
            }
            else {      //  Ultravisor, Supervisor
                sql  = "select                      " +
                       "        grcode,             " +
                       "        grcodenm            ";
                sql += "  from                      " +
                       "        tz_grcode           ";
                sql += " order by grcodenm          ";
            }

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);

            if ( v_gadmin.equals("A") ) {      //      Ultra/Super �� ��� 'ALL' ��0�׷� ���
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
    
    
    public ArrayList getGyear(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        try { 
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�

            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select distinct gyear ";
            sql += "   from tz_grseq       ";
            sql += "  order by gyear desc  ";

            ls = connMgr.executeQuery(sql);

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


    /**
    ��0���
    @param box      receive from the form object
    @return ArrayList
    */    
    public ArrayList getGrseq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        try { 
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            String  ss_gyear    = box.getString("s_gyear");     // �⵵

            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql = "select grseq, grseqnm";
            sql += " from tz_grseq";
            sql += " where gyear = " + SQLString.Format(ss_gyear);
            sql += " and grcode = " + SQLString.Format(ss_grcode);
            sql += " order by grseq";

            ls = connMgr.executeQuery(sql);

            dbox = this.setAllSelectBox( ls);// System.out.println("dbox.size() : " + dbox.size() );
            list.add(dbox);// System.out.println("list.size() : " + list.size() );

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
    
// ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == =   
    public static String getGrcode(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        //String result = "��0�׷� ";
        String result = "";
        
        try { 
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String v_gadmin2 = StringManager.substring(s_gadmin, 0, 2);
            String s_userid = box.getSession("userid");
            String  ss_grcode   = box.getStringDefault("s_grcode","N000001");    // ��0�׷�

            connMgr = new DBConnectionManager();

            if ( v_gadmin.equals("F") || v_gadmin.equals("P") ) {      //  ������, ����
                sql  = "\n select distinct a.grcode, a.grcodenm";
                sql += "\n from tz_grcode a, tz_grsubj b,";
                sql += "\n (select subj from tz_subjman where userid = " + SQLString.Format(s_userid) + " and gadmin = " + SQLString.Format(s_gadmin) + ") c";
                sql += "\n where a.grcode = b.grcode and b.subjcourse = c.subj";
                sql += "\n order by a.grcodenm";
            }
            else if ( v_gadmin.equals("H") ) {     //  ��0�׷����
                sql  = "\n select distinct a.grcode, a.grcodenm";
                sql += "\n from tz_grcode a, tz_grcodeman b";
                sql += "\n where a.grcode = b.grcode and b.userid = " + SQLString.Format(s_userid) + " and b.gadmin = " + SQLString.Format(s_gadmin);
                sql += "\n order by a.grcodenm";
            }
            else if ( v_gadmin.equals("K") ) {     //  ȸ�����, �μ�����
                sql  = "\n select distinct a.grcode, a.grcodenm";
                sql += "\n from   tz_grcode a, tz_grcomp d, tz_compclass b ";
                sql += "\n      , (select comp from tz_compman where userid = " + SQLString.Format(s_userid) + " and gadmin = " + SQLString.Format(v_gadmin2) + ") c";
                sql += "\n where  a.grcode = d.grcode ";
                sql += "\n and    d.comp = b.comp ";
                sql += "\n and    b.comp = c.comp";
                sql += "\n order  by a.grcodenm";
            }
            else {      //  Ultravisor, Supervisor
                sql  = " select grcode, grcodenm";
                sql += " from tz_grcode";
                sql += " order by grcodenm";
            }

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);     
            
            result += getSelectTag( ls, isChange, isALL, "s_grcode", ss_grcode);
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
    ��0�⵵
    @param box      receive from the form object
    @param isChange  
    @return String
    */    
    public static String getGyear(RequestBox box, boolean isChange) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        //String result = "���� ";
        String result = "";
        
        try { 
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            // String  ss_gyear = box.getString("s_gyear");          // ��0�⵵
			String  ss_gyear = box.getString("s_gyear");          // ��0�⵵
            
            connMgr = new DBConnectionManager();

            sql  = " select distinct gyear ";
            sql += "   from tz_grseq       ";

       
			if ( !ss_grcode.equals("ALL") ) { 
				     sql += "   where grcode = " + SQLString.Format(ss_grcode);
			}
            sql += "  order by gyear desc  ";

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);     
            
            result += getSelectTag( ls, isChange, false, "s_gyear", ss_gyear);
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
    ��0�⵵
    @param box      receive from the form object
    @param isChange  
    @return String
    */    
    public static String getGyear(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        
        try { 
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            // String  ss_gyear = box.getString("s_gyear");          // ��0�⵵
			String  ss_gyear = box.getString("s_gyear");          // ��0�⵵
			
            if((!isALL) && ss_gyear.equals("ALL")) {
            ss_gyear = FormatDate.getDate("yyyyMMdd").substring(0,4);
         	   box.remove("s_gyear");
         	   box.put("s_gyear", ss_gyear);
            }
            
            connMgr = new DBConnectionManager();

            sql  = " select distinct gyear ";
            sql += "   from tz_grseq       ";

       
			if ( !ss_grcode.equals("ALL") ) { 
				     sql += "   where grcode = " + SQLString.Format(ss_grcode);
			}
            sql += "  order by gyear desc  ";

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);     
            
            result += getSelectTag( ls, isChange, isALL, "s_gyear", ss_gyear);
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
    public static String getGrseq_back(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        //String result = "��0��� ";
        String result = "";
        
        try { 
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            String  ss_gyear    = box.getString("s_gyear");     // ��0�⵵
            String  ss_grseq   = box.getStringDefault("s_grseq","ALL");    // ��0���

            connMgr = new DBConnectionManager();

            sql = "select grseq, grseqnm";
            sql += " from tz_grseq";
            
            if ( !ss_gyear.equals("") ) { 
              sql += " where gyear = " + SQLString.Format(ss_gyear);
            } else { 
              sql += " where gyear = (select max(gyear) from tz_grseq where grcode = " + SQLString.Format(ss_grcode) + ")";
            }

            sql += " and grcode = " + SQLString.Format(ss_grcode);
            sql += " order by grseq desc";
//System.out.println("///"+sql);
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);     

            result += getSelectTag( ls, isChange, isALL, "s_grseq", ss_grseq);
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
    public static String getGrseq(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        //String result = "��0��� ";
        String result = "";
        
        try { 
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            String  ss_gyear    = box.getString("s_gyear");     // ��0�⵵
            String  ss_grseq   = box.getStringDefault("s_grseq","ALL");    // ��0���
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);

            connMgr = new DBConnectionManager();
            if ( v_gadmin.equals("F") || v_gadmin.equals("P") ) { //�����϶� �߰�
            	sql+=" select a.grseq, a.grseqnm from                             ";
            	sql+=" tz_grseq a,                                                ";
            	sql+=" (select c.grseq  as grseq from  tz_subjman b, tz_subjseq c ";
            	sql+=" where b.userid='kckwun'                                    ";
            	sql+=" and   b.subj = c.subj                                      ";
            	sql+=" ) b                                                        ";
            	sql+=" where a.grseq = b.grseq		  							  ";

            	if ( !ss_gyear.equals("") ) { 
	              sql += " and a.gyear = " + SQLString.Format(ss_gyear);
	            } else { 
	              sql += " and a.gyear = (select max(gyear) from tz_grseq where grcode = " + SQLString.Format(ss_grcode) + ")";
	            }
            	
            	sql += " and a.grcode = " + SQLString.Format(ss_grcode);
                sql += " order by a.grseq desc";
                
            }else{
            	sql = "select grseq, grseqnm";
	            sql += " from tz_grseq";
	            
	            if ( !ss_gyear.equals("") ) { 
	              sql += " where gyear = " + SQLString.Format(ss_gyear);
	            } else { 
	              sql += " where gyear = (select max(gyear) from tz_grseq where grcode = " + SQLString.Format(ss_grcode) + ")";
	            }
	            
	            sql += " and grcode = " + SQLString.Format(ss_grcode);
	            sql += " order by grseq desc";
            }
                      
//System.out.println(":::::::::"+box.getSession("gadmin")  );
//System.out.println("//asas/"+sql);
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);     

            result += getSelectTag( ls, isChange, isALL, "s_grseq", ss_grseq);
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
    
    public static String getBlendedGrseq(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        //String result = "��0��� ";
        String result = "";
        
        try { 
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            String  ss_gyear    = box.getString("s_gyear");     // ��0�⵵
            String  ss_grseq   = box.getStringDefault("s_grseq","ALL");    // ��0���

            connMgr = new DBConnectionManager();

            sql = "select grseq, grseqnm";
            sql += " from tz_grseq";
            
            if ( !ss_gyear.equals("") ) { 
              sql += " where gyear = " + SQLString.Format(ss_gyear);
            } else { 
              sql += " where gyear = (select max(gyear) from tz_grseq where grcode = " + SQLString.Format(ss_grcode) + ")";
            }

            sql += " and grcode = " + SQLString.Format(ss_grcode);
            sql += " and isblended = 'Y' ";
            sql += " order by grseq";

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);     

            result += getSelectTag( ls, isChange, isALL, "s_grseq", ss_grseq);
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
        
    public static String getDamunGyear(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        //String result = "���� ";
        String result = "";
        
        try { 
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            String  ss_gyear = box.getString("s_gyear");          // ��0�⵵
            
            connMgr = new DBConnectionManager();

            sql  = " select distinct gyear ";
            sql += "   from tz_grseq       ";
            sql += "   where grcode = " + SQLString.Format(ss_grcode);
            sql += "  order by gyear desc  ";

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);     
            
            result += getSelectTag( ls, isChange, false, "s_damungyyyy", ss_gyear);
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
        boolean isSelected = false;
        String v_tmpselname = "";
        
        try { 
            sb = new StringBuffer();  
            
            if ( selname.equals("s_damungyyyy") ) { 
                v_tmpselname = "s_gyear";
                sb.append("<select name = \"" + v_tmpselname + "\"");
            }
            else { 
                sb.append("<select id = \"" + selname + "\" name = \"" + selname + "\"");
            }
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

                if ( optionselected.equals( ls.getString(1)) && !isSelected) { 
                    sb.append(" selected");
                    isSelected = true;
                }
                else if ( selname.equals("s_gyear") && ls.getString("gyear").equals(FormatDate.getDate("yyyy")) && optionselected.equals("") && !isSelected) {     //      ����ڵ������ ����
                    sb.append(" selected");
                    isSelected = true;
                }    
               
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

    /**
    ��0�׷� select box
    @param selectname       select box name
    @param selected         selected valiable
    @param allcheck         all check Y(1),all check N(0)
    @return int
    */
    public static String getGrcodeSelect (String selectname, String selected, int allcheck) throws Exception { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        ListSet             ls      = null;
        String result = null;
        String              sql     = "";

        result = "  <SELECT name=" + selectname + " > \n";

        if ( allcheck == 1) { 
          result += " <option value='' >== =��ü == =</option > \n";
        }

        try { 
            connMgr = new DBConnectionManager();

            sql  = "select grcode, grcodenm from tz_grcode  ";
            sql += " order by grcodenm";

            ls = connMgr.executeQuery(sql);
//            System.out.println("selected == = > " +selected);
            while ( ls.next() ) { 
                result += " <option value=" + ls.getString("grcode");
                if ( selected.equals( ls.getString("grcode"))) { 
                    result += " selected ";
                }

                result += " > " + ls.getString("grcodenm") + "</option > \n";
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        result += "  </SELECT > \n";
        return result;
    }

}
