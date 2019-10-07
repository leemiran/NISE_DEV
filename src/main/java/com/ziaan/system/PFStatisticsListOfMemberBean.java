// **********************************************************
//  1. 제      목: 인사DB 검색
//  2. 프로그램명: MemberSearchBean.java
//  3. 개      요: 인사DB 검색
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성:  2004. 12. 20
//  7. 수      정:
// **********************************************************

package com.ziaan.system;

import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class PFStatisticsListOfMemberBean {

    // private
    private ConfigSet config;
    private int         row;
    
    public PFStatisticsListOfMemberBean() {
    }


    /**
    과정별 교육실적
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList perform_03_01_01(RequestBox box) throws Exception {
         DBConnectionManager connMgr         = null;
         ListSet             ls              = null;
         ArrayList           list            = null;
         StringBuffer        sbSQL           = new StringBuffer("");
          
         DataBox             dbox            = null;
          
         String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
         String              v_courseyear    = box.getStringDefault  ( "p_courseyear"   , ""    ); // 년도
         String              v_courseseq     = box.getStringDefault  ( "p_courseseq"    , ""    ); // 과목 기수
         String              v_coursecode    = box.getStringDefault  ( "p_coursecode"   , ""    ); // 과목&코스
         String              s_gadmin        = box.getSession        ( "gadmin"                 );
         String              v_branch        = box.getStringDefault  ( "p_branch"       , ""    );
         String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
         String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
          
        String              v_orderColumn   = box.getString( "p_orderColumn"  );  // 정렬할 컬럼명
        String              v_orderType     = box.getString( "p_orderType"    );  // 정렬할 순서
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();    

            sbSQL.append(" SELECT  a.grcode                                                                                                                   \n")                                       
                 .append("     ,   a.coursenm                                                                                                                 \n")
                 .append("     ,   a.coursecode                                                                                                               \n")
                 .append("     ,   a.courseyear                                                                                                               \n")
                 .append("     ,   a.courseseq                                                                                                                \n")
                 .append("     ,   a.userid                                                                                                                 \n")
                 .append("     ,   a.name                                                                                                                     \n")
                 .append("     ,   a.subj                                                                                                                     \n")
                 .append("     ,   a.subjnm                                                                                                                   \n")
                 .append("     ,   a.isrequired                                                                                                               \n")
                 .append("     ,   a.isgraduated                                                                                                              \n")
                 .append("     ,   a.score                                                                                                                    \n")
                 .append("     ,   a.tstep                                                                                                                    \n")
                 .append("     ,   a.mtest                                                                                                                    \n")
                 .append("     ,   a.ftest                                                                                                                    \n")
                 .append("     ,   a.report                                                                                                                   \n")
                 .append("     ,   a.act                                                                                                                      \n")
                 .append("     ,   a.etc1                                                                                                                     \n")
                 .append("     ,   a.etc2                                                                                                                     \n")
                 .append("     ,   a.avtstep                                                                                                                  \n")
                 .append("     ,   a.avmtest                                                                                                                  \n")
                 .append("     ,   a.avftest                                                                                                                  \n")
                 .append("     ,   a.avreport                                                                                                                 \n")
                 .append("     ,   a.avact                                                                                                                    \n")
                 .append("     ,   a.avetc1                                                                                                                   \n")
                 .append("     ,   a.avetc2                                                                                                                   \n")
                 .append("     ,   a.htest                                                                                                                    \n")
                 .append("     ,   a.avhtest                                                                                                                  \n")
                 .append("     ,   a.course_graduated                                                                                                         \n")
                 .append("     ,   b.total                                                                                                                  \n")
                 .append("     ,   b.requried_n_count                                                                                                       \n")
                 .append("     ,   b.graduated_n_count                                                                                                      \n")
                 .append("     ,   b.requried_y_count                                                                                                       \n")
                 .append("     ,   b.graduated_y_count                                                                                                      \n")
                 .append(" FROM    (                                                                                                                        \n")
                 .append("          select  b.grcode                                                                                                        \n")
                 .append("                 ,   a.coursenm                                                                                                   \n")
                 .append("                 ,   b.coursecode                                                                                                 \n")
                 .append("                 ,   b.courseyear                                                                                                 \n")
                 .append("                 ,   b.courseseq                                                                                                  \n")
                 .append("                 ,   d.userid                                                                                                     \n")
                 .append("                 ,   d.name                                                                                                       \n")
                 .append("                 ,   e.subj                                                                                                       \n")
                 .append("                 ,   e.subjnm                                                                                                     \n")
                 .append("                 ,   c.isrequired                                                                                                 \n")
                 .append("                 ,   f.isgraduated                                                                                                \n")
                 .append("                 ,   f.score                                                                                                      \n")
                 .append("                 ,   f.tstep                                                                                                      \n")
                 .append("                 ,   f.mtest                                                                                                      \n")
                 .append("                 ,   f.ftest                                                                                                      \n")
                 .append("                 ,   f.report                                                                                                     \n")
                 .append("                 ,   f.act                                                                                                        \n")
                 .append("                 ,   f.etc1                                                                                                       \n")
                 .append("                 ,   f.etc2                                                                                                       \n")
                 .append("                 ,   f.avtstep                                                                                                    \n")
                 .append("                 ,   f.avmtest                                                                                                    \n")
                 .append("                 ,   f.avftest                                                                                                    \n")
                 .append("                 ,   f.avreport                                                                                                   \n")
                 .append("                 ,   f.avact                                                                                                      \n")
                 .append("                 ,   f.avetc1                                                                                                     \n")
                 .append("                 ,   f.avetc2                                                                                                     \n")
                 .append("                 ,   f.htest                                                                                                      \n")
                 .append("                 ,   f.avhtest                                                                                                    \n")
                 .append("                 ,   h.isgraduated       course_graduated                                                                         \n")
                 .append("             FROM    tz_pfcourse         a                                                                                        \n")
                 .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                 .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                 .append("                 ,   tz_member           d                                                                                        \n")
                 .append("                 ,   tz_subj             e                                                                                        \n")
                 .append("                 ,   tz_student          f                                                                                        \n")
                 .append("                 ,   (                                                                                                            \n")
                 .append("                         select  i.userid, i.subj, max(i.score) max_score                                                         \n")
                 .append("                         from    tz_student  i                                                                                    \n")
                 .append("                             ,   tz_subjseq  j                                                                                    \n")
                 .append("                         where   i.subj      = j.subj                                                                             \n")
                 .append("                         and     i.subjseq   = j.subjseq                                                                          \n")
                 .append("                         and     i.year      = j.year                                                                             \n")
                 .append("                         and     j.isblended = 'N'                                                                                \n")
                 .append("                         and     j.isexpert  = 'N'                                                                                \n")
                 .append("                         group by i.userid, i.subj                                                                                \n")
                 .append("                     )                   g                                                                                        \n")
                 .append("                 ,   tz_pfstudent        h                                                                                        \n")
                 .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                 .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                 .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                 .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                 .append("             AND     c.userid        = d.userid                                                                                   \n")
                 .append("             AND     c.subj          = e.subj                                                                                     \n")
                 .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                 .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                 .append("             AND     c.userid        = h.userid                                                                                   \n")
                 .append("             AND     b.coursecode    = h.coursecode                                                                               \n")
                 .append("             AND     b.courseyear    = h.courseyear                                                                               \n")
                 .append("             AND     b.courseseq     = h.courseseq                                                                                \n")
                 .append("             AND     c.status        = 'RV'                                                                                       \n")
                 .append("             AND     f.userid        = g.userid                                                                                   \n")
                 .append("             AND     f.subj          = g.subj                                                                                     \n")
                 .append("             AND     g.max_score     = f.score                                                                                    \n")
                 .append("             GROUP BY b.grcode                                                                                                    \n")
                 .append("                 ,    b.coursecode                                                                                                \n")
                 .append("                 ,    b.courseyear                                                                                                \n")
                 .append("                 ,    b.courseseq                                                                                                 \n")
                 .append("                 ,    a.coursenm                                                                                                  \n")
                 .append("                 ,    d.userid                                                                                                    \n")
                 .append("                 ,    d.name                                                                                                      \n")
                 .append("                 ,    e.subj                                                                                                      \n")
                 .append("                 ,    e.subjnm                                                                                                    \n")
                 .append("                 ,    c.isrequired                                                                                                \n")
                 .append("                 ,    f.isgraduated                                                                                               \n")
                 .append("                 ,    f.score                                                                                                     \n")
                 .append("                 ,    f.tstep                                                                                                     \n")
                 .append("                 ,    f.mtest                                                                                                     \n")
                 .append("                 ,    f.ftest                                                                                                     \n")
                 .append("                 ,    f.report                                                                                                    \n")
                 .append("                 ,    f.act                                                                                                       \n")
                 .append("                 ,    f.etc1                                                                                                      \n")
                 .append("                 ,    f.etc2                                                                                                      \n")
                 .append("                 ,    f.avtstep                                                                                                   \n")
                 .append("                 ,    f.avmtest                                                                                                   \n")
                 .append("                 ,    f.avftest                                                                                                   \n")
                 .append("                 ,    f.avreport                                                                                                  \n")
                 .append("                 ,    f.avact                                                                                                     \n")
                 .append("                 ,    f.avetc1                                                                                                    \n")
                 .append("                 ,    f.avetc2                                                                                                    \n")
                 .append("                 ,    f.htest                                                                                                     \n")
                 .append("                 ,    f.avhtest                                                                                                   \n")
                 .append("                 ,    h.isgraduated                                                                                               \n")
                 .append("             UNION                                                                                                                \n")
                 .append("             SELECT  b.grcode                                                                                                     \n")
                 .append("                 ,   a.coursenm                                                                                                   \n")
                 .append("                 ,   b.coursecode                                                                                                 \n")
                 .append("                 ,   b.courseyear                                                                                                 \n")
                 .append("                 ,   b.courseseq                                                                                                  \n")
                 .append("                 ,   d.userid                                                                                                     \n")
                 .append("                 ,   d.name                                                                                                       \n")
                 .append("                 ,   e.subj                                                                                                       \n")
                 .append("                 ,   e.subjnm                                                                                                     \n")
                 .append("                 ,   c.isrequired                                                                                                 \n")
                 .append("                 ,   f.isgraduated                                                                                                \n")
                 .append("                 ,   f.score                                                                                                      \n")
                 .append("                 ,   f.tstep                                                                                                      \n")
                 .append("                 ,   f.mtest                                                                                                      \n")
                 .append("                 ,   f.ftest                                                                                                      \n")
                 .append("                 ,   f.report                                                                                                     \n")
                 .append("                 ,   f.act                                                                                                        \n")
                 .append("                 ,   f.etc1                                                                                                       \n")
                 .append("                 ,   f.etc2                                                                                                       \n")
                 .append("                 ,   f.avtstep                                                                                                    \n")
                 .append("                 ,   f.avmtest                                                                                                    \n")
                 .append("                 ,   f.avftest                                                                                                    \n")
                 .append("                 ,   f.avreport                                                                                                   \n")
                 .append("                 ,   f.avact                                                                                                      \n")
                 .append("                 ,   f.avetc1                                                                                                     \n")
                 .append("                 ,   f.avetc2                                                                                                     \n")
                 .append("                 ,   f.htest                                                                                                      \n")
                 .append("                 ,   f.avhtest                                                                                                    \n")
                 .append("                 ,   g.isgraduated       course_graduated                                                                         \n")
                 .append("             FROM    tz_pfcourse         a                                                                                        \n")
                 .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                 .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                 .append("                 ,   tz_member           d                                                                                        \n")
                 .append("                 ,   tz_subj             e                                                                                        \n")
                 .append("                 ,   tz_student            f                                                                                        \n")
                 .append("                 ,   tz_pfstudent        g                                                                                        \n")
                 .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                 .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                 .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                 .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                 .append("             AND     c.userid        = d.userid                                                                                   \n")
                 .append("             AND     c.subj          = e.subj                                                                                     \n")
                 .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                 .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                 .append("             AND     c.userid        = g.userid                                                                                   \n")
                 .append("             AND     b.coursecode    = g.coursecode                                                                               \n")
                 .append("             AND     b.courseyear    = g.courseyear                                                                               \n")
                 .append("             AND     b.courseseq     = g.courseseq                                                                                \n")
                 .append("             AND     c.status in ( 'RE', 'FE' )                                                                                   \n")
                 .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                 .append("             AND     c.year          = f.year(+)                                                                                  \n")
                 .append("             AND     c.subjseq       = f.subjseq(+)                                                                               \n")
                 .append("             GROUP BY b.grcode                                                                                                    \n")
                 .append("                 ,    b.coursecode                                                                                                \n")
                 .append("                 ,    b.courseyear                                                                                                \n")
                 .append("                 ,    b.courseseq                                                                                                 \n")
                 .append("                 ,    a.coursenm                                                                                                  \n")
                 .append("                 ,    d.userid                                                                                                    \n")
                 .append("                 ,    d.name                                                                                                      \n")
                 .append("                 ,    e.subj                                                                                                      \n")
                 .append("                 ,    e.subjnm                                                                                                    \n")
                 .append("                 ,    c.isrequired                                                                                                \n")
                 .append("                 ,    f.isgraduated                                                                                               \n")
                 .append("                 ,    f.score                                                                                                     \n")
                 .append("                 ,    f.tstep                                                                                                     \n")
                 .append("                 ,    f.mtest                                                                                                     \n")
                 .append("                 ,    f.ftest                                                                                                     \n")
                 .append("                 ,    f.report                                                                                                    \n")
                 .append("                 ,    f.act                                                                                                       \n")
                 .append("                 ,    f.etc1                                                                                                      \n")
                 .append("                 ,    f.etc2                                                                                                      \n")
                 .append("                 ,    f.avtstep                                                                                                   \n")
                 .append("                 ,    f.avmtest                                                                                                   \n")
                 .append("                 ,    f.avftest                                                                                                   \n")
                 .append("                 ,    f.avreport                                                                                                  \n")
                 .append("                 ,    f.avact                                                                                                     \n")
                 .append("                 ,    f.avetc1                                                                                                    \n")
                 .append("                 ,    f.avetc2                                                                                                    \n")
                 .append("                 ,    f.htest                                                                                                     \n")
                 .append("                 ,    f.avhtest                                                                                                   \n")
                 .append("                 ,    g.isgraduated                                                                                               \n")
                 .append("             ORDER BY name, isrequired desc                                                                                       \n")
                 .append("         )           a                                                                                                            \n")
                 .append("     ,   (                                                                                                                        \n")
                 .append("             SELECT  grcode                                                                                                       \n")
                 .append("                 ,   courseyear                                                                                                   \n")
                 .append("                 ,   courseseq                                                                                                    \n")
                 .append("                 ,   coursecode                                                                                                   \n")
                 .append("                 ,   userid                                                                                                       \n")
                 .append("                 ,   avg(score                                                               )   total                            \n")
                 .append("                 ,   sum(case when isrequired = 'N' and isgraduated = 'Y' then 1 else 0 end  )   graduated_n_count                \n")
                 .append("                 ,   max(subjnumforcomplete)                                                     requried_n_count                 \n")
                 .append("                 ,   sum(case when isrequired = 'Y' and isgraduated = 'Y' then 1 else 0 end  )   graduated_y_count                \n")
                 .append("                 ,   sum(decode(isrequired, 'Y', 1, 0))                                          requried_y_count                 \n")
                 .append("             FROM    (                                                                                                            \n")
                 .append("                         SELECT  b.grcode                                                                                         \n")
                 .append("                             ,   a.coursenm                                                                                       \n")
                 .append("                             ,   b.coursecode                                                                                     \n")
                 .append("                             ,   b.courseyear                                                                                     \n")
                 .append("                             ,   b.courseseq                                                                                      \n")
                 .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                 .append("                             ,   d.userid                                                                                         \n")
                 .append("                             ,   d.name                                                                                           \n")
                 .append("                             ,   e.subj                                                                                           \n")
                 .append("                             ,   e.subjnm                                                                                         \n")
                 .append("                             ,   c.isrequired                                                                                     \n")
                 .append("                             ,   f.isgraduated                                                                                    \n")
                 .append("                             ,   f.score                                                                                          \n")
                 .append("                             ,   f.tstep                                                                                          \n")
                 .append("                             ,   f.mtest                                                                                          \n")
                 .append("                             ,   f.ftest                                                                                          \n")
                 .append("                             ,   f.report                                                                                         \n")
                 .append("                             ,   f.act                                                                                            \n")
                 .append("                             ,   f.etc1                                                                                           \n")
                 .append("                             ,   f.etc2                                                                                           \n")
                 .append("                             ,   f.avtstep                                                                                        \n")
                 .append("                             ,   f.avmtest                                                                                        \n")
                 .append("                             ,   f.avftest                                                                                        \n")
                 .append("                             ,   f.avreport                                                                                       \n")
                 .append("                             ,   f.avact                                                                                          \n")
                 .append("                             ,   f.avetc1                                                                                         \n")
                 .append("                             ,   f.avetc2                                                                                         \n")
                 .append("                             ,   f.htest                                                                                          \n")
                 .append("                             ,   f.avhtest                                                                                        \n")
                 .append("                             ,   h.isgraduated course_graduated                                                                   \n")
                 .append("                         from    tz_pfcourse         a                                                                            \n")
                 .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                 .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                 .append("                             ,   tz_member           d                                                                            \n")
                 .append("                             ,   tz_subj             e                                                                            \n")
                 .append("                             ,   tz_student            f                                                                            \n")
                 .append("                             ,   (                                                                                                \n")
                 .append("                                     select userid, subj, max(score) max_score                                                    \n")
                 .append("                                     from tz_student                                                                                \n")
                 .append("                                     group by userid, subj                                                                        \n")
                 .append("                                 )                   g                                                                            \n")
                 .append("                             ,        tz_pfstudent   h                                                                            \n")
                 .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                 .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                 .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                 .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                 .append("                         and     c.userid        = d.userid                                                                       \n")
                 .append("                         and     c.subj          = e.subj                                                                         \n")
                 .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                 .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                 .append("                         and     c.userid        = h.userid                                                                       \n")
                 .append("                         and     b.coursecode    = h.coursecode                                                                   \n")
                 .append("                         and     b.courseyear    = h.courseyear                                                                   \n")
                 .append("                         and     b.courseseq     = h.courseseq                                                                    \n")
                 .append("                         and     c.status        = 'RV'                                                                           \n")
                 .append("                         and     f.userid        = g.userid                                                                       \n")
                 .append("                         and     f.subj          = g.subj                                                                         \n")
                 .append("                         and     g.max_score     = f.score                                                                        \n")
                 .append("                         group by b.grcode                                                                                        \n")
                 .append("                             ,    b.coursecode                                                                                    \n")
                 .append("                             ,    b.courseyear                                                                                    \n")
                 .append("                             ,    b.courseseq                                                                                     \n")
                 .append("                             ,    a.coursenm                                                                                      \n")
                 .append("                             ,    d.userid                                                                                        \n")
                 .append("                             ,    d.name                                                                                          \n")
                 .append("                             ,    e.subj                                                                                          \n")
                 .append("                             ,    e.subjnm                                                                                        \n")
                 .append("                             ,    c.isrequired                                                                                    \n")
                 .append("                             ,    f.isgraduated                                                                                   \n")
                 .append("                             ,    f.score                                                                                         \n")
                 .append("                             ,    f.tstep                                                                                         \n")
                 .append("                             ,    f.mtest                                                                                         \n")
                 .append("                             ,    f.ftest                                                                                         \n")
                 .append("                             ,    f.report                                                                                        \n")
                 .append("                             ,    f.act                                                                                           \n")
                 .append("                             ,    f.etc1                                                                                          \n")
                 .append("                             ,    f.etc2                                                                                          \n")
                 .append("                             ,    f.avtstep                                                                                       \n")
                 .append("                             ,    f.avmtest                                                                                       \n")
                 .append("                             ,    f.avftest                                                                                       \n")
                 .append("                             ,    f.avreport                                                                                      \n")
                 .append("                             ,    f.avact                                                                                         \n")
                 .append("                             ,    f.avetc1                                                                                        \n")
                 .append("                             ,    f.avetc2                                                                                        \n")
                 .append("                             ,    f.htest                                                                                         \n")
                 .append("                             ,    f.avhtest                                                                                       \n")
                 .append("                             ,    h.isgraduated                                                                                   \n")
                 .append("                         UNION                                                                                                    \n")
                 .append("                         select  b.grcode                                                                                         \n")
                 .append("                             ,   a.coursenm                                                                                       \n")
                 .append("                             ,   b.coursecode                                                                                     \n")
                 .append("                             ,   b.courseyear                                                                                     \n")
                 .append("                             ,   b.courseseq                                                                                      \n")
                 .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                 .append("                             ,   d.userid                                                                                         \n")
                 .append("                             ,   d.name                                                                                           \n")
                 .append("                             ,   e.subj                                                                                           \n")
                 .append("                             ,   e.subjnm                                                                                         \n")
                 .append("                             ,   c.isrequired                                                                                     \n")
                 .append("                             ,   f.isgraduated                                                                                    \n")
                 .append("                             ,   f.score                                                                                          \n")
                 .append("                             ,   f.tstep                                                                                          \n")
                 .append("                             ,   f.mtest                                                                                          \n")
                 .append("                             ,   f.ftest                                                                                          \n")
                 .append("                             ,   f.report                                                                                         \n")
                 .append("                             ,   f.act                                                                                            \n")
                 .append("                             ,   f.etc1                                                                                           \n")
                 .append("                             ,   f.etc2                                                                                           \n")
                 .append("                             ,   f.avtstep                                                                                        \n")
                 .append("                             ,   f.avmtest                                                                                        \n")
                 .append("                             ,   f.avftest                                                                                        \n")
                 .append("                             ,   f.avreport                                                                                       \n")
                 .append("                             ,   f.avact                                                                                          \n")
                 .append("                             ,   f.avetc1                                                                                         \n")
                 .append("                             ,   f.avetc2                                                                                         \n")
                 .append("                             ,   f.htest                                                                                          \n")
                 .append("                             ,   f.avhtest                                                                                        \n")
                 .append("                             ,   g.isgraduated course_graduated                                                                   \n")
                 .append("                         from    tz_pfcourse         a                                                                            \n")
                 .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                 .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                 .append("                             ,   tz_member           d                                                                            \n")
                 .append("                             ,   tz_subj             e                                                                            \n")
                 .append("                             ,   tz_student            f                                                                            \n")
                 .append("                             ,   tz_pfstudent        g                                                                            \n")
                 .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                 .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                 .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                 .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                 .append("                         and     c.userid        = d.userid                                                                       \n")
                 .append("                         and     c.subj          = e.subj                                                                         \n")
                 .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                 .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                 .append("                         and     c.userid        = g.userid                                                                       \n")
                 .append("                         and     b.coursecode    = g.coursecode                                                                   \n")
                 .append("                         and     b.courseyear    = g.courseyear                                                                   \n")
                 .append("                         and     b.courseseq     = g.courseseq                                                                    \n")
                 .append("                         and     c.status in ( 'RE', 'FE' )                                                                       \n")
                 .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                 .append("                         and     c.year          = f.year(+)                                                                      \n")
                 .append("                         and     c.subjseq       = f.subjseq(+)                                                                   \n")
                 .append("                         group by b.grcode                                                                                        \n")
                 .append("                             , b.coursecode                                                                                       \n")
                 .append("                             , b.courseyear                                                                                       \n")
                 .append("                             , b.courseseq                                                                                        \n")
                 .append("                             , a.coursenm                                                                                         \n")
                 .append("                             , d.userid                                                                                           \n")
                 .append("                             , d.name                                                                                             \n")
                 .append("                             , e.subj                                                                                             \n")
                 .append("                             , e.subjnm                                                                                           \n")
                 .append("                             , c.isrequired                                                                                       \n")
                 .append("                             , f.isgraduated                                                                                      \n")
                 .append("                             , f.score                                                                                            \n")
                 .append("                             , f.tstep                                                                                            \n")
                 .append("                             , f.mtest                                                                                            \n")
                 .append("                             , f.ftest                                                                                            \n")
                 .append("                             , f.report                                                                                           \n")
                 .append("                             , f.act                                                                                              \n")
                 .append("                             , f.etc1                                                                                             \n")
                 .append("                             , f.etc2                                                                                             \n")
                 .append("                             , f.avtstep                                                                                          \n")
                 .append("                             , f.avmtest                                                                                          \n")
                 .append("                             , f.avftest                                                                                          \n")
                 .append("                             , f.avreport                                                                                         \n")
                 .append("                             , f.avact                                                                                            \n")
                 .append("                             , f.avetc1                                                                                           \n")
                 .append("                             , f.avetc2                                                                                           \n")
                 .append("                             , f.htest                                                                                            \n")
                 .append("                             , f.avhtest                                                                                          \n")
                 .append("                             , g.isgraduated                                                                                      \n")
                 .append("                         order by name, isrequired desc                                                                           \n")
                 .append("                     )                                                                                                            \n")
                 .append("             group by grcode, courseyear, courseseq, coursecode, userid                                                           \n")
                 .append("         )                b                                                                                                       \n");
            
            if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                sbSQL.append("  ,  vz_orgmember     c                                                                                                       \n");
            }
            
            sbSQL.append(" WHERE   a.grcode        = " + StringManager.makeSQL(v_grcode         ) + "                                                       \n")
                 .append(" and     a.courseyear    = " + StringManager.makeSQL(v_courseyear     ) + "                                                       \n");

             if ( !v_coursecode.equals("") ) { 
                 sbSQL.append(" and a.coursecode    = " + StringManager.makeSQL(v_coursecode    ) + "   \n");
             }
             
             if ( !v_courseseq.equals("") ) { 
                 sbSQL.append(" and a.courseseq     = " + StringManager.makeSQL(v_courseseq     ) + "   \n");
             }
             
             
             if ( v_isgraduated.equals("Y") ) {
                 sbSQL.append(" and a.course_graduated   = " + StringManager.makeSQL(v_isgraduated   ) + "   \n");
             } else if ( v_isgraduated.equals("N") ) {
                 sbSQL.append(" and ( a.course_graduated   IS NULL OR a.course_graduated = " + StringManager.makeSQL(v_isgraduated   ) + " ) \n");
             }
             
             if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                 sbSQL.append(" AND     c.orgcode       = " + v_orgcode + "                             \n")
                      .append(" and     a.userid        = c.userid                                      \n");
             }
             
             sbSQL.append(" and a.userid = b.userid                                                                                                         \n")
                  .append(" ORDER BY a.grcode, a.courseyear, a.coursecode, a.courseseq, a.name, a.userid, a.isrequired desc, a.subj                         \n");
                 
            System.out.println("SQL.toString : \n" + sbSQL.toString() );
            
            ls = connMgr.executeQuery(sbSQL.toString());
    
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
    }

     
     /**
     과정별 교육실적
     @param box      receive from the form object and session
     @return ArrayList
     */
      public ArrayList perform_03_01_02(RequestBox box) throws Exception {
          DBConnectionManager connMgr         = null;
          ListSet             ls              = null;
          ArrayList           list            = null;
          StringBuffer        sbSQL           = new StringBuffer("");
           
          DataBox             dbox            = null;
           
          String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
          String              v_courseyear    = box.getStringDefault  ( "p_courseyear"   , ""    ); // 년도
          String              v_courseseq     = box.getStringDefault  ( "p_courseseq"    , ""    ); // 과목 기수
          String              v_coursecode    = box.getStringDefault  ( "p_coursecode"   , ""    ); // 과목&코스
          String              s_gadmin        = box.getSession        ( "gadmin"                 );
          String              v_branch        = box.getStringDefault  ( "p_branch"       , ""    );
          String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
          String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
           
         String              v_orderColumn   = box.getString( "p_orderColumn"  );  // 정렬할 컬럼명
         String              v_orderType     = box.getString( "p_orderType"    );  // 정렬할 순서
         
         try { 
             connMgr = new DBConnectionManager();
             list   = new ArrayList();    

             sbSQL.append(" SELECT  a.grcode                                                                                                                   \n")                                       
                  .append("     ,   a.coursenm                                                                                                                 \n")
                  .append("     ,   a.coursecode                                                                                                               \n")
                  .append("     ,   a.courseyear                                                                                                               \n")
                  .append("     ,   a.courseseq                                                                                                                \n")
                  .append("     ,   a.userid                                                                                                                 \n")
                  .append("     ,   a.name                                                                                                                     \n")
                  .append("     ,   a.subj                                                                                                                     \n")
                  .append("     ,   a.subjnm                                                                                                                   \n")
                  .append("     ,   a.isrequired                                                                                                               \n")
                  .append("     ,   a.isgraduated                                                                                                              \n")
                  .append("     ,   a.score                                                                                                                    \n")
                  .append("     ,   a.tstep                                                                                                                    \n")
                  .append("     ,   a.mtest                                                                                                                    \n")
                  .append("     ,   a.ftest                                                                                                                    \n")
                  .append("     ,   a.report                                                                                                                   \n")
                  .append("     ,   a.act                                                                                                                      \n")
                  .append("     ,   a.etc1                                                                                                                     \n")
                  .append("     ,   a.etc2                                                                                                                     \n")
                  .append("     ,   a.avtstep                                                                                                                  \n")
                  .append("     ,   a.avmtest                                                                                                                  \n")
                  .append("     ,   a.avftest                                                                                                                  \n")
                  .append("     ,   a.avreport                                                                                                                 \n")
                  .append("     ,   a.avact                                                                                                                    \n")
                  .append("     ,   a.avetc1                                                                                                                   \n")
                  .append("     ,   a.avetc2                                                                                                                   \n")
                  .append("     ,   a.htest                                                                                                                    \n")
                  .append("     ,   a.avhtest                                                                                                                  \n")
                  .append("     ,   a.course_graduated                                                                                                         \n")
                  .append("     ,   b.total                                                                                                                  \n")
                  .append("     ,   b.requried_n_count                                                                                                       \n")
                  .append("     ,   b.graduated_n_count                                                                                                      \n")
                  .append("     ,   b.requried_y_count                                                                                                       \n")
                  .append("     ,   b.graduated_y_count                                                                                                      \n")
                  .append(" FROM    (                                                                                                                        \n")
                  .append("          select  b.grcode                                                                                                        \n")
                  .append("                 ,   a.coursenm                                                                                                   \n")
                  .append("                 ,   b.coursecode                                                                                                 \n")
                  .append("                 ,   b.courseyear                                                                                                 \n")
                  .append("                 ,   b.courseseq                                                                                                  \n")
                  .append("                 ,   d.userid                                                                                                     \n")
                  .append("                 ,   d.name                                                                                                       \n")
                  .append("                 ,   e.subj                                                                                                       \n")
                  .append("                 ,   e.subjnm                                                                                                     \n")
                  .append("                 ,   c.isrequired                                                                                                 \n")
                  .append("                 ,   f.isgraduated                                                                                                \n")
                  .append("                 ,   f.score                                                                                                      \n")
                  .append("                 ,   f.tstep                                                                                                      \n")
                  .append("                 ,   f.mtest                                                                                                      \n")
                  .append("                 ,   f.ftest                                                                                                      \n")
                  .append("                 ,   f.report                                                                                                     \n")
                  .append("                 ,   f.act                                                                                                        \n")
                  .append("                 ,   f.etc1                                                                                                       \n")
                  .append("                 ,   f.etc2                                                                                                       \n")
                  .append("                 ,   f.avtstep                                                                                                    \n")
                  .append("                 ,   f.avmtest                                                                                                    \n")
                  .append("                 ,   f.avftest                                                                                                    \n")
                  .append("                 ,   f.avreport                                                                                                   \n")
                  .append("                 ,   f.avact                                                                                                      \n")
                  .append("                 ,   f.avetc1                                                                                                     \n")
                  .append("                 ,   f.avetc2                                                                                                     \n")
                  .append("                 ,   f.htest                                                                                                      \n")
                  .append("                 ,   f.avhtest                                                                                                    \n")
                  .append("                 ,   h.isgraduated       course_graduated                                                                         \n")
                  .append("             FROM    tz_pfcourse         a                                                                                        \n")
                  .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                  .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                  .append("                 ,   tz_member           d                                                                                        \n")
                  .append("                 ,   tz_subj             e                                                                                        \n")
                  .append("                 ,   tz_student          f                                                                                        \n")
                  .append("                 ,   (                                                                                                            \n")
                  .append("                         select  i.userid, i.subj, max(i.score) max_score                                                         \n")
                  .append("                         from    tz_student  i                                                                                    \n")
                  .append("                             ,   tz_subjseq  j                                                                                    \n")
                  .append("                         where   i.subj      = j.subj                                                                             \n")
                  .append("                         and     i.subjseq   = j.subjseq                                                                          \n")
                  .append("                         and     i.year      = j.year                                                                             \n")
                  .append("                         and     j.isblended = 'N'                                                                                \n")
                  .append("                         and     j.isexpert  = 'N'                                                                                \n")
                  .append("                         group by i.userid, i.subj                                                                                \n")
                  .append("                     )                   g                                                                                        \n")
                  .append("                 ,   tz_pfstudent        h                                                                                        \n")
                  .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                  .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                  .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                  .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                  .append("             AND     c.userid        = d.userid                                                                                   \n")
                  .append("             AND     c.subj          = e.subj                                                                                     \n")
                  .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                  .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                  .append("             AND     c.userid        = h.userid                                                                                   \n")
                  .append("             AND     b.coursecode    = h.coursecode                                                                               \n")
                  .append("             AND     b.courseyear    = h.courseyear                                                                               \n")
                  .append("             AND     b.courseseq     = h.courseseq                                                                                \n")
                  .append("             AND     c.status        = 'RV'                                                                                       \n")
                  .append("             AND     f.userid        = g.userid                                                                                   \n")
                  .append("             AND     f.subj          = g.subj                                                                                     \n")
                  .append("             AND     g.max_score     = f.score                                                                                    \n")
                  .append("             GROUP BY b.grcode                                                                                                    \n")
                  .append("                 ,    b.coursecode                                                                                                \n")
                  .append("                 ,    b.courseyear                                                                                                \n")
                  .append("                 ,    b.courseseq                                                                                                 \n")
                  .append("                 ,    a.coursenm                                                                                                  \n")
                  .append("                 ,    d.userid                                                                                                    \n")
                  .append("                 ,    d.name                                                                                                      \n")
                  .append("                 ,    e.subj                                                                                                      \n")
                  .append("                 ,    e.subjnm                                                                                                    \n")
                  .append("                 ,    c.isrequired                                                                                                \n")
                  .append("                 ,    f.isgraduated                                                                                               \n")
                  .append("                 ,    f.score                                                                                                     \n")
                  .append("                 ,    f.tstep                                                                                                     \n")
                  .append("                 ,    f.mtest                                                                                                     \n")
                  .append("                 ,    f.ftest                                                                                                     \n")
                  .append("                 ,    f.report                                                                                                    \n")
                  .append("                 ,    f.act                                                                                                       \n")
                  .append("                 ,    f.etc1                                                                                                      \n")
                  .append("                 ,    f.etc2                                                                                                      \n")
                  .append("                 ,    f.avtstep                                                                                                   \n")
                  .append("                 ,    f.avmtest                                                                                                   \n")
                  .append("                 ,    f.avftest                                                                                                   \n")
                  .append("                 ,    f.avreport                                                                                                  \n")
                  .append("                 ,    f.avact                                                                                                     \n")
                  .append("                 ,    f.avetc1                                                                                                    \n")
                  .append("                 ,    f.avetc2                                                                                                    \n")
                  .append("                 ,    f.htest                                                                                                     \n")
                  .append("                 ,    f.avhtest                                                                                                   \n")
                  .append("                 ,    h.isgraduated                                                                                               \n")
                  .append("             UNION                                                                                                                \n")
                  .append("             SELECT  b.grcode                                                                                                     \n")
                  .append("                 ,   a.coursenm                                                                                                   \n")
                  .append("                 ,   b.coursecode                                                                                                 \n")
                  .append("                 ,   b.courseyear                                                                                                 \n")
                  .append("                 ,   b.courseseq                                                                                                  \n")
                  .append("                 ,   d.userid                                                                                                     \n")
                  .append("                 ,   d.name                                                                                                       \n")
                  .append("                 ,   e.subj                                                                                                       \n")
                  .append("                 ,   e.subjnm                                                                                                     \n")
                  .append("                 ,   c.isrequired                                                                                                 \n")
                  .append("                 ,   f.isgraduated                                                                                                \n")
                  .append("                 ,   f.score                                                                                                      \n")
                  .append("                 ,   f.tstep                                                                                                      \n")
                  .append("                 ,   f.mtest                                                                                                      \n")
                  .append("                 ,   f.ftest                                                                                                      \n")
                  .append("                 ,   f.report                                                                                                     \n")
                  .append("                 ,   f.act                                                                                                        \n")
                  .append("                 ,   f.etc1                                                                                                       \n")
                  .append("                 ,   f.etc2                                                                                                       \n")
                  .append("                 ,   f.avtstep                                                                                                    \n")
                  .append("                 ,   f.avmtest                                                                                                    \n")
                  .append("                 ,   f.avftest                                                                                                    \n")
                  .append("                 ,   f.avreport                                                                                                   \n")
                  .append("                 ,   f.avact                                                                                                      \n")
                  .append("                 ,   f.avetc1                                                                                                     \n")
                  .append("                 ,   f.avetc2                                                                                                     \n")
                  .append("                 ,   f.htest                                                                                                      \n")
                  .append("                 ,   f.avhtest                                                                                                    \n")
                  .append("                 ,   g.isgraduated       course_graduated                                                                         \n")
                  .append("             FROM    tz_pfcourse         a                                                                                        \n")
                  .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                  .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                  .append("                 ,   tz_member           d                                                                                        \n")
                  .append("                 ,   tz_subj             e                                                                                        \n")
                  .append("                 ,   tz_student            f                                                                                        \n")
                  .append("                 ,   tz_pfstudent        g                                                                                        \n")
                  .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                  .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                  .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                  .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                  .append("             AND     c.userid        = d.userid                                                                                   \n")
                  .append("             AND     c.subj          = e.subj                                                                                     \n")
                  .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                  .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                  .append("             AND     c.userid        = g.userid                                                                                   \n")
                  .append("             AND     b.coursecode    = g.coursecode                                                                               \n")
                  .append("             AND     b.courseyear    = g.courseyear                                                                               \n")
                  .append("             AND     b.courseseq     = g.courseseq                                                                                \n")
                  .append("             AND     c.status in ( 'RE', 'FE' )                                                                                   \n")
                  .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                  .append("             AND     c.year          = f.year(+)                                                                                  \n")
                  .append("             AND     c.subjseq       = f.subjseq(+)                                                                               \n")
                  .append("             GROUP BY b.grcode                                                                                                    \n")
                  .append("                 ,    b.coursecode                                                                                                \n")
                  .append("                 ,    b.courseyear                                                                                                \n")
                  .append("                 ,    b.courseseq                                                                                                 \n")
                  .append("                 ,    a.coursenm                                                                                                  \n")
                  .append("                 ,    d.userid                                                                                                    \n")
                  .append("                 ,    d.name                                                                                                      \n")
                  .append("                 ,    e.subj                                                                                                      \n")
                  .append("                 ,    e.subjnm                                                                                                    \n")
                  .append("                 ,    c.isrequired                                                                                                \n")
                  .append("                 ,    f.isgraduated                                                                                               \n")
                  .append("                 ,    f.score                                                                                                     \n")
                  .append("                 ,    f.tstep                                                                                                     \n")
                  .append("                 ,    f.mtest                                                                                                     \n")
                  .append("                 ,    f.ftest                                                                                                     \n")
                  .append("                 ,    f.report                                                                                                    \n")
                  .append("                 ,    f.act                                                                                                       \n")
                  .append("                 ,    f.etc1                                                                                                      \n")
                  .append("                 ,    f.etc2                                                                                                      \n")
                  .append("                 ,    f.avtstep                                                                                                   \n")
                  .append("                 ,    f.avmtest                                                                                                   \n")
                  .append("                 ,    f.avftest                                                                                                   \n")
                  .append("                 ,    f.avreport                                                                                                  \n")
                  .append("                 ,    f.avact                                                                                                     \n")
                  .append("                 ,    f.avetc1                                                                                                    \n")
                  .append("                 ,    f.avetc2                                                                                                    \n")
                  .append("                 ,    f.htest                                                                                                     \n")
                  .append("                 ,    f.avhtest                                                                                                   \n")
                  .append("                 ,    g.isgraduated                                                                                               \n")
                  .append("             ORDER BY name, isrequired desc                                                                                       \n")
                  .append("         )           a                                                                                                            \n")
                  .append("     ,   (                                                                                                                        \n")
                  .append("             SELECT  grcode                                                                                                       \n")
                  .append("                 ,   courseyear                                                                                                   \n")
                  .append("                 ,   courseseq                                                                                                    \n")
                  .append("                 ,   coursecode                                                                                                   \n")
                  .append("                 ,   userid                                                                                                       \n")
                  .append("                 ,   avg(score                                                               )   total                            \n")
                  .append("                 ,   sum(case when isrequired = 'N' and isgraduated = 'Y' then 1 else 0 end  )   graduated_n_count                \n")
                  .append("                 ,   max(subjnumforcomplete)                                                     requried_n_count                 \n")
                  .append("                 ,   sum(case when isrequired = 'Y' and isgraduated = 'Y' then 1 else 0 end  )   graduated_y_count                \n")
                  .append("                 ,   sum(decode(isrequired, 'Y', 1, 0))                                          requried_y_count                 \n")
                  .append("             FROM    (                                                                                                            \n")
                  .append("                         SELECT  b.grcode                                                                                         \n")
                  .append("                             ,   a.coursenm                                                                                       \n")
                  .append("                             ,   b.coursecode                                                                                     \n")
                  .append("                             ,   b.courseyear                                                                                     \n")
                  .append("                             ,   b.courseseq                                                                                      \n")
                  .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                  .append("                             ,   d.userid                                                                                         \n")
                  .append("                             ,   d.name                                                                                           \n")
                  .append("                             ,   e.subj                                                                                           \n")
                  .append("                             ,   e.subjnm                                                                                         \n")
                  .append("                             ,   c.isrequired                                                                                     \n")
                  .append("                             ,   f.isgraduated                                                                                    \n")
                  .append("                             ,   f.score                                                                                          \n")
                  .append("                             ,   f.tstep                                                                                          \n")
                  .append("                             ,   f.mtest                                                                                          \n")
                  .append("                             ,   f.ftest                                                                                          \n")
                  .append("                             ,   f.report                                                                                         \n")
                  .append("                             ,   f.act                                                                                            \n")
                  .append("                             ,   f.etc1                                                                                           \n")
                  .append("                             ,   f.etc2                                                                                           \n")
                  .append("                             ,   f.avtstep                                                                                        \n")
                  .append("                             ,   f.avmtest                                                                                        \n")
                  .append("                             ,   f.avftest                                                                                        \n")
                  .append("                             ,   f.avreport                                                                                       \n")
                  .append("                             ,   f.avact                                                                                          \n")
                  .append("                             ,   f.avetc1                                                                                         \n")
                  .append("                             ,   f.avetc2                                                                                         \n")
                  .append("                             ,   f.htest                                                                                          \n")
                  .append("                             ,   f.avhtest                                                                                        \n")
                  .append("                             ,   h.isgraduated course_graduated                                                                   \n")
                  .append("                         from    tz_pfcourse         a                                                                            \n")
                  .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                  .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                  .append("                             ,   tz_member           d                                                                            \n")
                  .append("                             ,   tz_subj             e                                                                            \n")
                  .append("                             ,   tz_student            f                                                                            \n")
                  .append("                             ,   (                                                                                                \n")
                  .append("                                     select userid, subj, max(score) max_score                                                    \n")
                  .append("                                     from tz_student                                                                                \n")
                  .append("                                     group by userid, subj                                                                        \n")
                  .append("                                 )                   g                                                                            \n")
                  .append("                             ,        tz_pfstudent   h                                                                            \n")
                  .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                  .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                  .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                  .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                  .append("                         and     c.userid        = d.userid                                                                       \n")
                  .append("                         and     c.subj          = e.subj                                                                         \n")
                  .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                  .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                  .append("                         and     c.userid        = h.userid                                                                       \n")
                  .append("                         and     b.coursecode    = h.coursecode                                                                   \n")
                  .append("                         and     b.courseyear    = h.courseyear                                                                   \n")
                  .append("                         and     b.courseseq     = h.courseseq                                                                    \n")
                  .append("                         and     c.status        = 'RV'                                                                           \n")
                  .append("                         and     f.userid        = g.userid                                                                       \n")
                  .append("                         and     f.subj          = g.subj                                                                         \n")
                  .append("                         and     g.max_score     = f.score                                                                        \n")
                  .append("                         group by b.grcode                                                                                        \n")
                  .append("                             ,    b.coursecode                                                                                    \n")
                  .append("                             ,    b.courseyear                                                                                    \n")
                  .append("                             ,    b.courseseq                                                                                     \n")
                  .append("                             ,    a.coursenm                                                                                      \n")
                  .append("                             ,    d.userid                                                                                        \n")
                  .append("                             ,    d.name                                                                                          \n")
                  .append("                             ,    e.subj                                                                                          \n")
                  .append("                             ,    e.subjnm                                                                                        \n")
                  .append("                             ,    c.isrequired                                                                                    \n")
                  .append("                             ,    f.isgraduated                                                                                   \n")
                  .append("                             ,    f.score                                                                                         \n")
                  .append("                             ,    f.tstep                                                                                         \n")
                  .append("                             ,    f.mtest                                                                                         \n")
                  .append("                             ,    f.ftest                                                                                         \n")
                  .append("                             ,    f.report                                                                                        \n")
                  .append("                             ,    f.act                                                                                           \n")
                  .append("                             ,    f.etc1                                                                                          \n")
                  .append("                             ,    f.etc2                                                                                          \n")
                  .append("                             ,    f.avtstep                                                                                       \n")
                  .append("                             ,    f.avmtest                                                                                       \n")
                  .append("                             ,    f.avftest                                                                                       \n")
                  .append("                             ,    f.avreport                                                                                      \n")
                  .append("                             ,    f.avact                                                                                         \n")
                  .append("                             ,    f.avetc1                                                                                        \n")
                  .append("                             ,    f.avetc2                                                                                        \n")
                  .append("                             ,    f.htest                                                                                         \n")
                  .append("                             ,    f.avhtest                                                                                       \n")
                  .append("                             ,    h.isgraduated                                                                                   \n")
                  .append("                         UNION                                                                                                    \n")
                  .append("                         select  b.grcode                                                                                         \n")
                  .append("                             ,   a.coursenm                                                                                       \n")
                  .append("                             ,   b.coursecode                                                                                     \n")
                  .append("                             ,   b.courseyear                                                                                     \n")
                  .append("                             ,   b.courseseq                                                                                      \n")
                  .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                  .append("                             ,   d.userid                                                                                         \n")
                  .append("                             ,   d.name                                                                                           \n")
                  .append("                             ,   e.subj                                                                                           \n")
                  .append("                             ,   e.subjnm                                                                                         \n")
                  .append("                             ,   c.isrequired                                                                                     \n")
                  .append("                             ,   f.isgraduated                                                                                    \n")
                  .append("                             ,   f.score                                                                                          \n")
                  .append("                             ,   f.tstep                                                                                          \n")
                  .append("                             ,   f.mtest                                                                                          \n")
                  .append("                             ,   f.ftest                                                                                          \n")
                  .append("                             ,   f.report                                                                                         \n")
                  .append("                             ,   f.act                                                                                            \n")
                  .append("                             ,   f.etc1                                                                                           \n")
                  .append("                             ,   f.etc2                                                                                           \n")
                  .append("                             ,   f.avtstep                                                                                        \n")
                  .append("                             ,   f.avmtest                                                                                        \n")
                  .append("                             ,   f.avftest                                                                                        \n")
                  .append("                             ,   f.avreport                                                                                       \n")
                  .append("                             ,   f.avact                                                                                          \n")
                  .append("                             ,   f.avetc1                                                                                         \n")
                  .append("                             ,   f.avetc2                                                                                         \n")
                  .append("                             ,   f.htest                                                                                          \n")
                  .append("                             ,   f.avhtest                                                                                        \n")
                  .append("                             ,   g.isgraduated course_graduated                                                                   \n")
                  .append("                         from    tz_pfcourse         a                                                                            \n")
                  .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                  .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                  .append("                             ,   tz_member           d                                                                            \n")
                  .append("                             ,   tz_subj             e                                                                            \n")
                  .append("                             ,   tz_student            f                                                                            \n")
                  .append("                             ,   tz_pfstudent        g                                                                            \n")
                  .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                  .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                  .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                  .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                  .append("                         and     c.userid        = d.userid                                                                       \n")
                  .append("                         and     c.subj          = e.subj                                                                         \n")
                  .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                  .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                  .append("                         and     c.userid        = g.userid                                                                       \n")
                  .append("                         and     b.coursecode    = g.coursecode                                                                   \n")
                  .append("                         and     b.courseyear    = g.courseyear                                                                   \n")
                  .append("                         and     b.courseseq     = g.courseseq                                                                    \n")
                  .append("                         and     c.status in ( 'RE', 'FE' )                                                                       \n")
                  .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                  .append("                         and     c.year          = f.year(+)                                                                      \n")
                  .append("                         and     c.subjseq       = f.subjseq(+)                                                                   \n")
                  .append("                         group by b.grcode                                                                                        \n")
                  .append("                             , b.coursecode                                                                                       \n")
                  .append("                             , b.courseyear                                                                                       \n")
                  .append("                             , b.courseseq                                                                                        \n")
                  .append("                             , a.coursenm                                                                                         \n")
                  .append("                             , d.userid                                                                                           \n")
                  .append("                             , d.name                                                                                             \n")
                  .append("                             , e.subj                                                                                             \n")
                  .append("                             , e.subjnm                                                                                           \n")
                  .append("                             , c.isrequired                                                                                       \n")
                  .append("                             , f.isgraduated                                                                                      \n")
                  .append("                             , f.score                                                                                            \n")
                  .append("                             , f.tstep                                                                                            \n")
                  .append("                             , f.mtest                                                                                            \n")
                  .append("                             , f.ftest                                                                                            \n")
                  .append("                             , f.report                                                                                           \n")
                  .append("                             , f.act                                                                                              \n")
                  .append("                             , f.etc1                                                                                             \n")
                  .append("                             , f.etc2                                                                                             \n")
                  .append("                             , f.avtstep                                                                                          \n")
                  .append("                             , f.avmtest                                                                                          \n")
                  .append("                             , f.avftest                                                                                          \n")
                  .append("                             , f.avreport                                                                                         \n")
                  .append("                             , f.avact                                                                                            \n")
                  .append("                             , f.avetc1                                                                                           \n")
                  .append("                             , f.avetc2                                                                                           \n")
                  .append("                             , f.htest                                                                                            \n")
                  .append("                             , f.avhtest                                                                                          \n")
                  .append("                             , g.isgraduated                                                                                      \n")
                  .append("                         order by name, isrequired desc                                                                           \n")
                  .append("                     )                                                                                                            \n")
                  .append("             group by grcode, courseyear, courseseq, coursecode, userid                                                           \n")
                  .append("         )                b                                                                                                       \n");
             
             if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                 sbSQL.append("  ,  vz_orgmember     c                                                                                                       \n");
             }
             
             sbSQL.append(" WHERE   a.grcode        = " + StringManager.makeSQL(v_grcode         ) + "                                                       \n")
                  .append(" and     a.courseyear    = " + StringManager.makeSQL(v_courseyear     ) + "                                                       \n");

              if ( !v_coursecode.equals("") ) { 
                  sbSQL.append(" and a.coursecode    = " + StringManager.makeSQL(v_coursecode    ) + "   \n");
              }
              
              if ( !v_courseseq.equals("") ) { 
                  sbSQL.append(" and a.courseseq     = " + StringManager.makeSQL(v_courseseq     ) + "   \n");
              }
              
              
              if ( v_isgraduated.equals("Y") ) {
                  sbSQL.append(" and a.course_graduated   = " + StringManager.makeSQL(v_isgraduated   ) + "   \n");
              } else if ( v_isgraduated.equals("N") ) {
                  sbSQL.append(" and ( a.course_graduated   IS NULL OR a.course_graduated = " + StringManager.makeSQL(v_isgraduated   ) + " ) \n");
              }
              
              if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                  sbSQL.append(" AND     c.orgcode       = " + v_orgcode + "                             \n")
                       .append(" and     a.userid        = c.userid                                      \n");
              }
              
              sbSQL.append(" and a.userid = b.userid                                                                                                         \n")
                   .append(" ORDER BY a.grcode, a.courseyear, a.coursecode, a.courseseq, a.name, a.userid, a.isrequired desc, a.subj                         \n");
                  
             System.out.println("SQL.toString : \n" + sbSQL.toString() );
             
             ls = connMgr.executeQuery(sbSQL.toString());
     
             while ( ls.next() ) { 
                 dbox = ls.getDataBox();
                 list.add(dbox);
             }
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
             throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         
         return list;
     }
     
      /**
      과정현황
      @param box      receive from the form object and session
      @return ArrayList
      */
       public ArrayList perform_03_01_03(RequestBox box) throws Exception { 
           DBConnectionManager connMgr         = null;
           ListSet             ls              = null;
           ArrayList           list            = null;
           StringBuffer        sbSQL           = new StringBuffer("");
           
           DataBox             dbox            = null;
           
           String              v_Bcourse       = ""; // 이전코스
           String              v_course        = ""; // 현재코스
           String              v_Bcourseseq    = ""; // 이전코스기수
           String              v_courseseq     = ""; // 현재코스기수
           
           String              ss_grcode       = box.getStringDefault  ( "s_grcode"        , "ALL" );  // 교육그룹
           String              ss_pfcourseyear = box.getStringDefault  ( "s_pfcourseyear"  , "ALL" );  // 년도
           String              ss_pfcourseseq  = box.getStringDefault  ( "s_pfcourseseq"   , "ALL" );  // 과목 기수
           String              ss_pfcourse     = box.getStringDefault  ( "s_pfcourse"      , "ALL" );  // 과목&코스
           String              ss_action       = box.getString         ( "s_action"                );
           String              v_orgcode       = box.getStringDefault  ( "p_orgcode"       , "ALL" );  // 조직코드
           String              c_sido          = box.getString         ( "c_sido"                  );
           String              c_gugun         = box.getString         ( "c_gugun"                 );
           String              c_gender        = box.getString         ( "c_gender"                );
           String              c_achievement   = box.getString         ( "c_achievement"           );
           String              c_age           = box.getString         ( "c_age"                   );
           String              c_gubun           = box.getString       ( "c_gubun"                 );
           
           String              v_iscomplete    = box.getString         ( "p_iscomplete"            );
           
           String              v_orderColumn   = box.getString( "p_orderColumn"  );  // 정렬할 컬럼명
           String              v_orderType     = box.getString( "p_orderType"    );  // 정렬할 순서
           
           try { 
               connMgr = new DBConnectionManager();
               list   = new ArrayList();    

               sbSQL.append(" SELECT  a.grcode                                                                                                                   \n")                                       
                    .append("     ,   a.coursenm                                                                                                                 \n")
                    .append("     ,   a.coursecode                                                                                                               \n")
                    .append("     ,   a.courseyear                                                                                                               \n")
                    .append("     ,   a.courseseq                                                                                                                \n")
                    .append("     ,   a.userid                                                                                                                 \n")
                    .append("     ,   a.name                                                                                                                     \n")
                    .append("     ,   a.subj                                                                                                                     \n")
                    .append("     ,   a.subjnm                                                                                                                   \n")
                    .append("     ,   a.isrequired                                                                                                               \n")
                    .append("     ,   a.isgraduated                                                                                                              \n")
                    .append("     ,   a.score                                                                                                                    \n")
                    .append("     ,   a.tstep                                                                                                                    \n")
                    .append("     ,   a.mtest                                                                                                                    \n")
                    .append("     ,   a.ftest                                                                                                                    \n")
                    .append("     ,   a.report                                                                                                                   \n")
                    .append("     ,   a.act                                                                                                                      \n")
                    .append("     ,   a.etc1                                                                                                                     \n")
                    .append("     ,   a.etc2                                                                                                                     \n")
                    .append("     ,   a.avtstep                                                                                                                  \n")
                    .append("     ,   a.avmtest                                                                                                                  \n")
                    .append("     ,   a.avftest                                                                                                                  \n")
                    .append("     ,   a.avreport                                                                                                                 \n")
                    .append("     ,   a.avact                                                                                                                    \n")
                    .append("     ,   a.avetc1                                                                                                                   \n")
                    .append("     ,   a.avetc2                                                                                                                   \n")
                    .append("     ,   a.htest                                                                                                                    \n")
                    .append("     ,   a.avhtest                                                                                                                  \n")
                    .append("     ,   a.course_graduated                                                                                                         \n")
                    .append("     ,   b.total                                                                                                                  \n")
                    .append("     ,   b.requried_n_count                                                                                                       \n")
                    .append("     ,   b.graduated_n_count                                                                                                      \n")
                    .append("     ,   b.requried_y_count                                                                                                       \n")
                    .append("     ,   b.graduated_y_count                                                                                                      \n")
                    .append(" FROM    (                                                                                                                        \n")
                    .append("          select  b.grcode                                                                                                        \n")
                    .append("                 ,   a.coursenm                                                                                                   \n")
                    .append("                 ,   b.coursecode                                                                                                 \n")
                    .append("                 ,   b.courseyear                                                                                                 \n")
                    .append("                 ,   b.courseseq                                                                                                  \n")
                    .append("                 ,   d.userid                                                                                                     \n")
                    .append("                 ,   d.name                                                                                                       \n")
                    .append("                 ,   e.subj                                                                                                       \n")
                    .append("                 ,   e.subjnm                                                                                                     \n")
                    .append("                 ,   c.isrequired                                                                                                 \n")
                    .append("                 ,   f.isgraduated                                                                                                \n")
                    .append("                 ,   f.score                                                                                                      \n")
                    .append("                 ,   f.tstep                                                                                                      \n")
                    .append("                 ,   f.mtest                                                                                                      \n")
                    .append("                 ,   f.ftest                                                                                                      \n")
                    .append("                 ,   f.report                                                                                                     \n")
                    .append("                 ,   f.act                                                                                                        \n")
                    .append("                 ,   f.etc1                                                                                                       \n")
                    .append("                 ,   f.etc2                                                                                                       \n")
                    .append("                 ,   f.avtstep                                                                                                    \n")
                    .append("                 ,   f.avmtest                                                                                                    \n")
                    .append("                 ,   f.avftest                                                                                                    \n")
                    .append("                 ,   f.avreport                                                                                                   \n")
                    .append("                 ,   f.avact                                                                                                      \n")
                    .append("                 ,   f.avetc1                                                                                                     \n")
                    .append("                 ,   f.avetc2                                                                                                     \n")
                    .append("                 ,   f.htest                                                                                                      \n")
                    .append("                 ,   f.avhtest                                                                                                    \n")
                    .append("                 ,   h.isgraduated       course_graduated                                                                         \n")
                    .append("             FROM    tz_pfcourse         a                                                                                        \n")
                    .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                    .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                    .append("                 ,   tz_member           d                                                                                        \n")
                    .append("                 ,   tz_subj             e                                                                                        \n")
                    .append("                 ,   tz_student            f                                                                                        \n")
                    .append("                 ,   (                                                                                                            \n")
                    .append("                         select  i.userid, i.subj, max(i.score) max_score                                                         \n")
                    .append("                         from    tz_student    i                                                                                    \n")
                    .append("                             ,   tz_subjseq  j                                                                                    \n")
                    .append("                         where   i.subj      = j.subj                                                                             \n")
                    .append("                         and     i.subjseq   = j.subjseq                                                                          \n")
                    .append("                         and     i.year      = j.year                                                                             \n")
                    .append("                         and     j.isblended = 'N'                                                                                \n")
                    .append("                         and     j.isexpert  = 'N'                                                                                \n")
                    .append("                         group by i.userid, i.subj                                                                                \n")
                    .append("                     )                   g                                                                                        \n")
                    .append("                 ,   tz_pfstudent        h                                                                                        \n")
                    .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                    .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                    .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                    .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                    .append("             AND     c.userid        = d.userid                                                                                   \n")
                    .append("             AND     c.subj          = e.subj                                                                                     \n")
                    .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                    .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                    .append("             AND     c.userid        = h.userid                                                                                   \n")
                    .append("             AND     b.coursecode    = h.coursecode                                                                               \n")
                    .append("             AND     b.courseyear    = h.courseyear                                                                               \n")
                    .append("             AND     b.courseseq     = h.courseseq                                                                                \n")
                    .append("             AND     c.status        = 'RV'                                                                                       \n")
                    .append("             AND     f.userid        = g.userid                                                                                   \n")
                    .append("             AND     f.subj          = g.subj                                                                                     \n")
                    .append("             AND     g.max_score     = f.score                                                                                    \n")
                    .append("             AND     b.grcode        = " + StringManager.makeSQL(ss_grcode)          + "                                          \n")
                    .append("             AND     b.courseyear    = " + StringManager.makeSQL(ss_pfcourseyear)    + "                                          \n");
                    
               if ( !ss_pfcourse.equals("ALL") ) { 
                   sbSQL.append(" and b.coursecode = " + StringManager.makeSQL(ss_pfcourse)    + "     \n");
               }
               
               if ( !ss_pfcourseseq.equals("ALL") ) { 
                   sbSQL.append(" and b.courseseq = " + StringManager.makeSQL(ss_pfcourseseq)  + "     \n");
               }
               
               if ( !v_iscomplete.equals("") ) {
                   sbSQL.append(" and g.isgraduated = " + StringManager.makeSQL(v_iscomplete) + "      \n");
               }
                    
               sbSQL.append("             GROUP BY b.grcode                                                                                                    \n")
                    .append("                 ,    b.coursecode                                                                                                \n")
                    .append("                 ,    b.courseyear                                                                                                \n")
                    .append("                 ,    b.courseseq                                                                                                 \n")
                    .append("                 ,    a.coursenm                                                                                                  \n")
                    .append("                 ,    d.userid                                                                                                    \n")
                    .append("                 ,    d.name                                                                                                      \n")
                    .append("                 ,    e.subj                                                                                                      \n")
                    .append("                 ,    e.subjnm                                                                                                    \n")
                    .append("                 ,    c.isrequired                                                                                                \n")
                    .append("                 ,    f.isgraduated                                                                                               \n")
                    .append("                 ,    f.score                                                                                                     \n")
                    .append("                 ,    f.tstep                                                                                                     \n")
                    .append("                 ,    f.mtest                                                                                                     \n")
                    .append("                 ,    f.ftest                                                                                                     \n")
                    .append("                 ,    f.report                                                                                                    \n")
                    .append("                 ,    f.act                                                                                                       \n")
                    .append("                 ,    f.etc1                                                                                                      \n")
                    .append("                 ,    f.etc2                                                                                                      \n")
                    .append("                 ,    f.avtstep                                                                                                   \n")
                    .append("                 ,    f.avmtest                                                                                                   \n")
                    .append("                 ,    f.avftest                                                                                                   \n")
                    .append("                 ,    f.avreport                                                                                                  \n")
                    .append("                 ,    f.avact                                                                                                     \n")
                    .append("                 ,    f.avetc1                                                                                                    \n")
                    .append("                 ,    f.avetc2                                                                                                    \n")
                    .append("                 ,    f.htest                                                                                                     \n")
                    .append("                 ,    f.avhtest                                                                                                   \n")
                    .append("                 ,    h.isgraduated                                                                                               \n")
                    .append("             UNION                                                                                                                \n")
                    .append("             SELECT  b.grcode                                                                                                     \n")
                    .append("                 ,   a.coursenm                                                                                                   \n")
                    .append("                 ,   b.coursecode                                                                                                 \n")
                    .append("                 ,   b.courseyear                                                                                                 \n")
                    .append("                 ,   b.courseseq                                                                                                  \n")
                    .append("                 ,   d.userid                                                                                                     \n")
                    .append("                 ,   d.name                                                                                                       \n")
                    .append("                 ,   e.subj                                                                                                       \n")
                    .append("                 ,   e.subjnm                                                                                                     \n")
                    .append("                 ,   c.isrequired                                                                                                 \n")
                    .append("                 ,   f.isgraduated                                                                                                \n")
                    .append("                 ,   f.score                                                                                                      \n")
                    .append("                 ,   f.tstep                                                                                                      \n")
                    .append("                 ,   f.mtest                                                                                                      \n")
                    .append("                 ,   f.ftest                                                                                                      \n")
                    .append("                 ,   f.report                                                                                                     \n")
                    .append("                 ,   f.act                                                                                                        \n")
                    .append("                 ,   f.etc1                                                                                                       \n")
                    .append("                 ,   f.etc2                                                                                                       \n")
                    .append("                 ,   f.avtstep                                                                                                    \n")
                    .append("                 ,   f.avmtest                                                                                                    \n")
                    .append("                 ,   f.avftest                                                                                                    \n")
                    .append("                 ,   f.avreport                                                                                                   \n")
                    .append("                 ,   f.avact                                                                                                      \n")
                    .append("                 ,   f.avetc1                                                                                                     \n")
                    .append("                 ,   f.avetc2                                                                                                     \n")
                    .append("                 ,   f.htest                                                                                                      \n")
                    .append("                 ,   f.avhtest                                                                                                    \n")
                    .append("                 ,   g.isgraduated       course_graduated                                                                         \n")
                    .append("             FROM    tz_pfcourse         a                                                                                        \n")
                    .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                    .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                    .append("                 ,   tz_member           d                                                                                        \n")
                    .append("                 ,   tz_subj             e                                                                                        \n")
                    .append("                 ,   tz_student            f                                                                                        \n")
                    .append("                 ,   tz_pfstudent        g                                                                                        \n")
                    .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                    .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                    .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                    .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                    .append("             AND     c.userid        = d.userid                                                                                   \n")
                    .append("             AND     c.subj          = e.subj                                                                                     \n")
                    .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                    .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                    .append("             AND     c.userid        = g.userid                                                                                   \n")
                    .append("             AND     b.coursecode    = g.coursecode                                                                               \n")
                    .append("             AND     b.courseyear    = g.courseyear                                                                               \n")
                    .append("             AND     b.courseseq     = g.courseseq                                                                                \n")
                    .append("             AND     c.status in ( 'RE', 'FE' )                                                                                   \n")
                    .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                    .append("             AND     c.year          = f.year(+)                                                                                  \n")
                    .append("             AND     c.subjseq       = f.subjseq(+)                                                                               \n")
                    .append("             AND     b.grcode        = " + StringManager.makeSQL(ss_grcode)      + "                                              \n")
                    .append("             AND     b.courseyear    = " + StringManager.makeSQL(ss_pfcourseyear)+ "                                              \n");
                    
                    
               if ( !ss_pfcourse.equals("ALL") ) { 
                   sbSQL.append(" and b.coursecode = " + StringManager.makeSQL(ss_pfcourse)    + "     \n");
               }
               
               if ( !ss_pfcourseseq.equals("ALL") ) { 
                   sbSQL.append(" and b.courseseq = " + StringManager.makeSQL(ss_pfcourseseq)  + "     \n");
               }
               
               if ( !v_iscomplete.equals("") ) {
                   sbSQL.append(" and g.isgraduated = " + StringManager.makeSQL(v_iscomplete) + "      \n");
               }
                    
               sbSQL.append("             GROUP BY b.grcode                                                                                                    \n")
                    .append("                 ,    b.coursecode                                                                                                \n")
                    .append("                 ,    b.courseyear                                                                                                \n")
                    .append("                 ,    b.courseseq                                                                                                 \n")
                    .append("                 ,    a.coursenm                                                                                                  \n")
                    .append("                 ,    d.userid                                                                                                    \n")
                    .append("                 ,    d.name                                                                                                      \n")
                    .append("                 ,    e.subj                                                                                                      \n")
                    .append("                 ,    e.subjnm                                                                                                    \n")
                    .append("                 ,    c.isrequired                                                                                                \n")
                    .append("                 ,    f.isgraduated                                                                                               \n")
                    .append("                 ,    f.score                                                                                                     \n")
                    .append("                 ,    f.tstep                                                                                                     \n")
                    .append("                 ,    f.mtest                                                                                                     \n")
                    .append("                 ,    f.ftest                                                                                                     \n")
                    .append("                 ,    f.report                                                                                                    \n")
                    .append("                 ,    f.act                                                                                                       \n")
                    .append("                 ,    f.etc1                                                                                                      \n")
                    .append("                 ,    f.etc2                                                                                                      \n")
                    .append("                 ,    f.avtstep                                                                                                   \n")
                    .append("                 ,    f.avmtest                                                                                                   \n")
                    .append("                 ,    f.avftest                                                                                                   \n")
                    .append("                 ,    f.avreport                                                                                                  \n")
                    .append("                 ,    f.avact                                                                                                     \n")
                    .append("                 ,    f.avetc1                                                                                                    \n")
                    .append("                 ,    f.avetc2                                                                                                    \n")
                    .append("                 ,    f.htest                                                                                                     \n")
                    .append("                 ,    f.avhtest                                                                                                   \n")
                    .append("                 ,    g.isgraduated                                                                                               \n")
                    .append("             ORDER BY name, isrequired desc                                                                                       \n")
                    .append("         )           a                                                                                                            \n")
                    .append("     ,   (                                                                                                                        \n")
                    .append("             SELECT  userid                                                                                                       \n")
                    .append("                 ,   avg(score                                                               )   total                            \n")
                    .append("                 ,   sum(case when isrequired = 'N' and isgraduated = 'Y' then 1 else 0 end  )   graduated_n_count                \n")
                    .append("                 ,   max(subjnumforcomplete)                                                     requried_n_count                 \n")
                    .append("                 ,   sum(case when isrequired = 'Y' and isgraduated = 'Y' then 1 else 0 end  )   graduated_y_count                \n")
                    .append("                 ,   sum(decode(isrequired, 'Y', 1, 0))                                          requried_y_count                 \n")
                    .append("             FROM    (                                                                                                            \n")
                    .append("                         SELECT  b.grcode                                                                                         \n")
                    .append("                             ,   a.coursenm                                                                                       \n")
                    .append("                             ,   b.coursecode                                                                                     \n")
                    .append("                             ,   b.courseyear                                                                                     \n")
                    .append("                             ,   b.courseseq                                                                                      \n")
                    .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                    .append("                             ,   d.userid                                                                                         \n")
                    .append("                             ,   d.name                                                                                           \n")
                    .append("                             ,   e.subj                                                                                           \n")
                    .append("                             ,   e.subjnm                                                                                         \n")
                    .append("                             ,   c.isrequired                                                                                     \n")
                    .append("                             ,   f.isgraduated                                                                                    \n")
                    .append("                             ,   f.score                                                                                          \n")
                    .append("                             ,   f.tstep                                                                                          \n")
                    .append("                             ,   f.mtest                                                                                          \n")
                    .append("                             ,   f.ftest                                                                                          \n")
                    .append("                             ,   f.report                                                                                         \n")
                    .append("                             ,   f.act                                                                                            \n")
                    .append("                             ,   f.etc1                                                                                           \n")
                    .append("                             ,   f.etc2                                                                                           \n")
                    .append("                             ,   f.avtstep                                                                                        \n")
                    .append("                             ,   f.avmtest                                                                                        \n")
                    .append("                             ,   f.avftest                                                                                        \n")
                    .append("                             ,   f.avreport                                                                                       \n")
                    .append("                             ,   f.avact                                                                                          \n")
                    .append("                             ,   f.avetc1                                                                                         \n")
                    .append("                             ,   f.avetc2                                                                                         \n")
                    .append("                             ,   f.htest                                                                                          \n")
                    .append("                             ,   f.avhtest                                                                                        \n")
                    .append("                             ,   h.isgraduated course_graduated                                                                   \n")
                    .append("                         from    tz_pfcourse         a                                                                            \n")
                    .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                    .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                    .append("                             ,   tz_member           d                                                                            \n")
                    .append("                             ,   tz_subj             e                                                                            \n")
                    .append("                             ,   tz_student            f                                                                            \n")
                    .append("                             ,   (                                                                                                \n")
                    .append("                                     select userid, subj, max(score) max_score                                                    \n")
                    .append("                                     from tz_student                                                                                \n")
                    .append("                                     group by userid, subj                                                                        \n")
                    .append("                                 )                   g                                                                            \n")
                    .append("                             ,        tz_pfstudent   h                                                                            \n")
                    .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                    .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                    .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                    .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                    .append("                         and     c.userid        = d.userid                                                                       \n")
                    .append("                         and     c.subj          = e.subj                                                                         \n")
                    .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                    .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                    .append("                         and     c.userid        = h.userid                                                                       \n")
                    .append("                         and     b.coursecode    = h.coursecode                                                                   \n")
                    .append("                         and     b.courseyear    = h.courseyear                                                                   \n")
                    .append("                         and     b.courseseq     = h.courseseq                                                                    \n")
                    .append("                         and     c.status        = 'RV'                                                                           \n")
                    .append("                         and     f.userid        = g.userid                                                                       \n")
                    .append("                         and     f.subj          = g.subj                                                                         \n")
                    .append("                         and     g.max_score     = f.score                                                                        \n")
                    .append("                         and     b.grcode        = " + StringManager.makeSQL(ss_grcode       ) + "                                \n")
                    .append("                         and     b.courseyear    = " + StringManager.makeSQL(ss_pfcourseyear ) + "                                \n");
                    
               if ( !ss_pfcourse.equals("ALL") ) { 
                   sbSQL.append(" and b.coursecode = " + StringManager.makeSQL(ss_pfcourse)    + "     \n");
               }
               
               if ( !ss_pfcourseseq.equals("ALL") ) { 
                   sbSQL.append(" and b.courseseq = " + StringManager.makeSQL(ss_pfcourseseq)  + "     \n");
               }
               
               if ( !v_iscomplete.equals("") ) {
                   sbSQL.append(" and g.isgraduated = " + StringManager.makeSQL(v_iscomplete) + "      \n");
               }
                    
               sbSQL.append("                         group by b.grcode                                                                                        \n")
                    .append("                             ,    b.coursecode                                                                                    \n")
                    .append("                             ,    b.courseyear                                                                                    \n")
                    .append("                             ,    b.courseseq                                                                                     \n")
                    .append("                             ,    a.coursenm                                                                                      \n")
                    .append("                             ,    d.userid                                                                                        \n")
                    .append("                             ,    d.name                                                                                          \n")
                    .append("                             ,    e.subj                                                                                          \n")
                    .append("                             ,    e.subjnm                                                                                        \n")
                    .append("                             ,    c.isrequired                                                                                    \n")
                    .append("                             ,    f.isgraduated                                                                                   \n")
                    .append("                             ,    f.score                                                                                         \n")
                    .append("                             ,    f.tstep                                                                                         \n")
                    .append("                             ,    f.mtest                                                                                         \n")
                    .append("                             ,    f.ftest                                                                                         \n")
                    .append("                             ,    f.report                                                                                        \n")
                    .append("                             ,    f.act                                                                                           \n")
                    .append("                             ,    f.etc1                                                                                          \n")
                    .append("                             ,    f.etc2                                                                                          \n")
                    .append("                             ,    f.avtstep                                                                                       \n")
                    .append("                             ,    f.avmtest                                                                                       \n")
                    .append("                             ,    f.avftest                                                                                       \n")
                    .append("                             ,    f.avreport                                                                                      \n")
                    .append("                             ,    f.avact                                                                                         \n")
                    .append("                             ,    f.avetc1                                                                                        \n")
                    .append("                             ,    f.avetc2                                                                                        \n")
                    .append("                             ,    f.htest                                                                                         \n")
                    .append("                             ,    f.avhtest                                                                                       \n")
                    .append("                             ,    h.isgraduated                                                                                   \n")
                    .append("                         UNION                                                                                                    \n")
                    .append("                         select  b.grcode                                                                                         \n")
                    .append("                             ,   a.coursenm                                                                                       \n")
                    .append("                             ,   b.coursecode                                                                                     \n")
                    .append("                             ,   b.courseyear                                                                                     \n")
                    .append("                             ,   b.courseseq                                                                                      \n")
                    .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                    .append("                             ,   d.userid                                                                                         \n")
                    .append("                             ,   d.name                                                                                           \n")
                    .append("                             ,   e.subj                                                                                           \n")
                    .append("                             ,   e.subjnm                                                                                         \n")
                    .append("                             ,   c.isrequired                                                                                     \n")
                    .append("                             ,   f.isgraduated                                                                                    \n")
                    .append("                             ,   f.score                                                                                          \n")
                    .append("                             ,   f.tstep                                                                                          \n")
                    .append("                             ,   f.mtest                                                                                          \n")
                    .append("                             ,   f.ftest                                                                                          \n")
                    .append("                             ,   f.report                                                                                         \n")
                    .append("                             ,   f.act                                                                                            \n")
                    .append("                             ,   f.etc1                                                                                           \n")
                    .append("                             ,   f.etc2                                                                                           \n")
                    .append("                             ,   f.avtstep                                                                                        \n")
                    .append("                             ,   f.avmtest                                                                                        \n")
                    .append("                             ,   f.avftest                                                                                        \n")
                    .append("                             ,   f.avreport                                                                                       \n")
                    .append("                             ,   f.avact                                                                                          \n")
                    .append("                             ,   f.avetc1                                                                                         \n")
                    .append("                             ,   f.avetc2                                                                                         \n")
                    .append("                             ,   f.htest                                                                                          \n")
                    .append("                             ,   f.avhtest                                                                                        \n")
                    .append("                             ,   g.isgraduated course_graduated                                                                   \n")
                    .append("                         from    tz_pfcourse         a                                                                            \n")
                    .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                    .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                    .append("                             ,   tz_member           d                                                                            \n")
                    .append("                             ,   tz_subj             e                                                                            \n")
                    .append("                             ,   tz_student            f                                                                            \n")
                    .append("                             ,   tz_pfstudent        g                                                                            \n")
                    .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                    .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                    .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                    .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                    .append("                         and     c.userid        = d.userid                                                                       \n")
                    .append("                         and     c.subj          = e.subj                                                                         \n")
                    .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                    .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                    .append("                         and     c.userid        = g.userid                                                                       \n")
                    .append("                         and     b.coursecode    = g.coursecode                                                                   \n")
                    .append("                         and     b.courseyear    = g.courseyear                                                                   \n")
                    .append("                         and     b.courseseq     = g.courseseq                                                                    \n")
                    .append("                         and     c.status in ( 'RE', 'FE' )                                                                       \n")
                    .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                    .append("                         and     c.year          = f.year(+)                                                                      \n")
                    .append("                         and     c.subjseq       = f.subjseq(+)                                                                   \n")
                    .append("                         and     b.grcode        = " + StringManager.makeSQL(ss_grcode       ) + "                                \n")
                    .append("                         and     b.courseyear    = " + StringManager.makeSQL(ss_pfcourseyear ) + "                                \n");
                    
                    
               if ( !ss_pfcourse.equals("ALL") ) { 
                   sbSQL.append(" and b.coursecode = " + StringManager.makeSQL(ss_pfcourse)    + "     \n");
               }
               
               if ( !ss_pfcourseseq.equals("ALL") ) { 
                   sbSQL.append(" and b.courseseq = " + StringManager.makeSQL(ss_pfcourseseq)  + "     \n");
               }
               
               if ( !v_iscomplete.equals("") ) {
                   sbSQL.append(" and g.isgraduated = " + StringManager.makeSQL(v_iscomplete) + "      \n");
               }
                    
               sbSQL.append("                         group by b.grcode                                                                                        \n")
                    .append("                             , b.coursecode                                                                                       \n")
                    .append("                             , b.courseyear                                                                                       \n")
                    .append("                             , b.courseseq                                                                                        \n")
                    .append("                             , a.coursenm                                                                                         \n")
                    .append("                             , d.userid                                                                                           \n")
                    .append("                             , d.name                                                                                             \n")
                    .append("                             , e.subj                                                                                             \n")
                    .append("                             , e.subjnm                                                                                           \n")
                    .append("                             , c.isrequired                                                                                       \n")
                    .append("                             , f.isgraduated                                                                                      \n")
                    .append("                             , f.score                                                                                            \n")
                    .append("                             , f.tstep                                                                                            \n")
                    .append("                             , f.mtest                                                                                            \n")
                    .append("                             , f.ftest                                                                                            \n")
                    .append("                             , f.report                                                                                           \n")
                    .append("                             , f.act                                                                                              \n")
                    .append("                             , f.etc1                                                                                             \n")
                    .append("                             , f.etc2                                                                                             \n")
                    .append("                             , f.avtstep                                                                                          \n")
                    .append("                             , f.avmtest                                                                                          \n")
                    .append("                             , f.avftest                                                                                          \n")
                    .append("                             , f.avreport                                                                                         \n")
                    .append("                             , f.avact                                                                                            \n")
                    .append("                             , f.avetc1                                                                                           \n")
                    .append("                             , f.avetc2                                                                                           \n")
                    .append("                             , f.htest                                                                                            \n")
                    .append("                             , f.avhtest                                                                                          \n")
                    .append("                             , g.isgraduated                                                                                      \n")
                    .append("                         order by name, isrequired desc                                                                           \n")
                    .append("                     )                                                                                                            \n")
                    .append("             group by userid                                                                                                      \n")
                    .append("         )                b                                                                                                       \n")
                    .append("  ,      vz_orgmember     c                                                                                                       \n")
                    .append(" where   a.userid     = b.userid                                                                                                  \n")
                    .append(" AND     a.userid     = c.userid(+)                                                                                               \n");
                    
               if ( !box.getString("p_sdate").equals("") && !box.getString("p_ldate").equals("") ) { // 기간선택
                   if ( box.getString("p_sdate").equals(box.getString("p_ldate")) ) {
                       sbSQL.append("          AND TO_CHAR(TO_DATE(c.indate, 'yyyymmddhh24miss'), 'yyyymm') = TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymm') \n");
                   } else {
                       sbSQL.append("          AND TO_CHAR(TO_DATE(c.indate, 'yyyymmddhh24miss'), 'yyyymm') BETWEEN TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymm') AND TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_ldate")) + ", 'yyyy-mm-dd'), 'yyyymm') \n");
                   }
               }
               
               if ( !c_sido.equals("") ) {
                   sbSQL.append("             AND c.sido               = " + StringManager.makeSQL(c_sido          ) + "                                       \n");
               }    

               if ( !c_age.equals("") ) {
                   sbSQL.append("             AND c.age               = " + StringManager.makeSQL(c_age            ) + "                                       \n");               
               }
               
               if ( !c_achievement.equals("") ) {
                   sbSQL.append("             AND c.achievement       = " + StringManager.makeSQL(c_achievement    ) + "                                       \n");               
               }
               
               if ( !c_gender.equals("") ) {
                   sbSQL.append("             AND c.gender            = " + StringManager.makeSQL(c_gender         ) + "                                       \n");               
               }
               
               if ( !c_gugun.equals("") ) {
                   sbSQL.append("             AND c.gugun             = " + StringManager.makeSQL(c_gubun          ) + "                                       \n");
               }
               
               if ( !v_orgcode.equals("ALL") ) { 
                   sbSQL.append("            AND  c.orgcode           = " + v_orgcode                                + "                                       \n");
               }
               
               System.out.println("SQL.toString : \n" + sbSQL.toString() );
               
               ls = connMgr.executeQuery(sbSQL.toString());
       
               while ( ls.next() ) { 
                   dbox = ls.getDataBox();
                   list.add(dbox);
               }
           } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
               throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
           } finally { 
               if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
               if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
           }
           
           return list;
      }

       
       /**
       과정별 교육실적
       @param box      receive from the form object and session
       @return ArrayList
       */
        public ArrayList perform_03_01_04(RequestBox box) throws Exception {
            DBConnectionManager connMgr         = null;
            ListSet             ls              = null;
            ArrayList           list            = null;
            StringBuffer        sbSQL           = new StringBuffer("");
             
            DataBox             dbox            = null;
             
            String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
            String              v_courseyear    = box.getStringDefault  ( "p_courseyear"   , ""    ); // 년도
            String              v_courseseq     = box.getStringDefault  ( "p_courseseq"    , ""    ); // 과목 기수
            String              v_coursecode    = box.getStringDefault  ( "p_coursecode"   , ""    ); // 과목&코스
            String              s_gadmin        = box.getSession        ( "gadmin"                 );
            String              v_branch        = box.getStringDefault  ( "p_branch"       , ""    );
            String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
            String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
             
           String              v_orderColumn   = box.getString( "p_orderColumn"  );  // 정렬할 컬럼명
           String              v_orderType     = box.getString( "p_orderType"    );  // 정렬할 순서
           
           try { 
               connMgr = new DBConnectionManager();
               list   = new ArrayList();    

               sbSQL.append(" SELECT  a.grcode                                                                                                                   \n")                                       
                    .append("     ,   a.coursenm                                                                                                                 \n")
                    .append("     ,   a.coursecode                                                                                                               \n")
                    .append("     ,   a.courseyear                                                                                                               \n")
                    .append("     ,   a.courseseq                                                                                                                \n")
                    .append("     ,   a.userid                                                                                                                 \n")
                    .append("     ,   a.name                                                                                                                     \n")
                    .append("     ,   a.subj                                                                                                                     \n")
                    .append("     ,   a.subjnm                                                                                                                   \n")
                    .append("     ,   a.isrequired                                                                                                               \n")
                    .append("     ,   a.isgraduated                                                                                                              \n")
                    .append("     ,   a.score                                                                                                                    \n")
                    .append("     ,   a.tstep                                                                                                                    \n")
                    .append("     ,   a.mtest                                                                                                                    \n")
                    .append("     ,   a.ftest                                                                                                                    \n")
                    .append("     ,   a.report                                                                                                                   \n")
                    .append("     ,   a.act                                                                                                                      \n")
                    .append("     ,   a.etc1                                                                                                                     \n")
                    .append("     ,   a.etc2                                                                                                                     \n")
                    .append("     ,   a.avtstep                                                                                                                  \n")
                    .append("     ,   a.avmtest                                                                                                                  \n")
                    .append("     ,   a.avftest                                                                                                                  \n")
                    .append("     ,   a.avreport                                                                                                                 \n")
                    .append("     ,   a.avact                                                                                                                    \n")
                    .append("     ,   a.avetc1                                                                                                                   \n")
                    .append("     ,   a.avetc2                                                                                                                   \n")
                    .append("     ,   a.htest                                                                                                                    \n")
                    .append("     ,   a.avhtest                                                                                                                  \n")
                    .append("     ,   a.course_graduated                                                                                                         \n")
                    .append("     ,   b.total                                                                                                                  \n")
                    .append("     ,   b.requried_n_count                                                                                                       \n")
                    .append("     ,   b.graduated_n_count                                                                                                      \n")
                    .append("     ,   b.requried_y_count                                                                                                       \n")
                    .append("     ,   b.graduated_y_count                                                                                                      \n")
                    .append(" FROM    (                                                                                                                        \n")
                    .append("          select  b.grcode                                                                                                        \n")
                    .append("                 ,   a.coursenm                                                                                                   \n")
                    .append("                 ,   b.coursecode                                                                                                 \n")
                    .append("                 ,   b.courseyear                                                                                                 \n")
                    .append("                 ,   b.courseseq                                                                                                  \n")
                    .append("                 ,   d.userid                                                                                                     \n")
                    .append("                 ,   d.name                                                                                                       \n")
                    .append("                 ,   e.subj                                                                                                       \n")
                    .append("                 ,   e.subjnm                                                                                                     \n")
                    .append("                 ,   c.isrequired                                                                                                 \n")
                    .append("                 ,   f.isgraduated                                                                                                \n")
                    .append("                 ,   f.score                                                                                                      \n")
                    .append("                 ,   f.tstep                                                                                                      \n")
                    .append("                 ,   f.mtest                                                                                                      \n")
                    .append("                 ,   f.ftest                                                                                                      \n")
                    .append("                 ,   f.report                                                                                                     \n")
                    .append("                 ,   f.act                                                                                                        \n")
                    .append("                 ,   f.etc1                                                                                                       \n")
                    .append("                 ,   f.etc2                                                                                                       \n")
                    .append("                 ,   f.avtstep                                                                                                    \n")
                    .append("                 ,   f.avmtest                                                                                                    \n")
                    .append("                 ,   f.avftest                                                                                                    \n")
                    .append("                 ,   f.avreport                                                                                                   \n")
                    .append("                 ,   f.avact                                                                                                      \n")
                    .append("                 ,   f.avetc1                                                                                                     \n")
                    .append("                 ,   f.avetc2                                                                                                     \n")
                    .append("                 ,   f.htest                                                                                                      \n")
                    .append("                 ,   f.avhtest                                                                                                    \n")
                    .append("                 ,   h.isgraduated       course_graduated                                                                         \n")
                    .append("             FROM    tz_pfcourse         a                                                                                        \n")
                    .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                    .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                    .append("                 ,   tz_member           d                                                                                        \n")
                    .append("                 ,   tz_subj             e                                                                                        \n")
                    .append("                 ,   tz_student          f                                                                                        \n")
                    .append("                 ,   (                                                                                                            \n")
                    .append("                         select  i.userid, i.subj, max(i.score) max_score                                                         \n")
                    .append("                         from    tz_student  i                                                                                    \n")
                    .append("                             ,   tz_subjseq  j                                                                                    \n")
                    .append("                         where   i.subj      = j.subj                                                                             \n")
                    .append("                         and     i.subjseq   = j.subjseq                                                                          \n")
                    .append("                         and     i.year      = j.year                                                                             \n")
                    .append("                         and     j.isblended = 'N'                                                                                \n")
                    .append("                         and     j.isexpert  = 'N'                                                                                \n")
                    .append("                         group by i.userid, i.subj                                                                                \n")
                    .append("                     )                   g                                                                                        \n")
                    .append("                 ,   tz_pfstudent        h                                                                                        \n")
                    .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                    .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                    .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                    .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                    .append("             AND     c.userid        = d.userid                                                                                   \n")
                    .append("             AND     c.subj          = e.subj                                                                                     \n")
                    .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                    .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                    .append("             AND     c.userid        = h.userid                                                                                   \n")
                    .append("             AND     b.coursecode    = h.coursecode                                                                               \n")
                    .append("             AND     b.courseyear    = h.courseyear                                                                               \n")
                    .append("             AND     b.courseseq     = h.courseseq                                                                                \n")
                    .append("             AND     c.status        = 'RV'                                                                                       \n")
                    .append("             AND     f.userid        = g.userid                                                                                   \n")
                    .append("             AND     f.subj          = g.subj                                                                                     \n")
                    .append("             AND     g.max_score     = f.score                                                                                    \n")
                    .append("             GROUP BY b.grcode                                                                                                    \n")
                    .append("                 ,    b.coursecode                                                                                                \n")
                    .append("                 ,    b.courseyear                                                                                                \n")
                    .append("                 ,    b.courseseq                                                                                                 \n")
                    .append("                 ,    a.coursenm                                                                                                  \n")
                    .append("                 ,    d.userid                                                                                                    \n")
                    .append("                 ,    d.name                                                                                                      \n")
                    .append("                 ,    e.subj                                                                                                      \n")
                    .append("                 ,    e.subjnm                                                                                                    \n")
                    .append("                 ,    c.isrequired                                                                                                \n")
                    .append("                 ,    f.isgraduated                                                                                               \n")
                    .append("                 ,    f.score                                                                                                     \n")
                    .append("                 ,    f.tstep                                                                                                     \n")
                    .append("                 ,    f.mtest                                                                                                     \n")
                    .append("                 ,    f.ftest                                                                                                     \n")
                    .append("                 ,    f.report                                                                                                    \n")
                    .append("                 ,    f.act                                                                                                       \n")
                    .append("                 ,    f.etc1                                                                                                      \n")
                    .append("                 ,    f.etc2                                                                                                      \n")
                    .append("                 ,    f.avtstep                                                                                                   \n")
                    .append("                 ,    f.avmtest                                                                                                   \n")
                    .append("                 ,    f.avftest                                                                                                   \n")
                    .append("                 ,    f.avreport                                                                                                  \n")
                    .append("                 ,    f.avact                                                                                                     \n")
                    .append("                 ,    f.avetc1                                                                                                    \n")
                    .append("                 ,    f.avetc2                                                                                                    \n")
                    .append("                 ,    f.htest                                                                                                     \n")
                    .append("                 ,    f.avhtest                                                                                                   \n")
                    .append("                 ,    h.isgraduated                                                                                               \n")
                    .append("             UNION                                                                                                                \n")
                    .append("             SELECT  b.grcode                                                                                                     \n")
                    .append("                 ,   a.coursenm                                                                                                   \n")
                    .append("                 ,   b.coursecode                                                                                                 \n")
                    .append("                 ,   b.courseyear                                                                                                 \n")
                    .append("                 ,   b.courseseq                                                                                                  \n")
                    .append("                 ,   d.userid                                                                                                     \n")
                    .append("                 ,   d.name                                                                                                       \n")
                    .append("                 ,   e.subj                                                                                                       \n")
                    .append("                 ,   e.subjnm                                                                                                     \n")
                    .append("                 ,   c.isrequired                                                                                                 \n")
                    .append("                 ,   f.isgraduated                                                                                                \n")
                    .append("                 ,   f.score                                                                                                      \n")
                    .append("                 ,   f.tstep                                                                                                      \n")
                    .append("                 ,   f.mtest                                                                                                      \n")
                    .append("                 ,   f.ftest                                                                                                      \n")
                    .append("                 ,   f.report                                                                                                     \n")
                    .append("                 ,   f.act                                                                                                        \n")
                    .append("                 ,   f.etc1                                                                                                       \n")
                    .append("                 ,   f.etc2                                                                                                       \n")
                    .append("                 ,   f.avtstep                                                                                                    \n")
                    .append("                 ,   f.avmtest                                                                                                    \n")
                    .append("                 ,   f.avftest                                                                                                    \n")
                    .append("                 ,   f.avreport                                                                                                   \n")
                    .append("                 ,   f.avact                                                                                                      \n")
                    .append("                 ,   f.avetc1                                                                                                     \n")
                    .append("                 ,   f.avetc2                                                                                                     \n")
                    .append("                 ,   f.htest                                                                                                      \n")
                    .append("                 ,   f.avhtest                                                                                                    \n")
                    .append("                 ,   g.isgraduated       course_graduated                                                                         \n")
                    .append("             FROM    tz_pfcourse         a                                                                                        \n")
                    .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                    .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                    .append("                 ,   tz_member           d                                                                                        \n")
                    .append("                 ,   tz_subj             e                                                                                        \n")
                    .append("                 ,   tz_student            f                                                                                        \n")
                    .append("                 ,   tz_pfstudent        g                                                                                        \n")
                    .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                    .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                    .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                    .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                    .append("             AND     c.userid        = d.userid                                                                                   \n")
                    .append("             AND     c.subj          = e.subj                                                                                     \n")
                    .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                    .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                    .append("             AND     c.userid        = g.userid                                                                                   \n")
                    .append("             AND     b.coursecode    = g.coursecode                                                                               \n")
                    .append("             AND     b.courseyear    = g.courseyear                                                                               \n")
                    .append("             AND     b.courseseq     = g.courseseq                                                                                \n")
                    .append("             AND     c.status in ( 'RE', 'FE' )                                                                                   \n")
                    .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                    .append("             AND     c.year          = f.year(+)                                                                                  \n")
                    .append("             AND     c.subjseq       = f.subjseq(+)                                                                               \n")
                    .append("             GROUP BY b.grcode                                                                                                    \n")
                    .append("                 ,    b.coursecode                                                                                                \n")
                    .append("                 ,    b.courseyear                                                                                                \n")
                    .append("                 ,    b.courseseq                                                                                                 \n")
                    .append("                 ,    a.coursenm                                                                                                  \n")
                    .append("                 ,    d.userid                                                                                                    \n")
                    .append("                 ,    d.name                                                                                                      \n")
                    .append("                 ,    e.subj                                                                                                      \n")
                    .append("                 ,    e.subjnm                                                                                                    \n")
                    .append("                 ,    c.isrequired                                                                                                \n")
                    .append("                 ,    f.isgraduated                                                                                               \n")
                    .append("                 ,    f.score                                                                                                     \n")
                    .append("                 ,    f.tstep                                                                                                     \n")
                    .append("                 ,    f.mtest                                                                                                     \n")
                    .append("                 ,    f.ftest                                                                                                     \n")
                    .append("                 ,    f.report                                                                                                    \n")
                    .append("                 ,    f.act                                                                                                       \n")
                    .append("                 ,    f.etc1                                                                                                      \n")
                    .append("                 ,    f.etc2                                                                                                      \n")
                    .append("                 ,    f.avtstep                                                                                                   \n")
                    .append("                 ,    f.avmtest                                                                                                   \n")
                    .append("                 ,    f.avftest                                                                                                   \n")
                    .append("                 ,    f.avreport                                                                                                  \n")
                    .append("                 ,    f.avact                                                                                                     \n")
                    .append("                 ,    f.avetc1                                                                                                    \n")
                    .append("                 ,    f.avetc2                                                                                                    \n")
                    .append("                 ,    f.htest                                                                                                     \n")
                    .append("                 ,    f.avhtest                                                                                                   \n")
                    .append("                 ,    g.isgraduated                                                                                               \n")
                    .append("             ORDER BY name, isrequired desc                                                                                       \n")
                    .append("         )           a                                                                                                            \n")
                    .append("     ,   (                                                                                                                        \n")
                    .append("             SELECT  grcode                                                                                                       \n")
                    .append("                 ,   courseyear                                                                                                   \n")
                    .append("                 ,   courseseq                                                                                                    \n")
                    .append("                 ,   coursecode                                                                                                   \n")
                    .append("                 ,   userid                                                                                                       \n")
                    .append("                 ,   avg(score                                                               )   total                            \n")
                    .append("                 ,   sum(case when isrequired = 'N' and isgraduated = 'Y' then 1 else 0 end  )   graduated_n_count                \n")
                    .append("                 ,   max(subjnumforcomplete)                                                     requried_n_count                 \n")
                    .append("                 ,   sum(case when isrequired = 'Y' and isgraduated = 'Y' then 1 else 0 end  )   graduated_y_count                \n")
                    .append("                 ,   sum(decode(isrequired, 'Y', 1, 0))                                          requried_y_count                 \n")
                    .append("             FROM    (                                                                                                            \n")
                    .append("                         SELECT  b.grcode                                                                                         \n")
                    .append("                             ,   a.coursenm                                                                                       \n")
                    .append("                             ,   b.coursecode                                                                                     \n")
                    .append("                             ,   b.courseyear                                                                                     \n")
                    .append("                             ,   b.courseseq                                                                                      \n")
                    .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                    .append("                             ,   d.userid                                                                                         \n")
                    .append("                             ,   d.name                                                                                           \n")
                    .append("                             ,   e.subj                                                                                           \n")
                    .append("                             ,   e.subjnm                                                                                         \n")
                    .append("                             ,   c.isrequired                                                                                     \n")
                    .append("                             ,   f.isgraduated                                                                                    \n")
                    .append("                             ,   f.score                                                                                          \n")
                    .append("                             ,   f.tstep                                                                                          \n")
                    .append("                             ,   f.mtest                                                                                          \n")
                    .append("                             ,   f.ftest                                                                                          \n")
                    .append("                             ,   f.report                                                                                         \n")
                    .append("                             ,   f.act                                                                                            \n")
                    .append("                             ,   f.etc1                                                                                           \n")
                    .append("                             ,   f.etc2                                                                                           \n")
                    .append("                             ,   f.avtstep                                                                                        \n")
                    .append("                             ,   f.avmtest                                                                                        \n")
                    .append("                             ,   f.avftest                                                                                        \n")
                    .append("                             ,   f.avreport                                                                                       \n")
                    .append("                             ,   f.avact                                                                                          \n")
                    .append("                             ,   f.avetc1                                                                                         \n")
                    .append("                             ,   f.avetc2                                                                                         \n")
                    .append("                             ,   f.htest                                                                                          \n")
                    .append("                             ,   f.avhtest                                                                                        \n")
                    .append("                             ,   h.isgraduated course_graduated                                                                   \n")
                    .append("                         from    tz_pfcourse         a                                                                            \n")
                    .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                    .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                    .append("                             ,   tz_member           d                                                                            \n")
                    .append("                             ,   tz_subj             e                                                                            \n")
                    .append("                             ,   tz_student            f                                                                            \n")
                    .append("                             ,   (                                                                                                \n")
                    .append("                                     select userid, subj, max(score) max_score                                                    \n")
                    .append("                                     from tz_student                                                                                \n")
                    .append("                                     group by userid, subj                                                                        \n")
                    .append("                                 )                   g                                                                            \n")
                    .append("                             ,        tz_pfstudent   h                                                                            \n")
                    .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                    .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                    .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                    .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                    .append("                         and     c.userid        = d.userid                                                                       \n")
                    .append("                         and     c.subj          = e.subj                                                                         \n")
                    .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                    .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                    .append("                         and     c.userid        = h.userid                                                                       \n")
                    .append("                         and     b.coursecode    = h.coursecode                                                                   \n")
                    .append("                         and     b.courseyear    = h.courseyear                                                                   \n")
                    .append("                         and     b.courseseq     = h.courseseq                                                                    \n")
                    .append("                         and     c.status        = 'RV'                                                                           \n")
                    .append("                         and     f.userid        = g.userid                                                                       \n")
                    .append("                         and     f.subj          = g.subj                                                                         \n")
                    .append("                         and     g.max_score     = f.score                                                                        \n")
                    .append("                         group by b.grcode                                                                                        \n")
                    .append("                             ,    b.coursecode                                                                                    \n")
                    .append("                             ,    b.courseyear                                                                                    \n")
                    .append("                             ,    b.courseseq                                                                                     \n")
                    .append("                             ,    a.coursenm                                                                                      \n")
                    .append("                             ,    d.userid                                                                                        \n")
                    .append("                             ,    d.name                                                                                          \n")
                    .append("                             ,    e.subj                                                                                          \n")
                    .append("                             ,    e.subjnm                                                                                        \n")
                    .append("                             ,    c.isrequired                                                                                    \n")
                    .append("                             ,    f.isgraduated                                                                                   \n")
                    .append("                             ,    f.score                                                                                         \n")
                    .append("                             ,    f.tstep                                                                                         \n")
                    .append("                             ,    f.mtest                                                                                         \n")
                    .append("                             ,    f.ftest                                                                                         \n")
                    .append("                             ,    f.report                                                                                        \n")
                    .append("                             ,    f.act                                                                                           \n")
                    .append("                             ,    f.etc1                                                                                          \n")
                    .append("                             ,    f.etc2                                                                                          \n")
                    .append("                             ,    f.avtstep                                                                                       \n")
                    .append("                             ,    f.avmtest                                                                                       \n")
                    .append("                             ,    f.avftest                                                                                       \n")
                    .append("                             ,    f.avreport                                                                                      \n")
                    .append("                             ,    f.avact                                                                                         \n")
                    .append("                             ,    f.avetc1                                                                                        \n")
                    .append("                             ,    f.avetc2                                                                                        \n")
                    .append("                             ,    f.htest                                                                                         \n")
                    .append("                             ,    f.avhtest                                                                                       \n")
                    .append("                             ,    h.isgraduated                                                                                   \n")
                    .append("                         UNION                                                                                                    \n")
                    .append("                         select  b.grcode                                                                                         \n")
                    .append("                             ,   a.coursenm                                                                                       \n")
                    .append("                             ,   b.coursecode                                                                                     \n")
                    .append("                             ,   b.courseyear                                                                                     \n")
                    .append("                             ,   b.courseseq                                                                                      \n")
                    .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                    .append("                             ,   d.userid                                                                                         \n")
                    .append("                             ,   d.name                                                                                           \n")
                    .append("                             ,   e.subj                                                                                           \n")
                    .append("                             ,   e.subjnm                                                                                         \n")
                    .append("                             ,   c.isrequired                                                                                     \n")
                    .append("                             ,   f.isgraduated                                                                                    \n")
                    .append("                             ,   f.score                                                                                          \n")
                    .append("                             ,   f.tstep                                                                                          \n")
                    .append("                             ,   f.mtest                                                                                          \n")
                    .append("                             ,   f.ftest                                                                                          \n")
                    .append("                             ,   f.report                                                                                         \n")
                    .append("                             ,   f.act                                                                                            \n")
                    .append("                             ,   f.etc1                                                                                           \n")
                    .append("                             ,   f.etc2                                                                                           \n")
                    .append("                             ,   f.avtstep                                                                                        \n")
                    .append("                             ,   f.avmtest                                                                                        \n")
                    .append("                             ,   f.avftest                                                                                        \n")
                    .append("                             ,   f.avreport                                                                                       \n")
                    .append("                             ,   f.avact                                                                                          \n")
                    .append("                             ,   f.avetc1                                                                                         \n")
                    .append("                             ,   f.avetc2                                                                                         \n")
                    .append("                             ,   f.htest                                                                                          \n")
                    .append("                             ,   f.avhtest                                                                                        \n")
                    .append("                             ,   g.isgraduated course_graduated                                                                   \n")
                    .append("                         from    tz_pfcourse         a                                                                            \n")
                    .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                    .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                    .append("                             ,   tz_member           d                                                                            \n")
                    .append("                             ,   tz_subj             e                                                                            \n")
                    .append("                             ,   tz_student            f                                                                            \n")
                    .append("                             ,   tz_pfstudent        g                                                                            \n")
                    .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                    .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                    .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                    .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                    .append("                         and     c.userid        = d.userid                                                                       \n")
                    .append("                         and     c.subj          = e.subj                                                                         \n")
                    .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                    .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                    .append("                         and     c.userid        = g.userid                                                                       \n")
                    .append("                         and     b.coursecode    = g.coursecode                                                                   \n")
                    .append("                         and     b.courseyear    = g.courseyear                                                                   \n")
                    .append("                         and     b.courseseq     = g.courseseq                                                                    \n")
                    .append("                         and     c.status in ( 'RE', 'FE' )                                                                       \n")
                    .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                    .append("                         and     c.year          = f.year(+)                                                                      \n")
                    .append("                         and     c.subjseq       = f.subjseq(+)                                                                   \n")
                    .append("                         group by b.grcode                                                                                        \n")
                    .append("                             , b.coursecode                                                                                       \n")
                    .append("                             , b.courseyear                                                                                       \n")
                    .append("                             , b.courseseq                                                                                        \n")
                    .append("                             , a.coursenm                                                                                         \n")
                    .append("                             , d.userid                                                                                           \n")
                    .append("                             , d.name                                                                                             \n")
                    .append("                             , e.subj                                                                                             \n")
                    .append("                             , e.subjnm                                                                                           \n")
                    .append("                             , c.isrequired                                                                                       \n")
                    .append("                             , f.isgraduated                                                                                      \n")
                    .append("                             , f.score                                                                                            \n")
                    .append("                             , f.tstep                                                                                            \n")
                    .append("                             , f.mtest                                                                                            \n")
                    .append("                             , f.ftest                                                                                            \n")
                    .append("                             , f.report                                                                                           \n")
                    .append("                             , f.act                                                                                              \n")
                    .append("                             , f.etc1                                                                                             \n")
                    .append("                             , f.etc2                                                                                             \n")
                    .append("                             , f.avtstep                                                                                          \n")
                    .append("                             , f.avmtest                                                                                          \n")
                    .append("                             , f.avftest                                                                                          \n")
                    .append("                             , f.avreport                                                                                         \n")
                    .append("                             , f.avact                                                                                            \n")
                    .append("                             , f.avetc1                                                                                           \n")
                    .append("                             , f.avetc2                                                                                           \n")
                    .append("                             , f.htest                                                                                            \n")
                    .append("                             , f.avhtest                                                                                          \n")
                    .append("                             , g.isgraduated                                                                                      \n")
                    .append("                         order by name, isrequired desc                                                                           \n")
                    .append("                     )                                                                                                            \n")
                    .append("             group by grcode, courseyear, courseseq, coursecode, userid                                                           \n")
                    .append("         )                b                                                                                                       \n");
               
               if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                   sbSQL.append("  ,  vz_orgmember     c                                                                                                       \n");
               }
               
               sbSQL.append(" WHERE   a.grcode        = " + StringManager.makeSQL(v_grcode         ) + "                                                       \n")
                    .append(" and     a.courseyear    = " + StringManager.makeSQL(v_courseyear     ) + "                                                       \n");

                if ( !v_coursecode.equals("") ) { 
                    sbSQL.append(" and a.coursecode    = " + StringManager.makeSQL(v_coursecode    ) + "   \n");
                }
                
                if ( !v_courseseq.equals("") ) { 
                    sbSQL.append(" and a.courseseq     = " + StringManager.makeSQL(v_courseseq     ) + "   \n");
                }
                
                
                if ( v_isgraduated.equals("Y") ) {
                    sbSQL.append(" and a.course_graduated   = " + StringManager.makeSQL(v_isgraduated   ) + "   \n");
                } else if ( v_isgraduated.equals("N") ) {
                    sbSQL.append(" and ( a.course_graduated   IS NULL OR a.course_graduated = " + StringManager.makeSQL(v_isgraduated   ) + " ) \n");
                }
                
                if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                    sbSQL.append(" AND     c.orgcode       = " + v_orgcode + "                             \n")
                         .append(" and     a.userid        = c.userid                                      \n");
                }
                
                sbSQL.append(" and a.userid = b.userid                                                                                                         \n")
                     .append(" ORDER BY a.grcode, a.courseyear, a.coursecode, a.courseseq, a.name, a.userid, a.isrequired desc, a.subj                         \n");
                    
               System.out.println("SQL.toString : \n" + sbSQL.toString() );
               
               ls = connMgr.executeQuery(sbSQL.toString());
       
               while ( ls.next() ) { 
                   dbox = ls.getDataBox();
                   list.add(dbox);
               }
           } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
               throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
           } finally { 
               if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
               if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
           }
           
           return list;
       }
        
     
        /**
        분야별 교육 실적
        @param box      receive from the form object and session
        @return ArrayList
        */
         public ArrayList perform_03_01_05(RequestBox box) throws Exception { 
             DBConnectionManager connMgr         = null;
             ListSet             ls              = null;
             ArrayList           list            = null;
             StringBuffer        sbSQL           = new StringBuffer("");
              
             DataBox             dbox            = null;
              
             String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
             String              v_courseyear    = box.getStringDefault  ( "p_courseyear"   , ""    ); // 년도
             String              v_courseseq     = box.getStringDefault  ( "p_courseseq"    , ""    ); // 과목 기수
             String              v_coursecode    = box.getStringDefault  ( "p_coursecode"   , ""    ); // 과목&코스
             String              s_gadmin        = box.getSession        ( "gadmin"                 );
             String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
             String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
             String              v_sido          = box.getString         ( "p_sido"                 );
             String              v_gugun         = box.getString         ( "p_gugun"                );
             String              v_gender        = box.getString         ( "p_gender"               );
             String              v_achievement   = box.getString         ( "p_achievement"          );
             String              v_age           = box.getString         ( "p_age"                  );
             String              v_gubun         = box.getString         ( "p_gubun"                );
              
             try {
                 connMgr     = new DBConnectionManager();            
                 list        = new ArrayList();
                 

                 sbSQL.append(" SELECT  a.grcode                                                                                                                   \n")                                       
                      .append("     ,   a.coursenm                                                                                                                 \n")
                      .append("     ,   a.coursecode                                                                                                               \n")
                      .append("     ,   a.courseyear                                                                                                               \n")
                      .append("     ,   a.courseseq                                                                                                                \n")
                      .append("     ,   a.userid                                                                                                                 \n")
                      .append("     ,   a.name                                                                                                                     \n")
                      .append("     ,   a.subj                                                                                                                     \n")
                      .append("     ,   a.subjnm                                                                                                                   \n")
                      .append("     ,   a.isrequired                                                                                                               \n")
                      .append("     ,   a.isgraduated                                                                                                              \n")
                      .append("     ,   a.score                                                                                                                    \n")
                      .append("     ,   a.tstep                                                                                                                    \n")
                      .append("     ,   a.mtest                                                                                                                    \n")
                      .append("     ,   a.ftest                                                                                                                    \n")
                      .append("     ,   a.report                                                                                                                   \n")
                      .append("     ,   a.act                                                                                                                      \n")
                      .append("     ,   a.etc1                                                                                                                     \n")
                      .append("     ,   a.etc2                                                                                                                     \n")
                      .append("     ,   a.avtstep                                                                                                                  \n")
                      .append("     ,   a.avmtest                                                                                                                  \n")
                      .append("     ,   a.avftest                                                                                                                  \n")
                      .append("     ,   a.avreport                                                                                                                 \n")
                      .append("     ,   a.avact                                                                                                                    \n")
                      .append("     ,   a.avetc1                                                                                                                   \n")
                      .append("     ,   a.avetc2                                                                                                                   \n")
                      .append("     ,   a.htest                                                                                                                    \n")
                      .append("     ,   a.avhtest                                                                                                                  \n")
                      .append("     ,   a.course_graduated                                                                                                         \n")
                      .append("     ,   b.total                                                                                                                  \n")
                      .append("     ,   b.requried_n_count                                                                                                       \n")
                      .append("     ,   b.graduated_n_count                                                                                                      \n")
                      .append("     ,   b.requried_y_count                                                                                                       \n")
                      .append("     ,   b.graduated_y_count                                                                                                      \n")
                      .append(" FROM    (                                                                                                                        \n")
                      .append("          select  b.grcode                                                                                                        \n")
                      .append("                 ,   a.coursenm                                                                                                   \n")
                      .append("                 ,   b.coursecode                                                                                                 \n")
                      .append("                 ,   b.courseyear                                                                                                 \n")
                      .append("                 ,   b.courseseq                                                                                                  \n")
                      .append("                 ,   d.userid                                                                                                     \n")
                      .append("                 ,   d.name                                                                                                       \n")
                      .append("                 ,   e.subj                                                                                                       \n")
                      .append("                 ,   e.subjnm                                                                                                     \n")
                      .append("                 ,   c.isrequired                                                                                                 \n")
                      .append("                 ,   f.isgraduated                                                                                                \n")
                      .append("                 ,   f.score                                                                                                      \n")
                      .append("                 ,   f.tstep                                                                                                      \n")
                      .append("                 ,   f.mtest                                                                                                      \n")
                      .append("                 ,   f.ftest                                                                                                      \n")
                      .append("                 ,   f.report                                                                                                     \n")
                      .append("                 ,   f.act                                                                                                        \n")
                      .append("                 ,   f.etc1                                                                                                       \n")
                      .append("                 ,   f.etc2                                                                                                       \n")
                      .append("                 ,   f.avtstep                                                                                                    \n")
                      .append("                 ,   f.avmtest                                                                                                    \n")
                      .append("                 ,   f.avftest                                                                                                    \n")
                      .append("                 ,   f.avreport                                                                                                   \n")
                      .append("                 ,   f.avact                                                                                                      \n")
                      .append("                 ,   f.avetc1                                                                                                     \n")
                      .append("                 ,   f.avetc2                                                                                                     \n")
                      .append("                 ,   f.htest                                                                                                      \n")
                      .append("                 ,   f.avhtest                                                                                                    \n")
                      .append("                 ,   h.isgraduated       course_graduated                                                                         \n")
                      .append("             FROM    tz_pfcourse         a                                                                                        \n")
                      .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                      .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                      .append("                 ,   tz_member           d                                                                                        \n")
                      .append("                 ,   tz_subj             e                                                                                        \n")
                      .append("                 ,   tz_student          f                                                                                        \n")
                      .append("                 ,   (                                                                                                            \n")
                      .append("                         select  i.userid, i.subj, max(i.score) max_score                                                         \n")
                      .append("                         from    tz_student  i                                                                                    \n")
                      .append("                             ,   tz_subjseq  j                                                                                    \n")
                      .append("                         where   i.subj      = j.subj                                                                             \n")
                      .append("                         and     i.subjseq   = j.subjseq                                                                          \n")
                      .append("                         and     i.year      = j.year                                                                             \n")
                      .append("                         and     j.isblended = 'N'                                                                                \n")
                      .append("                         and     j.isexpert  = 'N'                                                                                \n")
                      .append("                         group by i.userid, i.subj                                                                                \n")
                      .append("                     )                   g                                                                                        \n")
                      .append("                 ,   tz_pfstudent        h                                                                                        \n")
                      .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                      .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                      .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                      .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                      .append("             AND     c.userid        = d.userid                                                                                   \n")
                      .append("             AND     c.subj          = e.subj                                                                                     \n")
                      .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                      .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                      .append("             AND     c.userid        = h.userid                                                                                   \n")
                      .append("             AND     b.coursecode    = h.coursecode                                                                               \n")
                      .append("             AND     b.courseyear    = h.courseyear                                                                               \n")
                      .append("             AND     b.courseseq     = h.courseseq                                                                                \n")
                      .append("             AND     c.status        = 'RV'                                                                                       \n")
                      .append("             AND     f.userid        = g.userid                                                                                   \n")
                      .append("             AND     f.subj          = g.subj                                                                                     \n")
                      .append("             AND     g.max_score     = f.score                                                                                    \n")
                      .append("             GROUP BY b.grcode                                                                                                    \n")
                      .append("                 ,    b.coursecode                                                                                                \n")
                      .append("                 ,    b.courseyear                                                                                                \n")
                      .append("                 ,    b.courseseq                                                                                                 \n")
                      .append("                 ,    a.coursenm                                                                                                  \n")
                      .append("                 ,    d.userid                                                                                                    \n")
                      .append("                 ,    d.name                                                                                                      \n")
                      .append("                 ,    e.subj                                                                                                      \n")
                      .append("                 ,    e.subjnm                                                                                                    \n")
                      .append("                 ,    c.isrequired                                                                                                \n")
                      .append("                 ,    f.isgraduated                                                                                               \n")
                      .append("                 ,    f.score                                                                                                     \n")
                      .append("                 ,    f.tstep                                                                                                     \n")
                      .append("                 ,    f.mtest                                                                                                     \n")
                      .append("                 ,    f.ftest                                                                                                     \n")
                      .append("                 ,    f.report                                                                                                    \n")
                      .append("                 ,    f.act                                                                                                       \n")
                      .append("                 ,    f.etc1                                                                                                      \n")
                      .append("                 ,    f.etc2                                                                                                      \n")
                      .append("                 ,    f.avtstep                                                                                                   \n")
                      .append("                 ,    f.avmtest                                                                                                   \n")
                      .append("                 ,    f.avftest                                                                                                   \n")
                      .append("                 ,    f.avreport                                                                                                  \n")
                      .append("                 ,    f.avact                                                                                                     \n")
                      .append("                 ,    f.avetc1                                                                                                    \n")
                      .append("                 ,    f.avetc2                                                                                                    \n")
                      .append("                 ,    f.htest                                                                                                     \n")
                      .append("                 ,    f.avhtest                                                                                                   \n")
                      .append("                 ,    h.isgraduated                                                                                               \n")
                      .append("             UNION                                                                                                                \n")
                      .append("             SELECT  b.grcode                                                                                                     \n")
                      .append("                 ,   a.coursenm                                                                                                   \n")
                      .append("                 ,   b.coursecode                                                                                                 \n")
                      .append("                 ,   b.courseyear                                                                                                 \n")
                      .append("                 ,   b.courseseq                                                                                                  \n")
                      .append("                 ,   d.userid                                                                                                     \n")
                      .append("                 ,   d.name                                                                                                       \n")
                      .append("                 ,   e.subj                                                                                                       \n")
                      .append("                 ,   e.subjnm                                                                                                     \n")
                      .append("                 ,   c.isrequired                                                                                                 \n")
                      .append("                 ,   f.isgraduated                                                                                                \n")
                      .append("                 ,   f.score                                                                                                      \n")
                      .append("                 ,   f.tstep                                                                                                      \n")
                      .append("                 ,   f.mtest                                                                                                      \n")
                      .append("                 ,   f.ftest                                                                                                      \n")
                      .append("                 ,   f.report                                                                                                     \n")
                      .append("                 ,   f.act                                                                                                        \n")
                      .append("                 ,   f.etc1                                                                                                       \n")
                      .append("                 ,   f.etc2                                                                                                       \n")
                      .append("                 ,   f.avtstep                                                                                                    \n")
                      .append("                 ,   f.avmtest                                                                                                    \n")
                      .append("                 ,   f.avftest                                                                                                    \n")
                      .append("                 ,   f.avreport                                                                                                   \n")
                      .append("                 ,   f.avact                                                                                                      \n")
                      .append("                 ,   f.avetc1                                                                                                     \n")
                      .append("                 ,   f.avetc2                                                                                                     \n")
                      .append("                 ,   f.htest                                                                                                      \n")
                      .append("                 ,   f.avhtest                                                                                                    \n")
                      .append("                 ,   g.isgraduated       course_graduated                                                                         \n")
                      .append("             FROM    tz_pfcourse         a                                                                                        \n")
                      .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                      .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                      .append("                 ,   tz_member           d                                                                                        \n")
                      .append("                 ,   tz_subj             e                                                                                        \n")
                      .append("                 ,   tz_student            f                                                                                        \n")
                      .append("                 ,   tz_pfstudent        g                                                                                        \n")
                      .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                      .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                      .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                      .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                      .append("             AND     c.userid        = d.userid                                                                                   \n")
                      .append("             AND     c.subj          = e.subj                                                                                     \n")
                      .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                      .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                      .append("             AND     c.userid        = g.userid                                                                                   \n")
                      .append("             AND     b.coursecode    = g.coursecode                                                                               \n")
                      .append("             AND     b.courseyear    = g.courseyear                                                                               \n")
                      .append("             AND     b.courseseq     = g.courseseq                                                                                \n")
                      .append("             AND     c.status in ( 'RE', 'FE' )                                                                                   \n")
                      .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                      .append("             AND     c.year          = f.year(+)                                                                                  \n")
                      .append("             AND     c.subjseq       = f.subjseq(+)                                                                               \n")
                      .append("             GROUP BY b.grcode                                                                                                    \n")
                      .append("                 ,    b.coursecode                                                                                                \n")
                      .append("                 ,    b.courseyear                                                                                                \n")
                      .append("                 ,    b.courseseq                                                                                                 \n")
                      .append("                 ,    a.coursenm                                                                                                  \n")
                      .append("                 ,    d.userid                                                                                                    \n")
                      .append("                 ,    d.name                                                                                                      \n")
                      .append("                 ,    e.subj                                                                                                      \n")
                      .append("                 ,    e.subjnm                                                                                                    \n")
                      .append("                 ,    c.isrequired                                                                                                \n")
                      .append("                 ,    f.isgraduated                                                                                               \n")
                      .append("                 ,    f.score                                                                                                     \n")
                      .append("                 ,    f.tstep                                                                                                     \n")
                      .append("                 ,    f.mtest                                                                                                     \n")
                      .append("                 ,    f.ftest                                                                                                     \n")
                      .append("                 ,    f.report                                                                                                    \n")
                      .append("                 ,    f.act                                                                                                       \n")
                      .append("                 ,    f.etc1                                                                                                      \n")
                      .append("                 ,    f.etc2                                                                                                      \n")
                      .append("                 ,    f.avtstep                                                                                                   \n")
                      .append("                 ,    f.avmtest                                                                                                   \n")
                      .append("                 ,    f.avftest                                                                                                   \n")
                      .append("                 ,    f.avreport                                                                                                  \n")
                      .append("                 ,    f.avact                                                                                                     \n")
                      .append("                 ,    f.avetc1                                                                                                    \n")
                      .append("                 ,    f.avetc2                                                                                                    \n")
                      .append("                 ,    f.htest                                                                                                     \n")
                      .append("                 ,    f.avhtest                                                                                                   \n")
                      .append("                 ,    g.isgraduated                                                                                               \n")
                      .append("             ORDER BY name, isrequired desc                                                                                       \n")
                      .append("         )           a                                                                                                            \n")
                      .append("     ,   (                                                                                                                        \n")
                      .append("             SELECT  grcode                                                                                                       \n")
                      .append("                 ,   courseyear                                                                                                   \n")
                      .append("                 ,   courseseq                                                                                                    \n")
                      .append("                 ,   coursecode                                                                                                   \n")
                      .append("                 ,   userid                                                                                                       \n")
                      .append("                 ,   avg(score                                                               )   total                            \n")
                      .append("                 ,   sum(case when isrequired = 'N' and isgraduated = 'Y' then 1 else 0 end  )   graduated_n_count                \n")
                      .append("                 ,   max(subjnumforcomplete)                                                     requried_n_count                 \n")
                      .append("                 ,   sum(case when isrequired = 'Y' and isgraduated = 'Y' then 1 else 0 end  )   graduated_y_count                \n")
                      .append("                 ,   sum(decode(isrequired, 'Y', 1, 0))                                          requried_y_count                 \n")
                      .append("             FROM    (                                                                                                            \n")
                      .append("                         SELECT  b.grcode                                                                                         \n")
                      .append("                             ,   a.coursenm                                                                                       \n")
                      .append("                             ,   b.coursecode                                                                                     \n")
                      .append("                             ,   b.courseyear                                                                                     \n")
                      .append("                             ,   b.courseseq                                                                                      \n")
                      .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                      .append("                             ,   d.userid                                                                                         \n")
                      .append("                             ,   d.name                                                                                           \n")
                      .append("                             ,   e.subj                                                                                           \n")
                      .append("                             ,   e.subjnm                                                                                         \n")
                      .append("                             ,   c.isrequired                                                                                     \n")
                      .append("                             ,   f.isgraduated                                                                                    \n")
                      .append("                             ,   f.score                                                                                          \n")
                      .append("                             ,   f.tstep                                                                                          \n")
                      .append("                             ,   f.mtest                                                                                          \n")
                      .append("                             ,   f.ftest                                                                                          \n")
                      .append("                             ,   f.report                                                                                         \n")
                      .append("                             ,   f.act                                                                                            \n")
                      .append("                             ,   f.etc1                                                                                           \n")
                      .append("                             ,   f.etc2                                                                                           \n")
                      .append("                             ,   f.avtstep                                                                                        \n")
                      .append("                             ,   f.avmtest                                                                                        \n")
                      .append("                             ,   f.avftest                                                                                        \n")
                      .append("                             ,   f.avreport                                                                                       \n")
                      .append("                             ,   f.avact                                                                                          \n")
                      .append("                             ,   f.avetc1                                                                                         \n")
                      .append("                             ,   f.avetc2                                                                                         \n")
                      .append("                             ,   f.htest                                                                                          \n")
                      .append("                             ,   f.avhtest                                                                                        \n")
                      .append("                             ,   h.isgraduated course_graduated                                                                   \n")
                      .append("                         from    tz_pfcourse         a                                                                            \n")
                      .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                      .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                      .append("                             ,   tz_member           d                                                                            \n")
                      .append("                             ,   tz_subj             e                                                                            \n")
                      .append("                             ,   tz_student            f                                                                            \n")
                      .append("                             ,   (                                                                                                \n")
                      .append("                                     select userid, subj, max(score) max_score                                                    \n")
                      .append("                                     from tz_student                                                                                \n")
                      .append("                                     group by userid, subj                                                                        \n")
                      .append("                                 )                   g                                                                            \n")
                      .append("                             ,        tz_pfstudent   h                                                                            \n")
                      .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                      .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                      .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                      .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                      .append("                         and     c.userid        = d.userid                                                                       \n")
                      .append("                         and     c.subj          = e.subj                                                                         \n")
                      .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                      .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                      .append("                         and     c.userid        = h.userid                                                                       \n")
                      .append("                         and     b.coursecode    = h.coursecode                                                                   \n")
                      .append("                         and     b.courseyear    = h.courseyear                                                                   \n")
                      .append("                         and     b.courseseq     = h.courseseq                                                                    \n")
                      .append("                         and     c.status        = 'RV'                                                                           \n")
                      .append("                         and     f.userid        = g.userid                                                                       \n")
                      .append("                         and     f.subj          = g.subj                                                                         \n")
                      .append("                         and     g.max_score     = f.score                                                                        \n")
                      .append("                         group by b.grcode                                                                                        \n")
                      .append("                             ,    b.coursecode                                                                                    \n")
                      .append("                             ,    b.courseyear                                                                                    \n")
                      .append("                             ,    b.courseseq                                                                                     \n")
                      .append("                             ,    a.coursenm                                                                                      \n")
                      .append("                             ,    d.userid                                                                                        \n")
                      .append("                             ,    d.name                                                                                          \n")
                      .append("                             ,    e.subj                                                                                          \n")
                      .append("                             ,    e.subjnm                                                                                        \n")
                      .append("                             ,    c.isrequired                                                                                    \n")
                      .append("                             ,    f.isgraduated                                                                                   \n")
                      .append("                             ,    f.score                                                                                         \n")
                      .append("                             ,    f.tstep                                                                                         \n")
                      .append("                             ,    f.mtest                                                                                         \n")
                      .append("                             ,    f.ftest                                                                                         \n")
                      .append("                             ,    f.report                                                                                        \n")
                      .append("                             ,    f.act                                                                                           \n")
                      .append("                             ,    f.etc1                                                                                          \n")
                      .append("                             ,    f.etc2                                                                                          \n")
                      .append("                             ,    f.avtstep                                                                                       \n")
                      .append("                             ,    f.avmtest                                                                                       \n")
                      .append("                             ,    f.avftest                                                                                       \n")
                      .append("                             ,    f.avreport                                                                                      \n")
                      .append("                             ,    f.avact                                                                                         \n")
                      .append("                             ,    f.avetc1                                                                                        \n")
                      .append("                             ,    f.avetc2                                                                                        \n")
                      .append("                             ,    f.htest                                                                                         \n")
                      .append("                             ,    f.avhtest                                                                                       \n")
                      .append("                             ,    h.isgraduated                                                                                   \n")
                      .append("                         UNION                                                                                                    \n")
                      .append("                         select  b.grcode                                                                                         \n")
                      .append("                             ,   a.coursenm                                                                                       \n")
                      .append("                             ,   b.coursecode                                                                                     \n")
                      .append("                             ,   b.courseyear                                                                                     \n")
                      .append("                             ,   b.courseseq                                                                                      \n")
                      .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                      .append("                             ,   d.userid                                                                                         \n")
                      .append("                             ,   d.name                                                                                           \n")
                      .append("                             ,   e.subj                                                                                           \n")
                      .append("                             ,   e.subjnm                                                                                         \n")
                      .append("                             ,   c.isrequired                                                                                     \n")
                      .append("                             ,   f.isgraduated                                                                                    \n")
                      .append("                             ,   f.score                                                                                          \n")
                      .append("                             ,   f.tstep                                                                                          \n")
                      .append("                             ,   f.mtest                                                                                          \n")
                      .append("                             ,   f.ftest                                                                                          \n")
                      .append("                             ,   f.report                                                                                         \n")
                      .append("                             ,   f.act                                                                                            \n")
                      .append("                             ,   f.etc1                                                                                           \n")
                      .append("                             ,   f.etc2                                                                                           \n")
                      .append("                             ,   f.avtstep                                                                                        \n")
                      .append("                             ,   f.avmtest                                                                                        \n")
                      .append("                             ,   f.avftest                                                                                        \n")
                      .append("                             ,   f.avreport                                                                                       \n")
                      .append("                             ,   f.avact                                                                                          \n")
                      .append("                             ,   f.avetc1                                                                                         \n")
                      .append("                             ,   f.avetc2                                                                                         \n")
                      .append("                             ,   f.htest                                                                                          \n")
                      .append("                             ,   f.avhtest                                                                                        \n")
                      .append("                             ,   g.isgraduated course_graduated                                                                   \n")
                      .append("                         from    tz_pfcourse         a                                                                            \n")
                      .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                      .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                      .append("                             ,   tz_member           d                                                                            \n")
                      .append("                             ,   tz_subj             e                                                                            \n")
                      .append("                             ,   tz_student            f                                                                            \n")
                      .append("                             ,   tz_pfstudent        g                                                                            \n")
                      .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                      .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                      .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                      .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                      .append("                         and     c.userid        = d.userid                                                                       \n")
                      .append("                         and     c.subj          = e.subj                                                                         \n")
                      .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                      .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                      .append("                         and     c.userid        = g.userid                                                                       \n")
                      .append("                         and     b.coursecode    = g.coursecode                                                                   \n")
                      .append("                         and     b.courseyear    = g.courseyear                                                                   \n")
                      .append("                         and     b.courseseq     = g.courseseq                                                                    \n")
                      .append("                         and     c.status in ( 'RE', 'FE' )                                                                       \n")
                      .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                      .append("                         and     c.year          = f.year(+)                                                                      \n")
                      .append("                         and     c.subjseq       = f.subjseq(+)                                                                   \n")
                      .append("                         group by b.grcode                                                                                        \n")
                      .append("                             , b.coursecode                                                                                       \n")
                      .append("                             , b.courseyear                                                                                       \n")
                      .append("                             , b.courseseq                                                                                        \n")
                      .append("                             , a.coursenm                                                                                         \n")
                      .append("                             , d.userid                                                                                           \n")
                      .append("                             , d.name                                                                                             \n")
                      .append("                             , e.subj                                                                                             \n")
                      .append("                             , e.subjnm                                                                                           \n")
                      .append("                             , c.isrequired                                                                                       \n")
                      .append("                             , f.isgraduated                                                                                      \n")
                      .append("                             , f.score                                                                                            \n")
                      .append("                             , f.tstep                                                                                            \n")
                      .append("                             , f.mtest                                                                                            \n")
                      .append("                             , f.ftest                                                                                            \n")
                      .append("                             , f.report                                                                                           \n")
                      .append("                             , f.act                                                                                              \n")
                      .append("                             , f.etc1                                                                                             \n")
                      .append("                             , f.etc2                                                                                             \n")
                      .append("                             , f.avtstep                                                                                          \n")
                      .append("                             , f.avmtest                                                                                          \n")
                      .append("                             , f.avftest                                                                                          \n")
                      .append("                             , f.avreport                                                                                         \n")
                      .append("                             , f.avact                                                                                            \n")
                      .append("                             , f.avetc1                                                                                           \n")
                      .append("                             , f.avetc2                                                                                           \n")
                      .append("                             , f.htest                                                                                            \n")
                      .append("                             , f.avhtest                                                                                          \n")
                      .append("                             , g.isgraduated                                                                                      \n")
                      .append("                         order by name, isrequired desc                                                                           \n")
                      .append("                     )                                                                                                            \n")
                      .append("             group by grcode, courseyear, courseseq, coursecode, userid                                                           \n")
                      .append("         )                b                                                                                                       \n");
                 
                 
                 if ( (!v_sido.equals("") || !v_age.equals("") || !v_achievement.equals("") || !v_gender.equals("") || !v_gugun.equals("")) && !(v_orgcode.equals("") || v_orgcode.equals("ALL")) ) { 
                     sbSQL.append("  ,  vz_orgmember      c                                                                                                                                                       \n");
                 } else if ( (!v_sido.equals("") || !v_age.equals("") || !v_achievement.equals("") || !v_gender.equals("") || !v_gugun.equals("")) && (v_orgcode.equals("") || v_orgcode.equals("ALL")) ) {
                     sbSQL.append("  ,  vz_orgmember1     c                                                                                                                                                       \n");
                 }
                 
                 sbSQL.append("      ,  Tz_Member         d                                                                                                      \n")
                      .append(" WHERE   a.grcode        = " + StringManager.makeSQL(v_grcode         ) + "                                                       \n")
                      .append(" and     a.courseyear    = " + StringManager.makeSQL(v_courseyear     ) + "                                                       \n");

                  if ( !v_coursecode.equals("") ) { 
                      sbSQL.append(" and a.coursecode    = " + StringManager.makeSQL(v_coursecode    ) + "   \n");
                  }
                  
                  if ( !v_courseseq.equals("") ) { 
                      sbSQL.append(" and a.courseseq     = " + StringManager.makeSQL(v_courseseq     ) + "   \n");
                  }
                  
                  if ( v_isgraduated.equals("Y") ) {
                      sbSQL.append(" and a.course_graduated   = " + StringManager.makeSQL(v_isgraduated   ) + "   \n");
                  } else if ( v_isgraduated.equals("N") ) {
                      sbSQL.append(" and ( a.course_graduated   IS NULL OR a.course_graduated = " + StringManager.makeSQL(v_isgraduated   ) + " ) \n");
                  }
                  
                  if ( (!v_sido.equals("") || !v_age.equals("") || !v_achievement.equals("") || !v_gender.equals("") || !v_gugun.equals("") || !(v_orgcode.equals("ALL") || v_orgcode.equals("ALL")) ) ) { 
                      sbSQL.append("                          AND     a.userid        = c.userid(+)                                                       \n");
                  }
                  
                  if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL")) ) { 
                      sbSQL.append("                          AND     c.orgcode       = " + v_orgcode                                 + "                 \n");
                  }
                  
                  if ( !v_sido.equals("") ) {
                      sbSQL.append("             AND trim(c.sido)               = " + StringManager.makeSQL(v_sido          ) + "                                       \n");
                  }    

                  if ( !v_age.equals("") ) {
                      sbSQL.append("             AND trim(c.age)               = " + StringManager.makeSQL(v_age            ) + "                                       \n");               
                  }
                  
                  if ( !v_achievement.equals("") ) {
                      sbSQL.append("             AND trim(c.achievement)       = " + StringManager.makeSQL(v_achievement    ) + "                                       \n");               
                  }
                  
                  if ( !v_gender.equals("") ) {
                      sbSQL.append("             AND trim(c.gender)            = " + StringManager.makeSQL(v_gender         ) + "                                       \n");               
                  }
                  
                  if ( !v_gugun.equals("") ) {
                      sbSQL.append("             AND trim(c.gugun)             = " + StringManager.makeSQL(v_gugun          ) + "                                       \n");
                  }
                  
                  if ( !box.getString("p_sdate").equals("") && !box.getString("p_ldate").equals("") ) { // 기간선택
                      if ( box.getString("p_sdate").equals(box.getString("p_ldate")) ) {
                          sbSQL.append("          AND TO_CHAR(TO_DATE(d.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') \n");
                      } else {
                          sbSQL.append("          AND TO_CHAR(TO_DATE(d.indate, 'yyyymmddhh24miss'), 'yyyymmdd') BETWEEN TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') AND TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_ldate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') \n");
                      }
                  }
                  
                  sbSQL.append(" and a.userid = b.userid                                                                                                         \n")
                       .append(" and a.userid = d.userid                                                                                                         \n")
                       .append(" ORDER BY a.grcode, a.courseyear, a.coursecode, a.courseseq, a.name, a.userid                                                    \n");
                      
                 System.out.println("SQL.toString : \n" + sbSQL.toString() );
                 
                 ls = connMgr.executeQuery(sbSQL.toString());
         
                 while ( ls.next() ) { 
                     dbox = ls.getDataBox();
                     list.add(dbox);
                 }
             } catch ( Exception ex ) { 
                 ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
                 throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
             } finally { 
                 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
             }
             
             return list;
         }
         
         
         
         /**
         * 전문가 양성 과정 개인별 온라인 성적 상세보기 rowspan 카운트 
         */
         public ArrayList selectRowspanCount01(RequestBox box) throws Exception {
             DBConnectionManager connMgr         = null;
             ListSet             ls              = null;
             ArrayList           list            = null;
             StringBuffer        sbSQL           = new StringBuffer("");
              
             DataBox             dbox            = null;
              
             String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
             String              v_courseyear    = box.getStringDefault  ( "p_courseyear"   , ""    ); // 년도
             String              v_courseseq     = box.getStringDefault  ( "p_courseseq"    , ""    ); // 과목 기수
             String              v_coursecode    = box.getStringDefault  ( "p_coursecode"   , ""    ); // 과목&코스
             String              s_gadmin        = box.getSession        ( "gadmin"                 );
             String              v_branch        = box.getStringDefault  ( "p_branch"       , ""    );
             String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
             String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
              
            String              v_orderColumn   = box.getString( "p_orderColumn"  );  // 정렬할 컬럼명
            String              v_orderType     = box.getString( "p_orderType"    );  // 정렬할 순서
            
            try { 
                connMgr = new DBConnectionManager();
                list   = new ArrayList();    
                
                sbSQL.append(" SELECT  a.grcode                                                                                                                 \n")                                       
                     .append("     ,   a.coursecode                                                                                                             \n")
                     .append("     ,   a.courseyear                                                                                                             \n")
                     .append("     ,   a.courseseq                                                                                                              \n")
                     .append("     ,   a.userid                                                                                                                 \n")
                     .append("     ,   a.name                                                                                                                   \n")
                     .append("     ,   count(*)      count                                                                                                      \n")
                     .append(" FROM    (                                                                                                                        \n")
                     .append("          select  b.grcode                                                                                                        \n")
                     .append("                 ,   a.coursenm                                                                                                   \n")
                     .append("                 ,   b.coursecode                                                                                                 \n")
                     .append("                 ,   b.courseyear                                                                                                 \n")
                     .append("                 ,   b.courseseq                                                                                                  \n")
                     .append("                 ,   d.userid                                                                                                     \n")
                     .append("                 ,   d.name                                                                                                       \n")
                     .append("                 ,   e.subj                                                                                                       \n")
                     .append("                 ,   e.subjnm                                                                                                     \n")
                     .append("                 ,   c.isrequired                                                                                                 \n")
                     .append("                 ,   f.isgraduated                                                                                                \n")
                     .append("                 ,   f.score                                                                                                      \n")
                     .append("                 ,   f.tstep                                                                                                      \n")
                     .append("                 ,   f.mtest                                                                                                      \n")
                     .append("                 ,   f.ftest                                                                                                      \n")
                     .append("                 ,   f.report                                                                                                     \n")
                     .append("                 ,   f.act                                                                                                        \n")
                     .append("                 ,   f.etc1                                                                                                       \n")
                     .append("                 ,   f.etc2                                                                                                       \n")
                     .append("                 ,   f.avtstep                                                                                                    \n")
                     .append("                 ,   f.avmtest                                                                                                    \n")
                     .append("                 ,   f.avftest                                                                                                    \n")
                     .append("                 ,   f.avreport                                                                                                   \n")
                     .append("                 ,   f.avact                                                                                                      \n")
                     .append("                 ,   f.avetc1                                                                                                     \n")
                     .append("                 ,   f.avetc2                                                                                                     \n")
                     .append("                 ,   f.htest                                                                                                      \n")
                     .append("                 ,   f.avhtest                                                                                                    \n")
                     .append("                 ,   h.isgraduated       course_graduated                                                                         \n")
                     .append("             FROM    tz_pfcourse         a                                                                                        \n")
                     .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                     .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                     .append("                 ,   tz_member           d                                                                                        \n")
                     .append("                 ,   tz_subj             e                                                                                        \n")
                     .append("                 ,   tz_student          f                                                                                        \n")
                     .append("                 ,   (                                                                                                            \n")
                     .append("                         select  i.userid, i.subj, max(i.score) max_score                                                         \n")
                     .append("                         from    tz_student  i                                                                                    \n")
                     .append("                             ,   tz_subjseq  j                                                                                    \n")
                     .append("                         where   i.subj      = j.subj                                                                             \n")
                     .append("                         and     i.subjseq   = j.subjseq                                                                          \n")
                     .append("                         and     i.year      = j.year                                                                             \n")
                     .append("                         and     j.isblended = 'N'                                                                                \n")
                     .append("                         and     j.isexpert  = 'N'                                                                                \n")
                     .append("                         group by i.userid, i.subj                                                                                \n")
                     .append("                     )                   g                                                                                        \n")
                     .append("                 ,   tz_pfstudent        h                                                                                        \n")
                     .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                     .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                     .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                     .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                     .append("             AND     c.userid        = d.userid                                                                                   \n")
                     .append("             AND     c.subj          = e.subj                                                                                     \n")
                     .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                     .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                     .append("             AND     c.userid        = h.userid                                                                                   \n")
                     .append("             AND     b.coursecode    = h.coursecode                                                                               \n")
                     .append("             AND     b.courseyear    = h.courseyear                                                                               \n")
                     .append("             AND     b.courseseq     = h.courseseq                                                                                \n")
                     .append("             AND     c.status        = 'RV'                                                                                       \n")
                     .append("             AND     f.userid        = g.userid                                                                                   \n")
                     .append("             AND     f.subj          = g.subj                                                                                     \n")
                     .append("             AND     g.max_score     = f.score                                                                                    \n")
                     .append("             GROUP BY b.grcode                                                                                                    \n")
                     .append("                 ,    b.coursecode                                                                                                \n")
                     .append("                 ,    b.courseyear                                                                                                \n")
                     .append("                 ,    b.courseseq                                                                                                 \n")
                     .append("                 ,    a.coursenm                                                                                                  \n")
                     .append("                 ,    d.userid                                                                                                    \n")
                     .append("                 ,    d.name                                                                                                      \n")
                     .append("                 ,    e.subj                                                                                                      \n")
                     .append("                 ,    e.subjnm                                                                                                    \n")
                     .append("                 ,    c.isrequired                                                                                                \n")
                     .append("                 ,    f.isgraduated                                                                                               \n")
                     .append("                 ,    f.score                                                                                                     \n")
                     .append("                 ,    f.tstep                                                                                                     \n")
                     .append("                 ,    f.mtest                                                                                                     \n")
                     .append("                 ,    f.ftest                                                                                                     \n")
                     .append("                 ,    f.report                                                                                                    \n")
                     .append("                 ,    f.act                                                                                                       \n")
                     .append("                 ,    f.etc1                                                                                                      \n")
                     .append("                 ,    f.etc2                                                                                                      \n")
                     .append("                 ,    f.avtstep                                                                                                   \n")
                     .append("                 ,    f.avmtest                                                                                                   \n")
                     .append("                 ,    f.avftest                                                                                                   \n")
                     .append("                 ,    f.avreport                                                                                                  \n")
                     .append("                 ,    f.avact                                                                                                     \n")
                     .append("                 ,    f.avetc1                                                                                                    \n")
                     .append("                 ,    f.avetc2                                                                                                    \n")
                     .append("                 ,    f.htest                                                                                                     \n")
                     .append("                 ,    f.avhtest                                                                                                   \n")
                     .append("                 ,    h.isgraduated                                                                                               \n")
                     .append("             UNION                                                                                                                \n")
                     .append("             SELECT  b.grcode                                                                                                     \n")
                     .append("                 ,   a.coursenm                                                                                                   \n")
                     .append("                 ,   b.coursecode                                                                                                 \n")
                     .append("                 ,   b.courseyear                                                                                                 \n")
                     .append("                 ,   b.courseseq                                                                                                  \n")
                     .append("                 ,   d.userid                                                                                                     \n")
                     .append("                 ,   d.name                                                                                                       \n")
                     .append("                 ,   e.subj                                                                                                       \n")
                     .append("                 ,   e.subjnm                                                                                                     \n")
                     .append("                 ,   c.isrequired                                                                                                 \n")
                     .append("                 ,   f.isgraduated                                                                                                \n")
                     .append("                 ,   f.score                                                                                                      \n")
                     .append("                 ,   f.tstep                                                                                                      \n")
                     .append("                 ,   f.mtest                                                                                                      \n")
                     .append("                 ,   f.ftest                                                                                                      \n")
                     .append("                 ,   f.report                                                                                                     \n")
                     .append("                 ,   f.act                                                                                                        \n")
                     .append("                 ,   f.etc1                                                                                                       \n")
                     .append("                 ,   f.etc2                                                                                                       \n")
                     .append("                 ,   f.avtstep                                                                                                    \n")
                     .append("                 ,   f.avmtest                                                                                                    \n")
                     .append("                 ,   f.avftest                                                                                                    \n")
                     .append("                 ,   f.avreport                                                                                                   \n")
                     .append("                 ,   f.avact                                                                                                      \n")
                     .append("                 ,   f.avetc1                                                                                                     \n")
                     .append("                 ,   f.avetc2                                                                                                     \n")
                     .append("                 ,   f.htest                                                                                                      \n")
                     .append("                 ,   f.avhtest                                                                                                    \n")
                     .append("                 ,   g.isgraduated       course_graduated                                                                         \n")
                     .append("             FROM    tz_pfcourse         a                                                                                        \n")
                     .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                     .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                     .append("                 ,   tz_member           d                                                                                        \n")
                     .append("                 ,   tz_subj             e                                                                                        \n")
                     .append("                 ,   tz_student            f                                                                                        \n")
                     .append("                 ,   tz_pfstudent        g                                                                                        \n")
                     .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                     .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                     .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                     .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                     .append("             AND     c.userid        = d.userid                                                                                   \n")
                     .append("             AND     c.subj          = e.subj                                                                                     \n")
                     .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                     .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                     .append("             AND     c.userid        = g.userid                                                                                   \n")
                     .append("             AND     b.coursecode    = g.coursecode                                                                               \n")
                     .append("             AND     b.courseyear    = g.courseyear                                                                               \n")
                     .append("             AND     b.courseseq     = g.courseseq                                                                                \n")
                     .append("             AND     c.status in ( 'RE', 'FE' )                                                                                   \n")
                     .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                     .append("             AND     c.year          = f.year(+)                                                                                  \n")
                     .append("             AND     c.subjseq       = f.subjseq(+)                                                                               \n")
                     .append("             GROUP BY b.grcode                                                                                                    \n")
                     .append("                 ,    b.coursecode                                                                                                \n")
                     .append("                 ,    b.courseyear                                                                                                \n")
                     .append("                 ,    b.courseseq                                                                                                 \n")
                     .append("                 ,    a.coursenm                                                                                                  \n")
                     .append("                 ,    d.userid                                                                                                    \n")
                     .append("                 ,    d.name                                                                                                      \n")
                     .append("                 ,    e.subj                                                                                                      \n")
                     .append("                 ,    e.subjnm                                                                                                    \n")
                     .append("                 ,    c.isrequired                                                                                                \n")
                     .append("                 ,    f.isgraduated                                                                                               \n")
                     .append("                 ,    f.score                                                                                                     \n")
                     .append("                 ,    f.tstep                                                                                                     \n")
                     .append("                 ,    f.mtest                                                                                                     \n")
                     .append("                 ,    f.ftest                                                                                                     \n")
                     .append("                 ,    f.report                                                                                                    \n")
                     .append("                 ,    f.act                                                                                                       \n")
                     .append("                 ,    f.etc1                                                                                                      \n")
                     .append("                 ,    f.etc2                                                                                                      \n")
                     .append("                 ,    f.avtstep                                                                                                   \n")
                     .append("                 ,    f.avmtest                                                                                                   \n")
                     .append("                 ,    f.avftest                                                                                                   \n")
                     .append("                 ,    f.avreport                                                                                                  \n")
                     .append("                 ,    f.avact                                                                                                     \n")
                     .append("                 ,    f.avetc1                                                                                                    \n")
                     .append("                 ,    f.avetc2                                                                                                    \n")
                     .append("                 ,    f.htest                                                                                                     \n")
                     .append("                 ,    f.avhtest                                                                                                   \n")
                     .append("                 ,    g.isgraduated                                                                                               \n")
                     .append("             ORDER BY name, isrequired desc                                                                                       \n")
                     .append("         )           a                                                                                                            \n")
                     .append("     ,   (                                                                                                                        \n")
                     .append("             SELECT  grcode                                                                                                       \n")
                     .append("                 ,   courseyear                                                                                                   \n")
                     .append("                 ,   courseseq                                                                                                    \n")
                     .append("                 ,   coursecode                                                                                                   \n")
                     .append("                 ,   userid                                                                                                       \n")
                     .append("                 ,   avg(score                                                               )   total                            \n")
                     .append("                 ,   sum(case when isrequired = 'N' and isgraduated = 'Y' then 1 else 0 end  )   graduated_n_count                \n")
                     .append("                 ,   max(subjnumforcomplete)                                                     requried_n_count                 \n")
                     .append("                 ,   sum(case when isrequired = 'Y' and isgraduated = 'Y' then 1 else 0 end  )   graduated_y_count                \n")
                     .append("                 ,   sum(decode(isrequired, 'Y', 1, 0))                                          requried_y_count                 \n")
                     .append("             FROM    (                                                                                                            \n")
                     .append("                         SELECT  b.grcode                                                                                         \n")
                     .append("                             ,   a.coursenm                                                                                       \n")
                     .append("                             ,   b.coursecode                                                                                     \n")
                     .append("                             ,   b.courseyear                                                                                     \n")
                     .append("                             ,   b.courseseq                                                                                      \n")
                     .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                     .append("                             ,   d.userid                                                                                         \n")
                     .append("                             ,   d.name                                                                                           \n")
                     .append("                             ,   e.subj                                                                                           \n")
                     .append("                             ,   e.subjnm                                                                                         \n")
                     .append("                             ,   c.isrequired                                                                                     \n")
                     .append("                             ,   f.isgraduated                                                                                    \n")
                     .append("                             ,   f.score                                                                                          \n")
                     .append("                             ,   f.tstep                                                                                          \n")
                     .append("                             ,   f.mtest                                                                                          \n")
                     .append("                             ,   f.ftest                                                                                          \n")
                     .append("                             ,   f.report                                                                                         \n")
                     .append("                             ,   f.act                                                                                            \n")
                     .append("                             ,   f.etc1                                                                                           \n")
                     .append("                             ,   f.etc2                                                                                           \n")
                     .append("                             ,   f.avtstep                                                                                        \n")
                     .append("                             ,   f.avmtest                                                                                        \n")
                     .append("                             ,   f.avftest                                                                                        \n")
                     .append("                             ,   f.avreport                                                                                       \n")
                     .append("                             ,   f.avact                                                                                          \n")
                     .append("                             ,   f.avetc1                                                                                         \n")
                     .append("                             ,   f.avetc2                                                                                         \n")
                     .append("                             ,   f.htest                                                                                          \n")
                     .append("                             ,   f.avhtest                                                                                        \n")
                     .append("                             ,   h.isgraduated course_graduated                                                                   \n")
                     .append("                         from    tz_pfcourse         a                                                                            \n")
                     .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                     .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                     .append("                             ,   tz_member           d                                                                            \n")
                     .append("                             ,   tz_subj             e                                                                            \n")
                     .append("                             ,   tz_student            f                                                                            \n")
                     .append("                             ,   (                                                                                                \n")
                     .append("                                     select userid, subj, max(score) max_score                                                    \n")
                     .append("                                     from tz_student                                                                                \n")
                     .append("                                     group by userid, subj                                                                        \n")
                     .append("                                 )                   g                                                                            \n")
                     .append("                             ,        tz_pfstudent   h                                                                            \n")
                     .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                     .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                     .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                     .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                     .append("                         and     c.userid        = d.userid                                                                       \n")
                     .append("                         and     c.subj          = e.subj                                                                         \n")
                     .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                     .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                     .append("                         and     c.userid        = h.userid                                                                       \n")
                     .append("                         and     b.coursecode    = h.coursecode                                                                   \n")
                     .append("                         and     b.courseyear    = h.courseyear                                                                   \n")
                     .append("                         and     b.courseseq     = h.courseseq                                                                    \n")
                     .append("                         and     c.status        = 'RV'                                                                           \n")
                     .append("                         and     f.userid        = g.userid                                                                       \n")
                     .append("                         and     f.subj          = g.subj                                                                         \n")
                     .append("                         and     g.max_score     = f.score                                                                        \n")
                     .append("                         group by b.grcode                                                                                        \n")
                     .append("                             ,    b.coursecode                                                                                    \n")
                     .append("                             ,    b.courseyear                                                                                    \n")
                     .append("                             ,    b.courseseq                                                                                     \n")
                     .append("                             ,    a.coursenm                                                                                      \n")
                     .append("                             ,    d.userid                                                                                        \n")
                     .append("                             ,    d.name                                                                                          \n")
                     .append("                             ,    e.subj                                                                                          \n")
                     .append("                             ,    e.subjnm                                                                                        \n")
                     .append("                             ,    c.isrequired                                                                                    \n")
                     .append("                             ,    f.isgraduated                                                                                   \n")
                     .append("                             ,    f.score                                                                                         \n")
                     .append("                             ,    f.tstep                                                                                         \n")
                     .append("                             ,    f.mtest                                                                                         \n")
                     .append("                             ,    f.ftest                                                                                         \n")
                     .append("                             ,    f.report                                                                                        \n")
                     .append("                             ,    f.act                                                                                           \n")
                     .append("                             ,    f.etc1                                                                                          \n")
                     .append("                             ,    f.etc2                                                                                          \n")
                     .append("                             ,    f.avtstep                                                                                       \n")
                     .append("                             ,    f.avmtest                                                                                       \n")
                     .append("                             ,    f.avftest                                                                                       \n")
                     .append("                             ,    f.avreport                                                                                      \n")
                     .append("                             ,    f.avact                                                                                         \n")
                     .append("                             ,    f.avetc1                                                                                        \n")
                     .append("                             ,    f.avetc2                                                                                        \n")
                     .append("                             ,    f.htest                                                                                         \n")
                     .append("                             ,    f.avhtest                                                                                       \n")
                     .append("                             ,    h.isgraduated                                                                                   \n")
                     .append("                         UNION                                                                                                    \n")
                     .append("                         select  b.grcode                                                                                         \n")
                     .append("                             ,   a.coursenm                                                                                       \n")
                     .append("                             ,   b.coursecode                                                                                     \n")
                     .append("                             ,   b.courseyear                                                                                     \n")
                     .append("                             ,   b.courseseq                                                                                      \n")
                     .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                     .append("                             ,   d.userid                                                                                         \n")
                     .append("                             ,   d.name                                                                                           \n")
                     .append("                             ,   e.subj                                                                                           \n")
                     .append("                             ,   e.subjnm                                                                                         \n")
                     .append("                             ,   c.isrequired                                                                                     \n")
                     .append("                             ,   f.isgraduated                                                                                    \n")
                     .append("                             ,   f.score                                                                                          \n")
                     .append("                             ,   f.tstep                                                                                          \n")
                     .append("                             ,   f.mtest                                                                                          \n")
                     .append("                             ,   f.ftest                                                                                          \n")
                     .append("                             ,   f.report                                                                                         \n")
                     .append("                             ,   f.act                                                                                            \n")
                     .append("                             ,   f.etc1                                                                                           \n")
                     .append("                             ,   f.etc2                                                                                           \n")
                     .append("                             ,   f.avtstep                                                                                        \n")
                     .append("                             ,   f.avmtest                                                                                        \n")
                     .append("                             ,   f.avftest                                                                                        \n")
                     .append("                             ,   f.avreport                                                                                       \n")
                     .append("                             ,   f.avact                                                                                          \n")
                     .append("                             ,   f.avetc1                                                                                         \n")
                     .append("                             ,   f.avetc2                                                                                         \n")
                     .append("                             ,   f.htest                                                                                          \n")
                     .append("                             ,   f.avhtest                                                                                        \n")
                     .append("                             ,   g.isgraduated course_graduated                                                                   \n")
                     .append("                         from    tz_pfcourse         a                                                                            \n")
                     .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                     .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                     .append("                             ,   tz_member           d                                                                            \n")
                     .append("                             ,   tz_subj             e                                                                            \n")
                     .append("                             ,   tz_student            f                                                                            \n")
                     .append("                             ,   tz_pfstudent        g                                                                            \n")
                     .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                     .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                     .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                     .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                     .append("                         and     c.userid        = d.userid                                                                       \n")
                     .append("                         and     c.subj          = e.subj                                                                         \n")
                     .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                     .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                     .append("                         and     c.userid        = g.userid                                                                       \n")
                     .append("                         and     b.coursecode    = g.coursecode                                                                   \n")
                     .append("                         and     b.courseyear    = g.courseyear                                                                   \n")
                     .append("                         and     b.courseseq     = g.courseseq                                                                    \n")
                     .append("                         and     c.status in ( 'RE', 'FE' )                                                                       \n")
                     .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                     .append("                         and     c.year          = f.year(+)                                                                      \n")
                     .append("                         and     c.subjseq       = f.subjseq(+)                                                                   \n")
                     .append("                         group by b.grcode                                                                                        \n")
                     .append("                             , b.coursecode                                                                                       \n")
                     .append("                             , b.courseyear                                                                                       \n")
                     .append("                             , b.courseseq                                                                                        \n")
                     .append("                             , a.coursenm                                                                                         \n")
                     .append("                             , d.userid                                                                                           \n")
                     .append("                             , d.name                                                                                             \n")
                     .append("                             , e.subj                                                                                             \n")
                     .append("                             , e.subjnm                                                                                           \n")
                     .append("                             , c.isrequired                                                                                       \n")
                     .append("                             , f.isgraduated                                                                                      \n")
                     .append("                             , f.score                                                                                            \n")
                     .append("                             , f.tstep                                                                                            \n")
                     .append("                             , f.mtest                                                                                            \n")
                     .append("                             , f.ftest                                                                                            \n")
                     .append("                             , f.report                                                                                           \n")
                     .append("                             , f.act                                                                                              \n")
                     .append("                             , f.etc1                                                                                             \n")
                     .append("                             , f.etc2                                                                                             \n")
                     .append("                             , f.avtstep                                                                                          \n")
                     .append("                             , f.avmtest                                                                                          \n")
                     .append("                             , f.avftest                                                                                          \n")
                     .append("                             , f.avreport                                                                                         \n")
                     .append("                             , f.avact                                                                                            \n")
                     .append("                             , f.avetc1                                                                                           \n")
                     .append("                             , f.avetc2                                                                                           \n")
                     .append("                             , f.htest                                                                                            \n")
                     .append("                             , f.avhtest                                                                                          \n")
                     .append("                             , g.isgraduated                                                                                      \n")
                     .append("                         order by name, isrequired desc                                                                           \n")
                     .append("                     )                                                                                                            \n")
                     .append("             group by grcode, courseyear, courseseq, coursecode, userid                                                           \n")
                     .append("         )                b                                                                                                       \n");
                
                if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                    sbSQL.append("  ,  vz_orgmember     c                                                                                                       \n");
                }
                
                sbSQL.append(" WHERE   a.grcode        = " + StringManager.makeSQL(v_grcode         ) + "                                                       \n")
                     .append(" and     a.courseyear    = " + StringManager.makeSQL(v_courseyear     ) + "                                                       \n");

                 if ( !v_coursecode.equals("") ) { 
                     sbSQL.append(" and a.coursecode    = " + StringManager.makeSQL(v_coursecode    ) + "   \n");
                 }
                 
                 if ( !v_courseseq.equals("") ) { 
                     sbSQL.append(" and a.courseseq     = " + StringManager.makeSQL(v_courseseq     ) + "   \n");
                 }
                 
                 
                 if ( v_isgraduated.equals("Y") ) {
                     sbSQL.append(" and a.course_graduated   = " + StringManager.makeSQL(v_isgraduated   ) + "   \n");
                 } else if ( v_isgraduated.equals("N") ) {
                     sbSQL.append(" and ( a.course_graduated   IS NULL OR a.course_graduated = " + StringManager.makeSQL(v_isgraduated   ) + " ) \n");
                 }
                 
                 if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                     sbSQL.append(" AND     c.orgcode       = " + v_orgcode + "                             \n")
                          .append(" and     a.userid        = c.userid                                      \n");
                 }
                 
                 sbSQL.append(" and a.userid = b.userid                                                                                                         \n")
                      .append(" GROUP BY a.grcode, a.courseyear, a.coursecode, a.courseseq, a.name, a.userid                                                    \n")
                      .append(" ORDER BY a.grcode, a.courseyear, a.coursecode, a.courseseq, a.name, a.userid                                                    \n");
                 
                 System.out.println("sql : " + sbSQL.toString());
                       
                 ls = connMgr.executeQuery(sbSQL.toString());
       
                 while ( ls.next() ) { 
                     dbox = ls.getDataBox();
                     list.add(dbox);
                 }
            } catch ( Exception ex ) { 
                ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
                throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
            } finally { 
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
                   
            return list;
         }
         
         
         /**
         * 전문가 양성 과정 개인별 온라인 성적 상세보기 rowspan 카운트 
         */
         public ArrayList selectRowspanCount02(RequestBox box) throws Exception {
             DBConnectionManager connMgr         = null;
             ListSet             ls              = null;
             ArrayList           list            = null;
             StringBuffer        sbSQL           = new StringBuffer("");
              
             DataBox             dbox            = null;
              
             String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
             String              v_courseyear    = box.getStringDefault  ( "p_courseyear"   , ""    ); // 년도
             String              v_courseseq     = box.getStringDefault  ( "p_courseseq"    , ""    ); // 과목 기수
             String              v_coursecode    = box.getStringDefault  ( "p_coursecode"   , ""    ); // 과목&코스
             String              s_gadmin        = box.getSession        ( "gadmin"                 );
             String              v_branch        = box.getStringDefault  ( "p_branch"       , ""    );
             String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
             String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
              
            String              v_orderColumn   = box.getString( "p_orderColumn"  );  // 정렬할 컬럼명
            String              v_orderType     = box.getString( "p_orderType"    );  // 정렬할 순서
            
            try { 
                connMgr = new DBConnectionManager();
                list   = new ArrayList();    
                
                sbSQL.append(" SELECT  a.grcode                                                                                                                 \n")                                       
                     .append("     ,   a.coursecode                                                                                                             \n")
                     .append("     ,   a.courseyear                                                                                                             \n")
                     .append("     ,   a.courseseq                                                                                                              \n")
                     .append("     ,   a.userid                                                                                                                 \n")
                     .append("     ,   a.name                                                                                                                   \n")
                     .append("     ,   count(*)      count                                                                                                      \n")
                     .append(" FROM    (                                                                                                                        \n")
                     .append("          select  b.grcode                                                                                                        \n")
                     .append("                 ,   a.coursenm                                                                                                   \n")
                     .append("                 ,   b.coursecode                                                                                                 \n")
                     .append("                 ,   b.courseyear                                                                                                 \n")
                     .append("                 ,   b.courseseq                                                                                                  \n")
                     .append("                 ,   d.userid                                                                                                     \n")
                     .append("                 ,   d.name                                                                                                       \n")
                     .append("                 ,   e.subj                                                                                                       \n")
                     .append("                 ,   e.subjnm                                                                                                     \n")
                     .append("                 ,   c.isrequired                                                                                                 \n")
                     .append("                 ,   f.isgraduated                                                                                                \n")
                     .append("                 ,   f.score                                                                                                      \n")
                     .append("                 ,   f.tstep                                                                                                      \n")
                     .append("                 ,   f.mtest                                                                                                      \n")
                     .append("                 ,   f.ftest                                                                                                      \n")
                     .append("                 ,   f.report                                                                                                     \n")
                     .append("                 ,   f.act                                                                                                        \n")
                     .append("                 ,   f.etc1                                                                                                       \n")
                     .append("                 ,   f.etc2                                                                                                       \n")
                     .append("                 ,   f.avtstep                                                                                                    \n")
                     .append("                 ,   f.avmtest                                                                                                    \n")
                     .append("                 ,   f.avftest                                                                                                    \n")
                     .append("                 ,   f.avreport                                                                                                   \n")
                     .append("                 ,   f.avact                                                                                                      \n")
                     .append("                 ,   f.avetc1                                                                                                     \n")
                     .append("                 ,   f.avetc2                                                                                                     \n")
                     .append("                 ,   f.htest                                                                                                      \n")
                     .append("                 ,   f.avhtest                                                                                                    \n")
                     .append("                 ,   h.isgraduated       course_graduated                                                                         \n")
                     .append("             FROM    tz_pfcourse         a                                                                                        \n")
                     .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                     .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                     .append("                 ,   tz_member           d                                                                                        \n")
                     .append("                 ,   tz_subj             e                                                                                        \n")
                     .append("                 ,   tz_student          f                                                                                        \n")
                     .append("                 ,   (                                                                                                            \n")
                     .append("                         select  i.userid, i.subj, max(i.score) max_score                                                         \n")
                     .append("                         from    tz_student  i                                                                                    \n")
                     .append("                             ,   tz_subjseq  j                                                                                    \n")
                     .append("                         where   i.subj      = j.subj                                                                             \n")
                     .append("                         and     i.subjseq   = j.subjseq                                                                          \n")
                     .append("                         and     i.year      = j.year                                                                             \n")
                     .append("                         and     j.isblended = 'N'                                                                                \n")
                     .append("                         and     j.isexpert  = 'N'                                                                                \n")
                     .append("                         group by i.userid, i.subj                                                                                \n")
                     .append("                     )                   g                                                                                        \n")
                     .append("                 ,   tz_pfstudent        h                                                                                        \n")
                     .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                     .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                     .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                     .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                     .append("             AND     c.userid        = d.userid                                                                                   \n")
                     .append("             AND     c.subj          = e.subj                                                                                     \n")
                     .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                     .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                     .append("             AND     c.userid        = h.userid                                                                                   \n")
                     .append("             AND     b.coursecode    = h.coursecode                                                                               \n")
                     .append("             AND     b.courseyear    = h.courseyear                                                                               \n")
                     .append("             AND     b.courseseq     = h.courseseq                                                                                \n")
                     .append("             AND     c.status        = 'RV'                                                                                       \n")
                     .append("             AND     f.userid        = g.userid                                                                                   \n")
                     .append("             AND     f.subj          = g.subj                                                                                     \n")
                     .append("             AND     g.max_score     = f.score                                                                                    \n")
                     .append("             GROUP BY b.grcode                                                                                                    \n")
                     .append("                 ,    b.coursecode                                                                                                \n")
                     .append("                 ,    b.courseyear                                                                                                \n")
                     .append("                 ,    b.courseseq                                                                                                 \n")
                     .append("                 ,    a.coursenm                                                                                                  \n")
                     .append("                 ,    d.userid                                                                                                    \n")
                     .append("                 ,    d.name                                                                                                      \n")
                     .append("                 ,    e.subj                                                                                                      \n")
                     .append("                 ,    e.subjnm                                                                                                    \n")
                     .append("                 ,    c.isrequired                                                                                                \n")
                     .append("                 ,    f.isgraduated                                                                                               \n")
                     .append("                 ,    f.score                                                                                                     \n")
                     .append("                 ,    f.tstep                                                                                                     \n")
                     .append("                 ,    f.mtest                                                                                                     \n")
                     .append("                 ,    f.ftest                                                                                                     \n")
                     .append("                 ,    f.report                                                                                                    \n")
                     .append("                 ,    f.act                                                                                                       \n")
                     .append("                 ,    f.etc1                                                                                                      \n")
                     .append("                 ,    f.etc2                                                                                                      \n")
                     .append("                 ,    f.avtstep                                                                                                   \n")
                     .append("                 ,    f.avmtest                                                                                                   \n")
                     .append("                 ,    f.avftest                                                                                                   \n")
                     .append("                 ,    f.avreport                                                                                                  \n")
                     .append("                 ,    f.avact                                                                                                     \n")
                     .append("                 ,    f.avetc1                                                                                                    \n")
                     .append("                 ,    f.avetc2                                                                                                    \n")
                     .append("                 ,    f.htest                                                                                                     \n")
                     .append("                 ,    f.avhtest                                                                                                   \n")
                     .append("                 ,    h.isgraduated                                                                                               \n")
                     .append("             UNION                                                                                                                \n")
                     .append("             SELECT  b.grcode                                                                                                     \n")
                     .append("                 ,   a.coursenm                                                                                                   \n")
                     .append("                 ,   b.coursecode                                                                                                 \n")
                     .append("                 ,   b.courseyear                                                                                                 \n")
                     .append("                 ,   b.courseseq                                                                                                  \n")
                     .append("                 ,   d.userid                                                                                                     \n")
                     .append("                 ,   d.name                                                                                                       \n")
                     .append("                 ,   e.subj                                                                                                       \n")
                     .append("                 ,   e.subjnm                                                                                                     \n")
                     .append("                 ,   c.isrequired                                                                                                 \n")
                     .append("                 ,   f.isgraduated                                                                                                \n")
                     .append("                 ,   f.score                                                                                                      \n")
                     .append("                 ,   f.tstep                                                                                                      \n")
                     .append("                 ,   f.mtest                                                                                                      \n")
                     .append("                 ,   f.ftest                                                                                                      \n")
                     .append("                 ,   f.report                                                                                                     \n")
                     .append("                 ,   f.act                                                                                                        \n")
                     .append("                 ,   f.etc1                                                                                                       \n")
                     .append("                 ,   f.etc2                                                                                                       \n")
                     .append("                 ,   f.avtstep                                                                                                    \n")
                     .append("                 ,   f.avmtest                                                                                                    \n")
                     .append("                 ,   f.avftest                                                                                                    \n")
                     .append("                 ,   f.avreport                                                                                                   \n")
                     .append("                 ,   f.avact                                                                                                      \n")
                     .append("                 ,   f.avetc1                                                                                                     \n")
                     .append("                 ,   f.avetc2                                                                                                     \n")
                     .append("                 ,   f.htest                                                                                                      \n")
                     .append("                 ,   f.avhtest                                                                                                    \n")
                     .append("                 ,   g.isgraduated       course_graduated                                                                         \n")
                     .append("             FROM    tz_pfcourse         a                                                                                        \n")
                     .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                     .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                     .append("                 ,   tz_member           d                                                                                        \n")
                     .append("                 ,   tz_subj             e                                                                                        \n")
                     .append("                 ,   tz_student            f                                                                                        \n")
                     .append("                 ,   tz_pfstudent        g                                                                                        \n")
                     .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                     .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                     .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                     .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                     .append("             AND     c.userid        = d.userid                                                                                   \n")
                     .append("             AND     c.subj          = e.subj                                                                                     \n")
                     .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                     .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                     .append("             AND     c.userid        = g.userid                                                                                   \n")
                     .append("             AND     b.coursecode    = g.coursecode                                                                               \n")
                     .append("             AND     b.courseyear    = g.courseyear                                                                               \n")
                     .append("             AND     b.courseseq     = g.courseseq                                                                                \n")
                     .append("             AND     c.status in ( 'RE', 'FE' )                                                                                   \n")
                     .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                     .append("             AND     c.year          = f.year(+)                                                                                  \n")
                     .append("             AND     c.subjseq       = f.subjseq(+)                                                                               \n")
                     .append("             GROUP BY b.grcode                                                                                                    \n")
                     .append("                 ,    b.coursecode                                                                                                \n")
                     .append("                 ,    b.courseyear                                                                                                \n")
                     .append("                 ,    b.courseseq                                                                                                 \n")
                     .append("                 ,    a.coursenm                                                                                                  \n")
                     .append("                 ,    d.userid                                                                                                    \n")
                     .append("                 ,    d.name                                                                                                      \n")
                     .append("                 ,    e.subj                                                                                                      \n")
                     .append("                 ,    e.subjnm                                                                                                    \n")
                     .append("                 ,    c.isrequired                                                                                                \n")
                     .append("                 ,    f.isgraduated                                                                                               \n")
                     .append("                 ,    f.score                                                                                                     \n")
                     .append("                 ,    f.tstep                                                                                                     \n")
                     .append("                 ,    f.mtest                                                                                                     \n")
                     .append("                 ,    f.ftest                                                                                                     \n")
                     .append("                 ,    f.report                                                                                                    \n")
                     .append("                 ,    f.act                                                                                                       \n")
                     .append("                 ,    f.etc1                                                                                                      \n")
                     .append("                 ,    f.etc2                                                                                                      \n")
                     .append("                 ,    f.avtstep                                                                                                   \n")
                     .append("                 ,    f.avmtest                                                                                                   \n")
                     .append("                 ,    f.avftest                                                                                                   \n")
                     .append("                 ,    f.avreport                                                                                                  \n")
                     .append("                 ,    f.avact                                                                                                     \n")
                     .append("                 ,    f.avetc1                                                                                                    \n")
                     .append("                 ,    f.avetc2                                                                                                    \n")
                     .append("                 ,    f.htest                                                                                                     \n")
                     .append("                 ,    f.avhtest                                                                                                   \n")
                     .append("                 ,    g.isgraduated                                                                                               \n")
                     .append("             ORDER BY name, isrequired desc                                                                                       \n")
                     .append("         )           a                                                                                                            \n")
                     .append("     ,   (                                                                                                                        \n")
                     .append("             SELECT  grcode                                                                                                       \n")
                     .append("                 ,   courseyear                                                                                                   \n")
                     .append("                 ,   courseseq                                                                                                    \n")
                     .append("                 ,   coursecode                                                                                                   \n")
                     .append("                 ,   userid                                                                                                       \n")
                     .append("                 ,   avg(score                                                               )   total                            \n")
                     .append("                 ,   sum(case when isrequired = 'N' and isgraduated = 'Y' then 1 else 0 end  )   graduated_n_count                \n")
                     .append("                 ,   max(subjnumforcomplete)                                                     requried_n_count                 \n")
                     .append("                 ,   sum(case when isrequired = 'Y' and isgraduated = 'Y' then 1 else 0 end  )   graduated_y_count                \n")
                     .append("                 ,   sum(decode(isrequired, 'Y', 1, 0))                                          requried_y_count                 \n")
                     .append("             FROM    (                                                                                                            \n")
                     .append("                         SELECT  b.grcode                                                                                         \n")
                     .append("                             ,   a.coursenm                                                                                       \n")
                     .append("                             ,   b.coursecode                                                                                     \n")
                     .append("                             ,   b.courseyear                                                                                     \n")
                     .append("                             ,   b.courseseq                                                                                      \n")
                     .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                     .append("                             ,   d.userid                                                                                         \n")
                     .append("                             ,   d.name                                                                                           \n")
                     .append("                             ,   e.subj                                                                                           \n")
                     .append("                             ,   e.subjnm                                                                                         \n")
                     .append("                             ,   c.isrequired                                                                                     \n")
                     .append("                             ,   f.isgraduated                                                                                    \n")
                     .append("                             ,   f.score                                                                                          \n")
                     .append("                             ,   f.tstep                                                                                          \n")
                     .append("                             ,   f.mtest                                                                                          \n")
                     .append("                             ,   f.ftest                                                                                          \n")
                     .append("                             ,   f.report                                                                                         \n")
                     .append("                             ,   f.act                                                                                            \n")
                     .append("                             ,   f.etc1                                                                                           \n")
                     .append("                             ,   f.etc2                                                                                           \n")
                     .append("                             ,   f.avtstep                                                                                        \n")
                     .append("                             ,   f.avmtest                                                                                        \n")
                     .append("                             ,   f.avftest                                                                                        \n")
                     .append("                             ,   f.avreport                                                                                       \n")
                     .append("                             ,   f.avact                                                                                          \n")
                     .append("                             ,   f.avetc1                                                                                         \n")
                     .append("                             ,   f.avetc2                                                                                         \n")
                     .append("                             ,   f.htest                                                                                          \n")
                     .append("                             ,   f.avhtest                                                                                        \n")
                     .append("                             ,   h.isgraduated course_graduated                                                                   \n")
                     .append("                         from    tz_pfcourse         a                                                                            \n")
                     .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                     .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                     .append("                             ,   tz_member           d                                                                            \n")
                     .append("                             ,   tz_subj             e                                                                            \n")
                     .append("                             ,   tz_student            f                                                                            \n")
                     .append("                             ,   (                                                                                                \n")
                     .append("                                     select userid, subj, max(score) max_score                                                    \n")
                     .append("                                     from tz_student                                                                                \n")
                     .append("                                     group by userid, subj                                                                        \n")
                     .append("                                 )                   g                                                                            \n")
                     .append("                             ,        tz_pfstudent   h                                                                            \n")
                     .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                     .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                     .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                     .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                     .append("                         and     c.userid        = d.userid                                                                       \n")
                     .append("                         and     c.subj          = e.subj                                                                         \n")
                     .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                     .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                     .append("                         and     c.userid        = h.userid                                                                       \n")
                     .append("                         and     b.coursecode    = h.coursecode                                                                   \n")
                     .append("                         and     b.courseyear    = h.courseyear                                                                   \n")
                     .append("                         and     b.courseseq     = h.courseseq                                                                    \n")
                     .append("                         and     c.status        = 'RV'                                                                           \n")
                     .append("                         and     f.userid        = g.userid                                                                       \n")
                     .append("                         and     f.subj          = g.subj                                                                         \n")
                     .append("                         and     g.max_score     = f.score                                                                        \n")
                     .append("                         group by b.grcode                                                                                        \n")
                     .append("                             ,    b.coursecode                                                                                    \n")
                     .append("                             ,    b.courseyear                                                                                    \n")
                     .append("                             ,    b.courseseq                                                                                     \n")
                     .append("                             ,    a.coursenm                                                                                      \n")
                     .append("                             ,    d.userid                                                                                        \n")
                     .append("                             ,    d.name                                                                                          \n")
                     .append("                             ,    e.subj                                                                                          \n")
                     .append("                             ,    e.subjnm                                                                                        \n")
                     .append("                             ,    c.isrequired                                                                                    \n")
                     .append("                             ,    f.isgraduated                                                                                   \n")
                     .append("                             ,    f.score                                                                                         \n")
                     .append("                             ,    f.tstep                                                                                         \n")
                     .append("                             ,    f.mtest                                                                                         \n")
                     .append("                             ,    f.ftest                                                                                         \n")
                     .append("                             ,    f.report                                                                                        \n")
                     .append("                             ,    f.act                                                                                           \n")
                     .append("                             ,    f.etc1                                                                                          \n")
                     .append("                             ,    f.etc2                                                                                          \n")
                     .append("                             ,    f.avtstep                                                                                       \n")
                     .append("                             ,    f.avmtest                                                                                       \n")
                     .append("                             ,    f.avftest                                                                                       \n")
                     .append("                             ,    f.avreport                                                                                      \n")
                     .append("                             ,    f.avact                                                                                         \n")
                     .append("                             ,    f.avetc1                                                                                        \n")
                     .append("                             ,    f.avetc2                                                                                        \n")
                     .append("                             ,    f.htest                                                                                         \n")
                     .append("                             ,    f.avhtest                                                                                       \n")
                     .append("                             ,    h.isgraduated                                                                                   \n")
                     .append("                         UNION                                                                                                    \n")
                     .append("                         select  b.grcode                                                                                         \n")
                     .append("                             ,   a.coursenm                                                                                       \n")
                     .append("                             ,   b.coursecode                                                                                     \n")
                     .append("                             ,   b.courseyear                                                                                     \n")
                     .append("                             ,   b.courseseq                                                                                      \n")
                     .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                     .append("                             ,   d.userid                                                                                         \n")
                     .append("                             ,   d.name                                                                                           \n")
                     .append("                             ,   e.subj                                                                                           \n")
                     .append("                             ,   e.subjnm                                                                                         \n")
                     .append("                             ,   c.isrequired                                                                                     \n")
                     .append("                             ,   f.isgraduated                                                                                    \n")
                     .append("                             ,   f.score                                                                                          \n")
                     .append("                             ,   f.tstep                                                                                          \n")
                     .append("                             ,   f.mtest                                                                                          \n")
                     .append("                             ,   f.ftest                                                                                          \n")
                     .append("                             ,   f.report                                                                                         \n")
                     .append("                             ,   f.act                                                                                            \n")
                     .append("                             ,   f.etc1                                                                                           \n")
                     .append("                             ,   f.etc2                                                                                           \n")
                     .append("                             ,   f.avtstep                                                                                        \n")
                     .append("                             ,   f.avmtest                                                                                        \n")
                     .append("                             ,   f.avftest                                                                                        \n")
                     .append("                             ,   f.avreport                                                                                       \n")
                     .append("                             ,   f.avact                                                                                          \n")
                     .append("                             ,   f.avetc1                                                                                         \n")
                     .append("                             ,   f.avetc2                                                                                         \n")
                     .append("                             ,   f.htest                                                                                          \n")
                     .append("                             ,   f.avhtest                                                                                        \n")
                     .append("                             ,   g.isgraduated course_graduated                                                                   \n")
                     .append("                         from    tz_pfcourse         a                                                                            \n")
                     .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                     .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                     .append("                             ,   tz_member           d                                                                            \n")
                     .append("                             ,   tz_subj             e                                                                            \n")
                     .append("                             ,   tz_student            f                                                                            \n")
                     .append("                             ,   tz_pfstudent        g                                                                            \n")
                     .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                     .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                     .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                     .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                     .append("                         and     c.userid        = d.userid                                                                       \n")
                     .append("                         and     c.subj          = e.subj                                                                         \n")
                     .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                     .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                     .append("                         and     c.userid        = g.userid                                                                       \n")
                     .append("                         and     b.coursecode    = g.coursecode                                                                   \n")
                     .append("                         and     b.courseyear    = g.courseyear                                                                   \n")
                     .append("                         and     b.courseseq     = g.courseseq                                                                    \n")
                     .append("                         and     c.status in ( 'RE', 'FE' )                                                                       \n")
                     .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                     .append("                         and     c.year          = f.year(+)                                                                      \n")
                     .append("                         and     c.subjseq       = f.subjseq(+)                                                                   \n")
                     .append("                         group by b.grcode                                                                                        \n")
                     .append("                             , b.coursecode                                                                                       \n")
                     .append("                             , b.courseyear                                                                                       \n")
                     .append("                             , b.courseseq                                                                                        \n")
                     .append("                             , a.coursenm                                                                                         \n")
                     .append("                             , d.userid                                                                                           \n")
                     .append("                             , d.name                                                                                             \n")
                     .append("                             , e.subj                                                                                             \n")
                     .append("                             , e.subjnm                                                                                           \n")
                     .append("                             , c.isrequired                                                                                       \n")
                     .append("                             , f.isgraduated                                                                                      \n")
                     .append("                             , f.score                                                                                            \n")
                     .append("                             , f.tstep                                                                                            \n")
                     .append("                             , f.mtest                                                                                            \n")
                     .append("                             , f.ftest                                                                                            \n")
                     .append("                             , f.report                                                                                           \n")
                     .append("                             , f.act                                                                                              \n")
                     .append("                             , f.etc1                                                                                             \n")
                     .append("                             , f.etc2                                                                                             \n")
                     .append("                             , f.avtstep                                                                                          \n")
                     .append("                             , f.avmtest                                                                                          \n")
                     .append("                             , f.avftest                                                                                          \n")
                     .append("                             , f.avreport                                                                                         \n")
                     .append("                             , f.avact                                                                                            \n")
                     .append("                             , f.avetc1                                                                                           \n")
                     .append("                             , f.avetc2                                                                                           \n")
                     .append("                             , f.htest                                                                                            \n")
                     .append("                             , f.avhtest                                                                                          \n")
                     .append("                             , g.isgraduated                                                                                      \n")
                     .append("                         order by name, isrequired desc                                                                           \n")
                     .append("                     )                                                                                                            \n")
                     .append("             group by grcode, courseyear, courseseq, coursecode, userid                                                           \n")
                     .append("         )                b                                                                                                       \n");
                
                if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                    sbSQL.append("  ,  vz_orgmember     c                                                                                                       \n");
                }
                
                sbSQL.append(" WHERE   a.grcode        = " + StringManager.makeSQL(v_grcode         ) + "                                                       \n")
                     .append(" and     a.courseyear    = " + StringManager.makeSQL(v_courseyear     ) + "                                                       \n");

                 if ( !v_coursecode.equals("") ) { 
                     sbSQL.append(" and a.coursecode    = " + StringManager.makeSQL(v_coursecode    ) + "   \n");
                 }
                 
                 if ( !v_courseseq.equals("") ) { 
                     sbSQL.append(" and a.courseseq     = " + StringManager.makeSQL(v_courseseq     ) + "   \n");
                 }
                 
                 
                 if ( v_isgraduated.equals("Y") ) {
                     sbSQL.append(" and a.course_graduated   = " + StringManager.makeSQL(v_isgraduated   ) + "   \n");
                 } else if ( v_isgraduated.equals("N") ) {
                     sbSQL.append(" and ( a.course_graduated   IS NULL OR a.course_graduated = " + StringManager.makeSQL(v_isgraduated   ) + " ) \n");
                 }
                 
                 if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                     sbSQL.append(" AND     c.orgcode       = " + v_orgcode + "                             \n")
                          .append(" and     a.userid        = c.userid                                      \n");
                 }
                 
                 sbSQL.append(" and a.userid = b.userid                                                                                                         \n")
                      .append(" GROUP BY a.grcode, a.courseyear, a.coursecode, a.courseseq, a.name, a.userid                                                    \n")
                      .append(" ORDER BY a.grcode, a.courseyear, a.coursecode, a.courseseq, a.name, a.userid                                                    \n");
                 
                 System.out.println("sql : " + sbSQL.toString());
                       
                 ls = connMgr.executeQuery(sbSQL.toString());
       
                 while ( ls.next() ) { 
                     dbox = ls.getDataBox();
                     list.add(dbox);
                 }
            } catch ( Exception ex ) { 
                ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
                throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
            } finally { 
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
                   
            return list;
         }
         
         
         /**
         * 전문가 양성 과정 개인별 온라인 성적 상세보기 rowspan 카운트 
         */
         public ArrayList selectRowspanCount04(RequestBox box) throws Exception {
             DBConnectionManager connMgr         = null;
             ListSet             ls              = null;
             ArrayList           list            = null;
             StringBuffer        sbSQL           = new StringBuffer("");
              
             DataBox             dbox            = null;
              
             String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
             String              v_courseyear    = box.getStringDefault  ( "p_courseyear"   , ""    ); // 년도
             String              v_courseseq     = box.getStringDefault  ( "p_courseseq"    , ""    ); // 과목 기수
             String              v_coursecode    = box.getStringDefault  ( "p_coursecode"   , ""    ); // 과목&코스
             String              s_gadmin        = box.getSession        ( "gadmin"                 );
             String              v_branch        = box.getStringDefault  ( "p_branch"       , ""    );
             String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
             String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
              
            String              v_orderColumn   = box.getString( "p_orderColumn"  );  // 정렬할 컬럼명
            String              v_orderType     = box.getString( "p_orderType"    );  // 정렬할 순서
            
            try { 
                connMgr = new DBConnectionManager();
                list   = new ArrayList();    
                
                sbSQL.append(" SELECT  a.grcode                                                                                                                 \n")                                       
                     .append("     ,   a.coursecode                                                                                                             \n")
                     .append("     ,   a.courseyear                                                                                                             \n")
                     .append("     ,   a.courseseq                                                                                                              \n")
                     .append("     ,   a.userid                                                                                                                 \n")
                     .append("     ,   a.name                                                                                                                   \n")
                     .append("     ,   count(*)      count                                                                                                      \n")
                     .append(" FROM    (                                                                                                                        \n")
                     .append("          select  b.grcode                                                                                                        \n")
                     .append("                 ,   a.coursenm                                                                                                   \n")
                     .append("                 ,   b.coursecode                                                                                                 \n")
                     .append("                 ,   b.courseyear                                                                                                 \n")
                     .append("                 ,   b.courseseq                                                                                                  \n")
                     .append("                 ,   d.userid                                                                                                     \n")
                     .append("                 ,   d.name                                                                                                       \n")
                     .append("                 ,   e.subj                                                                                                       \n")
                     .append("                 ,   e.subjnm                                                                                                     \n")
                     .append("                 ,   c.isrequired                                                                                                 \n")
                     .append("                 ,   f.isgraduated                                                                                                \n")
                     .append("                 ,   f.score                                                                                                      \n")
                     .append("                 ,   f.tstep                                                                                                      \n")
                     .append("                 ,   f.mtest                                                                                                      \n")
                     .append("                 ,   f.ftest                                                                                                      \n")
                     .append("                 ,   f.report                                                                                                     \n")
                     .append("                 ,   f.act                                                                                                        \n")
                     .append("                 ,   f.etc1                                                                                                       \n")
                     .append("                 ,   f.etc2                                                                                                       \n")
                     .append("                 ,   f.avtstep                                                                                                    \n")
                     .append("                 ,   f.avmtest                                                                                                    \n")
                     .append("                 ,   f.avftest                                                                                                    \n")
                     .append("                 ,   f.avreport                                                                                                   \n")
                     .append("                 ,   f.avact                                                                                                      \n")
                     .append("                 ,   f.avetc1                                                                                                     \n")
                     .append("                 ,   f.avetc2                                                                                                     \n")
                     .append("                 ,   f.htest                                                                                                      \n")
                     .append("                 ,   f.avhtest                                                                                                    \n")
                     .append("                 ,   h.isgraduated       course_graduated                                                                         \n")
                     .append("             FROM    tz_pfcourse         a                                                                                        \n")
                     .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                     .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                     .append("                 ,   tz_member           d                                                                                        \n")
                     .append("                 ,   tz_subj             e                                                                                        \n")
                     .append("                 ,   tz_student          f                                                                                        \n")
                     .append("                 ,   (                                                                                                            \n")
                     .append("                         select  i.userid, i.subj, max(i.score) max_score                                                         \n")
                     .append("                         from    tz_student  i                                                                                    \n")
                     .append("                             ,   tz_subjseq  j                                                                                    \n")
                     .append("                         where   i.subj      = j.subj                                                                             \n")
                     .append("                         and     i.subjseq   = j.subjseq                                                                          \n")
                     .append("                         and     i.year      = j.year                                                                             \n")
                     .append("                         and     j.isblended = 'N'                                                                                \n")
                     .append("                         and     j.isexpert  = 'N'                                                                                \n")
                     .append("                         group by i.userid, i.subj                                                                                \n")
                     .append("                     )                   g                                                                                        \n")
                     .append("                 ,   tz_pfstudent        h                                                                                        \n")
                     .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                     .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                     .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                     .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                     .append("             AND     c.userid        = d.userid                                                                                   \n")
                     .append("             AND     c.subj          = e.subj                                                                                     \n")
                     .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                     .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                     .append("             AND     c.userid        = h.userid                                                                                   \n")
                     .append("             AND     b.coursecode    = h.coursecode                                                                               \n")
                     .append("             AND     b.courseyear    = h.courseyear                                                                               \n")
                     .append("             AND     b.courseseq     = h.courseseq                                                                                \n")
                     .append("             AND     c.status        = 'RV'                                                                                       \n")
                     .append("             AND     f.userid        = g.userid                                                                                   \n")
                     .append("             AND     f.subj          = g.subj                                                                                     \n")
                     .append("             AND     g.max_score     = f.score                                                                                    \n")
                     .append("             GROUP BY b.grcode                                                                                                    \n")
                     .append("                 ,    b.coursecode                                                                                                \n")
                     .append("                 ,    b.courseyear                                                                                                \n")
                     .append("                 ,    b.courseseq                                                                                                 \n")
                     .append("                 ,    a.coursenm                                                                                                  \n")
                     .append("                 ,    d.userid                                                                                                    \n")
                     .append("                 ,    d.name                                                                                                      \n")
                     .append("                 ,    e.subj                                                                                                      \n")
                     .append("                 ,    e.subjnm                                                                                                    \n")
                     .append("                 ,    c.isrequired                                                                                                \n")
                     .append("                 ,    f.isgraduated                                                                                               \n")
                     .append("                 ,    f.score                                                                                                     \n")
                     .append("                 ,    f.tstep                                                                                                     \n")
                     .append("                 ,    f.mtest                                                                                                     \n")
                     .append("                 ,    f.ftest                                                                                                     \n")
                     .append("                 ,    f.report                                                                                                    \n")
                     .append("                 ,    f.act                                                                                                       \n")
                     .append("                 ,    f.etc1                                                                                                      \n")
                     .append("                 ,    f.etc2                                                                                                      \n")
                     .append("                 ,    f.avtstep                                                                                                   \n")
                     .append("                 ,    f.avmtest                                                                                                   \n")
                     .append("                 ,    f.avftest                                                                                                   \n")
                     .append("                 ,    f.avreport                                                                                                  \n")
                     .append("                 ,    f.avact                                                                                                     \n")
                     .append("                 ,    f.avetc1                                                                                                    \n")
                     .append("                 ,    f.avetc2                                                                                                    \n")
                     .append("                 ,    f.htest                                                                                                     \n")
                     .append("                 ,    f.avhtest                                                                                                   \n")
                     .append("                 ,    h.isgraduated                                                                                               \n")
                     .append("             UNION                                                                                                                \n")
                     .append("             SELECT  b.grcode                                                                                                     \n")
                     .append("                 ,   a.coursenm                                                                                                   \n")
                     .append("                 ,   b.coursecode                                                                                                 \n")
                     .append("                 ,   b.courseyear                                                                                                 \n")
                     .append("                 ,   b.courseseq                                                                                                  \n")
                     .append("                 ,   d.userid                                                                                                     \n")
                     .append("                 ,   d.name                                                                                                       \n")
                     .append("                 ,   e.subj                                                                                                       \n")
                     .append("                 ,   e.subjnm                                                                                                     \n")
                     .append("                 ,   c.isrequired                                                                                                 \n")
                     .append("                 ,   f.isgraduated                                                                                                \n")
                     .append("                 ,   f.score                                                                                                      \n")
                     .append("                 ,   f.tstep                                                                                                      \n")
                     .append("                 ,   f.mtest                                                                                                      \n")
                     .append("                 ,   f.ftest                                                                                                      \n")
                     .append("                 ,   f.report                                                                                                     \n")
                     .append("                 ,   f.act                                                                                                        \n")
                     .append("                 ,   f.etc1                                                                                                       \n")
                     .append("                 ,   f.etc2                                                                                                       \n")
                     .append("                 ,   f.avtstep                                                                                                    \n")
                     .append("                 ,   f.avmtest                                                                                                    \n")
                     .append("                 ,   f.avftest                                                                                                    \n")
                     .append("                 ,   f.avreport                                                                                                   \n")
                     .append("                 ,   f.avact                                                                                                      \n")
                     .append("                 ,   f.avetc1                                                                                                     \n")
                     .append("                 ,   f.avetc2                                                                                                     \n")
                     .append("                 ,   f.htest                                                                                                      \n")
                     .append("                 ,   f.avhtest                                                                                                    \n")
                     .append("                 ,   g.isgraduated       course_graduated                                                                         \n")
                     .append("             FROM    tz_pfcourse         a                                                                                        \n")
                     .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                     .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                     .append("                 ,   tz_member           d                                                                                        \n")
                     .append("                 ,   tz_subj             e                                                                                        \n")
                     .append("                 ,   tz_student            f                                                                                        \n")
                     .append("                 ,   tz_pfstudent        g                                                                                        \n")
                     .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                     .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                     .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                     .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                     .append("             AND     c.userid        = d.userid                                                                                   \n")
                     .append("             AND     c.subj          = e.subj                                                                                     \n")
                     .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                     .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                     .append("             AND     c.userid        = g.userid                                                                                   \n")
                     .append("             AND     b.coursecode    = g.coursecode                                                                               \n")
                     .append("             AND     b.courseyear    = g.courseyear                                                                               \n")
                     .append("             AND     b.courseseq     = g.courseseq                                                                                \n")
                     .append("             AND     c.status in ( 'RE', 'FE' )                                                                                   \n")
                     .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                     .append("             AND     c.year          = f.year(+)                                                                                  \n")
                     .append("             AND     c.subjseq       = f.subjseq(+)                                                                               \n")
                     .append("             GROUP BY b.grcode                                                                                                    \n")
                     .append("                 ,    b.coursecode                                                                                                \n")
                     .append("                 ,    b.courseyear                                                                                                \n")
                     .append("                 ,    b.courseseq                                                                                                 \n")
                     .append("                 ,    a.coursenm                                                                                                  \n")
                     .append("                 ,    d.userid                                                                                                    \n")
                     .append("                 ,    d.name                                                                                                      \n")
                     .append("                 ,    e.subj                                                                                                      \n")
                     .append("                 ,    e.subjnm                                                                                                    \n")
                     .append("                 ,    c.isrequired                                                                                                \n")
                     .append("                 ,    f.isgraduated                                                                                               \n")
                     .append("                 ,    f.score                                                                                                     \n")
                     .append("                 ,    f.tstep                                                                                                     \n")
                     .append("                 ,    f.mtest                                                                                                     \n")
                     .append("                 ,    f.ftest                                                                                                     \n")
                     .append("                 ,    f.report                                                                                                    \n")
                     .append("                 ,    f.act                                                                                                       \n")
                     .append("                 ,    f.etc1                                                                                                      \n")
                     .append("                 ,    f.etc2                                                                                                      \n")
                     .append("                 ,    f.avtstep                                                                                                   \n")
                     .append("                 ,    f.avmtest                                                                                                   \n")
                     .append("                 ,    f.avftest                                                                                                   \n")
                     .append("                 ,    f.avreport                                                                                                  \n")
                     .append("                 ,    f.avact                                                                                                     \n")
                     .append("                 ,    f.avetc1                                                                                                    \n")
                     .append("                 ,    f.avetc2                                                                                                    \n")
                     .append("                 ,    f.htest                                                                                                     \n")
                     .append("                 ,    f.avhtest                                                                                                   \n")
                     .append("                 ,    g.isgraduated                                                                                               \n")
                     .append("             ORDER BY name, isrequired desc                                                                                       \n")
                     .append("         )           a                                                                                                            \n")
                     .append("     ,   (                                                                                                                        \n")
                     .append("             SELECT  grcode                                                                                                       \n")
                     .append("                 ,   courseyear                                                                                                   \n")
                     .append("                 ,   courseseq                                                                                                    \n")
                     .append("                 ,   coursecode                                                                                                   \n")
                     .append("                 ,   userid                                                                                                       \n")
                     .append("                 ,   avg(score                                                               )   total                            \n")
                     .append("                 ,   sum(case when isrequired = 'N' and isgraduated = 'Y' then 1 else 0 end  )   graduated_n_count                \n")
                     .append("                 ,   max(subjnumforcomplete)                                                     requried_n_count                 \n")
                     .append("                 ,   sum(case when isrequired = 'Y' and isgraduated = 'Y' then 1 else 0 end  )   graduated_y_count                \n")
                     .append("                 ,   sum(decode(isrequired, 'Y', 1, 0))                                          requried_y_count                 \n")
                     .append("             FROM    (                                                                                                            \n")
                     .append("                         SELECT  b.grcode                                                                                         \n")
                     .append("                             ,   a.coursenm                                                                                       \n")
                     .append("                             ,   b.coursecode                                                                                     \n")
                     .append("                             ,   b.courseyear                                                                                     \n")
                     .append("                             ,   b.courseseq                                                                                      \n")
                     .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                     .append("                             ,   d.userid                                                                                         \n")
                     .append("                             ,   d.name                                                                                           \n")
                     .append("                             ,   e.subj                                                                                           \n")
                     .append("                             ,   e.subjnm                                                                                         \n")
                     .append("                             ,   c.isrequired                                                                                     \n")
                     .append("                             ,   f.isgraduated                                                                                    \n")
                     .append("                             ,   f.score                                                                                          \n")
                     .append("                             ,   f.tstep                                                                                          \n")
                     .append("                             ,   f.mtest                                                                                          \n")
                     .append("                             ,   f.ftest                                                                                          \n")
                     .append("                             ,   f.report                                                                                         \n")
                     .append("                             ,   f.act                                                                                            \n")
                     .append("                             ,   f.etc1                                                                                           \n")
                     .append("                             ,   f.etc2                                                                                           \n")
                     .append("                             ,   f.avtstep                                                                                        \n")
                     .append("                             ,   f.avmtest                                                                                        \n")
                     .append("                             ,   f.avftest                                                                                        \n")
                     .append("                             ,   f.avreport                                                                                       \n")
                     .append("                             ,   f.avact                                                                                          \n")
                     .append("                             ,   f.avetc1                                                                                         \n")
                     .append("                             ,   f.avetc2                                                                                         \n")
                     .append("                             ,   f.htest                                                                                          \n")
                     .append("                             ,   f.avhtest                                                                                        \n")
                     .append("                             ,   h.isgraduated course_graduated                                                                   \n")
                     .append("                         from    tz_pfcourse         a                                                                            \n")
                     .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                     .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                     .append("                             ,   tz_member           d                                                                            \n")
                     .append("                             ,   tz_subj             e                                                                            \n")
                     .append("                             ,   tz_student            f                                                                            \n")
                     .append("                             ,   (                                                                                                \n")
                     .append("                                     select userid, subj, max(score) max_score                                                    \n")
                     .append("                                     from tz_student                                                                                \n")
                     .append("                                     group by userid, subj                                                                        \n")
                     .append("                                 )                   g                                                                            \n")
                     .append("                             ,        tz_pfstudent   h                                                                            \n")
                     .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                     .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                     .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                     .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                     .append("                         and     c.userid        = d.userid                                                                       \n")
                     .append("                         and     c.subj          = e.subj                                                                         \n")
                     .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                     .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                     .append("                         and     c.userid        = h.userid                                                                       \n")
                     .append("                         and     b.coursecode    = h.coursecode                                                                   \n")
                     .append("                         and     b.courseyear    = h.courseyear                                                                   \n")
                     .append("                         and     b.courseseq     = h.courseseq                                                                    \n")
                     .append("                         and     c.status        = 'RV'                                                                           \n")
                     .append("                         and     f.userid        = g.userid                                                                       \n")
                     .append("                         and     f.subj          = g.subj                                                                         \n")
                     .append("                         and     g.max_score     = f.score                                                                        \n")
                     .append("                         group by b.grcode                                                                                        \n")
                     .append("                             ,    b.coursecode                                                                                    \n")
                     .append("                             ,    b.courseyear                                                                                    \n")
                     .append("                             ,    b.courseseq                                                                                     \n")
                     .append("                             ,    a.coursenm                                                                                      \n")
                     .append("                             ,    d.userid                                                                                        \n")
                     .append("                             ,    d.name                                                                                          \n")
                     .append("                             ,    e.subj                                                                                          \n")
                     .append("                             ,    e.subjnm                                                                                        \n")
                     .append("                             ,    c.isrequired                                                                                    \n")
                     .append("                             ,    f.isgraduated                                                                                   \n")
                     .append("                             ,    f.score                                                                                         \n")
                     .append("                             ,    f.tstep                                                                                         \n")
                     .append("                             ,    f.mtest                                                                                         \n")
                     .append("                             ,    f.ftest                                                                                         \n")
                     .append("                             ,    f.report                                                                                        \n")
                     .append("                             ,    f.act                                                                                           \n")
                     .append("                             ,    f.etc1                                                                                          \n")
                     .append("                             ,    f.etc2                                                                                          \n")
                     .append("                             ,    f.avtstep                                                                                       \n")
                     .append("                             ,    f.avmtest                                                                                       \n")
                     .append("                             ,    f.avftest                                                                                       \n")
                     .append("                             ,    f.avreport                                                                                      \n")
                     .append("                             ,    f.avact                                                                                         \n")
                     .append("                             ,    f.avetc1                                                                                        \n")
                     .append("                             ,    f.avetc2                                                                                        \n")
                     .append("                             ,    f.htest                                                                                         \n")
                     .append("                             ,    f.avhtest                                                                                       \n")
                     .append("                             ,    h.isgraduated                                                                                   \n")
                     .append("                         UNION                                                                                                    \n")
                     .append("                         select  b.grcode                                                                                         \n")
                     .append("                             ,   a.coursenm                                                                                       \n")
                     .append("                             ,   b.coursecode                                                                                     \n")
                     .append("                             ,   b.courseyear                                                                                     \n")
                     .append("                             ,   b.courseseq                                                                                      \n")
                     .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                     .append("                             ,   d.userid                                                                                         \n")
                     .append("                             ,   d.name                                                                                           \n")
                     .append("                             ,   e.subj                                                                                           \n")
                     .append("                             ,   e.subjnm                                                                                         \n")
                     .append("                             ,   c.isrequired                                                                                     \n")
                     .append("                             ,   f.isgraduated                                                                                    \n")
                     .append("                             ,   f.score                                                                                          \n")
                     .append("                             ,   f.tstep                                                                                          \n")
                     .append("                             ,   f.mtest                                                                                          \n")
                     .append("                             ,   f.ftest                                                                                          \n")
                     .append("                             ,   f.report                                                                                         \n")
                     .append("                             ,   f.act                                                                                            \n")
                     .append("                             ,   f.etc1                                                                                           \n")
                     .append("                             ,   f.etc2                                                                                           \n")
                     .append("                             ,   f.avtstep                                                                                        \n")
                     .append("                             ,   f.avmtest                                                                                        \n")
                     .append("                             ,   f.avftest                                                                                        \n")
                     .append("                             ,   f.avreport                                                                                       \n")
                     .append("                             ,   f.avact                                                                                          \n")
                     .append("                             ,   f.avetc1                                                                                         \n")
                     .append("                             ,   f.avetc2                                                                                         \n")
                     .append("                             ,   f.htest                                                                                          \n")
                     .append("                             ,   f.avhtest                                                                                        \n")
                     .append("                             ,   g.isgraduated course_graduated                                                                   \n")
                     .append("                         from    tz_pfcourse         a                                                                            \n")
                     .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                     .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                     .append("                             ,   tz_member           d                                                                            \n")
                     .append("                             ,   tz_subj             e                                                                            \n")
                     .append("                             ,   tz_student            f                                                                            \n")
                     .append("                             ,   tz_pfstudent        g                                                                            \n")
                     .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                     .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                     .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                     .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                     .append("                         and     c.userid        = d.userid                                                                       \n")
                     .append("                         and     c.subj          = e.subj                                                                         \n")
                     .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                     .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                     .append("                         and     c.userid        = g.userid                                                                       \n")
                     .append("                         and     b.coursecode    = g.coursecode                                                                   \n")
                     .append("                         and     b.courseyear    = g.courseyear                                                                   \n")
                     .append("                         and     b.courseseq     = g.courseseq                                                                    \n")
                     .append("                         and     c.status in ( 'RE', 'FE' )                                                                       \n")
                     .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                     .append("                         and     c.year          = f.year(+)                                                                      \n")
                     .append("                         and     c.subjseq       = f.subjseq(+)                                                                   \n")
                     .append("                         group by b.grcode                                                                                        \n")
                     .append("                             , b.coursecode                                                                                       \n")
                     .append("                             , b.courseyear                                                                                       \n")
                     .append("                             , b.courseseq                                                                                        \n")
                     .append("                             , a.coursenm                                                                                         \n")
                     .append("                             , d.userid                                                                                           \n")
                     .append("                             , d.name                                                                                             \n")
                     .append("                             , e.subj                                                                                             \n")
                     .append("                             , e.subjnm                                                                                           \n")
                     .append("                             , c.isrequired                                                                                       \n")
                     .append("                             , f.isgraduated                                                                                      \n")
                     .append("                             , f.score                                                                                            \n")
                     .append("                             , f.tstep                                                                                            \n")
                     .append("                             , f.mtest                                                                                            \n")
                     .append("                             , f.ftest                                                                                            \n")
                     .append("                             , f.report                                                                                           \n")
                     .append("                             , f.act                                                                                              \n")
                     .append("                             , f.etc1                                                                                             \n")
                     .append("                             , f.etc2                                                                                             \n")
                     .append("                             , f.avtstep                                                                                          \n")
                     .append("                             , f.avmtest                                                                                          \n")
                     .append("                             , f.avftest                                                                                          \n")
                     .append("                             , f.avreport                                                                                         \n")
                     .append("                             , f.avact                                                                                            \n")
                     .append("                             , f.avetc1                                                                                           \n")
                     .append("                             , f.avetc2                                                                                           \n")
                     .append("                             , f.htest                                                                                            \n")
                     .append("                             , f.avhtest                                                                                          \n")
                     .append("                             , g.isgraduated                                                                                      \n")
                     .append("                         order by name, isrequired desc                                                                           \n")
                     .append("                     )                                                                                                            \n")
                     .append("             group by grcode, courseyear, courseseq, coursecode, userid                                                           \n")
                     .append("         )                b                                                                                                       \n");
                
                if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                    sbSQL.append("  ,  vz_orgmember     c                                                                                                       \n");
                }
                
                sbSQL.append(" WHERE   a.grcode        = " + StringManager.makeSQL(v_grcode         ) + "                                                       \n")
                     .append(" and     a.courseyear    = " + StringManager.makeSQL(v_courseyear     ) + "                                                       \n");

                 if ( !v_coursecode.equals("") ) { 
                     sbSQL.append(" and a.coursecode    = " + StringManager.makeSQL(v_coursecode    ) + "   \n");
                 }
                 
                 if ( !v_courseseq.equals("") ) { 
                     sbSQL.append(" and a.courseseq     = " + StringManager.makeSQL(v_courseseq     ) + "   \n");
                 }
                 
                 
                 if ( v_isgraduated.equals("Y") ) {
                     sbSQL.append(" and a.course_graduated   = " + StringManager.makeSQL(v_isgraduated   ) + "   \n");
                 } else if ( v_isgraduated.equals("N") ) {
                     sbSQL.append(" and ( a.course_graduated   IS NULL OR a.course_graduated = " + StringManager.makeSQL(v_isgraduated   ) + " ) \n");
                 }
                 
                 if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL") ) ) {
                     sbSQL.append(" AND     c.orgcode       = " + v_orgcode + "                             \n")
                          .append(" and     a.userid        = c.userid                                      \n");
                 }
                 
                 sbSQL.append(" and a.userid = b.userid                                                                                                         \n")
                      .append(" GROUP BY a.grcode, a.courseyear, a.coursecode, a.courseseq, a.name, a.userid                                                    \n")
                      .append(" ORDER BY a.grcode, a.courseyear, a.coursecode, a.courseseq, a.name, a.userid                                                    \n");
                 
                 System.out.println("sql : " + sbSQL.toString());
                       
                 ls = connMgr.executeQuery(sbSQL.toString());
       
                 while ( ls.next() ) { 
                     dbox = ls.getDataBox();
                     list.add(dbox);
                 }
            } catch ( Exception ex ) { 
                ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
                throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
            } finally { 
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
                   
            return list;
         }
         
         
         /**
         * 전문가 양성 과정 개인별 온라인 성적 상세보기 rowspan 카운트 
         */
         public ArrayList selectRowspanCount05(RequestBox box) throws Exception {
             DBConnectionManager connMgr         = null;
             ListSet             ls              = null;
             ArrayList           list            = null;
             StringBuffer        sbSQL           = new StringBuffer("");
              
             DataBox             dbox            = null;
              
             String              v_grcode        = box.getStringDefault  ( "p_grcode"       , ""    ); // 교육그룹
             String              v_courseyear    = box.getStringDefault  ( "p_courseyear"   , ""    ); // 년도
             String              v_courseseq     = box.getStringDefault  ( "p_courseseq"    , ""    ); // 과목 기수
             String              v_coursecode    = box.getStringDefault  ( "p_coursecode"   , ""    ); // 과목&코스
             String              v_orgcode       = box.getStringDefault  ( "p_orgcode"      , ""    ); // 조직코드
             String              v_isgraduated   = box.getString         ( "p_isgraduated"          );
             String              v_sido          = box.getString         ( "p_sido"                 );
             String              v_gugun         = box.getString         ( "p_gugun"                );
             String              v_gender        = box.getString         ( "p_gender"               );
             String              v_achievement   = box.getString         ( "p_achievement"          );
             String              v_age           = box.getString         ( "p_age"                  );
             String              v_gubun         = box.getString         ( "p_gubun"                );
              
             try {
                 connMgr     = new DBConnectionManager();            
                 list        = new ArrayList();
                 
                 sbSQL.append(" SELECT  a.grcode                                                                                                                   \n")                                       
                      .append("     ,   a.coursecode                                                                                                               \n")
                      .append("     ,   a.courseyear                                                                                                               \n")
                      .append("     ,   a.courseseq                                                                                                                \n")
                      .append("     ,   a.userid                                                                                                                   \n")
                      .append("     ,   count(*)        count                                                                                                      \n")
                      .append(" FROM    (                                                                                                                        \n")
                      .append("          select  b.grcode                                                                                                        \n")
                      .append("                 ,   a.coursenm                                                                                                   \n")
                      .append("                 ,   b.coursecode                                                                                                 \n")
                      .append("                 ,   b.courseyear                                                                                                 \n")
                      .append("                 ,   b.courseseq                                                                                                  \n")
                      .append("                 ,   d.userid                                                                                                     \n")
                      .append("                 ,   d.name                                                                                                       \n")
                      .append("                 ,   e.subj                                                                                                       \n")
                      .append("                 ,   e.subjnm                                                                                                     \n")
                      .append("                 ,   c.isrequired                                                                                                 \n")
                      .append("                 ,   f.isgraduated                                                                                                \n")
                      .append("                 ,   f.score                                                                                                      \n")
                      .append("                 ,   f.tstep                                                                                                      \n")
                      .append("                 ,   f.mtest                                                                                                      \n")
                      .append("                 ,   f.ftest                                                                                                      \n")
                      .append("                 ,   f.report                                                                                                     \n")
                      .append("                 ,   f.act                                                                                                        \n")
                      .append("                 ,   f.etc1                                                                                                       \n")
                      .append("                 ,   f.etc2                                                                                                       \n")
                      .append("                 ,   f.avtstep                                                                                                    \n")
                      .append("                 ,   f.avmtest                                                                                                    \n")
                      .append("                 ,   f.avftest                                                                                                    \n")
                      .append("                 ,   f.avreport                                                                                                   \n")
                      .append("                 ,   f.avact                                                                                                      \n")
                      .append("                 ,   f.avetc1                                                                                                     \n")
                      .append("                 ,   f.avetc2                                                                                                     \n")
                      .append("                 ,   f.htest                                                                                                      \n")
                      .append("                 ,   f.avhtest                                                                                                    \n")
                      .append("                 ,   h.isgraduated       course_graduated                                                                         \n")
                      .append("             FROM    tz_pfcourse         a                                                                                        \n")
                      .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                      .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                      .append("                 ,   tz_member           d                                                                                        \n")
                      .append("                 ,   tz_subj             e                                                                                        \n")
                      .append("                 ,   tz_student          f                                                                                        \n")
                      .append("                 ,   (                                                                                                            \n")
                      .append("                         select  i.userid, i.subj, max(i.score) max_score                                                         \n")
                      .append("                         from    tz_student  i                                                                                    \n")
                      .append("                             ,   tz_subjseq  j                                                                                    \n")
                      .append("                         where   i.subj      = j.subj                                                                             \n")
                      .append("                         and     i.subjseq   = j.subjseq                                                                          \n")
                      .append("                         and     i.year      = j.year                                                                             \n")
                      .append("                         and     j.isblended = 'N'                                                                                \n")
                      .append("                         and     j.isexpert  = 'N'                                                                                \n")
                      .append("                         group by i.userid, i.subj                                                                                \n")
                      .append("                     )                   g                                                                                        \n")
                      .append("                 ,   tz_pfstudent        h                                                                                        \n")
                      .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                      .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                      .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                      .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                      .append("             AND     c.userid        = d.userid                                                                                   \n")
                      .append("             AND     c.subj          = e.subj                                                                                     \n")
                      .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                      .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                      .append("             AND     c.userid        = h.userid                                                                                   \n")
                      .append("             AND     b.coursecode    = h.coursecode                                                                               \n")
                      .append("             AND     b.courseyear    = h.courseyear                                                                               \n")
                      .append("             AND     b.courseseq     = h.courseseq                                                                                \n")
                      .append("             AND     c.status        = 'RV'                                                                                       \n")
                      .append("             AND     f.userid        = g.userid                                                                                   \n")
                      .append("             AND     f.subj          = g.subj                                                                                     \n")
                      .append("             AND     g.max_score     = f.score                                                                                    \n")
                      .append("             GROUP BY b.grcode                                                                                                    \n")
                      .append("                 ,    b.coursecode                                                                                                \n")
                      .append("                 ,    b.courseyear                                                                                                \n")
                      .append("                 ,    b.courseseq                                                                                                 \n")
                      .append("                 ,    a.coursenm                                                                                                  \n")
                      .append("                 ,    d.userid                                                                                                    \n")
                      .append("                 ,    d.name                                                                                                      \n")
                      .append("                 ,    e.subj                                                                                                      \n")
                      .append("                 ,    e.subjnm                                                                                                    \n")
                      .append("                 ,    c.isrequired                                                                                                \n")
                      .append("                 ,    f.isgraduated                                                                                               \n")
                      .append("                 ,    f.score                                                                                                     \n")
                      .append("                 ,    f.tstep                                                                                                     \n")
                      .append("                 ,    f.mtest                                                                                                     \n")
                      .append("                 ,    f.ftest                                                                                                     \n")
                      .append("                 ,    f.report                                                                                                    \n")
                      .append("                 ,    f.act                                                                                                       \n")
                      .append("                 ,    f.etc1                                                                                                      \n")
                      .append("                 ,    f.etc2                                                                                                      \n")
                      .append("                 ,    f.avtstep                                                                                                   \n")
                      .append("                 ,    f.avmtest                                                                                                   \n")
                      .append("                 ,    f.avftest                                                                                                   \n")
                      .append("                 ,    f.avreport                                                                                                  \n")
                      .append("                 ,    f.avact                                                                                                     \n")
                      .append("                 ,    f.avetc1                                                                                                    \n")
                      .append("                 ,    f.avetc2                                                                                                    \n")
                      .append("                 ,    f.htest                                                                                                     \n")
                      .append("                 ,    f.avhtest                                                                                                   \n")
                      .append("                 ,    h.isgraduated                                                                                               \n")
                      .append("             UNION                                                                                                                \n")
                      .append("             SELECT  b.grcode                                                                                                     \n")
                      .append("                 ,   a.coursenm                                                                                                   \n")
                      .append("                 ,   b.coursecode                                                                                                 \n")
                      .append("                 ,   b.courseyear                                                                                                 \n")
                      .append("                 ,   b.courseseq                                                                                                  \n")
                      .append("                 ,   d.userid                                                                                                     \n")
                      .append("                 ,   d.name                                                                                                       \n")
                      .append("                 ,   e.subj                                                                                                       \n")
                      .append("                 ,   e.subjnm                                                                                                     \n")
                      .append("                 ,   c.isrequired                                                                                                 \n")
                      .append("                 ,   f.isgraduated                                                                                                \n")
                      .append("                 ,   f.score                                                                                                      \n")
                      .append("                 ,   f.tstep                                                                                                      \n")
                      .append("                 ,   f.mtest                                                                                                      \n")
                      .append("                 ,   f.ftest                                                                                                      \n")
                      .append("                 ,   f.report                                                                                                     \n")
                      .append("                 ,   f.act                                                                                                        \n")
                      .append("                 ,   f.etc1                                                                                                       \n")
                      .append("                 ,   f.etc2                                                                                                       \n")
                      .append("                 ,   f.avtstep                                                                                                    \n")
                      .append("                 ,   f.avmtest                                                                                                    \n")
                      .append("                 ,   f.avftest                                                                                                    \n")
                      .append("                 ,   f.avreport                                                                                                   \n")
                      .append("                 ,   f.avact                                                                                                      \n")
                      .append("                 ,   f.avetc1                                                                                                     \n")
                      .append("                 ,   f.avetc2                                                                                                     \n")
                      .append("                 ,   f.htest                                                                                                      \n")
                      .append("                 ,   f.avhtest                                                                                                    \n")
                      .append("                 ,   g.isgraduated       course_graduated                                                                         \n")
                      .append("             FROM    tz_pfcourse         a                                                                                        \n")
                      .append("                 ,   tz_pfcourseseq      b                                                                                        \n")
                      .append("                 ,   tz_pfproposesubj    c                                                                                        \n")
                      .append("                 ,   tz_member           d                                                                                        \n")
                      .append("                 ,   tz_subj             e                                                                                        \n")
                      .append("                 ,   tz_student            f                                                                                        \n")
                      .append("                 ,   tz_pfstudent        g                                                                                        \n")
                      .append("             WHERE   a.coursecode    = b.coursecode                                                                               \n")
                      .append("             AND     b.coursecode    = c.coursecode                                                                               \n")
                      .append("             AND     b.courseyear    = c.courseyear                                                                               \n")
                      .append("             AND     b.courseseq     = c.courseseq                                                                                \n")
                      .append("             AND     c.userid        = d.userid                                                                                   \n")
                      .append("             AND     c.subj          = e.subj                                                                                     \n")
                      .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                      .append("             AND     c.userid        = f.userid(+)                                                                                \n")
                      .append("             AND     c.userid        = g.userid                                                                                   \n")
                      .append("             AND     b.coursecode    = g.coursecode                                                                               \n")
                      .append("             AND     b.courseyear    = g.courseyear                                                                               \n")
                      .append("             AND     b.courseseq     = g.courseseq                                                                                \n")
                      .append("             AND     c.status in ( 'RE', 'FE' )                                                                                   \n")
                      .append("             AND     c.subj          = f.subj(+)                                                                                  \n")
                      .append("             AND     c.year          = f.year(+)                                                                                  \n")
                      .append("             AND     c.subjseq       = f.subjseq(+)                                                                               \n")
                      .append("             GROUP BY b.grcode                                                                                                    \n")
                      .append("                 ,    b.coursecode                                                                                                \n")
                      .append("                 ,    b.courseyear                                                                                                \n")
                      .append("                 ,    b.courseseq                                                                                                 \n")
                      .append("                 ,    a.coursenm                                                                                                  \n")
                      .append("                 ,    d.userid                                                                                                    \n")
                      .append("                 ,    d.name                                                                                                      \n")
                      .append("                 ,    e.subj                                                                                                      \n")
                      .append("                 ,    e.subjnm                                                                                                    \n")
                      .append("                 ,    c.isrequired                                                                                                \n")
                      .append("                 ,    f.isgraduated                                                                                               \n")
                      .append("                 ,    f.score                                                                                                     \n")
                      .append("                 ,    f.tstep                                                                                                     \n")
                      .append("                 ,    f.mtest                                                                                                     \n")
                      .append("                 ,    f.ftest                                                                                                     \n")
                      .append("                 ,    f.report                                                                                                    \n")
                      .append("                 ,    f.act                                                                                                       \n")
                      .append("                 ,    f.etc1                                                                                                      \n")
                      .append("                 ,    f.etc2                                                                                                      \n")
                      .append("                 ,    f.avtstep                                                                                                   \n")
                      .append("                 ,    f.avmtest                                                                                                   \n")
                      .append("                 ,    f.avftest                                                                                                   \n")
                      .append("                 ,    f.avreport                                                                                                  \n")
                      .append("                 ,    f.avact                                                                                                     \n")
                      .append("                 ,    f.avetc1                                                                                                    \n")
                      .append("                 ,    f.avetc2                                                                                                    \n")
                      .append("                 ,    f.htest                                                                                                     \n")
                      .append("                 ,    f.avhtest                                                                                                   \n")
                      .append("                 ,    g.isgraduated                                                                                               \n")
                      .append("             ORDER BY name, isrequired desc                                                                                       \n")
                      .append("         )           a                                                                                                            \n")
                      .append("     ,   (                                                                                                                        \n")
                      .append("             SELECT  grcode                                                                                                       \n")
                      .append("                 ,   courseyear                                                                                                   \n")
                      .append("                 ,   courseseq                                                                                                    \n")
                      .append("                 ,   coursecode                                                                                                   \n")
                      .append("                 ,   userid                                                                                                       \n")
                      .append("                 ,   avg(score                                                               )   total                            \n")
                      .append("                 ,   sum(case when isrequired = 'N' and isgraduated = 'Y' then 1 else 0 end  )   graduated_n_count                \n")
                      .append("                 ,   max(subjnumforcomplete)                                                     requried_n_count                 \n")
                      .append("                 ,   sum(case when isrequired = 'Y' and isgraduated = 'Y' then 1 else 0 end  )   graduated_y_count                \n")
                      .append("                 ,   sum(decode(isrequired, 'Y', 1, 0))                                          requried_y_count                 \n")
                      .append("             FROM    (                                                                                                            \n")
                      .append("                         SELECT  b.grcode                                                                                         \n")
                      .append("                             ,   a.coursenm                                                                                       \n")
                      .append("                             ,   b.coursecode                                                                                     \n")
                      .append("                             ,   b.courseyear                                                                                     \n")
                      .append("                             ,   b.courseseq                                                                                      \n")
                      .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                      .append("                             ,   d.userid                                                                                         \n")
                      .append("                             ,   d.name                                                                                           \n")
                      .append("                             ,   e.subj                                                                                           \n")
                      .append("                             ,   e.subjnm                                                                                         \n")
                      .append("                             ,   c.isrequired                                                                                     \n")
                      .append("                             ,   f.isgraduated                                                                                    \n")
                      .append("                             ,   f.score                                                                                          \n")
                      .append("                             ,   f.tstep                                                                                          \n")
                      .append("                             ,   f.mtest                                                                                          \n")
                      .append("                             ,   f.ftest                                                                                          \n")
                      .append("                             ,   f.report                                                                                         \n")
                      .append("                             ,   f.act                                                                                            \n")
                      .append("                             ,   f.etc1                                                                                           \n")
                      .append("                             ,   f.etc2                                                                                           \n")
                      .append("                             ,   f.avtstep                                                                                        \n")
                      .append("                             ,   f.avmtest                                                                                        \n")
                      .append("                             ,   f.avftest                                                                                        \n")
                      .append("                             ,   f.avreport                                                                                       \n")
                      .append("                             ,   f.avact                                                                                          \n")
                      .append("                             ,   f.avetc1                                                                                         \n")
                      .append("                             ,   f.avetc2                                                                                         \n")
                      .append("                             ,   f.htest                                                                                          \n")
                      .append("                             ,   f.avhtest                                                                                        \n")
                      .append("                             ,   h.isgraduated course_graduated                                                                   \n")
                      .append("                         from    tz_pfcourse         a                                                                            \n")
                      .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                      .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                      .append("                             ,   tz_member           d                                                                            \n")
                      .append("                             ,   tz_subj             e                                                                            \n")
                      .append("                             ,   tz_student            f                                                                            \n")
                      .append("                             ,   (                                                                                                \n")
                      .append("                                     select userid, subj, max(score) max_score                                                    \n")
                      .append("                                     from tz_student                                                                                \n")
                      .append("                                     group by userid, subj                                                                        \n")
                      .append("                                 )                   g                                                                            \n")
                      .append("                             ,        tz_pfstudent   h                                                                            \n")
                      .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                      .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                      .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                      .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                      .append("                         and     c.userid        = d.userid                                                                       \n")
                      .append("                         and     c.subj          = e.subj                                                                         \n")
                      .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                      .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                      .append("                         and     c.userid        = h.userid                                                                       \n")
                      .append("                         and     b.coursecode    = h.coursecode                                                                   \n")
                      .append("                         and     b.courseyear    = h.courseyear                                                                   \n")
                      .append("                         and     b.courseseq     = h.courseseq                                                                    \n")
                      .append("                         and     c.status        = 'RV'                                                                           \n")
                      .append("                         and     f.userid        = g.userid                                                                       \n")
                      .append("                         and     f.subj          = g.subj                                                                         \n")
                      .append("                         and     g.max_score     = f.score                                                                        \n")
                      .append("                         group by b.grcode                                                                                        \n")
                      .append("                             ,    b.coursecode                                                                                    \n")
                      .append("                             ,    b.courseyear                                                                                    \n")
                      .append("                             ,    b.courseseq                                                                                     \n")
                      .append("                             ,    a.coursenm                                                                                      \n")
                      .append("                             ,    d.userid                                                                                        \n")
                      .append("                             ,    d.name                                                                                          \n")
                      .append("                             ,    e.subj                                                                                          \n")
                      .append("                             ,    e.subjnm                                                                                        \n")
                      .append("                             ,    c.isrequired                                                                                    \n")
                      .append("                             ,    f.isgraduated                                                                                   \n")
                      .append("                             ,    f.score                                                                                         \n")
                      .append("                             ,    f.tstep                                                                                         \n")
                      .append("                             ,    f.mtest                                                                                         \n")
                      .append("                             ,    f.ftest                                                                                         \n")
                      .append("                             ,    f.report                                                                                        \n")
                      .append("                             ,    f.act                                                                                           \n")
                      .append("                             ,    f.etc1                                                                                          \n")
                      .append("                             ,    f.etc2                                                                                          \n")
                      .append("                             ,    f.avtstep                                                                                       \n")
                      .append("                             ,    f.avmtest                                                                                       \n")
                      .append("                             ,    f.avftest                                                                                       \n")
                      .append("                             ,    f.avreport                                                                                      \n")
                      .append("                             ,    f.avact                                                                                         \n")
                      .append("                             ,    f.avetc1                                                                                        \n")
                      .append("                             ,    f.avetc2                                                                                        \n")
                      .append("                             ,    f.htest                                                                                         \n")
                      .append("                             ,    f.avhtest                                                                                       \n")
                      .append("                             ,    h.isgraduated                                                                                   \n")
                      .append("                         UNION                                                                                                    \n")
                      .append("                         select  b.grcode                                                                                         \n")
                      .append("                             ,   a.coursenm                                                                                       \n")
                      .append("                             ,   b.coursecode                                                                                     \n")
                      .append("                             ,   b.courseyear                                                                                     \n")
                      .append("                             ,   b.courseseq                                                                                      \n")
                      .append("                             ,   max(b.subjnumforcomplete) subjnumforcomplete                                                     \n")
                      .append("                             ,   d.userid                                                                                         \n")
                      .append("                             ,   d.name                                                                                           \n")
                      .append("                             ,   e.subj                                                                                           \n")
                      .append("                             ,   e.subjnm                                                                                         \n")
                      .append("                             ,   c.isrequired                                                                                     \n")
                      .append("                             ,   f.isgraduated                                                                                    \n")
                      .append("                             ,   f.score                                                                                          \n")
                      .append("                             ,   f.tstep                                                                                          \n")
                      .append("                             ,   f.mtest                                                                                          \n")
                      .append("                             ,   f.ftest                                                                                          \n")
                      .append("                             ,   f.report                                                                                         \n")
                      .append("                             ,   f.act                                                                                            \n")
                      .append("                             ,   f.etc1                                                                                           \n")
                      .append("                             ,   f.etc2                                                                                           \n")
                      .append("                             ,   f.avtstep                                                                                        \n")
                      .append("                             ,   f.avmtest                                                                                        \n")
                      .append("                             ,   f.avftest                                                                                        \n")
                      .append("                             ,   f.avreport                                                                                       \n")
                      .append("                             ,   f.avact                                                                                          \n")
                      .append("                             ,   f.avetc1                                                                                         \n")
                      .append("                             ,   f.avetc2                                                                                         \n")
                      .append("                             ,   f.htest                                                                                          \n")
                      .append("                             ,   f.avhtest                                                                                        \n")
                      .append("                             ,   g.isgraduated course_graduated                                                                   \n")
                      .append("                         from    tz_pfcourse         a                                                                            \n")
                      .append("                             ,   tz_pfcourseseq      b                                                                            \n")
                      .append("                             ,   tz_pfproposesubj    c                                                                            \n")
                      .append("                             ,   tz_member           d                                                                            \n")
                      .append("                             ,   tz_subj             e                                                                            \n")
                      .append("                             ,   tz_student            f                                                                            \n")
                      .append("                             ,   tz_pfstudent        g                                                                            \n")
                      .append("                         where   a.coursecode    = b.coursecode                                                                   \n")
                      .append("                         and     b.coursecode    = c.coursecode                                                                   \n")
                      .append("                         and     b.courseyear    = c.courseyear                                                                   \n")
                      .append("                         and     b.courseseq     = c.courseseq                                                                    \n")
                      .append("                         and     c.userid        = d.userid                                                                       \n")
                      .append("                         and     c.subj          = e.subj                                                                         \n")
                      .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                      .append("                         and     c.userid        = f.userid(+)                                                                    \n")
                      .append("                         and     c.userid        = g.userid                                                                       \n")
                      .append("                         and     b.coursecode    = g.coursecode                                                                   \n")
                      .append("                         and     b.courseyear    = g.courseyear                                                                   \n")
                      .append("                         and     b.courseseq     = g.courseseq                                                                    \n")
                      .append("                         and     c.status in ( 'RE', 'FE' )                                                                       \n")
                      .append("                         and     c.subj          = f.subj(+)                                                                      \n")
                      .append("                         and     c.year          = f.year(+)                                                                      \n")
                      .append("                         and     c.subjseq       = f.subjseq(+)                                                                   \n")
                      .append("                         group by b.grcode                                                                                        \n")
                      .append("                             , b.coursecode                                                                                       \n")
                      .append("                             , b.courseyear                                                                                       \n")
                      .append("                             , b.courseseq                                                                                        \n")
                      .append("                             , a.coursenm                                                                                         \n")
                      .append("                             , d.userid                                                                                           \n")
                      .append("                             , d.name                                                                                             \n")
                      .append("                             , e.subj                                                                                             \n")
                      .append("                             , e.subjnm                                                                                           \n")
                      .append("                             , c.isrequired                                                                                       \n")
                      .append("                             , f.isgraduated                                                                                      \n")
                      .append("                             , f.score                                                                                            \n")
                      .append("                             , f.tstep                                                                                            \n")
                      .append("                             , f.mtest                                                                                            \n")
                      .append("                             , f.ftest                                                                                            \n")
                      .append("                             , f.report                                                                                           \n")
                      .append("                             , f.act                                                                                              \n")
                      .append("                             , f.etc1                                                                                             \n")
                      .append("                             , f.etc2                                                                                             \n")
                      .append("                             , f.avtstep                                                                                          \n")
                      .append("                             , f.avmtest                                                                                          \n")
                      .append("                             , f.avftest                                                                                          \n")
                      .append("                             , f.avreport                                                                                         \n")
                      .append("                             , f.avact                                                                                            \n")
                      .append("                             , f.avetc1                                                                                           \n")
                      .append("                             , f.avetc2                                                                                           \n")
                      .append("                             , f.htest                                                                                            \n")
                      .append("                             , f.avhtest                                                                                          \n")
                      .append("                             , g.isgraduated                                                                                      \n")
                      .append("                         order by name, isrequired desc                                                                           \n")
                      .append("                     )                                                                                                            \n")
                      .append("             group by grcode, courseyear, courseseq, coursecode, userid                                                           \n")
                      .append("         )                b                                                                                                       \n");
                 
                 
                 if ( (!v_sido.equals("") || !v_age.equals("") || !v_achievement.equals("") || !v_gender.equals("") || !v_gugun.equals("")) && !(v_orgcode.equals("") || v_orgcode.equals("ALL")) ) { 
                     sbSQL.append("  ,  vz_orgmember      c                                                                                                                                                       \n");
                 } else if ( (!v_sido.equals("") || !v_age.equals("") || !v_achievement.equals("") || !v_gender.equals("") || !v_gugun.equals("")) && (v_orgcode.equals("") || v_orgcode.equals("ALL")) ) {
                     sbSQL.append("  ,  vz_orgmember1     c                                                                                                                                                       \n");
                 }
                 
                 sbSQL.append("      ,  Tz_Member         d                                                                                                      \n")
                      .append(" WHERE   a.grcode        = " + StringManager.makeSQL(v_grcode         ) + "                                                       \n")
                      .append(" and     a.courseyear    = " + StringManager.makeSQL(v_courseyear     ) + "                                                       \n");

                  if ( !v_coursecode.equals("") ) { 
                      sbSQL.append(" and a.coursecode    = " + StringManager.makeSQL(v_coursecode    ) + "   \n");
                  }
                  
                  if ( !v_courseseq.equals("") ) { 
                      sbSQL.append(" and a.courseseq     = " + StringManager.makeSQL(v_courseseq     ) + "   \n");
                  }
                  
                  if ( v_isgraduated.equals("Y") ) {
                      sbSQL.append(" and a.course_graduated   = " + StringManager.makeSQL(v_isgraduated   ) + "   \n");
                  } else if ( v_isgraduated.equals("N") ) {
                      sbSQL.append(" and ( a.course_graduated   IS NULL OR a.course_graduated = " + StringManager.makeSQL(v_isgraduated   ) + " ) \n");
                  }
                  
                  if ( (!v_sido.equals("") || !v_age.equals("") || !v_achievement.equals("") || !v_gender.equals("") || !v_gugun.equals("") || !(v_orgcode.equals("ALL") || v_orgcode.equals("ALL")) ) ) { 
                      sbSQL.append("                          AND     a.userid        = c.userid(+)                                                       \n");
                  }
                  
                  if ( !(v_orgcode.equals("") || v_orgcode.equals("ALL")) ) { 
                      sbSQL.append("                          AND     c.orgcode       = " + v_orgcode                                 + "                 \n");
                  }
                  
                  if ( !v_sido.equals("") ) {
                      sbSQL.append("             AND trim(c.sido)               = " + StringManager.makeSQL(v_sido          ) + "                                       \n");
                  }    

                  if ( !v_age.equals("") ) {
                      sbSQL.append("             AND trim(c.age)               = " + StringManager.makeSQL(v_age            ) + "                                       \n");               
                  }
                  
                  if ( !v_achievement.equals("") ) {
                      sbSQL.append("             AND trim(c.achievement)       = " + StringManager.makeSQL(v_achievement    ) + "                                       \n");               
                  }
                  
                  if ( !v_gender.equals("") ) {
                      sbSQL.append("             AND trim(c.gender)            = " + StringManager.makeSQL(v_gender         ) + "                                       \n");               
                  }
                  
                  if ( !v_gugun.equals("") ) {
                      sbSQL.append("             AND trim(c.gugun)             = " + StringManager.makeSQL(v_gugun          ) + "                                       \n");
                  }
                  
                  if ( !box.getString("p_sdate").equals("") && !box.getString("p_ldate").equals("") ) { // 기간선택
                      if ( box.getString("p_sdate").equals(box.getString("p_ldate")) ) {
                          sbSQL.append("          AND TO_CHAR(TO_DATE(d.indate, 'yyyymmddhh24miss'), 'yyyymmdd') = TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') \n");
                      } else {
                          sbSQL.append("          AND TO_CHAR(TO_DATE(d.indate, 'yyyymmddhh24miss'), 'yyyymmdd') BETWEEN TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') AND TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_ldate")) + ", 'yyyy-mm-dd'), 'yyyymmdd') \n");
                      }
                  }
                  
                  sbSQL.append(" and a.userid = b.userid                                                                                                         \n")
                       .append(" and a.userid = d.userid                                                                                                         \n")
                       .append(" GROUP BY a.grcode, a.courseyear, a.coursecode, a.courseseq, a.name, a.userid                                                    \n")
                       .append(" ORDER BY a.grcode, a.courseyear, a.coursecode, a.courseseq, a.name, a.userid                                                    \n");
                      
                 System.out.println("SQL.toString11111 : \n" + sbSQL.toString() );
                 
                 ls = connMgr.executeQuery(sbSQL.toString());
         
                 while ( ls.next() ) { 
                     dbox = ls.getDataBox();
                     list.add(dbox);
                 }
             } catch ( Exception ex ) { 
                 ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
                 throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
             } finally { 
                 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
             }
             
             return list;
         }     

    /**
     * 전문가 양성 과정 개인별 온라인 성적 상세보기 rowspan 카운트 
     */
     public ArrayList selectRowspanCount1(RequestBox box) throws Exception {
         DBConnectionManager connMgr         = null;
         ListSet             ls              = null;
         ArrayList           list            = null;
         StringBuffer        sbSQL           = new StringBuffer("");
         
         DataBox             dbox            = null;
         
         String              v_Bcourse       = ""; // 이전코스
         String              v_course        = ""; // 현재코스
         String              v_Bcourseseq    = ""; // 이전코스기수
         String              v_courseseq     = ""; // 현재코스기수
         
         String              ss_grcode       = box.getStringDefault  ( "s_grcode"        , "ALL" );  // 교육그룹
         String              ss_pfcourseyear = box.getStringDefault  ( "s_pfcourseyear"  , "ALL" );  // 년도
         String              ss_pfcourseseq  = box.getStringDefault  ( "s_pfcourseseq"   , "ALL" );  // 과목 기수
         String              ss_pfcourse     = box.getStringDefault  ( "s_pfcourse"      , "ALL" );  // 과목&코스
         String              ss_action       = box.getString         ( "s_action"                );
         String              v_orgcode       = box.getStringDefault  ( "p_orgcode"       , "ALL" );  // 조직코드
         String              c_sido          = box.getString         ( "c_sido"                  );
         String              c_gugun         = box.getString         ( "c_gugun"                 );
         String              c_gender        = box.getString         ( "c_gender"                );
         String              c_achievement   = box.getString         ( "c_achievement"           );
         String              c_age           = box.getString         ( "c_age"                   );
         String              c_gubun           = box.getString       ( "c_gubun"                 );
         
         String              v_iscomplete    = box.getString         ( "p_iscomplete"            );
         
         String              v_orderColumn   = box.getString( "p_orderColumn"  );  // 정렬할 컬럼명
         String              v_orderType     = box.getString( "p_orderType"    );  // 정렬할 순서
         
         try {
             connMgr = new DBConnectionManager();            
             list    = new ArrayList();
                   
             sbSQL.append(" select a.userid, count(*) count                                        \n")
                  .append(" from tz_pfproposesubj a, tz_pfcourseseq b, tz_member c, vz_orgmember d \n")
                  .append(" where a.coursecode   = b.coursecode                                    \n")
                  .append(" and a.courseyear     = b.courseyear                                    \n")
                  .append(" and a.courseseq      = b.courseseq                                     \n")
                  .append(" and a.userid         = c.userid                                        \n")
                  .append(" and b.grcode     = " + StringManager.makeSQL(ss_grcode) + "            \n")
                  .append(" and b.courseyear = " + StringManager.makeSQL(ss_pfcourseyear) +  "     \n");
             
             if ( !ss_pfcourse.equals("ALL") ) { 
                 sbSQL.append(" and b.coursecode = " + StringManager.makeSQL(ss_pfcourse)    + "     \n");
             }
             
             if ( !ss_pfcourseseq.equals("ALL") ) { 
                 sbSQL.append(" and b.courseseq = " + StringManager.makeSQL(ss_pfcourseseq)  + "     \n");
             }
             
             sbSQL.append(" AND     c.userid     = d.userid(+)                                       \n");
                       
                  if ( !box.getString("p_sdate").equals("") && !box.getString("p_ldate").equals("") ) { // 기간선택
                      if ( box.getString("p_sdate").equals(box.getString("p_ldate")) ) {
                          sbSQL.append("          AND TO_CHAR(TO_DATE(d.indate, 'yyyymmddhh24miss'), 'yyyymm') = TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymm') \n");
                      } else {
                          sbSQL.append("          AND TO_CHAR(TO_DATE(d.indate, 'yyyymmddhh24miss'), 'yyyymm') BETWEEN TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_sdate")) + ", 'yyyy-mm-dd'), 'yyyymm') AND TO_CHAR(TO_DATE(" + StringManager.makeSQL(box.getString("p_ldate")) + ", 'yyyy-mm-dd'), 'yyyymm') \n");
                      }
                  }
                  
                  if ( !c_sido.equals("") ) {
                      sbSQL.append("             AND d.sido               = " + StringManager.makeSQL(c_sido          ) + "                                       \n");
                  }    

                  if ( !c_age.equals("") ) {
                      sbSQL.append("             AND d.age               = " + StringManager.makeSQL(c_age            ) + "                                       \n");               
                  }
                  
                  if ( !c_achievement.equals("") ) {
                      sbSQL.append("             AND d.achievement       = " + StringManager.makeSQL(c_achievement    ) + "                                       \n");               
                  }
                  
                  if ( !c_gender.equals("") ) {
                      sbSQL.append("             AND d.gender            = " + StringManager.makeSQL(c_gender         ) + "                                       \n");               
                  }
                  
                  if ( !c_gugun.equals("") ) {
                      sbSQL.append("             AND d.gugun             = " + StringManager.makeSQL(c_gubun          ) + "                                       \n");
                  }
                  
                  if ( !v_orgcode.equals("ALL") ) { 
                      sbSQL.append("            AND  d.orgcode           = " + v_orgcode                                + "                                       \n");
                  }
                  
                  sbSQL.append(" group by a.userid, c.name                               \n")
                       .append(" order by c.name                                         \n");
                  
                  
                   System.out.println("SQL.toString : \n" + sbSQL.toString() );
                   
                   ls = connMgr.executeQuery(sbSQL.toString());
           
                   while ( ls.next() ) { 
                       dbox = ls.getDataBox();
                       list.add(dbox);
                   }
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
             throw new Exception("sql1 = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
         } finally { 
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
               
         return list;
     }
}