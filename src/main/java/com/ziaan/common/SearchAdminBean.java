// **********************************************************
//  1. 제      목: 검색
//  2. 프로그램명: SearchAdminBean.java
//  3. 개      요: 검색모듈
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: ChungHyun  2008. 10. 06
//  7. 수      정:
// **********************************************************

package com.ziaan.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.system.MemberData;

public class SearchAdminBean { 
    public SearchAdminBean() { }


    /**
     * 그룹코드 검색
     * @param box  receive from the form object and session
     * @return ArrayList 그룹코드 검색리스트
     * @throws Exception
     */
    public ArrayList searchGrcode(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         ArrayList           list    = null;
         String              sql     = "";
         SearchData data = null;

         String v_search     = box.getString("p_gubun");
         String v_searchtext = box.getString("p_key1");
         int v_pageno = box.getInt("p_pageno");

         try { 
             connMgr = new DBConnectionManager();

             list = new ArrayList();

             sql  = "  select grcode, grcodenm  ";
             sql += "    from TZ_GRCODE         ";

             if ( !v_searchtext.equals("") ) {                            //    검색어가 있으면
                 if ( v_search.equals("grcode") ) {                        //    CODE로 검색할때
                     sql += " where grcode like   " + StringManager.makeSQL("%" + v_searchtext + "%");
                 } else if ( v_search.equals("grcodenm") ) {               //    그룹이름으로 검색할때
                     sql += " where grcodenm like " + StringManager.makeSQL("%" + v_searchtext + "%");
                 }
             }
             sql += "   order by grcode asc     ";

             ls = connMgr.executeQuery(sql);

             ls.setPageSize(10);                         //  페이지당 row 갯수를 세팅한다
             ls.setCurrentPage(v_pageno);                //     현재페이지번호를 세팅한다.
             int total_page_count = ls.getTotalPage();   //     전체 페이지 수를 반환한다
             int total_row_count  = ls.getTotalCount();  //     전체 row 수를 반환한다

             while ( ls.next() ) { 
                 data = new SearchData();

                 data.setCode( ls.getString("grcode") );
                 data.setCodenm( ls.getString("grcodenm") );
                 data.setDispnum(total_row_count - ls.getRowNum() + 1);
                 data.setTotalpagecount(total_page_count);

                 list.add(data);
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
     * 과목 검색
     * @param box  receive from the form object and session
     * @return ArrayList 과목 검색리스트
     * @throws Exception
     */
    public ArrayList searchSubj(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         ListSet             ls      = null;
         ArrayList           list    = null;
         String              sql     = "";
         SearchData data = null;

         String v_search     = box.getString("p_gubun");
         String v_searchtext = box.getString("p_key1");
         int v_pageno = box.getInt("p_pageno");

         try { 
             connMgr = new DBConnectionManager();

             list = new ArrayList();

             sql  = "   select a.subj,b.year, b.subjseq, a.subjnm, a.isonoff, get_codenm('0004',a.isonoff) isonoff_value     ";
             sql += "    from TZ_SUBJ a, tz_subjseq b     ";
             sql += "    where a.subj = b.subj      ";


             if ( !v_searchtext.equals("") ) {                            //    검색어가 있으면
            	 v_searchtext = v_searchtext.replaceAll("%", "/%").toLowerCase();
            	 sql += " and (lower(a.subj) like " + StringManager.makeSQL("%" + v_searchtext + "%")  ;
            	 sql += "       or lower(a.subjnm) like " + StringManager.makeSQL("%" + v_searchtext + "%") + " ) ";
            	 /*
                 if ( v_search.equals("subj") ) {                          //    코드로 검색할때
                     sql += " where lower(subj) like " + StringManager.makeSQL("%" + v_searchtext + "%") + " escape '/' ";
                 } else if ( v_search.equals("subjnm") ) {                 //    명칭으로 검색할때
                     sql += " where lower(subjnm) like " + StringManager.makeSQL("%" + v_searchtext + "%") + " escape '/' ";
                 }*/
             }
             sql += "   order by b.year desc, a.subj asc     ";
//System.out.println("==강사===" +sql);
             ls = connMgr.executeQuery(sql);

             ls.setPageSize(10);                         //  페이지당 row 갯수를 세팅한다
             ls.setCurrentPage(v_pageno);                //     현재페이지번호를 세팅한다.
             int total_page_count = ls.getTotalPage();   //     전체 페이지 수를 반환한다
             int total_row_count = ls.getTotalCount();   //     전체 row 수를 반환한다

             while ( ls.next() ) { 
                 data = new SearchData();

                 data.setCode( ls.getString("subj") );
                 data.setYear(ls.getString("year"));
                 data.setSubjseq(ls.getString("subjseq"));
                 data.setCodenm( ls.getString("subjnm") );
                 data.setIsonoff( ls.getString("isonoff") );
                 data.setTmp1( ls.getString("isonoff_value") );                 
                 data.setDispnum(total_row_count - ls.getRowNum() + 1);
                 data.setTotalpagecount(total_page_count);

                 list.add(data);
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
     * 부서 검색
     * @param box          receive from the form object and session
     * @return ArrayList   부서 검색 리스트
     * @throws Exception
     */
    public ArrayList searchDept(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        SearchData data = null;

        String v_search     = box.getString("p_gubun");
        String v_searchtext = box.getString("p_key1");
        int v_pageno = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select comp, compnm, comptype, groupsnm, companynm, gpmnm, deptnm, partnm ";
            sql += "from TZ_COMP                                                               ";
            sql += " where comptype = 4                                                        ";
            sql += "   and isUsed   = 'Y'                                                      ";

            if ( !v_searchtext.equals("") ) {                            //    검색어가 있으면
                if ( v_search.equals("comp") ) {                          //    코드로 검색할때
                    sql += " and comp like   " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("deptnm") ) {                 //    명칭으로 검색할때
                    sql += " and deptnm like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
            }
            sql += "   order by comp asc     ";

            ls = connMgr.executeQuery(sql);

            ls.setPageSize(10);                         //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                //     현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();   //     전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();   //     전체 row 수를 반환한다

            while ( ls.next() ) { 
                data = new SearchData();

                data.setCode( ls.getString("comp") );
                data.setCodenm( ls.getString("compnm") );
                data.setTmp1( ls.getString("comptype") );
                data.setTmp2( ls.getString("groupsnm") );
                data.setTmp3( ls.getString("companynm") );
                data.setTmp4( ls.getString("gpmnm") );
                data.setTmp5( ls.getString("deptnm") );
                data.setTmp6( ls.getString("partnm") );
                data.setDispnum(total_row_count - ls.getRowNum() + 1);
                data.setTotalpagecount(total_page_count);

                list.add(data);
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
     * 회사 검색
     * @param box          receive from the form object and session
     * @return ArrayList   부서 검색 리스트
     * @throws Exception
     */
    public ArrayList searchGrpComp(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        SearchData data = null;

        String v_search     = box.getString("p_gubun");
        String v_searchtext = box.getString("p_key1");
        String v_key2       = box.getString("p_key2");
		String v_defaultcomp = "";
        int v_pageno = box.getInt("p_pageno");
        
        String v_comp = box.getString("p_comp");

		ConfigSet conf = new ConfigSet();  
		v_defaultcomp = conf.getProperty("default.compcd"); // 소유회사일 경우 항상 나오게끔

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            if ( v_key2.equals("p_producer") ) { // 제공회사 - 베타업체
			    sql  = "\n select cpseq as comp ";
			    sql += "\n      , cpnm  as compnm ";
			    sql += "\n      , '2'   as comptype ";
			    sql += "\n      , ''    as groupsnm ";
			    sql += "\n      , cpnm  as companynm ";
			    sql += "\n      , ''    as gpmnm ";
			    sql += "\n      , ''    as deptnm ";
			    sql += "\n      , ''    as partnm ";
			    sql += "\n      , '1'   as seq ";
                sql += "\n from   tz_cpinfo ";
                sql += "\n where  1=1 ";
                sql += "\n and    (compgubun = 'T' or compgubun = 'M')";
                
                if ( !v_searchtext.equals("") ) {	//    검색어가 있으면
                    if ( v_search.equals("") ) {	//    코드로 검색할때
                        sql += "\n and    cpbirth_date like " + StringManager.makeSQL("%" + v_searchtext + "%");
                    } else if ( v_search.equals("companynm") ) {  //    명칭으로 검색할때
                        sql += "\n and    upper(cpnm) like upper(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                    }
                }
                /*
				sql += "\n union  all ";
				sql += "\n select comp   as comp ";
				sql += "\n      , compnm as compnm ";
				sql += "\n      , ''     as comptype ";
				sql += "\n      , ''     as groupsnm ";
				sql += "\n      , ''     as companynm ";
				sql += "\n      , ''     as gpmnm ";
				sql += "\n      , ''     as deptnm ";
				sql += "\n      , ''     as partnm ";
				sql += "\n      , '0'    as seq ";
                sql += "\n from   tz_compclass ";
                sql += "\n where  isdeleted = 'N' ";
                sql += "\n and    comp   = '" +v_ktcomp + "' ";
                if ( !v_searchtext.equals("") ) {	//    검색어가 있으면
                    if ( v_search.equals("companynm") ) {	//    명칭으로 검색할때
                        sql += "\n and    upper(compnm) like upper(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                    }
                }
                */
                sql += "\n order  by seq, compnm asc ";

            } else if ( v_key2.equals("p_owner") ) { // 소유회사 - TZ_CPINFO
                sql  = "\n select cpseq as comp ";
                sql += "\n      , cpnm  as compnm ";
                sql += "\n      , '2'   as comptype ";
                sql += "\n      , ''    as groupsnm ";
                sql += "\n      , cpnm  as companynm ";
                sql += "\n      , ''    as gpmnm ";
                sql += "\n      , ''    as deptnm ";
                sql += "\n      , ''    as partnm ";
                sql += "\n      , '1'   as seq ";
                sql += "\n from   tz_cpinfo ";
                sql += "\n where  1=1 ";
                sql += "\n and    (compgubun = 'S' or compgubun = 'M')";
                
                if ( !v_searchtext.equals("") ) {	//    검색어가 있으면
                    if ( v_search.equals("") ) {	//    코드로 검색할때
                        sql += "\n and    cpbirth_date like   " + StringManager.makeSQL("%" + v_searchtext + "%");
                    } else if ( v_search.equals("companynm") ) {	//    명칭으로 검색할때
                        sql += "\n and    upper(cpnm) like upper(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                    }
                }
				sql += "\n union  all ";
				sql += "\n select comp   as comp ";
				sql += "\n      , compnm as compnm ";
				sql += "\n      , ''     as comptype ";
				sql += "\n      , ''     as groupsnm ";
				sql += "\n      , ''     as companynm ";
				sql += "\n      , ''     as gpmnm ";
				sql += "\n      , ''     as deptnm ";
				sql += "\n      , ''     as partnm ";
				sql += "\n      , '0'    as seq ";
                sql += "\n from   tz_compclass  ";
                sql += "\n where  isdeleted = 'N'  ";
                sql += "\n and    comp   = '" +v_defaultcomp + "' ";
				if ( !v_searchtext.equals("") ) {	//    검색어가 있으면
                    if ( v_search.equals("companynm") ) {	//    명칭으로 검색할때
                        sql += "\n and    upper(compnm) like upper(" + StringManager.makeSQL("%" + v_searchtext + "%") + ") ";
                    }
                }
                sql += "\n order  by seq, compnm asc ";
                
            } else if ( v_key2.equals("p_cpinfo") || v_key2.equals("p_cp") ) { // 외주업체검색
                sql  = "\n select cpseq as comp ";
                sql += "\n      , cpnm  as compnm ";
                sql += "\n      , 2     as comptype ";
                sql += "\n      , ''    as groupsnm ";
                sql += "\n      , cpnm  as companynm ";
                sql += "\n      , ''    as gpmnm ";
                sql += "\n      , ''    as deptnm ";
                sql += "\n      , ''    as partnm ";
                sql += "\n from   tz_cpinfo ";
                sql += "\n where  1=1 ";
                sql += "\n and    (compgubun = 'S' or compgubun = 'M')";
                
                if ( !v_searchtext.equals("") ) {	//    검색어가 있으면
                    if ( v_search.equals("") ) {	//    코드로 검색할때
                        sql += "\n and    cpbirth_date like   " + StringManager.makeSQL("%" + v_searchtext + "%");
                    } else if ( v_search.equals("companynm") ) {	//    명칭으로 검색할때
                        sql += "\n and    upper(cpnm) like upper(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                    }
                }
                sql += "\n order  by cpnm asc     ";
                
            } else if ( v_key2.equals("p_bpinfo") ) { // 베타업체검색
                sql  = "\n select cpseq as comp ";
                sql += "\n      , cpnm  as compnm ";
                sql += "\n      , 2     as comptype ";
                sql += "\n      , ''    as groupsnm ";
                sql += "\n      , cpnm  as companynm ";
                sql += "\n      , ''    as gpmnm ";
                sql += "\n      , ''    as deptnm ";
                sql += "\n      , ''    as partnm ";
                sql += "\n from   tz_cpinfo ";
                sql += "\n where  1=1 ";
                sql += "\n and    (compgubun = 'T' or compgubun = 'M') ";
                
                if ( !v_searchtext.equals("") ) {                            //    검색어가 있으면
                    if ( v_search.equals("") ) {                          //    코드로 검색할때
                        sql += "\n and    cpbirth_date like   " + StringManager.makeSQL("%" + v_searchtext + "%");
                    } else if ( v_search.equals("companynm") ) {                 //    명칭으로 검색할때
                        sql += "\n and    upper(cpnm) like upper(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                    }
                }
                sql += "\n order  by cpnm asc     ";

            } else if ( v_key2.equals("p_cpbpinfo") ) { // 외주업체검색
                sql  = "\n select cpseq as comp ";
                sql += "\n      , cpnm  as compnm ";
                sql += "\n      , 2     as comptype ";
                sql += "\n      , ''    as groupsnm ";
                sql += "\n      , cpnm  as companynm ";
                sql += "\n      , ''    as gpmnm ";
                sql += "\n      , ''    as deptnm ";
                sql += "\n      , ''    as partnm ";
                sql += "\n from   tz_cpinfo ";
                sql += "\n where  1=1 ";
                sql += "\n and    compgubun = 'M' ";

                if ( !v_searchtext.equals("") ) {                            //    검색어가 있으면
                    if ( v_search.equals("") ) {                          //    코드로 검색할때
                        sql += "\n and    cpbirth_date like   " + StringManager.makeSQL("%" + v_searchtext + "%");
                    } else if ( v_search.equals("companynm") ) {                 //    명칭으로 검색할때
                        sql += "\n and    upper(cpnm) like upper(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                    }
                }
                sql += "\n order  by cpnm asc     ";
                
            } else if ( v_key2.equals("p_cpasinfo") ) { // 컨텐츠유지보수업체
                sql  = "\n select cpseq as comp ";
                sql += "\n      , cpnm  as compnm ";
                sql += "\n      , ''    as comptype ";
                sql += "\n      , ''    as groupsnm ";
                sql += "\n      , cpnm  as companynm ";
                sql += "\n      , ''    as gpmnm ";
                sql += "\n      , ''    as deptnm ";
                sql += "\n      , ''    as partnm ";
                sql += "\n from   tz_cpinfo ";
                sql += "\n where  1=1 ";
                
                if ( !v_searchtext.equals("") ) {	//    검색어가 있으면
                    if ( v_search.equals("") ) {	//    코드로 검색할때
                        sql += "\n and    cpbirth_date like   " + StringManager.makeSQL("%" + v_searchtext + "%");
                    } else if ( v_search.equals("companynm") ) {	//    명칭으로 검색할때
                        sql += "\n and    upper(cpnm) like upper(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                    }
                }
                sql += "\n order  by cpnm asc     ";
                
            } else { 
                sql  = "\n select comp   as comp ";
                sql += "\n      , compnm as compnm ";
                sql += "\n      , ''     as comptype ";
                sql += "\n      , ''     as groupsnm ";
                sql += "\n      , ''     as companynm ";
                sql += "\n      , ''     as gpmnm ";
                sql += "\n      , ''     as deptnm ";
                sql += "\n      , ''     as partnm ";
                sql += "\n from   tz_compclass ";
                sql += "\n where  isdeleted = 'N' ";

                if ( !v_searchtext.equals("") ) {                            //    검색어가 있으면
                    if ( v_search.equals("comp") ) {                          //    코드로 검색할때
                        sql += "\n and    comp like   " + StringManager.makeSQL("%" + v_searchtext + "%");
                    } else if ( v_search.equals("companynm") ) {                 //    명칭으로 검색할때
                        sql += "\n and    upper(compnm) like upper(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                    }
                }
                
                if(!(box.getString("p_compgubun").equals("") || box.getString("p_compgubun").equals("ALL"))) {
                	sql += "\n and    gubun = " + StringManager.makeSQL(box.getString("p_compgubun"));
                }
                sql += "\n order  by comp asc     ";
            }

            ls = connMgr.executeQuery(sql);
            int total_page_count = 0;
            int total_row_count = 0;
            if(v_key2.equals("p_comp") || v_key2.equals("p_cp") || v_key2.equals("p_producer") || v_key2.equals("p_owner") || v_key2.equals("p_cpinfo") || v_key2.equals("p_bpinfo") || v_key2.equals("p_cpbpinfo")) {
	            ls.setPageSize(10);                         //  페이지당 row 갯수를 세팅한다
	            ls.setCurrentPage(v_pageno);                //     현재페이지번호를 세팅한다.
	            total_page_count = ls.getTotalPage();   //     전체 페이지 수를 반환한다
	            total_row_count = ls.getTotalCount();   //     전체 row 수를 반환한다
            }

            while ( ls.next() ) { 
                data = new SearchData();

                data.setCode( ls.getString("comp") );
                data.setCodenm( ls.getString("compnm") );
                data.setTmp1( ls.getString("comptype") );
                data.setTmp2( ls.getString("groupsnm") );
                data.setTmp3( ls.getString("companynm") );
                data.setTmp4( ls.getString("gpmnm") );
                data.setTmp5( ls.getString("deptnm") );
                data.setTmp6( ls.getString("partnm") );
                if(v_key2.equals("p_comp") || v_key2.equals("p_cp") || v_key2.equals("p_producer") || v_key2.equals("p_owner") || v_key2.equals("p_cpinfo") || v_key2.equals("p_bpinfo") || v_key2.equals("p_cpbpinfo")) {
	                data.setDispnum(total_row_count - ls.getRowNum() + 1);
	                data.setTotalpagecount(total_page_count);
                }

                list.add(data);
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
    * 회원조회 리스트
    * @param box          receive from the form object and session
    * @return ArrayList   회원 리스트
    * @throws Exception
    */
    public ArrayList searchMember(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        SearchData data = null;

        String v_search     = box.getString("p_gubun");
        String v_searchtext = box.getString("p_key1");
        int v_pageno = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "select a.userid AS userid                                  \n" +
                   "     , fn_crypt('2', a.birth_date, 'knise') birth_date                                   \n" +
                   "     , a.name                                    \n" +
                   "     , a.email                                   \n" +
                   "     , b.compnm                                  \n" +
                   "     , b.telno			                         \n" +
                   "     , a.hometel                                 \n" +
                   "     , a.handphone as comptel                    \n" +
                   "     , a.position_nm                             \n" +
                   "     , a.lvl_nm                                  \n" +                   
                   "from   tz_member a                               \n" +
                   "     , tz_compclass b                            \n" +
                   "where  a.comp = b.comp(+)                        \n";
            
            if ( !v_searchtext.equals("") ) {                            //    검색어가 있으면
                if ( v_search.equals("userid") ) {                        //    ID로 검색할때
                    sql += "and    a.userid like '%" + v_searchtext+"%' ";
                } else if ( v_search.equals("name") ) {                   //    이름으로 검색할때
                    sql += "and    a.name like '%" + v_searchtext+"%' ";
                }

            } else { 
                sql +="and    a.userid = 'notselect'				\n";
            }
            
            sql += "order  by a.comp asc                           \n";

            ls = connMgr.executeQuery(sql);

            ls.setPageSize(10);                         //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                //     현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();   //     전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();   //     전체 row 수를 반환한다

            while ( ls.next() ) { 
                data = new SearchData();

                data.setCode( ls.getString("userid") );
                data.setCodenm( ls.getString("name") );
                data.setTmp1( ls.getString("birth_date") );
                data.setTmp2( ls.getString("email") );
                data.setTmp6( ls.getString("compnm") );
                data.setTmp8( ls.getString("comptel") );
                data.setTmp9( ls.getString("position_nm") );
                data.setTmp10( ls.getString("lvl_nm") );                
                data.setDispnum(total_row_count - ls.getRowNum() + 1);
                data.setTotalpagecount(total_page_count);

                list.add(data);
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
    * 강사조회 리스트
    * @param box          receive from the form object and session
    * @return ArrayList   회원 리스트
    * @throws Exception
    */
    public ArrayList searchTutor(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = null;
        DataBox             dbox            = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println을 Mehtod에서 사용하는 순서를 표시하는 변수 
        
        String              v_search        = box.getString("p_gubun"   );
        String              v_searchtext    = box.getString("p_key1"    );
        int                 v_pageno        = box.getInt   ("p_pageno"  );
        
        int                 total_page_count= 0;    // 전체 페이지 수를 반환한다
        int                 total_row_count = 0;    // 전체 row 수를 반환한다
        
        try { 
            connMgr = new DBConnectionManager();

            list    = new ArrayList();
            
            sbSQL.append(" select  a.userid                                                                         \n")
                 .append("     ,   a.name                                                                           \n")
                 .append("     ,   a.email                                                                          \n")
                 .append("     ,   a.comp                                                                           \n")
                 .append("     ,   e.codenm                                                                         \n")
                 .append("     ,   DECODE(a.ismanager, 'N', 'N'                                                     \n")
                 .append("                           , 'Y '                                                         \n")
                 .append("                             || '('                                                       \n")
                 .append("                             || TO_CHAR(TO_DATE(d.fmon, 'YYYYMMDD'), 'YYYY.MM.DD')        \n")
                 .append("                             || ' - '                                                     \n")
                 .append("                             || TO_CHAR(TO_DATE(d.tmon, 'YYYYMMDD'), 'YYYY.MM.DD')        \n")
                 .append("                             || ')'                                                       \n")
                 .append("               )                                 authname                                 \n")
                 .append("     ,   NVL(a.handphone, a.phone)               phone                                    \n")
                 .append(" from    tz_tutor    a                                                                    \n")       
                 .append("       , tz_member   c                                                                    \n")       
                 .append("       , tz_manager  d                                                                    \n")       
                 .append("       , tz_code     e                                                                    \n")       
                 .append(" where   A.userid        = C.userid                                                       \n")      
                 .append(" and     A.userid        = D.userid(+)                                                    \n")     
                 .append(" and     A.tutorgubun    = E.code                                                         \n")      
                 .append(" and     D.GADMIN(+)     = 'P1'                                                           \n")       
                 .append(" and     E.gubun         = '0060'                                                         \n");
                 
            if ( v_search.equals("userid") )    // ID로 검색할때
                sbSQL.append(" and  c.userid like " + StringManager.makeSQL("%" + v_searchtext + "%") + "           \n");
            else if ( v_search.equals("name") ) // 이름으로 검색할때
                sbSQL.append(" and  c.name   like " + StringManager.makeSQL("%" + v_searchtext + "%") + "           \n");
                 
            sbSQL.append(" order by c.name                                                                          \n");
                        
            ls  = connMgr.executeQuery(sbSQL.toString());
            
            ls.setPageSize(10);                             // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                    // 현재페이지번호를 세팅한다.
            total_page_count    = ls.getTotalPage();    // 전체 페이지 수를 반환한다
            total_row_count     = ls.getTotalCount();   // 전체 row 수를 반환한다
            
            while ( ls.next() ) { 
                dbox    = ls.getDataBox();
                
                dbox.put("d_dispnum"            , String.valueOf(total_row_count - ls.getRowNum() + 1));
                dbox.put("d_total_page_count"   , String.valueOf(total_page_count                    ));

                list.add(dbox);
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


    /**
    * 결재상신대상 회원조회 리스트
    * @param box          receive from the form object and session
    * @return ArrayList   회원 리스트
    * @throws Exception
    */
    public ArrayList searchAppMember(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        SearchData data = null;

        String v_search     = box.getString("p_gubun");
        String v_searchtext = box.getString("p_searchtext");
        int v_pageno   = box.getInt("p_pageno");
        String v_comp  = box.getSession("comp");
        String v_jikwi = box.getSession("jikwi");
        

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "  select userid, fn_crypt('2', birth_date, 'knise') birth_date, name, jikwi, get_jikwinm(jikwi,comp) jikwinm,  ";
            sql += "         email, cono, comp, get_compnm(comp,2,2) compnm                ";
            sql += "    from TZ_MEMBER                                                     ";
            sql += "  where                                                                ";
            sql += "  office_gbn = 'Y'                                                     ";
            sql += "  and jikwi  <= '" +v_jikwi + "' ";
            // sql += "  and deptcod = (select deptcod from tz_member where userid = '" +box.getSession("userid") + "')\n";
            sql += "  and ( (divinam||deptnam) = (select (divinam||deptnam) from tz_member where userid = '" +box.getSession("userid") + "') or \n";
            sql += "  ( select deptmbirth_date from tz_comp where tz_comp.comp = '" +box.getSession("comp") + "' ) = TZ_MEMBER.userid  or ( deptcod = (select deptcod from tz_member where userid = '" +box.getSession("userid") + "') ) )\n";
            // sql += "  and comp    = '" +v_comp + "' ";
            sql += "  and userid != '" +box.getSession("userid") + "' ";

            if ( !v_searchtext.equals("") ) {                            //    검색어가 있으면
                if ( v_search.equals("userid") ) {                        //    ID로 검색할때
                    sql += "  and userid like   " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("cono") ) {                   //    사번으로 검색할때
                    sql += "  and cono like " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("name") ) {                   //    이름으로 검색할때
                    sql += " and name like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
            }

            sql += "   order by comp asc, jikwi asc                                         ";


            ls = connMgr.executeQuery(sql);

            ls.setPageSize(10);                         //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                //     현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();   //     전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();   //     전체 row 수를 반환한다

            while ( ls.next() ) { 
                data = new SearchData();

                data.setCode( ls.getString("userid") );
                data.setCodenm( ls.getString("name") );
                data.setTmp1( ls.getString("birth_date") );
                data.setTmp2( ls.getString("email") );
                data.setTmp3( ls.getString("cono") );
                data.setTmp4( ls.getString("jikwi") );
                data.setTmp5( ls.getString("jikwinm") );
                data.setTmp6( ls.getString("comp") );
                data.setTmp7( ls.getString("compnm") );
                data.setDispnum(total_row_count - ls.getRowNum() + 1);
                data.setTotalpagecount(total_page_count);

                list.add(data);
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
     * 컨텐츠 제공업체/담당자 검색
     * @param box          receive from the form object and session
     * @return ArrayList   베타테스트 컨텐츠 제공업체/담당자 검색 리스트
     * @throws Exception
     */
    public ArrayList searchBetaCompany(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        SearchData data = null;

        String v_search     = box.getString("p_gubun");
        String v_searchtext = box.getString("p_key1");
        int v_pageno = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select userid, usernm, betacpno, betacpnm  ";
            sql += "from TZ_BETACPINFO                          ";

            if ( !v_searchtext.equals("") ) {                                 //    검색어가 있으면
                if ( v_search.equals("companyno") ) {                          //   사업자등록번호로 검색할때
                    sql += " where betacpno like   " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("companyname") ) {                 //    명칭으로 검색할때
                    sql += " where betacpnm like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
            }
            sql += "   order by betacpno asc     ";

            ls = connMgr.executeQuery(sql);

            ls.setPageSize(10);                         //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                //  현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();   //  전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();   //  전체 row 수를 반환한다

            while ( ls.next() ) { 
                data = new SearchData();

                data.setUserid( ls.getString("userid") );
                data.setUsernm( ls.getString("usernm") );
                data.setCode( ls.getString("betacpno") );
                data.setCodenm( ls.getString("betacpnm") );
                data.setDispnum(total_row_count - ls.getRowNum() + 1);
                data.setTotalpagecount(total_page_count);

                list.add(data);
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
     * 교육주관처 검색
     * @param box          receive from the form object and session
     * @return ArrayList   교육주관처 검색 리스트
     * @throws Exception
     */
    public ArrayList searchCompany(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        SearchData data = null;

        String v_search     = box.getString("p_gubun");
        String v_searchtext = box.getString("p_key1");
        int v_pageno = box.getInt("p_pageno");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select companyno, companyname, telephone, musername  ";
            sql += "from TZ_CONSIGNCOM                                    ";

            if ( !v_searchtext.equals("") ) {                                 //    검색어가 있으면
                if ( v_search.equals("companyno") ) {                          //   사업자등록번호로 검색할때
                    sql += " where companyno like   " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("companyname") ) {                 //    명칭으로 검색할때
                    sql += " where companyname like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
            }
            sql += "   order by companyno asc     ";

            ls = connMgr.executeQuery(sql);

            ls.setPageSize(10);                         //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                //  현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();   //  전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();   //  전체 row 수를 반환한다

            while ( ls.next() ) { 
                data = new SearchData();

                data.setCode( ls.getString("companyno") );
                data.setCodenm( ls.getString("companyname") );
                data.setTmp1( ls.getString("telephone") );
                data.setTmp2( ls.getString("musername") );
                data.setDispnum(total_row_count - ls.getRowNum() + 1);
                data.setTotalpagecount(total_page_count);

                list.add(data);
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
   * 개인정보조회
    @param box      receive from the form object and session
    @return MemberData
   */
    public MemberData selectPersonalInformation(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        MemberData data = null;
        ListSet             ls      = null;
        String              sql     = "";

        String v_userid     = box.getString("p_userid");
        try { 
            connMgr = new DBConnectionManager();

            sql  = "select name , fn_crypt('2', pwd, 'knise') pwd, (SELECT CODENM FROM TZ_CODE WHERE GUBUN = '0115' AND CODE = A.JOB_CD) AS LIC, DECODE(EMP_GUBUN,'T','교원/보조원','E','보조원','학부모') AS GB, DECODE(EMP_GUBUN,'T',USER_PATH,'E',USER_PATH,'학부모') AS SNM                                                 \n" +
                   "     , email , zip_cd, address, zip_cd1, address1, DECODE(hrdc,'C','직장(학교)','자택') AS RV                                                \n" +
                   "     , hometel                                               \n" +
                   "     , handphone                                             \n" +
                   "     , get_postnm(post) as jikupnm                           \n" +
                   "     , get_compnm(comp) as compnm	                         \n" +
                   "     , position_nm as deptnm		 		                 \n" +
                   "from   tz_member  A                                           \n" +
                   "where  userid = " +SQLString.Format(v_userid)+ "             \n";

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                data = new MemberData();
                data.setName     (ls.getString("name"));
                data.setEmail    (ls.getString("email"));
				data.setHometel  (ls.getString("hometel"));
				data.setHandphone(ls.getString("handphone"));
				data.setJikupnm  (ls.getString("jikupnm"));
				data.setCompnm   (ls.getString("compnm"));
				data.setDeptnm   (ls.getString("deptnm"));
				data.setPwd      (ls.getString("pwd"));
				data.setLic      (ls.getString("lic"));
				data.setGb      (ls.getString("gb"));
				data.setSnm      (ls.getString("snm"));
				
				data.setPost1      (ls.getString("zip_cd"));
				data.setAddr      (ls.getString("address"));
				data.setPost2      (ls.getString("zip_cd1"));
				data.setAddr2      (ls.getString("address1"));
				data.setRv      (ls.getString("rv"));
				
				data.setUserid   (v_userid);
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
        return data;
    }


   /**
   * 협력업체 개인정보조회
    @param box      receive from the form object and session
    @return MemberData
   */
    public DataBox selectOutPersonalInformation(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        Connection conn = null;
        DataBox             dbox    = null;
        ListSet             ls      = null;
        String              sql     = "";

        String v_userid     = box.getString("p_userid");
        try { 
            connMgr = new DBConnectionManager();
            sql  = "select birth_date, name, email, comptel, hometel, cono, pwd ";
            sql += "from TZ_OUTMEMBER ";
            sql += "where userid = " +SQLString.Format(v_userid);

            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) { 
                dbox = ls.getDataBox();
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
        return dbox;
    }
    
    /**
     * 거점 검색
     * @param box          receive from the form object and session
     * @return ArrayList   거점 검색 리스트
     * @throws Exception
     */
    public ArrayList searchBranch(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        SearchData data = null;

        String v_search     = box.getString("p_gubun");
        String v_searchtext = box.getString("p_key1");
        String v_key2       = box.getString("p_key2");
        String v_autoevercomp = "";
        int v_pageno = box.getInt("p_pageno");

        ConfigSet conf = new ConfigSet();  
        v_autoevercomp = conf.getProperty("autoever.compcd");        //        이 모듈의 페이지당 row 수를 셋팅한다

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql = "select branchcode, branchnm                  \n"
                + "from tz_branch                               \n"
                + "where 1=1                                    \n";
                
            if ( !v_searchtext.equals("") ) {                            //    검색어가 있으면
                if ( v_search.equals("") ) {                          //    코드로 검색할때
                    sql += " and branchcode like   " + StringManager.makeSQL("%" + v_searchtext + "%");
                } else if ( v_search.equals("branchnm") ) {                 //    명칭으로 검색할때
                    sql += " and upper(branchnm) like upper(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                }
            }
            
            ls = connMgr.executeQuery(sql);

            ls.setPageSize(10);                         //  페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);                //     현재페이지번호를 세팅한다.
            int total_page_count = ls.getTotalPage();   //     전체 페이지 수를 반환한다
            int total_row_count = ls.getTotalCount();   //     전체 row 수를 반환한다

            while ( ls.next() ) { 
                data = new SearchData();

                data.setCode( ls.getString("branchcode") );
                data.setCodenm( ls.getString("branchnm") );
                data.setDispnum(total_row_count - ls.getRowNum() + 1);
                data.setTotalpagecount(total_page_count);

                list.add(data);
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
     * 회사 검색3
     * @param box          receive from the form object and session
     * @return ArrayList   부서 검색 리스트
     * @throws Exception
     * 2008.10.06 오충현 수정 
     * SelectBox 추가.
     */
    public ArrayList searchCompList3(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
		DataBox             dbox    = null;
		
		String v_gadmin = box.getSession("gadmin");
		String v_userid = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();
            
            /*if(v_gadmin.equals("A1") || v_gadmin.equals("A2")) {
	            sql  = " select a.comp, a.compnm, a.comptype, a.groupsnm, a.companynm                \n";
	            sql += "      , a.gpmnm, a.deptnm, a.partnm, b.alias                                 \n";
	            sql += "from TZ_COMP a, TZ_COMPCLASS b                                               \n";
	            sql += " where 1=1--a.comptype = 2                                                   \n";
	            sql += "   and a.comp = b.comp                                                       \n";
	            sql += "   and a.isUsed   = 'Y'                                                      \n";
	            sql += "   order by a.orga_sort, a.comp asc     									 \n";
            } else {
                sql  = "SELECT A.COMP  \n";
                sql += "     , A.COMPNM  \n";
                sql += "     , A.COMPANYNM  \n";
                sql += "FROM   TZ_COMP A  \n";
                sql += "     , TZ_GRCOMP B  \n";
                sql += "WHERE  A.COMP = B.COMP  \n";
                sql += "AND    A.ISUSED = 'Y'  \n";
                sql += "AND    B.GRCODE IN (SELECT GRCODE FROM TZ_GRCODEMAN WHERE USERID = " + SQLString.Format(v_userid) + " AND GADMIN = 'H1')  \n";
                sql += "order by a.orga_sort, a.comp asc  \n";
            }*/
            //if(v_gadmin.equals("A1") || v_gadmin.equals("A2")) {
            	sql  = " select comp,  compnm, gubun, isdeleted, show_yn,  coname             	\n";
            	sql += " from TZ_COMPCLASS             											\n";
            	sql += " where show_yn = 'Y'                									\n";
            	sql += " and isdeleted = 'N'                									\n";
            	sql += " order by orga_sort				  									\n";
            //} else {
            	/*
            	 * 나머지 권한은 아직 정해지지 않았음 나중으로....
            	 * 2008.10.06 ChungHyun 
            	 */
            //}
            ls = connMgr.executeQuery(sql);
            
			while ( ls.next() ) {
				dbox = ls.getDataBox();
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
     * 직급 리스트
     * @param box
     * @return
     * @throws Exception
     */
    public ArrayList selectPostList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
		DataBox             dbox    = null;
		

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();
            
        	sql = " select class_cd, class_nm "
        		+ " from   tk_hap009t "
        		+ " where  close_dt = '        ' "
        	    + " order  by class_cd ";
            ls = connMgr.executeQuery(sql);
            
			while ( ls.next() ) {
				dbox = ls.getDataBox();
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
     * 개인별 접속 domain검색
     * @param box
     * @return string
     * @throws Exception
     */
    public ArrayList selectDomainForGrcode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        ArrayList 			list    = new ArrayList();
        DataBox             dbox    = null;
		
		String v_userid = box.getString("p_userid"); 

        try { 
            connMgr = new DBConnectionManager();

            sql = "SELECT C.DOMAIN, C.GRCODE, D.TYPE							";
            sql += "FROM TZ_MEMBER A, TZ_GRCOMP B, TZ_GRCODE C, TZ_TEMPLET D	";
            sql += "WHERE A.COMP = B.COMP										";
            sql += "  AND B.GRCODE = C.GRCODE									";
            sql += "  AND C.GRCODE = D.GRCODE									";
            sql += "  AND A.USERID = " + StringManager.makeSQL(v_userid);
            sql += "  AND C.DOMAIN IS NOT NULL									";

            ls = connMgr.executeQuery(sql);
            
			while ( ls.next() ) {
				dbox = (DataBox)ls.getDataBox();
				list.add(dbox);
			}
			ls.close();
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
    
}
