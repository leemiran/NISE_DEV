// **********************************************************
//  1. ��      ��: ������ Bean
//  2. ���α׷���: ResBean.java
//  3. ��      ��: ������
//  4. ȯ      ��: JDK 1.4
//  5. ��      ��: 1.0
//  6. ��      ��: ������
//  7. ��      ��: 
// **********************************************************

package com.ziaan.complete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import oracle.sql.CLOB;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class SuRoyBean { 
    private static final String FILE_TYPE = "p_file";           //      ���Ͼ��ε�Ǵ� tag name
    private static final int FILE_LIMIT = 1;                    //    �������� ���õ� ����÷�� ����
    private ConfigSet config;
    private int row;

    public SuRoyBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        �� ����� �������� row ���� �����Ѵ�
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    * �ڷ�� ����Ʈȭ�� select
    * @param    box          receive from the form object and session
    * @return ArrayList  �ڷ�� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectDocList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_pageno = box.getInt("p_pageno");
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");
        String v_sel1	    = box.getString("p_sel1");
        String v_sel2	    = box.getString("p_sel2");
        
        String v_sdate1     = box.getString("p_sdate1").replaceAll("-","");//��û ������
        String v_edate1     = box.getString("p_edate1").replaceAll("-","");//��û
        
        String v_sdate2     = box.getString("p_sdate2").replaceAll("-","");//�߱� ������
        String v_edate2     = box.getString("p_edate2").replaceAll("-","");//�߱�
        
        String v_tabseq     = box.getString("p_tabseq");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " 	select tct.subj, tct.year, tct.subjseq, tct.userid, tct.seq, 			";
            sql += "		tct.username, tct.useradd, tct.useremail, tct.usertel, tct.ea, 		";
            sql += "		tct.appdate, tct.issend, tct.senddate, tct.ison, tct.sname, 		";
            sql += "		tct.suserid, 								";
            sql += "		tsj.edustart, tsj.eduend, tsj.subjnm,					";
            sql += "		tsd.serno								";
            sql += "	from tz_certapp tct, tz_subjseq tsj, tz_stold tsd				";
            sql += "	where tct.subj = tsj.subj							";
            sql += "		and tct.year = tsj.year							";
            sql += "		and tct.subjseq = tsj.subjseq 						";
            sql += "		and tct.subj = tsd.subj							";
            sql += "		and tct.year = tsd.year 						";
            sql += "		and tct.subjseq = tsd.subjseq 						";
            sql += "		and tct.userid = tsd.userid 						";
            
            //�߱޴���...��û����...���...
            
            //�߱�, ��ó�� ����            
            if ( !v_sel1.equals("")) {
            	if (v_sel1.equals("req")) {
            		sql += " and tct.issend = 'N' ";
            	} else if (v_sel1.equals("send")) {
            		sql += " and tct.issend = 'Y' ";
            	}
            }
            
            //�¶��� �������� ���� ����
            if ( !v_sel2.equals("")) {
            	if (v_sel2.equals("on")) {
            		sql += " and tct.ison = 'Y' ";
            	} else if (v_sel2.equals("off")) {
            		sql += " and tct.ison = 'N' ";
            	}
            }

            //��û�Ⱓ
            if ( !v_sdate1.equals("")) {
            	sql += " and tct.appdate >= " + StringManager.makeSQL(v_sdate1) + " ";            	
            }
            
            if ( !v_edate1.equals("")) {
            	sql += " and tct.appdate <= " + StringManager.makeSQL(v_edate1) + " ";            	
            }            
            
            //�߱ޱⰣ
            if ( !v_sdate2.equals("")) {
            	sql += " and tct.senddate >= " + StringManager.makeSQL(v_sdate2) + " ";            	
            }
            
            if ( !v_edate2.equals("")) {
            	sql += " and tct.senddate <= " + StringManager.makeSQL(v_edate2) + " ";            	
            } 
            
            
            if ( !v_searchtext.equals("") ) {                //    �˻�� ������
                if ( v_search.equals("name") ) {              //    �̸����� �˻��Ҷ�
                    sql += " and tct.username like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
                else if ( v_search.equals("id") ) {        //    ���̵�� �˻��Ҷ�
                    sql += " and tct.userid like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
                else if ( v_search.equals("content") ) {     //    ���������� �˻��Ҷ�
                    sql += " and tsj.subjnm like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }    
            }
            sql += " order by tct.appdate desc 	                                                       ";

            ls = connMgr.executeQuery(sql);

             ls.setPageSize(row);                       //  �������� row ������ �����Ѵ�
             ls.setCurrentPage(v_pageno);               //     ������������ȣ�� �����Ѵ�.
             int totalpagecount = ls.getTotalPage();    //     ��ü ������ ���� ��ȯ�Ѵ�
             int totalrowcount = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                 dbox.put("d_dispnum",        new Integer(totalrowcount - ls.getRowNum() + 1));
                 dbox.put("d_totalpage",      new Integer(totalpagecount));
                 dbox.put("d_rowcount",       new Integer(row));
                list.add(dbox);
            }
            ls.close();
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
   * ���õ� �ڷ�� �Խù� �󼼳��� select
   * @param box          receive from the form object and session
   * @return ArrayList   ��ȸ�� ������
   * @throws Exception
   */
   public DataBox selectBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_seq = box.getInt("p_seq");
        int v_tabseq = box.getInt("p_tabseq");
        int v_upfilecnt = (box.getInt("p_upfilecnt") > 0?box.getInt("p_upfilecnt"):1);

        Vector realfileVector = new Vector();
        Vector savefileVector = new Vector();
        Vector fileseqVector  = new Vector();        

        try { 
            connMgr = new DBConnectionManager();
            
            /*
            sql  = " select a.seq, a.userid, a.name, a.title, '' fileseq, '' realfile, a.content, \n";
            sql += " decode(a.gadmin, 'ZZ', 'ZZ', 'P1', '����',  '���') gadmin, '' savefile,    \n";
            sql += " a.indate, a.cnt, a.refseq, a.levels, a.position, (                         \n";
            sql += "            select decode(count(*), 0, 'Y', 'N')                            \n";
            sql += "            from                                                            \n";
            sql += "            (select refseq, levels, position                                \n";
            sql += "            from TZ_CENTER_BOARD                                            \n";
            sql += "            where tabseq = " + v_tabseq + " and seq = " + v_seq         +  "\n";
            sql += "            ) a, TZ_CENTER_BOARD b                                          \n";
            sql += "            where a.refseq = b.refseq                                       \n";
            sql += "            and b.levels = (a.levels +1)                                    \n";
            sql += "            and b.position = (a.position +1)                                \n";
            sql += " ) delyn                                                                    \n";
            sql += " from TZ_HOMEPAGE_QNA a, TZ_HOMEPAGE_QNAFILE b                              \n";
            sql += "  where a.seq    = b.seq( +)                                                \n";
            sql += "    and a.seq    = " + v_seq;
            */
            
            
            sql  = "	select	tabseq, seq, title, userid, name, 				";
            sql	+= "		content, indate, refseq, levels, position, 			";
            sql += "		upfile, cnt, recomcnt, luserid, ldate, 				";
            sql += "		gubunA, gubunB, isopen, email, hasanswer,			";
            sql += "		realfile, savefile, gadmin, pwd, auserid, 			";
            sql += "		adate, acontent, atitle, aname				 		";
            sql += "	from tz_center_board                                                    ";
            sql += "	where	tabseq	= " + v_tabseq + " and seq = " + v_seq + "              ";            
            
            ls = connMgr.executeQuery(sql);


            for ( int i = 0; ls.next(); i++ ) { 

                dbox = ls.getDataBox();
                /*

                realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
                fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));
                */
            }
            /*
            if ( realfileVector      != null ) dbox.put("d_realfile", realfileVector);
            if ( savefileVector      != null ) dbox.put("d_savefile", savefileVector);
            if ( fileseqVector   != null ) dbox.put("d_fileseq", fileseqVector);
            */

            connMgr.executeUpdate("update TZ_CENTER_BOARD set cnt = cnt + 1 where tabseq = " + v_tabseq + " and seq = " + v_seq);
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
    * ���ο� �ڷ�� ���� ���
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int insertBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;

        ResultSet rs1 = null;
        Statement stmt1 = null;
        PreparedStatement pstmt1  = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int v_seq = 0;
        ListSet ls1         = null;

        int v_tabseq   = box.getInt("p_tabseq");
        String v_title   = box.getString("p_title");
        String v_content = box.getString("p_content"); // ���� clob

        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name"); 
        String s_gadmin = box.getSession("gadmin");

        /*********************************************************************************************/
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // ----------------------   �Խ��� ��ȣ �����´� ----------------------------
            sql = " select nvl(max(seq), 0) from TZ_CENTER_BOARD where tabseq = " + v_tabseq;
            ls1 = connMgr.executeQuery(sql);
            if ( ls1.next() ) { 
                v_seq = ls1.getInt(1) + 1;
            }

            // ----------------------   �Խ��� table �� �Է�  --------------------------
            sql1 =  " insert into TZ_CENTER_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate, gadmin) ";
            sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?)";
//            sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?)";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_tabseq);
            pstmt1.setInt(2, v_seq);
            pstmt1.setString(3, s_userid);
            pstmt1.setString(4, s_usernm);
            pstmt1.setString(5, v_title);
            pstmt1.setString(6, v_content);
            pstmt1.setInt(7, 0);
            pstmt1.setInt(8, v_seq);
            pstmt1.setInt(9, 1);
            pstmt1.setInt(10, 1);
            pstmt1.setString(11, s_userid);
            pstmt1.setString(12, s_gadmin);

            isOk1 = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
            sql2 = "select content from TZ_CENTER_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq;
//            connMgr.setOracleCLOB(sql2, v_content);       //      (��Ÿ ���� ���)       

            // ���Ͼ��ε�
            //isOk2 = this.insertUpFile(connMgr, v_seq, box);
                    
            if ( isOk1 > 0 && isOk2 > 0 ) { 
                connMgr.commit();
            } else { 
                connMgr.rollback();
            }
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
        }

        return isOk1*isOk2;
    }





    /**
    * ���ο� �ڷ�� �亯 ���
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int replyBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ResultSet rs1 = null;
        Statement stmt1 = null;
        Statement stmt2 = null;
        PreparedStatement pstmt1  = null;
        PreparedStatement pstmt2 = null;

        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int isOk3 = 1;
        int v_seq = 0;

        int    v_refseq   = box.getInt("p_refseq");
        int    v_levels   = box.getInt("p_levels");
        int    v_position = box.getInt("p_position");
        String v_title    = box.getString("p_title");
        String v_content  = box.getString("p_content");

        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name");
        String s_gadmin = box.getSession("gadmin");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // ���� �亯�� ��ġ ��ĭ������ ����
            sql1  = "update TZ_HOMEPAGE_QNA ";
            sql1 += "   set position = position + 1 ";
            sql1 += " where refseq   = ? ";
            sql1 += "   and position > ? ";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_refseq);
            pstmt1.setInt(2, v_position);
            isOk1 = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
            stmt1 = connMgr.createStatement();
            // ----------------------   �Խ��� ��ȣ �����´� ----------------------------
            sql2 = "select nvl(max(seq), 0) from TZ_HOMEPAGE_QNA";
            rs1 = stmt1.executeQuery(sql2);
            if ( rs1.next() ) { 
                v_seq = rs1.getInt(1) + 1;
            }
            // ------------------------------------------------------------------------------------

            //// //// //// //// //// //// //// //// /  �Խ��� table �� �Է�  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql1 =  " insert into TZ_HOMEPAGE_QNA(seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate, gadmin) ";
            sql1 += " values (?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?) ";
//            sql1 += " values (?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?) ";

            pstmt2 = connMgr.prepareStatement(sql1);
            pstmt2.setInt(1, v_seq);
            pstmt2.setString(2, s_userid);
            pstmt2.setString(3, s_usernm);
            pstmt2.setString(4, v_title);
            pstmt2.setString(5, v_content);
            //connMgr.setCharacterStream(pstmt2, 5, v_content); //      Oracle 9i or Weblogic 6.1 �� ���
            pstmt2.setInt(6, 0);
            pstmt2.setInt(7, v_refseq);
            pstmt2.setInt(8, v_levels + 1);
            pstmt2.setInt(9, v_position + 1);
            pstmt2.setString(10, s_userid);
            pstmt2.setString(11, s_gadmin);
            isOk2 = pstmt2.executeUpdate();
            if ( pstmt2 != null ) { pstmt2.close(); }
            sql2 = "select content from TZ_HOMEPAGE_QNA where seq = " + v_seq;
//            connMgr.setOracleCLOB(sql2, v_content);       //      (��Ÿ ���� ���)   


            isOk3 = this.insertUpFile(connMgr, v_seq, box);

            if ( isOk2 > 0 && isOk3 > 0 ) { 
                connMgr.commit();
            } else { 
                connMgr.rollback();
            }
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( stmt2 != null ) { try { stmt2.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
        }

        return isOk2*isOk3;
    }



    /**
    * ���õ� �ڷ� �󼼳��� ����
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
     public int updateBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ResultSet rs1 = null;
        Statement stmt1 = null;
        PreparedStatement pstmt1  = null;
        CLOB clob                 = null;
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int isOk3 = 1;

        int    v_tabseq    = box.getInt("p_tabseq");
        int    v_seq       = box.getInt("p_seq");
        int    v_upfilecnt = box.getInt("p_upfilecnt");    //  ������ ������ִ� ���ϼ�
        String v_title     = box.getString("p_title");
        String v_content   = box.getString("p_content");
        String v_atitle     = box.getString("p_atitle");
        String v_acontent   = box.getString("p_acontent");        
 
        Vector v_savefile     = new Vector();
        Vector v_filesequence = new Vector();
        
        String s_usernm = box.getSession("name");
        
        System.out.println("�̰��� ���� " +v_upfilecnt);
        for ( int   i = 0; i < v_upfilecnt; i++ ) { 
            if (    !box.getString("p_fileseq" + i).equals("")) { 
                   System.out.println("���� �����°ž�?");
                v_savefile.addElement(box.getString("p_savefile" + i));         //      ������ ������ִ� ���ϸ� �߿���   ������ ���ϵ�
                v_filesequence.addElement(box.getString("p_fileseq"  + i));      //         ������ ������ִ� ���Ϲ�ȣ  �߿��� ������ ���ϵ�
            }
        }
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sql1 = "update TZ_CENTER_BOARD set title = ?, content=empty_clob(), atitle = ?, acontent=empty_clob() ";//, userid = ?, name = ?, luserid = ?, ldate = to_char(sysdate, 'YYYYMMDDHH24MISS')";
            if (!v_atitle.equals("") || !v_acontent.equals("")) {
            	sql1 += " ,hasanswer ='Y' , adate =to_char(sysdate,  'YYYYMMDDHH24MISS'),  aname= '"+s_usernm+"' ";
            } else if (v_atitle.equals("") && v_acontent.equals("")) {
            	sql1 += " ,hasanswer ='N' , adate ='', aname= '' ";
            }
            sql1 += "  where tabseq = ? and seq = ? ";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_title);
            pstmt1.setString(2, v_atitle);
	    pstmt1.setInt(3, v_tabseq);
            pstmt1.setInt(4, v_seq);            

            isOk1 = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
            // WebLogic 6.1�ΰ��
            sql2 = "select content from TZ_CENTER_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq;
            connMgr.setWeblogicCLOB(sql2, v_content);       //      clob �ʵ忡 ��Ʈ���� �̿�,  data �� �ִ´�(Weblogic �� ���)
            
            sql2 = "select acontent from TZ_CENTER_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq;
            connMgr.setWeblogicCLOB(sql2, v_acontent);       //      clob �ʵ忡 ��Ʈ���� �̿�,  data �� �ִ´�(Weblogic �� ���)            


            isOk2 = this.insertUpFile(connMgr, v_seq, box);       //      ����÷���ߴٸ� ����table��  insert

            System.out.println("���⼭ �ѹ� ");
            System.out.println(v_filesequence);
            isOk3 = this.deleteUpFile(connMgr, box, v_filesequence);        //     ������ ������ �ִٸ� ����table���� ����

            if ( isOk1 > 0 && isOk2 > 0 && isOk3 > 0 ) { 
                connMgr.commit();
                if ( v_savefile != null ) { 
                    FileManager.deleteFile(v_savefile);         //   DB ���� ���ó���� �Ϸ�Ǹ� �ش� ÷������ ����
                }
            } else connMgr.rollback();
        } catch ( Exception ex ) { 
            // conn.rollback();
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( clob != null ) { try { clob.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            // if ( conn != null ) { try { conn.setAutoCommit(true); } catch ( Exception e10 ) { } }
            // if ( conn != null ) { try { bulletinDB.freeConnection("ziaan", conn); } catch ( Exception e10 ) { } }
        }

        return isOk1*isOk2*isOk3;
    }


    /**
    * ���õ� �Խù� ����
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 0;
        int isOk2 = 0;

        int v_seq = box.getInt("p_seq");
        int v_tabseq = box.getInt("p_tabseq");
        int v_upfilecnt = box.getInt("p_upfilecnt");    //  ������ ������ִ� ���ϼ�
        Vector v_savefile  = box.getVector("p_savefile");

        // �亯 ���� üũ(�亯 ������ �����Ұ�)
        if ( this.selectBoard(v_seq) == 0 ) { 

            try { 
                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);

                isOk1 = 1;
                isOk2 = 1;
                sql1 = "delete from TZ_CENTER_BOARD where tabseq = ? and seq = ? ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);
                pstmt1.setInt(2, v_seq);
                isOk1 = pstmt1.executeUpdate();
                if ( pstmt1 != null ) { pstmt1.close(); }
                if ( v_upfilecnt > 0 ) { 
                    sql2 = "delete from TZ_HOMEPAGE_QNAFILE where seq =  ?";
                    pstmt2 = connMgr.prepareStatement(sql2);
                    pstmt2.setInt(1, v_seq);                    
                    isOk2 = pstmt2.executeUpdate();
                    if ( pstmt2 != null ) { pstmt2.close(); }
                }

                if ( isOk1 > 0 && isOk2 > 0 ) { 
                    connMgr.commit();
                    if ( v_savefile != null ) { 
                        FileManager.deleteFile(v_savefile);         //   ÷������ ����
                    }
                } else connMgr.rollback();
            }
            catch ( Exception ex ) { 
                connMgr.rollback();
                ErrorManager.getErrorStackTrace(ex, box,    sql1);
                throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
            }
            finally { 
                if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }  
                if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }

        return isOk1*isOk2;
    }


   /**
   * ������ ���� �亯 ���� üũ
   * @param seq          �Խ��� ��ȣ
   * @return result      0 : �亯 ����,    1 : �亯 ����
   * @throws Exception
   */
   public int selectBoard(int seq) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        int result = 0;

        try { 
            connMgr = new DBConnectionManager();

            sql  = "  select count(*) cnt                         ";
            sql += "  from                                        ";
            sql += "    (select refseq, levels, position  ";
            sql += "       from TZ_HOMEPAGE_QNA                   ";
            sql += "      where seq = " + seq;
            sql += "     ) a, TZ_HOMEPAGE_QNA b                   ";
            sql += " where a.refseq = b.refseq                    ";
            sql += "   and b.levels = (a.levels +1)                ";
            sql += "   and b.position = (a.position +1)            ";


            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }


//// //// //// //// //// //// //// //// //// //// //// //// //// /// ���� ���̺�   //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

    /**
    * ���ο� �ڷ����� ���
    * @param connMgr  DB Connection Manager
    * @param p_seq    �Խù� �Ϸù�ȣ
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int insertUpFile(DBConnectionManager connMgr, int p_seq, RequestBox   box) throws Exception { 
        ListSet             ls      = null;
        PreparedStatement pstmt2 = null;
        String              sql     = "";
        String sql2 = "";
        int isOk2 = 1;

        // ----------------------   ���ε�Ǵ� ������ ������ �˰� �ڵ��ؾ��Ѵ�  --------------------------------

        String [] v_realFileName = new String [FILE_LIMIT];
        String [] v_newFileName = new String [FILE_LIMIT];

        for ( int i = 0; i < FILE_LIMIT; i++ ) { 
            v_realFileName [i] = box.getRealFileName(FILE_TYPE + (i +1));
            v_newFileName [i] = box.getNewFileName(FILE_TYPE + (i +1));
        }
        // ----------------------------------------------------------------------------------------------------------------------------

        String s_userid = box.getSession("userid");

        try { 
             // ----------------------   �ڷ� ��ȣ �����´� ----------------------------
            sql = "select nvl(max(fileseq), 0) from TZ_HOMEPAGE_QNAFILE where seq =   " + p_seq;
            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_fileseq = ls.getInt(1) + 1;
            ls.close();
            // ------------------------------------------------------------------------------------

            //// //// //// //// //// //// //// //// //   ���� table �� �Է�  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql2 =  "insert into TZ_HOMEPAGE_QNAFILE(seq, fileseq, realfile, savefile, luserid, ldate)";
            sql2 += " values (?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";

            pstmt2 = connMgr.prepareStatement(sql2);

            for ( int i = 0; i < FILE_LIMIT; i++ ) { 
                if ( !v_realFileName [i].equals("") ) {       //      ���� ���ε� �Ǵ� ���ϸ� üũ�ؼ� db�� �Է��Ѵ�
                    pstmt2.setInt(1, p_seq);
                    pstmt2.setInt(2, v_fileseq);
                    pstmt2.setString(3, v_realFileName [i]);
                    pstmt2.setString(4, v_newFileName [i]);
                    pstmt2.setString(5, s_userid);

                    isOk2 = pstmt2.executeUpdate();
                    v_fileseq++;
                }
            }
            if ( pstmt2 != null ) { pstmt2.close(); }
        } catch ( Exception ex ) { 
            FileManager.deleteFile(v_newFileName, FILE_LIMIT);      //  �Ϲ�����, ÷������ ������ ����..
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
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
    public int deleteUpFile(DBConnectionManager connMgr, RequestBox box, Vector p_filesequence) throws Exception { 
        PreparedStatement pstmt3 = null;
        String sql3 = "";
        int isOk3 = 1;

        int v_seq    = box.getInt("p_seq");

        try { 
            sql3 = "delete from TZ_HOMEPAGE_QNAFILE where seq =? and fileseq = ?";
            pstmt3 = connMgr.prepareStatement(sql3);

            for ( int i = 0; i < p_filesequence.size(); i++ ) { 
                int v_fileseq = Integer.parseInt((String)p_filesequence.elementAt(i));

                pstmt3.setInt(1, v_seq);
                pstmt3.setInt(2, v_fileseq);

                isOk3 = pstmt3.executeUpdate();
            }
            if ( pstmt3 != null ) { pstmt3.close(); }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql3);
            throw new Exception("sql = " + sql3 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e ) { } }
        }

        return isOk3;
    }

 
    public static String convertBody(String contents) throws Exception { 
        String result = "";
        
        result = StringManager.replace(contents, "<HTML > ","");
        result = StringManager.replace(result, "<HEAD > ","");
        result = StringManager.replace(result, "<META NAME=\"GENERATOR\" Content=\"Microsoft DHTML Editing Control\" > ","");
        result = StringManager.replace(result, "<TITLE > ","");
        result = StringManager.replace(result, "</TITLE > ","");
        result = StringManager.replace(result, "</HEAD > ","");
        result = StringManager.replace(result, "<BODY > ","");
        result = StringManager.replace(result, "</BODY > ","");       
        result = StringManager.replace(result, "</HTML > ","");
                
        return result;
    }
    

/**
    �������� ��������� ����
    @param box      receive from the form object and session
    @return int
    */
     public int SuRoyAppUpdate032(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt = null;
        ListSet ls1         = null;
        
        String sql1         = "";
        int cancel_cnt      = 0;
        int isOk            = 0;
        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name"); 
        
        String  v_subj      = box.getString("p_subj");
        String  v_year      = box.getString("p_year");
        String  v_subjseq   = box.getString("p_subjseq");
        String  v_userid    = box.getString("p_userid");
        
        int 	v_seq	    = box.getInt("p_seq");
        
        Hashtable insertData = new Hashtable();
        /*
        ProposeBean probean = new ProposeBean();

        SubjComBean scbean = new SubjComBean();
        */

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

                      sql1 =	"	update TZ_CERTAPP ";
                      sql1 +=	"	set issend = 'Y', ";
                      sql1 +=	"		senddate = to_char(sysdate,'YYYYMMDDHH24MISS'), ";                      
                      sql1 +=	"		sname = ?, ";
                      sql1 +=	"		suserid = ? ";
                      sql1 +=	"	where subj = ?		";
                      sql1 +=	"		and year = ?	";
                      sql1 +=	"		and subjseq = ?";
                      sql1 +=	"		and userid = ?	";
                      sql1 +=	"		and seq = ?	";                                            
                      
                      pstmt = connMgr.prepareStatement(sql1);
                      pstmt.setString(1, s_usernm);
                      pstmt.setString(2, s_userid);
                      pstmt.setString(3, v_subj);
                      pstmt.setString(4, v_year);
                      pstmt.setString(5, v_subjseq);
                      pstmt.setString(6, v_userid);
                      pstmt.setInt(7, v_seq);
                      
                      isOk = pstmt.executeUpdate();
                      
                      if ( pstmt != null ) { pstmt.close(); }

                if ( isOk > 0 ) connMgr.commit();
                else connMgr.rollback();

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }         
}
