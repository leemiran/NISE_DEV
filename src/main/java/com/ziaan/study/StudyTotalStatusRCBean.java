// **********************************************************
//  1. 제      목: 독서교육 종합학습현황
//  2. 프로그램명: StudyTotalStatusRCBean.java
//  3. 개      요: 독서교육 종합학습현황
//  4. 환      경: JDK 1.5
//  5. 버      젼: 1.0
//  6. 작      성: 2009.2.4
//  7. 수      정:
// **********************************************************
package com.ziaan.study;
import java.util.ArrayList;

import com.ziaan.common.GetCodenm;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
public class StudyTotalStatusRCBean { 

    public StudyTotalStatusRCBean() { }

    /**
     * 독서교육 종합학습현황
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectStudyTotalStatusList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls      = null;
        ListSet ls2     = null;

        ArrayList list  = null;
        ArrayList list2 = null;
        String sql      = "";
        DataBox dbox    = null;
        DataBox dbox2   = null;

        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수

        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");    // 과목분류
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류

        String  ss_subjcourse = box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq    = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_company    = box.getStringDefault("s_company","ALL");   // 회사
        String  ss_selGubun   = box.getString("s_selGubun");              // 진도율 1,취득점수 2
        String  ss_selStart   = box.getStringDefault("s_selStart","0");   // ~부터
        String  ss_selEnd     = box.getStringDefault("s_selEnd","10000"); // ~까지
        String  v_isgrad      = box.getStringDefault("p_isgrad"  , "M");   // 수료 여부(ALL:전체, M:진행중, Y:수료, N:미수료)
        String  v_isexam      = box.getStringDefault("p_isexam"  , "ALL");   // 평가 여부
        String  v_isreport    = box.getStringDefault("p_isreport", "ALL");   // 레포트 여부
        String  ss_class      = box.getStringDefault("s_class","ALL");// 클래스
        String  v_searchtxt = box.getStringDefault("p_searchtext1", "ZZZ");    // id or name 검색

        String  ss_action   	= box.getString("s_action");
        String  v_orderColumn	= box.getString("p_orderColumn");           // 정렬할 컬럼명
        String v_orderType    = box.getString("p_orderType");           // 정렬할 순서

        String  s_gadmin    	= box.getSession("gadmin");

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list = new ArrayList();
                list2= new ArrayList();

                sql = "\n select t1.comp "
                    + "\n      , get_compnm(t1.comp) as compnm "
                    + "\n      , t1.subj "
                    + "\n      , t2.subjnm "
                    + "\n      , t1.year "
                    + "\n      , t1.subjseq "
                    + "\n      , (select position_nm from tz_member where userid = t1.userid) as position_nm "
                    + "\n      , t1.userid "
                    + "\n      , get_name(t1.userid) as name "
                    + "\n      , t1.score "
                    + "\n      , ( "
                    + "\n         select count(*) "
                    + "\n         from   tz_subjseq "
                    + "\n         where  subj = t1.subj "
                    + "\n         and    year = t1.year "
                    + "\n         and    subjseq = t1.subjseq "
                    + "\n         and    (nvl(sulpapernum,0) <> 0 or nvl(presulpapernum,0) <> 0 or nvl(aftersulpapernum,0) <> 0) "
                    + "\n        ) as totsulcnt "
                    + "\n      , ( "
                    + "\n         select count(*) "
                    + "\n         from   tz_suleach "
                    + "\n         where  subj = t1.subj "
                    + "\n         and    year = t1.year "
                    + "\n         and    subjseq = t1.subjseq "
                    + "\n         and    userid = t1.userid "
                    + "\n        ) as sulcnt "
                    + "\n      , nvl(t3.isgraduated,'M') as isgraduated "
                    + "\n from   tz_student t1 "
                    + "\n      , vz_scsubjseq t2 "
                    + "\n      , tz_stold t3 "
                    + "\n where  t1.subj = t2.subj "
                    + "\n and    t1.year = t2.year "
                    + "\n and    t1.subjseq = t2.subjseq "
                    + "\n and    t1.subj = t3.subj(+) "
                    + "\n and    t1.year = t3.year(+) "
                    + "\n and    t1.subjseq = t3.subjseq(+) "
                    + "\n and    t1.userid = t3.userid(+) "
                    + "\n and    t2.isonoff = 'RC' ";

                
                if ( !ss_grcode.equals("ALL"))     sql += "\n and    t2.grcode        	= " +SQLString.Format(ss_grcode);
                if ( !ss_gyear.equals("ALL"))      sql += "\n and    t2.gyear         	= " +SQLString.Format(ss_gyear);
                if ( !ss_grseq.equals("ALL"))      sql += "\n and    t2.grseq         	= " +SQLString.Format(ss_grseq);
                if ( !ss_uclass.equals("ALL"))     sql += "\n and    t2.scupperclass  	= " +SQLString.Format(ss_uclass);
                if ( !ss_mclass.equals("ALL"))     sql += "\n and    t2.scmiddleclass 	= " +SQLString.Format(ss_mclass);
                if ( !ss_lclass.equals("ALL"))     sql += "\n and    t2.sclowerclass  	= " +SQLString.Format(ss_lclass);
                if ( !ss_subjcourse.equals("ALL")) sql += "\n and    t2.subj        	= " +SQLString.Format(ss_subjcourse);
                if ( !ss_subjseq.equals("ALL"))    sql += "\n and    t2.subjseq     	= " +SQLString.Format(ss_subjseq);
                if ( !ss_company.equals("ALL"))    sql += "\n and    t1.comp			= " +SQLString.Format(ss_company);
                if ( !ss_class.equals("ALL"))      sql += "\n and    t1.class			= " +SQLString.Format(ss_class);
                if ( !v_searchtxt.equals("ZZZ") ) { 
	            	sql += "   and (t1.userid like  '%" +v_searchtxt + "%' or get_name(t1.userid) like '%" +v_searchtxt+"%') \n";
	            }
                if ( ss_selGubun.equals("2") ) {  // 취득점수
                    sql += "\n and    t1.score >= " + ss_selStart + " and t1.score <= " + ss_selEnd;
                } else if ( ss_selGubun.equals("3") ) {  // 리포트
                    sql += "\n and    t1.avreport >= " + ss_selStart + " and t1.avreport <= " + ss_selEnd;
                }
                
                if ( v_orderColumn.equals("subjnm"))     	v_orderColumn =" t2.subjnm ";
                if ( v_orderColumn.equals("userid"))   		v_orderColumn =" t1.userid ";
                if ( v_orderColumn.equals("name"))     		v_orderColumn =" name ";
                if ( v_orderColumn.equals("compnm"))   		v_orderColumn =" compnm ";
                if ( v_orderColumn.equals("subjseq"))  		v_orderColumn =" t1.subjseq ";
                if ( v_orderColumn.equals("sulcnt")) 		v_orderColumn =" sulcnt ";
                
                if ( !v_isgrad.equals("ALL") ) {      
                    sql += "\n and    nvl(t3.isgraduated,'M') = "  + SQLString.Format(v_isgrad);
                }
                
                if ( v_orderColumn.equals("") ) { 
                	sql += "\n order  by t1.subj, t1.year, t1.subjseq, name , t1.userid ";
                } else { 
                	sql += "\n order  by " + v_orderColumn + v_orderType;
                }
                
                ls = connMgr.executeQuery(sql);

                String[] arr_notgraducd;
                String v_notgradunm = "";
                while ( ls.next() ) { 
                    dbox = ls.getDataBox();

					// 미수료 사유
					arr_notgraducd = dbox.getString("d_notgraducd").split(",");
					for (int i=0; i<arr_notgraducd.length; i++) {
						if (i == 0) {
							v_notgradunm = GetCodenm.get_codenm("0028", arr_notgraducd[i]);
						} else {
							v_notgradunm += " , " + GetCodenm.get_codenm("0028", arr_notgraducd[i]);
						}
					}
					dbox.put("d_notgradunm", v_notgradunm);
                    dbox.put("d_score"	, new Double( ls.getDouble("score"))); 

                    // 독서교육 개월차 평가결과
                    sql = "\n select t1.subj "
                    	+ "\n      , t1.year "
                    	+ "\n      , t1.subjseq "
                    	+ "\n      , t1.userid "
                    	+ "\n      , t1.month "
                        + "\n      , t1.bookcode "
                        + "\n      , decode(nvl(t3.subj,'N'),'N','N','Y') as exam_yn "
                        + "\n      , nvl(t3.final_yn,'N') as final_yn "
                        + "\n      , nvl(t3.marking_yn,'N') as marking_yn "
                        + "\n      , nvl(t3.totalscore,0) as totalscore "
                        + "\n from   tz_proposebook t1 "
                        + "\n      , tz_bookexam_paper t2 "
                        + "\n      , tz_bookexam_result t3 "
                        + "\n where  t1.subj = t2.subj(+) "
                        + "\n and    t1.year = t2.year(+) "
                        + "\n and    t1.subjseq = t2.subjseq(+) "
                        + "\n and    t1.month = t2.month(+) "
                        + "\n and    t1.bookcode = t2.bookcode(+) "
                        + "\n and    t1.subj = t3.subj(+) "
                        + "\n and    t1.year = t3.year(+) "
                        + "\n and    t1.subjseq = t3.subjseq(+) "
                        + "\n and    t1.month = t3.month(+) "
                        + "\n and    t1.bookcode = t3.bookcode(+) "
                        + "\n and    t1.userid = t3.userid(+) "
                        + "\n and    t1.subj = " + StringManager.makeSQL(ls.getString("subj")) 
                        + "\n and    t1.year = " + StringManager.makeSQL(ls.getString("year"))
                        + "\n and    t1.subjseq = " + StringManager.makeSQL(ls.getString("subjseq"))
                        + "\n and    t1.userid = " + StringManager.makeSQL(ls.getString("userid"));
                    ls2 = connMgr.executeQuery(sql);
                    while ( ls2.next() ) { 
                    	dbox2 = ls2.getDataBox();
                    	dbox2.put("d_totalscore"	, new Double( ls2.getDouble("totalscore"))); 
                    	list2.add(dbox2);
                    }
                    if ( ls2 != null )  { try { ls2.close(); } catch ( Exception e ) { } }
                    
                    dbox.put("bookexam_result", list2);

                    list.add(dbox);
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
        } finally { 
            if ( ls != null )  { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls2 != null )  { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
     * 몇개월 과정인지 알아내기
     * @param box
     * @return
     * @throws Exception
     */
	public int getCntBookMonth(RequestBox box) throws Exception {
        DBConnectionManager	connMgr	= null;
        String sql = "";
        ListSet ls = null;
        int cnt = 0;
        String v_subj = box.getString("p_subj");
        
		try {
			connMgr = new DBConnectionManager();
			
            // 독서교육 개월차 평가결과
            sql = "\n select count(distinct month) cnt "
                + "\n from   tz_subjbook "
                + "\n where  subj = " + StringManager.makeSQL(v_subj);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) { 
            	cnt = ls.getInt("cnt");
            }
            			
		} catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
        } finally { 
            if ( ls != null )  { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
		return cnt;
	}
}