// **********************************************************
// 1. 제      목: 나의 질문방
// 2. 프로그램명: MyQnaServlet.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: 정은년
// 7. 수      정:
// 
// **********************************************************
package com.ziaan.study;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class MyQnaBean { 
    private ConfigSet config;
    private int row;

    public MyQnaBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }        
        
    }

    /**
    나의 질문방 학습관련 리스트
    @param box      receive from the form object and session
    @return ArrayList 리스트
    */
     public ArrayList SelectMyQnaStudyList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list1     = null;
        String sql1         = "";
        String wsql         = "";
        DataBox dbox        = null;

        String v_searchtext= box.getString("p_searchtext");
        String v_select    = box.getString("p_search");        
        String v_user_id   = box.getSession("userid");
        int    v_pageno    = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            if ( !v_searchtext.equals("") ) {      // 검색어가 있으면
                if ( v_select.equals("title") ) {     // 제목으로 검색할때
                    wsql = " and lower(b.title) like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                }
                else if ( v_select.equals("content") ) { // 내용으로 검색할때 // Oracle 9i 용
                    wsql = " and b.content like " + StringManager.makeSQL("%" + v_searchtext + "%");         
                }            
            }
                       
          sql1 = "select a.subj, a.year, a.subjseq, c.subjnm, b.tabseq, b.seq, b.title, b.content, b.upfile, b.indate, b.cnt,	       \n"
        	  + "  (select title from tz_board where refseq = b.seq  and tabseq=b.tabseq and levels='2' and rownum < 2 ) atitle          \n"
        	  + " from tz_bds a, tz_board b, tz_subj c																			       \n"
        	  + " where a.tabseq = b.tabseq																						       \n"
        	  + " and a.subj = c.subj																							       \n"
        	  + " and a.type = 'SQ'																								       \n"
        	  + " and levels='1'																								       \n"
        	  + " and b.luserid = "+ StringManager.makeSQL(v_user_id) + "														       \n"
              + " ORDER BY  indate desc";
          
            ls = connMgr.executeQuery(sql1);

            ls.setPageSize(row);                            // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                    // 현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();       // 전체 페이지 수를 반환한다
            int total_row_count  = ls.getTotalCount();      // 전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum",   new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount",  new Integer(row));

                list1.add(dbox);
            }          
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    } 
    
     
    /**
    나의 질문방 학습 상세보기
    @param box      receive from the form object and session
    @return ArrayList 리스트
    */
    public DataBox selectMyQnaStudy(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_tabseq     = box.getInt("p_tabseq");
        int v_seq        = box.getInt("p_seq");
		Vector realfileVector = new Vector();
		Vector savefileVector = new Vector();
		Vector fileseqVector  = new Vector();  
        
        try { 
            connMgr = new DBConnectionManager();   
            
            sql  = " select a.seq, a.userid, a.name, a.title, a.content, ( case when a.gadmin = 'ZZ' then 'ZZ' when a.gadmin = 'P1' then '강사' else '운영자' end) gadmin, a.gadmin gadmin_value, ";
            sql += "        a.indate, a.cnt, a.refseq, a.levels, a.position, a.isimport, a.isopen, b.fileseq, b.realfile, b.savefile,		";
            sql +="         (select count(realfile) from TZ_BOARDFILE where tabseq = a.tabseq and seq = a.seq) upfilecnt             		";         
            sql += " from TZ_BOARD a, TZ_BOARDFILE b                                                                                        ";
            sql += "  where a.tabseq = b.tabseq (+)                                                                                           ";
            sql += "    and a.seq    = b.seq (+)                                                                                              ";
            sql += "    and a.tabseq = " + v_tabseq;
            sql += "    and a.seq    = " + v_seq;
                                
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
            // -------------------   2003.12.25  변경     -------------------------------------------------------------------
                dbox = ls.getDataBox();
                realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
				fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));

            }

			if ( realfileVector 	 != null ) dbox.put("d_realfile", realfileVector);
			if ( savefileVector 	 != null ) dbox.put("d_savefile", savefileVector);
			if ( fileseqVector 	 != null ) dbox.put("d_fileseq", fileseqVector);
			
			            

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }       


    /**
    나의 질문방 사이트관련 리스트
    @param box      receive from the form object and session
    @return ArrayList 리스트
    */
     public ArrayList SelectMyQnaSiteList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list1     = null;
        String sql1         = "";
        String wsql         = "";
        DataBox dbox        = null;

        String v_searchtext= box.getString("p_searchtext");
        String v_search    = box.getString("p_search");        
        String v_user_id   = box.getSession("userid");
        int    v_pageno    = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            if ( !v_searchtext.equals("") ) {      // 검색어가 있으면
                if ( v_search.equals("title") ) {     // 제목으로 검색할때
                    wsql = " and lower(title) like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                }
                else if ( v_search.equals("content") ) { // 내용으로 검색할때 // Oracle 9i 용
                    wsql = " and content like " + StringManager.makeSQL("%" + v_searchtext + "%");         
                }              
            }

            sql1 ="  SELECT a.tabseq, a.seq , a.title, a.content, a.indate, a.upfile , a.cnt, isopen, hasanswer, userid, name, realfile, savefile  "
                 + " FROM   tz_center_board a "
                 + " WHERE   a.levels='1' " 
                 + "     and a.seq in (select t.seq from tz_center_board t where t.tabseq = a.tabseq and t.seq = a.seq and  t.userid=" +SQLString.Format(v_user_id) + " ) "
                 + wsql 
                 + " ORDER BY tabseq, seq desc ";  
            ls = connMgr.executeQuery(sql1);

            ls.setPageSize(row);                            // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                    // 현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();       // 전체 페이지 수를 반환한다
            int total_row_count  = ls.getTotalCount();      // 전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum",   new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount",  new Integer(row));

                list1.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }


    /**
    나의 질문방 사이트 상세보기
    @param box      receive from the form object and session
    @return ArrayList 리스트
    */
    public DataBox selectMyQnaSite(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_tabseq 	= box.getInt("p_tabseq");
        int v_seq 		= box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();

            sql = "select a.tabseq, a.seq, a.userid, a.name, a.title, a.content, a.indate, a.cnt, a.auserid, a.aname, a.adate, a.atitle, a.acontent, a.savefile, a.realfile ";
            sql += " from tz_center_board a";
            sql += " where a.tabseq = " + v_tabseq + " and a.seq = " +v_seq + ""; 

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
            }

			sql = "update tz_center_board set cnt = cnt + 1 where tabseq = " + v_tabseq + " and seq = " +v_seq; 
            connMgr.executeUpdate(sql);

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }


}