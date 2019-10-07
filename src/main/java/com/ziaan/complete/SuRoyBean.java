// **********************************************************
//  1. 제      목: 예약상담 Bean
//  2. 프로그램명: ResBean.java
//  3. 개      요: 예약상담
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 조용준
//  7. 수      정: 
// **********************************************************

package com.ziaan.complete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import oracle.sql.CLOB;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class SuRoyBean { 
    private static final String FILE_TYPE = "p_file";           //      파일업로드되는 tag name
    private static final int FILE_LIMIT = 1;                    //    페이지에 세팅된 파일첨부 갯수
    private ConfigSet config;
    private int row;

    public SuRoyBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    * 자료실 리스트화면 select
    * @param    box          receive from the form object and session
    * @return ArrayList  자료실 리스트
    * @throws Exception
    */
    public ArrayList selectDocList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_pageno = box.getInt("p_pageno");
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");
        String v_sel1	    = box.getString("p_sel1");
        String v_sel2	    = box.getString("p_sel2");
        
        String v_sdate1     = box.getString("p_sdate1").replaceAll("-","");//신청 시작일
        String v_edate1     = box.getString("p_edate1").replaceAll("-","");//신청
        
        String v_sdate2     = box.getString("p_sdate2").replaceAll("-","");//발급 시작일
        String v_edate2     = box.getString("p_edate2").replaceAll("-","");//발급
        
        String v_tabseq     = box.getString("p_tabseq");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " 	select tct.subj, tct.year, tct.subjseq, tct.userid, tct.seq, 			";
            sql += "		tct.username, tct.useradd, tct.useremail, tct.usertel, tct.ea, 		";
            sql += "		tct.appdate, tct.issend, tct.senddate, tct.ison, tct.sname, 		";
            sql += "		tct.suserid, 								";
            sql += "		tsj.edustart, tsj.eduend, tsj.subjnm,					";
            sql += "		tsd.serno								";
            sql += "	from tz_certapp tct, tz_subjseq tsj, tz_stold tsd				";
            sql += "	where tct.subj = tsj.subj							";
            sql += "		and tct.year = tsj.year							";
            sql += "		and tct.subjseq = tsj.subjseq 						";
            sql += "		and tct.subj = tsd.subj							";
            sql += "		and tct.year = tsd.year 						";
            sql += "		and tct.subjseq = tsd.subjseq 						";
            sql += "		and tct.userid = tsd.userid 						";
            
            //발급대장...신청대장...모두...
            
            //발급, 미처리 구분            
            if ( !v_sel1.equals("")) {
            	if (v_sel1.equals("req")) {
            		sql += " and tct.issend = 'N' ";
            	} else if (v_sel1.equals("send")) {
            		sql += " and tct.issend = 'Y' ";
            	}
            }
            
            //온라인 오프라인 여부 구분
            if ( !v_sel2.equals("")) {
            	if (v_sel2.equals("on")) {
            		sql += " and tct.ison = 'Y' ";
            	} else if (v_sel2.equals("off")) {
            		sql += " and tct.ison = 'N' ";
            	}
            }

            //신청기간
            if ( !v_sdate1.equals("")) {
            	sql += " and tct.appdate >= " + StringManager.makeSQL(v_sdate1) + " ";            	
            }
            
            if ( !v_edate1.equals("")) {
            	sql += " and tct.appdate <= " + StringManager.makeSQL(v_edate1) + " ";            	
            }            
            
            //발급기간
            if ( !v_sdate2.equals("")) {
            	sql += " and tct.senddate >= " + StringManager.makeSQL(v_sdate2) + " ";            	
            }
            
            if ( !v_edate2.equals("")) {
            	sql += " and tct.senddate <= " + StringManager.makeSQL(v_edate2) + " ";            	
            } 
            
            
            if ( !v_searchtext.equals("") ) {                //    검색어가 있으면
                if ( v_search.equals("name") ) {              //    이름으로 검색할때
                    sql += " and tct.username like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
                else if ( v_search.equals("id") ) {        //    아이디로 검색할때
                    sql += " and tct.userid like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
                else if ( v_search.equals("content") ) {     //    과정명으로 검색할때
                    sql += " and tsj.subjnm like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }    
            }
            sql += " order by tct.appdate desc 	                                                       ";

            ls = connMgr.executeQuery(sql);

             ls.setPageSize(row);                       //  페이지당 row 갯수를 세팅한다
             ls.setCurrentPage(v_pageno);               //     현재페이지번호를 세팅한다.
             int totalpagecount = ls.getTotalPage();    //     전체 페이지 수를 반환한다
             int totalrowcount = ls.getTotalCount();    //     전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                 dbox.put("d_dispnum",        new Integer(totalrowcount - ls.getRowNum() + 1));
                 dbox.put("d_totalpage",      new Integer(totalpagecount));
                 dbox.put("d_rowcount",       new Integer(row));
                list.add(dbox);
            }
            ls.close();
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

   /**
   * 선택된 자료실 게시물 상세내용 select
   * @param box          receive from the form object and session
   * @return ArrayList   조회한 상세정보
   * @throws Exception
   */
   public DataBox selectBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_seq = box.getInt("p_seq");
        int v_tabseq = box.getInt("p_tabseq");
        int v_upfilecnt = (box.getInt("p_upfilecnt") > 0?box.getInt("p_upfilecnt"):1);

        Vector realfileVector = new Vector();
        Vector savefileVector = new Vector();
        Vector fileseqVector  = new Vector();        

        try { 
            connMgr = new DBConnectionManager();
            
            /*
            sql  = " select a.seq, a.userid, a.name, a.title, '' fileseq, '' realfile, a.content, \n";
            sql += " decode(a.gadmin, 'ZZ', 'ZZ', 'P1', '강사',  '운영자') gadmin, '' savefile,    \n";
            sql += " a.indate, a.cnt, a.refseq, a.levels, a.position, (                         \n";
            sql += "            select decode(count(*), 0, 'Y', 'N')                            \n";
            sql += "            from                                                            \n";
            sql += "            (select refseq, levels, position                                \n";
            sql += "            from TZ_CENTER_BOARD                                            \n";
            sql += "            where tabseq = " + v_tabseq + " and seq = " + v_seq         +  "\n";
            sql += "            ) a, TZ_CENTER_BOARD b                                          \n";
            sql += "            where a.refseq = b.refseq                                       \n";
            sql += "            and b.levels = (a.levels +1)                                    \n";
            sql += "            and b.position = (a.position +1)                                \n";
            sql += " ) delyn                                                                    \n";
            sql += " from TZ_HOMEPAGE_QNA a, TZ_HOMEPAGE_QNAFILE b                              \n";
            sql += "  where a.seq    = b.seq( +)                                                \n";
            sql += "    and a.seq    = " + v_seq;
            */
            
            
            sql  = "	select	tabseq, seq, title, userid, name, 				";
            sql	+= "		content, indate, refseq, levels, position, 			";
            sql += "		upfile, cnt, recomcnt, luserid, ldate, 				";
            sql += "		gubunA, gubunB, isopen, email, hasanswer,			";
            sql += "		realfile, savefile, gadmin, pwd, auserid, 			";
            sql += "		adate, acontent, atitle, aname				 		";
            sql += "	from tz_center_board                                                    ";
            sql += "	where	tabseq	= " + v_tabseq + " and seq = " + v_seq + "              ";            
            
            ls = connMgr.executeQuery(sql);


            for ( int i = 0; ls.next(); i++ ) { 

                dbox = ls.getDataBox();
                /*

                realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
                fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));
                */
            }
            /*
            if ( realfileVector      != null ) dbox.put("d_realfile", realfileVector);
            if ( savefileVector      != null ) dbox.put("d_savefile", savefileVector);
            if ( fileseqVector   != null ) dbox.put("d_fileseq", fileseqVector);
            */

            connMgr.executeUpdate("update TZ_CENTER_BOARD set cnt = cnt + 1 where tabseq = " + v_tabseq + " and seq = " + v_seq);
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
    * 새로운 자료실 내용 등록
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int insertBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;

        ResultSet rs1 = null;
        Statement stmt1 = null;
        PreparedStatement pstmt1  = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int v_seq = 0;
        ListSet ls1         = null;

        int v_tabseq   = box.getInt("p_tabseq");
        String v_title   = box.getString("p_title");
        String v_content = box.getString("p_content"); // 내용 clob

        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name"); 
        String s_gadmin = box.getSession("gadmin");

        /*********************************************************************************************/
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // ----------------------   게시판 번호 가져온다 ----------------------------
            sql = " select nvl(max(seq), 0) from TZ_CENTER_BOARD where tabseq = " + v_tabseq;
            ls1 = connMgr.executeQuery(sql);
            if ( ls1.next() ) { 
                v_seq = ls1.getInt(1) + 1;
            }

            // ----------------------   게시판 table 에 입력  --------------------------
            sql1 =  " insert into TZ_CENTER_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate, gadmin) ";
            sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?)";
//            sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?)";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_tabseq);
            pstmt1.setInt(2, v_seq);
            pstmt1.setString(3, s_userid);
            pstmt1.setString(4, s_usernm);
            pstmt1.setString(5, v_title);
            pstmt1.setString(6, v_content);
            pstmt1.setInt(7, 0);
            pstmt1.setInt(8, v_seq);
            pstmt1.setInt(9, 1);
            pstmt1.setInt(10, 1);
            pstmt1.setString(11, s_userid);
            pstmt1.setString(12, s_gadmin);

            isOk1 = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
            sql2 = "select content from TZ_CENTER_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq;
//            connMgr.setOracleCLOB(sql2, v_content);       //      (기타 서버 경우)       

            // 파일업로드
            //isOk2 = this.insertUpFile(connMgr, v_seq, box);
                    
            if ( isOk1 > 0 && isOk2 > 0 ) { 
                connMgr.commit();
            } else { 
                connMgr.rollback();
            }
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
        }

        return isOk1*isOk2;
    }





    /**
    * 새로운 자료실 답변 등록
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int replyBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ResultSet rs1 = null;
        Statement stmt1 = null;
        Statement stmt2 = null;
        PreparedStatement pstmt1  = null;
        PreparedStatement pstmt2 = null;

        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int isOk3 = 1;
        int v_seq = 0;

        int    v_refseq   = box.getInt("p_refseq");
        int    v_levels   = box.getInt("p_levels");
        int    v_position = box.getInt("p_position");
        String v_title    = box.getString("p_title");
        String v_content  = box.getString("p_content");

        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name");
        String s_gadmin = box.getSession("gadmin");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 기존 답변글 위치 한칸밑으로 변경
            sql1  = "update TZ_HOMEPAGE_QNA ";
            sql1 += "   set position = position + 1 ";
            sql1 += " where refseq   = ? ";
            sql1 += "   and position > ? ";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_refseq);
            pstmt1.setInt(2, v_position);
            isOk1 = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
            stmt1 = connMgr.createStatement();
            // ----------------------   게시판 번호 가져온다 ----------------------------
            sql2 = "select nvl(max(seq), 0) from TZ_HOMEPAGE_QNA";
            rs1 = stmt1.executeQuery(sql2);
            if ( rs1.next() ) { 
                v_seq = rs1.getInt(1) + 1;
            }
            // ------------------------------------------------------------------------------------

            //// //// //// //// //// //// //// //// /  게시판 table 에 입력  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql1 =  " insert into TZ_HOMEPAGE_QNA(seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate, gadmin) ";
            sql1 += " values (?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?) ";
//            sql1 += " values (?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?) ";

            pstmt2 = connMgr.prepareStatement(sql1);
            pstmt2.setInt(1, v_seq);
            pstmt2.setString(2, s_userid);
            pstmt2.setString(3, s_usernm);
            pstmt2.setString(4, v_title);
            pstmt2.setString(5, v_content);
            //connMgr.setCharacterStream(pstmt2, 5, v_content); //      Oracle 9i or Weblogic 6.1 인 경우
            pstmt2.setInt(6, 0);
            pstmt2.setInt(7, v_refseq);
            pstmt2.setInt(8, v_levels + 1);
            pstmt2.setInt(9, v_position + 1);
            pstmt2.setString(10, s_userid);
            pstmt2.setString(11, s_gadmin);
            isOk2 = pstmt2.executeUpdate();
            if ( pstmt2 != null ) { pstmt2.close(); }
            sql2 = "select content from TZ_HOMEPAGE_QNA where seq = " + v_seq;
//            connMgr.setOracleCLOB(sql2, v_content);       //      (기타 서버 경우)   


            isOk3 = this.insertUpFile(connMgr, v_seq, box);

            if ( isOk2 > 0 && isOk3 > 0 ) { 
                connMgr.commit();
            } else { 
                connMgr.rollback();
            }
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( stmt2 != null ) { try { stmt2.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
        }

        return isOk2*isOk3;
    }



    /**
    * 선택된 자료 상세내용 수정
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
     public int updateBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ResultSet rs1 = null;
        Statement stmt1 = null;
        PreparedStatement pstmt1  = null;
        CLOB clob                 = null;
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int isOk3 = 1;

        int    v_tabseq    = box.getInt("p_tabseq");
        int    v_seq       = box.getInt("p_seq");
        int    v_upfilecnt = box.getInt("p_upfilecnt");    //  서버에 저장되있는 파일수
        String v_title     = box.getString("p_title");
        String v_content   = box.getString("p_content");
        String v_atitle     = box.getString("p_atitle");
        String v_acontent   = box.getString("p_acontent");        
 
        Vector v_savefile     = new Vector();
        Vector v_filesequence = new Vector();
        
        String s_usernm = box.getSession("name");
        
        System.out.println("이값은 뭐야 " +v_upfilecnt);
        for ( int   i = 0; i < v_upfilecnt; i++ ) { 
            if (    !box.getString("p_fileseq" + i).equals("")) { 
                   System.out.println("여기 들어오는거야?");
                v_savefile.addElement(box.getString("p_savefile" + i));         //      서버에 저장되있는 파일명 중에서   삭제할 파일들
                v_filesequence.addElement(box.getString("p_fileseq"  + i));      //         서버에 저장되있는 파일번호  중에서 삭제할 파일들
            }
        }
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sql1 = "update TZ_CENTER_BOARD set title = ?, content=empty_clob(), atitle = ?, acontent=empty_clob() ";//, userid = ?, name = ?, luserid = ?, ldate = to_char(sysdate, 'YYYYMMDDHH24MISS')";
            if (!v_atitle.equals("") || !v_acontent.equals("")) {
            	sql1 += " ,hasanswer ='Y' , adate =to_char(sysdate,  'YYYYMMDDHH24MISS'),  aname= '"+s_usernm+"' ";
            } else if (v_atitle.equals("") && v_acontent.equals("")) {
            	sql1 += " ,hasanswer ='N' , adate ='', aname= '' ";
            }
            sql1 += "  where tabseq = ? and seq = ? ";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_title);
            pstmt1.setString(2, v_atitle);
	    pstmt1.setInt(3, v_tabseq);
            pstmt1.setInt(4, v_seq);            

            isOk1 = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
            // WebLogic 6.1인경우
            sql2 = "select content from TZ_CENTER_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq;
            connMgr.setWeblogicCLOB(sql2, v_content);       //      clob 필드에 스트림을 이용,  data 를 넣는다(Weblogic 인 경우)
            
            sql2 = "select acontent from TZ_CENTER_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq;
            connMgr.setWeblogicCLOB(sql2, v_acontent);       //      clob 필드에 스트림을 이용,  data 를 넣는다(Weblogic 인 경우)            


            isOk2 = this.insertUpFile(connMgr, v_seq, box);       //      파일첨부했다면 파일table에  insert

            System.out.println("여기서 한번 ");
            System.out.println(v_filesequence);
            isOk3 = this.deleteUpFile(connMgr, box, v_filesequence);        //     삭제할 파일이 있다면 파일table에서 삭제

            if ( isOk1 > 0 && isOk2 > 0 && isOk3 > 0 ) { 
                connMgr.commit();
                if ( v_savefile != null ) { 
                    FileManager.deleteFile(v_savefile);         //   DB 에서 모든처리가 완료되면 해당 첨부파일 삭제
                }
            } else connMgr.rollback();
        } catch ( Exception ex ) { 
            // conn.rollback();
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( clob != null ) { try { clob.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            // if ( conn != null ) { try { conn.setAutoCommit(true); } catch ( Exception e10 ) { } }
            // if ( conn != null ) { try { bulletinDB.freeConnection("ziaan", conn); } catch ( Exception e10 ) { } }
        }

        return isOk1*isOk2*isOk3;
    }


    /**
    * 선택된 게시물 삭제
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 0;
        int isOk2 = 0;

        int v_seq = box.getInt("p_seq");
        int v_tabseq = box.getInt("p_tabseq");
        int v_upfilecnt = box.getInt("p_upfilecnt");    //  서버에 저장되있는 파일수
        Vector v_savefile  = box.getVector("p_savefile");

        // 답변 유무 체크(답변 있을시 삭제불가)
        if ( this.selectBoard(v_seq) == 0 ) { 

            try { 
                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);

                isOk1 = 1;
                isOk2 = 1;
                sql1 = "delete from TZ_CENTER_BOARD where tabseq = ? and seq = ? ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);
                pstmt1.setInt(2, v_seq);
                isOk1 = pstmt1.executeUpdate();
                if ( pstmt1 != null ) { pstmt1.close(); }
                if ( v_upfilecnt > 0 ) { 
                    sql2 = "delete from TZ_HOMEPAGE_QNAFILE where seq =  ?";
                    pstmt2 = connMgr.prepareStatement(sql2);
                    pstmt2.setInt(1, v_seq);                    
                    isOk2 = pstmt2.executeUpdate();
                    if ( pstmt2 != null ) { pstmt2.close(); }
                }

                if ( isOk1 > 0 && isOk2 > 0 ) { 
                    connMgr.commit();
                    if ( v_savefile != null ) { 
                        FileManager.deleteFile(v_savefile);         //   첨부파일 삭제
                    }
                } else connMgr.rollback();
            }
            catch ( Exception ex ) { 
                connMgr.rollback();
                ErrorManager.getErrorStackTrace(ex, box,    sql1);
                throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
            }
            finally { 
                if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }  
                if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }

        return isOk1*isOk2;
    }


   /**
   * 삭제시 하위 답변 유무 체크
   * @param seq          게시판 번호
   * @return result      0 : 답변 없음,    1 : 답변 있음
   * @throws Exception
   */
   public int selectBoard(int seq) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        int result = 0;

        try { 
            connMgr = new DBConnectionManager();

            sql  = "  select count(*) cnt                         ";
            sql += "  from                                        ";
            sql += "    (select refseq, levels, position  ";
            sql += "       from TZ_HOMEPAGE_QNA                   ";
            sql += "      where seq = " + seq;
            sql += "     ) a, TZ_HOMEPAGE_QNA b                   ";
            sql += " where a.refseq = b.refseq                    ";
            sql += "   and b.levels = (a.levels +1)                ";
            sql += "   and b.position = (a.position +1)            ";


            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }


//// //// //// //// //// //// //// //// //// //// //// //// //// /// 파일 테이블   //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

    /**
    * 새로운 자료파일 등록
    * @param connMgr  DB Connection Manager
    * @param p_seq    게시물 일련번호
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int insertUpFile(DBConnectionManager connMgr, int p_seq, RequestBox   box) throws Exception { 
        ListSet             ls      = null;
        PreparedStatement pstmt2 = null;
        String              sql     = "";
        String sql2 = "";
        int isOk2 = 1;

        // ----------------------   업로드되는 파일의 형식을 알고 코딩해야한다  --------------------------------

        String [] v_realFileName = new String [FILE_LIMIT];
        String [] v_newFileName = new String [FILE_LIMIT];

        for ( int i = 0; i < FILE_LIMIT; i++ ) { 
            v_realFileName [i] = box.getRealFileName(FILE_TYPE + (i +1));
            v_newFileName [i] = box.getNewFileName(FILE_TYPE + (i +1));
        }
        // ----------------------------------------------------------------------------------------------------------------------------

        String s_userid = box.getSession("userid");

        try { 
             // ----------------------   자료 번호 가져온다 ----------------------------
            sql = "select nvl(max(fileseq), 0) from TZ_HOMEPAGE_QNAFILE where seq =   " + p_seq;
            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_fileseq = ls.getInt(1) + 1;
            ls.close();
            // ------------------------------------------------------------------------------------

            //// //// //// //// //// //// //// //// //   파일 table 에 입력  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql2 =  "insert into TZ_HOMEPAGE_QNAFILE(seq, fileseq, realfile, savefile, luserid, ldate)";
            sql2 += " values (?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";

            pstmt2 = connMgr.prepareStatement(sql2);

            for ( int i = 0; i < FILE_LIMIT; i++ ) { 
                if ( !v_realFileName [i].equals("") ) {       //      실제 업로드 되는 파일만 체크해서 db에 입력한다
                    pstmt2.setInt(1, p_seq);
                    pstmt2.setInt(2, v_fileseq);
                    pstmt2.setString(3, v_realFileName [i]);
                    pstmt2.setString(4, v_newFileName [i]);
                    pstmt2.setString(5, s_userid);

                    isOk2 = pstmt2.executeUpdate();
                    v_fileseq++;
                }
            }
            if ( pstmt2 != null ) { pstmt2.close(); }
        } catch ( Exception ex ) { 
            FileManager.deleteFile(v_newFileName, FILE_LIMIT);      //  일반파일, 첨부파일 있으면 삭제..
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
        }

        return isOk2;
    }


    /**
     * 선택된 자료파일 DB에서 삭제
     * @param connMgr           DB Connection Manager
     * @param box               receive from the form object and session
     * @param p_filesequence    선택 파일 갯수
     * @return
     * @throws Exception
     */
    public int deleteUpFile(DBConnectionManager connMgr, RequestBox box, Vector p_filesequence) throws Exception { 
        PreparedStatement pstmt3 = null;
        String sql3 = "";
        int isOk3 = 1;

        int v_seq    = box.getInt("p_seq");

        try { 
            sql3 = "delete from TZ_HOMEPAGE_QNAFILE where seq =? and fileseq = ?";
            pstmt3 = connMgr.prepareStatement(sql3);

            for ( int i = 0; i < p_filesequence.size(); i++ ) { 
                int v_fileseq = Integer.parseInt((String)p_filesequence.elementAt(i));

                pstmt3.setInt(1, v_seq);
                pstmt3.setInt(2, v_fileseq);

                isOk3 = pstmt3.executeUpdate();
            }
            if ( pstmt3 != null ) { pstmt3.close(); }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql3);
            throw new Exception("sql = " + sql3 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e ) { } }
        }

        return isOk3;
    }

 
    public static String convertBody(String contents) throws Exception { 
        String result = "";
        
        result = StringManager.replace(contents, "<HTML > ","");
        result = StringManager.replace(result, "<HEAD > ","");
        result = StringManager.replace(result, "<META NAME=\"GENERATOR\" Content=\"Microsoft DHTML Editing Control\" > ","");
        result = StringManager.replace(result, "<TITLE > ","");
        result = StringManager.replace(result, "</TITLE > ","");
        result = StringManager.replace(result, "</HEAD > ","");
        result = StringManager.replace(result, "<BODY > ","");
        result = StringManager.replace(result, "</BODY > ","");       
        result = StringManager.replace(result, "</HTML > ","");
                
        return result;
    }
    

/**
    오프라인 수료증출력 저장
    @param box      receive from the form object and session
    @return int
    */
     public int SuRoyAppUpdate032(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt = null;
        ListSet ls1         = null;
        
        String sql1         = "";
        int cancel_cnt      = 0;
        int isOk            = 0;
        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name"); 
        
        String  v_subj      = box.getString("p_subj");
        String  v_year      = box.getString("p_year");
        String  v_subjseq   = box.getString("p_subjseq");
        String  v_userid    = box.getString("p_userid");
        
        int 	v_seq	    = box.getInt("p_seq");
        
        Hashtable insertData = new Hashtable();
        /*
        ProposeBean probean = new ProposeBean();

        SubjComBean scbean = new SubjComBean();
        */

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

                      sql1 =	"	update TZ_CERTAPP ";
                      sql1 +=	"	set issend = 'Y', ";
                      sql1 +=	"		senddate = to_char(sysdate,'YYYYMMDDHH24MISS'), ";                      
                      sql1 +=	"		sname = ?, ";
                      sql1 +=	"		suserid = ? ";
                      sql1 +=	"	where subj = ?		";
                      sql1 +=	"		and year = ?	";
                      sql1 +=	"		and subjseq = ?";
                      sql1 +=	"		and userid = ?	";
                      sql1 +=	"		and seq = ?	";                                            
                      
                      pstmt = connMgr.prepareStatement(sql1);
                      pstmt.setString(1, s_usernm);
                      pstmt.setString(2, s_userid);
                      pstmt.setString(3, v_subj);
                      pstmt.setString(4, v_year);
                      pstmt.setString(5, v_subjseq);
                      pstmt.setString(6, v_userid);
                      pstmt.setInt(7, v_seq);
                      
                      isOk = pstmt.executeUpdate();
                      
                      if ( pstmt != null ) { pstmt.close(); }

                if ( isOk > 0 ) connMgr.commit();
                else connMgr.rollback();

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }         
}
