/**
 * 학습이력 관리
 * @author LEE SEUNG LEE
 * @version 1.0
 */

package com.ziaan.mypage;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DatabaseExecute;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.RequestBox;

public class HistoryUserBean {
    private ConfigSet config;
    private int row;
    	
    public HistoryUserBean() {
        try{
            config = new ConfigSet();  
            row = Integer.parseInt(config.getProperty("page.bulletin.row"));        //        이 모듈의 페이지당 row 수를 셋팅한다
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /** 
    성적조회 목록을 가져온다.
    @param RequestBox box 
    @return ArrayList
    */
    public ArrayList selectListScore(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        ArrayList list = null;
        String sql = "";

        try {            
            db = new DatabaseExecute();
            
		String s_userid   = box.getSession("userid");

		sql = "select 1 from dual ";
//		
//		sql = "select a.subjnm, b.*, c.subjCnt, substr(a.subj,0,2) subjgbn, d.comp ";
//		sql += "from tu_subjseq a, tu_member d, ";
//		sql += "(";
//		sql += "select substr(suyear,0,4) year, substr(suyear,5,2) subjseq, haksu subj, bunno class, isugbn, hakjum, grade, hkhakno ";
//		sql += "from susinch@ssw.haksa ";
//		sql += "where hkhakno = '" + s_userid + "' ";
//		sql += ") b, ";
//		sql += "( ";
//		sql += "select substr(suyear,0,4) year, substr(suyear,5,2) subjseq, count(*) subjCnt "; 
//		sql += "from susinch@ssw.haksa ";
//		sql += "where hkhakno = '" + s_userid + "' ";
//		sql += "group by substr(suyear,0,4), substr(suyear,5,2) ";
//		sql += ") c ";
//		sql += "where a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq and a.class = b.class ";
//		sql += "	  and a.year = c.year and a.subjseq = c.subjseq ";
//		sql += "	  and d.userid = '" + s_userid + "' and b.hkhakno = d.userid ";
//		sql += "order by a.year, a.subjseq, a.subj ";                                    
//            
//            System.out.println("sql => " + sql);
            
            list = db.executeQueryList(box, sql);				
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }

    /** 
    과제이력 목록을 가져온다.
    @param RequestBox box 
    @return ArrayList
    */
    public ArrayList selectListReport(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        ArrayList list = null;
        String sql = "";

        try {            
            db = new DatabaseExecute();

            String s_userid   = box.getSession("userid");
            
            String v_yearsubjseq = box.getString("p_yearsubjseq");

		    String v_year = "";
		    String v_subjseq = "";

		    if(!v_yearsubjseq.equals("") && v_yearsubjseq != null){
			    StringTokenizer token = new StringTokenizer(v_yearsubjseq, ",");
		    	v_year = token.nextToken();
		    	v_subjseq = token.nextToken();
		    }

			sql += "\r\n	SELECT															";
			sql += "\r\n	s.subj,                                                         ";
			sql += "\r\n	s.year,                                                         ";
			sql += "\r\n	s.subjseq,                                                      ";
			sql += "\r\n	s.class,                                                        ";
			sql += "\r\n	s.subjnm,                                                       ";
			sql += "\r\n	p.ordseq,                                                       ";
			sql += "\r\n	p.title,                                                        ";
			sql += "\r\n	p.reptype,                                                      ";
			sql += "\r\n	NVL((SELECT score FROM TU_PROJREP                               ";
			sql += "\r\n		 WHERE 1=1                                                  ";
			sql += "\r\n		 AND grcode = p.grcode                                      ";
			sql += "\r\n		 AND subj = p.subj                                          ";
			sql += "\r\n		 AND year = p.year                                          ";
			sql += "\r\n		 AND subjseq = p.subjseq                                    ";
			sql += "\r\n		 AND class = p.class                                        ";
			sql += "\r\n		 AND ordseq = p.ordseq                                      ";
			sql += "\r\n		 AND( (projid = stu.userid                                  ";
			sql += "\r\n				AND p.reptype = 'P'                                 ";
			sql += "\r\n				)                                                   ";
			sql += "\r\n				OR                                                  ";
			sql += "\r\n				(projid =                                           ";
			sql += "\r\n					(SELECT                                         ";
			sql += "\r\n					leader                                          ";
			sql += "\r\n					FROM                                            ";
			sql += "\r\n					TU_TEAMMASTER xx, TU_TEAMMEMBER yy              ";
			sql += "\r\n					WHERE 1=1                                       ";
			sql += "\r\n					AND xx.grcode = yy.grcode                       ";
			sql += "\r\n					AND xx.subj = yy.subj                           ";
			sql += "\r\n					AND xx.year = yy.year                           ";
			sql += "\r\n					AND xx.subjseq = yy.subjseq                     ";
			sql += "\r\n					AND xx.class = yy.class                         ";
			sql += "\r\n					AND xx.teamno = yy.teamno                       ";
			sql += "\r\n					AND xx.grcode = p.grcode                        ";
			sql += "\r\n					AND xx.subj = p.subj                            ";
			sql += "\r\n					AND xx.year = p.year                            ";
			sql += "\r\n					AND xx.subjseq = p.subjseq                      ";
			sql += "\r\n					AND xx.class = p.class                          ";
			sql += "\r\n					AND yy.userid = stu.userid )                    ";
			sql += "\r\n				AND                                                 ";
			sql += "\r\n				p.reptype = 'T') )                                  ";
			sql += "\r\n	), p.basicscore) score,                                         ";
			sql += "\r\n	NVL((SELECT count(*) FROM TU_PROJREP                            ";
			sql += "\r\n		 WHERE 1=1                                                  ";
			sql += "\r\n		 AND grcode = p.grcode                                      ";
			sql += "\r\n		 AND subj = p.subj                                          ";
			sql += "\r\n		 AND year = p.year                                          ";
			sql += "\r\n		 AND subjseq = p.subjseq                                    ";
			sql += "\r\n		 AND class = p.class                                        ";
			sql += "\r\n		 AND ordseq = p.ordseq                                      ";
			sql += "\r\n		 AND( (projid = stu.userid                                  ";
			sql += "\r\n				AND p.reptype = 'P'                                 ";
			sql += "\r\n				)                                                   ";
			sql += "\r\n				OR                                                  ";
			sql += "\r\n				(projid =                                           ";
			sql += "\r\n					(SELECT                                         ";
			sql += "\r\n					leader                                          ";
			sql += "\r\n					FROM                                            ";
			sql += "\r\n					TU_TEAMMASTER xx, TU_TEAMMEMBER yy              ";
			sql += "\r\n					WHERE 1=1                                       ";
			sql += "\r\n					AND xx.grcode = yy.grcode                       ";
			sql += "\r\n					AND xx.subj = yy.subj                           ";
			sql += "\r\n					AND xx.year = yy.year                           ";
			sql += "\r\n					AND xx.subjseq = yy.subjseq                     ";
			sql += "\r\n					AND xx.class = yy.class                         ";
			sql += "\r\n					AND xx.teamno = yy.teamno                       ";
			sql += "\r\n					AND xx.grcode = p.grcode                        ";
			sql += "\r\n					AND xx.subj = p.subj                            ";
			sql += "\r\n					AND xx.year = p.year                            ";
			sql += "\r\n					AND xx.subjseq = p.subjseq                      ";
			sql += "\r\n					AND xx.class = p.class                          ";
			sql += "\r\n					AND yy.userid = stu.userid )                    ";
			sql += "\r\n				AND                                                 ";
			sql += "\r\n				p.reptype = 'T') )                                  ";
			sql += "\r\n	), 0) submitcnt,                                         		";
			sql += "\r\n	(SELECT isbestreport FROM TU_PROJREP                            ";
			sql += "\r\n	 WHERE 1=1                                                      ";
			sql += "\r\n	 AND grcode = p.grcode                                          ";
			sql += "\r\n	 AND subj = p.subj                                              ";
			sql += "\r\n	 AND year = p.year                                              ";
			sql += "\r\n	 AND subjseq = p.subjseq                                        ";
			sql += "\r\n	 AND class = p.class                                            ";
			sql += "\r\n	 AND ordseq = p.ordseq                                          ";
			sql += "\r\n	 AND( (projid = stu.userid                                      ";
			sql += "\r\n			AND p.reptype = 'P'                                     ";
			sql += "\r\n			)                                                       ";
			sql += "\r\n			OR                                                      ";
			sql += "\r\n			(projid =                                               ";
			sql += "\r\n				(SELECT                                             ";
			sql += "\r\n				leader                                              ";
			sql += "\r\n				FROM                                                ";
			sql += "\r\n				TU_TEAMMASTER xx, TU_TEAMMEMBER yy                  ";
			sql += "\r\n				WHERE 1=1                                           ";
			sql += "\r\n				AND xx.grcode = yy.grcode                           ";
			sql += "\r\n				AND xx.subj = yy.subj                               ";
			sql += "\r\n				AND xx.year = yy.year                               ";
			sql += "\r\n				AND xx.subjseq = yy.subjseq                         ";
			sql += "\r\n				AND xx.class = yy.class                             ";
			sql += "\r\n				AND xx.teamno = yy.teamno                           ";
			sql += "\r\n				AND xx.grcode = p.grcode                            ";
			sql += "\r\n				AND xx.subj = p.subj                                ";
			sql += "\r\n				AND xx.year = p.year                                ";
			sql += "\r\n				AND xx.subjseq = p.subjseq                          ";
			sql += "\r\n				AND xx.class = p.class                              ";
			sql += "\r\n				AND yy.userid = stu.userid )                        ";
			sql += "\r\n			AND                                                     ";
			sql += "\r\n			p.reptype = 'T') )                                      ";
			sql += "\r\n	) isbestreport,                                                 ";
			sql += "\r\n	get_name(t.userid) usernm                                       ";
			sql += "\r\n	FROM                                                            ";
			sql += "\r\n	TU_STUDENT stu,                                                 ";
			sql += "\r\n	TU_PROJORD p,                                                   ";
			sql += "\r\n	TU_SUBJSEQ s,                                                   ";
			sql += "\r\n	TU_TUTOR t                                                      ";
			sql += "\r\n	WHERE 1=1                                                       ";
			sql += "\r\n	AND stu.grcode = p.grcode                                       ";
			sql += "\r\n	AND stu.subj = p.subj                                           ";
			sql += "\r\n	AND stu.year = p.year                                           ";
			sql += "\r\n	AND stu.subjseq = p.subjseq                                     ";
			sql += "\r\n	AND stu.class = p.class                                         ";
			sql += "\r\n	AND stu.grcode = s.grcode                                       ";
			sql += "\r\n	AND stu.subj = s.subj                                           ";
			sql += "\r\n	AND stu.year = s.year                                           ";
			sql += "\r\n	AND stu.subjseq = s.subjseq                                     ";
			sql += "\r\n	AND stu.class = s.class                                         ";
			sql += "\r\n	AND p.grcode = t.grcode(+)                                      ";
			sql += "\r\n	AND p.subj = t.subj(+)                                          ";
			sql += "\r\n	AND p.year = t.year(+)                                          ";
			sql += "\r\n	AND p.subjseq = t.subjseq(+)                                    ";
			sql += "\r\n	AND p.class = t.class(+)                                        ";
			sql += "\r\n	AND p.luserid = t.userid(+)                                     ";
            sql += "\r\n	AND stu.year = '"+v_year+"' ";
            sql += "\r\n	AND stu.subjseq = '"+v_subjseq+"' ";
            sql += "\r\n	AND stu.userid = '"+s_userid+"' ";
//			sql += "\r\n	AND stu.chkfinal = 'Y'                                          ";
			sql += "\r\n	ORDER BY subjnm                                                 ";
			
            box.put("p_isPage", new Boolean(false));     //    리스트 검색시 페이지 나누기가 있는 경우
            box.put("p_row", new Integer(row));     //    리스트 검색시 페이지 나누기가 있는 게시판 경우
            
            list = db.executeQueryList(box, sql);
            
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }

    /** 
    온라인시험이력 목록을 가져온다.
    @param RequestBox box 
    @return ArrayList
    */
    public ArrayList selectListExam(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        ArrayList list = null;
        String sql = "";

        try {            
            db = new DatabaseExecute();

            String s_userid   = box.getSession("userid");
            
            String v_yearsubjseq = box.getString("p_yearsubjseq");

		    String v_year = "";
		    String v_subjseq = "";

		    if(!v_yearsubjseq.equals("") && v_yearsubjseq != null){
			    StringTokenizer token = new StringTokenizer(v_yearsubjseq, ",");
		    	v_year = token.nextToken();
		    	v_subjseq = token.nextToken();
		    }

            sql += "\r\n	SELECT                                                          ";
			sql += "\r\n	s.subj,                                                         ";
			sql += "\r\n	s.year,                                                         ";
			sql += "\r\n	s.subjseq,                                                      ";
			sql += "\r\n	s.class,                                                        ";
            sql += "\r\n	s.subjnm,                                                       ";
            sql += "\r\n	p.papernum,                                                       ";
            sql += "\r\n	p.papernm,                                                        ";
            sql += "\r\n	get_codenm_lang('0026', p.examtype, '"+box.getSession("languageName")+"') examtypenm, ";
            sql += "\r\n	(SELECT                                                         ";
            sql += "\r\n	score                                                        ";
            sql += "\r\n	FROM                                                            ";
            sql += "\r\n	TU_EXAMRESULT                                                        ";
            sql += "\r\n	WHERE 1=1                                                       ";
            sql += "\r\n	AND papernum = p.papernum                                           ";
            sql += "\r\n	AND userid = stu.userid                                         ";
            sql += "\r\n	) score,                                                          ";
            sql += "\r\n	(SELECT                                                         ";
            sql += "\r\n	count(*)                                                        ";
            sql += "\r\n	FROM                                                            ";
            sql += "\r\n	TU_EXAMRESULT                                                        ";
            sql += "\r\n	WHERE 1=1                                                       ";
            sql += "\r\n	AND papernum = p.papernum                                           ";
            sql += "\r\n	AND userid = stu.userid                                         ";
            sql += "\r\n	) submitcnt,                                                          ";
            sql += "\r\n	get_name(t.userid) usernm                                       ";
            sql += "\r\n	FROM                                                            ";
            sql += "\r\n	TU_STUDENT stu,                                                 ";
            sql += "\r\n	TU_EXAMPAPER p,                                                   ";
            sql += "\r\n	TU_SUBJSEQ s,                                                   ";
            sql += "\r\n	TU_TUTOR t                                                      ";
            sql += "\r\n	WHERE 1=1                                                       ";
            sql += "\r\n	AND stu.subj = p.subj                                           ";
            sql += "\r\n	AND stu.year = p.year                                           ";
            sql += "\r\n	AND stu.subjseq = p.subjseq                                     ";
            sql += "\r\n	AND stu.class = p.class                                         ";
            sql += "\r\n	AND stu.grcode = s.grcode                                       ";
            sql += "\r\n	AND stu.subj = s.subj                                           ";
            sql += "\r\n	AND stu.year = s.year                                           ";
            sql += "\r\n	AND stu.subjseq = s.subjseq                                     ";
            sql += "\r\n	AND stu.class = s.class                                         ";
            sql += "\r\n	AND p.subj = t.subj(+)                                          ";
            sql += "\r\n	AND p.year = t.year(+)                                          ";
            sql += "\r\n	AND p.subjseq = t.subjseq(+)                                    ";
            sql += "\r\n	AND p.class = t.class(+)                                        ";
            sql += "\r\n	AND p.luserid = t.userid(+)                                     ";
            sql += "\r\n	AND stu.year = '"+v_year+"' ";
            sql += "\r\n	AND stu.subjseq = '"+v_subjseq+"' ";
            sql += "\r\n	AND stu.userid = '"+s_userid+"' ";
//            sql += "\r\n	AND stu.chkfinal = 'Y'                                          ";
            sql += "\r\n	ORDER BY subjnm                                                 ";
            
            box.put("p_isPage", new Boolean(false));     //    리스트 검색시 페이지 나누기가 있는 경우
            box.put("p_row", new Integer(row));     //    리스트 검색시 페이지 나누기가 있는 게시판 경우
            
            list = db.executeQueryList(box, sql);

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }

    /** 
    토론이력 목록을 가져온다.
    @param RequestBox box 
    @return ArrayList
    */
    public ArrayList selectListDebate(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        ArrayList list = null;
        String sql = "";

        try {            
            db = new DatabaseExecute();

            String s_userid   = box.getSession("userid");
            
            String v_yearsubjseq = box.getString("p_yearsubjseq");

		    String v_year = "";
		    String v_subjseq = "";

		    if(!v_yearsubjseq.equals("") && v_yearsubjseq != null){
			    StringTokenizer token = new StringTokenizer(v_yearsubjseq, ",");
		    	v_year = token.nextToken();
		    	v_subjseq = token.nextToken();
		    }
            
            sql += "\r\n	SELECT                                                          ";
			sql += "\r\n	s.subj,                                                         ";
			sql += "\r\n	s.year,                                                         ";
			sql += "\r\n	s.subjseq,                                                      ";
			sql += "\r\n	s.class,                                                        ";
            sql += "\r\n	s.subjnm,                                                       ";
            sql += "\r\n	p.tpcode,                                                       ";
            sql += "\r\n	p.title,                                                        ";
            sql += "\r\n	(SELECT                                                         ";
            sql += "\r\n	count(*)                                                        ";
            sql += "\r\n	FROM                                                            ";
            sql += "\r\n	TU_TORON                                                        ";
            sql += "\r\n	WHERE 1=1                                                       ";
            sql += "\r\n	AND grcode = p.grcode                                           ";
            sql += "\r\n	AND subj = p.subj                                               ";
            sql += "\r\n	AND year = p.year                                               ";
            sql += "\r\n	AND subjseq = p.subjseq                                         ";
            sql += "\r\n	AND class = p.class                                             ";
            sql += "\r\n	AND tpcode = p.tpcode                                           ";
            sql += "\r\n	AND userid = stu.userid                                         ";
            sql += "\r\n	) cnt,                                                          ";
            sql += "\r\n	(SELECT                                                         ";
            sql += "\r\n	count(*)                                                        ";
            sql += "\r\n	FROM                                                            ";
            sql += "\r\n	TU_TORON                                                        ";
            sql += "\r\n	WHERE 1=1                                                       ";
            sql += "\r\n	AND grcode = p.grcode                                           ";
            sql += "\r\n	AND subj = p.subj                                               ";
            sql += "\r\n	AND year = p.year                                               ";
            sql += "\r\n	AND subjseq = p.subjseq                                         ";
            sql += "\r\n	AND class = p.class                                             ";
            sql += "\r\n	AND tpcode = p.tpcode                                           ";
            sql += "\r\n	) totalcnt,                                                     ";
            sql += "\r\n	get_name(t.userid) usernm                                       ";
            sql += "\r\n	FROM                                                            ";
            sql += "\r\n	TU_STUDENT stu,                                                 ";
            sql += "\r\n	TU_TORONTP p,                                                   ";
            sql += "\r\n	TU_SUBJSEQ s,                                                   ";
            sql += "\r\n	TU_TUTOR t                                                      ";
            sql += "\r\n	WHERE 1=1                                                       ";
            sql += "\r\n	AND stu.grcode = p.grcode                                       ";
            sql += "\r\n	AND stu.subj = p.subj                                           ";
            sql += "\r\n	AND stu.year = p.year                                           ";
            sql += "\r\n	AND stu.subjseq = p.subjseq                                     ";
            sql += "\r\n	AND stu.class = p.class                                         ";
            sql += "\r\n	AND stu.grcode = s.grcode                                       ";
            sql += "\r\n	AND stu.subj = s.subj                                           ";
            sql += "\r\n	AND stu.year = s.year                                           ";
            sql += "\r\n	AND stu.subjseq = s.subjseq                                     ";
            sql += "\r\n	AND stu.class = s.class                                         ";
            sql += "\r\n	AND p.grcode = t.grcode(+)                                      ";
            sql += "\r\n	AND p.subj = t.subj(+)                                          ";
            sql += "\r\n	AND p.year = t.year(+)                                          ";
            sql += "\r\n	AND p.subjseq = t.subjseq(+)                                    ";
            sql += "\r\n	AND p.class = t.class(+)                                        ";
            sql += "\r\n	AND p.luserid = t.userid(+)                                     ";
            sql += "\r\n	AND stu.year = '"+v_year+"' ";
            sql += "\r\n	AND stu.subjseq = '"+v_subjseq+"' ";
            sql += "\r\n	AND stu.userid = '"+s_userid+"' ";
//            sql += "\r\n	AND stu.chkfinal = 'Y'                                          ";
            sql += "\r\n	ORDER BY subjnm                                                 ";
            
            box.put("p_isPage", new Boolean(false));     //    리스트 검색시 페이지 나누기가 있는 경우
            box.put("p_row", new Integer(row));     //    리스트 검색시 페이지 나누기가 있는 게시판 경우
            
            list = db.executeQueryList(box, sql);

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }

    /** 
    출석/진도이력 목록을 가져온다.
    @param RequestBox box 
    @return ArrayList
    */
    public ArrayList selectListAttendance(RequestBox box) throws Exception {
        DatabaseExecute db = null;
        ArrayList list = null;
        String sql = "";

        try {            
            db = new DatabaseExecute();

            String s_userid   = box.getSession("userid");
            
            String v_yearsubjseq = box.getString("p_yearsubjseq");

		    String v_year = "";
		    String v_subjseq = "";

		    if(!v_yearsubjseq.equals("") && v_yearsubjseq != null){
			    StringTokenizer token = new StringTokenizer(v_yearsubjseq, ",");
		    	v_year = token.nextToken();
		    	v_subjseq = token.nextToken();
		    }
            
            sql += "\r\n	SELECT                                                          ";
			sql += "\r\n	a.subj,                                                         ";
			sql += "\r\n	a.year,                                                         ";
			sql += "\r\n	a.subjseq,                                                      ";
			sql += "\r\n	a.class,                                                        ";
            sql += "\r\n	s.subjnm,                                                       ";
            sql += "\r\n	TRUNC(((SELECT count(*) FROM TU_ATTENDANCEDETAIL                ";
            sql += "\r\n	WHERE 1=1                                                       ";
            sql += "\r\n	AND grcode = a.grcode                                           ";
            sql += "\r\n	AND subj = a.subj                                               ";
            sql += "\r\n	AND year = a.year                                               ";
            sql += "\r\n	AND subjseq = a.subjseq                                         ";
            sql += "\r\n	AND class = a.class                                             ";
            sql += "\r\n	AND attendancediv = 'AT'                                        ";
            sql += "\r\n	AND userid = stu.userid                                         ";
            sql += "\r\n	) / count(*) * 100), 1) attrate,                                ";
            sql += "\r\n	TRUNC(SUM(stu.tstep) / count(*), 1) prograte,                   ";
            sql += "\r\n	get_name(t.userid) usernm                                       ";
            sql += "\r\n	FROM                                                            ";
            sql += "\r\n	TU_STUDENT stu,                                                 ";
            sql += "\r\n	TU_ATTENDANCEMASTER a,                                          ";
            sql += "\r\n	TU_SUBJSEQ s,                                                   ";
            sql += "\r\n	TU_TUTOR t                                                      ";
            sql += "\r\n	WHERE 1=1                                                       ";
            sql += "\r\n	AND stu.grcode = a.grcode                                       ";
            sql += "\r\n	AND stu.subj = a.subj                                           ";
            sql += "\r\n	AND stu.year = a.year                                           ";
            sql += "\r\n	AND stu.subjseq = a.subjseq                                     ";
            sql += "\r\n	AND stu.class = a.class                                         ";
            sql += "\r\n	AND stu.grcode = s.grcode                                       ";
            sql += "\r\n	AND stu.subj = s.subj                                           ";
            sql += "\r\n	AND stu.year = s.year                                           ";
            sql += "\r\n	AND stu.subjseq = s.subjseq                                     ";
            sql += "\r\n	AND stu.class = s.class                                         ";
            sql += "\r\n	AND a.grcode = t.grcode(+)                                      ";
            sql += "\r\n	AND a.subj = t.subj(+)                                          ";
            sql += "\r\n	AND a.year = t.year(+)                                          ";
            sql += "\r\n	AND a.subjseq = t.subjseq(+)                                    ";
            sql += "\r\n	AND a.class = t.class(+)                                        ";
            sql += "\r\n	AND a.luserid = t.userid(+)                                     ";
            sql += "\r\n	AND stu.year = '"+v_year+"' ";
            sql += "\r\n	AND stu.subjseq = '"+v_subjseq+"' ";
            sql += "\r\n	AND stu.userid = '"+s_userid+"' ";
//            sql += "\r\n	AND stu.chkfinal = 'Y'                                          ";
            sql += "\r\n	GROUP BY a.grcode, a.subj, a.year, a.subjseq, a.class, s.subjnm, t.userid, stu.userid ";
            sql += "\r\n	ORDER BY subjnm													";
            
            box.put("p_isPage", new Boolean(false));     //    리스트 검색시 페이지 나누기가 있는 경우
            box.put("p_row", new Integer(row));     //    리스트 검색시 페이지 나누기가 있는 게시판 경우
            
            list = db.executeQueryList(box, sql);

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        return list;
    }

}
