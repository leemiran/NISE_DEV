// **********************************************************
//  1. ��      ��: ���͵���� USER BEAN
//  2. ���α׷���: CenterDeskQnaBean.java
//  3. ��      ��: CenterDeskQna Bean
//  4. ȯ      ��: JDK 1.4
//  5. ��      ��: 1.0
//  6. ��      ��: ������ 2006.07. 11
//  7. ��      ��: 
//  8. ��      ��: 
// **********************************************************
package com.ziaan.centerdesk;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ziaan.common.GetCodenm;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.FreeMailBean;
import com.ziaan.library.ListSet;
import com.ziaan.library.Mailing;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class CenterDeskQnaBean {
    private     ConfigSet   config;
    private     int         row;
	private Logger logger = Logger.getLogger(this.getClass());
    public CenterDeskQnaBean() { 
        try { 
            config          = new ConfigSet();
            row             = Integer.parseInt(config.getProperty("page.bulletin.row") ); // �� ����� �������� row ���� �����Ѵ�.
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
    
    /**
    * ����Ʈȭ�� select
    * @param    box          receive from the form object and session
    * @return ArrayList  �ڷ�� ����Ʈ
    * @throws Exception
    */
    public ArrayList selectBoardList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
        int v_pageno = box.getInt("p_pageno");
        
        String s_grcode     = box.getSession("tem_grcode");
        String s_gadmin     = box.getSession("gadmin");
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");
        String s_userid     = box.getSession("userid");
        String v_gadmin     = "";
        
        if (!s_gadmin.equals("")) {
        	v_gadmin = s_gadmin.substring(0,1);
        }

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "	select	tabseq, seq, title, userid, name, DECODE(userid,'"+s_userid+"','Y',NVL(ISOPEN,'N')) AS chk,		\n";
            sql	+= "		content, indate, refseq, levels, position, 	\n";
            sql += "		upfile, cnt, recomcnt, luserid, ldate, 		\n";
            sql += "		gubunA, gubunB, isopen, email, hasanswer,	\n";
            sql += "		realfile, savefile, gadmin, pwd, auserid, 	\n";
            sql += "		adate, acontent, nvl(istop,'N') as istop	\n";
            sql += "	from tz_center_board		\n";
            sql += "	where	tabseq	= " + v_tabseq + "		\n";
//            sql += "		and (isopen='Y' or userid='"+s_userid+"')	\n";
            
//            if (!s_grcode.equals("")) {
//            	sql += "	and grcode like " + SQLString.Format("%"+s_grcode+"%") ;
//            }else{
//            	sql += "	and grcode is null" ;
//            }
            
            if ( !v_searchtext.equals("") ) {                //    �˻�� ������
                if ( v_search.equals("name") ) {              //    �̸����� �˻��Ҷ�
                    sql += " and name like " + StringManager.makeSQL("%" + v_searchtext + "%") + "	\n";
                }
                else if ( v_search.equals("title") ) {        //    �������� �˻��Ҷ�
                    sql += " and title like " + StringManager.makeSQL("%" + v_searchtext + "%") + "	\n";
                }
                else if ( v_search.equals("content") ) {     //    �������� �˻��Ҷ�
                    sql += " and content like " + StringManager.makeSQL("%" + v_searchtext + "%"); //   Oracle 9i ��
                }
            }
            
            sql += " order by istop desc, refseq desc, position asc  \n";

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
   * �󼼳��� select
   * @param box          receive from the form object and session
   * @return ArrayList   ��ȸ�� ������
   * @throws Exception
   */
   public DataBox selectBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;
        String              v_upcnt = "Y";

        int v_tabseq    = box.getInt("p_tabseq");
        int v_seq       = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();

            sql  = "	select	tabseq, seq, title, userid, name, 				";
            sql	+= "		content, indate, refseq, levels, position, 			";
            sql += "		upfile, cnt, recomcnt, luserid, ldate, 				";
            sql += "		gubunA, gubunB, isopen, email, hasanswer,			";
            sql += "		realfile, savefile, gadmin, pwd, auserid, 			";
            sql += "		adate, acontent, atitle, arealfile realfile2, asavefile savefile2    	 			    ";
            sql += "	from tz_center_board                                                    ";
            sql += "	where	tabseq	= " + v_tabseq + " and seq = " + v_seq + "              ";
           
            
            ls = connMgr.executeQuery(sql);
            for ( int i = 0; ls.next(); i++ ) { 
                dbox = ls.getDataBox();
            }

            if ( !v_upcnt.equals("N") ) { 
                connMgr.executeUpdate("update tz_center_board set cnt = cnt + 1 where  tabseq = " + v_tabseq + " and seq = " + v_seq);
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
     public int Merge(RequestBox box) throws Exception { 
    	 ConfigSet conf = new ConfigSet();
    	 Mailing mailing = null;
    	 DBConnectionManager connMgr = null;
        ListSet rs1   = null;
        PreparedStatement pstmt1  = null;
        String sql    = "";
        String sql1   = "";
        int isOk1     = 1;
        int v_seq     = 0;  // ��ϵ� �Խ��� �Ϸù�ȣ... max��.
        int preIdx    = 1;

        String v_type       = box.getString("p_type");
        
        int v_tabseq     = box.getString("p_tabseq").equals("") ? 1 : box.getInt("p_tabseq");
        
        String v_title      = box.getString("p_title");
        String v_content    = box.getString("p_content").replace("\r\n","<br>");   
        String v_email	    = box.getString("p_email");
        String v_gubuna	    = box.getString("p_gubuna");
        String v_isopen	    = box.getString("p_isopen");
        String v_realFile   = box.getRealFileName("p_file1");
        String v_saveFile   = box.getNewFileName("p_file1");
        String v_gadmin     = box.getSession("gadmin");
        String s_grcode     = box.getSession("tem_grcode");
        
        String mailServer  = conf.getProperty("mail.server");
		String FromName  = box.getSession("userid");
		String FromEmail = box.getSession("email");
		String v_schecks = "";
		String mailForm = "";
		String ToEmail= conf.getProperty("mail.admin.email");
        
        
        
        v_title = StringManager.replace(v_title,"\"","&quot;");
        
        // �Ϲ� ȸ���̶�� �ױ׸� ���� ���Ѵ�. 
        if(box.getSession("gadmin").equals("ZZ"))
        {
            v_title = StringManager.replace(v_title,"<","&lt;");
            v_title = StringManager.replace(v_title,">","&gt;");
            v_content = StringManager.replace(v_content,"\r\n","<br>");   
            v_content = StringManager.replace(v_content,"\r\n","<br>");   
        }
        
        int v_seq_       = box.getInt("p_seq");
        int v_refseq     = box.getString("p_refseq").equals("") ? 0 : box.getInt("p_refseq");
        int v_levels     = box.getString("p_levels").equals("") ? 0 : box.getInt("p_levels");
        int v_position   = box.getString("p_position").equals("") ? 0 : box.getInt("p_position");
        
        String s_userid     = box.getSession("userid");
        String s_userNm     = box.getSession("name");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            if (v_seq_ > 0) {	// �������� ���� ���
                
                sql1 = "update TZ_CENTER_BOARD set title = ?, content=?, gubuna = ?,  isopen=?, email = ? ";//, userid = ?, name = ?, luserid = ?, ldate = to_char(sysdate, 'YYYYMMDDHH24MISS')";
                sql1 += "                          , REALFILE = ?, SAVEFILE = ?  ";
                sql1 += "  where tabseq = ? and seq = ? ";

                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setString(1, v_title);
                pstmt1.setString(2, v_content);
                pstmt1.setString(3, v_gubuna);
                pstmt1.setString(4, v_isopen);
                pstmt1.setString(5, v_email);
                pstmt1.setString(6, v_realFile);
                pstmt1.setString(7, v_saveFile);
                pstmt1.setInt(8, v_tabseq);
                pstmt1.setInt(9, v_seq_);
logger.debug( "CenterDeskQnaBean : "+v_title+"|"+v_content+"|"+v_gubuna+"|"+v_isopen+"|"+v_email+"|"+v_realFile+"|"+v_saveFile+"|"+v_tabseq+"|"+v_seq );
                isOk1 = pstmt1.executeUpdate();

            } else {            // �Է����� �Ѿ�� ���
                //----------------------   �Խ��� ��ȣ �����´� ----------------------------
                sql = "select nvl(max(seq), 0) from TZ_CENTER_BOARD where tabseq = " + v_tabseq;
                rs1 = connMgr.executeQuery(sql);
                if ( rs1.next() ) { 
                    v_seq = rs1.getInt(1) + 1;
                }
                rs1.close();
            	
                // ----------------------   �Խ��� table �� �Է�  --------------------------
                sql1 =  " insert into TZ_CENTER_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate, gadmin, sangdam_gubun, gubuna) ";
                sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?, ?, ?)";
//                sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?, ?, ?)";

                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);
                pstmt1.setInt(2, v_seq);
                pstmt1.setString(3, s_userid);
                pstmt1.setString(4, s_userNm);
                pstmt1.setString(5, v_title);
                pstmt1.setString(6, v_content);
                pstmt1.setInt(7, 0);
                pstmt1.setInt(8, v_seq);
                pstmt1.setInt(9, 1);
                pstmt1.setInt(10, 1);
                pstmt1.setString(11, s_userid);
                pstmt1.setString(12, v_gadmin);
                pstmt1.setString(13, "");
                pstmt1.setString(14, v_gubuna);

                isOk1 = pstmt1.executeUpdate();
                
            }
            
/*



            
            
            
            
            
            
            
            
            // ����̶�� �θ�۹�ȣ�� ������ �����Ѵ�. 
            if(v_type.equals("reply"))
            {
                sql1  = "update TZ_CENTER_BOARD ";
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
                
                //v_refseq = v_refseq;
                v_levels = v_levels + 1;
                v_position = v_position + 1;
            }
            else if(v_type.equals("write"))
            {                
              v_refseq = v_seq;
              v_levels = 1;
              v_position = 1;
            }
            else if(v_type.equals("update"))
            {
              v_seq = v_seq_;
            }
            
            // ----------------------   �Խ��� table �� �Է�  --------------------------
            sql1 =  "merge into TZ_CENTER_BOARD a                              \n" +
                    "using                                                      \n" +
                    "(                                                          \n" +
                    " select                                                    \n" +
                    "       '" + v_tabseq + "' TABSEQ,                          \n" +
                    "       '" + v_seq + "' SEQ                                 \n" +
                    "   from                                                    \n" +
                    "        dual                                               \n" +
                    ") b                                                        \n" +
                    "on ( a.tabseq = b.tabseq and a.seq = b.seq )               \n" +
                    "when matched then                                          \n" +
                    "update set                                                 \n" +
                    "       TITLE       = ?,                                    \n" +   //title                                   
//                    "       CONTENT     = empty_clob(),                         \n" +   //content
                    "       CONTENT     = ?,                         \n" +   //content
                    //"       gadmin      = ?,                                    \n" +   //gadmin
                    "       gubuna       = ?,                                    \n" +   //gubuna
                    "       isopen       = ?,                                    \n" +   //isopen
                    "       email        = ?,                                    \n" +   //email
                    "       savefile      = ?,                                    \n" +  
                    "       realfile      = ?,                                    \n" +   
                    "       LDATE       = to_char(sysdate, 'yyyymmddhh24miss')  \n" +   //ldate
                    "when not matched then                                      \n" +                         
                    "insert values                                              \n" +                    
                    "      (                                                    \n" +               
                    "       b.TABSEQ,                                           \n" +   //tabseq
                    "       b.SEQ,                                              \n" +   //seq
                    "       ?,                                                  \n" +   //title
                    "       ?,                                                  \n" +   //userid
                    "       ?,                                                  \n" +   //username
//                    "       empty_clob(),                                       \n" +   //content
                    "       ?,                                       \n" +   //content
                    "       to_char(sysdate, 'yyyymmddhh24miss'),               \n" +   //indate
                    "       ?,                                                  \n" +   //refseq
                    "       ?,                                                  \n" +   //levels
                    "       ?,                                                  \n" +   //position
                    "       0,                                                  \n" +   //upfile
                    "       0,                                                  \n" +   //cnt
                    "       0,                                                  \n" +   //recomcnt
                    "       ?,                                                  \n" +   //luserid
                    "       to_char(sysdate, 'yyyymmddhh24miss'),               \n" +   //ldate                                
                    "       ?,                                                  \n" +   //gubunA
                    "       '',                                                 \n" +   //gubunB
                    "       ?,                                                  \n" +   //isopen
                    "       ?,                                                  \n" +   //email
                    "       'N',                                                \n" +   //hasanswer
                    "       ?,                                                 \n" +   //realfile
                    "       ?,                                                 \n" +   //savefile
                    "       ?,                                                  \n" +   //gadmin
                    "       '',                                                 \n" +   //pwd
                    "       '',                                                 \n" +   //auserid
                    "       '',                                                 \n" +   //adate
                    "       ?,                                                  \n" +   //acontent
//                    "       empty_clob(),                                       \n" +   //acontent
                    "       '',                                                 \n" +   //adate                    
                    "       '',                                                 \n" +   //atitle
                    "       null,                                               \n" +   //ptr
                    "       null,                                               \n" +   //sangdam_gubun
                    "       ?,                                                   \n" +   //grcode
 // ���������� Q&A ���� ����...��� ��ġ ���� ����
                    "       '',                                                  \n" +   //sangdam_gubun
                    "       '',                                                  \n" +   
                    "       ''                                                  \n" +   
                    "      )                                                    \n";                    



            pstmt1 = connMgr.prepareStatement(sql1);
            
            
            preIdx = 1;
            // ����
            pstmt1.setString(preIdx++, v_title);    
            pstmt1.setString(preIdx++, v_content);    
            pstmt1.setString(preIdx++, v_gubuna);
            pstmt1.setString(preIdx++, v_isopen);
            pstmt1.setString(preIdx++, v_email);       
            pstmt1.setString(preIdx++, v_saveFile);          
            pstmt1.setString(preIdx++, v_realFile);          
            
            // �Է� 
            pstmt1.setString(preIdx++, v_title);
            pstmt1.setString(preIdx++, s_userid);
            pstmt1.setString(preIdx++, s_userNm);
            pstmt1.setString(preIdx++, v_content);
            pstmt1.setInt(preIdx++, v_refseq);
            pstmt1.setInt(preIdx++, v_levels);
            pstmt1.setInt(preIdx++, v_position);
            pstmt1.setString(preIdx++, s_userid);
            pstmt1.setString(preIdx++, v_gubuna);
            pstmt1.setString(preIdx++, v_isopen);
            pstmt1.setString(preIdx++, v_email);
            pstmt1.setString(preIdx++, v_realFile);
            pstmt1.setString(preIdx++, v_saveFile);
            pstmt1.setString(preIdx++, v_gadmin);
            pstmt1.setString(preIdx++, "");
            pstmt1.setString(preIdx++, s_grcode);
            
            isOk1 = pstmt1.executeUpdate();
*/
            if ( pstmt1 != null ) { pstmt1.close(); }
            
            // �������� �Է��Ѵ�.
            //String sql2 = "select CONTENT from TZ_CENTER_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq ;
            //connMgr.setOracleCLOB(sql2, v_content);
            
            //if ( isOk1 > 0 ) {
            //	FreeMailBean bean = new FreeMailBean();
            //    int isOk = bean.QnaSendMail(box);
           //} 
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
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk1;
    }    

   
     
     
     
     
    /**
    * ���õ� Qna ���� (�����)
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt1 = null;
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 0;
        int isOk2 = 0;
        int isOkComment = 0;
        int isOkRecom = 0;

        int v_tabseq = box.getInt("p_tabseq");
        int v_seq = box.getInt("p_seq");


        // �亯 ���� üũ(�亯 ������ �����Ұ�)
        

            try { 
                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);

                isOk1 = 1;
                isOk2 = 1;
                isOkComment = 1;
                isOkRecom = 1;
                sql1 = "delete TZ_CENTER_BOARD where tabseq = ? and seq = ? ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);
                pstmt1.setInt(2, v_seq);
                isOk1 = pstmt1.executeUpdate();
                
                /*
                
                sql1 = "delete TZ_BOARDcomment_general where tabseq = ? and seq = ? ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);
                pstmt1.setInt(2, v_seq);
                isOkComment = pstmt1.executeUpdate();
                
                sql1 = "delete TZ_BOARDrecom_general where tabseq = ? and seq = ? ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);
                pstmt1.setInt(2, v_seq);
                isOkRecom = pstmt1.executeUpdate();
                */

                if ( isOk1 > 0 && isOk2 > 0 ) { 
                    connMgr.commit();
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

        return isOk1*isOk2;

    }    
    
	
}
