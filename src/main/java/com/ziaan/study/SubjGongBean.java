// **********************************************************
//  1. 제      목: 과목 QNA DATA
//  2. 프로그램명: SubjQnaBean.java
//  3. 개      요: 과목 QNA bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 김영만 2003. 9. 7
//  7. 수      정:
// **********************************************************
package com.ziaan.study;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.course.SubjGongData;
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FileManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.Log;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;
import com.ziaan.system.CodeConfigBean;
import com.ziaan.system.MemberAdminBean;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class SubjGongBean { 
    private ConfigSet config;
    private int row;
	private	static final String	FILE_TYPE =	"p_file";			// 		파일업로드되는 tag name
	private	static final int FILE_LIMIT	= 5;					// 	  페이지에 세팅된 파일첨부 갯수


    
    public SubjGongBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.bulletin.row") );        //        이 모듈의 페이지당 row 수를 셋팅한다
        } catch( Exception e ) { 
            e.printStackTrace();
        }
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

        try { 
            connMgr = new DBConnectionManager();


            list = new ArrayList();

            sql  = "\n select tg.seq , tg.types, tg.addate, tg.title , tg.userid userid, tg.adcontent, tm.name, decode(tg.gadmin, 'ZZ', 'ZZ', 'P1', '강사',  '운영자') gadmin, tg.isimport,tg.cnt ";
            sql += "\n   from tz_gong   tg ";
            sql += "\n      , Tz_member tm ";
            sql += "\n  where tg.subj    = " + StringManager.makeSQL(v_subj);
            sql += "\n    and tg.year    = " + StringManager.makeSQL(v_year);
            sql += "\n    and tg.subjseq = " + StringManager.makeSQL(v_subjseq);
            sql += "\n    and tg.userid = tm.userid ";
            sql += "\n    and tg.types != 'H' ";

            if ( v_orderColumn.equals("") ) { 
                sql += "\n  order by tg.addate desc ";
            } else { 
                sql += "\n  order by tg.addate desc, " + v_orderColumn + v_orderType;
            }
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new SubjGongData();

                data.setSeq( ls.getInt("seq") );
                data.setTypes( ls.getString("types") );
                data.setAddate( ls.getString("addate") );
                data.setTitle( ls.getString("title") );
                data.setUserid( ls.getString("userid") );
                data.setName( ls.getString("name") );
                data.setAdcontent( ls.getCharacterStream("adcontent") );
                data.setGadmin( ls.getString("gadmin") );
                data.setCnt( ls.getInt("cnt") );
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
            sql += "          a.title title, a.userid userid, a.adcontent adcontent ,a.cnt        ";
            sql += "          from TZ_GONG a, TZ_CODE b                                          ";
            sql += "          where a.types   = b.code                                            ";
            sql += "            and b.gubun   = " + StringManager.makeSQL(v_typesgubun);
            sql += "            and b.levels  = 1 and a.types = 'H'                               ";
            sql += "            and a.subj    = " + StringManager.makeSQL(v_subj);
            sql += "            and a.year    = " + StringManager.makeSQL(v_year);
            sql += "            and a.subjseq = " + StringManager.makeSQL(v_subjseq);
            sql += "          order by a.addate desc ) ";
            //sql += " where rownum <3 ";


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
                data.setCnt( ls.getInt("cnt") );
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
        DBConnectionManager	connMgr	= null;
        ListSet ls        = null;
        ArrayList list    = null;
        String sql        = "";
        String sql1       = "";
        SubjGongData data = null;

        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq");
        int    v_seq     = box.getInt("p_seq");
        String              v_upcnt 	= "Y";
        
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

//System.out.println("sql :::" + sql);
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
                
                //첨부파일 관련
                data.setUpfile( ls.getString("upfile"));
                data.setRealfile( ls.getString("realfile"));
                
                // data.setAdcontent( ls.getString("adcontent") );
            }
            
            if ( !v_upcnt.equals("N") ) {  // 조회수 증가
                
            	sql1 ="update TZ_GONG set cnt = cnt + 1 where subj = "+StringManager.makeSQL(v_subj)
						+" and year = "+StringManager.makeSQL(v_year)
						+" and subjseq = "+StringManager.makeSQL(v_subjseq)
						+" and seq = "+v_seq;
            	connMgr.executeUpdate(sql1);
           // System.out.println("cnt update :::::" + sql1);
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
}
