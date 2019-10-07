// **********************************************************
//  1. ��      ��: �Խ���
//  2. ���α׷���: BoardBean.java
//  3. ��      ��: �Խ���
//  4. ȯ      ��: JDK 1.4
//  5. ��      ��: 1.0
//  6. ��      ��: mscho 2008.10.20
//  7. ��      ��:
// **********************************************************
package com.ziaan.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.course.SubjGongData;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class BoardBean { 
    private ConfigSet config;
    private int row;
    private int admin_row;
	private	static final String	FILE_TYPE =	"p_file";			// 		���Ͼ��ε�Ǵ� tag name
	private	static final int FILE_LIMIT	= 5;					// 	  �������� ���õ� ����÷�� ����
    public static String COUNSEL_KIND = "0047";

    public BoardBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        �� ����� �������� row ���� �����Ѵ�
            admin_row = Integer.parseInt(config.getProperty("page.bulletin.adminrow") );        //        �� ����� �������� row ���� �����Ѵ�
            
            System.out.println(" config.getProperty : " + config.getProperty("page.bulletin.adminrow"));            
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }


    /**
    * �ڷ�� ���̺��ȣ
    * @param box          receive from the form object and session
    * @return int         �ڷ�� ���̺��ȣ
    * @throws Exception
    */
    public int selectTableseq(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         String              sql     = "";
         int result = 0;

         String v_type    = box.getStringDefault("p_type","");
         String v_grcode  = box.getStringDefault("p_grcode","0000000");
         String v_comp    = box.getStringDefault("p_comp","0000000000");
         String v_subj    = box.getStringDefault("p_subj","0000000000");
         String v_year    = box.getStringDefault("p_year","0000");
         String v_subjseq = box.getStringDefault("p_subjseq","0000");

         try { 
             connMgr = new DBConnectionManager();

             sql  = " select tabseq from TZ_BDS      ";
             sql += "  where type    = " + StringManager.makeSQL(v_type);
             sql += "    and grcode  = " + StringManager.makeSQL(v_grcode);
             sql += "    and comp    = " + StringManager.makeSQL(v_comp);
             sql += "    and subj    = " + StringManager.makeSQL(v_subj);
             sql += "    and year    = " + StringManager.makeSQL(v_year);
             sql += "    and subjseq = " + StringManager.makeSQL(v_subjseq);

             ls = connMgr.executeQuery(sql);

             if ( ls.next() ) { 
                 result = ls.getInt("tabseq");
             }
         }
         catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }
         finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return result;
     }


    /**
    * ����Խ��� ���̺��ȣ
    * @param box          receive from the form object and session
    * @return int         �ڷ�� ���̺��ȣ
    * @throws Exception
    */
    public int selectSBTableseq(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         PreparedStatement pstmt4 = null;
         ListSet ls1 = null;
         ListSet ls2 = null;
         ListSet ls3 = null;
         String sql1 = "";
         String sql2 = "";
         String sql3 = "";
         String sql4 = "";
         int v_tabseq = 0;
         int result = 0;

         String v_user_id = box.getSession("userid");
         String v_type    = box.getString("p_type");
         String v_subj    = box.getString("p_subj");
         String v_year    = box.getString("p_year");
         String v_subjseq = box.getString("p_subjseq");
         if ( v_subj.equals("") ) {     v_subj = box.getSession("s_subj");          }
         if ( v_year.equals("") ) {     v_year = box.getSession("s_year");          }
         if ( v_subjseq.equals("") ) {  v_subjseq = box.getSession("s_subjseq");    }

         try { 
             connMgr = new DBConnectionManager();

             sql1  = " select tabseq from TZ_BDS      ";
             sql1 += "  where type    = " + StringManager.makeSQL(v_type);
             sql1 += " and subj       = " + StringManager.makeSQL(v_subj);
             sql1 += " and year       = " + StringManager.makeSQL(v_year);
             sql1 += " and subjseq    = " + StringManager.makeSQL(v_subjseq);
             ls1 = connMgr.executeQuery(sql1);

             if ( ls1.next() ) {     // TZ_BDS�� �ش� ���̺� �������� �ִ� ���
                 v_tabseq = ls1.getInt("tabseq");
             } else {                 // TZ_BDS�� �ش� ���̺� �������� ���� ���
                 sql2  = " select count(subj) cnt from TZ_SUBJSEQ      ";
                 sql2 += "  where subj    = " + StringManager.makeSQL(v_subj);
                 sql2 += " and year       = " + StringManager.makeSQL(v_year);
                 sql2 += " and subjseq    = " + StringManager.makeSQL(v_subjseq);
                 ls2 = connMgr.executeQuery(sql2);

                 if ( ls2.next() && ls2.getInt("cnt") > 0 ) {   // ���� �����Ǿ��ִ� �����̸�
                    sql3 = "select nvl(max(tabseq), 0) from TZ_BDS";
                    ls3 = connMgr.executeQuery(sql3);
                    ls3.next();
                    v_tabseq = ls3.getInt(1) + 1;
                    ls3.close();

                    sql4  = "insert into TZ_BDS(tabseq,type,subj,year,subjseq,sdesc,luserid,ldate) ";
                    sql4 += " values(?,?,?,?,?,?,?,to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
                    pstmt4 = connMgr.prepareStatement(sql4);
                    pstmt4.setInt(1, v_tabseq);
                    pstmt4.setString(2, v_type);
                    pstmt4.setString(3, v_subj);
                    pstmt4.setString(4, v_year);
                    pstmt4.setString(5, v_subjseq);
                    pstmt4.setString(6, v_subj + "����" +v_subjseq + "�����Խ���");
                    pstmt4.setString(7, v_user_id);
                    result = pstmt4.executeUpdate();
                    
                    if ( pstmt4 != null ) { pstmt4.close(); }
                    
                } else { 
                    v_tabseq = 0;
                }
            }
         }
         catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql1);
             throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
         }
         finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { }}
             if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { }}
             if ( ls3 != null ) { try { ls3.close(); } catch ( Exception e ) { }}
             if ( pstmt4 != null ) { try { pstmt4.close(); } catch ( Exception e1 ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return v_tabseq;
     }


    /**
    * �����ڷ�� ���̺��ȣ
    * @param box          receive from the form object and session
    * @return int         �ڷ�� ���̺��ȣ
    * @throws Exception
    */
    public int selectSDTableseq(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         PreparedStatement pstmt4 = null;
         ListSet ls1 = null;
         ListSet ls2 = null;
         ListSet ls3 = null;
         String sql1 = "";
         String sql2 = "";
         String sql3 = "";
         String sql4 = "";
         int v_tabseq = 0;
         int result = 0;

         String v_user_id = box.getSession("userid");
         String v_type    = box.getString("p_type");
         String v_subj    = box.getString("p_subj");
         String v_year    = box.getString("p_year");
         String v_subjseq    = box.getString("p_subjseq");
         if ( v_subj.equals("") ) {     v_subj = box.getSession("s_subj");          }

         try { 
             connMgr = new DBConnectionManager();

             sql1  = " select tabseq from TZ_BDS      ";
             sql1 += "  where type    = " + StringManager.makeSQL(v_type);
             sql1 += " and subj    = " + StringManager.makeSQL(v_subj);
             sql1 += " and year    = " + StringManager.makeSQL(v_year);
             sql1 += " and subjseq    = " + StringManager.makeSQL(v_subjseq);
             
             // ���ǰ������̳� �����ϱ� ���̶�� �⵵�� �������� ������ �ش�.
             if(v_type.equals("MP") || v_type.equals("CJ"))
             {
                 sql1 += " and year    = " + StringManager.makeSQL(v_year);
                 sql1 += " and subjseq    = " + StringManager.makeSQL(v_subjseq);
             }
             
             ls1 = connMgr.executeQuery(sql1);

             if ( ls1.next() ) {     // TZ_BDS�� �ش� ���̺� �������� �ִ� ���
                 v_tabseq = ls1.getInt("tabseq");
             } else {                 // TZ_BDS�� �ش� ���̺� �������� ���� ���
                 sql2  = " select count(subj) cnt from TZ_SUBJ      ";
                 sql2 += "  where subj    = " + StringManager.makeSQL(v_subj);
                 ls2 = connMgr.executeQuery(sql2);

                 if ( ls2.next() && ls2.getInt("cnt") > 0 ) {   // ���� �����Ǿ��ִ� �����̸�
                    sql3 = "select nvl(max(tabseq), 0) from TZ_BDS";
                    ls3 = connMgr.executeQuery(sql3);
                    ls3.next();
                    v_tabseq = ls3.getInt(1) + 1;
                    ls3.close();

                    sql4  = "insert into TZ_BDS(tabseq,type,subj,year,subjseq,sdesc,luserid,ldate) ";
                    sql4 += " values(?,?,?,?,?,?,?,to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
                    
                    pstmt4 = connMgr.prepareStatement(sql4);
                    pstmt4.setInt(1, v_tabseq);
                    pstmt4.setString(2, v_type);
                    pstmt4.setString(3, v_subj);
                    pstmt4.setString(4, v_year);
                    pstmt4.setString(5, v_subjseq);
                    pstmt4.setString(6, v_subj + "�����ڷ��");
                    pstmt4.setString(7, v_user_id);
                    result = pstmt4.executeUpdate();
                    
                    if ( pstmt4 != null ) { pstmt4.close(); }
                    
                } else { 
                    v_tabseq = 0;
                }
            }
         }
         catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql1);
             throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
         }
         finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { }}
             if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { }}
             if ( ls3 != null ) { try { ls3.close(); } catch ( Exception e ) { }}
             if ( pstmt4 != null ) { try { pstmt4.close(); } catch ( Exception e1 ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return v_tabseq;
     }


    /**
    * �ڷ�� ����Ʈȭ�� select
    * @param    box          receive from the form object and session
    * @return ArrayList  �ڷ�� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectBoardList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
        int v_pageno = box.getInt("p_pageno");
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select a.seq seq, a.userid userid, a.name name, a.title title, count(b.realfile) upfilecnt, decode(substr(a.gadmin,1,1), 'Z', 'ZZ', 'P', '����',  '���') gadmin,  ";
            sql += "        a.indate indate, a.cnt cnt, a.refseq refseq, a.levels levels, a.position position, isopen, origin_userid, a.isimport ";
            sql += "   from TZ_BOARD a, TZ_BOARDFILE b                                                         ";
            sql += "  where a.tabseq = b.tabseq( +)                                                             ";
            sql += "    and a.seq    = b.seq( +)                                                                ";
            sql += "    and a.tabseq = " + v_tabseq;

            if ( !v_searchtext.equals("") ) {                //    �˻�� ������
                // v_pageno = 1;   //      �˻��� ��� ù��° �������� �ε��ȴ�

                if ( v_search.equals("name") ) {              //    �̸����� �˻��Ҷ�
                    sql += " and a.name like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                }
                else if ( v_search.equals("title") ) {        //    �������� �˻��Ҷ�
                    sql += " and a.title like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                }
                else if ( v_search.equals("content") ) {     //    �������� �˻��Ҷ�
                    // sql += " and dbms_lob.instr(a.content, " + StringManager.makeSQL(v_searchtext) + ",1,1) < > 0";
                    sql += " and ( a.content like " + StringManager.makeSQL("%" + v_searchtext + "%") + " or a.content like " + StringManager.makeSQL("%" + v_searchtext) + " or a.content like " + StringManager.makeSQL( v_searchtext + "%") + ")";            //   Oracle 9i ��
                }
            }

            sql += " group by a.seq, a.userid, a.name, a.title, a.indate, a.cnt, a.refseq, a.levels, a.position, decode(substr(a.gadmin,1,1), 'Z', 'ZZ', 'P', '����',  '���'), isopen, origin_userid, a.isimport ";
            sql += " order by a.isimport desc, a.refseq desc, position asc                                                       ";
            
            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                       //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);               //     ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();    //     ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum()+1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount", new Integer(row));
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
     * �ڷ�� ����Ʈȭ�� select (�����ڵ�, �⵵, �������)
     * @param    box          receive from the form object and session
     * @return ArrayList  �ڷ�� ����Ʈ
     * @throws Exception
     */
     public ArrayList selectBoardListBySubjseq(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         ArrayList           list    = null;
         String              sql     = "";
         DataBox             dbox    = null;

         int v_tabseq = box.getInt("p_tabseq");
         int v_pageno = box.getInt("p_pageno");
         String v_searchtext = box.getString("p_searchtext");
         String v_search     = box.getString("p_search");
         String v_subj       = box.getString("p_subj");
         String v_year       = box.getString("p_year");
         String v_subjseq    = box.getString("p_subjseq");

         try { 
             connMgr = new DBConnectionManager();

             list = new ArrayList();

             sql = "\n select a.seq seq, a.userid userid, a.name name, a.title title, count(b.realfile) upfilecnt, decode(substr(a.gadmin,1,1), 'Z', 'ZZ', 'P', '����',  '���') gadmin,  "
             	 + "\n        a.indate indate, a.cnt cnt, a.refseq refseq, a.levels levels, a.position position, isopen, origin_userid, a.isimport "
             	 + "\n   from TZ_BOARD a, TZ_BOARDFILE b "
             	 + "\n  where a.tabseq = b.tabseq( +) "
             	 + "\n    and a.seq    = b.seq( +) "
             	 + "\n    and a.tabseq = " + v_tabseq;

             if ( !v_searchtext.equals("") ) {                //    �˻�� ������
                 // v_pageno = 1;   //      �˻��� ��� ù��° �������� �ε��ȴ�

                 if ( v_search.equals("name") ) {              //    �̸����� �˻��Ҷ�
                     sql += "\n    and a.name like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 }
                 else if ( v_search.equals("title") ) {        //    �������� �˻��Ҷ�
                     sql += "\n    and a.title like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 }
                 else if ( v_search.equals("content") ) {     //    �������� �˻��Ҷ�
                     // sql += " and dbms_lob.instr(a.content, " + StringManager.makeSQL(v_searchtext) + ",1,1) < > 0";
                     sql += "\n    and ( a.content like " + StringManager.makeSQL("%" + v_searchtext + "%") + " or a.content like " + StringManager.makeSQL("%" + v_searchtext) + " or a.content like " + StringManager.makeSQL( v_searchtext + "%") + ")";            //   Oracle 9i ��
                 }
             }
             
             sql+="\n    and a.subj = " + StringManager.makeSQL(v_subj)
	            + "\n    and a.year = " + StringManager.makeSQL(v_year)
	            + "\n    and a.subjseq = " + StringManager.makeSQL(v_subjseq)
             	+ "\n group by a.seq, a.userid, a.name, a.title, a.indate, a.cnt, a.refseq, a.levels, a.position, decode(substr(a.gadmin,1,1), 'Z', 'ZZ', 'P', '����',  '���'), isopen, origin_userid, a.isimport "
             	+ "\n order by a.isimport desc, a.refseq desc, position asc ";
             
             ls = connMgr.executeQuery(sql);
             ls.setPageSize(row);                       //  �������� row ������ �����Ѵ�
             ls.setCurrentPage(v_pageno);               //     ������������ȣ�� �����Ѵ�.
             int totalpagecount = ls.getTotalPage();    //     ��ü ������ ���� ��ȯ�Ѵ�
             int totalrowcount = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�
//System.out.println( "��� \n" + sql );
             while ( ls.next() ) { 
                 dbox = ls.getDataBox();

                 dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum()+1));
                 dbox.put("d_totalpage", new Integer(totalpagecount));
                 dbox.put("d_rowcount", new Integer(row));
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
   * ���õ� �ڷ�� �Խù� �󼼳��� select
   * @param box          receive from the form object and session
   * @return ArrayList   ��ȸ�� ������
   * @throws Exception
   */
   public DataBox selectBoard(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;
        String              v_upcnt = box.getString("p_upcnt");

        int v_tabseq    = box.getInt("p_tabseq");
        int v_seq       = box.getInt("p_seq");
        int v_upfilecnt = (box.getInt("p_upfilecnt") > 0?box.getInt("p_upfilecnt"):1);

        // String [] realfile = new String [v_upfilecnt];
        // String [] savefile= new String [v_upfilecnt];
        // int [] fileseq = new int [v_upfilecnt];
        Vector realfileVector = new Vector();
        Vector savefileVector = new Vector();
        Vector fileseqVector  = new Vector();
        try { 
            connMgr = new DBConnectionManager();

            sql  = " select a.seq seq, a.userid userid, a.name name, a.title title, b.fileseq fileseq, b.realfile realfile, a.content content, decode(substr(a.gadmin,1,1), 'Z', 'ZZ', 'P', '����',  '���') gadmin, a.gadmin gadmin_value, ";
            sql += "        b.savefile savefile, a.indate indate, a.cnt cnt, a.refseq refseq, a.levels levels, a.position position ,             ";
            sql +="         (select count(realfile) from TZ_BOARDFILE where tabseq = a.tabseq and seq = a.seq) upfilecnt, a.isimport,            ";
            sql +=" (                                           "+
                    "           select decode(count(*), 0, 'Y', 'N')    "+
                    "           from TZ_BOARD c, TZ_BOARD d             "+
                    "           where c.refseq = d.refseq               "+
                    "           and d.levels = (c.levels +1)            "+
                    "           and d.position = (c.position +1)        "+
                    "           and c.tabseq = a.tabseq                 "+  
                    "           and c.seq = a.seq                       "+
                    "           and c.tabseq = d.tabseq                 "+
                    "       ) delyn, isopen, a.sangdam_gubun, nvl(c.codenm, '') sangdam_gubun_name  ";             
            sql += " from TZ_BOARD a, TZ_BOARDFILE b, TZ_Code c                                                                                 ";
            sql += "  where a.tabseq = b.tabseq( +)                                                                                              ";
            sql += "    and a.seq    = b.seq( +)                                                                                                 ";
            sql += "    and a.tabseq = " + v_tabseq;
            sql += "    and a.seq    = " + v_seq;
            sql += "    and     c.gubun(+)  = " + StringManager.makeSQL(COUNSEL_KIND); 
            sql += "    and     a.sangdam_gubun = c.code(+)                                      ";            
            
//            System.out.println("�� \n " + sql);
            
            ls = connMgr.executeQuery(sql);
            for ( int i = 0; ls.next(); i++ ) { 

                dbox = ls.getDataBox();

                realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
                fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));
            }
            if ( realfileVector != null ) dbox.put("d_realfile", realfileVector);
            if ( savefileVector != null ) dbox.put("d_savefile", savefileVector);
            if ( fileseqVector  != null ) dbox.put("d_fileseq", fileseqVector);

            if ( !v_upcnt.equals("N") ) { 
                connMgr.executeUpdate("update TZ_BOARD set cnt = cnt + 1 where  tabseq = " + v_tabseq + " and seq = " + v_seq);
            }
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return dbox;
    }
   
   
   
   
   
   /**
    * ���õ� �ڷ�� �Խù� �󼼳���亯 select
    * @param box          receive from the form object and session
    * @return ArrayList   ��ȸ�� ������
    * @throws Exception
    */
    public DataBox selectBoarddetail(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         String              sql     = "";
         DataBox             dbox1    = null;
         String              v_upcnt = box.getString("p_upcnt");

         int v_tabseq    = box.getInt("p_tabseq");
         int v_seq       = box.getInt("p_seq");
        
         try { 
             connMgr = new DBConnectionManager();

             sql  = "select  a.*, ";
             sql += " (select content from tz_board where refseq = a.seq and tabseq=a.tabseq and levels='2' and rownum < 2 ) acontents, ";
             sql += " (select title from tz_board where refseq = a.seq  and tabseq=a.tabseq and levels='2' and rownum < 2) atitle  ";
             sql += "  from tz_board a  where seq  = "  +v_seq;
             sql += "  and tabseq = " + v_tabseq;
             
//             System.out.println("�� \n " + sql);
             
             ls = connMgr.executeQuery(sql);
             for ( int i = 0; ls.next(); i++ ) { 

                 dbox1 = ls.getDataBox();

             }

         }
         catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }
         finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return dbox1;
     }


    /**
    * ���ο� �ڷ�� ���� ���
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int insertBoard(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ResultSet rs1   = null;
        Statement stmt1 = null;
        PreparedStatement pstmt1  = null;
        String sql    = "";
        String sql1   = "";
        String sql2   = "";
        String v_type = "";
        int isOk1     = 1;
        int isOk2     = 1;
        int v_seq     = 0;

        int    v_tabseq  = box.getInt("p_tabseq");
        String v_title   = box.getString("p_title");
        String v_content = box.getString("p_content");
        String s_userid  = box.getSession("userid");
        String s_usernm  = box.getSession("name");
        String v_isedu   = box.getString("p_isedu"); // (2005.9)�н�â�Խ���,����ڷ�� ���� ����(�н�â�Խ��� = '1', ����ڷ�� = '')
        String v_isopen  = box.getString("p_isopen");
        String s_gadmin  = box.getSession("gadmin");
        String v_sangdamgubun = box.getString("p_sangdamgubun");
        String v_isimport = box.getStringDefault("p_isimport", "N");
               
        String v_subj  = box.getString("p_subj");
        String v_year  = box.getString("p_year");
        String v_subjseq  = box.getString("p_subjseq");

        /*if ( box.getSession("gadmin").substring(0,1).equals("A") ) { 
        	s_usernm = "���";	
        }*/

        /*
		if ( box.getSession("gadmin").substring(0,1).equals("A1") ) { 
			s_userid = "���";
		} else { 
			s_userid = box.getSession("userid");
		}*/
        
		try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            stmt1 = connMgr.createStatement();

            // ----------------------   �Խ��� ��ȣ �����´� ----------------------------
            sql = "select nvl(max(seq), 0) from TZ_BOARD where tabseq = " + v_tabseq;
            rs1 = stmt1.executeQuery(sql);
            if ( rs1.next() ) { 
                v_seq = rs1.getInt(1) + 1;
            }
            rs1.close();
            // -------------------------------------------------------------------------
			
            // ----------------------   �Խ��� table �� �Է�  --------------------------
            sql1= " insert into tz_board( "
            	+ "      tabseq, seq, userid, name, indate "
            	+ "    , title, content, cnt, refseq, levels "
            	+ "    , position, luserid, ldate, isopen, gadmin "
            	+ "    , sangdam_gubun, isimport, subj, year, subjseq )  "
            	+ " values ( "
            	+ "      ?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS') "
            	+ "    , ?, ?, ?, ?, ? "
//            	+ "    , ?, empty_clob(), ?, ?, ? "
            	+ "    , ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ? "
            	+ "    , ?, ?, ?, ?, ? )    ";

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
            pstmt1.setString(12, v_isopen);
            pstmt1.setString(13, s_gadmin);
            pstmt1.setString(14, v_sangdamgubun);
            pstmt1.setString(15, v_isimport);
            pstmt1.setString(16, v_subj);
            pstmt1.setString(17, v_year);
            pstmt1.setString(18, v_subjseq);

            isOk1 = pstmt1.executeUpdate();
            if ( pstmt1 != null ) { pstmt1.close(); }
			sql2 = "select content from tz_board where tabseq = " + v_tabseq + " and seq = " + v_seq;
//			connMgr.setOracleCLOB(sql2, v_content);       //      (��Ÿ ���� ���)       

            // ���Ͼ��ε�
            isOk2 = this.insertUpFile(connMgr, v_tabseq, v_seq, box);
            
            if ( isOk1 > 0 && isOk2 > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
                if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }
        }

        catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
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
        DBConnectionManager	connMgr	= null;
        ResultSet rs1 = null;

        Statement stmt1 = null;
        Statement stmt2 = null;
        PreparedStatement pstmt1  = null;
        PreparedStatement pstmt2 = null;

        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        String sql4 = "";

        int isOk1 = 1;
        int isOk2 = 1;
        int isOk3 = 1;
        int v_seq = 0;
        String v_isedu    = box.getString("p_isedu"); // (2005.9)�н�â�Խ���,����ڷ�� ���� ����(�н�â�Խ��� = '1', ����ڷ�� = '')  
        int    v_tabseq   = box.getInt("p_tabseq");
        int    v_refseq   = box.getInt("p_refseq");
        int    v_levels   = box.getInt("p_levels");
        int    v_position = box.getInt("p_position");
        String v_title1    = box.getString("p_title");
        String v_content1  = box.getString("p_content");

        String s_userid         = box.getSession("userid");
        String s_usernm         = box.getSession("name");
        String s_gadmin         = box.getSession("gadmin");
        String v_isopen         = box.getString("p_isopen");
        String v_origin_userid  = box.getString("p_origin_userid");
        String v_sangdamgubun = box.getString("p_sangdamgubun");
        String v_isimport       = box.getStringDefault("p_isimport", "N");

        String v_subj  = box.getString("p_subj");
        String v_year  = box.getString("p_year");
        String v_subjseq  = box.getString("p_subjseq");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // ���� �亯�� ��ġ ��ĭ������ ����
            sql1  = "update TZ_BOARD ";
            sql1 += "   set position = position + 1 ";
            sql1 += " where tabseq   = ? ";
            sql1 += "   and refseq   = ? ";
            sql1 += "   and position > ? ";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_tabseq);
            pstmt1.setInt(2, v_refseq);
            pstmt1.setInt(3, v_position);
            isOk1 = pstmt1.executeUpdate();
            
            if ( pstmt1 != null ) { pstmt1.close(); }

            stmt1 = connMgr.createStatement();
            // ----------------------   �Խ��� ��ȣ �����´� ----------------------------
            sql2 = "select nvl(max(seq), 0) from TZ_BOARD where tabseq = " +  v_tabseq;
            rs1 = stmt1.executeQuery(sql2);
            if ( rs1.next() ) { 
                v_seq = rs1.getInt(1) + 1;
            }
            // ------------------------------------------------------------------------------------

            //// //// //// //// //// //// //// //// /  �Խ��� table �� �Է�  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql3 =  " insert into TZ_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate, gadmin, isopen, origin_userid, sangdam_gubun, isimport, subj, year, subjseq)  ";
            sql3 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, ? ) ";
//            sql3 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, ? ) ";

            pstmt2 = connMgr.prepareStatement(sql3);
            pstmt2.setInt(1, v_tabseq);
            pstmt2.setInt(2, v_seq);
            pstmt2.setString(3, s_userid);
            pstmt2.setString(4, s_usernm);
            pstmt2.setString(5, v_title1);
            pstmt2.setString(6, v_content1);
            pstmt2.setInt(7, 0);
            pstmt2.setInt(8, v_refseq);
            pstmt2.setInt(9, v_levels + 1);
            pstmt2.setInt(10, v_position + 1);
            pstmt2.setString(11, s_userid);
            pstmt2.setString(12, s_gadmin);
            pstmt2.setString(13, v_isopen);
            pstmt2.setString(14, v_origin_userid);
            pstmt2.setString(15, v_sangdamgubun);
            pstmt2.setString(16, v_isimport);
            pstmt2.setString(17, v_subj);
            pstmt2.setString(18, v_year);
            pstmt2.setString(19, v_subjseq);
            isOk2 = pstmt2.executeUpdate();
            
            if ( pstmt2 != null ) { pstmt2.close(); }

            // WebLogic 6.1�ΰ��
            sql4 = "select content from TZ_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq + " for update";
            //connMgr.setWeblogicCLOB(sql4, v_content);       //      clob �ʵ忡 ��Ʈ���� �̿�,  data �� �ִ´�(Weblogic �� ���)
//			connMgr.setOracleCLOB(sql4, v_content);       //      (��Ÿ ���� ���)       

            isOk3 = this.insertUpFile(connMgr, v_tabseq, v_seq, box);

            if ( isOk2 > 0 && isOk3 > 0 ) {   connMgr.commit(); }
            else {                          connMgr.rollback(); }

        }
        catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1 + "\r\n" +sql2 + "\r\n" +sql3 + "\r\n" +sql4);
            throw new Exception("sql = " + sql1 + "\r\n" + "sql2 = " + sql2 + "\r\n" + "sql3 = " + sql3 + "\r\n" + "sql4 = " + sql4 + "\r\n" +ex.getMessage() );
        }
        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( stmt2 != null ) { try { stmt2.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
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
        DBConnectionManager	connMgr	= null;
        ResultSet rs1 = null;
        PreparedStatement pstmt1  = null;
        String sql1 = "";
        String sql2 = "";

        int isOk1 = 1, isOk2=1, isOk3=1;
        String v_isedu   = box.getString("p_isedu"); // (2005.9)�н�â�Խ���,����ڷ�� ���� ����(�н�â�Խ��� = '1', ����ڷ�� = '')        
        int    v_tabseq    = box.getInt("p_tabseq");
        int    v_seq       = box.getInt("p_seq");
        int    v_upfilecnt = box.getInt("p_upfilecnt");    //  ������ ������ִ� ���ϼ�
        String v_title     = box.getString("p_title");
        // String v_content =  StringManager.replace(box.getString("content"),"<br > ","\n"); 
        String v_content   = box.getString("p_content");
        Vector v_savefile     = box.getVector("p_savefile"); // ���û�������
        Vector v_filesequence = box.getVector("p_fileseq");  // ���û������� sequence
        Vector v_realfile     = box.getVector("p_file");     // ���� ��� ����
        String v_sangdamgubun = box.getString("p_sangdamgubun");
        String v_isopen = box.getString("p_isopen");			//��������
        String v_isimport     = box.getStringDefault("p_isimport", "N");
        
		for ( int	i =	0; i < v_upfilecnt;	i++ ) { 
			if ( 	!box.getString("p_fileseq" + i).equals(""))	{ 

				v_savefile.addElement(box.getString("p_savefile" + i));			// 		������ ������ִ� ���ϸ� �߿���	������ ���ϵ�
				v_filesequence.addElement(box.getString("p_fileseq"	 + i));		 // 		������	������ִ� ���Ϲ�ȣ	�߿��� ������ ���ϵ�

			}
		}

        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name");

        /*
        if ( box.getSession("gadmin").substring(0,1).equals("A") ) { 
        	s_usernm = "���";	
        }*/

      
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql1 = "update TZ_BOARD set title = ?, content = ?, userid = ?, name = ?, luserid = ?, ldate = to_char(sysdate, 'YYYYMMDDHH24MISS'), sangdam_gubun = ?, isimport = ? ,isopen = ? ";
//            sql1 = "update TZ_BOARD set title = ?, content=empty_clob(), userid = ?, name = ?, luserid = ?, ldate = to_char(sysdate, 'YYYYMMDDHH24MISS'), sangdam_gubun = ?, isimport = ? ,isopen = ? ";
            sql1 += "  where tabseq = ? and seq = ?";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_title);
            pstmt1.setString(2, v_content);
        //  connMgr.setCharacterStream(pstmt1, 2, v_content);           // Oracle 9i or Weblogic 6.1 �� ���
            pstmt1.setString(3, s_userid);
            pstmt1.setString(4, s_usernm);
            pstmt1.setString(5, s_userid);
            pstmt1.setString(6, v_sangdamgubun);
            pstmt1.setString(7, v_isimport);
            //�������� �߰� 2008.11.25
            pstmt1.setString(8, v_isopen);
            pstmt1.setInt(9, v_tabseq);
            pstmt1.setInt(10, v_seq);

            isOk1 = pstmt1.executeUpdate();
            
            if ( pstmt1 != null ) { pstmt1.close(); }

            // WebLogic 6.1�ΰ��
            sql2 = "select content from TZ_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq ;
            //connMgr.setWeblogicCLOB(sql2, v_content);       //      clob �ʵ忡 ��Ʈ���� �̿�,  data �� �ִ´�(Weblogic �� ���)
//            connMgr.setOracleCLOB(sql2, v_content);       //      (��Ÿ ���� ���)

			isOk2 =	this.insertUpFile(connMgr, v_tabseq, v_seq,	box);		// 		����÷���ߴٸ� ����table��	insert

			isOk3 =	this.deleteUpFile(connMgr, box,	v_filesequence);		// 	   ������ ������ �ִٸ�	����table���� ����

			if ( isOk1 > 0 &&	isOk2 > 	0 && isOk3 > 0)	{ 
				connMgr.commit();
				if ( v_savefile != null )	{ 
					FileManager.deleteFile(v_savefile);			// 	 DB	���� ���ó����	�Ϸ�Ǹ� �ش� ÷������ ����
				}
			} else connMgr.rollback();




        }
        catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
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
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 0;
        int isOk2 = 0;

        int v_tabseq = box.getInt("p_tabseq");
        int v_seq = box.getInt("p_seq");

        Vector v_savefile  = box.getVector("p_savefile");
        int v_upfilecnt = v_savefile.size();    //  ������ ������ִ� ���ϼ�

        // �亯 ���� üũ(�亯 ������ �����Ұ�)
        if ( this.selectBoard(v_tabseq, v_seq) == 0 ) { 

            try { 
                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);

                isOk1 = 1;
                isOk2 = 1;
                sql1 = "delete from TZ_BOARD where tabseq = ? and seq = ? ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);
                pstmt1.setInt(2, v_seq);
                isOk1 = pstmt1.executeUpdate();
                
                if ( pstmt1 != null ) { pstmt1.close(); }

                if ( v_upfilecnt > 0 ) { 
                    // sql2 = "delete from TZ_BOARDFILE where tabseq = ? and seq =  ?";
                    sql2 = "delete from TZ_BOARDFILE where tabseq = " + v_tabseq + " and seq = " +v_seq;
                    // pstmt2 = connMgr.prepareStatement(sql2);
                    // pstmt2.setInt(1, v_tabseq);
                    // pstmt2.setInt(2, v_seq);
                    // isOk2 = pstmt2.executeUpdate();
                    isOk2 = connMgr.executeUpdate(sql2);
                }
                if ( isOk1 > 0 && isOk2 > 0 ) { 
                    connMgr.commit();
                    if ( v_upfilecnt > 0 ) { 
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
                if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
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
   public int selectBoard(int tabseq, int seq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        int result = 0;

        try { 
            connMgr = new DBConnectionManager();

            sql  = "  select count(*) cnt                         ";
            sql += "  from                                        ";
            sql += "    (select tabseq, refseq, levels, position  ";
            sql += "       from TZ_BOARD                          ";
            sql += "      where tabseq = " + tabseq;
            sql += "        and seq = " + seq;
            sql += "     ) a, TZ_BOARD b                          ";
            sql += " where a.tabseq = b.tabseq                    ";
            sql += "   and a.refseq = b.refseq                    ";
            sql += "   and b.levels = (a.levels +1)                ";
            sql += "   and b.position = (a.position +1)            ";

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = ls.getInt("cnt");
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
     public int insertUpFile(DBConnectionManager connMgr, int p_tabseq, int p_seq, RequestBox   box) throws Exception { 
        ListSet             ls      = null;
        PreparedStatement pstmt2 = null;
        String              sql     = "";
        String sql2 = "";
        int isOk2 = 1;

        // ----------------------   ���ε�Ǵ� ������ ������ �˰� �ڵ��ؾ��Ѵ�  --------------------------------
		String [] v_realFileName = new String [FILE_LIMIT];
		String [] v_newFileName	= new String [FILE_LIMIT];

		for ( int	i =	0; i < FILE_LIMIT; i++ )	{ 
			v_realFileName [i] = box.getRealFileName(FILE_TYPE + (i +1));
			v_newFileName [i] =	box.getNewFileName(FILE_TYPE + (i +1));
		}

        String s_userid = box.getSession("userid");

        try { 

            // ----------------------   �ڷ� ��ȣ �����´� ----------------------------
            sql = "select nvl(max(fileseq), 0) from TZ_BOARDFILE where tabseq = " + p_tabseq + " and seq =   " + p_seq;

            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_fileseq = ls.getInt(1) + 1;
            ls.close();
            // ------------------------------------------------------------------------------------
//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +FILE_LIMIT);
            //// //// //// //// //// //// //// //// //   ���� table �� �Է�  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql2 =  "insert into TZ_BOARDFILE(tabseq, seq, fileseq, realfile, savefile, luserid, ldate)";
            sql2 += " values (?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";

			pstmt2 = connMgr.prepareStatement(sql2);

			for ( int	i =	0; i < FILE_LIMIT; i++ )	{ 
				if ( 	!v_realFileName[i].equals(""))	{ 		// 		���� ���ε�	�Ǵ� ���ϸ�	üũ�ؼ� db�� �Է��Ѵ�
//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +v_realFileName[i]);
					pstmt2.setInt(1, p_tabseq);
					pstmt2.setInt(2, p_seq);
					pstmt2.setInt(3, v_fileseq);
					pstmt2.setString(4, v_realFileName[i]);
					pstmt2.setString(5, v_newFileName[i]);
					pstmt2.setString(6, s_userid);

					isOk2 = pstmt2.executeUpdate();
					v_fileseq++;
				}
			}
        }
		catch ( Exception ex ) { 
			FileManager.deleteFile(v_newFileName, FILE_LIMIT);		// 	�Ϲ�����, ÷������ ������ ����..
			ErrorManager.getErrorStackTrace(ex,	box, sql2);
			throw new Exception("sql = " + sql2	 + "\r\n" + ex.getMessage() );
		}
		finally	{ 
		    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }	}
			if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
		}
		return isOk2;
	}

	/**
	 * ���õ� �ڷ����� DB���� ����
	 * @param connMgr			DB Connection Manager
	 * @param box				receive from the form object and session
	 * @param p_filesequence    ���� ���� ����
	 * @return
	 * @throws Exception
	 */
	public int deleteUpFile(DBConnectionManager	connMgr, RequestBox box, Vector p_filesequence)	throws Exception { 
		PreparedStatement pstmt3 = null;
		String sql	= "";
		String sql3	= "";
        ListSet             ls      = null;
		int	isOk3 =	1;
        int v_tabseq = box.getInt("p_tabseq");
        String v_types   = box.getString("p_types");
		int	v_seq =	box.getInt("p_seq");

		try	{ 


			sql3 = "delete from TZ_BOARDFILE where tabseq = " + v_tabseq + " and seq =? and fileseq = ?";

			pstmt3 = connMgr.prepareStatement(sql3);
			for ( int	i =	0; i < p_filesequence.size(); i++ ) { 
				int	v_fileseq =	Integer.parseInt((String)p_filesequence.elementAt(i));

				pstmt3.setInt(1, v_seq);
				pstmt3.setInt(2, v_fileseq);
				isOk3 =	pstmt3.executeUpdate();
			}
			if ( pstmt3 != null ) { pstmt3.close(); }
		}
		catch ( Exception ex ) { 
			   ErrorManager.getErrorStackTrace(ex, box,	sql3);
			throw new Exception("sql = " + sql3	 + "\r\n" + ex.getMessage() );
		}
		finally	{ 
			if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e ) { }	}
		}
		return isOk3;
	}

    /**
    * �ڷ�� ����Ʈȭ�� select
    * @param    box          receive from the form object and session
    * @return ArrayList  �ڷ�� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectBoardListForAdmin(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        int v_tabseq = box.getInt("p_tabseq");
        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        int v_pageno = box.getInt("p_pageno");
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");
        
        Vector realfileVector = new Vector();
        Vector savefileVector = new Vector();
        Vector fileseqVector  = new Vector();
            
        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select a.seq seq, a.userid userid, a.name name, a.title title, count(b.realfile) upfilecnt, decode(substr(a.gadmin,1,1), 'Z', 'ZZ', 'P', '����',  '���') gadmin, ";
            sql += "        a.indate indate, a.cnt cnt, a.refseq refseq, a.levels levels, a.position position, isopen, MAX(b.savefile) savefile, MAX(b.realfile) realfile, MAX(b.fileseq) fileseq, a.isimport  ";
            sql += "   from TZ_BOARD a, TZ_BOARDFILE b                                                         ";
            sql += "  where a.tabseq = b.tabseq( +)                                                             ";
            sql += "    and a.seq    = b.seq( +)                                                                ";
            sql += "    and a.tabseq = " + v_tabseq;
            sql += "    and a.subj = " + StringManager.makeSQL(v_subj);
            sql += "    and a.year = " + StringManager.makeSQL(v_year);
            sql += "    and a.subjseq = " + StringManager.makeSQL(v_subjseq);

            if ( !v_searchtext.equals("") ) {                //    �˻�� ������
                // v_pageno = 1;   //      �˻��� ��� ù��° �������� �ε��ȴ�

                if ( v_search.equals("name") ) {              //    �̸����� �˻��Ҷ�
                    sql += " and a.name like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                }
                else if ( v_search.equals("title") ) {        //    �������� �˻��Ҷ�
                    sql += " and a.title like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                }
                else if ( v_search.equals("content") ) {     //    �������� �˻��Ҷ�
                    // sql += " and dbms_lob.instr(a.content, " + StringManager.makeSQL(v_searchtext) + ",1,1) < > 0";
                    sql += " and ( a.content like " + StringManager.makeSQL("%" + v_searchtext + "%") + " or a.content like " + StringManager.makeSQL("%" + v_searchtext) + " or a.content like " + StringManager.makeSQL( v_searchtext + "%") + ")";            //   Oracle 9i ��
                }
                else if ( v_search.equals("ldate") ) {     //    �ۼ��������� �˻��Ҷ�
                    sql += " and ( SUBSTR(a.indate, 1, 8) = " + StringManager.makeSQL( StringManager.replace(v_searchtext, ".", "") ) + " )  \n";            
                }
                else if ( v_search.equals("userid") ) {     //    ���̵�� �˻��Ҷ�
                    sql += " and ( a.userid  = " + StringManager.makeSQL(v_searchtext ) + " ) \n";            
                }
            }

            sql += " group by a.seq, a.userid, a.name, a.title, a.indate, a.cnt, a.refseq, a.levels, a.position, decode(substr(a.gadmin,1,1), 'Z', 'ZZ', 'P', '����',  '���'), isopen, a.isimport ";
            //sql += " order by a.isimport desc , a.refseq desc, position asc                                                       ";
            sql += " order by a.refseq desc, position asc                                                       ";
            
            ls = connMgr.executeQuery(sql);
            
            ls.setPageSize(admin_row);                       //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);               //     ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();    //     ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                realfileVector = new Vector();
                savefileVector = new Vector();
                fileseqVector  = new Vector();

                dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount", new Integer(admin_row));
                
                realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
                fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));
                
                if ( realfileVector != null ) dbox.put("d_realfile", realfileVector);
                if ( savefileVector != null ) dbox.put("d_savefile", savefileVector);
                if ( fileseqVector  != null ) dbox.put("d_fileseq", fileseqVector);            

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
     * �ڷ�� ����Ʈȭ�� select (�����ڵ�, �⵵, �������)
     * @param    box          receive from the form object and session
     * @return ArrayList  �ڷ�� ����Ʈ
     * @throws Exception
     */
     public ArrayList selectBoardListForAdminBySubjseq(RequestBox box) throws Exception { 
         DBConnectionManager connMgr = null;
         ListSet             ls      = null;
         ArrayList           list    = null;
         String              sql     = "";
         DataBox             dbox    = null;
         
         int v_tabseq = box.getInt("p_tabseq");
         int v_pageno = box.getInt("p_pageno");
         String v_searchtext = box.getString("p_searchtext");
         String v_search     = box.getString("p_search");

         String v_subj     = box.getString("p_subj");
         String v_year     = box.getString("p_year");
         String v_subjseq  = box.getString("p_subjseq");
         
         Vector realfileVector = new Vector();
         Vector savefileVector = new Vector();
         Vector fileseqVector  = new Vector();
             
         try { 
             connMgr = new DBConnectionManager();

             list = new ArrayList();

             sql  = " select a.seq seq, a.userid userid, a.name name, a.title title, count(b.realfile) upfilecnt, decode(substr(a.gadmin,1,1), 'Z', 'ZZ', 'P', '����',  '���') gadmin, ";
             sql += "        a.indate indate, a.cnt cnt, a.refseq refseq, a.levels levels, a.position position, isopen, MAX(b.savefile) savefile, MAX(b.realfile) realfile, MAX(b.fileseq) fileseq, a.isimport  ";
             sql += "   from TZ_BOARD a, TZ_BOARDFILE b                                                         ";
             sql += "  where a.tabseq = b.tabseq( +)                                                             ";
             sql += "    and a.seq    = b.seq( +)                                                                ";
             sql += "    and a.tabseq = " + v_tabseq;

             if ( !v_searchtext.equals("") ) {                //    �˻�� ������
                 // v_pageno = 1;   //      �˻��� ��� ù��° �������� �ε��ȴ�

                 if ( v_search.equals("name") ) {              //    �̸����� �˻��Ҷ�
                     sql += " and a.name like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 }
                 else if ( v_search.equals("title") ) {        //    �������� �˻��Ҷ�
                     sql += " and a.title like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 }
                 else if ( v_search.equals("content") ) {     //    �������� �˻��Ҷ�
                     // sql += " and dbms_lob.instr(a.content, " + StringManager.makeSQL(v_searchtext) + ",1,1) < > 0";
                     sql += " and ( a.content like " + StringManager.makeSQL("%" + v_searchtext + "%") + " or a.content like " + StringManager.makeSQL("%" + v_searchtext) + " or a.content like " + StringManager.makeSQL( v_searchtext + "%") + ")";            //   Oracle 9i ��
                 }
                 else if ( v_search.equals("ldate") ) {     //    �ۼ��������� �˻��Ҷ�
                     sql += " and ( SUBSTR(a.indate, 1, 8) = " + StringManager.makeSQL( StringManager.replace(v_searchtext, ".", "") ) + " )  \n";            
                 }
                 else if ( v_search.equals("userid") ) {     //    ���̵�� �˻��Ҷ�
                     sql += " and ( a.userid  = " + StringManager.makeSQL(v_searchtext ) + " ) \n";            
                 }
             }
             
             sql +="   and a.subj = " + StringManager.makeSQL(v_subj)
	             + "   and a.year = " + StringManager.makeSQL(v_year)
	             + "   and a.subjseq = " + StringManager.makeSQL(v_subjseq);

             sql += " group by a.seq, a.userid, a.name, a.title, a.indate, a.cnt, a.refseq, a.levels, a.position, decode(substr(a.gadmin,1,1), 'Z', 'ZZ', 'P', '����',  '���'), isopen, a.isimport ";
             //sql += " order by a.isimport desc , a.refseq desc, position asc                                                       ";
             sql += " order by a.refseq desc, position asc                                                       ";
             
             ls = connMgr.executeQuery(sql);
             
             ls.setPageSize(admin_row);                       //  �������� row ������ �����Ѵ�
             ls.setCurrentPage(v_pageno);               //     ������������ȣ�� �����Ѵ�.
             int totalpagecount = ls.getTotalPage();    //     ��ü ������ ���� ��ȯ�Ѵ�
             int totalrowcount = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

             while ( ls.next() ) { 
                 dbox = ls.getDataBox();
                 
                 realfileVector = new Vector();
                 savefileVector = new Vector();
                 fileseqVector  = new Vector();

                 dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum() + 1));
                 dbox.put("d_totalpage", new Integer(totalpagecount));
                 dbox.put("d_rowcount", new Integer(admin_row));
                 
                 realfileVector.addElement( ls.getString("realfile") );
                 savefileVector.addElement( ls.getString("savefile") );
                 fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));
                 
                 if ( realfileVector != null ) dbox.put("d_realfile", realfileVector);
                 if ( savefileVector != null ) dbox.put("d_savefile", savefileVector);
                 if ( fileseqVector  != null ) dbox.put("d_fileseq", fileseqVector);            

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

    public ArrayList selectFaqList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
        int v_pageno = box.getInt("p_pageno");
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select a.title, a.content, b.realfile, b.savefile          \n"
                 + " from TZ_BOARD a, TZ_BOARDFILE b                            \n"
                 + " where a.tabseq = b.tabseq( +)                              \n"                                                                 
                 + " and a.seq    = b.seq( +)                                   \n"
                 + "    and a.tabseq = " + v_tabseq;

            if ( !v_searchtext.equals("") ) {                //    �˻�� ������
                sql += " and ( a.title like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                sql += " or ( a.content like " + StringManager.makeSQL("%" + v_searchtext + "%") + " or a.content like " + StringManager.makeSQL("%" + v_searchtext) + " or a.content like " + StringManager.makeSQL( v_searchtext + "%") + ") )";            //   Oracle 9i ��
            }
            sql += " order by a.ldate desc                                         ";
            
            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                       //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);               //     ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();    //     ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount", new Integer(row));
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
    
    public static String convertSQLInsaction(String v_userid, String v_contents) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;
        String              sql         = "";
        DataBox             dbox        = null;
        String              v_rtnvalue  = "";

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select COUNT(*) Cnt                            \n"
                 + " from   TZ_Manager                              \n"
                 + " WHERE  userid  = " + SQLString.Format(v_userid);
            
            //System.out.println(sql);
            
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) 
                dbox = ls.getDataBox();
            
            if ( dbox.getInt("d_cnt") > 0 )
                v_rtnvalue  = v_contents;
            else {
                v_contents  = v_contents.replaceAll("<", "&lt").replaceAll(">", "&gt");
                v_contents  = v_contents.replaceAll("\n", "<br>");
                
                if ( v_contents.indexOf("<br>") < 0 ) {
                    v_contents  = v_contents.replaceAll("\r\n", "<br>");
                }
                
                v_rtnvalue  = v_contents;
            }    
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return v_rtnvalue;
    }
    
    
    public static String convertGadminName(String v_userid) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;
        String              sql         = "";
        DataBox             dbox        = null;
        String              v_rtnvalue  = "";
        String              v_gadmin    = "";
        String              v_name      = "";

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select MIN(b.Gadmin) Gadmin, Max(a.name) name  \n"
                + " from   TZ_Member    a                           \n"
                + "     ,  TZ_Manager   b                           \n"
                + " WHERE  a.userid  = " + SQLString.Format(v_userid) + " \n"
                + " AND    a.userid  = b.userid(+)                  \n";
            
//            System.out.println(sql);
            
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                v_gadmin = StringManager.substring(dbox.getString("d_gadmin"),0,1);
                v_name   = dbox.getString("d_name"  );
            }    
            
            if ( v_gadmin.equals("P") )
                v_rtnvalue              = "[����]<br>";
            else if ( v_gadmin.equals("") )
                v_rtnvalue              = "";
            else     
                v_rtnvalue              = "[���]<br>";
            
            v_rtnvalue += v_name;
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return v_rtnvalue;
    }
    

    /**
    * ���õ� �Խù� ��ȸ�� ����
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int updateCnt(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;

        int v_tabseq = box.getInt("p_tabseq");
        int v_seq = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql = "update tz_board set cnt = nvl(cnt,0) + 1 where tabseq = ? and seq = ? ";
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setInt(1, v_tabseq);
            pstmt.setInt(2, v_seq);
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            if ( isOk > 0 ) { 
                connMgr.commit();
            } else connMgr.rollback();

        }
        catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;

    }    
    
    /**
     * �Խ��� ����
     * @param box
     * @return int v_tabseq
     * @throws Exception
     */
    public int InsertBds(RequestBox box) throws Exception {
    	DBConnectionManager connMgr		= null;
        PreparedStatement   pstmt       = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 0;
        
        ListSet             ls          = null;
        int					v_tabseq    = 0;
        int					v_cnt		= 0;
        
        try { 
			connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            String v_bdType = box.getString("type");
            String v_bdSdesc = box.getString("sdesc");
            
            sbSQL.setLength(0);
            sbSQL.append(" select count(*) as cnt ");
            sbSQL.append(" from   tz_bds ");
            sbSQL.append(" where  type = " + StringManager.makeSQL(v_bdType));
            ls              = connMgr.executeQuery(sbSQL.toString());
            if ( ls.next() ) { 
            	v_cnt    = ls.getInt(1);
            }
            
            if (v_cnt > 0) {
                
                sbSQL.setLength(0);
                sbSQL.append(" select tabseq ");
                sbSQL.append(" from   tz_bds ");
                sbSQL.append(" where  type = " + StringManager.makeSQL(v_bdType));
                ls              = connMgr.executeQuery(sbSQL.toString());
                if ( ls.next() ) { 
                	v_tabseq    = ls.getInt(1);
                }
                
            } else {

            	sbSQL.setLength(0);
	            sbSQL.append(" select nvl(max(tabseq), 0) + 1 from tz_bds ");
	            ls              = connMgr.executeQuery(sbSQL.toString());
	
	            if ( ls.next() ) { 
	                v_tabseq    = ls.getInt(1);
	            } else { 
	                v_tabseq    = 1;
	            }
	            
	            // StringBuffer �ʱ�ȭ    
	            sbSQL.setLength(0);
	
	            // insert tz_bds table
	            sbSQL.append(" insert into tz_bds                               \n")
	                 .append(" (                                                \n")
	                 .append("         tabseq  , type  , grcode    , comp       \n")
	                 .append("     ,   subj    , year  , subjseq   , sdesc      \n")
	                 .append("     ,   ldesc   , status, luserid   , ldate      \n")
	                 .append(" ) values (                                       \n")
	                 .append("         ?       , ?     , ?         , ?          \n")
	                 .append("     ,   ?       , ?     , ?         , ?          \n")
	                 .append("     ,   ?       , ?     , ?         , ?          \n")
	                 .append(" )                                                \n");
	            
	            pstmt           = connMgr.prepareStatement(sbSQL.toString());
	
	            pstmt.setInt   ( 1, v_tabseq    );
	            pstmt.setString( 2, v_bdType    ); // �Խ��� Ÿ��
	            pstmt.setString( 3, "0000000"   );
	            pstmt.setString( 4, "0000000000");
	            pstmt.setString( 5, "00000"     );
	            pstmt.setString( 6, "0000"      );
	            pstmt.setString( 7, "0000"      );
	            pstmt.setString( 8, v_bdSdesc   );
	            pstmt.setString( 9, ""          );
	            pstmt.setString(10, "A"         );
	            pstmt.setString(11, "SYSTEM"    );
	            pstmt.setString(12, FormatDate.getDate("yyyyMMddHHmmss") );
	
	            isOk = pstmt.executeUpdate();
	            
	            if ( pstmt != null ) { pstmt.close(); }

	            if (isOk == 1) {
	                connMgr.commit();
	            } else {
	                connMgr.rollback();
	            }
                
	        }
        } catch ( Exception e ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e1 ) { } 
            }
            
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
            if ( connMgr != null ) { 
            	try { 
            		connMgr.freeConnection(); 
            	}catch (Exception e10) {} 
            }            
        }
        
        return v_tabseq;
    }

    /***************************** ���հ��� �Խ��� �߰� ******************************/
    
    /**
     * ���հ��� ���� ���� ����Ʈ
     * @param    box      receive from the form object and session
     * @return ArrayList  
     * @throws Exception
     */
     public ArrayList selectOffBoardNotice(RequestBox box) throws Exception {
         DBConnectionManager connMgr = null;
         ListSet ls = null;
         ArrayList list = null;
         StringBuffer sql = new StringBuffer();
         DataBox dbox = null;

         int v_pageno = box.getInt("p_pageno");
         
         String v_searchtext = box.getString("p_searchtext");
         String v_search     = box.getString("p_search");
         
         String v_subj		= box.getString("p_subj");
         String v_year		= box.getString("p_year");
         String v_subjseq	= box.getString("p_subjseq");

         try {
             connMgr = new DBConnectionManager();

             list = new ArrayList();

             sql.append(" SELECT subj, year, subjseq, seq, types, title, adcontent, a.userid, a.ldate, b.name \n" +
             			" FROM TZ_GONG a, TZ_MEMBER b \n" +
             			" WHERE a.userid = b.userid \n" +
             			"	  	and subj = " + StringManager.makeSQL(v_subj) + " \n" +
             			"		and year = " + StringManager.makeSQL(v_year) + " \n" +
             			"		and subjseq = " + StringManager.makeSQL(v_subjseq) );

             if ( !v_searchtext.equals("")) {                //    �˻�� ������
                 //v_pageno = 1;   //      �˻��� ��� ù��° �������� �ε��ȴ�

                 if (v_search.equals("name")) {              //    �̸����� �˻��Ҷ�
                     sql.append(" and b.name like lower(" + StringManager.makeSQL("%" + v_searchtext + "%")+")");
                 }
                 else if (v_search.equals("title")) {        //    �������� �˻��Ҷ�
                     sql.append(" and a.title like lower(" + StringManager.makeSQL("%" + v_searchtext + "%")+")");
                 }
                 else if (v_search.equals("content")) {     //    �������� �˻��Ҷ�
                     //sql += " and dbms_lob.instr(a.content, " + StringManager.makeSQL(v_searchtext) + ",1,1) <> 0";
                     sql.append(" and a.adcontent like " + StringManager.makeSQL("%" + v_searchtext + "%") );
                 }
             }
             
             sql.append(" ORDER BY a.seq desc");


             ls = connMgr.executeQuery(sql.toString());
             ls.setPageSize(row);                       //  �������� row ������ �����Ѵ�
             ls.setCurrentPage(v_pageno);               //     ������������ȣ�� �����Ѵ�.
             int totalpagecount = ls.getTotalPage();    //     ��ü ������ ���� ��ȯ�Ѵ�
             int totalrowcount = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

             while (ls.next()) {
                 dbox = ls.getDataBox();

                 dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum() + 1));
                 dbox.put("d_totalpage", new Integer(totalpagecount));
                 dbox.put("d_rowcount", new Integer(row));
                 list.add(dbox);
             }
         }
         catch (Exception ex) {
             ErrorManager.getErrorStackTrace(ex, box, sql.toString());
             throw new Exception("sql = " + sql.toString() + "\r\n" + ex.getMessage());
         }
         finally {
             if(ls != null) { try { ls.close(); }catch (Exception e) {} }
             if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
         }
         return list;
     }
     
     /**
      * ���ο� �ڷ�� ���� ���� ���
      * @param box      receive from the form object and session
      * @return isOk    1:insert success,0:insert fail
      * @throws Exception
      */
    public int courseinsertBoard(RequestBox box) throws Exception {
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        SubjGongData  data = null;
        String sql  = "";
        String sql1 = "";
        int isOk = 0;

        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_year");
        String v_subjseq   = box.getString("p_subjseq");

        String v_types     = box.getString("p_types");
        String v_title    = box.getString("p_title");

        String v_content   = box.getString("p_content");
        String s_userid    = box.getSession("userid");
        String s_gadmin    = box.getSession("gadmin");
        String v_isimport  = box.getStringDefault("p_isimport", "N");

        System.out.println("*******************************************  "+v_content);
        
        // String v_title= "";
        // String v_adcontent= "";

        String v_userid    = "";
        int    v_seq       = 0;
        int idx = 1;

        try {

            data = getChangeText(v_subj, v_year, v_subjseq, v_title, v_content );
            if ( data != null ) {
                v_title     = data.getTitle();
                v_content = data.getAdcontent();
                v_userid    = data.getUserid();
            }

            v_userid        = s_userid;

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql  = " select max(seq) from TZ_GONG  ";
            sql += "  where subj    = " + StringManager.makeSQL(v_subj);
            sql += "    and year    = " + StringManager.makeSQL(v_year);
            sql += "    and subjseq = " + StringManager.makeSQL(v_subjseq);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) {
                v_seq = ls.getInt(1) + 1;
            } else {
                v_seq = 1;
            }
            
            if ( ls != null ) ls.close();

            sql1 =  " insert into TZ_GONG(subj, year, subjseq, seq, types, addate, title, userid, adcontent, luserid, ldate, gadmin, isimport )                     ";
            sql1 += "             values (?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?) ";

            pstmt = connMgr.prepareStatement(sql1);
            
            idx = 1;
            pstmt.setString(idx++, v_subj);
            pstmt.setString(idx++, v_year);
            pstmt.setString(idx++, v_subjseq);
            pstmt.setInt(idx++, v_seq);
            pstmt.setString(idx++, v_types);
            pstmt.setString(idx++, v_title);
            pstmt.setString(idx++, v_userid);
            pstmt.setString(idx++, v_content);
            
            pstmt.setString(idx++, s_userid);
            pstmt.setString(idx++, s_gadmin);
            pstmt.setString(idx++, v_isimport);

            isOk = pstmt.executeUpdate();

           
            if ( pstmt != null ) pstmt.close();

            if ( isOk > 0) connMgr.commit();
           else connMgr.rollback();

        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
    
    
    
    /**
     * ������ ��ġ(���� �� ����)
     * @param subj       �����ڵ�
     * @param year       ����⵵
     * @param subjseq    ������
     * @param title      ��������
     * @param adcontent  ��������
     * @return data      SubjGongData (��������Ÿ��- ������ ����,Ÿ��Ʋ ����)
     * @throws Exception
     */
    public SubjGongData getChangeText (String subj, String year, String subjseq, String title, String adcontent) throws Exception {
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        SubjGongData  data = null;

        String v_subjnm     = "";   // �����
        String v_edudates   = "";   // �����ϼ�
        String v_eduperiod  = "";   // �����Ⱓ
        String v_name       = "";   // ��� �̸�
        String v_comptel    = "";   // ��� ��ȭ
        String v_email      = "";   // ��� E-mail
        String v_wstep      = "";   // ����ġ(������)
        String v_wmtest     = "";   // ����ġ(�߰���)
        String v_wftest     = "";   // ����ġ(������)
        String v_whtest     = "";   // ����ġ(������)
        String v_wreport    = "";   // ����ġ(������Ʈ)
        String v_wact       = "";   // ����ġ(��Ƽ��Ƽ)
        String v_gradscore  = "";   // �����������
        String v_gradstep   = "";   // �������������
        String v_gradexam   = "";   // ������ؽ���
        String v_gradreport = "";   // ������ذ���
        String v_point      = "";   // ��������
        String v_edustart   = "";   // ������
        String v_eduend     = "";   // ������

        String v_userid     = "";

        try {
            connMgr = new DBConnectionManager();

            sql  = " select a.subjnm subjnm, ";
            sql += "        a.point point, ";
            sql += "        a.gradscore gradscore, ";
            sql += "        a.gradstep gradstep, ";
            sql += "        a.gradexam gradexam, ";
            sql += "        a.gradreport gradreport,";
            sql += "        a.wstep wstep, ";
            sql += "        a.wmtest wmtest,";
            sql += "        a.wftest wftest, ";
            sql += "        a.whtest whtest, ";
            sql += "        a.wreport wreport, ";
            sql += "        a.wact wact, ";
            sql += "        a.edustart edustart,     ";
            sql += "        a.eduend eduend, ";
            sql += "        b.muserid userid, ";
            sql += "        (select name from tz_member where userid=b.muserid) name, ";
            sql += "        (select email from tz_member where userid=b.muserid) email, ";
            sql += "        '' comptel ";
            sql += " from   tz_subjseq a, TZ_SUBJ b ";
            sql += " where  a.subj = b.subj  ";
            sql += "    and a.subj    = " + StringManager.makeSQL(subj);
            sql += "    and a.year    = " + StringManager.makeSQL(year);
            sql += "    and a.subjseq = " + StringManager.makeSQL(subjseq);

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) {

                v_subjnm     = ls.getString("subjnm");
                v_point      = String.valueOf( ls.getInt("point") );
                v_gradscore  = ls.getString("gradscore");
                v_gradstep   = ls.getString("gradstep");
                v_gradexam   = ls.getString("gradexam");
                v_gradreport = ls.getString("gradreport");
                v_wstep      = String.valueOf( ls.getInt("wstep") );
                v_wmtest     = String.valueOf( ls.getInt("wmtest") );
                v_wftest     = String.valueOf( ls.getInt("wftest") );
                v_whtest     = String.valueOf( ls.getInt("whtest") );
                v_wreport    = String.valueOf( ls.getInt("wreport") );
                v_wact       = String.valueOf( ls.getInt("wact") );

                v_edustart   = ls.getString("edustart");
                v_eduend     = ls.getString("eduend");
                v_edudates   = String.valueOf(FormatDate.datediff("date", v_edustart, v_eduend));

                v_edustart   = FormatDate.getFormatDate(v_edustart,"yyyy.MM.dd");
                v_eduend     = FormatDate.getFormatDate(v_eduend, "yyyy.MM.dd");
                v_eduperiod  = v_edustart + " ~ " + v_eduend;

                v_userid     = ls.getString("userid");

                v_name       = ls.getString("name");
                v_comptel    = ls.getString("comptel");
                v_email      = ls.getString("email");

                // Ÿ��Ʋ ����
                title = StringManager.replace(title, "v_subjnm", v_subjnm);
                title = StringManager.replace(title, "v_point", v_point);
                title = StringManager.replace(title, "v_gradscore", v_gradscore);
                title = StringManager.replace(title, "v_gradstep", v_gradstep);
                title = StringManager.replace(title, "v_gradexam", v_gradexam);
                title = StringManager.replace(title, "v_gradreport", v_gradreport);
                title = StringManager.replace(title, "v_wstep", v_wstep);
                title = StringManager.replace(title, "v_wmtest", v_wmtest);
                title = StringManager.replace(title, "v_wftest", v_wftest);
                title = StringManager.replace(title, "v_whtest", v_whtest);
                title = StringManager.replace(title, "v_wreport", v_wreport);
                title = StringManager.replace(title, "v_wact", v_wact);
                title = StringManager.replace(title, "v_edustart", v_edustart);
                title = StringManager.replace(title, "v_eduend", v_eduend);
                title = StringManager.replace(title, "v_edudates", v_edudates);
                title = StringManager.replace(title, "v_eduperiod", v_eduperiod);
                title = StringManager.replace(title, "v_name", v_name);
                title = StringManager.replace(title, "v_comptel", v_comptel);
                title = StringManager.replace(title, "v_email", v_email);

                // ���� ����
                adcontent = StringManager.replace(adcontent, "v_subjnm", v_subjnm);
                adcontent = StringManager.replace(adcontent, "v_point", v_point);
                adcontent = StringManager.replace(adcontent, "v_gradscore", v_gradscore);
                adcontent = StringManager.replace(adcontent, "v_gradstep", v_gradstep);
                adcontent = StringManager.replace(adcontent, "v_gradexam", v_gradexam);
                adcontent = StringManager.replace(adcontent, "v_gradreport", v_gradreport);
                adcontent = StringManager.replace(adcontent, "v_wstep", v_wstep);
                adcontent = StringManager.replace(adcontent, "v_wmtest", v_wmtest);
                adcontent = StringManager.replace(adcontent, "v_wftest", v_wftest);
                adcontent = StringManager.replace(adcontent, "v_whtest", v_whtest);
                adcontent = StringManager.replace(adcontent, "v_wreport", v_wreport);
                adcontent = StringManager.replace(adcontent, "v_wact", v_wact);
                adcontent = StringManager.replace(adcontent, "v_edustart", v_edustart);
                adcontent = StringManager.replace(adcontent, "v_eduend", v_eduend);
                adcontent = StringManager.replace(adcontent, "v_edudates", v_edudates);
                adcontent = StringManager.replace(adcontent, "v_eduperiod", v_eduperiod);
                adcontent = StringManager.replace(adcontent, "v_name", v_name);
                adcontent = StringManager.replace(adcontent, "v_comptel", v_comptel);
                adcontent = StringManager.replace(adcontent, "v_email", v_email);


                data = new SubjGongData();
                data.setTitle(title);
                data.setAdcontent(adcontent);
                data.setUserid(v_userid);
            }
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data;
    }
    
    /**
     * ���õ� �ڷ�� �Խù� �󼼳��� select
     * @param box          receive from the form object and session
     * @return ArrayList   ��ȸ�� ������
     * @throws Exception
     */
     public DataBox courseselectBoard(RequestBox box) throws Exception {
          DBConnectionManager connMgr = null;
          ListSet ls = null;
          String sql = "";
          DataBox dbox = null;
          String v_upcnt = "Y";

          int v_tabseq    = box.getInt("p_tabseq");
          int v_seq       = box.getInt("p_seq");
          //int v_upfilecnt = (box.getInt("p_upfilecnt")>0?box.getInt("p_upfilecnt"):1);
          String v_subj    = box.getString("p_subj");
          String v_year    = box.getString("p_year");
          String v_subjseq = box.getString("p_subjseq");
          if(v_subj.equals("")){     v_subj = box.getSession("s_subj");          }
          if(v_year.equals("")){     v_year = box.getSession("s_year");          }
          if(v_subjseq.equals("")){  v_subjseq = box.getSession("s_subjseq");    }


          //String [] realfile = new String [v_upfilecnt];
          //String [] savefile= new String [v_upfilecnt];
          //int [] fileseq = new int [v_upfilecnt];
          Vector realfileVector = new Vector();
          Vector savefileVector = new Vector();
          Vector fileseqVector  = new Vector();
          try {
              connMgr = new DBConnectionManager();

              sql =" SELECT a.subj subj, a.year year, a.subjseq subjseq, a.seq seq, a.types types, a.title title, a.adcontent adcontent, a.userid userid, b.name name, a.ldate ldate \n";
          	 sql +=" FROM TZ_GONG a, tz_member b \n";
          	 sql +=" WHERE a.userid = b.userid and subj = " + StringManager.makeSQL(v_subj);  
          	 sql +="		and year = " + StringManager.makeSQL(v_year);
          	 sql +="		and subjseq = " + StringManager.makeSQL(v_subjseq); 
          	 sql += " and seq = " + v_seq;
          	 
          	 System.out.println(sql);
          	 
              ls = connMgr.executeQuery(sql);

              for (int i = 0; ls.next(); i++) {

                  dbox = ls.getDataBox();
              }

              if (!v_upcnt.equals("N")){
                  //connMgr.executeUpdate("update TZ_GONG set cnt = cnt + 1 where  subjseq = " + v_subjseq + " and seq = "+ v_seq);
              }
          }
          catch (Exception ex) {
              ErrorManager.getErrorStackTrace(ex, box, sql);
              throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
          }
          finally {
              if(ls != null) {try {ls.close();} catch(Exception e){}}
              if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
          }
          return dbox;
      }
    /***************************** ���հ��� �Խ��� �߰� ******************************/
    
}