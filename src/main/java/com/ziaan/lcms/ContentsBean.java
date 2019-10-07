/*
 * @(#)ContentsBean.java	1.0 2008. 11. 03
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */

package com.ziaan.lcms;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ziaan.lcms.MasterFormListData;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * 컨텐츠관리 DAO Bean
 *
 * @version		1.0, 2008/11/03
 * @author		Chung Jin-pil
 */
public class ContentsBean
{
	private Logger logger = Logger.getLogger(this.getClass());

	public ContentsBean()
	{
	}

	/**
	 * 컨텐츠 리스트
	 * 
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List selectContentsList(RequestBox box) throws Exception
	{
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		
		String sql = "";
		List list = null;
		
		MasterFormListData data = null;

		String ss_gadmin = box.getSession("gadmin");
		String ss_userid = box.getSession("userid");
		String v_testGubun = box.getString("p_testgubun");

		// 검색 조건
		String s_upperclass = box.getString("s_upperclass");
		String s_middleclass = box.getString("s_middleclass");
		String s_lowerclass = box.getString("s_lowerclass");
		String s_contenttype = box.getString("s_contenttype");
		String s_subjnm = box.getString("s_subjnm");
		String s_subj = box.getString("s_subj");
		
		String p_orderColumn = box.getString("p_orderColumn");
		String p_orderType = box.getString("p_orderType");

		try
		{
			connMgr = new DBConnectionManager();
			list = new ArrayList();

			// 베타 테스터 업체 여부
			/*
    		String betaProducer = "";
		    boolean isBetaTest = false;

			if ((s_gadmin.equals("S1") || s_gadmin.equals("T1") || s_gadmin.equals("M1")) && v_testGubun.equals("beta"))
			{
				isBetaTest = true;

				String betaSql = "\n  SELECT cpseq, cpnm         " + "\n  FROM tz_cpinfo             " + "\n  WHERE userid = ':userid'   " + "\n  ORDER BY cpnm              ";

				betaSql = betaSql.replaceAll(":userid", s_userid);
				ls = connMgr.executeQuery(betaSql);

				if (ls.next())
				{
					betaProducer = ls.getString("cpseq");
				}
				ls.close();
			}

			sql = "select subj, subjnm, iscentered, dir, isuse, contenttype, " 
				    + "       mftype, width, height, eduprocess, otbgcolor, isvisible,      "
					+ "       (select count(subj) from tz_subjlesson where subj=a.subj) cnt_lesson " // ,aescontentid
					+ "  from tz_subj a ";

			if (isBetaTest)
			{
				sql += ", tz_cpinfo b where 1 = 1 and a.producer = b.cpseq " + "AND a.producer = " + SQLString.Format(betaProducer);
			}
			else
			{
				sql += " where 1=1 ";
			}

			sql += " AND a.isonoff='ON' ";
			*/
			
			// CP 권한이면, 해당 컨텐츠만 보임
			boolean isCPEduAuth = box.getSession("gadmin").startsWith("M");

			// 컨텐츠 리스트 sql
			sql =
				"\n  SELECT subj, subjnm, contenttype, iscentered, dir, isuse, mftype," +
				"\n         isvisible, get_codenm('0007', contenttype) contenttypenm,  " +
				"\n         (SELECT COUNT (subj) FROM tz_subjlesson WHERE subj = tz_subj.subj) countLesson,  " +
				"\n  		 CASE WHEN contenttype = 'O' OR contenttype = 'OA' THEN  " +
				"\n  		 (  " +
				"\n  		    SELECT COUNT(OID) countSco   " +
				"\n              FROM tz_subjobj a  " +
				"\n              WHERE a.subj = tz_subj.subj  " +
				"\n           )  " +
				"\n  		 WHEN contenttype = 'S' THEN  " +
				"\n  		 (  " +
				"\n  			SELECT COUNT (*) countSco                                " +
				"\n  			FROM tys_item a, tys_resource b, tz_subj_contents c   " +
				"\n  			WHERE 1 = 1                                              " +
				"\n  			   AND a.course_code = b.course_code                     " +
				"\n  			   AND b.course_code = c.course_code                     " +
				"\n  			   AND a.org_id = b.org_id                               " +
				"\n  			   AND b.org_id = c.org_id                             " +
				"\n  			   AND a.item_id = b.item_id                          " +
				"\n  			   AND b.res_scorm_type = 'sco'  " +
				"\n  			   AND c.subj = tz_subj.subj  " +
				"\n  		 )  " +
				"\n  		 END countSco , tz_subj.height, tz_subj.width ";
			
			if ( isCPEduAuth ) {
				sql +=
					"\n  FROM tz_subj tz_subj, tz_cpinfo tz_cpinfo  " +
					"\n  WHERE isonoff in ('ON','ML')  " +
				    "\n    AND tz_cpinfo.userid = " + StringManager.makeSQL(box.getSession("userid")) +
					"\n    AND tz_subj.producer = tz_cpinfo.cpseq " ;
			} else {
				sql += 
					"\n  FROM tz_subj tz_subj  " +
					"\n  WHERE isonoff in ('ON','ML')" +
					"\n    and tz_subj.producer is not null  ";
			}
			
			// 검색조건 Sql
			String searchSql = "";
			if (!s_upperclass.equals("ALL") && !s_upperclass.equals(""))
				searchSql += "\n     AND tz_subj.upperclass=" + StringManager.makeSQL(s_upperclass);
			
			if (!s_middleclass.equals("ALL") && !s_middleclass.equals(""))
				searchSql += "\n     AND tz_subj.middleclass=" + StringManager.makeSQL(s_middleclass);
			
			if (!s_lowerclass.equals("ALL") && !s_lowerclass.equals(""))
				searchSql += "\n     AND tz_subj.lowerclass=" + StringManager.makeSQL(s_lowerclass);
			
			if (!s_contenttype.equals("ALL") && !s_contenttype.equals(""))
				searchSql += "\n     AND tz_subj.contenttype=" + StringManager.makeSQL(s_contenttype);

			if (!s_subjnm.equals("") )
				searchSql += "\n     AND UPPER(tz_subj.subjnm) like UPPER(" + StringManager.makeSQL("%" + s_subjnm + "%") + ")";
			
			if (!s_subj.equals("") )
				searchSql += "\n     AND UPPER(tz_subj.subj) like UPPER(" + StringManager.makeSQL("%" + s_subj + "%") + ")";
			
			searchSql += "\n  ORDER BY ";

			if (p_orderColumn.equals("subj") || p_orderColumn.equals(""))
				searchSql += "subj " + p_orderType;
			else if (p_orderColumn.equals("subjnm"))
				searchSql += "tz_subj.subjnm " + p_orderType;
			else if (p_orderColumn.equals("dir"))
				searchSql += "tz_subj.dir " + p_orderType;
			else if (p_orderColumn.equals("branch"))
				searchSql += "tz_subj.ismfbranch " + p_orderType;
			else if (p_orderColumn.equals("center"))
				searchSql += "tz_subj.iscentered " + p_orderType;

			ls = connMgr.executeQuery(sql+searchSql);
System.out.println("ContentsBean **** : "+sql+searchSql);
			logger.debug( "LCMS > 컨텐츠 관리 > 리스트 Query \n" + sql+searchSql);
			
			DataBox dbox = null;
			while (ls.next())
			{
				dbox = ls.getDataBox();
				list.add( dbox );
			}
		}
		catch (Exception ex)
		{
			logger.error( sql, ex );
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally
		{
			if (ls != null) {
				try { ls.close(); } catch (Exception e) {}
			}
			if (connMgr != null) {
				try { connMgr.freeConnection(); } catch (Exception e10) {}
			}
		}

		return list;
	}

	public DataBox selectContentInfo(RequestBox box) throws Exception {
		return null;
	}
}
