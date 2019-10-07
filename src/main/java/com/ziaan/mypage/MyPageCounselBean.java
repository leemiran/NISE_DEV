// **********************************************************
//  1. ��      ��: ���������� counsel
//  2. ���α׷���: MyPageCounselBean.java
//  3. ��      ��: MyPageCounsel Bean
//  4. ȯ      ��: JDK 1.4
//  5. ��      ��: 1.0
//  6. ��      ��: ������ 2006.07. 11
//  7. ��      ��: 
//  8. ��      ��: 
// **********************************************************
package com.ziaan.mypage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;
import com.ziaan.library.DatabaseExecute;

public class MyPageCounselBean {
    private     ConfigSet   config;
    private     int         row;

    public MyPageCounselBean() { 
        try { 
            config          = new ConfigSet();
            row             = Integer.parseInt(config.getProperty("page.bulletin.row") ); // �� ����� �������� row ���� �����Ѵ�.
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
    
    /**
     * ����������-���� ��㳻�� ��
     * @param box
     * @return
     * @throws Exception
     */
    public DataBox selectCounselView(RequestBox box) throws Exception{
    	DataBox dbox = null;
    	DatabaseExecute db = null;
    	StringBuffer strSQL = null;
    	
    	try{
    		db = new DatabaseExecute();
    		strSQL = new StringBuffer();
    		int v_seq = box.getInt("p_seq");
    		
    		strSQL.append(" select title, (select name from tz_member where userid=a.userid ) as userid, ");
    		strSQL.append("        ldate, adate, (select name from tz_member where userid=a.amane ) as amane, content, acontent, savefile, realfile ") ;
    		strSQL.append("   from tz_center_resboard a ") ;
    		strSQL.append("  where seq = " + v_seq) ;    		 	
    		
    		dbox = db.executeQuery(box, strSQL.toString());
    		
    	}catch( Exception ex ){
            ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
            throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );    		
    	}
    	return dbox;
    }
    
    /**
     *  ����������-���ǻ�㳻�� ����Ʈ�� ���� �� �亯 ī��Ʈ
     * @param box
     * @return
     * @throws Exception
     */
    public DataBox getCounselListCount(RequestBox box) throws Exception{
    	DataBox dbox = null;
    	DatabaseExecute db = null;
    	StringBuffer strSQL = null;
    	
    	try{
    		db = new DatabaseExecute();
    		strSQL = new StringBuffer();
    		String v_userid = StringManager.makeSQL(box.getSession("userid"));
    		
    		strSQL.append("select ( ") ;
    		strSQL.append("         select count(*) ") ;
    		strSQL.append("           from tz_center_resboard ") ;
    		strSQL.append("          where userid = " + v_userid);
    		strSQL.append("       ) as cnt, ") ;
    		strSQL.append("       ( ") ;
    		strSQL.append("          select count(*) ") ;
    		strSQL.append("            from tz_center_resboard ") ;
    		strSQL.append("           where userid = " + v_userid);
    		//strSQL.append("             and dbms_lob.getlength(acontent) > 0 ") ;
    		strSQL.append("             and ismeet = 'Y' ") ;
    		strSQL.append("       ) as ans ") ;
    		strSQL.append("  from dual ") ;    	
    		
    		dbox = db.executeQuery(box, strSQL.toString());
    		
    	}catch( Exception ex ){
            ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
            throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );    		
    	}
    	return dbox;
    }
    
    /**
     * ����������-���ǻ�㳻�� ����Ʈ
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectCounselList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
		String sql1 = "";
        DataBox             dbox    = null;
		
		String v_searchtext = box.getString("p_searchtext");
        int v_pageno        = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();
			
            list = new ArrayList();
            
            sql  = " SELECT seq, userid, name, email, title, ldate, ismeet \n"; 
            sql	+= "   FROM tz_center_resboard ";
            sql	+= "  WHERE userid = " + StringManager.makeSQL(box.getSession("userid"));
			
            if ( !v_searchtext.equals("") ) {
                v_pageno = 1;
                    sql += " WHERE title LIKE " + StringManager.makeSQL("%" + v_searchtext + "%");
            }
            sql += " ORDER BY ldate DESC ";
            ls = connMgr.executeQuery(sql);

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
    * ����Ʈȭ�� select
    * @param    box          receive from the form object and session
    * @return ArrayList  �ڷ�� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectQnaBoardList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
        int v_pageno = box.getInt("p_pageno");
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");
        String s_userid     = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "	select	tabseq, seq, title, userid, name, 				";
            sql	+= "		content, indate, refseq, levels, position, 			";
            sql += "		upfile, cnt, recomcnt, luserid, ldate, 				";
            sql += "		gubunA, gubunB, isopen, email, hasanswer,			";
            sql += "		realfile, savefile, gadmin, pwd, auserid, 			";
            sql += "		adate, acontent				 			";
            sql += "	from tz_center_board                                                    ";
            sql += "	where	tabseq	= " + v_tabseq + "                                      ";
            sql += "		and userid	= '" + s_userid + "' 		                        ";
                

            if ( !v_searchtext.equals("") ) {                //    �˻�� ������
                if ( v_search.equals("name") ) {              //    �̸����� �˻��Ҷ�
                    sql += " and name like " + StringManager.makeSQL("%" + v_searchtext + "%") + "";
                }
                else if ( v_search.equals("title") ) {        //    �������� �˻��Ҷ�
                    sql += " and title like " + StringManager.makeSQL("%" + v_searchtext + "%") + "";
                }
                else if ( v_search.equals("content") ) {     //    �������� �˻��Ҷ�
                    sql += " and content like " + StringManager.makeSQL("%" + v_searchtext + "%"); //   Oracle 9i ��
                }
            }            
            sql += " order by refseq desc, position asc                                           ";

            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                       //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);               //     ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();    //     ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum",   new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(row));
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
    * ����Ʈȭ�� select
    * @param    box          receive from the form object and session
    * @return ArrayList  �ڷ�� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectProfessorBoardList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
        int v_pageno = box.getInt("p_pageno");
        String v_searchtext1 = box.getString("p_searchtext1");        
	String v_searchtext2 = box.getString("p_searchtext2");        
        String v_searchtext3 = box.getString("p_searchtext3");
        
        String v_search1     = box.getString("p_search1");        
        String v_search2     = box.getString("p_search2");
        String v_search3     = box.getString("p_search3");
        String s_userid     = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "	select	tabseq, seq, title, userid, name, 				";
            sql	+= "		content, indate, refseq, levels, position, 			";
            sql += "		upfile, cnt, recomcnt, luserid, ldate, 				";
            sql += "		gubunA, gubunB, isopen, email, hasanswer,			";
            sql += "		realfile, savefile, gadmin, pwd, auserid, 			";
            sql += "		adate, acontent, aname			 			";
            sql += "	from tz_center_board                                                    ";
            sql += "	where	tabseq	= " + v_tabseq + "                                      ";
            sql += "		and userid	= '" + s_userid + "' 		                        ";
            
            if (!v_search1.equals("")) {
            	if (!v_search2.equals("")) {
            		sql += " and gubunA = " + StringManager.makeSQL(v_search1) + " ";
            		sql += " and gubunB = " + StringManager.makeSQL(v_search2) + " ";
        	} else {
        		sql += " and gubunA = " + StringManager.makeSQL(v_search1) + " ";        		
        	}     	
            }
                            

            if ( !v_searchtext3.equals("") ) {                //    �˻�� ������
                if ( v_search3.equals("name") ) {              //    �̸����� �˻��Ҷ�
                    sql += " and name like " + StringManager.makeSQL("%" + v_searchtext3 + "%") + "";
                }
                else if ( v_search3.equals("title") ) {        //    �������� �˻��Ҷ�
                    sql += " and title like " + StringManager.makeSQL("%" + v_searchtext3 + "%") + "";
                }
                else if ( v_search3.equals("content") ) {     //    �������� �˻��Ҷ�
                    sql += " and content like " + StringManager.makeSQL("%" + v_searchtext3 + "%"); //   Oracle 9i ��
                }
            }           
            
            if (!v_searchtext1.equals("")) {
            	sql += " and indate>= " + StringManager.makeSQL(v_searchtext1) + " ";
            } 
            
            if (!v_searchtext2.equals("")) {
            	sql += " and substr(indate,0,8)<= " + StringManager.makeSQL(v_searchtext2) + " ";
            }             
            sql += " order by refseq desc, position asc                                           ";

            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                       //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);               //     ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();    //     ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum",   new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(row));
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
    * ����ȭ�� select
    * @param    box          receive from the form object and session
    * @return ArrayList  
    * @throws Exception
    */
    public ArrayList selectResBoardList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
        int v_pageno = box.getInt("p_pageno");
        
        String s_userid     = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "	select	seq, userid, name, tel1, tel2, tel3, 				";
            sql	+= "		email, gubuna, resdate, stime, etime,  				";
            sql += "		content, ldate, ismeet, gubunb, gubunc,				";            
	    sql += "		amane, adate							";            		            
            sql += "	from tz_center_resboard                                                    ";
            sql += "	where	userid	= '" + s_userid + "' 		                        ";                     
            sql += " 	order by resdate desc, stime                                            ";

            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                       //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);               //     ������������ȣ�� �����Ѵ�.
            int totalpagecount = ls.getTotalPage();    //     ��ü ������ ���� ��ȯ�Ѵ�
            int totalrowcount = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum",   new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(row));
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
    * ���õ� ������ ����
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteResDate(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        
        String sql1 = "";
        int isOk1 = 0;
        
        int v_seq = box.getInt("p_seq");        

            try { 
                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);

                isOk1 = 1;
                sql1 = "delete from TZ_CENTER_RESBOARD where seq = ? ";
                pstmt1 = connMgr.prepareStatement(sql1);                
                pstmt1.setInt(1, v_seq);
                isOk1 = pstmt1.executeUpdate();

                if ( isOk1 > 0) { 
                    connMgr.commit();                    
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
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }

        return isOk1;
    }
        
}
