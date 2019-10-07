package com.ziaan.homepage;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.system.CodeData;

/**
 * file name : AdminBean.java
 * date      : 2006/3/7
 * programmer: kimjayoung chopin.kim@ziaan.samsung.com
 * function  : AdminBean
 */
 
public class ScheduleBean {

    public ScheduleBean() {}
 
  /********************
   * 1년치 달별 스케줄 select
   ********************/
   public Vector selectScheduleList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        DataBox dbox    = null;
        ArrayList list  = null;

        String v_sel_year	= box.getString("p_sel_year");
    	String v_sel_month_ = box.getStringDefault("p_sel_month", FormatDate.getDate("MM"));
        int v_sel_month = Integer.parseInt(v_sel_month_);
        String v_date		= "";
        
        Vector vc = new Vector();
		
        int i=0;
        int j=0;
        int k=0;
        
        String v_default_var = "----";
    	boolean isAll = false;
    	String ss_gadmin = box.getSession("gadmin");
    	String v_gadmin = ss_gadmin.substring(0,1);
    	if("A".equals(v_gadmin)) {
    		isAll = true;		
    		v_default_var = "ALL";
    	}
        String v_gadmin2 = box.getStringDefault("p_gadmin", "ALL");
    	
        try {
			
			connMgr = new DBConnectionManager();         
            list = new ArrayList();
            
            String v_month = "01";
            int v_prev_month = 2;
            int v_next_month = 3;
            String v_startdate = "200802";
            String v_enddate = "200803";
            String v_prev_realyear = "2008";
            String v_next_realyear = "2008";
            String v_realyear = "2008";
     		if(v_sel_month == 1) {
     			v_prev_month = 1;
     			v_startdate = (Integer.parseInt(v_sel_year)-1) + "" + v_prev_month;
     			v_prev_realyear = (Integer.parseInt(v_sel_year)-1) + "";
     		} else {
     			v_prev_month = v_sel_month - 1;
     			if(v_prev_month < 10) {
     				v_startdate = v_sel_year + "0" + v_prev_month;	
     			} else {
     				v_startdate = v_sel_year + v_prev_month;
     			}
     			v_prev_realyear = v_sel_year;
     		}
     		
     		if(v_sel_month == 12) {
     			v_next_month = 1;
     			v_enddate = (Integer.parseInt(v_sel_year)+1) + "" + v_next_month;
     			v_next_realyear = (Integer.parseInt(v_sel_year)+1) + "";
     		} else {
     			v_next_month = v_sel_month + 1;
     			if(v_next_month < 10) {
     				v_enddate = v_sel_year + "0" + v_next_month;	
     			} else {
     				v_enddate = v_sel_year + v_next_month;
     			}
     			v_next_realyear = v_sel_year;
     		}
            
     		int v_realmonth = 1;
     		for(i=1;i<=2;i++){
         	   if( i == 1) {
         		   v_realmonth = v_sel_month;
         		   v_realyear = v_sel_year;
     	       } else if (i == 2) {
         		   v_realmonth = v_next_month;
         		   v_realyear = v_next_realyear;
     	       }
            	
         	   if(v_realmonth < 10) {
         		   v_month = "0" + v_realmonth;
         	   } else {
         		   v_month = v_realmonth + "";
         	   }
         	   
            	sql = "select f_year || f_month || f_date fdate 		\n"
            		+ "		,  content, flag, seq 						\n"
            		+ "     , f_year, f_month, f_date f_day     		\n"
            		+ "     , b.codenm gubun, c.codenm title, a.titlecd, a.gubun, a.gadmin    \n"
            		+ "from tz_schedule a, (							\n"
            		+ "	 select code, codenm							\n"
            		+ "	 from tz_code									\n"
            		+ "	 where gubun = '0107' -- 수료기준					\n" 
            		+ ") b, (											\n"
            		+ "  select code, codenm							\n"
            		+ "	 from tz_code									\n"
            		+ "	 where gubun = '0108' -- title					\n"
            		+ ") c  						  					\n"
            		+ "where 1=1 										\n"
            		+ "and a.gubun = b.code								\n"
            		+ "and a.titlecd = c.code 							\n"	
            		+ "and (F_YEAR || F_MONTH = " + StringManager.makeSQL(v_realyear + v_month) + " ) \n";
            		if(!"ALL".equals(v_gadmin2)) {
            			sql += "and gadmin = " + StringManager.makeSQL(v_gadmin2) + " \n";
            		}
            	sql+= "order by f_year, f_month, f_date 				\n";
            	
            	ls = connMgr.executeQuery(sql);
				
            	while (ls.next()) { 
            		dbox = ls.getDataBox();  
            	    list.add(dbox);   
            	     
            	}  
            	ls.close();
            	vc.addElement(list);     
            	list = new ArrayList();         	
            }   
        }             
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) {try {ls.close();} catch(Exception e){}}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return vc;
    }
   
   
   /********************
    * 1년치 달별 스케줄 select - 사용자
    ********************/
    public Vector selectUserScheduleList(RequestBox box) throws Exception {
         DBConnectionManager connMgr = null;
         ListSet ls = null;
         String sql = "";
         DataBox dbox    = null;
         ArrayList list  = null;

         String v_sel_year	= box.getString("p_sel_year");
     	String v_sel_month_ = box.getStringDefault("p_sel_month", FormatDate.getDate("MM"));
         int v_sel_month = Integer.parseInt(v_sel_month_);
         String v_date		= "";
         
         Vector vc = new Vector();
 		
         int i=0;
         int j=0;
         int k=0;
         
         String v_default_var = "----";
     	boolean isAll = false;
     	String ss_gadmin = box.getSession("gadmin");
     	String v_gadmin = ss_gadmin.substring(0,1);
     	if("A".equals(v_gadmin)) {
     		isAll = true;		
     		v_default_var = "ALL";
     	}
         String v_grcode = box.getStringDefault("s_grcode", v_default_var);
         
         String s_comp = box.getSession("comp");
         
         try {
 			
 			connMgr = new DBConnectionManager();         
             list = new ArrayList();
             
             String v_month = "01";
             int v_prev_month = 2;
             int v_next_month = 3;
             String v_startdate = "200802";
             String v_enddate = "200803";
             String v_prev_realyear = "2008";
             String v_next_realyear = "2008";
             String v_realyear = "2008";
      		if(v_sel_month == 1) {
      			v_prev_month = 1;
      			v_startdate = (Integer.parseInt(v_sel_year)-1) + "" + v_prev_month;
      			v_prev_realyear = (Integer.parseInt(v_sel_year)-1) + "";
      		} else {
      			v_prev_month = v_sel_month - 1;
      			if(v_prev_month < 10) {
      				v_startdate = v_sel_year + "0" + v_prev_month;	
      			} else {
      				v_startdate = v_sel_year + v_prev_month;
      			}
      			v_prev_realyear = v_sel_year;
      		}
      		
      		if(v_sel_month == 12) {
      			v_next_month = 1;
      			v_enddate = (Integer.parseInt(v_sel_year)+1) + "" + v_next_month;
      			v_next_realyear = (Integer.parseInt(v_sel_year)+1) + "";
      		} else {
      			v_next_month = v_sel_month + 1;
      			if(v_next_month < 10) {
      				v_enddate = v_sel_year + "0" + v_next_month;	
      			} else {
      				v_enddate = v_sel_year + v_next_month;
      			}
      			v_next_realyear = v_sel_year;
      		}
             
      		int v_realmonth = 1;
      		for(i=1;i<=2;i++){
          	   if( i == 1) {
          		   v_realmonth = v_sel_month;
          		   v_realyear = v_sel_year;
      	       } else if (i == 2) {
          		   v_realmonth = v_next_month;
          		   v_realyear = v_next_realyear;
      	       }
             	
          	   if(v_realmonth < 10) {
          		   v_month = "0" + v_realmonth;
          	   } else {
          		   v_month = v_realmonth + "";
          	   }
          	 	
             	sql  = " select distinct f_year || f_month || f_date fdate ";
             	sql += " 	 ,  title, content, flag, seq ";
 				sql += "      , f_year, f_month, f_date f_day                                                    ";
             	sql += "   from tz_schedule  ";
             	sql += "  where 1=1 ";
             	sql += "    and (F_YEAR + F_MONTH <= " + StringManager.makeSQL(v_realyear + v_month) + " ) \n";
             	sql += "  order by f_year + f_month + f_date ";
             	
             	ls = connMgr.executeQuery(sql);
 				
             	while (ls.next()) { 
             		dbox = ls.getDataBox();  
             	    list.add(dbox);   
             	     
             	}   
             	vc.addElement(list);     
             	list = new ArrayList();         	
             }   
         }             
         catch (Exception ex) {
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
         }
         finally {
             if(ls != null) {try {ls.close();} catch(Exception e){}}
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }
         return vc;
     }

  /********************
   * 달별 스케줄 select
   ********************/
   public ArrayList selectMonthSchedule(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        DataBox dbox    = null;
        ArrayList list  = null;

        String v_year		= box.getString("p_year");
        String v_month	= box.getString("p_month");

        if(v_month.length()==1){v_month="0"+v_month;}

        String v_date		= v_year+v_month;

        try {
        			
						connMgr = new DBConnectionManager();         
            list = new ArrayList();
            
            	
            	sql  = " select f_year||f_month||f_date fdate ";
            	sql += " 	 ,  title, content, seq ";
            	sql += "   from tz_schedule  ";
            	sql += "  where 1=1 ";
            	sql += "    and (F_YEAR||F_MONTH <= " +StringManager.makeSQL(v_date)+" ) ";
            	
            	ls = connMgr.executeQuery(sql);
				
            	while (ls.next()) { 
            			dbox = ls.getDataBox();  
            	    list.add(dbox);   
            	     
            	}   
        }             
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) {try {ls.close();} catch(Exception e){}}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

  /********************
   * 일별 스케줄 select
   ********************/
   public ArrayList selectDaySchedule(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        DataBox dbox    = null;
        ArrayList list  = null;

        String v_date		= box.getString("p_date");
       
        try {
        			
						connMgr = new DBConnectionManager();         
            list = new ArrayList();

						sql  = " select F_YEAR||F_MONTH||F_DATE fdate, title, content ";
						sql += " from tz_schedule ";
						sql += " where F_YEAR||F_MONTH||F_DATE <= "+ StringManager.makeSQL(v_date)+" \n ";
				
						ls = connMgr.executeQuery(sql);
						
           	while (ls.next()) { 
           			dbox = ls.getDataBox();  
           	    list.add(dbox);   
           	}   
        }             
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) {try {ls.close();} catch(Exception e){}}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

  /********************
   * 스케줄 insert
   ********************/
   public int insertSchedule(RequestBox box) throws Exception {

        DBConnectionManager connMgr = null;
        ListSet ls = null;
        PreparedStatement pstmt1 = null;   
        String sql1  = "";
        int isOk1 = 0;
   
        String v_startdt 		= box.getString("p_startdt");
        String v_content 		= box.getString("p_content");
		
		String v_f_year			= v_startdt.substring(0,4);
		String v_f_month		= v_startdt.substring(4,6);
		String v_f_date			= v_startdt.substring(6,8);
		
		String s_userid			= box.getSession("userid");		        
          
		String v_titlecd = box.getString("p_titlecd");
		String v_gubun = box.getString("p_gubun");
		String v_gadmin = box.getStringDefault("p_gadmin", "A");
		
		String v_flag = box.getString("p_flag");
		int v_seq = 0;
        try {
            connMgr = new DBConnectionManager();       
            
            
            sql1 = "select 'X'													\n"
           	 + "from tz_schedule 											\n"
           	 + "where f_year = " + StringManager.makeSQL(v_f_year) + "      \n"
           	 + "and f_month = " + StringManager.makeSQL(v_f_month) + "      \n"
           	 + "and f_date = " + StringManager.makeSQL(v_f_date) + "        \n"
           	 + "and titlecd = " + StringManager.makeSQL(v_titlecd) + "      \n"
           	 + "and gubun = " + StringManager.makeSQL(v_gubun) + "          \n"
           	 + "and rownum >= 1                                             \n";
        
           ls = connMgr.executeQuery(sql1);
           
           if(!ls.next()) {
        	    if(ls != null) { try { ls.close(); } catch (Exception e) {} }
	            sql1 = "select nvl(max(seq),0)+1 seq from tz_schedule		\n";
	            ls = connMgr.executeQuery(sql1);
	            if(ls.next()) {
	            	v_seq = ls.getInt("seq");
	            }
	            
				 // 스케줄 등록 ------------------------------------------------
	           	sql1 =  "insert into tz_schedule(seq, f_year, f_month, f_date, content, luserid, ldate, flag, gubun, titlecd, gadmin)";
	           	sql1 += " values (?,?,?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'), ?, ?, ?, ?)";
	           	pstmt1 = connMgr.prepareStatement(sql1);
	            
	           	pstmt1.setInt(1, v_seq);
	           	pstmt1.setString(2, v_f_year);
	           	pstmt1.setString(3, v_f_month);
	           	pstmt1.setString(4, v_f_date);             
	           	pstmt1.setString(5, v_content); 
	           	pstmt1.setString(6, s_userid);              
	           	pstmt1.setString(7, v_flag);
	           	pstmt1.setString(8, v_gubun);
	           	pstmt1.setString(9, v_titlecd);
	           	pstmt1.setString(10, v_gadmin);
	           	isOk1 = pstmt1.executeUpdate();
           }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); } catch (Exception e) {} }
            if(pstmt1 != null) { try { pstmt1.close(); } catch (Exception e1) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }            
        }
        return isOk1;
	}

  /********************
   * 스케줄 update
   ********************/
   public int updateSchedule(RequestBox box) throws Exception {

        DBConnectionManager connMgr = null;
        ListSet ls = null;
        PreparedStatement pstmt1 = null;   
        String sql1  = "";
        int isOk1 = 0;
   
        String v_startdt 		= box.getString("p_startdt");
        String v_enddt 			= box.getString("p_enddt");
        String v_content 		= box.getString("p_content");
        int	   v_seq 			= StringManager.toInt(box.getString("p_seq"));
		
		String v_f_year			= v_startdt.substring(0,4);
		String v_f_month		= v_startdt.substring(4,6);
		String v_f_date			= v_startdt.substring(6,8);
		
		String s_userid			= box.getSession("userid");		
		String v_titlecd = box.getString("p_titlecd");
		String v_flag = box.getString("p_flag");
		String v_gubun = box.getString("p_gubun");
		String v_gadmin = box.getStringDefault("p_gadmin", "A");
		
	    try {
            connMgr = new DBConnectionManager();          
            connMgr.setAutoCommit(false);
            
            sql1 = "select 'X'													\n"
            	 + "from tz_schedule 											\n"
            	 + "where f_year = " + StringManager.makeSQL(v_f_year) + "      \n"
            	 + "and f_month = " + StringManager.makeSQL(v_f_month) + "      \n"
            	 + "and f_date = " + StringManager.makeSQL(v_f_date) + "        \n"
            	 + "and titlecd = " + StringManager.makeSQL(v_titlecd) + "      \n"
            	 + "and gubun = " + StringManager.makeSQL(v_gubun) + "          \n"
            	 + "and seq != " + SQLString.Format(v_seq) + "                  \n"
            	 + "and rownum >= 1                                             \n";
         
            ls = connMgr.executeQuery(sql1);
            
            if(!ls.next()) {
	             // 스케줄 등록 ------------------------------------------------
	           	sql1  = " update tz_schedule 										\n" 
	           		  + "    set f_year 	= ? 									\n"
	           		  + "      , f_month 	= ? 									\n"
	           		  + "      , f_date 	= ? 									\n"
	           		  + "      , content 	= ? 									\n"
	           		  + "      , luserid 	= ? 									\n"
	           		  + "      , ldate 		= to_char(sysdate,'YYYYMMDDHH24MISS') 	\n"
	           		  + "      , titlecd    = ? 									\n"
	           		  + "      , flag       = ? 									\n"
	           		  + "      , gubun      = ?                                     \n"
	           		  + "      , gadmin = ?                                    \n"
	           		  + "  where seq 		= ? 									\n";
	           	
	           	pstmt1 = connMgr.prepareStatement(sql1);
	            
	            pstmt1.setString(1, v_f_year);
	           	pstmt1.setString(2, v_f_month);	
	           	pstmt1.setString(3, v_f_date);
	           	pstmt1.setString(4, v_content);   
	           	pstmt1.setString(5, s_userid);
	           	pstmt1.setString(6, v_titlecd);  
	           	pstmt1.setString(7, v_flag);
	           	pstmt1.setString(8, v_gubun);
	           	pstmt1.setString(9, v_gadmin);
	           	pstmt1.setInt(10, v_seq);
	            	                 
	           	isOk1 = pstmt1.executeUpdate();
            }
        }                        
        catch (Exception ex) {   
            if(connMgr != null) { try { connMgr.rollback(); }catch (Exception e10) {} }  
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
        }
        finally {
            if(connMgr != null) { try { connMgr.setAutoCommit(true); }catch (Exception e10) {} }            
            if(ls != null) { try { ls.close(); } catch (Exception e) {} }
            if(pstmt1 != null) { try { pstmt1.close(); } catch (Exception e1) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }            
        }
        return isOk1;
	}

  /********************
   * 스케줄 delete
   ********************/
   public int deleteSchedule(RequestBox box) throws Exception {

        DBConnectionManager connMgr = null;
        ListSet ls = null;
        PreparedStatement pstmt1 = null;   
        String sql1  = "";
        int isOk1 = 1;
        int v_seq = StringManager.toInt(box.getString("p_seq"));
		          
        try {
            connMgr = new DBConnectionManager();          
            connMgr.setAutoCommit(false);

			 // 스케줄 삭제 ------------------------------------------------
           	sql1  = " delete tz_schedule " ;
           	sql1 += "  where seq 		= ? ";
           	
           	pstmt1 = connMgr.prepareStatement(sql1);
            
            pstmt1.setInt(1, v_seq);
            	                 
           	isOk1 = pstmt1.executeUpdate(); 
        }                        
        catch (Exception ex) {   
            if(connMgr != null) { try { connMgr.rollback(); }catch (Exception e10) {} }  
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
        }
        finally {
            if(connMgr != null) { try { connMgr.setAutoCommit(true); }catch (Exception e10) {} }            
            if(ls != null) { try { ls.close(); } catch (Exception e) {} }
            if(pstmt1 != null) { try { pstmt1.close(); } catch (Exception e1) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }            
        }
        return isOk1;
	}

  /********************
   * 스케줄 년도 select
   ********************/
   public DataBox selectYear(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        DataBox dbox    = null;
        ArrayList list  = null;

        try {
            connMgr = new DBConnectionManager();         
 
            list = new ArrayList();
            
            		sql  = "select min(year) min_year, max(year) max_year ";
            		sql += "  from (select f_year year from tz_schedule where 1=1 ";
            		sql += ") a";
            
						ls = connMgr.executeQuery(sql);

            if(ls.next()){
            		dbox = ls.getDataBox();
            }
            
            if(dbox.getString("d_min_year").equals("")){
            		FormatDate fdt = new FormatDate();
            		Calendar now = new GregorianCalendar(); 
   					String v_year 	= fdt.getDate("yyyy"); 
            		dbox.put("d_min_year", v_year);
            		dbox.put("d_max_year", v_year);
            }			
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) {try {ls.close();} catch(Exception e){}}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return dbox;
    }  
    	
  /********************
   * 스케줄  select
   ********************/
   public DataBox selectScheduleUpdate(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        DataBox dbox    = null;
        
        int	   v_seq 		= StringManager.toInt(box.getString("p_seq"));

        try {
            connMgr = new DBConnectionManager();         
 
            sql  = "select f_year || f_month || f_date fdate, content, titlecd, flag, gubun, gadmin \n"
            	 + "from tz_schedule 																\n"
            	 + "where seq=" + SQLString.Format(v_seq) + "                               		\n";

			
       		ls = connMgr.executeQuery(sql);
       	    if(ls.next()) {
       	       	dbox = ls.getDataBox();
       	    }    
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) {try {ls.close();} catch(Exception e){}}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return dbox;
    }
   
    // 코드 값 불러오기
   	public ArrayList getCodeData(RequestBox box, String gubunCode) throws Exception { 
       DBConnectionManager connMgr = null;
       ListSet             ls      = null;
       ArrayList           list    = null;
       String              sql     = "";
       DataBox             dbox    = null;

       try { 
           connMgr = new DBConnectionManager();
           list = new ArrayList();

           sql  = "SELECT                                                   \n" +
                  "    code,                                                \n" +
                  "    codenm                                               \n" +
                  "FROM                                                     \n" +
                  "    tz_code                                              \n" +
                  "WHERE                                                    \n" +
                  "    gubun  = " + StringManager.makeSQL(gubunCode) + "    \n" +
                  "order by                                                 \n" +
                  "    code asc                                             \n" ;

           ls = connMgr.executeQuery(sql);

           while ( ls.next() ) { 
        	   dbox = ls.getDataBox();
        	   list.add(dbox);
           }
       } catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex, box, sql);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
       } finally { 
           if ( ls != null      ) { try { ls.close();               } catch ( Exception e )   { } }
           if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
       }

       return list;
   	}    
   	
   	// 과정코드, 과정명 불러오기
   	public ArrayList getSubj(RequestBox box) throws Exception { 
       DBConnectionManager connMgr = null;
       ListSet             ls      = null;
       ArrayList           list    = null;
       String              sql     = "";
       DataBox             dbox    = null;

       try { 
           connMgr = new DBConnectionManager();
           list = new ArrayList();

           sql  = "SELECT                                                   \n" +
                  "    subj,                                                \n" +
                  "    subjnm                                               \n" +
                  "FROM                                                     \n" +
                  "    tz_subj                                              \n" +
                  "order by                                                 \n" +
                  "    subjnm                                               \n" ;

           ls = connMgr.executeQuery(sql);

           while ( ls.next() ) { 
        	   dbox = ls.getDataBox();
        	   list.add(dbox);
           }
       } catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex, box, sql);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
       } finally { 
           if ( ls != null      ) { try { ls.close();               } catch ( Exception e )   { } }
           if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
       }

       return list;
   	}    
}
