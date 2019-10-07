//**********************************************************
//1. 제      목: 근태및가점 BEAN
//2. 프로그램명: AdditionBean.java
//3. 개      요: 근태및가점 BEAN
//4. 환      경: JDK 1.4
//5. 버      젼: 0.1
//6. 작      성:
//7. 수      정:
//
//**********************************************************

package com.ziaan.complete;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.ziaan.common.SubjComBean;
import com.ziaan.course.SubjseqData;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AdditionBean {
    public static final int FINISH_COMPLETE = 0;   // 수료처리 종료
    public static final int FINISH_CANCEL   = 1;   // 수료취소 가능
    public static final int FINISH_PROCESS  = 3;   // 수료처리
    public static final int SCORE_COMPUTE   = 4;   // 점수재계산

    public static final String ONOFF_GUBUN  = "0004";
    public static final String SUBJ_NOT_INCLUDE_COURSE = "000000";

    public AdditionBean() {}

	/**
    근태및가점
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList SelectAdditionList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        ArrayList list = new ArrayList();
        CourseFinishData  data = null;
        FinishData     subdata = null;
        DataBox dbox = null;

		String v_grcode    		= box.getStringDefault("s_grcode","N000001");    //교육그룹
        String v_gyear			= box.getString("s_gyear");
        String v_grseq			= box.getString("s_subjseq");

        String  ss_upperclass   = box.getStringDefault("s_upperclass","ALL");    //과정분류
        String  ss_middleclass  = box.getStringDefault("s_middleclass","ALL");   //과정분류
        String  ss_lowerclass   = box.getStringDefault("s_lowerclass","ALL");    //과정분류
        String  ss_subjcourse   = box.getStringDefault("s_subjcourse","ALL");    //과정&코스

        String  v_orderColumn   = box.getString("p_orderColumn");               //정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 //정렬할 순서
        String  v_subjseq       = box.getString("s_subjseq");


        try {
				sql ="  select subj,scyear								  ";
				sql+="	  ,scsubjseq                                                              ";
				sql+="    ,(select classname from tz_subjatt where upperclass=a.scupperclass      ";
				sql+="	    and middleclass=a.scmiddleclass and lowerclass='000') middleclassnm   ";
				sql+="	  ,scsubjnm                                                               ";
				sql+=" from VZ_SCSUBJSEQ a                                                        ";

				sql+= " where a.grcode =" +SQLString.Format(v_grcode);
				sql+= "   and a.year  =" +SQLString.Format(v_gyear);
				sql+= "   and a.isonoff  = 'OFF'";

				if (!v_subjseq.equals("ALL")) {
					sql+= "   and a.subjseq = " +SQLString.Format(v_subjseq);
				}

				if (!ss_subjcourse.equals("ALL")) {
					sql+= "   and a.subj = " + SQLString.Format(ss_subjcourse);
				} else {
					if (!ss_upperclass.equals("ALL")) {
						if (!ss_upperclass.equals("ALL"))  sql += " and a.scupperclass = "+SQLString.Format(ss_upperclass);
						if (!ss_middleclass.equals("ALL")) sql += " and a.scmiddleclass = "+SQLString.Format(ss_middleclass);
						if (!ss_lowerclass.equals("ALL"))  sql += " and a.sclowerclass = "+SQLString.Format(ss_lowerclass);
					}
					sql+= "   and a.isuse = 'Y'";
				}

				if(v_orderColumn.equals("")) {
					sql+= " order by a.subjnm ";
				} else {
					sql+= " order by " + v_orderColumn + v_orderType;
				}

                System.out.println("sql===>>>"+sql);

                connMgr = new DBConnectionManager();
                ls = connMgr.executeQuery(sql);

                while (ls.next()) {
                    dbox = ls.getDataBox();
                    dbox.put("d_subj"			,ls.getString("subj"));
					dbox.put("d_scyear"			,ls.getString("scyear"));
                    dbox.put("d_scsubjseq"		,ls.getString("scsubjseq"));
                    dbox.put("d_middleclassnm"	,ls.getString("middleclassnm"));
                    dbox.put("d_scsubjnm"		,ls.getString("scsubjnm"));
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
	근태및가점
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList SelectAdditionMemberList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        ArrayList list = new ArrayList();
        CourseFinishData  data = null;
        FinishData     subdata = null;
        DataBox dbox = null;

        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq     = box.getString("p_subjseq");

		String  v_orderColumn   = box.getString("p_orderColumn");           	//정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");           		//정렬할 순서
        
        int isOk = 0;
        
        String v_eduterm = "";
        //점수재계산
        FinishBean fbean = new FinishBean();
        SubjComBean scbean = new SubjComBean();

        try {

				sql =" select a.subj																";
				sql+=" 	     ,a.year                                                              ";
				sql+=" 	     ,a.subjseq                                                           ";
				sql+=" 	     ,get_compnm(a.comp,2,4) as compnm                                    ";
				sql+=" 	     ,get_name(a.userid) as name                                          ";
				sql+=" 	     ,a.tstep                                                             ";
				sql+=" 	     ,a.avtstep                                                           ";
				sql+=" 	     ,decode(a.stdgubun,0,'일반',1,'분임장',2,'학생장') as stdgubunnm      ";
				sql+=" 	     ,a.userid                                                             ";
				sql+=" 	     ,b.subjnm                                                             ";
				sql+=" from ";
				sql+="   tz_student a, tz_subjseq b ";
				sql+= " where ";
				sql+= "   a.subj = b.subj ";
				sql+= "   and a.year = b.year  ";
				sql+= "   and a.subjseq = b.subjseq  ";
				sql+= "   and a.subj = " +SQLString.Format(v_subj);
				sql+= "   and a.year = " +SQLString.Format(v_year);
				sql+= "   and a.subjseq = " +SQLString.Format(v_subjseq);


                //sql+= " order by a.grcode, a.gyear, a.grseq, a.course, a.cyear, a.courseseq, a.subj, a.year, a.subjseq ";
                System.out.println("sql===>>>"+sql);

                connMgr = new DBConnectionManager();
                
                v_eduterm = scbean.getEduTermClosedGubun(connMgr, v_subj, v_subjseq , v_year);
                System.out.println("v_eduterm========>>>>"+v_eduterm);
                //교육중이후 수료처리전까지 계산한다.
			    if(v_eduterm.equals("4") || v_eduterm.equals("5")){
			      if(!v_year.equals("ALL") && !v_subj.equals("ALL") && !v_subjseq.equals("ALL")){
                    isOk = fbean.OffSubjectCompleteRerating(box);
                  }
                }
                
                ls = connMgr.executeQuery(sql);

                while (ls.next()) {
                    dbox = ls.getDataBox();
                    dbox.put("d_subj"			,ls.getString("subj"));
					dbox.put("d_year"			,ls.getString("year"));
                    dbox.put("d_subjseq"		,ls.getString("subjseq"));
                    dbox.put("d_compnm"			,ls.getString("compnm"));
                    dbox.put("d_name"			,ls.getString("name"));
                    dbox.put("d_tstep"			,new Double(ls.getDouble("tstep")));
                    dbox.put("d_avtstep"			,new Double(ls.getDouble("avtstep")));
                    dbox.put("d_stdgubunnm"		,ls.getString("stdgubunnm"));
                    dbox.put("d_userid"			,ls.getString("userid"));
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
	근태및가점
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList SelectAdditionInsertPage(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        ArrayList list = new ArrayList();
        CourseFinishData  data = null;
        FinishData     subdata = null;
        DataBox dbox = null;

        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq  = box.getString("p_subjseq");
        String v_userid   = box.getString("p_userid");

        try {

				sql =" select subj																";
				sql+=" ,year                                                                    ";
				sql+=" ,subjseq,serial_no                                                      ";
				sql+=" , (select violation_name from TZ_VIOLATION_INFO                          ";
				sql+="    where top_code = substr(a.demerit_code,1,2) and middle_code = '000')  ";
				sql+="    ||' '||(select violation_name from TZ_VIOLATION_INFO                  ";
				sql+="    where top_code = substr(a.demerit_code,1,2)                           ";
				sql+="    and middle_code = substr(a.demerit_code,3,3)) as violationnm          ";
				sql+=" ,to_char(to_date(login_date,'YYYYMMDD'),'YYYY/MM/DD') as logdate         ";
				sql+=" ,demerit_code                                                            ";
				sql+=" ,demerit_score                                                           ";
				sql+="  from TZ_STUDENT_VIOLATION a                                             ";

				sql+= " where subj = " +SQLString.Format(v_subj);
				sql+= "   and year = " +SQLString.Format(v_year);
				sql+= "   and subjseq = " +SQLString.Format(v_subjseq);
				sql+= "   and userid = " +SQLString.Format(v_userid);

                //sql+= " order by a.grcode, a.gyear, a.grseq, a.course, a.cyear, a.courseseq, a.subj, a.year, a.subjseq ";
                System.out.println("sql===>>>"+sql);

                connMgr = new DBConnectionManager();
                ls = connMgr.executeQuery(sql);

                while (ls.next()) {
                    dbox = ls.getDataBox();
                    dbox.put("d_subj"			,ls.getString("subj"));
					dbox.put("d_year"			,ls.getString("year"));
                    dbox.put("d_subjseq"		,ls.getString("subjseq"));
                    dbox.put("d_searial_no"		,ls.getString("serial_no"));
					dbox.put("d_violationnm"	,ls.getString("violationnm"));
                    dbox.put("d_logdate"		,ls.getString("logdate"));
                    dbox.put("d_demerit_code"	,ls.getString("demerit_code"));
					dbox.put("d_dscore"			,new Double(ls.getDouble("demerit_score")));
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
	근태및가점
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList AdditionSelectBox(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        ArrayList list = new ArrayList();
        CourseFinishData  data = null;
        FinishData     subdata = null;
        DataBox dbox = null;

        try {

				sql =" select												";
				sql+=" top_code || middle_code as code                                                                  ";
				sql+=" ,(select violation_name from TZ_VIOLATION_INFO                                                   ";
				sql+="   where top_code = a.top_code and middle_code = '000') ||' '|| violation_name  as codenm		    ";
				sql+=" from TZ_VIOLATION_INFO a where middle_code <> '000'                                              ";

                //sql+= " order by a.grcode, a.gyear, a.grseq, a.course, a.cyear, a.courseseq, a.subj, a.year, a.subjseq ";
                System.out.println("sql===>>>"+sql);

                connMgr = new DBConnectionManager();
                ls = connMgr.executeQuery(sql);

                while (ls.next()) {
                    dbox = ls.getDataBox();
                    dbox.put("d_code"			,ls.getString("code"));
					dbox.put("d_codenm"			,ls.getString("codenm"));
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
    근태및가점등록
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int insertAddition(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ListSet ls2 = null;
        ListSet ls3 = null;
        ListSet ls4 = null;                
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
        String sql = "";
        String sql1 = "";
        String sql2 = "";
		int isOk = 0;
		int isOk1 = 0;
		int count_ok = 0;
		int v_serialno = 0;
        String v_code = "";

        String v_subj			= box.getString("p_subj");
        String v_year			= box.getString("p_year");
        String v_subjseq		= box.getString("p_subjseq");
        String v_demerit_code	= box.getString("p_demerit_code");
		String v_logdate		= box.getString("p_logdate");
		String v_userid			= box.getString("p_userid");

		double v_demerit		= 0;

        String v_value1  = box.getString("p_value");        //String with userids
        String v_value2  = "";
        String v_luserid = box.getSession("userid");

        double v_tstep   = 0;
        double v_mtest   = 0;
        double v_ftest   = 0;
        double v_test1   = 0;
        double v_test2   = 0;
        double v_test3   = 0;
        double v_test4   = 0;
        double v_report  = 0;
        double v_etc1    = 0;
        double v_etc2    = 0;

        String v_currdate = FormatDate.getDate("yyyyMMddHH");  // 현재시각 년+월+일+시
        
        ConfigSet conf = new ConfigSet();
        
        int cnt = 0;
        int cnt2 = 0;


		try {
           connMgr = new DBConnectionManager();

			FinishBean finishbean = new FinishBean();
			StoldData  data = new StoldData();

			sql = "select nvl(max(SERIAL_NO), 0) from TZ_STUDENT_VIOLATION where subj='"+v_subj+"' and year='"+v_year+"' and subjseq = '"+v_subjseq+"'";
			ls = connMgr.executeQuery(sql);
			if(ls.next()){ v_serialno = ls.getInt(1) + 1;    }

			sql2 = "select demerit from TZ_VIOLATION_INFO where top_code = substr('"+v_demerit_code+"',1,2) and middle_code = substr('"+v_demerit_code+"',3,3)";
			ls2 = connMgr.executeQuery(sql2);
			if(ls2.next()){ v_demerit = ls2.getDouble(1);    }

			sql1 =  "insert into TZ_STUDENT_VIOLATION(subj,year,subjseq,userid,login_date,serial_no,demerit_code,demerit_score) ";
			sql1 += " values (?, ?, ?, ?, ?, ?, ?, ?)           ";
			pstmt = connMgr.prepareStatement(sql1);
			pstmt.setString(1, v_subj);
			pstmt.setString(2, v_year);
			pstmt.setString(3, v_subjseq);
			pstmt.setString(4, v_userid);
			pstmt.setString(5, v_logdate);
			pstmt.setInt(6, v_serialno);
			pstmt.setString(7, v_demerit_code);
			pstmt.setDouble(8, v_demerit);
			isOk = pstmt.executeUpdate();
			if ( pstmt != null ) { pstmt.close(); }
			
			
			sql1 = get_subjseq_SqlString(v_subj, v_year, v_subjseq, v_logdate);

			ls3 = connMgr.executeQuery(sql1);
			
			if(ls3.next()){
			    cnt = ls3.getInt("cnt");
			}
			
			//입력한 일자가 과정학습기간일때에만 처리함
			if(cnt > 0){
			  sql1 = get_attendance_SqlString(v_subj, v_year, v_subjseq, v_userid, "Y", v_logdate);
			  //System.out.println("존재여부================="+v_demerit_code+"=======>>>>>"+sql1);
			  ls4 = connMgr.executeQuery(sql1);
			  if(ls4.next()){
			      cnt2 = ls4.getInt("cnt");
			  }
			  //결석을 입력하였을때
			  if(v_demerit_code.equals(conf.getProperty("demerit_code.absent.value"))){ 
			    if(cnt2>0){
                  sql1 ="delete from tz_attendance where";
                  sql1+=" subj = ?";
                  sql1+=" and year = ?";
                  sql1+=" and subjseq = ?";
                  sql1+=" and userid  = ?";
                  sql1+=" and attdate = ?";
                  pstmt2 = connMgr.prepareStatement(sql1);
			      pstmt2.setString(1, v_subj);
			      pstmt2.setString(2, v_year);
			      pstmt2.setString(3, v_subjseq);
			      pstmt2.setString(4, v_userid);
			      pstmt2.setString(5, v_logdate);
			      isOk1 = pstmt2.executeUpdate();
			      if ( pstmt2 != null ) { pstmt2.close(); }
			    }
			  }
			  //지각코드를 입력했을때
			  else if(v_demerit_code.equals(conf.getProperty("demerit_code.late2.value")) || v_demerit_code.equals(conf.getProperty("demerit_code.late1.value"))){
			    String temp_isattend ="";
			    if(v_demerit_code.equals(conf.getProperty("demerit_code.late1.value")) ){ // 10분초과 1시간이내
			        temp_isattend = "L";
			    }else if(v_demerit_code.equals(conf.getProperty("demerit_code.late2.value"))){ // 1시간초과
			        temp_isattend = "M";
			    }
			    //tz_attendance 기존테이블에 존재할시
			    if(cnt2>0){
			      sql1 ="update tz_attendance set isattend ='"+temp_isattend+"', atttime='0000', ldate=to_char(sysdate, 'yyyyMMddhhmmss') ";
                  sql1+="where";
                  sql1+=" subj = ?";
                  sql1+=" and year = ?";
                  sql1+=" and subjseq = ?";
                  sql1+=" and userid  = ?";
                  sql1+=" and attdate = ?";
                  pstmt3 = connMgr.prepareStatement(sql1);
			      pstmt3.setString(1, v_subj);
			      pstmt3.setString(2, v_year);
			      pstmt3.setString(3, v_subjseq);
			      pstmt3.setString(4, v_userid);
			      pstmt3.setString(5, v_logdate);
			      isOk1 = pstmt3.executeUpdate();
			      if ( pstmt3 != null ) { pstmt3.close(); }
			    }
			    //tz_attendance 기존테이블에 존재하지 않으면
			    else{
			      sql1 ="insert into tz_attendance(subj, year, subjseq, userid, attdate, atttime, isattend, ldate) values";
                  sql1+="(?, ?, ?, ?, ?, ?, '"+temp_isattend+"', to_char(sysdate, 'yyyyMMddhhmmss'))";
                  pstmt3 = connMgr.prepareStatement(sql1);
			      pstmt3.setString(1, v_subj);
			      pstmt3.setString(2, v_year);
			      pstmt3.setString(3, v_subjseq);
			      pstmt3.setString(4, v_userid);
			      pstmt3.setString(5, v_logdate);
			      pstmt3.setString(6, "0000");
			      isOk1 = pstmt3.executeUpdate();
			      if ( pstmt3 != null ) { pstmt3.close(); }
			    
			    }
              }
			  
		    }

			isOk = updatestudent(connMgr,v_subj,v_year,v_subjseq,v_demerit_code,v_logdate,v_userid,v_value1,v_luserid,data);


        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); } catch (Exception e) {} }
            if(ls2 != null) { try { ls2.close(); } catch (Exception e) {} }
            if(ls3 != null) { try { ls3.close(); } catch (Exception e) {} }
            if(ls4 != null) { try { ls4.close(); } catch (Exception e) {} }
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }
			if(pstmt2 != null) { try { pstmt2.close(); } catch (Exception e1) {} }
			if(pstmt3 != null) { try { pstmt3.close(); } catch (Exception e1) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }

    /**
    근태코드 수정시
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
     public int updateAddition(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
		PreparedStatement pstmt = null;
        String sql = "";
        String sql1 = "";
		int isOk = 0;

        String v_subj			= box.getString("p_subj");
        String v_year			= box.getString("p_year");
        String v_subjseq		= box.getString("p_subjseq");
        String v_demerit_code	= box.getString("p_demerit_code");
		String v_logdate		= box.getString("p_logdate");
		String v_userid			= box.getString("p_userid");
		String v_searial_no		= box.getString("p_searial_no");
		double v_demerit		= 0;

        String v_value1  = box.getString("p_value");        //String with userids
        String v_value2  = "";
        String v_luserid = box.getSession("userid");
        String v_currdate = FormatDate.getDate("yyyyMMddHH");  // 현재시각 년+월+일+시

        try {
            connMgr = new DBConnectionManager();

			StoldData  data = new StoldData();

			sql1 = "select demerit from TZ_VIOLATION_INFO where top_code = substr('"+v_demerit_code+"',1,2) and middle_code = substr('"+v_demerit_code+"',3,3)";
			ls1 = connMgr.executeQuery(sql1);
			if(ls1.next()){ v_demerit = ls1.getDouble(1);    }

			sql  = " update TZ_STUDENT_VIOLATION set demerit_code = ?, login_date= ?, demerit_score= ? ";
            sql += " where subj=? and year=? and subjseq=? and userid=? and serial_no=? ";

            pstmt = connMgr.prepareStatement(sql);

			pstmt.setString(1, v_demerit_code);
			pstmt.setString(2, v_logdate);
			pstmt.setDouble(3, v_demerit);
			pstmt.setString(4, v_subj);
			pstmt.setString(5, v_year);
			pstmt.setString(6, v_subjseq);
			pstmt.setString(7, v_userid);
			pstmt.setString(8, v_searial_no);

            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
			isOk = updatestudent(connMgr,v_subj,v_year,v_subjseq,v_demerit_code,v_logdate,v_userid,v_value1,v_luserid,data);

		}
        catch(Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls1 != null) { try { ls1.close(); } catch (Exception e) {} }
			if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }

    /**
    근태 코드삭제시
    @param box      receive from the form object and session
    @return isOk    1:delete success,0:delete fail
    */
    public int deleteAddition(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        ListSet ls = null;
        String sql1 = "";
        int isOk1 = 0;

        String v_subj			= box.getString("p_subj");
        String v_year			= box.getString("p_year");
        String v_subjseq		= box.getString("p_subjseq");
        String v_demerit_code	= box.getString("p_demerit_code");
		String v_logdate		= box.getString("p_logdate");
		String v_userid			= box.getString("p_userid");
		String v_searial_no		= box.getString("p_searial_no");

        String v_value1  = box.getString("p_value");        //String with userids
        String v_value2  = "";
        String v_luserid = box.getSession("userid");
        String v_currdate = FormatDate.getDate("yyyyMMddHH");  // 현재시각 년+월+일+시
        ConfigSet conf = new ConfigSet();
        int cnt = 0;

		try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

			StoldData  data = new StoldData();

            sql1  = " delete from TZ_STUDENT_VIOLATION           ";
            sql1 += "  where subj=? and year=? and subjseq=? and userid=? and serial_no=?  ";

            pstmt1 = connMgr.prepareStatement(sql1);
			pstmt1.setString(1, v_subj);
			pstmt1.setString(2, v_year);
			pstmt1.setString(3, v_subjseq);
			pstmt1.setString(4, v_userid);
			pstmt1.setString(5, v_searial_no);
			isOk1 = pstmt1.executeUpdate();
			if ( pstmt1 != null ) { pstmt1.close(); }
            
			sql1 = get_attendance_SqlString(v_subj, v_year, v_subjseq, v_userid, "Y", v_logdate);
			
			System.out.println("존재여부================="+v_demerit_code+"=======>>>>>"+sql1);
			
			ls = connMgr.executeQuery(sql1);
			
			if(ls.next()){
			    cnt = ls.getInt("cnt");
			}

			if(v_demerit_code.equals(conf.getProperty("demerit_code.absent.value"))){ //결석코드를 지웠을때
				System.out.println("1111111111111111111111============================================================");
			  if(cnt==0){
                sql1 ="insert into tz_attendance(subj, year, subjseq, userid, attdate, atttime, isattend, ldate) values";
                sql1+="(?, ?, ?, ?, ?, ?, 'Y', to_char(sysdate, 'yyyyMMddhhmmss'))";
                pstmt2 = connMgr.prepareStatement(sql1);
			    pstmt2.setString(1, v_subj);
			    pstmt2.setString(2, v_year);
			    pstmt2.setString(3, v_subjseq);
			    pstmt2.setString(4, v_userid);
			    pstmt2.setString(5, v_logdate);
			    pstmt2.setString(6, "0000");
			    isOk1 = pstmt2.executeUpdate();
			    if ( pstmt2 != null ) { pstmt2.close(); }
			  }
			}
			//지각코드를 지웠을때
			else if(v_demerit_code.equals(conf.getProperty("demerit_code.late2.value")) || v_demerit_code.equals(conf.getProperty("demerit_code.late1.value"))){
			  if(cnt>0){
			    sql1 ="update tz_attendance set isattend ='Y', ldate=to_char(sysdate, 'yyyyMMddhhmmss') ";
                sql1+="where";
                sql1+=" subj = ?";
                sql1+=" and year = ?";
                sql1+=" and subjseq = ?";
                sql1+=" and userid  = ?";
                sql1+=" and attdate = ?";
                pstmt3 = connMgr.prepareStatement(sql1);
			    pstmt3.setString(1, v_subj);
			    pstmt3.setString(2, v_year);
			    pstmt3.setString(3, v_subjseq);
			    pstmt3.setString(4, v_userid);
			    pstmt3.setString(5, v_logdate);
			    isOk1 = pstmt3.executeUpdate();
			    if ( pstmt3 != null ) { pstmt3.close(); }
			  }
            }

			isOk1 = updatestudent(connMgr,v_subj,v_year,v_subjseq,v_demerit_code,v_logdate,v_userid,v_value1,v_luserid,data);

			if(isOk1 > 0 ) connMgr.commit();         //      하위 분류의 로우가 없을경우 isOk2 가 0 이므로 isOk2 >0 조건 제외
            else connMgr.rollback();

        }
        catch (Exception ex) {
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt1 != null) { try { pstmt1.close(); } catch (Exception e) {} }
            if(pstmt2 != null) { try { pstmt2.close(); } catch (Exception e) {} }
            if(pstmt3 != null) { try { pstmt3.close(); } catch (Exception e) {} }
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) try { connMgr.setAutoCommit(true); } catch (Exception e) {}
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk1;
    }

	/**
    근태및가점
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList SelectAdditionSubInsertPage(RequestBox box) throws Exception {

		DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        ArrayList list = new ArrayList();
        CourseFinishData  data = null;
        FinishData     subdata = null;
        DataBox dbox = null;

        String v_subj			= box.getString("p_subj");
        String v_year			= box.getString("p_year");
        String v_subjseq		= box.getString("p_subjseq");
		String v_userid			= box.getString("p_userid");

		String  v_orderColumn   = box.getString("p_orderColumn");           	//정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");           		//정렬할 순서

        try {


				sql =" select																								  ";
				sql+=" 	     subj                                                                                             ";
				sql+=" 	     ,year                                                                                            ";
				sql+=" 	     ,subjseq                                                                                         ";
				sql+=" 	     ,userid                                                                                          ";
				sql+=" 		 ,(select subjnm from tz_subjseq where subj=a.subj and year=a.year and subjseq=a.subjseq) subjnm  ";
				sql+=" 	     ,get_compnm(comp,2,4) as compnm                                                                  ";
				sql+=" 	     ,get_name(userid) as name                                                                        ";
				sql+=" 		 ,nvl((select sum(demerit_score) from TZ_STUDENT_VIOLATION where substr(demerit_code,1,2)= '01'   ";
				sql+=" 		   and subj=a.subj and year=a.year and subjseq=a.subjseq and userid=a.userid),0) as demsum1       ";
				sql+=" 		 ,nvl((select sum(demerit_score) from TZ_STUDENT_VIOLATION where substr(demerit_code,1,2)= '02'   ";
				sql+=" 		   and subj=a.subj and year=a.year and subjseq=a.subjseq and userid=a.userid),0) as demsum2       ";
				sql+=" 		 ,nvl((select sum(demerit_score) from TZ_STUDENT_VIOLATION where substr(demerit_code,1,2)= '03'   ";
				sql+=" 		   and subj=a.subj and year=a.year and subjseq=a.subjseq and userid=a.userid),0) as demsum3       ";
				sql+=" 		 ,nvl((select sum(demerit_score) from TZ_STUDENT_VIOLATION where substr(demerit_code,1,2)= '04'   ";
				sql+=" 		   and subj=a.subj and year=a.year and subjseq=a.subjseq and userid=a.userid),0) as demsum4       ";
				sql+=" 		 ,nvl((select sum(demerit_score) from TZ_STUDENT_VIOLATION where substr(demerit_code,1,2)= '05'   ";
				sql+=" 		   and subj=a.subj and year=a.year and subjseq=a.subjseq and userid=a.userid),0) as demsum5       ";
				sql+=" 		 ,nvl((select sum(demerit_score) from TZ_STUDENT_VIOLATION where substr(demerit_code,1,2)= '06'   ";
				sql+=" 		   and subj=a.subj and year=a.year and subjseq=a.subjseq and userid=a.userid),0) as demsum6       ";
				sql+=" 	     ,stdgubun                                                                                        ";
				sql+=" from tz_student a 																				  	  ";

				sql+= " where subj = " +SQLString.Format(v_subj);
				sql+= "   and year = " +SQLString.Format(v_year);
				sql+= "   and subjseq = " +SQLString.Format(v_subjseq);
				sql+= "   and userid = " +SQLString.Format(v_userid);

                //sql+= " order by a.grcode, a.gyear, a.grseq, a.course, a.cyear, a.courseseq, a.subj, a.year, a.subjseq ";
                System.out.println("sql===>>>"+sql);

                connMgr = new DBConnectionManager();
                ls = connMgr.executeQuery(sql);

                while (ls.next()) {
                    dbox = ls.getDataBox();
                    dbox.put("d_subj"			,ls.getString("subj"));
					dbox.put("d_year"			,ls.getString("year"));
                    dbox.put("d_subjseq"		,ls.getString("subjseq"));
                    dbox.put("d_userid"			,ls.getString("userid"));
					dbox.put("d_subjnm"			,ls.getString("subjnm"));
					dbox.put("d_compnm"			,ls.getString("compnm"));
                    dbox.put("d_name"			,ls.getString("name"));
					dbox.put("d_demsum1"		,new Double(ls.getDouble("demsum1")));
					dbox.put("d_demsum2"		,new Double(ls.getDouble("demsum2")));
					dbox.put("d_demsum3"		,new Double(ls.getDouble("demsum3")));
					dbox.put("d_demsum4"		,new Double(ls.getDouble("demsum4")));
					dbox.put("d_demsum5"		,new Double(ls.getDouble("demsum5")));
					dbox.put("d_demsum6"		,new Double(ls.getDouble("demsum6")));
					dbox.put("d_stdgubun"		,ls.getString("stdgubun"));
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
    근태및가점 교육직책수정
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
     public int updateSubAddition(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
		PreparedStatement pstmt = null;
        String sql = "";
        String sql1 = "";
		int isOk = 0;

        String v_subj			= box.getString("p_subj");
        String v_year			= box.getString("p_year");
        String v_subjseq		= box.getString("p_subjseq");
		String v_userid			= box.getString("p_userid");
		String v_class_position	= box.getString("p_class_position");

		String v_logdate		= box.getString("p_logdate");
		String v_demerit_code	= box.getString("p_demerit_code");
        String v_value1  = box.getString("p_value");        //String with userids
        String v_value2  = "";
        String v_luserid = box.getSession("userid");
        String v_currdate = FormatDate.getDate("yyyyMMddHH");  // 현재시각 년+월+일+시

		int v_etc2 = 0;

		if(v_class_position.equals("1")) v_etc2 = 1;

        try {
            connMgr = new DBConnectionManager();

			StoldData  data = new StoldData();

			sql  = " update TZ_STUDENT set stdgubun = ?, etc2 = ?, avetc2 = ? ";
            sql += " where subj=? and year=? and subjseq=? and userid=? ";

            pstmt = connMgr.prepareStatement(sql);

			pstmt.setString(1, v_class_position);
			pstmt.setInt(2, v_etc2);
			pstmt.setInt(3, v_etc2);
			pstmt.setString(4, v_subj);
			pstmt.setString(5, v_year);
			pstmt.setString(6, v_subjseq);
			pstmt.setString(7, v_userid);

            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }

			isOk = updatestudent(connMgr,v_subj,v_year,v_subjseq,v_demerit_code,v_logdate,v_userid,v_value1,v_luserid,data);

		}
        catch(Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }



    /**
    근태및가점등록
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int updatestudent(DBConnectionManager connMgr, String v_subj, String v_year, String v_subjseq, String v_demerit_code, String v_logdate, String v_userid, String v_value1, String v_luserid, StoldData data) throws Exception {

		ListSet ls = null;
        ListSet ls2 = null;
		PreparedStatement pstmt = null;
        String sql = "";
        String sql1 = "";
        String sql2 = "";
		int isOk = 0;
		int count_ok = 0;
		int v_serialno = 0;

		String v_currdate = FormatDate.getDate("yyyyMMddHH");  // 현재시각 년+월+일+시

		double v_demerit		= 0;
        PreparedStatement pstmt_update_student = null;
        ResultSet rs = null;
        ArrayList list = null;

        double v_tstep   = 0;
        double v_etc1    = 0;
        double v_etc2    = 0;

		FinishBean finishbean = new FinishBean();
        SubjseqData subjseqdata = null;

        boolean v_isexception = false;
        String   v_return_msg  = "";

		try {
           //connMgr = new DBConnectionManager();

            // 수료처리 완료여부, 학습중 검토
            //subjseqdata = getSubjseqInfo(connMgr, v_subj, v_year, v_subjseq);

            data.setSubj     (v_subj);
            data.setYear     (v_year);
            data.setSubjseq  (v_subjseq);
            data.setUserid   (v_userid);
            //data.setWstep    (subjseqdata.getWstep());
            //data.setWetc1    (subjseqdata.getWetc1());
            //data.setWetc2    (subjseqdata.getWetc2());
            
            sql  = "select isclosed from tz_subjseq where subj = '"+v_subj+"' and year = '"+v_year+"' and subjseq = '"+v_year+"'";
            
            ls = connMgr.executeQuery(sql);
            String v_isclosed = "";
            if(ls.next()){
              v_isclosed = ls.getString("isclosed");
            }

            //if (subjseqdata.getIsclosed().equals("Y")) {
            if (v_isclosed.equals("Y")) {
                v_return_msg  = "이미 수료처리 되었습니다.";
                isOk = 2;
                return isOk;
            }

            //edited by 2006.03.06
            //집합과정시 점수재계산
            System.out.println("123123412341234234++++++++++++++++++++++++++++");
            isOk = finishbean.calc_offscore(connMgr, v_subj, v_year, v_subjseq, v_userid, v_luserid, data);
        }
        catch (Exception ex) {
//			ErrorManager.getErrorStackTrace(ex, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); } catch (Exception e) {} }
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e1) {} }
            //if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }


/*
    public SubjseqData getSubjseqInfo(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq) throws Exception {
        ListSet ls = null;
        String sql = "";
        SubjseqData data = new SubjseqData();

        // 과정차수 정보
        sql = " select b.isgoyong,   b.isclosed,  b.edustart, b.eduend,  ";
        sql+= "        b.wstep,      b.wmtest,    b.wftest,   b.wtest1,	b.wtest2, b.wtest3, b.wtest4, b.whtest,	b.wreport, ";
        sql+= "        b.wact,       b.wetc1,     b.wetc2, ";
        sql+= "        b.gradscore,  b.gradstep,  b.gradexam, b.gradftest, b.gradtest1, b.gradtest2, b.gradtest3, b.gradtest4, b.gradhtest, b.gradreport,";
        sql+= "        b.grcode,     b.grseq,     b.gyear,    b.subjnm, ";
        sql+= "        a.isonoff,    b.biyong  ";
        sql+= "   from tz_subj     a, ";
        sql+= "        tz_subjseq  b";
        sql+= "  where a.subj    = b.subj ";
        sql+= "    and b.subj    = " + SQLString.Format(p_subj);
        sql+= "    and b.year    = " + SQLString.Format(p_year);
        sql+= "    and b.subjseq = " + SQLString.Format(p_subjseq);
//System.out.println("sql ==>" + sql);
        try {
            ls = connMgr.executeQuery(sql);
            while (ls.next()) {
                data.setIsgoyong(ls.getString("isgoyong"));
                data.setIsclosed(ls.getString("isclosed"));
                data.setEdustart(ls.getString("edustart"));
                data.setEduend  (ls.getString("eduend"));

                data.setWstep   ((int)ls.getDouble("wstep"));
                data.setWmtest  ((int)ls.getDouble("wmtest"));
                data.setWftest  ((int)ls.getDouble("wftest"));
                data.setWtest1  ((int)ls.getDouble("wtest1"));
                data.setWtest2  ((int)ls.getDouble("wtest2"));
                data.setWtest3  ((int)ls.getDouble("wtest3"));
                data.setWtest4  ((int)ls.getDouble("wtest4"));
                data.setWhtest  ((int)ls.getDouble("whtest"));
                data.setWreport ((int)ls.getDouble("wreport"));
                data.setWact    ((int)ls.getDouble("wact"));
                data.setWetc1   ((int)ls.getDouble("wetc1"));
                data.setWetc2   ((int)ls.getDouble("wetc2"));

                data.setGradscore(ls.getInt("gradscore"));
                data.setGradstep (ls.getInt("gradstep"));
                data.setGradexam (ls.getInt("gradexam"));
                data.setGradftest (ls.getInt("gradftest"));
                data.setGradtest1 (ls.getInt("gradtest1"));
                data.setGradtest2 (ls.getInt("gradtest2"));
                data.setGradtest3 (ls.getInt("gradtest3"));
                data.setGradtest4 (ls.getInt("gradtest4"));
                data.setGradhtest (ls.getInt("gradhtest"));
                data.setGradreport (ls.getInt("gradreport"));


                data.setGrcode(ls.getString("grcode"));
                data.setGyear(ls.getString("gyear"));
                data.setGrseq(ls.getString("grseq"));
                data.setSubjnm(ls.getString("subjnm"));
                data.setGrcodenm(GetCodenm.get_grcodenm(data.getGrcode()));
                data.setGrseqnm(GetCodenm.get_grseqnm(data.getGrcode(), data.getGyear(), data.getGrseq()));

                data.setIsonoff(ls.getString("isonoff"));
                data.setBiyong(ls.getInt("biyong"));
            }
        }
        catch (Exception ex) {
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); } catch (Exception e) {} }
        }
        return data;
    }
*/

	/**
    sql문 작성
    @param p_gubun
    @param p_command
    @return PreparedStatement
    */
    public PreparedStatement getPreparedStatement(DBConnectionManager connMgr, String p_gubun, String p_command) throws Exception {
        PreparedStatement pstmt = null;
        String sql = "";
		System.out.println("----------- p_gubun = " + p_gubun);
		System.out.println("----------- p_command = " + p_command);

		if (p_gubun.equals("SUBJECT_STOLD")) {
            if (p_command.equals("select")) {
                sql = " select count(*) cnt ";
                sql+= "   from tz_stold ";
                sql+= "  where subj      = ? ";
                sql+= "    and year      = ? ";
                sql+= "    and subjseq   = ? ";
                sql+= "    and userid    = ? ";
            }
        } else if (p_gubun.equals("SUBJECT_STUDENT")) {
            if (p_command.equals("update")) {
                sql = " update tz_student ";
                sql+= "    set score   = ?, ";
                sql+= "        tstep   = ?, ";
                sql+= "        etc1    = ?, ";
                sql+= "        etc2    = ?, ";
                sql+= "        avtstep = ?, ";
                sql+= "        avetc1  = ?, ";
                sql+= "        avetc2  = ?, ";
                sql+= "        isgraduated= ?, ";
                sql+= "        luserid = ?, ";
                sql+= "        ldate   = ? ";
                sql+= "  where subj    = ? ";
                sql+= "    and year    = ? ";
                sql+= "    and subjseq = ? ";
                sql+= "    and userid  = ? "; //31
            }
        } else if (p_gubun.equals("MEMBER")) {
            if (p_command.equals("select")) {
                sql = " select name, comp, jikwi ";
                sql+= "   from tz_member ";
                sql+= "  where userid = ? ";
            }
        }


	//	System.out.println("sql==>"+ sql);

        try {
            pstmt = connMgr.prepareStatement(sql);
        }
        catch (Exception ex) {
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
        }
        return pstmt;
    }
    
    public String get_attendance_SqlString(String p_subj, String p_year, String p_subjseq, String p_userid, String p_isattend, String p_attdate) {
      String sql1 = "";
      sql1 = "select ";
      sql1+= "  count(*) cnt";
      sql1+= "  from tz_attendance ";
      sql1+= "where ";
      sql1+= "  subj = '"+p_subj+"' and year = '"+p_year+"' and subjseq = '"+p_subjseq+"' and userid = '"+p_userid+"' ";
      sql1+= "  and attdate    = '"+p_attdate+"'";
      //sql1+= "  and isattend = '"+p_isattend+"'";
      return sql1;
    }
    
    public String get_subjseq_SqlString(String p_subj, String p_year, String p_subjseq, String p_attdate) {
      String sql1 = "";
      sql1+=" select                                                               \n";
      sql1+="   count(*) cnt                                                       \n";
      sql1+=" from                                                                 \n";
      sql1+="   tz_subjseq                                                         \n";
      sql1+=" where                                                                \n";
      sql1+="   subj = '"+p_subj+"'                                                     \n";
      sql1+="   and year = '"+p_year+"'                                                 \n";
      sql1+="   and subjseq = '"+p_subjseq+"'                                           \n";
      sql1+="   and '"+p_attdate+"' between substr(edustart,1,8) and substr(eduend,1,8) \n";
      
      return sql1;
    }


}
