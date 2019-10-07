// **********************************************************
//  1. f      ��: ����̿��� �� ��¾�ü(�迭��) ���
//  2. �wα׷��� : OutUserAdminBean.java
//  3. ��      ��: ����̿��� �� ��¾�ü(�迭��) ���
//  4. ȯ      ��: JDK 1.4
//  5. ��      o: 1.0
//  6. ��      ��: ChungHyun 2008. 10. 01
//  7. ��      d: 
// **********************************************************

package com.ziaan.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;

public class OutUserAdminBean { 
	private ConfigSet config;
	private int row;
	
    public OutUserAdminBean() { 
        try { 
            config = new ConfigSet();  
            //row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        �� ����� ������� row �� �����Ѵ�
            row = Integer.parseInt("30" );        //        �� ����� ������� row �� �����Ѵ�
		} catch( Exception e ) { 
            e.printStackTrace();
        }
    }

	
    /**
    ��¾�ü(�迭��) ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList	�迭��(��¾�ü) ����Ʈ
    */
	public ArrayList selectoutcomp(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ArrayList           list    = null;
		DataBox             dbox    = null;
		ListSet             ls      = null;
		String              sql     = "";

		int		v_pageno = box.getInt("p_pageno");       
		String	v_gubun = box.getString("p_gubun");
		String	v_compnm = box.getString("p_compnm");

        String	v_orderColumn	= box.getString("p_orderColumn");           	// d���� �÷���
        String	v_orderType     = box.getString("p_orderType");           		// d���� ��

		try { 
			connMgr = new DBConnectionManager();

			list = new ArrayList();
			
			sql  =" select comp, compnm, userid, decode(userid,'','',get_name(userid)) name, compgubun, gubun, orga_sort, show_yn, ldate , ispotalloginyn \n";
			//sql +="(select aes_code from tz_comp x where a.comp = x.comp) aes_code ";
			sql +=" from \"TZ_COMPCLASS\" a  \n";
			sql +=" where isdeleted = 'N' \n";
			if ( !v_gubun.equals("ALL") && !v_gubun.equals(""))sql += " and gubun = " + SQLString.Format(v_gubun);
			if ( !v_compnm.equals(""))sql += " and upper(compnm) like upper('%" + v_compnm + "%')";
            if ( v_orderColumn.equals("") ) { 
				sql += " order by comp";
			} else { 
			    sql += " order by " + v_orderColumn + v_orderType;
			}			
			ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);             //  ������� row ���� �����Ѵ�
            ls.setCurrentPage(v_pageno);                    //     �����������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();       //     ��ü ������ �� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();    //     ��ü row �� ��ȯ�Ѵ�

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
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return list;
	}   
	/**
    ��¾�ü(�迭��) ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList	�迭��(��¾�ü) ����Ʈ
	 */
	public ArrayList selectoutcompexcel(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ArrayList           list    = null;
		DataBox             dbox    = null;
		ListSet             ls      = null;
		String              sql     = "";
		     
		String	v_gubun = box.getString("p_gubun");
		String	v_compnm = box.getString("p_compnm");
		
		try { 
			connMgr = new DBConnectionManager();
			
			list = new ArrayList();
			
			sql  ="\r\n select comp, compnm, userid, decode(userid,'','',get_name(userid)) name," +
					" compgubun, gubun, orga_sort, show_yn, ldate ";
			sql +="\r\n from TZ_COMPCLASS ";
			sql +="\r\n where isdeleted = 'N' ";
			if ( !v_gubun.equals("ALL") && !v_gubun.equals(""))sql += "\r\n  and gubun = " + SQLString.Format(v_gubun);
			if ( !v_compnm.equals(""))sql += "\r\n  and upper(compnm) like upper('%" + v_compnm + "%')";
			
			sql += "\r\n  order by comp";
				
			ls = connMgr.executeQuery(sql);
			
			while ( ls.next() ) { 
				
				dbox = ls.getDataBox();
				list.add(dbox);
			}
			System.out.println("sql ::> "+ sql);
			
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return list;
	}   
	

    /**
    ��¾�ü(�迭��) d�� ������
    @param box          receive from the form object and session
    @return DataBox	�迭��(��¾�ü) d��
    */
	public DataBox select2outcomp(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		DataBox             dbox    = null;
		ListSet             ls      = null;
		String              sql     = "";
		
		String v_comp = box.getString("p_comp");
		String v_userid = box.getString("p_userid");
		
		try { 
			connMgr = new DBConnectionManager();
			
/* 2006.06.20 - ��d
			sql = "select a.compgubun, a.comp, a.compnm, a.complogo, a.compbirth_date, a.coname, a.telno, a.faxno, a.addr, a.zip, a.gubun, " ;
            sql += "b.userid, b.name, b.pwd, b.jikwinm, b.email, b.handphone, ";
          // 2006.06.12 �߰�
            sql += " a.homepage, a.intro, ";
			sql += " (select x.aes_code from tz_comp x where x.comp = a.comp) aes_code ";
			sql += "from \"TZ_COMPCLASS\" a, \"TZ_MEMBER\" b ";
			sql += "where a.userid = b.userid( +) ";
			sql += "and a.comp = " + SQLString.Format(v_comp) ;
			sql += "and a.isdeleted = 'N' ";
*/
            sql = "SELECT                                                    \n" +
                  "    a.compgubun,                                          \n" +
                  "    a.comp,                                               \n" +
                  "    a.compnm,                                             \n" +
                  "    a.compbirth_date,                                          \n" +
                  "    a.coname,                                             \n" +
                  "    a.telno,                                              \n" +
                  "    a.faxno,                                              \n" +
                  "    a.addr,                                               \n" +
                  "    a.zip,                                                \n" +
                  "    a.gubun,                                              \n" +
                  "    a.email,                                              \n" +
                  "    a.orga_sort, 										 \n" +
                  "    a.kind,                                             	 \n" +
                  "    a.userid,                                           	 \n" +
                  "    b.name,												 \n" +
                  "    a.show_yn                                             \n" +
                
                  "FROM                                                      \n" +
                  "    TZ_COMPCLASS                         a,               \n" +
                  "    TZ_MEMBER                            b                \n" +
                  "WHERE                                                     \n" +
                  "        a.userid    = b.userid( +)                        \n" +
                  "    AND a.comp      = " + SQLString.Format(v_comp) + "    \n" +
                  "    AND a.isdeleted = 'N'                                 \n" ;
            
			ls = connMgr.executeQuery(sql);

			ls.next();
			
			dbox = ls.getDataBox();

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
    ��¾�ü(�迭��) �� ��0����� ���
    @param box		receive from the form object and session
    @return int		d���Ͽ���(1 : d��, 0 : �7�)
    */
	public int insertoutcomp(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		PreparedStatement   pstmt   = null; 
		PreparedStatement pstmt1 = null; 
		String              sql     = "";
		String sql1 = "";
		String sql2 = "";
		int isOk = 1;
		int isOk1 = 1;
		int isOk2 = 1;


		String v_groups = "01";
        String s_userid = box.getSession("userid");
        
		String v_process 		= box.getString("p_process");
        String v_compgubun 		= box.getString("p_compgubun");
         
        //int v_aes_code 			= box.getInt("p_aes_code");
        String v_comp 			= box.getString("p_comp");
		String v_compnm 		= box.getString("p_compnm");
		String v_coname 		= box.getString("p_coname");
		String v_compbirth_date1 	= box.getString("p_compbirth_date1");
		String v_compbirth_date2 	= box.getString("p_compbirth_date2");
		String v_compbirth_date3 	= box.getString("p_compbirth_date3");
        String v_compbirth_date 		= ""; 
        if("".equals(v_compbirth_date1) && "".equals(v_compbirth_date2) && "".equals(v_compbirth_date3)){
        	v_compbirth_date = v_compbirth_date1 + '-' + v_compbirth_date2 + '-' + v_compbirth_date3;
        	
        }
        String v_mail 			= box.getString("p_mail");
        String v_kind 			= box.getString("p_kind");

		String v_telno1 		= box.getString("p_telno1");
		String v_telno2 		= box.getString("p_telno2");
		String v_telno3 		= box.getString("p_telno3");
        String v_telno 			= ""; 
        
        if("".equals(v_telno1) && "".equals(v_telno2) && "".equals(v_telno3)){
        	v_telno = v_telno1 + '-' + v_telno2 + '-' + v_telno3;        	
        }

		String v_faxno1 		= box.getString("p_faxno1");
		String v_faxno2 		= box.getString("p_faxno2");
		String v_faxno3 		= box.getString("p_faxno3");
        String v_faxno 			= "";
        if("".equals(v_faxno1) && "".equals(v_faxno2) && "".equals(v_faxno3)){
        	v_faxno = v_faxno1 + '-' + v_faxno2 + '-' + v_faxno3;
        }
		String v_addr 			= box.getString("p_addr");

		String v_zip1 			= box.getString("p_post1");
		String v_zip2 			= box.getString("p_post2");
		String v_zip 			= "";
		if("".equals(v_zip1) && "".equals(v_zip2)) {
			v_zip = v_zip1 + '-' + v_zip2;
		}
		String v_gubun 			= box.getString("p_gubun");

        String v_userid 		= box.getString("p_userid");
        
        // 2008.09.30 �߰�
        String v_orga_sort 		= box.getString("p_orga_sort");		//��
        String v_show_yn 		= box.getString("p_show_yn");		//���⿩��
        
        String v_ploginyn		= box.getString("p_ploginyn");		//��Ż�α��ΰ��ɿ���

		try { 
			connMgr = new DBConnectionManager();          
			connMgr.setAutoCommit(false);
			
			// ----------------------ȸ���ڵ带 ���� ----------------------------
			
			/*sql = "select nvl(max(substr(comp,3,2)),'0') as comp from \"TZ_COMP\" ";
            
			ls = connMgr.executeQuery(sql);
            ls.next();
*/
// 			int v_comp_int = Integer.parseInt( ls.getString("comp") );  // ȸ���ڵ带 int��8�� ��ȯ	
//          v_comp_int = v_comp_int + 1; // ��ȯ�� ȸ���ڵ忡 + 1          
// 			v_comp = "" + v_comp_int;  // ȸ���ڵ带 �ٽ� String��8�� ��ȯ

			//v_comp = v_groups + SerialOneAdd.oneAddKey( ls.getString("comp")) + "000000";
//            v_comp = CodeConfigBean.addZero(v_comp, 6);  // '0'; �ٿ��ִ� �޼ҵ� (6�ڸ�)

			//ls.close();
            // ------------------------------------------------------------------------------------
            
			// TZ_COMP
			/*sql1 = "insert into \"TZ_COMP\" (comp, comptype, groups, company, companynm, compnm, isused, luserid, ldate, aes_code, orga_sort, show_yn)";  
			sql1 += " values (?, '2', ?, ?, ?, ?, 'Y', ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?)" ;

			pstmt = connMgr.prepareStatement(sql1);
			pstmt.setString(1, v_comp				);
			pstmt.setString(2, v_groups				);
			pstmt.setString(3, v_comp.substring(2,3));
			pstmt.setString(4, v_compnm				);
			pstmt.setString(5, v_compnm				);
			pstmt.setString(6, s_userid				);
			pstmt.setInt   (7, v_aes_code			);
			pstmt.setString(8, v_orga_sort			);
			pstmt.setString(9, v_show_yn			);

			isOk = pstmt.executeUpdate();   */

			// TZ_COMPCLASS    
			sql2 = "insert into \"TZ_COMPCLASS\" (comp, userid, compbirth_date, compnm, coname, telno, faxno, addr, zip, luserid, ldate, gubun, compgubun, email, kind , show_yn, orga_sort, comptype, ispotalloginyn ) ";  
			sql2 += " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, '2',?)" ;

			pstmt1 = connMgr.prepareStatement(sql2);
			
			pstmt1.setString(1, v_comp			);
			pstmt1.setString(2, v_userid		);
			pstmt1.setString(3, v_compbirth_date		);
			pstmt1.setString(4, v_compnm		);
			pstmt1.setString(5, v_coname		);
			pstmt1.setString(6, v_telno			);
			pstmt1.setString(7, v_faxno			);
			pstmt1.setString(8, v_addr			);
			pstmt1.setString(9, v_zip			);
			pstmt1.setString(10, s_userid		);
            pstmt1.setString(11, v_gubun		);
            pstmt1.setString(12, v_compgubun	);
            pstmt1.setString(13, v_mail			);
            pstmt1.setString(14, v_kind			);
            pstmt1.setString(15, v_show_yn		);
            pstmt1.setString(16, v_orga_sort	);
            pstmt1.setString(17, v_ploginyn		);
            

			isOk1 = pstmt1.executeUpdate();   
			
			//2008. 09. 30 ��0����� tz_compclass�� ��ħ, ��ο� ��� ����ϴ°� �ƴϰ� ��ϵȻ��; �ҷ��� �Է�...
			//if ( !v_userid.equals("") ) {  // ��0����� d���� �����;��쿡 ����
			//	isOk2 = insertoutuser(connMgr, box, v_comp);	// ȸ���߰�
            //}

//			if ( isOk > 0 && isOk1 > 0 && isOk2 > 0) { 
// 2008.10.01 ������
			if ( isOk > 0 ) { 
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
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }            
			if ( ls != null      ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( pstmt != null   ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
			if ( pstmt1 != null  ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }            
		}
		return isOk*(isOk1);
	}


    /**
    ��0����� ���
    @param box		receive from the form object and session
    @return int		d���Ͽ���(1 : d��, 0 : �7�)
    */
	public int insertoutuser(RequestBox box,String v_comp) throws Exception { 
		DBConnectionManager connMgr     = null;
		int isOk = 1;

		try { 
			connMgr = new DBConnectionManager();          
			
            isOk = insertoutuser(connMgr,box, v_comp);
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		}
		finally { 
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }      
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }         
		}
		return isOk;
	}
	
	/**
    ��0����� ���
    @param box		receive from the form object and session
    @return int		d���Ͽ���(1 : d��, 0 : �7�)
    */
	public int insertoutuser(DBConnectionManager connMgr, RequestBox box,String v_comp) throws Exception { 
		PreparedStatement   pstmt   = null; 
		PreparedStatement pstmt1 = null; 
		PreparedStatement pstmt2 = null; 
		String              sql     = "";
		String sql1 = "";
		String sql2 = "";
		int isOk = 1;
		int isOk1 = 1;
		int isOk2 = 1;

        String s_userid = box.getSession("userid");        

		String v_gadmin = "K2";
		String v_userid = box.getString("p_userid");  // ��0����� ID
		String v_name = box.getString("p_name");
		String v_pwd = box.getString("p_pwd");
		String v_jikwinm = box.getString("p_jikwinm");
		String v_email = box.getString("p_email");
		// String v_handphone1 = box.getString("p_handphone1");
		// String v_handphone2 = box.getString("p_handphone2");
		// String v_handphone3 = box.getString("p_handphone3");
		// String v_handphone = v_handphone1 + '-' + v_handphone2 + '-' + v_handphone3;
		String v_handphone = box.getString("p_handphone");

		try { 
            
			if ( !v_userid.equals("") ) {  // ��0����� d���� �����;��쿡 ����

				// TZ_MEMBER INSERT
				sql = "insert into \"TZ_MEMBER\" (comp, userid, name, pwd, jikwinm, email, handphone, indate, office_gbn) ";  
				sql += " values (?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), 'Y')" ;

				pstmt = connMgr.prepareStatement(sql);
				
				pstmt.setString(1, v_comp);
				pstmt.setString(2, v_userid);
				pstmt.setString(3, v_name);
				pstmt.setString(4, v_pwd);
				pstmt.setString(5, v_jikwinm);
				pstmt.setString(6, v_email);
				pstmt.setString(7, v_handphone);

				isOk = pstmt.executeUpdate();

				// TZ_MANAGER INSERT
				sql1 = "insert into \"TZ_MANAGER\" (userid, gadmin, comp, fmon, tmon, luserid, ldate) ";  
				sql1 += " values (?, ?, ?, to_char(sysdate, 'YYYYMMDD'), '99991231', ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";

				pstmt1 = connMgr.prepareStatement(sql1);
				
				pstmt1.setString(1, v_userid);
				pstmt1.setString(2, v_gadmin);
				pstmt1.setString(3, v_comp);
				pstmt1.setString(4, s_userid);

				isOk1 = pstmt1.executeUpdate();

				// TZ_COMPMAN INSERT
				sql2 = "insert into \"TZ_COMPMAN\" (userid, gadmin, comp, luserid, ldate) ";  
				sql2 += " values (?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";

				pstmt2 = connMgr.prepareStatement(sql2);
				
				pstmt2.setString(1, v_userid);
				pstmt2.setString(2, v_gadmin);
				pstmt2.setString(3, v_comp);
				pstmt2.setString(4, s_userid);

				isOk2 = pstmt2.executeUpdate();

            }

		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
			if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
			if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
		}
		return isOk*isOk1*isOk2;
	}
	


    /**
    �迭��(��¾�ü) ��d
    @param	box		receive from the form object and session
    @return	int		d��ó������(1 : d��, 2 : �7�)
    */
	public int updateoutcomp(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement pstmt1 = null;  
		PreparedStatement pstmt2 = null; 
		PreparedStatement pstmt3 = null;
		String              sql     = "";
		String sql1 = "";
		String sql2 = "";
		String sql3 = "";
		
		int isOk1 = 1;
		int isOk2 = 1;
		int isOk3 = 1;
		ListSet             ls      = null;

		String v_process = box.getString("p_process");

		String v_comp = box.getString("p_comp");
		int v_aes_code = box.getInt("p_aes_code");
        String v_compgubun = box.getString("p_compgubun");
		String v_coname = box.getString("p_coname");
		String v_compnm = box.getString("p_compnm");
		
		String v_compbirth_date1 = box.getString("p_compbirth_date1");
		String v_compbirth_date2 = box.getString("p_compbirth_date2");
		String v_compbirth_date3 = box.getString("p_compbirth_date3");
        String v_compbirth_date = v_compbirth_date1 + '-' + v_compbirth_date2 + '-' + v_compbirth_date3;

		String v_telno1 = box.getString("p_telno1");
		String v_telno2 = box.getString("p_telno2");
		String v_telno3 = box.getString("p_telno3");
        String v_telno = v_telno1 + '-' + v_telno2 + '-' + v_telno3;

		String v_faxno1 = box.getString("p_faxno1");
		String v_faxno2 = box.getString("p_faxno2");
		String v_faxno3 = box.getString("p_faxno3");
        String v_faxno = v_faxno1 + '-' + v_faxno2 + '-' + v_faxno3;

		String v_addr = box.getString("p_addr");

		String v_zip1 = box.getString("p_post1");
		String v_zip2 = box.getString("p_post2"); 
		String v_zip = v_zip1 + '-' + v_zip2;

		String v_gubun = box.getString("p_gubun");
		String v_ploginyn = box.getString("p_ploginyn");

		String v_userid = box.getString("p_userid");  // ��0����� ID
		//String v_name = box.getString("p_name");
		//String v_pwd = box.getString("p_pwd");
		//String v_jikwinm = box.getString("p_jikwinm");
		//String v_email = box.getString("p_email");
		// String v_handphone1 = box.getString("p_handphone1");
		// String v_handphone2 = box.getString("p_handphone2");
		// String v_handphone3 = box.getString("p_handphone3");
		// String v_handphone = v_handphone1 + '-' + v_handphone2 + '-' + v_handphone3;  
		//String v_handphone = box.getString("p_handphone");
		
		String s_userid = box.getSession("userid");   // ����
		
		String v_show_yn = box.getString("p_show_yn");
		
		try { 
			connMgr = new DBConnectionManager();     
			connMgr.setAutoCommit(false);
			
				sql1 = "update \"TZ_COMPCLASS\" set 					 \n";
				sql1 += " userid = ?,                  					 \n";
				sql1 += " compnm = ?,                  					 \n";
				sql1 += " compbirth_date = ?,               					 \n";
				sql1 += " coname = ?,                 					 \n";
				sql1 += " telno = ?,                  					 \n";
				sql1 += " faxno = ?,                  					 \n";
				sql1 += " addr = ?,                   					 \n";
				sql1 += " zip = ?,                    					 \n";
				sql1 += " gubun = ?,                   					 \n";
				sql1 += " luserid = ?,                 					 \n";
				sql1 += " ldate = to_char(sysdate, 'YYYYMMDDHH24MISS'),  \n";
				sql1 += " ispotalloginyn = ?,							 \n";
				sql1 += " show_yn = ?							         \n";
				
				
				sql1 += " where comp = " + SQLString.Format(v_comp);                   
				pstmt1 = connMgr.prepareStatement(sql1);
				
				pstmt1.setString(1, v_userid);
				pstmt1.setString(2, v_compnm);
				pstmt1.setString(3, v_compbirth_date);
				pstmt1.setString(4, v_coname);
				pstmt1.setString(5, v_telno);
				pstmt1.setString(6, v_faxno);
				pstmt1.setString(7, v_addr);
				pstmt1.setString(8, v_zip);
				pstmt1.setString(9, v_gubun);
				pstmt1.setString(10, s_userid);
				pstmt1.setString(11, v_ploginyn);
				pstmt1.setString(12, v_show_yn);
                //pstmt1.setString(11, v_intro);
                //pstmt1.setString(12, v_homepage);
                
				isOk1 = pstmt1.executeUpdate();	          

/*			if ( !v_userid.equals("") ) {  // ��0����� d���� ��d��;��쿡 ����

				int usercnt = usercheck(box);

				if ( usercnt == 0) { // \"TZ_MEMBER\" ���̺? x���� ��� - Update
*//* 2006.06.20 ��d
					sql2 = "update \"TZ_MEMBER\" set name = ?, pwd = ?, jikwinm = ?, email = ?, handphone = ? ";
                    sql2 += " where userid = "  + SQLString.Format(v_userid);                              
*/
/*                    sql2 = "UPDATE                                                                   \n" +
                           "    TZ_MEMBER                                                            \n" +
                           "SET                                                                      \n" +
                           "    name = ?,                                                            \n" +
                           "    pwd = ?,                                                             \n" +
                           "    email = ?,                                                           \n" +
                           "    handphone = ?                                                        \n" +
					       "WHERE                                                                    \n" +
                           "    userid = "  + SQLString.Format(v_userid) + "                         \n" ;                              

					pstmt2 = connMgr.prepareStatement(sql2);
					
					pstmt2.setString(1, v_name);
					pstmt2.setString(2, v_pwd);*/
/* 2006.06.20 ��d
					pstmt2.setString(3, v_jikwinm);
					pstmt2.setString(4, v_email);
					pstmt2.setString(5, v_handphone);
*/
/*                    pstmt2.setString(3, v_email);
                    pstmt2.setString(4, v_handphone);
                    
					isOk2 = pstmt2.executeUpdate();
				} else {   // \"TZ_MEMBER\" ���̺? x�������; ��� - Insert

					isOk2 = insertoutuser(box, v_comp);	// ȸ���߰�

				}	
			}*/
			
			/*sql3 = "update TZ_COMP set aes_code = ?";
			sql3 += " where comp = " + SQLString.Format(v_comp);                   
			
			pstmt3 = connMgr.prepareStatement(sql3);
			pstmt3.setInt(1, v_aes_code);
			isOk3 = pstmt3.executeUpdate();*/
		
			//if ( isOk1 > 0 && isOk2 > 0 && isOk3 > 0) { 
			if ( isOk1 > 0 ) { 
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
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }            
			if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
			if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
			if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk1*isOk2*isOk3;
	}	


    /**
    �迭��(��¾�ü) ��f
    @param	box		receive from the form object and session
    @return	int		d��ó������(1 : d��, 2 : �7�)
    */
	public int deleteoutcomp(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		String sql1 = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		int isOk1 = 1;
		int isOk2 = 1;
		int isOk3 = 1;
		int isOk4 = 1;
		int isOk5 = 1;
		
		String s_userid = box.getSession("userid");		

        String v_process = box.getString("p_process");
		String v_comp = box.getString("p_comp");
		String v_userid = box.getString("p_userid");
		
		try { 
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			// ȸ��d�� ��f
			// sql1 = "delete from \"TZ_COMPCLASS\" where comp = " + SQLString.Format(v_comp);
			sql1 = "update \"TZ_COMPCLASS\" set isdeleted = 'Y' where comp = " + SQLString.Format(v_comp);
			isOk1 = connMgr.executeUpdate(sql1);

            // ȸ��d�� ��f
			// sql1 = "delete from \"TZ_COMP\" where comp = " + SQLString.Format(v_comp);
			//sql2 = "update \"TZ_COMP\" set isused = 'N' where comp = " + SQLString.Format(v_comp);
			//isOk2 = connMgr.executeUpdate(sql2);

            
			if ( !v_userid.equals("") ) { 
				// ��0����� ��f
				//sql3 = "delete from \"TZ_MEMBER\" where userid = " + SQLString.Format(v_userid);
				//isOk3 = connMgr.executeUpdate(sql3);
				
				// ��ڱ��� ��f
				sql4 = "update \"TZ_MANAGER\" set isdeleted = 'Y' where userid = " + SQLString.Format(v_userid) + " and gadmin ='K2' and comp = " + SQLString.Format(v_comp);
				isOk4 = connMgr.executeUpdate(sql4);				
				
				// ��ڱ��� ��f
				sql5 = "delete from \"TZ_COMPMAN\" where userid = " + SQLString.Format(v_userid) + " and gadmin ='K2' and comp = " + SQLString.Format(v_comp);
				isOk5 = connMgr.executeUpdate(sql5);				
			} else { 
			  isOk3 =1;
			  isOk4 =1;
			  isOk5 =1;
			}
			
			if ( isOk1 > 0 && isOk2 > 0 && isOk3 > 0 && isOk4 > 0  && isOk5 > 0) { 
				connMgr.commit();
			} else { 
			    connMgr.rollback();
			}
			System.out.println("isOk1 ==  ==  ==  ==  == >>  > " +isOk1);
			System.out.println("isOk2 ==  ==  ==  ==  == >>  > " +isOk2);

		} catch ( Exception ex ) { 
			if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }  
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
		    }
		finally { 
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }            
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk1*isOk2;
	}	    
	


    /**
    �迭�� �ΰ� �̹��� ����
    @param box          receive from the form object and session
    @return String	�̹��� url
    */
	public static String getCompLogo(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		String complogo = "";
// 		String v_userid = "test00";
		
		try { 
			connMgr = new DBConnectionManager();
            complogo = getCompLogo(connMgr, box);
		}

		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception(ex.getMessage() );
		}
		finally { 
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return complogo;
	}   
	
	/**
    �迭�� �ΰ� �̹��� ����
    @param box          receive from the form object and session
    @return String	�̹��� url
    */
	public static String getCompLogo(DBConnectionManager connMgr, RequestBox box) throws Exception { 
		ListSet             ls      = null;
		String              sql     = "";
		String complogo = "";
		String v_dir = "";
		ConfigSet conf = new 	ConfigSet();
	
		String v_userid = box.getSession("userid");
// 		String v_userid = "test00";
		
		try { 
			
			sql = "select complogo ";
			sql += "  from \"TZ_COMPCLASS\" a, \"TZ_MEMBER\" b ";
			sql += " where a.comp = b.comp ";
			sql += "  and b.userid = " + SQLString.Format(v_userid) ;
			
			ls = connMgr.executeQuery(sql);
			v_dir = conf.getProperty("dir.outuseradminpath");
			if ( ls.next() )	complogo = v_dir + "" + ls.getString("complogo");

		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
		}
		return complogo;
	}   


	
    /**
    ȸ��� �޺��ڽ�
    @param box			receive from the form object and session
    @param isChange		���ú���� ������ε� ����(true,false)
    @param isALL		ALL ǥ�ÿ���(true,false)
    @return String		ȸ���޺��ڽ�html��
    */
	public static String getComp(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;        
		ListSet             ls      = null;
		String              sql     = "";
		String result = "";
		String v_comp = "";
		
		try { 
			connMgr = new DBConnectionManager();
						
			sql = "select comp, compnm ";
			sql += " from \"TZ_COMPCLASS\" ";
			sql += " where isdeleted = 'N' ";
			sql += " order by compnm ";
			// sql += " where substr(comp,1,4) = " + SQLString.Format(v_comp) ;
			
			pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			ls = new ListSet(pstmt);

			result += getSelectTag( ls, isChange, false, "p_comp");
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, true);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }    
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return result;
	}  
	
	

    
    /**
    �����ID �ߺ�üũ
    @param	p_userid	�����id	
    @return	int			d��ó������(1:d��,2:�7�)
    */
	public int usercheck(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;     
        String              sql     = "";
        int isOk = 1;
		ListSet             ls      = null;

		String v_userid = box.getString("p_userid");
        
        try { 
            connMgr = new DBConnectionManager();
            
			if ( !v_userid.equals("") ) { 

				sql = "select count(*) as cnt from \"TZ_MEMBER\" where userid = " + SQLString.Format(v_userid);
					ls = connMgr.executeQuery(sql);
					ls.next();

					if ( ls.getInt("cnt") > 0) { // \"TZ_MEMBER\" ���̺? x���� ��� - �ߺ�
						isOk = 0;
					} else {         
						isOk = 1;
					}
					
					ls.close();
			}
        } catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }	


    /**
    ȸ�籸�� �ߺ�üũ
    @param	p_userid	ȸ�籸��	
    @return	int			d��ó������(1:d��,2:�7�)
    */
	public int compgubuncheck(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;     
        String              sql     = "";
        int isOk = 0;
		ListSet             ls      = null;

		String v_comp = box.getString("p_comp");
        
        try { 
            connMgr = new DBConnectionManager();
            
			sql = "select count(*) as cnt from TZ_COMPCLASS where comp = " + SQLString.Format(v_comp);
				ls = connMgr.executeQuery(sql);
				ls.next();

				if ( ls.getInt("cnt") > 0) { // \"TZ_COMPCLASS\" ���̺? x���� ��� - �ߺ�
					isOk = 0;
				} else {         
					isOk = 1;
				}
                
				ls.close();
        } catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
	
	 /**
    ȸ�籸�� �ߺ�üũ
    @param	p_userid	ȸ�籸��	
    @return	int			d��ó������(1:d��,2:�7�)
    */
	public DataBox compgubuncheck_dbox(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox             dbox    = null;
        Connection conn = null;     
        String              sql     = "";
        int isOk = 0;
		ListSet             ls      = null;

		int v_aes_code = box.getInt("p_aes_code");
        
        try { 
            connMgr = new DBConnectionManager();
            
			    sql = "select count(*) as cnt from TZ_COMP where aes_code = " + SQLString.Format(v_aes_code);
				ls = connMgr.executeQuery(sql);
                while ( ls.next() ) { 
                    dbox = ls.getDataBox();
                }
				ls.close();
        } catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }	
    
    /**
    ������� x���ϴ��� üũ
    @param	p_parentcono	�������
    @return	int				x�翩��(1:d��,2:�7�)
    */
	public int empcheck(String p_parentcono) throws Exception { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;     
        String              sql     = "";
        int isOk = 0;
		ListSet             ls      = null;
        
        try { 
            connMgr = new DBConnectionManager();
            
			sql = "select count(*) as cnt from \"TZ_MEMBER\" where parentcono = " + SQLString.Format(p_parentcono);
				ls = connMgr.executeQuery(sql);
				ls.next();

				if ( ls.getInt("cnt") > 0) { // \"TZ_MEMBER\" ���̺? x���� ��� - d��
					isOk = 0;
				} else {         // \"TZ_MEMBER\" ���̺? x�������; ��� - ����
					isOk = 1;
				}
                
				ls.close();
        } catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }	
    

    /**
    �ɼǸ�����ִ� �޼ҵ�
    @param ls			�޺��ڽ� ������ ����Ʈ��
    @param isChange		���ú���� ������ε� ����(true,false)
    @param isALL		ALL ǥ�ÿ���(true,false)
    @param selname		�޺��ڽ���
    @return String		�޺��ڽ�html��
    */
	public static String getSelectTag(ListSet ls, boolean isChange, boolean isALL, String selname) throws Exception { 
		StringBuffer sb = null;
		
		try { 
			sb = new StringBuffer();  
			
			sb.append("<select name = \"" + selname + "\"");
			if ( isChange) sb.append(" onChange = \"whenSelection('change')\"");  
			sb.append(" > \r\n");
			if ( isALL) { 
				sb.append("<option value = \"ALL\" > ALL</option > \r\n");  
			}
			else if ( isChange) { 
				if ( selname.indexOf("year") == -1) 
					sb.append("<option value = \"----\" > ----</option > \r\n");  
			}
			
			while ( ls.next() ) {    
				ResultSetMetaData meta = ls.getMetaData();
				int columnCount = meta.getColumnCount();

				sb.append("<option value = \"" + ls.getString(1) + "\"");
				
				// if ( optionselected.equals( ls.getString(1))) sb.append(" selected");
				
				sb.append(" > " + ls.getString(columnCount) + "</option > \r\n");
			}
            
			sb.append("</select > \r\n");
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, true);
			throw new Exception(ex.getMessage() );
		}
		return sb.toString();
	}




    /**
    �ɼǸ�����ִ� �޼ҵ�
    @param box			receive from the form object and session
    @param ls			�޺��ڽ� ������ ����Ʈ��
    @param isChange		���ú���� ������ε� ����(true,false)
    @param isALL		ALL ǥ�ÿ���(true,false)
    @param selname		�޺��ڽ���
    @param p_scnt		ǥ�ñ���
    @return String		�޺��ڽ�html��
    */
	public static String getSelectTag2(RequestBox box, ListSet ls, boolean isChange, boolean isALL, String selname, String p_scnt) throws Exception { 
		StringBuffer sb = null;
		int cnt = 0;
		try { 
			sb = new StringBuffer();  
	
			if ( selname == "0") box.put("p_scnt","0");			
			
			while ( ls.next() ) {    
				ResultSetMetaData meta = ls.getMetaData();
				int columnCount = meta.getColumnCount();
				
				if ( cnt == 0 && p_scnt != "0" && selname != "0")	sb.append(",\r\n");
				
				if ( selname == "0") box.put("p_scnt","1");
				
				if ( cnt > 0) sb.append(",\r\n");
				
				sb.append("new Style('" + ls.getString(1) + "', " + selname + ", \"" + ls.getString(2) + "\")");
				
				cnt++;

			}
			// System.out.println("ls.getRowNum() : " + ls.getRowNum() );
			// if ( ls.getRowNum() > 1) { 
				// ����ڵ��� �Ҽ�8�� vȸ�� �迭�� �� ��»簡 ��;���
            	// if ( selname.equals("0")) sb.append(",\r\n");
        	// }
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, true);
			throw new Exception(ex.getMessage() );
		}

		return sb.toString();
	}



    /**
    ����̿��� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList	����̿��� ����Ʈ
    */
	public ArrayList selectoutuserlist(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		ArrayList           list    = null;
		String              sql     = "";
		DataBox             dbox    = null;
        
        String v_searchtext = box.getString("p_searchtext");
		String v_select = box.getString("p_select");
		int v_pageno = box.getInt("p_pageno");
        
		String s_userid = box.getSession("userid");

		String v_compgubun = box.getString("p_compgubun");
		String v_comp = box.getString("p_comp");
		String v_usergubun = box.getString("p_usergubun");
        String v_userid = box.getString("p_userid");
		String v_name = box.getString("p_name");

		if ( v_compgubun.equals("�����ϼ���") ) { 
		   v_compgubun = "%";
		}
		else if ( v_usergubun.equals("D") ) { 
			// �Ǹ�a(��)���
			if ( v_compgubun.equals("0") ) { 
			   v_compgubun = "0202"; // ���
			} else if ( v_compgubun.equals("1") ) { 
			   v_compgubun = "0201"; // ���
			}
		}
		else{ 
			if ( v_compgubun.equals("0") ) { 
			   v_compgubun = "0101"; // ���
			} else if ( v_compgubun.equals("1") ) { 
			   v_compgubun = "0102"; // ���
			}
		}

        if ( v_comp.equals("�����ϼ���") ) { 
		   v_comp = "%";
		} else if ( v_comp.equals("") ) { 
           v_comp = "%";
		}

        if ( v_userid.equals("") ) { 
		   v_userid = "%";
		}
        
        if ( v_name.equals("") ) { 
		   v_name = "";
		}


		try { 
			connMgr = new DBConnectionManager();
			
			list = new ArrayList();			
			
			sql = " select a.userid, a.usergubun, a.pwd, a.name, a.birth_date, a.comp, b.compnm, a.orga_ename, a.jikwinm, a.email, b.gubun ";
			sql += " from \"TZ_MEMBER\" a, \"TZ_COMPCLASS\" b ";
			sql += " where a.comp = b.comp( +) ";
			if ( !v_usergubun.equals("P") && !v_usergubun.equals("") ) { 
				// ��w�̳� �Ǹ�a(��) vȸ���(ȸ��v��; �ش�.)
			sql += " and substr(a.comp,1,4) = " + SQLString.Format(v_compgubun);
			sql += " and nvl(a.usergubun,'') = " + SQLString.Format(v_usergubun);
			}
			else if ( v_usergubun.equals("P") ) { 
				// �迭�� vȸ���
			sql += " and nvl(a.comp,'') like " + SQLString.Format("%" + v_comp + "%");
			sql += " and nvl(a.usergubun,'') = " + SQLString.Format(v_usergubun);
			}
			else{ 
			sql += " and substr(a.comp,1,4) = " + SQLString.Format(v_compgubun);
			}
			sql += " and nvl(a.userid,'') like " + SQLString.Format(v_userid + "%");
			sql += " and nvl(a.name,'') like " + SQLString.Format("%" +v_name + "%"); 
			sql += " and a.usergubun = ANY('F','D','P') and b.isdeleted( +) = 'N' ";

			// System.out.println("v_usergubun : " + v_usergubun);
			// System.out.println("row : " + row);
			// System.out.println("sql : " + sql);

			ls = connMgr.executeQuery(sql); 
			
            ls.setPageSize(row);             //  ������� row ���� �����Ѵ�
            ls.setCurrentPage(v_pageno);                    //     �����������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();       //     ��ü ������ �� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();    //     ��ü row �� ��ȯ�Ѵ�
            
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
	

    /**
    ����̿��� ��d������
    @param box          receive from the form object and session
    @return DataBox		����̿��� d��
    */
	public DataBox select2outuser(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		DataBox             dbox    = null;
		ListSet             ls      = null;
		String              sql     = "";
		
		String v_userid = box.getString("p_user");
		
		try { 
			connMgr = new DBConnectionManager();
			
			sql = "select substr(a.comp,1,4) as compgubun, a.usergubun, a.comp, b.compnm, a.userid, a.pwd, a.name, a.birth_date, " ;
			sql += " a.parentcono, a.orga_ename, a.jikwinm, a.addr, a.email, a.comptel, a.handphone, a.hometel ";
			sql += "from \"TZ_MEMBER\" a, \"TZ_COMPCLASS\" b ";
			sql += "where a.comp = b.comp( +) and a.userid = " + SQLString.Format(v_userid) ;
			
			ls = connMgr.executeQuery(sql);
			
			ls.next();
			
			dbox = ls.getDataBox();

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
    ����̿��� ���
    @param box		receive from the form object and session
    @return int		d���Ͽ���(1 : d��, 0 : �7�)
    */
	public int insertoutuser(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null; 
		String              sql     = "";
		int isOk = 1;
				
        String s_userid = box.getSession("userid");
        
		String v_process = box.getString("p_process");

        String v_compgubun = box.getString("p_compgubun");
		String v_usergubun = box.getString("p_usergubun");
		String v_comp = box.getString("p_comp");

		String v_userid = box.getString("p_userid");
		String v_name = box.getString("p_name");
		String v_pwd = box.getString("p_pwd"); 

		String v_birth_date1 = box.getString("p_birth_date1"); 
		String v_birth_date2 = box.getString("p_birth_date2"); 
		String v_birth_date = v_birth_date1 + v_birth_date2 ;

        String v_parentcono = box.getString("p_parentcono");
		String v_orga_ename = box.getString("p_orga_ename");
		String v_jikwinm = box.getString("p_jikwinm");
		String v_addr = box.getString("p_addr");
		String v_email = box.getString("p_email");

		String v_comptel1 = box.getString("p_comptel1");
		String v_comptel2 = box.getString("p_comptel2");
		String v_comptel3 = box.getString("p_comptel3");
        String v_comptel = v_comptel1 + '-' + v_comptel2 + '-' + v_comptel3;

		String v_handphone1 = box.getString("p_handphone1");
		String v_handphone2 = box.getString("p_handphone2");
		String v_handphone3 = box.getString("p_handphone3");
		String v_handphone = v_handphone1 + '-' + v_handphone2 + '-' + v_handphone3;

		String v_hometel1 = box.getString("p_hometel1");
		String v_hometel2 = box.getString("p_hometel2");
		String v_hometel3 = box.getString("p_hometel3");
        String v_hometel = v_hometel1 + '-' + v_hometel2 + '-' + v_hometel3;

		try { 
			connMgr = new DBConnectionManager();          
			connMgr.setAutoCommit(false);
			
			// ----------------------ȸ���ڵ带 ���� ----------------------------
			if ( !v_usergubun.equals("P") ) {  // ��w/�Ǹ�a(��)�� ���
				if ( v_usergubun.equals("D") ) { 
					// �Ǹ�a(��)���
					if ( v_compgubun.equals("0") ) { 
					   v_comp = "0202000000"; // ���
					} else if ( v_compgubun.equals("1") ) { 
					   v_comp = "0201000000"; // ���
					}
				}
				else{ 
					if ( v_compgubun.equals("0") ) { 
					   v_comp = "0101000000"; // ���
					} else if ( v_compgubun.equals("1") ) { 
					   v_comp = "0102000000"; // ���
					}
				}
            }

			sql = "insert into \"TZ_MEMBER\" (usergubun, comp, userid, pwd, name, birth_date, parentcono, orga_ename, " ;
			sql += "jikwinm, addr, email, comptel, handphone, hometel, indate ) ";  
			sql += " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))" ;

			pstmt = connMgr.prepareStatement(sql);
			
			pstmt.setString(1, v_usergubun);
			pstmt.setString(2, v_comp);
			pstmt.setString(3, v_userid);
			pstmt.setString(4, v_pwd);
			pstmt.setString(5, v_name);
			pstmt.setString(6, v_birth_date);
			pstmt.setString(7, v_parentcono);
			pstmt.setString(8, v_orga_ename);
			pstmt.setString(9, v_jikwinm);
			pstmt.setString(10, v_addr);
			pstmt.setString(11, v_email);
            pstmt.setString(12, v_comptel);
			pstmt.setString(13, v_handphone);
			pstmt.setString(14, v_hometel);

			isOk = pstmt.executeUpdate();   

			if ( isOk > 0) { 
				if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
			} else { 
				if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
			}
		}
		catch ( Exception ex ) { 
			if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }                     
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }            
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }            
		}
		return isOk;
	}

    /**
    ����̿��� ��d
    @param	box		receive from the form object and session
    @return	int		d��ó������(1 : d��, 2 : �7�)
    */
	public int updateoutuser(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;  
		String              sql     = "";
		int isOk1 = 1;
		ListSet             ls      = null;

		String v_process = box.getString("p_process");
		
		String v_userid = box.getString("p_userid");  
		String v_name = box.getString("p_name");
		String v_pwd = box.getString("p_pwd");

		String v_birth_date1 = box.getString("p_birth_date1");
		String v_birth_date2 = box.getString("p_birth_date2");
        String v_birth_date = v_birth_date1 + v_birth_date2;  
        
		String v_parentcono = box.getString("p_parentcono");
		String v_orga_ename = box.getString("p_orga_ename");
		String v_jikwinm = box.getString("p_jikwinm");
		String v_addr = box.getString("p_addr");
		String v_email = box.getString("p_email");
		
		String v_comptel1 = box.getString("p_comptel1");
		String v_comptel2 = box.getString("p_comptel2");
		String v_comptel3 = box.getString("p_comptel3");
        String v_comptel = v_comptel1 + '-' + v_comptel2 + '-' + v_comptel3;

		String v_handphone1 = box.getString("p_handphone1");
		String v_handphone2 = box.getString("p_handphone2");
		String v_handphone3 = box.getString("p_handphone3");
		String v_handphone = v_handphone1 + '-' + v_handphone2 + '-' + v_handphone3;  		

		String v_hometel1 = box.getString("p_hometel1");
		String v_hometel2 = box.getString("p_hometel2");
		String v_hometel3 = box.getString("p_hometel3");
        String v_hometel = v_hometel1 + '-' + v_hometel2 + '-' + v_hometel3;
		
		String s_userid = box.getSession("userid");   // ����

		try { 
			connMgr = new DBConnectionManager();     
			connMgr.setAutoCommit(false);
			
				sql = "update \"TZ_MEMBER\" set pwd = ?, name = ?, email=?, birth_date = ?, parentcono = ?, orga_ename = ?, jikwinm = ?, " ;
				sql += "addr = ?, comptel = ?, handphone = ?, hometel = ? ";
				sql += " where userid = " + SQLString.Format(v_userid);                   
				
				pstmt = connMgr.prepareStatement(sql);
				
				pstmt.setString(1, v_pwd);
				pstmt.setString(2, v_name);
				pstmt.setString(3, v_email);
				pstmt.setString(4, v_birth_date);
				pstmt.setString(5, v_parentcono);
				pstmt.setString(6, v_orga_ename);
				pstmt.setString(7, v_jikwinm);
				pstmt.setString(8, v_addr);
				pstmt.setString(9, v_comptel);
				pstmt.setString(10, v_handphone);
				pstmt.setString(11, v_hometel);

				isOk1 = pstmt.executeUpdate();	          
		
			if ( isOk1 > 0) { 
				if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
			} else { 
				if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
			}  
		}
		catch ( Exception ex ) { 
			if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }  
			ErrorManager.getErrorStackTrace(ex, box, sql);            
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }            
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk1;
	}	


    /**
    ����̿��� ��f
    @param	box		receive from the form object and session
    @return	int		d��ó������(1 : d��, 2 : �7�)
    */
	public int deleteoutuser(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		String              sql     = "";
		int isOk1 = 1;
		
		String s_userid = box.getSession("userid");		

        String v_process = box.getString("p_process");
		String v_userid = box.getString("p_userid");
		
		try { 
			connMgr = new DBConnectionManager();

			sql = "delete from \"TZ_MEMBER\" where userid = " + SQLString.Format(v_userid);
			
			isOk1 = connMgr.executeUpdate(sql);

		} catch ( Exception ex ) { 
			if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }  
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		    }
		finally { 
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk1;
	}	    


    /**
    ����̿��� ��� PreparedStatement
    @param	connMgr				db���ᰳü
    @return	PreparedStatement	����̿��� ��� PreparedStatement
    */
	public PreparedStatement getPreparedStatement(DBConnectionManager connMgr) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";

				sql = " insert into \"TZ_MEMBER\" ";
                sql += "  (userid, pwd, name, birth_date, orga_ename, jikwinm, addr, email, comptel, handphone, hometel, parentcono, usergubun, comp) ";
                sql += " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        try { 
            pstmt = connMgr.prepareStatement(sql);
        } catch ( Exception ex ) { 
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
        }

        return pstmt;
    }

    /**
    ����̿��� ���
    @param	pstmt_insert_outuser	����̿��� PreparedStatement
    @param	data					����̿��� d�� data
    @param	p_usergubun				����̿���~��(��w,��,�迭��)
    @param	p_comp					ȸ���ڵ�
    @return	int						d��ó������(1:d��,2:�7�)
    */
    public int insert_outuser(PreparedStatement pstmt_insert_outuser, OutUserDataBean data, String p_usergubun, String p_comp)  throws Exception { 

        ResultSet rs = null;
        int isOk = 0;

		try { 

			if ( p_usergubun.equals("P") ) { 
				// �迭�� ����̶�� ��d; �ֹι�ȣ�� �־��ش�.
				// System.out.println("��»����");
			}

            pstmt_insert_outuser.setString( 1, data.getUserid() );
            pstmt_insert_outuser.setString( 2, data.getPwd() );
            pstmt_insert_outuser.setString( 3, data.getName() );
            pstmt_insert_outuser.setString( 4, data.getbirth_date() );
            pstmt_insert_outuser.setString( 5, data.getOrga_ename() );
            pstmt_insert_outuser.setString( 6, data.getJikwinm() );
            pstmt_insert_outuser.setString( 7, data.getAddr() );
            pstmt_insert_outuser.setString( 8, data.getEmail() );
            pstmt_insert_outuser.setString( 9, data.getComptel() );
            pstmt_insert_outuser.setString(10, data.getHandphone() );
            pstmt_insert_outuser.setString(11, data.getHometel() );
            pstmt_insert_outuser.setString(12, data.getParentcono() );
			pstmt_insert_outuser.setString(13, p_usergubun);
			pstmt_insert_outuser.setString(14, p_comp);

            isOk = pstmt_insert_outuser.executeUpdate();
            
        } catch ( Exception ex ) { 
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }



    /**
    ����� ����Ʈ
    @param box          receive from the form object and session
    @return ArrayList	����� ����Ʈ
    */
	public ArrayList selectuser(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ArrayList           list    = null;
		DataBox             dbox    = null;
		ListSet             ls      = null;
		String              sql     = "";
        
		String s_userid = box.getSession("userid");
		
		String v_compgubun = box.getString("p_compgubun");	
		String v_userid = box.getString("p_userid2");
		String v_name = box.getString("p_name2");
		
		try { 
			connMgr = new DBConnectionManager();

			list = new ArrayList();
			
			sql = "select userid, name, divinam, orga_ename, jikwinm ";
			sql += "from \"TZ_MEMBER\" ";
			sql += "where usergubun in ('H','K','RH','RK') ";
			sql += " and substr(nvl(comp,''),1,4) = " + SQLString.Format(v_compgubun);
			if ( v_userid.equals("") && v_name.equals("") ) { 
				// ����̳� �̸�8�� vȸ���� �ʾҴٸ� vȸ���� �ʰ��Ѵ�.
				sql += " and userid = 'zzzzzzzz' ";
			}
			else{ 
				if ( !v_userid.equals("") ) { 
					sql += " and nvl(userid,'') = " + SQLString.Format(v_userid);
				}
				if ( !v_name.equals("") ) { 
					sql += " and nvl(name,'') like " + SQLString.Format("%" +v_name + "%"); 
				}
			}
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
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return list;
	}  


    /**
    ����̿��� �����ٿ�ε�
    @param	box			receive from the form object and session
    @return	ArrayList	����̿��� ����Ʈ
    */
    public ArrayList outuserexceldownload(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
		// String v_searchtext = box.getString("p_searchtext");
		// String v_select = box.getString("p_select");
		// int v_pageno = box.getInt("p_pageno");

        String v_compgubun = box.getString("p_compgubun");
		String v_comp = box.getString("p_comp");
		String v_usergubun = box.getString("p_usergubun");
        String v_userid = box.getString("p_userid");
		String v_name = box.getString("p_name");
		
		if ( v_compgubun.equals("�����ϼ���") ) { 
		   v_compgubun = "%";
		}
		else if ( v_usergubun.equals("D") ) { 
			// �Ǹ�a(��)���
			if ( v_compgubun.equals("0") ) { 
			   v_compgubun = "0202"; // ���
			} else if ( v_compgubun.equals("1") ) { 
			   v_compgubun = "0201"; // ���
			}
		}
		else{ 
			if ( v_compgubun.equals("0") ) { 
			   v_compgubun = "0101"; // ���
			} else if ( v_compgubun.equals("1") ) { 
			   v_compgubun = "0102"; // ���
			}
		}

		// if ( v_usergubun.equals("0") ) { 
		//   v_usergubun = "%";
		// }

        if ( v_comp.equals("�����ϼ���") ) { 
		   v_comp = "%";
		} else if ( v_comp.equals("") ) { 
           v_comp = "%";
		}

        if ( v_userid.equals("") ) { 
		   v_userid = "%";
		}
        
        if ( v_name.equals("") ) { 
		   v_name = "%";
		}

        try { 
            connMgr = new DBConnectionManager();            
            
            list = new ArrayList();
            
            sql = " select a.usergubun, a.userid, a.pwd, a.name, a.birth_date, a.comp, b.compnm, a.orga_ename, a.jikwinm, a.email ";
            sql += " , a.addr, a.comptel, a.handphone, a.hometel, b.gubun, a.parentcono ";
			sql += " from tz_member a, tz_compclass b ";
			sql += " where a.comp = b.comp( +) ";
			if ( !v_usergubun.equals("P") && !v_usergubun.equals("") ) { 
				// ��w�̳� �Ǹ�a(��) vȸ���(ȸ��v��; �ش�.)
			sql += " and substr(a.comp,1,4) = " + SQLString.Format(v_compgubun);
			sql += " and nvl(a.usergubun,'') = " + SQLString.Format(v_usergubun);
			}
			else if ( v_usergubun.equals("P") ) { 
				// �迭�� vȸ���
			sql += " and nvl(a.comp,'') like " + SQLString.Format("%" + v_comp + "%");
			sql += " and nvl(a.usergubun,'') = " + SQLString.Format(v_usergubun);
			}
			else{ 
			sql += " and substr(a.comp,1,4) = " + SQLString.Format(v_compgubun);
			}
			sql += " and nvl(a.userid,'') like " + SQLString.Format(v_userid + "%");
			sql += " and nvl(a.name,'') like " + SQLString.Format("%" +v_name + "%"); 
			sql += " and a.usergubun = ANY('F','D','P') and b.isdeleted( +) = 'N' ";
			
			
			// and substr(a.comp,1,4) like " + SQLString.Format(v_compgubun) + " and nvl(a.comp,'') like ";
			// sql +=   SQLString.Format(v_comp) + " and nvl(a.usergubun,'') like " + SQLString.Format(v_usergubun) + " and nvl(a.userid,'') like " ;
			// sql +=   SQLString.Format(v_userid + "%") + " and nvl(a.name,'') like " +  SQLString.Format("%" +v_name + "%"); 
			
			
            ls = connMgr.executeQuery(sql);
			
			// ls.setPageSize(row);             //  ������� row ���� �����Ѵ�
            // ls.setCurrentPage(v_pageno);                    //     �����������ȣ�� �����Ѵ�.
            // int total_page_count = ls.getTotalPage();       //     ��ü ������ �� ��ȯ�Ѵ�
            // int total_row_count = ls.getTotalCount();    //     ��ü row �� ��ȯ�Ѵ�

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                // dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));

                list.add(dbox);
            }
             
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }        //      �� �ݾ��ش�
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    ȸ��� �޺��ڽ�(����ڵ���) �ɼ�
    @param box			receive from the form object and session
    @param isChange		���ú���� ������ε� ����(true,false)
    @param isALL		ALL ǥ�ÿ���(true,false)
    @return String		�޺��ڽ�html��
    */
	public static String getCompH(RequestBox box, boolean isChange, boolean isALL) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;        
		ListSet             ls      = null;
		String              sql     = "";
		String result = "";
		String v_comp = "";
		
		try { 
			connMgr = new DBConnectionManager();
						
			sql = "select comp, compnm ";
			sql += " from \"TZ_COMPCLASS\" ";
			sql += " where isdeleted = 'N' ";
			sql += " order by compnm ";
			// sql += " where substr(comp,1,4) = '0101'" ;
			
			pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			ls = new ListSet(pstmt);

			result += getSelectTag2(box, ls, isChange, false, "0", "0");
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, true);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }    
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return result;
	}  


    /**
    ȸ��� �޺��ڽ�(����ڵ���) �ɼ�
    @param box			receive from the form object and session
    @param isChange		���ú���� ������ε� ����(true,false)
    @param isALL		ALL ǥ�ÿ���(true,false)
    @param p_scnt		ǥ�ñ���
    @return String		�޺��ڽ�html��
    */
	public static String getCompK(RequestBox box, boolean isChange, boolean isALL, String p_scnt) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;        
		ListSet             ls      = null;
		String              sql     = "";
		String result = "";
		String v_comp = "";

		try { 
			connMgr = new DBConnectionManager();
						
			sql = "select comp, compnm ";
			sql += " from \"TZ_COMPCLASS\" ";
			sql += " where isdeleted = 'N' ";
			sql += " order by compnm ";
			// sql += " where substr(comp,1,4) = '0102'" ;
			
			pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			ls = new ListSet(pstmt);

			result += getSelectTag2(box, ls, isChange, false, "1", p_scnt);
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, true);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }    
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return result;
	}

	/**
	 * ����ڵ� ���
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public ArrayList selectJikupExcel(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ArrayList           list    = null;
		DataBox             dbox    = null;
		ListSet             ls      = null;
		String              sql     = "";
		     
		try { 
			connMgr = new DBConnectionManager();
			
			list = new ArrayList();
			
			sql = "\n select class_cd, class_nm "
				+ "\n from   tk_hap009t "
				+ "\n where  close_dt = '        ' "
				+ "\n order  by class_cd ";
				
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
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return list;
	}  
}