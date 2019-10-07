package com.ziaan.propose.email.log.impl;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;
import com.ziaan.propose.email.log.EmailLog;

/**
 * EmailLog 인터페이스를 구현한 빈
 * @author Seunghyeon
 *
 */
public class EmailLogImplBean implements EmailLog{
	private ConfigSet conf;
	private int row;
	private int adminrow;
	
	public EmailLogImplBean() {
		try {
			conf = new ConfigSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		row = Integer.parseInt(conf.getProperty("page.bulletin.row")); // 이 모듈의  페이지당 row 수를 셋팅한다
																			
		adminrow = Integer.parseInt(conf.getProperty("page.bulletin.adminrow")); // 이 모듈의 페이지당 row 수를 셋팅한다
	}
	
	public List getEmailAllLogList(RequestBox box) throws Exception {
		return null;
	}

	public List getEmailLogListGroupByTabSeq(RequestBox box) throws Exception {
		DBConnectionManager connMgr=null;
		List logList=null;
		String sql = "";
		DataBox dbox = null;
		ListSet ls = null;
		
		String v_startdt=box.getString("p_startdt");
		String v_enddt=box.getString("p_enddt");
		int v_pageno = box.getInt("p_pageno");
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
//		String v_searchtext = box.getString("p_searchtext");
//		String v_date=box.getString("p_date");
		try {
			connMgr = new DBConnectionManager();
			logList = new ArrayList();
			sql=  "   select tabseq, email_title,TO_CHAR(SEND_DATE,'yyyy.mm.dd') sendDate,count(seq) cnt  from tz_email_log 	        \n";
			if(!v_startdt.equals("")&&!v_enddt.equals("")){
				v_startdt=FormatDate.getFormatDate(v_startdt, "yyyy.MM.dd");
				v_enddt=FormatDate.getFormatDate(v_enddt, "yyyy.MM.dd");
				sql+="where TO_CHAR(SEND_DATE,'yyyy.mm.dd')  between "+StringManager.makeSQL(v_startdt)+" and "+StringManager.makeSQL(v_enddt)+"\n";
			}
			sql+= "  	group by tabseq, email_title,TO_CHAR(SEND_DATE,'yyyy.mm.dd')                                    \n";
			sql+= "  	order by tabseq desc                                                                            \n";
			System.out.println("EmailLogImplBean.getEmailLogListGroupByTabSeq");
			System.out.println("sql=============> "+sql);
			ls=connMgr.executeQuery(sql);
			ls.setPageSize(row); // 페이지당 row 갯수를 세팅한다
			ls.setCurrentPage(v_pageno); // 현재페이지번호를 세팅한다.
			int totalpagecount = ls.getTotalPage(); // 전체 페이지 수를 반환한다
			int totalrowcount = ls.getTotalCount(); // 전체 row 수를 반환한다
			
			while(ls.next()){
				dbox = ls.getDataBox();
				dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum() + 1));
				dbox.put("d_totalpage", new Integer(totalpagecount));
				dbox.put("d_rowcount", new Integer(row));
				logList.add(dbox);
			}
			
		} catch (Exception e) {
			ErrorManager.getErrorStackTrace(e, box, sql);
		} finally {
			if (ls != null) {
				try {
					ls.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connMgr != null) {
				try {
					connMgr.freeConnection();
				} catch (Exception e10) {
					e10.printStackTrace();
				}
			}
		}

		return logList;
	}

	public List getSelectedEmailLogList(RequestBox box) throws Exception {
		DBConnectionManager connMgr=null;
		List detailList=null;
		String sql = null;
		
		DataBox dbox = null;
		ListSet ls = null;
		int v_tabseq=box.getInt("p_tabseq");
		int v_pageno = box.getInt("p_pageno");
		String v_searchtext = box.getString("p_searchtext");
		String v_search = box.getString("p_search");
		String v_title=box.getString("p_title");
		try {
			connMgr = new DBConnectionManager();
			detailList = new ArrayList();
			sql="  select SEQ,TABSEQ,SUBJ,YEAR,SUBJSEQ,SENDER_NAME,SENDER_EMAIL,                                  \n";
			sql+="RECEIVER_NAME,RECEIVER_EMAIL,RECEIVER_USERID,EMAIL_TITLE,                 \n" ;
			sql+="EMAIL_CONTENTS,STATUS,                         \n" ;
			sql+="to_char(send_date,'yyyy.MM.dd HH24:mi') senddate from tz_email_log ";
			sql+=" where tabseq=";
			sql+=v_tabseq +" and email_title like "+StringManager.makeSQL("%"+v_title+"%")+" ";
			if (!v_searchtext.equals("")) { // 검색어가 있으면
				if (v_search.equals("name")) { // 수신자 이름으로 검색할때
					sql+="and receiver_name like ";
					sql+=StringManager.makeSQL("%" + v_searchtext + "%");
				}else if(v_search.equals("email")){//수신자 이메일로 검색할 때
					sql+="and receiver_email like ";
					sql+=StringManager.makeSQL("%" + v_searchtext + "%");
				}else if(v_search.equals("title")){//제목으로 검색할 때
					sql+="and  email_title like ";
					sql+=StringManager.makeSQL("%" + v_searchtext + "%");
				}else if(v_search.equals("content")){//내용으로 검색할 때
					sql+="and  DBMS_LOB.INSTR(email_contents, ";
					sql+=StringManager.makeSQL(v_searchtext);
					sql+=",1,1) <> 0";
				}
			}
			sql+=" order by seq desc                                                                        \n";
			System.out.println("EmailLogImplBean.getSelectedEmailLogList");
			System.out.println("sql=============> "+sql);
			ls=connMgr.executeQuery(sql);
			ls.setPageSize(row); // 페이지당 row 갯수를 세팅한다
			ls.setCurrentPage(v_pageno); // 현재페이지번호를 세팅한다.
			int totalpagecount = ls.getTotalPage(); // 전체 페이지 수를 반환한다
			int totalrowcount = ls.getTotalCount(); // 전체 row 수를 반환한다
			
			while(ls.next()){
				dbox = ls.getDataBox();
				dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum() + 1));
				dbox.put("d_totalpage", new Integer(totalpagecount));
				dbox.put("d_rowcount", new Integer(row));
				detailList.add(dbox);
				
			}
		} catch (Exception e) {
			ErrorManager.getErrorStackTrace(e, box, sql);
		}finally {
			if (ls != null) {
				try {
					ls.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (connMgr != null) {
				try {
					connMgr.freeConnection();
				} catch (Exception e10) {
					e10.printStackTrace();
				}
			}
		}
		
		return detailList;
	}

	public int insertEmailLog(RequestBox box) throws Exception {
		DBConnectionManager connMgr=null;
		PreparedStatement pstmt=null;
		String sql1=null;
		int isOk=0;
		int v_seq=getMaxSeq();
		int    v_tabseq=box.getInt("p_tabseq");//발송 그룹
		String v_subj=box.getString("p_subj");//과목 코드
		String v_subjseq=box.getString("p_subjseq");//기수
		String v_year =box.getString("p_year");//년도
		String v_senderName=box.getString("p_senderName");// 발신자 이름
		String v_senderEmail=box.getString("p_senderEmail");// 발신자 이메일
		String v_receiverName=box.getString("p_receiverName");// 수신자 이름
		String v_receiverEmail=box.getString("p_email");// 수신자 이메일
		String v_receiverUserId=box.getString("p_userid");// 수신자 아이디
		String v_emailTitle=box.getString("p_mailTitle");// 이메일 제목
		String v_emailContents=box.getString("p_mailContent");// 이메일 내용
		String v_status=box.getString("p_status");// 발신상태 1: 성공  0: 실패
		
		//변수 값 확인
		System.out.println("EmailLogImplBean.insertEmailLog");
		System.out.println("v_tabseq==============>"+v_tabseq);
		System.out.println("v_subj================>"+v_subj);
		System.out.println("v_subjseq=============>"+v_subjseq);
		System.out.println("v_year================>"+v_year);
		System.out.println("v_senderName==========>"+v_senderName);
		System.out.println("v_senderEmail=========>"+v_senderEmail);
		System.out.println("v_receiverName========>"+v_receiverName);
		System.out.println("v_receiverEmail=======>"+v_receiverEmail);
		System.out.println("v_receiverUserId======>"+v_receiverUserId);
		System.out.println("v_emailTitle==========>"+v_emailTitle);
		System.out.println("v_emailContents=======>"+v_emailContents);
		System.out.println("v_status==============>"+v_status);

		
		
		try{
			connMgr =new DBConnectionManager();
			connMgr.setAutoCommit(false);
			
			///////////////받아온 정보를 tz_email_log 테이블에 저장한다.//////////////////
			sql1="INSERT INTO tz_email_log(SEQ,TABSEQ,SUBJ,YEAR,SUBJSEQ,                               \n";
			sql1+="						   SENDER_NAME,SENDER_EMAIL,RECEIVER_NAME,                     \n";
			sql1+="						   RECEIVER_EMAIL,RECEIVER_USERID,EMAIL_TITLE,EMAIL_CONTENTS,  \n" ;
			sql1+="						   STATUS,SEND_DATE)                                           \n";
			sql1+="values(tz_email_log_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)        \n";
//			sql1+="values(?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)        \n";
			
			pstmt=connMgr.prepareStatement(sql1);
			
			int i=1;
			//pstmt.setInt(i++, v_seq);
			pstmt.setInt(i++, v_tabseq);
			pstmt.setString(i++, v_subj);
			pstmt.setString(i++, v_year);
			pstmt.setString(i++, v_subjseq);
			pstmt.setString(i++, v_senderName);
			pstmt.setString(i++, v_senderEmail);
			pstmt.setString(i++, v_receiverName);
			pstmt.setString(i++, v_receiverEmail);
			pstmt.setString(i++, v_receiverUserId);
			pstmt.setString(i++, v_emailTitle);
			pstmt.setString(i++, v_emailContents);
			pstmt.setString(i++, v_status);
			
			isOk=pstmt.executeUpdate();
			
			if(isOk==1){
				connMgr.commit();
				System.out.println("Logging E-mail Succeeded");
			}else{
				connMgr.rollback();
				System.out.println("Logging E-mail Failed");
			}
			
			
		}catch (Exception e) {
			ErrorManager.getErrorStackTrace(e, box, sql1);
			throw new Exception("sql = " + sql1 + "\r\n" + e.getMessage());
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

	public DataBox retrieveLog(RequestBox box) throws Exception {
		DBConnectionManager connMgr=null;
		ListSet ls=null;
		String sql=null;
		int v_seq=box.getInt("p_seq");
		DataBox dbox=null;
		try{
			connMgr=new DBConnectionManager();
			sql="select SEQ,TABSEQ,SUBJ,YEAR,SUBJSEQ,SENDER_NAME,SENDER_EMAIL,             \n" ;
			sql+="RECEIVER_NAME,RECEIVER_EMAIL,RECEIVER_USERID,EMAIL_TITLE,                 \n" ;
			sql+="EMAIL_CONTENTS,STATUS,                         \n" ;
			sql+="to_char(send_date,'yyyy.MM.dd HH24:mi:ss.ff2') senddate,(SELECT DBTIMEZONE FROM dual) timezone from tz_email_log ";
			sql+="where seq="+v_seq;
			ls=connMgr.executeQuery(sql);
			System.out.println("EmailLogImplBean.retrieveLog");
			System.out.println("sql=============> "+sql);
			while(ls.next()){
				dbox=ls.getDataBox();
			}
		}catch (Exception e) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + e.getMessage());
		}
		return dbox;
	}

	public int getMaxTabseq() throws Exception {
		DBConnectionManager connMgr=null;
		ListSet ls=null;
		String sql=null;
		int v_tabseq=0;
		try{
			connMgr = new DBConnectionManager();
			//connMgr.setAutoCommit(false);
			sql="select nvl(max(tabseq),0)+1 max_tabseq from tz_email_log";
			ls=connMgr.executeQuery(sql);
			while (ls.next()) {
				v_tabseq=Integer.parseInt(ls.getString("max_tabseq"));
			}
			System.out.println("EmailLogImplBean.getMaxTabseq");
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
			sql="select nvl(max(tabseq),0)+1 max_seq from tz_email_log";
			ls=connMgr.executeQuery(sql);
			while (ls.next()) {
				v_seq=Integer.parseInt(ls.getString("max_seq"));
			}
			System.out.println("EmailLogImplBean.getMaxSeq");
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
