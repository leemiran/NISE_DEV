
// **********************************************************
//  1. 제      목: 상담 관리
//  2. 프로그램명 : CounselAdminBean.java
//  3. 개      요: 상담 관리
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 7. 13
//  7. 수      정:
// **********************************************************

package com.ziaan.study;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * 상담 관리(ADMIN)
 *
 * @date   : 2003. 7
 * @author : s.j Jung
 */
public class CounselAdminBean { 

    public static String COUNSEL_KIND = "0047";

    public CounselAdminBean() { }

    /**
    상담화면 리스트
    @param box          receive from the form object and session
    @return ArrayList   상담 리스트
    */
    public ArrayList selectListCounsel(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        String v_userid = box.getString("p_userid");
        String v_mcode = box.getStringDefault("s_mcode","ALL");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select a.no, a.userid, a.cuserid, b.name, a.title, a.mcode, c.codenm mcodenm, a.etime,  ";
            sql += "        a.ftext, a.ctext, a.status, a.sdate, a.ldate, a.subj, a.year, a.subjseq, a.gubun ";
            sql += "   from TZ_SANGDAM a, TZ_MEMBER b, TZ_CODE c         ";
            sql += "  where a.cuserid = b.userid                         ";
            sql += "    and a.mcode   = c.code                           ";
            sql += "    and c.gubun   = " + StringManager.makeSQL(COUNSEL_KIND); 
            sql += "    and a.userid  = " + StringManager.makeSQL(v_userid);
            if ( !v_mcode.equals("ALL") ) { 
                sql += "    and a.mcode   = " + StringManager.makeSQL(v_mcode);
            }
            sql += " order by no desc";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
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
    상담화면 리스트 - 과목별
    @param box          receive from the form object and session
    @return ArrayList   상담 리스트
    */
    public ArrayList selectListCounselSubj(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        String v_userid = box.getString("p_userid");
        String v_mcode = box.getStringDefault("s_mcode","ALL");
        String v_subjyearseq = box.getString("p_subj");
        StringTokenizer st = null;

        String v_subj    = "";
        String v_year    = "";
        String v_subjseq     = "";

        if ( v_subjyearseq.indexOf("/") > 0) { 
          st      = new StringTokenizer(v_subjyearseq,"/");
          if ( st.hasMoreElements() ) { 
            v_subj    = (String)st.nextToken();
            v_year    = (String)st.nextToken();
            v_subjseq = (String)st.nextToken();
          }
        }

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql  = " select a.no, a.userid, a.cuserid, b.name, a.title, a.mcode, c.codenm mcodenm, a.etime,  ";
            sql += "        a.ftext, a.ctext, a.status, a.sdate, a.ldate, a.subj, a.year, a.subjseq, a.gubun ";
            sql += "   from TZ_SANGDAM a, TZ_MEMBER b, TZ_CODE c         ";
            sql += "  where a.cuserid = b.userid                         ";
            sql += "    and a.mcode   = c.code                           ";
            sql += "    and c.gubun   = " + StringManager.makeSQL(COUNSEL_KIND); 
            sql += "    and a.userid  = " + StringManager.makeSQL(v_userid);
            sql += "    and a.subj    = " + StringManager.makeSQL(v_subj);
            sql += "    and a.year    = " + StringManager.makeSQL(v_year);
            sql += "    and a.subjseq = " + StringManager.makeSQL(v_subjseq);
            if ( !v_mcode.equals("ALL") ) { 
                sql += "    and a.mcode   = " + StringManager.makeSQL(v_mcode);
            }
            sql += " order by no desc";

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
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
    상담화면 상세보기
    @param box          receive from the form object and session
    @return DataBox     조회한 상세정보
    */
    public DataBox selectViewCounsel(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        DataBox             dbox    = null;

        String v_userid = box.getString("p_userid");
        int    v_no     = box.getInt("p_no");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select no, userid, cuserid, title, ftext, ctext, subj, status, ldate, ";
            sql += "        etime, mcode, sdate, subj, year, subjseq, gubun, remote        ";
            sql += "   from TZ_SANGDAM                                                     ";
            sql += "  where userid  = " + StringManager.makeSQL(v_userid);
            sql += "    and no      = " + v_no;

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
    상담 등록할때
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int insertCounsel(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String sql1 = "";
        int isOk = 0;

        int    v_no      = 0;
        String v_userid  = box.getString("p_userid");
        String v_title   = box.getString("p_title");
        String v_ftext   = box.getString("p_ftext");
        String v_ctext   = box.getString("p_ctext");
        String v_status  = box.getString("p_status");
        String v_mcode   = box.getString("p_mcode");
        String v_etime   = box.getString("p_etime");
        String v_sdate   = box.getString("p_sdate");
        String v_gubun   = box.getString("p_gubun");
        String v_remote  = box.getString("p_remote");
        String s_userid = box.getSession("userid");
        String v_subjyearseq = box.getString("p_subj");
        StringTokenizer st = null;

        String v_subj    = "";
        String v_year    = "";
        String v_subjseq     = "";

        if ( v_subjyearseq.indexOf("/") > 0) { 
          st      = new StringTokenizer(v_subjyearseq,"/");
          if ( st.hasMoreElements() ) { 
            v_subj    = (String)st.nextToken();
            v_year    = (String)st.nextToken();
            v_subjseq = (String)st.nextToken();
          }
        }

        try { 
           connMgr = new DBConnectionManager();

           sql  = "select max(no) from TZ_SANGDAM ";
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) { 
               v_no = ls.getInt(1) + 1;
           } else { 
               v_no = 1;
           }

           sql1 =  "insert into TZ_SANGDAM(no, userid, cuserid, title, ftext, ctext, status, mcode, etime, sdate, ldate ,subj, year, subjseq, gubun, remote)  ";
           sql1 += "            values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), ?, ?, ?, ?, ?)  ";

           pstmt = connMgr.prepareStatement(sql1);
           pstmt.setInt(1,  v_no);
           pstmt.setString(2,  v_userid);
           pstmt.setString(3,  s_userid);
           pstmt.setString(4,  v_title);
//           pstmt.setCharacterStream(5,  new StringReader(v_ftext), v_ftext.length() );
           pstmt.setString( 5, v_ftext);
           pstmt.setString( 6, v_ctext);
           pstmt.setString( 7, v_status);
           pstmt.setString( 8, v_mcode);
           pstmt.setString( 9, v_etime);
           pstmt.setString(10, v_sdate);
           pstmt.setString(11, v_subj);
           pstmt.setString(12, v_year);
           pstmt.setString(13, v_subjseq);
           pstmt.setString(14, v_gubun);
           pstmt.setString(15, v_remote);

           isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    상담 수정하여 저장할때
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
     public int updateCounsel(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        int    v_no      = box.getInt("p_no");
        String v_userid  = box.getString("p_userid");
        String v_title   = box.getString("p_title");
        String v_ftext   = box.getString("p_ftext");
        String v_ctext   = box.getString("p_ctext");
        String v_status  = box.getString("p_status");
        String v_mcode   = box.getString("p_mcode");
        String v_remote  = box.getString("p_remote");
        String v_etime   = box.getString("p_etime");
        String v_sdate   = box.getString("p_sdate");
        String v_gubun   = box.getString("p_gubun");
        String s_userid = box.getSession("userid");
        String v_subjyearseq = box.getString("p_subj");
        StringTokenizer st = null;

        String v_subj    = "";
        String v_year    = "";
        String v_subjseq     = "";

        if ( v_subjyearseq.indexOf("/") > 0) { 
          st      = new StringTokenizer(v_subjyearseq,"/");
          if ( st.hasMoreElements() ) { 
            v_subj    = (String)st.nextToken();
            v_year    = (String)st.nextToken();
            v_subjseq = (String)st.nextToken();
          }
        }

        try { 
            connMgr = new DBConnectionManager();

//            sql  = " update TZ_SANGDAM set cuserid = ? , title = ?, ftext = ? , ctext = ? , status = ? , mcode = ?, etime = ?, sdate=?,  ";
            sql  = " update TZ_SANGDAM set title = ?, ftext = ? , ctext = ? , status = ? , mcode = ?, etime = ?, sdate=?,  ";
            sql += "                       ldate = to_char(sysdate, 'YYYYMMDDHH24MISS'), subj = ?, year = ?, subjseq = ?, gubun = ?, remote = ?   ";
            sql += "  where userid = ? and no = ?                                                   ";

            pstmt = connMgr.prepareStatement(sql);

//            pstmt.setString( 1, s_userid);
            pstmt.setString( 1, v_title);
//            pstmt.setCharacterStream(3,  new StringReader(v_ftext), v_ftext.length() );
            pstmt.setString( 2, v_ftext);
            pstmt.setString( 3, v_ctext);
            pstmt.setString( 4, v_status);
            pstmt.setString( 5, v_mcode);
            pstmt.setString( 6, v_etime);
            pstmt.setString( 7, v_sdate);
            pstmt.setString( 8, v_subj);
            pstmt.setString(9, v_year);
            pstmt.setString(10, v_subjseq);
            pstmt.setString(11, v_gubun);
            pstmt.setString(12, v_remote);
            pstmt.setString(13, v_userid);
            pstmt.setInt   (14, v_no);

            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    상담 삭제할때
    @param box      receive from the form object and session
    @return isOk    1:delete success,0:delete fail
    */
    public int deleteCounsel(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        int    v_no      = box.getInt("p_no");
        String v_userid  = box.getString("p_userid");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " delete from TZ_SANGDAM            ";
            sql += "  where userid = ? and no = ?      ";

            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, v_userid);
            pstmt.setInt(2, v_no);
            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n"  + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    과목 리스트
    @param p_userid  사용자아이디
    @param name      select box name
    @param selected  select box 선택값
    @return result   select box text
    */
    public static String getSubjSelect (String p_userid, String name, String selected) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String result = null;
        String              sql     = "";
        String code         = "";
        String value        = "";
        String v_subjseq    = "";

        result = "  <SELECT name=" + name + " > \n";
        result += " <option value='' > 과목을 선택하세요</option > \n";
        try { 
            connMgr = new DBConnectionManager();

            sql  = " select a.subj ||'/'|| a.year ||'/'|| a.subjseq subj, b.subjnm, a.subjseq from tz_student a, tz_subj b ";
            sql += " where a.subj = b.subj ";
            sql += "   and a.userid = " + StringManager.makeSQL(p_userid);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                code        = ls.getString("subj");
                value       = ls.getString("subjnm");
                v_subjseq   = ls.getString("subjseq");

                result += " <option value=" + code;
                if ( selected.equals(code)) { 
                    result += " selected ";
                }
                
                result += " > " + value + " " + v_subjseq + "기</option > \n";
            }
            result += " <option value=0000/0000/0000";
            if ( selected.equals("0000/0000/0000") ) { 
            		result += " selected ";
            }
            result += " >==  ==  ==  == =기타 ==  ==  ==  == =</option > \n";
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
    
    /***********************************************************************************/
    /**
    상담화면 리스트 - 과목별
    @param box          receive from the form object and session
    @return ArrayList   상담 리스트
    */
    public ArrayList selectCounselList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        String p_grcode     = box.getStringDefault("s_grcode", "ALL");
        String  ss_company  = box.getStringDefault("s_company", "ALL");
        
        String v_startdt = box.getString("p_startdt");
        String v_enddt = box.getString("p_enddt");
        StringBuffer strSQL = new StringBuffer();

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            strSQL.append("select a.no, ") ;
            strSQL.append("  a.cuserid, ") ;
            strSQL.append("  (select name from tz_member f where f.userid = a.cuserid) as aname, ") ;
            strSQL.append("  a.userid, ") ;
            strSQL.append("  b.name as qname, ") ;
            strSQL.append("  get_grcodenm((select grcode from tz_grcomp f where f.comp = b.comp)) grcodenm, ") ;
            strSQL.append("  ( ") ;
            strSQL.append("  select grseqnm from tz_grseq gr ") ;
            strSQL.append("   where gr.grcode = (select grcode from tz_subjseq f where f.subj = a.subj and f.year = a.year and f.subjseq = a.subjseq) ") ;
            strSQL.append("     and gr.gyear  = (select gyear from tz_subjseq f where f.subj = a.subj and f.year = a.year and f.subjseq = a.subjseq) ") ;
            strSQL.append("     and gr.grseq   = (select grseq from tz_subjseq f where f.subj = a.subj and f.year = a.year and f.subjseq = a.subjseq) ") ;
            strSQL.append("  ) grseqnm, ") ;
            strSQL.append("  (select grseq from tz_subjseq f where f.subj = a.subj and f.year = a.year and f.subjseq = a.subjseq) grseq, ") ;
            strSQL.append("  (select grcode from tz_subjseq f where f.subj = a.subj and f.year = a.year and f.subjseq = a.subjseq) grcode, ") ;
            strSQL.append("  (select gyear from tz_subjseq f where f.subj = a.subj and f.year = a.year and f.subjseq = a.subjseq) gryear, ") ;
            strSQL.append("  a.subjseq, ") ;
            strSQL.append("  b.comp, ") ;
            strSQL.append("  get_compnm(b.comp) compnm, ") ;
            strSQL.append("  b.position_nm, ") ;
            strSQL.append("  b.lvl_nm, ") ;
            strSQL.append("  a.title, ") ;
            strSQL.append("  a.mcode, ") ;
            strSQL.append("  c.codenm mcodenm, ") ;
            strSQL.append("  a.etime, ") ;
            strSQL.append("  a.ftext, ") ;
            strSQL.append("  a.ctext, ") ;
            strSQL.append("  a.status, ") ;
            strSQL.append("  a.sdate, ") ;
            strSQL.append("  a.ldate, ") ;
            strSQL.append("  a.subj, ") ;
            strSQL.append("  (select subjnm from tz_subj f where subj = a.subj) subjnm, ") ;
            strSQL.append("  a.year, ") ;
            strSQL.append("  a.subjseq, ") ;
            strSQL.append("  a.gubun ") ;
            strSQL.append("from TZ_SANGDAM a, \n") ;
            strSQL.append("  TZ_MEMBER b, \n") ;
            strSQL.append("  TZ_CODE c \n") ;
            strSQL.append("where a.userid = b.userid \n") ;
            strSQL.append("  and a.mcode = c.code \n") ;
            strSQL.append("  and c.gubun   = " + StringManager.makeSQL(COUNSEL_KIND)+"\n"); 
            if ( !p_grcode.equals("ALL") ) { 
            	strSQL.append("    and (select grcode from tz_grcomp f where f.comp = b.comp)   = " + StringManager.makeSQL(p_grcode)+"\n");
            }
            if ( !ss_company.equals("ALL") ) { 
            	strSQL.append("    and b.comp   = " + StringManager.makeSQL(ss_company)+"\n");
            }
            if(!v_startdt.equals("") && !v_enddt.equals("")){
            	strSQL.append("  and a.sdate between "+v_startdt+" and "+v_enddt+" \n") ;
            }
            else if(!v_startdt.equals("") && v_enddt.equals("")){
            	strSQL.append("  and a.sdate >= "+v_startdt+" \n") ;
            }
            else if(v_startdt.equals("") && !v_enddt.equals("")){
            	strSQL.append("  and a.sdate <= "+v_enddt+" \n") ;
            }
            
            strSQL.append(" order by a.sdate desc \n");

            System.out.println(strSQL.toString());
            
            ls = connMgr.executeQuery(strSQL.toString());

            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                list.add(dbox);
            }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
            throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
    /**
    상담내역 통계화면
    @param box          receive from the form object and session
    @return ArrayList   상담 리스트
    */
    public ArrayList selectStatisticsCounselList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        
        String p_grcode     = box.getStringDefault("s_grcode", "ALL");
        String  ss_company  = box.getStringDefault("s_company", "ALL");
        
        String v_startdt = box.getString("p_startdt");
        String v_enddt = box.getString("p_enddt");
        StringBuffer strSQL = new StringBuffer();

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();
            if(p_grcode.equals("ALL")){
            	strSQL.append("select x.mcode, \n") ;
            	strSQL.append("       x.codenm, \n") ;
            	strSQL.append("       count(*) cnt \n") ;
            	strSQL.append("  from (select b.userid, a.mcode, c.codenm,b.comp, \n") ;
            	strSQL.append("       (select grcode from tz_grcomp f where f.comp = b.comp) grcode, \n") ;
            	strSQL.append("       get_grcodenm((select grcode from tz_grcomp f where f.comp = b.comp)) grcodenm, \n") ;
            	strSQL.append("       ( \n") ;
            	strSQL.append("         select grseqnm from tz_grseq gr \n") ;
            	strSQL.append("          where gr.grcode = (select grcode from tz_subjseq f where f.subj = a.subj and f.year = a.year and f.subjseq = a.subjseq) \n") ;
            	strSQL.append("            and gr.gyear = (select gyear from tz_subjseq f where f.subj = a.subj and f.year = a.year and f.subjseq = a.subjseq) \n") ;
            	strSQL.append("            and gr.grseq = (select grseq from tz_subjseq f where f.subj = a.subj and f.year = a.year and f.subjseq = a.subjseq) \n") ;
            	strSQL.append("        ) grseqnm, \n") ;
            	strSQL.append("        get_compnm(b.comp) compnm \n") ;
            	strSQL.append("        from TZ_SANGDAM a, \n") ;
            	strSQL.append("          TZ_MEMBER b, \n") ;
            	strSQL.append("          TZ_CODE c \n") ;
            	strSQL.append("        where a.userid = b.userid \n") ;
            	strSQL.append("          and a.mcode = c.code \n") ;
            	strSQL.append("          and c.gubun = '0047' \n") ;
            	            
                if(!v_startdt.equals("") && !v_enddt.equals("")){
                	strSQL.append("  and a.sdate between "+v_startdt+" and "+v_enddt+" \n") ;
                }
                else if(!v_startdt.equals("") && v_enddt.equals("")){
                	strSQL.append("  and a.sdate >= "+v_startdt+" \n") ;
                }
                else if(v_startdt.equals("") && !v_enddt.equals("")){
                	strSQL.append("  and a.sdate <= "+v_enddt+" \n") ;
                }
                	
            	strSQL.append("    ) x \n") ;
            	strSQL.append(" where 1=1 \n") ;
            	strSQL.append("group by x.mcode, x.codenm \n") ;
            	strSQL.append("order by x.mcode \n") ;
            }
            else{
            	strSQL.append("select x.grcode, \n") ;
            	strSQL.append("       x.grcodenm, \n") ;
            	strSQL.append("       x.comp, \n") ;
            	strSQL.append("       x.compnm, \n") ;
            	strSQL.append("       x.mcode, \n") ;
            	strSQL.append("       x.codenm, \n") ;
            	strSQL.append("       count(*) cnt \n") ;
            	strSQL.append("  from (select b.userid, a.mcode, c.codenm,b.comp, \n") ;
            	strSQL.append("       (select grcode from tz_grcomp f where f.comp = b.comp) grcode, \n") ;
            	strSQL.append("       get_grcodenm((select grcode from tz_grcomp f where f.comp = b.comp)) grcodenm, \n") ;
            	strSQL.append("       ( \n") ;
            	strSQL.append("         select grseqnm from tz_grseq gr \n") ;
            	strSQL.append("          where gr.grcode = (select grcode from tz_subjseq f where f.subj = a.subj and f.year = a.year and f.subjseq = a.subjseq) \n") ;
            	strSQL.append("            and gr.gyear = (select gyear from tz_subjseq f where f.subj = a.subj and f.year = a.year and f.subjseq = a.subjseq) \n") ;
            	strSQL.append("            and gr.grseq = (select grseq from tz_subjseq f where f.subj = a.subj and f.year = a.year and f.subjseq = a.subjseq) \n") ;
            	strSQL.append("        ) grseqnm, \n") ;
            	strSQL.append("        get_compnm(b.comp) compnm \n") ;
            	strSQL.append("        from TZ_SANGDAM a, \n") ;
            	strSQL.append("          TZ_MEMBER b, \n") ;
            	strSQL.append("          TZ_CODE c \n") ;
            	strSQL.append("        where a.userid = b.userid \n") ;
            	strSQL.append("          and a.mcode = c.code \n") ;
            	strSQL.append("          and c.gubun = '0047' \n") ;
            	            
                if(!v_startdt.equals("") && !v_enddt.equals("")){
                	strSQL.append("  and a.sdate between "+v_startdt+" and "+v_enddt+" \n") ;
                }
                else if(!v_startdt.equals("") && v_enddt.equals("")){
                	strSQL.append("  and a.sdate >= "+v_startdt+" \n") ;
                }
                else if(v_startdt.equals("") && !v_enddt.equals("")){
                	strSQL.append("  and a.sdate <= "+v_enddt+" \n") ;
                }
                	 
            	strSQL.append("    ) x \n") ;
            	strSQL.append(" where 1=1 \n") ;
            	if ( !p_grcode.equals("ALL") ) { 
            		strSQL.append("    and grcode   = " + StringManager.makeSQL(p_grcode)+"\n");
            	}
            	if ( !ss_company.equals("ALL") ) { 
            		strSQL.append("    and comp   = " + StringManager.makeSQL(ss_company)+"\n");
            	}
            	/*            
            if(!v_startdt.equals("") && !v_enddt.equals("")){
            	strSQL.append("  and a.sdate between "+v_startdt+" and "+v_enddt+" \n") ;
            }
            else if(!v_startdt.equals("") && v_enddt.equals("")){
            	strSQL.append("  and a.sdate >= "+v_startdt+" \n") ;
            }
            else if(v_startdt.equals("") && !v_enddt.equals("")){
            	strSQL.append("  and a.sdate <= "+v_enddt+" \n") ;
            }
            	 */
            	strSQL.append("group by x.grcode,x.grcodenm, x.comp, x.compnm, x.mcode, x.codenm \n") ;
            	strSQL.append("order by grcode, comp, x.mcode \n") ;
            }
            System.out.println(strSQL.toString());
            
            ls = connMgr.executeQuery(strSQL.toString());

            while ( ls.next() ) { 
                dbox = ls.getDataBox();

                list.add(dbox);
            }
        } catch ( Exception ex ) { 
               ErrorManager.getErrorStackTrace(ex, box, strSQL.toString());
            throw new Exception("sql = " + strSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
    /***********************************************************************************/
    
    /**
    상담 수정하여 저장할때
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
     public int updateCounsel2(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        int    v_no      = box.getInt("p_no");
        String v_userid  = box.getString("p_userid");
        String v_title   = box.getString("p_title");
        String v_ftext   = box.getString("p_ftext");
        String v_ctext   = box.getString("p_ctext");
        String v_status  = box.getString("p_status");
        String v_mcode   = box.getString("p_mcode");
        String v_remote  = box.getString("p_remote");
        String v_etime   = box.getString("p_etime");
        String v_sdate   = box.getString("p_sdate");
        String v_gubun   = box.getString("p_gubun");
        String s_userid = box.getSession("userid");
        String v_subjyearseq = box.getString("p_subj");
        StringTokenizer st = null;

        String v_subj    = "";
        String v_year    = "";
        String v_subjseq     = "";

        if ( v_subjyearseq.indexOf("/") > 0) { 
          st      = new StringTokenizer(v_subjyearseq,"/");
          if ( st.hasMoreElements() ) { 
            v_subj    = (String)st.nextToken();
            v_year    = (String)st.nextToken();
            v_subjseq = (String)st.nextToken();
          }
        }

        try { 
            connMgr = new DBConnectionManager();

//            sql  = " update TZ_SANGDAM set cuserid = ? , title = ?, ftext = ? , ctext = ? , status = ? , mcode = ?, etime = ?, sdate=?,  ";
            sql  = " update TZ_SANGDAM set " +
            		" ctext = ? ," +
            		" status = ? ," +
            		" mcode = ?," +
            		" sdate=?," +
            		"  ";
            sql += "   ldate = to_char(sysdate, 'YYYYMMDDHH24MISS'), subj = ?, year = ?, subjseq = ?, gubun = ?, remote = ?   ";
            sql += "  where userid = ? and no = ?                                                   ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, v_ctext);
            pstmt.setString( 2, v_status);
            pstmt.setString( 3, v_mcode);
            pstmt.setString( 4, v_sdate);
            pstmt.setString( 5, v_subj);
            pstmt.setString(6, v_year);
            pstmt.setString(7, v_subjseq);
            pstmt.setString(8, v_gubun);
            pstmt.setString(9, v_remote);
            pstmt.setString(10, v_userid);
            pstmt.setInt   (11, v_no);

            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }
}
