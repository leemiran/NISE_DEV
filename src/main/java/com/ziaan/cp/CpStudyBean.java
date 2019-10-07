// **********************************************************
//  1. ��      ��: cp ��������
//  2. ���α׷���: CpStudyBean.java
//  3. ��      ��: cp ��������
//  4. ȯ      ��: JDK 1.5
//  5. ��      ��: 1.0
//  6. ��      ��: 2009.11.18
//  7. ��      ��:
// **********************************************************
package com.ziaan.cp;

import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class CpStudyBean { 

    public CpStudyBean() { 
        try { 
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
       
    
    /**
     * ���� SELECT
     * @param box          receive from the form object and session
     * @return ArrayList   ���� ����Ʈ
     */
      public ArrayList getSubj(RequestBox box) throws Exception { 
          DBConnectionManager	connMgr	= null;   
          ListSet             ls      = null;
          ArrayList           list    = null;
          String              sql     = "";
          DataBox             dbox    = null;
          
          try { 
              
              String s_gadmin = box.getSession("gadmin");
              //String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
              String s_userid = box.getSession("userid");
                            
              connMgr = new DBConnectionManager();            

              list = new ArrayList();

              if ( s_gadmin.startsWith("M")) {      //  ���������, ����
            	  
                  sql = "\n select distinct a.subj, a.subjnm, a.cp, b.cpnm, b.userid, get_name(b.userid) as name, b.handphone "
                	  + "\n from   tz_subj a "
                	  + "\n      , tz_cpinfo b "
                	  + "\n where  a.cp = b.cpseq "
               	 	  + "\n and    a.cp in (select cpseq from tz_cpinfo where userid ='"+s_userid+"')"    
                	  + "\n order  by a.subjnm ";

              } else {
            	  
                  sql = "\n select distinct a.subj, a.subjnm, a.cp, b.cpnm, b.userid, get_name(b.userid) as name, b.handphone "
                	  + "\n from   tz_subj a "
                	  + "\n      , tz_cpinfo b"
                	  + "\n where  a.cp = b.cpseq "
                	  + "\n order  by a.subjnm ";
                  
              }
              
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
         * �ڷ�� ����Ʈȭ�� select
         * @param    box          receive from the form object and session
         * @return ArrayList  �ڷ�� ����Ʈ
         * @throws Exception
         */
         public ArrayList selectList(RequestBox box) throws Exception { 
             DBConnectionManager	connMgr	= null;
             ListSet             ls      = null;
             ArrayList           list    = null;
             String              sql     = "";
             DataBox             dbox    = null;

             String v_userid    	= box.getSession("userid");        
             String v_gadmin     	= box.getSession("gadmin"); 
             String v_year 			= box.getStringDefault("p_year",FormatDate.getDate("yyyy"));
             String v_month     	= box.getStringDefault("p_month",FormatDate.getDate("MM"));         
             String v_subj     		= box.getString("p_subj"); 
             String v_order     	= box.getString("p_order");                  
             String v_orderType     = box.getStringDefault("p_orderType", "asc");          
             

             try { 
                 connMgr = new DBConnectionManager();

                 list = new ArrayList();
               
                 sql = "\n select a.subj, a.subjnm, get_compnm(b.comp) compnm, get_cpnm(a.cp) cpnm,  a.biyong, b.stucnt"     	 
	                 + "\n from   tz_subj a   "
	                 + "\n      , (select x.subj, y.comp, count(y.userid) stucnt "
	                 + "\n         from   tz_subjseq x, tz_student y"
	                 + "\n         where  x.subj 	= y.subj(+) "
	                 + "\n         and    x.year 	= y.year(+) "
	                 + "\n         and    x.subjseq = y.subjseq(+) ";               
                 if ( !v_year.equals("") ) {              //    �⵵ �˻��Ҷ�
                 sql +="\n         and    substr(x.edustart,0,4) ='" + v_year + "'";
                 }
                 if ( !v_month.equals("") ) {        //    �� �˻��Ҷ�
                 sql +="\n         and    substr(x.edustart,5,2) ='" + v_month + "'";
                 }   
                 sql +="\n         group by x.subj, y.comp having count(y.userid) > 0) b "
	                 + "\n where  a.subj = b.subj "               
	                 + "\n and    a.isuse = 'Y' ";

                 if ( !v_subj.equals("") ) {        //    �� �˻��Ҷ�
                 sql +="\n and    a.subj ='" + v_subj + "'";
                 }                    
                 
                 if( v_gadmin.startsWith("A") ){
                	 String v_cpseq = box.getString("p_cpseq");
                	 if ( !v_cpseq.equals("") ) {
                		 sql += "\n and    a.cp = " + StringManager.makeSQL(v_cpseq);          
                	 }
                 } else if( v_gadmin.startsWith("M")){
                	 sql += "\n and    a.cp in (select cpseq from tz_cpinfo where userid ='"+v_userid+"')";          
                 }                              
                 
                 if(!v_order.equals("")){
                	 sql += "\n order  by "+ v_order + " " +v_orderType ;            	 
                 }else{
                	 sql += "\n order  by a.subj asc";
                 }

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
         
		public ArrayList selectCPList(RequestBox box) throws Exception {
			
            DBConnectionManager	connMgr	= null;
            ListSet             ls      = null;

            ArrayList           list    = null;
            String              sql     = "";

            try { 
                connMgr = new DBConnectionManager();
                list = new ArrayList();
                  
                sql =
                	"\n  SELECT CPSEQ, CPNM FROM tz_cpinfo  " +
                	"\n  ORDER BY CPSEQ  ";
                
                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    list.add(ls.getDataBox());
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
         * �ڷ�� ����Ʈȭ�� select
         * @param    box          receive from the form object and session
         * @return ArrayList  �ڷ�� ����Ʈ
         * @throws Exception
         */
         public ArrayList selectBookList(RequestBox box) throws Exception { 
             DBConnectionManager	connMgr	= null;
             ListSet             ls      = null;
             ArrayList           list    = null;
             String              sql     = "";
             DataBox             dbox    = null;

             String v_userid    	= box.getSession("userid");        
             String v_gadmin     	= box.getSession("gadmin"); 
             String v_year 			= box.getStringDefault("p_year",FormatDate.getDate("yyyy"));
             String v_month     	= box.getStringDefault("p_month",FormatDate.getDate("MM"));         
             String v_subj     		= box.getString("p_subj"); 
             String v_order     	= box.getString("p_order");                  
             String v_orderType     = box.getStringDefault("p_orderType", "asc");          
             

             try { 
                 connMgr = new DBConnectionManager();

                 list = new ArrayList();
               
                 sql = "\n select a.subj, a.subjnm, get_compnm(b.comp) compnm, get_cpnm(a.cp) cpnm,  a.biyong, b.stucnt"     	 
	                 + "\n from   tz_subj a   "
	                 + "\n      , (select x.subj, y.comp, count(y.userid) stucnt "
	                 + "\n         from   tz_subjseq x, tz_student y"
	                 + "\n         where  x.subj 	= y.subj(+) "
	                 + "\n         and    x.year 	= y.year(+) "
	                 + "\n         and    x.subjseq = y.subjseq(+) ";               
                 if ( !v_year.equals("") ) {              //    �⵵ �˻��Ҷ�
                 sql +="\n         and    substr(x.eduend,0,4) ='" + v_year + "'";
                 }
                 if ( !v_month.equals("") ) {        //    �� �˻��Ҷ�
                 sql +="\n         and    substr(x.eduend,5,2) ='" + v_month + "'";
                 }   
                 sql +="\n         group by x.subj, y.comp having count(y.userid) > 0) b "
	                 + "\n where  a.subj = b.subj "               
	                 + "\n and    a.isuse = 'Y' ";

                 if ( !v_subj.equals("") ) {        //    �� �˻��Ҷ�
                 sql +="\n and    a.subj ='" + v_subj + "'";
                 }                    
                 
                 if( v_gadmin.startsWith("A") ){
                	 String v_cpseq = box.getString("p_cpseq");
                	 if ( !v_cpseq.equals("") ) {
                		 sql += "\n and    a.cp = " + StringManager.makeSQL(v_cpseq);          
                	 }
                 } else if( v_gadmin.startsWith("M")){
                	 sql += "\n and    a.cp in (select cpseq from tz_cpinfo where userid ='"+v_userid+"')";          
                 }                              
                 
                 if(!v_order.equals("")){
                	 sql += "\n order  by "+ v_order + " " +v_orderType ;            	 
                 }else{
                	 sql += "\n order  by a.subj asc";
                 }

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
		
    
}