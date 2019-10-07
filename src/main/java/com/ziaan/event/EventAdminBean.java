// **********************************************************
//  1. 제      목: 이벤트관리
//  2. 프로그램명: EventAdminBean.java
//  3. 개      요: 이벤트관리
//  4. 환      경: JDK 1.5
//  5. 버      젼: 1.0
//  6. 작      성: 
//  7. 수      정:
// **********************************************************
package com.ziaan.event;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.homepage.NoticeData;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class EventAdminBean { 
	
	private ConfigSet config;
    private int row; 
    private int adminrow; 
	
    public EventAdminBean() { 
	    try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );                  //이 모듈의 페이지당 row 수를 셋팅한다
            adminrow = Integer.parseInt(config.getProperty("page.bulletin.adminrow") );        //이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }


    /**
     * 이벤트 리스트 조회
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList SelectList(RequestBox box) throws Exception { 

        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList   list    = null;
        DataBox dbox        = null;        
        StringBuffer strSQL = null;
        
        int v_tabseq = box.getInt("p_tabseq");
        int v_pageno        = box.getInt("p_pageno");
        
		String v_searchtext = box.getString("p_searchtext");
		String v_search     = box.getString("p_search");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            strSQL = new StringBuffer();
            
            strSQL.append("select seq, adtitle, adname, addate, useyn, cnt \n ") ;
            strSQL.append("  from TZ_NOTICE \n ") ;
            strSQL.append(" where tabseq = "+v_tabseq + " \n ");
            
            if ( !v_searchtext.equals("") ) {                                //    검색어가 있으면
                if ( v_search.equals("adtitle") ) {                          //    제목으로 검색할때
                	strSQL.append(" and adtitle like " + StringManager.makeSQL("%" + v_searchtext + "%"));
                } else if ( v_search.equals("adcontents") ) {                //    내용으로 검색할때
                	strSQL.append(" and adcontent like " + StringManager.makeSQL("%" + v_searchtext + "%"));
                } else if ( v_search.equals("aduserid") ) {                  //    아이디로 검색할때
                	strSQL.append(" and aduserid  =  " + StringManager.makeSQL( v_searchtext ));
                } else if ( v_search.equals("adname") ) {                    //    작성자로 검색할때
                	strSQL.append(" and adname    like " + StringManager.makeSQL("%" + v_searchtext + "%"));
                } else if ( v_search.equals("addate") ) {                    //    작성일로 검색할때
                	strSQL.append(" and SUBSTR(addate, 1, 8) <= " + StringManager.makeSQL( StringManager.replace(v_searchtext, ".", "")));
                }
            }
            
            strSQL.append(" order by seq desc \n ") ;
            
            ls = connMgr.executeQuery(strSQL.toString());

			ls.setPageSize(adminrow);                       // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                    // 현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();       // 전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();       // 전체 row 수를 반환한다
            
            while ( ls.next() ) { 
				dbox = ls.getDataBox();
				dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(adminrow));
				list.add(dbox);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
            throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
     * 이벤트 상세 보기
     * @param box
     * @return
     * @throws Exception
     */
    public DataBox selectView(RequestBox box) throws Exception { 
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		StringBuffer strSQL = null;
		NoticeData data = null;
		DataBox dbox = null;

        int v_tabseq = box.getInt("p_tabseq");
		int v_seq = box.getInt("p_seq");
		String v_process = box.getString("p_process");

		try { 
			connMgr = new DBConnectionManager();
			
			strSQL = new StringBuffer();
			
			strSQL.append(" select adname, aduserid, addate, startdate, enddate, useyn, adtitle, adcontent, type, isall, notice_gubun, gubun, get_codenm('0110',gubun) gubunnm, loginyn \n ") ;
			strSQL.append("   from TZ_NOTICE \n ") ;
			strSQL.append("  where tabseq = " + v_tabseq + " \n " ) ;
			strSQL.append("    and seq = " + v_seq) ;

			ls = connMgr.executeQuery(strSQL.toString());

			while ( ls.next() ) { 
				dbox = ls.getDataBox();
			}
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
     * 이벤트 등록
     * @param box
     * @return
     * @throws Exception
     */
	public int insertEvent(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		PreparedStatement   pstmt   = null;
		
		StringBuffer strSQL = null;
		int isOk  = 0;
		int v_seq = 0;
		int preIdx = 1;

        int    v_tabseq       = box.getInt("p_tabseq");
        String v_useyn        = box.getString("p_use");         // 사용유무
		String v_startdate    = box.getString("p_startdate");   // 이벤트 시작일
		String v_enddate      = box.getString("p_enddate");     // 이벤트 종료일
		String v_adtitle      = box.getString("p_adtitle");     // 제목
		String v_content      = box.getString("p_content");     // 내용
		String s_userid       = box.getSession("userid");       // 작성자 아이디
		String s_name         = box.getSession("name");         // 작성자
		String v_type         = box.getString("p_type");        // 댓글 표시여부
		String v_isall        = box.getString("p_isall");                  // 댓글입력폼 표시여부
		String v_notice_gubun = box.getString("p_notice_gubun");        // 이벤트 마감인원
		String v_gubun        = box.getString("p_gubun");               // 이벤트 구분
		String v_loginyn      = box.getString("p_loginyn");               // 입력데이터통계여부
		
		
		try { 

		   connMgr = new DBConnectionManager();
		   connMgr.setAutoCommit(false);
		   strSQL = new StringBuffer();

		   strSQL.append(" select nvl(max(seq), 0)+1 from TZ_NOTICE where tabseq = " + v_tabseq );
		   ls = connMgr.executeQuery(strSQL.toString());
		   
		   if ( ls.next() ) { 
			   v_seq = ls.getInt(1);
		   }
		   
		   strSQL.setLength(0);
		   
		   strSQL.append(" insert into tz_notice (                                           \n ");
		   strSQL.append("   tabseq, seq, startdate, enddate                                 \n ");
		   strSQL.append(" , addate, adtitle, adcontent, aduserid                            \n ");
		   strSQL.append(" , adname, useyn , luserid, ldate                                  \n ");
		   strSQL.append(" , cnt, type, notice_gubun, gubun, isall, loginyn )                \n ");
		   strSQL.append(" values (                                                          \n ");
		   strSQL.append("   ?, ?, ?, ?                                                      \n ");
		   strSQL.append(" , to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, empty_clob(), ?        \n ");
		   strSQL.append(" , ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')                   \n ");
		   strSQL.append(" , ?, ?, ?, ?, ?, ? )                                              \n ");
		   
		   preIdx = 1;
           pstmt = connMgr.prepareStatement(strSQL.toString());
           
           pstmt.setInt   (preIdx++,  v_tabseq);
           pstmt.setInt   (preIdx++,  v_seq);
           pstmt.setString(preIdx++,  v_startdate);
           pstmt.setString(preIdx++,  v_enddate);

           /* addate */
           pstmt.setString(preIdx++,  v_adtitle);
           /* content */
           pstmt.setString(preIdx++,  s_userid);
           
           pstmt.setString(preIdx++,  s_name);
           pstmt.setString(preIdx++,  v_useyn);
           pstmt.setString(preIdx++,  s_userid);
           /* sysdate */  
           pstmt.setInt   (preIdx++,  0);
           pstmt.setString(preIdx++,  v_type);
           pstmt.setString(preIdx++,  v_notice_gubun);
           pstmt.setString(preIdx++,  v_gubun);
           pstmt.setString(preIdx++,  v_isall);
           pstmt.setString(preIdx++,  v_loginyn);
           
           isOk = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }
           
           strSQL.setLength(0);
           
           strSQL.append("select adcontent from TZ_NOTICE where tabseq = " + v_tabseq + " and seq = " + v_seq);
           connMgr.setOracleCLOB(strSQL.toString(), v_content);
           
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
		}
		return isOk;
	}
	
	/**
	 * 이벤트 수정
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int updateEvent(RequestBox box) throws Exception { 
		DBConnectionManager connMgr = null;
		PreparedStatement   pstmt   = null;
		StringBuffer strSQL = null;
		int isOk = 0;
		
		int v_tabseq = box.getInt("p_tabseq");
		int v_seq    = box.getInt("p_seq");
		
		String v_startdate    = box.getString("p_startdate");
		String v_enddate      = box.getString("p_enddate");
		String v_adtitle      = box.getString("p_adtitle");
		String v_adcontent    = box.getString("p_adcontent");
		String v_useyn        = box.getString("p_use");
		String v_type         = box.getString("p_type");
		String v_isall        = box.getString("p_isall");
		String v_notice_gubun = box.getString("p_notice_gubun");
		String v_gubun        = box.getString("p_gubun");
		String v_loginyn      = box.getString("p_loginyn");     //입력데이터 통계 사용여부
		String s_userid       = box.getSession("userid");

		int preIdx = 1;
		
		
		try { 
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			strSQL = new StringBuffer();
			
			strSQL.append(" update tz_notice set  useyn = ? ");
			strSQL.append("                     , adtitle = ? ");
			strSQL.append("                     , adcontent = empty_clob() ");
			strSQL.append("                     , startdate = ? ");
			strSQL.append("                     , enddate = ? ");
			strSQL.append("                     , luserid = ? ");
			strSQL.append("                     , ldate = to_char(sysdate, 'YYYYDDMMHH24MISS') ");
			strSQL.append("                     , type = ? ");
			strSQL.append("                     , isall = ? ");
			strSQL.append("                     , notice_gubun = ? ");
			strSQL.append("                     , gubun = ? ");
			strSQL.append("                     , loginyn = ? ");
			strSQL.append(" where tabseq = ? ");
			strSQL.append("   and seq = ? ");
			
			pstmt = connMgr.prepareStatement(strSQL.toString());
			
			preIdx = 1;
			
			pstmt.setString(preIdx++, v_useyn);
			pstmt.setString(preIdx++, v_adtitle);
			//pstmt.setString(preIdx++, v_adcontent);
			pstmt.setString(preIdx++, v_startdate);
			pstmt.setString(preIdx++, v_enddate);
			pstmt.setString(preIdx++, s_userid);
			//sysdate
			pstmt.setString(preIdx++, v_type);
			pstmt.setString(preIdx++, v_isall);
			pstmt.setString(preIdx++, v_notice_gubun);
			pstmt.setString(preIdx++, v_gubun);
			pstmt.setString(preIdx++, v_loginyn);
			pstmt.setInt(preIdx++,    v_tabseq);
			pstmt.setInt(preIdx++,    v_seq);
			
			isOk = pstmt.executeUpdate();
			if ( pstmt != null ) { pstmt.close(); }
			
			strSQL.setLength(0);
			strSQL.append( " select adcontent from tz_notice where seq = " + v_seq + " and tabseq = " + v_tabseq );
            connMgr.setOracleCLOB(strSQL.toString(), v_adcontent);           
			
			if ( isOk > 0 ) { 
				connMgr.commit();
				isOk =1;
			}
			else{ 
				connMgr.rollback();
				isOk =0;
			}
		}
		
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}

	/**
	 * 이벤트 삭제
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int deleteEvent(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;
		StringBuffer strSQL = null;
		ListSet             ls      = null;

		int isOk = 0;
		int preIdx = 1;

        int v_tabseq = box.getInt("p_tabseq");
		int v_seq  = box.getInt("p_seq");
		int v_usercnt = 0;

		try { 
			connMgr = new DBConnectionManager();
			strSQL = new StringBuffer();

			/* S : 해당 이벤트에 참여자가 있는지 확인 */
			strSQL.append(" select count(userid) as usercnt \n ") ;
			strSQL.append("   from tz_event \n ") ;
			strSQL.append("  where tabseq = " + v_tabseq +" \n "); 
			strSQL.append("    and seq = " + v_seq +" \n ");

			/* E : 해당 이벤트에 참여자가 있는지 확인 */
			
			ls = connMgr.executeQuery(strSQL.toString());
			
			if ( ls.next() ) { 
				v_usercnt = ls.getInt(1);
			}
			
			if(v_usercnt > 0){
	            isOk = -1 ; 
           	    return isOk;
			}
			
			strSQL.setLength(0);
			strSQL.append(" delete from TZ_NOTICE           ");
			strSQL.append("  where tabseq = ? and seq = ?  ");

			pstmt = connMgr.prepareStatement(strSQL.toString());
			preIdx = 1;
			
			pstmt.setInt(preIdx++, v_tabseq);
			pstmt.setInt(preIdx++, v_seq);
			
			isOk = pstmt.executeUpdate();
			if ( pstmt != null ) { pstmt.close(); }

		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString() + "\r\n");
			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}
    
    /**
    이벤트 참여자 리스트 조회
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList SelectParticipantList(RequestBox box) throws Exception { 

        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList   list    = null;
        DataBox dbox        = null;        
        String  sql         = "";
        
        int v_tabseq   = box.getInt("p_tabseq");
        String s_event = box.getString("s_event");
        
        String v_orderColumn    = box.getString       ("p_orderColumn"         );   // 정렬할 컬럼명
        String v_orderType      = box.getString       ("p_orderType"           );   // 정렬할 순서

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = " select tabseq, seq, userid, name, adtitle, inputdata1, nvl(prizeyn,'N') prizeyn, indate, ldate, nvl(orders,'') orders, handphone, compnm            \n"
            	+ "   from (	                                                                                            \n"
                + "          select a.tabseq, a.seq, a.userid, get_name(a.userid) name, adtitle,	                        \n"
            	+ "                 inputdata1, inputdata2, inputdata3, inputdata4, inputdata5, prizeyn, a.indate, a.ldate, orders, handphone, get_compnm(comp) compnm	\n"	            	
            	+ "                 from tz_event a , tz_notice b, tz_member c															\n"	
            	+ "          where a.tabseq = b.tabseq                                                               		\n"
                + "            and a.seq    = b.seq                                                               		    \n"
                + "            and a.userid = c.userid(+)                                                       		        \n"
                + "        )                                                                                                \n"
                + "  where 1 = 1                                                                                            \n";
            
            if(!box.getString("p_tabseq").equals("")){
            	sql += "   and tabseq = " + v_tabseq + "                                                                    \n ";
            }
            
            if(!box.getString("s_event").equals("")){
                sql += "   and seq = " + s_event +                                                                        " \n ";
            }
            
	        if( v_orderColumn.equals("") ){
	        	sql += " order by seq desc, indate desc												                        \n";
	        } else { 
	        	sql += " order by " + v_orderColumn + v_orderType + " , indate desc                                         \n";
            }   
	        
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


   /**
    이벤트 당첨자 저장
    @param box      receive from the form object and session
    @return int		isOk
    */
     public int updatePrize(RequestBox box) throws Exception { 

 		DBConnectionManager connMgr     = null;
         PreparedStatement   pstmt   	= null;
         String  sql			= "";
         int isOk			= 0;
         int preIdx = 1;

         String s_userid    = box.getSession("userid");
         Vector v_checks	= box.getVector("p_checks");
         Vector v_orders    = box.getVector("p_orders");
         String v_temp = "";
         String v_tempArr[] = new String[v_checks.size()];
         String v_ordersTemp = "";

         try { 
             connMgr = new DBConnectionManager();

 			 sql  = " update tz_event set prizeyn=? , orders = ? , luserid = ?, ldate = to_char(sysdate,'YYYYMMDDHH24MISS') where tabseq = ? and seq = ? and userid = ? ";
             
             pstmt = connMgr.prepareStatement(sql);

             if ( v_checks != null ) { 
                 for ( int i = 0; i < v_checks.size() ; i++ ) {
                	 v_temp = (String)v_checks.elementAt(i);
                	 v_tempArr = v_temp.split(",");
                	 v_ordersTemp = (String)v_orders.elementAt(i);
                	 
                	 preIdx = 1;
                	 pstmt.setString (preIdx++,  "Y");
                	 if(!((String)v_orders.elementAt(i)).equals("")){
                	     pstmt.setInt    (preIdx++, Integer.parseInt(v_ordersTemp));                    
                	 }else{
                		 pstmt.setNull(preIdx++, java.sql.Types.NUMERIC);
                	 }
                     pstmt.setString (preIdx++,  s_userid);                    
                     pstmt.setInt    (preIdx++,  Integer.parseInt(v_tempArr[0]));
                     pstmt.setInt    (preIdx++,  Integer.parseInt(v_tempArr[1]));
                     pstmt.setString (preIdx++,  v_tempArr[2]);
                     isOk = pstmt.executeUpdate();
                 }
             }   
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return isOk;
     }
     
     /**
      * 이벤트 당첨자 삭제
      * @param box
      * @return
      * @throws Exception
      */
     public int deletePrize(RequestBox box) throws Exception { 

  		DBConnectionManager connMgr     = null;
          PreparedStatement   pstmt   	= null;
          String  sql			= "";
          int isOk			= 0;
          int preIdx = 1;

          String s_userid    = box.getSession("userid");
          Vector v_checks	= box.getVector("p_checks");
          Vector v_orders    = box.getVector("p_orders");
          String v_temp = "";
          String v_tempArr[] = new String[v_checks.size()];
          String v_ordersTemp = "";

          try { 
              connMgr = new DBConnectionManager();

  			 sql  = " update tz_event set prizeyn=? , orders = ? , luserid = ?, ldate = to_char(sysdate,'YYYYMMDDHH24MISS') where tabseq = ? and seq = ? and userid = ? ";
              
              pstmt = connMgr.prepareStatement(sql);

              if ( v_checks != null ) { 
                  for ( int i = 0; i < v_checks.size() ; i++ ) {
                 	 v_temp = (String)v_checks.elementAt(i);
                 	 v_tempArr = v_temp.split(",");
                 	 v_ordersTemp = (String)v_orders.elementAt(i);
                 	 
                 	 preIdx = 1;
                 	 pstmt.setString (preIdx++,  "N");
                 	 pstmt.setNull(preIdx++, java.sql.Types.NUMERIC);
                     pstmt.setString (preIdx++,  s_userid);                    
                     pstmt.setInt    (preIdx++,  Integer.parseInt(v_tempArr[0]));
                     pstmt.setInt    (preIdx++,  Integer.parseInt(v_tempArr[1]));
                     pstmt.setString (preIdx++,  v_tempArr[2]);
                     isOk = pstmt.executeUpdate();
                  }
              }   
          } catch ( Exception ex ) { 
              ErrorManager.getErrorStackTrace(ex, box, sql);
              throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
          } finally { 
              if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }

          return isOk;
      }
     
     /**
      * 이벤트 참여자 코멘트 삭제
      * @param box
      * @return
      * @throws Exception
      */
     public int deleteParticipant(RequestBox box) throws Exception { 
    	 
    	 DBConnectionManager connMgr     = null;
    	 PreparedStatement   pstmt   	= null;
    	 String  sql			= "";
    	 int isOk			= 0;
    	 int preIdx = 1;
    	 
    	 Vector v_checks	= box.getVector("p_checks");
         StringTokenizer st = null;
         String temp = "";
         String v_tabseq = "";
         String v_seq = "";
         String v_userid = "";
         
    	 try { 
    		 connMgr = new DBConnectionManager();
    		 
    		 sql  = " delete from tz_event where tabseq = ? and seq = ? and userid = ? ";
    		 
    		 pstmt = connMgr.prepareStatement(sql);
    		 
    		 if ( v_checks != null ) { 
    		     for (int i=0; i<v_checks.size();i++) {  
    			     temp = v_checks.elementAt(i).toString();
    	             st   = new StringTokenizer(temp, ",");
    				 v_tabseq = st.nextToken();
    				 v_seq    = st.nextToken();
    				 v_userid = st.nextToken();
    				 //v_prizeyn = st.nextToken();
    				 
    				 preIdx = 1;
    				 pstmt.setInt    (preIdx++,  Integer.parseInt(v_tabseq));
    				 pstmt.setInt    (preIdx++,  Integer.parseInt(v_seq));
    				 pstmt.setString (preIdx++,  v_userid);
    				 isOk = pstmt.executeUpdate();
    			 }
    		 }   
    	 } catch ( Exception ex ) { 
    		 ErrorManager.getErrorStackTrace(ex, box, sql);
    		 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	 } finally { 
    		 if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
    		 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	 }
    	 
    	 return isOk;
     }

     /**
      * 이벤트 진행현황
      * @param box
      * @return
      * @throws Exception
      */
     public ArrayList selectListEventStatus(RequestBox box) throws Exception { 

         DBConnectionManager	connMgr	= null;
         ListSet ls          = null;
         ArrayList   list    = null;
         DataBox dbox        = null;        
         StringBuffer strSQL = null;
         
         int v_tabseq = box.getInt("p_tabseq");

         try { 
             connMgr = new DBConnectionManager();
             list = new ArrayList();
             
             strSQL = new StringBuffer();
             
             strSQL.append("select distinct adtitle, tabseq, seq, useyn, gubun, loginyn, cnt, jungwon \n ") ;
             strSQL.append("     , case when to_number(cnt) < to_number(jungwon) or jungwon is null then status \n ") ;
             strSQL.append("            else '마감' \n ") ;
             strSQL.append("        end as status \n ") ;
             strSQL.append("  from \n ") ;
             strSQL.append("       ( \n ") ;
             strSQL.append("         select   adtitle, a.tabseq, a.seq, useyn, a.gubun, a.loginyn \n ") ;
             strSQL.append("                , (select nvl(count(userid),0) cnt from tz_event bb where a.tabseq=bb.tabseq and a.seq=bb.seq) as cnt \n ") ;
             strSQL.append("                , notice_gubun as jungwon \n ") ;
             strSQL.append("                , decode( \n ") ;
             strSQL.append("                          a.gubun,'A', \n ") ;
             strSQL.append("                          case when to_char(sysdate, 'yyyymmdd') between trim(startdate) and trim(enddate) then '진행중' \n ") ;
             strSQL.append("			                   when to_char(sysdate, 'yyyymmdd') < trim(startdate) then  '준비중' \n ") ;
             strSQL.append("                               when to_char(sysdate, 'yyyymmdd') > trim(enddate) then  '마감' \n ") ;
             strSQL.append("                               else '진행중' \n ") ;
             strSQL.append("                           end \n ") ;
             strSQL.append("                          ,'B', '당첨자발표' \n ") ;
             strSQL.append("                         )  as status \n ") ;
             strSQL.append("           from tz_notice a, tz_event b \n ") ;
             strSQL.append("          where a.tabseq=b.tabseq(+) \n ") ;
             strSQL.append("            and a.seq=b.seq(+) \n ") ;
             strSQL.append("       ) \n ") ;
             strSQL.append(" where tabseq = " + v_tabseq + " \n ") ;
             strSQL.append("   and useyn = 'Y' " + "  \n ") ;
             strSQL.append("   and gubun = 'A' " + " \n ") ;
             strSQL.append(" order by tabseq desc, seq desc \n ") ;
             
             ls = connMgr.executeQuery(strSQL.toString());

             while ( ls.next() ) { 
 				dbox = ls.getDataBox();
 				list.add(dbox);
             }

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
             throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return list;
     }     

     /**
      * 이벤트진행현황 - 입력데이터통계 상세보기
      * @param box
      * @return
      * @throws Exception
      */
     public ArrayList selectViewStatistics(RequestBox box) throws Exception { 
 		DBConnectionManager connMgr = null;
 		ListSet ls = null;
 		StringBuffer strSQL = null;
 		NoticeData data = null;
 		ArrayList list = null;
 		DataBox dbox = null;

         int v_tabseq = box.getInt("p_tabseq");
 		int v_seq = box.getInt("p_seq");
 		String v_process = box.getString("p_process");

 		try { 
 			connMgr = new DBConnectionManager();
 			list = new ArrayList();
 			
 			strSQL = new StringBuffer();
 			
 			strSQL.append(" select inputdata1, count(userid) cnt \n ") ;
 			strSQL.append("   from TZ_EVENT \n ") ;
 			strSQL.append("  where tabseq = " + v_tabseq + " \n " ) ;
 			strSQL.append("    and seq = " + v_seq + " \n ") ;
 			strSQL.append("  group by inputdata1 \n ") ;

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
 			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
 			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
 		}
 		return list;
 	}
}
