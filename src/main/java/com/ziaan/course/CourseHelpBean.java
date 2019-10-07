// **********************************************************
//  1. 제      목: 수강신청 도우미 Bean
//  2. 프로그램명: CourseHelpBean.java
//  3. 개      요: 수강신청 도우미 bean
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 2004.01.14
//  7. 수      정:
// **********************************************************
package com.ziaan.course;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;

public class CourseHelpBean { 
    public final static String GUBUN_CODE = "0049";
    public final static String WORK_CODE  = "W";
    public final static String LANG_CODE  = "L";

    private ConfigSet config;
    private int row; 
    
    public CourseHelpBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }      
    }

   /**
   * 대분류 SELECT (ALL 제외)
   * @param box          receive from the form object and session
   * @return ArrayList   분류 리스트가져오기
   */    
    public ArrayList ClassName(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = new ArrayList();
        String              sql     = "";
        String              sql1    = "";
        DataBox             dbox    = null;
        
        /*
        String v_faq_type   = box.getStringDefault("p_faq_type", "DIRECT");
        int v_pageno        = box.getInt("p_pageno");
        */
        
        String v_middleclass	=	box.getString("p_middleclass");
        String v_upperclass	=	box.getString("p_upperclass");

        try { 
            connMgr = new DBConnectionManager();
                	sql = "select upperclass, middleclass, classname						";
                	sql += " from tz_subjatt							";            
            
                if (v_upperclass.equals("")) { 

                	sql += " where middleclass='000' and lowerclass='000'				";
                	sql += " order by upperclass							";
                	
                } else { 
                  	if (v_middleclass.equals("")) {
                		sql += " where upperclass = '"+v_upperclass+"' and middleclass>'000' and lowerclass='000'		";
                		sql += " order by upperclass							";                  		
                		
                	} else {
                		sql += " where upperclass = '"+v_upperclass+"' and middleclass='"+v_middleclass+"'		";
                		sql += " order by upperclass							";                		
                		
                	}
                }
                System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(100);                         // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(1);                 // 현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();    // 전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();    // 전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum"  , new Integer(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                dbox.put("d_rowcount" , new Integer(100));
                list.add(dbox);
            }
        }
        catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
    }    
    

    /**
    수강 신청을 위한 과목검색
    @param box      receive from the form object and session
    @return ArrayList 검색 결과 리스트
    */
     public ArrayList selectSubjForApply(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls1             = null;
        ArrayList           list1           = null;
        ArrayList           list2           = null;
        DataBox             dbox            = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        //String              gyear           = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );
        String              gyear           = box.getString("p_gyear");
        String              v_user_id       = box.getSession("userid"           );

        // 개인의 회사 기준으로 과목 리스트
        String              v_comp          = box.getSession("comp"             );
        // 사이트의 GRCODE 로 과목 리스트
        String              v_grcode        = box.getSession("tem_grcode"       );
        String              v_lsearch       = box.getString ("p_lsearch"        );
        String              v_lsearchtext   = box.getString ("p_lsearchtext"    );
        String              v_basis_upperclass = box.getString("p_basis_upperclass");
        String              v_upperclass = box.getString("p_upperclass");
        String              v_middleclass = box.getString("p_middleclass");

        int                 v_propstart     = 0;
        int                 v_propend       = 0;
        boolean             ispossible      = false;

        int v_pageno        = box.getInt("p_pageno");
        try { 
            connMgr = new DBConnectionManager();
            list1   = new ArrayList();

            sbSQL.append(" select  distinct a.subj                                                      \n")
                 .append("     ,   a.scupperclass                                                       \n")
                 .append("     ,   a.scmiddleclass                                                      \n")
                 .append("     ,   a.isonoff                                                            \n")
                 .append("     ,   b.classname                                                          \n")
                 .append("     ,   a.subjnm                                                             \n")
                 .append("     ,   a.edustart                                                           \n")
                 .append("     ,   a.eduend                                                             \n")
                 .append("     ,   a.studentlimit                                                       \n") 
                 .append("     ,   a.scyear                                                             \n")   
                 .append("     ,   a.scsubjseq                                                          \n")    
                 .append("     ,   a.propose_date                                                       \n")
                 .append("     ,   decode(length(propstart), 0, propstart, substr(propstart, 0, 10)) propstart\n")
                 .append("     ,   decode(length(propend), 0, propend, substr(propend, 0, 10)) propend   \n")                                                     
                 .append("     ,   (                                                                    \n")
                 .append("             select  substr(nvl(indate,'00000000000000'),0,4 )                \n")
                 .append("             from    TZ_SUBJ                                                  \n")
                 .append("             where   subj = a.subj                                            \n")
                 .append("         )                               indate                               \n")
                 .append("     ,   (                                                                    \n")
                 .append("             select  nvl(avg(distcode1_avg), 0.0)                             \n")
                 .append("             from    tz_suleach                                               \n")                  
                 .append("             where   subj    = a.subj                                         \n")
                 .append("             and     grcode <> 'ALL'                                          \n")
                 .append("         )                               distcode1_avg                        \n")
                 .append("     ,   (                                                                    \n")
                 .append("             select  nvl(avg(distcode_avg), 0.0)                              \n")
                 .append("             from    tz_suleach                                               \n")                   
                 .append("             where   subj= a.subj                                             \n")
                 .append("             and     grcode = 'ALL'                                           \n")
                 .append("         )                               distcode_avg                         \n")
                 .append("     ,   (                                                                    \n")
                 .append("             select count(userid) CNTS                                        \n")
                 .append("             from tz_propose c                                                \n")
                 .append("             where c.subj = a.scsubj                                          \n")
                 .append("             and c.year = a.scyear                                            \n")
                 .append("             and c.subjseq = a.scsubjseq                                      \n")
                 .append("         ) proposecnt                                                         \n")
                 .append("     ,   NVL(c.haspreviewobj, 'N')      haspreviewobj                         \n")
                 .append(" from    (                                                                                                        \n")
                 .append("          SELECT TO_CHAR(TO_DATE(tp.ldate, 'YYYYMMDDHH24MISS'), 'MM\"월\" DD\"일\"') propose_date                 \n")
                 .append("              ,  tp.userid                                                                                        \n")
                 .append("              ,  vs.*                                                                                             \n")
                 .append("          FROM   VZ_SCSUBJSEQ vs                                                                                  \n")
                 .append("              ,  Tz_Propose   tp                                                                                  \n")
                 .append("          WHERE  vs.grcode        = 'N000001'                                                                     \n")
                 .append("          and    vs.isuse         = 'Y'                                                                           \n")
                 .append("          and    vs.subjvisible   = 'Y'                                                                           \n")
                 .append("          and    to_char(sysdate,'YYYYMMDDHH') between substr(propstart, 0, 10) and substr(eduend, 0, 10)         \n")
                 .append("          and    vs.scsubj        = tp.subj    (+)                                                                \n")
                 .append("          and    vs.scyear        = tp.year    (+)                                                                \n")
                 .append("          and    vs.scsubjseq     = tp.subjseq (+)                                                                \n")
                 .append("          and    tp.userid(+)     = " + SQLString.Format(v_user_id) + "                                           \n")
                 .append("          and    NVL(tp.cancelkind(+), ' ')   NOT IN ('F', 'P')                                                   \n")
                 .append("         )               a                                                                                        \n")
                 .append("     ,   (                                                                    \n")
                 .append("             select  upperclass                                               \n")
                 .append("                 ,   middleclass                                              \n")
                 .append("                 ,   classname                                                \n")
                 .append("             from    tz_subjatt                                               \n")
                 .append("             where   middleclass <> '000'                                     \n")
                 .append("             and     lowerclass  =  '000'                                     \n")
                 .append("         )               b                                                    \n")
                 .append("     ,   (                                                                    \n")
                 .append("             select  subj, DECODE(count(*), 0, 'N', 'Y')  haspreviewobj       \n")
                 .append("             from    tz_previewobj                                            \n")
                 .append("             group by subj                                                    \n")
                 .append("         )               c                                                    \n")
                 .append(" where   a.scupperclass  = b.upperclass                                       \n")
                 .append(" and     a.scmiddleclass = b.middleclass                                      \n")
                 .append(" and     a.grcode        = " + SQLString.Format(v_grcode ) + "                \n")
                 .append(" and     a.isuse         = 'Y'                                                \n")
                 .append(" and     a.subjvisible   = 'Y'                                                \n")
                 .append(" and     to_char(sysdate,'YYYYMMDDHH') between substr(propstart, 0, 10) and substr(a.eduend, 0, 10)\n")
                 .append(" and     a.subj          = c.subj(+)                                          \n");
                 //.append(" and     a.gyear         = " + SQLString.Format(gyear    ) + "                \n");
              
                 
            /*
            if( "A01".equals(v_upperclass) ) {
                sbSQL.append(" and     a.scupperclass   = 'A01'                                         \n");
            } else if( "A02".equals(v_upperclass) ) {
                sbSQL.append(" and     a.scupperclass   = 'A02'                                         \n");
            } else if( "A03".equals(v_upperclass) ) {
                sbSQL.append(" and     a.scupperclass   = 'A03'                                         \n");
            }    
            */
            
            if (!v_upperclass.equals("")) {
            	sbSQL.append(" and     a.scupperclass   = '"+v_upperclass+"'                            \n");
            }
            
            if (!v_middleclass.equals("")) {
            	sbSQL.append(" and     a.scmiddleclass   = '"+v_middleclass+"'                            \n");
            }            
            
            /*
            if( "A01".equals(v_upperclass) ) {
                sbSQL.append(" and     a.scupperclass   = 'A01'                                         \n");
            } else if( "A02".equals(v_upperclass) ) {
                sbSQL.append(" and     a.scupperclass   = 'A02'                                         \n");
            } else if( "A03".equals(v_upperclass) ) {
                sbSQL.append(" and     a.scupperclass   = 'A03'                                         \n");
            } 
            */               
            
            if ( !"".equals(v_lsearchtext))  {       // 과목명
                sbSQL.append(" and upper(a.scsubjnm ) like upper(" + SQLString.Format("%" + v_lsearchtext + "%") + ")   \n");
            }

            sbSQL.append(" order by propstart desc, isonoff desc, scupperclass, scmiddleclass, subjnm               \n");
            
            System.out.println(sbSQL.toString());
            //System.out.println(this.getClass().getName() + "." + "selectSubjSearch() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls1 = connMgr.executeQuery(sbSQL.toString());
            ls1.setPageSize(15);             //  페이지당 row 갯수를 세팅한다
            ls1.setCurrentPage(v_pageno);                    //     현재페이지번호를 세팅한다.
            int total_page_count = ls1.getTotalPage();       //     전체 페이지 수를 반환한다
            int total_row_count = ls1.getTotalCount();    //     전체 row 수를 반환한다
            //MainSubjSearchBean bean = new MainSubjSearchBean();
            while ( ls1.next() ) { 
                dbox    = ls1.getDataBox();
                dbox.put("d_dispnum", new Integer(total_row_count - ls1.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(total_page_count));
                box.put("d_rowcount", new Integer(15));
                dbox.put("distcode1_avg", new Double( ls1.getDouble("distcode1_avg" )));
                dbox.put("distcode_avg" , new Double( ls1.getDouble("distcode_avg"  )));
                //dbox.put("d_ispropose", bean.getPropseStatus(box,ls1.getString("subj"), ls1.getString("scsubjseq"), ls1.getString("scyear"), v_user_id, "3"));
                list1.add(dbox);
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls1 != null ) { 
                try { 
                    ls1.close();  
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