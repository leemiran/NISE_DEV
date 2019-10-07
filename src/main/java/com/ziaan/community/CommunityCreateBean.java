// **********************************************************
// 1. ��      ��:
// 2. ���α׷���: CommunityCreateBean.java
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
public class CommunityCreateBean { 
//    private ConfigSet config;
//    private int row;

    public CommunityCreateBean() { 
        try { 
//            config = new ConfigSet();
//            row = Integer.parseInt(config.getProperty("page.manage.row") );  // �� ����� �������� row ���� �����Ѵ�
//            row = 10; // ������ ����
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
 
 /**
 Ŀ�´�Ƽ �з� ��ȸ
 @param gubun        �ڵ屸��
 @return ArrayList   Ŀ�´�Ƽ �з�����Ʈ
 */     
    public ArrayList selectCodeType_L(String gubun) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList list = new ArrayList();
        ListSet   ls   = null;
        String    sql  = "";
        DataBox             dbox    = null;
        try { 
            connMgr = new DBConnectionManager();
            sql = " select gubun, levels, code, codenm, upper, parent, luserid, ldate"
                 + "   from tz_code "
                 + "  where gubun = " + SQLString.Format(gubun)
                 ;
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                   dbox = ls.getDataBox();
                   list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }

    /**
    * Ŀ�´�Ƽ Į������
    * @param box          receive from the form object and session
    * @return ArrayList   Ŀ�´�Ƽ �޴�����
    * @throws Exception
    */
    public String getSingleColumn(String v_cmuno,String v_column) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
//        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        DataBox             dbox    = null;

        String              v_ret   = "";
        try { 
            connMgr = new DBConnectionManager();

            sql  = "\n select " +v_column
                  + "\n   from tz_cmubasemst"
                  + "\n  where cmuno        = '" +v_cmuno     + "'" ;
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_ret = ls.getString(1);
            }
        }
        catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return v_ret;
    }

 /**
 Ŀ�´�Ƽ�� �ߺ�üũ ��ȸ
 @param box     receive from the form object and session
 @return int    row count
 */     
    public int selectCmuNmRowCnt(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
//        ArrayList list = new ArrayList();
        ListSet   ls   = null;
        String    sql  = "";
        int       iRowCnt=0;
        try { 
            connMgr = new DBConnectionManager();
            sql = " select count(*) rowcnt"
                 + "   from tz_cmubasemst "
                 + "  where cmu_nm = " + SQLString.Format(box.getStringDefault("p_cmu_nm",""));
            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                  iRowCnt = ls.getInt("rowcnt");
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return iRowCnt;
    }

    /**
    * Ŀ�´�Ƽ �����
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertBaseMst(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";
        String      sql1        = "";
        String      createCmuno = "";
        int         isOk        = 0;
//        int         v_seq       = 0;

//        String v_adtitle   = box.getString("p_adtitle");
        String v_intro     = StringManager.replace(box.getString("content"),"<br > ","\n");
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

        String v_grcode = "";
        String thisYear= "";
//        String v_templetfile = "";

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 
           thisYear = FormatDate.getDate("yyyyMMdd").substring(0,4);
           createCmuno = thisYear + "000001";
           sql  = "select nvl(max(cmuno),'" +thisYear + "000000') from tz_cmubasemst where cmuno like  '" +thisYear + "%'";
           ls = connMgr.executeQuery(sql);
           while ( ls.next() ) { 
               createCmuno = String.valueOf( ls.getInt(1) + 1);
           } 

           sql  =" insert into tz_cmubasemst ( cmuno     , cmu_nm         , in_method_fg, search_fg   , data_passwd_fg"
             + "                           , display_fg  , type_l         , type_m      , intro       , img_path"
             + "                           , layout_fg   , html_skin_fg   , read_cnt    , member_cnt  , close_fg"
             + "                           , register_dte, register_userid"
             + "                           , modifier_dte, modifier_userid)"
             + "                   values  (?,?,?,?,?"
             + "                           ,?,?,?,?,?"
             + "                           ,?,?,0,1,'0'"
             + "                           ,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),?"
             + "                           ,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),?)"
             ;
           pstmt = connMgr.prepareStatement(sql);
           
           pstmt.setString(1, createCmuno                       );// Ŀ�´�Ƽ��ȣ
           pstmt.setString(2, box.getString("p_cmu_nm")          );// Ŀ�´�Ƽ��
           pstmt.setString(3, box.getString("p_in_method_fg")   );// ȸ�����Թ��
           pstmt.setString(4, box.getString("p_search_fg")      );// �ڷ�ǰ˻���뤷
           pstmt.setString(5, box.getStringDefault("p_data_passwd_fg","N") );// �ڷ��ȣȭ����
           pstmt.setString(6, box.getString("p_display_fg")     );// Ŀ�´�Ƽ��������
           pstmt.setString(7, box.getString("p_type_l")         );// ��з�
           pstmt.setString(8, box.getString("p_type_m")         );// �ߺз�
           pstmt.setString(9, v_intro         );
           pstmt.setString(10, box.getNewFileName("p_img_path")  );// �̹���
           pstmt.setString(11, box.getString("p_layout_fg")     );// ���̾ƿ�
           pstmt.setString(12, box.getString("p_html_skin_fg")  );// ȭ�齺Ų
           pstmt.setString(13, s_userid                         );// �����
           pstmt.setString(14, s_userid                         );// ������
           isOk = pstmt.executeUpdate();
           
//           if ( pstmt != null ) { pstmt.close(); }

//           sql1 = "select intro from tz_cmubasemst where cmuno = '" + createCmuno + "'";
//           connMgr.setOracleCLOB(sql1, v_intro);       //      (��Ÿ ���� ���)

           // Ŀ�´�Ƽ ����ڵ����
           sql  =" insert into tz_cmugrdcode (  cmuno           , grcode       , kor_nm          ,eng_nm        , descript "
                + "                            ,register_userid , register_dte , modifier_userid , modifier_dte , del_fg)"
                + "                   values  (?,?,?,'',''"
                + "                           ,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'N')";
           
           pstmt = connMgr.prepareStatement(sql);

           sql1  = " select code,codenm from tz_code where gubun ='0053' order by code asc";
           ls = connMgr.executeQuery(sql1);
           
//           if ( pstmt != null ) { pstmt.close(); }
           while ( ls.next() ) { 
                v_grcode=ls.getString("code") ;
                pstmt.setString(1, createCmuno            );
                pstmt.setString(2, ls.getString("code")   );
                pstmt.setString(3, ls.getString("codenm") );
                pstmt.setString(4, s_userid               );
                pstmt.setString(5, s_userid               );
                pstmt.executeUpdate();
           }   
//           if ( pstmt != null ) { pstmt.close(); }
           ls.close();

     // ȸ�����

           sql  =" insert into tz_cmuusermst (cmuno        , userid      , kor_name       , eng_name      , email \n"
                + "                          , tel         , mobile      , duty           , wk_area       , grade \n"
                + "                          , request_dte , license_dte , license_userid , close_fg      , close_reason \n"
                + "                          , close_dte   , intro       , recent_dte     , visit_num     , search_num \n"
                + "                          , register_num, modifier_dte)"
                + "             values (       ?           , ?           , ?          , ''             , ?  \n"
                + "                          , ?           , ?           ,''          , ''             ,'01'  \n"
                + "                          ,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),to_char(sysdate,'YYYYMMDDHH24MISS'),?,'1' ,'' \n"
                + "                          ,''           ,''           ,''          ,0               ,0   \n"
                + "                          ,0            ,to_char(sysdate,'YYYYMMDDHH24MISS'))";

           pstmt = connMgr.prepareStatement(sql);
           //sql1  = " select userid,name,email,hometel,handphone,comptel,work_plcnm from tz_member where userid ='" +s_userid + "'";
           //sql1  = " select userid,name,email,'' hometel,handphone,comptel,'' work_plcnm from tz_member where userid ='" +s_userid + "'";
           sql1  = " select userid, name, email, hometel, handphone from tz_member where userid = " + SQLString.Format(s_userid);
           ls = connMgr.executeQuery(sql1);
           
//           if ( pstmt != null ) { pstmt.close(); }
           
           while ( ls.next() ) { 
                pstmt.setString(1, createCmuno );
                pstmt.setString(2, ls.getString("userid")   );
                pstmt.setString(3, ls.getString("name") );
                pstmt.setString(4, ls.getString("email") );
                pstmt.setString(5, ls.getString("hometel") );
                pstmt.setString(6, ls.getString("handphone") );
                pstmt.setString(7, s_userid );
                //pstmt.setString(7, ls.getString("comptel") );
                //pstmt.setString(8, ls.getString("work_plcnm") );
                pstmt.executeUpdate();
           }   
           ls.close();

           // �������� �Խ��Ǹ޴�����
           sql  =" insert into tz_cmumenu ( cmuno          ,menuno         ,title          ,read_cd        "
                + "                         ,write_cd       ,arrange        ,fileadd_fg     ,filecnt        "
                + "                         ,directory_fg   ,brd_fg         ,root           ,parent         " //directory_memo ,
                + "                         ,lv             ,position       ,register_userid,register_dte   " //limit_list     ,
                + "                         ,modifier_userid,modifier_dte   ,del_fg                         )"
                + "                 values  (?,?,?,?"
                + "                         ,?,?,?,?"
                + "                         ,?,?,?,?"//empty_clob(),
                + "                         ,?,?,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')" //empty_clob(),
                + "                         ,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'N')"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1 , createCmuno      );// Ŀ�´�����ȣ
           pstmt.setInt   (2 , 1            );// �޴��Ϸù�ȣ
           pstmt.setString(3 , "��������"     );// ����
           pstmt.setString(4 , v_grcode     );// �б����
           pstmt.setString(5 , "02"          );// ������� (grcode ������ �νü������� �ۼ������ϰ� �ٲ�)
           pstmt.setString(6 , "register_dte"      );// ����
           pstmt.setString(7 , "Y"          );// ����÷�α���
           pstmt.setInt   (8 , 5            );// ÷�����ϰ���
           pstmt.setString(9 , "file"       );// ���丮����
           pstmt.setString(10,"0"           );// �ڷ�Ǳ���
           pstmt.setInt   (11, 1            );// Root
           pstmt.setInt   (12, 1            );// Parent
           pstmt.setInt   (13, 1            );// 
           pstmt.setInt   (14, 1            );// 
           pstmt.setString(15, s_userid     );// �Խ���
           pstmt.setString(16, s_userid     );// ������
           pstmt.executeUpdate();
//           if ( pstmt != null ) { pstmt.close(); }

           // �ٹ��Խ��ǻ���
           sql  =" insert into tz_cmumenu (  cmuno          ,menuno         ,title          ,read_cd        "
                + "                         ,write_cd       ,arrange        ,fileadd_fg     ,filecnt        "
                + "                         ,directory_fg   ,brd_fg         ,root           ,parent         " //directory_memo ,
                + "                         ,lv             ,position       ,register_userid,register_dte   " //limit_list     ,
                + "                         ,modifier_userid,modifier_dte   ,del_fg                         )"
                + "                 values  (?,?,?,?"
                + "                         ,?,?,?,?"
                + "                         ,?,?,?,?" //empty_clob(),
                + "                         ,?,?,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')" //empty_clob(),
                + "                         ,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'N')"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1 , createCmuno      );// Ŀ�´�����ȣ
           pstmt.setInt   (2 , 2            );// �޴��Ϸù�ȣ
           pstmt.setString(3 , "�ٹ�"     );// ����
           pstmt.setString(4 , v_grcode     );// �б����
           pstmt.setString(5 , v_grcode     );// �������
           pstmt.setString(6 , "register_dte"      );// ����
           pstmt.setString(7 , "Y"          );// ����÷�α���
           pstmt.setInt   (8 , 1            );// ÷�����ϰ���
           pstmt.setString(9 , "file"       );// ���丮����
           pstmt.setString(10,"3"           );// �ڷ�Ǳ���
           pstmt.setInt   (11, 2            );// Root
           pstmt.setInt   (12, 2            );// Parent
           pstmt.setInt   (13, 1            );// 
           pstmt.setInt   (14, 2            );// 
           pstmt.setString(15, s_userid     );// �Խ���
           pstmt.setString(16, s_userid     );// ������
           pstmt.executeUpdate();
//           if ( pstmt != null ) { pstmt.close(); }
           // �Խ��ǻ���
           sql  =" insert into tz_cmumenu (  cmuno          ,menuno         ,title          ,read_cd        "
                + "                         ,write_cd       ,arrange        ,fileadd_fg     ,filecnt        "
                + "                         ,directory_fg   ,brd_fg         ,root           ,parent         " //directory_memo ,
                + "                         ,lv             ,position       ,register_userid,register_dte   " //limit_list     ,
                + "                         ,modifier_userid,modifier_dte   ,del_fg                         )"
                + "                 values  (?,?,?,?"
                + "                         ,?,?,?,?"
                + "                         ,?,?,?,?" //empty_clob(),
                + "                         ,?,?,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')" //empty_clob(),
                + "                         ,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'N')"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1 , createCmuno      );// Ŀ�´�����ȣ
           pstmt.setInt   (2 , 3            );// �޴��Ϸù�ȣ
           pstmt.setString(3 , "�Խ���"     );// ����
           pstmt.setString(4 , v_grcode     );// �б����
           pstmt.setString(5 , v_grcode     );// �������
           pstmt.setString(6 , "register_dte"      );// ����
           pstmt.setString(7 , "Y"          );// ����÷�α���
           pstmt.setInt   (8 , 1            );// ÷�����ϰ���
           pstmt.setString(9 , "file"       );// ���丮����
           pstmt.setString(10,"2"           );// �ڷ�Ǳ���
           pstmt.setInt   (11, 3            );// Root
           pstmt.setInt   (12, 3            );// Parent
           pstmt.setInt   (13, 1            );// 
           pstmt.setInt   (14, 1            );// 
           pstmt.setString(15, s_userid     );// �Խ���
           pstmt.setString(16, s_userid     );// ������
           pstmt.executeUpdate();           
//           if ( pstmt != null ) { pstmt.close(); }
           // �ڷ�ǻ���
           sql  =" insert into tz_cmumenu (  cmuno          ,menuno         ,title          ,read_cd        "
                + "                         ,write_cd       ,arrange        ,fileadd_fg     ,filecnt        "
                + "                         ,directory_fg   ,brd_fg         ,root           ,parent         " //directory_memo ,
                + "                         ,lv             ,position       ,register_userid,register_dte   " //limit_list     ,
                + "                         ,modifier_userid,modifier_dte   ,del_fg                         )"
                + "                 values  (?,?,?,?"
                + "                         ,?,?,?,?"
                + "                         ,?,?,?,?" //empty_clob(),
                + "                         ,?,?,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')" //empty_clob(),
                + "                         ,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'N')"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1 , createCmuno      );// Ŀ�´�����ȣ
           pstmt.setInt   (2 , 4            );// �޴��Ϸù�ȣ
           pstmt.setString(3 , "�ڷ��"     );// ����
           pstmt.setString(4 , v_grcode     );// �б����
           pstmt.setString(5 , v_grcode     );// �������
           pstmt.setString(6 , "register_dte"      );// ����
           pstmt.setString(7 , "Y"          );// ����÷�α���
           pstmt.setInt   (8 , 1            );// ÷�����ϰ���
           pstmt.setString(9 , "file"       );// ���丮����
           pstmt.setString(10,"1"           );// �ڷ�Ǳ���
           pstmt.setInt   (11, 4            );// Root
           pstmt.setInt   (12, 4            );// Parent
           pstmt.setInt   (13, 1            );// 
           pstmt.setInt   (14, 1            );// 
           pstmt.setString(15, s_userid     );// �Խ���
           pstmt.setString(16, s_userid     );// ������
           pstmt.executeUpdate();                  
//           if ( pstmt != null ) { pstmt.close(); }
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
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }

    /**
    * Ŀ�´�Ƽ ��������
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateBaseMst(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";
//        String      sql1        = "";
        int         isOk        = 0;
//        int         v_seq       = 0;

        String v_cmuno     = box.getString("p_cmuno");
//        String v_adtitle   = box.getString("p_adtitle");
        String v_intro     = StringManager.replace(box.getString("content"),"<br > ","\n");

        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

//        String thisYear= "";
//        String v_templetfile = "";

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 


           sql  =" update tz_cmubasemst set  cmu_nm             =?   "
                + "                         , in_method_fg       =?   "
                + "                         , search_fg          =?   "
                + "                         , data_passwd_fg     =?   "
                + "                         , display_fg         =?   "
                + "                         , type_l             =?   "  
                + "                         , type_m             =?   "
                + "                         , intro              =?   "
                + "                         , img_path           =?   "
                + "                         , layout_fg          =?   "
                + "                         , html_skin_fg       =?   "
                + "                         , modifier_dte       =to_char(sysdate,'YYYYMMDDHH24MISS')"
                + "                         , modifier_userid    =?   "
                + "                where cmuno = ?"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1, box.getString("p_cmu_nm")          );// Ŀ�´�Ƽ��
           pstmt.setString(2, box.getString("p_in_method_fg")   );// ȸ�����Թ��
           pstmt.setString(3, box.getString("p_search_fg")      );// �ڷ�ǰ˻���뤷
           pstmt.setString(4, box.getStringDefault("p_data_passwd_fg","N") );// �ڷ��ȣȭ����
           pstmt.setString(5, box.getString("p_display_fg")     );// Ŀ�´�Ƽ��������
           pstmt.setString(6, box.getString("p_type_l")         );// ��з�
           pstmt.setString(7, box.getString("p_type_m")         );// �ߺз�
           pstmt.setString(8, v_intro         );
           pstmt.setString(9, box.getNewFileName("p_img_path")  );// �̹���
           pstmt.setString(10, box.getString("p_layout_fg")     );// ���̾ƿ�
           pstmt.setString(11, box.getString("p_html_skin_fg")  );// ȭ�齺Ų
           pstmt.setString(12, s_userid                         );// ������
           pstmt.setString(13, v_cmuno                          );// Ŀ�´�Ƽ��ȣ
           isOk = pstmt.executeUpdate();
//           if ( pstmt != null ) { pstmt.close(); }
           // Ŀ�´�Ƽ ���� ���� 
//           sql1 = "select intro from tz_cmubasemst where cmuno = '" + v_cmuno + "'";
//           connMgr.setOracleCLOB(sql1, v_intro);  

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
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }

    /**
    * �⺻���� ��ȸ
    * @param box          receive from the form object and session
    * @return ArrayList   �⺻���� ������
    * @throws Exception
    */
    public ArrayList selectBaseMst(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
//        PreparedStatement   pstmt = null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();

        String              sql     = "";
//        String              sql1    = "";
        DataBox             dbox    = null;
//        int                 isOK    =1;

//        String  v_static_cmuno = box.getString("p_static_cmuno"); 
        String  v_cmuno = box.getString("p_cmuno"); 
//        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");

 

 
        try { 
            connMgr = new DBConnectionManager();
  
            sql  =  " select a.cmuno cmuno, a.cmu_nm cmu_nm, a.in_method_fg in_method_fg, a.search_fg search_fg, a.data_passwd_fg data_passwd_fg, a.display_fg display_fg, a.type_l type_l "
                  + "       , a.type_m, a.intro intro, a.img_path img_path, a.layout_fg layout_fg, a.html_skin_fg html_skin_fg, a.read_cnt read_cnt, a.member_cnt member_cnt, a.close_fg close_fg"
                  + "       , a.close_reason close_reason, a.close_dte close_dte, a.close_userid close_userid, a.hold_fg hold_fg, a.accept_dte accept_dte, a.accept_userid accept_userid, a.register_dte register_dte"
                  + "       , a.register_userid  register_userid, a.modifier_dte modifier_dte, a.modifier_userid modifier_userid"
                  + "       , nvl(d.savefile,'')        hongbo_savefile , nvl(d.contents,'')      hongbo_contents"
                  + "   from  tz_cmubasemst a"
                  + "        ,tz_cmuhongbo d"
                  + "  where a.cmuno        = '" +v_cmuno + "'"
                  + "    and a.cmuno  = d.cmuno(+) "
                  ;
            ls = connMgr.executeQuery(sql);
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
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }

 /**
  * ���õ� �ڷ����� DB���� ����
  * @param connMgr   DB Connection Manager
  * @param box    receive from the form object and session
  * @return
  * @throws Exception
  */
 public int deleteSingleFile( RequestBox box) throws Exception { 
   DBConnectionManager connMgr     = null;
   PreparedStatement   pstmt   = null;
   ListSet             ls      = null;
   String              sql     = "";
//   String              sql2    = "";
   int                 isOk2   = 1;



//  String s_userid = box.getSession("userid");
  String v_cmuno  = box.getString("p_cmuno");
  try { 
       connMgr = new DBConnectionManager();
       connMgr.setAutoCommit(false); 

       sql  =" update tz_cmubasemst set img_path='' "
           + "  where cmuno        = ?"
       ;
       pstmt = connMgr.prepareStatement(sql);
       pstmt.setString(1, v_cmuno       );// Ŀ�´�Ƽ��ȣ
       isOk2 = pstmt.executeUpdate();
//       if ( pstmt != null ) { pstmt.close(); }
       if ( isOk2 > 0 ) { 
          if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
       }
  }
  catch ( Exception ex ) { 
      ErrorManager.getErrorStackTrace(ex, box, sql);
   throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
  }
  finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
  }
  return isOk2;
 }
 
    /**
    * Ŀ�´�Ƽ ��ȸ�� ����
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateCmuHit(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";
//        String      sql1        = "";
        int         isOk        = 0;
//        int         v_seq       = 0;

        String v_cmuno     = box.getString("p_cmuno");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 


           sql  ="	update tz_cmubasemst  \n" +
           	 "	set total_cnt = nvl(total_cnt,0)+1  \n" +
           	 "	   ,today_cnt = (case when substr(nvl(hit_dte,to_char(sysdate,'YYYYMMDDHH24MISS')),0,8) = to_char(sysdate,'YYYYMMDD') then nvl(today_cnt,0)+1 else 1 end)  \n" +
           	 "	   ,hit_dte = to_char(sysdate,'YYYYMMDDHH24MISS')  \n" +
           	 "	where cmuno = ? ";
           	 
           pstmt = connMgr.prepareStatement(sql);
           pstmt.setString(1, v_cmuno);// Ŀ�´�Ƽ��ȣ                
           isOk = pstmt.executeUpdate();
//           if ( pstmt != null ) { pstmt.close(); }
           // Ŀ�´�Ƽ ���� ���� 
           /*
           sql1 = "select intro from tz_cmubasemst where cmuno = '" + v_cmuno + "'";
           connMgr.setOracleCLOB(sql1, v_intro);  
           */

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
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }
}