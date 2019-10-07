// **********************************************************
//  1. ��      ��: �����������ø� ����
//  2. ���α׷��� : NoticeTempletBean.java
//  3. ��      ��: �����������ø� ����
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0��������
//  6. ��      ��:  2005. 7.  14
//  7. ��      ��:
// **********************************************************
package com.ziaan.homepage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class NoticeTempletBean { 
    private ConfigSet config;
    private int row;

    public NoticeTempletBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.manage.row") );  // �� ����� �������� row ���� �����Ѵ�
            row = 10; // ������ ����
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }


    /**
    * �������� ���ø� ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   �����������ø� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectListNoticeTemplet(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox dbox        = null;

        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");
        int v_pageno        = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select seq, addate, adtitle, adname, templetfile, cnt, luserid, ldate from TZ_NOTICE_TEMPLET ";

            if ( !v_searchtext.equals("") ) {      //    �˻�� ������
                if ( v_search.equals("adtitle") ) {                          //    �������� �˻��Ҷ�
                    sql += " where adtitle like " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("name") ) {                //    �������� �˻��Ҷ�
                    sql += " where name like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
            }

            sql += " order by seq desc ";

            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                       // �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);               // ������������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();  // ��ü ������ ���� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();  // ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(row));

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
    * �������� ���ø� �󼼺���
    * @param box          receive from the form object and session
    * @return ArrayList   ��ȸ�� ������
    * @throws Exception
    */
   public DataBox selectViewNoticeTemplet(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox dbox        = null;

        String v_seq = box.getString("p_seq");

        String v_filepath     = config.getProperty("dir.namo.template");
        String v_templetfile  = "";
        String v_contents     = "";
        try { 
            connMgr = new DBConnectionManager();

            sql += "  select seq, addate, adtitle, adname, templetfile, cnt, luserid, ldate from TZ_NOTICE_TEMPLET  ";
            sql += "  where seq    = " + StringManager.makeSQL(v_seq);

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox = ls.getDataBox();

                v_templetfile = dbox.getString("d_templetfile");
                v_contents = read(v_filepath + v_templetfile);
                dbox.put("d_contents",v_contents);
            }
            // ��ȸ�� ����
            connMgr.executeUpdate("update TZ_NOTICE_TEMPLET set cnt = cnt + 1 where seq = " + v_seq);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }


    /**
    * �������� ���ø� ����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertNoticeTemplet(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String sql  = "";
        String sql1 = "";
        int isOk  = 0;
        int v_seq = 0;

        String v_adtitle   = box.getString("p_adtitle");
        String v_contents  = box.getString("p_contents");
        String s_userid   = box.getSession("userid");
        String s_name     = box.getSession("name");


        String v_templetfile = "";

        try { 
           connMgr = new DBConnectionManager();

           sql  = "select max(seq) from TZ_NOTICE_TEMPLET  ";
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) { 
               v_seq = ls.getInt(1) + 1;
           } else { 
               v_seq = 1;
           }


            /*********************************************************************************************/
            // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ���ε�)
/*           
            SmeNamoMime namo = new SmeNamoMime(v_contents); // ��ü���� 
            boolean result = namo.parse(); // ���� �Ľ� ���� 
            if ( !result ) { // �Ľ� ���н� 
                System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
                return 0;
            }
            if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ� 
                String v_server = config.getProperty("autoever.url.value");
                String fPath = config.getProperty("dir.namo");   // ���� ���� ��� ����
                String refUrl = config.getProperty("url.namo");  // ������ ����� ������ �����ϱ� ���� ���
                String prefix = "notice" + v_seq;                // ���ϸ� ���ξ�
                result = namo.saveFile(fPath, v_server +refUrl, prefix);   // ���� ���� ���� 
            }
            if ( !result ) { // �������� ���н� 
                System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
                return 0;
            }
            v_contents = namo.getContent(); // ���� ����Ʈ ���
*/            
            /*********************************************************************************************/

           // HTML ���ø� ���� ����
            String v_filepath = config.getProperty("dir.namo.template");
            long v_time = System.currentTimeMillis();
            v_templetfile = "namo_" + v_time + ".html";
            write(v_filepath + v_templetfile , v_contents);

           sql1 =  "insert into TZ_NOTICE_TEMPLET(seq, addate, adtitle, adname, templetfile, cnt, luserid, ldate )        ";
           sql1 += "     values (?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
           pstmt = connMgr.prepareStatement(sql1);

           pstmt.setInt   (1, v_seq);
           pstmt.setString(2, v_adtitle);
           pstmt.setString(3, s_name);
           pstmt.setString(4, v_templetfile);
           pstmt.setInt   (5, 0);
           pstmt.setString(6, s_userid);

           isOk = pstmt.executeUpdate();

           if ( isOk > 0 ) { 
               // templete_list.ini ���� ���� �ۼ�
               makeTemplateList();
           }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql - > " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    * �������� ���ø� �����Ͽ� �����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
     public int updateNoticeTemplet(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        int v_seq         = box.getInt("p_seq");
        String v_adtitle  = box.getString("p_adtitle");
        String v_contents = box.getString("p_contents");
        String v_templetfile = box.getString("p_templetfile");;

        String s_userid   = box.getSession("userid");
        String s_name     = box.getSession("name");

        try { 

            /*********************************************************************************************/
            // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ���ε�)
/*            
            SmeNamoMime namo = new SmeNamoMime(v_contents); // ��ü���� 
            boolean result = namo.parse(); // ���� �Ľ� ���� 
            if ( !result ) { // �Ľ� ���н� 
                System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
                return 0;
            }
            if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ� 
                String v_server = config.getProperty("autoever.url.value");
                String fPath = config.getProperty("dir.namo");   // ���� ���� ��� ����
                String refUrl = config.getProperty("url.namo");  // ������ ����� ������ �����ϱ� ���� ���
                String prefix = "notice" + v_seq;                // ���ϸ� ���ξ�
                result = namo.saveFile(fPath, v_server +refUrl, prefix);   // ���� ���� ���� 
            }
            if ( !result ) { // �������� ���н� 
                System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
                return 0;
            }
            v_contents = namo.getContent(); // ���� ����Ʈ ���
*/            
            /*********************************************************************************************/

           // HTML ���ø� ���� ����
            String v_filepath = config.getProperty("dir.namo.template");
            write(v_filepath + v_templetfile , v_contents);

            connMgr = new DBConnectionManager();

            sql  = " update TZ_NOTICE_TEMPLET set adtitle = ? ,     adname = ? ,       ";
            sql += "                              templetfile = ? , luserid = ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
            sql += " where  seq = ?                                                                    ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_adtitle);
            pstmt.setString(2, s_name);
            pstmt.setString(3, v_templetfile);
            pstmt.setString(4, s_userid);
            pstmt.setInt   (5, v_seq);

            isOk = pstmt.executeUpdate();

           if ( isOk > 0 ) { 
               // templete_list.ini ���� ���� �ۼ�
               makeTemplateList();
           }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    * �������� ���ø� �����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteNoticeTemplet(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        int v_seq  = box.getInt("p_seq");
        String v_templetfile = box.getString("d_templetfile");
        String v_filepath = config.getProperty("dir.namo.template");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " delete from TZ_NOTICE_TEMPLET   ";
            sql += "   where seq = ?  ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setInt(1, v_seq);
            isOk = pstmt.executeUpdate();
            if ( isOk > 0 ) { 
                // template ���� ����
                delete(v_filepath + v_templetfile);
                // template_list.ini ���� ���� �ۼ�
                makeTemplateList();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql + "\r\n");
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    * �������� ���ø� ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   �����������ø� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectAllNoticeTemplet() throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox dbox        = null;

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select seq, addate, adtitle, adname, templetfile, cnt, luserid, ldate from TZ_NOTICE_TEMPLET ";
            sql += " order by seq desc ";
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
    * ���ø� ini ���� �ۼ�
    * @throws Exception
    */
    public void makeTemplateList() throws Exception { 
        ArrayList           list    = null;
        String v_templet_list    = "";
        String v_tem_title       = "";
        String v_tem_templetfile = "";
        String v_filepath        = config.getProperty("dir.namo.template");
        String v_server          = config.getProperty("autoever.url.value");
        String v_path            = config.getProperty("url.namo.template");
        String v_filename        = config.getProperty("name.namo.template.ini");


        try { 
           v_templet_list = "[���ø� ���]\n";
           // ���ø� ����Ʈ
           list = selectAllNoticeTemplet();

           if ( list != null ) { 
             for ( int i = 0; i < list.size(); i++ ) { 
               DataBox dbox   = (DataBox)list.get(i);
               v_tem_title       = dbox.getString("d_adtitle");
               v_tem_templetfile = dbox.getString("d_templetfile");
               v_templet_list = v_templet_list + v_tem_title + "=" +v_server +v_path + v_tem_templetfile + "\n";
             }
           }
           write(v_filepath + v_filename , v_templet_list);

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
    }

    /**
    * �����ۼ�
    * @param  path       ����  �н� +�̸�
    * @param  contents   ������ ����
    * @throws Exception
    */
    public void write(String path, String contents) throws Exception { 
        File file = null;

        try { 
           file = new File(path);
           FileWriter fw = new FileWriter(file);
           BufferedWriter owriter = new BufferedWriter( fw );
           owriter.write(contents);
           owriter.flush();
           owriter.close();
           fw.close();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
    }

    /**
     * ���� ������ �����Ѵ�
     * @param path        ������ ������ �н� +���ϸ�
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
     * ������ �о ������ �����Ѵ�.
     * @param sPath          ������ �н� +���ϸ�
     * @return result         ���� ������ ��� �ִ� ��Ʈ�� ��ü
     * @throws Exception
     */
    public String read(String path) throws Exception
    { 
        String result = "";
        try { 
            FileReader fr = new FileReader(path);
            BufferedReader reader = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            int len = 4096; // 4k
            char[] buff = new char[len];
            while ( true)
            { 
                int rsize = reader.read(buff, 0, len);
                if ( rsize < 0)
                { 
                    break;
                }
                sb.append(buff, 0, rsize);
            }
            buff = null;
            result = sb.toString();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }

        return result;
    }

}

