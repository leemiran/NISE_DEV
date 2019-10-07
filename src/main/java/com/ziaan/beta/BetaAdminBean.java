/**
*베타테스트의 운영자에게 빈클래스
*<p> 제목:BetaAdminBean.java</p> 
*<p> 설명:운영자에게 빈</p> 
*<p> Copyright: Copright(c)2004</p> 
*<p> Company: VLC</p> 
*@author 박준현
*@version 1.0
*/
package com.ziaan.beta;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.MailSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class BetaAdminBean { 
	private 	ConfigSet 	config;
    private 	int 		row;
	
	public BetaAdminBean() { 
		try { 
			config 			= new ConfigSet();
            row 			= Integer.parseInt(config.getProperty("page.bulletin.row") ); // 이 모듈의 페이지당 row 수를 셋팅한다
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
    
    

    /**
    HyunDai운영자화면 리스트
    @param box          receive from the form object and session
    @return ArrayList   운영자 리스트
    */
    public ArrayList selectListAdminhyundai(RequestBox box) throws Exception { 
    	DBConnectionManager connMgr 		= null;
        ListSet 			ls 				= null;
        ArrayList 			list 			= null;
        String 				sql 			= "";
        DataBox 			dbox			= null;
       
        String 				v_searchtext	= box.getString("p_searchtext");
        String 				v_search 		= box.getString("p_select");
		
		int 				v_pageno        = box.getInt("p_pageno");
		String 				v_gcode 		= box.getString("s_grcode");
        try { 
            connMgr 						= new DBConnectionManager();

            list 							= new ArrayList();
			
            sql = "SELECT DISTINCT a.userid, a.name, get_jikwinm(a.jikwi, a.comp) as jikwinm, a.handphone, a.comptel,   ";
			sql += "c.gadmin, a.email, a.orga_ename "  ;
			sql += "FROM tz_member a, tz_manager b, tz_grcodeman c   ";
			sql += "WHERE a.userid = b.userid  ";
			sql += "AND a.userid = c.userid and substr(b.comp,1,4) = '0101' ";
			sql += " and a.jikwi >= '28'";
			sql += "AND ( (substr(c.gadmin,1,2) = 'H1') OR (substr(c.gadmin,1,2) = 'A1') OR (substr(c.gadmin,1,2) = 'A2' ) )" ;
			
			if ( !v_gcode.equals("") && !v_gcode.equals("----") ) { 
				sql	 += "  and c.grcode   = " + SQLString.Format(v_gcode);
            }

            if ( !v_searchtext.equals("") ) { // 검색어가 있으면
            	// 이름으로 검색할때
                sql += " and a.name like " + StringManager.makeSQL("%" + v_searchtext + "%");
            }
            
            sql 	 += " order by jikwinm desc";
			
            ls 		= connMgr.executeQuery(sql);
			ls.setPageSize(row);             					// 페이지당 row 갯수를 세팅한다.
            ls.setCurrentPage(v_pageno);                    	// 현재페이지번호를 세팅한다.
            int 	total_page_count 	= ls.getTotalPage();	// 전체 페이지 수를 반환한다.
            int 	total_row_count		= ls.getTotalCount();   // 전체 row 수를 반환한다.

            while ( ls.next() ) { 
            	dbox 	= ls.getDataBox();
            	
				dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(row));
                
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
        	ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { 
            	try { 
            		ls.close(); 
            	} catch ( Exception e ) { 
            	} 
            }
            
            if ( connMgr != null ) { 
            	try { 
            		connMgr.freeConnection(); 
            	} catch ( Exception e10 ) { 
            	} 
            }
        }
        
        return list;
    }
    
    
    /**
    kia운영자화면 리스트
    @param box          receive from the form object and session
    @return ArrayList   운영자 리스트
    */
	public ArrayList selectListAdminkia(RequestBox box) throws Exception { 
        DBConnectionManager connMgr 		= null;
        ListSet 			ls 				= null;
        ArrayList 			list 			= null;
        String 				sql 			= "";
		DataBox 			dbox 			= null;
		        
        String 				v_searchtext 	= box.getString("p_searchtext");
        String 				v_search 		= box.getString("p_select");
		int 				v_pageno        = box.getInt("p_pageno");
		String 				v_gcode 		= box.getString("s_grcode");
		
        try { 
            connMgr = new DBConnectionManager();

            list 	= new ArrayList();
			
            sql 	=   " SELECT DISTINCT a.userid, a.name, get_jikwinm(a.jikwi, a.comp) as jikwinm, a.handphone, a.comptel,            ";
			sql 	 +=  "           c.gadmin, a.email, a.orga_ename                                                                     ";
			sql 	 +=  " FROM      tz_member a, tz_manager b, tz_grcodeman c                                                           ";
			sql 	 +=  " WHERE     a.userid = b.userid                                                                                 ";
			sql 	 +=  " AND       a.userid = c.userid and substr(b.comp,1,4) = '0102'                                                 ";
			sql 	 +=  " and       a.jikwi >= '28'                                                                                     ";
			sql 	 +=  " AND       ( (substr(c.gadmin,1,2) = 'H1') OR (substr(c.gadmin,1,2) = 'A1') OR (substr(c.gadmin,1,2) = 'A2' ) )";

			if ( !v_gcode.equals("") && !v_gcode.equals("----") ) { 
				sql += "  and c.grcode   = " + SQLString.Format(v_gcode);
            }

            if ( !v_searchtext.equals("") ) { // 검색어가 있으면
            	// 이름으로 검색할때
                sql += " and a.name like " + StringManager.makeSQL("%" + v_searchtext + "%");
            }
            
            sql 	 += " order by jikwinm desc ";
			
            ls 		= connMgr.executeQuery(sql);
			ls.setPageSize(row);             					// 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                    	// 현재페이지번호를 세팅한다.
            int 	total_page_count 	= ls.getTotalPage();    // 전체 페이지 수를 반환한다.
            int 	total_row_count 	= ls.getTotalCount();   // 전체 row 수를 반환한다.

            while ( ls.next() ) { 
            	dbox 	= ls.getDataBox();
				dbox.put("d_dispnum"	, new Integer(total_row_count - ls.getRowNum() + 1)	);
                dbox.put("d_totalpage"	, new Integer(total_page_count)						);
                dbox.put("d_rowcount"	, new Integer(row)									);
                
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
        	ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { 
            	try { ls.close(); } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
            	try { 
            		connMgr.freeConnection(); 
            	} catch ( Exception e10 ) { } 
            }
        }
        
        return list;
    }


	/*
	운영자에게 DB에 입력후 메일보내기
	@param box          receive from the form object and session
	@param v_gubun		베타테스트시스템의 구분명
	@raturn 메일발송에 성공하면 true를 리턴한다
	*/
	public boolean selectPds(RequestBox box) throws Exception { 
        DBConnectionManager connMgr 		= null;
        ListSet 			ls 				= null;
        String 				sql 			= "";
		String 				sql1 			= "";
		String 				sql2 			= "";
        DataBox 			dbox 			= null;
		boolean 			isMailed 		= false;
		PreparedStatement 	pstmt1 			= null; 
		int 				isOk1 			= 0;
        
		String 				v_toEmail 		= box.getString("p_toemail");
		String 				v_toCono 		= box.getString("p_cono");
		String 				v_mailTitle 	= box.getString("p_title");
		String 				v_mailContent 	= box.getString("p_content");
		String 				s_userid 		= box.getSession("userid");
		String 				v_gubun 		= "04";
		
        MailSet 			mset 			= new MailSet(box);	// 메일 세팅 및 발송
        
        try { 
	        connMgr 	= new DBConnectionManager();
			 
			// ----------------------   게시판 번호 가져온다 ----------------------------
			sql2 		= "select nvl(max(seq), 0) from tz_contact";
			ls 			= connMgr.executeQuery(sql2);
			ls.next();
			int v_seq 	= ls.getInt(1) + 1;
			ls.close();
			// ------------------------------------------------------------------------------------
	
			sql1 		=  "insert into TZ_CONTACT(seq, addate, queid, quetitle, quecontent, gubun)   ";
			sql1 		 += "            values (?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?)  ";
	
			pstmt1 		= connMgr.prepareStatement(sql1);
			pstmt1.setInt(1,v_seq);
			pstmt1.setString(2, s_userid);
			pstmt1.setString(3, v_mailTitle);
			pstmt1.setString(4, v_mailContent);
			pstmt1.setString(5, v_gubun);
			
			isOk1 		= pstmt1.executeUpdate();  
			
			if ( isOk1 == 1) { 
				isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent, "1", ""); 
			}
			
			ls.close();  
        }
        catch ( Exception ex ) { 
        	ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { 
            	try { 
            		ls.close();
            	} catch ( Exception e ) { }
            }
            
			if ( pstmt1 != null ) { 
				try { 
					pstmt1.close(); 
				} catch ( Exception e1 ) { } 
			}
			
            if ( connMgr != null ) { 
            	try { 
            		connMgr.freeConnection(); 
            	} catch ( Exception e10 ) { } 
            }
        }
		
        return isMailed;
    }   
  

    /**
        메일을 검색한다
    @param box          receive from the form object and session
    @return list     조회한 메일정보
    */
    public ArrayList selectMail(RequestBox box) throws Exception { 
        DBConnectionManager connMgr 		= null;
        PreparedStatement 	pstmt 			= null;        
        ListSet 			ls 				= null;
        ArrayList 			list 			= null;
        String 				sql 			= "";
        DataBox 			dbox 			= null;

        String 				v_touserid		= box.getString("p_touserid");
		String 				v_fromuserid 	= box.getString("p_formuserid");

        try { 
			list 	= new ArrayList();
			
            connMgr = new DBConnectionManager();

            sql     += " SELECT  Name                                           \n" ;             
            sql     += "     ,   Email                                          \n" ;
            sql     += "     ,   Orga_Ename                                     \n" ;
            sql     += "     ,   GET_JIKWINM(Jikwi, Comp) as Jikwinm            \n" ;
            sql     += "     ,   jikmunm                                        \n" ;
            sql     += "     ,   cono                                           \n" ;
            sql     += " FROM    Tz_Member                                      \n" ;  
            sql     += " WHERE   Userid  = " + SQLString.Format( v_touserid   )     ;
            sql     += " OR      Userid  = " + SQLString.Format( v_fromuserid )     ;

            ls 		= connMgr.executeQuery(sql);

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
            if ( ls != null ) { 
            	try { 
            		ls.close();
            	} catch ( Exception e ) { }
            }
			
            if ( connMgr != null ) { 
            	try { 
            		connMgr.freeConnection(); 
            	} catch ( Exception e10 ) { } 
            }
        }
        
        return list;
    }
}
