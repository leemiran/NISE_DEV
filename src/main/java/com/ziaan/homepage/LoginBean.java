// **********************************************************
//  1. 제      목: 로긴관리
//  2. 프로그램명 : LoginBean.java
//  3. 개      요: 로그인,패스워드찾기
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 7.  2
//  7. 수      정:
// **********************************************************
package com.ziaan.homepage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormMail;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.MailSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.SmsRepository;
import com.ziaan.library.SmsSendBean;
import com.ziaan.library.StringManager;
import com.ziaan.system.CountBean;
import com.ziaan.system.MemberData;
import com.ziaan.system.OutUserAdminBean;

public class LoginBean {

    public LoginBean() { }


    /**
     * 로그인 (세션 세팅)
     * @param box       receive from the form object and session
     * @return is_Ok    1 : login ok      -1 : ID 오류    -2 : 퇴직자    -3 : 비밀번호 오류
     * @throws Exception
     */
    public int checkLoginGubun(RequestBox box) throws Exception {
        ConfigSet           config          = new ConfigSet();

        int                 is_ok           = 1;

        String              v_userip        = box.getString("p_userip");
        String              v_tem_grcode    = box.getSession("tem_grcode");
        String              s_gubun         = box.getSession("gubun");
        String              v_ultraflag     = (String)config.getProperty("manager.ultra.checkflag");
        String              v_profflag      = (String)config.getProperty("manager.prof.checkflag" );
        String              v_olspflag      = (String)config.getProperty("manager.olsp.checkflag" );
        String              v_ultraip       = (String)config.getProperty("manager.ultra.ip");
        String              v_profip        = (String)config.getProperty("manager.prof.ip" );
        String              v_olspip        = (String)config.getProperty("manager.olsp.ip" );
        int                 v_fromip        = 0;
        int                 v_toip          = 0;
        int                 v_checkip       = 0;

        try {
            if ( s_gubun.equals("admin") ) {
                if ( v_ultraflag.equals("Y") ) {
                    String [] v_arrultra        = v_ultraip.split(",");

                    if ( v_arrultra != null && v_arrultra.length > 0 ) {
                        for ( int i = 0 ; i < v_arrultra.length ; i++ ) {
                            String [] v_arrscultra   = v_arrultra[i].split("-");

                            StringTokenizer v_tokenultra1   = new StringTokenizer(v_arrscultra[0].trim(), ".");
                            StringTokenizer v_tokenultra2   = new StringTokenizer(v_arrscultra[1].trim(), ".");
                            StringTokenizer v_tokenuserip   = new StringTokenizer(v_userip.trim()       , ".");

                            is_ok   = 0;

                            while ( v_tokenultra1.hasMoreTokens() ) {
                                v_fromip   = Integer.parseInt(v_tokenultra1.nextToken());
                                v_toip     = Integer.parseInt(v_tokenultra2.nextToken());
                                v_checkip  = Integer.parseInt(v_tokenuserip.nextToken());

                                if ( v_checkip >= v_fromip && v_checkip <= v_toip ) {
                                    is_ok   = 1;
                                    break;
                                } else {
                                    break;
                                }
                            }

                            if ( is_ok == 1 )
                                break;
                        }
                    } else {
                        is_ok   = 0;
                    }
                } else {
                    is_ok   = 1;
                }
            } else if ( s_gubun.equals("olsp") ) {
                if ( v_olspflag.equals("Y") ) {
                    String [] v_arrolsp        = v_olspip.split(",");

                    if ( v_arrolsp != null && v_arrolsp.length > 0 ) {
                        for ( int i = 0 ; i < v_arrolsp.length ; i++ ) {
                            String [] v_arrscolsp   = v_arrolsp[i].split("-");

                            StringTokenizer v_tokenolsp1   = new StringTokenizer(v_arrscolsp[0].trim(), ".");
                            StringTokenizer v_tokenolsp2   = new StringTokenizer(v_arrscolsp[1].trim(), ".");
                            StringTokenizer v_tokenuserip   = new StringTokenizer(v_userip.trim()       , ".");

                            is_ok   = 0;

                            while ( v_tokenolsp1.hasMoreTokens() ) {
                                v_fromip   = Integer.parseInt(v_tokenolsp1.nextToken());
                                v_toip     = Integer.parseInt(v_tokenolsp2.nextToken());
                                v_checkip  = Integer.parseInt(v_tokenuserip.nextToken());

                                if ( v_checkip >= v_fromip && v_checkip <= v_toip ) {
                                    is_ok   = 1;
                                    break;
                                } else {
                                    break;
                                }
                            }

                            if ( is_ok == 1 )
                                break;
                        }
                    } else {
                        is_ok   = 0;
                    }
                } else {
                    is_ok   = 1;
                }
            } else if ( s_gubun.equals("prof") ) {
                if ( v_profflag.equals("Y") ) {
                    String [] v_arrprof        = v_profip.split(",");

                    if ( v_arrprof != null && v_arrprof.length > 0 ) {
                        for ( int i = 0 ; i < v_arrprof.length ; i++ ) {
                            String [] v_arrscprof   = v_arrprof[i].split("-");

                            StringTokenizer v_tokenprof1   = new StringTokenizer(v_arrscprof[0].trim(), ".");
                            StringTokenizer v_tokenprof2   = new StringTokenizer(v_arrscprof[1].trim(), ".");
                            StringTokenizer v_tokenuserip   = new StringTokenizer(v_userip.trim()       , ".");

                            is_ok   = 0;

                            while ( v_tokenprof1.hasMoreTokens() ) {
                                v_fromip   = Integer.parseInt(v_tokenprof1.nextToken());
                                v_toip     = Integer.parseInt(v_tokenprof2.nextToken());
                                v_checkip  = Integer.parseInt(v_tokenuserip.nextToken());

                                if ( v_checkip >= v_fromip && v_checkip <= v_toip ) {
                                    is_ok   = 1;
                                    break;
                                } else {
                                    break;
                                }
                            }

                            if ( is_ok == 1 )
                                break;
                        }
                    } else {
                        is_ok   = 0;
                    }
                }
            } else {
                is_ok   = 1;
            }
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, "");
            throw new Exception( ex.getMessage() );
        }

        return is_ok;
    }


    /**
     * 로그인 (세션 세팅)
     * @param box       receive from the form object and session
     * @return is_Ok    1 : login ok      -1 : ID 오류    -2 : 퇴직자    -3 : 비밀번호 오류
     * @throws Exception
     */
   public int login(RequestBox box) throws Exception {
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ConfigSet           config          = new ConfigSet();

        String              sql             = "";
        String              sql1            = "";
        String              sql2            = "";
        String              sql3            = "";
        MemberData          data            = null;
        int                 is_Ok           = 0;
        int                 is_Ok2          = 0;
        int                 v_lgfail        = 0;
        int                 lgfailcnt       = 0;

        String              v_userid        = box.getString("p_userid");
        String              v_pwd           = box.getString("p_pwd");
        //2008.11.10 오충현 수정 암호화 정책 현재는 주석처리 나중에 주석풀어야한다.
        //v_pwd			= StringManager.encode(v_pwd);
        String              v_userip        = box.getString("p_userip");
        String              v_tem_grcode    = box.getSession("tem_grcode");

        String v_birth_date         	= "";
        String v_name          	= "";
        String v_email         	= "";
        String v_comp          	= "";
        String v_compnm          	= "";
        String v_hometel       	= "";
        String v_handphone     	= "";
        String v_gubun         	= "";
        String v_grcode        	= "";
        String v_retire_date   	= "";
        String v_isretire      	= "";
        String v_retire_type   	= "";
        String v_retire_reason 	= "";
        String v_job_cd 		= "";
        String v_lgip 			= "";
        String v_bon_adm 		= "";
        String v_hqorgcd 		= "";
        String v_gubn 			= "";
        String v_domain			= "";

        int    v_lgexceptcnt = Integer.parseInt(config.getProperty("login.except.value") );
        String lgexcept      = config.getProperty("login.except.isuse");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql1= "\n select a.userid "
            	+ "\n      , fn_crypt('2', a.birth_date, 'knise') birth_date "
            	+ "\n      , a.name "
            	+ "\n      , a.email "
            	+ "\n      , fn_crypt('2', pwd, 'knise') pwd "
            	+ "\n      , a.comp "
            	+ "\n      , get_compnm(a.comp) compnm "
            	+ "\n      , a.hometel "
            	+ "\n      , a.lgfail "
            	+ "\n      , a.handphone "
            	+ "\n      , a.lgip "
            	+ "\n      , ( "
            	+ "\n         select DECODE(NVL(MIN(b.gadmin), 'N'), 'N', 'N', 'A201', 'O', 'M1', 'O', 'T1', 'O', 'A1', 'Y', 'A2', 'Y', 'P') "
            	+ "\n         from   tz_manager c, tz_gadmin b "
            	+ "\n         where  c.gadmin    = b.gadmin "
            	+ "\n         and    c.userid    = a.userid "
            	+ "\n         and    c.isdeleted = 'N' "
            	+ "\n         and    to_char(sysdate,'yyyymmdd') between c.fmon and c.tmon "
            	+ "\n        ) gubun "
            	+ "\n      , ( "
            	+ "\n         select grcode "
            	+ "\n         from   tz_grcomp " 
            	+ "\n         where  comp = a.comp "
            	+ "\n        ) grcode "
            	+ "\n      , isretire "
            	+ "\n      , retire_date "
            	+ "\n      , get_codenm('0069',retire_type) retire_type "
            	+ "\n      , retire_reason "
            	+ "\n      , job_cd "
            	+ "\n      , nvl(bon_adm,'N') as bon_adm "
            	+ "\n      , hq_org_cd "
              	+ "\n      , emp_gubun " 
              	+ "\n,       (select domain from tz_grcode where grcode = ( select grcode from   tz_grcomp where  comp = a.comp)) domain"
                       	+ "\n from   tz_member a " 
            	+ "\n where  a.userid = " + StringManager.makeSQL(v_userid);
             
            ls = connMgr.executeQuery(sql1);

            // 로그인 결과
            // 1 : 성공
            // -1 : 사용자 없음
            // -2 : 퇴직자
            // -3 : 비밀번호 오류
            // -6 : 탈퇴 회원
            if ( ls.next() ) {
                //DataBox dbox = (DataBox)ls.get(i);
                
                v_userid        = ls.getString("userid");
                v_birth_date         = ls.getString("birth_date");
                v_name          = ls.getString("name");
                v_email         = ls.getString("email");
                v_comp          = ls.getString("comp");
                v_compnm        = ls.getString("compnm");
                v_hometel       = ls.getString("hometel");
                v_handphone		= ls.getString("handphone");
                v_gubun         = ls.getString("gubun");
                v_grcode        = ls.getString("grcode");
                v_retire_date   = ls.getString("retire_date");
                v_isretire      = ls.getString("isretire");
                v_retire_type   = ls.getString("retire_type");
                v_retire_reason = ls.getString("retire_reason");
                v_lgfail        = ls.getInt("lgfail");
                v_job_cd        = ls.getString("job_cd");
                v_lgip        	= ls.getString("lgip");
                v_bon_adm      	= ls.getString("bon_adm");	//본부담당교육여부
                v_hqorgcd      	= ls.getString("hq_org_cd");//소속(본부코드)
                v_gubn			= ls.getString("emp_gubun");//예전권한
                v_domain		= ls.getString("domain");//게이트 페이지도메인

                box.put("p_retire_date", v_retire_date);
                box.put("p_retire_type", v_retire_type);
                box.put("p_retire_reason", ls.getString("retire_reason"));

                if ( v_lgfail<v_lgexceptcnt||!lgexcept.equals("true") ) {
                  //if ( StringManager.decode(ls.getString("pwd")).equals(v_pwd)) {
                  if(ls.getString("pwd").equals(v_pwd) || box.getString("p_issso").equals("Y")) {
                      if("Y".equals(v_isretire)) {
                          is_Ok = -6; // 탈퇴한 회원
                      } else {
                          is_Ok = 1; // 성공
                      }

/* 2006.06.15 - 제거  [퇴직자란 개념이 없음]
                      // 퇴직자는 로그인 할 수 없음
                      if ( ls.getString("office_gbn").equals("Y") ) {
                          is_Ok = 1; // 성공
                      } else {
                          is_Ok = -2; // 퇴직자
                      }
*/
                  } else {
                    is_Ok = -3; // 비밀번호 오류
                    lgfailcnt = v_lgfail +1;
                    box.put("p_lgfailcnt", String.valueOf(lgfailcnt));
                  }
                } else {
                  is_Ok = -4; // Login Exception 로그인 제한
                }
            } else {
                is_Ok = -1; // 사용자정보 없음
            }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}

            if ( is_Ok == 1) {
            	box.setSession("gubun1", v_gubun);
            	String s_gubun = box.getSession("gubun");
            	String s_gubun1 = box.getSession("gubun1");

                box.setSession("userid"		,v_userid  );
                box.setSession("birth_date"		,v_birth_date   );
                box.setSession("name"		,v_name    );
                box.setSession("email"		,v_email   );
                box.setSession("comp"		,v_comp    );
                box.setSession("compnm"		,v_compnm  );
                box.setSession("grcode"		,v_grcode  );
                box.setSession("jobcd"		,v_job_cd  );                    
                box.setSession("hometel"	,v_hometel );
                box.setSession("handphone"	,v_handphone);

                box.setSession("userip", v_userip);
                box.setSession("buserip", v_lgip);
                box.setSession("bonadm", v_bon_adm);	//본부담당교육여부
                box.setSession("hqorgcd", v_hqorgcd);	//소속(본부코드)
                box.setSession("domain", v_domain);	//소속(본부코드)
                
                box.setSession("gadmin","ZZ"); 
                
                box.setSession("emp_id", v_userid  );
                box.setSession("emp_nm", v_userid  );
                box.setSession("emp_gubn", v_gubn  );
                box.setSession("email_id",  v_email   );

                // 접속통계 누적 부분
                CountBean bean1 = new CountBean();
                bean1.writeLog(connMgr, box);    // 로그 작성

                // LOGIN DATA UPDATE
                is_Ok2 = updateLoginData(connMgr, v_userid, v_userip, v_name, v_comp);
                is_Ok2 = updateLoginFail(connMgr, v_userid, "Y");

                connMgr.commit();
           }

           if ( is_Ok == -3) {
             // 오류 회수 추가
             is_Ok2 = updateLoginFail(connMgr, v_userid, "N");

             if ( lgfailcnt == v_lgexceptcnt) {
                is_Ok = -4;
             }

             connMgr.commit();
           }
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }

    /**
     * 패스워드 검색
     * @param box       receive from the form object and session
     * @return String   패스워드 검색 결과
     * @throws Exception
     */
    public String getPwd(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls1 = null;
        ListSet ls2 = null;
        String sql1 = "";
        String sql2 = "";
        String v_userid = box.getString("p_userid");
        String v_name   = box.getString("p_name");
        String v_birth_date1 = box.getString("p_birth_date1");
        String v_birth_date2 = box.getString("p_birth_date2");
        String v_birth_date  = v_birth_date1 + v_birth_date2;

        String v_tem_grcode = box.getSession("tem_grcode");
        String result = "";

        try {
            connMgr = new DBConnectionManager();

            // 아이디로 비교
            sql1  = " select fn_crypt('2', pwd, 'knise') pwd      ";
            sql1 += " from TZ_MEMBER  ";
            sql1 += " where userid = lower(" + StringManager.makeSQL(v_userid) + ")";
            sql1 += "   and birth_date  = " + StringManager.makeSQL(v_birth_date);
            sql1 += "   and name   = " + StringManager.makeSQL(v_name);

            ls1 = connMgr.executeQuery(sql1);
            if ( ls1.next() ) {
                result = ls1.getString("pwd");
            } else {
                // 사번으로 비교
                sql2  = " select fn_crypt('2', pwd, 'knise') pwd      ";
                sql2 += " from TZ_MEMBER  ";
                sql2 += " where cono   = lower(" + StringManager.makeSQL(v_userid) + " )";
                sql2 += "   and comp in (select comp from tz_grcomp where grcode = " + StringManager.makeSQL(v_tem_grcode) + ")   ";
                sql2 += "   and birth_date  = fn_crypt('1', " + StringManager.makeSQL(v_birth_date) + ", 'knise')";
                sql2 += "   and name   = " + StringManager.makeSQL(v_name);
                ls2 = connMgr.executeQuery(sql2);
                if ( ls2.next() ) {
                    result = ls2.getString("pwd");
                }
            }

        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
   }

    /**
     * 로그인 연동(세션 세팅)
     * @param box       receive from the form object and session
     * @return is_Ok    1 : login ok      2 : login fail
     * @throws Exception
     */
   public int ssologin(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        MemberData data = null;
        int is_Ok = 0;
        int is_Ok2 = 0;
        String v_userid  = box.getString("p_userid");
        String v_company = box.getString("p_company");
        String v_userip  = box.getString("p_userip");
        v_company = v_company.toLowerCase();
        // System.out.println("v_company ==  == >>  >>  >>  >> " +v_company);

        try {
            connMgr = new DBConnectionManager();
           // userid,birth_date,name,email, comp, jikup, cono
            sql1  = " select userid, fn_crypt('2', birth_date, 'knise') birth_date, name, email,fn_crypt('2', pwd, 'knise') pwd, office_gbn, comp, jikup, cono, comptel,                               ";
            sql1 += "        jikwi, get_jikwinm(jikwi,comp) jikwinm, get_deptnm(deptnam,'') deptnm, get_compnm(comp,2,2) companynm, ";
            sql1 += "        nvl((select compgubun from tz_compclass where comp=tz_member.comp and rownum=1),'') compgubun         ";
            sql1 += " from TZ_MEMBER                     ";
            sql1 += " where userid = " + StringManager.makeSQL(v_userid);
            // sql1 += " where userid = " + StringManager.makeSQL(v_company +v_userid);
            ls = connMgr.executeQuery(sql1);

            if ( ls.next() ) {
                data = new MemberData();
                data.setUserid( ls.getString("userid") );
                data.setbirth_date( ls.getString("birth_date") );
                data.setName( ls.getString("name") );
                data.setEmail( ls.getString("email") );
                data.setComp( ls.getString("comp") );
                data.setComptel( ls.getString("comptel") );
                data.setJikup( ls.getString("jikup") );
                data.setJikwi( ls.getString("jikwi") );
                data.setCono( ls.getString("cono") );
                data.setCompanynm( ls.getString("companynm") );
                data.setCompgubun( ls.getString("compgubun") );
                data.setOffice_gbn( ls.getString("office_gbn") );
                data.setJikwinm( ls.getString("jikwinm") );
                data.setDeptnm( ls.getString("deptnm") );

                is_Ok = 1;
            }
            ls.close();

           if ( is_Ok != 0 ) {
                box.setSession("userid", data.getUserid() );
                box.setSession("birth_date", data.getbirth_date() );
                box.setSession("name", data.getName() );
                box.setSession("email", data.getEmail() );
                box.setSession("comp", data.getComp() );
                box.setSession("comptel", data.getComptel() );
                box.setSession("jikup", data.getJikup() );
                box.setSession("jikwi", data.getJikwi() );
                box.setSession("cono", data.getCono() );
                box.setSession("usergubun", data.getUsergubun() );
                box.setSession("companynm", data.getCompanynm() );
                box.setSession("compgubun", data.getCompgubun() );
                box.setSession("complogo", OutUserAdminBean.getCompLogo(box));
                box.setSession("jikwinm", data.getJikwinm() );
                box.setSession("deptnm", data.getDeptnm() );

                box.setSession("userip", v_userip);

                    box.setSession("gadmin","ZZ");
/*
               // 권한 체크
                sql3  = " select gadmin         ";
                sql3 += "   from TZ_MANAGER     ";
                sql3 += "  where upper(userid) = upper(" + StringManager.makeSQL(v_userid) + " )";
                sql3 += "    and isdeleted = 'N' ";
                sql3 += "    and to_char(sysdate,'yyyymmdd') between fmon and tmon ";
                sql3 += "  order by gadmin asc   ";
                ls = connMgr.executeQuery(sql3);
                if ( ls.next() ) {
                    box.setSession("gadmin",ls.getString("gadmin") );
                } else {
                    box.setSession("gadmin","ZZ");
                }
*/
                // LOGIN DATA UPDATE
                is_Ok2 = updateLoginData(connMgr, v_userid, v_userip, data.getName(), data.getComp());

           }


        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }



    /**
     * 외부강의실 로그인 연동(세션 세팅)
     * @param box       receive from the form object and session
     * @return is_Ok    1 : login ok      2 : login fail
     * @throws Exception
     */
   public int ssologin2(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        MemberData data = null;
        int is_Ok = 0;
        int is_Ok2 = 0;
        String birth_date = box.getSession("birth_date");
        String comp = box.getSession("comp");

        try {
            connMgr = new DBConnectionManager();
           // userid,birth_date,name,email, comp, jikup, cono
            sql1  = " select userid, fn_crypt('2', birth_date, 'knise') birth_date, name, email, fn_crypt('2', pwd, 'knise') pwd, office_gbn, comp, jikup, cono, comptel,                               ";
            sql1 += "        jikwi, get_jikwinm(jikwi,comp) jikwinm, get_deptnm(deptnam,'') deptnm, get_compnm(comp,2,2) companynm, ";
            sql1 += "        nvl((select compgubun from tz_compclass where comp=tz_member.comp and rownum=1),'') compgubun         ";
            sql1 += " from TZ_MEMBER                     ";
            sql1 += " where birth_date = fn_crypt('1', " + StringManager.makeSQL(birth_date) + ", 'knise')";
            sql1 += "   and comp  = " + StringManager.makeSQL(comp);
            ls = connMgr.executeQuery(sql1);

            if ( ls.next() ) {
                data = new MemberData();
                data.setUserid( ls.getString("userid") );
                data.setbirth_date( ls.getString("birth_date") );
                data.setName( ls.getString("name") );
                data.setEmail( ls.getString("email") );
                data.setComp( ls.getString("comp") );
                data.setComptel( ls.getString("comptel") );
                data.setJikup( ls.getString("jikup") );
                data.setJikwi( ls.getString("jikwi") );
                data.setCono( ls.getString("cono") );
                data.setCompanynm( ls.getString("companynm") );
                data.setCompgubun( ls.getString("compgubun") );
                data.setOffice_gbn( ls.getString("office_gbn") );
                data.setJikwinm( ls.getString("jikwinm") );
                data.setDeptnm( ls.getString("deptnm") );

                is_Ok = 1;
            }
            ls.close();

           if ( is_Ok != 0 ) {
                box.setSession("userid", data.getUserid() );
                box.setSession("birth_date", data.getbirth_date() );
                box.setSession("name", data.getName() );
                box.setSession("email", data.getEmail() );
                box.setSession("comp", data.getComp() );
                box.setSession("comptel", data.getComptel() );
                box.setSession("jikup", data.getJikup() );
                box.setSession("jikwi", data.getJikwi() );
                box.setSession("cono", data.getCono() );
                box.setSession("usergubun", data.getUsergubun() );
                box.setSession("companynm", data.getCompanynm() );
                box.setSession("compgubun", data.getCompgubun() );
                box.setSession("complogo", OutUserAdminBean.getCompLogo(box));
                box.setSession("jikwinm", data.getJikwinm() );
                box.setSession("deptnm", data.getDeptnm() );

                // box.setSession("userip", v_userip);

                box.setSession("gadmin","ZZ");

           }
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }

    /**
     * Login 정보 변경 (lgcnt:로그인횟수, lglast:최종로그인시간, lgip:로그인ip
     * @param box       receive from the form object and session
     * @return is_Ok    1 : success      2 : fail
     */
    public int updateLoginData(String p_userid, String p_userip, String p_usernm, String p_comp) throws Exception {
        DBConnectionManager connMgr = null;
        int is_Ok = 0;
        String v_userid = p_userid;
        String v_userip = p_userip;

        try {
              connMgr = new DBConnectionManager();

              is_Ok = updateLoginData(connMgr, v_userid, v_userip, p_usernm, p_comp);
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally {
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }


    /**
     * Login 정보 변경 (lgcnt:로그인횟수, lglast:최종로그인시간, lgip:로그인ip, lgfirst : 최초로그인
     * @param box       receive from the form object and session
     * @return is_Ok    1 : success      2 : fail
     */
    public int updateLoginData(DBConnectionManager connMgr, String p_userid, String p_userip, String p_usernm, String p_comp) throws Exception {
        ListSet             ls      = null;
        ListSet ls1 = null;
        String              sql     = "";
        int is_Ok = 0, is_Ok2 = 1;
        String v_userid = p_userid;
        String v_userip = p_userip;
        String v_usernm = p_usernm;
        String v_comp = p_comp;
        int cnt = 0;

        ConfigSet           config          = new ConfigSet();
        
        try {
              // 최초 로그인 체크
              sql  = " select count(*) cnt from tz_member   ";
              sql += " where userid = lower(" + StringManager.makeSQL(v_userid) + ")";
              sql += "   and lgfirst is null                ";
              ls = connMgr.executeQuery(sql);

              if ( ls.next() ) {
                  cnt = ls.getInt("cnt");
              }
              
              // 정보 업데이트
              sql  = " update TZ_MEMBER                       ";
              sql += " set lgcnt=lgcnt +1, lglast= to_char(sysdate, 'YYYYMMDDHH24MISS') \n";
              
              if ( cnt > 0 ) {
                  sql += " , lgfirst= to_char(sysdate, 'YYYYMMDDHH24MISS') ";
              }
              sql += " where userid = " + StringManager.makeSQL(v_userid) + "";
              is_Ok = connMgr.executeUpdate(sql);

/*
              sql = "select count(*) cnt from tz_loginid where userid = " + StringManager.makeSQL(v_userid) + " ";
              ls1 = connMgr.executeQuery(sql);
              if ( ls1.next() ) {
                cnt = ls1.getInt("cnt");
                if ( cnt > 0) {
                  is_Ok2 = connMgr.executeUpdate("delete from tz_loginid where userid = " + StringManager.makeSQL(v_userid) + " " );
                }
              }
*/
              
              if ( is_Ok2 > 0 ) {   // 로그인 정보를 DB에 남긴다
                sql  = " insert into TZ_LOGINID ";
                sql += " (userid,lgip,ldate) ";
                sql += " values ";
                sql += "(" +StringManager.makeSQL(v_userid) + "," +StringManager.makeSQL(v_userip) + ",to_char(sysdate,'YYYYMMDDHH24MISS')) ";

                is_Ok2 = connMgr.executeUpdate(sql);
              }
              
              if ( is_Ok2 > 0 ) {   // 로그인 대상자 기록을 DB에 남긴다.
                  sql  = " insert into TZ_LOGIN_LOG ";
                  sql += " (SEQ, USERID, USERNM, COMP, LGIP, LDATE) ";
                  sql += " values ";
                  sql += "(LOGIN_LOG_SEQ.NEXTVAL, " +StringManager.makeSQL(v_userid) + ", "+StringManager.makeSQL(v_usernm) + "," +StringManager.makeSQL(v_comp) + "," +StringManager.makeSQL(v_userip) + ",to_char(sysdate,'YYYYMMDDHH24MISS')) ";

                  is_Ok2 = connMgr.executeUpdate(sql);
              }

        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { }}
        }

        return is_Ok;
    }

    /**
     * 로그아웃
     * @param box       receive from the form object and session
     * @return is_Ok    1 : success      2 : fail
     */
    public int logOut(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        int is_Ok = 0;
        try {
              connMgr = new DBConnectionManager();
              is_Ok = logOut(connMgr, box);
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally {
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }


    /**
    * 비밀번호 오류 회수 증가
    * @param box       receive from the form object and session
    * @return is_Ok    1 : success      2 : fail
    */
    public int logOut(DBConnectionManager connMgr, RequestBox box) throws Exception {
        ListSet             ls      = null;
        String              sql     = "";
        int is_Ok = 0;
        String v_userid = box.getSession("userid");

        try {
            sql  = " delete from TZ_loginid                       ";
            sql += " where userid = " + StringManager.makeSQL(v_userid);
            is_Ok = connMgr.executeUpdate(sql);
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
        }

        return is_Ok;
    }



    /**
     * 비밀번호 오류 회수 증가
     * @param box       receive from the form object and session
     * @return is_Ok    1 : success      2 : fail
     */
    public int updateLoginFail(String p_userid, String p_issuccess) throws Exception {
        DBConnectionManager connMgr = null;
        int is_Ok = 0;
        String v_userid = p_userid;
        String v_issuccess = p_issuccess;

        try {
              connMgr = new DBConnectionManager();
              is_Ok = updateLoginFail(connMgr, v_userid, v_issuccess);
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally {
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }


    /**
     * 비밀번호 오류 회수 증가
     * @param box       receive from the form object and session
     * @return is_Ok    1 : success      2 : fail
     */
    public int updateLoginFail(DBConnectionManager connMgr, String p_userid, String p_issuccess) throws Exception {
        ListSet             ls      = null;
        String              sql     = "";
        int is_Ok = 0;
        String v_userid = p_userid;
        String v_issuccess = p_issuccess;

        try {

              sql  = " update TZ_MEMBER                       ";
              if ( v_issuccess.equals("N") ) {
                sql += " set lgfail=lgfail +1 ";
              } else {
                sql += " set lgfail=0 ";
              }
              sql += " where userid = lower(" + StringManager.makeSQL(v_userid) + ")";
            is_Ok = connMgr.executeUpdate(sql);
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
        }

        return is_Ok;
    }

   /**
     * 정보동의 체크
     * @param box       receive from the form object and session
     * @return is_Ok    0 : 정보동의필요      1 : 정보동의함
     * @throws Exception
     */
    public int firstCheck(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        int is_Ok = 0;
        String v_userid = box.getSession("userid");
//        String v_pwd    = box.getString("p_pwd");

        try {
              connMgr = new DBConnectionManager();

              sql  = " select nvl(validation,'0') validation  ";
              sql += " from TZ_MEMBER                       ";
              sql += " where userid = " + StringManager.makeSQL(v_userid);
//              sql += "   and pwd    = " + StringManager.makeSQL(v_pwd);
              ls = connMgr.executeQuery(sql);

              if ( ls.next() ) {
                is_Ok = StringManager.toInt( ls.getString("validation") );
              } else {
                is_Ok = 1;
              }
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }

    /**
     * 이메일 불러오기
     * @param box       receive from the form object and session
     * @return return
     * @throws Exception
     */
    public String emailOpen(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        String v_email = "" ;
        String v_userid = box.getString("p_userid");
        String v_pwd    = box.getString("p_pwd");


        try {
              connMgr = new DBConnectionManager();

              sql  = " select email  ";
              sql += " from TZ_MEMBER                       ";
              sql += " where userid = lower(" + StringManager.makeSQL(v_userid) + ")";

              ls = connMgr.executeQuery(sql);

              if ( ls.next() ) {
                v_email = ls.getString("email");
              }
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_email;
    }

    /**
     * 정보보호 확인
     * @param box       receive from the form object and session
     * @return is_Ok    1 : login ok      2 : login fail
     * @throws Exception
     */
    public int firstLogin(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int is_Ok = 0;

        String v_userid    = box.getSession("userid");

        try {
             connMgr = new DBConnectionManager();

            sql  = " update TZ_MEMBER set validation = ? ";
            sql += "  where userid = ?                   ";
            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, "1");
            pstmt.setString(2, v_userid);
            is_Ok = pstmt.executeUpdate();
         }
         catch ( Exception ex ) {
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }
         finally {
             if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

        return is_Ok;
    }


    /**
     * 비밀번호 분실 폼메일 발송
     * @param box       receive from the form object and session
     * @return is_Ok    1 : send ok      2 : send fail
     * @throws Exception
     */
    public int sendFormMail(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        Connection conn = null;
        ListSet             ls      = null;
        String              sql     = "";
        int is_Ok  = 0;    //  메일발송 성공 여부

        String v_userid = box.getString("p_userid");
        String v_birth_date  = box.getString("p_birth_date1") + box.getString("p_birth_date2");

        try {
            connMgr = new DBConnectionManager();

            sql  = " select name, email, fn_crypt('2', pwd, 'knise') pwd, cono,ismailing,office_gbn, ";
            sql += "            decode(usergubun,'H', (select codenm from tz_code where gubun = '0038' and code = 'HMAIL') ,";
            sql += "                                           'K', (select codenm from tz_code where gubun = '0038' and code = 'KMAIL') , ";
            sql += "                                           'RH', (select codenm from tz_code where gubun = '0038' and code = 'HMAIL') , ";
            sql += "                                           'RK', (select codenm from tz_code where gubun = '0038' and code = 'KMAIL') , ";
            sql += "                                                 (select codenm from tz_code where gubun = '0038' and code = 'HMAIL') ) fromemail,";
            sql += "             decode(usergubun,'H', (select codenm from tz_code where gubun = '0038' and code = 'HNAME') ,";
            sql += "                                          'K', (select codenm from tz_code where gubun = '0038' and code = 'KNAME') , ";
            sql += "                                          'RH', (select codenm from tz_code where gubun = '0038' and code = 'HNAME') , ";
            sql += "                                          'RK', (select codenm from tz_code where gubun = '0038' and code = 'KNAME') , ";
            sql += "                                               (select codenm from tz_code where gubun = '0038' and code = 'HNAME') ) fromname ";
            sql += " from TZ_MEMBER                          ";
            sql += " where userid = " + StringManager.makeSQL(v_userid);
            sql += "   and birth_date  = fn_crypt('1'," + StringManager.makeSQL(v_birth_date) + " , 'knise')";
            ls = connMgr.executeQuery(sql);


            if ( ls.next() ) {

//// //// //// //// ////  폼메일 발송 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //
            String v_sendhtml = "LossPwd.html";

                // 보내는 사람 세팅
                box.put("p_fromEmail",ls.getString("fromemail") );
                box.put("p_fromName",ls.getString("fromname") );
                box.put("p_comptel","");


            FormMail fmail = new FormMail(v_sendhtml);     //      폼메일발송인 경우

            // p_fromEmail, p_fromName, p_fromTel

            MailSet mset = new MailSet(box);               //      메일 세팅 및 발송

            String v_mailTitle = "현대기아학습센터 비밀번호 안내 메일입니다.";
//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /



//                String v_isMailing =  ls.getString("ismailing");
//                if ( v_isMailing.equals("3") || v_isMailing.equals("")) v_isMailing = "1";
                String v_isMailing =  "1";              // email로만 전송

                String v_name      =  ls.getString("name");
                String v_pwd       =  ls.getString("pwd");
                String v_toEmail   =  ls.getString("email");
                String v_toCono    =  ls.getString("cono");
                String v_office_gbn=  ls.getString("office_gbn");

                box.put("v_name",v_name);
                box.put("v_pwd",v_pwd);
                box.put("v_toCono",v_toCono);
                box.put("v_toEmail",v_toEmail);


                if ( v_toEmail.equals("") ) {
                    is_Ok = -2; // 메일주소 없음
                } else  if ( !v_office_gbn.equals("Y") ) {
                    is_Ok = -3; // 재직자가 아님
                } else {

                    mset.setSender(fmail);     //  메일보내는 사람 세팅

                    fmail.setVariable("userid", v_userid);
                    fmail.setVariable("name", ls.getString("name") );
                    fmail.setVariable("pwd", ls.getString("pwd") );

                    String v_mailContent = fmail.getNewMailContent();

                    boolean isMailed = mset.sendMail(v_toCono, v_toEmail, v_mailTitle, v_mailContent, v_isMailing, v_sendhtml);

                    if ( isMailed) is_Ok = 1;     //      메일발송에 성공하면
                }
            } else {
                is_Ok = -1; // 사용자가 없음
            }
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }



    /**
     * 권한 셀렉트 박스
     * @param userid      유저아이디
     * @param name        셀렉트박스명
     * @param selected    선택값
     * @param event       이벤트명
     * @return
     * @throws Exception
     */
     public static String getAuthSelect(String userid, String name, String selected, String event) throws Exception {
         DBConnectionManager    connMgr = null;
         ListSet             ls      = null;
         String result = null;
         String              sql     = "";
         int cnt = 0;

         result = "<SELECT id='" + name + "' name='" + name + "' " + event + "  style='border-style:solid; border-width:1px 1px 1px 1px; border-color:cccccc; color:333333; font-size:8pt; background-color:#ddedfa; width:80px; height:19px;' >\n";

         try {
             connMgr = new DBConnectionManager();

             sql  = " select b.gadmin gadmin, b.gadminnm gadminnm    ";
             sql += "   from tz_manager a, tz_gadmin b               ";
             sql += "  where a.gadmin    = b.gadmin                  ";
             sql += "    and a.userid    = " + StringManager.makeSQL(userid);
             sql += "    and a.isdeleted = 'N'                       ";
             sql += "    and to_char(sysdate,'yyyymmdd') between a.fmon and a.tmon ";
             sql += " order by b.gadmin asc";

             ls = connMgr.executeQuery(sql);

             while ( ls.next() ) {

                 result += "<option value=" + ls.getString("gadmin");
                 if ( selected.equals( ls.getString("gadmin"))) {
                     result += " selected ";
                 }
                 result += " > " + ls.getString("gadminnm") + "</option >\n";
                 cnt++;
             }
         }
         catch ( Exception ex ) {
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }
         finally {
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         result += "  <option value='ZZ'";

        if ( selected.equals("ZZ") || selected.equals("") ) {
           result += " selected ";
        }

         result += " > 학습자</option > ";
         result += "  </SELECT > \n";

         if ( cnt == 0) result = "";
         return result;
     }

     /**
      * 수강중인 과목 셀렉트 박스
      * @param userid      유저아이디
      * @param name        셀렉트박스명
      * @param selected    선택값
      * @param event       이벤트명
      * @return
      * @throws Exception
      */
      public static String getSubjIngSelect(String userid, String name, String selected, String event) throws Exception {
          DBConnectionManager   connMgr = null;
          ListSet   ls      = null;
          String    result  = null;
          String    sql     = "";
          int       cnt     = 0;
          try {
              connMgr = new DBConnectionManager();

              /*
              sql = "select                                                                 \n" +
                    "       b.subj,                                                         \n" +
                    "       b.year,                                                         \n" +
                    "       b.subjseq,                                                      \n" +
                    "       userid,                                                         \n" +
                    "       b.subjnm                                                        \n" +
                    "  from                                                                 \n" +
                    "       tz_student a,                                                   \n" +
                    "       tz_subjseq b                                                    \n" +
                    " where                                                                 \n" +
                    "       userid = " + StringManager.makeSQL(userid) + "                  \n" +
                    "   and a.subj = b.subj                                                 \n" +
                    "   and a.year = b.year                                                 \n" +
                    "   and a.subjseq = b.subjseq                                           \n" +
                    "   and to_char(sysdate,'yyyymmddhh') between b.edustart and b.eduend   \n";
              */

              sql  =
                  "\n  select                                                                                         " +
                  "\n      tsj.contenttype, tst.subj, tst.year, tst.subjseq, tst.isgraduated, tst.branch,             " +
                  "\n      tss.edustart studystart, tss.eduend studyend,                                              " +
                  "\n      tsj.eduurl, tsj.isonoff, tsj.subjnm, tsj.subjclass,                                        " +
                  "\n      tsj.upperclass, tsj.middleclass, tsj.lowerclass,                                           " +
                  "\n      tsa.classname cname                                                                        " +
                  "\n  from                                                                                           " +
                  "\n      tz_student tst, tz_subjseq tss, tz_subj tsj, tz_subjatt tsa                                " +
                  "\n  where 1=1                                                                                      " +
                  "\n      and tst.userid     = ':userid'                                                             " +
                  "\n      and tst.subj       = tss.subj                                                              " +
                  "\n      and tst.year       = tss.year                                                              " +
                  "\n      and tst.subjseq    = tss.subjseq                                                           " +
                  "\n      and tst.subj       = tsj.subj                                                              " +
                  "\n      and tsj.subjclass  = tsa.subjclass                                                         " +
                  "\n      and tsj.upperclass     = tsa.upperclass                                                    " +
                  "\n      and tsj.middleclass    = tsa.middleclass                                                   " +
                  "\n      and tsj.lowerclass     = tsa.lowerclass                                                    " +
                  "\n      and tsj.isonoff      = 'ON'                                                                  " +
                  "\n      and tss.edustart   <= to_char(sysdate,'yyyymmddhh')                                          " +
                  "\n      and tss.eduend     >= to_char(sysdate,'yyyymmddhh')                                          " +
                  "\n  order by                                                                                       " +
                  "\n      tsa.classname, tsj.subjnm, tst.edustart desc                                               ";

              sql = sql.replaceAll( ":userid", userid );
              ls = connMgr.executeQuery(sql);

              //result  = "<SELECT name='" + name + "' " + event + "  style='border-style:solid;border-width: 1px 1px 1px 1px;border-color:cccccc;color:333333;font-size:9pt;background-color:none;width : ";
              result  = "<SELECT name='" + name + "' " + event + "  style='BORDER-RIGHT: #cccccc 1px solid; BORDER-TOP: #cccccc 1px solid; FONT-SIZE: 9pt; BORDER-LEFT: #cccccc 1px solid; WIDTH: 125px; COLOR: #333333; BORDER-BOTTOM: #cccccc 1px solid; HEIGHT: 19px' >";
              result += "<option value='' selected >학습중인 과정</option>";

              //studyPopup('<%= v_subj %>', '<%= v_year %>', '<%= v_subjseq %>', '<%= v_subjnm %>', '<%= v_courseCode %>', '<%= v_orgID %>', '<%= v_orgTitle %>', '<%= v_contenttype %>')

              String v_subj =   "";
              String v_year =   "";
              String v_subjseq  =   "";
              String v_subjnm   =   "";
              String v_contenttype  =   "";
              String v_eduurl       =   "";

              String strHidden  =   "";
              int i=0;
              while ( ls.next() ) {
                  v_subj        =   ls.getString("subj");
                  v_year        =   ls.getString("year");
                  v_subjseq     =   ls.getString("subjseq");
                  v_subjnm      =   ls.getString("subjnm");
                  v_contenttype =   ls.getString("contenttype");
                  v_eduurl      =   ls.getString("eduurl");

                  i++;
                  strHidden +="<input type=\"hidden\" name=\"subj"         + i + "\" value=\"" + v_subj + "\">";
                  strHidden +="<input type=\"hidden\" name=\"year"         + i + "\" value=\"" + v_year + "\">";
                  strHidden +="<input type=\"hidden\" name=\"subjseq"      + i + "\" value=\"" + v_subjseq + "\">";
                  strHidden +="<input type=\"hidden\" name=\"subjnm"       + i + "\" value=\"" + v_subjnm + "\">";
                  strHidden +="<input type=\"hidden\" name=\"contenttype"  + i + "\" value=\"" + v_contenttype + "\">";
                  strHidden +="<input type=\"hidden\" name=\"eduurl"       + i + "\" value=\"" + v_eduurl + "\">";

                  result += "<option value=\"" + i + "\" ";
                  if ( selected.equals( v_subjnm ) ) {
                      result += " selected ";
                  }
                  result += " > " + v_subjnm + "</option>";
                  cnt++;
              }
              result += "</SELECT>";
              result += strHidden;
          }
          catch ( Exception ex ) {
              ErrorManager.getErrorStackTrace(ex);
              throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
          }
          finally {
              if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }

          //if ( cnt == 0) result = "";
          return result;
      }

    /**
     * SSO 로그인 (세션 세팅)
     * @param box       receive from the form object and session
     * @return is_Ok    1 : login ok      2 : login fail
     * @throws Exception
     */
   public int loginSSO(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        MemberData data = null;
        int is_Ok = 0;
        int is_Ok2 = 0;
        String v_userid = box.getString("p_userid");
        String v_userip = box.getString("p_userip");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql1  = " select userid, fn_crypt('2', birth_date, 'knise') birth_date, name, email, ";
            sql1 += "        comp, jikup,  cono,comptel, jikwi, replace(get_compnm(comp,2,2),'/','') companynm , ";
            sql1 += "        decode(nvl((select deptmbirth_date from tz_comp where deptmbirth_date=tz_member.userid and rownum=1),''),'','N','Y') isdeptmnger ";
            sql1 += " from TZ_MEMBER                     ";
            sql1 += " where office_gbn = 'Y' and userid = " + StringManager.makeSQL(v_userid);

            ls = connMgr.executeQuery(sql1);

            if ( ls.next() ) {
                data = new MemberData();
                data.setUserid( ls.getString("userid") );
                data.setbirth_date( ls.getString("birth_date") );
                data.setName( ls.getString("name") );
                data.setEmail( ls.getString("email") );
                data.setComp( ls.getString("comp") );
                data.setComptel( ls.getString("comptel") );
                data.setJikup( ls.getString("jikup") );
                data.setJikwi( ls.getString("jikwi") );
                data.setCono( ls.getString("cono") );
                data.setCompanynm( ls.getString("companynm") );
                data.setIsdeptmnger( ls.getString("isdeptmnger") );

                is_Ok = 1;
            }
            ls.close();

            // USERID 에 없을경우 CONO에서 체크

            if ( is_Ok == 0 ) {
                sql2  = " select userid, fn_crypt('2', birth_date, 'knise') birth_date, name, email, ";
                sql2 += "        comp, jikup,  cono, comptel, jikwi, replace(get_compnm(comp,2,2),'/',''),     ";
                sql2 += "        decode(nvl((select deptmbirth_date from tz_comp where deptmbirth_date=tz_member.userid and rownum=1),''),'','N','Y') isdeptmnger ";
                sql2 += " from TZ_MEMBER                     ";
                sql2 += " where office_gbn = 'Y' and lower(cono)   = lower(" + StringManager.makeSQL(v_userid) + " )";

                ls = connMgr.executeQuery(sql2);

                if ( ls.next() ) {
                    data = new MemberData();
                    data.setUserid( ls.getString("userid") );
                    data.setbirth_date( ls.getString("birth_date") );
                    data.setName( ls.getString("name") );
                    data.setEmail( ls.getString("email") );
                    data.setComp( ls.getString("comp") );
                    data.setComptel( ls.getString("comptel") );
                    data.setJikup( ls.getString("jikup") );
                    data.setJikwi( ls.getString("jikwi") );
                    data.setCono( ls.getString("cono") );
                    data.setCompanynm( ls.getString("companynm") );
                    data.setIsdeptmnger( ls.getString("isdeptmnger") );

                    is_Ok = 1;

                } else {
                    is_Ok = -1; // 정보 없음
                }
                ls.close();

            }

           if ( is_Ok == 1) {
                box.setSession("userid", data.getUserid() );
                box.setSession("birth_date", data.getbirth_date() );
                box.setSession("name", data.getName() );
                box.setSession("email", data.getEmail() );
                box.setSession("comp", data.getComp() );
                box.setSession("comptel", data.getComptel() );
                box.setSession("jikup", data.getJikup() );
                box.setSession("jikwi", data.getJikwi() );
                box.setSession("cono", data.getCono() );
                box.setSession("usergubun", data.getUsergubun() );
                box.setSession("companynm", data.getCompanynm() );
                box.setSession("isdeptmnger", data.getIsdeptmnger() );


               box.setSession("gadmin","ZZ");

               // box.getHttpSession().setAttribute("binding.SessionListener", new SessionListener(box));

               is_Ok2 = updateLoginData(v_userid, v_userip,data.getName() , data.getComp());

               this.loginMileage(connMgr,v_userid, "ELN_LOGIN");
               connMgr.commit();

           }
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }

public boolean loginMileage(DBConnectionManager connMgr, String v_userid, String v_mileageType) throws Exception {
        ListSet             ls      = null;
        String              sql     = "";

        int v_point = 0;
        String v_date = FormatDate.getDate("yyyyMM");

        boolean success = false;
        try {
            // 마일리지 점수 정보
            if ( v_mileageType.equals("ELN_LOGIN") ) {
                sql = "select point from site_pointitem where code = 1 ";
            } else if ( v_mileageType.equals("ELN_REG_REPLY") ) {
                sql = "select point from tz_pointitem where code = 2 ";
            } else {
                sql = "select 0 from dual;";
            }

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) {
                v_point  = ls.getInt(1);
            }
            ls.close();

    //       MileageManager mileManager = MileageManager.getInstance();



            // 마일리지 점수 정보
            if ( v_mileageType.equals("ELN_LOGIN") ) {
            //  success = mileManager.updateMileage(connMgr.getConnection(), v_userid, v_date , IMileageItemCode.ELN_LOGIN,v_point);
            } else if ( v_mileageType.equals("ELN_REG_REPLY") ) {
            //  success = mileManager.updateMileage(connMgr.getConnection(), v_userid, v_date , IMileageItemCode.ELN_REG_REPLY,v_point);
            }

        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}

        }

        return success;
    }


    /**
    * 유저메일전송 (TZ_USERMAIL 에 등록)
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int insertUserMail(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ResultSet rs1   = null;
        Statement stmt1 = null;
        PreparedStatement pstmt1  = null;
        String sql    = "";
        String sql1   = "";
        String sql2   = "";
        int isOk1     = 1;

        int v_seq = 0;
        String v_fuserid       = box.getString("p_fuserid");
        String v_fusername     = box.getString("p_fusername");
        String v_fuseremail    = box.getString("p_fuseremail");
        String v_tuserid       = box.getString("p_tuserid");
        String v_tusername     = box.getString("p_tusername");
        String v_tuseremail    = box.getString("p_tuseremail");
        String v_title         = box.getString("p_title");
        String v_contents      = box.getString("p_contents");

        String s_userid  = box.getSession("userid");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            stmt1 = connMgr.createStatement();

            // ----------------------  최대값 구한다. ----------------------------
            sql = "select nvl(max(seq),0) from TZ_USERMAIL ";
            rs1 = stmt1.executeQuery(sql);
            if ( rs1.next() ) {
                v_seq = rs1.getInt(1) +1;
            }
            rs1.close();

            // -------------------------------------------------------------------------
            sql1  = " insert into TZ_USERMAIL(seq, title, fuserid, fusername, fuseremail, tuserid,  ";
            sql1 += "                         tusername, tuseremail,contents, retmailing, ldate)     ";
            sql1 += "                values (?, ?, ?, ?, ?, ?, ?, ?, ?,'N', to_char(sysdate, 'YYYYMMDDHH24MISS'))  ";
//            sql1 += "                values (?, ?, ?, ?, ?, ?, ?, ?, empty_clob(),'N', to_char(sysdate, 'YYYYMMDDHH24MISS'))  ";

            pstmt1 = connMgr.prepareStatement(sql1);

            pstmt1.setInt   ( 1, v_seq);
            pstmt1.setString( 2, v_title);
            pstmt1.setString( 3, v_fuserid);
            pstmt1.setString( 4, v_fusername);
            pstmt1.setString( 5, v_fuseremail);
            pstmt1.setString( 6, v_tuserid);
            pstmt1.setString( 7, v_tusername);
            pstmt1.setString( 8, v_tuseremail);
            pstmt1.setString( 9, v_contents);
            isOk1 = pstmt1.executeUpdate();

            sql2 = "select contents from TZ_USERMAIL where seq = " + v_seq;
//            connMgr.setOracleCLOB(sql2, v_contents);       //      (기타 서버 경우)

            if ( isOk1 > 0) connMgr.commit();
            else          connMgr.rollback();
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally {
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1;
    }

    // 북마크 했을때 처리 방안
    public static String getCompanyUrl(RequestBox box) throws Exception {
         DBConnectionManager    connMgr = null;
         String v_hostname = box.getString("p_hostname");
         ListSet             ls      = null;
         String              sql     = "";
         String v_grcode = "";
         int cnt = 0;

         try {
             connMgr = new DBConnectionManager();

                         sql = "select grcode from tz_grcode where domain like  " + StringManager.makeSQL("%" + v_hostname + "%") + " ";

             ls = connMgr.executeQuery(sql);

             if ( ls.next() ) {
                             v_grcode = ls.getString("grcode");
             }
             ls.close();
         }
         catch ( Exception ex ) {
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }
         finally {
             if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return v_grcode;
     }

     /**
     * SSO 로그인 (세션 세팅)
     * @param box       receive from the form object and session
     * @return is_Ok    1 : login ok      2 : login fail
     * @throws Exception
     */
   public int loginSSO_AES(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        MemberData data = null;
        int is_Ok = 0;
        int is_Ok2 = 0;
        String v_userid = box.getString("p_userid");
        String v_userip = box.getString("p_userip");
        String v_tem_grcode = box.getSession("tem_grcode");
        v_userid = v_userid.toLowerCase();

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql1  = " select a.userid, fn_crypt('2', a.birth_date, 'knise') birth_date, a.name, a.email, fn_crypt('2', a.pwd, 'knise') pwd, a.office_gbn, ";
            sql1 += "        a.comp, a.jikup, a.cono, a.comptel,a.jikwi, get_jikwinm(a.jikwi,a.comp) jikwinm, get_deptnm(deptnam,'') deptnm, ";
            sql1 += "        replace(get_compnm(a.comp,2,2),'/','') companynm , ";
            sql1 += "        nvl((select compgubun from tz_compclass where a.comp=tz_compclass.comp and rownum=1),'') compgubun ";
            sql1 += " from TZ_MEMBER a                    ";
            sql1 += " where a.userid = " + StringManager.makeSQL(v_userid);
            sql1 += "   and  \n";
            sql1 += "      comp in (select comp from tz_grcomp where grcode = " + StringManager.makeSQL(v_tem_grcode) + ") ";

            ls = connMgr.executeQuery(sql1);

            if ( ls.next() ) {
                data = new MemberData();
                data.setUserid( ls.getString("userid") );
                data.setbirth_date( ls.getString("birth_date") );
                data.setName( ls.getString("name") );
                data.setEmail( ls.getString("email") );
                data.setComp( ls.getString("comp") );
                data.setComptel( ls.getString("comptel") );
                data.setJikup( ls.getString("jikup") );
                data.setJikwi( ls.getString("jikwi") );
                data.setCono( ls.getString("cono") );
                data.setCompanynm( ls.getString("companynm") );
                data.setCompgubun( ls.getString("compgubun") );
                data.setOffice_gbn( ls.getString("office_gbn") );
                data.setJikwinm( ls.getString("jikwinm") );
                data.setDeptnm( ls.getString("deptnm") );

                is_Ok = 1;
            }
            // USERID 에 없을경우 CONO에서 체크

           if ( is_Ok == 1) {
                data = new MemberData();
                data.setUserid( ls.getString("userid") );
                data.setbirth_date( ls.getString("birth_date") );
                data.setName( ls.getString("name") );
                data.setEmail( ls.getString("email") );
                data.setComp( ls.getString("comp") );
                data.setComptel( ls.getString("comptel") );
                data.setJikup( ls.getString("jikup") );
                data.setJikwi( ls.getString("jikwi") );
                data.setCono( ls.getString("cono") );
                data.setCompanynm( ls.getString("companynm") );
                data.setCompgubun( ls.getString("compgubun") );
                data.setOffice_gbn( ls.getString("office_gbn") );
                data.setJikwinm( ls.getString("jikwinm") );
                data.setDeptnm( ls.getString("deptnm") );

              if ( ls.getString("office_gbn").equals("Y") ) {
                  is_Ok = 1; // 성공
              } else {
                  is_Ok = -2; // 퇴직자
              }

            } else {
              is_Ok = -1; // 사용자정보 없음
            }

            if ( is_Ok == 1) {
                box.setSession("userid", data.getUserid() );
                box.setSession("birth_date", data.getbirth_date() );
                box.setSession("name", data.getName() );
                box.setSession("email", data.getEmail() );
                box.setSession("comp", data.getComp() );
                box.setSession("comptel", data.getComptel() );
                box.setSession("jikup", data.getJikup() );
                box.setSession("jikwi", data.getJikwi() );
                box.setSession("cono", data.getCono() );
                box.setSession("usergubun", data.getUsergubun() );
                box.setSession("companynm", data.getCompanynm() );
                box.setSession("compgubun", data.getCompgubun() );
                box.setSession("complogo", OutUserAdminBean.getCompLogo(connMgr, box));
                box.setSession("jikwinm", data.getJikwinm() );
                box.setSession("deptnm", data.getDeptnm() );

                box.setSession("userip", v_userip);

               box.setSession("gadmin","ZZ");

                // 접속통계 누적 부분
                CountBean bean1 = new CountBean();
                bean1.writeLog(connMgr, box);    // 로그 작성

                // LOGIN DATA UPDATE
                is_Ok2 = updateLoginData(connMgr, v_userid, v_userip,data.getName() , data.getComp());

                // this.loginMileage(connMgr,v_userid, "ELN_LOGIN");
                connMgr.commit();

           }
        }

        catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return is_Ok;
    }

   public DataBox findId(RequestBox box) throws Exception {
       DBConnectionManager connMgr = null;
       PreparedStatement pstmt = null;
       ListSet ls = null;
       String sql = "";
       DataBox dbox = null;

       String v_name = box.getString("p_name");
       String v_birth_date = box.getString("p_birth_date");
       String v_email = box.getString("p_email");
       String v_member_gubun = box.getString("p_member_gubun");
       try {
           connMgr = new DBConnectionManager();

           sql = "select a.userid, a.name, a.type, b.codenm pwdhintitem, a.pwdhintanswer        \n"+
                 "from tz_member a, tz_code b                                                   \n"+
                 "where name = ?                                                                \n";
            if("B".equals(v_member_gubun)) {
            sql+="and email = ?                                                                 \n"+
                 "and type = '3'                                                                \n";
            } else {
            sql+="and birth_date = fn_crypt('1', ?, 'knise')                                                             \n"+
                 "and type in ('1', '2')                                                        \n";
            }
            sql+="and b.gubun = '0061'                                                          \n"+
                 "and trim(a.pwdhintitem) = trim(b.code)                                        \n"+
                 "and ( a.isretire is null or ( a.isretire is not null and a.isretire != 'Y'))  \n";

           pstmt = connMgr.prepareStatement(sql);
           pstmt.setString(1, v_name);
           if("B".equals(v_member_gubun)) {
               pstmt.setString(2, v_email);
           } else {
               pstmt.setString(2, v_birth_date);
           }
           ls = new ListSet(pstmt);

           while(ls.next()) {
               dbox = ls.getDataBox();
           }
       }
       catch (Exception ex) {
           ErrorManager.getErrorStackTrace(ex, box, sql);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
       }
       finally {
           if(ls != null) {try {ls.close();} catch(Exception e){}}
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
           if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
       }
       return dbox;
   }

   public String insertPwd(RequestBox box) throws Exception {
       DBConnectionManager connMgr  = null;
       PreparedStatement   pstmt    = null;
       PreparedStatement   pstmt1    = null;
       ListSet ls = null;

       String              sql          = "";
       String              sql1          = "";
       String              sql2          = "";
       int                 isOk         = 0;
       int                 isOk1         = 0;
       String v_pwd = "";
       //String v_pwd = box.getString("p_pwd");
       v_pwd = GenerateTempPassword();
       String v_pwd_encode =  StringManager.BASE64Encode(v_pwd);

       String v_userid = box.getString("p_userid");
       String v_answer = box.getString("p_answer");
       String v_email = box.getString("p_email");
       String v_member_gubun = box.getString("p_member_gubun");
       String v_success_yn = "";
       try {
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);

           sql  = "UPDATE TZ_MEMBER SET PWD = fn_crypt('1', ?, 'knise'), member_info_check = ? \n"+
                  "WHERE USERID=?                                   \n";
           if(!"B".equals(v_member_gubun)) {
               sql+="AND PWDHINTANSWER = ?                          \n";
           }
           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1,  v_pwd_encode);
           pstmt.setInt(2, 1);
           pstmt.setString(3,  v_userid);
           if(!"B".equals(v_member_gubun)) {
               pstmt.setString(4, v_answer);
           }

           isOk  = pstmt.executeUpdate();

           if(isOk > 0) {
               box.put("p_pwd", v_pwd);
           } else {
               v_pwd = "0";
           }

           sql1 = "select nvl(max(seq), 0) from tz_pwdsearchlist";
           ls = connMgr.executeQuery(sql1);
           ls.next();
           int v_seq = ls.getInt(1) + 1;
           ls.close();

           sql2 =  "insert into tz_pwdsearchlist(seq, userid, ldate, success_yn, admin_yn )";
           sql2 += " values (?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?)";

           pstmt1 = connMgr.prepareStatement(sql2);

           pstmt1.setInt(1, v_seq);
           pstmt1.setString(2, v_userid);
           if(isOk > 0) {
               v_success_yn = "Y";
           } else {
               v_success_yn = "N";
           }
           pstmt1.setString(3, v_success_yn);
           pstmt1.setString(4, "N");

           isOk1 = pstmt1.executeUpdate();


           if ( isOk1 > 0) {    connMgr.commit();      }
           else            {    connMgr.rollback();  v_pwd="0";  }
       } catch ( Exception ex ) {
           isOk = 0;
           v_pwd = "0";
           connMgr.rollback();
           ErrorManager.getErrorStackTrace(ex, box, sql);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
       } finally {
           if ( pstmt   != null ) { try { pstmt.close();               } catch ( Exception e1 )  { } }
           if ( pstmt1   != null ) { try { pstmt.close();               } catch ( Exception e1 )  { } }
           if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
           if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e10 ) { } }
       }

       return v_pwd;
   }

   // 패스워드 10자리로 임의의 값 생성
   private String GenerateTempPassword()
   {
        String pwChar = "abcdefghijklmnopqrstuvwxyz1234567890";
        char[] password = new char[10];
        Random random = new Random();
        for ( int i=0; i<10; i++ )
        password[i] = pwChar.charAt(random.nextInt(36));

        return new String( password );
   }

   public DataBox memberInfo(RequestBox box) throws Exception {
       DBConnectionManager connMgr = null;
       PreparedStatement pstmt = null;
       ListSet ls = null;
       String sql = "";
       DataBox dbox = null;

       String s_userid = box.getString("p_userid");
       try {
           connMgr = new DBConnectionManager();

           sql = "select a.userid, a.name, a.type, b.codenm pwdhintitem, a.pwdhintanswer, a.email   \n"+
                 "from tz_member a, tz_code b                                                       \n"+
                 "where userid = ?                                                                  \n";

           pstmt = connMgr.prepareStatement(sql);
           pstmt.setString(1, s_userid);

           ls = new ListSet(pstmt);

           while(ls.next()) {
               dbox = ls.getDataBox();
           }
       }
       catch (Exception ex) {
           ErrorManager.getErrorStackTrace(ex, box, sql);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
       }
       finally {
           if(ls != null) {try {ls.close();} catch(Exception e){}}
            if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
           if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
       }
       return dbox;
   }

   /**
    * 비밀번호 이메일 받았는지 체크 /
    * @param box       receive from the form object and session
    * @return is_Ok    1 : 정보동의필요
    * @throws Exception
    */
   public int memberEditCheck(RequestBox box) throws Exception {
       DBConnectionManager  connMgr = null;
       ListSet             ls      = null;
       String              sql     = "";
       int is_Ok = 0;
       String v_userid = box.getSession("userid");
       try {
             connMgr = new DBConnectionManager();

             sql  = " select member_info_check, case when nvl(instr(address1, ' '), 0) > 0 then trim(substr(address1, 1, instr(address1, ' '))) else address1 end gubun \n"
                  + " from tz_member                                                                                                                                    \n"
                  + " where userid = " + StringManager.makeSQL(v_userid)
                  + " and ((case when nvl(instr(address1, ' '), 0) > 0 then trim(substr(address1, 1, instr(address1, ' '))) else address1 end) not in (                 \n"
                  + "     select trim(sido) from tz_zipcode                                                                                                             \n"
                  + "     group by sido                                                                                                                                 \n"
                  + " ) or member_info_check = 1 )                                                                                                                      \n";

             ls = connMgr.executeQuery(sql);

             if ( ls.next() ) {
               is_Ok = StringManager.toInt( ls.getString("member_info_check") );
               if(is_Ok == 1) {
                   is_Ok = 1;
               } else {
                   is_Ok = 2;
               }
             } else {
               is_Ok = 0;
             }
       } catch ( Exception ex ) {
           ErrorManager.getErrorStackTrace(ex, box, sql);
           throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
       } finally {
           if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
           if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
       }

       return is_Ok;
   }
   
   
   /**
    * 수강중인 과목 셀렉트 박스(메인페이지)
    * @param userid      유저아이디
    * @param name        셀렉트박스명
    * @param selected    선택값
    * @param event       이벤트명
    * @return
    * @throws Exception
    */
    public static String getSubjIngSelect1(String userid, String name, String selected, String event, String comp) throws Exception {
        DBConnectionManager   connMgr = null;
        ListSet   ls      = null;
        String    result  = null;
        String    sql     = "";
        int       cnt     = 0;
        
        try {
            connMgr = new DBConnectionManager();

            /*
            sql = "select                                                                 \n" +
                  "       b.subj,                                                         \n" +
                  "       b.year,                                                         \n" +
                  "       b.subjseq,                                                      \n" +
                  "       userid,                                                         \n" +
                  "       b.subjnm                                                        \n" +
                  "  from                                                                 \n" +
                  "       tz_student a,                                                   \n" +
                  "       tz_subjseq b                                                    \n" +
                  " where                                                                 \n" +
                  "       userid = " + StringManager.makeSQL(userid) + "                  \n" +
                  "   and a.subj = b.subj                                                 \n" +
                  "   and a.year = b.year                                                 \n" +
                  "   and a.subjseq = b.subjseq                                           \n" +
                  "   and substring(dbo.to_date(getdate(),'YYYYMMDDHH24MISS'), 1, 10) between b.edustart and b.eduend   \n";
            */
/*
            sql  =
                "\n  select                                                                                         " +
                "\n      tsj.contenttype, tst.subj, tst.year, tst.subjseq, tst.isgraduated, tst.branch,             " +
                "\n      tss.edustart studystart, tss.eduend studyend,                                              " +
                "\n      tsj.eduurl, tsj.isonoff, tsj.subjnm, tsj.subjclass,                                        " +
                "\n      tsj.upperclass, tsj.middleclass, tsj.lowerclass,                                           " +
                "\n      tsa.classname cname                                                                        " +
                "\n  from                                                                                           " +
                "\n      tz_student tst, tz_subjseq tss, tz_subj tsj, tz_subjatt tsa                                " +
                "\n  where 1=1                                                                                      " +
                "\n      and tst.userid     = ':userid'                                                             " +
                "\n      and tst.subj       = tss.subj                                                              " +
                "\n      and tst.year       = tss.year                                                              " +
                "\n      and tst.subjseq    = tss.subjseq                                                           " +
                "\n      and tst.subj       = tsj.subj                                                              " +
                "\n      and tsj.subjclass  = tsa.subjclass                                                         " +
                "\n      and tsj.upperclass     = tsa.upperclass                                                    " +
                "\n      and tsj.middleclass    = tsa.middleclass                                                   " +
                "\n      and tsj.lowerclass     = tsa.lowerclass                                                    " +
                "\n      and tsj.isonoff      = 'ON'                                                                  " +
                "\n      and tss.edustart   <= substring(dbo.to_date(getdate(),'YYYYMMDDHH24MISS'), 1, 10)                                          " +
                "\n      and tss.eduend     >= substring(dbo.to_date(getdate(),'YYYYMMDDHH24MISS'), 1, 10)                                          " +
                "\n  order by                                                                                       " +
                "\n      tsa.classname, tsj.subjnm, tst.edustart desc                                               ";
*/
/*            
            sql = " select a.subj															\n";
            sql += "  , b.year                                                    		\n";
            sql += "  , b.subjseq                                                 		\n";
            sql += " 	, (																	\n";
            sql += " 		select classname												\n";
            sql += " 		from tz_subjatt													\n";
            sql += " 		where upperclass = a.upperclass									\n";
            sql += " 		and middleclass = '000'											\n";
            sql += " 	) upperclassname													\n";
            sql += "  , a.upperclass                                  					\n";
            sql += "  , a.subjnm                                      					\n";
            sql += "  , a.contenttype                                 					\n";
            sql += "  , dbo.get_progress(b.subj, b.year, b.subjseq, b.userid) tstep 		\n";
            sql += "  , a.eduurl															\n";
            sql += " from tz_subj a inner join tz_student b		    					\n";
            sql += " on a.subj = b.subj													\n";
            sql += " inner join tz_subjseq c                                      		\n";
            sql += " on b.subj = c.subj                                           		\n";
            sql += " and b.year = c.year                                          		\n";
            sql += " and b.subjseq = c.subjseq                                    		\n";
            sql += " where b.isgraduated != 'Y'                                   		\n";
            sql += " and b.userid = " + StringManager.makeSQL(userid) + "					\n";
            sql += " and c.grcode = " + StringManager.makeSQL(grcode) + " 				\n";
            sql += " and (b.subj + b.year + b.subjseq + b.userid ) not in (				\n";
            sql += " 		select subj + year + subjseq + userid							\n";
            sql += " 		from tz_stold													\n";
            sql += " )																	\n";
*/                            	

            sql = "\n select a.subj "
                + "\n      , a.year "
                + "\n      , a.subjseq "
                + "\n      , get_subjclassnm(d.upperclass, '000','000') as upperclassname "
                + "\n      , d.upperclass "
                + "\n      , d.subjnm "       
                + "\n      , d.contenttype "
                + "\n      , get_progress(a.subj, a.year, a.subjseq, a.userid) tstep "
                + "\n      , d.eduurl "
                + "\n      , d.cpsubj "
                + "\n      , c.cpsubjseq "
                + "\n      , d.isoutsourcing "
                + "\n      , a.comp "
                + "\n      , c.eduend "
                + "\n      , d.isonoff "
                + "\n      , d.height "
                + "\n      , d.width "
                + "\n from   tz_student a, tz_stold b, tz_subjseq c, tz_subj d "
                + "\n where  a.userid = " + StringManager.makeSQL(userid)
                + "\n and    a.subj = b.subj(+) "
                + "\n and    a.year = b.year(+) "
                + "\n and    a.subjseq = b.subjseq(+) "
                + "\n and    a.userid = b.userid(+) "
                + "\n and    b.userid is null "
                + "\n and    a.subj = c.subj "
                + "\n and    a.year = c.year "
                + "\n and    a.subjseq = c.subjseq "
                + "\n and    c.subj = d.subj "
                + "\n and    d.isonoff != 'OFF' "
                + "\n and    c.edustart <= to_char(sysdate,'yyyymmddhh24') " 
                + "\n and    c.eduend >= to_char(sysdate,'yyyymmddhh24')    " 
                + "\n order by d.subjnm ";
                            
            ls = connMgr.executeQuery(sql);

            //result  = "<SELECT name='" + name + "' " + event + "  style='border-style:solid;border-width: 1px 1px 1px 1px;border-color:cccccc;color:333333;font-size:9pt;background-color:none;width : ";
            result  = "<select id='" + name + "' name='" + name + "' " + event + " style='border-style:solid; border-width: 1px 1px 1px 1px; border-color:cccccc; color:333333; background-color:none; width: 176px; height:19px; font-size:8pt;'>";
            result += "<option value='' selected >:::학습 중인 과정:::</option>";

            //studyPopup('<%= v_subj %>', '<%= v_year %>', '<%= v_subjseq %>', '<%= v_subjnm %>', '<%= v_courseCode %>', '<%= v_orgID %>', '<%= v_orgTitle %>', '<%= v_contenttype %>')

            String v_subj =   "";
            String v_year =   "";
            String v_subjseq  =   "";
            String v_subjnm   =   "";
            String v_contenttype  =   "";
            String v_eduurl       =   "";
            String v_cpsubj = "";
            String v_cpsubjseq = "";
            String v_isoutsourcing = "";
            String v_comp = "";
            String v_endgu = "";
            String v_isonoff = "";
            String v_height = "";
            String v_width = "";

            String strHidden  =   "";
            int i=0;
            while ( ls.next() ) {
                v_subj         =   ls.getString("subj");
                v_year         =   ls.getString("year");
                v_subjseq      =   ls.getString("subjseq");
                v_subjnm       =   ls.getString("subjnm");
                v_contenttype  =   ls.getString("contenttype");
                v_eduurl       =   ls.getString("eduurl");
                v_cpsubj       =   ls.getString("cpsubj");
                v_cpsubjseq    =   ls.getString("cpsubjseq");
                v_isoutsourcing=   ls.getString("isoutsourcing");
                v_comp		   =   ls.getString("comp");
                v_endgu		   =   (FormatDate.datediff("date",FormatDate.getDate("yyyyMMdd"),ls.getString("eduend")) > 0) ? "N":"Y";//교육기간 종료여부
                v_isonoff	   =   ls.getString("isonoff");
                v_height	   =   ls.getString("height");
                v_width	   =   ls.getString("width");
                
                i++;
                strHidden +="<input type=\"hidden\" name=\"subj"         	+ i + "\" value=\"" + v_subj + "\">";
                strHidden +="<input type=\"hidden\" name=\"year"         	+ i + "\" value=\"" + v_year + "\">";
                strHidden +="<input type=\"hidden\" name=\"subjseq"      	+ i + "\" value=\"" + v_subjseq + "\">";
                strHidden +="<input type=\"hidden\" name=\"subjnm"       	+ i + "\" value=\"" + v_subjnm + "\">";
                strHidden +="<input type=\"hidden\" name=\"contenttype"  	+ i + "\" value=\"" + v_contenttype + "\">";
                strHidden +="<input type=\"hidden\" name=\"eduurl"       	+ i + "\" value=\"" + v_eduurl + "\">";
                strHidden +="<input type=\"hidden\" name=\"cpsubj"    	 	+ i + "\" value=\"" + v_cpsubj + "\">";
                strHidden +="<input type=\"hidden\" name=\"cpsubjseq"  		+ i + "\" value=\"" + v_cpsubjseq + "\">";
                strHidden +="<input type=\"hidden\" name=\"isoutsourcing"  	+ i + "\" value=\"" + v_isoutsourcing + "\">";
                strHidden +="<input type=\"hidden\" name=\"comp"		  	+ i + "\" value=\"" + v_comp + "\">";
                strHidden +="<input type=\"hidden\" name=\"endgu"		  	+ i + "\" value=\"" + v_endgu + "\">";
                strHidden +="<input type=\"hidden\" name=\"isonoff"			+ i + "\" value=\"" + v_isonoff + "\">";
                strHidden +="<input type=\"hidden\" name=\"height"			+ i + "\" value=\"" + v_height + "\">";
                strHidden +="<input type=\"hidden\" name=\"width"			+ i + "\" value=\"" + v_width + "\">";

                result += "<option value=\"" + i + "\" ";
                if ( selected.equals( v_subjnm ) ) {
                    result += " selected ";
                }
                result += " > " + v_subjnm + "</option>";
                cnt++;
            }
            result += "</select>";
            result += strHidden;
        }
        catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }
        finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        //if ( cnt == 0) result = "";
        return result;
    }


    /**
     * IP동일하지 않은 경우 입력받은 값 처리
     * @param box       receive from the form object and session
     * @return is_Ok    1 : 저장성공
     * @throws Exception
     */
    public int updateIp(RequestBox box) throws Exception {
        DBConnectionManager  connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        PreparedStatement pstmt1  = null;
        int isOk = 0;
        int isOk1 = 1;

        String s_userip = box.getSession("userip");
    	String s_buserip = box.getSession("buserip");
    	String v_contents = box.getString("p_contents");
    	String s_userid = box.getSession("userid");
    	String v_cause = box.getString("p_cause");
    	String v_birth_date2 = box.getString("p_birth_date2");

        try {
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sql = " select count(*) from tz_member"
            	+ " where userid = " + StringManager.makeSQL(s_userid) + "   \n"
            	+ "  and  substr(fn_crypt('1', birth_date, 'knise'),7,13) = " + StringManager.makeSQL(v_birth_date2) + "   \n";
            ls = connMgr.executeQuery(sql);
            ls.next();
            int icnt = ls.getInt(1);
            ls.close();

            if(icnt == 0) {
            	isOk1 = -1;
            } else {
	            
	            sql = " select nvl(max(seq),0) from tz_ipcheck";
	            ls = connMgr.executeQuery(sql);
	            ls.next();
	            int v_seq = ls.getInt(1) + 1;
	            ls.close();
	            
	            sql  = " insert into TZ_IPCHECK (seq, userid, lgip, before_ip, type, contents, ldate)\n"
	                 + " values ( ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS')) ";
	
	            pstmt1 = connMgr.prepareStatement(sql);
	
	            pstmt1.setInt   ( 1, v_seq);
	            pstmt1.setString( 2, s_userid);
	            pstmt1.setString( 3, s_userip);
	            pstmt1.setString( 4, s_buserip);
	            pstmt1.setString( 5, v_cause);
	            pstmt1.setString( 6, v_contents);
	            isOk = pstmt1.executeUpdate();
	            if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }
	            
	            // 2009. 02 입력받은 정보를 처리 하고 나서 최종IP정보를 업데이트 한다.
	            sql  = " update TZ_MEMBER set lgip= ? ";
	            sql += " where userid = ? ";
	
	            pstmt1 = connMgr.prepareStatement(sql);
	
	            pstmt1.setString( 1, s_userip);
	            pstmt1.setString( 2, s_userid);
	            isOk = pstmt1.executeUpdate();
	            if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            }

            if ( isOk > 0 && isOk1 > 0) {    connMgr.commit();      }
            else            {    connMgr.rollback();  }
        } catch ( Exception ex ) {
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( pstmt1   != null ) { try { pstmt1.close();               } catch ( Exception e1 )  { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection();    } catch ( Exception e10 ) { } }
        }

        return isOk + isOk1;
    }
      

    /** 사용자 아이디 조회
     * @param box       receive from the form object and session
     * @return list    -- 사용자 아이디, 핸드폰번호
     * @throws Exception
     */
    public ArrayList getUserId(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        String sql = "";
        DataBox data    = null;
        ArrayList list    = new ArrayList();

        String v_name = box.getString("p_name");
        String v_birth_date = box.getString("p_birth_date");
     
        try {
            connMgr = new DBConnectionManager();

            sql  = "select decode(trim(handphone), '', 1, 0) as gubn    \n";
            sql += "     , userid, handphone, fn_crypt('2', pwd, 'knise') pwd                            \n"; 
            sql += " from tz_member                                     \n";
            sql += "where name = " + StringManager.makeSQL(v_name) + "  \n";
            sql += "  and birth_date = fn_crypt('1', " + StringManager.makeSQL(v_birth_date) + ", 'knise')\n";
            //sql += "  and isretire = 'N'                                \n"; // 탈퇴하지 않은 회원
            sql += "  and handphone is not null                           ";
            
            ls = connMgr.executeQuery(sql);

            while( ls.next() ) { 
                data = ls.getDataBox();
                list.add(data);
            }
        }
        catch (Exception ex) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally {
            if(ls != null) {try {ls.close();} catch(Exception e){}}
             if(pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }
        return list;
    }
    
    /** 인증번호 발송 및 처리 결과
     * @param box       receive from the form object and session
     * @return isOk    -- 성공여부
     * @throws Exception
     */
    public int sendSMS(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        String sql = "";
        DataBox data    = null;
        int isOk = 0;

        String v_userid = box.getString("p_userid");
        String v_handphone = box.getString("p_handphone");
        String ran = box.getString("ran");
     
        try {
            connMgr = new DBConnectionManager();

            // 인증번호 발송여부
            sql = " select nvl(count(seq),0) as cnt from TZ_SMSLOSS \n";
            sql += " where userid = " + StringManager.makeSQL(v_userid) + " \n";
            sql += "   and handphone = " + StringManager.makeSQL(v_handphone) + " \n";
            sql += "   and to_char(sysdate, 'YYYYMMDDHH24MISS') between stdt and endt";
            ls = connMgr.executeQuery(sql);
            
            int iseq = 0;
          	while( ls.next() ) { 
          		iseq = ls.getInt("cnt");
          	}
          	ls.close();
          	
          	if(iseq > 0) {
          		isOk = -1;
          	} else {      
          		connMgr.setAutoCommit(false);
          		
	            // max seq번호를 조회한다.
	            sql = " select nvl(max(seq),0) as cnt from TZ_SMSLOSS";
	            ls = connMgr.executeQuery(sql);
	            
	            iseq = 0;
	          	while( ls.next() ) { 
	          		iseq = ls.getInt("cnt") + 1;
	          	}
	          	ls.close();
	            
	          	// 시작시간과 종료시간을 3분으로 셋팅한다.
	            sql  = " insert into TZ_SMSLOSS(SEQ,USERID,HANDPHONE,SERIAL,STDT,ENDT) \n";
	            sql += " values(?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), to_char(sysdate + 3/24/60, 'YYYYMMDDHH24MISS'))";
	            
	            pstmt = connMgr.prepareStatement(sql);
	            pstmt.setInt(1, iseq);
	            pstmt.setString(2, v_userid);
	            pstmt.setString(3, v_handphone);
	            pstmt.setInt(4, Integer.parseInt(ran));
			
	            isOk = pstmt.executeUpdate();
            
	            if(isOk > 0){
	            	connMgr.commit();
	        	} else {
	        		connMgr.rollback();
	        	}
          	}
     	} catch ( Exception ex ) {
     		ErrorManager.getErrorStackTrace(ex, box, sql);
     		throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
     	} finally {
     		if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
     		if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
     		if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
     	}
        return isOk;
    }
    
    
    /** 인증번호 입력후 비밀번호 조회
     * @param box       receive from the form object and session
     * @return isOk    -- 성공여부
     * @throws Exception
     */
    public int findPwd(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        String sql = "";
        DataBox data    = null;
        int isOk = 0;

        String v_userid = box.getString("p_userid");
        String v_serial = box.getString("p_serial");
        String v_handphone = box.getString("p_handphone");
     
        try {
            connMgr = new DBConnectionManager();

            // 인증번호 일치 여부
            sql = " select nvl(count(seq),0) as cnt from TZ_SMSLOSS \n";
            sql += " where userid = " + StringManager.makeSQL(v_userid) + " \n";
            sql += "   and handphone = " + StringManager.makeSQL(v_handphone) + " \n";
            sql += "   and serial = " + StringManager.makeSQL(v_serial) + " \n";
            ls = connMgr.executeQuery(sql);
            
            int icnt1 = 0;
          	while( ls.next() ) { 
          		icnt1 = ls.getInt("cnt"); // 일치여부
          	}
          	ls.close();
          	
            sql = " select nvl(count(seq),0) as cnt from TZ_SMSLOSS \n";
            sql += " where userid = " + StringManager.makeSQL(v_userid) + " \n";
            sql += "   and handphone = " + StringManager.makeSQL(v_handphone) + " \n";
            sql += "   and serial = " + StringManager.makeSQL(v_serial) + " \n";
            sql += "   and to_char(sysdate, 'YYYYMMDDHH24MISS') between stdt and endt";
            ls = connMgr.executeQuery(sql);
            
            int icnt2 = 0;
          	while( ls.next() ) { 
          		icnt2 = ls.getInt("cnt"); // 시간초과여부
          	}
          	ls.close();
          	
          	if(icnt1 > 0 && icnt2 > 0) { // 인증번호 일치 및 시간내 입력
          		isOk = 1;
          	} else if(icnt1 == 0 && icnt2 > 0){// 인증번호 불일치 및 시간내 입력
          		isOk = -1;
          	} else if(icnt1 > 0 && icnt2 == 0){// 인증번호 일치 및 시간외 입력
          		isOk = -2;
          	}
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }
    
    /** 비밀번호 변경
     * @param box       receive from the form object and session
     * @return isOk    -- 성공여부
     * @throws Exception
     */
    public int changePwd(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        ListSet ls = null;
        String sql = "";
        int isOk = 0;

        String v_userid = box.getString("p_userid");
        String v_handphone = box.getString("p_handphone");
        
        try {
            connMgr = new DBConnectionManager();
            
            sql = " select serial from TZ_SMSLOSS \n";
            sql += " where userid = " + StringManager.makeSQL(v_userid) + " \n";
            sql += "   and handphone = " + StringManager.makeSQL(v_handphone) + " \n";
            ls = connMgr.executeQuery(sql);
            
            String v_serial = "";
          	while( ls.next() ) { 
          		v_serial = ls.getString("serial");
          	}
          	ls.close();
            
          	// 비밀번호를 초기화한다.
          	// 주민등록번호 뒷자리 7자리로 셋팅한다.
            sql  = " update tz_member \n";
            //sql += " set pwd = " + StringManager.makeSQL(v_serial) + " \n";
            sql += " set pwd = substr(fn_crypt('1', birth_date, 'knise') birth_date, 7,7) \n";
            sql += "   , lgfail = 0 \n";
            sql += " where userid = " + StringManager.makeSQL(v_userid) + " \n";
            
            isOk = connMgr.executeUpdate(sql);
        
            if(isOk > 0){
            	connMgr.commit();
        	} else {
        		connMgr.rollback();
        	}
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally {
        	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }
    
    /** 현재 저장되어 있는 IP
     * @param box       receive from the form object and session
     * @return sip    -- 저장된 ip
     * @throws Exception
     */
    public String getIp(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        String sip = "";

        String v_userid = box.getSession("userid");
     
        try {
            connMgr = new DBConnectionManager();

            // 현재 저장되어 있는 IP를 찾아 온다.
            sql  = "   SELECT DISTINCT LGIP ";
            sql += "\n FROM TZ_LOGINID     ";
            sql += "\n WHERE USERID = " + StringManager.makeSQL(v_userid);

            ls = connMgr.executeQuery(sql);
            
          	while( ls.next() ) { 
          		sip = ls.getString("LGIP");
          	}
          	
          	// sip가 없는 경우는 로그아웃된 상황임을 의미한다.
          	// 그러므로 로그아웃된 상황하에서는 이전아이피와 현재아이피가 동일하면상관없다.
          	if(sip == null || sip.equals("")) {
          		sip = box.getSession("userip");
          	}
          	
          	ls.close();
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally {
        	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return sip;
    }
    
    /** 회사별로 포탈로그인 가능여부
     * @param box       receive from the form object and session
     * @return isOk    -- 가능여부 Y:가능 N:불가능
     * @throws Exception
     */
    public int checkPortalYn(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        StringBuffer sbSQL = new StringBuffer();
        int isOk = 0;

        String v_userid = box.getString("p_userid");
     
        try {
            connMgr = new DBConnectionManager();

            // 현재 저장되어 있는 IP를 찾아 온다.
            sbSQL.append("\n SELECT COUNT(*) CNT FROM TZ_MEMBER WHERE USERID = " + StringManager.makeSQL(v_userid));
            ls = connMgr.executeQuery(sbSQL.toString());

            int icnt = 0;
          	while( ls.next() ) { 
          		icnt = ls.getInt("CNT");
          	}
            ls.close();
                        
            if(icnt == 0) {
            	isOk = -1;
            } else {
                /* 회사정보에서 포탈로그인 가능 여부를 체크한다. 기능 보류함.
            	sbSQL.setLength(0);
            	sbSQL.append("\n SELECT DECODE(ISPOTALLOGINYN, 'N', 0, 1) ISPORTAL			");
       			sbSQL.append("\n FROM TZ_COMPCLASS A, TZ_MEMBER B							");
            	sbSQL.append("\n WHERE A.COMP = B.COMP										");
            	sbSQL.append("\n   AND B.USERID = " + StringManager.makeSQL(v_userid));

            	ls = connMgr.executeQuery(sbSQL.toString());
                
                while( ls.next() ) { 
              		isOk = ls.getInt("ISPORTAL");
              	}
                ls.close();
                */
            	isOk = 1;
            }
          	
          	ls.close();
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally {
        	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return isOk;
    }
    
    /** 선택한 권한정보가 있는지의 여부
     * @param box       receive from the form object and session
     * @return bOk    -- 가능여부
     * @throws Exception
     */
    public boolean getGadminOk(RequestBox box) throws Exception {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        String sql = "";
        int icnt = 0;
        boolean bOk = false;

        String v_userid = box.getSession("userid");
        String v_gadmin = box.getString("p_auth");
     
        if ( v_gadmin.equals("ZZ") )
        	return true;
        
        try {
            connMgr = new DBConnectionManager();

            sql += "\n select count(b.gadmin) as cnt					     			";
            sql += "\n from tz_manager a, tz_gadmin b					     			";
            sql += "\n where a.gadmin    = b.gadmin					     				";
            sql += "\n    and a.userid    = " + StringManager.makeSQL(v_userid);
            sql += "\n    and a.isdeleted = 'N'						     				";
            sql += "\n    AND a.gadmin = " + StringManager.makeSQL(v_gadmin);
            sql += "\n    and to_char(sysdate,'yyyymmdd') between a.fmon and a.tmon	    ";
            sql += "\n order by b.gadmin asc						     				";

            ls = connMgr.executeQuery(sql);
            
          	while( ls.next() ) { 
          		icnt = ls.getInt("cnt");
          	}
          	
          	if(icnt > 0) {
          		bOk = true;
          	}
          	ls.close();
        } catch ( Exception ex ) {
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally {
        	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
        return bOk;
    }
}
