// **********************************************************
//  1. 제      목: 교육기수OPERATION BEAN
//  2. 프로그램명: GrseqBean.java
//  3. 개      요:
//  4. 환      경: JDK 1.3
//  5. 버      젼: 0.1
//  6. 작      성: LeeSuMin 2003. 07. 16
//  7. 수      정:
// **********************************************************
package com.ziaan.propose;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sds.sec.Encrypt;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.propose.email.SendRegisterCoursesEMail;
import com.ziaan.propose.email.SendRegisterCoursesEMailImplBean;
import com.ziaan.propose.email.log.EmailLog;
import com.ziaan.propose.email.log.impl.EmailLogImplBean;


public class ApprovalBean { 
	
	private Logger logger = Logger.getLogger(this.getClass());


	
    public ApprovalBean() { }

    /**
    수강신청 승인리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   수강신청 승인대상 명단 리스트
    */
    public ArrayList SelectApprovalScreenList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls = null,ls2= null;
        ArrayList list1 = null, list2= null;
        String sql  = "", sql2= "";
        ApprovalScreenData data = null;
        String  v_scsubj= "", v_scyear= "", v_scsubjseq= "";

        String p_grcode     = box.getString("s_grcode");
        String p_gyear      = box.getString("s_gyear");
        String p_grseq      = box.getStringDefault("s_grseq","ALL");
        String p_mastercd   = box.getString("s_mastercd");
        String ss_uclass    = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String ss_mclass    = box.getStringDefault("s_middleclass","ALL");    // 과목분류
        String ss_lclass    = box.getStringDefault("s_lowerclass","ALL");    // 과목분류
        String p_subjcourse = box.getString("s_subjcourse");
        String p_subjseq    = box.getString("s_subjseq");
        String  ss_company  = box.getString("s_company");
        
        int    p_step       = box.getInt("p_step");
        String p_appstatus  = box.getString("p_appstatus");

        String v_order      = box.getString("p_order");
        String v_orderType     = box.getString("p_orderType");                 // 정렬할 순서

        String p_searchkind = box.getStringDefault("p_searchkind","subjseq");
        
        //String p_startdt = box.getString("p_startdt")+"000000";
        //String p_enddt = box.getString("p_enddt")+"232359";
        String p_startdt = box.getString("p_startdt")+"00";
        String p_enddt = box.getString("p_enddt")+"23";

        String v_mastercd   = "";
        String v_isedutarget   = "";
        boolean isAddLs     = true;
        String v_searchtext = box.getString("p_key1");
        String v_search     = box.getString("p_gubun");
        String v_searchtext1 = box.getString("p_searchtext");
        String v_search1     = box.getString("p_search");

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            
            sql = "\n select a.subj "
            	+ "\n      , get_cpnm((select cp from tz_subj where subj=a.scsubj)) cpnm "
            	+ "\n      , a.year "
            	+ "\n      , a.subjseq "
            	+ "\n      , a.subjnm "
            	+ "\n      , a.edustart "
            	+ "\n      , a.eduend "
            	+ "\n      , b.userid "
            	+ "\n      , b.comp "
            	+ "\n      , b.appdate "
            	+ "\n      , b.isdinsert "
            	+ "\n      , b.chkfirst "
            	+ "\n      , b.chkfinal "
            	+ "\n      , b.ischkfirst "
            	+ "\n      , a.course "
            	+ "\n      , a.cyear "
            	+ "\n      , a.courseseq "
            	+ "\n      , a.scsubjnm coursenm "            	
            	+ "\n      , c.name "
            	+ "\n      , c.position_nm "
            	+ "\n      , c.lvl_nm "
            	+ "\n      , c.hometel "
            	+ "\n      , c.handphone "
            	+ "\n      , c.email "
            	+ "\n      , get_compnm(b.comp) as companynm "
            	//+ "\n      , get_postnm(c.post) as jikwinm " 
            	+ "\n      , c.lvl_nm as jikwinm "
            	+ "\n      , decode(d.userid, null, 'N', 'Y') as isclosed "                      
            	+ "\n      , c.position_nm as deptnm "	
            	+ "\n      , a.biyong "                                    
            	+ "\n      , a.isgoyong "
            	+ "\n      , a.goyongpricemajor "
            	+ "\n      , a.goyongpriceminor "
            	+ "\n      , a.goyongpricestand "
            	+ "\n      , get_codenm('0004', a.isonoff) as isonoff "
            	// 추가요청 교재 배송을 위한 정보
            	+ "\n      , HRDC "
            	+ "\n      , ZIP_CD "
            	+ "\n      , ADDRESS "
            	+ "\n      , ZIP_CD1 "
            	+ "\n      , ADDRESS1 "
            	+ "\n      , USER_PATH "
            	+ "\n      , ISMAILLING "
            	+ "\n      , ISSMS "
            	//결제관련 정보 추가-서지한
            	+ "\n      , (select decode(trim(type), 'OB','교육청일괄납부','PB','무통장','SC0010','신용카드','SC0030','계좌이체','SC0040','가상계좌',type) from pa_payment pa where pa.order_id = b.order_id and pa.useyn='Y') as pay "
            	+ "\n      , (select type from pa_payment pa where pa.order_id = b.order_id and pa.useyn='Y') as paycd "
            	+ "\n      , (select decode(enterance_dt,null,'',to_char(to_date(enterance_dt,'yyyy-mm-dd'),'yyyy-mm-dd')) from pa_payment pa where pa.order_id = b.order_id and pa.useyn='Y') as enterance_dt "
            	+ "\n      , (select decode(enter_dt,null,'',to_char(to_date(enter_dt,'yyyy-mm-dd'),'yyyy-mm-dd')) from pa_payment pa where pa.order_id = b.order_id and pa.useyn='Y') as enter_dt "
            	+ "\n      , (select order_id from pa_payment pa where pa.order_id = b.order_id and pa.useyn='Y') as order_id "
            	+ "\n      , c.cert "
            	+ "\n      , fn_crypt('2', c.birth_date, 'knise') birth_date "
            	+ "\n      , b.etc " //비고컬럼 추가
            	+ "\n      , decode(b.chkfinal,'Y',b.ldate,'') as approvaldate " //승인일시
            	+ "\n from   tz_propose b "
            	+ "\n      , vz_scsubjseq a "
            	+ "\n      , tz_member c "
            	+ "\n      , tz_stold d "
            	//+ "\n      ,( select scsubj,scyear,scsubjseq, count(*) rowspan from tz_propose x, vz_scsubjseq y where x.subj=y.subj and x.year=y.year and x.subjseq=y.subjseq group by scsubj, scyear, scsubjseq, userid ) e "            	            	
            	+ "\n where  b.subj         = a.subj " 
            	+ "\n and    b.year         = a.year "
            	+ "\n and    b.subjseq      = a.subjseq "
            	+ "\n and    b.userid       = c.userid "
            	//+ "\n and    c.cert is null "
            	+ "\n and    b.subj         = d.subj(+) "
            	+ "\n and    b.year         = d.year(+) "
            	+ "\n and    b.subjseq      = d.subjseq(+) "
            	+ "\n and    b.userid       = d.userid(+) ";
            	//결제수단 검색
            if(!"".equals(box.getString("p_search_payType"))){  
            	
            	if(!box.getString("p_search_payType").equals("etc")){
            		sql += "\n and (select trim(type) from pa_payment pa  where pa.order_id = b.order_id and pa.useyn='Y')  = '"+box.getString("p_search_payType")+"' ";
            	}else{
            		sql += "\n and (select trim(type) from pa_payment pa  where pa.order_id = b.order_id and pa.useyn='Y')  not in ('PB','OB','SC0010','SC0030','SC0040') ";
            	}
            
            }
            
            if ( !v_searchtext.equals("") ) {                             //    검색어가 있으면
            	
                if ( v_search.equals("userid") ) {                        //    ID로 검색할때
                	sql += " and upper(c.userid) like upper('%" + v_searchtext + "%')";
                } else if ( v_search.equals("name") ) {                   //    이름으로 검색할때
                	sql += " and upper(c.name) like upper('%" + v_searchtext + "%')";
                } else if ( v_search.equals("birth_date") ) {                  //    주민등록번호로  검색할때
                	sql += " and upper(substr(fn_crypt('1', c.birth_date, 'knise'),1,6)) like upper('%" + v_searchtext + "%')";
                } else if ( v_search.equals("handphone") ) {              //    핸드폰번호로  검색할때
                	v_searchtext = v_searchtext.replace("-", "");
                	sql += " and replace(handphone,'-','') like ('%" + v_searchtext + "%')";
                } else if ( v_search.equals("user_path") ) {                  //    학교명  검색할때
                	sql += " and upper(c.user_path) like upper('%" + v_searchtext + "%')";
                }

            } 
            if ( !v_searchtext1.equals("") ) {                             //    검색어가 있으면
            	
                if ( v_search1.equals("userid") ) {                        //    ID로 검색할때
                	sql += " and upper(c.userid) like upper('%" + v_searchtext1 + "%')";
                } else if ( v_search1.equals("name") ) {                   //    이름으로 검색할때
                	sql += " and upper(c.name) like upper('%" + v_searchtext1 + "%')";
                } else if ( v_search1.equals("birth_date") ) {                  //    주민등록번호로  검색할때
                	sql += " and upper(substr(fn_crypt('1', c.birth_date, 'knise') ,1,6)) like upper('%" + v_searchtext + "%')";
                } else if ( v_search1.equals("handphone") ) {              //    핸드폰번호로  검색할때
                	v_searchtext1 = v_searchtext1.replace("-", "");
                	sql += " and replace(handphone,'-','') like ('%" + v_searchtext1 + "%')";
                } else if ( v_search1.equals("user_path") ) {                  //    학교명  검색할때
                	sql += " and upper(c.user_path) like upper('%" + v_searchtext1 + "%')";
                }

            } 
              if(!"ALL".equals(box.getString("s_gyear"))){
            	  //sql += " and b.appdate between '"+box.getString("s_gyear")+"'||'0101000001' and '"+box.getString("s_gyear")+"'||'1231235959'";
              }
            
              if ( !p_grseq.equals("ALL")) sql += "\n and    a.grseq        ="  + SQLString.Format(p_grseq);
              
              // 검색방식에 따른 과목 검색조건 분기
              if ( !p_grcode.equals("ALL"))    sql +="\n and    a.grcode = " + StringManager.makeSQL(p_grcode);
              
              if ( !ss_uclass.equals("ALL"))    sql +="\n and    a.scupperclass = " + StringManager.makeSQL(ss_uclass);
              if ( !ss_mclass.equals("ALL"))    sql +="\n and    a.scmiddleclass= " + StringManager.makeSQL(ss_mclass);
              if ( !ss_lclass.equals("ALL"))    sql +="\n and    a.sclowerclass = " + StringManager.makeSQL(ss_lclass);
              if ( !p_subjcourse.equals("ALL")) sql +="\n and    a.scsubj         = " + StringManager.makeSQL(p_subjcourse);
              if ( !p_subjseq.equals("ALL"))    sql +="\n and    a.scsubjseq      = " + StringManager.makeSQL(p_subjseq);
            

			// p_appstatus : B-미처리, Y-승인, N-반려
            if ( !p_appstatus.equals("ALL") ) { 
                sql +=  "\n and    b.chkfinal = " + StringManager.makeSQL(p_appstatus);
            }

            if ( !ss_company.equals("ALL") ) { 
                    sql += "\n and    c.comp =" +StringManager.makeSQL(ss_company) + " ";
            }
            
            if ( v_order.equals("subj")     )    v_order ="a.scsubj";
            if ( v_order.equals("companynm"))    v_order ="get_compnm(c.comp)";
            if ( v_order.equals("deptnm")   )    v_order ="get_deptnm(c.hq_org_cd)";
            if ( v_order.equals("jikwinm")  )    v_order ="get_postnm(c.post)";
            
            //if ( v_order.equals("") ) { 
            //    sql += "\n order  by b.comp, a.isonoff, a.scsubjnm ";
            //} else { 
            ///    sql += "\n order  by " + v_order + v_orderType;
            //}
            if ( v_order.equals("") ) { 
                sql += " order by a.scsubj, a.scyear, a.scsubjseq, a.scsubjnm";
            } else { 
                sql += " order by " + v_order + v_orderType;
            }    
            
            System.out.println("---------------sql-----------------\n" + sql);

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                    data = new ApprovalScreenData();
                    data.setIsclosed           ( ls.getString("isclosed") );
                    data.setBiyong             ( ls.getInt   ("biyong") );
                    data.setCompanynm          ( ls.getString("companynm") );
                    
                    data.setGrcode	 (p_grcode);	 
    				data.setGyear	 (p_gyear);
    				data.setGrseq    (p_grseq);
    				data.setScsubj    (ls.getString("subj"));
    				data.setScyear    (ls.getString("year"));
    				data.setScsubjseq (ls.getString("subjseq"));
    				data.setScsubjnm  (ls.getString("subjnm"));    				
    				data.setCourse    (ls.getString("course"));
    				data.setCyear     (ls.getString("cyear"));
    				data.setCourseseq (ls.getString("courseseq"));
    				data.setCoursenm  (ls.getString("coursenm"));    				
    				data.setEdustart  (ls.getString("edustart"));
    				data.setEduend    (ls.getString("eduend"));
    				data.setUserid    (ls.getString("userid"));
    				data.setComp      (ls.getString("comp"));
    				data.setJikwinm   (ls.getString("jikwinm"));
    				data.setAppdate   (ls.getString("appdate"));
    				data.setIsdinsert (ls.getString("isdinsert"));
    				data.setChkfirst  (ls.getString("chkfirst"));
    				data.setChkfinal  (ls.getString("chkfinal"));
    				data.setIschkfirst(ls.getString("ischkfirst"));
    				data.setName      (ls.getString("name"));
    				data.setDeptnm    (ls.getString("deptnm"));    				
    				data.setIsgoyong  (ls.getString("isgoyong"));
    				data.setEmail(ls.getString("email"));
    				
    				data.setGoyongpricemajor(ls.getInt("goyongpricemajor"));
    				data.setGoyongpriceminor(ls.getInt("goyongpriceminor"));
    				data.setGoyongpricestand(ls.getInt("goyongpricestand"));
    				data.setIsonoff   (ls.getString("isonoff"));
    				data.setCpnm   (ls.getString("cpnm"));
    				//data.setRowspan(ls.getInt("rowspan"));
    				
    				data.setPosition_nm(ls.getString("position_nm"));
    				data.setLvl_nm(ls.getString("lvl_nm"));
    				data.setHometel(ls.getString("hometel"));
    				data.setHandphone(ls.getString("handphone"));
    				
    				data.setHRDC(ls.getString("hrdc"));
    				data.setZIP_CD(ls.getString("zip_cd"));
    				data.setADDRESS(ls.getString("address"));
    				data.setZIP_CD1(ls.getString("zip_cd1"));
    				data.setADDRESS1(ls.getString("address1"));
    				data.setUSER_PATH(ls.getString("user_path"));
    				data.setISMAILLING(ls.getString("ismailling"));
    				data.setIpayType(ls.getString("pay"));
    				data.setPaycd(ls.getString("paycd"));
    				data.setEnterance_dt(ls.getString("enterance_dt"));
    				data.setEnter_dt(ls.getString("enter_dt"));
    				data.setOrder_id(ls.getString("order_id"));
    				data.setEtc(ls.getString("etc"));
    				data.setApprovaldate(ls.getString("approvaldate"));
    				
    				//data.setCert(ls.getString("cert"));
    				//System.out.println("#######################################################");
    				//System.out.println(data.getUserid()+"::"+ls.getString("birth_date"));
	    			if(!"".equals(ls.getString("birth_date")) && ls.getString("birth_date") != null){	
    					if("Y".equals(ls.getString("cert"))){
	    					data.setbirth_date(ls.getString("birth_date").substring(0,6)+"-"+ls.getString("birth_date").substring(6,13));
	    					//System.out.println(data.getbirth_date());
	    				}else{
	    					data.setbirth_date(ls.getString("birth_date").substring(0,6)+"-"+ls.getString("birth_date").substring(6));
	    					//System.out.println(data.getbirth_date());
	    				}
            		}else{
            			data.setbirth_date(ls.getString("birth_date"));	
            		}
    				//data.setbirth_date(ls.getString("birth_date"));
    				list1.add(data);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
    
    /**
    수강신청 삭제리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   수강신청 승인대상 명단 리스트
    */
    public ArrayList SelectApprovalDeleteScreenList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls = null,ls2= null;
        ArrayList list1 = null, list2= null;
        String sql  = "", sql2= "";
        ApprovalScreenData data = null;
        String  v_scsubj= "", v_scyear= "", v_scsubjseq= "";

        String p_grcode     = box.getString("s_grcode");
        String p_gyear      = box.getString("s_gyear");
        String p_grseq      = box.getStringDefault("s_grseq","ALL");
        String p_mastercd   = box.getString("s_mastercd");
        String ss_uclass    = box.getStringDefault("s_upperclass","ALL");    // 과목분류
        String ss_mclass    = box.getStringDefault("s_middleclass","ALL");    // 과목분류
        String ss_lclass    = box.getStringDefault("s_lowerclass","ALL");    // 과목분류
        String p_subjcourse = box.getString("s_subjcourse");
        String p_subjseq    = box.getString("s_subjseq");
        String  ss_company  = box.getString("s_company");
        
        int    p_step       = box.getInt("p_step");
        String p_appstatus  = box.getString("p_appstatus");

        String v_order      = box.getString("p_order");
        String v_orderType     = box.getString("p_orderType");                 // 정렬할 순서

        String p_searchkind = box.getStringDefault("p_searchkind","subjseq");
        
        //String p_startdt = box.getString("p_startdt")+"000000";
        //String p_enddt = box.getString("p_enddt")+"232359";
        String p_startdt = box.getString("p_startdt")+"00";
        String p_enddt = box.getString("p_enddt")+"23";

        String v_mastercd   = "";
        String v_isedutarget   = "";
        boolean isAddLs     = true;
        String v_searchtext = box.getString("p_key1");
        String v_search     = box.getString("p_gubun");
        String v_searchtext1 = box.getString("p_searchtext");
        String v_search1     = box.getString("p_search");

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            
            sql = "\n select a.subj "
            	+ "\n      , get_cpnm((select cp from tz_subj where subj=a.scsubj)) cpnm "
            	+ "\n      , a.year "
            	+ "\n      , a.subjseq "
            	+ "\n      , a.subjnm "
            	+ "\n      , a.edustart "
            	+ "\n      , a.eduend "
            	+ "\n      , b.userid "
            	+ "\n      , b.comp "
            	+ "\n      , b.appdate "
            	+ "\n      , b.isdinsert "
            	+ "\n      , b.chkfirst "
            	+ "\n      , b.chkfinal "
            	+ "\n      , b.ischkfirst "
            	+ "\n      , a.course "
            	+ "\n      , a.cyear "
            	+ "\n      , a.courseseq "
            	+ "\n      , a.scsubjnm coursenm "            	
            	+ "\n      , c.name "
            	+ "\n      , c.position_nm "
            	+ "\n      , c.lvl_nm "
            	+ "\n      , c.hometel "
            	+ "\n      , c.handphone "
            	+ "\n      , c.email "
            	+ "\n      , get_compnm(b.comp) as companynm "
            	//+ "\n      , get_postnm(c.post) as jikwinm " 
            	+ "\n      , c.lvl_nm as jikwinm "
            	+ "\n      , decode(d.userid, null, 'N', 'Y') as isclosed "                      
            	+ "\n      , c.position_nm as deptnm "	
            	+ "\n      , a.biyong "                                    
            	+ "\n      , a.isgoyong "
            	+ "\n      , a.goyongpricemajor "
            	+ "\n      , a.goyongpriceminor "
            	+ "\n      , a.goyongpricestand "
            	+ "\n      , get_codenm('0004', a.isonoff) as isonoff "
            	// 추가요청 교재 배송을 위한 정보
            	+ "\n      , HRDC "
            	+ "\n      , ZIP_CD "
            	+ "\n      , ADDRESS "
            	+ "\n      , ZIP_CD1 "
            	+ "\n      , ADDRESS1 "
            	+ "\n      , USER_PATH "
            	+ "\n      , ISMAILLING "
            	+ "\n      , ISSMS "
            	//결제관련 정보 추가-서지한
            	+ "\n      , (select decode(trim(type), 'OB','교육청일괄납부','PB','무통장','SC0010','신용카드','SC0030','계좌이체','SC0040','가상계좌',type) from pa_payment pa where pa.order_id = b.order_id and pa.useyn='Y') as pay "
            	+ "\n      , (select type from pa_payment pa where pa.order_id = b.order_id and pa.useyn='Y') as paycd "
            	+ "\n      , (select decode(enterance_dt,null,'',to_char(to_date(enterance_dt,'yyyy-mm-dd'),'yyyy-mm-dd')) from pa_payment pa where pa.order_id = b.order_id and pa.useyn='Y') as enterance_dt "
            	+ "\n      , (select decode(enter_dt,null,'',to_char(to_date(enter_dt,'yyyy-mm-dd'),'yyyy-mm-dd')) from pa_payment pa where pa.order_id = b.order_id and pa.useyn='Y') as enter_dt "
            	+ "\n      , (select order_id from pa_payment pa where pa.order_id = b.order_id and pa.useyn='Y') as order_id "
            	+ "\n      , c.cert "
            	+ "\n      , fn_crypt('2', c.birth_date, 'knise') birth_date "
            	+ "\n      , b.etc " //비고컬럼 추가
//            	+ "\n      , decode(b.chkfinal,'Y',b.ldate,'') as approvaldate " //승인일시
            	+ "\n      , to_char(b.delete_date, 'yyyy.MM.dd HH:mm:ss') as approvaldate " //삭제일시
            	+ "\n from   tz_propose_delete b "
            	+ "\n      , vz_scsubjseq a "
            	+ "\n      , tz_member c "
            	+ "\n      , tz_stold d "
            	//+ "\n      ,( select scsubj,scyear,scsubjseq, count(*) rowspan from tz_propose_delete x, vz_scsubjseq y where x.subj=y.subj and x.year=y.year and x.subjseq=y.subjseq group by scsubj, scyear, scsubjseq, userid ) e "            	            	
            	+ "\n where  b.subj         = a.subj " 
            	+ "\n and    b.year         = a.year "
            	+ "\n and    b.subjseq      = a.subjseq "
            	+ "\n and    b.userid       = c.userid "
            	//+ "\n and    c.cert is null "
            	+ "\n and    b.subj         = d.subj(+) "
            	+ "\n and    b.year         = d.year(+) "
            	+ "\n and    b.subjseq      = d.subjseq(+) "
            	+ "\n and    b.userid       = d.userid(+) ";
            	
            
            
           /*
            //조건절은 일단 주석처리한다. 이후 데이터가 많아 지면 주석을 제거 하도록 하자.~~
         
            //결제수단 검색
            if(!"".equals(box.getString("p_search_payType"))){  
            	
            	if(!box.getString("p_search_payType").equals("etc")){
            		sql += "\n and (select trim(type) from pa_payment pa  where pa.order_id = b.order_id and pa.useyn='Y')  = '"+box.getString("p_search_payType")+"' ";
            	}else{
            		sql += "\n and (select trim(type) from pa_payment pa  where pa.order_id = b.order_id and pa.useyn='Y')  not in ('PB','OB','SC0010','SC0030','SC0040') ";
            	}
            
            }
            
            if ( !v_searchtext.equals("") ) {                             //    검색어가 있으면
            	
                if ( v_search.equals("userid") ) {                        //    ID로 검색할때
                	sql += " and upper(c.userid) like upper('%" + v_searchtext + "%')";
                } else if ( v_search.equals("name") ) {                   //    이름으로 검색할때
                	sql += " and upper(c.name) like upper('%" + v_searchtext + "%')";
                } else if ( v_search.equals("birth_date") ) {                  //    주민등록번호로  검색할때
                	sql += " and upper(substr(c.birth_date,1,6)) like upper('%" + v_searchtext + "%')";
                } else if ( v_search.equals("handphone") ) {              //    핸드폰번호로  검색할때
                	v_searchtext = v_searchtext.replace("-", "");
                	sql += " and replace(handphone,'-','') like ('%" + v_searchtext + "%')";
                } else if ( v_search.equals("user_path") ) {                  //    학교명  검색할때
                	sql += " and upper(c.user_path) like upper('%" + v_searchtext + "%')";
                }

            } 
            if ( !v_searchtext1.equals("") ) {                             //    검색어가 있으면
            	
                if ( v_search1.equals("userid") ) {                        //    ID로 검색할때
                	sql += " and upper(c.userid) like upper('%" + v_searchtext1 + "%')";
                } else if ( v_search1.equals("name") ) {                   //    이름으로 검색할때
                	sql += " and upper(c.name) like upper('%" + v_searchtext1 + "%')";
                } else if ( v_search1.equals("birth_date") ) {                  //    주민등록번호로  검색할때
                	sql += " and upper(substr(c.birth_date,1,6)) like upper('%" + v_searchtext + "%')";
                } else if ( v_search1.equals("handphone") ) {              //    핸드폰번호로  검색할때
                	v_searchtext1 = v_searchtext1.replace("-", "");
                	sql += " and replace(handphone,'-','') like ('%" + v_searchtext1 + "%')";
                } else if ( v_search1.equals("user_path") ) {                  //    학교명  검색할때
                	sql += " and upper(c.user_path) like upper('%" + v_searchtext1 + "%')";
                }

            } 
              if(!"ALL".equals(box.getString("s_gyear"))){
            	  sql += " and b.appdate between '"+box.getString("s_gyear")+"'||'0101000001' and '"+box.getString("s_gyear")+"'||'1231235959'";
              }
            
              if ( !p_grseq.equals("ALL")) sql += "\n and    a.grseq        ="  + SQLString.Format(p_grseq);
              
              // 검색방식에 따른 과목 검색조건 분기
              if ( !p_grcode.equals("ALL"))    sql +="\n and    a.grcode = " + StringManager.makeSQL(p_grcode);
              
              if ( !ss_uclass.equals("ALL"))    sql +="\n and    a.scupperclass = " + StringManager.makeSQL(ss_uclass);
              if ( !ss_mclass.equals("ALL"))    sql +="\n and    a.scmiddleclass= " + StringManager.makeSQL(ss_mclass);
              if ( !ss_lclass.equals("ALL"))    sql +="\n and    a.sclowerclass = " + StringManager.makeSQL(ss_lclass);
              if ( !p_subjcourse.equals("ALL")) sql +="\n and    a.scsubj         = " + StringManager.makeSQL(p_subjcourse);
              if ( !p_subjseq.equals("ALL"))    sql +="\n and    a.scsubjseq      = " + StringManager.makeSQL(p_subjseq);
            

			// p_appstatus : B-미처리, Y-승인, N-반려
            if ( !p_appstatus.equals("ALL") ) { 
                sql +=  "\n and    b.chkfinal = " + StringManager.makeSQL(p_appstatus);
            }

            if ( !ss_company.equals("ALL") ) { 
                    sql += "\n and    c.comp =" +StringManager.makeSQL(ss_company) + " ";
            }
            
            */
            
            
            if ( v_order.equals("subj")     )    v_order ="a.scsubj";
            if ( v_order.equals("companynm"))    v_order ="get_compnm(c.comp)";
            if ( v_order.equals("deptnm")   )    v_order ="get_deptnm(c.hq_org_cd)";
            if ( v_order.equals("jikwinm")  )    v_order ="get_postnm(c.post)";
            
           
            
            
            
            
            //if ( v_order.equals("") ) { 
            //    sql += "\n order  by b.comp, a.isonoff, a.scsubjnm ";
            //} else { 
            ///    sql += "\n order  by " + v_order + v_orderType;
            //}
            if ( v_order.equals("") ) { 
                sql += " order by a.scsubj, a.scyear, a.scsubjseq, a.scsubjnm";
            } else { 
                sql += " order by " + v_order + v_orderType;
            }    
            
            //System.out.println("---------------sql-----------------\n" + sql);

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                    data = new ApprovalScreenData();
                    data.setIsclosed           ( ls.getString("isclosed") );
                    data.setBiyong             ( ls.getInt   ("biyong") );
                    data.setCompanynm          ( ls.getString("companynm") );
                    
                    data.setGrcode	 (p_grcode);	 
    				data.setGyear	 (p_gyear);
    				data.setGrseq    (p_grseq);
    				data.setScsubj    (ls.getString("subj"));
    				data.setScyear    (ls.getString("year"));
    				data.setScsubjseq (ls.getString("subjseq"));
    				data.setScsubjnm  (ls.getString("subjnm"));    				
    				data.setCourse    (ls.getString("course"));
    				data.setCyear     (ls.getString("cyear"));
    				data.setCourseseq (ls.getString("courseseq"));
    				data.setCoursenm  (ls.getString("coursenm"));    				
    				data.setEdustart  (ls.getString("edustart"));
    				data.setEduend    (ls.getString("eduend"));
    				data.setUserid    (ls.getString("userid"));
    				data.setComp      (ls.getString("comp"));
    				data.setJikwinm   (ls.getString("jikwinm"));
    				data.setAppdate   (ls.getString("appdate"));
    				data.setIsdinsert (ls.getString("isdinsert"));
    				data.setChkfirst  (ls.getString("chkfirst"));
    				data.setChkfinal  (ls.getString("chkfinal"));
    				data.setIschkfirst(ls.getString("ischkfirst"));
    				data.setName      (ls.getString("name"));
    				data.setDeptnm    (ls.getString("deptnm"));    				
    				data.setIsgoyong  (ls.getString("isgoyong"));
    				data.setEmail(ls.getString("email"));
    				
    				data.setGoyongpricemajor(ls.getInt("goyongpricemajor"));
    				data.setGoyongpriceminor(ls.getInt("goyongpriceminor"));
    				data.setGoyongpricestand(ls.getInt("goyongpricestand"));
    				data.setIsonoff   (ls.getString("isonoff"));
    				data.setCpnm   (ls.getString("cpnm"));
    				//data.setRowspan(ls.getInt("rowspan"));
    				
    				data.setPosition_nm(ls.getString("position_nm"));
    				data.setLvl_nm(ls.getString("lvl_nm"));
    				data.setHometel(ls.getString("hometel"));
    				data.setHandphone(ls.getString("handphone"));
    				
    				data.setHRDC(ls.getString("hrdc"));
    				data.setZIP_CD(ls.getString("zip_cd"));
    				data.setADDRESS(ls.getString("address"));
    				data.setZIP_CD1(ls.getString("zip_cd1"));
    				data.setADDRESS1(ls.getString("address1"));
    				data.setUSER_PATH(ls.getString("user_path"));
    				data.setISMAILLING(ls.getString("ismailling"));
    				data.setIpayType(ls.getString("pay"));
    				data.setPaycd(ls.getString("paycd"));
    				data.setEnterance_dt(ls.getString("enterance_dt"));
    				data.setEnter_dt(ls.getString("enter_dt"));
    				data.setOrder_id(ls.getString("order_id"));
    				data.setEtc(ls.getString("etc"));
    				data.setApprovaldate(ls.getString("approvaldate"));
    				
    				//data.setCert(ls.getString("cert"));
    				//System.out.println("#######################################################");
    				//System.out.println(data.getUserid()+"::"+ls.getString("birth_date"));
	    			if(!"".equals(ls.getString("birth_date")) && ls.getString("birth_date") != null){	
    					if("Y".equals(ls.getString("cert"))){
	    					data.setbirth_date(Encrypt.com_Decode(ls.getString("birth_date")).substring(0,6)+"-"+Encrypt.com_Decode(ls.getString("birth_date")).substring(6,13));
	    					//System.out.println(data.getbirth_date());
	    				}else{
	    					data.setbirth_date(ls.getString("birth_date").substring(0,6)+"-"+ls.getString("birth_date").substring(6));
	    					//System.out.println(data.getbirth_date());
	    				}
            		}else{
            			data.setbirth_date(ls.getString("birth_date"));	
            		}
    				//data.setbirth_date(ls.getString("birth_date"));
    				list1.add(data);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }

    /**
    get Row Count
    @param ArrayList   신청리스트
    @param String      신청과목/코스코드,
    @return int         갯수
    */
    public int getRowCnt(ArrayList list1, String p_scsubj, String p_scyear, String p_scsubjseq) { 
        int ncnt = 0;
        ApprovalScreenData data = null;

        for ( int i = 0; i<list1.size(); i++ ) { 
            data = (ApprovalScreenData)list1.get(i);

            if ( data.getScsubj().equals(p_scsubj)&&data.getScyear().equals(p_scyear)&&data.getScsubjseq().equals(p_scsubjseq)) { 
                ncnt++;
            }
        }

        return ncnt;
    }
    
    /**
    승인권한값 얻기
    @param box          receive from the form object and session
    @return int     승인권한값(0,1,2,3)
* == 승인권한표 ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ==  == *
            부서담당    회사담당    그룹담당    수퍼이상
1차승인미사용시     0       2       2       2
1차승인사용시       1       3       3       3

0 : 승인권한 부재
1: 1차승인만
2: 최종승인만
3: 1차/최종승인 모두가능

 == > 최종승인 미사용 교육그룹의 경우에도 최종승인은 가능하도록 한다.
      사유: 교육그룹정보상의 1차/최종승인 사용여부는 홈페이지의 수강신청 프로세스에
              영향을 주게되나 수강신청시 자동으로 1차/최종승인되지 않은경우
              직접승인할 수 있도록 하기 위함
*------------------------------------------------------------------------------------*

    **/
    public int getApprovalAuth(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        String p_grcode     = box.getString("s_grcode");
        int    p_step       = box.getInt("p_step");
        String p_appstatus  = box.getString("p_appstatus");
        String p_gadmin     = box.getSession("gadmin");

        String  v1= "",v2= "";
        int v_appauth=0;
        String  g = p_gadmin.substring(0,1);

        // String v_Grseqnm = box.getString("p_grtype");

        try { 
            connMgr = new DBConnectionManager();

            sql = " select  nvl(chkfirst,'Y') chkfirst, nvl(chkfinal,'Y') chkfinal from tz_grcode where grcode=" +SQLString.Format(p_grcode);

            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) { 
                v1 = ls.getString("chkfirst");
                v2 = ls.getString("chkfinal");
            }
            ls.close();

            sql = " select  applevel from tz_gadmin where gadmin=" +SQLString.Format(p_gadmin);
            ls = connMgr.executeQuery(sql);
            ls.next();
            v_appauth = ls.getInt("applevel");

            if ( v1.equals("Y")&&v2.equals("Y") ) { 
            } else if ( v1.equals("Y") ) { 
                if ( v_appauth > 1) v_appauth = 1;
            } else if ( v2.equals("Y") ) { 
                if ( v_appauth<2) v_appauth = 0;
                if ( v_appauth == 3)    v_appauth = 1;
            }

            /*
            if ( v1.equals("Y")&&v2.equals("Y") ) { 
                if ( p_gadmin.substring(0,1).equals("A"))
                    v_appauth = 2;
                else if ( p_gadmin.equals("K1")||p_gadmin.equals("K2")||p_gadmin.substring(0,1).equals("F"))
                    v_appauth = 3;
                else if ( p_gadmin.substring(0,1).equals("K"))
                    v_appauth = 1;
                else    v_appauth = 0;
            } else if ( v1.equals("Y") ) { 

            } else if ( v2.equals("Y") ) { 
                if ( p_gadmin.substring(0,1).equals("A")||p_gadmin.substring(0,1).equals("F")
                    ||p_gadmin.equals("K1")||p_gadmin.equals("K2"))     v_appauth = 2;
                else    v_appauth = 0;
            }
            */

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_appauth;
    }


    /**
    집단승인처리
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int ApprovalProcess(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        ListSet             ls                  = null;
        ListSet             ls2                  = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        SendRegisterCoursesEMail mail =new SendRegisterCoursesEMailImplBean();
        EmailLog emailLog=new EmailLogImplBean();
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수         
                                                
        int                 isOk                = 0;
        int                 isOk1               = 0;
                                                
        String              p_grcode            = box.getString("p_grcode"  );
        String              p_gyear             = box.getString("p_gyear"   );
        String              p_grseq             = box.getString("p_grseq"   );
                                                
        String              ss_uclass           = box.getStringDefault("s_upperclass"   , "ALL"); // 과목분류
        String              ss_mclass           = box.getStringDefault("s_middleclass"  , "ALL"); // 과목분류
        String              ss_lclass           = box.getStringDefault("s_lowerclass"   , "ALL"); // 과목분류
                                                
        String              p_subjcourse        = box.getString("p_subjcourse"  );
        String              p_subjseq           = box.getString("p_subjseq"     );
        String              p_comp              = box.getString("p_company"     );
        int                 p_step              = box.getInt   ("p_step"        );
        String              p_appstatus         = box.getString("p_appstatus"   );
        String              p_order             = box.getString("p_order"       );
                                                
        String              p_gadmin            = box.getSession("gadmin");
                                                
        String              v1                  = "";
        String              v2                  = "";
        int                 v_appauth           = 0;

        Vector              vec_param           = box.getVector("p_params"          );
        Vector              vec_rejectkind      = box.getVector("p_rejectkind"      );
        Vector              vec_rejectedreason  = box.getVector("p_rejectedreason"  );
        Vector              vec_chk             = null;
        Vector              vec_enter_dt        = box.getVector("p_enter_dt");//입금일자
        Vector              vec_paycd           = box.getVector("paycd");//결제수단
        Vector              vec_order_id        = box.getVector("order_id");//주문번호
        Vector				vec_Oldchk			= box.getVector("p_final_chk"); // 승인 chk old
        
        if ( p_step == 1)  
            vec_chk                             = box.getVector("p_chkfirst");
        else            
            vec_chk                             = box.getVector("p_chkfinal");

        String              v_luserid           = box.getSession("userid");     // 세션변수에서 사용자 id를 가져온다.
        String              v_param             = "";                           // scsubj, scyear, scsubjseq, userid
        String              v_rejectkind        = "";
        String              v_rejectedreason    = "";

        String              v_scsubj            = "";
        String              v_scyear            = "";
        String              v_scsubjseq         = "";
        String              v_userid            = "";
        String              v_chk               = "";
        String 				v_oldchk			= "";
        
        String              v_mastercd          = "";
        String              v_targetColumn      ="chkfirst";
        
        Hashtable           insertData          = null;
        ProposeBean         propBean            = new ProposeBean();
        String              v_appstatus         = "";
        String              v_eduterm           = "";
        boolean             v_isedutarget       = false;
        
        String              v_chkfinal          = "";
    	String				v_canceldate 		= FormatDate.getDate("yyyyMMddHHmmss");
    	
    	String p_userName=null;
    	String p_email=null;
    	
    	boolean sendStatus = false;
    	
    	//mail 로그를 작성하기 위해서 tz_email_log의 tabseq의 최대값을 가져와 같이 동시에 발송된 그룹의 flag 로 한다.
        //tabseq를 flag 로 삼는 이유는 로그를 조회할 때 동시에 발송된 내역을 조회하기 위해서이다.
    	int p_tabseq=emailLog.getMaxTabseq();
        
       // System.out.println("p_tabseq===========>"+p_tabseq);
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            if ( p_step == 2)  
                v_targetColumn                  = "chkfinal";

            /*
            sbSQL.append(" select  chkfinal                                             \n")
            sbSQL.append(" from    tz_grcode                                            \n")
            sbSQL.append(" where   grcode  = " + StringManager.makeSQL(p_grcode) + "    \n");
            
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() );
            
            v_chkfinal  = ls.getString("chkfinal");
            */
                        
            v_chkfinal  = "Y"; // 최종승인 만사용
           
            
            for ( int i = 0 ; i < vec_param.size(); i++ ) { 
            	//System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::"+i);
                v_param             = vec_param.elementAt(i).toString();    // 과목코드기수Userid...
                v_chk               = vec_chk.elementAt(i).toString();      // 변경된 데이터 - 미처리/승인/반려/삭제
                v_oldchk			= vec_Oldchk.elementAt(i).toString();	// 기존 DB - 미처리/승인/반려/삭제
                
                v_rejectkind        = "";
                v_rejectedreason    = "";
                
                /*
                if ( v_chk.equals("N") ) { 
                    v_rejectkind    = vec_rejectkind.elementAt(i).toString();
                    v_rejectedreason= vec_rejectedreason.elementAt(i).toString();
                }
                */
                
                String[] v_sparam = v_param.split(",");
                
                v_scsubj          = v_sparam[0];
                v_scyear          = v_sparam[1];
                v_scsubjseq       = v_sparam[2];
                v_userid          = v_sparam[3];
                
                insertData = new Hashtable();
//System.out.println("~~~~ 승인:"+v_chk+"/"+v_userid+"/"+v_scyear+"/"+v_scsubj+"/"+v_scsubjseq);                
                if ( !(v_chk.equals("D")) ) { // 삭제처리가 아닐때
                	if(v_chk.equals("N")) {
                		v_rejectkind = "P";
                	} 
                	
                	
                    sbSQL.setLength(0);
                   if("Y".equals(v_oldchk)){
                    sbSQL.append(" UPDATE tz_propose SET                                                    \n")
                         .append("       " + v_targetColumn + "    = ?                                      \n")
                         .append("      ,   cancelkind      = ?                                             \n")    
                         .append("      ,   rejectedreason  = ?                                             \n")
                         .append("      ,   luserid         = ?                                             \n")
                       //  .append("      ,   ldate           = to_char(sysdate,'YYYYMMDDHH24MISS')           \n")
                         .append(" WHERE    userid          = ?                                             \n")
                         .append(" and      subj            = ?                                             \n")
                         .append(" and      year            = ?                                             \n")
                         .append(" and      subjseq         = ?                                             \n");
                   }else{
                	   sbSQL.append(" UPDATE tz_propose SET                                                    \n")
                       .append("       " + v_targetColumn + "    = ?                                      \n")
                       .append("      ,   cancelkind      = ?                                             \n")    
                       .append("      ,   rejectedreason  = ?                                             \n")
                       .append("      ,   luserid         = ?                                             \n")
                       .append("      ,   ldate           = to_char(sysdate,'YYYYMMDDHH24MISS')           \n")
                       .append(" WHERE    userid          = ?                                             \n")
                       .append(" and      subj            = ?                                             \n")
                       .append(" and      year            = ?                                             \n")
                       .append(" and      subjseq         = ?                                             \n");
                   }
                    pstmt   = connMgr.prepareStatement(sbSQL.toString());
                    

                    pstmt.setString(1, v_chk            );
                    pstmt.setString(2, v_rejectkind     );
                    pstmt.setString(3, v_rejectedreason );
                    pstmt.setString(4, v_userid         );
                    pstmt.setString(5, v_userid         );
                    pstmt.setString(6, v_scsubj         );
                    pstmt.setString(7, v_scyear         );
                    pstmt.setString(8, v_scsubjseq      );
                    
                    isOk    = pstmt.executeUpdate();
                    if ( pstmt != null ) { pstmt.close(); }
                    
                   
                    ////////////////////자동메일을 발송하기 위해서 수신자 이름과 수신자 이메일을 가져옴///////////
                   String sql="select name, email from tz_member where userid="+SQLString.Format(v_userid);
                    // START :: tz_propose update 성공 && 최종승인 && '승인'처리일 경우 tz_student에 Record 생성
                    if ( (isOk > 0) && (p_step == 2)) { 
                        insertData.clear();
                        
                        insertData.put("connMgr"    , connMgr       );
                        insertData.put("subj"       , v_scsubj      );
                        insertData.put("year"       , v_scyear      );
                        insertData.put("subjseq"    , v_scsubjseq   );
                        insertData.put("userid"     , v_userid      );
                        insertData.put("isdinsert"  , "N"           );
                        insertData.put("chkfirst"   , ""            );
                        insertData.put("chkfinal"   , v_chk         );
                        
                        insertData.put("box"        , box           );
                        
                        if(v_chk.equals("Y") || v_chk.equals("N")){
                        	//sendStatus = this.mailSendStatus(insertData);
                        }
                        
                       
                        if ( v_chk.equals("Y") ) { // 승인시
                        	insertData.put("status"     , "W"           );
                        	isOk = propBean.updateStatus(insertData);	//독서통신 proposebook 상태값 변경(대기)
                            isOk = propBean.insertStudent(insertData);
                            
                            
                            
                            
                            //승인시 승인 처리 자동메일 발송
                            if(sendStatus){
                               // mail.sendAcceptRegisterCoursesMail(box);
                            }
                            
                        } else if( v_chk.equals("N")) { // 반려시
                        	isOk = propBean.deleteStudent(insertData);
                        	insertData.put("status"     , "N"           );
                        	isOk = propBean.updateStatus(insertData); //독서통신 proposebook 상태값 변경(반려)
                        	insertData.put("cancelkind"   , "F");
                        	insertData.put("userid" , box.getSession("userid"));  //운영자 반려일 경우 세션 아이디를 가져온다.
                        	//System.out.println("반려시;;;vec_order_id.elementAt(i).toString()"+vec_order_id.elementAt(i).toString());
                        	
                        	//isOk = propBean.insertCancel(insertData);
                        	
                        	//반려시 반려 안내 자동메일 발송
                        	if(sendStatus){
                        		//mail.sendRejectRegisterCoursesMail(box);
                        	}
                        	
                        } else { // 미처리
                            isOk = propBean.deleteStudent(insertData);	
                            insertData.put("status"     , "W"           );
                        	isOk = propBean.updateStatus(insertData); //독서통신 proposebook 상태값 변경(대기)
                        	
                        }
                        
                        isOk1   = 1;
                    }
                    // --END :: tz_propose update 성공 && 최종승인 && '승인'처리일 경우 tz_student에 Record 생성                          
                } else {                  ///삭제처리시
                    insertData.clear();
                    
                    insertData.put("connMgr", connMgr       );
                    insertData.put("subj"   , v_scsubj      );
                    insertData.put("year"   , v_scyear      );
                    insertData.put("subjseq", v_scsubjseq   );
                    insertData.put("userid" , v_userid      );

                    insertData.put("status"     , "R"           );    //R:제거  
                    isOk = propBean.updateStatus(insertData);         //독서통신 proposebook 상태값 변경(삭제)
                    insertData.put("cancelkind"   , "D");             //D:제거
                    insertData.put("reason"   , "운영자삭제");             
                    insertData.put("reasoncd"   , "99");             
                    insertData.put("luserid" , box.getSession("userid"));  //운영자 삭제일 경우 세션 아이디를 가져온다.
                    isOk    = propBean.insertCancel(insertData);
                    isOk    = propBean.deleteStudent(insertData);
                    isOk1   = propBean.deletePropose(insertData);
                    //System.out.println("삭제시;;;vec_order_id.elementAt(i).toString()"+vec_order_id.elementAt(i).toString());
                    //삭제시에는 결제정보를 사용안함으로 변경한다.
                	isOk = propBean.UpdatePayData(insertData, vec_order_id.elementAt(i).toString());
                	
                	
                	
                }            
                isOk        = isOk * isOk1;
            }
            
            //입금일자 처리로직 추가 
            //Vector              vec_enter_dt        = box.getVector("p_enter_dt");//입금일자
            //Vector              vec_paycd           = box.getVector("paycd");//결제수단
            //Vector              vec_order_id           = box.getVector("order_id");//주문번호
            for(int i = 0; i < vec_order_id.size();i++){	
            	//결제수단이 교육청 일괄납부일때만 빼고 모두 처리함
				if(!"OB".equals(vec_paycd.elementAt(i).toString())){	
            		//if(!"".equals(vec_enter_dt.elementAt(i).toString())){
						String sql="update pa_payment set enter_dt = ? where order_id=?";
						pstmt = connMgr.prepareStatement(sql);
						pstmt.setString(1, vec_enter_dt.elementAt(i).toString().replaceAll("-",""));
						pstmt.setString(2, vec_order_id.elementAt(i).toString());
						
						logger.debug(i + ") vec_enter_dt : " +  vec_enter_dt);
						
						isOk    = pstmt.executeUpdate();
						if ( pstmt != null ) { pstmt.close(); }
            		//}
				}
            }
            
            Vector vec_paytype = box.getVector("p_paytype");//pay_type
            for(int i = 0; i < vec_order_id.size();i++){	
            	//결제수단이 교육청 일괄납부일때나 무통장일때만 업데이트 한다.
				if("OB".equals(vec_paytype.elementAt(i).toString())||"PB".equals(vec_paytype.elementAt(i).toString())){
					String sql="update pa_payment set type = ? where order_id=?";
					pstmt = connMgr.prepareStatement(sql);
					pstmt.setString(1, vec_paytype.elementAt(i).toString().replaceAll("-",""));
					pstmt.setString(2, vec_order_id.elementAt(i).toString());
					
					isOk    = pstmt.executeUpdate();
					if ( pstmt != null ) { pstmt.close(); }
        		}
            }
            //System.out.println("######################################################");
            isOk    = 1;
            
            connMgr.commit();
        } catch ( SQLException e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback(); 
                } catch ( Exception ex ) { } 
            }

            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback(); 
                } catch ( Exception ex ) { } 
            }

            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            if(ls2!=null){
            	try { 
                    ls2.close();  
                } catch ( Exception e ) { } 
            }
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
    선택승인처리
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int ApprovalProcessSelect(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        ListSet             ls                  = null;
        ListSet             ls2                  = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        SendRegisterCoursesEMail mail =new SendRegisterCoursesEMailImplBean();
        EmailLog emailLog=new EmailLogImplBean();
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수         
                                                
        int                 isOk                = 0;
        int                 isOk1               = 0;
                                                
        String              p_grcode            = box.getString("p_grcode"  );
        String              p_gyear             = box.getString("p_gyear"   );
        String              p_grseq             = box.getString("p_grseq"   );
                                                
        String              ss_uclass           = box.getStringDefault("s_upperclass"   , "ALL"); // 과목분류
        String              ss_mclass           = box.getStringDefault("s_middleclass"  , "ALL"); // 과목분류
        String              ss_lclass           = box.getStringDefault("s_lowerclass"   , "ALL"); // 과목분류
                                                
        String              p_subjcourse        = box.getString("p_subjcourse"  );
        String              p_subjseq           = box.getString("p_subjseq"     );
        String              p_comp              = box.getString("p_company"     );
        int                 p_step              = box.getInt   ("p_step"        );
        String              p_appstatus         = box.getString("p_appstatus"   );
        String              p_order             = box.getString("p_order"       );
                                                
        String              p_gadmin            = box.getSession("gadmin");
                                                
        String              v1                  = "";
        String              v2                  = "";
        int                 v_appauth           = 0;

        Vector              vec_param           = box.getVector("p_params"          );
        Vector              vec_rejectkind      = box.getVector("p_rejectkind"      );
        Vector              vec_rejectedreason  = box.getVector("p_rejectedreason"  );
        Vector              vec_chk             = null;
        Vector              vec_enter_dt        = box.getVector("p_enter_dt");//입금일자
        Vector              vec_paycd           = box.getVector("paycd");//결제수단
        Vector              vec_order_id           = box.getVector("order_id");//주문번호
        Vector              vec_appcheck1          = box.getVector("checkvalue");//체크된 히든값
        
        
        if ( p_step == 1)  
            vec_chk                             = box.getVector("p_chkfirst");
        else            
            vec_chk                             = box.getVector("p_chkfinal");

        String              v_luserid           = box.getSession("userid");     // 세션변수에서 사용자 id를 가져온다.
        String              v_param             = "";                           // scsubj, scyear, scsubjseq, userid
        String              v_rejectkind        = "";
        String              v_rejectedreason    = "";

        String              v_scsubj            = "";
        String              v_scyear            = "";
        String              v_scsubjseq         = "";
        String              v_userid            = "";
        String              v_chk               = "";
        
        String              v_mastercd          = "";
        String              v_targetColumn      ="chkfirst";
        
        Hashtable           insertData          = null;
        ProposeBean         propBean            = new ProposeBean();
        String              v_appstatus         = "";
        String              v_eduterm           = "";
        boolean             v_isedutarget       = false;
        
        String              v_chkfinal          = "";
    	String				v_canceldate 		= FormatDate.getDate("yyyyMMddHHmmss");
    	
    	String p_userName=null;
    	String p_email=null;
    	
    	boolean sendStatus = false;
    	
    	//mail 로그를 작성하기 위해서 tz_email_log의 tabseq의 최대값을 가져와 같이 동시에 발송된 그룹의 flag 로 한다.
        //tabseq를 flag 로 삼는 이유는 로그를 조회할 때 동시에 발송된 내역을 조회하기 위해서이다.
    	int p_tabseq=emailLog.getMaxTabseq();
        
    	logger.info("p_tabseq ===========> " + p_tabseq);
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            if ( p_step == 2)  
                v_targetColumn                  = "chkfinal";
      
            v_chkfinal  = "Y"; // 최종승인 만사용
            
            sbSQL.setLength(0);
            
            sbSQL.append(" UPDATE tz_propose SET                                                    \n")
                 .append("       " + v_targetColumn + "    = ?                                      \n")
                 .append("      ,   cancelkind      = ?                                             \n")    
                 .append("      ,   rejectedreason  = ?                                             \n")
                 .append("      ,   luserid         = ?                                             \n")
                 .append("      ,   ldate           = to_char(sysdate,'YYYYMMDDHH24MISS')           \n")
                 .append(" WHERE    userid          = ?                                             \n")
                 .append(" and      subj            = ?                                             \n")
                 .append(" and      year            = ?                                             \n")
                 .append(" and      subjseq         = ?                                             \n");
                 
            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            if ( pstmt != null ) { pstmt.close(); }
            
            logger.info("vec_param.size() >>>>>>> " + vec_param.size());
            
            for ( int i = 0 ; i < vec_param.size(); i++ ) { 
                v_param             = vec_param.elementAt(i).toString();    // 과목코드기수Userid...
                v_chk               = "Y"; //항상승인으로

                
                v_rejectkind        = "";
                v_rejectedreason    = "";
                
                
                String[] v_sparam = v_param.split(",");
                
                v_scsubj          = v_sparam[0];
                v_scyear          = v_sparam[1];
                v_scsubjseq       = v_sparam[2];
                v_userid          = v_sparam[3];
                
                insertData = new Hashtable();
                logger.info("~~~~ 승인:"+v_chk+"/"+v_userid+"/"+v_scyear+"/"+v_scsubj+"/"+v_scsubjseq); 
                if ( !(v_chk.equals("D"))&&("1".equals(vec_appcheck1.elementAt(i).toString())) ) { // 삭제처리가 아니면서 체크박스에 체크한경우
                	if(v_chk.equals("N")) {
                		v_rejectkind = "P";
                	} 

                    pstmt.setString(1, v_chk            );
                    pstmt.setString(2, v_rejectkind     );
                    pstmt.setString(3, v_rejectedreason );
                    pstmt.setString(4, v_userid         );
                    pstmt.setString(5, v_userid         );
                    pstmt.setString(6, v_scsubj         );
                    pstmt.setString(7, v_scyear         );
                    pstmt.setString(8, v_scsubjseq      );
                    
                    isOk    = pstmt.executeUpdate();
                    if ( pstmt != null ) { pstmt.close(); }
                    
                   
                    ////////////////////자동메일을 발송하기 위해서 수신자 이름과 수신자 이메일을 가져옴///////////
                   String sql="select name, email from tz_member where userid="+SQLString.Format(v_userid);
                    // START :: tz_propose update 성공 && 최종승인 && '승인'처리일 경우 tz_student에 Record 생성
                    if ( (isOk > 0) && (p_step == 2)) { 
                        insertData.clear();
                        
                        insertData.put("connMgr"    , connMgr       );
                        insertData.put("subj"       , v_scsubj      );
                        insertData.put("year"       , v_scyear      );
                        insertData.put("subjseq"    , v_scsubjseq   );
                        insertData.put("userid"     , v_userid      );
                        insertData.put("isdinsert"  , "N"           );
                        insertData.put("chkfirst"   , ""            );
                        insertData.put("chkfinal"   , v_chk         );                        
                        insertData.put("box"        , box           );
                        
                       
                        if ( v_chk.equals("Y") ) { // 승인시
                        	insertData.put("status"     , "W"           );
                        	isOk = propBean.updateStatus(insertData);	//독서통신 proposebook 상태값 변경(대기)
                            isOk = propBean.insertStudent(insertData);
                            
                            //승인시 승인 처리 자동메일 발송
                            if(sendStatus){
                               // mail.sendAcceptRegisterCoursesMail(box);
                            }
                        }
                        
                        isOk1   = 1;
                    }
                    // --END :: tz_propose update 성공 && 최종승인 && '승인'처리일 경우 tz_student에 Record 생성                          
                }          
                isOk        = isOk * isOk1;
            }
            
            isOk    = 1;
            
            connMgr.commit();
        } catch ( SQLException e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback(); 
                } catch ( Exception ex ) { } 
            }

            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback(); 
                } catch ( Exception ex ) { } 
            }

            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            if(ls2!=null){
            	try { 
                    ls2.close();  
                } catch ( Exception e ) { } 
            }
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
     * 메일 발송전 신청 반려 상태 확인 
     * @param data
     * @return
     * @throws Exception
     */
    public boolean mailSendStatus(Hashtable data) throws Exception { 
        boolean             v_CreateConnManager = false;
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        ListSet             ls      = null;
        ArrayList           list                = null;
        SubjseqData         v_subjseqdata       = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        int                 isOk                = 1;
        
        String              v_subj              = (String)data.get("subj"   );
        String              v_year              = (String)data.get("year"   );
        String              v_subjseq           = (String)data.get("subjseq");
        String              v_userid            = (String)data.get("userid" );
        String              v_status            = (String)data.get("status" );
        String              v_chkfinal          = (String)data.get("chkfinal" );
        String              v_tablenm = "";
        int                 v_cnt = 0;
        boolean             result = true;
        
        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            
            if ( connMgr == null ) { 
                v_CreateConnManager = true;

                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);
            }

            if(v_chkfinal.equals("Y")){
            	v_tablenm = "TZ_STUDENT";
            }else if(v_chkfinal.equals("N")){
            	v_tablenm = "TZ_CANCEL";
            }
           
            sbSQL.append(" select count(userid) cnt from "+v_tablenm+" where subj='"+v_subj+"' and year='"+v_year+"' and subjseq='"+v_subjseq+"' and userid='"+v_userid+"' ");
            
            ls = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                v_cnt = ls.getInt("cnt");
            }
            
            if ( ls != null ) { try {ls.close();} catch ( Exception e ) { } }
            
            if(v_cnt > 0){
            	result = false;
            }
            
            if(v_chkfinal.equals("N")){
            	
            	sbSQL.setLength(0);
            	sbSQL.append(" select count(userid) cnt ");
            	sbSQL.append("   from TZ_PROPOSE where subj='"+v_subj+"' and year='"+v_year+"' and subjseq='"+v_subjseq+"' and userid='"+v_userid+"' and chkfinal = 'N' ");
            
	            ls = connMgr.executeQuery(sbSQL.toString());
	            
	            while ( ls.next() ) { 
	                v_cnt = ls.getInt("cnt");
	            }
	            
	            if(v_cnt > 0){
	            	result = false;
	            }
            }
            
            
            if ( v_CreateConnManager ) {
                connMgr.commit();
            }   
            
        } catch ( SQLException e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( v_CreateConnManager ) {
                try {
                    connMgr.rollback();
                } catch ( Exception ex) {}
            }        
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            
            if ( pstmt != null ) { 
                try { 
                    pstmt.close();
                } catch ( Exception e ) { } 
            }
        
            if ( v_CreateConnManager ) { 
                if ( connMgr != null ) { 
                    try { 
                        connMgr.setAutoCommit(true);
                        connMgr.freeConnection(); 
                    } catch ( Exception e ) { } 
                }
            }
        }
        
        return result;
    }
    
    /**
    개인승인처리
    @param box          receive from the form object and session
    @return int     결과값(0,1)
    **/
    public int PersonApprovalProcess(RequestBox box) throws Exception { 
        DBConnectionManager connMgr             = null;
        PreparedStatement   pstmt               = null;
        ListSet             ls                  = null;
        ListSet             ls2                  = null;
        StringBuffer        sbSQL               = new StringBuffer("");
        SendRegisterCoursesEMail mail=new SendRegisterCoursesEMailImplBean();
        EmailLog emailLog=new EmailLogImplBean();
        
        int                 iSysAdd             = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수         
                                                
        int                 isOk                = 0;
        int                 isOk1               = 0;
                                                
        String              v_subj              = box.getString ("p_subj"           );
        String              v_subjseq           = box.getString ("p_subjseq"        );
        String              v_year              = box.getString ("p_year"           );
        String              v_appstatus         = box.getString ("p_appstatus"      );
        String              v_userid            = box.getSession("userid"           );
        String              v_rejectkind        = box.getString ("p_rejectkind"     );
        String              v_rejectedreason    = box.getString ("p_rejectedreason" );
        String              v_targetColumn      ="chkfinal";
        
        Hashtable           insertData          = null;
        ProposeBean         propBean            = new ProposeBean();
        
        String p_userName=null;
    	String p_email=null;
    	
    	  
        //mail 로그를 작성하기 위해서 tz_email_log의 tabseq의 최대값을 가져와 같이 동시에 발송된 그룹의 flag 로 한다.
        //tabseq를 flag 로 삼는 이유는 로그를 조회할 때 동시에 발송된 내역을 조회하기 위해서이다.
    	int p_tabseq=emailLog.getMaxTabseq();
        box.put("p_tabseq", p_tabseq);
        //System.out.println("p_tabseq===========>"+p_tabseq);
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            

            sbSQL.setLength(0);
            
            sbSQL.append(" update tz_propose set                                                    \n")
                 .append("       " + v_targetColumn + "    = ?                                      \n")
                 .append("      ,   rejectkind      = ?                                             \n")    
                 .append("      ,   rejectedreason  = ?                                             \n")
                 .append("      ,   luserid         = ?                                             \n")
                 .append("      ,   ldate           = to_char(sysdate,'yyyymmddhh24miss')           \n")
                 .append(" where    userid          = ?                                             \n")
                 .append(" and      subj            = ?                                             \n")
                 .append(" and      year            = ?                                             \n")
                 .append(" and      subjseq         = ?                                             \n");
                 
            pstmt   = connMgr.prepareStatement(sbSQL.toString());
                
            insertData = new Hashtable();
                
            if ( !(v_appstatus.equals("D")) ) { // 삭제처리가 아닐때

                pstmt.setString(1, v_appstatus      );
                pstmt.setString(2, v_rejectkind     );
                pstmt.setString(3, v_rejectedreason );
                pstmt.setString(4, v_userid         );
                pstmt.setString(5, v_userid         );
                pstmt.setString(6, v_subj           );
                pstmt.setString(7, v_year           );
                pstmt.setString(8, v_subjseq        );
                
                //System.out.println(this.getClass().getName() + "." + "PersonApprovalProcess() Printing Order " + ++iSysAdd + ". ======[v_userid ] : " + " [" + v_userid     + "]\n");
                //System.out.println(this.getClass().getName() + "." + "PersonApprovalProcess() Printing Order " + ++iSysAdd + ". ======[v_subj   ] : " + " [" + v_subj       + "]\n");
                //System.out.println(this.getClass().getName() + "." + "PersonApprovalProcess() Printing Order " + ++iSysAdd + ". ======[v_year   ] : " + " [" + v_year       + "]\n");
                //System.out.println(this.getClass().getName() + "." + "PersonApprovalProcess() Printing Order " + ++iSysAdd + ". ======[v_subjseq] : " + " [" + v_subjseq    + "]\n");
                
                
                isOk    = pstmt.executeUpdate();
                
                
                ////////////////////자동메일을 발송하기 위해서 수신자 이름과 수신자 이메일을 가져옴///////////
                String sql="select name, email from tz_member where userid="+SQLString.Format(v_userid);
                ls2=connMgr.executeQuery(sql);
                while(ls2.next()){
             	   p_userName=ls2.getString("name");
             	   p_email=ls2.getString("email");
                }
                
            	box.put("p_userid", v_userid); 
            	box.put("p_userName", p_userName);
                box.put("p_email", p_email);
                box.put("p_subjseq", v_subjseq);
                box.put("p_year", v_year);
                box.put("p_subj", v_subj);
                 //System.out.println("p_userName======>"+p_userName);
                 //System.out.println("p_email=========>"+p_email);
                 /////////////////////////////////////////////////////////////////////////////////
                
              
                
                
                //System.out.println(this.getClass().getName() + "." + "PersonApprovalProcess() Printing Order " + ++iSysAdd + ". ======[isOk] : " + " [" + isOk + "]\n");
                    
                // START :: tz_propose update 성공 && 최종승인 && '승인'처리일 경우 tz_student에 Record 생성
                if ( isOk > 0 ) { 
                    insertData.clear();
                    
                    insertData.put("connMgr"    , connMgr       );
                    insertData.put("subj"       , v_subj        );
                    insertData.put("year"       , v_year        );
                    insertData.put("subjseq"    , v_subjseq     );
                    insertData.put("userid"     , v_userid      );
                    insertData.put("isdinsert"  , "N"           );
                    insertData.put("chkfirst"   , ""            );
                    insertData.put("chkfinal"   , v_appstatus   );
                    
                    insertData.put("box"        , box           );
                    
               
                    if ( v_appstatus.equals("Y") ) {
                        isOk = propBean.insertStudent(insertData);
                        
                        //승인 시 자동메일 발송
                    	mail.sendAcceptRegisterCoursesMail(box);
                    }else{ 
                        isOk = propBean.deleteStudent(insertData);
                        //반려시 자동메일 발송
                    	mail.sendRejectRegisterCoursesMail(box);
                    }
                    
                    //System.out.println(this.getClass().getName() + "." + "PersonApprovalProcess() Printing Order " + ++iSysAdd + ". ======[isOk] : " + " [" + isOk + "]\n");
                    
                    isOk1   = 1;
                }
                // --END :: tz_propose update 성공 && 최종승인 && '승인'처리일 경우 tz_student에 Record 생성
            } else {                  ///삭제처리시
                insertData.clear();
                
                insertData.put("connMgr", connMgr       );
                insertData.put("subj"   , v_subj        );
                insertData.put("year"   , v_year        );
                insertData.put("subjseq", v_subjseq     );
                insertData.put("userid" , v_userid      );

                isOk    = propBean.deleteStudent(insertData);
                isOk1   = propBean.deletePropose(insertData);
            }
                
            isOk        = 1;
            
            connMgr.commit();
        } catch ( SQLException e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback(); 
                } catch ( Exception ex ) { } 
            }

            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) {
            if ( connMgr != null ) { 
                try { 
                    connMgr.rollback(); 
                } catch ( Exception ex ) { } 
            }

            isOk    = 0;
            
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally {
            if ( ls != null ) { 
                try { 
                    ls.close();  
                } catch ( Exception e ) { } 
            }
            if(ls2!=null){
            	try { 
                    ls2.close();  
                } catch ( Exception e ) { } 
            }
            
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
    기수정보
    @param box      receive from the form object and session
    @return DataBox
    */
     public DataBox getSubjInfomat(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        DataBox dbox        = null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        ProposeBean probean = new ProposeBean();
        
        Hashtable subjAppStatus = new Hashtable(); // 교육개시 결재상신상태
        
        String sql          = "";
        String  ss_subj     = box.getString("s_subjcourse");
        String  ss_subjseq  = box.getString("s_subjseq");
        String  ss_grcode   = box.getString("s_grcode");
        String  ss_gyear    = box.getString("s_gyear");
        String  ss_grseq    = box.getString("s_grseq");
        String  v_year      = probean.getSubjYear(ss_grcode, ss_gyear, ss_grseq, ss_subj, ss_subjseq);
        String  v_ispropose = "";
        String isManagerPropose = "";  // 교육개시 결재상신 가능여부

        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
 
            sql = " select                   \n";
            sql += "   a.subjnm,              \n";
            sql += "   a.subjseq,             \n";
            sql += "   a.edustart,            \n";
            sql += "   a.eduend,              \n";
            sql += "   (select mastercd from tz_mastersubj where subjcourse =" +SQLString.Format(ss_subj) + " and subjseq = " +SQLString.Format(ss_subjseq) + " and year = " +SQLString.Format(v_year) + ") mastercd, \n";
            sql += "   (select masternm from tz_mastercd where mastercd =(select mastercd from tz_mastersubj where subjcourse  =" +SQLString.Format(ss_subj) + " and subjseq = " +SQLString.Format(ss_subjseq) + " and year = " +SQLString.Format(v_year) + ") ) masternm, \n";
            sql += "   (select proposetype from tz_mastercd where mastercd =(select mastercd from tz_mastersubj where subjcourse  =" +SQLString.Format(ss_subj) + " and subjseq = " +SQLString.Format(ss_subjseq) + " and year = " +SQLString.Format(v_year) + ") ) proposetype, \n";
            sql += "   (select isedutarget from tz_mastercd where mastercd =(select mastercd from tz_mastersubj where subjcourse  =" +SQLString.Format(ss_subj) + " and subjseq = " +SQLString.Format(ss_subjseq) + " and year = " +SQLString.Format(v_year) + ") ) isedutarget, \n";
            sql += "   (select count(userid) from tz_edutarget where mastercd =(select mastercd from tz_mastersubj where subjcourse  =" +SQLString.Format(ss_subj) + " and subjseq = " +SQLString.Format(ss_subjseq) + " and year = " +SQLString.Format(v_year) + ") ) educnt \n";
            sql += " from vz_scsubjseq a \n";
            sql += " where          \n"; 
            sql += " a.subj=" +SQLString.Format(ss_subj) + "\n";
            sql += " and a.subjseq=" +SQLString.Format(ss_subjseq) + "\n";
            sql += " and a.year=" +SQLString.Format(v_year) + "\n";
            
            // System.out.println(sql);

            ls1 = connMgr.executeQuery(sql);
            if ( ls1 != null ) { 
              if ( ls1.next() ) { 
               dbox  = ls1.getDataBox();
              }
            }
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }
     
     
     /**
  	 비고 저장
     @param box      receive from the form object and session
     @return int
     */
      public int etcInsert(RequestBox box) throws Exception { 
    	DBConnectionManager connMgr = null;
  		PreparedStatement   pstmt   = null;
  		StringBuffer strSQL = null;
  		int isOk = 0;
           
         String  v_subj      = box.getString("p_subj");
         String  v_year      = box.getString("p_year");
         String  v_subjseq   = box.getString("p_subjseq");
         String  v_userid    = box.getString("p_userid");
         String v_etc		 = box.getString("p_etc");

         int preIdx = 1;
 		
 		try { 
 			connMgr = new DBConnectionManager();
 			connMgr.setAutoCommit(false);
 			
 			strSQL = new StringBuffer();
 			
 			strSQL.append(" update tz_propose set etc = ? ");
 			strSQL.append(" where subj = ? ");
 			strSQL.append("   and year = ? ");
 			strSQL.append("   and subjseq = ? ");
 			strSQL.append("   and userid = ? ");
 			
 			pstmt = connMgr.prepareStatement(strSQL.toString());
 			
 			preIdx = 1;
 			
 			pstmt.setString(preIdx++, v_etc);
 			pstmt.setString(preIdx++, v_subj);
 			pstmt.setString(preIdx++, v_year);
 			pstmt.setString(preIdx++, v_subjseq);
 			pstmt.setString(preIdx++, v_userid);
 			
 			isOk = pstmt.executeUpdate();
 		}
 		
 		catch ( Exception ex ) { 
 			ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
 			throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
 		}
 		finally { 
 			if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
 			if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
 		}
 		
 		return isOk;
     }
     //비고 조회
      public DataBox selectEtcInfo(RequestBox box) throws Exception { 
  		DBConnectionManager	connMgr	    = null;
  		DataBox             dbox        = null;
  		ListSet             ls1         = null;
  		String              sql         = "";
  		
  		String v_subj = box.getString("p_subj"); 
  		String v_year = box.getString("p_year"); 
  		String v_subjseq = box.getString("p_subjseq"); 
  		String v_userid = box.getString("p_userid"); 
  	
  		try { 
  			connMgr     = new DBConnectionManager();
  		
  			sql = "select etc from tz_propose		\n"
  				+ "where subj = "+StringManager.makeSQL(v_subj)+"	 \n"	
  			    + "  and year = "+StringManager.makeSQL(v_year)+"	 \n"	
  			    + "  and subjseq = "+StringManager.makeSQL(v_subjseq)+" 	\n"	
  		     	+ "  and userid = "+StringManager.makeSQL(v_userid)+" 	\n";	
  		
  			ls1         = connMgr.executeQuery(sql);
  		//System.out.println("-비고조회-" + sql);
  			while ( ls1.next() ) { 
  				dbox    = ls1.getDataBox();
  			}
  		} catch ( SQLException e ) {
  			ErrorManager.getErrorStackTrace(e, box, sql);
  			throw new Exception("\n SQL : [\n" + sql + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
  		} catch ( Exception e ) {
  			ErrorManager.getErrorStackTrace(e, box, "");
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
  	
  		return dbox;
  	}
      
}
