// **********************************************************
// 1. ��      ��:
// 2. ���α׷���: CommunityMsPrBean.java
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
public class CommunityMsPrBean { 
//    private ConfigSet config;
//    private static int row=10;
//    private String v_type = "PQ";
//    private static final String FILE_TYPE = "p_file";           //      ���Ͼ��ε�Ǵ� tag name
//    private static final int FILE_LIMIT = 1;                    //    �������� ���õ� ����÷�� ����


    public CommunityMsPrBean() { 
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
    * Ŀ�´�Ƽ ȫ��������ȸ
    * @param box          receive from the form object and session
    * @return ArrayList   Ŀ�´�Ƽ ȫ������
    * @throws Exception
    */
    public ArrayList selectQuery(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
//        String              sql2    = "";
        DataBox             dbox    = null;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");

//        String s_userid        = box.getSession("userid");
//        String s_name          = box.getSession("name");

        try { 
            connMgr = new DBConnectionManager();

            sql  = "\n select cmuno, realfile, savepath, savefile, filesize"
                  + "\n       , contents , register_userid, register_dte"
                  + "\n       , modifier_userid, modifier_dte"
                  + "\n           from tz_cmuhongbo "
                  + "\n           where cmuno        = '" +v_cmuno     + "'" ;
                 
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
    * ȫ������ ����ϱ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertHongbo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";

        int         isOk1       = 1;
        int         isOk2       = 1;
//        int         v_seq       = 0;
//        int         v_menuno    = 0; 
//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
        String v_content   = StringManager.replace(box.getString("content"),"<br > ","\n");

        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

         
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 
           int v_reccnt=0;
            sql  = "\n select count(*) cnt"
                  + "\n           from tz_cmuhongbo "
                  + "\n           where cmuno        = '" +v_cmuno     + "'" ;
                 
            ls = connMgr.executeQuery(sql);
            while ( ls.next() )  v_reccnt= ls.getInt("cnt");

           if ( v_reccnt<1) { 
              sql  =" insert into tz_cmuhongbo ( cmuno, realfile, savepath, savefile, filesize, contents"
                   + "                         , register_userid, register_dte      "
                   + "                         , modifier_userid, modifier_dte     )"
                   + "                 values  (?,?,'',?,0,?"
                   + "                         ,?,to_char(sysdate,'YYYYMMDDHH24MISS')"
                   + "                         ,?,to_char(sysdate,'YYYYMMDDHH24MISS'))"
                   ;
              pstmt = connMgr.prepareStatement(sql);

              pstmt.setString(1 , v_cmuno                      );// Ŀ�´�����ȣ
              pstmt.setString (2 , box.getRealFileName("p_file" ));// ��������
              pstmt.setString(3 , box.getNewFileName("p_file"  ));// ��������
              pstmt.setString(4, v_content);
              pstmt.setString(5, s_userid);// �Խ���
              pstmt.setString(6, s_userid );// ������
              isOk1 = pstmt.executeUpdate();
              if ( pstmt != null ) { pstmt.close(); }
//              String sql1 = "select contents from tz_cmuhongbo where cmuno = '" +v_cmuno + "'";
//              connMgr.setOracleCLOB(sql1, v_content);
           } else { 
              if ( box.getString("p_savefile").length() <1) { 
                sql  =" update tz_cmuhongbo set realfile=?,savefile=?,contents=?"
                     + "                         , modifier_userid=?, modifier_dte=to_char(sysdate,'YYYYMMDDHH24MISS')     "
                    + "\n           where cmuno        = '" +v_cmuno     + "'" ;  
                pstmt = connMgr.prepareStatement(sql);

                pstmt.setString   (1 , box.getRealFileName("p_file" ));// ��������
                pstmt.setString(2 , box.getNewFileName("p_file"  ));// ��������
                pstmt.setString(3, v_content);
                pstmt.setString(4, s_userid );// ������
                isOk1 = pstmt.executeUpdate();
              } else { 
                sql  =" update tz_cmuhongbo set contents=?"
                     + "                         , modifier_userid=?, modifier_dte=to_char(sysdate,'YYYYMMDDHH24MISS')     "
                    + "\n           where cmuno        = '" +v_cmuno     + "'" ;  
                pstmt = connMgr.prepareStatement(sql);

                pstmt.setString(1, v_content );
                pstmt.setString(2, s_userid );// ������
                isOk1 = pstmt.executeUpdate();
                if ( pstmt != null ) { pstmt.close(); }
              }
//              String sql1 = "select contents from tz_cmuhongbo where cmuno = '" +v_cmuno + "'";
//              connMgr.setOracleCLOB(sql1, v_content);

           }
           if ( isOk1 > 0 ) { 
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
        return isOk1*isOk2;
    }


 /**
  * ȫ����������
  * @param connMgr   DB Connection Manager
  * @param box    receive from the form object and session
  * @return
  * @throws Exception
  */
 public int deleteHongbo( RequestBox box) throws Exception { 
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

       sql  =" delete from tz_cmuhongbo' "
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

       sql  =" update tz_cmuhongbo set realfile='',savefile='' "
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
