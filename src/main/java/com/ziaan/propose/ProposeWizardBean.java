//**********************************************************
// 1. 제      목: 교육대상선정 마법사 관리
// 2. 프로그램명: ProposeWizardBean.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: 2004.03. 17
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.propose;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.system.ManagerAdminBean;

/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to Window >
 * Preferences > Java > Code Generation > Code and Comments
 */

public class ProposeWizardBean {
	/* 선정된 교육대상자 검색 */
	public ArrayList SelectedAcceptTargetMember(RequestBox box)
			throws Exception {
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ListSet ls = null;
		ArrayList list = new ArrayList();
		DataBox dbox = null;
		StringBuffer sbSQL = new StringBuffer("");

		int iSysAdd = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수

		ProposeBean probean = new ProposeBean();
		ManagerAdminBean bean = null;

		/*
		 * 검색.params : 본부, 부서, 사원구분(FIX_GUBUN), 입사일(시작~종료)(ENT_DATE),
		 * 승격일(시작~종료)-PMT_DATE, 직위(jikwi), 직급(jikup), 성별(sex), 호봉 (시작~
		 * 종료)-JIKHO, JIKHONM
		 */
		String ss_action = box.getString("p_action");
		String v_orderColumn = box.getString("p_orderColumn"); // 정렬할 컬럼명
		// String ss_subjcourse = box.getString("s_subjcourse"); // 정렬할 컬럼명
		String ss_subjseq = box.getString("s_subjseq"); // 정렬할 컬럼명

		String v_sql_add = "";
		String v_userid = box.getSession("userid");
		String s_gadmin = box.getSession("gadmin");
		String ss_grcode = box.getString("s_grcode");
		String ss_gyear = box.getString("s_gyear");
		String ss_subjcourse = box.getString("s_subjcourse");
		String ss_mastercd = box.getString("s_mastercd");
		String ss_company = box.getString("s_company");

		String p_order = box.getString("p_order");
		String v_orderType = box.getString("p_orderType"); // 정렬할 순서

		String v_subjnm = "";
		String v_subj = "";
		String v_subjseq = "";
		String v_subjseqgr = "";
		String v_year = "";
		String v_chkfinal = "";
		String v_appdate = "";
		String v_isproposeapproval = "";

		try {
			connMgr = new DBConnectionManager();

			
			sbSQL.append(" select                                 \n")
				 .append("     a.userid,                          \n")
				 .append("     a.subj,                            \n")
				 .append("     a.subjnm,                          \n")
				 .append("     a.subjseq,                         \n")
				 .append("     a.year,                            \n")
				 .append("     a.isproposeapproval,               \n")
				 .append("     b.comp,                            \n")
				 .append("     fn_crypt('2', b.birth_date, 'knise') birth_date,                           \n")
				 .append("     b.name,                            \n")
				 .append("     b.email,                           \n")
				 .append("     b.hometel,                         \n")
				 .append("     b.handphone                        \n")
				 .append(" from                                   \n")
				 .append("     tz_edutarget a,                    \n")
				 .append("     tz_member    b                     \n")
				 .append(" where                                  \n")
				 .append("     a.userid = b.userid                \n");

			if (!ss_mastercd.equals("ALL")) {
				sbSQL.append(" and a.mastercd = "
						+ StringManager.makeSQL(ss_mastercd) + " \n");
			}

			if (!ss_company.equals("ALL")) {
				sbSQL.append(" and b.comp = "
						+ StringManager.makeSQL(ss_company)
						+ " \n");
			}

			if (p_order.equals("email"))
				sbSQL.append(" order by b.email  " + v_orderType + " \n");
			if (p_order.equals("userid"))
				sbSQL.append(" order by b.userid " + v_orderType + " \n");
			if (p_order.equals("name"))
				sbSQL.append(" order by b.name   " + v_orderType + " \n");

			System.out.println(this.getClass().getName() + "."
					+ "SelectedAcceptTargetMember() Printing Order "
					+ ++iSysAdd + ". ======[SQL] : " + " [\n"
					+ sbSQL.toString() + "\n]");

			ls = connMgr.executeQuery(sbSQL.toString());

			sbSQL.setLength(0);

			sbSQL
					.append(
							" select  b.subj                                                       \n")
					.append(
							"     ,   b.subjseq                                                    \n")
					.append(
							"     ,   b.year                                                       \n")
					.append(
							"     ,   b.chkfinal                                                   \n")
					.append(
							"     ,   b.isproposeapproval                                          \n")
					.append(
							"     ,   a.scsubjnm                                                   \n")
					.append(
							"     ,   a.subjseqgr                                                  \n")
					.append(
							"     ,   b.appdate                                                    \n")
					.append(
							"     ,   (                                                            \n")
					.append(
							"             select  subjnm                                           \n")
					.append(
							"             from    tz_subjseq                                       \n")
					.append(
							"             where   a.scsubj        = subj                           \n")
					.append(
							"             and     a.scsubjseq     = subjseq                        \n")
					.append(
							"             and     a.scyear        = year                           \n")
					.append(
							"         )           subjnm                                           \n")
					.append(
							" from    vz_mastersubjseq    a                                        \n")
					.append(
							"     ,   tz_propose          b                                        \n")
					.append(
							" where   a.subj      = b.subj                                         \n")
					.append(
							" and     a.subjseq   = b.subjseq                                      \n")
					.append(
							" and     a.year      = b.year                                         \n")
					.append(
							" and     (                                                            \n")
					.append(
							"                 b.isproposeapproval = 'Y'                            \n")
					.append(
							"             or  b.isproposeapproval = 'L'                            \n")
					.append(
							"         )                                                            \n")
					.append(
							" and     a.mastercd = "
									+ StringManager.makeSQL(ss_mastercd)
									+ "       \n")
					.append(
							" and     b.userid= ?                                                  \n")
					.append(
							" order by b.appdate asc                                               \n");

			System.out.println(this.getClass().getName() + "."
					+ "SelectedAcceptTargetMember() Printing Order "
					+ ++iSysAdd + ". ======[SQL] : " + " [\n"
					+ sbSQL.toString() + "\n]");

			pstmt = connMgr.prepareStatement(sbSQL.toString());

			while (ls.next()) {
				pstmt.setString(1, ls.getString("userid"));

				rs = pstmt.executeQuery();

				v_subj = "";
				v_subjnm = "";
				v_subjseq = "";
				v_year = "";
				v_chkfinal = "";
				v_isproposeapproval = "";

				while (rs.next()) {
					v_isproposeapproval = rs.getString("isproposeapproval"); // 현업팀장
																				// 결재여부
					v_chkfinal = rs.getString("chkfinal"); // 최종승인 여부
					v_subj = rs.getString("subj");
					v_subjnm = rs.getString("subjnm");
					v_subjseq = rs.getString("subjseq");
					v_subjseqgr = rs.getString("subjseqgr");
					v_appdate = rs.getString("appdate");
					v_year = rs.getString("year");
				}

				dbox = ls.getDataBox();

				dbox.put("d_psubj", v_subj);
				dbox.put("d_psubjnm", v_subjnm);
				dbox.put("d_psubjseq", v_subjseq);
				dbox.put("d_psubjseqgr", v_subjseqgr);
				dbox.put("d_pyear", v_year);
				dbox.put("d_pchkfinal", v_chkfinal);
				dbox.put("d_appdate", v_appdate);
				dbox.put("d_pisproposeapproval", v_isproposeapproval);

				list.add(dbox);
			}
		} catch (SQLException e) {
			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString()
					+ "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch (Exception e) {
			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage()
					+ "\n]");
		} finally {
			if (ls != null) {
				try {
					ls.close();
				} catch (Exception e) {
				}
			}

			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}

			if (connMgr != null) {
				try {
					connMgr.freeConnection();
				} catch (Exception e) {
				}
			}
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		}

		return list;
	}

	/**
	 * 선정할 교육대상자 검색
	 * 
	 * @param box
	 *            receive from the form object and session
	 * @return ArrayList 선정할 교육대상자 리스트
	 */
	public ArrayList SelectAcceptTargetMember(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		PreparedStatement pstmt = null;
		StringBuffer sbSQL = new StringBuffer("");
		ResultSet rs = null;
		ListSet ls = null;
		ArrayList list = new ArrayList();
		DataBox dbox = null;
		ProposeBean probean = new ProposeBean();

		int iSysAdd = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수

		String v_process = box.getString("p_process");
		String v_action = box.getString("p_action");
		String v_grcode = box.getString("s_grcode");
		String v_gyear = box.getString("s_gyear");
		String v_grseq = box.getString("s_grseq");
		String v_subjcourse = box.getString("s_subjcourse");
		String v_subjseq = box.getString("s_subjseq");
		String v_mastercd = box.getString("s_mastercd");
		String v_name = box.getString("p_name");
		String v_userid = box.getString("p_userid");
		String v_compnm = box.getString("p_compnm");
		String ss_jikwi = box.getString("s_jikwi");
		String v_orderColumn = box.getString("p_orderColumn"); // 정렬할 컬럼명

		//String v_year = probean.getSubjYear(v_grcode, v_gyear, v_grseq, v_subjcourse, v_subjseq);
		ManagerAdminBean bean = null;

		String v_sql_add = "";
		String s_userid = box.getSession("userid");
		String s_gadmin = box.getSession("gadmin");

		String v_company = box.getStringDefault("s_company", "ALL");
		String v_jikwi = box.getStringDefault("s_jikwi", "ALL");

		String v_grcomp = box.getStringDefault("s_grcomp", "ALL");
		String v_jikun = box.getStringDefault("s_jikun", "ALL");
		String v_seltext = box.getStringDefault("s_seltext", "ALL"); // 본부/사업부
		String v_seldept = box.getStringDefault("s_seldept", "ALL"); // 부서별검색

		String sql1 = "";
		try {

			connMgr = new DBConnectionManager();

			sbSQL
					.append(" select                                                                         	\n")
					.append("     a.userid,                                                                  	\n")
					.append("     fn_crypt('2', a.birth_date, 'knise') birth_date,                                                    				  	\n")
					.append("     a.name,                                                                    	\n")
					.append("     a.email,                                                       			  	\n")
					.append("     a.hometel,                                                       		  		\n")
					.append("     a.handphone,                                                    			  	\n")
					.append("     a.comp,                                                        			  	\n")
					.append("     a.jikup,                                                        			  	\n")
					.append("     (select                                                                    	\n")
					.append("          'Y'                                                                   	\n")
					.append("      from                                                                      	\n")
					.append("          tz_edutarget                                                          	\n")
					.append("      where                                                                     	\n")
					.append("              userid   = a.userid                                               	\n")
					.append("          and mastercd = " + SQLString.Format(v_mastercd) + "                    	\n")
					.append("     )                                          istarget,                       	\n")
					.append("     (select                                                                    	\n")
					.append("          'Y'                                                                   	\n")
					.append("      from                                                                      	\n")
					.append("          tz_propose                                                            	\n")
					.append("      where                                                                     	\n")
					.append("              userid  = a.userid                                                	\n")
					.append("          and subj    = " + SQLString.Format(v_subjcourse) + "                   	\n")
					.append("          and subjseq = "+ SQLString.Format(v_subjseq) + "                      	\n")
					.append("          and year    = " + SQLString.Format(v_gyear)+ "                         	\n")
					.append("      )                                         ispropose                       	\n")
					.append(" from                                                                           	\n")
					.append("     (select                                                                    	\n")
					.append("          userid,                                                               	\n")
					.append("          birth_date,                                                 					\n")
					.append("          name,                                                                 	\n")
					.append("          email,                                                    				\n")
					.append("          hometel,                                                    			\n")
					.append("          handphone,                                                 				\n")
					.append("          comp,                                                     				\n")
					.append("          post_nm as jikup                                                      	\n")
					.append("      from                                                                      	\n")
					.append("          tz_member                                                             	\n")
					.append("      where                                                                     	\n")
					.append("              1 = 1                                                             	\n");

			if (!v_userid.trim().equals(""))
				sbSQL.append("        and userid like "	+ SQLString.Format("%"+v_userid+"%") + "                     	\n");

			if (!v_name.trim().equals(""))
				sbSQL.append("        and name   like " + SQLString.Format("%"+v_name+"%") + "               				\n");

			sbSQL
					.append("     ) a                                                                        	\n")
					.append(" where                                                                          	\n")
					.append("         1 = 1                                                                  	\n")
					.append("     and a.comp      in (select                                                	\n")
					.append("                             comp                                               	\n")
					.append("                         from                                                   	\n")
					.append("                             tz_grcomp                                           	\n")
					.append("                         where                                                  	\n")
					.append("                                 grcode = " + SQLString.Format(v_grcode) + "       \n")
					.append("                        )                                                       	\n");

			// if ( !v_company.equals("ALL") )
			// sbSQL.append(" and substr(a.comp, 0, 4) = '"
			// +StringManager.substring(v_company, 0,4) + "' \n");

			ls = connMgr.executeQuery(sbSQL.toString());

			while (ls.next()) {
				dbox = ls.getDataBox();

				list.add(dbox);
			}
		} catch (SQLException e) {
			ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
			throw new Exception("\n SQL : [\n" + sbSQL.toString()
					+ "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
		} catch (Exception e) {
			ErrorManager.getErrorStackTrace(e, box, "");
			throw new Exception("\n e.getMessage() : [\n" + e.getMessage()
					+ "\n]");
		} finally {
			if (ls != null) {
				try {
					ls.close();
				} catch (Exception e) {
				}
			}

			if (connMgr != null) {
				try {
					connMgr.freeConnection();
				} catch (Exception e) {
				}
			}
		}

		return list;
	}

	/* 선정된 교육대상자 삭제처리 */
	public int SelectedDeleteTargetMember(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		String sql1 = "";
		ProposeBean probean = new ProposeBean();
		int isOk = 1;

		/* 교육 및 과목 기수 정보 selected Params */
		String v_grcode = box.getString("s_grcode"); // 교육그룹
		String v_gyear = box.getString("s_gyear"); // 년도
		String v_grseq = box.getString("s_grseq"); // 교육기수
		String v_subjcourse = box.getString("s_subjcourse");
		String v_mastercd = box.getString("s_mastercd");
		String v_subjseq = box.getString("s_subjseq");
		// String v_year = probean.getSubjYear(v_grcode, v_gyear, v_grseq,
		// v_subj, v_subjseq);
		// if ( v_year.equals("")) v_year = v_gyear;

		// p_subj로 넘어온 다수의 value를 처리하기 위해 vector로 구현
		String v_userid = "";
		Vector v_checks = box.getVector("p_checks");
		Enumeration em = v_checks.elements();
		Hashtable deleteData = new Hashtable();
		String v_luserid = box.getSession("userid");

		try {
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			while (em.hasMoreElements()) {
				v_userid = (String) em.nextElement();

				deleteData.clear();
				deleteData.put("grcode", v_grcode);
				deleteData.put("mastercd", v_mastercd);
				// insertData.put("subjnm", v_subjnm);
				deleteData.put("gyear", v_gyear);
				deleteData.put("subjseq", v_subjseq);
				deleteData.put("userid", v_userid);
				deleteData.put("luserid", v_luserid);
				isOk = probean.deleteEduTarget(deleteData);
			}

			if (isOk > 0) {
				connMgr.commit();
			} else {
				connMgr.rollback();
			}

		} catch (Exception ex) {
			isOk = 0;
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
		} finally {
			if (connMgr != null) {
				try {
					connMgr.setAutoCommit(true);
				} catch (Exception e10) {
				}
			}
			if (connMgr != null) {
				try {
					connMgr.freeConnection();
				} catch (Exception e10) {
				}
			}
		}

		return isOk;
	}

	/* 선정된 교육대상자 입과처리 */
	public int AcceptTargetMember(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		String sql1 = "";
		ProposeBean probean = new ProposeBean();
		int isOk = 1;

		/* 교육 및 과목 기수 정보 selected Params */
		String v_grcode = box.getString("s_grcode"); // 교육그룹
		String v_gyear = box.getString("s_gyear"); // 년도
		String v_grseq = box.getString("s_grseq"); // 교육기수
		String v_subj = box.getString("s_subjcourse");
		String v_mastercd = box.getString("s_mastercd");
		String v_subjseq = box.getString("s_subjseq");
		String v_subjnm = box.getString("s_subjnm");
		String v_year = probean.getSubjYear(v_grcode, v_gyear, v_grseq, v_subj,
				v_subjseq);
		if (v_year.equals(""))
			v_year = v_gyear;

		// p_subj로 넘어온 다수의 value를 처리하기 위해 vector로 구현
		String v_userid = "";
		Vector v_checks = box.getVector("p_checks");
		Enumeration em = v_checks.elements();
		Hashtable insertData = new Hashtable();
		String v_isproposeapproval = "";
		String v_luserid = box.getSession("userid");

		try {
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			while (em.hasMoreElements()) {
				v_userid = (String) em.nextElement();

				insertData.clear();
				insertData.put("grcode", v_grcode);
				insertData.put("subjcourse", v_subj);
				insertData.put("mastercd", v_mastercd);
				insertData.put("subjnm", v_subjnm);
				insertData.put("gyear", v_year);
				insertData.put("subjseq", v_subjseq);
				insertData.put("isproposeapproval", v_isproposeapproval);
				insertData.put("userid", v_userid);
				insertData.put("luserid", v_luserid);
				isOk = probean.insertEduTarget(insertData);

			}

			if (isOk > 0) {
				connMgr.commit();
			} else {
				connMgr.rollback();
			}

		} catch (Exception ex) {
			isOk = 0;
			connMgr.rollback();
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
		} finally {
			if (connMgr != null) {
				try {
					connMgr.setAutoCommit(true);
				} catch (Exception e10) {
				}
			}
			if (connMgr != null) {
				try {
					connMgr.freeConnection();
				} catch (Exception e10) {
				}
			}
		}

		return isOk;
	}

	/* 호봉 select list */
	public ArrayList getJikho(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		ArrayList list = null;
		String sql = "";
		DataBox dbox = null;

		try {

			connMgr = new DBConnectionManager();
			list = new ArrayList();

			sql = "select distinct jikho, jikhonm ";
			sql += "from   tz_member ";
			sql += "where  jikho is not null";
			sql += "order  by jikho";

			ls = connMgr.executeQuery(sql);

			while (ls.next()) {
				dbox = ls.getDataBox();
				list.add(dbox);
			}
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		} finally {
			if (ls != null) {
				try {
					ls.close();
				} catch (Exception e) {
				}
			}
			if (connMgr != null) {
				try {
					connMgr.freeConnection();
				} catch (Exception e10) {
				}
			}
		}

		return list;
	}

	public int edutargetCount(RequestBox box) throws Exception {
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		String sql1 = "";
		int db_cnt = 0;
		String ss_mastercd = box.getString("s_mastercd");

		try {
			sql1 = "select count(a.userid) cnt ";
			sql1 += "from   tz_edutarget a, tz_member b ";
			sql1 += "where  a.userid   = b.userid ";
			sql1 += "and    a.mastercd = " + StringManager.makeSQL(ss_mastercd)
					+ " ";

			connMgr = new DBConnectionManager();
			ls = connMgr.executeQuery(sql1);
			if (ls.next()) {
				db_cnt = ls.getInt(1);
			}
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql1);
			throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
		} finally {
			if (ls != null) {
				try {
					ls.close();
				} catch (Exception e) {
				}
			}
			if (connMgr != null) {
				try {
					connMgr.freeConnection();
				} catch (Exception e10) {
				}
			}
		}

		return db_cnt;
	}

}