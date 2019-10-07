// **********************************************************
//  1. ��      ��: ���İ��� �Խ��� ����
//  2. ���α׷���: KnowBoardUserBean.java
//  3. ��      ��: ���İ��� ����
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0 QnA
//  6. ��      ��: ������ 2005. 9. 1
//  7. ��      ��:
// **********************************************************
package com.ziaan.homepage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KnowBoardUserBean { 
    
    public KnowBoardUserBean() { 
        
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        �� ����� �������� row ���� �����Ѵ�
        } catch( Exception e ) { 
            e.printStackTrace();
        }          
    }
    
    private ConfigSet config;   
	private static final String	BOARD_TYPE = "KB";     
	private static final int    BOARD_TABSEQ = 7; 
	private	static final String	FILE_TYPE =	"p_file";			// ���Ͼ��ε�Ǵ� tag name
	private	static final int    FILE_LIMIT	= 5;					// �������� ���õ� ����÷�� ����
    private int row;


    /**
    * ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   QNA ����Ʈ
    * @throws Exception
    */
    public ArrayList SelectKnowLatestList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        String sql1 = "";
        DataBox             dbox    = null;

        String s_grcode = box.getStringDefault("tem_grcode", box.getSession("tem_grcode") );     
        String v_listgubun = box.getString("p_listgubun"); // ����Ʈ ����
        String v_categorycd = box.getString("p_categorycd"); // ī�װ���
                
        String v_searchtext = box.getString("p_searchtext");
        String v_select = box.getString("p_select");
        // String v_categorycd = box.getStringDefault("p_categorycd", "00");
        int v_pageno        = box.getInt("p_pageno");

        // int v_tabseq = box.getInt("p_tabseq");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
 

			sql += " select a.seq, a.types, a.title, a.contents, a.indate, a.inuserid, a.ldate, b.name,a.cnt, nvl(a.recommend, 0) recommend ,  ";     
			sql += "		(select classname from tz_knowcode where subjclass=a.categorycd  and grcode=a.grcode) categorynm,  ";
			sql += "		(select count(realfile) from tz_homefile where tabseq = a.TABSEQ and seq = a.seq ) filecnt, ";
			sql += "        (select count(commentseq) ccnt from TZ_COMMENTQNA where tabseq='" +BOARD_TABSEQ + "' and seq=a.seq) commentcnt    ";
            sql += "   from TZ_HOMEQNA a, tz_member b";
            sql += "  where a.inuserid = b.userid( +)  and a.grcode='" +s_grcode + "'  ";
            sql += "  and tabseq = " + BOARD_TABSEQ;

            // �ֱ����� ����Ʈ              
            if ( !v_categorycd.equals("") ) { 
                sql += "  and categorycd = '" +v_categorycd + "' ";
            }
                
            // �α����� ����Ʈ    
            if ( v_listgubun.equals("") ) {  
                if ( !v_searchtext.equals("") ) {      //    �˻�� ������
                    v_pageno = 1;   //      �˻��� ��� ù��° �������� �ε��ȴ�                      
                   if ( v_select.equals("title") ) {     //    �������� �˻��Ҷ�
                        sql += " and lower(title) like lower ( " + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                    }
                    else if ( v_select.equals("content") ) {     //    �������� �˻��Ҷ�
                        sql += " and contents like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i ��
                    }
                    else if ( v_select.equals("name") ) {     //    �̸����� �˻��Ҷ�
                        sql += " and lower(name) like lower (" +  StringManager.makeSQL("%" + v_searchtext + "%") + ")";            //   Oracle 9i ��
                    }
                }
                sql += " order by seq desc ";
                
            } else if ( v_listgubun.equals("popcnt_list") ) { // *** �ִ���ȸ
                sql += " order by cnt desc ";
            } else if ( v_listgubun.equals("poprec_list") ) { // *** �ִ���õ
                sql += " order by recommend desc ";                
            } else if ( v_listgubun.equals("popcom_list") ) { // *** �ִٴ��
                sql += " order by commentcnt desc ";
            }

            ls = connMgr.executeQuery(sql);
System.out.println("-----------------" +sql);

            ls.setPageSize(row);             //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);                    //     ������������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();       //     ��ü ������ ���� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

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
    ī�װ� Ʈ�� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList   ����Ʈ
    */
    public ArrayList SelectCategoryTreeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList list1 = null;
        String sql  = "";
        DataBox             dbox    = null;
        String s_grcode = box.getStringDefault("tem_grcode", box.getSession("tem_grcode") );  
        try { 
            // dir           :���丮����[true/false] 
            // menuno        :�ڵ� subjclass
            // target        :���������
            // desc          :�ڵ�� classname
            // root          :ROOT�ڵ�
            // parent        :PARENT�ڵ�
            // level         :Ʈ����ġ(0����)
            // subcnt        :�ڽİ���
            // viewname      :ī�װ����� ��
                        
            sql = " select a.subjclass, a.classname, RPAD(a.upperclass,9,'0') root, ";
            sql += "       (case when a.middleclass='000' and  a.lowerclass='000' then a.subjclass  ";
            sql += "        when a.middleclass != '000' and  a.lowerclass='000' then RPAD(a.upperclass,9,'0') ";
            sql += "        else RPAD(a.upperclass||a.middleclass,9,'0') ";
            sql += "        end) parent, ";
            sql += "       (case when a.middleclass='000' and  a.lowerclass='000' then 0 ";
            sql += "        when a.middleclass != '000' and  a.lowerclass='000' then 1 ";
            sql += "        else 2 ";
            sql += "        end) levels, ";
            sql += "       (case when a.middleclass='000' and  a.lowerclass='000'  "; // --��з�
            sql += "         then (select count(*) from tz_knowcode where upperclass=a.upperclass and middleclass != '000' and lowerclass='000' and grcode='" +s_grcode + "')  ";
            sql += "        when a.middleclass != '000' and  a.lowerclass='000'      "; // --�ߺз�
            sql += "         then (select count(*) from tz_knowcode where upperclass=a.upperclass and middleclass=a.middleclass and lowerclass != '000' and grcode='" +s_grcode + "')  ";
            sql += "        else 0                                                 "; // --�Һз�
            sql += "        end ";
            sql += "       ) subcnt, ";
            sql += "       (case when a.middleclass='000' and  a.lowerclass='000' then a.classname  ";
            sql += "        when a.middleclass != '000' and  a.lowerclass='000'  ";
            sql += "         then (select classname from tz_knowcode where  grcode='" +s_grcode + "' and subjclass=a.upperclass||'000000')||'>'||a.classname  ";
            sql += "        else  ";
            sql += "         (select classname from tz_knowcode where  grcode='" +s_grcode + "' and subjclass=a.upperclass||'000000')||'>'||(select classname from tz_knowcode where  grcode='" +s_grcode + "' and subjclass=a.upperclass||a.middleclass||'000')||'>'||a.classname  ";
            sql += "        end  ";
            sql += "        ) viewname  ";          
            sql += "from tz_knowcode a where a.grcode='" +s_grcode + "' order by subjclass ";


            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox=ls.getDataBox();
                list1.add(dbox);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }

    /**
    �޴� ī�װ� Ʈ�� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList   ����Ʈ
    */
    public static ArrayList SelectMenuCategoryTreeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList list1 = null;
        String sql  = "";
        DataBox             dbox    = null;

        String s_grcode = box.getStringDefault("tem_grcode", box.getSession("tem_grcode") );
    
        try { 
            // dir           :���丮����[true/false] 
            // menuno        :�ڵ� subjclass
            // target        :���������
            // desc          :�ڵ�� classname
            // root          :ROOT�ڵ�
            // parent        :PARENT�ڵ�
            // level         :Ʈ����ġ(0����)
            // subcnt        :�ڽİ���
            // viewname      :ī�װ����� ��
                
            sql = " select a.subjclass, a.classname, RPAD(a.upperclass,9,'0') root, ";
            sql += "       (case when a.middleclass='000' and  a.lowerclass='000' then a.subjclass  ";
            sql += "        when a.middleclass != '000' and  a.lowerclass='000' then RPAD(a.upperclass,9,'0') ";
            sql += "        else RPAD(a.upperclass||a.middleclass,9,'0') ";
            sql += "        end) parent, ";
            sql += "       (case when a.middleclass='000' and  a.lowerclass='000' then 0 ";
            sql += "        when a.middleclass != '000' and  a.lowerclass='000' then 1 ";
            sql += "        else 2 ";
            sql += "        end) levels, ";
            sql += "       (case when a.middleclass='000' and  a.lowerclass='000'  "; // --��з�
            sql += "         then (select count(*) from tz_knowcode where upperclass=a.upperclass and middleclass != '000' and lowerclass='000' and grcode='" +s_grcode + "')  ";
            sql += "        when a.middleclass != '000' and  a.lowerclass='000'      "; // --�ߺз�
            sql += "         then (select count(*) from tz_knowcode where upperclass=a.upperclass and middleclass=a.middleclass and lowerclass != '000' and grcode='" +s_grcode + "')  ";
            sql += "        else 0                                                 "; // --�Һз�
            sql += "        end ";
            sql += "       ) subcnt, ";
            sql += "       (case when a.middleclass='000' and  a.lowerclass='000'then a.classname  ";
            sql += "        when a.middleclass != '000' and  a.lowerclass='000'  ";
            sql += "         then (select classname from tz_knowcode where  grcode='" +s_grcode + "' and subjclass=a.upperclass||'000000')||'>'||a.classname  ";
            sql += "        else  ";
            sql += "         (select classname from tz_knowcode where  grcode='" +s_grcode + "' and subjclass=a.upperclass||'000000')||'>'||(select classname from tz_knowcode where  grcode='" +s_grcode + "' and subjclass=a.upperclass||a.middleclass||'000')||'>'||a.classname  ";
            sql += "        end  ";
            sql += "        ) viewname  ";          
            sql += "from tz_knowcode a  where a.grcode='" +s_grcode + "' ";
            sql +=" order by subjclass ";

// Log.info.println(sql);

            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox=ls.getDataBox();
                list1.add(dbox);
            }

        } catch ( Exception ex ) { 
            // ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }

    /**
    * ����Ҷ�(����)
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */

     public int insertKnowBoard(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement pstmt1 = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int v_cnt = 0;
        String v_catecd = box.getString("p_catecd");      // ī�װ�   
        String v_title  = box.getString("p_title");
        String v_contents =  StringManager.replace(box.getString("content"),"<br > ","\n");
        String v_types   = "0";
        String s_userid = "";
        String s_usernm = box.getSession("name");
        String s_gadmin = box.getSession("gadmin");
        // String v_type    = box.getStringDefault("p_type", "HQ");
        String s_grcode = box.getSession("tem_grcode");
    //  if ( s_gadmin.equals("A1") ) { 
    //      s_userid = "���";
    //  } else { 
            s_userid = box.getSession("userid");
    //  }
        String v_isopen  = "Y";
 //       Vector newFileNames = box.getNewFileNames("p_file");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            // ----------------------   ��Խ�������������  ������ tabseq�� �����Ѵ� ----------------------------
            // sql = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);
            // ls = connMgr.executeQuery(sql);
            // ls.next();
            // int v_tabseq = ls.getInt(1);
            // ls.close();
            // ------------------------------------------------------------------------------------
            // ----------------------   �Խ��� ��ȣ �����´� ----------------------------
            sql = "select nvl(max(seq), 0) from TZ_HOMEQNA where tabseq = '" +BOARD_TABSEQ + "'";
            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_seq = ls.getInt(1) +1;
            ls.close();

            //// //// //// //// //// //// //// //// //   �Խ��� table �� �Է�  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql1 =  "insert into TZ_HOMEQNA(tabseq, seq, types, title, contents, indate, inuserid, isopen, luserid, ldate, grcode, cnt, categorycd)                      ";
            sql1 += "                values (?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?,?,?) ";
//            sql1 += "                values (?, ?, ?, ?, empty_clob(), to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?,?,?) ";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, BOARD_TABSEQ);
            pstmt1.setInt(2, v_seq);
            pstmt1.setString(3, v_types);
            pstmt1.setString(4, v_title);
            pstmt1.setString(5, v_contents);
            pstmt1.setString(6,  s_userid);
            pstmt1.setString(7,  v_isopen);
            pstmt1.setString(8,  s_userid);
            pstmt1.setString(9,  s_grcode);
            pstmt1.setInt(10, v_cnt);
            pstmt1.setString(11,  v_catecd);            

            isOk1 = pstmt1.executeUpdate();     //      ���� �ش� content �� empty_clob()�� �����ϰ� ���� ���� ��Ʈ������ ġȯ�Ѵ�.

            sql2 = "select contents from tz_HOMEQNA where tabseq = " + BOARD_TABSEQ + " and  seq = " + v_seq;
//            connMgr.setOracleCLOB(sql2, v_contents);       //      (��Ÿ ���� ���)

            isOk2 = this.insertUpFile(connMgr, BOARD_TABSEQ, v_seq, v_types, box);

            if ( isOk1 > 0 && isOk2 > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            }
			
        } catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1*isOk2;
    }

 

    /**
    * ���õ� �ڷ�� �Խù� �󼼳��� select
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */

   public DataBox SelectView(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_seq = box.getInt("p_seq");

        String v_types = box.getString("p_types");

        String v_fileseq = box.getString("p_fileseq");
		int	v_upfilecnt	 = (box.getInt("p_upfilecnt") > 0?box.getInt("p_upfilecnt"):1);

		Vector realfileVector = new Vector();
		Vector savefileVector = new Vector();
		Vector fileseqVector  = new Vector();  

		int	[] fileseq = new int [v_upfilecnt];

        try { 
            connMgr = new DBConnectionManager();

            sql = "select a.types, a.seq, a.inuserid, a.title, a.contents, a.categorycd, b.fileseq, b.realfile, b.savefile, a.indate ,c.name, a.cnt, nvl(a.recommend,0) recommend ";
            sql += " from tz_homeqna a, tz_homefile b, tz_member c";
            sql += " where a.tabseq = b.tabseq( +) and a.seq = b.seq( +) and a.types = b.types( +) ";
            sql += " and a.inuserid = c.userid( +) ";
            sql += "and a.tabseq = '" + BOARD_TABSEQ + "' and a.seq = " +v_seq + " and a.types = " + SQLString.Format(v_types); 

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
				fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));
            }
            
            String s_categorynm = SelectCategoryPathnm(connMgr,dbox.getString("d_categorycd") );
            dbox.put("d_categorynm", s_categorynm);

			if ( realfileVector 	 != null ) dbox.put("d_realfile", realfileVector);
			if ( savefileVector 	 != null ) dbox.put("d_savefile", savefileVector);
			if ( fileseqVector 	 != null ) dbox.put("d_fileseq", fileseqVector);

			sql = "update tz_homeqna set cnt = cnt + 1 where tabseq = " + BOARD_TABSEQ + " and seq = " +v_seq + " and types = " + SQLString.Format(v_types); 
            connMgr.executeUpdate(sql);

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
    * ���õ� �ڷ�� �Խù� �󼼳��� select
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws String
    */

   public String SelectCategoryPathnm(DBConnectionManager connMgr, String categorycd) throws Exception { 
        ListSet             ls      = null;
        String              sql     = "";
        String upper  = "";
        String middle = "";
        String lower  = "";
        String result = "";

        try { 
            upper  = categorycd.substring(0,3);
            middle = categorycd.substring(3,6);
            lower  = categorycd.substring(6,9);                        

            // ��з�
            sql = "select classname from tz_knowcode where upperclass='" +upper + "' and middleclass='000'";
            ls = connMgr.executeQuery(sql);
            
            if ( ls.next() ) { result = ls.getString("classname"); System.out.println(" >>  >>  > " +result); }
            
            // �ߺз�
            if ( !middle.equals("000") ) { 
                ls.close();
                result = result + " > ";
                sql = "select classname from tz_knowcode where upperclass='" +upper + "' and middleclass='" +middle + "' and lowerclass='000' ";
                ls = connMgr.executeQuery(sql);
                if ( ls.next() ) { result += ls.getString("classname"); }                
            }
            
            // �Һз�
            if ( !lower.equals("000") ) { 
                ls.close();
                result = result + " > ";
                sql = "select classname from tz_knowcode where upperclass='" +upper + "' and middleclass='" +middle + "' and lowerclass='" +lower + "' ";
                ls = connMgr.executeQuery(sql);
                if ( ls.next() ) { result += ls.getString("classname"); }                
            }

        } catch ( Exception ex ) { 
            // ErrorManager.getErrorStackTrace(ex, box, sql);
            // throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
            System.out.println(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
        }

        return result;
    }
    
    
     

    /**
    * ������ ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   
    * @throws Exception
    */
    public ArrayList selectcommentList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        String sql1 = "";
        DataBox             dbox    = null;

        String v_searchtext = box.getString("p_searchtext");
        String v_select = box.getString("p_select");
        int v_pageno        = box.getInt("p_pageno");
        int v_seq        = box.getInt("p_seq");
        // int v_tabseq = box.getInt("p_tabseq");

        String v_types = box.getString("p_types");
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            // seq, types, title, contents, indate, inuserid, upfile, isopen, luserid, ldate
            sql += " select a.seq,a.types,a.commentseq,a.inuserid,a.commentqna,a.cdate, b.name ";
            sql += "   from TZ_COMMENTQNA a, tz_member b ";
            sql += "  where a.inuserid = b.userid( +) ";
            sql += "  and tabseq = " +BOARD_TABSEQ;
            sql += "  and seq = " + v_seq;
            sql += "  and types = " + v_types;

            sql += " order by seq desc, types asc, commentseq asc ";
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(row);             //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);                    //     ������������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();       //     ��ü ������ ���� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
                dbox = ls.getDataBox();

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
    * �����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteBoard(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int isOk3 = 1;

        int    v_seq     = box.getInt("p_seq");
        String v_types   = box.getString("p_types");
        Vector savefile  = box.getVector("p_savefile");
        String v_savemotion = box.getString("p_savemotion");


        try { 
            connMgr = new DBConnectionManager();

            if ( v_types.equals("0") ) {                 // ���������� �亯���û���
                sql1  = " delete from TZ_HOMEQNA    ";
                sql1 += "  where tabseq = ? and seq = ?";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, BOARD_TABSEQ);
                pstmt1.setInt(2, v_seq);


            } else { 
                sql1  = " delete from TZ_HOMEQNA";
                sql1 += "  where tabseq = ? and seq = ? and types = ?  ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, BOARD_TABSEQ);
                pstmt1.setInt(2, v_seq);
                pstmt1.setString(3, v_types);

           }

            isOk1 = pstmt1.executeUpdate();

            sql3  = " delete from TZ_COMMENTQNA    ";
            sql3 += "  where tabseq = ? and seq = ? and types = ?  ";
            pstmt2 = connMgr.prepareStatement(sql3);
            pstmt2.setInt(1, BOARD_TABSEQ);
            pstmt2.setInt(2, v_seq);
            pstmt2.setString(3, v_types);

            isOk3 = pstmt2.executeUpdate();

            for ( int i = 0; i < savefile.size() ;i++ ) { 
                String str = (String)savefile.elementAt(i);
                if ( !str.equals("") ) { 
               //     isOk2 = this.deleteUpFile(connMgr, box);
                }
            }
            if ( isOk1 > 0 && isOk2 > 0 ) { 
                if ( savefile != null ) { 
                    FileManager.deleteFile(savefile);         //     ÷������ ����
                }
                if ( v_savemotion != null ) { 
                    FileManager.deleteFile(v_savemotion);
                }

                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            }

        } catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1 * isOk2;
    }




    /**
    * ��õ�ϱ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertRecommend(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String sql   = "";
        String v_seq = box.getString("p_seq");
        int isOk=0;

        try { 
            connMgr = new DBConnectionManager();
			sql = " update tz_homeqna set recommend = nvl(recommend,0) + 1 where tabseq = " + BOARD_TABSEQ + " and seq = " +v_seq + " "; 
            isOk = connMgr.executeUpdate(sql);

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    * ������ ����Ҷ�(����)
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int insertComment(RequestBox box) throws Exception { 
       DBConnectionManager connMgr     = null;
        ListSet             ls      = null;
        PreparedStatement pstmt1 = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int v_cnt = 0;

        String v_commentqna =  box.getString("commentqna");
        int v_seq = box.getInt("p_seq");

        String v_types = box.getString("p_types");
        String v_fileseq = box.getString("p_fileseq");

        String s_userid = "";
        String s_usernm = box.getSession("name");
        String s_gadmin = box.getSession("gadmin");

    //  if ( s_gadmin.equals("A1") ) { 
            s_userid = "���";
    //  } else { 
            s_userid = box.getSession("userid");
    //  }
        String v_isopen  = "Y";
        Vector newFileNames = box.getNewFileNames("p_file");


        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            // ----------------------   �Խ��� ������ ��ȣ�� �����´� ----------------------------
            sql = "select nvl(max(commentseq), 0) from TZ_COMMENTQNA";
            sql += " where tabseq=" + BOARD_TABSEQ + " and seq = " +v_seq + " ";

            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_commentseq = ls.getInt(1) + 1;
            ls.close();
            // ------------------------------------------------------------------------------------


            //// //// //// //// //// //// //// //// //   �Խ��� table �� �Է�  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql1 =  "insert into TZ_COMMENTQNA(tabseq, seq, types, commentseq, inuserid, commentqna, cdate)";
            sql1 += "                values (?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";


            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, BOARD_TABSEQ);
            pstmt1.setInt(2, v_seq);
            pstmt1.setString(3, v_types);
            pstmt1.setInt(4, v_commentseq);
            pstmt1.setString(5,  s_userid);
            pstmt1.setString(6,  v_commentqna);


            isOk1 = pstmt1.executeUpdate();  


            if ( isOk1 > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }


            }
        } catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1;
    }


    /**
    * ��� �����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteComment(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        Connection conn = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk1 = 1;

        int    v_seq          = box.getInt("p_seq");
        String v_types        = box.getString("p_types");
        int    v_commentseq   = box.getInt("p_commentseq");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " delete from TZ_COMMENTQNA    ";
            sql += "  where tabseq = ? and seq = ? and types = ? and commentseq = ? ";
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setInt(1, BOARD_TABSEQ);
            pstmt.setInt(2, v_seq);
            pstmt.setString(3, v_types);
            pstmt.setInt(4, v_commentseq);
            isOk1 = pstmt.executeUpdate();

        } catch ( Exception ex ) { 
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1;
    }
    
    
    /**
    * �亯 ����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
    public int insertKnowBoardAns(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String sql   = "";
        String sql1  = "";
        String sql2  = "";
        int    isOk  = 0;
        int    isOk2 = 0;
        int    isOk3 = 0;
        int    v_cnt = 0;
        String s_grcode  = box.getSession("tem_grcode");
        int    v_seq     = box.getInt("p_seq");
        String v_types   = "";
        String v_title   = box.getString("p_title");
        String v_contents =  StringManager.replace(box.getString("content"),"<br > ","\n");
        String v_isopen  = "Y";
        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name");
        String s_gadmin = box.getSession("gadmin");
        String v_catecd = box.getString("p_catecd");      // ī�װ�   
        Vector newFileNames = box.getNewFileNames("p_file");

        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);

           sql  = " select max(to_number(types)) from TZ_HOMEQNA  ";
           sql += "  where tabseq = " + BOARD_TABSEQ + " and seq = " + v_seq;
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) { 
               v_types = String.valueOf(( ls.getInt(1) + 1));
           } else { 
                v_types = "1";
           }
            //// //// //// //// //// //// //// //// //   �Խ��� table �� �Է�  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql1 =  "insert into TZ_HOMEQNA(tabseq, seq, types, title, contents, indate, inuserid, isopen, luserid, ldate, cnt, grcode,categorycd)                ";
            sql1 += "                values (?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?,?,?) ";
//            sql1 += "                values (?, ?, ?, ?, empty_clob(), to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?,?,?) ";

            pstmt = connMgr.prepareStatement(sql1);
            pstmt.setInt(1, BOARD_TABSEQ);
            pstmt.setInt(2, v_seq);
            pstmt.setString(3, v_types);
            pstmt.setString(4, v_title);
            pstmt.setString(5,  v_contents);
            pstmt.setString(6,  s_userid);
            pstmt.setString(7,  v_isopen);
            pstmt.setString(8, s_userid);
            pstmt.setInt(9, v_cnt);
            pstmt.setString(10, s_grcode);
            pstmt.setString(11,v_catecd);

           isOk = pstmt.executeUpdate();

			sql2 = "select contents from tz_HOMEQNA where tabseq = " + BOARD_TABSEQ + " and  seq = " + v_seq + " and types = '" +v_types + "'";
//            connMgr.setOracleCLOB(sql2, v_contents);       //      (��Ÿ ���� ���)

		   isOk2 = this.insertUpFile(connMgr, BOARD_TABSEQ, v_seq, v_types, box);       //      ����÷���ߴٸ� ����table��  insert
System.out.println("2222222222222");
        //   isOk3 = this.deleteUpFile(connMgr, box);

            // System.out.println(v_contents);

            if ( isOk > 0 && isOk2 > 0 ) { 
                if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    * �����Ͽ� �����Ҷ�
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
     public int updateKnowBoard(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        PreparedStatement pstmt1 = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        ListSet             ls      = null;
        int isOk = 0;
        int isOk2 = 0;
        int isOk3 = 0;
        int isOk5 = 0;
        int v_seq             = box.getInt("p_seq");
		int	v_upfilecnt       = box.getInt("p_upfilecnt");	// 	������ ������ִ� ���ϼ�
		Vector v_savefile     =	new	Vector();
		Vector v_filesequence =	new	Vector();
		
        String v_catecd    = box.getString("p_catecd");      // ī�װ�   
        String v_types     = box.getString("p_types");
        String v_title     = box.getString("p_title");
        String v_contents  =  StringManager.replace(box.getString("content"),"<br > ","\n");
        String v_isopen    = "Y";
        String s_userid    = "";
        String s_usernm    = box.getSession("name");
        String s_gadmin    = box.getSession("gadmin");
        String s_grcode    = box.getSession("tem_grcode");


		for ( int	i =	0; i < v_upfilecnt;	i++ ) { 
			if ( 	!box.getString("p_fileseq" + i).equals(""))	{ 

				v_savefile.addElement(box.getString("p_savefile" + i));			// 		������ ������ִ� ���ϸ� �߿���	������ ���ϵ�
				v_filesequence.addElement(box.getString("p_fileseq"	 + i));		 // 		������	������ִ� ���Ϲ�ȣ	�߿��� ������ ���ϵ�

			}
		}

    //  if ( s_gadmin.equals("A1") ) { 
            s_userid = "���";
    //  } else { 
            s_userid = box.getSession("userid");
    //  }

        try { 
                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);

                sql  = " update TZ_HOMEQNA set title = ? , contents = ?, categorycd = ?,luserid = ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
//                sql  = " update TZ_HOMEQNA set title = ? , contents = empty_clob(), categorycd = ?,luserid = ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
                sql += "  where tabseq = ? and seq = ? and types = ? and grcode = ?                       ";

                pstmt = connMgr.prepareStatement(sql);

                pstmt.setString(1,  v_title);
                // pstmt.setCharacterStream(2,  new StringReader(v_contents), v_contents.length() );
                pstmt.setString(2,  v_contents);
                pstmt.setString(3,  v_catecd);
                pstmt.setString(4,  s_userid);
                pstmt.setInt(5,  BOARD_TABSEQ);
                pstmt.setInt(6,  v_seq);
                pstmt.setString(7,  v_types);
                pstmt.setString(8,  s_grcode);

			    isOk = pstmt.executeUpdate();
			   
			    sql3 = "select contents from TZ_HOMEQNA where seq = " + v_seq + " and tabseq = '" +BOARD_TABSEQ + "' and types = '" +v_types + "'";

//				connMgr.setOracleCLOB(sql3, v_contents);       //      (��Ÿ ���� ���)

    			isOk2 =	this.insertUpFile(connMgr, BOARD_TABSEQ, v_seq,v_types,	box);		// 		����÷���ߴٸ� ����table��	insert
    
    			isOk3 =	this.deleteUpFile(connMgr, box,	v_filesequence);		// 	   ������ ������ �ִٸ�	����table���� ����
    
    			if ( isOk > 0 &&	isOk2 > 	0 && isOk3 > 0)	{ 
    				connMgr.commit();
    				if ( v_savefile != null )	{ 
    					FileManager.deleteFile(v_savefile);			// 	 DB	���� ���ó����	�Ϸ�Ǹ� �ش� ÷������ ����
    				}
    			} else connMgr.rollback();
		}
		catch(Exception	ex)	{ 
			connMgr.rollback();			
			ErrorManager.getErrorStackTrace(ex,	box, sql1);
			throw new Exception("sql = " + sql1	 + "\r\n" + ex.getMessage() );
		}
		finally	{ 
			if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { }	}
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
			if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e10 ) { }	}
		}
		return isOk*isOk2*isOk3;
	}    

   /**
    * QNA ���ο� �ڷ����� ���
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
	 public	int	insertUpFile(DBConnectionManager connMgr, int p_tabseq, int p_seq, String types, RequestBox	box) throws	Exception { 
		ListSet	ls = null;
		PreparedStatement pstmt2 = null;
		String              sql     = "";
		String sql2	= "";
		int	isOk2 =	1;

		// ----------------------   ���ε�Ǵ� ������ ������	�˰� �ڵ��ؾ��Ѵ�  --------------------------------
		String [] v_realFileName = new String [FILE_LIMIT];
		String [] v_newFileName	= new String [FILE_LIMIT];

		for ( int	i =	0; i < FILE_LIMIT; i++ )	{ 
			v_realFileName [i] = box.getRealFileName(FILE_TYPE + (i +1));
			v_newFileName [i] =	box.getNewFileName(FILE_TYPE + (i +1));
		}
		// ----------------------------------------------------------------------------------------------------------------------------

		String s_userid	= box.getSession("userid");

		try	{ 
			 // ----------------------	�ڷ� ��ȣ �����´� ----------------------------
			sql	= "select nvl(max(fileseq),	0) from	tz_homefile	where tabseq = " +p_tabseq + " and seq = " +	p_seq + "and types = '" + types + "' " ;
			ls = connMgr.executeQuery(sql);
			ls.next();
			int	v_fileseq =	ls.getInt(1) + 1;
			ls.close();
			// ------------------------------------------------------------------------------------

			//// //// //// //// //// //// //// //// // 	 ���� table	�� �Է�	 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
			sql2 =	"insert	into tz_homefile(tabseq, seq, fileseq, types, realfile, savefile, luserid,	ldate)";
			sql2 +=	" values (?, ?, ?, ?, ?, ?,?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";
			pstmt2 = connMgr.prepareStatement(sql2);

			for ( int	i =	0; i < FILE_LIMIT; i++ )	{ 
				if ( 	!v_realFileName	[i].equals(""))	{ 		// 		���� ���ε�	�Ǵ� ���ϸ�	üũ�ؼ� db�� �Է��Ѵ�
					pstmt2.setInt(1, p_tabseq);
					pstmt2.setInt(2, p_seq);
					pstmt2.setInt(3, v_fileseq);
					pstmt2.setString(4, types);
					pstmt2.setString(5,	v_realFileName[i]);
					pstmt2.setString(6,	v_newFileName[i]);
					pstmt2.setString(7,	s_userid);
					isOk2 =	pstmt2.executeUpdate();
					v_fileseq++;
					System.out.println("p_tabseq:::" +p_tabseq);
					System.out.println("p_seq:::" +p_seq);
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
        String v_types   = box.getString("p_types");
		int	v_seq =	box.getInt("p_seq");

		try	{ 
			sql3 = "delete from tz_homefile where tabseq = " + BOARD_TABSEQ + " and seq =? and fileseq = ?";
			pstmt3 = connMgr.prepareStatement(sql3);
			for ( int	i =	0; i < p_filesequence.size(); i++ ) { 
				int	v_fileseq =	Integer.parseInt((String)p_filesequence.elementAt(i));

				pstmt3.setInt(1, v_seq);
				pstmt3.setInt(2, v_fileseq);
				isOk3 =	pstmt3.executeUpdate();
			}
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
	

}



