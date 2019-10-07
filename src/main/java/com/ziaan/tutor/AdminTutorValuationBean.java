// **********************************************************
//  1. 제      목: TUTOR ADMIN BEAN
//  2. 프로그램명: TutorAdminBean.java
//  3. 개      요: 강사관리 관리자 bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 
//  7. 수      정:
// **********************************************************
package com.ziaan.tutor;

import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;

public class AdminTutorValuationBean { 
    private ConfigSet   config  ;
    private int         row     ;
    

    public AdminTutorValuationBean() { 
        try { 
            config  = new ConfigSet();
            // row = Integer.parseInt(config.getProperty("page.manage.row") );        // 이 모듈의 페이지당 row 수를 셋팅한다
            row     = 100;
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    강사평가 명단 리스트  2005.01.28 kimsujin
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectTutorGradeList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet ls1         = null;
        ListSet ls2         = null;
        ArrayList list1     = null;
        String sql         = "";
        DataBox dbox1= null;
        int v_pageno = box.getInt("p_pageno");
        String v_subj = "", v_year= "", v_subjseq= "";
        int v_stucnt = 0, v_sulsum=0;
        double v_okrate = 0;

        String  ss_grcode       = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear        = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq        = box.getStringDefault("s_grseq","ALL");     // 교육기수
        String  ss_uclass       = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_mclass       = box.getStringDefault("s_middleclass","ALL");   // 과목분류
        String  ss_lclass       = box.getStringDefault("s_lowerclass","ALL");    // 과목분류
        String  ss_subjcourse   =box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq      = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_action       = box.getString("s_action");
        String  v_orderStr   = box.getString("p_orderStr");           // 정렬할 컬럼명
        String              v_orderType     = box.getString        ("p_orderType"          );   // 정렬할 순서
        if ( v_orderStr.equals("")) v_orderStr = "b.subjnm";
        

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();

                sql += "select                                                                                                                              \n";
                sql += "       rownum                      NO,                                                                                              \n";
                sql += "       b.subj                      subj, -- 과정코드,                                                                               \n";
                sql += "       b.subjnm                    subjnm, --과정명,                                                                               \n";
                sql += "       b.year                      year, --과정명,                                                                               \n";
                sql += "       b.subjseq                   subjseq, --과정차수,                                                                             \n";
                sql += "       a.class                     class, --클래스,                                                                                    \n";
                sql += "       c.userid                    userid, --강사아이디,                                                                         \n";
                sql += "       c.name                      name, --강사명,                                                                                 \n";
                sql += "       (                                                                                                                            \n";
                sql += "        select count(*)                                                                                                             \n";
                sql += "          from tz_student                                                                                                           \n";
                sql += "         where subj = a.subj and year = a.year and subjseq = a.subjseq and class = a.class                                          \n";
                sql += "       ) studentcnt, --입과인원,                                                                                                    \n";
                sql += "       (                                                                                                                            \n";
                sql += "        select count(*)                                                                                                             \n";
                sql += "          from tz_student                                                                                                           \n";
                sql += "         where subj = a.subj and year = a.year and subjseq = a.subjseq and class = a.class and isgraduated = 'Y'                    \n";
                sql += "       ) stoldcnt, --수료인원,                                                                                                      \n";
                sql += "       (                                                                                                                            \n";
                sql += "        select                                                                                                                      \n";
                sql += "               count(*)                                                                                                             \n";
                sql += "          from                                                                                                                      \n";
                sql += "               tz_gong gong                                                                                                         \n";
                sql += "         where                                                                                                                      \n";
                sql += "               gong.subj = a.subj and gong.addate between RPAD(b.edustart, 16, '0') and RPAD(b.eduend, 16, '9')                     \n";
                sql += "           and gong.userid = c.userid                                                                                               \n";
                sql += "       ) gongcnt, --공지사항,                                                                                                       \n";
                sql += "       (                                                                                                                            \n";
                sql += "        select                                                                                                                      \n";
                sql += "               count(*)                                                                                                             \n";
                sql += "          from                                                                                                                      \n";
                sql += "               tz_bds bds,                                                                                                          \n";
                sql += "               tz_board boa                                                                                                         \n";
                sql += "         where                                                                                                                      \n";
                sql += "               bds.tabseq = boa.tabseq                                                                                              \n";
                sql += "           and bds.subj = a.subj and indate between RPAD(b.edustart, 16, '0') and RPAD(b.eduend, 16, '9')                           \n";
                sql += "           and bds.type='SD'                                                                                                        \n";
                sql += "           and boa.userid = c.userid                                                                                                \n";
                sql += "       ) sdcnt, --자료방,                                                                                                          \n";
                sql += "       (                                                                                                                            \n";
                sql += "        select                                                                                                                      \n";
                sql += "               count(*)                                                                                                             \n";
                sql += "          from                                                                                                                      \n";
                sql += "               tz_dic dic                                                                                                           \n";
                sql += "         where                                                                                                                      \n";
                sql += "               dic.subj = a.subj and ldate between RPAD(b.edustart, 16, '0') and RPAD(b.eduend, 16, '9')                            \n";
                sql += "           and dic.luserid = c.userid                                                                                               \n";
                sql += "       ) diccnt, --용어정리,                                                                                                        \n";
                sql += "       (\n";
                sql += "        select\n";
                sql += "               avg(\n";
                sql += "               case when boa.indate-boaa.indate <= 120000 then 4\n";
                sql += "                    when boa.indate-boaa.indate <= 240000 then 3\n";
                sql += "                    when boa.indate-boaa.indate <= 360000 then 2\n";
                sql += "                    else 1\n";
                sql += "               end\n";
                sql += "               ) cnt\n";
                sql += "          from\n";
                sql += "               tz_bds bds,\n";
                sql += "               tz_board boa,\n";
                sql += "               tz_board boaa\n";
                sql += "         where\n";
                sql += "               bds.tabseq = boa.tabseq\n";
                sql += "           and bds.type = 'SQ'\n";
                sql += "           and boa.levels > 1\n";
                sql += "           and bds.subj = b.subj\n";
                sql += "           and boa.indate between RPAD(b.edustart, 16, '0') and RPAD(b.eduend, 16, '9')                                         \n";
                sql += "           and a.subj = b.subj\n";
                sql += "           and a.year = b.year\n";
                sql += "           and a.subjseq = b.subjseq\n";
                sql += "           and a.tuserid = boa.USERID\n";
                sql += "           and boaa.tabseq = boa.tabseq\n";
                sql += "           and boaa.seq = boa.refseq\n";
                sql += "       ) sqtimepoint, --질문방답변시간점수,\n";
                sql += "       (\n";
                sql += "        select\n";
                sql += "             avg(\n";
                sql += "             case when d.ldate-d.repdate <= 120000 then 4\n";
                sql += "                  when d.ldate-d.repdate <= 240000 then 3\n";
                sql += "                  when d.ldate-d.repdate <= 360000 then 2\n";
                sql += "                  else 1\n";
                sql += "             end\n";
                sql += "             ) cnt\n";
                sql += "          from\n";
                sql += "            tz_projassign d \n";
                sql += "         where\n";
                sql += "               d.subj = b.subj\n";
                sql += "           and d.year = b.year\n";
                sql += "           and d.subjseq = b.subjseq\n";
                sql += "           and d.ldate is not null\n";
//                sql += "           and d.repdate between b.edustart and b.eduend\n";
                sql += "           and b.subj = a.subj\n";
                sql += "           and b.year = a.year\n";
                sql += "           and b.subjseq = a.subjseq\n";
                sql += "           and a.tuserid = d.luserid\n";
                sql += "       ) reporttimepoint, --과제첨삭답변시간점수,   \n";
                //sql += "       --GET_ANSERPRT(a.subj, a.tuserid) sqpoint, --답변의질,                                                                         \n";
                sql += "       0 sqpoint, --답변의질,                                                                         \n";
                sql += "       a.point point, --첨삭의질,                                                                                                   \n";
                sql += "       (                                                                                                                            \n";
                sql += "        select                                                                                                                      \n";
                sql += "              min(addate)                                                                                                           \n";
                sql += "          from                                                                                                                      \n";
                sql += "               tz_gong gong                                                                                                         \n";
                sql += "         where                                                                                                                      \n";
                sql += "               gong.subj = a.subj and gong.addate between RPAD(b.edustart, 16, '0') and RPAD(b.eduend, 16, '9')                     \n";
                sql += "           and gong.userid = c.userid                                                                                               \n";
                sql += "       ) gongfirsttime --알림방최초등록시간                                                                                          \n";
                sql += "       ,b.edustart                                                                                          \n";                
                sql += "  from                                                                                                                              \n";
                sql += "       tz_classtutor a,                                                                                                             \n";
                sql += "       VZ_SCSUBJSEQ b,                                                                                                                \n";
                sql += "       tz_tutor c                                                                                                                   \n";
                sql += " where                                                                                                                              \n";
                sql += "       a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq                                                                \n";
                sql += "   and a.tuserid = c.userid                                                                                                         \n";
                
                
                if ( !ss_grcode.equals("ALL") && !ss_grcode.equals("----"))
                    sql += " and b.grcode       = " +SQLString.Format(ss_grcode);
                if ( !ss_gyear.equals("ALL"))      sql += " and b.gyear        = " +SQLString.Format(ss_gyear);
                if ( !ss_grseq.equals("ALL"))      sql += " and b.grseq        = " +SQLString.Format(ss_grseq);
                if ( !ss_uclass.equals("ALL"))     sql += " and b.scupperclass = " +SQLString.Format(ss_uclass);
                if ( !ss_mclass.equals("ALL"))     sql += " and b.scmiddleclass = " +SQLString.Format(ss_mclass);
                if ( !ss_lclass.equals("ALL"))     sql += " and b.sclowerclass = " +SQLString.Format(ss_lclass);
                if ( !ss_subjcourse.equals("ALL")) sql += " and b.scsubj       = " +SQLString.Format(ss_subjcourse);
                if ( !ss_subjseq.equals("ALL"))    sql += " and b.scsubjseq    = " +SQLString.Format(ss_subjseq);

                if ( v_orderStr.equals("") ) { 
                    sql += " order by b.subjnm           \n";
                } else { 
                    sql += " order by " + v_orderStr+" " + v_orderType + " \n";
                }
                
                
                System.out.println("\n\n"+sql+"\n\n");
                ls1 = connMgr.executeQuery(sql);

                while ( ls1.next() ) { 
                    dbox1 = ls1.getDataBox();
                    dbox1.put("d_sqtimepoint", new Double(ls1.getDouble("sqtimepoint"))); 
                    dbox1.put("d_reporttimepoint", new Double(ls1.getDouble("reporttimepoint"))); 
                    dbox1.put("d_sqpoint", new Double(ls1.getDouble("sqpoint"))); 
                    dbox1.put("d_point", new Double(ls1.getDouble("point"))); 
                    list1.add(dbox1);
                }       // END while
            }       // END if

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
    
     /**
     공지사항 등록 일 리스트  2005.01.28 kimsujin
     @param box      receive from the form object and session
     @return ArrayList
     */
      public ArrayList selectTutorGradeListGong(RequestBox box) throws Exception { 
         DBConnectionManager connMgr = null;
         ListSet ls1         = null;
         ListSet ls2         = null;
         ArrayList list1     = null;
         String sql         = "";
         DataBox dbox1= null;
         int v_pageno = box.getInt("p_pageno");
         String v_subj = "", v_year= "", v_subjseq= "";
         int v_stucnt = 0, v_sulsum=0;
         double v_okrate = 0;

         String  ss_grcode       = box.getStringDefault("s_grcode","ALL");    // 교육그룹
         String  ss_gyear        = box.getStringDefault("s_gyear","ALL");     // 년도
         String  ss_grseq        = box.getStringDefault("s_grseq","ALL");     // 교육기수
         String  ss_uclass       = box.getStringDefault("s_upperclass","ALL");    // 과목분류
         String  ss_mclass       = box.getStringDefault("s_middleclass","ALL");   // 과목분류
         String  ss_lclass       = box.getStringDefault("s_lowerclass","ALL");    // 과목분류
         String  ss_subjcourse   =box.getStringDefault("s_subjcourse","ALL");// 과목&코스
         String  ss_subjseq      = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
         String  ss_action       = box.getString("s_action");
         String  v_orderStr   = box.getString("p_orderStr");           // 정렬할 컬럼명
         String              v_orderType     = box.getString        ("p_orderType"          );   // 정렬할 순서
         v_orderStr = "b.subjnm";
         

         try { 
             if ( ss_action.equals("go") ) { 
                 connMgr = new DBConnectionManager();
                 list1 = new ArrayList();

                 sql += "select\n";
                 sql += "       b.subjnm                    subjnm, --과정명, \n";
                 sql += "       c.name                      name, --강사명,   \n";
                 sql += "      d.TITLE, \n";
                 sql += "      d.LDATE \n";
                 sql += "  from\n";
                 sql += "       tz_classtutor a, \n";
                 sql += "       VZ_SCSUBJSEQ b, \n";
                 sql += "       tz_tutor c, \n";
                 sql += "      tz_gong d \n";
                 sql += " where\n";
                 sql += "       a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq \n";
                 sql += "   and a.tuserid = c.userid \n";
                 sql += "   and d.subj = a.subj  \n";
                 sql += "   and d.addate between b.edustart and b.eduend \n";
                 sql += "   and d.userid = c.userid  \n";
                 
                 if ( !ss_grcode.equals("ALL") && !ss_grcode.equals("----"))
                     sql += " and b.grcode       = " +SQLString.Format(ss_grcode);
                 if ( !ss_gyear.equals("ALL"))      sql += " and b.gyear        = " +SQLString.Format(ss_gyear);
                 if ( !ss_grseq.equals("ALL"))      sql += " and b.grseq        = " +SQLString.Format(ss_grseq);
                 if ( !ss_uclass.equals("ALL"))     sql += " and b.scupperclass = " +SQLString.Format(ss_uclass);
                 if ( !ss_mclass.equals("ALL"))     sql += " and b.scmiddleclass = " +SQLString.Format(ss_mclass);
                 if ( !ss_lclass.equals("ALL"))     sql += " and b.sclowerclass = " +SQLString.Format(ss_lclass);
                 if ( !ss_subjcourse.equals("ALL")) sql += " and b.scsubj       = " +SQLString.Format(ss_subjcourse);
                 if ( !ss_subjseq.equals("ALL"))    sql += " and b.scsubjseq    = " +SQLString.Format(ss_subjseq);

                 if ( v_orderStr.equals("") ) { 
                     sql += " order by b.subjnm           \n";
                 } else { 
                     sql += " order by " + v_orderStr+" " + v_orderType + " \n";
                 }
                 
                 
                 System.out.println("\n\n"+sql+"\n\n");
                 ls1 = connMgr.executeQuery(sql);

                 while ( ls1.next() ) { 
                     dbox1 = ls1.getDataBox();
                     list1.add(dbox1);
                 }       // END while
             }       // END if

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
             if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return list1;
     }

      /**
      질문방 답변시간 리스트  2005.01.28 kimsujin
      @param box      receive from the form object and session
      @return ArrayList
      */
       public ArrayList selectTutorGradeListSQ(RequestBox box) throws Exception { 
          DBConnectionManager connMgr = null;
          ListSet ls1         = null;
          ListSet ls2         = null;
          ArrayList list1     = null;
          String sql         = "";
          DataBox dbox1= null;
          int v_pageno = box.getInt("p_pageno");
          String v_subj = "", v_year= "", v_subjseq= "";
          int v_stucnt = 0, v_sulsum=0;
          double v_okrate = 0;

          String  ss_grcode       = box.getStringDefault("s_grcode","ALL");    // 교육그룹
          String  ss_gyear        = box.getStringDefault("s_gyear","ALL");     // 년도
          String  ss_grseq        = box.getStringDefault("s_grseq","ALL");     // 교육기수
          String  ss_uclass       = box.getStringDefault("s_upperclass","ALL");    // 과목분류
          String  ss_mclass       = box.getStringDefault("s_middleclass","ALL");   // 과목분류
          String  ss_lclass       = box.getStringDefault("s_lowerclass","ALL");    // 과목분류
          String  ss_subjcourse   =box.getStringDefault("s_subjcourse","ALL");// 과목&코스
          String  ss_subjseq      = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
          String  ss_action       = box.getString("s_action");
          String  v_orderStr   = box.getString("p_orderStr");           // 정렬할 컬럼명
          String              v_orderType     = box.getString        ("p_orderType"          );   // 정렬할 순서
          v_orderStr = "s.subjnm";
          

          try { 
              if ( ss_action.equals("go") ) { 
                  connMgr = new DBConnectionManager();
                  list1 = new ArrayList();

                  sql += "select\n";
                  sql += "     SUM( \n";
                  sql += "      case when s.indate-boaa.indate <= 120000 then 1 \n";
                  sql += "           else 0 \n";
                  sql += "      end \n";
                  sql += "      )  cnt12 \n";
                  sql += "    ,SUM( \n";
                  sql += "      case when s.indate-boaa.indate <= 120000 then 0 \n";
                  sql += "           when s.indate-boaa.indate <= 240000 then 1 \n";
                  sql += "           else 0 \n";
                  sql += "      end \n";
                  sql += "      )  cnt24 \n";
                  sql += "    ,SUM(\n";
                  sql += "      case when s.indate-boaa.indate <= 120000 then 0 \n";
                  sql += "           when s.indate-boaa.indate <= 240000 then 0 \n";
                  sql += "           when s.indate-boaa.indate <= 360000 then 1 \n";
                  sql += "           else 0 \n";
                  sql += "      end \n";
                  sql += "      )  cnt36 \n";
                  sql += "    ,SUM( \n";
                  sql += "      case when s.indate-boaa.indate <= 120000 then 0 \n";
                  sql += "           when s.indate-boaa.indate <= 240000 then 0 \n";
                  sql += "           when s.indate-boaa.indate <= 360000 then 0 \n";
                  sql += "           else 1 \n";
                  sql += "      end \n";
                  sql += "      )  cntetc \n";
                  sql += "     ,SUM( \n";
                  sql += "      case when s.indate-boaa.indate <= 120000 then 1 \n";
                  sql += "           when s.indate-boaa.indate <= 240000 then 1 \n";
                  sql += "           when s.indate-boaa.indate <= 360000 then 1\n";
                  sql += "           else 1 \n";
                  sql += "      end \n";
                  sql += "      ) cnt \n";
                  sql += "     , s.subjnm \n";
                  sql += "     , rank() over (order by avg( \n";
                  sql += "      case when s.indate-boaa.indate <= 120000 then 4 \n";
                  sql += "           when s.indate-boaa.indate <= 240000 then 3 \n";
                  sql += "           when s.indate-boaa.indate <= 360000 then 2 \n";
                  sql += "           else 1 \n";
                  sql += "      end ) desc) rank \n";
                  sql += "from \n";
                  sql += "         tz_board boaa, \n";
                  sql += "     ( \n";
                  sql += "      select \n";
                  sql += "             boa.tabseq, \n";
                  sql += "             boa.seq, \n";
                  sql += "             boa.REFSEQ, \n";
                  sql += "             boa.INDATE, \n";
                  sql += "             b.subjnm \n";
                  sql += "        from \n";
                  sql += "             tz_bds bds, \n";
                  sql += "             tz_board boa, \n";
                  sql += "             vz_scsubjseq b, \n";
                  sql += "             tz_classtutor ctt \n";
                  sql += "       where \n";
                  sql += "             bds.tabseq = boa.tabseq \n";
                  sql += "         and bds.type = 'SQ'   \n";
                  sql += "         and boa.levels > 1 \n";
                  sql += "         and bds.subj = b.subj \n";
                  sql += "         and boa.indate between RPAD(b.edustart, 16, '0') and RPAD(b.eduend, 16, '9') \n";
                  sql += "         and b.subj = ctt.subj \n";
                  sql += "         and b.year = ctt.year \n";
                  sql += "         and b.subjseq = ctt.subjseq \n";
                  sql += "         and boa.USERID = ctt.tuserid \n";
                  if ( !ss_grcode.equals("ALL") && !ss_grcode.equals("----"))
                      sql += " and b.grcode       = " +SQLString.Format(ss_grcode);
                  if ( !ss_gyear.equals("ALL"))      sql += " and b.gyear        = " +SQLString.Format(ss_gyear);
                  if ( !ss_grseq.equals("ALL"))      sql += " and b.grseq        = " +SQLString.Format(ss_grseq);
                  if ( !ss_uclass.equals("ALL"))     sql += " and b.scupperclass = " +SQLString.Format(ss_uclass);
                  if ( !ss_mclass.equals("ALL"))     sql += " and b.scmiddleclass = " +SQLString.Format(ss_mclass);
                  if ( !ss_lclass.equals("ALL"))     sql += " and b.sclowerclass = " +SQLString.Format(ss_lclass);
                  if ( !ss_subjcourse.equals("ALL")) sql += " and b.scsubj       = " +SQLString.Format(ss_subjcourse);
                  if ( !ss_subjseq.equals("ALL"))    sql += " and b.scsubjseq    = " +SQLString.Format(ss_subjseq);
                  sql += "     ) s \n";
                  sql += " where boaa.tabseq = s.tabseq \n";
                  sql += " and   boaa.seq = s.refseq \n";
                  sql += " group by s.subjnm \n";
                  
                  if ( v_orderStr.equals("") ) { 
                      sql += " order by s.subjnm           \n";
                  } else { 
                      sql += " order by " + v_orderStr+" " + v_orderType + " \n";
                  }
                  
                  
                  System.out.println("\n\n"+sql+"\n\n");
                  ls1 = connMgr.executeQuery(sql);

                  while ( ls1.next() ) { 
                      dbox1 = ls1.getDataBox();
                      list1.add(dbox1);
                  }       // END while
              }       // END if

          } catch ( Exception ex ) { 
              ErrorManager.getErrorStackTrace(ex, box, sql);
              throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
          } finally { 
              if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
              if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }

          return list1;
      }

       /**
       과제첨삭 답변시간 리스트  2005.01.28 kimsujin
       @param box      receive from the form object and session
       @return ArrayList
       */
        public ArrayList selectTutorGradeListReport(RequestBox box) throws Exception { 
           DBConnectionManager connMgr = null;
           ListSet ls1         = null;
           ListSet ls2         = null;
           ArrayList list1     = null;
           String sql         = "";
           DataBox dbox1= null;
           int v_pageno = box.getInt("p_pageno");
           String v_subj = "", v_year= "", v_subjseq= "";
           int v_stucnt = 0, v_sulsum=0;
           double v_okrate = 0;

           String  ss_grcode       = box.getStringDefault("s_grcode","ALL");    // 교육그룹
           String  ss_gyear        = box.getStringDefault("s_gyear","ALL");     // 년도
           String  ss_grseq        = box.getStringDefault("s_grseq","ALL");     // 교육기수
           String  ss_uclass       = box.getStringDefault("s_upperclass","ALL");    // 과목분류
           String  ss_mclass       = box.getStringDefault("s_middleclass","ALL");   // 과목분류
           String  ss_lclass       = box.getStringDefault("s_lowerclass","ALL");    // 과목분류
           String  ss_subjcourse   =box.getStringDefault("s_subjcourse","ALL");// 과목&코스
           String  ss_subjseq      = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
           String  ss_action       = box.getString("s_action");
           String  v_orderStr   = box.getString("p_orderStr");           // 정렬할 컬럼명
           String              v_orderType     = box.getString        ("p_orderType"          );   // 정렬할 순서
           v_orderStr = "b.subjnm";
           

           try { 
               if ( ss_action.equals("go") ) { 
                   connMgr = new DBConnectionManager();
                   list1 = new ArrayList();

                   sql += "select\n";
                   sql += "    b.subjnm\n";
                   sql += "   ,SUM(\n";
                   sql += "     case when a.ldate-a.repdate <= 120000 then 1 \n";
                   sql += "          else 0 \n";
                   sql += "     end\n";
                   sql += "     )  cnt12\n";
                   sql += "   ,SUM(\n";
                   sql += "     case when a.ldate-a.repdate <= 120000 then 0 \n";
                   sql += "          when a.ldate-a.repdate <= 240000 then 1 \n";
                   sql += "          else 0 \n";
                   sql += "     end\n";
                   sql += "     )  cnt24\n";
                   sql += "   ,SUM(\n";
                   sql += "     case when a.ldate-a.repdate <= 120000 then 0 \n";
                   sql += "          when a.ldate-a.repdate <= 240000 then 0 \n";
                   sql += "          when a.ldate-a.repdate <= 360000 then 1 \n";
                   sql += "          else 0 \n";
                   sql += "     end\n";
                   sql += "     )  cnt36\n";
                   sql += "   ,SUM(\n";
                   sql += "     case when a.ldate-a.repdate <= 120000 then 0 \n";
                   sql += "          when a.ldate-a.repdate <= 240000 then 0 \n";
                   sql += "          when a.ldate-a.repdate <= 360000 then 0 \n";
                   sql += "          else 1 \n";
                   sql += "     end\n";
                   sql += "     )  cntetc\n";
                   sql += "    ,sum(\n";
                   sql += "     case when a.ldate-a.repdate <= 120000 then 1 \n";
                   sql += "          when a.ldate-a.repdate <= 240000 then 1 \n";
                   sql += "          when a.ldate-a.repdate <= 360000 then 1 \n";
                   sql += "          else 1 \n";
                   sql += "     end\n";
                   sql += "     ) cnt\n";
                   sql += "    , rank() over (order by avg(\n";
                   sql += "     case when a.ldate-a.repdate <= 120000 then 4 \n";
                   sql += "          when a.ldate-a.repdate <= 240000 then 3 \n";
                   sql += "          when a.ldate-a.repdate <= 360000 then 2 \n";
                   sql += "          else 1 \n";
                   sql += "     end ) desc) rank \n";
                   sql += "  from  \n";
                   sql += "    tz_projassign a,\n";
                   sql += "    vz_scsubjseq b,\n";
                   sql += "    tz_classtutor c \n";
                   sql += " where\n";
                   sql += "       a.subj = b.subj \n";
                   sql += "   and a.year = b.year \n";
                   sql += "   and a.subjseq = b.subjseq \n";
                   sql += "   and a.ldate is not null \n";
//                   sql += "   and a.repdate between b.edustart and b.eduend\n";
                   if ( !ss_grcode.equals("ALL") && !ss_grcode.equals("----"))
                       sql += " and b.grcode       = " +SQLString.Format(ss_grcode);
                   if ( !ss_gyear.equals("ALL"))      sql += " and b.gyear        = " +SQLString.Format(ss_gyear);
                   if ( !ss_grseq.equals("ALL"))      sql += " and b.grseq        = " +SQLString.Format(ss_grseq);
                   if ( !ss_uclass.equals("ALL"))     sql += " and b.scupperclass = " +SQLString.Format(ss_uclass);
                   if ( !ss_mclass.equals("ALL"))     sql += " and b.scmiddleclass = " +SQLString.Format(ss_mclass);
                   if ( !ss_lclass.equals("ALL"))     sql += " and b.sclowerclass = " +SQLString.Format(ss_lclass);
                   if ( !ss_subjcourse.equals("ALL")) sql += " and b.scsubj       = " +SQLString.Format(ss_subjcourse);
                   if ( !ss_subjseq.equals("ALL"))    sql += " and b.scsubjseq    = " +SQLString.Format(ss_subjseq);
                   sql += "   and b.subj = c.subj \n";
                   sql += "   and b.year = c.year \n";
                   sql += "   and b.subjseq = c.subjseq \n";
                   sql += "   and c.tuserid = a.luserid \n";
                   sql += " group by \n";
                   sql += "       b.subjnm\n";
                   
                   if ( v_orderStr.equals("") ) { 
                       sql += " order by b.subjnm           \n";
                   } else { 
                       sql += " order by " + v_orderStr+" " + v_orderType + " \n";
                   }
                   
                   ls1 = connMgr.executeQuery(sql);

                   while ( ls1.next() ) { 
                       dbox1 = ls1.getDataBox();
                       list1.add(dbox1);
                   }       // END while
               }       // END if

           } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
               throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
           } finally { 
               if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
               if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
               if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
           }

           return list1;
       }
       
     /**
     강사운영자평가 저장  2005.01.28 kimsujin
     @param box      receive from the form object and session
     @return ArrayList
     */
      public int calcTutorGrade(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr = null;
         ListSet ls1 = null;
         String sql = "";
         int isOk = 0;
         String  v_subj   = box.getString("p_subj");
         String  v_year   = box.getString("p_year");
         String  v_subjseq   = box.getString("p_subjseq");
         String  v_class   = box.getString("p_class");
         String  v_userid   = box.getString("p_userid");
         String  v_point   = box.getString("p_point");
         
         try { 
             connMgr = new DBConnectionManager();
             sql += "update tz_classtutor set point="+v_point+" where subj='"+v_subj+"' and year='"+v_year+"' and subjseq='"+v_subjseq+"' and class='"+v_class+"' and tuserid='"+v_userid+"' ";
             System.out.println(sql);
             isOk = connMgr.executeUpdate(sql);
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return isOk;
     }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    


}