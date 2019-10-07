// **********************************************************
//  1. 제      목: 접속통계
//  2. 프로그램명: SubjCountBean.java
//  3. 개      요: 접속통계
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 7. 7
//  7. 수      정:
// **********************************************************

package com.ziaan.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;


public class SubjCountBean { 
    private static final String CONFIG_NAME = "cur_nrm_grcode";
    
    public SubjCountBean() { }

    /**
     * 로그 작성
     * @param box       receive from the form object and session
     * @return is_Ok    1 : log ok      2 : log fail
     * @throws Exception
     */
    public boolean writeLog(RequestBox box, String v_subj, String v_gyear, String v_subjseq) throws Exception { 
        
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        PreparedStatement pstmt2 = null;

        String sql  = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        
        int cnt  = 0;
        int is_Ok = 0;
        // String v_gubun = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_gubun = "";// v_grcode; // 교육주관코드
        String v_year  = FormatDate.getDate("yyyy");
        String v_month = FormatDate.getDate("MM");
        String v_day   = FormatDate.getDate("dd");
        String v_time  = FormatDate.getDate("HH");
//        String v_week  = FormatDate.getDayOfWeek();
        String v_week  = "";
        Calendar calendar=Calendar.getInstance();
        calendar.set(Integer.parseInt(v_year),Integer.parseInt(v_month)-1,Integer.parseInt(v_day));
        v_week=String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)); // 1(일),2(월),3(화),4(수),5(목),6(금),7(토)

        // gubun은 교육주관코드로 사용됨
//      String v_grcode = "";
//      String v_upperclass = "";
//      String v_middleclass = "";
//      String v_comp = box.getSession("comp");
        
        try { 
            
            connMgr = new DBConnectionManager();            

            sql   = " select grcode from VZ_SCSUBJSEQ ";
            sql += "  where subj       = " + StringManager.makeSQL(v_subj);   // 과목코드
            sql += "   and subjseq     = " + StringManager.makeSQL(v_subjseq);// 과목기수
            sql += "   and year  = " + StringManager.makeSQL(v_gyear);   

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                v_gubun = ls.getString("grcode");
            }
            ls.close();

            if ( v_gubun.equals(""))     v_gubun = box.getSession("tem_grcode");

            sql1  = " select count(cnt) cnt ";           
            sql1 += " from TZ_SUBJCOUNT                     ";
            sql1 += " where gubun      = " + StringManager.makeSQL(v_gubun); // 교육주관
            sql1 += "   and subj       = " + StringManager.makeSQL(v_subj);   // 과목코드
            sql1 += "   and subjseq    = " + StringManager.makeSQL(v_subjseq);// 과목기수
            sql1 += "   and date_year  = " + StringManager.makeSQL(v_gyear);   
            sql1 += "   and date_month = " + StringManager.makeSQL(v_month);
            sql1 += "   and date_day   = " + StringManager.makeSQL(v_day);
            sql1 += "   and date_time  = " + StringManager.makeSQL(v_time);
            sql1 += "   and date_week  = " + StringManager.makeSQL(v_week);
            
            ls = connMgr.executeQuery(sql1);

            if ( ls.next() ) { 
                cnt = ls.getInt("cnt");
            }
            ls.close();
            
            if ( cnt > 0 ) {                         // update
                sql2  = " update TZ_SUBJCOUNT set cnt = cnt +1              ";
                sql2 += " where gubun    = ?  and subj = ? and subjseq = ? ";
                sql2 += "   and date_year = ?  and date_month = ?          ";
                sql2 += "   and date_day = ?  and date_time = ?  and date_week  = ?  ";
                
                pstmt = connMgr.prepareStatement(sql2);

                pstmt.setString(1,  v_gubun);
                pstmt.setString(2,  v_subj);
                pstmt.setString(3,  v_subjseq);
                
                pstmt.setString(4,  v_gyear);
                pstmt.setString(5,  v_month);
                pstmt.setString(6,  v_day);
                pstmt.setString(7,  v_time);
                pstmt.setString(8,  v_week);

                is_Ok = pstmt.executeUpdate();
            }else {                                // insert
                sql3  = " insert into TZ_SUBJCOUNT(gubun, date_year, date_month, date_day, date_time, date_week, cnt, subj,  subjseq) ";
                sql3 += " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                pstmt2 = connMgr.prepareStatement(sql3);

                pstmt2.setString(1,  v_gubun);
                pstmt2.setString(2,  v_gyear);
                pstmt2.setString(3,  v_month);
                pstmt2.setString(4,  v_day);
                pstmt2.setString(5,  v_time);
                pstmt2.setString(6,  v_week);
                pstmt2.setInt(7,  1);
                pstmt2.setString(8,  v_subj);
                pstmt2.setString(9,  v_subjseq);
                                
                is_Ok = pstmt2.executeUpdate();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok > 0;
    }


    /**
    * 년통계 카운트
    * @param  box          receive from the form object and session
    * @return result       년통계카운트
    * @throws Exception
    */
    public int SelectYearCnt(RequestBox box) throws Exception   { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        ListSet             ls      = null;
        CountData data = null;
        String sql    = "";
        int    result = 0;

        // String v_gubun = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_gubun = box.getString("s_grcode");
        String v_subj = box.getString("s_subjcourse");
        String v_subjseq = box.getString("s_subjseq");
        String v_year  = box.getStringDefault("p_date_year", FormatDate.getDate("yyyy") );
        
        if ( v_gubun.equals(""))  v_gubun = "N000001";
        if ( v_subj.equals(""))   v_subj = "ALL";
        if ( v_subjseq.equals(""))    v_subjseq = "ALL";

        try { 
            connMgr = new DBConnectionManager();

            sql = "select date_year, sum(cnt) cnt             ";
            sql += " from TZ_SUBJCOUNT                        ";
            sql += " where date_year  = " + StringManager.makeSQL(v_year);
            if ( !v_gubun.equals("----")) sql += "  and gubun      = " + StringManager.makeSQL(v_gubun);
            if ( !v_subj.equals("ALL")) sql += "  and subj      = " + StringManager.makeSQL(v_subj);
            if ( !v_subjseq.equals("ALL")) sql += "  and subjseq      = " + StringManager.makeSQL(v_subjseq);
            sql += " group by date_year                ";

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new CountData();
                result = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }


    /**
    * 월통계 리스트
    * @param  box          receive from the form object and session
    * @return ArrayList    월통계 리스트
    * @throws Exception
    */
    public ArrayList SelectMonth(RequestBox box) throws Exception   { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        CountData data = null;

        // String v_gubun = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_gubun = box.getString("s_grcode");
        String v_subj = box.getString("s_subjcourse");
        String v_subjseq = box.getString("s_subjseq");
        String v_year  = box.getStringDefault("p_date_year",  FormatDate.getDate("yyyy") );

        if ( v_gubun.equals(""))  v_gubun = "N000001";
        if ( v_subj.equals(""))   v_subj = "ALL";
        if ( v_subjseq.equals(""))    v_subjseq = "ALL";

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql = "select date_year, date_month, sum(cnt) cnt     ";
            sql += " from TZ_SUBJCOUNT                                   ";
            sql += " where date_year  = " + StringManager.makeSQL(v_year);
            if ( !v_gubun.equals("----")) sql += "  and gubun      = " + StringManager.makeSQL(v_gubun);
            if ( !v_subj.equals("ALL")) sql += "  and subj      = " + StringManager.makeSQL(v_subj);
            if ( !v_subjseq.equals("ALL")) sql += "  and subjseq      = " + StringManager.makeSQL(v_subjseq);
            sql += " group by date_year, date_month                      ";
            sql += " order by date_month asc                             ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new CountData();

//              data.setGubun( ls.getString("gubun") );
                data.setDate_year( ls.getString("date_year") );
                data.setDate_month( ls.getString("date_month") );
                data.setCnt( ls.getInt("cnt") );

                list.add(data);
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
    * 월일통계 리스트
    * @param  box          receive from the form object and session
    * @return ArrayList    월일통계 리스트
    * @throws Exception
    */
    public ArrayList SelectMonthDay(RequestBox box) throws Exception   { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        CountData data = null;

        // String v_gubun = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_gubun = box.getString("s_grcode");
        String v_subj = box.getString("s_subjcourse");
        String v_subjseq = box.getString("s_subjseq");
        String v_year  = box.getStringDefault("p_date_year",  FormatDate.getDate("yyyy") );
        String v_month = box.getStringDefault("p_date_month", FormatDate.getDate("MM") );
        String ss_action = box.getString("s_action");

        if ( v_gubun.equals(""))  v_gubun = "N000001";
        if ( v_subj.equals(""))   v_subj = "ALL";
        if ( v_subjseq.equals(""))    v_subjseq = "ALL";

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

// if ( ss_action.equals("go") ) { 
            sql = "select date_year, date_month, date_day, sum(cnt) cnt         ";
            sql += " from TZ_SUBJCOUNT                                          ";
            sql += " where date_year   = " + StringManager.makeSQL(v_year);
            sql += "   and date_month  = " + StringManager.makeSQL(v_month);
            if ( !v_gubun.equals("----")) sql += "  and gubun      = " + StringManager.makeSQL(v_gubun);
            if ( !v_subj.equals("ALL")) sql += "  and subj      = " + StringManager.makeSQL(v_subj);
            if ( !v_subjseq.equals("ALL")) sql += "  and subjseq      = " + StringManager.makeSQL(v_subjseq);
            sql += " group by date_year, date_month, date_day                   ";
            sql += " order by date_day asc                                      ";
            ls = connMgr.executeQuery(sql);
System.out.println("월일 >>  >>  >>  >>  >>  >>  >>  >>  > " +sql);
            while ( ls.next() ) { 
                data = new CountData();

//                data.setGubun( ls.getString("gubun") );
                data.setDate_year( ls.getString("date_year") );
                data.setDate_month( ls.getString("date_month") );
                data.setDate_day( ls.getString("date_day") );
                data.setCnt( ls.getInt("cnt") );

                list.add(data);
            }
// }
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
    * 일시통계 리스트
    * @param  box          receive from the form object and session
    * @return ArrayList    일시통계 리스트
    * @throws Exception
    */
    public ArrayList SelectDayTime(RequestBox box) throws Exception   { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        CountData data = null;

        // String v_gubun = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_gubun = box.getString("s_grcode");
        String v_subj = box.getString("s_subjcourse");
        String v_subjseq = box.getString("s_subjseq");
        String v_year  = box.getStringDefault("p_date_year",  FormatDate.getDate("yyyy") );
        String v_month = box.getStringDefault("p_date_month", FormatDate.getDate("MM") );
        String v_day   = box.getStringDefault("p_date_day",   FormatDate.getDate("dd") );

        if ( v_gubun.equals(""))  v_gubun = "N000001";
        if ( v_subj.equals(""))   v_subj = "ALL";
        if ( v_subjseq.equals(""))    v_subjseq = "ALL";

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql = "select date_year, date_month, date_day, date_time, cnt ";
            sql += " from TZ_SUBJCOUNT                                    ";
            sql += " where date_year   = " + StringManager.makeSQL(v_year);
            sql += "   and date_month  = " + StringManager.makeSQL(v_month);
            sql += "   and date_day    = " + StringManager.makeSQL(v_day);
            if ( !v_gubun.equals("----")) sql += "  and gubun      = " + StringManager.makeSQL(v_gubun);
            if ( !v_subj.equals("ALL")) sql += "  and subj      = " + StringManager.makeSQL(v_subj);
            if ( !v_subjseq.equals("ALL")) sql += "  and subjseq      = " + StringManager.makeSQL(v_subjseq);
            sql += " order by date_time asc                               ";
System.out.println("일시 >>  >>  >>  >>  >>  >>  >>  >>  > " +sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new CountData();

//                data.setGubun( ls.getString("gubun") );
                data.setDate_year( ls.getString("date_year") );
                data.setDate_month( ls.getString("date_month") );
                data.setDate_day( ls.getString("date_day") );
                data.setDate_time( ls.getString("date_time") );
                data.setCnt( ls.getInt("cnt") );

                list.add(data);
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
    * 월시통계 리스트
    * @param  box          receive from the form object and session
    * @return ArrayList    월시통계 리스트
    * @throws Exception
    */
    public ArrayList SelectMonthTime(RequestBox box) throws Exception   { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        CountData data = null;

        // String v_gubun = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_gubun = box.getString("s_grcode");
        String v_subj = box.getString("s_subjcourse");
        String v_subjseq = box.getString("s_subjseq");
        String v_year  = box.getStringDefault("p_date_year",  FormatDate.getDate("yyyy") );
        String v_month = box.getStringDefault("p_date_month", FormatDate.getDate("MM") );

        if ( v_gubun.equals(""))  v_gubun = "N000001";
        if ( v_subj.equals(""))   v_subj = "ALL";
        if ( v_subjseq.equals(""))    v_subjseq = "ALL";

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql = "select date_year, date_month, date_time, sum(cnt) cnt ";
            sql += " from TZ_SUBJCOUNT                                   ";
            sql += " where date_year   = " + StringManager.makeSQL(v_year);
            sql += "   and date_month  = " + StringManager.makeSQL(v_month);
            if ( !v_gubun.equals("----")) sql += "  and gubun      = " + StringManager.makeSQL(v_gubun);
            if ( !v_subj.equals("ALL")) sql += "  and subj      = " + StringManager.makeSQL(v_subj);
            if ( !v_subjseq.equals("ALL")) sql += "  and subjseq      = " +  StringManager.makeSQL(v_subjseq);
            sql += " group by date_year, date_month, date_time           ";
            sql += " order by date_time asc                              ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new CountData();

//                data.setGubun( ls.getString("gubun") );
                data.setDate_year( ls.getString("date_year") );
                data.setDate_month( ls.getString("date_month") );
                data.setDate_time( ls.getString("date_time") );
                data.setCnt( ls.getInt("cnt") );

                list.add(data);
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
    * 월요일통계 리스트
    * @param  box          receive from the form object and session
    * @return ArrayList    월요일통계 리스트
    * @throws Exception
    */
    public ArrayList SelectMonthWeek(RequestBox box) throws Exception   { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        CountData data = null;

        // String v_gubun = CodeConfigBean.getConfigValue(CONFIG_NAME);
        String v_gubun = box.getString("s_grcode");
        String v_subj = box.getString("s_subjcourse");
        String v_subjseq = box.getString("s_subjseq");
        String v_year  = box.getStringDefault("p_date_year",  FormatDate.getDate("yyyy") );
        String v_month = box.getStringDefault("p_date_month", FormatDate.getDate("MM") );

        if ( v_gubun.equals(""))  v_gubun = "N000001";
        if ( v_subj.equals(""))   v_subj = "ALL";
        if ( v_subjseq.equals(""))    v_subjseq = "ALL";

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql = "select date_year, date_month, date_week, sum(cnt) cnt ";
            sql += " from TZ_SUBJCOUNT                                   ";
            sql += " where date_year   = " + StringManager.makeSQL(v_year);
            sql += "   and date_month  = " + StringManager.makeSQL(v_month);
            if ( !v_gubun.equals("----")) sql += "  and gubun      = " + StringManager.makeSQL(v_gubun);
            if ( !v_subj.equals("ALL")) sql += "  and subj      = " + StringManager.makeSQL(v_subj);
            if ( !v_subjseq.equals("ALL")) sql += "  and subjseq      = " + StringManager.makeSQL(v_subjseq);
            sql += " group by date_year, date_month, date_week     ";
            sql += " order by date_week asc                               ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new CountData();

//                data.setGubun( ls.getString("gubun") );
                data.setDate_year( ls.getString("date_year") );
                data.setDate_month( ls.getString("date_month") );
                data.setDate_week( ls.getString("date_week") );
                data.setCnt( ls.getInt("cnt") );

                list.add(data);
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
    * 과목, 개인별 접속 로그 리스트
    * @param  box          receive from the form object and session
    * @return ArrayList    접속 로그 리스트
    * @throws Exception
    */
    public ArrayList SelectCountLog(RequestBox box) throws Exception   { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_subj    = box.getStringDefault("p_subj","ALL");
        String v_year    = box.getStringDefault("p_year", FormatDate.getDate("yyyy") );
        String v_subjseq = box.getStringDefault("p_subjseq","ALL");
        String v_userid = box.getStringDefault("p_userid","ALL");

        try { 
            list = new ArrayList();
            connMgr = new DBConnectionManager();

            sql = "select subj, year, subjseq, userid, lgip, ldate ";
            sql += " from TZ_SUBJLOGINID                           ";
            sql += " where year   = " + StringManager.makeSQL(v_year);
            if ( !v_subj.equals("ALL"))    sql += " and subj    = " + StringManager.makeSQL(v_subj);
            if ( !v_subjseq.equals("ALL")) sql += " and subjseq = " + StringManager.makeSQL(v_subjseq);
            if ( !v_userid.equals("ALL"))  sql += " and userid  = " + StringManager.makeSQL(v_userid);
            sql += " order by ldate desc                           ";

            ls = connMgr.executeQuery(sql);

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
     *  개인별 출석부 리스트
     * @param  box          receive from the form object and session
     * @return ArrayList   출석부 리스트
     * @throws Exception
     */
     public ArrayList SelectAttend(RequestBox box) throws Exception   { 
    	 DBConnectionManager	connMgr	= null;
         ListSet ls          = null;
         ArrayList list     = null;
         DataBox dbox    	= null;
         String  sql        	= "";
         String  sql1        	= "";

         String  v_subj      = box.getString("p_subj");        // 과목
         String  v_subjseq   = box.getString("p_subjseq"); // 과목 기수
         String  v_year      = box.getString("p_year");    // 년도
         String  v_userid    = box.getString("p_userid");
         String  v_edustart  = "";
         String  v_eduend  = "";

         try { 
             connMgr = new DBConnectionManager();
             list = new ArrayList();
             sql += " select substr(edustart,0,8) edustart, substr(eduend,0,8) eduend from vz_scsubjseq 	\n"+
				" where subj=" + StringManager.makeSQL(v_subj)						+
				" and subjseq=" + StringManager.makeSQL(v_subjseq)					+
				" and year=" + StringManager.makeSQL(v_year);

             ls = connMgr.executeQuery(sql);
             
             ls.next();
              v_edustart = ls.getString(1);
              v_eduend = ls.getString(2);
             ls.close();
             

             sql1 += " select								\n"
           	  +	 " 		a.date_seq								\n"
           	  +  "		,decode(b.isattend,'','X', b.isattend) as ist,b.reason \n"
           	  +  "	from											\n" 
           	  +	 "		(											\n"
           	  +	 "		select										\n" 
           	  +  "			to_char(to_date('"+v_edustart+"', 'YYYYMMDD') + level- 1,'YYYYMMDD') as DATE_SEQ \n" 
           	  +  "		from dual										\n" 
           	  +  "		connect by level<=to_date('"+v_eduend+"', 'YYYYMMDD')-to_date('"+v_edustart+"', 'YYYYMMDD')+1	\n"
           	  +  "		) a left outer join (	\n"    
           	  +  "   select attdate,isattend,reason \n"
           	  +  "   from tz_attendance	\n"
           	  +  "   where attdate between '"+v_edustart+"' and '"+v_eduend+"' \n"
           	  +  " 	 and subj=" + StringManager.makeSQL(v_subj)						
           	  +  "   and year=" + StringManager.makeSQL(v_year)
           	  +  " 	 and subjseq=" + StringManager.makeSQL(v_subjseq)
           	  +	 "   and userid=" + StringManager.makeSQL(v_userid)
           	  +  ") b \n"
           	  +  "on (a.date_seq = b.attdate)\n";
           	 
             ls = connMgr.executeQuery(sql1);

             while ( ls.next() ) { 
                 dbox = ls.getDataBox();
                 list.add(dbox);
             }

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return list;
     }
     
     /**
     출석 수정
     @param box      receive from the form object and session
     @return isOk    1:update success,0:update fail
     */
      public int updateattend(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         PreparedStatement   pstmt   = null;
         String              sql     = "";
         int isOk = 0;
         int commit_chk=0;

    	 String  v_userid  	= box.getString("p_userid");
    	 String v_subjseq  	= box.getString("p_subjseq");
    	 String  v_subj   		= box.getString("p_subj");
    	 String v_year   		= box.getString("p_year");
    	 String s_userid		= box.getSession("userid");
    	 Vector v_attdate		= box.getVector("p_attdate");
    	 Vector v_attendyn	= box.getVector("p_attendyn");  //combo
    	 Vector v_reason		= box.getVector("p_reason"); //변경사유
    	 Vector v_attendyn_chk		= box.getVector("p_attendyn_chk"); //기존값


    	 
         try { 
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false);
             
             for(int i=0;i<v_attendyn_chk.size();i++){
            	 if("X".equals(v_attendyn_chk.elementAt(i).toString()) && "O".equals(v_attendyn.elementAt(i).toString())  ){
	            	 sql  = " insert into tz_attendance													\n"+
	         	 			" values (?, ?, ?, ?, ?,'', ?,TO_CHAR(sysdate, 'YYYYMMDDHH24MISS'),? ,?)		\n";

	            	 pstmt = connMgr.prepareStatement(sql);
		             
	            	 pstmt.setString( 1, v_subj);
		             pstmt.setString( 2, v_year);
		             pstmt.setString( 3, v_subjseq);
		             pstmt.setString( 4, v_userid);
		             pstmt.setString( 5, v_attdate.elementAt(i).toString());
		             pstmt.setString( 6, v_attendyn.elementAt(i).toString());
		             pstmt.setString( 7, v_reason.elementAt(i).toString());
		             pstmt.setString( 8, s_userid);
		             
		             isOk = pstmt.executeUpdate();
                     
		             if(isOk > 0){
		            	 commit_chk++;
		             }

            	 }else if("O".equals(v_attendyn_chk.elementAt(i).toString()) && "X".equals(v_attendyn.elementAt(i).toString())){
            		
            		 
            		 sql  = " delete from  tz_attendance													\n"+
	 							" where subj=?														\n"+
			 					" and year=?														\n"+
			 					" and subjseq=?														\n"+
			     		 		" and userid=?														\n"+
			     		 		" and attdate=?														\n";
	            		 
            		 
            	
		         	 pstmt = connMgr.prepareStatement(sql);
			            
		          	 pstmt.setString( 1, v_subj);
		             pstmt.setString( 2, v_year);
		             pstmt.setString( 3, v_subjseq);
		             pstmt.setString( 4, v_userid);
		             pstmt.setString( 5, v_attdate.elementAt(i).toString());
		             
		             isOk = pstmt.executeUpdate();
                     
		             if(isOk > 0){
		            	 commit_chk++;
		             }
		             
            	 }	           
             }
             
                        
             if(commit_chk > 0){
            	 connMgr.commit();
             }else{
            	 connMgr.rollback();
             }
             
             
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return commit_chk;
     }

     
     
     
}