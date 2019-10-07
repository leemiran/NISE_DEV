// **********************************************************
// 1. ��      ��:
// 2. ���α׷���: CommunityAdminRoomBean.java
// 3. ��      ��:
// 4. ȯ      ��: JDK 1.3
// 5. ��      ��: 0.1
// 6. ��      ��: Administrator 2003-08-29
// 7. ��      ��:
// 
// **********************************************************

package com.ziaan.community;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class CommunityAdminRoomBean { 
    private ConfigSet config;
    private static int row=10;
//    private String v_type = "PQ";
//    private static final String FILE_TYPE = "p_file";           //      ���Ͼ��ε�Ǵ� tag name
//    private static final int FILE_LIMIT = 1;                    //    �������� ���õ� ����÷�� ����


    public CommunityAdminRoomBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );  // �� ����� �������� row ���� �����Ѵ�
            row = 10; // ������ ����
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    * Ŀ�´�Ƽ ��ȸ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   Ŀ�´�Ƽ ��ȸ����Ʈ
    * @throws Exception
    */
    public ArrayList selectList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
//        String              sql2    = "";
        DataBox             dbox    = null;

        String v_searchtext    = box.getString("p_searchtext");
        String v_select        = box.getString("p_select");
        String v_orderby       = box.getStringDefault("p_orderby","cmu_nm");

        String v_if_close_fg   = box.getStringDefault("p_if_close_fg","1");
        int    v_pageno        = box.getInt("p_pageno");
//        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");
        try { 
            connMgr = new DBConnectionManager();

//            sql  = " select a.*,rownum rowseq "
              sql = "select a.cmuno           cmuno           , a.cmu_nm        cmu_nm      , a.in_method_fg     in_method_fg   , a.search_fg search_fg"
                  + "              , a.data_passwd_fg  data_passwd_fg  , a.display_fg    display_fg  , a.type_l           type_l         ,a.type_m type_m"
                  + "             , a.intro           intro           , a.img_path      img_path    , a.layout_fg        layout_fg      , a.html_skin_fg html_skin_fg"
                  + "              , a.read_cnt        read_cnt        , a.member_cnt    member_cnt  , a.close_fg         close_fg"
                  + "             , a.close_reason    close_reason    , a.close_dte     close_dte   , a.close_userid     close_userid"
                  + "              , a.hold_fg         hold_fg         , a.accept_dte    accept_dte  , a.accept_userid    accept_userid  , a.register_dte  register_dte"
                  + "             , a.register_userid register_userid , a.modifier_dte  modifier_dte, a.modifier_userid  modifier_userid "
                  + "              , b.userid userid, b.kor_name        kor_name        , c.codenm      grade_nm,d.codenm type_l_nm,e.codenm type_m_nm"
                  + "    from tz_cmubasemst a "
                  + "        ,(select cmuno,userid,kor_name,grade from tz_cmuusermst where grade='01' and close_fg='1') b"
                  + "        ,(select cmuno cmuno,grcode grcode,kor_nm codenm from tz_cmugrdcode) c"
                  + "        ,(select code code,codenm  codenm from tz_code where gubun='0052' and levels=1)d"
                  + "        ,(select code code,codenm  codenm from tz_code where gubun='0052' and levels=2)e"
                  + "  where a.cmuno  = b.cmuno"
                  + "    and b.cmuno  = c.cmuno"
                  + "    and a.type_l = d.code"
                  + "    and a.type_m = e.code"
                  + "    and b.grade  = c.grcode"
                  + "    and a.close_fg  = '" +v_if_close_fg + "'"
            ;

            if ( !v_searchtext.equals("") ) {      // �˻�� ������
                 if ( v_select.equals("cmu_nm"))      sql += " and lower(a.cmu_nm)    like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 if ( v_select.equals("intro"))      sql += " and lower(a.intro)    like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";  
                 if ( v_select.equals("kor_name"))   sql += " and lower(b.kor_name) like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
            }
            
            if ( v_orderby.equals("cmu_nm"))    sql += " order by a.cmu_nm asc";
            if ( v_orderby.equals("kor_name"))  sql += " order by b.kor_name asc";
            if ( v_orderby.equals("accept_dte"))sql += " order by a.accept_dte asc";

//            sql += " ) a";
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(row);                         // �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);                 // ������������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();    // ��ü ������ ���� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();    // ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum"  , new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount" , new Integer(row));
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
    * Ŀ�´�Ƽ �ź� �� ��� /����ó��
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateCommunity(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";
        int         isOk        = 0;
//        int         v_seq       = 0;

        String v_token_cmuno  = box.getString("p_token_cmuno");

        Vector v_cbx_cmuno = new Vector();
        String v_close_fg  = box.getString("p_close_fg");
        
        
        //String v_content  = box.getString("p_content");
        
        
        
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

        // v_close_fg 0.��û 1.���� 2.��� 3.�ź�
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           StringTokenizer stok    = new StringTokenizer(v_token_cmuno, "/");
           String[]        sTokens = new String[stok.countTokens()];
           for ( int i = 0; stok.hasMoreElements();i++ ) { 
               sTokens[i] = ((String)stok.nextElement() ).trim();
               v_cbx_cmuno.addElement(sTokens[i]);
           }

           for ( int i = 0;i<v_cbx_cmuno.size();i++ ) { 
               String v_tmp_cmuno = (String)v_cbx_cmuno.elementAt(i) ;
               if ( "1".equals(v_close_fg)) { 
            	   sql  =" update tz_cmubasemst set  close_fg           =?   "
                       + "                         , accept_dte         = to_char(sysdate,'YYYYMMDDHH24MISS') "
                       + "                         , accept_userid      =?   "
                       + "                where cmuno = ?"
                       ;
                  pstmt = connMgr.prepareStatement(sql);
                  pstmt.setString(1, v_close_fg      );// ����
                  pstmt.setString(2, s_userid        );// ó����
                  pstmt.setString(3, v_tmp_cmuno     );// Ŀ�´�Ƽ��ȣ
               } else { 
            	   sql  =" update tz_cmubasemst set  close_fg           =?   "
                       + "                         , close_dte          =to_char(sysdate,'YYYYMMDDHH24MISS') "
                       + "                         , close_userid       =?   "
                       + "                where cmuno = ?"
                       ;
                  pstmt = connMgr.prepareStatement(sql);
                  pstmt.setString(1, v_close_fg      );// ����
                  pstmt.setString(2, s_userid        );// ó����
                  pstmt.setString(3, v_tmp_cmuno     );// Ŀ�´�Ƽ��ȣ
               }
               isOk = pstmt.executeUpdate();
               
               if ( pstmt != null ) { pstmt.close(); }
               
//               if ( "1".equals(v_close_fg) )
//                   MileageManager.insertMileage("00000000000000000011", getRegisterUserId(v_tmp_cmuno));
//               else if ( "3".equals(v_close_fg) )
//                   MileageManager.insertMileage("00000000000000000012", getRegisterUserId(v_tmp_cmuno));
           } 

           if ( isOk > 0 ) { 
               if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
           }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql - > " +FormatDate.getDate("yyyyMMdd") + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }




    /**
    * �α�Ŀ�´�Ƽ ����
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateHold(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";
        int         isOk        = 0;
//        int         v_seq       = 0;

        Vector v_cmuno    = box.getVector("p_cmuno");
        Vector v_hold_fg  = box.getVector("p_hold_fg");         
//        String s_userid       = box.getSession("userid");
//        String s_name         = box.getSession("name");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 
           for ( int i = 0;i<v_cmuno.size();i++ ) { 
               String v_tmp_cmuno = (String)v_cmuno.elementAt(i) ;
               String v_tmp_hold  = (String)v_hold_fg.elementAt(i) ;


               sql  =" update tz_cmubasemst set  hold_fg           =?   "
                    + "                where cmuno = ?"
                    ;
               pstmt = connMgr.prepareStatement(sql);
               pstmt.setString(1, v_tmp_hold      );// �αⱸ��
               pstmt.setString(2, v_tmp_cmuno     );// Ŀ�´�Ƽ��ȣ
               isOk = pstmt.executeUpdate();
               
               if ( pstmt != null ) { pstmt.close(); }
           } 

            if ( isOk > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            }

        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql - > " +FormatDate.getDate("yyyyMMdd") + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }



    /**
    * �Ϲݸ�������
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int sendMail(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";
        String      sql1        = "";
        int         isOk        = 1;
//        int         v_seq       = 0;

        String v_token_cmuno  = box.getString("p_token_cmuno");
//        String v_cmuno        = box.getString("p_close_fg");
        Vector v_cbx_cmuno    = new Vector();
        String v_title        = box.getString("p_title");
        String v_intro     = StringManager.replace(box.getString("p_content"),"<br > ","\n");
        
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 
           StringTokenizer stok    = new StringTokenizer(v_token_cmuno, "/");
           String[]        sTokens = new String[stok.countTokens()];
           for ( int i = 0; stok.hasMoreElements();i++ ) { 
               sTokens[i] = ((String)stok.nextElement() ).trim();
               v_cbx_cmuno.addElement(sTokens[i]);
           }

           // �߽��� �̸���
           String v_tmp_send_email= "";
           sql1 = "select email   from tz_member where userid = '" +s_userid + "' ";
           ls = connMgr.executeQuery(sql1);
           while ( ls.next() ) v_tmp_send_email = ls.getString(1);

           for ( int i = 0;i<v_cbx_cmuno.size();i++ ) { 
               String v_tmp_cmuno = (String)v_cbx_cmuno.elementAt(i) ;

               // Ŀ�´�Ƽ���ϱ�
               String v_tmp_cmu_nm= "";
               sql1 = "select cmu_nm   from tz_cmubasemst where cmuno = '" +v_tmp_cmuno + "' ";
               ls = connMgr.executeQuery(sql1);
               while ( ls.next() ) v_tmp_cmu_nm = ls.getString(1);



               // �Ϸù�ȣ ���ϱ�
               int v_mailno=0;
               sql1 = "select nvl(max(MAILNO), 0)   from TZ_CMUMAIL ";
               ls = connMgr.executeQuery(sql1);
               while ( ls.next() ) v_mailno = ls.getInt(1);


               sql  =" insert into TZ_CMUMAIL ( mailno, userid, kor_nm, recv_email"
                    + "                       ,cmuno, cmu_nm, SEND_USERID,send_email, title, content"
                    + "                       ,loc_fg,loc_nm,regster_dte, send_fg)"
                    + "               values  (?,?,?,?"
                    + "                       ,?,?,?,?,?,?"
                    + "                       ,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'),'N')"
                    ;
               pstmt = connMgr.prepareStatement(sql);


               sql1 = "select a.cmuno,b.cmu_nm,a.userid,a.kor_name,a.email   from tz_cmuusermst a,tz_cmubasemst b where a.cmuno = b.cmuno and a.cmuno = '" +v_tmp_cmuno + "' ";
               ls = connMgr.executeQuery(sql1);
               while ( ls.next() ) { 
                     v_mailno =v_mailno +1;
                     pstmt.setInt   (1, v_mailno                                );// �Ϸù�ȣ
                     pstmt.setString(2, ls.getString(3)                         );// �����ھ��̵�
                     pstmt.setString(3, ls.getString(4)                         );// �����ڸ�
                     pstmt.setString(4, ls.getString(5)                         );// �������̸���
                     pstmt.setString(5, v_tmp_cmuno                             );// Ŀ�´�Ƽ��ȣ
                     pstmt.setString(6, v_tmp_cmu_nm                            );// Ŀ�´�Ƽ��
                     pstmt.setString(7 ,s_userid                                );// �߽��ھ��̵�
                     pstmt.setString(8 ,v_tmp_send_email                        );// �߽����̸���
                     pstmt.setString(9 , v_title                                );// ����
                     pstmt.setString(10, v_intro                                );// ����
                     pstmt.setString(11, "5"                                    );// ����
                     pstmt.setString(12, "Ŀ�´�Ƽ  ���ΰ���"                     );// ���и�
                     isOk = pstmt.executeUpdate();
                     
                     if ( pstmt != null ) { pstmt.close(); }
//                     sql1 = "select content  from TZ_CMUMAIL where mailno = '" +v_mailno + "'";
//                     connMgr.setOracleCLOB(sql1, v_intro);
                     if ( isOk > 0 ) { 
                         if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
                     }
               }
           }// end for

        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql - > " +FormatDate.getDate("yyyyMMdd") + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }
    

    /**
    * Ŀ�´�Ƽ ������ ��������
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public String getRegisterUserId(String p_cmuno) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;
        String              sql         = "";

        String              v_reguserid = "";

        try { 
           connMgr  = new DBConnectionManager();

           sql      = "select register_userid from tz_cmubasemst where cmuno = " + SQLString.Format(p_cmuno) + " ";

           ls       = connMgr.executeQuery(sql);
           
           while ( ls.next() ) 
               v_reguserid  = ls.getString("register_userid");
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql - > " +FormatDate.getDate("yyyyMMdd") + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return v_reguserid;
    }
}