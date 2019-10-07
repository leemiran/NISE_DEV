// **********************************************************
//  1. f      ��: �λ�DB �˻�
//  2. �wα׷���? MemberSearchBean.java
//  3. ��      ��: �λ�DB �˻�
//  4. ȯ      ��: JDK 1.4
//  5. ��      o: 1.0
//  6. ��      ��:  2004. 12. 20
//  7. ��      d:
// **********************************************************

package com.ziaan.system;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ziaan.common.GetCodenm;
import com.ziaan.complete.CompleteStatusData;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.study.StudyStatusData;

public class StatisticsBean {

	private Logger logger = Logger.getLogger(this.getClass());
    // private
    private ConfigSet config;
    private int         row;
    
    public StatisticsBean() {
    }

    public static String getOrganization(RequestBox box, boolean isChange,
            boolean isALL) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        String sql = "";
        String result = "v�� ";

        try {
            String p_orgcode = box.getStringDefault("p_orgcode", "ALL"); // ��0���?

            connMgr = new DBConnectionManager();

            sql = "select orgcode, title ";
            sql += " from tz_organization ";
            sql += " order by title";

            pstmt = connMgr.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            ls = new ListSet(pstmt);

            result += getSelectTag(ls, isChange, isALL, "p_orgcode", p_orgcode);
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        } finally {
            if (ls != null) {
                try {
                    ls.close();
                } catch (Exception e) {
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e1) {
                }
            }
            if (connMgr != null) {
                try {
                    connMgr.freeConnection();
                } catch (Exception e10) {
                }
            }
        }
        return result;
    }

    public static String getSelectTag(ListSet ls, boolean isChange,
            boolean isALL, String selname, String optionselected)
            throws Exception {
        StringBuffer sb = null;
        boolean isSelected = false;
        String v_tmpselname = "";

        try {
            sb = new StringBuffer();

            if (selname.equals("s_damungyyyy")) {
                v_tmpselname = "p_orgcode";
                sb.append("<select name = \"" + v_tmpselname + "\"");
            } else {
                sb.append("<select name = \"" + selname + "\"");
            }
            if (isChange)
                sb.append(" onChange = \"whenSelection('change')\"");
            sb.append(" > \r\n");
            if (isALL) {
                sb.append("<option value = \"ALL\" > ALL</option > \r\n");
            } else if (isChange) {
                if (selname.indexOf("year") == -1)
                    sb
                            .append("<option value = \"----\" >== ���� == </option > \r\n");
            }

            while (ls.next()) {
                ResultSetMetaData meta = ls.getMetaData();
                int columnCount = meta.getColumnCount();

                sb.append("<option value = \"" + ls.getString(1) + "\"");
                if (optionselected.equals(ls.getString(1)) && !isSelected) {
                    sb.append(" selected");
                    isSelected = true;
                } else if (selname.equals("s_gyear")
                        && ls.getString("gyear").equals(
                                FormatDate.getDate("yyyy"))
                        && optionselected.equals("") && !isSelected) { // ����ڵ������
                                                                        // ����
                    sb.append(" selected");
                    isSelected = true;
                }

                sb
                        .append(" > " + ls.getString(columnCount)
                                + "</option > \r\n");
            }
            sb.append("</select > \r\n");
        } catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, true);
            throw new Exception(ex.getMessage());
        }
        return sb.toString();
    }

    /**
     * ��d�� ��0����
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList performStatistics_03_01_L(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls1         = null;
        ArrayList           list1       = null;
        String              sql1        = "";
        
        CompleteStatusData  data1       = null;
        
        int                 v_pageno    = box.getInt            ( "p_pageno"                );
        String              ss_grcode   = box.getStringDefault  ( "s_grcode"        , "ALL" ); // ��0�׷�
        String              ss_gyear    = box.getStringDefault  ( "s_gyear"         , "ALL" );  // �⵵
        String              ss_grseq    = box.getStringDefault  ( "s_grseq"         , "ALL" );  // ��0���?
        String              ss_uclass   = box.getStringDefault  ( "s_upperclass"    , "ALL" );  // ���з�
        String              ss_mclass   = box.getStringDefault  ( "s_middleclass"   , "ALL" );  // ���з�
        String              ss_lclass   = box.getStringDefault  ( "s_lowerclass"    , "ALL" );  // ���з�
        String              ss_subjcourse=box.getStringDefault  ( "s_subjcourse"    , "ALL" );  // ���?�ڽ�
        String              ss_subjseq  = box.getStringDefault  ( "s_subjseq"       , "ALL" );  // ���?���?
        String              ss_action   = box.getString         ( "s_action"                );
        String              ss_company  = box.getStringDefault  ( "s_company"       , "ALL" );  // ȸ���ڵ�
        
        String              v_orderColumn = box.getString( "p_orderColumn"  );  // d���� �÷���
        String              v_orderType   = box.getString( "p_orderType"    );  // d���� ��
        
        String v_subQry1 = "";
        String v_subQry2 = "";

        try { 
                connMgr = new DBConnectionManager();
                list1   = new ArrayList();

                if ( !ss_company.equals("ALL")) {
                	v_subQry1 = "\n and    comp = " + SQLString.Format(ss_company) + " ";
                	v_subQry2 = "\n and    userid in (select userid from tz_member where comp = " + SQLString.Format(ss_company) + ") ";
                }

                sql1= "\n select get_subjclassnm(a.scupperclass, a.scmiddleclass, a.sclowerclass) as classname "
                    + "\n      , c.grseqnm "
                    + "\n      , a.grseq "
                    + "\n      , a.course "
                    + "\n      , a.cyear "
                    + "\n      , a.courseseq "
                    + "\n      , a.coursenm "
                    + "\n      , a.subj "
                    + "\n      , a.year "
                    + "\n      , a.subjnm "
                    + "\n      , a.subjseq "
                    + "\n      , a.subjseqgr "
                    + "\n      , a.edustart "
                    + "\n      , a.eduend "
                    + "\n      , get_codenm('0004',a.isonoff) as isonoff "
                    + "\n      , ( "
                    + "\n         select count(subj) "
                    + "\n         from   tz_student "
                    + "\n         where  subj = a.subj "
                    + "\n         and    year = a.year "
                    + "\n         and    subjseq = a.subjseq "
                    + v_subQry1
                    + v_subQry2
                	+ "\n        ) as educnt "
                    + "\n      , ( "
                    + "\n         select count(subj) "
                    + "\n         from   tz_stold "
                    + "\n         where  subj = a.subj "
                    + "\n         and    year = a.year "
                    + "\n         and    subjseq = a.subjseq "
                    + "\n         and    isgraduated = 'Y' "
	                + v_subQry1
	                + v_subQry2
	                + "\n        ) as gradcnt1 "
                    + "\n      , ( "
                    + "\n         select count(subj) "
                    + "\n         from   tz_stold "
                    + "\n         where  subj = a.subj "
                    + "\n         and    year = a.year "
                    + "\n         and    subjseq = a.subjseq "
                    + "\n         and    isgraduated = 'N' "
	                + v_subQry1
	                + v_subQry2
                	+ "\n        ) as gradcnt2 "
                    + "\n      , ( "                         
                    + "\n         select nvl(avg(distcode1_avg),0.0) "
                    + "\n         from   tz_suleach "
                    + "\n         where  subj = a.subj "
                    + "\n         and    year = a.year "
                    + "\n         and    subjseq = a.subjseq "
                    + "\n         and    grcode = a.grcode "
                    + "\n         and    grcode <> 'ALL' "
                    + "\n        ) as distcode1_avg "
                    + "\n        ,case when (select count(subj) from tz_student where subj = a.subj and year = a.year and subjseq = a.subjseq) = 0 then 0 " 
                    + "\n              else (select count(subj) from   tz_stold where subj = a.subj and year = a.year and subjseq = a.subjseq and isgraduated = 'Y') "
                    + "                   / (select count(subj) from tz_student where subj = a.subj and year = a.year and subjseq = a.subjseq) * 100 end gradrate "
                    + "\n from   vz_scsubjseq a "
                    + "\n      , tz_grseq c "
                    + "\n      , tz_grsubj d "
                    + "\n where  1=1 "                            
                    + "\n and    a.grcode = c.grcode "                  
                    + "\n and    a.gyear = c.gyear "                   
                    + "\n and    a.grseq = c.grseq "
	                + "\n and    a.grcode = d.grcode "                   
	                + "\n and    a.subj = d.subjcourse ";
                
                if ( !ss_grcode.equals("ALL") ) { 
                	sql1+= "\n and    a.grcode = " +SQLString.Format(ss_grcode);
                }
                
                if ( !ss_gyear.equals("ALL") ) { 
                    sql1+= "\n and    a.gyear = " +SQLString.Format(ss_gyear);
                } 

                if ( !ss_grseq.equals("ALL") ) { 
                    sql1+= "\n and    a.grseq = " +SQLString.Format(ss_grseq);
                } 
                
                if ( !ss_uclass.equals("ALL") ) { 
                    sql1+= "\n and    a.scupperclass = " +SQLString.Format(ss_uclass);
                }
                
                if ( !ss_mclass.equals("ALL") ) { 
                    sql1+= "\n and    a.scmiddleclass = " +SQLString.Format(ss_mclass);
                }
                
                if ( !ss_lclass.equals("ALL") ) { 
                    sql1+= "\n and    a.sclowerclass = " +SQLString.Format(ss_lclass);
                }

                if ( !ss_subjcourse.equals("ALL") ) { 
                    sql1+= "\n and    a.scsubj = " +SQLString.Format(ss_subjcourse);
                }
                
                if ( !ss_subjseq.equals("ALL") ) { 
                    sql1+= "\n and    a.scsubjseq = " +SQLString.Format(ss_subjseq);
                }
                
                if ( v_orderColumn.equals("") ) { 
                    sql1+= "\n order  by a.course, a.cyear, a.courseseq, a.subj, a.year, a.subjseq ";
                } 
                else if(v_orderColumn.equals("notgradcnt")){
                	sql1+= "\n order  by (educnt-gradcnt1) " + v_orderType;
                }
                else {
                    sql1+= "\n order  by " + v_orderColumn + v_orderType;
                }     
                    
                ls1 = connMgr.executeQuery(sql1);
                
                logger.info("��d�� ��0���� : \n" + sql1);
                
                ls1.setPageSize     ( row      );               // �������?row ���� �����Ѵ�
                ls1.setCurrentPage  ( v_pageno );               // �����������ȣ��?�����Ѵ�.
                int total_page_count    = ls1.getTotalPage();   // ��ü ������ �� ��ȯ�Ѵ�
                int total_row_count     = ls1.getTotalCount();  // ��ü row �� ��ȯ�Ѵ�
//System.out.println( "���?: " + sql1 );
                while ( ls1.next() ) { 
                    data1 = new CompleteStatusData();
                    
                    data1.setGrseqNm        ( ls1.getString("grseqnm")      );
                    data1.setClassName      ( ls1.getString("classname")    );
                    data1.setGrseq          ( ls1.getString("grseq")        );
                    data1.setCourse         ( ls1.getString("course")       );
                    data1.setCyear          ( ls1.getString("cyear")        );
                    data1.setCourseseq      ( ls1.getString("courseseq")    );
                    data1.setCoursenm       ( ls1.getString("coursenm")     );
                    data1.setSubj           ( ls1.getString("subj")         );
                    data1.setYear           ( ls1.getString("year")         );
                    data1.setSubjseq        ( ls1.getString("subjseq")      );
                    data1.setSubjseqgr      ( ls1.getString("subjseqgr")    );
                    data1.setSubjnm         ( ls1.getString("subjnm")       );
                    data1.setEdustart       ( ls1.getString("edustart")     );
                    data1.setEduend         ( ls1.getString("eduend")       );
                    data1.setEducnt         ( ls1.getInt("educnt")          );
                    data1.setGradcnt1       ( ls1.getInt("gradcnt1")        );
                    data1.setGradcnt2       ( ls1.getInt("gradcnt2")        );
                    data1.setIsonoff        ( ls1.getString("isonoff")      );
                    data1.setDistcode1avg   ( ls1.getDouble("distcode1_avg")); // ��w��
                    data1.setDispnum        ( total_row_count - ls1.getRowNum() + 1);
                    data1.setTotalPageCount ( total_page_count  );
                    data1.setRowCount       ( row               );

                    list1.add(data1);
                }
                
        } catch ( Exception ex ) { 
        	ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1= " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list1;
    }
    
    /**
     * ��d�� ������?
     * @param box
     * @return
     * @throws Exception
     */ 
    public ArrayList performStatistics_03_02_L(RequestBox box) throws Exception {
        DBConnectionManager connMgr     = null;
        ListSet             ls1         = null;
        ArrayList           list1       = null;        
        String              sql1        = "";
        
        CompleteStatusData  data1       = null;
        
        String              ss_grcode   = box.getStringDefault  ( "s_grcode"        , "ALL" ); // ��0�׷�
        String              ss_gyear    = box.getStringDefault  ( "s_gyear"         , "ALL" );  // �⵵
        String              ss_grseq    = box.getStringDefault  ( "s_grseq"         , "ALL" );  // ��0���?
        String              ss_uclass   = box.getStringDefault  ( "s_upperclass"    , "ALL" );  // ���з�
        String              ss_mclass   = box.getStringDefault  ( "s_middleclass"   , "ALL" );  // ���з�
        String              ss_lclass   = box.getStringDefault  ( "s_lowerclass"    , "ALL" );  // ���з�
        String              ss_subjcourse=box.getStringDefault  ( "s_subjcourse"    , "ALL" );  // ���?�ڽ�
        String              ss_subjseq  = box.getStringDefault  ( "s_subjseq"       , "ALL" );  // ���?���?
        String              ss_action   = box.getString         ( "s_action"                );
        String              ss_company  = box.getStringDefault  ( "s_company"       , "ALL" );  // ȸ���ڵ�

        String              v_orderColumn = box.getString( "p_orderColumn"  );  // d���� �÷���
        String              v_orderType   = box.getString( "p_orderType"    );  // d���� ��
        
        String				v_subQry1 = "";
        String				v_subQry2 = "";
        
        try { 
                connMgr = new DBConnectionManager();
                list1   = new ArrayList();

                if ( !ss_company.equals("ALL")) {
                	v_subQry1 = "\n and    comp = " + SQLString.Format(ss_company) + " ";
                	v_subQry2 = "\n and    userid in (select userid from tz_member where comp = " + SQLString.Format(ss_company) + ") ";
                }

                sql1= "\n select a.subj "
                    + "\n      , a.year "
                    + "\n      , a.subjnm "
                    + "\n      , a.subjseq "
                    + "\n      , get_codenm('0004', a.isonoff) as isonoff "
                    + "\n      , nvl(( "
                    + "\n             select count(subj) "
                    + "\n             from   tz_student "
                    + "\n             where  subj = a.subj "
                    + "\n             and    year = a.year "
                    + "\n             and    subjseq = a.subjseq "
                    + v_subQry1
                    + v_subQry2
                    + "\n            ), 0) educnt "
                    + "\n      , nvl(( "
                    + "\n             select count(subj) "
                    + "\n             from   tz_stold "
                    + "\n             where  subj = a.subj "
                    + "\n             and    year = a.year "
                    + "\n             and    subjseq = a.subjseq "
                    + "\n             and    isgraduated = 'Y' "
                    + v_subQry1
                    + v_subQry2
                    + "\n            ), 0) gradcnt "
                    + "\n      , e.tuserid  "
                    + "\n      , get_name(e.tuserid) tname  "
                    + "\n      , f.to_dt tdate  "
                    + "\n      , f.income tincome  "
                    + "\n  	   , g.account taccount  "
                    + "\n 	   , (SELECT codenm FROM tz_code WHERE gubun = '0102' and code=g.bank) tbank  "
                    + "\n      , (SELECT position_nm FROM tz_member where userid=e.tuserid) tpositionnm  "
                    + "\n      , TO_DATE (SUBSTR (a.eduend, 0, 8), 'YYYYMMDD') - TO_DATE (SUBSTR (a.edustart, 0, 8), 'YYYYMMDD') edudate  "
                    + "\n      , h.muserid cmuserid  "
                    + "\n      , get_name(h.muserid) cmname   "
                    + "\n      , GET_SUBJCLASS_FULLNM(A.SCSUBJCLASS) subjclassnm   "
                    + "\n from   vz_scsubjseq a "
                    + "\n      , tz_grsubj b "
                    + "\n      , tz_classtutor e "
                    + "\n      , tk_edu243t f "
                    + "\n      , tz_tutor g "
                    + "\n      , tz_subjseq h  "
                    + "\n where  1=1 "
	                + "\n and    a.grcode = b.grcode "                   
	                + "\n and    a.subj = b.subjcourse "
	                + "\n  and    a.subj = h.subj  "
	                + "\n  and    a.year = h.year  "
	                + "\n  and    a.subjseq = h.subjseq  "
	                + "\n  and    a.subj = e.subj(+)  "
	                + "\n  and    a.year = e.year(+)  "
	                + "\n  and    a.subjseq = e.subjseq(+)  "
	                + "\n  and    e.class(+) = '0001'  "
	                + "\n  and    e.ttype(+) = 'M'  "
	                + "\n  and    e.subj = f.poi_cd(+)  "
	                + "\n  and    e.year = f.poi_year(+)  "
	                + "\n  and    e.subjseq = f.poi_round(+)  "
	                + "\n  and    e.tuserid = f.profes_id(+)  "
	                + "\n  and    e.tuserid = g.userid(+)  ";
                    

                if ( !ss_grcode.equals("ALL") ) { 
                	sql1+= "\n and    a.grcode = " +SQLString.Format(ss_grcode);
                }

                if ( !ss_gyear.equals("ALL") ) { 
                    sql1+= "\n and    a.gyear = " +SQLString.Format(ss_gyear);
                } 
                
                if ( !ss_grseq.equals("ALL") ) { 
                    sql1+= "\n and    a.grseq = " +SQLString.Format(ss_grseq);
                } 
                
                if ( !ss_uclass.equals("ALL") ) { 
                    sql1+= "\n and    a.scupperclass = " +SQLString.Format(ss_uclass);
                }
                
                if ( !ss_mclass.equals("ALL") ) { 
                    sql1+= "\n and    a.scmiddleclass = " +SQLString.Format(ss_mclass);
                }
                
                if ( !ss_lclass.equals("ALL") ) { 
                    sql1+= "\n and    a.sclowerclass = " +SQLString.Format(ss_lclass);
                }

                if ( !ss_subjcourse.equals("ALL") ) { 
                    sql1+= "\n and    a.scsubj = " +SQLString.Format(ss_subjcourse);
                }
                
                if ( !ss_subjseq.equals("ALL") ) { 
                    sql1+= "\n and    a.scsubjseq = " +SQLString.Format(ss_subjseq);
                }
                
                if ( v_orderColumn.equals("") ) { 
                    sql1+= "\n order  by a.course, a.cyear, a.courseseq, a.subj, a.year, a.subjseq ";
                } else { 
                    sql1+= "\n order  by " + v_orderColumn + v_orderType;
                }                     

                ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
                    data1 = new CompleteStatusData();
                    
                    data1.setSubj           ( ls1.getString("subj")         );
                    data1.setYear           ( ls1.getString("year")         );
                    data1.setSubjseq        ( ls1.getString("subjseq")      );
                    data1.setSubjnm         ( ls1.getString("subjnm")       );
                    data1.setEducnt         ( ls1.getInt("educnt")          );
                    data1.setGradcnt1       ( ls1.getInt("gradcnt")         );
                    data1.setIsonoff        ( ls1.getString("isonoff")      );
                    
                    data1.setTuserid        ( ls1.getString("tuserid")      );
                    data1.setTname          ( ls1.getString("tname")        );
                    data1.setTincome        ( ls1.getInt("tincome")         );
                    data1.setTaccount       ( ls1.getString("taccount")     );
                    data1.setTbank          ( ls1.getString("tbank")     	);
                    data1.setTpositionnm    ( ls1.getString("tpositionnm")  );
                    data1.setCmuserid       ( ls1.getString("cmuserid")     );
                    data1.setCmname         ( ls1.getString("cmname")      	);
                    data1.setEdudate        ( ls1.getInt("edudate")         );
                    data1.setSubjclassnm    ( ls1.getString("subjclassnm")  );
                    
                    list1.add(data1);
                }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list1;
    }
    
    /**
     * ��d��Ȳ
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList performStatistics_03_03_L(RequestBox box) throws Exception { 
        DBConnectionManager    connMgr = null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql1         = "";
        StudyStatusData data1= null;
        
        int     l           = 0;
        String  			ss_grcode   = box.getStringDefault( "s_grcode"		  , "ALL" );  // ��0�׷�
        String              ss_gyear    = box.getStringDefault( "s_gyear"         , "ALL" );  // �⵵
        String              ss_grseq    = box.getStringDefault( "s_grseq"         , "ALL" );  // ��0���?
        String              ss_uclass   = box.getStringDefault( "s_upperclass"    , "ALL" );  // ���з�
        String              ss_mclass   = box.getStringDefault( "s_middleclass"   , "ALL" );  // ���з�
        String              ss_lclass   = box.getStringDefault( "s_lowerclass"    , "ALL" );  // ���з�
        String              ss_subjcourse=box.getStringDefault( "s_subjcourse"    , "ALL" );  // ���?�ڽ�
        String              ss_subjseq  = box.getStringDefault( "s_subjseq"       , "ALL" );  // ���?���?
        String              ss_company  = box.getStringDefault( "s_company"       , "ALL" );  // ȸ��

        String v_orderColumn   = box.getString("p_orderColumn");
        String v_orderType     = box.getString("p_orderType");                 // d���� ��

        String  ss_action   = box.getString("s_action");

        String	v_subQry1	= "";
        int		v_rowspan   = 0;
        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();

                if ( !ss_company.equals("ALL")) {
                	v_subQry1 = "\n and    a.comp = " + SQLString.Format(ss_company) + " ";
                }

                sql1= "\n select c.grseq "
                    + "\n      , d.grseqnm "
                    + "\n      , c.course "
                    + "\n      , c.cyear "
                    + "\n      , c.courseseq "
                    + "\n      , c.coursenm "
                    + "\n      , c.subj "
                    + "\n      , c.year "
                    + "\n      , c.subjseq "
                    + "\n      , c.subjnm "
                    + "\n      , c.subjseqgr "
                    + "\n      , get_codenm('0004', c.isonoff) as isonoff "
                    + "\n      , count(a.subj) as educnt "
                    + "\n      , avg(a.tstep) tstep "
                    + "\n      , avg(a.avtstep) avtstep ";
                
                if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // ����ġ���?
	                sql1+="\n      , sum(a.avreport) report "
	                    + "\n      , sum(a.avmtest) mtest "
	                    + "\n      , sum(a.avftest) ftest "
	                    + "\n      , sum(decode(a.avmtest, 0, 0, 1)) mcnt "
	                    + "\n      , sum(decode(a.avftest, 0, 0, 1)) fcnt "
	                    + "\n      , sum(decode(a.avreport, 0, 0, 1)) rcnt ";
                } else {                                                  // ����ġ�����?
                    sql1+="\n      , sum(a.report) report "
                        + "\n      , sum(a.mtest) mtest "
                        + "\n      , sum(a.ftest) ftest "
	                    + "\n      , sum(decode(a.mtest, 0, 0, 1)) mcnt "
	                    + "\n      , sum(decode(a.ftest, 0, 0, 1)) fcnt "
	                    + "\n      , sum(decode(a.report, 0, 0, 1)) rcnt ";
                }
                
                sql1+="\n      , avg(a.score) score "
                    + "\n      , sum(decode(e.isgraduated, 'Y', 1, 0)) as gradcnt "
                    + "\n from   tz_student a "
                    + "\n      , vz_scsubjseq c "
                    + "\n      , tz_grseq d "
                    + "\n      , tz_stold e "
                    + "\n      , tz_grsubj b "
                    + "\n where  a.subj = c.subj "
                    + "\n and    a.year = c.year "
                    + "\n and    a.subjseq = c.subjseq "
                    + "\n and    a.subj = e.subj(+) "
                    + "\n and    a.year = e.year(+) "
                    + "\n and    a.subjseq = e.subjseq(+) "
                    + "\n and    a.userid = e.userid(+) "
                    + "\n and    c.grcode = d.grcode "
                    + "\n and    c.gyear = d.gyear "
                    + "\n and    c.grseq = d.grseq " 
	                + "\n and    c.grcode = b.grcode "                   
	                + "\n and    c.subj = b.subjcourse "
                    + v_subQry1;
                    
                    if ( !ss_grcode.equals("ALL")) sql1+="\n and    c.grcode = " +SQLString.Format(ss_grcode);
                    if ( !ss_gyear.equals("ALL"))  sql1+="\n and    c.gyear = " +SQLString.Format(ss_gyear);
                    if ( !ss_grseq.equals("ALL"))  sql1+="\n and    c.grseq = " +SQLString.Format(ss_grseq);

                    if ( !ss_uclass.equals("ALL") ) { 
                        sql1+="\n and    c.scupperclass = " +SQLString.Format(ss_uclass);
                    }
                    
                    if ( !ss_mclass.equals("ALL") ) { 
                        sql1+="\n and    c.scmiddleclass = " +SQLString.Format(ss_mclass);
                    }
                    
                    if ( !ss_lclass.equals("ALL") ) { 
                        sql1+="\n and    c.sclowerclass = " +SQLString.Format(ss_lclass);
                    }

                    if ( !ss_subjcourse.equals("ALL") ) { 
                        sql1+="\n and    c.scsubj = " +SQLString.Format(ss_subjcourse);
                    }
                    
                    if ( !ss_subjseq.equals("ALL") ) { 
                        sql1+="\n and    c.scsubjseq = " +SQLString.Format(ss_subjseq);
                    }
                    
                sql1+="\n group  by c.grseq, d.grseqnm, c.course, c.cyear, c.courseseq, c.coursenm "
                    + "\n      , c.subj, c.year, c.subjseq, c.subjnm, c.subjseqgr, c.isonoff ";
                
                if ( v_orderColumn.equals("subj"))     v_orderColumn = "c.subj";
                if ( v_orderColumn.equals("grseq"))    v_orderColumn = "c.grseq";
                if ( v_orderColumn.equals("subjseq"))  v_orderColumn =" c.subjseq";
                if ( v_orderColumn.equals("isonoff"))  v_orderColumn =" c.isonoff ";

                if ( v_orderColumn.equals("") ) { 
                    sql1+="\n order  by c.subj, c.year, c.subjseq, c.subjnm ";
                } else { 
                    sql1+="\n order  by " + v_orderColumn + v_orderType;
                }

                ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
                    data1 = new StudyStatusData();

                    data1.setGrseq    ( ls1.getString("grseq") );
                    data1.setGrseqnm  ( ls1.getString("grseqnm") );
                    data1.setCourse   ( ls1.getString("course") );
                    data1.setCyear    ( ls1.getString("cyear") );
                    data1.setCoursenm ( ls1.getString("coursenm") );
                    data1.setCourseseq( ls1.getString("courseseq") );
                    data1.setSubj     ( ls1.getString("subj") );
                    data1.setYear     ( ls1.getString("year") );
                    data1.setSubjnm   ( ls1.getString("subjnm") );
                    data1.setSubjseq  ( ls1.getString("subjseq") );
                    data1.setSubjseqgr( ls1.getString("subjseqgr") );
                    data1.setIsonoff  ( ls1.getString("isonoff") );
                    data1.setEducnt   ( ls1.getInt("educnt") );
                    data1.setTstep    ( ls1.getInt("tstep") );
                    data1.setAvtstep  ( ls1.getInt("avtstep") );
                    data1.setReport   ( ls1.getInt("report") );
                    data1.setMtest    ( ls1.getInt("mtest") );
                    data1.setFtest    ( ls1.getInt("ftest") );
                    data1.setScore    ( ls1.getInt("score") );
                    data1.setMcnt     ( ls1.getInt("mcnt") );
                    data1.setFcnt     ( ls1.getInt("fcnt") );
                    data1.setRcnt     ( ls1.getInt("rcnt") );
                    data1.setGradcnt  (ls1.getInt("gradcnt") );
                    
                    list1.add(data1);
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }

// �н��� ����м�?
    public ArrayList performStatistics_03_03_L_1(RequestBox box) throws Exception { 
        DBConnectionManager    connMgr = null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql1         = "";
        StudyStatusData data1= null;
        
        int     l           = 0;
        String  			ss_grcode   = box.getStringDefault( "s_grcode"		  , "ALL" );  // ��0�׷�
        String              ss_gyear    = box.getStringDefault( "s_gyear"         , "ALL" );  // �⵵
        String              ss_grseq    = box.getStringDefault( "s_grseq"         , "ALL" );  // ��0���?
        String              ss_uclass   = box.getStringDefault( "s_upperclass"    , "ALL" );  // ���з�
        String              ss_mclass   = box.getStringDefault( "s_middleclass"   , "ALL" );  // ���з�
        String              ss_lclass   = box.getStringDefault( "s_lowerclass"    , "ALL" );  // ���з�
        String              ss_subjcourse=box.getStringDefault( "s_subjcourse"    , "ALL" );  // ���?�ڽ�
        String              ss_subjseq  = box.getStringDefault( "s_subjseq"       , "ALL" );  // ���?���?
        String              ss_company  = box.getStringDefault( "s_company"       , "ALL" );  // ȸ��

        String v_orderColumn   = box.getString("p_orderColumn");
        String v_orderType     = box.getString("p_orderType");                 // d���� ��

        String  ss_action   = box.getString("s_action");

        String	v_subQry1	= "";
        int		v_rowspan   = 0;
        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();

                if ( !ss_company.equals("ALL")) {
                	v_subQry1 = "\n and    b.comp = " + SQLString.Format(ss_company) + " ";
                }

                sql1= "\n   SELECT   "
                	+ "\n   COUNT(*) educnt,  "
                	+ "\n     c.year,c.subj,c.subjseq,c.subjnm,   "
                    + "\n  SUM(DECODE( SUBSTR(birth_date,7,1), '1','1','0' )) MAN_CNT,   "
                    + "\n  SUM(DECODE( SUBSTR(birth_date,7,1), '2','1','0' )) WOMAN_CNT,  "
                    + "\n  SUM(CASE WHEN (to_char(sysdate,'yyyy') - (1900+substr(birth_date, 1, 2)) +    "
                    + "\n  decode(sign(to_char(sysdate, 'mmdd') - substr(birth_date, 3, 4)), 1, 0, -1) ) <= 30   "
                    + "\n    THEN '1' ELSE '' END) AGE30_CNT,  "
                    + "\n   SUM(CASE WHEN (to_char(sysdate,'yyyy') - (1900+substr(birth_date, 1, 2)) +  "
                    + "\n  decode(sign(to_char(sysdate, 'mmdd') - substr(birth_date, 3, 4)), 1, 0, -1) ) BETWEEN  31 AND 40   "
                    + "\n   THEN '1' ELSE '' END) AGE40_CNT,   "
                    + "\n   SUM(CASE WHEN (to_char(sysdate,'yyyy') - (1900+substr(birth_date, 1, 2)) +    "
                    + "\n   decode(sign(to_char(sysdate, 'mmdd') - substr(birth_date, 3, 4)), 1, 0, -1) ) BETWEEN  41 AND 50 "
                    + "\n   THEN '1' ELSE '' END) AGE50_CNT,  "
                    + "\n  SUM(CASE WHEN (to_char(sysdate,'yyyy') - (1900+substr(birth_date, 1, 2)) +   "
                    + "\n  decode(sign(to_char(sysdate, 'mmdd') - substr(birth_date, 3, 4)), 1, 0, -1) ) BETWEEN  51 AND 50     "
                    + "\n  THEN '1' ELSE '' END) AGE60_CNT,    "
                    + "\n SUM(case  when scholar_cd='12' then '1' ELSE '' END) SCHOOL1_CNT,   "
                    + "\n  SUM(case  when scholar_cd between '13' and '15' then '1' ELSE '' END) SCHOOL2_CNT,   "
                    + "\n   SUM(case  when scholar_cd between '16' and '23' then '1' ELSE '' END) SCHOOL3_CNT,   "
                    + "\n  SUM(case  when scholar_cd between '24' and '31' then '1' ELSE '' END) SCHOOL4_CNT,    "
                    + "\n   SUM(case  when scholar_cd<'12' or scholar_cd>'31' then '1' ELSE '' END) SCHOOL5_CNT  "
                    + "\n   FROM TZ_STUDENT A,TZ_MEMBER B,vz_scsubjseq c  "
                    + "\n    WHERE  "
                    + "\n   A.USERID=B.USERID and  "
                    + "\n   a.subj=c.scsubj and  "
                    + "\n   a.year=c.gyear and  "
                    + "\n    a.subjseq=c.scsubjseq  "
                    + v_subQry1;
                    
                    if ( !ss_grcode.equals("ALL")) sql1+="\n and    c.grcode = " +SQLString.Format(ss_grcode);
                    if ( !ss_gyear.equals("ALL"))  sql1+="\n and    c.gyear = " +SQLString.Format(ss_gyear);
                    if ( !ss_grseq.equals("ALL"))  sql1+="\n and    c.grseq = " +SQLString.Format(ss_grseq);

                    if ( !ss_uclass.equals("ALL") ) { 
                        sql1+="\n and    c.scupperclass = " +SQLString.Format(ss_uclass);
                    }
                    
                    if ( !ss_mclass.equals("ALL") ) { 
                        sql1+="\n and    c.scmiddleclass = " +SQLString.Format(ss_mclass);
                    }
                    
                    if ( !ss_lclass.equals("ALL") ) { 
                        sql1+="\n and    c.sclowerclass = " +SQLString.Format(ss_lclass);
                    }

                    if ( !ss_subjcourse.equals("ALL") ) { 
                        sql1+="\n and    c.scsubj = " +SQLString.Format(ss_subjcourse);
                    }
                    
                    if ( !ss_subjseq.equals("ALL") ) { 
                        sql1+="\n and    c.scsubjseq = " +SQLString.Format(ss_subjseq);
                    }
                    
                    sql1+="\n group  by   c.year,c.subj,c.subjseq,c.subjnm " ;
                
                if ( v_orderColumn.equals("subj"))     v_orderColumn = "c.subj";
                if ( v_orderColumn.equals("grseq"))    v_orderColumn = "c.grseq";
                if ( v_orderColumn.equals("subjseq"))  v_orderColumn =" c.subjseq";
                if ( v_orderColumn.equals("isonoff"))  v_orderColumn =" c.isonoff ";

 

                ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
                    data1 = new StudyStatusData();
                    data1.setSubj     ( ls1.getString("subj") );
                    data1.setYear     ( ls1.getString("year") );
                    data1.setSubjnm   ( ls1.getString("subjnm") );
                    data1.setSubjseq  ( ls1.getString("subjseq") );
                    data1.setEducnt   ( ls1.getInt("educnt") );
                    data1.setMan_cnt    ( ls1.getString("man_cnt") );
                    data1.setWoman_cnt  ( ls1.getString("woman_cnt") );
                    data1.setAge30_cnt   ( ls1.getString("age30_cnt") );
                    data1.setAge40_cnt    ( ls1.getString("age40_cnt") );
                    data1.setAge50_cnt   ( ls1.getString("age50_cnt") );
                    data1.setAge60_cnt    ( ls1.getString("age60_cnt") );
                    data1.setSchool1_cnt	( ls1.getString("school1_cnt") );
                    data1.setSchool2_cnt	( ls1.getString("school2_cnt") );
                    data1.setSchool3_cnt	( ls1.getString("school3_cnt") );
                    data1.setSchool4_cnt	( ls1.getString("school4_cnt") );
                    data1.setSchool5_cnt	( ls1.getString("school5_cnt") );
                    
                    list1.add(data1);
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
       

// �н��� ��޺м�?
    public ArrayList performStatistics_03_03_L_2(RequestBox box) throws Exception { 
        DBConnectionManager    connMgr = null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql1         = "";
        StudyStatusData data1= null;
        
        int     l           = 0;
        String  			ss_grcode   = box.getStringDefault( "s_grcode"		  , "ALL" );  // ��0�׷�
        String              ss_gyear    = box.getStringDefault( "s_gyear"         , "ALL" );  // �⵵
        String              ss_grseq    = box.getStringDefault( "s_grseq"         , "ALL" );  // ��0���?
        String              ss_uclass   = box.getStringDefault( "s_upperclass"    , "ALL" );  // ���з�
        String              ss_mclass   = box.getStringDefault( "s_middleclass"   , "ALL" );  // ���з�
        String              ss_lclass   = box.getStringDefault( "s_lowerclass"    , "ALL" );  // ���з�
        String              ss_subjcourse=box.getStringDefault( "s_subjcourse"    , "ALL" );  // ���?�ڽ�
        String              ss_subjseq  = box.getStringDefault( "s_subjseq"       , "ALL" );  // ���?���?
        String              ss_company  = box.getStringDefault( "s_company"       , "ALL" );  // ȸ��

        String v_orderColumn   = box.getString("p_orderColumn");
        String v_orderType     = box.getString("p_orderType");                 // d���� ��

        String  ss_action   = box.getString("s_action");

        String	v_subQry1	= "";
        int		v_rowspan   = 0;
        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();

                if ( !ss_company.equals("ALL")) {
                	v_subQry1 = "\n and    comp = " + SQLString.Format(ss_company) + " ";
                }
                
                sql1= "\n    select a.*, educnt - (title_nm1 + title_nm2 + title_nm3 + title_nm4 + title_nm5 + title_nm6 + title_nm7 ) title_nm8 ,c.subjnm from (     "
                	  + "\n  SELECT   a.year,a.subj,a.subjseq, "
                	  + "\n  sum(decode(title_nm,'���?,1,0)) title_nm1 , "
                	  + "\n   sum(decode(title_nm,'�븮',1,0)) title_nm2 , "
                	  + "\n   sum(decode(title_nm,'����',1,0)) title_nm3 , "
                	  + "\n  sum(decode(title_nm,'����',1,0)) title_nm4 , "
                	  + "\n  sum(decode(title_nm,'����',1,0)) title_nm5 ,  "
                	  + "\n   sum(decode(title_nm,'�󹫺�',1,0)) title_nm6 , "
                	  + "\n  sum(decode(title_nm,'��',1,0)) title_nm7 , "
                	  + "\n   "
                 	  + "\n  COUNT(*) educnt, b.comp   "
                      + "\n  FROM TZ_STUDENT A,TZ_MEMBER B  "
                	  + "\n  WHERE   "
                 	  + "\n  A.USERID=B.USERID "
                	  + "\n  group by a.year,a.subj,a.subjseq, b.comp "
                	  + "\n  ) a ,vz_scsubjseq c   "
                 	  + "\n  where "
                	  + "\n  a.subj=c.subj and  "
                  	  + "\n  a.year=c.year and   "
                  	  + "\n  a.subjseq=c.subjseq    "
                  	  
                  	  
                  	  
                  	  
                      + v_subQry1;
                    if ( !ss_grcode.equals("ALL")) sql1+="\n and    c.grcode = " +SQLString.Format(ss_grcode);
                    if ( !ss_gyear.equals("ALL"))  sql1+="\n and    c.gyear = " +SQLString.Format(ss_gyear);
                    if ( !ss_grseq.equals("ALL"))  sql1+="\n and    c.grseq = " +SQLString.Format(ss_grseq);

                    if ( !ss_uclass.equals("ALL") ) { 
                        sql1+="\n and    c.scupperclass = " +SQLString.Format(ss_uclass);
                    }
                    
                    if ( !ss_mclass.equals("ALL") ) { 
                        sql1+="\n and    c.scmiddleclass = " +SQLString.Format(ss_mclass);
                    }
                    
                    if ( !ss_lclass.equals("ALL") ) { 
                        sql1+="\n and    c.sclowerclass = " +SQLString.Format(ss_lclass);
                    }

                    if ( !ss_subjcourse.equals("ALL") ) { 
                        sql1+="\n and    c.scsubj = " +SQLString.Format(ss_subjcourse);
                    }
                    
                    if ( !ss_subjseq.equals("ALL") ) { 
                        sql1+="\n and    c.scsubjseq = " +SQLString.Format(ss_subjseq);
                    }
                    
                
                if ( v_orderColumn.equals("subj"))     v_orderColumn = "c.subj";
                if ( v_orderColumn.equals("grseq"))    v_orderColumn = "c.grseq";
                if ( v_orderColumn.equals("subjseq"))  v_orderColumn =" c.subjseq";
                if ( v_orderColumn.equals("isonoff"))  v_orderColumn =" c.isonoff ";



                ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
                    data1 = new StudyStatusData();

                    data1.setSubj  ( ls1.getString("subj") );
                    data1.setYear  ( ls1.getString("year") );
                    data1.setSubjseq  ( ls1.getString("subjseq") );
                    data1.setSubjnm  ( ls1.getString("subjnm") );
                         
                    data1.setEducnt   ( ls1.getInt("educnt") );
                    data1.setTitle_nm1   ( ls1.getString("title_nm1") );
                    data1.setTitle_nm2   ( ls1.getString("title_nm2") );
                    data1.setTitle_nm3   ( ls1.getString("title_nm3") );
                    data1.setTitle_nm4   ( ls1.getString("title_nm4") );
                    data1.setTitle_nm5   ( ls1.getString("title_nm5") );
                    data1.setTitle_nm6   ( ls1.getString("title_nm6") );
                    data1.setTitle_nm7   ( ls1.getString("title_nm7") );
                    data1.setTitle_nm8   ( ls1.getString("title_nm8") );
                             
                    list1.add(data1);
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
       

//  �������?
    public ArrayList performStatistics_03_03_L_3(RequestBox box) throws Exception { 
        DBConnectionManager    connMgr = null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql1         = "";
        StudyStatusData data1= null;
        
        int     l           = 0;
        String  			ss_grcode   = box.getStringDefault( "s_grcode"		  , "ALL" );  // ��0�׷�
        String              ss_gyear    = box.getStringDefault( "s_gyear"         , "ALL" );  // �⵵
        String              ss_grseq    = box.getStringDefault( "s_grseq"         , "ALL" );  // ��0���?
        String              ss_uclass   = box.getStringDefault( "s_upperclass"    , "ALL" );  // ���з�
        String              ss_mclass   = box.getStringDefault( "s_middleclass"   , "ALL" );  // ���з�
        String              ss_lclass   = box.getStringDefault( "s_lowerclass"    , "ALL" );  // ���з�
        String              ss_subjcourse=box.getStringDefault( "s_subjcourse"    , "ALL" );  // ���?�ڽ�
        String              ss_subjseq  = box.getStringDefault( "s_subjseq"       , "ALL" );  // ���?���?
        String              ss_company  = box.getStringDefault( "s_company"       , "ALL" );  // ȸ��

        String v_orderColumn   = box.getString("p_orderColumn");
        String v_orderType     = box.getString("p_orderType");                 // d���� ��

        String  ss_action   = box.getString("s_action");

        String	v_subQry1	= "";
        int		v_rowspan   = 0;
        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();

                if ( !ss_company.equals("ALL")) {
                	v_subQry1 = "\n and    comp = " + SQLString.Format(ss_company) + " ";
                }

                
                sql1= "\n    select a.* from (   "
        
                	+ "\n  select   "
                	+ "\n  c.year,   "
                	+ "\n  c.subjnm,   "
                	+ "\n   a.SUBJ,  "
                 	+ "\n  a.SUBJSEQ ,    "
                	+ "\n  c.EDUEND,   "
                 	+ "\n  count(*) a_cnt ,   "
                	+ "\n  round(sum(nvl(a.distcode1_avg , 0))/count(*),2) b_cnt,   "
                 	+ "\n  round(sum(nvl(a.distcode3_avg , 0))/count(*),2) c_cnt,   "
                	+ "\n  round(sum(nvl(a.distcode4_avg , 0))/count(*),2) d_cnt,   "
                 	+ "\n  round(sum(nvl(a.distcode5_avg , 0))/count(*),2) e_cnt,   "
                	+ "\n  round(sum(nvl(a.distcode6_avg , 0))/count(*),2) f_cnt,   "
                 	+ "\n  round((sum(nvl(a.distcode1_avg , 0)) + sum(nvl(a.distcode3_avg , 0)) + sum(nvl(a.distcode4_avg , 0))   "
                	+ "\n  + sum(nvl(a.distcode5_avg , 0))+sum(nvl(a.distcode6_avg , 0)))/5/count(*),2) total_cnt, comp    "
                 	+ "\n  	from   "
                	+ "\n  	tz_suleach a    "
                 	+ "\n   ,tz_member b  "
                 	+ "\n  ,tz_subjseq c    "
                 	+ "\n  	where    "
                 	+ "\n   a.userid = b.userid   "
                 	+ "\n   and a.sulpapernum = 1   "
                 	+ "\n   and a.year=c.year   "
                 	+ "\n   and a.subj=c.subj   "
                 	+ "\n   and a.subjseq=c.subjseq   "
                	+ "\n   group by c.year,c.subjnm,a.SUBJ,a.SUBJSEQ ,c.eduend, comp ) a, vz_scsubjseq c     "
                	+ "\n   	where   "
                	+ "\n    a.subj=c.subj and      "
                	+ "\n    a.year=c.year and     "
                	+ "\n    a.subjseq=c.subjseq    "
                    + v_subQry1;
                    
                    if ( !ss_grcode.equals("ALL")) sql1+="\n and    c.grcode = " +SQLString.Format(ss_grcode);
                    if ( !ss_gyear.equals("ALL"))  sql1+="\n and    c.gyear = " +SQLString.Format(ss_gyear);
                    if ( !ss_grseq.equals("ALL"))  sql1+="\n and    c.grseq = " +SQLString.Format(ss_grseq);

                    if ( !ss_uclass.equals("ALL") ) { 
                        sql1+="\n and    c.scupperclass = " +SQLString.Format(ss_uclass);
                    }
                    
                    if ( !ss_mclass.equals("ALL") ) { 
                        sql1+="\n and    c.scmiddleclass = " +SQLString.Format(ss_mclass);
                    }
                    
                    if ( !ss_lclass.equals("ALL") ) { 
                        sql1+="\n and    c.sclowerclass = " +SQLString.Format(ss_lclass);
                    }

                    if ( !ss_subjcourse.equals("ALL") ) { 
                        sql1+="\n and    c.scsubj = " +SQLString.Format(ss_subjcourse);
                    }
                    
                    if ( !ss_subjseq.equals("ALL") ) { 
                        sql1+="\n and    c.scsubjseq = " +SQLString.Format(ss_subjseq);
                    }
                    
                
                if ( v_orderColumn.equals("subj"))     v_orderColumn = "c.subj";
                if ( v_orderColumn.equals("grseq"))    v_orderColumn = "c.grseq";
                if ( v_orderColumn.equals("subjseq"))  v_orderColumn =" c.subjseq";
                if ( v_orderColumn.equals("isonoff"))  v_orderColumn =" c.isonoff ";

                 ls1 = connMgr.executeQuery(sql1);

                 while ( ls1.next() ) { 
                    data1 = new StudyStatusData();
                    data1.setSubjnm   ( ls1.getString("subjnm") );
                    data1.setSubj   ( ls1.getString("subj") );
                    data1.setSubjseq   ( ls1.getString("subjseq") );
                    data1.setA_cnt   ( ls1.getString("a_cnt") );
                    data1.setB_cnt   ( ls1.getString("b_cnt") );
                    data1.setC_cnt   ( ls1.getString("c_cnt") );
                    data1.setD_cnt   ( ls1.getString("d_cnt") );
                    data1.setE_cnt   ( ls1.getString("e_cnt") );
                    data1.setF_cnt   ( ls1.getString("f_cnt") );
                    data1.setTotal_cnt   ( ls1.getString("total_cnt") );
                    
                    list1.add(data1);
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
       
        
    
    /**
     * ������ ��0����
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList performStatistics_03_04_L(RequestBox box) throws Exception {
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;
        ArrayList           list        = null;        
        String              sql         = "";
        DataBox             dbox        = null;
        
        String              ss_grcode   = box.getStringDefault  ( "s_grcode"        , "ALL" ); 	// ��0�׷�
        String              ss_gyear    = box.getStringDefault  ( "s_gyear"         , "ALL" );  // �⵵
        String              ss_company  = box.getStringDefault  ( "s_company"       , "ALL" );  // ȸ��
        String              ss_action   = box.getString         ( "s_action"                );
        
        String              v_orderColumn = box.getString( "p_orderColumn"  );  // d���� �÷���
        String              v_orderType   = box.getString( "p_orderType"    );  // d���� ��
        
        String v_subQry1 = "";
        
        try { 
            if ( ss_action.equals("go") ) { 
            	connMgr = new DBConnectionManager();
            	list    = new ArrayList();

                if ( !ss_company.equals("ALL")) {
                	v_subQry1 = "\n and    comp = " + SQLString.Format(ss_company) + " ";
                }

                sql = "\n select a.subj "
                    + "\n      , a.year "
                    + "\n      , a.scupperclass as upperclass "
                    + "\n      , max(get_subjclassnm(a.scupperclass,'000','000')) as classname "
                    + "\n      , max(a.subjnm) as subjnm "
                    + "\n      , max(a.isonoff) as isonoff "
                    + "\n      , sum(nvl((  "
                    + "\n                 select count(subj) "
                    + "\n                 from   tz_student "
                    + "\n                 where  subj = a.subj "
                    + "\n                 and    year = a.year "
                    + "\n                 and    subjseq = a.subjseq "
                    + v_subQry1
                    + "\n                ), 0)) educnt "
                    + "\n      , sum(nvl((  "
                    + "\n                 select count(subj) "
                    + "\n                 from   tz_stold "
                    + "\n                 where  subj = a.subj "
                    + "\n                 and    year = a.year "
                    + "\n                 and    subjseq = a.subjseq "
                    + v_subQry1
                    + "\n                ), 0)) gradcnt "
                    + "\n from   vz_scsubjseq a "
                    + "\n      , tz_grsubj b "
                    + "\n where  1=1 "
	                + "\n and    a.grcode = b.grcode "                   
	                + "\n and    a.subj = b.subjcourse ";
                    
                
                if ( !ss_grcode.equals("ALL") ) { 
                    sql+="\n and    a.grcode = " +SQLString.Format(ss_grcode);
                }
                
                if ( !ss_gyear.equals("ALL") ) { 
                	sql+="\n and    a.gyear = " +SQLString.Format(ss_gyear);
                }
                
                sql+="\n group  by a.scupperclass, a.subj, a.year ";
                
                if ( v_orderColumn.equals("") ) { 
                    sql+="\n order  by a.scupperclass, a.subj, a.year ";
                } else { 
                    sql+="\n order  by " + v_orderColumn + v_orderType;
                }      
                
                ls  = connMgr.executeQuery(sql);

                while ( ls.next() ) {
                    dbox = ls.getDataBox();
                    list.add(dbox);
                }
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
     �н���Ȳ ����Ʈ
     @param box      receive from the form object and session
     @return ArrayList
     */
      public ArrayList performStatistics_03_031_L(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr = null;
         ListSet ls1         = null;
         ResultSet rs2       = null;
         ListSet ls3         = null;
         ArrayList list1     = null;
         ArrayList list3     = null;
         PreparedStatement pstmt2 = null;
         String sql1         = "";
         String sql2         = "";
         String sql3         = "";
         StudyStatusData data1= null;
         StudyStatusData data3= null;
         String  v_Bcourse   = ""; // �����ڽ�
         String  v_course    = ""; // �����ڽ�
         String  v_Bcourseseq= ""; // �����ڽ����?
         String  v_courseseq = ""; // �����ڽ����?
         int     l           = 0;
         String  ss_grcode   = box.getStringDefault("s_grcode","ALL");   // ��0�׷�
         String              ss_gyear    = box.getStringDefault  ( "s_gyear"         , "ALL" );  // �⵵
         String              ss_grseq    = box.getStringDefault  ( "s_grseq"         , "ALL" );  // ��0���?
         String              ss_uclass   = box.getStringDefault  ( "s_upperclass"    , "ALL" );  // ���з�
         String              ss_mclass   = box.getStringDefault  ( "s_middleclass"   , "ALL" );  // ���з�
         String              ss_lclass   = box.getStringDefault  ( "s_lowerclass"    , "ALL" );  // ���з�
         String              ss_subjcourse=box.getStringDefault  ( "s_subjcourse"    , "ALL" );  // ���?�ڽ�
         String              ss_subjseq  = box.getStringDefault  ( "s_subjseq"       , "ALL" );  // ���?���?
         String              ss_edustart = box.getStringDefault  ( "s_edustart"      , "ALL" );  // ��0������
         String              ss_eduend   = box.getStringDefault  ( "s_eduend"        , "ALL" );  // ��0~����

         String v_orderColumn   = box.getString("p_orderColumn");
         String v_orderType     = box.getString("p_orderType");                 // d���� ��

         String  ss_action   = box.getString("s_action");

         String v_orgcode = box.getString("p_orgcode");  // v���ڵ�
         
         int     v_rowspan   = 0;
         try { 
             if ( ss_action.equals("go") ) { 
                 connMgr = new DBConnectionManager();
                 list1 = new ArrayList();
                 list3 = new ArrayList();

                 sql1  = "select                                                    \n" +
                         "       C.course,                                          \n" +
                         "       C.cyear,                                           \n" +
                         "       C.courseseq,                                       \n" +
                         "       C.coursenm,                                        \n" +
                         "       C.subj,                                            \n" +
                         "       C.subjseq,                                         \n" +
                         "       C.subjnm,                                          \n" +
                         "       c.subjseqgr,                                       \n";
                 sql1 += "       C.isonoff,                                         \n" +
                         "       count(A.subj) as educnt,                           \n" +
                         "       C.year,                                            \n" +
                         "       avg(A.tstep) tstep,                                \n" +
                         "       sum(decode(a.avmtest, 0, 0, 1)) mcnt,              \n" +   //�߰����?û��
                         "       sum(decode(a.avftest, 0, 0, 1)) fcnt,              \n" +   //��~���?û��
                         "       sum(decode(a.avreport, 0, 0, 1)) rcnt,             \n" +   //��ff���ο�
                         "       avg(A.avtstep) avtstep,                            \n";
                 if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // ����ġ���?
                     sql1 += "       sum(A.avreport) report,                            \n" +
                             "       avg(A.avact) act,                                  \n" +
                             "       sum(A.avmtest) mtest,                              \n" +
                             "       sum(A.avftest) ftest,                              \n";
                 } else {                                                  // ����ġ�����?
                     sql1 += "       sum(A.report) report,                              \n" +
                             "       avg(A.act) act,                                    \n" +
                             "       sum(A.mtest) mtest,                                \n" +
                             "       sum(A.ftest) ftest,                                \n";
                 }
  
                 sql1 += "       avg(A.score) score,                                \n" +
                         "       C.isonoff ,                                        \n" +
                         "       c.grseq ,                                          \n" +
                         "       D.grseqnm                                          \n";
                 sql1 += "  from                                                    \n" +
                         "       TZ_STUDENT A,                                      \n" +
                         "       VZ_SCSUBJSEQ C,                                    \n" +
                         "       tz_grseq D                                         \n";
                 if(!v_orgcode.equals("ALL")){   // v����
                     sql1 += "              ,tz_member\n";
                     sql1 += "              ,vz_zipcode\n";
                     sql1 += "              ,tz_organization\n";
                     sql1 += "              ,tz_organization_mat\n";
                 }
                 sql1 += " where                                                    \n" +
                         "       A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq     \n";
                 if(!v_orgcode.equals("ALL")){   // v����
                     sql1 += "           and A.userid = tz_member.userid\n";
                     sql1 += "           and tz_member.zip_cd = vz_zipcode.zipcode(+)\n";
                     sql1 += "           and vz_zipcode.sido = tz_organization.sido               \n";
                     sql1 += "           and vz_zipcode.gugun = tz_organization_mat.gugun                        \n";
                     sql1 += "           and tz_organization.orgcode = tz_organization_mat.orgcode\n";
                     sql1 += "           and tz_organization.orgcode = "+SQLString.Format(v_orgcode)+"   \n";
                 }
                 sql1 += "   and c.grcode=d.grcode and c.gyear=d.gyear and c.grseq=d.grseq   \n";

                 if ( !ss_grcode.equals("ALL")) sql1 += " and C.grcode = " +SQLString.Format(ss_grcode)+"\n";
                 if ( !ss_gyear.equals("ALL"))  sql1 += " and C.gyear = " +SQLString.Format(ss_gyear)+"\n";
                 if ( !ss_grseq.equals("ALL"))  sql1 += " and C.grseq = " +SQLString.Format(ss_grseq)+"\n";

                 if ( !ss_uclass.equals("ALL") ) { 
                     sql1 += " and c.scupperclass = " +SQLString.Format(ss_uclass);
                 }
                 
                 if ( !ss_mclass.equals("ALL") ) { 
                     sql1 += " and c.scmiddleclass = " +SQLString.Format(ss_mclass);
                 }
                 
                 if ( !ss_lclass.equals("ALL") ) { 
                     sql1 += " and c.sclowerclass = " +SQLString.Format(ss_lclass);
                 }

                 if ( !ss_subjcourse.equals("ALL") ) { 
                     sql1 += " and c.scsubj = " +SQLString.Format(ss_subjcourse);
                 }
                 
                 if ( !ss_subjseq.equals("ALL") ) { 
                     sql1 += " and c.scsubjseq = " +SQLString.Format(ss_subjseq);
                 }
                 
                 sql1 += " group by  C.course,C.cyear,C.courseseq,C.coursenm,C.subj,C.subjseq, \n" +
                         " C.subjnm, c.subjseqgr, C.isonoff,C.year, c.grseq,  D.grseqnm    \n";

                 if ( v_orderColumn.equals("subj"))     v_orderColumn = "c.subj";
                 if ( v_orderColumn.equals("grseq"))    v_orderColumn = "c.grseq";
                 if ( v_orderColumn.equals("subjseq"))  v_orderColumn =" c.subjseqgr ";
                 if ( v_orderColumn.equals("isonoff"))  v_orderColumn =" C.isonoff ";

                 if ( v_orderColumn.equals("") ) { 
                     sql1 += " order by C.subj,C.year,C.subjseq, subjnm";
                 } else { 
                     sql1 += " order by " + v_orderColumn + v_orderType;
                 }

                 System.out.println("\n\n"+sql1);
                 ls1 = connMgr.executeQuery(sql1);

                 // select gradcnt
                 sql2 = "select count(isgraduated) gradcnt ";
                 sql2 += "from TZ_STUDENT ";
                 sql2 += "where subj=? and year=? and subjseq=? and isgraduated='Y' ";
                 pstmt2 = connMgr.prepareStatement(sql2);

                 while ( ls1.next() ) { 
                     data1 = new StudyStatusData();

                     pstmt2.setString(1, ls1.getString("subj") );
                     pstmt2.setString(2, ls1.getString("year") );
                     pstmt2.setString(3, ls1.getString("subjseq") );
                     rs2 = pstmt2.executeQuery();
                     if ( rs2.next() ) { 
                         data1.setGradcnt(rs2.getInt("gradcnt") );
                     }

                     data1.setGrseq( ls1.getString("grseq") );
                     data1.setGrseqnm( ls1.getString("grseqnm") );
                     data1.setCourse( ls1.getString("course") );
                     data1.setCyear( ls1.getString("cyear") );
                     data1.setCoursenm( ls1.getString("coursenm") );
                     data1.setCourseseq( ls1.getString("courseseq") );
                     data1.setSubj( ls1.getString("subj") );
                     data1.setYear( ls1.getString("year") );
                     data1.setSubjnm( ls1.getString("subjnm") );
                     data1.setSubjseq( ls1.getString("subjseq") );
                     data1.setSubjseqgr( ls1.getString("subjseqgr") );
                     data1.setIsonoff( ls1.getString("isonoff") );
                     data1.setEducnt( ls1.getInt("educnt") );
                     data1.setTstep( ls1.getInt("tstep") );
                     data1.setAvtstep( ls1.getInt("avtstep") );
                     data1.setReport( ls1.getInt("report") );
                     data1.setAct( ls1.getInt("act") );
                     data1.setMtest( ls1.getInt("mtest") );
                     data1.setFtest( ls1.getInt("ftest") );
                     data1.setScore( ls1.getInt("score") );
                     data1.setIsonoff( ls1.getString("isonoff") );
                     data1.setMcnt( ls1.getInt("mcnt") );
                     data1.setFcnt( ls1.getInt("fcnt") );
                     data1.setRcnt( ls1.getInt("rcnt") );
                     // data1.setDistcode1avg( ls1.getDouble("distcode1_avg") );
                     
                     list1.add(data1);
                 }
             }
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql1);
             throw new Exception("sql1 = " + sql1 + "\r" + ex.getMessage() );
         } finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
             if ( rs2 != null ) { try { rs2.close(); } catch ( Exception e ) { } }
             if ( ls3 != null ) { try { ls3.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return list1;
	}
    

    /**
     * �оߺ� ��0���� �Ѱ�
     * @param box
     * @return
     * @throws Exception
     */
	public ArrayList performStatistics_04_01_L_01(RequestBox box) throws Exception { 
		DBConnectionManager    connMgr = null;
		ListSet ls         = null;
		ArrayList list     = null;
		DataBox dbox = null;
		String sql         = "";
          
		String  ss_grcode   = box.getStringDefault("s_grcode","ALL");   // ��0�׷�
		String  ss_gyear    = box.getStringDefault("s_gyear","ALL");    // �⵵
		String  ss_company  = box.getStringDefault("s_company","ALL");  // ȸ��
		String  ss_action   = box.getString("s_action");

		String v_subQry1 = "";
		
        try { 
        	if ( ss_action.equals("go") ) { 
        		connMgr = new DBConnectionManager();
        		list = new ArrayList();

                if ( !ss_company.equals("ALL")) {
                	v_subQry1 = "\n and    comp = " + SQLString.Format(ss_company) + " ";
                }

                sql = "\n select a.scupperclass as upperclass "
            	    + "\n      , get_subjclassnm(a.scupperclass,'000','000') as upclassname "
            	    + "\n      , sum(nvl(( "
            	    + "\n                 select count(subj) "
            	    + "\n                 from   tz_student "
            	    + "\n                 where  subj = a.subj "
            	    + "\n                 and    year = a.year "
            	    + "\n                 and    subjseq = a.subjseq "
            	    + v_subQry1
            	    + "\n                ), 0)) educnt "
            	    + "\n      , sum(nvl((  "
            	    + "\n                 select count(subj) "
            	    + "\n                 from   tz_stold "
            	    + "\n                 where  subj = a.subj "
            	    + "\n                 and    year = a.year "
            	    + "\n                 and    subjseq = a.subjseq "
            	    + v_subQry1
            	    + "\n                ), 0)) gradcnt "
            	    + "\n from   vz_scsubjseq a "
            	    + "\n      , tz_grseq c "
            	    + "\n      , tz_grsubj b "
            	    + "\n where  a.grcode = c.grcode(+) "
            	    + "\n and    a.gyear = c.gyear(+) "
            	    + "\n and    a.grseq = c.grseq(+) "
	                + "\n and    a.grcode = b.grcode "                   
	                + "\n and    a.subj = b.subjcourse ";
                
                if ( !ss_grcode.equals("ALL") ) { 
                    sql+="\n and    a.grcode(+) = " +SQLString.Format(ss_grcode);
                }
                
                if ( !ss_gyear.equals("ALL") ) { 
                    sql+="\n and    a.gyear(+) = " +SQLString.Format(ss_gyear);
                }
                  
                sql+="\n group  by a.scupperclass ";
                   
                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                	dbox = ls.getDataBox();
                	list.add(dbox);
                }
        	}
        } catch ( Exception ex ) { 
        	ErrorManager.getErrorStackTrace(ex, box, sql);
        	throw new Exception("sql1 = " + sql + "\r" + ex.getMessage() );
        } finally { 
        	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        	if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
	}

    /**
     * �оߺ� ��0���� ��0���?
     * @param box
     * @return
     * @throws Exception
     */
	public ArrayList performStatistics_04_01_L_02(RequestBox box) throws Exception { 
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		ArrayList list = null;
		DataBox dbox = null;
		String sql = "";
           
		String  ss_grcode   = box.getStringDefault("s_grcode","ALL");   // ��0�׷�
		String  ss_gyear    = box.getStringDefault("s_gyear","ALL");    // �⵵
		String  ss_company  = box.getStringDefault("s_company","ALL");  // ȸ��
		String  ss_action   = box.getString("s_action");

		String v_subQry1 = "";

		try { 
			if ( ss_action.equals("go") ) { 
				connMgr = new DBConnectionManager();
				list = new ArrayList();

                if ( !ss_company.equals("ALL")) {
                	v_subQry1 = "\n and    comp = " + SQLString.Format(ss_company) + " ";
                }

                sql = "\n select c.grcode "
                    + "\n      , c.gyear "
                    + "\n      , c.grseq "
                    + "\n      , c.grseqnm "
                    + "\n      , a.scupperclass as upperclass "
                    + "\n      , get_subjclassnm(a.scupperclass,'000','000') as upclassname "
                    + "\n      , sum(nvl(( "
                    + "\n                 select count(subj) "
                    + "\n                 from   tz_student "
                    + "\n                 where  subj = a.subj "
                    + "\n                 and    year = a.year "
                    + "\n                 and    subjseq = a.subjseq "
                    + v_subQry1
                    + "\n                ), 0)) educnt "
                    + "\n      , sum(nvl(( "
                    + "\n                 select count(subj) "
                    + "\n                 from   tz_stold "
                    + "\n                 where  subj = a.subj "
                    + "\n                 and    year = a.year "
                    + "\n                 and    subjseq = a.subjseq "
                    + v_subQry1
                    + "\n                ), 0)) gradcnt "
                    + "\n from   vz_scsubjseq a "
                    + "\n      , tz_grseq c "
                    + "\n      , tz_grsubj b "
                    + "\n where  a.grcode = c.grcode(+) "
                    + "\n and    a.gyear = c.gyear(+) "
                    + "\n and    a.grseq = c.grseq(+) "
	                + "\n and    a.grcode = b.grcode "                   
	                + "\n and    a.subj = b.subjcourse ";
                if ( !ss_grcode.equals("ALL") ) { 
                    sql += " and a.grcode(+) = " +SQLString.Format(ss_grcode);
                }
                
                if ( !ss_gyear.equals("ALL") ) { 
                    sql += " and a.gyear(+) = " +SQLString.Format(ss_gyear);
                }
                
                sql +="\n group  by c.grcode, c.gyear, c.grseq, c.grseqnm, a.scupperclass ";
                
                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                	dbox = ls.getDataBox();
                	list.add(dbox);
                }
			}
		} catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql1 = " + sql + "\r" + ex.getMessage() );
		} finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}
		return list;
	}

       /**
        * ȸ����Ȳ ����˻�?
        * 
        * @param box
        *            receive from the form object and session
        * @return ArrayList
        */
       public ArrayList performStatistics_03_05_L(RequestBox box) throws Exception {
           DBConnectionManager connMgr = null;
           ListSet ls = null;
           ArrayList list = null;
           DataBox dbox = null;

           String c_sido = box.getString("c_sido");
           String c_gugun = box.getString("c_gugun");
           String c_gender = box.getString("c_gender");
           String c_achievement = box.getString("c_achievement");
           String c_age = box.getString("c_age");
           String v_orgcode = box.getString("p_orgcode");  // v���ڵ�
           
           String              ss_grcode   = box.getStringDefault  ( "s_grcode"        , "ALL" ); // ��0�׷�
           String              ss_gyear    = box.getStringDefault  ( "s_gyear"         , "ALL" );  // �⵵
           String              ss_grseq    = box.getStringDefault  ( "s_grseq"         , "ALL" );  // ��0���?

           String sql = "";

           sql = "";
           sql += "select                                                                         \n";
           sql += "       '1'                                                                         \n";
           if (c_sido.equals("Y")) {  // �õ�����
               sql += "       ,nvl(b.sido, '��Ÿ') sido                                                 \n";
               sql += "       ,count(nvl(b.sido, ' ')) cnt                                            \n";
           }
           if (c_gugun.equals("Y")) {  // ��������
               sql += "      ,nvl(b.gugun, '��Ÿ') gugun\n";
               sql += "       ,count(nvl(b.gugun, ' ')) cnt                                            \n";
           }
           if (c_gender.equals("Y")) {  // ��������
               sql += "      ,decode(nvl(a.gender, ' '), 'F', '����', 'M', '����', '��Ÿ(�ܱ���)') gender \n";
               sql += "       ,count(nvl(a.gender, ' ')) cnt                                            \n";
           }
           if (c_achievement.equals("Y")) {  // �з¼���
               sql += "      ,decode(a.achievement, '', '���?�?, (select codenm from tz_code where gubun='0063' and code = a.achievement)) achievement \n";
               sql += "       ,count(nvl(a.achievement, ' ')) cnt                                            \n";
           }
           if (c_age.equals("Y")) {  // ���ɼ���
               sql += "      ,decode(nvl(f.age, ' '), ' ', '��Ÿ', f.age) age\n";
               sql += "       ,count(nvl(f.age, ' ')) cnt                                            \n";
           }
           sql += "   ,g.subj                                                                     \n";
           sql += "   ,g.year                                                                     \n";
           sql += "   ,g.subjnm                                                                     \n";
           sql += "   ,g.subjseq                                                                     \n";
           sql += "   ,count(nvl(g.subjnm, ' ')) cnt                                                                     \n";
           sql += "   ,sum(nvl((select count(subj) from TZ_STOLD where subj=g.subj and year=g.year and subjseq=g.subjseq and userid = g.userid and isgraduated='Y'), 0)) gradcnt                                                                     \n";
           sql += "  from                                                                         \n";
           sql += "       tz_member a                                                             \n";
           sql += "       ,(select distinct zipcode, sido, gugun from tz_zipcode) b               \n";
           if(!v_orgcode.equals("ALL")){   // v����
               sql += "       ,tz_organization d \n";
               sql += "       ,tz_organization_mat e             \n";
           }
           if (c_age.equals("Y")) {  // ���ɼ���
               sql += "       ,(\n";
               sql += "         select\n";
               sql += "                userid,\n";
               sql += "                case when to_char(sysdate, 'yyyy') - b.byyyy <= 5 then '01~05��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy >  5 and to_char(sysdate, 'yyyy') - b.byyyy <= 10 then '06~10��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 10 and to_char(sysdate, 'yyyy') - b.byyyy <= 15 then '11~15��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 15 and to_char(sysdate, 'yyyy') - b.byyyy <= 20 then '16~20��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 20 and to_char(sysdate, 'yyyy') - b.byyyy <= 25 then '21~25��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 25 and to_char(sysdate, 'yyyy') - b.byyyy <= 30 then '26~30��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 30 and to_char(sysdate, 'yyyy') - b.byyyy <= 35 then '31~35��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 35 and to_char(sysdate, 'yyyy') - b.byyyy <= 40 then '36~40��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 40 and to_char(sysdate, 'yyyy') - b.byyyy <= 45 then '41~45��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 45 and to_char(sysdate, 'yyyy') - b.byyyy <= 50 then '46~50��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 50 and to_char(sysdate, 'yyyy') - b.byyyy <= 55 then '51~55��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 55 and to_char(sysdate, 'yyyy') - b.byyyy <= 60 then '56~60��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 60 and to_char(sysdate, 'yyyy') - b.byyyy <= 65 then '61~65��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 65 and to_char(sysdate, 'yyyy') - b.byyyy <= 70 then '66~70��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 70 and to_char(sysdate, 'yyyy') - b.byyyy <= 75 then '71~75��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 75 and to_char(sysdate, 'yyyy') - b.byyyy <= 80 then '76~80��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 80 and to_char(sysdate, 'yyyy') - b.byyyy <= 85 then '81~85��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 85 and to_char(sysdate, 'yyyy') - b.byyyy <= 90 then '86~90��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 90 and to_char(sysdate, 'yyyy') - b.byyyy <= 95 then '91~95��'\n";
               sql += "                     when to_char(sysdate, 'yyyy') - b.byyyy > 95 and to_char(sysdate, 'yyyy') - b.byyyy <= 100 then '96~100��'\n";
               sql += "                     else ''\n";
               sql += "                 end age\n";
               sql += "           from\n";
               sql += "                (\n";
               sql += "                 select\n";
               sql += "                         userid, \n";
               sql += "                         decode(substr(birth_date, 7, 1), '9', '18'||substr(birth_date, 0, 2)\n";
               sql += "                                                   , '0', '18'||substr(birth_date, 0, 2)\n";
               sql += "                                                   , '1', '19'||substr(birth_date, 0, 2)\n";
               sql += "                                                   , '2', '19'||substr(birth_date, 0, 2)\n";
               sql += "                                                   , '3', '20'||substr(birth_date, 0, 2)\n";
               sql += "                                                   , '4', '20'||substr(birth_date, 0, 2)\n";
               sql += "                                                   , '' ) byyyy\n";
               sql += "                   from\n";
               sql += "                         tz_member\n";
               sql += "                ) b\n";
               sql += "       ) f           \n";
           }           
           sql += "    ,(                                           \n";
           sql += "     select                                      \n";
           sql += "            a.subjnm, a.subj, a.year, a.subjseq,                            \n";
           sql += "            b.userid                             \n";
           sql += "       from                                      \n";
           sql += "            vz_SCSUBJSEQ a,                      \n";
           sql += "            tz_student b                         \n";
           sql += "      where                                      \n";
           sql += "            a.subj = b.subj and a.year = b.year and a.subjseq = b.subjseq\n";
           
           if ( !ss_grcode.equals("ALL") ) { 
               sql += " and a.grcode = " +SQLString.Format(ss_grcode);
           }
           
           if ( !ss_gyear.equals("ALL") ) { 
               sql += " and a.gyear = " +SQLString.Format(ss_gyear);
           }
           
           if ( !ss_grseq.equals("ALL") ) { 
               sql += " and a.grseq = " +SQLString.Format(ss_grseq);
           }
           
           sql += "      group by                                   \n";
           sql += "            a.subjnm, a.subj, a.year, a.subjseq, b.userid                   \n";
           sql += "    ) g                                          \n";
           sql += " where                                                                         \n";
           sql += "       1=1                                                                     \n";
           sql += "   and a.zip_cd = b.zipcode(+)                                    \n";
           sql += "   and a.userid = g.userid                                                       \n";
           

           
           if(!v_orgcode.equals("ALL")){   // v����
               sql += "   and b.sido = d.sido\n";
               sql += "   and b.gugun = e.gugun\n";
               sql += "   and d.orgcode = e.orgcode\n";
               sql += "   and d.orgcode = "+v_orgcode+"                                 \n";
           }
           if (c_age.equals("Y")) {  // ���ɼ���
               sql += "   and a.userid = f.userid                               \n";
           }
           if (!box.getString("p_sdate").equals("") && !box.getString("p_ldate").equals("")) { // �Ⱓ����
               if (box.getString("p_sdate").equals(box.getString("p_ldate"))) {
                   sql += " and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymm') = to_char(to_date('"
                           + box.getString("p_sdate")
                           + "', 'yyyy-mm-dd'), 'yyyymm') \n";
               } else {
                   sql += "   and to_char(to_date(a.indate, 'yyyymmddhh24miss'), 'yyyymm') between to_char(to_date('"
                           + box.getString("p_sdate")
                           + "', 'yyyy-mm-dd'), 'yyyymm') and to_char(to_date('"
                           + box.getString("p_ldate")
                           + "', 'yyyy-mm-dd'), 'yyyymm')                              \n";
               }
           }
           sql += " group by                                                                      \n";
           sql += "       '1'                                                                      \n";
           if (c_sido.equals("Y")) {  // �õ�����
               sql += "      ,b.sido                                                                  \n";
           }
           sql += "      ,g.subj\n";
           sql += "      ,g.year\n";
           sql += "      ,g.subjnm\n";
           sql += "      ,g.subjseq\n";
           if (c_gugun.equals("Y")) {  // ��������
               sql += "      ,b.gugun\n";
           }
           if (c_gender.equals("Y")) {  // ��������
               sql += "      ,a.gender\n";
           }
           if (c_achievement.equals("Y")) {  // �з¼���
               sql += "      ,a.achievement \n";
           }
           if (c_age.equals("Y")) {  // ���ɼ���
               sql += "      ,f.age                               \n";
           }
           if (!box.getString("p_orderStr").equals("")) {
               sql += " order by " + box.getString("p_orderStr") + " "
                       + box.getString("p_orderType") + "        \n";
           } else {
               sql += " order by                                                                      \n";
               sql += "        g.subjnm\n";
               if (c_sido.equals("Y")) {  // �õ�����
                   sql += "       ,b.sido                                                                  \n";
               }
               if (c_gugun.equals("Y")) {  // ��������
                   sql += "      ,b.gugun\n";
               }
               if (c_gender.equals("Y")) {  // ��������
                   sql += "      ,a.gender\n";
               }
               if (c_achievement.equals("Y")) {  // �з¼���
                   sql += "      ,a.achievement \n";
               }
               if (c_age.equals("Y")) {  // ���ɼ���
                   sql += "      ,f.age                               \n";
               }
           }

           try {
               if(box.getString("p_action").equals("GO")){
                   connMgr = new DBConnectionManager();
                   list = new ArrayList();
       
                   System.out.println(sql);
                   ls = connMgr.executeQuery(sql);
       
                   while (ls.next()) {
                       dbox = ls.getDataBox();
                       list.add(dbox);
                   }
               }
           } catch (Exception ex) {
               ErrorManager.getErrorStackTrace(ex, box, sql);
               throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
           } finally {
               if (ls != null) {
                   try {
                       ls.close();
                   } catch (Exception e) {
                   }
               }
               if (connMgr != null) {
                   try {
                       connMgr.freeConnection();
                   } catch (Exception e10) {
                   }
               }
           }

           return list;
       }
    

    /**
     * �׷캰 ��0����
     * @param box
     * @return
     * @throws Exception 
     */
	public ArrayList getStatistics_05_01_L(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        DataBox				dbox	= null;
        String              sql     = "";
        
        String              ss_gyear    = box.getString("s_gyear");  	// �⵵
        String              v_mm    	= box.getString("p_mm");  		// ����
        
        String              v_orderColumn = box.getString( "p_orderColumn"  );  // d���� �÷���
        String              v_orderType   = box.getString( "p_orderType"    );  // d���� ��
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();
            
            sql = "\n select upperclass		/* ��з�?*/"
                + "\n      , middleclass 	/* �ߺз� */"
                + "\n      , lowerclass 	/* �Һз� */"
                + "\n      , subjclassnm 	/* �з��?*/"
                + "\n      , stmonth 		/* ���ۿ� */"
                + "\n      , enmonth 		/* ~���?*/"
                + "\n      , subj 			/* ~���?*/"
                + "\n      , subjnm 		/* ��d�� */"
                + "\n      , subjseq 		/* ���?*/"
                + "\n      , decode(get_cpnm(owner),'-',get_compnm(owner),get_cpnm(owner)) as owner	/* ��/ȸ�� */"
                + "\n      , biyong 		/* ��0�� */"
                + "\n      , rebiyong 		/* ȯ�ޱ� */"
                + "\n      , firstdate 		/* ��~Ȯ���� */"
                + "\n      , getmethod 		/* ������Ȯ�����?*/"
                + "\n      , edutimes 		/* ��?ð�?*/"
                + "\n      , edudays 		/* ��0�Ⱓ */"
                + "\n      , grade 			/* ���?*/"
                + "\n      , tutorfee 		/* �����?*/"
                + "\n      , substr(eduend, 1, 8) as eduend /* ��0~���� */"
                + "\n      , nvl(sum(summary),0) as summary /* ��ü�Ա� */"
                + "\n      , nvl(max(cntn1),0) as cntn1 	/* KT�Ա� */"
                + "\n      , nvl(max(cnty1),0) as cnty1 	/* KT���?*/"
                + "\n      , nvl(max(cntn2),0) as cntn2 	/* �׷���Ա�?*/"
                + "\n      , nvl(max(cnty2),0) as cnty2 	/* �׷����?*/"
                + "\n      , nvl(max(cntn3),0) as cntn3 	/* ���?����Ա�?*/"
                + "\n      , nvl(max(cnty3),0) as cnty3 	/* ���?�����?*/"
                + "\n      , nvl(max(cntn4),0) as cntn4 	/* �?�����Ա� */"
                + "\n      , nvl(max(cnty4),0) as cnty4 	/* �?���ͼ��?*/"
                + "\n      , nvl(max(cntn5),0) as cntn5 	/* ��»��Ա�?*/"
                + "\n      , nvl(max(cnty5),0) as cnty5 	/* ��»���?*/"
                + "\n      , nvl(max(cntn6),0) as cntn6 	/* ��Ÿ�Ա� */"
                + "\n      , nvl(max(cnty6),0) as cnty6 	/* ��Ÿ���?*/"
                + "\n from  ( "
                + "\n        select upperclass "
                + "\n             , middleclass "
                + "\n             , lowerclass "
                + "\n             , subjclassnm "
                + "\n             , stmonth "
                + "\n             , enmonth "
                + "\n             , subj "
                + "\n             , subjnm "
                + "\n             , subjseq "
                + "\n             , owner "
                + "\n             , biyong "
                + "\n             , rebiyong "
                + "\n             , getmethod "
                + "\n             , firstdate "
                + "\n             , edutimes "
                + "\n             , edudays "
                + "\n             , grade "
                + "\n             , tutorfee "
                + "\n             , eduend "
                + "\n             , decode(gubun, 1, sum(cntn)) as cntn1 "
                + "\n             , decode(gubun, 1, sum(cnty)) as cnty1 "
                + "\n             , decode(gubun, 2, sum(cntn)) as cntn2 "
                + "\n             , decode(gubun, 2, sum(cnty)) as cnty2 "
                + "\n             , decode(gubun, 3, sum(cntn)) as cntn3 "
                + "\n             , decode(gubun, 3, sum(cnty)) as cnty3 "
                + "\n             , decode(gubun, 4, sum(cntn)) as cntn4 "
                + "\n             , decode(gubun, 4, sum(cnty)) as cnty4 "
                + "\n             , decode(gubun, 5, sum(cntn)) as cntn5 "
                + "\n             , decode(gubun, 5, sum(cnty)) as cnty5 "
                + "\n             , decode(gubun, 6, sum(cntn)) as cntn6 "
                + "\n             , decode(gubun, 6, sum(cnty)) as cnty6 "
                + "\n             , sum(cntn) as summary "
                + "\n        from  ( "
                + "\n               select a.upperclass "
                + "\n                    , a.middleclass "
                + "\n                    , a.lowerclass "
                + "\n                    , get_subjclassnm(a.upperclass,'000','000') as subjclassnm "
                + "\n                    , substr(b.edustart,5,2) as stmonth "
                + "\n                    , substr(b.eduend,5,2) as enmonth "
                + "\n                    , a.subj "
                + "\n                    , a.subjnm "
                + "\n                    , b.subjseq "
                + "\n                    , a.owner "
                + "\n                    , decode(a.biyong, 0, decode(a.goyongpriceminor,a.goyongpricestand, a.goyongpriceminor),a.biyong) as biyong "
                + "\n                    , a.goyongpricemajor as rebiyong "
                + "\n                    , a.getmethod "
                + "\n                    , a.firstdate "
                + "\n                    , a.edutimes "
                + "\n                    , a.edudays "
                + "\n                    , a.grade "
                + "\n                    , b.eduend "
                + "\n                    , e.tutorfee as tutorfee "
                + "\n                    , (select gubun from tz_compclass where comp = c.comp) as gubun "
                + "\n                    , count(c.userid) as cntn "
                + "\n                    , count(d.userid) as cnty "
                + "\n               from   tz_subj a, tz_subjseq b, tz_student c, tz_stold d "
                + "\n                    ,( "
                + "\n         		         select subj, year, subjseq, sum(nvl(income,0)) as tutorfee "
                + "\n         		         from   tz_tutorfee "
                + "\n         		         group  by subj, year, subjseq "
                + "\n         		        ) e "
                + "\n               where  a.subj = b.subj "
                + "\n               and    b.subj = c.subj "
                + "\n               and    b.year = c.year "
                + "\n               and    b.subjseq = c.subjseq "
                + "\n               and    c.subj = d.subj(+) "
                + "\n               and    c.year = d.year(+) "
                + "\n               and    c.subjseq = d.subjseq(+) "
                + "\n               and    c.userid = d.userid(+) "
                + "\n               and    c.subj = e.subj(+) "
                + "\n               and    c.year = e.year(+) "
                + "\n               and    c.subjseq = e.subjseq(+) "
                + "\n               and    b.year = " + StringManager.makeSQL(ss_gyear)
                + "\n               and    b.eduend like " + StringManager.makeSQL(ss_gyear+v_mm+"%")
                + "\n               and    c.year = " + StringManager.makeSQL(ss_gyear)
                + "\n               group  by a.upperclass "
                + "\n                    , a.middleclass "
                + "\n                    , a.lowerclass "
                + "\n                    , a.subjclass "
                + "\n                    , b.edustart "
                + "\n                    , b.eduend "
                + "\n                    , a.subj "
                + "\n                    , a.subjnm "
                + "\n                    , b.subjseq "
                + "\n                    , a.owner "
                + "\n                    , a.biyong "
                + "\n                    , a.goyongpriceminor "
                + "\n                    , a.goyongpricestand "
                + "\n                    , a.goyongpricemajor "
                + "\n                    , a.getmethod "
                + "\n                    , a.firstdate "
                + "\n                    , a.edutimes "
                + "\n                    , a.edudays "
                + "\n                    , a.grade "
                + "\n                    , c.comp "
                + "\n                    , b.eduend "
                + "\n                    , e.tutorfee "
                + "\n              ) "
                + "\n        group  by upperclass "
                + "\n                , middleclass "
                + "\n                , lowerclass "
                + "\n                , subjclassnm "
                + "\n                , stmonth "
                + "\n                , enmonth "
                + "\n                , subj "
                + "\n                , subjnm "
                + "\n                , subjseq "
                + "\n                , owner "
                + "\n                , biyong "
                + "\n                , rebiyong "
                + "\n                , getmethod "
                + "\n                , firstdate "
                + "\n                , edutimes "
                + "\n                , edudays "
                + "\n                , grade "
                + "\n                , tutorfee "
                + "\n                , gubun "
                + "\n                , eduend "
                + "\n       ) "
                + "\n group  by upperclass "
                + "\n 	   , middleclass "
                + "\n 	   , lowerclass "
                + "\n 	   , stmonth "
                + "\n 	   , enmonth "
                + "\n 	   , subj "
                + "\n 	   , subjnm "
                + "\n 	   , subjseq "
                + "\n 	   , owner "
                + "\n 	   , biyong "
                + "\n 	   , rebiyong "
                + "\n 	   , getmethod "
                + "\n 	   , firstdate "
                + "\n 	   , edutimes "
                + "\n 	   , edudays "
                + "\n 	   , grade "
                + "\n 	   , tutorfee "
                + "\n 	   , eduend "
                + "\n 	   , subjclassnm ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
                
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql= " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
	}
	
    /**
     * �� �̷��� ��d� ����1
     * @param box
     * @return
     * @throws Exception 
     */
	public DataBox getStatistics_06_01_L_01(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        DataBox				dbox	= null;
        String              sql     = "";
        
        String              v_eduendfrom = box.getString("s_endfrom");
        String              v_eduendto   = box.getString("s_endto");
        
        try {
            connMgr = new DBConnectionManager();
            
            sql  = "SELECT *  \n";
            sql += "FROM  (SELECT SUM(DECODE(B.CP, NULL, 1, '', 1, 0)) AS SELF  \n";
            sql += "            , SUM(DECODE(B.CP, NULL, 0, '', 0, 1)) AS CP  \n";
            sql += "            , COUNT(*) AS TOTAL  \n";
            sql += "       FROM   TZ_SUBJSEQ A  \n";
            sql += "            , TZ_SUBJ B  \n";
            sql += "       WHERE  A.SUBJ = B.SUBJ  \n";
            sql += "       AND    A.ISCLOSED = 'Y'  \n";
            sql += "       AND    B.ISONOFF = 'ON'  \n";
            sql += "       AND    SUBSTR(EDUEND, 1, 8) BETWEEN " + StringManager.makeSQL(v_eduendfrom) + " AND " + StringManager.makeSQL(v_eduendto) + "  \n";
            sql += "      ) T1  \n";
            sql += "     ,(SELECT TRIM(TO_CHAR(COUNT(*), '999,999,999')) AS TOTALCNT  \n";
            sql += "            , TRIM(TO_CHAR(SUM(DECODE(B.ISGRADUATED, 'Y', 1, 0)), '999,999,999')) AS GRADUCNT  \n";
            sql += "            , ROUND(SUM(DECODE(B.ISGRADUATED, 'Y', 1, 0)) / COUNT(*) * 100, 2) AS RATE  \n";
            sql += "       FROM   TZ_SUBJSEQ A  \n";
            sql += "            , TZ_STOLD B  \n";
            sql += "            , TZ_SUBJ C  \n";
            sql += "       WHERE  A.SUBJ = B.SUBJ  \n";
            sql += "       AND    A.YEAR = B.YEAR  \n";
            sql += "       AND    A.SUBJSEQ = B.SUBJSEQ  \n";
            sql += "       AND    A.SUBJ = C.SUBJ  \n";
            sql += "       AND    A.ISCLOSED = 'Y'  \n";
            sql += "       AND    C.ISONOFF = 'ON'  \n";
            sql += "       AND    SUBSTR(A.EDUEND, 1, 8) BETWEEN " + StringManager.makeSQL(v_eduendfrom) + " AND " + StringManager.makeSQL(v_eduendto) + "  \n";
            sql += "      ) T2  \n";
            ls = connMgr.executeQuery(sql);

            if(ls.next()) { 
                dbox = ls.getDataBox();
            } else {
            	dbox = new DataBox("");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql= " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return dbox;
	}
	
    /**
     * �� �̷��� ��d� ����2
     * @param box
     * @return
     * @throws Exception 
     */
	public ArrayList getStatistics_06_01_L_02(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList			list	= null;
        DataBox				dbox	= null;
        String              sql     = "";
        
        String              v_eduendfrom = box.getString("s_endfrom");
        String              v_eduendto   = box.getString("s_endto");
        
        try {
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = "SELECT T1.SUBJCLASS  \n";
            sql += "     , T1.CLASSNAME  \n";
            sql += "     , NVL(T2.CNT, 0) AS CNT  \n";
            sql += "FROM   TZ_SUBJATT T1  \n";
            sql += "     ,(SELECT UPPERCLASS  \n";
            sql += "            , COUNT(*) AS CNT  \n";
            sql += "       FROM   TZ_SUBJSEQ A  \n";
            sql += "            , TZ_SUBJ B  \n";
            sql += "            , TZ_CLASSTUTOR C  \n";
            sql += "       WHERE  A.SUBJ = B.SUBJ  \n";
            sql += "       AND    A.SUBJ = C.SUBJ  \n";
            sql += "       AND    A.YEAR = C.YEAR  \n";
            sql += "       AND    A.SUBJSEQ = C.SUBJSEQ  \n";
            sql += "       AND    C.CLASS = '0001'  \n";
            sql += "       AND    B.ISONOFF = 'ON'  \n";
            sql += "       AND    A.ISCLOSED = 'Y'  \n";
            sql += "       AND    SUBSTR(A.EDUEND, 1, 8) BETWEEN " + StringManager.makeSQL(v_eduendfrom) + " AND " + StringManager.makeSQL(v_eduendto) + "  \n";
            sql += "       GROUP BY  \n";
            sql += "              UPPERCLASS  \n";
            sql += "      ) T2  \n";
            sql += "WHERE  T1.UPPERCLASS = T2.UPPERCLASS (+)  \n";
            sql += "AND    T1.MIDDLECLASS = '000'  \n";
            sql += "AND    T1.LOWERCLASS = '000'  \n";
            ls = connMgr.executeQuery(sql);

            while(ls.next()) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql= " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list;
	}
	
    /**
     * �� �̷��� ��d� ����3
     * @param box
     * @return
     * @throws Exception 
     */
	public DataBox getStatistics_06_01_L_03(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        DataBox				dbox	= null;
        String              sql     = "";
        
        String              v_eduendfrom = box.getString("s_endfrom");
        String              v_eduendto   = box.getString("s_endto");
        
        try {
            connMgr = new DBConnectionManager();

            sql  = "SELECT ROUND(NVL(AVG(C.LOGCNT), 0), 2) AS LOGCNT  \n";
            sql += "     , NVL(SUM(D.GONGCNT), 0) AS GONGCNT  \n";
            sql += "     , NVL(SUM(E.PDSCNT), 0) AS PDSCNT  \n";
            sql += "     , NVL(SUM(F.TORONTPCNT), 0) AS TORONTPCNT  \n";
            sql += "     , NVL(SUM(G.TORONCNT), 0) AS TORONCNT  \n";
            sql += "     , ROUND(NVL(AVG(H.PROJCNT), 0), 2) AS PROJCNT  \n";
            sql += "     , ROUND(NVL(AVG(I.QUESTCNT), 0), 2) AS QUESTCNT  \n";
            sql += "     , ROUND(NVL(AVG(I.NOANSCNT), 0), 2) AS NOANSCNT  \n";
            sql += "FROM   TZ_SUBJSEQ A  \n";
            sql += "     , TZ_CLASSTUTOR B  \n";
            sql += "     ,(SELECT TUSERID  \n";
            sql += "            , COUNT(*) AS LOGCNT  \n";
            sql += "       FROM   TZ_TUTORLOG  \n";
            sql += "       WHERE  SUBSTR(LOGIN, 1, 8) BETWEEN " + StringManager.makeSQL(v_eduendfrom) + " AND " + StringManager.makeSQL(v_eduendto) + "  \n";
            sql += "       GROUP BY TUSERID  \n";
            sql += "      ) C  \n";
            sql += "     ,(SELECT A.SUBJ  \n";
            sql += "            , A.YEAR  \n";
            sql += "            , A.SUBJSEQ  \n";
            sql += "            , B.USERID  \n";
            sql += "            , COUNT(*) AS GONGCNT  \n";
            sql += "       FROM   TZ_SUBJSEQ A  \n";
            sql += "            , TZ_GONG B  \n";
            sql += "       WHERE  A.SUBJ = B.SUBJ  \n";
            sql += "       AND    A.YEAR = B.YEAR  \n";
            sql += "       AND    A.SUBJSEQ = B.SUBJSEQ  \n";
            sql += "       GROUP BY  \n";
            sql += "              A.SUBJ  \n";
            sql += "            , A.YEAR  \n";
            sql += "            , A.SUBJSEQ  \n";
            sql += "            , B.USERID  \n";
            sql += "      ) D  \n";
            sql += "     ,(SELECT A.SUBJ  \n";
            sql += "            , B.USERID  \n";
            sql += "            , COUNT(*) AS PDSCNT  \n";
            sql += "       FROM   TZ_BDS A  \n";
            sql += "            , TZ_BOARD B  \n";
            sql += "       WHERE  A.TABSEQ = B.TABSEQ  \n";
            sql += "       AND    A.TYPE = 'SD'  \n";
            sql += "       AND    SUBSTR(B.INDATE, 1, 8) BETWEEN " + StringManager.makeSQL(v_eduendfrom) + " AND " + StringManager.makeSQL(v_eduendto) + "  \n";
            sql += "       GROUP BY  \n";
            sql += "              A.SUBJ  \n";
            sql += "            , B.USERID  \n";
            sql += "      ) E  \n";
            sql += "     ,(SELECT SUBJ  \n";
            sql += "            , YEAR  \n";
            sql += "            , SUBJSEQ  \n";
            sql += "            , ADUSERID  \n";
            sql += "            , COUNT(*) AS TORONTPCNT  \n";
            sql += "       FROM   TZ_TORONTP  \n";
            sql += "       GROUP BY  \n";
            sql += "              SUBJ  \n";
            sql += "            , YEAR  \n";
            sql += "            , SUBJSEQ  \n";
            sql += "            , ADUSERID  \n";
            sql += "      ) F  \n";
            sql += "     ,(SELECT SUBJ  \n";
            sql += "            , YEAR  \n";
            sql += "            , SUBJSEQ  \n";
            sql += "            , ADUSERID  \n";
            sql += "            , COUNT(*) AS TORONCNT  \n";
            sql += "       FROM   TZ_TORON  \n";
            sql += "       GROUP BY  \n";
            sql += "              SUBJ  \n";
            sql += "            , YEAR  \n";
            sql += "            , SUBJSEQ  \n";
            sql += "            , ADUSERID  \n";
            sql += "      ) G  \n";
            sql += "     ,(SELECT SUBJ  \n";
            sql += "            , YEAR  \n";
            sql += "            , SUBJSEQ  \n";
            sql += "            , COUNT(*) AS PROJCNT  \n";
            sql += "       FROM   TZ_PROJASSIGN  \n";
            sql += "       WHERE (TOTALSCORE IS NOT NULL OR TOTALSCORE != '')  \n";
            sql += "       GROUP BY  \n";
            sql += "              SUBJ  \n";
            sql += "            , YEAR  \n";
            sql += "            , SUBJSEQ  \n";
            sql += "      ) H  \n";
            sql += "     ,(SELECT A.SUBJ  \n";
            sql += "            , B.YEAR  \n";
            sql += "            , B.SUBJSEQ  \n";
            sql += "            , SUM(DECODE(LEVELS, 1, 1, 0)) AS QUESTCNT  \n";
            sql += "            , SUM(DECODE(LEVELS, 2, 1, 0)) AS ANSCNT  \n";
            sql += "            , SUM(DECODE(LEVELS, 1, 1, 0)) - SUM(DECODE(LEVELS, 2, 1, 0)) AS NOANSCNT  \n";
            sql += "       FROM   TZ_BDS A  \n";
            sql += "            , TZ_BOARD B  \n";
            sql += "       WHERE  A.TABSEQ = B.TABSEQ  \n";
            sql += "       AND    A.TYPE = 'SQ'  \n";
            sql += "       AND    SUBSTR(B.INDATE, 1, 8) BETWEEN " + StringManager.makeSQL(v_eduendfrom) + " AND " + StringManager.makeSQL(v_eduendto) + "  \n";
            sql += "       GROUP BY  \n";
            sql += "              A.SUBJ  \n";
            sql += "            , B.YEAR  \n";
            sql += "            , B.SUBJSEQ  \n";
            sql += "      ) I  \n";
            sql += "     , TZ_SUBJ J  \n";
            sql += "WHERE  A.SUBJ = B.SUBJ  \n";
            sql += "AND    A.YEAR = B.YEAR  \n";
            sql += "AND    A.SUBJSEQ = B.SUBJSEQ  \n";
            sql += "AND    B.TUSERID = C.TUSERID (+)  \n";
            sql += "AND    B.SUBJ = D.SUBJ (+)  \n";
            sql += "AND    B.YEAR = D.YEAR (+)  \n";
            sql += "AND    B.SUBJSEQ = D.SUBJSEQ (+)  \n";
            sql += "AND    B.TUSERID = D.USERID (+)  \n";
            sql += "AND    B.SUBJ = E.SUBJ (+)  \n";
            sql += "AND    B.TUSERID = E.USERID (+)  \n";
            sql += "AND    B.SUBJ = F.SUBJ (+)  \n";
            sql += "AND    B.YEAR = F.YEAR (+)  \n";
            sql += "AND    B.SUBJSEQ = F.SUBJSEQ (+)  \n";
            sql += "AND    B.TUSERID = F.ADUSERID (+)  \n";
            sql += "AND    B.SUBJ = G.SUBJ (+)  \n";
            sql += "AND    B.YEAR = G.YEAR (+)  \n";
            sql += "AND    B.SUBJSEQ = G.SUBJSEQ (+)  \n";
            sql += "AND    B.TUSERID = G.ADUSERID (+)  \n";
            sql += "AND    B.SUBJ = H.SUBJ (+)  \n";
            sql += "AND    B.YEAR = H.YEAR (+)  \n";
            sql += "AND    B.SUBJSEQ = H.SUBJSEQ (+)  \n";
            sql += "AND    B.SUBJ = I.SUBJ (+)  \n";
            sql += "AND    B.YEAR = I.YEAR (+)  \n";
            sql += "AND    B.SUBJSEQ = I.SUBJSEQ (+)  \n";
            sql += "AND    B.SUBJ = J.SUBJ  \n";
            sql += "AND    A.ISCLOSED = 'Y'  \n";
            sql += "AND    J.ISONOFF = 'ON'  \n";
            sql += "AND    SUBSTR(A.EDUEND, 1, 8) BETWEEN " + StringManager.makeSQL(v_eduendfrom) + " AND " + StringManager.makeSQL(v_eduendto) + "  \n";
            ls = connMgr.executeQuery(sql);

            if(ls.next()) { 
                dbox = ls.getDataBox();
            } else {
            	dbox = new DataBox("");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql= " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return dbox;
	}
	
    /**
     * �� �̷��� ��d� ����4
     * @param box
     * @return
     * @throws Exception 
     */
	public DataBox getStatistics_06_01_L_04(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        DataBox				dbox	= null;
        String              sql     = "";
        
        String              v_eduendfrom = box.getString("s_endfrom");
        String              v_eduendto   = box.getString("s_endto");
        
        try {
            connMgr = new DBConnectionManager();
            
            sql  = "SELECT ROUND(NVL(AVG(B.DISTCODE1_AVG), 0), 2) AS STUDY  \n";
            sql += "     , ROUND(NVL(AVG(B.DISTCODE3_AVG), 0), 2) AS SUBJECT  \n";
            sql += "     , ROUND(NVL(AVG(B.DISTCODE4_AVG), 0), 2) AS SUBJRUN  \n";
            sql += "     , ROUND(NVL(AVG(B.DISTCODE5_AVG), 0), 2) AS TUTOR  \n";
            sql += "     , ROUND(NVL(AVG(B.DISTCODE6_AVG), 0), 2) AS SYSTEM  \n";
            sql += "     , ROUND(NVL((AVG(B.DISTCODE1_AVG) + AVG(B.DISTCODE3_AVG) + AVG(B.DISTCODE4_AVG) + AVG(B.DISTCODE5_AVG) + AVG(B.DISTCODE6_AVG)) / 5, 0), 2) AS TOTAL  \n";
            sql += "FROM   TZ_SUBJSEQ A  \n";
            sql += "     , TZ_SULEACH B  \n";
            sql += "     , TZ_SUBJ C  \n";
            sql += "WHERE  A.SUBJ = B.SUBJ  \n";
            sql += "AND    A.YEAR = B.YEAR  \n";
            sql += "AND    A.SUBJSEQ = B.SUBJSEQ  \n";
            sql += "AND    A.SUBJ = C.SUBJ  \n";
            sql += "AND    A.ISCLOSED = 'Y'  \n";
            sql += "AND    C.ISONOFF = 'ON'  \n";
            sql += "AND    SUBSTR(A.EDUEND, 1, 8) BETWEEN " + StringManager.makeSQL(v_eduendfrom) + " AND " + StringManager.makeSQL(v_eduendto) + "  \n";
            ls = connMgr.executeQuery(sql);

            if(ls.next()) { 
                dbox = ls.getDataBox();
            } else {
            	dbox = new DataBox("");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql= " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return dbox;
	}
	
	
    /**
     * �׷캰 ��0����
     * @param box
     * @return
     * @throws Exception 
     */
	public ArrayList getStatistics_07_01_L(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        DataBox				dbox	= null;
        String              sql     = "";
        
        String              v_isonoff   = box.getString("p_isonoff");  	// ��0����
        String              v_startdt	= box.getString("p_startdt").replace("-", "");  // ��0�Ⱓ ������
        String              v_enddt     = box.getString("p_enddt").replace("-", "");  // ��0�Ⱓ ������
        
        try { 
            connMgr = new DBConnectionManager();
            list   = new ArrayList();
            

            sql = "\n select  a1.grcode,a1.grcodenm,a1.gyear,a1.grseq,a1.grseqnm,a1.isonoff,a1.isonoffnm,a1.comp,a1.compnm,a1.subjcnt,a1.stucnt,a1.stuamt,a2.cancnt from " 
            	+ "\n ( select a.grcode, c.grcodenm, a.gyear, a.grseq, d.grseqnm, b.isonoff, get_codenm('0004',b.isonoff) isonoffnm, e.comp, get_compnm(e.comp) compnm, count(a.subjseq) subjcnt, sum(e.stucnt) stucnt , sum(nvl(e.stuamt,0)) stuamt "
            	+ "\n     from tz_subjseq a, tz_subj b, tz_grcode c, tz_grseq d, "
            	+ "\n      ( select x.subj,x.year,x.subjseq, y.comp, count(y.userid) stucnt, ( x.biyong * nvl(count(y.userid),0)) stuamt "
            	+ "\n          from tz_subjseq x, tz_student y "
            	+ "\n         where x.subj=y.subj and x.year=y.year and x.subjseq=y.subjseq "
            	+ "\n         group by x.subj,x.year,x.subjseq,y.comp,x.biyong "
            	+ "\n      )e "
            	+ "\n   where a.subj=b.subj"
            	+ "\n     and a.grcode=c.grcode"
            	+ "\n     and a.grcode=d.grcode"
            	+ "\n     and a.grseq=d.grseq"
            	+ "\n     and a.subj=e.subj"
            	+ "\n     and a.year=e.year"
            	+ "\n     and a.subjseq=e.subjseq"
            	+ "\n     and b.isuse ='Y'"
            	+ "\n     and a.isvisible ='Y'";
        	
        	if(!v_isonoff.equals("ALL")){
        	sql += "\n   and b.isonoff = '"+v_isonoff+"'";
        	}
        	
        	if(!v_startdt.equals("")&&!v_enddt.equals("")){
        	sql += "\n   and substr(a.edustart,0,8) between '"+v_startdt+"' and '"+v_enddt+"'";
        	sql += "\n   and substr(a.eduend,0,8) between '"+v_startdt+"' and '"+v_enddt+"'";
        	}
        	
        	sql += "\n   group by a.grcode, c.grcodenm, a.gyear, a.grseq, d.grseqnm, b.isonoff, e.comp ) a1,";
        		//+  "\n   order by grcode, grseq, compnm) a1 , ";
        	
        	sql += "" 
        	+ "\n ( select a.grcode, c.grcodenm, a.gyear, a.grseq, d.grseqnm, b.isonoff, get_codenm('0004',b.isonoff) isonoffnm, e.comp, get_compnm(e.comp) compnm, sum(cancnt) cancnt"
        	+ "\n     from tz_subjseq a, tz_subj b, tz_grcode c, tz_grseq d, "
        	+ "\n      ( select x.subj,x.year,x.subjseq,z.comp,count(y.userid) cancnt "
        	+ "\n          from tz_subjseq x, (select subj, year,subjseq, userid from tz_cancel y1 group by subj, year,subjseq, userid ) y, tz_member z "
        	+ "\n         where x.subj=y.subj and x.year=y.year and x.subjseq=y.subjseq and y.userid=z.userid"
        	+ "\n         group by x.subj,x.year,x.subjseq,z.comp"
        	+ "\n      )e "
        	+ "\n   where a.subj=b.subj"
        	+ "\n     and a.grcode=c.grcode"
        	+ "\n     and a.grcode=d.grcode"
        	+ "\n     and a.grseq=d.grseq"
        	+ "\n     and a.subj=e.subj"
        	+ "\n     and a.year=e.year"
        	+ "\n     and a.subjseq=e.subjseq"
        	+ "\n     and b.isuse ='Y'"
        	+ "\n     and a.isvisible ='Y'";
    	
    	if(!v_isonoff.equals("ALL")){
    	sql += "\n   and b.isonoff = '"+v_isonoff+"'";
    	}
    	
    	if(!v_startdt.equals("")&&!v_enddt.equals("")){
    	sql += "\n   and substr(a.edustart,0,8) between '"+v_startdt+"' and '"+v_enddt+"'";
    	sql += "\n   and substr(a.eduend,0,8) between '"+v_startdt+"' and '"+v_enddt+"'";
    	}
    	
    	sql += "\n   group by a.grcode, c.grcodenm, a.gyear, a.grseq, d.grseqnm, b.isonoff, e.comp ) a2";
    	
    	sql += "\n   where a1.grcode=a2.grcode(+)"
    		+ "\n      and a1.grcode=a2.grcode(+)"
    		+ "\n      and a1.grseq=a2.grseq(+)"
    		+ "\n      and a1.isonoff=a2.isonoff(+)"
    		+ "\n      and a1.comp=a2.comp(+)"    		
    		+ "\n   order by a1.grcode, a1.grseq, a1.compnm ";
    	
//System.out.println("sql=="+sql);        	
            ls = connMgr.executeQuery(sql);            

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
                
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql= " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        
        return list;
	}	
	
	
    /**
    ����?���θ���Ʈ vȸ
    @param box          receive from the form object and session
    @return ArrayList   ����?���δ��?���?����Ʈ
    */
    public ArrayList getStatistics_07_01_P(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls 		= null;
        ArrayList list 	= null;
        DataBox	dbox	= null;
        String sql  = "";

        String p_grcode     = box.getString("s_grcode");
        String p_gyear      = box.getString("s_gyear");
        String p_grseq      = box.getString("s_grseq");
        String p_company  	= box.getString("s_company");
        String p_isonoff 	= box.getString("s_isonoff");        
        String p_appstatus  = box.getString("p_appstatus");

        String              v_startdt	= box.getString("p_startdt").replace("-", "");  // ��0�Ⱓ ������
        String              v_enddt     = box.getString("p_enddt").replace("-", "");  // ��0�Ⱓ ������
        
        String v_order      = box.getString("p_order");
        String v_orderType  = box.getString("p_orderType");                 // d���� ��

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            
            if (p_appstatus.equals("Y")) {             
	            sql = "\n select a.scsubj "
	            	+ "\n      , a.scyear "
	            	+ "\n      , a.scsubjseq "
	            	+ "\n      , a.scsubjnm "
	            	+ "\n      , a.edustart "
	            	+ "\n      , b.userid "
	            	+ "\n      , b.comp "
	            	+ "\n      , c.name "
	            	+ "\n      , get_compnm(b.comp) as companynm "
	            	+ "\n      , c.lvl_nm as jikwinm "                 
	            	+ "\n      , c.position_nm  as deptnm "            	
	            	+ "\n      , a.biyong "                                    
	            	+ "\n      , a.isgoyong "
	            	+ "\n      , a.goyongpricemajor "
	            	+ "\n      , a.goyongpriceminor "
	            	+ "\n      , a.goyongpricestand "
	            	+ "\n      , get_codenm('0004', a.isonoff) as isonoff "
	            	+ "\n from   tz_student b "
	            	+ "\n      , vz_scsubjseq a "
	            	+ "\n      , tz_member c "
	            	+ "\n where  b.subj         = a.subj " 
	            	+ "\n and    b.year         = a.year "
	            	+ "\n and    b.subjseq      = a.subjseq "
	            	+ "\n and    b.userid       = c.userid "
	            	+ "\n and    a.grcode       = "+ SQLString.Format(p_grcode)
	            	+ "\n and    a.gyear        = "+ SQLString.Format(p_gyear)
	            	+ "\n and    a.grseq        = "+ SQLString.Format(p_grseq)
	            	+ "\n and    b.comp 		= "+ StringManager.makeSQL(p_company)            
	            	+ "\n and    a.isonoff 		= "+ StringManager.makeSQL(p_isonoff);
	            
		        	if(!v_startdt.equals("")&&!v_enddt.equals("")){
		            	sql += "\n and substr(a.edustart,0,8) between '"+v_startdt+"' and '"+v_enddt+"'";
		            	sql += "\n and substr(a.eduend,0,8) between '"+v_startdt+"' and '"+v_enddt+"'";
		            	}
		        	
		            if ( v_order.equals("") ) { 
		                sql += "\n order  by b.comp, a.isonoff, a.scsubjnm ";
		            } else { 
		                sql += "\n order  by " + v_order + v_orderType;
		            }
            
            }else {
                sql = "\n select a.scsubj "
                	+ "\n      , a.scyear "
                	+ "\n      , a.scsubjseq "
                	+ "\n      , a.scsubjnm "
                	+ "\n      , a.edustart "
                	+ "\n      , b.userid "
                	+ "\n      , c.comp "
                	+ "\n      , c.name "
                	+ "\n      , get_compnm(c.comp) as companynm "
                	+ "\n      , c.lvl_nm as jikwinm "                 
                	+ "\n      , c.position_nm  as deptnm "            	
                	+ "\n      , a.biyong "                                    
                	+ "\n      , a.isgoyong "
                	+ "\n      , a.goyongpricemajor "
                	+ "\n      , a.goyongpriceminor "
                	+ "\n      , a.goyongpricestand "
                	+ "\n      , get_codenm('0004', a.isonoff) as isonoff "
                	+ "\n      , decode(b.cancelkind, 'P', '�ݷ�','���?) as cancelkind "
                	+ "\n      , reason"                	
                	+ "\n from   tz_cancel b "
                	+ "\n      , vz_scsubjseq a "
                	+ "\n      , tz_member c "            	
                	+ "\n where  b.subj         = a.subj " 
                	+ "\n and    b.year         = a.year "
                	+ "\n and    b.subjseq      = a.subjseq "
                	+ "\n and    b.userid       = c.userid "
                	+ "\n and    a.grcode       = "+ SQLString.Format(p_grcode)
                	+ "\n and    a.gyear        = "+ SQLString.Format(p_gyear)
                	+ "\n and    a.grseq        = "+ SQLString.Format(p_grseq)
                	+ "\n and    c.comp 		= "+ StringManager.makeSQL(p_company)            
                	+ "\n and    a.isonoff 		= "+ StringManager.makeSQL(p_isonoff)
                	+ "\n and    a.isuse ='Y'" ;               	          
                
	            	if(!v_startdt.equals("")&&!v_enddt.equals("")){
	                	sql += "\n and substr(a.edustart,0,8) between '"+v_startdt+"' and '"+v_enddt+"'";
	                	sql += "\n and substr(a.eduend,0,8) between '"+v_startdt+"' and '"+v_enddt+"'";
	                	}
	            	
	                if ( v_order.equals("") ) { 
	                    sql += "\n order  by c.comp, a.isonoff, a.scsubjnm ";
	                } else { 
	                    sql += "\n order  by " + v_order + v_orderType;
	                }                
            }

System.out.println("sql==\n"+sql);
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
	
}