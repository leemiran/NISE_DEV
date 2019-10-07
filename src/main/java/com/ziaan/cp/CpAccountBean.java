// **********************************************************
//  1. 제      목: 교육비정산
//  2. 프로그램명: CpAccountBean.java
//  3. 개      요: 게시판
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 2008.10.20
//  7. 수      정:
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

public class CpAccountBean { 

    public CpAccountBean() { 
        try { 
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }

        /**
         * 자료실 리스트화면 select
         * @param    box          receive from the form object and session
         * @return ArrayList  자료실 리스트
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
             String v_order     	= box.getString("p_order"); 
             String v_orderType     = box.getStringDefault("p_orderType", "asc");          
             

             try { 
                 connMgr = new DBConnectionManager();

                 list = new ArrayList();
               
                 sql = "\n select a.subj, a.subjnm, get_compnm(b.comp) compnm, get_cpnm(a.cp) cpnm, a.biyong, a.bookprice, a.cp_accrate, a.cp_account, a.cp_vat, b.stucnt, b.tuserid "
	                 + "\n ,(select price from tz_tutor c where c.userid=b.tuserid) tutorfee    "                	 
	                 + "\n from   tz_subj a   "
	                 + "\n      , (select x.subj, y.comp, count(y.userid) stucnt, tuserid "
	                 + "\n         from   tz_subjseq x, tz_student y, tz_classtutor z "
	                 + "\n         where  x.subj 	= y.subj(+) "
	                 + "\n         and    x.year 	= y.year(+) "
	                 + "\n         and    x.subjseq = y.subjseq(+) "
	                 + "\n         and    x.subj 	= z.subj(+) "
	                 + "\n         and    x.year 	= z.year(+) "
	                 + "\n         and    x.subjseq = z.subjseq(+) ";                 
                 if ( !v_year.equals("") ) {              //    년도 검색할때
                	 sql += "\n         and    substr(x.edustart,0,4) ='" + v_year + "'";
                 }
                 if ( !v_month.equals("") ) {        //    월 검색할때
                	 sql += "\n         and    substr(x.edustart,5,2) ='" + v_month + "'";
                 }   
                 sql +="\n         group by x.subj, y.comp, z.tuserid having count(y.userid) > 0) b "
	                 + "\n where  a.subj = b.subj "               
	                 + "\n and    a.isuse = 'Y' ";
                 
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
    
}