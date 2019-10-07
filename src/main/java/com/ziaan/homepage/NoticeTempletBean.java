// **********************************************************
//  1. 제      목: 공지사항템플릿 관리
//  2. 프로그램명 : NoticeTempletBean.java
//  3. 개      요: 공지사항템플릿 관리
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0공지사항
//  6. 작      성:  2005. 7.  14
//  7. 수      정:
// **********************************************************
package com.ziaan.homepage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class NoticeTempletBean { 
    private ConfigSet config;
    private int row;

    public NoticeTempletBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.manage.row") );  // 이 모듈의 페이지당 row 수를 셋팅한다
            row = 10; // 강제로 지정
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }


    /**
    * 공지사항 템플릿 리스트
    * @param box          receive from the form object and session
    * @return ArrayList   공지사항템플릿 리스트
    * @throws Exception
    */
    public ArrayList selectListNoticeTemplet(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox dbox        = null;

        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");
        int v_pageno        = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select seq, addate, adtitle, adname, templetfile, cnt, luserid, ldate from TZ_NOTICE_TEMPLET ";

            if ( !v_searchtext.equals("") ) {      //    검색어가 있으면
                if ( v_search.equals("adtitle") ) {                          //    제목으로 검색할때
                    sql += " where adtitle like " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("name") ) {                //    내용으로 검색할때
                    sql += " where name like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
            }

            sql += " order by seq desc ";

            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                       // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);               // 현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();  // 전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();  // 전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(row));

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


    /**
    * 공지사항 템플릿 상세보기
    * @param box          receive from the form object and session
    * @return ArrayList   조회한 상세정보
    * @throws Exception
    */
   public DataBox selectViewNoticeTemplet(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox dbox        = null;

        String v_seq = box.getString("p_seq");

        String v_filepath     = config.getProperty("dir.namo.template");
        String v_templetfile  = "";
        String v_contents     = "";
        try { 
            connMgr = new DBConnectionManager();

            sql += "  select seq, addate, adtitle, adname, templetfile, cnt, luserid, ldate from TZ_NOTICE_TEMPLET  ";
            sql += "  where seq    = " + StringManager.makeSQL(v_seq);

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox = ls.getDataBox();

                v_templetfile = dbox.getString("d_templetfile");
                v_contents = read(v_filepath + v_templetfile);
                dbox.put("d_contents",v_contents);
            }
            // 조회수 증가
            connMgr.executeUpdate("update TZ_NOTICE_TEMPLET set cnt = cnt + 1 where seq = " + v_seq);
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
    * 공지사항 템플릿 등록할때
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertNoticeTemplet(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String sql  = "";
        String sql1 = "";
        int isOk  = 0;
        int v_seq = 0;

        String v_adtitle   = box.getString("p_adtitle");
        String v_contents  = box.getString("p_contents");
        String s_userid   = box.getSession("userid");
        String s_name     = box.getSession("name");


        String v_templetfile = "";

        try { 
           connMgr = new DBConnectionManager();

           sql  = "select max(seq) from TZ_NOTICE_TEMPLET  ";
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) { 
               v_seq = ls.getInt(1) + 1;
           } else { 
               v_seq = 1;
           }


            /*********************************************************************************************/
            // 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
/*           
            SmeNamoMime namo = new SmeNamoMime(v_contents); // 객체생성 
            boolean result = namo.parse(); // 실제 파싱 수행 
            if ( !result ) { // 파싱 실패시 
                System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
                return 0;
            }
            if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
                String v_server = config.getProperty("autoever.url.value");
                String fPath = config.getProperty("dir.namo");   // 파일 저장 경로 지정
                String refUrl = config.getProperty("url.namo");  // 웹에서 저장된 파일을 접근하기 위한 경로
                String prefix = "notice" + v_seq;                // 파일명 접두어
                result = namo.saveFile(fPath, v_server +refUrl, prefix);   // 실제 파일 저장 
            }
            if ( !result ) { // 파일저장 실패시 
                System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
                return 0;
            }
            v_contents = namo.getContent(); // 최종 컨텐트 얻기
*/            
            /*********************************************************************************************/

           // HTML 템플릿 파일 생성
            String v_filepath = config.getProperty("dir.namo.template");
            long v_time = System.currentTimeMillis();
            v_templetfile = "namo_" + v_time + ".html";
            write(v_filepath + v_templetfile , v_contents);

           sql1 =  "insert into TZ_NOTICE_TEMPLET(seq, addate, adtitle, adname, templetfile, cnt, luserid, ldate )        ";
           sql1 += "     values (?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
           pstmt = connMgr.prepareStatement(sql1);

           pstmt.setInt   (1, v_seq);
           pstmt.setString(2, v_adtitle);
           pstmt.setString(3, s_name);
           pstmt.setString(4, v_templetfile);
           pstmt.setInt   (5, 0);
           pstmt.setString(6, s_userid);

           isOk = pstmt.executeUpdate();

           if ( isOk > 0 ) { 
               // templete_list.ini 파일 새로 작성
               makeTemplateList();
           }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql - > " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    * 공지사항 템플릿 수정하여 저장할때
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
     public int updateNoticeTemplet(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        int v_seq         = box.getInt("p_seq");
        String v_adtitle  = box.getString("p_adtitle");
        String v_contents = box.getString("p_contents");
        String v_templetfile = box.getString("p_templetfile");;

        String s_userid   = box.getSession("userid");
        String s_name     = box.getSession("name");

        try { 

            /*********************************************************************************************/
            // 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
/*            
            SmeNamoMime namo = new SmeNamoMime(v_contents); // 객체생성 
            boolean result = namo.parse(); // 실제 파싱 수행 
            if ( !result ) { // 파싱 실패시 
                System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
                return 0;
            }
            if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
                String v_server = config.getProperty("autoever.url.value");
                String fPath = config.getProperty("dir.namo");   // 파일 저장 경로 지정
                String refUrl = config.getProperty("url.namo");  // 웹에서 저장된 파일을 접근하기 위한 경로
                String prefix = "notice" + v_seq;                // 파일명 접두어
                result = namo.saveFile(fPath, v_server +refUrl, prefix);   // 실제 파일 저장 
            }
            if ( !result ) { // 파일저장 실패시 
                System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
                return 0;
            }
            v_contents = namo.getContent(); // 최종 컨텐트 얻기
*/            
            /*********************************************************************************************/

           // HTML 템플릿 파일 생성
            String v_filepath = config.getProperty("dir.namo.template");
            write(v_filepath + v_templetfile , v_contents);

            connMgr = new DBConnectionManager();

            sql  = " update TZ_NOTICE_TEMPLET set adtitle = ? ,     adname = ? ,       ";
            sql += "                              templetfile = ? , luserid = ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
            sql += " where  seq = ?                                                                    ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_adtitle);
            pstmt.setString(2, s_name);
            pstmt.setString(3, v_templetfile);
            pstmt.setString(4, s_userid);
            pstmt.setInt   (5, v_seq);

            isOk = pstmt.executeUpdate();

           if ( isOk > 0 ) { 
               // templete_list.ini 파일 새로 작성
               makeTemplateList();
           }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    * 공지사항 템플릿 삭제할때
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteNoticeTemplet(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        int v_seq  = box.getInt("p_seq");
        String v_templetfile = box.getString("d_templetfile");
        String v_filepath = config.getProperty("dir.namo.template");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " delete from TZ_NOTICE_TEMPLET   ";
            sql += "   where seq = ?  ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setInt(1, v_seq);
            isOk = pstmt.executeUpdate();
            if ( isOk > 0 ) { 
                // template 파일 삭제
                delete(v_filepath + v_templetfile);
                // template_list.ini 파일 새로 작성
                makeTemplateList();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql + "\r\n");
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    * 공지사항 템플릿 리스트
    * @param box          receive from the form object and session
    * @return ArrayList   공지사항템플릿 리스트
    * @throws Exception
    */
    public ArrayList selectAllNoticeTemplet() throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox dbox        = null;

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select seq, addate, adtitle, adname, templetfile, cnt, luserid, ldate from TZ_NOTICE_TEMPLET ";
            sql += " order by seq desc ";
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    * 템플릿 ini 파일 작성
    * @throws Exception
    */
    public void makeTemplateList() throws Exception { 
        ArrayList           list    = null;
        String v_templet_list    = "";
        String v_tem_title       = "";
        String v_tem_templetfile = "";
        String v_filepath        = config.getProperty("dir.namo.template");
        String v_server          = config.getProperty("autoever.url.value");
        String v_path            = config.getProperty("url.namo.template");
        String v_filename        = config.getProperty("name.namo.template.ini");


        try { 
           v_templet_list = "[템플릿 목록]\n";
           // 템플릿 리스트
           list = selectAllNoticeTemplet();

           if ( list != null ) { 
             for ( int i = 0; i < list.size(); i++ ) { 
               DataBox dbox   = (DataBox)list.get(i);
               v_tem_title       = dbox.getString("d_adtitle");
               v_tem_templetfile = dbox.getString("d_templetfile");
               v_templet_list = v_templet_list + v_tem_title + "=" +v_server +v_path + v_tem_templetfile + "\n";
             }
           }
           write(v_filepath + v_filename , v_templet_list);

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
    }

    /**
    * 파일작성
    * @param  path       파일  패스 +이름
    * @param  contents   컨텐츠 내용
    * @throws Exception
    */
    public void write(String path, String contents) throws Exception { 
        File file = null;

        try { 
           file = new File(path);
           FileWriter fw = new FileWriter(file);
           BufferedWriter owriter = new BufferedWriter( fw );
           owriter.write(contents);
           owriter.flush();
           owriter.close();
           fw.close();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
    }

    /**
     * 실제 파일을 삭제한다
     * @param path        삭제할 파일의 패스 +파일명
     * @throws Exception
     */
    public void delete(String path) throws Exception
    { 
        try { 
            File file = new File(path);
            file.delete();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
    }

    /**
     * 파일을 읽어서 내용을 리턴한다.
     * @param sPath          파일의 패스 +파일명
     * @return result         파일 내용을 담고 있는 스트링 객체
     * @throws Exception
     */
    public String read(String path) throws Exception
    { 
        String result = "";
        try { 
            FileReader fr = new FileReader(path);
            BufferedReader reader = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            int len = 4096; // 4k
            char[] buff = new char[len];
            while ( true)
            { 
                int rsize = reader.read(buff, 0, len);
                if ( rsize < 0)
                { 
                    break;
                }
                sb.append(buff, 0, rsize);
            }
            buff = null;
            result = sb.toString();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }

        return result;
    }

}

