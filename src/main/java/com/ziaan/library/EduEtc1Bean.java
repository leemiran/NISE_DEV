
package com.ziaan.library;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Hashtable;

import com.ziaan.common.GetCodenm;

  /**
 * <p> 제목: 학습관련 라이브러리</p> 
 * <p> 설명: </p> 
 * <p> Copyright: Copyright (c) 2004</p> 
 * <p> Company:  </p> 
 *@author 이정한
 *@date 2003. 12
 *@version 1.0
 */
public class EduEtc1Bean { 

    public EduEtc1Bean() { }

    /**
    * 학습자 여부 체크
    @param String   p_subj 과목코드
    @param String   p_year 연도코드
    @param String   p_subjseq 과목기수코드
    @param String   p_userid 사용자ID
    @return String 학습자 여부 리턴
    */
    public static String isCurrentStudent(String p_subj, String p_year, String p_subjseq, String p_userid, String v_examtype, String v_papernum) throws Exception{ 

        DBConnectionManager	connMgr	= null;
        ListSet ls1 = null;
        ListSet ls2 = null;
        ListSet ls3 = null;
        String  sql  = "";
        int check = 0;
        String  v_edustart  = "";
        String  v_eduend    = "";
        
        if ( p_year.equals("PREV") || p_subjseq.equals("PREV") ) { 
            return "Y";
        }

        int v_sysdate   = Integer.parseInt(FormatDate.getDate("yyyyMMddhh") );

        try { 
            connMgr = new DBConnectionManager();

            // 1.학습자여부 체크
            sql = "select count(userid) CNTS from tz_student "
                + " where subj   =" +StringManager.makeSQL(p_subj)
                + "   and year   =" +StringManager.makeSQL(p_year)
                + "   and subjseq=" +StringManager.makeSQL(p_subjseq)
                + "   and userid =" +StringManager.makeSQL(p_userid);
            ls1 = connMgr.executeQuery(sql);

            if ( ls1.next() ) { 
                check = ls1.getInt("CNTS");
            }

            if ( check > 0 ) { //기존에는 교육기간 체크였지만 시험기간으로 대체
              
            	//교육기간 체크
                sql ="select a.grcode, a.edustart, a.eduend, a.isonoff, b.edustart s_edustart, b.eduend s_eduend "
                    + "  from vz_scsubjseq a, tz_student b "
                    + "  where a.scsubj=b.subj and a.scyear=b.year and a.scsubjseq=b.subjseq "
                    + "   and a.scsubj   =" +StringManager.makeSQL(p_subj)
                   + "   and a.scyear   =" +StringManager.makeSQL(p_year)
                    + "   and a.scsubjseq=" +StringManager.makeSQL(p_subjseq)
                    + "   and userid =" +StringManager.makeSQL(p_userid);
                
                ls2 = connMgr.executeQuery(sql);
                
            	sql =" select ";
            	sql+="   count(1) as cnt ";
            	sql+=" from tz_exampaper a, "; 
            	sql+="      tz_subj       b   ";
            	sql+=" where a.subj( +)        = b.subj   ";
            	sql+=" and a.subj   =" +StringManager.makeSQL(p_subj);
            	sql+=" and a.year   =" +StringManager.makeSQL(p_year);
            	sql+=" and a.subjseq   =" +StringManager.makeSQL(p_subjseq);
            	sql+=" and a.papernum   =" +StringManager.makeSQL(v_papernum);
            	sql+=" and sysdate between to_DATE(startdt||'000000','YYYYMMDDHH24MISS') and to_DATE(ENDDT||'235959','YYYYMMDDHH24MISS') ";

                ls3 = connMgr.executeQuery(sql);
                
                int cnt = 0;
                if(ls3.next()){
                	cnt = ls3.getInt("cnt");
                }

                if ( ls2.next() ) { 
                    if ( ls2.getString("grcode").substring(0,1).equals("C") ) {    // B2C교육그룹이면 tz_student의 학습기간dk
                        v_edustart  = ls2.getString("s_edustart");
                        v_eduend    = ls2.getString("s_eduend");
                    } else { 
                        v_edustart  = ls2.getString("edustart");
                        v_eduend    = ls2.getString("eduend");
                    }

                    // 집합과목은 학습기간 이후에도 과제 제출 가능 (2003.11.24)
                    if ( ( ls2.getString("isonoff")).equals("ON") ) { 
                        if ( v_sysdate >= Integer.parseInt(v_edustart) && v_sysdate <= Integer.parseInt(v_eduend)) { 
                            return "Y";     // 학습기간중이면  Y
                        }else { 
                           // return "N";
                        	if(cnt > 0){
                        		return "Y";
                        	}else{
                        		return "N";
                        	}
                        }
                    } else { 
                        return "Y";
                    }
                	
                	
                }
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return  "N";
    }

    /**
     * 학습자 여부 체크
     @param String   p_subj 과목코드
     @param String   p_year 연도코드
     @param String   p_subjseq 과목기수코드
     @param String   p_userid 사용자ID
     @return String 학습자 여부 리턴
     */
     public static String isCurrentStudent(String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception{ 

         DBConnectionManager	connMgr	= null;
         ListSet ls1 = null;
         ListSet ls2 = null;
         String  sql  = "";
         int check = 0;
         String  v_edustart  = "";
         String  v_eduend    = "";
         
         if ( p_year.equals("PREV") || p_subjseq.equals("PREV") ) { 
             return "Y";
         }

         int v_sysdate   = Integer.parseInt(FormatDate.getDate("yyyyMMddhh") );

         try { 
             connMgr = new DBConnectionManager();

             // 1.학습자여부 체크
             sql = "select count(userid) CNTS from tz_student "
                 + " where subj   =" +StringManager.makeSQL(p_subj)
                 + "   and year   =" +StringManager.makeSQL(p_year)
                 + "   and subjseq=" +StringManager.makeSQL(p_subjseq)
                 + "   and userid =" +StringManager.makeSQL(p_userid);
             ls1 = connMgr.executeQuery(sql);

             if ( ls1.next() ) { 
                 check = ls1.getInt("CNTS");
             }

             if ( check > 0 ) { 
                 // 1.2 학습자이므로 학습기간여부 체크
             /*
                 sql = "select a.grcode, a.edustart, a.eduend, b.edustart s_edustart, b.eduend s_eduend "
                     + "  from tz_subjseq a, tz_student b "
                     + " where a.subj=b.subj and a.year=b.year and a.subjseq=b.subjseq "
                     + "   and a.subj   =" +StringManager.makeSQL(p_subj)
                     + "   and a.year   =" +StringManager.makeSQL(p_year)
                     + "   and a.subjseq=" +StringManager.makeSQL(p_subjseq)
                     + "   and userid =" +StringManager.makeSQL(p_userid);
             */
                 // to get the value of isonoff (집합/사이버 과목 여부 edited by MSCHO ) (2003.11.24)
             	//교육기간 체크
                 sql ="select a.grcode, a.edustart, a.eduend, a.isonoff, b.edustart s_edustart, b.eduend s_eduend "
                     + "  from vz_scsubjseq a, tz_student b "
                     + "  where a.scsubj=b.subj and a.scyear=b.year and a.scsubjseq=b.subjseq "
                     + "   and a.scsubj   =" +StringManager.makeSQL(p_subj)
                     + "   and a.scyear   =" +StringManager.makeSQL(p_year)
                     + "   and a.scsubjseq=" +StringManager.makeSQL(p_subjseq)
                     + "   and userid =" +StringManager.makeSQL(p_userid);

                 ls2 = connMgr.executeQuery(sql);

                 if ( ls2.next() ) { 
                     if ( ls2.getString("grcode").substring(0,1).equals("C") ) {    // B2C교육그룹이면 tz_student의 학습기간
                         v_edustart  = ls2.getString("s_edustart");
                         v_eduend    = ls2.getString("s_eduend");
                     } else { 
                         v_edustart  = ls2.getString("edustart");
                         v_eduend    = ls2.getString("eduend");
                     }

                     // 집합과목은 학습기간 이후에도 과제 제출 가능 (2003.11.24)
                     if ( ( ls2.getString("isonoff")).equals("ON") ) { 
                         if ( v_sysdate >= Integer.parseInt(v_edustart) && v_sysdate <= Integer.parseInt(v_eduend)) { 
                             return "Y";     // 학습기간중이면  Y
                         }else { 
                             return "N";
                         }
                     } else { 
                         return "Y";
                     }
                 }
             }

         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }finally { 
             if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
             if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return  "N";
     }
    /**
    * 학습용 권한값 결정
    @param RequestBox box
    @return String 학습용권한값
    * - 학습가능여부 체크
    * 1. 학습자이면
    *     = 학습기간중이면    Y
    *     = 종료된 이후이면
    *         > .청강기간이면  P
    *         > .Reject        N
    * 2. 특수권한자이면       P
    * 3. Reject               N
    * 
    *     return 'Y' :    컨텐츠 조회 가능, 과제/평가/액티비티/토론/의견입력 가능, 로깅
    *     return 'N';     불가= > 창닫아버려.
    *     return 'P';     컨텐츠 조회만 가능.로깅하지 않음.
    * 
    * == > 결과값을 session변수 ("s_eduauth")로 Set.
    */
    public static String get_eduAuth(RequestBox box) throws Exception{ 

        DBConnectionManager	connMgr	= null;
        ListSet ls1 = null;
        ListSet ls2 = null;
        String  sql  = "";
        int check = 0;

        String  s_gadmin    = box.getSession("gadmin");
        String  s_subj      = box.getSession("s_subj");
        String  s_year      = box.getSession("s_year");
        String  s_subjseq   = box.getSession("s_subjseq");
        String  s_userid    = box.getSession("userid");

        String  v_edustart  = "";
        String  v_eduend    = "";
        int v_sysdate   = Integer.parseInt(FormatDate.getDate("yyyyMMddHH") );

        if ( s_gadmin.equals("")||s_userid.equals("")||s_subj.equals("")||s_year.equals("")||s_subjseq.equals("") ) { 
            return "N";
        } 
        
        if ( s_year.equals("PREV") || s_subjseq.equals("PREV") ) { 
            return "Y";
        }

        try { 
            connMgr = new DBConnectionManager();
            
            // 2. 특수권한자이면
            if ( !s_gadmin.substring(0,1).equals("Z") ) { 
                // Ultra, Super, 과목관리자, 강사일 경우만 입력가능토록 한다.
                String s =s_gadmin.substring(0,1);
                // if ( s.equals("A")||s.equals("F")||s.equals("P")||s.equals("T")||s.equals("M")||s.equals("S"))
                if ( s.equals("A")||s.equals("F")||s.equals("P")||s.equals("T")||s.equals("M")||s.equals("S"))
                    return "Y";
                else
                    return "N";
            }

            // 1.학습자여부 체크
            sql = "select count(userid) CNTS from tz_student "
                + " where subj   =" +StringManager.makeSQL(s_subj)
                + "   and year   =" +StringManager.makeSQL(s_year)
                + "   and subjseq=" +StringManager.makeSQL(s_subjseq)
                + "   and userid =" +StringManager.makeSQL(s_userid);
            
            System.out.println(sql);
            ls1 = connMgr.executeQuery(sql);

            if ( ls1.next() ) { 
                check = ls1.getInt("CNTS");
            }

            if ( check > 0 ) { 
                // 1.2 학습자이므로 학습기간여부 체크
                sql = "select a.grcode, a.edustart, a.eduend, b.edustart s_edustart, b.eduend s_eduend "
                    + "  from tz_subjseq a, tz_student b "
                    + " where a.subj=b.subj and a.year=b.year and a.subjseq=b.subjseq "
                    + "   and a.subj   =" +StringManager.makeSQL(s_subj)
                    + "   and a.year   =" +StringManager.makeSQL(s_year)
                    + "   and a.subjseq=" +StringManager.makeSQL(s_subjseq)
                    + "   and userid =" +StringManager.makeSQL(s_userid);
                ls2 = connMgr.executeQuery(sql);

                if ( ls2.next() ) { 
                    if ( ls2.getString("grcode").substring(0,1).equals("C") ) {    // B2C교육그룹이면 tz_student의 학습기간
                        v_edustart  = ls2.getString("s_edustart");
                        v_eduend    = ls2.getString("s_eduend");
                    } else { 
                        v_edustart  = ls2.getString("edustart");
                        v_eduend    = ls2.getString("eduend");
                    }

                    if ( v_sysdate >= Integer.parseInt(v_edustart) && v_sysdate <= Integer.parseInt(v_eduend)) { 
                        return "Y";     // 학습기간중이면  Y
                    } else if ( !s_gadmin.substring(0,1).equals("Z") ) { 
                        sql = "";           // dummy code for process
                    } else if ( v_sysdate > Integer.parseInt(v_eduend)) { 
                    // 학습기간 이후이고
                        int overTerm = Integer.parseInt(GetCodenm.get_config("overEduTerm") );
                    // 1.3 tz_config의 청강유효개월수 이하이면 P   (v_eduend 자리수때문에 에러나서 "00" 추가
                        if ( v_sysdate<=Integer.parseInt(FormatDate.getDayAdd(v_eduend + "00","yyyyMMddhh","month",overTerm))) { 
                            return "P";
                        } else { 
                            return "N";
                        }
                    }
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return  "N";
    }
    
    /**
    * 학습창 시작URL 만들기
    @param String   과목코드, String 구분("PGM":서블릿Url,"DOC":Document-Base)
    @return String tz_config.vals
    */
    public static String make_betaEduURL(String p_subj,String p_gubun) throws Exception{ 
        String  v_vals = "";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";
        String  v_server= "";
        String  v_dir   = "";
        String  v_port  = "";
        String  v_eduurl    = "";
        String  v_domain    = GetCodenm.get_config("eduDomain");

        String  results = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  server,domain,port,contenttype, dir  from tz_subj "
                + "  where  subj=" +SQLString.Format(p_subj);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   { 
                v_server = ls.getString("server");
                v_domain = ls.getString("domain");
                v_port   = ls.getString("port");
                v_dir    = ls.getString("dir");
            }

            if ( v_eduurl.equals("") ) { 
                if ( p_gubun.equals("DOC") ) {     // Contents Document Base
                    results = "http:// " +v_server +v_domain +v_port + "/" +v_dir + "/";
                } else {                          // Servlet Url
                    results = "http:// " +v_server +v_domain +v_port + "/servlet/controller.beta.BetaEduStart?p_subj=" +p_subj;
                    // results = "/servlet/controller.beta.BetaEduStart?p_subj=" +p_subj;
                }
            } else { 
                results = v_eduurl;
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return  results;
    }
    
    /**
    * 학습창 시작URL 만들기
    @param String   과목코드, String 구분("PGM":서블릿Url,"DOC":Document-Base)
    @return String tz_config.vals
    */
    public static String make_eduURL(String p_subj,String p_gubun) throws Exception{ 
        String  v_vals = "";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";
        String  v_server= "";
        String  v_dir   = "";
        String  v_port  = "";
        String  v_eduurl    = "";
        String  v_domain    = GetCodenm.get_config("eduDomain");

        String  results = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  server, port, contenttype, eduurl,dir  from tz_subj "
                + "  where  subj=" +SQLString.Format(p_subj);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   { 
                v_server = ls.getString("server");
                v_port   = ls.getString("port");
                v_eduurl = ls.getString("eduurl");
                v_dir    = ls.getString("dir");
            }

            if ( v_eduurl.equals("") ) { 
                if ( !v_server.equals(""))                        v_server = v_server + ".";
                if ( !v_port.equals("80") && !v_port.equals(""))  v_port   = ":" +v_port;

                if ( p_gubun.equals("DOC") ) {     // Contents Document Base
//                    results = "http:// " +v_server +v_domain +v_port + "/" +v_dir + "/";
                    results = "/" +v_dir + "/";
                } else {                              // Servlet Url
  //                  results = "http:// " +v_server +v_domain +v_port + "/servlet/controller.beta.BetaEduStart?p_subj=" +p_subj;
                    results = "/servlet/controller.contents.EduStart?p_subj=" +p_subj;
                }
            } else { 
                results = v_eduurl;
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return  results;
    }
    
    /**
    * 학습창 시작URL 만들기
    @param String   과목코드, String 구분("PGM":서블릿Url,"DOC":Document-Base)
    @return String tz_config.vals
    */
    public static String make_eduURL(String p_subj,String p_year, String p_subjseq) throws Exception{ 
        String v_year   ="2000", v_subjseq="0001";
        if ( !p_year.equals("")) v_year      = p_year;
        if ( !p_subjseq.equals(""))  v_subjseq   = p_subjseq;

        return EduEtc1Bean.make_eduURL(p_subj,"PGM") + "&p_year=" +v_year + "&p_subjseq=" +v_subjseq;

    }

    
    /**
    * 학습창 시작URL 만들기
    @param String   과목코드, String 구분("PGM":서블릿Url,"DOC":Document-Base)
    @return String tz_config.vals
    */
    public static String make_eduURL(String p_subj,String p_year, String p_subjseq, String p_userid) throws Exception{ 
        String v_year   ="2000", v_subjseq="0001";
        String results = "";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";
        String  v_contenttype= "";
        String  v_birth_date = "";
        String  v_eduurl    = "";

        if ( !p_year.equals("")) v_year      = p_year;
        if ( !p_subjseq.equals(""))  v_subjseq   = p_subjseq;

        try { 
            connMgr = new DBConnectionManager();

            sql = " select  contenttype, eduurl  from tz_subj "
                    + "  where  subj=" +SQLString.Format(p_subj);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   { 
                v_contenttype = ls.getString("contenttype");
                v_eduurl = ls.getString("eduurl");
            }

            // Link 과목일경우
            if ( v_contenttype.equals("L") ) { 
                if ( v_eduurl.equalsIgnoreCase("CREDU") ) { // CREDU Link일경우
                    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                    sql = "select fn_crypt('2', birth_date, 'knise') birth_date from tz_member where userid='" +p_userid + "'";
                    ls = connMgr.executeQuery(sql);
                    ls.next();
                    v_birth_date=ls.getString("birth_date");
                    results = "http:// www.ziaan.com/pls/cyber/zasp.new_study1?p_subj=" +p_subj + "&p_birth_date=" +v_birth_date;
                } else { 
                    results = v_eduurl;
                }
            } else { 
                results = EduEtc1Bean.make_eduURL(p_subj,"PGM") + "&p_year=" +v_year + "&p_subjseq=" +v_subjseq;
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }


        // return EduEtc1Bean.make_eduURL(p_subj,"PGM") + "&p_year=" +v_year + "&p_subjseq=" +v_subjseq;
        return results;

    }


    /**
    * Lesson 시작URL 만들기
    @param String   p_gubun : '1':whole url, '2':lesson까지
    @return String tz_config.vals
    */
    public static String make_startURL(String p_gubun, String p_subj, String p_server, String p_port, String p_dir, String p_lesson) throws Exception{ 
        // 수정시 controlLessonBranch 수정 필!!!
        String  results = "", v_server= "", v_port= "", v_lesson="";

        if ( p_lesson.length() == 3 ) {
            v_lesson = p_lesson.substring(1, 3);
        }

        if ( !p_server.equals(""))                        v_server = v_server + ".";
        if ( !p_port.equals("80") && !p_port.equals(""))  v_port   = ":" +p_port;
        results = "http://" +v_server +GetCodenm.get_config("eduDomain") +v_port + "/" +p_dir + "/docs/" +v_lesson;
        if ( p_gubun.equals("1")) results +="/start.html";

        return  results;
    }

    /**
    * preview URL 만들기
    @param String    컨텐츠타입,과목코드,서버,포트,디렉토리,외부preurl
    @return String url
    */
    public static String make_previewURL(   String p_contenttype,
                                            String p_subj,
                                            String p_server,
                                            String p_port,
                                            String p_dir,
                                            String p_preurl) throws Exception{ 
//        String  results = "", v_server= "", v_port= "";
//        if ( p_preurl != null && !p_preurl.equals("")) return p_preurl;
//
//        if ( !p_server.equals(""))                        v_server = v_server + ".";
//        if ( !p_port.equals("80") && !p_port.equals(""))  v_port   = ":" +p_port;
//        if ( p_contenttype.equals("O"))
//            return make_eduURL(p_subj,"PREV","PREV");
//        else
//            return  "http:// " +v_server +GetCodenm.get_config("eduDomain") +v_port + "/" +p_dir + "/guest/guest.html";
    	 String  v_server= "", v_port= ""; //results = "", 
         
         ConfigSet conf = new ConfigSet();
         String home = conf.getProperty("dir.home");
         String path = home + p_dir + "\\guest\\guest.html";
         File file = new File(path);
         
         if ( p_preurl != null && !p_preurl.equals("")) return p_preurl;

         if ( !p_server.equals(""))                        v_server = v_server + ".";
         if ( !p_port.equals("80") && !p_port.equals(""))  v_port   = ":" +p_port;
         if ( p_contenttype.equals("O"))
             return make_eduURL(p_subj,"PREV","PREV");
         else if ( p_contenttype.equals("N")) {
         	if(file.exists()) 
         		return v_server + GetCodenm.get_config("eduDomain") +v_port + "/" +p_dir + "/guest/guest.html"; //"http:// " +
         	else 
         		return "";
         } else
             return  "";//v_server +GetCodenm.get_config("eduDomain") +v_port + "/" +p_dir + "/guest/guest.html"; //"http:// " +
    }
    /**
    * preview URL 만들기
    @param String   과목코드
    @return String url
    */
    public static String make_previewURL(String p_subj) throws Exception{ 
        String  v_vals = "";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";
        String  v_contenttype= "";
        String  v_server= "";
        String  v_dir   = "";
        String  v_port  = "";
        String  v_preurl    = "";
        String  results = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  server, port, contenttype, preurl,dir  from tz_subj "
                + "  where  subj=" +SQLString.Format(p_subj);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   { 
                v_server = ls.getString("server");
                v_port   = ls.getString("port");
                v_preurl = ls.getString("preurl");
                v_dir    = ls.getString("dir");
                v_contenttype = ls.getString("contenttype");
            }
            if ( v_preurl.equals(""))
                results = make_previewURL(v_contenttype,p_subj,v_server,v_port,v_dir,v_preurl);
            else
                results = v_preurl;


        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return  results;
    }
    /**
    * 학습자 branch 얻기
    @param String   과목코드, 년도, 과목기수, userid
    @return int  branch
    */
    public static int get_mybranch(String p_subj,String p_year, String p_subjseq, String p_userid) throws Exception{ 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";
        int resulti = 99;

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  branch  from tz_student "
                + "  where  subj=" +SQLString.Format(p_subj)
                + "    and  year=" +SQLString.Format(p_year)
                + "    and  subjseq=" +SQLString.Format(p_subjseq)
                + "    and  userid=" +SQLString.Format(p_userid);

            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   { 
                resulti  = ls.getInt("branch");
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return  resulti;
    }
    /**
    * 학습자 branch Set
    @param String   과목코드, 년도, 과목기수, userid, branch
    @return int  isOk
    */
    public static int set_mybranch(String p_subj,String p_year, String p_subjseq, String p_userid, int p_branch) throws Exception{ 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";
        int resulti = 1;

        try { 
            connMgr = new DBConnectionManager();
            sql = " update tz_student set branch= " + p_branch
                + "  where  subj=" +SQLString.Format(p_subj)
                + "    and  year=" +SQLString.Format(p_year)
                + "    and  subjseq=" +SQLString.Format(p_subjseq)
                + "    and  userid=" +SQLString.Format(p_userid);

            resulti = connMgr.executeUpdate(sql);
            connMgr.commit();

        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return  resulti;
    }
    /**
    * 미학습 학습객체수 얻기(진도/평가)
    @param String   과목코드, 년도, 과목기수, userid, 과목타입(N/O/S),lesson, oid
    @return int  미학습객체수
    */
    public static int get_noteducnt(    String p_subj,
                                        String p_year,
                                        String p_subjseq,
                                        String p_userid,
                                        String p_contenttype,
                                        String p_lesson,
                                        String p_oid) throws Exception{ 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        ResultSet rs = null;
        String  sql  = "", v_contenttype= "";
        int resulti = 99, v_basecnt=0, v_educnt=0, isOk = 0;

        try { 
            connMgr = new DBConnectionManager();
            if ( p_contenttype.equals("") ) { 
                sql = "select contenttype from tz_subj where subj=" +StringManager.makeSQL(p_subj);
                ls = connMgr.executeQuery(sql);
                ls.next();
                v_contenttype = ls.getString("contenttype");
            }else   v_contenttype = p_contenttype;
            
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            // 컨텐트타입별 기준 학습객체수 얻기 (SCORM은 추후 추가)
            if ( v_contenttype.equals("O") ) { 
                sql = "select count(oid) CNTS  from tz_subjobj ";

                if ( p_oid.equals("EXAM") ) { 
                    sql = sql
                        +  " where subj=? and lesson<=?)";
                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, p_subj);
                    pstmt.setString(2, p_lesson);
                } else { 
                    sql = sql
                        + " where subj=? "
                        + "   and ( (lesson < ?)"
                        + "        or (lesson=? and "
                        + "            ordering < (select ordering from tz_subjobj where subj=? and lesson=? and oid=?) ) )";
                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, p_subj);
                    pstmt.setString(2, p_lesson);
                    pstmt.setString(3, p_lesson);
                    pstmt.setString(4, p_subj);
                    pstmt.setString(5, p_lesson);
                    pstmt.setString(6, p_oid);
                }

            } else if ( v_contenttype.equals("N") ) { 
                if ( p_oid.equals("EXAM") ) { 
                    sql = "select count(lesson) CNTS from tz_subjlesson where subj=? and lesson<=?";
                } else { 
                    sql = "select count(lesson) CNTS from tz_subjlesson where subj=? and lesson<?";
                }
                pstmt = connMgr.prepareStatement(sql);
                pstmt.setString(1, p_subj);
                pstmt.setString(2, p_lesson);
            }
            rs = pstmt.executeQuery();
            rs.next();
            v_basecnt = rs.getInt("CNTS");
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }

            // Base 평가수 얻기
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            sql = "select count(ptype) CNTS from tz_exammaster where subj=? and lesson<? and year='TEST'";
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, p_subj);
            pstmt.setString(2, p_lesson);
            rs = pstmt.executeQuery();
            rs.next();
            v_basecnt += rs.getInt("CNTS");

            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            // 미학습 객체수 얻기
            if ( v_contenttype.equals("O") ) { 
                sql = "select count(a.userid) CNTS  from tz_progress a, tz_subjobj b "
                    + " where a.subj=b.subj and a.oid=b.oid and a.subj=? and a.year=? and  a.subjseq=? and a.userid=?"
                    + "   and first_end is not null ";
                if ( p_oid.equals("EXAM") ) { 
                    sql = sql + "  and b.lesson <=?";
                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, p_subj);
                    pstmt.setString(2, p_year);
                    pstmt.setString(3, p_subjseq);
                    pstmt.setString(4, p_userid);
                    pstmt.setString(5, p_lesson);
                } else { 
                    sql = sql + "   and (b.lesson < ? or (b.lesson=? "
                        + "                         and b.ordering<(select ordering from tz_subjobj where subj=? and lesson=? and oid=?)))";
                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, p_subj);
                    pstmt.setString(2, p_year);
                    pstmt.setString(3, p_subjseq);
                    pstmt.setString(4, p_userid);
                    pstmt.setString(5, p_lesson);
                    pstmt.setString(6, p_lesson);
                    pstmt.setString(7, p_subj);
                    pstmt.setString(8, p_lesson);
                    pstmt.setString(9, p_oid);
                }

            } else if ( v_contenttype.equals("N") ) { 
                sql = "select count(userid) CNTS  from tz_progress where subj=? and year=? and subjseq=? and userid=?";
                if ( p_oid.equals("EXAM") ) { 
                    sql = sql + "   and first_end is not null and  lesson <= ? ";
                } else { 
                    sql = sql + "   and first_end is not null and  lesson < ? ";
                }
                pstmt = connMgr.prepareStatement(sql);
                pstmt.setString(1, p_subj);
                pstmt.setString(2, p_year);
                pstmt.setString(3, p_subjseq);
                pstmt.setString(4, p_userid);
                pstmt.setString(5, p_lesson);
            }
            rs = pstmt.executeQuery();
            rs.next();
            v_educnt = rs.getInt("CNTS");
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            // Base 평가결과수 얻기
            sql = "select count(userid) CNTS from tz_examresult where subj=? and year=? and subjseq=? and userid=? and lesson<? ";
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, p_subj);
            pstmt.setString(2, p_year);
            pstmt.setString(3, p_subjseq);
            pstmt.setString(4, p_userid);
            pstmt.setString(5, p_lesson);
            rs = pstmt.executeQuery();
            rs.next();
            v_educnt += rs.getInt("CNTS");

            resulti  = v_basecnt - v_educnt;    // =(학습할 진도/평가수)-(학습한 진도/평가수)

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( rs != null ) { try { rs.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return  resulti;
    }

    /* 평가 실시여부 체크 (고용보험 과목)
     * added 2003/10/24 icarus .. (for OBC)
     */
    public String   chkeckExamGoOn(RequestBox box) throws Exception{ 
        String  v_vals = "";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";
        String  v_contenttype= "";
        String  results     = "Y";
        int resulti     = 0;
        String  p_subj    = box.getSession("s_subj");
        String  p_year    = box.getSession("s_year");
        String  p_subjseq = box.getSession("s_subjseq");
        String  p_lesson  = box.getString("p_lesson");
        String  p_userid  = box.getSession("userid");
        if ( p_subj.equals("") ) { 
            p_subj = box.getString("p_subj");
        }
        if ( p_year.equals("") ) { 
            p_year = box.getString("p_year");
        }
        if ( p_subjseq.equals("") ) { 
            p_subjseq = box.getString("p_subjseq");
        }
        int    v_branch  = 0;
        Hashtable data = null;
        String v_ptype   = box.getString("p_ptype");
        int    v_papernum= 0;
        String  v_oid= "";
        // 고용보험 과목여부
        // Added by LeeSuMin.. 2003.11.15
        if ( GetCodenm.get_isgoyong_seq(p_subj, p_year, p_subjseq).equals("Y")
            && EduEtc1Bean.isCurrentStudent(p_subj,p_year,p_subjseq,p_userid).equals("Y") ) { 
            // OBC이면 Object-ID로 부터 ptype구한다. ('TM0000001','TT00000002')
            if ( GetCodenm.get_contenttype(p_subj).equals("O"))  { 
                v_oid   = box.getString("p_oid");
                v_ptype = v_oid.substring(1,2);
            }
            resulti = get_noteducnt(    p_subj,
                                        p_year,
                                        p_subjseq,
                                        p_userid,
                                        v_contenttype,
                                        p_lesson,
                                        "EXAM");
            if ( resulti > 0)  results = "종료하지 않은 학습Object가 있으므로 평가를 진행할 수 없습니다.";
        }

        return  results;
    }


    /**
    * Session_time 얻기
    @param String   시작일시(varchar2 14), 종료일시(varchar2 14)
    @return String session_time string (03:01:39.52)
    */
    public static String get_duringtime(String p_s, String p_e) throws Exception{ 
        String  results = "";
        int gap = 0;
        try { 
            long    l_gap = FormatDate.getTimeDifference(p_s,p_e);
            int hh =  (int)(l_gap/(1000*60*60));
            gap     = (int)(l_gap - hh*1000*60*60);
            /*
            int mm =  (int)(l_gap/(1000*60));
            int ss =  (int)(l_gap/(1000));
            int ms =  (int)l_gap;
            */
            int mm =  (int)(gap/(1000*60));
            gap     = gap - mm*1000*60;
            int ss =  (int)(gap/1000);
            gap     = gap - ss*1000;
            int ms =  gap;
            results = (new DecimalFormat("00").format(hh)) + ":"
                    + (new DecimalFormat("00").format(mm)) + ":"
                    + (new DecimalFormat("00").format(ss)) + "."
                    + (new DecimalFormat("00").format(ms));
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }finally { 
        }

        return  results;
    }
    /**
    * Session_time 더하기
    @param String   더할넘1(03:01:39.52), 더할넘2(03:01:39.52)
    @return String total_time string (03:01:39.52)
    */
    public static String add_duringtime(String p_s, String p_e) throws Exception{ 
        String  results = "";

        int hh     = 0;
        int mm   = 0;
        double ss = 0.0;

        ss = Double.parseDouble( getSecond(p_s)) + Double.parseDouble( getSecond(p_e) );
        if ( ss > 60.0 ) { 
            ss = ss - 60.0;
            mm = mm + 1;
        }

        mm = mm + Integer.parseInt( getMinute(p_s) ) + Integer.parseInt( getMinute(p_e) );
        if ( mm > 60 ) { 
            mm = mm - 60;
            hh   = hh + 1;
        }
        hh   = hh + Integer.parseInt( getHour(p_s) ) + Integer.parseInt( getHour(p_e) );

        results = (new DecimalFormat("00").format(hh)) + ":"
                + (new DecimalFormat("00").format(mm)) + ":"
                + (new DecimalFormat("00.00").format(ss));

        return  results;
    }
    
	private static String getHour(String time) {
		return getTime(time, "HOUR");
	}

	private static String getMinute(String time) {
		return getTime(time, "MINUTE");
	}

	private static String getSecond(String time) {
		return getTime(time, "SECOND");
	}

	private static String getTime(String time, String type) {
		String result = "";
		String[] str = time.split(":");
		
		if ( str == null || str.length != 3 ) {
			return "00";
		}

		if("HOUR".equals(type)) {
			result = str[0];
		} else if("MINUTE".equals(type)) {
			result = str[1];
		} else if("SECOND".equals(type)) {
			result = str[2];
		}

		return result;
	}    
    
    /**
    * 학습시작 링크 만들기
    @param String   과목코드, 년도, 과목기수, userid
    @return String
    */
    public static String get_starting(String p_subj,String p_year, String p_subjseq, String p_userid) throws Exception{ 
        DBConnectionManager	connMgr	= null;
        ListSet ls = null, ls2= null;
        String  sql  = "";
        String  v_contenttype= "";
        String  results= "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  contenttype  from tz_subj "
                + "  where  subj=" +SQLString.Format(p_subj);
            ls = connMgr.executeQuery(sql);
            ls.next();
            v_contenttype  = ls.getString("contenttype");

            if ( v_contenttype.equals("N") ) { 
                sql = " select  ltrim(to_char(to_number(max(lesson)) +1,'00'))  Maxlesson"
                    + "   from tz_progress "
                    + "  where  subj=" +SQLString.Format(p_subj)
                    + "    and  year=" +SQLString.Format(p_year)
                    + "    and  subjseq=" +SQLString.Format(p_subjseq)
                    + "    and  userid=" +SQLString.Format(p_userid)
                    + "    and  first_end is not null ";
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                ls = connMgr.executeQuery(sql);
                if ( ls.next() )   { 
                    results  = ls.getString("Maxlesson");
                } else { 
                    results = "01";
                }

                sql = "select count(lesson) CNTS from tz_subjlesson where subj=" +SQLString.Format(p_subj)
                    + "   and lesson=ltrim('" +results + "')";

                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                ls = connMgr.executeQuery(sql);
                ls.next();
                if ( ls.getInt("CNTS") == 0)    results = "01";

                results = "('" +results + "')";
            } else if ( v_contenttype.equals("O") ) { 
                String v_module = "01", v_lesson="01", v_oid="1000000001";
                if ( !p_year.equals("PREV") ) { 
                    sql = "select subj,module,lesson,oid,ordering "
                        + "  from tz_subjobj  where subj=" +StringManager.makeSQL(p_subj)
                        + " order by lesson,ordering";
// System.out.println("sql == > " + sql);

                    if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                    ls = connMgr.executeQuery(sql);
                    while ( ls.next() )    { 
                        sql = "select count(*) CNTS from tz_progress "
                            + " where subj=" +StringManager.makeSQL(p_subj)
                            + "   and year=" +StringManager.makeSQL(p_year)
                            + "   and subjseq=" +StringManager.makeSQL(p_subjseq)
                            + "   and userid=" +StringManager.makeSQL(p_userid)
                            + "   and lesson=" +StringManager.makeSQL( ls.getString("lesson"))
                            + "   and oid=" +StringManager.makeSQL( ls.getString("oid"))
                            + "   and (first_end != '' or first_end is not null )";
                        if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                        ls2 = connMgr.executeQuery(sql);
                        ls2.next();
                        if ( ls2.getInt("CNTS") == 0) { 
                            v_module = ls.getString("module");
                            v_lesson = ls.getString("lesson");
                            v_oid    = ls.getString("oid");
                            break;
                        }

                    }
                    results = "('" +v_module + "','" +v_lesson + "','" +v_oid + "')";

                } else { 
                    results = "('01','01','0000000000')";
                }
            }
// System.out.println("v_contenttype == > " + v_contenttype);
// System.out.println("results == > " + results);

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return  results;
    }
    
    
    
    /**
    * 학습창 시작URL 만들기(베타테스트 모의과목에 사용
    @param String   과목코드, String 구분("PGM":서블릿Url,"DOC":Document-Base)
    @return String tz_config.vals
    */
    public static String make_eduURL_beta(String p_subj,String p_gubun) throws Exception{ 
        String  v_vals = "";
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";
        String  v_server= "";
        String  v_dir   = "";
        String  v_port  = "";
        String  v_eduurl    = "";
        String  v_domain    = GetCodenm.get_config("eduDomain");

        String  results = "";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select  contenttype, dir  from tz_subj "
                + "  where  subj=" +SQLString.Format(p_subj);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() )   { 
                // v_server = ls.getString("server");
                // v_port   = ls.getString("port");
                // v_eduurl = ls.getString("eduurl");
                v_dir    = ls.getString("dir");
            }

            // if ( v_eduurl.equals("") ) { 
                // if ( !v_server.equals(""))                        v_server = v_server + ".";
                // if ( !v_port.equals("80") && !v_port.equals(""))  v_port   = ":" +v_port;

                if ( p_gubun.equals("DOC") ) {     // Contents Document Base
//                    results = "http:// " +v_server +v_domain +v_port + "/" +v_dir + "/";
                    results = "/" +v_dir + "/";
                } else {                              // Servlet Url
  //                  results = "http:// " +v_server +v_domain +v_port + "/servlet/controller.beta.BetaEduStart?p_subj=" +p_subj;
                    results = "/servlet/controller.beta.BetaEduStart?p_subj=" +p_subj;
                    
                }
                
            // } else { 
            //   results = v_eduurl;
            // }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return  results;
    }
    
    /**
    * 학습창 시작URL 만들기
    @param String   과목코드, String 구분("PGM":서블릿Url,"DOC":Document-Base)
    @return String tz_config.vals
    */
    public static String make_eduURL_beta(String p_subj,String p_year, String p_subjseq) throws Exception{ 
        String v_year   ="2000", v_subjseq="0001";
        if ( !p_year.equals("")) v_year      = p_year;
        if ( !p_subjseq.equals(""))  v_subjseq   = p_subjseq;

        return EduEtc1Bean.make_eduURL_beta(p_subj,"PGM") + "&p_year=" +v_year + "&p_subjseq=" +v_subjseq;

    }


    /**
    * 스콤과목 맛보기 설정여부 체크
    @param String   과목코드
    @return String 
    */
    public static String hasPreviewObj(String p_subj) throws Exception{ 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";
        String  v_hasPreviewObj = "N";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select a.subj                      						\n"
            	+ "   from tz_previewobj a, tz_subj b  						\n"
            	+ "  where a.subj = b.subj             						\n"
            	+ "    and (b.contenttype = 'S' or b.contenttype = 'O')		\n"
            	+ "    and b.subj = " + StringManager.makeSQL(p_subj) + " 	\n";
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   { 
                v_hasPreviewObj = "Y";
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return  v_hasPreviewObj;
    }


    /**
    * 외주 URL
    @param String   과목코드
    @return String 
    */
    public static String getCpEduurl(String userid,String p_subj, String p_year, String p_subjseq, String p_gadmin) throws Exception{ 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String  sql  = "";
        String  result = "N";

        try { 
            connMgr = new DBConnectionManager();
            sql = " select get_cpsubjeduurl( '" + userid + "','" + p_subj + "','" + p_year + "','" +  p_subjseq + "','" + p_gadmin + "' ) cpurl";
            sql += "   from dual  ";
            System.out.println("sql + ==  == =" +sql);
            
            ls = connMgr.executeQuery(sql);
            if ( ls.next() )   { 
                result = ls.getString("cpurl");
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        }finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return  result;
    }
    
     
     /**
      * 랜덤학습인지 아닌지 여부(랜덤학습이면 return값 Y)
      @param String  과정코드
      @return String 
      */
      public static String israndomaccess(String p_subj) throws Exception{ 
          DBConnectionManager	connMgr	= null;
          ListSet             ls      = null;
          String  sql  = "";
          String v_contentprogress = "";

          try { 
              connMgr = new DBConnectionManager();
              sql = "select contentprogress		\n"
             	 + "from tz_subj																		\n"
             	 + "where subj = " + StringManager.makeSQL(p_subj) + "									\n";
              
              System.out.println(sql);
              
              ls = connMgr.executeQuery(sql);
              if ( ls.next() )   { 
                  v_contentprogress = ls.getString("contentprogress");
              }

          } catch ( Exception ex ) { 
              ErrorManager.getErrorStackTrace(ex);
              throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
          }finally { 
              if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
              if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
          }

          return  v_contentprogress;
      }
      
      /**
       * 가장최근학습한 lesson 정보를 가져온다.
       @param String  과정코드
       @return String 
       */
       public static String maxlesson(String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception{ 
           DBConnectionManager	connMgr	= null;
           ListSet             ls      = null;
           String  sql  = "";
           String v_returnvalue = "";

           try { 
               connMgr = new DBConnectionManager();
               sql = "select nvl(max(lesson),0) maxlesson from tz_progress  \n"+
               		"  where subj    = " +StringManager.makeSQL(p_subj)+ " \n"+
               		"    and year    = " +StringManager.makeSQL(p_year)+ " \n"+
               		"    and subjseq = " +StringManager.makeSQL(p_subjseq)+ " \n"+
               		"    and userid  = " +StringManager.makeSQL(p_userid)+ " \n";
              
               ls = connMgr.executeQuery(sql);
               if ( ls.next() )   { 
             	  v_returnvalue = ls.getString("maxlesson");
               }

           } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex);
               throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
           }finally { 
               if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
               if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
           }

           return  v_returnvalue;
       }
       
       /**
        * 가장 최근에 학습한 차시 및 페이지정보
        * @param p_subj
        * @param p_year
        * @param p_subjseq
        * @param p_userid
        * @return
        * @throws Exception
        */
       public static DataBox getRecentLessionPageInfo(String p_subj, String p_year, String p_subjseq, String p_userid, String tableName) throws Exception{ 
    	   DBConnectionManager	connMgr	= null;
    	   ListSet             ls      = null;
    	   String  sql  = "";
    	   DataBox dbox = null;
    	   StringBuffer strSQL = null;
    		   
    	   try { 
    		   connMgr = new DBConnectionManager();
    		   
    		   strSQL = new StringBuffer();
    		   /*
    		   strSQL.append(" select a.lesson, a.stu_page, b.starting, substr(b.starting, 0, instr(b.starting, '/', -1)) url \n ") ;
    		   strSQL.append("   from tz_progress a, tz_subjlesson b \n ") ;
    		   strSQL.append("  where 1=1 \n ") ;
    		   strSQL.append("    and a.subj = b.subj \n ") ;
    		   strSQL.append("    and a.lesson = b.lesson \n ") ;
    		   strSQL.append("    and a.lesson = ( \n ") ;
    		   strSQL.append("                  select nvl(max(lesson),0) maxlesson from tz_progress \n ") ;
    		   strSQL.append("                   where subj    = " +StringManager.makeSQL(p_subj) + " \n ") ;
    		   strSQL.append("                     and year    = " +StringManager.makeSQL(p_year) + " \n ") ;
    		   strSQL.append("                     and subjseq = " +StringManager.makeSQL(p_subjseq) + " \n ") ;
    		   strSQL.append("                     and userid  = " +StringManager.makeSQL(p_userid) + " ) \n ") ;
    		   */
    		   
    		   strSQL.append(" \n select subj, lesson, stu_page, starting, url, substr(lastpage, instr(lastpage, '/', -1) + 1) lastpage ") ;
    		   strSQL.append(" \n from ( ") ;
    		   strSQL.append(" \n         select subj, lesson, stu_page, starting, url, ") ;
    		   strSQL.append(" \n                (select max(starting) from tz_subjlesson_page where subj = ta.subj and lesson = ta.lesson) lastpage ") ;
    		   strSQL.append(" \n           from ( ") ;
    		   strSQL.append(" \n                 select a.subj, a.lesson, a.stu_page, b.starting, substr(b.starting, 0, instr(b.starting, '/', -1)) url ") ;
    		   strSQL.append(" \n                    from "+tableName+" a, tz_subjlesson b ") ;
    		   strSQL.append(" \n                   where 1=1 ") ;
    		   strSQL.append(" \n                     and a.subj = b.subj ") ;
    		   strSQL.append(" \n                     and a.subj    = "+StringManager.makeSQL(p_subj)+" \n ") ;
    		   strSQL.append(" \n                     and a.year    = "+StringManager.makeSQL(p_year)+" \n ") ;
    		   strSQL.append(" \n                     and a.subjseq = "+StringManager.makeSQL(p_subjseq)+" \n ") ;
    		   strSQL.append(" \n                     and a.userid  = "+StringManager.makeSQL(p_userid)+" \n ") ;
    		   strSQL.append(" \n                     and a.lesson = b.lesson ") ;
    		   strSQL.append(" \n                     and a.lesson = ( ") ;
    		   strSQL.append(" \n                                       select nvl(max(lesson),0) maxlesson from "+tableName+" \n ") ;
    		   strSQL.append(" \n                                        where subj    = "+StringManager.makeSQL(p_subj)+" \n ") ;
    		   strSQL.append(" \n                                          and year    = "+StringManager.makeSQL(p_year)+" \n ") ;
    		   strSQL.append(" \n                                          and subjseq = "+StringManager.makeSQL(p_subjseq)+" \n ") ;
    		   strSQL.append(" \n                                          and userid  = "+StringManager.makeSQL(p_userid)+" \n ") ;
    		   strSQL.append(" \n                                    ) ") ;
    		   strSQL.append(" \n                 ) ta ") ;
    		   strSQL.append(" \n       ) ") ;
    		   

    		   System.out.println(strSQL.toString());
    		   ls = connMgr.executeQuery(strSQL.toString());
    		   
    		   if ( ls.next() )   { 
    			   dbox = ls.getDataBox();
    		   }
    		   
    	   } catch ( Exception ex ) { 
    		   ErrorManager.getErrorStackTrace(ex);
    		   throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	   }finally { 
    		   if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
    		   if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	   }
    	   
    	   return  dbox;
       }
       
       /**
        * 이어보기 정보를 가져온다.
        @param String  과정코드
        @return String 
        */
        public static DataBox getContInfo(String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception{ 
            DBConnectionManager	connMgr	= null;
            ListSet             ls      = null;
            String  sql  = "";
            DataBox v_returnBox = null;

            try { 
                connMgr = new DBConnectionManager();
                sql = "select seq as cont_page, cont_url, lgip from  tz_subjconturl  \n"+
                		"  where subj    = " +StringManager.makeSQL(p_subj)+ " \n"+
                		"    and year    = " +StringManager.makeSQL(p_year)+ " \n"+
                		"    and subjseq = " +StringManager.makeSQL(p_subjseq)+ " \n"+
                		"    and userid  = " +StringManager.makeSQL(p_userid)+ " \n";

                System.out.println(sql);
                
                ls = connMgr.executeQuery(sql);
                if ( ls.next() )   { 
                	v_returnBox = ls.getDataBox();
                }

            } catch ( Exception ex ) { 
                ErrorManager.getErrorStackTrace(ex);
                throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
            }finally { 
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }

            return  v_returnBox;
        }
        
        /**
         * 이어보기 정보를 가져온다.
         @param String  과정코드
         @return String 
         */
         public static DataBox getContUrlInfo(String p_subj) throws Exception{ 
             DBConnectionManager	connMgr	= null;
             ListSet             ls      = null;
             String  sql  = "";
             DataBox v_returnBox = null;

             try { 
                 connMgr = new DBConnectionManager();
                 sql = "select subj, cont_yn, cont_url_info from  tz_subjcontinfo  \n"+
                 		"  where subj    = " +StringManager.makeSQL(p_subj)+ " \n";

                 System.out.println(sql);
                 
                 ls = connMgr.executeQuery(sql);
                 if ( ls.next() )   { 
                 	v_returnBox = ls.getDataBox();
                 }

             } catch ( Exception ex ) { 
                 ErrorManager.getErrorStackTrace(ex);
                 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
             }finally { 
                 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
             }

             return  v_returnBox;
         }
         
         /**
     	 * 
     	 * 컨텐츠 차수별 페이지정보 여부 
     	 * @param reqBox
     	 * @return
     	 * @throws Exception
     	 */
         public static boolean isLessonPage(String subj) throws Exception {
        	 ListSet ls = null;
        	 DBConnectionManager	connMgr	= null;
        	 PreparedStatement pstmt = null;
        	 
        	 String sql = "";
        	 
        	 boolean result = false;
        	 
        	 try {
        		 connMgr = new DBConnectionManager();
        		 
        		 sql = "\n select count(subj) "
        			 + "\n   from tz_subjlesson_page "
        			 + "\n  where  subj = " + StringManager.makeSQL(subj);
        			 
        			 ls = connMgr.executeQuery(sql);
        		 
        		 if ( ls.next() ) {
        			 if(ls.getInt(1) > 0){
        				 result = true;
        			 }
        		 }
        	 }
        	 
        	 catch (Exception ex) {
        		 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        	 }
        	 finally {
        		 if (ls != null) { try { ls.close(); } catch (Exception e) {} }
        		 if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
        		 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e ) { } }
        	 }
        	 
        	 return result;	
         }
         
   
         
         // 들어야 할 LESSON 구하기 20100512 @ Parks
         public static String getStudyLesson(String p_subj, String p_year, String p_subjseq, String p_userid) throws Exception{ 
             DBConnectionManager	connMgr	= null;
             ListSet             ls      = null;
             String  sql  = "";
             String v_returnvalue = "";

             try { 
                 connMgr = new DBConnectionManager();
                 // 앞으로 들어야 할 부분을 표시하도록 하였으나 현재 들은 것 중에 마지막 LESSON 을 표시하는 것으로 수정 @ 20100512 Parks
                 // sql = "SELECT NVL((SELECT MIN(LESSON) FROM TZ_SUBJLESSON WHERE SUBJ = " +StringManager.makeSQL(p_subj)+ " AND LESSON NOT IN (SELECT LESSON FROM TZ_PROGRESS WHERE YEAR = " +StringManager.makeSQL(p_year)+ " AND SUBJ = " +StringManager.makeSQL(p_subj)+ " AND SUBJSEQ = " +StringManager.makeSQL(p_subjseq)+ " AND USERID = " +StringManager.makeSQL(p_userid)+ ")),(SELECT MAX(LESSON) FROM TZ_SUBJLESSON WHERE SUBJ = " +StringManager.makeSQL(p_subj)+ ")) AS LESSON FROM DUAL";
                
                 sql = "SELECT NVL((SELECT MAX(LESSON) FROM TZ_PROGRESS WHERE YEAR = " +StringManager.makeSQL(p_year)+ " AND SUBJ = " +StringManager.makeSQL(p_subj)+ " AND SUBJSEQ = " +StringManager.makeSQL(p_subjseq)+ " AND USERID = " +StringManager.makeSQL(p_userid)+ "),(SELECT MIN(LESSON) FROM TZ_SUBJLESSON WHERE SUBJ = " +StringManager.makeSQL(p_subj)+ ")) AS LESSON FROM DUAL";
                 ls = connMgr.executeQuery(sql);
                 if ( ls.next() )   { 
               	  v_returnvalue = ls.getString("lesson");
                 }

             } catch ( Exception ex ) { 
                 ErrorManager.getErrorStackTrace(ex);
                 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
             }finally { 
                 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
             }

             return  v_returnvalue;
         }
         
         
         
         
         
         
}
