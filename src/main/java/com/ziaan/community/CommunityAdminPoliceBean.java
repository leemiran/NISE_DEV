// **********************************************************
// 1. ��      ��:
// 2. ���α׷���: CommunityAdminPoliceBean.java
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

import com.ziaan.library.ConfigSet;
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
public class CommunityAdminPoliceBean { 
    private ConfigSet config;
    private int row;

    public CommunityAdminPoliceBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.manage.row") );  // �� ����� �������� row ���� �����Ѵ�
            row = 10; // ������ ����
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
 
    /**
    * �ҷ� Ŀ�´�Ƽ �亯
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updatePolice(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";
        String      sql1        = "";

        int         isOk        = 0;

        String v_policeno  = box.getString("p_policeno");
        //String v_repmemo   = StringManager.replace(box.getString("content"),"<br > ","\n");
        String v_repmemo   = box.getString("p_content");
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ���ε�)
/*           
           ConfigSet conf = new ConfigSet();
           SmeNamoMime namo = new SmeNamoMime(v_repmemo); // ��ü���� 
           boolean result = namo.parse(); // ���� �Ľ� ���� 

           if ( !result ) { // �Ľ� ���н� 
               return 0;
           }
           if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ� 
               String v_server = conf.getProperty("autoever.url.value");
               String fPath = conf.getProperty("dir.namo");   // ���� ���� ��� ����
               String refUrl = conf.getProperty("url.namo");; // ������ ����� ������ �����ϱ� ���� ���
               String prefix = "Community" + v_policeno;         // ���ϸ� ���ξ�
               result = namo.saveFile(fPath, v_server +refUrl, prefix); // ���� ���� ���� 
           }
           if ( !result ) { // �������� ���н� 
               return 0;
           }
           v_repmemo = namo.getContent(); // ���� ����Ʈ ���
*/           

           sql  =" update tz_cmupolice set  str_fg=2"
                + "                         ,str_dte=to_char(sysdate,'YYYYMMDDHH24MISS')"
                + "                         ,repmemo=?"
                + "                         ,str_userid=?"
                + "  where policeno =?";
           pstmt = connMgr.prepareStatement(sql);
           pstmt.setString(1, v_repmemo);
           pstmt.setString   (2, s_userid                       );
           pstmt.setString   (3, v_policeno                       );
           isOk = pstmt.executeUpdate();

//           sql1 = "select repmemo from tz_cmupolice where policeno = '" + v_policeno + "'";
//           connMgr.setOracleCLOB(sql1, v_repmemo);       //      (��Ÿ ���� ���)


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


               // �߽��� �̸���
               String v_tmp_send_email= "";
               sql1 = "select email   from tz_member where userid = '" +s_userid + "' ";
               ls = connMgr.executeQuery(sql1);
               while ( ls.next() ) v_tmp_send_email = ls.getString(1);


               sql1 = "select a.policeno, a.cmuno  , a.cmu_nm   , a.userid,b.name,b.email "
                     + "  from tz_cmupolice a,tz_member b "
                     + " where a.userid   = b.userid "
                     + "   and a.policeno ='" +v_policeno + "'";
               ls = connMgr.executeQuery(sql1);
               while ( ls.next() ) { 
                     v_mailno =v_mailno +1;
                     pstmt.setInt   (1, v_mailno                                );// �Ϸù�ȣ
                     pstmt.setString(2, ls.getString(4)                         );// �����ھ��̵�
                     pstmt.setString(3, ls.getString(5)                         );// �����ڸ�
                     pstmt.setString(4, ls.getString(6)                         );// �������̸���
                     pstmt.setString(5, ls.getString(2)                         );// Ŀ�´�Ƽ��ȣ
                     pstmt.setString(6, ls.getString(3)                            );// Ŀ�´�Ƽ��
                     pstmt.setString(7 ,s_userid                                );// �߽��ھ��̵�
                     pstmt.setString(8 ,v_tmp_send_email                        );// �߽����̸���
                     pstmt.setString(9 , ""                                     );// ����
                     pstmt.setString(10, v_repmemo);
                     pstmt.setString(11, "6"                                    );// ����
                     pstmt.setString(12, "�Ұ���Ŀ�´�Ƽ�Ű�"                     );// ���и�
                     isOk = pstmt.executeUpdate();
                     
                     if ( pstmt != null ) { pstmt.close(); }
                     
//                     sql1 = "select content  from TZ_CMUMAIL where mailno = '" +v_mailno + "'";
//                     connMgr.setOracleCLOB(sql1, v_repmemo);
                     if ( isOk > 0 ) { 
                         if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
                     }
               }
               if ( pstmt != null ) { pstmt.close(); }

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
    * ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   ����Ʈ
    * @throws Exception
    */
    public ArrayList selectList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
        DataBox             dbox    = null;

        String v_str_fg  = box.getStringDefault("p_str_fg","1");
//        String s_userid             = box.getSession("userid");
        int v_pageno        = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();
            sql  ="\n  select    a.policeno  policeno , a.cmuno   cmuno , a.cmu_nm    cmu_nm  "
                 + "\n           ,a.userid    userid   , a.email   email , a.content   content "
                 + "\n           ,a.singo_dte singo_dte, a.str_fg  str_fg, a.repmemo   repmemo "
                 + "\n           ,a.str_dte   str_dte  , a.str_userid    , d.cmu_nm    cmu_nm  "
                 + "\n           ,e.kor_name  room_master,e.userid  room_masterid, d.member_cnt member_cnt,d.accept_dte accept_dte,d.intro intro"
                 //+ "\n           ,b.deptnam         bdeptnam                ,b.jikupnm              bjikupnm ,b.jikwinm         bjikwinm                "     
                 + "\n           ,b.name                 bname"          
                 + "\n           ,b.userid                 buserid"
                 //+ "\n           ,nvl(c.deptnam,'') cdeptnam                ,nvl(c.jikupnm,'')      cjikupnm ,nvl(c.jikwinm,'') cjikwinm                "     
                 + "\n           ,nvl(c.name,'')         cname"          
                 + "\n           ,c.userid                 cuserid"
                 + "\n    from tz_cmupolice a,tz_member b,tz_member c,tz_cmubasemst d"
                 + "\n       ,(select cmuno,userid,kor_name,grade from tz_cmuusermst where grade='01' and close_fg='1') e"
                 + "\n   where a.cmuno            = d.cmuno  "
                 + "\n     and a.userid           = b.userid "  
                 + "\n     and d.cmuno            = e.cmuno  "
                 + "\n     and a.str_userid       = c.userid(+) "  
                 + "\n     and a.str_fg           = '" +v_str_fg + "'"
                 + "\n  order by a.policeno desc";
            
            System.out.println("==========  receive from the form object and session=========");
            System.out.println(sql);
            System.out.println("==========selectList=============");
            
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
    * ��ȸ
    * @param box          receive from the form object and session
    * @return ArrayList   ��ȸ
    * @throws Exception
    */
    public ArrayList selectView(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
        DataBox             dbox    = null;

        String v_policeno  = box.getString("p_policeno");
//        String s_userid             = box.getSession("userid");
//        int v_pageno        = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();
            sql  ="\n  select    a.policeno  policeno , a.cmuno   cmuno , a.cmu_nm    cmu_nm  "
                 + "\n           ,a.userid    userid   , a.email   email , a.content   content "
                 + "\n           ,a.singo_dte singo_dte, a.str_fg  str_fg, a.repmemo   repmemo "
                 + "\n           ,a.str_dte   str_dte  , a.str_userid    , d.cmu_nm    cmu_nm  "
                 + "\n           ,e.kor_name  room_master,d.member_cnt member_cnt,d.accept_dte accept_dte,d.intro intro"
                 //+ "\n           ,b.deptnam         bdeptnam                ,b.jikupnm              bjikupnm ,b.jikwinm         bjikwinm                "     
                 + "\n           ,b.name                 bname"          
                 //+ "\n           ,nvl(c.deptnam,'') cdeptnam                ,nvl(c.jikupnm,'')      cjikupnm ,nvl(c.jikwinm,'') cjikwinm                "     
                 + "\n           ,nvl(c.name,'')         cname"          
                 + "\n    from tz_cmupolice a,tz_member b,tz_member c,tz_cmubasemst d"
                 + "\n       ,(select cmuno,userid,kor_name,grade from tz_cmuusermst where grade='01' and close_fg='1') e"
                 + "\n   where a.cmuno            = d.cmuno  "
                 + "\n     and a.userid           = b.userid "  
                 + "\n     and d.cmuno            = e.cmuno  "
                 + "\n     and a.str_userid       = c.userid(+) "  
                 + "\n     and a.policeno         = " + StringManager.makeSQL(v_policeno);

            
            
            System.out.println("=================");
            System.out.println(sql);
            System.out.println("=================");
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
}