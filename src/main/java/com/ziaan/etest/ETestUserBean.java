
//**********************************************************
//1. 제      목: 온라인테스트 사용자
//2. 프로그램명: ETestUserBean.java
//3. 개      요:
//4. 환      경: JDK 1.3
//5. 버      젼: 0.1
//6. 작      성: Administrator 2003-08-29
//7. 수      정:
//
//**********************************************************

package com.ziaan.etest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileMove;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ETestUserBean {

    private ConfigSet conf;

    public ETestUserBean() {
        try{
            conf = new ConfigSet();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    /**
    사용자 해당과정리스트
    @param box          receive from the form object and session
    @return ArrayList   해당과정리스트
    */
/*    public ArrayList SelectUserList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String sql = "";

        try {
        String s_userid     = box.getSession("userid");
        String v_etestsubj     = box.getString("p_etestsubj");
        String v_year     = box.getString("p_year");
        String v_etestcode     = box.getString("p_etestcode");


            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql = "select b.etestsubj,   a.year,    a.etestcode, ";
            sql+= "       a.startdt, a.enddt,  a.etestpoint, a.etestcnt, a.totalscore,  ";
            sql+= "       a.cntlevel1, a.cntlevel2, a.cntlevel3, a.isopenanswer, ";
            sql+= "       a.isopenexp,  a.papercnt,  a.etesttext ";
            sql+= "  from tz_etestmaster a, ";
            sql+= "   where a.subj   = " + SQLString.Format(v_etestsubj);
            sql+= "   and a.year   = " + SQLString.Format(v_year);
            sql+= "   and a.subjseq   = " + SQLString.Format(v_etestcode);
            sql+= "   and rownum <= 1 ";
            sql+= " order by a.subj, a.year, a.subjseq, a.lesson, a.examtype  ";

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
*/



    /**
	평가문제 응시 등록 
    @param box          receive from the form object and session
    @return int
    */
    public int InsertResult(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        String sql = "";
        int isOk = 0;

        String v_etestsubj      = box.getString("p_etestsubj");
        String v_year      = box.getString("p_gyear");
        String v_etestcode   = box.getString("p_etestcode");

        int    v_etestnum  = box.getInt("p_etestnum");
        String v_userid    = box.getString("p_userid");
        String v_answer    = box.getString("p_answer");
        String v_etest    = box.getString("p_etest");

        String v_started    = box.getString("p_started");
        String v_ended    = box.getString("p_ended");
        double v_time = 0;

        int    v_etestcnt  = box.getInt("p_etestcnt");
        int    v_etestpoint  = box.getInt("p_etestpoint");

        int v_score     = 0;
        int v_answercnt = 0;

        String v_luserid   = box.getSession("userid");

        int    v_exist     = 0;

        Vector v_result = null;

        String v_corrected = "";

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);


            v_exist = chkResultExist(connMgr,   v_etestsubj,     v_year,   v_etestcode,   v_userid);

            if (v_exist == 0) {

                    v_result = getScore(connMgr, v_etestsubj,     v_year,   v_etestcode,   v_etestnum,  v_userid, v_etest, v_answer, v_etestcnt, v_etestpoint);

                    v_score = Integer.parseInt((String)v_result.get(0));
                    v_answercnt = Integer.parseInt((String)v_result.get(1));
                    v_corrected = (String)v_result.get(2);

                    v_time = FormatDate.getSecDifference(v_started, v_ended);

                        isOk = InsertTZ_examresult(connMgr,
                        v_etestsubj,     v_year,   v_etestcode,  v_etestnum,  v_userid, v_etest, v_etestcnt, v_etestpoint, v_score, v_answercnt, v_started, v_ended, v_time, v_answer, v_corrected, v_luserid);
            } else {
                isOk = 99;
            }
        }
        catch(Exception ex) {
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            connMgr.commit();
            if(connMgr != null) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }


    /**
	평가문제 응시 등록 
    @param box          receive from the form object and session
    @return int
    */
    public int InsertTZ_examresult(DBConnectionManager connMgr,
                                String p_etestsubj,     String p_year,      String p_etestcode, int p_etestnum,
                                String p_userid,  String p_etest, int p_etestcnt,
                                int p_etestpoint,  int p_score, int p_answercnt,  String p_started,  String p_ended,  double p_time,
                                String p_answer, String p_corrected,   String p_luserid) throws Exception {

        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;

        try {
            //insert TZ_EXAMRESULT table
            sql =  " insert into TZ_ETESTRESULT ";
            sql+=  " (etestsubj,   year,      etestcode,  etestnum, ";
            sql+=  "  userid,  etest, etestcnt, ";
            sql+=  "  etestpoint,  score, answercnt, started, ";
            sql+=  "  ended,  time, answer, corrected,  luserid,  ldate) ";
            sql+=  " values ";
            sql+=  " (?,      ?,         ?,         ?, ";
            sql+=  "  ?,      ?,         ?,          ";
            sql+=  "  ?,      ?,         ?,         ?,      ";
            sql+=  "  ?,      ?,         ?,         ?,        ?,             ? ) ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, p_etestsubj);
            pstmt.setString( 2, p_year);
            pstmt.setString( 3, p_etestcode);
            pstmt.setInt   ( 4, p_etestnum);
            pstmt.setString( 5, p_userid);
            pstmt.setString( 6, p_etest);
            pstmt.setInt   ( 7, p_etestcnt);
            pstmt.setInt   ( 8, p_etestpoint);
            pstmt.setInt   ( 9, p_score);
            pstmt.setInt   ( 10, p_answercnt);
            pstmt.setString(11, p_started);
            pstmt.setString(12, p_ended);
            pstmt.setDouble(13, p_time);
            pstmt.setString(14, p_answer);
            pstmt.setString(15, p_corrected);
            pstmt.setString(16, p_luserid);
            pstmt.setString(17, FormatDate.getDate("yyyyMMddHHmmss"));

            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        }
        catch(Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
        }
        return isOk;
    }


    /**
	평가결과 존재여부 확인 
    @param box          receive from the form object and session
    @return int
    */
    public int chkResultExist(DBConnectionManager connMgr, String p_etestsubj,     String p_year,      String p_etestcode, String p_userid  ) throws Exception {
        ListSet ls = null;
        String sql  = "";
        int v_exist = 0;

        try {
            sql = "select count(*)  cnt ";
            sql+= "  from tz_etestresult  ";
            sql+= " where etestsubj    = " + SQLString.Format(p_etestsubj);
            sql+= "   and year    = " + SQLString.Format(p_year);
            sql+= "   and etestcode = " + SQLString.Format(p_etestcode);
            sql+= "   and userid  = " + SQLString.Format(p_userid);

            ls = connMgr.executeQuery(sql);
            while (ls.next()) {
                v_exist = ls.getInt("cnt");
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if (ls != null) { try { ls.close(); } catch (Exception e) {} }
        }
        return v_exist;
    }

    /**
	평가문제 응시결과 점수 구하기 
    @param box          receive from the form object and session
    @return Vector
    */
    public Vector getScore(DBConnectionManager connMgr,
                        String p_etestsubj, String p_year, String p_etestcode, int p_etestnum, String p_userid, String p_etest, String p_answer, int p_etestcnt, int p_etestpoint) throws Exception {

        Vector    v_examnums    = new Vector();
        int v_examnum = 0;
        Vector    v_answers = new Vector();
        String v_answer = "";

        StringTokenizer st1   = null;
        StringTokenizer st2  = null;

        int    v_score   = 0;
        int    v_answercnt = 0;
        String v_iscorrect = "";
        int    v_temp = 0;

        Vector v_result = new Vector();

        try {

            st1 = new StringTokenizer(p_etest,ETestBean.SPLIT_COMMA);
            while (st1.hasMoreElements()) {
                v_examnums.add((String)st1.nextToken());
            }

            st2 = new StringTokenizer(p_answer,ETestBean.SPLIT_COMMA);
            while (st2.hasMoreElements()) {
                v_answers.add((String)st2.nextToken());
            }

            for ( int i =0; i < v_examnums.size() ; i++) {

                   v_examnum = Integer.parseInt((String)v_examnums.get(i));
                   v_answer = (String)v_answers.get(i);

                   v_temp = MakeExamResult(connMgr, p_etestsubj, v_examnum, v_answer);

                   v_score += (v_temp * p_etestpoint);
                   v_answercnt += v_temp;
                   if (i == v_examnums.size()-1) {
                       v_iscorrect += String.valueOf(v_temp);
                   } else {
                       v_iscorrect += String.valueOf(v_temp) + ",";
                   }

            }

            v_result.add(String.valueOf(v_score));
            v_result.add(String.valueOf(v_answercnt));
            v_result.add(v_iscorrect);

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        return v_result;
    }

    /**
	평가문제 응시결과 점수 
    @param box          receive from the form object and session
    @return Vector
    */
  public Vector getScore2(RequestBox box,
                        String p_etestsubj, String p_year, String p_etestcode, int p_etestnum, String p_userid, String p_etest, String p_answer, int p_etestcnt, int p_etestpoint) throws Exception {

        Vector    v_examnums    = new Vector();
        int v_examnum = 0;
        Vector    v_answers = new Vector();
        String v_answer = "";

        StringTokenizer st1   = null;
        StringTokenizer st2  = null;
        StringTokenizer st3  = null;

        int    v_score   = 0;
        int    v_answercnt = 0;
        String v_iscorrect = "";
        int    v_temp = 0;

        Vector v_result = new Vector();

        String v_realAnswerStr = "";
        Vector    v_realAnswers = new Vector();
        String v_realAnswer = "";

        try {

            st1 = new StringTokenizer(p_etest,ETestBean.SPLIT_COMMA);
            while (st1.hasMoreElements()) {
                v_examnums.add((String)st1.nextToken());
            }

            st2 = new StringTokenizer(p_answer,ETestBean.SPLIT_COMMA);
            while (st2.hasMoreElements()) {
                v_answers.add((String)st2.nextToken());
            }
            //-------------------------------------------------------------
            String etestDir = conf.getProperty("dir.etest.resultpaper");           //           etest 평가문제답안지를 위한 문제/문항이 저장되는 경로
            BufferedReader in = new BufferedReader(new FileReader(new File(etestDir + File.separator + p_year + "_" + p_etestsubj, p_etestsubj + p_year + p_etestcode + p_etestnum + ".txt")));

            v_realAnswerStr = in.readLine();
            //System.out.println("v_realAnswerStr=="+v_realAnswerStr);
            st3 = new StringTokenizer(v_realAnswerStr,ETestBean.SPLIT_COMMA);
            while (st3.hasMoreElements()) {
                v_realAnswers.add((String)st3.nextToken());
            }
            //-------------------------------------------------------------

            for ( int i =0; i < v_examnums.size() ; i++) {

                   v_examnum = Integer.parseInt((String)v_examnums.get(i));
                   v_answer = (String)v_answers.get(i);
                   v_answer = v_answer.trim().toUpperCase();
                   v_realAnswer = (String)v_realAnswers.get(i);
                   v_realAnswer = v_realAnswer.trim().toUpperCase();
                   if(v_answer.equals(v_realAnswer)){
                        v_temp =1;
                   }else{ v_temp =0; }
                    System.out.println("v_answer==v_realAnswer==v_temp"+v_answer+"=="+v_realAnswer+"=="+v_temp);
                   v_score += (v_temp * p_etestpoint);
                   v_answercnt += v_temp;
                   if (i == v_examnums.size()-1) {
                       v_iscorrect += String.valueOf(v_temp);
                   } else {
                       v_iscorrect += String.valueOf(v_temp) + ",";
                   }

            }

            v_result.add(String.valueOf(v_score));
            v_result.add(String.valueOf(v_answercnt));
            v_result.add(v_iscorrect);

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        return v_result;
    }


    /**
	평가문제 결과 만들기 
    @param box          receive from the form object and session
    @return int
    */
    public int MakeExamResult(DBConnectionManager connMgr, String p_etestsubj, int p_etestnum, String p_answer) throws Exception {

        int isOk = 0;

        ListSet ls = null;
        String sql  = "";
        DataBox dbox = null;

        String v_examtype = "";

        try {
            sql = "select etesttype ";
            sql+= "  from tz_etest ";
            sql+= " where etestsubj      = " + SQLString.Format(p_etestsubj);
            sql+= " and etestnum        = " + SQLString.Format(p_etestnum);

            ls = connMgr.executeQuery(sql);
            while (ls.next()) {
                v_examtype = ls.getString("etesttype");
            }

            if (v_examtype.length() > 0) {

                if (v_examtype.equals("1")) {
                    int p_selnum = 0;
                    if(!p_answer.trim().equals("")){
                        p_selnum = Integer.parseInt(p_answer.trim());
                    }

                sql = "select count(b.selnum) cnt  ";
                sql += "   from tz_etest a,  ";
                sql += "   (select etestsubj, etestnum, selnum, isanswer  from tz_etestsel where etestsubj    = " + SQLString.Format(p_etestsubj);
                sql += "   and etestnum    = " + SQLString.Format(p_etestnum) + "   )  b ";
                sql += "   where b.isanswer    =   'Y'   ";
                sql += "   and a.etestsubj = b.etestsubj  ";
                sql += "   and a.etestnum = b.etestnum  ";
                sql += "   and b.selnum    =    " + p_selnum;

                } else if (v_examtype.equals("2")) {

                sql = "select count(b.selnum) cnt  ";
                sql += "   from tz_etest a,  ";
                sql += "   (select etestsubj, etestnum, selnum, seltext, isanswer  from tz_etestsel where etestsubj    = " + SQLString.Format(p_etestsubj);
                sql += "   and etestnum    = " + SQLString.Format(p_etestnum) + "   )  b ";
                sql += "   where a.etestsubj = b.etestsubj  ";
                sql += "   and a.etestnum = b.etestnum  ";
                sql += "   and b.seltext   like  " + SQLString.Format(p_answer.trim());

                }
//System.out.println(sql);
                ls.close();
                ls = connMgr.executeQuery(sql);

                while (ls.next()) {
                    dbox = ls.getDataBox();

                    if (ls.getInt("cnt") > 0){
                         isOk = 1;
                    }

                }

            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }

        return isOk;
    }


    /**
    사용자 결과 보기
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList SelectUserPaperResult(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ArrayList list = null;

        String v_action = box.getStringDefault("p_action",  "change");

        String v_etestsubj = box.getString("p_etestsubj");
        String v_year     = box.getString("p_gyear");
        String v_etestcode   = box.getString("p_etestcode");
        String v_userid    = box.getString("p_userid");

        try {
                connMgr = new DBConnectionManager();
                list = getPaperResultList(connMgr, v_etestsubj, v_year, v_etestcode, v_userid);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        finally {
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }


    /**
    사용자 결과 보기
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList getPaperResultList(DBConnectionManager connMgr, String p_etestsubj, String p_year, String p_etestcode, String p_userid) throws Exception {

        ArrayList QuestionExampleDataList  = null;
        ArrayList list = new ArrayList();
        ListSet ls = null;
        String sql  = "";
        DataBox dbox = null;


        try {
            sql = "select a.isopenanswer, a.isopenexp, b.etest, b.answer, b.corrected ";
            sql+= "  from tz_etestpaper  a, ";
            sql+= "       tz_etestresult b ";
            sql+= " where a.etestsubj    = b.etestsubj ";
            sql+= "   and a.year    = b.year ";
            sql+= "   and a.etestcode = b.etestcode ";
            sql+= " and b.etestsubj = " + SQLString.Format(p_etestsubj);
            sql+= " and b.year = " + SQLString.Format(p_year);
            sql+= " and b.etestcode = " + SQLString.Format(p_etestcode);
            sql+= " and b.userid = " + SQLString.Format(p_userid);
            sql+= "   and rownum <= 1 ";
            sql+= " order by a.etestsubj, a.year, a.etestcode, b.userid ";


            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }

                QuestionExampleDataList = getExampleData(connMgr, p_etestsubj, dbox.getString("d_etest"));

        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if (ls != null) { try { ls.close(); } catch (Exception e) {} }
        }
        return QuestionExampleDataList;
    }


    /**
	학습자별 평가 결과 리스트  
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList SelectUserPaperResult2(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ArrayList list = null;

        String v_action = box.getStringDefault("p_action",  "change");

        String v_etestsubj = box.getString("p_etestsubj");
        String v_year     = box.getString("p_gyear");
        String v_etestcode   = box.getString("p_etestcode");
        String v_userid    = box.getString("p_userid");

        try {
                connMgr = new DBConnectionManager();
                list = getPaperResultList2(connMgr, v_etestsubj, v_year, v_etestcode, v_userid);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        finally {
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }


    /**
	학습자별 평가 결과 리스트  
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList getPaperResultList2(DBConnectionManager connMgr, String p_etestsubj, String p_year, String p_etestcode, String p_userid) throws Exception {

        ArrayList list = new ArrayList();
        ListSet ls = null;
        String sql  = "";
        DataBox dbox = null;
        StringTokenizer st = null;
        Vector v_answer = null;
        StringTokenizer st2 = null;
        Vector v_corrected = null;


        try {
            sql = "select a.isopenanswer, a.isopenexp, a.startdt, a.enddt, b.etest, b.answer, b.corrected, ";
            sql += " b.etestcnt, b.answercnt, b.score, b.etestpoint, b.started, b.ended,  ";
            sql += " a.year, a.etestcode, a.etesttext  ";
            sql+= "  from tz_etestmaster  a, ";
            sql+= "       tz_etestresult b ";
            sql+= " where a.etestsubj    = b.etestsubj ";
            sql+= "   and a.year    = b.year ";
            sql+= "   and a.etestcode = b.etestcode ";
            sql+= " and b.etestsubj = " + SQLString.Format(p_etestsubj);
            sql+= " and b.year = " + SQLString.Format(p_year);
            sql+= " and b.etestcode = " + SQLString.Format(p_etestcode);
            sql+= " and b.userid = " + SQLString.Format(p_userid);
            sql+= "   and rownum <= 1 ";
            sql+= " order by a.etestsubj, a.year, a.etestcode, b.userid ";

//System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while (ls.next()) {
                dbox = ls.getDataBox();

                st = new StringTokenizer(dbox.getString("d_answer"),",");
                v_answer = new Vector();
                while(st.hasMoreElements()) {
                     v_answer.add(st.nextToken());
                }
                st2 = new StringTokenizer(dbox.getString("d_corrected"),",");
                v_corrected = new Vector();
                while(st2.hasMoreElements()) {
                     v_corrected.add(st2.nextToken());
                }
            }

            sql = "select count(b.userid) cnt ";
            sql+= "     from  tz_etestresult b ";
            sql+= " where b.etestsubj = " + SQLString.Format(p_etestsubj);
            sql+= " and b.year = " + SQLString.Format(p_year);
            sql+= " and b.etestcode = " + SQLString.Format(p_etestcode);
            sql+= " and b.score >  " + SQLString.Format(dbox.getString("d_score"));

                ls.close();
                ls = connMgr.executeQuery(sql);

                while (ls.next()) {
                    dbox.put("d_overman", ls.getString("cnt"));
                }

                list.add(dbox);
                list.add(v_answer);
                list.add(v_corrected);


        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if (ls != null) { try { ls.close(); } catch (Exception e) {} }
        }
        return list;
    }

    /**
    사용자 결과보기 - 문제별
    @param box          receive from the form object and session
    @return QuestionExampleData   평가문제
    */
    public ArrayList getExampleData(DBConnectionManager connMgr, String p_etestsubj,  String p_examnums) throws Exception {
        Hashtable hash = new Hashtable();
        ArrayList blist = new ArrayList();
        ArrayList list = null;

        ListSet ls  = null;
        String sql  = "";
        DataBox dbox = null;
        StringTokenizer st = null;

        try {

            st = new StringTokenizer(p_examnums, ",");

            while(st.hasMoreElements()) {

                int examnum = Integer.parseInt(st.nextToken());

                sql = "select a.etestsubj, a.etestnum, a.etesttype, a.etesttext, a.exptext,   ";
                sql+= "       a.levels, a.selcnt, a.realimage, a.saveimage, a.realaudio, a.saveaudio, a.realmovie, a.savemovie,    a.realflash,   a.saveflash,     ";
                sql+= "       b.selnum, b.seltext,  b.isanswer, c.codenm etesttypenm, d.codenm levelsnm,           ";
                sql+= "       (select codenm from tz_code where gubun="+SQLString.Format(ETestBean.ETEST_GUBUN)+" and levels='1' and code=a.etestgubun) gubunnm    "; //문제분류
                sql+= "  from tz_etest a, tz_etestsel b, tz_code c, tz_code d                                      ";
                sql+= " where   a.etestsubj     = b.etestsubj(+)                                                             ";
                sql+= "   and a.etestnum  = b.etestnum(+)                                                          ";
                sql+= "   and a.etesttype = c.code                                                                ";
                sql+= "   and a.levels   = d.code                                                                ";
                sql+= "   and a.etestsubj     = " + SQLString.Format(p_etestsubj);
                sql+= "   and a.etestnum  = " + examnum;
                sql+= "   and c.gubun    = " + SQLString.Format(ETestBean.ETEST_TYPE);
                sql+= "   and d.gubun    = " + SQLString.Format(ETestBean.ETEST_LEVEL);
                sql+= " order by a.etestnum, b.selnum              ";


            ls = connMgr.executeQuery(sql);
            list =  new ArrayList();
Log.info.println("sqlr결과 >>>>"+sql);
            while (ls.next()) {
                dbox = ls.getDataBox();
                list.add(dbox);

            }
               blist.add(list);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
        }
        return blist;
    }

    /**
    사용자 해당과정리스트
    @param box          receive from the form object and session
    @return ArrayList   해당과정리스트

    public ArrayList SelectUserList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String sql = "";

        try {
            String s_userid     = box.getSession("userid");
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "select a.etestsubj, a.year, a.etestcode,                  ";
            sql += "       a.startdt, a.enddt,  a.etesttext,                     ";
            sql += "       (select etestsubjnm from tz_etestsubj where etestsubj = a.etestsubj) etestsubjnm     ";
            sql += " from tz_etestmaster a, ";
            sql += "  (select etestsubj, year, etestcode, userid from tz_etestmember where userid = " + SQLString.Format(s_userid);
            sql += "   minus select etestsubj, year, etestcode, userid from tz_etestresult where userid = " + SQLString.Format(s_userid) + ") b";
            sql += " where a.etestsubj = b.etestsubj                                                ";
            sql += "  and a.year = b.year                                                ";
            sql += "  and a.etestcode = b.etestcode                                          ";
            sql += "  and substr(a.startdt,1,12) <= to_char(sysdate, 'yyyyMMddHHmm')         ";
            sql += "  and substr(a.enddt,1,12) > to_char(sysdate, 'yyyyMMddHHmm')         ";
            sql += "  and b.userid = " + StringManager.makeSQL(s_userid);
            sql += " order by a.startdt asc                                             ";

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
    */
    /**
    사용자 해당과정리스트
    @param box          receive from the form object and session
    @return ArrayList   해당과정리스트
    */
    public ArrayList SelectUserList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String sql = "";

        try {
            String s_userid     = box.getSession("userid");
            connMgr = new DBConnectionManager();

            list = new ArrayList();
            //yyyyMMddHHmm,YYYYMMDDHH24MI
            //재응시 전
            /*
            sql  = "select a.etestsubj, a.year, a.etestcode,   a.etesttime,  a.etestcnt,  a.papercnt,               ";
            sql += "       a.startdt, a.enddt,  a.etesttext,                     ";
            sql += "       (select etestsubjnm from tz_etestsubj where etestsubj = a.etestsubj) etestsubjnm     ";
            sql += " from tz_etestmaster a, ";
            sql += "  (select etestsubj, year, etestcode, userid from tz_etestmember where userid = " + SQLString.Format(s_userid);
            sql += "   minus select etestsubj, year, etestcode, userid from tz_etestresult where userid = " + SQLString.Format(s_userid) + ") b";
            sql += " where a.etestsubj = b.etestsubj                                                ";
            sql += "  and a.year = b.year                                                ";
            sql += "  and a.etestcode = b.etestcode                                          ";
            sql += "  and substr(a.startdt,1,12) <= to_char(sysdate, 'YYYYMMDDHH24MI')         ";
            sql += "  and substr(a.enddt,1,12) > to_char(sysdate, 'YYYYMMDDHH24MI')         ";
            sql += "  and b.userid = " + StringManager.makeSQL(s_userid);
            sql += " order by a.startdt asc                                              ";
            */
            //재응시 적용
            sql  = "select a.etestsubj, a.year, a.etestcode,   a.etesttime,  a.etestcnt,  a.papercnt,               ";
            sql += "       a.startdt, a.enddt,  a.etesttext,a.retrynum,b.trycnt,                     ";
            sql += "       (select etestsubjnm from tz_etestsubj where etestsubj = a.etestsubj) etestsubjnm     ";
            sql += " from tz_etestmaster a, ";
            sql += "  (select etestsubj, year, etestcode, userid,trycnt from tz_etestmember where userid = " + SQLString.Format(s_userid) + ") b";
            sql += " where a.etestsubj = b.etestsubj                                                ";
            sql += "  and a.year = b.year                                                ";
            sql += "  and a.etestcode = b.etestcode                                          ";
            //sql += "  and substr(a.startdt,1,12) <= to_char(sysdate, 'YYYYMMDDHH24MI')         ";
            sql += "  and substr(a.enddt,1,12) > to_char(sysdate, 'YYYYMMDDHH24MI')         ";
            sql += "  and b.userid = " + StringManager.makeSQL(s_userid);
            sql += " order by a.startdt asc                                              ";
 //System.out.println(sql);
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
    e-test 결과 리스트
    @param box          receive from the form object and session
    @return ArrayList   결과 리스트
    */
    public ArrayList SelectUserHistoryList(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        ArrayList list = null;
        DataBox dbox = null;
        String sql = "";

        try {
            String s_userid     = box.getSession("userid");
            connMgr = new DBConnectionManager();

            list = new ArrayList();
            /*
            sql = "select a.etesttext, a.etestsubj, a.year, a.etestcode,a.isopenanswer,a.isopenexp,a.ldate,b.score, b.started, b.ended, b.userid ";
            sql+= "  from tz_etestmaster a, ";
            sql+= "   tz_etestresult b ";
            sql += " where a.etestsubj = b.etestsubj                                                ";
            sql += "  and a.year = b.year                                                ";
            sql += "  and a.etestcode = b.etestcode                                          ";
            sql += "  and b.userid = " + StringManager.makeSQL(s_userid);
            */
            sql = "select a.etesttext, a.etestsubj, a.year, a.etestcode,a.isopenanswer,a.isopenexp,c.trycnt,b.score, b.started, b.ended, b.userid ";
            sql+= "  ,( to_number(substr(a.enddt,1,12)) - to_number(to_char(sysdate, 'YYYYMMDDHH24MI')) ) isnotended ";
            sql+= "  from tz_etestmaster a, ";
            sql+= "   tz_etestresult b, ";
            sql+= "   tz_etestmember c ";
            sql += " where a.etestsubj = c.etestsubj                                                ";
            sql += "  and a.year = c.year                                                ";
            sql += "  and a.etestcode = c.etestcode                                          ";

            sql += " and c.etestsubj = b.etestsubj(+)";
            sql += " and c.year = b.year(+) and c.etestcode = b.etestcode(+) and c.userid = b.userid(+)";

            sql += "  and c.userid = " + StringManager.makeSQL(s_userid);
            //sql+= " order by a.ldate desc";

//System.out.println(sql);
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
    e-test 학습자 제출
    @param box          receive from the form object and session
    @return int
    */
    public int WriteETestUserResult(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ArrayList list = null;
        ArrayList blist = null;
        DataBox dbox = null;
        int isOk = 0;
        String str = "";

        PrintWriter writer = null;

        try {
        connMgr = new DBConnectionManager();

        String v_etestsubj      = box.getString("p_etestsubj");
        String v_year      = box.getString("p_gyear");
        String v_etestcode   = box.getString("p_etestcode");

        int    v_etestnum  = box.getInt("p_etestnum");
        String v_userid    = box.getSession("userid");
        String v_answer    = box.getString("p_answer");
        String v_etest    = box.getString("p_etest");

        String v_started    = box.getString("p_started");
        String v_ended    = box.getString("p_ended");
        double v_time = 0;

        int    v_etestcnt  = box.getInt("p_etestcnt");
        int    v_etestpoint  = box.getInt("p_etestpoint");

        int v_score     = 0;
        int v_answercnt = 0;

        String v_luserid   = box.getSession("userid");

        int    v_exist     = 0;

        Vector v_result = null;

        String v_corrected = "";
                    /*
                    v_result = getScore(connMgr, v_etestsubj,     v_year,   v_etestcode,   v_etestnum,  v_userid, v_etest, v_answer, v_etestcnt, v_etestpoint);

                    v_score = Integer.parseInt((String)v_result.get(0));
                    v_answercnt = Integer.parseInt((String)v_result.get(1));
                    v_corrected = (String)v_result.get(2);
                    */
                    v_result = getScore2(box, v_etestsubj,     v_year,   v_etestcode,   v_etestnum,  v_userid, v_etest, v_answer, v_etestcnt, v_etestpoint);
                    v_score = Integer.parseInt((String)v_result.get(0));
                    v_answercnt = Integer.parseInt((String)v_result.get(1));
                    v_corrected = (String)v_result.get(2);
                    v_time = FormatDate.getSecDifference(v_started, v_ended);


            //------------------------------------  학습자들이 응시한 etest 평가답안지가 저장되는 디렉토리 생성 ----------------------------------
            //String userDir = conf.getProperty("dir.etest.userresult");
            String userDir = conf.getProperty("dir.etest.resultpaper");
            File userDirectory = new java.io.File(userDir + File.separator + v_year + "_" + v_etestsubj + "_" + v_etestcode);                //      '연도_과정그룹_이테스트코드' 디렉토리 생성



            if(!userDirectory.isDirectory()) {                      //  디렉토리 없으면 새로 생성
                userDirectory.mkdir();
            }
            // bakup jkh 0312
            File userDirectorybak = new java.io.File(userDir + File.separator + v_year + "_" + v_etestsubj + "_" + v_etestcode+"_bak");
            if(!userDirectorybak.isDirectory()) {                      //  디렉토리 없으면 새로 생성
                userDirectorybak.mkdir();
            }
            //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

            String v_filename = v_etestsubj + v_year + v_etestcode + "." + v_userid + ".txt";


            File txtFile = new File(userDirectory, v_filename);

            //if(txtFile.exists()){
            //   isOk = 99;
            //}else{
                if(txtFile.isFile()) txtFile.delete();
                if(txtFile.exists()) txtFile.delete();
                java.io.FileWriter fw =  new java.io.FileWriter(txtFile.getAbsolutePath(), true);
                writer = new PrintWriter(new java.io.BufferedWriter(fw), true);                  //         응시자 시험결과 .txt 파일 생성

                writer.println(v_etestsubj);   //   이테스트그룹
                writer.println(v_year);        //   연도
                writer.println(v_etestcode);   //   테스트코드
                writer.println(v_etestnum);    //   테스트번호
                writer.println(v_userid);      //   사용자
                writer.println(v_etest);       //   문제번호 조합
                writer.println(v_etestcnt);    //   문제수
                writer.println(v_etestpoint);  //   배점
                writer.println(v_score);       //   득점
                writer.println(v_answercnt);   //   정답갯수
                writer.println(v_started);     //   시작시간
                writer.println(v_ended);       //   종료시간
                writer.println(v_time);        //   걸린시간
                writer.println(v_answer);      //   사용자답
                writer.println(v_corrected);   //   정답여부
                //jkh 0313
                writer.flush();
                fw.close();
                writer.close();

                try{
                    boolean isMove = true;
                    if (!v_filename.equals("")) { isMove = new FileMove().copy(userDir + File.separator + v_year + "_" + v_etestsubj + "_" + v_etestcode+"_bak", userDir + File.separator + v_year + "_" + v_etestsubj + "_" + v_etestcode, v_filename);  }
                }catch(Exception e1){}
                isOk = 1;
            //}
        }catch (Exception ie ) {
            isOk = 0;
            ie.printStackTrace();
        }
        finally {
            if(writer != null) try{ writer.close(); }catch(Exception e1){ e1.printStackTrace(); }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return isOk;
    }

    /**
    온라인테스트 제출후 처리
    @param box     receive from the form object and session
    @return isOk   1:update success,0:update fail
    */
    public int IncreaseTrycnt(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String sql = "";
        int isOk = 0;

        String  v_etestsubj      = box.getString("p_etestsubj");
        String  v_etestcode      = box.getString("p_etestcode");
        String  v_gyear      = box.getString("p_gyear");
        String v_userid    = box.getSession("userid");

        try {
            connMgr = new DBConnectionManager();

            //update tz_etestmaster table
            sql =  " update tz_etestmember ";
            sql+=  "    set trycnt = nvl(trycnt,0) + 1 ";
            sql+=  "  where etestsubj = ? and etestcode = ? and year = ? and userid = ? ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_etestsubj);
            pstmt.setString(2, v_etestcode);
            pstmt.setString(3, v_gyear);
            pstmt.setString(4, v_userid);

            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
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
	학습자별 평가 결과 데이타 
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList SelectUserPaperTextResult(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ArrayList list = null;

        String v_action = box.getStringDefault("p_action",  "change");

        String v_etestsubj = box.getString("p_etestsubj");
        String v_year     = box.getString("p_gyear");
        String v_etestcode   = box.getString("p_etestcode");
        String v_userid    = box.getSession("userid");
        String v_etest    = box.getString("p_etest");

        try {
                connMgr = new DBConnectionManager();
                list = getExampleData(connMgr, v_etestsubj, v_etest);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage());
        }
        finally {
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

    /**
	학습자별 평가 결과 데이타 
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList SelectUserPaperTextResult2(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        DataBox dbox = null;
        ArrayList list = null;
        String str = "";
        String answers = "";
        String corrected = "";
        String started = "";
        String ended = "";
        String score = "";
        String etestpoint = "";
        int index = 0;
        String sql = "";
        StringTokenizer st = null;
        Vector v_answer = null;
        StringTokenizer st2 = null;
        Vector v_corrected = null;

        String v_action = box.getStringDefault("p_action",  "change");

        String v_etestsubj = box.getString("p_etestsubj");
        String v_year     = box.getString("p_gyear");
        String v_etestcode   = box.getString("p_etestcode");
        String v_userid    = box.getSession("userid");
        int    v_etestnum  = box.getInt("p_etestnum");

        try {
            connMgr = new DBConnectionManager();

            //String userDir = conf.getProperty("dir.etest.userresult");
            String userDir = conf.getProperty("dir.etest.resultpaper");
            File userDirectory = new java.io.File(userDir + File.separator + v_year + "_" + v_etestsubj + "_" + v_etestcode);                //      '연도_과정그룹_이테스트코드' 디렉토리 생성

            String v_filename = v_etestsubj + v_year + v_etestcode + "." + v_userid + ".txt";

            BufferedReader in = new BufferedReader(new FileReader(new File(userDirectory, v_filename)));

                while((str = in.readLine()) != null) {
                    index++;

                    if (index == 8){
                          etestpoint = str;
                    }else if (index == 9){
                          score = str;
                    }else if (index == 11){
                          started = str;
                    }else if (index == 12){
                          ended = str;
                    }else if (index == 14){
                          answers = str;
                    }else if (index == 15){
                          corrected = str;
                    }
                }

                st = new StringTokenizer(answers,",");
                v_answer = new Vector();
                while(st.hasMoreElements()) {
                     v_answer.add(st.nextToken());
                }
                st2 = new StringTokenizer(corrected,",");
                v_corrected = new Vector();
                while(st2.hasMoreElements()) {
                     v_corrected.add(st2.nextToken());
                }

            sql = "select a.isopenanswer, a.isopenexp, a.startdt, a.enddt,  ";
            sql += " a.year, a.etestcode, a.etesttext  ";
            sql+= "  from tz_etestmaster  a ";
            sql+= " where a.etestsubj = " + SQLString.Format(v_etestsubj);
            sql+= " and a.year = " + SQLString.Format(v_year);
            sql+= " and a.etestcode = " + SQLString.Format(v_etestcode);
            sql+= "   and rownum <= 1 ";
            sql+= " order by a.etestsubj, a.year, a.etestcode ";

            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                dbox = ls.getDataBox();
            }
                dbox.put("d_etestpoint", etestpoint);
                dbox.put("d_score", score);
                dbox.put("d_started", started);
                dbox.put("d_ended", ended);

                list = new ArrayList();
                list.add(dbox);
                list.add(v_answer);
                list.add(v_corrected);
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if (ls != null) { try { ls.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }

}