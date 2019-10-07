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
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class CommunityAdminDirectBean { 
    private ConfigSet config;
    private static int row=10;
//    private String v_type = "PQ";
    private static final String FILE_TYPE = "p_file";           //      ���Ͼ��ε�Ǵ� tag name
    private static final int FILE_LIMIT = 1;                    //    �������� ���õ� ����÷�� ����


    public CommunityAdminDirectBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );  // �� ����� �������� row ���� �����Ѵ�
            row = 10; // ������ ����
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
    
    public CommunityAdminDirectBean(String type) { 
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
    * �������� ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   �������� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectDirectList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
//        String              sql1    = "";
        DataBox             dbox    = null;

        String v_searchtext = box.getString("p_searchtext");
        String v_select     = box.getString("p_select");
        String v_faq_type   = box.getStringDefault("p_faq_type", "DIRECT");
        int v_pageno        = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();
//            sql  = "\n select a.*,rownum rowseq from ("
              sql = "\n select a.faq_type faq_type,a.faqno faqno,a.title title,a.content content,a.read_cnt read_cnt"
                 + "\n       ,a.add_cnt add_cnt,a.parent parent,a.lv lv,a.position position"
                 + "\n       ,a.register_userid register_userid,a.register_dte register_dte"
                 + "\n       ,a.modifier_userid modifier_userid,a.modifier_dte modifier_dte"
                 + "\n       ,b.userid userid, fn_crypt('2', b.birth_date, 'knise') birth_date,b.name name,b.email email"//,b.deptnam deptnam,b.jikupnm jikupnm,b.jikwinm jikwinm"
                 + "\n      ,nvl(c.cnt,0) cnt "
                 + "\n   from tz_cmufaq a,tz_member b "
                 + "\n       ,(select faq_type faq_type,faqno faqno, count(*) cnt from tz_cmufaqfile where faq_type = '" +v_faq_type + "' group by faq_type,faqno) c"
                 + "\n  where a.register_userid = b.userid "  
                 + "\n    and a.faq_type        = c.faq_type(+)" 
                 + "\n    and a.faqno           = c.faqno(+)"
                 + "\n    and a.faq_type        = '" +v_faq_type + "'"
                 ;
            
            if ( !v_searchtext.equals("") ) {      // �˻�� ������
                 if ( v_select.equals("title"))   sql += "\n and lower(a.title) like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 if ( v_select.equals("content")) sql += "\n and a.content like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i ��
                 if ( v_select.equals("name"))    sql += "\n and lower(b.name) like lower (" +  StringManager.makeSQL("%" + v_searchtext + "%") + ")";            //   Oracle 9i ��
            }

            sql += "\n order by a.root desc,a.position asc";
//                 + "\n ) a";
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
    * �������� ��ȸ
    * @param box          receive from the form object and session
    * @return ArrayList   �������� ��ȸ
    * @throws Exception
    */
    public ArrayList selectViewQna(RequestBox box,String qryFlag) throws Exception { 
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
//        int                 isOK    =1;

        String v_faq_type   = box.getStringDefault("p_faq_type", "CMUQNA");
        int    v_faqno        = box.getInt("p_faqno");
        int    v_rowseq        = box.getInt("p_rowseq");
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // ��ȸ�� ����.
            if ( "VIEW".equals(qryFlag)) { 
                sql  =" update tz_cmufaq set read_cnt =read_cnt +1"
                    + "  where faq_type        = '" +v_faq_type + "'"
                    + "    and faqno           = " +v_faqno
                    ;
                pstmt = connMgr.prepareStatement(sql);
                pstmt.executeUpdate(); //isOK = 
                if ( pstmt != null ) { pstmt.close(); }
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            }  

            // ���������б�
            sql1  = "\n select a.faq_type faq_type,a.faqno faqno,a.title title,a.content content,a.read_cnt read_cnt	\n"
                 + "\n       ,a.add_cnt add_cnt,a.parent parent,a.lv lv,a.position position,a.root root \n"
                 + "\n       ,a.register_userid register_userid,a.register_dte register_dte \n"
                 + "\n       ,a.modifier_userid modifier_userid,a.modifier_dte modifier_dte \n"
                 + "\n       ,b.userid userid,fn_crypt('2', b.birth_date, 'knise') birth_date,b.name name,b.email email \n"//,b.deptnam deptnam,b.jikupnm jikupnm,b.jikwinm jikwinm"
                 + "\n        ,nvl(c.cnt,0) cnt \n"
                 + "\n   from tz_cmufaq a,tz_member b \n"
                 + "\n        ,(select faq_type faq_type,faqno faqno, count(*) cnt from tz_cmufaqfile where faq_type = '" +v_faq_type + "' group by faq_type,faqno) c \n"
                 + "\n  where a.register_userid = b.userid  \n"  
                 + "\n    and a.faq_type        = c.faq_type(+) \n" 
                 + "\n    and a.faqno           = c.faqno(+) \n"
                 + "\n    and a.faq_type        = '" +v_faq_type + "' \n"
                 ;

            sql = sql1 + "    and a.faqno          = " +v_faqno;
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list1.add(dbox);
            }

            // ÷�������б�
            sql  = " select faq_type,faqno,fileno,realfile,savepath,savefile,filesize"
                 + "       ,register_userid,register_dte,modifier_userid,modifier_dte"
                 + "   from tz_cmufaqfile "
                 + "  where faq_type        = '" +v_faq_type + "'"
                 + "    and faqno          = " +v_faqno
                 ;
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list2.add(dbox);
            }


            int listRowCnt=0;
            // �������б�
            sql  = "  select a.* from(select a.*,1 rowseq from (" +sql1 //rownum
                  + "    ) a)a" //order by a.root desc,a.position asc
                  + "  where rowseq =" +(v_rowseq +1);
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                listRowCnt++;
                dbox = ls.getDataBox();
                list3.add(dbox);
            }

            // �������б�
            listRowCnt=0; 
            sql  = "  select a.* from(select a.*,1 rowseq from (" +sql1 //rownum
                  + "    ) a)a" //order by a.root desc,a.position asc
                  + "  where rowseq =" +(v_rowseq-1);

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                listRowCnt++;
                dbox = ls.getDataBox();
                list4.add(dbox);
            }


            // ����б�
            sql  = " select a.faq_type faq_type,a.faqno faqno,a.rplno rplno,a.content content,a.userid userid"
                 + "       ,a.register_dte register_dte,a.modifier_dte modifier_dte"
                 + "       ,fn_crypt('2', b.birth_date, 'knise') birth_date,b.name name,b.email email"//,b.deptnam deptnam,b.jikupnm jikupnm,b.jikwinm jikwinm"
                 + "   from tz_cmufaqreplay a,tz_member b "
                 + "  where a.userid = b.userid "  
                 + "    and a.faq_type        = '" +v_faq_type + "'"
                 + "    and a.faqno          = " +v_faqno
                 + "  order by a.rplno desc" 
                 ;
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list5.add(dbox);
            }

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
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }

    /**
    * �������׵���ϱ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertQnA(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";

        int         isOk1       = 1;
        int         isOk2       = 1;
//        int         v_seq       = 0;
        int         v_faqno     = 0; 


        String v_faq_type  = box.getString("p_faq_type");
        //String v_content   = StringManager.replace(box.getString("content"),"<br > ","\n");
        String v_content = box.getString("p_content"); // ���� clob
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");
		
//        String thisYear= "";
//        String v_templetfile = "";
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           // �������� faqno��ȣ�� �����Ѵ�.
           sql = "select nvl(max(faqno), 0) from tz_cmufaq where faq_type = '" +v_faq_type + "'";
           ls = connMgr.executeQuery(sql);
           while ( ls.next() ) { 
               v_faqno = ls.getInt(1) + 1;
           } 
           int tmp =0;

           sql = "select nvl(max(position), 0) from tz_cmufaq where faq_type = '" +v_faq_type + "'";
           ls = connMgr.executeQuery(sql);
           while ( ls.next() ) { 
               tmp = ls.getInt(1) + 1;
           } 

           sql  =" insert into tz_cmufaq ( faq_type     , faqno          , title       , content   , read_cnt"
                + "                       , add_cnt      , lv          , position  , register_userid"
                + "                       , register_dte , modifier_userid, modifier_dte, del_fg,parent,root)"
                + "               values  (?,?,?,?,?"
                + "                       ,?,?,?,?"
                + "                       ,to_char(sysdate,'YYYYMMDDHH24MISS'),?"
                + "                       ,to_char(sysdate,'YYYYMMDDHH24MISS'),'N',?,?)"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1, v_faq_type                         );// faq�з�
           pstmt.setInt   (2, v_faqno                            );// �Ϸù�ȣ
           pstmt.setString(3, box.getString("p_title" )          );// ����
           pstmt.setString(4, v_content);
           pstmt.setInt   (5, 0                                  );// ��ȸ��
           pstmt.setInt   (6, 0                                  );// ��õ��
           pstmt.setInt   (7, 1         );// �亯����
           pstmt.setInt   (8, tmp         );// �亯��ġ
           pstmt.setString(9, s_userid);// �Խ���
           pstmt.setString(10, s_userid );// ������
           pstmt.setInt   (11, v_faqno                             );// �θ�
           pstmt.setInt   (12, v_faqno                             );// �θ�
           isOk1 = pstmt.executeUpdate();
           
           if ( pstmt != null ) { pstmt.close(); }

//           String sql1 = "select content from tz_cmufaq where faq_type = '" + v_faq_type + "' and faqno =" +v_faqno;
//           
//           connMgr.setOracleCLOB(sql1, v_content);



            isOk2 = this.insertUpFile(connMgr,v_faq_type, v_faqno, box);

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
    * Q&A�亯����ϱ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int replyQnA(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";

        int         isOk1       = 1;
        int         isOk2       = 1;
        int         isOk3       = 1;
//        int         v_seq       = 0;
        int         v_thisfaqno =0;


        String v_faq_type  = box.getString("p_faq_type");
        int    v_faqno     = box.getInt("p_faqno");
        int    v_lv        = box.getInt("p_lv");
        int    v_position  = box.getInt("p_position");
//        int    v_parent  = box.getInt("p_parent");
        int    v_root  = box.getInt("p_root");
        String v_content   = StringManager.replace(box.getString("content"),"<br > ","\n");
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

//        String thisYear= "";
//        String v_templetfile = "";
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 



           sql  =" update tz_cmufaq set    position = position +1"
                + "  where faq_type        = ?"
//              + "     and parent          = ?"
                + "     and position       > ?"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1, v_faq_type                         );// faq�з�
//           pstmt.setInt   (2, v_parent                            );// �θ�
           pstmt.setInt   (2, v_position                            );// �亯��ġ
           pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }

           // qna faqno��ȣ�� �����Ѵ�.
           sql = "select nvl(max(faqno), 0) from tz_cmufaq where faq_type = '" +v_faq_type + "'";
           ls = connMgr.executeQuery(sql);
           while ( ls.next() ) { 
               v_thisfaqno = ls.getInt(1) + 1;
           } 
//           int tmp =0;
           sql = "select nvl(max(position), 0) from tz_cmufaq where faq_type = '" +v_faq_type + "' and parent= '" +v_faqno + "' and lv=" +(v_lv +1);
           ls = connMgr.executeQuery(sql);
           while ( ls.next() ) { 
//               tmp = ls.getInt(1) + 1;
           } 
           sql  =" insert into tz_cmufaq ( faq_type     , faqno          , title       , content   , read_cnt"
                + "                       , add_cnt      , lv          , position  , register_userid"
                + "                       , register_dte , modifier_userid, modifier_dte, del_fg,parent,root)"
                + "               values  (?,?,?,?,?"
                + "                       ,?,?,?,?"
                + "                       ,to_char(sysdate,'YYYYMMDDHH24MISS'),?"
                + "                       ,to_char(sysdate,'YYYYMMDDHH24MISS'),'N',?,?)"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1, v_faq_type                         );// faq�з�
           pstmt.setInt   (2, v_thisfaqno                            );// �Ϸù�ȣ
           pstmt.setString(3, box.getString("p_title" )          );// ����
           pstmt.setString(4, v_content);
           pstmt.setInt   (5, 0                                  );// ��ȸ��
           pstmt.setInt   (6, 0                                 );// ��õ��
           pstmt.setInt   (7, v_lv +1         );// �亯����
//           pstmt.setInt   (7, tmp         );// �亯��ġ
           pstmt.setInt   (8, v_position +1         );// �亯��ġ

           pstmt.setString(9, s_userid);// �Խ���
           pstmt.setString(10, s_userid );// ������
           pstmt.setInt   (11, v_faqno                            );// �θ�
           pstmt.setInt   (12, v_root                            );// �θ�
           isOk2 = pstmt.executeUpdate();
           
           if ( pstmt != null ) { pstmt.close(); }

//           String sql1 = "select content from tz_cmufaq where faq_type = '" + v_faq_type + "' and faqno =" +v_faqno;
//           connMgr.setOracleCLOB(sql1, v_content);

            isOk3 = this.insertUpFile(connMgr,v_faq_type, v_thisfaqno, box);

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
    public int updateQnA(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";

        int         isOk1       = 1;
        int         isOk2       = 1;
//        int         v_seq       = 0;



        String v_faq_type  = box.getString("p_faq_type");
        int    v_faqno     = box.getInt("p_faqno");
        //String v_content   = StringManager.replace(box.getString("content"),"<br > ","\n");
        String v_content   = box.getString("p_content");
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

//        String thisYear= "";
//        String v_templetfile = "";
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ���ε�)
/*
           ConfigSet conf = new ConfigSet();
           SmeNamoMime namo = new SmeNamoMime(v_content); // ��ü���� 
           boolean result = namo.parse(); // ���� �Ľ� ���� 
           
           if ( !result ) { // �Ľ� ���н� 
               return 0;
           }
           if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ� 
               String v_server = conf.getProperty("autoever.url.value");
               String fPath = conf.getProperty("dir.namo");   // ���� ���� ��� ����
               String refUrl = conf.getProperty("url.namo");; // ������ ����� ������ �����ϱ� ���� ���
               String prefix = "Community" + v_faqno;         // ���ϸ� ���ξ�
               result = namo.saveFile(fPath, v_server +refUrl, prefix); // ���� ���� ���� 
           }
           if ( !result ) { // �������� ���н� 
               return 0;
           }
           v_content = namo.getContent(); // ���� ����Ʈ ���
*/           


           sql  =" update tz_cmufaq set    title   = ?"      
                + "                       , content = ?"
                + "                       , modifier_userid = ?"
                + "                       , modifier_dte =to_char(sysdate,'YYYYMMDDHH24MISS')"
                + "  where faq_type        = ?"
                + "    and faqno           = ?"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1, box.getString("p_title" )          );// ����
           pstmt.setString(2, v_content);
           pstmt.setString(3, s_userid);// �Խ���
           pstmt.setString(4, v_faq_type                         );// faq�з�
           pstmt.setInt   (5, v_faqno                            );// �Ϸù�ȣ

           isOk1 = pstmt.executeUpdate();
           
           if ( pstmt != null ) { pstmt.close(); }

//           String sql1 = "select content from tz_cmufaq where faq_type = '" + v_faq_type + "' and faqno =" +v_faqno;
//           connMgr.setOracleCLOB(sql1, v_content);



           isOk2 = this.insertUpFile(connMgr,v_faq_type, v_faqno, box);

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

     public int insertUpFile(DBConnectionManager connMgr, String faq_type, int faqno, RequestBox    box) throws Exception { 
        ListSet           ls      = null;
        PreparedStatement pstmt   = null;
        String            sql     = "";
        String            sql2    = "";
        int               isOk2   = 1;
        int               vfileno = 0;
        // ----------------------   ���ε�Ǵ� ������ ������ �˰� �ڵ��ؾ��Ѵ�  --------------------------------
        String [] v_realFile     = new String [FILE_LIMIT];
        String [] v_saveFile     = new String [FILE_LIMIT];

        for ( int i = 0; i < FILE_LIMIT; i++ ) { 
            v_realFile[i] = box.getRealFileName(FILE_TYPE + (i +1));
            v_saveFile[i] = box.getNewFileName (FILE_TYPE + (i +1));
        }
        String s_userid = box.getSession("userid");

        try { 

             sql  =" insert into tz_cmufaqfile ( faq_type, faqno, fileno, realfile, savepath"
                  + "                       , savefile, filesize, register_userid, register_dte, modifier_userid, modifier_dte)"
                  + "               values  (?,?,?,?,''"
                  + "                       ,?,null,?"
                  + "                       ,to_char(sysdate,'YYYYMMDDHH24MISS'),?"
                  + "                       ,to_char(sysdate,'YYYYMMDDHH24MISS'))"
                  ;
             pstmt = connMgr.prepareStatement(sql);
             for ( int    i = 0; i < FILE_LIMIT; i++ ) { 
                 if ( !v_realFile    [i].equals("")&& "".equals(box.getString("p_fileno" +(i +1))) )   { 
                     // ���Ϲ�ȣ����ȣ�� �����Ѵ�.
                     sql2 = "select nvl(max(fileno), 0) from tz_cmufaqfile where faq_type = '" +faq_type + "' and faqno =" +faqno;
                     ls = connMgr.executeQuery(sql2);
                     while ( ls.next() ) { 
                         vfileno = ls.getInt(1) + 1;
                     } 
                     ls.close();
                     pstmt.setString(1, faq_type       );// faq�з�
                     pstmt.setInt   (2, faqno          );// �Ϸù�ȣ
                     pstmt.setInt   (3, vfileno        );// �����Ϸù�ȣ
                     pstmt.setString(4, v_realFile[i]  );// �������ϸ�
                     pstmt.setString(5, v_saveFile[i]  );// �������ϸ�
                     pstmt.setString(6, s_userid       );// �Խ���
                     pstmt.setString(7, s_userid       );// ������
                     isOk2 = pstmt.executeUpdate();
                 }
              } 
             
             if ( pstmt != null ) { pstmt.close(); }
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
//        String            sql2    = "";
        int               isOk2   = 1;


//        String s_userid = box.getSession("userid");
        String v_faq_type  = box.getString("p_faq_type");
        int    v_faqno     = box.getInt("p_faqno");
        int    v_fileno    = box.getInt("p_delfileno");
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

             sql  =" delete from  tz_cmufaqfile "
                 + "  where faq_type        = ?"
                 + "    and faqno           = ?"
                 + "    and fileno          = ?"
              ;
             pstmt = connMgr.prepareStatement(sql);
             pstmt.setString(1, v_faq_type       );// faq�з�
             pstmt.setInt   (2, v_faqno          );// �Ϸù�ȣ
             pstmt.setInt   (3, v_fileno        );// �����Ϸù�ȣ
             isOk2 = pstmt.executeUpdate();
             
             if ( pstmt != null ) { pstmt.close(); }
             
             if ( isOk2 > 0 ) { 
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
    * Q&A ��۵���ϱ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertQnAMemo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";

        int         isOk1       = 1;
        int         isOk2       = 1;
//        int         v_seq       = 0;
        int         v_rplno     = 0; 


        String v_faq_type  = box.getString("p_faq_type");
        int    v_faqno     = box.getInt("p_faqno"); 
        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

//        String thisYear= "";
//        String v_templetfile = "";
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           // qna faqno��ȣ�� �����Ѵ�.
           sql = "select nvl(max(rplno), 0) from tz_cmufaqreplay where faq_type = '" +v_faq_type + "' and faqno = " +v_faqno;
           ls = connMgr.executeQuery(sql);
           while ( ls.next() ) { 
               v_rplno = ls.getInt(1) + 1;
           } 
           sql  =" insert into tz_cmufaqreplay ( faq_type     , faqno          , rplno       , content   , userid"
                + "                       , register_dte , modifier_dte, del_fg)"
                + "               values  (?,?,?,?,?"
                + "                       ,to_char(sysdate,'YYYYMMDDHH24MISS')"
                + "                       ,to_char(sysdate,'YYYYMMDDHH24MISS'),'N')"
                ;
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1, v_faq_type                         );// faq�з�
           pstmt.setInt   (2, v_faqno                            );// �Ϸù�ȣ
           pstmt.setInt   (3, v_rplno                            );// ��۹�ȣ
           pstmt.setString(4, box.getString("p_rep_content" )          );// ����
           pstmt.setString(5, s_userid);// �Խ���
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
    * Q&A ��ۻ����ϱ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int deleteQnAMemo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt = null;
        ListSet     ls          = null;
        String      sql         = "";

        int         isOk1       = 1;
        int         isOk2       = 1;
//        int         v_seq       = 0;


        String v_faq_type  = box.getString("p_faq_type");
        int    v_faqno     = box.getInt("p_faqno"); 
        int    v_rplno     = box.getInt("p_rplno"); 
//        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

//        String thisYear= "";
//        String v_templetfile = "";
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           sql = "delete from tz_cmufaqreplay where faq_type = '" +v_faq_type + "' and faqno = " +v_faqno + " and rplno=" +v_rplno;
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
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk1*isOk2;
    }


    /**
    * Q&A �ۻ����ϱ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int deleteQnAData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt1 = null;
        PreparedStatement   pstmt2 = null;
        PreparedStatement   pstmt3 = null;
        ListSet     ls          = null;
        String      sql         = "";

//        int         isOk1       = 1;
//        int         isOk2       = 1;
//        int         isOk3       = 1;

//        int         v_seq       = 0;


        String v_faq_type  = box.getString("p_faq_type");
        int    v_faqno     = box.getInt("p_faqno"); 
        Vector savefile    = box.getVector("p_savefile");

//        String s_userid    = box.getSession("userid");
//        String s_name      = box.getSession("name");

//        String thisYear= "";
//        String v_templetfile = "";
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false); 

           // ��ۻ���
           sql = "delete from tz_cmufaqreplay where faq_type = ? and faqno = ?";
           pstmt1 = connMgr.prepareStatement(sql);
           pstmt1.setString(1, v_faq_type);
           pstmt1.setInt   (2, v_faqno);
           pstmt1.executeUpdate(); //isOk1 = 
           
           if ( pstmt1 != null ) { pstmt1.close(); }

           // ���ϻ�������
           sql = "delete from tz_cmufaqfile where faq_type = ? and faqno = ?";
           pstmt2 = connMgr.prepareStatement(sql);
           pstmt2.setString(1, v_faq_type);
           pstmt2.setInt   (2, v_faqno);
           pstmt2.executeUpdate(); //isOk2 = 
           
           if ( pstmt2 != null ) { pstmt2.close(); }
 
           // ��������
           sql = "delete from tz_cmufaq where faq_type = ? and faqno = ?";
           pstmt3 = connMgr.prepareStatement(sql);
           pstmt3.setString(1, v_faq_type);
           pstmt3.setInt   (2, v_faqno);
           pstmt3.executeUpdate(); //isOk3 = 
           
           if ( pstmt3 != null ) { pstmt3.close(); }

//            if ( isOk1 > 0&& isOk2 > 0&& isOk3 > 0 ) { 
                if ( savefile != null ) { 
                    FileManager.deleteFile(savefile);         //     ÷������ ����
                }
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }


//            }
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
        return 1;
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