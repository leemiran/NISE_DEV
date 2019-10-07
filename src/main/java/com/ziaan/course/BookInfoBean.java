//**********************************************************
//  1. 제      목: 도서정보 관리
//  2. 프로그램명: BookInfoBean.java
//  3. 개      요:
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 
//  7. 수      정: 
//**********************************************************
package com.ziaan.course;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class BookInfoBean {
	private ConfigSet config;
    private int row;
		
    public BookInfoBean() { 
    	try { 
    		config = new ConfigSet();
    		row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        이 모듈의 페이지당 row 수를 셋팅한다
    	}
    	catch( Exception e ) { 
    		e.printStackTrace();
    	}
	}

	/**
	도서정보 조회
	@param box          receive from the form object and session
	@return ArrayList   도서정보 조회
	*/      
	public ArrayList SelectBookInfoList(RequestBox box) throws Exception {               
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		ArrayList list = null;
		String sql  = "";
		DataBox dbox = null;
		int v_pageno = box.getInt("p_pageno");

		String ss_bookname = box.getString("s_bookname");     
		String  v_orderColumn = box.getString("p_orderColumn");           // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서

		try {
			sql = "select bookcode, bookname, author, publisher, price, ldate, indate, stock	\n"
				+ "from   tz_bookinfo															\n"
				+ "where  1=1																	\n";        

			if (!ss_bookname.equals(""))
				sql+= "and    bookname like " + SQLString.Format("%" + ss_bookname + "%") + "	\n";

			if ( v_orderColumn.equals("") ) { 
                sql += "order  by indate desc							\n";
            } else { 
                sql += "order  by " + v_orderColumn + v_orderType+ "	\n";
            }
			
			connMgr = new DBConnectionManager();
			list = new ArrayList();
			ls = connMgr.executeQuery(sql);
			ls.setPageSize(row);                       //  페이지당 row 갯수를 세팅한다
			ls.setCurrentPage(v_pageno);               //     현재페이지번호를 세팅한다.
			int totalpagecount = ls.getTotalPage();    //     전체 페이지 수를 반환한다
			int totalrowcount = ls.getTotalCount();    //     전체 row 수를 반환한다

            
			while (ls.next()) {
				dbox = ls.getDataBox();
				dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum()+1));
				dbox.put("d_totalpage", new Integer(totalpagecount));
				dbox.put("d_rowcount", new Integer(row));
				list.add(dbox);
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
		return list;
	}
    

	/**
	도서정보 조회
	@param box          receive from the form object and session
	@return CourseData   도서정보 조회
	*/      
	public DataBox SelectBookInfoData(RequestBox box) throws Exception {               
		DataBox dbox = null;
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		String sql  = "";
		String ss_bookcode = box.getString("p_bookcode");
		String v_bunru_seq = box.getString("p_bunru_seq");	
		try {
			sql = "\n select relativebookcode, a.comp, b.compnm						"
				+ "\n      , bookcode, bookname, author, publisher					"
				+ "\n      , price, a.ldate, indate, stock							"
				+ "\n      , core_contents, book_contents, savefile, realfile		"
				+ "\n      , a.refbook1, a.refbook2, a.bookimgurl					"
				+ "\n      , (select title  										"
				+ "\n         from   tz_resources_info								"
				+ "\n         where  bunru_seq = " + SQLString.Format(v_bunru_seq)
				+ "\n         and    seq = a.refbook1) as refbook_nm1				"
				+ "\n      , (select title  										"
				+ "\n         from   tz_resources_info								"
				+ "\n         where  bunru_seq = " + SQLString.Format(v_bunru_seq)
				+ "\n         and    seq = a.refbook2) as refbook_nm2				"
				+ "\n      , (														"
				+ "\n 			select case when count(*) > 0 then 'N' else 'Y' end	"
				+ "\n 			from tz_proposebook									"
				+ "\n 			where bookcode = a.bookcode							"
				+ "\n 		  ) delyn												"					
				+ "\n from   tz_bookinfo a											"
				+ "\n 		, (	select comp, compnm									" 
				+ "\n 			from   tz_compclass									"
				+ "\n 			union												"
				+ "\n 			select cpseq, cpnm									"
				+ "\n 			from   tz_cpinfo ) b 								"
				+ "\n where  a.comp = b.comp(+)										"        
				+ "\n and    a.bookcode = " + SQLString.Format(ss_bookcode);        

			
			connMgr = new DBConnectionManager();
			ls = connMgr.executeQuery(sql);
            
			if (ls.next()) {
				dbox = ls.getDataBox();
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
		return dbox;
	}
    
	/**
	도서정보 등록
	@param box      receive from the form object and session
	@return isOk    1:insert success,0:insert fail
	*/      	    
	 public int Insert(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;        
		PreparedStatement pstmt = null;        
		String sql = "";
		String sql2 = "";
		ListSet ls2 = null;
		int isOk = 0;  
		int v_code = 0;
		int v_bookcode = 0;

		String ss_userid = box.getSession("userid");
		
		String v_bookname			= box.getString("p_bookname");
		String v_author 			= box.getString("p_author");
		String v_publisher 			= box.getString("p_publisher");
		String v_core_contents 		= box.getString("p_core_contents");
		String v_book_contents 		= box.getString("p_book_contents");
        String v_realfile 			= box.getRealFileName("p_bookimage");
        String v_savefile 			= box.getNewFileName ("p_bookimage");
        String v_relativebookcode 	= box.getString("p_relativebookcode");
        String v_comp 				= box.getString("p_comp");        
        String v_refbook1 			= box.getString("p_refbook1");        
        String v_refbook2 			= box.getString("p_refbook2");   
        String v_bookimgurl         = box.getString("p_bookimgurl");
        int v_price = box.getInt("p_price");
        int v_stock = box.getInt("p_stock");
        
        try {
			connMgr = new DBConnectionManager();    
			connMgr.setAutoCommit(false);
			
			sql2 = "select nvl(max(bookcode), 0) from tz_bookinfo";
			ls2 = connMgr.executeQuery(sql2);
			ls2.next();
			v_bookcode = ls2.getInt(1) + 1;
			ls2.close();

			sql = "\n insert into tz_bookinfo( "
				+ "\n        bookcode "
				+ "\n      , bookname "
				+ "\n      , author "
				+ "\n      , publisher "
				+ "\n      , price "
				+ "\n      , ldate "
				+ "\n      , indate "
				+ "\n      , luserid "
				+ "\n      , inuserid "
				+ "\n      , stock "
				+ "\n      , core_contents "
				+ "\n      , book_contents "
				+ "\n      , realfile "
				+ "\n      , savefile "
				+ "\n      , comp "
				+ "\n      , relativebookcode "
				+ "\n      , refbook1 "
				+ "\n      , refbook2 "
				+ "\n      , bookimgurl "
				+ "\n ) "
				+ "\n values ( "
				+ "\n        ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , to_char(sysdate,'yyyymmddhh24miss') "
				+ "\n      , to_char(sysdate,'yyyymmddhh24miss') "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
//				+ "\n      , empty_clob() "
//				+ "\n      , empty_clob() "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n ) ";

			int param = 0;
			pstmt = connMgr.prepareStatement(sql);
			pstmt.setInt   (++param, v_bookcode);
			pstmt.setString(++param, v_bookname);
			pstmt.setString(++param, v_author);
			pstmt.setString(++param, v_publisher);
			pstmt.setInt   (++param, v_price);
			pstmt.setString(++param, ss_userid);
			pstmt.setString(++param, ss_userid);
			pstmt.setInt   (++param, v_stock);
			pstmt.setString(++param, v_core_contents);
			pstmt.setString(++param, v_book_contents);
			pstmt.setString(++param, v_realfile);
			pstmt.setString(++param, v_savefile);
			pstmt.setString(++param, v_comp);
			pstmt.setString(++param, v_relativebookcode);
			pstmt.setString(++param, v_refbook1);
			pstmt.setString(++param, v_refbook2);
			pstmt.setString(++param, v_bookimgurl);
			
			isOk = pstmt.executeUpdate();
			sql = "select book_contents from tz_bookinfo where bookcode = " + v_bookcode;
//            connMgr.setOracleCLOB(sql, v_book_contents);    
            sql = "select core_contents from tz_bookinfo where bookcode = " + v_bookcode;
//            connMgr.setOracleCLOB(sql, v_core_contents);    
			
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			
			if(v_stock > 0) {
				 sql2 = "select nvl(max(code), 0) from tz_bookinfo_history";
				 ls2 = connMgr.executeQuery(sql2);
	             ls2.next();
	             v_code = ls2.getInt(1) + 1;
	             ls2.close();
	                
				sql =  " insert into tz_bookinfo_history(code, bookcode, ldate, luserid, stock) ";
				sql+=  " values (?, ?, to_char(sysdate,'yyyymmddhh24miss'), ?, ?)";
				pstmt = connMgr.prepareStatement(sql);
	
				pstmt.setInt(1, v_code);
				pstmt.setInt(2, v_bookcode);
				pstmt.setString(3, ss_userid);
				pstmt.setInt(4, v_stock);
				
				isOk = pstmt.executeUpdate();
				if ( pstmt != null ) { pstmt.close(); }
			}
		}
		catch(Exception ex) {
			isOk = 0;
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if (isOk > 0) {connMgr.commit();} else {connMgr.rollback();}
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return isOk;
	}	
    
	/**
	도서정보 수정
	@param box      receive from the form object and session
	@return isOk    1:update success,0:update fail    
	*/      	    
	 public int Update(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;        
		ListSet	ls = null;
		PreparedStatement pstmt = null;        
		String sql = "";
		String sql2 = "";
		ListSet ls2 = null;
		int isOk = 0;   
		
		int v_code = 0;
		int v_bookcode = box.getInt("p_bookcode");
		String v_bookname= box.getString("p_bookname");
		String  v_author = box.getString("p_author");
		String v_publisher = box.getString("p_publisher");
		int v_price = box.getInt("p_price");
		String ss_userid = box.getSession("userid");
		int v_stock = box.getInt("p_stock");
		String v_core_contents = box.getString("p_core_contents");
		String v_book_contents = box.getString("p_book_contents");
        String v_realfile = box.getRealFileName("p_bookimage");
        String v_savefile = box.getNewFileName ("p_bookimage");
        String v_file = box.getStringDefault("p_file","0");
        String v_comp = box.getString("p_comp");
        String v_relativebookcode = box.getString("p_relativebookcode");
        String v_refbook1 = box.getString("p_refbook1");
        String v_refbook2 = box.getString("p_refbook2");
        String v_bookimgurl = box.getString("p_bookimgurl");
        
        if ( v_savefile.length() == 0 ) { 
            if ( v_file.equals("1") ) { 
            	v_realfile    		= "";
            	v_savefile     		= "";
            } else {
            	v_realfile = box.getString("p_realfile");
            	v_savefile = box.getString("p_savefile");
            }
        }
        
		try {
			connMgr = new DBConnectionManager();                             
			connMgr.setAutoCommit(false);  
                        
			//insert TZ_COURSE table
			sql = "update tz_bookinfo 			\n"
				+ "   set comp = ? 				\n"
				+ "     , relativebookcode = ?	\n"
				+ "     , bookname = ?			\n"
				+ "     , author = ? 			\n"
				+ "     , publisher = ? 		\n"
				+ "     , price = ? 			\n"
				+ "     , luserid= ? 			\n"
				+ "     , ldate = to_char(sysdate,'yyyymmddhh24miss') 		\n"
				+ "     , core_contents = ?	\n"
				+ "     , book_contents = ?	\n"
//				+ "     , core_contents = empty_clob()	\n"
//				+ "     , book_contents = empty_clob()	\n"
				+ "     , refbook1 = ?			\n"
				+ "     , refbook2 = ?			\n"
			    + "     , bookimgurl = ?			\n";
			if(v_stock > 0) {
				sql += "     , stock = stock + ? 		\n";
			}	
			if(v_savefile!=null) {
				sql += "     , realfile = ?				\n";
				sql += "     , savefile = ? 			\n";
			}
			
			sql+= " where bookcode = ? 			\n";

			pstmt = connMgr.prepareStatement(sql);
			
			int param = 0;
			pstmt.setString(++param, v_comp);
			pstmt.setString(++param, v_relativebookcode);
			pstmt.setString(++param, v_bookname);
			pstmt.setString(++param, v_author);
			pstmt.setString(++param, v_publisher);
			pstmt.setInt(++param, v_price);
			pstmt.setString(++param, ss_userid);
			pstmt.setString(++param, v_core_contents);
			pstmt.setString(++param, v_book_contents);
			pstmt.setString(++param, v_refbook1);
			pstmt.setString(++param, v_refbook2);
			pstmt.setString(++param, v_bookimgurl);
			if(v_stock > 0) {
				pstmt.setInt   (++param, v_stock);
			}
			if(v_savefile!=null) {
				pstmt.setString(++param, v_realfile);
				pstmt.setString(++param, v_savefile);
			}
			pstmt.setInt(++param, v_bookcode);
			
			isOk = pstmt.executeUpdate();
			if ( pstmt != null ) { pstmt.close(); }
			sql = "select book_contents from tz_bookinfo where bookcode = " + v_bookcode;
//            connMgr.setOracleCLOB(sql, v_book_contents);    
            sql = "select core_contents from tz_bookinfo where bookcode = " + v_bookcode;
//            connMgr.setOracleCLOB(sql, v_core_contents);    
			
			if(v_stock > 0) {
				sql2 = "select nvl(max(code), 0) from tz_bookinfo_history";
				ls2 = connMgr.executeQuery(sql2);
	            ls2.next();
	            v_code = ls2.getInt(1) + 1;
	            ls2.close();
	               
				sql =  "insert into tz_bookinfo_history(code, bookcode, ldate, luserid, stock) ";
				sql+=  " values (?, ?, to_char(sysdate,'yyyymmddhh24miss'), ?, ?)";
				pstmt = connMgr.prepareStatement(sql);
	
				pstmt.setInt(1, v_code);
				pstmt.setInt(2, v_bookcode);
				pstmt.setString(3, ss_userid);
				pstmt.setInt(4, v_stock);
				
				isOk = pstmt.executeUpdate();
				if ( pstmt != null ) { pstmt.close(); }
			}
		}
		catch(Exception ex) {
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if (isOk > 0) {connMgr.commit();} else {connMgr.rollback();}
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if(ls != null) { try { ls.close(); } catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return isOk;
	}	

	/**
	도서정보 삭제
	@param box      receive from the form object and session
	@return isOk    1:delete success,0:delete fail    
	*/      	    
	public int Delete(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;        
		PreparedStatement pstmt = null;        
		String sql = "";
		int    isOk = 0;   

		int v_bookcode = box.getInt("p_bookcode");
		
		try {
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);  
			
			// 수불이력 테이블 삭제
			sql = "delete from tz_bookinfo_history where bookcode = ? ";
			pstmt = connMgr.prepareStatement(sql);
			pstmt.setInt(1, v_bookcode);
			isOk = pstmt.executeUpdate();
			if ( pstmt != null ) { pstmt.close(); }

			// 과정별 개월차 도서정보 테이블 삭제
			sql = "delete from tz_subjbook where bookcode = ? ";
			pstmt = connMgr.prepareStatement(sql);
			pstmt.setInt(1, v_bookcode);
			isOk = pstmt.executeUpdate();
			if ( pstmt != null ) { pstmt.close(); }
			
			// 도서정보 테이블 삭제
			sql = "delete from tz_bookinfo where bookcode = ? ";
			pstmt = connMgr.prepareStatement(sql);
			pstmt.setInt(1, v_bookcode);
			isOk = pstmt.executeUpdate(); 
			if ( pstmt != null ) { pstmt.close(); }

			if(isOk > 0) connMgr.commit();        	 	
			else connMgr.rollback();
		}
		catch (Exception ex) {
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }
			if(connMgr != null) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return isOk;
	}	
	
	/**
	교재수불이력
	@param box          receive from the form object and session
	@return ArrayList   교재수불이력
	*/      
	public ArrayList SelectBookInfoHistoryList(RequestBox box) throws Exception {               
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		ArrayList list = null;
		String sql  = "";
		DataBox dbox = null;
		String v_bookcode = box.getString("p_bookcode");

		try {
			sql = "select a.code, a.bookcode, a.ldate, a.luserid, a.stock, c.name, b.bookname			\n"
				+ "from   tz_bookinfo_history a, tz_bookinfo b, tz_member c								\n"
				+ "where  a.bookcode = b.bookcode														\n"
				+ "and    a.luserid  = c.userid															\n"     
			    + "and    a.bookcode = " + StringManager.makeSQL(v_bookcode) + "                        \n";

			sql+= " order by a.ldate desc ";

			connMgr = new DBConnectionManager();
			list = new ArrayList();
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
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return list;
	}  

	/**
	수강신청한 교재 정보
	@param box          receive from the form object and session
	@return ArrayList   수강신청한 교재 정보
	*/      
	public ArrayList selectProposeBook(RequestBox box) throws Exception {               
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		ArrayList list = null;
		String sql  = "";
		DataBox dbox = null;
		String ss_userid = box.getStringDefault("p_userid", box.getSession("userid"));
		String v_subj = box.getString("p_subj");
		String v_year = box.getString("p_year");
		String v_subjseq = box.getString("p_subjseq");
		
		try {
			sql = "select a.month, a.bookcode, b.bookname							\n"
				+ "     , b.refbook1, b.refbook2									\n"
				+ "     , a.waybill_no, a.delivery_comp, a.delivery_tel 			\n"
				+ "     , a.delivery_url								 			\n"
				+ "     , a.status										 			\n"
				+ "     , a.shipment_date, a.delivery_date				 			\n"
                + "     , decode((select count(*) 									\n"
                + "        from   tz_bookexameach_result 							\n"
                + "        where  subj = d.subj 									\n"
                + "        and    year = d.year 									\n"
                + "        and    subjseq = d.subjseq 								\n"
                + "        and    month = d.month 									\n"
                + "        and    bookcode = d.bookcode 							\n"
                + "        and    userid = d.userid									\n"
                + "        and    choice_answer is not null							\n"
                + "        and    choice_answer != '0'),0,'N','Y') as exam_yn 		\n"
                + "     , nvl(d.final_yn,'N') as final_yn 							\n"
                + "     , nvl(d.marking_yn,'N') as marking_yn 						\n"
                + "     , nvl(d.totalscore,0) as totalscore 						\n"
                + "     , c.examstart, c.examend 									\n"
                + "     , case when to_char(sysdate, 'yyyymmdd') between c.examstart and c.examend then 'Y'  \n"
                + "            else 'N'  											\n"
                + "       end as isperiod										 	\n"
				+ "from   tz_proposebook a, tz_bookinfo b							\n"
				+ "     , tz_bookexam_paper c, tz_bookexam_result d					\n"
				+ "where  a.bookcode = b.bookcode									\n"
				+ "and    a.subj = c.subj(+)										\n"
				+ "and    a.year = c.year(+)										\n"
				+ "and    a.subjseq = c.subjseq(+)									\n"
				+ "and    a.month = c.month(+)										\n"
				+ "and    a.bookcode = c.bookcode(+)								\n"
				+ "and    a.subj = d.subj(+)										\n"
				+ "and    a.year = d.year(+)										\n"
				+ "and    a.subjseq = d.subjseq(+)									\n"
				+ "and    a.month = d.month(+)										\n"
				+ "and    a.bookcode = d.bookcode(+)								\n"
				+ "and    a.userid = d.userid(+)									\n"
				+ "and    a.userid  = " + StringManager.makeSQL(ss_userid) + "		\n"
				+ "and    a.subj    = " + StringManager.makeSQL(v_subj) + "			\n"
				+ "and    a.year    = " + StringManager.makeSQL(v_year) + "			\n"
				+ "and    a.subjseq = " + StringManager.makeSQL(v_subjseq) + "		\n";
			
			connMgr = new DBConnectionManager();
			list = new ArrayList();
			ls = connMgr.executeQuery(sql);
		
            while (ls.next()) {
				dbox = ls.getDataBox();
				dbox.put("d_totalscore"	, new Double( ls.getDouble("totalscore")));
				list.add(dbox);
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
		return list;
	}
	
	/**
	도서배송관리
	@param box          receive from the form object and session
	@return ArrayList   도서배송관리
	*/      
	public ArrayList SelectDeliveryList(RequestBox box) throws Exception {               
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		ArrayList list = null;
		String sql  = "";
		DataBox dbox = null;
		
		String  ss_bookname = box.getString("s_bookname");     
		String  v_order = box.getString("p_order");           // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서
        
        String ss_userid = box.getSession("userid");
        String ss_status = box.getStringDefault("s_status", "ALL");
        
        
        String  ss_grcode   = box.getString("s_grcode");    // 교육그룹
        String  ss_gyear    = box.getString("s_gyear");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수
        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과정분류(대분류)
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");   // 과정분류(중분류)
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과정분류(소분류)
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  v_search    = box.getString("p_search");    // 검색어
        

		try {
			sql = "select a.subj																	\n"
				+ "     , a.year																	\n"
				+ "     , a.subjseq																	\n"
				+ "     , a.subjnm																	\n"
				+ "     , d.bookname																\n"
				+ "     , d.bookcode																\n"
				+ "     , c.month                                                                   \n"
				+ "     , b.userid																	\n"
				+ "     , get_name(b.userid) as name												\n"
				+ "     , c.status																	\n"
				+ "     , c.shipment_date															\n"
				+ "     , c.delivery_date															\n"
				+ "     , nvl((                                                                		\n"
	            + "            select cpnm                                                        	\n"
	            + "            from   tz_cpinfo                                                   	\n"
	            + "            where  cpseq = a.owner                                             	\n"
	            + "           ), (                                                               	\n"
	            + "               select compnm		                                            	\n"
	            + "               from   tz_compclass                                            	\n"
	            + "               where  comp = a.owner                                          	\n"
	            + "              ))         ownernm                                             	\n"
	            + "     , get_compnm(b.comp) compnm                                        			\n"
	            + "     , get_postnm(b.jik) as jikwinm												\n"
	            + "     , b.ldate																	\n"
	            + "     , c.waybill_no																\n"
				+ "from   vz_scsubjseq a, tz_propose b, tz_proposebook c							\n"
				+ "     , tz_bookinfo d																\n"
				+ "where  a.subj    = b.subj														\n"
				+ "and    a.year    = b.year														\n"
				+ "and    a.subjseq = b.subjseq														\n"
				+ "and    a.isonoff = 'RC'															\n"
				+ "and    b.subj    = c.subj														\n"
				+ "and    b.year    = c.year														\n"
				+ "and    b.subjseq = c.subjseq														\n"
				+ "and    b.userid  = c.userid														\n"
				+ "and    c.bookcode= d.bookcode													\n";
			
			
			if(!ss_status.equals("ALL")) 
				sql+= "and    c.status = " + StringManager.makeSQL(ss_status) + "                   \n";
			
			if (!ss_bookname.equals(""))
				sql+= "and    bookname like " + SQLString.Format("%" + ss_bookname + "%") + "		\n";

			if ( !v_search.equals("") ) { 
                sql+= "and    (b.userid like " +SQLString.Format("%"+v_search+"%") + " or get_name(b.userid) like " +SQLString.Format("%"+v_search+"%") + ")\n";
			}

			if ( !ss_grcode.equals("ALL"))     sql += "and    a.grcode = " +SQLString.Format(ss_grcode) + "	\n";
			if ( !ss_gyear.equals("ALL"))      sql += "and    a.gyear = " +SQLString.Format(ss_gyear) + "	\n";
			if ( !ss_grseq.equals("ALL"))      sql += "and    a.grseq = " +SQLString.Format(ss_grseq) + "	\n";
              
			if ( !ss_uclass.equals("ALL"))     sql += "and    a.scupperclass = " +SQLString.Format(ss_uclass) + "	\n";
			if ( !ss_mclass.equals("ALL"))     sql += "and    a.scmiddleclass = " +SQLString.Format(ss_mclass) + "	\n";
			if ( !ss_lclass.equals("ALL"))     sql += "and    a.sclowerclass = " +SQLString.Format(ss_lclass) + "	\n";
              
			if ( !ss_subjcourse.equals("ALL")) sql += "and    a.subj = " +SQLString.Format(ss_subjcourse) + "	\n";
			if ( !ss_subjseq.equals("ALL"))    sql += "and    a.subjseq = " +SQLString.Format(ss_subjseq) + "	\n";
              
			if ( v_order.equals("") ) { 
                sql += "order  by substr(b.ldate,0,8), a.subjnm, a.year, a.subjseq, c.month, d.bookname, get_name(b.userid) 	\n";
            } else { 
                sql += "order  by " + v_order + v_orderType + "				\n";
            }

			connMgr = new DBConnectionManager();
			list = new ArrayList();
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
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return list;
	}
	
	/**
	도서 배송지 정보
	@param box          receive from the form object and session
	@return CourseData   도서 배송지 정보
	*/      
	public DataBox selectDeliveryInfo(RequestBox box) throws Exception {               
		DataBox dbox = null;
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		String sql  = "";
		String v_subj = box.getString("p_subj");
		String v_year = box.getString("p_year");
		String v_subjseq = box.getString("p_subjseq");
		String v_userid = box.getString("p_userid");
		int v_month = box.getInt("p_month");
		int v_bookcode = box.getInt("p_bookcode");
	
		try {
			sql = "select e.subjnm, b.bookname, c.name					\n"
				+ "     , c.userid, d.delivery_address1					\n"
				+ "		, d.delivery_address2, d.delivery_handphone		\n"
				+ "		, d.delivery_post1, d.delivery_post2, a.subj	\n"
				+ "     , a.year, a.subjseq, a.month, a.bookcode		\n"
				+ "     , a.note										\n"
				+ "     , a.waybill_no									\n"
				+ "     , a.delivery_comp								\n"
				+ "     , a.delivery_tel								\n"
				+ "     , a.delivery_url								\n"
				+ "from   tz_proposebook a, tz_bookinfo b, tz_member c	\n"
				+ "     , tz_delivery d, vz_scsubjseq e					\n"
				+ "where  a.bookcode = b.bookcode						\n"
				+ "and    a.userid = c.userid							\n"
				+ "and    a.subj = d.subj                               \n"
				+ "and    a.year = d.year                               \n"
				+ "and    a.subjseq = d.subjseq                         \n"
				+ "and    c.userid = d.userid                           \n"
				+ "and    a.subj = e.subj								\n"
				+ "and    a.year= e.year								\n"
				+ "and    a.subjseq = e.subjseq							\n"	
				+ "and    a.subj = " + SQLString.Format(v_subj) + "  	\n"
				+ "and    a.year= " + SQLString.Format(v_year) + "  	\n"
				+ "and    a.subjseq = " + SQLString.Format(v_subjseq) + " 	\n"
				+ "and    a.userid = " + SQLString.Format(v_userid) + "  	\n"
				+ "and    a.month = " + SQLString.Format(v_month) + "  		\n"
				+ "and    a.bookcode = " + SQLString.Format(v_bookcode) + "	\n";    
			
			

			connMgr = new DBConnectionManager();
			ls = connMgr.executeQuery(sql);
            
			if (ls.next()) {
				dbox = ls.getDataBox();
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
		return dbox;
	}
	
	/**
	상태값 변경
	@param box      receive from the form object and session
	@return isOk    1:update success,0:update fail    
	*/      	    
	 public int UpdateStatus(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;        
		ListSet	ls = null;
		ListSet	ls2 = null;
		PreparedStatement pstmt = null;        
		String sql = "";
		String sql2 = "";
		int isOk = 0;   
		
		String v_status = box.getString("p_status");
		String v_date = box.getStringDefault("p_date",FormatDate.getDate("yyyyMMdd")).replaceAll("\\.","");
		String v_gubun = box.getString("p_gubun");
		Vector v_checks = box.getVector("p_checks");
		String v_param = "";
		String v_subj = "";
		String v_year = "";
		String v_subjseq = "";
		String v_userid = "";
		String v_month = "";
		String v_bookcode = "";
		String v_origin_status = "";
		
		int    v_code = 0;
		
		try {
			connMgr = new DBConnectionManager();    
			
			for(int i = 0; i < v_checks.size(); i++) {
                v_param     = v_checks.elementAt(i).toString();    
                
				StringTokenizer arr_value = new StringTokenizer(v_param, ",");
				
				v_userid	= arr_value.nextToken();
				v_subj	= arr_value.nextToken();
				v_year		= arr_value.nextToken();
				v_subjseq	= arr_value.nextToken();
				v_month	= arr_value.nextToken();
				v_bookcode	= arr_value.nextToken();
				v_origin_status	= arr_value.nextToken();
				
				sql = "update tz_proposebook set status = ?, " + v_gubun + " = ? 			\n"
					+ "where  subj = ?														\n"
					+ "and    year = ?														\n"
					+ "and    subjseq= ?													\n"
					+ "and    userid = ?													\n"
					+ "and    month = ?														\n"
					+ "and    bookcode = ?													\n";
				if("D".equals(v_status)) {
					sql += "and    status = 'W'												\n";
				}
	
				pstmt = connMgr.prepareStatement(sql);
				
				int param = 0;
				pstmt.setString(++param, v_status);
				pstmt.setString(++param, v_date);
				pstmt.setString(++param, v_subj);
				pstmt.setString(++param, v_year);
				pstmt.setString(++param, v_subjseq);
				pstmt.setString(++param, v_userid);
				pstmt.setInt(++param, Integer.parseInt(v_month));
				pstmt.setInt(++param, Integer.parseInt(v_bookcode));
				
				isOk = pstmt.executeUpdate();
				
				if ( pstmt != null ) { pstmt.close(); }
				
				if(isOk > 0 && "D".equals(v_status)) {
					sql = "update tz_bookinfo set stock = stock - 1                             \n"
						+ "where  bookcode = ?													\n";
					
					param = 0;
					pstmt = connMgr.prepareStatement(sql);
					pstmt.setInt(++param, Integer.parseInt(v_bookcode));
					
					isOk = pstmt.executeUpdate();
					if ( pstmt != null ) { pstmt.close(); }
					sql2 = "select nvl(max(code), 0) from tz_bookinfo_history";
					ls2 = connMgr.executeQuery(sql2);
		            ls2.next();
		            v_code = ls2.getInt(1) + 1;
		            ls2.close();
					
					sql =  "insert into tz_bookinfo_history(code, bookcode, ldate, luserid, stock) ";
					sql+=  " values (?, ?, to_char(sysdate,'yyyymmddhh24miss'), ?, ?)";
					pstmt = connMgr.prepareStatement(sql);
		
					pstmt.setInt(1, v_code);
					pstmt.setInt(2, Integer.parseInt(v_bookcode)); 
					pstmt.setString(3, v_userid);
					pstmt.setInt(4, -1);
					
					isOk = pstmt.executeUpdate();
					if ( pstmt != null ) { pstmt.close(); }
				}
			}			
		}
		catch(Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if(ls != null) { try { ls.close(); } catch (Exception e) {} }
		}
		return isOk;
	}

	/**
	비고 변경
	@param box      receive from the form object and session
	@return isOk    1:update success,0:update fail    
	*/      	    
	 public int UpdateNote(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;        
		ListSet	ls = null;
		PreparedStatement pstmt = null;        
		String sql = "";
		int isOk = 0;   
		
		String v_subj = box.getString("p_subj");
		String v_year = box.getString("p_year");
		String v_subjseq = box.getString("p_subjseq");
		String v_userid = box.getString("p_userid");
		int v_month = box.getInt("p_month");
		int v_bookcode = box.getInt("p_bookcode");
		String v_note = box.getString("p_note");
		String v_waybill_no = box.getString("p_waybill_no");
		String v_delivery_comp = box.getString("p_delivery_comp");
		String v_delivery_tel = box.getString("p_delivery_tel");
		String v_delivery_url = box.getString("p_delivery_url");
		
		try {
			connMgr = new DBConnectionManager();                             
		
			sql = "update tz_proposebook 	\n"
				+ "set    waybill_no = ?	\n"
				+ "     , delivery_comp	= ?	\n"
				+ "     , delivery_tel = ?	\n"
				+ "     , delivery_url = ?	\n"
				+ "     , note = ?			\n"
				+ "     , status = decode(status,'W',decode(sign(?),1,'D',status),status) \n"
				+ "     , delivery_date = decode(status,'W',decode(sign(?),1,to_char(sysdate,'yyyy-mm-dd'),delivery_date),delivery_date) \n"
				+ "where  subj = ?			\n"
				+ "and    year = ?			\n"
				+ "and    subjseq= ?		\n"
				+ "and    userid = ?		\n"
				+ "and    month = ?			\n"
				+ "and    bookcode = ?		\n";
			
			pstmt = connMgr.prepareStatement(sql);
			
			int param = 0;
			pstmt.setString(++param, v_waybill_no);
			pstmt.setString(++param, v_delivery_comp);
			pstmt.setString(++param, v_delivery_tel);
			pstmt.setString(++param, v_delivery_url);
			pstmt.setString(++param, v_note);
			pstmt.setString(++param, v_waybill_no);
			pstmt.setString(++param, v_waybill_no);
			pstmt.setString(++param, v_subj);
			pstmt.setString(++param, v_year);
			pstmt.setString(++param, v_subjseq);
			pstmt.setString(++param, v_userid);
			pstmt.setInt(++param, v_month);
			pstmt.setInt(++param, v_bookcode);
			
			isOk = pstmt.executeUpdate();
			if ( pstmt != null ) { pstmt.close(); }
		}
		catch(Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if(ls != null) { try { ls.close(); } catch (Exception e) {} }
		}
		return isOk;
	}
	 
	 /**
	도서코드와 회사코드가 겹치는 지 여부 체크
	@param box          receive from the form object and session
	@return ArrayList   
	*/      
	public boolean validationCheck(RequestBox box) throws Exception {               
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		String sql  = "";
		String v_relativebookcode = box.getString("p_relativebookcode");
		String v_comp = box.getString("p_comp");
		String v_bookcode = box.getString("p_bookcode");
		String v_gubun = box.getString("gubun");
		
		boolean v_status = false;
		
		try {
			sql = "select bookcode																\n"
				+ "from   tz_bookinfo															\n"
				+ "where  relativebookcode = " + StringManager.makeSQL(v_relativebookcode) + "	\n"
				+ "and    comp = " + StringManager.makeSQL(v_comp) + "							\n";
			if("update".equals(v_gubun))
				sql+= "and    bookcode <> " + StringManager.makeSQL(v_bookcode) + "             \n";
					
			connMgr = new DBConnectionManager();
			ls = connMgr.executeQuery(sql);
			v_status = ls.next(); 
		}            
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return v_status;
	}
	
	/**
	도서배송관리 엑셀출력
	@param box          receive from the form object and session
	@return ArrayList   도서배송관리 엑셀출력
	*/      
	public ArrayList SelectDeliveryExcelList(RequestBox box) throws Exception {               
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		ListSet ls2 = null;
		ArrayList list = null;
		ArrayList list2 = null;
		String sql  = "";
		DataBox dbox = null;
		DataBox dbox2 = null;		
		
		String  ss_bookname = box.getString("s_bookname");     
		String  v_order = box.getString("p_order");           // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서
        
        String ss_userid = box.getSession("userid");
        String ss_status = box.getStringDefault("s_status", "ALL");
        
        
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수
        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과정분류(대분류)
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");   // 과정분류(중분류)
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과정분류(소분류)
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  v_search = box.getString("p_search");    // 검색어

		try {
		
			sql= "\n select a.subj "
        	   + "\n       , a.year "																	
        	   + "\n       , a.subjseq "
        	   + "\n       , a.subj "
        	   + "\n       , a.subjnm "
        	   + "\n       , e.userid	"
        	   + "\n       , e.name "
        	   + "\n       , nvl(( "
        	   + "\n              select cpnm "
        	   + "\n              from   tz_cpinfo "
        	   + "\n              where  cpseq = a.owner "
        	   + "\n             ), ( "
        	   + "\n                 select compnm "
        	   + "\n                 from   tz_compclass "
        	   + "\n                 where  comp = a.owner "
        	   + "\n                ))         ownernm "
        	   + "\n       , f.delivery_post1 "
        	   + "\n       , f.delivery_post2 "														
        	   + "\n       , f.delivery_address1 "
        	   + "\n       , f.delivery_address2 "
        	   + "\n       , f.delivery_handphone "
        	   + "\n       , get_compnm(e.comp) as compnm "
        	   + "\n       , lvl_nm as jikwinm "
        	   + "\n       , a.edustart "
        	   + "\n       , a.eduend "
/*	           + "\n       , e.cono "
	           + "\n       , ( "
	           + "\n          select alias "
	           + "\n          from tz_compclass "
	           + "\n          where comp = e.comp "
	           + "\n         ) alias "*/
        	   + "\n       , e.email "
        	   + "\n       , e.handphone as comptel "
        	   + "\n       , position_nm as deptnm "
        	   + "\n       , g.biyong "
        	   + "\n       , g.isgoyong "
        	   + "\n       , g.goyongpricemajor "
        	   + "\n       , g.goyongpriceminor "
/*			   + "\n       , case when b.chkfinal = 'B' then '신청' "
        	   + "\n         when b.chkfinal ='Y' then '승인' "
        	   + "\n         when b.chkfinal ='N' then '반려' "
        	   + "\n         end  chkfinal "*/
        	   + "\n       , b.chkfinal "
        	   + "\n       , fn_crypt('2', e.birth_date, 'knise') birth_date "
        	   + "\n from    vz_scsubjseq a, tz_propose b, tz_member e, tz_delivery f, tz_subjseq g "
        	   + "\n where   a.subj = b.subj "
        	   + "\n and     a.year= b.year "
        	   + "\n and     a.subjseq = b.subjseq "
        	   + "\n and     a.isonoff = 'RC' "
        	   + "\n and     b.userid = e.userid "
        	   + "\n and     a.subj = f.subj "
        	   + "\n and     a.year = f.year "
        	   + "\n and     a.subjseq = f.subjseq "
        	   + "\n and     e.userid = f.userid "
        	   + "\n and     a.subj = g.subj "
        	   + "\n and     a.year = g.year "
        	   + "\n and     a.subjseq = g.subjseq ";
			
//			if(!ss_status.equals("ALL")) 
//				sql+= "\n and     c.status = " + StringManager.makeSQL(ss_status);
			
//			if (!ss_bookname.equals(""))
//				sql+= "\n and     bookname like " + SQLString.Format("%" + ss_bookname + "%");

			if ( !v_search.equals("") ) { 
                sql += "\n and     (e.userid like " +SQLString.Format("%"+v_search+"%") + " or e.name like " +SQLString.Format("%"+v_search+"%") + ") ";
			}

			if ( !ss_grcode.equals("ALL"))     sql += "\n and     a.grcode = " +SQLString.Format(ss_grcode);
			if ( !ss_gyear.equals("ALL"))      sql += "\n and     a.gyear = " +SQLString.Format(ss_gyear);
			if ( !ss_grseq.equals("ALL"))      sql += "\n and     a.grseq = " +SQLString.Format(ss_grseq);
              
			if ( !ss_uclass.equals("ALL"))     sql += "\n and     a.scupperclass = " +SQLString.Format(ss_uclass);
			if ( !ss_mclass.equals("ALL"))     sql += "\n and     a.scmiddleclass = " +SQLString.Format(ss_mclass);
			if ( !ss_lclass.equals("ALL"))     sql += "\n and     a.sclowerclass = " +SQLString.Format(ss_lclass);
              
			if ( !ss_subjcourse.equals("ALL")) sql += "\n and     a.scsubj = " +SQLString.Format(ss_subjcourse);
			if ( !ss_subjseq.equals("ALL"))    sql += "\n and     a.scsubjseq = " +SQLString.Format(ss_subjseq);
              
			
			//if ( v_order.equals("") ) { 
                sql += "\n order   by a.subjnm, a.year, a.subjseq "; //, d.bookname
			//} else { 
                //sql += "\n order by " + v_order + v_orderType;
            //}

			connMgr = new DBConnectionManager();
			list = new ArrayList();
			list2 = new ArrayList();
			ls = connMgr.executeQuery(sql);
			while (ls.next()) {
				if (ls2 != null) try { ls2.close(); } catch (Exception e) {} 
				dbox = ls.getDataBox();
				sql = "select a.month, a.bookcode, b.bookname, b.relativebookcode, b.bookcode	\n"
					+ "from   tz_proposebook a, tz_bookinfo b							\n"
					+ "where  a.bookcode = b.bookcode									\n"
					+ "and    a.subj = " + SQLString.Format(ls.getString("subj")) + "			\n"
					+ "and    a.year = " + SQLString.Format(ls.getString("year")) + "			\n"
					+ "and    a.subjseq = " + SQLString.Format(ls.getString("subjseq")) + "		\n"
					+ "and    a.userid = " + SQLString.Format(ls.getString("userid")) + "		\n"
				    + "order  by a.month, b.bookname                     				\n";
				ls2 = connMgr.executeQuery(sql);
				while(ls2.next()) {
					dbox2 = ls2.getDataBox();
					list2.add(dbox2);
				}
				dbox.put("d_booklist", list2);
				list2 = new ArrayList();
				list.add(dbox);
				
			}
		}            
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return list;
	}
	
	/**
	도서선택 몇 개인지 찾기
	@param box          receive from the form object and session
	@return ArrayList   도서선택 몇 개인지 찾기
	*/      
	public int SelectMaxMonth(RequestBox box) throws Exception {               
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		ListSet ls2 = null;
		ArrayList list = null;
		String sql  = "";
		int max_month = 0;
		
		String  ss_bookname = box.getString("s_bookname");     
		String  v_order = box.getString("p_order");           // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서
        
        String ss_userid = box.getSession("userid");
        String ss_status = box.getStringDefault("s_status", "ALL");
        
        
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수
        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과정분류(대분류)
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");   // 과정분류(중분류)
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과정분류(소분류)
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  v_search = box.getString("p_search");    // 검색어

		try {
			sql = "select max(month) month 									\n"
				+ "from   vz_scsubjseq a, tz_proposebook b, tz_member e		\n"
				+ "where  a.subj = b.subj									\n"
				+ "and    a.year = b.year									\n"
				+ "and    a.subjseq = b.subjseq								\n"
				+ "and    b.userid = e.userid								\n"
				+ "and    a.isonoff = 'RC'									\n";
			
			if ( !v_search.equals("") ) { 
                sql += "and    (e.userid like " +SQLString.Format("%"+v_search+"%") + " or e.name like " +SQLString.Format("%"+v_search+"%") + ")	\n";
              }

              if ( !ss_grcode.equals("ALL"))     sql += "and    a.grcode = " +SQLString.Format(ss_grcode) + "	\n";
              if ( !ss_gyear.equals("ALL"))      sql += "and    a.gyear = " +SQLString.Format(ss_gyear) + "	\n";
              if ( !ss_grseq.equals("ALL"))      sql += "and    a.grseq = " +SQLString.Format(ss_grseq) + "	\n";
              
              if ( !ss_uclass.equals("ALL"))     sql += "and    a.scupperclass = " +SQLString.Format(ss_uclass) + "	\n";
              if ( !ss_mclass.equals("ALL"))     sql += "and    a.scmiddleclass = " +SQLString.Format(ss_mclass) + "	\n";
              if ( !ss_lclass.equals("ALL"))     sql += "and    a.sclowerclass = " +SQLString.Format(ss_lclass) + "	\n";
              
              if ( !ss_subjcourse.equals("ALL")) sql += "and    a.scsubj = " +SQLString.Format(ss_subjcourse) + "	\n";
              if ( !ss_subjseq.equals("ALL"))    sql += "and    a.scsubjseq = " +SQLString.Format(ss_subjseq) + "	\n";
              
//			sql += " order by a.subjnm, a.year, a.subjseq "; //, d.bookname
			
			connMgr = new DBConnectionManager();
			list = new ArrayList();
			ls = connMgr.executeQuery(sql);
            
			if (ls.next()) {
				max_month = ls.getInt("month");
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
		return max_month;
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
        
        String v_bookcode = (String)data.get("bookcode");
        String v_month = (String)data.get("month");
        String v_waybill_no = (String)data.get("waybill_no");
        String v_delivery_comp = (String)data.get("delivery_comp");
        String v_delivery_url = (String)data.get("delivery_url");
        String v_delivery_tel = (String)data.get("delivery_tel");
		
        int pidx = 1;

		sql = "update tz_proposebook 	\n"
			+ "set    waybill_no = ?	\n"
			+ "     , delivery_comp	= ?	\n"
			+ "     , delivery_tel = ?	\n"
			+ "     , delivery_url = ?	\n"
			+ "     , status = decode(status,'W',decode(sign(?),1,'D',status),status) \n"
			+ "     , delivery_date = decode(status,'W',decode(sign(?),1,to_char(sysdate,'yyyy-mm-dd'),delivery_date),delivery_date) \n"
			+ "where  subj = ?			\n"
			+ "and    year = ?			\n"
			+ "and    subjseq= ?		\n"
			+ "and    userid = ?		\n"
			+ "and    month = ?			\n"
			+ "and    bookcode = ?		\n";
        
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
           
            pstmt.setString( pidx++, v_waybill_no );
            pstmt.setString( pidx++, v_delivery_comp );
            pstmt.setString( pidx++, v_delivery_tel );
            pstmt.setString( pidx++, v_delivery_url );
            pstmt.setString( pidx++, v_waybill_no );
            pstmt.setString( pidx++, v_waybill_no );
            pstmt.setString( pidx++, v_subj );
            pstmt.setString( pidx++, v_year );
            pstmt.setString( pidx++, v_subjseq );
            pstmt.setString( pidx++, v_userid );
            pstmt.setString( pidx++, v_month );
            pstmt.setString( pidx++, v_bookcode );

            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }

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
		String fileNm = "운송장업로드를위한_도서배송목록.xls";
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
			
		    sheet.setColumnView( 0,  6); // 쉬트의 번호 컬럼(0번째)의 넓이 설정. setCloumnView(몇번째 컬럼, 넓이)
			sheet.setColumnView( 1,  9); // 쉬트의 이름 컬럼(1번째)의 넓이 설정
			sheet.setColumnView( 2,  7); // 쉬트의 비고 컬럼(2번째)의 넓이 설정
			sheet.setColumnView( 3,  5); // 쉬트의 비고 컬럼(3번째)의 넓이 설정
			sheet.setColumnView( 4, 10); // 쉬트의 비고 컬럼(4번째)의 넓이 설정
			sheet.setColumnView( 5, 10); // 쉬트의 비고 컬럼(5번째)의 넓이 설정
			sheet.setColumnView( 6,  8); // 쉬트의 비고 컬럼(6번째)의 넓이 설정
			sheet.setColumnView( 7, 25); // 쉬트의 비고 컬럼(7번째)의 넓이 설정
			sheet.setColumnView( 8, 10); // 쉬트의 비고 컬럼(8번째)의 넓이 설정
			sheet.setColumnView( 9, 10); // 쉬트의 비고 컬럼(9번째)의 넓이 설정
 
		    // 라벨을 이용하여 해당 셀에 정보 넣기 시작
		    sheet.addCell(new Label(0, 0, "연도",		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(1, 0, "과정코드",	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(2, 0, "기수",		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(3, 0, "월차",		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(4, 0, "ID", 	    numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(5, 0, "성명",		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(6, 0, "도서코드",	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(7, 0, "도서명",   	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(8, 0, "신청일",   	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(9, 0, "운송장번호",	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(10, 0, "업체이름", 	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(11, 0, "URL",      	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
		    sheet.addCell(new Label(12, 0, "연락처",   	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입

		    retCnt = list.size();
			if(list!=null && retCnt > 0) {

				for(int i = 0; i < retCnt; i++) {
					dbox  = (DataBox)list.get(i);

				    sheet.addCell(new Label( 0, i+1, dbox.getString("d_year"),		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 1, i+1, dbox.getString("d_subj"),		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 2, i+1, dbox.getString("d_subjseq"),	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 3, i+1, dbox.getString("d_month"),		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 4, i+1, dbox.getString("d_userid"), 	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 5, i+1, dbox.getString("d_name"),		numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 6, i+1, dbox.getString("d_bookcode"),	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 7, i+1, dbox.getString("d_bookname"),	dataFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 8, i+1, FormatDate.getFormatDate(dbox.getString("d_ldate").substring(0, 8), "yyyy-MM-dd"),  	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label( 9, i+1, "", 	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label(10, i+1, "", 	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label(11, i+1, "",	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
				    sheet.addCell(new Label(12, i+1, "",   	numberFormat)); // 쉬트의 addCell 메소드를 사용하여 삽입
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
	 * 도서 중복 체크 (업로드시 있는지 여부 판단)
	 * @param connMgr
	 * @param a_bookcode
	 * @return
	 * @throws Exception
	 */
	public int isExitRelativeBookCode(DBConnectionManager connMgr, String a_bookcode) throws Exception {               
		ListSet ls = null;
		String sql  = "";
		int retCnt = 0;
		try {

			sql = "\n select count(*) as cnt "					
				+ "\n from   tz_bookinfo "
				+ "\n where  relativebookcode = " + SQLString.Format(a_bookcode) ;        
			
			ls = connMgr.executeQuery(sql);
            
			if (ls.next()) {
				retCnt = ls.getInt("cnt");
			}
		}            
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
		}
		return retCnt;
	}
	

	/**
	 * 도서정보 등록
	 * @param box
	 * @return
	 * @throws Exception 
	 */
	public int insertBookInfo(Hashtable data) throws Exception {
        int isOk = 0;

        boolean v_CreateConnManager = false;
        boolean v_CreatePreparedStatement = false;

        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String sql = "";
        ListSet ls = null;

        String v_userid 			= (String)data.get("userid");

        int v_bookcode 	= 0;
        int v_code = 0;
        
        String v_bookname 			= (String)data.get("bookname");
        String v_author 			= (String)data.get("author");
        String v_publisher 			= (String)data.get("publisher");
        String v_core_contents 		= (String)data.get("core_contents");
        String v_book_contents 		= (String)data.get("book_contents");
        String v_relativebookcode 	= (String)data.get("relativebookcode");
        String v_refbook1 			= (String)data.get("refbook1");
        String v_refbook2 			= (String)data.get("refbook2");
        String v_comp 				= (String)data.get("comp");
        int v_price 	= Integer.parseInt(data.get("price").toString());
        int v_stock 	= Integer.parseInt(data.get("stock").toString());

        int pidx = 1;
        
        try {

            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

			// 도서코드 일련번호 생성
			sql = "\n select nvl(max(bookcode), 0)+1 as bookcode "
				+ "\n from   tz_bookinfo ";
			ls = connMgr.executeQuery(sql);
			if (ls.next()) {
				v_bookcode = ls.getInt("bookcode");
			} else {
				v_bookcode = 1;
			}
			ls.close();

			sql = "\n insert into tz_bookinfo( "
				+ "\n        bookcode "
				+ "\n      , bookname "
				+ "\n      , author "
				+ "\n      , publisher "
				+ "\n      , price "
				+ "\n      , ldate "
				+ "\n      , indate "
				+ "\n      , luserid "
				+ "\n      , inuserid "
				+ "\n      , stock "
				+ "\n      , core_contents "
				+ "\n      , book_contents "
				+ "\n      , realfile "
				+ "\n      , savefile "
				+ "\n      , comp "
				+ "\n      , relativebookcode "
				+ "\n      , refbook1 "
				+ "\n      , refbook2 "
				+ "\n ) "
				+ "\n values ( "
				+ "\n        ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , to_char(sysdate,'yyyymmddhh24miss') "
				+ "\n      , to_char(sysdate,'yyyymmddhh24miss') "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
//				+ "\n      , empty_clob() "
//				+ "\n      , empty_clob() "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n      , ? "
				+ "\n ) ";

            if ( pstmt == null ) { 
            	pstmt = connMgr.prepareStatement(sql);
                v_CreatePreparedStatement = true;
            }
			
			pstmt.setInt   (pidx++, v_bookcode);
			pstmt.setString(pidx++, v_bookname);
			pstmt.setString(pidx++, v_author);
			pstmt.setString(pidx++, v_publisher);
			pstmt.setInt   (pidx++, v_price);
			pstmt.setString(pidx++, v_userid);
			pstmt.setString(pidx++, v_userid);
			pstmt.setInt   (pidx++, v_stock);
			pstmt.setString(pidx++, v_core_contents);
			pstmt.setString(pidx++, v_book_contents);
			pstmt.setString(pidx++, ""); // 업로드시엔 파일을 올리지 않음.
			pstmt.setString(pidx++, ""); // 업로드시엔 파일을 올리지 않음.
			pstmt.setString(pidx++, v_comp);
			pstmt.setString(pidx++, v_relativebookcode);
			pstmt.setString(pidx++, v_refbook1);
			pstmt.setString(pidx++, v_refbook2);
			
			isOk = pstmt.executeUpdate();
			if ( pstmt != null ) { pstmt.close(); }
			
			sql = "select book_contents from tz_bookinfo where bookcode = " + v_bookcode;
//            connMgr.setOracleCLOB(sql, v_book_contents);    
            sql = "select core_contents from tz_bookinfo where bookcode = " + v_bookcode;
//            connMgr.setOracleCLOB(sql, v_core_contents);    
			
			
			if(v_stock > 0) {
				sql = "select nvl(max(code), 0)+1 as code from tz_bookinfo_history";
				ls = connMgr.executeQuery(sql);
				if (ls.next()) {
					v_code = ls.getInt("code");
				} else {
					v_code = 1;
				}
				ls.close();
	                
				sql = " insert into tz_bookinfo_history(code, bookcode, ldate, luserid, stock) "
					+ " values (?, ?, to_char(sysdate,'yyyymmddhh24miss'), ?, ?)";
				pstmt = connMgr.prepareStatement(sql);
	
				pstmt.setInt   (1, v_code);
				pstmt.setInt   (2, v_bookcode);
				pstmt.setString(3, v_userid);
				pstmt.setInt   (4, v_stock);
				
				isOk = pstmt.executeUpdate();
			}
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }

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
     * 도서변경 가능 리스트
     * @param box
     * @return
     * @throws Exception
     */
	public ArrayList selectChangeBookList(RequestBox box) throws Exception {               
		DataBox dbox = null;
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		ArrayList list = null;
		String sql  = "";
		String v_subj = box.getString("p_subj");
		String v_year = box.getString("p_year");
		String v_subjseq = box.getString("p_subjseq");
		String v_userid = box.getString("p_userid");
		int v_month = box.getInt("p_month");
		int v_bookcode = box.getInt("p_bookcode");
	
		try {
			list = new ArrayList();
			
			sql = "\n select a.subj "
				+ "\n      , a.month "
				+ "\n      , a.bookcode "
				+ "\n      , b.bookname "
				+ "\n      , b.author "
				+ "\n      , b.publisher "
				+ "\n      , b.price "
				+ "\n from   tz_subjbook a "
				+ "\n      , tz_bookinfo b "
				+ "\n where  a.bookcode = b.bookcode "
				+ "\n and    a.subj = " + SQLString.Format(v_subj)
				+ "\n and    a.month = " + SQLString.Format(v_month)
				+ "\n and    a.bookcode not in (select bookcode "
				+ "\n                           from   tz_proposebook "
				+" \n                           where  subj = " + SQLString.Format(v_subj)
				+ "\n                           and    year = " + SQLString.Format(v_year)
				+ "\n                           and    subjseq = " + SQLString.Format(v_subjseq)
				+ "\n                           and    userid = " + SQLString.Format(v_userid)
				+ "\n                          ) ";

			connMgr = new DBConnectionManager();
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
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return list;
	}

	
	/**
	 * 도서변경
	 * @param box
	 * @return
	 * @throws Exception 
	 */
	public int updateChangeBook(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;        
		PreparedStatement pstmt = null;        
		String sql = "";
		int isOk = 0;  

		String ss_userid = box.getSession("userid");
		
		String v_subj			= box.getString("p_subj");
		String v_year 			= box.getString("p_year");
		String v_subjseq 		= box.getString("p_subjseq");
		String v_month 			= box.getString("p_month");
		String v_userid			= box.getString("p_userid");
		String v_bookcode 		= box.getString("p_bookcode");
		String v_new_bookcode 	= box.getString("p_new_bookcode");
        
        try {
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			sql = "\n update tz_proposebook set "
				+ "\n        bookcode = ? "
				+ "\n      , shipment_date = '' "
				+ "\n      , delivery_date = '' "
				+ "\n      , status = 'W' "
				+ "\n      , note = '' "
				+ "\n      , waybill_no = '' "
				+ "\n      , delivery_comp = '' "
				+ "\n      , delivery_tel = '' "
				+ "\n      , delivery_url = '' "
				+ "\n where  subj = ? "
				+ "\n and    year = ? "
				+ "\n and    subjseq = ? "
				+ "\n and    userid = ? "
				+ "\n and    month = ? "
				+ "\n and    bookcode = ? ";

			int param = 0;
			pstmt = connMgr.prepareStatement(sql);
			pstmt.setString(++param, v_new_bookcode);
			pstmt.setString(++param, v_subj);
			pstmt.setString(++param, v_year);
			pstmt.setString(++param, v_subjseq);
			pstmt.setString(++param, v_userid);
			pstmt.setString(++param, v_month);
			pstmt.setString(++param, v_bookcode);
			
			isOk = pstmt.executeUpdate();
			
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}
		catch(Exception ex) {
			isOk = 0;
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if (isOk > 0) {connMgr.commit();} else {connMgr.rollback();}
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return isOk;

	}
	

	/**
	 * 도서정보가 없는 수강생 명단
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public ArrayList notSelectProposeBookList(RequestBox box) throws Exception {               
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		ArrayList list = null;
		String sql  = "";
		DataBox dbox = null;
        
		String  ss_grcode   = box.getString("s_grcode");    // 교육그룹
        String  ss_gyear    = box.getString("s_gyear");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수
        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과정분류(대분류)
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");   // 과정분류(중분류)
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과정분류(소분류)
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        
		String  v_orderColumn = box.getString("p_orderColumn");           // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서

		try {
			
			sql = "\n select a.subj "
			    + "\n      , a.year "
			    + "\n      , a.subjseq "
			    + "\n      , a.subjnm "
			    + "\n      , b.userid "
			    + "\n      , e.name "
			    + "\n      , get_compnm(b.comp) as compnm "
			    + "\n      , get_postnm(e.post) as postnm "
			    + "\n      , b.appdate "
			    + "\n from  (select grcode, gyear, grseq, subj, year, subjseq, subjnm, scupperclass, scmiddleclass, sclowerclass from vz_scsubjseq where isonoff = 'RC') a "
			    + "\n      , tz_propose b "
			    + "\n      ,(select subj, count(distinct month) as subj_cnt "
			    + "\n        from   tz_subjbook "
			    + "\n        where  nvl(use_yn,'Y')='Y' "
			    + "\n        group  by subj) c "
			    + "\n      ,(select subj, year, subjseq, userid, count(distinct month) as pro_cnt "
			    + "\n        from   tz_proposebook "
			    + "\n        group  by subj, year, subjseq, userid) d "
			    + "\n      , tz_member e "
			    + "\n where  a.subj = b.subj "
			    + "\n and    a.year = b.year "
			    + "\n and    a.subjseq = b.subjseq "
			    + "\n and    b.subj = c.subj "
			    + "\n and    b.userid = e.userid "
			    + "\n and    b.subj = d.subj(+) "
			    + "\n and    b.year = d.year(+) "
			    + "\n and    b.subjseq = d.subjseq(+) "
			    + "\n and    b.userid = d.userid(+) "
			    + "\n and    (c.subj_cnt > d.pro_cnt or d.userid is null) ";

			if ( !ss_grcode.equals("ALL"))     sql += "\n and    a.grcode = " +SQLString.Format(ss_grcode);
			if ( !ss_gyear.equals("ALL"))      sql += "\n and    a.gyear = " +SQLString.Format(ss_gyear);
			if ( !ss_grseq.equals("ALL"))      sql += "\n and    a.grseq = " +SQLString.Format(ss_grseq);
              
			if ( !ss_uclass.equals("ALL"))     sql += "\n and    a.scupperclass = " +SQLString.Format(ss_uclass);
			if ( !ss_mclass.equals("ALL"))     sql += "\n and    a.scmiddleclass = " +SQLString.Format(ss_mclass);
			if ( !ss_lclass.equals("ALL"))     sql += "\n and    a.sclowerclass = " +SQLString.Format(ss_lclass);
              
			if ( !ss_subjcourse.equals("ALL")) sql += "\n and    a.subj = " +SQLString.Format(ss_subjcourse);
			if ( !ss_subjseq.equals("ALL"))    sql += "\n and    a.subjseq = " +SQLString.Format(ss_subjseq);

			if ( v_orderColumn.equals("") ) { 
                sql += "\n order  by b.appdate desc ";
            } else { 
                sql += "\n order  by " + v_orderColumn + v_orderType;
            }
			
			connMgr = new DBConnectionManager();
			list = new ArrayList();
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
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return list;
	}
	

	/**
	 * 개월차도서
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public ArrayList selectMonthlyBookList(RequestBox box) throws Exception { 
		DBConnectionManager connMgr         = null;
		ListSet             ls              = null;
		ListSet             ls2              = null;
		ArrayList           list            = null;
		ArrayList           list2           = null;
		String              sql             = "";
		String              sql2            = "";
		DataBox             dbox            = null;
	
		String v_subj    = box.getString("p_subj"    );
		String v_year    = box.getString("p_year"); 
		String v_subjseq = box.getString("p_subjseq");
		String v_userid  = box.getString("p_userid");
	
		try { 
			connMgr     = new DBConnectionManager();
			list        = new ArrayList();
			list2        = new ArrayList();
		
			sql = "\n select subj, month "
				+ "\n from   tz_subjbook "
				+ "\n where  subj = " + StringManager.makeSQL(v_subj)
				+ "\n group  by subj, month "	
				+ "\n order  by subj, month ";
			ls  = connMgr.executeQuery(sql);
			while ( ls.next() ) { 
				dbox = ls.getDataBox();
			
				sql2= "select a.subj, a.month, b.bookname, author, b.bookcode "
					+ "     , ( "
					+ "		   select (case when count(*) > 0 then 'Y' else 'N' end) "
					+ "		   from   tz_proposebook "
					+ "		   where  subj = a.subj "
					+ "		   and    userid = " + StringManager.makeSQL(v_userid)
					+ "		   and    month = a.month "
					+ "		   and    bookcode = a.bookcode "
					+ "        and    year = " + StringManager.makeSQL(v_year)
					+ "        and    subjseq = " + StringManager.makeSQL(v_subjseq)
					+ "       ) as checkgubun "		
					+ " 	, ( "
					+ "        nvl(( select distinct 'Y' "
					+ "           	 from   tz_proposebook "
					+ "           	 where  subj = a.subj "
					+ "           	 and    year = " + StringManager.makeSQL(v_year)
					+ "           	 and    subjseq = " + StringManager.makeSQL(v_subjseq)
					+ "           	 and    userid = " + StringManager.makeSQL(v_userid)
					+ "       ),'N')  ) as disablegubun "
					+ "from   tz_subjbook a, tz_bookinfo b "
					+ "where  a.bookcode = b.bookcode "
					+ "and    nvl(a.use_yn,'Y')='Y' "
					+ "and    a.subj = " + StringManager.makeSQL(ls.getString("subj"))
					+ "and    a.month = " + SQLString.Format(ls.getString("month"));
				
				ls2      = connMgr.executeQuery(sql2);
				while(ls2.next()) {
					DataBox dbox2 = ls2.getDataBox();
					list2.add(dbox2);
				}
			
				dbox.put("d_bookinfo", list2);
				list2 = new ArrayList();
				list.add(dbox);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e);
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls != null ) { 
				try { ls.close(); } catch ( Exception e ) { } 
			}
			if ( ls2 != null ) { 
				try { ls2.close(); } catch ( Exception e ) { } 
			}
			if ( connMgr != null ) { 
				try { connMgr.freeConnection(); } catch ( Exception e ) { } 
			}
		}
	
		return list;
	}	

	/**
	 * 배송지정보
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public DataBox getDeliveryInfo(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	    = null;
		DataBox             dbox        	= null;
		ListSet             ls        	= null;
		String 			  sql 			= "";
		String 			  v_userid 	= box.getString("p_userid");
		String            v_subj        = box.getString("p_subj");
		String            v_year        = box.getString("p_year");
		String            v_subjseq     = box.getString("p_subjseq");
	
		try { 
			connMgr     = new DBConnectionManager();
			
			sql = "select b.userid, b.name, a.delivery_handphone \n"
				+ "     , a.delivery_post1, a.delivery_post2, a.delivery_address1	\n"
				+ "		, b.handphone,  b.zip_cd, b.address  \n"
				+ "     , get_compnm(b.comp) companynm \n"
				+ "     , b.position_nm as deptnm \n" 
				+ "     , b.lvl_nm as jikwinm \n"
				+ "from   tz_delivery a, tz_member b \n"
				+ "where  a.userid = b.userid \n"
				+ "and    a.userid = " + StringManager.makeSQL(v_userid) + " \n"
				+ "and    a.ldate = (select max(ldate) from tz_delivery where userid = " + StringManager.makeSQL(v_userid) + ") \n"
				+ "and    rownum = 1 \n";

			ls         = connMgr.executeQuery(sql);
		
			if ( ls.next() ) { 
				dbox    = ls.getDataBox();
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls != null ) { 
				try { ls.close(); } catch ( Exception e ) { } 
			}
			if ( connMgr != null ) { 
				try { connMgr.freeConnection(); } catch ( Exception e ) { } 
			}
		}
	
		return dbox;
	}

	
	/**
	배송지 정보
	@param box      receive from the form object and session
	@return 
	*/
	public DataBox selectMemberInfo(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	    = null;
		DataBox             dbox        	= null;
		ListSet             ls        	= null;
		String 			  sql 			= "";
		String 			  v_userid 	= box.getString("p_userid");
		
		try { 
			connMgr     = new DBConnectionManager();
		
			sql = "select b.userid, b.name \n"
				+ "		, b.handphone,  b.zip_cd, b.address  \n"
				+ "     , get_compnm(b.comp) companynm \n"
				+ "     , b.position_nm as deptnm \n" 
				+ "     , b.lvl_nm as jikwinm \n"
				+ "from  tz_member b \n"
				+ "where userid = " + StringManager.makeSQL(v_userid) + " \n";
			ls         = connMgr.executeQuery(sql);
		
			if ( ls.next() ) { 
				dbox    = ls.getDataBox();
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch ( Exception e ) {
			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} finally {
			if ( ls != null ) { 
				try { ls.close(); } catch ( Exception e ) { } 
			}
		
			if ( connMgr != null ) { 
				try { connMgr.freeConnection(); } catch ( Exception e ) { } 
			}
		}
	
		return dbox;
	}

    /**
    도서 선택 및 배송지 정보 등록
    @param box      receive from the form object and session
    @return isOk   
    */
    public int UpdateRCInfo(RequestBox box) throws Exception {
    	DBConnectionManager    connMgr                     = null;
    	PreparedStatement      pstmt                       = null;
    	String                 sql                         = "";
    	int                    isOk                        = 0;
    	ListSet ls = null;
         
    	String v_subj = box.getString("p_subj");
    	String v_year = box.getString("p_year");
    	String v_subjseq = box.getString("p_subjseq");
    	String v_userid = box.getString("p_userid");
    	try {
    		connMgr = new DBConnectionManager();
    		connMgr.setAutoCommit(false);
         	   
     		int v_bookcnt = box.getInt("p_bookcnt");
          	for(int i=1; i <= v_bookcnt; i++) {
          		int v_bookcode= box.getInt("p_" + i + "_radio");
          		int v_old_bookcode= box.getInt("p_" + i + "_bookcode");

          		sql = " SELECT USERID FROM TZ_PROPOSEBOOK "
          			+ " WHERE  SUBJ = " + StringManager.makeSQL(v_subj)
          			+ " AND    YEAR = " + StringManager.makeSQL(v_year)
          			+ " AND    SUBJSEQ = " + StringManager.makeSQL(v_subjseq)
          			+ " AND    MONTH = " + i
          			+ " AND    USERID = " + StringManager.makeSQL(v_userid);
         	  
          		ls =  connMgr.executeQuery(sql);
                   
          		if(!ls.next()) {  
         		 
          			// INSERT tz_proposesubj table
                	sql = " INSERT INTO TZ_PROPOSEBOOK                                   \n"
                        + " (                                                            \n"
                        + "         subj                                                 \n"
                        + "     ,   year                                                 \n"
                        + "     ,   subjseq                                              \n"
                        + "     ,   userid                                               \n"
                        + "     ,   month                                                \n"
                        + "     ,   bookcode                                             \n"
                        + "     ,   status                                               \n"
                        + " ) VALUES (                                                   \n"
                        + "         ?                                                    \n"
                        + "     ,   ?                                                    \n"
                        + "     ,   ?                                                    \n"
                        + "     ,   ?                                                    \n"
                        + "     ,   ?                                                    \n"
                        + "     ,   ?                                                    \n"
                        + "     ,   ?                                                    \n"
                        + " )                                                            \n";
                         
                    pstmt   = connMgr.prepareStatement(sql);
                    
                    pstmt.setString(1, v_subj);
                    pstmt.setString(2, v_year);
                    pstmt.setString(3, v_subjseq);
                    pstmt.setString(4, v_userid);
                    pstmt.setInt(5, i);
                    pstmt.setInt(6, v_bookcode);
                    pstmt.setString(7, "W");
                    isOk = pstmt.executeUpdate();
                    
                    if ( pstmt != null ) pstmt.close(); 
          		} else {

        			sql = "\n update tz_proposebook set "
        				+ "\n        bookcode = ? "
        				+ "\n      , shipment_date = '' "
        				+ "\n      , delivery_date = '' "
        				+ "\n      , status = 'W' "
        				+ "\n      , note = '' "
        				+ "\n      , waybill_no = '' "
        				+ "\n      , delivery_comp = '' "
        				+ "\n      , delivery_tel = '' "
        				+ "\n      , delivery_url = '' "
        				+ "\n where  subj = ? "
        				+ "\n and    year = ? "
        				+ "\n and    subjseq = ? "
        				+ "\n and    userid = ? "
        				+ "\n and    month = ? "
        				+ "\n and    bookcode = ? ";

        			pstmt = connMgr.prepareStatement(sql);
        			pstmt.setInt(1, v_bookcode);
        			pstmt.setString(2, v_subj);
        			pstmt.setString(3, v_year);
        			pstmt.setString(4, v_subjseq);
        			pstmt.setString(5, v_userid);
        			pstmt.setInt(6, i);
        			pstmt.setInt(7, v_old_bookcode);

                    isOk = pstmt.executeUpdate();
                    
                    if ( pstmt != null ) pstmt.close(); 
          		}


          		sql = "delete from TZ_DELIVERY   		\n"
          			+ "where   subj    = ?      			\n"
          			+ "and     year    = ?      			\n"
          			+ "and     subjseq = ?      			\n"
          			+ "and     userid  = ?      			\n";
	                    
          		pstmt   = connMgr.prepareStatement(sql);
          		pstmt.setString(1, v_subj);
          		pstmt.setString(2, v_year);
          		pstmt.setString(3, v_subjseq);
          		pstmt.setString(4, v_userid);
          		isOk   = pstmt.executeUpdate();
                
          		if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }

          		sql = "insert into tz_delivery( "
          			+ "       subj "
          			+ "     , year "
          			+ "     , subjseq "
          			+ "     , userid "
          			+ "     , delivery_post1 "
          			+ "     , delivery_post2 "
          			+ "     , delivery_address1 "
          			+ "     , delivery_handphone "
          			+ "     , luserid "
          			+ "     , ldate "
          			+ "      ) "
          			+ "values(? "
          			+ "     , ? "
          			+ "     , ? "
          			+ "     , ? "
          			+ "     , ? "
          			+ "     , ? "
          			+ "     , ? "
          			+ "     , ? "
          			+ "     , ? "
          			+ "     , to_char(sysdate, 'yyyymmddhh24miss') "
          			+ "      ) ";

          		pstmt = connMgr.prepareStatement(sql);
                pstmt.setString(1, v_subj);
                pstmt.setString(2, v_year);
                pstmt.setString(3, v_subjseq);
                pstmt.setString(4, v_userid);
                pstmt.setString(5, box.getString("p_post1"));
                pstmt.setString(6, box.getString("p_post2"));
                pstmt.setString(7, box.getString("p_address1"));
                pstmt.setString(8, box.getString("p_delivery_handphone"));
                pstmt.setString(9, v_userid);
                isOk   = pstmt.executeUpdate();
                if ( pstmt != null ) { pstmt.close(); }
          	}
                
            if ( isOk > 0) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e ) { } }
            } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e ) { } }
            }
             
    	} catch(Exception ex) {
   			isOk = 0;
   			if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
   			ErrorManager.getErrorStackTrace(ex, box, sql);
   			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
   		}
   		finally {
   			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
   			
   			 if ( connMgr != null ) { 
                  try { 
                	  connMgr.setAutoCommit(true);
                      connMgr.freeConnection(); 
                  } catch ( Exception e ) { } 
              }
   		}
   		return isOk;
   	}	

}