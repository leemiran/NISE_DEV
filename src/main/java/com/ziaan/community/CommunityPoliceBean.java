// **********************************************************
// 1. ��      ��:
// 2. ���α׷���: CommunityPoliceBean.java
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
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class CommunityPoliceBean { 
//    private ConfigSet config;
//    private int row;

    public CommunityPoliceBean() { 
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
    * �Ұ��� Ŀ�´�Ƽ �Ű�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertPolice(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";
//        String      sql1        = "";

        int         isOk        = 0;
        int         v_policeno       = 0;

        String v_cmuno     = box.getString("p_cmuno");
        String v_cmu_nm    = box.getString("p_cmu_nm");
        String v_email     = box.getString("p_email");
        String v_intro     = StringManager.replace(box.getString("content"),"<br > ","\n");
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");


        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           sql  = "select nvl(max(policeno),0) from tz_cmupolice";
           
           ls = connMgr.executeQuery(sql);
           while ( ls.next() ) { 
               v_policeno = ls.getInt(1) + 1;
           } 

           sql  =" insert into tz_cmupolice (policeno, cmuno, cmu_nm, userid, email, content, singo_dte, str_fg)"
             + "                   values  (?,?,?,?,?"
             + "                           ,?,to_char(sysdate,'YYYYMMDDHH24MISS'),'1')"
             ;
         
           pstmt = connMgr.prepareStatement(sql);


           pstmt.setInt   (1, v_policeno                       );// Ŀ�´�Ƽ��ȣ
           pstmt.setString(2, v_cmuno     );// Ŀ�´�Ƽ��ȣ
           pstmt.setString(3, v_cmu_nm    );// Ŀ�´�Ƽ��
           pstmt.setString(4, s_userid    );// �Ű���
           pstmt.setString(5, v_email     );// �Ű����̸���
           pstmt.setString(6, v_intro);
           isOk = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }
//           sql1 = "select content from tz_cmupolice where policeno = '" + v_policeno + "'";
//           connMgr.setOracleCLOB(sql1, v_intro);       //      (��Ÿ ���� ���)

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
                + "                         , intro              =? "
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
           pstmt.setString(8, v_intro);
           pstmt.setString(9, box.getNewFileName("p_img_path")  );// �̹���
           pstmt.setString(10, box.getString("p_layout_fg")     );// ���̾ƿ�
           pstmt.setString(11, box.getString("p_html_skin_fg")  );// ȭ�齺Ų
           pstmt.setString(12, s_userid                         );// ������
           pstmt.setString(13, v_cmuno                          );// Ŀ�´�Ƽ��ȣ
           isOk = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }
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
  
            sql  =  " select cmuno, cmu_nm, in_method_fg, search_fg, data_passwd_fg, display_fg, type_l "
                  + "       , type_m, intro, img_path, layout_fg, html_skin_fg, read_cnt, member_cnt, close_fg"
                  + "       , close_reason, close_dte, close_userid, hold_fg, accept_dte, accept_userid, register_dte "
                  + "       , register_userid, modifier_dte, modifier_userid "
                  + "   from tz_cmubasemst"
                  + "  where cmuno        = '" +v_cmuno + "'"
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
       if ( pstmt != null ) { pstmt.close(); }
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

}
