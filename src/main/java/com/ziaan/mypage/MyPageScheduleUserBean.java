/**
 * 개인 스케쥴 관리
 * @author 조용준
 * @version 1.0
 */

package com.ziaan.mypage;

import java.io.File;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.*;
import jxl.*;

import com.ziaan.library.*;
import com.ziaan.common.UpdateSpareTable;

public class MyPageScheduleUserBean {
    private ConfigSet config;
    private int row;
	private String dirUploadDefault;
    
    public MyPageScheduleUserBean() {
        try{
            config = new ConfigSet();  
            row = Integer.parseInt(config.getProperty("page.bulletin.row"));        //        이 모듈의 페이지당 row 수를 셋팅한다
			dirUploadDefault = config.getProperty("dir.upload.default");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

   /**
   * DB에서 나뉘어진 lnag 명을 가져올때 사용한다.
   */
  	public String makeLangSql(String v_prefix,RequestBox box) {

		String v_ret = "";

        try{
			String v_language = box.getSession("languageName");

			if(v_language.equals("ENGLISH")){
				v_ret = v_prefix + "_eng name";
			} else if(v_language.equals("JAPANESE")){
				v_ret = v_prefix + "_jpn name";
			} else if(v_language.equals("CHINESE")){
				v_ret = v_prefix + "_chn name";
			} else {
				v_ret = v_prefix + " name";	
			}
			
        }
        catch(Exception e) {
            e.printStackTrace();
        }      

		return v_ret;
	}

    /** 
    링크에 필요한 월일자 정보를 가져온다.
    @param RequestBox box
    @return DataBox
    */
    public DataBox selectMonthDate(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        DataBox dbox = null;
        String sql = "";

		Calendar gCal = Calendar.getInstance();
	
		String v_dyear	= Integer.toString(gCal.get(Calendar.YEAR));
		String v_dmonth = (gCal.get(Calendar.MONTH)<9)?"0"+Integer.toString(gCal.get(Calendar.MONTH)+1):Integer.toString(gCal.get(Calendar.MONTH)+1);
		String v_dday	= (gCal.get(Calendar.DATE)<10)?"0"+Integer.toString(gCal.get(Calendar.DATE)):Integer.toString(gCal.get(Calendar.DATE));

        try {

			String v_year	= box.getString("p_year").equals("")?v_dyear:box.getString("p_year");
			String v_month	= box.getString("p_month").equals("")?v_dmonth:box.getString("p_month");
			String v_day	= box.getString("p_day").equals("")?v_dday:box.getString("p_day");
		    
            db = new DatabaseExecute();
            
            sql = "		SELECT TO_CHAR(ADD_MONTHS(TO_DATE('"+v_year+v_month+v_day+"','yyyymmdd'),1),'yyyy') AFTER_YEAR, ";
			sql += "		TO_CHAR(ADD_MONTHS(TO_DATE('"+v_year+v_month+v_day+"','yyyymmdd'),1),'mm') AFTER_MONTH, ";
			sql += "		TO_CHAR(ADD_MONTHS(TO_DATE('"+v_year+v_month+v_day+"','yyyymmdd'),1),'dd') AFTER_DAY, ";
			sql += "		TO_CHAR(ADD_MONTHS(TO_DATE('"+v_year+v_month+v_day+"','yyyymmdd'),-1),'yyyy') BEFORE_YEAR, ";
			sql += "		TO_CHAR(ADD_MONTHS(TO_DATE('"+v_year+v_month+v_day+"','yyyymmdd'),-1),'mm') BEFORE_MONTH, ";
			sql += "		TO_CHAR(ADD_MONTHS(TO_DATE('"+v_year+v_month+v_day+"','yyyymmdd'),-1),'dd') BEFORE_DAY, ";
			sql += "		TO_CHAR(TO_DATE('"+v_year+v_month+v_day+"','yyyymmdd'),'yyyy') YEAR, ";
			sql += "		TO_CHAR(TO_DATE('"+v_year+v_month+v_day+"','yyyymmdd'),'mm') MONTH, ";
			sql += "		TO_CHAR(TO_DATE('"+v_year+v_month+v_day+"','yyyymmdd'),'dd') DAY ";
            sql += "	FROM DUAL				        		";
            
            dbox = db.executeQuery(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return dbox;        
    } 

    /** 
    링크에 필요한 주일자 정보를 가져온다.
    @param RequestBox box
    @return DataBox
    */
    public DataBox selectWeekDate(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        DataBox dbox = null;
        String sql = "";

		Calendar gCal = Calendar.getInstance();
	
		String v_dyear	= Integer.toString(gCal.get(Calendar.YEAR));
		String v_dmonth = (gCal.get(Calendar.MONTH)<9)?"0"+Integer.toString(gCal.get(Calendar.MONTH)+1):Integer.toString(gCal.get(Calendar.MONTH)+1);
		String v_dday	= (gCal.get(Calendar.DATE)<10)?"0"+Integer.toString(gCal.get(Calendar.DATE)):Integer.toString(gCal.get(Calendar.DATE));

        try {

			String v_year	= box.getString("p_year").equals("")?v_dyear:box.getString("p_year");
			String v_month	= box.getString("p_month").equals("")?v_dmonth:box.getString("p_month");
			String v_day	= box.getString("p_day").equals("")?v_dday:box.getString("p_day");
		    
            db = new DatabaseExecute();
            
            sql = "		select max(decode(no,1,chk,0)) bweek,substr(max(decode(no,1,datez,'')),0,4) byear, ";
			sql += "			substr(max(decode(no,1,datez,'')),5,2) bmonth, ";
			sql += "			max(decode(no,2,chk,0)) tweek,substr(max(decode(no,2,datez,'')),0,4) tyear, ";
			sql += "			substr(max(decode(no,2,datez,'')),5,2) tmonth, ";
			sql += "			max(decode(no,3,chk,0)) aweek,substr(max(decode(no,3,datez,'')),0,4) ayear, ";
			sql += "			substr(max(decode(no,3,datez,'')),5,2) amonth, ";
			sql += "			max(decode(no,4,chk,0)) cweek,substr(max(decode(no,4,datez,'')),0,4) cyear, ";
			sql += "			substr(max(decode(no,4,datez,'')),5,2) cmonth ";
			sql += "	from ( ";
			sql += "		select 0 grp, 1 no, to_char(add_months(last_day('"+v_year+v_month+"01'),-1),'yyyymm') datez, ";
			sql += "			to_char(to_date( ";
			sql += "							to_char( ";
			sql += "									add_months(last_day('"+v_year+v_month+"01'),-1),'yyyymmdd' ";
			sql += "									),'yyyymmdd' ";
			sql += "							),'w' ";
			sql += "					)+ ";
			sql += "			 		decode(sign( ";
			sql += "						      to_char(add_months(to_date('"+v_year+v_month+"01','yyyymmdd'),-1),'d') ";
			sql += "				            -to_char(to_date(to_char(add_months(last_day('"+v_year+v_month+"01'),-1),'yyyymmdd'),'yyyymmdd'),'d') ";
			sql += "		         ),1,1,0) chk ";
			sql += "		from dual ";
			sql += "		union all ";
			sql += "		select 0 grp, 2 no, to_char(last_day('"+v_year+v_month+"01'),'yyyymm'), ";
			sql += "			to_char(to_date( ";
			sql += "							to_char(";
			sql += "									last_day('"+v_year+v_month+"01'),'yyyymmdd'";
			sql += "									),'yyyymmdd'";
			sql += "							),'w'";
			sql += "					)+";
			sql += "			 		decode(sign(";
			sql += "						      to_char(to_date('"+v_year+v_month+"01','yyyymmdd'),'d')";
			sql += "				            -to_char(to_date(to_char(last_day('"+v_year+v_month+"01'),'yyyymmdd'),'yyyymmdd'),'d')";
			sql += "		         ),1,1,0) chk";
			sql += "		 from dual";
			sql += "		union all";
			sql += "		select 0 grp, 3 no, to_char(add_months(last_day('"+v_year+v_month+"01'),1),'yyyymm'),";
			sql += "			to_char(to_date(";
			sql += "							to_char(";
			sql += "									add_months(last_day('"+v_year+v_month+"01'),1),'yyyymmdd'";
			sql += "									),'yyyymmdd'";
			sql += "							),'w'";
			sql += "					)+";
			sql += "			 		decode(sign(";
			sql += "						      to_char(add_months(to_date('"+v_year+v_month+"01','yyyymmdd'),1),'d')";
			sql += "				            -to_char(to_date(to_char(add_months(last_day('"+v_year+v_month+"01'),1),'yyyymmdd'),'yyyymmdd'),'d')";
			sql += "		         ),1,1,0) chk";
			sql += "		 from dual";
			sql += "		 union all";
			sql += "		select 0 grp, 4 no, to_char(to_date('"+v_year+v_month+v_day+"','yyyymmdd'),'yyyymm'),";
			sql += "			 to_char(to_date(";
			sql += "						   to_char(";
			sql += "								    to_date('"+v_year+v_month+v_day+"','yyyymmdd'),'yyyymmdd'";
			sql += "								    ),'yyyymmdd'";
			sql += "							   ),'w'";
			sql += "					  )+";
			sql += "					   decode(sign(";
			sql += "						         to_char(to_date('"+v_year+v_month+"01','yyyymmdd'),'d')";
			sql += "				              -to_char(to_date(to_char(to_date('"+v_year+v_month+v_day+"','yyyymmdd'),'yyyymmdd'),'yyyymmdd'),'d')";
			sql += "		         ),1,1,0) chk";
			sql += "		 from dual";
			sql += "	)AA";
			sql += "	group by grp";
            
            dbox = db.executeQuery(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return dbox;        
    } 


    /** 
    링크에 필요한 일자 정보를 가져온다.
    @param RequestBox box
    @return DataBox
    */
    public DataBox selectDayDate(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        DataBox dbox = null;
        String sql = "";

		Calendar gCal = Calendar.getInstance();
	
		String v_dyear	= Integer.toString(gCal.get(Calendar.YEAR));
		String v_dmonth = (gCal.get(Calendar.MONTH)<9)?"0"+Integer.toString(gCal.get(Calendar.MONTH)+1):Integer.toString(gCal.get(Calendar.MONTH)+1);
		String v_dday	= (gCal.get(Calendar.DATE)<10)?"0"+Integer.toString(gCal.get(Calendar.DATE)):Integer.toString(gCal.get(Calendar.DATE));

        try {

			String v_year	= box.getString("p_year").equals("")?v_dyear:box.getString("p_year");
			String v_month	= box.getString("p_month").equals("")?v_dmonth:box.getString("p_month");
			String v_day	= box.getString("p_day").equals("")?v_dday:box.getString("p_day");
		    
            db = new DatabaseExecute();
            
            sql =  "	select to_char(a.bday,'yyyy') byear, to_char(a.bday,'mm') bmonth, to_char(a.bday,'dd') bday, ";
			sql += "		to_char(a.tday,'yyyy') tyear, to_char(a.tday,'mm') tmonth, to_char(a.tday,'dd') tday, ";
			sql += "		to_char(a.aday,'yyyy') ayear, to_char(a.aday,'mm') amonth, to_char(a.aday,'dd') aday, ";
			sql += "		to_char(a.bday,'yyyymmdd') bdate, to_char(a.tday,'yyyymmdd') tdate, to_char(a.aday,'yyyymmdd') adate, ";
			sql += "		to_char(a.tday,'d') dayz ";
			sql += "	from ( ";
			sql += "		select to_date('"+v_year+v_month+v_day+"','yyyymmdd')-1 bday,to_date('"+v_year+v_month+v_day+"','yyyymmdd') tday,to_date('"+v_year+v_month+v_day+"','yyyymmdd')+1 aday ";
			sql += "		from dual ";
			sql += "		)A ";
            
            dbox = db.executeQuery(box, sql);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return dbox;        
    } 

    /** 
    월별 스케쥴 보기
    @param RequestBox box
    @return DataBox
    */
    public ArrayList selectScheduleMonth(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
        DatabaseExecute db = null;
		ArrayList list = null;
		ListSet ls1         = null;
        DataBox dbox = null;
        String sql = "";

		Calendar gCal = Calendar.getInstance();
	
		String v_dyear	= Integer.toString(gCal.get(Calendar.YEAR));
		String v_dmonth = (gCal.get(Calendar.MONTH)<9)?"0"+Integer.toString(gCal.get(Calendar.MONTH)+1):Integer.toString(gCal.get(Calendar.MONTH)+1);
		String v_dday	= (gCal.get(Calendar.DATE)<10)?"0"+Integer.toString(gCal.get(Calendar.DATE)):Integer.toString(gCal.get(Calendar.DATE));

        try {
        	
		    String v_userid = box.getSession("userid");

			String v_year	= box.getString("p_year").equals("")?v_dyear:box.getString("p_year");
			String v_month	= box.getString("p_month").equals("")?v_dmonth:box.getString("p_month");
			String v_day	= box.getString("p_day").equals("")?v_dday:box.getString("p_day");
		    
            //db = new DatabaseExecute();
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            list = new ArrayList();

            String s_language = box.getSession("languageName");
            
            sql =  "	select * ";
			sql += "	from ( ";
			//토론		1
			sql += "	select 1 type, ts.grcode, ts.subj, ts.year, ts.subjseq, ts.class, ts.userid, ";
			sql += "			ttp.title, ttp.started, ttp.ended, substr(ttp.ended,7,2) daynum, ";
			sql += "			'' place ";
			sql += "	from tu_student ts, tu_torontp ttp ";
			sql += "	where ts.userid='"+v_userid+"' ";
			sql += "		and ts.year="+v_year+" ";
			sql += "		and ts.chkfinal='Y' ";
			sql += "		and ts.grcode = ttp.grcode ";
			sql += "		and ts.subj = ttp.subj ";
			sql += "		and ts.year = ttp.year ";
			sql += "		and ts.subjseq = ttp.subjseq ";
			sql += "		and ts.class = ttp.class ";
			sql += "		and ttp.ended like '"+v_year+v_month+"%' ";
			sql += "	union all ";
			//리포트	2
			sql += "	select 2 type, ts.grcode, ts.subj, ts.year, ts.subjseq, ts.class, ts.userid, ";
			sql += "		tpd.title, tpd.startdate, tpd.expiredate, substr(tpd.expiredate,7,2) daynum, ";
			sql += "			'' place ";
			sql += "	from tu_student ts, tu_projord tpd ";
			sql += "	where ts.userid='"+v_userid+"' ";
			sql += "		and ts.year="+v_year+" ";
			sql += "		and ts.chkfinal='Y' ";
			sql += "		and ts.grcode = tpd.grcode ";
			sql += "		and ts.subj = tpd.subj ";
			sql += "		and ts.year = tpd.year ";
			sql += "		and ts.subjseq = tpd.subjseq ";
			sql += "		and ts.class = tpd.class ";
			sql += "		and tpd.expiredate like '"+v_year+v_month+"%' ";
			
			//온라인시험3(정시)
			sql += "	union all ";
			sql += "	select 3 type, ts.grcode, ts.subj, ts.year, ts.subjseq, ts.class, ts.userid, ";
			sql += "		tep.examtype, tep.sdate, tep.edate, substr(tep.edate,7,2) daynum, ";
			sql += "			'' place ";
			sql += "	from tu_student ts, tu_exampaper tep ";
			sql += "	where ts.userid='"+v_userid+"' ";
			sql += "		and ts.year="+v_year+" ";
			sql += "		and ts.chkfinal='Y' ";
			sql += "		and ts.subj = tep.subj ";
			sql += "		and ts.year = tep.year ";
			sql += "		and ts.subjseq = tep.subjseq ";
			sql += "		and tep.edate like '"+v_year+v_month+"%' ";

			//온라인시험4(재시)
			sql += "	union all ";
			sql += "	select 4 type, ts.grcode, ts.subj, ts.year, ts.subjseq, ts.class, ts.userid, ";
			sql += "		tep.examtype, tep.resdate, tep.reedate, substr(tep.reedate,7,2) daynum, ";
			sql += "			'' place ";
			sql += "	from tu_student ts, tu_exampaper tep ";
			sql += "	where ts.userid='"+v_userid+"' ";
			sql += "		and ts.year="+v_year+" ";
			sql += "		and ts.chkfinal='Y' ";
			sql += "		and ts.subj = tep.subj ";
			sql += "		and ts.year = tep.year ";
			sql += "		and ts.subjseq = tep.subjseq ";
			sql += "		and tep.reedate like '"+v_year+v_month+"%' ";

			//도서대출  7
			
			/*
			sql += "	union all ";
			sql += "	select 7 type, '', '', '', '', '', '',  ";
			sql += "		convert(UTL_RAW.CAST_TO_VARCHAR2(title),'utf8','KO16MSWIN949') title, ";
			sql += "		loan_date, return_plan_date, substr(return_plan_date,7,2) daynum, ";
			sql += "			'' place ";
			sql += "	from vw_sungbook@sung_book ";
			sql += "	where user_id=lpad('"+v_userid+"',15,'*') ";
			sql += "		and return_plan_date like '"+v_year+v_month+"%' ";
			*/
			

			//상담		8
			sql += "	union all ";
			sql += "	select 8 type, '', '', '', '', '', '',  ";
			sql += "		tcc.reqtitle, tcc.bookdate, tcc.bookdate, substr(tcc.bookdate,7,2) daynum, ";
			sql += "			'' place ";
			sql += "	from tu_counsel_content tcc ";
			sql += "	where (tcc.reqbirth_date='"+v_userid+"' ";
			sql += "		or tcc.reqprofid='"+v_userid+"') ";
			sql += "		and tcc.bookdate like '"+v_year+v_month+"%' ";

			//수업		9
			sql += "	union all ";
			sql += "	select 9 type, '', '', '', '', '', '',  ";
			sql += "		a.name ,to_char(b.chkdate,'yyyymmdd') ,to_char(b.chkdate,'yyyymmdd'), substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "		'' place ";
			sql += "	from (select st.grcode, st.year, st.subjseq, st.gubun, vhs.subj, vhs.class, ";
			sql += "		  st.sdate, st.edate, vhs.weekday, vhs.stime, vhs.etime, ft.name ";
			sql += "		  from ( ";
			sql += "			 select tsj.grcode, tsj.subj, tsj.year, tsj.subjseq, tsj.class, tsj." + makeLangSql("subjnm",box) + ", tm.mtype ";
			sql += "			 from tu_student ts, tu_subjseq tsj, tu_member tm ";
			sql += "			 where ts.grcode='N000001' ";
			sql += "			  and ts.year = '"+v_year+"' ";
			sql += "			  and ts.userid='"+v_userid+"' ";
			sql += "			  and ts.grcode = tsj.grcode ";
			sql += "			  and ts.subj = tsj.subj ";
			sql += "			  and ts.year = tsj.year ";
			sql += "			  and ts.subjseq = tsj.subjseq ";
			sql += "			  and ts.class = tsj.class ";
			sql += "			  and ts.userid = tm.userid) FT, ";
			sql += "			  ( ";
			sql += "			    select tgs.grcode, tgs.year, tgs.subjseq, tgs.gubun, tgs.scheduleid, tgs.sdate, tgs.edate ";
			sql += "			    from (select grcode, year, subjseq, gubun, max(scheduleid) scheduleid ";
			sql += "					  from tu_gr_schedule ";
			sql += "					  where schedulegubun = 1 ";
			sql += "					  group by grcode, year, subjseq, gubun)A, tu_gr_schedule tgs ";
			sql += "			    where A.grcode = tgs.grcode ";
			sql += "				   and A.year = tgs.year ";
			sql += "				   and A.subjseq = tgs.subjseq ";
			sql += "				   and A.gubun = tgs.gubun ";
			//sql += "				   and A.scheduleid = tgs.scheduleid) ST, VW_HAKSA_SUBJTIME VHS ";
			sql += "				   and A.scheduleid = tgs.scheduleid) ST, TU_SUBJTIME VHS ";
			sql += "		  where st.grcode = vhs.grcode ";
			sql += "			  and st.year = vhs.year ";
			sql += "			  and st.subjseq = vhs.subjseq ";

			sql += "				and ft.grcode = st.grcode ";
			sql += "				and ft.year = st.year ";
			sql += "				and ft.subjseq = st.subjseq ";
			sql += "				and substr(ft.mtype,0,1) = st.gubun ";
			sql += "				and ft.grcode = vhs.grcode ";
			sql += "				and ft.subj = vhs.subj ";
			sql += "				and ft.year = vhs.year ";
			sql += "				and ft.subjseq = vhs.subjseq ";
			sql += "				and ft.class = vhs.class ";

			sql += "			  )A, ";
			sql += "		 (select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate ";
			sql += "		  from tu_member ";
			sql += "		  where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual) ";
			sql += "		  ) B ";
			sql += "	where A.sdate<=to_char(chkdate,'yyyy-mm-dd') ";
			sql += "	 and A.edate>=to_char(chkdate,'yyyy-mm-dd') ";
			sql += "	 and a.weekday = decode(to_char(chkdate,'d'),1,'일',2,'월',3,'화',4,'수',5,'목',6,'금',7,'토') ";

			//개인일정	반복없는 일정 - 5
			sql += "	union all ";
			sql += "	select 5 type, scheduleid||'', '', '', '', '', luserid, ";
			sql += "		title, sdate, edate, substr(edate,7,2) daynum, ";
			sql += "		place ";
			sql += "	from tu_schedule ";
			sql += "	where repeatgubun = 'N' and edate like '"+v_year+v_month+"%' ";
			sql += "		and luserid = '"+v_userid+"' ";
			
			
			
			//개인일정2 일단위 반복있는 일정 - 6
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, to_char(a.sdate1,'yyyymmdd') sdate, to_char(b.chkdate,'yyyymmdd') chkdate, ";
			sql += "		substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "			a.place ";
			sql += "	from ( ";
			sql += "		 select to_date(substr(sdate,0,8),'yyyymmdd') sdate1, repeatgubun, repeatterm, ";
			sql += "			fdate, repeatcount, repeatopt1, repeatopt2, repeatopt3, ";
			sql += "			title, sdate, edate, scheduleid, luserid, place ";
			sql += "		 from tu_schedule ";
			sql += "		 where repeatgubun = 'D' and fdate >= '"+v_year+v_month+"01' ";
			sql += "		and luserid = '"+v_userid+"' ";
			sql += "		and sdate<='"+v_year+v_month+"31')A , ";
			sql += "		(select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate ";
			sql += "		 from tu_member ";
			sql += "		 where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual)) B ";
			sql += "	where mod((b.chkdate-a.sdate1),a.repeatterm)=0 ";
			sql += "		and b.chkdate>=a.sdate1 ";
			sql += "		and b.chkdate<a.fdate ";
			
			
			//개인일정3 주단위 반복있는 일정(특정 요일)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, to_char(a.sdate1,'yyyymmdd') sdate, to_char(b.chkdate,'yyyymmdd') chkdate, ";
			sql += "		substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "			a.place ";
			sql += "	from ( ";
			sql += "		  select to_date(substr(sdate,0,8),'yyyymmdd') sdate1, repeatgubun, repeatterm, ";
			sql += "			 to_date(fdate,'yyyymmdd') fdate, repeatcount, repeatopt1, repeatopt2, repeatopt3, ";
			sql += "			 title, sdate, edate, scheduleid, luserid, place ";
			sql += "		  from tu_schedule ";
			sql += "		  where repeatgubun = 'W' and fdate >= '"+v_year+v_month+"01' ";
			sql += "			and sdate<='"+v_year+v_month+"31' and luserid = '"+v_userid+"')A , ";
			sql += "		 (select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate ";
			sql += "		  from tu_member ";
			sql += "		  where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual)) B ";
			sql += "	where  to_char(b.chkdate,'d')=a.repeatopt1 ";
			sql += "		 and b.chkdate>=a.sdate1 ";
			sql += "		 and b.chkdate<=a.fdate  ";
			sql += "		 and mod(b.chkdate-(a.sdate1-to_char(a.sdate1,'d')+a.repeatopt1),repeatterm*7)=0 ";
			
			/*
			//개인일정4 월단위 반복있는 일정(특정 일자)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, substr(a.sdate,0,8) sdate, ";
			sql += "		substr('"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8) chkdate, ";
			sql += "		decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) daynum, ";
			sql += "			a.place ";
			sql += "	from ( ";
			sql += "		  select repeatgubun, repeatterm, fdate, repeatcount, repeatopt1, repeatopt2, ";
			sql += "				repeatopt3, ";
			sql += "				title, sdate, edate, scheduleid, luserid, place ";
			sql += "		  from tu_schedule ";
			sql += "		  where repeatgubun = 'M' and repeatopt3=0 and luserid = '"+v_userid+"'";
			sql += "			  and fdate >= '"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) and sdate<='"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2))A ";
			sql += "	where mod(to_char(to_date(substr('"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8),'yyyymmdd'),'mm') ";
			sql += "		 -(to_char(to_date(substr(sdate,0,8),'yyyymmdd'),'mm')),repeatterm)=0 ";
			
			//개인일정5 월단위 반복있는 일정(특정 주 의 특정 요일)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, substr(a.sdate,0,8) sdate, to_char(b.chkdate,'yyyymmdd') chkdate, ";
			sql += "		substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "			a.place ";
			sql += "	from ( ";
			sql += "		  select repeatgubun, repeatterm, fdate, repeatcount, repeatopt1, repeatopt2, ";
			sql += "				 repeatopt3, ";
			sql += "				 title, sdate, edate, scheduleid,luserid, place ";
			sql += "		  from tu_schedule ";
			sql += "		  where repeatgubun = 'M' and repeatopt3>0 and luserid = '"+v_userid+"')A , ";
			sql += "		 ( ";
			sql += "		  select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate ";
			sql += "		  from tu_member ";
			sql += "		  where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual) ";
			sql += "		 ) B ";
			sql += "	where mod(to_char(to_date(substr('"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8),'yyyymmdd'),'mm') ";
			sql += "		 -(to_char(to_date(substr(sdate,0,8),'yyyymmdd'),'mm')),repeatterm)=0 ";
			sql += "			and to_char(b.chkdate,'w')=a.repeatopt2 ";
			sql += "			and to_char(b.chkdate,'d')=a.repeatopt3 ";
			//개인일정6 년단위 반복있는 일정(특정 일자)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, substr(a.sdate,0,8) sdate, '"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "		||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) chkdate, ";
			sql += "		decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) daynum, ";
			sql += "			a.place ";
			sql += "	from ( ";
			sql += "		  select repeatgubun, repeatterm, fdate, repeatcount, repeatopt1, repeatopt2, ";
			sql += "				repeatopt3,sdate,";
			sql += "				scheduleid, edate, title, luserid, place ";
			sql += "		  from tu_schedule ";
			sql += "		  where repeatgubun = 'Y' and repeatopt3=0 and luserid = '"+v_userid+"'";
			sql += "			 and fdate >= '"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "				||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) ";
			sql += "		 	 and sdate<='"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "				||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2))A ";
			sql += "	where mod(to_char(to_date(substr('"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "			||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8),'yyyymmdd'),'yyyy') ";
			sql += "			 -(to_char(to_date(substr(sdate,0,8),'yyyymmdd'),'yyyy')),repeatterm)=0 ";
			sql += "			and '"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) = '"+v_year+v_month+"' ";
			//개인일정7 년단위 반복있는 일정(특정 주 의 특정 요일)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, substr(a.sdate,0,8) sdate, to_char(b.chkdate,'yyyymmdd') chkdate, ";
			sql += "		substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "			a.place ";
			sql += "	from ( ";
			sql += "		 select repeatgubun, repeatterm, fdate, repeatcount, repeatopt1, repeatopt2,";
			sql += "				repeatopt3,sdate,";
			sql += "				title, edate, scheduleid, luserid, place ";
			sql += "		 from tu_schedule ";
			sql += "		 where repeatgubun = 'Y' and repeatopt3>0 and luserid = '"+v_userid+"')A , ";
			sql += "		 (";
			sql += "		  select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate";
			sql += "		  from tu_member";
			sql += "		  where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual) ";
			sql += "		 ) B";
			sql += "	where mod(to_char(to_date(substr('"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "		||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8),'yyyymmdd'),'yyyy')";
			sql += "		 -(to_char(to_date(substr(sdate,0,8),'yyyymmdd'),'yyyy')),repeatterm)=0 ";
			sql += "		and to_char(b.chkdate,'mm')=a.repeatopt1 ";
			sql += "		and to_char(b.chkdate,'w')=a.repeatopt2 ";
			sql += "		and to_char(b.chkdate,'d')=a.repeatopt3 ";*/
						
			sql += "	) AA ";
			sql += "	order by ended ";
            //----------------------   월별 스케쥴 보기 ----------------------------
            //list = db.executeQueryList(box, sql);
			//System.out.println(sql);
			
			ls1 = connMgr.executeQuery(sql);
 
            while (ls1.next() ) {
                dbox = ls1.getDataBox();
				list.add(dbox);
			} 

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            connMgr.commit();
            if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;        
    } 


    /** 
    주별 스케쥴 보기
    @param RequestBox box
    @return DataBox
    */
    public ArrayList selectScheduleWeek(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        DatabaseExecute db = null;
		ArrayList list = null;
		ListSet  ls1 = null;
        DataBox dbox = null;
		DataBox dbox1 = null;
        String sql = "";

		Calendar gCal = Calendar.getInstance();
	
		String v_dyear	= Integer.toString(gCal.get(Calendar.YEAR));
		String v_dmonth = (gCal.get(Calendar.MONTH)<9)?"0"+Integer.toString(gCal.get(Calendar.MONTH)+1):Integer.toString(gCal.get(Calendar.MONTH)+1);
		String v_dday	= (gCal.get(Calendar.DATE)<10)?"0"+Integer.toString(gCal.get(Calendar.DATE)):Integer.toString(gCal.get(Calendar.DATE));

        try {
        	
		    String v_userid = box.getSession("userid");

			//db = new DatabaseExecute();
			connMgr = new DBConnectionManager();
			list = new ArrayList();

			String v_year	= box.getString("p_year").equals("")?v_dyear:box.getString("p_year");
			String v_month	= box.getString("p_month").equals("")?v_dmonth:box.getString("p_month");
			String v_day	= box.getString("p_day").equals("")?v_dday:box.getString("p_day");
			String v_week	= box.getString("p_week").equals("")?"":box.getString("p_week");

			
		   //----------------------   현재 일자가 몇 주차 인지를 가져온다.----------------------------
			if (v_week.equals(""))	
			{            
               sql =  " select A.chk||'' chk";
			   sql += " from ( ";
			   sql += "		 select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate, ";
			   sql += "			to_char(to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)),'w')+ ";
			   sql += "			decode(sign( ";
			   sql += "				    to_char(to_date('"+v_year+v_month+"01','yyyymmdd'),'d') ";
			   sql += "		           -to_char(to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)),'d') ";
			   sql += "			    	   ),1,1,0) chk ";
			   sql += "		 from tu_member ";
			   sql += "		 where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual) ";
			   sql += "		)A ";
			   sql += " where a.chkdate=to_date('"+v_year+v_month+v_day+"','yyyymmdd') ";			   
     
               //dbox1 = db.executeQuery(box, sql);      
               ls1 = connMgr.executeQuery(sql);
               if (ls1.next() ) {
                   dbox1 = ls1.getDataBox();
			   }
               
               v_week = dbox1.getString("d_chk");
               ls1.close();     
			}
			System.out.println("==================="+v_week);

            String s_language = box.getSession("languageName");
            
            sql =  "	select AA.*, AB.weekday, AB.chkdate1 ";
			sql += "	from ( ";
			//토론	1
			sql += "	select 1 type, ts.grcode, ts.subj, ts.year, ts.subjseq, ts.class, ts.userid, ";
			sql += "			ttp.title, ttp.started, substr(ttp.ended,0,8) ended, substr(ttp.ended,7,2) daynum, ";
			sql += "			'' stime, '' etime, '' place, 0 scheduleid ";
			sql += "	from tu_student ts, tu_torontp ttp ";
			sql += "	where ts.userid='"+v_userid+"' ";
			sql += "		and ts.year="+v_year+" ";
			sql += "		and ts.chkfinal='Y' ";
			sql += "		and ts.grcode = ttp.grcode ";
			sql += "		and ts.subj = ttp.subj ";
			sql += "		and ts.year = ttp.year ";
			sql += "		and ts.subjseq = ttp.subjseq ";
			sql += "		and ts.class = ttp.class ";
			sql += "		and ttp.ended like '"+v_year+v_month+"%' ";
			sql += "	union all ";
			//리포트	2
			sql += "	select 2 type, ts.grcode, ts.subj, ts.year, ts.subjseq, ts.class, ts.userid, ";
			sql += "		tpd.title, tpd.startdate, substr(tpd.expiredate,0,8) expiredate, substr(tpd.expiredate,7,2) daynum, ";
			sql += "			'' stime, tpd.expiredate etime, '' place, 0 scheduleid ";
			sql += "	from tu_student ts, tu_projord tpd ";
			sql += "	where ts.userid='"+v_userid+"' ";
			sql += "		and ts.year="+v_year+" ";
			sql += "		and ts.chkfinal='Y' ";
			sql += "		and ts.grcode = tpd.grcode ";
			sql += "		and ts.subj = tpd.subj ";
			sql += "		and ts.year = tpd.year ";
			sql += "		and ts.subjseq = tpd.subjseq ";
			sql += "		and ts.class = tpd.class ";
			sql += "		and tpd.expiredate like '"+v_year+v_month+"%' ";
			
			//온라인시험3(정시)
			sql += "	union all ";
			sql += "	select 3 type, ts.grcode, ts.subj, ts.year, ts.subjseq, ts.class, ts.userid, ";
			sql += "		tep.examtype, tep.sdate, substr(tep.edate,0,8) edate, substr(tep.edate,7,2) daynum, ";
			sql += "			tep.sdate, tep.edate, '' place, 0 scheduleid ";
			sql += "	from tu_student ts, tu_exampaper tep ";
			sql += "	where ts.userid='"+v_userid+"' ";
			sql += "		and ts.year="+v_year+" ";
			sql += "		and ts.chkfinal='Y' ";
			sql += "		and ts.subj = tep.subj ";
			sql += "		and ts.year = tep.year ";
			sql += "		and ts.subjseq = tep.subjseq ";
			sql += "		and tep.edate like '"+v_year+v_month+"%' ";

			//온라인시험4(재시)
			sql += "	union all ";
			sql += "	select 4 type, ts.grcode, ts.subj, ts.year, ts.subjseq, ts.class, ts.userid, ";
			sql += "		tep.examtype, tep.resdate, substr(tep.reedate,0,8) reedate, substr(tep.reedate,7,2) daynum, ";
			sql += "			tep.sdate, tep.edate, '' place, 0 scheduleid ";
			sql += "	from tu_student ts, tu_exampaper tep ";
			sql += "	where ts.userid='"+v_userid+"' ";
			sql += "		and ts.year="+v_year+" ";
			sql += "		and ts.chkfinal='Y' ";
			sql += "		and ts.subj = tep.subj ";
			sql += "		and ts.year = tep.year ";
			sql += "		and ts.subjseq = tep.subjseq ";
			sql += "		and tep.reedate like '"+v_year+v_month+"%' ";

			//도서대출  7
			/*
			sql += "	union all ";
			sql += "	select 7 type, '', '', '', '', '', '',  ";
			sql += "		convert(UTL_RAW.CAST_TO_VARCHAR2(title),'utf8','KO16MSWIN949') title, ";
			sql += "		loan_date, substr(return_plan_date,0,8) edate, substr(return_plan_date,7,2) daynum, ";
			sql += "			loan_date, return_plan_date, '' place, 0 scheduleid  ";
			sql += "	from vw_sungbook@sung_book ";
			sql += "	where user_id=lpad('"+v_userid+"',15,'*') ";
			sql += "		and return_plan_date like '"+v_year+v_month+"%' ";
			*/

			//상담		8
			sql += "	union all ";
			sql += "	select 8 type, '', '', '', '', '', '',  ";
			sql += "		tcc.reqtitle, tcc.bookdate, substr(tcc.bookdate,0,8) edate, substr(tcc.bookdate,7,2) daynum, ";
			sql += "			tcc.bookdate, tcc.bookdate, '' place, 0 scheduleid ";
			sql += "	from tu_counsel_content tcc ";
			sql += "	where (tcc.reqbirth_date='"+v_userid+"' ";
			sql += "		or tcc.reqprofid='"+v_userid+"') ";
			sql += "		and tcc.bookdate like '"+v_year+v_month+"%' ";

			//수업		9
			sql += "	union all ";
			sql += "	select 9 type, '', '', '', '', '', '',  ";
			sql += "		a.name ,to_char(b.chkdate,'yyyymmdd') ,to_char(b.chkdate,'yyyymmdd'), substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "			a.stime||'' , a.etime||'' , '' place, 0 scheduleid ";
			sql += "	from (select st.grcode, st.year, st.subjseq, st.gubun, vhs.subj, vhs.class, ";
			sql += "		  st.sdate, st.edate, vhs.weekday, vhs.stime, vhs.etime, ft.name ";
			sql += "		  from ( ";
			sql += "			 select tsj.grcode, tsj.subj, tsj.year, tsj.subjseq, tsj.class, tsj." + makeLangSql("subjnm",box) + ", tm.mtype ";
			sql += "			 from tu_student ts, tu_subjseq tsj, tu_member tm ";
			sql += "			 where ts.grcode='N000001' ";
			sql += "			  and ts.year = '"+v_year+"' ";
			sql += "			  and ts.userid='"+v_userid+"' ";
			sql += "			  and ts.grcode = tsj.grcode ";
			sql += "			  and ts.subj = tsj.subj ";
			sql += "			  and ts.year = tsj.year ";
			sql += "			  and ts.subjseq = tsj.subjseq ";
			sql += "			  and ts.class = tsj.class ";
			sql += "			  and ts.userid = tm.userid) FT, ";
			sql += "				( ";
			sql += "			    select tgs.grcode, tgs.year, tgs.subjseq, tgs.gubun, tgs.scheduleid, tgs.sdate, tgs.edate ";
			sql += "			    from (select grcode, year, subjseq, gubun, max(scheduleid) scheduleid ";
			sql += "					  from tu_gr_schedule ";
			sql += "					  where schedulegubun = 1 ";
			sql += "					  group by grcode, year, subjseq, gubun)A, tu_gr_schedule tgs ";
			sql += "			    where A.grcode = tgs.grcode ";
			sql += "				   and A.year = tgs.year ";
			sql += "				   and A.subjseq = tgs.subjseq ";
			sql += "				   and A.gubun = tgs.gubun ";
			//sql += "				   and A.scheduleid = tgs.scheduleid) ST, VW_HAKSA_SUBJTIME VHS ";
			sql += "				   and A.scheduleid = tgs.scheduleid) ST, TU_SUBJTIME VHS ";
			sql += "		  where st.grcode = vhs.grcode ";
			sql += "			  and st.year = vhs.year ";
			sql += "			  and st.subjseq = vhs.subjseq ";

			sql += "				and ft.grcode = st.grcode ";
			sql += "				and ft.year = st.year ";
			sql += "				and ft.subjseq = st.subjseq ";
			sql += "				and substr(ft.mtype,0,1) = st.gubun ";
			sql += "				and ft.grcode = vhs.grcode ";
			sql += "				and ft.subj = vhs.subj ";
			sql += "				and ft.year = vhs.year ";
			sql += "				and ft.subjseq = vhs.subjseq ";
			sql += "				and ft.class = vhs.class ";

			sql += "			  )A, ";
			sql += "		 (select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate ";
			sql += "		  from tu_member ";
			sql += "		  where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual) ";
			sql += "		  ) B ";
			sql += "	where A.sdate<=to_char(chkdate,'yyyy-mm-dd') ";
			sql += "	 and A.edate>=to_char(chkdate,'yyyy-mm-dd') ";
			sql += "	 and a.weekday = decode(to_char(chkdate,'d'),1,'일',2,'월',3,'화',4,'수',5,'목',6,'금',7,'토') ";


			//개인일정1 반복없는 일정	5
			sql += "	union all ";
			sql += "	select 5 type, scheduleid||'', '', '', '', '', luserid, ";
			sql += "		title, sdate, substr(edate,0,8) edate, substr(edate,7,2) daynum, ";
			sql += "		sdate stime, edate etime, place, scheduleid ";
			sql += "	from tu_schedule ";
			sql += "	where repeatgubun = 'N' and edate like '"+v_year+v_month+"%' ";
			sql += "		and luserid = '"+v_userid+"' ";
			
			
			
			//개인일정2 일단위 반복있는 일정	6
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, to_char(a.sdate1,'yyyymmdd') sdate, to_char(b.chkdate,'yyyymmdd') chkdate, ";
			sql += "		substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "		a.sdate stime, a.edate etime, a.place, a.scheduleid ";
			sql += "	from ( ";
			sql += "		 select to_date(substr(sdate,0,8),'yyyymmdd') sdate1, repeatgubun, repeatterm, ";
			sql += "			fdate, repeatcount, repeatopt1, repeatopt2, repeatopt3, ";
			sql += "			title, sdate, edate, scheduleid, luserid, place ";
			sql += "		 from tu_schedule ";
			sql += "		 where repeatgubun = 'D' and fdate >= '"+v_year+v_month+"01' ";
			sql += "		and luserid = '"+v_userid+"' ";
			sql += "		and sdate<='"+v_year+v_month+"31')A , ";
			sql += "		(select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate ";
			sql += "		 from tu_member ";
			sql += "		 where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual)) B ";
			sql += "	where mod((b.chkdate-a.sdate1),a.repeatterm)=0 ";
			sql += "		and b.chkdate>=a.sdate1 ";
			sql += "		and b.chkdate<a.fdate ";
			
			
			//개인일정3 주단위 반복있는 일정(특정 요일)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, to_char(a.sdate1,'yyyymmdd') sdate, to_char(b.chkdate,'yyyymmdd') chkdate, ";
			sql += "		substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "		a.sdate stime, a.edate etime, a.place, a.scheduleid ";
			sql += "	from ( ";
			sql += "		  select to_date(substr(sdate,0,8),'yyyymmdd') sdate1, repeatgubun, repeatterm, ";
			sql += "			 to_date(fdate,'yyyymmdd') fdate, repeatcount, repeatopt1, repeatopt2, repeatopt3, ";
			sql += "			 title, sdate, edate, scheduleid, luserid, place ";
			sql += "		  from tu_schedule ";
			sql += "		  where repeatgubun = 'W' and fdate >= '"+v_year+v_month+"01' ";
			sql += "			and sdate<='"+v_year+v_month+"31' and luserid = '"+v_userid+"')A , ";
			sql += "		 (select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate ";
			sql += "		  from tu_member ";
			sql += "		  where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual)) B ";
			sql += "	where  to_char(b.chkdate,'d')=a.repeatopt1 ";
			sql += "		 and b.chkdate>=a.sdate1 ";
			sql += "		 and b.chkdate<=a.fdate  ";
			sql += "		 and mod(b.chkdate-(a.sdate1-to_char(a.sdate1,'d')+a.repeatopt1),repeatterm*7)=0 ";
			
			/*
			//개인일정4 월단위 반복있는 일정(특정 일자)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, substr(a.sdate,0,8) sdate, ";
			sql += "		substr('"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8) chkdate, ";
			sql += "		decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) daynum, ";
			sql += "		a.sdate stime, a.edate etime, a.place, a.scheduleid ";
			sql += "	from ( ";
			sql += "		  select repeatgubun, repeatterm, fdate, repeatcount, repeatopt1, repeatopt2, ";
			sql += "				repeatopt3, ";
			sql += "				title, sdate, edate, scheduleid, luserid, place ";
			sql += "		  from tu_schedule ";
			sql += "		  where repeatgubun = 'M' and repeatopt3=0 and luserid = '"+v_userid+"'";
			sql += "			  and fdate >= '"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) and sdate<='"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2))A ";
			sql += "	where mod(to_char(to_date(substr('"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8),'yyyymmdd'),'mm') ";
			sql += "		 -(to_char(to_date(substr(sdate,0,8),'yyyymmdd'),'mm')),repeatterm)=0 ";
			
			//개인일정5 월단위 반복있는 일정(특정 주 의 특정 요일)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, substr(a.sdate,0,8) sdate, to_char(b.chkdate,'yyyymmdd') chkdate, ";
			sql += "		substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "		a.sdate stime, a.edate etime, a.place, a.scheduleid ";
			sql += "	from ( ";
			sql += "		  select repeatgubun, repeatterm, fdate, repeatcount, repeatopt1, repeatopt2, ";
			sql += "				 repeatopt3, ";
			sql += "				 title, sdate, edate, scheduleid,luserid, place ";
			sql += "		  from tu_schedule ";
			sql += "		  where repeatgubun = 'M' and repeatopt3>0 and luserid = '"+v_userid+"')A , ";
			sql += "		 ( ";
			sql += "		  select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate ";
			sql += "		  from tu_member ";
			sql += "		  where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual) ";
			sql += "		 ) B ";
			sql += "	where mod(to_char(to_date(substr('"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8),'yyyymmdd'),'mm') ";
			sql += "		 -(to_char(to_date(substr(sdate,0,8),'yyyymmdd'),'mm')),repeatterm)=0 ";
			sql += "			and to_char(b.chkdate,'w')=a.repeatopt2 ";
			sql += "			and to_char(b.chkdate,'d')=a.repeatopt3 ";
			//개인일정6 년단위 반복있는 일정(특정 일자)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, substr(a.sdate,0,8) sdate1, '"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "		||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) chkdate, ";
			sql += "		decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) daynum, ";
			sql += "		a.sdate stime, a.edate etime, a.place, a.scheduleid ";
			sql += "	from ( ";
			sql += "		  select repeatgubun, repeatterm, fdate, repeatcount, repeatopt1, repeatopt2, ";
			sql += "				repeatopt3,sdate,";
			sql += "				scheduleid, edate, title, luserid, place ";
			sql += "		  from tu_schedule ";
			sql += "		  where repeatgubun = 'Y' and repeatopt3=0 and luserid = '"+v_userid+"'";
			sql += "			 and fdate >= '"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "				||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) ";
			sql += "		 	 and sdate<='"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "				||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2))A ";
			sql += "	where mod(to_char(to_date(substr('"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "			||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8),'yyyymmdd'),'yyyy') ";
			sql += "			 -(to_char(to_date(substr(sdate,0,8),'yyyymmdd'),'yyyy')),repeatterm)=0 ";
			sql += "			and '"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) = '"+v_year+v_month+"' ";
			//개인일정7 년단위 반복있는 일정(특정 주 의 특정 요일)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, substr(a.sdate,0,8) sdate1, to_char(b.chkdate,'yyyymmdd') chkdate, ";
			sql += "		substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "		a.sdate stime, a.edate etime, a.place, a.scheduleid ";
			sql += "	from ( ";
			sql += "		 select repeatgubun, repeatterm, fdate, repeatcount, repeatopt1, repeatopt2,";
			sql += "				repeatopt3,sdate,";
			sql += "				title, edate, scheduleid, luserid, place";
			sql += "		 from tu_schedule ";
			sql += "		 where repeatgubun = 'Y' and repeatopt3>0 and luserid = '"+v_userid+"')A , ";
			sql += "		 (";
			sql += "		  select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate";
			sql += "		  from tu_member";
			sql += "		  where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual) ";
			sql += "		 ) B";
			sql += "	where mod(to_char(to_date(substr('"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "		||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8),'yyyymmdd'),'yyyy')";
			sql += "		 -(to_char(to_date(substr(sdate,0,8),'yyyymmdd'),'yyyy')),repeatterm)=0 ";
			sql += "		and to_char(b.chkdate,'mm')=a.repeatopt1 ";
			sql += "		and to_char(b.chkdate,'w')=a.repeatopt2 ";
			sql += "		and to_char(b.chkdate,'d')=a.repeatopt3 ";						*/
			sql += "	) AA , ";
			sql += "	(	 select '"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum) chkdate1, ";
			sql += "				to_char(to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum),'yyyymmdd'),'d') weekday, ";
			sql += "			to_char(to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)),'w')+ ";
			sql += "			decode(sign( ";
			sql += "				    to_char(to_date('"+v_year+v_month+"01','yyyymmdd'),'d') ";
			sql += "		           -to_char(to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)),'d') ";
			sql += "			    	   ),1,1,0) chk ";
			sql += "		 from tu_member ";
			sql += "		 where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual) ";
			sql += "	)AB ";
			sql += " where AB.chk='"+v_week+"' ";	
			sql += "	and AA.ended(+) = AB.chkdate1 ";	
			sql += "	order by AB.chkdate1, AA.stime ";
			
			
            //----------------------   주별 스케쥴 보기 ----------------------------
            //list = db.executeQueryList(box, sql);
			//System.out.println(sql);			
			
			
			ls1 = connMgr.executeQuery(sql);
            while (ls1.next() ) {
                dbox = ls1.getDataBox();
				list.add(dbox);
			}

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
        	connMgr.commit();
            if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;        
    } 


    /** 
    일별 스케쥴 보기(일반 일정)
    @param RequestBox box
    @return DataBox
    */
    public ArrayList selectScheduleDay(RequestBox box) throws Exception {
        DatabaseExecute db = null;
		ArrayList list = null;
		ListSet ls1         = null;
        DataBox dbox = null;
        String sql = "";

		Calendar gCal = Calendar.getInstance();
	
		String v_dyear	= Integer.toString(gCal.get(Calendar.YEAR));
		String v_dmonth = (gCal.get(Calendar.MONTH)<9)?"0"+Integer.toString(gCal.get(Calendar.MONTH)+1):Integer.toString(gCal.get(Calendar.MONTH)+1);
		String v_dday	= (gCal.get(Calendar.DATE)<10)?"0"+Integer.toString(gCal.get(Calendar.DATE)):Integer.toString(gCal.get(Calendar.DATE));

        try {
        	
		    String v_userid = box.getSession("userid");

			String v_year	= box.getString("p_year").equals("")?v_dyear:box.getString("p_year");
			String v_month	= box.getString("p_month").equals("")?v_dmonth:box.getString("p_month");
			String v_day	= box.getString("p_day").equals("")?v_dday:box.getString("p_day");
		    
            db = new DatabaseExecute();

            String s_language = box.getSession("languageName");
            
            sql =  "	select AA.* ";
			sql += "	from ( ";


			//개인일정1 반복없는 일정	5
			sql += "	select 5 type, scheduleid||'' grcode, '' subj, '' year, '' subjseq, '' class, luserid userid, ";
			sql += "		title, sdate started, substr(edate,0,8) ended, substr(edate,7,2) daynum, ";
			sql += "		sdate stime, edate etime, place, scheduleid ";
			sql += "	from tu_schedule ";
			sql += "	where repeatgubun = 'N' and edate like '"+v_year+v_month+"%' ";
			sql += "		and luserid = '"+v_userid+"' ";

			//개인일정2 일단위 반복있는 일정	6
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, to_char(a.sdate1,'yyyymmdd') sdate, to_char(b.chkdate,'yyyymmdd') chkdate, ";
			sql += "		substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "		a.sdate stime, a.edate etime, a.place, a.scheduleid ";
			sql += "	from ( ";
			sql += "		 select to_date(substr(sdate,0,8),'yyyymmdd') sdate1, repeatgubun, repeatterm, ";
			sql += "			fdate, repeatcount, repeatopt1, repeatopt2, repeatopt3, ";
			sql += "			title, sdate, edate, scheduleid, luserid, place ";
			sql += "		 from tu_schedule ";
			sql += "		 where repeatgubun = 'D' and fdate >= '"+v_year+v_month+"01' ";
			sql += "		and luserid = '"+v_userid+"' ";
			sql += "		and sdate<='"+v_year+v_month+"31') A , ";
			sql += "		(select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate ";
			sql += "		 from tu_member ";
			sql += "		 where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual)) B ";
			sql += "	where mod((b.chkdate-a.sdate1),a.repeatterm)=0 ";
			sql += "		and b.chkdate>=a.sdate1 ";
			sql += "		and b.chkdate<a.fdate ";
			
			//개인일정3 주단위 반복있는 일정(특정 요일)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, to_char(a.sdate1,'yyyymmdd') sdate, to_char(b.chkdate,'yyyymmdd') chkdate, ";
			sql += "		substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "		a.sdate stime, a.edate etime, a.place, a.scheduleid ";
			sql += "	from ( ";
			sql += "		  select to_date(substr(sdate,0,8),'yyyymmdd') sdate1, repeatgubun, repeatterm, ";
			sql += "			 to_date(fdate,'yyyymmdd') fdate, repeatcount, repeatopt1, repeatopt2, repeatopt3, ";
			sql += "			 title, sdate, edate, scheduleid, luserid, place ";
			sql += "		  from tu_schedule ";
			sql += "		  where repeatgubun = 'W' and fdate >= '"+v_year+v_month+"01' ";
			sql += "			and sdate<='"+v_year+v_month+"31' and luserid = '"+v_userid+"') A , ";
			sql += "		 (select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate ";
			sql += "		  from tu_member ";
			sql += "		  where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual)) B ";
			sql += "	where  to_char(b.chkdate,'d')=a.repeatopt1 ";
			sql += "		 and b.chkdate>=a.sdate1 ";
			sql += "		 and b.chkdate<=a.fdate  ";
			sql += "		 and mod(b.chkdate-(a.sdate1-to_char(a.sdate1,'d')+a.repeatopt1),repeatterm*7)=0 ";
			
			/*
			//개인일정4 월단위 반복있는 일정(특정 일자)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, substr(a.sdate,0,8) sdate, ";
			sql += "		substr('"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8) chkdate, ";
			sql += "		decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) daynum, ";
			sql += "		a.sdate stime, a.edate etime, a.place, a.scheduleid ";
			sql += "	from ( ";
			sql += "		  select repeatgubun, repeatterm, fdate, repeatcount, repeatopt1, repeatopt2, ";
			sql += "				repeatopt3, ";
			sql += "				title, sdate, edate, scheduleid, luserid, place ";
			sql += "		  from tu_schedule ";
			sql += "		  where repeatgubun = 'M' and repeatopt3=0 and luserid = '"+v_userid+"'";
			sql += "			  and fdate >= '"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) and sdate<='"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2))A ";
			sql += "	where mod(to_char(to_date(substr('"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8),'yyyymmdd'),'mm') ";
			sql += "		 -(to_char(to_date(substr(sdate,0,8),'yyyymmdd'),'mm')),repeatterm)=0 ";
			
			//개인일정5 월단위 반복있는 일정(특정 주 의 특정 요일)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, substr(a.sdate,0,8) sdate, to_char(b.chkdate,'yyyymmdd') chkdate, ";
			sql += "		substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "		a.sdate stime, a.edate etime, a.place, a.scheduleid ";
			sql += "	from ( ";
			sql += "		  select repeatgubun, repeatterm, fdate, repeatcount, repeatopt1, repeatopt2, ";
			sql += "				 repeatopt3, ";
			sql += "				 title, sdate, edate, scheduleid,luserid, place ";
			sql += "		  from tu_schedule ";
			sql += "		  where repeatgubun = 'M' and repeatopt3>0 and luserid = '"+v_userid+"') A , ";
			sql += "		 ( ";
			sql += "		  select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate ";
			sql += "		  from tu_member ";
			sql += "		  where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual) ";
			sql += "		 ) B ";
			sql += "	where mod(to_char(to_date(substr('"+v_year+v_month+"'||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8),'yyyymmdd'),'mm') ";
			sql += "		 -(to_char(to_date(substr(sdate,0,8),'yyyymmdd'),'mm')),repeatterm)=0 ";
			sql += "			and to_char(b.chkdate,'w')=a.repeatopt2 ";
			sql += "			and to_char(b.chkdate,'d')=a.repeatopt3 ";
			
			//개인일정6 년단위 반복있는 일정(특정 일자)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, substr(a.sdate,0,8) sdate1, '"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "		||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) chkdate, ";
			sql += "		decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) daynum, ";
			sql += "		a.sdate stime, a.edate etime, a.place, a.scheduleid ";
			sql += "	from ( ";
			sql += "		  select repeatgubun, repeatterm, fdate, repeatcount, repeatopt1, repeatopt2, ";
			sql += "				repeatopt3,sdate,";
			sql += "				scheduleid, edate, title, luserid, place ";
			sql += "		  from tu_schedule ";
			sql += "		  where repeatgubun = 'Y' and repeatopt3=0 and luserid = '"+v_userid+"'";
			sql += "			 and fdate >= '"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "				||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2) ";
			sql += "		 	 and sdate<='"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "				||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2))A ";
			sql += "	where mod(to_char(to_date(substr('"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "			||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8),'yyyymmdd'),'yyyy') ";
			sql += "			 -(to_char(to_date(substr(sdate,0,8),'yyyymmdd'),'yyyy')),repeatterm)=0 ";
			sql += "			and '"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) = '"+v_year+v_month+"' ";
						
			//개인일정7 년단위 반복있는 일정(특정 주 의 특정 요일)
			sql += "	union all ";
			sql += "	select 6 type, a.scheduleid||'', '', '', '' ,'', luserid, ";
			sql += "		a.title, substr(a.sdate,0,8) sdate1, to_char(b.chkdate,'yyyymmdd') chkdate, ";
			sql += "		substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "		a.sdate stime, a.edate etime, a.place, a.scheduleid ";
			sql += "	from ( ";
			sql += "		 select repeatgubun, repeatterm, fdate, repeatcount, repeatopt1, repeatopt2,";
			sql += "				repeatopt3,sdate,";
			sql += "				title, edate, scheduleid, luserid, place";
			sql += "		 from tu_schedule ";
			sql += "		 where repeatgubun = 'Y' and repeatopt3>0 and luserid = '"+v_userid+"') A , ";
			sql += "		 (";
			sql += "		  select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate";
			sql += "		  from tu_member";
			sql += "		  where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual) ";
			sql += "		 ) B";
			sql += "	where mod(to_char(to_date(substr('"+v_year+"'||decode(sign(repeatopt1-10),-1,'0'||repeatopt1,repeatopt1) ";
			sql += "		||decode(sign(repeatopt2-10),-1,'0'||repeatopt2,repeatopt2),0,8),'yyyymmdd'),'yyyy')";
			sql += "		 -(to_char(to_date(substr(sdate,0,8),'yyyymmdd'),'yyyy')),repeatterm)=0 ";
			sql += "		and to_char(b.chkdate,'mm')=a.repeatopt1 ";
			sql += "		and to_char(b.chkdate,'w')=a.repeatopt2 ";
			sql += "		and to_char(b.chkdate,'d')=a.repeatopt3 ";			*/			
			sql += "	) AA  ";
			sql += "	where AA.ended like '"+v_year+v_month+v_day+"%' ";
			sql += "	order by AA.stime ";

            //----------------------   일별 스케쥴 보기 ----------------------------
            list = db.executeQueryList(box, sql);
            

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;        
    } 


    /** 
    일별 스케쥴 보기(강의관련)
    @param RequestBox box
    @return DataBox
    */
    public ArrayList selectScheduleDaySubj(RequestBox box) throws Exception {
        ListSet ls1         = null;
        DatabaseExecute db = null;
		ArrayList list = null;
        DataBox dbox = null;
        String sql = "";

		Calendar gCal = Calendar.getInstance();
	
		String v_dyear	= Integer.toString(gCal.get(Calendar.YEAR));
		String v_dmonth = (gCal.get(Calendar.MONTH)<9)?"0"+Integer.toString(gCal.get(Calendar.MONTH)+1):Integer.toString(gCal.get(Calendar.MONTH)+1);
		String v_dday	= (gCal.get(Calendar.DATE)<10)?"0"+Integer.toString(gCal.get(Calendar.DATE)):Integer.toString(gCal.get(Calendar.DATE));

        try {
        	
		    String v_userid = box.getSession("userid");

			String v_year	= box.getString("p_year").equals("")?v_dyear:box.getString("p_year");
			String v_month	= box.getString("p_month").equals("")?v_dmonth:box.getString("p_month");
			String v_day	= box.getString("p_day").equals("")?v_dday:box.getString("p_day");
		    
            db = new DatabaseExecute();
            
            String s_language = box.getSession("languageName");
            
            sql =  "	select AA.type, AA.title, AA.name, AA.userid, aa.started, aa.ended, aa.daynum ";
			sql += "	from ( ";
			//토론	1

			sql += "	select 1 type, ts.grcode, ts.subj, ts.year, ts.subjseq, ts.class, ts.userid, ";
			sql += "			ttp.title, ttp.started, ttp.ended, substr(ttp.ended,7,2) daynum, ";
			sql += "			tss." + makeLangSql("subjnm",box) + " ";
			sql += "	from tu_student ts, tu_torontp ttp, tu_subjseq tss ";
			sql += "	where ts.userid='"+v_userid+"' ";
			sql += "		and ts.year="+v_year+" ";
			sql += "		and ts.chkfinal='Y' ";
			sql += "		and ts.grcode = ttp.grcode ";
			sql += "		and ts.subj = ttp.subj ";
			sql += "		and ts.year = ttp.year ";
			sql += "		and ts.subjseq = ttp.subjseq ";
			sql += "		and ts.class = ttp.class ";
			sql += "		and ts.grcode = tss.grcode ";
			sql += "		and ts.subj = tss.subj ";
			sql += "		and ts.year = tss.year ";
			sql += "		and ts.subjseq = tss.subjseq ";
			sql += "		and ts.class = tss.class ";
			sql += "		and ttp.ended like '"+v_year+v_month+v_day+"%' ";
			//sql += "		and (ttp.ended>= '"+v_year+v_month+v_day+"' and ttp.started<= '"+v_year+v_month+v_day+"')";
			sql += "	union all ";
			//리포트	2
			sql += "	select 2 type, ts.grcode, ts.subj, ts.year, ts.subjseq, ts.class, ts.userid, ";
			sql += "			tpd.title, tpd.startdate, tpd.expiredate, substr(tpd.expiredate,7,2) daynum, ";
			sql += "			tss." + makeLangSql("subjnm",box) + " ";
			sql += "	from tu_student ts, tu_projord tpd, tu_subjseq tss ";
			sql += "	where ts.userid='"+v_userid+"' ";
			sql += "		and ts.year="+v_year+" ";
			sql += "		and ts.chkfinal='Y' ";
			sql += "		and ts.grcode = tpd.grcode ";
			sql += "		and ts.subj = tpd.subj ";
			sql += "		and ts.year = tpd.year ";
			sql += "		and ts.subjseq = tpd.subjseq ";
			sql += "		and ts.class = tpd.class ";
			sql += "		and ts.grcode = tss.grcode ";
			sql += "		and ts.subj = tss.subj ";
			sql += "		and ts.year = tss.year ";
			sql += "		and ts.subjseq = tss.subjseq ";
			sql += "		and ts.class = tss.class ";
			sql += "		and tpd.expiredate like '"+v_year+v_month+v_day+"%' ";
			//sql += "		and (tpd.expiredate>= '"+v_year+v_month+v_day+"' and tpd.startdate<= '"+v_year+v_month+v_day+"')";
			
			//온라인시험3(정시)
			sql += "	union all ";
			sql += "	select 3 type, ts.grcode, ts.subj, ts.year, ts.subjseq, ts.class, ts.userid, ";
			sql += "		tep.papernm title, tep.sdate, tep.edate, substr(tep.edate,7,2) daynum, ";
			sql += "			tss." + makeLangSql("subjnm",box) + " ";
			sql += "	from tu_student ts, tu_exampaper tep, tu_subjseq tss ";
			sql += "	where ts.userid='"+v_userid+"' ";
			sql += "		and ts.year="+v_year+" ";
			sql += "		and ts.chkfinal='Y' ";
			sql += "		and ts.subj = tep.subj ";
			sql += "		and ts.year = tep.year ";
			sql += "		and ts.subjseq = tep.subjseq ";
			sql += "		and ts.grcode = tss.grcode ";
			sql += "		and ts.subj = tss.subj ";
			sql += "		and ts.year = tss.year ";
			sql += "		and ts.subjseq = tss.subjseq ";
			sql += "		and ts.class = tss.class ";
			sql += "		and tep.edate like '"+v_year+v_month+v_day+"%' ";
			//sql += "		and (tep.edate>= '"+v_year+v_month+v_day+"' and tep.sdate<= '"+v_year+v_month+v_day+"')";

			//온라인시험4(재시)
			sql += "	union all ";
			sql += "	select 4 type, ts.grcode, ts.subj, ts.year, ts.subjseq, ts.class, ts.userid, ";
			sql += "		tep.papernm title, tep.resdate, tep.reedate, substr(tep.reedate,7,2) daynum, ";
			sql += "			tss." + makeLangSql("subjnm",box) + " ";
			sql += "	from tu_student ts, tu_exampaper tep, tu_subjseq tss ";
			sql += "	where ts.userid='"+v_userid+"' ";
			sql += "		and ts.year="+v_year+" ";
			sql += "		and ts.chkfinal='Y' ";
			sql += "		and ts.subj = tep.subj ";
			sql += "		and ts.year = tep.year ";
			sql += "		and ts.subjseq = tep.subjseq ";
			sql += "		and ts.grcode = tss.grcode ";
			sql += "		and ts.subj = tss.subj ";
			sql += "		and ts.year = tss.year ";
			sql += "		and ts.subjseq = tss.subjseq ";
			sql += "		and ts.class = tss.class ";
			sql += "		and tep.reedate like '"+v_year+v_month+v_day+"%' ";
			//sql += "		and (tep.reedate>= '"+v_year+v_month+v_day+"' and tep.reedate<= '"+v_year+v_month+v_day+"')";
					
			sql += "	) AA  ";
			sql += "	order by AA.ended ";
			//System.out.println("schdule+++++++++++++++++++++++++++++++++++++++++++++sql++++++"+sql);
			
            //----------------------   일별 스케쥴 보기 ----------------------------
            list = db.executeQueryList(box, sql);
            
            
            /*
            for(int i=0; i<list.size(); i++){
            dbox = (DataBox)list.get(i);
			System.out.println("sdfdsfsdfdsf===>>"+dbox.getString("d_title"));
			System.out.println("sdfdsfsdfdsf===>>"+dbox.getString("d_type"));
			}
			*/

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
       
        return list;        
    } 

    /** 
    일별 스케쥴 보기(상담관련)
    @param RequestBox box
    @return DataBox
    */
    public ArrayList selectScheduleDayCounsel(RequestBox box) throws Exception {
        DatabaseExecute db = null;
		ArrayList list = null;
        DataBox dbox = null;
        String sql = "";

		Calendar gCal = Calendar.getInstance();
	
		String v_dyear	= Integer.toString(gCal.get(Calendar.YEAR));
		String v_dmonth = (gCal.get(Calendar.MONTH)<9)?"0"+Integer.toString(gCal.get(Calendar.MONTH)+1):Integer.toString(gCal.get(Calendar.MONTH)+1);
		String v_dday	= (gCal.get(Calendar.DATE)<10)?"0"+Integer.toString(gCal.get(Calendar.DATE)):Integer.toString(gCal.get(Calendar.DATE));

        try {
        	
		    String v_userid = box.getSession("userid");

			String v_year	= box.getString("p_year").equals("")?v_dyear:box.getString("p_year");
			String v_month	= box.getString("p_month").equals("")?v_dmonth:box.getString("p_month");
			String v_day	= box.getString("p_day").equals("")?v_dday:box.getString("p_day");
		    
            db = new DatabaseExecute();

            String s_language = box.getSession("languageName");

			//상담		8

			sql =  "	select tcc.bookdate, tcc.reqtitle, tcc.reqbirth_date, ";
			sql += "			tm." + makeLangSql("NAME",box) + " , get_hakgwa(tm.comp , '"+s_language+"') hak1  ";
			sql += "	from tu_counsel_content tcc, tu_member tm ";
			sql += "	where (tcc.reqbirth_date='"+v_userid+"' ";
			sql += "		or tcc.reqprofid='"+v_userid+"') ";
			sql += "		and tcc.bookdate like '"+v_year+v_month+v_day+"%' ";					
			sql += "		and tcc.reqbirth_date = tm.userid ";					
			sql += "	order by tcc.bookdate ";
            
            //----------------------   일별 스케쥴 보기 ----------------------------
            list = db.executeQueryList(box, sql);
			

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;        
    } 

    /** 
    일별 스케쥴 보기(도서대출)
    @param RequestBox box
    @return DataBox
    */
    public ArrayList selectScheduleDayBook(RequestBox box) throws Exception {
    	DBConnectionManager connMgr = null;
    	ListSet ls1 = null;
        DatabaseExecute db = null;
		ArrayList list = null;
        DataBox dbox = null;
        String sql = "";

		Calendar gCal = Calendar.getInstance();
	
		String v_dyear	= Integer.toString(gCal.get(Calendar.YEAR));
		String v_dmonth = (gCal.get(Calendar.MONTH)<9)?"0"+Integer.toString(gCal.get(Calendar.MONTH)+1):Integer.toString(gCal.get(Calendar.MONTH)+1);
		String v_dday	= (gCal.get(Calendar.DATE)<10)?"0"+Integer.toString(gCal.get(Calendar.DATE)):Integer.toString(gCal.get(Calendar.DATE));

        try {
        	
		    String v_userid = box.getSession("userid");

			String v_year	= box.getString("p_year").equals("")?v_dyear:box.getString("p_year");
			String v_month	= box.getString("p_month").equals("")?v_dmonth:box.getString("p_month");
			String v_day	= box.getString("p_day").equals("")?v_dday:box.getString("p_day");
		    
            //db = new DatabaseExecute();
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            String s_language = box.getSession("languageName");
            
			//도서대출  7            
			sql  = "	select convert(UTL_RAW.CAST_TO_VARCHAR2(title),'utf8','KO16MSWIN949') title, ";
			sql += "			loan_date, return_plan_date  ";
			sql += "	from vw_sungbook@sung_book ";
			sql += "	where user_id=lpad('"+v_userid+"',15,'*') ";
			sql += "		and return_plan_date like '"+v_year+v_month+v_day+"%' ";					
            
            //----------------------   일별 스케쥴 보기 ----------------------------
            //list = db.executeQueryList(box, sql);
            ls1 = connMgr.executeQuery(sql);
 
            while (ls1.next() ) {
                dbox = ls1.getDataBox();
				list.add(dbox);
			} 
			

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
        	connMgr.commit();
            if(ls1 != null) { try { ls1.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;        
    } 

    /** 
    일별 스케쥴 보기(수업시간표)
    @param RequestBox box
    @return DataBox
    */
    public ArrayList selectScheduleDayClass(RequestBox box) throws Exception {
        DatabaseExecute db = null;
		ArrayList list = null;
        DataBox dbox = null;
        String sql = "";

		Calendar gCal = Calendar.getInstance();
	
		String v_dyear	= Integer.toString(gCal.get(Calendar.YEAR));
		String v_dmonth = (gCal.get(Calendar.MONTH)<9)?"0"+Integer.toString(gCal.get(Calendar.MONTH)+1):Integer.toString(gCal.get(Calendar.MONTH)+1);
		String v_dday	= (gCal.get(Calendar.DATE)<10)?"0"+Integer.toString(gCal.get(Calendar.DATE)):Integer.toString(gCal.get(Calendar.DATE));

        try {
        	
		    String v_userid = box.getSession("userid");

			String v_year	= box.getString("p_year").equals("")?v_dyear:box.getString("p_year");
			String v_month	= box.getString("p_month").equals("")?v_dmonth:box.getString("p_month");
			String v_day	= box.getString("p_day").equals("")?v_dday:box.getString("p_day");
		    
            db = new DatabaseExecute();

            String s_language = box.getSession("languageName");
            
            sql =  "	select AA.* ";
			sql += "	from ( ";
			//토론	1


			sql += "	select 9 type, ";//'', '', '', '', '', '',  ";
			sql += "		a.name title,to_char(b.chkdate,'yyyymmdd') sdate,to_char(b.chkdate,'yyyymmdd') edate, substr(to_char(b.chkdate,'yyyymmdd'),7,2) daynum, ";
			sql += "			a.stime||'' stime, a.etime||'' etime, '' place, 0 scheduleid ";
			sql += "	from (select st.grcode, st.year, st.subjseq, st.gubun, vhs.subj, vhs.class, ";
			sql += "		  st.sdate, st.edate, vhs.weekday, vhs.stime, vhs.etime, ft.name ";
			sql += "		  from ( ";
			sql += "			 select tsj.grcode, tsj.subj, tsj.year, tsj.subjseq, tsj.class, tsj." + makeLangSql("subjnm",box) + ", tm.mtype ";
			sql += "			 from tu_student ts, tu_subjseq tsj, tu_member tm ";
			sql += "			 where ts.grcode='N000001' ";
			sql += "			  and ts.year = '"+v_year+"' ";
			sql += "			  and ts.userid='"+v_userid+"' ";
			sql += "			  and ts.grcode = tsj.grcode ";
			sql += "			  and ts.subj = tsj.subj ";
			sql += "			  and ts.year = tsj.year ";
			sql += "			  and ts.subjseq = tsj.subjseq ";
			sql += "			  and ts.class = tsj.class ";
			sql += "			  and ts.userid = tm.userid) FT, ";
			sql += "				( ";
			sql += "			    select tgs.grcode, tgs.year, tgs.subjseq, tgs.gubun, tgs.scheduleid, tgs.sdate, tgs.edate ";
			sql += "			    from (select grcode, year, subjseq, gubun, max(scheduleid) scheduleid ";
			sql += "					  from tu_gr_schedule ";
			sql += "					  where schedulegubun = 1 ";
			sql += "					  group by grcode, year, subjseq, gubun)A, tu_gr_schedule tgs ";
			sql += "			    where A.grcode = tgs.grcode ";
			sql += "				   and A.year = tgs.year ";
			sql += "				   and A.subjseq = tgs.subjseq ";
			sql += "				   and A.gubun = tgs.gubun ";
			//sql += "				   and A.scheduleid = tgs.scheduleid) ST, VW_HAKSA_SUBJTIME VHS ";
			sql += "				   and A.scheduleid = tgs.scheduleid) ST, TU_SUBJTIME VHS ";
			sql += "		  where st.grcode = vhs.grcode ";
			sql += "			  and st.year = vhs.year ";
			sql += "			  and st.subjseq = vhs.subjseq ";

			sql += "				and ft.grcode = st.grcode ";
			sql += "				and ft.year = st.year ";
			sql += "				and ft.subjseq = st.subjseq ";
			sql += "				and substr(ft.mtype,0,1) = st.gubun ";
			sql += "				and ft.grcode = vhs.grcode ";
			sql += "				and ft.subj = vhs.subj ";
			sql += "				and ft.year = vhs.year ";
			sql += "				and ft.subjseq = vhs.subjseq ";
			sql += "				and ft.class = vhs.class ";
		
			sql += "			  )A, ";
			sql += "		 (select to_date('"+v_year+v_month+"'||decode(sign(rownum-10),-1,'0'||rownum,rownum)) chkdate ";
			sql += "		  from tu_member ";
			sql += "		  where rownum<=(select to_number(to_char(last_day('"+v_year+v_month+"01'),'dd')) from dual) ";
			sql += "		  ) B ";
			sql += "	where A.sdate<=to_char(chkdate,'yyyy-mm-dd') ";
			sql += "	 and A.edate>=to_char(chkdate,'yyyy-mm-dd') ";
			sql += "	 and a.weekday = decode(to_char(chkdate,'d'),1,'일',2,'월',3,'화',4,'수',5,'목',6,'금',7,'토') ";
			sql += "	 and to_char(chkdate,'yyyymmdd') = '"+v_year+v_month+v_day+"' ";

					
			sql += "	) AA  ";
			sql += "	order by aa.stime  ";

            
            //----------------------   일별 스케쥴 보기 ----------------------------
            list = db.executeQueryList(box, sql);
			

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;        
    } 


    /** 
    일정 상세 보기
    @param RequestBox box
    @return DataBox
    */
    public DataBox selectDetailView(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        DataBox dbox = null;
        String sql = "";

        try {
        	
		    String v_userid		= box.getSession("userid");
			int v_scheduleid	= box.getInt("p_scheduleid");
		    
            db = new DatabaseExecute();

            String s_language = box.getSession("languageName");
            					
			//일정
			sql =  "	select * ";
			sql += "	from tu_schedule ";
			sql += "	where scheduleid = "+v_scheduleid+" ";
			sql += "		and userid = '"+v_userid+"' ";
			
			
            //----------------------   스케쥴 보기 ----------------------------
            dbox = db.executeQuery(box, sql);
			

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return dbox;        
    } 

    /**
    * 개인 스케쥴 등록
    */
     public boolean insertSchedule(RequestBox box) throws Exception {
        DatabaseExecute db = null;
		DataBox dbox = null;
		DataBox dbox1 = null;
        boolean isCommit = false;           
		String sql = "008";

        try {        
            db = new DatabaseExecute();
			String s_userid = box.getSession("userid");

			String v_title = box.getString("p_title");
			String v_sdate = box.getString("p_date1");
			String v_edate = box.getString("p_date2");
			String v_fdate = box.getString("p_fdate");
			String v_stime = box.getString("p_shour")+box.getString("p_smin");
			String v_etime = box.getString("p_ehour")+box.getString("p_emin");
			String v_fulldayyn = box.getString("p_fulldayyn");			
			String v_repeatgubun = box.getString("p_repeatgubun");
			
			String v_repeatyn = "";
			String v_content = box.getString("p_content");
			String v_weightgubun = box.getString("p_weightgubun");
			String v_place	= box.getString("p_place");
			String v_inputplace	= box.getString("p_inputplace");
			
			v_place = v_inputplace;

			int v_repeatterm = box.getInt("p_repeatterm");
			int v_repeatcount= box.getInt("p_repeatcount");
			int v_opt1		 = box.getInt("p_opt1");
			int v_opt2		 = box.getInt("p_opt2");
			int v_opt3		 = box.getInt("p_opt3");

			int numz = 0;	//횟수가 선택된 경우...

			v_sdate = v_sdate.replaceAll("-","");
			v_edate = v_edate.replaceAll("-","");

			if (v_fulldayyn.equals("Y"))
			{
				v_stime = "0000";
				v_etime = "0000";
			}

			if (v_repeatgubun.equals("N"))
			{
				v_repeatyn = "N";
			} else {
				v_repeatyn = "Y";
				if (v_repeatgubun.equals("D"))
				{
					numz = v_repeatterm * v_repeatcount;
				} else if (v_repeatgubun.equals("W"))
				{
					numz = v_repeatterm * v_repeatcount * 7;
				} else if (v_repeatgubun.equals("M"))
				{
					numz = v_repeatterm * v_repeatcount;
				} else if (v_repeatgubun.equals("Y"))
				{
					numz = v_repeatterm * v_repeatcount * 12;
				}
				//----------------------   회수인 경우 마감일자를 가져온다. ----------------------------
				if (numz>0)
				{
					sql =  " SELECT TO_CHAR(TO_DATE('"+v_sdate+"','yyyymmdd')+"+numz+",'yyyymmdd') dayz, ";
					sql += "	TO_CHAR(ADD_MONTHS(TO_DATE('"+v_sdate+"','yyyymmdd'),"+numz+"),'yyyymmdd') monthz ";
					sql += " FROM DUAL";

	               dbox = db.executeQuery(box, sql);      
				   if (v_repeatgubun.equals("D") || v_repeatgubun.equals("W"))
				   {
					   v_fdate = dbox.getString("d_dayz");
				   } else {
					   v_fdate = dbox.getString("d_monthz");
				   }
				}
			}



               //----------------------   일련 번호 가져온다 ----------------------------
               sql = " SELECT NVL(MAX(scheduleid), 0)+1 scheduleid FROM TU_SCHEDULE WHERE userid = '"+s_userid+"' ";
     
               dbox1 = db.executeQuery(box, sql);      
               int v_scheduleid = dbox1.getInt("d_scheduleid");


			sql =  " INSERT INTO TU_SCHEDULE (USERID, SCHEDULEID, SDATE, EDATE, TITLE, CONTENTS, FULLDAYYN, ";
			sql += "	REPEATYN, REPEATGUBUN, LUSERID, LDATE, WEIGHTGUBUN, PLACE, REPEATTERM, ";
			sql += "	FDATE, REPEATCOUNT, REPEATOPT1, REPEATOPT2, REPEATOPT3) ";
			sql += " VALUES (?, ?, ?, ?, ?, EMPTY_CLOB(), ?, ?, ?, ?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedBox pbox = new PreparedBox("preparedbox");
            pbox.setString(1, s_userid);
            pbox.setInt(2, v_scheduleid);
            pbox.setString(3, v_sdate+v_stime);
			pbox.setString(4, v_edate+v_etime);
            pbox.setString(5, v_title);

			pbox.setString(6, v_fulldayyn);
			pbox.setString(7, v_repeatyn);
			pbox.setString(8, v_repeatgubun);
			pbox.setString(9, s_userid);
			pbox.setString(10, v_weightgubun);
			pbox.setString(11, v_place);
			pbox.setInt(12, v_repeatterm);

			pbox.setString(13, v_fdate);
			pbox.setInt(14, v_repeatcount);
			pbox.setInt(15, v_opt1);
			pbox.setInt(16, v_opt2);
			pbox.setInt(17, v_opt3);


              //-----------먼저 해당 content2 에 empty_clob()을 적용하고 나서 값을 스트림으로 치환한다.  
            pbox.setClob("select CONTENTS FROM TU_SCHEDULE WHERE SCHEDULEID = "+ v_scheduleid , v_content); 


             //CLOB 단일 테이블 입력 시 Transaction 처리 가능하게 하는 부분 선언
             String tempsql = "";
             PreparedBox temppbox = new PreparedBox("preparedbox");
             tempsql = UpdateSpareTable.setSpareSql();
             UpdateSpareTable.setSparePbox(temppbox);
			          
               isCommit = db.executeUpdate(new PreparedBox [] {pbox, temppbox},  new String [] {sql, tempsql});
        }
        catch(Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());            
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return isCommit;
    }

    /**
    * 개인 스케쥴 수정
    */
     public boolean updateSchedule(RequestBox box) throws Exception {
        DatabaseExecute db = null;
		DataBox dbox = null;
		DataBox dbox1 = null;
        boolean isCommit = false;           
		String sql = "008";

        try {        
            db = new DatabaseExecute(box);
			String s_userid = box.getSession("userid");

			String v_title = box.getString("p_title");
			String v_sdate = box.getString("p_date1");
			String v_edate = box.getString("p_date2");
			String v_fdate = box.getString("p_fdate");
			String v_stime = box.getString("p_shour")+box.getString("p_smin");
			String v_etime = box.getString("p_ehour")+box.getString("p_emin");
			String v_fulldayyn = box.getString("p_fulldayyn");			
			String v_repeatgubun = box.getString("p_repeatgubun");
			
			String v_repeatyn = "";
			String v_content = box.getString("p_content");
			String v_weightgubun = box.getString("p_weightgubun");
			String v_place	= box.getString("p_place");
			
			String v_inputplace	= box.getString("p_inputplace");
			
			v_place = v_inputplace;

			int v_repeatterm = box.getInt("p_repeatterm");
			int v_repeatcount= box.getInt("p_repeatcount");
			int v_opt1		 = box.getInt("p_opt1");
			int v_opt2		 = box.getInt("p_opt2");
			int v_opt3		 = box.getInt("p_opt3");
			int v_scheduleid = box.getInt("p_scheduleid");

			int numz = 0;	//횟수가 선택된 경우...

			v_sdate = v_sdate.replaceAll("-","");
			v_edate = v_edate.replaceAll("-","");

			if (v_fulldayyn.equals("Y"))
			{
				v_stime = "0000";
				v_etime = "0000";
			}

			if (v_repeatgubun.equals("N"))
			{
				v_repeatyn = "N";
			} else {
				v_repeatyn = "Y";
				if (v_repeatgubun.equals("D"))
				{
					numz = v_repeatterm * v_repeatcount;
				} else if (v_repeatgubun.equals("W"))
				{
					numz = v_repeatterm * v_repeatcount * 7;
				} else if (v_repeatgubun.equals("M"))
				{
					numz = v_repeatterm * v_repeatcount;
				} else if (v_repeatgubun.equals("Y"))
				{
					numz = v_repeatterm * v_repeatcount * 12;
				}
				//----------------------   회수인 경우 마감일자를 가져온다. ----------------------------
				if (numz>0)
				{
					sql =  " SELECT TO_CHAR(TO_DATE('"+v_sdate+"','yyyymmdd')+"+numz+",'yyyymmdd') dayz, ";
					sql += "	TO_CHAR(ADD_MONTHS(TO_DATE('"+v_sdate+"','yyyymmdd'),"+numz+"),'yyyymmdd') monthz ";
					sql += " FROM DUAL";

	               dbox = db.executeQuery(box, sql);      
				   if (v_repeatgubun.equals("D") || v_repeatgubun.equals("W"))
				   {
					   v_fdate = dbox.getString("d_dayz");
				   } else {
					   v_fdate = dbox.getString("d_monthz");
				   }
				}
			}


			sql =  " UPDATE TU_SCHEDULE ";
			sql += "	SET  ";
			sql += "	SDATE = ?, ";
			sql += "	EDATE= ?, ";
			sql += "	TITLE= ?, ";
			sql += "	CONTENTS= EMPTY_CLOB(), ";
			sql += "	FULLDAYYN= ?, ";
			sql += "	REPEATYN= ?, ";

			sql += "	REPEATGUBUN = ?, ";
			sql += "	LUSERID= ?, ";
			sql += "	LDATE= TO_CHAR(sysdate, 'YYYYMMDDHH24MISS'), ";
			sql += "	WEIGHTGUBUN= ?, ";
			sql += "	PLACE= ?, ";
			sql += "	REPEATTERM= ?, ";

			sql += "	FDATE= ?, ";
			sql += "	REPEATCOUNT= ?, ";
			sql += "	REPEATOPT1= ?, ";
			sql += "	REPEATOPT2= ?, ";
			sql += "	REPEATOPT3= ? ";

			sql += " WHERE userid = ? and scheduleid = ? ";
			

			PreparedBox pbox = new PreparedBox("preparedbox");

            pbox.setString(1, v_sdate+v_stime);
			pbox.setString(2, v_edate+v_etime);
            pbox.setString(3, v_title);
			pbox.setString(4, v_fulldayyn);
			pbox.setString(5, v_repeatyn);

			pbox.setString(6, v_repeatgubun);
			pbox.setString(7, s_userid);
			pbox.setString(8, v_weightgubun);
			pbox.setString(9, v_place);
			pbox.setInt(10, v_repeatterm);

			pbox.setString(11, v_fdate);
			pbox.setInt(12, v_repeatcount);
			pbox.setInt(13, v_opt1);
			pbox.setInt(14, v_opt2);
			pbox.setInt(15, v_opt3);

            pbox.setString(16, s_userid);
            pbox.setInt(17, v_scheduleid);


              //-----------먼저 해당 content2 에 empty_clob()을 적용하고 나서 값을 스트림으로 치환한다.  
            pbox.setClob("select CONTENTS FROM TU_SCHEDULE WHERE SCHEDULEID = "+ v_scheduleid , v_content); 


             //CLOB 단일 테이블 입력 시 Transaction 처리 가능하게 하는 부분 선언
             String tempsql = "";
             PreparedBox temppbox = new PreparedBox("preparedbox");
             tempsql = UpdateSpareTable.setSpareSql();
             UpdateSpareTable.setSparePbox(temppbox);
			          
               isCommit = db.executeUpdate(new PreparedBox [] {pbox, temppbox},  new String [] {sql, tempsql});
        }
        catch(Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());            
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return isCommit;
    }


    /**
    * 개인 스케쥴 삭제
    */
     public boolean deleteSchedule(RequestBox box) throws Exception {
        DatabaseExecute db = null;
		DataBox dbox = null;
		DataBox dbox1 = null;
        boolean isCommit = false;           
		String sql = "008";

        try {        
            db = new DatabaseExecute(box);
			String s_userid = box.getSession("userid");
			int v_scheduleid = box.getInt("p_scheduleid");

			sql =  " DELETE FROM tu_schedule ";
			sql += " WHERE userid = ? and scheduleid = ? ";			

			PreparedBox pbox = new PreparedBox("preparedbox");

            pbox.setString(1, s_userid);
            pbox.setInt(2, v_scheduleid);

			isCommit = db.executeUpdate(new PreparedBox [] {pbox},  new String [] {sql});

        }
        catch(Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());            
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return isCommit;
	 }

    /**
    * excel 등록
    */
     public boolean insertExcelToDB(RequestBox box) throws Exception {
        DatabaseExecute db = null;
		DataBox dbox = null;
		DataBox dbox1 = null;
        boolean isCommit = false;           
		String sql = "";

	    Cell cell = null;
	    Sheet sheet = null;
	    Workbook workbook = null;

        try {      

            db = new DatabaseExecute();
			String s_userid = box.getSession("userid");

            String tempsql = "";

			String v_title = "";
			String v_sdate = "";
			String v_edate = "";
			String v_fdate = "";
			String v_stime = "";
			String v_etime = "";
			String v_fulldayyn = "";			
			String v_repeatgubun = "";
			
			String v_repeatyn = "";
			String v_content = "";
			String v_weightgubun = "";
			String v_place	= "";

			int v_repeatterm = 0;
			int v_repeatcount= 0;
			int v_opt1		 = 0;
			int v_opt2		 = 0;
			int v_opt3		 = 0;
			int v_scheduleid = 0;

			int numz = 0;	//횟수가 선택된 경우...


	           //----------------------   업로드되는 파일 -------------------------------- 
               String realFileName = box.getRealFileName("p_file");
               String newFileName = box.getNewFileName("p_file");
			   

				workbook = Workbook.getWorkbook(new File(dirUploadDefault+"/"+newFileName));
		        sheet = workbook.getSheet(0);

				//System.out.println(sql);

				for (int i=1; i < sheet.getRows() ; i++ ) {
					if (sheet.getCell(0,i).getContents().equals("제목"))
					{
						continue;
					}
					v_sdate			= sheet.getCell(1,i).getContents();  // 시작일자
		            v_stime			= sheet.getCell(2,i).getContents();  // 시작시분
					v_edate			= sheet.getCell(3,i).getContents();  // 마감일자
		            v_etime			= sheet.getCell(4,i).getContents();  // 마감시분
					v_title			= sheet.getCell(0,i).getContents();  // 제목
		            v_weightgubun	= sheet.getCell(19,i).getContents();  // 중요도
					v_content		= sheet.getCell(16,i).getContents();  // 내용
		            v_place			= sheet.getCell(21,i).getContents();  // 장소
					v_fulldayyn		= sheet.getCell(5,i).getContents();  // 하루종일여부
		            v_repeatyn		= "N";  // 반복여부(반복없음)

					v_repeatgubun	= "N";  // 반복구분자
		            v_repeatterm	= 0;  // 반복주기
					v_repeatcount	= 0;  // 반복수
		            v_fdate			= "";  // 반복마감일

					if (v_weightgubun.equals("높음"))
					{
						v_weightgubun = "H";
					} else if (v_weightgubun.equals("낮음"))
					{
						v_weightgubun = "L";
					} else {
						v_weightgubun = "M";
					}

					if (v_fulldayyn.equals("FALSE"))
					{
						v_fulldayyn = "N";
					} else {
						v_fulldayyn = "Y";
					}



					System.out.println(v_sdate);
					System.out.println(v_stime);
					System.out.println(v_title);
					System.out.println(v_weightgubun);
					System.out.println(v_content);
					System.out.println(v_place);
					System.out.println(sql);
					System.out.println(sql);
					System.out.println(sql);
					System.out.println(sql);


					/*

					v_sdate = v_sdate.replaceAll("-","");
					v_edate = v_edate.replaceAll("-","");
					v_fdate = v_fdate.replaceAll("-","");
					v_stime = v_stime.replaceAll(":","");
					v_etime = v_etime.replaceAll(":","");


					if (v_fulldayyn.equals("Y"))
					{
						v_stime = "0000";
						v_etime = "0000";
					}

					if (v_repeatgubun.equals("N")) {
						v_repeatyn = "N";

					} else {

						v_repeatyn = "Y";

						if (v_repeatgubun.equals("D"))	{
							numz = v_repeatterm * v_repeatcount;

						} else if (v_repeatgubun.equals("W")) {
							numz = v_repeatterm * v_repeatcount * 7;

						} else if (v_repeatgubun.equals("M")) {
							numz = v_repeatterm * v_repeatcount;

						} else if (v_repeatgubun.equals("Y")) {
							numz = v_repeatterm * v_repeatcount * 12;

						}
						//----------------------   회수인 경우 마감일자를 가져온다. ----------------------------
						if (numz>0)	{
							sql =  " SELECT TO_CHAR(TO_DATE('"+v_sdate+"','yyyymmdd')+"+numz+",'yyyymmdd') dayz, ";
							sql += "	TO_CHAR(ADD_MONTHS(TO_DATE('"+v_sdate+"','yyyymmdd'),"+numz+"),'yyyymmdd') monthz ";
							sql += " FROM DUAL";

				           dbox = db.executeQuery(box, sql);      
						   if (v_repeatgubun.equals("D") || v_repeatgubun.equals("W")) {
							   v_fdate = dbox.getString("d_dayz");

						   } else {
							   v_fdate = dbox.getString("d_monthz");

						   }
						}
					}
					*/
				   //----------------------   일련 번호 가져온다 ----------------------------
	               sql = " SELECT NVL(MAX(scheduleid), 0)+1 scheduleid FROM TU_SCHEDULE WHERE userid = '"+s_userid+"' ";		     
	               dbox1 = db.executeQuery(box, sql);      
	               v_scheduleid = dbox1.getInt("d_scheduleid");

					sql =  " INSERT INTO TU_SCHEDULE (USERID, SCHEDULEID, SDATE, EDATE, TITLE, CONTENTS, FULLDAYYN, ";
					sql += "	REPEATYN, REPEATGUBUN, LUSERID, LDATE, WEIGHTGUBUN, PLACE, REPEATTERM, ";
					sql += "	FDATE, REPEATCOUNT, REPEATOPT1, REPEATOPT2, REPEATOPT3) ";
					sql += " VALUES (?, ?, to_char(to_date('"+v_sdate+"','yyyy-mm-dd'),'yyyymmdd')||to_char(to_date('"+v_stime+"','PM hh:mi:ss'),'hh24miss'), ";
					sql += "	to_char(to_date('"+v_edate+"','yyyy-mm-dd'),'yyyymmdd')||to_char(to_date('"+v_etime+"','PM hh:mi:ss'),'hh24miss'), ";
					sql += "	?, EMPTY_CLOB(), ?, ?, ?, ?, TO_CHAR(sysdate, 'YYYYMMDDHH24MISS'), ";
					sql += "	?, ?, ?, ?, ?, ?, ?, ?)";

					PreparedBox pbox = new PreparedBox("preparedbox");
		            pbox.setString(1, s_userid);
		            pbox.setInt(2, v_scheduleid);
					/*
		            pbox.setString(3, v_sdate);
					pbox.setString(4, v_stime);
					pbox.setString(5, v_edate);
					pbox.setString(6, v_etime);
		            */
					pbox.setString(3, v_title);

					pbox.setString(4, v_fulldayyn);
					pbox.setString(5, v_repeatyn);
					pbox.setString(6, v_repeatgubun);
					pbox.setString(7, s_userid);
					pbox.setString(8, v_weightgubun);
					pbox.setString(9, v_place);
					pbox.setInt(10, v_repeatterm);

					pbox.setString(11, v_fdate);
					pbox.setInt(12, v_repeatcount);
					pbox.setInt(13, v_opt1);
					pbox.setInt(14, v_opt2);
					pbox.setInt(15, v_opt3);


				      //-----------먼저 해당 content2 에 empty_clob()을 적용하고 나서 값을 스트림으로 치환한다.  
		            pbox.setClob("select CONTENTS FROM TU_SCHEDULE WHERE SCHEDULEID = "+ v_scheduleid , v_content); 


		             //CLOB 단일 테이블 입력 시 Transaction 처리 가능하게 하는 부분 선언
		
		             PreparedBox temppbox = new PreparedBox("preparedbox");
		             tempsql = UpdateSpareTable.setSpareSql();
		             UpdateSpareTable.setSparePbox(temppbox);
			          
		             isCommit = db.executeUpdate(new PreparedBox [] {pbox, temppbox},  new String [] {sql, tempsql});

				}

					//FileManager.deleteFile(dirUploadDefault+newFileName, "accept");
					
	      			File del = new File(dirUploadDefault+newFileName);
	      			
	      			if (del.exists()) {
	      				del.delete(); 
	      			}
					
					System.out.println(sql);


               //isCommit = true;
        }
        catch(Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());            
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return isCommit;
    }



















    /**
    * 회원정보 변경
    */
     public boolean update(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        boolean isCommit = false;           
		String sql = "";
		
        
        try {        
            db = new DatabaseExecute();
			String s_userid = box.getSession("userid");

			sql =  " UPDATE tu_member ";
			sql += " SET email	 = ? ";
			sql += "	,mobile	 = ? ";
			sql += "	,tel	 = ?  ";
			sql += " WHERE userid = ? ";

			PreparedBox pbox = new PreparedBox("preparedbox");
            pbox.setString(1, box.getString("p_email"));
            pbox.setString(2, box.getString("p_mobile"));
            pbox.setString(3, box.getString("p_tel"));
            pbox.setString(4, s_userid);
            
            isCommit = db.executeUpdate(pbox, sql);

        }
        catch(Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());            
            throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
        }
        return isCommit;
    }
    
    
    
    /** 
    일별 스케쥴 보기(강의관련)
    @param RequestBox box
    @return DataBox
    */
    public ArrayList selectPlace(RequestBox box) throws Exception {
        DatabaseExecute db = null;
		ArrayList list = null;
        DataBox dbox = null;
        String sql = "";

        try {
        	
		    String v_userid = box.getSession("userid");

            db = new DatabaseExecute();

			sql =" SELECT ";
            sql+="  distinct trim(PLACE) place";
            sql+=" FROM                        ";
            sql+="  TU_SCHEDULE               ";
            sql+=" WHERE                       ";
            sql+="  USERID='"+v_userid+"'     ";
            sql+="  AND PLACE IS NOT NULL     ";
            
            list = db.executeQueryList(box, sql);

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;        
    } 


}
