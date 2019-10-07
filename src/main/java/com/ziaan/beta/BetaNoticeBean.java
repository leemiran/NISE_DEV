/**
*��Ÿ�׽�Ʈ�ý����� �������� �ڷ�� ��
*<p> ����:BetaNoticeBean.java</p> 
*<p> ����:���������ڷ�� ��</p> 
*<p> Copyright: Copright(c)2004</p> 
*<p> Company: VLC</p> 
*@author ������
*@version 1.0
*/
package com.ziaan.beta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;


public class BetaNoticeBean { 
    private ConfigSet config;
    private int row;
    private String v_type = "CF";	
    public BetaNoticeBean() { 
        try { 
            config = new ConfigSet();  
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        �� ����� �������� row ���� �����Ѵ�
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
  
   /**
    * ���������ڷ�� ȭ�� ����Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   �������� ����Ʈ
    */
    public ArrayList selectPdsList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
		String v_searchtext = box.getString("p_searchtext");
        String v_select = box.getString("p_select");
        int v_pageno = box.getInt("p_pageno");

        try { 
		
            connMgr = new DBConnectionManager();            
            // ----------------------   ��Խ�������������  ������ tabseq�� �����Ѵ� ----------------------------
			sql = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);
			ls = connMgr.executeQuery(sql);
			ls.next();
			int v_tabseq = ls.getInt(1);
			ls.close();
			// ------------------------------------------------------------------------------------

            list = new ArrayList();

            sql = "select a.seq, a.adname, a.adtitle, count(b.realfile) filecnt, a.addate, a.cnt";
            sql += " from tz_Notice a, tz_boardfile b";
            sql += " where a.seq = b.seq( +) and a.tabseq = b.tabseq( +) and a.tabseq =" + v_tabseq;
                        
            if ( !v_searchtext.equals("") ) {      //    �˻�� ������
                v_pageno = 1;   //      �˻��� ��� ù��° �������� �ε��ȴ�
                
               if ( v_select.equals("title") ) {     //    �������� �˻��Ҷ�
                    sql += " and a.adtitle like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
                else if ( v_select.equals("content") ) {     //    �������� �˻��Ҷ�
                    sql += " and a.content like " + StringManager.makeSQL("%" + v_searchtext + "%");            //   Oracle 9i ��
                }                                                                                                                     
            
            }
            sql += " group by a.seq, a.adname, a.adtitle, a.addate, a.cnt";
            sql += " order by a.seq desc";  		
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
           
            ls = new ListSet(pstmt);        //      ListSet (ResultSet) ��ü����

            ls.setPageSize(row);             //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);                    //     ������������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();       //     ��ü ������ ���� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
				
                // -------------------   2003.12.25  ����     -------------------------------------------------------------------
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
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }        //      �� �ݾ��ش�
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }  
  
  /**
    * ���������ڷ�� �󼼺��� 
    * @param box          receive from the form object and session
    * @return DataBox	  ��ȸ�� ���� DataBox�� ��� ����
    */
   public DataBox selectPds(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
		
        DataBox             dbox    = null;
                
        int v_seq = box.getInt("p_seq");
		
        Vector realfileVector = new Vector();
        Vector savefileVector = new Vector();
		
        
        try { 
            connMgr = new DBConnectionManager();
			// ----------------------   ��Խ�������������  ������ tabseq�� �����Ѵ� ----------------------------
			sql = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);
			ls = connMgr.executeQuery(sql);
			ls.next();
			int v_tabseq = ls.getInt(1);
			ls.close();
			// ------------------------------------------------------------------------------------

			
            sql = "select a.seq, a.adname, a.adtitle, a.adcontent, b.fileseq, b.realfile, b.savefile, a.addate, a.cnt";
            sql += " from tz_notice a, tz_boardfile b";   
            sql += " where a.seq = b.seq( +) and a.tabseq = b.tabseq( +) and a.tabseq = " + v_tabseq + "and a.seq = " + v_seq;
            
            ls = connMgr.executeQuery(sql); 
			
            while ( ls.next() ) { 
            // -------------------   2003.12.25  ����     -------------------------------------------------------------------
                dbox = ls.getDataBox();

                realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
            }   
            dbox.put("d_realfile", realfileVector);
            dbox.put("d_savefile", savefileVector);
           
           
           
            // ------------------------------------------------------------------------------------------------------------------------------------
           
            connMgr.executeUpdate("update tz_notice set cnt = cnt + 1 where seq = " + v_seq );        
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
    * ����ȭ�� �������׸���Ʈ
    * @param box          receive from the form object and session
    * @return ArrayList   �������� ����Ʈ
    */
	   
  
    public ArrayList selectNoticeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
		
        int v_pageno = box.getInt("p_pageno");


        try { 
			
            connMgr = new DBConnectionManager();            
            // ----------------------   ��Խ�������������  ������ tabseq�� �����Ѵ� ----------------------------
			sql = "select tabseq from tz_bds where type = " + SQLString.Format(v_type);
			ls = connMgr.executeQuery(sql);
			ls.next();
			int v_tabseq = ls.getInt(1);
			ls.close();
			// ------------------------------------------------------------------------------------

            list = new ArrayList();

            sql = "select a.seq, a.adname, a.adtitle, count(b.realfile) filecnt, a.addate, a.cnt";
            sql += " from tz_Notice a, tz_boardfile b";
            sql += " where a.seq = b.seq( +) and a.tabseq = b.tabseq( +) and a.tabseq =" + v_tabseq;
                        
            
            sql += " group by a.seq, a.adname, a.adtitle, a.addate, a.cnt";
            sql += " order by a.seq desc";  		
            
            pstmt = connMgr.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
           
            ls = new ListSet(pstmt);        //      ListSet (ResultSet) ��ü����

            ls.setPageSize(row);             //  �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);                    //     ������������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();       //     ��ü ������ ���� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

            while ( ls.next() ) { 
				
                // -------------------   2003.12.25  ����     -------------------------------------------------------------------
                dbox = ls.getDataBox();
               
                dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(row));
                
                list.add(dbox);
                if ( list.size() == 5) { 
						break;
					}
					
           }
               
        }
        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }        //      �� �ݾ��ش�
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }
}
