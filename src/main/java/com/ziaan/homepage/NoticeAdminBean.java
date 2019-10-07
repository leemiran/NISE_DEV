// **********************************************************
//  1. ��      ��: �������� ����
//  2. ���α׷��� : NoticeAdminBean.java
//  3. ��      ��: �������� ����
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0��������
//  6. ��      ��: ��â�� 2005. 7.  14
//  7. ��      ��:
// **********************************************************
package com.ziaan.homepage;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Collections;

import org.apache.log4j.Logger;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
 
public class NoticeAdminBean { 
	
	private ConfigSet config;
    private int row; 
    private int adminrow; 
    private	static final String	FILE_TYPE =	"p_file";			// 		���Ͼ��ε�Ǵ� tag name
	private	static final int FILE_LIMIT	= 5;					// 	  �������� ���õ� ����÷�� ����
	private org.apache.log4j.Logger logger = Logger.getLogger(this.getClass());

    
	public NoticeAdminBean() { 
	    try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        �� ����� �������� row ���� �����Ѵ�
            adminrow = Integer.parseInt(config.getProperty("page.bulletin.adminrow") );        //        �� ����� �������� row ���� �����Ѵ�
        } catch( Exception e ) { 
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
		 DBConnectionManager connMgr     = null;
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


// ==  ==  ==  == =�����ȭ�� ����Ʈ ���� ==  ==  ==  == =

	/**
	* ��ü����  ����Ʈ 
	* @param box          receive from the form object and session
	* @return ArrayList   ��ü���� ����Ʈ
	* @throws Exception
	*/
	public ArrayList selectListNoticeAll(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		NoticeData data = null;
		DataBox             dbox    = null;
		
		String v_defaultcomp = "";
		
		v_defaultcomp = selectDefalutComp(box);
		
		String v_search     = box.getString("p_search");
        String v_searchtext = box.getString("p_searchtext");
        //String v_notice_gubun = box.getString("p_notice_gubun");
		String v_selcomp    = box.getStringDefault("p_selcomp", v_defaultcomp);
		String s_gadmin     = box.getSession("gadmin");
		String v_gadmin  = "";
		String s_comp = box.getSession("comp");
		String s_grcode = box.getSession("grcode");
		
		if ( !s_gadmin.equals("") ) { 
		  v_gadmin = s_gadmin.substring(0, 1);
		}

        int v_tabseq = box.getInt("p_tabseq");

		try { 
			connMgr = new DBConnectionManager();

			list = new ArrayList();
			sql += " select       \n";
			sql += "   rownum,     \n";
			sql += "   a.seq,        \n";
			sql += "   a.addate,     \n";
			sql += "   a.adtitle,    \n";
			sql += "   a.adname,     \n";
			sql += "   a.cnt,        \n";
			sql += "   a.luserid,    \n";
			sql += "   a.ldate,      \n";
			sql += "   a.isall,      \n";
			sql += "   a.useyn,      \n";
			sql += "   a.popup,      \n";
			sql += "   a.loginyn,    \n";
			sql += "   a.gubun,      \n";
			sql += "   a.aduserid,    \n";
            sql += "   a.type,       \n";
            sql += "   a.notice_gubun,\n";
			sql += "		(select count(realfile) from tz_boardfile where tabseq = a.TABSEQ and seq = a.seq) filecnt ";
			sql += " from TZ_NOTICE  a";
			sql += "    where isall = 'Y'                                                                    ";

//			if ( !v_selcomp.equals("ALL") ) { 
//			  sql += "    and compcd like '%" +v_selcomp + "%' ";
//			}

			if ( !v_searchtext.equals("") ) {      //    �˻�� ������
				if ( v_search.equals("adtitle") ) {                          //    �������� �˻��Ҷ�
					sql += " and adtitle like " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("adcontents") ) {                //    �������� �˻��Ҷ�
                    sql += " and adcontent like " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("aduserid") ) {                //    �������� �˻��Ҷ�
                    sql += " and aduserid  =  " + StringManager.makeSQL(  v_searchtext );
                } else if ( v_search.equals("adname") ) {                //    �������� �˻��Ҷ�
                    sql += " and adname    like " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("addate") ) {                //    �������� �˻��Ҷ�
                    sql += " and SUBSTR(addate, 1, 8) <= " + StringManager.makeSQL( StringManager.replace(v_searchtext, ".", ""));
                }
			}
            
			/*
            if ( !v_notice_gubun.equals("") ) {      //    �˻�� ������
                    sql += " and notice_gubun =  " + StringManager.makeSQL(  v_notice_gubun );
            }
            */
			if(!v_gadmin.equals("A")){
				sql += "  and ( compcd like '%"+s_comp+"%' or grcodecd like '%"+s_grcode+"%' ) ";
			}
			
			sql += "      and tabseq = " +  v_tabseq;
			sql += "    order by addate desc                                                                    ";

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
	* ��������ȭ�� ����Ʈ
	* @param box          receive from the form object and session
	* @return ArrayList   �������� ����Ʈ(��ü���� ����)
	* @throws Exception
	*/
	public ArrayList selectListNotice(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		NoticeData data = null;
		DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
        
        String v_defaultcomp = "";
		v_defaultcomp = selectDefalutComp(box);
        
		String v_searchtext = box.getString("p_searchtext");
		String v_search     = box.getString("p_search");
        //String v_notice_gubun = box.getString("p_notice_gubun");
		String v_selcomp    = box.getStringDefault("p_selcomp", v_defaultcomp);
		
		String s_gadmin     = box.getSession("gadmin");
		String v_gadmin  = "";
		if ( !s_gadmin.equals("") ) { 
		  v_gadmin = s_gadmin.substring(0, 1);
		}
		
		String s_comp = box.getSession("comp");
		String s_grcode = box.getSession("grcode");
		
		int v_pageno        = box.getInt("p_pageno");

		try { 
			connMgr = new DBConnectionManager();

			list = new ArrayList();

			sql += " select       \n";
			sql += "   rownum,     \n";
			sql += "   a.seq,        \n";
			sql += "   a.addate,     \n";
			sql += "   a.adtitle,    \n";
			sql += "   a.adname,     \n";
			sql += "   a.cnt,        \n";
			sql += "   a.luserid,    \n";
			sql += "   a.ldate,      \n";
			sql += "   a.isall,      \n";
			sql += "   a.useyn,      \n";
			sql += "   a.popup,      \n";
			sql += "   a.loginyn,    \n";
			sql += "   a.aduserid,    \n";
			sql += "   a.gubun,      \n";
            sql += "   a.type,    \n";
            sql += "   a.notice_gubun,\n";
			sql += "		(select count(realfile) from tz_boardfile where tabseq = a.TABSEQ and seq = a.seq) filecnt ";
			sql += " from TZ_NOTICE  a";
			sql += "  where ";
			sql += "  isall = 'N' ";
			sql += "    and tabseq = " +  v_tabseq;
			
//			if ( !v_selcomp.equals("ALL") ) { 
//			  sql += "    and compcd like '%" +  v_selcomp + "%' ";
//			}
            
            if ( !v_searchtext.equals("") ) {      //    �˻�� ������
                if ( v_search.equals("adtitle") ) {                          //    �������� �˻��Ҷ�
                    sql += " and adtitle like " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("adcontents") ) {                //    �������� �˻��Ҷ�
                    sql += " and adcontent like " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("aduserid") ) {                //    �������� �˻��Ҷ�
                    sql += " and aduserid  =  " + StringManager.makeSQL(  v_searchtext );
                } else if ( v_search.equals("adname") ) {                //    �������� �˻��Ҷ�
                    sql += " and adname    like " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("addate") ) {                //    �������� �˻��Ҷ�
                    sql += " and SUBSTR(addate, 1, 8) <= " + StringManager.makeSQL( StringManager.replace(v_searchtext, ".", ""));
                }
            }
            /*
            if ( !v_notice_gubun.equals("") ) {      //    �˻�� ������
                sql += " and notice_gubun =  " + StringManager.makeSQL(  v_notice_gubun );
            }
			*/
            
			if(!v_gadmin.equals("A")){
				sql += "  and ( compcd like '%"+s_comp+"%' or grcodecd like '%"+s_grcode+"%' ) ";
			}
			
			sql += " order by addate desc ";

			ls = connMgr.executeQuery(sql);
            
			ls.setPageSize(adminrow);             //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);                    //     ������������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();       //     ��ü ������ ���� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

			while ( ls.next() ) { 
				
				dbox = ls.getDataBox();
				dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(adminrow));
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
// ==  ==  ==  == =�����ȭ�� ����Ʈ �� ==  ==  ==  == =




	/**
	* �������� ����Ҷ�
	* @param box      receive from the form object and session
	* @return isOk    1:insert success,0:insert fail
	* @throws Exception
	*/
	public int insertNotice(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		PreparedStatement   pstmt   = null;
		String sql  = "";
		String sql1 = "";
		String sql2 = "";
		int isOk  = 0;
		int isOk2 = 0;
		int v_seq = 0;

        int v_tabseq       = box.getInt("p_tabseq");
		String v_gubun     = box.getStringDefault("p_gubun","N");
		String v_grcodegubun = box.getString("p_grcodegubun");
		String v_startdate = box.getString("p_startdate");
		String v_enddate   = box.getString("p_enddate");
		String v_adtitle   = box.getString("p_adtitle");
		String v_content   = box.getString("p_content");
		String v_loginyn   = box.getString("p_login");
		String v_useyn     = box.getString("p_use");
		String v_compcd	   = box.getString("p_compcd");
		int v_popwidth     = box.getInt("p_popsize1");
		int v_popheight    = box.getInt("p_popsize2");
		int v_popxpos      = box.getInt("p_popposition1");
		int v_popypos      = box.getInt("p_popposition2");
		String v_popup	   = box.getString("p_popup");
		String v_upfile	   = box.getNewFileName("p_file1");
		String v_realfile  = box.getRealFileName("p_file1");
		String v_useframe  = box.getString("p_useframe");
		String v_uselist   = box.getString("p_uselist");
		String v_isall     = box.getString("p_isAllvalue");
		String v_grcodecd  = box.getString("p_grocdecd");
        String v_type      = box.getString("p_type");
        String v_notice_gubun    = box.getString("p_notice_gubun");
        String v_tem_type        = box.getString("p_tem_type");

		String s_userid   = box.getSession("userid");
		String s_name     = box.getSession("name");
		
		String v_content1 = "";
		
		
Log.err.println("v_upfil1 == > " + v_upfile);		
Log.err.println("v_realfile1 == > " + v_realfile);
		
		try { 

		   connMgr = new DBConnectionManager();
		   
		   connMgr.setAutoCommit(false);

		   sql  = "select max(seq) from TZ_NOTICE  ";
		   ls = connMgr.executeQuery(sql);
		   if ( ls.next() ) { 
			   v_seq = ls.getInt(1) + 1;
		   } else { 
			   v_seq = 1;
		   }
		   
		   
		   /*********************************************************************************************/
		   // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ���ε�)
		   ConfigSet conf = new ConfigSet();

         
		   /*********************************************************************************************/
		   

           sql1 =  " insert into TZ_NOTICE(               \n";
           sql1 += " tabseq, seq, gubun, startdate,       \n";
           sql1 += " enddate, addate, adtitle, adname,    \n";
           sql1 += " adcontent, cnt, luserid, ldate,      \n";
           sql1 += " loginyn, useyn, compcd, popwidth,    \n";
           sql1 += " popheight, popxpos, popypos, upfile, \n";
           sql1 += " realfile , popup, uselist, useframe, \n"; 
           sql1 += " isall, grcodecd, aduserid, type, notice_gubun, tem_type, grcodegubun)   \n";
           sql1 += " values (";
           sql1 += " ?,            ?,            ?,            ?," ;
           sql1 += " ?,            to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ";
           sql1 += " ?,            ?,            ?,            to_char(sysdate, 'YYYYMMDDHH24MISS'), ";
           sql1 += " ?,            ?,            ?,            ?,";
           sql1 += " ?,            ?,            ?,            ?,";
           sql1 += " ?,            ?,            ?,            ?, ";
           sql1 += " ?,            ?, ?, ?, ?, ?, ?) ";
           
           pstmt = connMgr.prepareStatement(sql1);
           
           pstmt.setInt   (1,  v_tabseq);
           pstmt.setInt   (2,  v_seq);
           pstmt.setString(3,  v_gubun);
           pstmt.setString(4,  v_startdate);
           pstmt.setString(5,  v_enddate);
           pstmt.setString(6,  v_adtitle);
           pstmt.setString(7,  s_name);
           pstmt.setString(8,  v_content);
           pstmt.setInt   (9,  0);
           pstmt.setString(10, s_userid);
           pstmt.setString(11, v_loginyn);
           pstmt.setString(12, v_useyn);
           pstmt.setString(13, v_compcd);
           pstmt.setInt   (14, v_popwidth);
           pstmt.setInt   (15, v_popheight);
           pstmt.setInt   (16, v_popxpos);
           pstmt.setInt   (17, v_popypos);   
           pstmt.setString(18, v_upfile);       // ����
           pstmt.setString(19, v_realfile);     // ����
           pstmt.setString(20, v_popup);        // �˾�����
           pstmt.setString(21, v_uselist);      // ����Ʈ��뿩��
           pstmt.setString(22, v_useframe);     // �����ӻ�뿩��
           pstmt.setString(23, v_isall);        // ��ü��������
           pstmt.setString(24, v_grcodecd);     // �����׷��ڵ�
           pstmt.setString(25, s_userid);     // �����׷��ڵ�
           pstmt.setString(26, v_type);       // ��ü �������� ���� �������� ����
           pstmt.setString(27, v_notice_gubun);   // ��������
           pstmt.setString(28, v_tem_type);       // ���ø� Ÿ�� (A,B,C)
           pstmt.setString(29, v_grcodegubun);    // ������׷�
           isOk = pstmt.executeUpdate();
           
           sql2 = "select adcontent from TZ_NOTICE where tabseq = " + v_tabseq + " and seq = " + v_seq ;
//           connMgr.setOracleCLOB(sql2, v_content);       //      (��Ÿ ���� ���)       
           
           sql2 = "select compcd from tz_notice where seq = " + v_seq + "and tabseq = " + v_tabseq;
//           connMgr.setOracleCLOB(sql2, v_compcd);       //      (��Ÿ ���� ���)       
           
           sql2 = "select grcodecd from tz_notice where seq = " + v_seq + "and tabseq = " + v_tabseq;
//           connMgr.setOracleCLOB(sql2, v_grcodecd);       //      (��Ÿ ���� ���)       
           
           
           isOk2 = this.insertUpFile(connMgr,v_tabseq, v_seq, box);
           
           if ( isOk > 0 && isOk2 > 0 ) { 
           	  connMgr.commit();
           	} else { 
           	  connMgr.rollback();		   	
           	}

           Log.err.println("isOk == > " + isOk);

		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql - > " + sql1 + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}


	/**
	* �������� �����Ͽ� �����Ҷ�
	* @param box      receive from the form object and session
	* @return isOk    1:update success,0:update fail
	* @throws Exception
	*/
	 public int updateNotice(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;
		String              sql     = "";
		String sql2 = "";
		int isOk = 0;
		int isOk2 = 0;
		int isOk3 = 0;
		
		box.put("p_type", "HN");

        int v_tabseq = box.getInt("p_tabseq");
		int v_seq          = box.getInt("p_seq");
		String v_gubun     = box.getStringDefault("p_gubun","N");
		String v_startdate = box.getString("p_startdate");
		String v_enddate   = box.getString("p_enddate");
		String v_adtitle   = box.getString("p_adtitle");
		String v_adcontent = box.getString("p_adcontent");
		String v_loginyn   = box.getString("p_login");
		String v_useyn     = box.getString("p_use");
		String v_compcd    = box.getString("p_compcd");
		int v_popwidth     = box.getInt("p_popsize1");
		int v_popheight    = box.getInt("p_popsize2");
		int v_popxpos      = box.getInt("p_popposition1");
		int v_popypos      = box.getInt("p_popposition2");
		String v_popup     = box.getString("p_popup");
		String v_uselist   = box.getString("p_uselist");
		String v_useframe  = box.getString("p_useframe");
		String v_isall     = box.getString("p_isAllvalue");
		String v_grcodecd  = box.getString("p_grocdecd");
        
        String v_notice_type      = box.getString("p_notice_type");
        String v_notice_gubun     = box.getString("p_notice_gubun");
        String v_tem_type         = box.getString("p_tem_type");
        
		String s_userid   = box.getSession("userid");
		String s_name     = box.getSession("name");		
		
		int	v_upfilecnt       = box.getInt("p_upfilecnt");	// 	������ ������ִ� ���ϼ�
		Vector v_savefile     =	new	Vector();
		Vector v_filesequence =	new	Vector();
		

		try { 
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			
			for ( int	i =	0; i < v_upfilecnt;	i++ ) { 
			  if ( 	!box.getString("p_fileseq" + i).equals(""))	{ 
			  	v_savefile.addElement(box.getString("p_savefile" + i));			// 		������ ������ִ� ���ϸ� �߿���	������ ���ϵ�
			  	v_filesequence.addElement(box.getString("p_fileseq"	 + i));		 // 		������	������ִ� ���Ϲ�ȣ	�߿��� ������ ���ϵ�
			  }
		    }
			
			
		   /*********************************************************************************************/
		   // ���𿡵��� ���� ó��  (Mime Document Parsing �� �̹��� ���ε�)
		   ConfigSet conf = new ConfigSet();
/*           
		   SmeNamoMime namo = new SmeNamoMime(v_adcontent); // ��ü���� 
		   boolean result = namo.parse(); // ���� �Ľ� ���� 

		   if ( !result ) { // �Ľ� ���н� 
		   	System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
		   	return 0;
		   }

		   if ( namo.isMultipart() ) { // ������ ��Ƽ��Ʈ���� �Ǵ� 
		   	String v_server = conf.getProperty("autoever.url.value");
		   	String fPath = conf.getProperty("dir.namo");   // ���� ���� ��� ����
		   	String refUrl = conf.getProperty("url.namo");; // ������ ����� ������ �����ϱ� ���� ���
		   	String prefix =  "HomeNotice" + v_seq;         // ���ϸ� ���ξ�
		   	result = namo.saveFile(fPath, v_server +refUrl, prefix); // ���� ���� ���� 
		   }

		   if ( !result ) { // �������� ���н� 
		   	System.out.println( namo.getDebugMsg() ); // ����� �޽��� ��� 
		   	return 0;
		   }
		   

		   v_adcontent = namo.getContent(); // ���� ����Ʈ ���
*/           
		   /*********************************************************************************************/
		   
			sql  = " update TZ_NOTICE set gubun = ? ,  startdate = ? , enddate = ? , adtitle = ? ,       "; //,adname = ? 
			sql += "                      adcontent = ? , "; //luserid = ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS'),
//			sql += "                      adcontent = empty_clob() , "; //luserid = ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS'),
			sql += " loginyn= ?, useyn = ?, compcd = ?, popwidth = ?, popheight = ?, popxpos = ?, popypos = ?, 		 ";
//			sql += " loginyn= ?, useyn = ?, compcd = empty_clob(), popwidth = ?, popheight = ?, popxpos = ?, popypos = ?, 		 ";
			sql += " popup=?, uselist= ?, useframe = ?, isall = ? , grcodecd = ?, type = ?, notice_gubun = ?, tem_type = ? ";
//			sql += " popup=?, uselist= ?, useframe = ?, isall = ? , grcodecd = empty_clob(), type = ?, notice_gubun = ? ";
			sql += " where tabseq = ? and seq = ?                                                                    ";

			pstmt = connMgr.prepareStatement(sql);

            int params = 1;
			pstmt.setString(params++, v_gubun);
			pstmt.setString(params++, v_startdate);
			pstmt.setString(params++, v_enddate);
			pstmt.setString(params++, v_adtitle);
			pstmt.setString(params++, v_adcontent);
			//pstmt.setString(params++, s_name);
			//pstmt.setString(6, s_userid);
			pstmt.setString(params++, v_loginyn);
			pstmt.setString(params++, v_useyn);
			pstmt.setString(params++, v_compcd);
			pstmt.setInt(params++,v_popwidth); 
			pstmt.setInt(params++,v_popheight);
			pstmt.setInt(params++,v_popxpos);  
			pstmt.setInt(params++,v_popypos);  
			pstmt.setString(params++,v_popup);
			pstmt.setString(params++,v_uselist);
			pstmt.setString(params++,v_useframe);
			pstmt.setString(params++,v_isall);
			pstmt.setString(params++,v_grcodecd);
            pstmt.setString(params++,v_notice_type);
            pstmt.setString(params++, v_notice_gubun);
            pstmt.setString(params++, v_tem_type);
			pstmt.setInt(params++, v_tabseq);
			pstmt.setInt(params++, v_seq);
			
			isOk = pstmt.executeUpdate();
			
			sql2 = "select adcontent from tz_notice where seq = " + v_seq + "and tabseq = " + v_tabseq;
//			connMgr.setOracleCLOB(sql2, v_adcontent);       //      (��Ÿ ���� ���)       
			
			sql2 = "select compcd from tz_notice where seq = " + v_seq + "and tabseq = " + v_tabseq;
//			connMgr.setOracleCLOB(sql2, v_compcd);       //      (��Ÿ ���� ���)       
			
			sql2 = "select grcodecd from tz_notice where seq = " + v_seq + "and tabseq = " + v_tabseq;
//			connMgr.setOracleCLOB(sql2, v_grcodecd);       //      (��Ÿ ���� ���)       
			
			isOk3 =	this.deleteUpFile(connMgr, box,	v_filesequence);		// 	   ������ ������ �ִٸ�	����table���� ����
			isOk2 =	this.insertUpFile(connMgr, v_tabseq, v_seq,	box);		// 		����÷���ߴٸ� ����table��	insert
			
			if ( isOk > 0 && isOk2 > 0 && isOk3 > 0) { 
				connMgr.commit();
				// connMgr.rollback();
				isOk =1;
			}
			else{ 
				connMgr.rollback();
				isOk =0;
			}
		}

		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}


	

	/**
	* �������� �����Ҷ�
	* @param box      receive from the form object and session
	* @return isOk    1:delete success,0:delete fail
	* @throws Exception
	*/
	public int deleteNotice(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;
		String              sql     = "";

		int isOk = 0;

        int v_tabseq = box.getInt("p_tabseq");
		int v_seq  = box.getInt("p_seq");

		try { 
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
				  
			sql  = " delete from TZ_NOTICE           ";
			sql += "   where tabseq = ? and seq = ?  ";

			pstmt = connMgr.prepareStatement(sql);
			pstmt.setInt(1, v_tabseq);
			pstmt.setInt(2, v_seq);
			isOk = pstmt.executeUpdate();
			
			if(!"0".equals(box.getString("p_upfilecnt"))){//������ ������ ����
				if(pstmt != null){ pstmt.close(); }
				
				sql  = " delete from tz_boardfile        ";
				sql += "   where tabseq = ? and seq = ?  ";
				
				pstmt = connMgr.prepareStatement(sql);
				pstmt.setInt(1, v_tabseq);
				pstmt.setInt(2, v_seq);
				isOk = pstmt.executeUpdate();
			}
			
			
			if(isOk > 0){
				connMgr.commit();
			}else{
				connMgr.rollback();
			}
			
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql + "\r\n");
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}





	/**
	* ��������ȭ�� �󼼺���
	* @param box          receive from the form object and session
	* @return ArrayList   ��ȸ�� ������
	* @throws Exception
	*/
   public DataBox selectViewNotice(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		String              sql     = "";
		NoticeData data = null;
		DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
		String v_seq = box.getString("p_seq");
		String v_process = box.getString("p_process");
		
		Vector realfileVector = new Vector();
		Vector savefileVector = new Vector();
		Vector fileseqVector  = new Vector();  


		try { 
			connMgr = new DBConnectionManager();

			sql += " select \n";
			sql += "   a.seq,     \n";
			sql += "   a.gubun,   \n";
			sql += "   a.grcodegubun,   \n";
			sql += "   trim(startdate) startdate, \n";
			sql += "   trim(enddate) enddate,     \n";
			sql += "   a.addate,                    \n";
			sql += "   a.adtitle,                   \n";
			sql += "   a.adname,                    \n";
			sql += "   a.adcontent,                 \n";    
			sql += "   a.cnt,                       \n";
			sql += "   a.luserid,                   \n";
			sql += "   a.ldate,                     \n";
			sql += "   a.loginyn,                   \n"; 
			sql += "   a.useyn,                     \n"; 
			sql += "   a.compcd,                    \n";
			sql += "   a.popwidth,                  \n";
			sql += "   a.popheight,                 \n";
			sql += "   a.popxpos,                   \n";
			sql += "   a.popypos,                   \n";
			sql += "   a.popup,                     \n";
			sql += "   a.uselist,                   \n";
			sql += "   a.useframe,                  \n";
			sql += "   a.isall,                     \n";
			sql += "   a.aduserid,                  \n";
			sql += "   a.grcodecd,                  \n";
			sql += "   b.realfile,                  \n";
			sql += "   b.savefile,                  \n";
			sql += "   b.fileseq,                    \n";
            sql += "   a.type,                       \n";
            sql += "   a.notice_gubun,               \n";
            sql += "   a.tem_type                   \n";
			sql += " from TZ_NOTICE a , TZ_BOARDFILE B  \n";
			sql += "  where a.seq    = " + StringManager.makeSQL(v_seq);
			sql += "    and a.tabseq = " +  v_tabseq;
			sql += "    and a.tabseq = b.tabseq( +)";
			sql += "    and a.seq = b.seq( +)";
			
			ls = connMgr.executeQuery(sql);

			while ( ls.next() ) { 
				dbox = ls.getDataBox();
				realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
				fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));
			}
			
			if ( realfileVector 	 != null ) dbox.put("d_realfile", realfileVector);
			if ( savefileVector 	 != null ) dbox.put("d_savefile", savefileVector);
			if ( fileseqVector 	 != null ) dbox.put("d_fileseq", fileseqVector);
			
			// ��ȸ�� ��
			if ( !v_process.equals("popupview") ) { 
			  connMgr.executeUpdate("update TZ_NOTICE set cnt = cnt + 1 where seq = " + v_seq);
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
	* �������� Ȩ������ ����ȭ�� ����Ʈ(�ֽ�5��)
	* @param box          receive from the form object and session
	* @return ArrayList   �������� ����Ʈ(�ֽ�3�� ��ü���� ����)
	* @throws Exception
	*/
	public ArrayList selectListNoticeMain(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		NoticeData data = null;
		DataBox             dbox    = null;
		String v_login  = "";
        String v_comp   = box.getSession("comp");
        
        if ( box.getSession("userid").equals("") ) { 
        	v_login = "N";
        } else { 
        	if(v_comp.length()>4) {
        		v_comp = v_comp.substring(0, 4);
        	}
        	v_login = "Y";
        }

        int v_tabseq = box.getInt("p_tabseq");

		try { 
			connMgr = new DBConnectionManager();

			list = new ArrayList();
			sql  = " select seq, addate, adtitle, adname, cnt, luserid, ldate, type from                      ";
			sql += " ( select rownum, seq, addate, adtitle, adname, cnt, luserid, ldate, type from TZ_NOTICE  ";
			sql += "    where ";
			// sql += "      gubun = 'N' ";
			sql += "      and tabseq = " +  v_tabseq;
			sql += "      and useyn  = 'Y'";
			
			if ( v_login.equals("Y") ) {  // �α����� �Ѱ��
			  sql += "      and ( loginyn = 'AL' or loginyn = 'Y' )";
			  sql += "      and ( gubun = 'N' and compcd like " + StringManager.makeSQL("%" + v_comp + "%") + " ) ";
			} else { // �α������� �������
			  sql += "      and ( loginyn = 'AL' or loginyn = 'N' )";
			  sql += "      and gubun = 'Y'";
			}
			sql += "      and  ( ( popup = 'N' ) or (popup = 'Y' and uselist = 'Y') )";
			
			sql += "    order by addate desc )                                                                        ";
			sql += " where rownum < 4                                                                              ";

			ls = connMgr.executeQuery(sql);

			while ( ls.next() ) { 
				// data = new NoticeData();
                // 
				// data.setSeq( ls.getInt("seq") );
				// data.setAddate( ls.getString("addate") );
				// data.setAdtitle( ls.getString("adtitle") );
				// data.setAdname( ls.getString("adname") );
				// data.setAdcontent( ls.getString("adcontent") );
				// data.setCnt( ls.getInt("cnt") );
				// data.setLuserid( ls.getString("luserid") );
				// data.setLdate( ls.getString("ldate") );
				// list.add(data);
				
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




// ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == Ȩ������Main��� ==  ==  ==  ==  ==  ==  ==  ==  == 

	/**
	* �˾�����  ����Ʈ 
	* @param box          receive from the form object and session
	* @return ArrayList   �˾����� ����Ʈ
	* @throws Exception
	*/
	public ArrayList selectListNoticePopupHome(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		NoticeData data = null;
		DataBox             dbox    = null;

        int    v_tabseq = box.getInt("p_tabseq");
        String v_today  = FormatDate.getDate("yyyyMMdd");
        
        String v_login  = "";
        String v_comp   = box.getSession("comp");
        String tem_grcode = box.getStringDefault("tem_grcode", box.getSession("tem_grcode") );
        
        if(tem_grcode.equals("")){
        	tem_grcode="N000001";
        }
        
        //System.out.println("tem_grcode ::: " + tem_grcode);

        if ( box.getSession("userid").equals("") ) { 
        	v_login = "N";
        } else { 
            v_comp = (v_comp.length()>=4) ? v_comp.substring(0, 4) : v_comp;
        	v_login = "Y";
        }

		try { 
			connMgr = new DBConnectionManager();

			list = new ArrayList();
			
			sql += " select          \n";
			sql += "   rownum,       \n";
			sql += "   seq,          \n";
			sql += "   addate,       \n";
			sql += "   adtitle,      \n";
			sql += "   adname,       \n";
			sql += "   adcontent,    \n";
			sql += "   cnt,          \n";
			sql += "   uselist,      \n";
			sql += "   useframe,     \n";
			sql += "   popwidth,     \n";
			sql += "   popheight,     \n";
			sql += "   popxpos,     \n";
			sql += "   popypos,     \n";
			sql += "   luserid,      \n";
			sql += "   ldate         \n";
            sql += "   type,          \n";
            sql += "   tem_type       \n";
			sql += " from TZ_NOTICE  \n";
			sql += "    where        \n";
			sql += "      tabseq = " +  v_tabseq;
            sql += "      and useyn= 'Y'";			
			sql += "      and popup = 'Y'";  
			sql += "      and to_char(sysdate,'YYYYMMDDHH24') between startdate||'00' and enddate||'24'";

			if ( v_login.equals("Y") ) {  // �α����� �Ѱ��
				//sql += "      and ( compcd like '%" +v_comp + "%' or grcodecd like '%" +tem_grcode + "%' )";
				sql += "      and ( loginyn = 'AL' or loginyn = 'Y' )";
				sql += "      and ( compcd like '%" +v_comp + "%' )";
			} else { // �α������� �������
				// �α��� �����õǰų� ��ü�� ���õȰ��
				sql += "      and ( ( loginyn = 'AL' or loginyn = 'N' ) and grcodecd like '%" +tem_grcode + "%' )";
			}
			
			/*
			if(!tem_grcode.equals("")) {
				sql += "      and grcodecd like " + SQLString.Format("%"+tem_grcode+"%");
			}
			*/			

			sql += "    order by enddate desc ";
			
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
	* ��ü����  ����Ʈ (�ֽ� ��ü ���� 3����)
	* @param box          receive from the form object and session
	* @return ArrayList   ��ü���� ����Ʈ
	* @throws Exception
	*/
	public ArrayList selectListNoticeTop(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		NoticeData data = null;
		DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
        
        String v_login  = "";
        String v_comp   = box.getSession("comp");
        String tem_grcode   = box.getStringDefault("tem_grcode", box.getSession("tem_grcode") );
        String v_searchtext = box.getString("p_searchtext");
		String v_search     = box.getString("p_search");
        
        // System.out.println("tem_grcode == = >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  > " +tem_grcode);
        
        if ( box.getSession("userid").equals("") ) { 
        	v_login = "N";
        } else { 
            v_comp = (v_comp.length()>=4) ? v_comp.substring(0, 4) : v_comp;
        	v_login = "Y";
        }

		try { 
			connMgr = new DBConnectionManager();

			list = new ArrayList();
			sql  = " select isall, seq, addate, adtitle, adname, cnt, luserid, ldate, type  from                      ";
			sql += " ( select isall, rownum, seq, addate, adtitle, adname,  cnt, luserid, ldate, type from TZ_NOTICE  ";
			// sql += "    where gubun = 'Y'                                                                          ";
			sql += "    where  ";
			sql += "      tabseq = " +  v_tabseq;
			sql += "      and useyn= 'Y'";
			sql += "      and (popup = 'N' or (popup = 'Y' and uselist='Y') )";

			/*if ( v_login.equals("Y") ) {  // �α����� �Ѱ��
			  sql += "      and ( loginyn = 'AL' or loginyn = 'Y' )";
			  sql += "      and ( compcd like '%" +v_comp + "%' )";
			} else { // �α������� �������
			// �α��� �����õǰų� ��ü�� ���õȰ��
			  sql += "      and ( ( loginyn = 'AL' or loginyn = 'N' ) and grcodecd like '%" +tem_grcode + "%' )";
			}*/
			
			if(!tem_grcode.equals("")) {
				sql += "      and grcodecd like " + SQLString.Format("%"+tem_grcode+"%");
			}
			
			sql += "    order by addate desc )";
			sql += " where rownum < 6";
			
            ls = connMgr.executeQuery(sql);

			while ( ls.next() ) { 
				data = new NoticeData();
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
	* ��ü����  ����Ʈ (Ȩ������more)
	* @param box          receive from the form object and session
	* @return ArrayList   ��ü���� ����Ʈ
	* @throws Exception
	*/
	public ArrayList selectListNoticeAllHome(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		NoticeData data = null;
		DataBox             dbox    = null;
		
		int v_tabseq = Integer.parseInt(box.getStringDefault("p_tabseq","12"));
		
		String v_searchtext = box.getString("p_searchtext");
		String v_search     = box.getString("p_search");
		
		String v_login  = "";
        String v_comp   = box.getSession("comp");
        String tem_grcode        = box.getStringDefault("tem_grcode", box.getSession("tem_grcode") );  
        String v_notice_type = box.getString("p_notice_type");
        
        if ( box.getSession("userid").equals("") ) { 
        	v_login = "N";
        } else { 
        	if(v_comp.length()>4) {
        		v_comp = v_comp.substring(0, 4);
        	}
        	v_login = "Y";
        }
        
        int v_pageno        = box.getInt("p_pageno");
        

		try { 
			connMgr = new DBConnectionManager();

			list = new ArrayList();
			//sql += " SELECT * FROM (select \n";
			sql += " select \n";
			sql += "    rownum,     \n";
			sql += "    seq,        \n";
			sql += "    addate,     \n";
			sql += "    adtitle,    \n";
			sql += "    adname,     \n";
			sql += "    cnt,        \n";
			sql += "    luserid,    \n";
			sql += "    ldate,      \n";
			sql += "    isall,      \n";
			sql += "    useyn,      \n";
			sql += "    popup,      \n";
			sql += "    loginyn,    \n";
			sql += "    gubun,      \n";
			sql += "    compcd,     \n";
			sql += "    uselist,     \n";
			sql += "    aduserid,    \n";
            sql += "    type,        \n";
            sql += "    notice_gubun,\n";
			sql += "    filecnt      \n";
			sql += " from            \n";
			sql += " (select         \n";
			sql += "    rownum,       \n";
			sql += "    x.seq,        \n";
			sql += "    x.addate,     \n";
			sql += "    x.adtitle,    \n";
			sql += "    x.adname,     \n";
			sql += "    x.cnt,        \n";
			sql += "    x.luserid,    \n";
			sql += "    x.ldate,      \n";
			sql += "    x.isall,      \n";
			sql += "    x.useyn,      \n";
			sql += "    x.popup,      \n";
			sql += "    x.loginyn,    \n";
			sql += "    x.gubun,      \n";
			sql += "    x.compcd,     \n";
			sql += "    x.uselist,    \n";
			sql += "    x.tabseq,     \n";
			sql += "    x.adcontent,  \n";
			sql += "    x.aduserid,   \n";
			sql += "    x.grcodecd,   \n";
            sql += "    x.type,       \n";
            sql += "    x.notice_gubun,\n";
			sql += "	(select count(realfile) from tz_boardfile where tabseq = x.TABSEQ and seq = x.seq) filecnt ";
			sql += "  from      \n";
			sql += "    TZ_NOTICE x ) a";
			sql += "  where isall = 'Y' ";
			sql += "      and tabseq = " +  v_tabseq;
			sql += "      and notice_gubun <> 'B' ";
			sql += "      and useyn= 'Y'";
			sql += "      and (popup = 'N' or (popup = 'Y' and uselist='Y') )";

			/*if ( v_login.equals("Y") ) {  // �α����� �Ѱ��
			  sql += "      and ( loginyn = 'AL' or loginyn = 'Y' )";
			  sql += "      and ( compcd like '%" +v_comp + "%' )";
			} else { // �α������� �������
			// �α��� �����õǰų� ��ü�� ���õȰ��
			  sql += "      and ( ( loginyn = 'AL' or loginyn = 'N' ) and grcodecd like '%" +tem_grcode + "%' )";
			}*/
			
			if ( !v_searchtext.equals("") ) {      //    �˻�� ������
				v_pageno = 1;   //      �˻��� ��� ù��° �������� �ε��ȴ�       
				box.put("p_pageno", String.valueOf(v_pageno));
				if ( v_search.equals("adtitle") ) {                          //    �������� �˻��Ҷ�
					sql += " and adtitle like " + StringManager.makeSQL("%" + v_searchtext + "%");
				} else if ( v_search.equals("adcontents") ) {                //    �������� �˻��Ҷ�
					sql += " and adcontent like " + StringManager.makeSQL("%" + v_searchtext + "%");
				} else if ( v_search.equals("adname") ) {                //    �ۼ��ڷ� �˻��Ҷ�
					sql += " and adname like " + StringManager.makeSQL("%" + v_searchtext + "%");
				}
			}
            
            if( !"".equals(v_notice_type) ) {
                sql += " and type = " + StringManager.makeSQL(v_notice_type);
            }
			
            sql += "     order by addate desc ";//--) where rownum < 4";
			//sql += "    --order by seq desc                                                                    ";

/*            
            
            
            
            
            
            
			sql = " select \n";
			sql += "    rownum,     \n";
			sql += "    seq,        \n";
			sql += "    addate,     \n";
			sql += "    adtitle,    \n";
			sql += "    adname,     \n";
			sql += "    cnt,        \n";
			sql += "    luserid,    \n";
			sql += "    ldate,      \n";
			sql += "    isall,      \n";
			sql += "    useyn,      \n";
			sql += "    popup,      \n";
			sql += "    loginyn,    \n";
			sql += "    gubun,      \n";
			sql += "    compcd,     \n";
			sql += "    uselist,     \n";
			sql += "    aduserid,    \n";
            sql += "    type,        \n";
            sql += "    notice_gubun,\n";
			sql += "    filecnt      \n";
			sql += " from            \n";
			sql += " (select         \n";
			sql += "    rownum,       \n";
			sql += "    x.seq,        \n";
			sql += "    x.addate,     \n";
			sql += "    x.adtitle,    \n";
			sql += "    x.adname,     \n";
			sql += "    x.cnt,        \n";
			sql += "    x.luserid,    \n";
			sql += "    x.ldate,      \n";
			sql += "    x.isall,      \n";
			sql += "    x.useyn,      \n";
			sql += "    x.popup,      \n";
			sql += "    x.loginyn,    \n";
			sql += "    x.gubun,      \n";
			sql += "    x.compcd,     \n";
			sql += "    x.uselist,    \n";
			sql += "    x.tabseq,     \n";
			sql += "    x.adcontent,  \n";
			sql += "    x.aduserid,   \n";
            sql += "    x.grcodecd,   \n";
            sql += "    x.type,       \n";
            sql += "    x.notice_gubun,\n";
			sql += "	(select count(realfile) from tz_boardfile where tabseq = x.TABSEQ and seq = x.seq) filecnt ";
			sql += "  from      \n";
			sql += "    TZ_NOTICE x WHERE x.YEAR = TO_CHAR(SYSDATE,'YYYY')) a";
			sql += "  where ";
			sql += "  isall = 'N' ";
			sql += "      and tabseq = " +  v_tabseq;
			sql += "      and notice_gubun <> 'B' ";
			sql += "      and useyn= 'Y'";
			sql += "      and (popup = 'N' or (popup = 'Y' and uselist='Y') )";
			
//			if ( v_login.equals("Y") ) {  // �α����� �Ѱ��
//			  sql += "      and ( loginyn = 'AL' or loginyn = 'Y' )";
//			  sql += "      and ( compcd like '%" +v_comp + "%' )";
//			} else { // �α������� �������
//			// �α��� �����õǰų� ��ü�� ���õȰ��
//			  sql += "      and ( ( loginyn = 'AL' or loginyn = 'N' ) and grcodecd like '%" +tem_grcode + "%' )";
//			}
			
			if(!tem_grcode.equals("")) {
				sql = sql + "	and grcodecd like " +SQLString.Format("%"+tem_grcode+"%");
			}
			
			if ( !v_searchtext.equals("") ) {      //    �˻�� ������
				v_pageno = 1;   //      �˻��� ��� ù��° �������� �ε��ȴ�       
				if ( v_search.equals("adtitle") ) {                          //    �������� �˻��Ҷ�
					sql += " and adtitle like " + StringManager.makeSQL("%" + v_searchtext + "%");
				} else if ( v_search.equals("adcontents") ) {                //    �������� �˻��Ҷ�
					sql += " and adcontent like " + StringManager.makeSQL("%" + v_searchtext + "%");
				} else if ( v_search.equals("adname") ) {                //    �ۼ��ڷ� �˻��Ҷ�
					sql += " and adname like " + StringManager.makeSQL("%" + v_searchtext + "%");
				}
			}
            
            if( !"".equals(v_notice_type) ) {
            	sql += " and type = " + StringManager.makeSQL(v_notice_type);
            }
            
//            if ( v_gadmin.equals("K") || v_gadmin.equals("H")) { // ȸ�� ������ �̰ų� �����׷� ������ �ϰ��
//            	
//            	sql += " union all  \n";
//            	sql += sql;
//            	if(!tem_grcode.equals("")) {
//            		sql += "	and grcodecd not like " +SQLString.Format("%"+tem_grcode+"%");
//    			}
//            	
//            	sql += "      and compcd like  "+SQLString.Format("%"+v_comp+"%");
//            	
//            	if ( !v_searchtext.equals("") ) {      //    �˻�� ������
//    				v_pageno = 1;   //      �˻��� ��� ù��° �������� �ε��ȴ�       
//    				if ( v_search.equals("adtitle") ) {                          //    �������� �˻��Ҷ�
//    					sql += " and adtitle like " + StringManager.makeSQL("%" + v_searchtext + "%");
//    				} else if ( v_search.equals("adcontents") ) {                //    �������� �˻��Ҷ�
//    					sql += " and adcontent like " + StringManager.makeSQL("%" + v_searchtext + "%");
//    				} else if ( v_search.equals("adname") ) {                //    �ۼ��ڷ� �˻��Ҷ�
//    					sql += " and adname like " + StringManager.makeSQL("%" + v_searchtext + "%");
//    				}
//    			}
//			}
//System.out.println("Notice : "+sql);
            sql += " order by addate desc ";
            
            
*/            
            
            
            
            
            
            
            
            
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
	* �Ϲ� ��������ȭ�� ����Ʈ(Ȩ������more)
	* @param box          receive from the form object and session
	* @return ArrayList   �������� ����Ʈ(��ü���� ����)
	* @throws Exception
	*/
	public ArrayList selectListNoticeHome(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		String              sql1    = "";
		NoticeData data = null;
		DataBox             dbox    = null;

        int v_tabseq = Integer.parseInt(box.getStringDefault("p_tabseq","12"));
        
        String v_login  = "";
        String v_comp   = box.getSession("comp");
        String tem_grcode   = box.getStringDefault("tem_grcode", box.getSession("tem_grcode") );        
        String v_searchtext = box.getString("p_searchtext");
		String v_search     = box.getString("p_search");
		String s_gadmin     = box.getSession("gadmin");
		String v_gadmin     = "";
		
		if(!s_gadmin.equals("")) {
			v_gadmin = s_gadmin.substring(0,1);
		}
		
		int v_pageno        = box.getInt("p_pageno");
        
        if ( box.getSession("userid").equals("") ) { 
        	v_login = "N";
        } else { 
        	//System.out.println("v_compL:"+v_comp.length());
        	if(v_comp.length()>4) {


        	}
        	v_login = "Y";
        }
        String v_notice_type = box.getString("p_notice_type");
		
		try { 
			connMgr = new DBConnectionManager();

			list = new ArrayList();

			sql += " select \n";
			sql += "    rownum,     \n";
			sql += "    seq,        \n";
			sql += "    addate,     \n";
			sql += "    adtitle,    \n";
			sql += "    adname,     \n";
			sql += "    cnt,        \n";
			sql += "    luserid,    \n";
			sql += "    ldate,      \n";
			sql += "    isall,      \n";
			sql += "    useyn,      \n";
			sql += "    popup,      \n";
			sql += "    loginyn,    \n";
			sql += "    gubun,      \n";
			sql += "    compcd,     \n";
			sql += "    uselist,     \n";
			sql += "    aduserid,    \n";
            sql += "    type,        \n";
            sql += "    notice_gubun,\n";
			sql += "    filecnt      \n";
			sql += " from            \n";
			sql += " (select         \n";
			sql += "    rownum,       \n";
			sql += "    x.seq,        \n";
			sql += "    x.addate,     \n";
			sql += "    x.adtitle,    \n";
			sql += "    x.adname,     \n";
			sql += "    x.cnt,        \n";
			sql += "    x.luserid,    \n";
			sql += "    x.ldate,      \n";
			sql += "    x.isall,      \n";
			sql += "    x.useyn,      \n";
			sql += "    x.popup,      \n";
			sql += "    x.loginyn,    \n";
			sql += "    x.gubun,      \n";
			sql += "    x.compcd,     \n";
			sql += "    x.uselist,    \n";
			sql += "    x.tabseq,     \n";
			sql += "    x.adcontent,  \n";
			sql += "    x.aduserid,   \n";
            sql += "    x.grcodecd,   \n";
            sql += "    x.type,       \n";
            sql += "    x.notice_gubun,\n";
			sql += "	(select count(realfile) from tz_boardfile where tabseq = x.TABSEQ and seq = x.seq) filecnt ";
			sql += "  from      \n";
			sql += "    TZ_NOTICE x ) a";
			sql += "  where ";
			sql += "  isall = 'N' ";
			sql += "      and tabseq = " +  v_tabseq;
			sql += "      and notice_gubun <> 'B' ";
			sql += "      and useyn= 'Y'";
			sql += "      and (popup = 'N' or (popup = 'Y' and uselist='Y') )";
			
			/*if ( v_login.equals("Y") ) {  // �α����� �Ѱ��
			  sql += "      and ( loginyn = 'AL' or loginyn = 'Y' )";
			  sql += "      and ( compcd like '%" +v_comp + "%' )";
			} else { // �α������� �������
			// �α��� �����õǰų� ��ü�� ���õȰ��
			  sql += "      and ( ( loginyn = 'AL' or loginyn = 'N' ) and grcodecd like '%" +tem_grcode + "%' )";
			}*/
			
			if(!tem_grcode.equals("")) {
				sql = sql + "	and grcodecd like " +SQLString.Format("%"+tem_grcode+"%");
			}
			
			if ( !v_searchtext.equals("") ) {      //    �˻�� ������
				v_pageno = 1;   //      �˻��� ��� ù��° �������� �ε��ȴ�       
				if ( v_search.equals("adtitle") ) {                          //    �������� �˻��Ҷ�
					sql += " and adtitle like " + StringManager.makeSQL("%" + v_searchtext + "%");
				} else if ( v_search.equals("adcontents") ) {                //    �������� �˻��Ҷ�
					sql += " and adcontent like " + StringManager.makeSQL("%" + v_searchtext + "%");
				} else if ( v_search.equals("adname") ) {                //    �ۼ��ڷ� �˻��Ҷ�
					sql += " and adname like " + StringManager.makeSQL("%" + v_searchtext + "%");
				}
			}
            
            if( !"".equals(v_notice_type) ) {
            	sql += " and type = " + StringManager.makeSQL(v_notice_type);
            }
            
//            if ( v_gadmin.equals("K") || v_gadmin.equals("H")) { // ȸ�� ������ �̰ų� �����׷� ������ �ϰ��
//            	
//            	sql += " union all  \n";
//            	sql += sql;
//            	if(!tem_grcode.equals("")) {
//            		sql += "	and grcodecd not like " +SQLString.Format("%"+tem_grcode+"%");
//    			}
//            	
//            	sql += "      and compcd like  "+SQLString.Format("%"+v_comp+"%");
//            	
//            	if ( !v_searchtext.equals("") ) {      //    �˻�� ������
//    				v_pageno = 1;   //      �˻��� ��� ù��° �������� �ε��ȴ�       
//    				if ( v_search.equals("adtitle") ) {                          //    �������� �˻��Ҷ�
//    					sql += " and adtitle like " + StringManager.makeSQL("%" + v_searchtext + "%");
//    				} else if ( v_search.equals("adcontents") ) {                //    �������� �˻��Ҷ�
//    					sql += " and adcontent like " + StringManager.makeSQL("%" + v_searchtext + "%");
//    				} else if ( v_search.equals("adname") ) {                //    �ۼ��ڷ� �˻��Ҷ�
//    					sql += " and adname like " + StringManager.makeSQL("%" + v_searchtext + "%");
//    				}
//    			}
//			}
//System.out.println("Notice : "+sql);
            sql += " order by addate desc ";
			
			ls = connMgr.executeQuery(sql);
			
			row = 7;
            
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
// ------------------------------------------------------------------------------------------------

	
	
// ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == =ȸ�� select method ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == =
	/**
	* ��������  ȸ��� select
	* @param box          receive from the form object and session
	* @return ArrayList   ���� ����Ʈ
	* @throws Exception
    */
	public ArrayList selectComp(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		DataBox             dbox    = null;
		StringTokenizer     tokenizer = null;
		
	    String s_userid   = box.getSession("userid");
		String s_gadmin   = box.getSession("gadmin"); 
		String v_gadmin = "";
		String v_grcode   = box.getString("p_grcodeChked");
		
		if ( !s_gadmin.equals("") ) { 
		  v_gadmin = s_gadmin.substring(0,1);
		}

		try { 
			connMgr = new DBConnectionManager();
			list = new ArrayList();
			
			if(!v_grcode.equals("")){
				tokenizer = new StringTokenizer(v_grcode, ",");
				v_grcode = "";
				
				while(tokenizer.hasMoreTokens()){
					v_grcode	+= "'"+tokenizer.nextToken()+"',";
				}
				v_grcode = v_grcode.substring(0, v_grcode.length()-1);
				
		   	 	sql += " select compnm, b.comp from tz_compclass a, tz_grcomp b ";
		   		sql += "    where a.comp = b.comp and show_yn = 'Y' and b.grcode in ("+v_grcode+") " ;
	
			    if ( v_gadmin.equals("K") ) { // ȸ�������
				  sql += "    and   comp IN (select comp from tz_compman where userid=" + StringManager.makeSQL(s_userid) + " and gadmin= " + StringManager.makeSQL(s_gadmin) + ")";
			    } else if ( v_gadmin.equals("H") ) {  // �����׷������
			      sql += "    and   comp IN (select comp from tz_grcomp where grcode in (select grcode from tz_grcodeman where userid = " + StringManager.makeSQL(s_userid) + " and gadmin= " + StringManager.makeSQL(s_gadmin) + " ) ) "; 
			    }
	
				sql += "    order by compnm";   			
	
				ls = connMgr.executeQuery(sql);
	
				while ( ls.next() ) {
					dbox = ls.getDataBox();
					list.add(dbox);
				}
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
	 * ��������  ȸ��� select2
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public String selectComp2(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		DataBox             dbox    = null;
		StringTokenizer     tokenizer = null;

	    String s_userid   = box.getSession("userid");
		String s_gadmin   = box.getSession("gadmin"); 
		String v_gadmin   = "";
		String v_grcode   = box.getString("p_grcode");
		String v_compCheckedStorge   = box.getString("p_compCheckedStorge");
		
		
		if ( !s_gadmin.equals("") ) { 
		  v_gadmin = s_gadmin.substring(0,1);
		}

		if(v_grcode.equals("")){
			return "";
		}
		try { 
			connMgr = new DBConnectionManager();
			list = new ArrayList();
			
			tokenizer = new StringTokenizer(v_grcode, ",");
			v_grcode = "";
			
			while(tokenizer.hasMoreTokens()){
				v_grcode	+= "'"+tokenizer.nextToken()+"',";
			}
			v_grcode = v_grcode.substring(0, v_grcode.length()-1);
			
	   	 	sql += " select compnm, b.comp from tz_compclass a, tz_grcomp b ";
	   		sql += "    where a.comp = b.comp and show_yn = 'Y' and b.grcode in ("+v_grcode+") " ;

		    if ( v_gadmin.equals("K") ) { // ȸ�������
			  sql += "    and   a.comp IN (select comp from tz_compman where userid=" + StringManager.makeSQL(s_userid) + " and gadmin= " + StringManager.makeSQL(s_gadmin) + ")";
		    } else if ( v_gadmin.equals("H") ) {  // �����׷������
		      sql += "    and   a.comp IN (select comp from tz_grcomp where grcode in (select grcode from tz_grcodeman where userid = " + StringManager.makeSQL(s_userid) + " and gadmin= " + StringManager.makeSQL(s_gadmin) + " ) ) "; 
		    }

			sql += "    order by compnm";   			

			ls = connMgr.executeQuery(sql);

			sql = "";
			int i = 0;
			
			String[] values = v_compCheckedStorge.split(",");

			String flag = "";
			
			while ( ls.next() ) {
				dbox = ls.getDataBox();
				flag = "checked";
			    int v_lineChang = 5;
			    
			        if( ( (i+6)%v_lineChang == 1) ){
			        	sql += "<tr height=\"18\" class=\"table_02_2\">";
			    	}    
			        if(values.length > 0 && !values[0].equals("")){
				        for( int x = 0; x < values.length; x++ ){
				        	if(values[x].equals(ls.getString("comp"))){
				        		flag = "";
				        		break;
				        	}
				        }
			        }else{
			        	flag = "checked";
			        }
			        sql += "<td class=\"table_02_2\" width=\"170\" ><input type=\"checkbox\" name=\"p_comp\" "+ flag +" value=\""+ls.getString("comp")+"\" onclick=\"compCheckedStorge()\" >"+ls.getString("compnm")+"</td>";
			        
			        if( ( (i+1)%v_lineChang == 0) ){
			        	sql += "</tr>";
			        }
			    i++;        
		    }
			sql += "</td></tr>";
		}
		
		catch ( Exception ex ) { 
			   ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return sql;
	}
	
	/**
	* ��������  default compcd ����
	* @param box          receive from the form object and session
	* @return ArrayList   ���� ����Ʈ
	* @throws Exception
    */
	
	public String selectDefalutComp(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		// NoticeData data = null;
		DataBox             dbox    = null;

	    String s_userid   = box.getSession("userid");
		String s_gadmin   = box.getSession("gadmin"); 
		String v_gadmin = "";
		String returnValue = "";

		if ( !s_gadmin.equals("") ) { 
		  v_gadmin = s_gadmin.substring(0,1);
		}

		try { 
			connMgr = new DBConnectionManager();

			list = new ArrayList();
			
	   	 	sql += " select compnm, comp from tz_compclass ";
	   		sql += "    where comptype='2'" ;  	   		
	   		sql += "    and show_yn = 'Y'" ;

		    if ( v_gadmin.equals("K") ) { // ȸ�������
			  sql += "    and   comp IN (select comp from tz_compman where userid= " + StringManager.makeSQL(s_userid) + " and gadmin= " + StringManager.makeSQL(s_gadmin) + ")";
		    } else if ( v_gadmin.equals("H") ) {  // �����׷������
		      sql += "    and   comp IN (select comp from tz_grcomp where grcode in (select grcode from tz_grcodeman where userid = " + StringManager.makeSQL(s_userid) + " and gadmin= " + StringManager.makeSQL(s_gadmin) + " ) ) "; 
		    }

			sql += "    order by compnm";   			
		
			ls = connMgr.executeQuery(sql);

			if ( ls.next() ) { 
			  // data = new NoticeData();
			  // data.setCompnm( ls.getString("compnm") );				
			  // data.setComp( ls.getString("comp").substring(0,4));
              // dbox = ls.getDataBox();
              returnValue = ls.getString("comp");
              if(returnValue.length()>3) {
            	  returnValue = returnValue.substring(0,4);
              }
			}
			
			if ( v_gadmin.equals("A") ) { 
				returnValue = "ALL";
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
		return returnValue;
	}
	
	
	/**
	* ���Ѻ� ȸ�縮��Ʈ 
	* @param box          receive from the form object and session
	* @return ArrayList   ���Ѻ� ȸ�縮��Ʈ
	* @throws Exception
	*/
	
	public ArrayList selectCompany(RequestBox box, String compcd) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		NoticeData data = null; 
		
		try { 
			connMgr = new DBConnectionManager();

			list = new ArrayList();
			
	   	 	sql += " select compnm, comp from tz_compclass ";
	   		sql += "    where comptype='2' and substr(comp,0,4) in (" +compcd + ")" ;
	   		sql += "    and show_yn = 'Y'" ;
			sql += "    order by compnm";   			
			// System.out.println(sql);
		
			ls = connMgr.executeQuery(sql);

			while ( ls.next() ) { 
				data = new NoticeData();

				data.setCompnm( ls.getString("compnm") );				
				// data.setComp( ls.getString("comp").substring(0,4));
				list.add(data);
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
	* ���Ѻ� ȸ�縮��Ʈ 
	* @param box          receive from the form object and session
	* @return ArrayList   ���Ѻ� ȸ�縮��Ʈ
	* @throws Exception
	*/
	
	public ArrayList selectGrcode(RequestBox box, String grcodecd) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		DataBox             dbox    = null; 
		
		try { 
			connMgr = new DBConnectionManager();

			list = new ArrayList();
			
	   	 	sql += " select grcodenm, grcode from tz_grcode ";
	   		sql += "    where grcode in (" +grcodecd + ")" ;
			sql += "    order by grcodenm";   			
            ls = connMgr.executeQuery(sql);

			while ( ls.next() ) { 
				dbox = (DataBox)ls.getDataBox();
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
	

// ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == =ȸ�� select method end ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == =





    /**
    * �������� ���ο� �ڷ����� ���
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */

	 public	int	insertUpFile(DBConnectionManager connMgr, int p_tabseq, int p_seq, RequestBox	box) throws	Exception { 
		ListSet	ls = null;
		PreparedStatement pstmt2 = null;
		String              sql     = "";
		String sql2	= "";
		int	isOk2 =	1;

		// ----------------------   ���ε�Ǵ� ������ ������	�˰� �ڵ��ؾ��Ѵ�  --------------------------------
// System.out.println("333333333333");
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
			sql	= "select nvl(max(fileseq),	0) from	tz_boardfile	where tabseq = " +p_tabseq + " and seq = " +	p_seq ;

			ls = connMgr.executeQuery(sql);
			ls.next();
			int	v_fileseq =	ls.getInt(1) + 1;
			ls.close();
			// ------------------------------------------------------------------------------------

			//// //// //// //// //// //// //// //// // 	 ���� table	�� �Է�	 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
			sql2 =	"insert	into tz_boardfile(tabseq, seq, fileseq, realfile, savefile, luserid,	ldate)";
			sql2 +=	" values (?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";

			pstmt2 = connMgr.prepareStatement(sql2);

			for ( int	i =	0; i < FILE_LIMIT; i++ )	{ 
				if ( 	!v_realFileName	[i].equals(""))	{ 		// 		���� ���ε�	�Ǵ� ���ϸ�	üũ�ؼ� db�� �Է��Ѵ�
					pstmt2.setInt(1, p_tabseq);
					pstmt2.setInt(2, p_seq);
					pstmt2.setInt(3, v_fileseq);
					pstmt2.setString(4,	v_realFileName[i]);
					pstmt2.setString(5,	v_newFileName[i]);
					pstmt2.setString(6,	s_userid);
					isOk2 =	pstmt2.executeUpdate();
					v_fileseq++;
					// System.out.println("p_seq:::" +p_seq);
// System.out.println("v_fileseq:::" +v_fileseq);
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
        String v_type   = box.getString("p_type");
		int	v_seq =	box.getInt("p_seq");

		try	{ 

            // ----------------------   ��Խ�������������  ������ tabseq�� �����Ѵ� ----------------------------
            sql = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);
// System.out.println("file delete:::::::::::::" +sql);
            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_tabseq = ls.getInt(1);
            ls.close();
            // ------------------------------------------------------------------------------------


			sql3 = "delete from tz_boardfile where tabseq = " + v_tabseq + " and seq =? and fileseq = ?";
			pstmt3 = connMgr.prepareStatement(sql3);

			for ( int	i =	0; i < p_filesequence.size(); i++ ) { 
				// System.out.println("1111111111111111111111111111111111111111111111111111");
				int	v_fileseq =	Integer.parseInt((String)p_filesequence.elementAt(i));
				pstmt3.setInt(1, v_seq);
				pstmt3.setInt(2, v_fileseq);
				isOk3 =	pstmt3.executeUpdate();
				// System.out.println(isOk3);
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


	
	
	

	/**
	* ��������ȭ�� ��������������
	* @param box          receive from the form object and session
	* @return ArrayList   ��ȸ�� ����������
	* @throws Exception
	*/
   public NoticeData selectViewPre(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		String              sql     = "";
		NoticeData data = null;

        int v_tabseq = box.getInt("p_tabseq");
		String v_seq = box.getString("p_seq");
        String v_gubun = box.getString("p_gubun");
		String v_gubun_query = "";

		if ( v_gubun.equals("Y") ) { 		// ��ü�����ϰ��
			v_gubun_query = "('Y')";
		} else { 						// ��ü�������ƴҰ��(�Ϲ�,�˾�)
			v_gubun_query = "('N','P')";
		}

		try { 
			connMgr = new DBConnectionManager();

			sql += " select seq,gubun, addate, adtitle, adname, adcontent, cnt, luserid, ldate, type from TZ_NOTICE ";
			sql += "  where gubun in " + v_gubun_query;
			sql += "    and tabseq = " +  v_tabseq;
			sql += "    and seq   <  " + StringManager.makeSQL(v_seq);
			sql += "  order by seq desc                                                                       ";
		
			ls = connMgr.executeQuery(sql);

			if ( ls.next() ) { 
				data = new NoticeData();
				data.setSeq( ls.getInt("seq") );
				data.setGubun( ls.getString("gubun") );
				data.setAddate( ls.getString("addate") );
				data.setAdtitle( ls.getString("adtitle") );
				data.setAdname( ls.getString("adname") );
				data.setAdcontent( ls.getString("adcontent") );
				data.setCnt( ls.getInt("cnt") );
				data.setLuserid( ls.getString("luserid") );
				data.setLdate( ls.getString("ldate") );
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
		return data;
	}


	/**
	* ��������ȭ�� ��������������
	* @param box          receive from the form object and session
	* @return ArrayList   ��ȸ�� ����������
	* @throws Exception
	*/
   public NoticeData selectViewNext(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		String              sql     = "";
		NoticeData data = null;

        int v_tabseq = box.getInt("p_tabseq");
		String v_seq   = box.getString("p_seq");
		String v_gubun = box.getString("p_gubun");

		String v_gubun_query = "";

		if ( v_gubun.equals("Y") ) { 		// ��ü�����ϰ��
			v_gubun_query = "('Y')";
		} else { 						// ��ü�������ƴҰ��(�Ϲ�,�˾�)
			v_gubun_query = "('N','P')";
		}

		try { 
			connMgr = new DBConnectionManager();

			sql += " select seq, gubun, addate, adtitle, adname, adcontent, cnt, luserid, ldate, type from TZ_NOTICE ";
			sql += "  where gubun in " + v_gubun_query;
			sql += "    and tabseq = " +  v_tabseq;
			sql += "    and seq > " + StringManager.makeSQL(v_seq);
			sql += "  order by seq asc                                                                        ";

			ls = connMgr.executeQuery(sql);

			if ( ls.next() ) { 
				data = new NoticeData();
				data.setSeq( ls.getInt("seq") );
				data.setGubun( ls.getString("gubun") );
				data.setAddate( ls.getString("addate") );
				data.setAdtitle( ls.getString("adtitle") );
				data.setAdname( ls.getString("adname") );
				data.setAdcontent( ls.getString("adcontent") );
				data.setCnt( ls.getInt("cnt") );
				data.setLuserid( ls.getString("luserid") );
				data.setLdate( ls.getString("ldate") );
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
		return data;
	}
	
	
   /**
    * ��ü ����(���ο� �ѷ����� ��)
    * @param box          receive from the form object and session
    * @return ArrayList   ��ü���� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectListNoticeWithAll(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        NoticeData data = null;
        DataBox             dbox    = null;

       int v_tabseq = box.getInt("p_tabseq");
       
       String v_login  = "";
       String v_comp   = box.getSession("comp");
       String tem_grcode        = box.getStringDefault("tem_grcode", box.getSession("tem_grcode") );
       
       if(tem_grcode.equals("")){
       	tem_grcode="N000001";
       }
       
       String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");
       
       if ( box.getSession("userid").equals("") ) { 
        v_login = "N";
       } else { 
           v_comp = (v_comp.length()>=4) ? v_comp.substring(0, 4) : v_comp;
        v_login = "Y";
       }

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();
            sql  = " select isall, seq, addate, adtitle, adname, cnt, luserid, ldate, type, get_codenm('0076', notice_gubun) notice_gubun from                      ";
            sql += " ( select isall, rownum, seq, addate, adtitle, adname,  cnt, luserid, ldate, type, notice_gubun from TZ_NOTICE  ";
            // sql += "    where gubun = 'Y'                                                                          ";
            sql += "    where  ";
            sql += "      tabseq = " +  v_tabseq;
            sql += "      and useyn= 'Y'";
            sql += "      and notice_gubun <> 'B'";
            sql += "      and (popup = 'N' or (popup = 'Y' and uselist='Y') )";
            //sql += " and type = 'Y'";
            
            if ( v_login.equals("Y") ) {  // �α����� �Ѱ��
              sql += "      and ( loginyn = 'AL' or loginyn = 'Y' )";
              sql += "      and ( compcd like '%" +v_comp + "%' or grcodecd like '%" +tem_grcode + "%' )";
            } else { // �α������� �������
            // �α��� �����õǰų� ��ü�� ���õȰ��
              sql += "      and ( ( loginyn = 'AL' or loginyn = 'N' ) and grcodecd like '%" +tem_grcode + "%' )";
            }
            
            if (!tem_grcode.equals("")) {
            	sql += "	and grcodecd like "+SQLString.Format("%"+tem_grcode+"%");
            }
            
            sql += "    order by addate desc )";
            sql += " where rownum <= 4";
//System.out.println("@@@@@@@@@ : "+sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new NoticeData();
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
    * ���� ��������(���ο� �ѷ����� ��)
    * @param box          receive from the form object and session
    * @return ArrayList   ��ü���� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectListNoticeWithPart(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        NoticeData data = null;
        DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
        
        String v_login  = "";
        String v_comp   = box.getSession("comp");
        String tem_grcode        = box.getStringDefault("tem_grcode", box.getSession("tem_grcode") );        
        
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");

        if ( box.getSession("userid").equals("") ) { 
            v_login = "N";
        } else { 
            v_comp = (v_comp.length()>=4) ? v_comp.substring(0, 4) : v_comp;
            v_login = "Y";
        }

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();
            sql  = " select isall, seq, addate, adtitle, adname, cnt, luserid, ldate, type, notice_gubun  from                      ";
            sql += " ( select isall, rownum, seq, addate, adtitle, adname,  cnt, luserid, ldate, type, notice_gubun from TZ_NOTICE  ";
            // sql += "    where gubun = 'Y'                                                                          ";
            sql += "    where  ";
            sql += "      tabseq = " +  v_tabseq;
            sql += "      and useyn= 'Y'";
            sql += "      and (popup = 'N' or (popup = 'Y' and uselist='Y') )";
            sql += " and type = 'N'";

            /*if ( v_login.equals("Y") ) {  // �α����� �Ѱ��
              sql += "      and ( loginyn = 'AL' or loginyn = 'Y' )";
              sql += "      and ( compcd like '%" +v_comp + "%' )";
            } else { // �α������� �������
            // �α��� �����õǰų� ��ü�� ���õȰ��
              sql += "      and ( ( loginyn = 'AL' or loginyn = 'N' ) and grcodecd like '%" +tem_grcode + "%' )";
            }*/
            sql += "    order by seq desc )";
            sql += " where rownum < 5";

            
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new NoticeData();
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

    public ArrayList getCodeData(RequestBox box, String gubunCode) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox            data    = null;

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = "SELECT                                                   \n" +
                   "    code,                                                \n" +
                   "    codenm                                               \n" +
                   "FROM                                                     \n" +
                   "    tz_code                                              \n" +
                   "WHERE                                                    \n" +
                   "    gubun  = " + StringManager.makeSQL(gubunCode) + "    \n" +
                   "order by                                                 \n" +
                   "    code asc                                             \n" ;

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = ls.getDataBox();
                list.add(data);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null      ) { try { ls.close();               } catch ( Exception e )   { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }   
    
    /**
     * ���� ���������ȳ� ����Ʈ
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectTrainingList(RequestBox box) throws Exception { 
    	DBConnectionManager connMgr     = null;
    	ListSet             ls      = null;
    	ArrayList           list    = null;
    	DataBox             dbox    = null;
    	StringBuffer        strSQL  = null; 
    	
    	String v_trainingGubun = box.getString("p_trainingGubun");
    	
    	try { 
    		connMgr = new DBConnectionManager();
    		
    		list = new ArrayList();
    		
    		strSQL = new StringBuffer();
    		strSQL.append("\n SELECT ") ;
    		strSQL.append("\n         (SELECT UPPERCLASS FROM TZ_SUBJ WHERE SUBJ = A.SUBJ) AS CLS,  --// PRF : ����, PAR:�кθ�, EXT:������ ") ;
    		strSQL.append("\n         YEAR, ") ;
    		strSQL.append("\n          TO_NUMBER(SUBJSEQ) AS GISU, ") ;
    		strSQL.append("\n         (SELECT CLASSNAME FROM TZ_SUBJATT WHERE UPPERCLASS = (SELECT UPPERCLASS FROM TZ_SUBJ WHERE SUBJ = A.SUBJ)) AS TNM, ") ;
    		strSQL.append("\n         SUBJ, ") ;
    		strSQL.append("\n         SUBJNM, ") ;
    		strSQL.append("\n         PROPEND, ") ;
    		strSQL.append("\n         0 AS GUBUN ") ;
    		strSQL.append("\n FROM    TZ_SUBJSEQ A ") ;
    		strSQL.append("\n WHERE   ISDELETED = 'N' ") ;
    		strSQL.append("\n          AND ISVISIBLE = 'Y' ") ;
    		//strSQL.append("\n          AND YEAR = TO_CHAR(SYSDATE,'YYYY') ") ;
    		strSQL.append("\n          AND TO_CHAR(SYSDATE,'YYYYMMDDHH24') BETWEEN PROPSTART AND PROPEND ") ;
    		strSQL.append("\n UNION ALL ") ;
    		strSQL.append("\n SELECT ") ;
    		strSQL.append("\n         (SELECT UPPERCLASS FROM TZ_SUBJ WHERE SUBJ = A.SUBJ) AS CLS,  --// PRF : ����, PAR:�кθ�, EXT:������ ") ;
    		strSQL.append("\n         YEAR, ") ;
    		strSQL.append("\n          TO_NUMBER(SUBJSEQ) AS GISU, ") ;
    		strSQL.append("\n         (SELECT CLASSNAME FROM TZ_SUBJATT WHERE UPPERCLASS = (SELECT UPPERCLASS FROM TZ_SUBJ WHERE SUBJ = A.SUBJ)) AS TNM, ") ;
    		strSQL.append("\n         SUBJ, ") ;
    		strSQL.append("\n         SUBJNM, ") ;
    		strSQL.append("\n         PROPEND, ") ;
    		strSQL.append("\n         1 AS GUBUN ") ;
    		strSQL.append("\n FROM    TZ_SUBJSEQ A ") ;
    		strSQL.append("\n WHERE   ISDELETED = 'N' ") ;
    		strSQL.append("\n          AND ISVISIBLE = 'Y' ") ;
    		strSQL.append("\n          AND TO_CHAR(SYSDATE,'YYYYMMDDHH24') < PROPSTART ") ;
    		//strSQL.append("\n ORDER BY GUBUN ASC, YEAR DESC, GISU ASC ") ;
    		strSQL.append("\n UNION ALL ") ;
    		strSQL.append("\n SELECT ") ;
    		strSQL.append("\n         (SELECT UPPERCLASS FROM TZ_SUBJ WHERE SUBJ = A.SUBJ) AS CLS,  --// PRF : ����, PAR:�кθ�, EXT:������ ") ;
    		strSQL.append("\n         YEAR, ") ;
    		strSQL.append("\n          TO_NUMBER(SUBJSEQ) AS GISU, ") ;
    		strSQL.append("\n         (SELECT CLASSNAME FROM TZ_SUBJATT WHERE UPPERCLASS = (SELECT UPPERCLASS FROM TZ_SUBJ WHERE SUBJ = A.SUBJ)) AS TNM, ") ;
    		strSQL.append("\n         SUBJ, ") ;
    		strSQL.append("\n         SUBJNM, ") ;
    		strSQL.append("\n         PROPEND, ") ;
    		strSQL.append("\n         2 AS GUBUN ") ;
    		strSQL.append("\n FROM    TZ_SUBJSEQ A ") ;
    		strSQL.append("\n WHERE   ISDELETED = 'N' ") ;
    		strSQL.append("\n          AND ISVISIBLE = 'Y' ") ;
    		strSQL.append("\n          AND TO_CHAR(SYSDATE,'YYYYMMDDHH24') > PROPEND ") ;
    		strSQL.append("\n ORDER BY GUBUN ASC, YEAR DESC, GISU ASC ") ;
    		
    		
    		logger.info("��ü���� : " + strSQL);
    		
    		ls = connMgr.executeQuery(strSQL.toString());
    		
    		while ( ls.next() ) { 
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	}
    	catch ( Exception ex ) { 
    		ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
    		throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
    	}
    	finally { 
    		if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
    		if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	}
    	return list;
    }
}
