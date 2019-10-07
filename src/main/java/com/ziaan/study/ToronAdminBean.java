// **********************************************************
//  1. 제      목: TORON ADMIN BEAN
//  2. 프로그램명: ToronAdminBean.java
//  3. 개      요: 토론방 관리자 bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 박진희 2003. 7. 22
//  7. 수      정:
// **********************************************************
package com.ziaan.study;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class ToronAdminBean { 

    public ToronAdminBean() { }

    /**
    과목별 토론방 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectSubjToronList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ListSet ls2         = null;
        ArrayList list1     = null;
        ArrayList list2     = null;
        String sql1         = "";
        String sql2         = "";
        ToronData data1     = null;
        ToronData data2     = null;
        String  v_Bcourse   = ""; // 이전코스
        String  v_course    = ""; // 현재코스
        String  v_Bcourseseq= ""; // 이전코스기수
        String  v_courseseq = ""; // 현재코스기수
        int     l           = 0;
        String  ss_grcode   = box.getStringDefault("s_grcode","ALL");    // 교육그룹
        String  ss_gyear    = box.getStringDefault("s_gyear","ALL");     // 년도
        String  ss_grseq    = box.getStringDefault("s_grseq","ALL");     // 교육기수
        
		String  ss_uclass   = box.getStringDefault("s_upperclass","ALL");    // 과목분류
		String  ss_mclass   = box.getStringDefault("s_middleclass","ALL");    // 과목분류
		String  ss_lclass   = box.getStringDefault("s_lowerclass","ALL");    // 과목분류

        String  ss_subjcourse=box.getStringDefault("s_subjcourse","ALL");// 과목&코스
        String  ss_subjseq  = box.getStringDefault("s_subjseq","ALL");   // 과목 기수
        String  ss_action   = box.getString("s_action");
        
        String  v_orderColumn   = box.getString("p_orderColumn");           	// 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");           		// 정렬할 순서

		String s_userid = box.getSession("userid");
        String s_gadmin = box.getSession("gadmin");
        String v_gadmin = StringManager.substring(s_gadmin, 0, 1);

        try { 
            if ( ss_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                list2 = new ArrayList();

                // select grcode,gyear,grseq,course,scsubj,scyear,scsubjseq,scsubjnm,subj,year,subjseq,subjnm,edustart,
                // eduend,grcodenm,tpcnt,isonoff
                sql1 =  "\n select a.grcode, a.gyear, a.grseq, a.course, a.scsubj, a.scyear, a.scsubjseq, a.scsubjnm, a.subj, a.year, a.subjseq ";
                sql1 += "\n      , a.subjseqgr, a.subjnm, a.edustart, a.eduend, get_codenm('0004',a.isonoff) isonoff ";
                sql1 += "\n      , (select grcodenm from tz_grcode where grcode = a.grcode) as grcodenm ";
                sql1 += "\n      , (select count(*) from tz_torontp where subj = a.subj and year = a.year and subjseq = a.subjseq) as tpcnt, a.isonoff ";
                sql1 += "\n from   vz_scsubjseq a ";

                if (v_gadmin.equals("P")) {
                	sql1 += "\n      , tz_classtutor d ";
                }
                
                sql1 += "\n where  1 = 1 ";

                if (v_gadmin.equals("P")) {
                	sql1+="\n and    a.subj = d.subj "
                		+ "\n and    a.year = d.year "
                		+ "\n and    a.subjseq = d.subjseq "
                		+ "\n and    d.tuserid  = " + SQLString.Format(s_userid);
                }

                if ( !ss_grcode.equals("ALL") ) { 
                    sql1 += "\n and    a.grcode = '" + ss_grcode + "'";
                }
                if ( !ss_gyear.equals("ALL") ) { 
                    sql1 += "\n and    a.gyear = '" + ss_gyear + "'";
                }
                if ( !ss_grseq.equals("ALL") ) { 
                    sql1 += "\n and    a.grseq = '" + ss_grseq + "'";
                }

                if ( !ss_uclass.equals("ALL") ) { 
                    sql1 += "\n and    a.scupperclass = '" + ss_uclass + "'";
                }

                if ( !ss_mclass.equals("ALL") ) { 
                    sql1 += "\n and    a.scmiddleclass = '" + ss_mclass + "'";
                }

                if ( !ss_lclass.equals("ALL") ) { 
                    sql1 += "\n and    a.sclowerclass = '" + ss_lclass + "'";
                }


                if ( !ss_subjcourse.equals("ALL") ) { 
                    sql1 += "\n and    a.scsubj = '" + ss_subjcourse + "'";
                }
                if ( !ss_subjseq.equals("ALL") ) { 
                    sql1 += "\n and    a.scsubjseq = '" + ss_subjseq + "'";
                }


				if ( v_orderColumn.equals("") ) { 
	                sql1 += " order by a.course, a.cyear, a.courseseq, a.subj, a.year, a.subjseqgr,a.subjseq ";					
				} else { 
				    sql1 += "\n order  by a.course, a.cyear, a.courseseq, " + v_orderColumn + v_orderType;
				}
				
				
                ls1 = connMgr.executeQuery(sql1);

                    while ( ls1.next() ) { 
                        data1 = new ToronData();
                        data1.setGrcode( ls1.getString("grcode") );
                        data1.setGyear( ls1.getString("gyear") );
                        data1.setGrseq( ls1.getString("grseq") );
                        data1.setCourse( ls1.getString("course") );
                        data1.setScsubj( ls1.getString("scsubj") );
                        data1.setScyear( ls1.getString("scyear") );
                        data1.setScsubjseq( ls1.getString("scsubjseq") );
                        data1.setScsubjnm( ls1.getString("Scsubjnm") );
                        data1.setSubj( ls1.getString("subj") );
                        data1.setYear( ls1.getString("year") );
                        data1.setSubjseq( ls1.getString("subjseq") );
						data1.setSubjseqgr( ls1.getString("subjseqgr") );
                        data1.setSubjnm( ls1.getString("subjnm") );
                        data1.setEdustart( ls1.getString("edustart") );
                        data1.setEduend( ls1.getString("eduend") );
                        data1.setGrcodenm( ls1.getString("grcodenm") );
                        data1.setCnt( ls1.getInt("tpcnt") );
                        data1.setIsonoff( ls1.getString("isonoff") );
                        list1.add(data1);
                    }

                    for ( int i = 0;i < list1.size(); i++ ) { 
                        data2       =   (ToronData)list1.get(i);
                        v_course    =   data2.getCourse();
                        v_courseseq =   data2.getScsubjseq();
                        if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq))) { 
                            sql2 = "select count(subj) cnt from TZ_SUBJSEQ ";
                            sql2 += "where course = '" + v_course + "' and courseseq = '" +v_courseseq + "' ";
                            if ( !ss_grcode.equals("ALL") ) { 
                                sql2 += " and grcode = '" + ss_grcode + "'";
                            }
                            if ( !ss_gyear.equals("ALL") ) { 
                                sql2 += " and gyear = '" + ss_gyear + "'";
                            }
                            if ( !ss_grseq.equals("ALL") ) { 
                                sql2 += " and grseq = '" + ss_grseq + "'";
                            }
//                            System.out.println("sql2 ==  ==  ==  ==  ==  == > " +sql2);
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
    토론방 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectTopicList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        String sql          = "";
        ArrayList list      = null;
        ToronData data      = null;
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");      // 과목 기수
        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            // select stated,ended,title,addate,name
            sql = " select A.tpcode, A.started, A.ended, A.title, A.addate, decode(A.adgadmin, 'ZZ', 'ZZ', 'P1', '강사',  '운영자') adgadmin, ";
            sql += "        (select name from TZ_MEMBER where userid = A.aduserid) as name      ";
            sql += "   from TZ_TORONTP A ";
            sql += "  where subj='" +v_subj + "' and year='" +v_year + "' and subjseq='" +v_subjseq + "' ";
            sql += "  order by tpcode desc                                                      ";
//            System.out.println("sql ==  ==  ==  == = > " +sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new ToronData();
                data.setTpcode( ls.getString("tpcode") );
                data.setStarted( ls.getString("started") );
                data.setEnded( ls.getString("ended") );
                data.setTitle( ls.getString("title") );
                data.setName( ls.getString("name") );
                data.setAddate( ls.getString("addate") );
                data.setAdgadmin( ls.getString("adgadmin"));
                list.add(data);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
    토론방 주제 조회
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ToronData selectTopic(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls2         = null;
        String sql1         = "";
        String sql2         = "";
        int isOk            = 0;
        ToronData data2     = null;
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        String  v_countCntYn= box.getString("p_countCntYn");    // 조회수 카운트 해줌
        try { 
            connMgr = new DBConnectionManager();

            // update TZ_TORONTP
            if("Y".equals(v_countCntYn)) {
                sql1 = "update TZ_TORONTP set cnt=cnt +1 ";
                sql1 += "where subj=" +SQLString.Format(v_subj) + " and year=" +SQLString.Format(v_year);
                sql1 += " and subjseq=" +SQLString.Format(v_subjseq) + " and tpcode=" +SQLString.Format(v_tpcode);
                isOk = connMgr.executeUpdate(sql1);
            }        
                
            // select stated,ended,title,addate,adcontent,aduserid,name,cnt
            sql2 = "select A.started,A.ended,A.title,A.addate,A.adcontent,A.aduserid, decode(A.adgadmin, 'ZZ', 'ZZ', 'P1', '강사',  '운영자') adgadmin, ";
            sql2 += "(select name from TZ_MEMBER where userid = A.aduserid) as name,cnt ";
            sql2 += "from TZ_TORONTP A ";
            sql2 += "where subj=" +SQLString.Format(v_subj) + " and year=" +SQLString.Format(v_year);
            sql2 += " and subjseq=" +SQLString.Format(v_subjseq) + " and tpcode=" +SQLString.Format(v_tpcode);
//            System.out.println("sql2 ==  ==  ==  == = > " +sql2);
            ls2 = connMgr.executeQuery(sql2);
            
            if ( ls2.next() ) { 
                data2=new ToronData();
                data2.setStarted( ls2.getString("started") );
                data2.setEnded( ls2.getString("ended") );
                data2.setTitle( ls2.getString("title") );
                data2.setAddate( ls2.getString("addate") );
                data2.setAdcontent( ls2.getCharacterStream("adcontent") );
                data2.setAduserid( ls2.getString("aduserid") );
                data2.setName( ls2.getString("name") );
                data2.setCnt( ls2.getInt("cnt") );
                data2.setAdgadmin( ls2.getString("adgadmin"));
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data2;
    }
     
    /**
    토론주제 등록
    @param box      receive from the form object and session
    @return int
    */
     public int insertTopic(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt2 = null;
        String sql1         = "";
        String sql2         = "";
        ListSet ls1         = null;
        int isOk            = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_title     = box.getString("p_title");         // 토론주제
        String  v_adcontent = box.getString("p_adcontent");     // 내용clob
        String  v_started   = box.getString("p_started") +box.getString("p_stime");  // 토론 시작일
        String  v_ended     = box.getString("p_ended") +box.getString("p_ltime");    // 토론 종료일
        String  v_tpcode    = "";
        String s_gadmin     = box.getSession("gadmin");
//System.out.println(v_adcontent);
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
            String prefix = "torontp" + v_subj;         // 파일명 접두어
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
        }
        if ( !result ) { // 파일저장 실패시 
            System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
            return 0;
        }
        v_adcontent = namo.getContent(); // 최종 컨텐트 얻기
*/        
        /*********************************************************************************************/
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            // select max(tpcode)
            sql1 = "select max(to_number(tpcode)) from TZ_TORONTP";
            ls1 = connMgr.executeQuery(sql1);

            if ( ls1.next() ) { 
                v_tpcode = (StringManager.toInt( ls1.getString(1)) + 1) + "";
            }

            // insert TZ_TORONTP table
            sql2 =  "insert into TZ_TORONTP(year,subj,subjseq,tpcode,title,adcontent,aduserid,addate,started,ended,luserid,ldate, adgadmin) ";
            sql2 +=  "values (?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?,to_char(sysdate,'YYYYMMDDHH24MISS'),?)";
//            sql2 +=  "values (?, ?, ?, ?, ?, empty_clob(), ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?,to_char(sysdate,'YYYYMMDDHH24MISS'),?)";
            pstmt2 = connMgr.prepareStatement(sql2);

            pstmt2.setString(1, v_year);
            pstmt2.setString(2, v_subj);
            pstmt2.setString(3, v_subjseq);
            pstmt2.setString(4, v_tpcode);
            pstmt2.setString(5, v_title);
            pstmt2.setString(6, v_adcontent);
            // pstmt2.setCharacterStream(6,  new StringReader(v_adcontent), v_adcontent.length() );
            pstmt2.setString(7, v_user_id);
            pstmt2.setString(8, v_started);
            pstmt2.setString(9, v_ended);
            pstmt2.setString(10, v_user_id);
            pstmt2.setString(11, s_gadmin);

            isOk = pstmt2.executeUpdate();
            
            /**CLOB 처리*******************************************************************************************/
            sql2 = "select adcontent from TZ_TORONTP ";
            sql2 += "  where year     = " + StringManager.makeSQL(v_year);
            sql2 += "    and subj     = " + StringManager.makeSQL(v_subj);
            sql2 += "    and subjseq  = " + StringManager.makeSQL(v_subjseq);
            sql2 += "    and tpcode   = " + StringManager.makeSQL(v_tpcode);

            
//            connMgr.setOracleCLOB(sql2, v_adcontent);    // CLOB 처리
            
            if ( isOk > 0) {          
               if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
               if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }  
                       
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null )     { try { ls1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    토론주제 수정
    @param box      receive from the form object and session
    @return int
    */
     public int updateTopic(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        String sql1         = "";              
        String sql2         = "";
        int isOk            = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_title     = box.getString("p_title");         // 토론주제
        String  v_adcontent = box.getString("p_adcontent");     // 내용
        String  v_started   = box.getString("p_started") +box.getString("p_stime");  // 토론 시작일
        String  v_ended     = box.getString("p_ended") +box.getString("p_ltime");    // 토론 종료일
        String  v_tpcode    = box.getString("p_tpcode");

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
            String prefix = "torontp" + v_subj;         // 파일명 접두어
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
        }
        if ( !result ) { // 파일저장 실패시 
            System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
            return 0;
        }
        v_adcontent = namo.getContent(); // 최종 컨텐트 얻기
*/        
        /*********************************************************************************************/
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // update TZ_TORONTP table
            sql1 =  "update TZ_TORONTP set title=?,adcontent=?,started=?,ended=? ";
//            sql1 =  "update TZ_TORONTP set title=?,adcontent=empty_clob(),started=?,ended=? ";
            sql1 +=  "where subj=? and year=? and subjseq=? and tpcode=? ";
            pstmt1 = connMgr.prepareStatement(sql1);

            pstmt1.setString(1, v_title);
            pstmt1.setString(2, v_adcontent);
            // pstmt1.setCharacterStream(2,  new StringReader(v_adcontent), v_adcontent.length() );
            pstmt1.setString(3, v_started);
            pstmt1.setString(4, v_ended);
            pstmt1.setString(5, v_subj);
            pstmt1.setString(6, v_year);
            pstmt1.setString(7, v_subjseq);
            pstmt1.setString(8, v_tpcode);

            isOk = pstmt1.executeUpdate();

            /**CLOB 처리*******************************************************************************************/
            sql2 = "select adcontent from TZ_TORONTP ";
            sql2 += "  where year     = " + StringManager.makeSQL(v_year);
            sql2 += "    and subj     = " + StringManager.makeSQL(v_subj);
            sql2 += "    and subjseq  = " + StringManager.makeSQL(v_subjseq);
            sql2 += "    and tpcode   = " + StringManager.makeSQL(v_tpcode);
            
//            connMgr.setOracleCLOB(sql2, v_adcontent);    // CLOB 처리
            
            if ( isOk > 0) {          
               if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
               if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }              
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    토론주제 삭제
    @param box      receive from the form object and session
    @return int
    */
     public int deleteTopic(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1         = "";
        String sql2         = "";
        int isOk1           = 0;
        int isOk2           = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // delete TZ_TORONTP table
            sql1 =  "delete from TZ_TORONTP ";
            sql1 +=  "where subj=? and year=? and subjseq=? and tpcode=? ";
            pstmt1 = connMgr.prepareStatement(sql1);

            pstmt1.setString(1, v_subj);
            pstmt1.setString(2, v_year);
            pstmt1.setString(3, v_subjseq);
            pstmt1.setString(4, v_tpcode);

            isOk1 = pstmt1.executeUpdate();
            if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }

            // delete TZ_TORONTP table
            sql2 =  "delete from TZ_TORON ";
            sql2 +=  "where subj=? and year=? and subjseq=? and tpcode=? ";
            pstmt2 = connMgr.prepareStatement(sql2);

            pstmt2.setString(1, v_subj);
            pstmt2.setString(2, v_year);
            pstmt2.setString(3, v_subjseq);
            pstmt2.setString(4, v_tpcode);

            isOk2 = pstmt2.executeUpdate();
            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e1 ) { } }

            if ( isOk1 > 0) connMgr.commit();
            else connMgr.rollback();
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1;
     }


    /**
    토론글 리스트
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList selectToronList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls1         = null;
        ArrayList list1     = null;
        String sql1         = "";
        ToronData data1     = null;
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        int     l           = 0;
        try { 
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();

            // select seq,title,aduserid,addate,levels,name
            sql1 = "select seq,title,aduserid,addate,levels,cnt,agree,contrary, decode(A.adgadmin, 'ZZ', 'ZZ', 'P1', '강사',  '운영자') adgadmin, ";
            sql1 += "(select name from TZ_MEMBER where userid = A.aduserid) as name ";
            sql1 += " from TZ_TORON A ";
            sql1 += "where subj=" +SQLString.Format(v_subj) + " and year=" +SQLString.Format(v_year);
            sql1 += " and subjseq=" +SQLString.Format(v_subjseq) + " and tpcode=" +SQLString.Format(v_tpcode);
            sql1 += " order by A.refseq desc, A.position asc ";
//            System.out.println("sql1 ==  ==  ==  ==  ==  == > " +sql1);
            ls1 = connMgr.executeQuery(sql1);

                while ( ls1.next() ) { 
                    data1 = new ToronData();
                    data1.setSeq( ls1.getInt("seq") );
                    data1.setTitle( ls1.getString("title") );
                    data1.setAduserid( ls1.getString("aduserid") );
                    data1.setAddate( ls1.getString("addate") );
                    data1.setLevels( ls1.getInt("levels") );
                    data1.setName( ls1.getString("name") );
                    data1.setAgree( ls1.getInt("agree"));
                    data1.setContrary( ls1.getInt("contrary"));
                    data1.setCnt( ls1.getInt("cnt"));
                    data1.setAdgadmin( ls1.getString("adgadmin"));
                    
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
      * 토론글 리스트(tpcode와 관계없이...)
      * @param box
      * @return
      * @throws Exception
      */
     public ArrayList selectToronList2(RequestBox box) throws Exception { 
    	 DBConnectionManager	connMgr	= null;
    	 ListSet ls1         = null;
    	 ArrayList list1     = null;
    	 String sql1         = "";
    	 ToronData data1     = null;
    	 String  v_year      = box.getString("p_year");          // 년도
    	 String  v_subj      = box.getString("p_subj");          // 과목
    	 String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
    	 String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
    	 String  v_userid    = box.getString("p_userid");
    	 int     l           = 0;
    	 try { 
    		 connMgr = new DBConnectionManager();
    		 list1 = new ArrayList();
    		 
    		 // select seq,title,aduserid,addate,levels,name
    		 sql1 = "select seq,title,aduserid,addate,levels,cnt,agree,contrary, tpcode,decode(A.adgadmin, 'ZZ', 'ZZ', 'P1', '강사',  '운영자') adgadmin, \n ";
    		 sql1 += "(select name from TZ_MEMBER where userid = A.aduserid) as name \n ";
    		 sql1 += " from TZ_TORON A \n ";
    		 sql1 += "where subj=" +SQLString.Format(v_subj) + " and year=" +SQLString.Format(v_year) + " \n ";
    		 sql1 += " and subjseq = " +SQLString.Format(v_subjseq) + " \n ";
    		 sql1 += " and aduserid = " +SQLString.Format(v_userid) + " \n ";
    		 sql1 += " order by A.refseq desc, A.position asc \n ";
//             System.out.println("sql1 ==  ==  ==  ==  ==  == > " +sql1);
    		 ls1 = connMgr.executeQuery(sql1);
    		 
    		 while ( ls1.next() ) { 
    			 data1 = new ToronData();
    			 data1.setSeq( ls1.getInt("seq") );
    			 data1.setTpcode(ls1.getString("tpcode"));
    			 data1.setTitle( ls1.getString("title") );
    			 data1.setAduserid( ls1.getString("aduserid") );
    			 data1.setAddate( ls1.getString("addate") );
    			 data1.setLevels( ls1.getInt("levels") );
    			 data1.setName( ls1.getString("name") );
    			 data1.setAgree( ls1.getInt("agree"));
    			 data1.setContrary( ls1.getInt("contrary"));
    			 data1.setCnt( ls1.getInt("cnt"));
    			 data1.setAdgadmin( ls1.getString("adgadmin"));
    			 
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

     public DataBox selectToronStudentScore(RequestBox box) throws Exception { 
    	 DBConnectionManager	connMgr	= null;
    	 ListSet             ls      = null;
    	 String              sql     = "";
    	 DataBox dbox = null;
    	 
    	 String  v_year      = box.getString("p_year");          // 년도
    	 String  v_subj      = box.getString("p_subj");          // 과목
    	 String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
    	 String  v_userid    = box.getString("p_userid");
    	 
    	 try { 
    		 connMgr = new DBConnectionManager();
    		 
    		 sql =  " select a.userid, a.etc1, nvl(b.isgraduated,'-') as isgraduated ";
    		 sql += "   from tz_student a, tz_stold b ";
    		 sql += "  where a.subj=b.subj(+)";
    		 sql += "    and a.year=b.year(+)";
    		 sql += "    and a.subjseq=b.subjseq(+)";
    		 sql += "    and a.userid=b.userid(+)";
    		 sql += "    and a.subj    = " + StringManager.makeSQL(v_subj);
    		 sql += "    and a.year    = " + StringManager.makeSQL(v_year);
    		 sql += "    and a.subjseq = " + StringManager.makeSQL(v_subjseq);
    		 sql += "    and a.userid   = " + StringManager.makeSQL(v_userid);
    		 
    		 ls = connMgr.executeQuery(sql);
    		 
    		 while ( ls.next() ) { 
    			 dbox = ls.getDataBox();
    		 }
    	 } catch ( Exception ex ) { 
    		 ErrorManager.getErrorStackTrace(ex);
    		 throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
    	 } finally { 
    		 if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
    		 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
    	 }
    	 
    	 return dbox;
     }
     
    /**
    토론글 조회
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ToronData selectToron(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet ls2         = null;       
        String sql1         = "";
        String sql2         = "";
        ToronData data2     = null;
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        int     v_seq       = box.getInt("p_seq");              // 일련번호
        String s_userid     = box.getSession("userid");
        int     isOk1       = 0;
        int     l           = 0;
        try { 
            connMgr = new DBConnectionManager();
            
            // update TZ_TORONTP
            sql1 = "update TZ_TORON set cnt=cnt +1 ";
            sql1 += "where subj=" +SQLString.Format(v_subj) + " and year=" +SQLString.Format(v_year);
            sql1 += " and subjseq=" +SQLString.Format(v_subjseq) + " and tpcode=" +SQLString.Format(v_tpcode);
            sql1 += " and seq=" +SQLString.Format(v_seq);            
            isOk1 = connMgr.executeUpdate(sql1);
                        
            // select seq,refseq,levels,position,title,adcontent,aduserid,cnt,addate,name
            sql2 = "select seq,refseq,levels,position,title,adcontent,aduserid,cnt,addate,agree,contrary, decode(A.adgadmin, 'ZZ', 'ZZ', 'P1', '강사',  '운영자') adgadmin, \n"+
            "       (                                                   \n"+
            "           select decode(count(*), 0, 'Y', 'N')            \n"+
            "           from TZ_TORON c, TZ_TORON d                     \n"+
            "           where c.refseq = d.refseq                       \n"+
            "           and d.levels = (c.levels +1)                    \n"+
            "           and d.position = (c.position +1)                \n"+
            "           and c.seq = a.seq                               \n"+      
            "       ) delYn, (                                          \n"+
            "           select count(*)                                 \n"+
            "           from tz_board_agreeinfo                         \n"+
            "           where subjseq = A.subjseq                       \n"+
            "           and subj = A.subj                               \n"+
            "           and year = A.year                               \n"+
            "           and tpcode = A.tpcode                           \n"+
            "           and seq = A.seq                                 \n"+
            "           and userid = " +SQLString.Format(s_userid)       +
            "       ) agree_count,                                       \n";
            sql2 += "(select name from TZ_MEMBER where userid = A.aduserid) as name \n";           
            sql2 += " from TZ_TORON A \n";
            sql2 += "where subj=" +SQLString.Format(v_subj) + " and year=" +SQLString.Format(v_year);
            sql2 += " and subjseq=" +SQLString.Format(v_subjseq) + " and tpcode=" +SQLString.Format(v_tpcode);
            sql2 += " and seq=" +SQLString.Format(v_seq);
            System.out.println("sql2 ==  ==  ==  ==  ==  == > " +sql2);
            ls2 = connMgr.executeQuery(sql2);
            
            if ( ls2.next() ) {                    
                data2=new ToronData();
                data2.setSeq( ls2.getInt("seq") );
                data2.setRefseq( ls2.getInt("refseq") );
                data2.setLevels( ls2.getInt("levels") );
                data2.setPosition( ls2.getInt("position") );
                data2.setTitle( ls2.getString("title") );
                data2.setAdcontent( ls2.getCharacterStream("adcontent") );
                data2.setAduserid( ls2.getString("aduserid") );
                data2.setCnt( ls2.getInt("cnt") );
                data2.setAddate( ls2.getString("addate") );
                data2.setName( ls2.getString("name") );                
                data2.setAgree( ls2.getInt("agree"));
                data2.setContrary( ls2.getInt("contrary"));
                data2.setDelYn( ls2.getString("delYn"));
                data2.setAdgadmin( ls2.getString("adgadmin"));
                data2.setAgreecount( ls2.getInt("agree_count"));
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return data2;
    }    


    /**
    토론글 등록
    @param box      receive from the form object and session
    @return int
    */
     public int insertToron(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt2 = null;
        String sql1         = "";
        String sql2         = "";
        ListSet ls1         = null;
        int isOk            = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        String  v_title     = box.getString("p_title");         // 제목
        String  v_adcontent = box.getString("p_adcontent");     // 내용
        String  s_gadmin    = box.getSession("gadmin");
        int     v_seq       = 0;

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
            String prefix = "torontp" + v_subj;         // 파일명 접두어
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
        }
        if ( !result ) { // 파일저장 실패시 
            System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
            return 0;
        }
        v_adcontent = namo.getContent(); // 최종 컨텐트 얻기
*/        
        /*********************************************************************************************/
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            // select max(seq)
            sql1 = "select nvl(max(seq), 0) from TZ_TORON ";
            ls1 = connMgr.executeQuery(sql1);
            if ( ls1.next() ) { v_seq = ls1.getInt(1) + 1;    }
            ls1.close();

            // insert TZ_TORON table
            sql2 =  "insert into TZ_TORON(subj,year,subjseq,tpcode,seq,refseq,title,adcontent,aduserid,addate,luserid,ldate, agree, contrary, adgadmin) ";
            sql2 +=  "values (?, ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?, to_char(sysdate,'YYYYMMDDHH24MISS'), 0, 0, ?)";
//            sql2 +=  "values (?, ?, ?, ?, ?, ?, ?, empty_clob(), ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?, to_char(sysdate,'YYYYMMDDHH24MISS'), 0, 0, ?)";
            pstmt2 = connMgr.prepareStatement(sql2);

            pstmt2.setString(1, v_subj);
            pstmt2.setString(2, v_year);
            pstmt2.setString(3, v_subjseq);
            pstmt2.setString(4, v_tpcode);
            pstmt2.setInt(5, v_seq);
            pstmt2.setInt(6, v_seq);
            pstmt2.setString(7, v_title);
            pstmt2.setString(8, v_adcontent);
            // pstmt2.setCharacterStream(8,  new StringReader(v_adcontent), v_adcontent.length() );
            pstmt2.setString(9, v_user_id);
            pstmt2.setString(10, v_user_id);
            pstmt2.setString(11, s_gadmin);

            isOk = pstmt2.executeUpdate();

            /**CLOB 처리*******************************************************************************************/
            sql2 = "select adcontent from TZ_TORON ";
            sql2 += "  where year     = " + StringManager.makeSQL(v_year);
            sql2 += "    and subj     = " + StringManager.makeSQL(v_subj);
            sql2 += "    and subjseq  = " + StringManager.makeSQL(v_subjseq);
            sql2 += "    and tpcode   = " + StringManager.makeSQL(v_tpcode);
            sql2 += "    and seq      = " + v_seq;

            
//            connMgr.setOracleCLOB(sql2, v_adcontent);    // CLOB 처리
            
            if ( isOk > 0) {          
               if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
               if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }  
                        
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls1 != null )     { try { ls1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null )  { try { pstmt2.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    토론글 수정
    @param box      receive from the form object and session
    @return int
    */
     public int updateToron(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        String sql1         = "";
        String sql2         = "";
        int isOk            = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        int     v_seq       = box.getInt("p_seq");              // 의견일련번호
        String  v_title     = box.getString("p_title");         // 토론주제
        String  v_adcontent = box.getString("p_adcontent");     // 내용
        String  v_started   = box.getString("p_started") +box.getString("p_stime");  // 토론 시작일
        String  v_ended     = box.getString("p_ended") +box.getString("p_ltime");    // 토론 종료일

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
            String prefix = "torontp" + v_subj;         // 파일명 접두어
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
        }
        if ( !result ) { // 파일저장 실패시 
            System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
            return 0;
        }
        v_adcontent = namo.getContent(); // 최종 컨텐트 얻기
*/        
        /*********************************************************************************************/
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // update TZ_TORONTP table
            sql1 =  "update TZ_TORON set title=?,adcontent=? ";
//            sql1 =  "update TZ_TORON set title=?,adcontent=empty_clob() ";
            sql1 +=  "where subj=? and year=? and subjseq=? and tpcode=? and seq=? ";
            pstmt1 = connMgr.prepareStatement(sql1);

            pstmt1.setString(1, v_title);
            pstmt1.setString(2, v_adcontent);
            // pstmt1.setCharacterStream(2,  new StringReader(v_adcontent), v_adcontent.length() );
            pstmt1.setString(3, v_subj);
            pstmt1.setString(4, v_year);
            pstmt1.setString(5, v_subjseq);
            pstmt1.setString(6, v_tpcode);
            pstmt1.setInt   (7, v_seq);

            isOk = pstmt1.executeUpdate();

            /**CLOB 처리*******************************************************************************************/
            sql2 = "select adcontent from TZ_TORON ";
            sql2 += "  where year     = " + StringManager.makeSQL(v_year);
            sql2 += "    and subj     = " + StringManager.makeSQL(v_subj);
            sql2 += "    and subjseq  = " + StringManager.makeSQL(v_subjseq);
            sql2 += "    and tpcode   = " + StringManager.makeSQL(v_tpcode);
            sql2 += "    and seq      = " + v_seq;

            
//            connMgr.setOracleCLOB(sql2, v_adcontent);    // CLOB 처리
            
            if ( isOk > 0) {          
               if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
               if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }              
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    토론 답글 등록
    @param box      receive from the form object and session
    @return int
    */
     public int insertToronReply(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt3 = null;
        String sql1         = "";
        String sql2         = "";
        String sql3         = "";
        ListSet ls2         = null;
        int isOk1           = 0;
        int isOk3           = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        String  v_title     = box.getString("p_title");         // 제목
        String  v_adcontent = box.getString("p_adcontent");     // 내용
        int     v_refseq    = box.getInt("p_refseq");           // 상위글 번호
        int     v_levels    = box.getInt("p_levels");
        int     v_position  = box.getInt("p_position");
        int     v_seq       = 0;

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
            String prefix = "torontp" + v_subj;         // 파일명 접두어
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
        }
        if ( !result ) { // 파일저장 실패시 
            System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
            return 0;
        }
        v_adcontent = namo.getContent(); // 최종 컨텐트 얻기
*/        
        /*********************************************************************************************/
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            // 기존 답변글 위치 한칸밑으로 변경
            sql1  = "update TZ_TORON ";
            sql1 += "   set position = position + 1 ";
            sql1 += " where refseq   = ? ";
            sql1 += "   and position > ? ";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_refseq);
            pstmt1.setInt(2, v_position);
            isOk1 = pstmt1.executeUpdate();

            // select max(seq)
            sql2 = "select max(seq) from TZ_TORON";
            ls2 = connMgr.executeQuery(sql2);
            if ( ls2.next() ) { 
                v_seq = ls2.getInt(1) + 1;
            }

            // insert TZ_TORON table
            sql3 =  "insert into TZ_TORON(subj,year,subjseq,tpcode,seq,refseq,levels,position,title,adcontent,aduserid,addate,luserid,ldate, agree, contrary) ";
            sql3 +=  "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?, to_char(sysdate,'YYYYMMDDHH24MISS'), 0, 0)";
//            sql3 +=  "values (?, ?, ?, ?, ?, ?, ?, ?, ?, empty_clob(), ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?, to_char(sysdate,'YYYYMMDDHH24MISS'), 0, 0)";
            pstmt3 = connMgr.prepareStatement(sql3);

            pstmt3.setString(1, v_subj);
            pstmt3.setString(2, v_year); 
            pstmt3.setString(3, v_subjseq);
            pstmt3.setString(4, v_tpcode);
            pstmt3.setInt(5, v_seq);
            pstmt3.setInt(6, v_refseq);
            pstmt3.setInt(7, v_levels + 1);
            pstmt3.setInt(8, v_position + 1);
            pstmt3.setString(9, v_title);
            pstmt3.setString(10, v_adcontent);
            // pstmt3.setCharacterStream(10,  new StringReader(v_adcontent), v_adcontent.length() );
            pstmt3.setString(11, v_user_id);
            pstmt3.setString(12, v_user_id);

            isOk3 = pstmt3.executeUpdate();


            /**CLOB 처리*******************************************************************************************/
            sql2 = "select adcontent from TZ_TORON ";
            sql2 += "  where year     = " + StringManager.makeSQL(v_year);
            sql2 += "    and subj     = " + StringManager.makeSQL(v_subj);
            sql2 += "    and subjseq  = " + StringManager.makeSQL(v_subjseq);
            sql2 += "    and tpcode   = " + StringManager.makeSQL(v_tpcode);
            sql2 += "    and seq      = " + v_seq;

//            connMgr.setOracleCLOB(sql2, v_adcontent);    // CLOB 처리
            
            if ( isOk3 > 0) {          
               if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
               if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }              
        } catch ( Exception ex ) { 
            connMgr.rollback(); 
            ErrorManager.getErrorStackTrace(ex, box, sql3);
            throw new Exception("sql = " + sql3 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls2 != null )     { try { ls2.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt3 != null )  { try { pstmt3.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk3;
    }


    /**
    토론글 삭제
    @param box      receive from the form object and session
    @return int
    */
     public int deleteToron(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        String sql1         = "";
        int isOk            = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        int     v_seq       = box.getInt("p_seq");              // 의견일련번호

        try { 
            connMgr = new DBConnectionManager();

            // 답변 유무 체크(답변 있을시 삭제불가)
            if ( this.selectToronReply(v_seq) == 0 ) { 

                // delete TZ_TORONTP table
                sql1 =  "delete from TZ_TORON ";
                sql1 +=  "where subj=? and year=? and subjseq=? and tpcode=? and seq=?";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setString(1, v_subj);
                pstmt1.setString(2, v_year);
                pstmt1.setString(3, v_subjseq);
                pstmt1.setString(4, v_tpcode);
                pstmt1.setInt(5, v_seq);

                isOk = pstmt1.executeUpdate();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

   /**
   * 삭제시 하위 답변 유무 체크
   * @param seq          게시판 번호
   * @return result      0 : 답변 없음,    1 : 답변 있음
   * @throws Exception
   */
   public int selectToronReply(int seq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        int result = 0;

        try { 
            connMgr = new DBConnectionManager();

            sql  = "  select count(*) cnt                 ";
            sql += "  from                                ";
            sql += "    (select refseq, levels, position  ";
            sql += "      from TZ_TORON                   ";
            sql += "     where seq = " + seq;
            sql += "     ) a, TZ_TORON b                  ";
            sql += " where a.refseq = b.refseq            ";
            sql += "   and b.levels = (a.levels +1)        ";
            sql += "   and b.position = (a.position +1)    ";
//            System.out.println("sql ==  ==  ==  ==  ==  ==  == = > " +sql);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                result = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return result;
    }
   
   /**
   찬성 반대 카운트 업데이트
   @param box      receive from the form object and session
   @return int
   */
    public int updateColumn(RequestBox box) throws Exception { 
        DBConnectionManager  connMgr = null;
        PreparedStatement pstmt1 = null;
        String sql1         = "";
        
        ListSet ls2         = null;
        String sql2         = "";
        String sql3         = "";
        int v_seq2          = 0;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt = null;
        int isOk3            = 0;
        
        int isOk            = 0;
        String  v_user_id   = box.getSession("userid");
        String  v_year      = box.getString("p_year");          // 년도
        String  v_subj      = box.getString("p_subj");          // 과목
        String  v_subjseq   = box.getString("p_subjseq");       // 과목 기수
        String  v_tpcode    = box.getString("p_tpcode");        // 토론주제코드
        int     v_seq       = box.getInt("p_seq");              // 의견일련번호
        String  v_title     = box.getString("p_title");         // 토론주제
        String  v_adcontent = box.getString("p_adcontent");     // 내용
        String  v_started   = box.getString("p_started") +box.getString("p_stime");  // 토론 시작일
        String  v_ended     = box.getString("p_ended") +box.getString("p_ltime");    // 토론 종료일
        String  v_column    = box.getString("p_column");
        String  v_gubun = "N";
        
        if("AGREE".equals(v_column)) {
            v_gubun = "Y";
        } 
          
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sql1 =  "update TZ_TORON set " + v_column + " = " + v_column + " + 1 ";
            sql1 +=  "where subj=? and year=? and subjseq=? and tpcode=? and seq=? ";
            pstmt1 = connMgr.prepareStatement(sql1);

            /*pstmt1.setString(1, v_column);
            pstmt1.setString(2, v_column);*/
            pstmt1.setString(1, v_subj);
            pstmt1.setString(2, v_year);
            pstmt1.setString(3, v_subjseq);
            pstmt1.setString(4, v_tpcode);
            pstmt1.setInt   (5, v_seq);

            isOk = pstmt1.executeUpdate();
            
            sql2 = "select max(recseq) from tz_board_agreeinfo     \n"
                 + "where subj = ?                                 \n"
                 + "and year = ?                                   \n"
                 + "and subjseq = ?                                \n"
                 + "and tpcode = ?                                 \n"
                 + "and seq = ?                                    \n";                 
               
            pstmt = connMgr.prepareStatement(sql2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setString(1, v_subj);
            pstmt.setString(2, v_year);
            pstmt.setString(3, v_subjseq);
            pstmt.setString(4, v_tpcode);
            pstmt.setInt(5, v_seq);
            ls2 = new ListSet(pstmt);        //      ListSet (ResultSet) 객체생성
            if ( ls2.next() ) { 
                v_seq2 = ls2.getInt(1) + 1;
            }

            // insert tz_board_agreeinfo table // 찬성, 반대 이력
            sql3 =  "insert into tz_board_agreeinfo(subj, year, subjseq, tpcode, seq, recseq, userid, indate, gubun) ";
            sql3 +=  "values (?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?)";
            pstmt3 = connMgr.prepareStatement(sql3);

            pstmt3.setString(1, v_subj);
            pstmt3.setString(2, v_year);
            pstmt3.setString(3, v_subjseq);
            pstmt3.setString(4, v_tpcode);
            pstmt3.setInt(5, v_seq);
            pstmt3.setInt(6, v_seq2);
            pstmt3.setString(7, v_user_id);
            pstmt3.setString(8, v_gubun);
            
            isOk3 = pstmt3.executeUpdate();             
            
            if ( isOk > 0) {          
               if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
            } else { 
               if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
            }              
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null )  { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt3 != null )  { try { pstmt3.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
   }
}