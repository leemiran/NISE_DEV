/**
 * ���Ϲ߼� ����
 * @version 1.0
 */

package com.ziaan.homepage;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;

import com.ziaan.common.UpdateSpareTable;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DataBox;
import com.ziaan.library.DatabaseExecute;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.PreparedBox;
import com.ziaan.library.RequestBox;
import com.ziaan.library.FormMail;
import com.ziaan.library.MailSet;

public class MailBean {

	private ConfigSet config;
    private int row;
    private String dirUploadDefault;
    	
    public MailBean() {
        try{
            config = new ConfigSet();  
            row = Integer.parseInt(config.getProperty("page.bulletin.row"));  //  �� ����� �������� row ���� �����Ѵ�
            dirUploadDefault = config.getProperty("dir.upload.default");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
    ���� �߼� ó��
    @param box      receive from the form object and session
    @return int
    @param  p_toCono  ���Ϲ߼��� ���
    @param  p_toEmail  �н��� �̸���
    @param  p_mailTitle  ���� ����
    @param  p_mailContent  ���� ����
    @param  p_isMailing  ���� ����
    @param  p_sendHtml  Html ���ϸ�
    @return  isMailed   �������� ��������
    */
    public boolean insertMailSend(RequestBox box) throws Exception {
      
        DatabaseExecute db = null;
        DataBox dbox = null;
           
    	boolean isCommit = false;
    	   
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        String sql4 = "";
           
        int v_mailcode = 0;
        int cnt = 0;    		 //  ���Ϲ߼��� ������ �����           
        String v_realFile = "";	 //  �������� ����
        String v_addFile = "";   //  ������   ����
        String isMailOKYN = "Y"; //  �߼� ���� ����
           
        // �ڽ� ��ü
        String s_userid 	   = box.getSession("userid");
        String v_title         = box.getString("p_title");
        String v_userid        = box.getString("p_userid");
        String v_mailContent   = box.getString("p_contents");       //���� ����        
        String v_receiveremail = box.getString("p_receiver");

        StringTokenizer st1 = new StringTokenizer(v_receiveremail,",");
        StringTokenizer st2 = new StringTokenizer(v_userid,",");

        Vector vt1 = new Vector();
        Vector vt2 = new Vector();
           
        while(st1.hasMoreElements()){
            vt1.addElement(st1.nextToken().trim());
            vt2.addElement(st2.nextToken().trim());
        }

        /////////////////////////////////////////////////////////////////////////////
        String v_sendhtml = "";
        FormMail fmail = new FormMail(v_sendhtml);
        MailSet mset = new MailSet(box);
        /////////////////////////////////////////////////////////////////////////////
           
        try {
            db = new DatabaseExecute(box);
               
            //----------------------   �Ϸ� ��ȣ �����´� ----------------------------
            sql1 = " SELECT NVL(MAX(mailcode), 0)+1 mailcode FROM TU_MAIL";               
     
            dbox = db.executeQuery(box, sql1);      
            v_mailcode = dbox.getInt("d_mailcode");

            //----------------------   ���ε�Ǵ� ���� -------------------------------- 
            Vector realFileNames = box.getRealFileNames("p_file");
            Vector newFileNames = box.getNewFileNames("p_file");
               
            for(int i = 0; i < realFileNames.size(); i++) {
              v_realFile = (String)realFileNames.elementAt(i);
              v_addFile = (String)newFileNames.elementAt(i);
            }
               
            //////////////////////////////////   �Խ��� table �� �Է�  /////////////////////////////////
            sql2 = " INSERT INTO TU_MAIL (mailcode, sender, sdate, mailsubj, contents, attfilepath)" +
            		" VALUES(?, ?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS'), ?, EMPTY_CLOB(), ?)";     
               
            PreparedBox pbox1 = new PreparedBox("preparedbox");
               
            pbox1.setInt(1, v_mailcode);
            pbox1.setString(2, s_userid);
            pbox1.setString(3, v_title);
            pbox1.setString(4, v_addFile);
               
            //-----------���� �ش� content2 �� empty_clob()�� �����ϰ� ���� ���� ��Ʈ������ ġȯ�Ѵ�.                 
            pbox1.setClob("select contents FROM TU_MAIL WHERE mailcode = " + v_mailcode, v_mailContent);
            
            //////////////////////////////////   ���� table �� �Է�  ////////////////////////////////////
            sql3 =  " INSERT INTO TU_MAILRECV (mailcode, sender, receiver, receiveremail, viewdate) ";
            sql3 += " VALUES(?, ?, ?, ?, null)"; 
            
            PreparedBox pbox2 = new PreparedBox("preparedbox");               
            pbox2.setInt(1, v_mailcode);
            pbox2.setString(2, s_userid);
            pbox2.setVector(3, vt2); //�޴� ��� ���̵�
            pbox2.setVector(4, vt1); //�޴� ��� �̸���
              
            isCommit = db.executeUpdate(new PreparedBox [] {pbox1, pbox2},  new String [] {sql2, sql3});
               
            Enumeration ev1 = vt1.elements(); //�޴� ��� �̸���
            Enumeration ev2 = vt2.elements(); //�޴� ��� ���̵�

            while(ev1.hasMoreElements()){
            	String temp1 = (String)ev1.nextElement(); //�޴� ��� �̸���
            	String temp2 = (String)ev2.nextElement(); //�޴� ��� ���̵�
            	if(isCommit == true){
            		
            		//v_mailContent = "<img src='http://210.125.103.117:8080/servlet/controller.library.SendAcknowledgeServlet?p_process=MailReceiveCheck&p_mailcode="+v_mailcode+"&p_receiver=" + temp2 + "' width=0 height=0>" + v_mailContent;
            		v_mailContent = "<img src='http://edu.sungshin.ac.kr:8080/servlet/controller.library.SendAcknowledgeServlet?p_process=MailReceiveCheck&p_mailcode="+v_mailcode+"&p_receiver=" + temp2 + "' width=0 height=0>" + v_mailContent;
                   
            		if  (v_addFile != "" || v_addFile != null){            			
            			//v_mailContent += "<br><br>*÷�� ���� : <a href='http://210.125.103.117:8080/servlet/controller.library.DownloadServlet?p_savefile=" + v_addFile + "&p_realfile="+ v_realFile+"'>" + v_realFile + "</a>";
            			v_mailContent += "<br><br>*÷�� ���� : <a href='http://edu.sungshin.ac.kr:8080/servlet/controller.library.DownloadServlet?p_savefile=" + v_addFile + "&p_realfile="+ v_realFile+"'>" + v_realFile + "</a>";
            		}

            		mset.setSender(fmail);     //���Ϻ����� ��� ����
            		boolean isMailed = mset.sendMail(s_userid, temp1, v_title, v_mailContent,"1", "");
                   
            		if(isMailed) { //      ���Ϲ߼ۿ� �����ϸ�
            			cnt++;
            			isMailOKYN = "Y";
            		}else{
            			isMailOKYN = "N";
            			//������ ���� ó�� 
            			//������ �� ��� 
            			sql4 =  "\n\r DELETE FROM TU_MAILRECV WHERE MAILCODE= ? ";
                		sql4 += "\n\r AND RECEIVEREMAIL = ?";
            			
            			//���и� ������ ���
                		//sql4 =  "\n\r UPDATE TU_MAILRECV SET MAILOK = N ";
                		//sql4 += "\n\r WHERE MAILCODE= ? ";
                		//sql4 += "\n\r AND RECEIVER = ?";
                		
                		PreparedBox pbox3 = new PreparedBox("preparedbox");               
                        pbox3.setInt(1, v_mailcode);
                        pbox3.setString(2, temp2);
                        
                        isCommit = db.executeUpdate(new PreparedBox [] {pbox3},  new String [] {sql4});
            		}
                }
            }
       } catch (Exception ex) {
    	   ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
           throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
       }
        return isCommit;
    }
    
    /**
    ������ ���� �߼� ó��
    @param box      receive from the form object and session
    @return int
    @param  p_toCono  ���Ϲ߼��� ���
    @param  p_toEmail  �н��� �̸���
    @param  p_mailTitle  ���� ����
    @param  p_mailContent  ���� ����
    @param  p_isMailing  ���� ����
    @param  p_sendHtml  Html ���ϸ�
    @return  isMailed   �������� ��������
    */
    public boolean insertMailProf(RequestBox box) throws Exception {
      
        DatabaseExecute db = null;
        DataBox dbox = null;
           
    	boolean isCommit = false;
    	   
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        String sql4 = "";
           
        int v_mailcode = 0;
        int cnt = 0;    		 //  ���Ϲ߼��� ������ �����           
        String v_realFile = "";	 //  �������� ����
        String v_addFile = "";   //  ������   ����
        String isMailOKYN = "Y"; //  �߼� ���� ����
           
        // �ڽ� ��ü
        String s_userid 	   = box.getSession("userid");
        String v_title         = box.getString("p_title");        
        String v_mailContent   = box.getString("p_contents");       //���� ����
        Vector v_userid        = box.getVector("p_userid");
        Vector v_receiveremail = box.getVector("p_email");

        /////////////////////////////////////////////////////////////////////////////
        String v_sendhtml = "";
        FormMail fmail = new FormMail(v_sendhtml);
        MailSet mset = new MailSet(box);
        /////////////////////////////////////////////////////////////////////////////
           
        try {
            db = new DatabaseExecute(box);
               
            //----------------------   �Ϸ� ��ȣ �����´� ----------------------------
            sql1 = " SELECT NVL(MAX(mailcode), 0)+1 mailcode FROM TU_MAIL";               
     
            dbox = db.executeQuery(box, sql1);      
            v_mailcode = dbox.getInt("d_mailcode");

            //----------------------   ���ε�Ǵ� ���� -------------------------------- 
            Vector realFileNames = box.getRealFileNames("p_file");
            Vector newFileNames = box.getNewFileNames("p_file");
               
            for(int i = 0; i < realFileNames.size(); i++) {
              v_realFile = (String)realFileNames.elementAt(i);
              v_addFile = (String)newFileNames.elementAt(i);
            }
               
            //////////////////////////////////   �Խ��� table �� �Է�  /////////////////////////////////
            sql2 = " INSERT INTO TU_MAIL (mailcode, sender, sdate, mailsubj, contents, attfilepath)" +
            		" VALUES(?, ?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS'), ?, EMPTY_CLOB(), ?)";     
               
            PreparedBox pbox1 = new PreparedBox("preparedbox");
               
            pbox1.setInt(1, v_mailcode);
            pbox1.setString(2, s_userid);
            pbox1.setString(3, v_title);
            pbox1.setString(4, v_addFile);
               
            //-----------���� �ش� content2 �� empty_clob()�� �����ϰ� ���� ���� ��Ʈ������ ġȯ�Ѵ�.                 
            pbox1.setClob("select contents FROM TU_MAIL WHERE mailcode = " + v_mailcode, v_mailContent);
            
            //////////////////////////////////   ���� table �� �Է�  ////////////////////////////////////
            sql3 =  " INSERT INTO TU_MAILRECV (mailcode, sender, receiver, receiveremail, viewdate) ";
            sql3 += " VALUES(?, ?, ?, ?, null)"; 
            
            PreparedBox pbox2 = new PreparedBox("preparedbox");               
            pbox2.setInt(1, v_mailcode);
            pbox2.setString(2, s_userid);
            pbox2.setVector(3, v_userid); //�޴� ��� ���̵�
            pbox2.setVector(4, v_receiveremail); //�޴� ��� �̸���
              
            isCommit = db.executeUpdate(new PreparedBox [] {pbox1, pbox2},  new String [] {sql2, sql3});

            for(int i=0; i < v_userid.size(); i++){
            	//String temp1 = (String)ev1.nextElement(); //�޴� ��� �̸���
            	//String temp2 = (String)ev2.nextElement(); //�޴� ��� ���̵�
            	if(isCommit == true){
            		
            		//v_mailContent = "<img src='http://210.125.103.117:8080/servlet/controller.library.SendAcknowledgeServlet?p_process=MailReceiveCheck&p_mailcode="+v_mailcode+"&p_receiver=" + v_userid.get(i) + "' width=0 height=0>" + v_mailContent;
            		v_mailContent = "<img src='http://edu.sungshin.ac.kr:8080/servlet/controller.library.SendAcknowledgeServlet?p_process=MailReceiveCheck&p_mailcode="+v_mailcode+"&p_receiver=" + v_userid.get(i) + "' width=0 height=0>" + v_mailContent;
                   
            		if  (v_addFile != "" || v_addFile != null){            			
            			//v_mailContent += "<br><br>*÷�� ���� : <a href='http://210.125.103.117:8080/servlet/controller.library.DownloadServlet?p_savefile=" + v_addFile + "&p_realfile="+ v_realFile+"'>" + v_realFile + "</a>";
            			v_mailContent += "<br><br>*÷�� ���� : <a href='http://edu.sungshin.ac.kr:8080/servlet/controller.library.DownloadServlet?p_savefile=" + v_addFile + "&p_realfile="+ v_realFile+"'>" + v_realFile + "</a>";
            		}

            		mset.setSender(fmail);     //���Ϻ����� ��� ����
            		boolean isMailed = mset.sendMail(s_userid, (String)v_receiveremail.get(i), v_title, v_mailContent,"1", "");
                   
            		if(isMailed) { //      ���Ϲ߼ۿ� �����ϸ�
            			cnt++;
            			isMailOKYN = "Y";
            		}else{
            			isMailOKYN = "N";
            			
            			//���� �� �Ұ�� 
            			//sql4 =  "\n\r DELETE FROM TU_MAILRECV WHERE MAILCODE= ? ";
                		//sql4 += "\n\r AND RECEIVEREMAIL = ?";
            			
            			//���и� ������ ���
                		sql4 =  "\n\r UPDATE TU_MAILRECV SET MAILOK = N ";
                		sql4 += "\n\r WHERE MAILCODE= ? ";
                		sql4 += "\n\r AND RECEIVER = ?";
                		
                		PreparedBox pbox3 = new PreparedBox("preparedbox");               
                        pbox3.setInt(1, v_mailcode);
                        pbox3.setString(2, (String)v_receiveremail.get(i));
                        
                        isCommit = db.executeUpdate(new PreparedBox [] {pbox3},  new String [] {sql4});
            		}
                }
            }
       } catch (Exception ex) {
    	   ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
           throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
       }
        return isCommit;
    }

    /** 
    �߼۵� ������ ���� �Ѵ�.
    @param RequestBox box 
    @return ArrayList
    */
    public boolean deleteMailAll(RequestBox box) throws Exception {
    	   
    	   DatabaseExecute db = null;
           DataBox dbox = null;
           
           String sql1 = "";
           String sql2 = "";
           
           String s_userid = box.getSession("userid");
           Vector v_mailcode = box.getVector("p_checks");
           
    	   boolean isCommit = false;
    	   
    	   try {
    		   
    		   box.put("notTransaction", new Boolean(true)); 
    		   
    		   db = new DatabaseExecute(box);
    		   
    		   sql1 =  " DELETE FROM TU_MAILRECV ";
    		   sql1 += " WHERE mailcode= ?";
    		   sql1 += " AND   sender = ?";
    		   

               PreparedBox pbox1 = new PreparedBox("preparedbox");
               
               pbox1.setVector(1, v_mailcode);
               pbox1.setString(2, s_userid);
               
               sql2 = " DELETE FROM TU_MAIL ";
               sql2 += " WHERE mailcode = ?";
               sql2 += " AND   sender = ?";
               
               PreparedBox pbox2 = new PreparedBox("preparedbox");
               
               pbox2.setVector(1, v_mailcode);
               pbox2.setString(2, s_userid);               
               
               isCommit = db.executeUpdate(new PreparedBox [] {pbox1, pbox2},  new String [] {sql1, sql2});
    	   
    	   } catch (Exception ex) {             
    		   ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
           throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
    	   }
           return isCommit;       
       }
       
    /** 
    �߼۵� ������ ���� ���� �Ѵ�.
    @param RequestBox box 
    @return ArrayList
    */
    public boolean deleteMail(RequestBox box) throws Exception {
    	
    	DatabaseExecute db = null;
        DataBox dbox = null;
           
        String sql1 = "";
        String sql2 = "";
           
        String s_userid = box.getSession("userid");
        int	   v_mailcode = box.getInt("p_mailcode"); 
        Vector v_email = box.getVector("p_checks");
           
    	boolean isCommit = false;
    	   
    	try {
    		db = new DatabaseExecute(box);
    		   
    		sql1 =  "\n\r DELETE FROM TU_MAILRECV ";
    		sql1 += "\n\r WHERE mailcode= ?";
    		sql1 += "\n\r AND   sender = ?";
    		sql1 += "\n\r AND   receiveremail= ?";
    		
    		PreparedBox pbox1 = new PreparedBox("preparedbox");
               
            pbox1.setInt(1, v_mailcode);    		
            pbox1.setString(2, s_userid);
            pbox1.setVector(3, v_email);

            isCommit = db.executeUpdate(new PreparedBox [] {pbox1},  new String [] {sql1});
    	   
    	} catch (Exception ex) {             
    		ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
    		throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
    	}
    	return isCommit;       
    }
    	
       /** 
       �߼۵� ���� ����� �����´�.
       @param RequestBox box 
       @return ArrayList
       */
       public ArrayList selectListMailForward(RequestBox box) throws Exception {
           DatabaseExecute db = null;
           ArrayList list = null;
           String sql = "";

           try {
                  
        	   String s_userid = box.getSession("userid");
        	   
        	   db = new DatabaseExecute();
        	   
        	   sql =  " select a.mailcode mailcode, a.mailsubj mailsubj, a.sdate sdate, ";
        	   sql += " (select  count(*) from TU_MAILRECV where mailcode = a.MAILCODE )cnt, ";
        	   sql += " CASE WHEN FLOOR(SYSDATE-1 - TO_DATE(a.sdate, 'yyyymmdd hh24miss')) <= 0 THEN 'new' ELSE 'old' END newicon ";
        	   sql += " from tu_mail a ";
        	   sql += " where	  a.sender = '"+s_userid+"'";
        	   sql += " order by mailcode desc ";
        	   
               box.put("p_isPage", new Boolean(true));     //    ����Ʈ �˻��� ������ �����Ⱑ �ִ� ���
               box.put("p_row", new Integer(row));     //    ����Ʈ �˻��� ������ �����Ⱑ �ִ� �Խ��� ���
        	   
        	   list = db.executeQueryList(box, sql);
           }
           catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
               throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
           }
           return list;
       }

       /** 
       �߼۵� ���� �� ������ ��ȸ�Ѵ�.(�˾����� ���)
       @param RequestBox box 
       @return ArrayList
       */
       public DataBox selectViewMailForward(RequestBox box) throws Exception {
           DatabaseExecute db = null;
           DataBox dbox = null;      
           String sql = "";

           try {
               String s_userid = box.getSession("userid");
               int	  v_mailcode = box.getInt("p_mailcode");
        	   
        	   db = new DatabaseExecute();

        	   sql =  " select a.mailcode mailcode, a.mailsubj mailsubj, a.sdate sdate, ";
        	   sql += " a.contents contents, ";
        	   sql += " CASE WHEN FLOOR(SYSDATE-1 - TO_DATE(a.sdate, 'yyyymmdd hh24miss')) <= 0 THEN 'new' ELSE 'old' END newicon, "; 
        	   sql += " (select count(*) from TU_MAILRECV where mailcode = a.MAILCODE )cnt "; 
        	   sql += " from tu_mail a "; 
        	   sql += " where a.sender = '"+s_userid+"'"; 
        	   sql += " and	  a.mailcode ="+v_mailcode; 
        	   sql += " order by mailcode desc ";
        	   
        	   dbox = db.executeQuery(box, sql);           	
           }
           catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
               throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
           }
           return dbox;           
   	}
       
       /** 
       �����ϴ� �縧���� ����� �����´�.
       @param RequestBox box 
       @return ArrayList
       */
       public ArrayList selectListMailMemeber(RequestBox box) throws Exception {
           DatabaseExecute db = null;
           ArrayList list = null;
           String sql = "";

           try {
                  
        	   String s_userid = box.getSession("userid");
        	   int	  v_mailcode = box.getInt("p_mailcode");
        	   
        	   db = new DatabaseExecute();
        	   
        	   sql =  " select a.mailcode mailcode, get_position(b.mtype) mtype, ";
        	   sql += " get_dangwa(b.comp,'') dankwa, ";
        	   sql += " get_hakgwa(b.comp,'') hakgwa, ";
        	   sql += " b.name name, ";
        	   sql += " a.receiveremail email,";
        	   sql += " a.viewdate viewdate ";
        	   sql += " from tu_mailrecv a, tu_member b ";
        	   sql += " where a.sender = '"+s_userid+"'";        	   
        	   sql += " and	  a.RECEIVEREMAIL = b.EMAIL ";
        	   sql += " and	  a.mailcode ="+v_mailcode;
        	   sql += " and	  a.RECEIVER = b.USERID(+)";
        	   sql += " order by mailcode desc ";
        	   
        	   list = db.executeQueryList(box, sql);
           }
           catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
               throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
           }
           return list;
       }
       
       /** 
       ����Ȯ�� ó��
       @param RequestBox box 
       @return ArrayList
       */
       public boolean mailReceiveCheck(RequestBox box) throws Exception {
           
    	   DatabaseExecute db = null;
    	   DataBox dbox = null;
    	   String sql1 = "";
           String sql2 = "";
           int		v_cnt=0;
           boolean isCommit = false;
           
           int v_mailcode = box.getInt("p_mailcode");
           String v_receiver = box.getString("p_receiver");

           try {
        	   db = new DatabaseExecute(box);
        	   
        	   sql1 =  "select count(*) cnt from tu_mailrecv ";
        	   sql1 += " where mailcode="+v_mailcode;
        	   sql1 += " and   receiver='"+v_receiver+"'";
        	   sql1 += " and   viewdate is null";
        	   
        	   dbox = db.executeQuery(sql1);      
        	   v_cnt = dbox.getInt("d_cnt");

        	   if(v_cnt > 0){        		   
        	   
        		   sql2 = "update tu_mailrecv set ";
        		   sql2 += " viewdate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";               
        		   sql2 += " where mailcode = ? and receiver = ?";
               
        		   PreparedBox pbox = new PreparedBox("preparedbox");
               
        		   pbox.setInt(1, v_mailcode);
        		   pbox.setString(2, v_receiver);

        		   isCommit = db.executeUpdate(new PreparedBox [] {pbox},  new String [] {sql2});
        	   }               
           } catch (Exception ex) {             
    		   ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
           throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
    	   }
           return isCommit;       
       }
       
    /** 
    �߼��� ����ڸ� �����´�. 
    @param RequestBox box 
    @return ArrayList
    */
    public ArrayList getMailMember(RequestBox box) throws Exception {
    	
      	DatabaseExecute db = null;
      	ArrayList list = null;
        String sql = "";
        String temp = "";
        Vector v_userid = box.getVector("p_checks");
           
        for (int i=0; i < v_userid.size(); i++){
        	temp += "'"+(String)v_userid.get(i)+"'"+",";
        }
        temp = temp.substring(0,temp.length() - 1);
           
        try {
        	db = new DatabaseExecute();             
               
            sql  = "\n\r select userid, name, email, mobile from tu_member ";            
            sql += "\n\r where	  userid in ("+temp.trim()+") ";
            sql += "\n\r and	  email is not null ";
            sql += "\n\r order by userid ";
               
   			list = db.executeQueryList(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }
       
}
