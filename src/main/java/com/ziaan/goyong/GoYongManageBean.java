//**********************************************************
//  1. 제      목: 고용보험 관리자페이지
//  2. 프로그램명: 고용보험 훈련실시신고
//  3. 개      요:
//  4. 환      경:
//  5. 버      젼:
//  6. 작      성: 2005.07.10 이연정
//  7. 수      정:
//**********************************************************
package com.ziaan.goyong;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.propose.ProposeBean;

public class GoYongManageBean {

	public GoYongManageBean() {}
	
	/**
	* 고용보험 훈련실시신고 리스트
	* @param box          receive from the form object and session
	* @return ArrayList
	* @throws Exception
	*/
    public ArrayList selectStudentList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
//      	int v_pageno         = 1;
      	
      	String cnt = "" ;

        String  ss_uclass    = box.getStringDefault("s_upperclass","ALL");    //과정분류
		String  ss_mclass    = box.getStringDefault("s_middleclass","ALL");    //과정분류

		String  v_startdate  = box.getString("p_startdate");
		String  v_enddate    = box.getString("p_enddate");

		String v_order       = box.getString("p_order");
        String v_orderType   = box.getString("p_orderType");                 //정렬할 순서

//		ProposeBean probean = new ProposeBean();

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql += " select         \n";
            sql += "   grseq,       \n";
            sql += "   gyear,       \n";
            sql += "   course,      \n";
            sql += "   cyear,       \n";
            sql += "   courseseq,   \n";
            sql += "   coursenm,    \n";
            sql += "   subj,        \n";
            sql += "   year,        \n";
            sql += "   subjnm,      \n";
            sql += "   subjseq,     \n";
            sql += "   subjseqgr,   \n";
            sql += "   grcode,      \n";
            sql += "   grcodenm,    \n";
            sql += "   stucnt,       \n";
            sql += "   COUNT(*) OVER (PARTITION BY subj) row_cnt  \n";
            sql += " from           \n";
            sql += " (              \n";
						sql += " select     \n "
						  	+ "  C.grseq,	   \n "
						  	+ "  C.gyear,	   \n "
						  	+ " C.course,	   \n "
						  	+ " C.cyear,	   \n "
						  	+ " C.courseseq, \n "
						  	+ " C.coursenm,  \n  "
						  	+ " C.subj,		 \n "
						  	+ " C.year,		 \n "
						  	+ " C.subjnm,		 \n "
						  	+ " C.subjseq, 	 \n "
						  	+ " C.subjseqgr, 	 \n "
						  	+ " C.grcode, 	 \n "
						  	+ " (select grcodenm from tz_grcode where grcode = C.grcode) grcodenm,	 \n "
						  	+ " (select count(*) cnt from tz_student x where c.subj = x.subj and c.year = x.year and c.subjseq = x.subjseq) stucnt "

						 		+ "from              \n   "
						 		+ "  vz_scsubjseq C	 \n"
						 		+ " where 1=1 and isgoyong='Y'  \n";
						//if(!v_startdate.equals("") ){//
						//  sql += "  and substr(c.edustart, 0 , 8)  ="+SQLString.Format(v_startdate);
						//}
      			        //
						//if(!v_enddate.equals("") ){//
						//  sql += "  and substr(c.eduend, 0 , 8 ) ="+SQLString.Format(v_enddate);
						//}

					    sql += "  and '"+v_startdate+"00' <= edustart and eduend <= '"+v_enddate+"23'";
             if (!ss_uclass.equals("ALL")) {
                sql+= " and C.scupperclass = "+SQLString.Format(ss_uclass)+"\n";
            }
            if (!ss_mclass.equals("ALL")) {
                sql+= " and C.scmiddleclass = "+SQLString.Format(ss_mclass)+"\n";
            }
            sql+=" )   HuryunExeTable \n";
            sql+=" where                        \n";
            sql+="   stucnt > 0                 \n";

            if (v_order.equals("stucnt"))  v_order ="stucnt";

            if(v_order.equals("")) {
                sql+=" order by subj, year, subjseq \n";
            } else {
                sql+= " order by " + v_order + v_orderType;
            }

            ls = connMgr.executeQuery(sql);
            while (ls.next()) {
                dbox = ls.getDataBox();
                
                box.put("p_subj"     , ls.getString("subj"));     
                box.put("p_subjseq"  , ls.getString("subjseq"));  
                box.put("p_year"     , ls.getString("year"));     
                box.put("p_startdate", v_startdate);
                box.put("p_enddate"  , v_enddate);  
                list.add(dbox);
            }   
                
       }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

	/**
	* 고용보험 훈련실시신고 회원 뷰
	* @param box          receive from the form object and session
	* @return ArrayList
	*/
    public ArrayList selectStudentListView(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
//      	int v_pageno = 1;
		String v_subj = box.getString("p_subj");
		String v_year = box.getString("p_year");
		String v_subjseq = box.getString("p_subjseq");

		//ProposeBean probean = new ProposeBean();

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = " select C.grseq,C.course,C.cyear,C.courseseq,C.coursenm,C.subj,C.year,C.subjnm,C.subjseq,C.grcode,";
            sql+= " a.tstep,";
			//sql+= " dbo.get_deptnm(b.deptnm,'') compnm, dbo.get_jikwinm(B.jikwi, B.comp) jikwinm,\n";
            //sql+= " B.userid,B.cono, b.birth_date,B.name \n";
            sql+= " get_deptnm(b.dept_cd) compnm, '' jikwinm,\n";
            sql+= " B.userid, fn_crypt('2', b.birth_date, 'knise') birth_date,B.name \n";
			sql+= " from tz_student a,TZ_MEMBER b,vz_scsubjseq c \n";
			sql+= " where a.userid = b.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq \n";
			sql+= "   and  a.subj="+SQLString.Format(v_subj);
			sql+= "   and  a.year="+SQLString.Format(v_year);
			sql+= "   and  a.subjseq="+SQLString.Format(v_subjseq);
			//System.out.println("sql_studentlist==>>"+sql);

            ls = connMgr.executeQuery(sql);

			while (ls.next()) {
                dbox = ls.getDataBox();

                list.add(dbox);
            }
       }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

	/**
	* 출석부 SQL
	* @param box          receive from the form object and session
	* @return ArrayList
	*/
    public ArrayList selectProposeYList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";

        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    //교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     //년도
        //String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     //교육차수
        String  ss_grseq    = "0001";     //교육차수

        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    //과정분류
		String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");    //과정분류

		String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");//과정&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   //과정 차수
        String  ss_comp     = box.getStringDefault("s_company","ALL");   //회사

		ProposeBean probean = new ProposeBean();
		String v_year       = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subjcourse, ss_subjseq);
		if (v_year.equals("")) v_year = ss_gyear;

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = " select C.grseq,C.course,C.cyear,C.courseseq,C.coursenm,C.subj,C.year,C.subjnm,C.subjseq,\n"
				//+ " dbo.get_compnm(B.comp,2,4) compnm, dbo.get_jikwinm(B.jikwi, B.comp) jikwinm,\n"
                //+ " B.userid,B.cono,b.birth_date,B.name--, b.orga_ename \n"  //--20070917수정(컬럼 의미 모름)
            	+ " get_compnm(B.comp) compnm, post_nm jikwinm,\n"
                + " B.userid,'' as cono,fn_crypt('2', b.birth_date, 'knise') birth_date,B.name--, b.orga_ename \n"  //--20070917수정(컬럼 의미 모름)
				+ " from tz_propose a,TZ_MEMBER b,vz_scsubjseq c \n"
				+ " where a.userid = b.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq \n";
            sql+= " and a.chkfinal = 'Y' \n";
            sql+= " and C.grcode = "+SQLString.Format(ss_grcode) +"\n";
            sql+= " and C.year = "+SQLString.Format(v_year) +"\n";

            if (!ss_uclass.equals("ALL")) {
                sql+= " and C.scupperclass = "+SQLString.Format(ss_uclass)+"\n";
            }
            if (!ss_mclass.equals("ALL")) {
                sql+= " and C.scmiddleclass = "+SQLString.Format(ss_mclass)+"\n";
            }

            if (!ss_subjcourse.equals("ALL")) {
              sql+= " and C.scsubj = "+SQLString.Format(ss_subjcourse)+"\n";
            }
            if (!ss_subjseq.equals("ALL")) {
              sql+= " and C.scsubjseq = "+SQLString.Format(ss_subjseq)+"\n";
            }
            if (!ss_gyear.equals("ALL")) {
              sql+= " and c.gyear = "+SQLString.Format(ss_gyear)+"\n";
            }
            if (!ss_comp.equals("ALL")) {
                //sql+= " and substr(b.comp,0,4) = '0101'";
            }
            sql+= " order by B.name,C.course,C.cyear,C.courseseq,C.subj,C.year,C.subjseq,B.userid \n";

            //System.out.println("sq="+sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
    
	/**
	* 수료명단 리스트
	* @param box          receive from the form object and session
	* @return ArrayList
	*/
    public ArrayList selectSuryoStudentList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";

        String  ss_grcode   	= box.getStringDefault("s_grcode","ALL");    //교육그룹
        String  ss_gyear    	= box.getStringDefault("s_gyear","ALL");     //년도
        String  ss_grseq    	= box.getStringDefault("s_grseq","ALL");     //교육차수

        String  ss_uclass   	= box.getStringDefault("s_upperclass","ALL");    //과정분류
				String  ss_mclass   	= box.getStringDefault("s_middleclass","ALL");    //과정분류

				String  ss_subjcourse	=	box.getStringDefault("s_subjcourse","ALL");//과정&코스
        String  ss_subjseq  	= box.getStringDefault("s_subjseq","ALL");   //과정 차수
//        String  ss_comp  			= box.getStringDefault("s_company","ALL");   //회사

        String  v_startdate		= box.getString("p_startdate");		//학습시작일
        String  v_enddate			= box.getString("p_enddate");			//학습종료일
        String v_order       = box.getString("p_order");
        String v_orderType   = box.getString("p_orderType");                 //정렬할 순서

				ProposeBean probean = new ProposeBean();
				String v_year       = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subjcourse, ss_subjseq);
			if (v_year.equals("")) v_year = ss_gyear;

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select a.serno,C.grseq,C.courseseq,C.coursenm,C.subj,C.year,C.subjnm, ";
            sql += " C.subjseq, c.subjseqgr, c.edustart, c.eduend, ";
			//sql += " dbo.get_deptnm(B.deptnm,'') compnm, dbo.get_jikwinm(B.jikwi, B.comp) jikwinm,";
			//sql += " dbo.get_compnm(B.comp,1,2) company,";
            //sql += " B.userid,B.cono,b.birth_date,B.name,c.place,c.edustart,c.eduend";
            sql += " get_deptnm(B.dept_cd) compnm, b.post_nm jikwinm,";
            sql += " get_compnm(B.comp) company,";
            sql += " B.userid,'' cono, fn_crypt('2', b.birth_date, 'knise') birth_date,B.name,c.place,c.edustart,c.eduend";
			sql += " from tz_stold a,TZ_MEMBER b,vz_scsubjseq c";
			sql += " where c.isclosed='Y' and a.isgoyong = 'Y' and a.userid = b.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq and A.isgraduated='Y'";

            if (!ss_grcode.equals("ALL")){
            		sql+= " and C.grcode = "+SQLString.Format(ss_grcode);
            }
            if (!ss_gyear.equals("ALL")) {
                sql+= " and c.gyear = "+SQLString.Format(v_year);
            }
            if (!ss_uclass.equals("ALL")) {
                sql+= " and C.scupperclass = "+SQLString.Format(ss_uclass);
            }
            if (!ss_mclass.equals("ALL")) {
                sql+= " and C.scmiddleclass = "+SQLString.Format(ss_mclass);
            }
            if (!ss_subjcourse.equals("ALL")) {
                sql+= " and C.scsubj = "+SQLString.Format(ss_subjcourse);
            }
            if (!ss_subjseq.equals("ALL")) {
                sql+= " and C.scsubjseq = "+SQLString.Format(ss_subjseq);
            }
            if (!v_startdate.equals("") && !v_enddate.equals("")) {
            		sql+= " and substr(C.edustart,1,8) >= "+SQLString.Format(v_startdate);
            		sql+= " and substr(C.eduend,1,8) <= "+SQLString.Format(v_enddate);
            }


            if (v_order.equals("subjnm")   )  v_order = "c.subjnm";
            if (v_order.equals("subjseqgr"))  v_order = "c.subjseqgr";
            if (v_order.equals("eduterm")  )  v_order = "c.edustart";
            if (v_order.equals("serno")    )  v_order = "a.serno";
            if (v_order.equals("userid")   )  v_order = "b.userid";
            if (v_order.equals("name")     )  v_order = "b.name";
            //if (v_order.equals("deptnam")  )  v_order = "dbo.get_deptnm(B.deptnm,'')";
            //if (v_order.equals("compnm")   )  v_order = "dbo.get_compnm(B.comp,1,2)";
            //if (v_order.equals("jikwinm")  )  v_order = "dbo.get_jikwinm(B.jikwi, B.comp)";
            if (v_order.equals("deptnam")  )  v_order = "get_deptnm(B.dept_cd)";
            if (v_order.equals("compnm")   )  v_order = "get_compnm(B.comp)";
            if (v_order.equals("jikwinm")  )  v_order = "b.post_nm";
            if (v_order.equals("birth_date")    )  v_order = "birth_date";

            if(v_order.equals("")) {
                sql+= " order by B.name,C.course,C.cyear,C.courseseq,C.subj,C.subjseq,B.userid ";
            } else {
                sql+= " order by " + v_order + v_orderType;
            }

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }


	/**
	* 수료명단 데이타
	* @param box          receive from the form object and session
	* @return DataBox
	*/
    public DataBox selectSuryoTitle(RequestBox box, String ss_subjcourse) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        String sql                  = "";

        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    //교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     //년도
        String  ss_grseq    = box.getStringDefault("s_grseq","0001");     //교육차수

//        String  ss_comp  = box.getStringDefault("s_company","ALL");   //회사


		//String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");//과정&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   //과정 차수

		ProposeBean probean = new ProposeBean();
		String v_year       = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subjcourse, ss_subjseq);
		if (v_year.equals("")) v_year = ss_gyear;

        try {
            connMgr = new DBConnectionManager();

            //sql = " select c.scsubjnm,d.companynm,decode(c.place,'','온라인') place,c.edustart,c.eduend, c.subjseqgr"
			//	+ " from vz_scsubjseq c, tz_comp d, tz_grcomp e"
			//	+ " where
			//e.comp = d.comp and c.grcode = e.grcode";
			//	//+ " and c.eduend<to_char(sysdate,'YYYYMMDDHH24')";
            //
            //sql+= " and C.grcode = "+SQLString.Format(ss_grcode) +"\n";
            //sql+= " and c.year = "+SQLString.Format(v_year);
            //sql+= " and C.scsubj = "+SQLString.Format(ss_subjcourse);
            //sql+= " and C.scsubjseq = "+SQLString.Format(ss_subjseq);

            sql+=" select                               \n";
            sql+="   c.scsubjnm,                        \n";
            sql+="   d.companynm,                       \n";
            sql+="   c.place, 							\n";
            sql+="   c.edustart,                        \n";
            sql+="   c.eduend,                          \n";
            sql+="   c.subjseqgr                        \n";
            sql+=" from                                 \n";
            sql+="   vz_scsubjseq c,                    \n";
            sql+="   tz_comp d,                         \n";
            sql+="   tz_grcomp e                        \n";
            sql+=" where                                \n";
            sql+="   e.comp = d.comp and c.grcode = e.grcode                   \n";
            sql+="   and C.grcode    = "+SQLString.Format(ss_grcode) +"\n";
            sql+="   and c.year      = "+SQLString.Format(v_year);
            sql+="   and C.scsubj    = "+SQLString.Format(ss_subjcourse);
            sql+="   and C.scsubjseq = "+SQLString.Format(ss_subjseq);


            //System.out.println("sql1111111============>"+sql);

            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                dbox = ls.getDataBox();
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return dbox;
    }


	/**
	* 수료증  리스트
	* @param box          receive from the form object and session
	* @return ArrayList
	*/
    public ArrayList selectSuryoJeungList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";

        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    //교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     //년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     //교육차수

		String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");//과정&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   //과정 차수

	    Vector v_checks  = box.getVector("p_checks");
		Enumeration em   = v_checks.elements();


		ProposeBean probean = new ProposeBean();
		String v_year       = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subjcourse, ss_subjseq);
		if (v_year.equals("")) v_year = ss_gyear;

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = "\r\n select a.serno,C.grseq,C.courseseq,C.coursenm,C.subj,C.year,C.subjnm,C.subjseq,--,b.orga_ename"
//				+ " dbo.get_compnm(B.comp,2,4) compnm, dbo.get_jikwinm(B.jikwi, B.comp) jikwinm,"
				+ "\r\n get_compnm(B.comp) compnm, b.post_nm jikwinm,"
                //+ "\r\n B.userid,B.cono,b.birth_date,B.name,c.place,c.edustart,c.eduend"
				+ "\r\n B.userid,'' cono, fn_crypt('2', b.birth_date, 'knise') birth_date,B.name,c.place,c.edustart,c.eduend"
				+ "\r\n from tz_stold a,TZ_MEMBER b,vz_scsubjseq c"
				+ "\r\n where a.userid = b.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq";
				//+ " and C.eduend<to_char(sysdate,'YYYYMMDDHH24') ";
		    sql+= " and C.grcode = "+SQLString.Format(ss_grcode) +"\n";

            if (!ss_gyear.equals("ALL")) {
                sql+= " and c.gyear = "+SQLString.Format(v_year)+"\n";
            }
            if (!ss_subjcourse.equals("ALL")) {
                sql+= " and C.scsubj = "+SQLString.Format(ss_subjcourse)+"\n";
            }
            if (!ss_subjseq.equals("ALL")) {
                sql+= " and C.scsubjseq = "+SQLString.Format(ss_subjseq)+"\n";
            }

			int i=0;
			sql += " and (";
			while(em.hasMoreElements()){
				String v_userid = (String)em.nextElement();
				if ( i != 0 ) sql += " or";
				sql += " a.userid = '" + v_userid + "'";
				i++;
			}
			sql += " )";

            sql+= " order by C.course,C.cyear,C.courseseq,C.subj,C.year,C.subjseq,B.userid ";

            //System.out.println("sql============>"+sql);

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }


	/**
	* 수료증 리포트
	* @param box          receive from the form object and session
	* @return ArrayList
	*/
    public ArrayList selectSuryoJeungPrint(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";

        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    //교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     //년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     //교육차수

		String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");//과정&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   //과정 차수

		String v_serno = box.getString("serno");
		System.out.println("v_serno============>"+v_serno);
		StringTokenizer st = new StringTokenizer(v_serno, "|");

		ProposeBean probean = new ProposeBean();
		String v_year       = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subjcourse, ss_subjseq);
		if (v_year.equals("")) v_year = ss_gyear;

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            //sql = " select a.serno,substr(a.comp,0,4) comp, b.orga_ename,c.scsubjnm,C.grseq,C.courseseq,C.coursenm,C.subj,C.year,C.subjnm,C.subjseq,"
			//	+ " get_compnm(B.comp,2,4) compnm, get_jikwinm(B.jikwi, B.comp) jikwinm,"
            //    + " B.userid,B.cono,b.birth_date,B.name,decode(c.place,'','온라인') place,c.edustart,c.eduend"
			//	+ " from tz_stold a,TZ_MEMBER b,vz_scsubjseq c"
			//	+ " where a.userid = b.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq";
			//	//+ " and C.eduend<to_char(sysdate,'YYYYMMDDHH24') ";


			sql = " select                                                               \n";
            //sql+= "   a.serno,substring(a.comp, 1, 4) comp,                              \n";
			sql+= "   a.serno, comp,                              \n";
            sql+= "   c.scsubjnm,                                                        \n";
            sql+= "   C.grseq,                                                           \n";
            sql+= "   C.courseseq,                                                       \n";
            sql+= "   C.coursenm,                                                        \n";
            sql+= "   C.subj,                                                            \n";
            sql+= "   C.year,                                                            \n";
            sql+= "   C.subjnm,                                                          \n";
            sql+= "   C.subjseq,                                                         \n";
            sql+= "   C.subjseqgr,                                                       \n";
            //sql+= "   dbo.get_compnm(B.comp,2,4) compnm,                                     \n";
            //sql+= "   dbo.get_jikwinm(B.jikwi, B.comp) jikwinm,                              \n";
            sql += "     , get_deptnm(b.dept_cd) as deptnm  \n";
            sql += "     , b.post_nm as jikwinm  \n";
            sql+= "   B.userid,                                                          \n";
            sql+= "   B.cono,                                                            \n";
            sql+= "   fn_crypt('2', b.birth_date, 'knise') birth_date,                                                           \n";
            sql+= "   B.name,                                                            \n";
            sql+= "   c.place,                                 							 \n";
            sql+= "   c.edustart,                                                        \n";
            sql+= "   c.eduend                                                           \n";
            sql+= " from                                                                 \n";
            sql+= "   tz_stold a,                                                        \n";
            sql+= "   TZ_MEMBER b,                                                       \n";
            sql+= "   vz_scsubjseq c                                                     \n";
            sql+= " where                                                                \n";
            sql+= "   a.userid = b.userid                                                \n";
            sql+= "   and A.subj=C.subj                                                  \n";
            sql+= "   and A.year=C.year                                                  \n";
            sql+= "   and A.subjseq=C.subjseq                                            \n";
            if (!ss_gyear.equals("ALL")) {
              sql+= "   and c.year = "+SQLString.Format(v_year)+"\n";
            }
            if (!ss_subjcourse.equals("ALL")) {
              sql+= "   and C.scsubj = "+SQLString.Format(ss_subjcourse);
            }
            if (!ss_subjseq.equals("ALL")) {
              sql+= "   and C.scsubjseq = "+SQLString.Format(ss_subjseq);
            }

            int i=0;
			sql += " and (";
			while (st.hasMoreTokens()) {
				String temp = st.nextToken();
				if ( i != 0 ) sql += " or";
				sql += " a.serno = '" + temp + "'";
				i++;
			}
			sql += " )";
            sql+= " order by C.course,C.cyear,C.courseseq,C.subj,C.year,C.subjseq,B.userid\n";

            //System.out.println("sql_suryojeung============>"+sql);

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }


    /**
     * 훈련실시신고 파일 출력(일자범위)
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectStudentList1Zip(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	DataBox dbox                = null;
    	ListSet ls                  = null;
    	ArrayList list              = null;
    	String sql                  = "";
    	
    	String  ss_grcode   = box.getStringDefault("p_grcode","ALL");    //교육그룹
    	String  ss_gyear    = box.getStringDefault("p_year","ALL");     //년도
    	String  ss_grseq    = box.getStringDefault("p_grseq","ALL");     //교육차수
    	
    	String  ss_subj =box.getStringDefault("p_subj","ALL");           //과정&코스
    	String  ss_subjseq  = box.getStringDefault("p_subjseq","ALL");   //과정 차수
//        String  ss_comp     = box.getStringDefault("s_company","ALL");   //회사
    	
    	String  v_startdate  = box.getString("p_startdate");             //조회시작일자
    	String  v_enddate    = box.getString("p_enddate");               //조회끝일자
    	
    	ProposeBean probean = new ProposeBean();
    	String v_year       = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subj, ss_subjseq);
    	if (v_year.equals("")) v_year = ss_gyear;
    	
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		sql  = " select  \n";
			//sql += " 	birth_date||'@'||gubn||'@'||name||'@'||'31485080580'||'@'||scholar||'@'||gubn1||'@'||'N'||'@'||'N' as silsi, \n";
    		sql += " 	birth_date||'@'||name||'@'||gubn1 as silsi, \n";
			sql += "    userid, position_nm \n";
			sql += " from 					\n";
			sql += " ( select b.position_nm, b.userid, fn_crypt('2', b.birth_date, 'knise') birth_date,					\n";
			sql += "       case b.comp					\n";
			sql += "            when '1001' then ( case when post_nm='예비신입' then 'C0123' else 'C0127' end )					\n";
			sql += "            else 'C0128' end gubn,					\n";
			sql += " 	   case					\n";
			sql += " 	   		when b.comp='1001' and post_nm ='계약직' and series_nm in ('일반', '전문(프로젝트)') then'C0069'					\n";
			sql += " 			when b.comp='1001' and post_nm ='계약직' and series_nm in ('임시') then 'C0054'					\n";
			sql += " 			when b.comp='1001' and post_nm ='계약직' and series_nm in ('파견') then 'C0055'					\n";
			sql += " 			when b.comp='1002' and post_nm ='계약직'       then 'C0069'					\n";
			sql += " 			when b.comp='1003' and post_nm ='계약직 사원'  then 'C0069'					\n";
			sql += " 			when b.comp='1004' and post_nm ='계약직'         then 'C0069'					\n";
			sql += " 			when b.comp='1005' and post_nm ='계약'         then 'C0069'					\n";
			sql += " 			when b.comp='1006' and SUBSTR(b.userid,2,1)='9'  then 'C0069'					\n";
			sql += " 			when b.comp='1008' and post_nm ='계약' and SERIES_NM='파견' then 'C0055'					\n";
			sql += " 			when b.comp='1009' and SUBSTR(b.userid,2,1)='9'   then 'C0069'					\n";
			sql += " 			when b.comp='1010' and SUBSTR(b.userid,2,1)='9'   then 'C0069'					\n";
			sql += " 			when b.comp='1012' and post_nm ='계약직'  then 'C0069'					\n";
			sql += " 			when b.comp='1014' and post_nm ='계약직'   then 'C0069' else '00000' end gubn1, 					\n";
			sql += " 	   case 																					\n";
			sql += " 			when b.scholar_cd between '01' and '04' then 1											\n";
			sql += " 			when b.scholar_cd between '05' and '08' then 2											\n";
			sql += " 			when b.scholar_cd between '09' and '12' then 3											\n";
			sql += " 			when b.scholar_cd between '13' and '15' then 4											\n";
			sql += " 			when b.scholar_cd between '16' and '23' then 5											\n";
			sql += " 			when b.scholar_cd between '24' and '31' then 6 else 5 end scholar,						\n";
			sql += " 	   case b.post_nm 																		\n";
			sql += " 			when '1급' then '06'																	\n";
			sql += " 			when '2급' then '04'																	\n";
			sql += " 			when '3급' then '03'																	\n";
			sql += " 			when '4급' then '02'																	\n";
			sql += " 			else '01' end post_nm , b.name, b.comp, b.status_cd, b.emp_gubun					\n";			
			sql	+= " from tz_student a,TZ_MEMBER b,vz_scsubjseq c 											\n"
				+  " where a.userid = b.userid 								\n" +
						"and A.subj=C.subj 									\n" +
						"and A.year=C.year 									\n" +
						"and A.subjseq=C.subjseq 							\n";
				//+ " and C.eduend>to_char(sysdate,'YYYYMMDDHH24') \n";
            sql+= " and C.grcode = "+SQLString.Format(ss_grcode) +"\n";
//            sql+= " and C.year = "+SQLString.Format(v_year) +"\n";
            
            if (!ss_subj.equals("ALL")) {
              sql+= " and C.scsubj = "+SQLString.Format(ss_subj)+"\n";
            }
            if (!ss_subjseq.equals("ALL")) {
              sql+= " and C.scsubjseq = "+SQLString.Format(ss_subjseq)+"\n";
            }
            if (!ss_gyear.equals("ALL")) {
              sql+= " and c.gyear = "+SQLString.Format(ss_gyear)+"\n";
            }
            //if (!ss_comp.equals("ALL")) {
                //sql+= " and substr(b.comp,0,4) = '0101'";
            //}
            sql+= " order by C.subj,C.year,C.subjseq,B.userid )\n";	
    		
    		//System.out.println("고용보험 훈련실시신고-일자범위(OLD버전) sql==>"+sql);
    		
    		ls = connMgr.executeQuery(sql);
    		
    		while (ls.next()) {
    			dbox = ls.getDataBox();
    			dbox.put("d_silsi",    new String(dbox.getString("d_silsi").getBytes("euc-kr"),"8859_1"));
    			list.add(dbox);
    		}
    	}
    	catch (Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sql);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
    	}
    	finally {
    		if(ls != null) { try { ls.close(); }catch (Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
    	}
    	return list;
    }
	/**
	* 훈련실시신고 파일 출력(일자범위)
	* @param box          receive from the form object and session
	* @return ArrayList
	*/
    public ArrayList selectStudentList1(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";

        String  ss_grcode   = box.getStringDefault("p_grcode","ALL");    //교육그룹
        String  ss_gyear    = box.getStringDefault("p_gyear","ALL");     //년도
        String  ss_grseq    = box.getStringDefault("p_grseq","ALL");     //교육차수

		String  ss_subj =box.getStringDefault("p_subj","ALL");           //과정&코스
        String  ss_subjseq  = box.getStringDefault("p_subjseq","ALL");   //과정 차수
//        String  ss_comp     = box.getStringDefault("s_company","ALL");   //회사

		String  v_startdate  = box.getString("p_startdate");             //조회시작일자
		String  v_enddate    = box.getString("p_enddate");               //조회끝일자

		ProposeBean probean = new ProposeBean();
		String v_year       = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subj, ss_subjseq);
		if (v_year.equals("")) v_year = ss_gyear;

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            sql  = " select  \n";
			//sql += " 	birth_date||'@'||gubn||'@'||name||'@'||'31485080580'||'@'||scholar||'@'||gubn1||'@'||'N'||'@'||'N' as silsi,	 		\n";
            sql += " 	birth_date||'@'||name||'@'||gubn1 as silsi,	 		\n";
			sql += "    userid, position_nm 																							\n";
			sql += " from 																												\n";
			sql += " ( select b.position_nm, b.userid, fn_crypt('2', b.birth_date, 'knise') birth_date,																			\n";
			sql += "       case b.comp																									\n";
			sql += "            when '1001' then ( case when post_nm='예비신입' then 'C0123' else 'C0127' end )							\n";
			sql += "            else 'C0128' end gubn,																					\n";
			sql += " 	   case																											\n";
			sql += " 	   		when b.comp='1001' and post_nm ='계약직' and series_nm in ('일반', '전문(프로젝트)') then'C0069'				\n";
			sql += " 			when b.comp='1001' and post_nm ='계약직' and series_nm in ('임시') then 'C0054'							\n";
			sql += " 			when b.comp='1001' and post_nm ='계약직' and series_nm in ('파견') then 'C0055'							\n";
			sql += " 			when b.comp='1002' and post_nm ='계약직'       then 'C0069'												\n";
			sql += " 			when b.comp='1003' and post_nm ='계약직 사원'  then 'C0069'												\n";
			sql += " 			when b.comp='1004' and post_nm ='계약직'         then 'C0069'												\n";
			sql += " 			when b.comp='1005' and post_nm ='계약'         then 'C0069'												\n";
			sql += " 			when b.comp='1006' and SUBSTR(b.userid,2,1)='9'  then 'C0069'											\n";
			sql += " 			when b.comp='1008' and post_nm ='계약' and SERIES_NM='파견' then 'C0055'									\n";
			sql += " 			when b.comp='1009' and SUBSTR(b.userid,2,1)='9'   then 'C0069'											\n";
			sql += " 			when b.comp='1010' and SUBSTR(b.userid,2,1)='9'   then 'C0069'											\n";
			sql += " 			when b.comp='1012' and post_nm ='계약직'  then 'C0069'													\n";
			sql += " 			when b.comp='1014' and post_nm ='계약직'   then 'C0069' else '00000' end gubn1, 							\n";
			sql += " 	   case 																										\n";
			sql += " 			when b.scholar_cd between '01' and '04' then 1															\n";
			sql += " 			when b.scholar_cd between '05' and '08' then 2															\n";
			sql += " 			when b.scholar_cd between '09' and '12' then 3															\n";
			sql += " 			when b.scholar_cd between '13' and '15' then 4															\n";
			sql += " 			when b.scholar_cd between '16' and '23' then 5															\n";
			sql += " 			when b.scholar_cd between '24' and '31' then 6 else 5 end scholar,										\n";
			sql += " 	   case b.post_nm 																								\n";
			sql += " 			when '1급' then '06'																						\n";
			sql += " 			when '2급' then '04'																						\n";
			sql += " 			when '3급' then '03'																						\n";
			sql += " 			when '4급' then '02'																						\n";
			sql += " 			else '01' end post_nm , b.name, b.comp, b.status_cd, b.emp_gubun										\n";			
			sql	+= " from tz_student a,TZ_MEMBER b,vz_scsubjseq c 																		\n"
				+  " where a.userid = b.userid 																							\n" +
						"and A.subj=C.subj 																								\n" +
						"and A.year=C.year 																								\n" +
						"and A.subjseq=C.subjseq 																						\n" +
						"and '"+v_startdate+"' <= C.edustart and C.edustart <= '"+v_enddate+"23'";

            if (!ss_subj.equals("ALL")) {                                     // 과정코드
              sql+= " and C.scsubj = "+SQLString.Format(ss_subj)+"																		\n";
            }

            sql+= " order by C.subj,C.year,C.subjseq,B.userid ) 																		\n";

            //System.out.println("고용보험 훈련실시신고-일자범위(OLD버전) sql==>"+sql);

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("d_silsi", new String(dbox.getString("d_silsi").getBytes("euc-kr"),"8859_1"));
                list.add(dbox);
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }


	/**
	* 훈련실시신고 파일출력
	* @param box          receive from the form object and session
	* @return ArrayList
	*/
    public ArrayList selectStudentList2(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";

        String  ss_grcode   = box.getStringDefault("p_grcode","ALL");    //교육그룹
        String  ss_gyear    = box.getStringDefault("p_gyear","ALL");     //년도
        String  ss_grseq    = box.getStringDefault("p_grseq","ALL");     //교육차수

		String  ss_subj =box.getStringDefault("p_subj","ALL");//과정&코스
        String  ss_subjseq  = box.getStringDefault("p_subjseq","ALL");   //과정 차수
        String  ss_comp     = box.getStringDefault("s_company","ALL");   //회사

		ProposeBean probean = new ProposeBean();
		String v_year       = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subj, ss_subjseq);
		if (v_year.equals("")) v_year = ss_gyear;

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
			sql  = " select  \n";
			//sql += " 	birth_date||'@'||gubn||'@'||name||'@'||'31485080580'||'@'||scholar||'@'||gubn1||'@'||'N'||'@'||'N' as silsi, \n";
			sql += " 	birth_date||'@'||name||'@'||gubn1 as silsi, \n";
			sql += "    userid, position_nm \n";
			sql += " from 					\n";
			sql += " ( select b.position_nm, b.userid, fn_crypt('2', b.birth_date, 'knise') birth_date,					\n";
			sql += "       case b.comp					\n";
			sql += "            when '1001' then ( case when post_nm='예비신입' then 'C0123' else 'C0127' end )					\n";
			sql += "            else 'C0128' end gubn,					\n";
			sql += " 	   case					\n";
			sql += " 	   		when b.comp='1001' and post_nm ='계약직' and series_nm in ('일반', '전문(프로젝트)') then'C0069'					\n";
			sql += " 			when b.comp='1001' and post_nm ='계약직' and series_nm in ('임시') then 'C0054'					\n";
			sql += " 			when b.comp='1001' and post_nm ='계약직' and series_nm in ('파견') then 'C0055'					\n";
			sql += " 			when b.comp='1002' and post_nm ='계약직'       then 'C0069'					\n";
			sql += " 			when b.comp='1003' and post_nm ='계약직 사원'  then 'C0069'					\n";
			sql += " 			when b.comp='1004' and post_nm ='계약직'         then 'C0069'					\n";
			sql += " 			when b.comp='1005' and post_nm ='계약'         then 'C0069'					\n";
			sql += " 			when b.comp='1006' and SUBSTR(b.userid,2,1)='9'  then 'C0069'					\n";
			sql += " 			when b.comp='1008' and post_nm ='계약' and SERIES_NM='파견' then 'C0055'					\n";
			sql += " 			when b.comp='1009' and SUBSTR(b.userid,2,1)='9'   then 'C0069'					\n";
			sql += " 			when b.comp='1010' and SUBSTR(b.userid,2,1)='9'   then 'C0069'					\n";
			sql += " 			when b.comp='1012' and post_nm ='계약직'  then 'C0069'					\n";
			sql += " 			when b.comp='1014' and post_nm ='계약직'   then 'C0069' else '00000' end gubn1, 					\n";
			sql += " 	   case 																					\n";
			sql += " 			when b.scholar_cd between '01' and '04' then 1											\n";
			sql += " 			when b.scholar_cd between '05' and '08' then 2											\n";
			sql += " 			when b.scholar_cd between '09' and '12' then 3											\n";
			sql += " 			when b.scholar_cd between '13' and '15' then 4											\n";
			sql += " 			when b.scholar_cd between '16' and '23' then 5											\n";
			sql += " 			when b.scholar_cd between '24' and '31' then 6 else 5 end scholar,						\n";
			sql += " 	   case b.post_nm 																		\n";
			sql += " 			when '1급' then '06'																	\n";
			sql += " 			when '2급' then '04'																	\n";
			sql += " 			when '3급' then '03'																	\n";
			sql += " 			when '4급' then '02'																	\n";
			sql += " 			else '01' end post_nm , b.name, b.comp, b.status_cd, b.emp_gubun					\n";			
			sql	+= " from tz_student a,TZ_MEMBER b,vz_scsubjseq c 											\n"
				+  " where a.userid = b.userid 								\n" +
						"and A.subj=C.subj 									\n" +
						"and A.year=C.year 									\n" +
						"and A.subjseq=C.subjseq 							\n";
				//+ " and C.eduend>to_char(sysdate,'YYYYMMDDHH24') \n";
            sql+= " and C.grcode = "+SQLString.Format(ss_grcode) +"\n";
//            sql+= " and C.year = "+SQLString.Format(v_year) +"\n";
            
            if (!ss_subj.equals("ALL")) {
              sql+= " and C.scsubj = "+SQLString.Format(ss_subj)+"\n";
            }
            if (!ss_subjseq.equals("ALL")) {
              sql+= " and C.scsubjseq = "+SQLString.Format(ss_subjseq)+"\n";
            }
            if (!ss_gyear.equals("ALL")) {
              sql+= " and c.gyear = "+SQLString.Format(ss_gyear)+"\n";
            }
            if (!ss_comp.equals("ALL")) {
                //sql+= " and substr(b.comp,0,4) = '0101'";
            }
            sql+= " order by C.subj,C.year,C.subjseq,B.userid )\n";

            //System.out.println("고용보험 훈련실시신고(OLD버전) sql==>"+sql);

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                dbox.put("d_silsi", new String(dbox.getString("d_silsi").getBytes("euc-kr"),"8859_1"));
                list.add(dbox);
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

	/**
	* 수료증 발급대장 txt파일 인쇄
	* @param box          receive from the form object and session
	* @return ArrayList
	*/
    public ArrayList selectStudentList3(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";

        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    //교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     //년도
        //String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     //교육차수
        String  ss_grseq    = "0001";     //교육차수

        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    //과정분류
		String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");    //과정분류

		String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");//과정&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   //과정 차수
        String  ss_comp     = box.getStringDefault("s_company","ALL");   //회사

		ProposeBean probean = new ProposeBean();
		String v_year       = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subjcourse, ss_subjseq);
		if (v_year.equals("")) v_year = ss_gyear;

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            //sql = " select C.grseq,C.course,C.cyear,C.courseseq,C.coursenm,C.subj,C.year,C.subjnm,C.subjseq,\n"
			//	+ " get_compnm(B.comp,2,4) compnm, get_jikwinm(B.jikwi, B.comp) jikwinm,\n"
            //   + " B.userid,B.cono, b.orga_ename,b.birth_date,B.name \n"
			//	+ " from tz_student a,TZ_MEMBER b,vz_scsubjseq c \n"

            sql  = "select b.name  \n";
            sql += "     , len(b.name) namelength  \n";
            sql += "     , fn_crypt('2', b.birth_date, 'knise') birth_date  \n";
            sql += "     , case when b.office_gbn = 'Y' then '01'  \n";
            sql += "            else '04'  \n";
            sql += "       end office_gbn  \n";
            sql += "--     , b.acceace  \n";
            sql += "     , null as major  \n";
            sql += "     , null as jikjong  \n";
            sql += "     , case when b.jikwi = '*1' then '06'  \n";
            sql += "            when b.jikwi = '*5' then '06'  \n";
            sql += "            when b.jikwi = '*6' then '06'  \n";
            sql += "            when b.jikwi = '*7' then '06'  \n";
            sql += "            when b.jikwi = '*8' then '06'  \n";
            sql += "            when b.jikwi = '*A' then '06'  \n";
            sql += "            when b.jikwi = '*B' then '06'  \n";
            sql += "            when b.jikwi = '01' then '06'  \n";
            sql += "            when b.jikwi = '02' then '06'  \n";
            sql += "            when b.jikwi = '03' then '06'  \n";
            sql += "            when b.jikwi = '04' then '06'  \n";
            sql += "            when b.jikwi = '05' then '06'  \n";
            sql += "            when b.jikwi = '09' then '06'  \n";
            sql += "            when b.jikwi = '10' then '06'  \n";
            sql += "            when b.jikwi = '11' then '06'  \n";
            sql += "            when b.jikwi = '12' then '06'  \n";
            sql += "            when b.jikwi = '13' then '06'  \n";
            sql += "            when b.jikwi = '20' then '06'  \n";
            sql += "            when b.jikwi = '22' then '04'  \n";
            sql += "            when b.jikwi = '23' then '04'  \n";
            sql += "            when b.jikwi = '24' then '05'  \n";
            sql += "            when b.jikwi = '25' then '05'  \n";
            sql += "            when b.jikwi = '26' then '03'  \n";
            sql += "            when b.jikwi = '27' then '03'  \n";
            sql += "            when b.jikwi = '28' then '02'  \n";
            sql += "            when b.jikwi = '29' then '02'  \n";
            sql += "            when b.jikwi = '39' then '01'  \n";
            sql += "            when b.jikwi = '50' then '01'  \n";
            sql += "            when b.jikwi = '57' then '04'  \n";
            sql += "            when b.jikwi = '58' then '03'  \n";
            sql += "            when b.jikwi = '59' then '02'  \n";
            sql += "            when b.jikwi = '61' then '01'  \n";
            sql += "            when b.jikwi = '62' then '01'  \n";
            sql += "            when b.jikwi = '63' then '01'  \n";
            sql += "            when b.jikwi = '64' then '01'  \n";
            sql += "            when b.jikwi = '65' then '01'  \n";
            sql += "            when b.jikwi = '66' then '01'  \n";
            sql += "            when b.jikwi = '67' then '01'  \n";
            sql += "            when b.jikwi = '68' then '07'  \n";
            sql += "            when b.jikwi = '69' then '04'  \n";
            sql += "            when b.jikwi = '70' then '07'  \n";
            sql += "            when b.jikwi = '71' then '03'  \n";
            sql += "            when b.jikwi = '72' then '05'  \n";
            sql += "            when b.jikwi = '73' then '01'  \n";
            sql += "            when b.jikwi = '74' then '01'  \n";
            sql += "            when b.jikwi = '75' then '01'  \n";
            sql += "            when b.jikwi = '76' then '01'  \n";
            sql += "            when b.jikwi = '77' then '04'  \n";
            sql += "            when b.jikwi = '78' then '07'  \n";
            sql += "            when b.jikwi = '79' then '07'  \n";
            sql += "            when b.jikwi = '80' then '07'  \n";
            sql += "            when b.jikwi = '81' then '04'  \n";
            sql += "            when b.jikwi = '82' then '04'  \n";
            sql += "            when b.jikwi = '83' then '01'  \n";
            sql += "            when b.jikwi = '84' then '03'  \n";
            sql += "            when b.jikwi = '85' then '06'  \n";
            sql += "            when b.jikwi = '86' then '06'  \n";
            sql += "            when b.jikwi = '87' then '01'  \n";
            sql += "            when b.jikwi = '88' then '01'  \n";
            sql += "            when b.jikwi = '89' then '01'  \n";
            sql += "            when b.jikwi = '90' then '01'  \n";
            sql += "            when b.jikwi = '91' then '02'  \n";
            sql += "            when b.jikwi = '92' then '01'  \n";
            sql += "            when b.jikwi = '99' then '02'  \n";
            sql += "            else '07'  \n";
            sql += "       end jikup  \n";
            sql += "     , null as judang  \n";
            sql += "     , null as judangtime  \n";
            sql += "from   tz_stold a  \n";
            sql += "     , TZ_MEMBER b  \n";
            sql += "     , vz_scsubjseq c  \n";
            sql += "where  c.isclosed = 'Y'  \n";
            sql += "and    a.userid = b.userid  \n";
            sql += "and    A.subj = C.subj  \n";
            sql += "and    A.year = C.year  \n";
            sql += "and    A.subjseq = C.subjseq  \n";
				//+ " and C.eduend>to_char(sysdate,'YYYYMMDDHH24') \n";
            sql+= " and C.grcode = "+SQLString.Format(ss_grcode) +"\n";
            sql+= " and C.year = "+SQLString.Format(v_year) +"\n";

            if (!ss_uclass.equals("ALL")) {
                sql+= " and C.scupperclass = "+SQLString.Format(ss_uclass)+"\n";
            }
            if (!ss_mclass.equals("ALL")) {
                sql+= " and C.scmiddleclass = "+SQLString.Format(ss_mclass)+"\n";
            }

            if (!ss_subjcourse.equals("ALL")) {
              sql+= " and C.scsubj = "+SQLString.Format(ss_subjcourse)+"\n";
            }
            if (!ss_subjseq.equals("ALL")) {
              sql+= " and C.scsubjseq = "+SQLString.Format(ss_subjseq)+"\n";
            }
            if (!ss_gyear.equals("ALL")) {
              sql+= " and c.gyear = "+SQLString.Format(ss_gyear)+"\n";
            }
            if (!ss_comp.equals("ALL")) {
                //sql+= " and substr(b.comp,0,4) = '0101'";
            }
            sql+= " order by C.course,C.cyear,C.courseseq,C.subj,C.year,C.subjseq,B.userid \n";

            //System.out.println("고용보험 수료증발급대장 Text화일 sql===>"+sql+"<===\n");

            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }


    /**
	* 과정별 로스팬 카운트
	* @param box          receive from the form object and session
	* @return ArrayList
	*/

    public String getRowspanCnt(DBConnectionManager connMgr, RequestBox box) throws Exception {
//        DataBox dbox                = null;
        ListSet ls                  = null;
//        ArrayList list              = null;
        String sql                  = "";
      	String cnt = "0";

      	String v_subj      = box.getString("p_subj");
//      	String v_subjseq   = box.getString("p_subjseq");
//      	String v_year      = box.getString("p_year");
      	String v_startdate = box.getString("p_startdate");
      	String v_enddate   = box.getString("p_enddate");

        try {
//            list = new ArrayList();

             sql+= " select count(*) cnt from tz_subjseq  a             \n";
             sql+= " where                                         \n";
             //sql += "  edustart between '"+v_startdate+"' and '"+v_enddate+"'";
             sql+= "   '"+v_startdate+"' <= edustart and edustart <= '"+v_enddate+"23'";
             sql+= "   and subj = '"+v_subj+"'                               \n";
             sql+= "   and (select count(*) from tz_student x where a.subj = x.subj and a.subjseq = x.subjseq and a.year = x.year ) > 0 \n";
             //sql+= "   and subjseq = '"+v_subjseq+"'                            \n";
             //sql+= "   and year = '"+v_year+"'                               \n";

			//System.out.println("sql_studentlist==>>"+sql);

            ls = connMgr.executeQuery(sql);

			if(ls.next()) {
                cnt = ls.getString("cnt");
            }
            
       }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        return cnt;
    }
    
    /**
     * 고용보험 기간별 인원현황(신청자)
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectPriodProposeList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";

        String ss_grcode   = box.getStringDefault  ( "s_grcode"        , "ALL" ); // 교육그룹
        //String ss_gyear    = box.getStringDefault  ( "s_gyear"         , "ALL" );  // 년도
        String ss_grseq    = box.getStringDefault  ( "s_grseq"         , "ALL" );  // 교육기수
        String ss_uclass   = box.getStringDefault  ( "s_upperclass"    , "ALL" );  // 과목분류
        String ss_mclass   = box.getStringDefault  ( "s_middleclass"   , "ALL" );  // 과목분류
        String ss_lclass   = box.getStringDefault  ( "s_lowerclass"    , "ALL" );  // 과목분류
        String ss_subjcourse=box.getStringDefault  ( "s_subjcourse"    , "ALL" );  // 과목&코스
		String v_startdt = box.getString("p_startdt");
		String v_enddt = box.getString("p_enddt");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = "select a.subj  \n";
            sql += "     , a.edustart  \n";
            sql += "     , a.eduend  \n";
            sql += "     , a.year  \n";
            sql += "     , a.subjseq  \n";
            sql += "     , get_compnm(c.comp) as compnm  \n";
            sql += "     , fn_crypt('2', c.birth_date, 'knise') birth_date  \n";
            sql += "     , b.userid  \n";
            sql += "     , c.name  \n";
            sql += "     , c.position_nm as deptnm  \n";
            sql += "     , c.lvl_nm as jikwinm  \n";
            sql += "     , c.hometel  \n";
            sql += "     , c.handphone  \n";
            sql += "     , c.email  \n";
            sql += "     , to_char(to_date(b.appdate, 'YYYYMMDDHH24MISS'), 'YYYY.MM.DD HH24:MI') as appdate  \n";
            sql += "     , b.chkfirst  \n";
            sql += "     , b.chkfinal  \n";
            sql += "     , d.subjnm  \n";
            sql += "     , d.biyong  \n";
            sql += "     , d.goyongpricemajor  \n";
            sql += "     , d.goyongpriceminor  \n";
            
            sql += "from   tz_subjseq a  \n";
            sql += "     , tz_propose b  \n";
            sql += "     , tz_member c  \n";
            sql += "     , tz_subj d  \n";
            sql += "where  a.subj = b.subj  \n";
            sql += "and    a.year = b.year  \n";
            sql += "and    a.subjseq = b.subjseq  \n";
            sql += "and    b.userid = c.userid  \n";
            sql += "and    a.subj = d.subj \n";
            sql += "and    a.isgoyong = 'Y'  \n";

            if (!"ALL".equals(ss_grcode)) {
            	sql +="and    a.grcode = " + StringManager.makeSQL(ss_grcode);
            }
            
            /* 고용보험 - 기간별인운현황의 검색폼에서 연도 제외
            if (!"ALL".equals(ss_gyear)) {
            	sql +="and    a.gyear = " + StringManager.makeSQL(ss_gyear);
            }
            */
            if (!"ALL".equals(ss_grseq)) {
            	sql +="and    a.grseq = " + StringManager.makeSQL(ss_grseq);
            }
            if (!"ALL".equals(ss_uclass)) {
            	sql +="and    d.upperclass = " + StringManager.makeSQL(ss_uclass);
            }
            if (!"ALL".equals(ss_mclass)) {
            	sql +="and    d.middleclass = " + StringManager.makeSQL(ss_mclass);
            }
            if (!"ALL".equals(ss_lclass)) {
            	sql +="and    d.lowerclass = " + StringManager.makeSQL(ss_lclass);
            }
            if (!"ALL".equals(ss_subjcourse)) {
            	sql +="and    a.subj = " + StringManager.makeSQL(ss_subjcourse);
            }            
            sql += "and   (substr(edustart, 1, 8) between " + SQLString.Format(v_startdt) + " and " + SQLString.Format(v_enddt) + "  \n";
            sql += "    or substr(eduend, 1, 8) between " + SQLString.Format(v_startdt) + " and " + SQLString.Format(v_enddt) + ")  \n";
            //sql += "order by a.subj, d.subjnm, a.subjseq  \n";
            sql += "order by compnm, d.subjnm, a.subjseq  \n";

            ls = connMgr.executeQuery(sql);

			while (ls.next()) {
                dbox = ls.getDataBox();

                list.add(dbox);
            }
       }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

    /**
     * 고용보험 기간별 인원현황(신청자) - 노동부신고엑셀출력
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectPriodProposeListByExcel(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";

        String ss_grcode   = box.getStringDefault  ( "s_grcode"        , "ALL" ); // 교육그룹
        String ss_gyear    = box.getStringDefault  ( "s_gyear"         , "ALL" );  // 년도
        String ss_grseq    = box.getStringDefault  ( "s_grseq"         , "ALL" );  // 교육기수
        String ss_uclass   = box.getStringDefault  ( "s_upperclass"    , "ALL" );  // 과목분류
        String ss_mclass   = box.getStringDefault  ( "s_middleclass"   , "ALL" );  // 과목분류
        String ss_lclass   = box.getStringDefault  ( "s_lowerclass"    , "ALL" );  // 과목분류
        String ss_subjcourse=box.getStringDefault  ( "s_subjcourse"    , "ALL" );  // 과목&코스

		String v_startdt = box.getString("p_startdt");
		String v_enddt = box.getString("p_enddt");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = "\n select a.subj "
            	+ "\n      , a.subjnm "
            	+ "\n      , b.subjseq "
            	+ "\n      , to_char(to_date(substr(b.edustart, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') as edustart "
            	+ "\n      , to_char(to_date(substr(b.eduend, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') as eduend "
            	+ "\n      , c.name "
            	+ "\n      , c.tuserid "
            	+ "\n      , c.positionnm "
            	+ "\n      , c.birth_date "
            	+ "\n      , (select count(*) "
            	+ "\n         from   tz_propose "
            	+ "\n         where  subj = b.subj "
            	+ "\n         and    year = b.year "
            	+ "\n         and    subjseq = b.subjseq "
            	+ "\n         and    chkfinal = 'Y') as propose_cnt "
            	+ "\n      , get_name(b.muserid) as musernm "
            	+ "\n from   tz_subj a "
            	+ "\n      , tz_subjseq b "
            	+ "\n      , (select a.subj, a.year, a.subjseq "
            	+ "\n              , a.tuserid, b.name, fn_crypt('2', b.birth_date, 'knise') birth_date "
            	+ "\n              , get_deptnm(b.hq_org_cd) || ' ' || get_deptnm(b.dept_cd) as positionnm "
            	+ "\n         from   (select subj, year, subjseq, max(tuserid) as tuserid "
            	+ "\n                 from   tz_classtutor "
            	+ "\n                 group  by subj, year, subjseq) a "
            	+ "\n              , tz_member b "
            	+ "\n         where  a.tuserid = b.userid) c "
            	+ "\n where  a.subj = b.subj "
            	+ "\n and    b.subj = c.subj(+) "
            	+ "\n and    b.year = c.year(+) "
            	+ "\n and    b.subjseq = c.subjseq(+) "
            	+ "\n and    b.isgoyong = 'Y'"
            	+ "\n and    (substr(b.edustart, 1, 8) between " + StringManager.makeSQL(v_startdt) + " and " + StringManager.makeSQL(v_enddt)
            	+ "\n        or substr(b.eduend, 1, 8) between " + StringManager.makeSQL(v_startdt) + " and " + StringManager.makeSQL(v_enddt) + ")";

            if (!"ALL".equals(ss_grcode)) {
            	sql +="\n and    b.grcode = " + StringManager.makeSQL(ss_grcode);
            }
            if (!"ALL".equals(ss_gyear)) {
            	sql +="\n and    b.gyear = " + StringManager.makeSQL(ss_gyear);
            }
            if (!"ALL".equals(ss_grseq)) {
            	sql +="\n and    b.grseq = " + StringManager.makeSQL(ss_grseq);
            }
            if (!"ALL".equals(ss_uclass)) {
            	sql +="\n and    a.upperclass = " + StringManager.makeSQL(ss_uclass);
            }
            if (!"ALL".equals(ss_mclass)) {
            	sql +="\n and    a.middleclass = " + StringManager.makeSQL(ss_mclass);
            }
            if (!"ALL".equals(ss_lclass)) {
            	sql +="\n and    a.lowerclass = " + StringManager.makeSQL(ss_lclass);
            }
            if (!"ALL".equals(ss_subjcourse)) {
            	sql +="\n and    b.subj = " + StringManager.makeSQL(ss_subjcourse);
            }

            sql +="\n order by a.subj, a.subjnm, b.subjseq  ";
            
            ls = connMgr.executeQuery(sql);

			while (ls.next()) {
                dbox = ls.getDataBox();

                list.add(dbox);
            }
       }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

    /**
     * 고용보험 기간별 인원현황(입과자)
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectPriodStudentList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
        
        String ss_grcode   = box.getStringDefault  ( "s_grcode"        , "ALL" ); // 교육그룹
        String ss_gyear    = box.getStringDefault  ( "s_gyear"         , "ALL" );  // 년도
        String ss_grseq    = box.getStringDefault  ( "s_grseq"         , "ALL" );  // 교육기수
        String ss_uclass   = box.getStringDefault  ( "s_upperclass"    , "ALL" );  // 과목분류
        String ss_mclass   = box.getStringDefault  ( "s_middleclass"   , "ALL" );  // 과목분류
        String ss_lclass   = box.getStringDefault  ( "s_lowerclass"    , "ALL" );  // 과목분류
        String ss_subjcourse=box.getStringDefault  ( "s_subjcourse"    , "ALL" );  // 과목&코스
        
		String v_startdt = box.getString("p_startdt");
		String v_enddt = box.getString("p_enddt");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql  = "select a.subj  \n";
            sql += "     , a.year  \n";
            sql += "     , a.subjseq  \n";
            sql += "     , get_compnm(c.comp) as compnm  \n";
            sql += "     , to_char(to_date(substr(a.edustart, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') as edustartdt  \n";
            sql += "     , to_char(to_date(substr(a.eduend, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') as eduenddt  \n";
            sql += "     , fn_crypt('2', c.birth_date, 'knise') birth_date  \n";
            sql += "     , b.userid  \n";
            sql += "     , c.name  \n";
            sql += "     , get_deptnm(c.dept_cd) as deptnm  \n";
            sql += "     , c.post_nm as jikwinm  \n";
            sql += "     , b.avtstep  \n";
            sql += "     , b.avhtest  \n";
            sql += "     , b.avmtest  \n";
            sql += "     , b.avftest  \n";
            sql += "     , b.avetc1  \n";
            sql += "     , b.avetc2  \n";
            sql += "     , b.avact  \n";
            sql += "     , b.avreport  \n";
            sql += "     , b.score  \n";
            sql += "     , b.isgraduated  \n";
            sql += "     , case when d.userid is null then 'N'  \n";
            sql += "            else 'Y'  \n";
            sql += "       end as gruserid  \n";
            sql += "     , e.subjnm  \n";
            sql += "from   tz_subjseq a  \n";
            sql += "     , tz_student b  \n";
            sql += "     , tz_member c  \n";
            sql += "     , tz_stold d  \n";
            sql += "     , tz_subj e  \n";
            sql += "where  a.subj = b.subj  \n";
            sql += "and    a.year = b.year  \n";
            sql += "and    a.subjseq = b.subjseq  \n";
            sql += "and    b.userid = c.userid  \n";
            sql += "and    b.subj = d.subj(+)  \n";
            sql += "and    b.year = d.year(+)  \n";
            sql += "and    b.subjseq = d.subjseq(+)  \n";
            sql += "and    b.userid = d.userid(+)  \n";
            sql += "and    a.subj = e.subj  \n";
            sql += "and    a.isgoyong = 'Y'  \n";

            if (!"ALL".equals(ss_grcode)) {
            	sql +="\n and    a.grcode = " + StringManager.makeSQL(ss_grcode);
            }
            if (!"ALL".equals(ss_gyear)) {
            	sql +="\n and    a.gyear = " + StringManager.makeSQL(ss_gyear);
            }
            if (!"ALL".equals(ss_grseq)) {
            	sql +="\n and    a.grseq = " + StringManager.makeSQL(ss_grseq);
            }
            if (!"ALL".equals(ss_uclass)) {
            	sql +="\n and    e.upperclass = " + StringManager.makeSQL(ss_uclass);
            }
            if (!"ALL".equals(ss_mclass)) {
            	sql +="\n and    e.middleclass = " + StringManager.makeSQL(ss_mclass);
            }
            if (!"ALL".equals(ss_lclass)) {
            	sql +="\n and    e.lowerclass = " + StringManager.makeSQL(ss_lclass);
            }
            if (!"ALL".equals(ss_subjcourse)) {
            	sql +="\n and    a.subj = " + StringManager.makeSQL(ss_subjcourse);
            }
            
            sql += "and   (substr(a.edustart, 1, 8) between " + SQLString.Format(v_startdt) + " and " + SQLString.Format(v_enddt) + "  \n";
            sql += "    or substr(a.eduend, 1, 8) between " + SQLString.Format(v_startdt) + " and " + SQLString.Format(v_enddt) + ")  \n";
            sql += "order by a.subj, e.subjnm, a.subjseq  \n";

            ls = connMgr.executeQuery(sql);

			while (ls.next()) {
                dbox = ls.getDataBox();

                list.add(dbox);
            }
       }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
    
    /**
     * 고용보험 기간별 인원현황(수료자)
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectPriodGraduatedList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
        
        String ss_grcode   = box.getStringDefault  ( "s_grcode"        , "ALL" ); // 교육그룹
        String ss_gyear    = box.getStringDefault  ( "s_gyear"         , "ALL" );  // 년도
        String ss_grseq    = box.getStringDefault  ( "s_grseq"         , "ALL" );  // 교육기수
        String ss_uclass   = box.getStringDefault  ( "s_upperclass"    , "ALL" );  // 과목분류
        String ss_mclass   = box.getStringDefault  ( "s_middleclass"   , "ALL" );  // 과목분류
        String ss_lclass   = box.getStringDefault  ( "s_lowerclass"    , "ALL" );  // 과목분류
        String ss_subjcourse=box.getStringDefault  ( "s_subjcourse"    , "ALL" );  // 과목&코스
        
		String v_startdt = box.getString("p_startdt");
		String v_enddt = box.getString("p_enddt");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql  = "select a.subj  \n";
            sql += "     , a.year  \n";
            sql += "     , a.subjseq  \n";
            sql += "     , get_compnm(c.comp) as compnm  \n";
            sql += "     , to_char(to_date(substr(a.edustart, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') as edustartdt  \n";
            sql += "     , to_char(to_date(substr(a.eduend, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') as eduenddt  \n";
            sql += "     , fn_crypt('2', c.birth_date, 'knise') birth_date  \n";
            sql += "     , b.userid  \n";
            sql += "     , c.name  \n";
            sql += "     , get_deptnm(c.dept_cd) as deptnm  \n";
            sql += "     , c.post_nm as jikwinm  \n";
            sql += "     , b.avtstep  \n";
            sql += "     , b.avhtest  \n";
            sql += "     , b.avmtest  \n";
            sql += "     , b.avftest  \n";
            sql += "     , b.avetc1  \n";
            sql += "     , b.avetc2  \n";
            sql += "     , b.avact  \n";
            sql += "     , b.avreport  \n";
            sql += "     , b.score  \n";
            sql += "     , b.isgraduated  \n";
            sql += "     , case when d.userid is null then 'N'  \n";
            sql += "            else 'Y'  \n";
            sql += "       end as gruserid  \n";
            sql += "     , e.subjnm  \n";
            sql += "from   tz_subjseq a  \n";
            sql += "     , tz_student b  \n";
            sql += "     , tz_member c  \n";
            sql += "     , tz_stold d  \n";
            sql += "     , tz_subj e  \n";
            sql += "where  a.subj = b.subj  \n";
            sql += "and    a.year = b.year  \n";
            sql += "and    a.subjseq = b.subjseq  \n";
            sql += "and    b.userid = c.userid  \n";
            sql += "and    b.subj = d.subj  \n";
            sql += "and    b.year = d.year  \n";
            sql += "and    b.subjseq = d.subjseq  \n";
            sql += "and    b.userid = d.userid  \n";
            sql += "and    a.subj = e.subj  \n";
            sql += "and    a.isgoyong = 'Y'  \n";
            sql += "and    d.isgraduated = 'Y'  \n";

            if (!"ALL".equals(ss_grcode)) {
            	sql +="\n and    a.grcode = " + StringManager.makeSQL(ss_grcode);
            }
            if (!"ALL".equals(ss_gyear)) {
            	sql +="\n and    a.gyear = " + StringManager.makeSQL(ss_gyear);
            }
            if (!"ALL".equals(ss_grseq)) {
            	sql +="\n and    a.grseq = " + StringManager.makeSQL(ss_grseq);
            }
            if (!"ALL".equals(ss_uclass)) {
            	sql +="\n and    e.upperclass = " + StringManager.makeSQL(ss_uclass);
            }
            if (!"ALL".equals(ss_mclass)) {
            	sql +="\n and    e.middleclass = " + StringManager.makeSQL(ss_mclass);
            }
            if (!"ALL".equals(ss_lclass)) {
            	sql +="\n and    e.lowerclass = " + StringManager.makeSQL(ss_lclass);
            }
            if (!"ALL".equals(ss_subjcourse)) {
            	sql +="\n and    a.subj = " + StringManager.makeSQL(ss_subjcourse);
            }
            
            sql += "and   (substr(a.edustart, 1, 8) between " + SQLString.Format(v_startdt) + " and " + SQLString.Format(v_enddt) + "  \n";
            sql += "    or substr(a.eduend, 1, 8) between " + SQLString.Format(v_startdt) + " and " + SQLString.Format(v_enddt) + ")  \n";
            sql += "order by a.subj, e.subjnm, a.subjseq  \n";

            ls = connMgr.executeQuery(sql);

			while (ls.next()) {
                dbox = ls.getDataBox();

                list.add(dbox);
            }
       }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
   
    /**
     * 고용보험 기간별 인원현황(미수료자)  --->운영자의 요청으로 전체수료현황 항목으로 변경
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectPriodNotGraduatedList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";

        String ss_grcode   = box.getStringDefault  ( "s_grcode"        , "ALL" ); // 교육그룹
        String ss_gyear    = box.getStringDefault  ( "s_gyear"         , "ALL" );  // 년도
        String ss_grseq    = box.getStringDefault  ( "s_grseq"         , "ALL" );  // 교육기수
        String ss_uclass   = box.getStringDefault  ( "s_upperclass"    , "ALL" );  // 과목분류
        String ss_mclass   = box.getStringDefault  ( "s_middleclass"   , "ALL" );  // 과목분류
        String ss_lclass   = box.getStringDefault  ( "s_lowerclass"    , "ALL" );  // 과목분류
        String ss_subjcourse=box.getStringDefault  ( "s_subjcourse"    , "ALL" );  // 과목&코스
        
		String v_startdt = box.getString("p_startdt");
		String v_enddt = box.getString("p_enddt");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql  = "select a.subj  \n";
            sql += "     , a.year  \n";
            sql += "     , a.subjseq  \n";
            sql += "     , get_compnm(c.comp) as compnm  \n";
            sql += "     , to_char(to_date(substr(a.edustart, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') as edustartdt  \n";
            sql += "     , to_char(to_date(substr(a.eduend, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') as eduenddt  \n";
            sql += "     , fn_crypt('2', c.birth_date, 'knise') birth_date  \n";
            sql += "     , b.userid  \n";
            sql += "     , c.name  \n";
            sql += "     , get_deptnm(c.dept_cd) as deptnm  \n";
            sql += "     , c.post_nm as jikwinm  \n";
            sql += "     , b.avtstep  \n";
            sql += "     , b.avhtest  \n";
            sql += "     , b.avmtest  \n";
            sql += "     , b.avftest  \n";
            sql += "     , b.avetc1  \n";
            sql += "     , b.avetc2  \n";
            sql += "     , b.avact  \n";
            sql += "     , b.avreport  \n";
            sql += "     , b.score  \n";
            //sql += "     , b.isgraduated  \n";
            sql += "     , d.isgraduated  \n";
            sql += "     , e.subjnm  \n";
            
            sql += "from   tz_subjseq a  \n";
            sql += "     , tz_student b  \n";
            sql += "     , tz_member c  \n";
            sql += "     , tz_stold d  \n";
            sql += "     , tz_subj e  \n";
            sql += "where  a.subj = b.subj  \n";
            sql += "and    a.year = b.year  \n";
            sql += "and    a.subjseq = b.subjseq  \n";
            sql += "and    b.userid = c.userid  \n";
            sql += "and    b.subj = d.subj  \n";
            sql += "and    b.year = d.year  \n";
            sql += "and    b.subjseq = d.subjseq  \n";
            sql += "and    b.userid = d.userid  \n";
            sql += "and    a.subj = e.subj  \n";
            sql += "and    a.isgoyong = 'Y'  \n";
          //  sql += "and    d.isgraduated = 'N'  \n";

            if (!"ALL".equals(ss_grcode)) {
            	sql +="\n and    a.grcode = " + StringManager.makeSQL(ss_grcode);
            }
            if (!"ALL".equals(ss_gyear)) {
            	sql +="\n and    a.gyear = " + StringManager.makeSQL(ss_gyear);
            }
            if (!"ALL".equals(ss_grseq)) {
            	sql +="\n and    a.grseq = " + StringManager.makeSQL(ss_grseq);
            }
            if (!"ALL".equals(ss_uclass)) {
            	sql +="\n and    e.upperclass = " + StringManager.makeSQL(ss_uclass);
            }
            if (!"ALL".equals(ss_mclass)) {
            	sql +="\n and    e.middleclass = " + StringManager.makeSQL(ss_mclass);
            }
            if (!"ALL".equals(ss_lclass)) {
            	sql +="\n and    e.lowerclass = " + StringManager.makeSQL(ss_lclass);
            }
            if (!"ALL".equals(ss_subjcourse)) {
            	sql +="\n and    a.subj = " + StringManager.makeSQL(ss_subjcourse);
            }
            
            sql += "and   (substr(a.edustart, 1, 8) between " + SQLString.Format(v_startdt) + " and " + SQLString.Format(v_enddt) + "  \n";
            sql += "    or substr(a.eduend, 1, 8) between " + SQLString.Format(v_startdt) + " and " + SQLString.Format(v_enddt) + ")  \n";
            sql += "order by a.subj, e.subjnm, a.subjseq  \n";

            ls = connMgr.executeQuery(sql);

			while (ls.next()) {
                dbox = ls.getDataBox();

                list.add(dbox);
            }
       }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

    /**
     * 고용보험 기간별 인원현황(미수료자) - 노동부신고엑셀출력
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectPriodNotGraduatedListByExcel(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
        
        String ss_grcode   = box.getStringDefault  ( "s_grcode"        , "ALL" ); // 교육그룹
        String ss_gyear    = box.getStringDefault  ( "s_gyear"         , "ALL" );  // 년도
        String ss_grseq    = box.getStringDefault  ( "s_grseq"         , "ALL" );  // 교육기수
        String ss_uclass   = box.getStringDefault  ( "s_upperclass"    , "ALL" );  // 과목분류
        String ss_mclass   = box.getStringDefault  ( "s_middleclass"   , "ALL" );  // 과목분류
        String ss_lclass   = box.getStringDefault  ( "s_lowerclass"    , "ALL" );  // 과목분류
        String ss_subjcourse=box.getStringDefault  ( "s_subjcourse"    , "ALL" );  // 과목&코스

		String v_startdt = box.getString("p_startdt");
		String v_enddt = box.getString("p_enddt");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql = "\n select a.subj "
            	+ "\n      , b.subjnm "
            	+ "\n      , a.subjseq "
            	+ "\n      , substr(a.edustart, 5,2) as edustart "
            	+ "\n      , substr(a.eduend, 5,2) as eduend "
            	+ "\n      , a.userid "
            	+ "\n      , a.name "
            	+ "\n      , fn_crypt('2', d.birth_date, 'knise') birth_date "
            	+ "\n from   tz_stold a "
            	+ "\n      , tz_subj b "
            	+ "\n      , tz_subjseq c "
            	+ "\n      , tz_member d "
            	+ "\n where  a.subj = b.subj "
            	+ "\n and    a.subj = c.subj "
            	+ "\n and    a.year = c.year "
            	+ "\n and    a.subjseq = c.subjseq "
            	+ "\n and    a.userid = d.userid "
            	+ "\n and    a.isgraduated = 'N' "
            	+ "\n and    c.isgoyong = 'Y' ";

            if (!"ALL".equals(ss_grcode)) {
            	sql +="\n and    c.grcode = " + StringManager.makeSQL(ss_grcode);
            }
            if (!"ALL".equals(ss_gyear)) {
            	sql +="\n and    c.gyear = " + StringManager.makeSQL(ss_gyear);
            }
            if (!"ALL".equals(ss_grseq)) {
            	sql +="\n and    c.grseq = " + StringManager.makeSQL(ss_grseq);
            }
            if (!"ALL".equals(ss_uclass)) {
            	sql +="\n and    b.upperclass = " + StringManager.makeSQL(ss_uclass);
            }
            if (!"ALL".equals(ss_mclass)) {
            	sql +="\n and    b.middleclass = " + StringManager.makeSQL(ss_mclass);
            }
            if (!"ALL".equals(ss_lclass)) {
            	sql +="\n and    b.lowerclass = " + StringManager.makeSQL(ss_lclass);
            }
            if (!"ALL".equals(ss_subjcourse)) {
            	sql +="\n and    a.subj = " + StringManager.makeSQL(ss_subjcourse);
            }
            
            sql +="\n and   (substr(a.edustart, 1, 8) between " + SQLString.Format(v_startdt) + " and " + SQLString.Format(v_enddt)
            	+ "\n     or substr(a.eduend, 1, 8) between " + SQLString.Format(v_startdt) + " and " + SQLString.Format(v_enddt) + ") "
            	+ "\n order by a.subj, b.subjnm, a.subjseq, a.name ";

            ls = connMgr.executeQuery(sql);

			while (ls.next()) {
                dbox = ls.getDataBox();

                list.add(dbox);
            }
       }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

    /**
     * 고용보험 기간별 인원현황(취소자)
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectPriodCancelList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
        
        String ss_grcode   = box.getStringDefault  ( "s_grcode"        , "ALL" ); // 교육그룹
        String ss_gyear    = box.getStringDefault  ( "s_gyear"         , "ALL" );  // 년도
        String ss_grseq    = box.getStringDefault  ( "s_grseq"         , "ALL" );  // 교육기수
        String ss_uclass   = box.getStringDefault  ( "s_upperclass"    , "ALL" );  // 과목분류
        String ss_mclass   = box.getStringDefault  ( "s_middleclass"   , "ALL" );  // 과목분류
        String ss_lclass   = box.getStringDefault  ( "s_lowerclass"    , "ALL" );  // 과목분류
        String ss_subjcourse=box.getStringDefault  ( "s_subjcourse"    , "ALL" );  // 과목&코스

		String v_startdt = box.getString("p_startdt");
		String v_enddt = box.getString("p_enddt");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql  = "select a.subj  \n";
            sql += "     , a.year  \n";
            sql += "     , a.subjseq  \n";
            sql += "     , get_compnm(c.comp) as compnm  \n";
            sql += "     , to_char(to_date(substr(a.edustart, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') as edustartdt  \n";
            sql += "     , to_char(to_date(substr(a.eduend, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') as eduenddt  \n";
            sql += "     , fn_crypt('2', c.birth_date, 'knise') birth_date  \n";
            sql += "     , b.userid  \n";
            sql += "     , c.name  \n";
            sql += "     , get_deptnm(c.dept_cd) as deptnm  \n";
            sql += "     , post_nm as jikwinm  \n";
            sql += "     , b.seq  \n";
            sql += "     , to_char(to_date(b.canceldate, 'YYYYMMDDHH24MISS'), 'YYYY.MM.DD') as canceldt  \n";
            sql += "     , b.cancelkind  \n";
            sql += "     , b.reason  \n";
            sql += "     , d.subjnm  \n";
            sql += "from   tz_subjseq a  \n";
            sql += "     , tz_cancel b  \n";
            sql += "     , tz_member c  \n";
            sql += "     , tz_subj d  \n";
            sql += "where  a.subj = b.subj  \n";
            sql += "and    a.year = b.year  \n";
            sql += "and    a.subjseq = b.subjseq  \n";
            sql += "and    b.userid = c.userid  \n";
            sql += "and    b.seq = (select max(seq)  \n";
            sql += "                from   tz_cancel  \n";
            sql += "                where  subj = a.subj  \n";
            sql += "                and    year = a.year  \n";
            sql += "                and    subjseq = a.subjseq  \n";
            sql += "                and    userid = b.userid)  \n";
            sql += "and    a.subj = d.subj  \n";
            sql += "and    a.isgoyong = 'Y'  \n";

            if (!"ALL".equals(ss_grcode)) {
            	sql +="\n and    a.grcode = " + StringManager.makeSQL(ss_grcode);
            }
            if (!"ALL".equals(ss_gyear)) {
            	sql +="\n and    a.gyear = " + StringManager.makeSQL(ss_gyear);
            }
            if (!"ALL".equals(ss_grseq)) {
            	sql +="\n and    a.grseq = " + StringManager.makeSQL(ss_grseq);
            }
            if (!"ALL".equals(ss_uclass)) {
            	sql +="\n and    d.upperclass = " + StringManager.makeSQL(ss_uclass);
            }
            if (!"ALL".equals(ss_mclass)) {
            	sql +="\n and    d.middleclass = " + StringManager.makeSQL(ss_mclass);
            }
            if (!"ALL".equals(ss_lclass)) {
            	sql +="\n and    d.lowerclass = " + StringManager.makeSQL(ss_lclass);
            }
            if (!"ALL".equals(ss_subjcourse)) {
            	sql +="\n and    a.subj = " + StringManager.makeSQL(ss_subjcourse);
            }
            
            sql +="\n and   (substr(a.edustart, 1, 8) between " + SQLString.Format(v_startdt) + " and " + SQLString.Format(v_enddt)
            	+ "\n     or substr(a.eduend, 1, 8) between " + SQLString.Format(v_startdt) + " and " + SQLString.Format(v_enddt) + ") "
            	+ "\n order by a.subj, d.subjnm, a.subjseq ";

            ls = connMgr.executeQuery(sql);

			while (ls.next()) {
                dbox = ls.getDataBox();

                list.add(dbox);
            }
       }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

    /**
     * 고용보험 수료 완결 점검 리스트
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectNotClosedList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
        
		String v_startdt = box.getString("p_startdt");
		String v_enddt = box.getString("p_enddt");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql  = "select a.grcode  \n";
            sql += "     , d.grcodenm  \n";
            sql += "     , a.grseq  \n";
            sql += "     , a.grseqnm  \n";
            sql += "     , c.subj  \n";
            sql += "     , c.subjnm  \n";
            //sql += "     , dbo.to_charForDate(dbo.to_dateForDate(substring(b.edustart, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') as edustartdt  \n";
            //sql += "     , dbo.to_charForDate(dbo.to_dateForDate(substring(b.eduend, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') as eduenddt  \n";
            sql += "     , to_char(to_date(substr(b.edustart, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') as edustartdt  \n";
            sql += "     , to_char(to_date(substr(b.eduend, 1, 8), 'YYYYMMDD'), 'YYYY.MM.DD') as eduenddt  \n";
            sql += "     , c.muserid  \n";
            sql += "     , e.name  \n";
            sql += "from   tz_grseq a  \n";
            sql += "     , tz_subjseq b  \n";
            sql += "     , tz_subj c  \n";
            sql += "     , tz_grcode d  \n";
            sql += "     , tz_member e  \n";
            sql += "where  a.grcode = b.grcode  \n";
            sql += "and    a.gyear = b.gyear  \n";
            sql += "and    a.grseq = b.grseq  \n";
            sql += "and    b.subj = c.subj  \n";
            sql += "and    a.grcode = d.grcode  \n";
            sql += "and    c.muserid = e.userid  \n";
            //sql += "and    substring(b.eduend, 1, 8) between " + SQLString.Format(v_startdt) + " and " + SQLString.Format(v_enddt) + "  \n";
            sql += "and    substr(b.eduend, 1, 8) between " + SQLString.Format(v_startdt) + " and " + SQLString.Format(v_enddt) + "  \n";
            sql += "order by a.grcode, a.grseq, b.subj  \n";

            ls = connMgr.executeQuery(sql);

			while (ls.next()) {
                dbox = ls.getDataBox();

                list.add(dbox);
            }
       }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
    
    /**
     * 회사/과정별 인원현황
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectCompSubjStuCntList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
        
        String v_gubun = box.getString("p_gubun");
		String v_startdt = box.getString("p_startdt");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            if(v_gubun.equals("1")) {
	            sql  = "select c.comp  \n";
	            //sql += "     , d.compnm  \n";
	            sql += "     , get_compnm(c.comp) compnm  \n";
	            sql += "     , a.subj  \n";
	            sql += "     , b.subjnm  \n";
	            sql += "     , sum(c.cnt) as cnt  \n";
	            sql += "     , a.year, a.subjseq  \n";
	            sql += "     , a.grcode  \n";
	            sql += "from   tz_subjseq a  \n";
	            sql += "     , tz_subj b  \n";
	            sql += "     ,(select t1.subj  \n";
	            sql += "            , t1.year  \n";
	            sql += "            , t1.subjseq  \n";
	            sql += "            , t2.comp  \n";
	            sql += "            , count(t1.userid) as cnt  \n";
	            sql += "       from   tz_student t1  \n";
	            sql += "            , tz_member t2  \n";
	            sql += "       where  t1.userid = t2.userid  \n";
	            sql += "       group by  \n";
	            sql += "               t1.subj  \n";
	            sql += "            , t1.year  \n";
	            sql += "            , t1.subjseq  \n";
	            sql += "            , t2.comp) c  \n";
	            //sql += "     , tz_comp d  \n";
	            sql += "where  a.subj = b.subj  \n";
	            sql += "and    a.subj = c.subj  \n";
	            sql += "and    a.year = c.year  \n";
	            sql += "and    a.subjseq = c.subjseq  \n";
	            //sql += "and    c.comp = d.comp  \n";
	            sql += "and    a.isgoyong = 'Y'  \n";
	            //sql += "and    substring(a.edustart, 1, 8) = " + SQLString.Format(v_startdt) + "  \n";
	            sql += "and    substr(a.edustart, 1, 8) = " + SQLString.Format(v_startdt) + "  \n";
	            sql += "group by   \n";
	            sql += "       c.comp  \n";
	            //sql += "     , d.compnm  \n";
	            //sql += "     , compnm  \n";
	            sql += "     , a.subj  \n";
	            sql += "     , b.subjnm  \n";
	            sql += "     , a.year  \n";
                sql += "     , a.subjseq, a.grcode  \n";
	            sql += "order by c.comp, a.subj  \n";
            } else {
                sql  = "select a.subj  \n";
                sql += "     , b.subjnm  \n";
                sql += "     , sum(c.cnt) as cnt  \n";
                sql += "     , a.year, a.subjseq  \n";
                sql += "     , a.grcode  \n";
                sql += "from   tz_subjseq a  \n";
                sql += "     , tz_subj b  \n";
                sql += "     ,(select subj  \n";
                sql += "            , year  \n";
                sql += "            , subjseq  \n";
                sql += "            , count(userid) as cnt  \n";
                sql += "       from   tz_student  \n";
                sql += "       group by  \n";
                sql += "              subj  \n";
                sql += "            , year  \n";
                sql += "            , subjseq) c  \n";
                sql += "where  a.subj = b.subj  \n";
                sql += "and    a.subj = c.subj  \n";
                sql += "and    a.year = c.year  \n";
                sql += "and    a.subjseq = c.subjseq  \n";
                sql += "and    a.isgoyong = 'Y'  \n";
                //sql += "and    substring(a.edustart, 1, 8) = " + SQLString.Format(v_startdt) + "  \n";
                sql += "and    substr(a.edustart, 1, 8) = " + SQLString.Format(v_startdt) + "  \n";
                sql += "group by  \n";
                sql += "       a.subj  \n";
                sql += "     , b.subjnm  \n";
                sql += "     , a.year  \n";
                sql += "     , a.subjseq, a.grcode  \n";
                sql += "order by a.subj  \n";
            }

            ls = connMgr.executeQuery(sql);

			while (ls.next()) {
                dbox = ls.getDataBox();

                list.add(dbox);
            }
       }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
    
    /**
     * 위탁훈련 계약서 회사정
     * @param box          receive from the form object and session
     * @return DataBox
     */
    public DataBox selectCompanyInfo(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        String sql                  = "";
        
		String v_comp = box.getString("p_comp");

        try {
            connMgr = new DBConnectionManager();

            sql  = "SELECT COMP  \n";
            sql += "     , COMPNM  \n";
            sql += "     , ADDR  \n";
            sql += "     , CONAME  \n";
            sql += "     , COMPbirth_date  \n";
            //sql += "     , DATEPART(YYYY, GETDATE()) AS N_YEAR  \n";
            //sql += "     , DATEPART(MM, GETDATE()) AS N_MONTH  \n";
            //sql += "     , DATEPART(DD, GETDATE()) AS N_DAY  \n";
            sql += "     , to_char(sysdate,'YYYY') AS N_YEAR  \n";
            sql += "     , to_char(sysdate,'MM') AS N_MONTH  \n";
            sql += "     , to_char(sysdate,'DD') AS N_DAY  \n";
            sql += "FROM   TZ_COMPCLASS  \n";
            sql += "WHERE  COMP = " + SQLString.Format(v_comp) + "  \n";

            ls = connMgr.executeQuery(sql);

			if(ls.next())
				dbox = ls.getDataBox();
       }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return dbox;
    }
    
    /**
     * 위탁훈련 계약서 인원현황
     * @param box          receive from the form object and session
     * @return ArrayList
     */
    public ArrayList selectContractStuCntList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
        
		String v_grcode = box.getString("s_grcode");
		String v_gyear = box.getString("s_gyear");
		String v_grseq = box.getString("s_grseq");
		String v_comp = box.getString("p_comp");

        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            sql  = "SELECT A.GRCODE  \n";
            sql += "     , A.GYEAR  \n";
            sql += "     , A.GRSEQ  \n";
            sql += "     , A.GRSEQNM  \n";
            sql += "     , B.SUBJ  \n";
            sql += "     , B.SUBJNM  \n";
            sql += "     , B.STUDENTLIMIT  \n";
            sql += "     , B.BIYONG  \n";
            sql += "     , COUNT(C.USERID) AS STUCNT  \n";
            sql += "FROM   TZ_GRSEQ A  \n";
            sql += "     , TZ_SUBJSEQ B  \n";
            sql += "     , TZ_STUDENT C  \n";
            sql += "WHERE A.GRCODE = B.GRCODE  \n";
            sql += "AND   A.GYEAR = B.GYEAR  \n";
            sql += "AND   A.GRSEQ = B.GRSEQ  \n";
            sql += "AND   B.SUBJ = C.SUBJ  \n";
            sql += "AND   B.YEAR = C.YEAR  \n";
            sql += "AND   B.SUBJSEQ = C.SUBJSEQ  \n";
            sql += "AND   B.ISGOYONG = 'Y'  \n";
            sql += "AND   A.GRCODE = " + SQLString.Format(v_grcode) + "  \n";
            sql += "AND   A.GYEAR = " + SQLString.Format(v_gyear) + "  \n";
            sql += "AND   A.GRSEQ = " + SQLString.Format(v_grseq) + "  \n";
            sql += "AND   C.COMP = " + SQLString.Format(v_comp) + "  \n";
            sql += "GROUP BY  \n";
            sql += "       A.GRCODE  \n";
            sql += "     , A.GYEAR  \n";
            sql += "     , A.GRSEQ  \n";
            sql += "     , A.GRSEQNM  \n";
            sql += "     , B.SUBJ  \n";
            sql += "     , B.SUBJNM  \n";
            sql += "     , B.STUDENTLIMIT  \n";
            sql += "     , B.BIYONG  \n";

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
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectSuryoPrintList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        ArrayList list              = null;
        String sql                  = "";
        
        String  p_subj   = box.getString("p_subj");    //교육그룹
        String  p_year    = box.getString("p_year");     //년도
        String  p_subjseq    = box.getString("p_subjseq");     //교육차수

     
        String  v_startdate		= box.getString("p_startdate");		//학습시작일
     
        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select a.serno,C.grseq,C.courseseq,C.coursenm,C.subj,C.year,C.subjnm, \n";
            sql += " C.subjseq, c.subjseqgr, c.edustart, c.eduend, \n";
			//sql += " dbo.get_deptnm(B.deptnm,'') compnm, dbo.get_jikwinm(B.jikwi, B.comp) jikwinm,";
			//sql += " dbo.get_compnm(B.comp,1,2) company,";
            //sql += " B.userid,B.cono,b.birth_date,B.name,c.place,c.edustart,c.eduend,c.biyong,c.goyongpricemajor,c.goyongpriceminor";
            sql += " get_deptnm(B.dept_cd) compnm, lvl_nm jikwinm, \n";
            sql += " get_compnm(B.comp) company,";
            sql += " B.userid,fn_crypt('2', b.birth_date, 'knise') birth_date,B.name,c.place,c.edustart,c.eduend,c.biyong,c.goyongpricemajor,c.goyongpriceminor, \n";
            sql += "trunc((sum(nvl(to_number(substr(replace(trim(d.total_time),':',' ') ,1,2)),0))*60*60  \n" +
            		"+ sum(nvl(to_number(substr(replace(trim(d.total_time),':',' ') ,4,2)),0))*60" +
            		"+ sum(nvl(to_number(substr(replace(trim(d.total_time),':',' ') ,7,2)),0)) ) / (60*60) ,0) total_time, \n";
            sql+="trunc( mod( (   sum(nvl(to_number(substr(replace(trim(d.total_time),':',' ') ,1,2)),0)*60*60)  \n" +
            		"+ sum(nvl(to_number(substr(replace(trim(d.total_time),':',' ') ,4,2)),0)*60) \n" +
            		"+ sum(nvl(to_number(substr(replace(trim(d.total_time),':',' ') ,7,2)),0)))/60, 60 ), 0 ) total_minute,  \n";
            sql+="mod ( sum(   nvl(to_number(substr(replace(trim(d.total_time),':',' ') ,1,2)),0)*60*60 \n" +
            		"+ nvl(to_number(substr(replace(trim(d.total_time),':',' ') ,4,2)),0)*60 \n" +
            		"+ nvl(to_number(substr(replace(trim(d.total_time),':',' ') ,7,2)),0)    ) , 60)   total_sec \n";
			sql += " from tz_stold a,TZ_MEMBER b,vz_scsubjseq c ,TZ_PROGRESS d \n";
			sql += " where c.isclosed='Y' and c.isgoyong = 'Y' and a.userid = d.userid and a.userid = b.userid and A.subj=C.subj and A.year=C.year and  C.subjseq= d.subjseq( +) and A.subjseq=C.subjseq and A.isgraduated='Y' \n";
			sql += " and a.subj='"+p_subj+"'and a.year='"+p_year+"' and a.subjseq='"+p_subjseq+"' \n" ;
			sql+="group by a.serno, C.grseq, C.courseseq, C.coursenm, C.subj, C.year, C.subjnm, C.subjseq, \n" +
					" c.subjseqgr, c.edustart, c.eduend, get_deptnm(B.dept_cd), lvl_nm, get_compnm(B.comp),  \n" +
					"B.userid, fn_crypt('2', b.birth_date, 'knise') , B.name, c.place, c.edustart, c.eduend, c.biyong, c.goyongpricemajor, c.goyongpriceminor \n";
			sql += " order by a.serno " ;
			System.out.println("selectSuryoPrintList sql start\r\n\n"+sql+"\r\n\n selectSuryoPrintList sql end");
         
			


            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
    public DataBox selectSuryoPrintTitle1(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        String sql                  = "";
        
        String  p_grcode   = box.getString("p_grcode");    //교육그룹
        String  p_subj   = box.getString("p_subj");    //교육그룹
        String  p_year    = box.getString("p_year");     //년도
        String  p_subjseq    = box.getString("p_subjseq");     //교육차수
        
        try {
            connMgr = new DBConnectionManager();
            
            sql  = " select get_compnm(B.comp) company \n";
            sql += " from tz_stold a,TZ_MEMBER b,vz_scsubjseq c  \n";
			sql += " where c.isclosed='Y' and c.isgoyong = 'Y' and a.userid = b.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq and A.isgraduated='Y' \n";
			sql += " and a.subj='"+p_subj+"'and a.year='"+p_year+"' and a.subjseq='"+p_subjseq+"' \n" ;
			sql+="group by a.serno, C.grseq, C.courseseq, C.coursenm, C.subj, C.year, C.subjnm, C.subjseq, \n" +
					" c.subjseqgr, c.edustart, c.eduend, get_deptnm(B.dept_cd), lvl_nm, get_compnm(B.comp),  \n" +
					"B.userid, fn_crypt('2', b.birth_date, 'knise'), B.name, c.place, c.edustart, c.eduend, c.biyong, c.goyongpricemajor, c.goyongpriceminor \n";
			sql += " order by a.serno " ;

            System.out.println("selectSuryoPrintTitle1============>"+sql);

            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                dbox = ls.getDataBox();
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return dbox;
    }
    /**
     * 
     * @param box
     * @return
     * @throws Exception
     */
    public DataBox selectSuryoPrintTitle(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        String sql                  = "";
        
        String  p_grcode   = box.getString("p_grcode");    //교육그룹
        String  p_subj   = box.getString("p_subj");    //교육그룹
        String  p_year    = box.getString("p_year");     //년도
        String  p_subjseq    = box.getString("p_subjseq");     //교육차수
        
        try {
            connMgr = new DBConnectionManager();
         sql+=" select                               \n";
            sql+="   c.scsubjnm,                        \n";
            sql+="   d.compnm,                       \n";
            //sql+="   d.companynm,                       \n";
            sql+="   c.place, 							\n";
            sql+="   c.edustart,                        \n";
            sql+="   c.eduend,                          \n";
            sql+="   c.subjseqgr                        \n";
            sql+=" from                                 \n";
            sql+="   vz_scsubjseq c,                    \n";
            sql+="   tz_compclass d,                         \n";
            sql+="   tz_grcomp e                        \n";
            sql+=" where                                \n";
            sql+="   e.comp = d.comp and c.grcode = e.grcode                   \n";
            sql+="   and C.grcode    = "+SQLString.Format(p_grcode) +"\n";
            sql+="   and c.year      = "+SQLString.Format(p_year);
            sql+="   and C.scsubj    = "+SQLString.Format(p_subj);
            sql+="   and C.scsubjseq = "+SQLString.Format(p_subjseq);


            System.out.println("selectSuryoPrintTitle============>"+sql);

            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                dbox = ls.getDataBox();
            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return dbox;
    }
    public ArrayList selectSuryoSilPrint(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DataBox dbox                = null;
        ListSet ls                  = null;
        String sql                  = "";
        ArrayList list              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        String  p_grcode   = box.getString("p_grcode");    //교육그룹
        String  p_subj   = box.getString("p_subj");    //교육그룹
        String  p_year    = box.getString("p_year");     //년도
        String  p_subjseq    = box.getString("p_subjseq");     //교육차수
        
        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();


            sql  = "  select a.serno,C.grseq,C.courseseq,C.coursenm,C.subj,C.year,C.subjnm,   ";//수료증번호(serno)
            sql += "  C.subjseq, c.subjseqgr, c.edustart, c.eduend,  ";
			//sql += " dbo.get_deptnm(B.deptnm,'') compnm, dbo.get_jikwinm(B.jikwi, B.comp) jikwinm,";
			//sql += " dbo.get_compnm(B.comp,1,2) company , a.isgraduated ";
           // sql += " get_deptnm(B.dept_cd) compnm, -- get_jikwinm(B.jikwi, B.comp) jikwinm, \n";
            sql += " get_compnm(B.comp) company , a.isgraduated  ";//수료여부(isgraduated) company(사용자의 회사이름)
	         sql +="    ,   a.avtstep, a.tstep                             ";//진도율
	         
	         sql += " , (select max(ldate) from tz_examresult m  where examtype='M'  and m.subj='"+p_subj+"' and m.year='"+p_year+"' and m.subjseq='"+p_subjseq+"' and m.userid=a.userid) m_date " ;
	         
	         sql += " , (select max(ldate) from tz_examresult m  where examtype='E'  and m.subj='"+p_subj+"' and m.year='"+p_year+"' and m.subjseq='"+p_subjseq+"' and m.userid=a.userid) e_date " ;
	         
	         sql += " , (select max(indate) from  tz_projrep m  where   m.subj='"+p_subj+"' and m.year='"+p_year+"' and m.subjseq='"+p_subjseq+"' and m.projid =a.userid) r_date " ;
	 
	         sql +="       ,   A.avreport         ";//리포트
	         sql +="      ,   A.avact                  ";
	         sql +="      ,   A.avetc1                  ";
	         sql +="      ,   A.avmtest             ";//중간평가
	         sql +="      ,   A.avftest             ";//평가
	         sql +="     ,   A.avetc1               ";//참여율
	         sql +="     ,   A.score                     ";//총점
	         sql +="     ,   A.avhtest                       ";
	         
	         sql +="     ,   B.hometel               ";
	         sql +="      ,   B.handphone                   ";
	         sql +="      ,   B.lvl_nm               ";//직급
	         sql +="      ,   B.position_nm                 ";//직위
	         sql +="      ,   B.name                 ";//이름
	         sql +="      ,   B.userid                  ";//아이디
	         sql +="      ,   fn_crypt('2', b.birth_date, 'knise') birth_date ,               ";//주민등록번호
//	    	 sql +="     ,   A.report              ";
//	         sql +="     ,   A.act                     ";
//	         sql +="     ,   A.mtest                 ";
//	         sql +="     ,   A.ftest                 ";
//	         sql +="     ,   A.etc1                   ";
//	         sql +="     ,   A.htest                 ";
			//sql+="c.scsubjnm";//과정명
	         sql += " c.place,c.biyong,c.goyongpricemajor,c.goyongpriceminor";
	        // sql += " c.place,c.edustart,c.eduend,,c.biyong,c.goyongpricemajor,c.goyongpriceminor";
            //sql += " , a.score, B.userid,B.cono,b.birth_date,B.name,c.place,c.edustart,c.eduend,c.biyong,c.goyongpricemajor,c.goyongpriceminor";
			sql += " from tz_stold a,TZ_MEMBER b,vz_scsubjseq c";
			//sql += " where c.isclosed='Y' and a.isgoyong = 'Y' and a.userid = b.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq and A.isgraduated='Y'";
			//sql += " where c.isclosed='Y' and c.isgoyong = 'Y' and a.userid = b.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq and A.isgraduated='Y'";
			sql += " where c.isclosed='Y' and c.isgoyong = 'Y' and a.userid = b.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq ";  //미수료자 노출되게 변경
			sql += " and a.subj='"+p_subj+"' and a.year='"+p_year+"' and a.subjseq='"+p_subjseq+"'" ;
			sql += " order by a.serno " ;


            //System.out.println("sql1111111============>"+sql);

			  ls = connMgr.executeQuery(sql);

	            while (ls.next()) {
	                dbox = ls.getDataBox();
	                list.add(dbox);
	            }
        }
        catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
    
    /**
     * 훈련실시신고자명단, 수료자명단 엑셀 리스트
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectExcelList(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	DataBox dbox                = null;
    	ListSet ls                  = null;
    	ArrayList list              = null;
    	StringBuffer strSQL         = null;
    	String sql                  = "";
    	
		String  v_startdate  = box.getString("p_startdate");
		String  v_enddate    = box.getString("p_enddate");
		
    	try {
    		connMgr = new DBConnectionManager();
    		list = new ArrayList();
    		
    		strSQL = new StringBuffer();
    		//strSQL.append(" select C.grseq,C.course,C.cyear,C.courseseq,C.coursenm,C.subj,C.year,C.subjnm,C.subjseq,C.grcode, get_deptnm(b.dept_cd) compnm, ") ;
    		strSQL.append(" select C.subjnm, B.userid, fn_crypt('2', b.birth_date, 'knise')birth_date,B.name, get_compnm(B.comp) compnm, b.position_nm, ") ;
    		strSQL.append("        b.lvl_nm, fn_crypt('2', b.birth_date, 'knise') birth_date, c.edustart, c.eduend, b.userid, decode(d.ISGRADUATED, 'Y', '수료', '미수료') suryo ") ;
    		//strSQL.append("      ,   d.avtstep,   d.avmtest  ,   d.avftest ,   d.avreport ,d.score ") ;//진도율 중간평가 최종평가 리포트 총점(수료자임으로 값을 tz_stold )
    		strSQL.append("      ,   d.tstep as avtstep,   d.avmtest  ,   d.avftest ,   d.avreport ,d.score ") ;//진도율 중간평가 최종평가 리포트 총점(수료자임으로 값을 tz_stold )
    		strSQL.append(" from tz_student a, TZ_MEMBER b, vz_scsubjseq c, tz_stold d ") ;
    		strSQL.append(" where a.userid = b.userid ") ;
    		strSQL.append("   and A.subj=C.subj ") ;
    		strSQL.append("   and A.year=C.year ") ;
    		strSQL.append("   and A.subjseq=C.subjseq ") ;
    		
    		strSQL.append("   and A.userid=d.userid(+) ") ;
    		strSQL.append("   and A.subj=d.subj(+) ") ;
    		strSQL.append("   and A.year=d.year(+) ") ;
    		strSQL.append("   and A.subjseq=d.subjseq(+) ") ;
    		
    		strSQL.append("   and isgoyong='Y' ") ;
    		strSQL.append("   and '"+v_startdate+"00' <= c.edustart and c.eduend <= '"+v_enddate+"23' ") ;
    		
    		ls = connMgr.executeQuery(strSQL.toString());
    		
            while (ls.next()) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }

    	}
    	catch (Exception ex) {
    		ErrorManager.getErrorStackTrace(ex, box, sql);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
    	}
    	finally {
    		if(ls != null) { try { ls.close(); }catch (Exception e) {} }
    		if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
    	}
    	return list;
    }
}
