/**
 * ReportResultBean.java
 * <p>제목:과제 채점 관리 제어하는 Bean</p>
 * <p>설명:과제 채점 관리 제어 프로그램</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: credu </p>
 * @author ykcho (2008. 01)
 * @version 1.0
 */

package com.credu.learn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Date;
import java.text.SimpleDateFormat;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableWorkbook; // 실제 엑셀 파일 관리를 위해 Workbook, Sheet을 생성하는 Swing의 Frame과 같은 사막의 오아시스 역할하는 추상 클래스
import jxl.write.WritableSheet; // 쉬트를 관리하는 인터페이스
import jxl.write.WritableCellFormat; // 셀의 포멧 관련 클래스
import jxl.write.WriteException; // 셀의 쓰기 오류를 관리하기 위한
import jxl.write.Label; // 라벨 관리 클래스
import jxl.write.WritableFont;
import jxl.format.*;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DataBox;
import com.ziaan.library.DatabaseExecute;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileMove;
import com.ziaan.library.PreparedBox;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.RequestManager;
import com.ziaan.common.UploadUtil;

public class ReportResultBean
{
	private ConfigSet config;
	private int row;
	private String dirUploadDefault;
	private String dirUploadStuReport;
	private String dirUploadReportResult;
	public ReportResultBean()
	{
		try
		{
			config = new ConfigSet();
			row = Integer.parseInt(config.getProperty("page.bulletin.row"));				//			?? ????? ??????? row ?? ???????
			//dirUploadDefault = config.getProperty("dir.upload.reportuser");
			dirUploadDefault = config.getProperty("dir.upload.reportprof");
			dirUploadStuReport = config.getProperty("dir.upload.reportstu");
			dirUploadReportResult = config.getProperty("dir.upload.reportresult");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public ArrayList selectListOrder(RequestBox box) throws Exception
	{
		DatabaseExecute db = null;
		ArrayList list = null;
		String sql = "";

		try
		{
			db = new DatabaseExecute();

			String v_grcode  = box.getString("p_grcode");
			String v_subj 	 = box.getString("p_subj");
      String v_year 	 = box.getString("p_year");
      String v_subjseq = box.getString("p_subjseq");
      String v_class 	 = box.getString("p_class");

      String v_schyearsubjseq  = box.getString("p_schyearsubjseq");
      String v_searchyearsubj  = box.getString("p_searchyearsubj");
      
      if(!v_searchyearsubj.equals("")){
          StringTokenizer st1 = new StringTokenizer(v_searchyearsubj,"[$]");
          	v_subj    = (String)st1.nextToken();
          	v_grcode = (String)st1.nextToken();
          	v_subjseq = (String)st1.nextToken();
          	v_class = (String)st1.nextToken();
          	v_year = (String)st1.nextToken(); 
      }
      
			sql += "\r\n SELECT ordseq, weeklyseq, weeklysubseq, title, startdate, expiredate,  ";
			sql += "\r\n        restartdate, reexpiredate, score, reptype,                      ";
			sql += "\r\n        CASE WHEN                                                                                   ";
			sql += "\r\n               TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') ";
			sql += "\r\n              BETWEEN startdate AND expiredate                                                      ";
			sql += "\r\n             THEN 'Y' ELSE 'N'                                                                      ";
			sql += "\r\n        END indate,                                                                                 ";
			sql += "\r\n        CASE WHEN NVL(restartdate, 'NULL') != 'NULL'                 ";
			sql += "\r\n             THEN CASE WHEN  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS')";
			sql += "\r\n                            BETWEEN restartdate AND reexpiredate ";
			sql += "\r\n	                     THEN 'Y' ELSE 'N'             ";
			sql += "\r\n	                      END                          ";
			sql += "\r\n              END adddate,                           ";
			sql += "\r\n        CASE WHEN reptype = 'P'                      ";
			sql += "\r\n             THEN (SELECT COUNT(*) FROM tz_student   ";
			sql += "\r\n                    WHERE 1=1                        ";
			sql += "\n\r                      AND grcode = " 		+ StringManager.makeSQL(v_grcode);
			sql += "\n\r                      AND subj = " 		  + StringManager.makeSQL(v_subj);
      sql += "\n\r                      AND year = " 		  + StringManager.makeSQL(v_year);
      sql += "\n\r                      AND subjseq = " 	+ StringManager.makeSQL(v_subjseq);
     		sql += "\r\n                   )                                  ";
			sql += "\r\n             WHEN reptype = 'T'                       ";
			sql += "\r\n             THEN (SELECT COUNT(*) FROM tz_teammaster ";
			sql += "\r\n                    WHERE 1=1                         ";
			sql += "\n\r                      AND grcode = " 		+ StringManager.makeSQL(v_grcode);
			sql += "\n\r                      AND subj = " 		  + StringManager.makeSQL(v_subj);
      sql += "\n\r                      AND year = " 		  + StringManager.makeSQL(v_year);
      sql += "\n\r                      AND subjseq = " 	+ StringManager.makeSQL(v_subjseq);
     		sql += "\r\n                  )                       ";
			sql += "\r\n              END total,                  ";
			sql += "\r\n        (SELECT COUNT(*) FROM tu_projrep  ";
			sql += "\r\n          WHERE ldate is not null                       ";
			sql += "\n\r            AND grcode = " 		+ StringManager.makeSQL(v_grcode);
			sql += "\n\r            AND subj = " 		  + StringManager.makeSQL(v_subj);
      sql += "\n\r            AND year = " 		  + StringManager.makeSQL(v_year);
      sql += "\n\r            AND subjseq = " 	+ StringManager.makeSQL(v_subjseq);
      sql += "\n\r            AND class = " 		+ StringManager.makeSQL(v_class);
			sql += "\r\n            AND ordseq = a.ordseq         ";
			sql += "\r\n        ) submitcnt                       ";
			sql += "\r\n   FROM TU_PROJORD a                      ";
			sql += "\r\n  WHERE 1=1                               ";
			sql += "\n\r    AND grcode = " 		+ StringManager.makeSQL(v_grcode);
			sql += "\n\r    AND subj = " 		  + StringManager.makeSQL(v_subj);
      sql += "\n\r    AND year = " 		  + StringManager.makeSQL(v_year);
      sql += "\n\r    AND subjseq = " 	+ StringManager.makeSQL(v_subjseq);
      sql += "\n\r    AND class = " 		+ StringManager.makeSQL(v_class);
			sql += "\r\n    AND reptype='P' AND (DELYN = 'N' OR DELYN IS NULL) ";
			sql += "\r\n  ORDER BY year DESC, subjseq, ordseq DESC";

			box.put("p_isPage", new Boolean(false));     
			box.put("p_row", new Integer(row));     
System.out.println(sql);
			list = db.executeQueryList(box, sql);
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		return list;
	}

	/**
	??f??f ????; v????.
	@param RequestBox box
	@return DataBox
	*/
	public DataBox selectViewOrder(RequestBox box) throws Exception
	{
		DatabaseExecute db = null;
		DataBox dbox = null;
		String sql = "";

		try
		{
			db = new DatabaseExecute();

      String v_grcode  = box.getString("p_grcode");
      String v_subj 	 = box.getString("p_subj");
      String v_year 	 = box.getString("p_year");
      String v_subjseq = box.getString("p_subjseq");
      String v_class 	 = box.getString("p_class");

			String v_ordseq = box.getString("p_ordseq");

			sql  = "\r\n SELECT a.ordseq, a.weeklyseq, a.weeklysubseq, a.title,";
			sql += "\r\n        a.score, a.basicscore, a.perfectscore, a.submitscore, a.notsubmitscore, a.reptype, ";
			sql += "\r\n        a.startdate, a.expiredate, a.isopenscore, b.subjnm,  ";
			sql += "\r\n        CASE WHEN a.reexpiredate= ''                       ";
			// sql += "\r\n        CASE WHEN (a.LDATE <= C.EXPIREDATE)                       ";
			sql += "\r\n             THEN CASE WHEN  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') <= a.expiredate AND  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') >= a.startdate      ";
			sql += "\r\n	                     THEN 'Y' ELSE 'N'                                        ";
			sql += "\r\n	              END                                                     ";
			sql += "\r\n             ELSE CASE WHEN  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') <= a.expiredate AND  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') >= a.startdate  ";
			sql += "\r\n	                     THEN 'Y' ELSE                                         ";
			sql += "\r\n                             CASE WHEN  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') <= a.reexpiredate AND  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') >= a.restartdate ";  
			sql += "\r\n                                 THEN 'Y' ELSE 'N'                                        ";
			sql += "\r\n                             END                                                         ";
			sql += "\r\n	              END                                                     ";
			sql += "\r\n        END indate                                                            ";
			sql += "\r\n   FROM tu_projord a left join tz_subj b on (a.subj = b.subj) ";
			sql += "\r\n  WHERE 1=1 ";
			sql += "\n\r    AND a.grcode = " 		  + StringManager.makeSQL(v_grcode);
			sql += "\n\r    AND a.subj = " 		  + StringManager.makeSQL(v_subj);
      sql += "\n\r    AND a.year = " 		  + StringManager.makeSQL(v_year);
      sql += "\n\r    AND a.subjseq = " 	+ StringManager.makeSQL(v_subjseq);
      sql += "\n\r    AND a.class = " 		+ StringManager.makeSQL(v_class);
			sql += "\r\n    AND a.ordseq = "    + v_ordseq;
			sql += "\r\n    AND (a.DELYN = 'N' OR a.DELYN IS NULL) ";

			//----------------------   ??f??f ???γ??? v? ----------------------------
			dbox = db.executeQuery(box, sql);
		//System.out.println("sql :20100316:: \n" + sql);
		}
		
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		return dbox;
	}

	/**
	과제제출 목록(제출자, 미제출자 포함)
	@param RequestBox box
	@return DataBox
	*/
	public ArrayList selectReportStdList(RequestBox box) throws Exception
	{
		DatabaseExecute db = null;
		ArrayList list = null;
		StringBuffer sbSql = new StringBuffer();

		try
		{
			db = new DatabaseExecute();

      String v_grcode  = box.getString("p_grcode");
      String v_subj 	 = box.getString("p_subj");
      String v_year 	 = box.getString("p_year");
      String v_subjseq = box.getString("p_subjseq");
      String v_class 	 = box.getString("p_class");

			String v_ordseq = box.getString("p_ordseq");
			String s_language = box.getSession("languageName");

      sbSql.append("SELECT A.USERID AS PROJID, D.LDATE, D.ISBESTREPORT, \r\n"
          ).append("    D.REALFILE, D.NEWFILE, D.SCORE AS FINALSCORE, \r\n"
          ).append("    GET_NAME(A.USERID) AS NAME, \r\n"
          ).append("    GET_DANGWA(B.COMP, '").append(s_language).append("') AS HAKGWANM, \r\n"

          ).append("    CASE WHEN (D.LDATE IS NULL OR D.LDATE = '') THEN \r\n"
          ).append("          'NOT' ELSE \r\n"
          
          // ).append("        CASE WHEN (C.REEXPIREDATE IS NULL OR C.REEXPIREDATE = '') \r\n"
          // D.LDATE <= C.EXPIREDATE 제출일이 제출기간보다 늦었을 경우 추가제출기한에 제출했는지 체크
          ).append("        CASE WHEN (D.LDATE <= C.EXPIREDATE) \r\n"       
          ).append("             THEN CASE WHEN D.LDATE <= C.EXPIREDATE AND D.LDATE >= C.STARTDATE \r\n"
          ).append("                       THEN 'IN' ELSE 'OUT' \r\n"
          ).append("                 END \r\n"
          ).append("             ELSE CASE WHEN D.LDATE <= C.REEXPIREDATE AND D.LDATE >= C.RESTARTDATE \r\n"
          ).append("                       THEN 'RE' ELSE 'OUT' \r\n"
          ).append("                 END \r\n"
          ).append("        END \r\n"
          ).append("    END AS INDATE, \r\n"

          ).append("    B.EMAIL, B.handphone as MOBILE, \r\n"
          ).append("    CASE WHEN D.SCORE IS NULL THEN 'N' ELSE 'Y' END AS SCOREEXISTS, D.TUTORDATE, \r\n"
         // 20100601 - 감점일 1차종료일 -> 2차시작일부터감점으로 수정
         // ).append("    round(to_date(c.expiredate, 'yyyymmddhh24miss') - (to_date(D.LDATE, 'yyyymmddhh24miss'))-0.5) as minusday  \r\n"
          ).append("    round(to_date(c.RESTARTDATE, 'yyyymmddhh24miss') - (to_date(D.LDATE, 'yyyymmddhh24miss'))-0.5) as minusday  \r\n"
          ).append("    ,getscore, minusscore  \r\n"
          ).append("  FROM Tz_STUDENT a \r\n"
          ).append("      INNER JOIN Tz_MEMBER B ON (trim(A.USERID) = trim(B.USERID)) \r\n"
          ).append("      INNER JOIN TU_PROJORD c "
          ).append("                                on (trim(A.SUBJ) = trim(C.SUBJ)) \r\n"
          ).append("                                AND (trim(A.YEAR) = trim(C.YEAR)) \r\n"
          ).append("                                AND (trim(A.SUBJSEQ) = trim(C.SUBJSEQ)) \r\n"
          ).append("                                AND (C.ORDSEQ = ").append(v_ordseq).append(") \r\n"
          ).append("      LEFT JOIN TU_PROJREP D ON  \r\n"
          ).append("                                 (trim(A.SUBJ) = trim(D.SUBJ)) \r\n"
          ).append("                                AND (trim(A.YEAR) = trim(D.YEAR)) \r\n"
          ).append("                                AND (trim(A.SUBJSEQ) = trim(D.SUBJSEQ)) \r\n"
          ).append("                                  \r\n"
          ).append("                                AND (trim(A.USERID) = trim(D.PROJID)) \r\n"
          ).append("                                AND (D.ORDSEQ = ").append(v_ordseq).append(") \r\n"
          ).append("  WHERE  \r\n"
          ).append("     A.SUBJ = '").append(v_subj).append("' \r\n"
          ).append("    AND A.YEAR = '").append(v_year).append("' \r\n"
          ).append("    AND A.SUBJSEQ = '").append(v_subjseq).append("' \r\n"
          ).append("  ORDER BY A.USERID ");

      
     
     System.out.println("KSC : ------------------------"+sbSql.toString());
			list = db.executeQueryList(box, sbSql.toString());

		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, sbSql.toString());
			throw new Exception("sql = " + sbSql.toString() + "\r\n" + ex.getMessage());
		}
		return list;
	}

  /*
  * 표절검사를 했는지 확인
  */
  public boolean hasCheckedCopy(RequestBox box) throws Exception
	{
		DatabaseExecute db = null;
		ArrayList list = null;
		boolean v_return = false;

		String v_grcode  = box.getString("p_grcode");
    String v_subj 	 = box.getString("p_subj");
    String v_year 	 = box.getString("p_year");
    String v_subjseq = box.getString("p_subjseq");
    String v_class 	 = box.getString("p_class");
		int v_ordseq = box.getInt("p_ordseq");

    String sql = "SELECT GRCODE ";
          sql += "  FROM TU_PROJREP_CHECK ";
          sql += "  WHERE GRCODE = '"+ v_grcode +"' ";
          sql += "    AND SUBJ = '"+ v_subj +"' ";
          sql += "    AND YEAR = '"+ v_year +"' ";
          sql += "    AND SUBJSEQ = '"+ v_subjseq +"' ";
          sql += "    AND CLASS = '"+ v_class+"' ";
          sql += "    AND ORDSEQ = "+ v_ordseq;

		try {
			db = new DatabaseExecute();
			list = db.executeQueryList(box, sql);
			if(list.size()>0) {
			  v_return = true;
			}

		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		return v_return;
	}

	/**
	f???f ????; v????.
	@param RequestBox box
	@return DataBox
	*/
	public ArrayList selectListSubmitReport(RequestBox box) throws Exception
	{
		DatabaseExecute db = null;
		ArrayList list = null;
		String sql = "";

		try
		{
			db = new DatabaseExecute();

      String v_grcode  = box.getString("p_grcode");
      String v_subj 	 = box.getString("p_subj");
      String v_year 	 = box.getString("p_year");
      String v_subjseq = box.getString("p_subjseq");
      String v_class 	 = box.getString("p_class");

			String v_ordseq = box.getString("p_ordseq");
			String s_language = box.getSession("languageName");

			sql  = "\r\n SELECT ";
			sql += "\r\n        a.seq, a.projid, a.ldate, a.isbestreport,                     ";
			sql += "\r\n        a.realfile, a.newfile, a.score finalscore,                    ";
			sql += "\r\n        get_name(a.projid) name,                                  ";
			sql += "\r\n        GET_DANGWA(AA.comp, 'KOREAN') as hakgwanm,                                         ";
			sql += "\r\n        CASE WHEN a.ldate <= expiredate AND a.ldate >= startdate      ";
			sql += "\r\n             THEN 'Y' ELSE 'N'                                        ";
			sql += "\r\n         END indate                                                   ";
			sql += "\r\n   FROM tu_projrep a                                               ";
			sql += "\r\n        INNER JOIN tu_member AA ON (AA.userid = a.projid)          ";
			sql += "\r\n         LEFT OUTER JOIN tu_projord c ON  (a.subj = c.subj)        ";
			sql += "\r\n 																		     AND (a.year = c.year)        ";
			sql += "\r\n 																		     AND (a.subjseq = c.subjseq)  ";
			sql += "\r\n 																		     AND (a.class = c.class)      ";
			sql += "\r\n 																		     AND (a.ordseq = c.ordseq)    ";
			sql += "\r\n  WHERE 1=1                                                            ";
			sql += "\n\r    AND a.grcode = " 		+ StringManager.makeSQL(v_grcode);
			sql += "\n\r    AND a.subj = " 		  + StringManager.makeSQL(v_subj);
      sql += "\n\r    AND a.year = " 		  + StringManager.makeSQL(v_year);
      sql += "\n\r    AND a.subjseq = " 	+ StringManager.makeSQL(v_subjseq);
      sql += "\n\r    AND a.class = " 		+ StringManager.makeSQL(v_class);
			sql += "\r\n    AND a.ordseq = "    + v_ordseq;
			sql += "\r\n order by projid ";

			box.put("p_isPage", new Boolean(false));							//    ????? ???? ?????? ?????? ??? ???
			box.put("p_row", new Integer(row));										//    ????? ???? ?????? ?????? ??? ????? ???

			list = db.executeQueryList(box, sql);
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		return list;
	}

	/**
	??f???f ????; v????.
	@param RequestBox box
	@return DataBox
	*/
	public ArrayList selectListNotSubmitReport(RequestBox box) throws Exception
	{
		DatabaseExecute db = null;
		ArrayList list = null;
		String sql = "";

		try
		{
			db = new DatabaseExecute();

      String v_grcode  = box.getString("p_grcode");
      String v_subj 	 = box.getString("p_subj");
      String v_year 	 = box.getString("p_year");
      String v_subjseq = box.getString("p_subjseq");
      String v_class 	 = box.getString("p_class");

			String v_ordseq   = box.getString("p_ordseq");
			String s_language = box.getSession("languageName");
			String v_reptype  = box.getString("p_reptype");

			sql += "\r\n SELECT ";
			sql += "\r\n        a.userid projid, ";
			sql += "\r\n        get_name(a.userid) name, ";
			sql += "\r\n        GET_DANGWA(AA.comp, 'KOREAN') as hakgwanm, AA.email, AA.mobile ";
			sql += "\r\n   FROM tu_student a";
			sql += "\r\n        INNER JOIN tu_member AA ON (AA.userid = a.userid) ";
			sql += "\r\n  WHERE 1=1 ";
			sql += "\n\r    AND a.grcode = " 		+ StringManager.makeSQL(v_grcode);
			sql += "\n\r    AND a.subj = " 		  + StringManager.makeSQL(v_subj);
      sql += "\n\r    AND a.year = " 		  + StringManager.makeSQL(v_year);
      sql += "\n\r    AND a.subjseq = " 	+ StringManager.makeSQL(v_subjseq);
      sql += "\n\r    AND a.class = " 		+ StringManager.makeSQL(v_class);

			sql += "\r\n    AND (AA.userid NOT IN (                                  ";
			sql += "\r\n                           SELECT projid  FROM  tu_projrep  ";
			sql += "\r\n                            WHERE grcode = "    + StringManager.makeSQL(v_grcode);
			sql += "\r\n                              AND subj = " 		  + StringManager.makeSQL(v_subj);
      sql += "\n\r                              AND year = " 		  + StringManager.makeSQL(v_year);
      sql += "\n\r                              AND subjseq = " 	+ StringManager.makeSQL(v_subjseq);
      sql += "\n\r                              AND class = " 		+ StringManager.makeSQL(v_class);
			sql += "\r\n                              AND ordseq = "    + v_ordseq;
			sql += "\r\n                          )";

			sql += "\r\n       OR AA.userid IN (                                  ";
			sql += "\r\n                           SELECT projid FROM tu_projrep  ";
			sql += "\r\n                            WHERE ldate is null ";
			sql += "\r\n                              AND grcode = "    + StringManager.makeSQL(v_grcode);
			sql += "\r\n                              AND subj = " 		  + StringManager.makeSQL(v_subj);
      sql += "\n\r                              AND year = " 		  + StringManager.makeSQL(v_year);
      sql += "\n\r                              AND subjseq = " 	+ StringManager.makeSQL(v_subjseq);
      sql += "\n\r                              AND class = " 		+ StringManager.makeSQL(v_class);
			sql += "\r\n                              AND ordseq = "    + v_ordseq;
			sql += "\r\n                          )) ";

			sql += "\r\n   ORDER BY AA.userid ";

			box.put("p_isPage", new Boolean(false));     //    ????? ???? ?????? ?????? ??? ???
			box.put("p_row", new Integer(row));          //    ????? ???? ?????? ?????? ??? ????? ???

			list = db.executeQueryList(box, sql);
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		return list;
	}

	/**
	???? f???f ?? v????.
	@param RequestBox box
	@return DataBox
	*/
	public DataBox selectViewReportCount(RequestBox box) throws Exception
	{
		DatabaseExecute db = null;
		DataBox dbox = null;
		String sql = "";

		try
		{
			db = new DatabaseExecute();

      String v_grcode  = box.getString("p_grcode");
      String v_subj 	 = box.getString("p_subj");
      String v_year 	 = box.getString("p_year");
      String v_subjseq = box.getString("p_subjseq");
      String v_class 	 = box.getString("p_class");

			String v_ordseq = box.getString("p_ordseq");

			String v_reptype = "P";

			sql  = "\r\n SELECT ( SELECT COUNT(*) FROM tz_student ";
			sql += "\r\n           WHERE 1=1 ";
			sql += "\n\r             AND subj = " 		+ StringManager.makeSQL(v_subj);
      sql += "\n\r             AND year = " 		+ StringManager.makeSQL(v_year);
      sql += "\n\r             AND subjseq = " 	+ StringManager.makeSQL(v_subjseq);
      	sql += "\r\n        ) total, ";
			sql += "\r\n        ( SELECT COUNT(*) FROM tu_projrep ";
			sql += "\r\n           WHERE ldate is not null ";
			sql += "\n\r             AND grcode = " 		+ StringManager.makeSQL(v_grcode);
			sql += "\n\r             AND subj = " 		+ StringManager.makeSQL(v_subj);
      sql += "\n\r             AND year = " 		+ StringManager.makeSQL(v_year);
      sql += "\n\r             AND subjseq = " 	+ StringManager.makeSQL(v_subjseq);
      sql += "\n\r             AND class = " 		+ StringManager.makeSQL(v_class);
			sql += "\r\n             AND ordseq = "   + v_ordseq;
			sql += "\r\n        ) submitcnt from dual ";
			//----------------------   ???? f???f ?? v? ----------------------------
			dbox = db.executeQuery(box, sql);
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		return dbox;
	}

	/**
	f??? ??f ????; v????.
	@param RequestBox box
	@return DataBox
	*/
	public DataBox selectViewSubmitReport(RequestBox box) throws Exception
	{
		DatabaseExecute db = null;
		DataBox dbox = null;
		String sql = "";

		try
		{
			db = new DatabaseExecute();

      String v_grcode  = box.getString("p_grcode");
      String v_subj 	 = box.getString("p_subj");
      String v_year 	 = box.getString("p_year");
      String v_subjseq = box.getString("p_subjseq");
      String v_class 	 = box.getString("p_class");

			String v_ordseq = box.getString("p_ordseq");
			String v_projid = box.getString("p_projid");
//			String v_seq    = box.getString("p_seq");

			if(v_projid.equals("")) { // 본인의 과제 이력 조회할 때는 파라미터를 열어두면 안됨. -> 세션에서.
			 v_projid = box.getSession("userid");
			}

			sql  = "\r\n SELECT ";
			sql += "\r\n        a.seq, a.projid, a.ldate, a.realfile, a.newfile, /*s.subjnm,*/         ";
			sql += "\r\n        a.title, a.contents, a.score finalscore, a.tutordate,             ";
			sql += "\r\n        a.isbestreport, a.tutorcontents, a.tutorrealfile, a.tutornewfile, a.tutorrealfilesize, ";
			sql += "\r\n        get_name(a.projid) name,                                      ";
			sql += "\r\n        GET_DANGWA(C.COMP, 'KOREAN') hakgwanm,                                ";
			sql += "\r\n        CASE WHEN b.teamno IS NULL                                           ";
			sql += "\r\n             THEN 0 ELSE b.teamno                                       ";
			sql += "\r\n         END teamno                                                       ";
			sql += "\r\n   FROM /*tu_subj s,*/ tu_projrep a                                                   ";
			sql += "\r\n        LEFT OUTER JOIN tu_teammember b ON (a.subj = b.subj)           ";
			sql += "\r\n 																	        AND (a.year = b.year)           ";
			sql += "\r\n 																	        AND (a.subjseq = b.subjseq)     ";
			sql += "\r\n 																	        AND (a.class = b.class)         ";
			sql += "\r\n 																	        AND (a.projid = b.userid)       ";
			sql += "\r\n        INNER JOIN TU_MEMBER c ON (A.PROJID = C.USERID)                ";
			sql += "\r\n  WHERE 1=1                                                               ";
			sql += "\n\r    AND a.grcode = " 		+ StringManager.makeSQL(v_grcode);
			sql += "\n\r    AND a.subj = " 		 + StringManager.makeSQL(v_subj);
      sql += "\n\r    AND a.year = " 		 + StringManager.makeSQL(v_year);
      sql += "\n\r    AND a.subjseq = "  + StringManager.makeSQL(v_subjseq);
      sql += "\n\r    AND a.class = " 	 + StringManager.makeSQL(v_class);
			sql += "\r\n    AND a.ordseq = "   + v_ordseq;
			sql += "\r\n    AND a.projid = "   + StringManager.makeSQL(v_projid);
//			sql += "\r\n    AND a.seq = "      + StringManager.makeSQL(v_seq);
			sql += "\r\n    /*AND a.subj = s.subj*/ ";
			sql += "\r\n  ORDER BY seq DESC ";
			//----------------------   ???γ??? v? ----------------------------
//			System.out.println("giggle : "+ sql);
			dbox = db.executeQuery(box, sql);
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		return dbox;
	}

	/**
	제출자로 처리
	@param RequestBox box
	@return boolean
	*/
	public boolean updateSubmit(RequestBox box) throws Exception
	{
		DataBox dbox = null;
//		String sql = "";
		boolean isCommit = false;
		DatabaseExecute db = null;
		String sql1 = "";
		String sql2 = "";

		String v_grcode  = box.getString("p_grcode");
		String v_subj 	 = box.getString("p_subj");
		String v_year 	 = box.getString("p_year");
		String v_subjseq = box.getString("p_subjseq");
		String v_class 	 = box.getString("p_class");

		String v_ordseq       = box.getString("p_ordseq");
		 //System.out.println("v_ordseq	:	"+v_ordseq);
		String v_weeklyseq    = box.getString("p_weeklyseq");
		String v_weeklysubseq = box.getString("p_weeklysubseq");
		String s_userid       = box.getSession("userid");
		String v_userid       = box.getString("p_userid");
		String [] tempArr     = v_userid.split(",");
		String v_title        = box.getString("p_title");
		String v_basicscore   = box.getString("p_basicscore");

//		Vector v_useridVec  = new Vector();

		sql1  = "\r\n UPDATE tu_projrep SET   ";
		sql1 += "\r\n       weeklyseq = ?         ";
		sql1 += "\r\n     , weeklysubseq = ?  ";
		sql1 += "\r\n     , title = ?  ";
		sql1 += "\r\n     , luserid = ?  ";
		sql1 += "\r\n     , ldate =  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') ";
		sql1 += "\r\n WHERE 1=1 ";
		sql1 += "\r\n   AND grcode    = ? ";
		sql1 += "\r\n   AND subj    = ? ";
		sql1 += "\r\n   AND year    = ? ";
		sql1 += "\r\n   AND subjseq = ? ";
		sql1 += "\r\n   AND class   = ? ";
		sql1 += "\r\n   AND ordseq  = ? ";
		sql1 += "\r\n   AND projid  = ? ";

		sql2  = "\r\n INSERT INTO TU_PROJREP (                                            ";
		sql2 += "\r\n         grcode, subj, year, subjseq, class, ordseq, projid,     ";
		sql2 += "\r\n         weeklyseq, weeklysubseq, title, score, luserid, ldate       ";
		sql2 += "\r\n   )VALUES(                                                            ";
		sql2 += "\r\n         ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,                       ";
		sql2 += "\r\n          TO_CHAR(sysdate, 'YYYYMMDDHH24MISS'))";
		
		try
		{
			db = new DatabaseExecute(box);

			for(int i=0; i<tempArr.length; i++) {

  			PreparedBox pbox1 = new PreparedBox("preparedbox");

  			int lmt_Idx = 1;

  			pbox1.setString(lmt_Idx++, v_weeklyseq);
  			pbox1.setString(lmt_Idx++, v_weeklysubseq);
  			pbox1.setString(lmt_Idx++, v_title);
  			pbox1.setString(lmt_Idx++, s_userid);

  			pbox1.setString(lmt_Idx++, v_grcode);
  			pbox1.setString(lmt_Idx++, v_subj);
  			pbox1.setString(lmt_Idx++, v_year);
  			pbox1.setString(lmt_Idx++, v_subjseq);
  			pbox1.setString(lmt_Idx++, v_class);
  			pbox1.setInt(lmt_Idx++, Integer.parseInt(v_ordseq));
  			pbox1.setString(lmt_Idx++, tempArr[i]);
  			
  			isCommit = db.executeUpdate(new PreparedBox [] {pbox1},  new String [] {sql1});
  			
    		if(!isCommit) {
    			pbox1 = new PreparedBox("preparedbox");

    			lmt_Idx = 1;
    			pbox1.setString(lmt_Idx++, v_grcode);
    			pbox1.setString(lmt_Idx++, v_subj);
    			pbox1.setString(lmt_Idx++, v_year);
    			pbox1.setString(lmt_Idx++, v_subjseq);
    			pbox1.setString(lmt_Idx++, v_class);
    			pbox1.setInt(lmt_Idx++, Integer.parseInt(v_ordseq));
    			pbox1.setString(lmt_Idx++, tempArr[i]);
    			pbox1.setString(lmt_Idx++, v_weeklyseq);
    			pbox1.setString(lmt_Idx++, v_weeklysubseq);
    			pbox1.setString(lmt_Idx++, v_title);
    			pbox1.setString(lmt_Idx++, v_basicscore);
    			pbox1.setString(lmt_Idx++, s_userid);

    			isCommit = db.executeUpdate(new PreparedBox [] {pbox1},  new String [] {sql2});
    			
    		}

  		}

		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
			throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
		}
		return isCommit;
	}

	/**
	??f a?? ??????.
	@param RequestBox box
	@return boolean
	*/
	public boolean updateScore(RequestBox box) throws Exception
	{
		boolean isCommit = false;
		DatabaseExecute db = null;
		DataBox dbox = null;
		String sql = "";
		String sql1 = "";
		String sql2 = "";

		char a = (char)2;
    char b = (char)3;

		try
		{
			db = new DatabaseExecute(box);

      String v_grcode  = box.getString("p_grcode");
      String v_subj 	 = box.getString("p_subj");
      String v_year 	 = box.getString("p_year");
      String v_subjseq = box.getString("p_subjseq");
      String v_class 	 = box.getString("p_class");

			String v_ordseq = box.getString("p_ordseq");
			String v_projid = box.getString("p_projid");
//			String v_seq    = box.getString("p_seq");

			String v_isbestreport   = box.getString("p_isbestreport");
			String v_finalscore     = box.getString("p_finalscore");
			String v_tutorcontents  = box.getString("p_tutorcontents");
			String v_files   = box.getString("p_files");

//			String v_deletefile = box.getString("p_deletefile");
      //----------------------   삭제되는 파일 --------------------------------
      int	  v_upfilecnt     = box.getInt("p_upfilecnt");	//	서버에 저장되있는 파일수

      Vector v_savefile      =	new	Vector();
      Vector v_filesequence  =	new	Vector();

      for(int	i =	0; i < v_upfilecnt;	i++) {
        if(	!box.getString("p_fileseq" + i).equals(""))	{
          v_savefile.addElement(box.getString("p_savefile" + i));    // 서버에 저장되있는 파일명 중에서 삭제할 파일들
          v_filesequence.addElement(box.getString("p_fileseq"	+ i)); // 서버에 저장되있는 파일번호 중에서 삭제할 파일들
        }
      }
  		/*
  		Vector realFileNames  = box.getRealFileNames("p_upfile");
  		Vector newFileNames   = box.getNewFileNames("p_upfile");
  		*/
  		//----------------------   업로드되는 경로 -------------------------------
      String v_filepath = UploadUtil.performMakeFilepath("L", box);

      //----------------------   업로드되는 파일 -------------------------------
      String v_filesArr [] = v_files.split("["+b+"]");

      Vector realFileNames = new Vector();
      Vector newFileNames  = new Vector();
      Vector fileSizes     = new Vector();
      Vector fileseqs      = new Vector();

      if (!v_files.equals("")){
        for(int i=0, j=v_filesArr.length; i < j; i++) {
            String v_filename []  = v_filesArr[i].split("["+a+"]");
            fileseqs.add(new Integer(i+1));
            realFileNames.add(v_filename[0]);
            newFileNames.add(v_filename[1]);
            fileSizes.add(v_filename[2]);
        }
        // 새로 저장되는 파일이 있을 경우는 삭제태그 하지 않아도 지운다.
        v_savefile.addElement(box.getString("p_newfile"));
      }


			//////////////////////////////////   ÷?????? d?? v?  ///////////////////////////////////////////////////////////////////
			/*
			sql  = "\r\n SELECT tutornewfile  ";
			sql += "\r\n   FROM TU_PROJREP    ";
			sql += "\r\n  WHERE 1=1           ";
			sql += "\n\r    AND subj = " 		 + StringManager.makeSQL(v_subj);
      sql += "\n\r    AND year = " 		 + StringManager.makeSQL(v_year);
      sql += "\n\r    AND subjseq = "  + StringManager.makeSQL(v_subjseq);
      sql += "\n\r    AND class = " 	 + StringManager.makeSQL(v_class);
			sql += "\r\n    AND ordseq = "   + StringManager.makeSQL(v_ordseq);
			sql += "\r\n    AND projid = "   + StringManager.makeSQL(v_projid);
			sql += "\r\n    AND seq = "      + StringManager.makeSQL(v_seq);

			dbox = db.executeQuery(box, sql);
			*/

			//////////////////////////////////   ??f table ??d  ///////////////////////////////////////////////////////////////////
			sql1  = "\r\n UPDATE TU_PROJREP SET   ";
			sql1 += "\r\n       isbestreport = ?  ";
			sql1 += "\r\n     , score = ?         ";
			sql1 += "\r\n     , tutorcontents = ? ";

//			if(realFileNames.size() > 0 || (v_deletefile.equals("Y") && realFileNames.size() == 0)){
	    if(realFileNames.size() > 0 || v_filesequence.size() > 0){
				sql1 += "\r\n     , tutorrealfile = ? ";
				sql1 += "\r\n     , tutornewfile = ?  ";
				sql1 += "\r\n     , tutorrealfilesize = ?  ";
			}

			sql1 += "\r\n     , tutordate =  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') ";
			sql1 += "\r\n WHERE 1=1         ";
			sql1 += "\r\n   AND grcode  = ? ";
			sql1 += "\r\n   AND subj    = ? ";
			sql1 += "\r\n   AND year    = ? ";
			sql1 += "\r\n   AND subjseq = ? ";
			sql1 += "\r\n   AND class   = ? ";
			sql1 += "\r\n   AND ordseq  = ? ";
			sql1 += "\r\n   AND projid  = ? ";
			sql1 += "\r\n   AND seq     = ? ";

			PreparedBox pbox1 = new PreparedBox("preparedbox");

			int lmt_Idx = 1;

			pbox1.setString(lmt_Idx++, v_isbestreport);
			pbox1.setDouble(lmt_Idx++, Double.parseDouble(v_finalscore));
			pbox1.setString(lmt_Idx++, v_tutorcontents);

			if(realFileNames.size() > 0)
			{
				pbox1.setVector(lmt_Idx++, realFileNames);
				pbox1.setVector(lmt_Idx++, newFileNames);
				pbox1.setVector(lmt_Idx++, fileSizes);
//			} else if(v_deletefile.equals("Y") && realFileNames.size() == 0) {
		  } else if(v_filesequence.size() > 0){ // 삭제
				pbox1.setString(lmt_Idx++, "");
				pbox1.setString(lmt_Idx++, "");
				pbox1.setInt(lmt_Idx++, 0);
			}
			//÷???????? ???, ??f ???? ??? ???? ??d???? ??´?.

			pbox1.setString(lmt_Idx++, v_grcode);
			pbox1.setString(lmt_Idx++, v_subj);
			pbox1.setString(lmt_Idx++, v_year);
			pbox1.setString(lmt_Idx++, v_subjseq);
			pbox1.setString(lmt_Idx++, v_class);
			pbox1.setInt(lmt_Idx++, Integer.parseInt(v_ordseq));
			pbox1.setString(lmt_Idx++, v_projid);
			pbox1.setInt(lmt_Idx++, 1);

			isCommit = db.executeUpdate(new PreparedBox [] {pbox1},  new String [] {sql1});

			//첨부파일을 삭제한다.
      if(isCommit){
        //FileManager.deleteFile("L", v_savefile, box);
      }

			/*
			if(isCommit && v_deletefile.equals("Y"))
			{
				v_savefile = box.getString("p_tutornewfile");
				File del = new File(dirUploadDefault+"/"+v_savefile);

				if (del.exists())
				{
					del.delete();
				}
			}

			if(isCommit && realFileNames.size() > 0)
			{
				v_savefile = dbox.getString("d_tutornewfile");

				File del = new File(dirUploadDefault+"/"+v_savefile);

				if (del.exists())
				{
					del.delete();
				}
			}
			*/
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
			throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
		}
		return isCommit;
	}

	/**
	점수처리 목록에서 점수 일괄저장
	@param RequestBox box
	@return boolean
	*/
	public boolean updateScoreList(RequestBox box) throws Exception
	{
		boolean isCommit = false;
		DatabaseExecute db = null;
		DataBox dbox = null;
		String sql1 = "";
		String sql2 = "";

    String v_grcode  = box.getString("p_grcode");
    String v_subj 	 = box.getString("p_subj");
    String v_year 	 = box.getString("p_year");
    String v_subjseq = box.getString("p_subjseq");
    String v_class 	 = box.getString("p_class");

		String v_ordseq = box.getString("p_ordseq");

		int v_size = box.getInt("p_size");

//		Vector v_seqVec = new Vector();
		Vector v_projidVec = new Vector();
		Vector v_finalscoreVec = new Vector();
		Vector v_isbestreportVec = new Vector();
		Vector v_minusScore = new Vector();
		Vector v_getScore = new Vector();
		
		for(int i = 0; i < v_size; i++)
		{
//			v_seqVec.addElement(box.getString("p_seq"+i));
			v_projidVec.addElement(box.getString("p_projid"+i));
			v_finalscoreVec.addElement(box.getStringDefault("p_finalscore"+i,"N"));
			v_isbestreportVec.addElement(box.getString("p_isbestreport"+i));
			
			v_minusScore.addElement(box.getString("p_minusscore"+i));
			v_getScore.addElement(box.getString("p_getscore"+i));
			
		}

		sql1  = "\r\n UPDATE tu_projrep SET   ";
		sql1 += "\r\n       score = ?         ";
		sql1 += "\r\n     , isbestreport = ?  ";
		sql1 += "\r\n     , tutordate =  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') , minusscore  = ? , getscore  = ?";
		sql1 += "\r\n WHERE 1=1 ";
		sql1 += "\r\n   AND grcode    = ? ";
		sql1 += "\r\n   AND subj    = ? ";
		sql1 += "\r\n   AND year    = ? ";
		sql1 += "\r\n   AND subjseq = ? ";
		sql1 += "\r\n   AND class   = ? ";
		sql1 += "\r\n   AND ordseq  = ? ";
		sql1 += "\r\n   AND projid  = ? ";
		//sql1 += "\r\n   AND seq     = ? ";


    sql2  = "\r\n INSERT INTO TU_PROJREP ";
    sql2 += "\r\n     (GRCODE, SUBJ, YEAR, SUBJSEQ, CLASS, ORDSEQ, PROJID, ";
    sql2 += "\r\n     SCORE, ISBESTREPORT, TUTORDATE,SEQ,TITLE,minusscore,getscore) ";
    sql2 += "\r\n   VALUES ";
    sql2 += "\r\n     (?, ?, ?, ?, ?, ?, ?, ";
    sql2 += "\r\n     ?, ?,  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS'),?,? ,?,?) ";

		try
		{
			db = new DatabaseExecute(box);

			for(int i=0; i<v_size; i++) {

			PreparedBox pbox1 = new PreparedBox("preparedbox");

  			int lmt_Idx = 1;
  			pbox1.setFloat(lmt_Idx++, box.getFloat("p_finalscore"+i));
  			pbox1.setString(lmt_Idx++, box.getStringDefault("p_isbestreport"+i,"N"));
  			pbox1.setInt(lmt_Idx++, box.getInt("p_minusscore"+i));
  			//pbox1.setInt(lmt_Idx++, box.getInt("p_getscore"+i));
  			pbox1.setFloat(lmt_Idx++, box.getFloat("p_getscore"+i));

  			pbox1.setString(lmt_Idx++, v_grcode);
  			pbox1.setString(lmt_Idx++, v_subj);
  			pbox1.setString(lmt_Idx++, v_year);
  			pbox1.setString(lmt_Idx++, v_subjseq);
  			pbox1.setString(lmt_Idx++, v_class);
  			pbox1.setInt(lmt_Idx++, Integer.parseInt(v_ordseq));
  			pbox1.setString(lmt_Idx++, box.getString("p_projid"+i));
  			
  			
  			//pbox1.setVector(lmt_Idx++, v_seqVec);

  			//System.out.println("KK1 : " + box.getFloat("p_finalscore"+i)); 
  			///System.out.println("KK2 : " + box.getStringDefault("p_isbestreport"+i,"N"));  
  			//System.out.println("KK3 : " + box.getString("p_projid"+i)); 
 
  			isCommit = db.executeUpdate(new PreparedBox [] {pbox1}, new String [] {sql1});

        if(!isCommit) {

          pbox1 = new PreparedBox("preparedbox");

          lmt_Idx = 1;
    			pbox1.setString(lmt_Idx++, v_grcode);
    			pbox1.setString(lmt_Idx++, v_subj);
    			pbox1.setString(lmt_Idx++, v_year);
    			pbox1.setString(lmt_Idx++, v_subjseq);
    			pbox1.setString(lmt_Idx++, v_class);
    			pbox1.setInt(lmt_Idx++, Integer.parseInt(v_ordseq));
    			pbox1.setString(lmt_Idx++, box.getString("p_projid"+i));
    			pbox1.setFloat(lmt_Idx++, box.getFloat("p_finalscore"+i));
    			pbox1.setString(lmt_Idx++, box.getStringDefault("p_isbestreport"+i,"N"));
      			pbox1.setInt(lmt_Idx++, 0);
      			pbox1.setString(lmt_Idx++, " ");
      			pbox1.setInt(lmt_Idx++, box.getInt("p_minusscore"+i));
      			pbox1.setFloat(lmt_Idx++, box.getFloat("p_getscore"+i));
      			/*
    			System.out.println("KK4 : " + v_grcode); 
    			System.out.println("KK4 : " + v_subj); 
    			System.out.println("KK4 : " + v_year); 
    			System.out.println("KK4 : " + v_subjseq); 
    			System.out.println("KK4 : " + v_class); 
    			System.out.println("KK4 : " + Integer.parseInt(v_ordseq)); 
    			 System.out.println("KK4 : " + box.getFloat("p_finalscore"+i)); 
    			 System.out.println("KK5 : " + box.getStringDefault("p_isbestreport"+i,"N"));  
    			 System.out.println("KK6 : " + box.getString("p_projid"+i));     			
    			*/
    			isCommit = db.executeUpdate(new PreparedBox [] {pbox1}, new String [] {sql2});

        }
			}
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
			throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
		}
		return isCommit;
	}

	/**
	??f?? f?????.
	@param RequestBox box
	@return boolean
	*/
	public boolean insertReport(RequestBox box) throws Exception
	{
		DataBox dbox = null;
		String sql = "";
		String sql1 = "";
		String sql2 = "";
		boolean isCommit = false;
		DatabaseExecute db = null;

		char a = (char)2;
    char b = (char)3;

		String v_grcode  = box.getString("p_grcode");
		String v_subj 	 = box.getString("p_subj");
		String v_year 	 = box.getString("p_year");
		String v_subjseq = box.getString("p_subjseq");
		String v_class 	 = box.getString("p_class");

		String v_ordseq       = box.getString("p_ordseq");
		String v_weeklyseq    =  box.getString("p_weeklyseq");
		String v_weeklysubseq = box.getString("p_weeklysubseq");

		String s_userid = box.getSession("userid");

		String v_title    = box.getString("p_title");
		String v_contents = box.getString("p_contents");
		String v_files   = box.getString("p_files");

		/*
		Vector realFileNames = box.getRealFileNames("p_upfile");
		Vector newFileNames   = box.getNewFileNames("p_upfile");
		*/
		//----------------------   업로드되는 경로 -------------------------------
    String v_filepath = UploadUtil.performMakeFilepath("P", box);

    //----------------------   업로드되는 파일 -------------------------------
    String v_filesArr [] = v_files.split("["+b+"]");

    Vector realFileNames = new Vector();
    Vector newFileNames  = new Vector();
    Vector fileSizes     = new Vector();
    Vector fileseqs      = new Vector();

    if (!v_files.equals("")){
        for(int i=0, j=v_filesArr.length; i < j; i++) {
            String v_filename []  = v_filesArr[i].split("["+a+"]");
            fileseqs.add(new Integer(i+1));
            realFileNames.add(v_filename[0]);
            newFileNames.add(v_filename[1]);
            fileSizes.add(v_filename[2]);
        }
    }

		/*
		sql  = "\r\n SELECT ISNULL(MAX(seq), 0)+1 seq FROM TU_PROJREP ";
		sql += "\r\n  WHERE 1=1 ";
		sql += "\r\n    AND GRCODE = "+ StringManager.makeSQL(v_grcode);
		sql += "\n\r    AND subj = " 		 + StringManager.makeSQL(v_subj);
    sql += "\n\r    AND year = " 		 + StringManager.makeSQL(v_year);
    sql += "\n\r    AND subjseq = "  + StringManager.makeSQL(v_subjseq);
    sql += "\n\r    AND class = " 	 + StringManager.makeSQL(v_class);
		sql += "\r\n    AND ordseq = "   + StringManager.makeSQL(v_ordseq);
		sql += "\r\n    AND projid = "   + StringManager.makeSQL(s_userid);
		*/

		// 제출 시 기본점수 가져오기
		sql2  = "\r\n SELECT SUBMITSCORE FROM TU_PROJORD ";
		sql2 += "\r\n  WHERE 1=1 ";
		sql2 += "\r\n    AND GRCODE = "+ StringManager.makeSQL(v_grcode);
		sql2 += "\n\r    AND subj = "+ StringManager.makeSQL(v_subj);
		sql2 += "\n\r    AND year = "+ StringManager.makeSQL(v_year);
		sql2 += "\n\r    AND subjseq = "+ StringManager.makeSQL(v_subjseq);
		sql2 += "\n\r    AND class = "+ StringManager.makeSQL(v_class);
		sql2 += "\r\n    AND ordseq = "+ v_ordseq;

		sql  = "\r\n UPDATE TU_PROJREP SET ";
		sql += "\r\n     SCORE = ?, SEQ=1, ";
		sql += "\r\n     WEEKLYSEQ = ?, ";
		sql += "\r\n     WEEKLYSUBSEQ = ?, ";
		sql += "\r\n     TITLE = ?, ";
		sql += "\r\n     CONTENTS = ?, ";
		sql += "\r\n     REALFILE = ?, ";
		sql += "\r\n     NEWFILE = ?, ";
		sql += "\r\n     REALFILESIZE = ?, ";
		sql += "\r\n     LUSERID = ?, ";
		sql += "\r\n     LDATE =  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS')  ";
		sql += "\r\n   WHERE GRCODE = ? ";
		sql += "\r\n     AND SUBJ = ? ";
		sql += "\r\n     AND YEAR = ? ";
		sql += "\r\n     AND SUBJSEQ = ? ";
		sql += "\r\n     AND CLASS = ? ";
		sql += "\r\n     AND ORDSEQ = ? ";
		sql += "\r\n     AND PROJID = ? ";

		//
		sql1  = " INSERT INTO TU_PROJREP (                                                                     ";
		sql1 += "       GRCODE, subj, year, subjseq, class, ordseq,seq, projid, score,                              ";
		sql1 += "       weeklyseq, weeklysubseq, title, contents, realfile, newfile, realfilesize, luserid, ldate            ";
		sql1 += " ) VALUES(                                                                                    ";
		sql1 += "       ?, ?, ?, ?, ?, ?,1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,                                 ";
		sql1 += "        TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') )";

		try
		{
			db = new DatabaseExecute(box);

			/*
			dbox = db.executeQuery(box, sql);
			int v_seq = dbox.getInt("d_seq");
			*/

			dbox = db.executeQuery(box, sql2);
			float v_submitscore = dbox.getFloat("d_submitscore");

			PreparedBox pbox1 = new PreparedBox("preparedbox");

			int lmt_Idx = 1;

			pbox1.setFloat(lmt_Idx++, v_submitscore);
			pbox1.setString(lmt_Idx++, v_weeklyseq);
			pbox1.setString(lmt_Idx++, v_weeklysubseq);
			pbox1.setString(lmt_Idx++, v_title);
			pbox1.setString(lmt_Idx++, v_contents);
			if(realFileNames.size() == 0)
			{
				pbox1.setString(lmt_Idx++, "");
				pbox1.setString(lmt_Idx++, "");
				pbox1.setInt(lmt_Idx++, 0);
			}
			else		//÷???????? ??;?????? Vector ?? ??´?.
			{
				pbox1.setVector(lmt_Idx++, realFileNames);
				pbox1.setVector(lmt_Idx++, newFileNames);
				pbox1.setVector(lmt_Idx++, fileSizes);
			}
			pbox1.setString(lmt_Idx++, s_userid);

			pbox1.setString(lmt_Idx++, v_grcode);
			pbox1.setString(lmt_Idx++, v_subj);
			pbox1.setString(lmt_Idx++, v_year);
			pbox1.setString(lmt_Idx++, v_subjseq);
			pbox1.setString(lmt_Idx++, v_class);
			pbox1.setInt(lmt_Idx++, Integer.parseInt(v_ordseq));
			pbox1.setString(lmt_Idx++, s_userid);

			isCommit = db.executeUpdate(new PreparedBox [] {pbox1},  new String [] {sql});

			if(!isCommit) {

			 pbox1 = new PreparedBox("preparedbox");

  			lmt_Idx = 1;

  			pbox1.setString(lmt_Idx++, v_grcode);
  			pbox1.setString(lmt_Idx++, v_subj);
  			pbox1.setString(lmt_Idx++, v_year);
  			pbox1.setString(lmt_Idx++, v_subjseq);
  			pbox1.setString(lmt_Idx++, v_class);
  			pbox1.setInt(lmt_Idx++, Integer.parseInt(v_ordseq));
  			pbox1.setString(lmt_Idx++, s_userid);
  //			pbox1.setInt(lmt_Idx++, v_seq);
  			pbox1.setFloat(lmt_Idx++, v_submitscore);
  			pbox1.setString(lmt_Idx++, v_weeklyseq);
  			pbox1.setString(lmt_Idx++, v_weeklysubseq);
  			pbox1.setString(lmt_Idx++, v_title);
  			pbox1.setString(lmt_Idx++, v_contents);
  			if(realFileNames.size() == 0)
  			{
  				pbox1.setString(lmt_Idx++, "");
  				pbox1.setString(lmt_Idx++, "");
  				pbox1.setInt(lmt_Idx++, 0);
  			}
  			else		//÷???????? ??;?????? Vector ?? ??´?.
  			{
  				pbox1.setVector(lmt_Idx++, realFileNames);
  				pbox1.setVector(lmt_Idx++, newFileNames);
  				pbox1.setVector(lmt_Idx++, fileSizes);
  			}
  			pbox1.setString(lmt_Idx++, s_userid);

  			isCommit = db.executeUpdate(new PreparedBox [] {pbox1},  new String [] {sql1});

			}
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
			throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
		}
		return isCommit;
	}

	/**
	f??? ??f ????; v????.
	@param RequestBox box
	@return DataBox
	*/
	public DataBox selectViewReport(RequestBox box) throws Exception
	{
		DatabaseExecute db = null;
		DataBox dbox = null;
		String sql = "";

		try
		{
			db = new DatabaseExecute();

			String s_userid   = box.getSession("userid");
			String s_language = box.getSession("languageName");

	      String v_grcode  = box.getString("p_grcode");
	      String v_subj 	 = box.getString("p_subj");
	      String v_year 	 = box.getString("p_year");
	      String v_subjseq = box.getString("p_subjseq");
	      String v_class 	 = box.getString("p_class");

			String v_ordseq   = box.getString("p_ordseq");
			String v_reptype  = box.getString("p_reptype");
			String s_hakbun   = box.getSession("hakbun");

			sql += "\r\n SELECT                                                                         ";
			sql += "\r\n        a.seq, a.projid, a.ldate, a.ldate, a.ldate, a.realfile,                 ";
			sql += "\r\n        a.newfile, a.realfilesize, a.title, a.contents, a.score finalscore,                     ";
			sql += "\r\n        a.isbestreport, a.tutorcontents, a.tutorrealfile,                       ";
			sql += "\r\n        a.tutornewfile, a.tutordate,                                            ";
			sql += "\r\n        get_name(a.projid) name,                                            ";
			sql += "\r\n        GET_DANGWA(a.projid, 'KOREAN') hakgwanm,                                      ";
			sql += "\r\n        CASE WHEN c.reexpiredate= ''                       ";
			// sql += "\r\n        CASE WHEN (a.LDATE <= C.EXPIREDATE)                       ";
			sql += "\r\n             THEN CASE WHEN  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') <= c.expiredate AND  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') >= c.startdate      ";
			sql += "\r\n	                     THEN 'Y' ELSE 'N'                                        ";
			sql += "\r\n	              END                                                     ";
			sql += "\r\n             ELSE CASE WHEN  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') <= c.expiredate AND  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') >= c.startdate  ";
			sql += "\r\n	                     THEN 'Y' ELSE                                         ";
			sql += "\r\n                             CASE WHEN  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') <= c.reexpiredate AND  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') >= c.restartdate ";  
			sql += "\r\n                                 THEN 'Y' ELSE 'N'                                        ";
			sql += "\r\n                             END                                                         ";
			sql += "\r\n	              END                                                     ";
			sql += "\r\n        END indate, c.submitfiletype                                                              ";
			sql += "\r\n  FROM TU_PROJREP a                                                          ";
			sql += "\r\n        INNER JOIN tu_projord c ON (a.subj = c.subj) ";
			sql += "\r\n 															    AND (a.year = c.year) ";
			sql += "\r\n 															    AND (a.subjseq = c.subjseq) ";
			sql += "\r\n 															    AND (a.class = c.class) ";
			sql += "\r\n 															    AND (a.ordseq = c.ordseq) and c.reptype='P'  ";
			sql += "\r\n  WHERE 1=1 ";
			sql += "\n\r    AND a.grcode = " 		+ StringManager.makeSQL(v_grcode);
			sql += "\n\r    AND a.subj = " 		  + StringManager.makeSQL(v_subj);
			sql += "\n\r    AND a.year = " 		  + StringManager.makeSQL(v_year);
			sql += "\n\r    AND a.subjseq = "   + StringManager.makeSQL(v_subjseq);
			sql += "\n\r    AND a.class = " 	  + StringManager.makeSQL(v_class);
			sql += "\r\n    AND a.ordseq = "    + v_ordseq;
			 if(v_grcode.equals("N001001")){
				 sql+="\r\n        AND substr(a.projid, 1, 9)  = " + StringManager.makeSQL(s_hakbun);
	         }else{
	        	 sql+="\r\n        AND a.projid  = " + StringManager.makeSQL(s_userid);
	         }
			
			// sql += "\r\n    AND a.projid = "    + StringManager.makeSQL(s_userid);
			sql += "\r\n  ORDER BY seq DESC ";

			//----------------------   ???γ??? v? ----------------------------
			dbox = db.executeQuery(box, sql);
			System.out.println("sql:::::: "+sql);

		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		return dbox;
	}

	/**
	f??? ??f ????; ??d???.
	@param RequestBox box
	@return boolean
	*/
	public boolean updateReport(RequestBox box) throws Exception
	{
		boolean isCommit = false;
		DatabaseExecute db = null;
		DataBox dbox = null;
		String sql = "";
		String sql1 = "";
		
		
		char a = (char)2;
		char b = (char)3;

	String s_userid   = box.getSession("userid");

    String v_grcode  = box.getString("p_grcode");
    String v_subj 	 = box.getString("p_subj");
    String v_year 	 = box.getString("p_year");
    String v_subjseq = box.getString("p_subjseq");
    String v_class 	 = box.getString("p_class");

		String v_ordseq = box.getString("p_ordseq");
		String v_projid = box.getString("p_projid");
//		String v_seq    = box.getString("p_seq");

		String v_weeklyseq    = box.getString("p_weeklyseq");
		String v_weeklysubseq = box.getString("p_weeklysubseq");

		String v_title    = box.getString("p_title");
		String v_contents = box.getString("p_contents");
		String v_files   = box.getString("p_files");
		
		

		//----------------------   삭제되는 파일 --------------------------------
    
    int	  v_upfilecnt     = box.getInt("p_upfilecnt");	//	서버에 저장되있는 파일수

    Vector v_savefile      =	new	Vector();
    Vector v_filesequence  =	new	Vector();

    for(int	i =	0; i < v_upfilecnt;	i++) {
      if(	!box.getString("p_fileseq" + i).equals(""))	{
        v_savefile.addElement(box.getString("p_savefile" + i));    // 서버에 저장되있는 파일명 중에서 삭제할 파일들
        v_filesequence.addElement(box.getString("p_fileseq"	+ i)); // 서버에 저장되있는 파일번호 중에서 삭제할 파일들
      }
    }
		/*
		Vector realFileNames  = box.getRealFileNames("p_upfile");
		Vector newFileNames   = box.getNewFileNames("p_upfile");
		*/
		//----------------------   업로드되는 경로 -------------------------------
    String v_filepath = UploadUtil.performMakeFilepath("P", box);

    //----------------------   업로드되는 파일 -------------------------------
    String v_filesArr [] = v_files.split("["+b+"]");

    Vector realFileNames = new Vector();
    Vector newFileNames  = new Vector();
    Vector fileSizes     = new Vector();
    Vector fileseqs      = new Vector();

    if (!v_files.equals("")){
      for(int i=0, j=v_filesArr.length; i < j; i++) {
          String v_filename []  = v_filesArr[i].split("["+a+"]");
          fileseqs.add(new Integer(i+1));
          realFileNames.add(v_filename[0]);
          newFileNames.add(v_filename[1]);
          fileSizes.add(v_filename[2]);
      }
      // 새로 저장되는 파일이 있을 경우는 삭제태그 하지 않아도 지운다.
      // v_savefile.addElement(box.getString("p_newfile")); 예외처리 by 도현.. 필요 없는 사항임.
    }
    try
	{	db = new DatabaseExecute(box);

		sql1  = "\r\n SELECT newfile,projid ";
		sql1 += "\r\n  FROM TU_PROJREP ";
		sql1 += "\r\n  WHERE 1=1 ";
		sql1 += "\n\r    AND grcode = " 		+ StringManager.makeSQL(v_grcode);
		sql1 += "\n\r    AND subj = " 		  	+ StringManager.makeSQL(v_subj);
		sql1 += "\n\r    AND year = " 		  	+ StringManager.makeSQL(v_year);
		sql1 += "\n\r    AND subjseq = "   		+ StringManager.makeSQL(v_subjseq);
		sql1 += "\n\r    AND class = " 	  		+ StringManager.makeSQL(v_class);
		sql1 += "\r\n    AND ordseq = "    		+ v_ordseq;
		sql1 += "\r\n    AND projid = "    		+ StringManager.makeSQL(v_projid);
		//sql += "\r\n    AND seq = "       + StringManager.makeSQL(v_seq);
		//System.out.println("sql	:	"+sql1);
		dbox = db.executeQuery(box, sql1);
	   	//System.out.println("111111111111");
		String v_deletefile    = "";
		
		if(dbox != null)
		{
			v_deletefile = dbox.getString("d_newfile");
			File del = null;
			del = new File(dirUploadStuReport+v_filepath+"/"+v_deletefile);
			if (del.exists()) del.delete();
		}
	
	
		sql  = "\r\n UPDATE TU_PROJREP SET ";
		sql += "\r\n       title     = ?   ";
		sql += "\r\n     , contents  = ?   ";		
		sql += "\r\n     , realfile = ?     ";
  		sql += "\r\n     , newfile = ?     ";
  		sql += "\r\n     , realfilesize = ?     ";  
		sql += "\r\n     , luserid   = ?   ";
		sql += "\r\n     , ldate     =  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') ";
		sql += "\r\n WHERE 1=1 ";
		sql += "\r\n   AND grcode    = ? ";
		sql += "\r\n   AND subj    = ? ";
		sql += "\r\n   AND year    = ? ";
		sql += "\r\n   AND subjseq = ? ";
		sql += "\r\n   AND class   = ? ";
		sql += "\r\n   AND ordseq  = ? ";
		sql += "\r\n   AND projid  = ? ";
//		sql += "\r\n   AND seq     = ? ";


			PreparedBox pbox = new PreparedBox("preparedbox");

			int lmt_Idx = 1;

			pbox.setString(lmt_Idx++, v_title);
			pbox.setString(lmt_Idx++, v_contents);
			if(realFileNames.size() > 0){
				pbox.setVector(lmt_Idx++, realFileNames);
				pbox.setVector(lmt_Idx++, newFileNames);
				pbox.setVector(lmt_Idx++, fileSizes);
			} else if(v_filesequence.size() > 0){ // 삭제
				pbox.setString(lmt_Idx++, "");
				pbox.setString(lmt_Idx++, "");
				pbox.setInt(lmt_Idx++, 0);
			}else{
				pbox.setString(lmt_Idx++, "");
				pbox.setString(lmt_Idx++, "");
				pbox.setInt(lmt_Idx++, 0);
			}
			pbox.setString(lmt_Idx++, s_userid);
			pbox.setString(lmt_Idx++, v_grcode);
			pbox.setString(lmt_Idx++, v_subj);
			pbox.setString(lmt_Idx++, v_year);
			pbox.setString(lmt_Idx++, v_subjseq);
			pbox.setString(lmt_Idx++, v_class);
			pbox.setInt(lmt_Idx++, Integer.parseInt(v_ordseq));
			pbox.setString(lmt_Idx++, v_projid);
//			pbox.setString(lmt_Idx++, v_seq);


			isCommit = db.executeUpdate(new PreparedBox [] {pbox},  new String [] {sql});

			//첨부파일을 삭제한다.
      //if(isCommit){
    	//  System.out.println("여기는 오는게냐? 응?~~~~~");
      //  FileManager.deleteFile("P", v_savefile, box);
     // }
			/*	
		if(isCommit)
		{
			v_deletefile = dbox.getString("d_newfile");
			File del = new File(dirUploadDefault+File.separator+v_grcode+File.separator+v_subj+File.separator+v_year+File.separator+v_subjseq+File.separator+v_class+File.separator+v_ordseq+File.separator+v_deletefile);
			if (del.exists())
			{
				del.delete();
			}
		}
		
			if(isCommit && realFileNames.size() > 0)
			{
				v_savefile = dbox.getString("d_newfile");
				File del = new File(dirUploadDefault+File.separator+v_subj+File.separator+v_year+File.separator+v_subjseq+File.separator+v_class+File.separator+v_ordseq+File.separator+v_savefile);
				if (del.exists())
				{
					del.delete();
				}
			}
			*/
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
			throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
		}
		return isCommit;
	}

	/**
	f??? ??f ????; ??f???.
	@param RequestBox box
	@return boolean
	*/
	public boolean deleteReport(RequestBox box) throws Exception
	{
		DatabaseExecute db = null;
		DataBox dbox = null;
		boolean isCommit = false;
		String sql = "";
		String sql1 = "";
		String v_savefile = "";

		try
		{
			db = new DatabaseExecute(box);

      String v_grcode  = box.getString("p_grcode");
      String v_subj 	 = box.getString("p_subj");
      String v_year 	 = box.getString("p_year");
      String v_subjseq = box.getString("p_subjseq");
      String v_class 	 = box.getString("p_class");

			String v_ordseq = box.getString("p_ordseq");
			String v_projid = box.getString("p_projid");
			String v_seq    = box.getString("p_seq");

			String v_weeklyseq    = box.getString("p_weeklyseq");
			String v_weeklysubseq = box.getString("p_weeklysubseq");

			//////////////////////////////////   ÷?????? d?? v?  ///////////////////////////////////////////////////////////////////
			sql  = "\r\n SELECT newfile ";
			sql += "\r\n  FROM TU_PROJREP ";
			sql += "\r\n  WHERE 1=1 ";
			sql += "\n\r    AND grcode = " 		+ StringManager.makeSQL(v_grcode);
			sql += "\n\r    AND subj = " 		  + StringManager.makeSQL(v_subj);
			sql += "\n\r    AND year = " 		  + StringManager.makeSQL(v_year);
			sql += "\n\r    AND subjseq = "   + StringManager.makeSQL(v_subjseq);
			sql += "\n\r    AND class = " 	  + StringManager.makeSQL(v_class);
			sql += "\r\n    AND ordseq = "    + v_ordseq;
			sql += "\r\n    AND projid = "    + StringManager.makeSQL(v_projid);
			//sql += "\r\n    AND seq = "       + StringManager.makeSQL(v_seq);

			dbox = db.executeQuery(box, sql);

			//////////////////////////////////   f???f d?? ??f  ///////////////////////////////////////////////////////////////////
			sql1  = "\r\n DELETE FROM TU_PROJREP  ";
			sql1 += "\r\n  WHERE 1=1              ";
			sql1 += "\r\n    AND grcode   = ?     ";
			sql1 += "\r\n    AND subj     = ?     ";
			sql1 += "\r\n    AND year     = ?     ";
			sql1 += "\r\n    AND subjseq  = ?     ";
			sql1 += "\r\n    AND class    = ?     ";
			sql1 += "\r\n    AND ordseq   = ?     ";
			sql1 += "\r\n    AND projid   = ?     ";
			//sql1 += "\r\n    AND seq      = ?     ";

			PreparedBox pbox1 = new PreparedBox("preparedbox");

			pbox1.setString(1, v_grcode);
			pbox1.setString(2, v_subj);
			pbox1.setString(3, v_year);
			pbox1.setString(4, v_subjseq);
			pbox1.setString(5, v_class);
			pbox1.setInt(6, Integer.parseInt(v_ordseq));
			pbox1.setString(7, v_projid);
			//pbox1.setString(8, v_seq);

			isCommit = db.executeUpdate(new PreparedBox [] {pbox1},  new String [] {sql1});

			if(isCommit)
			{
				v_savefile = dbox.getString("d_newfile");
				File del = new File(dirUploadDefault+File.separator+v_grcode+File.separator+v_subj+File.separator+v_year+File.separator+v_subjseq+File.separator+v_class+File.separator+v_ordseq+File.separator+v_savefile);
				if (del.exists())
				{
					del.delete();
				}
			}
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
			throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
		}
		return isCommit;
	}

	/**
	????f ???; ??n?´?.
	@param RequestBox box
	@return ArrayList
	*/
	public ArrayList selectListBestReport(RequestBox box) throws Exception
	{
		DatabaseExecute db = null;
		ArrayList list = null;
		String sql = "";

		try
		{
			db = new DatabaseExecute();

      String v_grcode  = box.getString("p_grcode");
      String v_subj 	 = box.getString("p_subj");
      String v_year 	 = box.getString("p_year");
      String v_subjseq = box.getString("p_subjseq");
      String v_class 	 = box.getString("p_class");

			String v_ordseq = box.getString("p_ordseq");

			// 현재일시 변수선언
			//sql += "\r\n DECLARE @V_CURTIME NVARCHAR(14);        ";
			//sql += "\r\n SET @V_CURTIME =  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS');  ";

			sql += "\r\n SELECT                                                                         ";
			sql += "\r\n        a.seq, a.projid, a.ldate, a.realfile, a.newfile,                        ";
			sql += "\r\n        a.score finalscore, a.isbestreport,                                     ";
			sql += "\r\n        get_name(a.projid) name,                                            ";
			sql += "\r\n        GET_DANGWA(D.COMP, 'KOREAN') hakgwanm,                                      ";

			// 기한내에 제출했는지 여부
			sql += "\r\n        CASE WHEN NVL(c.reexpiredate, 'Null') = 'Null'                       ";
			sql += "\r\n             THEN CASE WHEN a.ldate <= expiredate AND a.ldate >= startdate      ";
			sql += "\r\n	                     THEN 'Y' ELSE 'N'                                        ";
			sql += "\r\n	                END                                                           ";
			sql += "\r\n             ELSE CASE WHEN a.ldate <= reexpiredate AND a.ldate >= restartdate  ";
			sql += "\r\n	                     THEN 'Y' ELSE 'N'                                        ";
			sql += "\r\n	                END                                                           ";
			sql += "\r\n        END indate,                                                              ";

			// 현재가 과제제출 가능시간인지 여부
			sql += "\r\n        CASE WHEN ( TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') >= C.STARTDATE AND  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') <= C.EXPIREDATE) ";
			sql += "\r\n                     OR ( TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') >= C.RESTARTDATE AND  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') <= C.REEXPIREDATE) ";
			sql += "\r\n             THEN 'IN' ELSE 'OUT' END AS INPERIOD ";

			sql += "\r\n  FROM TU_PROJREP a                                                          ";
			sql += "\r\n        INNER JOIN TU_PROJORD c ON (a.subj = c.subj)                             ";
			sql += "\r\n 															and (a.year = c.year)                             ";
			sql += "\r\n 															and (a.subjseq = c.subjseq)                       ";
			sql += "\r\n 															and (a.class = c.class)                           ";
			sql += "\r\n 															and (a.ordseq = c.ordseq) and c.reptype='P'       ";
			sql += "\r\n        INNER JOIN TU_MEMBER D ON (A.PROJID = D.USERID) ";
			sql += "\r\n  WHERE 1=1 ";
			sql += "\n\r    AND a.grcode = " 		+ StringManager.makeSQL(v_grcode);
			sql += "\n\r    AND a.subj = " 		  + StringManager.makeSQL(v_subj);
      sql += "\n\r    AND a.year = " 		  + StringManager.makeSQL(v_year);
      sql += "\n\r    AND a.subjseq = "   + StringManager.makeSQL(v_subjseq);
      sql += "\n\r    AND a.class = " 	  + StringManager.makeSQL(v_class);
			sql += "\r\n    AND a.ordseq = "    + v_ordseq;
			sql += "\r\n    AND a.isbestreport = 'Y'  ";
			sql += "\r\n  ORDER BY projid             ";

			box.put("p_isPage", new Boolean(false));     //    ????? ???? ?????? ?????? ??? ???
			box.put("p_row", new Integer(row));          //    ????? ???? ?????? ?????? ??? ????? ???

			list = db.executeQueryList(box, sql);
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		return list;
	}

	/**
	??u??f ???; ??n?´?.
	@param RequestBox box
	@return ArrayList
	*/
	public ArrayList selectListAllReport(RequestBox box) throws Exception
	{
		DatabaseExecute db = null;
		ArrayList list = null;
		String sql = "";

		try
		{
			db = new DatabaseExecute();

      String v_grcode  = box.getString("p_grcode");
      String v_subj 	 = box.getString("p_subj");
      String v_year 	 = box.getString("p_year");
      String v_subjseq = box.getString("p_subjseq");
      String v_class 	 = box.getString("p_class");

			String v_ordseq = box.getString("p_ordseq");

			// 현재일시 변수선언
			//sql += "\r\n DECLARE @V_CURTIME NVARCHAR(14);        ";
			//sql += "\r\n SET @V_CURTIME = CONVERT(VARCHAR(8),GETDATE(),112) + REPLACE(CONVERT(VARCHAR(8),GETDATE(),114),':','');  ";

			sql += "\r\n SELECT                                                                         ";
			sql += "\r\n        a.seq, a.projid, a.ldate, a.realfile, a.newfile,              ";
			sql += "\r\n        a.score finalscore, a.isbestreport, a.ordseq,                              ";
			sql += "\r\n        get_name(a.projid) name,                                            ";
			sql += "\r\n        GET_DANGWA(trim(D.COMP), 'KOREAN') hakgwanm,                                      ";

			// 기한내에 제출했는지 여부
			sql += "\r\n        CASE WHEN NVL(c.reexpiredate, 'Null') = 'Null'                       ";
			sql += "\r\n             THEN CASE WHEN a.ldate <= expiredate AND a.ldate >= startdate      ";
			sql += "\r\n	                     THEN 'Y' ELSE 'N'                                        ";
			sql += "\r\n	                END                                                           ";
			sql += "\r\n             ELSE CASE WHEN a.ldate <= reexpiredate AND a.ldate >= restartdate  ";
			sql += "\r\n	                     THEN 'Y' ELSE 'N'                                        ";
			sql += "\r\n	                END                                                           ";
			sql += "\r\n        END indate,                                                              ";

			// 현재가 과제제출 가능시간인지 여부
			sql += "\r\n        CASE WHEN ( TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') >= C.STARTDATE AND  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') <= C.EXPIREDATE) ";
			sql += "\r\n                     OR ( TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') >= C.RESTARTDATE AND  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') <= C.REEXPIREDATE) ";
			sql += "\r\n             THEN 'IN' ELSE 'OUT' END AS INPERIOD ";

			sql += "\r\n FROM TU_PROJREP a                                      ";
			sql += "\r\n INNER JOIN TU_PROJORD c ON (a.subj = c.subj)           ";
			sql += "\r\n 															AND (a.year = c.year)        ";
			sql += "\r\n 															AND (a.subjseq = c.subjseq)  ";
			sql += "\r\n 															AND (a.class = c.class)      ";
			sql += "\r\n 															AND (a.ordseq = c.ordseq)    ";
//			sql += "\r\n LEFT JOIN TU_SUBJ AS S ON (a.subj = s.subj)               ";
//			sql += "\r\n INNER JOIN TU_MEMBER  ON (A.PROJID = D.USERID) ";
			sql += "\r\n INNER JOIN TU_MEMBER D ON (A.PROJID = D.USERID) ";   // TU_MEMBER를 D로 지정해줌 20100304
			sql += "\r\n WHERE a.ldate is not null ";
			sql += "\n\r    AND a.grcode = " 		+ StringManager.makeSQL(v_grcode);
			sql += "\n\r    AND a.subj = " 		  + StringManager.makeSQL(v_subj);
      sql += "\n\r    AND a.year = " 		  + StringManager.makeSQL(v_year);
      sql += "\n\r    AND a.subjseq = "   + StringManager.makeSQL(v_subjseq);
      sql += "\n\r    AND a.class = " 	  + StringManager.makeSQL(v_class);
			sql += "\r\n    AND a.ordseq = "    + v_ordseq;
			sql += "\r\n AND c.reptype='P' ";
			sql += "\r\n order by projid ";

			box.put("p_isPage", new Boolean(false));					//    ????? ???? ?????? ?????? ??? ???
			box.put("p_row", new Integer(row));								//    ????? ???? ?????? ?????? ??? ????? ???
			list = db.executeQueryList(box, sql);
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		return list;
	}

	/**
	??f??? ???; ??n?´?.
	@param RequestBox box
	@return ArrayList
	*/
	public ArrayList selectListRecordReport(RequestBox box) throws Exception
	{
		DatabaseExecute db = null;
		ArrayList list = null;
		StringBuffer sbSql = new StringBuffer();

		String s_userid = box.getSession("userid");

		sbSql.append("SELECT A.GRCODE, A.YEAR, A.SUBJSEQ, A.SUBJ, C.SUBJNM, A.CLASS, A.ORDSEQ, A.SEQ, B.TITLE, \r\n"
        ).append("    A.REALFILE, A.NEWFILE, A.REALFILESIZE, A.LDATE, A.SCORE, A.ISBESTREPORT, A.TUTORDATE \r\n"
        ).append("  FROM TU_PROJREP A, TU_PROJORD B LEFT JOIN TU_SUBJ c ON (B.GRCODE = C.GRCODE) AND (B.SUBJ = C.SUBJ) \r\n"
        ).append("  WHERE A.PROJID = '").append(s_userid).append("' \r\n"
        ).append("    AND B.REPTYPE = 'P' \r\n"
        ).append("    AND A.GRCODE = B.GRCODE \r\n"
        ).append("    AND A.SUBJ = B.SUBJ \r\n"
        ).append("    AND A.YEAR = B.YEAR \r\n"
        ).append("    AND A.SUBJSEQ = B.SUBJSEQ \r\n"
        ).append("    AND A.CLASS = B.CLASS \r\n"
        ).append("    AND A.ORDSEQ = B.ORDSEQ \r\n"
        ).append("   AND A.LDATE IS NOT NULL \r\n"
        ).append("  ORDER BY A.YEAR DESC, A.SUBJSEQ DESC, A.SUBJ DESC ");

    /*
    String v_subj 	 = box.getString("p_subj");
    String v_year 	 = box.getString("p_year");
    String v_subjseq = box.getString("p_subjseq");
    String v_class 	 = box.getString("p_class");

		sql  = "\r\n SELECT                                       ";
		sql += "\r\n        CASE WHEN c.reexpiredate = ''                       ";
		sql += "\r\n             THEN CASE WHEN a.ldate <= expiredate AND a.ldate >= startdate      ";
		sql += "\r\n	                     THEN 'Y' ELSE 'N'                                        ";
		sql += "\r\n	                END                                                           ";
		sql += "\r\n             ELSE CASE WHEN a.ldate <= reexpiredate AND a.ldate >= restartdate  ";
		sql += "\r\n	                     THEN 'Y' ELSE 'N'      ";
		sql += "\r\n	                END                         ";
		sql += "\r\n        END indate  ,                         ";
		sql += "\r\n        a.ordseq,                             ";
		sql += "\r\n        a.projid,                             ";
		sql += "\r\n        a.seq,                                ";
		sql += "\r\n        a.weeklyseq,                          ";
		sql += "\r\n        a.weeklysubseq,                       ";
		sql += "\r\n        get_name(a.projid) name,          ";
		sql += "\r\n        GET_DANGWA(a.projid, 'KOREAN') hakgwanm,    ";
		sql += "\r\n        a.ldate,                              ";
		sql += "\r\n        a.realfile,                           ";
		sql += "\r\n        a.newfile,                            ";
		sql += "\r\n        a.score finalscore,                   ";
		sql += "\r\n        a.isbestreport,                       ";
		sql += "\r\n        c.isopenscore                         ";
		sql += "\r\n FROM TU_PROJREP a                         ";
		sql += "\r\n INNER JOIN TU_PROJORD c ON (a.subj = c.subj)       ";
		sql += "\r\n 															and (a.year = c.year)       ";
		sql += "\r\n 															and (a.subjseq = c.subjseq) ";
		sql += "\r\n 															and (a.class = c.class)     ";
		sql += "\r\n 															and (a.ordseq = c.ordseq)   ";

		sql += "\r\n WHERE 1=1 ";
		sql += "\n\r   AND a.subj = " 		+ StringManager.makeSQL(v_subj);
    sql += "\n\r   AND a.year = " 		+ StringManager.makeSQL(v_year);
    sql += "\n\r   AND a.subjseq = "  + StringManager.makeSQL(v_subjseq);
    sql += "\n\r   AND a.class = " 	  + StringManager.makeSQL(v_class);
		sql += "\r\n   AND a.projid = "   + StringManager.makeSQL(s_userid);
		sql += "\r\n   AND c.reptype = 'P' ";
		sql += "\r\n ORDER BY projid ";
    */

		try
		{
			db = new DatabaseExecute();

			box.put("p_isPage", new Boolean(true));
			box.put("p_row", new Integer(row));
//System.out.println("-------------sbSql------------ " + sbSql);
			list = db.executeQueryList(box, sbSql.toString());
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, sbSql.toString());
			throw new Exception("sql = " + sbSql.toString() + "\r\n" + ex.getMessage());
		}
		return list;
	}

	/**
	학생목록 조회
	@param RequestBox box
	@return ArrayList
	*/
	public ArrayList selectStdList(RequestBox box) throws Exception
	{
		DatabaseExecute db = null;
		ArrayList list = null;
		StringBuffer sbSql = new StringBuffer();

		String v_grcode  = box.getString("p_grcode");
		String v_subj 	 = box.getString("p_subj");
    String v_year 	 = box.getString("p_year");
    String v_subjseq = box.getString("p_subjseq");
    String v_class 	 = box.getString("p_class");
    String v_ordseq   = box.getString("p_ordseq");

    sbSql.append("SELECT GET_DANGWA(B.COMP, 'KOREAN') AS DEPT, A.USERID USERID, B.NAME, \r\n"
        ).append("      ( select score from TU_PROJREP where \r\n"
        ).append("              grcode =  'N000001' AND subj = a.SUBJ AND year = a.YEAR \r\n"
        ).append("              AND subjseq = a.SUBJSEQ AND class = a.CLASS and projid = a.userid \r\n"
        ).append("              and ORDSEQ = ").append(v_ordseq).append(" ) score \r\n"
        ).append("  FROM Tz_STUDENT A, Tz_MEMBER B \r\n"
        ).append("  WHERE   \r\n"
        ).append("        A.SUBJ = '").append(v_subj).append("' \r\n"
        ).append("    AND A.YEAR = '").append(v_year).append("' \r\n"
        ).append("    AND A.SUBJSEQ = '").append(v_subjseq).append("' \r\n"
        ).append("    AND A.CLASS = '").append(v_class).append("' \r\n"
        ).append("    AND A.USERID = B.USERID \r\n"
        ).append("  ORDER BY DEPT, USERID ");
//System.out.println("sbSql::"+sbSql.toString());
		try
		{
			db = new DatabaseExecute();
			list = db.executeQueryList(box, sbSql.toString());
		}
		catch (Exception ex)
		{
			ErrorManager.getErrorStackTrace(ex, box, sbSql.toString());
			throw new Exception("sql = " + sbSql.toString() + "\r\n" + ex.getMessage());
		}
		return list;
	}

  /*
  * 점수를 입력한 엑셀파일을 읽어 DB에 저장
  */
  public boolean insertExcelToDB(RequestBox box) throws Exception {

    DatabaseExecute db = null;
    boolean isCommit = false;
    StringBuffer sbSql = new StringBuffer();
    StringBuffer sbSql2 = new StringBuffer();
    int v_execCount = 0;
    Sheet sheet = null;
    Workbook workbook = null;
    char a = (char)2;
    char b = (char)3;

    String s_userid = box.getSession("userid");

    String v_grcode  = box.getString("p_grcode");
    String v_subj 	 = box.getString("p_subj");
    String v_year 	 = box.getString("p_year");
    String v_subjseq = box.getString("p_subjseq");
    String v_class 	 = box.getString("p_class");
    int v_ordseq  = box.getInt("p_ordseq");
    int v_weeklyseq  = box.getInt("p_weeklyseq");
    int v_weeklysubseq  = box.getInt("p_weeklysubseq");
    String v_files   = box.getString("p_files");

		String v_stdId = "";
		String v_score = "";
		String v_isExcellent = "";

		//----------------------   업로드되는 경로 -------------------------------
    String v_filepath = UploadUtil.performMakeFilepath("L", box);

    //----------------------   업로드되는 파일 -------------------------------
    String v_filesArr [] = v_files.split("["+b+"]");

    String realFileNames = "";
    String newFileNames = "";

    if (!v_files.equals("")){
      for(int i=0, j=v_filesArr.length; i < j; i++) {
          String v_filename []  = v_filesArr[i].split("["+a+"]");
          realFileNames = v_filename[0];
          newFileNames = v_filename[1];
      }
    }

		sbSql.append("UPDATE TU_PROJREP SET \r\n"
		    ).append("    ISBESTREPORT = ?, \r\n"
		    ).append("    SCORE = ?, \r\n"
		    ).append("    TUTORDATE =  TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') \r\n"
		    ).append("  WHERE GRCODE = ? \r\n"
		    ).append("    AND SUBJ = ? \r\n"
		    ).append("    AND YEAR = ? \r\n"
		    ).append("    AND SUBJSEQ = ? \r\n"
		    ).append("    AND CLASS = ? \r\n"
		    ).append("    AND ORDSEQ = ? \r\n"
		    ).append("    AND PROJID = ? ");

    sbSql2.append("INSERT INTO TU_PROJREP \r\n"
         ).append("   (GRCODE, SUBJ, YEAR, SUBJSEQ, CLASS, ORDSEQ, PROJID, \r\n"
         ).append("   WEEKLYSEQ, WEEKLYSUBSEQ, TITLE, SCORE, ISBESTREPORT, LUSERID, TUTORDATE) \r\n"
         ).append(" VALUES \r\n"
         ).append("   (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, \r\n"
         ).append("    TO_CHAR(sysdate, 'YYYYMMDDHH24MISS') ) ");

    try {

      // Vector에서 읽어 DB에 넣기
      db = new DatabaseExecute(box);

      // 엑셀파일을 읽어 Vector에 넣기
      File v_file = new File(dirUploadDefault+v_filepath+"/"+newFileNames);
      workbook = Workbook.getWorkbook(v_file);
      sheet = workbook.getSheet(0);

      Vector vec_stdId = new Vector();
      Vector vec_score = new Vector();
      Vector vec_isExcellent = new Vector();
      for(int i=1; i<sheet.getRows(); i++) {

        v_stdId = sheet.getCell(2,i).getContents();
		    v_score = sheet.getCell(4,i).getContents();
    		v_isExcellent = sheet.getCell(5,i).getContents();
    		if(v_isExcellent.equals("1")) {
    		  v_isExcellent = "Y";
    		} else {
    		  v_isExcellent = "";
    		}
    		vec_stdId.add(v_stdId);
    		vec_score.add(v_score);
    		vec_isExcellent.add(v_isExcellent);

      }

      PreparedBox pbox = null;
      for(int i=0; i<vec_stdId.size(); i++) {
        pbox = new PreparedBox("preparedbox");

        pbox.setString(1, (String)vec_isExcellent.elementAt(i));
        pbox.setFloat(2, Float.parseFloat((String)vec_score.elementAt(i)));
        pbox.setString(3, v_grcode);
        pbox.setString(4, v_subj);
        pbox.setString(5, v_year);
        pbox.setString(6, v_subjseq);
        pbox.setString(7, v_class);
        pbox.setInt(8, v_ordseq);
        pbox.setString(9, (String)vec_stdId.elementAt(i));

        isCommit = db.executeUpdate(new PreparedBox [] {pbox},  new String [] {sbSql.toString()});

        if(!isCommit) { // update가 안되는 건은 교수자처리로 insert해야함.
          pbox = new PreparedBox("preparedbox");

          pbox.setString(1, v_grcode);
          pbox.setString(2, v_subj);
          pbox.setString(3, v_year);
          pbox.setString(4, v_subjseq);
          pbox.setString(5, v_class);
          pbox.setInt(6, v_ordseq);
          pbox.setString(7, (String)vec_stdId.elementAt(i));

          pbox.setInt(8, v_weeklyseq);
          pbox.setInt(9, v_weeklysubseq);
          pbox.setString(10, "[교수 Excel 일괄처리]");
          pbox.setFloat(12, Float.parseFloat((String)vec_score.elementAt(i)));
          pbox.setString(12, (String)vec_isExcellent.elementAt(i));
          pbox.setString(13, s_userid);

          isCommit = db.executeUpdate(new PreparedBox [] {pbox},  new String [] {sbSql2.toString()});

          if(isCommit) v_execCount++;

        } else v_execCount++;

      }

      if(vec_stdId.size() == v_execCount) {
        isCommit = true;
      } else {
        isCommit = false;
      }

      // 무조건 지운다.
      if (v_file.exists()) {
        v_file.delete();
      }

    } catch(Exception ex) {
      ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
      throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
    }
    return isCommit;
  }

  /*
  * 엑셀파일 만들기
  */
  public void createExcelFile(RequestBox box) throws FileNotFoundException, IOException, WriteException, Exception {

    File v_file = null;
    WritableWorkbook workbook = null;
    WritableSheet sheet = null;
    WritableCellFormat titleFormat = null;
    WritableCellFormat dataFormat = null;

    String s_userid = box.getSession("userid");

    String now = (new SimpleDateFormat("yyyyMMddHHmmSSS")).format(new Date());

    String v_grcode 	 = box.getString("p_grcode");
    String v_subj 	 = box.getString("p_subj");
    String v_year 	 = box.getString("p_year");
    String v_subjseq = box.getString("p_subjseq");
    String v_class 	 = box.getString("p_class");

    String v_weeklyseq     = box.getString("p_weeklyseq");
  	String v_weeklysubseq  = box.getString("p_weeklysubseq");

    String v_filepath = UploadUtil.performMakeFilepath("L", box); // 업로드되는 경로
    //System.out.println("v_filepath : "+ v_filepath);
    String v_fileNames = "ReportProfServlet_students_"+ s_userid +".xls";
    String v_downNames = v_year +"_"+ v_subjseq +"_"+ v_subj +"_"+ v_class +"_"+ v_weeklyseq +"_"+ v_weeklysubseq +"_Students.xls";

    ArrayList list = selectReportStdList(box);

    try {

      /** 폴더 없을 경우 폴더 생성[시작] *******************************************/
      String v_updir = (new ConfigSet()).getProperty("dir.upload.reportprof");
      String [] tempArr = v_filepath.split("/");

      //System.out.println("EEE1 : "+v_fileNames);
      
      //System.out.println("EEE2 : "+v_downNames);
      if (tempArr.length > 1) {
          //System.out.println("EEE211 : "+v_updir);
    	  File upFile = new java.io.File(v_updir);

        for ( int i=1; i<tempArr.length; i++) {
            //System.out.println("EEE222 : "+ tempArr[i]);
          upFile = RequestManager.makeUploadDir(upFile, tempArr[i]);
        }
      }
      /** 폴더 없을 경우 폴더 생성[끝] ********************************************/
      //System.out.println("EEE233: "+ dirUploadDefault + v_filepath + "/"+ v_fileNames);
      v_file = new File(dirUploadDefault + v_filepath + "/"+ v_fileNames);

      if (v_file.exists()) {
        v_file.delete();
      }

      v_file = new File(dirUploadDefault + v_filepath + "/"+ v_fileNames);
      //System.out.println("EEE555: ");
//      System.out.println(dirUploadDefault + v_filepath + "/"+ v_fileNames);
      workbook = Workbook.createWorkbook(v_file);
      //System.out.println("EEE666 : ");
      sheet = workbook.createSheet("과제물 제출 목록", 0);
      
      //System.out.println("EEE3 : "+v_filepath);
      
      sheet.setColumnView(0, 8);
      sheet.setColumnView(1, 20);
      sheet.setColumnView(2, 15);
      sheet.setColumnView(3, 15);
      sheet.setColumnView(4, 15);
      sheet.setColumnView(5, 40);
      sheet.setColumnView(6, 10);
      sheet.setColumnView(7, 10);

      titleFormat = new WritableCellFormat();
      dataFormat = new WritableCellFormat();

      titleFormat.setAlignment(Alignment.CENTRE);
      titleFormat.setBackground(Colour.LIGHT_GREEN);
      titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
      dataFormat.setAlignment(Alignment.CENTRE);

      sheet.addCell(new Label(0, 0, "No.", titleFormat));
      sheet.addCell(new Label(1, 0, "제출상태", titleFormat));
      sheet.addCell(new Label(2, 0, "아이디", titleFormat));
      sheet.addCell(new Label(3, 0, "이름", titleFormat));
      sheet.addCell(new Label(4, 0, "제출일", titleFormat));
      sheet.addCell(new Label(5, 0, "제출과제물", titleFormat));
      sheet.addCell(new Label(6, 0, "감점", titleFormat));
      sheet.addCell(new Label(7, 0, "취득점수", titleFormat));

      String v_dept = "";
      String v_userid = "";
      String v_name = "";
      String v_score = "";
      for(int i=0; i<list.size(); i++) {
        DataBox dbox = (DataBox)list.get(i);

        v_dept = dbox.getString("d_projid");
        v_userid = dbox.getString("d_scoreexists");
        v_name = dbox.getString("d_name");
        v_score = dbox.getString("d_score");

        sheet.addCell(new Label(0, (i+1), ""+ (i+1), dataFormat));
        sheet.addCell(new Label(1, (i+1), dbox.getString("d_scoreexists"), dataFormat));
        sheet.addCell(new Label(2, (i+1), dbox.getString("d_projid"), dataFormat));
        sheet.addCell(new Label(3, (i+1), dbox.getString("d_name"), dataFormat));
        sheet.addCell(new Label(4, (i+1), dbox.getString("d_ldate"), dataFormat));
        sheet.addCell(new Label(5, (i+1), dbox.getString("d_realfile"), dataFormat));
        sheet.addCell(new Label(6, (i+1), dbox.getString("d_minusday"), dataFormat));
        sheet.addCell(new Label(7, (i+1), dbox.getString("d_score"), dataFormat));
      }

      workbook.write();
      workbook.close();

      box.put("p_savefile", v_fileNames);
      box.put("p_realfile", v_downNames);
      box.put("p_filepath", v_filepath);

    } catch(Exception ex) {
      ErrorManager.getErrorStackTrace(ex, box, ex.getMessage());
      throw new Exception(ex.getMessage());
    }
  }
  
  
  /*
   * 점수를 입력한 엑셀파일을 읽어 DB에 저장
   */
   public boolean insertExcelToDBNew(RequestBox box) throws Exception {

     DatabaseExecute db = null;
     boolean isCommit = false;
     StringBuffer sbSql = new StringBuffer();
     StringBuffer sbSql2 = new StringBuffer();
     int v_execCount = 0;
     Sheet sheet = null;
     //Workbook workbook = null;
     char a = (char)2;
     char b = (char)3;
     String v_grcode  = box.getString("p_grcode");
     String v_subj 	 = box.getString("p_subj");
     String v_year 	 = box.getString("p_year");
     String v_subjseq = box.getString("p_subjseq");
     String v_class 	 = box.getString("p_class");
     int v_ordseq  = box.getInt("p_ordseq");
     String v_files   = box.getString("p_files");
     int upCnt=0;

    //----------------------   업로드되는 경로 -------------------------------
     String v_filepath = UploadUtil.performMakeFilepath("L", box);
    //----------------------   업로드되는 파일 -------------------------------
     String v_filesArr [] = v_files.split("["+b+"]");

     String realFileNames = "";
     String newFileNames = "";

     if (!v_files.equals("")){
       for(int i=0, j=v_filesArr.length; i < j; i++) {
           String v_filename []  = v_filesArr[i].split("["+a+"]");
           realFileNames = v_filename[0];
           newFileNames = v_filename[1];
       }
     }
		sbSql.append("UPDATE TU_PROJREP SET \r\n"
		).append("    SCORE = ?, \r\n"
		).append("    getSCORE = ? \r\n"
	    ).append("  WHERE GRCODE = ? \r\n"
	    ).append("    AND SUBJ = ? \r\n"
	    ).append("    AND YEAR = ? \r\n"
	    ).append("    AND SUBJSEQ = ? \r\n"
	    ).append("    AND CLASS = ? \r\n"
	    ).append("    AND ORDSEQ = ? \r\n"
	    ).append("    AND PROJID = ? "
	    ).append("    AND luserid is not null ");
		
     try {

       // Vector에서 읽어 DB에 넣기
       db = new DatabaseExecute(box);

       // 엑셀파일을 읽어 Vector에 넣기
       File v_file = new File(dirUploadReportResult+v_filepath+"/"+newFileNames);       
       Workbook workbook = Workbook.getWorkbook(v_file);       
       sheet = workbook.getSheet(0);
       
       
       Vector vec_stdId = new Vector();
       Vector vec_score = new Vector();
       Vector vec_getscore = new Vector();
       
       String score="";
       String scoreMinus="";
       for(int i=1; i<sheet.getRows(); i++) {
           
           vec_stdId.add(sheet.getCell(0,i).getContents());
           score = sheet.getCell(1,i).getContents();
           scoreMinus = sheet.getCell(2,i).getContents();
           vec_score.add(String.valueOf(Double.valueOf(score)+Double.valueOf(scoreMinus)));
           vec_getscore.add(score);
       }
       
       
       PreparedBox pbox = null;
       System.out.println("size::"+vec_stdId.size());
       for(int i=0; i<vec_stdId.size(); i++) {
         pbox = new PreparedBox("preparedbox");
         
         pbox.setFloat(1, Float.parseFloat((String)vec_score.elementAt(i)));
         pbox.setFloat(2, Float.parseFloat((String)vec_getscore.elementAt(i)));
         pbox.setString(3, v_grcode);
         pbox.setString(4, v_subj);
         pbox.setString(5, v_year);
         pbox.setString(6, v_subjseq);
         pbox.setString(7, v_class);
         pbox.setInt(8, v_ordseq);
         pbox.setString(9, (String)vec_stdId.elementAt(i));

         isCommit = db.executeUpdate(new PreparedBox [] {pbox},  new String [] {sbSql.toString()});
         
         if(isCommit){
        	 upCnt++;
         }
         
         System.out.println("upCnt::"+upCnt);
       }
       
       //isCommit 셋팅
       if(upCnt > 0){
    	   isCommit = true;
       }else{
    	   isCommit = false;
       }
       
       // 무조건 지운다.
       if (v_file.exists()) {
         v_file.delete();
       }

     } catch(Exception ex) {
       ErrorManager.getErrorStackTrace(ex, box, db.getSQLString());
       throw new Exception("sql = " + db.getSQLString() + "\r\n" + ex.getMessage());
     }
     return isCommit;
   }
}