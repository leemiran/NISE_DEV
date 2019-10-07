// **********************************************************
// 1. ��      ��:
// 2. ���α׷���: CommunityFrBoardBean.java
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
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.PageList;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class CommunityFrBoardBean { 
    private ConfigSet config;
    private static int row=10;
//    private String v_type = "PQ";
//    private static final String FILE_TYPE = "p_file";           //      ���Ͼ��ε�Ǵ� tag name
    private static final int FILE_LIMIT = 1;                    //    �������� ���õ� ����÷�� ����


    public CommunityFrBoardBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );  // �� ����� �������� row ���� �����Ѵ�
            row = 10; // ������ ����
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
    
    public CommunityFrBoardBean(String type) { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        �� ����� �������� row ���� �����Ѵ�
//            this.v_type = type;
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    * ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   ����Ʈ
    * @throws Exception
    */
    public ArrayList selectListBrd(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
        String              sql1    = "";
        DataBox             dbox    = null;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
//        String v_brd_fg        = box.getString("p_brd_fg");
        String v_menuno        = box.getString("p_menuno");

        String v_searchtext = box.getString("p_searchtext");
        String v_select     = box.getString("p_select");
        String s_userid             = box.getSession("userid");
        int v_pageno        = box.getInt("p_pageno");
        String v_display_fg = "";  

        try { 
            connMgr = new DBConnectionManager();


            sql1 ="\n  select a.cmuno           cmuno                  ,a.menuno       menuno "
                 + "\n         ,a.brdno           brdno                  ,a.title        title "
                 + "\n         ,a.content         content                ,a.read_cnt     read_cnt"
                 + "\n         ,a.add_cnt         add_cnt                ,a.parent       parent "
                 + "\n         ,a.lv              lv                     ,a.position     position "
                 + "\n         ,a.display_fg      display_fg             ,a.root         root"  
                 + "\n         ,a.register_userid register_userid        ,a.register_dte register_dte"
                 + "\n         ,a.modifier_userid modifier_userid        ,a.modifier_dte modifier_dte"
                 + "\n         ,b.userid          userid                 ,fn_crypt('2', b.birth_date, 'knise')        birth_date "
                 + "\n         ,c.kor_name        name                   ,c.email        email "
                 //+ "\n         ,b.deptnam         deptnam                ,b.jikupnm      jikupnm ,b.jikwinm         jikwinm                "
                 + "\n         ,c.tel          tel"
                 + "\n         ,c.mobile          mobile                 ,c.office_tel   office_tel "
                 + "\n         ,c.wk_area         wk_area                ,c.grade        grade "
                 + "\n         ,e.kor_nm          grade_nm               ,nvl(d.cnt,0) cnt "
                 + "\n    from tz_cmuboard a,tz_member b,tz_cmuusermst c "
                 + "\n        ,(select cmuno cmuno   ,menuno    menuno "
                 + "\n                ,brdno brdno   , count(*) cnt  "
                 + "\n           from tz_cmuboardfile  "
                 + "\n          where cmuno     = '" +v_cmuno + "'"
                 + "\n            and menuno    = '" +v_menuno + "'"
                 + "\n          group by cmuno,menuno,brdno) d"
                 + "\n         ,tz_cmugrdcode  e  "
                 + "\n   where a.register_userid  = b.userid "  
                 + "\n     and a.cmuno            = c.cmuno "  
                 + "\n     and a.register_userid  = c.userid " 
                 + "\n     and c.cmuno            = e.cmuno  "
                 + "\n     and c.grade            = e.grcode "
                 + "\n     and a.cmuno           = d.cmuno(+)  "
                 + "\n     and a.menuno          = d.menuno(+) "
                 + "\n     and a.brdno           = d.brdno(+) "
                 + "\n     and a.cmuno            = '" +v_cmuno + "'"
                 + "\n     and a.menuno           = '" +v_menuno + "'"
                 + "\n     and a.del_fg           = 'N'"
                 ; 

            if ( !v_searchtext.equals("") ) {      // �˻�� ������
                 if ( v_select.equals("title"))   sql1 += "\n  and lower(a.title)    like lower (" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 if ( v_select.equals("content")) sql1 += "\n  and a.content like " + StringManager.makeSQL("%" + v_searchtext + "%");
                 if ( v_select.equals("name"))    sql1 += "\n  and lower(c.kor_name) like lower (" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
            }
            
            if(!CommunityMsMangeBean.getCheckGadmin(box,"A1")){
            	v_display_fg = "\n     and a.display_fg      = 'Y'";
            }
            
//            sql  ="\n  select a.*,rownum rowseq from ("
              sql = "\n  select a.* from ("
                 +sql1
                 + "\n     and  b.userid          != '" +s_userid + "'" 
                 //+ "\n     and  a.display_fg        = 'Y'"
                 + v_display_fg
                 + "\n  union all "
                 +sql1
                 + "\n     and  b.userid            = '" +s_userid + "'" 
                 + "\n   ) a"
                 + "\n  order by a.root desc,a.position asc";
//                 + "\n  ) a";
            ls = connMgr.executeQuery(sql);
            
            ls.setPageSize(row);                         // �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);                 // ������������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();    // ��ü ������ ���� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();    // ��ü row ���� ��ȯ�Ѵ�
//System.out.println(sql);
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
    * ���ο�������ϴ� ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   ����Ʈ
    * @throws Exception
    */
    public ArrayList selectRoomIndexListBrd(RequestBox box,String v_brd_fg) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
        String              sql1    = "";
        DataBox             dbox    = null;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");

        String s_userid             = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();


            sql1 ="\n  select a.cmuno           cmuno                  ,a.menuno       menuno "
                 + "\n         ,a.brdno           brdno                  ,a.title        title "
                 + "\n         ,a.content         content                ,a.read_cnt     read_cnt"
                 + "\n         ,a.add_cnt         add_cnt                ,a.parent       parent "
                 + "\n         ,a.lv              lv                     ,a.position     position "
                 + "\n         ,a.display_fg      display_fg             ,a.root         root"  
                 + "\n         ,a.register_userid register_userid        ,a.register_dte register_dte"
                 + "\n         ,a.modifier_userid modifier_userid        ,a.modifier_dte modifier_dte"
                 + "\n         ,b.userid          userid                 ,fn_crypt('2', b.birth_date, 'knise')        birth_date "
                 + "\n         ,c.kor_name        name                   ,c.email        email "
                 //+ "\n         ,b.deptnam         deptnam                ,b.jikupnm      jikupnm ,b.jikwinm         jikwinm                "
                 + "\n         ,c.tel          tel"
                 + "\n         ,c.mobile          mobile                 ,c.office_tel   office_tel "
                 + "\n         ,c.wk_area         wk_area                ,c.grade        grade "
                 + "\n         ,e.kor_nm          grade_nm               ,nvl(d.cnt,0) cnt "
                 + "\n    from tz_cmuboard a,tz_member b,tz_cmuusermst c "
                 + "\n        ,(select cmuno cmuno   ,menuno    menuno "
                 + "\n                ,brdno brdno   ,count(*) cnt  "
                 + "\n           from tz_cmuboardfile  "
                 + "\n          where cmuno     = '" +v_cmuno + "'"
                 + "\n          group by cmuno,menuno,brdno) d"
                 + "\n         ,tz_cmugrdcode  e  ,tz_cmumenu f"
                 + "\n   where a.register_userid  = b.userid "  
                 + "\n     and a.cmuno            = c.cmuno "  
                 + "\n     and a.register_userid  = c.userid " 
                 + "\n     and c.cmuno            = e.cmuno  "
                 + "\n     and c.grade            = e.grcode "
                 + "\n     and a.cmuno           = d.cmuno(+)  "
                 + "\n     and a.menuno          = d.menuno(+) "
                 + "\n     and a.brdno           = d.brdno(+) "
                 + "\n     and a.cmuno            = '" +v_cmuno + "'"
                 + "\n     and a.cmuno            = f.cmuno  "
                 + "\n     and a.menuno            = f.menuno  "
                 + "\n     and a.lv               =1";
             if ( "0".equals(v_brd_fg)) { // ��������
                sql1 +="\n     and f.brd_fg           = '0'"
                      + "\n     and a.del_fg           = 'N'"
                     ; 
             } else { 
                sql1 +="\n     and f.brd_fg           in ('1','2')"
                      + "\n     and a.del_fg           = 'N'"
                     ; 
             }
//             sql  ="\n  select a.*,rownum rowseq from ("
                  sql= "\n  select a.* from ("
                  +sql1
                  + "\n     and  b.userid          != '" +s_userid + "'" 
                  + "\n     and  a.display_fg        = 'Y'"
                  + "\n  union all "
                  +sql1
                  + "\n     and  b.userid            = '" +s_userid + "'" 
                  + "\n   ) a"
                  + "\n  order by a.root desc,a.position asc";
//                  + "\n  ) a";
             ls = connMgr.executeQuery(sql);
            

            ls.setPageSize(3);                          // �������� row ������ �����Ѵ�
            ls.setCurrentPage(1);                 // ������������ȣ�� �����Ѵ�.
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
    * ���հ˻� ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   ����Ʈ
    * @throws Exception
    */
    public ArrayList selectRoomTotalListBrd(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
        String              sql1    = "";
        DataBox             dbox    = null;

        String v_searchtext    = box.getString("p_searchtext");
        String v_select        = box.getString("p_select");
        String s_userid        = box.getSession("userid");
        int    v_pageno        = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();


            sql1 ="\n  select  brd_fg, a.cmuno           cmuno                  ,a.menuno       menuno "
                 + "\n         ,a.brdno           brdno                  ,a.title        title "
                 + "\n         ,a.content         content                ,a.read_cnt     read_cnt"
                 + "\n         ,a.add_cnt         add_cnt                ,a.parent       parent "
                 + "\n         ,a.lv              lv                     ,a.position     position "
                 + "\n         ,a.display_fg      display_fg             ,a.root         root"  
                 + "\n         ,a.register_userid register_userid        ,a.register_dte register_dte"
                 + "\n         ,a.modifier_userid modifier_userid        ,a.modifier_dte modifier_dte"
                 + "\n         ,b.userid          userid                 ,fn_crypt('2', b.birth_date, 'knise')        birth_date "
                 + "\n         ,c.kor_name        name                   ,c.email        email "
                 //+ "\n         ,b.deptnam         deptnam                ,b.jikupnm      jikupnm ,b.jikwinm         jikwinm                "
                 + "\n         ,c.tel          tel"
                 + "\n         ,c.mobile          mobile                 ,c.office_tel   office_tel "
                 + "\n         ,c.wk_area         wk_area                ,c.grade        grade "
                 + "\n         ,e.kor_nm          grade_nm               ,nvl(d.cnt,0) cnt "
                 + "\n         ,g.cmu_nm          cmu_nm                 ,g.accept_dte   accept_dte"
                 + "\n         ,f.title           menu_title"
                 + "\n    from tz_cmuboard a,tz_member b,tz_cmuusermst c "
                 + "\n        ,(select cmuno cmuno   ,menuno    menuno "
                 + "\n                ,brdno brdno   ,count(*) cnt  "
                 + "\n           from tz_cmuboardfile  "
                 + "\n          group by cmuno,menuno,brdno) d"
                 + "\n         ,tz_cmugrdcode  e  ,tz_cmumenu f,tz_cmubasemst g"
                 + "\n   where a.register_userid  = b.userid "  
                 + "\n     and a.cmuno            = c.cmuno "  
                 + "\n     and a.register_userid  = c.userid " 
                 + "\n     and c.cmuno            = e.cmuno  "
                 + "\n     and a.cmuno            = g.cmuno  "
                 + "\n     and c.grade            = e.grcode "
                 + "\n     and a.cmuno           = d.cmuno(+)  "
                 + "\n     and a.menuno          = d.menuno(+) "
                 + "\n     and a.brdno           = d.brdno(+) "
                 + "\n     and a.cmuno            = f.cmuno  "
                 + "\n     and a.menuno           = f.menuno  "
                 + "\n     and a.lv               = 1"
                 + "\n     and f.brd_fg in('1','2')"
                 + "\n     and g.close_fg         = '1'"
                 + "\n     and a.del_fg           = 'N'"
                     ; 
            if ( !v_searchtext.equals("") ) {      // �˻�� ������
                 if ( v_select.equals("title"))   sql1 += "\n  and lower(a.title)    like lower (" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 if ( v_select.equals("content")) sql1 += "\n  and lower(a.content)  like lower (" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 if ( v_select.equals("name"))    sql1 += "\n  and lower(c.kor_name) like lower (" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
            }

//             sql  ="\n  select a.*,rownum rowseq from ("
               sql = "\n  select a.*, ( select count(*) from (" + sql1 + ") b where b.cmuno = a.cmuno and b.menuno < a.menuno ) rowseq  from ("
                  +sql1
                  + "\n     and  b.userid          != '" +s_userid + "'" 
                  + "\n     and  a.display_fg        = 'Y'"
                  + "\n  union all "
                  +sql1
                  + "\n     and  b.userid            = '" +s_userid + "'" 
                  + "\n   ) a"
                  + "\n  order by a.register_dte desc ";
//                  + "\n  ) a";
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
    * �ٹ�����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   �ٹ�����Ʈ
    * @throws Exception
    */
    public ArrayList selectAlbumListBrd(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
        String              sql1    = "";
        DataBox             dbox    = null;

//        String v_static_cmuno  = box.getString("p_static_cmuno");
        String v_cmuno         = box.getString("p_cmuno");
//        String v_brd_fg        = box.getString("p_brd_fg");
        String v_menuno        = box.getString("p_menuno");

        String v_searchtext = box.getString("p_searchtext");
        String v_select     = box.getString("p_select");
        String s_userid             = box.getSession("userid");
        int v_pageno        = box.getInt("p_pageno");
        String v_display_fg = "";

        try { 
            connMgr = new DBConnectionManager();


            sql1 ="\n  select a.cmuno           cmuno                  ,a.menuno       menuno "
                 + "\n         ,a.brdno           brdno                  ,a.title        title "
                 + "\n         ,a.content         content                ,a.read_cnt     read_cnt"
                 + "\n         ,a.add_cnt         add_cnt                ,a.parent       parent "
                 + "\n         ,a.lv              lv                     ,a.position     position "
                 + "\n         ,a.display_fg      display_fg             ,a.root         root"  
                 + "\n         ,a.register_userid register_userid        ,a.register_dte register_dte"
                 + "\n         ,a.modifier_userid modifier_userid        ,a.modifier_dte modifier_dte"
                 + "\n         ,b.userid          userid                 ,fn_crypt('2', b.birth_date, 'knise')        birth_date "
                 + "\n         ,c.kor_name        name                   ,c.email        email "
                 //+ "\n         ,b.deptnam         deptnam                ,b.jikupnm      jikupnm ,b.jikwinm         jikwinm                "
                 + "\n         ,c.tel          tel"
                 + "\n         ,c.mobile          mobile                 ,c.office_tel   office_tel "
                 + "\n         ,c.wk_area         wk_area                ,c.grade        grade "
                 + "\n         ,e.kor_nm          grade_nm               ,nvl(d.cnt,0) cnt "
                 + "\n         ,nvl(f.savefile,'../../images/community/photo_smallback.gif')        savefile"
                 + "\n    from tz_cmuboard a,tz_member b,tz_cmuusermst c "
                 + "\n        ,(select cmuno cmuno   ,menuno    menuno "
                 + "\n                ,brdno brdno   , count(*) cnt  "
                 + "\n           from tz_cmuboardfile  "
                 + "\n          where cmuno     = '" +v_cmuno + "'"
                 + "\n            and menuno    = '" +v_menuno + "'"
                 + "\n          group by cmuno,menuno,brdno) d"
                 + "\n         ,tz_cmugrdcode  e  "
                 + "\n         ,tz_cmuboardfile f"
                 + "\n   where a.register_userid  = b.userid "  
                 + "\n     and a.cmuno            = c.cmuno "  
                 + "\n     and a.register_userid  = c.userid " 
                 + "\n     and c.cmuno            = e.cmuno  "
                 + "\n     and c.grade            = e.grcode "
                 + "\n     and a.cmuno           = d.cmuno(+)  "
                 + "\n     and a.menuno          = d.menuno(+) "
                 + "\n     and a.brdno           = d.brdno(+) "
                 + "\n     and a.cmuno           = f.cmuno(+)  "
                 + "\n     and a.menuno          = f.menuno(+) "
                 + "\n     and a.brdno           = f.brdno(+) "
                 + "\n     and a.cmuno            = '" +v_cmuno + "'"
                 + "\n     and a.menuno           = '" +v_menuno + "'"
                 + "\n     and a.del_fg           = 'N'"
                 ; 

            if ( !v_searchtext.equals("") ) {      // �˻�� ������
                 if ( v_select.equals("title"))   sql1 += "\n  and lower(a.title)    like lower (" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 if ( v_select.equals("content")) sql1 += "\n  and lower(a.content)  like lower (" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 if ( v_select.equals("name"))    sql1 += "\n  and lower(c.kor_name) like lower (" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
            }
            
            if(!CommunityMsMangeBean.getCheckGadmin(box,"A1")){
            	v_display_fg = "\n     and a.display_fg      = 'Y'";
            }
//            sql  ="\n  select a.*,rownum rowseq from ("
              sql = "\n  select a.* from ("
                 +sql1
                 + "\n     and  b.userid          != '" +s_userid + "'" 
                 //+ "\n     and  a.display_fg        = 'Y'"
                 + v_display_fg
                 + "\n  union all "
                 +sql1
                 + "\n     and  b.userid            = '" +s_userid + "'" 
                 + "\n   ) a"
                 + "\n  order by a.root desc,a.position asc";
//                 + "\n  ) a";
            ls = connMgr.executeQuery(sql);
            
            // �ٹ��ΰ�츸 8�μ����Ѵ�.
            CommunityMsMenuBean bean= new CommunityMsMenuBean();
            String v_tmp_brd_fg =bean.getSingleColumn(box,v_cmuno,v_menuno,"brd_fg");
            if ( "3".equals(v_tmp_brd_fg)) row=8;


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
    * �Խ��� ��ȸ
    * @param box          receive from the form object and session
    * @return ArrayList   �Խ��� ��ȸ
    * @throws Exception
    */
    public ArrayList selectViewBrd(RequestBox box,String qryFlag) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        ArrayList           list1   = new ArrayList();
        ArrayList           list2   = new ArrayList();
        ArrayList           list3   = new ArrayList();
        ArrayList           list4   = new ArrayList();
        ArrayList           list5   = new ArrayList();

        String              sql     = "";
        String              sql1    = "";
        DataBox             dbox    = null;
//        int                 isOK    = 1;

//        String v_static_cmuno       = box.getString("p_static_cmuno");
        String v_cmuno              = box.getString("p_cmuno");
//        String v_brd_fg             = box.getString("p_brd_fg");
        String v_menuno             = box.getString("p_menuno");
        String v_brdno              = box.getString("p_brdno");
//        String s_userid             = box.getSession("userid");
//        int    v_rowseq             = box.getInt("p_rowseq");
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // ��ȸ�� ����.
            if ( "VIEW".equals(qryFlag)) { 
                sql  =" update tz_cmuboard set read_cnt =read_cnt +1"
                    + "  where cmuno           = '" +v_cmuno + "'"
                    + "    and menuno          =  " +v_menuno
                    + "    and brdno           =  " +v_brdno
                    ;
                pstmt = connMgr.prepareStatement(sql);
                pstmt.executeUpdate(); //isOK =
                if ( pstmt != null ) { pstmt.close(); }
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            }  
            sql1  = "\n select a.cmuno           cmuno                  ,a.menuno       menuno "
                 + "\n        ,a.brdno           brdno                  ,a.title        title "
                 + "\n        ,a.content         content                ,a.read_cnt     read_cnt"
                 + "\n        ,a.add_cnt         add_cnt                ,a.parent       parent "
                 + "\n        ,a.lv              lv                     ,a.position     position "
                 + "\n        ,a.display_fg      display_fg             ,a.root         root"
                 + "\n        ,a.register_userid register_userid        ,a.register_dte register_dte"
                 + "\n        ,a.modifier_userid modifier_userid        ,a.modifier_dte modifier_dte"
                 + "\n        ,b.userid          userid                 ,fn_crypt('2', b.birth_date, 'knise')        birth_date "
                 + "\n        ,c.kor_name        name                   ,c.email        email "
                 //+ "\n         ,b.deptnam         deptnam                ,b.jikupnm      jikupnm ,b.jikwinm         jikwinm                "
                 + "\n         ,c.tel          tel"
                 + "\n        ,c.mobile          mobile                 ,c.office_tel   office_tel "
                 + "\n        ,c.wk_area         wk_area                ,c.grade        grade "
                 + "\n        ,e.kor_nm          grade_nm               ,nvl(d.cnt,0) cnt "
                 + "\n   from tz_cmuboard a,tz_member b,tz_cmuusermst c "
                 + "\n       ,(select cmuno cmuno   ,menuno    menuno "
                 + "\n               ,brdno brdno   , count(*) cnt  "
                 + "\n          from tz_cmuboardfile  "
                 + "\n         where cmuno     = '" +v_cmuno + "'"
                 + "\n           and menuno    = '" +v_menuno + "'"
                 + "\n         group by cmuno,menuno,brdno) d"
                 + "\n        ,tz_cmugrdcode  e  "
                 + "\n  where a.register_userid  = b.userid "  
                 + "\n    and a.cmuno            = c.cmuno "  
                 + "\n    and a.register_userid  = c.userid " 
                 + "\n    and c.cmuno            = e.cmuno  "
                 + "\n    and c.grade            = e.grcode "
                 + "\n    and a.cmuno           = d.cmuno(+)  "
                 + "\n    and a.menuno          = d.menuno(+) "
                 + "\n    and a.brdno           = d.brdno(+) "

                 + "\n    and a.del_fg           = 'N'"
                 + "\n    and a.cmuno            = '" +v_cmuno + "'"
                 + "\n    and a.menuno           = '" +v_menuno + "'"


                 ;
            sql=sql1 + "\n    and a.brdno            = '" +v_brdno + "'";      
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list1.add(dbox);
            }
            ls.close();

            // ÷�������б�
            sql  = "\n select cmuno,menuno,brdno,fileno,realfile,savepath,savefile,filesize"
                  + "\n       ,register_userid,register_dte,modifier_userid,modifier_dte"
                  + "\n   from tz_cmuboardfile "
                  + "\n  where cmuno           = '" +v_cmuno + "'"
                  + "\n    and menuno          =  " +v_menuno
                  + "\n    and brdno           =  " +v_brdno
                 ;
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list2.add(dbox);
            }
            ls.close();

//            int listRowCnt=0;
//            // �������б�
//            sql  = "\n  select a.* from(select a.*,rownum rowseq from (" +sql1
//                  + "\n    order by a.root desc,a.position asc) a)a"
//                  + "\n  where a.rowseq =" +(v_rowseq +1);
//            ls = connMgr.executeQuery(sql);
//            while ( ls.next() ) { 
//                listRowCnt++;
//                dbox = ls.getDataBox();
//                list3.add(dbox);
//            }

            // �������б�
//            listRowCnt=0; 
//            sql  = "  select a.* from(select a.*,rownum rowseq from (" +sql1
//                  + "    order by a.root desc,a.position asc) a)a"
//                  + "  where a.rowseq =" +(v_rowseq-1);
//
//            ls = connMgr.executeQuery(sql);
//            while ( ls.next() ) { 
//                listRowCnt++;
//                dbox = ls.getDataBox();
//                list4.add(dbox);
//            }


            // ����б�
            sql  = " select a.cmuno                   cmuno       ,a.menuno                     menuno"
                  + "       ,a.brdno                   brdno       ,a.rplno                      rplno"
                  + "       ,a.content                 content     ,a.userid                     userid"
                  + "       ,a.register_dte            register_dte,a.modifier_dte               modifier_dte"
                  + "       ,fn_crypt('2', b.birth_date, 'knise')                   birth_date       ,b.name                       name, c.email email"
                  //+ "       ,c.email                   email       ,b.deptnam                    deptnam"
                  //+ "       ,b.jikupnm                 jikupnm     ,b.jikwinm                    jikwinm"
                  + "   from tz_cmuboardreplay a,tz_member b,tz_cmuusermst c "
                  + "  where a.userid          = b.userid "  
                  + "    and a.cmuno           = c.cmuno  "
                  + "    and a.userid          = c.userid "
                  + "    and a.cmuno           = '" +v_cmuno + "'"
                  + "    and a.menuno          = '" +v_menuno + "'"
                  + "    and a.brdno           = '" +v_brdno + "'"
                  + "  order by a.rplno desc" 
                 ;

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list5.add(dbox);
            }
            ls.close();

            list.add(list1);
            list.add(list2);
            list.add(list3);
            list.add(list4);
            list.add(list5);
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
        return list;
    }

    /**
    * �Խ��ǵ���ϱ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertBrd(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String              sql     = "";

        int         isOk1       = 1;
        int         isOk2       = 1;
//        int         v_seq       = 0;
        int         v_brdno     = 0; 

//        String v_static_cmuno       = box.getString("p_static_cmuno");
        String v_cmuno              = box.getString("p_cmuno");
//        String v_brd_fg             = box.getString("p_brd_fg");
        String v_menuno             = box.getString("p_menuno");

        String v_content            = box.getString("content");//StringManager.replace(box.getString("content"),"<br > ","\n");

        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           sql = "select nvl(max(brdno), 0) from tz_cmuboard where  cmuno = '" +v_cmuno + "' and menuno = '" +v_menuno + "'";
           ls = connMgr.executeQuery(sql);
           while ( ls.next() ) { 
               v_brdno = ls.getInt(1) + 1;
           } 

           int tmp =0;
           sql = "select nvl(max(position), 0) from tz_cmuboard where cmuno = '" +v_cmuno + "' and menuno = '" +v_menuno + "'";
           ls = connMgr.executeQuery(sql);
           while ( ls.next() ) { 
               tmp = ls.getInt(1) + 1;
           } 

           sql  =" insert into tz_cmuboard ( cmuno            , menuno           , brdno      "    
                + "                         , title            , content          , read_cnt"
                + "                         , add_cnt          , lv               , position  "
                + "                         , display_fg       , register_userid  , register_dte "
                + "                         , modifier_userid  , modifier_dte     , del_fg"
                + "                         , parent           , root             )"
                + "                  values ( ?                , ?                , ?"
                + "                         , ?                , ?                , 0"
                + "                         , 0                , ?                , ?"
                + "                         , ?                , ?                , to_char(sysdate,'YYYYMMDDHH24MISS')"
                + "                         , ?                ,to_char(sysdate,'YYYYMMDDHH24MISS'),'N'"
                + "                         , ?                , ?                )"
                ;
           pstmt = connMgr.prepareStatement(sql);
           pstmt.setString(1, v_cmuno                            );
           pstmt.setString(2, v_menuno                           );
           pstmt.setInt   (3, v_brdno                            );
           pstmt.setString(4, box.getString("p_title" )          );// ����
           pstmt.setString(5, box.getString("content" )          );
           pstmt.setInt   (6, 1                                  );// �亯����
           pstmt.setInt   (7, tmp                                );// �亯��ġ
           pstmt.setString(8, box.getStringDefault("p_display_fg","N" )     );// ��������
           pstmt.setString(9, s_userid);// �Խ���
           pstmt.setString(10, s_userid );// ������
           pstmt.setInt   (11, v_brdno                             );// �θ�
           pstmt.setInt   (12, v_brdno                             );// ROOT
           isOk1 = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }
           String sql1 = "select content from tz_cmuboard where cmuno = '" +v_cmuno + "' and menuno = '" +v_menuno + "' and brdno =" +v_brdno;
//           connMgr.setOracleCLOB(sql1, v_content);



            isOk2 = this.insertUpFile(connMgr,box,String.valueOf(v_brdno));

            if ( isOk1 > 0 && isOk2 > 0 ) { 
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
    * �亯����ϱ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int replyBrd(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";

        int         isOk1       = 1;
        int         isOk2       = 1;
        int         isOk3       = 1;
//        int         v_seq       = 0;
        int         v_thisbrdno =0;


//        String v_static_cmuno       = box.getString("p_static_cmuno");
        String v_cmuno              = box.getString("p_cmuno");
//        String v_brd_fg             = box.getString("p_brd_fg");
        String v_menuno             = box.getString("p_menuno");
        String v_brdno              = box.getString("p_brdno");

        int    v_lv                 = box.getInt("p_lv");
        int    v_position           = box.getInt("p_position");
//        int    v_parent             = box.getInt("p_parent");
        int    v_root               = box.getInt("p_root");
        String v_content            = StringManager.replace(box.getString("content"),"<br > ","\n");
        String s_userid             = box.getSession("userid");
//        String s_name               = box.getSession("name");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 



           sql  =" update tz_cmuboard set    position = position +1"
                + "  where cmuno        = ?"
                + "     and menuno       = ?"
                + "     and position    > ?"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1, v_cmuno                         );// Ŀ�´�Ƽ��ȣ
           pstmt.setString(2, v_menuno                        );// �޴���ȣ
           pstmt.setInt   (3, v_position                      );// �亯��ġ
           pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }

           sql = "select nvl(max(brdno), 0) from tz_cmuboard where  cmuno = '" +v_cmuno + "' and menuno = '" +v_menuno + "'";
           ls = connMgr.executeQuery(sql);
           while ( ls.next() ) { 
               v_thisbrdno = ls.getInt(1) + 1;
           } 
           ls.close();

//           int tmp =0;
           sql = "select nvl(max(position), 0) from tz_cmuboard where cmuno = '" +v_cmuno + "' and menuno = " +v_menuno;
           ls = connMgr.executeQuery(sql);
           while ( ls.next() ) { 
//               tmp = ls.getInt(1) + 1;
           } 
           ls.close();
           
           sql  =" insert into tz_cmuboard ( cmuno            , menuno           , brdno      "    
                + "                         , title            , content          , read_cnt"
                + "                         , add_cnt          , lv               , position  "
                + "                         , display_fg       , register_userid  , register_dte "
                + "                         , modifier_userid  , modifier_dte     , del_fg"
                + "                         , parent           , root             )"
                + "                  values ( ?                , ?                , ?"
                + "                         , ?                , ?     , 0"
                + "                         , 0                , ?                , ?"
                + "                         , ?                , ?                , to_char(sysdate,'YYYYMMDDHH24MISS')"
                + "                         , ?                ,to_char(sysdate,'YYYYMMDDHH24MISS'),'N'"
                + "                         , ?                , ?                )"
                ;
           pstmt = connMgr.prepareStatement(sql);
           pstmt.setString(1, v_cmuno                            );
           pstmt.setString(2, v_menuno                           );
           pstmt.setInt   (3, v_thisbrdno                        );
           pstmt.setString(4, box.getString("p_title" )          );// ����
           pstmt.setString(5, v_content          );
           pstmt.setInt   (6, v_lv +1                             );// �亯����
           pstmt.setInt   (7, v_position +1                       );// �亯��ġ
           pstmt.setString(8, box.getStringDefault("p_display_fg","N" )     );// ��������
           pstmt.setString(9, s_userid);// �Խ���
           pstmt.setString(10, s_userid );// ������
           pstmt.setString(11, v_brdno                             );// �θ�
           pstmt.setInt   (12, v_root                             );// ROOT
           isOk1 = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }
//           String sql1 = "select content from tz_cmuboard where cmuno = '" +v_cmuno + "' and menuno = '" +v_menuno + "' and brdno =" +v_brdno;
//           connMgr.setOracleCLOB(sql1, v_content);



            isOk3 = this.insertUpFile(connMgr,box,String.valueOf(v_thisbrdno));

            if ( isOk1 > 0 && isOk2 > 0 && isOk3 > 0 ) { 
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
        return isOk1*isOk2*isOk3;
    }

    /**
    * Q&A�����ϱ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int updateBrd(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";

        int         isOk1       = 1;
        int         isOk2       = 1; 
//        int         v_seq       = 0;



//        String v_static_cmuno       = box.getString("p_static_cmuno");
        String v_cmuno              = box.getString("p_cmuno");
//        String v_brd_fg             = box.getString("p_brd_fg");
        String v_menuno             = box.getString("p_menuno");
        String v_brdno              = box.getString("p_brdno");

        String v_content            = StringManager.replace(box.getString("content"),"<br > ","\n");

        String s_userid             = box.getSession("userid");
//        String s_name               = box.getSession("name");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           sql  =" update tz_cmuboard set  title           = ?"      
                + "                       , content         = ?"
                + "                       , display_fg      = ?"
                + "                       , modifier_userid = ?"
                + "                       , modifier_dte =to_char(sysdate,'YYYYMMDDHH24MISS')"
                + "  where cmuno           = ?"
                + "    and menuno          = ?"
                + "    and brdno           = ?"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1, box.getString("p_title" )          );// ����
           pstmt.setString(2, box.getString("content" )         );
           pstmt.setString(3, box.getString("p_display_fg" )     );// ��������
           pstmt.setString(4, s_userid);// �Խ���
           pstmt.setString(5 , v_cmuno                            );
           pstmt.setString(6 , v_menuno                           );
           pstmt.setString(7 , v_brdno                        );

           isOk1 = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }
           String sql1 = "select content from tz_cmuboard where cmuno = '" +v_cmuno + "' and menuno = '" +v_menuno + "' and brdno =" +v_brdno;
//           connMgr.setOracleCLOB(sql1, v_content);




           isOk2 = this.insertUpFile(connMgr,box,v_brdno);

            if ( isOk1 > 0 && isOk2 > 0 ) { 
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
    * QNA ���ο� �ڷ����� ���
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */

     public int insertUpFile(DBConnectionManager connMgr, RequestBox    box,String v_brdno) throws Exception { 
        ListSet           ls      = null;
        PreparedStatement pstmt   = null;
        String            sql     = "";
        String            sql2    = "";
        int               isOk2   = 1;
        int               vfileno = 0;

//        String v_static_cmuno       = box.getString("p_static_cmuno");
        String v_cmuno              = box.getString("p_cmuno");
//        String v_brd_fg             = box.getString("p_brd_fg");
        String v_menuno             = box.getString("p_menuno");


        String s_userid             = box.getSession("userid");
//        String s_name               = box.getSession("name");


        CommunityMsMenuBean bean = new CommunityMsMenuBean();
        int    v_filecnt            = Integer.parseInt(bean.getSingleColumn(box,v_cmuno,v_menuno,"filecnt") );

        String [] v_realFile     = new String [v_filecnt];
        String [] v_saveFile     = new String [v_filecnt];

        for ( int i = 0; i < v_filecnt; i++ ) { 
            v_realFile[i] = box.getRealFileName("p_file" + (i +1));
            v_saveFile[i] = box.getNewFileName ("p_file" + (i +1));
        }

        try { 
             sql2 = "select nvl(max(fileno), 0) "
                   + "  from tz_cmuboardfile "
                   + " where cmuno   = '" +v_cmuno + "'"
                   + "   and menuno  =  " +v_menuno
                   + "   and brdno   =  " +v_brdno;
             ls = connMgr.executeQuery(sql2);
             while ( ls.next() ) { 
                 vfileno = ls.getInt(1);
             } 
             ls.close();

             sql  =" insert into tz_cmuboardfile ( cmuno, menuno,brdno, fileno, realfile, savepath"
                  + "                       , savefile, filesize, register_userid, register_dte, modifier_userid, modifier_dte)"
                  + "               values  (?,?,?,?,?,''"
                  + "                       ,?,null,?"
                  + "                       ,to_char(sysdate,'YYYYMMDDHH24MISS'),?"
                  + "                       ,to_char(sysdate,'YYYYMMDDHH24MISS'))"
                  ;
             pstmt = connMgr.prepareStatement(sql);
             for ( int    i = 0; i < v_filecnt; i++ ) { 
                 if ( !v_realFile[i].equals("")&& "".equals(box.getString("p_fileno" +(i +1))) )   { 
                     vfileno++;
                     pstmt.setString(1 , v_cmuno                            );
                     pstmt.setString(2 , v_menuno                           );
                     pstmt.setString(3 , v_brdno                        );
                     pstmt.setInt   (4, vfileno        );// �����Ϸù�ȣ
                     pstmt.setString(5, v_realFile[i]  );// �������ϸ�
                     pstmt.setString(6, v_saveFile[i]  );// �������ϸ�
                     pstmt.setString(7, s_userid       );// �Խ���
                     pstmt.setString(8, s_userid       );// ������
                     isOk2 = pstmt.executeUpdate();
                     if ( pstmt != null ) { pstmt.close(); }
                 }
              } 
        }
        catch ( Exception ex ) { 
            FileManager.deleteFile(v_saveFile, FILE_LIMIT);     //  �Ϲ�����, ÷������ ������ ����..
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
        }
        return isOk2;
    }

    /**
     * ���õ� �ڷ����� DB���� ����
     * @param connMgr           DB Connection Manager
     * @param box               receive from the form object and session
     * @param p_filesequence    ���� ���� ����
     * @return
     * @throws Exception
     */
    public int deleteUpFile( RequestBox box)    throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet           ls      = null;
        String            sql     = "";
        String            sql2    = "";
        int               isOk2   = 1;


//        String v_static_cmuno       = box.getString("p_static_cmuno");
        String v_cmuno              = box.getString("p_cmuno");
//        String v_brd_fg             = box.getString("p_brd_fg");
        String v_menuno             = box.getString("p_menuno");
        String v_brdno              = box.getString("p_brdno");
        int    v_fileno             = box.getInt("p_delfileno");        

//        String s_userid             = box.getSession("userid");
//        String s_name               = box.getSession("name");
        String v_savefilenm           = "";
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 
             sql2 = "select savefile "
                   + "  from tz_cmuboardfile "
                   + " where cmuno   = '" +v_cmuno + "'"
                   + "   and menuno  =  " +v_menuno
                   + "   and brdno   =  " +v_brdno
                   + "   and fileno  =  " +v_fileno;
             ls = connMgr.executeQuery(sql2);
             while ( ls.next() ) { 
                 v_savefilenm = ls.getString(1);
             } 
             ls.close();

             sql  =" delete from  tz_cmuboardfile "
                  + "  where cmuno   = '" +v_cmuno + "'"
                  + "    and menuno  =  " +v_menuno
                  + "    and brdno   =  " +v_brdno
                  + "    and fileno  =  " +v_fileno
              ;

             pstmt = connMgr.prepareStatement(sql);
             isOk2 = pstmt.executeUpdate();
             if ( pstmt != null ) { pstmt.close(); }
             Vector v_saveFile = new Vector();
             v_saveFile.addElement(v_savefilenm);
             if ( isOk2 > 0 ) { 
                FileManager.deleteFile(v_saveFile);     //  �Ϲ�����, ÷������ ������ ����..
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
             }
        }
        catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql  + "\r\n" + ex.getMessage() );
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
    * ��۵���ϱ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertBrdMemo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";

        int         isOk1       = 1;
        int         isOk2       = 1;
//        int         v_seq       = 0;
        int         v_rplno     = 0; 

//        String v_static_cmuno       = box.getString("p_static_cmuno");
        String v_cmuno              = box.getString("p_cmuno");
//        String v_brd_fg             = box.getString("p_brd_fg");
        String v_menuno             = box.getString("p_menuno");
        String v_brdno              = box.getString("p_brdno");
        String s_userid             = box.getSession("userid");
//        String s_name               = box.getSession("name");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           sql = "select nvl(max(rplno), 0) from tz_cmuboardreplay where cmuno = '" +v_cmuno + "' and menuno = '" +v_menuno + "' and brdno =" +v_brdno;
           ls = connMgr.executeQuery(sql);
           while ( ls.next() ) v_rplno = ls.getInt(1) + 1;

           sql  =" insert into tz_cmuboardreplay ( cmuno     , menuno,brdno          , rplno       , content   , userid"
                + "                       , register_dte , modifier_dte, del_fg)"
                + "               values  (?,?,?,?,?,?"
                + "                       ,to_char(sysdate,'YYYYMMDDHH24MISS')"
                + "                       ,to_char(sysdate,'YYYYMMDDHH24MISS'),'N')"
                ;
           pstmt = connMgr.prepareStatement(sql);
           pstmt.setString(1 , v_cmuno                            );
           pstmt.setString   (2 , v_menuno                           );
           pstmt.setString   (3 , v_brdno                        );
           pstmt.setInt   (4,  v_rplno        );
           pstmt.setString(5, box.getString("p_rep_content" )          );// ����
           pstmt.setString(6, s_userid);// �Խ���
           isOk1 = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }
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
    * ��ۻ����ϱ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int deleteBrdMemo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";

        int         isOk1       = 1;
        int         isOk2       = 1;
//        int         v_seq       = 0;

//        String v_static_cmuno       = box.getString("p_static_cmuno");
        String v_cmuno              = box.getString("p_cmuno");
//        String v_brd_fg             = box.getString("p_brd_fg");
        String v_menuno             = box.getString("p_menuno");
        String v_brdno              = box.getString("p_brdno");
        int    v_rplno              = box.getInt("p_rplno"); 

//        String s_userid             = box.getSession("userid");
//        String s_name               = box.getSession("name");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           sql = "delete  from tz_cmuboardreplay where cmuno = '" +v_cmuno + "' and menuno = '" +v_menuno + "' and brdno =" +v_brdno + " and rplno=" +v_rplno;
           pstmt = connMgr.prepareStatement(sql);
           isOk1 = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }
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
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk1*isOk2;
    }


    /**
    * �ۻ����ϱ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int deleteBrdData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt1 = null;
        PreparedStatement   pstmt2 = null;
        PreparedStatement   pstmt3 = null;
        ListSet     ls          = null;
        String      sql         = "";

        int         isOk1       = 0;
//        int         isOk2       = 1;
//        int         isOk3       = 1;

//        int         v_seq       = 0;

//        String v_static_cmuno       = box.getString("p_static_cmuno");
        String v_cmuno              = box.getString("p_cmuno");
//        String v_brd_fg             = box.getString("p_brd_fg");
        String v_menuno             = box.getString("p_menuno");
        String v_brdno              = box.getString("p_brdno");
        Vector savefile             = box.getVector("p_savefile");

//        String s_userid             = box.getSession("userid");
//        String s_name               = box.getSession("name");


        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 
           
           if(this.selectDeleteOk(box) == 0) {
           
	           // ��ۻ���
	           sql = "delete from tz_cmuboardreplay where cmuno = ? and menuno = ? and brdno =?";
	           pstmt1 = connMgr.prepareStatement(sql);
	           pstmt1.setString(1, v_cmuno);
	           pstmt1.setString(2, v_menuno);
	           pstmt1.setString(3, v_brdno);
	           pstmt1.executeUpdate(); //isOk1 = 
	           if ( pstmt1 != null ) { pstmt1.close(); }
	           // ���ϻ�������
	           sql = "delete from tz_cmuboardfile where cmuno = ? and menuno = ? and brdno =?";
	           pstmt2 = connMgr.prepareStatement(sql);
	           pstmt2.setString(1, v_cmuno);
	           pstmt2.setString(2, v_menuno);
	           pstmt2.setString(3, v_brdno);
	           pstmt2.executeUpdate(); //isOk2 = 
	           if ( pstmt2 != null ) { pstmt2.close(); }
	           // ��������
	           sql = "delete from tz_cmuboard where cmuno = ? and menuno = ? and brdno =?";
	           pstmt3 = connMgr.prepareStatement(sql);
	           pstmt3.setString(1, v_cmuno);
	           pstmt3.setString(2, v_menuno);
	           pstmt3.setString(3, v_brdno);
	           isOk1 = pstmt3.executeUpdate(); //isOk3 = 
	           if ( pstmt3 != null ) { pstmt3.close(); }
	//            if ( isOk1 > 0&& isOk2 > 0&& isOk3 > 0 ) { 
	                if ( savefile != null ) { 
	                    FileManager.deleteFile(savefile);         //     ÷������ ����
	                }
	                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
	//            }
           } else {
        	   isOk1 = -1;
           }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql - > " +FormatDate.getDate("yyyyMMdd") + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk1;
    }
    
    /**
     * ������ ���� �亯 ���� üũ
     * @param seq          �Խ��� ��ȣ
     * @return result      0 : �亯 ����,    1 : �亯 ����
     * @throws Exception
     */
     public int selectDeleteOk(RequestBox box) throws Exception { 
          DBConnectionManager	connMgr	= null;
          ListSet             ls      = null;
          String              sql     = "";
          int result1 = 0;

          String v_cmuno              = box.getString("p_cmuno");
          String v_menuno             = box.getString("p_menuno");
          String v_brdno              = box.getString("p_brdno");
          
          try { 
              connMgr = new DBConnectionManager();

              sql  = " select count(*) cnt										\n" 
            	  + "  from														\n"			
            	  + "  (														\n"	
            	  + "  	 select cmuno, menuno, brdno, root, lv, position		\n"
            	  + "  	 from tz_cmuboard										\n"
            	  + "    where  cmuno = " + SQLString.Format(v_cmuno) + " 		\n"
            	  + "  	 and menuno = " + SQLString.Format(v_menuno) + " 		\n"
            	  + "    and brdno = " + SQLString.Format(v_brdno) + " 			\n"
            	  + " ) a, tz_cmuboard b										\n"
            	  + " where b.lv = (a.lv +1)									\n"
            	  + " and b.position = (a.position +1)							\n"
            	  + " and b.root = a.root										\n"
            	  + " and a.cmuno = b.cmuno										\n"	
            	  + " and a.menuno = b.menuno									\n";

              ls = connMgr.executeQuery(sql);
              
              if ( ls.next() ) { 
                  result1 = ls.getInt("cnt");
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
          return result1;
      }

    /**
    * �Խ��� ��ȣ�ޱ�
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */

     public static String printPageList(int totalPage, int currPage, int blockSize) throws Exception { 

        currPage = (currPage == 0) ? 1 : currPage;
        String str= "";
        if ( totalPage > 0 ) { 
            PageList pagelist = new PageList(totalPage,currPage,blockSize);


            str += "<table border='0' width='100%' align='center' > ";
            str += "<tr > ";
            // str += "    <td width='100%' align='center' valign='middle' > ";

            if ( pagelist.previous() ) { 
                str += "<td align='center' valign='middle' > <a href=\"javascript:goPage('" + pagelist.getPreviousStartPage() + "')\" > <img src=\"/images/user/button/pre.gif\" border=\"0\" align=\"middle\" > </a > </td >  ";
            } else { 
                str += "<td align='center' valign='middle' > <img src=\"/images/user/button/pre.gif\" border=\"0\" align=\"middle\" > </td > ";
            }


            for ( int i=pagelist.getStartPage(); i <= pagelist.getEndPage(); i++ ) { 
                if ( i == currPage) { 
                    str += "<td align='center' valign='middle' > <strong > " + i + "</strong > " + "</td > ";
                } else { 
                    str += "<td align='center' valign='middle' > <a href=\"javascript:goPage('" + i + "')\" > " + i + "</a > </td > ";
                }
            }

            if ( pagelist.next() ) { 
                str += "<td align='center' valign='middle' > <a href=\"javascript:goPage('" + pagelist.getNextStartPage() + "')\" > <img src=\"/images/user/button/next.gif\"  border=\"0\" align=\"middle\" > </a > </td > ";
            } else { 
                str += "<td align='center' valign='middle' > <img src=\"/images/user/button/next.gif\" border=\"0\" align=\"middle\" > </td > ";
            }

           /* if ( str.equals("") ) { 
                str += "<�ڷᰡ �����ϴ�.";
            }
            */
           // str += "    </td > ";
           // str += "    <td width='15%' align='center' > ";



           // str += "    </td > ";
            str += "</tr > ";
            str += "</table > ";
        }
        return str;
    }
}