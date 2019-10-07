// **********************************************************
//  1. ��      ��: �̺�Ʈ����
//  2. ���α׷���: EventAdminBean.java
//  3. ��      ��: �̺�Ʈ����
//  4. ȯ      ��: JDK 1.5
//  5. ��      ��: 1.0
//  6. ��      ��: 
//  7. ��      ��:
// **********************************************************
package com.ziaan.event;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.homepage.NoticeData;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class EventAdminBean { 
	
	private ConfigSet config;
    private int row; 
    private int adminrow; 
	
    public EventAdminBean() { 
	    try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );                  //�� ����� �������� row ���� �����Ѵ�
            adminrow = Integer.parseInt(config.getProperty("page.bulletin.adminrow") );        //�� ����� �������� row ���� �����Ѵ�
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }


    /**
     * �̺�Ʈ ����Ʈ ��ȸ
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList SelectList(RequestBox box) throws Exception { 

        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList   list    = null;
        DataBox dbox        = null;        
        StringBuffer strSQL = null;
        
        int v_tabseq = box.getInt("p_tabseq");
        int v_pageno        = box.getInt("p_pageno");
        
		String v_searchtext = box.getString("p_searchtext");
		String v_search     = box.getString("p_search");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            strSQL = new StringBuffer();
            
            strSQL.append("select seq, adtitle, adname, addate, useyn, cnt \n ") ;
            strSQL.append("  from TZ_NOTICE \n ") ;
            strSQL.append(" where tabseq = "+v_tabseq + " \n ");
            
            if ( !v_searchtext.equals("") ) {                                //    �˻�� ������
                if ( v_search.equals("adtitle") ) {                          //    �������� �˻��Ҷ�
                	strSQL.append(" and adtitle like " + StringManager.makeSQL("%" + v_searchtext + "%"));
                } else if ( v_search.equals("adcontents") ) {                //    �������� �˻��Ҷ�
                	strSQL.append(" and adcontent like " + StringManager.makeSQL("%" + v_searchtext + "%"));
                } else if ( v_search.equals("aduserid") ) {                  //    ���̵�� �˻��Ҷ�
                	strSQL.append(" and aduserid  =  " + StringManager.makeSQL( v_searchtext ));
                } else if ( v_search.equals("adname") ) {                    //    �ۼ��ڷ� �˻��Ҷ�
                	strSQL.append(" and adname    like " + StringManager.makeSQL("%" + v_searchtext + "%"));
                } else if ( v_search.equals("addate") ) {                    //    �ۼ��Ϸ� �˻��Ҷ�
                	strSQL.append(" and SUBSTR(addate, 1, 8) <= " + StringManager.makeSQL( StringManager.replace(v_searchtext, ".", "")));
                }
            }
            
            strSQL.append(" order by seq desc \n ") ;
            
            ls = connMgr.executeQuery(strSQL.toString());

			ls.setPageSize(adminrow);                       // �������� row ������ �����Ѵ�
            ls.setCurrentPage(v_pageno);                    // ������������ȣ�� �����Ѵ�.
            int total_page_count = ls.getTotalPage();       // ��ü ������ ���� ��ȯ�Ѵ�
            int total_row_count = ls.getTotalCount();       // ��ü row ���� ��ȯ�Ѵ�
            
            while ( ls.next() ) { 
				dbox = ls.getDataBox();
				dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(adminrow));
				list.add(dbox);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
            throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
     * �̺�Ʈ �� ����
     * @param box
     * @return
     * @throws Exception
     */
    public DataBox selectView(RequestBox box) throws Exception { 
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		StringBuffer strSQL = null;
		NoticeData data = null;
		DataBox dbox = null;

        int v_tabseq = box.getInt("p_tabseq");
		int v_seq = box.getInt("p_seq");
		String v_process = box.getString("p_process");

		try { 
			connMgr = new DBConnectionManager();
			
			strSQL = new StringBuffer();
			
			strSQL.append(" select adname, aduserid, addate, startdate, enddate, useyn, adtitle, adcontent, type, isall, notice_gubun, gubun, get_codenm('0110',gubun) gubunnm, loginyn \n ") ;
			strSQL.append("   from TZ_NOTICE \n ") ;
			strSQL.append("  where tabseq = " + v_tabseq + " \n " ) ;
			strSQL.append("    and seq = " + v_seq) ;

			ls = connMgr.executeQuery(strSQL.toString());

			while ( ls.next() ) { 
				dbox = ls.getDataBox();
			}
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return dbox;
	}
    
    /**
     * �̺�Ʈ ���
     * @param box
     * @return
     * @throws Exception
     */
	public int insertEvent(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		PreparedStatement   pstmt   = null;
		
		StringBuffer strSQL = null;
		int isOk  = 0;
		int v_seq = 0;
		int preIdx = 1;

        int    v_tabseq       = box.getInt("p_tabseq");
        String v_useyn        = box.getString("p_use");         // �������
		String v_startdate    = box.getString("p_startdate");   // �̺�Ʈ ������
		String v_enddate      = box.getString("p_enddate");     // �̺�Ʈ ������
		String v_adtitle      = box.getString("p_adtitle");     // ����
		String v_content      = box.getString("p_content");     // ����
		String s_userid       = box.getSession("userid");       // �ۼ��� ���̵�
		String s_name         = box.getSession("name");         // �ۼ���
		String v_type         = box.getString("p_type");        // ��� ǥ�ÿ���
		String v_isall        = box.getString("p_isall");                  // ����Է��� ǥ�ÿ���
		String v_notice_gubun = box.getString("p_notice_gubun");        // �̺�Ʈ �����ο�
		String v_gubun        = box.getString("p_gubun");               // �̺�Ʈ ����
		String v_loginyn      = box.getString("p_loginyn");               // �Էµ�������迩��
		
		
		try { 

		   connMgr = new DBConnectionManager();
		   connMgr.setAutoCommit(false);
		   strSQL = new StringBuffer();

		   strSQL.append(" select nvl(max(seq), 0)+1 from TZ_NOTICE where tabseq = " + v_tabseq );
		   ls = connMgr.executeQuery(strSQL.toString());
		   
		   if ( ls.next() ) { 
			   v_seq = ls.getInt(1);
		   }
		   
		   strSQL.setLength(0);
		   
		   strSQL.append(" insert into tz_notice (                                           \n ");
		   strSQL.append("   tabseq, seq, startdate, enddate                                 \n ");
		   strSQL.append(" , addate, adtitle, adcontent, aduserid                            \n ");
		   strSQL.append(" , adname, useyn , luserid, ldate                                  \n ");
		   strSQL.append(" , cnt, type, notice_gubun, gubun, isall, loginyn )                \n ");
		   strSQL.append(" values (                                                          \n ");
		   strSQL.append("   ?, ?, ?, ?                                                      \n ");
		   strSQL.append(" , to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, empty_clob(), ?        \n ");
		   strSQL.append(" , ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')                   \n ");
		   strSQL.append(" , ?, ?, ?, ?, ?, ? )                                              \n ");
		   
		   preIdx = 1;
           pstmt = connMgr.prepareStatement(strSQL.toString());
           
           pstmt.setInt   (preIdx++,  v_tabseq);
           pstmt.setInt   (preIdx++,  v_seq);
           pstmt.setString(preIdx++,  v_startdate);
           pstmt.setString(preIdx++,  v_enddate);

           /* addate */
           pstmt.setString(preIdx++,  v_adtitle);
           /* content */
           pstmt.setString(preIdx++,  s_userid);
           
           pstmt.setString(preIdx++,  s_name);
           pstmt.setString(preIdx++,  v_useyn);
           pstmt.setString(preIdx++,  s_userid);
           /* sysdate */  
           pstmt.setInt   (preIdx++,  0);
           pstmt.setString(preIdx++,  v_type);
           pstmt.setString(preIdx++,  v_notice_gubun);
           pstmt.setString(preIdx++,  v_gubun);
           pstmt.setString(preIdx++,  v_isall);
           pstmt.setString(preIdx++,  v_loginyn);
           
           isOk = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }
           
           strSQL.setLength(0);
           
           strSQL.append("select adcontent from TZ_NOTICE where tabseq = " + v_tabseq + " and seq = " + v_seq);
           connMgr.setOracleCLOB(strSQL.toString(), v_content);
           
           if ( isOk > 0 ) { 
           	  connMgr.commit();
           	} else { 
           	  connMgr.rollback();		   	
           	}
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
			throw new Exception("sql - > " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}
	
	/**
	 * �̺�Ʈ ����
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int updateEvent(RequestBox box) throws Exception { 
		DBConnectionManager connMgr = null;
		PreparedStatement   pstmt   = null;
		StringBuffer strSQL = null;
		int isOk = 0;
		
		int v_tabseq = box.getInt("p_tabseq");
		int v_seq    = box.getInt("p_seq");
		
		String v_startdate    = box.getString("p_startdate");
		String v_enddate      = box.getString("p_enddate");
		String v_adtitle      = box.getString("p_adtitle");
		String v_adcontent    = box.getString("p_adcontent");
		String v_useyn        = box.getString("p_use");
		String v_type         = box.getString("p_type");
		String v_isall        = box.getString("p_isall");
		String v_notice_gubun = box.getString("p_notice_gubun");
		String v_gubun        = box.getString("p_gubun");
		String v_loginyn      = box.getString("p_loginyn");     //�Էµ����� ��� ��뿩��
		String s_userid       = box.getSession("userid");

		int preIdx = 1;
		
		
		try { 
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			strSQL = new StringBuffer();
			
			strSQL.append(" update tz_notice set  useyn = ? ");
			strSQL.append("                     , adtitle = ? ");
			strSQL.append("                     , adcontent = empty_clob() ");
			strSQL.append("                     , startdate = ? ");
			strSQL.append("                     , enddate = ? ");
			strSQL.append("                     , luserid = ? ");
			strSQL.append("                     , ldate = to_char(sysdate, 'YYYYDDMMHH24MISS') ");
			strSQL.append("                     , type = ? ");
			strSQL.append("                     , isall = ? ");
			strSQL.append("                     , notice_gubun = ? ");
			strSQL.append("                     , gubun = ? ");
			strSQL.append("                     , loginyn = ? ");
			strSQL.append(" where tabseq = ? ");
			strSQL.append("   and seq = ? ");
			
			pstmt = connMgr.prepareStatement(strSQL.toString());
			
			preIdx = 1;
			
			pstmt.setString(preIdx++, v_useyn);
			pstmt.setString(preIdx++, v_adtitle);
			//pstmt.setString(preIdx++, v_adcontent);
			pstmt.setString(preIdx++, v_startdate);
			pstmt.setString(preIdx++, v_enddate);
			pstmt.setString(preIdx++, s_userid);
			//sysdate
			pstmt.setString(preIdx++, v_type);
			pstmt.setString(preIdx++, v_isall);
			pstmt.setString(preIdx++, v_notice_gubun);
			pstmt.setString(preIdx++, v_gubun);
			pstmt.setString(preIdx++, v_loginyn);
			pstmt.setInt(preIdx++,    v_tabseq);
			pstmt.setInt(preIdx++,    v_seq);
			
			isOk = pstmt.executeUpdate();
			if ( pstmt != null ) { pstmt.close(); }
			
			strSQL.setLength(0);
			strSQL.append( " select adcontent from tz_notice where seq = " + v_seq + " and tabseq = " + v_tabseq );
            connMgr.setOracleCLOB(strSQL.toString(), v_adcontent);           
			
			if ( isOk > 0 ) { 
				connMgr.commit();
				isOk =1;
			}
			else{ 
				connMgr.rollback();
				isOk =0;
			}
		}
		
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}

	/**
	 * �̺�Ʈ ����
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int deleteEvent(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;
		StringBuffer strSQL = null;
		ListSet             ls      = null;

		int isOk = 0;
		int preIdx = 1;

        int v_tabseq = box.getInt("p_tabseq");
		int v_seq  = box.getInt("p_seq");
		int v_usercnt = 0;

		try { 
			connMgr = new DBConnectionManager();
			strSQL = new StringBuffer();

			/* S : �ش� �̺�Ʈ�� �����ڰ� �ִ��� Ȯ�� */
			strSQL.append(" select count(userid) as usercnt \n ") ;
			strSQL.append("   from tz_event \n ") ;
			strSQL.append("  where tabseq = " + v_tabseq +" \n "); 
			strSQL.append("    and seq = " + v_seq +" \n ");

			/* E : �ش� �̺�Ʈ�� �����ڰ� �ִ��� Ȯ�� */
			
			ls = connMgr.executeQuery(strSQL.toString());
			
			if ( ls.next() ) { 
				v_usercnt = ls.getInt(1);
			}
			
			if(v_usercnt > 0){
	            isOk = -1 ; 
           	    return isOk;
			}
			
			strSQL.setLength(0);
			strSQL.append(" delete from TZ_NOTICE           ");
			strSQL.append("  where tabseq = ? and seq = ?  ");

			pstmt = connMgr.prepareStatement(strSQL.toString());
			preIdx = 1;
			
			pstmt.setInt(preIdx++, v_tabseq);
			pstmt.setInt(preIdx++, v_seq);
			
			isOk = pstmt.executeUpdate();
			if ( pstmt != null ) { pstmt.close(); }

		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString() + "\r\n");
			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}
    
    /**
    �̺�Ʈ ������ ����Ʈ ��ȸ
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList SelectParticipantList(RequestBox box) throws Exception { 

        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList   list    = null;
        DataBox dbox        = null;        
        String  sql         = "";
        
        int v_tabseq   = box.getInt("p_tabseq");
        String s_event = box.getString("s_event");
        
        String v_orderColumn    = box.getString       ("p_orderColumn"         );   // ������ �÷���
        String v_orderType      = box.getString       ("p_orderType"           );   // ������ ����

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = " select tabseq, seq, userid, name, adtitle, inputdata1, nvl(prizeyn,'N') prizeyn, indate, ldate, nvl(orders,'') orders, handphone, compnm            \n"
            	+ "   from (	                                                                                            \n"
                + "          select a.tabseq, a.seq, a.userid, get_name(a.userid) name, adtitle,	                        \n"
            	+ "                 inputdata1, inputdata2, inputdata3, inputdata4, inputdata5, prizeyn, a.indate, a.ldate, orders, handphone, get_compnm(comp) compnm	\n"	            	
            	+ "                 from tz_event a , tz_notice b, tz_member c															\n"	
            	+ "          where a.tabseq = b.tabseq                                                               		\n"
                + "            and a.seq    = b.seq                                                               		    \n"
                + "            and a.userid = c.userid(+)                                                       		        \n"
                + "        )                                                                                                \n"
                + "  where 1 = 1                                                                                            \n";
            
            if(!box.getString("p_tabseq").equals("")){
            	sql += "   and tabseq = " + v_tabseq + "                                                                    \n ";
            }
            
            if(!box.getString("s_event").equals("")){
                sql += "   and seq = " + s_event +                                                                        " \n ";
            }
            
	        if( v_orderColumn.equals("") ){
	        	sql += " order by seq desc, indate desc												                        \n";
	        } else { 
	        	sql += " order by " + v_orderColumn + v_orderType + " , indate desc                                         \n";
            }   
	        
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


   /**
    �̺�Ʈ ��÷�� ����
    @param box      receive from the form object and session
    @return int		isOk
    */
     public int updatePrize(RequestBox box) throws Exception { 

 		DBConnectionManager connMgr     = null;
         PreparedStatement   pstmt   	= null;
         String  sql			= "";
         int isOk			= 0;
         int preIdx = 1;

         String s_userid    = box.getSession("userid");
         Vector v_checks	= box.getVector("p_checks");
         Vector v_orders    = box.getVector("p_orders");
         String v_temp = "";
         String v_tempArr[] = new String[v_checks.size()];
         String v_ordersTemp = "";

         try { 
             connMgr = new DBConnectionManager();

 			 sql  = " update tz_event set prizeyn=? , orders = ? , luserid = ?, ldate = to_char(sysdate,'YYYYMMDDHH24MISS') where tabseq = ? and seq = ? and userid = ? ";
             
             pstmt = connMgr.prepareStatement(sql);

             if ( v_checks != null ) { 
                 for ( int i = 0; i < v_checks.size() ; i++ ) {
                	 v_temp = (String)v_checks.elementAt(i);
                	 v_tempArr = v_temp.split(",");
                	 v_ordersTemp = (String)v_orders.elementAt(i);
                	 
                	 preIdx = 1;
                	 pstmt.setString (preIdx++,  "Y");
                	 if(!((String)v_orders.elementAt(i)).equals("")){
                	     pstmt.setInt    (preIdx++, Integer.parseInt(v_ordersTemp));                    
                	 }else{
                		 pstmt.setNull(preIdx++, java.sql.Types.NUMERIC);
                	 }
                     pstmt.setString (preIdx++,  s_userid);                    
                     pstmt.setInt    (preIdx++,  Integer.parseInt(v_tempArr[0]));
                     pstmt.setInt    (preIdx++,  Integer.parseInt(v_tempArr[1]));
                     pstmt.setString (preIdx++,  v_tempArr[2]);
                     isOk = pstmt.executeUpdate();
                 }
             }   
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return isOk;
     }
     
     /**
      * �̺�Ʈ ��÷�� ����
      * @param box
      * @return
      * @throws Exception
      */
     public int deletePrize(RequestBox box) throws Exception { 

  		DBConnectionManager connMgr     = null;
          PreparedStatement   pstmt   	= null;
          String  sql			= "";
          int isOk			= 0;
          int preIdx = 1;

          String s_userid    = box.getSession("userid");
          Vector v_checks	= box.getVector("p_checks");
          Vector v_orders    = box.getVector("p_orders");
          String v_temp = "";
          String v_tempArr[] = new String[v_checks.size()];
          String v_ordersTemp = "";

          try { 
              connMgr = new DBConnectionManager();

  			 sql  = " update tz_event set prizeyn=? , orders = ? , luserid = ?, ldate = to_char(sysdate,'YYYYMMDDHH24MISS') where tabseq = ? and seq = ? and userid = ? ";
              
              pstmt = connMgr.prepareStatement(sql);

              if ( v_checks != null ) { 
                  for ( int i = 0; i < v_checks.size() ; i++ ) {
                 	 v_temp = (String)v_checks.elementAt(i);
                 	 v_tempArr = v_temp.split(",");
                 	 v_ordersTemp = (String)v_orders.elementAt(i);
                 	 
                 	 preIdx = 1;
                 	 pstmt.setString (preIdx++,  "N");
                 	 pstmt.setNull(preIdx++, java.sql.Types.NUMERIC);
                     pstmt.setString (preIdx++,  s_userid);                    
                     pstmt.setInt    (preIdx++,  Integer.parseInt(v_tempArr[0]));
                     pstmt.setInt    (preIdx++,  Integer.parseInt(v_tempArr[1]));
                     pstmt.setString (preIdx++,  v_tempArr[2]);
                     isOk = pstmt.executeUpdate();
                  }
              }   
          } catch ( Exception ex ) { 
              ErrorManager.getErrorStackTrace(ex, box, sql);
              throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
          } finally { 
              if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }

          return isOk;
      }
     
     /**
      * �̺�Ʈ ������ �ڸ�Ʈ ����
      * @param box
      * @return
      * @throws Exception
      */
     public int deleteParticipant(RequestBox box) throws Exception { 
    	 
    	 DBConnectionManager connMgr     = null;
    	 PreparedStatement   pstmt   	= null;
    	 String  sql			= "";
    	 int isOk			= 0;
    	 int preIdx = 1;
    	 
    	 Vector v_checks	= box.getVector("p_checks");
         StringTokenizer st = null;
         String temp = "";
         String v_tabseq = "";
         String v_seq = "";
         String v_userid = "";
         
    	 try { 
    		 connMgr = new DBConnectionManager();
    		 
    		 sql  = " delete from tz_event where tabseq = ? and seq = ? and userid = ? ";
    		 
    		 pstmt = connMgr.prepareStatement(sql);
    		 
    		 if ( v_checks != null ) { 
    		     for (int i=0; i<v_checks.size();i++) {  
    			     temp = v_checks.elementAt(i).toString();
    	             st   = new StringTokenizer(temp, ",");
    				 v_tabseq = st.nextToken();
    				 v_seq    = st.nextToken();
    				 v_userid = st.nextToken();
    				 //v_prizeyn = st.nextToken();
    				 
    				 preIdx = 1;
    				 pstmt.setInt    (preIdx++,  Integer.parseInt(v_tabseq));
    				 pstmt.setInt    (preIdx++,  Integer.parseInt(v_seq));
    				 pstmt.setString (preIdx++,  v_userid);
    				 isOk = pstmt.executeUpdate();
    			 }
    		 }   
    	 } catch ( Exception ex ) { 
    		 ErrorManager.getErrorStackTrace(ex, box, sql);
    		 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	 } finally { 
    		 if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
    		 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	 }
    	 
    	 return isOk;
     }

     /**
      * �̺�Ʈ ������Ȳ
      * @param box
      * @return
      * @throws Exception
      */
     public ArrayList selectListEventStatus(RequestBox box) throws Exception { 

         DBConnectionManager	connMgr	= null;
         ListSet ls          = null;
         ArrayList   list    = null;
         DataBox dbox        = null;        
         StringBuffer strSQL = null;
         
         int v_tabseq = box.getInt("p_tabseq");

         try { 
             connMgr = new DBConnectionManager();
             list = new ArrayList();
             
             strSQL = new StringBuffer();
             
             strSQL.append("select distinct adtitle, tabseq, seq, useyn, gubun, loginyn, cnt, jungwon \n ") ;
             strSQL.append("     , case when to_number(cnt) < to_number(jungwon) or jungwon is null then status \n ") ;
             strSQL.append("            else '����' \n ") ;
             strSQL.append("        end as status \n ") ;
             strSQL.append("  from \n ") ;
             strSQL.append("       ( \n ") ;
             strSQL.append("         select   adtitle, a.tabseq, a.seq, useyn, a.gubun, a.loginyn \n ") ;
             strSQL.append("                , (select nvl(count(userid),0) cnt from tz_event bb where a.tabseq=bb.tabseq and a.seq=bb.seq) as cnt \n ") ;
             strSQL.append("                , notice_gubun as jungwon \n ") ;
             strSQL.append("                , decode( \n ") ;
             strSQL.append("                          a.gubun,'A', \n ") ;
             strSQL.append("                          case when to_char(sysdate, 'yyyymmdd') between trim(startdate) and trim(enddate) then '������' \n ") ;
             strSQL.append("			                   when to_char(sysdate, 'yyyymmdd') < trim(startdate) then  '�غ���' \n ") ;
             strSQL.append("                               when to_char(sysdate, 'yyyymmdd') > trim(enddate) then  '����' \n ") ;
             strSQL.append("                               else '������' \n ") ;
             strSQL.append("                           end \n ") ;
             strSQL.append("                          ,'B', '��÷�ڹ�ǥ' \n ") ;
             strSQL.append("                         )  as status \n ") ;
             strSQL.append("           from tz_notice a, tz_event b \n ") ;
             strSQL.append("          where a.tabseq=b.tabseq(+) \n ") ;
             strSQL.append("            and a.seq=b.seq(+) \n ") ;
             strSQL.append("       ) \n ") ;
             strSQL.append(" where tabseq = " + v_tabseq + " \n ") ;
             strSQL.append("   and useyn = 'Y' " + "  \n ") ;
             strSQL.append("   and gubun = 'A' " + " \n ") ;
             strSQL.append(" order by tabseq desc, seq desc \n ") ;
             
             ls = connMgr.executeQuery(strSQL.toString());

             while ( ls.next() ) { 
 				dbox = ls.getDataBox();
 				list.add(dbox);
             }

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
             throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return list;
     }     

     /**
      * �̺�Ʈ������Ȳ - �Էµ�������� �󼼺���
      * @param box
      * @return
      * @throws Exception
      */
     public ArrayList selectViewStatistics(RequestBox box) throws Exception { 
 		DBConnectionManager connMgr = null;
 		ListSet ls = null;
 		StringBuffer strSQL = null;
 		NoticeData data = null;
 		ArrayList list = null;
 		DataBox dbox = null;

         int v_tabseq = box.getInt("p_tabseq");
 		int v_seq = box.getInt("p_seq");
 		String v_process = box.getString("p_process");

 		try { 
 			connMgr = new DBConnectionManager();
 			list = new ArrayList();
 			
 			strSQL = new StringBuffer();
 			
 			strSQL.append(" select inputdata1, count(userid) cnt \n ") ;
 			strSQL.append("   from TZ_EVENT \n ") ;
 			strSQL.append("  where tabseq = " + v_tabseq + " \n " ) ;
 			strSQL.append("    and seq = " + v_seq + " \n ") ;
 			strSQL.append("  group by inputdata1 \n ") ;

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
 			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
 			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
 		}
 		return list;
 	}
}
