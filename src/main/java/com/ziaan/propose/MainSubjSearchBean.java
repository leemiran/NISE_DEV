// **********************************************************
//  1. 제      목: SUBJECT INFORMATION USER BEAN
//  2. 프로그램명:  MainSubjSearchBean.java
//  3. 개      요: 과목안내 사용자 bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2004.02
//  7. 수      정:
// **********************************************************
package com.ziaan.propose; 
import javax.servlet.http.*;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.sql.*;

import com.ziaan.propose.*;

import com.ziaan.study.MyClassBean;
import com.ziaan.common.*;
import com.ziaan.library.*;
import com.ziaan.system.*;

public class MainSubjSearchBean { 

	private org.apache.log4j.Logger logger = Logger.getLogger(this.getClass());

	
	private ConfigSet config;
    private int row; 
    
    public MainSubjSearchBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }   
        
    }

    /**
    과목검색
    @param box      receive from the form object and session
    @return ArrayList 검색 결과 리스트
    */
     public ArrayList selectSubjSearch(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr         = null;
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
        String              v_basis_upperclass = box.getString("basis_upperclass");

        int                 v_propstart     = 0;
        int                 v_propend       = 0;
        boolean             ispossible      = false;

        int v_pageno        = box.getInt("p_pageno");
        try { 
            connMgr = new DBConnectionManager();
            list1   = new ArrayList();
            
           /* sbSQL.append(" select  distinct a.subj                                                      \n")
                 .append("     ,   a.scupperclass                                                       \n")
                 .append("     ,   a.scmiddleclass                                                      \n")
                 .append("     ,   a.isonoff                                                            \n")
                 .append("     ,   b.classname                                                          \n")
                 .append("     ,   a.subjnm                                                             \n")
                 .append("     ,   a.edustart                                                           \n")
                 .append("     ,   a.eduend                                                             \n")
                 .append("     ,   a.studentlimit                                                       \n") 
                 .append("     ,   a.scsubjseq                                                          \n")
                 .append("     ,   a.scyear                                                             \n")
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
                 .append(" from    VZ_SCSUBJSEQ    a                                                    \n")
                 .append("     ,   (                                                                    \n")
                 .append("             select  upperclass                                               \n")
                 .append("                 ,   middleclass                                              \n")
                 .append("                 ,   classname                                                \n")
                 .append("             from    tz_subjatt                                               \n")
                 .append("             where   middleclass <> '000'                                     \n")
                 .append("             and     lowerclass  =  '000'                                     \n")
                 .append("         )               b                                                    \n")
                 .append(" where   a.scupperclass  = b.upperclass                                       \n")
                 .append(" and     a.scmiddleclass = b.middleclass                                      \n")
                 .append(" and     a.grcode        = " + SQLString.Format(v_grcode ) + "                \n")
                 .append(" and     a.isuse         = 'Y'                                                \n")
                 .append(" and     a.subjvisible   = 'Y'                                                \n");
                 
            
            if(!"".equals(gyear)) {
                sbSQL.append(" and     a.gyear         = " + SQLString.Format(gyear    ) + "            \n");
            }*/
            
            sbSQL.append("SELECT c.classname, a.subjnm, a.subj, a.isonoff , introducefilenamenew \n")
                 .append("FROM tz_subj a, tz_subjatt b, tz_subjatt c        \n")
                 .append("WHERE a.upperclass = b.upperclass             \n")
                 .append("AND a.middleclass = c.middleclass             \n")
                 .append("AND a.subjclass = c.subjclass                 \n")
                 .append("AND a.subjclass = b.subjclass                 \n")
                 .append(" and     a.isuse         = 'Y'                                                \n");
//                 .append(" and     a.subjvisible   = 'Y'                                                \n");
            
            if( "it".equals(v_basis_upperclass) ) {
                sbSQL.append(" and     a.upperclass   = 'A03'                                         \n");
            } else if( "business".equals(v_basis_upperclass) ) {
                sbSQL.append(" and     a.upperclass   = 'A01'                                         \n");
            } else if( "women".equals(v_basis_upperclass) ) {
                sbSQL.append(" and     a.upperclass   = 'A02'                                         \n");
            }

            if ( !"".equals(v_lsearchtext))  {       // 과목명
                sbSQL.append(" and upper(a.subjnm ) like upper(" + SQLString.Format("%" + v_lsearchtext + "%") + ")   \n");
            }

            sbSQL.append(" order by c.classname, a.subjnm                \n");
            
            //System.out.println(this.getClass().getName() + "." + "selectSubjSearch() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls1 = connMgr.executeQuery(sbSQL.toString());
            /*ls1.setPageSize(row);             //  페이지당 row 갯수를 세팅한다
            ls1.setCurrentPage(v_pageno);                    //     현재페이지번호를 세팅한다.
            int total_page_count = ls1.getTotalPage();       //     전체 페이지 수를 반환한다
            int total_row_count = ls1.getTotalCount();    //     전체 row 수를 반환한다*/
            
            while ( ls1.next() ) { 
                dbox    = ls1.getDataBox();
              //  dbox.put("d_dispnum", new Integer(total_row_count - ls1.getRowNum() + 1));
               // dbox.put("d_totalpage", new Integer(total_page_count));
                //dbox.put("d_rowcount", new Integer(row));
                /*dbox.put("distcode1_avg", new Double( ls1.getDouble("distcode1_avg" )));
                dbox.put("distcode_avg" , new Double( ls1.getDouble("distcode_avg"  )));
                dbox.put("d_ispropose", this.getPropseStatus(box,ls1.getString("subj"), ls1.getString("scsubjseq"), ls1.getString("scyear"), v_user_id, "3"));*/
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


/**
    추천과목 리스트
    @param box      receive from the form object and session
    @return ArrayList 추천 결과 리스트
    */
     public ArrayList selectSubjRecomList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        ArrayList list2     = null;
        DataBox dbox= null;
        String sql         = "";

        String  v_user_id   = box.getSession("userid");
        String  v_comp      = box.getSession("comp");

        v_comp              = v_comp.substring(0,4);
        String  v_lsearchsubject = box.getString("p_lsearchsubject");
        String  gyear            = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );

        int     l           = 0;

        int     v_propstart = 0;
        int     v_propend   = 0;

        boolean ispossible = false;

        try { 
            list1 = new ArrayList();

            sql  = " select " ;
            sql += "   distinct a.subj subj , ";
            sql += "   a.scupperclass upperclass, ";
            sql += "   a.isonoff isonoff, ";
            sql += "   b.classname classname, ";
            sql += "   a.subjnm subjnm, ";
            sql += " ( select ";
            sql += "     count(subjseq)  ";
            sql += "   from  ";
            sql += "     tz_subjseq x  ";
            sql += "   where  ";
            //sql += "     x.grcode in (select grcode from TZ_GRCOMP where comp like '" +v_comp + "%') ";
            sql += "     x.grcode in (select grcode from TZ_GRCOMP where comp like " +StringManager.makeSQL(v_comp + "%") +  " ) ";
            sql += "     and x.subj = a.subj ";
            sql += "     and x.gyear = a.gyear ";
            // sql += "     and x.subj = a.subj ";
            sql += "     ) totcnt ";
            sql += "     from ";
            sql += "      VZ_SCSUBJSEQ a, ";
            sql += "      (select upperclass, classname from TZ_SUBJATT where middleclass = '000' and lowerclass ='000' ) b";
            sql += "     where";
            sql += "        a.scupperclass = b.upperclass ";
            sql += "        and a.grcode in (select grcode from TZ_GRCOMP where comp " +StringManager.makeSQL(v_comp + "%") +  " ) ";
            sql += "        and a.isuse = 'Y' ";
            sql += "        and a.subjvisible = 'Y' ";
            sql += "        and substr(a.specials,3,1) = 'Y' ";
            //sql += "        and gyear = '" +gyear + "'";
            sql += "        and gyear = " +StringManager.makeSQL(gyear) + " ";
            sql += "     order by isonoff desc, upperclass, subj";
            connMgr = new DBConnectionManager();
            ls1 = connMgr.executeQuery(sql);
            while ( ls1.next() ) { 
                dbox = ls1.getDataBox();
                dbox.put("d11_possiblecnt", "");
                // dbox.put("d_possiblecnt", possibleSeqCount(box,ls1.getString("subj"),gyear,v_user_id,v_comp));
                ispossible = possibleSeqCheck(box,ls1.getString("subj"),gyear,v_user_id,v_comp);
                if ( ispossible) { 
                      dbox.put("d_possiblecnt", "1");
                }else { 
                      dbox.put("d_possiblecnt", "0");
                }

                list1.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }


    /**
    초기화면 추천과목 리스트 상위 3개
    @param box      receive from the form object and session
    @return ArrayList 추천 결과 리스트
    */
     public ArrayList selectSubjTop3RecomList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList list      = null;
        DataBox dbox        = null;
        String sql          = "";

        String v_matchcode      = box.getString("matchcode");
				String v_grcode					= box.getSession("tem_grcode");
        try { 
            list = new ArrayList();
            	sql =  " select             ";
            	sql += "  a.subj,           ";
            	sql += "  a.subjnm,         ";
            	sql += "  a.isonoff,        ";
            	sql += "  a.introducefilenamenew    ";
            	sql += " from               ";
            	sql += "  tz_subj a,        ";
            	sql += "  tz_classfymatch b, ";
            	sql += "  tz_grrecom c ";
            	sql += " where              ";
            	sql += "  a.upperclass = b.upperclass   ";
            	sql += "  and a.subj = c.subjcourse ";
            	sql += "  and b.matchcode = " +StringManager.makeSQL(v_matchcode);
							sql += "  and c.grcode = " +StringManager.makeSQL(v_grcode);
            	sql += "  and rownum <= 3  ";
            	sql += "order by a.ldate ";            

            connMgr = new DBConnectionManager();

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex,  box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    초기화면 추천과목 리스트 상위 4개
    @param box      receive from the form object and session
    @return ArrayList 추천 결과 리스트
    */
     public ArrayList selectSubjTop5RecomList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list     = null;
        DataBox dbox= null;
        // String  gyear            = box.getStringDefault("gyear",FormatDate.getDate("yyyy") );

        String sql         = "";

        try { 
            list = new ArrayList();

            // sql  = " select subj,subjnm,ldate from tz_subj where substr(specials,3,1)='Y' and rownum <= 5 order by ldate ";
            sql =  " select ";
            sql += "  a.subj, ";
            sql += "  a.subjnm, ";
            sql += "  a.ldate, ";
            sql += "  b.upperclass , ";
            sql += "  (select classname from TZ_SUBJATT where upperclass=a.upperclass and middleclass = a.middleclass and lowerclass ='000' ) middleclass, ";
            sql += "  a.isonoff ";
            sql += " from  ";
            sql += "  tz_subj a,  ";
            sql += "  (select upperclass, classname from TZ_SUBJATT where middleclass = '000' and lowerclass ='000' ) b  ";
            sql += "where  ";
            sql += "  a.upperclass = b.upperclass ";
            sql += "  and a.isuse = 'Y'  ";
            sql += "  and a.isvisible = 'Y' ";
            sql += "  and substr(a.specials,3,1)='Y'  ";
            sql += "  and rownum <= 4  ";
            sql += "order by ldate ";

            connMgr = new DBConnectionManager();

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                // dbox.put("d_subj",      ls.getString("subj") );
                // dbox.put("d_subjnm",    ls.getString("subjnm") );
                // dbox.put("d_ldate",     ls.getString("ldate") );
                list.add(dbox);

            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex,  box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    수강신청 가능기수 count
    @param box      receive from the form object and session
    @return int 신청가능기수 Return
    */
     public String possibleSeqCount(RequestBox box, String subj,String gyear,String userid,String comp) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;

        String sql         = "";
        String v_user_id   = userid;
        String v_comp      = comp;
        String v_gyear     = gyear;

        String cnt = "";

        v_comp             = v_comp.substring(0,4);

        try { 
            connMgr = new DBConnectionManager();

            // sql1 =  " select count(*) cnt from TZ_POLLSEL  ";

            sql += " select  \n";
            sql += "   count(subjseq)  cnt\n";
            sql += " from  \n";
            sql += "   VZ_SCSUBJSEQ a \n";
            sql += " where \n";
            sql += "   grcode in (select grcode from TZ_GRCOMP where comp like '" +v_comp + "%') \n";
            sql += "   and a.gyear = " +StringManager.makeSQL(v_gyear) + "\n";
            sql += "   and a.scsubj = " +StringManager.makeSQL(subj) + "\n";
            sql += "   and decode(a.studentlimit,0 , 1000000,  a.studentlimit) > (select count(userid) from tz_propose where subj = a.subj and subjseq = a.subjseq and year = a.year and (isproposeapproval != 'N'and chkfinal != 'N')) \n";
            sql += "   and to_char(sysdate,'YYYYMMDDHH24') between a.propstart and a.propend \n";
            //// 이미신청한과목은 다시 수강신청할수 없다.
            sql += "   and (subj||subjseq||year) not in ( \n";
            sql += "   select\n";
            sql += "     x.subj||x.subjseq||x.year\n";
            sql += "   from\n";
            sql += "     tz_propose x \n";
            sql += "   where\n";
            sql += "     (x.userid = " +StringManager.makeSQL(userid) + " and x.subj = " +StringManager.makeSQL(subj) + " and x.chkfinal = 'Y')\n";
            sql += "     or  (select isgraduated from tz_stold where userid = '2657659' and subj = '1028') = 'Y'\n";
            sql += ") \n";

            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();
            if ( rs1.next() ) { 
                cnt = rs1.getString("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return cnt;
    }


    /**

    수강신청 가능 여부

    2005 . 02. 26일
    작성자 : 이창훈
    email : leech@ziaan.com
    수강신청 가능 기수 구하는 Method입니다.

    @param box      receive from the form object and session
    @return boolean 신청가능기수 Return
    */
     public boolean possibleSeqCheck(RequestBox box, String subj,String gyear,String userid,String comp) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        // ResultSet rs1 = null;
        ListSet ls1 = null;

        String sql         = "";
        String v_user_id   = userid;
        String v_comp      = comp;
        String v_gyear     = gyear;

        String v_subj = "";
        String v_subjseq = "";
        String v_year= "";
        String v_edustart= "";
        String v_eduend= "";
        String v_isonoff= "";
        String v_isedutarget = "";
        String v_mastercd    = "";
        String v_proposetype = "";

        int cnt = 0;
        boolean chkseq          = false;
        boolean ismastersubj    = false;
        boolean isedutarget     = false;
        boolean ismasterpropose = false;

        v_comp             = v_comp.substring(0,4);


        SubjComBean csBean = new SubjComBean();

        try { 
            connMgr = new DBConnectionManager();

            // sql1 =  " select count(*) cnt from TZ_POLLSEL  ";

            sql += " select  \n";
            sql += "   a.edustart,a.eduend, a.year, a.subjseq, a.subjnm, a.isonoff , b.mastercd, b.isedutarget, b.proposetype \n";
            sql += " from  \n";
            sql += "   VZ_SCSUBJSEQ a, VZ_MASTERSUBJSEQ b \n";
            sql += " where \n";
            sql += "   a.grcode in (select grcode from TZ_GRCOMP where comp like " +StringManager.makeSQL(v_comp + "%") + ") \n";
            sql += "   and a.gyear = " +StringManager.makeSQL(v_gyear) + " \n";
            sql += "   and a.scsubj = " +StringManager.makeSQL(subj) + " \n";
            sql += "   and a.seqvisible = 'Y' \n";
            sql += "   and a.scsubj = b.subj( +)      \n";
            sql += "   and a.scsubjseq = b.subjseq( +) \n";
            sql += "   and a.year = b.year( +) \n";       ///총정원제약 조건
            ///수강신청기간 제약조건
            sql += "   and decode(a.studentlimit,0 , 1000000,  a.studentlimit) > (select count(userid) from tz_propose where subj = a.subj and subjseq = a.subjseq and year = a.year and (isproposeapproval != 'N'and chkfinal != 'N')) \n";
            sql += "   and to_char(sysdate,'YYYYMMDDHH24') between a.propstart and a.propend \n";


            //// //// //// //// //// //// //// //// //// //// //// // 
            //// 1
            //// 이미신청한과목은 다시 수강신청할수 없다.
            //// //// //// //// //// //// //// //// //// //// //// // 
            sql += "   and (a.subj) not in ( \n";
            sql += "   select\n";
            sql += "     x.subj\n";
            sql += "   from\n";
            sql += "     tz_propose x \n";
            sql += "   where\n";
            sql += "     (x.userid = " +StringManager.makeSQL(v_user_id) + " and x.subj = " +StringManager.makeSQL(subj) + " and (x.chkfinal != 'N' and x.isproposeapproval != 'N') )\n";
            // sql += "     or  (select isgraduated from tz_stold where userid = " +StringManager.makeSQL(userid) + " and subj = " +StringManager.makeSQL(subj) + ") = 'Y'\n";
            sql += ") \n";

            //// //// //// //// //// //// //// //// //// //// //// // 
            //// 2
            //// 수료한과목은 다시 수강신청할수 없다.
            //// //// //// //// //// //// //// //// //// //// //// // 
            sql += "   and (a.subj) not in ( \n";
            sql += "   select\n";
            sql += "     x.subj\n";
            sql += "   from\n";
            sql += "     tz_stold x \n";
            sql += "   where\n";
            sql += "     (x.userid = " +StringManager.makeSQL(v_user_id) + " and x.subj = " +StringManager.makeSQL(subj) + " and x.isgraduated = 'Y')\n";
            sql += ") \n";

            //System.out.println("신청제약조건 기본 공통 1=" +sql);

            ls1 = connMgr.executeQuery(sql);

            while ( ls1.next() ) { 

                chkseq = true;

                v_subj         = subj;
                v_subjseq      = ls1.getString("subjseq");
                v_year         = ls1.getString("year");
                v_edustart     = ls1.getString("edustart");
                v_eduend       = ls1.getString("eduend");
                v_isonoff      = ls1.getString("isonoff");
                v_isedutarget  = ls1.getString("isedutarget");   // 대상자만 수강신청 할수 있는지
                v_mastercd     = ls1.getString("mastercd");     // 마스터과목 코드로 매핑이 되어 있는지.
                v_proposetype  = ls1.getString("proposetype");

                //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 
                // 2. 해당과목이 off line과목일때 학습예정과목과 중복기간 체크,
                //   단 과목신청방식과 과목선택방식에서는 체크하지 않는다.
                //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 

                // if ( v_isonoff.equals("OFF") &&!(v_proposetype.equals("3")||v_proposetype.equals("2"))) { 
                if ( v_isonoff.equals("OFF") ) { 
                  chkseq = overLapEduterm(v_edustart,v_eduend,v_user_id);
                }

                //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
                // 3. 해당과목이 마스터과목일때 신청여부/대상자여부 체크
                //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
                if ( chkseq && !v_mastercd.equals("") ) { 
                  ismasterpropose = csBean.IsMasterPropose(v_mastercd, v_user_id);

                  if ( ismasterpropose) {   // 신청을 하지 않았다

                      isedutarget = csBean.IsEduTarget(v_subj, v_year, v_subjseq, v_user_id); // 대상자로 선정이 되어있는 지여부
                      //System.out.println("대상자인가???????" +v_subj + "::" +isedutarget);

                      if ( v_isedutarget.equals("Y") ) {  // 해당마스터과목이 대상자 지정 마스터이면

                          if ( isedutarget) {             // 본인이 마스터과목에 매핑된 대상자라면
                            chkseq = true;
                          }
                          else{ 
                            chkseq = false;            // 대상자가 아니라면
                          }
                      } else {                            // 대상자 지정과목이 아니면 신청가능하다.
                          chkseq = true;
                      }

                  }
                  else{                 // 신청을 했다
                      chkseq = false;
                  }

                }

                // 신청가능한 기수이면
                if ( chkseq) { 
                  cnt++;
                }

            }

            if ( cnt > 0) { 
                chkseq = true;
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return chkseq;
    }

    /**

    비용제약 여부
    2005 . 02. 26일
    작성자 : 이창훈
    email : leech@ziaan.com
    수강신청 가능 기수 구하는 Method입니다.

    @param box      receive from the form object and session
    @return boolean 신청가능기수 Return
    */
     public boolean LimitExe(String upperclass, String gyear, String comp, String userid, String v_propstart, String v_propend, double v_budget, int v_maxsubjcnt) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        // ListSet ls1 = null;

        String sql         = "";
        String v_user_id   = userid;
        String v_comp      = comp;
        String v_gyear     = gyear;
        String v_upperclass= upperclass;

        String v_subj = "";
        String v_subjseq = "";
        String v_year= "";
        String v_edustart= "";
        String v_eduend= "";
        String v_isonoff= "";
        String v_isedutarget = "";
        String v_mastercd    = "";

        double totbiyong = 0;
        int v_propcnt = 0;

        boolean chkupperclass = true;


        v_comp             = v_comp.substring(0,4);




        try { 
            connMgr = new DBConnectionManager();

            sql += " select  \n";
            sql += "   sum(c.biyong) totbiyong \n";
            sql += " from \n";
            sql += "   tz_propose a, \n";
            sql += "   tz_member b, \n";
            sql += "   vz_scsubjseq c\n";
            sql += " where\n";
            sql += "   a.userid = b.userid\n";
            sql += "   and substr(b.comp,1,4)  = " +StringManager.makeSQL(v_comp) + "\n";
            sql += "   and a.subj = c.subj\n";
            sql += "   and a.year = c.year\n";
            sql += "   and a.subjseq = c.subjseq\n";
            sql += "   and c.scupperclass = " +StringManager.makeSQL(v_upperclass) + "\n";
            sql += "   and  \n";
            sql += "( \n";
            sql += "  (to_char(sysdate, 'yyyymmddhh') between " +StringManager.makeSQL(v_propstart) + " and " +StringManager.makeSQL(v_propend) + ") \n";
            sql += "   and \n";
            sql += "  (appdate between " +StringManager.makeSQL(v_propstart) + " and " +StringManager.makeSQL(v_propend) + ") \n";
            sql += ") \n";

            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();
            if ( rs1.next() ) { 
                totbiyong = rs1.getDouble("totbiyong");
            }

            if ( totbiyong >= v_budget) { 
              chkupperclass = false;
            }


            sql = " select \n";
            sql += "   count(*) propcnt  \n";
            sql += " from \n";
            sql += "   tz_propose a, vz_scsubjseq b\n";
            sql += " where \n";
            sql += "   a.subj = b.subj\n";
            sql += "   and a.subjseq = b.subjseq\n";
            sql += "   and a.year = b.year\n";
            sql += "   and b.scupperclass = " +StringManager.makeSQL(v_upperclass) + "\n";
            sql += "   and a.userid = " +StringManager.makeSQL(v_user_id) + "\n";
            sql += "   and a.chkfinal != 'N'\n";
            sql += "   and to_char(sysdate, 'yyyymmddhh') <= b.eduend\n";

            pstmt2 = connMgr.prepareStatement(sql);

            rs2 = pstmt2.executeQuery();
            if ( rs2.next() ) { 
                v_propcnt = rs2.getInt("propcnt");
            }

            if ( v_propcnt >= v_maxsubjcnt ) { 
                chkupperclass = false;
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( rs2 != null ) { try { rs2.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return chkupperclass;
    }

    /**
    offline 교육기간 중복 체크
    @param box      receive from the form object and session
    @return boolean 신청가능기수 Return
    @ 신청가능하면 true (중복안됨)
    @ 신청불가능하면 false(중복됨)
    */
     public boolean overLapEduterm(String edustart, String eduend, String userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        // ResultSet rs1 = null;
        ResultSet rs1 = null;

        String sql         = "";

        int cnt = 0;
        boolean chkseq = true;



        try { 
            connMgr = new DBConnectionManager();

            sql +=" select \n";
            sql +="   count(subj) cnt \n";
            sql +=" from \n";
            sql +="   tz_student a\n";
            sql +=" where \n";
            sql +="   userid = " +StringManager.makeSQL(userid) + "\n";
            sql +="   and (select isonoff from tz_subj where subj = a.subj and subjseq = a.subjseq and year = a.year ) = 'OFF'\n";
            sql +="   and to_char(sysdate, 'yyyymmddhh') <= eduend \n";
            sql +="   and chkfinal = 'Y'\n";
            sql +="   and (\n";
            sql +="       (edustart <= " +StringManager.makeSQL(eduend) + " and eduend >= " +StringManager.makeSQL(eduend) + ") or \n";
            sql +="    (edustart <= " +StringManager.makeSQL(edustart) + " and eduend >= " +StringManager.makeSQL(edustart) + ") or\n";
            sql +="    (edustart >= " +StringManager.makeSQL(edustart) + "' and eduend <= " +StringManager.makeSQL(eduend) + ")\n";
            sql +="    )\n";

            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();
            if ( rs1.next() ) { 
                cnt = rs1.getInt("cnt");
                if ( cnt > 0) { 
                    chkseq = false;
                }
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return chkseq;
    }


    /**
    offline 교육기간 중복 체크
    @param box      receive from the form object and session
    @return boolean 신청가능기수 Return
    @ 신청가능하면 true (중복안됨)
    @ 신청불가능하면 false(중복됨)
    */
     public String ziphapOverLapEduterm(String edustart, String eduend, String userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        // ResultSet rs1 = null;
        ResultSet rs1 = null;

        String sql         = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "0";



        try { 
            connMgr = new DBConnectionManager();

            sql +=" select count(subj) cnt ";
            sql +=" from   vz_student a";
            sql +=" where  userid = " +StringManager.makeSQL(userid);
            sql +=" and    (select isonoff from tz_subj where subj = a.subj and subjseq = a.subjseq and year = a.year ) = 'OFF' ";
            sql +=" and    to_char(sysdate, 'yyyymmddhh') <= eduend ";
            sql +=" and    chkfinal = 'Y' ";
            sql +=" and    ( ";
            sql +="         (edustart <= " +StringManager.makeSQL(eduend) + " and eduend >= " +StringManager.makeSQL(eduend) + ") or \n";
            sql +="         (edustart <= " +StringManager.makeSQL(edustart) + " and eduend >= " +StringManager.makeSQL(edustart) + ") or\n";
            sql +="         (edustart >= " +StringManager.makeSQL(edustart) + " and eduend <= " +StringManager.makeSQL(eduend) + ")\n";
            sql +="        ) ";

            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();
            if ( rs1.next() ) { 
                cnt = rs1.getInt("cnt");
                if ( cnt > 0) { 
                    errvalue = "6";
                } else { 
                        errvalue = "0";
                }
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }


/**
    수강신청 기존 count
    @param box      receive from the form object and session
    @return int 신청가능기수 Return
    */
     public String possibleSeqCount_OLD(RequestBox box, String subj,String gyear,String userid,String comp) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;

        String sql         = "";
        String v_user_id   = userid;
        String v_comp      = comp;
        String v_gyear     = gyear;

        String cnt = "";

        v_comp             = v_comp.substring(0,4);

        try { 
            connMgr = new DBConnectionManager();

            // sql1 =  " select count(*) cnt from TZ_POLLSEL  ";

            sql += " select  ";
            sql += "   count(subjseq)  cnt";
            sql += " from  ";
            sql += "   VZ_SCSUBJSEQ a ";
            sql += " where ";
            sql += "   grcode in (select grcode from TZ_GRCOMP where comp like " +StringManager.makeSQL(v_comp + "%") + ") ";
            sql += "   and a.gyear = " +StringManager.makeSQL(v_gyear);
            sql += "   and a.scsubj = " +StringManager.makeSQL(subj);
            // sql += "   and a.studentlimit > (select count(userid) from tz_propose where a.subj = subj and a.subjseq = subjseq and a.year = year and (isproposeapproval != 'N'or chkfinal != 'N')) ";
            sql += "   and decode(a.studentlimit,0 , 1000000,  a.studentlimit) > (select count(userid) from tz_propose where subj = a.subj and subjseq = a.subjseq and year = a.year and (isproposeapproval != 'N'and chkfinal != 'N')) ";
            sql += "   and to_char(sysdate,'YYYYMMDDHH24') between a.propstart and a.propend ";

            //// 이미신청한과목은 다시 수강신청할수 없다.
            sql += "   and (subj,subjseq,year) not in ( ";
            sql += "     select  ";
            sql += "       subj, subjseq, year  ";
            sql += "     from  ";
            sql += "       tz_propose  ";
            sql += "     where  ";
            sql += "       userid = " +StringManager.makeSQL(userid);
            sql += "       and subj = a.scsubj  ";
            sql += "       and subjseq = a.scsubjseq  ";
            sql += "       and year = a.year  ) ";

            //// 대상자인경우 신청된 과목기수 (마스터과목에 매핑된 모든 과목은 한과목만 신청하면 모든 과목은 다시신청할수 없다.)
            sql += "   and (subj,subjseq,year)not in (        ";
            sql += "     select          ";
            sql += "       b.subj,          ";
            sql += "       b.subjseq,        ";
            sql += "       b.year        ";
            sql += "     from           ";
            sql += "       tz_edutarget a,           ";
            sql += "       vz_mastersubjseq b        ";
            sql += "     where           ";
            sql += "       a.userid = " +StringManager.makeSQL(userid);
            sql += "       and b.isedutarget = 'Y' ";
            sql += "       and a.mastercd = b.mastercd ";
            sql += "       and b.grcode in (select grcode from TZ_GRCOMP where comp like " +StringManager.makeSQL(v_comp + "%") + ")";
            sql += "       and (b.mastercd) in(       ";
            sql += "         select               ";
            sql += "           y.mastercd           ";
            sql += "         from               ";
            sql += "           tz_propose x, vz_mastersubjseq y            ";
            sql += "         where              ";
            sql += "           x.userid=" +StringManager.makeSQL(userid);
            sql += "           and y.grcode in (select grcode from TZ_GRCOMP where comp like " +StringManager.makeSQL(v_comp + "%") + ")";
            sql += "           and x.subj = y.subj            ";
            sql += "           and x.subjseq = y.subjseq   ";
            sql += "           and x.year = y.year ";
            sql += "           and (x.isproposeapproval != 'N'  and y.propend >= " +StringManager.makeSQL(FormatDate.getDate("yyyyMMddHH")) + "))      ";
            sql += "     ) ";

            //// 마스터과목 단위 신청일 경우 그중 한개의 과목만 신청하면 모든과목은 수강신청할수 없다.
            sql += "   and (subj,subjseq,year)not in ( ";
            sql += "     select             ";
            sql += "     subj,           ";
            sql += "     subjseq,         ";
            sql += "     year          ";
            sql += "   from            ";
            sql += "     vz_mastersubjseq          ";
            sql += "   where ";
            sql += "     grcode in (select grcode from TZ_GRCOMP where comp like " +StringManager.makeSQL(v_comp + "%") +") ";
            sql += "     and gyear = a.gyear  ";
            sql += "     and (mastercd) in ";
            sql += "     (select              ";
            sql += "        y.mastercd         ";
            sql += "      from              ";
            sql += "        tz_propose x, vz_mastersubjseq y           ";
            sql += "      where              ";
            sql += "        x.userid=" +StringManager.makeSQL(userid);
            sql += "        and y.grcode in (select grcode from TZ_GRCOMP where comp like " +StringManager.makeSQL(v_comp + "%")+")";
            sql += "        and x.subj = y.subj              ";
            sql += "        and x.subjseq = y.subjseq          ";
            sql += "        and x.year = y.year             ";
            sql += "        and (x.isproposeapproval != 'N'  and y.propend >= " +StringManager.makeSQL(FormatDate.getDate("yyyyMMddHH")) + ") ";
            sql += ")            ) ";

            // 대상자가 있는 마스터과목일경우 마스터과목에 대상자로 매핑이 되어있지않는경우 수강신청할수 없다.
            sql += "   and (subj,subjseq,year) not in(  ";
            sql += "   select  ";
            sql += "     subj,subjseq,year ";
            sql += "   from  ";
            sql += "     vz_mastersubjseq   ";
            sql += "   where ";
            sql += "     subj = a.subj ";
            sql += "     and year = a.year ";
            sql += "     and isedutarget = 'Y' ";
            sql += "     and mastercd not in(select mastercd from tz_edutarget where userid = " +StringManager.makeSQL(userid) + ") ";
            sql += "   ) ";

            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();
            if ( rs1.next() ) { 
                cnt = rs1.getString("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return cnt;
    }


/**
     수강신청 가능기수 리턴
     @param box      receive from the form object and session
     @return int 신청가능기수 상태 Return
     Return : boolean
     true : 가능
     false : 불가능

*/
     public boolean possibleSeqStatus(RequestBox box, String subj,String subjseq, String gyear,String userid, String comp) throws Exception { 
       DBConnectionManager connMgr     = null;
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        // ResultSet rs1 = null;
        ListSet ls1 = null;

        String sql         = "";
        String v_user_id   = userid;
        String v_comp      = comp;
        String v_gyear     = gyear;

        String v_subj = "";
        String v_subjseq = "";
        String v_year= "";
        String v_edustart= "";
        String v_eduend= "";
        String v_isonoff= "";
        String v_isedutarget = "";
        String v_mastercd    = "";
        String v_proposetype = "";

        int cnt = 0;
        boolean chkseq          = false;
        boolean ismastersubj    = false;
        boolean isedutarget     = false;
        boolean ismasterpropose = false;

        v_comp             = v_comp.substring(0,4);


        SubjComBean csBean = new SubjComBean();

        try { 
            connMgr = new DBConnectionManager();

            // sql1 =  " select count(*) cnt from TZ_POLLSEL  ";

            sql += " select  \n";
            sql += "   a.edustart,a.eduend, a.year, a.subjseq, a.subjnm, a.isonoff , b.mastercd, b.isedutarget, b.proposetype \n";
            sql += " from  \n";
            sql += "   VZ_SCSUBJSEQ a, VZ_MASTERSUBJSEQ b \n";
            sql += " where \n";
            sql += "   a.grcode in (select grcode from TZ_GRCOMP where comp like '" +v_comp + "%') \n";
            sql += "   and a.gyear = " +StringManager.makeSQL(v_gyear) + " \n";
            sql += "   and a.scsubj = " +StringManager.makeSQL(subj) + " \n";
            sql += "   and a.scsubjseq = " +StringManager.makeSQL(subjseq) + " \n";
            sql += "   and a.seqvisible = 'Y' \n";
            sql += "   and a.scsubj = b.subj( +)      \n";
            sql += "   and a.scsubjseq = b.subjseq( +) \n";
            sql += "   and a.year = b.year( +) \n";       ///총정원제약 조건

            ///총정원제약 조건
            ///수강신청기간 제약조건
            sql += "   and decode(a.studentlimit,0 , 1000000,  a.studentlimit) > (select count(userid) from tz_propose where subj = a.subj and subjseq = a.subjseq and year = a.year and (isproposeapproval != 'N'and chkfinal != 'N')) \n";
            sql += "   and to_char(sysdate,'YYYYMMDDHH24') between a.propstart and a.propend \n";


            //// //// //// //// //// //// //// //// //// //// //// // 
            //// 1
            //// 이미신청한과목은 다시 수강신청할수 없다.
            //// //// //// //// //// //// //// //// //// //// //// // 
            sql += "   and (a.subj) not in ( \n";
            sql += "   select\n";
            sql += "     x.subj\n";
            sql += "   from\n";
            sql += "     tz_propose x \n";
            sql += "   where\n";
            sql += "     (x.userid = " +StringManager.makeSQL(v_user_id) + " and x.subj = " +StringManager.makeSQL(subj) + " and (x.chkfinal != 'N' and x.isproposeapproval != 'N') )\n";
            // sql += "     or  (select isgraduated from tz_stold where userid = '" +userid + "' and subj = '" +subj + "') = 'Y'\n";
            sql += ") \n";

            //// //// //// //// //// //// //// //// //// //// //// // 
            //// 2
            //// 수료한과목은 다시 수강신청할수 없다.
            //// //// //// //// //// //// //// //// //// //// //// // 
            sql += "   and (a.subj) not in ( \n";
            sql += "   select\n";
            sql += "     x.subj\n";
            sql += "   from\n";
            sql += "     tz_stold x \n";
            sql += "   where\n";
            sql += "     (x.userid = " +StringManager.makeSQL(v_user_id) + " and x.subj = " +StringManager.makeSQL(subj) + " and x.isgraduated = 'Y')\n";
            sql += ") \n";

            ls1 = connMgr.executeQuery(sql);

            while ( ls1.next() ) { 

                chkseq = true;

                v_subj         = subj;
                v_subjseq      = ls1.getString("subjseq");
                v_year         = ls1.getString("year");
                v_edustart     = ls1.getString("edustart");
                v_eduend       = ls1.getString("eduend");
                v_isonoff      = ls1.getString("isonoff");
                v_isedutarget  = ls1.getString("isedutarget");   // 대상자만 수강신청 할수 있는지
                v_mastercd     = ls1.getString("mastercd");     // 마스터과목 코드로 매핑이 되어 있는지.
                v_proposetype  = ls1.getString("proposetype");

                //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
                // 2. 해당과목이 off line과목일때 학습과목과 중복기간 체크
                //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
                if ( v_isonoff.equals("OFF") &&!(v_proposetype.equals("3")||v_proposetype.equals("2"))) { 
                  chkseq = overLapEduterm(v_edustart,v_eduend,v_user_id);
                }


                //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
                // 3. 해당과목이 마스터과목일때 신청여부/대상자여부 체크
                //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
                if ( chkseq && !v_mastercd.equals("") ) { 
                  ismasterpropose = csBean.IsMasterPropose(v_mastercd, v_user_id);

                  if ( ismasterpropose) {   // 신청을 하지 않았다

                      isedutarget = csBean.IsEduTarget(v_subj, v_year, v_subjseq, v_user_id); // 대상자로 선정이 되어있는 지여부

                      if ( v_isedutarget.equals("Y") ) {  // 해당마스터과목이 대상자 지정 마스터이면

                          if ( isedutarget) {             // 본인이 마스터과목에 매핑된 대상자라면
                            chkseq = true;
                          } else { 
                            chkseq = false;            // 대상자가 아니라면
                          }
                      } else {                            // 대상자 지정과목이 아니면 신청가능하다.
                          chkseq = true;
                      }

                  } else {                 // 신청을 했다
                      chkseq = false;
                  }

                }
                // 신청가능한 기수이면
                if ( chkseq) { 
                  cnt++;
                }
            }

            if ( cnt > 0) { 
                chkseq = true;
            }


        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return chkseq;
    }


     /**
     수강신청 가능기수 리턴
     @param box      receive from the form object and session
     @return int 신청가능기수 상태 Return
     Return : first + second + third
     first  : 수강신청가능여부 0 : 불가능 , 1 : 가능
     second : 대상자과목여부   0 : 대상과목 , 1 : 일반과목
     third  : 대상자여부
     */
     public String possibleSeqStatus_old(RequestBox box, String subj,String subjseq, String gyear,String userid, String comp) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;

        String sql         = "";
        String sql1        = "";
        String v_user_id   = userid;
        String v_comp      = comp;
        String v_gyear     = gyear;
        String cnt         = "";
        String ispropose   = "";

        v_comp             = v_comp.substring(0,4);

        if ( v_user_id.equals("") ) { 
          v_user_id       = box.getSession("userid");
        }

        if ( v_comp.equals("") ) { 
          v_comp          = box.getSession("comp");
          if ( !v_comp.equals("") ) { 
            v_comp                  = v_comp.substring(0,4);
          }
        }

        try { 
            connMgr = new DBConnectionManager();

            sql += " select  ";
            sql += "   count(subjseq)  cnt ";
            sql += " from  ";
            sql += "   VZ_SCSUBJSEQ a ";
            sql += " where ";
            sql += "   grcode in (select grcode from TZ_GRCOMP where comp like " +StringManager.makeSQL(v_comp + "%")+"') ";
            sql += "   and a.gyear = " +StringManager.makeSQL(v_gyear);
            sql += "   and a.scsubjseq = " +StringManager.makeSQL(subjseq);
            sql += "   and a.scsubj = " +StringManager.makeSQL(subj);
            // sql += "   and a.studentlimit >= (select count(userid) from tz_propose where a.subj = subj and a.subjseq = subjseq and a.year = year and (isproposeapproval != 'N'or chkfinal != 'N') and userid= '" +userid + "') ";
            sql += "   and decode(a.studentlimit,0 , 1000000,  a.studentlimit) > (select count(userid) from tz_propose where subj = a.subj and subjseq = a.subjseq and year = a.year and (isproposeapproval != 'N'and chkfinal != 'N')) ";
            sql += "   and to_char(sysdate,'YYYYMMDDHH24') between a.propstart and a.propend ";

            //// 이미신청한과목은 다시 수강신청할수 없다.
            sql += "   and (subj,subjseq,year) not in ( ";
            sql += "     select  ";
            sql += "       subj, subjseq, year  ";
            sql += "     from  ";
            sql += "       tz_propose  ";
            sql += "     where  ";
            sql += "       userid = " +StringManager.makeSQL(userid);
            sql += "       and subj = a.scsubj  ";
            // sql += "       and subjseq = a.scsubjseq  ";
            // sql += "       and year = a.year  ) ";
            // sql += "       and year = a.year
            sql += "     ) ";

            //// 대상자인경우 신청된 과목기수 (마스터과목에 매핑된 모든 과목은 한과목만 신청하면 모든 과목은 다시신청할수 없다.)
            sql += "   and (subj,subjseq,year)not in (        ";
            sql += "     select          ";
            sql += "       b.subj,          ";
            sql += "       b.subjseq,        ";
            sql += "       b.year        ";
            sql += "     from           ";
            sql += "       tz_edutarget a,           ";
            sql += "       vz_mastersubjseq b        ";
            sql += "     where           ";
            sql += "       a.userid = " +StringManager.makeSQL(userid);
            sql += "       and b.isedutarget = 'Y' ";
            sql += "       and a.mastercd = b.mastercd ";
            sql += "       and b.grcode in (select grcode from TZ_GRCOMP where comp like " +StringManager.makeSQL(v_comp + "%")+"')";
            sql += "       and (b.mastercd) in(       ";
            sql += "         select               ";
            sql += "           y.mastercd           ";
            sql += "         from               ";
            sql += "           tz_propose x, vz_mastersubjseq y            ";
            sql += "         where              ";
            sql += "           x.userid=" +StringManager.makeSQL(userid);
            sql += "           and y.grcode in (select grcode from TZ_GRCOMP where comp like " +StringManager.makeSQL(v_comp + "%")+")";
            sql += "           and x.subj = y.subj            ";
            sql += "           and x.subjseq = y.subjseq   ";
            sql += "           and x.year = y.year ";
            sql += "           and (x.isproposeapproval != 'N'  and y.propend >= " +StringManager.makeSQL(FormatDate.getDate("yyyyMMddHH")) + ") ";
            sql += ")            ) ";

            //// 대상자가 없는 마스터과목 단위 신청인 경우
            sql += "   and (subj,subjseq,year)not in ( ";
            sql += "     select             ";
            sql += "     subj,           ";
            sql += "     subjseq,         ";
            sql += "     year          ";
            sql += "   from            ";
            sql += "     vz_mastersubjseq          ";
            sql += "   where ";
            sql += "     grcode in (select grcode from TZ_GRCOMP where comp like " +StringManager.makeSQL(v_comp + "%")+") ";
            sql += "     and gyear = a.gyear  ";
            sql += "     and (mastercd) in ";
            sql += "     (select              ";
            sql += "        y.mastercd         ";
            sql += "      from              ";
            sql += "        tz_propose x, vz_mastersubjseq y           ";
            sql += "      where              ";
            sql += "        x.userid=" +StringManager.makeSQL(userid);
            sql += "        and y.grcode in (select grcode from TZ_GRCOMP where comp like " +StringManager.makeSQL(v_comp + "%")+")              ";
            sql += "        and x.subj = y.subj              ";
            sql += "        and x.subjseq = y.subjseq          ";
            sql += "        and x.year = y.year             ";
            // sql += "        and x.cancelkind = 'P' or            ";
            sql += "        and (x.isproposeapproval != 'N'  and y.propend >= " +StringManager.makeSQL(FormatDate.getDate("yyyyMMddHH")) + ") ";
            sql += ")            ) ";

            // 마스터과목에 대상자매핑이되어 있지 않은 경우 마스터과목에 매핑된 모든과목은 수강신청할수 없다.
            sql += "   and (subj,subjseq,year) not in(  ";
            sql += "   select  ";
            sql += "     subj,subjseq,year ";
            sql += "   from  ";
            sql += "     vz_mastersubjseq   ";
            sql += "   where ";
            sql += "     subj = a.subj ";
            sql += "     and subjseq = a.subjseq ";
            sql += "     and year = a.year ";
            sql += "     and isedutarget = 'Y' ";
            sql += "     and mastercd not in(select mastercd from tz_edutarget where userid = " +StringManager.makeSQL(userid) + ") ";
            sql += "   ) ";

            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();
            if ( rs1.next() ) { 
                cnt = rs1.getString("cnt");
                if ( cnt.equals("1") ) { 
                  cnt = "Y";
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return cnt;
    }
     

     /**
     수강신청중인 과목여부(수강신청 시작 --- 교육시작일전까지)
     @param box      receive from the form object and session
     @param returnvalue == > 1:수강신??여부 2: 수강신청여부 +현업팀장결재여부 3 : 수강신청여부 +현업팀장결재여부 +최종승인여부
     @return String
     */
     public String getPropseStatus(RequestBox box, String subj,String subjseq, String year,String userid, String returnvalue) throws Exception { 
         DBConnectionManager connMgr         = null;
         ListSet             ls              = null;
         StringBuffer        sbSQL           = new StringBuffer("");
         
         int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
          
         String              v_user_id       = userid;
         String              return_value    = returnvalue;
         String              ispropose       = "";
         String              chkfinal        = "";

         if ( v_user_id.equals("") ) { 
           v_user_id                         = box.getSession("userid");
         }

         try { 
             connMgr = new DBConnectionManager();
             
             sbSQL.append(" SELECT  DECODE(userid , " + StringManager.makeSQL(v_user_id) + ", 'Y', 'N') ispropose     \n")
                  .append("     ,   chkfinal                                                     \n")
                  .append(" from    tz_propose                                                   \n")
                  .append(" where   subj        = " + SQLString.Format(subj      )  + "          \n")
                  .append(" and     subjseq     = " + SQLString.Format(subjseq   )  + "          \n")
                  .append(" and     year        = " + SQLString.Format(year      )  + "          \n")
                  .append(" and     userid      = " + SQLString.Format(v_user_id )  + "          \n");
                  
             ls  = connMgr.executeQuery(sbSQL.toString());

             if ( ls.next() ) { 
               ispropose = ls.getString("ispropose"  );
               chkfinal  = ls.getString("chkfinal"   );
             }

             if ( ispropose.equals("") )
                 ispropose = "N";
             
             if ( chkfinal.equals("") )
                 chkfinal = "0";
                 
             if ( return_value.equals("1") ) { 
                 return_value    = ispropose;
             } else if ( return_value.equals("3") ) { 
                 return_value    = ispropose + chkfinal;
             } else { 
                return_value     = ispropose;
             }
         } catch ( SQLException e ) {
             ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
             throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
         } catch ( Exception e ) {
             ErrorManager.getErrorStackTrace(e);
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

         return return_value;
     }


    /**
    기수 정보 리턴
    @param box      receive from the form object and session
    @return DataBox
    */
     public DataBox SubjseqInfo(String subj, String grcode, String subjseq, String year) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox             dbox    = null;

        try { 
          connMgr = new DBConnectionManager();
          dbox = SubjseqInfo(connMgr, subj, grcode, subjseq, year);

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }

    /**
    기수 정보 리턴
    @param box      receive from the form object and session
    @return DataBox
    */
     public DataBox SubjseqInfo(DBConnectionManager connMgr, String subj, String grcode, String subjseq, String year) throws Exception { 
        ListSet ls1         = null;
        DataBox dbox        = null;

        String sql1           = "";

        String v_eduterm      = "";
        String v_grdappstatus   = ""; // 결재승인상태

        SubjComBean scbean = new SubjComBean();
        // ChiefApprovalBean caBean    = new ChiefApprovalBean();

        try { 
            // if ( ss_action.equals("go") ) { 
                sql1 += " select            \n";
                sql1 += "    A.isonoff,     \n";
                sql1 += "    A.subjseqgr,   \n";
                sql1 += "    A.isclosed,    \n";
                sql1 += "    A.edustart,    \n";
                sql1 += "    A.eduend,      \n";
                sql1 += "    decode(A.studentlimit, 0, 1000000, A.studentlimit) studentlimit, \n";
                sql1 += "    (select count(userid) from tz_propose where a.subj = subj and a.subjseq = subjseq and a.year = year) propcnt,\n";
                sql1 += "    a.iscpflag     \n";
                sql1 += " from              \n";
                sql1 += "  VZ_SCSUBJSEQ A   \n";
                sql1 += " where             \n";
                sql1 += "  A.subj = " +StringManager.makeSQL(subj) + " \n";
                sql1 += "  and A.grcode = " +SQLString.Format(grcode) + " \n";
                sql1 += "  and A.subjseq = " +SQLString.Format(subjseq) + " \n";
                sql1 += "  and A.year    = " +SQLString.Format(year) + " \n";
                sql1 += "  order by subjseq";

                ls1 = connMgr.executeQuery(sql1);

                v_eduterm = scbean.getEduTerm(subj, subjseq, year);
                // v_grdappstatus = caBean.getApprovalStatus(subj,year,subjseq,"4");

                if ( ls1.next() ) { 
                    dbox = ls1.getDataBox();
                    dbox.put("d_eduterm", v_eduterm);             // 현재 교육기간
                    // dbox.put("d_grdappstatus", v_grdappstatus);   // 수료처리 결재상태
                }
            // }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
        }

        return dbox;
    }



    /**
    subj 존재 여부 체크
    @param subj
    @return String Return
    */
     public String isExitSubj(String subj) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String errvalue = "";

        try { 
            connMgr = new DBConnectionManager();
            errvalue = isExitSubj(connMgr, subj);
        }

        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }


    /**
    subj 존재 여부 체크
    @param subj
    @return String Return
    */
     public String isExitSubj(DBConnectionManager connMgr, String subj) throws Exception { 
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;

        String sql         = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";

        try { 
            sql +="select count(*) CNT from tz_subj where subj = " +StringManager.makeSQL(subj) + " \n";
            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();

            if ( rs1.next() ) { 
                cnt = rs1.getInt("CNT");
                if ( cnt > 0) { 
                  errvalue = "0";   // 정상
                }
                else{ 
                  errvalue = "12";   //  인사DB에 존재하지 않습니다.
                }
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
        }

        return errvalue;
    }




    /**
    기수 존재 여부 체크
    @param box      receive from the form object and session
    @return String Return
    */
     public String isExitSeq(String grcode, String gyear, String subj, String subjseq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String errvalue = "";

        try { 
            connMgr = new DBConnectionManager();
            errvalue = isExitSeq(connMgr, grcode, gyear, subj, subjseq);
        }

        catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }


    /**
    기수 존재 여부 체크
    @param box      receive from the form object and session
    @return String Return
    */
     public String isExitSeq(DBConnectionManager connMgr, String grcode, String gyear, String subj, String subjseq) throws Exception { 
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        // ResultSet rs1 = null;
        ResultSet rs1 = null;

        String sql         = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";


        try { 

            sql +="\n select count(scsubjseq) cnt "
            	+ "\n from   vz_scsubjseq "
            	+ "\n where  grcode = " +StringManager.makeSQL(grcode) 
            	+ "\n and    gyear = " +StringManager.makeSQL(gyear) 
            	+ "\n and    subj = " +StringManager.makeSQL(subj) 
            	+ "\n and    subjseq = " +StringManager.makeSQL(subjseq);

            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();
            if ( rs1.next() ) { 
                cnt = rs1.getInt("cnt");
                if ( cnt > 0) { 
                  errvalue = "0";   // 정상.
                }
                else{ 
                  errvalue = "5";   // 기수미존재
                }

            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
        }

        return errvalue;
    }


    /**
    기수 존재 여부 체크
    @param box      receive from the form object and session
    @return String Return
    */
     public String isStudent(String grcode, String gyear, String subj, String subjseq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        // ResultSet rs1 = null;
        ResultSet rs1 = null;

        String sql         = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";


        try { 
            connMgr = new DBConnectionManager();

            sql +="select count(userid) CNT  from tz_pr where grcode = " +StringManager.makeSQL(grcode) + " and gyear = " +StringManager.makeSQL(gyear) + " and subj = " +StringManager.makeSQL(subj) + " and subjseq = " +StringManager.makeSQL(subjseq) + " \n";
            // System.out.println("sql_seqchk == " +sql);

            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();
            if ( rs1.next() ) { 
                cnt = rs1.getInt("CNT");
                if ( cnt > 0) { 
                  errvalue = "0";   // 정상.
                }
                else{ 
                  errvalue = "5";   // 기수미존재
                }

            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }



    /**
    수강신청 여부 체크
    @param box      receive from the form object and session
    @return String Return
    */
     public String isProposeExit(String userid, String subj, String year, String subjseq) throws Exception { 

        DBConnectionManager	connMgr	= null;
        String errvalue = "";

        try { 
            connMgr = new DBConnectionManager();
            errvalue = isProposeExit(connMgr, userid, subj, year, subjseq);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }

        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }


    /**
    수강신청 여부 체크
    @param box      receive from the form object and session
    @return String Return
    */
     public String isProposeExit(DBConnectionManager connMgr, String userid, String subj, String year, String subjseq) throws Exception { 
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;

        String              sql     = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";


        try { 

            sql +="select count(subjseq) CNT  from tz_propose where subj = " +StringManager.makeSQL(subj) + " and year = " +StringManager.makeSQL(year) + " and userid = " +StringManager.makeSQL(userid) + "and subjseq = " +StringManager.makeSQL(subjseq) + " \n";
            // System.out.println("sql_propchk == " +sql);

            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();
            if ( rs1.next() ) { 

                cnt = rs1.getInt("CNT");
                if ( cnt > 0) { 
                  errvalue = "0";   // 정상
                }
                else{ 
                  errvalue = "14";   // 수강신청중
                }
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
        }

        return errvalue;
    }

    /**
    수강신청 여부 체크
    @param box      receive from the form object and session
    @return String Return
    */
     public String isPropose(String userid, String subj, String year, String subjseq) throws Exception { 

        DBConnectionManager	connMgr	= null;
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;

        String              sql     = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";


        try { 
            connMgr = new DBConnectionManager();

            sql +="select count(subjseq) CNT  from tz_propose where subj = " +StringManager.makeSQL(subj) + " and year = " +StringManager.makeSQL(year) + " and userid = " +StringManager.makeSQL(userid) + "and subjseq = " +StringManager.makeSQL(subjseq) + " \n";

            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();
            if ( rs1.next() ) { 

                cnt = rs1.getInt("CNT");
                if ( cnt > 0) { 
                  errvalue = "7";   // 수강신청중
                }
                else{ 
                  errvalue = "0";   // 정상
                }
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }

 	/**
 	 * 수강신청 여부 체크
 	 * @param box      receive from the form object and session
 	 * @return String Return
 	 */
 	public String isPropose(DBConnectionManager connMgr, String userid, String subj, String year, String subjseq) throws Exception { 

 		PreparedStatement pstmt1 = null;
 		ResultSet rs1 = null;
 		String              sql     = "";
 		int cnt = 0;
 		String errvalue = "";


 		try { 

		 	sql +="select count(subjseq) CNT  from tz_propose where subj = " +StringManager.makeSQL(subj) + " and year = " +StringManager.makeSQL(year) + " and userid = " +StringManager.makeSQL(userid) + "and subjseq = " +StringManager.makeSQL(subjseq) + " \n";
		 	
		 	pstmt1 = connMgr.prepareStatement(sql);
		 	
		 	rs1 = pstmt1.executeQuery();
		 	if ( rs1.next() ) { 
		
			 	cnt = rs1.getInt("CNT");
			 	if ( cnt > 0) { 
			 		errvalue = "7";   // 수강신청중
			 	}
			 	else{ 
			 		errvalue = "0";   // 정상
			 	}
		 	}
	
	 	} catch ( Exception ex ) { 
		 	ErrorManager.getErrorStackTrace(ex, null, sql);
		 	throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
	 	} finally { 
		 	if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
		 	if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
	 	}
	
	 	return errvalue;
 	}

    /**
    학습자 여부 체크
    @param box      receive from the form object and session
    @return String Return
    */
     public String isExitStudent(String userid, String subj, String year, String subjseq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;
        String              sql     = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";

        try { 
            connMgr = new DBConnectionManager();

            sql +="select count(subjseq) CNT  from tz_student where subj = " +StringManager.makeSQL(subj) + " and year = " +StringManager.makeSQL(year) + " and userid = " +StringManager.makeSQL(userid) + "and subjseq = " +StringManager.makeSQL(subjseq) + " \n";

            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();

            if ( rs1.next() ) { 
                cnt = rs1.getInt("CNT");
                if ( cnt > 0) { 
                  errvalue = "0";   // 정상
                } else{ 
                  errvalue = "8";   // 학습자가 아닙니다
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }


    /**
    과목 수료 여부
    @param box      receive from the form object and session
    @return String Return
    */
     public String isStold(String userid, String subj, String year, String subjseq) throws Exception { 

        DBConnectionManager	connMgr	= null;
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;

        String              sql     = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";

        try { 
            connMgr = new DBConnectionManager();

            sql +="select count(subjseq) CNT  from tz_stold where subj = " +StringManager.makeSQL(subj) + " and year = " +StringManager.makeSQL(year) + " and userid = " +StringManager.makeSQL(userid) + " and isgraduated = 'Y' \n";

            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();
            if ( rs1.next() ) { 
                cnt = rs1.getInt("CNT");
                if ( cnt > 0) { 
                  errvalue = "9";   // 수료처리된 과목
                }
                else{ 
                  errvalue = "0";   // 정상
                }
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }


 	/**
 	 * 과목 수료 여부
 	 * @param box      receive from the form object and session
 	 * @return String Return
 	 */
 	public String isStold(DBConnectionManager connMgr, String userid, String subj, String year, String subjseq) throws Exception { 

	 	PreparedStatement pstmt1 = null;
	 	ResultSet rs1 = null;
	 	String sql = "";
	 	int cnt = 0;
	 	String errvalue = "";
	
	 	try { 
		 	sql = "\n select count(subjseq) CNT "
		 		+ "\n from   tz_stold "
		 		+ "\n where  subj = " +StringManager.makeSQL(subj) 
		 		+ "\n and    year = " +StringManager.makeSQL(year) 
		 		+ "\n and    userid = " + StringManager.makeSQL(userid) 
		 		+ "\n and    isgraduated = 'Y' \n";
		 	
		 	pstmt1 = connMgr.prepareStatement(sql);
		 	
		 	rs1 = pstmt1.executeQuery();
		 	
		 	if ( rs1.next() ) { 
			 	cnt = rs1.getInt("CNT");
			 	if ( cnt > 0) { 
			 		errvalue = "9";   // 수료처리된 과목
			 	}
			 	else{ 
			 		errvalue = "0";   // 정상
			 	}
		 	}
		
	 	} catch ( Exception ex ) { 
		 	ErrorManager.getErrorStackTrace(ex, null, sql);
		 	throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
	 	} finally { 
		 	if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
		 	if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
	 	}
	
	 	return errvalue;
 	}

    /**
    Member 존재 여부 체크
    @param box      receive from the form object and session
    @return String Return
    @ 신청가능하면 true (중복안됨)
    @ 신청불가능하면 false(중복됨)
    */
     public String isExitMember(String userid, String isretired, String isemtpty, String isstoped) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox= null;
        ListSet ls1 = null;

        String sql         = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";

        try { 
            connMgr = new DBConnectionManager();

            sql +="select 'Y' office_gbn from tz_member where lower(userid) = lower(" +StringManager.makeSQL(userid) + ") \n";
            ls1 = connMgr.executeQuery(sql);

            if ( ls1.next() ) { 
                if ( ls1.getString("office_gbn").equals("N") && isretired.equals("Y") ) { 
                    errvalue = "2";   // 퇴직자입니다.
                }
                else{ 
                  errvalue = "0";   // 정상
                }

            } else { 
              errvalue = "1";  // 인사DB에 존재하지 않습니다.
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }

    /**
     * Member 존재 여부 체크
     * @param box      receive from the form object and session
     * @return String Return
     * @ 신청가능하면 true (중복안됨)
     * @ 신청불가능하면 false(중복됨)
     */
	public String isExitMember(DBConnectionManager connMgr, String userid, String isretired, String isemtpty, String isstoped) throws Exception { 
		ListSet ls1 = null;
		String sql      = "";
		String errvalue = "";

		try { 
			sql +="select 'Y' office_gbn from tz_member where lower(userid) = lower(" +StringManager.makeSQL(userid) + ") \n";
             
			ls1 = connMgr.executeQuery(sql);

			if ( ls1.next() ) { 
				if ( ls1.getString("office_gbn").equals("N") && isretired.equals("Y") ) { 
					errvalue = "2";   // 퇴직자입니다.
//				}
//				else if ( ls1.getString("lyocf").equals("*") && isemtpty.equals("Y") ) { 
//					errvalue = "3";   // 휴직자입니다.
//				}
//				else if ( ls1.getString("sspnc").equals("*") && isstoped.equals("Y") ) { 
//					errvalue = "4";   // 정직자입니다.
				} else { 
					errvalue = "0";   // 정상
				}

			} else { 
				errvalue = "1";  // 인사DB에 존재하지 않습니다.
			}

		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, null, sql);
			throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
		}
		return errvalue;
	}

    /**
    Member 존재 여부 체크
    @param userid           USER ID
    @return String Return
    @ 신청가능하면 true (중복안됨)
    @ 신청불가능하면 false(중복됨)
    */
     public String isExitMember(String userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String errvalue = "";
        try { 
            connMgr = new DBConnectionManager();
            errvalue = isExitMember(connMgr, userid);
        } catch ( Exception ex ) { 
        }

        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }



    /**
    Member 존재 여부 체크
    @param userid           USER ID
    @return String Return
    @ 신청가능하면 true (중복안됨)
    @ 신청불가능하면 false(중복됨)
    */
     public String isExitMember(DBConnectionManager connMgr, String userid) throws Exception { 
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;
        String sql         = "";
        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";

        try { 
            sql +="select count(*) CNT from tz_member where userid = " +StringManager.makeSQL(userid) + " \n";
            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();

            if ( rs1.next() ) { 
                cnt = rs1.getInt("CNT");
                if ( cnt > 0) { 
                  errvalue = "0";   // 정상
                }
                else{ 
                  errvalue = "1";   //  인사DB에 존재하지 않습니다.
                }
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
        }
    return errvalue;
   }

    /**
    튜터 존재 여부 체크
    @param box      receive from the form object and session
    @return String Return
    */
     public String isExitTutor(String tutorid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;

        String sql         = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";


        try { 
            connMgr = new DBConnectionManager();

            sql +="select count(*) CNT from tz_tutor where userid = " +StringManager.makeSQL(tutorid);

            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();
            if ( rs1.next() ) { 
                cnt = rs1.getInt("CNT");
                if ( cnt > 0) { 
                  errvalue = "0";   // 정상
                }
                else{ 
                  errvalue = "11";   // 강사정보가 존재하지 않습니다.
                }
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }


     /**
    엑셀데이터중복존재여부
    @param box      receive from the form object and session
    @return String Return
    */
     public String isExitDupli(String inputtime, String userid, String subj, String year, String subjseq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String errvalue = "";

        try { 
            connMgr = new DBConnectionManager();
            errvalue = isExitDupli(connMgr, inputtime, userid, subj, year, subjseq);

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }

        finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }


     /**
    엑셀데이터중복존재여부
    @param box      receive from the form object and session
    @return String Return
    */
     public String isExitDupli(DBConnectionManager connMgr, String inputtime, String userid, String subj, String year, String subjseq) throws Exception { 
        DataBox dbox= null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;

        String sql         = "";

        int cnt = 0;
        boolean chkseq = true;
        String errvalue = "";

        try { 

            sql +="select count(*) CNT from tz_cpexceltemp where ";
            sql +=" inputtime = " +StringManager.makeSQL(inputtime);
            sql +=" and subj    = " +StringManager.makeSQL(subj);
            sql +=" and subjseq = " +StringManager.makeSQL(subjseq);
            sql +=" and year    = " +StringManager.makeSQL(year);
            sql +=" and lower(userid)  = lower(" +StringManager.makeSQL(userid) + ") ";
            
            pstmt1 = connMgr.prepareStatement(sql);
            rs1 = pstmt1.executeQuery();
            if ( rs1.next() ) { 
                cnt = rs1.getInt("CNT");
                if ( cnt > 0) { 
                  errvalue = "13";   // 엑셀데이터에 이미 존재합니다.
                }
                else{ 
                  errvalue = "0";   // 정상
                }
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
        }

        return errvalue;
    }
     
     /**
     열린교육
     @param box      receive from the form object and session
     @return ArrayList 열린교육 조회 목록
     */
      public ArrayList selectOpenEdu(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr         = null;
         ListSet             ls1             = null;
         ArrayList           list1           = null;
         
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
         String              v_basis_upperclass = box.getString("basis_upperclass");

         int                 v_propstart     = 0;
         int                 v_propend       = 0;
         boolean             ispossible      = false;

         int v_pageno        = box.getInt("p_pageno");
         try { 
             connMgr = new DBConnectionManager();
             list1   = new ArrayList();

             /*sbSQL.append(" select  distinct a.subj                                                      \n")
                  .append("     ,   a.scupperclass                                                       \n")
                  .append("     ,   a.scmiddleclass                                                      \n")
                  .append("     ,   a.isonoff                                                            \n")
                  .append("     ,   b.classname                                                          \n")
                  .append("     ,   a.subjnm                                                             \n")
                  .append("     ,   a.edustart                                                           \n")
                  .append("     ,   a.eduend                                                             \n")
                  .append("     ,   a.studentlimit                                                       \n") 
                  .append("     ,   a.scsubjseq                                                          \n")
                  .append("     ,   a.scyear                                                             \n")
                  .append("     ,   a.eduurl                                                             \n")
                  .append("     ,   decode(length(propstart), 0, propstart, substr(propstart, 0, 10)) propstart\n")
                  .append("     ,   decode(length(propend), 0, propend, substr(propend, 0, 10)) propend   \n")                                                     
                  .append(" from    VZ_SCSUBJSEQ    a                                                    \n")
                  .append("     ,   (                                                                    \n")
                  .append("             select  upperclass                                               \n")
                  .append("                 ,   middleclass                                              \n")
                  .append("                 ,   classname                                                \n")
                  .append("             from    tz_subjatt                                               \n")
                  .append("             where   middleclass <> '000'                                     \n")
                  .append("             and     lowerclass  =  '000'                                     \n")
                  .append("         )               b                                                    \n")
                  .append(" where   a.scupperclass  = b.upperclass                                       \n")
                  .append(" and     a.scmiddleclass = b.middleclass                                      \n")
                  .append(" and     a.grcode        = " + SQLString.Format(v_grcode ) + "                \n")
                  .append(" and     a.isuse         = 'Y'                                                \n")
                  .append(" and     a.subjvisible   = 'Y'                                                \n")
                  .append(" and     a.isOpenEdu     = 'Y'                                                \n");
                  //.append(" and     a.gyear         = " + SQLString.Format(gyear    ) + "                \n");*/

             sbSQL.append("select a.subj, a.subjnm, a.isopenedu, a.contenttype, a.eduurl, a.isonoff,     \n")
                  .append("       b.classname                                                            \n")
                  .append("from tz_subj  a                                                               \n")
                  .append("     ,   (                                                                    \n")
                  .append("             select  upperclass                                               \n")
                  .append("                 ,   middleclass                                              \n")
                  .append("                 ,   classname                                                \n")
                  .append("             from    tz_subjatt                                               \n")
                  .append("             where   middleclass <> '000'                                     \n")
                  .append("             and     lowerclass  =  '000'                                     \n")
                  .append("         )               b                                                    \n")
                  .append(" where   a.upperclass  = b.upperclass                                         \n")
                  .append(" and     a.middleclass = b.middleclass                                        \n")
                  .append(" and     a.isuse         = 'Y'                                                \n")
                  .append(" and     a.isOpenEdu     = 'Y'                                                \n");
        
             sbSQL.append(" order by a.subjnm                                                            \n");
             
//             System.out.println(this.getClass().getName() + "." + "selectSubjSearch() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

             ls1 = connMgr.executeQuery(sbSQL.toString());
             ls1.setPageSize(row);             //  페이지당 row 갯수를 세팅한다
             ls1.setCurrentPage(v_pageno);                    //     현재페이지번호를 세팅한다.
             int total_page_count = ls1.getTotalPage();       //     전체 페이지 수를 반환한다
             int total_row_count = ls1.getTotalCount();    //     전체 row 수를 반환한다
             
             while ( ls1.next() ) { 
                 dbox    = ls1.getDataBox();
                 dbox.put("d_dispnum", new Integer(total_row_count - ls1.getRowNum() + 1));
                 dbox.put("d_totalpage", new Integer(total_page_count));
                 dbox.put("d_rowcount", new Integer(row));
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
      
      /**
      메인의 운영중인 과목 Top 5개
      @param box      receive from the form object and session
      @return ArrayList 운영중인 과목 Top 5개
      */
      public ArrayList selectCourseIng(RequestBox box) throws Exception { 
          DBConnectionManager   connMgr         = null;
          ListSet             ls1             = null;
          ArrayList           list1           = null;
          DataBox             dbox            = null;
          StringBuffer        sbSQL       = new StringBuffer("");
          
          String              v_user_id       = box.getSession("userid"           );
          String              v_grcode        = box.getSession("tem_grcode"       );
          String s_comp = box.getSession("comp");
          String s_sysdate = FormatDate.getDate("yyyyMM");
          try { 
              connMgr = new DBConnectionManager();
              list1   = new ArrayList();
//              sbSQL.append("select *                                                                 \n")
//              .append("from (																		 \n")
//              .append("		select distinct a.subj                                                   \n")
//              .append("     ,   a.scupperclass                                                       \n")
//              .append("     ,   a.scmiddleclass                                                      \n")
//              .append("     ,   a.isonoff                                                            \n")
//              .append("     ,   b.classname                                                          \n")
//              .append("     ,   a.subjnm                                                             \n")
//              .append("     ,   a.edustart                                                           \n")
//              .append("     ,   a.eduend                                                             \n")
//              .append("     ,   a.studentlimit                                                       \n") 
//              .append("     ,   a.scyear                                                             \n")   
//              .append("     ,   a.scsubjseq                                                          \n")    
//              .append("     ,   a.propose_date                                                       \n")
//              .append("     ,   (case when length(propstart) = 0 then propstart else substr(propstart, 0, 10) end) propstart\n")
//              .append("     ,   (case when length(propend) = 0 then propend else substr(propend, 0, 10) end) propend   \n")                                                     
//              .append("     ,   (                                                                    \n")
//              .append("             select  substr(nvl(indate,'00000000000000'),0,4 )                \n")
//              .append("             from    TZ_SUBJ                                                  \n")
//              .append("             where   subj = a.subj                                            \n")
//              .append("         )                               indate                               \n")
//              .append("     ,   (                                                                    \n")
//              .append("             select  nvl(avg(distcode1_avg), 0.0)                             \n")
//              .append("             from    tz_suleach                                               \n")                  
//              .append("             where   subj    = a.subj                                         \n")
//              .append("             and     grcode <> 'ALL'                                          \n")
//              .append("         )                               distcode1_avg                        \n")
//              .append("     ,   (                                                                    \n")
//              .append("             select  nvl(avg(distcode_avg), 0.0)                              \n")
//              .append("             from    tz_suleach                                               \n")                   
//              .append("             where   subj= a.subj                                             \n")
//              .append("             and     grcode = 'ALL'                                           \n")
//              .append("         )                               distcode_avg                         \n")
//              .append("     ,   (                                                                    \n")
//              .append("             select count(userid) CNTS                                        \n")
//              .append("             from tz_propose c                                                \n")
//              .append("             where c.subj = a.scsubj                                          \n")
//              .append("             and c.year = a.scyear                                            \n")
//              .append("             and c.subjseq = a.scsubjseq                                      \n")
//              .append("         ) proposecnt                                                         \n")
//              .append("     ,   nvl(c.haspreviewobj, 'N')      haspreviewobj                         \n")
//              .append(" from    (                                                                                                        \n")
//              .append("          SELECT tp.ldate propose_date,          \n")
//              .append("              tp.userid                                                                                        \n")
//              .append("              ,  vs.*                                                                                             \n")
//              .append("          FROM   VZ_SCSUBJSEQ vs left outer join Tz_Propose   tp                                                  \n")
//              .append("          on     vs.scsubj        = tp.subj                                                                    \n")
//              .append("          and    vs.scyear        = tp.year                                                                    \n")
//              .append("          and    vs.scsubjseq     = tp.subjseq                                                                 \n")
//              .append("          and    tp.userid     = " + SQLString.Format(v_user_id) + "                                           \n")
//              .append("          WHERE  vs.grcode        =  " + SQLString.Format(v_grcode ) + "                                         \n")
//              .append("          and    vs.isuse         = 'Y'                                                                           \n")
//              .append("          and    vs.subjvisible   = 'Y'                                                                           \n")
//              .append("          and    vs.isblended     = 'N'                                                                           \n")
//              .append("          and    vs.isexpert      = 'N'                                                                           \n")
//              .append("          and    substr(to_char(sysdate,'YYYYMMDDHH24MISS'), 1, 6) between substr(propstart, 1, 6) and substr(eduend, 1, 6)         \n")
//              .append("          and    nvl(tp.cancelkind, ' ')   NOT IN ('F', 'P')                                                   \n")
//              .append("         )               a  inner join (                                                                    \n")
//              .append("             select  upperclass                                               \n")
//              .append("                 ,   middleclass                                              \n")
//              .append("                 ,   classname                                                \n")
//              .append("             from    tz_subjatt                                               \n")
//              .append("             where   middleclass <> '000'                                     \n")
//              .append("             and     lowerclass  =  '000'                                     \n")
//              .append("         )               b                                                    \n")
//              .append(" on      a.scupperclass  = b.upperclass                                       \n")
//              .append(" and     a.scmiddleclass = b.middleclass                                      \n")
//              .append(" left outer join (                                                            \n")
//              .append("             select  subj, (case when count(*)= 0 then 'N' else 'Y' end)  haspreviewobj       \n")
//              .append("             from    tz_previewobj                                            \n")
//              .append("             group by subj                                                    \n")
//              .append("         )               c                                                    \n")
//              .append(" on      a.subj          = c.subj                                             \n")
//              .append(" where     a.grcode        = " + SQLString.Format(v_grcode ) + "                \n")
//              .append(" and     a.isuse         = 'Y'                                                \n")
//              .append(" and     a.subjvisible   = 'Y'                                                \n")
//              .append(" and    substr(a.specials, 3, 1) = 'Y'                                     \n")
//               
//              .append(" and     substr(to_char(sysdate,'YYYYMMDDHH24MISS'), 1, 6) between substr(propstart, 1, 6) and substr(a.propend, 1, 6)\n")
//              .append(") x                                                                           \n")
//              .append("where rownum <= 3  \n");
              
              sbSQL.append("select subj, isonoff, subjnm												\n")
              	   .append("from (																		\n")
              	   .append("    select subj, max(isonoff) isonoff, max(subjnm) subjnm					\n")
              	   .append("	from vz_scsubjseq														\n")
              	   .append("    where grcode in (select grcode from tz_grcomp where comp = " + StringManager.makeSQL(s_comp) + ")		\n")
              	   .append("    and ( edustart like " + StringManager.makeSQL(s_sysdate + "%") + " or eduend like " + StringManager.makeSQL(s_sysdate + "%") + ")					\n")
              	   .append("    and isuse = 'Y'															\n")
              	   .append("    and seqvisible = 'Y'													\n")
              	   .append("    and subjvisible = 'Y'													\n")
              	   .append("    and length(edustart) > 7												\n")
              	   .append("    and length(eduend) > 7													\n")
              	   .append("    and ((edustart between " + StringManager.makeSQL(s_sysdate + "01") + " and " + StringManager.makeSQL(s_sysdate + "31") + ")	or ( eduend between " + StringManager.makeSQL(s_sysdate + "01") + " and " + StringManager.makeSQL(s_sysdate + "31") + " 	))	\n")
              	   .append("    group by subj															\n")
              	   .append("    order by min(edustart), min(eduend)										\n")
              	   .append(")																			\n")
              	   .append("where rownum <= 3															\n");
              
              ls1 = connMgr.executeQuery(sbSQL.toString());
              
              while ( ls1.next() ) { 
                  dbox    = ls1.getDataBox();
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
      
     //----------------------------------------------------------
     
    /**
  	 신규과목 리스트(메인)
  	 @param box receive from the form object and session
  	 @return ArrayList 추천 결과 리스트
  	 */
  	public ArrayList selectSubjNewMain(RequestBox box) throws Exception { 
  		DBConnectionManager	connMgr	= null;
  		ListSet ls1 = null;
  		ArrayList list1 = null;
  		DataBox dbox = null;
  		String sql = "";
  		String v_grcode	= box.getSession("tem_grcode"); // 교육그룹
  		
  		if(v_grcode.equals("")){
  			v_grcode="N000001";
  		}
  		
  		try { 
  			list1 = new ArrayList();
  			
            sql  = "select subj, upperclass, isonoff, classname, subjnm	\n";
            sql += "     , introducefilenamenew, ldate, explain 		\n";
            sql += "     , contenttype							 		\n";
            sql += "from  (												\n";	
            sql += "       select distinct c.subj as subj  				\n";
            sql += "            , c.upperclass as upperclass  			\n";
            sql += "            , c.isonoff as isonoff  				\n";
            sql += "            , b.classname as classname  			\n";
            sql += "            , c.subjnm as subjnm  					\n";
            sql += "            , c.introducefilenamenew  				\n";
            sql += "            , c.ldate  								\n";
            sql += "            , c.explain as explain  				\n";
            sql += "            , c.contenttype			  				\n";
            sql += "	   from  (select upperclass  					\n";
            sql += "           		   , classname  					\n";
            sql += "       		  from   tz_subjatt  					\n";
            sql += "              where  middleclass = '000'  			\n";
            sql += "      		  and    lowerclass ='000') b  			\n";
            sql += "            , tz_subj c, tz_grsubj d  				\n";
            sql += " 	   where  c.upperclass = b.upperclass  			\n";
            sql += "	   and    c.subj = d.subjcourse 				\n";
            sql += "       and 	  d.grcode=" +SQLString.Format(v_grcode)+" \n";
            sql += "	   and    c.isuse = 'Y' 						\n";
            sql += "	   and    c.isvisible = 'Y'  					\n";
            sql += "	   and    substr(c.specials, 1, 1) = 'Y'  		\n";
            sql += "	   order by c.ldate desc  \n";
            sql += "      )												\n";
            sql += "where  rownum <= 3									\n";
  			
            logger.info("과정리스트 : " + sql);
            
            
            
  			connMgr = new DBConnectionManager();

  			ls1 = connMgr.executeQuery(sql);
  			
  			while ( ls1.next() ) { 
  				dbox = ls1.getDataBox();
  				list1.add(dbox);
  			}
  		} catch ( Exception ex ) { 
  			ErrorManager.getErrorStackTrace(ex, box, sql);
  			throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
  		} finally { 
  			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
  			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
  		}
  		
  		return list1;
  	}

  	/**
	 인기과목 리스트(메인)
	 @param box receive from the form object and session
	 @return ArrayList 추천 결과 리스트
	 */
	public ArrayList selectSubjHitMain(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet ls1 = null;
		ArrayList list1 = null;
		DataBox dbox = null;
		String sql = "";		
		String gyear = box.getStringDefault("gyear",FormatDate.getDate("yyyy"));
		String v_grcode	= box.getSession("tem_grcode"); // 교육그룹
		
  		if(v_grcode.equals("")){
  			v_grcode="N000001";
  		}
  		
		
		try { 
			list1 = new ArrayList();
			
			/*
            sql = "select subj									\n"
                + "     , upperclass							\n"
                + "     , isonoff 								\n"
                + "     , classname 							\n"
                + "     , subjnm 								\n"
                + "     , totcnt 								\n"
                + "     , introducefilenamenew 					\n"
                + "     , explain 								\n"
                + "     , haspreviewobj 						\n"
                + "from  ( 										\n"
                + "       select distinct a.subj subj 			\n"
                + "            , a.scupperclass upperclass 		\n"
                + "            , a.isonoff isonoff 				\n"
                + "            , b.classname classname 			\n"
                + "            , a.subjnm subjnm           		\n"
                + "            ,(select count(subjseq)  		\n"
                + "              from   tz_subjseq x  			\n"
                + "              where  x.subj = a.subj  		\n"
                + "              and    x.gyear = a.gyear  		\n"
                + "             ) totcnt  						\n"
                + "            , c.introducefilenamenew  		\n"
                + "            , c.explain						\n"
                + "            , case when a.preurl is null or a.preurl = '' or a.contenttype = 'L' then 'N' else 'Y' end as haspreviewobj  \n"
                + "       from   vz_scsubjseq a  				\n"
                + "            ,(select upperclass  			\n"
                + "                   , classname  				\n"
                + "              from   tz_subjatt  			\n"
                + "              where  middleclass = '000'  	\n"
                + "              and    lowerclass ='000') b  	\n"
                + "            , tz_subj c  					\n"
                + "       where  a.scupperclass = b.upperclass  \n"
                + "       and    a.isuse = 'Y'  				\n"
                + "       and    a.subjvisible = 'Y'  			\n"
                + "       and    substr(a.specials, 2, 1) = 'Y' \n"
                + "       and    a.gyear = " + StringManager.makeSQL(gyear) + " \n"
                + "       and    a.subj = c.subj  				\n"
                + "       order by a.subj desc	\n"
                + "      )										\n"
                + "where  rownum <= 3							\n";
			*/
			
            sql  = "select subj, upperclass, isonoff, classname, subjnm	\n";
            sql += "     , introducefilenamenew, ldate, explain 		\n";
            sql += "     , contenttype							 		\n";
            sql += "from  (												\n";	
            sql += "       select distinct c.subj as subj  				\n";
            sql += "            , c.upperclass as upperclass  			\n";
            sql += "            , c.isonoff as isonoff  				\n";
            sql += "            , b.classname as classname  			\n";
            sql += "            , c.subjnm as subjnm  					\n";
            sql += "            , c.introducefilenamenew  				\n";
            sql += "            , c.ldate  								\n";
            sql += "            , c.explain as explain  				\n";
            sql += "            , c.contenttype			  				\n";
            sql += "	   from  (select upperclass  					\n";
            sql += "           		   , classname  					\n";
            sql += "       		  from   tz_subjatt  					\n";
            sql += "              where  middleclass = '000'  			\n";
            sql += "      		  and    lowerclass ='000') b  			\n";
            sql += "            , tz_subj c, tz_grsubj d  				\n";
            sql += " 	   where  c.upperclass = b.upperclass  			\n";
            sql += "	   and    c.subj = d.subjcourse 				\n";
            sql += "       and 	  d.grcode=" +SQLString.Format(v_grcode)+" \n";
            sql += "	   and    c.isuse = 'Y' 						\n";
            sql += "	   and    c.isvisible = 'Y'  					\n";
            sql += "	   and    substr(c.specials, 2, 1) = 'Y'  		\n";
            sql += "	   order by c.ldate desc  \n";
            sql += "      )												\n";
            sql += "where  rownum <= 3									\n";
            
			connMgr = new DBConnectionManager();
			ls1 = connMgr.executeQuery(sql);
			
			while ( ls1.next() ) { 
				dbox = ls1.getDataBox();
				list1.add(dbox);
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return list1;
	}
     
	/**
	 추천과목 리스트(메인)
	 @param box receive from the form object and session
	 @return ArrayList 추천 결과 리스트
	 */
	public ArrayList selectSubjRecomMain(RequestBox box) throws Exception { 
		DBConnectionManager	connMgr	= null;
		ListSet ls1 = null;
		ArrayList list1 = null;
		DataBox dbox = null;
		String sql = "";		
		String gyear = box.getStringDefault("gyear",FormatDate.getDate("yyyy"));
		String v_grcode	= box.getSession("tem_grcode"); // 교육그룹
		
  		if(v_grcode.equals("")){
  			v_grcode="N000001";
  		}
  		
		try { 
			list1 = new ArrayList();
			
			/*
			sql = "select subj										\n"
			    + "     , upperclass								\n"
			    + "     , isonoff                                   \n" 
			    + "     , classname									\n" 
			    + "     , subjnm                                    \n" 
			    + "     , totcnt									\n" 
			    + "     , introducefilenamenew						\n" 
			    + "     , explain									\n" 
			    + "     , haspreviewobj								\n" 
			    + "     , contenttype								\n" 
			    + "from  (											\n" 
			    + "	      select distinct a.subj as subj  			\n"
			    + "	           , a.scupperclass as upperclass  		\n"
			    + "	           , a.isonoff as isonoff  				\n"
			    + "	           , b.classname as classname  			\n"
			    + "	           , a.subjnm as subjnm  				\n"
			    + "	           ,(select count(subjseq)  			\n"
			    + "	             from   tz_subjseq x  				\n"
			    + "	             where  x.subj = a.subj  			\n"
			    + "	             and    x.gyear = a.gyear  			\n"
			    + "	            ) as totcnt  						\n"
			    + "	           , c.introducefilenamenew  			\n"
			    + "	           , c.explain as explain 				\n"
			    + "	           , c.contenttype		 				\n"
			    //+ "            , a.year  							\n"
			    //+ "            , a.subjseq  						\n"
			    + "	           , case when a.preurl is null or a.preurl = '' or a.contenttype = 'L' then 'N' else 'Y' end as haspreviewobj  \n"
			    + "	      from   vz_scsubjseq a  					\n"
			    + "	           ,(select upperclass  				\n"
			    + "	                  , classname  					\n"
			    + "	             from   TZ_SUBJATT  				\n"
			    + "	             where  middleclass = '000'  		\n"
			    + "	             and    lowerclass ='000') b  		\n"
			    + "	           , tz_subj c  						\n"
			    + "	      where  a.scupperclass = b.upperclass  	\n"
			    + "	      and    a.isuse = 'Y'  					\n"
			    + "	      and    a.subjvisible = 'Y'  				\n"
			    + "	      and    substr(a.specials, 3, 1) = 'Y'  	\n"
			    + "	      and    a.gyear = " +StringManager.makeSQL(gyear) + " \n"
			    + "	      and    a.subj = c.subj  					\n"
			    //+ "       and    to_char(sysdate, 'yyyymmdd') between substring(a.propstart, 1, 8) and substring(a.propend, 1, 8)  \n"
			    + "       order  by a.subj desc  	\n"
			    + "      )											\n"
			    + "where  rownum <= 3								\n";
			*/
			
            sql  = "select subj, upperclass, isonoff, classname, subjnm	\n";
            sql += "     , introducefilenamenew, ldate, explain 		\n";
            sql += "     , contenttype							 		\n";
            sql += "from  (												\n";	
            sql += "       select distinct c.subj as subj  				\n";
            sql += "            , c.upperclass as upperclass  			\n";
            sql += "            , c.isonoff as isonoff  				\n";
            sql += "            , b.classname as classname  			\n";
            sql += "            , c.subjnm as subjnm  					\n";
            sql += "            , c.introducefilenamenew  				\n";
            sql += "            , c.ldate  								\n";
            sql += "            , c.explain as explain  				\n";
            sql += "            , c.contenttype			  				\n";
            sql += "	   from  (select upperclass  					\n";
            sql += "           		   , classname  					\n";
            sql += "       		  from   tz_subjatt  					\n";
            sql += "              where  middleclass = '000'  			\n";
            sql += "      		  and    lowerclass ='000') b  			\n";
            sql += "            , tz_subj c, tz_grsubj d  				\n";
            sql += " 	   where  c.upperclass = b.upperclass  			\n";
            sql += "	   and    c.subj = d.subjcourse 				\n";
            sql += "       and 	  d.grcode=" +SQLString.Format(v_grcode)+" \n";
            sql += "	   and    c.isuse = 'Y' 						\n";
            sql += "	   and    c.isvisible = 'Y'  					\n";
            sql += "	   and    substr(c.specials, 3, 1) = 'Y'  		\n";
            sql += "	   order by c.ldate desc  \n";
            sql += "      )												\n";
            sql += "where  rownum <= 3									\n";
            
			connMgr = new DBConnectionManager();
			ls1 = connMgr.executeQuery(sql);
			
			while ( ls1.next() ) { 
				dbox = ls1.getDataBox();
				list1.add(dbox);
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
		} finally { 
			if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		
		return list1;
	}
	
	/**
     * 교육후기
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList stoldCommentList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list = null;
        DataBox dbox        = null;

        String  sql        = "";
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            sql = "select * from 															\n"
            	+ "	(select seq																\n"            	
            	+ "		, subj																\n"
            	+ "		, year																\n"
            	+ "		, subjseq															\n"
            	+ "		, userid															\n"
            	+ "		, comments															\n"
            	+ "		, ldate																\n"
            	+ "	from tz_stold_comments													\n"        	
            	+ "	where hidden_yn ='Y'													\n"        	
            	+ "	order by ldate desc) 													\n"
		    	+ "where  rownum <= 4														\n";    
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
	
	
	/**
     * 지식채널
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList resourceList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list = null;
        DataBox dbox        = null;

        String  sql        = "";
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            sql = "select SEQ, BUNRU_SEQ, CATEGORY, TITLE  from 							\n"
            	+ "	(select SEQ																\n"            	
            	+ "		, BUNRU_SEQ															\n"
            	+ "		, CATEGORY															\n"
            	+ "		, TITLE																\n"         	
            	+ "	from TZ_RESOURCES_INFO													\n"      
            	+ "	where DEL_FLAG != 'Y'													\n"                   	
            	+ "	order by SEQ desc) 													\n"
		    	+ "where  rownum <= 4														\n";    
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

	
	/**
     * 커뮤니티
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList communityNoticeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls         = null;
        ArrayList list = null;
        DataBox dbox        = null;

        String  sql        = "";
        
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            sql = "select faq_type, faqno, title, register_dte  from 				\n"
            	+ "	(select faq_type, faqno											\n"            	
            	+ "		, title														\n"
            	+ "		, register_dte												\n"        	
            	+ "	from tz_cmufaq													\n" 
            	+ "	where faq_type='DIRECT'											\n"        	            	
            	+ "	order by register_dte desc) 									\n"
		    	+ "where  rownum <= 4												\n";    
            ls = connMgr.executeQuery(sql);
            System.out.println("sql==\n"+sql);
            while ( ls.next() ) {  
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }	
	
	
	/**
   전체과정검색
   @param box      receive from the form object and session
   @return ArrayList 전체과정검색
   */
    public ArrayList selectSearchSubject(RequestBox box) throws Exception { 
       DBConnectionManager    connMgr         = null;
       ListSet             ls1             = null;
       ArrayList           list1           = null;
       
       DataBox             dbox            = null;
       String              sql             = "";
       int v_pageno        = box.getInt("p_pageno");
       String v_month    = box.getStringDefault("p_month", "ALL");
       String ss_upperclass = box.getStringDefault("s_upperclass", "ALL");
       String ss_middleclass = box.getStringDefault("s_middleclass", "ALL");
       String ss_subjsearchkey = box.getString("p_lsearchtext");
       String ss_isonoff = box.getStringDefault("s_isonoff", "ALL");
       
       String year = FormatDate.getDate("yyyy"); 
       try { 
           connMgr = new DBConnectionManager();
           list1   = new ArrayList();


//           sql = "select b.isonoff									\n"
//           	+ "	, a.subj scsubj									\n"
//           	+ " , a.year scyear									\n"
//           	+ " , a.subjnm	scsubjnm							\n"
//           	+ " , a.subjseq	scsubjseq							\n"
//           	+ " , a.studentlimit								\n"
//           	+ " , a.edustart									\n"
//           	+ " , a.eduend										\n"
//           	+ " , b.edumans subjtarget							\n"
//           	+ " , b.upperclass									\n"
//           	+ "	, b.middleclass									\n"
//           	+ "	, (												\n"
//           	+ "		select classname							\n"
//           	+ "		from tz_subjatt								\n"
//           	+ "		where upperclass = b.upperclass				\n"
//           	+ "		and middleclass = '000'						\n"
//           	+ "	) upperclassname								\n"
//           	+ "	, (												\n"
//           	+ "		select classname							\n"
//           	+ "		from tz_subjatt								\n"
//           	+ "		where subjclass = b.subjclass				\n"
//           	+ "		and middleclass = b.middleclass				\n"
//           	+ "	) middleclassname								\n"
//           	+ "from tz_subjseq a, tz_subj b						\n"
//           	+ "where a.subj = b.subj							\n"
//           	+ "and b.isvisible = 'Y'							\n"
//           	+ "and b.isuse = 'Y'								\n"
//           	+ "and a.gyear= " + StringManager.makeSQL(year) + " \n";
//           
//               if(!"ALL".equals(v_month)) {
//                   sql += "and ( substr(edustart, 5, 2) = " + SQLString.Format(v_month)       + "  or substr(eduend, 5, 2) = " + SQLString.Format(v_month)       + "  ) \n";
//               }
//               
//               if(!"ALL".equals(ss_upperclass)) {
//                   sql+= "and upperclass = " + SQLString.Format(ss_upperclass) + " \n";
//               }
//               
//               if(!"ALL".equals(ss_middleclass)) {
//                   sql+= "and middleclass = " + SQLString.Format(ss_middleclass) + " \n";
//               }
//               
//               if(!"".equals(ss_subjsearchkey)) {
//                   sql+= "and a.subjnm like " + SQLString.Format("%" + ss_subjsearchkey + "%") + " \n";
//               }
//               
//               sql += "order by edustart";
			sql = "select subj											\n"
				+ "     ,(												\n"
				+ "		  select classname								\n"
				+ "		  from   tz_subjatt								\n"
				+ "		  where  upperclass = a.upperclass				\n"
				+ "		  and    middleclass = '000'					\n"
				+ "	     ) upperclassname								\n"
				+ "     , upperclass                                    \n"
				+ "     , subjnm                                        \n"
				+ "     , isonoff                                       \n"
				+ "     , edumans 										\n"
				+ "from   tz_subj a                                   	\n"
				+ "where  isuse = 'Y'									\n"
				+ "and    isvisible = 'Y'								\n";
	           
			if(!"ALL".equals(ss_isonoff)) {
				sql+= "and isonoff = " + SQLString.Format(ss_isonoff) + " 		\n";
			}
    
			if(!"ALL".equals(ss_upperclass)) {
				sql+= "and upperclass = " + SQLString.Format(ss_upperclass) + " 		\n";
			}
			  
			if(!"ALL".equals(ss_middleclass)) {
				sql+= "and middleclass = " + SQLString.Format(ss_middleclass) + " \n";
			}
			  
			if(!"".equals(ss_subjsearchkey)) {
				sql+= "and a.subjnm like " + SQLString.Format("%" + ss_subjsearchkey + "%") + " \n";
			}
           
			ls1 = connMgr.executeQuery(sql);
			ls1.setPageSize(row);             				// 페이지당 row 갯수를 세팅한다
			ls1.setCurrentPage(v_pageno);                   // 현재페이지번호를 세팅한다.
			int total_page_count = ls1.getTotalPage();      // 전체 페이지 수를 반환한다
			int total_row_count = ls1.getTotalCount();    	// 전체 row 수를 반환한다
           
			while ( ls1.next() ) { 
				dbox    = ls1.getDataBox();
				dbox.put("d_dispnum", new Integer(total_row_count - ls1.getRowNum() + 1));
				dbox.put("d_totalpage", new Integer(total_page_count));
				dbox.put("d_rowcount", new Integer(row));
				list1.add(dbox);
			}
		} catch ( SQLException e ) {
			ErrorManager.getErrorStackTrace(e, box, sql);
			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
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

    /**
   추천과정
    @param box          receive from the form object and session
    @return ArrayList   추천과정
    */
    public ArrayList SelectRecomSubj(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        DataBox             dbox    = null;
        ArrayList 			list 	= null;
        
        String 	sql  		= "";
        String  v_grcode	= box.getSession("tem_grcode"); // 교육그룹
        
        try { 
        		list = new ArrayList();
        		
                sql  = " select upperclass, classname, isonoff, get_codenm('0004',isonoff) inonoffval, subj, \n";
                sql += "        subjnm, introducefilenamereal, introducefilenamenew, intro \n"; // subj
                sql += " from  ( \n";
                sql += "        select a.upperclass, b.classname, a.isonoff,  \n";
                sql += "               a.subj, a.subjnm, a.introducefilenamereal,a.introducefilenamenew, a.intro \n";
                sql += "        from   tz_subj a,  tz_subjatt b, tz_grsubj c \n";
                sql += "        where  a.subjclass  = b.subjclass \n";
                sql += "        and    a.subj  		= c.subjcourse \n";                 
                sql += "        and    substr(a.specials, 3,1) = 'Y' and isuse ='Y' and isvisible = 'Y'	\n";
                sql += "        and 	grcode=" +SQLString.Format(v_grcode)+" \n";                 
                sql += "        order  by a.ldate desc \n";
                sql += "       ) \n";
                sql += "where  rownum <= 2";
                
                connMgr = new DBConnectionManager();
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
    인기과정
     @param box          receive from the form object and session
     @return ArrayList   인기과정
     */
     public ArrayList SelectHitSubj(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet             	ls     	= null;
         DataBox             	dbox   	= null;
         ArrayList 			 	list	= null;
         
         String sql  		= "";
         String  v_grcode	= box.getSession("tem_grcode"); // 교육그룹       
                  
         try { 
         		 list = new ArrayList();
         		
                 sql  = " select upperclass, classname, isonoff, get_codenm('0004',isonoff) inonoffval, subj, \n";
                 sql += "        subjnm, introducefilenamereal, introducefilenamenew, intro \n"; // subj
                 sql += " from  ( \n";
                 sql += "        select a.upperclass, b.classname, a.isonoff,  \n";
                 sql += "               a.subj, a.subjnm, a.introducefilenamereal,a.introducefilenamenew, a.intro \n";
                 sql += "        from   tz_subj a,  tz_subjatt b, tz_grsubj c \n";
                 sql += "        where  a.subjclass = b.subjclass \n";
                 sql += "        and    a.subj  	= c.subjcourse \n";                 
                 sql += "        and    substr(a.specials, 2,1) = 'Y' and isuse ='Y' and isvisible = 'Y'	\n";
                 sql += "        and 	grcode=" +SQLString.Format(v_grcode)+" \n";                 
                 sql += "        order  by a.ldate desc \n";
                 sql += "       ) \n";
                 sql += "where  rownum <= 2";

                 
	             connMgr = new DBConnectionManager();
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
     신규과정
      @param box          receive from the form object and session
      @return ArrayList   신규과정
      */
      public ArrayList SelectNewSubj(RequestBox box) throws Exception { 
          DBConnectionManager	connMgr	= null;
          ListSet           	ls      = null;
          DataBox           	dbox    = null;
          ArrayList 			list 	= null;
          
          String  sql  		= "";
          String  v_grcode	= box.getSession("tem_grcode"); // 교육그룹
          
          try { 
          		list = new ArrayList();
          		
                sql  = " select upperclass, classname, isonoff, get_codenm('0004',isonoff) inonoffval, subj, \n";
                sql += "        subjnm, introducefilenamereal, introducefilenamenew, intro \n"; // subj
                sql += " from  ( \n";
                sql += "        select a.upperclass, b.classname, a.isonoff,  \n";
                sql += "               a.subj, a.subjnm, a.introducefilenamereal,a.introducefilenamenew, a.intro \n";
                sql += "        from   tz_subj a,  tz_subjatt b, tz_grsubj c \n";
                sql += "        where  a.subjclass  = b.subjclass \n";
                sql += "        and    a.subj 		= c.subjcourse \n";                 
                sql += "        and    substr(a.specials, 1,1) = 'Y' and isuse ='Y' and isvisible = 'Y'	\n";
                sql += "        and 	grcode=" +SQLString.Format(v_grcode)+" \n";                 
                sql += "        order  by a.ldate desc \n";
                sql += "       ) \n";
                sql += "where  rownum <= 1";
                  
              connMgr = new DBConnectionManager();
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
    해당 과정 월차별 도서로 존재하는 지 여부
    @param box      receive from the form object and session
    @return String Return
    */
     public String isSubjMonth(String subj, int v_month, int v_bookcode) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;

        String              sql     = "";

        int cnt = 0;
        String errvalue = "";


        try { 
            connMgr = new DBConnectionManager();

            sql +="select count(bookcode) CNT from tz_subjbook where subj = " +StringManager.makeSQL(subj) + " and month = " + SQLString.Format(v_month) + " and bookcode = " + SQLString.Format(v_bookcode) + "	\n";
            pstmt1 = connMgr.prepareStatement(sql);
            
            rs1 = pstmt1.executeQuery();
            if ( rs1.next() ) { 

                cnt = rs1.getInt("CNT");
                if ( cnt > 0) { 
                  errvalue = "0";	// 정상   
                }
                else{ 
                  errvalue = "20";   // 존재 안함
                }
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
        }

        finally { 
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return errvalue;
    }
     
     /**
     해당 과정 월차별 도서로 존재하는 지 여부
     @param box      receive from the form object and session
     @return String Return
     */
      public String isSubjMonth(DBConnectionManager connMgr, String subj, int v_month, int v_bookcode) throws Exception { 
//         DBConnectionManager	connMgr	= null;
         PreparedStatement pstmt1 = null;
         ResultSet rs1 = null;

         String              sql     = "";

         int cnt = 0;
         String errvalue = "";


         try { 
//             connMgr = new DBConnectionManager();

             sql +="select count(bookcode) CNT from tz_subjbook where subj = " +StringManager.makeSQL(subj) + " and month = " + SQLString.Format(v_month) + " and bookcode = " + SQLString.Format(v_bookcode) + "	\n";
             pstmt1 = connMgr.prepareStatement(sql);
             
             rs1 = pstmt1.executeQuery();
             if ( rs1.next() ) { 

                 cnt = rs1.getInt("CNT");
                 if ( cnt > 0) { 
                   errvalue = "0";	// 정상   
                 }
                 else{ 
                   errvalue = "20";   // 존재 안함
                 }
             }

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, null, sql);
             throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
         }

         finally { 
             if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
             if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
//             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return errvalue;
     }
     
     
     
     /**
     해당 과정의 월차별 도서로 등록한 책인지 여부(tz_proposebook에 이미 등록한 내역이 있는지 판단)
     @param box      receive from the form object and session
     @return String Return
     */
      public String isProposeBook(String subj, String v_year, String v_subjseq, String v_userid, int v_month, int v_bookcode) throws Exception { 
         DBConnectionManager	connMgr	= null;
         PreparedStatement pstmt1 = null;
         ResultSet rs1 = null;

         String              sql     = "";

         int cnt = 0;
         String errvalue = "";


         try { 
             connMgr = new DBConnectionManager();

             sql +="select count(bookcode) CNT from tz_proposebook where subj = " +StringManager.makeSQL(subj) + " and year= " + StringManager.makeSQL(v_year) + " and subjseq = " + StringManager.makeSQL( v_subjseq ) + " and userid = " + StringManager.makeSQL(v_userid) + " and month = " + SQLString.Format(v_month) + " and bookcode = " + SQLString.Format(v_bookcode) + "	\n";
             pstmt1 = connMgr.prepareStatement(sql);
             rs1 = pstmt1.executeQuery();
             if ( rs1.next() ) { 

                 cnt = rs1.getInt("CNT");
                 if ( cnt > 0) { 
                   errvalue = "21";	// 이미 존재   
                 }
                 else{ 
                   errvalue = "0";   // 정상
                 }
             }

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, null, sql);
             throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
         }

         finally { 
             if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
             if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return errvalue;
     }
      
      /**
      해당 과정의 월차별 도서로 등록한 책인지 여부(tz_proposebook에 이미 등록한 내역이 있는지 판단)
      @param box      receive from the form object and session
      @return String Return
      */
       public String isProposeBook(DBConnectionManager connMgr, String subj, String v_year, String v_subjseq, String v_userid, int v_month, int v_bookcode) throws Exception { 
//          DBConnectionManager	connMgr	= null;
          PreparedStatement pstmt1 = null;
          ResultSet rs1 = null;

          String              sql     = "";

          int cnt = 0;
          String errvalue = "";


          try { 
//              connMgr = new DBConnectionManager();

              sql +="select count(bookcode) CNT from tz_proposebook where subj = " +StringManager.makeSQL(subj) + " and year= " + StringManager.makeSQL(v_year) + " and subjseq = " + StringManager.makeSQL( v_subjseq ) + " and userid = " + StringManager.makeSQL(v_userid) + " and month = " + SQLString.Format(v_month) + " and bookcode = " + SQLString.Format(v_bookcode) + "	\n";
              pstmt1 = connMgr.prepareStatement(sql);
              rs1 = pstmt1.executeQuery();
              if ( rs1.next() ) { 

                  cnt = rs1.getInt("CNT");
                  if ( cnt > 0) { 
                    errvalue = "21";	// 이미 존재   
                  }
                  else{ 
                    errvalue = "0";   // 정상
                  }
              }

          } catch ( Exception ex ) { 
              ErrorManager.getErrorStackTrace(ex, null, sql);
              throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
          }

          finally { 
              if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
              if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
//              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }

          return errvalue;
      }

       /**
       해당 과정의 월차별 도서로 등록되있으며, 승인인 됬는지 여부(tz_proposebook에 이미 등록한 내역이 있는지 판단)
       @param box      receive from the form object and session
       @return String Return
       */
        public String isProposeBookStatus(DBConnectionManager connMgr, String subj, String v_year, String v_subjseq, String v_userid, int v_month, int v_bookcode) throws Exception { 
//           DBConnectionManager	connMgr	= null;
           PreparedStatement pstmt1 = null;
           ResultSet rs1 = null;

           String sql     = "";

           String v_status = "";
           String errvalue = "";


           try { 
//               connMgr = new DBConnectionManager();

               sql +="select status from tz_proposebook where subj = " +StringManager.makeSQL(subj) + " and year= " + StringManager.makeSQL(v_year) + " and subjseq = " + StringManager.makeSQL( v_subjseq ) + " and userid = " + StringManager.makeSQL(v_userid) + " and month = " + SQLString.Format(v_month) + " and bookcode = " + SQLString.Format(v_bookcode) + "	\n";
               pstmt1 = connMgr.prepareStatement(sql);
               rs1 = pstmt1.executeQuery();
               if ( rs1.next() ) { 

            	   v_status = rs1.getString("status");
                   if ( "B".equals(v_status)) { 
                     errvalue = "36";	// 승인전  
                   }
                   else{ 
                     errvalue = "0";   // 정상
                   }
               }

           } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, null, sql);
               throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
           }

           finally { 
               if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
               if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
//               if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
           }

           return errvalue;
       }
       
      /**
      해당 과정 차수의 배송 정보가 있는지
      @param box      receive from the form object and session
      @return String Return
      */
       public String isDelivery(String subj, String v_year, String v_subjseq, String v_userid) throws Exception { 
          DBConnectionManager	connMgr	= null;
          PreparedStatement pstmt1 = null;
          ResultSet rs1 = null;

          String              sql     = "";

          int cnt = 0;
          String errvalue = "";


          try { 
              connMgr = new DBConnectionManager();

              sql +="select count(*) CNT from tz_delivery where subj = " +StringManager.makeSQL(subj) + " and year= " + StringManager.makeSQL(v_year) + " and subjseq = " + StringManager.makeSQL( v_subjseq ) + " and userid = " + StringManager.makeSQL(v_userid) + "	\n";
              pstmt1 = connMgr.prepareStatement(sql);
              rs1 = pstmt1.executeQuery();
              if ( rs1.next() ) { 

                  cnt = rs1.getInt("CNT");
                  if ( cnt > 0) { 
                    errvalue = "30";	// 이미 존재   
                  }
                  else{ 
                    errvalue = "0";   // 정상
                  }
              }

          } catch ( Exception ex ) { 
              ErrorManager.getErrorStackTrace(ex, null, sql);
              throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
          }

          finally { 
              if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
              if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }

          return errvalue;
      }
       
       /**
       해당 과정 차수의 배송 정보가 있는지
       @param box      receive from the form object and session
       @return String Return
       */
        public String isDelivery(DBConnectionManager connMgr, String subj, String v_year, String v_subjseq, String v_userid) throws Exception { 
//           DBConnectionManager	connMgr	= null;
           PreparedStatement pstmt1 = null;
           ResultSet rs1 = null;

           String              sql     = "";

           int cnt = 0;
           String errvalue = "";


           try { 
//               connMgr = new DBConnectionManager();

               sql +="select count(*) CNT from tz_delivery where subj = " +StringManager.makeSQL(subj) + " and year= " + StringManager.makeSQL(v_year) + " and subjseq = " + StringManager.makeSQL( v_subjseq ) + " and userid = " + StringManager.makeSQL(v_userid) + "	\n";
               pstmt1 = connMgr.prepareStatement(sql);
               rs1 = pstmt1.executeQuery();
               if ( rs1.next() ) { 

                   cnt = rs1.getInt("CNT");
                   if ( cnt > 0) { 
                     errvalue = "30";	// 이미 존재   
                   }
                   else{ 
                     errvalue = "0";   // 정상
                   }
               }

           } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, null, sql);
               throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
           }

           finally { 
               if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
               if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
//               if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
           }

           return errvalue;
       }

    /**
    해당 과정 차수의 배송 정보가 있는지
    @param box      receive from the form object and session
    @return String Return
    */
	public String isDeliveryWayBill(DBConnectionManager connMgr, String subj, String v_year, String v_subjseq, String v_userid, int v_month, int v_bookcode) throws Exception { 
//		DBConnectionManager	connMgr	= null;
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;

		String              sql     = "";
		int cnt = 0;
		String errvalue = "";

		try { 

			sql = "\n select count(*) cnt "
				+ "\n from   tz_proposebook "
				+ "\n where  subj = " +StringManager.makeSQL(subj) 
				+ "\n and    year= " + StringManager.makeSQL(v_year) 
				+ "\n and    subjseq = " + StringManager.makeSQL( v_subjseq ) 
				+ "\n and    userid = " + StringManager.makeSQL(v_userid)
				+ "\n and    month = " + v_month
				+ "\n and    bookcode = " + v_bookcode
				+ "\n and    waybill_no is not null";
			pstmt1 = connMgr.prepareStatement(sql);
			rs1 = pstmt1.executeQuery();
			if ( rs1.next() ) { 

				cnt = rs1.getInt("cnt");
				if ( cnt > 0) { 
					errvalue = "37";	// 이미 존재   
				}
				else{ 
					errvalue = "0";   // 정상
				}
			}

		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, null, sql);
			throw new Exception("sql1 = " + sql + "\r\n" + ex.getMessage() );
		}

		finally { 
			if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
			if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
//			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return errvalue;
	}   
	
   /**
   에러 메세지
   @param String          에러값
   @return String Return  에러 메세지
   */
    public String isGetErrtxt(String errvalue) throws Exception { 

       String errtxt = "";

       if ( errvalue.equals("0") ) { 
         errtxt = "정상입력.";
       }
       else if ( errvalue.equals("1") ) { 
         errtxt = "인사DB에 존재하지 않습니다.";
       }
       else if ( errvalue.equals("2") ) { 
         errtxt = "퇴직자입니다.";   // 
       }
       else if ( errvalue.equals("3") ) { 
         errtxt = "휴직자입니다.";   // 
       }
       else if ( errvalue.equals("4") ) { 
         errtxt = "정직자입니다.";
       }
       else if ( errvalue.equals("5") ) { 
         errtxt = "미개설기수 입니다.";
       }
       else if ( errvalue.equals("6") ) { 
         errtxt = "중복되는 오프라인과정이 있습니다..";
       }
       else if ( errvalue.equals("7") ) { 
         errtxt = "신청정보에 이미 존재합니다.";
       }
       else if ( errvalue.equals("8") ) { 
         errtxt = "학습자가 아닙니다";
       }
       else if ( errvalue.equals("9") ) { 
         errtxt = "이미 수료처리 되었습니다.";
       }
       else if ( errvalue.equals("9") ) { 
         errtxt = "강사정보가 존재하지 않습니다.";
       }
       else if ( errvalue.equals("10") ) { 
         errtxt = "이미 수강제약 리스트에 등록 되어있습니다.";
       }
       else if ( errvalue.equals("12") ) { 
         errtxt = "과목코드가 존재하지 않습니다.";
       }
       else if ( errvalue.equals("13") ) { 
         errtxt = "엑셀데이터에 이미 존재합니다.";
       }
       else if ( errvalue.equals("14") ) { 
         errtxt = "학습자정보에 존재하지 않습니다.";
       }
       else if ( errvalue.equals("22") ) { 
         errtxt = "수료처리가 완료된 과정입니다.";
       }
       else if ( errvalue.equals("23") ) { 
         errtxt = "이미 등록이 완료된 과정입니다.";
       }
       else if ( errvalue.equals("24") ) { 
         errtxt = "수료여부 오류입니다.";
       }
       else if ( errvalue.equals("25") ) { 
         errtxt = "총점계산 오류입니다.";
       }
       else if ( errvalue.equals("31") ) { 
         errtxt = "수강생수가 일치하지 않습니다.";
       }
       else if ( errvalue.equals("32") ) { 
         errtxt = "이미 수료처리가 완료되었습니다.";
       }
       else if ( errvalue.equals("33") ) { 
         errtxt = "이미 결과등록이 완료되었습니다.";
       }
       else if ( errvalue.equals("34") ) { 
         errtxt = "현재 교육기간이 아닙니다.";
       }
       else if ( errvalue.equals("35") ) { 
         errtxt = "교육이 아직종료되지 않았습니다.";
       }
       else if ( errvalue.equals("999") ) { 
         errtxt = "DB처리 중 에러가 발생하였습니다.";
       }
       else if ( errvalue.equals("18") ) { 
           errtxt = "수강등록 후 독서교육과정 도서등록을 실행해 주십시오.";
       }
       else if ( errvalue.equals("19") ) { 
           errtxt = "독서교육과정 수강 도서 등록은 독서교육과정만 가능합니다.";
       }
       else if ( errvalue.equals("20") ) { 
           errtxt = "해당 과정에 입력하신 월차별 도서가 존재하지 않습니다.";
       }
       else if ( errvalue.equals("21") ) { 
           errtxt = "해당 과정의 월차별 도서로 이미 등록되어 있습니다.";
       }
       else if ( errvalue.equals("30") ) { 
           errtxt = "해당 과정의 배송정보가 이미 등록되어 있습니다.";
       }
       else if ( errvalue.equals("36") ) { 
           errtxt = "승인되지 않은 자료는 운송장번호를 등록할 수 없습니다.";
       }
       else if ( errvalue.equals("37") ) { 
           errtxt = "이미 운송장번호가 등록되어 있습니다. 수정은 도서배송관리의 배송정보에서 할 수 있습니다.";
       }
       else if ( errvalue.equals("38") ) { 
           errtxt = "배송지정보가 등록되어 있지 않습니다.";
       }
       else if ( errvalue.equals("39") ) { 
           errtxt = "이미 수료한 과정입니다.";
       }
       else if ( errvalue.equals("40") ) { 
           errtxt = "이미 다른기수에서 학습중인 과정입니다.(동일과정의 한기수만 학습할 수 있습니다.)";
       }
       else if ( errvalue.equals("41") ) { 
           errtxt = "이미 다른기수를 수강신청한 과정입니다.(동일과정의 한기수만 학습할 수 있습니다.)";
       }
       else { 
         errtxt = "DB처리 중 에러가 발생하였습니다.";
       }

       return errtxt;
   }

}