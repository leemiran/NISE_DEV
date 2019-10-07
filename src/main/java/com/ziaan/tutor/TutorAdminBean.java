// **********************************************************
//  1. 제      목: TUTOR ADMIN BEAN
//  2. 프로그램명: TutorAdminBean.java
//  3. 개      요: 강사관리 관리자 bean
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 
//  7. 수      정:
// **********************************************************
package com.ziaan.tutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

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
import com.ziaan.propose.ProposeBean;
import com.ziaan.study.StudyStatusData;
import com.ziaan.system.ManagerAdminBean;
import com.ziaan.system.MemberData;

public class TutorAdminBean {
    private ConfigSet config;

    private int row;

    public TutorAdminBean() {
        try {
            config = new ConfigSet();
            // row = Integer.parseInt(config.getProperty("page.manage.row") );
            // // 이 모듈의 페이지당 row 수를 셋팅한다
            row = 100;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 강사료 관리 리스트
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList getPay(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
        ListSet ls2 = null;
        ArrayList list1 = null;
        String sql1 = "", sql2 = "";
        DataBox dbox1 = null;
        int v_pageno = box.getInt("p_pageno");
        String v_subj = "", v_year = "", v_subjseq = "";
        int v_stucnt = 0, v_sulsum = 0;
        double v_okrate = 0;

        String ss_grcode = box.getStringDefault("s_grcode", "ALL"); // 교육그룹
        String ss_gyear = box.getStringDefault("s_gyear", "ALL"); // 년도
        String ss_grseq = box.getStringDefault("s_grseq", "ALL"); // 교육기수
        String ss_uclass = box.getStringDefault("s_upperclass", "ALL"); // 과목분류
        String ss_mclass = box.getStringDefault("s_middleclass", "ALL"); // 과목분류
        String ss_lclass = box.getStringDefault("s_lowerclass", "ALL"); // 과목분류
        String ss_subjcourse = box.getStringDefault("s_subjcourse", "ALL");// 과목&코스
        String ss_subjseq = box.getStringDefault("s_subjseq", "ALL"); // 과목 기수
        String ss_action = box.getString("s_action");
        String ss_tutornm = box.getString("s_tutornm");  //강사명 
        String v_orderColumn = box.getString("p_orderColumn"); // 정렬할 컬럼명
        if (v_orderColumn.equals("")) {
        	v_orderColumn = "userid";
        }
        v_orderColumn = "b." + v_orderColumn;

        try {
            if (ss_action.equals("go")) {
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
            /*
                sql1= "\n select c.subj "
                    + "\n      , c.year "
                    + "\n      , c.subjnm "
                    + "\n      , c.subjseq "
                    + "\n      , c.isclosed "
                    + "\n      , a.class "
                    + "\n      , a.tuserid "
                    + "\n      , b.name "
                    + "\n      , b.birth_date "
                    + "\n      , b.email "
                    + "\n      , (select count(tuserid) from tz_classtutor where subj=a.subj and year=a.year and subjseq=a.subjseq) ctutorcnt "
                    + "\n      , (select count(userid) from tz_student where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class) stucnt "
                    + "\n      , (select count(userid) from tz_student where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class and isgraduated='Y') grayncnt "
                    + "\n      , d.malevel "
                    + "\n      , d.sapoint "
                    + "\n      , d.jigubfee "
                    + "\n from   tz_classtutor a "
                    + "\n      , vz_scsubjseq c "
                    + "\n      , tz_tutor b "
                    + "\n      , tz_tutorgrade d "
                    + "\n where  a.tuserid = b.userid  "
                    + "\n and    a.subj    = c.subj  "
                    + "\n and    a.year    = c.year  "
                    + "\n and    a.subjseq = c.subjseq "  
                    + "\n and    a.subj    = d.subj(+) "
                    + "\n and    a.year    = d.year(+) "
                    + "\n and    a.subjseq = d.subjseq(+) "
                    + "\n and    a.tuserid = d.userid(+) ";
              */
                
                sql1= "\n select c.subj "
                    + "\n      , c.year "
                    + "\n      , c.subjnm "
                    + "\n      , c.subjseq "
                    + "\n      , c.edustart "
                    + "\n      , c.eduend "
                    + "\n      , c.isclosed "
                    + "\n      , a.class "
                    + "\n      , a.tuserid "
                    + "\n      , b.name "
                    + "\n      , b.birth_date "
                    + "\n      , b.email "
                    + "\n      , (select count(tuserid) from tz_classtutor where subj=a.subj and year=a.year and subjseq=a.subjseq) ctutorcnt "
                    + "\n      , (select count(userid) from tz_student where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class) stucnt "
                    + "\n      , decode(isjungsan, 'Y', d.inwon, (select count(userid) from tz_student where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class and isgraduated='Y')) grayncnt "
                    + "\n      , decode(isjungsan, 'Y', d.jigub1, b.price) price "
                    + "\n      , d.jigubfee "
                    + "\n      , nvl(d.isjungsan,'N') isjungsan "
                    + "\n from   tz_classtutor a "
                    + "\n      , vz_scsubjseq c "
                    + "\n      , tz_tutor b "
                    + "\n      , tz_tutorgrade d "
                    + "\n where  a.tuserid = b.userid  "
                    + "\n and    a.subj    = c.subj  "
                    + "\n and    a.year    = c.year  "
                    + "\n and    a.subjseq = c.subjseq "  
                    + "\n and    a.subj    = d.subj(+) "
                    + "\n and    a.year    = d.year(+) "
                    + "\n and    a.subjseq = d.subjseq(+) "
                    + "\n and    a.tuserid = d.userid(+) ";
                    //+ "\n and    c.isclosed = 'Y' ";
                
                if (!ss_grcode.equals("ALL") && !ss_grcode.equals("----"))
                    sql1 += "\n and    c.grcode       = "
                            + SQLString.Format(ss_grcode);
                if (!ss_gyear.equals("ALL"))
                    sql1 += "\n and    c.gyear         = "
                            + SQLString.Format(ss_gyear);
                if (!ss_grseq.equals("ALL"))
                    sql1 += "\n and    c.grseq         = "
                            + SQLString.Format(ss_grseq);
                if (!ss_uclass.equals("ALL"))
                    sql1 += "\n and    c.scupperclass  = "
                            + SQLString.Format(ss_uclass);
                if (!ss_mclass.equals("ALL"))
                    sql1 += "\n and    c.scmiddleclass = "
                            + SQLString.Format(ss_mclass);
                if (!ss_lclass.equals("ALL"))
                    sql1 += "\n and    c.sclowerclass  = "
                            + SQLString.Format(ss_lclass);
                if (!ss_subjcourse.equals("ALL"))
                    sql1 += "\n and    c.scsubj        = "
                            + SQLString.Format(ss_subjcourse);
                if (!ss_subjseq.equals("ALL"))
                    sql1 += "\n and    c.scsubjseq     = "
                            + SQLString.Format(ss_subjseq);
                if (!ss_tutornm.equals(""))
                    sql1 += "\n and    b.name like '%" + ss_tutornm + "%' ";
                            

                sql1 +="\n order  by c.subj, c.year, c.subjseq," + v_orderColumn;
                System.out.println(sql1);
                ls1 = connMgr.executeQuery(sql1);

                while (ls1.next()) {
                    dbox1 = ls1.getDataBox();
                    list1.add(dbox1);
                } // END while
            } // END if

        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (ls1 != null) {
                try {
                    ls1.close();
                } catch (Exception e) {
                }
            }
            if (ls2 != null) {
                try {
                    ls2.close();
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

        return list1;
    }
    
    /**
     * 강사료 정산처리
     * @param box
     * @return
     * @throws Exception
     */
    public int insertTutorJungsan(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt  = null;
        ListSet ls = null;

        StringBuffer sb = null;
        
        Vector v_tutorId = box.getVector("p_tutorId");
        Vector v_subj = box.getVector("p_subj");
        Vector v_year = box.getVector("p_year");
        Vector v_subjseq = box.getVector("p_subjseq");
        Vector v_inwon = box.getVector("p_inwon");
        Vector v_jigub1 = box.getVector("p_jigub1");
        Vector v_jigubfee = box.getVector("p_jigubfee");
        Vector v_isjungsan = box.getVector("p_isjungsan");
        Vector v_isclosed = box.getVector("p_isclosed");
		
        String vs_tutorId = "";
        String vs_subj = "";
        String vs_year = "";
        String vs_subjseq = "";
        String vi_isjungsan = "";
        String vi_isclosed = "";
        
        int vi_inwon=0;
        int vi_jigub1 = 0;
        int vi_jigubfee = 0;
        
        int preIdx = 1;
        int isOk = 1;
        
        try {
            connMgr = new DBConnectionManager();
            for (int i = 0; i < v_tutorId.size(); i++) {

            	vs_tutorId   = (String)v_tutorId.elementAt(i);
                vs_subj      = (String)v_subj.elementAt(i);
                vs_year      = (String)v_year.elementAt(i);
                vs_subjseq   = (String)v_subjseq.elementAt(i);
                vi_inwon     = Integer.parseInt(((String)v_inwon.elementAt(i)));
                vi_jigub1    = Integer.parseInt(((String)v_jigub1.elementAt(i)));
                vi_jigubfee  = Integer.parseInt(((String)v_jigubfee.elementAt(i)));
                vi_isjungsan = (String)v_isjungsan.elementAt(i);
                vi_isclosed =  (String)v_isclosed.elementAt(i);

                if(vi_isclosed.equals("Y")){
                /* 정산여부가 Y일 경우  */
                if(vi_isjungsan.equals("Y")){
                	preIdx = 1;
                	sb = new StringBuffer();	
                	
	                sb.append(" update  tz_tutorgrade \n ")
	                  .append("    set ")
	                  .append("        userid = ?, ")
	                  .append("        subj   = ?, ")
	                  .append("        year   = ?, ")
	                  .append("        subjseq = ?, ")
	                  .append("        inwon   = ?, ")
	                  .append("        jigub1   = ?, ")
	                  .append("        jigubfee   = ?, ")
	                  .append("        isjungsan   = ?, ")
	                  .append("        luserid   = ?, ")
	                  .append("        ldate   = to_char(sysdate, 'YYYYMMDDHH24MISS') ")
	                  .append("  where userid = ? ")
	                  .append("    and subj = ? ")
	                  .append("    and year = ? ")
	                  .append("    and subjseq = ? ");
	                
	                pstmt = connMgr.prepareStatement(sb.toString());
	                
	                pstmt.setString(preIdx++, vs_tutorId);
	                pstmt.setString(preIdx++, vs_subj);
	                pstmt.setString(preIdx++, vs_year);
	                pstmt.setString(preIdx++, vs_subjseq);
	                pstmt.setInt(preIdx++, vi_inwon);
	                pstmt.setInt(preIdx++, vi_jigub1);
	                pstmt.setInt(preIdx++, vi_jigubfee);
	                pstmt.setString(preIdx++, vi_isjungsan);
	                pstmt.setString(preIdx++, box.getSession("userid"));
	                pstmt.setString(preIdx++, vs_tutorId);
	                pstmt.setString(preIdx++, vs_subj);
	                pstmt.setString(preIdx++, vs_year);
	                pstmt.setString(preIdx++, vs_subjseq);
                  
	            /* 정산여부가 N일 경우 */    
                } else {
                	preIdx = 1;
                	sb = new StringBuffer();

                	sb.append(" insert into tz_tutorgrade \n ");
                	sb.append("        ( userid, subj, year, subjseq, inwon, jigub1, jigubfee, isjungsan, luserid, ldate ) \n ");
                	sb.append(" values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS') ) ");
                	
                	pstmt = connMgr.prepareStatement(sb.toString());
                	  
	                pstmt.setString(preIdx++, vs_tutorId);
	                pstmt.setString(preIdx++, vs_subj);
	                pstmt.setString(preIdx++, vs_year);
	                pstmt.setString(preIdx++, vs_subjseq);
	                pstmt.setInt(preIdx++, vi_inwon);
	                pstmt.setInt(preIdx++, vi_jigub1);
	                pstmt.setInt(preIdx++, vi_jigubfee);
	                pstmt.setString(preIdx++, "Y");
	                pstmt.setString(preIdx++, box.getSession("userid"));

                }
                
                isOk += pstmt.executeUpdate();
                pstmt.close();
                }
            }

        } catch (Exception ex) {
            throw new Exception("sql = " + sb.toString() + "\r\n" + ex.getMessage());
        } finally	{
	      if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }	}
	      if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
	      if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
	      if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e10 ) { }	}
	    }

        return isOk;
    }
    
    /**
     * 강사 리스트
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectTutorList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        DataBox dbox = null;

        String v_TutorGubun = box.getString("p_TutorGubun"); // 강사구분 검색
        String v_select = box.getString("p_select"); // 검색항목(과목명1,강사명2,)
        String v_selectType = box.getString("p_selectType"); // 검색조건 (아이디1,
                                                                // 강사명2)
        String v_selectvalue = box.getString("p_selectvalue"); // 검색어
        String v_subjclass = box.getString("p_subjclass"); // 강의분야

        String v_orderColumn = box.getString("p_orderColumn"); // 정렬할 컬럼명
        String v_orderType = box.getString("p_orderType"); // 정렬할 순서

        int v_pageno = box.getInt("p_pageno");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            /*
             * 수정일 : 2006.06.14 수정자 : 김민수 수정내용 : 쿼리변경
             */
            /*
            sql = "select                                                                                      \n"
                    + "        A.userid                     -- 강사id                                                       \n"
                    + "      , A.name                       -- 강사명                                                       \n"
                    + "      , A.comp                       -- 강사소속회사                                                       \n"
                    + "      , A.dept                       -- 부서명                                                       \n"
                    + "      , A.email                      -- 이메일                                                       \n"
                    + "      , E.codenm                     -- 강사구분명                                                      \n"
                    + "      , A.ismanager                  -- 권한여부                                                       \n";
            sql += "      , decode( A.handphone,null, a.phone, '', a.phone, a.handphone ) handphone  -- 휴대폰번호             \n";
            //sql += "      , decode( nvl( c.comp, '' ), '', '', get_compnm( C.comp, 2, 2 ) ) compnm   -- 회사명           \n";
            //sql += "      , decode( C.handphone, null, c.comptel, '', c.comptel, c.handphone ) memhandphone  --      \n";
            //sql += "      , decode( nvl( c.group_id, '' ), '', '', get_compnm( C.comp, 2, 2 ) ) compnm   -- 회사명           \n";
            //sql += "      , decode( C.selp_no, null, c.tel_no, '', c.tel_no, c.selp_no ) memhandphone  --      \n";
            sql += "      , C.email mememail                                                                    \n"
                    + "      , nvl( D.fmon, '' ) fmon                                                              \n"
                    + "      , nvl( D.tmon, '' ) tmon                                                              \n"
                    + "      , c.pwd pwd                                                                           \n"
                    + "      , d.gadmin                                                                            \n";
            sql += "  from                                                                                      \n"
                    + "        TZ_TUTOR A                                                                          \n"
                    + "      , TZ_MEMBER C                                                                         \n"
                    + "      , TZ_MANAGER D                                                                        \n"
                    + "      , TZ_CODE E                                                                           \n";
            sql += " where                                                                                      \n"
                    + "        A.userid = C.userid                                                                 \n";
            sql += "   and  A.userid = D.userid( +)                                                             \n";
            sql += "   and  A.tutorgubun = E.code                                                               \n";
            sql += "   and  D.GADMIN(+) = 'P1'                                                                  \n";
            sql += "   and  E.gubun = '0060'                                                                    \n";
            

            if (!v_selectvalue.equals("")) { // 검색어를 입력시
                if (v_selectType.equals("1")) // 아이디로 조회
                    sql += " and upper(A.userid) like upper('%" + v_selectvalue
                            + "%')                           \n";
                else
                    // 강사명으로 조회
                    sql += " and upper(A.name) like upper('%" + v_selectvalue
                            + "%')                             \n";
            }

            if (!v_TutorGubun.equals("")) { // 강사구분 선택시 tz_code 대분류코드 = 0060
                sql += " and A.tutorgubun = '" + v_TutorGubun
                        + "'                                              \n";
            }

            if (v_orderColumn.equals("")) {
                sql += " order by A.name                                                                        \n";
            } else { // 필드명 정렬
                sql += " order by " + v_orderColumn + v_orderType
                        + "                                           \n";
            }
            */
            sql = "\n select a.userid                     -- 튜터id "
            	+ "\n      , a.name                       -- 튜터명 "
            	+ "\n      , c.position_nm                -- 조직 "
            	+ "\n      , a.comp                       -- 튜터소속회사 "
            	+ "\n      , a.dept                       -- 부서명 "
            	+ "\n      , a.email                      -- 이메일 "
            	+ "\n      , a.ismanager                  -- 권한여부 "
            	+ "\n      , decode( a.handphone,null, a.phone, '', a.phone, a.handphone ) handphone  -- 휴대폰번호 "
            	+ "\n      , c.email as mememail "
            	+ "\n      , nvl( d.fmon, '' ) as fmon "
            	+ "\n      , nvl( d.tmon, '' ) as tmon "
            	+ "\n      , fn_crypt('2', c.pwd, 'knise') as pwd "
            	+ "\n      , d.gadmin "
            	+ "\n      , get_codenm('0102', a.bank) as banknm "
            	+ "\n      , a.account "
            	+ "\n from   tz_tutor a "
            	+ "\n      , tz_member c "
            	+ "\n      , tz_manager d ";
	        if (!v_selectvalue.equals("")) { // 검색어를 입력시
	            if (v_selectType.equals("3")) {
	                // 담당과정으로 조회
		            sql	+="\n      , ( "
		            	+ "\n         select distinct x.userid "
		            	+ "\n         from   tz_subjman x "
		            	+ "\n              , tz_subj y "
		            	+ "\n         where  x.subj = y.subj "
		        		+ "\n         and    (upper(x.subj) like upper("+ StringManager.makeSQL("%" + v_selectvalue + "%")+")  "
		        		+ "\n                or upper(y.subjnm) like upper("+ StringManager.makeSQL("%" + v_selectvalue + "%")+") ) "
		            	+ "\n        ) e ";
                }
            }
            sql	+="\n where  a.userid = c.userid "                                                                 
            	+ "\n and    a.userid = d.userid(+) "
            	+ "\n and    d.gadmin(+) like 'P%' ";

            if (!v_selectvalue.equals("")) { // 검색어를 입력시
                if (v_selectType.equals("1")) {
                	// 강사명으로 조회
                	sql += "\n and    upper(a.name) like upper('%" + v_selectvalue + "%') ";                    
                } else if (v_selectType.equals("2")){
                    // 이름으로 조회
                	sql += "\n and    upper(a.userid) like upper('%" + v_selectvalue + "%') ";
                } else if (v_selectType.equals("3")){
                    // 담당과정으로 조회
                    sql += "\n and    a.userid = e.userid ";
                } else if (v_selectType.equals("4")){
                    // 전공분야으로 조회
                    sql += "\n and    upper(a.professional) like upper('%" + v_selectvalue + "%') ";
                }
            }

            if (v_orderColumn.equals("")) {
                sql += "\n order  by a.name ";
            } else { // 필드명 정렬
                sql += "\n order  by " + v_orderColumn + v_orderType;
            }

            ls = connMgr.executeQuery(sql);

            ls.setPageSize(row); // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno); // 현재페이지번호를 세팅한다.
            int totalpagecount = ls.getTotalPage(); // 전체 페이지 수를 반환한다
            int totalrowcount = ls.getTotalCount(); // 전체 row수를 반환한다

            while (ls.next()) {
                dbox = ls.getDataBox();

                dbox.put("d_dispnum", new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpagecount", new Integer(totalpagecount));
                dbox.put("d_rowcount", new Integer(row));

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

    
    
    
    
    
	/**    //09/04/10 16 : 08 메인에 강사공지사항 두개만 띄우기   rownum만
	* 강사공지사항 리스트화면 select
	* @param	box			 receive from the form object and session
	* @return ArrayList	 공지사항	리스트
	* @throws Exception
	*/
	public ArrayList selectNoticeList(RequestBox box) throws Exception	{ 
		DBConnectionManager	connMgr	= null;
		Connection conn	= null;
		ListSet	ls = null;
		ArrayList           list    = null;
		String              sql     = "";
		// NoticeData	data = null;
		DataBox             dbox    = null;

		int	v_tabseq = box.getInt("p_tabseq");
		int	v_pageno = box.getInt("p_pageno");

		String v_searchtext	= box.getString("p_searchtext");
		String v_search	    = box.getString("p_search");


		try	{ 
			connMgr	= new DBConnectionManager();

			list = new ArrayList();
			sql  = "select *";   //09/04/10 16 : 32 메인에 강사공지사항 두개만 띄우기  select하나면 rownum이 안먹어ㅠ
			sql += "from (";     //09/04/10 16 : 32 메인에 강사공지사항 두개만 띄우기
			sql	+= " select  a.tabseq, a.seq, a.userid, a.name, a.title,	count(b.realfile) upfilecnt, a.indate, a.cnt, a.edustart, a.eduend  ";
			sql	+= " from TZ_BOARD a, TZ_BOARDFILE b                                              ";
			sql	+= " where a.tabseq = b.tabseq( +)                                                       ";
			sql += "   and a.seq    = b.seq( +)                                                          ";
			sql += "   and a.tabseq = " + v_tabseq;

			if ( !v_searchtext.equals("") ) { 				// 	  검색어가 있으면
				if ( v_search.equals("name") ) { 				// 	  이름으로 검색할때
					sql	 += " and a.name	like " + StringManager.makeSQL("%" + v_searchtext +	"%");
				}
				else if	(v_search.equals("title") ) { 		// 	  제목으로 검색할때
					sql	 += " and a.title like "	 + StringManager.makeSQL("%"	 + v_searchtext + "%");
				}
				else if ( v_search.equals("content") ) { 		// 	  내용으로 검색할때
					sql += " and a.content like "	 + StringManager.makeSQL("%"	 + v_searchtext + "%");
				}
			}
			
			/*
			if ( box.getSession("gadmin").equals("P1") ) { 
              sql += " and (                                                                    \n";
              sql += " select                                                               \n";
              sql += "   count(*) cnt                                                       \n";
              sql += " from                                                                 \n";
              sql += "   tz_classtutor x,                                                   \n";
              sql += "   tz_subjseq y                                                       \n";
              sql += " where                                                                \n";
              sql += "   tuserid = '" +box.getSession("userid") + "'                           \n";
              sql += "   and x.subj = y.subj                                                \n";
              sql += "   and x.year = y.year                                                \n";
              sql += "   and x.subjseq = y.subjseq                                          \n";
              sql += "   and (substr(y.edustart, 0 , 8) = a.edustart and substr(y.eduend, 0, 8 ) = a.eduend)\n";
              sql += " ) > 0                                                                \n";
            }
            */
			
			sql	 += " group by a.tabseq, a.seq, a.userid, a.name, a.title,	a.indate, a.cnt, a.edustart, a.eduend ";
			sql	 += " order by a.seq desc                                                               ";
			sql  += ")";                        //09/04/10 16 : 32 메인에 강사공지사항 두개만 띄우기
			sql  += "where rownum <=2 ";        //09/04/10 16 : 32 메인에 강사공지사항 두개만 띄우기
			
			
			ls = connMgr.executeQuery(sql);

			ls.setPageSize(row);			              // 	 페이지당 row 갯수를 세팅한다
			ls.setCurrentPage(v_pageno);				  // 	   현재페이지번호를	세팅한다.
			int	totalpagecount = ls.getTotalPage();		  // 	 전체 페이지 수를 반환한다
			int	totalrowcount =	ls.getTotalCount();	      // 	  전체 row 수를	반환한다

			while ( ls.next() ) { 
				dbox = ls.getDataBox();

				list.add(dbox);
			}
		}
		catch ( Exception ex ) { 
			   ErrorManager.getErrorStackTrace(ex, box,	sql);
			throw new Exception("sql = " + sql + "\r\n"	 + ex.getMessage() );
		}
		finally	{ 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e )	{ } }
			if ( connMgr != null )	{ try { 	connMgr.freeConnection(); } catch (Exception	e10 ) { }	}
		}
		return list;
	}    
    
    
    
    
    
    
    
    /**
     * 강사 과목운영 정보
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectTutorSubjHistoryList(String v_tutorid)
            throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        DataBox dbox = null;

        int v_pay = 0;
        int v_price = 0;
        int v_addprice = 0;
        int v_grayncnt = 0;
        int v_edutimes = 0;

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            /*
            sql = " select c.subj,c.year,c.subjnm,c.subjseq, c.edustart, c.eduend, c.isclosed, ";
            sql += "        b.tutorgubun, d.price, d.addprice ";
            sql += "        , (select count(userid) from TZ_STUDENT where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class) stucnt";
            sql += "        , (select count(userid) from TZ_STUDENT where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class and ISGRADUATED='Y') grayncnt";
            sql += "  from TZ_CLASSTUTOR a,TZ_SUBJSEQ c, TZ_TUTOR b, TZ_TUTORPAY d                                        ";
            sql += "  where a.tuserid=b.userid and a.subj=c.subj and a.year=c.year and a.subjseq=c.subjseq and b.tutorgubun = d.tutorcode ";
            sql += "  and   a.tuserid = '" + v_tutorid + "'  \n";
            sql += "order by edustart desc \n";
            */
            
            // 2008.12.08 수정
            /*
            sql = "\n select c.subj "
            	+ "\n      , c.year "
            	+ "\n      , c.subjnm "
            	+ "\n      , c.subjseq "
            	+ "\n      , a.class "
            	+ "\n      , c.edustart "
            	+ "\n      , c.eduend "
            	+ "\n      , c.isclosed "
            	+ "\n      , b.tutorgubun "
            	+ "\n      , e.edutimes "
            	+ "\n      , d.price "
            	+ "\n      , d.addprice "
            	+ "\n      , ( "
            	+ "\n         select count(userid) "
            	+ "\n         from   tz_student "
            	+ "\n         where  subj    = a.subj "
            	+ "\n         and    year    = a.year "
            	+ "\n         and    subjseq = a.subjseq "
            	+ "\n         and    class   = a.class "
            	+ "\n        ) as stucnt "
            	+ "\n      , ( "
            	+ "\n         select count(userid) "
            	+ "\n         from   tz_student "
            	+ "\n         where  subj    = a.subj "
            	+ "\n         and    year    = a.year "
            	+ "\n         and    subjseq = a.subjseq "
            	+ "\n         and    class   = a.class "
            	+ "\n         and    isgraduated = 'Y' "
            	+ "\n        ) as grayncnt "
            	+ "\n      , satisfaction "
            	+ "\n from   tz_classtutor a "
            	+ "\n      , tz_tutor b "
            	+ "\n      , tz_tutorpay d "
            	+ "\n      , tz_subjseq c "
            	+ "\n      , tz_subj e "
            	+ "\n      , (select subj"
            	+ "\n              , round(avg(distcode7), 1) satisfaction "
            	+ "\n         from   tz_suleach "
            	+ "\n         group  by subj) f "
            	+ "\n where  a.tuserid = b.userid "
            	+ "\n and    a.subj    = c.subj "
            	+ "\n and    a.year    = c.year "
            	+ "\n and    a.subjseq = c.subjseq "
            	+ "\n and    c.subj    = e.subj "
            	+ "\n and    e.subj    = f.subj(+) "
            	+ "\n and    b.tutorgubun = d.tutorcode "
            	+ "\n and    a.tuserid = '" + v_tutorid + "' "
            	+ "\n order  by edustart desc ";
			*/
            
            sql = "\n select c.subj "
            	+ "\n      , c.year "
            	+ "\n      , c.subjnm "
            	+ "\n      , c.subjseq "
            	+ "\n      , a.class "
            	+ "\n      , c.edustart "
            	+ "\n      , c.eduend "
            	+ "\n      , c.isclosed "
            	+ "\n      , b.tutorgubun "
            	+ "\n      , e.edutimes "
            	+ "\n      , b.price "
            	+ "\n      , decode(g.jigub1, null, b.price, g.jigub1) jigub1 "
            	+ "\n      , ( "
            	+ "\n         select count(userid) "
            	+ "\n         from   tz_student "
            	+ "\n         where  subj    = a.subj "
            	+ "\n         and    year    = a.year "
            	+ "\n         and    subjseq = a.subjseq "
            	+ "\n         and    class   = a.class "
            	+ "\n        ) as stucnt "
            	+ "\n      , decode(g.isjungsan, 'Y', g.inwon "
            	+ "\n      , ( "
            	+ "\n         select count(userid) "
            	+ "\n         from   tz_student "
            	+ "\n         where  subj    = a.subj "
            	+ "\n         and    year    = a.year "
            	+ "\n         and    subjseq = a.subjseq "
            	+ "\n         and    class   = a.class "
            	+ "\n         and    isgraduated = 'Y' "
            	+ "\n        ) "
            	+ "\n        ) as grayncnt "
            	+ "\n      , satisfaction "
            	+ "\n from   tz_classtutor a "
            	+ "\n      , tz_tutor b "
            	+ "\n      , tz_subjseq c "
            	+ "\n      , tz_subj e "
            	+ "\n      , (select subj"
            	+ "\n              , round(avg(distcode7), 1) satisfaction "
            	+ "\n         from   tz_suleach "
            	+ "\n         group  by subj) f "
            	+ "\n      , tz_tutorgrade g "
            	+ "\n where  a.tuserid = b.userid "
            	+ "\n and    a.subj    = c.subj "
            	+ "\n and    a.year    = c.year "
            	+ "\n and    a.subjseq = c.subjseq "
            	+ "\n and    c.subj    = e.subj "
            	+ "\n and    e.subj    = f.subj(+) "
            	+ "\n and    a.tuserid = '" + v_tutorid + "' "
            	+ "\n and    g.subj(+)    = a.subj "
            	+ "\n and    g.year(+)    = a.year "
            	+ "\n and    g.subjseq(+) = a.subjseq "
            	+ "\n and    g.userid(+) = a.tuserid "
            	+ "\n order  by edustart desc ";
            
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();

                //v_price = ls.getInt("price");
                //v_addprice = ls.getInt("addprice");
                //v_grayncnt = ls.getInt("grayncnt");
                //v_pay = (v_price * v_grayncnt) + v_addprice;
                
                /*
                                          시간당 계산시 주석 제거
                v_edutimes = ls.getInt("edutimes");
                v_pay = (v_price * v_edutimes) + v_addprice;
                */

                //dbox.put("d_pay", v_pay + "");
                //dbox.put("d_satisfaction", new Double(ls.getDouble("satisfaction")));

                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex); 
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

    /**
     * 강사 조회
     * 
     * @param box
     *            receive from the form object and session
     * @return TutorData
     */
    public DataBox selectTutor(RequestBox box) throws Exception {

        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
        ListSet ls2 = null;
        ListSet ls3 = null;
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        TutorData data = null;
        DataBox dbox = null;

        String v_userid = box.getString("p_userid");
        String v_grcode = "";
        String v_phototerms = "";
        String v_birth_date = "";
        String v_sex = "";
        try {
            connMgr = new DBConnectionManager();
            /*
            // select * from tz_tutor
            sql1= "select a.grcode                                                                                          \n"
                + "     , a.userid                                                                                          \n"
                + "     , a.name                                                                                            \n"
                + "     , b.pwd                                                                                             \n"
                + "     , a.post1                                                                                           \n"
                + "     , a.post2                                                                                           \n"
                + "     , a.add1                                                                                            \n"
                + "     , a.add2                                                                                            \n"
            	+ "     , decode(a.tutorgubun,'I',b.hometel,a.phone) phone                                                  \n"
            	+ "     , decode(a.tutorgubun,'I',b.handphone,a.handphone) handphone                                        \n"
            	+ "     , a.fax                                                                                             \n"
            	+ "     , decode(a.tutorgubun,'I',b.email,a.email ) email                                                   \n"
            	+ "     , a.compcd                                                                                          \n"
            	+ "     , decode(a.tutorgubun,'I',b.comp ,a.comp ) comp                     								\n"
            	+ "     , decode(a.tutorgubun,'I','',a.jik ) jik                                                            \n"
            	+ "     , a.dept                                                                                            \n"
                + "     , a.academic                                                                                        \n"
                + "     , a.major                                                                                           \n"
                + "     , a.isadd                                                                                           \n"
                + "     , a.iscyber                                                                                         \n"
                + "     , c.codenm                                                                                          \n"
                + "     , a.isgubuntype                                                                                     \n"
                + "     , a.isgubun                                                                                         \n"
            	+ "     , a.isstatus                                                                                        \n"
                + "     , a.istutor                                                                                         \n"
                + "     , a.careeryear                                                                                      \n"
                + "     , a.license                                                                                         \n"
                + "     , a.career                                                                                          \n"
                + "     , a.book                                                                                            \n"
                + "     , a.professional                                                                                    \n"
                + "     , a.charge                                                                                          \n"
                + "     , a.isinfo                                                                                          \n"
            	+ "     , a.etc                                                                                             \n"
                + "     , a.phone                                                                                           \n"
            	+ "     , decode(a.tutorgubun,'I',b.birth_date, a.birth_date) birth_date                                                   \n"
            	+ "     , a.intro                                                                                           \n"
                + "     , ismanager                                                                                         \n"
            	+ "     , a.subjclass                                                                                    	\n"
            	+ "     , (select codenm from tz_code where gubun = '0039' and code = a.subjclass) subjclassnm              \n"
            	+ "     , a.profile              																			\n"
            	+ "from   tz_tutor a                                                                                        \n"
                + "     , tz_member b                                                                                       \n"
            	+ "     , tz_code c                                                                                         \n"
            	+ "where  a.userid = b.userid( +)                                                                         	\n"
                + "and    a.userid = '" + v_userid + "'									                                    \n"
                + "and    c.gubun = '0060' 																					\n"
                + "and    a.tutorgubun = c.code																				\n";
             */

            // 2008.12.08 수정
            sql1= "select a.grcode                                                                                          \n"
            	+ "     , nvl((select grcodenm from tz_grcode where grcode = a.grcode),'전체') as grcodenm					\n"
                + "     , a.userid                                                                                          \n"
                + "     , a.name                                                                                            \n"
                + "     , fn_crypt('2', b.pwd, 'knise') pwd                                                                                             \n"
                + "     , a.post1                                                                                           \n"
                + "     , a.post2                                                                                           \n"
                + "     , a.add1                                                                                            \n"
                + "     , a.add2                                                                                            \n"
            	+ "     , a.phone											                                                \n"
            	+ "     , a.handphone												                                        \n"
            	+ "     , a.fax                                                                                             \n"
            	+ "     , b.email											                                                \n"
            	+ "     , decode(a.compcd, null, b.comp, a.compcd) as compcd	                                            \n"
            	+ "     , decode(a.compcd, null, get_compnm(b.comp), get_compnm(a.compcd)) as comp							\n"
            	+ "     , decode(a.jik, null, b.lvl_nm, a.jik) as jik                                                       \n"
            	+ "     , a.dept                                                                                            \n"
                + "     , a.academic                                                                                        \n"
                + "     , a.major                                                                                           \n"
                + "     , a.isadd                                                                                           \n"
                + "     , a.iscyber                                                                                         \n"
                + "     , a.isgubuntype                                                                                     \n"
                + "     , a.isgubun                                                                                         \n"
            	+ "     , a.isstatus                                                                                        \n"
                + "     , a.istutor                                                                                         \n"
                + "     , a.careeryear                                                                                      \n"
                + "     , a.license                                                                                         \n"
                + "     , a.career                                                                                          \n"
                + "     , a.book                                                                                            \n"
                + "     , a.professional                                                                                    \n"
                + "     , a.charge                                                                                          \n"
                + "     , a.isinfo                                                                                          \n"
            	+ "     , a.etc                                                                                             \n"
                + "     , a.phone                                                                                           \n"
            	+ "     , fn_crypt('2', b.birth_date, 'knise') birth_date											                                                \n"
            	+ "     , a.intro                                                                                           \n"
                + "     , ismanager                                                                                         \n"
            	+ "     , a.subjclass                                                                                    	\n"
            	+ "     , a.profile              																			\n"
            	+ "     , a.tutorgubun             																			\n"
            	+ "     , a.price             																			\n"
            	+ "     , a.bank   		          																			\n"
            	+ "     , a.account             																			\n"
            	+ "     , decode(c.userid, null, 0, 1) as managerchk														\n"
            	+ "     , c.fmon             																				\n"
            	+ "     , c.tmon             																				\n"
            	+ "     , c.gadmin       																					\n"
            	+ "     , d.gadminnm       																					\n"
            	+ "from   tz_tutor a                                                                                        \n"
                + "     , tz_member b                                                                                       \n"
                + "     , tz_manager c                                                                                      \n"
                + "     , tz_gadmin d                                                                                      	\n"
            	+ "where  a.userid = b.userid(+)                                                                         	\n"
            	+ "and    b.userid = c.userid(+)                                                                         	\n"
            	+ "and    c.gadmin = d.gadmin(+)                                                                         	\n"
            	+ "and    c.gadmin(+) like 'P%'                                                                         	\n"
                + "and    a.userid = '" + v_userid + "'									                                    \n";
            
            ls1 = connMgr.executeQuery(sql1);

            if (ls1.next()) {
                dbox = ls1.getDataBox();
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (ls1 != null) {
                try {
                    ls1.close();
                } catch (Exception e) {
                }
            }
            if (ls2 != null) {
                try {
                    ls2.close();
                } catch (Exception e) {
                }
            }
            if (ls3 != null) {
                try {
                    ls3.close();
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

        return dbox;
    }

    /**
     * 강사등록
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int insertTutor(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk1 = 0;
        int isOk2 = 0;
        int v_result = 0;

        String v_user_id = box.getSession("userid"); // 등록자

        String v_manager = box.getString("p_manager"); // 강사권한여부
        String v_userid = box.getString("p_userid"); // 사용자ID
        String v_name = box.getString("p_name"); // 이름
        String v_subjclass = box.getString("p_subjclass"); // 강의 분야
        String v_post1 = box.getString("p_post1"); // 우편번호1
        String v_post2 = box.getString("p_post2"); // 우편번호2
        String v_add1 = box.getString("p_addr"); // 주소1
        String v_add2 = box.getString("p_addr2"); // 주소2
        String v_phone = box.getString("p_phone"); // 일반전화
        String v_handphone = box.getString("p_handphone"); // 휴대폰
        String v_email = box.getString("p_email"); // 이메일
        String v_comp = box.getString("p_comp"); // 현재소속
        String v_dept = box.getString("p_dept");
        String v_jik = box.getString("p_jik"); // 직위
        String v_intro = box.getString("p_intro"); // 강사소개말
        String v_academic = box.getString("p_academic"); // 학력사항
        String v_major = box.getString("p_major"); // 전공...
        String v_isGubun = box.getString("p_isgubun"); // 강사구분
        String v_tutorgubun = box.getString("p_tutorgubun"); // 강사구분
        String v_saoi = box.getString("p_saoi"); // 비회원 강사등록 구분.
        String v_career = box.getString("p_career"); // 경력사항
        String v_book = box.getString("p_book"); // 저서
        String v_charge = box.getString("p_charge");
        String v_isinfo = box.getString("p_isinfo"); // 메인페이지 소개여부
        String v_compcd = box.getString("p_compcd");
        String v_birth_date = box.getString("p_birth_date");
        String v_profile = box.getString("p_profile");
        int v_price = box.getInt("p_price");  //강사료
        
        String v_bank = box.getString("p_bank"); // 은행코드
        String v_account = box.getString("p_account"); // 계좌번호
        
        String v_professional = box.getString("p_professional"); // 전문분야

        try {
            // 강사의 중복여부 조회
            v_result = overlapping(v_userid);

            if (v_result == 1) {
                return 99;
            }

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // insert TZ_TUTOR table
            sql = "insert into tz_tutor(                  \n";
            sql += "			 userid                   \n";
            sql += "			,name                     \n";
            sql += "			,sex                      \n";
            sql += "			,post1                    \n";
            sql += "			,post2                    \n";
            sql += "			,add1                     \n";
            sql += "			,add2                     \n";
            sql += "			,phone                    \n";
            sql += "			,handphone                \n";
            sql += "			,fax                      \n";// 10
            sql += "			,email                    \n";
            sql += "			,comp                     \n";
            sql += "			,dept                     \n";
            sql += "			,jik                      \n";
            sql += "			,academic                 \n";
            sql += "			,major                    \n";
            sql += "			,iscyber                  \n";
            sql += "			,isgubun                  \n";
            sql += "			,isgubuntype              \n";
            sql += "			,license                  \n";// 20
            sql += "			,career                   \n";
            sql += "			,book                     \n";
            sql += "			,grcode                   \n";
            sql += "			,professional             \n";
            sql += "			,charge                   \n";
            sql += "			,isinfo                   \n";
            sql += "			,etc                      \n";
            sql += "			,photo                    \n";
            sql += "			,indate                   \n";
            sql += "			,luserid                  \n";// 30
            sql += "			,ldate                    \n";
            sql += "			,photoTerms               \n";
            sql += "			,compcd                   \n";
            sql += "			,tutorgubun               \n";
            sql += "			,intro                    \n";
            sql += "			,subjclass                \n";
            sql += "			,ismanager                \n";
            sql += "			,birth_date                    \n";
            sql += "            ,profile                  \n";
            sql += "			,bank                     \n";//40
            sql += "			,account                  \n";
            sql += "			,price                  \n";
            sql += "        )                             \n";
            sql += "values  (?, ?, ?, ?, ?, ?, ?, ?, ?, ? \n";
            sql += "       , ?, ?, ?, ?, ?, ?, ?, ?, ?, ? \n";
            sql += "       , ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDD'), ? \n";
            sql += "       , to_char(sysdate, 'YYYYMMDD'), ?, ?, ?, ?, ?, ?, ?, ?, ? \n";
            sql += "       , ? , ?)    \n";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_userid);
            pstmt.setString(2, v_name);
            pstmt.setString(3, ""); // v_sex
            pstmt.setString(4, v_post1);
            pstmt.setString(5, v_post2);
            pstmt.setString(6, v_add1);
            pstmt.setString(7, v_add2);
            pstmt.setString(8, v_phone);
            pstmt.setString(9, v_handphone);
            pstmt.setString(10, ""); // v_fax
            pstmt.setString(11, v_email);
            pstmt.setString(12, v_compcd);
            pstmt.setString(13, v_dept);
            pstmt.setString(14, v_jik);
            pstmt.setString(15, v_academic);
            pstmt.setString(16, v_major);
            pstmt.setString(17, ""); // v_isCyber
            pstmt.setString(18, v_tutorgubun);
            pstmt.setString(19, ""); // v_isGubuntype
            pstmt.setString(20, ""); // v_license
            pstmt.setString(21, v_career);
            pstmt.setString(22, v_book);
            pstmt.setString(23, ""); // v_grcode
            pstmt.setString(24, v_professional); // v_professional
            pstmt.setString(25, v_charge);
            pstmt.setString(26, v_isinfo);
            pstmt.setString(27, ""); // v_etc
            pstmt.setString(28, ""); // photo
            //
            pstmt.setString(29, v_user_id);
            //
            pstmt.setString(30, ""); // v_phototerms
            pstmt.setString(31, v_compcd);
            pstmt.setString(32, v_tutorgubun);
            pstmt.setString(33, v_intro);
            pstmt.setString(34, v_subjclass);
            pstmt.setString(35, v_manager);
            pstmt.setString(36, v_birth_date);
            pstmt.setString(37, v_profile);
            pstmt.setString(38, v_bank);
            pstmt.setString(39, v_account);
            pstmt.setInt(40, v_price);

            isOk1 = pstmt.executeUpdate(); // TZ_TUTOR 등록

            isOk2 = insertTutorSubj(connMgr, box, v_userid); // TZ_SUBJMAN 등록

            // 강사권한 부여시 권한 등록
            if (v_manager.equals("Y")) {
                this.insertManager(connMgr, box, v_userid); // TZ_MANAGER 등록
            }

            // 비회원 강사일 경우
            if (v_saoi.equals("2")) {
                // Member TABLE에 Insert
                this.insertMember(connMgr, box, v_userid); // TZ_MEMBER 등록
            }

            if (isOk1 > 0) {
                connMgr.commit();
            } else {
                connMgr.rollback();
            }
        } catch (Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e1) {
                }
            }
            if (connMgr != null)
                try {
                    connMgr.setAutoCommit(true);
                } catch (Exception e) {
                }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return isOk1;
    }

    /**
     * 입력한 강사의 중복여부 조회
     * 
     * @param v_userid
     *            강사 아이디
     * @return v_result 1:중복됨 ,0:중복되지 않음
     */
    public int overlapping(String v_userid) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        int v_result = 0;

        try {
            connMgr = new DBConnectionManager();

            sql = "select name from TZ_TUTOR where userid ='" + v_userid + "'";

            ls = connMgr.executeQuery(sql);

            // 중복된 경우 1을 return한다
            if (ls.next())
                v_result = 1;
        } catch (Exception ex) {
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

        return v_result;
    }

    /**
     * 운영자권한 테이블에 등록
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int insertManager(DBConnectionManager connMgr, RequestBox box,
            String v_userid) throws Exception {
        String sql = "";
        String v_user_id = box.getSession("userid");

        // String v_userid = box.getString("p_userid");
        String v_fmon = box.getString("p_fmon");
        String v_tmon = box.getString("p_tmon");
        String v_compcd = box.getString("p_compcd");
        String v_gadmin = box.getString("p_gadmin");
        int isOk = 0;

        try {
            // insert TZ_MANAGER table
            sql = "\n insert into tz_manager ( "
            	+ "\n        userid "
            	+ "\n      , gadmin "
            	+ "\n      , comp "
            	+ "\n      , isdeleted "
            	+ "\n      , fmon "
            	+ "\n      , tmon "
            	+ "\n      , luserid "
            	+ "\n      , ldate "
            	+ "\n       ) "
            	+ "\n values( "
            	+ "\n        '" + v_userid + "' "
            	+ "\n      , '" + v_gadmin + "' "
            	+ "\n      , '" + v_compcd + "' "
            	+ "\n      , 'N' "
            	+ "\n      , '" + v_fmon + "' "
            	+ "\n      , '" + v_tmon + "' "
            	+ "\n      , '" + v_user_id + "' "
            	+ "\n      , to_char(sysdate, 'YYYYMMDDHH24MISS') "
            	+ "\n       ) ";

            isOk = connMgr.executeUpdate(sql);
        } catch (Exception ex) {
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }

        return isOk;
    }

    /**
     * 운영자권한 테이블에 수정
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int updateManager(DBConnectionManager connMgr, RequestBox box,
            String v_userid) throws Exception {
        String sql1 = "";
        String sql2 = "";
        String v_user_id = box.getSession("userid");
        // String v_userid = box.getString("p_userid" );
        String v_manager = box.getString("p_manager");
        String v_fmon = box.getString("p_fmon");
        String v_tmon = box.getString("p_tmon");
        String v_compcd = box.getString("p_compcd");
        String v_gadmin = box.getString("p_gadmin"); // 권한
        String v_oldGadmin = box.getString("p_oldGadmin"); // 이전 권한
        
        int isOk = 0;

        try {
            // delete TZ_MANAGER table
            sql1= "\n delete from tz_manager "
            	+ "\n where  userid = " + StringManager.makeSQL(v_userid)
            	+ "\n and    gadmin = " + StringManager.makeSQL(v_oldGadmin);
            isOk = connMgr.executeUpdate(sql1);

            if (v_manager.equals("Y")) {
                sql2= "\n insert into tz_manager ( "
                	+ "\n        userid "
                	+ "\n      , gadmin "
                	+ "\n      , comp "
                	+ "\n      , isdeleted "
                	+ "\n      , fmon "
                	+ "\n      , tmon "
                	+ "\n      , luserid "
                	+ "\n      , ldate "
                	+ "\n       ) "
                	+ "\n values( " 
                	+ "\n        " + StringManager.makeSQL(v_userid)
                	+ "\n      , " + StringManager.makeSQL(v_gadmin)
                	+ "\n      , " + StringManager.makeSQL(v_compcd)
                	+ "\n      , 'N' "
                	+ "\n      , " + StringManager.makeSQL(v_fmon) 
                	+ "\n      , " + StringManager.makeSQL(v_tmon) 
                	+ "\n      , " + StringManager.makeSQL(v_user_id)
                	+ "\n      , to_char(sysdate, 'YYYYMMDDHH24MISS') "
                	+ "\n       ) ";

                isOk = connMgr.executeUpdate(sql2);
            }
        } catch (Exception ex) {
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        }

        return isOk;
    }

    /**
     * 사용자 테이블에 등록 (강사등록시 비회원일 경우)
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int insertMember(DBConnectionManager connMgr, RequestBox box,
            String v_userid) throws Exception {
        String sql = "";

        String v_user_id = box.getSession("userid");

        String v_loginpw = box.getString("p_loginpw");
        String v_birth_date = box.getString("p_birth_date");
        String v_name = box.getString("p_name");
        String v_email = box.getString("p_email");
        String v_post1 = box.getString("p_post1");
        String v_post2 = box.getString("p_post2");
        String v_post = "";
        if(!v_post1.equals("") && !v_post2.equals("")){
        	v_post = v_post1+"-"+v_post2;
        }

        String v_add1 = box.getString("p_addr");
        String v_add2 = box.getString("p_addr2");
        String v_addr = "";
        if(!v_add1.equals("") && !v_add2.equals("")){
        	v_addr = v_add1+"-"+v_add2;
        }

        String v_phone = box.getString("p_phone");
        String v_handphone = box.getString("p_handphone");
        String v_comp = box.getString("p_comp");

        int isOk = 0;

        try {
            // insert TZ_MEMBER table
            sql = "insert into                                                    "
                    + "       tz_member                                               "
                    + "       (                                                       "
                    + "             userid                                         " + // 아이디
                    "           , birth_date                                            " + // 주민등록번호
                    "           , pwd                                              " + // 비밀번호
                    "           , name                                             " + // 이름
                    "           , email                                            " + // 이메일
                    "           , zip_cd                                           " + // 우편번호
                    "           , address                                          " + // 주소1
                    "           , hometel                                          " + // 집전화번호
                    "           , handphone                                        " + // 휴대폰번호
                    "           , indate                                           " + // 등록일
                    "           , ldate                                            " + // 등록일
                    "           , lgcnt                                            " + // 로그인횟수
                    "           , lgfail                                           " + // 비밀번호오류횟수
                    "           , comp                                             " + // 소속
                    "           , position_nm                                      " + // 소속
                    "       )                                                      "
                    + "values                                                      "
                    + "       (                                                    "
                    + "             '" + v_userid + "'                             "
                    + "           , fn_crypt('1', '" + v_birth_date  + "', 'knise')                             "
                    + "           , fn_crypt('1', '" + v_loginpw + "', 'knise')                              "
                    + "           , '" + v_name   + "'                             "
                    + "           , '" + v_email  + "'                             "
                    + "           , '" + v_post  + "'                             "
                    + "           , '" + v_addr   + "'                             "
                    + "           , '" + v_phone  + "'                             "
                    + "           , '" + v_handphone + "'                             "
                    + "           , to_char(sysdate, 'YYYYMMDDHH24MISS')              "
                    + "           , to_char(sysdate, 'YYYYMMDDHH24MISS')              "
                    + "           , 0                                                 "
                    + "           , 0                                                 "
                    + "           , 'personal'                                        "
                    + "           , '무소속'                                           "
                    + "       )                                                       ";

            isOk = connMgr.executeUpdate(sql);
        } catch (Exception ex) {
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }

        return isOk;
    }

    /**
     * 사용자 테이블에 수정(사외강사중 권한이 있을 경우에만)
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int updateMember(DBConnectionManager connMgr, RequestBox box,
            String v_userid) throws Exception {
        String sql = "";
        String v_user_id = box.getSession("userid");

        String v_loginpw = box.getString("p_loginpw");

        String v_name = box.getString("p_name");
        String v_email = box.getString("p_email");
        String v_post1 = box.getString("p_post1");
        String v_post2 = box.getString("p_post2");

        String v_add1 = box.getString("p_addr");
        String v_add2 = box.getString("p_addr2");

        String v_phone = box.getString("p_phone");
        String v_handphone = box.getString("p_handphone");
        String v_comp = box.getString("p_comp");

        int isOk = 0;

        try {
            // insert TZ_MEMBER table
            sql = "update tz_member set";

            if (!v_loginpw.equals(""))
                sql += "		pwd = fn_crypt('1', '" + v_loginpw + "', 'knise'),";
            sql += "			email = '" + v_email + "',";
            sql += "			zip_cd = '" + v_post1 +"-" + v_post2 +"',";
            //sql += "			post1 = '" + v_post1 + "',";
            //sql += "			post2 = '" + v_post2 + "',";
            sql += "			address = '" + v_add1 + "',";
            //sql += "			address1 = '" + v_add1 + "',";
            //sql += "			address2 = '" + v_add2 + "',";
            sql += "			hometel ='" + v_phone + "',";
            sql += "			handphone = '" + v_handphone + "'";
            sql += "where userid = '" + v_userid + "'";

            isOk = connMgr.executeUpdate(sql);
        } catch (Exception ex) {
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }

        return isOk;
    }

    /**
     * 강사수정
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int updateTutor(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        String sql2 = "";
        int isOk1 = 0;
        int isOk2 = 0;
        int isOk3 = 0;
        int isOk4 = 0;

        ListSet ls = null;

        String v_user_id = box.getSession("userid");

        String v_userid = box.getString("p_userid");
        String v_manager = box.getString("p_manager");
        String v_isgubun = box.getString("p_isgubun" );
        String v_name = box.getString("p_name");
        String v_sex = box.getString("p_sex");
        String v_post1 = box.getString("p_post1");
        String v_post2 = box.getString("p_post2");
        String v_add1 = box.getString("p_addr");
        String v_add2 = box.getString("p_addr2");
        String v_phone = box.getString("p_phone");
        String v_handphone = box.getString("p_handphone");
        String v_email = box.getString("p_email");
        String v_comp = box.getString("p_comp");
        String v_dept = box.getString("p_dept");
        String v_jik = box.getString("p_jik");
        String v_book = box.getString("p_book");
        String v_intro = box.getString("p_intro");
        String v_academic = box.getString("p_academic");
        String v_major = box.getString("p_major");
        String v_isAdd = box.getString("p_isadd");
        String v_career = box.getString("p_career");
        String v_charge = box.getString("p_charge");
        String v_isInfo = box.getString("p_isinfo");
        int    v_price  = box.getInt("p_price");   
        // String v_subjclass = box.getString("p_subjclass");
        String v_subjclass = "";

        String v_sdate = box.getString("p_sdate"); // 권한사용시작일
        String v_ldate = box.getString("p_ldate"); // 권한사용종료일

        // 사외강사중 권한부여일 경우 TZ_MEMBER 테이블에 Insert한다.
        String v_loginid = box.getString("p_userid");
        String v_loginpw = box.getString("p_loginpw");

        // 사외 강사일때는 권한이 있으면 p_userid는 birth_date로 들어간다.
        String v_target_id = v_userid;
        String v_birth_date = box.getString("p_birth_date");
        String v_profile = box.getString("p_profile");
        String v_tutorgubun = box.getString("p_tutorgubun");

        String v_bank = box.getString("p_bank"); // 은행코드
        String v_account = box.getString("p_account"); // 계좌번호
        
        String v_professional = box.getString("p_professional"); // 전공분야
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            if (v_manager.equals("Y")) {
                v_target_id = v_userid;
            }

            // update TZ_TUTOR table
            sql  = "\n update                                   	";
            sql += "\n      tz_tutor                        	    ";
            sql += "\n    set 	                                    ";
            sql += "\n      name=?,                    		        ";
            sql += "\n 		post1=?,                                ";
            sql += "\n 		post2=?,                                ";
            sql += "\n 		add1=?,                                 ";
            sql += "\n 		add2=?,                                 ";
            sql += "\n 		phone=?,                                ";
            sql += "\n 		handphone=?,                            ";
            sql += "\n 		email=?,                                ";
            sql += "\n 		comp=?,                                 ";
            sql += "\n 		dept=?,                                 ";
            sql += "\n 		jik=?,                                  ";
            sql += "\n 		intro=?,                                ";
            sql += "\n 		academic=?,                             ";
            sql += "\n 		major=?,                                ";
            sql += "\n 		career=?,                               ";
            sql += "\n 		book=?,                                 ";
            sql += "\n 		charge=?,                               ";
            sql += "\n 		isinfo=?,                               ";
            sql += "\n 		luserid=?,                              ";
            sql += "\n 		subjclass=?,                            ";
            sql += "\n 		ismanager=?,                            ";
            sql += "\n 	    ldate=to_char(sysdate, 'YYYYMMDD'),     ";
            sql += "\n      profile=?,      						";
            sql += "\n      isgubun=?,      						";
            sql += "\n      tutorgubun=?,      						";
            sql += "\n      bank=?,  	    						";
            sql += "\n      account=?,   	   						";
            sql += "\n      professional=?,   	   					";
            sql += "\n      price=?   	   					";
            sql += "\n  where                                       ";
            sql += "\n      userid = ? 						        ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_name);
            pstmt.setString(2, v_post1);
            pstmt.setString(3, v_post2);
            pstmt.setString(4, v_add1);
            pstmt.setString(5, v_add2);
            pstmt.setString(6, v_phone);
            pstmt.setString(7, v_handphone);
            pstmt.setString(8, v_email);
            pstmt.setString(9, v_comp);
            pstmt.setString(10, v_dept);
            pstmt.setString(11, v_jik);
            pstmt.setString(12, v_intro);
            pstmt.setString(13, v_academic);
            pstmt.setString(14, v_major);
            pstmt.setString(15, v_career);
            pstmt.setString(16, v_book);
            pstmt.setString(17, v_charge);
            pstmt.setString(18, v_isInfo);
            pstmt.setString(19, v_user_id);
            pstmt.setString(20, v_subjclass);
            pstmt.setString(21, v_manager);
            pstmt.setString(22, v_profile);
            pstmt.setString(23, v_tutorgubun);
            pstmt.setString(24, v_tutorgubun);
            pstmt.setString(25, v_bank);
            pstmt.setString(26, v_account);
            pstmt.setString(27, v_professional);
            pstmt.setInt(28, v_price);
            pstmt.setString(29, v_target_id);

            isOk1 = pstmt.executeUpdate(); // TZ_TUTOR 수정

            isOk2 = updateTutorSubj(connMgr, box, v_target_id); // TZ_SUBJMAN 수정

            isOk3 = updateMember(connMgr, box, v_target_id); // TZ_MEMBER 수정

            isOk4 = updateManager(connMgr, box, v_target_id); // TZ_MANAGER 수정

            if (isOk1 > 0) {
                connMgr.commit();
            } else {
                connMgr.rollback();
            }
        } catch (Exception ex) {
        	ex.printStackTrace();
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e1) {
                }
            }
            if (connMgr != null)
                try {
                    connMgr.setAutoCommit(true);
                } catch (Exception e) {
                }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return isOk1;
    }

    /**
     * 강사삭제
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int deleteTutor(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        String sql4 = "";
        int isOk1 = 0;
        int isOk2 = 0;
        int isOk3 = 0;
        int isOk4 = 0;
        String v_userid = box.getString("p_userid");
        String v_gadmin = box.getString("p_gadmin");
        try {
            connMgr = new DBConnectionManager();

            // delete TZ_TUTOR table
            sql1 = "delete from TZ_TUTOR where userid='" + v_userid + "'";
            isOk1 = connMgr.executeUpdate(sql1);

            // delete TZ_SUBJMAN table
            sql2 = "delete from TZ_SUBJMAN where userid='" + v_userid
                    + "' and gadmin = '"+v_gadmin+"'";
            isOk2 = connMgr.executeUpdate(sql2);

            // delete TZ_MANAGER table
            sql3 = "delete from TZ_MANAGER where userid='" + v_userid
                    + "' and gadmin = '"+v_gadmin+"'";
            isOk3 = connMgr.executeUpdate(sql3);

            // delete TZ_MANAGER table
            // sql4 = "delete from tz_member where userid='" +v_userid + "' ";
            // isOk4 = connMgr.executeUpdate(sql4);
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return isOk1;
    }

    /**
     * 사내 강사 조회
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectSearchTutor(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        Connection conn = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        MemberData data = null;
        String v_search = box.getString("p_mode1");
        String v_searchtext = box.getString("p_mode2");
        int v_pageno = box.getInt("p_pageno");

        ConfigSet config = new ConfigSet();
        int row = Integer.parseInt(config.getProperty("page.bulletin.row"));

        try {
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql = "\n select a.userid, fn_crypt('2', a.birth_date, 'knise') birth_date, fn_crypt('2', a.pwd, 'knise') pwd, a.name, a.email "
            	+ "\n      , substr(a.zip_cd, 1, 3) as post1, substr(a.zip_cd, 5, 7) as post2 "
            	+ "\n      , a.address addr, a.hometel, a.handphone "
            	+ "\n      , a.comp, get_compnm(a.comp) compnm, b.telno as comptel "
            	+ "\n from   tz_member a, tz_compclass b "
            	+ "\n where  a.comp = b.comp(+) ";
            if (!v_searchtext.equals("")) { // 검색어가 있으면
                if (v_search.equals("id")) { // ID로 검색할때
                    sql += "\n and    a.userid like   "
                            + StringManager.makeSQL("%" + v_searchtext + "%");
                } /*else if (v_search.equals("cono")) { // 사번으로 검색할때
                    sql += "\n and    cono like "
                            + StringManager.makeSQL("%" + v_searchtext + "%");
                }*/ else if (v_search.equals("name")) { // 이름으로 검색할때
                    sql += "\n and    a.name like "
                            + StringManager.makeSQL("%" + v_searchtext + "%");
                }
            }
            sql += "\n order  by comp asc, name asc";
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(10); // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno); // 현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage(); // 전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount(); // 전체 row 수를 반환한다

            while (ls.next()) {
                data = new MemberData();

                data.setUserid(ls.getString("userid"));
                data.setbirth_date(ls.getString("birth_date"));
                data.setPwd(ls.getString("pwd"));
                data.setName(ls.getString("name"));
                data.setEmail(ls.getString("email"));
                //data.setCono(ls.getString("cono"));
                data.setPost1(ls.getString("post1"));
                data.setPost2(ls.getString("post2"));
                data.setAddr(ls.getString("addr"));
                //data.setAddr2(ls.getString("addr2"));
                data.setHometel(ls.getString("hometel"));
                data.setHandphone(ls.getString("handphone"));
                data.setComptel(ls.getString("comptel"));
                //data.setTel_line(ls.getString("tel_line"));
                data.setComp(ls.getString("comp"));
                //data.setCompnm(ls.getString("compnm"));
                //data.setJikupnm(ls.getString("jikupnm"));
                //data.setJikwi(ls.getString("jikwi"));
                //data.setJikwinm(ls.getString("jikwinm"));
                //data.setDeptnm(ls.getString("deptnam"));
                data.setDispnum(total_row_count - ls.getRowNum() + 1);
                data.setTotalPageCount(total_page_count);

                list.add(data);
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

    /**
     * 강의과목 조회
     * 
     * @param box
     *            receive from the form object and session
     * @return TUtorData
     */
    public ArrayList selectTutorSubjList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String sql = "";
        TutorData data = null;
        String v_userid = box.getString("p_userid");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            // select subj,subjnm
            sql = "\n select a.subj,b.subjnm "
            	+ "\n from   tz_subjman a, tz_subj b "
            	+ "\n where  a.userid = " + StringManager.makeSQL(v_userid)
            	+ "\n and    a.gadmin like 'P%' "
            	+ "\n and    a.subj = b.subj ";
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                data = new TutorData();

                data.setSubj(ls.getString("subj"));
                data.setSubjnm(ls.getString("subjnm"));

                list.add(data);
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

    /**
     * 강사에 해당하는 강의과목등록
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int insertTutorSubj(DBConnectionManager connMgr, RequestBox box, String v_userid) throws Exception {
        String sql = "";
        String v_user_id = box.getSession("userid");
        // String v_userid = box.getString("p_userid");
        String v_gadmin = box.getString("p_gadmin");
        
        // p_subj로 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector v_subj = new Vector();
        v_subj = box.getVector("p_subj");
        Enumeration em = v_subj.elements();
        String v_eachSubj = ""; // 실제 넘어온 각각의 과목코드
        
        int isOk = 0;
        
        try {
            while (em.hasMoreElements()) {
                v_eachSubj = (String) em.nextElement();
                // insert TZ_SUBJMAN table
                sql = "insert into tz_subjman ( "
                	+ "       userid "
                	+ "     , gadmin "
                	+ "     , subj "
                	+ "     , luserid "
                	+ "     , ldate "
                	+ "      ) "
                	+ "values( "
                	+ "       '" + v_userid + "' "
                	+ "     , '" + v_gadmin + "' "
                	+ "     , '" + v_eachSubj + "' "
                	+ "     , '" + v_user_id + "' "
                	+ "     , to_char(sysdate, 'YYYYMMDDHH24MISS') "
                	+ "      ) ";
                isOk = connMgr.executeUpdate(sql);
            }
        } catch (Exception ex) {
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }

        return isOk;
    }

    /**
     * 강사에 해당하는 강의과목수정
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int updateTutorSubj(DBConnectionManager connMgr, RequestBox box,
            String v_userid) throws Exception {
        String sql1 = "";
        String sql2 = "";
        String v_user_id = box.getSession("userid");
        // String v_userid = box.getString("p_userid");
        String v_gadmin = box.getString("p_gadmin"); // 권한
        String v_oldGadmin = box.getString("p_oldGadmin"); // 이전권한
        
        // p_subj로 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector v_subj = new Vector();
        v_subj = box.getVector("p_subj");
        Enumeration em = v_subj.elements();
        String v_eachSubj = ""; // 실제 넘어온 각각의 과목코드
        
        int isOk = 0;

        try {
            // delete TZ_SUBJMAN table
            sql1= "\n delete from tz_subjman "
            	+ "\n where  userid = " + StringManager.makeSQL(v_userid);
            	//+ "\n and    gadmin = " + StringManager.makeSQL(v_oldGadmin);
            isOk = connMgr.executeUpdate(sql1);

            while (em.hasMoreElements()) {
                v_eachSubj = (String) em.nextElement();

                sql2= "\ninsert into tz_subjman ( "
                	+ "\n       userid "
                	+ "\n     , gadmin "
                	+ "\n     , subj "
                	+ "\n     , luserid "
                	+ "\n     , ldate "
                	+ "\n      ) "
                	+ "\nvalues( "
                	+ "\n       " + StringManager.makeSQL(v_userid)
                	+ "\n     , " + StringManager.makeSQL(v_gadmin)
                	+ "\n     , " + StringManager.makeSQL(v_eachSubj) 
                	+ "\n     , " + StringManager.makeSQL(v_user_id) 
                	+ "\n     , to_char(sysdate, 'YYYYMMDDHH24MISS') "
                	+ "\n      ) ";
                isOk = connMgr.executeUpdate(sql2);
            }
        } catch (Exception ex) {
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
        }

        return isOk;
    }

    /**
     * 교육그룹 select box
     * 
     * @param selectname
     *            select box name
     * @param selected
     *            selected valiable
     * @param allcheck
     *            all check Y(1),all check N(0)
     * @return int
     */
    public static String getGrcodeSelect(String selectname, String selected,
            int allcheck) throws Exception {
        DBConnectionManager connMgr = null;
        Connection conn = null;
        ListSet ls = null;
        String result = null;
        String sql = "";

        result = "  <SELECT name=" + selectname + " > \n";

        if (allcheck == 1) {
            result += " <option value='' >== =전체 == =</option > \n";
        }

        try {
            connMgr = new DBConnectionManager();

            sql = "select grcode, grcodenm from tz_grcode  ";
            sql += " order by grcodenm";

            ls = connMgr.executeQuery(sql);
            while (ls.next()) {
                result += " <option value=" + ls.getString("grcode");
                if (selected.equals(ls.getString("grcode"))) {
                    result += " selected ";
                }

                result += " > " + ls.getString("grcodenm") + "</option > \n";
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
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

        result += "  </SELECT > \n";
        return result;
    }

    /**
     * 과목조회
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectSearchSubj(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        TutorData data = null;
        String v_open_select = box.getString("p_open_select");
        int v_pageno = box.getInt("p_pageno");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            // select subj,subjnm
            sql = "select subj,subjnm from TZ_SUBJ ";
            sql += "where subjnm like " + StringManager.makeSQL("%" + v_open_select + "%") + "";
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(10); // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno); // 현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage(); // 전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount(); // 전체 row 수를 반환한다
            while (ls.next()) {
                data = new TutorData();
                data.setSubj(ls.getString("subj"));
                data.setSubjnm(ls.getString("subjnm"));
                data.setDispnum(total_row_count - ls.getRowNum() + 1);
                data.setTotalpagecount(total_page_count);
                list.add(data);
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

    /**
     * 강사 이력 리스트
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectTutorHistoryList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        TutorData data = null;
        // String v_search = box.getString("p_search"); // 전문분야 검색
        String v_select = box.getString("p_select"); // 검색항목(과목명1,강사명2)
        String v_selectvalue = box.getString("p_selectvalue"); // 검색어
        String v_gyear = box.getString("p_gyear"); // 년도
        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            // select
            // userid,name,comp,dept,handphone,email,isGubun,subj,subjnm,lecture,sdesc,lectlevel
            sql = "select A.userid,A.name,A.comp,A.dept,A.handphone,A.email,A.isGubun,B.subj,B.subjnm, ";
            sql += "C.lecture,C.sdesc,C.lectlevel ";
            sql += "from TZ_TUTOR A,TZ_SUBJ B,TZ_OFFSUBJLECTURE C ";
            sql += "where A.userid = C.tutorid and B.subj = C.subj  ";
/*
            if ( !v_search.equals("") ) {            // 전문분야가 있는 경우
                sql += "and A.professional like " + StringManager.makeSQL("%" + v_search + "%") + " ";
            }
*/
            if ( v_select.equals("1") ) {              // 검색항목이 과목명인경우
                sql += "and B.subjnm like " + StringManager.makeSQL("%" + v_selectvalue + "%") + " ";
            } else if ( v_select.equals("2") ) {       // 검색항목이 강사명인경우
                sql += "and A.name like " + StringManager.makeSQL("%" + v_selectvalue + "%") + " ";
            }
            if (!v_gyear.equals("")) { // 년도가 있는 경우
                sql += "and C.year =" + SQLString.Format(v_gyear);
            }
            sql += "order by A.name,B.subj,C.subjseq,C.lecture ";

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                data = new TutorData();
                data.setUserid(ls.getString("userid"));
                data.setName(ls.getString("name"));
                data.setComp(ls.getString("comp"));
                data.setDept(ls.getString("dept"));
                data.setHandphone(ls.getString("handphone"));
                data.setEmail(ls.getString("email"));
                data.setIsgubun(ls.getString("isgubun"));
                data.setSubj(ls.getString("subj"));
                data.setSubjnm(ls.getString("subjnm"));
                data.setLecture(ls.getInt("lecture"));
                data.setSdesc(ls.getString("sdesc"));
                data.setLectlevel(ls.getString("lectlevel"));
                list.add(data);
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

    /**
     * 강사 이력평가 리스트
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectTutorLectureList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        TutorData data = null;
        String v_userid = box.getString("p_userid"); // 강사아이디
        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            // select
            // subj,subjnm,year,subjseq,lecture,sdesc,lectdate,lectsttime,lecttime,lectscore,lectlevel
            sql = "select B.subj,B.subjnm,C.year,C.subjseq,C.lecture,C.sdesc,C.lectdate,C.lectsttime,C.lecttime, ";
            sql += "C.lectscore,C.lectlevel ";
            sql += "from TZ_TUTOR A,TZ_SUBJ B,TZ_OFFSUBJLECTURE C ";
            sql += "where A.userid = C.tutorid and B.subj = C.subj and A.userid="
                    + SQLString.Format(v_userid);
            sql += "order by B.subj,C.subjseq,C.lecture";

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                data = new TutorData();
                data.setSubj(ls.getString("subj"));
                data.setSubjnm(ls.getString("subjnm"));
                data.setYear(ls.getString("year"));
                data.setSubjseq(ls.getString("subjseq"));
                data.setLecture(ls.getInt("lecture"));
                data.setSdesc(ls.getString("sdesc"));
                data.setLectdate(ls.getString("lectdate"));
                data.setLectsttime(ls.getString("lectsttime"));
                data.setLecttime(ls.getString("lecttime"));
                data.setLectscore(ls.getInt("lectscore"));
                data.setLectlevel(ls.getString("lectlevel"));
                list.add(data);
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

    /**
     * 강사 평가 저장
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int updateTutorScore(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt1 = null;
        String sql1 = "";
        int isOk = 0;
        String v_user_id = box.getSession("userid");
        String v_userid = box.getString("p_userid"); // 강사아이디
        String v_subj = "";
        String v_year = "";
        String v_subjseq = "";
        String v_lecture = "";
        // String v_lectscore = "";
        String v_lectlevel = "";
        int v_lectscore = 0;
        Vector v_vec1 = new Vector();
        Vector v_vec2 = new Vector();
        Vector v_vec3 = new Vector();
        Vector v_vec4 = new Vector();
        Vector v_vec5 = new Vector();
        v_vec1 = box.getVector("p_subj");
        v_vec2 = box.getVector("p_year");
        v_vec3 = box.getVector("p_subjseq");
        v_vec4 = box.getVector("p_lecture");
        v_vec5 = box.getVector("p_lectscore");
        Enumeration em1 = v_vec1.elements();
        Enumeration em2 = v_vec2.elements();
        Enumeration em3 = v_vec3.elements();
        Enumeration em4 = v_vec4.elements();
        Enumeration em5 = v_vec5.elements();

        try {
            connMgr = new DBConnectionManager();

            // update TZ_OFFSUBJLECTURE table
            sql1 = "update TZ_OFFSUBJLECTURE set lectscore=?,lectlevel=? ";
            sql1 += "where subj=? and year=? and subjseq=? and lecture=? ";
            pstmt1 = connMgr.prepareStatement(sql1);

            while (em1.hasMoreElements()) {
                v_subj = (String) em1.nextElement();
                v_year = (String) em2.nextElement();
                v_subjseq = (String) em3.nextElement();
                v_lecture = (String) em4.nextElement();
                v_lectscore = Integer.parseInt((String) em5.nextElement());
                if (v_lectscore >= 90) {
                    v_lectlevel = "A";
                } else if (v_lectscore >= 80) {
                    v_lectlevel = "B";
                } else if (v_lectscore >= 70) {
                    v_lectlevel = "C";
                } else if (v_lectscore >= 60) {
                    v_lectlevel = "D";
                } else if (v_lectscore >= 50) {
                    v_lectlevel = "E";
                } else {
                    v_lectlevel = "F";
                }

                pstmt1.setInt(1, v_lectscore);
                pstmt1.setString(2, v_lectlevel);
                pstmt1.setString(3, v_subj);
                pstmt1.setString(4, v_year);
                pstmt1.setString(5, v_subjseq);
                pstmt1.setInt(6, Integer.parseInt(v_lecture));
                isOk = pstmt1.executeUpdate();
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (pstmt1 != null) {
                try {
                    pstmt1.close();
                } catch (Exception e1) {
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

    /**
     * 용역업체 리스트
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectOutCompList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        DataBox dbox = null;

        String v_outcompnmsrch = box.getString("p_outcompnmsrch"); // 용역업체명
        String v_subjclass = box.getString("p_subjclass"); // 강의분야

        String v_orderColumn = box.getString("p_orderColumn"); // 정렬할 컬럼명
        String v_orderType = box.getString("p_orderType"); // 정렬할 순서

        int v_pageno = box.getInt("p_pageno");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = "select 	a.busino, a.compnm, a.represntnm, ";
            sql += "    	  (select codenm from tz_code where gubun = '0039' and code = a.subjclass) subjclassnm,";
            sql += "			tel,";
            sql += "			(select name from tz_member where userid=a.luserid) lusernm, get_compnm(comp, 2, 4) lcompnm ";
            sql += "from tz_tcomp a ";
            sql += "where 1=1 ";

			// 회사명으로 검색
            if ( !v_outcompnmsrch.equals("") ) {              
                sql += "       and a.compnm like " + StringManager.makeSQL("%" + v_outcompnmsrch + "%")  + " ";
            }

            // 분류
            if (!v_subjclass.equals("")) {
                sql += " and a.subjclass = " + SQLString.Format(v_subjclass);
            }

            if (v_orderColumn.equals("")) {
                sql += "order by compnm";
            } else {
                sql += " order by " + v_orderColumn + v_orderType;
            }

            ls = connMgr.executeQuery(sql);

            ls.setPageSize(row); // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno); // 현재페이지번호를 세팅한다.
            int totalpagecount = ls.getTotalPage(); // 전체 페이지 수를 반환한다
            int totalrowcount = ls.getTotalCount(); // 전체 row수를 반환한다

            while (ls.next()) {
                dbox = ls.getDataBox();

                dbox.put("busino", ls.getString("busino"));
                dbox.put("compnm", ls.getString("compnm"));
                dbox.put("represntnm", ls.getString("represntnm"));
                dbox.put("d_subjclassnm", ls.getString("subjclassnm"));
                dbox.put("d_dispnum", new Integer(totalrowcount
                        - ls.getRowNum() + 1));
                dbox.put("d_totalpagecount", new Integer(totalpagecount));
                dbox.put("d_rowcount", new Integer(row));

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

    /**
     * 교육기관 등록
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int insertOutComp(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;
        int v_result = 0;

        String v_user_id = box.getSession("userid"); // 등록자 사번
        String v_user_comp = box.getSession("comp"); // 등록자 소속

        String v_busino = box.getString("p_busino"); // 사업자번호(-없이)
        String v_compnm = box.getString("p_compnm"); // 상호명
        String v_represntnm = box.getString("p_represntnm"); // 대표자
        String v_subjclass = box.getString("p_subjclass"); // 강의분야
        String v_busistatus = box.getString("p_busistatus"); // 업태
        String v_busiitem = box.getString("p_busiitem"); // 업종
        String v_tel = box.getString("p_tel"); // 전화번호
        String v_fax = box.getString("p_fax"); // fax
        String v_post1 = box.getString("p_post1"); // 우편번호1
        String v_post2 = box.getString("p_post2"); // 우편번호2
        String v_addr = box.getString("p_addr2"); // 상세주소
        String v_post = "";

        try {
            // 교육기관 중복여부 조회
            v_result = overlappingOutComp(v_busino);
            if (v_result == 1) {
                return 0;
            }

            connMgr = new DBConnectionManager();

            sql = "insert into tz_tcomp (busino, compnm, represntnm, subjclass,  ";
            sql += "		busistatus, busiitem, tel,fax, ";
            sql += "zipcode, addr, comp, luserid, ldate) ";
            sql += "		values(?,?,?,?,?,?,?,?,?,?,?,?,to_char(sysdate, 'YYYYMMDD')) ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_busino);
            pstmt.setString(2, v_compnm);
            pstmt.setString(3, v_represntnm);
            pstmt.setString(4, v_subjclass);
            pstmt.setString(5, v_busistatus);
            pstmt.setString(6, v_busiitem);
            pstmt.setString(7, v_tel);
            pstmt.setString(8, v_fax);
            if (!v_post1.equals("") && !v_post2.equals("")) {
                v_post = v_post1 + "-" + v_post2;
            }
            pstmt.setString(9, v_post);
            pstmt.setString(10, v_addr);
            pstmt.setString(11, v_user_comp);
            pstmt.setString(12, v_user_id);

            isOk = pstmt.executeUpdate(); // TZ_TCOMP 등록
        } catch (Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e1) {
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

    /**
     * 입력한 교육기관 중복여부 조회
     * 
     * @param v_userid
     *            교육기관 아이디
     * @return v_result 1:중복됨 ,0:중복되지 않음
     */
    public int overlappingOutComp(String v_busino) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        int v_result = 0;

        try {
            connMgr = new DBConnectionManager();
            sql = "select compnm from TZ_TCOMP where busino ='" + v_busino
                    + "'";
            ls = connMgr.executeQuery(sql);
            // 중복된 경우 1을 return한다
            if (ls.next()) {
                v_result = 1;
            }
        } catch (Exception ex) {
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

        return v_result;
    }

    /**
     * 교육기관 조회
     * 
     * @param box
     *            receive from the form object and session
     * @return TutorData
     */
    public DataBox selectOutComp(RequestBox box) throws Exception {

        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        DataBox dbox = null;

        String v_busino = box.getString("p_busino");

        try {
            connMgr = new DBConnectionManager();

            sql = " select 	busino, compnm, represntnm, subjclass, ";
            sql += "    	  (select codenm from tz_code where gubun = '0039' and code = tz_tcomp.subjclass) subjclassnm, ";
            sql += "			busistatus, busiitem, tel, fax,  zipcode,";
            sql += "	    	(select  sido || ' ' || gugun || ' ' || dong  from tz_zipcode ";
            sql += " 		 where 	zipcode = tz_tcomp.zipcode and rownum=1)  as addr1,";
            sql += "	    	addr addr2, comp, luserid, ldate ,";
            sql += "	   		(select name from tz_member where userid=tz_tcomp.luserid) lusernm,";
            sql += "	   		get_compnm(comp,2,2) lusercompnm ";
            sql += "from 	tz_tcomp  ";
            sql += "where	busino = '" + v_busino + "'";
            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                dbox = ls.getDataBox();

                dbox.put("busino", ls.getString("busino"));
                dbox.put("compnm", ls.getString("compnm"));
                dbox.put("represntnm", ls.getString("represntnm"));
                dbox.put("subjclass", ls.getString("subjclass"));
                dbox.put("d_subjclassnm", ls.getString("subjclassnm"));
                dbox.put("busistatus", ls.getString("busistatus"));
                dbox.put("busiitem", ls.getString("busiitem"));
                dbox.put("tel", ls.getString("tel"));
                dbox.put("fax", ls.getString("fax"));
                dbox.put("zipcode", ls.getString("zipcode"));
                dbox.put("addr1", ls.getString("addr1"));
                dbox.put("addr2", ls.getString("addr2"));
                dbox.put("comp", ls.getString("comp"));
                dbox.put("luserid", ls.getString("luserid"));
                dbox.put("ldate", ls.getString("ldate"));
                dbox.put("lusernm", ls.getString("lusernm"));
                dbox.put("lusercompnm", ls.getString("lusercompnm"));

            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage());
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

        return dbox;
    }

    /**
     * 교육기관 수정
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int updateOutComp(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;

        String v_busino = box.getString("p_busino"); // 사업자번호(-없이)
        String v_compnm = box.getString("p_compnm"); // 상호명
        String v_represntnm = box.getString("p_represntnm"); // 대표자
        String v_subjclass = box.getString("p_subjclass"); // 강의분야
        String v_busistatus = box.getString("p_busistatus"); // 업태
        String v_busiitem = box.getString("p_busiitem"); // 업종
        String v_tel = box.getString("p_tel"); // 전화번호
        String v_fax = box.getString("p_fax"); // fax
        String v_post1 = box.getString("p_post1"); // 우편번호1
        String v_post2 = box.getString("p_post2"); // 우편번호2
        String v_addr = box.getString("p_addr2"); // 상세주소
        String v_post = "";

        try {
            connMgr = new DBConnectionManager();

            // update TZ_TCOMP table
            sql = "update TZ_TCOMP ";
            sql += "set 	compnm = ?,";
            sql += "       represntnm = ?,";
            sql += "       subjclass = ?,";
            sql += "       busistatus = ?,";
            sql += "       busiitem = ?,";
            sql += "       tel = ?,";
            sql += "       fax = ?,";
            sql += "       zipcode = ?,";
            sql += "       addr = ?";
            sql += "where	busino = ? ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_compnm);
            pstmt.setString(2, v_represntnm);
            pstmt.setString(3, v_subjclass);
            pstmt.setString(4, v_busistatus);
            pstmt.setString(5, v_busiitem);
            pstmt.setString(6, v_tel);
            pstmt.setString(7, v_fax);

            if (!v_post1.equals("") && !v_post2.equals("")) {
                v_post = v_post1 + "-" + v_post2;
            }
            pstmt.setString(8, v_post);

            pstmt.setString(9, v_addr);
            pstmt.setString(10, v_busino);

            isOk = pstmt.executeUpdate(); // TZ_TCOMP 수정

        } catch (Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e1) {
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

    /**
     * 교육기관 삭제
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int deleteOutComp(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String sql1 = "";
        String sql2 = "";

        int isOk1 = 0;
        int isOk2 = 0;

        String v_busino = box.getString("p_busino");
        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            // delete TZ_TCOMP table
            sql2 = "delete from TZ_TCOMP where busino='" + v_busino + "'";
            isOk2 = connMgr.executeUpdate(sql2);
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }

        return isOk2;
    }

    /**
     * 강사분반현황 2005.08.20 jungkyoungjin 강사지원 - 강사배분현황 <- 2006-06-14 김민수
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectTutorClass(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
        ArrayList list1 = null;
        String sql1 = "";
        DataBox dbox1 = null;

        String v_searchday = (!box.getString("p_searchday").equals("")) ? (box.getString("p_searchday")).replaceAll("-", "") + "" : ""; // 학습시작일
        String ss_action = box.getString("s_action");

        String v_orderColumn = box.getString("p_orderColumn"); // 정렬할 컬럼명
        String v_orderType = box.getString("p_orderType"); // 정렬할 순서

        try {
            if (ss_action.equals("go")) {
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();

                sql1= "\n select get_grcodenm(c.grcode) grcode, c.grseq, grseqnm, c.subjnm, a.tuserid "
                	+ "\n      , get_name(a.tuserid) tusernm "
                	+ "\n      , count(b.userid) stucnt "
                	+ "\n     ,e.comp tcomp, e.handphone tcellphone, e.email temail,e.phone tphone "
                	+ "\n from   tz_classtutor a "
                	+ "\n      , tz_student b "
                	+ "\n      , tz_subjseq c "
                	+ "\n      , tz_grseq   d "
                	+ "\n      , tz_tutor e "
                	+ "\n where  a.subj    = b.subj "
                	+ "\n and    a.year    = b.year "
                	+ "\n and    a.subjseq = b.subjseq "
                	+ "\n and    a.class   = b.class "
                	+ "\n and    a.subj    = c.subj "
                	+ "\n and    a.year    = c.year "
                	+ "\n and    a.subjseq = c.subjseq "
                	+ "\n and    c.grcode  = d.grcode  "
                	+ "\n and    c.gyear   = d.gyear  "
                	+ "\n and    a.tuserid=e.userid(+)   "
                	+ "\n and    c.grseq   = d.grseq  "
                	+ "\n and    substr(c.edustart,1,8) >= "+SQLString.Format(v_searchday)
                	//+ "\n and    c.isclosed = 'N' "
                	+ "\n group  by c.grcode, c.grseq, grseqnm, c.subjnm, a.tuserid ,e.comp , e.handphone , e.email, e.phone ";

                if (v_orderColumn.equals("")) {
                    sql1 += "\n order  by c.grcode, c.grseq, a.tuserid desc ";
                } else {
                    sql1 += "\n order  by " + v_orderColumn + v_orderType;
                }

                ls1 = connMgr.executeQuery(sql1);

                while (ls1.next()) {
                    dbox1 = ls1.getDataBox();
                    list1.add(dbox1);

                } // END while
            } // END if

        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (ls1 != null) {
                try {
                    ls1.close();
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

        return list1;
    }

    /**
     * 강사입과현황 2005.08.20 jungkyoungjin
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectTutorSubj(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
        ArrayList list1 = null;
        String sql1 = "";
        DataBox dbox1 = null;

        String ss_edustart = (!box.getString("p_edustart").equals("")) ? (box
                .getString("p_edustart")).replaceAll("-", "")
                + "" : ""; // 학습시작일
        // String ss_eduend =
        // (!box.getString("p_eduend").equals(""))?(box.getString("p_eduend")).replaceAll("-","")
        // + "":""; // 학습종료일

        String ss_action = box.getString("s_action");

        String v_orderColumn = box.getString("p_orderColumn"); // 정렬할 컬럼명
        String v_orderType = box.getString("p_orderType"); // 정렬할 순서

        try {

            if (ss_action.equals("go")) {
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();

                sql1 += " select  														\n";
                sql1 += " subj, subjnm, year, subjseq, class, tusernm, stucnt,tcomp,tphone,tcellphone, temail,edustart,eduend  \n";
                sql1 += " from 															\n";
                sql1 += "(                                                              \n";
                sql1 += "select                             ";
                sql1 += "a.subj,c.subjnm,a.year,a.subjseq,a.class,a.tuserid,get_name(a.tuserid) tusernm, \n";
                sql1 += "count(b.userid) stucnt,d.comp tcomp,d.phone tphone, d.handphone tcellphone, d.email temail,c.edustart,c.eduend \n";
                sql1 += " from TZ_CLASSTUTOR a, TZ_STUDENT b, TZ_SUBJSEQ c,tz_tutor d \n";
                sql1 += "  where b.subj=a.subj and b.year=a.year and b.subjseq=a.subjseq and b.class=a.class \n";
                sql1 += " and a.subj=c.subj and a.year=c.year and a.subjseq=c.subjseq and a.tuserid=d.userid(+) \n";
                sql1 += " and "
                        + SQLString.Format(ss_edustart)
                        + " between  substr(c.edustart,0,8) and substr(c.eduend,0,8) \n";
                // sql1 += " and " + SQLString.Format(ss_edustart) + " =
                // substr(c.edustart,0,8)";
                // sql1 += " and " + SQLString.Format(ss_eduend) + " =
                // substr(c.eduend,0,8)";

                // sql1 += " and isclosed = 'N'";
                sql1 += " group by a.subj, c.subjnm, a.year, a.subjseq, a.class, a.tuserid, get_name(a.tuserid), d.comp, d.handphone, d.email, c.edustart, c.eduend,d.phone \n";
                sql1 += " ) tutorstu \n";
                if (v_orderColumn.equals("")) {
                    sql1 += " order by tusernm asc ";
                } else {
                    sql1 += " order by tusernm, " + v_orderColumn + v_orderType;
                }
                System.out.println("강사입과현황시작 \r\n" + sql1 + "\r\n강사입과현황끝\r\n");
                ls1 = connMgr.executeQuery(sql1);

                while (ls1.next()) {
                    dbox1 = ls1.getDataBox();
                    list1.add(dbox1);

                } // END while
            } // END if

        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (ls1 != null) {
                try {
                    ls1.close();
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

        return list1;
    }

    /**
     * 강사분반학생목록 2005.08.20
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectTutorClassStudent(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
        ArrayList list1 = null;
        String sql1 = "";
        DataBox dbox1 = null;

        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_class = box.getString("p_class");

        String v_orderColumn = box.getString("p_orderColumn"); // 정렬할 컬럼명
        String v_orderType = box.getString("p_orderType"); // 정렬할 순서

        try {

            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql1 = "select " + "       a.tuserid, "
                    + "       get_name(a.tuserid)     tusernm, "
                    + "       b.userid, "
                    + "       get_name(b.userid)      usernm, ";
            sql1 += "       get_compnm(c.comp)  compnm ";
            sql1 += " from TZ_CLASSTUTOR a, TZ_STUDENT b, TZ_MEMBER c ";
            sql1 += " where b.subj=a.subj and b.year=a.year and b.subjseq=a.subjseq and b.class=a.class ";
            sql1 += "  and b.userid = c.userid";
            sql1 += "  and a.subj		= " + SQLString.Format(v_subj);
            sql1 += "  and a.year		= " + SQLString.Format(v_year);
            sql1 += "  and a.subjseq	= " + SQLString.Format(v_subjseq);
            sql1 += "  and a.class	= " + SQLString.Format(v_class);

            if (v_orderColumn.equals("")) {
                sql1 += " order by tusernm desc ";
            } else {
                sql1 += " order by " + v_orderColumn + v_orderType;
            }

            ls1 = connMgr.executeQuery(sql1);

            while (ls1.next()) {
                dbox1 = ls1.getDataBox();
                list1.add(dbox1);

            } // END while

        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (ls1 != null) {
                try {
                    ls1.close();
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

        return list1;
    }
    
    /*   
    public ArrayList getPay(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String sql = "";

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();*/
            /*
            sql = "select                              "
                    + "        b.code tutorcode,           "
                    + "        b.codenm tutorcodenm,       "
                    + "        nvl(a.price, 0) price,      "
                    + "        nvl(a.addprice, 0) addprice "
                    + "  from                              "
                    + "        TZ_TUTORPAY a,              "
                    + "        tz_code b                   "
                    + " where                              "
                    + "        a.tutorcode(+) = b.code     "
                    + "   and  b.gubun = '0060'            ";
    */
            // 2008.12.08 수정
            // KT인재개발원은 시간당 비용으로 계산
            // tutorcode는 무조건 'T'로 지정(주강사)
            //sql = "select a.tutorcode "
            	//+ "     , nvl(a.price,0) as price "
            	//+ "     , nvl(a.addprice,0) as addprice "
            	//+ "from   tz_tutorpay a "
            	//+ "where  a.tutorcode = 'T' ";
            //ls = connMgr.executeQuery(sql);

            //while (ls.next()) {
                //dbox = ls.getDataBox();

                //dbox.put("tutorcode", ls.getString("tutorcode"));
                //dbox.put("tutorcodenm", ls.getString("tutorcodenm"));
                //dbox.put("price", ls.getString("price"));
                //dbox.put("addprice", ls.getString("addprice"));

                //list.add(dbox);
            //}

        //} catch (Exception ex) {
            //ErrorManager.getErrorStackTrace(ex, box, sql);
            //throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        //} finally {
            //if (ls != null) {
                //try {
                    //ls.close();
                //} catch (Exception e) {
                //}
            //}
            //if (connMgr != null) {
                //try {
                    //connMgr.freeConnection();
                //} catch (Exception e10) {
                //}
            //}
        //}

        //return list;
    //}
    

    /*
     * 강사료 상세 가져오기 2006.06.29 @param box receive from the form object and
     * session @return ArrayList
     */
    public ArrayList getPayDetail(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String sql = "";

        String v_process = box.getString("p_process");
        String v_user_id = box.getSession("userid"); // 로그인 사용자

        String ss_edustart = (!box.getString("p_edustart").equals("")) ? (box
                .getString("p_edustart")).replaceAll("-", "")
                + "00" : ""; // 학습시작일
        String ss_eduend = (!box.getString("p_eduend").equals("")) ? (box
                .getString("p_eduend")).replaceAll("-", "")
                + "24" : ""; // 학습종료일

        // 검색조건들
        String ss_grcode = box.getStringDefault("s_grcode", "ALL"); // 교육그룹
        String ss_gyear = box.getStringDefault("s_gyear", "ALL"); // 년도
        String ss_grseq = box.getStringDefault("s_grseq", "ALL"); // 교육기수
        String ss_uclass = box.getStringDefault("s_upperclass", "ALL"); // 과목분류
        String ss_mclass = box.getStringDefault("s_middleclass", "ALL"); // 과목분류
        String ss_subjcourse = box.getStringDefault("s_subjcourse", "ALL");// 과목&코스
        String ss_subjseq = box.getStringDefault("s_subjseq", "ALL"); // 과목 기수

        String ss_action = box.getString("s_action");
        String v_isclosed = box.getString("p_isclosed");

        String v_orderColumn = box.getString("p_orderColumn"); // 정렬할 컬럼명
        String v_orderType = box.getString("p_orderType"); // 정렬할 순서
        try {

            connMgr = new DBConnectionManager();
            list = new ArrayList();

            if (ss_action.equals("go")) {
                sql += "select                                                                                                                                                             \n";
                sql += "      yyy.tutorid,                                                                                                                                                 \n";
                sql += "      yyy.tutorname,                                                                                                                                               \n";
                sql += "      yyy.tutortype,                                                                                                                                               \n";
                sql += "      yyy.subj,                                                                                                                                                    \n";
                sql += "      yyy.year,                                                                                                                                                    \n";
                sql += "      yyy.grcode,                                                                                                                                                  \n";
                sql += "      yyy.grcodenm,                                                                                                                                                \n";
                sql += "      yyy.grseq,                                                                                                                                                   \n";
                sql += "      yyy.grseqnm,                                                                                                                                                 \n";
                sql += "      yyy.subjname,                                                                                                                                                \n";
                sql += "      yyy.subjseq,                                                                                                                                                 \n";
                sql += "      yyy.class,                                                                                                                                                   \n";
                sql += "      yyy.stuCnt,\n";
                sql += "      yyy.defaultpay,                                                                                                                                              \n";
                sql += "      yyy.alimCnt,                                                                                                                                                 \n";
                sql += "      yyy.qnaCnt,                                                                                                                                                  \n";
                sql += "      yyy.pdsCnt,                                                                                                                                                  \n";
                sql += "      yyy.faqCnt,                                                                                                                                                  \n";
                sql += "      yyy.dicCnt,                                                                                                                                                  \n";
                sql += "      yyy.proCnt,                                                                                                                                                  \n";
                sql += "      yyy.comCnt,                                                                                                                                                  \n";
                sql += "      yyy.etcpay,                                                                                                                                                  \n";
                sql += "      nvl(yyy.reportcnt, 0) reportcnt, \n";
                // sql += "
                // yyy.defaultpay+yyy.alimCnt+yyy.qnaCnt+yyy.pdsCnt+yyy.faqCnt+yyy.dicCnt+yyy.proCnt+yyy.comCnt+yyy.etcpay+yyy.reportcnt
                // totpay,\n";
                sql += "      NVL(yyy.defaultpay, 0) + NVL(yyy.alimCnt, 0) + NVL(yyy.qnaCnt, 0) + NVL(yyy.pdsCnt, 0) + NVL(yyy.faqCnt, 0) + NVL(yyy.dicCnt, 0) + NVL(yyy.proCnt, 0) + NVL(yyy.comCnt, 0) + NVL(yyy.etcpay, 0) + NVL(yyy.reportcnt, 0) totpay, \n";
                sql += "      zzz.etcmemo                                                                                                                                                  \n";
                sql += "  from                                                                                                                                                             \n";
                sql += "      tz_classtutor zzz,                                                                                                                                           \n";
                sql += "       (                                                                                                                                                           \n";
                sql += "       select                                                                                                                                                      \n";
                sql += "               a.tuserid             tutorId         /*강사아이디*/                                                                                                 \n";
                sql += "             , b.NAME                tutorName       /*강사명*/                                                                                                   \n";
                sql += "             , c.CODENM              tutorType       /*강사구분*/                                                                                                  \n";
                sql += "             , a.SUBJ                subj            /*과목*/                                                                                                        \n";
                sql += "             , a.YEAR                year            /*년도*/                                                                                                        \n";
                sql += "             , f.grcode              grCode          /*교육그룹코드*/                                                                                                \n";
                sql += "             , g.gyear               gyear          /*교육그룹코드*/                                                                                                \n";
                sql += "             , f.grcodenm            grCodeNm        /*교육그룹명*/                                                                                                 \n";
                sql += "             , g.grseq               grSeq           /*교육기수*/                                                                                                  \n";
                sql += "             , g.grseqnm             grSeqNm         /*교육기수명*/                                                                                                 \n";
                sql += "             , d.SUBJNM              subjName        /*과목명*/                                                                                                   \n";
                sql += "             , a.SUBJSEQ             subjSeq         /*과목차시*/                                                                                                  \n";
                sql += "             , a.CLASS               class           /*class*/                                                                                                     \n";
                sql += "             , e.price+e.addprice    defaultPay      /*기본료*/                                                                                                       \n";
                sql += "             , a.etcpay              etcPay             /*추가금*/                                                                                                \n";
                sql += "             , d.scupperclass              upperclass                                                                                                             \n";
                sql += "             , d.scmiddleclass              middleclass                                                                                                             \n";
                sql += "             , (                                                                                                                                                   \n";
                sql += "               select                                                                                                                                              \n";
                sql += "                      count(*)*500                                                                                                                                 \n";
                sql += "                 from                                                                                                                                              \n";
                sql += "                      tz_gong gong                                                                                                                                 \n";
                sql += "                where                                                                                                                                              \n";
                sql += "                      gong.subj = a.subj and gong.year = a.year and gong.subjseq = a.subjseq                                                                       \n";
                sql += "                  and gong.userid = a.tuserid                                                                                                                      \n";
                sql += "               ) alimCnt            /*알림방*/                                                                                                                        \n";
                sql += "             , (                                                                                                                                                   \n";
                sql += "               select                                                                                                                                              \n";
                sql += "                      count(*)*500                                                                                                                                 \n";
                sql += "                 from                                                                                                                                              \n";
                sql += "                      tz_bds bds,                                                                                                                                  \n";
                sql += "                      tz_board boa                                                                                                                                 \n";
                sql += "                where                                                                                                                                              \n";
                sql += "                      bds.tabseq = boa.tabseq                                                                                                                      \n";
                sql += "                  and bds.subj = a.subj and indate between RPAD(d.edustart, 16, '0') and RPAD(d.eduend, 16, '9')                                                                                 \n";
                sql += "                  and boa.levels > 1                                                                                                                               \n";
                sql += "                  and bds.type='SQ'                                                                                                                                \n";
                sql += "                  and boa.userid = a.tuserid                                                                                                                       \n";
                sql += "               ) qnaCnt             /*QNA*/                                                                                                                        \n";
                sql += "             , (                                                                                                                                                   \n";
                sql += "               select                                                                                                                                              \n";
                sql += "                      count(*)*500                                                                                                                                 \n";
                sql += "                 from                                                                                                                                              \n";
                sql += "                      tz_bds bds,                                                                                                                                  \n";
                sql += "                      tz_board boa                                                                                                                                 \n";
                sql += "                where                                                                                                                                              \n";
                sql += "                      bds.tabseq = boa.tabseq                                                                                                                      \n";
                sql += "                  and bds.subj = a.subj and indate between RPAD(d.edustart, 16, '0') and RPAD(d.eduend, 16, '9')                                            \n";
                sql += "                  and bds.type='SD'                                                                                                                                \n";
                sql += "                  and boa.userid = a.tuserid                                                                                                                       \n";
                sql += "               ) pdsCnt             /*자료방*/                                                                                                                    \n";
                sql += "             , (                                                                                                                                                   \n";
                sql += "               select                                                                                                                                              \n";
                sql += "                      count(*)*500                                                                                                                                 \n";
                sql += "                 from                                                                                                                                              \n";
                sql += "                      tz_bds bds,                                                                                                                                  \n";
                sql += "                      tz_board boa                                                                                                                                 \n";
                sql += "                where                                                                                                                                              \n";
                sql += "                      bds.tabseq = boa.tabseq                                                                                                                      \n";
                sql += "                  and bds.subj = a.subj and indate between RPAD(d.edustart, 16, '0') and RPAD(d.eduend, 16, '9')                                                \n";
                sql += "                  and bds.type='FA'                                                                                                                                \n";
                sql += "                  and boa.userid = a.tuserid                                                                                                                       \n";
                sql += "               ) faqCnt             /*FAQ*/                                                                                                                        \n";
                sql += "             , (                                                                                                                                                   \n";
                sql += "               select                                                                                                                                              \n";
                sql += "                      count(*)*500                                                                                                                                 \n";
                sql += "                 from                                                                                                                                              \n";
                sql += "                      tz_dic dic                                                                                                                                   \n";
                sql += "                where                                                                                                                                              \n";
                sql += "                      dic.subj = a.subj and ldate between RPAD(d.edustart, 16, '0') and RPAD(d.eduend, 16, '9')                                                     \n";
                sql += "                  and dic.luserid = a.tuserid                                                                                                                      \n";
                sql += "               ) dicCnt             /*용어사전*/                                                                                                                   \n";
                sql += "             , (                                                                                                                                                   \n";
                sql += "               select                                                                                                                                              \n";
                sql += "                      count(*)*500                                                                                                                                 \n";
                sql += "                 from                                                                                                                                              \n";
                sql += "                      tz_bds bds,                                                                                                                                  \n";
                sql += "                      tz_board boa                                                                                                                                 \n";
                sql += "                where                                                                                                                                              \n";
                sql += "                      bds.tabseq = boa.tabseq                                                                                                                      \n";
                sql += "                  and bds.subj = a.subj and indate between RPAD(d.edustart, 16, '0') and RPAD(d.eduend, 16, '9')                                                   \n";
                sql += "                  and bds.type='MP'                                                                                                                                \n";
                sql += "                  and boa.userid = a.tuserid                                                                                                                       \n";
                sql += "               ) proCnt             /*나의각오방*/                                                                                                                  \n";
                sql += "             , (                                                                                                                                                   \n";
                sql += "               select                                                                                                                                              \n";
                sql += "                      count(*)*500                                                                                                                                 \n";
                sql += "                 from                                                                                                                                              \n";
                sql += "                      tz_bds bds,                                                                                                                                  \n";
                sql += "                      tz_board boa                                                                                                                                 \n";
                sql += "                where                                                                                                                                              \n";
                sql += "                      bds.tabseq = boa.tabseq                                                                                                                      \n";
                sql += "                  and bds.subj = a.subj and indate between RPAD(d.edustart, 16, '0') and RPAD(d.eduend, 16, '9')                                                   \n";
                sql += "                  and bds.type='CJ'                                                                                                                                \n";
                sql += "                  and boa.userid = a.tuserid                                                                                                                       \n";
                sql += "               ) comCnt             /*성찰하기방*/                                                                                                                  \n";
                sql += "             , (\n";
                sql += "               select\n";
                sql += "                      sum(\n";
                sql += "                       case when pjp.indate-pjp.tudate <= 120000 then 1\n";
                sql += "                            when pjp.indate-pjp.tudate <= 240000 then 1\n";
                sql += "                            when pjp.indate-pjp.tudate <= 360000 then 1\n";
                sql += "                            else 1 \n";
                sql += "                       end\n";
                sql += "                       )*2500 cnt\n";
                sql += "                 from  \n";
                sql += "                      tz_projrep pjp\n";
                sql += "                where\n";
                sql += "                      pjp.subj = d.subj \n";
                sql += "                  and pjp.year = d.year \n";
                sql += "                  and pjp.subjseq = d.subjseq \n";
                sql += "                  and pjp.tudate is not null\n";
//                sql += "                  and pjp.indate between d.edustart and d.eduend\n";
                // sql += " and d.grseq = '0002'\n";
                sql += "                  and d.subj = a.subj\n";
                sql += "                  and d.year = a.year\n";
                sql += "                  and d.subjseq = a.subjseq\n";
//                sql += "                  and a.tuserid = pjp.luserid\n";
                sql += "               ) reportcnt             /*과제첨삭*/\n";
                sql += "             , (\n";
                sql += "               select\n";
                sql += "                      count(sdt.userid) \n";
                sql += "                 from  \n";
                sql += "                      tz_student sdt\n";
                sql += "                where\n";
                sql += "                      sdt.subj = d.subj\n";
                sql += "                  and sdt.year = d.year\n";
                sql += "                  and sdt.subjseq = d.subjseq\n";
                sql += "               ) stuCnt             /*입과인원*/\n";
                sql += "         from                                                                                                                                                      \n";
                sql += "               TZ_CLASSTUTOR a                                                                                                                                     \n";
                sql += "             , TZ_TUTOR b                                                                                                                                          \n";
                sql += "             , TZ_CODE c                                                                                                                                           \n";
                sql += "             , VZ_SCSUBJSEQ d                                                                                                                                      \n";
                sql += "             , TZ_TUTORPAY e                                                                                                                                       \n";
                sql += "             , TZ_grcode f                                                                                                                                         \n";
                sql += "             , TZ_grseq g                                                                                                                                          \n";
                sql += "             , TZ_subj h                                                                                                                                           \n";
                sql += "        where                                                                                                                                                      \n";
                sql += "               a.TUSERID = b.userid                                                                                                                                \n";
                sql += "          and  c.GUBUN = '0060' and b.ISGUBUN = c.CODE                                                                                                             \n";
                sql += "          and  a.SUBJ = d.subj and a.year = d.year and a.subjseq = d.subjseq                                                                                       \n";
                sql += "          and  c.code = e.tutorcode                                                                                                                                \n";
                sql += "          and  d.grcode = f.grcode                                                                                                                                 \n";
                sql += "          and  d.grcode = g.grcode and d.gyear = g.gyear and d.grseq = g.grseq                                                                                     \n";
                sql += "          and  d.subj = h.subj                                                                                                                                     \n";
                sql += "           ) yyy                                                                                                                                                       \n";
                sql += " where                                                                                                                                                             \n";
                sql += "      zzz.subj = yyy.subj and zzz.year = yyy.year and zzz.subjseq = yyy.subjseq and zzz.tuserid = yyy.tutorId                                                      \n";

                if (!ss_grcode.equals("ALL") && !ss_grcode.equals("----")
                        && !ss_grcode.equals(""))
                    sql += "    and yyy.grcode       = "
                            + SQLString.Format(ss_grcode) + "\n";
                if (!ss_gyear.equals("ALL") && !ss_gyear.equals(""))
                    sql += "    and yyy.gyear        = "
                            + SQLString.Format(ss_gyear) + "\n";
                if (!ss_grseq.equals("ALL") && !ss_grseq.equals(""))
                    sql += "    and yyy.grseq        = "
                            + SQLString.Format(ss_grseq) + "\n";
                if (!ss_uclass.equals("ALL") && !ss_uclass.equals(""))
                    sql += "    and yyy.upperclass = "
                            + SQLString.Format(ss_uclass) + "\n";
                if (!ss_mclass.equals("ALL") && !ss_mclass.equals(""))
                    sql += "    and yyy.middleclass = "
                            + SQLString.Format(ss_mclass) + "\n";
                if (!ss_subjcourse.equals("ALL") && !ss_subjcourse.equals(""))
                    sql += "    and yyy.subj       = "
                            + SQLString.Format(ss_subjcourse) + "\n";
                if (!ss_subjseq.equals("ALL") && !ss_subjseq.equals(""))
                    sql += "    and yyy.subjseq    = "
                            + SQLString.Format(ss_subjseq) + "\n";

                sql += " order by yyy.subjname, zzz.year, zzz.subjseq              \n";

                System.out.println("\n\n\n" + sql + "\n\n\n");
                ls = connMgr.executeQuery(sql);

                while (ls.next()) {
                    dbox = ls.getDataBox();

                    dbox.put("tutorId", ls.getString("tutorId"));
                    dbox.put("tutorName", ls.getString("tutorName"));
                    dbox.put("tutorType", ls.getString("tutorType"));
                    dbox.put("subj", ls.getString("subj"));
                    dbox.put("year", ls.getString("year"));
                    dbox.put("grCode", ls.getString("grCode"));
                    dbox.put("grCodeNm", ls.getString("grCodeNm"));
                    dbox.put("grSeq", ls.getString("grSeq"));
                    dbox.put("grSeqNm", ls.getString("grSeqNm"));
                    dbox.put("subjName", ls.getString("subjName"));
                    dbox.put("subjSeq", ls.getString("subjSeq"));
                    dbox.put("class", ls.getString("class"));
                    dbox.put("stucnt", ls.getString("stucnt"));
                    dbox.put("defaultPay", ls.getString("defaultPay"));
                    dbox.put("alimCnt", ls.getString("alimCnt"));
                    dbox.put("qnaCnt", ls.getString("qnaCnt"));
                    dbox.put("pdsCnt", ls.getString("pdsCnt"));
                    dbox.put("faqCnt", ls.getString("faqCnt"));
                    dbox.put("dicCnt", ls.getString("dicCnt"));
                    dbox.put("proCnt", ls.getString("proCnt"));
                    dbox.put("comCnt", ls.getString("comCnt"));
                    dbox.put("etcPay", ls.getString("etcPay"));

                    System.out.println(dbox.getInt("d_reportcnt")
                            + "==============");
                    dbox.put("reportcnt", ls.getString("reportcnt"));
                    dbox.put("totPay", ls.getString("totPay"));
                    dbox.put("etcMemo", ls.getString("etcMemo"));

                    list.add(dbox);
                }
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

    /**
     * 강사료 수정
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int updatePay(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql1 = "";
        Vector v_tutorcode = box.getVector("p_tutorcode");
        Vector v_price = box.getVector("p_price");
        Vector v_addprice = box.getVector("p_addprice");

        String v_tutorcode1 = "", v_price1 = "", v_addprice1 = "";

        int isOk = 0;

        try {
            connMgr = new DBConnectionManager();

            String v_luserid = "";
            v_luserid = box.getSession("userid");
            for (int i = 0; i < v_tutorcode.size(); i++) {
                v_tutorcode1 = (String) v_tutorcode.elementAt(i);
                v_price1 = ((String) v_price.elementAt(i)).equals("") ? "0"
                        : ((String) v_price.elementAt(i));
                v_addprice1 = ((String) v_addprice.elementAt(i)).equals("") ? "0"
                        : (String) v_addprice.elementAt(i);

                sql1 = "update TZ_TUTORPAY set price = "
                        + SQLString.Format(v_price1) + ", addprice = "
                        + SQLString.Format(v_addprice1);
                sql1 += " where tutorcode =" + SQLString.Format(v_tutorcode1);

                sql1 = "merge into tz_tutorpay a                                       \n";
                sql1 += "using                                                          \n";
                sql1 += "(                                                              \n";
                sql1 += " select                                                        \n";
                sql1 += "        " + SQLString.Format(v_tutorcode1)
                        + " tutorcode,      \n";
                sql1 += "        " + v_price1 + " price,          \n";
                sql1 += "        " + v_addprice1 + " addprice,       \n";
                sql1 += "        " + SQLString.Format(v_luserid)
                        + " luserid,        \n";
                sql1 += "        to_char(sysdate, 'yyyymmddhh24miss')   ldate           \n";
                sql1 += "   from                                                        \n";
                sql1 += "        dual                                                   \n";
                sql1 += ") b                                                            \n";
                sql1 += "on ( a.tutorcode = b.tutorcode )                               \n";
                sql1 += "when matched then                                              \n";
                sql1 += "update set                                                     \n";
                sql1 += "          price    = " + v_price1 + ",    \n";
                sql1 += "          addprice = " + v_addprice1 + ",    \n";
                sql1 += "          Luserid  = " + SQLString.Format(v_luserid)
                        + ",    \n";
                sql1 += "          ldate    = to_char(sysdate, 'yyyymmddhh24miss')      \n";
                sql1 += "when not matched then                                          \n";
                sql1 += "insert values                                                  \n";
                sql1 += "      (                                                        \n";
                sql1 += "       b.tutorcode,                                            \n";
                sql1 += "       b.price,                                                \n";
                sql1 += "       b.addprice,                                             \n";
                sql1 += "       b.luserid,                                              \n";
                sql1 += "       b.ldate                                                 \n";
                sql1 += "      )                                                        \n";

                System.out.println(sql1);
                isOk = connMgr.executeUpdate(sql1);
            }

        } catch (Exception ex) {
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

        return isOk;
    }

    /**
     * 강사료 수정
     * 
     * @param box
     *            receive from the form object and session
     * @return int
     */
    public int DetailupdatePay(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        String sql1 = "";
        Vector v_tutorId = box.getVector("p_tutorId");
        Vector v_subj = box.getVector("p_subj");
        Vector v_year = box.getVector("p_year");
        Vector v_subjseq = box.getVector("p_subjseq");
        Vector v_etcpay = box.getVector("p_etcpay");
        Vector v_etcmemo = box.getVector("p_etcMemo");

        String s_etcpay = "";
        String s_etcmemo = "";
        String s_subj = "";
        String s_year = "";
        String s_subjseq = "";
        String s_tuserid = "";

        int isOk = 0;
        try {
            connMgr = new DBConnectionManager();
            for (int i = 0; i < v_tutorId.size(); i++) {

                s_tuserid = (String) v_tutorId.elementAt(i);
                System.out.println("id : " + s_tuserid + "\n\n");

                s_subj = ((String) v_subj.elementAt(i)).equals("") ? "0"
                        : ((String) v_subj.elementAt(i));
                System.out.println("s_subj : " + s_subj + "\n\n");

                s_year = ((String) v_year.elementAt(i)).equals("") ? "0"
                        : (String) v_year.elementAt(i);
                System.out.println("s_year : " + s_year + "\n\n");

                s_subjseq = ((String) v_subjseq.elementAt(i)).equals("") ? "0"
                        : (String) v_subjseq.elementAt(i);
                System.out.println("s_subjseq : " + s_subjseq + "\n\n");

                s_etcpay = ((String) v_etcpay.elementAt(i)).equals("") ? "0"
                        : (String) (v_etcpay.elementAt(i));
                System.out.println("s_etcpay : " + s_etcpay + "\n\n");

                s_etcmemo = ((String) v_etcmemo.elementAt(i)).equals("") ? ""
                        : (String) (v_etcmemo.elementAt(i));
                System.out.println("s_etcmemo : " + s_etcmemo + "\n\n");

                sql1 = "update                     \n"
                        + "       tz_classtutor \n" + "   set \n"
                        + "       etcpay = " + s_etcpay + ", \n"
                        + "       etcmemo = '" + s_etcmemo + "' \n"
                        + "where \n" + "       subj='" + s_subj + "' \n"
                        + "  and  year='" + s_year + "' \n"
                        + "  and  subjseq='" + s_subjseq + "' \n"
                        + "  and  tuserid='" + s_tuserid + "' \n";

                System.out.println(sql1);
                isOk += connMgr.executeUpdate(sql1);
            }

        } catch (Exception ex) {
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

        return isOk;
    }

    /**
     * 강사평가 명단 리스트 2005.01.28 kimsujin
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    /*
    public ArrayList selectTutorGradeList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
        ListSet ls2 = null;
        ArrayList list1 = null;
        String sql1 = "", sql2 = "";
        DataBox dbox1 = null;
        int v_pageno = box.getInt("p_pageno");
        String v_subj = "", v_year = "", v_subjseq = "";
        int v_stucnt = 0, v_sulsum = 0;
        double v_okrate = 0;

        String ss_grcode = box.getStringDefault("s_grcode", "ALL"); // 교육그룹
        String ss_gyear = box.getStringDefault("s_gyear", "ALL"); // 년도
        String ss_grseq = box.getStringDefault("s_grseq", "ALL"); // 교육기수
        String ss_uclass = box.getStringDefault("s_upperclass", "ALL"); // 과목분류
        String ss_mclass = box.getStringDefault("s_middleclass", "ALL"); // 과목분류
        String ss_lclass = box.getStringDefault("s_lowerclass", "ALL"); // 과목분류
        String ss_subjcourse = box.getStringDefault("s_subjcourse", "ALL");// 과목&코스
        String ss_subjseq = box.getStringDefault("s_subjseq", "ALL"); // 과목 기수
        String ss_action = box.getString("s_action");
        String v_orderColumn = box.getString("p_orderColumn"); // 정렬할 컬럼명
        if (v_orderColumn.equals("")) {
        	v_orderColumn = "userid";
        }
        v_orderColumn = "b." + v_orderColumn;

        try {
            if (ss_action.equals("go")) {
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();

                sql1= "\n select c.subj "
                    + "\n      , c.year "
                    + "\n      , c.subjnm "
                    + "\n      , c.subjseq "
                    + "\n      , c.isclosed "
                    + "\n      , a.class "
                    + "\n      , a.tuserid "
                    + "\n      , b.name "
                    + "\n      , b.birth_date "
                    + "\n      , b.email "
                    + "\n      , (select count(tuserid) from tz_classtutor where subj=a.subj and year=a.year and subjseq=a.subjseq) ctutorcnt "
                    + "\n      , (select count(userid) from tz_student where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class) stucnt "
                    + "\n      , (select count(userid) from tz_student where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class and isgraduated='Y') grayncnt "
                    + "\n      , d.malevel "
                    + "\n      , d.sapoint "
                    + "\n      , d.jigubfee "
                    + "\n from   tz_classtutor a "
                    + "\n      , vz_scsubjseq c "
                    + "\n      , tz_tutor b "
                    + "\n      , tz_tutorgrade d "
                    + "\n where  a.tuserid = b.userid  "
                    + "\n and    a.subj    = c.subj  "
                    + "\n and    a.year    = c.year  "
                    + "\n and    a.subjseq = c.subjseq "  
                    + "\n and    a.subj    = d.subj(+) "
                    + "\n and    a.year    = d.year(+) "
                    + "\n and    a.subjseq = d.subjseq(+) "
                    + "\n and    a.tuserid = d.userid(+) ";
                    
                sql1= "\n select c.subj "
                    + "\n      , c.year "
                    + "\n      , c.subjnm "
                    + "\n      , c.subjseq "
                    + "\n      , c.edustart "
                    + "\n      , c.eduend "
                    + "\n      , c.isclosed "
                    + "\n      , a.class "
                    + "\n      , a.tuserid "
                    + "\n      , b.name "
                    + "\n      , b.birth_date "
                    + "\n      , b.email "
                    + "\n      , (select count(tuserid) from tz_classtutor where subj=a.subj and year=a.year and subjseq=a.subjseq) ctutorcnt "
                    + "\n      , (select count(userid) from tz_student where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class) stucnt "
                    + "\n      , decode(isjungsan, 'Y', d.inwon, (select count(userid) from tz_student where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class and isgraduated='Y')) grayncnt "
                    + "\n      , decode(isjungsan, 'Y', d.jigub1, b.price) price "
                    + "\n      , d.jigubfee "
                    + "\n      , nvl(d.isjungsan,'N') isjungsan "
                    + "\n from   tz_classtutor a "
                    + "\n      , vz_scsubjseq c "
                    + "\n      , tz_tutor b "
                    + "\n      , tz_tutorgrade d "
                    + "\n where  a.tuserid = b.userid  "
                    + "\n and    a.subj    = c.subj  "
                    + "\n and    a.year    = c.year  "
                    + "\n and    a.subjseq = c.subjseq "  
                    + "\n and    a.subj    = d.subj(+) "
                    + "\n and    a.year    = d.year(+) "
                    + "\n and    a.subjseq = d.subjseq(+) "
                    + "\n and    a.tuserid = d.userid(+) "
                    + "\n and    c.isclosed = 'Y' ";
                
                if (!ss_grcode.equals("ALL") && !ss_grcode.equals("----"))
                    sql1 += "\n and    c.grcode       = "
                            + SQLString.Format(ss_grcode);
                if (!ss_gyear.equals("ALL"))
                    sql1 += "\n and    c.gyear         = "
                            + SQLString.Format(ss_gyear);
                if (!ss_grseq.equals("ALL"))
                    sql1 += "\n and    c.grseq         = "
                            + SQLString.Format(ss_grseq);
                if (!ss_uclass.equals("ALL"))
                    sql1 += "\n and    c.scupperclass  = "
                            + SQLString.Format(ss_uclass);
                if (!ss_mclass.equals("ALL"))
                    sql1 += "\n and    c.scmiddleclass = "
                            + SQLString.Format(ss_mclass);
                if (!ss_lclass.equals("ALL"))
                    sql1 += "\n and    c.sclowerclass  = "
                            + SQLString.Format(ss_lclass);
                if (!ss_subjcourse.equals("ALL"))
                    sql1 += "\n and    c.scsubj        = "
                            + SQLString.Format(ss_subjcourse);
                if (!ss_subjseq.equals("ALL"))
                    sql1 += "\n and    c.scsubjseq     = "
                            + SQLString.Format(ss_subjseq);

                sql1 +="\n order  by c.subj, c.year, c.subjseq," + v_orderColumn;

                ls1 = connMgr.executeQuery(sql1);

                while (ls1.next()) {
                    dbox1 = ls1.getDataBox();
                    list1.add(dbox1);
                } // END while
            } // END if

        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (ls1 != null) {
                try {
                    ls1.close();
                } catch (Exception e) {
                }
            }
            if (ls2 != null) {
                try {
                    ls2.close();
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

        return list1;
    }
    */

    /**
     * 강사료관리 엑셀출력
     */
    public ArrayList selectTutorGradeList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
        ListSet ls2 = null;
        ArrayList list1 = null;
        String sql1 = "", sql2 = "";
        DataBox dbox1 = null;
        int v_pageno = box.getInt("p_pageno");
        String v_subj = "", v_year = "", v_subjseq = "";
        int v_stucnt = 0, v_sulsum = 0;
        double v_okrate = 0;

        String ss_grcode = box.getStringDefault("s_grcode", "ALL"); // 교육그룹
        String ss_gyear = box.getStringDefault("s_gyear", "ALL"); // 년도
        String ss_grseq = box.getStringDefault("s_grseq", "ALL"); // 교육기수
        String ss_uclass = box.getStringDefault("s_upperclass", "ALL"); // 과목분류
        String ss_mclass = box.getStringDefault("s_middleclass", "ALL"); // 과목분류
        String ss_lclass = box.getStringDefault("s_lowerclass", "ALL"); // 과목분류
        String ss_subjcourse = box.getStringDefault("s_subjcourse", "ALL");// 과목&코스
        String ss_subjseq = box.getStringDefault("s_subjseq", "ALL"); // 과목 기수
        String ss_action = box.getString("s_action");
        String ss_tutornm = box.getString("s_tutornm");  //강사명 
        String v_orderColumn = box.getString("p_orderColumn"); // 정렬할 컬럼명
        if (v_orderColumn.equals("")) {
        	v_orderColumn = "userid";
        }
        v_orderColumn = "b." + v_orderColumn;

        try {
            if (ss_action.equals("go")) {
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                
                sql1= "\n select c.subj "
                    + "\n      , c.year "
                    + "\n      , c.subjnm "
                    + "\n      , c.subjseq "
                    + "\n      , c.edustart "
                    + "\n      , c.eduend "
                    + "\n      , c.isclosed "
                    + "\n      , a.class "
                    + "\n      , a.tuserid "
                    + "\n      , b.name "
                    + "\n      , b.birth_date "
                    + "\n      , b.email "
                    + "\n      , (select count(tuserid) from tz_classtutor where subj=a.subj and year=a.year and subjseq=a.subjseq) ctutorcnt "
                    + "\n      , (select count(userid) from tz_student where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class) stucnt "
                    + "\n      , decode(isjungsan, 'Y', d.inwon, (select count(userid) from tz_student where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class and isgraduated='Y')) grayncnt "
                    + "\n      , decode(isjungsan, 'Y', d.jigub1, b.price) price "
                    + "\n      , d.jigubfee "
                    + "\n      , nvl(d.isjungsan,'N') isjungsan "
                    + "\n from   tz_classtutor a "
                    + "\n      , vz_scsubjseq c "
                    + "\n      , tz_tutor b "
                    + "\n      , tz_tutorgrade d "
                    + "\n where  a.tuserid = b.userid  "
                    + "\n and    a.subj    = c.subj  "
                    + "\n and    a.year    = c.year  "
                    + "\n and    a.subjseq = c.subjseq "  
                    + "\n and    a.subj    = d.subj(+) "
                    + "\n and    a.year    = d.year(+) "
                    + "\n and    a.subjseq = d.subjseq(+) "
                    + "\n and    a.tuserid = d.userid(+) "
                    + "\n and    d.isjungsan = 'Y' ";
                
                if (!ss_grcode.equals("ALL") && !ss_grcode.equals("----"))
                    sql1 += "\n and    c.grcode       = "
                            + SQLString.Format(ss_grcode);
                if (!ss_gyear.equals("ALL"))
                    sql1 += "\n and    c.gyear         = "
                            + SQLString.Format(ss_gyear);
                if (!ss_grseq.equals("ALL"))
                    sql1 += "\n and    c.grseq         = "
                            + SQLString.Format(ss_grseq);
                if (!ss_uclass.equals("ALL"))
                    sql1 += "\n and    c.scupperclass  = "
                            + SQLString.Format(ss_uclass);
                if (!ss_mclass.equals("ALL"))
                    sql1 += "\n and    c.scmiddleclass = "
                            + SQLString.Format(ss_mclass);
                if (!ss_lclass.equals("ALL"))
                    sql1 += "\n and    c.sclowerclass  = "
                            + SQLString.Format(ss_lclass);
                if (!ss_subjcourse.equals("ALL"))
                    sql1 += "\n and    c.scsubj        = "
                            + SQLString.Format(ss_subjcourse);
                if (!ss_subjseq.equals("ALL"))
                    sql1 += "\n and    c.scsubjseq     = "
                            + SQLString.Format(ss_subjseq);
                if (!ss_tutornm.equals(""))
                    sql1 += "\n and    b.name like '%" + ss_tutornm + "%' ";
                            

                sql1 +="\n order  by c.subj, c.year, c.subjseq," + v_orderColumn;
                System.out.println(sql1);
                ls1 = connMgr.executeQuery(sql1);

                while (ls1.next()) {
                    dbox1 = ls1.getDataBox();
                    list1.add(dbox1);
                } // END while
            } // END if

        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (ls1 != null) {
                try {
                    ls1.close();
                } catch (Exception e) {
                }
            }
            if (ls2 != null) {
                try {
                    ls2.close();
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

        return list1;
    }    

    /**
     * 강사활동 상세보기: 활동카운트 2005.02.01 kimsujin
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public DataBox selectTutorActcnt(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
        String sql1 = "";
        DataBox dbox1 = null;

        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_userid = box.getString("p_userid");

        try {
            connMgr = new DBConnectionManager();

            sql1 = "select                                                                                                                                                                            \n"
                    + "        tuserid                                                                                                                                                                   \n";
            sql1 += "      , (select count(luserid)  from TZ_TORONTP    where subj=a.subj and year=a.year and subjseq=a.subjseq and luserid=a.tuserid                          ) torontpcnt            \n";
            sql1 += "      , (select count(luserid)  from TZ_TORON      where subj=a.subj and year=a.year and subjseq=a.subjseq and luserid=a.tuserid                          ) toroncnt              \n";
            // sql1 += " , (select count(inuserid) from TZ_QNA where subj=a.subj
            // and year=a.year and subjseq=a.subjseq and inuserid=a.tuserid and
            // seq > 0 ) qnacnt \n";
            sql1 += ", (select                                          \n";
            sql1 += "        count(boa.userid)                          \n";
            sql1 += "   from                                            \n";
            sql1 += "        tz_bds bds,                              \n";
            sql1 += "        tz_board boa                               \n";
            sql1 += "   where                                                   \n";
            sql1 += "        bds.tabseq = boa.tabseq                            \n";
            sql1 += "    and bds.subj = a.subj and indate between b.edustart and b.eduend\n";
            sql1 += "    and boa.levels > 1                             \n";
            sql1 += "    and bds.type='SQ'                                                                  \n";
            sql1 += "    and boa.userid = a.tuserid             ) qnacnt     --질문방 갯수                                    \n";
            sql1 += "      , (select count(luserid)  from TZ_GONG       where subj=a.subj and year=a.year and subjseq=a.subjseq and luserid=a.tuserid                          ) gongcnt               \n";
            sql1 += "      , (select malevel         from TZ_TUTORGRADE where subj=a.subj and year=a.year and subjseq=a.subjseq and userid=a.tuserid                           ) malevel               \n";
            sql1 += "      , (select count(tuserid)  from TZ_TUTORLOG   where tuserid=a.tuserid  and substr(login,1,8) between substr(b.edustart,1,8) and substr(b.eduend,1,8) ) logincnt              \n";
            sql1 += "      , (select count(userid)   from TZ_DATABOARD  where subj=a.subj and year=a.year and subjseq=a.subjseq and userid=a.tuserid                           ) datacnt               \n";
            sql1 += "  from                                                                                                                                                                            \n"
                    + "        tz_classtutor a ,                                                                                                                                                         \n"
                    + "        TZ_SUBJSEQ b                                                                                                                                                              \n";
            sql1 += " where                                                                                                                                                                            \n"
                    + "        a.subj=b.subj and a.year=b.year and a.subjseq=b.subjseq                                                                                                                   \n";
            sql1 += "   and  a.subj="
                    + SQLString.Format(v_subj)
                    + " and  a.year="
                    + SQLString.Format(v_year)
                    + "                                                                                        \n";
            sql1 += "   and  a.subjseq= "
                    + SQLString.Format(v_subjseq)
                    + " and tuserid="
                    + SQLString.Format(v_userid)
                    + " and rownum=1                                                                  \n";

            System.out.println("강사활동 상세보기: 활동카운트 ====================== \n\n" + sql1 + "\n\n");
            ls1 = connMgr.executeQuery(sql1);
            if (ls1.next()) {
                dbox1 = ls1.getDataBox();
            }

        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (ls1 != null) {
                try {
                    ls1.close();
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

        return dbox1;
    }

    /**
     * 강사활동 상세보기: 활동리스트 2005.02.01 kimsujin
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectTutorActList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
        String sql1 = "", sql2 = "";
        DataBox dbox1 = null;
        ArrayList list1 = null;

        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_userid = box.getString("p_userid");

        try {
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql1 = "select 'gong' gubun, '공지' gubunnm, userid, title, addate, 0 cnt, seq ,'' lesson, '0' types ,'' tpcode";
            sql1 += " from TZ_GONG ";
            sql1 += " where subj=" + SQLString.Format(v_subj) + " and  year="
                    + SQLString.Format(v_year) + " ";
            sql1 += "  and subjseq= " + SQLString.Format(v_subjseq)
                    + " and luserid=" + SQLString.Format(v_userid) + " ";

            sql1 += " union ";

            sql1 += "select 'data' gubun, '자료실' gubunnm, userid, title, indate addate, cnt,  seq ,'' lesson, '0' types ,'' tpcode";
            sql1 += " from TZ_DATABOARD ";
            sql1 += " where subj=" + SQLString.Format(v_subj) + " and  year="
                    + SQLString.Format(v_year) + " ";
            sql1 += "  and subjseq= " + SQLString.Format(v_subjseq)
                    + " and userid=" + SQLString.Format(v_userid) + " ";

            sql1 += " union ";

            sql1 += "select 'qna' gubun, 'Q'||'&'||'A' gubunnm, inuserid userid, title, indate addate , 0 cnt,  seq , lesson, kind types , '' tpcode";
            sql1 += " from TZ_QNA ";
            sql1 += " where  seq > 0  and subj=" + SQLString.Format(v_subj)
                    + " and  year=" + SQLString.Format(v_year) + " ";
            sql1 += "  and subjseq= " + SQLString.Format(v_subjseq)
                    + " and inuserid=" + SQLString.Format(v_userid) + " ";

            sql1 += " union ";

            sql1 += "select 'torontp' gubun, '토론실' gubunnm, luserid, title, addate , cnt,  0 seq , '' lesson, '0' types , tpcode  ";
            sql1 += " from TZ_TORONTP ";
            sql1 += " where subj=" + SQLString.Format(v_subj) + " and  year="
                    + SQLString.Format(v_year) + " ";
            sql1 += "  and subjseq= " + SQLString.Format(v_subjseq)
                    + " and luserid=" + SQLString.Format(v_userid) + " ";

            sql1 += " union ";

            sql1 += "select 'toron' gubun, '토론실' gubunnm, luserid, title, addate , cnt , seq ,'' lesson, '0' types ,tpcode ";
            sql1 += " from TZ_TORON ";
            sql1 += " where subj=" + SQLString.Format(v_subj) + " and  year="
                    + SQLString.Format(v_year) + " ";
            sql1 += "  and subjseq= " + SQLString.Format(v_subjseq)
                    + " and luserid=" + SQLString.Format(v_userid) + " ";
System.out.println("튜터활동리스트 ===== " + sql1);
            ls1 = connMgr.executeQuery(sql1);
            while (ls1.next()) {
                dbox1 = ls1.getDataBox();
                list1.add(dbox1);
            }

        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (ls1 != null) {
                try {
                    ls1.close();
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

        return list1;
    }

    /**
     * 강사활동 상세보기: 로그인 리스트 2005.02.01 kimsujin 해당 과목 기수의 교육기간동안 해당 강사의 로그인 정보를
     * 가져온다.
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectTutorLoginList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
        String sql1 = "", sql2 = "";
        DataBox dbox1 = null;
        ArrayList list1 = null;

        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_userid = box.getString("p_userid");

        try {
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql1 = "select                                                                                                \n"
                    + "       serno,                                                                                         \n"
                    + "       login,                                                                                         \n"
                    + "       logout,                                                                                        \n"
                    + "       loginip,                                                                                       \n"
                    + "       dtime                                                                                          \n"
                    + "  from                                                                                                \n"
                    + "       TZ_TUTORLOG a,                                                                                 \n"
                    + "       TZ_SUBJSEQ b,                                                                                   \n"
                    + "       TZ_CLASSTUTOR c                                                                                 \n"
                    + " where                                                                                                \n"
                    + "       c.tuserid = a.tuserid                                                                          \n"
                    + " and   c.subj = b.subj                                                                                \n"
                    + " and   c.year = b.year                                                                                \n"
                    + " and   c.subjseq = b.subjseq                                                                          \n" 
                    + " and   b.subj="
                    + SQLString.Format(v_subj)
                    + " and  b.year="
                    + SQLString.Format(v_year)
                    + "             \n"
                    + "   and b.subjseq= "
                    + SQLString.Format(v_subjseq)
                    + " and a.tuserid="
                    + SQLString.Format(v_userid)
                    + "  \n"
                    + "   and substr(a.login,1,8) between b.edustart and b.eduend                                            \n"
                    + " order by                                                                                             \n"
                    + "       serno desc                                                                                     \n";

            ls1 = connMgr.executeQuery(sql1);
            while (ls1.next()) {
                dbox1 = ls1.getDataBox();
                list1.add(dbox1);
            }

        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (ls1 != null) {
                try {
                    ls1.close();
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

        return list1;
    }

    /**
     * 강사운영자평가 저장 2005.01.28 kimsujin
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public int calcTutorGrade(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls1 = null;
        ListSet ls2 = null;
        String sql1 = "", sql2 = "", sql3 = "";
        int isOk = 0;
        int i = 0;
        int v_stucnt = 0, v_grayncnt = 0, v_sulsum = 0;
        int v_price = 0, v_addprice = 0, v_jigubfee = 0;
        String v_class = "";

        String s_userid = box.getSession("userid");

        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_userid = box.getString("p_userid");
        String v_malevel = box.getString("p_malevel");

        try {
            connMgr = new DBConnectionManager();

            sql1 = " select subj, year, subjseq, class, b.isgubun, d.price, d.addprice ";
            sql1 += "        , (select count(userid) from TZ_TUTORGRADE where subj=a.subj and year=a.year and subjseq=a.subjseq and userid=a.tuserid) gradecnt";
            sql1 += "        , (select count(userid) from TZ_STUDENT where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class) stucnt";
            sql1 += "        , (select count(userid) from TZ_STUDENT where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class and isgraduated='Y') grayncnt";
            sql1 += "  from TZ_CLASSTUTOR a , TZ_TUTOR b, TZ_TUTORPAY d ";
            sql1 += "  where a.tuserid = b.userid and b.tutorgubun = d.tutorcode and subj="
                    + SQLString.Format(v_subj)
                    + " and  year="
                    + SQLString.Format(v_year) + " ";
            sql1 += "        and subjseq= " + SQLString.Format(v_subjseq)
                    + " and a.tuserid=" + SQLString.Format(v_userid);

            ls1 = connMgr.executeQuery(sql1);

            if (ls1.next()) {

                v_class = ls1.getString("class");

                v_stucnt = ls1.getInt("stucnt");
                v_grayncnt = ls1.getInt("grayncnt");

                v_price = ls1.getInt("price");
                v_addprice = ls1.getInt("addprice");

                v_jigubfee = (v_grayncnt * v_price) + v_addprice;

                // s : == 만족도 점수가져오기 : 설문 == == == == == == == == == == == == == ==

                sql2  = " select avg(distcode7_avg) sulsum ";
                sql2 += " from   tz_suleach a, tz_student b ";
                sql2 += " where  a.subj=a.subj ";
                sql2 += " and    a.year=b.year ";
                sql2 += " and    a.subjseq=b.subjseq ";
                sql2 += " and    a.userid = b.userid ";
                sql2 += " and    a.subj=" + SQLString.Format(v_subj);
                sql2 += " and    a.year=" + SQLString.Format(v_year);
                sql2 += " and    a.subjseq= " + SQLString.Format(v_subjseq);
                sql2 += " and    b.class=" + SQLString.Format(v_class) + "";
                sql2 += " group  by a.subj, a.year, a.subjseq";

                ls2 = connMgr.executeQuery(sql2);

                if (ls2.next()) {
                    v_sulsum = ls2.getInt("sulsum");
                }
                ls2.close();

                // e : == 만족도 점수가져오기 : 설문 == == == == == == == == == == == == == ==

                // 구한 값들로 업데이트 한다 tz_tutorgrade
                if (ls1.getInt("gradecnt") > 0) {
                    sql3 = "update TZ_TUTORGRADE set inwon = ?, malevel = ?, sapoint = ?, jigubfee = ? , luserid = ?, ldate=to_char(sysdate,'yyyymmddhh24miss') ";
                    sql3 += "  where subj=" + SQLString.Format(v_subj)
                            + " and  year=" + SQLString.Format(v_year) + " ";
                    sql3 += "        and subjseq= "
                            + SQLString.Format(v_subjseq) + " and userid="
                            + SQLString.Format(v_userid) + " and rownum=1";
                } else {
                    sql3 = "insert into TZ_TUTORGRADE (subj, year, subjseq, userid, ldate, ";
                    sql3 += " inwon, malevel, sapoint, jigubfee, luserid ) ";
                    sql3 += "values(" + SQLString.Format(v_subj) + ","
                            + SQLString.Format(v_year) + ","
                            + SQLString.Format(v_subjseq) + ", ";
                    sql3 += " " + SQLString.Format(v_userid)
                            + ", to_char(sysdate,'yyyymmddhh24miss'),  ";
                    sql3 += " ?,?,?,?,?)";
                }
                System.out.println("강사운영자평가 저장 시작 \r\n" + sql3
                        + "\r\n강사운영자평가 저장 끝 \r\n");
                pstmt = connMgr.prepareStatement(sql3);
                i = 1;
                pstmt.setInt(i++, v_grayncnt);
                pstmt.setString(i++, v_malevel);
                pstmt.setInt(i++, v_sulsum);
                pstmt.setInt(i++, v_jigubfee);
                pstmt.setString(i++, s_userid);
                isOk = pstmt.executeUpdate();

            } // END while

        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql3);
            throw new Exception("sql3 = " + sql3 + "\r\n" + ex.getMessage());
        } finally {
            if (ls1 != null) {
                try {
                    ls1.close();
                } catch (Exception e) {
                }
            }
            if (ls2 != null) {
                try {
                    ls2.close();
                } catch (Exception e) {
                }
            }
            if(pstmt != null) {
            	try{
            		pstmt.close();
            	}
            	catch (Exception e){}
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

    /**
     * 강사운영자평가 저장 2005.01.28 kimsujin
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     * 
     * public int calcTutorGrade(RequestBox box) throws Exception {
     * DBConnectionManager connMgr = null; PreparedStatement pstmt = null;
     * ListSet ls1 = null; ListSet ls2 = null; String sql1 = "", sql2= "", sql3=
     * ""; int isOk = 0; int i = 0; int v_stucnt = 0, v_sulsum=0;
     * 
     * 
     * int v_grayncnt = 0, v_ctutorcnt=0; int v_inwon=0; int v_grcnt1 =0; int
     * v_grcnt2 =0; int v_grcnt3 =0; int v_grcnt4 =0; int v_grcnt5 =0; int
     * v_inwongrade = 0;
     * 
     * double v_sapoint = 0; int
     * v_okrate1=0,v_okrate2=0,v_okrate3=0,v_okrate4=0,v_okrate5=0; int
     * v_sagrade = 0;
     * 
     * int v_joinpoint = 0, v_joingrade = 0; int
     * v_actrate1=0,v_actrate2=0,v_actrate3=0,v_actrate4=0,v_actrate5=0;
     * 
     * String v_manrate1= "",v_manrate2= "",v_manrate3= "",v_manrate4=
     * "",v_manrate5= ""; int v_magrade = 0;
     * 
     * int v_total = 0; double v_jigub1=0, v_jigub2=0, v_fjigub = 0;
     * 
     * int v_basepay = 0 ; double v_realfee = 0 , v_tax=0 , v_jigubfee=0; String
     * v_isgubuntype= "";
     * 
     * String s_userid = box.getSession("userid");
     * 
     * String v_subj = box.getString("p_subj"); String v_year =
     * box.getString("p_year"); String v_subjseq = box.getString("p_subjseq");
     * String v_userid = box.getString("p_userid"); String v_malevel =
     * box.getString("p_malevel");
     * 
     * try { connMgr = new DBConnectionManager(); DataBox dbox =
     * getMeasure(box);
     * 
     * sql1 = " select b.isgubuntype "; sql1 += " , (select count(userid) from
     * TZ_TUTORGRADE where subj=a.subj and year=a.year and subjseq=a.subjseq and
     * userid=a.tuserid) gradecnt"; sql1 += " , (select count(tuserid) from
     * TZ_CLASSTUTOR where subj=a.subj and year=a.year and subjseq=a.subjseq)
     * ctutorcnt"; sql1 += " , (select count(userid) from TZ_STUDENT where
     * subj=a.subj and year=a.year and subjseq=a.subjseq) stucnt"; sql1 += " ,
     * (select count(userid) from TZ_STOLD where subj=a.subj and year=a.year and
     * subjseq=a.subjseq and isgraduated='Y') grayncnt"; sql1 += " , (select
     * count(ordseq) from TZ_PROJORD where subj=a.subj and year=a.year and
     * subjseq=a.subjseq ) reportcnt"; sql1 += " , (select count(userid) from
     * TZ_DATABOARD where subj=a.subj and year=a.year and subjseq=a.subjseq and
     * userid=a.tuserid) datacnt"; sql1 += " , (select count(userid) from
     * TZ_TORONTP where subj=a.subj and year=a.year and subjseq=a.subjseq and
     * userid=a.tuserid) torontpcnt"; sql1 += " , (select count(userid) from
     * TZ_TORON where subj=a.subj and year=a.year and subjseq=a.subjseq and
     * userid=a.tuserid) toroncnt"; sql1 += " , (select count(inuserid) from
     * TZ_QNA where subj=a.subj and year=a.year and subjseq=a.subjseq and seq >
     * 0 and inuserid=a.tuserid) qnacnt"; sql1 += " , (select count(userid) from
     * TZ_GONG where subj=a.subj and year=a.year and subjseq=a.subjseq and
     * luserid=a.tuserid) gongcnt"; sql1 += " from TZ_CLASSTUTOR a , TZ_TUTOR b ";
     * sql1 += " where a.tuserid = b.userid and subj=" +SQLString.Format(v_subj) + "
     * and year=" +SQLString.Format(v_year) + " "; sql1 += " and subjseq= "
     * +SQLString.Format(v_subjseq) + " and a.tuserid="
     * +SQLString.Format(v_userid) + " and rownum=1";
     * 
     * 
     * ls1 = connMgr.executeQuery(sql1);
     * 
     * if ( ls1.next() ) {
     *  // == 만족도 점수가져오기 : 설문 == == == == == == == == == == == == == == == == == == == == == == //
     * 화면변경 // SulmunResultBean sulbean = new SulmunResultBean(); // int sum =
     * sulbean.getTutorResult(connMgr, v_subj, v_year, v_subjseq); int sum = 0;
     * 
     * v_stucnt = ls1.getInt("stucnt"); v_sapoint = sum;
     * 
     * v_okrate1 = dbox.getInt("c_okrate1"); v_okrate2 =
     * dbox.getInt("c_okrate2"); v_okrate3 = dbox.getInt("c_okrate3"); v_okrate4 =
     * dbox.getInt("c_okrate4"); v_okrate5 = dbox.getInt("c_okrate5");
     * 
     * if ( v_sapoint >= v_okrate1) { v_sagrade = dbox.getInt("va_okrate1"); }
     * else if ( v_sapoint < v_okrate1 && v_sapoint >= v_okrate2) { v_sagrade =
     * dbox.getInt("va_okrate2"); } else if ( v_sapoint < v_okrate2 && v_sapoint >=
     * v_okrate3) { v_sagrade = dbox.getInt("va_okrate3"); } else if ( v_sapoint <
     * v_okrate3 && v_sapoint >= v_okrate4) { v_sagrade =
     * dbox.getInt("va_okrate4"); } else if ( v_sapoint < v_okrate5) { v_sagrade =
     * dbox.getInt("va_okrate5"); }
     *  // 인원 점수 매기기기 : 수료인원*(과제유무에 따라 *1.5) 20050317 kimsujin v_grayncnt =
     * ls1.getInt("grayncnt"); v_ctutorcnt = ls1.getInt("ctutorcnt");
     * 
     * if ( ls1.getInt("reportcnt") > 0) { v_inwon =
     * (int)Math.round((double)(v_grayncnt*1.5)); } else { v_inwon = v_grayncnt; }
     * 
     * v_grcnt1 = dbox.getInt("c_grcnt1"); v_grcnt2 = dbox.getInt("c_grcnt2");
     * v_grcnt3 = dbox.getInt("c_grcnt3"); v_grcnt4 = dbox.getInt("c_grcnt4");
     * v_grcnt5 = dbox.getInt("c_grcnt5");
     * 
     * if ( v_inwon >= v_grcnt1) { v_inwongrade = dbox.getInt("va_grcnt1"); }
     * else if ( v_inwon < v_grcnt1 && v_inwon >= v_grcnt2) { v_inwongrade =
     * dbox.getInt("va_grcnt2"); } else if ( v_inwon < v_grcnt2 && v_inwon >=
     * v_grcnt3) { v_inwongrade = dbox.getInt("va_grcnt3"); } else if ( v_inwon <
     * v_grcnt3 && v_inwon >= v_grcnt4) { v_inwongrade =
     * dbox.getInt("va_grcnt4"); } else if ( v_inwon < v_grcnt5) { v_inwongrade =
     * dbox.getInt("va_grcnt5"); }
     *  // 참여도 점수 매기기 v_joinpoint = ls1.getInt("datacnt") +
     * ls1.getInt("toroncnt") + ls1.getInt("torontpcnt") + ls1.getInt("qnacnt") +
     * ls1.getInt("gongcnt") ;
     * 
     * v_actrate1 = dbox.getInt("c_actrate1"); v_actrate2 =
     * dbox.getInt("c_actrate2"); v_actrate3 = dbox.getInt("c_actrate3");
     * v_actrate4 = dbox.getInt("c_actrate4"); v_actrate5 =
     * dbox.getInt("c_actrate5");
     * 
     * if ( v_joinpoint >= v_actrate1) { v_joingrade =
     * dbox.getInt("va_actrate1"); } else if ( v_joinpoint < v_actrate1 &&
     * v_joinpoint >= v_actrate2) { v_joingrade = dbox.getInt("va_actrate2"); }
     * else if ( v_joinpoint < v_actrate2 && v_joinpoint >= v_actrate3) {
     * v_joingrade = dbox.getInt("va_actrate3"); } else if ( v_joinpoint <
     * v_actrate3 && v_joinpoint >= v_actrate4) { v_joingrade =
     * dbox.getInt("va_actrate4"); } else if ( v_joinpoint < v_actrate5) {
     * v_joingrade = dbox.getInt("va_actrate5"); }
     *  // 강사 평가점수 v_manrate1 = dbox.getString("c_manrate1"); v_manrate2 =
     * dbox.getString("c_manrate2"); v_manrate3 = dbox.getString("c_manrate3");
     * v_manrate4 = dbox.getString("c_manrate4"); v_manrate5 =
     * dbox.getString("c_manrate5");
     * 
     * if ( v_malevel.equals(v_manrate1)) { v_magrade =
     * dbox.getInt("va_manrate1"); } else if ( v_malevel.equals(v_manrate2)) {
     * v_magrade = dbox.getInt("va_manrate2"); } else if (
     * v_malevel.equals(v_manrate3)) { v_magrade = dbox.getInt("va_manrate3"); }
     * else if ( v_malevel.equals(v_manrate4)) { v_magrade =
     * dbox.getInt("va_manrate4"); } else if ( v_malevel.equals(v_manrate5)) {
     * v_magrade = dbox.getInt("va_manrate5"); }
     * 
     *  // 1차 지급율: 과제가 있으면 // v_jigub1 : 합산점수 // v_jigub2 : 2차 지급율 - 과제 포인트적용
     * 잇으면 *1.5 // v_fjigub : 최종 지급율 - 참여도가 0점이면 0
     * 
     * v_jigub1 = (double)Math.round((double)(v_inwongrade + v_sagrade +
     * v_joingrade + v_magrade)/100*100)/100 ;
     * 
     * v_jigub2 = v_jigub1;
     *  // 과제가 없고 총점이 100을 넘을경우에는 점수가 100이상이 될수 없다 if ( ls1.getInt("reportcnt") ==
     * 0 && v_jigub1 > 1) { v_jigub2 = 1; }
     *  // 2차 지급율 v_fjigub = v_jigub2; if ( v_joingrade == 0) { // 참여도 점수가 0이면
     * 지급율도 0이다 v_fjigub = 0; }
     *  // 지급금액 : 기준금액 - (기준금액*tax + (기준금액*tax)*0.1) v_basepay =
     * dbox.getInt("va_basepay"); v_isgubuntype = ls1.getString("isgubuntype");
     * 
     * if ( v_isgubuntype.equals(dbox.getString("c_tax1"))) { v_tax =
     * dbox.getDouble("va_tax1"); } else if (
     * v_isgubuntype.equals(dbox.getString("c_tax2"))) { v_tax =
     * dbox.getDouble("va_tax2"); } else if (
     * v_isgubuntype.equals(dbox.getString("c_tax3"))) { v_tax =
     * dbox.getDouble("va_tax3"); }
     * 
     * v_jigubfee = v_basepay * v_fjigub; v_realfee = v_jigubfee +
     * (int)(v_jigubfee/0.78*(v_tax/100)) +
     * (int)(v_jigubfee/0.78*(v_tax/100)*0.1);
     * 
     *  // 구한 값들로 업데이트 한다 tz_tutorgrade if ( ls1.getInt("gradecnt") > 0 ) { sql3 =
     * "update TZ_TUTORGRADE set inwon=? ,inwongrade =? ,sapoint =? ,sagrade =?
     * ,joinpoint =? ,joingrade =?,malevel =? "; sql3 +=" , magrade =? ,jigub1 =?
     * ,jigub2 =? ,fjigub =? ,jigubfee=? ,realfee=?, luserid =?
     * ,ldate=to_char(sysdate,'yyyymmddhh24miss') "; sql3 += " where subj="
     * +SQLString.Format(v_subj) + " and year=" +SQLString.Format(v_year) + " ";
     * sql3 += " and subjseq= " +SQLString.Format(v_subjseq) + " and userid="
     * +SQLString.Format(v_userid) + " and rownum=1"; } else { sql3 = "insert
     * into TZ_TUTORGRADE (subj, year, subjseq,userid, ldate, "; sql3 += "
     * inwon, inwongrade, sapoint, sagrade, joinpoint,joingrade, malevel, ";
     * sql3 += " magrade, jigub1, jigub2, fjigub, jigubfee, realfee,luserid ) ";
     * sql3 += " values(" +SQLString.Format(v_subj) + ","
     * +SQLString.Format(v_year) + "," +SQLString.Format(v_subjseq) + " "; sql3 += " , "
     * +SQLString.Format(v_userid) + ", to_char(sysdate,'yyyymmddhh24miss') ";
     * sql3 += " , ?,?,?,?,?,?,?,?,?,?,?,?,?,? )"; } pstmt =
     * connMgr.prepareStatement(sql3); i = 1; pstmt.setInt(i++,v_inwon);
     * pstmt.setInt(i++,v_inwongrade); pstmt.setDouble(i++,v_sapoint);
     * pstmt.setInt(i++,v_sagrade); pstmt.setInt(i++,v_joinpoint);
     * pstmt.setInt(i++,v_joingrade); pstmt.setString(i++,v_malevel);
     * pstmt.setInt(i++,v_magrade); pstmt.setDouble(i++,v_jigub1);
     * pstmt.setDouble(i++,v_jigub2); pstmt.setDouble(i++,v_fjigub);
     * pstmt.setInt(i++,(int)v_jigubfee); pstmt.setInt(i++,(int)v_realfee);
     * pstmt.setString(i++,s_userid);
     * 
     * isOk = pstmt.executeUpdate();
     *  } // END while
     *  // 로그 // LogDB.insertLog(box, "insert", "TZ_TUTORGRADE", v_subj + ","
     * +v_year + "," +v_subjseq + "," +v_userid, "강사평가저장");
     * 
     * 
     *  } catch ( Exception ex ) { ErrorManager.getErrorStackTrace(ex, box,
     * sql3); throw new Exception("sql3 = " + sql3 + "\r\n" + ex.getMessage() ); }
     * finally { if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
     * if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } } if (
     * connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception
     * e10 ) { } } }
     * 
     * return isOk; }
     */

    /**
     * 강사활동관리 리스트 2005.02.01 kimsujin
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectTutorActionList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
        ListSet ls2 = null;
        ArrayList list1 = null;
        String sql = "";
        String sql1 = "";
        DataBox dbox1 = null;

        String v_process = box.getString("p_process");
        String v_user_id = box.getSession("userid"); // 로그인 사용자

        String ss_edustart = (!box.getString("p_edustart").equals("")) ? (box
                .getString("p_edustart")).replaceAll("-", "")
                + "00" : ""; // 학습시작일
        String ss_eduend = (!box.getString("p_eduend").equals("")) ? (box
                .getString("p_eduend")).replaceAll("-", "")
                + "24" : ""; // 학습종료일

        String ss_grcode = box.getStringDefault("s_grcode", "ALL"); // 교육그룹
        String ss_gyear = box.getStringDefault("s_gyear", "ALL"); // 년도
        String ss_grseq = box.getStringDefault("s_grseq", "ALL"); // 교육기수
        String ss_uclass = box.getStringDefault("s_upperclass", "ALL"); // 과목분류
        String ss_mclass = box.getStringDefault("s_middleclass", "ALL"); // 과목분류
        String ss_lclass = box.getStringDefault("s_lowerclass", "ALL"); // 과목분류
        String ss_subjcourse = box.getStringDefault("s_subjcourse", "ALL"); // 과목&코스
        String ss_subjseq = box.getStringDefault("s_subjseq", "ALL"); // 과목 기수
        String ss_action = box.getString("s_action");
        String v_isclosed = box.getString("p_isclosed");

        String v_orderColumn = box.getString("p_orderColumn"); // 정렬할 컬럼명
        String v_orderType = box.getString("p_orderType"); // 정렬할 순서

        try {
            if (ss_action.equals("go")) {
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                
                sql1 += "select\n";
                sql1 += "      aa.grcode,                    /* 교육그룹코드 */\n";
                sql1 += "      aa.rep_chk,                    /* 리포트출제여부 */\n";
                sql1 += "      aa.grseq,                        /* 교육그룹차수 */\n";
                sql1 += "      aa.subj,                         /* 과목코드 */\n";
                sql1 += "      aa.year,                         /* 년도 */\n";
                sql1 += "      aa.subjnm,                       /* 과목명 */\n";
                sql1 += "      aa.subjseq,                      /* 과목기수 */\n";
                sql1 += "      aa.edustart,                     /* 교육시작일 */\n";
                sql1 += "      aa.eduend,                       /* 교육종료일 */    \n";
                sql1 += "      aa.isclosed,                     /* 교육완료여부 */\n";
                sql1 += "      aa.ldate,\n";
                sql1 += "      aa.class,                        /* 클래스 */      \n";
                sql1 += "      aa.tuserid,                      /* 강사아이디 */\n";
                sql1 += "      aa.name,                         /* 강사명 */\n";
                sql1 += "      aa.birth_date,                        /* 강사주민등록번호 */\n";
                sql1 += "      aa.email,                        /* 강사이메일 */\n";
                sql1 += "      aa.tutorgubun,\n";
                sql1 += "      aa.price,\n";
                sql1 += "      aa.addprice,   \n";
                sql1 += "      aa.ttype,                        /*  */\n";
                sql1 += "      nvl(mm.cnt, 0) ctutorcnt,        /* 과목기수별강사수 */\n";
                sql1 += "      nvl(bb.cnt, 0) stucnt,           /* 입과인원 */\n";
                sql1 += "      nvl(nn.cnt, 0) grayncnt,         /* 수료생수 */\n";
                sql1 += "      '' reportcnt,                    /* 과제건수 */\n";
                sql1 += "      nvl(hh.cnt, 0) repcnt,           /* 과제제출건수 */\n";
                sql1 += "      '' subjrepavg,                   /* 과제평균점수 */\n";
                sql1 += "      nvl(ee.cnt, 0) datacnt,          /* 자료방등록갯수 */\n";
                sql1 += "      nvl(ff.cnt, 0) torontpcnt,       /* 토론발의건수 */\n";
                sql1 += "      nvl(gg.cnt, 0) toroncnt,         /* 토론참여건수 */\n";
                sql1 += "      nvl(cc.cnt, 0) gongcnt,          /* 공지사항등록갯수 */\n";
                sql1 += "      '' exam_descnt,                  /* 평가갯수 */\n";
                sql1 += "      nvl(dd.cnt, 0) logincnt,         /* 강사접속횟수 */\n";
                sql1 += "      nvl(jj.cnt, 0)-nvl(ll.cnt, 0) noanscnt,         /* 질문방미응답갯수 */\n";
                sql1 += "      nvl(ii.cnt, 0) noscorecnt,       /* 과제미평가갯수 */\n";
                sql1 += "      '' pay,                          /* 강사료 */\n";
                sql1 += "      aa.grcodenm,                     /* 교육그룹명 */\n";
                sql1 += "      nvl(jj.cnt, 0) qnacnt            /* 질문방질문갯수 */      \n";
                sql1 += "  from\n";
                sql1 += "      (\n";
                sql1 += "       select\n";
                sql1 += "              d.grcode,\n";
                sql1 += "              c.grseq,                       \n";
                sql1 += "              d.grcodenm,        \n";
                sql1 += "              c.subj,            \n";
                sql1 += "              c.year,            \n";
                sql1 += "              c.subjnm,          \n";
                sql1 += "              c.subjseq,         \n";
                sql1 += "              c.edustart,        \n";
                sql1 += "              c.eduend,          \n";
                sql1 += "              a.class,           \n";
                sql1 += "              a.tuserid,         \n";
                sql1 += "              b.name,            \n";
                sql1 += "              c.isclosed,\n";
                sql1 += "              c.ldate,\n";
                sql1 += "              b.birth_date,\n";
                sql1 += "              b.email,\n";
                sql1 += "              b.tutorgubun,\n";
                sql1 += "              d.price,\n";
                sql1 += "              d.addprice,\n";
                sql1 += "              a.ttype\n,";
                sql1 += "              c.scupperclass,\n";
                sql1 += "              c.scmiddleclass,\n";
              // 추가   
                sql1 += "    CASE WHEN WREPORT = 100 THEN (               \n";
                sql1 += "    SELECT MAX(DECODE(PROJGUBUN,'M','1차','')) || MIN(DECODE(PROJGUBUN,'F','2차',''))  FROM TZ_PROJGRP Z  \n";
                sql1 += "    WHERE A.YEAR=Z.YEAR AND A.SUBJ=Z.SUBJ AND A.SUBJSEQ=Z.SUBJSEQ   \n";
                sql1 += "    )                \n";
                sql1 += "    WHEN WREPORT BETWEEN 40 AND  50  THEN (               \n";
                sql1 += "    SELECT MAX(DECODE(PROJGUBUN,'F','1차',''))  FROM TZ_PROJGRP Z                \n";
                sql1 += "    WHERE A.YEAR=Z.YEAR AND A.SUBJ=Z.SUBJ AND A.SUBJSEQ=Z.SUBJSEQ )  ELSE '' END rep_chk,               \n";
                
                sql1 += "              c.sclowerclass,\n";
                sql1 += "              c.gyear\n";
                sql1 += "         from\n";
                sql1 += "              tz_classtutor a,\n";
                sql1 += "              tz_tutor b,\n";
                sql1 += "              vz_scsubjseq c,\n";
                sql1 += "              tz_grcode d,\n";
                sql1 += "              tz_tutorpay d\n";
                sql1 += "        where\n";
                sql1 += "              a.tuserid = b.userid\n";
                sql1 += "          and a.subj = c.subj and a.year = c.year and a.subjseq = c.subjseq ";
                if (!ss_grcode.equals("ALL"))
                    sql1 += "          and c.grcode = '" + ss_grcode + "' ";
                if (!ss_gyear.equals("ALL"))
                    sql1 += "          and c.gyear = '" + ss_gyear + "' ";
                if (!ss_grseq.equals("ALL"))
                    sql1 += "          and c.grseq = '" + ss_grseq + "'\n";
                sql1 += "          and c.grcode = d.grcode(+) \n";
                sql1 += "          and b.isgubun = d.tutorcode(+) \n";
                sql1 += "      ) aa,\n";
                sql1 += "      (\n";
                sql1 += "       select\n";
                sql1 += "              a.subj,\n";
                sql1 += "              a.year,\n";
                sql1 += "              a.subjseq,\n";
                sql1 += "              a.class,\n";
                sql1 += "              count(a.userid) cnt\n";
                sql1 += "         from\n";
                sql1 += "              tz_student a,\n";
                sql1 += "              vz_scsubjseq b\n";
                sql1 += "        where 1=1\n";
                if (!ss_grcode.equals("ALL"))
                    sql1 += "          and b.grcode = '" + ss_grcode + "' ";
                if (!ss_gyear.equals("ALL"))
                    sql1 += "          and b.gyear = '" + ss_gyear + "' ";
                if (!ss_grseq.equals("ALL"))
                    sql1 += "          and b.grseq = '" + ss_grseq + "'\n";
                sql1 += "          and a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq\n";
                sql1 += "        group by\n";
                sql1 += "              a.subj, a.year, a.subjseq, a.class\n";
                sql1 += "      ) bb,   /* 클래스별 학생수 */\n";
                sql1 += "      (\n";
                sql1 += "       select\n";
                sql1 += "              a.subj,\n";
                sql1 += "              a.year,\n";
                sql1 += "              a.subjseq,\n";
                sql1 += "              a.userid tuserid,\n";
                sql1 += "              count(a.userid) cnt\n";
                sql1 += "         from\n";
                sql1 += "              tz_gong a,\n";
                sql1 += "              vz_scsubjseq b\n";
                sql1 += "        where\n";
                sql1 += "              a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq and a.addate between b.edustart and b.eduend      \n";
                if (!ss_grcode.equals("ALL"))
                    sql1 += "          and b.grcode = '" + ss_grcode + "' ";
                if (!ss_gyear.equals("ALL"))
                    sql1 += "          and b.gyear = '" + ss_gyear + "' ";
                if (!ss_grseq.equals("ALL"))
                    sql1 += "          and b.grseq = '" + ss_grseq + "'\n";
                sql1 += "        group by \n";
                sql1 += "              a.subj, a.year, a.subjseq, a.userid\n";
                sql1 += "      ) cc,       /* 강사가 올린 공지사항 글갯수 */\n";
                sql1 += "      (\n";
                sql1 += "       select       \n";
                sql1 += "              b.subj,\n";
                sql1 += "              b.year,\n";
                sql1 += "              b.subjseq,\n";
                sql1 += "              c.class,\n";
                sql1 += "              a.tuserid,\n";
                sql1 += "              count(a.tuserid) cnt\n";
                sql1 += "         from  \n";
                sql1 += "              tz_tutorlog a,\n";
                sql1 += "              vz_scsubjseq b,\n";
                sql1 += "              tz_classtutor c\n";
                sql1 += "        where 1=1 \n";
                if (!ss_grcode.equals("ALL"))
                    sql1 += "          and b.grcode = '" + ss_grcode + "' ";
                if (!ss_gyear.equals("ALL"))
                    sql1 += "          and b.gyear = '" + ss_gyear + "' ";
                if (!ss_grseq.equals("ALL"))
                    sql1 += "          and b.grseq = '" + ss_grseq + "'\n";
                sql1 += "          and substr(a.login,1,10) between b.edustart and b.eduend\n";
                sql1 += "          and b.subj = c.subj and b.year = c.year and b.subjseq = c.subjseq\n";
                sql1 += "          and a.tuserid = c.tuserid\n";
                sql1 += "        group by\n";
                sql1 += "              b.subj, b.year, b.subjseq, c.class, a.tuserid\n";
                sql1 += "      ) dd,       /* 강사 로그인 횟수 */\n";
                sql1 += "      (\n";
                sql1 += "       select\n";
                sql1 += "              a.subj,\n";
                sql1 += "              b.userid tuserid,\n";
                sql1 += "              count(b.userid) cnt \n";
                sql1 += "         from\n";
                sql1 += "              tz_bds a,\n";
                sql1 += "              tz_board b,\n";
                sql1 += "              vz_scsubjseq c,\n";
                sql1 += "              tz_classtutor d\n";
                sql1 += "        where\n";
                sql1 += "              a.tabseq = b.tabseq and a.type='SD'\n";
                sql1 += "          and a.subj = c.subj ";
                if (!ss_grcode.equals("ALL"))
                    sql1 += "          and c.grcode = '" + ss_grcode + "' ";
                if (!ss_gyear.equals("ALL"))
                    sql1 += "          and c.gyear = '" + ss_gyear + "' ";
                if (!ss_grseq.equals("ALL"))
                    sql1 += "          and c.grseq = '" + ss_grseq + "'\n";
                sql1 += "          and substr(b.INDATE, 1, 10) between c.edustart and c.eduend\n";
                sql1 += "          and c.subj = d.subj and c.year = d.year and c.subjseq = d.subjseq\n";
                sql1 += "        group by\n";
                sql1 += "              a.subj, b.userid\n";
                sql1 += "      ) ee,       /* 자료방 등록 글수 */\n";
                sql1 += "      (\n";
                sql1 += "       select \n";
                sql1 += "              a.subj,\n";
                sql1 += "              a.year,\n";
                sql1 += "              a.subjseq,\n";
                sql1 += "              a.luserid,\n";
                sql1 += "              count(a.luserid) cnt\n";
                sql1 += "         from\n";
                sql1 += "              tz_torontp a,\n";
                sql1 += "              vz_scsubjseq b\n";
                sql1 += "        where\n";
                sql1 += "              a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq\n";
                if (!ss_grcode.equals("ALL"))
                    sql1 += "          and b.grcode = '" + ss_grcode + "' ";
                if (!ss_gyear.equals("ALL"))
                    sql1 += "          and b.gyear = '" + ss_gyear + "' ";
                if (!ss_grseq.equals("ALL"))
                    sql1 += "          and b.grseq = '" + ss_grseq + "'\n";
                sql1 += "        group by\n";
                sql1 += "              a.subj, a.year, a.subjseq, a.luserid\n";
                sql1 += "      ) ff,       /* 토론 게시 건수 */\n";
                sql1 += "      (\n";
                sql1 += "       select \n";
                sql1 += "              a.subj,\n";
                sql1 += "              a.year,\n";
                sql1 += "              a.subjseq,\n";
                sql1 += "              c.luserid,\n";
                sql1 += "              count(c.luserid) cnt\n";
                sql1 += "         from\n";
                sql1 += "              tz_torontp a,\n";
                sql1 += "              vz_scsubjseq b,\n";
                sql1 += "              tz_toron c\n";
                sql1 += "        where\n";
                sql1 += "              a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq\n";
                if (!ss_grcode.equals("ALL"))
                    sql1 += "          and b.grcode = '" + ss_grcode + "' ";
                if (!ss_gyear.equals("ALL"))
                    sql1 += "          and b.gyear = '" + ss_gyear + "' ";
                if (!ss_grseq.equals("ALL"))
                    sql1 += "          and b.grseq = '" + ss_grseq + "'\n";
                sql1 += "          and a.subj = c.subj and a.year = c.year and a.subjseq = c.subjseq and a.tpcode = c.tpcode\n";
                sql1 += "        group by\n";
                sql1 += "              a.subj, a.year, a.subjseq, c.luserid\n";
                sql1 += "      ) gg,       /* 토론 의견 건수 */      \n";
                sql1 += "      (\n";
                sql1 += "       select subj \n";
                sql1 += "            , year \n";
                sql1 += "            , subjseq \n";
            	sql1 += "            , class \n";
        		sql1 += "            , sum(cnt) as cnt \n";
    			sql1 += "       from   ( \n";
				sql1 += "               select a.subj \n";
				sql1 += "                    , a.year \n";
				sql1 += "                    , a.subjseq \n";
				sql1 += "                    , b.class \n";
				sql1 += "                    , a.projid \n";
				sql1 += "                    , count(distinct a.grpseq) as cnt \n";
				sql1 += "               from   tz_projrep a \n";
				sql1 += "                    , tz_student b \n";
				sql1 += "                    , tz_classtutor c \n";
				sql1 += "               where  a.subj = b.subj \n";
				sql1 += "               and    a.year = b.year \n";
				sql1 += "               and    a.subjseq = b.subjseq \n";
				sql1 += "               and    a.projid = b.userid \n";
				sql1 += "               and    b.subj = c.subj \n";
				sql1 += "               and    b.year = c.year \n";
				sql1 += "               and    b.subjseq = c.subjseq \n";
				sql1 += "               and    b.class = c.class \n";
				sql1 += "               and    a.isfinal = 'Y' \n";
				sql1 += "               group  by a.subj, a.year, a.subjseq, b.class, a.projid \n";
				sql1 += "              ) \n";
				sql1 += "       group  by subj, year, subjseq, class \n";
                sql1 += "      ) hh,       /* 제출된 과제 건수 */\n";
                sql1 += "      (\n";
                sql1 += "       select subj \n";
                sql1 += "            , year \n";
                sql1 += "            , subjseq \n";
            	sql1 += "            , class \n";
        		sql1 += "            , sum(cnt) as cnt \n";
    			sql1 += "       from   ( \n";
				sql1 += "               select a.subj \n";
				sql1 += "                    , a.year \n";
				sql1 += "                    , a.subjseq \n";
				sql1 += "                    , b.class \n";
				sql1 += "                    , a.projid \n";
				sql1 += "                    , count(distinct a.grpseq) as cnt \n";
				sql1 += "               from   tz_projrep a \n";
				sql1 += "                    , tz_student b \n";
				sql1 += "                    , tz_classtutor c \n";
				sql1 += "               where  a.subj = b.subj \n";
				sql1 += "               and    a.year = b.year \n";
				sql1 += "               and    a.subjseq = b.subjseq \n";
				sql1 += "               and    a.projid = b.userid \n";
				sql1 += "               and    b.subj = c.subj \n";
				sql1 += "               and    b.year = c.year \n";
				sql1 += "               and    b.subjseq = c.subjseq \n";
				sql1 += "               and    b.class = c.class \n";
				sql1 += "               and    a.isfinal = 'Y' \n";
				sql1 += "               and    a.score is null \n";
				sql1 += "               group  by a.subj, a.year, a.subjseq, b.class, a.projid \n";
				sql1 += "              ) \n";
				sql1 += "       group  by subj, year, subjseq, class \n";
                sql1 += "      ) ii,       /* 미평가된 과제 건수 */\n";
                sql1 += "      (\n";
                sql1 += "       select d.subj \n";
                sql1 += "            , d.year \n";
                sql1 += "            , d.subjseq \n";
                sql1 += "            , d.class \n";
                sql1 += "            , count(b.seq) cnt \n";
                sql1 += "       from   tz_bds a \n";
                sql1 += "            , tz_board b \n";
                sql1 += "            , vz_scsubjseq c \n";
                sql1 += "            , tz_student d \n";
                sql1 += "       where  a.tabseq = b.tabseq \n"; 
                sql1 += "       and    a.type='SQ' \n";
                sql1 += "       and    b.seq = b.refseq \n";
                sql1 += "       and    a.subj = c.subj \n";
                sql1 += "       and    b.subj = c.subj \n";
                sql1 += "       and    b.year = c.year \n";
                sql1 += "       and    b.subjseq = c.subjseq \n";
                sql1 += "       and    c.subj = d.subj \n";
                sql1 += "       and    c.year = d.year \n";
                sql1 += "       and    c.subjseq = d.subjseq \n";
                sql1 += "       and    b.userid = d.userid \n";       
                sql1 += "       and    substr(b.indate, 1, 10) between c.edustart and c.eduend \n";
                sql1 += "       group  by d.subj, d.year, d.subjseq, d.class \n";
                sql1 += "      ) jj,   /* 질문방 질문 건수 */\n";
                sql1 += "      ( \n";
                sql1 += "       select subj, year, subjseq, class, sum(cnt) as cnt \n";
                sql1 += "       from   ( \n";
                sql1 += "              select a.subj, a.year, a.subjseq, a.class, a.seq, decode(count(b.seq),0,0,1) as cnt \n";
                sql1 += "              from   ( \n";
                sql1 += "                     select d.subj \n";
                sql1 += "                          , d.year \n";
                sql1 += "                          , d.subjseq \n";
                sql1 += "                          , d.class \n";
                sql1 += "                          , b.tabseq \n";
                sql1 += "                          , b.seq \n";
                sql1 += "                     from   tz_bds a \n";
                sql1 += "                          , tz_board b \n";
                sql1 += "                          , vz_scsubjseq c \n";
                sql1 += "                          , tz_student d \n";
                sql1 += "                     where  a.tabseq = b.tabseq \n"; 
                sql1 += "                     and    a.type='SQ' \n";
                sql1 += "                     and    b.seq = b.refseq \n";
                sql1 += "                     and    a.subj = c.subj \n";     
                sql1 += "                     and    b.subj = c.subj \n";
                sql1 += "                     and    b.year = c.year \n";
                sql1 += "                     and    b.subjseq = c.subjseq \n";
                sql1 += "                     and    c.subj = d.subj \n";
                sql1 += "                     and    c.year = d.year \n";
                sql1 += "                     and    c.subjseq = d.subjseq \n";
                sql1 += "                     and    b.userid = d.userid \n";
                sql1 += "                     and    substr(b.indate, 1, 10) between c.edustart and c.eduend \n";
                sql1 += "                     ) a -- 질문한것 \n";
                sql1 += "                   , tz_board b \n";
                sql1 += "              where  a.tabseq = b.tabseq \n";
                sql1 += "              and    a.seq = b.refseq \n";
                sql1 += "              and    a.seq <> b.seq \n";
                sql1 += "              group  by a.subj, a.year, a.subjseq, a.class, a.seq \n";
                sql1 += "              ) \n";
                sql1 += "       group  by subj, year, subjseq, class \n";
                sql1 += "      ) ll,       /* 질문방 응답 건수(질문하나에 여러답변이 등록될경우 한개이상이면 하나로 센다) */\n";
                sql1 += "      (\n";
                sql1 += "       select    \n";
                sql1 += "              c.SUBJ,            \n";
                sql1 += "              c.year,  \n";
                sql1 += "              c.subjseq,     \n";
                sql1 += "              count(a.TUSERID) cnt  \n";
                sql1 += "         from\n";
                sql1 += "              tz_classtutor a,\n";
                sql1 += "              tz_tutor b,\n";
                sql1 += "              vz_scsubjseq c,\n";
                sql1 += "              tz_grcode d,\n";
                sql1 += "              tz_code e\n";
                sql1 += "        where\n";
                sql1 += "              a.tuserid = b.userid\n";
                sql1 += "          and a.subj = c.subj and a.year = c.year and a.subjseq = c.subjseq ";
                if (!ss_grcode.equals("ALL"))
                    sql1 += "          and c.grcode = '" + ss_grcode + "' ";
                if (!ss_gyear.equals("ALL"))
                    sql1 += "          and c.gyear = '" + ss_gyear + "' ";
                if (!ss_grseq.equals("ALL"))
                    sql1 += "          and c.grseq = '" + ss_grseq + "'\n";
                sql1 += "          and c.grcode = d.grcode\n";
                sql1 += "          and b.isgubun = e.code and e.gubun = '0060'\n";
                sql1 += "        group by\n";
                sql1 += "              c.subj, c.year, c.subjseq\n";
                sql1 += "      ) mm,       /* 과목기수별 강사수 */\n";
                sql1 += "      (\n";
                sql1 += "       select \n";
                sql1 += "              subj, \n";
                sql1 += "              year,\n";
                sql1 += "              subjseq,\n";
                sql1 += "              class,\n";
                sql1 += "              count(userid) cnt \n";
                sql1 += "         from \n";
                sql1 += "              TZ_STUDENT \n";
                sql1 += "        where \n";
                sql1 += "              ISGRADUATED='Y'\n";
                sql1 += "        group by\n";
                sql1 += "              subj, year, subjseq, class\n";
                sql1 += "      ) nn    /* 수료생수 */\n";
                sql1 += " where\n";
                sql1 += "       aa.subj = bb.subj(+) and aa.year = bb.year(+) and aa.subjseq = bb.subjseq(+)                                and aa.class = bb.class(+) \n";
                sql1 += "   and aa.subj = cc.subj(+) and aa.year = cc.year(+) and aa.subjseq = cc.subjseq(+) and aa.tuserid = cc.tuserid(+) \n";
                sql1 += "   and aa.subj = dd.subj(+) and aa.year = dd.year(+) and aa.subjseq = dd.subjseq(+) and aa.tuserid = dd.tuserid(+) and aa.class = dd.class(+) \n";
                sql1 += "   and aa.subj = ee.subj(+)                                                         and aa.tuserid = ee.tuserid(+) \n";
                sql1 += "   and aa.subj = ff.subj(+) and aa.year = ff.year(+) and aa.subjseq = ff.subjseq(+) and aa.tuserid = ff.luserid(+) \n";
                sql1 += "   and aa.subj = gg.subj(+) and aa.year = gg.year(+) and aa.subjseq = gg.subjseq(+) and aa.tuserid = gg.luserid(+) \n";
                sql1 += "   and aa.subj = hh.subj(+) and aa.year = hh.year(+) and aa.subjseq = hh.subjseq(+)                                and aa.class = hh.clasS(+) \n";
                sql1 += "   and aa.subj = ii.subj(+) and aa.year = ii.year(+) and aa.subjseq = ii.subjseq(+)                                and aa.class = ii.class(+) \n";
                sql1 += "   and aa.subj = jj.subj(+) and aa.year = jj.year(+) and aa.subjseq = jj.subjseq(+)                                and aa.class = jj.class(+) \n";
                sql1 += "   and aa.subj = ll.subj(+) and aa.year = ll.year(+) and aa.subjseq = ll.subjseq(+)                                and aa.class = ll.class(+) \n";
                sql1 += "   and aa.subj = mm.subj(+) and aa.year = mm.year(+) and aa.subjseq = mm.subjseq(+) \n";
                sql1 += "   and aa.subj = nn.subj(+) and aa.year = nn.year(+) and aa.subjseq = nn.subjseq(+)                                and aa.class = nn.class(+) \n";

              
                
                if (v_process.equals("listPage2"))
                    sql1 += "    and aa.tuserid = "
                            + SQLString.Format(v_user_id) + "\n"; // v_user_id(강사
                                                                    // 로그인 아이디)
                if (!v_isclosed.equals(""))
                    sql1 += "    and aa.isclosed = "
                            + SQLString.Format(v_isclosed) + "\n"; // v_user_id(강사
                                                                    // 로그인 아이디)
                if (!ss_grcode.equals("ALL") && !ss_grcode.equals("----") && !ss_grcode.equals(""))
                    sql1 += "    and aa.grcode       = "
                            + SQLString.Format(ss_grcode) + "\n";
                if (!ss_gyear.equals("ALL") && !ss_gyear.equals(""))
                    sql1 += "    and aa.gyear        = "
                            + SQLString.Format(ss_gyear) + "\n";
                if (!ss_grseq.equals("ALL") && !ss_grseq.equals(""))
                    sql1 += "    and aa.grseq        = "
                            + SQLString.Format(ss_grseq) + "\n";
                if (!ss_uclass.equals("ALL") && !ss_uclass.equals(""))
                    sql1 += "    and aa.scupperclass = "
                            + SQLString.Format(ss_uclass) + "\n";
                if (!ss_mclass.equals("ALL") && !ss_mclass.equals(""))
                    sql1 += "    and aa.scmiddleclass = "
                            + SQLString.Format(ss_mclass) + "\n";
                if (!ss_lclass.equals("ALL") && !ss_lclass.equals(""))
                    sql1 += "    and aa.sclowerclass = "
                            + SQLString.Format(ss_lclass) + "\n";
                if (!ss_subjcourse.equals("ALL") && !ss_subjcourse.equals(""))
                    sql1 += "    and aa.subj       = "
                            + SQLString.Format(ss_subjcourse) + "\n";
                if (!ss_subjseq.equals("ALL") && !ss_subjseq.equals(""))
                    sql1 += "    and aa.subjseq    = "
                            + SQLString.Format(ss_subjseq) + "\n";
                if (!ss_edustart.equals(""))
                    sql1 += "    and aa.edustart >= "
                            + SQLString.Format(ss_edustart) + "\n";
                if (!ss_eduend.equals(""))
                    sql1 += "    and aa.eduend <= "
                            + SQLString.Format(ss_eduend) + "\n";

                if (v_orderColumn.equals("")) {
                    sql1 += "order by aa.subj asc, aa.year asc, aa.subjseq asc, aa.name asc, aa.edustart desc \n";
                } else {
                    sql1 += "order by " + v_orderColumn + v_orderType + "\n";
                }

                String subQry = "";
                if (v_process.equals("listPage2")) {
                	subQry = "\n and    b.tuserid = " + StringManager.makeSQL(v_user_id);
                }
                System.out.println(sql1); 
 
                
                sql = "\n select c.grcode     /* 교육그룹 */ "
                    + "\n      , c.grcodenm   /* 교욱그룹명 */ "
                    + "\n      , a.subj       /* 과정코드 */ "
                    + "\n      , a.year       /* 년도 */ "
                    + "\n      , a.subjseq    /* 과정기수 */ "
                    + "\n      , b.class      /* 클래스 */ "
                    + "\n      , a.subjnm     /* 과정명 */ "
                    + "\n      , a.edustart   /* 교육시작일 */ "
                    + "\n      , a.eduend     /* 교육종료일 */ "
                    + "\n      , b.tuserid    /* 강사아이디 */ "
                    // 추가   
                    + "\n  , CASE WHEN WREPORT = 100 THEN  (               "
                    + "\n    SELECT MAX(DECODE(PROJGUBUN,'M','1차','')) || MIN(DECODE(PROJGUBUN,'F','2차',''))  FROM TZ_PROJGRP Z  "
                    + "\n    WHERE B.YEAR=Z.YEAR AND B.SUBJ=Z.SUBJ AND B.SUBJSEQ=Z.SUBJSEQ   "
                    + "\n    )               "
                    + "\n    WHEN WREPORT BETWEEN 40 AND  50  THEN (              "
                    + "\n    SELECT MAX(DECODE(PROJGUBUN,'F','1차',''))  FROM TZ_PROJGRP Z               "
                    + "\n    WHERE B.YEAR=Z.YEAR AND B.SUBJ=Z.SUBJ AND B.SUBJSEQ=Z.SUBJSEQ )   ELSE '' END rep_chk              "
                    
                    + "\n      , get_name(b.tuserid) as tusernm    /* 강사명 */ "
                    + "\n      , ( "
                    + "\n         select count(*) "
                    + "\n         from   tz_classtutor "
                    + "\n         where  subj = a.subj "
                    + "\n         and    year = a.year "
                    + "\n        ) as ctutorcnt     /* 총강사수 */ "
                    + "\n      , ( "
                    + "\n         select count(*) "
                    + "\n         from   tz_student "
                    + "\n         where  subj = b.subj "
                    + "\n         and    year = b.year "
                    + "\n         and    subjseq = b.subjseq "
                    + "\n         and    class = b.class "
                    + "\n        ) as stucnt          /* 입과인원 */ "
                    + "\n      , ( "
                    + "\n         select count(*) "
                    + "\n         from   tz_stold "
                    + "\n         where  subj = b.subj "
                    + "\n         and    year = b.year "
                    + "\n         and    subjseq = b.subjseq "
                    + "\n         and    class = b.class "
                    + "\n         and    isgraduated = 'Y' "
                    + "\n        ) as stoldcnt        /* 수료인원 */ "
                    + "\n      , ( "
                    + "\n         select count(*) "
                    + "\n         from   tz_tutorlog "
                    + "\n         where  tuserid = b.tuserid "
                    + "\n         and    substr(login,1,10) between a.edustart and a.eduend "
                    + "\n        ) as logincnt        /* 강사접속시간 */ "
                    + "\n      , ( "
                    + "\n         select count(*) "
                    + "\n         from   tz_gong "
                    + "\n         where  subj = b.subj "
                    + "\n         and    year = b.year "
                    + "\n         and    subjseq = b.subjseq "
                    + "\n         and    userid = b.tuserid "
                    + "\n        ) as gongcnt         /* 본인게시건수(공지) */ "
                    + "\n      , ( "
                    + "\n         select count(*) "
                    + "\n         from   tz_bds x "
                    + "\n              , tz_board y "
                    + "\n         where  x.type = 'SD' "
                    + "\n         and    x.tabseq = y.tabseq "
                    + "\n         and    x.subj = b.subj "
                    + "\n         and    y.userid = b.tuserid "
                    + "\n        ) as datacnt         /* 본인게시건수(자료) */ "
                    + "\n      , ( "
                    + "\n         select count(*) "
                    + "\n         from   tz_torontp "
                    + "\n         where  subj = b.subj "
                    + "\n         and    year = b.year "
                    + "\n         and    subjseq = b.subjseq "
                    + "\n         and    aduserid = b.tuserid "
                    + "\n        ) as torontpcnt        /* 본인게시건수(토론발의) */ "
                    + "\n      , ( "
                    + "\n         select count(*) "
                    + "\n         from   tz_toron "
                    + "\n         where  subj = b.subj "
                    + "\n         and    year = b.year "
                    + "\n         and    subjseq = b.subjseq "
                    + "\n         and    aduserid = b.tuserid "
                    + "\n        ) as toroncnt        /* 본인게시건수(토론의견) */ "
                    + "\n      , ( "
                    + "\n         select count(*) "
                    + "\n         from   tz_projassign "
                    + "\n         where  subj = b.subj "
                    + "\n         and    year = b.year "
                    + "\n         and    subjseq = b.subjseq "
                    + "\n         and    repdate is not null "
                    + "\n         and    userid in ( "
                    + "\n                           select userid "
                    + "\n                           from   tz_student "
                    + "\n                           where  subj = b.subj "
                    + "\n                           and    year = b.year "
                    + "\n                           and    subjseq = b.subjseq "
                    + "\n                           and    class = b.class "
                    + "\n                          ) "
                    + "\n        ) as repcnt        /* 리포트제출건수 */      "  
                    + "\n      , ( "
                    + "\n         select count(*) "
                    + "\n         from   tz_projassign "
                    + "\n         where  subj = b.subj "
                    + "\n         and    year = b.year "
                    + "\n         and    subjseq = b.subjseq "
                    + "\n         and    repdate is not null "
                    + "\n         and    totalscore is null "
                    + "\n         and    userid in ( "
                    + "\n                           select userid "
                    + "\n                           from   tz_student "
                    + "\n                           where  subj = b.subj "
                    + "\n                           and    year = b.year "
                    + "\n                           and    subjseq = b.subjseq "
                    + "\n                           and    class = b.class "
                    + "\n                          ) "
                    + "\n        ) as noscorecnt        /* 리포트미평가건수 */       " 
                    + "\n      , ( "
                    + "\n         select count(*) "
                    + "\n         from   tz_bds x "
                    + "\n              , tz_board y "
                    + "\n         where  x.type = 'SQ' "
                    + "\n         and    x.tabseq = y.tabseq "
                    + "\n         and    x.subj = b.subj "
                    + "\n         and    y.year = b.year "
                    + "\n         and    y.subjseq = b.subjseq "
                    + "\n         and    y.seq = y.refseq "
                    + "\n         and    y.userid in ( "
                    + "\n                             select userid "
                    + "\n                             from   tz_student "
                    + "\n                             where  subj = b.subj "
                    + "\n                             and    year = b.year "
                    + "\n                             and    subjseq = b.subjseq "
                    + "\n                             and    class = b.class "
                    + "\n                            ) "
                    + "\n        ) as qnacnt         /* 질의건수 */      "
                    + "\n      , ( "
                    + "\n         select count(distinct x.seq) "
                    + "\n         from   ( "
                    + "\n                 select y.tabseq, y.seq, y.subj, y.year, y.subjseq "
                    + "\n                 from   tz_bds x "
                    + "\n                      , tz_board y "
                    + "\n                 where  x.type = 'SQ' "
                    + "\n                 and    x.tabseq = y.tabseq "
                    + "\n                 and    y.seq = y.refseq "
                    + "\n                 and    (y.subj, y.year, y.subjseq, y.userid) in ( "
                    + "\n                                                                 select a.subj, a.year, a.subjseq, a.userid "
                    + "\n                                                                 from   tz_student a "
                    + "\n                                                                      , tz_classtutor b "
                    + "\n                                                                 where  a.subj = b.subj "
                    + "\n                                                                 and    a.year = b.year "
                    + "\n                                                                 and    a.subjseq = b.subjseq "
                    + "\n                                                                 and    a.class = b.class "
                    + subQry
                    + "\n                                                                ) "
                    + "\n                ) x "
                    + "\n              , tz_board y "
                    + "\n         where  x.tabseq = y.tabseq "
                    + "\n         and    x.seq = y.refseq "
                    + "\n         and    y.userid = b.tuserid "
                    + "\n         and    y.subj = b.subj "
                    + "\n         and    y.year = b.year "
                    + "\n         and    y.subjseq = b.subjseq  "
                    + "\n        ) as anscnt         /* 질의답변건수 */    "
                    + "\n      , ( "
                    + "\n         select max(ldate) "
                    + "\n         from   tz_stold "
                    + "\n         where  subj = a.subj "
                    + "\n         and    year = a.year "
                    + "\n         and    subjseq = a.subjseq "
                    + "\n        ) as ldate "
                    + "\n from   vz_scsubjseq a "
                    + "\n      , tz_classtutor b "
                    + "\n      , tz_grcode c "
                    + "\n where  a.subj = b.subj "
                    + "\n and    a.year = b.year "
                    + "\n and    a.subjseq = b.subjseq "
                    + "\n and    a.grcode = c.grcode "
                    + subQry;

                if (!v_isclosed.equals("")) {
                	sql += "\n and    substr(a.eduend,1,8) < " + SQLString.Format(FormatDate.getDate("yyyyMMdd"));
                }
                if (!ss_grcode.equals("ALL") && !ss_grcode.equals("----") && !ss_grcode.equals("")) {
                	sql += "\n and    a.grcode = " + SQLString.Format(ss_grcode);
                }
                if (!ss_gyear.equals("ALL") && !ss_gyear.equals("")) {
                	sql += "\n and    a.gyear = " + SQLString.Format(ss_gyear);
                }
                if (!ss_grseq.equals("ALL") && !ss_grseq.equals("")) {
                	sql += "\n and    a.grseq = " + SQLString.Format(ss_grseq);
                }
                if (!ss_uclass.equals("ALL") && !ss_uclass.equals("")) {
                	sql += "\n and    a.scupperclass = " + SQLString.Format(ss_uclass);
                }
                if (!ss_mclass.equals("ALL") && !ss_mclass.equals("")) {
                	sql += "\n and    a.scmiddleclass = " + SQLString.Format(ss_mclass);
                }
                if (!ss_lclass.equals("ALL") && !ss_lclass.equals("")) {
                	sql += "\n and    a.sclowerclass = " + SQLString.Format(ss_lclass);
                }
                if (!ss_subjcourse.equals("ALL") && !ss_subjcourse.equals("")) {
                	sql += "\n and    a.subj = " + SQLString.Format(ss_subjcourse);
                }
                if (!ss_subjseq.equals("ALL") && !ss_subjseq.equals("")) {
                	sql += "\n and    a.subjseq = " + SQLString.Format(ss_subjseq);
                }
                if (!ss_edustart.equals("")) {
                	sql += "\n and    a.edustart >= " + SQLString.Format(ss_edustart);
                }
                if (!ss_eduend.equals("")) {
                	sql += "\n and    a.eduend <= " + SQLString.Format(ss_eduend);
                }

                if (v_orderColumn.equals("")) {
                	sql += "\n order by a.grcode, a.subj asc, a.year asc, a.subjseq asc, a.edustart desc ";
                } else {
                	sql += "\n order by " + v_orderColumn + v_orderType;
                }
                
                ls1 = connMgr.executeQuery(sql1);

                while (ls1.next()) {
                    dbox1 = ls1.getDataBox();
                    list1.add(dbox1);
                }
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls1 != null) {
                try {
                    ls1.close();
                } catch (Exception e) {
                }
            }
            if (ls2 != null) {
                try {
                    ls2.close();
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

        return list1;
    }

    /**
     * 강사메인(미응답Q/A) 리스트 2005.06.22 jungkyoungjin
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectTutorQnaList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        DataBox dbox1 = null;
        String v_subjnm = "", v_subj = "", v_year = "", v_subjseq = "", v_title = "", v_inuserid = "", v_indate = "";
        int v_seq = 0;
        int anscnt = 0;

        String v_user_id = box.getSession("userid"); // 로그인 아이디

        String ss_edustart = (!box.getString("p_edustart").equals("")) ? (box.getString("p_edustart")).replaceAll("-", "") + "00" : ""; // 학습시작일
        String ss_eduend = (!box.getString("p_eduend").equals("")) ? (box.getString("p_eduend")).replaceAll("-", "") + "24" : ""; // 학습종료일

        String ss_action = box.getString("p_action");

        try {

            if (ss_action.equals("go")) {
                connMgr = new DBConnectionManager();
                list = new ArrayList();

                // sql = "select \r\n" +
                // " c.subj \r\n" +
                // ", c.subjnm \r\n" +
                // ", c.year \r\n" +
                // ", c.subjseq \r\n" +
                // ", d.seq \r\n" +
                // ", d.title \r\n" +
                // ", get_name(d.inuserid) inuserid \r\n" +
                // ", d.indate \r\n" +
                // ", (select count(*) from TZ_QNA where subj=d.subj and
                // year=d.year and subjseq=d.subjseq and lesson=d.lesson and
                // seq=d.seq and kind > 0) anscnt \r\n";
                // sql1 += "from \n" +
                // " TZ_CLASSTUTOR a \n" +
                // " , VZ_SCSUBJSEQ c \n" +
                // " , TZ_QNA d \r\n";
                // sql1 += "where \r\n";
                // sql1 += " a.tuserid='" +v_user_id + "' \r\n"; // v_user_id(강사
                // 로그인 아이디)
                // sql1 += " and c.isclosed = 'N' \r\n"; // 교육중
                // sql1 += " and d.kind = 0 \r\n"; // 답글이 아닌 것
                // sql1 += " and togubun = '2' \r\n";
                // // sql1 += " and anscnt = 0 \r\n"; // 답변구분이 학습자(1) 인 것
                // sql1 += " and a.subj=c.subj \r\n" +
                // " and a.year=c.year \r\n" +
                // " and a.subjseq=c.subjseq \r\n" +
                // " and a.subj=d.subj \r\n" +
                // " and a.year=d.year \r\n" +
                // " and a.subjseq=d.subjseq \r\n";

                sql = "\n select d.tabseq "
                    + "\n      , b.subj "
                    + "\n      , b.subjnm "
                    + "\n      , b.year "
                    + "\n      , b.subjseq "
                    + "\n      , a.class "
                    + "\n      , d.seq "
                    + "\n      , d.title "
                    + "\n      , d.name "
                    + "\n      , d.indate "
                    + "\n from   tz_classtutor a "
                    + "\n      , vz_scsubjseq b "
                    + "\n      , tz_bds c "
                    + "\n      , tz_board d "
                    + "\n where  a.tuserid = " + StringManager.makeSQL(v_user_id)
                    + "\n and    a.subj = b.subj "
                    + "\n and    a.year = b.year "
                    + "\n and    a.subjseq = b.subjseq "
                    + "\n and    b.isclosed = 'N' "
                    + "\n and    c.type = 'SQ' "
                    + "\n and    b.subj = c.subj "
                    + "\n and    b.subj = d.subj "
                    + "\n and    b.year = d.year "
                    + "\n and    b.subjseq = d.subjseq "
                    + "\n and    c.tabseq = d.tabseq "
                    + "\n and    d.levels = 1 "
                    + "\n and    d.indate between b.edustart and b.eduend "
                    + "\n and    (d.tabseq, d.seq) not in ( "
                    + "\n                      select d.tabseq, d.refseq "
                    + "\n                      from   tz_classtutor a "
                    + "\n                           , vz_scsubjseq b "
                    + "\n                           , tz_bds c "
                    + "\n                           , tz_board d "
                    + "\n                      where  a.tuserid = " + StringManager.makeSQL(v_user_id)
                    + "\n                      and    a.subj = b.subj "
                    + "\n                      and    a.year = b.year "
                    + "\n                      and    a.subjseq = b.subjseq "
                    + "\n                      and    b.subj = d.subj "
                    + "\n                      and    b.year = d.year "
                    + "\n                      and    b.subjseq = d.subjseq "                    
                    + "\n                      and    b.isclosed = 'N' "
                    + "\n                      and    c.type = 'SQ' "
                    + "\n                      and    c.subj = b.subj "
                    + "\n                      and    c.tabseq = d.tabseq "
                    + "\n                      and    d.levels > 1 "
                    + "\n                      and    d.ldate between b.edustart and b.eduend "
                    + "\n                     ) "
                    + "\n order  by b.subj, b.year, b.subjseq, d.indate desc ";
               
                ls = connMgr.executeQuery(sql);
                while (ls.next()) {
                    dbox1 = ls.getDataBox();
                    list.add(dbox1);
                } // END while
            } // END if
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

    /**
     * 강사메인(과제) 리스트 2005.06.22 jungkyoungjin
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectTutorReportList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
        ArrayList list1 = null;
        String sql1 = "";
        DataBox dbox1 = null;
        String v_subjnm = "", v_subj = "", v_year = "", v_subjseq = "", v_edustart = "", v_eduend = "";
        int v_wreport = 0, v_score = 0, v_reportcnt = 0, v_noscorecnt = 0;
        String v_user_id = box.getSession("userid"); // 로그인 사용자

        String ss_edustart = (!box.getString("p_edustart").equals("")) ? (box
                .getString("p_edustart")).replaceAll("-", "")
                + "00" : ""; // 학습시작일
        String ss_eduend = (!box.getString("p_eduend").equals("")) ? (box
                .getString("p_eduend")).replaceAll("-", "")
                + "24" : ""; // 학습종료일

        String ss_action = box.getString("p_action");

        try {

            if (ss_action.equals("go")) {
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                /*
                sql1 = "\n select c.subj, c.subjnm, c.year, c.subjseq, c.edustart, c.eduend, c.wreport, a.stucnt, a.reportcnt,noscorecnt,";
                sql1 += "\n  (select round(avg(score)) score from TZ_PROJREP where subj=c.subj group by subj) totavg";
                sql1 += "\n  from ";
                sql1 += "\n (select x.subj, x.year, x.subjseq, count(distinct y.userid) stucnt, count(z.projid) reportcnt, sum(nvl(decode(z.isfinal,'N',1),0)) noscorecnt, round(avg(z.score)) myavg ";
                sql1 += "\n  from TZ_CLASSTUTOR x, TZ_STUDENT y, TZ_PROJREP z";
                sql1 += "\n  where";
                sql1 += "\n  x.tuserid=" + SQLString.Format(v_user_id);
                sql1 += "\n  and x.subj=y.subj( +) and x.year=y.year( +) and x.subjseq=y.subjseq( +) and x.class=y.class( +)";
                sql1 += "\n  and y.subj=z.subj( +) and y.year=z.year( +) and y.subjseq=z.subjseq( +) and y.userid=z.projid( +)";
                sql1 += "\n  group by x.subj, x.year, x.subjseq) a, ";
                sql1 += "\n  VZ_SCSUBJSEQ c";
                sql1 += "\n  where ";
                sql1 += "\n  c.isclosed = 'N'"; // 교육중
                sql1 += "\n  and a.subj=c.subj and a.year=c.year and a.subjseq=c.subjseq";

                if (!ss_edustart.equals(""))
                    sql1 += "\n  and c.edustart <= "
                            + SQLString.Format(ss_edustart);
                if (!ss_eduend.equals(""))
                    sql1 += "\n  and c.eduend >= " + SQLString.Format(ss_eduend);

                sql1 += "\n  order by noscorecnt desc, eduend  ";
                */
                sql1= "\n select c.subj "
                    + "\n      , c.subjnm "
                    + "\n      , c.year "
                    + "\n      , c.subjseq "
                    + "\n      , a.class "
                    + "\n      , c.edustart "
                    + "\n      , c.eduend "
                    + "\n      , c.wreport "
                    + "\n      , a.stucnt "
                    + "\n      , ( "
                    + "\n         select count(distinct projgubun) " 
                    + "\n         from   tz_projgrp  "
                    + "\n         where  subj=c.subj  "
                    + "\n         and    year=c.year  "
                    + "\n         and    subjseq=c.subjseq  "
                    + "\n        ) totreportcnt "
                    + "\n      , a.reportcnt "
                    + "\n      , noscorecnt "
                    + "\n      , ( "
                    + "\n         select round(avg(totalscore)) score " 
                    + "\n         from   tz_projassign  "
                    + "\n         where  subj=c.subj  "
                    + "\n         group  by subj "
                    + "\n        ) totavg "
                    + "\n from   ( "
                    + "\n         select x.subj "
                    + "\n              , x.year "
                    + "\n              , x.subjseq "
                    + "\n              , x.class "
                    + "\n              , count(distinct y.userid) as stucnt "
                    + "\n              , sum(decode(z.subj,null,0,decode(z.repdate,null,1,0))) as reportcnt "
                    + "\n              , sum(decode(repdate,null,0,decode(totalscore,null,1,0))) as noscorecnt "
                    + "\n              , round(avg(z.totalscore)) myavg  "
                    + "\n         from   tz_classtutor x, tz_student y, tz_projassign z "
                    + "\n         where  x.tuserid= " + SQLString.Format(v_user_id)
                    + "\n         and    x.subj=y.subj(+)  "
                    + "\n         and    x.year=y.year(+)  "
                    + "\n         and    x.subjseq=y.subjseq(+) " 
                    + "\n         and    x.class=y.class(+) "
                    + "\n         and    y.subj=z.subj(+)  "
                    + "\n         and    y.year=z.year(+)  "
                    + "\n         and    y.subjseq=z.subjseq(+) " 
                    + "\n         and    y.userid=z.userid(+) "
                    + "\n         group  by x.subj, x.year, x.subjseq, x.class "
                    + "\n        ) a "
                    + "\n      , vz_scsubjseq c "
                    + "\n where  c.isclosed = 'N' "
                    + "\n and    a.subj=c.subj  "
                    + "\n and    a.year=c.year  "
                    + "\n and    a.subjseq=c.subjseq "
                    + "\n and    nvl(noscorecnt,0) > 0 ";
                
                if (!ss_edustart.equals(""))
                    sql1 += "\n and    c.edustart <= "
                            + SQLString.Format(ss_edustart);
                if (!ss_eduend.equals(""))
                    sql1 += "\n and    c.eduend >= " + SQLString.Format(ss_eduend);

                sql1+="\n order  by noscorecnt desc, eduend " ; 

                ls1 = connMgr.executeQuery(sql1);

                while (ls1.next()) {

                    dbox1 = ls1.getDataBox();
                    list1.add(dbox1);

                } // END while
            } // END if

        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (ls1 != null) {
                try {
                    ls1.close();
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

        return list1;
    }

    /**
     * 강사메인(강사료) 리스트 2005.06.22 jungkyoungjin
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectTutorPay(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
        ArrayList list1 = null;
        String sql1 = "";
        DataBox dbox1 = null;
        String v_subjnm = "", v_subj = "", v_year = "", v_subjseq = "", v_edustart = "", v_eduend = "";
        int v_stucnt = 0, v_price = 0, v_addprice = 0, v_pay = 0;
        String v_user_id = box.getSession("userid"); // 로그인 사용자

        String ss_edustart = (!box.getString("p_edustart").equals("")) ? (box
                .getString("p_edustart")).replaceAll("-", "")
                + "00" : ""; // 학습시작일
        String ss_eduend = (!box.getString("p_eduend").equals("")) ? (box
                .getString("p_eduend")).replaceAll("-", "")
                + "24" : ""; // 학습종료일

        String ss_action = box.getString("p_action");

        try {

            if (ss_action.equals("go")) {
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();

                sql1 = "select c.subj, c.subjnm, c.edustart, c.eduend,";
                sql1 += "(select count(userid) from TZ_STUDENT where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class and a.tuserid='"
                        + v_user_id + "') stucnt,";
                sql1 += "(select count(userid) from TZ_STUDENT where subj=a.subj and year=a.year and subjseq=a.subjseq and class=a.class and a.tuserid='"
                        + v_user_id + "' and ISGRADUATED='Y') grayncnt, ";
                sql1 += " d.price, d.addprice";
                sql1 += " from TZ_CLASSTUTOR a, TZ_TUTOR b, VZ_SCSUBJSEQ c, TZ_TUTORPAY d";
                sql1 += " where ";
                sql1 += " a.tuserid='" + v_user_id + "'"; // v_user_id(강사 로그인
                                                            // 아이디)
                sql1 += " and c.isclosed = 'N'"; // 교육중
                sql1 += " and a.tuserid = b.userid and a.subj=c.subj and a.year=c.year and a.subjseq=c.subjseq and  b.tutorgubun=d.tutorcode";

                if (!ss_edustart.equals(""))
                    sql1 += " and c.edustart <= "
                            + SQLString.Format(ss_edustart);
                if (!ss_eduend.equals(""))
                    sql1 += " and c.eduend >= " + SQLString.Format(ss_eduend);

                sql1 += " order by edustart asc";

                ls1 = connMgr.executeQuery(sql1);

                while (ls1.next()) {

                    dbox1 = ls1.getDataBox();

                    v_pay = (v_price * v_stucnt) + v_addprice;
                    dbox1.put("pay", v_pay + "");

                    list1.add(dbox1);

                } // END while
            } // END if

        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage());
        } finally {
            if (ls1 != null) {
                try {
                    ls1.close();
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

        return list1;
    }

    /**
     * 강사 과목운영 정보
     * 
     * @param box
     *            receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectTutorContentSubj(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        DataBox dbox = null;
        int v_pay = 0;
        int v_price = 0;
        int v_addprice = 0;
        int v_grayncnt = 0;

        String v_tutorid = box.getSession("userid");
        String v_gyear = box.getStringDefault("gyear", FormatDate.getDate("yyyy"));

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = "\n select a.subj "
            	+ "\n      , a.year "
            	+ "\n      , a.subjseq "
            	+ "\n      , a.class "
            	+ "\n      , a.tuserid "
            	+ "\n      , a.ttype "
            	+ "\n      , b.subjnm "
            	+ "\n      , b.contenttype "
            	+ "\n      , b.owner "
            	+ "\n      , b.isonoff "
            	+ "\n      , b.edustart "
            	+ "\n      , b.eduend "
            	+ "\n      , b.isclosed "
            	+ "\n      , (select count(*) "
            	+ "\n         from   tz_student x "
            	+ "\n         where  a.class = x.class "
            	+ "\n         and    a.subj = x.subj "
            	+ "\n         and    a.year = x.year "
            	+ "\n         and    a.subjseq = x.subjseq) stucnt "
            	+ "\n      , (select max(ldate) "
            	+ "\n         from   tz_stold "
            	+ "\n         where  subj = a.subj "
            	+ "\n         and    year = a.year "
            	+ "\n         and    subjseq = a.subjseq) as closedt "
            	+ "\n from   tz_classtutor a "
            	+ "\n      , vz_scsubjseq b "
            	+ "\n      , tz_subjman c "
            	+ "\n where  a.subj = b.subj "
            	+ "\n and    a.year = b.year "
            	+ "\n and    a.subjseq = b.subjseq "
            	+ "\n and    a.subj = c.subj "
            	+ "\n and    a.tuserid = c.userid "
            	+ "\n and    substr(c.gadmin,1,1) = 'P' "
            	+ "\n and    a.tuserid = " + StringManager.makeSQL(v_tutorid)
            	+ "\n and    b.gyear   = " + StringManager.makeSQL(v_gyear)
            	+ "\n order  by b.edustart desc";  // 09/04/13 15:52 추가!

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
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


    /**
     * 수강생 리스트
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectStudentList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls      = null;
        ArrayList list  = null;
        String sql      = "";
        DataBox dbox    = null;

        StudyStatusData data  = null;
        ProposeBean  probean = new ProposeBean();

        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        String v_class = box.getString("p_class");

        String v_orderColumn = box.getString("p_orderColumn");           // 정렬할 컬럼명
        String v_orderType   = box.getString("p_orderType");           // 정렬할 순서

        String v_userid    	= box.getSession("userid");
        String s_gadmin    	= box.getSession("gadmin");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = "\n select a.subj " +
                  "\n      , a.year " +
                  "\n      , a.subjseq " +
                  "\n      , a.class " +
                  "\n      , a.userid " +
                  "\n      , get_name(a.userid) as name " +
                  "\n      , trunc(a.tstep   ,1) as tstep " +
                  "\n      , trunc(a.avtstep ,1) as avtstep " +
            	  "\n      , trunc(a.act     ,1) as act ";
                   
            /*if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // 가중치적용
                sql += "\n      , trunc(a.avreport,1) report " +
                       "\n      , trunc(a.avmtest ,1) mtest " +
                       "\n      , trunc(a.avetc1  ,1) etc1 " +
                       "\n      , trunc(a.avetc2  ,1) etc2 " +
                       "\n      , trunc(a.avftest ,1) ftest ";
            } else {                                                    // 가중치비적용 */
                sql += "\n      , trunc(a.report,1) report " +
		               "\n      , trunc(a.mtest ,1) mtest " +
		               "\n      , trunc(a.etc1  ,1) etc1 " +
		               "\n      , trunc(a.etc2  ,1) etc2 " +
		               "\n      , trunc(a.ftest ,1) ftest ";
            //}
             
            sql += "\n      , trunc(a.study_count,1) as study_count " +
            	   "\n      , trunc(a.study_time,1) as study_time " +
            	   "\n      , trunc(a.score,1) as score " +
                   "\n      , nvl(b.isgraduated,'-') as isgraduated " +
                   "\n      , nvl(( " +
                   "\n         select count(lesson) " +
                   "\n         from   tz_exampaper " +
                   "\n         where  subj    = a.subj " +
                   "\n         and    year    = a.year " +
                   "\n         and    subjseq = a.subjseq " +
                   "\n         and    examtype= 'M' " +
                   "\n        ),0) as mexamcnt /*형성평가출제개수*/" +
                   "\n      , nvl(( " +
                   "\n         select count(lesson) " +
                   "\n         from   tz_exampaper " +
                   "\n         where  subj    = a.subj " +
                   "\n         and    year    = a.year " +
                   "\n         and    subjseq = a.subjseq " +
                   "\n         and    examtype= 'E' " +
                   "\n        ),0) as fexamcnt /*최종평가출제개수*/" +
                   "\n      , nvl(( " +
                   "\n         select count(distinct projgubun) " +
                   "\n         from   tz_projgrp " +
                   "\n         where  subj    = a.subj " +
                   "\n         and    year    = a.year " +
                   "\n         and    subjseq = a.subjseq " +
                   "\n         group by subj, year, subjseq " +
                   "\n        ),0) as totprojcnt /*리포트출제개수*/" +
                   "\n      , nvl(( " +
                   "\n         select count(*) " +
                   "\n         from   tz_examresult " +
                   "\n         where  subj    = a.subj " +
                   "\n         and    year    = a.year " +
                   "\n         and    subjseq = a.subjseq " +
                   "\n         and    userid  = a.userid " +
                   "\n         and    examtype= 'M' " +
                   "\n        ),0) as usermexamcnt /*형성평가제출개수*/" +
                   "\n      , nvl(( " +
                   "\n         select case when t1.ended is null or t1.ended = '' " +
                   "\n                     then case when trunc((sysdate - to_date(t1.started,'yyyymmddhh24miss')) * 24 * 60) > t2.examtime then 'Y' " +
                   "\n                               else 'N' " +
                   "\n                          end " +
                   "\n                end " +
                   "\n         from   tz_examresult t1 " +
                   "\n              , tz_exampaper t2 " +
                   "\n         where  t1.subj    = a.subj " +
                   "\n         and    t1.year    = a.year " +
                   "\n         and    t1.subjseq = a.subjseq " +
                   "\n         and    t1.userid  = a.userid " +
                   "\n         and    t1.subj  = t2.subj " +
                   "\n         and    t1.year  = t2.year " +
                   "\n         and    t1.subjseq  = t2.subjseq " +
                   "\n         and    t1.lesson  = t2.lesson " +
                   "\n         and    t1.examtype  = t2.examtype " +
                   "\n         and    t1.papernum  = t2.papernum " +
                   "\n         and    t1.examtype= 'M' " +
                   "\n        ),'N') as mexam_timeover /*형성평가시간초과*/" +
                   "\n      , nvl(( " +
                   "\n         select count(*) " +
                   "\n         from   tz_examresult " +
                   "\n         where  subj    = a.subj " +
                   "\n         and    year    = a.year " +
                   "\n         and    subjseq = a.subjseq " +
                   "\n         and    userid  = a.userid " +
                   "\n         and    examtype= 'E' " +
                   "\n        ),0) as userfexamcnt /*최종평가제출개수*/" +
                   "\n      , nvl(( " +
                   "\n         select case when t1.ended is null or t1.ended = '' " +
                   "\n                     then case when trunc((sysdate - to_date(t1.started,'yyyymmddhh24miss')) * 24 * 60) > t2.examtime then 'Y' " +
                   "\n                               else 'N' " +
                   "\n                          end " +
                   "\n                end " +
                   "\n         from   tz_examresult t1 " +
                   "\n              , tz_exampaper t2 " +
                   "\n         where  t1.subj    = a.subj " +
                   "\n         and    t1.year    = a.year " +
                   "\n         and    t1.subjseq = a.subjseq " +
                   "\n         and    t1.userid  = a.userid " +
                   "\n         and    t1.subj  = t2.subj " +
                   "\n         and    t1.year  = t2.year " +
                   "\n         and    t1.subjseq  = t2.subjseq " +
                   "\n         and    t1.lesson  = t2.lesson " +
                   "\n         and    t1.examtype  = t2.examtype " +
                   "\n         and    t1.papernum  = t2.papernum " +
                   "\n         and    t1.examtype= 'E' " +
                   "\n        ),'N') as fexam_timeover /*최종평가시간초과*/" +
                   "\n      , nvl(( " + 
                   "\n         select count(distinct grpseq) " +
                   "\n         from   tz_projrep " +
                   "\n         where  subj    = a.subj " +
                   "\n         and    year    = a.year " +
                   "\n         and    subjseq = a.subjseq " +
                   "\n         and    projid  = a.userid " +
                   "\n         and    isfinal = 'Y' " +
                   "\n         group  by subj, year, subjseq, projid " +
                   "\n        ),0) as repprojcnt /*리포트제출개수*/" +
                   "\n      , nvl(( " + 
                   "\n         select count(distinct grpseq) " +
                   "\n         from   tz_projassign " +
                   "\n         where  subj    = a.subj " +
                   "\n         and    year    = a.year " +
                   "\n         and    subjseq = a.subjseq " +
                   "\n         and    userid  = a.userid " +
                   "\n         and    ldate is null " +
                   "\n         and    totalscore is null " +
                   "\n         group  by subj, year, subjseq, userid " +
                   "\n        ),0) as notrepprojcnt /*리포트미채점개수*/" +
                   "\n      , nvl(( " +
                   "\n         select count(distinct isret) " +
                   "\n         from   tz_projrep " +
                   "\n         where  subj    = a.subj " +
                   "\n         and    year    = a.year " +
                   "\n         and    subjseq = a.subjseq " +
                   "\n         and    projid  = a.userid " +
                   "\n         and    isret   = 'Y' " +
                   "\n         group by subj, year, subjseq, projid " +
                   "\n        ),0) as isretcnt /*리포트반려개수*/" +
                   "\n      , nvl(( " +
                   "\n         select count(distinct seq) " +
                   "\n         from   tz_toron " +
                   "\n         where  subj    = a.subj " +
                   "\n         and    year    = a.year " +
                   "\n         and    subjseq = a.subjseq " +
                   "\n         and    luserid = a.userid " +
                   "\n         group by subj, year, subjseq " +
                   "\n        ),0) as toroncnt /*토론참여개수*/" +
                   "\n      , nvl(( " +
                   "\n         select sum(agree) " +
                   "\n         from   tz_toron " +
                   "\n         where  subj    = a.subj " +
                   "\n         and    year    = a.year " +
                   "\n         and    subjseq = a.subjseq " +
                   "\n         and    luserid = a.userid " +
                   "\n         group by subj, year, subjseq " +
                   "\n        ),0) as toronagreecnt /*토론찬성개수*/" +
                   "\n from   tz_student a " +
                   "\n      , tz_stold b " +
                   "\n where  a.subj    = b.subj(+) " +
                   "\n and    a.year    = b.year(+) " +
                   "\n and    a.subjseq = b.subjseq(+) " +
                   "\n and    a.userid  = b.userid(+) ";
                
			if ( !v_subj.equals(""))      	sql += "\n and    a.subj    = " +SQLString.Format(v_subj);
			if ( !v_year.equals(""))      	sql += "\n and    a.year    = " +SQLString.Format(v_year);
			if ( !v_subjseq.equals("")) 	sql += "\n and    a.subjseq = " +SQLString.Format(v_subjseq);
			if ( !v_class.equals(""))    	sql += "\n and    a.class   = " +SQLString.Format(v_class);
			
			if ( v_orderColumn.equals("") ) { 
				sql += "\n order  by get_name(a.userid), a.userid  ";
			} else { 
				sql += "\n order  by " + v_orderColumn + v_orderType + ", get_name(a.userid), a.userid ";
			}
                
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
            	dbox = ls.getDataBox();
                dbox.put("d_tstep"	, new Double( ls.getDouble("tstep"))); 
                dbox.put("d_avtstep", new Double( ls.getDouble("avtstep"))); 
                dbox.put("d_report"	, new Double( ls.getDouble("report"))); 
                dbox.put("d_etc1"	, new Double( ls.getDouble("etc1"))); 
                dbox.put("d_etc2"	, new Double( ls.getDouble("etc2"))); 
                dbox.put("d_act"	, new Double( ls.getDouble("act"))); 
                dbox.put("d_mtest"	, new Double( ls.getDouble("mtest"))); 
                dbox.put("d_ftest"	, new Double( ls.getDouble("ftest"))); 
                dbox.put("d_score"	, new Double( ls.getDouble("score"))); 
                    
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
        } finally { 
            if ( ls != null )  { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }    
    
    /**
     * 강사 권한 리스트
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectTutorManagerList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        String sql = "";
        DataBox dbox = null;

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = "\n select gadmin, gadminnm "
            	+ "\n from   tz_gadmin "
            	+ "\n where  substr(gadmin,1,1) = 'P' ";

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) { 
            	try { ls.close(); } catch (Exception e) { } 
            }
            if (connMgr != null) {
                try { connMgr.freeConnection(); } catch (Exception e10) { }
            }
        }

        return list;
    }
    
}