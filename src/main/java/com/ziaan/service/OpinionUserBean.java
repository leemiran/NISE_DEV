
package com.ziaan.service;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.Mailing;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class OpinionUserBean  {
    private     ConfigSet   config;
    private     int         row;
	private Logger logger = Logger.getLogger(this.getClass());
    public OpinionUserBean () { 
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
    public ArrayList selectList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
        int v_pageno = box.getInt("p_pageno");
        
        String s_grcode     = box.getSession("tem_grcode");
        String s_gadmin     = box.getSession("gadmin");
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");
        String s_userid     = box.getSession("userid");
        String v_gadmin     = "";
        
        if (!s_gadmin.equals("")) {
        	v_gadmin = s_gadmin.substring(0,1);
        }

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "	select	tabseq, seq, title, userid, name, --DECODE(userid,'"+s_userid+"','Y',NVL(ISOPEN,'N')) AS chk,		\n";
            sql	+= "		content, indate, refseq, levels, position, 	\n";
            sql += "		upfile, cnt, recomcnt, luserid, ldate, 		\n";
            sql += "		gubunA, gubunB, isopen, email, hasanswer,	\n";
            sql += "		realfile, savefile, gadmin, pwd, auserid, 	\n";
            sql += "		adate, acontent, nvl(istop,'N') as istop	\n";
            sql += "	from tz_center_board		\n";
            sql += "	where	tabseq	= " + v_tabseq + "		\n";
//            sql += "		and (isopen='Y' or userid='"+s_userid+"')	\n";
            
//            if (!s_grcode.equals("")) {
//            	sql += "	and grcode like " + SQLString.Format("%"+s_grcode+"%") ;
//            }else{
//            	sql += "	and grcode is null" ;
//            }
            
            if ( !v_searchtext.equals("") ) {                //    검색어가 있으면
                if ( v_search.equals("name") ) {              //    이름으로 검색할때
                    sql += " and name like " + StringManager.makeSQL("%" + v_searchtext + "%") + "	\n";
                }
                else if ( v_search.equals("title") ) {        //    제목으로 검색할때
                    sql += " and title like " + StringManager.makeSQL("%" + v_searchtext + "%") + "	\n";
                }
                else if ( v_search.equals("content") ) {     //    내용으로 검색할때
                    sql += " and content like " + StringManager.makeSQL("%" + v_searchtext + "%"); //   Oracle 9i 용
                }
            }
            
            sql += " order by istop desc, refseq desc, position asc  \n";
//System.out.println("-------------리스트" + sql);
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
//System.out.println("==v_tabseq==="+v_tabseq);
//System.out.println("==v_seq==="+v_seq);
        try { 
            connMgr = new DBConnectionManager();

            sql  = "	select	tabseq, seq, title, userid, name, 				";
            sql	+= "		content, indate, refseq, levels, position, 			";
            sql += "		upfile, cnt, recomcnt, luserid, ldate, 				";
            sql += "		gubunA, gubunB, isopen, email, hasanswer,			";
            sql += "		realfile, savefile, gadmin, pwd, auserid, 			";
            sql += "		adate, acontent, atitle, arealfile realfile2, asavefile savefile2    	 			    ";
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
    * 새로운 자료실 내용 등록
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int Merge(RequestBox box) throws Exception { 
    	 ConfigSet conf = new ConfigSet();
    	 Mailing mailing = null;
    	 DBConnectionManager connMgr = null;
        ListSet rs1   = null;
        PreparedStatement pstmt1  = null;
        String sql    = "";
        String sql1   = "";
        int isOk1     = 1;
        int v_seq     = 0;  // 등록될 게시판 일련번호... max값.
        int preIdx    = 1;

        String v_type       = box.getString("p_type");
        
        int v_tabseq     = box.getString("p_tabseq").equals("") ? 1 : box.getInt("p_tabseq");
        
        String v_title      = box.getString("p_title");
        String v_content    = box.getString("p_content").replace("\r\n","<br>");   
        String v_email	    = box.getString("p_email");
        String v_gubuna	    = box.getString("p_gubuna");
        String v_isopen	    = box.getString("p_isopen");
        String v_realFile   = box.getRealFileName("p_file1");
        String v_saveFile   = box.getNewFileName("p_file1");
        String v_gadmin     = box.getSession("gadmin");
        String s_grcode     = box.getSession("tem_grcode");
                
        
        v_title = StringManager.replace(v_title,"\"","&quot;");
        
        // 일반 회원이라면 테그를 쓰지 못한다. 
        if(box.getSession("gadmin").equals("ZZ"))
        {
            v_title = StringManager.replace(v_title,"<","&lt;");
            v_title = StringManager.replace(v_title,">","&gt;");
            v_content = StringManager.replace(v_content,"\r\n","<br>");   
            v_content = StringManager.replace(v_content,"\r\n","<br>");   
        }
        
        
        
        int v_seq_       = box.getInt("p_seq");
        int v_refseq     = box.getString("p_refseq").equals("") ? 0 : box.getInt("p_refseq");
        int v_levels     = box.getString("p_levels").equals("") ? 0 : box.getInt("p_levels");
        int v_position   = box.getString("p_position").equals("") ? 0 : box.getInt("p_position");
//System.out.println("v_refseq 1::::::::::::::::::" +v_refseq);
//System.out.println("v_levels 1::::::::::::::::::" +v_levels);
//System.out.println("v_position 1::::::::::::::::" +v_position);       
//       
//System.out.println("v_type 1::::::::::::::::" +v_type);       
        
        
        
        String s_userid     = box.getSession("userid");
        String s_userNm     = box.getSession("name");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            // 답글
            if(v_type.equals("reply"))
            {
                
                // 입력으로 넘어온 경우
                //----------------------   게시판 번호 가져온다 ----------------------------
                sql = "select nvl(max(seq), 0) from TZ_CENTER_BOARD where tabseq = " + v_tabseq;
                rs1 = connMgr.executeQuery(sql);
                if ( rs1.next() ) { 
                    v_seq = rs1.getInt(1) + 1;
                }
                rs1.close();
            	
                // ----------------------   게시판 table 에 입력  --------------------------
                sql1 =  " insert into TZ_CENTER_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate, gadmin, sangdam_gubun, gubuna) ";
                sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?, ?, ?)";

                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);
                pstmt1.setInt(2, v_seq);
                pstmt1.setString(3, s_userid);
                pstmt1.setString(4, s_userNm);
                pstmt1.setString(5, v_title);
                pstmt1.setString(6, v_content);
                pstmt1.setInt(7, 0);
                pstmt1.setInt(8, v_refseq);
                pstmt1.setInt(9, v_levels+1);
                pstmt1.setInt(10, v_position+1);
                pstmt1.setString(11, s_userid);
                pstmt1.setString(12, v_gadmin);
                pstmt1.setString(13, "");
                pstmt1.setString(14, v_gubuna);


                isOk1 = pstmt1.executeUpdate();
                
                
                
            }else if(v_type.equals("update")){
            
          //  if (v_seq_ > 0) {	// 수정으로 들어온 경우
                
                sql1 = "update TZ_CENTER_BOARD set title = ?, content=?, gubuna = ?,  isopen=?, email = ? ";//, userid = ?, name = ?, luserid = ?, ldate = to_char(sysdate, 'YYYYMMDDHH24MISS')";
                sql1 += "                          , REALFILE = ?, SAVEFILE = ?  ";
                sql1 += "  where tabseq = ? and seq = ? ";

                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setString(1, v_title);
                pstmt1.setString(2, v_content);
                pstmt1.setString(3, v_gubuna);
                pstmt1.setString(4, v_isopen);
                pstmt1.setString(5, v_email);
                pstmt1.setString(6, v_realFile);
                pstmt1.setString(7, v_saveFile);
                pstmt1.setInt(8, v_tabseq);
                pstmt1.setInt(9, v_seq_);

                isOk1 = pstmt1.executeUpdate();

            } else {            // 입력으로 넘어온 경우
                //----------------------   게시판 번호 가져온다 ----------------------------
                sql = "select nvl(max(seq), 0) from TZ_CENTER_BOARD where tabseq = " + v_tabseq;
                rs1 = connMgr.executeQuery(sql);
                if ( rs1.next() ) { 
                    v_seq = rs1.getInt(1) + 1;
                }
                rs1.close();
            	
                // ----------------------   게시판 table 에 입력  --------------------------
                sql1 =  " insert into TZ_CENTER_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate, gadmin, sangdam_gubun, gubuna) ";
                sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?, ?, ?)";

                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);
                pstmt1.setInt(2, v_seq);
                pstmt1.setString(3, s_userid);
                pstmt1.setString(4, s_userNm);
                pstmt1.setString(5, v_title);
                pstmt1.setString(6, v_content);
                pstmt1.setInt(7, 0);
                pstmt1.setInt(8, v_seq);
                pstmt1.setInt(9, 1);
                pstmt1.setInt(10, 1);
                pstmt1.setString(11, s_userid);
                pstmt1.setString(12, v_gadmin);
                pstmt1.setString(13, "");
                pstmt1.setString(14, v_gubuna);

                isOk1 = pstmt1.executeUpdate();
                
            }
            
 
            
            

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
    
	
}
