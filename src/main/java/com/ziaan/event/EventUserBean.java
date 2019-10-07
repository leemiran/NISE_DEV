// **********************************************************
//  1. 제      목: 이벤트관리
//  2. 프로그램명: EventBean.java
//  3. 개      요: 이벤트관리
//  4. 환      경: JDK 1.5
//  5. 버      젼: 1.0
//  6. 작      성: 
//  7. 수      정:
// **********************************************************
package com.ziaan.event;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.homepage.NoticeData;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class EventUserBean { 

    public EventUserBean() {
    	
    }
    
	/**
	 * 이벤트 포틀릿 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public ArrayList eventList(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		DataBox dbox    = null;
		StringBuffer strSQL = null;
		int v_tabseq = box.getInt("p_tabseq");
		
		try { 
			connMgr = new DBConnectionManager();
			
			list = new ArrayList();
			strSQL = new StringBuffer();
			
			//////////////// 추후 삭제 임시/////////////////////start
			if(box.getSession("tem_grcode").equals("N000003")){
				
			strSQL.append("select rownum, tabseq, seq, useyn, addate, adtitle, type, cnt, jungwon \n ") ;
			strSQL.append("      , case when to_number(cnt) < to_number(jungwon) or jungwon is null then status \n ") ;
			strSQL.append("             else '마감' \n ") ;
			strSQL.append("         end as status \n ") ;
			strSQL.append("  from ( \n ") ;
			strSQL.append("         select   distinct a.tabseq, a.seq, a.useyn, a.addate, a.adtitle, a.type \n ") ;
			strSQL.append("                , (select nvl(count(userid),0) cnt from tz_event bb where a.tabseq=bb.tabseq and a.seq=bb.seq) as cnt \n ") ;
			strSQL.append("                , notice_gubun as jungwon \n ") ;
			strSQL.append("                , decode( \n ") ;
			strSQL.append("                          gubun,'A', \n ") ;
			strSQL.append("                                     case when to_char(sysdate, 'yyyymmdd') between trim(startdate) and trim(enddate) then '진행중' \n ") ;
			strSQL.append("                                          when to_char(sysdate, 'yyyymmdd') < trim(startdate) then  '준비중' \n ") ;
			strSQL.append("                                          when to_char(sysdate, 'yyyymmdd') > trim(enddate) then  '마감' \n ") ;
			strSQL.append("                                          else '진행중' \n ") ;
			strSQL.append("                                      end \n ") ;
			strSQL.append("                               ,'B', '당첨자발표' \n ") ;
			strSQL.append("                         )  as status \n ") ;
			strSQL.append("           from tz_notice a, tz_event b \n ") ;
			strSQL.append("          where a.tabseq=b.tabseq(+) \n ") ;
			strSQL.append("            and a.seq=b.seq(+) \n ") ;
			strSQL.append("            order by tabseq desc, seq desc \n ") ;
			strSQL.append("         ) \n ") ;
			strSQL.append(" where tabseq = 5 \n ") ;
			strSQL.append("   and useyn= 'Y' \n ") ;
			strSQL.append("   and seq not in (16) \n ") ;
			strSQL.append("   and rownum <= 4 \n ") ;
			}else{
			////////////////end//////
			
			
			
			
			strSQL.append("select rownum, tabseq, seq, useyn, addate, adtitle, type, cnt, jungwon \n ") ;
			strSQL.append("      , case when to_number(cnt) < to_number(jungwon) or jungwon is null then status \n ") ;
			strSQL.append("             else '마감' \n ") ;
			strSQL.append("         end as status \n ") ;
			strSQL.append("  from ( \n ") ;
			strSQL.append("         select   distinct a.tabseq, a.seq, a.useyn, a.addate, a.adtitle, a.type \n ") ;
			strSQL.append("                , (select nvl(count(userid),0) cnt from tz_event bb where a.tabseq=bb.tabseq and a.seq=bb.seq) as cnt \n ") ;
			strSQL.append("                , notice_gubun as jungwon \n ") ;
			strSQL.append("                , decode( \n ") ;
			strSQL.append("                          gubun,'A', \n ") ;
			strSQL.append("                                     case when to_char(sysdate, 'yyyymmdd') between trim(startdate) and trim(enddate) then '진행중' \n ") ;
			strSQL.append("                                          when to_char(sysdate, 'yyyymmdd') < trim(startdate) then  '준비중' \n ") ;
			strSQL.append("                                          when to_char(sysdate, 'yyyymmdd') > trim(enddate) then  '마감' \n ") ;
			strSQL.append("                                          else '진행중' \n ") ;
			strSQL.append("                                      end \n ") ;
			strSQL.append("                               ,'B', '당첨자발표' \n ") ;
			strSQL.append("                         )  as status \n ") ;
			strSQL.append("           from tz_notice a, tz_event b \n ") ;
			strSQL.append("          where a.tabseq=b.tabseq(+) \n ") ;
			strSQL.append("            and a.seq=b.seq(+) \n ") ;
			strSQL.append("            order by tabseq desc, seq desc \n ") ;
			strSQL.append("         ) \n ") ;
			strSQL.append(" where tabseq = 5 \n ") ;
			strSQL.append("   and useyn= 'Y' \n ") ;
			strSQL.append("   and rownum <= 4 \n ") ;
			}
			
			String s_grcode = box.getSession("grcode");
			if(s_grcode.equals("N000003")){
				strSQL.append(" and seq <> 21 \n ") ;
			}else{
				strSQL.append(" and seq <> 22 \n ") ;
			}
			
			ls = connMgr.executeQuery(strSQL.toString());
			
			while ( ls.next() ) { 
				dbox = ls.getDataBox();
				list.add(dbox);
			}
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return list;
	}
	
	/**
	 * 이벤트 제목으로 셀렉트박스 생성
	 * @param name
	 * @param selected
	 * @param event
	 * @param allcheck
	 * @return
	 * @throws Exception
	 */
	public static String getEventSelectBox(String name, String selected, String event, int allcheck) throws Exception {
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		String result = "";
		String sql = "";
		String v_seq = "";
		String v_adtitle = "";

		result = "  <SELECT name=\"" + name + "\" " + event + " > \n";
        
		if (allcheck == 1) {
			result += " <option value=\"\">전체</option> \n";
		} else if (allcheck == 2) {
			result += " <option value=\"\">선택하세요</option> \n";
		}else if (allcheck == 3) {
				
		}

		try {
			connMgr = new DBConnectionManager();

			sql =  " select tabseq, seq, adtitle  from tz_notice  ";
			sql += "  where 1 = 1 ";
			sql += "  and tabseq = 5 ";
			sql += "  and gubun = 'A' ";
			sql += "  order by tabseq, seq desc";
			ls = connMgr.executeQuery(sql);
			
			while (ls.next()) {
				v_seq = ls.getString("seq");
				v_adtitle = ls.getString("adtitle");

				result += " <option value=\"" + v_seq+"\"";
				if (selected.equals(v_seq) ) {
					result += " selected ";
				}
				result += ">" + v_adtitle + "</option> \n";
			}
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			if (ls != null) {
				try {
					ls.close();
				} catch (Exception e) {
				}
			}
			if (connMgr != null) {
				try {
					connMgr.freeConnection();
				} catch (Exception e10) {
				}
			}
		}

		result += "  </SELECT> \n";
		return result;
	}
    
    /**
     * 이벤트 리스트
     * @param box
     * @return
     * @throws Exception
     */
	public ArrayList selectList(RequestBox box) throws Exception { 
		DBConnectionManager connMgr = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		DataBox             dbox    = null;
		StringBuffer strSQL         = null;

        int v_tabseq = box.getInt("p_tabseq");
        int v_seq    = box.getInt("p_seq");
        
        String v_searchtext = box.getString("p_searchtext");
		String v_search     = box.getString("p_search");
		String s_gadmin     = box.getSession("gadmin");
		String v_gadmin     = "";
		String s_grcode = box.getSession("tem_grcode");
		
		if(!s_gadmin.equals("")) {
			v_gadmin = s_gadmin.substring(0,1);
		}
		
		int v_pageno        = box.getInt("p_pageno");
		
		try { 
			connMgr = new DBConnectionManager();
			list = new ArrayList();
			
			strSQL = new StringBuffer();
			strSQL.append("select distinct tabseq, seq, adtitle, adname, addate, cnt, gubun, useyn, eventcnt, jungwon \n ") ;
			strSQL.append("      , case when to_number(eventcnt) < to_number(jungwon) or jungwon is null then status \n ") ;
			strSQL.append("             else 'C' \n ") ;
			strSQL.append("         end as status \n ") ;
			strSQL.append("  from \n ") ;
			strSQL.append("( \n ") ;
			strSQL.append("  select   a.tabseq, a.seq, a.adtitle, a.adname, a.addate, a.cnt, a.gubun, useyn \n ") ;
			strSQL.append("         , (select nvl(count(userid),0) cnt from tz_event bb where a.tabseq=bb.tabseq and a.seq=bb.seq) as eventcnt \n ") ;
			strSQL.append("         , notice_gubun as jungwon, \n ") ;
			strSQL.append("         case when to_char(sysdate, 'yyyymmdd') between trim(startdate) and trim(enddate) then 'B' \n ") ;  //진행중
			strSQL.append("              when to_char(sysdate, 'yyyymmdd') < trim(startdate) then  'A' \n ") ; //준비중
			strSQL.append("              when to_char(sysdate, 'yyyymmdd') > trim(enddate) then  'C' \n ") ;   //마감
			strSQL.append("              else 'B' end as status                                      \n ") ;   //진행중 
			strSQL.append("    from tz_notice a, tz_event b \n ") ;
			strSQL.append("   where a.tabseq=b.seq(+) \n ") ;
			strSQL.append("     and a.tabseq=b.seq(+) \n ") ;
			strSQL.append(") ") ;
			strSQL.append("   where tabseq = "+v_tabseq+" \n ") ;
			strSQL.append("     and useyn='Y' \n ") ;

			if(s_grcode.equals("N000003")){
				strSQL.append("     and seq <> 21 \n ") ;
			}else{
				strSQL.append("     and seq <> 22 \n ") ;
			}
			
			/*
			strSQL.append(" select seq, adtitle, adname, addate, cnt, gubun, \n ");
			strSQL.append("        case when to_char(sysdate, 'yyyymmdd') between trim(startdate) and trim(enddate) then 'B' \n ");  //진행중
			strSQL.append("             when to_char(sysdate, 'yyyymmdd') < trim(startdate) then  'A' \n ");                   //준비중
			strSQL.append("             when to_char(sysdate, 'yyyymmdd') > trim(enddate) then  'C' \n ");                       //마감
			strSQL.append("             else 'B' end as status \n ");                                                    //진행중
			strSQL.append("   from tz_notice \n ");
			strSQL.append("  where tabseq = " + v_tabseq + " \n ");
			*/
			if ( !v_searchtext.equals("") ) {                                //    검색어가 있으면
				v_pageno = 1;                                                //    검색할 경우 첫번째 페이지가 로딩된다       
				if ( v_search.equals("adtitle") ) {                          //    제목으로 검색할때
					strSQL.append(" and adtitle like " + StringManager.makeSQL("%" + v_searchtext + "%"));
				} else if ( v_search.equals("adcontents") ) {                //    내용으로 검색할때
					strSQL.append(" and adcontent like " + StringManager.makeSQL("%" + v_searchtext + "%"));
				} 
			}
			strSQL.append("  order by seq desc  ");

			ls = connMgr.executeQuery(strSQL.toString());
			
            int row=7;
			ls.setPageSize(row);                             //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                     //  현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();        //  전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();        //  전체 row 수를 반환한다
            
			while ( ls.next() ) { 
				
				dbox = ls.getDataBox();
				dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(row));
				list.add(dbox);
			}
		}
		catch ( Exception ex ) { 
			   ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return list;
	}
	
	/**
	 * 이벤트 상세보기
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public DataBox selectView(RequestBox box) throws Exception { 
		DBConnectionManager connMgr = null;
		ListSet             ls      = null;
		DataBox             dbox    = null;
		StringBuffer strSQL = null;;
		
		int v_tabseq = box.getInt("p_tabseq");
		String v_seq = box.getString("p_seq");
		String v_process = box.getString("p_process");
		
		try { 
			connMgr = new DBConnectionManager();
			strSQL = new StringBuffer();
			
			strSQL.append(" select addate, adtitle, adcontent, startdate, enddate, type, gubun, isall  \n ");
			strSQL.append("  from tz_notice \n ");
			strSQL.append(" where tabseq = " + v_tabseq + " \n ");
			strSQL.append("   and seq    = " + v_seq + " \n ");
			
			ls = connMgr.executeQuery(strSQL.toString());
			
			while ( ls.next() ) { 
				dbox = ls.getDataBox();
			}
			
			connMgr.executeUpdate("update TZ_NOTICE set cnt = cnt + 1 where tabseq = " + v_tabseq + " and seq = " + v_seq);
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return dbox;
	}
	   
   /**
    이벤트 등록 01
    @param box      receive from the form object and session
    @return int		isOk
    */
     public int insertEvent01(RequestBox box) throws Exception { 
    	 DBConnectionManager connMgr 	= null;
         PreparedStatement pstmt 		= null;
         ListSet ls  					= null;
         
         String  sql  = "";
         String  sql2 = "";
         
         String s_userid  		= box.getSession("userid");
         String v_event   		= box.getString("p_event");
         String v_inputdata1   	= box.getString("p_inputdata1");     
         String v_inputdata2   	= box.getString("p_inputdata2");   
         String v_inputdata3   	= box.getString("p_inputdata3");   
         String v_inputdata4   	= box.getString("p_inputdata4");   
         String v_inputdata5   	= box.getString("p_inputdata5");            

         int isOk   = 0;
         int v_seq = 0;
         
         try {
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false);
             
             sql2 ="select nvl(count(userid),0) cnt from tz_event where event = "+ StringManager.makeSQL(v_event) + "  and userid= " + StringManager.makeSQL(s_userid) + " \n";
             
             ls = connMgr.executeQuery(sql2);
  			 
             if(ls.next()) {
             	v_seq = ls.getInt("cnt");
             }
             
             if(v_seq > 0) {
            	 isOk = -1 ; 
            	 return isOk;
             }

 			sql  = " insert into tz_event(event, userid, inputdata1, inputdata2, inputdata3, inputdata4, inputdata5, ldate, luserid) "
 			     + " values (?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?)"; 

 			pstmt = connMgr.prepareStatement(sql);
 			pstmt.setString (1,v_event);
 			pstmt.setString (2,s_userid);
 			pstmt.setString (3,v_inputdata1);
 			pstmt.setString (4,v_inputdata2);
 			pstmt.setString (5,v_inputdata3);
 			pstmt.setString (6,v_inputdata4);
 			pstmt.setString (7,v_inputdata5); 			
 			pstmt.setString (8,s_userid);
 			
 			isOk = pstmt.executeUpdate();
 			if ( pstmt != null ) { pstmt.close(); }
	   		 if ( isOk > 0 ) { 
				 connMgr.commit();
			 } else { 
				 connMgr.rollback();		   	
			 }
 			
         }
         catch (Exception ex) {
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
         }
         finally {
             if(ls != null) { try { ls.close(); }catch (Exception e) {} }
             if(pstmt != null)  { try { pstmt.close(); } catch (Exception e1) {} }
             if(connMgr != null) { try { connMgr.setAutoCommit(true); }catch (Exception e2) {} }
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }
         return isOk;
    }
 
     /**
     이벤트 등록 02
     @param box      receive from the form object and session
     @return int		isOk
     */
      public int insertEvent02(RequestBox box) throws Exception { 
     	 DBConnectionManager connMgr 	= null;
          PreparedStatement pstmt 		= null;
          ListSet ls  					= null;
          
          String  sql  = "";
          String  sql2 = "";
          
          String v_event   	     	= box.getString("p_event");
     	  if(v_event.equals("")){
    		  v_event = "EV02";
    	  }
          
          String s_userid  		    = box.getSession("userid");
          String v_inputdata1   	= box.getString("p_rep_content");  
          String v_inputdata2   	= box.getString("p_inputdata2");   
          String v_inputdata3   	= box.getString("p_inputdata3");   
          String v_inputdata4   	= box.getString("p_inputdata4");   
          String v_inputdata5   	= box.getString("p_inputdata5");            

          int isOk   = 0;
          int v_seq = 0;
          
          try {
              connMgr = new DBConnectionManager();
              connMgr.setAutoCommit(false);
              
              sql2 ="select nvl(count(userid),0) cnt from tz_event where event = "+ StringManager.makeSQL(v_event) + "  and userid= " + StringManager.makeSQL(s_userid) + " \n";
              
              ls = connMgr.executeQuery(sql2);
   			 
              if(ls.next()) {
              	v_seq = ls.getInt("cnt");
              }
              
              if(v_seq > 0) {
             	 isOk = -1 ; 
             	 return isOk;
              }

  			sql  = " insert into tz_event(event, userid, inputdata1, inputdata2, inputdata3, inputdata4, inputdata5, ldate, luserid) "
  			     + " values (?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?)"; 

  			pstmt = connMgr.prepareStatement(sql);
  			pstmt.setString (1,v_event);
  			pstmt.setString (2,s_userid);
  			pstmt.setString (3,v_inputdata1);
  			pstmt.setString (4,v_inputdata2);
  			pstmt.setString (5,v_inputdata3);
  			pstmt.setString (6,v_inputdata4);
  			pstmt.setString (7,v_inputdata5); 			
  			pstmt.setString (8,s_userid);
  			
  			isOk = pstmt.executeUpdate();
  			if ( pstmt != null ) { pstmt.close(); }
  			
	   		 if ( isOk > 0 ) { 
				 connMgr.commit();
			 } else { 
				 connMgr.rollback();		   	
			 }
   		 
          }
          catch (Exception ex) {
              ErrorManager.getErrorStackTrace(ex, box, sql);
              throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
          }
          finally {
              if(ls != null) { try { ls.close(); }catch (Exception e) {} }
              if(pstmt != null)  { try { pstmt.close(); } catch (Exception e1) {} }
              if(connMgr != null) { try { connMgr.setAutoCommit(true); }catch (Exception e2) {} }
              if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
          }
          return isOk;
     }

     /**
      * 이벤트 댓글 등록처리
      * @param box
      * @return
      * @throws Exception
      */
     public int insertMemoData(RequestBox box) throws Exception { 
    	 DBConnectionManager connMgr     = null;
    	 ListSet             ls      = null;
    	 PreparedStatement   pstmt   = null;
    	 
    	 StringBuffer strSQL = null;
    	 int isOk   = 0;
    	 int preIdx = 1;
    	 
    	 //String v_event  = box.getString("p_event");
    	 int    v_tabseq = box.getInt("p_tabseq");
    	 int    v_seq    = box.getInt("p_seq");
    	 //int    v_comseq = 0;
    	 int    v_sseq     = 0;
    	 
    	 String s_userid = box.getSession("userid");              // 작성자 아이디
    	 String s_name   = box.getSession("name");                // 작성자
    	 String v_rep_content = box.getString("p_rep_content");   //응모내용
    	 String isFlag        = "";
    	 Vector temp = box.getVector("p_select");
    	 try { 
    		 
    		 connMgr = new DBConnectionManager();
    		 connMgr.setAutoCommit(false);
    		 
    		 strSQL = new StringBuffer();
    		 
    		 /* S : 이벤트 중복신청 확인  */
             strSQL.append("select nvl(count(userid),0) cnt from tz_event where tabseq = "+v_tabseq+"  and seq = "+v_seq+" and userid= " + StringManager.makeSQL(s_userid) + " \n");
             /* E : 이벤트 중복신청 확인  */
             
             ls = connMgr.executeQuery(strSQL.toString());
             if(ls.next()) {
            	 v_sseq = ls.getInt("cnt");
             }
             ls.close();
             
             if(v_sseq > 0){ 
            	 
            	 return isOk = -3;
            	 
    	     }else{
				 /* S : 이벤트 응모기간  확인 */
    	    	 strSQL.setLength(0);
				 strSQL.append("select \n ") ;
				 strSQL.append("        case when startdate is not null and enddate is not null then \n ") ;
				 strSQL.append("                                                                      case when to_char(sysdate, 'yyyymmdd') between trim(startdate) and trim(enddate) then 'true' \n ") ;
				 strSQL.append("                                                                           else 'flase' \n ") ;
				 strSQL.append("                                                                       end \n ") ;
				 strSQL.append("             when startdate is not null and enddate is null     then \n ") ;
				 strSQL.append("                                                                      case when to_char(sysdate, 'yyyymmdd') >= trim(startdate) then 'true' \n ") ;
				 strSQL.append("                                                                           else 'false' \n ") ;
				 strSQL.append("                                                                       end \n ") ;
				 strSQL.append("             when startdate is null     and enddate is not null then \n ") ;
				 strSQL.append("                                                                      case when to_char(sysdate, 'yyyymmdd') <= trim(enddate) then 'true' \n ") ;
				 strSQL.append("                                                                           else 'false' \n ") ;
				 strSQL.append("                                                                       end \n ") ;
				 strSQL.append("             else 'true' \n ") ;
				 strSQL.append("        end as flag \n ") ;
				 strSQL.append("   from \n ") ;
				 strSQL.append("        ( \n ") ;
				 strSQL.append("          select startdate, enddate, notice_gubun jungwon from tz_notice \n ") ;
				 strSQL.append("           where tabseq="+v_tabseq+" \n ") ;
				 strSQL.append("             and seq="+v_seq+" \n ") ;
				 strSQL.append("         ) \n ") ;
				 /* E : 이벤트 응모기간  확인 */
	    		 
	    		 ls = connMgr.executeQuery(strSQL.toString());
	    		 if(ls.next()) {
	    			isFlag = ls.getString("flag");
	             }
	    		 ls.close();
	    		 
	    		 if(!isFlag.equals("true")){
	    			 
	    			 return isOk = -2;
	    			 
	    		 } else {
	        		 /* S : 이벤트 인원수 제한  확인 */
	    			 strSQL.setLength(0);
	        		 strSQL.append("select \n ") ;
	        		 strSQL.append("        case when jungwon is not null then \n ") ;
	        		 strSQL.append("                                            case when to_number(jungwon) > cnt then 'true' \n ") ;
	        		 strSQL.append("                                                 else 'flase' \n ") ;
	        		 strSQL.append("                                             end \n ") ;
	        		 strSQL.append("             else 'true' \n ") ;
	        		 strSQL.append("         end as flag \n ") ;
	        		 strSQL.append("   from \n ") ;
	        		 strSQL.append("        ( \n ") ;
	        		 strSQL.append("          select notice_gubun jungwon, (select nvl(count(userid), 0) cnt from tz_event where tabseq="+v_tabseq+" and seq="+v_seq+") as cnt from tz_notice \n ") ;
	        		 strSQL.append("           where tabseq="+v_tabseq+" \n ") ;
	        		 strSQL.append("             and seq="+v_seq+" \n ") ;
	        		 strSQL.append("         ) \n ") ;
	        		 /* E : 이벤트 인원수 제한  확인 */
	    			 
	        		 ls = connMgr.executeQuery(strSQL.toString());
	        		 if(ls.next()) {
	        			isFlag = ls.getString("flag");
	                 }
	        		 ls.close();
	        		 
	        		 if(!isFlag.equals("true")){
	        			 
	        			 return isOk = -1;
	        			 
	        		 }else{
				        		 
			    	    	 strSQL.setLength(0);
			    	    	 strSQL.append(" insert into tz_event                                                                                    \n " );
			    	    	 strSQL.append("   (tabseq, seq, userid, inputdata1, indate, ldate, luserid )                                            \n " );
			    	    	 strSQL.append(" values                                                                                                  \n " );
			    	    	 strSQL.append("   ( ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), to_char(sysdate, 'YYYYMMDDHH24MISS'), ? )         \n " );
			    	    	 
			    	    	 preIdx = 1;
			    	    	 pstmt = connMgr.prepareStatement(strSQL.toString());
			    	    	 
			    	    	 pstmt.setInt   (preIdx++,  v_tabseq);
			    	    	 pstmt.setInt   (preIdx++,  v_seq);
			    	    	 pstmt.setString(preIdx++,  s_userid);
			    	    	 pstmt.setString(preIdx++,  v_rep_content);
			    	    	 pstmt.setString(preIdx++,  s_userid);
			    	    	 
			    	    	 isOk = pstmt.executeUpdate();
			    	    	 if ( pstmt != null ) { pstmt.close(); }
			    	 }
		         }
    	     }
             
    		 if ( isOk > 0 ) { 
    			 connMgr.commit();
    		 } else { 
    			 connMgr.rollback();		   	
    		 }
    	 }
    	 catch ( Exception ex ) { 
    		 ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
    		 throw new Exception("sql - > " + strSQL.toString() + "\r\n" + ex.getMessage() );
    	 }
    	 finally { 
    		 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
    		 if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
    		 if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
             if ( connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }    		 
    	 }
    	 return isOk;
     }
     
     public int insertMemoData2(RequestBox box) throws Exception { 
    	 DBConnectionManager connMgr     = null;
    	 ListSet             ls      = null;
    	 PreparedStatement   pstmt   = null;
    	 
    	 StringBuffer strSQL = null;
    	 int isOk   = 0;
    	 int preIdx = 1;
    	 
    	 //String v_event  = box.getString("p_event");
    	 int    v_tabseq = box.getInt("p_tabseq");
    	 int    v_seq    = box.getInt("p_seq");
    	 //int    v_comseq = 0;
    	 int    v_sseq     = 0;
    	 
    	 String s_userid = box.getSession("userid");              // 작성자 아이디
    	 String s_name   = box.getSession("name");                // 작성자
    	 String v_rep_content = box.getString("p_rep_content");   //응모내용
    	 String isFlag        = "";
    	 
    	 Vector v_select = box.getVector("p_select");
    	 try { 
    		 
    		 connMgr = new DBConnectionManager();
    		 connMgr.setAutoCommit(false);
    		 
    		 strSQL = new StringBuffer();
    		 
    		 /* S : 이벤트 중복신청 확인  */
    		 strSQL.append("select nvl(count(userid),0) cnt from tz_event where tabseq = "+v_tabseq+"  and seq = "+v_seq+" and userid= " + StringManager.makeSQL(s_userid) + " \n");
    		 /* E : 이벤트 중복신청 확인  */
    		 
    		 ls = connMgr.executeQuery(strSQL.toString());
    		 if(ls.next()) {
    			 v_sseq = ls.getInt("cnt");
    		 }
    		 ls.close();
    		 
    		 if(v_sseq > 0){ 
    			 
    			 return isOk = -3;
    			 
    		 }else{
    			 /* S : 이벤트 응모기간  확인 */
    			 strSQL.setLength(0);
    			 strSQL.append("select \n ") ;
    			 strSQL.append("        case when startdate is not null and enddate is not null then \n ") ;
    			 strSQL.append("                                                                      case when to_char(sysdate, 'yyyymmdd') between trim(startdate) and trim(enddate) then 'true' \n ") ;
    			 strSQL.append("                                                                           else 'flase' \n ") ;
    			 strSQL.append("                                                                       end \n ") ;
    			 strSQL.append("             when startdate is not null and enddate is null     then \n ") ;
    			 strSQL.append("                                                                      case when to_char(sysdate, 'yyyymmdd') >= trim(startdate) then 'true' \n ") ;
    			 strSQL.append("                                                                           else 'false' \n ") ;
    			 strSQL.append("                                                                       end \n ") ;
    			 strSQL.append("             when startdate is null     and enddate is not null then \n ") ;
    			 strSQL.append("                                                                      case when to_char(sysdate, 'yyyymmdd') <= trim(enddate) then 'true' \n ") ;
    			 strSQL.append("                                                                           else 'false' \n ") ;
    			 strSQL.append("                                                                       end \n ") ;
    			 strSQL.append("             else 'true' \n ") ;
    			 strSQL.append("        end as flag \n ") ;
    			 strSQL.append("   from \n ") ;
    			 strSQL.append("        ( \n ") ;
    			 strSQL.append("          select startdate, enddate, notice_gubun jungwon from tz_notice \n ") ;
    			 strSQL.append("           where tabseq="+v_tabseq+" \n ") ;
    			 strSQL.append("             and seq="+v_seq+" \n ") ;
    			 strSQL.append("         ) \n ") ;
    			 /* E : 이벤트 응모기간  확인 */
    			 
    			 ls = connMgr.executeQuery(strSQL.toString());
    			 if(ls.next()) {
    				 isFlag = ls.getString("flag");
    			 }
    			 ls.close();
    			 
    			 if(!isFlag.equals("true")){
    				 
    				 return isOk = -2;
    				 
    			 } else {
    				 /* S : 이벤트 인원수 제한  확인 */
    				 strSQL.setLength(0);
    				 strSQL.append("select \n ") ;
    				 strSQL.append("        case when jungwon is not null then \n ") ;
    				 strSQL.append("                                            case when to_number(jungwon) > cnt then 'true' \n ") ;
    				 strSQL.append("                                                  else 'flase' \n ") ;
    				 strSQL.append("                                             end \n ") ;
    				 strSQL.append("             else 'true' \n ") ;
    				 strSQL.append("         end as flag \n ") ;
    				 strSQL.append("   from \n ") ;
    				 strSQL.append("        ( \n ") ;
    				 strSQL.append("          select notice_gubun jungwon, (select nvl(count(userid), 0) cnt from tz_event where tabseq="+v_tabseq+" and seq="+v_seq+") as cnt from tz_notice \n ") ;
    				 strSQL.append("           where tabseq="+v_tabseq+" \n ") ;
    				 strSQL.append("             and seq="+v_seq+" \n ") ;
    				 strSQL.append("         ) \n ") ;
    				 /* E : 이벤트 인원수 제한  확인 */
    				 
    				 ls = connMgr.executeQuery(strSQL.toString());
    				 if(ls.next()) {
    					 isFlag = ls.getString("flag");
    				 }
    				 ls.close();
    				 
    				 if(!isFlag.equals("true")){
    					 
    					 return isOk = -1;
    					 
    				 }else{
    					 
    					 StringTokenizer st = null;	 
    					 String v_tokens  = "";
    					 String v_subjnm = "";
    					 String v_month = "";
    					 v_rep_content = "";
    					 
    					 for ( int i = 0; i < v_select.size(); i++ ) { 
    						 v_tokens = (String)v_select.elementAt(i); 
    						 st = new StringTokenizer(v_tokens,"|");
    						 v_month  = (String)st.nextToken();
    						 v_subjnm = (String)st.nextToken();
    						 v_rep_content += v_month+"&nbsp;&nbsp;"+v_subjnm+"<br>";         
    					 }
    					 v_rep_content.substring(0, v_rep_content.length()-4);
    					 
    					 strSQL.setLength(0);
    					 strSQL.append(" insert into tz_event                                                                                    \n " );
    					 strSQL.append("   (tabseq, seq, userid, inputdata1, indate, ldate, luserid )                                            \n " );
    					 strSQL.append(" values                                                                                                  \n " );
    					 strSQL.append("   ( ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), to_char(sysdate, 'YYYYMMDDHH24MISS'), ? )         \n " );
    					 
    					 preIdx = 1;
    					 pstmt = connMgr.prepareStatement(strSQL.toString());
    					 
    					 pstmt.setInt   (preIdx++,  v_tabseq);
    					 pstmt.setInt   (preIdx++,  v_seq);
    					 pstmt.setString(preIdx++,  s_userid);
    					 pstmt.setString(preIdx++,  v_rep_content);
    					 pstmt.setString(preIdx++,  s_userid);
    					 
    					 isOk = pstmt.executeUpdate();
    					 if ( pstmt != null ) { pstmt.close(); }
    				 }
    			 }
    		 }
    		 
    		 if ( isOk > 0 ) { 
    			 connMgr.commit();
    		 } else { 
    			 connMgr.rollback();		   	
    		 }
    	 }
    	 catch ( Exception ex ) { 
    		 ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
    		 throw new Exception("sql - > " + strSQL.toString() + "\r\n" + ex.getMessage() );
    	 }
    	 finally { 
    		 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
    		 if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
    		 if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    		 if ( connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }    		 
    	 }
    	 return isOk;
     }
     
     /**
      * 이벤트 댓글 삭제
      * @param box
      * @return
      * @throws Exception
      */
     public int deleteMemoData(RequestBox box) throws Exception { 
    	 DBConnectionManager connMgr     = null;
    	 PreparedStatement   pstmt   = null;
    	 StringBuffer strSQL = null;
    	 
    	 int isOk = 0;
    	 int preIdx = 1;
    	 
    	 int v_tabseq  = box.getInt("p_tabseq");
    	 int v_seq  = box.getInt("p_seq");
    	 int v_comseq = box.getInt("p_comseq");
    	 String v_event = "";
    	 String s_userid = box.getSession("userid");
    	 
    	 try { 
    		 connMgr = new DBConnectionManager();
    		 connMgr.setAutoCommit(false);
    		 
    		 strSQL = new StringBuffer();
    		 
    		 strSQL.append( " delete from tz_event                                   \n " );
    		 strSQL.append( "  where tabseq = ? and seq = ? and userid = ?           \n " );
    		 
    		 pstmt = connMgr.prepareStatement(strSQL.toString());
    		 preIdx = 1;
    		 
    		 pstmt.setInt(preIdx++, v_tabseq);
    		 pstmt.setInt(preIdx++, v_seq);
    		 pstmt.setString(preIdx++, s_userid);
    		 
    		 isOk = pstmt.executeUpdate();
    		 if ( pstmt != null ) { pstmt.close(); }
    		 if ( isOk > 0 ) { 
    			 connMgr.commit();
    		 } else { 
    			 connMgr.rollback();		   	
    		 }
    		 
    	 }
    	 catch ( Exception ex ) { 
    		 ErrorManager.getErrorStackTrace(ex, box, strSQL.toString() + "\r\n");
    		 throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
    	 }
    	 finally { 
    		 if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
    		 if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
             if ( connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }    	
    	 }
    	 return isOk;
     }
     
     /**
      *이벤트 댓글 리스트 
      * @param box
      * @return
      * @throws Exception
      */
     public ArrayList selectMemoList(RequestBox box) throws Exception { 
    	 DBConnectionManager connMgr = null;
    	 ListSet             ls      = null;
    	 ArrayList           list    = null;
    	 DataBox             dbox    = null;
    	 StringBuffer strSQL         = null;
    	 
    	 //int v_tabseq = box.getInt("p_tabseq");
    	 int v_tabseq = box.getInt("p_tabseq");
    	 int v_seq    = box.getInt("p_seq");
    	 int v_pageno = box.getInt("p_pageno");
    	 
    	 try { 
    		 connMgr = new DBConnectionManager();
    		 list = new ArrayList();
    		 
    		 strSQL = new StringBuffer();
    		 
    		 strSQL.append(" select tabseq, seq, userid, (select name from tz_member where userid=a.userid) name, inputdata1, ldate \n ");
    		 strSQL.append("   from tz_event a \n ");
    		 strSQL.append("  where tabseq = " + v_tabseq + " \n ");
    		 strSQL.append("    and seq = " + v_seq + " \n ");
    		 strSQL.append("  order by ldate desc  ");
    		 
    		 ls = connMgr.executeQuery(strSQL.toString());
    		 
    		 while ( ls.next() ) { 
    			 dbox = ls.getDataBox();
    			 list.add(dbox);
    		 }
    	 }
    	 catch ( Exception ex ) { 
    		 ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
    		 throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
    	 }
    	 finally { 
    		 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
    		 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	 }
    	 return list;
     }
     
     public ArrayList SelectSubjList(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         ArrayList           list    = null;
         String sql  = "";
         DataBox             dbox    = null;

         String v_searchtext = box.getString("p_searchtext");
         String s_grcode = box.getSession("grcode");
         String v_grgubun  = box.getString("p_grgubun");
         try { 

             sql  = "\n select 'S' subjtype, a.upperclass, b.classname, a.isonoff, get_codenm('0004',a.isonoff) as codenm ";
             sql += "\n      , a.subj, a.subjnm, a.isuse ";
             sql += "\n from   tz_subj a,  tz_subjatt b ";
             sql += "\n where  a.subjclass  = b.subjclass ";
             sql += "\n and    a.isuse     = 'Y' ";
             sql += "\n and    a.isonoff   <> 'RC' ";
             
             if ( !v_searchtext.equals("") ) { 
             	sql += "\n and lower(a.subjnm) like  " +SQLString.Format("%"+v_searchtext.toLowerCase()+"%");
             }
             
             if(v_grgubun.equals("A")){
            	 sql += "\n and a.upperclass in ('A02','A04') ";
             }else if(v_grgubun.equals("B")){
            	 sql += "\n and a.upperclass in ('A02') ";
             }
             
             connMgr = new DBConnectionManager();
             list = new ArrayList();
             ls = connMgr.executeQuery(sql);

             while ( ls.next() ) { 
             	dbox = ls.getDataBox();
             	list.add(dbox);
             }
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return list;
     }
     
     public String convertHtml(ArrayList list) throws Exception { 
         String result  = "";
         DataBox             dbox    = null;
         int i = 0;
         
         try { 
             result += "<table width='100%'  border='0' cellspacing='0' cellpadding='0' id='subjList'>";
             result += "<tr><td width='80' class='op_tit'>교육월</td><td width='400' class='op_tit'>제목</td><td width='55' class='op_tit'>선택</td></tr>";
        	 for(i=0; i<list.size(); i++){
        		 dbox = (DataBox)list.get(i);
        		 result += "<td class='op_c'><select name='p_month'><option value='5월'>5월</option><option value='6월'>6월</option><option value='7월'>7월</option><option value='8월'>8월</option></select></td>";
        		 result += "<td class='op_l'>"+dbox.getString("d_subjnm")+"</td>";
        		 result += "<td class='op_c'><input type='button' value='선택' onclick='javascript:addRow("+i+"&#44;&#39;"+dbox.getString("d_subjnm")+"&#39;);'></td></tr>";
        		 result += "<tr><td colspan='3' class='t_dot-1'></td></tr>";
        	 }
        	 result += "</table>";  

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex);
         } 

         return result;
     }
 	
}
