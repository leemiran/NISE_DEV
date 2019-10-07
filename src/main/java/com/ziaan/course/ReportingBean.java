// **********************************************************
//  1. 제      목: 레포팅
//  2. 프로그램명: ReportingBean.java
//  3. 개      요: 레포팅 관리
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0 QnA
//  6. 작      성: 정은년 2005. 9. 1
//  7. 수      정:
// **********************************************************
package com.ziaan.course    ;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ReportingBean { 
    
    public ReportingBean() { }
    
    // 5점척도
    public ArrayList SelectSulmumList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	    = null;
        ListSet             ls          = null;
        ArrayList           list1       = null;
        
        StringBuffer        sbSQL       = new StringBuffer("");
        DataBox             dbox        = null;
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_grcode    = box.getString("s_grcode");
        String              v_year      = box.getString("s_gyear");
        String              v_subjseqgr = box.getString("s_grseq");
       
        try {     
            connMgr = new DBConnectionManager();
            list1   = new ArrayList();
            
            sbSQL.append(" SELECT  c.usercnt                                                    \n")
                 .append("     ,   b.subjnm                                                     \n")
                 .append("     ,   a.subj                                                       \n")
                 .append("     ,   a.subjseq                                                    \n")
                 .append("     ,   a.userid                                                     \n")
                 .append("     ,   decode(a.distcode1,5,1,4,2,3,3,2,4,1,5,0) dcode1             \n")
                 .append("     ,   decode(a.distcode2,5,1,4,2,3,3,2,4,1,5,0) dcode2             \n")
                 .append("     ,   decode(a.distcode3,5,1,4,2,3,3,2,4,1,5,0) dcode3             \n")
                 .append("     ,   decode(a.distcode4,5,1,4,2,3,3,2,4,1,5,0) dcode4             \n")
                 .append("     ,   decode(a.distcode5,5,1,4,2,3,3,2,4,1,5,0) dcode5             \n")
                 .append("     ,   decode(a.distcode6,5,1,4,2,3,3,2,4,1,5,0) dcode6             \n")
                 .append(" FROM    tz_suleach      a                                            \n")
                 .append("     ,   tz_subjseq      b                                            \n")
                 .append("     ,   (                                                            \n")
                 .append("             SELECT  subj                                             \n")
                 .append("                 ,   subjseq                                          \n")
                 .append("                 ,   count(userid) usercnt                            \n")
                 .append("             FROM    tz_suleach                                       \n")
                 .append("             WHERE   subj not in ('CP','TARGET', 'CONTENTS')          \n")
                 .append("             AND     grcode != 'ALL'                                  \n")
                 .append("             GROUP BY subj, subjseq                                   \n")
                 .append("         )               c                                            \n")
                 .append(" WHERE   a.subj      = b.subj                                         \n")
                 .append(" AND     a.subjseq   = b.subjseq                                      \n")
                 .append(" AND     a.subj      = c.subj                                         \n")
                 .append(" AND     a.subjseq   = c.subjseq                                      \n")
                 .append(" AND     a.subj NOT IN ('CP','TARGET', 'CONTENTS')                    \n")
                 .append(" AND     a.grcode    != 'ALL'                                         \n");
       
            if ( !v_grcode.equals("ALL") )
                sbSQL.append(" AND     b.grcode    = " + StringManager.makeSQL(v_grcode      ) + "   \n");
           
            if ( !v_year.equals("ALL") )
                sbSQL.append(" AND     b.year      = " + StringManager.makeSQL(v_year        ) + "   \n");
           
            if ( !v_subjseqgr.equals("ALL") )
                sbSQL.append(" AND     b.grseq     = " + StringManager.makeSQL(v_subjseqgr   ) + "   \n");
           
            sbSQL.append(" ORDER BY a.subj asc, a.subjseq, a.userid                             \n");
            
            System.out.println(this.getClass().getName() + "." + "SelectSulmumList() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls          = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
                list1.add(dbox);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
                
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return list1;
    }
    

    // 시간대별학습비율
    public ArrayList SelectSubjTimeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	    = null;
        ListSet             ls          = null;
        ArrayList           list1       = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        
        DataBox             dbox        = null;
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_grcode    = box.getString("s_grcode"  );
        String              v_year      = box.getString("s_gyear"   );
        String              v_subjseqgr = box.getString("s_grseq"   );
       
        try {
            connMgr = new DBConnectionManager();
            list1   = new ArrayList();
            
            sbSQL.append(" SELECT  date_time                                                                                        \n")
                 .append("     ,   sum(cnt) cnt                                                                                     \n")
                 .append(" FROM    TZ_SUBJCOUNT                                                                                     \n")
                 .append(" WHERE   subj || date_year || subjseq in (                                                                \n")
                 .append("                                             SELECT  scsubj || scyear || scsubjseq                        \n")
                 .append("                                             FROM    vz_scsubjseq                                         \n")
                 .append("                                             WHERE   grcode  = " + StringManager.makeSQL(v_grcode ) + "   \n")
                 .append("                                             and     gyear   = " + StringManager.makeSQL(v_year   ) + "   \n")
                 .append("                                          )                                                               \n");
            
            if ( !v_subjseqgr.equals("ALL") )
                sbSQL.append(" AND     grseq   = '" + v_subjseqgr + "'                                                  \n");
       
            sbSQL.append(" GROUP BY date_time order by date_time                                                                    \n");
            
            System.out.println(this.getClass().getName() + "." + "SelectSubjTimeList() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls      = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
                list1.add(dbox);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
                
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return list1;
    }
}



