package com.ziaan.library;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.propose.email.log.EmailLog;
import com.ziaan.propose.email.log.impl.EmailLogImplBean;


public class FreeMailBean { 
        
    public FreeMailBean() { 
    }

    public boolean sendFreeMail(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        ListSet             ls      = null;
        String              sql     = "";
        boolean isMailed = false;
	
        int isOk = 0;
        
        ArrayList           list    = null;
        DataBox             dbox    = null;

        Vector v_vchecks = box.getVector("p_checks");
        String v_isMailing = box.getString("p_isMailing");
        String v_schecks = "";
        
        String v_touch = box.getString("p_touch");
        String v_msubjnm = box.getString("p_msubjnm");
        String v_mseqgrnm = box.getString("p_mseqgrnm");
        String v_msubj = box.getString("p_msubj");
        String v_myear = box.getString("p_myear");
        String v_msubjseq = box.getString("p_msubjseq");
        
        // System.out.println("â���� �ٺ�11=" +v_isMailing);
        
        try { 
            connMgr = new DBConnectionManager();
            
            list = new ArrayList();

//// //// //// //// ////  �������� �߼� //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 
			String v_sendhtml = "freeMailForm.html";
			FormMail fmail = new FormMail(v_sendhtml);

            MailSet mset = new MailSet(box);        //      ���� ���� �� �߼�
            mset.setSender(fmail);     //  ���Ϻ����� ��� ����
//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /


            String v_mailTitle = box.getString("p_title");
            fmail.setVariable("content", box.getString("p_content") );
            String v_mailContent = fmail.getNewMailContent();
            for ( int i = 0;i<v_vchecks.size();i++ ) { 
                v_schecks = (String)v_vchecks.elementAt(i);
                sql  = " select email, cono, handphone ";
                sql += "   from tz_member               ";
                sql += "  where userid = " + StringManager.makeSQL(v_schecks);
                // sql += "  where userid = 'lee1'";
                ls = connMgr.executeQuery(sql); 
                while ( ls.next() ) {                    
                    String v_toCono =  ls.getString("cono");
                    String v_toEmail =  ls.getString("email");
                    String v_handphone = ls.getString("handphone");

                    /*isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent, ls.getString("ismailing"), v_sendhtml); */
                    if ( v_isMailing.equals("1") ) { 
                   /* test*/ isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent, "1", v_sendhtml); 
                   }
                   else if ( v_isMailing.equals("2") ) { 
                   	/* test*/ isMailed = mset.sendMail(v_handphone, "HKMC ���", v_mailTitle, "", "2", ""); 
                   }
                   
                  dbox = ls.getDataBox();
                  dbox.put("d_subj", v_msubj);      
                  dbox.put("d_year", v_myear);      
                  dbox.put("d_subjseq", v_msubjseq);   
                  dbox.put("d_userid", v_schecks);    
                  dbox.put("d_touch", v_touch);     
                  dbox.put("d_ismail", v_isMailing);    
                  dbox.put("d_title",v_mailTitle);   
                  if ( isMailed) { 
                    dbox.put("d_isok", "Y");
                  } else { 
                    dbox.put("d_isok", "N");
                  }
                  dbox.put("d_ismailopen", "N");
                  dbox.put("d_subjnm", v_msubjnm);    
                  dbox.put("d_seqgrnm", v_mseqgrnm);   

                }

                isOk = mset.insertHumanTouch(dbox);
                
                System.out.println(list.size() );
                
                ls.close();
                
                
                /*isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent, ls.getString("ismailing"), v_sendhtml); */
            }
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }     
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isMailed;
    }
    
	public int amailSendMail(RequestBox box) throws Exception {
		ConfigSet conf = new ConfigSet();
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;
		Mailing mailing = null;
		EmailLog emailLog=new EmailLogImplBean();
		int v_tabseq=emailLog.getMaxTabseq();
		int isOk = 0;
		String sql = "";
		
		String smailServer  = conf.getProperty("mail.server");
		String sFromName  = box.getStringDefault("from_name", conf.getProperty("mail.admin.name"));
		String sFromEmail = box.getStringDefault("from_email", conf.getProperty("mail.admin.email"));
		String sSubject   = box.getStringDefault("p_title", "�������");
		String sBody      = box.getStringDefault("p_content", "�������");
		String v_schecks = "";
		String mailForm = "";
		
		Vector v_to = box.getVector("to");
		int temp[] = new int[v_to.size()];
		int k = 0;
		
		try {

			/* param ���� �Ѿ�� v_to �� �� ���� ���� */
			for(int j = 0; j < v_to.size(); j++){
			    if(v_to.get(j).equals("")){
			    	v_to.remove(j);
			    	--j;
				}
			}
			
			if(!v_to.isEmpty()){
			
				for(int i = 0 ; i < v_to.size() ; i++) {
					isOk = 0;
					v_schecks = (String)v_to.elementAt(i);
					
					String v_userid = "";
					String v_name = "";
					String v_email = ""; 
					String v_subjnm = "";
					String v_edustart = "";
					String v_eduend = "";
					String v_tstep = "";
					String v_study_time = "";
					
					StringTokenizer v_token = new StringTokenizer(v_schecks, "|");
	
					while(v_token.hasMoreTokens()) {
						v_userid = v_token.nextToken();
						if(v_token.hasMoreTokens()) {
							v_name = v_token.nextToken();
						}
						if(v_token.hasMoreTokens()) {
							v_email = v_token.nextToken();
						}
						if(v_token.hasMoreTokens()) {
							v_subjnm = v_token.nextToken(); 
						}
						if(v_token.hasMoreTokens()) {
							v_edustart = v_token.nextToken();
						}
						if(v_token.hasMoreTokens()) {
							v_eduend = v_token.nextToken();
						}
						if(v_token.hasMoreTokens()) {
							v_tstep = v_token.nextToken();
						}
						if(v_token.hasMoreTokens()) {
							v_study_time = v_token.nextToken();
						}
					}
					 
					// ��ü���� ó��
					String v_msg = sBody;
					v_msg = StringManager.replace(v_msg, "{name}", v_name);
					v_msg = StringManager.replace(v_msg, "{email}", v_email);
					v_msg = StringManager.replace(v_msg, "{subjnm}", v_subjnm);
					v_msg = StringManager.replace(v_msg, "{edustart}", v_edustart);
					v_msg = StringManager.replace(v_msg, "{eduend}", v_eduend);
					v_msg = StringManager.replace(v_msg, "{tstep}", v_tstep);
					v_msg = StringManager.replace(v_msg, "{study_time}", v_study_time);
					
					mailForm = "";
					mailForm =  "<table width='720' border='0' cellspacing='0' cellpadding='0'>";
					mailForm += "<tr><td><img src='http://iedu.nise.go.kr/images/user/mail/free_top.gif'></td></tr>";
					mailForm += "<tr><td background='http://iedu.nise.go.kr/images/user/mail/free_bg.gif'>";
					mailForm += "<table width='100%' border='0' cellspacing='0' cellpadding='0'><tr>";
					mailForm += "<td width='7%'></td>";
					mailForm += "<td style='font-size:9pt;line-height:150%;color:#5c5c5c;padding:0 50 0 50;word-break:break-all;'>::content::</td>";
					mailForm += "<td width='7%'></td>";
					mailForm += "</tr></table>";
					mailForm += "</td></tr>";
					mailForm += "<tr><td><img src='http://iedu.nise.go.kr/images/user/mail/free_bottom.gif'></td>";
					mailForm += "</tr></table>";
					       
					v_msg = StringManager.replace(mailForm, "::content::", v_msg); 
					
					mailing = Mailing.getInstance();
					v_msg = mailing.makeHtml(v_msg);
					
					isOk = mailing.send(smailServer, sFromEmail, sFromName, v_email, sSubject, v_msg);
					System.out.println("p_status============>"+isOk);
					//�����ͺ��̽� �̸��Ϸα׿� ������ �����ϱ� ���� box�� ������ �߰� �����Ѵ�.
					box.put("p_receiverName", v_name);// ������ �̸�
					box.put("p_userid", v_userid);//������ ���̵�
					box.put("p_email",v_email );//������ �̸���
					box.put("p_mailTitle", sSubject);//�̸��� ����
					box.put("p_mailContent", v_msg);//���� ����
					box.put("p_senderEmail", sFromEmail);// �߽��� �̸���
					box.put("p_senderName", sFromName); // �߽��� �̸�
					box.put("p_status", new Integer(isOk));// �߽� ��������
					box.put("p_tabseq", v_tabseq);
					emailLog.insertEmailLog(box);//�α׿� ���
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
		}
		System.out.println("send mail ok!!!!!!!!");
		return isOk;
	}
	
	
	public int QnaSendMail(RequestBox box) throws Exception {
		ConfigSet conf = new ConfigSet();
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;
		Mailing mailing = null;
		
		int isOk = 0;
		String sql = "";
		
		String smailServer  = conf.getProperty("mail.server");
		String sFromName  = box.getSession("name");  
		String sFromEmail = box.getSession("email");
		String sSubject   = box.getString("p_title");
		String lDate      = FormatDate.getDate("yyyy/MM/dd HH:mm:ss");
		String sBody      = box.getString("p_content").replace("\r\n","<br>");  
		String ToEmail= conf.getProperty("mail.admin.email");
		String v_schecks = "";
		String mailForm = "";
		
		Vector v_to = box.getVector("to");
		int temp[] = new int[v_to.size()];
		int k = 0;
		
		try {
			/* param ���� �Ѿ�� v_to �� �� ���� ���� */
			for(int j = 0; j < v_to.size(); j++){
			    if(v_to.get(j).equals("")){
			    	v_to.remove(j);
			    	--j;
				}
			}
					// ��ü���� ó��
					String v_msg = "�ۼ���: "+ sFromName+"<br>" ;
					v_msg += "�ۼ��ð�: "+ lDate+"<br>" ;
					v_msg += "����: "+ sSubject+"<br>" ;
					v_msg += "����: "+sBody+"<br>";
					
					mailForm = "";
					mailForm =  "<table width='720' border='0' cellspacing='0' cellpadding='0'>";
					mailForm += "<tr><td><img src='http://iedu.nise.go.kr/images/user/mail/free_top.gif'></td></tr>";
					mailForm += "<tr><td background='http://iedu.nise.go.kr/images/user/mail/free_bg.gif'>";
					mailForm += "<table width='100%' border='0' cellspacing='0' cellpadding='0'><tr>";
					mailForm += "<td width='7%'></td>";
					mailForm += "<td style='font-size:9pt;line-height:150%;color:#5c5c5c;padding:0 50 0 50;word-break:break-all;'>::content::</td>";
					mailForm += "<td width='7%'></td>";
					mailForm += "</tr></table>";
					mailForm += "</td></tr>";
					mailForm += "<tr><td><img src='http://iedu.nise.go.kr/images/user/mail/free_bottom.gif'></td>";
					mailForm += "</tr></table>";
					       
					v_msg = StringManager.replace(mailForm, "::content::", v_msg); 
					
					mailing = Mailing.getInstance();
					v_msg = mailing.makeHtml(v_msg);
					

					isOk += mailing.send(smailServer, sFromEmail, sFromName, ToEmail, "<QnA> �ű� ���� ��ϵǾ����ϴ�.", v_msg);
				
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
		}
		return isOk;
	}
	

    /**
    ������ �߼�-��������
    @param box      receive from the form object and session
    @return int
    */
    public int sendFormMail(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        ListSet             ls      = null;
        String              sql     = "";
        int cnt = 0;    //  ���Ϲ߼��� ������ �����
        // p_checks�� �Ѿ�� �ټ��� value�� ó���ϱ� ���� vector�� ����
        Vector v_check1     = new Vector();
        v_check1            = box.getVector("p_checks"); // userid,subj,year,subjseq

        Enumeration em1     = v_check1.elements();

        StringTokenizer st1 = null;
        String v_checks     = "";
        String v_subj       = "";
        String v_year       = "";
        String v_subjseq    = "";
        String v_userid     = "";
        String v_ismailing  = box.getString("p_isMailing");


        String v_touch = box.getString("p_touch");
        String v_msubjnm = box.getString("p_msubjnm");
        String v_mseqgrnm = box.getString("p_mseqgrnm");
        String v_msubj = box.getString("p_msubj");
        String v_myear = box.getString("p_myear");
        String v_msubjseq = box.getString("p_msubjseq");

        int isOk = 0;

        DataBox             dbox    = null;


        try { 
            connMgr = new DBConnectionManager();

//// //// //// //// ////  ������ �߼� //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 
            // String v_sendhtml = "mail3.htm";
            String v_sendhtml = "mail_jindopush.html";
            FormMail fmail = new FormMail(v_sendhtml);     //      �����Ϲ߼��� ���
            MailSet mset = new MailSet(box);        //      ���� ���� �� �߼�
            // String v_mailTitle = "�ȳ��ϼ���? HKMC ���������� �Դϴ�.(�������ȳ�) ";
            String v_mailTitle = v_msubjnm + "���� �н����� �ȳ��Դϴ�.";
//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

            while ( em1.hasMoreElements() ) { 
                v_checks    = (String)em1.nextElement();
                st1      = new StringTokenizer(v_checks,",");
                while ( st1.hasMoreElements() ) { 
                    v_userid    = (String)st1.nextToken();
                    v_subj      = (String)st1.nextToken();
                    v_year      = (String)st1.nextToken();
                    v_subjseq   = (String)st1.nextToken();
                    break;
                }
                // select subjnm,passday,tstep,gradstep,gradscore,name,ismailing,cono,email
                sql = "select  B.subjnm,A.tstep,B.gradstep,B.gradscore,B.gradreport,B.gradexam,B.gradhtest,B.gradftest,B.wstep,B.wmtest,B.wftest,B.whtest,B.wreport,D.name,D.ismailing,D.cono,D.email,\n";
                sql += " B.edustart,B.eduend, \n";
                sql += " (to_number(to_char(sysdate, 'YYYYMMDD')) - to_number(substr(B.edustart,1,8))) passday \n";
                sql += " from TZ_STUDENT A,tZ_SUBJSEQ B,TZ_MEMBER D \n";
                sql += " where A.userid = " +SQLString.Format(v_userid);
                sql += " and A.subj = " +SQLString.Format(v_subj);
                sql += " and A.year = " +SQLString.Format(v_year);
                sql += " and A.subjseq = " +SQLString.Format(v_subjseq);
                sql += " and A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq and A.userid=D.userid \n";
                sql += " group by B.subjnm,A.tstep,B.gradstep,B.gradscore,B.gradreport,B.gradexam,B.gradhtest,B.gradftest,B.wstep,B.wmtest,B.wftest,B.whtest,B.wreport,D.name,D.ismailing,D.cono,D.email,B.edustart,B.eduend \n";


                ls = connMgr.executeQuery(sql);
                //System.out.println("sql ==  ==  ==  == = > " +sql);

                while ( ls.next() ) { 
                    String v_toEmail =  ls.getString("email");
                    String v_toCono  =  ls.getString("cono");
                    String v_suryo = "";
                    // String v_ismailing= ls.getString("ismailing");
                    // String v_toEmail =  "jj1004@dreamwiz.com";
                    int wmtest  = Integer.parseInt( ls.getString("wmtest") );
                    int wftest  = Integer.parseInt( ls.getString("wftest") );
                    int whtest  = Integer.parseInt( ls.getString("whtest") );

                    v_suryo = "-������ (" +ls.getString("wstep") + "%): " +ls.getString("gradstep") + "% �̻�<br > ";
                    if ( whtest > 0) { 
                      v_suryo += "-������ (" +whtest + "%): " +ls.getString("gradhtest") + "�� �̻�<br > ";
                    }
                    if ( wmtest > 0) { 
                      v_suryo += "-�߰��� (" +wmtest + "%): " +ls.getString("gradexam") + "�� �̻�<br > ";
                    }
                    if ( wftest > 0) { 
                      v_suryo += "-������ (" +wftest + "%): " +ls.getString("gradftest") + "�� �̻�<br > ";
                    }
                    if ( !ls.getString("wreport").equals("0") ) { 
                      v_suryo += "-����Ʈ (" +ls.getString("wreport") + "%): " +ls.getString("gradreport") + "�� �̻�<br > ";
                    }
                    v_suryo += "-���� : " +ls.getString("gradscore") + "�� �̻�� ���ᰡ��<br > ";
                    v_suryo += "-�������� : " +ls.getString("gradscore") + "��";

                    mset.setSender(fmail);     //  ���Ϻ����� ��� ����

                    fmail.setVariable("tstep",     ls.getString("tstep") );
                    fmail.setVariable("subjnm",    ls.getString("subjnm") );
                    fmail.setVariable("passday",   ls.getString("passday") );
                    fmail.setVariable("tstep",     ls.getString("tstep") );
                    fmail.setVariable("gradstep",  v_suryo);
                    fmail.setVariable("gradscore", ls.getString("gradscore") );
                    fmail.setVariable("toname",    ls.getString("name") );
                    fmail.setVariable("edustart",  FormatDate.getFormatDate( ls.getString("edustart"), "yyyy.MM.dd") );
                    fmail.setVariable("eduend",    FormatDate.getFormatDate( ls.getString("eduend"), "yyyy.MM.dd") );

                    String v_mailContent = fmail.getNewMailContent();


                    boolean isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent,v_ismailing, v_sendhtml);
                    if ( isMailed) cnt++;     //      ���Ϲ߼ۿ� �����ϸ�

                    dbox = ls.getDataBox();
                    dbox.put("d_subj", v_msubj);
                    dbox.put("d_year", v_myear);
                    dbox.put("d_subjseq", v_msubjseq);
                    dbox.put("d_userid", v_userid);
                    dbox.put("d_touch", v_touch);
                    dbox.put("d_ismail", "1");
                    dbox.put("d_title",v_mailTitle);
                    if ( isMailed) { 
                      dbox.put("d_isok", "Y");
                    } else { 
                      dbox.put("d_isok", "N");
                    }
                    dbox.put("d_ismailopen", "N");
                    dbox.put("d_subjnm", v_msubjnm);
                    dbox.put("d_seqgrnm", v_mseqgrnm);


                }

                isOk = mset.insertHumanTouch(dbox);

            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return cnt;
    }	
}