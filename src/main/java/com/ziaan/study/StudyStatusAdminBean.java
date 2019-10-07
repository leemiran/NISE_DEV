// **********************************************************
//  1. 제      목: STUDY STATUS ADMIN BEAN
//  2. 프로그램명: StudyStatusAdminBean.java
//  3. 개      요: 학습 현황 관리자 bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 2004.1.28
//  7. 수      정:
// **********************************************************
package com.ziaan.study;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import com.ziaan.library.Encrypt;
import com.ziaan.common.GetCodenm;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormMail;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.MailSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.propose.ProposeBean;
import com.ziaan.system.ManagerAdminBean;
import com.ziaan.system.MemberData;
public class StudyStatusAdminBean { 

    public StudyStatusAdminBean() { }



    /**
    * 성명검색시 개인이력 동명이인 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectPersonalNameList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ArrayList           list            = null;
        ListSet             ls              = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        MemberData          data            = null;
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        String              s_userid        = box.getSession("userid");
        String              s_gadmin        = box.getSession("gadmin");
                                            
        String              v_company       = box.getString("p_company");
        String              v_search        = box.getString("p_search");
        String              v_searchtext    = box.getString("p_searchtext");

        try { 
            connMgr = new DBConnectionManager();
            list    = new ArrayList();
            
            sbSQL.append(" select  userid                                                                           \n")
                 .append("     ,   name                                                                             \n")
                 .append("     ,   email                                                                            \n")
                 .append("     ,   hometel                                                                          \n")
                 .append("     ,   handphone                                                                        \n")
                 .append(" from TZ_MEMBER                                                                           \n")
                 .append(" where upper(name) like upper(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")    \n");
            
            if ( StringManager.substring(s_gadmin,0,1).equals("H") ) {          // 교육그룹관리자일경우
                sbSQL.append(" " + this.selectManagerGrcode (s_userid, s_gadmin ) + " \n");
            } else if ( s_gadmin.equals("K2") ) {                               // 회사관리자일경우
                sbSQL.append(" " + this.selectManagerComp   (s_userid, s_gadmin ) + " \n");
            } else if ( s_gadmin.equals("K6") || s_gadmin.equals("K7") ) {      // 부서관리자일경우
                sbSQL.append(" " + this.selectManagerDept   (s_userid, s_gadmin ) + " \n");
            }

            // 회사코드가 있을경우 회사코드 검색조건 추가
            if ( !(v_company.equals("") || v_company.equals("ALL"))) { 
                sbSQL.append(" and substr(comp,0,4) = " + StringManager.makeSQL(v_company) + " \n");
            }

            sbSQL.append(" order by userid asc, name asc \n");
            
            //System.out.println(this.getClass().getName() + "." + "selectPersonalNameList() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls      = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                data    = new MemberData();
                
                data.setUserid   ( ls.getString("userid"     ) );
                data.setName     ( ls.getString("name"       ) );
                data.setEmail    ( ls.getString("email"      ) );
                data.setHometel  ( ls.getString("hometel"    ) );
                data.setHandphone( ls.getString("handphone"  ) );

                list.add(data);
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

        return list;        
    }
    
    

    public ArrayList selectPersonalNameList1(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
         ArrayList          list            = null;
         ListSet            ls              = null;
         StringBuffer       sbSQL           = new StringBuffer("");
         DataBox            dbox            = null;
         
         int                iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

         String             s_userid        = box.getSession("userid");
         String             s_gadmin        = box.getSession("gadmin");
                                            
         String             v_search        = box.getString("p_search");
         String             v_searchtext    = box.getString("p_searchtext");

        try { 
            connMgr     = new DBConnectionManager();
            list        = new ArrayList();
             
            sbSQL.append("\n select a.userid ")
	             .append("\n      , a.name ")
	             .append("\n      , a.hometel ")
	             .append("\n      , a.handphone ")
	             .append("\n      , b.telno as comptel ")
	             .append("\n      , a.email ")
	             .append("\n      , a.address as addr ")
	             .append("\n      , a.zip_cd    post ")
	             .append("\n      , a.lglast ")
	             .append("\n      , fn_crypt('2', a.birth_date, 'knise') birth_date  ")
	             .append("\n      , '' useuserid ")
	             .append("\n      , '' type ")
	             .append("\n      , a.indate ")
	             .append("\n      , a.ldate ")
	             .append("\n      , get_compnm(a.comp) as compnm ")
	             .append("\n      , a.isretire ")
	             .append("\n      , get_deptnm(a.dept_cd) as deptnm ")
	             .append("\n	  , get_postnm(a.post) as jikupnm ")
	             .append("\n	  , a.position_nm ")
	             .append("\n from   tz_member a, tz_compclass b ")
	             .append("\n where  a.comp = b.comp(+) ");
            

                 
            if ( v_search.equals("name") ) {
                sbSQL.append("\n and    a.name = " + SQLString.Format(v_searchtext) );
            } else if ( v_search.equals("userid") ) {
                sbSQL.append("\n and    a.userid = " + SQLString.Format( v_searchtext ) );
            } else if ( v_search.equals("handphone") ) {
                sbSQL.append("\n and    replace(replace(replace(trim(a.handphone), ')', ''), '(', ''), '-', '') = " + SQLString.Format(StringManager.replace(StringManager.replace(StringManager.replace(v_searchtext, "(", "-"), ")", ""), "-", "")) );
            } else if ( v_search.equals("hometel") ) {
                sbSQL.append("\n and    replace(replace(replace(trim(a.hometel), ')', ''), '(', ''), '-', '') = " + SQLString.Format(StringManager.replace(StringManager.replace(StringManager.replace(v_searchtext, "(", "-"), ")", ""), "-", "")) );
            } else if ( v_search.equals("comptel") ) {
                sbSQL.append("\n and    replace(replace(replace(trim(b.telno), ')', ''), '(', ''), '-', '') = " + SQLString.Format(StringManager.replace(StringManager.replace(StringManager.replace(v_searchtext, "(", "-"), ")", ""), "-", "")) );
            } else if ( v_search.equals("email") ) {
                sbSQL.append("\n and    a.email = " + SQLString.Format(v_searchtext) + " \n");
            }                 
            
            sbSQL.append("\n order  by compnm, a.position_nm, a.name");
            
            ls = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
        	ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


   /**
   * 아이디 검색시 개인이력 상세조회
    @param box      receive from the form object and session
    @return MemberData
   */
    public MemberData selectPersonal(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        MemberData data = null;
        String v_userid     = "";
        String v_phototerms = "";
        PreparedStatement   pstmt        = null;
        ListSet             ls      = null;
        String              sql     = "";
        String              sql1     = "";
        String              sql2     = "";

        String s_userid     = box.getSession("userid");
        String s_userip     = box.getSession("userip");
        String s_name       = box.getSession("name");
        String s_gadmin     = box.getSession("gadmin");

        String v_company    = box.getString("p_company");
        String v_search     = box.getString("p_search");
        String v_searchtext = box.getString("p_searchtext");
        String v_searchuserid = box.getString("p_searchuserid");
        
        v_search = "userid";
        v_searchtext = v_searchuserid;
        
        try { 
            connMgr = new DBConnectionManager();
/* 2006.06.19 - 수정
            sql  = " select get_compnm(comp,2,2) companynm, userid, birth_date, pwd, name, email,    ";
            sql += "        cono, post1, post2, addr, addr2, hometel,                           ";
            sql += "        handphone, comptel, tel_line, comp, indate, lgcnt, lglast, lgip,    ";
            sql += "        jikup, get_jikupnm(jikup, comp, jikupnm) jikupnm, jikwi,            ";
            sql += "        get_jikwinm(jikwi,comp) jikwinm, office_gbn, office_gbnnm,          ";
            sql += "        work_plc, work_plcnm,  get_deptnm(deptnam,'') compnm, ismailing     ";
            sql += "  from TZ_MEMBER                                                            ";
            sql += " where 1 = 1                                                                ";
*/
            
            sql  = " select                                             \n" +
                   "    userid,                                         \n" +
                   "    fn_crypt('2', birth_date, 'knise') birth_date,                                          \n" +
                   "    fn_crypt('2', pwd, 'knise') pwd,     user_path,                                       \n" +
                   "    name,                                           \n" +
                   "    email,                                          \n" +
                   "    zip_cd,                                         \n" +
                   "    address, 		                                \n" +
                   "    address1, 		                                \n" +
                   "    hometel,                                        \n" +
                   "    handphone,                                      \n" +
                   "	get_compnm(comp) compnm,						\n"+
                   "	get_deptnm(dept_cd) deptnm,						\n"+
                   "    comp,                                           \n" +
                   "    indate,                                         \n" +
                   "    lgcnt,                                          \n" +
                   "    lglast,                                         \n" +
                   "    lgip,                                           \n" +
                   "    ismailling,                                     \n" +
                   "    get_postnm(post) as jikupnm,                    \n" +
                   "    isretire,                                       \n" +
                   "    retire_date,                                    \n" +
                   "    ( select codenm                                 \n" +
                   "      from tz_code                                  \n" +
                   "      where code = a.retire_type                    \n" +
                   "      and gubun = '0069'                            \n" +
                   "    ) retire_type,                                  \n" +
                   "    bon_adm, 										\n" +
                   "    position_nm,									\n" +
                   "    lvl_nm, cert ,dept_cd, agency_cd 				\n" +
                   "FROM                                                \n" +
                   "    TZ_MEMBER a                                     \n" +
                   "WHERE                                               \n" +
                   "        1 = 1                                       \n" ;
            
            
            if ( StringManager.substring(s_gadmin,0,1).equals("H") ) {          // 교육그룹관리자일경우
                sql += this.selectManagerGrcode(s_userid, s_gadmin);
            } else if ( s_gadmin.equals("K2") ) {                               // 회사관리자일경우
                sql += this.selectManagerComp(s_userid, s_gadmin);
            } else if ( s_gadmin.equals("K6") || s_gadmin.equals("K7") ) {      // 부서관리자일경우
                sql += this.selectManagerDept(s_userid, s_gadmin);
            }

            // 회사코드가 있을경우 회사코드 검색조건 추가
            if ( !(v_company.equals("") || v_company.equals("ALL"))) { 
            	sql += "   and comp = " + StringManager.makeSQL(v_company);
            }


            if ( v_search.equals("userid") ) {                        // ID로 검색할때
                sql += " and userid = '" + v_searchtext + "' ";
            }

            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) { 

                data = new MemberData();
/* 2006.06.19 - 제거 
                data.setJikwi( ls.getString("jikwi") );
                data.setJikwinm( ls.getString("jikwinm") );
                data.setOffice_gbn( ls.getString("office_gbn") );
                data.setOffice_gbnnm( ls.getString("office_gbnnm") );
                data.setWork_plc( ls.getString("work_plc") );
                data.setWork_plcnm( ls.getString("work_plcnm") );
                data.setCono( ls.getString("cono") );
                data.setIsmailing( ls.getString("ismailing") );
                data.setCompnm( ls.getString("compnm") );
                data.setCompanynm( ls.getString("companynm") );
*/              
                String v_post1 = "";
                String v_post2 = "";
                if(ls.getString("zip_cd").trim().length() == 7 ){
	                if(!"".equals(ls.getString("zip_cd"))) v_post1 = StringManager.substring(ls.getString("zip_cd").trim(),0,3);
	                if(!"".equals(ls.getString("zip_cd"))) v_post2 = StringManager.substring(ls.getString("zip_cd").trim(),4,7);
                }
                else if(ls.getString("zip_cd").trim().length() == 6){
                	if(!"".equals(ls.getString("zip_cd"))) v_post1 = StringManager.substring(ls.getString("zip_cd").trim(),0,3);
	                if(!"".equals(ls.getString("zip_cd"))) v_post2 = StringManager.substring(ls.getString("zip_cd").trim(),3,6);
                }
                else{
                	v_post1 = "";
                	v_post2 = "";
                }
                v_userid = ls.getString("userid");

                data.setDeptnm(ls.getString("deptnm"));
                data.setCompnm( ls.getString("compnm") );
                data.setUserid(v_userid);
                if ("Y".equals(ls.getString("cert"))) {
                	String temp = "";
                	temp = Encrypt.com_Decode(ls.getString("birth_date"));
                	if (temp.length() > 13) {
                		temp = temp.substring(0,13);
                	}
                	data.setbirth_date(temp);
                } else {
                	data.setbirth_date(ls.getString("birth_date") );
                }
                data.setJikupnm( ls.getString("jikupnm") );
                data.setPwd( ls.getString("pwd") );
                data.setName( ls.getString("name") );
                data.setEmail( ls.getString("email") );
                data.setPost1( v_post1 );
                data.setPost2( v_post2 );
                data.setAddr( ls.getString("address") );
                data.setAddr2( ls.getString("address1") );
                data.setHometel( ls.getString("hometel") );
                data.setHandphone( ls.getString("handphone") );
                data.setComp( ls.getString("comp") );
                data.setIndate( ls.getString("indate") );
                data.setLglast( ls.getString("lglast") );
                data.setLgip( ls.getString("lgip") );
                data.setLgcnt( ls.getInt("lgcnt") );
                data.setIsretire( ls.getString("isretire"));
                data.setRetire_date( ls.getString("retire_date"));
                data.setRetireType( ls.getString("retire_type"));
                data.setBon_adm(ls.getString("bon_adm"));
                data.setPosition_nm(ls.getString("position_nm"));
                data.setLvl_nm(ls.getString("lvl_nm"));
                data.setSnm(ls.getString("user_path"));
                data.setDept_cd(ls.getString("dept_cd"));
                data.setAgency_cd(ls.getString("agency_cd"));

                sql1 = "select nvl(max(seq), 0) from tz_membersearchlog";
                ls = connMgr.executeQuery(sql1);
                ls.next();
                int v_seq = ls.getInt(1) + 1;
                ls.close();
                
                sql2 = "Insert into tz_membersearchlog ( ";
                sql2 += "           seq, logdate, userid, name, gadmin,target_userid, target_name, ip) ";
                sql2 += "values(?, to_char(sysdate, 'YYYYMMDDHH24MISS') , ? , ?, ?, ?, ?, ?)";
                pstmt = connMgr.prepareStatement(sql2);
                pstmt.setInt(1, v_seq);
                pstmt.setString(2, s_userid);
                pstmt.setString(3, s_name);
                pstmt.setString(4, s_gadmin);
                pstmt.setString(5, v_searchtext);
                pstmt.setString(6, data.getName());
                pstmt.setString(7, s_userip);
                int   isOk = pstmt.executeUpdate();
                if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }

            } else {  // NullPointerException error를 막기위함
                data = new MemberData();
                v_userid = "";
            }
            ls.close();
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data;
    }


    /**
    교육그룹관리자 관리그룹해당 회사 조건쿼리
    @param box          receive from the form object and session
    @return String      조건쿼리
    */
    public String selectManagerGrcode(String v_userid, String v_gadmin) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        int    i = 0;

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select substr(comp,0,4) comp from TZ_GRCOMP         ";
            sql += "  where grcode in (                                  ";
            sql += "                   select grcode from TZ_GRCODEMAN   ";
            sql += "                    where userid = " + StringManager.makeSQL(v_userid);
            sql += "                      and gadmin = " + StringManager.makeSQL(v_gadmin);
            sql += "                  )                                  ";
            sql += " order by comp asc                                   ";
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                if ( i == 0) result  = "   and substr(comp,0,4) in ( ";
                else        result += ", ";

                result += StringManager.makeSQL( ls.getString("comp") );
                i++;
            }
            if ( i > 0) result += " ) ";
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }


    /**
    회사관리자 관리회사조건쿼리
    @param box          receive from the form object and session
    @return String      조건쿼리
    */
    public String selectManagerComp(String v_userid, String v_gadmin) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        int    i = 0;

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select substr(comp,0,4) comp from TZ_COMPMAN   ";
            sql += "  where userid = " + StringManager.makeSQL(v_userid);
            sql += "    and gadmin = " + StringManager.makeSQL(v_gadmin);
            sql += " order by comp asc                              ";
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                if ( i == 0) result  = "   and substr(comp,0,4) in ( ";
                else        result += ", ";

                result += StringManager.makeSQL( ls.getString("comp") );
                i++;
            }
            if ( i > 0) result += " ) ";
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }


    /**
    부서관리자 관리부사조건쿼리
    @param box          receive from the form object and session
    @return String      조건쿼리
    */
    public String selectManagerDept(String v_userid, String v_gadmin) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        int    i = 0;

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select substr(comp,0,8) comp from TZ_COMPMAN  ";
            sql += "  where userid = " + StringManager.makeSQL(v_userid);
            sql += "    and gadmin = " + StringManager.makeSQL(v_gadmin);
            sql += " order by comp asc            ";
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                if ( i == 0) result  = "   and substr(comp,0,8) in ( ";
                else        result += ", ";

                result += StringManager.makeSQL( ls.getString("comp") );
                i++;
            }
            if ( i > 0) result += " ) ";
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }


    /**
    개인별 신청 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectProposeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;        
        ArrayList list1     = null;
        ArrayList list2     = null;        
        DataBox dbox        = null;
        DataBox dbox2       = null;        
        String sql1         = "";
        String sql2         = "";
        StudyStatusData data1= null;
        StudyStatusData data2= null;
        String  v_Bcourse   = ""; // 이전코스
        String  v_course    = ""; // 현재코스
        String  v_Bcourseseq= ""; // 이전코스기수
        String  v_courseseq = ""; // 현재코스기수
        int     l           = 0;
        String v_userid         = box.getString("p_userid");        // 아이디

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            list2 = new ArrayList();            

            sql1= "\n select a.grcode, a.gyear, a.grseq, a.course, a.cyear, a.courseseq, a.coursenm, a.subj "
            	+ "\n      , a.subjnm, a.year, a.subjseq, a.subjseqgr, a.isonoff, a.edustart, a.eduend, b.appdate, b.chkfirst, b.chkfinal "
            	+ "\n      , (select grcodenm from tz_grcode where grcode=a.grcode) as grcodenm, a.isonoff "  
            	+ "\n      , (select grseqnm from tz_grseq where grcode = a.grcode and gyear=a.gyear and grseq=a.grseq) as grseqnm "
            	+ "\n      , isproposeapproval "
            	+ "\n from   vz_scsubjseq a, tz_propose b "
            	+ "\n where  b.userid = " +StringManager.makeSQL(v_userid) 
            	+ "\n and    a.subj = b.subj "
            	+ "\n and    a.year = b.year "
            	+ "\n and    a.subjseq = b.subjseq "
                + "\n and    ((b.chkfinal = 'Y' and (to_char(sysdate,'YYYYMMDDHH24') < a.edustart))  "
                + "\n        or (b.chkfinal='N' and (to_char(sysdate,'YYYYMMDDHH24') < to_char(to_date(substr(a.edustart,1,8))+10,'YYYYMMDDHH24')))  "
                + "\n        or b.chkfinal='B') "
                + "\n and    nvl(b.cancelkind,' ') not in ('P','F') "
            	//+ "\n order  by a.edustart desc,  a.subjnm ";
            	+ "\n order by A.course,A.cyear,A.courseseq,A.subj,A.year,A.subjseq ";            
            ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 

                dbox = ls1.getDataBox();
                list1.add(dbox);
            }

            for(int i=0;i < list1.size(); i++){
                dbox2       =  (DataBox)list1.get(i);
                v_course    =  dbox2.getString("d_course");
                v_courseseq =  dbox2.getString("d_courseseq");
                if(!v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq))){
                    sql2 = "select count(*) cnt from TZ_SUBJSEQ A,TZ_PROPOSE B ";
                    sql2+= "where B.userid = "+SQLString.Format(v_userid)+" and A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq ";
                    sql2+= "and course = "+SQLString.Format(v_course)+" and courseseq = "+SQLString.Format(v_courseseq);
                    
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
                list2.add(dbox2);
                if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }
            }            
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }            
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }

    /**
    개인별 수강 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectEducationList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;
        ArrayList list1     = null;
        ArrayList list2     = null;
        String sql1         = "";
        String sql2         = "";
        StudyStatusData data1= null;
        StudyStatusData data2= null;
        String  v_Bcourse   = ""; // 이전코스
        String  v_course    = ""; // 현재코스
        String  v_Bcourseseq= ""; // 이전코스기수
        String  v_courseseq = ""; // 현재코스기수
        int     l           = 0;
        String v_userid         = box.getString("p_userid");        // 아이디

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            list2 = new ArrayList();            

            sql1= "\n select a.grcode, a.gyear, a.grseq, a.course, a.cyear, a.courseseq, a.coursenm "
            	+ "\n      , a.subj,a.year, a.subjnm, a.subjseq "
            	+ "\n      , a.isonoff, a.edustart, a.eduend "
            	+ "\n      , b.avtstep, b.score, a.point "
            	+ "\n      , b.study_count, b.study_time ";
            
            sql1+= "\n      , nvl(ROUND (( ";
            sql1+= "\n              SELECT COUNT (*) completed_educheck_cnt ";
            sql1+= "\n              FROM tz_progress ";
            sql1+= "\n              WHERE subj =  a.subj ";
            sql1+= "\n        AND YEAR = a.year ";
            sql1+= "\n        AND subjseq = a.subjseq  ";
            sql1+= "\n        AND userid = b.userid  ";
            sql1+= "\n        AND first_end is not null ";
            sql1+= "\n    )	/ 	(Case When ";
            sql1+= "\n				( ";
            sql1+= "\n        		SELECT COUNT (*) total_cnt FROM TZ_SUBJLESSON  ";
            sql1+= "\n        		WHERE subj = a.subj and lesson != '00' and lesson != '99' ";
            sql1+= "\n				) > 0 Then  ( ";
            sql1+= "\n        		SELECT COUNT (*) total_cnt FROM TZ_SUBJLESSON  ";
            sql1+= "\n        		WHERE subj = a.subj and lesson != '00' and lesson != '99' ";
            sql1+= "\n    			) Else 1 End ";
            sql1+= "\n 			) * 100, 2), 0) as tstep  ";
            
            if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // 가중치적용
                sql1+= "\n      , b.avmtest mtest, b.avftest ftest "
                	+  "\n      , b.avreport report "
                	+  "\n      , b.avetc1 etc1, b.avetc2 etc2 "
                	+  "\n      , b.avact act ";
            } else {                                                  // 가중치비적용
                sql1+= "\n      , b.mtest, b.ftest "
                	+  "\n      , b.report "
                	+  "\n      , b.etc1, b.etc2 "
                	+  "\n      , b.act ";
            }
            sql1+="\n      , (select grcodenm from tz_grcode where grcode=A.grcode) as grcodenm "
            	+ "\n      , a.isonoff "
            	+ "\n      , (select grseqnm from tz_grseq where grcode = a.grcode and gyear=a.gyear and grseq=a.grseq) as grseqnm "
            	+ "\n      , b.isgraduated , a.isoutsourcing"
            	+ "\n from   vz_scsubjseq a, tz_student b, tz_stold c "
            	+ "\n where  b.userid = " +SQLString.Format(v_userid) 
            	+ "\n and    a.subj=b.subj "
            	+ "\n and    a.year=b.year "
            	+ "\n and    a.subjseq=b.subjseq "
            	+ "\n and    b.subj = c.subj(+) "
            	+ "\n and    b.year = c.year(+) "
            	+ "\n and    b.subjseq = c.subjseq(+) "
            	+ "\n and    b.userid = c.userid(+) "
                + "\n and    a.edustart    <= to_char(sysdate,'yyyymmddhh24') "
              	+ "\n and    c.isgraduated is null "
            	//+ "\n order  by substr(a.edustart, 1, 6) desc,  a.subjnm ";
                + "\n order by course, cyear, courseseq, substr(a.edustart, 1, 6) DESC,  A.subjnm ";       
            
            
            System.out.println("수강과목 sql1 :" + sql1);
            
            ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 
                data1 = new StudyStatusData();
                data1.setGrcode( ls1.getString("grcode") );
                data1.setGrcodenm( ls1.getString("grcodenm") );
                data1.setGrseqnm( ls1.getString("grseqnm") );
                data1.setGyear( ls1.getString("gyear") );
                data1.setGrseq( ls1.getString("grseq") );
                data1.setCourse( ls1.getString("course") );
                data1.setCyear( ls1.getString("cyear") );
                data1.setCourseseq( ls1.getString("courseseq") );
                data1.setCoursenm( ls1.getString("coursenm") );
                data1.setSubj( ls1.getString("subj") );
                data1.setYear( ls1.getString("year") );
                data1.setSubjnm( ls1.getString("subjnm") );
                data1.setSubjseq( ls1.getString("subjseq") );
                data1.setIsonoff( ls1.getString("isonoff") );
                data1.setEdustart( ls1.getString("edustart") );
                data1.setEduend( ls1.getString("eduend") );
                data1.setTstep( ls1.getInt("tstep") );
                data1.setAvtstep( ls1.getInt("avtstep") );
                data1.setMtest( ls1.getInt("mtest") );
                data1.setFtest( ls1.getInt("ftest") );
                data1.setEtc1( ls1.getInt("etc1") );
                data1.setEtc2( ls1.getInt("etc2") );
                data1.setAct( ls1.getInt("act") );
                data1.setReport( ls1.getInt("report") );
                data1.setScore( ls1.getInt("score") );
                data1.setStudy_count( ls1.getInt("study_count") );
                data1.setStudy_time( ls1.getInt("study_time") );
                data1.setPoint( ls1.getInt("point") );
                data1.setIsonoff( ls1.getString("isonoff") );
                data1.setIsgraduated( ls1.getString("isgraduated") );
                data1.setIsoutsourcing( ls1.getString("isoutsourcing") );
                list1.add(data1);
            }
            
            for ( int i = 0;i < list1.size(); i++ ) { 
                data2       =   (StudyStatusData)list1.get(i);
                v_course    =   data2.getCourse();
                v_courseseq =   data2.getCourseseq();
                if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq))) { 
                    sql2 = "select count(*) cnt 									\n"
                    	 + "from VZ_SCSUBJSEQ A,TZ_STUDENT B 						\n"
                    	 + "where B.userid = " +SQLString.Format(v_userid) + "		\n"
                    	 + "and A.subj=B.subj										\n"
                    	 + "and A.year=B.year										\n"
                    	 + "and A.subjseq=B.subjseq 								\n"
                    	 + "and course = " + SQLString.Format(v_course) + "			\n"
                    	 + "and courseseq = " + SQLString.Format(v_courseseq) + "	\n";
                    if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                    ls2 = connMgr.executeQuery(sql2);
                    if(ls2.next()){
                        data2.setRowspan(ls2.getInt("cnt"));
                        data2.setIsnewcourse("Y");
                    }                        
                }else{
                    data2.setRowspan(0);
                    data2.setIsnewcourse("N");
                }
                v_Bcourse   =   v_course;
                v_Bcourseseq=   v_courseseq;
                list2.add(data2);
                if(ls2 != null) { try { ls2.close(); }catch (Exception e) {} }   
            }            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }            
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }

    /**
    개인별 수료 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectGraduationList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        DataBox dbox        = null;
        String sql1         = "";
        String sql2         = "";
        StudyStatusData data1= null;
        StudyStatusData data2= null;
        String  v_Bcourse   = ""; // 이전코스
        String  v_course    = ""; // 현재코스
        String  v_Bcourseseq= ""; // 이전코스기수
        String  v_courseseq = ""; // 현재코스기수
        int     l           = 0;
        String v_userid         = box.getString("p_userid");        // 아이디

        String  v_birth_date = "";
        ProposeBean probean = new ProposeBean();
        Hashtable outdata = new Hashtable();

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            outdata = probean.getMeberInfo(v_userid);
            v_birth_date = (String)outdata.get("birth_date");

            sql1= "select * "
            	+ "from   ( "
            	+ "        select a.grcode , a.gyear , a.grseq , a.course , a.cyear, a.courseseq , a.coursenm "
            	+ "             , a.subj , a.year , a.subjseq, a.subjseqgr, a.subjnm, a.isonoff "
            	+ "             , a.edustart , a.eduend "
            	+ "             , b.tstep , b.avtstep , b.score , a.point, b.act "
            	+ "             , b.study_count, b.study_time ";
            if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // 가중치적용
            	sql1+="             , b.avmtest mtest, b.avftest ftest "
            		+ "             , b.avetc1 etc1, b.avetc2 etc2 ";
            } else {                                                  // 가중치비적용
            	sql1+="             , b.mtest, b.ftest "
            		+ "             , b.etc1, b.etc2 ";
            }
            sql1+="             , (select grcodenm from tz_grcode where grcode=a.grcode) as grcodenm "
            	+ "             , (select grseqnm from tz_grseq where grcode = a.grcode and gyear=a.gyear and grseq=a.grseq) as grseqnm "
            	+ "             , b.credit ,  b.creditexam "
            	+ "             , decode(b.isgraduated, 'Y', '수료', '미수료') graduatxt, report , a.isoutsourcing"
            	+ "        from   vz_scsubjseq a, tz_stold b "
            	+ "        where  b.userid = " + StringManager.makeSQL(v_userid)
            	+ "        and    a.subj = b.subj "
            	+ "        and    a.year = b.year "
            	+ "        and    a.subjseq = b.subjseq "
            	+ "       ) a "
            	+ "order  by substr(a.edustart, 1, 6) desc,  a.subjnm ";

            ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 

                dbox = ls1.getDataBox();
                dbox.put("d_hakjeum", new Double( ls1.getDouble("credit") + ls1.getDouble("creditexam")));
                list1.add(dbox);

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
    개인별 학점수료현황 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectScoreCompleteList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        DataBox dbox        = null;
        ArrayList list1     = null;
        String sql1         = "";
        String v_userid         = box.getString("p_userid");        // 아이디

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            sql1  = " select get_jikwinm(a.jikwi,b.comp) jikwinm, duty_yn, ";
            sql1 += "        required_cnt, required_score, choice_score    ";
            sql1 += "   from tz_scorecompleteresult a, tz_member b         ";
            sql1 += " where a.userid = b.userid                            ";
            sql1 += "   and a.userid = " + StringManager.makeSQL(v_userid);
            sql1 += " order by a.jikwi                                     ";

            ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 
                dbox = ls1.getDataBox();
                list1.add(dbox);
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
    수강과목의 일차별 학습현황 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectSubjectLessonList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls      = null;
        ArrayList list  = null;
        String sql      = "";
        String v_subj   = box.getString("p_subj");
        String v_year   = box.getString("p_year");
        String v_subjseq= box.getString("p_subjseq");
        String v_isonoff= box.getString("p_isonoff");
        String v_userid = box.getString("p_userid");
        StudyStatusData data  = null;
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            if ( v_isonoff.equals("ON") ) { 
                // select classnm,lesson,sdesc,first_edu,ldate,proj_score,act_score
                sql = "select (select classnm from TZ_CLASS where subj=B.subj and year=B.year and subjseq=B.subjseq and class=B.class),";
                sql += "A.lesson,A.sdesc,C.first_edu,C.ldate,";
                sql += "decode((select reptype from TZ_PROJORD where subj=B.subj and year=B.year and subjseq=B.subjseq and lesson=A.lesson),\'R\',";
                sql += "(select score from TZ_PROJREP where subj=B.subj and year=B.year and subjseq=B.subjseq and projgrp=B.userid and max(ordseq)),";
                sql += "(select score from TZ_PROJREP where subj=B.subj and year=B.year and subjseq=B.subjseq and max(ordseq) and projgrp in ";
                sql += "(select userid from TZ_PROJGRP where subj=B.subj and year=B.year and subjseq=B.subjseq))) proj_score, ";
                sql += "(select score from TZ_ACTIVITY_ANS where subj=B.subj and year=B.year and subjseq=B.subjseq and lesson=A.lesson and userid=B.userid ";
                sql += "and max(seq)) act_score ";
                sql += "from TZ_SUBJLESSON A,TZ_STUDENT B,TZ_PROGRESS C ";
                sql += "A.subj=" +SQLString.Format(v_subj) + " and A.subj=B.subj and B.year=" +SQLString.Format(v_year);
                sql += " and B.subjseq=" +SQLString.Format(v_subjseq) + " and B.userid=" +SQLString.Format(v_userid);
                sql += " and C.subj=B.subj and C.year=B.year and C.subjseq=B.subjseq and C.lesson=A.lesson";
            }
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                data = new StudyStatusData();
                data.setClassnm( ls.getString("classnm") );
                data.setLesson( ls.getString("lesson") );
                data.setSdesc( ls.getString("sdesc") );
                data.setFirstedu( ls.getString("first_edu") );
                data.setLdate( ls.getString("ldate") );
                data.setProjscore( ls.getString("projscore") );
                data.setActscore( ls.getString("actscore") );
                list.add(data);
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
    클래스별 학습현황 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectClassLearningList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls      = null;
        ListSet ls2     = null;        
        ArrayList list  = null;
        ArrayList list2 = null;        
        String sql      = "";
        String sql2      = "";        
        StudyStatusData data  = null;
        StudyStatusData data2  = null;        
        DataBox dbox = null;
        DataBox dbox2 = null;        
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수

        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");    // 과목분류
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류

        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_company  = box.getStringDefault("s_company","ALL");   // 회사
        String  ss_class    = box.getStringDefault("s_class","ALL");     // 클래스
        String  ss_action   = box.getString("s_action");
        String  v_orderColumn= box.getString("p_orderColumn");           // 정렬할 컬럼명
        String v_orderType     = box.getString("p_orderType");           // 정렬할 순서
        String  v_searchtxt = box.getStringDefault("p_searchtext1", "ZZZ");    // id or name 검색
        
        String  v_Bcourse   = ""; // 이전코스
        String  v_course    = ""; // 현재코스
        String  v_Bcourseseq= ""; // 이전코스기수
        String  v_courseseq = ""; // 현재코스기수
        int     v_rowspan   = 0;
        
        String v_userid = "";
        String v_Buserid = "";

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list = new ArrayList();
                list2 = new ArrayList();                

                sql = "\n select decode(a.isgraduated, 'N', case when to_char(sysdate, 'yyyymmddhh24') between c.edustart and c.eduend then '진행중' "
                	+ "\n                                            when to_char(sysdate, 'yyyymmddhh24') > c.eduend   then '미수료' "
                	+ "\n                                            when to_char(sysdate, 'yyyymmddhh24') < c.edustart then '대기중' "
                	+ "\n                                            else '미수료' "
                	+ "\n                                       end "
                	+ "\n                                , '수료' "
                	+ "\n           )  isgraduated "
                	+ "\n      , get_compnm(B.comp) as companynm "
                	+ "\n      , b.position_nm "
                	+ "\n      , b.lvl_nm "                	
                	+ "\n      , b.name "
                	+ "\n      , '' comptel "
                	+ "\n      , b.email "
		    		+ "\n      , c.course	"  
		    		+ "\n      , c.cyear " 
		    		+ "\n      , c.courseseq "
		    		+ "\n      , c.coursenm  "                	
                	+ "\n      , c.subjnm "
                	+ "\n      , c.subj "
                	+ "\n      , c.year "
                	+ "\n      , c.subjseq "
                	+ "\n      , a.class "
                	+ "\n      , d.sdate "
                	+ "\n      , e.tuserid "
                	+ "\n      , e.tname "
                	+ "\n      , a.tstep tstep "
                	+ "\n      , a.avtstep avtstep "
                	+ "\n      , b.userid "
                	+ "\n      , c.edustart "
                	+ "\n      , c.eduend ";

                if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // 가중치적용
                    sql +="\n      , a.avreport report" 
                    	+ "\n      , a.avmtest mtest" 
                    	+ "\n      , a.avftest ftest "
                    	+ "\n      , a.avact act ";
                } else {                                                  // 가중치비적용
                    sql +="\n      , a.report report" 
                    	+ "\n      , a.mtest mtest" 
                    	+ "\n      , a.ftest ftest "
                    	+ "\n      , a.act act ";
                }
                sql +="\n      , a.score score" 
                	+ "\n      , a.study_count study_count  " 
                	+ "\n      , a.study_time study_time  " 
                	+ "\n      , C.point point  "
                	+ "\n      , b.hometel  "//집전화
                	+ "\n      , b.handphone  "//휴대전화
                	+ "\n      , b.lvl_nm  "//직급
                	+ "\n      , b.email  "//이메일
                	+ "\n      , e.tcomp  "//강사소속회사
                	+ "\n      , e.tcellphone  "//강사 휴대전화
                	+ "\n      , e.temail  "//강사 이메일
                	+ "\n      , c.isgoyong  "//환급여부
                	+ "\n      , get_cpnm((select cp from tz_subj where subj=c.scsubj)) cpnm  "//cp
                	+ "\n from   tz_student a "
                	+ "\n      , tz_member b "
                	+ "\n      , vz_scsubjseq c "
                	+ "\n      , (select userid, subj, year, subjseq, max(sdate) sdate from tz_sangdam group by userid, subj, year, subjseq ) d "
                	+ "\n      , (select x.subj, x.year, x.subjseq, x.class, x.ttype, x.tuserid, y.name tname,y.comp tcomp,y.handphone tcellphone,y.email temail "
                	+ "\n         from   tz_classtutor x, tz_tutor y "
                	+ "\n         where  x.tuserid = y.userid and x.ttype = 'M') e  "
                	+ "\n where  a.userid = b.userid "
                	+ "\n and    a.subj = c.subj "
                	+ "\n and    a.year = c.year "
                	+ "\n and    a.subjseq = c.subjseq "
                	+ "\n and    a.userid = d.userid(+) "
                	+ "\n and    a.subj=d.subj(+) "
                	+ "\n and    a.year=d.year(+) "
                	+ "\n and    a.subjseq=d.subjseq(+) "
                	+ "\n and    a.subj = e.subj(+) "
                	+ "\n and    a.year = e.year(+) "
                	+ "\n and    a.subjseq = e.subjseq(+) "
                	+ "\n and    a.class = e.class(+) ";

                if ( !ss_grcode.equals("ALL"))     sql += "\n and c.grcode = " +SQLString.Format(ss_grcode);
                if ( !ss_gyear.equals("ALL"))      sql += "\n and c.gyear = " +SQLString.Format(ss_gyear);
                if ( !ss_grseq.equals("ALL"))      sql += "\n and c.grseq = " +SQLString.Format(ss_grseq);
                if ( !ss_uclass.equals("ALL"))     sql += "\n and c.scupperclass = " +SQLString.Format(ss_uclass);
                if ( !ss_mclass.equals("ALL"))     sql += "\n and c.scmiddleclass = " +SQLString.Format(ss_mclass);
                if ( !ss_lclass.equals("ALL"))     sql += "\n and c.sclowerclass = " +SQLString.Format(ss_lclass);
                if ( !ss_subjcourse.equals("ALL")) sql += "\n and c.scsubj = " +SQLString.Format(ss_subjcourse);
                if ( !ss_subjseq.equals("ALL"))    sql += "\n and c.scsubjseq = " +SQLString.Format(ss_subjseq);

                if ( !ss_class.equals("ALL") ) { 
                    sql += "\n and    a.class = " + SQLString.Format(ss_class);
                }
                if ( !ss_company.equals("ALL") ) { 
                    sql += "\n and    b.comp = " + SQLString.Format(ss_company);
                }

                if ( box.getSession("gadmin").equals("P1")) {
                	sql += "\n and e.tuserid = " +SQLString.Format(box.getSession("userid"));
                }
                //sql +="\n group  by a.isgraduated, get_compnm(B.comp), b.position_nm, d.sdate,  e.tuserid , e.tname "
                //	+ "\n         , b.userid, b.name, b.email,b.comp" 
                //	+ "\n         , c.subjnm " 
                //	+ "\n         , c.subj, c.year, c.subjseq, a.class, c.edustart, c.eduend ";

                if ( v_orderColumn.equals("subj"))     v_orderColumn ="c.subj";
                if ( v_orderColumn.equals("userid"))   v_orderColumn ="b.userid";
                if ( v_orderColumn.equals("name"))     v_orderColumn ="b.name";
                if ( v_orderColumn.equals("compnm1"))  v_orderColumn ="get_compnm(b.comp)";

                //if ( v_orderColumn.equals("") ) { 
                //    sql += "\n order  by c.subj, c.year, c.subjseq, a.class ";
                //} else { 
                //    sql += "\n order  by " + v_orderColumn + v_orderType;
                //}
                if ( !v_searchtxt.equals("ZZZ") ) { 
	            	sql += "   and (b.userid like  '%" +v_searchtxt + "%' or b.name like '%" +v_searchtxt+"%') \n";
	            } 
                
                if ( v_orderColumn.equals("") ) { 
                    sql += " order by C.course, C.cyear, C.courseseq, b.userid, C.subj, C.year, C.subjseq";
                } else { 
                    sql += " order by C.course, C.cyear, C.courseseq, b.userid, " + v_orderColumn + v_orderType;
                }                

                ls = connMgr.executeQuery(sql);

                
                while ( ls.next() ) { 
                	dbox = ls.getDataBox();
                    list.add(dbox);
                }
                
                for(int i=0;i < list.size(); i++){
                    dbox2       =   (DataBox)list.get(i);
                    v_course    =   dbox2.getString("d_course");
                    v_courseseq =   dbox2.getString("d_courseseq");
                    v_userid    =   dbox2.getString("d_userid");                  
                    if(!v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq) && v_Buserid.equals(v_userid))){
                        sql2 = "select count(*) cnt ";    
                        sql2+= "from TZ_STUDENT A,VZ_SCSUBJSEQ B,TZ_MEMBER C where 1 = 1  ";
            			if (!ss_grcode.equals("ALL")) {
            				sql2+= " and B.grcode = "+SQLString.Format(ss_grcode);
            			} 
            			if (!ss_gyear.equals("ALL")) {
            				sql2+= " and B.gyear = "+SQLString.Format(ss_gyear);
            			} 
            			if (!ss_grseq.equals("ALL")) {
            				sql2+= " and B.grseq = "+SQLString.Format(ss_grseq);
            			}
            			if (!ss_uclass.equals("ALL")) {
            				sql2+= " and B.scupperclass = "+SQLString.Format(ss_uclass);
            			}
            			if (!ss_mclass.equals("ALL")) {
            				sql2+= " and B.scmiddleclass = "+SQLString.Format(ss_mclass);
            			}
            			if (!ss_subjcourse.equals("ALL")) {
            				sql2+= " and B.scsubj = "+SQLString.Format(ss_subjcourse);
            			}
            			if (!ss_subjseq.equals("ALL")) {
            				sql2+= " and B.scsubjseq = "+SQLString.Format(ss_subjseq);
            			}
                        sql2+= " and A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq and A.userid=C.userid ";
                        sql2+= "and B.course = "+SQLString.Format(v_course)+" and B.courseseq = "+SQLString.Format(v_courseseq)
                             + "and a.userid = "+SQLString.Format(v_userid);
                        //sql2+= " group by B.course,B.cyear,B.courseseq,B.coursenm,B.subj,B.year,B.subjnm,B.subjseq,A.userid,C.name ";
                        ls2 = connMgr.executeQuery(sql2);	
                        //System.out.println("sql2==\n"+sql2);
                        if(ls2.next()){
	                        dbox2.put("d_rowspan", new Integer(ls2.getInt("cnt")));
	                        dbox2.put("d_isnewcourse", "Y");            
                            //v_rowspan = 0;
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
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }            
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
    성적별 학습현황 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectScoreLearningList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls      = null;
        ArrayList list  = null;
        String sql      = "";
        StudyStatusData data  = null;
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수

        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");    // 과목분류
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류

        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_company  = box.getStringDefault("s_company","ALL");   // 회사
        String  ss_selGubun  = box.getString("s_selGubun");              // 진도율 1,취득점수 2
        String  ss_selStart  = box.getStringDefault("s_selStart","0");   // ~부터
        String  ss_selEnd    = box.getStringDefault("s_selEnd","10000"); // ~까지
        String  ss_action   = box.getString("s_action");
        String  v_orderColumn= box.getString("p_orderColumn");   // 정렬할 컬럼명

        ManagerAdminBean bean = null;
        String  v_sql_add   = "";
        String  v_userid    = box.getSession("userid");
        String  s_gadmin    = box.getSession("gadmin");

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list = new ArrayList();

                // select compnm,jikwi,jikwinm,userid,cono,name,tstep,avtstep,
                // report,act,mtest,ftest,score,point,comptel,email
                sql = "select get_compnm(B.comp,2,4) compnm, B.jikwi,get_jikwinm(B.jikwi,B.comp) jikwinm,   ";
                sql += "B.jikup,get_jikupnm(B.jikup,B.comp) jikupnm, B.userid, B.cono, B.name,               ";
                sql += "avg(A.tstep) tstep, avg(A.avtstep) avtstep,                                          ";
                if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // 가중치적용
                    sql += "avg(A.avreport) report,avg(A.avact) act,avg(A.avmtest) mtest,avg(A.avftest) ftest, ";
                } else {                                                  // 가중치비적용
                    sql += "avg(A.report) report,avg(A.act) act,avg(A.mtest) mtest,avg(A.ftest) ftest, ";
                }
                sql += "avg(A.score) score,B.comptel,B.email,avg(C.point) point ";
                sql += "from TZ_STUDENT A,TZ_MEMBER B,VZ_SCSUBJSEQ C where 1=1 ";

                if ( !ss_grcode.equals("ALL")) sql += " and C.grcode = " +SQLString.Format(ss_grcode);
                if ( !ss_gyear.equals("ALL")) sql += " and C.gyear = " +SQLString.Format(ss_gyear);
                if ( !ss_grseq.equals("ALL")) sql += " and C.grseq = " +SQLString.Format(ss_grseq);
                if ( !ss_uclass.equals("ALL")) sql += " and C.scupperclass = " +SQLString.Format(ss_uclass);
                if ( !ss_mclass.equals("ALL")) sql += " and C.scmiddleclass = " +SQLString.Format(ss_mclass);
                if ( !ss_lclass.equals("ALL")) sql += " and C.sclowerclass = " +SQLString.Format(ss_lclass);
                if ( !ss_subjcourse.equals("ALL")) sql += " and C.scsubj = " +SQLString.Format(ss_subjcourse);
                if ( !ss_subjseq.equals("ALL")) sql += " and C.scsubjseq = " +SQLString.Format(ss_subjseq);

                if ( !ss_company.equals("ALL") ) { 
                    sql += " and substr(B.comp, 0 ,4) = '" +ss_company.substring(0,4) + "'";
                }
                if ( ss_selGubun.equals("1"))      {  // 진도율
                    sql += " and A.tstep >= " +SQLString.Format(ss_selStart) + " and A.tstep <= " +SQLString.Format(ss_selEnd);
                } else if ( ss_selGubun.equals("2") ) {  // 취득점수
                    sql += " and A.score >= " +SQLString.Format(ss_selStart) + " and A.score <= " +SQLString.Format(ss_selEnd);
                }

                // 부서장일경우
                if ( s_gadmin.equals("K6") || s_gadmin.equals("K7") ) { 
                    bean = new ManagerAdminBean();
                    v_sql_add   = bean.getManagerDept(v_userid, s_gadmin);
                    if ( !v_sql_add.equals("")) sql += " and B.comp in " + v_sql_add;       // 관리부서검색조건쿼리
                }

                sql += " and A.userid=B.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq           ";
                sql += " group by get_compnm(B.comp,2,4), B.jikwi, get_jikwinm(B.jikwi,B.comp),                      ";
                sql += "          B.jikup, get_jikupnm(B.jikup,B.comp,B.jikupnm),  B.userid, B.cono, B.name, B.comptel,B.email,B.comp      ";
                if ( !v_orderColumn.equals("") ) { 
                    v_orderColumn = "B." +v_orderColumn;
                    sql += " order by " +v_orderColumn;
                } else { 
                    sql += " order by B.comp,B.jikup,B.userid ";
                }
                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    data = new StudyStatusData();
                    data.setCompnm( ls.getString("compnm") );
                    data.setJikwi( ls.getString("jikwi") );
                    data.setJikwinm( ls.getString("jikwinm") );
                    data.setJikup( ls.getString("jikup") );
                    data.setJikupnm( ls.getString("jikupnm") );
                    data.setUserid( ls.getString("userid") );
                    data.setCono( ls.getString("cono") );
                    data.setName( ls.getString("name") );
                    data.setTstep( ls.getInt("tstep") );
                    data.setAvtstep( ls.getInt("avtstep") );
                    data.setReport( ls.getInt("report") );
                    data.setAct( ls.getInt("act") );
                    data.setMtest( ls.getInt("mtest") );
                    data.setFtest( ls.getInt("ftest") );
                    data.setScore( ls.getInt("score") );
                    data.setPoint( ls.getInt("point") );
                    data.setComptel( ls.getString("comptel") );
                    data.setEmail( ls.getString("email") );
                    list.add(data);
                }
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
    종합 학습현황 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectTotalScoreStatusList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls      = null;
        ListSet ls2     = null;
        ArrayList list  = null;
        ArrayList list2  = null;        
        String sql      = "";
        String sql1      = "";
        String sql2      = "";
        
        String  v_Bcourse   = ""; // 이전코스
        String  v_course    = ""; // 현재코스
        String  v_Bcourseseq= ""; // 이전코스기수
        String  v_courseseq = ""; // 현재코스기수
        String  v_Buserid   = "";            
        int     v_rowspan   = 0;        
          
        DataBox             dbox    = null;
        DataBox             dbox2    = null;        

        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수

        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");    // 과목분류
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류

        String  ss_subjcourse = box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq    = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_company    = box.getStringDefault("s_company","ALL");   // 회사
        String  ss_selGubun   = box.getString("s_selGubun");              // 출석일 1,취득점수 2
        String  ss_selStart   = box.getStringDefault("s_selStart","0");   // ~부터
        String  ss_selEnd     = box.getStringDefault("s_selEnd","10000"); // ~까지
        String  ss_samtotal   = box.getStringDefault("s_samtotal","ALL"); // 삼진아웃
        String  ss_goyong     = box.getStringDefault("s_goyong","ALL");   // 고용보험
        String  v_isgrad      = box.getStringDefault("p_isgrad"  , "M");   // 수료 여부(ALL:전체, M:진행중, Y:수료, N:미수료)
        String  v_isexam      = box.getStringDefault("p_isexam"  , "ALL");   // 평가 여부
        String  v_isreport    = box.getStringDefault("p_isreport", "ALL");   // 레포트 여부
        String  ss_class      = box.getStringDefault("s_class","ALL");// 클래스
        String  v_searchtxt = box.getStringDefault("p_searchtext1", "ZZZ");    // id or name 검색

        String  ss_action   	= box.getString("s_action");
        String  v_orderColumn	= box.getString("p_orderColumn");           // 정렬할 컬럼명
        String v_orderType    = box.getString("p_orderType");           // 정렬할 순서

        String  v_subj     		= "";// 과목&코스
        String  v_subjseq  		= "";   // 과목 기수
        //String  v_year     		= probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subjcourse, ss_subjseq);

        ManagerAdminBean bean = null;
        String  v_sql_add   	= "";
        String  v_userid    	= box.getSession("userid");
        String  s_gadmin    	= box.getSession("gadmin");

        String v_totsulcnt = "";
        String v_repsulcnt = "";
        String v_totexamcnt = "";
        String v_repexamcnt = "";
        String v_totprojcnt = "";
        String v_repprojcnt = "";
        
        int    v_cnt1 = 0;
        int    v_cnt2 = 0;

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list = new ArrayList();
                list2 = new ArrayList();                

                sql = "\n select * "
                    + "\n from  ( "
                    + "\n        select a.examnum "
                    + "\n    		  ,  c.course    " 
                    + "\n    		  , c.cyear     " 
                    + "\n    		  , c.courseseq " 
                    + "\n    		  , c.coursenm  " 
                    + "\n             , c.subj "                      
                    + "\n             , c.year "      
                    + "\n             , c.subjseq "
                    + "\n             , c.subjseqgr "
                    + "\n             , c.subjnm "
                    + "\n             , c.isonoff "
                    + "\n             , get_codenm('0004',c.isonoff) isonoffval "                    
                    + "\n             , b.userid "
                    + "\n             , fn_crypt('2', b.pwd, 'knise') pwd "
                    + "\n             , b.name "
                    + "\n             , b.hometel "
                    //+ "\n             , b.address || ' ' || b.address1 addr "
                    + "\n             , b.handphone "
                    + "\n             , b.email "
                    + "\n             , get_compnm(b.comp) companynm "
                    + "\n             , b.position_nm "
                    + "\n             , b.lvl_nm "
                    + "\n             , a.samtotal "
                    + "\n             , a.etc1 "
                    + "\n             , fn_crypt('2', b.birth_date, 'knise') birth_date "
                    + "\n             , b.cert "
                    + "\n             , b.user_path "
                    + "\n             , (select count(*) as cnt  from  tz_attendance f where a.subj=f.subj and a.year=f.year and b.userid=f.userid and isattend ='O' and  f.attdate between  substr(c.EDUSTART,0,8) and substr(c.EDUEND,0,8)) as rect1 "
                    + "\n             , trunc(a.avtstep , 1) as avtstep ";
                  
                       
                if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // 가중치적용
                sql +="\n             , trunc(a.avreport, 1)  report "
                    + "\n             , trunc(a.avmtest , 1)  mtest "
                    + "\n             , trunc(a.avetc1 , 1)   avetc1 "
                    + "\n             , trunc(a.avetc2 , 1)   avetc2 "
                    + "\n             , trunc(a.avftest , 1)  ftest "
                    + "\n             , trunc(a.avact , 1)    act ";
                } else {                                                    // 가중치비적용
                sql +="\n             , trunc(a.report, 1)  report "
                    + "\n             , trunc(a.mtest , 1)  mtest "
                    + "\n             , trunc(a.etc1 , 1)   avetc1 "
                    + "\n             , trunc(a.etc2 , 1)   avetc2 "
                    + "\n             , trunc(a.ftest , 1)  ftest "
                    + "\n             , trunc(a.act , 1)    act ";
                }

                sql +="\n             , trunc(a.study_count,1) as study_count "
                    + "\n             , trunc(a.study_time,1) as study_time "
                    + "\n             , trunc(a.score,1) as score " 
                    + "\n             , nvl(d.isgraduated,'M') as isgraduated "
                    + "\n             , d.notgraducd as notgraducd "
                    + "\n             , (select count(*) as totsulcnt "
                    + "\n                from   tz_subjseq "
                    + "\n                where  subj = c.subj "
                    + "\n                and    year = c.year "
                    + "\n                and    subjseq = c.subjseq "
                    + "\n                and    (nvl(sulpapernum,0) <> 0 or nvl(presulpapernum,0) <> 0 or nvl(aftersulpapernum,0) <> 0) "
                    + "\n               ) as totsulcnt "
                    + "\n             , (select count(lesson) as totexamcnt "
                    + "\n                from   tz_exampaper "
                    + "\n                where  subj    = c.subj "
                    + "\n                and    year    = c.year "
                    + "\n                and    subjseq = c.subjseq "
                    + "\n               ) as totexamcnt "
                    + "\n             , (select count(distinct projgubun) as totprojcnt "
                    + "\n                from   tz_projgrp "
                    + "\n                where  subj    = c.subj "
                    + "\n                and    year    = c.year "
                    + "\n                and    subjseq = c.subjseq "
                    + "\n                group  by subj, year, subjseq "
                    + "\n               ) as totprojcnt "
                    + "\n             , (select count(userid) as repsulcnt "
                    + "\n                from   tz_suleach "
                    + "\n                where  subj    = c.subj "
                    + "\n                and    year    = c.year "
                    + "\n                and    subjseq = c.subjseq "
                    + "\n                and    userid  = b.userid "
                    + "\n               ) as repsulcnt "
                    + "\n             , (select count(*) as repexamcnt "
                    + "\n                from   tz_examresult "
                    + "\n                where  subj    = c.subj "
                    + "\n                and    year    = c.year "
                    + "\n                and    subjseq = c.subjseq "
                    + "\n                and    userid  = b.userid "
                    + "\n               ) as repexamcnt "
                    + "\n             , nvl((select count(distinct grpseq) as repprojcnt "
                    + "\n                from   tz_projrep "
                    + "\n                where  subj    = c.subj "
                    + "\n                and    year    = c.year "
                    + "\n                and    subjseq = c.subjseq "
                    + "\n                and    projid  = b.userid "
                    + "\n                and    isfinal = 'Y' "
                    + "\n                group  by subj, year, subjseq, projid "
                    + "\n               ),0) as repprojcnt "
                    + "\n             , nvl((select count(distinct isret) "
                    + "\n                from   tz_projrep "
                    + "\n                where  subj    = c.subj "
                    + "\n                and    year    = c.year "
                    + "\n                and    subjseq = c.subjseq "
                    + "\n                and    projid  = b.userid "
                    + "\n                and    isret   = 'Y' "
                    + "\n                group  by grpseq " 
                    + "\n               ),0) as isretcnt "
                    + "\n                , c.edustart "
                	+ "\n                , c.eduend "
                	
                	+ "\n     , get_cpnm((select cp from tz_subj where subj=c.scsubj)) cpnm   "//cp업체 
                	+ "\n                , c.isgoyong "//환급여부
                	
                	+ "\n				,(SELECT view1.tstep "
					+ "\n				  FROM (SELECT ROUND ((a.completed_educheck_cnt / b.total_cnt) * 100, 2) AS tstep "
					+ "\n					            , a.userid, a.subj,a.subjseq, a.year  " 
					+ "\n						 FROM (SELECT   userid, subj, subjseq, YEAR "
					+ "\n									    ,COUNT (*) completed_educheck_cnt " 
					+ "\n							   FROM tz_progress "
					+ "\n					           WHERE first_end IS NOT NULL "
					+ "\n					           GROUP BY userid, subj, subjseq, YEAR) a ,"
					+ "\n					          (SELECT   COUNT (*) total_cnt, subj "
					+ "\n					           FROM tz_subjlesson "
					+ "\n					           WHERE lesson != '00' AND lesson != '99' "
					+ "\n					           GROUP BY subj) b "
					+ "\n					     WHERE a.subj = b.subj) view1 " 
					+ "\n				WHERE view1.userid = a.userid " 
					+ "\n					  AND view1.subj = a.subj " 
					+ "\n					  and view1.subjseq = a.subjseq "
					+ "\n					  and view1.year=a.year) tstep " // 진도율
                    + "\n        from    tz_student a "
                    + "\n              , tz_member b "
                    + "\n              , vz_scsubjseq c "
                    + "\n              , tz_stold d "
                    + "\n        where  a.userid  = b.userid "
                    + "\n        and    a.subj    = c.subj "
                    + "\n        and    a.year    = c.year "
                    + "\n        and    a.subjseq = c.subjseq "
	                + "\n        and    a.subj    = d.subj(+) "
	                + "\n        and    a.year    = d.year(+) "
	                + "\n        and    a.subjseq = d.subjseq(+) "
	                + "\n        and    a.userid  = d.userid(+) ";

                
                if ( !ss_grcode.equals("ALL"))     sql += "\n        and    c.grcode        	= " +SQLString.Format(ss_grcode);
                if ( !ss_gyear.equals("ALL"))      sql += "\n        and    c.gyear         	= " +SQLString.Format(ss_gyear);
                if ( !ss_grseq.equals("ALL"))      sql += "\n        and    c.grseq         	= " +SQLString.Format(ss_grseq);
                if ( !ss_uclass.equals("ALL"))     sql += "\n        and    c.scupperclass  	= " +SQLString.Format(ss_uclass);
                if ( !ss_mclass.equals("ALL"))     sql += "\n        and    c.scmiddleclass 	= " +SQLString.Format(ss_mclass);
                if ( !ss_lclass.equals("ALL"))     sql += "\n        and    c.sclowerclass  	= " +SQLString.Format(ss_lclass);
                if ( !ss_subjcourse.equals("ALL")) sql += "\n        and    c.scsubj        	= " +SQLString.Format(ss_subjcourse);
                if ( !ss_subjseq.equals("ALL"))    sql += "\n        and    c.scsubjseq     	= " +SQLString.Format(ss_subjseq);
                if ( !ss_company.equals("ALL"))    sql += "\n        and    b.comp				= " +SQLString.Format(ss_company);
                if ( !ss_class.equals("ALL"))      sql += "\n        and    b.class				= " +SQLString.Format(ss_class);
                if ( !v_searchtxt.equals("ZZZ") ) { 
	            	sql += "   and (b.userid like  '%" +v_searchtxt + "%' or b.name like '%" +v_searchtxt+"%') \n";
	            }  
                if ( ss_selGubun.equals("1"))      {  // 출석일
                    //sql += "\n        and    a.etc1 >= " + ss_selStart + " and a.etc1 <= " + ss_selEnd;
                	sql += "\n        and    (select count(*) as cnt  from  tz_attendance f where a.subj=f.subj and a.year=f.year and b.userid=f.userid and isattend ='O') >= " + ss_selStart + " and (select count(*) as cnt  from  tz_attendance f where a.subj=f.subj and a.year=f.year and b.userid=f.userid and isattend ='O') <= " + ss_selEnd;

                } else if ( ss_selGubun.equals("2") ) {  // 취득점수
                    sql += "\n        and    a.score >= " + ss_selStart + " and a.score <= " + ss_selEnd;
                } else if ( ss_selGubun.equals("3") ) {  // 과제
                    sql += "\n        and    a.avreport >= " + ss_selStart + " and a.avreport <= " + ss_selEnd;
                } else if ( ss_selGubun.equals("4") ) {  // 평가(최종)
                    sql += "\n        and    a.avftest  >= " + ss_selStart + " and a.avftest  <= " + ss_selEnd;
                }
                
                
                //sql +="\n        group  by C.course,C.cyear,C.courseseq,C.coursenm, c.subj, c.year, c.subjseq, c.subjseqgr, c.subjnm, c.edustart, c.eduend, c.isonoff, b.userid, b.name "
                //	+ "\n                , b.email, b.comp, b.position_nm, b.lvl_nm, a.samtotal "
                //	+ "\n                , d.isgraduated, c.grcode, c.gyear, c.grseq "
                	sql+= "\n       ) "
                	+ "\n where  1 =1 ";
                
                if ( v_orderColumn.equals("subj"))     		v_orderColumn =" subj";
                if ( v_orderColumn.equals("userid"))   		v_orderColumn =" userid";
                if ( v_orderColumn.equals("name"))     		v_orderColumn =" name";
                if ( v_orderColumn.equals("compnm"))   		v_orderColumn =" get_compnm(comp)";
                if ( v_orderColumn.equals("subjseq"))  		v_orderColumn =" subjseqgr ";
                if ( v_orderColumn.equals("isonoff"))  		v_orderColumn =" isonoff ";
                if ( v_orderColumn.equals("samtotal")) 		v_orderColumn =" samtotal ";
                if ( v_orderColumn.equals("repexamcnt")) 	v_orderColumn =" repexamcnt ";
                if ( v_orderColumn.equals("repsulcnt")) 	v_orderColumn =" repsulcnt ";
                if ( v_orderColumn.equals("repprojcnt")) 	v_orderColumn =" repprojcnt ";
                
                if ( !v_isexam.equals("ALL"))		sql +=" and    (totexamcnt - repexamcnt            ) "  + (v_isexam.equals("Y")   ? " = 0 " : " > 0 ");
                if ( !v_isreport.equals("ALL"))    	sql +=" and    (totprojcnt- repprojcnt + isretcnt  ) "  + (v_isreport.equals("Y") ? " = 0 " : " > 0 ");
                
                if ( !v_isgrad.equals("ALL") ) {      
                    sql += "\n and    isgraduated = "  + SQLString.Format(v_isgrad);
                }
                
                //if ( v_orderColumn.equals("") ) { 
                //	sql += "\n order  by subj, year, subjseq, name , userid     ";
                //} else { 
                //	sql += "\n order  by " + v_orderColumn + v_orderType + ", name, userid, subjnm ";
                //}
                
                if ( v_orderColumn.equals("") ) { 
                 // sql += " order by course, cyear, courseseq, userid, subjnm           ";
                	sql += "  order by name, examnum, birth_date, userid,course,cyear,courseseq           ";
                } else { 
                //  sql += " order by course, cyear, courseseq, userid, " + v_orderColumn + v_orderType;
                    sql += " order by  name, examnum, birth_date, userid, " + v_orderColumn + v_orderType;
                }                
                System.out.println("======================== 종합학습현황  ===============================\n" + sql);
                ls = connMgr.executeQuery(sql); 

                String[] arr_notgraducd;
                String v_notgradunm = "";
                while ( ls.next() ) { 
                    dbox = ls.getDataBox();

					// 미수료 사유
					arr_notgraducd = dbox.getString("d_notgraducd").split(",");
					for (int i=0; i<arr_notgraducd.length; i++) {
						if (i == 0) {
							v_notgradunm = GetCodenm.get_codenm(connMgr, "0028", arr_notgraducd[i]);
						} else {
							v_notgradunm += " , " + GetCodenm.get_codenm(connMgr, "0028", arr_notgraducd[i]);
						}
					}
					dbox.put("d_notgradunm", v_notgradunm);

                    dbox.put("d_tstep"	, new Double( ls.getDouble("tstep"))); 
                    dbox.put("d_avtstep", new Double( ls.getDouble("avtstep"))); 
                    dbox.put("d_report"	, new Double( ls.getDouble("report"))); 
                    dbox.put("d_act"	, new Double( ls.getDouble("act"))); 
                    dbox.put("d_mtest"	, new Double( ls.getDouble("mtest"))); 
                    dbox.put("d_ftest"	, new Double( ls.getDouble("ftest"))); 
                    dbox.put("d_score"	, new Double( ls.getDouble("score"))); 
                    
                    list.add(dbox);
                }
                
                for(int i=0;i < list.size(); i++){
                    dbox2       =   (DataBox)list.get(i);
                    v_course    =   dbox2.getString("d_course");
                    v_courseseq =   dbox2.getString("d_courseseq");
                    v_userid    =   dbox2.getString("d_userid");
                    if(!v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq) && v_Buserid.equals(v_userid))){
                        sql2 = "select count(*) cnt ";   
                        sql2+= "from TZ_STUDENT A,VZ_SCSUBJSEQ B,TZ_MEMBER C where 1 = 1  ";
            			if (!ss_grcode.equals("ALL")) {
            				sql2+= " and B.grcode = "+SQLString.Format(ss_grcode);
            			} 
            			if (!ss_gyear.equals("ALL")) {
            				sql2+= " and B.gyear = "+SQLString.Format(ss_gyear);
            			} 
            			if (!ss_grseq.equals("ALL")) {
            				sql2+= " and B.grseq = "+SQLString.Format(ss_grseq);
            			}
            			if (!ss_uclass.equals("ALL")) {
            				sql2+= " and B.scupperclass = "+SQLString.Format(ss_uclass);
            			}
            			if (!ss_mclass.equals("ALL")) {
            				sql2+= " and B.scmiddleclass = "+SQLString.Format(ss_mclass);
            			}
            			if (!ss_subjcourse.equals("ALL")) {
            				sql2+= " and B.scsubj = "+SQLString.Format(ss_subjcourse);
            			}
            			if (!ss_subjseq.equals("ALL")) {
            				sql2+= " and B.scsubjseq = "+SQLString.Format(ss_subjseq);
            			}
                        sql2+= " and A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq and A.userid=C.userid ";
                        sql2+= "and B.course = "+SQLString.Format(v_course)+" and B.courseseq = "+SQLString.Format(v_courseseq)
                            + " and c.userid = " + SQLString.Format(v_userid);
                        sql2+= " group by B.course,B.cyear,B.courseseq,B.coursenm,B.subj,B.year,B.subjnm,B.subjseq,A.userid,C.name ";
                        ls2 = connMgr.executeQuery(sql2);	
                        while(ls2.next()){      v_rowspan ++;      }
	                        dbox2.put("d_rowspan", new Integer(v_rowspan));
	                        dbox2.put("d_isnewcourse", "Y");  
	                        v_rowspan=0;
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
                
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
        } finally { 
            if ( ls != null )  { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

	/**
    종합 학습현황 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectTotalScoreStatusExcel(RequestBox box) throws Exception {
/*         
         DBConnectionManager    connMgr = null;
         ListSet ls      = null;

         ArrayList list  = null;
         String sql      = "";
         String sql1      = "";
         String sql2      = "";
         String sql3      = "";
         String sql4      = "";
         String sql5      = "";
         String sql6      = "";
         DataBox             dbox    = null;

         PreparedStatement pstmt1 = null;
         PreparedStatement pstmt2 = null;
         PreparedStatement pstmt3 = null;
         PreparedStatement pstmt4 = null;
         PreparedStatement pstmt5 = null;
         PreparedStatement pstmt6 = null;

         StudyStatusData data  = null;
         ProposeBean  probean = new ProposeBean();

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
         String  ss_samtotal   = box.getStringDefault("s_samtotal","ALL"); // 삼진아웃
         String  ss_goyong     = box.getStringDefault("s_goyong","ALL");   // 고용보험

         String  ss_action      = box.getString("s_action");
         String  v_orderColumn  = box.getString("p_orderColumn");           // 정렬할 컬럼명
         String v_orderType    = box.getString("p_orderType");           // 정렬할 순서

         String  v_subj             = "";// 과목&코스
         String  v_subjseq          = "";   // 과목 기수
         String  v_year             = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subjcourse, ss_subjseq);

         ManagerAdminBean bean = null;
         String  v_sql_add      = "";
         String  v_userid       = box.getSession("userid");
         String  s_gadmin       = box.getSession("gadmin");

         String v_totsulcnt = "";
         String v_repsulcnt = "";
         String v_totexamcnt = "";
         String v_repexamcnt = "";
         String v_totprojcnt = "";
         String v_repprojcnt = "";
         
         int    v_cnt1 = 0;
         int    v_cnt2 = 0;

         try { 
             if ( ss_action.equals("go") ) { 
                 connMgr = new DBConnectionManager();
                 list = new ArrayList();

                 // select compnm,jikwi,jikwinm,userid,cono,name,tstep,avtstep,
                 // report,act,mtest,ftest,score,point,comptel,email
 /* 2006.06.19 - 수정                
                 sql  = " select C.subj, C.year,  C.subjseq, C.subjseqgr, c.subjnm, c.isonoff, B.userid, B.cono, B.name,                 ";
                 sql += "        B.comptel, B.email, get_compnm(B.comp,2,2) companynm, get_deptnm(B.deptnam,'') compnm, a.samtotal,        ";
                 sql += "        B.jikwi, get_jikwinm(B.jikwi,B.comp) jikwinm, B.jikup, get_jikupnm(B.jikup,B.comp, B.jikupnm) jikupnm,  ";
                 sql += "        trunc(avg(A.tstep), 1) tstep, trunc(avg(A.avtstep), 1) avtstep, trunc(avg(A.avreport),1) report, trunc(avg(A.avact), 1) act, ";
                 sql += "        trunc(avg(A.avmtest), 1) mtest, trunc(avg(A.avftest),1) ftest,  trunc(avg(A.score),1) score, avg(C.point) point , A.isgraduated,    ";
                sql += "        e.sdate,(select count(*) totsulcnt from tz_subjseq                                                      ";
                 sql += "           where subj = C.subj and year = C.year and subjseq = C.subjseq                                        ";
                 sql += "             and nvl(sulpapernum,0) < > 0                                 ) totsulcnt,                           ";
                 sql += "         (select count(lesson) totexamcnt                                                                       ";
                 sql += "            from tz_exampaper                                                                                   ";
                 sql += "           where subj = C.subj and year = C.year and subjseq = C.subjseq ) totexamcnt,                          ";
                 sql += "          (select count(*) sangdamcount from tz_sangdam  where subj = C.subj and year = C.year and subjseq = C.subjseq and status = '1')                                                             sangdamcount,                       "; // 추가 이경배(상담상태조회)
                 sql += "         (select decode(count(projseq),'',0, count(projseq)) totprojcnt                                         ";
                 sql += "           from tz_projord                                                                                      ";
                 sql += "          where subj = C.subj and year = C.year and subjseq = C.subjseq                                         ";
                 sql += "            and projseq =  (select min(projseq) from tz_projord                                                 ";
                 sql += "                             where subj = C.subj and year = C.year and subjseq = C.subjseq)) totprojcnt,        ";
                 sql += "         (select count(userid) repsulcnt                                                                        ";
                 sql += "           from tz_suleach                                                                                      ";
                 sql += "           where subj = C.subj and year = C.year and subjseq = C.subjseq                                        ";
                 sql += "             and userid = B.userid                                       ) repsulcnt,                           ";
                 sql += "         (select  count(*) repexamcnt                                                                           ";
                 sql += "            from tz_examresult                                                                                  ";
                 sql += "           where subj = C.subj and year = C.year and subjseq = C.subjseq                                        ";
                 sql += "             and userid = B.userid                                       ) repexamcnt,                          ";
                 sql += "         (select count(projid) repprojcnt                                                                       ";
                 sql += "            from tz_projrep                                                                                     ";
                 sql += "            where subj = C.subj and year = C.year and subjseq = C.subjseq                                       ";
                 sql += "              and projid = B.userid                                      ) repprojcnt,                          ";
                 sql += "         ( select count(isret) from tz_projrep                                                                  ";
                 sql += "            where subj = C.subj and year = C.year and subjseq = C.subjseq and projid = B.userid                 ";
                sql += "              and isret = 'Y'                                            ) isretcnt,                            ";
                sql += "         ( select resend from tz_projrep                                                                  ";
                 sql += "            where subj = C.subj and year = C.year and subjseq = C.subjseq and projid = B.userid                 ";
                sql += "                                                                         ) resend ";
                 sql += "        from  TZ_STUDENT A, TZ_MEMBER B, VZ_SCSUBJSEQ C,                                                         ";
                 sql += "             ( select userid, subj, year, subjseq, max(sdate) sdate from TZ_SANGDAM                             ";
                 sql += "               group by userid, subj, year, subjseq ) E                                                         ";
                 sql += "        where A.userid=B.userid                                                                                 ";
                 sql += "          and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq                                           ";
                 sql += "          and A.subj = E.subj( +) and A.year = E.year( +) and A.subjseq = E.subjseq( +) and A.userid = E.userid( +) ";
                 
                 sql  = "SELECT                                                                               \n" +
                        "    C.subj,                                                                          \n" +
                        "    C.year,                                                                          \n" +
                        "    C.subjseq,                                                                       \n" +
                        "    C.subjseqgr,                                                                     \n" +
                        "    c.subjnm,                                                                        \n" +
                        "    c.isonoff,                                                                       \n" +
                        "    B.userid,                                                                        \n" +
                        "    B.name,                                                                          \n" +
                        "    B.comptel,                                                                       \n" +
                        "    B.email,                                                                         \n" +
                        "    a.samtotal,                                                                      \n" +
                        "    B.jikup,                                                                         \n" +
                        "    trunc(avg(A.tstep   ), 1)                                  tstep,                \n" +
                        "    trunc(avg(A.avtstep ), 1)                                  avtstep,              \n" ;
                        
                 if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // 가중치적용
                     sql += " trunc(avg(A.avreport), 1) report  ,   " +
                            " trunc(avg(A.avact   ), 1) act     ,   " +
                            " trunc(avg(A.avmtest ), 1) mtest   ,   " +
                            " trunc(avg(A.avftest ), 1) ftest   ,   ";
                 } else {                                                    // 가중치비적용
                     sql += " trunc(avg(A.report  ), 1)  report ,   " +
                            " trunc(avg(A.act     ), 1)  act    ,   " +
                            " trunc(avg(A.mtest   ), 1)  mtest  ,   " +
                            " trunc(avg(A.ftest   ), 1)  ftest  ,   ";
                 }
                  
                 sql += "    trunc(avg(A.score),1)                                     score,                 \n" +
                        "    avg(C.point)                                              point,                 \n" +
                        "    MAX(DECODE(A.isgraduated, 'N', CASE WHEN TO_CHAR(SYSDATE, 'YYYYMMDDHH24') BETWEEN c.EduStart AND c.EduEnd THEN '진행중'          \n" +
                        "                                        WHEN TO_CHAR(SYSDATE, 'YYYYMMDDHH24') > c.EduEnd   THEN 'N'                                 \n" +
                        "                                        WHEN TO_CHAR(SYSDATE, 'YYYYMMDDHH24') < c.EduStart THEN '대기중'                             \n" +
                        "                                        ELSE 'N'                                     \n" +
                        "                                   END                                               \n" +
                        "                           , 'Y'                                                     \n" +
                        "        )) isgraduated ,                                                             \n" +
                        "    e.sdate,                                                                         \n" +
                        "    (SELECT                                                                          \n" +
                        "         COUNT(*)                              totsulcnt                             \n" +
                        "     FROM                                                                            \n" +
                        "         tz_subjseq                                                                  \n" +
                        "     WHERE                                                                           \n" +
                        "             subj                = C.subj                                            \n" +
                        "         AND year                = C.year                                            \n" +
                        "         AND subjseq             = C.subjseq                                         \n" +
                        "         AND nvl(sulpapernum,0) <> 0                                                 \n" +
                        "    )                                                         totsulcnt,             \n" +
                        "    (SELECT                                                                          \n" +
                        "         COUNT(lesson)                         totexamcnt                            \n" +
                        "     FROM                                                                            \n" +
                        "         tz_exampaper                                                                \n" +
                        "     WHERE                                                                           \n" +
                        "             subj    = C.subj                                                        \n" +
                        "         AND year    = C.year                                                        \n" +
                        "         AND subjseq = C.subjseq                                                     \n" +
                        "    )                                                         totexamcnt,            \n" +
                        "    (SELECT                                                                          \n" +
                        "         COUNT(*)                              sangdamcount                          \n" +
                        "     FROM                                                                            \n" +
                        "         tz_sangdam                                                                  \n" +
                        "     WHERE                                                                           \n" +
                        "             subj    = C.subj                                                        \n" +
                        "          and year   = C.year                                                        \n" +
                        "         and subjseq = C.subjseq                                                     \n" +
                        "         and status  = '1'                                                           \n" +
                        "    )                                                         sangdamcount,          \n" +
                        "    (SELECT                                                                          \n" +
                        "         DECODE(COUNT(projseq), '', 0, COUNT(projseq)) totprojcnt                    \n" +
                        "     FROM                                                                            \n" +
                        "         tz_projord                                                                  \n" +
                        "     WHERE                                                                           \n" +
                        "             subj    = C.subj                                                        \n" +
                        "         AND year    = C.year                                                        \n" +
                        "         AND subjseq = C.subjseq                                                     \n" +
                        "         AND projseq = (SELECT                                                       \n" +
                        "                            MIN(projseq)                                             \n" +
                        "                        FROM                                                         \n" +
                        "                            tz_projord                                               \n" +
                        "                        WHERE                                                        \n" +
                        "                                subj    = C.subj                                     \n" +
                        "                            AND year    = C.year                                     \n" +
                        "                            AND subjseq = C.subjseq                                  \n" +
                        "                       )                                                             \n" +
                        "    )                                                         totprojcnt,            \n" +
                        "    (SELECT                                                                          \n" +
                        "         COUNT(userid)                         repsulcnt                             \n" +
                        "     FROM                                                                            \n" +
                        "         tz_suleach                                                                  \n" +
                        "     WHERE                                                                           \n" +
                        "             subj    = C.subj                                                        \n" +
                        "         AND year    = C.year                                                        \n" +
                        "         AND subjseq = C.subjseq                                                     \n" +
                        "         AND userid  = B.userid                                                      \n" +
                        "    )                                                         repsulcnt,             \n" +
                        "    (SELECT                                                                          \n" +
                        "         count(*)                              repexamcnt                            \n" +
                        "     FROM                                                                            \n" +
                        "         tz_examresult                                                               \n" +
                        "     WHERE                                                                           \n" +
                        "             subj    = C.subj                                                        \n" +
                        "         AND year    = C.year                                                        \n" +
                        "         AND subjseq = C.subjseq                                                     \n" +
                        "         AND userid  = B.userid                                                      \n" +
                        "    )                                                          repexamcnt,            \n" +
                        "    (SELECT                                                                          \n" +
                        "         COUNT(projid)                         repprojcnt                            \n" +
                        "     FROM                                                                            \n" +
                        "         tz_projrep                                                                  \n" +
                        "     WHERE                                                                           \n" +
                        "             subj    = C.subj                                                        \n" +
                        "         AND year    = C.year                                                        \n" +
                        "         AND subjseq = C.subjseq                                                     \n" +
                        "         AND projid  = B.userid                                                      \n" +
                        "    )                                                         repprojcnt,            \n" +
                        "    (SELECT                                                                          \n" +
                        "         COUNT(isret)                                                                \n" +
                        "     FROM                                                                            \n" +
                        "         tz_projrep                                                                  \n" +
                        "     WHERE                                                                           \n" +
                        "             subj    = C.subj                                                        \n" +
                        "         AND year    = C.year                                                        \n" +
                        "         AND subjseq = C.subjseq                                                     \n" +
                        "         AND projid  = B.userid                                                      \n" +
                        "         AND isret   = 'Y'                                                           \n" +
                        "    )                                                         isretcnt,              \n" +
                        "    (SELECT                                                                          \n" +
                        "         resend                                                                      \n" +
                        "     FROM                                                                            \n" +
                        "         tz_projrep                                                                  \n" +
                        "     WHERE                                                                           \n" +
                        "             subj    = C.subj                                                        \n" +
                        "         AND year    = C.year                                                        \n" +
                        "         AND subjseq = C.subjseq                                                     \n" +
                        "         AND projid  = B.userid                                                      \n" +
                        "    )                                                         resend                 \n" +
                        "FROM                                                                                 \n" +
                        "    TZ_STUDENT                                                A,                     \n" +
                        "    TZ_MEMBER                                                 B,                     \n" +
                        "    VZ_SCSUBJSEQ                                              C,                     \n" +
                        "    (SELECT                                                                          \n" +
                        "         userid,                                                                     \n" +
                        "         subj,                                                                       \n" +
                        "         year,                                                                       \n" +
                        "         subjseq,                                                                    \n" +
                        "         max(sdate)                            sdate                                 \n" +
                        "     FROM                                                                            \n" +
                        "         TZ_SANGDAM                                                                  \n" +
                        "     GROUP BY                                                                        \n" +
                        "         userid, subj, year, subjseq                                                 \n" +
                        "    )                                                         E                      \n" +
                        "WHERE                                                                                \n" +
                        "        A.userid  = B.userid                                                         \n" +
                        "    AND A.subj    = C.subj                                                           \n" +
                        "    AND A.year    = C.year                                                           \n" +
                        "    and A.subjseq = C.subjseq                                                        \n" +
                        "    AND A.subj    = E.subj( +)                                                       \n" +
                        "    AND A.year    = E.year( +)                                                       \n" +
                        "    and A.subjseq = E.subjseq( +)                                                    \n" +
                        "    and A.userid  = E.userid( +)                                                     \n" ;
                 
                 if ( !ss_grcode.equals("ALL"))     sql += " and C.grcode        = " +SQLString.Format(ss_grcode);
                 if ( !ss_gyear.equals("ALL"))      sql += " and C.gyear         = " +SQLString.Format(ss_gyear);
                 if ( !ss_grseq.equals("ALL"))      sql += " and C.grseq         = " +SQLString.Format(ss_grseq);
                 if ( !ss_uclass.equals("ALL"))     sql += " and C.scupperclass  = " +SQLString.Format(ss_uclass);
                 if ( !ss_mclass.equals("ALL"))     sql += " and C.scmiddleclass = " +SQLString.Format(ss_mclass);
                 if ( !ss_lclass.equals("ALL"))     sql += " and C.sclowerclass  = " +SQLString.Format(ss_lclass);
                 if ( !ss_subjcourse.equals("ALL")) sql += " and C.scsubj        = " +SQLString.Format(ss_subjcourse);
                 if ( !ss_subjseq.equals("ALL"))    sql += " and C.scsubjseq     = " +SQLString.Format(ss_subjseq);
                 if ( !ss_company.equals("ALL"))    sql += " and substr(B.comp, 0 ,4) = '" +ss_company.substring(0,4) + "'";
                 
                 if ( ss_selGubun.equals("1"))      {  // 진도율
                     sql += " and A.tstep >= " +SQLString.Format(ss_selStart) + " and A.tstep <= " +SQLString.Format(ss_selEnd);
                 } else if ( ss_selGubun.equals("2") ) {  // 취득점수
                     sql += " and A.score >= " +SQLString.Format(ss_selStart) + " and A.score <= " +SQLString.Format(ss_selEnd);
                 } else if ( ss_selGubun.equals("3") ) {  // 과제
                     sql += " and A.avreport >= " +SQLString.Format(ss_selStart) + " and A.avreport <= " +SQLString.Format(ss_selEnd);
                 } else if ( ss_selGubun.equals("4") ) {  // 평가(최종)
                     sql += " and A.avftest  >= " +SQLString.Format(ss_selStart) + " and A.avftest  <= " +SQLString.Format(ss_selEnd);
                 }
                 
                 if ( !ss_samtotal.equals("ALL"))    sql += " and A.samtotal = " + SQLString.Format(ss_samtotal);
                 if ( !ss_goyong.equals("ALL"))      sql += " and A.isgoyong = " + SQLString.Format(ss_goyong);
 /* 2006.06.19 - 수정                 
                 sql += " group by C.subj, C.year, C.subjseq, C.subjseqgr, c.subjnm, c.isonoff, B.userid, B.cono, B.name, B.comptel, B.email, B.comp, a.samtotal, ";
                 sql += "          get_compnm(B.comp,2,2), get_deptnm(B.deptnam,''), B.jikwi, get_jikwinm(B.jikwi,B.comp), B.jikup, get_jikupnm(B.jikup,B.comp, B.jikupnm) , A.isgraduated, e.sdate ";
 /
                 sql += "GROUP BY C.subj, C.year, C.subjseq, C.subjseqgr, c.subjnm, c.isonoff, B.userid, B.name, B.comptel, B.email, B.comp, a.samtotal, ";
                 sql += "          B.jikup, A.isgraduated, e.sdate ";
                 
                 if ( v_orderColumn.equals("subj"))     v_orderColumn =" c.subj";
                 if ( v_orderColumn.equals("userid"))   v_orderColumn =" b.userid";
                 if ( v_orderColumn.equals("name"))     v_orderColumn =" b.name";
                 if ( v_orderColumn.equals("compnm1"))  v_orderColumn =" get_compnm(b.comp,2,2)";
                 if ( v_orderColumn.equals("deptnam"))  v_orderColumn =" get_deptnm(B.deptnam,'')";
                 if ( v_orderColumn.equals("jiknm"))    v_orderColumn =" get_jikupnm(b.jikup,b.comp, b.jikupnm)";
                 if ( v_orderColumn.equals("subjseq"))  v_orderColumn =" c.subjseqgr ";
                 if ( v_orderColumn.equals("isonoff"))  v_orderColumn =" c.isonoff ";
                 if ( v_orderColumn.equals("samtotal")) v_orderColumn =" A.samtotal ";
                 
                 if ( v_orderColumn.equals("") ) { 
                     sql += " order by c.subj, c.year, c.subjseq, B.comp";
                 } else { 
                     sql += " order by " + v_orderColumn + v_orderType;
                 }
 System.out.println("sql ==  ==  ==  ==  ==  ==  == =종합학습현황 ==  == =" +sql);
                 ls = connMgr.executeQuery(sql);

                 while ( ls.next() ) { 
                     dbox = ls.getDataBox();
                                        v_subj      = dbox.getString("d_subj");
                                        v_year      = dbox.getString("d_year");
                                        v_subjseq = dbox.getString("d_subjseq");
                                        
                     dbox.put("d_tstep" , new Double( ls.getDouble("tstep"))); 
                     dbox.put("d_avtstep", new Double( ls.getDouble("avtstep"))); 
                     dbox.put("d_report"    , new Double( ls.getDouble("report"))); 
                     dbox.put("d_act"   , new Double( ls.getDouble("act"))); 
                     dbox.put("d_mtest" , new Double( ls.getDouble("mtest"))); 
                     dbox.put("d_ftest" , new Double( ls.getDouble("ftest"))); 
                     dbox.put("d_score" , new Double( ls.getDouble("score"))); 
                     
                     list.add(dbox);
                 }
             }
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
         } finally { 
             if ( ls != null )  { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
             if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e10 ) { } }
             if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e10 ) { } }
             if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e10 ) { } }
             if ( pstmt4 != null ) { try { pstmt4.close(); } catch ( Exception e10 ) { } }
             if ( pstmt5 != null ) { try { pstmt5.close(); } catch ( Exception e10 ) { } }
             if ( pstmt6 != null ) { try { pstmt6.close(); } catch ( Exception e10 ) { } }
         }

         return list;
*/
         DBConnectionManager    connMgr = null;
         ListSet ls      = null;

         ArrayList list  = null;
         String sql      = "";
         String sql1      = "";
         String sql2      = "";
         String sql3      = "";
         String sql4      = "";
         String sql5      = "";
         String sql6      = "";
         DataBox             dbox    = null;

         PreparedStatement pstmt1 = null;
         PreparedStatement pstmt2 = null;
         PreparedStatement pstmt3 = null;
         PreparedStatement pstmt4 = null;
         PreparedStatement pstmt5 = null;
         PreparedStatement pstmt6 = null;

         StudyStatusData data  = null;
         ProposeBean  probean = new ProposeBean();

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
         String  ss_samtotal   = box.getStringDefault("s_samtotal","ALL"); // 삼진아웃
         String  ss_goyong     = box.getStringDefault("s_goyong","ALL");   // 고용보험
         String  v_isgrad      = box.getStringDefault("p_isgrad"  , "ALL");   // 수료 여부
         String  v_isexam      = box.getStringDefault("p_isexam"  , "ALL");   // 평가 여부
         String  v_isreport    = box.getStringDefault("p_isreport", "ALL");   // 레포트 여부

         String  ss_action      = box.getString("s_action");
         String  v_orderColumn  = box.getString("p_orderColumn");           // 정렬할 컬럼명
         String v_orderType    = box.getString("p_orderType");           // 정렬할 순서

         String  v_subj             = "";// 과목&코스
         String  v_subjseq          = "";   // 과목 기수
         String  v_year             = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subjcourse, ss_subjseq);

         ManagerAdminBean bean = null;
         String  v_sql_add      = "";
         String  v_userid       = box.getSession("userid");
         String  s_gadmin       = box.getSession("gadmin");

         String v_totsulcnt = "";
         String v_repsulcnt = "";
         String v_totexamcnt = "";
         String v_repexamcnt = "";
         String v_totprojcnt = "";
         String v_repprojcnt = "";
         
         int    v_cnt1 = 0;
         int    v_cnt2 = 0;

         try { 
             if ( ss_action.equals("go") ) { 
                 connMgr = new DBConnectionManager();
                 list = new ArrayList();

                 // select compnm,jikwi,jikwinm,userid,cono,name,tstep,avtstep,
                 // report,act,mtest,ftest,score,point,comptel,email
 /* 2006.06.19 - 수정                
                 sql  = " select C.subj, C.year,  C.subjseq, C.subjseqgr, c.subjnm, c.isonoff, B.userid, B.cono, B.name,                 ";
                 sql += "        B.comptel, B.email, get_compnm(B.comp,2,2) companynm, get_deptnm(B.deptnam,'') compnm, a.samtotal,        ";
                 sql += "        B.jikwi, get_jikwinm(B.jikwi,B.comp) jikwinm, B.jikup, get_jikupnm(B.jikup,B.comp, B.jikupnm) jikupnm,  ";
                 sql += "        trunc(avg(A.tstep), 1) tstep, trunc(avg(A.avtstep), 1) avtstep, trunc(avg(A.avreport),1) report, trunc(avg(A.avact), 1) act, ";
                 sql += "        trunc(avg(A.avmtest), 1) mtest, trunc(avg(A.avftest),1) ftest,  trunc(avg(A.score),1) score, avg(C.point) point , A.isgraduated,    ";
                sql += "        e.sdate,(select count(*) totsulcnt from tz_subjseq                                                      ";
                 sql += "           where subj = C.subj and year = C.year and subjseq = C.subjseq                                        ";
                 sql += "             and nvl(sulpapernum,0) < > 0                                 ) totsulcnt,                           ";
                 sql += "         (select count(lesson) totexamcnt                                                                       ";
                 sql += "            from tz_exampaper                                                                                   ";
                 sql += "           where subj = C.subj and year = C.year and subjseq = C.subjseq ) totexamcnt,                          ";
                 sql += "          (select count(*) sangdamcount from tz_sangdam  where subj = C.subj and year = C.year and subjseq = C.subjseq and status = '1')                                                             sangdamcount,                       "; // 추가 이경배(상담상태조회)
                 sql += "         (select decode(count(projseq),'',0, count(projseq)) totprojcnt                                         ";
                 sql += "           from tz_projord                                                                                      ";
                 sql += "          where subj = C.subj and year = C.year and subjseq = C.subjseq                                         ";
                 sql += "            and projseq =  (select min(projseq) from tz_projord                                                 ";
                 sql += "                             where subj = C.subj and year = C.year and subjseq = C.subjseq)) totprojcnt,        ";
                 sql += "         (select count(userid) repsulcnt                                                                        ";
                 sql += "           from tz_suleach                                                                                      ";
                 sql += "           where subj = C.subj and year = C.year and subjseq = C.subjseq                                        ";
                 sql += "             and userid = B.userid                                       ) repsulcnt,                           ";
                 sql += "         (select  count(*) repexamcnt                                                                           ";
                 sql += "            from tz_examresult                                                                                  ";
                 sql += "           where subj = C.subj and year = C.year and subjseq = C.subjseq                                        ";
                 sql += "             and userid = B.userid                                       ) repexamcnt,                          ";
                 sql += "         (select count(projid) repprojcnt                                                                       ";
                 sql += "            from tz_projrep                                                                                     ";
                 sql += "            where subj = C.subj and year = C.year and subjseq = C.subjseq                                       ";
                 sql += "              and projid = B.userid                                      ) repprojcnt,                          ";
                 sql += "         ( select count(isret) from tz_projrep                                                                  ";
                 sql += "            where subj = C.subj and year = C.year and subjseq = C.subjseq and projid = B.userid                 ";
                sql += "              and isret = 'Y'                                            ) isretcnt,                            ";
                sql += "         ( select resend from tz_projrep                                                                  ";
                 sql += "            where subj = C.subj and year = C.year and subjseq = C.subjseq and projid = B.userid                 ";
                sql += "                                                                         ) resend ";
                 sql += "        from  TZ_STUDENT A, TZ_MEMBER B, VZ_SCSUBJSEQ C,                                                         ";
                 sql += "             ( select userid, subj, year, subjseq, max(sdate) sdate from TZ_SANGDAM                             ";
                 sql += "               group by userid, subj, year, subjseq ) E                                                         ";
                 sql += "        where A.userid=B.userid                                                                                 ";
                 sql += "          and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq                                           ";
                 sql += "          and A.subj = E.subj( +) and A.year = E.year( +) and A.subjseq = E.subjseq( +) and A.userid = E.userid( +) ";
 */                
                 
                 sql  = " SELECT *                                                                            \n" +
                 " FROM (                                                                               \n" +
                 " SELECT                                                                               \n" +
                        "    C.subj,                                                                          \n" +
                        "    C.year,                                                                          \n" +
                        "    C.subjseq,                                                                       \n" +
                        "    C.subjseqgr,                                                                     \n" +
                        "    c.subjnm,                                                                        \n" +
                        "    c.isonoff,                                                                       \n" +
                        "    B.userid,                                                                        \n" +
                        "    B.name,                                                                          \n" +
                        "    B.comptel,                                                                       \n" +
                        "    MAX(B.hometel) hometel,                                                                       \n" +
                        "    MAX(B.address1 || ' ' || B.address2) addr,                                             \n" +
                        "    MAX(B.handphone) handphone,                                                                     \n" +
                        "    B.email,                                                                         \n" +
                        "    a.samtotal,                                                                      \n" +
                        "    B.jikup,                                                                         \n" +
                        "    trunc(avg(A.tstep   ), 1)                                  tstep,                \n" +
                        "    trunc(avg(A.avtstep ), 1)                                  avtstep,              \n" ;
                        
                 if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // 가중치적용
                     sql += " trunc(avg(A.avreport), 1) report  ,   " +
                            " trunc(avg(A.avact   ), 1) act     ,   " +
                            " trunc(avg(A.avmtest ), 1) mtest   ,   " +
                            " trunc(avg(A.avftest ), 1) ftest   ,   ";
                 } else {                                                    // 가중치비적용
                     sql += " trunc(avg(A.report  ), 1)  report ,   " +
                            " trunc(avg(A.act     ), 1)  act    ,   " +
                            " trunc(avg(A.mtest   ), 1)  mtest  ,   " +
                            " trunc(avg(A.ftest   ), 1)  ftest  ,   ";
                 }
                  
                 sql += "    trunc(avg(A.score),1)                                     score,                 \n" +
                        "    avg(C.point)                                              point,                 \n" +
//                        "    a.isgraduated ,                                                                  \n" +
                        "    MAX(DECODE(A.isgraduated, 'N', CASE WHEN TO_CHAR(SYSDATE, 'YYYYMMDDHH24') BETWEEN c.EduStart AND c.EduEnd THEN '진행중'          \n" +
                        "                                        WHEN TO_CHAR(SYSDATE, 'YYYYMMDDHH24') > c.EduEnd   THEN 'N'                                 \n" +
                        "                                        WHEN TO_CHAR(SYSDATE, 'YYYYMMDDHH24') < c.EduStart THEN '대기중'                             \n" +
                        "                                        ELSE 'N'                                     \n" +
                        "                                   END                                               \n" +
                        "                           , 'Y'                                                     \n" +
                        "        )) isgraduated ,                                                             \n" +
                        "    (SELECT                                                                          \n" +
                        "         MAX(sdate)                              sdate                               \n" +
                        "     FROM                                                                            \n" +
                        "         tz_sangdam                                                                  \n" +
                        "     WHERE  ( subj, year, subjseq ) IN ( SELECT subj, year, subjseq                                        \n" + 
                        "                                         FROM   VZ_SCSUBJSEQ                                               \n" + 
                        "                                     where  grcode = c.grcode and gyear = c.gyear and grseq = c.grseq      \n" + 
                        "                                   )                                                                       \n" + 
                        "         and userid  = b.userid                                                      \n" +
//                        "         and status  = '1'                                                           \n" +
                        "    )                                                         sdate,          \n" +
//                        "    e.sdate,                                                                         \n" +
                        "    max(f.ldate)                                                     ldate_end,       \n" +
                        "    trunc( mod( (   sum(to_number(substr(f.total_time,1,2))*60*60)                    \n" +
                        "                  + sum( to_number(substr(f.total_time,4,2))*60)                      \n" +
                        "                  + sum(to_number(substr(f.total_time,7,2)))                          \n" +
                        "                )/60, 60 ), 0 )                                      total_minute,    \n" +
                        "    (SELECT                                                                           \n" +
                        "         COUNT(*)                              totsulcnt                              \n" +
                        "     FROM                                                                             \n" +
                        "         tz_subjseq                                                                   \n" +
                        "     WHERE                                                                            \n" +
                        "             subj                = C.subj                                             \n" +
                        "         AND year                = C.year                                             \n" +
                        "         AND subjseq             = C.subjseq                                          \n" +
                        "         AND nvl(sulpapernum,0) <> 0                                                  \n" +
                        "    )                                                         totsulcnt,              \n" +
                        "    (SELECT                                                                           \n" +
                        "         COUNT(lesson)                         totexamcnt                             \n" +
                        "     FROM                                                                             \n" +
                        "         tz_exampaper                                                                 \n" +
                        "     WHERE                                                                            \n" +
                        "             subj    = C.subj                                                         \n" +
                        "         AND year    = C.year                                                         \n" +
                        "         AND subjseq = C.subjseq                                                      \n" +
                        "    )                                                         totexamcnt,             \n" +
                        "    (SELECT                                                                           \n" +
                        "         COUNT(*)                              sangdamcount                           \n" +
                        "     FROM                                                                             \n" +
                        "         tz_sangdam                                                                   \n" +
                        "     WHERE  ( subj, year, subjseq ) IN ( SELECT subj, year, subjseq                                        \n" + 
                         "                                         FROM   VZ_SCSUBJSEQ                                               \n" + 
                         "                                     where  grcode = c.grcode and gyear = c.gyear and grseq = c.grseq      \n" + 
                         "                                   )                                                                       \n" + 
                        "         and userid  = b.userid                                                      \n" +
//                        "         and status  = '1'                                                           \n" +
                        "    )                                                         sangdamcount,          \n" +
                        "    (SELECT                                                                          \n" +
                        "         DECODE(COUNT(projseq), '', 0, COUNT(projseq)) totprojcnt                    \n" +
                        "     FROM                                                                            \n" +
                        "         tz_projord                                                                  \n" +
                        "     WHERE                                                                           \n" +
                        "             subj    = C.subj                                                        \n" +
                        "         AND year    = C.year                                                        \n" +
                        "         AND subjseq = C.subjseq                                                     \n" +
                        "         AND projseq = (SELECT                                                       \n" +
                        "                            MIN(projseq)                                             \n" +
                        "                        FROM                                                         \n" +
                        "                            tz_projord                                               \n" +
                        "                        WHERE                                                        \n" +
                        "                                subj    = C.subj                                     \n" +
                        "                            AND year    = C.year                                     \n" +
                        "                            AND subjseq = C.subjseq                                  \n" +
                        "                       )                                                             \n" +
                        "    )                                                         totprojcnt,            \n" +
                        "    (SELECT                                                                          \n" +
                        "         COUNT(userid)                         repsulcnt                             \n" +
                        "     FROM                                                                            \n" +
                        "         tz_suleach                                                                  \n" +
                        "     WHERE                                                                           \n" +
                        "             subj    = C.subj                                                        \n" +
                        "         AND year    = C.year                                                        \n" +
                        "         AND subjseq = C.subjseq                                                     \n" +
                        "         AND userid  = B.userid                                                      \n" +
                        "    )                                                         repsulcnt,             \n" +
                        "    (SELECT                                                                          \n" +
                        "         count(*)                              repexamcnt                            \n" +
                        "     FROM                                                                            \n" +
                        "         tz_examresult                                                               \n" +
                        "     WHERE                                                                           \n" +
                        "             subj    = C.subj                                                        \n" +
                        "         AND year    = C.year                                                        \n" +
                        "         AND subjseq = C.subjseq                                                     \n" +
                        "         AND userid  = B.userid                                                      \n" +
                        "    )                                                          repexamcnt,            \n" +
                        "    (SELECT                                                                          \n" +
                        "         COUNT(projid)                         repprojcnt                            \n" +
                        "     FROM                                                                            \n" +
                        "         tz_projrep                                                                  \n" +
                        "     WHERE                                                                           \n" +
                        "             subj    = C.subj                                                        \n" +
                        "         AND year    = C.year                                                        \n" +
                        "         AND subjseq = C.subjseq                                                     \n" +
                        "         AND projid  = B.userid                                                      \n" +
                        "    )                                                         repprojcnt,            \n" +
                        "    (SELECT                                                                          \n" +
                        "         COUNT(isret)                                                                \n" +
                        "     FROM                                                                            \n" +
                        "         tz_projrep                                                                  \n" +
                        "     WHERE                                                                           \n" +
                        "             subj    = C.subj                                                        \n" +
                        "         AND year    = C.year                                                        \n" +
                        "         AND subjseq = C.subjseq                                                     \n" +
                        "         AND projid  = B.userid                                                      \n" +
                        "         AND isret   = 'Y'                                                           \n" +
                        "    )                                                         isretcnt,              \n" +
                        "    (SELECT                                                                          \n" +
                        "         resend                                                                      \n" +
                        "     FROM                                                                            \n" +
                        "         tz_projrep                                                                  \n" +
                        "     WHERE                                                                           \n" +
                        "             subj    = C.subj                                                        \n" +
                        "         AND year    = C.year                                                        \n" +
                        "         AND subjseq = C.subjseq                                                     \n" +
                        "         AND projid  = B.userid                                                      \n" +
                        "    )                                                         resend                 \n" +
                        "FROM                                                                                 \n" +
                        "    TZ_STUDENT                                                A,                     \n" +
                        "    TZ_MEMBER                                                 B,                     \n" +
                        "    VZ_SCSUBJSEQ                                              C,                     \n" +
                        "    (SELECT                                                                          \n" +
                        "         userid,                                                                     \n" +
                        "         subj,                                                                       \n" +
                        "         year,                                                                       \n" +
                        "         subjseq,                                                                    \n" +
                        "         max(sdate)                            sdate                                 \n" +
                        "     FROM                                                                            \n" +
                        "         TZ_SANGDAM                                                                  \n" +
                        "     GROUP BY                                                                        \n" +
                        "         userid, subj, year, subjseq                                                 \n" +
                        "    )                                                         E,                     \n" +
                        "    tz_progress                                               f                      \n" +
                        "WHERE                                                                                \n" +
                        "        A.userid  = B.userid                                                         \n" +
                        "    AND A.subj    = C.subj                                                           \n" +
                        "    AND A.year    = C.year                                                           \n" +
                        "    and A.subjseq = C.subjseq                                                        \n" +
                        "    AND A.subj    = E.subj( +)                                                       \n" +
                        "    AND A.year    = E.year( +)                                                       \n" +
                        "    and A.subjseq = E.subjseq( +)                                                    \n" +
                        "    and A.userid  = E.userid( +)                                                     \n" +
                         "    AND A.subj    = f.subj( +)                                                       \n" +
                         "    AND A.year    = f.year( +)                                                       \n" +
                         "    and A.subjseq = f.subjseq( +)                                                    \n" +
                         "    and A.userid  = f.userid( +)                                                     \n" ;
                 
                 if ( !ss_grcode.equals("ALL"))     sql += " and C.grcode        = " +SQLString.Format(ss_grcode);
                 if ( !ss_gyear.equals("ALL"))      sql += " and C.gyear         = " +SQLString.Format(ss_gyear);
                 if ( !ss_grseq.equals("ALL"))      sql += " and C.grseq         = " +SQLString.Format(ss_grseq);
                 if ( !ss_uclass.equals("ALL"))     sql += " and C.scupperclass  = " +SQLString.Format(ss_uclass);
                 if ( !ss_mclass.equals("ALL"))     sql += " and C.scmiddleclass = " +SQLString.Format(ss_mclass);
                 if ( !ss_lclass.equals("ALL"))     sql += " and C.sclowerclass  = " +SQLString.Format(ss_lclass);
                 if ( !ss_subjcourse.equals("ALL")) sql += " and C.scsubj        = " +SQLString.Format(ss_subjcourse);
                 if ( !ss_subjseq.equals("ALL"))    sql += " and C.scsubjseq     = " +SQLString.Format(ss_subjseq);
                 if ( !ss_company.equals("ALL"))    sql += " and substr(B.comp, 0 ,4) = '" +ss_company.substring(0,4) + "'";
                 
                 if ( ss_selGubun.equals("1"))      {  // 진도율
                     sql += " and A.tstep >= " + ss_selStart + " and A.tstep <= " + ss_selEnd;
                 } else if ( ss_selGubun.equals("2") ) {  // 취득점수
                     sql += " and A.score >= " + ss_selStart + " and A.score <= " + ss_selEnd;
                 } else if ( ss_selGubun.equals("3") ) {  // 과제
                     sql += " and A.avreport >= " + ss_selStart + " and A.avreport <= " + ss_selEnd;
                 } else if ( ss_selGubun.equals("4") ) {  // 평가(최종)
                     sql += " and A.avftest  >= " + ss_selStart + " and A.avftest  <= " + ss_selEnd;
                 }
                 
                 if ( !ss_samtotal.equals("ALL"))    sql += " and A.samtotal = " + SQLString.Format(ss_samtotal);
                 if ( !ss_goyong.equals("ALL"))      sql += " and A.isgoyong = " + SQLString.Format(ss_goyong);
 /* 2006.06.19 - 수정                 
                 sql += " group by C.subj, C.year, C.subjseq, C.subjseqgr, c.subjnm, c.isonoff, B.userid, B.cono, B.name, B.comptel, B.email, B.comp, a.samtotal, ";
                 sql += "          get_compnm(B.comp,2,2), get_deptnm(B.deptnam,''), B.jikwi, get_jikwinm(B.jikwi,B.comp), B.jikup, get_jikupnm(B.jikup,B.comp, B.jikupnm) , A.isgraduated, e.sdate ";
 */
                 sql += "GROUP BY C.subj, C.year, C.subjseq, C.subjseqgr, c.subjnm, c.isonoff, B.userid, B.name, B.comptel, B.email, B.comp, a.samtotal, ";
                 sql += "          B.jikup, A.isgraduated, e.sdate, c.grcode, c.gyear, c.grseq ) where 1 =1 ";
                 
                 if ( v_orderColumn.equals("subj"))     v_orderColumn =" subj";
                 if ( v_orderColumn.equals("userid"))   v_orderColumn =" userid";
                 if ( v_orderColumn.equals("name"))     v_orderColumn =" name";
                 if ( v_orderColumn.equals("compnm1"))  v_orderColumn =" get_compnm(comp,2,2)";
                 if ( v_orderColumn.equals("deptnam"))  v_orderColumn =" get_deptnm(deptnam,'')";
                 if ( v_orderColumn.equals("jiknm"))    v_orderColumn =" get_jikupnm(jikup,comp, jikupnm)";
                 if ( v_orderColumn.equals("subjseq"))  v_orderColumn =" subjseqgr ";
                 if ( v_orderColumn.equals("isonoff"))  v_orderColumn =" isonoff ";
                 if ( v_orderColumn.equals("samtotal")) v_orderColumn =" samtotal ";
                 if ( v_orderColumn.equals("repexamcnt")) v_orderColumn =" repexamcnt ";
                 if ( v_orderColumn.equals("repsulcnt")) v_orderColumn =" repsulcnt ";
                 if ( v_orderColumn.equals("repprojcnt")) v_orderColumn =" (repprojcnt + isretcnt) ";
                 
                 if ( !v_isexam.equals("ALL"))      sql += " and (totexamcnt - repexamcnt            ) "  + (v_isexam.equals("Y")   ? " = 0 " : " > 0 ");
                 if ( !v_isreport.equals("ALL"))    sql += " and (totprojcnt- repprojcnt + isretcnt  ) "  + (v_isreport.equals("Y") ? " = 0 " : " > 0 ");
                 
                 
                 if ( v_isgrad.equals("Y") ) {      
                     sql += " and isgraduated   = "  + SQLString.Format(v_isgrad);
                 } else if ( v_isgrad.equals("N") ) {
                     sql += " and isgraduated   IN ('N', '진행중') \n";
                 }
                 
                 if ( v_orderColumn.equals("") ) { 
//                   sql += " order by c.subj, c.year, c.subjseq, B.comp ";
                   sql += " order by name , userid, subjnm           ";
               } else { 
                   sql += " order by " + v_orderColumn + v_orderType + ", name, userid, subjnm ";
               }
                 
                 ls = connMgr.executeQuery(sql);

                 while ( ls.next() ) { 
                     dbox = ls.getDataBox();
                    v_subj      = dbox.getString("d_subj");
                    v_year      = dbox.getString("d_year");
                    v_subjseq = dbox.getString("d_subjseq");
                                        
                     dbox.put("d_tstep" , new Double( ls.getDouble("tstep"))); 
                     dbox.put("d_avtstep", new Double( ls.getDouble("avtstep"))); 
                     dbox.put("d_report"    , new Double( ls.getDouble("report"))); 
                     dbox.put("d_act"   , new Double( ls.getDouble("act"))); 
                     dbox.put("d_mtest" , new Double( ls.getDouble("mtest"))); 
                     dbox.put("d_ftest" , new Double( ls.getDouble("ftest"))); 
                     dbox.put("d_score" , new Double( ls.getDouble("score"))); 
                     
                     list.add(dbox);
                 }
             }
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
         } finally { 
             if ( ls != null )  { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
             if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e10 ) { } }
             if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e10 ) { } }
             if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e10 ) { } }
             if ( pstmt4 != null ) { try { pstmt4.close(); } catch ( Exception e10 ) { } }
             if ( pstmt5 != null ) { try { pstmt5.close(); } catch ( Exception e10 ) { } }
             if ( pstmt6 != null ) { try { pstmt6.close(); } catch ( Exception e10 ) { } }
         }

         return list;
    }

    /**
    소속별 학습현황 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectAssignmentLearningList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls      = null;
        ArrayList list  = null;
        String sql      = "";
        StudyStatusData data  = null;
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수

        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");    // 과목분류
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류

        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_company  = box.getStringDefault("s_company","ALL");   // 회사
        String  ss_selgubun = box.getString("s_selgubun");               // 직군별:JIKUN,직급별:JIKUP,사업부별:GPM
        String  ss_seltext  = box.getStringDefault("s_seltext","ALL");   // 검색분류별 검색내용
        String  ss_seldept  = box.getStringDefault("s_seldept","ALL");   // 사업부별 부서 검색내용
        String  ss_action   = box.getString("s_action");
        String  v_orderColumn= box.getString("p_orderColumn");           // 정렬할 컬럼명

        ManagerAdminBean bean = null;
        String  v_sql_add   = "";
        String  v_userid    = box.getSession("userid");
        String  s_gadmin    = box.getSession("gadmin");

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list = new ArrayList();

                // select compnm,jikwi,jikwinm,userid,cono,name,tstep,avtstep,
                // report,act,mtest,ftest,score,point,comptel,email
                sql = "select get_compnm(B.comp,2,4) compnm, B.jikwi,get_jikwinm(B.jikwi,B.comp) jikwinm,   ";
                sql += "B.jikup, get_jikupnm(B.jikup, B.comp) jikupnm , B.userid,B.cono,B.name,              ";
                sql += "avg(A.tstep) tstep,avg(A.avtstep) avtstep,                                           ";
                if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // 가중치적용
                    sql += "avg(A.avreport) report,avg(A.avact) act,avg(A.avmtest) mtest,avg(A.avftest) ftest, ";
                } else {                                                  // 가중치비적용
                    sql += "avg(A.report) report,avg(A.act) act,avg(A.mtest) mtest,avg(A.ftest) ftest, ";
                }
                sql += "avg(A.score) score,B.comptel,B.email,avg(C.point) point  ";
                sql += "from TZ_STUDENT A,TZ_MEMBER B,VZ_SCSUBJSEQ C,TZ_COMP D where 1=1 ";

                if ( !ss_grcode.equals("ALL") ) { 
                    sql += " and C.grcode = " +SQLString.Format(ss_grcode);
                }
                if ( !ss_gyear.equals("ALL") ) { 
                    sql += " and C.gyear = " +SQLString.Format(ss_gyear);
                }
                if ( !ss_grseq.equals("ALL") ) { 
                    sql += " and C.grseq = " +SQLString.Format(ss_grseq);
                }
                if ( !ss_uclass.equals("ALL") ) { 
                    sql += " and C.scupperclass = " +SQLString.Format(ss_uclass);
                }
                if ( !ss_mclass.equals("ALL") ) { 
                    sql += " and C.scmiddleclass = " +SQLString.Format(ss_mclass);
                }

                if ( !ss_lclass.equals("ALL") ) { 
                    sql += " and C.sclowerclass = " +SQLString.Format(ss_lclass);
                }

                if ( !ss_subjcourse.equals("ALL") ) { 
                    sql += " and C.scsubj = " +SQLString.Format(ss_subjcourse);
                }
                if ( !ss_subjseq.equals("ALL") ) { 
                    sql += " and C.scsubjseq = " +SQLString.Format(ss_subjseq);
                }
                if ( !ss_company.equals("ALL") ) { 
                    sql += " and substr(B.comp, 0 ,4) = '" +ss_company.substring(0,4) + "'";
                }

                // 부서장일경우
                if ( s_gadmin.equals("K6") || s_gadmin.equals("K7") ) { 
                    bean = new ManagerAdminBean();
                    v_sql_add   = bean.getManagerDept(v_userid, s_gadmin);
                    if ( !v_sql_add.equals("")) sql += " and B.comp in " + v_sql_add;       // 관리부서검색조건쿼리
                }

                if ( ss_selgubun.equals("JIKUN") && !ss_seltext.equals("ALL"))      {  // 직군별
                    sql += " and B.jikun = " +SQLString.Format(ss_seltext);
                } else if ( ss_selgubun.equals("JIKUP") && !ss_seltext.equals("ALL") ) {  // 직급별
                    sql += " and B.jikup = " +SQLString.Format(ss_seltext);
                } else if ( ss_selgubun.equals("GPM") && !ss_seltext.equals("ALL") ) {  // 사업부별
                    sql += " and B.comp like " +SQLString.Format(GetCodenm.get_compval(ss_seltext));
                    if ( !ss_seldept.equals("ALL") ) {  sql += " and D.dept = " +SQLString.Format(ss_seldept);   }
                }
                sql += " and B.comp=D.comp and A.userid=B.userid and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq ";
                sql += " group by get_compnm(B.comp,2,4),B.jikwi,get_jikwinm(B.jikwi,B.comp),B.jikup,get_jikupnm(B.jikup,B.comp),B.userid,B.cono,B.name,B.comptel,B.email,B.comp";
                if ( !v_orderColumn.equals("") ) { 
                    v_orderColumn = "B." +v_orderColumn;
                    sql += " order by " +v_orderColumn;
                } else { 
                    sql += " order by B.comp,B.jikup,B.userid ";
                }
                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    data = new StudyStatusData();
                    data.setCompnm( ls.getString("compnm") );
                    data.setJikwi( ls.getString("jikwi") );
                    data.setJikwinm( ls.getString("jikwinm") );
                    data.setJikup( ls.getString("jikup") );
                    data.setJikupnm( ls.getString("jikupnm") );
                    data.setUserid( ls.getString("userid") );
                    data.setCono( ls.getString("cono") );
                    data.setName( ls.getString("name") );
                    data.setTstep( ls.getInt("tstep") );
                    data.setAvtstep( ls.getInt("avtstep") );
                    data.setReport( ls.getInt("report") );
                    data.setAct( ls.getInt("act") );
                    data.setMtest( ls.getInt("mtest") );
                    data.setFtest( ls.getInt("ftest") );
                    data.setScore( ls.getInt("score") );
                    data.setPoint( ls.getInt("point") );
                    data.setComptel( ls.getString("comptel") );
                    data.setEmail( ls.getString("email") );
                    list.add(data);
                }
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
    교육그룹별 학습현황 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectGrcodeLearningList(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet ls1         = null;
         ListSet ls3         = null;         
         ArrayList list1     = null;
         ArrayList list3     = null;         
         String sql1         = "";
         String sql3         = "";         
         StudyStatusData data1= null;
         StudyStatusData data3= null;
         String  v_Bcourse   = ""; // 이전코스
         String  v_course    = ""; // 현재코스
         String  v_Bcourseseq= ""; // 이전코스기수
         String  v_courseseq = ""; // 현재코스기수
//         int     l           = 0;
         String  ss_grcode   = box.getStringDefault("s_grcode","ALL");   // 교육그룹
         String  ss_gyear    = box.getStringDefault("s_gyear","ALL");    // 년도
         String  ss_grseq    = box.getStringDefault("s_grseq","ALL");    // 교육기수

         String v_orderColumn   = box.getString("p_orderColumn");
         String v_orderType     = box.getString("p_orderType");                 // 정렬할 순서

         String  ss_action   = box.getString("s_action");

         int     v_rowspan   = 0;
         try { 
             if ( ss_action.equals("go") ) { 
                 connMgr = new DBConnectionManager();
                 list1 = new ArrayList();
                 list3 = new ArrayList();                 

                 sql1="\n select c.grseq , d.grseqnm, c.course, c.cyear, c.courseseq, c.coursenm "
                	 +"\n      , c.subj, c.year, c.subjseq, c.subjnm, c.subjseqgr "
                	 +"\n      , get_codenm('0004',c.isonoff) as isonoff, count(a.subj) as educnt"
                	 +"\n      , avg(a.tstep) tstep, avg(a.avtstep) avtstep, avg(a.act) act ";
                 
                 if ( GetCodenm.get_config("score_disp").equals("WS") ) {    // 가중치적용
                     sql1+="\n      , avg(a.avreport) report "
                    	 + "\n      , avg(a.avmtest) mtest "
                    	 + "\n      , avg(a.avftest) ftest "
                    	 + "\n      , avg(a.avetc1) etc1 "
                    	 + "\n      , avg(a.avetc2) etc2 ";
                 } else {                                                  // 가중치비적용
                     sql1+="\n      , avg(a.report) report "
                    	 + "\n      , avg(a.mtest) mtest "
                    	 + "\n      , avg(a.ftest) ftest "
                    	 + "\n      , avg(a.etc1) etc1 "
                    	 + "\n      , avg(a.etc2) etc2 ";
                 }
  
                 sql1+="\n      , avg(a.score) score "
                	 + "\n      , avg(a.study_count) study_count, avg(a.study_time) study_time "
                	 + "\n      , sum((select count(*) "
                	 + "\n             from   tz_stold "
                	 + "\n             where  subj = a.subj "
                	 + "\n             and    year = a.year "
                	 + "\n             and    subjseq = a.subjseq "
                	 + "\n             and    userid = a.userid "
                	 + "\n             and    isgraduated = 'Y')) as gradcnt "
                	 + "\n from   tz_student a,vz_scsubjseq c, tz_grseq d "
                	 + "\n where  a.subj = c.subj "
                	 + "\n and    a.year = c.year "
                	 + "\n and    a.subjseq = c.subjseq "
                	 + "\n and    c.grcode = d.grcode "
                	 + "\n and    c.gyear = d.gyear "
                	 + "\n and    c.grseq=d.grseq ";

                 if ( !ss_grcode.equals("ALL")) sql1 += "\n and    c.grcode = " +SQLString.Format(ss_grcode);
                 if ( !ss_gyear.equals("ALL"))  sql1 += "\n and    c.gyear = " +SQLString.Format(ss_gyear);
                 if ( !ss_grseq.equals("ALL"))  sql1 += "\n and    c.grseq = " +SQLString.Format(ss_grseq);

                 sql1+="\n group  by c.grseq, d.grseqnm, c.course, c.cyear, c.courseseq, c.coursenm "
                	 + "\n         , c.subj, c.year, c.subjseq, c.subjnm, c.subjseqgr "
                	 + "\n         , c.isonoff ";

                 if ( v_orderColumn.equals("subj"))     v_orderColumn = "c.subj";
                 if ( v_orderColumn.equals("grseq"))    v_orderColumn   = "c.grseq";
                 if ( v_orderColumn.equals("subjseq"))  v_orderColumn =" c.subjseqgr ";
                 if ( v_orderColumn.equals("isonoff"))  v_orderColumn =" C.isonoff ";

                 //if ( v_orderColumn.equals("") ) { 
                 //    sql1 += "\n order  by c.subj, c.year, c.subjseq, subjnm";
                 //} else { 
                 //    sql1 += "\n order  by " + v_orderColumn + v_orderType;
                 //}
                 
                 if ( v_orderColumn.equals("") ) { 
                     sql1 += " order by C.course, c.cyear, c.courseseq, C.subj,C.year,C.subjseq, subjnm";
                 } else { 
                     sql1 += " order by C.course, c.cyear, c.courseseq, " + v_orderColumn + v_orderType;
                 }                 

                 ls1 = connMgr.executeQuery(sql1);

                 while ( ls1.next() ) { 
                     data1 = new StudyStatusData();

                     data1.setGradcnt(ls1.getInt("gradcnt") );
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
                     data1.setEtc1(ls1.getInt("etc1"));
                     data1.setEtc2(ls1.getInt("etc2"));
                     data1.setAct( ls1.getInt("act") );
                     data1.setMtest( ls1.getInt("mtest") );
                     data1.setFtest( ls1.getInt("ftest") );
                     data1.setScore( ls1.getInt("score") );
                     data1.setIsonoff( ls1.getString("isonoff") );
                     // data1.setDistcode1avg( ls1.getDouble("distcode1_avg") );
                     data1.setStudy_count(ls1.getInt("study_count"));
                     data1.setStudy_time(ls1.getInt("study_time"));
                     
                     list1.add(data1);
                 }

                 
                 for ( int i = 0;i < list1.size(); i++ ) { 
                     v_rowspan   =   0;
                     data3       =   (StudyStatusData)list1.get(i);
                     v_course    =   data3.getCourse();
                     v_courseseq =   data3.getCourseseq();
                     if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq))) { 
                         sql3 = "select count(C.subj) cnt ";
                         sql3 += "  from TZ_STUDENT A,VZ_SCSUBJSEQ C, tz_grseq D                     ";
                         sql3 += " where A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq     ";
                         sql3 += "   and c.grcode=d.grcode and c.gyear=d.gyear and c.grseq=d.grseq   ";

                         if ( !ss_grcode.equals("ALL")) sql3 += " and C.grcode = " +SQLString.Format(ss_grcode);
                         if ( !ss_gyear.equals("ALL"))  sql3 += " and C.gyear = " +SQLString.Format(ss_gyear);
                         if ( !ss_grseq.equals("ALL"))  sql3 += " and C.grseq = " +SQLString.Format(ss_grseq);
                         
                         sql3 += " and A.subj=C.subj and A.year=C.year and A.subjseq=C.subjseq ";
                         sql3 += " and C.course = " +SQLString.Format(v_course) + " and C.courseseq = " +SQLString.Format(v_courseseq);
                         sql3 += " group by  C.course,C.cyear,C.courseseq,C.coursenm,C.subj,C.subjseq,C.subjnm, c.subjseqgr, C.isonoff,C.year, c.grseq,  D.grseqnm    ";
                         sql3 += " order by C.course,C.cyear,C.courseseq,C.subj,C.year,C.subjseq ";
//                         
                         ls3 = connMgr.executeQuery(sql3);

                         while ( ls3.next() ) {      v_rowspan++;      }
                         data3.setRowspan(v_rowspan);
                         data3.setIsnewcourse("Y");
                     } else { 
                         data3.setRowspan(0);
                         data3.setIsnewcourse("N");
                     }
                     v_Bcourse   =   v_course;
                     v_Bcourseseq=   v_courseseq;
                     list3.add(data3);
                 }

             }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls3 != null ) { try { ls3.close(); } catch ( Exception e ) { } }            
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
	/**
    학습시간 조회 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectLearningTimeList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;        
        ArrayList list1     = null;
        ArrayList list2     = null;        
        String sql1         = "";
        String sql2         = "";
        DataBox dbox 		= null;
        DataBox dbox2 		= null;        
        //StudyStatusData data1= null;
        //StudyStatusData data2= null;
        String  v_Bcourse   = ""; // 이전코스
        String  v_course    = ""; // 현재코스
        String  v_Bcourseseq= ""; // 이전코스기수
        String  v_courseseq = ""; // 현재코스기수
        String  v_Buserid = "";
        
        int     l           = 0;
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수
        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");    // 과목분류
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_company  = box.getStringDefault("s_company","ALL");   // 회사
        String  ss_action   = box.getString("s_action");
        String  v_orderColumn= box.getString("p_orderColumn");           // 정렬할 컬럼명
        String v_orderType     = box.getString("p_orderType");           // 정렬할 순서
        String  v_searchtxt = box.getStringDefault("p_searchtext1", "ZZZ");    // id or name 검색
        int     v_rowspan   = 0;

        ManagerAdminBean bean = null;
        String  v_sql_add   = "";
        String  v_userid    = box.getSession("userid");
        String  s_gadmin    = box.getSession("gadmin");

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                list2 = new ArrayList();                
/* 2006.06.19 - 수정
                // select course,cyear,courseseq,coursenm,subj,year,subjnm,subjseq,
                // userid,name,first_edu,ldate_start,ldate_end,total_time,total_minute,isonoff
                sql1 = "select B.course,B.cyear,B.courseseq,B.coursenm,B.subj,B.year,B.subjnm,B.subjseq,B.subjseqgr, c.userid,C.name,        ";
                sql1 += "       get_compnm(c.comp,2,2) companynm, get_deptnm(c.deptnam,'') compnm, c.jikwi,get_jikwinm(c.jikwi,c.comp) jikwinm, ";
                sql1 += "min(to_number(A.first_edu)) first_edu, max(A.ldate) ldate_end, min(A.ldate) ldate_start,            ";
                // sql1 += "  trunc( ( sum(to_number(substr(A.total_time,1,2))*60*60            ";
                // sql1 += "      + to_number(substr(A.total_time,4,2))*60                      ";
                // sql1 += "      + to_number(substr(A.total_time,7,2))                         ";
                // sql1 += "      ) / (60*60) ),0) total_time,                                  ";
                // sql1 += "  trunc ( sum(to_number(substr(A.total_time,1,2))*60*60             ";
                // sql1 += "      + to_number(substr(A.total_time,4,2))*60                      ";
                // sql1 += "      + to_number(substr(A.total_time,7,2))                         ";
                // sql1 += "      ) / 60) total_minute,                                         ";
                // sql1 += "  mod ( sum(to_number(substr(A.total_time,1,2))*60*60               ";
                // sql1 += "      + to_number(substr(A.total_time,4,2))*60                      ";
                // sql1 += "      + to_number(substr(A.total_time,7,2))                         ";
                // sql1 += "      ) , 60) total_sec,";
                sql1 +=" trunc( ( sum(to_number(substr(A.total_time,1,2))) *60*60 + sum(to_number(substr(A.total_time,4,2)))*60 + sum(to_number(substr(A.total_time,7,2))) ) / (60*60) ,0) total_time,     ";
                sql1 +=" trunc( mod( (sum(to_number(substr(A.total_time,1,2))*60*60) + sum( to_number(substr(A.total_time,4,2))*60) + sum(to_number(substr(A.total_time,7,2))) )/60, 60), 0 ) total_minute,";
                sql1 +=" mod ( sum(to_number(substr(A.total_time,1,2))*60*60 + to_number(substr(A.total_time,4,2))*60 + to_number(substr(A.total_time,7,2))) , 60) total_sec, ";
                sql1 +=" B.isonoff                                   ";
                sql1 += "from TZ_PROGRESS A,VZ_SCSUBJSEQ B,TZ_MEMBER C , tz_student d where 1 = 1            ";
*/
  
                sql1 = "SELECT                                                                                \n" +
                       "    B.course,                                                                         \n" +
                       "    B.cyear,                                                                          \n" +
                       "    B.courseseq,                                                                      \n" +
                       "    B.coursenm,                                                                       \n" +
                       "    B.subj,                                                                           \n" +
                       "    B.year,                                                                           \n" +
                       "    B.subjnm,                                                                         \n" +
                       "    B.subjseq,                                                                        \n" +
                       "    B.subjseqgr,                                                                      \n" +
                       "    c.userid,                                                                         \n" +
                       "    C.name,                                                                           \n" +
                       "    min(A.first_edu) 			                                     first_edu,       \n" +
                       "    max(A.ldate)                                                     ldate_end,       \n" +
                       "    min(A.ldate)                                                     ldate_start,     \n" +
                       "    trunc( (   sum(nvl(to_number(substr(replace(trim(A.total_time),':',' ') ,1,2)),0))*60*60                         \n" +
                       "             + sum(nvl(to_number(substr(replace(trim(A.total_time),':',' ') ,4,2)),0))*60                            \n" +
                       "             + sum(nvl(to_number(substr(replace(trim(A.total_time),':',' ') ,7,2)),0))                               \n" +
                       "           ) / (60*60) ,0 )                                          total_time,      \n" +
                       "    trunc( mod( (   sum(nvl(to_number(substr(replace(trim(A.total_time),':',' ') ,1,2)),0)*60*60)                    \n" +
                       "                  + sum(nvl(to_number(substr(replace(trim(A.total_time),':',' ') ,4,2)),0)*60)                      \n" +
                       "                  + sum(nvl(to_number(substr(replace(trim(A.total_time),':',' ') ,7,2)),0))                          \n" +
                       "                )/60, 60 ), 0 )                                      total_minute,    \n" +
                       "    mod ( sum(   nvl(to_number(substr(replace(trim(A.total_time),':',' ') ,1,2)),0)*60*60                            \n" +
                       "               + nvl(to_number(substr(replace(trim(A.total_time),':',' ') ,4,2)),0)*60                               \n" +
                       "               + nvl(to_number(substr(replace(trim(A.total_time),':',' ') ,7,2)),0)                                  \n" +
                       "              ) , 60)                                                total_sec,       \n" +
                       "    B.isonoff,                                                                        \n" +
                       "    get_compnm(c.comp) as companynm,                                                  \n" +
                       "    c.position_nm,					                                                  \n" +
                       "    c.lvl_nm					                                                 	  \n" +                       
                       "FROM                                                                                  \n" +
                       "    TZ_PROGRESS                                                      A,               \n" +
                       "    VZ_SCSUBJSEQ                                                     B,               \n" +
                       "    TZ_MEMBER                                                        C,               \n" +
                       "    tz_student                                                       d                \n" +
                       "WHERE                                                                                 \n" +
                       "        1 = 1                                                                         \n" ;
                
                if ( !ss_grcode.equals("ALL"))     sql1 += " and B.grcode = " +SQLString.Format(ss_grcode);
                if ( !ss_gyear.equals("ALL"))      sql1 += " and B.gyear = " +SQLString.Format(ss_gyear);
                if ( !ss_grseq.equals("ALL"))      sql1 += " and B.grseq = " +SQLString.Format(ss_grseq);
                if ( !ss_uclass.equals("ALL"))     sql1 += " and B.scupperclass = " +SQLString.Format(ss_uclass);
                if ( !ss_mclass.equals("ALL"))     sql1 += " and B.scmiddleclass = " +SQLString.Format(ss_mclass);
                if ( !ss_lclass.equals("ALL"))     sql1 += " and B.sclowerclass = " +SQLString.Format(ss_lclass);
                if ( !ss_subjcourse.equals("ALL")) sql1 += " and B.scsubj = " +SQLString.Format(ss_subjcourse);
                if ( !ss_subjseq.equals("ALL"))    sql1 += " and B.scsubjseq = " +SQLString.Format(ss_subjseq);
                if ( !ss_company.equals("ALL"))    sql1 += " and C.comp = '" +ss_company + "'";
                if ( !v_searchtxt.equals("ZZZ") ) { 
	            	sql1 += "   and (c.userid like  '%" +v_searchtxt + "%' or c.name like '%" +v_searchtxt+"%') \n";
	            }  

                // 부서장일경우
                if ( s_gadmin.equals("K6") || s_gadmin.equals("K7") ) { 
                    bean = new ManagerAdminBean();
                    v_sql_add   = bean.getManagerDept(v_userid, s_gadmin);
                    if ( !v_sql_add.equals("")) sql1 += " and C.comp in " + v_sql_add;       // 관리부서검색조건쿼리
                }

                // sql1 += " and A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq and A.userid=C.userid ";
                sql1 += " and D.userid  = C.userid ";
                sql1 += " and D.subj    = B.subj ";
                sql1 += " and D.year    = B.year ";
                sql1 += " and D.subjseq = B.subjseq ";
                sql1 += " and D.subj    = A.subj( +) ";
                sql1 += " and D.subjseq = A.subjseq( +) ";
                sql1 += " and D.year    = A.year( +) ";
                sql1 += " and D.userid  = A.userid( +) ";

/* 2006.06.19 - 수정
                sql1 += " group by B.course,B.cyear,B.courseseq,B.coursenm,B.subj,B.year,B.subjnm,B.subjseq,B.subjseqgr, c.userid,C.name,B.isonoff, ";
                sql1 += "          get_compnm(c.comp,2,2), get_deptnm(c.deptnam,''), c.jikwi, get_jikwinm(c.jikwi,c.comp)     ";
*/                
                sql1 += "GROUP BY B.course,B.cyear,B.courseseq,B.coursenm,B.subj,B.year,B.subjnm,B.subjseq,B.subjseqgr, " +
                        "           c.userid,C.name,B.isonoff, get_compnm(c.comp), c.position_nm, c.lvl_nm ";

                if ( v_orderColumn.equals("subj"))     v_orderColumn =" b.subj";
                if ( v_orderColumn.equals("userid"))   v_orderColumn =" c.userid";
                if ( v_orderColumn.equals("name"))     v_orderColumn =" c.name";
                if ( v_orderColumn.equals("compnm1"))  v_orderColumn =" get_compnm(c.comp)";
                if ( v_orderColumn.equals("subjseq"))  v_orderColumn =" b.subjseqgr ";
                if ( v_orderColumn.equals("isonoff"))  v_orderColumn =" b.isonoff ";

                //if ( v_orderColumn.equals("") ) { 
                //    sql1 += " order by b.subj, b.year, b.subjseq,b.subjnm";
                //} else { 
                //    sql1 += " order by " + v_orderColumn + v_orderType;
                //}
                
                if ( v_orderColumn.equals("") ) { 
                    sql1 += " order by b.course, b.cyear, b.courseseq, c.userid, b.subj, b.year, b.subjseq,b.subjnm";
                } else { 
                    sql1 += " order by b.course, b.cyear, b.courseseq, c.userid, " + v_orderColumn + v_orderType;
                }                

                ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
                	dbox = ls1.getDataBox();
                    list1.add(dbox);
                }
                
                for(int i=0;i < list1.size(); i++){
                    dbox2       =   (DataBox)list1.get(i);
                    v_course    =   dbox2.getString("d_course");
                    v_courseseq =   dbox2.getString("d_courseseq");
                    v_userid    =   dbox2.getString("d_userid");
                    
                    if(!v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq) && v_Buserid.equals(v_userid))){
                        sql2 = "select count(*) cnt from VZ_SCSUBJSEQ A,TZ_STUDENT B,TZ_MEMBER C ";                        
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
            			if (!ss_mclass.equals("ALL")) {
            			    sql2+= " and A.scmiddleclass = "+SQLString.Format(ss_mclass);
            			}
            			if (!ss_subjcourse.equals("ALL")) {
            				sql2+= " and A.scsubj = "+SQLString.Format(ss_subjcourse);
            			}
            			if (!ss_subjseq.equals("ALL")) {
            				sql2+= " and A.scsubjseq = "+SQLString.Format(ss_subjseq);
            			}            
                        if (!ss_company.equals("ALL")) {
                            sql2+= " and C.comp like '"+GetCodenm.get_compval(ss_company)+"'";
                        }                     
                        sql2+= "and A.course = "+SQLString.Format(v_course)+" and A.courseseq = "+SQLString.Format(v_courseseq)
                            + " and b.userid = "+SQLString.Format(v_userid);

                     
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
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }            
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }



    /**
    폼메일 발송
    @param box      receive from the form object and session
    @return int
    */
    public int sendFormMail(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        ListSet             ls      = null;
        String              sql     = "";
        int cnt = 0;    //  메일발송이 성공한 사람수
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수
        String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");    // 과목분류
        String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류
        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        // p_checks로 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector v_checks     = new Vector();
        v_checks            = box.getVector("p_checks");
        Enumeration em      = v_checks.elements();
        String v_userid     = "";

        try { 
            connMgr = new DBConnectionManager();

//// //// //// //// ////  폼메일 발송 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// // 
            String v_sendhtml = "mail3.html";
            FormMail fmail = new FormMail(v_sendhtml);     //      폼메일발송인 경우
            MailSet mset = new MailSet(box);        //      메일 세팅 및 발송
            String v_mailTitle = "안녕하세요? 사이버러닝센터 운영자입니다.(진도율안내)";
//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

            while ( em.hasMoreElements() ) { 
                v_userid   = (String)em.nextElement();

                // select subjnm,passday,tstep,gradstep,gradscore,name,ismailing,cono,email
                sql = "select  B.subjnm,A.tstep,B.gradstep,B.sgradscore gradscore,D.name,D.ismailing,D.cono,D.email,";
                sql += "        to_char(sysdate, 'YYYYMMDD') nowday, substr(B.edustart,1,8) eduday                 ";
                sql += " from TZ_STUDENT A,VZ_SCSUBJSEQ B,TZ_MEMBER D ";
                sql += " where A.userid = " +SQLString.Format(v_userid);
                if ( !ss_grcode.equals("ALL") ) { 
                    sql += " and B.grcode = " +SQLString.Format(ss_grcode);
                }
                if ( !ss_gyear.equals("ALL") ) { 
                    sql += " and B.gyear = " +SQLString.Format(ss_gyear);
                }
                if ( !ss_grseq.equals("ALL") ) { 
                    sql += " and B.grseq = " +SQLString.Format(ss_grseq);
                }
                if ( !ss_uclass.equals("ALL") ) { 
                    sql += " and B.scupperclass = " +SQLString.Format(ss_uclass);
                }
                if ( !ss_mclass.equals("ALL") ) { 
                    sql += " and B.scmiddleclass = " +SQLString.Format(ss_mclass);
                }
                if ( !ss_lclass.equals("ALL") ) { 
                    sql += " and B.sclowerclass = " +SQLString.Format(ss_lclass);
                }

                if ( !ss_subjcourse.equals("ALL") ) { 
                    sql += " and B.scsubj = " +SQLString.Format(ss_subjcourse);
                }
                if ( !ss_subjseq.equals("ALL") ) { 
                    sql += " and B.scsubjseq = " +SQLString.Format(ss_subjseq);
                }
                sql += " and A.subj=B.subj and A.year=B.year and A.subjseq=B.subjseq and A.userid=D.userid ";
                sql += " group by B.subjnm,A.tstep,B.gradstep,B.sgradscore,D.name,D.ismailing,D.cono,D.email,B.edustart,B.eduend ";
                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    String v_toEmail =  ls.getString("email");
                    String v_toCono  =  ls.getString("cono");
                    String v_ismailing= ls.getString("ismailing");

                    int passday       = FormatDate.datediff("d", ls.getString("eduday"), ls.getString("nowday") );
                    // String v_toEmail =  "jj1004@dreamwiz.com";

                    mset.setSender(fmail);     //  메일보내는 사람 세팅

                    fmail.setVariable("tstep", ls.getString("tstep") );
                    fmail.setVariable("subjnm", ls.getString("subjnm") );
                    fmail.setVariable("passday", String.valueOf(passday));
                    fmail.setVariable("tstep", ls.getString("tstep") );
                    fmail.setVariable("gradstep", ls.getString("gradstep") );
                    fmail.setVariable("gradscore", ls.getString("gradscore") );
                    fmail.setVariable("toname", ls.getString("name") );

                    String v_mailContent = fmail.getNewMailContent();

                    boolean isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent,v_ismailing, v_sendhtml);
                    if ( isMailed) cnt++;     //      메일발송에 성공하면
                }

            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return cnt;
    }

    /**
    유저정보 수정하여 저장할때
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
    public int ChangeUserInfo(RequestBox box) throws Exception { 
        DBConnectionManager connMgr      = null;
        PreparedStatement   pstmt        = null;
        StringBuffer        sbSQL        = new StringBuffer("");
        int                 isOk         = 0;
        
        int                 iSysAdd      = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 

        String              v_searchtext = box.getString("p_searchtext" );
        String              v_name      = box.getString("p_name"      );
        String              v_email      = box.getString("p_email"      );
        String              v_pwd        = box.getString("p_pwd"        );
        String              v_birth_date    = box.getString("p_birth_date1"    )+box.getString("p_birth_date2"    );
        String              v_user_path   = box.getString("p_user_path"   );
        String              v_hometel    = box.getString("p_hometel"    );
        String              v_handphone  = box.getString("p_handphone"  );
        String              v_post1      = box.getString("p_post1"      );
        String              v_post2      = box.getString("p_post2"      );
        String              v_addr       = box.getString("p_addr"       );
        String              v_addr2      = box.getString("p_addr2"      );
        String              v_ismailling = box.getString("p_ismailling" );
        String              v_issms      = box.getString("p_issms"      );
        //String              v_specials   = box.getString("p_specials"   );
        String              v_comp   	 = box.getString("p_comp"   );
        String              v_bon_adm  	 = box.getStringDefault("p_bon_adm","N");
        String              v_position_nm  	 = box.getString("p_position_nm");
        String              v_lvl_nm  	 = box.getString("p_lvl_nm");
        String              v_isretire   = box.getString("p_isretire");
        String   			v_dept_cd		  = box.getString("p_dept_cd"); 
        String   			v_agency_cd		  = box.getString("p_agency_cd"); 

        try {
            connMgr     = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            sbSQL.append(" UPDATE tz_member SET                 \n")
                 .append("         email       = ?              \n")
                 .append("     ,   hometel     = ?              \n")
                 .append("     ,   handphone   = ?              \n")
                 .append("     ,   zip_cd      = ?              \n")
                 .append("     ,   address     = ?              \n")
                 .append("     ,   ismailling  = ?              \n")
                 .append("     ,   comp  	   = ?              \n")
                 .append("     ,   bon_adm 	   = ?              \n")
                 .append("     ,   issms 	   = ?              \n")
                 .append("     ,   position_nm = ?              \n")
                 .append("     ,   lvl_nm 	   = ?              \n")
                 .append("     ,   isretire    = ?              \n")
                 .append("     ,   pwd    = fn_crypt('1', ?, 'knise')             \n")
                 .append("     ,   birth_date = fn_crypt('2', '" + v_birth_date + "', 'knise')  \n") //DECODE(CERT,'Y','"+Encrypt.com_Encode(v_birth_date)+"','"+v_birth_date+"')              \n")
                 .append("     ,   user_path    = ?              \n")
                 .append("     ,   name    = ?              \n")
                 .append("     ,   dept_cd 	   = ?  	 \n")
                 .append("     ,   agency_cd 	   = ?  	\n")
                 .append(" WHERE   userid      = ?              \n");

            pstmt       = connMgr.prepareStatement(sbSQL.toString());

            pstmt.setString(1, v_email          							);
            pstmt.setString(2, v_hometel        							);
            pstmt.setString(3, v_handphone      							);
            pstmt.setString(4, v_post1 + "-" + v_post2          			);
            pstmt.setString(5, v_addr           							);
            pstmt.setString(6, v_ismailling.equals("") ? "N" : v_ismailling );
            pstmt.setString(7, v_comp    									);
            pstmt.setString(8, v_bon_adm    								);
            pstmt.setString(9, v_issms.equals("") ? "N" : v_issms       	);
            pstmt.setString(10, v_position_nm  								);
            pstmt.setString(11, v_lvl_nm    								);
            pstmt.setString(12, v_isretire    								);
            
            pstmt.setString(13, v_pwd    								);
            pstmt.setString(14, v_user_path    								);
            pstmt.setString(15, v_name    								);
            pstmt.setString(16, v_dept_cd);
            pstmt.setString(17, v_agency_cd);
            
            pstmt.setString(18, v_searchtext    								);
            
/*            sbSQL.append(" UPDATE tz_member SET                 \n")
            .append("         email       = ?              \n")
            .append("     ,   pwd         = ?              \n")
            .append("     ,   comptel     = ?              \n")
            .append("     ,   hometel     = ?              \n")
            .append("     ,   handphone   = ?              \n")
            .append("     ,   post1       = ?              \n")
            .append("     ,   post2       = ?              \n")
            .append("     ,   address1    = ?              \n")
            .append("     ,   address2    = ?              \n")
            .append("     ,   issms       = ?              \n")
            .append("     ,   ismailling  = ?              \n")
            .append("     ,   specials    = empty_clob()   \n")
            .append(" WHERE   userid      = ?              \n");
            
            System.out.println(this.getClass().getName() + "." + "ChangeUserInfo() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            pstmt       = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setString(1, v_email          );
            pstmt.setString(2, v_pwd            );
            pstmt.setString(3, v_comptel        );
            pstmt.setString(4, v_hometel        );
            pstmt.setString(5, v_handphone      );
            pstmt.setString(6, v_post1          );
            pstmt.setString(7, v_post2          );
            pstmt.setString(8, v_addr           );
            pstmt.setString(9, v_addr2          );
            pstmt.setString(10, v_issms.equals("") ? "N" : v_issms           );
            pstmt.setString(11, v_ismailling.equals("") ? "N" : v_ismailling );
            pstmt.setString(12, v_searchtext    );
*/            
            isOk        = pstmt.executeUpdate();
            
            sbSQL.setLength(0);
            
            // WebLogic 6.1인경우
            //sbSQL.append("select specials from TZ_Member where userid = " + SQLString.Format(v_searchtext) + " \n");
            //connMgr.setWeblogicCLOB(sbSQL.toString(), v_specials);  // clob 필드에 스트림을 이용,  data 를 넣는다(Weblogic 인 경우)
            
            sbSQL.setLength(0);
            
            pstmt.close();
            
            // 튜터테이블도 반영( 있을경우만)
            if ( isOk > 0 ) { 
                sbSQL.append(" UPDATE  tz_tutor SET             \n")
                     //.append("         phone       = ?          \n")
                     .append("        handphone   = ?          \n")
                     .append("     ,   post1       = ?          \n")
                     .append("     ,   post2       = ?          \n")
                     .append("     ,   add1        = ?          \n")
                     .append("     ,   add2        = ?          \n")
                     .append("     ,   email       = ?          \n")
                     .append(" WHERE   userid      = ?          \n");
                
                //System.out.println(this.getClass().getName() + "." + "ChangeUserInfo() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

                pstmt   = connMgr.prepareStatement(sbSQL.toString());

                //pstmt.setString(1, v_comptel    );
                pstmt.setString(1, v_handphone  );
                pstmt.setString(2, v_post1      );
                pstmt.setString(3, v_post2      );
                pstmt.setString(4, v_addr       );
                pstmt.setString(5, v_addr2      );
                pstmt.setString(6, v_email      );
                pstmt.setString(7, v_searchtext );
                
                isOk    = pstmt.executeUpdate();
                
                if ( isOk == 0 )
                    isOk = 1;
            }

            if ( isOk > 0 )    
                connMgr.commit();
            else
                connMgr.rollback();
        } catch ( SQLException e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback(); 
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback(); 
                } catch ( Exception ex ) { } 
            }
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.setAutoCommit(true); 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }


    /**
    회원 비밀번호 초기화
    @param box      receive from the form object and session
    @return int     정상처리여부(1 : 정상, 0 : 오류)
    */
    public int resetUserPwd(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;       
        PreparedStatement   pstmt       = null;
        DataBox             dbox        = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 1;
        int                 iparamidx   = 0; // pstmt의 Parameter Index 
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_userid    = box.getString ("p_userid" );
        String              v_adminid   = box.getSession("userid"   );
        String              v_pwd       = "";

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL.append(" SELECT   SUBSTR(fn_crypt('2', birth_date, 'knise'), 7) Pwd                        \n")
                 .append(" FROM     Tz_Member                                   \n")
                 .append(" WHERE  UserId = " + SQLString.Format(v_userid) + "   \n");
            
            ls      = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() )
                dbox = ls.getDataBox();
            
            v_pwd = dbox.getString("d_pwd");
            if(v_pwd.equals(""))
            	v_pwd = v_userid;
                
            sbSQL.setLength(0);
            
            sbSQL.append(" UPDATE Tz_Member SET                                                         \n")
                 .append("     Pwd     = fn_crypt('1', " + SQLString.Format(v_pwd) + ", 'knise')                                \n")
                 .append("  ,  LDate   = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')                           \n")
                 .append(" WHERE  UserId = " + SQLString.Format(v_userid) + "                           \n");
            pstmt = connMgr.prepareStatement(sbSQL.toString());
            
            isOk  = pstmt.executeUpdate();
            
            pstmt.close();
            
            sbSQL.setLength(0);

            sbSQL.append(" INSERT INTO Tz_PwdSearchList                                            \n")
                 .append(" (                                                                       \n")
                 .append("         Seq                                                             \n")
                 .append("     ,   UserId                                                          \n")
                 .append("     ,   LDate                                                           \n")
                 .append("     ,   Success_Yn                                                      \n")
                 .append("     ,   Admin_Yn                                                        \n")
                 .append("     ,   Admin_Id                                                        \n")
                 .append(" )                                                                       \n")
                 .append(" VALUES                                                                  \n")
                 .append(" (                                                                       \n")
                 .append("         (SELECT NVL(MAX(Seq) + 1, 1) FROM Tz_PwdSearchList)             \n")
                 .append("     ,   ?                                                               \n")
                 .append("     ,   TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')                            \n")
                 .append("     ,   'Y'                                                             \n")
                 .append("     ,   'Y'                                                             \n")
                 .append("     ,   ?                                                               \n")
                 .append(" )                                                                       \n");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setString(++iparamidx, v_userid   );
            pstmt.setString(++iparamidx, v_adminid  );
            
            isOk  = pstmt.executeUpdate();
            
            connMgr.commit();
        } catch ( SQLException e ) {
            if ( connMgr != null ) { try { connMgr.rollback();          } catch ( Exception ex ) { } }
            
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { try { connMgr.rollback();          } catch ( Exception ex ) { } }
            
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e ) { } }
        }
        
        return isOk;
    }
    
    /**
    탈퇴 회원 복구
    @param box      receive from the form object and session
    @return int     
    */
    public int recoveryRetire(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;       
        PreparedStatement   pstmt       = null;
        DataBox             dbox        = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 1;
        int                 iparamidx   = 0; // pstmt의 Parameter Index 
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_userid    = box.getString ("p_userid" );
        String              v_adminid   = box.getSession("userid"   );
        String              v_pwd       = "";

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL.append(" UPDATE Tz_Member SET                                                         \n")
                 .append("  isretire = null                                                             \n")
                 .append(", retire_type = null                                                          \n")
                 .append(", retire_date = null                                                          \n")
                 .append(", retire_reason = null                                                        \n")
                 .append(" WHERE  UserId = " + SQLString.Format(v_userid) + "                           \n");
                 
            //System.out.println(this.getClass().getName() + "." + "resetUserPwd() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            isOk  = pstmt.executeUpdate();
            
            pstmt.close();
            
            connMgr.commit();
        } catch ( SQLException e ) {
            if ( connMgr != null ) { try { connMgr.rollback();          } catch ( Exception ex ) { } }
            
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { try { connMgr.rollback();          } catch ( Exception ex ) { } }
            
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e ) { } }
        }
        
        return isOk;
    }
    
    /**
    탈퇴 기간 제한 없이 재가입 가능 하도록 설정
    @param box      receive from the form object and session
    @return int     
    */
    public int IgnoreRetireDate(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;
        ListSet             ls          = null;       
        PreparedStatement   pstmt       = null;
        DataBox             dbox        = null;
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 1;
        int                 iparamidx   = 0; // pstmt의 Parameter Index 
        
        int                 iSysAdd     = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_userid    = box.getString ("p_userid" );
        String              v_adminid   = box.getSession("userid"   );
        String              v_pwd       = "";

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sbSQL.append(" UPDATE Tz_Member SET                                                         \n")
                 .append("  isignoreretiredate = 'Y'                                                    \n")
                 .append(" WHERE  UserId = " + SQLString.Format(v_userid) + "                           \n");
                 
            //System.out.println(this.getClass().getName() + "." + "resetUserPwd() Printing Order " + ++iSysAdd + ". ======[SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            pstmt = connMgr.prepareStatement(sbSQL.toString());
            isOk  = pstmt.executeUpdate();
            
            pstmt.close();
            
            connMgr.commit();
        } catch ( SQLException e ) {
            if ( connMgr != null ) { try { connMgr.rollback();          } catch ( Exception ex ) { } }
            
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { try { connMgr.rollback();          } catch ( Exception ex ) { } }
            
            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls      != null ) { try { ls.close();                  } catch ( Exception e ) { } }
            if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e ) { } }
        }
        
        return isOk;
    }
    
    
	public ArrayList selectIpHistory(RequestBox box) throws Exception {               
		DataBox dbox = null;
		DBConnectionManager connMgr = null;
		ListSet ls = null;
		ArrayList list = null;
		String sql  = "";
		String v_userid = box.getString("p_userid");

		try {
			list = new ArrayList();
			
			sql = "\n   select    "
				+ "\n   lgip,    "
				+ "\n   before_ip,    "
				+ "\n   decode(type,'1','등록IP변경','2','공용pc','3''기타','') type,    "
				+ "\n   contents ,   "
				+ "\n   substr(ldate,1,4) ||'.'|| substr(ldate,5,2) ||'.'|| substr(ldate,7,2)  ldate    "
				+ "\n   from TZ_IPCHECK    "
				+ "\n   where    "
				+ "\n  userid = " + SQLString.Format(v_userid)
	         	+ "\n   	order by ldate desc   "
	    		+ "\n                             ";
			
			connMgr = new DBConnectionManager();
			ls = connMgr.executeQuery(sql);
            
			while (ls.next()) {
				dbox = ls.getDataBox();
				list.add(dbox);
			}
		}            
		catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, box, sql);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
		}
		finally {
			if(ls != null) { try { ls.close(); }catch (Exception e) {} }
			if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
		}
		return list;
	}	
	
    
    
}