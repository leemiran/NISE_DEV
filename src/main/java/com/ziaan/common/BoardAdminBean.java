// **********************************************************
//  1. 제      목: 게시판(어드민)
//  2. 프로그램명: BoardAdminBean.java
//  3. 개      요: 게시판
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: mscho 2004.01.15
//  7. 수      정:
// **********************************************************
package com.ziaan.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class BoardAdminBean { 
    private ConfigSet config;
    private int row;
    private static final String FILE_TYPE = "p_file";           //      파일업로드되는 tag name
    private static final int FILE_LIMIT = 5;                    //    페이지에 세팅된 파일첨부 갯수

    public BoardAdminBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        이 모듈의 페이지당 row 수를 셋팅한다
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }


    /**
    * 자료실 테이블번호
    * @param box          receive from the form object and session
    * @return int         자료실 테이블번호
    * @throws Exception
    */
    public int selectTableseq(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         String              sql     = "";
         int result = 0;

         String v_type    = box.getStringDefault("p_type","");
         String v_grcode  = box.getStringDefault("p_grcode","0000000");
         String v_comp    = box.getStringDefault("p_comp","0000000000");
         String v_subj    = box.getStringDefault("p_subj","0000000000");
         String v_year    = box.getStringDefault("p_year","0000");
         String v_subjseq = box.getStringDefault("p_subjseq","0000");

         try { 
             connMgr = new DBConnectionManager();

             sql  = " select tabseq from TZ_BDS      ";
             sql += "  where type    = " + StringManager.makeSQL(v_type);
             sql += "    and grcode  = " + StringManager.makeSQL(v_grcode);
             sql += "    and comp    = " + StringManager.makeSQL(v_comp);
             sql += "    and subj    = " + StringManager.makeSQL(v_subj);
             sql += "    and year    = " + StringManager.makeSQL(v_year);
             sql += "    and subjseq = " + StringManager.makeSQL(v_subjseq);

             ls = connMgr.executeQuery(sql);

             if ( ls.next() ) { 
                 result = ls.getInt("tabseq");
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
         return result;
     }


    /**
    * 과목게시판 테이블번호
    * @param box          receive from the form object and session
    * @return int         자료실 테이블번호
    * @throws Exception
    */
    public int selectSBTableseq(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         PreparedStatement pstmt4 = null;
         ListSet ls1 = null;
         ListSet ls2 = null;
         ListSet ls3 = null;
         String sql1 = "";
         String sql2 = "";
         String sql3 = "";
         String sql4 = "";
         int v_tabseq = 0;
         int result = 0;

         String v_user_id = box.getSession("userid");
         String v_type    = box.getString("p_type");
         String v_subj    = box.getString("p_subj");
         String v_year    = box.getString("p_year");
         String v_subjseq = box.getString("p_subjseq");
         if ( v_subj.equals("") ) {     v_subj = box.getSession("s_subj");          }
         if ( v_year.equals("") ) {     v_year = box.getSession("s_year");          }
         if ( v_subjseq.equals("") ) {  v_subjseq = box.getSession("s_subjseq");    }

         try { 
             connMgr = new DBConnectionManager();

             sql1  = " select tabseq from TZ_BDS      ";
             sql1 += "  where type    = " + StringManager.makeSQL(v_type);
             sql1 += " and subj       = " + StringManager.makeSQL(v_subj);
             sql1 += " and year       = " + StringManager.makeSQL(v_year);
             sql1 += " and subjseq    = " + StringManager.makeSQL(v_subjseq);
             ls1 = connMgr.executeQuery(sql1);

             if ( ls1.next() ) {     // TZ_BDS에 해당 테이블 시퀀스가 있는 경우
                 v_tabseq = ls1.getInt("tabseq");
             } else {                 // TZ_BDS에 해당 테이블 시퀀스가 없는 경우
                 sql2  = " select count(subj) cnt from TZ_SUBJSEQ      ";
                 sql2 += "  where subj    = " + StringManager.makeSQL(v_subj);
                 sql2 += " and year       = " + StringManager.makeSQL(v_year);
                 sql2 += " and subjseq    = " + StringManager.makeSQL(v_subjseq);
                 ls2 = connMgr.executeQuery(sql2);

                 if ( ls2.next() && ls2.getInt("cnt") > 0 ) {   // 실제 생성되어있는 과목이면
                    sql3 = "select nvl(max(tabseq), 0) from TZ_BDS";
                    ls3 = connMgr.executeQuery(sql3);
                    ls3.next();
                    v_tabseq = ls3.getInt(1) + 1;
                    ls3.close();

                    sql4  = "insert into TZ_BDS(tabseq,type,subj,year,subjseq,sdesc,luserid,ldate) ";
                    sql4 += " values(?,?,?,?,?,?,?,to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
                    pstmt4 = connMgr.prepareStatement(sql4);
                    pstmt4.setInt(1, v_tabseq);
                    pstmt4.setString(2, v_type);
                    pstmt4.setString(3, v_subj);
                    pstmt4.setString(4, v_year);
                    pstmt4.setString(5, v_subjseq);
                    pstmt4.setString(6, v_subj + "과정" +v_subjseq + "차수게시판");
                    pstmt4.setString(7, v_user_id);
                    result = pstmt4.executeUpdate();
                    
                    if ( pstmt4 != null ) { pstmt4.close(); }
                    
                } else { 
                    v_tabseq = 0;
                }
            }
         }
         catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql1);
             throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
         }
         finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { }}
             if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { }}
             if ( ls3 != null ) { try { ls3.close(); } catch ( Exception e ) { }}
             if ( pstmt4 != null ) { try { pstmt4.close(); } catch ( Exception e1 ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return v_tabseq;
     }


    /**
    * 과목자료실 테이블번호
    * @param box          receive from the form object and session
    * @return int         자료실 테이블번호
    * @throws Exception
    */
    public int selectSDTableseq(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         PreparedStatement pstmt4 = null;
         ListSet ls1 = null;
         ListSet ls2 = null;
         ListSet ls3 = null;
         String sql1 = "";
         String sql2 = "";
         String sql3 = "";
         String sql4 = "";
         int v_tabseq = 0;
         int result = 0;

         String v_user_id = box.getSession("userid");
         String v_type    = box.getString("p_type");
         String v_subj    = box.getString("p_subj");
         if ( v_subj.equals("") ) {     v_subj = box.getSession("s_subj");          }

         try { 
             connMgr = new DBConnectionManager();

             sql1  = " select tabseq from TZ_BDS      ";
             sql1 += "  where type    = " + StringManager.makeSQL(v_type);
             sql1 += " and subj    = " + StringManager.makeSQL(v_subj);
             ls1 = connMgr.executeQuery(sql1);

             if ( ls1.next() ) {     // TZ_BDS에 해당 테이블 시퀀스가 있는 경우
                 v_tabseq = ls1.getInt("tabseq");
             } else {                 // TZ_BDS에 해당 테이블 시퀀스가 없는 경우
                 sql2  = " select count(subj) cnt from TZ_SUBJ      ";
                 sql2 += "  where subj    = " + StringManager.makeSQL(v_subj);
                 ls2 = connMgr.executeQuery(sql2);

                 if ( ls2.next() && ls2.getInt("cnt") > 0 ) {   // 실제 생성되어있는 과목이면
                    sql3 = "select nvl(max(tabseq), 0) from TZ_BDS";
                    ls3 = connMgr.executeQuery(sql3);
                    ls3.next();
                    v_tabseq = ls3.getInt(1) + 1;
                    ls3.close();

                    sql4  = "insert into TZ_BDS(tabseq,type,subj,sdesc,luserid,ldate) ";
                    sql4 += " values(?,?,?,?,?,to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
                    pstmt4 = connMgr.prepareStatement(sql4);
                    pstmt4.setInt(1, v_tabseq);
                    pstmt4.setString(2, v_type);
                    pstmt4.setString(3, v_subj);
                    pstmt4.setString(4, v_subj + "과목자료실");
                    pstmt4.setString(5, v_user_id);
                    result = pstmt4.executeUpdate();
                    
                    if ( pstmt4 != null ) { pstmt4.close(); }
                    
                } else { 
                    v_tabseq = 0;
                }
            }
         }
         catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql1);
             throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
         }
         finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { }}
             if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { }}
             if ( ls3 != null ) { try { ls3.close(); } catch ( Exception e ) { }}
             if ( pstmt4 != null ) { try { pstmt4.close(); } catch ( Exception e1 ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return v_tabseq;
     }


    /**
    * 자료실 리스트화면 select
    * @param    box          receive from the form object and session
    * @return ArrayList  자료실 리스트
    * @throws Exception
    */
    public ArrayList selectBoardList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
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

            sql  = " select a.seq seq, a.userid userid, a.name name, a.title title, count(b.realfile) upfilecnt, ";
            sql += "        a.indate indate, a.cnt cnt, a.refseq refseq, a.levels levels, a.position position  ";
            sql += "   from TZ_BOARD a, TZ_BOARDFILE b                                                         ";
            sql += "  where a.tabseq = b.tabseq( +)                                                             ";
            sql += "    and a.seq    = b.seq( +)                                                                ";
            sql += "    and a.tabseq = " + v_tabseq;

            if ( !v_searchtext.equals("") ) {                //    검색어가 있으면
                // v_pageno = 1;   //      검색할 경우 첫번째 페이지가 로딩된다

                if ( v_search.equals("name") ) {              //    이름으로 검색할때
                    sql += " and a.name like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                }
                else if ( v_search.equals("title") ) {        //    제목으로 검색할때
                    sql += " and a.title like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                }
                else if ( v_search.equals("content") ) {     //    내용으로 검색할때
                    // sql += " and dbms_lob.instr(a.content, " + StringManager.makeSQL(v_searchtext) + ",1,1) < > 0";
                    sql += " and a.content like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i 용
                }
            }

            sql += " group by a.seq, a.userid, a.name, a.title, a.indate, a.cnt, a.refseq, a.levels, a.position ";
            sql += " order by a.refseq desc, position asc                                                       ";

// System.out.println("sql == > " + sql);
            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                       //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);               //     현재페이지번호를 세팅한다.
            int totalpagecount = ls.getTotalPage();    //     전체 페이지 수를 반환한다
            int totalrowcount = ls.getTotalCount();    //     전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount", new Integer(row));
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
   * 선택된 자료실 게시물 상세내용 select
   * @param box          receive from the form object and session
   * @return ArrayList   조회한 상세정보
   * @throws Exception
   */
   public DataBox selectBoard(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;
        String v_upcnt = "Y";

        int v_tabseq    = box.getInt("p_tabseq");
        int v_seq       = box.getInt("p_seq");
        int v_upfilecnt = (box.getInt("p_upfilecnt") > 0?box.getInt("p_upfilecnt"):1);

        // String [] realfile = new String [v_upfilecnt];
        // String [] savefile= new String [v_upfilecnt];
        // int [] fileseq = new int [v_upfilecnt];
        Vector realfileVector = new Vector();
        Vector savefileVector = new Vector();
        Vector fileseqVector  = new Vector();
        try { 
            connMgr = new DBConnectionManager();

            sql  = " select a.seq seq, a.userid userid, a.name name, a.title title, b.fileseq fileseq, b.realfile realfile, a.content content,  ";
            sql += "        b.savefile savefile, a.indate indate, a.cnt cnt, a.refseq refseq, a.levels levels, a.position position ,             ";
            sql +="         (select count(realfile) from TZ_BOARDFILE where tabseq = a.tabseq and seq = a.seq) upfilecnt                         ";
            sql += " from TZ_BOARD a, TZ_BOARDFILE b                                                                                            ";
            sql += "  where a.tabseq = b.tabseq( +)                                                                                              ";
            sql += "    and a.seq    = b.seq( +)                                                                                                 ";
            sql += "    and a.tabseq = " + v_tabseq;
            sql += "    and a.seq    = " + v_seq;
System.out.println("selectBoard" +sql);
            ls = connMgr.executeQuery(sql);

            for ( int i = 0; ls.next(); i++ ) { 

                dbox = ls.getDataBox();

                realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
                fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));
            }
            if ( realfileVector != null ) dbox.put("d_realfile", realfileVector);
            if ( savefileVector != null ) dbox.put("d_savefile", savefileVector);
            if ( fileseqVector  != null ) dbox.put("d_fileseq", fileseqVector);

            if ( !v_upcnt.equals("N") ) { 
                connMgr.executeUpdate("update TZ_BOARD set cnt = cnt + 1 where  tabseq = " + v_tabseq + " and seq = " + v_seq);
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
    * 새로운 자료실 내용 등록
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int insertBoard(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ResultSet rs1   = null;
        Statement stmt1 = null;
        PreparedStatement pstmt1  = null;
        String sql    = "";
        String sql1   = "";
        String sql2   = "";
        String v_type = "";
        int isOk1     = 1;
        int isOk2     = 1;
        int v_seq     = 0;

        int    v_tabseq  = box.getInt("p_tabseq");
        String v_title   = box.getString("p_title");
        String v_content = box.getString("p_content");
        String s_userid  = box.getSession("userid");
        String s_usernm  = box.getSession("name");
        String v_isedu   = box.getString("p_isedu"); // (2005.9)학습창게시판,운영자자료실 같이 쓰임(학습창게시판 = '1', 운영자자료실 = '')
        
               
        /*if ( box.getSession("gadmin").substring(0,1).equals("A") ) { 
            s_usernm = "운영자";    
        }*/

        if ( box.getSession("gadmin").substring(0,1).equals("A1") ) { 
            s_userid = "운영자";
        } else { 
            s_userid = box.getSession("userid");
        }

        if ( v_isedu.equals("") ) { 
            /*********************************************************************************************/
            // 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
/*            
            ConfigSet conf = new ConfigSet();
            SmeNamoMime namo = new SmeNamoMime(v_content); // 객체생성 
            boolean result = namo.parse(); // 실제 파싱 수행 
            if ( !result ) { // 파싱 실패시 
                System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
                return 0;
            }
            if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
                String v_server = conf.getProperty("autoever.url.value");
                String fPath = conf.getProperty("dir.namo");   // 파일 저장 경로 지정
                String refUrl = conf.getProperty("url.namo");; // 웹에서 저장된 파일을 접근하기 위한 경로
                String prefix = "board" + v_tabseq;         // 파일명 접두어
                result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
            }
            if ( !result ) { // 파일저장 실패시 
                System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
                return 0;
            }
            v_content = namo.getContent(); // 최종 컨텐트 얻기
*/            
            /*********************************************************************************************/
        } 

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            stmt1 = connMgr.createStatement();

            // ----------------------   게시판 번호 가져온다 ----------------------------
            sql = "select nvl(max(seq), 0) from TZ_BOARD where tabseq = " + v_tabseq;
            rs1 = stmt1.executeQuery(sql);
            if ( rs1.next() ) { 
                v_seq = rs1.getInt(1) + 1;
            }
            rs1.close();
            // -------------------------------------------------------------------------
            
            // ----------------------   게시판 table 에 입력  --------------------------
            sql1 =  " insert into TZ_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate)  ";
            sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))    ";
//            sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))    ";

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

            isOk1 = pstmt1.executeUpdate();
            
            if ( pstmt1 != null ) { pstmt1.close(); }
            
            sql2 = "select content from TZ_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq;
//            connMgr.setOracleCLOB(sql2, v_content);       //      (기타 서버 경우)       

            // 파일업로드
            isOk2 = this.insertUpFile(connMgr, v_tabseq, v_seq, box);
            
            if ( isOk1 > 0 && isOk2 > 0 ) { 
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
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
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
        DBConnectionManager	connMgr	= null;
        ResultSet rs1 = null;

        Statement stmt1 = null;
        Statement stmt2 = null;
        PreparedStatement pstmt1  = null;
        PreparedStatement pstmt2 = null;

        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        String sql4 = "";

        int isOk1 = 1;
        int isOk2 = 1;
        int isOk3 = 1;
        int v_seq = 0;
        String v_isedu   = box.getString("p_isedu"); // (2005.9)학습창게시판,운영자자료실 같이 쓰임(학습창게시판 = '1', 운영자자료실 = '')  
        int    v_tabseq   = box.getInt("p_tabseq");
        int    v_refseq   = box.getInt("p_refseq");
        int    v_levels   = box.getInt("p_levels");
        int    v_position = box.getInt("p_position");
        String v_title    = box.getString("p_title");
        String v_content  = box.getString("p_content");

        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name");
        
        if ( box.getSession("gadmin").substring(0,1).equals("A") ) { 
            s_usernm = "운영자";    
        }
System.out.println("v_content--[" +v_content + "]");
System.out.println("학습창인가??--[" +v_isedu + "]");
        if ( v_isedu.equals("") ) { 
            /*********************************************************************************************/
            // 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
/*            
            ConfigSet conf = new ConfigSet();
            SmeNamoMime namo = new SmeNamoMime(v_content); // 객체생성 
            boolean result = namo.parse(); // 실제 파싱 수행 
            if ( !result ) { // 파싱 실패시 
                System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
                return 0;
            }
            if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
                String v_server = conf.getProperty("autoever.url.value");
                String fPath = conf.getProperty("dir.namo");   // 파일 저장 경로 지정
                String refUrl = conf.getProperty("url.namo");; // 웹에서 저장된 파일을 접근하기 위한 경로
                String prefix = "board" + v_tabseq;         // 파일명 접두어
                result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
            }
            if ( !result ) { // 파일저장 실패시 
                System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
                return 0;
            }
            v_content = namo.getContent(); // 최종 컨텐트 얻기
*/            
            /*********************************************************************************************/
        }        

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 기존 답변글 위치 한칸밑으로 변경
            sql1  = "update TZ_BOARD ";
            sql1 += "   set position = position + 1 ";
            sql1 += " where tabseq   = ? ";
            sql1 += "   and refseq   = ? ";
            sql1 += "   and position > ? ";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_tabseq);
            pstmt1.setInt(2, v_refseq);
            pstmt1.setInt(3, v_position);
            isOk1 = pstmt1.executeUpdate();
            
            if ( pstmt1 != null ) { pstmt1.close(); }

            stmt1 = connMgr.createStatement();
            // ----------------------   게시판 번호 가져온다 ----------------------------
            sql2 = "select nvl(max(seq), 0) from TZ_BOARD where tabseq = " +  v_tabseq;
            rs1 = stmt1.executeQuery(sql2);
            if ( rs1.next() ) { 
                v_seq = rs1.getInt(1) + 1;
            }
            // ------------------------------------------------------------------------------------

            //// //// //// //// //// //// //// //// /  게시판 table 에 입력  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql3 =  " insert into TZ_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate)  ";
            sql3 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))    ";

            pstmt2 = connMgr.prepareStatement(sql3);
            pstmt2.setInt(1, v_tabseq);
            pstmt2.setInt(2, v_seq);
            pstmt2.setString(3, s_userid);
            pstmt2.setString(4, s_usernm);
            pstmt2.setString(5, v_title);
            // connMgr.setCharacterStream(pstmt2, 6, v_content); //      Oracle 9i or Weblogic 6.1 인 경우
            pstmt2.setInt(6, 0);
            pstmt2.setInt(7, v_refseq);
            pstmt2.setInt(8, v_levels + 1);
            pstmt2.setInt(9, v_position + 1);
            pstmt2.setString(10, s_userid);
            isOk2 = pstmt2.executeUpdate();
            
            if ( pstmt2 != null ) { pstmt2.close(); }

            // WebLogic 6.1인경우
            sql4 = "select content from TZ_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq + " for update";
            connMgr.setWeblogicCLOB(sql4, v_content);       //      clob 필드에 스트림을 이용,  data 를 넣는다(Weblogic 인 경우)

            isOk3 = this.insertUpFile(connMgr, v_tabseq, v_seq, box);

            if ( isOk2 > 0 && isOk3 > 0 ) {   connMgr.commit(); }
            else {                          connMgr.rollback(); }

        }
        catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1 + "\r\n" +sql2 + "\r\n" +sql3 + "\r\n" +sql4);
            throw new Exception("sql = " + sql1 + "\r\n" + "sql2 = " + sql2 + "\r\n" + "sql3 = " + sql3 + "\r\n" + "sql4 = " + sql4 + "\r\n" +ex.getMessage() );
        }
        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( stmt2 != null ) { try { stmt2.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
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
        DBConnectionManager	connMgr	= null;
        ResultSet rs1 = null;
        PreparedStatement pstmt1  = null;
        String sql1 = "";
        String sql2 = "";

        int isOk1 = 1, isOk2=1, isOk3=1;
        String v_isedu   = box.getString("p_isedu"); // (2005.9)학습창게시판,운영자자료실 같이 쓰임(학습창게시판 = '1', 운영자자료실 = '')        
        int    v_tabseq    = box.getInt("p_tabseq");
        int    v_seq       = box.getInt("p_seq");
        int    v_upfilecnt = box.getInt("p_upfilecnt");    //  서버에 저장되있는 파일수
        String v_title     = box.getString("p_title");
        // String v_content =  StringManager.replace(box.getString("content"),"<br > ","\n"); 
        String v_content   = box.getString("p_content");
        Vector v_savefile     = box.getVector("p_savefile"); // 선택삭제파일
        Vector v_filesequence = box.getVector("p_fileseq");  // 선택삭제파일 sequence
        Vector v_realfile     = box.getVector("p_file");     // 새로 등록 파일

        for ( int i = 0; i < v_upfilecnt; i++ ) { 
            if ( !box.getString("p_fileseq" + i).equals("") ) { 

                v_savefile.addElement(box.getString("p_savefile" + i));         //      서버에 저장되있는 파일명 중에서 삭제할 파일들
                v_filesequence.addElement(box.getString("p_fileseq" + i));       //     서버에  저장되있는 파일번호 중에서 삭제할 파일들

            }
        }

        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name");

        if ( box.getSession("gadmin").substring(0,1).equals("A") ) { 
            s_usernm = "운영자";    
        }

        if ( v_isedu.equals("") ) { 
            /*********************************************************************************************/
            // 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
/*            
            ConfigSet conf = new ConfigSet();
            SmeNamoMime namo = new SmeNamoMime(v_content); // 객체생성 
            boolean result = namo.parse(); // 실제 파싱 수행 
            if ( !result ) { // 파싱 실패시 
                System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
                return 0;
            }
            if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
                String v_server = conf.getProperty("autoever.url.value");
                String fPath = conf.getProperty("dir.namo");   // 파일 저장 경로 지정
                String refUrl = conf.getProperty("url.namo");; // 웹에서 저장된 파일을 접근하기 위한 경로
                String prefix = "board" + v_tabseq;         // 파일명 접두어
                result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
            }
            if ( !result ) { // 파일저장 실패시 
                System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
                return 0;
            }
            v_content = namo.getContent(); // 최종 컨텐트 얻기
*/            
            /*********************************************************************************************/
        } 

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql1 = "update TZ_BOARD set title = ?, content=empty_clob(), userid = ?, name = ?, luserid = ?, ldate = to_char(sysdate, 'YYYYMMDDHH24MISS')";
            sql1 += "  where tabseq = ? and seq = ?";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_title);
        //  connMgr.setCharacterStream(pstmt1, 2, v_content);           // Oracle 9i or Weblogic 6.1 인 경우
            pstmt1.setString(2, s_userid);
            pstmt1.setString(3, s_usernm);
            pstmt1.setString(4, s_userid);
            pstmt1.setInt(5, v_tabseq);
            pstmt1.setInt(6, v_seq);

            isOk1 = pstmt1.executeUpdate();
            
            if ( pstmt1 != null ) { pstmt1.close(); }

            // WebLogic 6.1인경우
            sql2 = "select content from TZ_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq ;
            connMgr.setWeblogicCLOB(sql2, v_content);       //      clob 필드에 스트림을 이용,  data 를 넣는다(Weblogic 인 경우)


            isOk2 = this.insertUpFile(connMgr, v_tabseq, v_seq, box);       //      파일첨부했다면 파일table에  insert

            isOk3 = this.deleteUpFile(connMgr, box, v_filesequence);        //     삭제할 파일이 있다면 파일table에서 삭제

            if ( isOk1 > 0 && isOk2 > 0 && isOk3 > 0 ) { 
                connMgr.commit();
                if ( v_savefile != null ) { 
                    FileManager.deleteFile(v_savefile);         //   DB 에서 모든처리가 완료되면 해당 첨부파일 삭제
                }
            } else connMgr.rollback();




        }
        catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
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
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 0;
        int isOk2 = 0;

        int v_tabseq = box.getInt("p_tabseq");
        int v_seq = box.getInt("p_seq");

        Vector v_savefile  = box.getVector("p_savefile");
        int v_upfilecnt = v_savefile.size();    //  서버에 저장되있는 파일수
// System.out.println("savefile ==  ==  == = > " + v_savefile + " size == = > " + v_savefile.size() );

        // 답변 유무 체크(답변 있을시 삭제불가)
        if ( this.selectBoard(v_tabseq, v_seq) == 0 ) { 

            try { 
                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);

                isOk1 = 1;
                isOk2 = 1;
                sql1 = "delete from TZ_BOARD where tabseq = ? and seq = ? ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);
                pstmt1.setInt(2, v_seq);
                isOk1 = pstmt1.executeUpdate();
                
                if ( pstmt1 != null ) { pstmt1.close(); }

                if ( v_upfilecnt > 0 ) { 
                    // sql2 = "delete from TZ_BOARDFILE where tabseq = ? and seq =  ?";
                    sql2 = "delete from TZ_BOARDFILE where tabseq = " + v_tabseq + " and seq = " +v_seq;
// System.out.println("sql2 == > " + sql2);
                    // pstmt2 = connMgr.prepareStatement(sql2);
                    // pstmt2.setInt(1, v_tabseq);
                    // pstmt2.setInt(2, v_seq);
                    // isOk2 = pstmt2.executeUpdate();
                    isOk2 = connMgr.executeUpdate(sql2);
                }
                if ( isOk1 > 0 && isOk2 > 0 ) { 
                    connMgr.commit();
                    if ( v_upfilecnt > 0 ) { 
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
                if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
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
   public int selectBoard(int tabseq, int seq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        int result = 0;

        try { 
            connMgr = new DBConnectionManager();

            sql  = "  select count(*) cnt                         ";
            sql += "  from                                        ";
            sql += "    (select tabseq, refseq, levels, position  ";
            sql += "       from TZ_BOARD                          ";
            sql += "      where tabseq = " + tabseq;
            sql += "        and seq = " + seq;
            sql += "     ) a, TZ_BOARD b                          ";
            sql += " where a.tabseq = b.tabseq                    ";
            sql += "   and a.refseq = b.refseq                    ";
            sql += "   and b.levels = (a.levels +1)                ";
            sql += "   and b.position = (a.position +1)            ";
System.out.println("sql += > " + sql);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = ls.getInt("cnt");
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
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
     public int insertUpFile(DBConnectionManager connMgr, int p_tabseq, int p_seq, RequestBox   box) throws Exception { 
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

        String s_userid = box.getSession("userid");

        try { 

            // ----------------------   자료 번호 가져온다 ----------------------------
            sql = "select nvl(max(fileseq), 0) from TZ_BOARDFILE where tabseq = " + p_tabseq + " and seq =   " + p_seq;

            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_fileseq = ls.getInt(1) + 1;
            ls.close();
            // ------------------------------------------------------------------------------------
System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +FILE_LIMIT);
            //// //// //// //// //// //// //// //// //   파일 table 에 입력  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql2 =  "insert into TZ_BOARDFILE(tabseq, seq, fileseq, realfile, savefile, luserid, ldate)";
            sql2 += " values (?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";

            pstmt2 = connMgr.prepareStatement(sql2);
            
            for ( int i = 0; i < FILE_LIMIT; i++ ) { 
                if ( !v_realFileName[i].equals(""))  {       //      실제 업로드 되는 파일만 체크해서 db에 입력한다
System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +v_realFileName[i]);
                    pstmt2.setInt(1, p_tabseq);
                    pstmt2.setInt(2, p_seq);
                    pstmt2.setInt(3, v_fileseq);
                    pstmt2.setString(4, v_realFileName[i]);
                    pstmt2.setString(5, v_newFileName[i]);
                    pstmt2.setString(6, s_userid);

                    isOk2 = pstmt2.executeUpdate();
                    v_fileseq++;
                }
            }
            if ( pstmt2 != null ) { pstmt2.close(); }
        }
        catch ( Exception ex ) { 
            FileManager.deleteFile(v_newFileName, FILE_LIMIT);      //  일반파일, 첨부파일 있으면 삭제..
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
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
        String sql  = "";
        String sql3 = "";
        ListSet             ls      = null;
        int isOk3 = 1;
        int v_tabseq = box.getInt("p_tabseq");
        String v_types   = box.getString("p_types");
        int v_seq = box.getInt("p_seq");

        try { 


            sql3 = "delete from TZ_BOARDFILE where tabseq = " + v_tabseq + " and seq =? and fileseq = ?";

            pstmt3 = connMgr.prepareStatement(sql3);
            for ( int i = 0; i < p_filesequence.size(); i++ ) { 
                int v_fileseq = Integer.parseInt((String)p_filesequence.elementAt(i));

                pstmt3.setInt(1, v_seq);
                pstmt3.setInt(2, v_fileseq);
                isOk3 = pstmt3.executeUpdate();
            }
            if ( pstmt3 != null ) { pstmt3.close(); }
        }
        catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql3);
            throw new Exception("sql = " + sql3 + "\r\n" + ex.getMessage() );
        }
        finally { 
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

}

