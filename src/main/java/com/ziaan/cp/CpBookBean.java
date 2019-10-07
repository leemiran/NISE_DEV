// **********************************************************
//  1. 제      목: cp 교재배송
//  2. 프로그램명: CpBookBean.java
//  3. 개      요: cp 교재배송
//  4. 환      경: JDK 1.5
//  5. 버      젼: 1.0
//  6. 작      성: 2009.11.18
//  7. 수      정:
// **********************************************************
package com.ziaan.cp;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;
import com.ziaan.system.CodeAdminBean;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class CpBookBean { 

    public CpBookBean() { 
        try { 
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
       
    
    /**
     * 과목 SELECT
     * @param box          receive from the form object and session
     * @return ArrayList   과목 리스트
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

              if ( s_gadmin.startsWith("M")) {      //  과목관리자, 강사
            	  
                  sql = "\n select distinct a.subj, a.subjnm, a.cp, b.cpnm, b.userid, get_name(b.userid) as name, b.handphone "
                	  + "\n from   tz_subj a "
                	  + "\n      , tz_cpinfo b "
                	  + "\n where  a.cp = b.cpseq "
                	  + "\n and    a.usebook = 'Y' "                	  
               	 	  + "\n and    a.cp in (select cpseq from tz_cpinfo where userid ='"+s_userid+"')"    
                	  + "\n order  by a.subjnm ";

              } else {
            	  
                  sql = "\n select distinct a.subj, a.subjnm, a.cp, b.cpnm, b.userid, get_name(b.userid) as name, b.handphone "
                	  + "\n from   tz_subj a "
                	  + "\n      , tz_cpinfo b"
                	  + "\n where  a.cp = b.cpseq "
                	  + "\n and    a.usebook = 'Y' "                  	  
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
         * 자료실 리스트화면 select
         * @param    box          receive from the form object and session
         * @return ArrayList  자료실 리스트
         * @throws Exception
         */
         public ArrayList selectList(RequestBox box) throws Exception { 
             DBConnectionManager	connMgr	= null;
             ListSet             ls      = null;
             ListSet             ls2      = null;
             ArrayList           list    = null;
             String              sql     = "";
             DataBox             dbox    = null;

             String v_userid    	= box.getSession("userid");        
             String v_gadmin     	= box.getSession("gadmin"); 
             String v_year 			= box.getString("s_gyear");         
             String v_subj     		= box.getString("s_subjcourse"); 
             String v_subjseq     		= box.getString("s_subjseq"); 
             String v_order     	= box.getString("p_order");                  
             String v_orderType     = box.getStringDefault("p_orderType", "asc");   
             String v_searchtext = box.getString("p_key1");
             String v_search     = box.getString("p_gubun");
             String v_searchtext1 = box.getString("p_searchtext1");
             String v_search1     = box.getString("p_search");            

             try { 
                 connMgr = new DBConnectionManager();

                 list = new ArrayList();
               
                 sql = "\n select a.subj, a.subjnm, get_cpnm(a.cp) cpnm, b.year, b.subjseq, b.edustart, b.eduend, b.bookname, get_compnm(c.comp) compnm, c.userid, e.name,"
                	 + "\n   (select codenm from tz_code a where a.code=d.delivery_status and gubun='0111' and code=d.delivery_status) delivery_status, delivery_status, (select codenm from tz_code a where a.code=d.delivery_comp and gubun='0112' and code=d.delivery_comp) delivery_comp, d.delivery_number, d.delivery_date, d.name dname, e.zip_cd, e.address,e.handphone, fn_crypt('2', e.birth_date, 'knise') birth_date   "     	 
	                 + "\n   from  tz_subj a, tz_subjseq b, tz_student c, tz_bookdelivery d, tz_member e   "
	                 + "\n  where  a.subj 	 = b.subj "
	                 + "\n    and  b.subj 	 = c.subj "	                 
	                 + "\n    and  b.year 	 = c.year "
	                 + "\n    and  b.subjseq = c.subjseq " 
	                 + "\n    and  c.subj 	 = d.subj(+) "
	                 + "\n    and  c.year 	 = d.year(+) "
	                 + "\n    and  c.subjseq = d.subjseq(+) " 
	                 + "\n    and  c.userid  = d.userid(+) "
	                 + "\n    and  c.userid  = e.userid "
	                 + "\n	  and  a.isuse 	 = 'Y' "
                 	 + "\n	  and  b.usebook = 'Y' ";    

                 if ( !v_year.equals("") ) {              //    년도 검색할때
                     sql +="\n  and    b.year ='" + v_year + "'";
                 }else{
                	 sql +="\n  and    b.year =''";
                 }
                 if ( !v_subj.equals("") ) {        //    과정 검색할때
                	 sql +="\n and    a.subj ='" + v_subj + "'";
                 }else{
                	 sql +="\n and    a.subj =''";
                 }
                 if ( !v_subjseq.equals("") ) {        //    과정 검색할때
                	 sql +="\n and    b.subjseq ='" + v_subjseq + "'";
                 }else{
                	 sql +="\n and    b.subjseq =''";
                 }
                 
                 if( v_gadmin.startsWith("A") ){
                	 String v_cpseq = box.getString("s_cpseq");
                	 if ( !v_cpseq.equals("") ) {
                		 sql += "\n and    a.cp = " + StringManager.makeSQL(v_cpseq);          
                	 }
                 } else if( v_gadmin.startsWith("M")){
                	 sql += "\n and    a.cp in (select cpseq from tz_cpinfo where userid ='"+v_userid+"')";          
                 }                              
                 
                 if(!v_order.equals("")){
                	 sql += "\n order  by "+ v_order + " " +v_orderType ;            	 
                 }else{
                	 sql += "\n order  by b.edustart desc, a.subj";
                 }
//System.out.println("------------배송지--------------"+sql);
                 ls = connMgr.executeQuery(sql);
                 while ( ls.next() ) { 
                     dbox = ls.getDataBox();
                     
                     //배송지 주소가없을경우 TZ_MEMBER에서 주소를 조회해온다.
                     if(dbox.getString("d_address").equals("")){
                    	 String sql1 = "select zip_cd, address, handphone \n" +
                    	 		       "  from tz_member \n" +
                    	 		       " where userid = "+StringManager.makeSQL(dbox.getString("d_userid"))+"\n";
                    	 ls2 = connMgr.executeQuery(sql1);
                    	 if(ls2.next()){
                    		 dbox.put("d_zip_cd", ls2.getString("zip_cd"));
                    		 dbox.put("d_address", ls2.getString("address"));
                    		 dbox.put("d_handphone", ls2.getString("handphone"));
                    	 }
                    	 if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                     }
                     
                     list.add(dbox);
                 }
             }
             catch ( Exception ex ) { 
                 ErrorManager.getErrorStackTrace(ex, box, sql);
                 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
             }
             finally { 
                 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                 if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
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
                	"\n  SELECT CPSEQ, CPNM FROM tz_cpinfo  ORDER BY CPSEQ  ";
                
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
		상태값 변경
		@param box      receive from the form object and session
		@return isOk    1:update success,0:update fail    
		*/      	    
		 public int UpdateStatus(RequestBox box) throws Exception {
			DBConnectionManager connMgr = null;        
			PreparedStatement pstmt = null;        
			String sql = "";
			int isOk = 0;   
			
			String v_date = box.getStringDefault("p_date",FormatDate.getDate("yyyyMMdd")).replaceAll("\\-","");
			Vector v_checks = box.getVector("p_checks");
			String v_status = box.getString("p_status");
			String v_param = "";
			String v_subj = "";
			String v_year = "";
			String v_subjseq = "";
			String v_userid = "";
			
			try {
				connMgr = new DBConnectionManager();    
				
				for(int i = 0; i < v_checks.size(); i++) {
					
	                v_param     = v_checks.elementAt(i).toString();    
	                
					StringTokenizer arr_value = new StringTokenizer(v_param, ",");
					
					v_userid	= arr_value.nextToken();
					v_subj		= arr_value.nextToken();
					v_year		= arr_value.nextToken();
					v_subjseq	= arr_value.nextToken();
					
					if(v_status.equals("Y")){

						sql = "update TZ_BOOKDELIVERY set delivery_status = 'Y'						\n"
							+ "where  	subj = ?													\n"
							+ "  and    year = ?													\n"
							+ "  and    subjseq= ?													\n"
							+ "  and    userid = ?													\n";
					}else if(v_status.equals("A")){
						sql = "update TZ_BOOKDELIVERY set delivery_status = 'A'						\n"
							+ "where  	subj = ?													\n"
							+ "  and    year = ?													\n"
							+ "  and    subjseq= ?													\n"
							+ "  and    userid = ?													\n";
					}else if(v_status.equals("F")){
						sql = "update TZ_BOOKDELIVERY set delivery_status = 'F'						\n"
							+ "where  	subj = ?													\n"
							+ "  and    year = ?													\n"
							+ "  and    subjseq= ?													\n"
							+ "  and    userid = ?													\n";
					}
					
					//System.out.println("----------------------sql -"+i+"--"+ sql);
					pstmt = connMgr.prepareStatement(sql);
					
					int param = 0;
					pstmt.setString(++param, v_subj);
					pstmt.setString(++param, v_year);
					pstmt.setString(++param, v_subjseq);
					pstmt.setString(++param, v_userid);
					
					isOk = pstmt.executeUpdate();	
					pstmt.close();
				}			
			}
			catch(Exception ex) {
				ErrorManager.getErrorStackTrace(ex, box, sql);
				throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
			}
			finally {
				if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }				
                if(connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }				
			}
			return isOk;
		}
		 
		 
		public ArrayList selectDeliveryCompExcelList(RequestBox box) throws Exception {
			
            DBConnectionManager	connMgr	= null;
            ListSet             ls      = null;

            ArrayList           list    = null;
            String              sql     = "";

            try { 
                connMgr = new DBConnectionManager();
                list = new ArrayList();
                  
                sql =
                	"\n  SELECT CODE, CODENM FROM TZ_CODE  WHERE GUBUN ='0112' ORDER BY CODE  ";
                
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
		
		
		public DataBox selectDeliveryView(RequestBox box) throws Exception {
			
            DBConnectionManager	connMgr	= null;
            ListSet             ls      = null;

            DataBox           dbox    	= null;
            String              sql     = "";
			String v_subj 				= box.getString("pp_subj");
			String v_year 				= box.getString("pp_year");
			String v_subjseq 			= box.getString("pp_subjseq");
			String v_userid 			= box.getString("pp_userid");
			
            try { 
                connMgr = new DBConnectionManager();
                  
                sql = "    SELECT b.subjnm, a.* FROM TZ_BOOKDELIVERY a, TZ_SUBJ b " 
                	+ "\n   WHERE  a.SUBJ = b.SUBJ " 
                	+ "\n     AND  a.SUBJ = " + StringManager.makeSQL(v_subj)
                	+ "\n     AND  a.YEAR = " + StringManager.makeSQL(v_year)
                	+ "\n     AND  a.SUBJSEQ = " + StringManager.makeSQL(v_subjseq)
                	+ "\n     AND  a.USERID = " + StringManager.makeSQL(v_userid);             	  

                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                	dbox = ls.getDataBox();              
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
            return dbox;			
		}   		 
		
		
        /**
         * 자료실 리스트화면 select
         * @param    box          receive from the form object and session
         * @return ArrayList  자료실 리스트
         * @throws Exception
         */
         public ArrayList selectSampleList(RequestBox box) throws Exception { 
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
               
                 sql = "\n select a.subj, a.subjnm, get_cpnm(a.cp) cpnm, b.year, b.subjseq, b.edustart, b.eduend, b.bookname, get_compnm(c.comp) compnm, c.userid, decode(d.name, '', e.name, d.name) name , decode(d.zip_cd, '', e.zip_cd, d.zip_cd) zip_cd, decode(d.address, '', e.address, d.address) address, decode(d.handphone, '', e.handphone, d.handphone) handphone, d.delivery_status, d.delivery_comp, d.delivery_number, d.delivery_date   "     	 
	                 + "\n   from  tz_subj a, tz_subjseq b, tz_student c, tz_bookdelivery d, tz_member e "
	                 + "\n  where  a.subj 	 = b.subj "
	                 + "\n    and  b.subj 	 = c.subj "	                 
	                 + "\n    and  b.year 	 = c.year "
	                 + "\n    and  b.subjseq = c.subjseq " 
	                 + "\n    and  c.subj 	 = d.subj(+) "
	                 + "\n    and  c.year 	 = d.year(+) "
	                 + "\n    and  c.subjseq = d.subjseq(+) " 
	                 + "\n    and  c.userid  = d.userid(+) "  
	                 + "\n    and  c.userid  = e.userid "  		                 
	                 + "\n	  and  a.isuse 	 = 'Y' "
                 	 + "\n	  and  b.usebook = 'Y' ";                 
                 
                 if ( !v_year.equals("") ) {              //    년도 검색할때
                     sql +="\n  and    substr(b.edustart,0,4) ='" + v_year + "'";
                 }
                 if ( !v_month.equals("") ) {        //    월 검색할때
                     sql +="\n         and    substr(b.edustart,5,2) ='" + v_month + "'";
                 }
                 if ( !v_subj.equals("") ) {        //    과정 검색할때
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
                	 sql += "\n order  by b.edustart desc, a.subj";
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
		
         
		public String isExitUserid(DBConnectionManager connMgr, String subj, String year, String subjseq, String userid) throws Exception {

			PreparedStatement pstmt1 = null;
			ResultSet rs1 = null;

            String              sql     = "";
            String				value	= "";
            int cnt = 0;

            try { 
                connMgr = new DBConnectionManager();
                  
                sql = "\n  SELECT count(*) cnt from tz_student where subj =? and year =? and subjseq =? and userid =? ";
                
                pstmt1 = connMgr.prepareStatement(sql);
                pstmt1.setString(1, subj	);
                pstmt1.setString(2, year	);
                pstmt1.setString(3, subjseq	); 
                pstmt1.setString(4, userid	);                  
                rs1 = pstmt1.executeQuery();
                

                if ( rs1.next() ) { 
                    cnt = rs1.getInt("cnt");
                    if ( cnt > 0) { 
                    	value = "0";   // 정상.
                    }
                    else{ 
                    	value = "1";   // 수강생정보
                    }
                }
            }
            catch ( Exception ex ) { 
                ErrorManager.getErrorStackTrace(ex, null, sql);
                throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
            }
            finally { 
                if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
                if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }                
            }
            return value;			
		}  
		
		
		public String isDelivery(DBConnectionManager connMgr, String subj, String year, String subjseq, String userid) throws Exception {

			PreparedStatement pstmt1 = null;
			ResultSet rs1 = null;

            String              sql     = "";
            String				value	= "";
            int cnt = 0;

            try { 
                connMgr = new DBConnectionManager();
                  
                sql = "\n  SELECT count(*) cnt from tz_bookdelivery where subj =? and year =? and subjseq =? and userid =? ";
                
                pstmt1 = connMgr.prepareStatement(sql);
                pstmt1.setString(1, subj	);
                pstmt1.setString(2, year	);
                pstmt1.setString(3, subjseq	); 
                pstmt1.setString(4, userid	);                  
                rs1 = pstmt1.executeQuery();
                

                if ( rs1.next() ) { 
                    cnt = rs1.getInt("cnt");
                    if ( cnt > 0) { 
                    	value = "Y";   // 정상.
                    }
                    else{ 
                    	value = "N";   // 배송정보
                    }
                }
            }
            catch ( Exception ex ) { 
                ErrorManager.getErrorStackTrace(ex, null, sql);
                throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
            }
            finally { 
                if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
                if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }                
            }
            return value;			
		}   
			
		
		/**
		 * 배송정보 수정 (운송장번호 외)
		 * @param box
		 * @return
		 * @throws Exception 
		 */
		public int UpdateDeliveryInfo(Hashtable data) throws Exception {
	        int isOk = 0;
	        boolean v_CreateConnManager = false;
	        boolean v_CreatePreparedStatement = false;

	        DBConnectionManager	connMgr	= null;
	        PreparedStatement   pstmt   = null;
	        String sql = "";

	        String v_subj = (String)data.get("subj");
	        String v_year = (String)data.get("year");
	        String v_subjseq = (String)data.get("subjseq");
	        String v_userid = (String)data.get("userid");
	        String v_bookname = (String)data.get("bookname");	        
	        String v_name = (String)data.get("name");	
	        String v_zipcd = (String)data.get("zipcd");	   	        
	        String v_address = (String)data.get("address");
	        String v_handphone = (String)data.get("handphone");
	        String v_delivery_status = (String)data.get("delivery_status");	        
	        String v_delivery_comp = (String)data.get("delivery_comp");
	        String v_delivery_number = (String)data.get("delivery_number");
	        String v_delivery_date = (String)data.get("delivery_date");
	        String v_luserid = (String)data.get("luserid");        
			
	        int pidx = 1;

			sql = "update tz_bookdelivery 	\n"
				+ "set    bookname  = ?		\n"
				+ "     , name		= ?		\n"				
				+ "     , zip_cd	= ?		\n"
				+ "     , address	= ?		\n"
				+ "     , handphone	= ?		\n"	
				+ "     , delivery_status	= ?	\n"				
				+ "     , delivery_comp		= ?	\n"
				+ "     , delivery_number 	= ?	\n"
				+ "     , delivery_date 	= ?	\n"
				+ "     , luserid 			= ? \n"
				+ "     , ldate 			= ? \n"
				+ "where  subj = ?			\n"
				+ "and    year = ?			\n"
				+ "and    subjseq= ?		\n"
				+ "and    userid = ?		\n";
	        
	        try { 
	            connMgr = (DBConnectionManager)data.get("connMgr");
	            if ( connMgr == null ) { 
	                connMgr = new DBConnectionManager();
	                v_CreateConnManager = true;
	            }

	            if ( pstmt == null ) { 
	            	pstmt = connMgr.prepareStatement(sql);
	                v_CreatePreparedStatement = true;
	            }
	            pstmt.setString( pidx++, v_bookname );
	            pstmt.setString( pidx++, v_name );
	            pstmt.setString( pidx++, v_zipcd );
	            pstmt.setString( pidx++, v_address );	
	            pstmt.setString( pidx++, v_handphone );	
	            pstmt.setString( pidx++, v_delivery_status );	            
	            pstmt.setString( pidx++, v_delivery_comp );
	            pstmt.setString( pidx++, v_delivery_number );
	            pstmt.setString( pidx++, v_delivery_date );
	            pstmt.setString( pidx++, v_luserid );
	            pstmt.setString( pidx++, FormatDate.getDate("yyyyMMddHHmmss") );
	            pstmt.setString( pidx++, v_subj );
	            pstmt.setString( pidx++, v_year );
	            pstmt.setString( pidx++, v_subjseq );
	            pstmt.setString( pidx++, v_userid );


	            isOk = pstmt.executeUpdate();

	        } catch ( Exception ex ) { 
	            ErrorManager.getErrorStackTrace(ex, null, sql);
	            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
	        } finally { 
	            if ( v_CreateConnManager) { 
	                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
	            }
	            if ( v_CreatePreparedStatement) { 
	                if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
	            }
	        }

	        return isOk;

		}
		
		/**
		 * 배송정보 수정 (운송장번호 외)
		 * @param box
		 * @return
		 * @throws Exception 
		 */
		public int InsertDeliveryInfo(Hashtable data) throws Exception {
	        int isOk = 0;
	        boolean v_CreateConnManager = false;
	        boolean v_CreatePreparedStatement = false;

	        DBConnectionManager	connMgr	= null;
	        PreparedStatement   pstmt   = null;
	        String sql = "";

	        String v_subj = (String)data.get("subj");
	        String v_year = (String)data.get("year");
	        String v_subjseq = (String)data.get("subjseq");
	        String v_userid = (String)data.get("userid");
	        String v_bookname = (String)data.get("bookname");	        
	        String v_name = (String)data.get("name");	
	        String v_zipcd = (String)data.get("zipcd");	   	        
	        String v_address = (String)data.get("address");
	        String v_handphone = (String)data.get("handphone");
	        String v_delivery_status = (String)data.get("delivery_status");	        
	        String v_delivery_comp = (String)data.get("delivery_comp");
	        String v_delivery_number = (String)data.get("delivery_number");
	        String v_delivery_date = (String)data.get("delivery_date");
	        String v_luserid = (String)data.get("luserid");    
			
	        int pidx = 1;

			sql = "insert into tz_bookdelivery (				\n"
				+ "	    subj, year, subjseq, userid, bookname,	\n"
				+ "     name, zip_cd, address, handphone,		\n"
				+ "     delivery_status, delivery_comp, delivery_number, delivery_date,	\n"
				+ "     ldate, luserid ) 	\n"
				+ "     values ( 			\n"
				+ "		?,?,?,?,?,			\n"
				+ "		?,?,?,?,			\n"
				+ "		?,?,?,?,			\n"				
				+ "		?,? )				\n";
	        
	        try { 
	            connMgr = (DBConnectionManager)data.get("connMgr");
	            if ( connMgr == null ) { 
	                connMgr = new DBConnectionManager();
	                v_CreateConnManager = true;
	            }

	            if ( pstmt == null ) { 
	            	pstmt = connMgr.prepareStatement(sql);
	                v_CreatePreparedStatement = true;
	            }
	           
	            pstmt.setString( pidx++, v_subj );
	            pstmt.setString( pidx++, v_year );
	            pstmt.setString( pidx++, v_subjseq );
	            pstmt.setString( pidx++, v_userid );	            
	            pstmt.setString( pidx++, v_bookname );
	            pstmt.setString( pidx++, v_name );
	            pstmt.setString( pidx++, v_zipcd );
	            pstmt.setString( pidx++, v_address );	
	            pstmt.setString( pidx++, v_handphone );	
	            pstmt.setString( pidx++, v_delivery_status );	            
	            pstmt.setString( pidx++, v_delivery_comp );
	            pstmt.setString( pidx++, v_delivery_number );
	            pstmt.setString( pidx++, v_delivery_date );
	            pstmt.setString( pidx++, v_luserid );
	            pstmt.setString( pidx++, FormatDate.getDate("yyyyMMddHHmmss") );


	            isOk = pstmt.executeUpdate();

	        } catch ( Exception ex ) { 
	            ErrorManager.getErrorStackTrace(ex, null, sql);
	            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
	        } finally { 
	            if ( v_CreateConnManager) { 
	                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
	            }
	            if ( v_CreatePreparedStatement) { 
	                if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
	            }
	        }

	        return isOk;

		}
		
		/**
		 * 도서배송관리
		 * jsp에서 엑셀파일 작성시 그 파일을 다시 업로드 했을경우 파일을 인식하지 못한다.
		 * 원인은 jsp에서 만든 엑셀파일은 순수 엑셀파일이 아닌 html로 작성된것이므로 엑셀을 read할경우 OLE객체를 읽지 못하는 에러가 발생한다.
		 * 해결책으로 서버에서 엑셀파일을 생성(jxl을 이용)해서 다운로드 할 수 있게 방법을 변경하였다.
		 * @param box          receive from the form object and session
		 * @return ArrayList   도서배송관리
		 */      
		public int ExcelDownBookDelivery(RequestBox box, ArrayList list) throws Exception {               
			DataBox dbox = null;
			String fileNm = "Delivery_List.xls";
			ConfigSet conf = new ConfigSet(); 
			int retCnt = 0;
			
			try {
				WritableWorkbook workbook = Workbook.createWorkbook(new File(conf.getProperty("dir.upload.default") + fileNm)); // 파일이름을 정하여 생성한다.
				
				WritableSheet sheet = workbook.createSheet("Sheet1", 0); // WritableSheet는 인터페이스

			    WritableCellFormat numberFormat = new WritableCellFormat(); // 번호 셀 포멧 생성
			    WritableCellFormat dataFormat = new WritableCellFormat(); // 번호 셀 포멧 생성

			    // 번호 레이블 셀 포멧 구성(자세한 것은 링크 된 API를 참조하셈) 참고사항 : 여기 부분에서 WriteException이 발생하네요.
			    // 그러나 상단에서 미리 예외 처리를 해서 상관 없겠네요.
			    numberFormat.setAlignment(Alignment.CENTRE); // 셀 가운데 정렬
			    numberFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 가운데 정렬
			  
				// 쉬트의 컬럼 넓이 설정
			    sheet.setColumnView( 0, 5); // 쉬트의 번호 컬럼(0번째)의 넓이 설정. setCloumnView(몇번째 컬럼, 넓이)
				sheet.setColumnView( 1, 50); // 쉬트의 이름 컬럼(1번째)의 넓이 설정
				sheet.setColumnView( 2, 25); // 쉬트의 비고 컬럼(2번째)의 넓이 설정
				sheet.setColumnView( 3, 15); // 쉬트의 비고 컬럼(3번째)의 넓이 설정
				sheet.setColumnView( 4, 10); // 쉬트의 비고 컬럼(4번째)의 넓이 설정
				sheet.setColumnView( 5, 15); // 쉬트의 비고 컬럼(5번째)의 넓이 설정
				sheet.setColumnView( 6, 20); // 쉬트의 비고 컬럼(6번째)의 넓이 설정
				sheet.setColumnView( 7, 50); // 쉬트의 비고 컬럼(7번째)의 넓이 설정
				sheet.setColumnView( 8, 30); // 쉬트의 비고 컬럼(8번째)의 넓이 설정
				sheet.setColumnView( 9, 15); // 쉬트의 비고 컬럼(9번째)의 넓이 설정
				sheet.setColumnView(10, 50); // 쉬트의 비고 컬럼(9번째)의 넓이 설정
				sheet.setColumnView(11, 20); // 쉬트의 비고 컬럼(9번째)의 넓이 설정
				sheet.setColumnView(12, 20); // 쉬트의 비고 컬럼(9번째)의 넓이 설정
				sheet.setColumnView(13, 30); // 쉬트의 비고 컬럼(9번째)의 넓이 설정
				sheet.setColumnView(14, 15); // 쉬트의 비고 컬럼(9번째)의 넓이 설정	
				sheet.setColumnView(15, 10); // 쉬트의 비고 컬럼(9번째)의 넓이 설정					

				  
			    // 라벨을 이용하여 해당 셀에 정보 넣기 시작
			    sheet.addCell(new Label(0, 0, "NO",			dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
			    sheet.addCell(new Label(1, 0, "과정명",		dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
			    sheet.addCell(new Label(2, 0, "학습기간",	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
			    sheet.addCell(new Label(3, 0, "과정코드(★)",	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
			    sheet.addCell(new Label(4, 0, "년도(★)", 		dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
			    sheet.addCell(new Label(5, 0, "과정차수(★)",	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
			    sheet.addCell(new Label(6, 0, "아이디(★)",		dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
			    sheet.addCell(new Label(7, 0, "도서명(★)",  	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
			    sheet.addCell(new Label(8, 0, "수신인(★)",   		dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
			    sheet.addCell(new Label(9, 0, "우편번호(★)",	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
			    sheet.addCell(new Label(10, 0, "주소(★)", 		dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
			    sheet.addCell(new Label(11, 0, "연락처(★)", 	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
			    sheet.addCell(new Label(12, 0, "택배사코드(★)",	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
			    sheet.addCell(new Label(13, 0, "운송장번호(★)",  	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
			    sheet.addCell(new Label(14, 0, "배송일(★)",   	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입	
			    sheet.addCell(new Label(15, 0, "배송상태",   	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입				    
			    
			    retCnt = list.size();
				if(list!=null && retCnt > 0) {

					for(int i = 0; i < retCnt; i++) {
						dbox  = (DataBox)list.get(i);

					    sheet.addCell(new Label( 0, i+1, (i+1)+"",		dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입			    
					    sheet.addCell(new Label( 1, i+1, dbox.getString("d_subjnm"),	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입		    
					    sheet.addCell(new Label( 2, i+1, dbox.getString("d_edustart")+"~"+dbox.getString("d_eduend"),	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입				    
					    sheet.addCell(new Label( 3, i+1, dbox.getString("d_subj"),		dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입						    
					    sheet.addCell(new Label( 4, i+1, dbox.getString("d_year"), 		dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입						    
					    sheet.addCell(new Label( 5, i+1, dbox.getString("d_subjseq"),	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입					    
					    sheet.addCell(new Label( 6, i+1, dbox.getString("d_userid"),	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입						    
					    sheet.addCell(new Label( 7, i+1, dbox.getString("d_bookname"),	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입					    
					    sheet.addCell(new Label( 8, i+1, dbox.getString("d_name"),  	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입							    
					    sheet.addCell(new Label( 9, i+1, dbox.getString("d_zip_cd"), 	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입						    
					    sheet.addCell(new Label(10, i+1, dbox.getString("d_address"), 	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입						    
					    sheet.addCell(new Label(11, i+1, dbox.getString("d_handphone"),	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입							    
					    sheet.addCell(new Label(12, i+1, dbox.getString("d_delivery_comp"),   		dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
					    sheet.addCell(new Label(13, i+1, dbox.getString("d_delivery_number"),   	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입						    
					    sheet.addCell(new Label(14, i+1, FormatDate.getDate("yyyyMMdd"),   	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
					    sheet.addCell(new Label(15, i+1, CodeAdminBean.getCodeName("0111",dbox.getString("d_delivery_status")),   	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입						    
					}
				}
			    workbook.write();
			    workbook.close();
			}            
			catch (Exception ex) {
				ErrorManager.getErrorStackTrace(ex);
			}
			return retCnt;
			
		}				
		
		
		/**
		 * 배송정보 수정 (운송장번호 외)
		 * @param box
		 * @return
		 * @throws Exception 
		 */
		public int UpdateDeliveryInfo(RequestBox box) throws Exception {
	        DBConnectionManager	connMgr	= null;
	        PreparedStatement   pstmt   = null;
	        String sql 	= "";
	        int isOk 	= 0;

	        String v_subj = box.getString("p_subj");
	        String v_year = box.getString("p_year");
	        String v_subjseq = box.getString("p_subjseq");
	        String v_userid = box.getString("p_userid");
	        String v_bookname = box.getString("p_bookname");	        
	        String v_name = box.getString("p_name");	
	        String v_zipcd = box.getString("p_zipcd");	   	        
	        String v_address = box.getString("p_addr");
	        String v_handphone = box.getString("p_handphone");
	        String v_delivery_status = box.getString("p_delivery_status");	        
	        String v_delivery_comp = box.getString("p_delivery_comp");
	        String v_delivery_number = box.getString("p_delivery_number");
	        String v_delivery_date = box.getString("p_delivery_date").replaceAll("\\-","");
	        String v_luserid = box.getSession("userid");        
		

			sql = "update tz_bookdelivery 	\n"
				+ "set    bookname  = ?		\n"
				+ "     , name		= ?		\n"				
				+ "     , zip_cd	= ?		\n"
				+ "     , address	= ?		\n"
				+ "     , handphone	= ?		\n"	
				+ "     , delivery_status	= ?	\n"				
				+ "     , delivery_comp		= ?	\n"
				+ "     , delivery_number 	= ?	\n"
				+ "     , delivery_date 	= ?	\n"
				+ "     , luserid 			= ? \n"
				+ "     , ldate 			= ? \n"
				+ "where  subj = ?			\n"
				+ "and    year = ?			\n"
				+ "and    subjseq= ?		\n"
				+ "and    userid = ?		\n";
	        
	        try { 
                connMgr = new DBConnectionManager();
                
				pstmt = connMgr.prepareStatement(sql);
				
		        int pidx = 1;		
	            pstmt.setString( pidx++, v_bookname );
	            pstmt.setString( pidx++, v_name );
	            pstmt.setString( pidx++, v_zipcd );
	            pstmt.setString( pidx++, v_address );	
	            pstmt.setString( pidx++, v_handphone );	
	            pstmt.setString( pidx++, v_delivery_status );	            
	            pstmt.setString( pidx++, v_delivery_comp );
	            pstmt.setString( pidx++, v_delivery_number );
	            pstmt.setString( pidx++, v_delivery_date );
	            pstmt.setString( pidx++, v_luserid );
	            pstmt.setString( pidx++, FormatDate.getDate("yyyyMMddHHmmss") );
	            pstmt.setString( pidx++, v_subj );
	            pstmt.setString( pidx++, v_year );
	            pstmt.setString( pidx++, v_subjseq );
	            pstmt.setString( pidx++, v_userid );

	            isOk = pstmt.executeUpdate();           

	        } catch ( Exception ex ) { 
	            ErrorManager.getErrorStackTrace(ex, null, sql);
	            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
	        } finally { 
				if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }				
                if(connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }	
	        }

	        return isOk;

		}
		
		/**
		 * 도서배송관리 - 20100611
		 * @param box          receive from the form object and session
		 * @return ArrayList   도서배송관리
		 */      
		public String ExcelDownBookDelivery2(RequestBox box) throws Exception {               
			DBConnectionManager connMgr = null;
	        ListSet ls  = null; 
	        ConfigSet conf = new ConfigSet();       
	        int isOk = 0;
	        int isOk1= 0; 
	      
	        String  s_grcode     = box.getString("s_grcode");
	        String  s_gyear      = box.getString("s_gyear");
	        String  s_grseq      = box.getString("s_grseq");
	        String  s_subjcourse     = box.getString("s_subjcourse");
	        String  s_subjseq      = box.getString("s_subjseq");
	        String  s_grname      = box.getString("s_grname");
	        String  s_subjnm      = box.getString("s_subjnm");
	        String  s_subjseqnm      = box.getString("s_subjseqnm");
	       
	        String  v_luserid      = box.getSession("userid");
	        String  v_realFileName = box.getRealFileName("p_file");
	        String  v_newFileName  = box.getNewFileName("p_file");
	        
	        int     i=0;
	        int irows =1; // 첫번째 로우는 데이터가 들어가지 않음
	        PreparedStatement   pstmt   = null;
	        PreparedStatement   pstmtj   = null;
	        
	        /**
	   	    * 데이타 검증 관련 필드
	   	    */
		    int is_inputok = 0;     // 에러 유무(정상 1, 공백 0)
		    int cnt_total = 0;      // 총 카운트
		    int cnt_succ = 0;       // 총 성공 카운트
		    int cnt_error = 0;      // 총 에러 카운트
		    String v_errnme="";     // 에러명
		    
//		    //수험번호 코드
//		    int cnt_examnum=0;
//		    String str_examnum="";
//		    //점수 
//		    int cnt_score=0;
//		    String str_score="";
		    
		    //ID
		    int cnt_userid=0;
		    String str_userid="";
		    //이름
		    int cnt_name=0;
		    String str_name="";
		    //택배사코드
		    int cnt_delivery_comp=0;
		    String str_delivery_comp="";
		    //운송장번호
		    int cnt_delivery_number=0;
		    String str_delivery_number="";
		    
	    //System.out.println("box값  :"+box);	   
		    //결과 
		    String str_result = "";
		    
	        Cell cell = null;
	        Sheet sheet = null;
	        Workbook workbook = null;
	        
	        try {
	       	    connMgr = new DBConnectionManager();
	     	    connMgr.setAutoCommit(false);
	     	    workbook = Workbook.getWorkbook(new File(conf.getProperty("dir.upload.default")+v_newFileName));
         //System.out.println("workbook : "+workbook);
	            sheet = workbook.getSheet(0);
	            
	            String sql1="", sql="";
	           
	            for (i=1; i < sheet.getRows(); i++ ) {
		    	    v_errnme = "";
		    	    irows ++;
		    	    cnt_total ++;
		    	    int j=0;
		    	    
		    	    int v_idcnt=0;
		            String v_userid= "";
		            String v_name="";
		            String v_delivery_comp="";
		            String v_delivery_number="";
		            
//		            for( j=0; j < sheet.getColumns(); j++){
			            v_userid				= sheet.getCell(j,i).getContents();
			            v_name					= sheet.getCell(++j,i).getContents();
			            v_delivery_comp  		= sheet.getCell(++j,i).getContents();
			            v_delivery_number		= sheet.getCell(++j,i).getContents();
	//	            }
	            	
//		            System.out.println("v_userid ::::["+i+ "]-["+j+"]"+v_userid);
//		            System.out.println("v_name ::::["+i+ "]-["+j+"]"+v_name);
//		            System.out.println("v_delivery_comp ::::["+i+ "]-["+j+"]"+v_delivery_comp);
//		            System.out.println("v_delivery_number ::::["+i+ "]-["+j+"]"+v_delivery_number);
		            //ID 확인 체크
	            	if (v_userid.equals("")) { 
		        	    is_inputok = 1;
		        	    cnt_userid++;
		      	        str_userid += "["+irows+"로우의 ID를 입력해 주세요.]<br>";
		            }
	            
	            	
	            	sql  = " select userid, name from tz_bookdelivery ";
	            	sql += " where subj = '"+s_subjcourse+"' and subjseq='"+s_subjseq+"' and year='"+s_gyear+"' and userid='"+v_userid+"' and name='"+v_name+"'    \n"; 
//System.out.println("sql-----------select  : \n"+sql);	
	            	ls = connMgr.executeQuery(sql);
	                
	                if (ls.next()) {
	                	v_userid = ls.getString("userid");
	                }

            	if(ls != null) {	
             	    ls.close();
                }

		            //이름 확인 체크 
		            if (v_name.equals("")) {
		        	    is_inputok = 1;
		        	    cnt_name++;
		      	        str_name += "["+irows+"로우의 이름을 입력해 주세요.]<br>";
		            }
		            //택배사 체크
	            	if (v_delivery_comp.equals("")) { 
		        	    is_inputok = 1;
		        	    cnt_delivery_comp++;
		      	        str_delivery_comp += "["+irows+"로우의 택배사코드를 입력해 주세요.]<br>";
		            }
		            //운송장번호 확인 체크 
		            if (v_delivery_number.equals("")) {
		        	    is_inputok = 1;
		        	    cnt_delivery_number++;
		      	        str_delivery_number += "["+irows+"로우의 운송장번호를 입력해 주세요.]<br>";
		            }

		            if(is_inputok == 0) {
		            	
//		                sql1  = " update tz_student set   ";
//		                sql1 += " mtest =?  " ;
//		                sql1 += " where subj = '"+s_subjcourse+"' and subjseq='"+s_subjseq+"' and year='"+s_gyear+"' and examnum='"+v_examnum+"'  \n"; 

		            	sql1  = " insert into  tz_bookdelivery  ";
		            	sql1 += " (subj, year, subjseq, userid, name, delivery_comp, delivery_number, delivery_status, luserid, delivery_date, ldate  )  " ;
		            	sql1 += " values 	";
		            	sql1 += " ('"+s_subjcourse+"', '"+s_gyear+"', '"+s_subjseq+"', '"+v_userid+"', '"+v_name+"', '"+v_delivery_comp+"', '"+v_delivery_number+"', 'A', '"+v_luserid+"', to_char(sysdate,'yyyymmdd'), to_char(sysdate,'yyyymmddhh24miss')) 	";
		            //System.out.println("sql1 --------------------------------------- : \n"+sql1);		            	
		                pstmt = connMgr.prepareStatement(sql1);

//		                pstmt.setString(1, v_delivery_number);
		                isOk = pstmt.executeUpdate();
			    
		                if(pstmt != null) {	
		             	    pstmt.close();
		                }
	            
		            }else{
		            	sql1  = " update tz_bookdelivery set   ";
		                sql1 += " delivery_comp=? , delivery_number =? , delivery_status='A', delivery_date=to_char(sysdate,'yyyymmdd') " ;
		                sql1 += " where subj = '"+s_subjcourse+"' and subjseq='"+s_subjseq+"' and year='"+s_gyear+"' and userid='"+v_userid+"' and name='"+v_name+"'    \n";
  //System.out.println("sql1 --update------------------------------------- : \n"+sql1);		            	
		                pstmt = connMgr.prepareStatement(sql1);

		                pstmt.setString(1, v_delivery_comp);
		                pstmt.setString(2, v_delivery_number);
		                isOk = pstmt.executeUpdate();
			    
		                if(pstmt != null) {	
		             	    pstmt.close();
		                }
//		        	    cnt_error ++;
		            }
		      
		            str_result = "처리결과 <br><br>총입력 : "+cnt_total +"건  <br><br>성공: "+cnt_succ +"건 실패: "+cnt_error  +"건<br><br>";
		   	        str_result += "[오류 건수 상세 내용]<br><br>";
		   	        str_result += "1.ID 없음 :   "+cnt_userid+"건<br>"+str_userid+"<br><br>";       	    
		   	        str_result += "2.이름 없음 :  "+cnt_name+"건<br>"+str_name+"<br><br>";
		   	        str_result += "3.택배사코드 없음 :   "+cnt_delivery_comp+"건<br>"+str_delivery_comp+"<br><br>";
		   	     	str_result += "4.운송장번호 없음 :   "+cnt_delivery_number+"건<br>"+str_delivery_number+"<br><br>";
		        }   
	            if(cnt_error > 0)  {
	        	    connMgr.rollback();
	            }else {
	   	            connMgr.commit();
	   	        }
	           
	        } catch (Exception e) {
	       	    connMgr.rollback(); 
	        } finally {
	    	    if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
	    	    if ( pstmtj != null ) { try { pstmtj.close(); } catch ( Exception e ) { } }
	    	    if(ls != null) { try { ls.close(); } catch (Exception e) {} }
	      	    if(connMgr != null) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); }catch (Exception e10) {} }
	        }  
	        return str_result;
	    }
		
		   
}