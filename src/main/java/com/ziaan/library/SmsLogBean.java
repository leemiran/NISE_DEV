package com.ziaan.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Sms�� ���۳����� ��� ��ȸ �ϴ� ��
 * @author Seunghyeon
 *
 */
public class SmsLogBean {
	private ConfigSet conf;
	private int row;
	private int adminrow;
	
	
	public SmsLogBean(){
		try {
			conf = new ConfigSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		row = Integer.parseInt(conf.getProperty("page.bulletin.row")); // �� �����  �������� row ���� �����Ѵ�
																			
		adminrow = Integer.parseInt(conf.getProperty("page.bulletin.adminrow")); // �� ����� �������� row ���� �����Ѵ�
	}
	
	/**
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List getSMSLogList(RequestBox box)throws Exception{
		List logList=new ArrayList();
		DBConnectionManager connMgr=null;
		ListSet ls=null;
		String sql=null;
		DataBox dbox=null;
		int v_pageno = box.getInt("p_pageno");
		String v_startdt=FormatDate.getFormatDate(box.getString("p_startdt"), "yyyyMMdd") ;
		String v_enddt=FormatDate.getFormatDate(box.getString("p_enddt"),"yyyyMMdd");
		try {
			connMgr=new DBConnectionManager();
			sql="select tabseq,SND_MSG,to_char(to_date(WRT_DTTM,'yyyymmddHH24miss'),'yyyymmdd') WRT_DTTM,count(seq) cnt from arreo_sms_log                                        \n";
			if(!v_startdt.equals("")&&!v_enddt.equals("")){
				sql+="where to_char(to_date(WRT_DTTM,'yyyymmddHH24miss'),'yyyymmdd')  between "+StringManager.makeSQL(v_startdt)+" and "+StringManager.makeSQL(v_enddt)+"\n";
			}
			sql+="Group by  tabseq,SND_MSG,to_char(to_date(WRT_DTTM,'yyyymmddHH24miss'),'yyyymmdd')                                                                     \n";
			sql+= "  	order by tabseq desc                                                                            \n";
			ls=connMgr.executeQuery(sql);
			System.out.println("SmsLogBean.getSMSLogList");
			System.out.println("sql======>"+sql);
			ls.setPageSize(row); // �������� row ������ �����Ѵ�
			ls.setCurrentPage(v_pageno); // ������������ȣ�� �����Ѵ�.
			int totalpagecount = ls.getTotalPage(); // ��ü ������ ���� ��ȯ�Ѵ�
			int totalrowcount = ls.getTotalCount(); // ��ü row ���� ��ȯ�Ѵ�
			while(ls.next()){
				dbox=ls.getDataBox();
				dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum() + 1));
				dbox.put("d_totalpage", new Integer(totalpagecount));
				dbox.put("d_rowcount", new Integer(row));
				logList.add(dbox);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + e.getMessage());
		}finally{
			try {
				if(ls!=null)ls.close();
				if(connMgr!=null)connMgr.freeConnection();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return logList;
	}
	/**
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List getSMSDetailLogList(RequestBox box)throws Exception{
		List logList=new ArrayList();
		DBConnectionManager connMgr=null;
		ListSet ls=null;
		String sql=null;
		DataBox dbox=null;
		int v_pageno = box.getInt("p_pageno");
		int v_tabseq=box.getInt("p_tabseq");
		String v_smdMsg=box.getString("p_smdMsg");
		String v_searchtext = "";
		String v_search = "";
		String p_RSLT_VAL=null;
		String v_CMP_MSG_ID=null;
		try {
			connMgr=new DBConnectionManager();
			sql="SELECT SEQ,TABSEQ,USERID,SND_PHN_ID,RCV_PHN_ID,SND_MSG,WRT_DTTM,SND_DTTM,RSLT_VAL,CMP_MSG_ID,STATUS   \n";
			sql+="FROM arreo_sms_log      \n";
			sql+="WHERE tabseq="+v_tabseq   +"\n";
			sql+=" and SND_MSG like "+StringManager.makeSQL("%"+v_smdMsg+"%");
			
			v_searchtext = box.getString("p_searchtext");
			v_search = box.getString("p_search");
			if (!v_searchtext.equals("")) { // �˻�� ������
				if (v_search.equals("receiverPhoneNumber")) { // ������ �̸����� �˻��Ҷ�
					sql+=" and RCV_PHN_ID like ";
					sql+=StringManager.makeSQL("%" + v_searchtext + "%");
				}else if(v_search.equals("userId")){//������ �̸��Ϸ� �˻��� ��
					sql+=" and USERID like ";
					sql+=StringManager.makeSQL("%" + v_searchtext + "%");
				}
			}
			
			System.out.println("SmsLogBean.getSMSDetailLogList");
			System.out.println("seq======================>"+sql);
			ls=connMgr.executeQuery(sql);
			ls.setPageSize(row); // �������� row ������ �����Ѵ�
			ls.setCurrentPage(v_pageno); // ������������ȣ�� �����Ѵ�.
			int totalpagecount = ls.getTotalPage(); // ��ü ������ ���� ��ȯ�Ѵ�
			int totalrowcount = ls.getTotalCount(); // ��ü row ���� ��ȯ�Ѵ�
			
			
			
			while(ls.next()){
				p_RSLT_VAL=ls.getString("RSLT_VAL");
				v_CMP_MSG_ID=ls.getString("CMP_MSG_ID");
				
				// SMS���� ���¿� ���� ���� ������Ʈ ���ش�. 
				// insert ���Ŀ� ���� �������� ������ ���°��� 99�� �������Ƿ� �󼼸���Ʈ�� ��ȸ�� �� ���ۻ��¸� �˻��Ͽ� update �Ѵ�.
				if(p_RSLT_VAL.equals("99")){
					updateRSLT_VAL(v_CMP_MSG_ID);
				}
				dbox=ls.getDataBox();
				dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum() + 1));
				dbox.put("d_totalpage", new Integer(totalpagecount));
				dbox.put("d_rowcount", new Integer(row));
				logList.add(dbox);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + e.getMessage());
		}finally{
			try {
				if(ls!=null)ls.close();
				if(connMgr!=null)connMgr.freeConnection();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return logList;
	}
	/**
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public DataBox retrieveSMSLogList(RequestBox box)throws Exception{
		DBConnectionManager connMgr=null;
		ListSet ls=null;
		String sql=null;
		DataBox dbox=null;
		int v_seq=box.getInt("p_seq");
		try {
			connMgr=new DBConnectionManager();
			sql="SELECT * FROM arreo_sms_log WHERE seq="+v_seq;
			ls=connMgr.executeQuery(sql);
			while(ls.next()){
				dbox=ls.getDataBox();
			}
		} catch (Exception e) {
			
		}
		return dbox;
	}
	
	/**
	 * sms data�� insert �� �� �α׸� ����Ѵ�.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int insertSmsLog(RequestBox box)throws Exception{
		DBConnectionManager connMgr=null;
		PreparedStatement pstmt=null;
		StringBuffer sql=new StringBuffer();
		int v_seq=getMaxSeq();
		int v_tabseq=box.getInt("p_tabseq");//�׷�seq
		String v_userid=box.getString("p_userid");//����� ID
		String v_sendPhoneId=box.getString("p_sendPhoneId");//������ ��� ��ȭ��ȣ
		String v_receiverPhoneId=box.getString("p_receiverPhoneId");//�޴� ��� ��ȭ��ȣ
		String v_sendMessage=box.getString("p_sendMessage");//������ �޽���
		String v_sendDateTime=box.getString("p_sendDateTime");//������ ��¥ �ð�
		String v_status=box.getString("p_status");//���ۻ���
		String v_CMP_MSG_ID=box.getString("p_CMP_MSG_ID");//sms ���� Ű
		String v_writeDateTime=box.getString("p_writeDateTime");// �ۼ��ð�
		String v_RSLT_VAL=getRSLT_VAL(v_CMP_MSG_ID);
		//�Ķ���� �� Ȯ��
		System.out.println("SmsLogBean.insertSmsLog");
		System.out.println("v_tabseq==========>"+v_tabseq);
		System.out.println("v_userid==========>"+v_userid);
		System.out.println("v_sendPhoneId==========>"+v_sendPhoneId);
		System.out.println("v_receiverPhoneId==========>"+v_receiverPhoneId);
		System.out.println("v_sendMessage==========>"+v_sendMessage);
		System.out.println("v_sendDateTime==========>"+v_sendDateTime);
		System.out.println("p_CMP_MSG_ID==========>"+v_CMP_MSG_ID);
		System.out.println("v_RSLT_VAL==========>"+v_RSLT_VAL);
		int isOk=0;
		try {
			connMgr =new DBConnectionManager();
			connMgr.setAutoCommit(false);
			sql.append("INSERT INTO arreo_sms_log(     \n");
			sql.append("SEQ,              \n");//�⺻Ű
			sql.append("TABSEQ,          \n");//�׷�Ű
			sql.append("USERID,          \n");//�������� ���̵�
			sql.append("SND_PHN_ID,       \n");// ������ �� ��ȭ��ȣ
			sql.append("RCV_PHN_ID,        \n");// �޴��� ��ȭ��ȣ
			sql.append("SND_MSG,          \n");// ������ �޽���
			sql.append("WRT_DTTM,         \n");// �� ��¥ �ð�
			sql.append("SND_DTTM,        \n");// ���۵� ��¥ �ð�
			sql.append("CMP_MSG_ID,      \n");// arreo_sms ���̺��� �⺻Ű
			sql.append("RSLT_VAL,      \n"); // ���� ����
			sql.append("STATUS)           \n");// arreo_sms�� insert ���� ����
			
			//sql.append("VALUES(arreo_sms_log_seq.nextval,\n");//SEQ
			sql.append("VALUES(?,\n");//SEQ
			sql.append("?, \n");//TABSEQ
			sql.append("?, \n");//USERID
			sql.append("?, \n");//SND_PHN_ID
			sql.append("?, \n");//RCV_PHN_ID
			sql.append("?, \n");//SND_MSG
			sql.append("?,  \n");//WRT_DTTM
			sql.append("?,     \n");//SND_DTTM
			sql.append("?,     \n");//CMP_MSG_ID
			sql.append("?,     \n");//RSLT_VAL
			sql.append("?     \n");//STATUS
			sql.append(")  \n");
			pstmt=connMgr.prepareStatement(sql.toString());
			int idx = 1; 
			pstmt.setInt(idx++, v_seq);//SEQ
			pstmt.setInt(idx++, v_tabseq);//TABSEQ
			pstmt.setString(idx++,v_userid );//USERID
			pstmt.setString(idx++,v_sendPhoneId );//SND_PHN_ID
			pstmt.setString(idx++,v_receiverPhoneId );//RCV_PHN_ID
			pstmt.setString(idx++,v_sendMessage );//SND_MSG
			pstmt.setString(idx++,v_writeDateTime );//WRT_DTTM
			pstmt.setString(idx++,v_sendDateTime );//WRT_DTTM
			pstmt.setString(idx++,v_CMP_MSG_ID);//CMP_MSG_ID
			pstmt.setString(idx++,v_RSLT_VAL);//RSLT_VAL
			pstmt.setString(idx++,v_status );//STATUS
			isOk=pstmt.executeUpdate();
			System.out.println("sql==============>"+sql.toString());
			if(isOk==1){
				connMgr.commit();
				System.out.println("Logging SMS Succeeded");
			}else{
				connMgr.rollback();
				System.out.println("Logging SMS Failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ErrorManager.getErrorStackTrace(e, box, sql.toString());
			throw new Exception("sql = " + sql.toString() + "\r\n" + e.getMessage());
		}finally{
			try{
				if(pstmt!=null)pstmt.close();
				if(connMgr!=null)connMgr.freeConnection();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isOk;
	}
	
	
	
	/**
	 *  sms�� ���� ����� sms �����ͺ��̽� �������� ������ �� ������Ʈ �Ѵ�.
	 * @param p_CMP_MSG_ID
	 * @param tabseq
	 * @throws Exception
	 */
	public void updateRSLT_VAL(String p_CMP_MSG_ID)throws Exception{
		DBConnectionManager connMgr=null;
		PreparedStatement pstmt=null;
		String sql=null;
		String p_RSLT_VAL=getRSLT_VAL(p_CMP_MSG_ID);
		int isOk=0;
		try {
			connMgr=new DBConnectionManager();
			connMgr.setAutoCommit(false);
			sql="UPDATE arreo_sms_log SET RSLT_VAL="+StringManager.makeSQL(p_RSLT_VAL);
			sql+="WHERE CMP_MSG_ID="+StringManager.makeSQL(p_CMP_MSG_ID);
			System.out.println();
			pstmt=connMgr.prepareStatement(sql);
			isOk=pstmt.executeUpdate();
			
			if(isOk==1){
				connMgr.commit();
				System.out.println("Update RSLT_VAL from arreo_sms_log Succeeded");
			}else{
				connMgr.rollback();
				System.out.println("Update RSLT_VAL from arreo_sms_log Failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("sql = " + sql.toString() + "\r\n" + e.getMessage());
		}finally{
			try{
				if(pstmt!=null)pstmt.close();
				if(connMgr!=null)connMgr.freeConnection();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * sms�� ���۰����  sms �����ͺ��̽� �������� �����´�.
	 * @param p_CMP_MSG_ID
	 * @return
	 * @throws Exception
	 */
	public String getRSLT_VAL(String p_CMP_MSG_ID)throws Exception{
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String url="jdbc:oracle:thin:@172.16.1.51:1521:akis01";
		String user="arreo_sms";
		String password="akissms";
		String sql=null;
		String p_RSLT_VAL=null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn=DriverManager.getConnection(url, user, password);
			sql="select RSLT_VAL FROM arreo_sms where CMP_MSG_ID="+StringManager.makeSQL(p_CMP_MSG_ID);
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while (rs.next()) {
				p_RSLT_VAL=rs.getString("RSLT_VAL");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("sql = " + sql.toString() + "\r\n" + e.getMessage());
		}finally{
			try {
				if(rs!=null)rs.close();
				if(pstmt!=null)pstmt.close();
				if(conn!=null)conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return p_RSLT_VAL;
	}
	
	
	
	
	/**
	 * Tabseq�� �ִ밪�� �����ͼ� +1�� ���� ��ȯ�Ѵ�.
	 * @return
	 * @throws Exception
	 */
	public int getMaxTabseq() throws Exception {
		DBConnectionManager connMgr=null;
		ListSet ls=null;
		String sql=null;
		int v_tabseq=0;
		try{
			 connMgr = new DBConnectionManager();
			//connMgr.setAutoCommit(false);
			sql="select nvl(max(tabseq),0)+1 max_tabseq from arreo_sms_log";
			ls=connMgr.executeQuery(sql);
			while (ls.next()) {
				v_tabseq=Integer.parseInt(ls.getString("max_tabseq"));
			}
			System.out.println("SmsLogBean.getMaxTabseq");
			System.out.println("v_tabseq==========>"+v_tabseq);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("sql:"+sql);
			throw new Exception("sql = " + sql + "\r\n" + e.getMessage());
		}finally{
			try{
				if(ls!=null)ls.close();
				if(connMgr!=null)connMgr.freeConnection();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return v_tabseq;
	}
	
	public int getMaxSeq() throws Exception {
		DBConnectionManager connMgr=null;
		ListSet ls=null;
		String sql=null;
		int v_seq=0;
		try{
			 connMgr = new DBConnectionManager();
			//connMgr.setAutoCommit(false);
			sql="select nvl(max(seq),0)+1 max_seq from arreo_sms_log";
			ls=connMgr.executeQuery(sql);
			while (ls.next()) {
				v_seq=Integer.parseInt(ls.getString("max_seq"));
			}
			System.out.println("SmsLogBean.getMaxSeq");
			System.out.println("v_seq==========>"+v_seq);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("sql:"+sql);
			throw new Exception("sql = " + sql + "\r\n" + e.getMessage());
		}finally{
			try{
				if(ls!=null)ls.close();
				if(connMgr!=null)connMgr.freeConnection();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return v_seq;
	}
}
