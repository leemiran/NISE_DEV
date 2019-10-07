// **********************************************************
//  1. 제      목: 과목별 공지사항 BEAN
//  2. 프로그램명:  SubjGongAdminBean.java
//  3. 개      요: 과목별 공지사항 BEAN
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 노희성 2004. 11. 11
//  7. 수      정:
// **********************************************************
package com.ziaan.course;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.common.GetCodenm;
import com.ziaan.lcms.EduAuthBean;
import com.ziaan.library.CalcUtil;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.FreeMailBean;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.system.CodeConfigBean;
import com.ziaan.system.CodeData;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class SubjGongAdminBean { 
	private int row;
	
	public SubjGongAdminBean() { 
        try { 
            row = 5;        // 이 모듈의 페이지당 row 수를 셋팅한다
        }
        catch( Exception e ) { 
            e.printStackTrace();
        }
    }
	
    /**
    과목별 공지사항 현황  리스트
    @param box          receive from the form object and session
    @return ArrayList   과목별 공지사항 현황
    */
     public ArrayList selectListGongAll(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;
        ArrayList list1     = null;
        ArrayList list2     = null;
        SubjGongData data1  = null;
        SubjGongData data2  = null;
        String sql1         = "";
        String sql2         = "";

        String  v_Bcourse    = ""; // 이전코스
        String  v_course     = ""; // 현재코스
        String  v_Bcourseseq = ""; // 이전코스기수
        String  v_courseseq  = ""; // 현재코스기수

		String s_userid = box.getSession("userid");
        String s_gadmin = box.getSession("gadmin");
        String v_gadmin = StringManager.substring(s_gadmin, 0, 1);

        String  ss_grcode    = box.getStringDefault("s_grcode","ALL");        // 교육그룹
        String  ss_gyear     = box.getStringDefault("s_gyear","ALL");         // 년도
        String  ss_grseq     = box.getStringDefault("s_grseq","ALL");         // 교육기수
        String  ss_uclass    = box.getStringDefault("s_upperclass","ALL");    // 과목분류(대)
        String  ss_mclass    = box.getStringDefault("s_middleclass","ALL");   // 과목분류(중)
        String  ss_lclass    = box.getStringDefault("s_lowerclass","ALL");    // 과목분류(소)
        String  ss_subjcourse= box.getStringDefault("s_subjcourse","ALL");    // 과목&코스
        String  ss_subjseq   = box.getStringDefault("s_subjseq","ALL");       // 과목 기수
        String  ss_isclosed  = box.getStringDefault("s_isclosed","ALL");      // 교육진행여부

        String  v_orderColumn   = box.getString("p_orderColumn");               // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서

        String sql_add1 = "";                                         // 권한종류 관련 추가 쿼리(권한코드)
        String sql_add2 = "";                                         // 권한종류 관련 추가 쿼리(해당권한(읽기/쓰기))
        ArrayList list3 = (ArrayList)CodeConfigBean.getCodeList("noticeCategory","",1);    // 카테고리종류리스트
        CodeData data3 = null;
        String v_code    = "";
        int    v_codecnt = list3.size();

        try { 

            //   공지카테고리 종류 관련 쿼리
            for ( int i = 0; i < v_codecnt; i++ ) { 
                data3   = (CodeData)list3.get(i);
                v_code   = data3.getCode();

                // 공지카테고리코드
                if ( i == 0 ) {                                           // 처음 쿼리 연결을 위하여
                    sql_add1 += ", ";
                } else {                                                // 문자간 결합시 사이에 '/' 추가
                    sql_add1 += " || '/' ||  ";
                }

                // 각각의 코드  ID colnum
                sql_add1 += StringManager.makeSQL(v_code) ;
                if ( (i +1) == v_codecnt ) sql_add1 += " as types ";   // 마지막 로우일 경우

                // 카테고리 갯수
                if ( i == 0 ) {                                           // 처음 쿼리 연결을 위하여
                    sql_add2 += ", ";
                } else {                                                // 문자간 결합시 사이에 '/' 추가
                    sql_add2 += " || '/' ||  ";
                }

                // 참고 : 해당값에 위치 지정을 위한 구분값 (맨앞숫자 한자리) 추가해줌 (  i  + 갯수  )
                sql_add2 += " max(decode(c.types, "  + StringManager.makeSQL(v_code) + ", '" + i +
                            "' || (select count(*) from tz_gong where types=" + StringManager.makeSQL(v_code) +
                            " and subj = a.subj and subjseq = a.scsubjseq),'" + i + "')) ";
                if ( (i +1) == v_codecnt ) sql_add2 += " as typescnt ";   // 마지막 로우일 경우
            }


            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            list2 = new ArrayList();
            
            sql1  = "  select a.subj subj,  a.subjnm subjnm,  a.isonoff isonoff , a.subjseq ,c.year ";
            sql1 += sql_add1;
            sql1 += sql_add2;

            sql1 += "   from VZ_SCSUBJSEQ a left outer join TZ_GONG c                                      \n"; // TZ_GRCODE b, 
            sql1 += "   on a.subj  = c.subj   															\n";
            sql1 += "   and a.year  = c.year   															\n";
            sql1 += "	and a.subjseq = c.subjseq       													\n";
            sql1 += "   where 1=1                                                                           \n";


            if ( !ss_grcode.equals("ALL") ) { 
                sql1 += " and a.grcode = " + StringManager.makeSQL(ss_grcode) + "	\n";
            }
            if ( !ss_gyear.equals("ALL") ) { 
                sql1 += " and a.gyear = " + StringManager.makeSQL(ss_gyear) + "		\n";
            }
            if ( !ss_grseq.equals("ALL") ) { 
                //sql1 += " and a.grseq = " + StringManager.makeSQL(ss_grseq) + "		\n";
            }

            if ( !ss_uclass.equals("ALL") ) { 
                sql1 += " and a.scupperclass = " + StringManager.makeSQL(ss_uclass) + "	\n";
            }
            if ( !ss_mclass.equals("ALL") ) { 
                sql1 += " and a.scmiddleclass = " + StringManager.makeSQL(ss_mclass) + "	\n";
            }
            if ( !ss_lclass.equals("ALL") ) { 
                sql1 += " and a.sclowerclass = " + StringManager.makeSQL(ss_lclass) + "		\n";
            }

            if ( !ss_subjcourse.equals("ALL") ) { 
                sql1 += " and a.scsubj = " + StringManager.makeSQL(ss_subjcourse) + "		\n";
            }
            if ( !ss_subjseq.equals("ALL") ) { 
                sql1 += " and a.scsubjseq = " + StringManager.makeSQL(ss_subjseq) + "		\n";
            }
            if ( !ss_isclosed.equals("ALL") ) { 
                sql1 += " and a.isclosed = " + StringManager.makeSQL(ss_isclosed) + "			\n";
            }

            sql1 += " group by a.subj, a.subjnm, a.isonoff  , a.subjseq ,c.year                 ";


            if ( v_orderColumn.equals("") ) { 
                sql1 += " order by a.subj ";
            } else { 
                sql1 += " order by " + v_orderColumn + v_orderType;
            }

   //    System.out.println("-----------------------------공지--------" + sql1);   
            ls1 = connMgr.executeQuery(sql1);

            while ( ls1.next() ) { 
                data1 = new SubjGongData();

                //data1.setGrcode( ls1.getString("grcode") );
                //data1.setGrcodenm( ls1.getString("grcodenm") );
                //data1.setGyear( ls1.getString("gyear") );
                //data1.setGrseq( ls1.getString("grseq") );
                //data1.setCourse( ls1.getString("course") );
                //data1.setCyear( ls1.getString("cyear") );
                //data1.setCourseseq( ls1.getString("courseseq") );
                //data1.setCoursenm( ls1.getString("coursenm") );
                data1.setSubj( ls1.getString("subj") );
                data1.setYear( ls1.getString("year") );
                data1.setSubjseq( ls1.getString("subjseq") );
             //   data1.setSubjseqgr( ls1.getString("subjseqgr") );
                data1.setSubjnm( ls1.getString("subjnm") );
                data1.setIsonoff( ls1.getString("isonoff") );
                //data1.setEdustart( ls1.getString("edustart") );
                //data1.setEduend( ls1.getString("eduend") );

                data1.setTypescnt( ls1.getString("typescnt") );



                list1.add(data1);
            }
            ls1.close();

            
            
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
    과목기수별 공지 화면 리스트
    @param box          receive from the form object and session
    @return ArrayList   과목기수별 공지  리스트
    */
    public ArrayList selectListGong(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls        = null;
        ArrayList list    = null;
        String sql        = "";
        SubjGongData data = null;

        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year"); 
        String v_subjseq = box.getString("p_subjseq");
        String v_admin   = box.getString("p_admin");  // 운영자 페이지

        String v_typesgubun = CodeConfigBean.getConfigValue("noticeCategory");    // 카테고리종류리스트

        String  v_orderColumn   = box.getString("p_orderColumn");               // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");
        
        try { 
            connMgr = new DBConnectionManager();


            list = new ArrayList();

            sql  = " select tg.seq , " +
            	   "        tg.types, " +
            	   "        (select codenm from tz_code where gubun = '0008' and code = tg.types) as typesnm, " +
            	   "        tg.addate, " +
            	   "        tg.title , " +
            	   "        tg.userid " +
            	   "        userid, " +
            	   "        tg.adcontent, " +
            	   "        tm.name, " +
            	   "        tg.cnt, " +
            	   "        decode(tg.gadmin, 'ZZ', 'ZZ', 'P1', '강사',  '운영자') gadmin, tg.isimport \n";
            sql += "   from TZ_GONG   tg                                     \n";
            sql += "   ,    Tz_member tm                                     \n";
            sql += "  where tg.subj    = " + StringManager.makeSQL(v_subj);
            sql += "    and tg.year    in (" + StringManager.makeSQL(v_year)+",'0000')";
            sql += "    and tg.subjseq in (" + StringManager.makeSQL(v_subjseq)+",'0000')";
            sql += "    and tg.userid = tm.userid                          \n";
            if ( !v_searchtext.equals("") ) {                //    검색어가 있으면
                // v_pageno = 1;   //      검색할 경우 첫번째 페이지가 로딩된다

                if ( v_search.equals("name") ) {              //    이름으로 검색할때
                    sql += " and tm.name like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                }
                else if ( v_search.equals("title") ) {        //    제목으로 검색할때
                    sql += " and tg.title like lower(" + StringManager.makeSQL("%" + v_searchtext + "%") + ")";
                }
                else if ( v_search.equals("content") ) {     //    내용으로 검색할때
                    // sql += " and dbms_lob.instr(a.content, " + StringManager.makeSQL(v_searchtext) + ",1,1) < > 0";
                    sql += " and ( tg.content like " + StringManager.makeSQL("%" + v_searchtext + "%") + " or a.content like " + StringManager.makeSQL("%" + v_searchtext) + " or a.content like " + StringManager.makeSQL( v_searchtext + "%") + ")";            //   Oracle 9i 용
                }
                else if ( v_search.equals("ldate") ) {     //    작성일자으로 검색할때
                    sql += " and ( SUBSTR(tg.addate, 1, 8) = " + StringManager.makeSQL( StringManager.replace(v_searchtext, ".", "") ) + " )  \n";            
                }
                else if ( v_search.equals("userid") ) {     //    아이디로 검색할때
                    sql += " and ( tg.userid  = " + StringManager.makeSQL(v_searchtext ) + " ) \n";            
                }
            }
            if ( v_orderColumn.equals("") ) { 
                sql += " order by nvl(tg.isimport,'N') desc, seq desc";
            } else { 
                sql += " order by nvl(tg.isimport,'N') desc, " + v_orderColumn + v_orderType;
            }
//System.out.println("======================공지뷰--" + sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new SubjGongData();

                data.setSeq( ls.getInt("seq") );
                data.setTypes( ls.getString("types") );
                data.setTypesnm( ls.getString("typesnm") );
                data.setAddate( ls.getString("addate") );
                data.setTitle( ls.getString("title") );
                data.setUserid( ls.getString("userid") );
                data.setName( ls.getString("name") );
                data.setCnt( ls.getInt("cnt") );
                data.setAdcontent( ls.getCharacterStream("adcontent") );
                data.setGadmin( ls.getString("gadmin") );
                // data.setAdcontent( ls.getString("adcontent") );

                list.add(data);
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
    과목기수별 전체 공지 화면 리스트
    @param box          receive from the form object and session
    @return ArrayList   과목기수별 공지  리스트
    */
    public ArrayList selectListGongAll_H(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls        = null;
        ArrayList list    = null;
        String sql        = "";
        SubjGongData data = null;

        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");

        String v_typesgubun = CodeConfigBean.getConfigValue("noticeCategory");    // 카테고리종류리스트

        String  v_orderColumn   = box.getString("p_orderColumn");               // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select seq , types, typesnm, addate,title, userid, adcontent,  rownum   ";
            sql += " from   (select a.seq seq , a.types types, b.codenm typesnm, a.addate addate, ";
            sql += "          a.title title, a.userid userid, a.adcontent adcontent         ";
            sql += "          from TZ_GONG a, TZ_CODE b                                          ";
            sql += "          where a.types   = b.code                                            ";
            sql += "            and b.gubun   = " + StringManager.makeSQL(v_typesgubun);
            sql += "            and b.levels  = 1 and a.types = 'H'                               ";
            sql += "            and a.subj    = " + StringManager.makeSQL(v_subj);
            sql += "            and a.year    = " + StringManager.makeSQL(v_year);
            sql += "            and a.subjseq = " + StringManager.makeSQL(v_subjseq);
            sql += "          order by a.seq desc ) ";
            sql += " where rownum <3 ";


            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new SubjGongData();

                data.setSeq( ls.getInt("seq") );
                data.setTypes( ls.getString("types") );
                data.setTypesnm( ls.getString("typesnm") );
                data.setAddate( ls.getString("addate") );
                data.setTitle( ls.getString("title") );
                data.setUserid( ls.getString("userid") );
                data.setAdcontent( ls.getCharacterStream("adcontent") );
                // data.setAdcontent( ls.getString("adcontent") );

                list.add(data);
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
     * 과목기수별 공지 상세보기
     * @param box          receive from the form object and session
     * @return data        SubjGongData 공지데이타빈
     * @throws Exception
     */
    public SubjGongData selectViewGong(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr		= null;
        ListSet 			ls        	= null;
        ArrayList 			list    	= null;
        String 				sql       	= "";
        String 				sql1       	= "";
        SubjGongData 		data 		= null;
        String              v_upcnt 	= "Y";

        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        int    v_seq     = box.getInt("p_seq");

        String v_typesgubun = CodeConfigBean.getConfigValue("noticeCategory");    // 카테고리종류리스트

        try { 
            connMgr = new DBConnectionManager();

            // sql  = " select a.seq seq , a.types types, b.codenm typesnm, a.addate addate, ";
            // sql += "        a.title title, a.userid userid, a.adcontent adcontent         ";
            // sql += "   from TZ_GONG a, TZ_CODE b                                          ";
            // sql += "  where a.types   = b.code                                            ";
            // sql += "    and b.gubun   = " + StringManager.makeSQL(v_typesgubun);
            // sql += "    and b.levels  = 1                                                 ";
            // sql += "    and a.subj    = " + StringManager.makeSQL(v_subj);
            // sql += "    and a.year    = " + StringManager.makeSQL(v_year);
            // sql += "    and a.subjseq = " + StringManager.makeSQL(v_subjseq);
            // sql += "    and a.seq     = " + v_seq;
            
			sql  = " select tg.seq , tg.types, tg.addate, tg.title, tg.userid userid, tg.adcontent, tm.name, decode(tg.gadmin, 'ZZ', 'ZZ', 'P1', '강사',  '운영자') gadmin, isimport,upfile,realfile,cnt  ";
            sql += "   from TZ_GONG   tg                                          ";
            sql += "   ,    TZ_Member tm                                          ";
            sql += "  where tg.subj    = " + StringManager.makeSQL(v_subj);
            sql += "    and tg.year    = " + StringManager.makeSQL(v_year);
            sql += "    and tg.subjseq = " + StringManager.makeSQL(v_subjseq);
            sql += "    and tg.seq     = " + v_seq;
            sql += "    and tg.userid  = tm.userid ";
//System.out.println("sql :::::::::" +sql);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new SubjGongData();

                data.setSeq( ls.getInt("seq") );
                data.setTypes( ls.getString("types") );
                data.setAddate( ls.getString("addate") );
                data.setTitle( ls.getString("title") );
                data.setUserid( ls.getString("userid") );
                data.setAdcontent( ls.getCharacterStream("adcontent") );
                data.setName( ls.getCharacterStream("name") );
                data.setGadmin( ls.getCharacterStream("gadmin") );
                data.setIsimport ( ls.getString("isimport"));
                
                data.setUpfile( ls.getString("upfile")); //upfile
                data.setRealfile( ls.getString("realfile")); //realfile
              //  data.setCnt( ls.getInt("cnt"));
                
                // data.setAdcontent( ls.getString("adcontent") );
            }
            if ( !v_upcnt.equals("N") ) {  // 조회수 증가
                
            	sql1 ="update TZ_GONG set cnt = cnt + 1 where subj = "+StringManager.makeSQL(v_subj)
						+" and year = "+StringManager.makeSQL(v_year)
						+" and subjseq = "+StringManager.makeSQL(v_subjseq)
						+" and seq = "+v_seq;
            	connMgr.executeUpdate(sql1);
         //   System.out.println("cnt update :::::" + sql1);
            }
            
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data;

    }

    /**
    공지구분별 샘플 공지사항정보
    @param box            receive from the form object and session
    @return SubjGongData  샘플 공지사항정보
    */
   public SubjGongData selectGongSample(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        SubjGongData  data = null;
        String v_types   = "";
        String v_default = "";

        try { 
            v_default = CodeConfigBean.getCodeDefault("noticeCategory","",1);
            v_types   = box.getStringDefault("p_types",v_default);

            connMgr = new DBConnectionManager();


            sql  = " select title, adcontent from TZ_GONGSAMPLE        ";
            sql += "  where types = " + StringManager.makeSQL(v_types);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new SubjGongData();
                data.setTitle( ls.getString("title") );
                data.setAdcontent( ls.getCharacterStream("adcontent") );

                // data.setAdcontent( ls.getString("adcontent") );
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data;
    }


    /**
    선택 공지사항 등록
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
   public int insertGongAll(RequestBox box) throws Exception { 
	   DBConnectionManager	connMgr	= null;
	   ListSet ls1 = null;
	   ListSet ls2 = null;
	   PreparedStatement   pstmt   = null;
	   SubjGongData  data = null;
	   String sql1 = "";
	   String sql2 = "";
	   String sql3 = "";
	   int isOk = 0;
	   int isOk_check = 0;
	   
	   String  ss_grcode     = box.getString("s_grcode");        // 교육그룹
	   String  ss_gyear      = box.getString("s_gyear");         // 년도
	   String  ss_grseq      = box.getStringDefault("s_grseq","ALL");         // 교육기수
	   String  ss_uclass    = box.getStringDefault("s_upperclass","ALL");    // 과목분류(대)
	   String  ss_mclass    = box.getStringDefault("s_middleclass","ALL");   // 과목분류(중)
	   String  ss_lclass    = box.getStringDefault("s_lowerclass","ALL");    // 과목분류(소)
	   String  ss_subjcourse = box.getString("s_subjcourse");    // 과목&코스
	   String  ss_subjseq    = box.getString("s_subjseq");       // 과목 기수
	   String  ss_isclosed   = box.getString("s_isclosed");      // 진행여부
	   
	   String v_types         = box.getString("p_types");
	   String s_title     = box.getString("p_title");        // 변경전 제목
	   String s_adcontent = box.getString("p_adcontent");    // 변경전 내용
	   
	   String s_userid        = box.getSession("userid");
	   
	   String v_checks        = box.getString("p_checks");       // 체크된 과목코드 + "/" +년도 + "/" +과목기수
	   
	   String v_title = "";
	   String v_adcontent = "";
	   
	   String v_userid    = "";
	   String v_subj      = "";
	   int    v_seq       = 0;
	   
	   try { 
		   connMgr = new DBConnectionManager();
		   connMgr.setAutoCommit(false);
		   
		   isOk = 1;
		   
		   // p_checks[]로 변경
		   StringTokenizer st = new StringTokenizer(v_checks, ",");
		   
		   while ( st.hasMoreElements() ) { 
			   
			   v_subj = st.nextToken(); 
			   
			   sql2  = " select max(seq) from TZ_GONG  ";
			   sql2 += "  where subj    = " + StringManager.makeSQL(v_subj);
			   //sql2 += "    and year    = " + StringManager.makeSQL(v_year);
			   //sql2 += "    and subjseq = " + StringManager.makeSQL(v_subjseq);
			   
			   ls2 = connMgr.executeQuery(sql2);
			   
			   if ( ls2.next() ) { 
				   v_seq = ls2.getInt(1) + 1;
			   } else { 
				   v_seq = 1;
			   }
			   
			   ls2.close();
			   
			   //data = getChangeText(v_subj, v_year, v_subjseq, s_title, s_adcontent );
			   
			   
			   //if ( data != null ) {
			//	   v_title         = data.getTitle();
				//   v_adcontent     = data.getAdcontent();
			//   }
			   
			   sql3 =  " insert into TZ_GONG(subj, year, subjseq, seq, types, addate, title, userid, adcontent, luserid, ldate)                    ";
			   sql3 += "             values (?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ? , ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
//                sql3 += "             values (?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ? , ?, empty_clob(), ?, to_char(sysdate, 'YYYYMMDDHH24MISS')) ";
			   
			   
			   pstmt = connMgr.prepareStatement(sql3);
			   
			   pstmt.setString(1, v_subj);
			   pstmt.setString(2, "0000");
			   pstmt.setString(3, "0000");
			   pstmt.setInt(4, v_seq);
			   pstmt.setString(5, v_types);
			   pstmt.setString(6, s_title);
			   pstmt.setString(7, s_userid);
			   pstmt.setString(8, s_adcontent);
			   pstmt.setString(9, s_userid);
			   
			   isOk_check =  pstmt.executeUpdate();
			   
			   if ( isOk_check == 0) isOk = 0;
			   
			   sql2 = "select adcontent from TZ_GONG";
			   sql2 += "  where subj    = " + StringManager.makeSQL(v_subj);
			   //sql2 += "    and year    = " + StringManager.makeSQL(v_year);
			   //sql2 += "    and subjseq = " + StringManager.makeSQL(v_subjseq);
			   sql2 += "    and seq = " + v_seq;
			   
//                connMgr.setOracleCLOB(sql2, v_adcontent);       //      (기타 서버 경우)
			   
		   }
		   
		   if ( isOk > 0) {
			   // 학생들에게 메일발송
			   //senMail(connMgr, box);
			   
			   connMgr.commit();
		   } else {
			   connMgr.rollback();
		   }
	   } catch ( Exception ex ) { 
		   ErrorManager.getErrorStackTrace(ex, box, sql1);
		   throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
	   } finally { 
		   if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
		   if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
		   if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
		   if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
	   }
	   
	   return isOk;
   }


    /**
    과목기수별 공지사항 등록
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int insertGong(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        SubjGongData  data = null;
        String sql  = "";
        String sql1 = "";
        int isOk = 0;

        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("s_gyear");
        String v_subjseq   = box.getString("p_subjseq");

        String v_types     = box.getString("p_types");
        String v_title    = box.getString("p_title");

        String v_content   = box.getString("p_adcontent");
        String s_userid    = box.getSession("userid");
        String s_gadmin    = box.getSession("gadmin");
        String v_isimport  = box.getStringDefault("p_isimport", "N");
        String v_upfile	   = box.getNewFileName("p_file1");
		String v_realfile  = box.getRealFileName("p_file1");

        // String v_title= "";
        // String v_adcontent= "";
//System.out.println("---------------------------------공지등록---"+box);
        String v_userid    = "";
        int    v_seq       = 0;

        try { 
        	
        	/*********************************************************************************************/
		   // 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
/*            
		   ConfigSet conf = new ConfigSet();
		   SmeNamoMime namo = new SmeNamoMime(v_content); // 객체생성 
		   boolean result = namo.parse(); // 실제 파싱 수행 
           
		   if ( !result ) { // 파싱 실패시 
		   	System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
		   	return 0;
		   }
           
		   if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
		   	String v_server = conf.getProperty("autoever.url.value");
		   	String fPath = conf.getProperty("dir.namo");   // 파일 저장 경로 지정
		   	String refUrl = conf.getProperty("url.namo");; // 웹에서 저장된 파일을 접근하기 위한 경로
		   	String prefix =  "SubjGong" + v_seq;         // 파일명 접두어
		   	result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
		   }
           
		   if ( !result ) { // 파일저장 실패시 
		   	System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
		   	return 0;
		   }
		   
           
		   v_content = namo.getContent(); // 최종 컨텐트 얻기
*/           
		   /*********************************************************************************************/
        	
            //data = getChangeText(v_subj, v_title, v_content );
            if ( data != null ) { 
                v_title     = data.getTitle();
                v_content = data.getAdcontent();
                v_userid    = data.getUserid();
            }
            
            v_userid        = s_userid;

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql  = " select max(seq) from TZ_GONG  ";
            sql += "  where subj    = " + StringManager.makeSQL(v_subj);
            sql += "    and year    = " + StringManager.makeSQL(v_year);
            sql += "    and subjseq = " + StringManager.makeSQL(v_subjseq);
         //System.out.println("========================" + sql);
            ls = connMgr.executeQuery(sql);
            if ( ls.next() ) { 
                v_seq = ls.getInt(1) + 1;
            } else { 
                v_seq = 1;
            }

            sql1 =  " insert into TZ_GONG(subj, year, subjseq, seq, types, addate, title, userid, adcontent, luserid, ldate, gadmin, isimport,upfile,realfile )                     ";
            sql1 += "             values (?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ? ,? ,?) ";


            pstmt = connMgr.prepareStatement(sql1);

            pstmt.setString(1, v_subj);
            pstmt.setString(2, v_year);
            pstmt.setString(3, v_subjseq);
            pstmt.setInt(4, v_seq);
            pstmt.setString(5, v_types);
            pstmt.setString(6, v_title);
            pstmt.setString(7, v_userid);
            pstmt.setString(8, v_content);
            pstmt.setString(9, s_userid);
            pstmt.setString(10, s_gadmin);
            pstmt.setString(11, v_isimport);
            pstmt.setString(12, v_upfile);
            pstmt.setString(13, v_realfile);
            
            isOk = pstmt.executeUpdate();

            sql1 = "select adcontent from tz_gong";
            sql1 += "  where subj    = " + StringManager.makeSQL(v_subj);
            sql1 += "    and year    = " + StringManager.makeSQL(v_year);
            sql1 += "    and subjseq = " + StringManager.makeSQL(v_subjseq);
            sql1 += "    and seq = " + v_seq;
//            connMgr.setOracleCLOB(sql1, v_content);       //      (ORACLE 9i 서버)

            if ( isOk > 0) {
         	   	// 학생들에게 메일발송
                box.put("p_checks", v_subj);       // 체크된 과목코드 + "/" +년도 + "/" +과목기수
                //senMail(connMgr, box);

                connMgr.commit();
            } else {
            	connMgr.rollback();
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
     과목기수별 공지사항 수정하여 저장할때
     @param box      receive from the form object and session
     @return isOk    1:update success,0:update fail
     */
      public int updateGong(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        SubjGongData  data = null;
        String              sql     = "";
        int isOk = 0;

        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_year");
        String v_subjseq   = box.getString("p_subjseq");
        int    v_seq       = box.getInt("p_seq");

        String v_types     = box.getString("p_types");
        String v_title     = box.getString("p_title");
        String v_adcontent = box.getString("p_adcontent");
        String s_userid    = box.getSession("userid");
        String s_gadmin    = box.getSession("gadmin");
        String v_isimport  = box.getStringDefault("p_isimport", "N");

        String v_userid    = "";
        
        String v_filedelch = box.getString("p_filedelch"); // 파일 삭제 여부
        String v_upfile	   = box.getNewFileName("p_file1");
		String v_realfile  = box.getRealFileName("p_file1");
        
        try { 
			
		   /*********************************************************************************************/
		   // 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
/*            
		   ConfigSet conf = new ConfigSet();
		   SmeNamoMime namo = new SmeNamoMime(v_adcontent); // 객체생성 
		   boolean result = namo.parse(); // 실제 파싱 수행 

		   if ( !result ) { // 파싱 실패시 
		   	System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
		   	return 0;
		   }

		   if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
		   	String v_server = conf.getProperty("autoever.url.value");
		   	String fPath = conf.getProperty("dir.namo");   // 파일 저장 경로 지정
		   	String refUrl = conf.getProperty("url.namo");; // 웹에서 저장된 파일을 접근하기 위한 경로
		   	String prefix =  "HomeNotice" + v_seq;         // 파일명 접두어
		   	result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
		   }

		   if ( !result ) { // 파일저장 실패시 
		   	System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
		   	return 0;
		   }
		   

		   v_adcontent = namo.getContent(); // 최종 컨텐트 얻기
*/           
		   /*********************************************************************************************/
			
            //data = getChangeText(v_subj, v_year, v_subjseq, v_title, v_adcontent );
            if ( data != null ) { 
                v_title     = data.getTitle();
                v_adcontent = data.getAdcontent();
                v_userid    = data.getUserid();
            }

            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql  = " update TZ_GONG set types = ?, title = ?, adcontent= ?, luserid = ?, ldate = to_char(sysdate, 'YYYYMMDDHH24MISS'), gadmin = ?, isimport = ? ";
            
            if("1".equals(v_filedelch) && "".equals(v_upfile) ){
            	sql +=" , upfile=? ,realfile=?  \n";
            }
            
            if(!"".equals(v_upfile)){
            	sql +=" , upfile=?  ,realfile=?   \n";
            }
            
            
            sql += "  where subj    = " + StringManager.makeSQL(v_subj);
            sql += "    and year    = " + StringManager.makeSQL(v_year);
            sql += "    and subjseq = " + StringManager.makeSQL(v_subjseq);
            sql += "    and seq = " + v_seq;


            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1,  v_types);
            pstmt.setString(2,  v_title);
            pstmt.setString(3,  v_adcontent);
            pstmt.setString(4,  s_userid);
            pstmt.setString(5,  s_gadmin);
            pstmt.setString(6,  v_isimport);
            
            if("1".equals(v_filedelch) && "".equals(v_upfile) ){
            	pstmt.setString(7,  ""  );
            	pstmt.setString(8,  ""  );
//            	 pstmt.setString(9,  v_subj  );
//                 pstmt.setInt(10,  v_seq);
            	
            }
            if(!"".equals(v_upfile)){
            	pstmt.setString(7,  v_upfile  );
            	pstmt.setString(8,  v_realfile  );
//            	pstmt.setString(9,  v_subj  );
//                pstmt.setInt(10,  v_seq);
            
            }
            
            if("".equals(v_filedelch) && "".equals(v_upfile) ){
            	
//            	pstmt.setString(7,  v_subj  );
//                pstmt.setInt(8,  v_seq);
             
            }
            
           
            
           

             isOk = pstmt.executeUpdate();


            sql = "select adcontent from tz_gong";
            sql += "  where subj    = " + StringManager.makeSQL(v_subj);
            sql += "    and year    = " + StringManager.makeSQL(v_year);
            sql += "    and subjseq = " + StringManager.makeSQL(v_subjseq);
            sql += "    and seq = " + v_seq;

//            connMgr.setOracleCLOB(sql, v_adcontent);       //      (ORACLE 9i 서버)

            if ( isOk > 0) connMgr.commit();
            else connMgr.rollback();
         }
         catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
         }
         finally { 
             if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return isOk;
     }

     /**
     과목기수별 공지사항 삭제할때
     @param box      receive from the form object and session
     @return isOk    1:delete success,0:delete fail
     */
     public int deleteGong(RequestBox box) throws Exception { 
         DBConnectionManager	connMgr	= null;
         PreparedStatement   pstmt   = null;
         String              sql     = "";
         int isOk = 0;

        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_year");
        String v_subjseq   = box.getString("p_subjseq");
        int    v_seq       = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " delete from TZ_GONG                                      ";
         //   sql += "  where subj = ? and seq = ? ";
            sql += "  where subj = ? and year = ? and subjseq = ? and seq = ? ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1,  v_subj);
            pstmt.setString(2,  v_year);
            pstmt.setString(3,  v_subjseq);
            pstmt.setInt(4,  v_seq);
            isOk = pstmt.executeUpdate();

             Log.info.println(this, box, "delete TZ_GONG where subj" + v_subj + " and seq" + v_seq);
         }
         catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex, box, sql);
             throw new Exception("sql = " + sql + "\r\n"  + ex.getMessage() );
         }
         finally { 
             if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }
         return isOk;
     }


    /**
     * 변수값 대치(제목 및 내용)
     * @param subj       과목코드
     * @param year       과목년도
     * @param subjseq    과목기수
     * @param title      공지제목
     * @param adcontent  공지내용
     * @return data      SubjGongData (공지데이타빈- 변경한 제목,타이틀 셋팅)
     * @throws Exception
     */
    public SubjGongData getChangeText (String subj, String year, String subjseq, String title, String adcontent) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        SubjGongData  data = null;

        String v_subjnm     = "";   // 과목명
        String v_edudates   = "";   // 교육일수
        String v_eduperiod  = "";   // 교육기간
        String v_name       = "";   // 운영자 이름
        String v_comptel    = "";   // 운영자 전화
        String v_email      = "";   // 운영자 E-mail
        String v_wstep      = "";   // 가중치(진도율)
        String v_wmtest     = "";   // 가중치(중간평가)
        String v_wftest     = "";   // 가중치(최종평가)
        String v_whtest     = "";   // 가중치(형성평가)
        String v_wreport    = "";   // 가중치(프로젝트)
        String v_wact       = "";   // 가중치(액티비티)
        String v_gradscore  = "";   // 수료기준점수
        String v_gradstep   = "";   // 수료기준진도율
        String v_gradexam   = "";   // 수료기준시험
        String v_gradreport = "";   // 수료기준과제
        String v_point      = "";   // 수료점수
        String v_edustart   = "";   // 시작일
        String v_eduend     = "";   // 종료일

        String v_userid     = "";

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select a.subjnm subjnm, ";
            sql += "        a.point point, ";
            sql += "        a.gradscore gradscore, ";
            sql += "        a.gradstep gradstep, ";
            sql += "        a.gradexam gradexam, ";
            sql += "        a.gradreport gradreport,";
            sql += "        a.wstep wstep, ";
            sql += "        a.wmtest wmtest,";
            sql += "        a.wftest wftest, ";
            sql += "        a.whtest whtest, ";
            sql += "        a.wreport wreport, ";
            sql += "        a.wact wact, ";
            sql += "        a.edustart edustart,     ";
            sql += "        a.eduend eduend, ";
            sql += "        b.muserid userid, ";
            sql += "        (select name from tz_member where userid=b.muserid) name, ";
            sql += "        (select email from tz_member where userid=b.muserid) email, ";
            sql += "        '' comptel 													";
            //sql += "        (select comptel from tz_member where userid=b.muserid) comptel ";
            sql += " from   tz_subjseq a, TZ_SUBJ b ";
            sql += " where  a.subj = b.subj  ";
            sql += "    and a.subj    = " + StringManager.makeSQL(subj);
            sql += "    and a.year    = " + StringManager.makeSQL(year);
            sql += "    and a.subjseq = " + StringManager.makeSQL(subjseq);

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 

                v_subjnm     = ls.getString("subjnm");
                v_point      = String.valueOf( ls.getInt("point") );
                v_gradscore  = ls.getString("gradscore");
                v_gradstep   = ls.getString("gradstep");
                v_gradexam   = ls.getString("gradexam");
                v_gradreport = ls.getString("gradreport");
                v_wstep      = String.valueOf( ls.getInt("wstep") );
                v_wmtest     = String.valueOf( ls.getInt("wmtest") );
                v_wftest     = String.valueOf( ls.getInt("wftest") );
                v_whtest     = String.valueOf( ls.getInt("whtest") );
                v_wreport    = String.valueOf( ls.getInt("wreport") );
                v_wact       = String.valueOf( ls.getInt("wact") );

                v_edustart   = ls.getString("edustart");
                v_eduend     = ls.getString("eduend");
                v_edudates   = String.valueOf(FormatDate.datediff("date", v_edustart, v_eduend));
                // System.out.println("v_edudates == >> " +v_edudates);
                // System.out.println("v_gradscore == >> " +v_gradscore);
                v_edustart   = FormatDate.getFormatDate(v_edustart,"yyyy.MM.dd");
                v_eduend     = FormatDate.getFormatDate(v_eduend, "yyyy.MM.dd");
                v_eduperiod  = v_edustart + " ~ " + v_eduend;

                v_userid     = ls.getString("userid");

                v_name       = ls.getString("name");
                v_comptel    = ls.getString("comptel");
                v_email      = ls.getString("email");

                // 타이틀 변경
                title = StringManager.replace(title, "v_subjnm", v_subjnm);
                title = StringManager.replace(title, "v_point", v_point);
                title = StringManager.replace(title, "v_gradscore", v_gradscore);
                title = StringManager.replace(title, "v_gradstep", v_gradstep);
                title = StringManager.replace(title, "v_gradexam", v_gradexam);
                title = StringManager.replace(title, "v_gradreport", v_gradreport);
                title = StringManager.replace(title, "v_wstep", v_wstep);
                title = StringManager.replace(title, "v_wmtest", v_wmtest);
                title = StringManager.replace(title, "v_wftest", v_wftest);
                title = StringManager.replace(title, "v_whtest", v_whtest);
                title = StringManager.replace(title, "v_wreport", v_wreport);
                title = StringManager.replace(title, "v_wact", v_wact);
                title = StringManager.replace(title, "v_edustart", v_edustart);
                title = StringManager.replace(title, "v_eduend", v_eduend);
                title = StringManager.replace(title, "v_edudates", v_edudates);
                title = StringManager.replace(title, "v_eduperiod", v_eduperiod);
                title = StringManager.replace(title, "v_name", v_name);
                title = StringManager.replace(title, "v_comptel", v_comptel);
                title = StringManager.replace(title, "v_email", v_email);

                // 내용 변경
                adcontent = StringManager.replace(adcontent, "v_subjnm", v_subjnm);
                adcontent = StringManager.replace(adcontent, "v_point", v_point);
                adcontent = StringManager.replace(adcontent, "v_gradscore", v_gradscore);
                adcontent = StringManager.replace(adcontent, "v_gradstep", v_gradstep);
                adcontent = StringManager.replace(adcontent, "v_gradexam", v_gradexam);
                adcontent = StringManager.replace(adcontent, "v_gradreport", v_gradreport);
                adcontent = StringManager.replace(adcontent, "v_wstep", v_wstep);
                adcontent = StringManager.replace(adcontent, "v_wmtest", v_wmtest);
                adcontent = StringManager.replace(adcontent, "v_wftest", v_wftest);
                adcontent = StringManager.replace(adcontent, "v_whtest", v_whtest);
                adcontent = StringManager.replace(adcontent, "v_wreport", v_wreport);
                adcontent = StringManager.replace(adcontent, "v_wact", v_wact);
                adcontent = StringManager.replace(adcontent, "v_edustart", v_edustart);
                adcontent = StringManager.replace(adcontent, "v_eduend", v_eduend);
                adcontent = StringManager.replace(adcontent, "v_edudates", v_edudates);
                adcontent = StringManager.replace(adcontent, "v_eduperiod", v_eduperiod);
                adcontent = StringManager.replace(adcontent, "v_name", v_name);
                adcontent = StringManager.replace(adcontent, "v_comptel", v_comptel);
                adcontent = StringManager.replace(adcontent, "v_email", v_email);


                data = new SubjGongData();
                data.setTitle(title);
                data.setAdcontent(adcontent);
                data.setUserid(v_userid);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data;
    }


    /**
    권장진도율 (  권장진도율=(현재까지 일수*100)/전체학습기간일수 )
    @param box            receive from the form object and session
    @return String       권장진도율
    */
   public String getPromotion(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";

        float percent     = (float)0.0;
        String result     = "0";
        String v_today    = "";
        String v_edustart = "";
        String v_eduend   = "";
        int    v_nowday   = 0;
        int    v_allday   = 0;

        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_year");
        String v_subjseq   = box.getString("p_subjseq");
        try { 
            connMgr = new DBConnectionManager();


            sql  = " select to_char(sysdate,'yyyymmdd') today, substr(edustart,0,8) edustart, substr(eduend,0,8) eduend ";
            sql += "   from tz_subjseq                                                                                  ";
            sql += "  where subj    = " + StringManager.makeSQL(v_subj);
            sql += "    and year    = " + StringManager.makeSQL(v_year);
            sql += "    and subjseq = " + StringManager.makeSQL(v_subjseq);

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                v_today    = ls.getString("today");
                v_edustart = ls.getString("edustart");
                v_eduend   = ls.getString("eduend");
                v_nowday = FormatDate.datediff("date",v_edustart,v_today);
                v_allday = FormatDate.datediff("date",v_edustart,v_eduend);

                if ( v_allday != 0 ) { 
                    percent = (float)((v_nowday * 100) / (float)v_allday);
                    if ( percent <= 0.0) percent=0;
                    else if ( percent > 100.0) percent=100;
                    result =  new DecimalFormat("0.00").format(percent);
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }

    /**
    평균진도율
    @param box            receive from the form object and session
    @return String       평균진도율
    */
   public String getAverage(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";

        float percent     = (float)0.0;
        String result     = "0";

        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_year");
        String v_subjseq   = box.getString("p_subjseq");
        try { 
            connMgr = new DBConnectionManager();

            sql  = " select nvl(avg(tstep),0.0) tstep  ";
            sql += "   from tz_student                 ";
            sql += "  where subj    = " + StringManager.makeSQL(v_subj);
            sql += "    and year    = " + StringManager.makeSQL(v_year);
            sql += "    and subjseq = " + StringManager.makeSQL(v_subjseq);

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                percent    = ls.getFloat("tstep");
                result =  String.valueOf(percent);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }
    
    /**
    자기진도율
    @param box            receive from the form object and session
    @return String       자기진도율
    */
   public String getProgress(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";

        float percent     	 = (float)0.0;
        String result     	 = "0";
        String v_contenttype = "";

        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_year");
        String v_subjseq   = box.getString("p_subjseq");
        
        String s_userid = box.getSession("userid");

        if ( !box.getString("p_userid").equals("") && EduAuthBean.getInstance().isAdminAuth(box.getSession("gadmin")) ) {
        	s_userid = box.getString("p_userid");
        }
        
        // String s_userid = box.getString("p_userid");
        try { 
            // 미리보기가 아닐경우만 실행
            if ( !v_year.equals("PREV") && !v_year.equals("2000") ) { 
                connMgr = new DBConnectionManager();
						
                // 점수 재계산 (ALL)
                int isOk = 0;
               	isOk = CalcUtil.getInstance().calc_score(connMgr,CalcUtil.ALL,v_subj,v_year,v_subjseq,s_userid);
/*
                sql  = " select tstep                ";
                sql += "   from tz_student           ";
                sql += "  where subj    = " + StringManager.makeSQL(v_subj);
                sql += "    and year    = " + StringManager.makeSQL(v_year);
                sql += "    and subjseq = " + StringManager.makeSQL(v_subjseq);
                sql += "    and userid = " + StringManager.makeSQL(s_userid);
*/
                
                sql  = "    SELECT ROUND ((a.completed_educheck_cnt / b.total_cnt) * 100, 2) AS tstep ";
                sql += "    FROM (SELECT COUNT (*) completed_educheck_cnt  ";
                sql += "            FROM tz_progress   ";
                sql += "           WHERE subj = " + StringManager.makeSQL(v_subj);
                sql += "             AND YEAR = " + StringManager.makeSQL(v_year);
                sql += "             AND subjseq = " + StringManager.makeSQL(v_subjseq);
                sql += "             AND userid = " + StringManager.makeSQL(s_userid);
                sql += "             AND first_end is not null ";
                sql += "          ) a,   ";
                sql += "         (SELECT COUNT (*) total_cnt ";  
                sql += "            FROM TZ_SUBJLESSON  ";
                sql += "           WHERE subj = "+StringManager.makeSQL(v_subj)+" and lesson != '00' and lesson != '99') b ";
                
                
                ls = connMgr.executeQuery(sql);

                if ( ls.next() ) { 
                    percent    = ls.getFloat("tstep");
                    result =  String.valueOf(percent);
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }


    /**
    자기진도율
    @param box            receive from the form object and session
    @return String       자기진도율
    */
   public String getProgress2(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";

        float percent     = (float)0.0;
        String result     = "0";

        String v_subj      = box.getString("p_subj");
        String v_year      = box.getString("p_year");
        String v_subjseq   = box.getString("p_subjseq");
        
        String s_userid = box.getSession("userid");

        // String s_userid = box.getString("p_userid");
        try { 
            // 미리보기가 아닐경우만 실행
            if ( !v_year.equals("PREV") && !v_year.equals("2000") ) { 
                connMgr = new DBConnectionManager();

                sql  = " select tstep                ";
                sql += "   from tz_student           ";
                sql += "  where subj    = " + StringManager.makeSQL(v_subj);
                sql += "    and year    = " + StringManager.makeSQL(v_year);
                sql += "    and subjseq = " + StringManager.makeSQL(v_subjseq);
                sql += "    and userid = " + StringManager.makeSQL(s_userid);

                ls = connMgr.executeQuery(sql);

                if ( ls.next() ) { 
                    percent    = ls.getFloat("tstep");
                    result =  String.valueOf(percent);
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }

    /**
     * 고용보험안내 보기
     * @param box          receive from the form object and session
     * @return data        SubjGongData 공지데이타빈
     * @throws Exception
     */
    public SubjGongData selectViewGoyong(RequestBox box) throws Exception { 

        DBConnectionManager	connMgr	= null;
        ListSet ls        = null;
        ArrayList list    = null;
        String sql        = "";
        SubjGongData data = null;


        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        int    v_seq     = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();

             sql  = " select a.subjnm, ";
             sql += "        a.studentlimit, ";
             sql += "        a.edutimes, ";
             sql += "        a.musertel, ";
             sql += "        b.name, ";
             sql += "        (select substr(comp,0,4) from tz_grcode where grcode=c.grcode) comp";
             sql += "   from tz_subj a, ";
             sql += "        tz_member b, ";
             sql += "        tz_subjseq c";
             sql += "  where a.muserid=b.userid ";
             sql += "    and a.subj=c.subj ";
             sql += "    and a.subj=" +StringManager.makeSQL(v_subj);
             sql += "    and c.subjseq=" +StringManager.makeSQL(v_subjseq);
             sql += "    and c.year=" +StringManager.makeSQL(v_year);
//             System.out.println("sql=" +sql);
//            sql  = " select a.subjnm, ";
//            sql += "        a.studentlimit, ";
//            sql += "        a.edutimes, ";
//            sql += "        a.musertel, ";
//            sql += "        (select substr(comp,0,4) from tz_grcode where grcode=c.grcode) comp,"; 
//            sql	+= "        a.name";
//            sql += "   from tz_subj a, ";
//            sql += "        tz_subjseq c";
//            sql += "  where a.subj=c.subj ";
//            sql += "    and a.subj=" +StringManager.makeSQL(v_subj);
//            sql += "    and c.subjseq=" +StringManager.makeSQL(v_subjseq);
//            sql += "    and c.year=" +StringManager.makeSQL(v_year);
            
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new SubjGongData();

                data.setSubjnm( ls.getString("subjnm") );
                data.setStudentLimit( ls.getInt("studentlimit") );
                data.setEduTimes( ls.getInt("edutimes") );
                data.setMuserTel( ls.getString("musertel") );
                data.setName(ls.getString("name"));
                data.setComp( ls.getString("comp") );

                  // data.setAdcontent( ls.getString("adcontent") );

            }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data;

    }


    /**
    학습 가능 여부 조회
    @param box      receive from the form object and session
    @return
    */
    public boolean allowStudy(RequestBox box, String subj, String year, String subjseq) throws Exception { 
		box.put("p_subj", subj);
		box.put("p_year", year);
		box.put("p_subjseq", subjseq);
		return allowStudy(box);
	}

    /**
    학습 가능 여부 조회
    @param box      receive from the form object and session
    @return
    */
    public boolean allowStudy(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;

        ListSet ls1 = null;
        ListSet ls2 = null;
        ListSet ls3 = null;
        ListSet ls4 = null;
        ListSet ls5 = null;

        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        String sql4 = "";
        String sql5 = "";
        int isOk = 0;
        int v_cnt = 0;
        boolean isexp = false;

        String v_subj       = box.getString("p_subj");
        String v_year       = box.getString("p_year");
        String v_subjseq    = box.getString("p_subjseq");
        String s_userid     = box.getSession("userid");

        String v_upperclass = "", v_comp = "", v_day = "", v_code = "";

        try { 
            connMgr = new DBConnectionManager();

            sql1  = "  select comp from tz_member where userid=" + StringManager.makeSQL(s_userid);
            ls1 = connMgr.executeQuery(sql1);

            if ( ls1.next() ) { 
                v_comp = ls1.getString("comp");
            }

            // 대분류 조회 - > 직무, 어학 구분하기 위해
            sql2  = " select scupperclass from vz_scsubjseq ";
            sql2 += "   where scsubj=" + StringManager.makeSQL(v_subj);
            sql2 += "     and scyear=" + StringManager.makeSQL(v_year);
            sql2 += "     and scsubjseq=" + StringManager.makeSQL(v_subjseq);
            ls2 = connMgr.executeQuery(sql2);
            if ( ls2.next() ) { 
                v_upperclass = ls2.getString("scupperclass");
            }


            // 학습제약 예외자체크 - > 학습가능
            sql3  = " select count(*) cnt from TZ_STUDYCONTROLEXP ";
            sql3 += "   where company = " + StringManager.makeSQL(v_comp);
            sql3 += "     and gubun   = (select matchcode from tz_classfymatch where upperclass = " + StringManager.makeSQL(v_upperclass) + ")" ;
            sql3 += "     and userid=" + StringManager.makeSQL(s_userid);
            ls3 = connMgr.executeQuery(sql3);
            if ( ls3.next() ) { 
                if ( ls3.getInt("cnt") > 0) isexp = true;
            }
// System.out.println("예외자체크" + sql3);
            // 예외자가 아닐경우
            if ( !isexp) { 
                // 휴일 여부 체크 - > 학습불가
                sql4  = " select count(*) cnt from TZ_STUDYCONTROL ";
                sql4 += "   where company = " + StringManager.makeSQL(v_comp);
                sql4 += "     and gubun   = (select matchcode from tz_classfymatch where upperclass = " + StringManager.makeSQL(v_upperclass) + ")" ;
                sql4 += "     and startdt <= to_char(sysdate,'yyyyMMdd') ";
                sql4 += "     and enddt >= to_char(sysdate,'yyyyMMdd')   ";
                sql4 += "     and isholiday = 'Y'                        ";
                sql4 += "     and isuse='Y'                              ";
                ls4 = connMgr.executeQuery(sql4);
                if ( ls4.next() ) { 
                    v_cnt = ls4.getInt("cnt");
                }
// System.out.println("휴일 여부 " + sql4);
                if ( v_cnt == 0 ) { 
                    // 학습시간제한 체크
                    sql5  = " select count(seq) cnt ";
                    sql5 += "   from TZ_STUDYCONTROL ";
                    sql5 += "  where company=" + StringManager.makeSQL(v_comp);
                    sql5 += "    and gubun= (select matchcode from tz_classfymatch where upperclass = " + StringManager.makeSQL(v_upperclass) + ")" ;
                    sql5 += "    and startdt <= to_char(sysdate,'yyyyMMdd') ";
                    sql5 += "    and enddt >= to_char(sysdate,'yyyyMMdd') ";
                    sql5 += "    and starttime <= to_char(sysdate,'HH24mi') ";
                    sql5 += "    and endtime >= to_char(sysdate,'HH24mi') ";
                    // sql5 += "    and day1 <= (select decode(to_char(sysdate,'dy'),'일','1','월','2','화','3','수','4','목','5','금','6','토','7') from dual) ";
                    // sql5 += "    and day2 >= (select decode(to_char(sysdate,'dy'),'일','1','월','2','화','3','수','4','목','5','금','6','토','7') from dual) ";
                    sql5 += "    and (select decode(to_char(sysdate,'dy'),'월','1','화','2','수','3','목','4','금','5','토','6','일','7') from dual) between day1 and day2 ";
                    sql5 += "    and isuse='Y' ";

                    ls5 = connMgr.executeQuery(sql5);
// System.out.println("학습시간제한 체크 " + sql5);
                    if ( ls5.next() ) { 
                        v_cnt = ls5.getInt("cnt");
                    }
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception ( "sql = " + sql1 + "\r\n " + ex.getMessage() );
        } finally { 
         if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
         if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
         if ( ls3 != null ) { try { ls3.close(); } catch ( Exception e ) { } }
         if ( ls4 != null ) { try { ls4.close(); } catch ( Exception e ) { } }
         if ( ls5 != null ) { try { ls5.close(); } catch ( Exception e ) { } }
         if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_cnt == 1;
    }

    public String getProgressBetatest(RequestBox box) throws Exception 
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        
        String p_subj = box.getString("p_subj");
        String p_year = box.getString("p_year");
        String p_subjseq = box.getString("p_subjseq");
        String p_userid = box.getSession("userid");

        StringBuffer sbSQL = new StringBuffer("");
        StringBuffer sbSQL1 = new StringBuffer("");
        
        int v_datecnt = 0;
        int v_edudatecnt = 0;
        String tstep = "";

        try 
        {
            connMgr = new DBConnectionManager();
            
            String courseCode = "";
            sbSQL.append( " SELECT course_code                              \n" )
                 .append( " FROM tz_subj_contents                           \n" )
                 .append( " WHERE subj = " + SQLString.Format(p_subj) + "   \n" )
                 .append( " AND rownum = 1                                  \n" );
            
            ls = connMgr.executeQuery( sbSQL.toString() );
            
            if ( ls.next() )
            {
                courseCode = ls.getString( "course_code" );
            }
            
            ls.close();
            sbSQL.delete( 0, sbSQL.length() );
            
            /* 전체 sco 갯수 */
            sbSQL.append( " SELECT count(*) datecnt                                     \n" )
                 .append( " FROM tys_item a, tys_resource b                             \n" )
                 .append( " WHERE 1=1                                                   \n" )
                 .append( " AND a.course_code = b.course_code                           \n" )
                 .append( " AND a.org_id = b.org_id                                     \n" )
                 .append( " AND a.item_id = b.item_id                                   \n" )
                 .append( " AND a.course_code = " + SQLString.Format(courseCode) + "    \n" )
                 .append( " AND b.res_scorm_type = 'sco'                                \n" );

            /* 학습한 sco 갯수 */
            sbSQL1.append( " SELECT count(*) edudatecnt                                      \n" )
                  .append( " FROM tys_item a, tys_resource b, tz_betaprogress c                  \n" )
                  .append( " WHERE 1 = 1                                                     \n" )
                  .append( " AND a.course_code = b.course_code                               \n" )
                  .append( " AND a.org_id = b.org_id                                         \n" )
                  .append( " AND a.item_id = b.item_id                                       \n" )
                  .append( " AND a.course_code = " + SQLString.Format(courseCode) + "        \n" )
                  .append( " AND b.res_scorm_type = 'sco'                                    \n" )
                  .append( " AND c.subj = " + SQLString.Format(p_subj) + "                   \n" )
                  .append( " AND c.year = " + SQLString.Format(p_year) + "                   \n" )
                  .append( " AND c.subjseq = " + SQLString.Format(p_subjseq) + "             \n" )
                  .append( " AND c.lesson like '" + courseCode + "_%'                        \n" )
                  .append( " AND c.oid = b.item_id                                           \n" )
                  .append( " AND c.userid = " + SQLString.Format(p_userid) + "               \n" )
                  .append( " AND c.lessonstatus = 'complete'                                 \n" );

            ls  = connMgr.executeQuery(sbSQL.toString());
            while ( ls.next() ) 
            { 
                v_datecnt   = ls.getInt("datecnt");
            }
            ls.close();
                
            ls  = connMgr.executeQuery(sbSQL1.toString());
            while ( ls.next() ) 
            { 
                v_edudatecnt = ls.getInt("edudatecnt");
            }
            ls.close();

            if ( v_edudatecnt == 0 ) 
            { 
                tstep = "0";
            }
            else  
            { 
                tstep = String.valueOf((double) Math.round((double)v_edudatecnt / v_datecnt * 100 * 100) / 100);
            }

            if ( Double.parseDouble(tstep) > 100 )
            { 
                tstep = "100";
            }
        }
        catch ( SQLException e ) 
        {
            ErrorManager.getErrorStackTrace(e, null, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        }
        catch ( Exception e ) 
        {
            ErrorManager.getErrorStackTrace(e);
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        }
        finally 
        {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }        
        
        return tstep;
        
    }
    
    /**
    ThinkWise S/W 다운로드시 Parameter 정보 Select
    @param box          receive from the form object and session
    @return Map         ThinkWise에 넘길 Parameter 정보 Map
    */
    public Map selectThinkWiseInfo(RequestBox box) throws Exception {
        
        DBConnectionManager connMgr = null;
        ListSet ls = null;
        
        Map map = null;
        String sql = "";

        String v_userid = box.getSession("userid");
        
        try {
            connMgr = new DBConnectionManager();

            map = new HashMap();

            sql =
                "\r\n  SELECT userid, name, SUBSTR(fn_crypt('2', birth_date, 'knise'), 1, 6) birth_date1 FROM tz_member  " +
                "\r\n  WHERE userid = " + StringManager.makeSQL( v_userid );                

            ls = connMgr.executeQuery(sql);

            if (ls.next()) {
                map.put( "p_id", ls.getString("userid") );
                map.put( "p_name", ls.getString("name") );
                map.put( "p_birth_date1", ls.getString("birth_date1") );
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
        return map;
    }    
    
    /**
    과목기수별 공지 화면 리스트
    @param box          receive from the form object and session
    @return ArrayList   과목기수별 공지  리스트
    */
    public ArrayList selectListSubjGong(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls        = null;
        ArrayList list    = null;
        String sql        = "";
        DataBox dbox      = null;

        String v_subj    = box.getStringDefault("p_sel_subj", "ALL");
        
        int v_pageno   = box.getInt("p_pageno");        
        String v_userid = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();


            list = new ArrayList();

            /*
            sql = "\n select tg.subj, tg.year, tg.subjseq, (select subjnm from tz_subj where subj = tg.subj) as subjnm "
            	+ "\n      , tg.seq , tg.types, tg.addate "
            	+ "\n      , tg.title , tg.userid, tg.adcontent "
            	+ "\n      , get_name(ts.userid) as name, decode(tg.gadmin, 'ZZ', 'ZZ', 'P1', '강사',  '운영자') gadmin "
            	+ "\n      , tg.isimport "
            	+ "\n from   tz_gong   tg "
            	+ "\n      , tz_student ts "
            	+ "\n where  tg.subj = ts.subj "
            	//+ "\n and    tg.year = ts.year "
            	//+ "\n and    tg.subjseq = ts.subjseq "
            	+ "\n and    ts.userid = " + StringManager.makeSQL(v_userid);
            */
            
            sql = "\n select tg.subj, tg.year, tg.subjseq, (select subjnm from tz_subj where subj = tg.subj) as subjnm "
            	+ "\n      , tg.seq , tg.types, tg.addate "
            	+ "\n      , tg.title , tg.userid, tg.adcontent "
            	+ "\n      , get_name(ts.userid) as name, decode(tg.gadmin, 'ZZ', 'ZZ', 'P1', '강사',  '운영자') gadmin "
            	+ "\n      , tg.isimport, tsg.edustart, tsg.eduend "
            	+ "\n from   tz_gong   tg "
            	+ "\n      , tz_student ts "
            	+ "\n      , tz_subjseq tsg "
            	+ "\n where  tg.subj = ts.subj "
            	+ "\n and    tg.year = ts.year "
            	+ "\n and    tg.subjseq = ts.subjseq "
            	+ "\n and    tg.subj = tsg.subj "
            	+ "\n and    tg.year = tsg.year "
            	+ "\n and    tg.subjseq = tsg.subjseq "
            	+ "\n and    tsg.eduend >= to_char(sysdate, 'YYYYMMDDHH24') "
            	+ "\n and    ts.userid = " + StringManager.makeSQL(v_userid);
            
            
            if (!"ALL".equals(v_subj)) {
            	sql += " and    tg.subj = " + StringManager.makeSQL(v_subj);
            }
            sql += "\n order  by tg.addate desc ";

            ls = connMgr.executeQuery(sql);
            ls.setPageSize(row);                       // 페이지당 row 갯수를 세팅한다
            ls.setCurrentPage(v_pageno);               // 현재페이지번호를 세팅한다.
            
            int totalpagecount = ls.getTotalPage();    // 전체 페이지 수를 반환한다
            int totalrowcount = ls.getTotalCount();    // 전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                dbox.put("d_dispnum",   new Integer(totalrowcount - ls.getRowNum() + 1));
                dbox.put("d_totalpage", new Integer(totalpagecount));
                dbox.put("d_rowcount",  new Integer(row));
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
	 * 공지사항 등록시, 학생에게 메일발송을 보내기 위한 정보
	 * @param box
	 * @return
	 * @throws Exception
	 */  
    public int senMail(DBConnectionManager connMgr, RequestBox box) throws Exception { 
        ListSet             ls      = null;
        String              sql     = "";
        ConfigSet			conf	= new ConfigSet();
        SubjGongData		data	= null;
        int					isOk	= 0;

        try { 
            
            String v_subj = "";
            String v_year = "";
            String v_subjseq = "";

            String v_types     = box.getString("p_types");
            String v_title    = box.getString("p_title");
            String v_content   = box.getString("p_adcontent");

            String v_tmp_title = "";
            String v_tmp_content = "";
            
        	String v_checks   = box.getString("p_checks");
            StringTokenizer st = new StringTokenizer(v_checks, ",");
            String v_email = "";

            while ( st.hasMoreElements() ) { 
                 String token = st.nextToken();

                 StringTokenizer st2 = new StringTokenizer(token, "/");

                 while ( st2.hasMoreElements() ) { 
                     v_subj   = st2.nextToken();
                     v_year   = st2.nextToken();
                     v_subjseq = st2.nextToken();
                     break;
                 }
                 
                 data = getChangeText(v_subj, v_year, v_subjseq, v_title, v_content );
                 
                 if ( data != null ) { 
                	 v_tmp_title     = data.getTitle();
                	 v_tmp_content = data.getAdcontent();
                 }
                 	                 
                 sql= "\n select a.userid, b.name, b.email "
                 	+ "\n from   tz_student a, tz_member b "
                 	+ "\n where  a.subj = " + StringManager.makeSQL(v_subj)
                 	+ "\n and    a.year = " + StringManager.makeSQL(v_year)
                 	+ "\n and    a.subjseq = " + StringManager.makeSQL(v_subjseq)
                 	+ "\n and    a.userid = b.userid ";
                 ls = connMgr.executeQuery(sql);

                 box.put("p_mail_code", "003");
                 box.put("from_name", conf.getProperty("mail.admin.name"));
                 box.put("from_email", conf.getProperty("mail.admin.email"));
                 box.put("p_title", "[e-Eureka] " + v_tmp_title);
                 box.put("p_content", v_tmp_content);
                 box.put("p_map1", GetCodenm.get_subjnm(v_subj));
                 box.put("p_map2", v_types);

                 while(ls.next()) {
                 	v_email = ls.getString("email");
                	if (!"".equals(StringManager.trim(v_email))) {
	     	            Vector v_to = new Vector();
	     	            v_to.addElement(ls.getString("userid") + "|" + ls.getString("name") + "|" + v_email);
	     	            box.put("to", v_to);
	     	            
	     	            FreeMailBean bean = new FreeMailBean();
	     	            isOk = bean.amailSendMail(box);
                	 }
                 }
            }
        
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }
}