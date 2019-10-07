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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.ziaan.library.ConfigSet;
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
public class SelectSubjBean { 

   /**
   * ��з� SELECT
   * @param box          receive from the form object and session
   * @return ArrayList   ��з� ����Ʈ
   */
    public ArrayList getUpperClass(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
            String isCourse = box.getStringDefault("isCourse", "N");        //      �ڽ��� �־�� �ϴ��� ����
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();

            sql  = "select                          \n" +
                   "        upperclass,             \n" +
                   "        classname               \n";
            sql += "  from                          \n" +
                   "        tz_subjatt              \n";  
            sql += " where                          \n" +
                   "        middleclass = '000'     \n";
            sql += "   and  lowerclass = '000'      \n";    
            if ( isCourse.equals("N") ) {      //     �ڽ��з� ���
                sql += "   and  upperclass != 'COUR'    \n";    
            }            
            sql += " order by classname             \n";          
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);       
            
            dbox = this.setAllSelectBox(ls);
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
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }    
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }      
     
   /**
   * ��з� SELECT (ALL f��)
   * @param box          receive from the form object and session
   * @return ArrayList   ��з� ����Ʈ
   */
    public ArrayList getOnlyUpperClass(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
            // String isCourse = box.getStringDefault("isCourse", "N");        //      �ڽ��� �־�� �ϴ��� ����
            String isCourse = "N";        //      �ڽ��� �־�� �ϴ��� ����
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();

            sql = "select upperclass, classname";
            sql += " from tz_subjatt";  
            sql += " where middleclass = '000'";
            sql += " and lowerclass = '000'";    
            
            if ( isCourse.equals("N") ) {      //     �ڽ��з� ���
                sql += " and upperclass != 'COUR'";    
            }
            
            sql += " order by classname";          
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);       
            
            // dbox = this.setAllSelectBox( ls);
            // list.add(dbox);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
        	ex.printStackTrace();
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
    
    
    
    
   /**
   * ��з� SELECT (ALL f��)
   * @param box          receive from the form object and session
   * @return ArrayList   ��з� ����Ʈ
   */
    public ArrayList getOnlyUpperClass(RequestBox box, String firststr) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        StringTokenizer st = null;
        String upperstr = "";
        
        try { 
            // String isCourse = box.getStringDefault("isCourse", "N");        //      �ڽ��� �־�� �ϴ��� ����
            String isCourse = "N";        //      �ڽ��� �־�� �ϴ��� ����
            connMgr = new DBConnectionManager();            
            if ( firststr.indexOf(",") > 0) { 
              st      = new StringTokenizer(firststr,",");
              while ( st.hasMoreElements() ) { 
                upperstr += StringManager.makeSQL((String)st.nextToken() ) + ",";
              }
              upperstr += "''";
            } else { 
              upperstr = StringManager.makeSQL(firststr);
            }
            //System.out.println("upperstr ==  == = >>  >>  >>  > " +upperstr);
            
            
            list = new ArrayList();
            sql = "select upperclass, classname";
            sql += " from tz_subjatt";  
            sql += " where middleclass = '000'";
            sql += " and lowerclass = '000'";    
            // sql += " and substr(upperclass, 0, 1) in (" +upperstr + ")";
            sql += " and upperclass in (" +upperstr + ")";
            sql += " order by upperclass";
            //System.out.println("sql ==  == = >>  >>  >>  > " +sql);
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls = new ListSet(pstmt);       
            
            // dbox = this.setAllSelectBox( ls);
            // list.add(dbox);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
        	ex.printStackTrace();
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
    

   /**
   * �ߺз� SELECT
   * @param box          receive from the form object and session
   * @return ArrayList   �ߺз� ����Ʈ
   */
    public ArrayList getMiddleClass(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
            String  ss_upperclass = box.getStringDefault("s_upperclass","ALL");
            
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();

            sql = "select middleclass, classname";
            sql += " from tz_subjatt";  
            sql += " where upperclass = " + SQLString.Format(ss_upperclass);      
            sql += " and lowerclass = '000'";      
            sql += " order by classname";         
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
    
    /**
   * �ߺз� SELECT
   * @param box          receive from the form object and session
   * @return ArrayList   �ߺз� ����Ʈ
   */
    public ArrayList getOnlyMiddleClass(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
            String  ss_upperclass = box.getStringDefault("s_upperclass","ALL");
            
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();

            sql = "select middleclass, classname";
            sql += " from tz_subjatt";  
            sql += " where upperclass = " + SQLString.Format(ss_upperclass);      
            sql += " and lowerclass = '000'";      
            sql += " order by classname";         
            // System.out.print("sql_middleclass=" +sql);
            ls = connMgr.executeQuery(sql); 
            
            // dbox = this.setAllSelectBox( ls);
            // list.add(dbox);

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
   * �Һз� SELECT
   * @param box          receive from the form object and session
   * @return ArrayList   �Һз� ����Ʈ
   */
    public ArrayList getLowerClass(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
            String  ss_upperclass = box.getStringDefault("s_upperclass","ALL");
            String  ss_middleclass = box.getStringDefault("s_middleclass","ALL");
            
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();

            sql  = " select lowerclass, classname";
            sql += " from   tz_subjatt";  
            sql += " where  upperclass = " + SQLString.Format(ss_upperclass);
            sql += " and    middleclass = " + SQLString.Format(ss_middleclass);            
            sql += " order  by classname";          
            
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
        
   /**
   * ��� SELECT
   * @param box          receive from the form object and session
   * @return ArrayList   ��� ����Ʈ
   */
    public ArrayList getSubj(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
        	//System.out.println();
            String isCourse = box.getStringDefault("isCourse", "Y");        //      �ڽ��� �־�� �ϴ��� ����
            String isOffSubj = box.getStringDefault("isOffSubj", "N");       //      ���հ�� ����
            
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_upperclass = box.getStringDefault("s_upperclass","ALL");
            String  ss_middleclass = box.getStringDefault("s_middleclass","ALL");
            String  ss_lowerclass = box.getStringDefault("s_lowerclass","ALL");
            
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            String  ss_grseq   = box.getStringDefault("s_grseq","ALL");    // ��0���
            
            String ss_subjsearchkey = box.getSession("subjsearchkey");
            
            connMgr = new DBConnectionManager();            

            list = new ArrayList();
            
            if ( v_gadmin.equals("F") || v_gadmin.equals("P") ) {      //  ������, ����
                sql = "select distinct a.subj, a.subjnm";
                sql += " from tz_subj a, tz_subjman b";
                sql += " where 1=1 ";// a.isapproval = 'Y' 
				sql += "   and a.subj = b.subj and b.userid = " + SQLString.Format(s_userid) + " and b.gadmin = " + SQLString.Format(s_gadmin);
                if ( isOffSubj.equals("Y") ) { sql += " and a.isonoff = 'OFF'  "; }
                sql += " order by a.subjnm";      
            }
            else if ( v_gadmin.equals("H") ) {     //  ��0�׷����
                sql = "select distinct a.subj, a.subjnm";
                sql += " from tz_subj a, ";
                sql += " (select e.subjcourse as subj from tz_grsubj e, tz_grcodeman f where 1=1 ";// a.isapproval = 'Y' 
				sql += "    and f.userid = " + SQLString.Format(s_userid) + " and f.gadmin = " + SQLString.Format(s_gadmin);
                sql += " and e.grcode = f.grcode) b";
                sql += " where a.subj = b.subj";
                if ( isOffSubj.equals("Y") ) { sql += " and a.isonoff = 'OFF'  "; }
                sql += " order by a.subjnm";          
            }
            else if ( v_gadmin.equals("K") ) {     //  ȸ�����, �μ�����
                sql = "select distinct a.subj, a.subjnm";
                sql += " from tz_subj a, tz_grsubj b,";
                sql += " (select e.grcode from tz_grcomp e, tz_compman f where 1=1 ";// a.isapproval = 'Y' 
				sql += "    and f.userid = " + SQLString.Format(s_userid) + " and f.gadmin = " + SQLString.Format(s_gadmin);
                sql += " and substr(e.comp, 1, 4) = substr(f.comp, 1, 4)) c";
                sql += " where a.subj = b.subjcourse and b.grcode = c.grcode";
                if ( isOffSubj.equals("Y") ) { sql += " and a.isonoff = 'OFF'  "; }
                sql += " order by a.subjnm";          
            }   
            else {      //  Ultravisor, Supervisor
                sql = "select distinct a.subj, a.subjnm from tz_subj a, tz_grsubj b, tz_subjseq c where 1=1 "; // a.isapproval = 'Y' ";
                if ( !ss_upperclass.equals("ALL") && ss_middleclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && !ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                    sql += " and a.lowerclass = " + SQLString.Format(ss_lowerclass);
                }
                if ( !ss_grcode.equals("ALL") && ss_grseq.equals("ALL") ) {       //      ��0�׷츸 ���õ� ���
                        sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.subj = b.subjcourse";
                    }
                if ( !ss_grcode.equals("ALL") && !ss_grseq.equals("ALL") ) {      //      ��0�׷�� ��0��� ���õ� ���
                    sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.subj = b.subjcourse";
                    sql += " and c.grseq = " + SQLString.Format(ss_grseq) + " and a.subj = c.subj";
                    sql += " and c.course = '000000'";
                }
                
                
                if ( isOffSubj.equals("Y") ) { sql += " and isonoff = 'OFF'  "; }// ���հ��

                if ( isCourse.equals("Y") ) {      //     �ڽ��� �ִ�
                    sql += " union";
                    sql += " select distinct a.course, a.coursenm from tz_course a, tz_grsubj b, tz_courseseq c where 1=1";            
                    if ( !ss_upperclass.equals("ALL") && ss_middleclass.equals("ALL") ) { 
                        sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    }
                    else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && ss_lowerclass.equals("ALL") ) { 
                        sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                        sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                    }
                    else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && !ss_lowerclass.equals("ALL") ) { 
                        sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                        sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                        sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                    }
                    if ( !ss_grcode.equals("ALL") && ss_grseq.equals("ALL") ) {       //      ��0�׷츸 ���õ� ���
                        sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.course = b.subjcourse";
                    }
                    if ( !ss_grcode.equals("ALL") && !ss_grseq.equals("ALL") ) {      //      ��0�׷�� ��0��� ���õ� ���
                        sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.course = b.subjcourse";
                        sql += " and c.grseq = " + SQLString.Format(ss_grseq) + " and a.course = c.course"; 
                    }
                }
                sql += " order by 2"; 
            }
            
            ls = connMgr.executeQuery(sql); 
			//System.out.println("d�ĵ� ����Ÿ" +sql);
            
            if ( v_gadmin.equals("A") || v_gadmin.equals("H") || v_gadmin.equals("K") ) {      //      Ultra/Super or ��0�׷���� or ȸ��/�μ�/�μ����� ��� 'ALL' ��� ��� 
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
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }  




   /**
   * ����� SELECT
   * @param box          receive from the form object and session
   * @return ArrayList   ����� ����Ʈ
   */
    public ArrayList getSubjseq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        try { 
            String  ss_subjcourse = box.getStringDefault("s_subjcourse", "ALL");
            String  ss_gyear    = box.getString("s_gyear");     //  �⵵
            
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            String  ss_grseq   = box.getStringDefault("s_grseq","ALL");    //   ��0���
            
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();

            sql  = "select distinct decode(b.course, '000000', a.subjseq, b.courseseq) subjseq	\n";
            sql += "from   tz_subjseq a, tz_courseseq b											\n";      
            sql += "where  decode(b.course, '000000', a.subj, b.course) = " + SQLString.Format(ss_subjcourse) + " \n";
            sql += "and    decode(b.course, '000000', a.gyear, b.gyear) = " + SQLString.Format(ss_gyear) + " \n";
            if ( !ss_grcode.equals("ALL") && ss_grseq.equals("ALL") && !ss_subjcourse.equals("ALL") ) {       //      ��0�׷츸 ���õ� ���
                sql += " and decode(b.course, '000000', a.grcode, b.grcode) = " + SQLString.Format(ss_grcode) + " \n";
            }
            else if ( !ss_grcode.equals("ALL") && !ss_grseq.equals("ALL") && !ss_subjcourse.equals("ALL") ) {      //      ��0�׷�� ��0��� ���õ� ���
                sql += "and    decode(b.course, '000000', a.grcode, b.grcode) = " + SQLString.Format(ss_grcode) + " \n";
                sql += "and    decode(b.course, '000000', a.grseq, b.grseq) = " + SQLString.Format(ss_grseq) + " \n";
            }
            sql += "order  by decode(b.course, '000000', a.subjseq, b.courseseq) \n";
            
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
   
   /**
   * ������ SELECT
   * @param box          receive from the form object and session
   * @return ArrayList   ������ ����Ʈ
   */
    public ArrayList getSubjLesson(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        try { 
            String  ss_subj = box.getStringDefault("s_subj","ALL");

            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select lesson, sdesc    ";
            sql += "   from tz_subjlesson    ";
            sql += "  where subj = " + SQLString.Format(ss_subj);
            sql += "  order by lesson        ";

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

   /**
   * ������ SELECT without ALL 
   * @param box          receive from the form object and session
   * @return ArrayList   ������ ����Ʈ
   */
    public ArrayList getSubjLesson2(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        try { 
            String  ss_subj = box.getStringDefault("s_subjcourse","ALL");

            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select lesson, sdesc    ";
            sql += "   from tz_subjlesson    ";
            sql += "  where subj = " + SQLString.Format(ss_subj);
            sql += "  order by lesson        ";

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
     * ��� class d��
     */
    public ArrayList getSubjClass(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        try { 
            String  ss_subjcourse = box.getStringDefault("s_subjcourse","ALL");

            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select class , classnm \n";
            sql += " from   tz_class        \n";
            sql += " where  subj    = " + SQLString.Format(ss_subjcourse) + " \n";
            sql += " and    year    =" + SQLString.Format(box.getString("s_gyear") ) + " \n";
            sql += " and    subjseq =" + SQLString.Format(box.getString("s_subjseq") ) + " \n";
            sql += " order  by class        \n" ;

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
            columnCount = meta.getColumnCount();
            for ( int i = 1; i <= columnCount; i++ ) { 
                String columnName = meta.getColumnName(i).toLowerCase();
                
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


// ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==    
    public static String getSubjGubun(RequestBox box, boolean isChange, boolean isALL, boolean isStatisticalPage) throws Exception { 
    	DBConnectionManager	connMgr	= null;
    	PreparedStatement   pstmt   = null;        
    	ListSet             ls      = null;
    	String              sql     = "";
    	//String result = "��з� ";
    	String result = "";
    	boolean isVisible = false;
    	String  v_comp      = box.getSession("comp");
    	
    	try { 
    		isVisible = getIsVisible(isStatisticalPage, 1);     //     
    		
    		if ( isVisible) {        
    			String  ss_subjgubun   = box.getStringDefault("s_subjgubun","ALL");   
    			String  subjgubuncnt   = box.getStringDefault("subjgubuncnt","ALL");    
    			
    			connMgr = new DBConnectionManager();
    			sql =  " select code, codenm ";
    			sql += "   from tz_code ";
    			sql += "  where gubun = '0004' ";
    			sql += "  order by codenm ";
    			
    			pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    			
    			ls = new ListSet(pstmt);     
    			if(box.getString("go_gubun").equals("Y")){
    				result += getSelectTag_go( ls, isChange, isALL, "s_subjgubun", ss_subjgubun, subjgubuncnt);
    			}
    			else{
    				result += getSelectTag( ls, isChange, isALL, "s_subjgubun", ss_subjgubun);
    			}
    		}
    		else { 
    			result = "";
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
    public static String getUpperClass(RequestBox box, boolean isChange, boolean isALL, boolean isStatisticalPage) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        //String result = "��з� ";
        String result = "";
        boolean isVisible = false;
    	String  v_comp      = box.getSession("comp");
        
        try { 
            isVisible = getIsVisible(isStatisticalPage, 1);     //      UpperClass = 1;
            							
            if ( isVisible) {        
                String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    // 1�ܰ� ���з�
                String  upperclasscnt   = box.getStringDefault("upperclasscnt","ALL");    // 1�ܰ� ���з�
    
                connMgr = new DBConnectionManager();
                if("1002".equals(v_comp)) {   
	                sql  = "select distinct " +
	                       "        upperclass, " +
	                       "        classname";
	                sql += "  from " +
	                       "        tz_subjatt_ktf";  
	                sql += " where " +
	                       "        middleclass = '000'";
	                sql += "   and  lowerclass = '000'";    
	                
	                ConfigSet conf = new ConfigSet();
	                
	                if ( !conf.getBoolean("course.use") ) {      //     �ڽ��з� ���
	                    sql += " and upperclass != 'COUR'";    
	                }
	                //sql += " order by classname";
	                sql += " order by upperclass";
                } else {
                  sql  = "select distinct " +
	                     "        upperclass, " +
	                     "        classname";
	              sql += "  from " +
	                     "        tz_subjatt";  
	              sql += " where " +
	                     "        middleclass = '000'";
	              sql += "   and  lowerclass = '000'";    
	              
	              ConfigSet conf = new ConfigSet();
	              
	              if ( !conf.getBoolean("course.use") ) {      //     �ڽ��з� ���
	                  sql += " and upperclass != 'COUR'";    
	              }
	              //sql += " order by classname";
	              sql += " order by upperclass";
                }
    
                pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                
                ls = new ListSet(pstmt);     
                if(box.getString("go_gubun").equals("Y")){
                	result += getSelectTag_go( ls, isChange, isALL, "s_upperclass", ss_upperclass, upperclasscnt);
                }
                else{
                	result += getSelectTag( ls, isChange, isALL, "s_upperclass", ss_upperclass);
                }
            }
            else { 
                result = "";
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
        
    public static String getMiddleClass(RequestBox box, boolean isChange, boolean isALL, boolean isStatisticalPage) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        //String result = "�ߺз� ";
        String result = "";
        boolean isVisible = false;
    	String  v_comp      = box.getSession("comp");
        
        try { 
            isVisible = getIsVisible(isStatisticalPage, 2);     //      MiddleClass = 2;
            
            if ( isVisible) { 
                String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    //     1�ܰ� ���з�
                String  ss_middleclass   = box.getStringDefault("s_middleclass","ALL");    //       2�ܰ� ���з�
                String  ss_lowerclass   = box.getStringDefault("s_middleclass","ALL");    //       2�ܰ� ���з�
                String  cnt = box.getString("cnt");
                
                connMgr = new DBConnectionManager();
                if("1002".equals(v_comp)) {   
	                sql  = "select distinct " +
	                       "        middleclass, " +
	                       "        classname";
	                sql += "  from " +
	                       "        tz_subjatt_ktf";  
	                sql += " where " +
	                       "        upperclass = " + SQLString.Format(ss_upperclass);      
	                sql += "   and  middleclass != '000'";     
	                sql += "   and  lowerclass = '000'";      
	                //sql += " order  by classname";         
	                sql += " order  by middleclass";         
                } else {
                	sql  = "select distinct " +
                    "        middleclass, " +
                    "        classname";
		             sql += "  from " +
		                    "        tz_subjatt";  
		             sql += " where " +
		                    "        upperclass = " + SQLString.Format(ss_upperclass);      
		             sql += "   and  middleclass != '000'";     
		             sql += "   and  lowerclass = '000'";      
		             //sql += " order  by classname";         
		             sql += " order  by middleclass";
                }
                pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                
                ls = new ListSet(pstmt);     
                if(box.getString("go_gubun").equals("Y")){
                	
                	result += getSelectTag_go( ls, isChange, isALL, "s_middleclass", ss_middleclass,cnt);
                }
                else{
                	result += getSelectTag( ls, isChange, isALL, "s_middleclass", ss_middleclass);
                }
            }
            else { 
                result = "";
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
    
    public static String getLowerClass(RequestBox box, boolean isChange, boolean isALL, boolean isStatisticalPage) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        //String result = "�Һз� ";
        String result = "";
        boolean isVisible = false;
        
        try { 
            isVisible = getIsVisible(isStatisticalPage, 3);     //      LowerClass = 3;
            
            if ( isVisible) { 
                String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    //     1�ܰ� ���з�
                String  ss_middleclass   = box.getStringDefault("s_middleclass","ALL");    //       2�ܰ� ���з�
                String  ss_lowerclass = box.getStringDefault("s_lowerclass","ALL");    //       3�ܰ� ���з�
                String  upperclasscnt = box.getString("cnt");    //       3�ܰ� ���з�
    
                connMgr = new DBConnectionManager();
    
                sql  = "select distinct " +
                       "        lowerclass, " +
                       "        classname";
                sql += "  from " +
                       "        tz_subjatt";  
                sql += " where " +
                       "        upperclass = " + SQLString.Format(ss_upperclass);
                sql += "   and  middleclass = " + SQLString.Format(ss_middleclass);       
                sql += "   and  lowerclass != '000'";     
                //sql += " order by classname";        
                sql += " order by lowerclass";        
    
                pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                
                ls = new ListSet(pstmt);     
                
                if(box.getString("go_gubun").equals("Y")){
                	result += getSelectTag_go( ls, isChange, isALL, "s_lowerclass", ss_lowerclass, upperclasscnt);
                }
                else{
                	result += getSelectTag( ls, isChange, isALL, "s_lowerclass", ss_lowerclass);
                }
            }
            else { 
                result = "";
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
    
    public static String getMasterSubj(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        return getMasterSubj(box, isChange, isALL, false, false);
    }
    
    public static String getMasterSubj(RequestBox box, boolean isChange, boolean isALL, boolean isTarget) throws Exception { 
        return getMasterSubj(box, isChange, isALL, false, isTarget);
    }
    
    
    public static String getMasterSubj(RequestBox box, boolean isChange, boolean isALL, boolean justOn, boolean isTarget) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        //String result = "�����Ͱ�d";
        String result = "";
        
        try {           
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_upperclass = box.getStringDefault("s_upperclass","ALL");
            String  ss_middleclass = box.getStringDefault("s_middleclass","ALL");
            String  ss_lowerclass = box.getStringDefault("s_lowerclass","ALL");
            
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            String  ss_gyear    = box.getStringDefault("s_gyear",FormatDate.getDate("yyyy") );    // ��0�׷�
            String  ss_grseq    = box.getStringDefault("s_grseq","ALL");    // ��0���
            String  ss_subjcourse   = box.getStringDefault("s_subjcourse","ALL");    // 
            String  ss_mastercd   = box.getStringDefault("s_mastercd","ALL");    // 
            
            connMgr = new DBConnectionManager();            
            
            // if ( v_gadmin.equals("F") || v_gadmin.equals("P") ) {      //  ������, ����
            //    sql = "select distinct a.subj, a.subjnm";
            //    sql += " from tz_subj a, tz_subjman b";
            //    sql += " where a.subj = b.subj and b.userid = " + SQLString.Format(s_userid) + " and b.gadmin = " + SQLString.Format(s_gadmin);
            //    if ( justOn) sql += " and a.isonoff = 'ON' "; 
            //    if ( justOff) sql += " and a.isonoff = 'OFF' "; 
            //    sql += " order by a.subj";      
            // }
            // else if ( v_gadmin.equals("H") ) {     //  ��0�׷����
            //    sql = "select distinct a.subj, a.subjnm";
            //    sql += " from tz_subj a, ";
            //    sql += " (select e.subjcourse as subj from tz_grsubj e, tz_grcodeman f where f.userid = " + SQLString.Format(s_userid) + " and f.gadmin = " + SQLString.Format(s_gadmin);
            //    sql += " and e.grcode = f.grcode) b";
            //    sql += " where a.subj = b.subj";
            //    if ( justOn) sql += " and a.isonoff = 'ON' "; 
            //    if ( justOff) sql += " and a.isonoff = 'OFF' "; 
            //    sql += " order by a.subj";          
            // }
            // else if ( v_gadmin.equals("K") ) {     //  ȸ�����, �μ�����
            //    sql = "select distinct a.subj, a.subjnm";
            //    sql += " from tz_subj a, tz_grsubj b,";
            //    sql += " (select e.grcode from tz_grcomp e, tz_compman f where f.userid = " + SQLString.Format(s_userid) + " and f.gadmin = " + SQLString.Format(s_gadmin);
            //    sql += " and substr(e.comp, 1, 4) = substr(f.comp, 1, 4)) c";
            //    sql += " where a.subj = b.subjcourse and b.grcode = c.grcode";
            //    if ( justOn) sql += " and a.isonoff = 'ON' "; 
            //    if ( justOff) sql += " and a.isonoff = 'OFF' "; 
            //    sql += " order by a.subj";          
            // }   
            // else {      //  Ultravisor, Supervisor
                sql = "select mastercd, masternm from tz_mastercd where 1=1";

                if ( (!ss_grcode.equals("ALL")||!ss_grcode.equals("----")) && (!ss_gyear.equals("ALL")||!ss_gyear.equals("----")) ) {      //      ��0�׷�� ��0�⵵�� ���õ� ���
                    sql += " and grcode = " + SQLString.Format(ss_grcode);
                    // sql += " and gyear  = " + SQLString.Format(ss_gyear);
                }
                
                sql += " and gyear  = " + SQLString.Format(ss_gyear);

                if ( (!ss_grcode.equals("ALL")||!ss_grcode.equals("----")) && (!ss_gyear.equals("ALL")||!ss_gyear.equals("----")) 
                		&& (!ss_grseq.equals("ALL")||!ss_grseq.equals("----")) ) {      //      ��0��� ���õ� ���
                    sql += " and grseq = " + SQLString.Format(ss_grseq);
                }

                if ( isTarget) { 
                	sql += " and isedutarget = 'Y'" ;
                }
                sql += " order by masternm";       
            // }
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);     
            
            if ( v_gadmin.equals("A") || v_gadmin.equals("H") || v_gadmin.equals("K") ) {      //      Ultra/Super or ��0�׷���� or ȸ��/�μ�/�μ����� ��� 'ALL' ��� ��� 
                result += getSelectTag( ls, isChange, isALL, "s_mastercd", ss_mastercd);
            }
            else { 
                result += getSelectTag( ls, isChange, false, "s_mastercd", ss_mastercd);
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


    public static String getBetaSubj(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        return getBetaSubj(box, isChange, isALL, false, false);
    }
    
    public static String getBetaSubj(RequestBox box, boolean isChange, boolean isALL, boolean justOn, boolean justOff) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "��� ";
        
        try {           
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_subjcourse   = box.getStringDefault("s_subjcourse","ALL");    // ��0���
            
            connMgr = new DBConnectionManager();            
            
            if ( s_gadmin.equals("A1") || s_gadmin.equals("A2") ) {      //  ������, ����
                sql = "select subj, subjnm from tz_betasubj order by subjnm";
            }
            else {      //  Ultravisor, Supervisor
                sql = "select a.subj, a.subjnm";
                sql += " from tz_betasubj a, tz_betacpinfo b";
                sql += " where a.company=b.betacpno ";
                sql += "   and a.cuserid=" +SQLString.Format(s_userid);
                sql += " order by a.subjnm";
                
            }

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);     
            
            if ( v_gadmin.equals("A") ) {      //      Ultra/Super or ��0�׷���� or ȸ��/�μ�/�μ����� ��� 'ALL' ��� ��� 
                result += getSelectTag( ls, isChange, isALL, "s_subjcourse", ss_subjcourse);
            }
            else { 
                result += getSelectTag( ls, isChange, false, "s_subjcourse", ss_subjcourse);
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

    
    public static String getSubj(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        return getSubj(box, isChange, isALL, false, false);
    }
    
    public static String getBLCourse(RequestBox box, boolean isChange, boolean isALL, boolean isDisplayNm) throws Exception { 
        return getBLCourse(box, isChange, isALL, false, false, isDisplayNm);
    }
    
    public static String getBLCoursenm(RequestBox box, boolean isChange, boolean isALL, boolean isDisplayNm) throws Exception { 
        return getBLCoursenm(box, isChange, isALL, false, false, isDisplayNm);
    }
    
    public static String getSubj(RequestBox box, boolean isChange, boolean isALL, boolean justOn, boolean justOff) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        //String result = "��� ";
        String result = "";
        
        try {           
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_subjgubun = box.getStringDefault("s_subjgubun","ALL");
            String  ss_upperclass = box.getStringDefault("s_upperclass","ALL");
            String  ss_middleclass = box.getStringDefault("s_middleclass","ALL");
            String  ss_lowerclass = box.getStringDefault("s_lowerclass","ALL");
            
            String  ss_grcode  = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            String  ss_grseq   = box.getStringDefault("s_grseq","ALL");    	// ��0���
            String  ss_gyear   = box.getString("s_gyear"); // ��0����
            
            String  ss_subjcourse   = box.getStringDefault("s_subjcourse","ALL");    // ��d�ڵ�
            String  s_subjsearchkey = box.getString("s_subjsearchkey");
        	s_subjsearchkey = s_subjsearchkey.replaceAll("'", "");
        	s_subjsearchkey = s_subjsearchkey.replaceAll("%", "/%").toUpperCase();

            String isOffSubj = box.getStringDefault("isOffSubj", "N");
            
            String v_isoutsourcing = box.getString("p_isoutsourcing");
            
            if("Y".equals(isOffSubj)) {
            	justOff = true;
            }
            
            connMgr = new DBConnectionManager();            
            
            if ( v_gadmin.equals("F") || v_gadmin.equals("P") ) {      //  ������, ����
                sql  = "select distinct         \n" +
                       "        a.subj,         \n" +
                       "        '[' || a.subj || '] ' || a.subjnm as subjnm  \n" +
                       "  from                  \n" +
                       "        tz_subj a,      \n" +
                       "        tz_subjman b    \n";

                if ( !ss_grcode.equals("ALL") ) { 
                    sql += " , tz_grsubj c      \n";
                }                
                sql += " where                                                                                                                          " +
                       "        a.isuse='Y'     \n";// and a.isapproval='Y' 
				sql += "   and  a.subj = b.subj \n" +
					   "   and  b.userid = " + SQLString.Format(s_userid) + " \n" +
					   "   and  b.gadmin = " + SQLString.Format(s_gadmin) + " \n";
				if(v_isoutsourcing.equals("N")){
					sql += " and a.isoutsourcing ='N' ";
				}
                if ( !s_subjsearchkey.equals("") ) { 
                    sql +="  and (upper(a.subjnm) like " + StringManager.makeSQL("%" +s_subjsearchkey + "%") + " escape '/' \n";
                    sql +="       or upper(a.subj) like " + StringManager.makeSQL("%" +s_subjsearchkey + "%") + " escape '/') \n";
                }
                if ( justOn) sql += " and a.isonoff = 'ON' "; 
                if ( justOff) sql += " and a.isonoff = 'OFF' "; 
                
                if ( !ss_grcode.equals("ALL") ) { 
                    sql += " and c.grcode = " + SQLString.Format(ss_grcode) + " and a.subj = c.subjcourse";
                }
                
                
                if ( !ss_subjgubun.equals("ALL")) { 
                	sql += " and a.isonoff = " + SQLString.Format(ss_subjgubun);
                }
                
                if ( !ss_upperclass.equals("ALL") && ss_middleclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && !ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                    sql += " and a.lowerclass = " + SQLString.Format(ss_lowerclass);
                }
                
                sql += " order by subjnm";      
            }
            else if ( v_gadmin.equals("H") ) {     //  ��0�׷����
                sql  = "select distinct " +
                       "        a.subj, " +
                       "        '[' || a.subj || '] ' || a.subjnm as subjnm  ";
                sql += "  from " +
                       "        tz_subj a, ";
                sql += "        (" +
                       "        select " +
                       "                e.subjcourse as subj, " +
                       "                e.grcode as grcode " +
                       "          from " +
                       "                tz_grsubj e, " +
                       "                tz_grcodeman f " +
                       "         where " +
                       "                f.userid = " + SQLString.Format(s_userid) + " " +
                       "           and  f.gadmin = " + SQLString.Format(s_gadmin) + " ";
                sql += "           and  e.grcode = f.grcode" +
                       "        ) b,";
                sql += "        tz_subjseq c ";
                sql += " where " +
                       "        a.subj = b.subj " +
                       "   and  a.isuse='Y' ";// and a.isapproval='Y' ";
                sql += "   and  b.subj = c.subj";
                sql += "   and  b.grcode = c.grcode";
                if(v_isoutsourcing.equals("N")){
                	sql += " and a.isoutsourcing ='N' ";
                }
                
                if( !ss_grseq.equals("ALL") ){
                  sql += "   and  c.grseq = '" +ss_grseq + "'";
                }
                
                if ( !s_subjsearchkey.equals("") ) { 
                    sql +="  and (upper(a.subjnm) like " + StringManager.makeSQL("%" +s_subjsearchkey + "%") + " escape '/' \n";
                    sql +="       or upper(a.subj) like " + StringManager.makeSQL("%" +s_subjsearchkey + "%") + " escape '/') \n";
                }
                if ( justOn) sql += " and a.isonoff = 'ON' "; 
                if ( justOff) sql += " and a.isonoff = 'OFF' "; 
           
                if ( !ss_grcode.equals("ALL") ) { 
                    sql += " and b.grcode = " + SQLString.Format(ss_grcode);
                }
                
                if ( !ss_subjgubun.equals("ALL")) { 
                	sql += " and a.isonoff = " + SQLString.Format(ss_subjgubun);
                }
                
                if ( !ss_upperclass.equals("ALL") && ss_middleclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && !ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                    sql += " and a.lowerclass = " + SQLString.Format(ss_lowerclass);
                }
                sql += " order by subjnm";          
            }
            else if ( v_gadmin.equals("K") ) {     //  ȸ�����, �μ�����
                sql  = "\n select distinct a.subj, '[' || a.subj || '] ' || a.subjnm as subjnm  ";
                sql += "\n from   tz_subj a, tz_grsubj b ";
                sql += "\n      , (select e.grcode ";
                sql += "\n         from   tz_grcomp e, tz_compman f ";
                sql += "\n         where  f.userid = " + SQLString.Format(s_userid);
                sql += "\n         and    f.gadmin = " + SQLString.Format(s_gadmin);
                sql += "\n         and    e.comp = f.comp) c";
                sql += "\n where  a.subj = b.subjcourse ";
                sql += "\n and    b.grcode = c.grcode ";
                sql += "\n and    a.isuse='Y' ";
                if(v_isoutsourcing.equals("N")){
                	sql += " and a.isoutsourcing ='N' ";
                }// and a.isapproval='Y'";
                if ( !s_subjsearchkey.equals("") ) { 
                    sql +="\n and    (upper(a.subjnm) like " + StringManager.makeSQL("%" +s_subjsearchkey + "%") + " escape '/' \n";
                    sql +="\n         or upper(a.subj) like " + StringManager.makeSQL("%" +s_subjsearchkey + "%") + " escape '/') \n";
                }
                if ( justOn)  sql += "\n and    a.isonoff = 'ON' "; 
                if ( justOff) sql += "\n and    a.isonoff = 'OFF' "; 
                
                if ( !ss_grcode.equals("ALL") ) { 
                    sql += "\n and    b.grcode = " + SQLString.Format(ss_grcode);
                }
                
                if ( !ss_subjgubun.equals("ALL")) { 
                	sql += " and a.isonoff = " + SQLString.Format(ss_subjgubun);
                }
                
                if ( !ss_upperclass.equals("ALL") && ss_middleclass.equals("ALL") ) { 
                    sql += "\n and    a.upperclass = " + SQLString.Format(ss_upperclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && ss_lowerclass.equals("ALL") ) { 
                    sql += "\n and    a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += "\n and    a.middleclass = " + SQLString.Format(ss_middleclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && !ss_lowerclass.equals("ALL") ) { 
                    sql += "\n and    a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += "\n and    a.middleclass = " + SQLString.Format(ss_middleclass);
                    sql += "\n and    a.lowerclass = " + SQLString.Format(ss_lowerclass);
                }
                sql += "\n order  by subjnm";          
            }
            else if ( v_gadmin.equals("M") ) {      //CP
            	sql = "select distinct a.subj, '[' || a.subj || '] ' || a.subjnm as subjnm  from tz_subj a";
            	
            	if ( !ss_grcode.equals("ALL") && (ss_grseq.equals("ALL") || ss_grseq.equals("----"))) {       //      ��0�׷츸 ���õ� ���
            		sql += " , tz_grsubj b where a.isuse='Y' ";// and a.isapproval='Y' ";
            		sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.subj = b.subjcourse";
            	}
            	else if ( !ss_grcode.equals("ALL") && !(ss_grseq.equals("ALL")||ss_grseq.equals("----")) ) {      //      ��0�׷�� ��0��� ���õ� ���
            		sql += " , tz_grsubj b, tz_subjseq c where a.isuse='Y' ";// and a.isapproval='Y' ";
            		sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.subj = b.subjcourse";
            		sql += " and b.grcode = c.grcode ";
            		sql += " and c.grseq = " + SQLString.Format(ss_grseq) + " and a.subj = c.subj";
            		sql += " and c.course = '000000'";
            		sql += " and c.gyear = " + SQLString.Format(ss_gyear);
            	}
            	else { 
            		sql += " where a.isuse='Y' ";// and a.isapproval='Y'";
            	}
            	
            	if(v_isoutsourcing.equals("N")){
            		sql += " and a.isoutsourcing ='N' ";
            	}
            	
                if ( !ss_subjgubun.equals("ALL")) { 
                	sql += " and a.isonoff = " + SQLString.Format(ss_subjgubun);
                }
                
            	if ( !ss_upperclass.equals("ALL") && ss_middleclass.equals("ALL") ) { 
            		sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
            	}
            	else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && ss_lowerclass.equals("ALL") ) { 
            		sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
            		sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
            	}
            	else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && !ss_lowerclass.equals("ALL") ) { 
            		sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
            		sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
            		sql += " and a.lowerclass = " + SQLString.Format(ss_lowerclass);
            	}
            	
            	if ( !s_subjsearchkey.equals("") ) { 
            		sql +="  and (upper(a.subjnm) like " + StringManager.makeSQL("%" +s_subjsearchkey + "%") + " escape '/' \n";
            		sql +="       or upper(a.subj) like " + StringManager.makeSQL("%" +s_subjsearchkey + "%") + " escape '/') \n";
            	}
            	
            	sql += " and a.cp in (select cpseq from tz_cpinfo where userid="+StringManager.makeSQL(box.getSession("userid"))+") ";
            	
            	if ( justOn) sql += " and a.isonoff = 'ON' "; 
            	if ( justOff) sql += " and a.isonoff = 'OFF' "; 
            	
            	//sql += " order by subjnm";
            	
            	
            	ConfigSet conf = new ConfigSet();
            	
            	/* cp�� ��� �ڽ� ������� ��=
            	if ( conf.getBoolean("course.use") ) {      //     �ڽ��з� �ִ�
            		sql += " union";
            		sql += " select distinct a.course, '[' || a.course || '] ' || a.coursenm from tz_course a, tz_grsubj b, tz_courseseq c where 1=1";            
            		if ( !ss_upperclass.equals("ALL") && ss_middleclass.equals("ALL") ) { 
            			sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
            		}
            		else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && ss_lowerclass.equals("ALL") ) { 
            			sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
            			sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
            		}
            		else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && !ss_lowerclass.equals("ALL") ) { 
            			sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
            			sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
            			sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
            		}
            		if ( !ss_grcode.equals("ALL") && ss_grseq.equals("ALL") ) {       //      ��0�׷츸 ���õ� ���
            			sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.course = b.subjcourse";
            		}
            		if ( !ss_grcode.equals("ALL") && !ss_grseq.equals("ALL") ) {      //      ��0�׷�� ��0��� ���õ� ���
            			sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.course = b.subjcourse";
            			sql += " and c.grseq = " + SQLString.Format(ss_grseq) + " and a.course = c.course";
            		}
            	}
            	*/
            }
            else {      //  Ultravisor, Supervisor
                sql = "select distinct a.subj, '[' || a.subj || '] ' || a.subjnm as subjnm  from tz_subj a";
                
                if ( !ss_grcode.equals("ALL") && (ss_grseq.equals("ALL") || ss_grseq.equals("----"))) {       //      ��0�׷츸 ���õ� ���
                    sql += " , tz_grsubj b where a.isuse='Y' ";// and a.isapproval='Y' ";
                    sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.subj = b.subjcourse";
	            }
	            else if ( !ss_grcode.equals("ALL") && !(ss_grseq.equals("ALL")||ss_grseq.equals("----")) ) {      //      ��0�׷�� ��0��� ���õ� ���
	                sql += " , tz_grsubj b, tz_subjseq c where a.isuse='Y' ";// and a.isapproval='Y' ";
	                sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.subj = b.subjcourse";
	                sql += " and b.grcode = c.grcode ";
	                sql += " and c.grseq = " + SQLString.Format(ss_grseq) + " and a.subj = c.subj";
	                sql += " and c.course = '000000'";
	                sql += " and c.gyear = " + SQLString.Format(ss_gyear);
	            }
                else { 
                    sql += " where a.isuse='Y' ";// and a.isapproval='Y'";
                }
                
                if(v_isoutsourcing.equals("N")){
                	sql += " and a.isoutsourcing ='N' ";
                }

                if ( !ss_subjgubun.equals("ALL")) { 
                	sql += " and a.isonoff = " + SQLString.Format(ss_subjgubun);
                }
                
                if ( !ss_upperclass.equals("ALL") && ss_middleclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && !ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                    sql += " and a.lowerclass = " + SQLString.Format(ss_lowerclass);
                }
                
                if ( !s_subjsearchkey.equals("") ) { 
                    sql +="  and (upper(a.subjnm) like " + StringManager.makeSQL("%" +s_subjsearchkey + "%") + " escape '/' \n";
                    sql +="       or upper(a.subj) like " + StringManager.makeSQL("%" +s_subjsearchkey + "%") + " escape '/') \n";
                }
                if ( justOn) sql += " and a.isonoff = 'ON' "; 
                if ( justOff) sql += " and a.isonoff = 'OFF' "; 
                
                //sql += " order by subjnm";
                
                
                ConfigSet conf = new ConfigSet();
                
                if ( conf.getBoolean("course.use") ) {      //     �ڽ��з� �ִ�
                    sql += " union";
                    sql += " select distinct a.course, '[' || a.course || '] ' || a.coursenm from tz_course a, tz_grsubj b, tz_courseseq c where 1=1";            
                    if ( !ss_upperclass.equals("ALL") && ss_middleclass.equals("ALL") ) { 
                        sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    }
                    else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && ss_lowerclass.equals("ALL") ) { 
                        sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                        sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                    }
                    else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && !ss_lowerclass.equals("ALL") ) { 
                        sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                        sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                        sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                    }
                    if ( !ss_grcode.equals("ALL") && ss_grseq.equals("ALL") ) {       //      ��0�׷츸 ���õ� ���
                        sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.course = b.subjcourse";
                    }
                    if ( !ss_grcode.equals("ALL") && !ss_grseq.equals("ALL") ) {      //      ��0�׷�� ��0��� ���õ� ���
                        sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.course = b.subjcourse";
                        sql += " and c.grseq = " + SQLString.Format(ss_grseq) + " and a.course = c.course";
                    }
                }
            }
            
            //System.out.println("\n ��� ���û��� �� ���� \n " + sql + "\n ��� ���û��� �� �� \n " );

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);     
            
            if ( v_gadmin.equals("M") || v_gadmin.equals("A") || v_gadmin.equals("H") || v_gadmin.equals("K") || v_gadmin.equals("U") || v_gadmin.equals("P")) {      //      Ultra/Super or ��0�׷���� or ȸ��/�μ�/�μ����� ��� 'ALL' ��� ��� 
                result += getSelectTag( ls, isChange, isALL, "s_subjcourse", ss_subjcourse);
            }
            else { 
                result += getSelectTag( ls, isChange, false, "s_subjcourse", ss_subjcourse);
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

    public static String getSubj(RequestBox box, boolean isChange, boolean isALL, boolean justOn, boolean justOff, String subjGu) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        //String result = "��� ";
        String result = "";
        
        try {           
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_upperclass = box.getStringDefault("s_upperclass","ALL");
            String  ss_middleclass = box.getStringDefault("s_middleclass","ALL");
            String  ss_lowerclass = box.getStringDefault("s_lowerclass","ALL");
            
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            String  ss_grseq   = box.getStringDefault("s_grseq","ALL");    	 // ��0���
            
            String  ss_subjcourse   = box.getStringDefault("s_subjcourse","ALL");    // ��d�ڵ�
            String  s_subjsearchkey = box.getString("s_subjsearchkey");
            s_subjsearchkey = s_subjsearchkey.toLowerCase();
            s_subjsearchkey = StringManager.replace(s_subjsearchkey, " ", "");
            
            String isOffSubj = box.getStringDefault("isOffSubj", "N");
            if("Y".equals(isOffSubj)) {
            	justOff = true;
            }
            
            connMgr = new DBConnectionManager();            
            
            if ( v_gadmin.equals("F") || v_gadmin.equals("P") ) {      //  ������, ����
                sql  = "select distinct         \n" +
                       "        a.subj,         \n" +
                       "        a.subjnm        \n" +
                       "  from                  \n" +
                       "        tz_subj a,      \n" +
                       "        tz_subjman b    \n";

                if ( !ss_grcode.equals("ALL") ) { 
                    sql += " , tz_grsubj c      \n";
                }                
                sql += " where                                                                                                                          " +
                       "        a.isuse='Y'     \n";// and a.isapproval='Y' 
				sql += "   and  a.subj = b.subj \n" +
					   "   and  b.userid = " + SQLString.Format(s_userid) + " \n" +
					   "   and  b.gadmin = " + SQLString.Format(s_gadmin) + " \n";
                if ( !s_subjsearchkey.equals("") ) { 
                    sql +="  and lower(a.subjnm) like " + StringManager.makeSQL("%" +s_subjsearchkey + "%") + " \n";
                }
                if ( justOn) sql += " and a.isonoff = 'ON' "; 
                if ( justOff) sql += " and a.isonoff = 'OFF' "; 
                
                if ( !ss_grcode.equals("ALL") ) { 
                    sql += " and c.grcode = " + SQLString.Format(ss_grcode) + " and a.subj = c.subjcourse";
                }
                
                if ( !ss_upperclass.equals("ALL") && ss_middleclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && !ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                    sql += " and a.lowerclass = " + SQLString.Format(ss_lowerclass);
                }
                
                if ( !"".equals(subjGu) ) {
                	sql += " and a.subj_gu = " + SQLString.Format(subjGu);
                }
                sql += " order by a.subjnm";      
            }
            else if ( v_gadmin.equals("H") ) {     //  ��0�׷����
                sql  = "select distinct " +
                       "        a.subj, " +
                       "        a.subjnm";
                sql += "  from " +
                       "        tz_subj a, ";
                sql += "        (" +
                       "        select " +
                       "                e.subjcourse as subj, " +
                       "                e.grcode as grcode " +
                       "          from " +
                       "                tz_grsubj e, " +
                       "                tz_grcodeman f " +
                       "         where " +
                       "                f.userid = " + SQLString.Format(s_userid) + " " +
                       "           and  f.gadmin = " + SQLString.Format(s_gadmin) + " ";
                sql += "           and  e.grcode = f.grcode" +
                       "        ) b,";
                sql += "        tz_subjseq c ";
                sql += " where " +
                       "        a.subj = b.subj " +
                       "   and  a.isuse='Y' ";// and a.isapproval='Y' ";
                sql += "   and  b.subj = c.subj";
                sql += "   and  b.grcode = c.grcode";
                sql += "   and  c.grseq = '" +ss_grseq + "'";
                
                if ( !s_subjsearchkey.equals("") ) { 
                    sql +="  and lower(a.subjnm) like  '%" + StringManager.makeSQL("%" +s_subjsearchkey + "%") + "%'";
                }
                if ( justOn) sql += " and a.isonoff = 'ON' "; 
                if ( justOff) sql += " and a.isonoff = 'OFF' "; 
           
                if ( !ss_grcode.equals("ALL") ) { 
                    sql += " and b.grcode = " + SQLString.Format(ss_grcode);
                }
                
                if ( !ss_upperclass.equals("ALL") && ss_middleclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && !ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                    sql += " and a.lowerclass = " + SQLString.Format(ss_lowerclass);
                }
                if ( !"".equals(subjGu) ) {
                	sql += " and a.subj_gu = " + SQLString.Format(subjGu);
                }
                
                sql += " order by a.subjnm";          
            }
/*            else if ( v_gadmin.equals("K") ) {     //  ȸ�����, �μ�����
                sql = "select distinct a.subj, a.subjnm";
                sql += " from tz_subj a, tz_grsubj b,";
                sql += " (select e.grcode from tz_grcomp e, tz_compman f where f.userid = " + SQLString.Format(s_userid) + " and f.gadmin = " + SQLString.Format(s_gadmin);
                sql += " and substr(e.comp, 1, 4) = substr(f.comp, 1, 4)) c";
                sql += " where a.subj = b.subjcourse and b.grcode = c.grcode and a.isuse='Y' ";// and a.isapproval='Y'";
                if ( !s_subjsearchkey.equals("") ) { 
                    sql +="  and lower(a.subjnm) like  '%" + StringManager.makeSQL("%" +s_subjsearchkey + "%") + "%'";
                }
                if ( justOn) sql += " and a.isonoff = 'ON' "; 
                if ( justOff) sql += " and a.isonoff = 'OFF' "; 
                
                if ( !ss_grcode.equals("ALL") ) { 
                    sql += " and b.grcode = " + SQLString.Format(ss_grcode);
                }
                
                if ( !ss_upperclass.equals("ALL") && ss_middleclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && !ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                    sql += " and a.lowerclass = " + SQLString.Format(ss_lowerclass);
                }
                sql += " order by a.subjnm";          
            }
*/               
            else {      //  Ultravisor, Supervisor
                sql = "select distinct a.subj, a.subjnm from tz_subj a";
                
                if ( !ss_grcode.equals("ALL") && ss_grseq.equals("ALL") ) {       //      ��0�׷츸 ���õ� ���
                        sql += " , tz_grsubj b where a.isuse='Y' ";// and a.isapproval='Y' ";
                        sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.subj = b.subjcourse";
                }
                else if ( !ss_grcode.equals("ALL") && !ss_grseq.equals("ALL") ) {      //      ��0�׷�� ��0��� ���õ� ���
                    sql += " , tz_grsubj b, tz_subjseq c where a.isuse='Y' ";// and a.isapproval='Y' ";
                    sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.subj = b.subjcourse";
                    sql += " and b.grcode = c.grcode ";
                    sql += " and c.grseq = " + SQLString.Format(ss_grseq) + " and a.subj = c.subj";
                    sql += " and c.course = '000000'";
                }
                else { 
                    sql += " where a.isuse='Y' ";// and a.isapproval='Y'";
                }
                
                if ( !ss_upperclass.equals("ALL") && ss_middleclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                }
                else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && !ss_lowerclass.equals("ALL") ) { 
                    sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                    sql += " and a.lowerclass = " + SQLString.Format(ss_lowerclass);
                }
                
                if ( !s_subjsearchkey.equals("") ) { 
                    sql +="  and lower(a.subjnm) like " + StringManager.makeSQL("%" +s_subjsearchkey + "%") + " \n";
                }
                if ( justOn) sql += " and a.isonoff = 'ON' "; 
                if ( justOff) sql += " and a.isonoff = 'OFF' "; 

                if ( !"".equals(subjGu) ) {
                	sql += " and a.subj_gu = " + SQLString.Format(subjGu);
                }

                sql += " order by a.subjnm";

                ConfigSet conf = new ConfigSet();
                
                if ( conf.getBoolean("course.use") ) {      //     �ڽ��з� �ִ�
                    sql += " union";
                    sql += " select distinct a.course, a.coursenm from tz_course a, tz_grsubj b, tz_courseseq c where 1=1";            
                    if ( !ss_upperclass.equals("ALL") && ss_middleclass.equals("ALL") ) { 
                        sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                    }
                    else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && ss_lowerclass.equals("ALL") ) { 
                        sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                        sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                    }
                    else if ( !ss_upperclass.equals("ALL") && !ss_middleclass.equals("ALL") && !ss_lowerclass.equals("ALL") ) { 
                        sql += " and a.upperclass = " + SQLString.Format(ss_upperclass);
                        sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                        sql += " and a.middleclass = " + SQLString.Format(ss_middleclass);
                    }
                    if ( !ss_grcode.equals("ALL") && ss_grseq.equals("ALL") ) {       //      ��0�׷츸 ���õ� ���
                        sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.course = b.subjcourse";
                    }
                    if ( !ss_grcode.equals("ALL") && !ss_grseq.equals("ALL") ) {      //      ��0�׷�� ��0��� ���õ� ���
                        sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.course = b.subjcourse";
                        sql += " and c.grseq = " + SQLString.Format(ss_grseq) + " and a.course = c.course";
                    }
                }
            }
            
            //System.out.println("\n ��� ���û��� �� ���� \n " + sql + "\n ��� ���û��� �� �� \n " );

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);     
            
            if ( v_gadmin.equals("A") || v_gadmin.equals("H") || v_gadmin.equals("K") || v_gadmin.equals("U") ) {      //      Ultra/Super or ��0�׷���� or ȸ��/�μ�/�μ����� ��� 'ALL' ��� ��� 
                result += getSelectTag( ls, isChange, isALL, "s_subjcourse", ss_subjcourse);
            }
            else { 
                result += getSelectTag( ls, isChange, false, "s_subjcourse", ss_subjcourse);
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
    
    public static String getSubjseq(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt           = null;        
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println; Mehtod���� ����ϴ� �� ǥ���ϴ� ���� 
        
        //String              result          = "��� ";
        String              result          = "";
        
        String              ss_grcode       = box.getStringDefault("s_grcode"       , "ALL");   // ��0�׷�
        String              ss_gyear        = box.getStringDefault("s_gyear"        , "ALL");   // ��0�⵵
        String              ss_grseq        = box.getStringDefault("s_grseq"        , "ALL");   // ��0���            
        String              ss_subjcourse   = box.getStringDefault("s_subjcourse"   , "ALL");   // ���
        String              ss_subjseq      = box.getStringDefault("s_subjseq"      , "ALL");   // �����
        
		String s_userid = box.getSession("userid");
        String s_gadmin = box.getSession("gadmin");
        String v_gadmin = StringManager.substring(s_gadmin, 0, 1);

        try {
            connMgr = new DBConnectionManager();
            
            ss_gyear        = box.getStringDefault("s_gyear", getGyear(connMgr, ss_grcode));
            
            sbSQL.append(" select  distinct                                                                                         \n")                                       
                 .append("         decode(b.course, '000000', a.subjseq, b.courseseq) subjseq                                       \n")
                 .append("     ,   a.subjseq                                                                                        \n")
                 .append(" from    tz_subjseq      a                                                                                \n")
                 .append("     ,   tz_courseseq    b                                                                                \n");

//            if (v_gadmin.equals("P")) {
//                sbSQL.append("      ,  tz_classtutor c  \n");
//            }

            sbSQL.append(" where   decode(b.course, '000000', a.subj   , b.course) = " + SQLString.Format(ss_subjcourse) + "        \n")
                 .append(" and     decode(b.course, '000000', a.gyear  , b.gyear ) = " + SQLString.Format(ss_gyear     ) + "        \n");

//            if (v_gadmin.equals("P")) {
//                sbSQL.append(" and     a.subj = c.subj \n");
//                sbSQL.append(" and     a.year = c.year \n");
//                sbSQL.append(" and     a.subjseq = c.subjseq \n");
//                sbSQL.append(" and     c.tuserid = " + StringManager.makeSQL(s_userid) + " \n");
//            }

            if ( !ss_grcode.equals("ALL") && ss_grseq.equals("ALL") && !ss_subjcourse.equals("ALL") ) {         // ��0�׷츸 ���õ� ���
                sbSQL.append(" and decode(b.course, '000000', a.grcode, b.grcode)  = " + SQLString.Format(ss_grcode    ) + "        \n");
            }
            else if ( !ss_grcode.equals("ALL") && !ss_grseq.equals("ALL") && !ss_subjcourse.equals("ALL") ) {   // ��0�׷�� ��0��� ���õ� ���
                sbSQL.append(" and decode(b.course, '000000', a.grcode, b.grcode)  = " + SQLString.Format(ss_grcode    ) + "        \n")
                     .append(" and decode(b.course, '000000', a.grseq , b.grseq )  = " + SQLString.Format(ss_grseq     ) + "        \n");
            }
            
            sbSQL.append(" order by decode(b.course, '000000', a.subjseq, b.courseseq)                                              \n");
            
            //System.out.println("com.ziaan.common.SelectSubjBean" + "." + "getSubjseq() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
//System.out.println("-----��� : " + sbSQL.toString());            
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
    
    public static String getSubjClass(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String              sql     = "";
        //String result = "Ŭ���� ";
        String result = "";

        try { 
            String  ss_gyear = box.getString("s_gyear");     						//  ��0�⵵
            String  ss_subjcourse = box.getStringDefault("s_subjcourse","ALL");    	//  ��d
            String  ss_subjseq = box.getStringDefault("s_subjseq","ALL");    		//  ��d���
            String  ss_class = box.getStringDefault("s_class","ALL");    			//  Ŭ����

            connMgr = new DBConnectionManager();

            sql  = " select class , classnm ";
            sql += " from   tz_class ";
            sql += " where  subj    = " + SQLString.Format(ss_subjcourse);
            sql += " and    year    = " + SQLString.Format(ss_gyear);
            sql += " and    subjseq = " + SQLString.Format(ss_subjseq);
            sql += " order  by classnm  " ;

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
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }    
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return result;
    }
    
    
    public static String getGrcompany(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        
        try { 
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            String ss_company = box.getStringDefault("s_company","1001");    //ȸ�� ����Ʈ = ��ݱ�0����
            
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
                result += getSelectTag( ls, isChange, isALL, "s_company", ss_company);
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
    
    public static String getSelectTag(ListSet ls, boolean isChange, boolean isALL, String selname, String optionselected) throws Exception { 
        StringBuffer sb = null;
        // System.out.println("isChange" + isChange);System.out.println("isALL" + isALL);System.out.println("selname" + selname);System.out.println("optionselected" + optionselected);
        try { 
            sb = new StringBuffer();  

            sb.append("<select id = \"" + selname + "\" name = \"" + selname + "\" class=\"subjectcss\"    ");
            if ( isChange) sb.append(" onChange = \"whenSelection('change', event)\"");  
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
                else if ( selname.equals("s_blcourseseq")) sb.append(" > " + StringManager.cutZero( ls.getString(columnCount)) + "��</option > \r\n");
                else if ( selname.equals("s_pfcourseseq")) sb.append(" > " + StringManager.cutZero( ls.getString(columnCount)) + "��</option > \r\n");
                else sb.append(" > " + ls.getString(columnCount) + "</option > \r\n");
            }
            sb.append("</select >");
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage() );
        }
        return sb.toString();
    }
    
    public static String getSelectTag_go(ListSet ls, boolean isChange, boolean isALL, String selname, String optionselected,String upperclasscnt) throws Exception { 
        StringBuffer sb = null;
        // System.out.println("isChange" + isChange);System.out.println("isALL" + isALL);System.out.println("selname" + selname);System.out.println("optionselected" + optionselected);
        try { 
            sb = new StringBuffer();  

            sb.append("<select id = \"" + selname + "\" name = \"" + selname + "\" class=\"subjectcss\"    ");
            
            if ( isChange) sb.append(" onChange = \"whenSelection('change', event,'"+upperclasscnt+"')\"");  
            sb.append(" >");
            if ( isALL) { 
                sb.append("<option value = \"ALL\" > ALL</option >");  
            }
            else if ( isChange) { 
                if ( selname.indexOf("year") == -1) 
                    sb.append("<option value = \"----\" >== ���� == </option >");  
            }
            while ( ls.next() ) {    
                ResultSetMetaData meta = ls.getMetaData();
                int columnCount = meta.getColumnCount();
                
                sb.append("<option value = \"" + ls.getString(1) + "\"");

                if ( optionselected.equals( ls.getString(1))) sb.append(" selected");
               
                if ( selname.equals("s_subjseq")) sb.append(" > " + StringManager.cutZero( ls.getString(columnCount)) + "��</option >");
                else if ( selname.equals("s_blcourseseq")) sb.append(" > " + StringManager.cutZero( ls.getString(columnCount)) + "��</option >");
                else if ( selname.equals("s_pfcourseseq")) sb.append(" > " + StringManager.cutZero( ls.getString(columnCount)) + "��</option >");
                else sb.append(" > " + ls.getString(columnCount) + "</option >");
            }
            sb.append("</select >");
           // System.out.println(sb.toString());
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage() );
        }
        return sb.toString();
    }
    
    
    
    
    // �����Ͱ��� ��' ��� ���ν� �ش�Ǵ� ��� select
    public static String getMasterSeq(RequestBox box, String subj, String grcode, String gyear, String grseq, String mastercd) throws Exception { 

    	StringBuffer sb = null;
    	DBConnectionManager connMgr     = null;
    	PreparedStatement   pstmt   = null;
    	ResultSet rs = null;
        ListSet             ls      = null;
        String  sql  = "";
        String  resString = "";
        String  v_subjseq  = "";
        String  v_subjseqgr  = "";
        int i = 0;
        
    	try { 
    		sb = new StringBuffer();
    		connMgr = new DBConnectionManager();
    		
    		sql  = "select a.subjseq,a.subjseqgr from tz_subjseq a ";
    		sql += "where ";
    		sql += " a.subj = " +SQLString.Format(subj);
    		sql += " and a.grcode = " +SQLString.Format(grcode);
    		sql += " and a.gyear= " +SQLString.Format(gyear);
    		sql += " and a.grseq= " +SQLString.Format(grseq);
    		sql += " and a.subjseq not in( ";
    		sql += " select subjseq from tz_mastersubj ";
    		sql += " where ";
    		sql += " 1=1 ";
    		// sql += " and mastercd=" +SQLString.Format(mastercd);
    		sql += " and a.year  = year ";
    		sql += " and subjcourse = " +SQLString.Format(subj);
    		sql += " )";
    		sql += " order by subjseqgr";
    		
            pstmt = connMgr.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if ( pstmt != null ) { pstmt.close(); }
    		
    		i = 0;
    		
    		while ( rs.next() ) { 
    		  v_subjseq = rs.getString("subjseq");
    		  v_subjseqgr = rs.getString("subjseqgr");
    		  sb.append("document.form2.p_subjectseq.options[" +i + "] = new Option(\"" +Integer.parseInt(v_subjseqgr) + "��\",\"" +v_subjseq + "\");");
    		  i++;
    	    }                                                                                           
    	}

        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage() );
        }
        finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return sb.toString();        
    	
    }
    
    public static boolean getIsVisible(boolean isStatisticalPage, int classNum) throws Exception { 
        boolean result = false;
        int classview = 0;
        int classdepth = 0;
        
        try { 
            ConfigSet conf = new ConfigSet();
            
            classview = conf.getInt("subj.class.view");
            classdepth = conf.getInt("subj.class.depth");
            
            if ( isStatisticalPage) {     //      ��������� 
                if ( classNum == 1) {     //      UpperClass
                    if ( classview == 0) result = false;
                    else if ( classview == 1) result = true;
                    else if ( classview == 2) result = true;
                    else if ( classview == 3) result = true;
                }
                else if ( classNum == 2) {     //      MiddleClass
                    if ( classview == 0) result = false;
                    else if ( classview == 1) result = false;
                    else if ( classview == 2) result = true;
                    else if ( classview == 3) result = true;
                }
                else if ( classNum == 3) {     //      LowerClass
                    if ( classview == 0) result = false;
                    else if ( classview == 1) result = false;
                    else if ( classview == 2) result = false;
                    else if ( classview == 3) result = true;
                }
            }
            else { 
                if ( classNum == 1) {     //      UpperClass
                    if ( classdepth > 0) result = true;
                }
                else if ( classNum == 2) {     //      MiddleClass
                    if ( classdepth > 1) result = true;
                }
                else if ( classNum == 3) {     //      LowerClass
                    if ( classdepth > 2) result = true;
                }
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage() );
        }
        return result;
    }  
    
    public static String getBLCourse(RequestBox box, boolean isChange, boolean isALL, boolean justOn, boolean justOff, boolean isDisplayNm) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        if(isDisplayNm)
            result += "B/L ��d ";
        
        try {           
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            
            String  ss_blcourse   = box.getStringDefault("s_blcourse","ALL");    // ��0���
            String  ss_blcourseyear   = box.getStringDefault("s_blcourseyear","ALL");    // B/L ��d ����
            
            String  s_subjsearchkey = box.getString("s_subjsearchkey");
            s_subjsearchkey = s_subjsearchkey.toLowerCase();
            s_subjsearchkey = StringManager.replace(s_subjsearchkey, " ", "");
            
            connMgr = new DBConnectionManager();            
            
            //  Ultravisor, Supervisor
            sql = "select distinct a.coursecode, a.coursenm from tz_bl_course a";
            
            //if ( !ss_grcode.equals("ALL") && ss_grcode.equals("----") ) {       //      ��0�׷츸 ���õ� ���
            if( !ss_blcourseyear.equals("ALL")) {
                    sql += " , tz_grblcourse b, tz_blcourseseq c where a.isuse='Y' ";  
                    sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.coursecode = b.subjcourse";
                    sql += " and a.coursecode = c.coursecode";
                    sql += " and c.courseyear = " + SQLString.Format(ss_blcourseyear);
            }
            else { 
                sql += " where a.isuse='Y' ";
            }
            
            if ( !s_subjsearchkey.equals("") ) { 
                sql +="  and lower(a.coursenm) like  '%" + StringManager.makeSQL("%" +s_subjsearchkey + "%") + "%'";
            }
            
            sql += " order by a.coursenm";
            
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);     
            
            result += getSelectTag( ls, isChange, isALL, "s_blcourse", ss_blcourse);
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
    
    public static String getBLCoursenm(RequestBox box, boolean isChange, boolean isALL, boolean justOn, boolean justOff, boolean isDisplayNm) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        if(isDisplayNm)
            result += "B/L ��d ";
        
        try {           
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            
            String  ss_blcourse   = box.getStringDefault("s_blcourse","ALL");    // ��0���
            String  ss_blcourseyear   = box.getStringDefault("s_blcourseyear","ALL");    // B/L ��d ����
            
            String  s_subjsearchkey = box.getString("s_subjsearchkey");
            s_subjsearchkey = s_subjsearchkey.toLowerCase();
            s_subjsearchkey = StringManager.replace(s_subjsearchkey, " ", "");
            
            connMgr = new DBConnectionManager();            
            
            //  Ultravisor, Supervisor
            sql = "select distinct a.coursecode, a.coursenm from tz_bl_course a";
            
            //if ( !ss_grcode.equals("ALL") && ss_grcode.equals("----") ) {       //      ��0�׷츸 ���õ� ���
            if( !ss_blcourseyear.equals("ALL")) {
                    sql += " , tz_grblcourse b where a.isuse='Y' ";  
                    sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.coursecode = b.subjcourse";
                    //sql += " and a.coursecode = c.coursecode";
                    //sql += " and c.courseyear = " + SQLString.Format(ss_blcourseyear);
            }
            else { 
                sql += " where a.isuse='Y' ";
            }
            
            if ( !s_subjsearchkey.equals("") ) { 
                sql +="  and lower(a.coursenm) like  '%" + StringManager.makeSQL("%" +s_subjsearchkey + "%") + "%'";
            }
            
            sql += " order by a.coursenm";
            
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);     
            
            result += getSelectTag( ls, isChange, isALL, "s_blcourse", ss_blcourse);
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
    
    public static String getBLCourseyear(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt           = null;        
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println; Mehtod���� ����ϴ� �� ǥ���ϴ� ���� 
        
        String              result          = "���� ";
        
        String              ss_grcode       = box.getStringDefault("s_grcode"       , "ALL");   // ��0�׷�
        String              ss_blcourse   = box.getStringDefault("s_blcourse"   , "ALL");   // B/L ��d
        String              ss_blcourseseq      = box.getStringDefault("s_blcourseseq"      , "ALL");   // ��d ���
        String              ss_blcourseyear      = box.getStringDefault("s_blcourseyear"      , "ALL");   // ��d �⵵
        
        try {
            connMgr = new DBConnectionManager();
            
            sbSQL.append(" select  distinct courseyear                                                                          \n")
                 .append(" from    tz_blcourseseq                                                                               \n")
                 .append(" where   1 = 1                                                                                        \n");
                 
            if ( !ss_grcode.equals("ALL")) {         // ��0�׷츸 ���õ� ���
                sbSQL.append(" and grcode  = " + SQLString.Format(ss_grcode    ) + "   \n");
            }
            
            sbSQL.append(" order by courseyear                                                                                      \n");
            pstmt   = connMgr.prepareStatement(sbSQL.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls      = new ListSet(pstmt);
            
            result += getSelectTag( ls, isChange, isALL, "s_blcourseyear", ss_blcourseyear);
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
    
    public static String getBLCourseseq(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt           = null;        
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        String              result          = "B/L ��� ";
        
        String              ss_grcode       = box.getStringDefault("s_grcode"       , "ALL");   // ��0�׷�
        String              ss_blcourseyear = "";                                               // ��d����
        String              ss_blcourse     = box.getStringDefault("s_blcourse"   , "ALL");     // ��d
        String              ss_blcourseseq      = box.getStringDefault("s_blcourseseq"      , "ALL");   // ��d���
        
        try {
            connMgr = new DBConnectionManager();
            
            ss_blcourseyear        = box.getStringDefault("s_blcourseyear" ,"ALL");
            
            sbSQL.append(" select  distinct courseseq                                       \n")
                 .append(" from    tz_blcourseseq                                           \n")
                 .append(" where   grcode  = " + SQLString.Format(ss_grcode    ) + "        \n")
                 .append(" and     courseyear = " + SQLString.Format(ss_blcourseyear) + "   \n")
                 .append(" and   coursecode = " + SQLString.Format(ss_blcourse    ) + "     \n");
                 
            sbSQL.append(" order by courseseq                                                                                      \n");
            pstmt   = connMgr.prepareStatement(sbSQL.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls      = new ListSet(pstmt);
            
            result += getSelectTag( ls, isChange, isALL, "s_blcourseseq", ss_blcourseseq);
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
    
    public static String getBranch(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt           = null;        
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        String              result          = "��a ";
        String ss_branch = box.getString("s_branch");
        try {
            connMgr = new DBConnectionManager();
            
            sbSQL.append("select branchcode, branchnm                          \n")
                 .append("from tz_branch                                       \n")
                 .append("order by branchnm                                    \n");
                 
            pstmt   = connMgr.prepareStatement(sbSQL.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls      = new ListSet(pstmt);
            
            result += getSelectTag( ls, isChange, isALL, "s_branch", ss_branch);
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
    
    public static String getPfCourse(RequestBox box, boolean isChange, boolean isALL, boolean isDisplayNm) throws Exception { 
        return getPfCourse(box, isChange, isALL, false, false, isDisplayNm);
    }
    
    public static String getPFCoursenm(RequestBox box, boolean isChange, boolean isALL, boolean isDisplayNm) throws Exception { 
        return getPFCoursenm(box, isChange, isALL, false, false, isDisplayNm);
    }
    
    public static String getPfCourse(RequestBox box, boolean isChange, boolean isALL, boolean justOn, boolean justOff, boolean isDisplayNm) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        if(isDisplayNm)
            result += "�� �缺 ��d ";
        
        try {           
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");

            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�

            String  ss_pfcourse   = box.getStringDefault("s_pfcourse","ALL");    // ��0���
            String  ss_pfcourseyear   = box.getStringDefault("s_pfcourseyear","ALL");    // B/L ��d ����

            String  s_subjsearchkey = box.getString("s_subjsearchkey");
            s_subjsearchkey = s_subjsearchkey.toLowerCase();
            s_subjsearchkey = StringManager.replace(s_subjsearchkey, " ", "");

            connMgr = new DBConnectionManager();            

            //  Ultravisor, Supervisor
            sql = "select distinct a.coursecode, a.coursenm from tz_pfcourse a";

            //if ( !ss_grcode.equals("ALL") && ss_grcode.equals("----") ) {       //      ��0�׷츸 ���õ� ���
            if( !ss_pfcourseyear.equals("ALL")) {
                    sql += " , tz_grpfcourse b, tz_pfcourseseq c where a.isuse='Y' ";
                    sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.coursecode = b.subjcourse";
                    sql += " and a.coursecode = c.coursecode";
                    sql += " and c.courseyear = " + SQLString.Format(ss_pfcourseyear);
            }
            else { 
                sql += " where a.isuse='Y' ";
            }

            if ( !s_subjsearchkey.equals("") ) { 
                sql +="  and lower(a.coursenm) like  '%" + StringManager.makeSQL("%" +s_subjsearchkey + "%") + "%'";
            }

            sql += " order by a.coursenm";

            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);     

            result += getSelectTag( ls, isChange, isALL, "s_pfcourse", ss_pfcourse);
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
    
    public static String getPFCoursenm(RequestBox box, boolean isChange, boolean isALL, boolean justOn, boolean justOff, boolean isDisplayNm) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        if(isDisplayNm)
            result += "�� �缺 ��d";
        
        try {           
            String s_gadmin = box.getSession("gadmin");
            String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
            String s_userid = box.getSession("userid");
            
            String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // ��0�׷�
            
            String  ss_pfcourse   = box.getStringDefault("s_pfcourse","ALL");    // ��0���
            String  ss_pfcourseyear   = box.getStringDefault("s_pfcourseyear","ALL");    // B/L ��d ����
            
            String  s_subjsearchkey = box.getString("s_subjsearchkey");
            s_subjsearchkey = s_subjsearchkey.toLowerCase();
            s_subjsearchkey = StringManager.replace(s_subjsearchkey, " ", "");
            
            connMgr = new DBConnectionManager();            
            
            //  Ultravisor, Supervisor
            sql = "select distinct a.coursecode, a.coursenm from tz_pfcourse a";
            
            //if ( !ss_grcode.equals("ALL") && ss_grcode.equals("----") ) {       //      ��0�׷츸 ���õ� ���
            if( !ss_pfcourseyear.equals("ALL")) {
                    sql += " , tz_grpfcourse b where a.isuse='Y' ";
                    sql += " and b.grcode = " + SQLString.Format(ss_grcode) + " and a.coursecode = b.subjcourse";
                    //sql += " and a.coursecode = c.coursecode";
                    //sql += " and c.courseyear = " + SQLString.Format(ss_pfcourseyear);
            }
            else { 
                sql += " where a.isuse='Y' ";
            }
            
            if ( !s_subjsearchkey.equals("") ) { 
                sql +="  and lower(a.coursenm) like  '%" + StringManager.makeSQL("%" +s_subjsearchkey + "%") + "%'";
            }
            
            sql += " order by a.coursenm";
            
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);     
            
            result += getSelectTag( ls, isChange, isALL, "s_pfcourse", ss_pfcourse);
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

    
    public static String getPfCourseyear(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt           = null;        
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println; Mehtod���� ����ϴ� �� ǥ���ϴ� ���� 
        
        String              result          = "���� ";
        
        String              ss_grcode       = box.getStringDefault("s_grcode"       , "ALL");   // ��0�׷�
        String              ss_pfcourse   = box.getStringDefault("s_pfcourse"   , "ALL");   // B/L ��d
        String              ss_pfcourseseq      = box.getStringDefault("s_pfcouorseseq"      , "ALL");   // ��d ���
        String              ss_pfcourseyear      = box.getStringDefault("s_pfcourseyear"      , "ALL");   // ��d �⵵
        
        try {
            connMgr = new DBConnectionManager();
            
            sbSQL.append(" select  distinct courseyear                                                                          \n")
                 .append(" from    tz_pfcourseseq                                                                               \n")
                 .append(" where   1 = 1                                                                                        \n");
                 
            if ( !ss_grcode.equals("ALL")) {         // ��0�׷츸 ���õ� ���
                sbSQL.append(" and grcode  = " + SQLString.Format(ss_grcode    ) + "   \n");
            }
            
            sbSQL.append(" order by courseyear                                                                                      \n");
            pstmt   = connMgr.prepareStatement(sbSQL.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls      = new ListSet(pstmt);
            
            result += getSelectTag( ls, isChange, isALL, "s_pfcourseyear", ss_pfcourseyear);
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
    
    public static String getPfCourseseq(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
        DBConnectionManager connMgr         = null;
        PreparedStatement   pstmt           = null;        
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        String              result          = "�� �缺 ��� ";
        
        String              ss_grcode       = box.getStringDefault("s_grcode"       , "ALL");   // ��0�׷�
        String              ss_pfcourseyear = "";                                               // ��d����
        String              ss_pfcourse     = box.getStringDefault("s_pfcourse"   , "ALL");     // ��d
        String              ss_pfcourseseq      = box.getStringDefault("s_pfcourseseq"      , "ALL");   // ��d���
        
        try {
            connMgr = new DBConnectionManager();
            
            ss_pfcourseyear        = box.getStringDefault("s_gyear", getGyear(connMgr, ss_grcode));
            
            sbSQL.append(" select  distinct courseseq                                       \n")
                 .append(" from    tz_pfcourseseq                                           \n")
                 .append(" where   grcode  = " + SQLString.Format(ss_grcode    ) + "        \n")
                 .append(" and     courseyear = " + SQLString.Format(ss_pfcourseyear) + "   \n")
                 .append(" and   coursecode = " + SQLString.Format(ss_pfcourse    ) + "     \n");
                 
            sbSQL.append(" order by courseseq                                                                                      \n");
            pstmt   = connMgr.prepareStatement(sbSQL.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            ls      = new ListSet(pstmt);
            
            result += getSelectTag( ls, isChange, isALL, "s_pfcourseseq", ss_pfcourseseq);
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
    
    
    /**
     * ���� selectbox
     * @param box
     * @param isChange
     * @param isALL
     * @param isStatisticalPage
     * @return
     * @throws Exception
     */
    public static String getYear(RequestBox box, boolean isChange, boolean isALL, boolean isStatisticalPage) throws Exception { 
    	return getYear(box, isChange, isALL, isStatisticalPage, "s_gyear");
   }
   
   public static String getYear(RequestBox box, boolean isChange, boolean isALL, boolean isStatisticalPage, String paramName) throws Exception { 
       DBConnectionManager	connMgr	= null;
       PreparedStatement   pstmt   = null;        
       ListSet             ls      = null;
       String              sql     = "";
       String result = ""; //��з�
       boolean isVisible = false;
       String ss_grcode = box.getStringDefault("s_grcode", "N000001");
       try { 
           isVisible = getIsVisible(isStatisticalPage, 1);     //      UpperClass = 1;
           							
           if ( isVisible) {        
               String  ss_lowerclass   = box.getString(paramName.substring(0,1)+"_gyear");    // 1�ܰ� ���з�
               
               if((!isALL) && ss_lowerclass.equals("ALL")) {
            	   ss_lowerclass = FormatDate.getDate("yyyyMMdd").substring(0,4);
            	   box.remove(paramName.substring(0,1)+"_gyear");
            	   box.put(paramName.substring(0,1)+"_gyear", ss_lowerclass);
               }
   
               connMgr = new DBConnectionManager();

               sql  = " select distinct gyear ";
               sql += " from   tz_grseq       ";
               if(!ss_grcode.equals("ALL")){
                   sql += " where  grcode = " + SQLString.Format(ss_grcode);
               }
               sql += " union ";
               sql += " select " + StringManager.makeSQL(FormatDate.getDate("yyyy")) + " gyear ";
               sql += " from   dual ";
               sql += " order  by gyear desc  ";
               
             //  System.out.println(sql);
               
               pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
               
               ls = new ListSet(pstmt);     
               
               result += getSelectTag( ls, isChange, isALL, paramName, ss_lowerclass);
           }
           else { 
               result = "";
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
}
