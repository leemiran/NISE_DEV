// **********************************************************
//  1. 제      목: 센터도우미 USER BEAN
//  2. 프로그램명: CenterDeskQnaBean.java
//  3. 개      요: CenterDeskQna Bean
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 조용준 2006.07. 11
//  7. 수      정: 
//  8. 내      용: 
// **********************************************************
package com.ziaan.centerdesk;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FreeMailBean;
import com.ziaan.library.ListSet;
import com.ziaan.library.Mailing;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SmsSendBean;
import com.ziaan.library.StringManager;

public class CustomersVoiceBean {
    private     ConfigSet   config;
    private     int         row;

    public CustomersVoiceBean() { 
        try { 
            config          = new ConfigSet();
            row             = Integer.parseInt(config.getProperty("page.bulletin.row") ); // 이 모듈의 페이지당 row 수를 셋팅한다.
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
    
    /**
    * 리스트화면 select
    * @param    box          receive from the form object and session
    * @return ArrayList  자료실 리스트
    * @throws Exception
    */
    public ArrayList selectBoardList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
        int v_pageno = box.getInt("p_pageno");
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "	select	tabseq, seq, title, userid, name, 				";
            sql	+= "		content, indate, refseq, levels, position, 			";
            sql += "		upfile, cnt, recomcnt, luserid, ldate, 				";
            sql += "		gubunA, gubunB, isopen, email, hasanswer,			";
            sql += "		realfile, savefile, gadmin, pwd, auserid, 			";
            sql += "		adate, acontent	, atitle			 			";
            sql += "	from tz_center_board                                                    ";
            sql += "	where	tabseq	= " + v_tabseq + "                                      ";
                

            if ( !v_searchtext.equals("") ) {                //    검색어가 있으면
                if ( v_search.equals("name") ) {              //    이름으로 검색할때
                    sql += " and name like " + StringManager.makeSQL("%" + v_searchtext + "%") + "";
                }
                else if ( v_search.equals("title") ) {        //    제목으로 검색할때
                    sql += " and title like " + StringManager.makeSQL("%" + v_searchtext + "%") + "";
                }
                else if ( v_search.equals("content") ) {     //    내용으로 검색할때
                    sql += " and content like " + StringManager.makeSQL("%" + v_searchtext + "%"); //   Oracle 9i 용
                }
            }            
            sql += " order by refseq desc, position asc                                           ";

            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                       //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);               //     현재페이지번호를 세팅한다.
            int totalpagecount = ls.getTotalPage();    //     전체 페이지 수를 반환한다
            int totalrowcount = ls.getTotalCount();    //     전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum",   new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(row));
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
   * 상세내용 select
   * @param box          receive from the form object and session
   * @return ArrayList   조회한 상세정보
   * @throws Exception
   */
   public DataBox selectBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;
        String              v_upcnt = "Y";

        int v_tabseq    = box.getInt("p_tabseq");
        int v_seq       = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();

            sql  = "	select	tabseq, seq, title, userid, name, 				";
            sql	+= "		content, indate, refseq, levels, position, 			";
            sql += "		upfile, cnt, recomcnt, luserid, ldate, 				";
            sql += "		gubunA, gubunB, isopen, email, hasanswer,			";
            sql += "		realfile, savefile, gadmin, pwd, auserid, 			";
            sql += "		adate, acontent, atitle				 			";
            sql += "	from tz_center_board                                                    ";
            sql += "	where	tabseq	= " + v_tabseq + " and seq = " + v_seq + "              ";
       
            ls = connMgr.executeQuery(sql);
            for ( int i = 0; ls.next(); i++ ) { 
                dbox = ls.getDataBox();
            }

            if ( !v_upcnt.equals("N") ) { 
                connMgr.executeUpdate("update tz_center_board set cnt = cnt + 1 where  tabseq = " + v_tabseq + " and seq = " + v_seq);
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return dbox;
    }    
    
    /**
    * 새로운 예약상담 내용 등록
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int Merge(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ConfigSet conf = new ConfigSet();
        Mailing mailing = null;
        ListSet rs1   = null;
        DataBox dbox  = null;
        PreparedStatement pstmt1  = null;
        String sql    = "";
        String sql1   = "";
        int isOk1     = 1;
        int v_seq     = 0;  // 등록될 게시판 일련번호... max값.

        String v_content    = box.getString("p_content").replace("\r\n","<br>");   ;
        String v_email	    = box.getString("p_email");
        String v_gubuna	    = box.getString("p_search1");
        String v_gubunb	    = box.getString("p_search2");
        String v_gubunc	    = box.getString("p_search3");
        String v_tel1	    = box.getString("p_tel1");
        String v_tel2	    = box.getString("p_tel2");
        String v_tel3	    = box.getString("p_tel3");
        String v_title      = box.getString("p_title");
        String v_realFile   = box.getRealFileName("p_file1");
        String v_saveFile   = box.getNewFileName("p_file1");
       
        String v_resdate    = box.getString("p_resdate").replaceAll("-","");
        String v_stime1	    = box.getString("p_stime1");
        String v_stime2	    = box.getString("p_stime2");
        String v_etime1	    = box.getString("p_etime1");
        String v_etime2	    = box.getString("p_etime2");  
      
        
        
        // 일반 회원이라면 테그를 쓰지 못한다. 
        if(box.getSession("gadmin").equals("ZZ"))
        {
            v_content = StringManager.replace(v_content,"\r\n","<br>");   ;
            v_content = StringManager.replace(v_content,"\r\n","<br>");   ;
        }
        
        
        String s_userid     = box.getSession("userid");
        String s_userNm     = box.getSession("name");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            //----------------------   게시판 번호 가져온다 ----------------------------
            
            sql = "select nvl(max(seq), 0)+1 seq from TZ_CENTER_RESBOARD ";
            
            rs1 = connMgr.executeQuery(sql);
            for ( int i = 0; rs1.next(); i++ ) { 
                dbox = rs1.getDataBox();
                v_seq = dbox.getInt("d_seq");
            }
            rs1.close();
            
            // ----------------------   게시판 table 에 입력  --------------------------
            sql1 =  "insert into tz_center_resboard values                      \n" +                    
                    "      (                                                    \n" +                                   
                    "       ?,                                              	\n" +   //seq
                    "       ?,                                                  \n" +   //userid
                    "       ?,                                                  \n" +   //name
                    "       ?,                                                  \n" +   //tel1
                    "       ?,                                                  \n" +   //tel2
                    "       ?,                                                  \n" +   //tel3
                    "       ?,                                                  \n" +   //email
                    "       ?,                                                  \n" +   //gubuna
                    "       ?,                                                  \n" +   //resdate
                    "       ?,                                                  \n" +   //stime
                    "       ?,                                                  \n" +   //etime
                    "       ?,                                       			\n" +   //content
                    "       to_char(sysdate, 'yyyymmddhh24miss'),               \n" +   //indate
                    "       'N',                                                \n" +   //ismeet
                    "       ?,                                                  \n" +   //gubunb
                    "       ?,                                                  \n" +   //gubunc
                    "       '',                                                 \n" +   //amane
                    "       '',                                                 \n" +   //adate
                    "       ?,                                       			\n" +   //acontent
                    "		?,													\n" +	//title
                    "       'Y',                                                 \n"   +
                    "       ?,                                       			\n" +   //acontent
                    "		? )													\n" ;

                    
                    
            pstmt1 = connMgr.prepareStatement(sql1);
            
            // 입력
            pstmt1.setInt(1, v_seq);    
            pstmt1.setString(2, s_userid);
            pstmt1.setString(3, s_userNm);
            pstmt1.setString(4, v_tel1);            
            pstmt1.setString(5, v_tel2);            
            pstmt1.setString(6, v_tel3);
            pstmt1.setString(7, v_email);
            pstmt1.setString(8, v_gubuna);
            pstmt1.setString(9, v_resdate);
            pstmt1.setString(10, v_stime1+v_stime2);            
            pstmt1.setString(11, v_etime1+v_etime2);
            pstmt1.setString(12, v_content);
            pstmt1.setString(13, v_gubunb);
            pstmt1.setString(14, v_gubunc);
            pstmt1.setString(15, "");
            pstmt1.setString(16, v_title);        
            pstmt1.setString(17, v_saveFile);    
            pstmt1.setString(18, v_realFile);    
            
            
            isOk1 = pstmt1.executeUpdate(); 
            
            if ( pstmt1 != null ) { pstmt1.close(); }
            
            if ( isOk1 > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }
        }

        catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk1;
    }    
    
    /**
    * 선택된 Qna 삭제 (사용자)
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt1 = null;
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 0;
        int isOk2 = 0;
        int isOkComment = 0;
        int isOkRecom = 0;

        int v_tabseq = box.getInt("p_tabseq");
        int v_seq = box.getInt("p_seq");


        // 답변 유무 체크(답변 있을시 삭제불가)
        

            try { 
                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);

                isOk1 = 1;
                isOk2 = 1;
                isOkComment = 1;
                isOkRecom = 1;
                sql1 = "delete TZ_CENTER_BOARD where tabseq = ? and seq = ? ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);
                pstmt1.setInt(2, v_seq);
                isOk1 = pstmt1.executeUpdate();
                
                if ( pstmt1 != null ) { pstmt1.close(); }
 
                if ( isOk1 > 0 && isOk2 > 0 ) { 
                    connMgr.commit();
                }

            }
            catch ( Exception ex ) { 
                connMgr.rollback();
                ErrorManager.getErrorStackTrace(ex, box,    sql1);
                throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
            }
            finally { 
                if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }        

        return isOk1*isOk2;

    }    
    
    public ArrayList selectCodeList(RequestBox box) throws Exception {

        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list1    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
        int v_pageno = box.getInt("p_pageno");
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");

        try { 
            connMgr = new DBConnectionManager();

            list1 = new ArrayList();

            sql  = "											 ";
            sql += "			select gubun, levels, code, codenm				 ";
            sql += "			from tz_code  							 ";
            sql += "			where gubun='0077'						 ";
            sql += "			order by code							 ";
                
                
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox(); 
                list1.add(dbox);
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
        return list1;    	    	
    }    
    

    /** 
    링크에 필요한 주일자 정보를 가져온다.
    @param RequestBox box
    @return DataBox
    */
    public DataBox selectWeekDate(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list1    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        
		Calendar gCal = Calendar.getInstance();
	
		String v_dyear	= Integer.toString(gCal.get(Calendar.YEAR));
		String v_dmonth = (gCal.get(Calendar.MONTH)<9)?"0"+Integer.toString(gCal.get(Calendar.MONTH)+1):Integer.toString(gCal.get(Calendar.MONTH)+1);
		String v_dday	= (gCal.get(Calendar.DATE)<10)?"0"+Integer.toString(gCal.get(Calendar.DATE)):Integer.toString(gCal.get(Calendar.DATE));

        try {

			String v_year	= box.getString("p_year").equals("")?v_dyear:box.getString("p_year");
			String v_month	= box.getString("p_month").equals("")?v_dmonth:box.getString("p_month");
			String v_day	= box.getString("p_day").equals("")?v_dday:box.getString("p_day");
		    
            connMgr = new DBConnectionManager();
            
            sql = "		select max(decode(no,1,chk,0)) bweek,substr(max(decode(no,1,datez,'')),0,4) byear, ";
			sql += "			substr(max(decode(no,1,datez,'')),5,2) bmonth, ";
			sql += "			max(decode(no,2,chk,0)) tweek,substr(max(decode(no,2,datez,'')),0,4) tyear, ";
			sql += "			substr(max(decode(no,2,datez,'')),5,2) tmonth, ";
			sql += "			max(decode(no,3,chk,0)) aweek,substr(max(decode(no,3,datez,'')),0,4) ayear, ";
			sql += "			substr(max(decode(no,3,datez,'')),5,2) amonth, ";
			sql += "			max(decode(no,4,chk,0)) cweek,substr(max(decode(no,4,datez,'')),0,4) cyear, ";
			sql += "			substr(max(decode(no,4,datez,'')),5,2) cmonth, ";
			sql += "			to_char(to_date('"+v_year+v_month+v_day+"','yyyymmdd'),'d') weekday ";			
			sql += "	from ( ";
			sql += "		select 0 grp, 1 no, to_char(add_months(last_day('"+v_year+v_month+"01'),-1),'yyyymm') datez, ";
			sql += "			to_char(to_date( ";
			sql += "							to_char( ";
			sql += "									add_months(last_day('"+v_year+v_month+"01'),-1),'yyyymmdd' ";
			sql += "									),'yyyymmdd' ";
			sql += "							),'w' ";
			sql += "					)+ ";
			sql += "			 		decode(sign( ";
			sql += "						      to_char(add_months(to_date('"+v_year+v_month+"01','yyyymmdd'),-1),'d') ";
			sql += "				            -to_char(to_date(to_char(add_months(last_day('"+v_year+v_month+"01'),-1),'yyyymmdd'),'yyyymmdd'),'d') ";
			sql += "		         ),1,1,0) chk ";
			sql += "		from dual ";
			sql += "		union all ";
			sql += "		select 0 grp, 2 no, to_char(last_day('"+v_year+v_month+"01'),'yyyymm'), ";
			sql += "			to_char(to_date( ";
			sql += "							to_char(";
			sql += "									last_day('"+v_year+v_month+"01'),'yyyymmdd'";
			sql += "									),'yyyymmdd'";
			sql += "							),'w'";
			sql += "					)+";
			sql += "			 		decode(sign(";
			sql += "						      to_char(to_date('"+v_year+v_month+"01','yyyymmdd'),'d')";
			sql += "				            -to_char(to_date(to_char(last_day('"+v_year+v_month+"01'),'yyyymmdd'),'yyyymmdd'),'d')";
			sql += "		         ),1,1,0) chk";
			sql += "		 from dual";
			sql += "		union all";
			sql += "		select 0 grp, 3 no, to_char(add_months(last_day('"+v_year+v_month+"01'),1),'yyyymm'),";
			sql += "			to_char(to_date(";
			sql += "							to_char(";
			sql += "									add_months(last_day('"+v_year+v_month+"01'),1),'yyyymmdd'";
			sql += "									),'yyyymmdd'";
			sql += "							),'w'";
			sql += "					)+";
			sql += "			 		decode(sign(";
			sql += "						      to_char(add_months(to_date('"+v_year+v_month+"01','yyyymmdd'),1),'d')";
			sql += "				            -to_char(to_date(to_char(add_months(last_day('"+v_year+v_month+"01'),1),'yyyymmdd'),'yyyymmdd'),'d')";
			sql += "		         ),1,1,0) chk";
			sql += "		 from dual";
			sql += "		 union all";
			sql += "		select 0 grp, 4 no, to_char(to_date('"+v_year+v_month+v_day+"','yyyymmdd'),'yyyymm'),";
			sql += "			 to_char(to_date(";
			sql += "						   to_char(";
			sql += "								    to_date('"+v_year+v_month+v_day+"','yyyymmdd'),'yyyymmdd'";
			sql += "								    ),'yyyymmdd'";
			sql += "							   ),'w'";
			sql += "					  )+";
			sql += "					   decode(sign(";
			sql += "						         to_char(to_date('"+v_year+v_month+"01','yyyymmdd'),'d')";
			sql += "				              -to_char(to_date(to_char(to_date('"+v_year+v_month+v_day+"','yyyymmdd'),'yyyymmdd'),'yyyymmdd'),'d')";
			sql += "		         ),1,1,0) chk";
			sql += "		 from dual";
			sql += "	)AA";
			sql += "	group by grp";
            //System.out.println(sql);
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
    주별 스케쥴 보기
    @param RequestBox box
    @return DataBox
    */
    public ArrayList selectScheduleWeek(RequestBox box) throws Exception {
	DBConnectionManager connMgr = null;
	DBConnectionManager connMgr1 = null;
        ListSet             ls      = null;
        ListSet             ls1      = null;
        ArrayList           list1    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        DataBox             dbox1    = null;
        
		Calendar gCal = Calendar.getInstance();
	
		String v_dyear	= Integer.toString(gCal.get(Calendar.YEAR));
		String v_dmonth = (gCal.get(Calendar.MONTH)<9)?"0"+Integer.toString(gCal.get(Calendar.MONTH)+1):Integer.toString(gCal.get(Calendar.MONTH)+1);
		String v_dday	= (gCal.get(Calendar.DATE)<10)?"0"+Integer.toString(gCal.get(Calendar.DATE)):Integer.toString(gCal.get(Calendar.DATE));

        try {        	
		    String v_userid = box.getSession("userid");

			connMgr = new DBConnectionManager();
			connMgr1 = new DBConnectionManager();
			
			list1 = new ArrayList();

			String v_year	= box.getString("p_year").equals("")?v_dyear:box.getString("p_year");
			String v_month	= box.getString("p_month").equals("")?v_dmonth:box.getString("p_month");
			String v_day	= box.getString("p_day").equals("")?v_dday:box.getString("p_day");
			String v_week	= box.getString("p_week").equals("")?"":box.getString("p_week");
			
			String v_byear	= "";
			String v_bmonth	= "";
			String v_ayear	= "";
			String v_amonth	= "";
			

			
		   //----------------------   현재 일자가 몇 주차 인지를 가져온다.----------------------------
			if (v_week.equals(""))	
			{            
               		   sql =  " select A.chk||'' chk";
			   sql += " from ( ";
			   sql += "		 select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate, ";
			   sql += "			to_char(to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)),'w')+ ";
			   sql += "			decode(sign( ";
			   sql += "				    to_char(to_date('"+v_year+v_month+"01','yyyymmdd'),'d') ";
			   sql += "		           -to_char(to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)),'d') ";
			   sql += "			    	   ),1,1,0) chk ";
			   sql += "		 from tz_member ";
			   sql += "		 where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual) ";
			   sql += "		)A ";
			   sql += " where a.chkdate=to_date('"+v_year+v_month+v_day+"','yyyymmdd') ";	
			   
            			ls = connMgr.executeQuery(sql);
            			for ( int i = 0; ls.next(); i++ ) { 
                			dbox = ls.getDataBox();
            			}			   		   
     
               			v_week = dbox.getString("d_chk");
			}
			
			sql  = "	select to_char(add_months(to_date('"+v_year+v_month+"01','yyyymmdd'),-1),'yyyy') byear, ";
			sql += "		to_char(add_months(to_date('"+v_year+v_month+"01','yyyymmdd'),-1),'mm') bmonth, ";
			sql += "		to_char(add_months(to_date('"+v_year+v_month+"01','yyyymmdd'),1),'yyyy') ayear, ";
			sql += "		to_char(add_months(to_date('"+v_year+v_month+"01','yyyymmdd'),1),'mm') amonth ";
			sql += "	from dual ";
			
			ls = connMgr.executeQuery(sql);
			while (ls.next()) {
				dbox = ls.getDataBox();
			}
			
			v_byear		= dbox.getString("d_byear");
			v_bmonth	= dbox.getString("d_bmonth");
			
			v_ayear	= dbox.getString("d_ayear");
			v_amonth	= dbox.getString("d_amonth");
			
			
			sql  = "	select chkdate1,to_char(to_date(chkdate1,'yyyymmdd'),'d') weekday, c.resdate	";
			sql += "	from (					";
			sql += "		select to_char(to_date('"+v_byear+v_bmonth+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)),'yyyymmdd') chkdate1 ";
			sql += "		from tz_member 			";
			sql += "		where rownum<=(select to_number(to_char(last_day('"+v_byear+v_bmonth+"01'),'dd')) from dual) ";
			sql += "		union all			";
			sql += "		select to_char(to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)),'yyyymmdd') chkdate1 ";
			sql += "		from tz_member			";
			sql += "		where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual)	";
			sql += "		union all			";
			sql += "		select to_char(to_date('"+v_ayear+v_amonth+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)),'yyyymmdd') chkdate1 ";
			sql += "		from tz_member			";
			sql += "		where rownum<=(select to_number(to_char(last_day('"+v_ayear+v_amonth+"01'),'dd')) from dual) ";
			sql += "		)A, 				";
			sql += "		(				";
			sql += "		select to_date('"+v_year+v_month+v_day+"','yyyymmdd')	";
			sql += "			-to_char(to_date('"+v_year+v_month+v_day+"','yyyymmdd'),'d') sdate,	";
			sql += "			to_date('"+v_year+v_month+v_day+"','yyyymmdd')				";
			sql += "			-to_char(to_date('"+v_year+v_month+v_day+"','yyyymmdd'),'d')+8 edate	";
			sql += "		from dual							";
			sql += "		)B,								";
			sql += "		(								";
			sql += "		select resdate							";
			sql += "		from tz_center_resdate						";			
			sql += "		where resdate>=to_char(sysdate,'yyyymmdd')			";			
			sql += "		)C								";
			sql += "	where a.chkdate1>=b.sdate						";
			sql += "		and a.chkdate1<=b.edate						";
			sql += "		and a.chkdate1=c.resdate(+)					";
			sql += "	order by a.chkdate1							";


            
            
            //----------------------   주별 스케쥴 보기 ----------------------------
            
            			ls1 = connMgr1.executeQuery(sql);
            			while ( ls1.next() ) { 
	                		dbox1 = ls1.getDataBox();
                			list1.add(dbox1);
            			}
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( connMgr1 != null ) { try { connMgr1.freeConnection(); } catch ( Exception e10 ) { } }
        }    
        
        return list1;        
    }     
    
    
    /*
    //일자정보를 주면 예약 정보를 출력한다.
    @param RequestBox box
    @return DataBox
    */
    public ArrayList selectResTime(RequestBox box) throws Exception {
	DBConnectionManager connMgr = null;
	ListSet             ls      = null;
        ArrayList           list1    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
		Calendar gCal = Calendar.getInstance();
	
		String v_dyear	= Integer.toString(gCal.get(Calendar.YEAR));
		String v_dmonth = (gCal.get(Calendar.MONTH)<9)?"0"+Integer.toString(gCal.get(Calendar.MONTH)+1):Integer.toString(gCal.get(Calendar.MONTH)+1);
		String v_dday	= (gCal.get(Calendar.DATE)<10)?"0"+Integer.toString(gCal.get(Calendar.DATE)):Integer.toString(gCal.get(Calendar.DATE));

        try {        	
		    String v_userid = box.getSession("userid");

			connMgr = new DBConnectionManager();
			
			list1 = new ArrayList();

			String v_year	= box.getString("p_year").equals("")?v_dyear:box.getString("p_year");
			String v_month	= box.getString("p_month").equals("")?v_dmonth:box.getString("p_month");
			String v_day	= box.getString("p_day").equals("")?v_dday:box.getString("p_day");
			String v_week	= box.getString("p_week").equals("")?"":box.getString("p_week");
			
			
			sql  = "	select rnum,									";
			sql += "		max(decode(rnum2,'00',stime,0)) m00,					";
			sql += "		max(decode(rnum2,'10',stime,0)) m10,					";
			sql += "		max(decode(rnum2,'20',stime,0)) m20,					";
			sql += "		max(decode(rnum2,'30',stime,0)) m30,					";
			sql += "		max(decode(rnum2,'40',stime,0)) m40,					";
			sql += "		max(decode(rnum2,'50',stime,0)) m50					";
			sql += "	from (										";
			sql += "		select rnum,rnum2,stime							";
			sql += "		from (									";
			sql += "			select * 							";
			sql += "			from 	(							";
			sql += "				select rnum, rnum2					";
			sql += "				from (							";
			sql += "					  select rnum					";
			sql += "					  from (					";
			sql += "						   select rownum rnum			";
			sql += "						   from tz_member			";
			sql += "						   where rownum<25			";
			sql += "						)a					";
			sql += "					  where rnum>=10 and rnum<=18			";
			sql += "					  )b1 ,						";
			sql += "					  (						";
			sql += "					  select decode((rnum-1),0,'00',to_char((rnum-1)*10)) rnum2	";
			sql += "					  from (					";
			sql += "						   select rownum rnum			";
			sql += "						   from tz_member			";
			sql += "						   where rownum<7			";
			sql += "						)a					";
			sql += "					  )b2						";
			sql += "				 )p2,							";
			sql += "				(select resdate, stime					";
			sql += "				from tz_center_resboard					";
			sql += "				where resdate = '"+v_year+v_month+v_day+"'		";			
			sql += "				) p3							";
			sql += "			where p2.rnum||p2.rnum2 = p3.stime(+)				";
			sql += "			)AA,								";
			sql += "			(								";
			sql += "			select resdate							";
			sql += "			from tz_center_resdate						";
			sql += "			where resdate ='"+v_year+v_month+v_day+"'			";
			sql += "				and resdate >=to_char(sysdate,'yyyymmdd')		";
			sql += "			)BB								";
			sql += "		)A0									";
			sql += "	group by rnum									";
			sql += "	order by rnum									";			


            //----------------------   주별 스케쥴 보기 ----------------------------
            
            			ls = connMgr.executeQuery(sql);
            			while ( ls.next() ) { 
	                		dbox = ls.getDataBox();
                			list1.add(dbox);
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
        return list1;        
    }    

    
    /**
     * 새로운 예약상담 내용 등록
     * @param box      receive from the form object and session
     * @return isOk    1:insert success,0:insert fail
     * @throws Exception
     */
      public int inserttutor(RequestBox box) throws Exception { 
         DBConnectionManager connMgr = null;
         ListSet rs1   = null;
         DataBox dbox  = null;
         PreparedStatement pstmt1  = null;
         String sql    = "";
         String sql1   = "";
         int isOk1     = 1;
         int v_seq     = 0;  // 등록될 게시판 일련번호... max값.

         /*
         String v_content    = box.getString("p_content").replace("\r\n","<br>");   ;
         String v_email	     = box.getString("p_email");
         String v_title	     = box.getString("p_title");
         String v_field     = box.getString("p_field");
         String v_name      = box.getString("p_name");
         String v_phone	     = box.getString("p_phone").replaceAll("-","");
         String v_realFile   = box.getRealFileName("p_file1");
         String v_saveFile   = box.getNewFileName("p_file1");
         */
         
         String v_field         = box.getString("p_field");                            //지원과정분야
         String v_name          = box.getString("p_name");                             //성명
         String v_birth_date         = box.getString("p_birth_date");                            //주민등록번호
         String v_job           = box.getString("p_job");                              //현직
         String v_address       = box.getString("p_address");                          //현주소
         String v_phone         = box.getString("p_phone");                            //연락처
         String v_email         = box.getString("p_email");                            //이메일
         String v_achievement   = box.getString("p_achievement");                      //최종학력
         String v_stu_exp       = box.getString("p_stu_exp");                          //온라인교육수강경험
         String v_tutor_exp     = box.getString("p_tutor_exp");                        //온라인교육튜터경험
         String v_career        = box.getString("p_career").replace("\r\n","<br>");    //주요경력(강의경력포함)
         String v_lecture_field = box.getString("p_lecture_field");                    //강의분야
         String v_writing       = box.getString("p_writing");                          //저서
         String v_certificate   = box.getString("p_certificate");                      //주요자격사항
         String v_realFile      = box.getRealFileName("p_file1");
         String v_saveFile      = box.getNewFileName("p_file1");
         
         try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false);
             
             //----------------------   게시판 번호 가져온다 ----------------------------
             
             sql = "select nvl(max(seq), 0)+1 seq from TZ_TUTOR_RECRUIT ";
             
             rs1 = connMgr.executeQuery(sql);
             for ( int i = 0; rs1.next(); i++ ) { 
                 dbox = rs1.getDataBox();
                 v_seq = dbox.getInt("d_seq");
             }
             rs1.close();
             
             // ----------------------   게시판 table 에 입력  --------------------------
             /*
             sql1 =  "insert into TZ_TUTOR_RECRUIT (seq ,name, email, phone, field, title, content, ldate, realfile, savefile)" +
             		" values                      \n" +                    
                     "      (                                                    \n" +                                   
                     "       ?,                                                 \n" +   //seq
                     "       ?,                                                  \n" +   //name
                     "       ?,                                                  \n" +   //email
                     "       ?,                                                  \n" +   //phone
                     "       ?,                                                  \n" +   //field
                     "       ?,                                                  \n" +   //title
                     "       ?,                                                  \n" +   //content
                     "       to_char(sysdate, 'yyyymmddhh24miss'),               \n" +   //ldate
                     "       ?,                                                  \n" +   //real
                     "       ?)	                                                 \n" ;   //savefile
             */
             
             sql1 += " insert into tz_tutor_recruit \n ";
             sql1 += "             ( ";
             sql1 += "               seq, field, name, birth_date, job, address,  \n ";
             sql1 += "               phone, email, achievement, stu_exp, tutor_exp, career,  \n ";
             sql1 += "               lecture_field, writing, certificate, realfile, savefile, ldate \n ";
             sql1 += "             ) \n ";
             sql1 += "     values  ( \n ";
             sql1 += "               ?, ?, ?, ?, ?, ?, \n ";
             sql1 += "               ?, ?, ?, ?, ?, ?, \n ";
             sql1 += "               ?, ?, ?, ?, ?,    \n ";
             sql1 += "               to_char(sysdate, 'yyyymmddhh24miss') \n ";
             sql1 += "             ) \n ";
                     
             pstmt1 = connMgr.prepareStatement(sql1);
             
             int idx = 1;
             // 입력
             pstmt1.setInt   (idx++, v_seq);    
             pstmt1.setString(idx++, v_field);
             pstmt1.setString(idx++, v_name);
             pstmt1.setString(idx++, v_birth_date);            
             pstmt1.setString(idx++, v_job);           
             pstmt1.setString(idx++, v_address);       
             pstmt1.setString(idx++, v_phone);           
             pstmt1.setString(idx++, v_email);
             pstmt1.setString(idx++, v_achievement);
             pstmt1.setString(idx++, v_stu_exp);
             pstmt1.setString(idx++, v_tutor_exp);
             pstmt1.setString(idx++, v_career);
             pstmt1.setString(idx++, v_lecture_field);
             pstmt1.setString(idx++, v_writing);
             pstmt1.setString(idx++, v_certificate);
             pstmt1.setString(idx++, v_realFile);
             pstmt1.setString(idx++, v_saveFile);
           
             isOk1 = pstmt1.executeUpdate();         
             
             if ( pstmt1 != null ) { pstmt1.close(); }

             if ( isOk1 > 0 ) { 
                 if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
             } else { 
                 if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
             }
         }

         catch ( Exception ex ) { 
             if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
             ErrorManager.getErrorStackTrace(ex, box, sql1);
             throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
         }
         finally { 
             if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
             if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
             if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return isOk1;
     }    
         
    
    
    
}
