// **********************************************************
// 1. ��      ��:
// 2. ���α׷���: CommunityMsCommunityCloseBean.java
// 3. ��      ��:
// 4. ȯ      ��: JDK 1.3
// 5. ��      ��: 0.1
// 6. ��      ��: Administrator 2003-08-29
// 7. ��      ��:
// 
// **********************************************************

package com.ziaan.community;

import java.sql.PreparedStatement;

import com.ziaan.library.DBConnectionManager;
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
public class CommunityMsCommunityCloseBean { 
//    private ConfigSet config;
//    private static int row=10;
//    private String v_type = "PQ";
//    private static final String FILE_TYPE = "p_file";           //      ���Ͼ��ε�Ǵ� tag name
//    private static final int FILE_LIMIT = 1;                    //    �������� ���õ� ����÷�� ����


    public CommunityMsCommunityCloseBean() { 
        try { 
//            config = new ConfigSet();
//            row = Integer.parseInt(config.getProperty("page.bulletin.row") );  // �� ����� �������� row ���� �����Ѵ�
//            row = 10; // ������ ����
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }


    /**
    * Ŀ�´�Ƽ ���
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateCommunityClose(RequestBox box) throws Exception { 
    	DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet             ls1      = null;
        ListSet     ls          = null;
        String      sql         = "";
        String      sql1        = "";
        int         isOk        = 1;
        int         v_seq       = 0;

        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        String v_title         = box.getString("p_title");
        String v_intro     = StringManager.replace(box.getString("content"),"<br > ","\n");

        String s_userid    = box.getSession("userid");
        String s_name      = box.getSession("name");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 


           sql  =" update tz_cmubasemst set  close_fg           = ?   "
                + "                         , close_reason      = ?   "
//                + "                         , close_reason    = empty_clob()   "
                + "                         , close_dte         = to_char(sysdate, 'YYYYMMDDHH24MISS')   "
                + "                         , close_userid      = ?   "
                + "                where cmuno = ?"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1, "2"       );// Ŀ�´�Ƽ��
           pstmt.setString(2, v_intro       );// Ŀ�´�Ƽ��
           pstmt.setString(3, s_userid  );// ������
           pstmt.setString(4, v_cmuno   );
           isOk = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }

           // Ŀ�´�Ƽ ���� ���� 
           sql1 = "select close_reason from tz_cmubasemst where cmuno = '" + v_cmuno + "'";
//           connMgr.setOracleCLOB(sql1, v_intro);  


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
//                + "                       ,?,?,?,?,?,empty_clob()"
                + "                       ,?,?,to_char(sysdate, 'YYYYMMDDHH24MISS'),'E')"
                ;
           pstmt = connMgr.prepareStatement(sql);
           sql1 = "select userid,kor_name,email  from tz_cmuusermst where cmuno='" +v_cmuno + "'";
           ls = connMgr.executeQuery(sql1);
           
           while ( ls.next() ) { 

                // Ŀ�´�Ƽ���ϱ�
                String v_tmp_cmu_nm= "";
                sql1 = "select cmu_nm   from tz_cmubasemst where cmuno = '" +v_cmuno + "' ";
                System.out.println(sql1);
                ls1 = connMgr.executeQuery(sql1);
                while ( ls1.next() ) v_tmp_cmu_nm = ls1.getString(1);

                 // �߽��� �̸���
                 String v_tmp_send_email= "";
                 sql1 = "select email   from tz_member where userid = '" +s_userid + "' ";
                 ls1 = connMgr.executeQuery(sql1);
                 while ( ls1.next() ) v_tmp_send_email = ls1.getString(1);

                 v_mailno =v_mailno +1;
                 pstmt.setInt   (1, v_mailno                                );// �Ϸù�ȣ
                 pstmt.setString(2, ls.getString(1)                         );// �����ھ��̵�
                 pstmt.setString(3, ls.getString(2)                         );// �����ڸ�
                 pstmt.setString(4, ls.getString(3)                         );// �������̸���
                 pstmt.setString(5, v_cmuno                                 );// Ŀ�´�Ƽ��ȣ
                 pstmt.setString(6, v_tmp_cmu_nm                            );// Ŀ�´�Ƽ��
                 pstmt.setString(7 ,s_userid                                );// �߽��ھ��̵�
                 pstmt.setString(8 ,v_tmp_send_email                        );// �߽����̸���
                 pstmt.setString(9 , v_title                                );// ����
                 pstmt.setString(10 , v_intro                                );// ����
                 pstmt.setString(11, "3"                                    );// ����
                 pstmt.setString(12, "Ŀ�´�Ƽ ���"                       );// ���и�

                 isOk = pstmt.executeUpdate();
                 if ( pstmt != null ) { pstmt.close(); }
                 sql1 = "select content  from TZ_CMUMAIL where mailno = '" +v_mailno + "'";
//                 connMgr.setOracleCLOB(sql1, v_intro);
                 if ( isOk > 0 ) { 
                     if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
                 }
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
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }
}