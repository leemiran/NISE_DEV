// **********************************************************
//  1. 제      목: 템플릿 관리
//  2. 프로그램명: TempletBean.java
//  3. 개      요:
//  4. 환      경: JDK 1.3
//  5. 버      젼: 0.1
//  6. 작      성:  2005.7.8
//  7. 수      정:
// **********************************************************
package com.ziaan.templet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileMove;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class TempletBean { 

    public TempletBean() { }

    /**
    교육그룹리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   교육그룹리스트
    */
    public ArrayList SelectEduGroupList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        DataBox             dbox    = null;

        try { 
            list = new ArrayList();

            sql  = " SELECT a.grcode, a.grcodenm, b.type, a.domain ";
            sql += "   FROM  tz_grcode a left outer join tz_templet b   ";
            sql += "  on a.grcode = b.grcode       ";
            sql += "  WHERE a.grcode <> 'N000001'       ";
            sql += "  ORDER BY a.grcode asc              ";

            connMgr = new DBConnectionManager();

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



    /**
    템플릿 타입
    @param box          receive from the form object and session
    @return String      템플릿 타입
    */
    public String getGrcodeType(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        String v_grcode = box.getString("p_grcode");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select type from TZ_TEMPLET   ";
            sql += "  where grcode = " + StringManager.makeSQL(v_grcode);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = ls.getString("type");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }


    /**
    교육그룹별 타입 지정
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/
     public int InsertGrcodeType(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1 = "";
        String sql2 = "";
        int isOk = 0;

        String v_grcode     = box.getString("p_grcode");
        String v_type       = box.getString("p_type");
        String v_luserid    = box.getSession("userid"); // 세션변수에서 사용자 id를 가져온다.
        String v_domain     = box.getString("p_domain");

        try { 
            connMgr = new DBConnectionManager();
            // tz_templet initialize
            sql1 = " delete from TZ_TEMPLET where grcode=?";
            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_grcode);
            isOk = pstmt1.executeUpdate();

            // tz_templet insert (TZ_TEMPLET_MASTER 마스터 정보 COPY)
            sql2  = " insert into TZ_TEMPLET(  grcode       ,type         ,imgpath      ,mainflash    ,mainbg       ,subbg        ,toplogo      ,topbg          ";
            sql2 += "                         ,footbg       ,footcopyright,footleftimg  ,subtopbg     ,subtopimg    ,ldate        ,luserid      )               ";
            sql2 += "                 select   ?            ,type         ,imgpath      ,mainflash    ,mainbg       ,subbg        ,toplogo      ,topbg          ";
            sql2 += "                         ,footbg       ,footcopyright,footleftimg  ,subtopbg     ,subtopimg    ,to_char(sysdate,'YYYYMMDDHH24MISS')  ,?    ";
            sql2 += "                   from  TZ_TEMPLET_MASTER ";
            sql2 += "                  where  type = ?          ";

            pstmt2 = connMgr.prepareStatement(sql2);

            pstmt2.setString(1,  v_grcode);
            pstmt2.setString(2,  v_luserid);
            pstmt2.setString(3,  v_type);
            isOk = pstmt2.executeUpdate();  
            
            // 파일 셍성- 기존파일 삭제
            if ( isOk > 0 ) { 
                createIndex(v_grcode, v_type, v_domain);
            }

        } catch ( Exception ex ) { 
            isOk = 0;
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql2 = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }



     /**
     교육그룹별 INDEX 파일 생성  (index+교육그룹코드.jsp)
     @param grcode    그룹 코드
     @param type      템플릿 타입
     **/
     public void createIndex(String grcode, String type, String domain) throws Exception {
         File file = null;
         ConfigSet conf = new ConfigSet();
         String home = conf.getProperty("dir.home");     // 생성될 위치
         String filename = "index" + grcode + ".jsp";
         String fileseparator = File.separator;//File.separator.equals("\\") ? File.separator + File.separator : File.separator;
         String path = home + "gate" + fileseparator;
         String contents = "";

         contents  = "<%@ page import = \"com.ziaan.library.*\" %>                                  \n";
         contents += "<%                                                                            \n";
         contents += "    RequestBox box = (RequestBox)request.getAttribute(\"requestbox\");        \n";
         contents += "    if (box == null) box = RequestManager.getBox(request);                    \n";
         contents += "    box.setSession(\"tem_type\",\""+type+"\");                                \n";
         contents += "    box.setSession(\"tem_grcode\",\""+grcode+"\");                            \n";
         contents += "	  box.setSession(\"s_site_gubun\", \"gate\");								\n";
         contents += "	  box.setSession(\"s_domain\", \""+domain+"\");								\n";
         contents += "%>                                                                            \n";
         contents += "<html>                                                                        \n";
         contents += "<head>                                                                        \n";
         contents += "<Script Language='JavaScript'>                                                \n";
         contents += "<!--                                                                          \n";
         contents += "    document.location.replace(\"/servlet/controller.homepage.MainServlet\");  \n";
         contents += "//-->                                                                         \n";
         contents += "</Script>                                                                     \n";
         contents += "</head>                                                                       \n";
         contents += "<body>                                                                        \n";
         contents += "</body>                                                                       \n";
         contents += "</html>                                                                       \n";

         try {
            // 삭제
            delete(path);
            // 생성
            file = new File(path);
            if(!file.exists())
            	file.mkdirs();
            
            file = new File(path + filename);
            FileWriter fw = new FileWriter(file);
            BufferedWriter owriter = new BufferedWriter( fw );
            owriter.write(contents);
            owriter.flush();
            owriter.close();
            fw.close();
         } catch(Exception ex) {
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception(ex.getMessage());
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
    템플릿 상세정보
    @param box      receive from the form object and session
    @return DataBox
    */
    public DataBox selectTemplet(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        String sql         = "";
        DataBox dbox       = null;

       String v_grcode     = box.getString("p_grcode");

       sql  = " select grcode ,type , imgpath ,mainflash ,mainbg ,subbg ,toplogo ,topbg,       ";
       sql += "        footbg ,footcopyright, footleftimg ,subtopbg ,subtopimg ,ldate ,luserid ";
       sql += "   from TZ_TEMPLET                                                              ";
       sql += "  where grcode =  " +SQLString.Format(v_grcode);
       try { 
           connMgr = new DBConnectionManager();
           ls = connMgr.executeQuery(sql);

           if ( ls.next() ) { 
               dbox = ls.getDataBox();
           }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
            return dbox;
    }


    /**
    * 템플릿 메인 수정할때
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateTempletMain(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String sql  = "";
        int isOk  = 0;
        int i     = 1;
        String v_grcode    = box.getString("p_grcode");
        String v_type      = box.getString("p_type");
        String v_imgpath   = box.getString("p_imgpath");
//        String v_imgpath   = "\\images\\user\\homepage\\type1\\";    // 임시 (디비 마스터 정보 변경)
        String s_userid   = box.getSession("userid");

        // 업로드된 파일명
        String v_mainbg_old        = box.getNewFileName("p_mainbg");
        String v_subbg_old         = box.getNewFileName("p_subbg");
        String v_mainflash_old     = box.getNewFileName("p_mainflash");
        String v_toplogo_old       = box.getNewFileName("p_toplogo");
        String v_topbg_old         = box.getNewFileName("p_topbg");
        String v_footbg_old        = box.getNewFileName("p_footbg");
        String v_footcopyright_old = box.getNewFileName("p_footcopyright");
        String v_footleftimg_old   = box.getNewFileName("p_footleftimg");

        // 변경될 파일명
        String v_mainbg_new        = v_grcode + "_bg_maintotal.gif";
        String v_subbg_new         = v_grcode + "_bg_subtotal.gif";
        String v_mainflash_new     = v_grcode + "_mainflash.swf";
        String v_toplogo_new       = v_grcode + "_logo.gif";
        String v_topbg_new         = v_grcode + "_bg_maintop.gif";
        String v_footbg_new        = v_grcode + "_bg_footer.gif";
        String v_footcopyright_new = v_grcode + "_footer_copy.gif";
        String v_footleftimg_new   = v_grcode + "_footer_left.gif";

            // 파일 이동
            ConfigSet conf = new ConfigSet();
            String v_thisPath = conf.getProperty("dir.upload.default");     //  이동하기전 경로
            String v_thatPath = conf.getProperty("dir.templet." +v_type);   // 이동할 경로

            FileMove filemove = new FileMove();
            if ( !v_mainbg_old.equals(""))        filemove.move(v_thatPath, v_thisPath, v_mainbg_old,        v_mainbg_new);
            if ( !v_subbg_old.equals(""))         filemove.move(v_thatPath, v_thisPath, v_subbg_old,         v_subbg_new);
            if ( !v_mainflash_old.equals(""))     filemove.move(v_thatPath, v_thisPath, v_mainflash_old,     v_mainflash_new);
            if ( !v_toplogo_old.equals(""))       filemove.move(v_thatPath, v_thisPath, v_toplogo_old,       v_toplogo_new);
            if ( !v_topbg_old.equals(""))         filemove.move(v_thatPath, v_thisPath, v_topbg_old,         v_topbg_new);
            if ( !v_footbg_old.equals(""))        filemove.move(v_thatPath, v_thisPath, v_footbg_old,        v_footbg_new);
            if ( !v_footcopyright_old.equals("")) filemove.move(v_thatPath, v_thisPath, v_footcopyright_old, v_footcopyright_new);
            if ( !v_footleftimg_old.equals(""))   filemove.move(v_thatPath, v_thisPath, v_footleftimg_old,   v_footleftimg_new);


         try { 
            connMgr = new DBConnectionManager();

           sql  = " update TZ_TEMPLET set  imgpath = ? ";

           if ( !v_mainbg_old.equals(""))        sql += " ,mainbg        =  ? ";
           if ( !v_subbg_old.equals(""))         sql += " ,subbg         =  ? ";
           if ( !v_mainflash_old.equals(""))     sql += " ,mainflash     =  ? ";
           if ( !v_toplogo_old.equals(""))       sql += " ,toplogo       =  ? ";
           if ( !v_topbg_old.equals(""))         sql += " ,topbg         =  ? ";
           if ( !v_footbg_old.equals(""))        sql += " ,footbg        =  ? ";
           if ( !v_footcopyright_old.equals("")) sql += " ,footcopyright =  ? ";
           if ( !v_footleftimg_old.equals(""))   sql += " ,footleftimg   =  ? ";

           sql += "       ,ldate= to_char(sysdate,'YYYYMMDDHH24MISS') ,luserid = ?   ";
           sql += " where grcode = ?   ";

           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(i++,  v_imgpath);
           if ( !v_mainbg_old.equals(""))        pstmt.setString(i++,  v_mainbg_new       );
           if ( !v_subbg_old.equals(""))         pstmt.setString(i++,  v_subbg_new        );
           if ( !v_mainflash_old.equals(""))     pstmt.setString(i++,  v_mainflash_new    );
           if ( !v_toplogo_old.equals(""))       pstmt.setString(i++,  v_toplogo_new      );
           if ( !v_topbg_old.equals(""))         pstmt.setString(i++,  v_topbg_new        );
           if ( !v_footbg_old.equals(""))        pstmt.setString(i++,  v_footbg_new       );
           if ( !v_footcopyright_old.equals("")) pstmt.setString(i++,  v_footcopyright_new);
           if ( !v_footleftimg_old.equals(""))   pstmt.setString(i++,  v_footleftimg_new  );
           pstmt.setString(i++,  s_userid);
           pstmt.setString(i++,  v_grcode);

           isOk = pstmt.executeUpdate();

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql - > " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    * 템플릿 서브 수정할때
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateTempletSub(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String sql  = "";
        int isOk  = 0;
        int i     = 1;
        String v_grcode    = box.getString("p_grcode");
        String v_type      = box.getString("p_type");
        String v_imgpath   = box.getString("p_imgpath");
        String s_userid   = box.getSession("userid");

        // 업로드된 파일명
        String v_subtopbg_old      = box.getNewFileName("p_subtopbg");
        String v_subtopimg_old     = box.getNewFileName("p_subtopimg");

        // 변경될 파일명
        String v_subtopbg_new      = v_grcode + "_bg_top.gif";
        String v_subtopimg_new     = v_grcode + "_subimg.jpg";

            // 파일 이동
            ConfigSet conf = new ConfigSet();
            String v_thisPath = conf.getProperty("dir.upload.default");     //  이동하기전 경로
            String v_thatPath = conf.getProperty("dir.templet." + v_type);   // 이동할 경로

            FileMove filemove = new FileMove();
            if ( !v_subtopbg_old.equals(""))   filemove.move(v_thatPath, v_thisPath, v_subtopbg_old,      v_subtopbg_new);
            if ( !v_subtopimg_old.equals(""))  filemove.move(v_thatPath, v_thisPath, v_subtopimg_old,     v_subtopimg_new);


         try { 
            connMgr = new DBConnectionManager();

           sql  = " update TZ_TEMPLET set  imgpath = ? ";

           if ( !v_subtopbg_old.equals(""))      sql += " ,subtopbg       =  ? ";
           if ( !v_subtopimg_old.equals(""))     sql += " ,subtopimg         =  ? ";

           sql += "       ,ldate= to_char(sysdate,'YYYYMMDDHH24MISS') ,luserid = ?   ";
           sql += " where grcode = ?   ";

           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(i++,  v_imgpath);
           if ( !v_subtopbg_old.equals(""))    pstmt.setString(i++,  v_subtopbg_new       );
           if ( !v_subtopimg_old.equals(""))   pstmt.setString(i++,  v_subtopimg_new        );
           pstmt.setString(i++,  s_userid);
           pstmt.setString(i++,  v_grcode);

           isOk = pstmt.executeUpdate();

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql - > " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


/* ==  ==  ==  ==  ==  ==  ==  == 컨텐츠/메뉴 관련 ==  ==  ==  ==  ==  ==  ==  ==  == =*/


    /**
    마스터 메뉴리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   마스터 메뉴리스트
    */
    public ArrayList SelectMenuMaster(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        DataBox             dbox    = null;

        String v_gubun = box.getString("p_gubun");
        String v_position = box.getString("p_position");
        String v_kind = box.getString("p_kind");

        try { 
            list = new ArrayList();

            sql  = " SELECT gubun, menuid, kind, position, orders, menuname, menuurl, menuimg,menuoverimg ";
            sql += "   FROM tz_homemenu_master         ";
            sql += "  WHERE gubun = " +SQLString.Format(v_gubun);
            sql += "    AND position =  " + SQLString.Format(v_position);
            sql += "    AND kind = " + SQLString.Format(v_kind);
            sql += "  ORDER BY orders asc              ";

            connMgr = new DBConnectionManager();

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

    /**
    선택된 메뉴리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   마스터 메뉴리스트
    */
    public ArrayList SelectMenuList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        DataBox             dbox    = null;

        String v_grcode = box.getString("p_grcode");
        String v_gubun = box.getString("p_gubun");
        String v_position = box.getString("p_position");
        String v_kind = box.getString("p_kind");
        
        try { 
            list = new ArrayList();

            sql  = " SELECT gubun, menuid, kind, position, orders, menuname, menuurl, menuimg,menuoverimg ";
            sql += "   FROM tz_homemenu              ";
            sql += "  WHERE grcode = " + SQLString.Format(v_grcode);
            sql += "    AND gubun  = " + SQLString.Format(v_gubun);
            sql += "    AND position =  " + SQLString.Format(v_position);
            sql += "    AND kind = " + SQLString.Format(v_kind);
            sql += "  ORDER BY orders asc              ";
            
            connMgr = new DBConnectionManager();

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



    /**
    선택된 메뉴리스트 조회
    @param grcode     교육그룹 코드
    @param gubun      구분 (MB : MAIN, SB : SUB )
    @param position   위치
    @return ArrayList   마스터 메뉴리스트
    */
    public ArrayList SelectMenuList(String grcode, String gubun, String position, String kind) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        DataBox             dbox    = null;

        try { 
            list = new ArrayList();

            sql  = " SELECT gubun, menuid, kind, position, orders, menuname, menuurl, menuimg,menuoverimg ";
            sql += "   FROM tz_homemenu              ";
            sql += "  WHERE grcode = " + SQLString.Format(grcode);
            sql += "    AND gubun  = " + SQLString.Format(gubun);
            sql += "    AND position =  " + SQLString.Format(position);
            sql += "    AND kind = " + SQLString.Format(kind);
            sql += "  ORDER BY orders asc              ";
            connMgr = new DBConnectionManager();

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
    교육그룹별 메뉴등록
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/
     public int InsertMenu(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1 = "";
        String sql2 = "";
        int isOk = 1;
        int isOk_check = 0;

        String v_grcode   = box.getString("p_grcode");
        String v_gubun    = box.getString("p_gubun");
        String v_position = box.getString("p_position");
        String v_kind     = box.getString("p_kind");
        String v_luserid  = box.getSession("userid"); // 세션변수에서 사용자 id를 가져온다.

        String v_order    = box.getString("p_order");
        String v_menuid   = "";
        int    v_orders   = 0;
        StringTokenizer st = new StringTokenizer(v_order, "|");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // tz_templet initialize
            sql1 = " delete from TZ_HOMEMENU where grcode=? and gubun = ? and position = ? and kind = ? ";
            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_grcode);
            pstmt1.setString(2, v_gubun);
            pstmt1.setString(3, v_position);
            pstmt1.setString(4, v_kind);
            isOk = pstmt1.executeUpdate();
            isOk = 1;

            // TZ_HOMEMENU insert (TZ_HOMEMENU_MASTER 마스터 정보 COPY)
            sql2  = " insert into TZ_HOMEMENU( grcode      ,gubun        ,menuid      ,kind         ,position  ,orders           ";
            sql2 += "                         ,menuname    ,menuurl      ,menuimg     ,menuoverimg  ,ldate     ,luserid      )   ";
            sql2 += "                 select   ?           ,gubun        ,?           ,kind         ,position  ,?                ";
            sql2 += "                         ,menuname    ,menuurl      ,menuimg     ,menuoverimg  ,to_char(sysdate,'YYYYMMDDHH24MISS')  ,?  ";
            sql2 += "                   from  TZ_HOMEMENU_MASTER ";
            sql2 += "                  where  gubun = ? and position = ? and menuid = ? and kind = ?";
            pstmt2 = connMgr.prepareStatement(sql2);

            while ( st.hasMoreTokens() ) { 
                v_menuid = st.nextToken();
                v_orders++;

                pstmt2.setString(1, v_grcode);
                pstmt2.setString(2, v_menuid);
                pstmt2.setString(3, String.valueOf(v_orders));
                pstmt2.setString(4, v_luserid);
                pstmt2.setString(5, v_gubun);
                pstmt2.setString(6, v_position);
                pstmt2.setString(7, v_menuid);
                pstmt2.setString(8, v_kind);
                
                isOk_check = pstmt2.executeUpdate();
                if ( isOk_check == 0) isOk = 0;
            }

            if ( isOk > 0) connMgr.commit();
            else          connMgr.rollback();
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql2 = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            connMgr.setAutoCommit(true);
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }





/* ==  ==  ==  ==  ==  ==  ==  == 홈페이지 관련 ==  ==  ==  ==  ==  ==  ==  ==  == =*/

    /**
    템플릿 상세정보
    @param p_grcode     교육그룹코드
    @return DataBox     템플릿 정보
    */
    public static DataBox getTemplet(String p_grcode) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        String sql         = "";
        DataBox dbox       = null;


       sql  = " select grcode ,type , imgpath ,mainflash ,mainbg ,subbg ,toplogo ,topbg       ";
       sql += "        ,footbg ,footcopyright, footleftimg ,subtopbg ,subtopimg ,ldate ,luserid ";
       sql += "   from TZ_TEMPLET                                                              ";
       sql += "  where grcode =  " +SQLString.Format(p_grcode);
       try { 
           connMgr = new DBConnectionManager();
           ls = connMgr.executeQuery(sql);

           if ( ls.next() ) { 
               dbox = ls.getDataBox();
           }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
            return dbox;
    }


    /**
    서브화면 메뉴 리스트
    @param box          receive from the form object and session
    @return ArrayList   메뉴 리스트
    */
    public static ArrayList getMenuList(String grcode, String gubun, String position, String kind) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        DataBox             dbox    = null;

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " SELECT gubun, menuid, kind, position, orders, menuname, menuurl, menuoverimg as menuimg, menuoverimg ";
            sql += "   FROM tz_homemenu              ";
            sql += "  WHERE grcode   = " + SQLString.Format(grcode);
            sql += "    AND menuid    = " + SQLString.Format("0" + gubun);
            sql += "    AND gubun = '0' \n";
            sql += "    AND position =  " + SQLString.Format(position);
            sql += "    AND kind = " + SQLString.Format(kind);
            sql += " union all \n";
            sql += " SELECT gubun, menuid, kind, position, orders, menuname, menuurl, menuimg, menuoverimg ";
            sql += "   FROM tz_homemenu              ";
            sql += "  WHERE grcode   = " + SQLString.Format(grcode);
            sql += "    AND gubun    = " + SQLString.Format(gubun);
            sql += "    AND position =  " + SQLString.Format(position);
            sql += "    AND kind = " + SQLString.Format(kind);
            sql += "  ORDER BY gubun, orders asc              ";
            
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
    서브화면 메뉴 리스트
    @param box          receive from the form object and session
    @return ArrayList   메뉴 리스트
    */
    public static ArrayList getMenuList2(String grcode, String gubun, String position, String kind) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        DataBox             dbox    = null;

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql += " SELECT gubun, menuid, kind, position, orders, menuname, menuurl, menuimg, menuoverimg ";
            sql += "   FROM tz_homemenu              ";
            sql += "  WHERE grcode   = " + SQLString.Format(grcode);
            sql += "    AND gubun    = " + SQLString.Format(gubun);
            sql += "    AND position =  " + SQLString.Format(position);
            sql += "    AND kind = " + SQLString.Format(kind);
            sql += "  ORDER BY gubun, orders asc              ";
            
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
    서브 갯수 가져오기
    @param box          receive from the form object and session
    @return ArrayList   서브 갯수 가져오기
    */
    public ArrayList getSubMenuCntList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        DataBox             dbox    = null;
        String s_grcode = box.getString("p_grcode");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = "select gubun													\n"
            	+ "from tz_homemenu													\n"
            	+ "where grcode= " + StringManager.makeSQL(s_grcode) + "			\n"
            	+ "and kind = 'SB'													\n"
            	+ "group by gubun													\n";
            
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
    서브 첫번째 url 가져오기
    @param box          receive from the form object and session
    @return ArrayList    서브 첫번째 url 가져오기
    */
    public ArrayList selectSubmenuList(String grcode) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String sql  = "";
        DataBox             dbox    = null;
      
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

//            sql = "select a.gubun, a.orders, a.menuurl, a.menuimg				\n"
//            	+ "from tz_homemenu a, (							\n"
//            	+ "		select gubun, min(orders) orders			\n"
//            	+ "		from tz_homemenu							\n"
//            	+ "		where kind = 'SB'							\n"
//            	+ "		and grcode= " + StringManager.makeSQL(grcode) + "		\n"
//            	+ "		group by gubun								\n"
//            	+ ") b												\n"
//            	+ "where a.gubun = b.gubun							\n"
//            	+ "and a.orders = b.orders							\n"
//            	+ "and a.kind = 'SB'								\n"
//            	+ "and a.grcode = " + StringManager.makeSQL(grcode) + "		\n";
            
            sql = "select x.grcode, x.gubun, x.kind, x.menuname, x.menuimg, y.menuurl		\n"
            	+ "from (																	\n"
            	+ "    select grcode, gubun, kind, menuname, menuimg, menuid				\n"
            	+ "    from tz_homemenu a													\n"
            	+ "    where grcode =  " + StringManager.makeSQL(grcode) + "				\n"
            	+ "    and gubun = '0'														\n"
            	+ "    and kind = 'SB'														\n"
            	+ "    order by orders														\n"
            	+ ") x left outer join (													\n"
            	+ "    select grcode, gubun, kind, menuurl									\n"
            	+ "    from tz_homemenu a													\n"
            	+ "    where grcode =  " + StringManager.makeSQL(grcode) + "				\n"
            	+ "    and kind = 'SB'														\n"
            	+ "    and orders = 1														\n"    
            	+ ") y																		\n"
            	+ "on x.grcode = y.grcode													\n"
            	+ "and to_number(x.menuid) = y.gubun										\n";
            
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
}