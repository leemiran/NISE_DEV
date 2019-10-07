// **********************************************************
//  1. 제      목: 과목OPERATION BEAN
//  2. 프로그램명: SubjectBean.java
//  3. 개      요: 과목OPERATION BEAN
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 노희성 2004. 11. 14
//  7. 수      정: 정기현 2006. 06. 01
// **********************************************************
package com.ziaan.course;

import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class BasicSubjectBean { 

    public final static String LANGUAGE_GUBUN   = "0017";
    public final static String ONOFF_GUBUN      = "0004";

    public BasicSubjectBean() { }

    
    /**
    과목리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   과목리스트
    */
    public ArrayList SelectSubjectList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        DataBox             dbox    = null;
        
        System.out.println(this.getClass().getName() + "." + "SelectSubjectList().SQL" + ++iSysAdd +  " : " + " [\n" + sbSQL.toString() + "\n]");
        
        String              v_upperclass    = box.getStringDefault("p_upperclass"   , "ALL");   // 과목분류
        
        try { 
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
            
            System.out.println(this.getClass().getName() + "." + "SelectSubjectList().SQL" + ++iSysAdd +  " : " + " [\n" + sbSQL.toString() + "\n]");
            
            sbSQL.append(" SELECT  a.upperclass, b.classname   , a.isonoff     , c.codenm                               \n")
                 .append("     ,   a.subj      , a.subjnm      , a.muserid     , d.name                                 \n")
                 .append("     ,   a.isuse     , a.isapproval  , a.isintroduction                                       \n")
                 .append("     ,   a.introducefilenamereal     , a.introducefilenamenew                                 \n")
                 .append("     ,   a.informationfilenamereal   , a.informationfilenamenew                               \n")
                 .append("  ,   a.edudays   , a.edutimes    , eduperiod                                                 \n")
                 .append("  ,   a.isvisible , DECODE(a.isvisible, 'Y', 'Y', 'N')    isopentime                          \n")
                 .append("  ,   a.crdate    , TO_CHAR(TO_DATE(a.crdate, 'YYYYMMDD'), 'YY\"년\" MM\"월\"') CrYearMonth    \n")
                 .append("  ,   a.eduperiod || '개월' eduperiodname                                                      \n")
                 .append(" FROM    tz_subj     a                                                                        \n")
                 .append("     ,   tz_subjatt  b                                                                        \n")
                 .append("     ,   tz_code     c                                                                        \n")
                 .append("     ,   tz_member   d                                                                        \n")
                 .append("     ,   tz_grsubj   e                                                                        \n")
                 .append(" WHERE   a.upperclass  = b.upperclass                                                         \n")
                 .append(" AND     a.isonoff     = c.code                                                               \n")                             
                 .append(" AND     e.grcode      = 'N000001'                                                            \n")                             
                 .append(" AND     a.isonoff     = 'ON'                                                                 \n")                             
                 .append(" AND     a.subj        = e.subjcourse                                                         \n")                             
                 .append(" AND     a.muserid     = d.userid( +)                                                         \n")
                 .append(" AND     b.middleclass = '000'                                                                \n")
                 .append(" AND     b.lowerclass  = '000'                                                                \n")
                 .append(" AND     a.isuse       = 'Y'                                                                  \n")
                 .append(" AND     c.gubun       = " + SQLString.Format(ONOFF_GUBUN) + "                                \n");
            
            if ( !v_upperclass.equals("ALL") ) { 
                if ( !v_upperclass.equals("ALL") ) { 
                    sbSQL.append(" and a.upperclass  = " + StringManager.makeSQL(v_upperclass ) + "\n");
                }
            }

            sbSQL.append(" order by DECODE(a.isvisible, 'Y', 'Y', 'N') DESC, a.upperclass, a.subj \n");

            System.out.println(this.getClass().getName() + "." + "SelectSubjectList().SQL" + ++iSysAdd +  " : " + " [\n" + sbSQL.toString() + "\n]");

            ls          = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch (Exception e ) { } 
            }
        }
        
        return list;
    }

    
    /**
    과목리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   과목리스트
    */
    public ArrayList SelectSubjectRoadList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        DataBox             dbox    = null;
        
        System.out.println(this.getClass().getName() + "." + "SelectSubjectList().SQL" + ++iSysAdd +  " : " + " [\n" + sbSQL.toString() + "\n]");
        
        String              v_upperclass    = box.getStringDefault("p_upperclass"   , "ALL");   // 과목분류
        
        try { 
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
            
            System.out.println(this.getClass().getName() + "." + "SelectSubjectList().SQL" + ++iSysAdd +  " : " + " [\n" + sbSQL.toString() + "\n]");

            sbSQL.append(" SELECT  a.upperclass, b.classname upperclassname, a.isonoff     , c.codenm                                       \n")                                                         
                 .append("     ,   a.subj      , a.subjnm      , a.muserid     , d.name                                                     \n")
                 .append("     ,   a.isuse     , a.isapproval  , a.isintroduction                                                           \n")
                 .append("     ,   a.introducefilenamereal     , a.introducefilenamenew                                                     \n")
                 .append("     ,   a.informationfilenamereal   , a.informationfilenamenew                                                   \n")
                 .append("     ,   a.edudays   , a.edutimes    , eduperiod                                                                  \n")
                 .append("     ,   a.isvisible , DECODE(a.isvisible, 'Y', 'Y', 'N')    isopentime                                           \n")
                 .append("     ,   a.crdate    , TO_CHAR(TO_DATE(a.crdate, 'YYYYMMDD'), 'YY\"년\" MM\"월\"') CrYearMonth                    \n")
                 .append("     ,   a.eduperiod || '개월' eduperiodname                                                                      \n")
                 .append("     ,   a.middleclass, e.classname middleclassname, COUNT(*) OVER(PARTITION BY a.Upperclass) upperrowspan        \n")
                 .append("     , COUNT(*) OVER(PARTITION BY a.Upperclass, a.middleclass) middlerowspan                                      \n")                                                    
                 .append(" FROM    tz_subj     a                                                                                            \n")
                 .append("     ,   tz_subjatt  b                                                                                            \n")
                 .append("     ,   tz_code     c                                                                                            \n")
                 .append("     ,   tz_member   d                                                                                            \n")
                 .append("     ,   tz_subjatt  e                                                                                            \n")
                 .append("     ,   tz_grsubj   e                                                                                            \n")
                 .append(" WHERE   a.upperclass  = b.upperclass                                                                             \n")
                 .append(" AND     a.isonoff     = c.code                                                                                   \n")
                 .append(" AND     e.grcode      = 'N000001'                                                                                \n")                             
                 .append(" AND     a.isonoff     = 'ON'                                                                                     \n")                             
                 .append(" AND     a.subj        = e.subjcourse                                                                             \n")                             
                 .append(" AND     a.muserid     = d.userid( +)                                                                             \n")
                 .append(" AND     b.middleclass = '000'                                                                                    \n")
                 .append(" AND     b.lowerclass  = '000'                                                                                    \n")
                 .append(" AND     a.upperclass  = e.upperclass                                                                             \n")   
                 .append(" AND     a.middleclass = e.middleclass                                                                            \n")    
                 .append(" AND     e.lowerclass  = '000'                                                                                    \n")
                 .append(" AND     a.isuse       = 'Y'                                                                                      \n")
                 .append(" AND     c.gubun       = '0004'                                                                                   \n")
                 .append(" order by  a.upperclass desc, a.middleclass, DECODE(a.isvisible, 'Y', 'Y', 'N') DESC, a.upperclass, a.subj        \n"); 

            System.out.println(this.getClass().getName() + "." + "SelectSubjectRoadList().SQL" + ++iSysAdd +  " : " + " [\n" + sbSQL.toString() + "\n]");

            ls          = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch (Exception e ) { } 
            }
        }
        
        return list;
    }

    
    /**
    과목리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   과목리스트
    */
    public ArrayList SelectSubjectYearList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수
        
        DataBox             dbox    = null;
        
        System.out.println(this.getClass().getName() + "." + "SelectSubjectList().SQL" + ++iSysAdd +  " : " + " [\n" + sbSQL.toString() + "\n]");
        
        String              v_upperclass    = box.getStringDefault("p_upperclass"   , "ALL" );   // 과목분류
        String              v_year          = box.getStringDefault("p_year"         , "2006");   // 년도
        
        try { 
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
            
            System.out.println(this.getClass().getName() + "." + "SelectSubjectList().SQL" + ++iSysAdd +  " : " + " [\n" + sbSQL.toString() + "\n]");


            sbSQL.append(" SELECT  v1.*                                                                                                                                         \n")
                 .append(" FROM    (                                                                                                                                            \n")
                 .append("             SELECT  v.subj                                                                                                                           \n")
                 .append("                 ,   MAX(v.upperclass                )   upperclass                                                                                   \n")
                 .append("                 ,   MAX(v.upperclassname            )   upperclassname                                                                               \n")
                 .append("                 ,   MAX(v.isonoff                   )   isonoff                                                                                      \n")
                 .append("                 ,   MAX(v.codenm                    )   codenm                                                                                       \n")
                 .append("                 ,   MAX(v.subjnm                    )   subjnm                                                                                       \n")
                 .append("                 ,   MAX(v.muserid                   )   muserid                                                                                      \n")
                 .append("                 ,   MAX(v.name                      )   name                                                                                         \n")
                 .append("                 ,   MAX(v.isuse                     )   isuse                                                                                        \n")
                 .append("                 ,   MAX(v.isapproval                )   isapproval                                                                                   \n")
                 .append("                 ,   MAX(v.isintroduction            )   isintroduction                                                                               \n")
                 .append("                 ,   MAX(v.introducefilenamereal     )   introducefilenamereal                                                                        \n")
                 .append("                 ,   MAX(v.introducefilenamenew      )   introducefilenamenew                                                                         \n")
                 .append("                 ,   MAX(v.informationfilenamereal   )   informationfilenamereal                                                                      \n")
                 .append("                 ,   MAX(v.informationfilenamenew    )   informationfilenamenew                                                                       \n")
                 .append("                 ,   MAX(v.edudays                   )   edudays                                                                                      \n")
                 .append("                 ,   MAX(v.edutimes                  )   edutimes                                                                                     \n")
                 .append("                 ,   MAX(v.eduperiod                 )   eduperiod                                                                                    \n")
                 .append("                 ,   MAX(v.isvisible                 )   isvisible                                                                                    \n")
                 .append("                 ,   MAX(v.isopentime                )   isopentime                                                                                   \n")
                 .append("                 ,   MAX(v.crdate                    )   crdate                                                                                       \n")
                 .append("                 ,   MAX(v.CrYearMonth               )   CrYearMonth                                                                                  \n")
                 .append("                 ,   MAX(v.eduperiodname             )   eduperiodname                                                                                \n")
                 .append("                 ,   MAX(v.middleclass               )   middleclass                                                                                  \n")
                 .append("                 ,   MAX(v.middleclassname           )   middleclassname                                                                              \n")
                 .append("                 ,   MAX(v.upperrowspan              )   upperrowspan                                                                                 \n")
                 .append("                 ,   MAX(v.middlerowspan             )   middlerowspan                                                                                \n")
                 .append("                 ,   MAX(v.compyear                  )   compyear                                                                                     \n")
                 .append("                 ,   MAX(v.mon01yn                   )   mon01yn                                                                                      \n")
                 .append("                 ,   MAX(v.mon02yn                   )   mon02yn                                                                                      \n")
                 .append("                 ,   MAX(v.mon03yn                   )   mon03yn                                                                                      \n")
                 .append("                 ,   MAX(v.mon04yn                   )   mon04yn                                                                                      \n")
                 .append("                 ,   MAX(v.mon05yn                   )   mon05yn                                                                                      \n")
                 .append("                 ,   MAX(v.mon06yn                   )   mon06yn                                                                                      \n")
                 .append("                 ,   MAX(v.mon07yn                   )   mon07yn                                                                                      \n")
                 .append("                 ,   MAX(v.mon08yn                   )   mon08yn                                                                                      \n")
                 .append("                 ,   MAX(v.mon09yn                   )   mon09yn                                                                                      \n")
                 .append("                 ,   MAX(v.mon10yn                   )   mon10yn                                                                                      \n")
                 .append("                 ,   MAX(v.mon11yn                   )   mon11yn                                                                                      \n")
                 .append("                 ,   MAX(v.mon12yn                   )   mon12yn                                                                                      \n")
                 .append("             FROM    (                                                                                                                                \n")
                 .append("                         SELECT  a.*                                                                                                                  \n")
                 .append("                 ,    CASE WHEN compyear || '01' BETWEEN SUBSTR(b.edustart, 1, 6) AND SUBSTR(eduend, 1, 6)  THEN 'Y'                          \n")
                 .append("                  ELSE                                                          'N'                                                           \n")
                 .append("                  END     mon01yn                                                                                                             \n")
                 .append("                 ,    CASE WHEN compyear || '02' BETWEEN SUBSTR(b.edustart, 1, 6)  AND SUBSTR(eduend, 1, 6)  THEN 'Y'                         \n")
                 .append("                  ELSE                                                          'N'                                                           \n")
                 .append("                  END     mon02yn                                                                                                             \n")
                 .append("                 ,    CASE WHEN compyear || '03' BETWEEN SUBSTR(b.edustart, 1, 6)  AND SUBSTR(eduend, 1, 6)  THEN 'Y'                         \n")
                 .append("                  ELSE                                                          'N'                                                           \n")
                 .append("                  END     mon03yn                                                                                                             \n")
                 .append("                 ,    CASE WHEN compyear || '04' BETWEEN SUBSTR(b.edustart, 1, 6)  AND SUBSTR(eduend, 1, 6)  THEN 'Y'                         \n")
                 .append("                  ELSE                                                          'N'                                                           \n")
                 .append("                  END     mon04yn                                                                                                             \n")
                 .append("                 ,    CASE WHEN compyear || '05' BETWEEN SUBSTR(b.edustart, 1, 6)  AND SUBSTR(eduend, 1, 6)  THEN 'Y'                         \n")
                 .append("                  ELSE                                                          'N'                                                           \n")
                 .append("                  END     mon05yn                                                                                                             \n")
                 .append("                 ,    CASE WHEN compyear || '06' BETWEEN SUBSTR(b.edustart, 1, 6)  AND SUBSTR(eduend, 1, 6)  THEN 'Y'                         \n")
                 .append("                  ELSE                                                          'N'                                                           \n")
                 .append("                  END     mon06yn                                                                                                             \n")
                 .append("                 ,    CASE WHEN compyear || '07' BETWEEN SUBSTR(b.edustart, 1, 6)  AND SUBSTR(eduend, 1, 6)  THEN 'Y'                         \n")
                 .append("                  ELSE                                                          'N'                                                           \n")
                 .append("                  END     mon07yn                                                                                                             \n")
                 .append("                 ,    CASE WHEN compyear || '08' BETWEEN SUBSTR(b.edustart, 1, 6)  AND SUBSTR(eduend, 1, 6)  THEN 'Y'                         \n")
                 .append("                  ELSE                                                          'N'                                                           \n")
                 .append("                  END     mon08yn                                                                                                             \n")
                 .append("                 ,    CASE WHEN compyear || '09' BETWEEN SUBSTR(b.edustart, 1, 6)  AND SUBSTR(eduend, 1, 6)  THEN 'Y'                         \n")
                 .append("                  ELSE                                                          'N'                                                           \n")
                 .append("                  END     mon09yn                                                                                                             \n")
                 .append("                 ,    CASE WHEN compyear || '10' BETWEEN SUBSTR(b.edustart, 1, 6)  AND SUBSTR(eduend, 1, 6)  THEN 'Y'                         \n")
                 .append("                  ELSE                                                          'N'                                                           \n")
                 .append("                  END     mon10yn                                                                                                             \n")
                 .append("                 ,    CASE WHEN compyear || '11' BETWEEN SUBSTR(b.edustart, 1, 6)  AND SUBSTR(eduend, 1, 6)  THEN 'Y'                         \n")
                 .append("                  ELSE                                                          'N'                                                           \n")
                 .append("                  END     mon11yn                                                                                                             \n")
                 .append("                 ,    CASE WHEN compyear || '12' BETWEEN SUBSTR(b.edustart, 1, 6)  AND SUBSTR(eduend, 1, 6)  THEN 'Y'                         \n")
                 .append("                  ELSE                                                          'N'                                                           \n")
                 .append("                  END     mon12yn                                                                                                             \n")
                 .append("                         FROM    (                                                                                                                    \n")
                 .append("                                     SELECT  a.upperclass, b.classname upperclassname, a.isonoff     , c.codenm                                       \n")
                 .append("                                         ,   a.subj      , a.subjnm      , a.muserid     , d.name                                                     \n")
                 .append("                                         ,   a.isuse     , a.isapproval  , a.isintroduction                                                           \n")
                 .append("                                         ,   a.introducefilenamereal     , a.introducefilenamenew                                                     \n")
                 .append("                                         ,   a.informationfilenamereal   , a.informationfilenamenew                                                   \n")
                 .append("                                         ,   a.edudays   , a.edutimes    , eduperiod                                                                  \n")
                 .append("                                         ,   a.isvisible , DECODE(a.isvisible, 'Y', 'Y', 'N')    isopentime                                           \n")
                 .append("                                         ,   a.crdate    , TO_CHAR(TO_DATE(a.crdate, 'YYYYMMDD'), 'YY\"년\" MM\"월\"') CrYearMonth                    \n")
                 .append("                                         ,   a.eduperiod || '개월' eduperiodname                                                                      \n")
                 .append("                                         ,   a.middleclass, e.classname middleclassname, COUNT(*) OVER(PARTITION BY a.Upperclass) upperrowspan        \n")
                 .append("                                         ,   COUNT(*) OVER(PARTITION BY a.Upperclass, a.middleclass) middlerowspan                                    \n")
                 .append("                                         ,   " + StringManager.makeSQL(v_year) + " compyear                                                           \n")
                 .append("                                     FROM    tz_subj     a                                                                                            \n")
                 .append("                                         ,   tz_subjatt  b                                                                                            \n")
                 .append("                                         ,   tz_code     c                                                                                            \n")
                 .append("                                         ,   tz_member   d                                                                                            \n")
                 .append("                                         ,   tz_subjatt  e                                                                                            \n")
                 .append("                                         ,   tz_grsubj   f                                                                                            \n")
                 .append("                                     WHERE   a.upperclass  = b.upperclass                                                                             \n")
                 .append("                                     AND     a.isonoff     = c.code                                                                                   \n")
                 .append("                                     AND     a.muserid     = d.userid( +)                                                                             \n")
                 .append("                                     AND     f.grcode      = 'N000001'                                                                                \n")                             
                 .append("                                     AND     a.isonoff     = 'ON'                                                                                     \n")                             
                 .append("                                     AND     a.subj        = f.subjcourse                                                                             \n")                             
                 .append("                                     AND     b.middleclass = '000'                                                                                    \n")
                 .append("                                     AND     b.lowerclass  = '000'                                                                                    \n")
                 .append("                                     AND     a.upperclass  = e.upperclass                                                                             \n")
                 .append("                                     AND     a.middleclass = e.middleclass                                                                            \n")
                 .append("                                     AND     e.lowerclass  = '000'                                                                                    \n")
                 .append("                                     AND     a.isuse       = 'Y'                                                                                      \n")
                 .append("                                     AND     c.gubun       = '0004'                                                                                   \n")
                 .append("                                 )           a                                                                                                        \n")
                 .append("                             ,   Tz_subjseq  b                                                                                                        \n")
                 .append("                         WHERE   a.subj  = b.subj(+)                                                                                                  \n")
                 .append("                     )       v                                                                                                                        \n")
                 .append("             GROUP BY v.subj                                                                                                                          \n")
                 .append("         )   v1                                                                                                                                       \n")
                 .append(" ORDER BY  v1.upperclass desc, v1.middleclass, DECODE(v1.isvisible, 'Y', 'Y', 'N') DESC                                                               \n");


            System.out.println(this.getClass().getName() + "." + "SelectSubjectRoadList().SQL" + ++iSysAdd +  " : " + " [\n" + sbSQL.toString() + "\n]");

            ls          = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) {
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch (Exception e ) { } 
            }
        }
        
        return list;
    }
}
