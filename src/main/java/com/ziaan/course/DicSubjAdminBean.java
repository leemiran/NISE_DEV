// **********************************************************
//  1. 제      목: 용어사전 관리
//  2. 프로그램명: DicSubjAdminBean.java
//  3. 개      요: 용어사전 관리
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 정은년 2005. 9. 1
//  7. 수      정:
// **********************************************************

package com.ziaan.course;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;
/**
 * 용어사전 관리(ADMIN)
 *
 * @date   : 2003. 7
 * @author : s.j Jung
 */
public class DicSubjAdminBean { 

    public DicSubjAdminBean() { }



    /**
    용어사전 화면 리스트
    @param box          receive from the form object and session
    @return ArrayList   용어사전  리스트
    */
    public ArrayList selectListDic(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DicData data = null;
        DataBox             dbox    = null;
        
        String v_gubun      = "1";
        String ss_upperclass    = box.getString("s_upperclass");    // 과목분류
        String ss_middleclass   = box.getString("s_middleclass");   // 과목분류
        String ss_lowerclass    = box.getString("s_lowerclass");    // 과목분류
        String ss_subj          = box.getString("s_subjcourse");    // 과목코드
        String v_searchtext = box.getString("p_searchtext");
        
        String  v_orderColumn   = box.getString("p_orderColumn");           	// 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");           		// 정렬할 순서

        String v_groups     = box.getStringDefault("p_group","");        // ㄱ,ㄴ,ㄷ ....
        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = " select a.seq seq, a.subj subj, b.subjnm subjnm, a.words words,          ";
            sql += "        a.descs descs, a.groups groups, a.luserid luserid, a.ldate ldate, d.name ";
            sql += "   from TZ_DIC a, TZ_SUBJ b, TZ_DICGROUP c                               ";
            sql += "    ,   TZ_Member d                               ";
            sql += "  where a.subj = b.subj                                                  ";
            sql += "    and a.groups = c.groups                                              ";
            sql += "    and a.luserid = d.userid (+)                                         ";
            sql += "    and a.gubun = " + StringManager.makeSQL(v_gubun);


            if ( !ss_subj.equals("") && !ss_subj.equals("ALL") ) {                               // 과목코드 있으면
                sql += "  and a.subj   = " + StringManager.makeSQL(ss_subj);
            }

            if ( !v_searchtext.equals("") ) {                            //    검색어가 있으면
                // sql += " and a.words like " + StringManager.makeSQL("%" + v_searchtext + "%");
                sql += " and (lower(a.words) like " +StringManager.makeSQL("%" + v_searchtext + "%") + " OR upper(a.words) like " +StringManager.makeSQL("%" + v_searchtext + "%") + " ) ";
            }

            if ( !v_groups.equals("") ) {                            //    용어분류로 검색할때
                sql += " and a.groups = " + StringManager.makeSQL(v_groups);
            }
            
            if ( v_orderColumn.equals("") ) { 
            	sql += " order by a.subj asc, a.groups asc, a.words asc ";
			} else { 
			    sql += " order by " + v_orderColumn + v_orderType;
			}
			
            

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                /*data = new DicData();
                data.setSeq( ls.getInt("seq") );
                data.setSubj( ls.getString("subj") );
                data.setSubjnm( ls.getString("subjnm") );
                data.setWords( ls.getString("words") );
                data.setDescs( ls.getString("descs") );
                data.setGroups( ls.getString("groups") );
                data.setLuserid( ls.getString("luserid") );
                data.setLdate( ls.getString("ldate") );
                dbox = ls.getDataBox();
                list.add(data);*/
                
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


    /**
    용어사전 화면 상세보기
    @param box          receive from the form object and session
    @return DicData     조회한 상세정보
    */
    public DataBox selectViewDic(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DicData data = null;
        DataBox             dbox    = null;
        String v_gubun      = "1";
        String v_subj       = box.getString("p_subj");
        String v_seq        = box.getString("p_seq");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select a.seq seq, a.subj subj, b.subjnm subjnm, a.words words,          ";
            sql += "        a.descs descs, a.groups groups, a.luserid luserid, a.ldate ldate ";
            sql += "   from TZ_DIC a, TZ_SUBJ b                                              ";
            sql += "  where a.subj = b.subj                                                  ";
            sql += "    and a.gubun  = " + StringManager.makeSQL(v_gubun);
            sql += "    and a.subj   = " + StringManager.makeSQL(v_subj);
            sql += "    and a.seq    = " + v_seq;

            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                dbox = ls.getDataBox();
            }
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
    용어사전  등록할때
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int insertDic(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";        
        int isOk = 0;
        String v_menu = "";
    
        String v_gubun  = "1";
        String v_subj   = box.getString("p_subj");
        int    v_seq    = 0;
        String v_words  = box.getString("p_words");
        // String v_descs  = box.getString("p_descs");
        String v_contents  = box.getString("p_contents");
        String v_groups = box.getString("p_groups");
        String s_userid = box.getSession("userid");

        /*********************************************************************************************/
        // 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
/*        
        ConfigSet conf = new ConfigSet();
        SmeNamoMime namo = new SmeNamoMime(v_contents); // 객체생성 
        boolean result = namo.parse(); // 실제 파싱 수행 
        if ( !result ) { // 파싱 실패시 
            System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
            return 0;
        }
        if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
            String v_server = conf.getProperty("autoever.url.value");
            String fPath = conf.getProperty("dir.namo");   // 파일 저장 경로 지정
            String refUrl = conf.getProperty("url.namo");; // 웹에서 저장된 파일을 접근하기 위한 경로
            String prefix = "dic" + v_subj;         // 파일명 접두어
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
        }
        if ( !result ) { // 파일저장 실패시 
            System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
            return 0;
        }
        v_contents = namo.getContent(); // 최종 컨텐트 얻기
*/        
        /*********************************************************************************************/
        
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);
           sql  = "select max(seq) from TZ_DIC ";
           sql += " where gubun = " + StringManager.makeSQL(v_gubun);
           sql += "   and subj  = " + StringManager.makeSQL(v_subj);
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) { 
               v_seq = ls.getInt(1) + 1;
           } else { 
               v_seq = 1;
           }

           sql1 =  "insert into TZ_DIC(gubun, subj, seq, words, descs, groups, luserid, ldate)      ";
           sql1 += "            values (?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))  ";
//           sql1 += "            values (?, ?, ?, ?, empty_clob(), ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))  ";

           pstmt = connMgr.prepareStatement(sql1);

           pstmt.setString(1,  v_gubun);
           pstmt.setString(2,  v_subj);
           pstmt.setInt(3,  v_seq);
           pstmt.setString(4,  v_words);
            pstmt.setString(5, v_contents);
           pstmt.setString(6,  v_groups);
           pstmt.setString(7, s_userid);
           isOk = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }
           sql2 = "select descs from TZ_DIC";
           sql2 += "  where gubun    = " + StringManager.makeSQL(v_gubun);
           sql2 += "    and subj     = " + StringManager.makeSQL(v_subj);
           sql2 += "    and seq      = " + v_seq;           
   
//           connMgr.setOracleCLOB(sql2, v_contents);    // CLOB 처리

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
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
    
    /**
    용어사전  등록할때 Excel
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int insertDicExcel(String v_subj, String v_words, String v_groups,String v_descs, String s_userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String sql1 = "";
        String sql2 = "";
        int isOk = 0;
        String v_menu = "";
    
        String v_gubun  = "1";
        // String v_subj   = box.getString("p_subj");
        int    v_seq    = 0;
        // String v_words  = box.getString("p_words");
        // String v_descs  = box.getString("p_descs");
        // String v_groups = box.getString("p_groups");
        // String v_groups = "";
        // String v_descs  = box.getString("p_descs");v_descs
        // String v_contents  = box.getString("p_contents");
        // String s_userid = box.getSession("userid");

        try {                     
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);
           
           sql  = "select max(seq) from TZ_DIC ";
           sql += " where gubun = " + StringManager.makeSQL(v_gubun);
           sql += "   and subj  = " + StringManager.makeSQL(v_subj);
        
           ls = connMgr.executeQuery(sql);

           if ( ls.next() ) { 
               v_seq = ls.getInt(1) + 1;
           } else { 
               v_seq = 1;
           }
           if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                
           sql1 =  "insert into TZ_DIC(gubun, subj, seq, words, descs, groups, luserid, ldate)      ";
           sql1 += "            values (?, ?, ?, ?, ?, UPPER(?), ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))  ";
//           sql1 += "            values (?, ?, ?, ?, empty_clob(), UPPER(?), ?, to_char(sysdate, 'YYYYMMDDHH24MISS'))  ";
           pstmt = connMgr.prepareStatement(sql1);
           pstmt.setString(1,  v_gubun);
           pstmt.setString(2,  v_subj);
           pstmt.setInt(3,  v_seq);
           pstmt.setString(4,  v_words);
           pstmt.setString(5,  v_descs);
           pstmt.setString(6,  v_groups);
           pstmt.setString(7, s_userid);
           isOk = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }
           sql2 = "select descs from TZ_DIC";
           sql2 += "  where gubun    = " + StringManager.makeSQL(v_gubun);
           sql2 += "    and subj     = " + StringManager.makeSQL(v_subj);
           sql2 += "    and seq      = " + v_seq;           
//           connMgr.setOracleCLOB(sql2, v_descs);    // CLOB 처리    
           
           if ( isOk > 0) {          
               if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
           } else { 
               if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
           }              
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }            
        }

        return isOk;
    }


    /**
    용어사전  수정할때
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
     public int updateDic(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        String v_gubun  = "1";
        String v_subj   = box.getString("p_subj");
        int    v_seq    = box.getInt("p_seq");
        String v_words  = box.getString("p_words");
        // String v_descs  = box.getString("p_descs");
        String v_contents  = box.getString("p_contents");
        String v_groups = box.getString("p_groups");
        String s_userid = box.getSession("userid");

        /*********************************************************************************************/
        // 나모에디터 본문 처리  (Mime Document Parsing 및 이미지 업로드)
/*        
        ConfigSet conf = new ConfigSet();
        SmeNamoMime namo = new SmeNamoMime(v_contents); // 객체생성 
        boolean result = namo.parse(); // 실제 파싱 수행 
        if ( !result ) { // 파싱 실패시 
            System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
            return 0;
        }
        if ( namo.isMultipart() ) { // 문서가 멀티파트인지 판단 
            String v_server = conf.getProperty("autoever.url.value");
            String fPath = conf.getProperty("dir.namo");   // 파일 저장 경로 지정
            String refUrl = conf.getProperty("url.namo");; // 웹에서 저장된 파일을 접근하기 위한 경로
            String prefix = "dic" + v_subj;         // 파일명 접두어
            result = namo.saveFile(fPath, v_server +refUrl, prefix); // 실제 파일 저장 
        }
        if ( !result ) { // 파일저장 실패시 
            System.out.println( namo.getDebugMsg() ); // 디버깅 메시지 출력 
            return 0;
        }
        v_contents = namo.getContent(); // 최종 컨텐트 얻기
*/        
        /*********************************************************************************************/
        
        try { 
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);
           sql  = " update TZ_DIC set words = ?, descs = ?, groups= ?"; //, luserid = ?, ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
//           sql  = " update TZ_DIC set words = ?, descs = empty_clob(), groups= ?"; //, luserid = ?, ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
           sql += "  where gubun = ? and subj = ? and seq = ?                                                                    ";

           pstmt = connMgr.prepareStatement(sql);

           pstmt.setString(1,  v_words);
           pstmt.setString(2,  v_contents);
           pstmt.setString(3,  v_groups);
//           pstmt.setString(3, s_userid);
           pstmt.setString(4,  v_gubun);
           pstmt.setString(5,  v_subj);
           pstmt.setInt(   6,  v_seq);

           isOk = pstmt.executeUpdate();
           if ( pstmt != null ) { pstmt.close(); }
           sql = "select descs from TZ_DIC";
           sql += "  where gubun    = " + StringManager.makeSQL(v_gubun);
           sql += "    and subj     = " + StringManager.makeSQL(v_subj);
           sql += "    and seq      = " + v_seq;    
//           connMgr.setOracleCLOB(sql, v_contents);       //      (ORACLE 9i 서버)

           if ( isOk > 0) {          
               if ( connMgr != null ) { try { connMgr.commit(); } catch ( Exception e10 ) { } }
           } else { 
               if ( connMgr != null ) { try { connMgr.rollback(); } catch ( Exception e10 ) { } }
           }   
                                  
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    용어사전 삭제할때
    @param box      receive from the form object and session
    @return isOk    1:delete success,0:delete fail
    */
    public int deleteDic(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        String v_gubun  = "1";
        String v_subj   = box.getString("p_subj");
        int    v_seq    = box.getInt("p_seq");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " delete from TZ_DIC                        ";
            sql += "  where gubun = ? and subj = ? and seq = ? ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1,  v_gubun);
            pstmt.setString(2,  v_subj);
            pstmt.setInt(3,  v_seq);
            isOk = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
            // Log.info.println(this, box, "delete TZ_DIC where gubun" + v_gubun + " and subj " +v_subj + " and seq" + v_seq);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n"  + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
     * 용어분류셀렉트박스
     * @param selected      선택값
     * @return result       select box 구성 String
     * @throws Exception
     */
    public static String getDicGroupSelect (String selected) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String result = null;
        String              sql     = "";

        String value = "";


        result  = "  <SELECT name='p_groups' > \n";

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select groups from TZ_DICGROUP ";
            sql += " order by seq asc               ";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 

                value = ls.getString("groups"); 

                result += " <option value=" + value;
                if ( selected.equals(value)) { 
                    result += " selected ";
                }

                result += " > " + value + " </option > \n";
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        result += "  </SELECT > \n";
        return result;
    }

}

