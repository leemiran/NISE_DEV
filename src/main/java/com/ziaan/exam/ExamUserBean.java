// **********************************************************
// 1. 제      목: 온라인테스트 사용자
// 2. 프로그램명: ExamUserBean.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: Administrator 2003-08-29
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.exam;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.complete.StoldData;
import com.ziaan.library.CalcUtil;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.EduEtc1Bean;
import com.ziaan.library.ErrorManager;
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
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class ExamUserBean { 

    public ExamUserBean() { }


    /**
    사용자 해당과목리스트
    @param box          receive from the form object and session
    @return ArrayList   해당과목리스트
    */
    public ArrayList SelectUserList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        DataBox             dbox    = null;
        String              sql     = "";
                        
        try { 
        String s_userid     = box.getSession("userid");
        String v_subj       = box.getString("p_subj");
        String v_year       = box.getString("p_year");
        String v_subjseq    = box.getString("p_subjseq");


			connMgr = new DBConnectionManager();
            
            list = new ArrayList();
            
            sql  = "SELECT B.SUBJ  \n";
            sql += "     , A.YEAR  \n";
            sql += "     , A.SUBJSEQ  \n";
            sql += "     , A.LESSON  \n";
            sql += "     , B.ISONOFF  \n";
            sql += "     , A.EXAMTYPE  \n";
            sql += "     , A.PAPERNUM  \n";
            sql += "     , DECODE(D.STARTED, NULL, 'Y', '', 'Y'  \n";
            sql += "            , CASE WHEN D.ENDED IS NULL OR D.ENDED = ''  \n";
            sql += "                        THEN CASE WHEN TRUNC((SYSDATE - TO_DATE(D.STARTED,'YYYYMMDDHH24MISS')) * 24 * 60) < A.EXAMTIME THEN 'Y'  \n";
            sql += "                                  ELSE CASE WHEN nvl(D.USERRETRY,A.RETRYCNT) > 0 THEN 'Y'  \n";
            sql += "                                            ELSE 'N'  \n";
            sql += "                                       END  \n";
            sql += "                             END  \n";
            sql += "                   ELSE CASE WHEN nvl(D.USERRETRY,A.RETRYCNT) > 0 THEN 'Y'  \n";
            sql += "                             ELSE 'N'  \n";
            sql += "                        END  \n";
            sql += "              END) TIME_YN  \n";
            sql += "     , CASE WHEN D.ENDED IS NOT NULL OR D.ENDED != '' THEN A.EXAMTIME  \n";
            sql += "            ELSE DECODE(D.STARTED, NULL, A.EXAMTIME, '', A.EXAMTIME  \n";
            sql += "                      , A.EXAMTIME - TRUNC((SYSDATE - TO_DATE(D.STARTED,'YYYYMMDDHH24MISS')) * 24 * 60))  \n";
            sql += "       END AS EXTRATIME  \n";
            sql += "     , A.LESSONSTART  \n";
            sql += "     , A.LESSONEND  \n";
            sql += "     , A.EXAMTIME  \n";
            sql += "     , A.EXAMPOINT  \n";
            sql += "     , A.EXAMCNT  \n";
            sql += "     , A.TOTALSCORE  \n";
            sql += "     , A.CNTLEVEL1  \n";
            sql += "     , A.CNTLEVEL2  \n";
            sql += "     , A.CNTLEVEL3  \n";
            sql += "     , A.ISOPENANSWER  \n";
            sql += "     , A.ISOPENEXP  \n";
            sql += "     , A.RETRYCNT  \n";
            sql += "     , A.PROGRESS  \n";
            sql += "     , B.SUBJNM  \n";
            sql += "     , GET_CODENM(" + SQLString.Format(ExamBean.PTYPE) + ", NVL(A.EXAMTYPE, '')) EXAMTYPENM  \n";
            sql += "     , C.MTEST_START  \n";
            sql += "     , C.MTEST_END  \n";
            sql += "     , C.FTEST_START  \n";
            sql += "     , C.FTEST_END  \n";
            sql += "     , D.SCORE  \n";
            sql += "     , D.STARTED  \n";
            sql += "     , D.ENDED  \n";
            sql += "     , nvl(D.USERRETRY,A.RETRYCNT) as USERRETRY  \n";
            sql += "     , D.ANSWER  \n";
            sql += "     , to_char(sysdate, 'YYYYMMDD') nowtime \n";
            sql += "FROM   TZ_EXAMPAPER A  \n";
            sql += "     , TZ_SUBJ B  \n";
            sql += "     , TZ_SUBJSEQ C  \n";
            sql += "     , TZ_EXAMRESULT D  \n";
            sql += "WHERE  A.SUBJ = B.SUBJ  \n";
            sql += "AND    A.SUBJ = C.SUBJ  \n";
            sql += "AND    A.YEAR = C.YEAR  \n";
            sql += "AND    A.SUBJSEQ = C.SUBJSEQ  \n";
            sql += "AND    A.SUBJ = D.SUBJ (+)  \n";
            sql += "AND    A.YEAR = D.YEAR (+)  \n";
            sql += "AND    A.SUBJSEQ = D.SUBJSEQ (+)  \n";
            sql += "AND    A.LESSON = D.LESSON (+)  \n";
            sql += "AND    A.EXAMTYPE = D.EXAMTYPE (+)  \n";
            sql += "AND    A.PAPERNUM = D.PAPERNUM (+)  \n";
            sql += "AND    D.USERID (+) = " + SQLString.Format(s_userid) + "  \n";
            sql += "AND    A.SUBJ = " + SQLString.Format(v_subj) + "  \n";
            sql += "AND    A.YEAR = " + SQLString.Format(v_year) + "  \n";
            sql += "AND    A.SUBJSEQ = " + SQLString.Format(v_subjseq) + "  \n";
            //sql += "AND    TO_CHAR(SYSDATE, 'YYYYMMDD') BETWEEN DECODE(A.EXAMTYPE, 'M', MTEST_START, FTEST_START) AND DECODE(A.EXAMTYPE, 'M', MTEST_END, FTEST_END)  \n";
            sql += "ORDER BY  \n";
            sql += "       A.SUBJ  \n";
            sql += "     , A.YEAR  \n";
            sql += "     , A.SUBJSEQ  \n";
            sql += "     , A.LESSON  \n";
            sql += "     , A.EXAMTYPE  \n";

            /*
            sql  = " select b.subj, a.year, a.subjseq, a.lesson, b.isonoff, 					\n";
            sql += "        a.examtype, a.papernum, a.lessonstart, a.lessonend,   				\n";
            sql += "        a.examtime, a.exampoint, a.examcnt, a.totalscore, 					\n";
            sql += "        a.cntlevel1, a.cntlevel2, a.cntlevel3, a.isopenanswer, 				\n";
            sql += "	   (select eduend from tz_subjseq  										\n";
            sql += "	    where subj    = a.subj												\n";
            sql += "	      and year    = a.year 												\n";
            sql += "	      and subjseq = a.subjseq 											\n";
            sql += "	    ) eduend , 															\n";
            sql += "	   (select mtest_start from tz_subjseq  								\n";
            sql += "	    where subj    = a.subj												\n";
            sql += "	      and year    = a.year 												\n";
            sql += "	      and subjseq = a.subjseq 											\n";
            sql += "	    ) mtest_start , 													\n";
            sql += "	   (select mtest_end from tz_subjseq  									\n";
            sql += "	    where subj    = a.subj												\n";
            sql += "	      and year    = a.year 												\n";
            sql += "	      and subjseq = a.subjseq 											\n";
            sql += "	    ) mtest_end , 														\n";
            sql += "	   (select ftest_start from tz_subjseq  								\n";
            sql += "	    where subj    = a.subj												\n";
            sql += "	      and year    = a.year 												\n";
            sql += "	      and subjseq = a.subjseq 											\n";
            sql += "	    ) ftest_start , 													\n";
            sql += "	   (select ftest_end from tz_subjseq  									\n";
            sql += "	    where subj    = a.subj												\n";
            sql += "	      and year    = a.year 												\n";
            sql += "	      and subjseq = a.subjseq 											\n";
            sql += "	    ) ftest_end , 														\n";
            sql += "       a.isopenexp, a.retrycnt, a.progress, b.subjnm,   					\n";
            sql += "	   GET_CODENM("+SQLString.Format(ExamBean.PTYPE)+", nvl(a.examtype, '') \n";
            sql += "	   ) examtypenm , 														\n";
            sql += "      nvl(to_char((select score from tz_examresult where subj=a.subj  		\n";
            sql += "       and year= a.year and subjseq=a.subjseq   							\n";
            sql += "	   and lesson=a.lesson and examtype=a.examtype 							\n";
            sql += "       and papernum=a.papernum and userid='"+s_userid+"')), '-') score,		\n";
            sql += "      nvl(to_char((select ended from tz_examresult where subj=a.subj  		\n";
            sql += "       and year= a.year and subjseq=a.subjseq   							\n";
            sql += "	   and lesson=a.lesson and examtype=a.examtype 							\n";
            sql += "       and papernum=a.papernum and userid='"+s_userid+"')), '-') ended, 	\n";
            sql += "      nvl(to_char((select started from tz_examresult where subj=a.subj  	\n";
            sql += "       and year= a.year and subjseq=a.subjseq   							\n";
            sql += "	   and lesson=a.lesson and examtype=a.examtype 							\n";
            sql += "       and papernum=a.papernum and userid='"+s_userid+"')), '-') started 	\n";
            sql += " from tz_exampaper a, 														\n";
            sql += "      tz_subj b  															\n";
            sql += " where a.subj( +)= b.subj  													\n";
            sql += "   and a.subj    = " + SQLString.Format(v_subj) + " 						\n";
            sql += "   and a.year    = " + SQLString.Format(v_year) + "							\n";
            sql += "   and a.subjseq = " + SQLString.Format(v_subjseq) + "						\n";
//            sql += "   and rownum <= 1 ";
            sql += " order by a.subj, a.year, a.subjseq, a.lesson, a.examtype  					\n";
			*/

            ls = connMgr.executeQuery(sql);
 //System.out.println(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
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
    사용자 해당과목리스트
    @param box          receive from the form object and session
    @return ArrayList   해당과목리스트
    */
    public ArrayList SelectUserResultList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        DataBox             dbox    = null;
        String              sql     = "";
		int v_result = 0;
                        
        try { 
        String s_userid     = box.getSession("userid");
        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq     = box.getString("p_subjseq");


			connMgr = new DBConnectionManager();
            
            list = new ArrayList();

            sql  = "  select b.subj,   a.year,    a.subjseq,   a.lesson,  											\n";
            sql += "       a.examtype,  a.papernum,   a.lessonstart, a.lessonend,    								\n";
            sql += "       a.cntlevel1, a.cntlevel2, a.cntlevel3, a.isopenanswer, 									\n";
            sql += "       a.isopenexp,  a.retrycnt,  a.progress, b.subjnm,   										\n";
            sql += "       a.examtime, a.exampoint, a.examcnt, a.totalscore, 										\n";
            sql += "       GET_CODENM(" + SQLString.Format(ExamBean.PTYPE) + ",  									\n";
            sql += "       nvl(a.examtype, '')) examtypenm, 														\n";
            sql += "       (select score from tz_examresult where subj=a.subj  										\n";
            sql += "        and year= a.year and subjseq=a.subjseq and lesson=a.lesson and  examtype=a.examtype  	\n";
            sql += "        and papernum=a.papernum and  userid='" +s_userid + "') score                           \n";
            sql += "  from tz_exampaper a, 																			\n";
            sql += "       tz_subj b  																				\n";
            sql += "  where a.subj( +) = b.subj  																	\n";
            sql += "   and a.subj = " + SQLString.Format(v_subj);
            sql += "   and a.year = " + SQLString.Format(v_year);
            sql += "   and a.subjseq = " + SQLString.Format(v_subjseq);
//            sql += "   and rownum <= 1 ";
            sql += " order by a.subj, a.year, a.subjseq, a.lesson, a.examtype  ";

//            System.out.println(sql);            

            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                v_result = SelectUserResultList(connMgr, v_subj, v_year, v_subjseq, s_userid, dbox.getString("d_examtype"), dbox.getInt("d_papernum") );
                list.add(String.valueOf(v_result));
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
    학습자 결과 리스트 
    @param box          receive from the form object and session
    @return ArrayList   해당과목리스트
    */
    public int SelectUserResultList(DBConnectionManager connMgr,
                    String p_subj, String p_year, String p_subjseq, String p_userid, String p_examtype, int p_papernum) throws Exception { 

        ArrayList QuestionExampleDataList  = null;
        int v_result = 0;
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;


        try { 
            sql = "select count(score) examcount ";
            sql += "  from tz_examresult  ";
            sql += " where subj = " + SQLString.Format(p_subj);
            sql += " and year = " + SQLString.Format(p_year);
            sql += " and subjseq = " + SQLString.Format(p_subjseq);
            sql += " and examtype = " + SQLString.Format(p_examtype);
            sql += " and userid = " + SQLString.Format(p_userid);
            sql += " and papernum = " + SQLString.Format(p_papernum);

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox = ls.getDataBox();
                v_result = dbox.getInt("d_examcount");
			}
		
		} catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_result;
    }
   /* 
   public String InsertRetry(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String              sql     = "";
        int isOk = 0;

        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_gyear");
        String v_subjseq   = box.getString("p_subjseq");

        String v_lesson    = box.getString("p_lesson");
        String v_examtype     = box.getString("p_examtype");
        int    v_papernum  = box.getInt("p_papernum");
        String v_userid    = box.getString("p_userid");
        String v_answer    = box.getString("p_answer");
        String v_exam    = box.getString("p_exam");

        String v_started    = box.getString("p_started");
        String v_ended    = box.getString("p_ended");
		double v_time = 0;

        int    v_examcnt  = box.getInt("p_examcnt");
        int    v_exampoint  = box.getInt("p_exampoint");

        int    v_retry  = box.getInt("p_retry");
        int    v_realretry  = box.getInt("p_realretry");

        int v_score     = 0;
		int v_answercnt = 0;

        String v_luserid   = box.getSession("userid");

        int    v_exist     = 0;

		String v_result = "0,";
		Vector v_v = null;

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

			 if ( v_retry == 0 || (v_retry - v_realretry) == 1) { 
				 v_result = "1," +String.valueOf(InsertResult(box));
             } else{ 

					v_v = getScore(connMgr, v_subj,     v_year,   v_subjseq,   v_lesson,  v_examtype,    v_papernum,  v_userid, v_exam, v_answer, v_examcnt, v_exampoint);

                    v_score = Integer.parseInt((String)v_v.get(0));
					v_answercnt = Integer.parseInt((String)v_v.get(1));

					v_time = FormatDate.getSecDifference(v_started, v_ended);

                        v_result +=(String.valueOf(v_score)) + ",";
						v_result +=(String.valueOf(v_answercnt)) ;
                   

			}
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            connMgr.commit();
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_result;
    }
    */
    /**
	평가 제출 : 평가 시작하면서 넣어진 결과 테이블의 EXAMCNT, EXAMPOINT, SCORE, ANSWERCNT , TIME, ANSWER
											, CORRECTED, USERRETRY, LUSERID,LDATE 값을 넣는다.
	@param box      receive from the form object
	@return int
	*/
    public int UpdateResult(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String              sql     = "";
        int isOk = 0;

        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_gyear");
        String v_subjseq   = box.getString("p_subjseq");

        String v_lesson    = box.getString("p_lesson");
        String v_examtype  = box.getString("p_examtype");
        int    v_papernum  = box.getInt("p_papernum");
        String v_userid    = box.getString("p_userid");
        String v_answer    = box.getString("p_answer");
        String v_exam      = box.getString("p_exam");

        String v_started   = box.getString("p_started");			// 평가 시작 시간
        String v_ended 	   = FormatDate.getDate("yyyyMMddHHmmss");	// 평가 끝 시간
        // String v_ended     = box.getString("p_ended");			// 평가 끝 시간
        
    	double v_time = 0;

        int    v_examcnt  = box.getInt("p_examcnt");			// 문제수
        int    v_exampoint  = box.getInt("p_exampoint");		// 문제 별 점수
        int    v_userretry  = box.getInt("p_userretry");		// 재시험 가능 횟수

        int v_score     = 0;
	    int v_answercnt = 0;

        String v_luserid   = box.getSession("userid");
        String v_userip	= box.getSession("userip");
        

        int    v_exist     = 0;

    	Vector v_result = null;

	    String v_corrected = "";

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

			// Added by LeeSuMin.. 2004.02.24
			// 현재 학습중인 학생신분이 아니므로 평가결과를 저장하지 않으며 결과를 확인하실 수 없습니다.
			if ( !EduEtc1Bean.isCurrentStudent(v_subj,v_year,v_subjseq,v_userid,v_examtype,String.valueOf(v_papernum)).equals("Y")) return 97;
            //if ( !EduEtc1Bean.isCurrentStudent(v_subj,v_year,v_subjseq,v_userid ).equals("Y")) return 97;

            // 평가지,평가자별 카운트(제출했는지 여부인데 여기서 따지면 안된다 그래서 주석처리한다.)
            //v_exist = chkResultExist(connMgr,   v_subj,     v_year,   v_subjseq,   v_lesson,   v_examtype,    v_papernum,  v_userid);
			// 위 대신 answer 와 ended 의 값 으로 제출 여부를 확인 둘의 값이 있으면 제출한 것, 없으면 제출하지 않은 것
			String v_exist_answer = chkResultExist1(connMgr,   v_subj,     v_year,   v_subjseq,   v_lesson,   v_examtype,    v_papernum,  v_userid);
			String v_exist_ended = chkResultExist2(connMgr,   v_subj,     v_year,   v_subjseq,   v_lesson,   v_examtype,    v_papernum,  v_userid);
//			String v_exist_answer = v_answer;
//			String v_exist_ended = v_ended;
			
            // 평가점수채점
			v_result = getScore(connMgr, v_subj,     v_year,   v_subjseq,   v_lesson,  v_examtype,    v_papernum,  v_userid, v_answer.equals("") ? "" : v_exam, v_answer, v_examcnt, v_exampoint);

						
            v_score     = Integer.parseInt((String)v_result.get(0)); // 점수
            
			v_answercnt = Integer.parseInt((String)v_result.get(1));
			v_corrected = (String)v_result.get(2);

			v_time = v_ended.equals("")?0:FormatDate.getMinDifference(v_started, v_ended);
					
            StringTokenizer st2 = new StringTokenizer(v_answer,ExamBean.SPLIT_COMMA);
            
            v_answer = "";
            while ( st2.hasMoreElements() ) { 
                
	            String s = StringManager.trim((String)st2.nextToken() );
	
	            if ( s.length() == 0) v_answer = v_answer + " " + ",";
	            else v_answer = v_answer + s + ",";
            }

            //if ( v_userretry != 0 && v_exist != 0 ) v_userretry = v_userretry-1;
            if ( v_userretry != 0 && !(v_exist_answer.equals("") && v_exist_ended.equals("")) ) v_userretry = v_userretry-1;

            //if ( v_exist == 0 ) { // 제출한것이 없다는것
            //if(v_exist_answer.trim().equals("") && v_exist_ended.equals("")) {  // 제출한것이 없다는것
            if(v_exist_ended.equals("")||v_exist_ended == null) {  // 제출한것이 없다는것
		            
				isOk = UpdateTZ_examresult(connMgr,
				v_subj,     v_year,   v_subjseq,   v_lesson,
				v_examtype,    v_papernum,  v_userid, v_exam, v_examcnt, v_exampoint, v_score, v_answercnt, v_started,
				v_ended, v_time, v_answer, v_corrected, v_userretry, v_luserid, v_userip);
                                  
				isOk = CalcUtil.getInstance().calc_score(connMgr, CalcUtil.EXAM, v_subj, v_year, v_subjseq, v_userid);

            } else { 	// 재응시 한것
            	//System.out.println("444444444444444444444444  "+v_exist_ended);
            	
            	// 기존점수가 높으면 업데이트 안됨.
            	int is_score = IsResultScore(connMgr, box);

            	
            	// 획득점수 > 기존점수
            	if ( v_score > is_score) { 
            		
					isOk = UpdateTZ_examresult(connMgr,
					v_subj,     v_year,   v_subjseq,   v_lesson,
					v_examtype,    v_papernum,  v_userid, v_exam, v_examcnt, v_exampoint, v_score, v_answercnt, v_started, v_ended, v_time, v_answer, v_corrected, v_userretry, v_luserid, v_userip);

					isOk = CalcUtil.getInstance().calc_score(connMgr, CalcUtil.EXAM, v_subj, v_year, v_subjseq, v_userid);
            	} else { 
	        		int z = SelectEndedTime(connMgr, v_subj, v_year, v_subjseq, v_lesson, v_examtype, v_papernum,  v_userid);
	        		if( z > 0){	/* && 응시완료시간이 있는지 */
						// 재응시횟수 업데이트
						isOk = UpdateUserUserretry(connMgr, v_subj, v_year, v_subjseq, v_lesson, v_examtype, v_papernum, v_userid, v_userretry);
	        		}
				}
						
            }
            
        connMgr.commit();
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
	/**
	평가 시작 --> 평가지 결과지에 등록 (평가문제 푸는 시간을 알기 위해서 tz_examresult테이블에 값을 넣는다.)
	@param box      receive from the form object
	@return int
	*/
    public int InsertResult(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String              sql     = "";
        int isOk = 0;

        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_gyear");
        String v_subjseq   = box.getString("p_subjseq");

        String v_lesson    = box.getString("p_lesson");
        String v_examtype  = box.getString("p_examtype");
        int    v_papernum  = box.getInt("p_papernum");
        String v_userid    = box.getString("p_userid");
        String v_answer    = box.getString("p_answer");
        String v_exam      = box.getString("p_exam");

        String v_started   = box.getStringDefault("p_started", FormatDate.getDate("yyyyMMddHHmmss"));		// 평가 시작 시간
        String v_ended     = box.getString("p_ended");			// 평가 끝 시간
	    	double v_time = 0;

        int    v_examcnt  = box.getInt("p_examcnt");			// 문제수
        int    v_exampoint  = box.getInt("p_exampoint");		// 문제 별 점수
        int    v_userretry  = box.getInt("p_userretry");		// 재시험 가능 횟수

        int v_score     = 0;
		    int v_answercnt = 0;

        String v_luserid   = box.getSession("userid");
        String v_userip	= box.getSession("userip");

        int    v_exist     = 0;

	    	Vector v_result = null;

		    String v_corrected = "";

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

			// Added by LeeSuMin.. 2004.02.24
			// 현재 학습중인 학생신분이 아니므로 평가결과를 저장하지 않으며 결과를 확인하실 수 없습니다., 
			//if ( !EduEtc1Bean.isCurrentStudent(v_subj,v_year,v_subjseq,v_userid).equals("Y")) return 97;
            if ( !EduEtc1Bean.isCurrentStudent(v_subj,v_year,v_subjseq,v_userid,v_examtype, String.valueOf(v_papernum)).equals("Y")) return 97;
            // 평가지,평가자별 카운트j
            v_exist = chkResultExist(connMgr,   v_subj,     v_year,   v_subjseq,   v_lesson,   v_examtype,    v_papernum,  v_userid);

            // 평가점수채점
			v_result = getScore(connMgr, v_subj,     v_year,   v_subjseq,   v_lesson,  v_examtype,    v_papernum,  v_userid, v_answer.equals("") ? "" : v_exam, v_answer, v_examcnt, v_exampoint);

			v_score     = Integer.parseInt((String)v_result.get(0)); // 점수
            
			v_answercnt = Integer.parseInt((String)v_result.get(1));
			v_corrected = (String)v_result.get(2);

			v_time = v_ended.equals("")?0:FormatDate.getMinDifference(v_started, v_ended);
					
            StringTokenizer st2 = new StringTokenizer(v_answer,ExamBean.SPLIT_COMMA);
            
            v_answer = "";
            while ( st2.hasMoreElements() ) {                 
	            String s = StringManager.trim((String)st2.nextToken() );
		
	            if ( s.length() == 0) v_answer = v_answer + " " + ",";
	            else v_answer = v_answer + s + ",";
	        }
            if ( v_userretry != 0 && v_exist != 0 ) v_userretry = v_userretry-1;

            if ( v_exist == 0 ) { // 평가지,평가자별 카운트
		            
				isOk = InsertTZ_examresult(connMgr,
				v_subj,     v_year,   v_subjseq,   v_lesson,
				v_examtype,    v_papernum,  v_userid, v_exam, v_examcnt, v_exampoint, v_score, v_answercnt, v_started, v_ended, v_time, v_answer, v_corrected, v_userretry, v_luserid, v_userip);
	                              
				isOk = CalcUtil.getInstance().calc_score(connMgr,CalcUtil.EXAM,v_subj,v_year,v_subjseq,v_userid);

            } else { 
            	
            	// 기존점수가 높으면 업데이트 안됨.
            	int is_score = IsResultScore(connMgr, box);

            	// 획득점수 > 기존점수
            	if ( ( v_score > is_score ) ) { 
            		
					isOk = UpdateTZ_examresult(connMgr, v_subj, v_year, v_subjseq, v_lesson, v_examtype,
											v_papernum,  v_userid, v_exam, v_examcnt, v_exampoint, v_score, 
											v_answercnt, v_started, v_ended, v_time, v_answer, v_corrected, 
											v_userretry, v_luserid, v_userip);
	
					isOk = CalcUtil.getInstance().calc_score(connMgr, CalcUtil.EXAM,v_subj,v_year,v_subjseq,v_userid);
            	} else {
            		int z = SelectEndedTime(connMgr, v_subj, v_year, v_subjseq, v_lesson, v_examtype, v_papernum,  v_userid);
            		if( z > 0){	/* && 응시완료시간이 있는지 */
						 // 재응시횟수 업데이트
						 //isOk = UpdateUserUserretry(connMgr, v_subj, v_year, v_subjseq, v_lesson, v_examtype, v_papernum, v_userid, v_userretry);           	  
            		}
				}
						
            }
            
        connMgr.commit();
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
    
    /**
     * 응시완료시간이 있는지 여부 > 0 (응시완료시간이 존재하면 재응시가 된다.)
     * @param connMgr
     * @param p_subj
     * @param p_year
     * @param p_subjseq
     * @param p_lesson
     * @param p_examtype
     * @param p_papernum
     * @param p_userid
     * @return
     * @throws Exception
     */
    public int SelectEndedTime(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_lesson, String p_examtype, int p_papernum,  String p_userid) throws Exception { 
		int v_result 		= 0;
		String sql  		= "";
		ListSet ls      	= null;
		DataBox dbox 		= null;
		try { 
		    sql = "select count(ended) endedcnt ";
		    sql += "  from tz_examresult  ";
		    sql += " where subj = " + SQLString.Format(p_subj);
		    sql += " and year = " + SQLString.Format(p_year);
		    sql += " and subjseq = " + SQLString.Format(p_subjseq);
		    sql += " and examtype = " + SQLString.Format(p_examtype);
		    sql += " and userid = " + SQLString.Format(p_userid);
		    sql += " and papernum = " + SQLString.Format(p_papernum);
		
		    ls = connMgr.executeQuery(sql);
		
		    if ( ls.next() ) { 
		        dbox = ls.getDataBox();
		        v_result = dbox.getInt("d_endedcnt");
			}
		
		} catch ( Exception ex ) { 
		    ErrorManager.getErrorStackTrace(ex);
		    throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
		    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
		}
		
		return v_result;
	}
    /**
	평가 결과 수정 처리 
	@param box      receive from the form object
	@return int
     */
    public int UpdateFirstTZ_examresult(DBConnectionManager connMgr,
    		String p_subj,     String p_year,      String p_subjseq, String p_lesson,
    		String p_examtype,     int   p_papernum, String p_userid,  String p_exam, int p_examcnt, 
    		int p_exampoint,  int p_score, int p_answercnt,  String p_started,  String p_ended,  double p_time,
    		String p_answer, String p_corrected,  int p_userretry, String p_luserid, String p_userip) throws Exception { 
    	
    	PreparedStatement   pstmt   = null;
    	String              sql     = "";
    	String              sql1    = "";
    	int isOk = 0;
    	int index = 1;
    	try { 
    		// insert TZ_EXAMRESULT table
    		sql = " update TZ_EXAMRESULT ";
            sql += "    set exam   = ?, ";
            sql += "        examcnt   = ?, ";
            sql += "        exampoint   = ?, ";
            sql += "        score   = ?, ";
            sql += "        answercnt  = ?, ";
            //sql += "        started     = ?, ";
            sql += "        ended    = ?, ";
            sql += "        time    = ?, ";
            sql += "        answer = ?, ";
            sql += "        corrected = ?, ";
            sql += "        userretry = ?, ";
            sql += "        luserid = ?, ";
            sql += "        ldate   = ?, ";
            sql += "        user_ip = ? ";
            sql += "  where subj    = ? ";
            sql += "    and year    = ? ";
            sql += "    and subjseq = ? ";
            sql += "    and lesson = ? ";
            sql += "    and examtype = ? ";
            sql += "    and papernum = ? ";
            sql += "    and userid  = ? ";
    		
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( index++, p_exam);
            pstmt.setInt( index++, p_examcnt);
            pstmt.setInt( index++, p_exampoint);
            pstmt.setInt( index++, p_score);
            pstmt.setInt( index++, p_answercnt);
            //pstmt.setString   ( index++, p_started);
            pstmt.setString( index++, p_ended);
            pstmt.setDouble( index++, p_time);
            pstmt.setString   ( index++, p_answer);
            pstmt.setString   ( index++, p_corrected);
            pstmt.setInt(index++, p_userretry);
            pstmt.setString(index++, p_luserid);
            pstmt.setString(index++, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(index++, p_userip);
            pstmt.setString(index++, p_subj);
            pstmt.setString(index++, p_year);
			pstmt.setString(index++, p_subjseq);
			pstmt.setString(index++, p_lesson);
            pstmt.setString(index++, p_examtype);
            pstmt.setInt(index++, p_papernum);
            pstmt.setString(index++, p_userid);

            isOk = pstmt.executeUpdate();
            
    	} catch ( Exception ex ) { 
    		ErrorManager.getErrorStackTrace(ex);
    		throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
    	} finally { 
    		if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
    	}
    	
    	return isOk;
    }
	/**
	평가 결과 등록 처리 
	@param box      receive from the form object
	@return int
	*/
    public int InsertTZ_examresult(DBConnectionManager connMgr,
                                String p_subj,     String p_year,      String p_subjseq, String p_lesson,
                                String p_examtype,     int   p_papernum, String p_userid,  String p_exam, int p_examcnt, 
		                        int p_exampoint,  int p_score, int p_answercnt,  String p_started,  String p_ended,  double p_time,
                                String p_answer, String p_corrected,  int p_userretry, String p_luserid, String p_userip) throws Exception { 

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String              sql1    = "";
        int isOk = 0;

        try { 
            // insert TZ_EXAMRESULT table
            sql =  " insert into TZ_EXAMRESULT ";
            sql +=  " (subj,   year,      subjseq,  lesson, ";
            sql +=  "  examtype,  papernum, userid, ";
            sql +=  "  exam, examcnt, exampoint, score, answercnt,   started, ";
            sql +=  "  ended,  time, answer, corrected, userretry,  luserid,  ldate, user_ip, indate ) ";
            sql +=  " values ";
            sql +=  " (?,      ?,         ?,         ?, ";
            sql +=  "  ?,      ?,         ?,          ";
            sql +=  "  ?,      ?,         ?,         ?,                   ?,                    ?, ";
            sql +=  "  ?,      ?,         ?,         ?,        ?,        ?,      ?,		?,    ? ) ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, p_subj);
            pstmt.setString( 2, p_year);
            pstmt.setString( 3, p_subjseq);
            pstmt.setString( 4, p_lesson);
            pstmt.setString( 5, p_examtype);
            pstmt.setInt   ( 6, p_papernum);
            pstmt.setString( 7, p_userid);
            pstmt.setString( 8, p_exam);
            pstmt.setInt   ( 9, p_examcnt);
            pstmt.setInt   ( 10, p_exampoint);
            pstmt.setInt   ( 11, p_score);
            pstmt.setInt   ( 12, p_answercnt);
            pstmt.setString(13, p_started);
            pstmt.setString(14, p_ended);
            pstmt.setDouble(15, p_time);
			pstmt.setString(16, p_answer);
			pstmt.setString(17, p_corrected);
            pstmt.setInt   ( 18, p_userretry);
            pstmt.setString(19, p_luserid);
            pstmt.setString(20, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(21, p_userip );
            pstmt.setString(22, FormatDate.getDate("yyyyMMddHHmmss") );
            
            sql1 += " p_subj                                : " + p_subj                              + "\n";                                                             
            sql1 += " p_year                                : " + p_year                              + "\n";                                                             
            sql1 += " p_subjseq                             : " + p_subjseq                           + "\n";                                                          
            sql1 += " p_lesson                              : " + p_lesson                            + "\n";                                                           
            sql1 += " p_examtype                            : " + p_examtype                          + "\n";                                                         
            sql1 += " p_papernum                            : " + p_papernum                          + "\n";                                                         
            sql1 += " p_userid                              : " + p_userid                            + "\n";                                                           
            sql1 += " p_exam                                : " + p_exam                              + "\n";                                                             
            sql1 += " p_examcnt                             : " + p_examcnt                           + "\n";                                                          
            sql1 += " p_exampoint                           : " + p_exampoint                         + "\n";                                                       
            sql1 += " p_score                               : " + p_score                             + "\n";                                                           
            sql1 += " p_answercnt                           : " + p_answercnt                         + "\n";                                                       
            sql1 += " p_started                             : " + p_started                           + "\n";                                                          
            sql1 += " p_ended                               : " + p_ended                             + "\n";                                                            
            sql1 += " p_time                                : " + p_time                              + "\n";                                                             
            sql1 += " p_answer                              : " + p_answer                            + "\n";                                                           
            sql1 += " p_corrected                           : " + p_corrected                         + "\n";                                                        
            sql1 += " p_userretry                           : " +  p_userretry                        + "\n";                                                       
            sql1 += " p_luserid                             : " + p_luserid                           + "\n";                                                          
            sql1 += " FormatDate.getDate(yyyyMMddHHmmss)    : " + FormatDate.getDate("yyyyMMddHHmmss")+ "\n";                                          
            sql1 += " p_userip                              : " + p_userip 							  + "\n";                                          

            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }


	/**
	평가 결과 수정 처리 
	@param box      receive from the form object
	@return int
	*/
    public int UpdateTZ_examresult(DBConnectionManager connMgr,
                                String p_subj,     String p_year,      String p_subjseq, String p_lesson,
                                String p_examtype,     int   p_papernum, String p_userid,  String p_exam, int p_examcnt, 
		                        int p_exampoint,  int p_score, int p_answercnt,  String p_started,  String p_ended,  double p_time,
                                String p_answer, String p_corrected, int p_userretry,  String p_luserid, String p_userip) throws Exception { 

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // insert TZ_EXAMRESULT table
                    sql = " update TZ_EXAMRESULT ";
                    sql += "    set exam   = ?, ";
                    sql += "        examcnt   = ?, ";
                    sql += "        exampoint   = ?, ";
                    sql += "        score   = ?, ";
                    sql += "        answercnt  = ?, ";
                    //sql += "        started     = ?, ";
                    sql += "        ended    = ?, ";
                    sql += "        time    = ?, ";
                    sql += "        answer = ?, ";
                    sql += "        corrected = ?, ";
                    sql += "        userretry = ?, ";
                    sql += "        luserid = ?, ";
                    sql += "        ldate   = ?, ";
                    sql += "        user_ip  = ? ";
                    sql += "  where subj    = ? ";
                    sql += "    and year    = ? ";
                    sql += "    and subjseq = ? ";
                    sql += "    and lesson = ? ";
                    sql += "    and examtype = ? ";
                    sql += "    and papernum = ? ";
                    sql += "    and userid  = ? ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, p_exam);
            pstmt.setInt( 2, p_examcnt);
            pstmt.setInt( 3, p_exampoint);
            pstmt.setInt( 4, p_score);
            pstmt.setInt( 5, p_answercnt);
            //pstmt.setString   ( 6, p_started);
            pstmt.setString( 6, p_ended);
            pstmt.setDouble( 7, p_time);
            pstmt.setString   ( 8, p_answer);
            pstmt.setString   ( 9, p_corrected);
            pstmt.setInt(10, p_userretry);
            pstmt.setString(11, p_luserid);
            pstmt.setString(12, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(13, p_userip);
            pstmt.setString(14, p_subj);
            pstmt.setString(15, p_year);
			pstmt.setString(16, p_subjseq);
			pstmt.setString(17, p_lesson);
            pstmt.setString(18, p_examtype);
            pstmt.setInt(19, p_papernum);
            pstmt.setString(20, p_userid);

            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }


	/**
	평가 결과 수정 처리 (재채점)
	@param box      receive from the form object
	@return int
	*/
    public int UpdateTZ_examresult(DBConnectionManager connMgr,
                                String p_subj, String p_year, String p_subjseq, String p_lesson, String p_examtype, int  p_papernum, String p_userid,   
		                        int p_score, int p_answercnt,  String p_answer, String p_corrected, String p_luserid, String p_userip) throws Exception { 

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // insert TZ_EXAMRESULT table
                    sql = " update TZ_EXAMRESULT set  ";
                    sql += "        score      = ?, ";
                    sql += "        answercnt  = ?, ";
                    sql += "        answer     = ?, ";
                    sql += "        corrected  = ?, ";
                    sql += "        luserid = ?, ";
                    sql += "        ldate   = ?, ";
                    sql += "        user_ip  = ? ";
                    sql += "  where subj    = ? ";
                    sql += "    and year    = ? ";
                    sql += "    and subjseq = ? ";
                    sql += "    and lesson = ? ";
                    sql += "    and examtype = ? ";
               //     sql += "    and papernum = ? ";
                    sql += "    and userid  = ? ";
//System.out.println("점수updatq======" + sql);
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setInt( 1, p_score);
            pstmt.setInt( 2, p_answercnt);
            pstmt.setString( 3, p_answer);
            pstmt.setString( 4, p_corrected);
            pstmt.setString(5, p_luserid);
            pstmt.setString(6, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(7, p_userip);
            pstmt.setString(8, p_subj);
            pstmt.setString(9, p_year);
			pstmt.setString(10, p_subjseq);
			pstmt.setString(11, p_lesson);
            pstmt.setString(12, p_examtype);
           // pstmt.setInt(13, p_papernum);
            pstmt.setString(13, p_userid);

            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }
    
    
	/**
	평가결과 평가지,평가자별 카운트
	@param        
	@return int
	*/
    public int chkResultExist(DBConnectionManager connMgr,
                            String p_subj,     String p_year,      String p_subjseq, String p_lesson,
                            String p_examtype,    int   p_papernum, String p_userid
                            ) throws Exception { 
        ListSet             ls      = null;
        String sql  = "";
        int v_exist = 0;

        try { 
            sql = "select count(*)  cnt ";
            sql += "  from tz_examresult  ";
            sql += " where subj    = " + SQLString.Format(p_subj);
            sql += "   and year    = " + SQLString.Format(p_year);
            sql += "   and subjseq = " + SQLString.Format(p_subjseq);
            sql += "   and lesson  = " + SQLString.Format(p_lesson);
            sql += "   and examtype   = " + SQLString.Format(p_examtype);
            sql += "   and papernum= " + SQLString.Format(p_papernum);
            sql += "   and userid  = " + SQLString.Format(p_userid);

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_exist = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_exist;
    }
    /**
	평가결과 평가지,평가자별 카운트
	@param        
	@return String
     */
    public String chkResultExist1(DBConnectionManager connMgr,
    		String p_subj,     String p_year,      String p_subjseq, String p_lesson,
    		String p_examtype,    int   p_papernum, String p_userid
    ) throws Exception { 
    	ListSet             ls      = null;
    	String sql  = "";
    	String v_exist = "";
    	
    	try { 
    		sql = "select answer ";
    		sql += "  from tz_examresult  ";
    		sql += " where subj    = " + SQLString.Format(p_subj);
    		sql += "   and year    = " + SQLString.Format(p_year);
    		sql += "   and subjseq = " + SQLString.Format(p_subjseq);
    		sql += "   and lesson  = " + SQLString.Format(p_lesson);
    		sql += "   and examtype   = " + SQLString.Format(p_examtype);
    		sql += "   and papernum= " + SQLString.Format(p_papernum);
    		sql += "   and userid  = " + SQLString.Format(p_userid);
    		
    		ls = connMgr.executeQuery(sql);
    		while ( ls.next() ) { 
    			v_exist = ls.getString("answer");
    		}
    	} catch ( Exception ex ) { 
    		ErrorManager.getErrorStackTrace(ex);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	} finally { 
    		if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
    	}
    	
    	return v_exist;
    }
    /**
	평가결과 평가지,평가자별 카운트
	@param        
	@return String
     */
    public String chkResultExist2(DBConnectionManager connMgr,
    		String p_subj,     String p_year,      String p_subjseq, String p_lesson,
    		String p_examtype,    int   p_papernum, String p_userid
    ) throws Exception { 
    	ListSet             ls      = null;
    	String sql  = "";
    	String v_exist = "";
    	
    	try { 
    		sql = "select ended ";
    		sql += "  from tz_examresult  ";
    		sql += " where subj    = " + SQLString.Format(p_subj);
    		sql += "   and year    = " + SQLString.Format(p_year);
    		sql += "   and subjseq = " + SQLString.Format(p_subjseq);
    		sql += "   and lesson  = " + SQLString.Format(p_lesson);
    		sql += "   and examtype   = " + SQLString.Format(p_examtype);
    		sql += "   and papernum= " + SQLString.Format(p_papernum);
    		sql += "   and userid  = " + SQLString.Format(p_userid);
    		
    		ls = connMgr.executeQuery(sql);
    		while ( ls.next() ) { 
    			v_exist = ls.getString("ended");
    		}
    	} catch ( Exception ex ) { 
    		ErrorManager.getErrorStackTrace(ex);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	} finally { 
    		if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
    	}
    	
    	return v_exist;
    }


	/**
	평가 점수 채점 
	@param 
	@return Vector
	*/
    public Vector getScore(DBConnectionManager connMgr,
                        String p_subj, String p_year, String p_subjseq, String p_lesson, String p_examtype,
                        int p_papernum, String p_userid, String p_exam, String p_answer, int p_examcnt, int p_exampoint) throws Exception { 

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
            st1 = new StringTokenizer(p_exam,ExamBean.SPLIT_COMMA);
            while ( st1.hasMoreElements() ) { 
                v_examnums.add(StringManager.trim((String)st1.nextToken() ));
            }
			
			st2 = new StringTokenizer(p_answer,ExamBean.SPLIT_COMMA);


            while ( st2.hasMoreElements() ) { 
				String s = StringManager.trim((String)st2.nextToken() );// System.out.println("s :" + s + "a");

                v_answers.add(s);
            }
			for ( int i =0; i < v_examnums.size() ; i++ ) { 

                   v_examnum = Integer.parseInt((String)v_examnums.get(i));
				   v_answer = (String)v_answers.get(i);

                   v_temp = MakeExamResult(connMgr, p_subj, v_examnum, v_answer);

				   v_score += (v_temp * p_exampoint);
				   v_answercnt += v_temp;
				   if ( i == v_examnums.size()-1) { 
					   v_iscorrect += String.valueOf(v_temp);
				   } else { 
				       v_iscorrect += String.valueOf(v_temp) + ",";
				   }

			}
           
		    v_result.add(String.valueOf(v_score));
			v_result.add(String.valueOf(v_answercnt));
			v_result.add(v_iscorrect);

        } catch ( Exception ex ) { ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }

        return v_result;
    }

	/**
	평가 점수 계산
	@param box      receive from the form object
	@return int
	*/
    public int MakeExamResult(DBConnectionManager connMgr, String p_subj, int p_examnum, String p_answer) throws Exception { 

        int isOk = 0;

        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;

        String v_examtype = "";
        

        try { 
            sql = "select examtype ";
            sql += "  from tz_exam ";
            sql += " where subj      = " + SQLString.Format(p_subj);
            sql += " and examnum        = " + SQLString.Format(p_examnum);
//System.out.println("평가점수계산 =-=" +sql);
			ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_examtype = ls.getString("examtype");
            }

            if ( v_examtype.length() > 0 ) { 

				if ( v_examtype.equals("2") ) { // 주관식
				    
				    // 공백처리 및 소문자 변환비교
				    // 2005.11.22 edit by leechanghun
				    
				    p_answer = StringManager.replace(p_answer, " ", "");
				    p_answer = p_answer.toLowerCase();
				    p_answer = p_answer.replaceAll("'", "''");
				    //p_answer = p_answer.replaceAll("%", "/%");
				    

    				sql = "select count(b.selnum) cnt  ";
    				sql += "   from tz_exam a,  ";				
    				sql += "   (select subj, examnum, selnum, seltext, isanswer  from tz_examsel where subj    = " + SQLString.Format(p_subj);
                    sql += "   and examnum    = " + SQLString.Format(p_examnum) + "	)  b ";
                    sql += "   where a.subj = b.subj  ";
                    sql += "   and a.examnum = b.examnum  ";
                    sql += "   and lower(replace(replace(trim(b.seltext), ' ', ''),',','&#44;')) = " + SQLString.Format(p_answer.trim() );
//   System.out.println("주관식 ---" + sql);                 
				} else {  // 객관식, ox식
                    int p_selnum = 0;
					if ( !p_answer.trim().equals("") ) { 
					    p_selnum = Integer.parseInt(p_answer.trim() );
			        }

    				sql = "select count(b.selnum) cnt  ";
    				sql += "   from tz_exam a,  ";				
    				sql += "   (select subj, examnum, selnum, isanswer  from tz_examsel where subj    = " + SQLString.Format(p_subj);
                    sql += "   and examnum    = " + SQLString.Format(p_examnum) + "	)  b ";
                    sql += "   where b.isanswer    =   'Y'   ";
                    sql += "   and a.subj = b.subj  ";
                    sql += "   and a.examnum = b.examnum  ";
                    sql += "   and b.selnum    =    " + p_selnum;									
				}
//  System.out.println("객관식 ---" + sql);   
                ls.close();
                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    dbox = ls.getDataBox();

                    if ( ls.getInt("cnt") > 0) { 
					     isOk = 1;
					}

                }

            }
        } catch ( Exception ex ) { ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }  
    
	/**
	평가자 결과 보기
	@param 
	@return ArrayList
	*/
    public ArrayList SelectUserPaperResult(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;

        String v_action = box.getStringDefault("p_action",  "change");

        String v_subj = box.getString("p_subj");
		String v_year     = box.getString("p_gyear");
        String v_subjseq   = box.getString("p_subjseq");
        String v_lesson    = box.getString("p_lesson");
        String v_examtype    = box.getString("p_examtype");
        int v_papernum    = box.getInt("p_papernum");
        String v_userid    = box.getString("p_userid");

        try { 
                connMgr = new DBConnectionManager();
                list = getPaperResultList(connMgr, v_subj, v_year, v_subjseq, v_lesson, v_examtype, v_papernum, v_userid);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


	/**
	평가자 결과 보기
	@param 
	@return ArrayList
	*/
    public ArrayList getPaperResultList(DBConnectionManager connMgr,
                    String p_subj, String p_year, String p_subjseq, String p_lesson, String p_examtype, int p_papernum, String p_userid) throws Exception { 

        ArrayList QuestionExampleDataList  = null;
        ArrayList list = new ArrayList();
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;


        try { 
            sql = "select a.isopenanswer, a.isopenexp, b.exam, b.answer, b.corrected ";
            sql += "  from tz_exampaper  a, ";
            sql += "       tz_examresult b ";
            sql += " where a.subj    = b.subj ";
            sql += "   and a.year    = b.year ";
            sql += "   and a.subjseq = b.subjseq ";
            sql += "   and a.lesson = b.lesson ";
            sql += "   and a.examtype = b.examtype ";
            sql += "   and a.papernum = b.papernum ";
            sql += " and b.subj = " + SQLString.Format(p_subj);
            sql += " and b.year = " + SQLString.Format(p_year);
            sql += " and b.subjseq = " + SQLString.Format(p_subjseq);
            sql += " and b.lesson = " + SQLString.Format(p_lesson);
            sql += " and b.examtype = " + SQLString.Format(p_examtype);
           // sql += " and b.papernum = " + SQLString.Format(p_papernum);
            sql += " and b.userid = " + SQLString.Format(p_userid);
            sql += "   and rownum <= 1 ";
            sql += " order by a.subj, a.year, a.subjseq, b.userid ";
//System.out.println("=평가자 결과보기=" + sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
			}
            Log.info.println("dbox.getString(d_exam) >>  >> " +dbox.getString("d_exam") );
            QuestionExampleDataList = getExampleData(connMgr, p_subj, dbox.getString("d_exam") );		
		
		} catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return QuestionExampleDataList;
    }



    /**
    평가 결과 리스트 
    @param box          receive from the form object and session
    @return ArrayList   해당과목리스트
    */
    public ArrayList SelectUserPaperResult2(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;

        String v_action = box.getStringDefault("p_action",  "change");

        String v_subj = box.getString("p_subj");
		String v_year     = box.getString("p_gyear");
        String v_subjseq   = box.getString("p_subjseq");
        String v_lesson    = box.getString("p_lesson");
        String v_examtype    = box.getString("p_examtype");
        int v_papernum    = box.getInt("p_papernum");
        String v_userid    = box.getString("p_userid");

        try { 
                connMgr = new DBConnectionManager();
                list = getPaperResultList2(connMgr, v_subj, v_year, v_subjseq, v_lesson, v_examtype, v_userid);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    평가 결과 리스트 
    @param box          receive from the form object and session
    @return ArrayList   
    */
    public ArrayList getPaperResultList2(DBConnectionManager connMgr,
                    String p_subj, String p_year, String p_subjseq, String p_lesson, String p_examtype, String p_userid) throws Exception { 

        ArrayList list = new ArrayList();
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;
		StringTokenizer st = null;
		Vector v_answer = null;
		StringTokenizer st2 = null;
		Vector v_corrected = null;


        try { 
            sql = "select a.isopenanswer, a.isopenexp, b.exam, b.answer, b.corrected, ";
            sql += " b.examcnt, b.answercnt, b.score, b.exampoint, b.started, b.ended,  ";
            sql += " a.year, a.lesson, a.subjseq, c.subjnm,  GET_CODENM(" + SQLString.Format(ExamBean.PTYPE) + ", nvl(a.examtype, '')) examtypenm  ";
            sql += "  from tz_exampaper  a, ";
            sql += "       tz_examresult b, ";
            sql += "       tz_subj c ";
            sql += " where a.subj( +)        = c.subj  ";
            sql += " and a.subj    = b.subj ";
            sql += "   and a.year    = b.year ";
            sql += "   and a.subjseq = b.subjseq ";
            sql += "   and a.lesson = b.lesson ";
            sql += "   and a.examtype = b.examtype ";
            sql += "   and a.papernum = b.papernum ";
            sql += " and b.subj = " + SQLString.Format(p_subj);
            sql += " and b.year = " + SQLString.Format(p_year);
            sql += " and b.subjseq = " + SQLString.Format(p_subjseq);
            sql += " and b.lesson = " + SQLString.Format(p_lesson);
            sql += " and b.examtype = " + SQLString.Format(p_examtype);
        //    sql += " and b.papernum = " + SQLString.Format(p_papernum);
            sql += " and b.userid = " + SQLString.Format(p_userid);
            sql += "   and rownum <= 1 ";
            sql += " order by a.subj, a.year, a.subjseq, b.userid ";

//System.out.println("평가결과 리스트 ---- " + sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
				st = new StringTokenizer(dbox.getString("d_answer"),",");
				v_answer = new Vector();
                while ( st.hasMoreElements() ) { 
                     v_answer.add(st.nextToken() );
				}
				st2 = new StringTokenizer(dbox.getString("d_corrected"),",");
				v_corrected = new Vector();
                while ( st2.hasMoreElements() ) { 
                     v_corrected.add(st2.nextToken() );
				}
			}
       
            sql = "select count(b.userid) cnt ";
            sql += "     from  tz_examresult b ";
            sql += " where b.subj = " + SQLString.Format(p_subj);
            sql += " and b.year = " + SQLString.Format(p_year);
            sql += " and b.subjseq = " + SQLString.Format(p_subjseq);
            sql += " and b.lesson = " + SQLString.Format(p_lesson);
            sql += " and b.examtype = " + SQLString.Format(p_examtype);
           // sql += " and b.papernum = " + SQLString.Format(p_papernum);
         //   sql += " and b.score > " + SQLString.Format(dbox.getString("d_score") );
			
               ls.close();
                ls = connMgr.executeQuery(sql);
//System.out.println("=sql -평가결과 키운트=" + sql);
                while ( ls.next() ) { 
                    dbox.put("d_overman", ls.getString("cnt") );
                }

				list.add(dbox);
				list.add(v_answer);
				list.add(v_corrected);

		
		} catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return list;
    }

    /**
    문제당 문제항목 리스트
    @param box          receive from the form object and session
    @return QuestionExampleData   평가문제
    */
    public ArrayList getExampleData(DBConnectionManager connMgr, String p_subj,  String p_examnums) throws Exception { 
        Hashtable hash = new Hashtable();
        ArrayList blist = new ArrayList();
		ArrayList           list    = null;

        ListSet ls  = null;
        String sql  = "";
        DataBox             dbox    = null;
        StringTokenizer st = null;

        try { 

			st = new StringTokenizer(p_examnums, ",");

            while ( st.hasMoreElements() ) { 

                int examnum = Integer.parseInt(st.nextToken() );

	            sql = "select a.subj,     a.examnum,  a.lesson,  a.examtype, ";
	            sql += "       a.examtext,      a.exptext,   a.levels,  a.selcount,  a.saveimage,   a.saveaudio, ";
	            sql += "       a.savemovie,  a.saveflash, a.realimage,   a.realaudio, a.realmovie, a.realflash, ";
	            sql += "       b.selnum,   b.seltext,  b.isanswer,  ";
	            sql += "       b.saveselimage,   b.realselimage,  b.isselimage,  ";
	            sql += "       c.codenm    examtypenm, ";
	            sql += "       d.codenm    levelsnm    ";
	            sql += "  from tz_exam      a, ";
	            sql += "       tz_examsel   b, ";
	            sql += "       tz_code      c, ";
	            sql += "       tz_code      d  ";
	            sql += " where a.subj     = b.subj( +) ";
	            sql += "   and a.examnum  = b.examnum( +) ";
	            sql += "   and a.examtype = c.code ";
	            sql += "   and a.levels   = d.code ";
	            sql += "   and a.subj     = " + SQLString.Format(p_subj);
	            sql += "   and a.examnum  = " + examnum;
	            sql += "   and c.gubun    = " + SQLString.Format(ExamBean.EXAM_TYPE);
	            sql += "   and d.gubun    = " + SQLString.Format(ExamBean.EXAM_LEVEL);
	            sql += " order by a.examnum, b.selnum ";

            	ls = connMgr.executeQuery(sql);
				list =  new ArrayList();

            	while ( ls.next() ) { 
                	dbox = ls.getDataBox();
                	list.add(dbox);

				}
			    blist.add(list);
			    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return blist;
    }


    /**
    평가 결과 문제 데이타 
    @param box          receive from the form object and session
    @return ArrayList   
    */
    public ArrayList getUserData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;
		int v_result = 0;

        String v_userid   = box.getSession("userid");
        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq  = box.getString("p_subjseq");

        try { 
                connMgr = new DBConnectionManager();
				list = new ArrayList();
                    v_result = getUserData(connMgr, v_subj, v_year, v_subjseq, v_userid, "M");
                    
				list.add(String.valueOf(v_result));
                    v_result = getUserData(connMgr, v_subj, v_year, v_subjseq, v_userid, "H");
                    
				list.add(String.valueOf(v_result));
                    v_result = getUserData(connMgr, v_subj, v_year, v_subjseq, v_userid, "E");
                    
				list.add(String.valueOf(v_result));
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    평가 결과 문제 데이타 
    @param box          receive from the form object and session
    @return ArrayList   
    */
    public int getUserData(DBConnectionManager connMgr,
                    String p_subj, String p_year, String p_subjseq, String p_userid, String p_examtype) throws Exception { 

        ArrayList QuestionExampleDataList  = null;
        int v_result = 0;
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;


        try { 
            sql = "select count(papernum) examcount ";
            sql += "  from tz_exampaper  ";
            sql += " where subj = " + SQLString.Format(p_subj);
            sql += " and year = " + SQLString.Format(p_year);
            sql += " and subjseq = " + SQLString.Format(p_subjseq);
            sql += " and examtype = " + SQLString.Format(p_examtype);

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox = ls.getDataBox();
                v_result = dbox.getInt("d_examcount");
			}
		
		} catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_result;
    }


    /**
    평가 결과 개인별  문제 데이타 
    @param box          receive from the form object and session
    @return ArrayList   
    */
    public ArrayList getUserResultData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;
		int v_result = 0;

        String v_userid     = box.getStringDefault("p_userid", box.getSession("userid"));
        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq     = box.getString("p_subjseq");

        try { 
                connMgr = new DBConnectionManager();
				list = new ArrayList();
                
                v_result = getUserResultData(connMgr, v_subj, v_year, v_subjseq, v_userid, "M");
				list.add(String.valueOf(v_result));
                
                v_result = getUserResultData(connMgr, v_subj, v_year, v_subjseq, v_userid, "H");
				list.add(String.valueOf(v_result));
                
                v_result = getUserResultData(connMgr, v_subj, v_year, v_subjseq, v_userid, "E");
				list.add(String.valueOf(v_result));
                
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    평가 결과 개인별 문제 데이타 
    @param box          receive from the form object and session
    @return int   
    */
    public int getUserResultData(DBConnectionManager connMgr,
                    String p_subj, String p_year, String p_subjseq, String p_userid, String p_examtype) throws Exception { 

        ArrayList QuestionExampleDataList  = null;
        int v_result = 0;
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;


        try { 
            sql = "select count(score) examcount ";
            sql += "  from tz_examresult  ";
            sql += " where subj = " + SQLString.Format(p_subj);
            sql += " and year = " + SQLString.Format(p_year);
            sql += " and subjseq = " + SQLString.Format(p_subjseq);
            sql += " and examtype = " + SQLString.Format(p_examtype);
            sql += " and userid = " + SQLString.Format(p_userid);

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox = ls.getDataBox();
                v_result = dbox.getInt("d_examcount");
			}
		
		} catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_result;
    }


    /**
    평가 문제 차시 구함
    @param box          receive from the form object and session
    @return String   
    */
    public String getProgressData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        DataBox             dbox    = null;
        String              sql     = "";
        String v_research = "";
						
        try { 
        String s_userid     = box.getSession("userid");
        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq     = box.getString("p_subjseq");


			connMgr = new DBConnectionManager();
            
			sql = "select a.lesson  ";
            sql += "  from tz_progress a ";
            sql += "   where a.subj   = " + SQLString.Format(v_subj);
            sql += "   and a.year   = " + SQLString.Format(v_year);
            sql += "   and a.subjseq   = " + SQLString.Format(v_subjseq);
            sql += "   and a.userid   = " + SQLString.Format(s_userid);
            sql += "   and a.lessonstatus = 'complete' ";
            sql += "   and rownum <= 1 ";
            sql += "   order by a.lesson desc ";

            ls = connMgr.executeQuery(sql);
            
            if ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                v_research = dbox.getString("d_lesson");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_research;
    }
    
    /**
    운영자권한 테이블에 수정
    @param box      receive from the form object and session
    @return int
    */
     public int UpdateUserRetry(RequestBox box) throws Exception { 
     	DBConnectionManager connMgr     = null;
        String sql                 = "";
        
        
        String v_subj     = box.getString("p_subj"); 
	    String v_year     = box.getString("p_gyear"); 
		String v_subjseq  = box.getString("p_subjseq"); 
		String v_lesson   = box.getString("p_lesson"); 
		String v_examtype = box.getString("p_examtype"); 
	    String v_papernum = box.getString("p_papernum"); 
	    String v_userid   = box.getString("p_userid");
	    String v_userretrycnt  = box.getStringDefault("p_userretrycnt","0");


        int    isOk                 = 0;
        try { 
        	connMgr = new DBConnectionManager();
        	connMgr.setAutoCommit(false);
        	
            // delete TZ_MANAGER table
            sql =  "update tz_examresult set userretry = " + v_userretrycnt + " ";
            sql +=  "where  subj = '" + v_subj + "' and ";
            sql +=  "	   year = '" + v_year + "' and ";
            sql +=  "	   subjseq = '" + v_subjseq + "' and ";
            sql +=  "	   lesson = '" + v_lesson + "' and ";
            sql +=  "	   examtype = '" + v_examtype + "' and ";
            sql +=  "	   papernum = '" + v_papernum + "' and ";
            sql +=  "	   userid = '" + v_userid + "' ";

            Log.info.println("재응시 >>  > " +sql);
            isOk = connMgr.executeUpdate(sql);
            
            if ( isOk == 1)
            	connMgr.commit();
            else
            	connMgr.rollback();

        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

	/**
	평가지 전체 보기(재채점 뷰)
	@param 
	@return ArrayList
	*/
    public ArrayList SelectPaperResult(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList QuestionExampleDataList  = null;
        ArrayList list = new ArrayList();
        ListSet             ls      = null;
        DataBox             dbox    = null;        
        String sql    = "";
        String s_exam = ""; // 문제번호


        String v_subj = box.getString("p_subj");

        try { 
            connMgr = new DBConnectionManager();
            
            sql = " select examnum from TZ_EXAM where subj='" +v_subj + "' order by examnum asc " ; 
//         System.out.println("과목재채점 ::::" + sql);
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                s_exam = s_exam + ls.getInt("examnum") + ","; 
			}
            ls.close();
            Log.info.println(" >>  >>  >>  >>  >>  >>  >>  > " +s_exam);
            QuestionExampleDataList = getExampleData(connMgr, v_subj, s_exam);
            
                    			
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
        	if ( ls != null ) { try { ls.close(); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return QuestionExampleDataList;
    }

	/**
	평가 id별 재채점 
	@param box      receive from the form object
	@return int
	*/
    public int InsertReRating(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;        
        String              sql     = "";
        int isOk = 0;

        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_gyear");
        String v_subjseq   = box.getString("p_subjseq");
        String v_lesson    = box.getString("p_lesson");
        String v_examtype  = box.getString("p_examtype");
        int    v_papernum  = box.getInt("p_papernum");
        String v_userid    = box.getString("p_userid");
        String v_luserid   = box.getSession("userid");   // 최종수정자
        String v_change1    = box.getString("p_change1");  // 재채점정보(객관식)
        String v_change2    = box.getString("p_change2");  // 재채점정보(주관식)        

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 새로운객관식정답 update 
            if ( !v_change1.equals("") ) {            
                isOk = UpdateNewAnswer(connMgr, v_change1, v_subj, v_luserid);
            }    
            
            // 새로운주관식정답 update 
            if ( !v_change2.equals("") ) { 
                isOk = UpdateNewAnswerSubjective(connMgr, v_change2, v_subj, v_luserid);   
            }
            
            // id별 재채점 처리
            isOk = UpdateIdReRating(connMgr, box);

        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            connMgr.commit();
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

	/**
	평가 과목별 재채점 
	@param box      receive from the form object
	@return int
	*/
    public int InsertAllReRating(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;        
        String              sql     = "";
        int isOk = 0;
        
		Vector v_result = null;
        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_gyear");
        String v_subjseq   = box.getString("p_subjseq");
        String v_lesson    = box.getString("p_lesson");
        String v_examtype  = box.getString("p_examtype");
        int    v_papernum  = box.getInt("p_papernum");
        String v_userid    = box.getString("p_userid");
        String v_luserid   = box.getSession("userid");   // 최종수정자
        String v_change1   = box.getString("p_change1");  // 재채점정보(객관식)
        String v_change2   = box.getString("p_change2");  // 재채점정보(주관식)  

        String v_exam      = ""; // 문제조합
        int    v_examcnt   = 0;  // 문제수
        int    v_exampoint = 0;  // 문제당 배점
        String v_started   = ""; // 시작시간
        String v_ended     = ""; // 종료시간
		double v_time      = 0;  // 소요시간
        int    v_userretry = 0;  // 사용자재응시횟수

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 새로운객관식정답 update 
            if ( !v_change1.equals("") ) {            
                isOk = UpdateNewAnswer(connMgr, v_change1, v_subj, v_luserid);
            }    
            
            // 새로운주관식정답 update 
            if ( !v_change2.equals("") ) { 
                isOk = UpdateNewAnswerSubjective(connMgr, v_change2, v_subj, v_luserid);   
            }
            
            // 조회된 학습자 데이타 select            
            sql = " SELECT userid, lesson, examtype FROM TZ_EXAMRESULT  "
                + " WHERE  subj = " + SQLString.Format(v_subj) 
                + " AND    year = " + SQLString.Format(v_year)
                + " AND    subjseq =  " + SQLString.Format(v_subjseq)
                + " and Ended is not null "; //미제출자는 제외
              //  + " AND    papernum = " + SQLString.Format(v_papernum)
              //  + " AND    answer is not null ";
//System.out.println("학습자 데이타 -----" + sql);
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                box.put("p_userid",  ls.getString("userid") );  
                box.put("p_lesson",  ls.getString("lesson") );  
                box.put("p_examtype",ls.getString("examtype") );      

                // id별 재채점 처리
                isOk = UpdateIdReRating(connMgr, box);
            }

        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            connMgr.commit();
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }   


	/**
	평가 id별 재채점 UPDATE
	@param box      receive from the form object
	@return int
	*/
    public int UpdateIdReRating(DBConnectionManager connMgr, RequestBox box) throws Exception { 
        ListSet             ls      = null;        
        String              sql     = "";
        int isOk = 0;
        
		Vector v_result = null;
        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_gyear");
        String v_subjseq   = box.getString("p_subjseq");
        String v_lesson    = box.getString("p_lesson");
        String v_examtype  = box.getString("p_examtype");
        int    v_papernum  = box.getInt("p_papernum");
        String v_userid    = box.getString("p_userid");
        String v_luserid   = box.getSession("userid");   // 최종수정자
        String v_change    = box.getString("p_change");  // 재채점정보
        String v_userip    = box.getSession("userip");
        
        String v_exam      = ""; // 문제조합
        int    v_examcnt   = 0;  // 문제수
        int    v_exampoint = 0;  // 문제당 배점
        String v_answer    = ""; // 평가답안
        String v_started   = ""; // 시작시간
        String v_ended     = ""; // 종료시간
		double v_time      = 0;  // 소요시간
        int    v_userretry = 0;  // 사용자재응시횟수

        try { 
            // 기존 데이타 select
            sql = " select exam, examcnt, exampoint, answer, started, ended, time, userretry "
                + " from TZ_EXAMRESULT "
                + " where subj    = " + SQLString.Format(v_subj)
                + "   and year    = " + SQLString.Format(v_year)
                + "   and subjseq = " + SQLString.Format(v_subjseq)
                + "   and lesson  = " + SQLString.Format(v_lesson)
                + "   and examtype   = " + SQLString.Format(v_examtype)
            //    + "   and papernum= " + SQLString.Format(v_papernum)
                + "   and userid  = " + SQLString.Format(v_userid);
//System.out.println("=평가ID 체점="+sql);            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_exam      = ls.getString("exam");    // 문제조합
                v_examcnt   = ls.getInt("examcnt");    // 문제수
                v_exampoint = ls.getInt("exampoint");  // 문제당 배점
                v_answer    = ls.getString("answer");  // 평가답안                
                v_started   = ls.getString("started"); // 시작시간
                v_ended     = ls.getString("ended");   // 종료시간
                v_time      = ls.getDouble("time");    // 소요시간
                v_userretry = ls.getInt("userretry");  // 사용자재응시횟수                        
            }
            
            // 평가점수채점
    		v_result = getScore(connMgr, v_subj, v_year,  v_subjseq,   v_lesson,  v_examtype,   v_papernum,  v_userid, v_exam, v_answer, v_examcnt, v_exampoint);
            int v_score     = Integer.parseInt((String)v_result.get(0)); // 획득점수
			int v_answercnt = Integer.parseInt((String)v_result.get(1)); // 정답수 
			String v_corrected = (String)v_result.get(2);                // 정답유무


            // update
			isOk = UpdateTZ_examresult(connMgr, v_subj,     v_year,   v_subjseq,   v_lesson, v_examtype,    v_papernum,  v_userid, 
			                           v_score, v_answercnt,  v_answer, v_corrected, v_luserid, v_userip);

            // tz_student 점수계산
			isOk = CalcUtil.getInstance().calc_score(connMgr, CalcUtil.EXAM, v_subj,v_year,v_subjseq,v_userid);

        } catch ( Exception ex ) { 
            isOk = 0;
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }
    
        
	/**
	평가문제 새로운 객관식 정답 UPDATE
	@param box      receive from the form object
	@return int
	*/
    public int UpdateNewAnswer(DBConnectionManager connMgr, String v_change, String v_subj, String v_luserid) throws Exception { 
        PreparedStatement   pstmt   = null;        
        String              sql     = "";
        int isOk = 0;
        
        try { 

            StringTokenizer st_exam = new StringTokenizer(v_change, "|");
            int s_examnum = 0;  
            int s_selnum  = 0; 
            while ( st_exam.hasMoreElements() ) { 
		        String s = StringManager.trim((String)st_exam.nextToken() );     		        
	            String[] tokens = s.split("\\^"); 
	            s_examnum = Integer.parseInt(tokens[0]);   // 문제번호           
	            s_selnum  = Integer.parseInt(tokens[1]);   // 문제보기 정답번호  

                // 문제항목 정답필드 '' 셋팅
                sql =  "  update TZ_EXAMSEL  set isanswer='' , luserid=? , ldate=?  WHERE subj=?  and examnum=?  ";
                pstmt = connMgr.prepareStatement(sql);
                pstmt.setString(1, v_luserid);            
                pstmt.setString(2, FormatDate.getDate("yyyyMMddHHmmss") );
                pstmt.setString(3, v_subj);            
                pstmt.setInt   (4, s_examnum);            
                isOk = pstmt.executeUpdate();
                
                if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }

                if ( isOk > 0) { 
                    // 새로운 정답 Y 셋팅
                    sql =  "  update TZ_EXAMSEL set isanswer='Y' WHERE subj=? and examnum=? and selnum=?  ";
                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, v_subj);            
                    pstmt.setInt   (2, s_examnum);            
                    pstmt.setInt   (3, s_selnum);                            
                    isOk = pstmt.executeUpdate();
                    
                    if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
                }
	        }
        } catch ( Exception ex ) { ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }  
    
	/**
	평가문제 새로운 주관식 정답 UPDATE
	@param box      receive from the form object
	@return int
	*/
    public int UpdateNewAnswerSubjective(DBConnectionManager connMgr, String v_change, String v_subj, String v_luserid) throws Exception { 
        PreparedStatement   pstmt   = null;        
        String              sql     = "";
        int isOk = 0;
        
        try { 
//          StringTokenizer st_exam = new StringTokenizer(v_change, ExamBean.SPLIT_COMMA); // 문제번호^정답^정답,문제번호^정답^정답,...
            StringTokenizer st_exam = new StringTokenizer(v_change, "|"); // 문제번호^정답^정답,문제번호^정답^정답,...
            int s_examnum = 0;  
            String s_selnum  = ""; 
            while ( st_exam.hasMoreElements() ) { 
		        String s = StringManager.trim((String)st_exam.nextToken() );     		    
	            String[] tokens = s.split("\\^"); 
	            
	            s_examnum = Integer.parseInt(tokens[0]);   // 문제번호          
	            
                // 문제항목 정답필드 delete
                sql =  "  delete from tz_examsel where subj=? and examnum=?  ";
                pstmt = connMgr.prepareStatement(sql);
                pstmt.setString(1, v_subj);            
                pstmt.setInt   (2, s_examnum);            
                isOk = pstmt.executeUpdate();
                
                if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }

	            // 새로운 정답 insert
	            for ( int e=1; e<tokens.length; e++ ) { 
	                s_selnum  = tokens[e];   // 문제정답
    
                    sql =  "insert into TZ_EXAMSEL(subj, examnum, selnum, seltext, luserid, ldate) ";
                    sql +=  " values (?, ?, ?, ?, ?, ?)";
    
                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, v_subj);            
                    pstmt.setInt   (2, s_examnum);                
                    pstmt.setInt   (3, e);            // 정답번호
                    pstmt.setString(4, s_selnum);     // 주관식정답
                    pstmt.setString(5, v_luserid);     
                    pstmt.setString(6, FormatDate.getDate("yyyyMMddHHmmss") );
                                             
                    isOk = pstmt.executeUpdate();
	            }
	            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
	        }
        } catch ( Exception ex ) { ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }
    
 
 
	/**
	평가 제출시 기존 시험이 최고점수면 업데이트 안됨
	@param box      receive from the form object
	@return int
	*/
    public int IsResultScore(DBConnectionManager connMgr, RequestBox box) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
        int is_score = 0;
	    	ResultSet rs = null;

        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_gyear");
        String v_subjseq   = box.getString("p_subjseq");

        String v_lesson    = box.getString("p_lesson");
        String v_examtype  = box.getString("p_examtype");
        int    v_papernum  = box.getInt("p_papernum");
        String v_userid    = box.getString("p_userid");
        String v_answer    = box.getString("p_answer");
        String v_exam      = box.getString("p_exam");

        String v_started   = box.getString("p_started");
        String v_ended     = box.getString("p_ended");
		    double v_time = 0;

        int    v_examcnt  = box.getInt("p_examcnt");
        int    v_exampoint  = box.getInt("p_exampoint");
        int    v_userretry  = box.getInt("p_userretry");

        int v_score     = 0;
		    int v_answercnt = 0;

        String v_luserid   = box.getSession("userid");

        int    v_exist     = 0;

		Vector v_result = null;

		String v_corrected = "";

        try { 
            sql = " select score from tz_examresult where subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' and examtype='" +v_examtype + "' and papernum=" +v_papernum + " and userid='" +v_userid + "' ";
            pstmt = connMgr.prepareStatement(sql);
            rs = pstmt.executeQuery();
    		if ( rs.next() ) { 
				is_score = rs.getInt("score");
    		}	
		    		
        } catch ( Exception ex ) { 
            is_score = 0;
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
        }

        return is_score;
    }               
    
	/**
	평가 결과 재응시횟수 update(학습창에서..)
	@param box      receive from the form object
	@return int
	*/
    public int UpdateUserUserretry(DBConnectionManager connMgr,
                                String p_subj,     String p_year,      String p_subjseq, String p_lesson,
                                String p_examtype,     int   p_papernum, String p_userid,  int p_userretry) throws Exception { 

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // insert TZ_EXAMRESULT table
                    sql = " update TZ_EXAMRESULT ";
                    sql += "    set ";
                    sql += "        userretry = ?, ";
                    sql += "        ldate   = ? ";
                    sql += "  where subj    = ? ";
                    sql += "    and year    = ? ";
                    sql += "    and subjseq = ? ";
                    sql += "    and lesson = ? ";
                    sql += "    and examtype = ? ";
                    sql += "    and papernum = ? ";
                    sql += "    and userid  = ? ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setInt(1, p_userretry);
            pstmt.setString(2, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(3, p_subj);
            pstmt.setString(4, p_year);
						pstmt.setString(5, p_subjseq);
						pstmt.setString(6, p_lesson);
            pstmt.setString(7, p_examtype);
            pstmt.setInt(8, p_papernum);
            pstmt.setString(9, p_userid);

            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }    


  // 평가 제출시 임시테이블에 결과데이타 저장해서 결과보여줌 - 실제 결과데이타와의 구분을 두기 위해 // 
  //// // TEMP//// //// //// //// //// //// //// //// //// //// // 2005.10    
	/**
	평가 제출시 TZ_EXAMRESULTTEMP 테이블에 결과 등록
	@param box      receive from the form object
	@return int
	*/
    public int InsertResultTemp(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String              sql     = "";
        int isOk = 0;
        
        PreparedStatement   pstmt   = null;
	    	ResultSet rs = null;        
        

        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_gyear");
        String v_subjseq   = box.getString("p_subjseq");

        String v_lesson    = box.getString("p_lesson");
        String v_examtype  = box.getString("p_examtype");
        int    v_papernum  = box.getInt("p_papernum");
        String v_userid    = box.getString("p_userid");
        String v_answer    = box.getString("p_answer");
        String v_exam      = box.getString("p_exam");

        String v_started   = box.getString("p_started");
        String v_ended     = box.getString("p_ended");
	    double v_time = 0;

        int    v_examcnt  = box.getInt("p_examcnt");
        int    v_exampoint  = box.getInt("p_exampoint");
        int    v_userretry  = box.getInt("p_userretry");

        int v_score     = 0;
		    int v_answercnt = 0;

        String v_luserid   = box.getSession("userid");
        String v_userip    = box.getSession("userip");

        int    v_exist     = 0;

	    	Vector v_result = null;

		    String v_corrected = "";

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 평가지,평가자별 카운트
            v_exist = chkResultExistTemp(connMgr,   v_subj,     v_year,   v_subjseq,   v_lesson,   v_examtype,    v_papernum,  v_userid);
            
            // 평가점수채점
			v_result = getScore(connMgr, v_subj,     v_year,   v_subjseq,   v_lesson,  v_examtype,    v_papernum,  v_userid, v_exam, v_answer, v_examcnt, v_exampoint);
						
            v_score     = Integer.parseInt((String)v_result.get(0)); // 점수
            
			v_answercnt = Integer.parseInt((String)v_result.get(1));
			v_corrected = (String)v_result.get(2);

			v_time = FormatDate.getMinDifference(v_started, v_ended);
					
            StringTokenizer st2 = new StringTokenizer(v_answer,ExamBean.SPLIT_COMMA);
            
            v_answer = "";
            while ( st2.hasMoreElements() ) { 
                
	            String s = StringManager.trim((String)st2.nextToken() );
	
	            if ( s.length() == 0) v_answer = v_answer + " " + ",";
	            else v_answer = v_answer + s + ",";
	        }

            if ( v_userretry != 0) v_userretry = v_userretry-1;
		    		
            if ( v_exist == 0 ) { 		            
		  		isOk = InsertTZ_examresultTemp(connMgr,
				        v_subj,     v_year,   v_subjseq,   v_lesson,
				        v_examtype,    v_papernum,  v_userid, v_exam, v_examcnt, v_exampoint, v_score, v_answercnt, v_started, v_ended, v_time, v_answer, v_corrected, v_userretry, v_luserid, v_userip);
            } else { 
            		
				isOk = UpdateTZ_examresultTemp(connMgr,
							v_subj,     v_year,   v_subjseq,   v_lesson,
							v_examtype,    v_papernum,  v_userid, v_exam, v_examcnt, v_exampoint, v_score, v_answercnt, v_started, v_ended, v_time, v_answer, v_corrected, v_userretry, v_luserid, v_userip);						
            }
            
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            connMgr.commit();
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }    


	/**
	평가 결과 임시테이블 등록 처리 
	@param box      receive from the form object
	@return int
	*/
    public int InsertTZ_examresultTemp(DBConnectionManager connMgr,
                                String p_subj,     String p_year,      String p_subjseq, String p_lesson,
                                String p_examtype,     int   p_papernum, String p_userid,  String p_exam, int p_examcnt, 
		                        int p_exampoint,  int p_score, int p_answercnt,  String p_started,  String p_ended,  double p_time,
                                String p_answer, String p_corrected,  int p_userretry, String p_luserid, String p_userip) throws Exception { 

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // insert TZ_EXAMRESULT table
            sql =  " insert into TZ_EXAMRESULTTEMP ";
            sql +=  " (subj,   year,      subjseq,  lesson, ";
            sql +=  "  examtype,  papernum, userid, ";
            sql +=  "  exam, examcnt, exampoint, score, answercnt,   started, ";
            sql +=  "  ended,  time, answer, corrected, userretry,  luserid,  ldate, user_ip) ";
            sql +=  " values ";
            sql +=  " (?,      ?,         ?,         ?, ";
            sql +=  "  ?,      ?,         ?,          ";
            sql +=  "  ?,      ?,         ?,         ?,                   ?,                    ?, ";
            sql +=  "  ?,      ?,         ?,         ?,        ?,        ?,      ?,		? ) ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, p_subj);
            pstmt.setString( 2, p_year);
            pstmt.setString( 3, p_subjseq);
            pstmt.setString( 4, p_lesson);
            pstmt.setString( 5, p_examtype);
            pstmt.setInt   ( 6, p_papernum);
            pstmt.setString( 7, p_userid);
            pstmt.setString( 8, p_exam);
            pstmt.setInt   ( 9, p_examcnt);
            pstmt.setInt   ( 10, p_exampoint);
            pstmt.setInt   ( 11, p_score);
            pstmt.setInt   ( 12, p_answercnt);
            pstmt.setString(13, p_started);
            pstmt.setString(14, p_ended);
            pstmt.setDouble(15, p_time);
						pstmt.setString(16, p_answer);
						pstmt.setString(17, p_corrected);
            pstmt.setInt   ( 18, p_userretry);
            pstmt.setString(19, p_luserid);
            pstmt.setString(20, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(21, p_userip);

            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }


	/**
	평가 결과 임시테이블  수정 처리 
	@param box      receive from the form object
	@return int
	*/
    public int UpdateTZ_examresultTemp(DBConnectionManager connMgr,
                                String p_subj,     String p_year,      String p_subjseq, String p_lesson,
                                String p_examtype,     int   p_papernum, String p_userid,  String p_exam, int p_examcnt, 
		                        int p_exampoint,  int p_score, int p_answercnt,  String p_started,  String p_ended,  double p_time,
                                String p_answer, String p_corrected, int p_userretry,  String p_luserid, String p_userip) throws Exception { 

        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        try { 
            // insert TZ_EXAMRESULT table
            sql = " update TZ_EXAMRESULTTEMP ";
            sql += "    set exam   = ?, ";
            sql += "        examcnt   = ?, ";
            sql += "        exampoint   = ?, ";
            sql += "        score   = ?, ";
            sql += "        answercnt  = ?, ";
            sql += "        started     = ?, ";
            sql += "        ended    = ?, ";
            sql += "        time    = ?, ";
            sql += "        answer = ?, ";
            sql += "        corrected = ?, ";
            sql += "        userretry = ?, ";
            sql += "        luserid = ?, ";
            sql += "        ldate   = ?, ";
            sql += "        user_ip = ? ";
            sql += "  where subj    = ? ";
            sql += "    and year    = ? ";
            sql += "    and subjseq = ? ";
            sql += "    and lesson = ? ";
            sql += "    and examtype = ? ";
            sql += "    and papernum = ? ";
            sql += "    and userid  = ? ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, p_exam);
            pstmt.setInt( 2, p_examcnt);
            pstmt.setInt( 3, p_exampoint);
            pstmt.setInt( 4, p_score);
            pstmt.setInt( 5, p_answercnt);
            pstmt.setString   ( 6, p_started);
            pstmt.setString( 7, p_ended);
            pstmt.setDouble( 8, p_time);
            pstmt.setString   ( 9, p_answer);
            pstmt.setString   ( 10, p_corrected);
            pstmt.setInt(11, p_userretry);
            pstmt.setString(12, p_luserid);
            pstmt.setString(13, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString(14, p_userip);
            pstmt.setString(15, p_subj);
            pstmt.setString(16, p_year);
			pstmt.setString(17, p_subjseq);
			pstmt.setString(18, p_lesson);
            pstmt.setString(19, p_examtype);
            pstmt.setInt(20, p_papernum);
            pstmt.setString(21, p_userid);

            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }
    
	/**
	평가결과 임시테이블 평가지,평가자별 카운트
	@param        
	@return int
	*/
    public int chkResultExistTemp(DBConnectionManager connMgr,
                            String p_subj,     String p_year,      String p_subjseq, String p_lesson,
                            String p_examtype,    int   p_papernum, String p_userid
                            ) throws Exception { 
        ListSet             ls      = null;
        String sql  = "";
        int v_exist = 0;

        try { 
            sql = "select count(*)  cnt ";
            sql += "  from tz_examresulttemp  ";
            sql += " where subj    = " + SQLString.Format(p_subj);
            sql += "   and year    = " + SQLString.Format(p_year);
            sql += "   and subjseq = " + SQLString.Format(p_subjseq);
            sql += "   and lesson  = " + SQLString.Format(p_lesson);
            sql += "   and examtype   = " + SQLString.Format(p_examtype);
            sql += "   and papernum= " + SQLString.Format(p_papernum);
            sql += "   and userid  = " + SQLString.Format(p_userid);

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_exist = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_exist;
    }    
    


	/**
	평가자 결과 보기-제출후 바로 보는 결과
	@param 
	@return ArrayList
	*/
    public ArrayList SelectUserPaperResultTemp(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;

        String v_action = box.getStringDefault("p_action",  "change");

        String v_subj = box.getString("p_subj");
				String v_year     = box.getString("p_gyear");
        String v_subjseq   = box.getString("p_subjseq");
        String v_lesson    = box.getString("p_lesson");
        String v_examtype    = box.getString("p_examtype");
        int v_papernum    = box.getInt("p_papernum");
        String v_userid    = box.getString("p_userid");

        try { 
                connMgr = new DBConnectionManager();
                list = getPaperResultListTemp(connMgr, v_subj, v_year, v_subjseq, v_lesson, v_examtype, v_papernum, v_userid);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


	/**
	평가자 결과 보기-제출후 바로 보는 결과
	@param 
	@return ArrayList
	*/
    public ArrayList getPaperResultListTemp(DBConnectionManager connMgr,
                    String p_subj, String p_year, String p_subjseq, String p_lesson, String p_examtype, int p_papernum, String p_userid) throws Exception { 

        ArrayList QuestionExampleDataList  = null;
        ArrayList list = new ArrayList();
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;


        try { 
            sql = "select a.isopenanswer, a.isopenexp, b.exam, b.answer, b.corrected ";
            sql += "  from tz_exampaper  a, ";
            sql += "       tz_examresulttemp b ";
            sql += " where a.subj    = b.subj ";
            sql += "   and a.year    = b.year ";
            sql += "   and a.subjseq = b.subjseq ";
            sql += "   and a.lesson = b.lesson ";
            sql += "   and a.examtype = b.examtype ";
            sql += "   and a.papernum = b.papernum ";
            sql += " and b.subj = " + SQLString.Format(p_subj);
            sql += " and b.year = " + SQLString.Format(p_year);
            sql += " and b.subjseq = " + SQLString.Format(p_subjseq);
            sql += " and b.lesson = " + SQLString.Format(p_lesson);
            sql += " and b.examtype = " + SQLString.Format(p_examtype);
            sql += " and b.papernum = " + SQLString.Format(p_papernum);
            sql += " and b.userid = " + SQLString.Format(p_userid);
            sql += "   and rownum <= 1 ";
            sql += " order by a.subj, a.year, a.subjseq, b.userid ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
			}

            QuestionExampleDataList = getExampleData(connMgr, p_subj, dbox.getString("d_exam") );		
		
		} catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return QuestionExampleDataList;
    }


	/**
	평가자 결과 보기-제출후 바로 보는 결과
	@param 
	@return ArrayList
	*/
    public ArrayList SelectUserPaperResult2Temp(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;

        String v_action = box.getStringDefault("p_action",  "change");

        String v_subj = box.getString("p_subj");
				String v_year     = box.getString("p_gyear");
        String v_subjseq   = box.getString("p_subjseq");
        String v_lesson    = box.getString("p_lesson");
        String v_examtype    = box.getString("p_examtype");
        int v_papernum    = box.getInt("p_papernum");
        String v_userid    = box.getString("p_userid");

        try { 
                connMgr = new DBConnectionManager();
                list = getPaperResultList2Temp(connMgr, v_subj, v_year, v_subjseq, v_lesson, v_examtype, v_papernum, v_userid);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


	/**
	평가자 결과 보기-제출후 바로 보는 결과
	@param 
	@return ArrayList
	*/
    public ArrayList getPaperResultList2Temp(DBConnectionManager connMgr,
                    String p_subj, String p_year, String p_subjseq, String p_lesson, String p_examtype, int p_papernum, String p_userid) throws Exception { 

        ArrayList list = new ArrayList();
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;
				StringTokenizer st = null;
				Vector v_answer = null;
				StringTokenizer st2 = null;
				Vector v_corrected = null;


        try { 
            sql = "select a.isopenanswer, a.isopenexp, b.exam, b.answer, b.corrected, ";
            sql += " b.examcnt, b.answercnt, b.score, b.exampoint, b.started, b.ended,  ";
            sql += " a.year, a.lesson, a.subjseq, c.subjnm,  GET_CODENM(" + SQLString.Format(ExamBean.PTYPE) + ", nvl(a.examtype, '')) examtypenm  ";
            sql += "  from tz_exampaper  a, ";
            sql += "       tz_examresulttemp b, ";
            sql += "       tz_subj c ";
            sql += " where a.subj( +)        = c.subj  ";
            sql += " and a.subj    = b.subj ";
            sql += "   and a.year    = b.year ";
            sql += "   and a.subjseq = b.subjseq ";
            sql += "   and a.lesson = b.lesson ";
            sql += "   and a.examtype = b.examtype ";
            sql += "   and a.papernum = b.papernum ";
            sql += " and b.subj = " + SQLString.Format(p_subj);
            sql += " and b.year = " + SQLString.Format(p_year);
            sql += " and b.subjseq = " + SQLString.Format(p_subjseq);
            sql += " and b.lesson = " + SQLString.Format(p_lesson);
            sql += " and b.examtype = " + SQLString.Format(p_examtype);
            sql += " and b.papernum = " + SQLString.Format(p_papernum);
            sql += " and b.userid = " + SQLString.Format(p_userid);
            sql += "   and rownum <= 1 ";
            sql += " order by a.subj, a.year, a.subjseq, b.userid ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
								st = new StringTokenizer(dbox.getString("d_answer"),",");
								v_answer = new Vector();
                while ( st.hasMoreElements() ) { 
                     v_answer.add(st.nextToken() );
								}
								st2 = new StringTokenizer(dbox.getString("d_corrected"),",");
								v_corrected = new Vector();
                while ( st2.hasMoreElements() ) { 
                     v_corrected.add(st2.nextToken() );
								}
						}
       
            /*sql = "select count(b.userid) cnt ";
            sql += "     from  tz_examresult b ";
            sql += " where b.subj = " + SQLString.Format(p_subj);
            sql += " and b.year = " + SQLString.Format(p_year);
            sql += " and b.subjseq = " + SQLString.Format(p_subjseq);
            sql += " and b.lesson = " + SQLString.Format(p_lesson);
            sql += " and b.examtype = " + SQLString.Format(p_examtype);
            sql += " and b.papernum = " + SQLString.Format(p_papernum);
            sql += " and b.score > " + SQLString.Format(dbox.getString("d_score") );
			
                ls.close();
                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    dbox.put("d_overman", ls.getString("cnt") );
                }*/

				list.add(dbox);
				list.add(v_answer);
				list.add(v_corrected);

		
		} catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return list;
    }  
    
    /**
    평가 결과 문제 데이타 
    @param box          receive from the form object and session
    @return ArrayList   
    */
    public ArrayList getApplyExamConditionData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;
		String v_result = "";

        String v_userid   = box.getSession("userid");
        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq  = box.getString("p_subjseq");

        try { 
                connMgr = new DBConnectionManager();
				list = new ArrayList();
                    v_result = getApplyExamCondition(connMgr, v_subj, v_year, v_subjseq, v_userid, "M");
                    
				list.add(v_result);
                    v_result = getApplyExamCondition(connMgr, v_subj, v_year, v_subjseq, v_userid, "H");
                    
				list.add(v_result);
                    v_result = getApplyExamCondition(connMgr, v_subj, v_year, v_subjseq, v_userid, "E");
                    
				list.add(v_result);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

	/**
	 * 평가 응시 여부
	 * @param connMgr
	 * @param p_subj
	 * @param p_year
	 * @param p_subjseq
	 * @param p_userid
	 * @param p_examtype
	 * @return
	 * @throws Exception
	 */    
    public String getApplyExamCondition(DBConnectionManager connMgr,
                    String p_subj, String p_year, String p_subjseq, String p_userid, String p_examtype) throws Exception { 

        ArrayList list  = null;
        ListSet ls = null;
        String sql = "";
        DataBox dbox = null;

        String v_result = "";

        try { 

            sql = "\n select progress "
            	+ "\n from   tz_exampaper  "
            	+ "\n where  subj = " + SQLString.Format(p_subj)
            	+ "\n and    year = " + SQLString.Format(p_year)
            	+ "\n and    subjseq = " + SQLString.Format(p_subjseq)
            	+ "\n and    examtype = " + SQLString.Format(p_examtype);

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox = ls.getDataBox();
                v_result = ls.getString("progress");
			}
		
		} catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_result;
    }    
    
	/**
	 * 평가 문제번호
	 * @param connMgr
	 * @param p_subj
	 * @param p_year
	 * @param p_subjseq
	 * @param p_userid
	 * @param p_examtype
	 * @return
	 * @throws Exception
	 */    
    public static String getExamPapernum( String p_subj, String p_year, String p_subjseq, String p_examtype) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList list  = null;
        ListSet ls = null;
        String sql = "";
        DataBox dbox = null;

        String v_result = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = "\n select papernum "
            	+ "\n from   tz_exampaper  "
            	+ "\n where  subj = " + SQLString.Format(p_subj)
            	+ "\n and    year = " + SQLString.Format(p_year)
            	+ "\n and    subjseq = " + SQLString.Format(p_subjseq)
            	+ "\n and    examtype = " + SQLString.Format(p_examtype);

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox = ls.getDataBox();
                v_result = ls.getString("papernum");
			}
		
		} catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_result;
    }      
    
	/**
	 * 평가 응시 여부
	 * @param connMgr
	 * @param p_subj
	 * @param p_year
	 * @param p_subjseq
	 * @param p_userid
	 * @param p_examtype
	 * @return
	 * @throws Exception
	 */    
    public static String getExamScore( String p_subj, String p_year, String p_subjseq, String p_papernum, String p_userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList list  = null;
        ListSet ls = null;
        String sql = "";
        DataBox dbox = null;

        String v_result = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = "\n select score "
            	+ "\n from   tz_examresult  "
            	+ "\n where  subj = " + SQLString.Format(p_subj)
            	+ "\n and    year = " + SQLString.Format(p_year)
            	+ "\n and    subjseq = " + SQLString.Format(p_subjseq)
            	+ "\n and    papernum = " + SQLString.Format(p_papernum)
        		+ "\n and    userid = " + SQLString.Format(p_userid);            

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox = ls.getDataBox();
                v_result = ls.getString("score");
			}
		
		} catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_result;
    }  

    /**
     * 학습자의 질문에 대한 응답을 기록
     * @param box
     * @return
     * @throws Exception
     */
    public String setQuestionAnswer(RequestBox box) throws Exception {
        DBConnectionManager connMgr  = null;
        PreparedStatement   pstmt    = null;
        StringBuffer        sbSQL    = null;
        ListSet             ls       = null;
        
        int                 isOk     = 0;
        String result = "N";
        
        String v_answerTemp[] = null;
        String v_answerStr = "";
        String v_answerAlready = "";
        String v_answer   = box.getString("p_answer");
        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq  = box.getString("p_subjseq");
        String v_lesson   = box.getString("p_lesson");
        String v_examtype = box.getString("p_examtype");
        String v_papernum = box.getString("p_papernum");
        String v_userid   = box.getString("p_userid");
        int    v_examcnt  = box.getInt("p_examcnt");
        int    v_qNumber  = box.getInt("p_qNumber");
        
        
        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL = new StringBuffer();
            sbSQL.append(" select answer \n ");
            sbSQL.append(" from tz_examresult \n ");
            sbSQL.append(" where SUBJ = '"+v_subj+"' and \n ");
            sbSQL.append("       YEAR = '"+v_year+"' and \n ");
            sbSQL.append("       SUBJSEQ = '"+v_subjseq+"' and \n ");
            sbSQL.append("       LESSON = '"+v_lesson+"' and \n ");
            sbSQL.append("       EXAMTYPE = '"+v_examtype+"' and \n ");
            sbSQL.append("       PAPERNUM = '"+v_papernum+"' and \n ");
            sbSQL.append("       USERID = '"+v_userid+"' \n ");
            
            ls = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() ) { 
            	v_answerAlready = ls.getString("answer");
			}
            
            v_answerTemp = new String[v_examcnt];
            
            String[] v_answerDivision = v_answerAlready.split(",");
            
            for( int x = 0; x < v_answerDivision.length; x++ ){
            	//System.out.println("num= " + x + "   ------  "+ v_answerDivision[x]);
            	v_answerTemp[x] = v_answerDivision[x];
            } 
            
            v_answerTemp[v_qNumber-1] = v_answer;
            
            for( int x = 0; x < v_answerTemp.length; x++ ){
            	v_answerStr += (v_answerTemp[x] == null)?",":v_answerTemp[x] + ",";
            }
            
            sbSQL = new StringBuffer();
            sbSQL.append(" update tz_examresult \n ");
            sbSQL.append("    set answer = '"+v_answerStr+"' \n ");
            sbSQL.append(" where SUBJ = '"+v_subj+"' and \n ");
            sbSQL.append("       YEAR = '"+v_year+"' and \n ");
            sbSQL.append("       SUBJSEQ = '"+v_subjseq+"' and \n ");
            sbSQL.append("       LESSON = '"+v_lesson+"' and \n ");
            sbSQL.append("       EXAMTYPE = '"+v_examtype+"' and \n ");
            sbSQL.append("       PAPERNUM = '"+v_papernum+"' and \n ");
            sbSQL.append("       USERID = '"+v_userid+"' \n ");
  			
  			pstmt = connMgr.prepareStatement(sbSQL.toString());
  			isOk  = pstmt.executeUpdate();
            
            if ( isOk > 0 ) {    result = "Y"; connMgr.commit();      }
            else            {    connMgr.rollback();    }
            
        } catch ( Exception ex ) {
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally {
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e1 )  { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e10 ) { } }
        }
        
        return result;
    }        
    
    public ArrayList getUserStatusData(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;
		DataBox v_result = null;

        String v_userid     = box.getStringDefault("p_userid", box.getSession("userid"));
        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq     = box.getString("p_subjseq");

        try { 
                connMgr = new DBConnectionManager();
				list = new ArrayList();
                
                v_result = getUserStatusData(connMgr, v_subj, v_year, v_subjseq, v_userid, "M");
				list.add(v_result);
                
                v_result = getUserStatusData(connMgr, v_subj, v_year, v_subjseq, v_userid, "H");
                list.add(v_result);
                
                v_result = getUserStatusData(connMgr, v_subj, v_year, v_subjseq, v_userid, "E");
                list.add(v_result);
                
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
    
    
    public DataBox getUserStatusData(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_userid, String p_examtype) throws Exception { 
    	
    	ArrayList QuestionExampleDataList  = null;
    	int v_result = 0;
    	ListSet             ls      = null;
    	String sql  = "";
    	DataBox             dbox    = null;
    	StringBuffer strSQL = null;
    	StringBuffer strSQL2 = null;
    	
    	try { 
    		
    		/*
    		sql = "select count(score) examcount ";
    		sql += "  from tz_examresult  ";
    		sql += " where subj = " + SQLString.Format(p_subj);
    		sql += " and year = " + SQLString.Format(p_year);
    		sql += " and subjseq = " + SQLString.Format(p_subjseq);
    		sql += " and examtype = " + SQLString.Format(p_examtype);
    		sql += " and userid = " + SQLString.Format(p_userid);
    		
    		
    		strSQL2 = new StringBuffer();
    		strSQL2.append(" \n select b.subj,   a.year,    a.subjseq,   a.lesson, ");
    		strSQL2.append(" \n       a.examtype,  a.papernum,   a.lessonstart, a.lessonend,  a.examtime, a.exampoint, a.examcnt, a.totalscore,  ");
    		strSQL2.append(" \n       a.cntlevel1, a.cntlevel2, a.cntlevel3, a.isopenanswer, ");
    		strSQL2.append(" \n       a.isopenexp,  a.retrycnt,  b.subjnm,  GET_CODENM(" + SQLString.Format(ExamBean.PTYPE) + ", nvl(a.examtype, '')) examtypenm ");
    		strSQL2.append(" \n  from tz_exampaper a, ");
    		strSQL2.append(" \n       tz_subj       b  ");
    		strSQL2.append(" \n where a.subj( +)        = b.subj  ");
    		strSQL2.append(" \n   and a.subj   = " + SQLString.Format(p_subj));
    		strSQL2.append(" \n   and a.year   = " + SQLString.Format(p_year));
    		strSQL2.append(" \n   and a.subjseq   = " + SQLString.Format(p_subjseq) );
    		strSQL2.append(" \n order by a.subj, a.year, a.subjseq, a.lesson, a.examtype  ");
    		 */
    		
    		strSQL = new StringBuffer();
//    		strSQL.append(" \n select ") ;
//    		strSQL.append(" \n                   DECODE(b.STARTED, NULL, 'Y', '', 'Y' ") ;
//    		strSQL.append(" \n                        , CASE WHEN b.ENDED IS NULL OR b.ENDED = '' ") ;
//    		strSQL.append(" \n                                    THEN CASE WHEN TRUNC((SYSDATE - TO_DATE(b.STARTED,'YYYYMMDDHH24MISS')) * 24 * 60) < A.EXAMTIME THEN 'Y' ") ;
//    		strSQL.append(" \n                                              ELSE CASE WHEN nvl(b.USERRETRY,A.RETRYCNT) > 0 THEN 'Y' ") ;
//    		strSQL.append(" \n                                                        ELSE 'N' ") ;
//    		strSQL.append(" \n                                                   END ") ;
//    		strSQL.append(" \n                                         END ") ;
//    		strSQL.append(" \n                               ELSE CASE WHEN nvl(b.USERRETRY,A.RETRYCNT) > 0 THEN 'Y' ") ;
//    		strSQL.append(" \n                                       ELSE 'N' ") ;
//    		strSQL.append(" \n                                    END ") ;
//    		strSQL.append(" \n                          END) TIME_YN ") ;
//    		strSQL.append(" \n                 , CASE WHEN b.ENDED IS NOT NULL OR b.ENDED != '' THEN A.EXAMTIME ") ;
//    		strSQL.append(" \n                        ELSE DECODE(b.STARTED, NULL, A.EXAMTIME, '', A.EXAMTIME ") ;
//    		strSQL.append(" \n                                  , A.EXAMTIME - TRUNC((SYSDATE - TO_DATE(b.STARTED,'YYYYMMDDHH24MISS')) * 24 * 60)) ") ;
//    		strSQL.append(" \n                   END AS EXTRATIME ") ;
//    		strSQL.append(" \n      , a.progress ") ;
//    		strSQL.append(" \n      , a.examtype ") ;
//    		strSQL.append(" \n      , tstep ") ;
//    		strSQL.append(" \n      , (select count(score) from tz_examresult where subj= " + SQLString.Format(p_subj) + "and year = " + SQLString.Format(p_year) + "and subjseq = " + SQLString.Format(p_subjseq) + " and examtype = " + SQLString.Format(p_examtype) + " and userid = " + SQLString.Format(p_userid) + " and papernum = b.papernum ) examcount ") ;
//    		//strSQL.append(" \n      , (select userretry from tz_examresult where subj= " + SQLString.Format(p_subj) + "and year = " + SQLString.Format(p_year) + "and subjseq = " + SQLString.Format(p_subjseq) + " and examtype = " + SQLString.Format(p_examtype) + " and userid = " + SQLString.Format(p_userid) + " and papernum = b.papernum ) userretry ") ;
//    		//strSQL.append(" \n      , (select retrycnt from tz_exampaper where subj= " + SQLString.Format(p_subj) + "and year = " + SQLString.Format(p_year) + "and subjseq = " + SQLString.Format(p_subjseq) + ") as retrycnt ") ;
//    		strSQL.append(" \n      , (select max(ended) from tz_examresult where subj= " + SQLString.Format(p_subj) + "and year = " + SQLString.Format(p_year) + "and subjseq = " + SQLString.Format(p_subjseq) + " and examtype = " + SQLString.Format(p_examtype) + " and userid = " + SQLString.Format(p_userid) + " and papernum = b.papernum ) ended ") ;
//    		strSQL.append(" \n      , b.started ") ;
//    		//strSQL.append(" \n      , b.ended") ;
//    		strSQL.append(" \n      , b.USERRETRY") ;
//    		strSQL.append(" \n      , A.RETRYCNT") ;
//    		strSQL.append(" \n      , A.STARTDT") ;
//    		strSQL.append(" \n      , A.ENDDT") ;
//    		strSQL.append(" \n      , A.PAPERNUM") ;
//    		strSQL.append(" \n      , B.SCORE") ;
//    		strSQL.append(" \n      , A.LESSON") ;
//    		strSQL.append(" \n      , A.LESSONSTART") ;
//    		strSQL.append(" \n      , (CASE WHEN TO_CHAR(SYSDATE,'YYYYMMDD') BETWEEN a.STARTDT AND a.ENDDT THEN 'Y' WHEN TO_CHAR(SYSDATE,'YYYYMMDD') < a.STARTDT THEN 'B' ELSE 'A' END) AS TERMYN") ;
//    		strSQL.append(" \n from tz_exampaper a, tz_examresult b, tz_student c ") ;
//    		strSQL.append(" \nwhere a.subj = b.subj(+) ") ;
//    		strSQL.append(" \n  and a.year = b.year(+) ") ;
//    		strSQL.append(" \n  and a.subjseq=b.subjseq(+) ") ;
//    		strSQL.append(" \n  and a.lesson=b.lesson(+) ") ;
//    		strSQL.append(" \n  and a.examtype=b.examtype(+) ") ;
//    		strSQL.append(" \n  and a.papernum=b.papernum(+) ") ;
//    		strSQL.append(" \n  and a.subj=c.subj ") ;
//    		strSQL.append(" \n  and a.year=c.year ") ;
//    		strSQL.append(" \n  and a.subjseq=c.subjseq ") ;
//    		strSQL.append(" \n  and b.userid = c.userid");
//    		strSQL.append(" \n  and c.userid = " + SQLString.Format(p_userid)) ;
//    		strSQL.append(" \n  and a.examtype = '"+p_examtype+"' ") ;
//    		strSQL.append(" \n  and a.subj = '"+p_subj+"' ") ;
//    		strSQL.append(" \n  and a.year = '"+p_year+"' ") ;
//    		strSQL.append(" \n  and a.subjseq = '"+p_subjseq+"' ") ;
    		
    		strSQL.append(" \n select "); 
    		strSQL.append(" \n             DECODE(b.STARTED, NULL, 'Y', '', 'Y' "); 
    		strSQL.append(" \n                  , CASE WHEN b.ENDED IS NULL OR b.ENDED = '' "); 
    		strSQL.append(" \n                              THEN CASE WHEN TRUNC((SYSDATE - TO_DATE(b.STARTED,'YYYYMMDDHH24MISS')) * 24 * 60) < A.EXAMTIME THEN 'Y' "); 
    		strSQL.append(" \n                                        ELSE CASE WHEN nvl(b.USERRETRY,A.RETRYCNT) > 0 THEN 'Y'  ");
    		strSQL.append(" \n                                                  ELSE 'N'  ");
    		strSQL.append(" \n                                             END  ");
    		strSQL.append(" \n                                   END  ");
    		strSQL.append(" \n                         ELSE CASE WHEN nvl(b.USERRETRY,A.RETRYCNT) > 0 THEN 'Y' "); 
    		strSQL.append(" \n                                 ELSE 'N'  ");
    		strSQL.append(" \n                              END  ");
    		strSQL.append(" \n                    END) TIME_YN  ");
    		strSQL.append(" \n           , CASE WHEN b.ENDED IS NOT NULL OR b.ENDED != '' THEN A.EXAMTIME "); 
    		strSQL.append(" \n                  ELSE DECODE(b.STARTED, NULL, A.EXAMTIME, '', A.EXAMTIME  ");
    		strSQL.append(" \n                            , A.EXAMTIME - TRUNC((SYSDATE - TO_DATE(b.STARTED,'YYYYMMDDHH24MISS')) * 24 * 60)) "); 
    		strSQL.append(" \n             END AS EXTRATIME  ");
    		strSQL.append(" \n , a.progress  ");
    		strSQL.append(" \n , a.examtype  ");
    		strSQL.append(" \n , (SELECT MAX(tstep) FROM TZ_STUDENT WHERE subj = " + SQLString.Format(p_subj) + " and year = " + SQLString.Format(p_year) + " and subjseq = " + SQLString.Format(p_subjseq) + " and userid = '"+p_userid+"') AS tstep ");
//    		strSQL.append(" \n --      , (select count(score) from tz_examresult where subj= 'EXT003001' and year = '2010' and subjseq = '0001' and examtype = 'E' and userid = 'cartt67' and papernum = b.papernum ) examcount "); 
//    		strSQL.append(" \n --      , (select max(ended) from tz_examresult where subj= 'EXT003001' and year = '2010' and subjseq = '0001' and examtype = 'E' and userid = 'cartt67' and papernum = b.papernum ) ended  ");
//    		strSQL.append(" \n -- , (select MAX(USERRETRY) from tz_examresult where subj= 'EXT003001' and year = '2010' and subjseq = '0001' and examtype = 'E' and userid = 'zzang8993' and papernum = b.papernum ) USERRETRY "); 
    		strSQL.append(" \n , b.started  ");
    		strSQL.append(" \n , b.ended ");
    		strSQL.append(" \n , B.SCORE ");
    		strSQL.append(" \n , b.USERRETRY ");
    		strSQL.append(" \n , A.RETRYCNT ");
    		strSQL.append(" \n , A.STARTDT ");
    		strSQL.append(" \n , A.ENDDT ");
    		strSQL.append(" \n , A.PAPERNUM ");
    		strSQL.append(" \n , A.LESSON ");
    		strSQL.append(" \n , A.LESSONSTART ");
    		strSQL.append(" \n , (CASE WHEN TO_CHAR(SYSDATE,'YYYYMMDD') BETWEEN a.STARTDT AND a.ENDDT THEN 'Y' WHEN TO_CHAR(SYSDATE,'YYYYMMDD') < a.STARTDT THEN 'B' ELSE 'A' END) AS TERMYN ");
    		strSQL.append(" \n from tz_exampaper a ");
    		strSQL.append(" \n LEFT JOIN tz_examresult b ON a.subj = b.subj and a.year = b.year and a.subjseq=b.subjseq and a.lesson=b.lesson and a.examtype=b.examtype and a.papernum=b.papernum AND b.userid = " + SQLString.Format(p_userid) );
    		strSQL.append(" \n WHERE ");
    		strSQL.append(" \n  	a.examtype = '"+p_examtype+"' ") ;
    		strSQL.append(" \n  and a.subj = '"+p_subj+"' ") ;
    		strSQL.append(" \n  and a.year = '"+p_year+"' ") ;
    		strSQL.append(" \n  and a.subjseq = '"+p_subjseq+"' ") ;
   		
    		System.out.println("@@@@ sql : "+strSQL.toString());
    		ls = connMgr.executeQuery(strSQL.toString());
    		
    		if ( ls.next() ) { 
    			dbox = ls.getDataBox();
    		}
    		
    	} catch ( Exception ex ) { 
    		ErrorManager.getErrorStackTrace(ex);
    		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	} finally { 
    		if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
    	}
    	
    	return dbox;
    }
    
}