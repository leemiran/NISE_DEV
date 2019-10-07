// **********************************************************
//  1. 제      목: 과목별질문방
//  2. 프로그램명: QnaByCourseBean.java
//  3. 개      요: 과목별질문방
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 
//  7. 수      정: 
// **********************************************************

package com.ziaan.study;

import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import oracle.sql.CLOB;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

public class QnaByCourseBean { 
    private static final String FILE_TYPE = "p_file";           //      파일업로드되는 tag name
    private static final int FILE_LIMIT = 1;                    //    페이지에 세팅된 파일첨부 갯수
    private ConfigSet config;
    private int row;

    public QnaByCourseBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    과목자료실 관리자 리스트
    @param box      receive from the form object and session
    @return ArrayList   
    */
     public ArrayList selectAdminList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet ls1         = null;       
        ListSet ls2         = null;
        ArrayList list1     = null;
        ArrayList list2     = null;
        String sql1         = "";
        String sql2         = "";
        ProjectData data1   = null;
        ProjectData data2   = null;
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
        
        String  v_orderColumn   = box.getString("p_orderColumn");               // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");                 // 정렬할 순서

		String s_userid = box.getSession("userid");
        String s_gadmin = box.getSession("gadmin");
        String v_gadmin = StringManager.substring(s_gadmin, 0, 1);
        
        try { 
            if ( ss_action.equals("go") ) {  
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                list2 = new ArrayList();                   
                                
                // select course,cyear,courseseq,coursenm,subj,isclosed,subjnm,year,subjseq,isonoff,cnt,tabseq
               //  sql1 = "select A.course,A.cyear,A.courseseq,A.coursenm,A.subj,A.isclosed,A.subjnm,A.year,A.subjseq,a.subjseqgr,A.isonoff,edustart,eduend, get_grcodenm(a.grcode) as grcodenm, ";
               //  sql1 += "(select count(seq) from TZ_BOARD where tabseq=B.tabseq) cnt,B.tabseq ";
               //  sql1 += "from VZ_SCSUBJSEQ A,TZ_BDS B where A.subj=B.subj and a.year=b.year and a.subjseq=b.subjseq and B.type='SQ' ";
                
                sql1= "\n select a.course,a.cyear,a.courseseq,a.coursenm,a.subj,a.subjnm,a.year,a.subjseq,a.subjseqgr,a.isonoff,a.isclosed,edustart,eduend  "
                	+ "\n      , (select count(distinct refseq) from tz_board "
                	+ "\n         where  tabseq = b.tabseq "
                	+ "\n         and    subj = a.subj "
	                + "\n         and    year = a.year "
	                + "\n         and    subjseq = a.subjseq ";

                sql1+="\n        ) cnt "
                	+ "\n      , b.tabseq, get_grcodenm(a.grcode) as grcodenm "
                	+ "\n from   vz_scsubjseq a "
                	+ "\n      , tz_bds b ";

                if (v_gadmin.equals("P")) {
                	sql1+="\n      , tz_classtutor d ";
                }
                
                sql1+="\n where  a.subj = b.subj "
               		+ "\n and    a.year = b.year "
            		+ "\n and    a.subjseq = b.subjseq "
                	+ "\n and    b.type='SQ' "
                	+ "\n and    a.gyear = '" + ss_gyear + "'";


                if (v_gadmin.equals("P")) {
                	sql1+="\n and    a.subj = d.subj(+) "
                		+ "\n and    a.year = d.year(+) "
                		+ "\n and    a.subjseq = d.subjseq(+) "
                		+ "\n and    d.tuserid(+)  = " + SQLString.Format(s_userid);
                }
                
            	sql1+="\n and  0 < (select count(distinct refseq) from tz_board   "
            		+ "\n            where  tabseq = b.tabseq "
            		+ "\n              and    subj = a.subj   "
            		+ "\n              and    year = a.year   "
            	    + "\n              and    subjseq = a.subjseq) ";
                

                 if ( !ss_grcode.equals("ALL") ) { 
                 	sql1 += "\n and    a.grcode = '" + ss_grcode + "'";
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
                    sql1 += "\n and    a.subj = '" + ss_subjcourse + "'";
                }
                if ( !ss_subjseq.equals("ALL") ) { 
                	sql1 += "\n and    a.subjseq = '" + ss_subjseq + "'";
                }
                
				if ( v_orderColumn.equals("") ) { 
	                sql1 += "\n order by a.course, a.cyear, a.courseseq, a.subj, a.year, a.subjseqgr,a.subjseq ";					
				} else { 
				    sql1 += "\n order  by a.course, a.cyear, a.courseseq, " + v_orderColumn + v_orderType;
				}
            
				System.out.println(sql1);
				
                ls1 = connMgr.executeQuery(sql1);
                
                    while ( ls1.next() ) { 
                        data1 = new ProjectData();
                        data1.setCourse( ls1.getString("course") );
                        data1.setCyear( ls1.getString("cyear") );
                        data1.setCourseseq( ls1.getString("courseseq") );
                        data1.setCoursenm( ls1.getString("coursenm") );                    
                        data1.setSubj( ls1.getString("subj") );
                        data1.setYear( ls1.getString("year") );              
                        data1.setSubjseq( ls1.getString("subjseq") );
                        data1.setSubjseqgr( ls1.getString("subjseqgr") );
                        data1.setSubjnm( ls1.getString("subjnm") );
                        data1.setIsclosed( ls1.getString("isclosed") );                            
                        data1.setIsonoff( ls1.getString("isonoff") );    
                        data1.setEdustart( ls1.getString("edustart") );
                        data1.setEduend( ls1.getString("eduend") );                        
                        data1.setCnt( ls1.getInt("cnt") );        
                        data1.setTabseq( ls1.getInt("tabseq") );     
                        data1.setGrcodemn(ls1.getString("grcodenm"));
                        
                        list1.add(data1);
                    }
                    
                     for ( int i = 0;i < list1.size(); i++ ) { 
                        data2       =   (ProjectData)list1.get(i);
                        v_course    =   data2.getCourse();
                        v_courseseq =   data2.getCourseseq();
                        if ( !v_course.equals("000000") && !(v_Bcourse.equals(v_course) && v_Bcourseseq.equals(v_courseseq))) { 
                            sql2 = "select count(A.subj) cnt from TZ_SUBJSEQ A, TZ_BDS B ";
                            sql2 += "where A.subj=B.subj and a.year=b.year and a.subjseq=b.subjseq and B.type='SQ' and A.course = '" + v_course + "' and A.courseseq = '" +v_courseseq + "' ";
                          if ( !ss_grcode.equals("ALL") ) { 
                              sql2 += " and A.grcode = '" + ss_grcode + "'";
                          } 
                          if ( !ss_gyear.equals("ALL") ) { 
                              sql2 += " and A.gyear = '" + ss_gyear + "'";
                          } 
                          if ( !ss_grseq.equals("ALL") ) { 
                              sql2 += " and A.grseq = '" + ss_grseq + "'";
                          }
                     
                          if ( !ss_uclass.equals("ALL") ) { 
                              sql2 += " and A.scupperclass = '" + ss_uclass + "'";
                          }
                     
                          if ( !ss_mclass.equals("ALL") ) { 
                              sql2 += " and A.scmiddleclass = '" + ss_mclass + "'";
                          }
                          if ( !ss_lclass.equals("ALL") ) { 
                              sql2 += " and A.sclowerclass = '" + ss_lclass + "'";
                          }
                          if ( !ss_subjcourse.equals("ALL") ) { 
                              sql2 += " and A.scsubj = '" + ss_subjcourse + "'";
                          }
                          if ( !ss_subjseq.equals("ALL") ) { 
                              sql2 += " and A.scsubjseq = '" + ss_subjseq + "'";
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

        return list1;        
    }

    /**
    * 자료실 리스트화면 select
    * @param    box          receive from the form object and session
    * @return ArrayList  자료실 리스트
    * @throws Exception
    */
    public ArrayList selectDocList(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;


        int v_tabseq = box.getInt("p_tabseq");
        // int v_pageno = box.getInt("p_pageno");
        String v_searchtext = box.getString("p_searchtext");
        String v_search     = box.getString("p_search");


        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select a.seq seq, a.userid userid, a.name name, a.title title, count(b.realfile) filecnt, decode(a.gadmin, 'ZZ', 'ZZ', 'P1', '강사',  '운영자') gadmin, ";
            sql += "        a.indate indate, a.cnt cnt, a.refseq refseq, a.levels levels, a.position position, isopen ";
            sql += "   from TZ_BOARD a, TZ_BOARDFILE b                                                         ";
            sql += "  where a.tabseq = b.tabseq( +)                                                             ";
            sql += "    and a.seq    = b.seq( +)                                                                ";
            sql += "    and a.tabseq = " + v_tabseq;

            if ( !v_searchtext.equals("") ) {                //    검색어가 있으면
                if ( v_search.equals("name") ) {              //    이름으로 검색할때
                    sql += " and a.name like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
                else if ( v_search.equals("title") ) {        //    제목으로 검색할때
                    sql += " and a.title like " + StringManager.makeSQL("%" + v_searchtext + "%");
                }
                else if ( v_search.equals("content") ) {     //    내용으로 검색할때
                    sql += " and dbms_lob.instr(a.content, " + StringManager.makeSQL(v_searchtext) + ",1,1) < > 0";
                }                

            }
            sql += " group by a.seq, a.userid, a.name, a.title, a.indate, a.cnt, a.refseq, a.levels, a.position, decode(A.gadmin, 'ZZ', 'ZZ', 'P1', '강사',  '운영자') ";
            sql += " order by a.refseq desc, position asc                                                       ";

            ls = connMgr.executeQuery(sql);

            // ls.setPageSize(row);                       //  페이지당 row 갯수를 세팅한다
            // ls.setCurrentPage(v_pageno);               //     현재페이지번호를 세팅한다.
            // int totalpagecount = ls.getTotalPage();    //     전체 페이지 수를 반환한다
            // int totalrowcount = ls.getTotalCount();    //     전체 row 수를 반환한다

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                // dbox.put("d_dispnum",        new Integer(totalrowcount - ls.getRowNum() + 1));
                // dbox.put("d_totalpagecount", new Integer(totalpagecount));
                // dbox.put("d_rowcount",       new Integer(row));
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
   * 선택된 자료실 게시물 상세내용 select
   * @param box          receive from the form object and session
   * @return ArrayList   조회한 상세정보
   * @throws Exception
   */
   public DataBox selectBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        int v_tabseq = box.getInt("p_tabseq");
        int v_seq = box.getInt("p_seq");
        int v_upfilecnt = (box.getInt("p_upfilecnt") > 0?box.getInt("p_upfilecnt"):1);

       // String [] realfile = new String [v_upfilecnt];
       // String [] savefile= new String [v_upfilecnt];
       // int [] fileseq = new int [v_upfilecnt];
        Vector realfileVector = new Vector();
        Vector savefileVector = new Vector();
        Vector fileseqVector  = new Vector();        

        try { 
            connMgr = new DBConnectionManager();


            sql  = " select a.seq seq, a.userid userid, a.name name, a.title title, b.fileseq fileseq, b.realfile realfile, a.content content, decode(a.gadmin, 'ZZ', 'ZZ', 'P1', '강사',  '운영자') gadmin, ";
            sql += "        b.savefile savefile, a.indate indate, a.cnt cnt, a.refseq refseq, a.levels levels, a.position position, isopen ";
            sql += " from TZ_BOARD a, TZ_BOARDFILE b                                                                                            ";
            sql += "  where a.tabseq = b.tabseq( +)                                                                                              ";
            sql += "    and a.seq    = b.seq( +)                                                                                                 ";
            sql += "    and a.tabseq = " + v_tabseq;            
            sql += "    and a.seq    = " + v_seq;

            ls = connMgr.executeQuery(sql);


            for ( int i = 0; ls.next(); i++ ) { 

                dbox = ls.getDataBox();

                realfileVector.addElement( ls.getString("realfile") );
                savefileVector.addElement( ls.getString("savefile") );
                fileseqVector.addElement(String.valueOf( ls.getInt("fileseq")));
            }
            if ( realfileVector      != null ) dbox.put("d_realfile", realfileVector);
            if ( savefileVector      != null ) dbox.put("d_savefile", savefileVector);
            if ( fileseqVector   != null ) dbox.put("d_fileseq", fileseqVector);

            connMgr.executeUpdate("update TZ_BOARD set cnt = cnt + 1 where  tabseq = " + v_tabseq + " and seq = " + v_seq);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }


    /**
    * 새로운 자료실 내용 등록
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
       public int insertBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;

        ResultSet rs1 = null;
        Statement stmt1 = null;
        PreparedStatement pstmt1  = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int v_seq = 0;
        ListSet ls1         = null;

        int    v_tabseq  = box.getInt("p_tabseq");
        String v_title   = box.getString("p_title");
        String v_content = box.getString("p_content"); // 내용 clob

        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name"); 
        String s_gadmin = box.getSession("gadmin");
        String v_isopen = box.getString("p_isopen");
        String v_sangdamgubun = box.getString("p_sangdamgubun");

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
            String prefix = "studyboard" + v_tabseq;         // 파일명 접두어
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
        }
        if ( !result ) { // 파일저장 실패시 
            System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
            return 0;
        }
        v_content = namo.getContent(); // 최종 컨텐트 얻기
*/        
        /*********************************************************************************************/
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // ----------------------   게시판 번호 가져온다 ----------------------------
            sql = " select nvl(max(seq), 0) from TZ_BOARD where tabseq = '" +v_tabseq + "' ";
            ls1 = connMgr.executeQuery(sql);
            if ( ls1.next() ) { 
                v_seq = ls1.getInt(1) + 1;
            }
            ls1.close();
            // --------------------------------------------------------------------------

            // ----------------------   게시판 table 에 입력  --------------------------
            sql1 =  " insert into TZ_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate, gadmin, isopen, sangdam_gubun) ";
            sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?, ?, ?)";
            sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, empty_clob(), ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'),?, ?, ?)";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_tabseq);
            pstmt1.setInt(2, v_seq);
            pstmt1.setString(3, s_userid);
            pstmt1.setString(4, s_usernm);
            pstmt1.setString(5, v_title);
            pstmt1.setString(6, v_content);
            pstmt1.setInt(7, 0);
            pstmt1.setInt(8, v_seq);
            pstmt1.setInt(9, 1);
            pstmt1.setInt(10, 1);
            pstmt1.setString(11, s_userid);
            pstmt1.setString(12, s_gadmin);
            pstmt1.setString(13, v_isopen);
            pstmt1.setString(14, v_sangdamgubun);

            isOk1 = pstmt1.executeUpdate();
            sql2 = "select content from TZ_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq;
//            connMgr.setOracleCLOB(sql2, v_content);       //      (기타 서버 경우)       

            // 파일업로드
            isOk2 = this.insertUpFile(connMgr, v_tabseq, v_seq, box);
                    
            if ( isOk1 > 0 && isOk2 > 0 ) { 
                connMgr.commit();
            } else { 
                connMgr.rollback();
            }
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
        }

        return isOk1*isOk2;
    }





    /**
    * 새로운 자료실 답변 등록
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int replyBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ResultSet rs1 = null;
        Statement stmt1 = null;
        Statement stmt2 = null;
        PreparedStatement pstmt1  = null;
        PreparedStatement pstmt2 = null;

        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int isOk3 = 1;
        int v_seq = 0;

        int    v_tabseq   = box.getInt("p_tabseq");
        int    v_refseq   = box.getInt("p_refseq");
        int    v_levels   = box.getInt("p_levels");
        int    v_position = box.getInt("p_position");
        String v_title    = box.getString("p_title");
        String v_content  = box.getString("p_content");

        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name");
        String s_gadmin = box.getSession("gadmin");
        String v_isopen = box.getString("p_isopen");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            // 기존 답변글 위치 한칸밑으로 변경
            sql1  = "update TZ_BOARD ";
            sql1 += "   set position = position + 1 ";
            sql1 += " where tabseq   = ? ";
            sql1 += "   and refseq   = ? ";
            sql1 += "   and position > ? ";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setInt(1, v_tabseq);            
            pstmt1.setInt(2, v_refseq);
            pstmt1.setInt(3, v_position);
            isOk1 = pstmt1.executeUpdate();

            stmt1 = connMgr.createStatement();
            // ----------------------   게시판 번호 가져온다 ----------------------------
            sql2 = "select nvl(max(seq), 0) from TZ_BOARD where tabseq = " +  v_tabseq;
            rs1 = stmt1.executeQuery(sql2);
            if ( rs1.next() ) { 
                v_seq = rs1.getInt(1) + 1;
            }
            rs1.close();
            // ------------------------------------------------------------------------------------

            //// //// //// //// //// //// //// //// /  게시판 table 에 입력  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql1 =  " insert into TZ_BOARD(tabseq, seq, userid, name, indate, title, content, cnt, refseq, levels, position, luserid, ldate, gadmin, isopen) ";
            sql1 += " values (?, ?, ?, ?, to_char(sysdate,  'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?) ";

            pstmt2 = connMgr.prepareStatement(sql3);
            pstmt2.setInt(1, v_tabseq);            
            pstmt2.setInt(2, v_seq);
            pstmt2.setString(3, s_userid);
            pstmt2.setString(4, s_usernm);
            pstmt2.setString(5, v_title);
            connMgr.setCharacterStream(pstmt2, 6, v_content); //      Oracle 9i or Weblogic 6.1 인 경우
            pstmt2.setInt(7, 0);
            pstmt2.setInt(8, v_refseq);
            pstmt2.setInt(9, v_levels + 1);
            pstmt2.setInt(10, v_position + 1);
            pstmt2.setString(11, s_userid);
            pstmt2.setString(12, s_gadmin);
            pstmt2.setString(13, v_isopen);
            isOk2 = pstmt2.executeUpdate();


            isOk3 = this.insertUpFile(connMgr, v_tabseq, v_seq, box);

            if ( isOk2 > 0 && isOk3 > 0 ) { 
                connMgr.commit();
            } else { 
                connMgr.rollback();
            }
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( stmt2 != null ) { try { stmt2.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
        }

        return isOk2*isOk3;
    }



    /**
    * 선택된 자료 상세내용 수정
    * @param box      receive from the form object and session
    * @return isOk    1:update success,0:update fail
    * @throws Exception
    */
     public int updateBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        // DBConnectionManager_bulletin bulletinDB = null;
        Connection conn = null;
        ResultSet rs1 = null;
        Statement stmt1 = null;
        PreparedStatement pstmt1  = null;
        CLOB clob                 = null;
        Writer clobWriter = null;
        Reader clobReader = null;
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 1;
        int isOk2 = 1;
        int isOk3 = 1;

        String s_userid = box.getSession("userid");
        String s_usernm = box.getSession("name");

        int    v_tabseq    = box.getInt("p_tabseq");
        int    v_seq       = box.getInt("p_seq");
        int    v_upfilecnt = box.getInt("p_upfilecnt");    //  서버에 저장되있는 파일수
        String v_title     = box.getString("p_title");
        String v_content   = box.getString("p_content");
        String v_isopen    = box.getString("p_isopen");
        String v_sangdamgubun   = box.getString("p_sangdamgubun");
        // String v_content =  StringManager.replace(box.getString("p_content"),"<br > ","\n"); 
        
        // Vector v_savefile     = box.getVector("p_savefile"); // 선택삭제파일
        // Vector v_filesequence = box.getVector("p_fileseq");  // 선택삭제파일 sequence
        // Vector v_realfile     = box.getVector("p_file");     // 새로 등록 파일

        Vector v_savefile     = new Vector();
        Vector v_filesequence = new Vector();
        
        for ( int   i = 0; i < v_upfilecnt; i++ ) { 
            if (    !box.getString("p_fileseq" + i).equals("")) { 

                v_savefile.addElement(box.getString("p_savefile" + i));         //      서버에 저장되있는 파일명 중에서   삭제할 파일들
                v_filesequence.addElement(box.getString("p_fileseq"  + i));      //         서버에 저장되있는 파일번호  중에서 삭제할 파일들

            }
        }


        /*********************************************************************************************/
        // 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
/*        
        ConfigSet conf = new ConfigSet();
        SmeNamoMime namo = new SmeNamoMime(v_content); // 객체생성 
        boolean result = namo.parse(); // 실제 파싱 수행 
        System.out.println(result);
        
        if ( !result ) { // 파싱 실패시 
            System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
            return 0;
        }
        if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
            String v_server = conf.getProperty("autoever.url.value");
            String fPath = conf.getProperty("dir.namo");   // 파일 저장 경로 지정
            String refUrl = conf.getProperty("url.namo");; // 웹에서 저장된 파일을 접근하기 위한 경로
            String prefix = "board" + v_seq;         // 파일명 접두어
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
            System.out.println(result);
        }
        if ( !result ) { // 파일저장 실패시 
            System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
            return 0;
        }
        v_content = namo.getContent(); // 최종 컨텐트 얻기
*/        
        /*********************************************************************************************/
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sql1 = "update TZ_BOARD set title = ?, content=?, isopen = ?, sangdam_gubun = ? ";//, userid = ?, name = ?, luserid = ?, ldate = to_char(sysdate, 'YYYYMMDDHH24MISS')";
            sql1 += "  where tabseq = ? and seq = ?";

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_title);
            pstmt1.setString(2, v_content);
            pstmt1.setString(3, v_isopen);
            pstmt1.setString(4, v_sangdamgubun);
            
        //  connMgr.setCharacterStream(pstmt1, 2, v_content);           // Oracle 9i or Weblogic 6.1 인 경우
            /*pstmt1.setString(2, s_userid);
            pstmt1.setString(3, s_usernm);
            pstmt1.setString(4, s_userid);*/
            pstmt1.setInt(5, v_tabseq);
            pstmt1.setInt(6, v_seq);

            isOk1 = pstmt1.executeUpdate();

            // WebLogic 6.1인경우
            sql2 = "select content from TZ_BOARD where tabseq = " + v_tabseq + " and seq = " + v_seq;
            //connMgr.setWeblogicCLOB(sql2, v_content);       //      clob 필드에 스트림을 이용,  data 를 넣는다(Weblogic 인 경우)

            isOk2 = this.insertUpFile(connMgr, v_tabseq, v_seq, box);       //      파일첨부했다면 파일table에  insert

            isOk3 = this.deleteUpFile(connMgr, box, v_filesequence);        //     삭제할 파일이 있다면 파일table에서 삭제

            if ( isOk1 > 0 && isOk2 > 0 && isOk3 > 0 ) { 
                connMgr.commit();
                if ( v_savefile != null ) { 
                    FileManager.deleteFile(v_savefile);         //   DB 에서 모든처리가 완료되면 해당 첨부파일 삭제
                }
            } else connMgr.rollback();
        } catch ( Exception ex ) { 
            // conn.rollback();
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( clob != null ) { try { clob.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            if ( rs1 != null ) { try { rs1.close(); } catch ( Exception e ) { } }
            if ( stmt1 != null ) { try { stmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            // if ( conn != null ) { try { conn.setAutoCommit(true); } catch ( Exception e10 ) { } }
            // if ( conn != null ) { try { bulletinDB.freeConnection("ziaan", conn); } catch ( Exception e10 ) { } }
        }

        return isOk1*isOk2*isOk3;
    }


    /**
    * 선택된 게시물 삭제
    * @param box      receive from the form object and session
    * @return isOk    1:delete success,0:delete fail
    * @throws Exception
    */
    public int deleteBoard(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1 = "";
        String sql2 = "";
        int isOk1 = 0;
        int isOk2 = 0;

        int v_tabseq = box.getInt("p_tabseq");
        int v_seq = box.getInt("p_seq");
        int v_upfilecnt = box.getInt("p_upfilecnt");    //  서버에 저장되있는 파일수
        Vector v_savefile  = box.getVector("p_savefile");

        // 답변 유무 체크(답변 있을시 삭제불가)
        if ( this.selectBoard(v_tabseq, v_seq) == 0 ) { 

            try { 
                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);

                isOk1 = 1;
                isOk2 = 1;
                sql1 = "delete from TZ_BOARD where tabseq = ? and seq = ? ";
                pstmt1 = connMgr.prepareStatement(sql1);
                pstmt1.setInt(1, v_tabseq);                
                pstmt1.setInt(2, v_seq);
                isOk1 = pstmt1.executeUpdate();

                if ( v_upfilecnt > 0 ) { 
                    sql2 = "delete from TZ_BOARDFILE where tabseq = ? and seq =  ?";
                    pstmt2 = connMgr.prepareStatement(sql2);
                    pstmt2.setInt(1, v_tabseq);
                    pstmt2.setInt(2, v_seq);                    
                    isOk2 = pstmt2.executeUpdate();
                }

                if ( isOk1 > 0 && isOk2 > 0 ) { 
                    connMgr.commit();
                    if ( v_savefile != null ) { 
                        FileManager.deleteFile(v_savefile);         //   첨부파일 삭제
                    }
                } else connMgr.rollback();
            }
            catch ( Exception ex ) { 
                connMgr.rollback();
                ErrorManager.getErrorStackTrace(ex, box,    sql1);
                throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
            }
            finally { 
                if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }  
                if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
                if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }

        return isOk1*isOk2;
    }


   /**
   * 삭제시 하위 답변 유무 체크
   * @param seq          게시판 번호
   * @return result      0 : 답변 없음,    1 : 답변 있음
   * @throws Exception
   */
   public int selectBoard(int tabseq, int seq) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String              sql     = "";
        int result = 0;

        try { 
            connMgr = new DBConnectionManager();

            sql  = "  select count(*) cnt                         ";
            sql += "  from                                        ";
            sql += "    (select tabseq, refseq, levels, position  ";
            sql += "       from TZ_BOARD                          ";
            sql += "      where tabseq = " + tabseq;
            sql += "        and seq = " + seq;
            sql += "     ) a, TZ_BOARD b                          ";
            sql += " where a.tabseq = b.tabseq                    ";
            sql += "   and a.refseq = b.refseq                    ";
            sql += "   and b.levels = (a.levels +1)                ";
            sql += "   and b.position = (a.position +1)            ";


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


//// //// //// //// //// //// //// //// //// //// //// //// //// /// 파일 테이블   //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// /

    /**
    * 새로운 자료파일 등록
    * @param connMgr  DB Connection Manager
    * @param p_seq    게시물 일련번호
    * @param box      receive from the form object and session
    * @return isOk    1:insert success,0:insert fail
    * @throws Exception
    */
     public int insertUpFile(DBConnectionManager connMgr, int p_tabseq, int p_seq, RequestBox   box) throws Exception { 
        ListSet             ls      = null;
        PreparedStatement pstmt2 = null;
        String              sql     = "";
        String sql2 = "";
        int isOk2 = 1;

        // ----------------------   업로드되는 파일의 형식을 알고 코딩해야한다  --------------------------------

        String [] v_realFileName = new String [FILE_LIMIT];
        String [] v_newFileName = new String [FILE_LIMIT];

        for ( int i = 0; i < FILE_LIMIT; i++ ) { 
            v_realFileName [i] = box.getRealFileName(FILE_TYPE + (i +1));
            v_newFileName [i] = box.getNewFileName(FILE_TYPE + (i +1));
        }
        // ----------------------------------------------------------------------------------------------------------------------------

        String s_userid = box.getSession("userid");

        try { 
             // ----------------------   자료 번호 가져온다 ----------------------------
            sql = "select nvl(max(fileseq), 0) from TZ_BOARDFILE where tabseq = " + p_tabseq + " and seq =   " + p_seq;
            ls = connMgr.executeQuery(sql);
            ls.next();
            int v_fileseq = ls.getInt(1) + 1;
            ls.close();
            // ------------------------------------------------------------------------------------

            //// //// //// //// //// //// //// //// //   파일 table 에 입력  //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
            sql2 =  "insert into TZ_BOARDFILE(tabseq, seq, fileseq, realfile, savefile, luserid, ldate)";
            sql2 += " values (?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))";

            pstmt2 = connMgr.prepareStatement(sql2);

            for ( int i = 0; i < FILE_LIMIT; i++ ) { 
                if ( !v_realFileName [i].equals("") ) {       //      실제 업로드 되는 파일만 체크해서 db에 입력한다
                    pstmt2.setInt(1, p_tabseq);                 
                    pstmt2.setInt(2, p_seq);
                    pstmt2.setInt(3, v_fileseq);
                    pstmt2.setString(4, v_realFileName [i]);
                    pstmt2.setString(5, v_newFileName [i]);
                    pstmt2.setString(6, s_userid);

                    isOk2 = pstmt2.executeUpdate();
                    v_fileseq++;
                }
            }
        } catch ( Exception ex ) { 
            FileManager.deleteFile(v_newFileName, FILE_LIMIT);      //  일반파일, 첨부파일 있으면 삭제..
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }
        }

        return isOk2;
    }


    /**
     * 선택된 자료파일 DB에서 삭제
     * @param connMgr           DB Connection Manager
     * @param box               receive from the form object and session
     * @param p_filesequence    선택 파일 갯수
     * @return
     * @throws Exception
     */
    public int deleteUpFile(DBConnectionManager connMgr, RequestBox box, Vector p_filesequence) throws Exception { 
        PreparedStatement pstmt3 = null;
        String sql3 = "";
        int isOk3 = 1;

        int v_tabseq = box.getInt("p_tabseq");
        int v_seq    = box.getInt("p_seq");

        try { 
            sql3 = "delete from TZ_BOARDFILE where tabseq =? and seq =? and fileseq = ?";
Log.info.println("deleteUpFile >>  >>  >>  >>  >>  >>  >> " +sql3);
Log.info.println("deleteUpFile >>  >>  >>  >>  >>  >>  >> " +v_tabseq);
Log.info.println("deleteUpFile >>  >>  >>  >>  >>  >>  >> " +v_seq);
            pstmt3 = connMgr.prepareStatement(sql3);

Log.info.println("deleteUpFile >>  >>  >>  >>  >>  >>  >> " +p_filesequence.size() );

            for ( int i = 0; i < p_filesequence.size(); i++ ) { 
                int v_fileseq = Integer.parseInt((String)p_filesequence.elementAt(i));

Log.info.println("deleteUpFile >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  >>  > " +v_fileseq);

                pstmt3.setInt(1, v_tabseq);
                pstmt3.setInt(2, v_seq);
                pstmt3.setInt(3, v_fileseq);

                isOk3 = pstmt3.executeUpdate();
            }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, sql3);
            throw new Exception("sql = " + sql3 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e ) { } }
        }

        return isOk3;
    }

 
    public static String convertBody(String contents) throws Exception { 
        String result = "";
        
        result = StringManager.replace(contents, "<HTML > ","");
        result = StringManager.replace(result, "<HEAD > ","");
        result = StringManager.replace(result, "<META NAME=\"GENERATOR\" Content=\"Microsoft DHTML Editing Control\" > ","");
        result = StringManager.replace(result, "<TITLE > ","");
        result = StringManager.replace(result, "</TITLE > ","");
        result = StringManager.replace(result, "</HEAD > ","");
        result = StringManager.replace(result, "<BODY > ","");
        result = StringManager.replace(result, "</BODY > ","");       
        result = StringManager.replace(result, "</HTML > ","");
                
        return result;
    }
}
