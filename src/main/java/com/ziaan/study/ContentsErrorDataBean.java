// **********************************************************
//  1. ��      ��: �Խ���
//  2. ���α׷���: BoardBean.java
//  3. ��      ��: �Խ���
//  4. ȯ      ��: JDK 1.4
//  5. ��      ��: 1.0
//  6. ��      ��: mscho 2008.10.20
//  7. ��      ��:
// **********************************************************
package com.ziaan.study;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.ziaan.common.GetCodenm;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.library.SmsSendBean;

public class ContentsErrorDataBean { 
    private ConfigSet config;
    private int row;
    private int admin_row;
	private	static final String	FILE_TYPE =	"p_file";			// 		���Ͼ��ε�Ǵ� tag name
	private	static final int FILE_LIMIT	= 5;					// 	  �������� ���õ� ����÷�� ����

    public ContentsErrorDataBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        �� ����� �������� row ���� �����Ѵ�
            admin_row = Integer.parseInt(config.getProperty("page.bulletin.adminrow") );        //        �� ����� �������� row ���� �����Ѵ�            
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
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

        int v_pageno = box.getInt("p_pageno");
        String v_subj     	= box.getStringDefault("p_subj", box.getSession("s_subj"));
        String v_userid    	= box.getSession("userid");        
        String v_gadmin     = box.getSession("gadmin"); 
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");        

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql = "\n select seq "
            	+ "\n      , subj "
            	+ "\n      , subjnm "
            	+ "\n      , rgubun "
            	+ "\n      , requestid "
            	+ "\n      , get_name(requestid) name "
            	+ "\n      , requestdt "
            	+ "\n      , title "
            	+ "\n      , cpseq "
            	+ "\n      , decode(get_cpnm(a.cpseq), '-', get_cpasnm(a.cpseq), get_cpnm(a.cpseq)) cpnm "
            	+ "\n      , get_name(completeid) cname "
            	+ "\n      , completedt "
            	+ "\n      , completeid "
            	+ "\n      , status "
            	+ "\n from   tz_contentserror a "
            	+ "\n where  a.subj = " + StringManager.makeSQL(v_subj);
            
            if(v_gadmin.equals("ZZ")){
            	sql += "\n and    a.requestid = '" + v_userid +"' ";
            }
            if ( !v_searchtext.equals("") ) {                //    �˻�� ������

                if ( v_search.equals("requestid") ) {              //    �̸����� �˻��Ҷ�
                    sql += "\n and    a.requestid like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                }
                else if ( v_search.equals("title") ) {        //    �������� �˻��Ҷ�
                    sql += "\n and    a.title like " + StringManager.makeSQL("%" + v_searchtext + "%") + "";
                }
                else if ( v_search.equals("content") ) {     //    �������� �˻��Ҷ�
                    sql += "\n and    (a.contents like " + StringManager.makeSQL("%" + v_searchtext + "%") + " or a.contents like " + StringManager.makeSQL("%" + v_searchtext) + " or a.contents like " + StringManager.makeSQL( v_searchtext + "%") + ")";            //   Oracle 9i ��
                }
            }            
            sql += "\n order  by seq desc";            

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
     * �ڷ�� ����Ʈȭ�� select
     * @param    box          receive from the form object and session
     * @return ArrayList  �ڷ�� ����Ʈ
     * @throws Exception
     */
     public ArrayList selectBoardListAdmin(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         ArrayList           list    = null;
         String              sql     = "";
         DataBox             dbox    = null;

         int v_pageno 			= box.getInt("p_pageno");
         String v_subj     		= box.getStringDefault("p_subj", box.getSession("s_subj"));
         String v_userid    	= box.getSession("userid");        
         String v_gadmin     	= box.getSession("gadmin"); 
         String v_searchtext 	= box.getString("p_searchtext");
         String v_search     	= box.getString("p_search"); 
         String v_cpseq     	= box.getString("p_cpseq");          
         String v_order     	= box.getString("p_order"); 
         String v_orderType     = box.getStringDefault("p_orderType", "asc");          
         
         v_gadmin = v_gadmin.substring(0, 1);

         try { 
             connMgr = new DBConnectionManager();

             list = new ArrayList();

             sql = "\n select seq, subj, subjnm, rgubun, requestid, get_name(requestid) name "
            	 + "\n      , requestdt, title, cpseq "
            	 + "\n      , decode(get_cpnm(a.cpseq), '-', get_cpasnm(a.cpseq), get_cpnm(a.cpseq)) cpnm "
            	 + "\n      , get_name(completeid) cname, completedt, completeid, status "
             	 + "\n from   tz_contentserror a "
             	 + "\n where  1=1 ";

             if ( !v_searchtext.equals("") ) {                //    �˻�� ������
                 // v_pageno = 1;   //      �˻��� ��� ù��° �������� �ε��ȴ�

                 if ( v_search.equals("requestid") ) {              //    �̸����� �˻��Ҷ�
                     sql += "\n and    a.requestid like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                 }
                 else if ( v_search.equals("title") ) {        //    �������� �˻��Ҷ�
                     sql += "\n and    a.title like " + StringManager.makeSQL("%" + v_searchtext + "%") + "";
                 }
                 else if ( v_search.equals("content") ) {     //    �������� �˻��Ҷ�
                     // sql += " and dbms_lob.instr(a.content, " + StringManager.makeSQL(v_searchtext) + ",1,1) < > 0";
                     sql += "\n and    (a.contents like " + StringManager.makeSQL("%" + v_searchtext + "%") + " or a.contents like " + StringManager.makeSQL("%" + v_searchtext) + " or a.contents like " + StringManager.makeSQL( v_searchtext + "%") + ")";            //   Oracle 9i ��
                 }
             }     
             
             //if( !v_cpseq.equals("")){
             //	 sql += "\n and    a.cpseq = '"+v_cpseq+"' ";
             //}               
             
             if( v_gadmin.equals("M")){
            	 sql += "\n and    a.cpseq = (select cpseq from tz_cpinfo where userid = '"+v_userid+"') ";
            	 //sql += "\n and    a.completeid = '"+v_userid+"' and status not in('I','R') ";
             }                        
             
             if(!v_order.equals("")){
                 sql += "\n order  by "+ v_order + " " +v_orderType ;            	 
             }else{
            	 sql += "\n order by seq desc";
             }
         
             //System.out.println(sql);

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

        int v_seq       = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();

            sql = "\n select a.* "
            	+ "\n      , decode(get_cpnm(a.cpseq), '-', get_cpasnm(a.cpseq), get_cpnm(a.cpseq)) as cpnm "
            	+ "\n      , get_name(requestid) as requestnm "
            	+ "\n      , (select handphone from tz_member where userid = requestid) rhandphone "
            	+ "\n      , get_name(acceptid) as acceptnm "
            	+ "\n      , (select handphone from tz_member where userid = acceptid) ahandphone "
            	+ "\n      , get_name(completeid) as completenm "
            	+ "\n      , (select handphone from tz_member where userid = completeid) chandphone "
            	+ "\n from   tz_contentserror a "
            	+ "\n where  a.seq = " + v_seq;                             
            
            ls = connMgr.executeQuery(sql);
            while (ls.next()) { 
                dbox = ls.getDataBox();
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
        
        ResultSet rs3   = null;
        Statement stmt3 = null;            
        String sql3   = "";        
        int isOk1     = 1;
        int v_seq     = 0;

        String v_subj    = box.getStringDefault("p_subj", box.getSession("s_subj"));      
        String v_title   = box.getString("p_title");
        String v_content = box.getString("p_content");
        String s_userid  = box.getSession("userid");
        String s_usernm  = box.getSession("name");
        String s_gadmin  = box.getSession("gadmin");
        		s_gadmin =  s_gadmin.substring(0,1);
        		
        // ----------------------   ���ε�Ǵ� ������ ������ �˰� �ڵ��ؾ��Ѵ�  --------------------------------
		String v_realFileName 	= box.getRealFileName(FILE_TYPE);
		String v_newFileName	= box.getNewFileName(FILE_TYPE);        		
        
		try { 
            connMgr = new DBConnectionManager();
            stmt1 = connMgr.createStatement();
            connMgr.setAutoCommit(false);
            
            // ----------------------   �Խ��� ��ȣ �����´� ----------------------------
            sql = "select nvl(max(seq), 0) from TZ_CONTENTSERROR";
            rs1 = stmt1.executeQuery(sql);
            if ( rs1.next() ) { 
                v_seq = rs1.getInt(1) + 1;
            }
            // -------------------------------------------------------------------------
			
            // ----------------------   �Խ��� table �� �Է�  --------------------------
            sql1= "\n insert into TZ_CONTENTSERROR ( "
            	+ "\n        seq "
            	+ "\n      , subj "
            	+ "\n      , subjnm "
            	+ "\n      , rgubun "
            	+ "\n      , requestid "
            	+ "\n      , requestdt "
            	+ "\n      , title "
            	+ "\n      , contents "
            	+ "\n      , realfile "
            	+ "\n      , savefile "
            	+ "\n      , cpseq "
            	+ "\n      , completeid "
            	+ "\n      , status "
            	+ "\n ) "
            	+ "\n values ( "
            	+ "\n        ? "
            	+ "\n      , ? "
            	+ "\n      , (select subjnm from tz_subj where subj=?) "
            	+ "\n      , ? "
            	+ "\n      , ? "
            	+ "\n      , to_char(sysdate, 'YYYYMMDDHH24MISS') "
            	+ "\n      , ? "
            	+ "\n      , ? "
//            	+ "\n      , empty_clob() "
            	+ "\n      , ? "
            	+ "\n      , ? "
            	+ "\n      , (select producer from tz_subj where subj = ?) "
            	+ "\n      , (select userid from tz_cpasinfo a, tz_subj b where a.cpseq = b.producer and b.subj = ?) "
            	+ "\n      , 'I' "
            	+ "\n ) ";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_seq);
            pstmt1.setString(2, v_subj);            
            pstmt1.setString(3, v_subj);
            pstmt1.setString(4, s_gadmin);
            pstmt1.setString(5, s_userid);            
            pstmt1.setString(6, v_title);
            pstmt1.setString(7, v_content);
			pstmt1.setString(8, v_realFileName);
			pstmt1.setString(9, v_newFileName); 
            pstmt1.setString(10, v_subj);
            pstmt1.setString(11, v_subj);            
            isOk1 = pstmt1.executeUpdate();
            
			sql2 = "select contents from TZ_CONTENTSERROR where seq = " + v_seq;
//			connMgr.setOracleCLOB(sql2, v_content);       //      (��Ÿ ���� ���)       				
            
            if ( isOk1 > 0) { 
                connMgr.commit();

    			String v_subjnm 	= GetCodenm.get_subjnm(v_subj);
    			String v_userid 	= "";
    			String v_handphone  = "";	
    			
                // ----------------------   SMS �߼��� ���� �����´� ----------------------------
                stmt3 = connMgr.createStatement();
                sql3= "\n select a.userid, c.name, c.handphone "
                	+ "\n from   tz_manager a, tz_subjman b, tz_member c "
                	+ "\n where  a.gadmin like 'F%' "
                	+ "\n and    b.subj = " + StringManager.makeSQL(v_subj)
                	+ "\n and    a.gadmin = b.gadmin "
                	+ "\n and    a.userid = b.userid "
                	+ "\n and    b.userid = c.userid ";
                rs3 = stmt3.executeQuery(sql3);
                
                if ( rs3.next() ) { 
                    v_userid = rs3.getString(1);
                    v_handphone = rs3.getString(3);                
                }
                
                if(!v_handphone.equals("")){
                
	                v_subjnm = "[" + v_subjnm+ "]���� ����������������û�� �����Ǿ����ϴ�.";
	                // -------------------------------------------------------------------------
	                box.put("p_checks", v_userid); 
	                box.put("p_handphone", v_handphone);             
	                box.put("p_title", v_subjnm);   
	                
	                SmsSendBean sms = new SmsSendBean();
	                sms.sendSMSMsg2(box);                                
                }
            } else { 
                connMgr.rollback();
            }   
            
        }
        catch ( Exception ex ) {
            connMgr.rollback();   
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( rs3 != null ) { try { rs3.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }            
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk1;
    }

     
     /**
      * ��� �ڷ��  ���� ���
      * @param box      receive from the form object and session
      * @return isOk    1:insert success,0:insert fail
      * @throws Exception
      */
       public int insertBoard2(RequestBox box) throws Exception { 
          DBConnectionManager	connMgr	= null;
          ResultSet rs1   = null;
          Statement stmt1 = null;
          PreparedStatement pstmt1  = null;
          String sql    = "";
          String sql1   = "";
          String sql2   = "";
          int isOk1     = 1;
          int v_seq     = 0;

          String v_subj    		= box.getString("p_subj");    
          String v_subjnm   	= box.getString("p_subjnm");
          String v_requestid	= box.getString("p_requestid");       
          String v_cpseq   		= box.getString("p_cpseq");    
          String v_completeid   = box.getString("p_completeid");              
          String v_completenm   = box.getString("p_completenm");              
          String v_completetel  = box.getString("p_completetel");              
          String v_title   		= box.getString("p_title");
          String v_content 		= box.getString("p_content");
          String v_issms 		= box.getString("p_issms");
          String s_userid  		= box.getSession("userid");
          String s_usernm  		= box.getSession("name");
          String s_gadmin  		= box.getSession("gadmin");
          		s_gadmin =  s_gadmin.substring(0,1);
        		
          // ----------------------   ���ε�Ǵ� ������ ������ �˰� �ڵ��ؾ��Ѵ�  --------------------------------
  		String v_realFileName 	= box.getRealFileName(FILE_TYPE);
  		String v_newFileName	= box.getNewFileName(FILE_TYPE);        		
          
  		try { 
              connMgr = new DBConnectionManager();
              connMgr.setAutoCommit(false);
              
              stmt1 = connMgr.createStatement();

              // ----------------------   �Խ��� ��ȣ �����´� ----------------------------
              sql = "select nvl(max(seq), 0) from TZ_CONTENTSERROR";
              rs1 = stmt1.executeQuery(sql);
              if ( rs1.next() ) { 
                  v_seq = rs1.getInt(1) + 1;
              }
              rs1.close();
              // -------------------------------------------------------------------------
  			
              // ----------------------   �Խ��� table �� �Է�  --------------------------
              sql1 =  " insert into TZ_CONTENTSERROR(seq, subj, subjnm, rgubun, requestid, requestdt, title, contents, realfile, savefile, acceptid, acceptdt, cpseq, completeid, status)  ";
              sql1 += " values (?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, 'A')";
//              sql1 += " values (?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, 'A')";

              pstmt1 = connMgr.prepareStatement(sql1);
              pstmt1.setInt(1, v_seq);
              pstmt1.setString(2, v_subj);            
              pstmt1.setString(3, v_subjnm);
              pstmt1.setString(4, s_gadmin);
              pstmt1.setString(5, v_requestid);            
              pstmt1.setString(6, v_title);
              pstmt1.setString(7, v_content);
  			  pstmt1.setString(8, v_realFileName);
  			  pstmt1.setString(9, v_newFileName); 
              pstmt1.setString(10, s_userid);  			
              pstmt1.setString(11, v_cpseq);	
              pstmt1.setString(12, v_completeid);               
                               
              isOk1 = pstmt1.executeUpdate();
              
  			  sql2 = "select contents from TZ_CONTENTSERROR where seq = " + v_seq;
//  			  connMgr.setOracleCLOB(sql2, v_content);       //      (��Ÿ ���� ���)       
  			
  			  if ( isOk1 > 0) { 
            	  if ("Y".equals(v_issms)) {
                      box.put("p_checks", v_completeid);
                      box.put("p_names", v_completenm);
                      box.put("p_handphone", v_completetel);
                      box.put("p_title","e-Eureka[" + v_subjnm+ "]������  ������������ϵǾ����ϴ�.����Ȯ�κ�Ź�帳�ϴ�.");
                      SmsSendBean ssbean = new SmsSendBean();
                      ssbean.sendSms(box);
            	  }
                  connMgr.commit();
              } else { 
                  connMgr.rollback();
              }              

          }
          catch ( Exception ex ) {  
              connMgr.rollback();        	  
              if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
              ErrorManager.getErrorStackTrace(ex, box, sql1);
              throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
          }
          finally { 
              if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
              if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
              if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
              if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }
          return isOk1;
      }
     


    /**
    * ���õ� �ڷ� �󼼳��� ����
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
     public int updateBoard(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1  = null;
        String sql1 = "";
        String sql2 = "";

        int 	isOk1 		= 1;
        int    	v_seq       = box.getInt("p_seq");
        String 	v_title     = box.getString("p_title");
        // String v_content =  StringManager.replace(box.getString("content"),"<br > ","\n"); 
        String v_content   	= box.getString("p_content");
        String v_savefile   = box.getString("p_savefile"); // ���û�������
        String v_realfile   = box.getString("p_realfile"); // ���û�������      
        String v_filedel    = box.getString("p_filedel"); // ���û�������               
        String s_userid 	= box.getSession("userid");
        String s_usernm 	= box.getSession("name");

        // ----------------------   ���ε�Ǵ� ������ ������ �˰� �ڵ��ؾ��Ѵ�  --------------------------------
		String v_realFileName 	= box.getRealFileName(FILE_TYPE);
		String v_newFileName	= box.getNewFileName(FILE_TYPE);
		
		if(!v_filedel.equals("Y") && (v_realFileName.equals("") || v_realFileName == null)){
			v_realFileName = v_realfile;
			v_newFileName = v_savefile;
		}
		
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sql1 = "update TZ_CONTENTSERROR set title = ?, realfile=?, savefile=?, contents=?";
//            sql1 = "update TZ_CONTENTSERROR set title = ?, realfile=?, savefile=?, contents=empty_clob()";
            sql1 += "  where seq = ?";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_title);
            pstmt1.setString(2, v_realFileName);
            pstmt1.setString(3, v_newFileName);            
            pstmt1.setString(4, v_content);            
            pstmt1.setInt(5, v_seq);
            isOk1 = pstmt1.executeUpdate();
            
			sql2 = "select contents from TZ_CONTENTSERROR where seq = " + v_seq;
//			connMgr.setOracleCLOB(sql2, v_content);       //      (��Ÿ ���� ���)
			
            if ( isOk1 > 0) { 
                connMgr.commit();
            } else { 
                connMgr.rollback();
            }   
            
        }
        catch ( Exception ex ) { 
            connMgr.rollback();   
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }            
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk1;
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
        int isOk1 = 1;

        int v_seq = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();

            sql1 = "delete from TZ_CONTENTSERROR where seq = ? ";
            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_seq);
            isOk1 = pstmt1.executeUpdate();

        }
        catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box,    sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

    return isOk1;

    }
    
    /**
     * ���õ� �Խù� �ݷ��ϱ�
     * @param box      receive from the form object and session
     * @return isOk    1:delete success,0:delete fail
     * @throws Exception
     */
     public int rejectBoard(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         PreparedStatement pstmt1 = null;
         String sql1 = "";
         int isOk1 = 1;

         int v_seq = box.getInt("p_seq");
         String v_rejectreason    = box.getString("p_rejectreason"); // �ݷ�����               
         String s_userid 	= box.getSession("userid");
         String s_usernm 	= box.getSession("name");         

             try { 
                 connMgr = new DBConnectionManager();
                 
                 sql1 = "update TZ_CONTENTSERROR set status='R', rejectreason = ?, acceptid=?, acceptdt=to_char(sysdate,  'YYYYMMDDHH24MISS')";
                 sql1 += "  where seq = ?";

                 pstmt1 = connMgr.prepareStatement(sql1);
                 pstmt1.setString(1, v_rejectreason);
                 pstmt1.setString(2, s_userid);       
                 pstmt1.setInt(3, v_seq);
                 isOk1 = pstmt1.executeUpdate();
             }
             catch ( Exception ex ) { 
                 ErrorManager.getErrorStackTrace(ex, box,    sql1);
                 throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
             }
             finally { 
                 if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                 if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
                 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
             }

         return isOk1;

     }    

     
    
    /**
     * ���õ� �Խù�  �����ϱ�(���)
     * @param box      receive from the form object and session
     * @return isOk    1:delete success,0:delete fail
     * @throws Exception
     */
     public int acceptBoard(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         PreparedStatement pstmt1 = null;
         String sql1 = "";
         int isOk1 = 1;

         int v_seq = box.getInt("p_seq");             
         String s_userid 	= box.getSession("userid");
         String s_usernm 	= box.getSession("name");         

             try { 
                 connMgr = new DBConnectionManager();
                 
                 sql1 = "update TZ_CONTENTSERROR set status='A', acceptid=?, acceptdt=to_char(sysdate,  'YYYYMMDDHH24MISS')";
                 sql1 += "  where seq = ?";

                 pstmt1 = connMgr.prepareStatement(sql1);
                 pstmt1.setString(1, s_userid);       
                 pstmt1.setInt(2, v_seq);
                 isOk1 = pstmt1.executeUpdate();
             }
             catch ( Exception ex ) { 
                 ErrorManager.getErrorStackTrace(ex, box,    sql1);
                 throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
             }
             finally { 
                 if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                 if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
                 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
             }

         return isOk1;

     }    
     
     
     /**
      * ���õ� �Խù� �����ϱ�(CP)
      * @param box      receive from the form object and session
      * @return isOk    1:delete success,0:delete fail
      * @throws Exception
      */
      public int acceptBoard2(RequestBox box) throws Exception { 
          DBConnectionManager	connMgr	= null;
          PreparedStatement pstmt1 = null;
          String sql1 = "";
          int isOk1 = 1;

          int v_seq = box.getInt("p_seq");             
          String s_userid 	= box.getSession("userid");
          String s_usernm 	= box.getSession("name");         

              try { 
                  connMgr = new DBConnectionManager();
                  
                  sql1 = "update TZ_CONTENTSERROR set status='P', agreeid=?, agreedt=to_char(sysdate,  'YYYYMMDDHH24MISS')";
                  sql1 += "  where seq = ?";

                  pstmt1 = connMgr.prepareStatement(sql1);
                  pstmt1.setString(1, s_userid);       
                  pstmt1.setInt(2, v_seq);
                  isOk1 = pstmt1.executeUpdate();
              }
              catch ( Exception ex ) { 
                  ErrorManager.getErrorStackTrace(ex, box,    sql1);
                  throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
              }
              finally { 
                  if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                  if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
                  if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
              }

          return isOk1;

      }    
      
      
      /**
       * ���õ� �Խù� Ȯ�ο�û�ϱ�(CP)
       * @param box      receive from the form object and session
       * @return isOk    1:delete success,0:delete fail
       * @throws Exception
       */
       public int confirmBoard(RequestBox box) throws Exception { 
           DBConnectionManager	connMgr	= null;
           PreparedStatement pstmt1 = null;
           String sql1 = "";
           String sql2 = "";           
           int isOk1 = 1;

           int v_seq = box.getInt("p_seq");             
           String s_userid 	= box.getSession("userid");
           String s_usernm 	= box.getSession("name");     
           String v_content   	= box.getString("p_content");           

               try { 
                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);
                   
                sql1 = "update TZ_CONTENTSERROR set status='Q', completeid=?, completedt=to_char(sysdate,  'YYYYMMDDHH24MISS'), ccontents=?";
//                sql1 = "update TZ_CONTENTSERROR set status='Q', completeid=?, completedt=to_char(sysdate,  'YYYYMMDDHH24MISS'), ccontents=empty_clob()";
                sql1 += "  where seq = ?";

                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setString(1, s_userid);       
                pstmt1.setString(2, v_content);       
                pstmt1.setInt(3, v_seq);
                isOk1 = pstmt1.executeUpdate();
                   
       			sql2 = "select ccontents from TZ_CONTENTSERROR where seq = " + v_seq;
//    			connMgr.setOracleCLOB(sql2, v_content);       //      (��Ÿ ���� ���)   
    			
                if ( isOk1 > 0) { 
                    connMgr.commit();
                } else { 
                    connMgr.rollback();
                }   
                
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

           return isOk1;

       }       
     
       /**
        * ���õ� �Խù�  ��ó����û�ϱ�(���)
        * @param box      receive from the form object and session
        * @return isOk    1:delete success,0:delete fail
        * @throws Exception
        */
        public int reconfirmBoard(RequestBox box) throws Exception { 
            DBConnectionManager	connMgr	= null;
            PreparedStatement pstmt1 = null;
            String sql1 = "";
            int isOk1 = 1;

            int v_seq = box.getInt("p_seq");             
            String s_userid 	= box.getSession("userid");
            String s_usernm 	= box.getSession("name");         

                try { 
                    connMgr = new DBConnectionManager();
                    
                    sql1 = "update TZ_CONTENTSERROR set status='U', luserid=?, ldate=to_char(sysdate,  'YYYYMMDDHH24MISS')";
                    sql1 += "  where seq = ?";

                    pstmt1 = connMgr.prepareStatement(sql1);
                    pstmt1.setString(1, s_userid);       
                    pstmt1.setInt(2, v_seq);
                    isOk1 = pstmt1.executeUpdate();
                }
                catch ( Exception ex ) { 
                    ErrorManager.getErrorStackTrace(ex, box,    sql1);
                    throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
                }
                finally { 
                    if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                    if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
                    if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
                }

            return isOk1;

        }          
       

       /**
        * ���õ� �Խù�  �Ϸ�ó��(���)
        * @param box      receive from the form object and session
        * @return isOk    1:delete success,0:delete fail
        * @throws Exception
        */
        public int completeBoard(RequestBox box) throws Exception { 
            DBConnectionManager	connMgr	= null;
            PreparedStatement pstmt1 = null;
            String sql1 = "";
            int isOk1 = 1;
            
            ResultSet rs3   = null;
            Statement stmt3 = null;            
            String sql3   = "";                 

            int v_seq = box.getInt("p_seq");             
            String s_userid 	= box.getSession("userid");
            String s_usernm 	= box.getSession("name");         

                try { 
                    connMgr = new DBConnectionManager();
                    
                    sql1 = "update TZ_CONTENTSERROR set status='C', luserid=?, ldate=to_char(sysdate,  'YYYYMMDDHH24MISS')";
                    sql1 += "  where seq = ?";

                    pstmt1 = connMgr.prepareStatement(sql1);
                    pstmt1.setString(1, s_userid);       
                    pstmt1.setInt(2, v_seq);
                    isOk1 = pstmt1.executeUpdate();
                    
        			String v_subjnm 	= "";
        			String v_userid 	= "";
        			String v_handphone  = "";			
                    // ----------------------   SMS �߼��� ���� �����´� ----------------------------
                    stmt3 = connMgr.createStatement();
                    sql3 = "select subjnm, requestid, handphone from TZ_CONTENTSERROR a, TZ_MEMBER b where a.requestid = b.userid and seq ='"+v_seq+"'";
                    rs3 = stmt3.executeQuery(sql3);
                    
                    if ( rs3.next() ) { 
                        v_subjnm = rs3.getString(1);
                        v_userid = rs3.getString(2);
                        v_handphone = rs3.getString(3);                
                    }
                    rs3.close();
                    
                    v_subjnm = v_subjnm+ "]���� ���������������� �Ϸ�Ǿ����ϴ�.";
                    // -------------------------------------------------------------------------
                    box.put("p_checks", v_userid); 
                    box.put("p_handphone", v_handphone);             
                    box.put("p_title", v_subjnm);             
                    SmsSendBean sms = new SmsSendBean();
                    sms.sendSms(box);  
                    
                    
                }
                catch ( Exception ex ) { 
                    ErrorManager.getErrorStackTrace(ex, box,    sql1);
                    throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
                }
                finally { 
                    if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                    if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
                    if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
                }

            return isOk1;

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
       
    
    /**
     * ���� SELECT
     * @param box          receive from the form object and session
     * @return ArrayList   ���� ����Ʈ
     */
      public ArrayList getSubj(RequestBox box) throws Exception { 
          DBConnectionManager	connMgr	= null;   
          ListSet             ls      = null;
          ArrayList           list    = null;
          String              sql     = "";
          DataBox             dbox    = null;
          
          try { 
              
              String s_gadmin = box.getSession("gadmin");
              String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
              String s_userid = box.getSession("userid");
                            
              connMgr = new DBConnectionManager();            

              list = new ArrayList();

              if ( v_gadmin.equals("F") || v_gadmin.equals("P") ) {      //  ���������, ����
            	  
                  sql = "\n select distinct a.subj, a.subjnm, a.cp, b.cpnm, b.userid, get_name(b.userid) as name, b.handphone "
                	  + "\n from   tz_subj a "
                	  + "\n      , tz_cpinfo b "
                	  + "\n      , tz_subjman c "
                	  + "\n where  a.cp = b.cpseq "
                	  + "\n and    a.subj = c.subj "
                	  + "\n and    a.isonoff = 'ON' "
                	  //+ "\n and    a.contenttype != 'L'"
                	  + "\n and    c.userid = " + StringManager.makeSQL(s_userid)
                	  + "\n and    c.gadmin = " + StringManager.makeSQL(s_gadmin)
                	  + "\n order  by a.subjnm ";

              }
              else if ( v_gadmin.equals("H") ) {     //  �����׷������

                  sql = "\n select distinct a.subj, a.subjnm, a.cp, b.cpnm, b.userid, get_name(b.userid) as name, b.handphone "
                	  + "\n from   tz_subj a "
                	  + "\n      , tz_cpinfo b "
                	  + "\n      , ( "
                	  + "\n         select e.subjcourse as subj "
                	  + "\n         from   tz_grsubj e "
                	  + "\n              , tz_grcodeman f "
                	  + "\n         where  e.grcode = f.grcode "
                	  + "\n         and    f.userid = " + StringManager.makeSQL(s_userid)
                	  + "\n         and    f.gadmin = " + StringManager.makeSQL(s_gadmin)
                	  + "\n        ) c "
                	  + "\n where  a.cp = b.cpseq "
                	  + "\n and    a.subj = c.subj "
                	  + "\n and    a.isonoff = 'ON' "
                	  //+ "\n and    a.contenttype != 'L'"
                	  + "\n order  by a.subjnm ";

              }
              else if ( v_gadmin.equals("K") ) {     //  ȸ�������, �μ�������
            	  
                  sql = "\n select distinct a.subj, a.subjnm, a.cp, b.cpnm, b.userid, get_name(b.userid) as name, b.handphone "
                	  + "\n from   tz_subj a "
                	  + "\n      , tz_cpinfo b "
                	  + "\n      , tz_grsubj c "
                	  + "\n      , ( "
                	  + "\n         select e.grcode "
                	  + "\n         from   tz_grcomp e, tz_compman f "
                	  + "\n         where  e.comp = f.comp "
                	  + "\n         and    f.userid = " + StringManager.makeSQL(s_userid)
                	  + "\n         and    f.gadmin = " + StringManager.makeSQL(s_gadmin)
                	  + "\n        ) d "
                	  + "\n where  a.cp = b.cpseq "
                	  + "\n and    a.isonoff = 'ON' "
                	  //+ "\n and    a.contenttype != 'L'"
                	  + "\n and    a.subj = c.subjcourse "
                	  + "\n and    c.grcode = d.grcode "
                	  + "\n order  by a.subjnm ";
                  
              }
              else {      //  Ultravisor, Supervisor
            	  
                  sql = "\n select distinct a.subj, a.subjnm, a.cp, b.cpnm, b.userid, get_name(b.userid) as name, b.handphone "
                	  + "\n from   tz_subj a "
                	  + "\n      , tz_cpinfo b"
                	  + "\n where  a.cp = b.cpseq "
                	  //+ "\n and    a.contenttype != 'L'"
                	  + "\n and    a.isonoff = 'ON' "
                	  + "\n order  by a.subjnm ";
                  
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
              if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }
          return list;
      }    
      
      /**
       * ������� SELECT
       * @param box          receive from the form object and session
       * @return ArrayList   ���� ����Ʈ
       */
        public ArrayList getSubjAdmin(RequestBox box) throws Exception { 
            DBConnectionManager	connMgr	= null;       
            ListSet             ls      = null;
            ArrayList           list    = null;
            String              sql     = "";
            DataBox             dbox    = null;
            
            try { 
                                                
                connMgr = new DBConnectionManager();            

                list = new ArrayList();
                
                /*
                sql = "\n select distinct a.userid, get_name(a.userid) name"
                	+ "\n from   tz_subjman a"
                	+ "\n where  1=1 "
                	+ "\n order  by userid";                
                */
                // 2009.02.19 ����
                // ����⵵, ��������� CM, �̷��װ���
                sql = "select distinct muserid as userid "
                	+ "     , get_name(muserid) as name "
                	+ "from   tz_subjseq "
                	+ "where  substr(subj,5,1) = 'D' "
                	+ "and    year = to_char(sysdate,'yyyy') "
                	+ "and    muserid is not null "
                	+ "order  by get_name(muserid) ";
                
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
         * cp��ü SELECT
         * @param box          receive from the form object and session
         * @return ArrayList   ���� ����Ʈ
         */
        public ArrayList getCpList(RequestBox box) throws Exception { 
        	DBConnectionManager	connMgr	= null;       
        	ListSet             ls      = null;
        	ArrayList           list    = null;
        	String              sql     = "";
        	DataBox             dbox    = null;
        	
        	try { 
        		connMgr = new DBConnectionManager();            
        		list = new ArrayList();
        		
        		/*
                  sql = "\n select cpseq, cpnm "
                	  + "\n from   tz_cpinfo "
                	  + "\n order  by cpnm ";                
        		 */
        		
        		String s_gadmin = box.getSession("gadmin");
        		String s_userid = box.getSession("userid");
        		
        		String v_gadmin = s_gadmin.substring(0,1);
        		
        		
        		// ���־�ü ����Ʈ
        		if ( s_gadmin.equals("S1") || s_gadmin.equals("T1") || v_gadmin.equals("M") ) { 
        			// ���־�ü ������ϰ��(�ش��ü�� ������������)
        			sql = "select cpseq, cpnm ";
        			sql += " from tz_cpinfo where userid = " + SQLString.Format(s_userid);	
        			sql += " order by cpnm";
        		}
        		else{ 
        			// ���־�ü ����ڰ��ƴҰ��(����ü ������ ������)
        			sql = "select cpseq, cpnm ";
        			sql += " from tz_cpinfo ";	
        			sql += " order by cpnm";
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
        		if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        		if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        	}
        	return list;
        }  
          
        
        /**
         * �ڷ�� ����Ʈȭ�� select
         * @param    box          receive from the form object and session
         * @return ArrayList  �ڷ�� ����Ʈ
         * @throws Exception
         */
         public ArrayList selectCalList(RequestBox box) throws Exception { 
             DBConnectionManager	connMgr	= null;
             ListSet             ls      = null;
             ArrayList           list    = null;
             String              sql     = "";
             DataBox             dbox    = null;

             int v_pageno 			= box.getInt("p_pageno");
             String v_userid    	= box.getSession("userid");        
             String v_gadmin     	= box.getSession("gadmin"); 
             String v_year 			= box.getStringDefault("p_year",FormatDate.getDate("yyyy"));
             String v_month     	= box.getStringDefault("p_month",FormatDate.getDate("MM")); 
             String v_order     	= box.getString("p_order"); 
             String v_orderType     = box.getStringDefault("p_orderType", "asc");          
             

             try { 
                 connMgr = new DBConnectionManager();

                 list = new ArrayList();
               
                 sql = "\n select a.subj, a.subjnm, a.cp_accrate, a.goyongpricemajor, a.goyongpriceminor, cpseq, cpnm, b.stucnt, b.grdcnt "
	                 + "\n      , (select price from tz_tutorpay) as tutorfee "
	                 + "\n from   tz_subj a  "
	                 + "\n      , (select x.subj,count(y.userid) stucnt, sum(decode(z.isgraduated,'Y',1,0)) grdcnt "
	                 + "\n         from   tz_subjseq x, tz_student y, tz_stold z "
	                 + "\n         where  x.subj = y.subj(+) "
	                 + "\n         and    x.year = y.year(+) "
	                 + "\n         and    x.subjseq = y.subjseq(+) "
	                 + "\n         and    y.subj = z.subj(+) "
	                 + "\n         and    y.year = z.year(+) "
	                 + "\n         and    y.subjseq = z.subjseq(+) "
	                 + "\n         and    y.userid = z.userid(+) ";
                 if ( !v_year.equals("") ) {              //    �⵵ �˻��Ҷ�
                	 sql += "\n         and    substr(x.eduend,0,4) ='" + v_year + "'";
                 }
                 if ( !v_month.equals("") ) {        //    �� �˻��Ҷ�
                	 sql += "\n         and    substr(x.eduend,5,2) ='" + v_month + "'";
                 }   
                 sql +="\n         group by x.subj having count(y.userid) > 0) b "
	                 + "\n      , tz_cpinfo c "
	                 + "\n where  a.subj = b.subj "
	                 + "\n and    a.cp = c.cpseq "
	                 + "\n and    a.isuse = 'Y' ";
                 
                 if( v_gadmin.startsWith("A") ){
                	 String v_cpseq = box.getString("p_cpseq");
                	 if ( !v_cpseq.equals("") ) {
                		 sql += "\n and    c.cpseq = " + StringManager.makeSQL(v_cpseq);          
                	 }
                 } else if( v_gadmin.equals("M1")){
                	 sql += "\n and    c.cpseq in (select cpseq from tz_cpinfo where userid ='"+v_userid+"')";          
                 }                              
                 
                 if(!v_order.equals("")){
                	 sql += "\n order  by "+ v_order + " " +v_orderType ;            	 
                 }else{
                	 sql += "\n order  by b.subj desc";
                 }

                 ls = connMgr.executeQuery(sql);
                 //ls.setPageSize(row);                       //  �������� row ������ �����Ѵ�
                 //ls.setCurrentPage(v_pageno);               //     ������������ȣ�� �����Ѵ�.
                 //int totalpagecount = ls.getTotalPage();    //     ��ü ������ ���� ��ȯ�Ѵ�
                 //int totalrowcount = ls.getTotalCount();    //     ��ü row ���� ��ȯ�Ѵ�

                 while ( ls.next() ) { 
                     dbox = ls.getDataBox();

                     //dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum()+1));
                     //dbox.put("d_totalpage", new Integer(totalpagecount));
                     //dbox.put("d_rowcount", new Integer(row));
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
          * �ڷ�� ����Ʈȭ�� select
          * @param    box          receive from the form object and session
          * @return ArrayList  �ڷ�� ����Ʈ
          * @throws Exception
          */
          public ArrayList selectCalListExcel(RequestBox box) throws Exception { 
              DBConnectionManager	connMgr	= null;
              ListSet             ls      = null;
              ArrayList           list    = null;
              String              sql     = "";
              DataBox             dbox    = null;

              String v_userid    	= box.getSession("userid");        
              String v_gadmin     	= box.getSession("gadmin"); 
              String v_year 			= box.getStringDefault("p_year",FormatDate.getDate("yyyy"));
              String v_month     	= box.getStringDefault("p_month",FormatDate.getDate("MM")); 
              String v_order     	= box.getString("p_order"); 
              String v_orderType     = box.getStringDefault("p_orderType", "asc");          
              

              try { 
                  connMgr = new DBConnectionManager();

                  list = new ArrayList();
                
                  sql  = " select a.subj, a.subjnm, a.cp_accrate, a.goyongpricemajor, a.goyongpriceminor, cpseq, b.stucnt, b.grdcnt, ";
                  sql += " (select price from tz_tutorpay )tutorfee ";                   
                  sql += "  from TZ_SUBJ a,  ";
                  sql += "   (select x.subj,count(userid) stucnt, count(decode(isgraduated,'Y',isgraduated,'')) grdcnt  from tz_subjseq x, tz_student y ";
                  sql += "     where x.subj=y.subj and x.year=y.year and x.subjseq=y.subjseq ";
                  if ( !v_year.equals("") ) {              //    �⵵ �˻��Ҷ�
                      sql += " and substr(x.eduend,0,4) ='" + v_year + "'";
                  }
                  if ( !v_month.equals("") ) {        //    �� �˻��Ҷ�
                      sql += " and substr(x.eduend,5,2) ='" + v_month + "'";
                  }   
                  sql += " group by x.subj) b,";
                  sql += " TZ_CPINFO c     ";
                  sql += "  where a.subj=b.subj and a.owner=c.cpseq and a.isuse='Y' ";
                  if( v_gadmin.equals("M1")){
                      sql += "  and   c.cpseq in (select cpseq from tz_cpinfo where userid ='"+v_userid+"')";          
                  }                              
                  
                  if(!v_order.equals("")){
                      sql += "  order by "+ v_order + " " +v_orderType ;            	 
                  }else{
                  sql += "  order by b.subj desc";
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
                  if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                  if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
              }
              return list;
          }
         
        
         
         /**
          * �ڷ�� ����Ʈȭ�� select
          * @param    box          receive from the form object and session
          * @return ArrayList  �ڷ�� ����Ʈ
          * @throws Exception
          */
          public ArrayList selectStaticList(RequestBox box) throws Exception { 
              DBConnectionManager	connMgr	= null;
              ListSet             ls      = null;
              ArrayList           list    = null;
              String              sql     = "";
              DataBox             dbox    = null;

              String v_select 		= box.getStringDefault("p_select","cp");
              String v_sdate     	= StringManager.replace(box.getString("p_sdate"),"-",""); 
              String v_ldate     	= StringManager.replace(box.getString("p_ldate"),"-","");               
              String v_order     	= box.getString("p_order"); 
              String v_orderType    = box.getStringDefault("p_orderType", "asc");          

              try { 
                  connMgr = new DBConnectionManager();

                  list = new ArrayList();
                    
                  if(v_select.equals("cp")){
                	  sql =  "\r\n select b.cpnm gubunnm, acnt, pcnt, qcnt, ucnt, ccnt from ";
                      sql += "\r\n        (select cpseq, ";
                      sql += "\r\n 				  count(decode(status, 'A','1','')) acnt, ";
                      sql += "\r\n 				  count(decode(status, 'P','1','')) pcnt, ";
                      sql += "\r\n 				  count(decode(status, 'Q','1','')) qcnt, ";
                      sql += "\r\n 				  count(decode(status, 'U','1','')) ucnt, ";
                      sql += "\r\n 				  count(decode(status, 'C','1','')) ccnt  ";                         
                      sql += "\r\n    	   from   TZ_CONTENTSERROR where 1=1 ";
                      if( !v_sdate.equals("")&& !v_ldate.equals("")){
                    	  sql += "\r\n         and    requestdt between '"+v_sdate+"' and '"+v_ldate+"'";          
                      }  
                      sql += "\r\n         group  by cpseq) a ";                         
                      sql += "\r\n      , tz_cpinfo b ";                      
                      sql += "\r\n where  a.cpseq(+)=b.cpseq ";                   
                      sql += "\r\n order  by b.cpnm asc";                	                  	  
                  }else if(v_select.equals("subj")){
                	  sql =  "\r\n select b.subjnm gubunnm, acnt, pcnt, qcnt, ucnt, ccnt from ";                	  
                      sql += "\r\n   ( select subj, ";
                      sql += "\r\n 		count(decode(status, 'A','1','')) acnt, ";
                      sql += "\r\n 		count(decode(status, 'P','1','')) pcnt, ";
                      sql += "\r\n 		count(decode(status, 'Q','1','')) qcnt, ";
                      sql += "\r\n 		count(decode(status, 'U','1','')) ucnt, ";
                      sql += "\r\n 		count(decode(status, 'C','1','')) ccnt  ";                  
                      sql += "\r\n    from TZ_CONTENTSERROR where 1=1 ";
                      if( !v_sdate.equals("")&& !v_ldate.equals("")){
                          sql += "      and   requestdt between '"+v_sdate+"' and '"+v_ldate+"'";          
                      }  
                      sql += "\r\n    group by subj) a, ";                         
                      sql += "\r\n  TZ_SUBJ b ";                      
                      sql += "\r\n  where a.subj(+)=b.subj ";                   
                      sql += "\r\n  order by b.subjnm asc";                  	  
                  }else if(v_select.equals("cm")) {
                	  sql =  "\r\n select distinct get_name(b.userid) gubunnm, acnt, pcnt, qcnt, ucnt, ccnt from ";                  	  
                      sql += "\r\n   ( select acceptid, ";
                      sql += "\r\n 		count(decode(status, 'A','1','')) acnt, ";
                      sql += "\r\n 		count(decode(status, 'P','1','')) pcnt, ";
                      sql += "\r\n 		count(decode(status, 'Q','1','')) qcnt, ";
                      sql += "\r\n 		count(decode(status, 'U','1','')) ucnt, ";
                      sql += "\r\n 		count(decode(status, 'C','1','')) ccnt  ";                  
                      sql += "\r\n    from TZ_CONTENTSERROR where acceptid is not null ";
                      if( !v_sdate.equals("")&& !v_ldate.equals("")){
                          sql += "      and   requestdt between '"+v_sdate+"' and '"+v_ldate+"'";          
                      }  
                      sql += "\r\n    group by acceptid) a, ";                         
                      sql += "\r\n  TZ_SUBJMAN b ";                      
                      sql += "\r\n  where a.acceptid(+)=b.userid ";                   
                      sql += "\r\n  order by get_name(b.userid) asc";             	  
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
                  if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                  if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
              }
              return list;
          }



		public ArrayList selectCPList(RequestBox box) throws Exception {
			
            DBConnectionManager	connMgr	= null;
            ListSet             ls      = null;

            ArrayList           list    = null;
            String              sql     = "";

            try { 
                connMgr = new DBConnectionManager();
                list = new ArrayList();
                  
                sql =
                	"\n  SELECT CPSEQ, CPNM FROM tz_cpinfo  " +
                	"\n  ORDER BY CPSEQ  ";
                
                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    list.add(ls.getDataBox());
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
    
}