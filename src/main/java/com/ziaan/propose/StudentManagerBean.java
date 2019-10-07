// **********************************************************
//  1. 제      목: PROPOSE STATUS ADMIN BEAN
//  2. 프로그램: StudentManagerBean.java
//  3. 개      요: 신청 현황 관리자 bean
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 김미향  2008. 09. 25
//  7. 수      정:
// **********************************************************
package com.ziaan.propose;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.common.GetCodenm;
import com.ziaan.common.SubjComBean;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.Encrypt;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormMail;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.MailSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import common.Logger;
import org.apache.log4j.*;

public class StudentManagerBean { 
    private ConfigSet config;
    private int row;
    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(StudentManagerBean.class);

    public StudentManagerBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.manage.row") );  // 이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }
    
    /**
     * 신청명단 리스트
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList selectStduentMemberList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;        
        ArrayList list1     = null;
        ArrayList list2     = null;        
        DataBox dbox        = null;
        DataBox dbox2        = null;

        String sql1         = "";
        String sql2         = "";
        String  v_eduterm   = "";   // 교육기간정보
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수
        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과정분류(대분류)
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");   // 과정분류(중분류)
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과정분류(소분류)
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_company  = box.getStringDefault("s_company","ALL");   // 회사
        String  v_searchtxt = box.getStringDefault("p_searchtext1", "ZZZ");    // id or name 검색

        String  v_order     = box.getString("p_order");           // 정렬할 컬럼명
        String  v_orderType = box.getString("p_orderType");                 // 정렬할 순서

        SubjComBean scbean  = new SubjComBean();
        ProposeBean probean = new ProposeBean();

        //String v_year       = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subjcourse, ss_subjseq);
        //if ( v_year.equals("")) v_year = ss_gyear;

        String              v_Bcourse       = ""; // 이전코스
        String              v_course        = ""; // 현재코스
        String              v_Bcourseseq    = ""; // 이전코스기수
        String              v_courseseq     = ""; // 현재코스기수
        String              v_Buserid       = "";
        String              v_userid        = "";
        
        String p_startdt = box.getString("p_startdt")+"00";
        String p_enddt = box.getString("p_enddt")+"23";
        
        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            list2 = new ArrayList();
            
            sql1  = "select 	                     	                                            \n" +
		    		"       c.course																\n" + 
		    		"     , c.cyear																	\n" +
		    		"     , c.courseseq																\n" +
		    		"     , c.coursenm                                                              \n" +
                    "     , c.subj                                                                  \n" +		    		
                    "     , c.year                                                                  \n" +
                    "     , c.subjnm                                                                \n" +
                    "     , c.subjseq                                                               \n" +
                    "     , c.subjseqgr                                                             \n" +
                    "     , c.edustart                                                              \n" +
                    "     , c.eduend                                                                \n" +
                    "     , c.isclosed                                                              \n" +
                    "     , c.isgoyong                                                              \n" +
                    "     , c.biyong                                                                \n" +//수강료
                    "     , c.goyongpricemajor                                                      \n" +//대기업
                    "     , c.goyongpriceminor                                                      \n" +//우선지원
                    "     , c.goyongpricestand                                                      \n" +
                    "     , get_codenm('0004',c.isonoff) isonoff                                    \n" +
                    "     , b.name                                                                  \n" +
                    "     , b.position_nm                                                           \n" +
                    "     , b.lvl_nm                                                                \n" +
                    "     , b.userid                                                                \n" +
                    "     , b.email				                                                    \n" +
                    "     , b.hometel				                                                \n" +
                    "     , b.handphone				                                                \n" +
                	"     , get_compnm(b.comp) as companynm 										\n" +                    
                    "     , a.appdate                                                               \n" +
                    "     , get_cpnm((select cp from tz_subj where subj=c.scsubj)) cpnm             \n" +
                    "     , a.chkfirst                                                              \n" +
                    "     , a.chkfinal                                                              \n" +
                    "     , nvl(d.isgraduated,'M') as grdvalue                                      \n" +
                    "     , d.notgraducd                                                            \n" +
                    "     , decode(d.isgraduated, null, (case when to_char(sysdate,'yyyymmddhhmm') >= c.edustart then 'Y' else 'N' end), 'Y') as rejectpossible \n" +
                    "     , case 																	\n" +
                    "            when c.edustart <= to_char(sysdate,'yyyymmddhh') and c.eduend  > to_char(sysdate,'yyyymmddhh') then \n" +
                    "                 '4' --교육기간												\n" +
                    "            when c.eduend <= to_char(sysdate,'yyyymmddhh') then 				\n" +
                    "                 '5' --교육종료후												\n" +
                    "            when to_char(sysdate,'yyyymmddhh') < c.propstart then 				\n" +
                    "                 '1' --수강신청전												\n" +
                    "            when c.propstart <= to_char(sysdate,'yyyymmddhh') and c.propend  > to_char(sysdate,'yyyymmddhh') then \n" +
                    "                 '2' --수강신청기간											\n" +
                    "            when c.propend <= to_char(sysdate,'yyyymmddhh') and c.edustart  > to_char(sysdate,'yyyymmddhh') then \n" +
                    "                 '3' --교육대기기간											\n" +
                    "            when c.propstart is null and c.propend is null then 				\n" +
                    "                 '0' 															\n" +
                    "       end as eduterm            												\n" +
                    
                	// 추가요청 교재 배송을 위한 정보
                	 "\n      , HRDC "
                	+ "\n      , ZIP_CD "
                	+ "\n      , ADDRESS "
                	+ "\n      , ZIP_CD1 "
                	+ "\n      , ADDRESS1 "
                	+ "\n      , USER_PATH "
                	+ "\n      , ISMAILLING "
                	+ "\n      , ISSMS "
                	//+ "\n		, ( select examnum from tz_student ts where ts.userid=b.userid and ts.subj = a.subj) examnum \n"       +
                	+ "\n      , ts.examnum "+
                	"from   tz_propose   a                                                          \n" +
                    "     , tz_member    b                                                          \n" +
                    "     , vz_scsubjseq c                                                          \n" +
                    "     , tz_stold d                                                          	\n" +
                    "     , tz_student ts                                                          	\n" +
                    "where  a.userid  = b.userid                                                    \n" +
                    "and    a.subj    = c.subj                                                      \n" +
                    "and    a.year    = c.year                                                      \n" +
                    "and    a.subjseq = c.subjseq                                                   \n" +
		            "and    a.subj    = d.subj(+)                                                   \n" +
		            "and    a.year    = d.year(+)                                                   \n" +
		            "and    a.subjseq = d.subjseq(+)                                                \n" +
		            "and    a.userid  = d.userid(+)                                                 \n" +
		            "and    a.userid =ts.userid(+)                                                 \n" +
		            "and    a.subj= ts.subj(+)                                                  \n" +
		            "and    a.subjseq=ts.subjseq(+)                                                   \n" +
		            "and    a.year=ts.year(+)                                                  \n" ;
                    //"and    c.edustart between " + SQLString.Format(p_startdt) + " and " + SQLString.Format(p_enddt);
		            
            if ( !v_searchtxt.equals("ZZZ") ) { 
            	sql1 += "   and (b.userid like  '%" +v_searchtxt + "%' or b.name like '%" +v_searchtxt+"%') \n";
            }

            if ( !ss_grcode.equals("ALL"))     sql1 += "and    c.grcode           = " +SQLString.Format(ss_grcode) + "   \n";
            if ( !ss_grseq.equals("ALL"))      sql1 += "and    c.grseq            = " +SQLString.Format(ss_grseq)  + "   \n";
            //if ( !ss_gyear.equals("ALL"))      sql1 += "and    c.gyear            = " +SQLString.Format(ss_gyear)  + "   \n";
            if ( !ss_uclass.equals("ALL"))     sql1 += "and    c.scupperclass     = " +SQLString.Format(ss_uclass) + "   \n";
            if ( !ss_mclass.equals("ALL"))     sql1 += "and    c.scmiddleclass    = " +SQLString.Format(ss_mclass) + "   \n";
            if ( !ss_lclass.equals("ALL"))     sql1 += "and    c.sclowerclass     = " +SQLString.Format(ss_lclass) + "   \n";
            if ( !ss_subjcourse.equals("ALL")) sql1 += "and    c.scsubj           = " +SQLString.Format(ss_subjcourse)+" \n";
            if ( !ss_subjseq.equals("ALL"))    sql1 += "and    c.scsubjseq        = " +SQLString.Format(ss_subjseq)+ "   \n";
            if ( !ss_company.equals("ALL"))    sql1 += "and    b.comp 			  = " +SQLString.Format(ss_company)+ "   \n";


            if ( v_order.equals("subj"))     v_order ="a.subj";
            if ( v_order.equals("compnm1"))  v_order ="get_compnm(b.comp)";
            if ( v_order.equals("compnm2"))  v_order ="b.position_nm";
            if ( v_order.equals("jiknm"))    v_order ="b.post_nm";
            if ( v_order.equals("name"))     v_order ="b.name";
            if ( v_order.equals("userid"))   v_order ="b.userid";

            //if ( v_order.equals("") ) { 
            //    sql1 += "order  by c.subj,c.year,c.subjseq";
            //} else { 
            //    sql1 += "order  by " + v_order + v_orderType;
            //}
            
            if ( v_order.equals("") ) { 
                sql1 += " order by C.course, C.cyear, C.courseseq, C.subjnm,C.year,C.subjseq,B.userid ";
            } else { 
                sql1 += " order by C.course, C.cyear, C.courseseq, " + v_order + v_orderType+",B.userid ";
            } 
            
            //System.out.println("======================수강생관리=========================\n"+sql1);
            
            ls1 = connMgr.executeQuery(sql1);

            String[] arr_notgraducd;
            String v_notgradunm = "";
            while ( ls1.next() ) { 
                dbox = ls1.getDataBox();
                /*
                // 미수료 사유
                arr_notgraducd = ls1.getString("notgraducd").split(",");
                for (int i=0; i<arr_notgraducd.length; i++) {
                	if (i == 0) {
                		v_notgradunm = GetCodenm.get_codenm("0028", arr_notgraducd[i]);
                	} else {
                		v_notgradunm += " , " + GetCodenm.get_codenm("0028", arr_notgraducd[i]);
                	}
                }
                dbox.put("d_notgradunm", v_notgradunm);
                 
                // 교육기간구분
                v_eduterm = scbean.getEduTerm( ls1.getString("subj"), ls1.getString("subjseq"), ls1.getString("year") );

                dbox.put("d_eduterm", v_eduterm);
                */
                list1.add(dbox);
            }
            
            for(int i=0;i < list1.size(); i++){
                dbox2       =   (DataBox)list1.get(i);
                v_course    =   dbox2.getString("d_course");
                v_courseseq =   dbox2.getString("d_courseseq");
                v_userid    =   dbox2.getString("d_userid");
                if(!v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq) && v_Buserid.equals(v_userid))){
                    sql2 = "select count(*) cnt from VZ_SCSUBJSEQ A,tz_propose B,TZ_MEMBER C ";                        
                    sql2+= "where A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq ";
                    sql2+= "and B.userid=C.userid  ";

        			if (!ss_grcode.equals("ALL")) {
        				sql2+= " and A.grcode = "+SQLString.Format(ss_grcode);
        			}
        			if (!ss_grseq.equals("ALL")) {
        				sql2+= " and A.grseq = "+SQLString.Format(ss_grseq);
        			}
        			if (!ss_uclass.equals("ALL")) {
        			    sql2+= " and A.scupperclass = "+SQLString.Format(ss_uclass);
        			}
        			
        			if (!ss_subjcourse.equals("ALL")) {
        				sql2+= " and A.scsubj = "+SQLString.Format(ss_subjcourse);
        			}
        			if (!ss_subjseq.equals("ALL")) {
        				sql2+= " and A.scsubjseq = "+SQLString.Format(ss_subjseq);
        			}            
                    if (!ss_company.equals("ALL")) {
                        sql2+= " and C.comp = "+SQLString.Format(ss_company);
                    }
        			sql2+= "and A.course = "+SQLString.Format(v_course)+" and A.courseseq = "+SQLString.Format(v_courseseq)
        			    +  "and B.userid = " + SQLString.Format(v_userid);

					if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
                    ls2 = connMgr.executeQuery(sql2);	
                    if(ls2.next()){
                        dbox2.put("d_rowspan", new Integer(ls2.getInt("cnt")));
                        dbox2.put("d_isnewcourse", "Y");                            
                    }
                }else{
                	dbox2.put("d_rowspan", new Integer(0));
                    dbox2.put("d_isnewcourse", "N");
                }
                v_Bcourse   =   v_course;
                v_Bcourseseq=   v_courseseq;
                v_Buserid   =   v_userid;
                list2.add(dbox2);
                if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
            }            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }            
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }


    /**
     *업로드 영수증 리스트
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList selectStduentMemberTax(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        DataBox dbox        = null;
        DataBox dbox2        = null;

        String sql1         = "";
        String sql2         = "";
        String  v_eduterm   = "";   // 교육기간정보
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수
        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과정분류(대분류)
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");   // 과정분류(중분류)
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과정분류(소분류)
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_company  = box.getStringDefault("s_company","ALL");   // 회사
        String  v_searchtxt = box.getStringDefault("p_searchtext1", "ZZZ");    // id or name 검색

        String  v_order     = box.getString("p_order");           // 정렬할 컬럼명
        String  v_orderType = box.getString("p_orderType");                 // 정렬할 순서

        SubjComBean scbean  = new SubjComBean();
        ProposeBean probean = new ProposeBean();

        //String v_year       = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subjcourse, ss_subjseq);
        //if ( v_year.equals("")) v_year = ss_gyear;

        String              v_Bcourse       = ""; // 이전코스
        String              v_course        = ""; // 현재코스
        String              v_Bcourseseq    = ""; // 이전코스기수
        String              v_courseseq     = ""; // 현재코스기수
        String              v_Buserid       = "";
        String              v_userid        = "";
        
        String p_startdt = box.getString("p_startdt")+"00";
        String p_enddt = box.getString("p_enddt")+"23";
        
        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            
            sql1  = "";
            sql1  += " select \n";
            sql1  += " ''발급||believe2024|' || d.order_id || '|1348301004|국립특수교육원|김은주|041-537-1474|' || fn_crypt('2', c.birth_date, 'knise') || '|||' || b.biyong || '|' || b.biyong || '|0|0|' || b.biyong || '|1' as pgnum \n";
            sql1  += " from tz_propose a inner join tz_subj b on a.subj=b.subj \n";
            sql1  += " inner join tz_member c on a.userid=c.userid inner join pa_payment d on a.userid=d.userid \n";
            sql1  += " where a.chkfinal='Y' \n";
            //sql1  += "  and  substr(d.order_id, 1, 4) = to_char(sysdate, 'yyyy')  \n";
            sql1  += "  and  trim(d.order_id) > '20100604105511805257'  \n";
        	sql1  += "  and (trim(a.gubun) != 'OB' or  trim(a.gubun) = '' or trim(a.gubun) is null) \n";
    		sql1  += "  and (d.card_type = '' or d.card_type is null) \n";
    		sql1  += "  and (d.order_id is not null or trim(d.order_id) != '')  \n";
    		sql1  += "  and b.biyong > 0  \n";
            //sql1  += " and a.gubun = ' PB' \n";
            //sql1  += " and a.appdate >= '20100501' \n";
            sql1  += " order by d.order_id \n";
            
            //sql1 = " select birth_date, cert, userid as pg1, '' pg2 from tz_member where cert='Y' ";
            
            //System.out.println("======================수강생관리=========================\n"+sql1);
            
            logger.info("수강생 무통장 영수증 \n" + sql1);
            ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 
                dbox = ls1.getDataBox();
                String cert = ls1.getString("cert");
                String birth_date = ls1.getString("birth_date");
                String pg1 = ls1.getString("pg1");
                String pg2 = ls1.getString("pg2");
                
                
                logger.info(" Cert : " + cert + " / birth_date : " + birth_date);
                
                if(cert.equals("Y"))
                	dbox.put("birth_date", Encrypt.com_Decode(birth_date));
                else
                	dbox.put("birth_date", birth_date);
                
                dbox.put("pg1", pg1);
                dbox.put("pg2", pg2);
                
                
                list1.add(dbox);
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
	 * 대상자삭제
     * @param box      receive from the form object and session
     * @return int
     */
	public int stduentMemberDelete(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        int isOk            = 0;
        int isOk1           = 0;
        int isOk2           = 0;
        int isOk3           = 0;
        int isOk4           = 0;
        int isOk5           = 0;
        int isOk6           = 0;
        
        Vector v_check1     = new Vector();
        v_check1            = box.getVector("p_checks");
        Enumeration em      = v_check1.elements();
        
        Hashtable insertData= new Hashtable();
        StringTokenizer st1 = null;

        String  v_user_id   = box.getSession("userid");
        String v_checks     = "";
        String v_userid     = "";
        String v_subj       = "";
        String v_year       = "";
        String v_subjseq    = "";
        String v_mastercd   = "";
        String v_gubun      = "2";
        boolean v_isedutarget = false;
        
        ProposeBean probean = new ProposeBean();
        SubjComBean csbean  = new SubjComBean();

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            while ( em.hasMoreElements() ) { 
                v_checks = (String)em.nextElement();

                st1      = new StringTokenizer(v_checks,",");
                while ( st1.hasMoreElements() ) { 
                    v_userid  = (String)st1.nextToken();
                    v_subj    = (String)st1.nextToken();
                    v_year    = (String)st1.nextToken();
                    v_subjseq = (String)st1.nextToken();
                    break;
                }

                v_mastercd    = csbean.getMasterCode(v_subj,v_year,v_subjseq);
                v_isedutarget = csbean.IsEduTarget(v_subj,v_year,v_subjseq, v_userid);

                insertData.clear();
                insertData.put("connMgr"    ,connMgr   );
                insertData.put("subj"       ,v_subj    );
                insertData.put("subjseq"    ,v_subjseq );
                insertData.put("year"       ,v_year    );
                insertData.put("userid"     ,v_userid  );
                insertData.put("gubun"      ,v_gubun   );
                insertData.put("cancelkind" ,"D"       );
                insertData.put("reason"     ,"운영자삭제");
                insertData.put("luserid"    ,v_user_id );
                insertData.put("reasoncd"   ,"99"      );

                //삭제전 이력 남김 
                isOk    = probean.insertCancel(insertData);
                // 신청테이블삭제
                isOk1 = probean.deletePropose(insertData);
                if(isOk1 > 0) {
                	probean.deleteProposeBook(insertData);
	                probean.deleteDeliveryInfo(insertData);
                }


                // 학생테이블삭제
                if ( probean.getOverStuCount(v_subj, v_year, v_subjseq, v_userid) > 0) { 
                	isOk2 = probean.deleteStudent(insertData);
                } else { 
                	isOk2 = 1;
                }

                // 반려테이블삭제
                if ( probean.getStuRejCount(v_subj, v_year, v_subjseq, v_userid) > 0) { 
                	isOk2 = probean.deleteStudentreject(insertData);
                } else { 
                	isOk2 = 1;
                }

                // 교육대상 정보 삭제
                if ( !v_mastercd.equals("")&&v_isedutarget) { 
                	insertData.put("mastercd",   v_mastercd);
                	isOk3 = probean.deleteEduTarget(insertData);
                } else { 
                	isOk3 = 1;
                }

                // 수강취소/반려테이블 삭제 - > 등록 (삭제정보 기록)
                if ( probean.getCancelCount(v_subj, v_year, v_subjseq, v_userid) > 0) { 
                	isOk4 = probean.deleteCancel(insertData);
                	isOk4 = probean.insertCancel(insertData);
                } else { 
                	isOk4 = 1;
                }

                // 수료테이블 삭제
                if ( probean.getOverStoldCount(v_subj, v_year, v_subjseq, v_userid) > 0) { 
                	isOk5 = probean.deleteStold(insertData);
                } else { 
                	isOk5 = 1;
                }

                isOk6 = 1;
                // 진도테이블 삭제 : 2009.02.06 최성환 과장님 요청, 진도정보는 남겨둘 것
                /*
                if ( probean.getOverProgressCount(v_subj, v_year, v_subjseq, v_userid) > 0) { 
                	isOk6 = probean.deleteProgress(insertData);
                } else { 
                	isOk6 = 1;
                }
                */

                if ( isOk1 > 0 && isOk2 > 0 && isOk3 > 0 && isOk4 > 0 && isOk5 > 0 && isOk6 > 0) { 
                	isOk = 1;
                	connMgr.commit();
                } else { 
                	isOk = 0;
                	connMgr.rollback();
                }
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, "");
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }

	/**
     * 신청 인원 조회 리스트
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList selectProposeMemberCountList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;
        ArrayList list1     = null;
        ArrayList list2     = null;
        String sql1         = "";
        String sql2         = "";
        ProposeStatusData data1= null;
        ProposeStatusData data2= null;
        String  v_Bcourse   = ""; // 이전코스
        String  v_course    = ""; // 현재코스
        String  v_Bcourseseq= ""; // 이전코스기수
        String  v_courseseq = ""; // 현재코스기수
        int v_pageno = box.getInt("p_pageno");
        String  ss_grcode     = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear      = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq      = box.getStringDefault("s_grseq","ALL");     // 교육기수
        String  ss_uclass     = box.getStringDefault("s_upperclass","ALL");    // 과정분류(대분류)
        String  ss_mclass     = box.getStringDefault("s_middleclass","ALL");   // 과정분류(중분류)
        String  ss_lclass     = box.getStringDefault("s_lowerclass","ALL");    // 과정분류(소분류)
        String  ss_subjcourse = box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq    = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_edustart   = box.getStringDefault("s_edustart","");  // 교육시작일
        String  ss_eduend     = box.getStringDefault("s_eduend","");    // 교육종료일
        String  ss_action     = box.getString("s_action");
        String  v_orderColumn = box.getString("p_orderColumn");           // 정렬할 컬럼명

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                list2 = new ArrayList();

                sql1  = "select b.grseq                                   \n";
                sql1 += "      ,b.course                                  \n";
                sql1 += "      ,b.cyear                                   \n";
                sql1 += "      ,b.coursenm                                \n";
                sql1 += "      ,b.courseseq                               \n";
                sql1 += "      ,b.subj                                    \n";
                sql1 += "      ,b.year                                    \n";
                sql1 += "      ,b.subjnm                                  \n";
                sql1 += "      ,b.subjseq                                 \n";
                sql1 += "      ,b.propstart                               \n";
                sql1 += "      ,b.propend                                 \n";
                sql1 += "      ,b.edustart                                \n";
                sql1 += "      ,b.eduend                                  \n";
                sql1 += "      ,b.studentlimit                            \n";
                sql1 += "      ,(select grseqnm                           \n";
                sql1 += "        from   tz_grseq                          \n";
                sql1 += "        where  grcode = b.grcode                 \n";
                sql1 += "        and    gyear  = b.gyear                  \n";
                sql1 += "        and    grseq  = b.grseq) as grseqnm      \n";
                sql1 += "      ,(select count(subj)                       \n";
                sql1 += "        from   tz_propose                        \n";
                sql1 += "        where  subj = b.subj                     \n";
                sql1 += "        and    year = b.year                     \n";
                sql1 += "        and    subjseq =b.subjseq                \n";
                sql1 += "        and    cancelkind is null) as procnt     \n";
                sql1 += "      ,(select count(subj)                       \n";
                sql1 += "        from   tz_propose                        \n";
                sql1 += "        where  subj    = b.subj                  \n";
                sql1 += "        and    year    = b.year                  \n";
                sql1 += "        and    subjseq = b.subjseq               \n";
                sql1 += "        and    cancelkind in('P','F')) as cancnt \n";
                sql1 += "      ,b.isonoff                                 \n";
                sql1 += "from   vz_scsubjseq b                            \n";
                sql1 += "where  1 = 1                                     \n";
                if ( !ss_grcode.equals("ALL") ) { 
                    sql1 += " and b.grcode = " +SQLString.Format(ss_grcode) + " \n";
                }
                    sql1 += " and b.gyear = " +SQLString.Format(ss_gyear) + " \n";
                if ( !ss_grseq.equals("ALL") ) { 
                    sql1 += " and b.grseq = " +SQLString.Format(ss_grseq) + " \n";
                }
                if ( !ss_uclass.equals("ALL"))     sql1 += "and    c.scupperclass     = " +SQLString.Format(ss_uclass) + "  \n";
                if ( !ss_mclass.equals("ALL"))     sql1 += "and    c.scmiddleclass    = " +SQLString.Format(ss_mclass) + "  \n";
                if ( !ss_lclass.equals("ALL"))     sql1 += "and    c.sclowerclass     = " +SQLString.Format(ss_lclass) + "  \n";

                if ( !ss_subjcourse.equals("ALL") ) { 
                    sql1 += " and b.scsubj = " +SQLString.Format(ss_subjcourse) + " \n";
                }
                if ( !ss_subjseq.equals("ALL") ) { 
                    sql1 += " and b.scsubjseq = " +SQLString.Format(ss_subjseq) + " \n";
                }
                if ( !ss_edustart.equals("") ) { 
                    sql1 += " and b.edustart >= " +SQLString.Format(ss_edustart + "00") + " \n";
                }
                if ( !ss_eduend.equals("") ) { 
                    sql1 += " and b.eduend <= " +SQLString.Format(ss_eduend + "00") + " \n";
                }
                // 교육시작일과 종료일이 선택되지 않은 경우에만 교육년도별도 검색함
                if ( ss_edustart.equals("ALL") && ss_eduend.equals("ALL") ) { 
                    sql1 += " and b.gyear = " +SQLString.Format(ss_gyear) + " \n";
                }
                if ( !v_orderColumn.equals("") ) { 
                    v_orderColumn = "b." +v_orderColumn;
                    sql1 += "order  by b.course ,b.cyear ,b.courseseq ,b.subj ,b.year ,b.subjseq ," +v_orderColumn + " \n";
                } else { 
                    sql1 += "order  by b.course ,b.cyear ,b.courseseq ,b.subj ,b.year ,b.subjseq \n";
                }

                ls1 = connMgr.executeQuery(sql1);

                ls1.setPageSize(row);                       // 페이지당 row 갯수를 세팅한다
                ls1.setCurrentPage(v_pageno);               // 현재페이지번호를 세팅한다.
                int total_page_count = ls1.getTotalPage();  // 전체 페이지 수를 반환한다
                int total_row_count = ls1.getTotalCount();  // 전체 row 수를 반환한다

                while ( ls1.next() ) { 
                    data1 = new ProposeStatusData();
                    data1.setGrseq         ( ls1.getString("grseq")     );
                    data1.setGrseqnm       ( ls1.getString("grseqnm")   );
                    data1.setCourse        ( ls1.getString("course")    );
                    data1.setCyear         ( ls1.getString("cyear")     );
                    data1.setCourseseq     ( ls1.getString("courseseq") );
                    data1.setCoursenm      ( ls1.getString("coursenm")  );
                    data1.setSubj          ( ls1.getString("subj")      );
                    data1.setYear          ( ls1.getString("year")      );
                    data1.setSubjseq       ( ls1.getString("subjseq")   );
                    data1.setSubjnm        ( ls1.getString("subjnm")    );
                    data1.setPropstart     ( ls1.getString("propstart") );
                    data1.setPropend       ( ls1.getString("propend")   );
                    data1.setEdustart      ( ls1.getString("edustart")  );
                    data1.setEduend        ( ls1.getString("eduend")    );
                    data1.setStudentlimit  ( ls1.getInt("studentlimit") );
                    data1.setProcnt        ( ls1.getInt("procnt")       );
                    data1.setCancnt        ( ls1.getInt("cancnt")       );
                    data1.setIsonoff       ( ls1.getString("isonoff")   );
                    
                    data1.setDispnum       (total_row_count - ls1.getRowNum() + 1);
                    data1.setTotalPageCount(total_page_count);
                    data1.setRowCount      (row);
                    
                    list1.add(data1);
                }
                
                for ( int i = 0;i < list1.size(); i++ ) { 
                    data2       =   (ProposeStatusData)list1.get(i);
                    v_course    =   data2.getCourse();
                    v_courseseq =   data2.getCourseseq();

                    if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq))) { 
                        sql2  = "select count(a.subj) as cnt \n";
                        sql2 += "from   vz_scsubjseq a       \n";
                        sql2 += "where  ";
                        if ( !ss_grcode.equals("ALL") ) { 
                            sql2 += "a.grcode = " +SQLString.Format(ss_grcode) + " \n";
                            sql2 += "and    ";
                        }
                        if ( !ss_grseq.equals("ALL") ) { 
                            sql2 += "a.grseq = " +SQLString.Format(ss_grseq) + " \n";
                            sql2 += "and    ";
                        }
                        if ( !ss_uclass.equals("ALL") ) { 
                            sql2 += "a.scupperclass = " +SQLString.Format(ss_uclass) + " \n";
                            sql2 += "and    ";
                        }
                        if ( !ss_mclass.equals("ALL") ) { 
                            sql2 += "a.scmiddleclass = " +SQLString.Format(ss_mclass) + " \n";
                            sql2 += "and    ";
                        }
                        if ( !ss_lclass.equals("ALL") ) { 
                            sql2 += "a.sclowerclass = " +SQLString.Format(ss_lclass) + " \n";
                            sql2 += "and    ";
                        }
                        if ( !ss_subjcourse.equals("ALL") ) { 
                            sql2 += "a.scsubj = " +SQLString.Format(ss_subjcourse) + " \n";
                            sql2 += "and    ";
                        }
                        if ( !ss_subjseq.equals("ALL") ) { 
                            sql2 += "a.scsubjseq = " +SQLString.Format(ss_subjseq) + " \n";
                            sql2 += "and    ";
                        }
                        if ( !ss_edustart.equals("") ) { 
                            sql2 += "a.edustart >= " +SQLString.Format(ss_edustart + "00") + " \n";
                            sql2 += "and    ";
                        }
                        if ( !ss_eduend.equals("") ) { 
                            sql2 += "a.eduend <= " +SQLString.Format(ss_eduend + "00") + " \n";
                            sql2 += "and    ";
                        }
                        // 교육시작일과 종료일이 선택되지 않은 경우에만 교육년도별도 검색함
                        if ( ss_edustart.equals("") && ss_eduend.equals("") ) { 
                            sql2 += "a.gyear = " +SQLString.Format(ss_gyear) + " \n";
                            sql2 += "and    ";
                        }
                        sql2 += "a.course = " +SQLString.Format(v_course) + " \n";
                        sql2 += "and    a.courseseq = " +SQLString.Format(v_courseseq) + " \n";

                        ls2 = connMgr.executeQuery(sql2);
                        if ( ls2.next() ) { 
                            data2.setRowspan( ls2.getInt("cnt") );
                            data2.setIsnewcourse("Y");
                        }
                    } else { 
                        data2.setRowspan(0);
                        data2.setIsnewcourse("N");
                    }
                    v_Bcourse   =   v_course;
                    v_Bcourseseq=   v_courseseq;
                    list2.add(data2);
                    if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return list2;
    }

	/**
	 * 폼메일 발송
	 * @param box      receive from the form object and session
	 * @return int
	 */
    public int sendFormMail(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        int cnt = 0;    //  메일발송이 성공한 사람수
        // p_checks로 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector v_check1     = new Vector();
        Vector v_check2     = new Vector();
        Vector v_check3     = new Vector();
        Vector v_check4     = new Vector();
        v_check1            = box.getVector("p_checks");
        v_check2            = box.getVector("p_subj");
        v_check3            = box.getVector("p_year");
        v_check4            = box.getVector("p_subjseq");
        Enumeration em1     = v_check1.elements();
        Enumeration em2     = v_check2.elements();
        Enumeration em3     = v_check3.elements();
        Enumeration em4     = v_check4.elements();
        String v_userid     = "";
        String v_subj       = "";
        String v_year       = "";
        String v_subjseq    = "";
        String v_isonoff    = "";
        String v_edustart   = "";

        try { 
            connMgr = new DBConnectionManager();
			/*========================폼메일 발송==========================*/ 
            String v_sendhtml = "mail8.html";
            FormMail fmail = new FormMail(v_sendhtml); // 폼메일발송인 경우
            MailSet mset = new MailSet(box);           // 메일 세팅 및 발송
            String v_mailTitle = "안녕하세요? 사이버연수원 운영자입니다.";
			/*========================폼메일 발송==========================*/ 

            while ( em1.hasMoreElements() ) { 
                v_userid    = (String)em1.nextElement();
                v_subj      = (String)em2.nextElement();
                v_year      = (String)em3.nextElement();
                v_subjseq   = (String)em4.nextElement();

                sql  = "select b.subjnm                                     \n";
                sql += "      ,b.isonoff                                    \n";
                sql += "      ,d.name                                       \n";
                sql += "      ,d.ismailing				                    \n";
                sql += "      ,d.userid as cono                             \n";
                sql += "      ,d.email				                        \n";
                sql += "      ,(select to_char(edustart,'yyyymmdd') - 2     \n";
                sql += "        from   tz_subjseq                           \n";
                sql += "        where  subj    = a.subj                     \n";
                sql += "        and    year    = a.year                     \n";
                sql += "        and    subjseq = '0002') as edustart        \n";
                sql += "from   tz_propose   a                               \n";
                sql += "      ,vz_scsubjseq b                               \n";
                sql += "      ,tz_member    d                               \n";
                sql += "where  a.userid  = " +SQLString.Format(v_userid) +" \n";
                sql += "and    a.subj    = " +SQLString.Format(v_subj)   +" \n";
                sql += "and    a.year    = " +SQLString.Format(v_year)   +" \n";
                sql += "and    a.subjseq = " +SQLString.Format(v_subjseq)+" \n";
                sql += "and    a.subj    = b.subj                           \n";
                sql += "and    a.year    = b.year                           \n";
                sql += "and    a.subjseq = b.subjseq                        \n";
                sql += "and    a.userid  = d.userid                         \n";
                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    String v_toEmail =  ls.getString("email");
                    String v_toCono  =  ls.getString("cono");
                    String v_ismailing= ls.getString("ismailing");

                    mset.setSender(fmail); // 메일보내는 사람 세팅

                    if ( ls.getString("isonoff").equals("ON") ) {     
                    	v_isonoff = "사이버";   
                    } else {
                    	v_isonoff = "집합";     
                    }
                    v_edustart      = FormatDate.getFormatDate(v_edustart,"yyyy.MM.dd");

                    fmail.setVariable("subjnm"  , ls.getString("subjnm") );
                    fmail.setVariable("edustart", ls.getString("edustart") );
                    fmail.setVariable("isonoff" , v_isonoff);
                    fmail.setVariable("toname"  , ls.getString("name") );

                    String v_mailContent = fmail.getNewMailContent();

                    boolean isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent,v_ismailing, v_sendhtml);
                    if ( isMailed) cnt++;     // 메일발송에 성공하면
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return cnt;
    }


    /**
     * 변경가능기수 리스트
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList selectPossibleChangeSeq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        DataBox dbox        = null;

        String  sql1        = "";
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  v_eduterm   = "";
        String  v_edutermtxt= "";

        SubjComBean scbean = new SubjComBean();

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql1 += "select a.subjseq                                         \n";
            sql1 += "      ,a.subjseqgr                                       \n";
            sql1 += "      ,a.year                                            \n";
            sql1 += "      ,a.subjnm                                          \n";
            sql1 += "      ,a.propstart                                       \n";
            sql1 += "      ,a.propend                                         \n";
            sql1 += "      ,a.edustart                                        \n";
            sql1 += "      ,a.eduend                                          \n";
            sql1 += "      ,a.studentlimit                                    \n";
            sql1 += "      ,(select count(userid)                             \n";
            sql1 += "        from   vz_student                                \n";
            sql1 += "        where  a.subj    = subj                          \n";
            sql1 += "        and    a.subjseq = subjseq                       \n";
            sql1 += "        and    a.year    = year) as stucnt               \n";
            sql1 += "      ,(select count(userid)                             \n";
            sql1 += "        from   tz_propose                                \n";
            sql1 += "        where  a.subj    = subj                          \n";
            sql1 += "        and    a.subjseq = subjseq                       \n";
            sql1 += "        and    a.year    = year) as propcnt              \n";
            sql1 += "      ,a.isclosed                                        \n";
            sql1 += "from   vz_scsubjseq a                                    \n";
            sql1 += "where  a.subj    = " +SQLString.Format(ss_subjcourse)+ " \n";
            sql1 += "and    a.grcode  = " +SQLString.Format(ss_grcode)    + " \n";
            sql1 += "and    a.subjseq != " +SQLString.Format(ss_subjseq)  + " \n";
            sql1 += "order  by subjseq                                        \n";

            ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 
                dbox = ls1.getDataBox();

                v_eduterm = scbean.getEduTerm(ss_subjcourse, ls1.getString("subjseq"), ls1.getString("year") );
                if ( v_eduterm.equals("0") ) { 
                    v_edutermtxt = "-";
                } else if ( v_eduterm.equals("1") ) { 
                    v_edutermtxt = "신청전";
                } else if ( v_eduterm.equals("2") ) { 
                    v_edutermtxt = "수강신청기간";
                } else if ( v_eduterm.equals("3") ) { 
                    v_edutermtxt = "교육대기";
                } else if ( v_eduterm.equals("4") ) { 
                    v_edutermtxt = "교육기간";
                } else if ( v_eduterm.equals("5") ) { 
                    v_edutermtxt = "교육완료";
                }

                if ( ls1.getString("isclosed").equals("Y") ) { 
                    v_edutermtxt += "<br > <font color=red > (이력생성완료)</font > ";
                }
                else if ( ls1.getString("isclosed").equals("N") ) { 
                    v_edutermtxt += "<br > <font color=blue > (이력생성전)</font > ";
                }

                dbox.put("d_edutermtxt",v_edutermtxt);
                dbox.put("d_eduterm",v_eduterm);

                list1.add(dbox);
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
     * 기수변경처리
     * @param box      receive from the form object and session
     * @return ArrayList
     */
     public int updateChangeSeq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt4 = null;
        ListSet ls1         = null;
        ListSet ls3         = null;
        String sql1         = "";
        int isOk            = 0;
        int isOk1           = 0;
        int isOk2           = 0;
        int isOk3           = 0;
        int isOk4           = 0;
        int isOk5           = 0;
        int isOk6           = 0;

        Vector v_check1     = new Vector();
        v_check1            = box.getVector("p_checks");
        Enumeration em      = v_check1.elements();
        Hashtable insertData= new Hashtable();
        String  v_user_id   = box.getSession("userid");

        StringTokenizer st1 = null;
        String  v_checks    = "";
        String  v_userid    = "";
        String  v_subj      = "";
        String  v_year      = "";
        String  v_subjseq   = "";
        String  v_targetsubj      = box.getString("p_subj");
        String  v_targetsubjseq   = box.getString("p_subjseq");
        String  v_targetyear      = box.getString("p_year");
        String  v_mastercd  = "";
        boolean  v_isedutarget = false;
        
        ProposeBean probean = new ProposeBean();
        SubjComBean csbean = new SubjComBean();

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            while ( em.hasMoreElements() ) { 
                v_checks    = (String)em.nextElement();
                st1         = new StringTokenizer(v_checks,",");

                while ( st1.hasMoreElements() ) { 
                    v_userid  = (String)st1.nextToken();
                    v_subj    = (String)st1.nextToken();
                    v_year    = (String)st1.nextToken();
                    v_subjseq = (String)st1.nextToken();
                    break;
                }

                v_mastercd    = csbean.getMasterCode(v_subj,v_year,v_subjseq);
                v_isedutarget = csbean.IsEduTarget(v_subj,v_year,v_subjseq, v_userid);

                // 기존저장기수 Delete
                insertData.clear();
                insertData.put("connMgr", connMgr);
                insertData.put("subj", v_subj);
                insertData.put("subjseq", v_subjseq);
                insertData.put("year", v_year);
                insertData.put("userid", v_userid);

                isOk1 = probean.deletePropose(insertData);

                if ( probean.getOverStuCount(v_subj, v_year, v_subjseq, v_userid) > 0) { 
                	isOk2 = probean.deleteStudent(insertData);
                } else { 
                	isOk2 = 1;
                }

                if ( probean.getStuRejCount(v_subj, v_year, v_subjseq, v_userid) > 0) { 
                	isOk2 = probean.deleteStudentreject(insertData);
                } else { 
                	isOk2 = 1;
                }

                if ( !v_mastercd.equals("")&&v_isedutarget) { 
                	insertData.put("mastercd",   v_mastercd);
                	isOk3 = probean.deleteEduTarget(insertData);
                } else { 
                	isOk3 = 1;
                }

                if ( probean.getOverStoldCount(v_subj, v_year, v_subjseq, v_userid) > 0) { 
                	isOk4 = probean.deleteStold(insertData);
                } else { 
                	isOk4 = 1;
                }

                // 변경대상 기수 Insert
                insertData.clear();
                insertData.put("connMgr", connMgr);
                insertData.put("subj",     v_targetsubj);
                insertData.put("year",     v_targetyear);
                insertData.put("subjseq",  v_targetsubjseq);
                insertData.put("userid",   v_userid);
                insertData.put("luserid",  v_user_id);
                insertData.put("isdinsert","Y");
                insertData.put("chkfirst", "Y");
                insertData.put("chkfinal", "Y");

                if ( isOk1 > 0 && isOk2 > 0) { 
                	isOk5 = probean.insertPropose(insertData);
                	isOk6 = probean.insertStudent(insertData);
                }

                if ( isOk1 > 0 && isOk2 > 0 && isOk3 > 0 && isOk4 > 0 && isOk5 > 0 && isOk6 > 0 ) { 
                	isOk = 1;
                	connMgr.commit();
                } else { 
                	isOk = 0;
                	connMgr.rollback();
                }
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls3 != null ) { try { ls3.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( pstmt4 != null )  { try { pstmt4.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }

     
    /**
     * 기수 정보 리턴
     * @param box      receive from the form object and session
     * @return DataBox 기수 정보 리턴
     */
	public DataBox isClosedInfo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        DataBox dbox        = null;

        String  sql1          = "";
        String  ss_grcode     = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear      = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq      = box.getStringDefault("s_grseq","ALL");     // 교육기수
        String  ss_subjcourse = box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq    = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  v_eduterm     = "";
        ProposeBean probean   = new ProposeBean();
        String v_year         = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subjcourse, ss_subjseq);
        if ( v_year.equals("")) {
        	v_year = ss_gyear;
        }
        SubjComBean scbean = new SubjComBean();

        try { 
            connMgr = new DBConnectionManager();

            sql1 += "select a.isonoff                                                          \n";
            sql1 += "      ,a.subjseqgr                                                        \n";
            sql1 += "      ,a.isclosed                                                         \n";
            sql1 += "      ,decode(a.studentlimit, 0, 1000000, a.studentlimit) as studentlimit \n";
            sql1 += "      ,(select count(userid)                                              \n";
            sql1 += "        from   tz_propose                                                 \n";
            sql1 += "        where  a.subj    = subj                                           \n";
            sql1 += "        and    a.subjseq = subjseq                                        \n";
            sql1 += "        and    a.year    = year) as propcnt                               \n";
            sql1 += "from   vz_scsubjseq a                                                     \n";
            sql1 += "where  a.subj    = " +SQLString.Format(ss_subjcourse)                 + " \n";
            sql1 += "and    a.year    = " +SQLString.Format(v_year)                        + " \n";
            sql1 += "and    a.subjseq = " +SQLString.Format(ss_subjseq)                    + " \n";
            sql1 += "order  by subjseq                                                         \n";

            ls1 = connMgr.executeQuery(sql1);

            v_eduterm = scbean.getEduTerm(ss_subjcourse, ss_subjseq, v_year);

            if ( ls1.next() ) { 
                dbox = ls1.getDataBox();
                dbox.put("d_eduterm", v_eduterm);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return dbox;
    }

	
    /**
     * 신청가능여부(신청 인원) 체크
     * @param box      receive from the form object and session
     * @return String
     */
	public String  isOverflowStudent(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;

        String sql1         = "";
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_action   = box.getString("s_action");
        String  v_isaddpossible = "";

        ProposeBean probean = new ProposeBean();
        String v_year       = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subjcourse, ss_subjseq);
        if ( v_year.equals("")) v_year = ss_gyear;

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();

                sql1 += " select 'Y' as isaddpossible                               \n";
                sql1 += " from   vz_scsubjseq a                                     \n";
                sql1 += " where  a.subj    = " +SQLString.Format(ss_subjcourse) + " \n";
                sql1 += " and    a.subjseq = " +SQLString.Format(ss_subjseq)    + " \n";
                sql1 += " and    a.year    = " +SQLString.Format(v_year)        + " \n";
                sql1 += " and    decode(a.studentlimit,0, 1000000,  a.studentlimit) > (select count(userid) from tz_propose where subj = a.subj and subjseq = a.subjseq and year = a.year) \n";
                sql1 += " order  by subjseq                                         \n";

                ls1 = connMgr.executeQuery(sql1);

                if ( ls1.next() ) { 
                    v_isaddpossible = ls1.getString("isaddpossible");
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_isaddpossible;
    }


    /**
     * 선정된 교육대상자 입과처리
     * @param box      receive from the form object and session
     * @return int
     */
	public int AcceptTargetMember(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String sql1  = "";
        ProposeBean probean = new ProposeBean();
        int isOk =0, isOk1=0, isOk2=0, isOk4=0;

        // 교육 및 과목 기수 정보 selected Params
        String v_grcode     = box.getString("s_grcode");           // 교육그룹
        String v_gyear      = box.getString("s_gyear");            // 년도
        String v_grseq      = box.getString("s_grseq");            // 교육기수
        String v_subj       = box.getString("s_subjcourse");
        String v_subjseq    = box.getString("s_subjseq");
        String v_year       = probean.getSubjYear(v_grcode, v_gyear, v_grseq, v_subj, v_subjseq);

        if ( v_year.equals("")) {
        	v_year = v_gyear;
        }

        // p_subj로 넘어온 다수의 value를 처리하기 위해 vector로 구현
        String v_userid      = "";
        Vector v_checks      = box.getVector("p_checks");
        Enumeration em       = v_checks.elements();
        Hashtable insertData = new Hashtable();
        String v_luserid     = box.getSession("userid");

        boolean v_isedutarget= false;
        String  v_mastercd   = "";

        SubjComBean csbean   = new SubjComBean();

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            while ( em.hasMoreElements() ) { 
                v_userid = (String)em.nextElement();

                insertData.clear();

                insertData.clear();
                insertData.put("connMgr"  , connMgr  );
                insertData.put("subj"     , v_subj   );
                insertData.put("year"     , v_year   );
                insertData.put("subjseq"  , v_subjseq);
                insertData.put("userid"   , v_userid );
                insertData.put("luserid"  , v_luserid);
                insertData.put("isdinsert", "Y"      );
                insertData.put("chkfirst" , "Y"      );
                insertData.put("chkfinal" , "Y"      );
                insertData.put("box" 	  , box      );

                isOk1 = probean.insertPropose(insertData);
                isOk2 = probean.insertStudent(insertData);

                // 마스터과목과 매핑되어 있는 과목이 이고 수강신청 대상자로 선정이 되어 있지 않다면
                // 대상자 테이블에 Insert한다.
                v_mastercd    = csbean.getMasterCode(v_subj,v_year,v_subjseq);
                v_isedutarget = csbean.IsEduTarget(v_subj,v_year,v_subjseq, v_userid);

                if ( !v_mastercd.equals("")&&!v_isedutarget) { 
                	insertData.put("mastercd",   v_mastercd);
                	isOk4 = probean.insertEduTarget(insertData);
                } else { 
                	isOk4 = 1;
                }
                isOk = isOk1*isOk2*isOk4;
            }

            if ( isOk > 0 ) {   
            	ProposeBean proposeBean = new ProposeBean();
            	proposeBean.sendMail(connMgr, box, v_subj, v_year, v_subjseq, v_userid);	// 메일발송
            	
            	connMgr.commit();   
            } else {   
            	connMgr.rollback(); 
            }

        } catch ( Exception ex ) { 
            isOk =0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }


    /**
     * 학습자 추가가능여부
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public String  isStuAddPossible(String p_subj, String p_subjseq, String p_year, String p_userid) throws Exception { 
        DBConnectionManager	connMgr	= null;

        String v_isOk = "OK";
        int result = 0;

        try { 
            connMgr = new DBConnectionManager();
            ProposeCourseBean bean = new ProposeCourseBean();

			// 이미수료한과정이면 안됨	-14
			result =bean.jeyakIsgraduated(connMgr, p_subj, p_year, p_subjseq, p_userid, "");
			if ( result < 0) {
				v_isOk = "ERR1";
			}
			
			if ("OK".equals(v_isOk)) {
				// 다른기수를 학습하고 있는 과정이면 안됨	-15
				result =bean.jeyakStudentYn(connMgr, p_subj, p_year, p_subjseq, p_userid, "");
				if ( result < 0) {
					v_isOk = "ERR2";
				}
			}
			
			if ("OK".equals(v_isOk)) {
				// 다른기수를 신청한 과정이면 안됨	-16 
				result =bean.jeyakProposeYn(connMgr, p_subj, p_year, p_subjseq, p_userid, "");
				if ( result < 0) {
					v_isOk = "ERR3";
				}
			}

        } catch ( Exception ex ) { 
        	v_isOk = "ERR";
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception( ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_isOk;
    }

    /**
    변경가능기수 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectPossibleAddSeq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1          = null;
        ArrayList list1      = null;
        DataBox dbox         = null;

        String sql1          = "";
        String  ss_grcode    = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear     = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq     = box.getStringDefault("s_grseq","ALL");     // 교육기수
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq   = box.getStringDefault("s_subjseq","ALL");   // 과목 기수

        String  v_eduterm    = "";
        String  v_isclosed   = "";
        String  v_edutermtxt = "";
        SubjComBean scbean   = new SubjComBean();

        ProposeBean probean  = new ProposeBean();
        //String v_year        = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subjcourse, ss_subjseq);
        /*
        if ( v_year.equals("")) {
        	v_year = ss_gyear;
        }
         */
        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql1 += " select a.subj         \n";
            sql1 += "       ,a.subjseq      \n";
            sql1 += "       ,a.subjseqgr    \n";
            sql1 += "       ,a.year         \n";
            sql1 += "       ,a.subjnm       \n";
            sql1 += "       ,a.propstart    \n";
            sql1 += "       ,a.propend      \n";
            sql1 += "       ,a.edustart     \n";
            sql1 += "       ,a.eduend       \n";
            sql1 += "       ,a.studentlimit \n";
            sql1 += "       ,(select count(userid) from tz_student where a.subj = subj and a.subjseq = subjseq and a.year = year) stucnt  \n";
            sql1 += "       ,(select count(userid) from tz_propose where a.subj = subj and a.subjseq = subjseq and a.year = year) propcnt \n";
            sql1 += "       ,a.isclosed     \n";
            sql1 += "       ,case 																	\n";
            sql1 += "            when a.edustart <= to_char(sysdate,'yyyymmddhh') and a.eduend  > to_char(sysdate,'yyyymmddhh') then \n";
            sql1 += "                 '교육기간' --4												\n";
            sql1 += "            when a.eduend <= to_char(sysdate,'yyyymmddhh') then 				\n";
            sql1 += "                 '교육완료' --5												\n";
            sql1 += "            when to_char(sysdate,'yyyymmddhh') < a.propstart then 				\n";
            sql1 += "                 '신청전' --1													\n";
            sql1 += "            when a.propstart <= to_char(sysdate,'yyyymmddhh') and a.propend  > to_char(sysdate,'yyyymmddhh') then \n";
            sql1 += "                 '수강신청기간' --2											\n";
            sql1 += "            when a.propend <= to_char(sysdate,'yyyymmddhh') and a.edustart  > to_char(sysdate,'yyyymmddhh') then \n";
            sql1 += "                 '교육대기' --3												\n";
            sql1 += "            when a.propstart is null and a.propend is null then 				\n";
            sql1 += "                 '-' --0 														\n";
            sql1 += "        end as edutermtxt         												\n";
            sql1 += "       ,case 																	\n";
            sql1 += "            when a.edustart <= to_char(sysdate,'yyyymmddhh') and a.eduend  > to_char(sysdate,'yyyymmddhh') then \n";
            sql1 += "                 '4'															\n";
            sql1 += "            when a.eduend <= to_char(sysdate,'yyyymmddhh') then 				\n";
            sql1 += "                 '5'															\n";
            sql1 += "            when to_char(sysdate,'yyyymmddhh') < a.propstart then 				\n";
            sql1 += "                 '1'															\n";
            sql1 += "            when a.propstart <= to_char(sysdate,'yyyymmddhh') and a.propend  > to_char(sysdate,'yyyymmddhh') then \n";
            sql1 += "                 '2'															\n";
            sql1 += "            when a.propend <= to_char(sysdate,'yyyymmddhh') and a.edustart  > to_char(sysdate,'yyyymmddhh') then \n";
            sql1 += "                 '3'															\n";
            sql1 += "            when a.propstart is null and a.propend is null then 				\n";
            sql1 += "                 '0' 															\n";
            sql1 += "        end as eduterm            												\n";
            sql1 += " from   vz_scsubjseq a \n";
            sql1 += " where  a.subj   = " +SQLString.Format(ss_subjcourse) + " \n";
            sql1 += " and    a.grcode = " +SQLString.Format(ss_grcode)     + " \n";
            sql1 += " and    a.year   = " +SQLString.Format(ss_gyear)      + " \n";
            sql1 += " order  by subjseq     \n";

            ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 
                dbox = ls1.getDataBox();
                /*
                v_eduterm = scbean.getEduTerm(ss_subjcourse, ls1.getString("subjseq"), ls1.getString("year") );
                if ( v_eduterm.equals("0") ) { 
                    v_edutermtxt = "-";
                } else if ( v_eduterm.equals("1") ) { 
                    v_edutermtxt = "신청전";
                } else if ( v_eduterm.equals("2") ) { 
                    v_edutermtxt = "수강신청기간";
                } else if ( v_eduterm.equals("3") ) { 
                    v_edutermtxt = "교육대기";
                } else if ( v_eduterm.equals("4") ) { 
                    v_edutermtxt = "교육기간";
                } else if ( v_eduterm.equals("5") ) { 
                    v_edutermtxt = "교육완료";
                }*/

                // 이력결재상태
                v_isclosed = ls1.getString("isclosed");
                v_edutermtxt = ls1.getString("edutermtxt");

                if ( v_isclosed.equals("Y") ) { 
                    v_edutermtxt += "<br > <font color=red > (이력생성완료)</font > ";
                }
                else if ( v_isclosed.equals("N") ) { 
                    v_edutermtxt += "<br > <font color=blue > (이력생성전)</font > ";
                }

                dbox.put("d_edutermtxt",v_edutermtxt);

                list1.add(dbox);
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
     * 선택가능 회사 리스트
     * @param box      receive from the form object and session
     * @return ArrayList
     */
	public ArrayList selectPossibleCompany(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        DataBox dbox        = null;
        String sql1         = "";
        String ss_grcode    = box.getStringDefault("s_grcode","ALL");    // 교육그룹

        try { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();

                sql1 += " select a.comp            \n";
                sql1 += "       ,b.compnm          \n";
                sql1 += " from   tz_grcomp a       \n";
                sql1 += "       ,tz_compclass b    \n";
                sql1 += " where  a.grcode = " +SQLString.Format(ss_grcode) + " \n";
                sql1 += " and    b.comp   = a.comp \n";

                ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
                    dbox = ls1.getDataBox();
                    list1.add(dbox);
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
     * 반려처리 
     * @param box      receive from the form object and session
     * @return int
     */
	public int SaveRejectProcess(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String sql1         = "";
        int isOk            = 0;
        int isOk1           = 0;
        int isOk2           = 0;
        int isOk3           = 0;
        int isOk4           = 0;

        Vector v_check1     = new Vector();
        v_check1            = box.getVector("p_checks");
        Enumeration em      = v_check1.elements();
        Hashtable insertData= new Hashtable();

        StringTokenizer st1 = null;
        String v_checks     = "";
        String v_userid     = "";
        String  v_subj      = "";
        String  v_year      = "";
        String  v_subjseq   = "";
        String v_cancelkind = "F";
        String v_reason     = box.getString("p_rejreasonnm");
        String v_reasoncd   = box.getString("p_rejreasoncd");

        ProposeBean probean = new ProposeBean();
        String v_luserid    = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            while ( em.hasMoreElements() ) { 
                v_checks = (String)em.nextElement();

                st1      = new StringTokenizer(v_checks,",");
                while ( st1.hasMoreElements() ) { 
                    v_userid  = (String)st1.nextToken();
                    v_subj    = (String)st1.nextToken();
                    v_year    = (String)st1.nextToken();
                    v_subjseq = (String)st1.nextToken();
                    break;
                }

                insertData.clear();
                insertData.put("connMgr"       , connMgr     );
                insertData.put("subj"          , v_subj      );
                insertData.put("year"          , v_year      );
                insertData.put("subjseq"       , v_subjseq   );
                insertData.put("userid"        , v_userid    );
                insertData.put("luserid"       , v_luserid   );
                insertData.put("cancelkind"    , v_cancelkind);
                insertData.put("chkfinal"      , "N"         );
                insertData.put("rejectkind"    , v_reasoncd  );
                insertData.put("rejectedreason", v_reason    );

                isOk1 = probean.updatePropose(insertData);
//System.out.println("isOk1 = "+isOk1);
                insertData.put("targettb", "TZ_STUDENTREJECT");
                insertData.put("sourcetb", "TZ_STUDENT");
                
                isOk2 = probean.insertStudentreject(insertData);
//System.out.println("isOk2 = "+isOk2);
                if ( probean.getOverStuCount(v_subj, v_year, v_subjseq, v_userid) > 0) { 
                	isOk3 = probean.deleteStudent(insertData);
                } else { 
                	isOk3 = 1;
                }

                if ( probean.getOverStoldCount(v_subj, v_year, v_subjseq, v_userid) > 0) { 
                	isOk3 = probean.deleteStold(insertData);
                } else { 
                	isOk3 = 1;
                }

                insertData.put("reason",     v_reason);
                insertData.put("reasoncd",   v_reasoncd);

                //isOk4 = probean.insertCancel(insertData);

                isOk = isOk1*isOk2*isOk3;//*isOk4;
            }

            if ( isOk > 0 ) {   
            	connMgr.commit();   
            } else {   
            	connMgr.rollback(); 
            }
        } catch ( Exception ex ) { 
            isOk =0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
     * 반려자 승인 처리
     * @param box      receive from the form object and session
     * @return int 
     */
    public int SaveApprovalProcess(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String sql1         = "";
        int isOk            = 0;
        int isOk1           = 0;
        int isOk2           = 0;
        int isOk3           = 0;
        int isOk4           = 0;

        Vector v_check1     = new Vector();
        v_check1            = box.getVector("p_checks");
        Enumeration em      = v_check1.elements();
        Hashtable insertData= new Hashtable();

        StringTokenizer st1 = null;
        String v_checks     = "";
        String v_userid     = "";
        String  v_subj      = "";
        String  v_year      = "";
        String  v_subjseq   = "";

        ProposeBean probean = new ProposeBean();
        String v_luserid    = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            while ( em.hasMoreElements() ) { 
                v_checks = (String)em.nextElement();

                st1      = new StringTokenizer(v_checks,",");
                while ( st1.hasMoreElements() ) { 
                    v_userid  = (String)st1.nextToken();
                    v_subj    = (String)st1.nextToken();
                    v_year    = (String)st1.nextToken();
                    v_subjseq = (String)st1.nextToken();
                    break;
                }

                insertData.clear();
                insertData.put("connMgr", connMgr);
                insertData.put("subj",       v_subj);
                insertData.put("year",       v_year);
                insertData.put("subjseq",    v_subjseq);
                insertData.put("userid",     v_userid);
                insertData.put("luserid",    v_luserid);
                insertData.put("chkfinal",   "Y");
                insertData.put("rejectkind",   "");
                insertData.put("rejectedreason", "");

                isOk1 = probean.updatePropose(insertData);

                insertData.put("targettb", "tz_student");
                insertData.put("sourcetb", "tz_studentreject");

                if ( probean.getOverStuCount(connMgr, v_subj, v_year, v_subjseq, v_userid) > 0) { 
                	isOk2 = probean.deleteStudent(insertData);
                } else { 
                	isOk2 =1;
                }

                if ( probean.getStuRejCount(connMgr, v_subj, v_year, v_subjseq, v_userid) > 0) { 
                	isOk2 = probean.insertStudentreject(insertData);
                	isOk3 = probean.deleteStudentreject(insertData);
                } else { 
                	isOk2 =1;
                	isOk3 =1;
                }

                if ( probean.getCancelCount(connMgr, v_subj, v_year, v_subjseq, v_userid) > 0) { 
                	isOk4 = probean.deleteCancel(insertData);
                } else { 
                	isOk4 = 1;
                }

                isOk = isOk1*isOk2*isOk3*isOk4;
            }

            if ( isOk > 0 ) {   
            	connMgr.commit();   
            } else {   
            	connMgr.rollback(); 
            }

        } catch ( Exception ex ) { 
            isOk =0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
     * 고용보험 비적용자 처리
     * @param box          receive from the form object and session
     * @return int         1 : update
     */
    public int SaveNogoyongProcess(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String sql1         = "";
        int isOk            = 1;
        int isOk1           = 0;

        Vector v_check1     = new Vector();
        v_check1            = box.getVector("p_checks");
        Enumeration em      = v_check1.elements();

        StringTokenizer st1 = null;
        String v_checks     = "";
        String v_userid     = "";
        String  v_subj      = "";
        String  v_year      = "";
        String  v_subjseq   = "";

        String v_luserid     = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            while ( em.hasMoreElements() ) { 
                v_checks    = (String)em.nextElement();

                st1      = new StringTokenizer(v_checks,",");
                while ( st1.hasMoreElements() ) { 
                    v_userid  = (String)st1.nextToken();
                    v_subj    = (String)st1.nextToken();
                    v_year    = (String)st1.nextToken();
                    v_subjseq = (String)st1.nextToken();
                    break;
                }

                sql1  =  "update tz_student         \n";
                sql1 +=  "set    isgoyong = 'N'     \n";
                sql1 +=  "      ,luserid  = ?       \n";
                sql1 +=  "      ,ldate    = ?       \n";
                sql1 +=  "where  subj     = ?       \n";
                sql1 +=  "and    year     = ?       \n";
                sql1 +=  "and    subjseq  = ?       \n";
                sql1 +=  "and    userid   = ?       \n";
                pstmt = connMgr.prepareStatement(sql1);

                pstmt.setString(1, v_luserid);
                pstmt.setString(2, FormatDate.getDate("yyyyMMddHHmmss") );
                pstmt.setString(3, v_subj);
                pstmt.setString(4, v_year);
                pstmt.setString(5, v_subjseq);
                pstmt.setString(6, v_userid);

                isOk1 = pstmt.executeUpdate();
                if ( isOk1 == 0 ) {
                	isOk = 0;
                }
            }

            if ( isOk > 0 ) {   
            	connMgr.commit();   
            } else {   
            	connMgr.rollback(); 
            }

        } catch ( Exception ex ) { 
            isOk =0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
     * 고용보험 적용자 처리
     * @param box          receive from the form object and session
     * @return int         1 : update
     */
    public int SaveGoyongProcess(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String sql1         = "";
        int isOk            = 1;
        int isOk1           = 0;

        Vector v_check1     = new Vector();
        v_check1            = box.getVector("p_checks");
        Enumeration em      = v_check1.elements();

        StringTokenizer st1 = null;
        String v_checks     = "";
        String v_userid     = "";
        String  v_subj      = "";
        String  v_year      = "";
        String  v_subjseq   = "";

        String v_luserid     = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            while ( em.hasMoreElements() ) { 
                v_checks = (String)em.nextElement();

                st1      = new StringTokenizer(v_checks,",");
                while ( st1.hasMoreElements() ) { 
                    v_userid  = (String)st1.nextToken();
                    v_subj    = (String)st1.nextToken();
                    v_year    = (String)st1.nextToken();
                    v_subjseq = (String)st1.nextToken();
                    break;
                }

                sql1  =  "update tz_student      \n";
                sql1 +=  "set    isgoyong = 'Y'  \n";
                sql1 +=  "      ,luserid  = ?    \n";
                sql1 +=  "      ,ldate    = ?    \n";
                sql1 +=  "where  subj     = ?    \n";
                sql1 +=  "and    year     = ?    \n";
                sql1 +=  "and    subjseq  = ?    \n";
                sql1 +=  "and    userid   = ?    \n";
                pstmt = connMgr.prepareStatement(sql1);

                pstmt.setString(1, v_luserid);
                pstmt.setString(2, FormatDate.getDate("yyyyMMddHHmmss") );
                pstmt.setString(3, v_subj);
                pstmt.setString(4, v_year);
                pstmt.setString(5, v_subjseq);
                pstmt.setString(6, v_userid);

                isOk1 = pstmt.executeUpdate();
                if ( isOk1 == 0 ) {
                	isOk = 0;
                }
            }

            if ( isOk > 0 ) {   
            	connMgr.commit();   
            } else {   
            	connMgr.rollback(); 
            }

        } catch ( Exception ex ) { 
            isOk =0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

}