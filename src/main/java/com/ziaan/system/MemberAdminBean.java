// **********************************************************
//  1. 제      목: 회원관리(마일리지) 관리
//  2. 프로그램명 : MemberAdminBean.java
//  3. 개      요: 회원관리(마일리지) 관리
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 강성욱 2004. 11. 12
//  7. 수      정:
// *****************************************
package com.ziaan.system;

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

/**
 * 회원관리 관리(ADMIN)
 *
 * @date   : 2003. 7
 * @author : S.W Kang
 */
public class MemberAdminBean { 

    public MemberAdminBean() { }


	/**
    회원조회 리스트
    @param box          receive from the form object and session
    @return ArrayList   회원 리스트
    */
    public ArrayList searchMember(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        MemberData data = null;

        String v_search     = box.getString("p_gubun");
        String v_searchtext = box.getString("p_key1");
        int v_pageno = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "  select a.userid userid, fn_crypt('2', a.birth_date, 'knise') birth_date, fn_crypt('2', a.pwd, 'knise') pwd, a.name name, a.email email, a.birthday birthday, a.cono cono,     ";
            sql += "         a.authority authority, a.post1 post1, a.post2 post2, a.addr addr, a.hometel hometel, a.handphone handphone,  ";
            sql += "         a.comptel comptel, a.tel_line tel_line, a.comp comp, a.interest interest, a.recomid recomid,                 ";
            sql += "         a.ismailing ismailing, a.indate indate, a.lgcnt lgcnt, a.lglast lglast, a.lgip lgip, a.pwd_date pwd_date,    ";
            sql += "         a.old_pwd old_pwd, a.asgn asgn, a.asgnnm asgnnm, a.jikun jikun, a.jikunnm jikunnm, a.jikup jikup,            ";
            sql += "         a.jikupnm jikupnm, a.jikwi jikwi, get_jikwinm(a.jikwi,a.comp) jikwinm, a.jikmu1 jikmu1, a.jikmu2 jikmu2,     ";
            sql += "         a.jikmu3 jikmu3, a.jikmu4 jikmu4, a.jikmunm jikmunm, a.jikchek jikchek, a.jikcheknm jikcheknm,               ";
            sql += "         a.ent_date ent_date, a.grp_ent_date grp_ent_date, a.pmt_date pmt_date, a.old_cono old_cono,                  ";
            sql += "         a.cono_chg_date cono_chg_date, a.office_gbn office_gbn, a.office_gbnnm office_gbnnm,                         ";
            sql += "         a.retire_date retire_date, a.work_plc work_plc , a.work_plcnm work_plcnm, a.sex sex,                         ";
            sql += "         get_compnm(a.comp,2,4) compnm                                                                                ";
            sql += "    from TZ_MEMBER a                                                                                                  ";

            if ( !v_searchtext.equals("") ) {                            //    검색어가 있으면
                if ( v_search.equals("userid") ) {                        //    ID로 검색할때
                    sql += " where a.userid like   '%" + v_searchtext + "%'";
                } else if ( v_search.equals("cono") ) {                   //    사번으로 검색할때
                    sql += " where a.cono like " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("name") ) {                   //    이름으로 검색할때
                    sql += " where a.name like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
            }
            sql += "   order by a.comp asc, a.name asc                                                                                    ";

            ls = connMgr.executeQuery(sql);

            ls.setPageSize(10);                         //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                //     현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();   //     전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();   //     전체 row 수를 반환한다

            while ( ls.next() ) { 
                data = new MemberData();

                data.setUserid( ls.getString("userid") );
                data.setbirth_date( ls.getString("birth_date") );
                data.setPwd( ls.getString("pwd") );
                data.setName( ls.getString("name") );
                data.setEmail( ls.getString("email") );
                data.setBirthday( ls.getString("birthday") );
                data.setCono( ls.getString("cono") );
                data.setAuthority( ls.getString("authority") );
                data.setPost1( ls.getString("post1") );
                data.setPost2( ls.getString("post2") );
                data.setAddr( ls.getString("addr") );
                data.setHometel( ls.getString("hometel") );
                data.setHandphone( ls.getString("handphone") );
                data.setComptel( ls.getString("comptel") );
                data.setTel_line( ls.getString("tel_line") );
                data.setComp( ls.getString("comp") );
                data.setInterest( ls.getString("interest") );
                data.setRecomid( ls.getString("recomid") );
                data.setIsmailing( ls.getString("ismailing") );
                data.setIndate( ls.getString("indate") );
                data.setLglast( ls.getString("lglast") );
                data.setLgip( ls.getString("lgip") );
                data.setPwd_date( ls.getString("pwd_date") );
                data.setOld_pwd( ls.getString("old_pwd") );
                data.setAsgn( ls.getString("asgn") );
                data.setAsgnnm( ls.getString("asgnnm") );
                data.setJikun( ls.getString("jikun") );
                data.setJikunnm( ls.getString("jikunnm") );
                data.setJikup( ls.getString("jikup") );
                data.setJikupnm( ls.getString("jikupnm") );
                data.setJikwi( ls.getString("jikwi") );
                data.setJikwinm( ls.getString("jikwinm") );
                data.setJikmu1( ls.getString("jikmu1") );
                data.setJikmu2( ls.getString("jikmu2") );
                data.setJikmu3( ls.getString("jikmu3") );
                data.setJikmu4( ls.getString("jikmu4") );
                data.setJikmunm( ls.getString("jikmunm") );
                data.setJikchek( ls.getString("jikchek") );
                data.setJikcheknm( ls.getString("jikcheknm") );
                data.setEnt_date( ls.getString("ent_date") );
                data.setGrp_ent_date( ls.getString("grp_ent_date") );
                data.setPmt_date( ls.getString("pmt_date") );
                data.setOld_cono( ls.getString("old_cono") );
                data.setCono_chg_date( ls.getString("cono_chg_date") );
                data.setOffice_gbn( ls.getString("office_gbn") );
                data.setOffice_gbnnm( ls.getString("office_gbnnm") );
                data.setRetire_date( ls.getString("retire_date") );
                data.setWork_plc( ls.getString("work_plc") );
                data.setWork_plcnm( ls.getString("work_plcnm") );
                data.setSex( ls.getString("sex") );
                data.setCompnm( ls.getString("compnm") );
                data.setLgcnt( ls.getInt("lgcnt") );
                data.setDispnum(total_row_count - ls.getRowNum() + 1);
                data.setTotalPageCount(total_page_count);

                list.add(data);
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
    회원이름 조회
    @param box          receive from the form object and session
    @return result      USER NAME
    */
    public static String getUserName(String userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        String result = "";

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "  select name from TZ_MEMBER  ";
            sql += "   where userid = " + StringManager.makeSQL(userid) + "";

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = ls.getString("name");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }


    /**
    회원이름 조회
    @param box          receive from the form object and session
    @return result      USER NAME
    */
    public String getUserInfo(String userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        String result = "";
        String v_userid = userid;

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "  select email, name, comptel from TZ_MEMBER  ";
            sql += "   where userid = " + StringManager.makeSQL(v_userid) + "";

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = "<a href='mailto:" +ls.getString("email") + "' > " + ls.getString("name") + "</a >  (☎ : " + ls.getString("comptel") + ")";
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }
    
    
    /**
    회원이름 조회
    @param box          receive from the form object and session
    @return result      USER NAME
    */
    public String getUserInfo(String userid, String name) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        String result = "";
        String v_userid             = userid.trim();
        String v_name               = name.trim();

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "  select email, name, comptel from TZ_MEMBER  ";
            sql += "   where userid = " + StringManager.makeSQL(v_userid) + "";
            sql += "   and   name   = " + StringManager.makeSQL(v_name  ) + "";

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = "<a href='mailto:" +ls.getString("email") + "' > " + ls.getString("name") + "</a >  (☎ : " + ls.getString("comptel") + ")";
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }


    /**
    회원 등록
    @param box		receive from the form object and session
    @return int		정상등록여부(1 : 정상, 0 : 오류)
    */
	public Hashtable insertMember(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;
		String              sql     = "";
		int isOk = 1;

        boolean v_CreateConnManager = false;
        boolean v_CreatePreparedStatement = false;

        Hashtable outputdata = new Hashtable();
        outputdata.put("p_errorcode", "1");


		String v_comp				= box.getString("comp");
		String v_userid				= box.getString("userid");
		String v_name				= box.getString("name");
		String v_pwd				= box.getString("pwd");
		String v_birth_date				= box.getString("birth_date").trim() ;
		//String v_cono				= box.getString("cono") ;

		//String v_deptnam		= box.getString("deptnam");
		//String v_jikupnm		= box.getString("jikupnm");
		//String v_work_plcnm	= box.getString("work_plcnm");
		//String v_jikwi		= box.getString("jikwi");
		//String v_jikwinm		= box.getString("jikwinm");

		String v_post1			= box.getString("post1");
		String v_post2			= box.getString("post2");
		String v_addr			= box.getString("addr");
		String v_addr2			= box.getString("addr2");
		String v_email			= box.getString("email");
		String v_ismailling		= box.getString("ismailling");

	    //String v_comptel		= box.getString("comptel");
	    //String v_tel_line		= box.getString("tel_line");
		String v_handphone		= box.getString("handphone");
		String v_hometel		= box.getString("hometel");
		
		//2008.10.15 오충현 추가
		String v_post_nm		= box.getString("post_nm");
		String v_emp_gubun		= box.getString("emp_gubun");
		String v_lvlnm		= box.getString("lvlnm");
		String v_issms      = box.getString("issms");
    //String v_office_gbn	= box.getString("office_gbn");

		try { 

            connMgr = (DBConnectionManager)box.getObject("connMgr");

			if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }
			sql = "\n insert into tz_member "
				+ "\n        (comp, userid, pwd, name, birth_date, email, ismailling "
				+ "\n       , zip_cd, address, handphone, hometel "
				+ "\n       , position_nm, lgcnt, lgfail, ldate, indate, emp_gubun,lvl_nm,issms) "
				+ "\n values (?, ?, fn_crypt('1', ?, 'knise'), ?, fn_crypt('1', ?, 'knise'), ?, ? "
				+ "\n       , ?, ?, ?, ? "
				+ "\n       , ?, 0, 0, to_char(sysdate, 'YYYYMMDDHH24MISS'), to_char(sysdate, 'YYYYMMDDHH24MISS'), ?,?,?) ";

			pstmt = connMgr.prepareStatement(sql);
            v_CreatePreparedStatement = true;

            pstmt.setString(1, v_comp);
            pstmt.setString(2, v_userid);
  			pstmt.setString(3, v_pwd);
  			pstmt.setString(4, v_name);
  			pstmt.setString(5, v_birth_date);
  			pstmt.setString(6, v_email);
  			pstmt.setString(7, v_ismailling);
  			pstmt.setString(8, v_post1.trim()+"-"+v_post2.trim());
  			pstmt.setString(9, v_addr.trim()+" "+ v_addr2.trim());
  			pstmt.setString(10, v_handphone);
  			pstmt.setString(11, v_hometel);
  			pstmt.setString(12, v_post_nm);
  			pstmt.setString(13, v_emp_gubun);
  			pstmt.setString(14, v_lvlnm);
  			pstmt.setString(15, v_issms);
  			/*
			pstmt.setString(1, v_comp);
			pstmt.setString(2, v_userid);
			pstmt.setString(3, v_pwd);
			pstmt.setString(4, v_name);
			pstmt.setString(5, v_birth_date);
			pstmt.setString(6, v_cono);
			pstmt.setString(7, v_email);

			pstmt.setString(8, v_post1);
			pstmt.setString(9, v_post2);
			pstmt.setString(10, v_addr);
			pstmt.setString(11, v_addr2);
            pstmt.setString(12, v_comptel);
			pstmt.setString(13, v_tel_line);
			pstmt.setString(14, v_handphone);
			pstmt.setString(15, v_hometel);

            pstmt.setString(16, v_deptnam);
			pstmt.setString(17, v_jikupnm);
			pstmt.setString(18, v_jikwi);
			pstmt.setString(19, v_jikwi);
			pstmt.setString(20, v_comp);
			pstmt.setString(21, v_work_plcnm);
			pstmt.setString(22, v_office_gbn);
  			 */

			isOk = pstmt.executeUpdate();
			

		} catch( SQLException es) {
			outputdata.put("p_errorcode", "0");
			outputdata.put("p_exception", es);
			outputdata.put("p_exceState", es.getSQLState());
			
			System.out.println("=====> "+es.getSQLState()); 
			
		} catch ( Exception ex ) { 
			outputdata.put("p_errorcode", "0");
			outputdata.put("p_exception", ex);
//            ErrorManager.getErrorStackTrace(ex, null, sql);
			//          throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }

        return outputdata;
    }

    /**
    회원 삭제
    @param box		receive from the form object and session
    @return int		정상등록여부(1 : 정상, 0 : 오류)
    */
	public int deleteMemberTemp(RequestBox box) throws Exception { 
		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;
		String              sql     = "";
		int isOk = 1;

		try { 
			connMgr = new DBConnectionManager();
			connMgr.setAutoCommit(false);

			sql = "delete from \"TZ_MEMBERTEMP\" ";
			pstmt = connMgr.prepareStatement(sql);

			isOk = pstmt.executeUpdate();

			if ( isOk > 0) { 
				if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
			} else { 
				if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
			}

		}
		catch ( Exception ex ) { 
			if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return isOk;
	}

    
    /**
    회원 등록
    @param box		receive from the form object and session
    @return int		정상등록여부(1 : 정상, 0 : 오류)
    */
	public int insertNewMember(RequestBox box) throws Exception { 

        boolean v_CreateConnManager = false;
        boolean v_CreatePreparedStatement = false;

		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
		String              sql     = "";
		String sql1 = "";
		int isOk = 1;
		int isOk1 = 1;
		int	success = 0;
		String v_userid = "";

		String v_comp		= box.getString("comp");

		try { 

            connMgr = (DBConnectionManager)box.getObject("connMgr");

			if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

			// sql =  "select lower(userid) from \"TZ_MEMBERTEMP\" ";
			// sql += " MINUS ";
			// sql += "select lower(userid) from \"TZ_MEMBER\" ";
// 			sql += "select userid from \"TZ_MEMBER\" where comp = '" +v_comp + "' and office_gbn='Y'";
			
			// sql = "select userid from tz_membertemp a, tz_member b where a.userid not in (b.userid)";
			sql = "select a.userid from tz_membertemp a where a.userid not in (select userid from tz_member)";


            ls = connMgr.executeQuery(sql);

			while ( ls.next() ) { 
				
				v_userid = ls.getString("userid");
// 2008.11.12 오충현 수정 KT member table 수정 때문에 쿼리수정
//				sql1 = "insert into \"TZ_MEMBER\" ";
//				sql1 += " (                                                                    \n";
//                sql1 += " USERID        ,   birth_date         ,   PWD           ,   NAME          ,\n";
//                sql1 += " EMAIL         ,   CONO          ,   POST1         ,   POST2         ,\n";
//                sql1 += " ADDR          ,   HOMETEL       ,   HANDPHONE     ,   COMPTEL       ,\n";
//                sql1 += " TEL_LINE      ,   COMP          ,   INDATE        ,   LGCNT         ,\n";
//                sql1 += " LGLAST        ,   LGIP          ,   JIKUP         ,   JIKUPNM       ,\n";
//                sql1 += " JIKWI         ,   JIKWINM       ,   OFFICE_GBN    ,   OFFICE_GBNNM  ,\n";
//                sql1 += " WORK_PLC      ,   WORK_PLCNM    ,   DEPTCOD       ,   DEPTNAM       ,\n";
//                sql1 += " LDATE         ,   LGFIRST       ,   ISMAILING     ,   ADDR2         ,\n";
//                sql1 += " ADDTXT        ,   AESID         ,   VALIDATION    )       \n";
                // 아래values
//				sql1 += "select \n";
//				sql1 += " USERID        ,   birth_date         ,   PWD           ,   NAME          ,\n";
//                sql1 += " EMAIL         ,   CONO          ,   POST1         ,   POST2         ,\n";
//                sql1 += " ADDR          ,   HOMETEL       ,   HANDPHONE     ,   COMPTEL       ,\n";
//                sql1 += " TEL_LINE      ,   COMP          ,   INDATE        ,   LGCNT         ,\n";
//                sql1 += " LGLAST        ,   LGIP          ,   JIKUP         ,   JIKUPNM       ,\n";
//                sql1 += " JIKWI         ,   JIKWINM       ,   OFFICE_GBN    ,   'Y'           ,\n";
//                sql1 += " WORK_PLC      ,   WORK_PLCNM    ,   DEPTCOD       ,   DEPTNAM       ,\n";
//                sql1 += " LDATE         ,   LGFIRST       ,   ISMAILING     ,   ADDR2         ,\n";
//                sql1 += " ADDTXT        ,   AESID         ,   VALIDATION      \n";
//				sql1 += " from \"TZ_MEMBERTEMP\" where userid = '" +v_userid.toLowerCase() + "'";

				sql1= "\n insert into tz_member "
					+ "\n        ( "
					+ "\n         comp, userid, pwd, name "
					+ "\n       , birth_date, email, ismailling, zip_cd "
					+ "\n       , address, handphone, hometel, post, post_nm "
					+ "\n       , lgcnt, lgfail, ldate, indate, position_nm, lvl_nm "
					+ "\n       , issms "
					+ "\n        ) " 
					+ "\n select  comp, userid, pwd, name "
					+ "\n       , birth_date, email, ismailling, zip_cd "
					+ "\n       , address, handphone, hometel, post, get_postnm(post) as post_nm "
					+ "\n       , lgcnt, lgfail, ldate, indate, position_nm, lvl_nm "
					+ "\n       , issms "
					+ "\n from    tz_membertemp "
					+ "\n where   userid = '" +v_userid + "'";
				pstmt = connMgr.prepareStatement(sql1);
				v_CreatePreparedStatement = true;

				isOk1 = pstmt.executeUpdate();
				isOk	= isOk * isOk1;
				// if ( isOk1 == 1)
				success++;
			}

			isOk = isOk * success;


        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }

        return isOk;
    }


    /**
    회원 변경 (기존회원 : 회사관련정보 변경)
    @param box		receive from the form object and session
    @return int		정상등록여부(1 : 정상, 0 : 오류)
    */
	public int updateOldMember(RequestBox box) throws Exception { 

        boolean v_CreateConnManager = false;
        boolean v_CreatePreparedStatement = false;

		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
		String              sql     = "";
		String sql1 = "";
		int isOk = 1;
		int isOk1 = 1;
		int	success = 0;
		String v_userid = "";

		String v_comp		= box.getString("comp");

		try { 
            connMgr = (DBConnectionManager)box.getObject("connMgr");

			if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }
			
			// sql =  "select a.comp, a.userid, deptnam, work_plcnm, jikwi, jikupnm from TZ_MEMBER a, ";
			// sql +=  "( select lower(userid) from \"TZ_MEMBER\" where office_gbn='Y'";
			// sql += "  INTERSECT ";
			// sql += "  select lower(userid) from \"TZ_MEMBERTEMP\" ) b ";
			// sql += " where lower(a.userid) = lower(b.userid)";
			//2008.11.12 오충현 주석
			//sql =  "select \n";
			//sql += "  b.comp, b.userid, b.deptnam, b.work_plcnm, \n";
			//sql += "  b.jikwi, get_jikwinm(b.jikwi,b.comp) jikwinm, b.jikupnm, \n";
			//sql += "  b.email, b.birth_date, b.pwd,  b.post1, b.post2, \n";
			//sql += "  b.addr, b.addr2, b.hometel, b.handphone, b.comptel, b.office_gbn \n";
			//sql += "from TZ_MEMBER a, TZ_MEMBERTEMP b \n";
			//sql += "where a.userid=b.userid \n";
			//이건 내가 한 주석이 아니당..ㅋ            sql += "  and a.office_gbn='Y' \n";
			
			//2008.11.12 오충현 쿼리수정
			sql = "\n select b.comp, b.userid, b.name, b.dept_cd, b.email, fn_crypt('2', b.birth_date, 'knise') birth_date, fn_crypt('2', b.pwd, 'knise') pwd, b.zip_cd "
				+ "\n      , b.address, b.hometel, b.handphone, b.email, b.ismailling, b.post, get_postnm(b.post) as post_nm "
				+ "\n      , b.emp_gubun "
				+ "\n from   tz_member a, tz_membertemp b "
				+ "\n where  a.userid = b.userid ";
			
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
            	//2008.11.12 오충현 쿼리수정
				// sql1 =  "update \"TZ_MEMBER\" set ";
				// sql1 +=  "  deptnam = ?, \n";
				// sql1 +=  "  work_plcnm = ?, \n";
				// sql1 +=  "  jikwi = ?,  \n";
				// sql1 +=  "  jikwinm = get_jikwinm(?,?) , \n";
				// sql1 +=  "  jikupnm = ?, \n";
				// sql1 +=  "  ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
				// sql1 +=  "where \n";
				// sql1 +=  "  userid = ? ";
        // 
	      //    pstmt = connMgr.prepareStatement(sql1);
		    //    v_CreatePreparedStatement = true;
		    //    
		        v_userid = ls.getString("userid");
        // 
				// pstmt.setString(1, ls.getString("deptnam") );
				// pstmt.setString(2, ls.getString("work_plcnm") );
				// pstmt.setString(3, ls.getString("jikwi") );
				// pstmt.setString(4, ls.getString("jikwi") );
				// pstmt.setString(5, ls.getString("comp") );
				// pstmt.setString(6, ls.getString("jikupnm") );
				// pstmt.setString(7, v_userid.toLowerCase() );
        // 
				// isOk1 = pstmt.executeUpdate();
				// isOk	= isOk * isOk1;
				//// if ( isOk1 == 1)
				// success++;
				
				//sql1 	="update TZ_MEMBER set \n";
				//sql1 += " deptnam			= " + (( ls.getString("deptnam") 		 == null || ls.getString("deptnam") 		 == "") ? "deptnam"			: SQLString.Format( ls.getString("deptnam"))) + ",\n";
				//sql1 += " work_plcnm 	=	" + (( ls.getString("work_plcnm") 	 == null || ls.getString("work_plcnm") == "") ? "work_plcnm"	: SQLString.Format( ls.getString("work_plcnm"))) + ",\n";
				//sql1 += " jikwi				= " + (( ls.getString("jikwi")			 == null || ls.getString("jikwi") 		 == "") ? "jikwi"			: SQLString.Format( ls.getString("jikwi"))) + ",\n";
				//sql1 += " jikupnm			= " + (( ls.getString("jikupnm")			 == null || ls.getString("jikupnm") 		 == "") ? "jikupnm"			: SQLString.Format( ls.getString("jikupnm"))) + ",\n";
				//sql1 += " email             = " + (( ls.getString("email")			 == null || ls.getString("email") 		 == "") ? "email"			: SQLString.Format( ls.getString("email"))) + ",\n";
				//sql1 += " birth_date             = " + (( ls.getString("birth_date")			 == null || ls.getString("birth_date") 		 == "") ? "birth_date"			: SQLString.Format( ls.getString("birth_date"))) + ",\n";
				// sql1 += " pwd               = " + (( ls.getString("pwd")			   == null || ls.getString("pwd") 		   == "") ? "pwd"			    : SQLString.Format( ls.getString("pwd"))) + ",\n";
				//sql1 += " post1             = " + (( ls.getString("post1")			 == null || ls.getString("post1") 		 == "") ? "post1"			: SQLString.Format( ls.getString("post1"))) + ",\n";
				//sql1 += " post2             = " + (( ls.getString("post2")			 == null || ls.getString("post2") 		 == "") ? "post2"			: SQLString.Format( ls.getString("post2"))) + ",\n";
				//sql1 += " addr              = " + (( ls.getString("addr")			 == null || ls.getString("addr") 		 == "") ? "addr"			    : SQLString.Format( ls.getString("addr"))) + ",\n";
				//sql1 += " addr2             = " + (( ls.getString("addr2")			 == null || ls.getString("addr2") 		 == "") ? "addr2"			: SQLString.Format( ls.getString("addr2"))) + ",\n";
				//sql1 += " hometel           = " + (( ls.getString("hometel")			 == null || ls.getString("hometel") 		 == "") ? "hometel"			: SQLString.Format( ls.getString("hometel"))) + ",\n";
				//sql1 += " handphone         = " + (( ls.getString("handphone")		 == null || ls.getString("handphone") 	 == "") ? "handphone"		: SQLString.Format( ls.getString("handphone"))) + ",\n";
				//sql1 += " comptel           = " + (( ls.getString("comptel")			 == null || ls.getString("comptel") 		 == "") ? "comptel"			: SQLString.Format( ls.getString("comptel"))) + ",\n";
				//sql1 += " office_gbn        = " + (( ls.getString("office_gbn")		 == null || ls.getString("office_gbn") 		 == "") ? "office_gbn"	: SQLString.Format( ls.getString("office_gbn"))) + ",\n";
				//sql1 += " ldate				= to_char(sysdate, 'YYYYMMDDHH24MISS')";
				//sql1 += " where userid = " + SQLString.Format(v_userid.toLowerCase() );

				sql1  = " update TZ_MEMBER set \n";
				sql1 += " 	post	 		= " + (( ls.getString("post") 		 		== null || ls.getString("post") 		 == "") ? "post"			: SQLString.Format( ls.getString("post"))) + 		",\n";
				sql1 += " 	post_nm 		= " + (( ls.getString("post_nm") 		 	== null || ls.getString("post_nm") 		 == "") ? "post_nm"			: SQLString.Format( ls.getString("post_nm"))) + 	",\n";
				sql1 += " 	email			= " + (( ls.getString("email") 		 		== null || ls.getString("email") 		 == "") ? "email"			: SQLString.Format( ls.getString("email"))) + 		",\n";
				sql1 += " 	birth_date 			= fn_crypt('1', " + (( ls.getString("birth_date") 		 		== null || ls.getString("birth_date") 		 == "") ? "birth_date"			: SQLString.Format( ls.getString("birth_date"))) + 		", 'knise'),\n";
				sql1 += " 	zip_cd 			= " + (( ls.getString("zip_cd") 		 	== null || ls.getString("zip_cd") 		 == "") ? "zip_cd"			: SQLString.Format( ls.getString("zip_cd"))) + 		",\n";
				sql1 += " 	address 		= " + (( ls.getString("address") 		 	== null || ls.getString("address") 		 == "") ? "address"			: SQLString.Format( ls.getString("address"))) + 	",\n";
				sql1 += " 	hometel			= " + (( ls.getString("hometel") 		 	== null || ls.getString("hometel") 		 == "") ? "hometel"			: SQLString.Format( ls.getString("hometel"))) + 	",\n";
				sql1 += " 	handphone 		= " + (( ls.getString("handphone") 		 	== null || ls.getString("handphone") 	 == "") ? "handphone"		: SQLString.Format( ls.getString("handphone"))) + 	",\n";
				sql1 += " 	name 			= " + (( ls.getString("name") 		 		== null || ls.getString("name") 		 == "") ? "name"			: SQLString.Format( ls.getString("name"))) + 		",\n";
				sql1 += " 	comp 			= " + (( ls.getString("comp") 		 		== null || ls.getString("comp") 		 == "") ? "comp"			: SQLString.Format( ls.getString("comp"))) + 		",\n";
				sql1 += " 	ismailling 		= " + (( ls.getString("ismailling") 		== null || ls.getString("ismailling") 	 == "") ? "ismailling"		: SQLString.Format( ls.getString("ismailling"))) + 	",\n";
				sql1 += " 	emp_gubun 		= " + (( ls.getString("emp_gubun") 		 	== null || ls.getString("emp_gubun") 	 == "") ? "emp_gubun"		: SQLString.Format( ls.getString("emp_gubun"))) + 	",\n";
				sql1 += " 	ldate			= to_char(sysdate, 'YYYYMMDDHH24MISS')";
				sql1 += " where userid = " + SQLString.Format(v_userid);
				isOk1 = connMgr.executeUpdate(sql1);
				
				isOk = isOk * isOk1;
				if ( isOk1 == 1)
				success++;
			}


			isOk = isOk * success;
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }

        return isOk;
    }


    /**
    회원 변경 (퇴사자 : 재직여부:퇴사로 변경)
    @param box		receive from the form object and session
    @return int		정상등록여부(1 : 정상, 0 : 오류)
    */
	public int updateOutMember(RequestBox box) throws Exception { 

        boolean v_CreateConnManager = false;
        boolean v_CreatePreparedStatement = false;

		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
		String              sql     = "";
		String sql1 = "";
		int isOk = 1;
		int isOk1 = 1;
		int	success = 0;
		
		String v_userid = "";
		String v_comp		= box.getString("comp");

		try { 
            connMgr = (DBConnectionManager)box.getObject("connMgr");

			if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

			sql =  "select userid from \"TZ_MEMBER\" where comp = '" +v_comp + "' and office_gbn='Y'";
			sql += " MINUS ";
			sql += "select userid from \"TZ_MEMBERTEMP\" where comp = '" +v_comp + "' ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
            	v_userid = ls.getString("userid");

				sql1 =  "update \"TZ_MEMBER\" set office_gbn = 'N', ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
				sql1 += "where userid = '" +v_userid + "'";

	            pstmt = connMgr.prepareStatement(sql1);
		        v_CreatePreparedStatement = true;

				isOk1 = pstmt.executeUpdate();
				isOk	= isOk * isOk1;
				// if ( isOk1 == 1)
				success++;
			}

			isOk = isOk * success;


        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }

        return isOk;
    }


    /**
    회원 변경 (퇴사자 : 재직여부:퇴사로 변경)
    @param box		receive from the form object and session
    @return int		정상등록여부(1 : 정상, 0 : 오류)
    */
	public int updateOutMember2(RequestBox box) throws Exception { 

        boolean v_CreateConnManager = false;
        boolean v_CreatePreparedStatement = false;

		DBConnectionManager connMgr     = null;
		PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
		String              sql     = "";
		String sql1 = "";
		int isOk = 0;
		int isOk1 = 0;
		int	success = 0;
		
		String v_userid = "";

        Vector v_comp               = new Vector();
        v_comp                      = box.getVector("tem_comp");
        Enumeration em              = v_comp.elements();
        String v_compcd		        = "";   // 실제 넘어온 회사코드
        String tem_compcd	        = "";   // 임시 회사코드

		try { 
            connMgr = (DBConnectionManager)box.getObject("connMgr");

			if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            while ( em.hasMoreElements() ) { 
                v_compcd   = (String)em.nextElement();

				if ( !tem_compcd.equals(v_compcd)) { 

						sql =  "select userid from \"TZ_MEMBER\" where comp = '" +v_compcd + "' and office_gbn='Y'";
						sql += " MINUS ";
						sql += "select userid from \"TZ_MEMBERTEMP\" where comp = '" +v_compcd + "' ";

						ls = connMgr.executeQuery(sql);

						while ( ls.next() ) { 
							v_userid = ls.getString("userid");

							sql1 =  "update \"TZ_MEMBER\" set office_gbn = 'N', ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
							sql1 += "where userid = '" +v_userid + "'";

							pstmt = connMgr.prepareStatement(sql1);
							v_CreatePreparedStatement = true;

							isOk1 = pstmt.executeUpdate();
							isOk	= isOk + isOk1;
						}
				}

				tem_compcd = v_compcd ;
			}
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }

        return isOk;
    }




    /**
    Member 존재 여부 체크
    @param userid           USER ID
    @return String Return
    @ 신청가능하면 true (중복안됨)
    @ 신청불가능하면 false(중복됨)
    */
     public String isExitMember(String userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;

        String sql			= "";
        String value		= "";



        try { 
            connMgr = new DBConnectionManager();
            sql +="select count(userid) as isretire from tz_member where userid = '" +userid + "' \n";
            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();

            if ( rs1.next() ) { 
                if ( rs1.getInt("isretire") == 0 ) { 
                  value = "0";  // 정상
                } else{         
                  value = "1";  // 동일아이디 존재
                }               
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return value;
    }
     /**
    Member 존재 여부 체크
    @param userid           USER ID
    @return String Return
    @ 신청가능하면 true (중복안됨)
    @ 신청불가능하면 false(중복됨)
      */
     public String isExitMemberbirth_date(String birth_date) throws Exception { 
    	 DBConnectionManager	connMgr	= null;
    	 PreparedStatement pstmt1 = null;
    	 ResultSet rs1 = null;
    	 
    	 String sql			= "";
    	 String value		= "";
    	 
    	 
    	 
    	 try { 
    		 connMgr = new DBConnectionManager();
    		 sql +="select count(userid) cnt from tz_member where birth_date = fn_crypt('1', '" +birth_date + "', 'knise') \n";
    		 pstmt1 = connMgr.prepareStatement(sql);
    		 rs1 = pstmt1.executeQuery();
    		 
    		 if ( rs1.next() ) {
    			 if(rs1.getInt("cnt") > 0 ){
    				 value = "3";  	// 중복 주민등록번호 존재
    				 
    			 }else {             
    				 value = "0";	// 정상
    			 }
    		 }
    		 
    	 } catch ( Exception ex ) { 
    		 ErrorManager.getErrorStackTrace(ex, null, sql);
    		 throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
    	 }
    	 
    	 finally { 
    		 if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
    		 if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
    		 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	 }
    	 
    	 return value;
     }

    /**
    Member 존재 여부 체크
    @param userid           USER ID
    @return String Return
    @ 신청가능하면 true (중복안됨)
    @ 신청불가능하면 false(중복됨)
    */
     public String isExitMember(String comp, String userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;

        String sql			= "";
        String value		= "";
        String v_userid = userid;

        try { 
            connMgr = new DBConnectionManager();
            sql = "select comp  from tz_member where userid = '" +v_userid + "' \n";
            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();

            if ( rs1.next() ) { 
				if ( comp.equals(rs1.getString("comp"))) { 
					value = "0";	//  아이디와 회사가 일치하므로 문제가 없음.
				} else { 
					value = "1";	//  중복된 아이디로 회사도 다른 경우의 문제 발생.
				}
            }else { 
				value = "0";	//  인사DB에 존재하지 않습니다.
			}

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return value;
    }

    
    /**
    재직인원 
    @param userid           USER ID
    @return String Return
    @ 신청가능하면 true (중복안됨)
    @ 신청불가능하면 false(중복됨)
    */
     public int gbnYCount(DBConnectionManager connMgr,RequestBox box) throws Exception { 
        ListSet             ls      = null;

        String sql			= "";
        int cnt = 0;

        try { 
             sql = " select count(*) cnt ";
            sql += "  from tz_member a,";
            sql += "       tz_membertemp b ";
            sql += " where a.userid=b.userid";
            sql += "   and a.office_gbn = 'Y'";
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
				cnt = ls.getInt("cnt");
			}
			ls.close();

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return cnt;
    }


    /**
    재직인원 
    @param userid           USER ID
    @return String Return
    @ 신청가능하면 true (중복안됨)
    @ 신청불가능하면 false(중복됨)
    */
     public int gbnNCount(DBConnectionManager connMgr, RequestBox box) throws Exception { 
        ListSet             ls      = null;

        String sql			= "";
        int cnt = 0;

        try { 
             sql = " select count(*) cnt ";
            sql += "  from tz_member a,";
            sql += "       tz_membertemp b ";
            sql += " where a.userid=b.userid";
            sql += "   and a.office_gbn = 'N'";
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
				cnt = ls.getInt("cnt");
			}
			ls.close();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return cnt;
    }
     
     public ArrayList searchPwdList(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr = null;
         ListSet             ls      = null;
         ArrayList           list    = null;
         String              sql     = "";
         DataBox             dbox    = null;

         String v_select     = box.getString("p_select");
         String v_searchtext = box.getString("p_searchtext");
         int v_pageno = box.getInt("p_pageno");
         String v_orderType = box.getString("p_orderType");
         String v_orderColumn = box.getString("p_orderColumn");
         try { 
             connMgr = new DBConnectionManager();

             list = new ArrayList();

             sql = " select userid, ldate, success_yn, admin_yn, admin_id   \n"+
                   " from tz_pwdsearchlist                                  \n"+
                   " where 1=1                                              \n";

             if ( !v_searchtext.equals("") ) {                            //    검색어가 있으면
                 sql += " and userid like   '%" + v_searchtext + "%'";
             }
             
             if ( !v_select.equals("") ) {                            //    검색어가 있으면
                 sql += " and success_yn ='" + v_select + "'";
             }
             if(!"".equals(v_orderColumn)) {
                 sql += "   order by " + v_orderColumn + " " + v_orderType;
             } else {
                 sql += "   order by ldate desc                   ";
             }
             
             ls = connMgr.executeQuery(sql);

             ls.setPageSize(500);                         //  페이지당 row 갯수를 세팅한다
             ls.setCurrentPage(v_pageno);                    //     현재페이지번호를 세팅한다.
             int total_page_count = ls.getTotalPage();       //     전체 페이지 수를 반환한다
             int total_row_count = ls.getTotalCount();    //     전체 row 수를 반환한다

             while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(500));
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
     
     public ArrayList LoginList(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr = null;
         ListSet             ls      = null;
         ArrayList           list    = null;
         String              sql     = "";
         DataBox             dbox    = null;

         String v_searchtext_id = box.getString("p_searchtext_id");
         String v_searchtext_name = box.getString("p_searchtext_name");
         int v_pageno = box.getInt("p_pageno");
         String v_orderType = box.getString("p_orderType");
         String v_orderColumn = box.getString("p_orderColumn");
         try { 
             connMgr = new DBConnectionManager();

             list = new ArrayList();

             sql =   " select a.userid, a.lgip, a.ldate, b.name, b.comp, get_compnm(comp) compnm, b.position_nm, b.lvl_nm   \n"+
                     " from tz_loginid a, tz_member b           \n"+
                    " where a.userid = b.userid                 \n";
                 
              
             
             if ( !v_searchtext_id.equals("") ) {                            //    검색어가 있으면
                 sql += " and a.userid like   '%" + v_searchtext_id + "%'";
             }
             
             if ( !v_searchtext_name.equals("") ) {                            //    검색어가 있으면
                 sql += " and name like   '%" + v_searchtext_name + "%'";
             }
             
             if(!"".equals(v_orderColumn)) {
                 sql += "   order by " + v_orderColumn + " " + v_orderType;
             } else {
                 sql += "   order by ldate desc                   ";
             }

             ls = connMgr.executeQuery(sql);

             ls.setPageSize(15);                             //  페이지당 row 갯수를 세팅한다
             ls.setCurrentPage(v_pageno);                    //     현재페이지번호를 세팅한다.
             int total_page_count = ls.getTotalPage();       //     전체 페이지 수를 반환한다
             int total_row_count = ls.getTotalCount();       //     전체 row 수를 반환한다

             while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(500));
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
     
     
     
     public ArrayList searchLogList(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr = null;
         ListSet             ls      = null;
         ArrayList           list    = null;
         String              sql     = "";
         DataBox             dbox    = null;

         String v_searchtext_id = box.getString("p_searchtext_id");
         String v_searchtext_name = box.getString("p_searchtext_name");
         int v_pageno = box.getInt("p_pageno");
         String v_orderType = box.getString("p_orderType");
         String v_orderColumn = box.getString("p_orderColumn");
         try { 
             connMgr = new DBConnectionManager();

             list = new ArrayList();

             sql = "select a.seq, a.logdate, a.userid, a.name,a.target_userid, a.target_name, a.ip, b.gadminnm   \n"
                 + "from tz_membersearchlog a, tz_gadmin b                  \n"
                 + "where a.gadmin = b.gadmin                               \n";
             
             if ( !v_searchtext_id.equals("") ) {                            //    검색어가 있으면
                 sql += " and userid like   '%" + v_searchtext_id + "%'";
             }
             
             if ( !v_searchtext_name.equals("") ) {                            //    검색어가 있으면
                 sql += " and name like   '%" + v_searchtext_name + "%'";
             }
             
             if(!"".equals(v_orderColumn)) {
                 sql += "   order by " + v_orderColumn + " " + v_orderType;
             } else {
                 sql += "   order by logdate desc                   ";
             }

             ls = connMgr.executeQuery(sql);

             ls.setPageSize(15);                         //  페이지당 row 갯수를 세팅한다
             ls.setCurrentPage(v_pageno);                    //     현재페이지번호를 세팅한다.
             int total_page_count = ls.getTotalPage();       //     전체 페이지 수를 반환한다
             int total_row_count = ls.getTotalCount();    //     전체 row 수를 반환한다

             while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum", new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount", new Integer(500));
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
      *  성공 실패 카운트 (비밀번호 재발급 내역)
      */
      public DataBox selectStatCount(RequestBox box) throws Exception {
           DBConnectionManager connMgr = null;
           ListSet ls = null;
           String sql = "";
           DataBox dbox = null;
                   
          try {
               connMgr = new DBConnectionManager();

               sql = "select sum(case when success_yn = 'Y' then 1 else 0 end) success, \n"
                   + "  sum(case when success_yn = 'N' then 1 else 0 end) fail          \n"
                   + "from tz_pwdsearchlist                                             \n";
               
               ls = connMgr.executeQuery(sql); 
           
               while(ls.next()) {
                   dbox = ls.getDataBox();
               }   
           }
           catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
               throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
           }
           finally {
               if(ls != null) {try {ls.close();} catch(Exception e){}}
               if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
           }
           return dbox;
       }
      
      /**
  	 * 부서목록
  	 * @param box          receive from the form object and session
  	 * @return ArrayList
  	 */
  	public ArrayList selectDeptList(RequestBox box) throws Exception {
  		DBConnectionManager connMgr = null;
  		DataBox dbox                = null;
  		ListSet ls                  = null;
  		ArrayList list              = null;
  		String sql                  = "";

  		String v_comp = box.getString("p_comp");
  		
  		try {
  			connMgr = new DBConnectionManager();
  			list = new ArrayList();
  			      
              sql  = "SELECT GRPCOMP  \n";
              sql += "     , DEPTM  \n";
              sql += "     , DEPTMNM  \n";
              sql += "FROM   TZ_DEPTM  \n";
              sql += "WHERE  GRPCOMP = " + SQLString.Format(v_comp) + "  \n";
  			
  			ls = connMgr.executeQuery(sql);
  			
  			while (ls.next()) {
  				dbox = ls.getDataBox();
  				list.add(dbox);
  			}
  		} catch (Exception ex) {
  			ErrorManager.getErrorStackTrace(ex, box, sql);
  			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
  		} finally {
  			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
  			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
  		}
  		return list;
  	}
  	
  	/**
  	 * 직급목록
  	 * @param box          receive from the form object and session
  	 * @return ArrayList
  	 */
  	public ArrayList selectJikwiList(RequestBox box) throws Exception {
  		DBConnectionManager connMgr = null;
  		DataBox dbox                = null;
  		ListSet ls                  = null;
  		ArrayList list              = null;
  		String sql                  = "";

  		String v_comp = box.getString("p_comp");
  		
  		try {
  			connMgr = new DBConnectionManager();
  			list = new ArrayList();
  			      
              sql  = "SELECT GRPCOMP  \n";
              sql += "     , JIKWI  \n";
              sql += "     , JIKWINM  \n";
              sql += "FROM   TZ_JIKWI  \n";
              sql += "WHERE  GRPCOMP = " + SQLString.Format(v_comp) + "  \n";
  			
  			ls = connMgr.executeQuery(sql);
  			
  			while (ls.next()) {
  				dbox = ls.getDataBox();
  				list.add(dbox);
  			}
  		} catch (Exception ex) {
  			ErrorManager.getErrorStackTrace(ex, box, sql);
  			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
  		} finally {
  			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
  			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
  		}
  		return list;
  	}
  	
      /**
       신규 회원등록(From EP)
       @param box		receive from the form object and session
       @return int	정상등록여부(1 : 정상, 0 : 오류)
       */
  	public int insertMemberInfo(RequestBox box) throws Exception { 
  		DBConnectionManager connMgr = null;
  		PreparedStatement   pstmt   = null;
  		ListSet             ls      = null;
  		String              sql     = "";
  		int 				isOk 	= 0;

  		try {
  			connMgr = new DBConnectionManager();
  			connMgr.setAutoCommit(false);
  			//중복된 회원 조회 
  			sql  = "SELECT USERID  \n";
  			sql += "FROM   TZ_MEMBER  \n";
  			sql += "WHERE  birth_date = fn_crypt('1', " + SQLString.Format(box.getString("p_birth_date1")+box.getString("p_birth_date2")) + ", 'knise')  \n";
  			ls = connMgr.executeQuery(sql);
  			if(ls.next()) {
  				return -1;
  			}
  			//중복된 아이디 조회
            /*  sql  = "SELECT USERID  \n";
              sql += "FROM   TZ_MEMBER  \n";
              sql += "WHERE  USERID = (SELECT ALIAS + " + SQLString.Format(box.getString("p_cono")) + "  \n";
              sql += "                 FROM   TZ_COMPCLASS  \n";
              sql += "                 WHERE  COMP = " + SQLString.Format(box.getString("p_comp")) + ")  \n";
  			ls = connMgr.executeQuery(sql);
  			if(ls.next()) {
  				return -2;
  			}*/
  			
  			/*
  			 * 2008.10.06 회원 테이블 수정으로 인해 주석 
  			 * String v_birthday = StringManager.substring(box.getString("p_birth_date2"), 0, 1).equals("1")
  				|| StringManager.substring(box.getString("p_birth_date2"), 0, 1).equals("2")
  				? "19" + box.getString("p_birth_date1") : "20" + box.getString("p_birth_date1");
  				
  			String v_gender = StringManager.substring(box.getString("p_birth_date2"), 0, 1).equals("1")
  				|| StringManager.substring(box.getString("p_birth_date2"), 0, 1).equals("3")
  				? "M" : "F";				

              sql  = "INSERT INTO TZ_MEMBER  \n";
              sql += "      (USERID, TYPE, CONO, USEUSERID, INDATE  \n";
              sql += "     , LGCNT, LGFAIL, LDATE, PWD, NAME  \n";
              sql += "     , birth_date, BIRTHDAY, POST1, POST2, ADDRESS1  \n";
              sql += "     , ADDRESS2, HOMETEL, HANDPHONE, COMPTEL, EMAIL  \n";
              sql += "     , GENDER, NATIONALITY, SPECIALS, MILEAGEPOINT, PWDHINTITEM  \n";
              sql += "     , PWDHINTANSWER, VALIDATION, ISMAILLING, ISSMS, ISRETIRE  \n";
              sql += "     , COMP, COMPNM, DEPT, DEPTNM, JIKWI  \n";
              sql += "     , JIKWINM, FAMILYYN, OFFICE_GBN)  \n";
              sql += "SELECT ALIAS + ?, 1, ?, 'Y', DBO.TO_DATE(GETDATE(), 'YYYYMMDDHH24MISS')  \n";
              sql += "     , 0, 0, DBO.TO_DATE(GETDATE(), 'YYYYMMDDHH24MISS'), ?, ?  \n";
              sql += "     , ?, ?, ?, ?, ?  \n";
              sql += "     , ?, ?, ?, ?, ?  \n";
              sql += "     , ?, '195', ?, 0, '1'  \n";
              sql += "     , ?, 1, 'Y', 'Y', 'N'  \n";
              sql += "     , ?, ?, ?, ?, ?  \n";
              sql += "     , ?, 'N', 1  \n";
              sql += "FROM   TZ_COMPCLASS  \n";
              sql += "WHERE  COMP = ?  \n";
  			pstmt = connMgr.prepareStatement(sql);
  			int index = 1;
  			pstmt.setString(index++, box.getString("p_cono"));
  			pstmt.setString(index++, box.getString("p_cono"));
  			pstmt.setString(index++, box.getString("p_birth_date2"));
  			pstmt.setString(index++, box.getString("p_name"));
  			pstmt.setString(index++, box.getString("p_birth_date1") + box.getString("p_birth_date2"));
  			pstmt.setString(index++, v_birthday);
  			pstmt.setString(index++, box.getString("p_post1"));
  			pstmt.setString(index++, box.getString("p_post2"));
  			pstmt.setString(index++, box.getString("p_addr"));
  			pstmt.setString(index++, box.getString("p_addr2"));
  			pstmt.setString(index++, box.getString("p_hometel"));
  			pstmt.setString(index++, box.getString("p_handphone"));
  			pstmt.setString(index++, box.getString("p_comptel"));
  			pstmt.setString(index++, box.getString("p_email"));
  			pstmt.setString(index++, v_gender);
  			pstmt.setString(index++, box.getString("p_specials"));
  			pstmt.setString(index++, box.getString("p_birth_date2"));
  			pstmt.setString(index++, box.getString("p_comp"));
  			pstmt.setString(index++, box.getString("p_compnm"));
  			pstmt.setString(index++, box.getString("p_dept"));
  			pstmt.setString(index++, box.getString("p_deptnm"));
  			pstmt.setString(index++, box.getString("p_jikwi"));
  			pstmt.setString(index++, box.getString("p_jikwinm"));
  			pstmt.setString(index++, box.getString("p_comp"));*/
  			String v_email_get = box.getString("p_email_get");

  			if(v_email_get.length()==0) v_email_get = "N";
  			/*
  			 * 2008.10.06 회원 테이블 수정으로 인해 새로 작성
  			 */
  			sql  = "INSERT INTO TZ_MEMBER  							\n";
  			sql += " 	(	  USERID								\n";
  			sql += " 		, PWD									\n";
  			sql += " 		, birth_date							\n";
  			sql += " 		, NAME									\n";
  			sql += " 		, HOMETEL								\n";
  			sql += " 		, EMAIL								\n";
  			sql += " 		, HANDPHONE								\n";
  			sql += " 	  	, LGCNT   								\n";
  			sql += " 	  	, LGFAIL   								\n";
  			sql += " 	  	, LDATE   								\n";
  			sql += " 		, ZIP_CD 								\n";
  			sql += " 		, ADDRESS								\n";
  			sql += " 		, ISMAILLING								\n";
  			sql += " 		, POST_NM								\n";
  			sql += " 		, INDATE								\n";
  			sql += " 		, EMP_GUBUN								\n";
  			sql += " 		, comp								\n";
  			sql += " 		, position_nm								\n";
  			sql += " 		, lvl_nm								\n";
  			sql += " 		, issms								\n";
  			sql += " 	)											\n";
  			sql += " 	VALUES 											\n";
  			sql += " 	(	 ? 											\n";
  			sql += " 		,fn_crypt('1', ?, 'knise') 											\n";
  			sql += " 		,fn_crypt('1', ?, 'knise') 											\n";
  			sql += " 		,? 											\n";
  			sql += " 		,? 											\n";
  			sql += " 		,? 											\n";
  			sql += " 		,? 											\n";
  			sql += " 		,'0' 										\n";
  			sql += " 		,'0'										\n";
  			sql += " 		,to_char(sysdate, 'YYYYMMDDHH24MISS')		\n";
  			sql += " 		,? 											\n";
  			sql += " 		,? 											\n";
  			sql += " 		,? 											\n";
  			sql += " 		,? 											\n";
  			sql += " 		,to_char(sysdate, 'YYYYMMDDHH24MISS')		\n";
  			sql += " 		,? 											\n";
  			sql += " 		,? 											\n";
  			sql += " 		,? 											\n";
  			sql += " 		,? 											\n";
  			sql += " 		,? 											\n";
  			sql += " 	)												\n";
  			
  			
  			pstmt = connMgr.prepareStatement(sql);
  			int index = 1;
  			pstmt.setString(index++, box.getString("p_userid"));
  			pstmt.setString(index++, box.getString("p_pwd"));
  			pstmt.setString(index++, box.getString("p_birth_date1").trim()+box.getString("p_birth_date2").trim());
  			pstmt.setString(index++, box.getString("p_name"));
  			pstmt.setString(index++, box.getString("p_hometel"));
  			pstmt.setString(index++, box.getString("p_email"));
  			pstmt.setString(index++, box.getString("p_handphone"));
  			pstmt.setString(index++, box.getString("p_post1").trim()+"-"+box.getString("p_post2").trim());
  			pstmt.setString(index++, box.getString("p_addr").trim()+" "+box.getString("p_addr2").trim());
  			pstmt.setString(index++, v_email_get);
  			pstmt.setString(index++, box.getString("p_post_nm"));
  			pstmt.setString(index++, box.getString("p_emp_gubun"));
  			pstmt.setString(index++, box.getString("s_company"));
  			pstmt.setString(index++, box.getString("p_position_nm"));
  			pstmt.setString(index++, box.getString("p_lvl_nm"));
  			pstmt.setString(index++, box.getString("p_issms"));
  			
  			isOk  = pstmt.executeUpdate();
  			
  			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
              if(isOk > 0)
              	connMgr.commit();
              else
              	connMgr.rollback();
         } catch ( Exception ex ) {
             ErrorManager.getErrorStackTrace(ex, null, sql);
             if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
         } finally {
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return isOk;
     }
  	
  	/**
    회원가입 아이디 중복 체크
    @param RequestBox box
    @return boolean
    */
    public boolean checkID(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
  		ListSet             ls      = null;
		boolean returnValue	= true;
		String sql			= "";
		
		try {
			connMgr = new DBConnectionManager();
  			connMgr.setAutoCommit(false);
  			int v_cnt = -1;
			String v_wishid = box.getString("p_userid");
			
			sql  = " SELECT COUNT(*) cnt FROM tz_member ";
			sql += "\r\n WHERE userid = "+ StringManager.makeSQL(v_wishid);
			
			ls = connMgr.executeQuery(sql);
			if ( ls.next() ) { 
				v_cnt = ls.getInt("cnt");
			}
			if(v_cnt>0) returnValue = false;

		}catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, null, sql);
            if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
		return returnValue;
	}
    /**
    * 고객센터 회원 등록
    @param RequestBox box
    @return boolean
     */
    public ArrayList selAllDancheM(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet             ls      = null;
    	DataBox dbox                = null;
    	ArrayList list				= null;
    	String sql					= "";
    	
    	
    	try {
  			connMgr = new DBConnectionManager();
  			list = new ArrayList();
  			      
			sql  = " select comp,compnm,compbirth_date,coname,  						\n";
			sql += " case when comp = '1001' then 'KT(주)'    					\n";
			sql += " when comp between '1002'  and '1999' then '계열사'  			\n";
			sql += " when comp between '2000'  and '2999' then '비계열사' 		\n";
			sql += " when comp like 'C%'    then '고객센터' 						\n";
			sql += " when comp like 'PIM' then '협력사'  							\n";
			sql += " else '기타' 												\n";
			sql += " end kind, 													\n";
			sql += " userid, telno,'' zip,'' addr,email 						\n";
			sql += " from tz_compclass 											\n";
			sql += " where substr(comp,1,1) in ('C') 							\n";
			sql += " order by comp 												\n";
               
  			ls = connMgr.executeQuery(sql);
  			
  			while (ls.next()) {
  				dbox = ls.getDataBox();
  				list.add(dbox);
  			}
  		} catch (Exception ex) {
  			ErrorManager.getErrorStackTrace(ex, box, sql);
  			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
  		} finally {
  			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
  			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
  		}
  		return list;
  	}
    /**
     * 비계열사 회원 등록
    @param RequestBox box
    @return boolean
     */
    public ArrayList selAllDancheN(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet             ls      = null;
    	DataBox dbox                = null;
    	ArrayList list				= null;
    	String sql					= "";
    	
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		
    		sql  = " select comp,compnm,compbirth_date,coname,  						\n";
    		sql += " case when comp = '1001' then 'KT(주)'    					\n";
    		sql += " when comp between '1002'  and '1999' then '계열사'  			\n";
    		sql += " when comp between '2000'  and '2999' then '비계열사' 		\n";
    		sql += " when comp like 'C%'    then '고객센터' 						\n";
    		sql += " when comp like 'PIM' then '협력사'  							\n";
    		sql += " else '기타' 												\n";
    		sql += " end kind, 													\n";
    		sql += " userid, telno,'' zip,'' addr,email 						\n";
    		sql += " from tz_compclass 											\n";
    		sql += " where substr(comp,1,1) in ('2') 							\n";
    		sql += " order by comp 												\n";
    		
    		ls = connMgr.executeQuery(sql);
    		
    		while (ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	} catch (Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sql);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); }catch (Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
    	}
    	return list;
    }
    /**
     *  커머스 회원 등록
    @param RequestBox box
    @return boolean
     */
    public ArrayList selAllDancheC(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet             ls      = null;
    	DataBox dbox                = null;
    	ArrayList list				= null;
    	String sql					= "";
    	
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		
    		sql  = " select comp,compnm,compbirth_date,coname,  						\n";
    		sql += " case when comp = '1001' then 'KT(주)'    					\n";
    		sql += " when comp between '1002'  and '1999' then '계열사'  			\n";
    		sql += " when comp between '2000'  and '2999' then '비계열사' 		\n";
    		sql += " when comp like 'C%'    then '고객센터' 						\n";
    		sql += " when comp like 'PIM' then '협력사'  							\n";
    		sql += " else '기타' 												\n";
    		sql += " end kind, 													\n";
    		sql += " userid, telno,'' zip,'' addr,email 						\n";
    		sql += " from tz_compclass 											\n";
    		sql += " where comp = '1009' 										\n";
    		sql += " order by comp 												\n";
    		
    		ls = connMgr.executeQuery(sql);
    		
    		while (ls.next()) {
    			dbox = ls.getDataBox();
    			list.add(dbox);
    		}
    	} catch (Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sql);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
    	} finally {
    		if(ls != null) { try { ls.close(); }catch (Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
    	}
    	return list;
    }
}