// **********************************************************
//  1. 제      목: 지식노트 코드 관리
//  2. 프로그램명 : KCodeAdminBean.java
//  3. 개      요: 코드 관리
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 조용준 2006. 8.  8
//  7. 수      정:
// **********************************************************

package com.ziaan.system;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

/**
 * 코드 관리(ADMIN)
 *
 * @date   : 2003. 7
 * @author : s.j Jung
 */
public class KCodeAdminBean { 

    public KCodeAdminBean() { }


    /**
    대분류 코드화면 리스트
    @param box          receive from the form object and session
    @return ArrayList   대분류 코드 리스트
    */
     public ArrayList selectListCode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        KCodeData data = null;

//        String v_searchtext = box.getString("p_searchtext");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "	select a.seq, a.upper, a.eorder, a.name, a.luserid, level	";
            sql += "	from tz_knowledge_code a					";
            sql += "	start with a.type='K' and a.upper=0				";
            sql += "	connect by prior a.seq = a.upper				";
            
            sql	 = "	select c.lvl1, c.seq1, c.eorder1				";
            sql += "		,c.name1, c.luserid1					";
            sql += "		,c.lvl2, c.seq2, c.eorder2				";
            sql += "		,c.name2, c.luserid2					";
            sql += "		,3 lvl3, d.seq, d.upper, d.eorder 			";
            sql += "		,d.name, d.luserid					";
            sql += "		,decode(c.seq1,c.seq2,1,				";
            sql += "				decode(c.seq2,d.seq,2,3			";
            sql += "					)				";
            sql += "			) lvl						";
            sql += "	from (								";
            sql += "		select 1 lvl1,	a.seq seq1,	a.eorder eorder1	";
            sql += "			,a.name name1,	a.luserid luserid1		";
            sql += "			,2 lvl2	,b.seq seq2,	decode(a.seq,b.seq,0,b.eorder) eorder2	";
            sql += "			,b.name name2	, b.luserid luserid2		";
            sql += "		from tz_knowledge_code a, tz_knowledge_code b		";
            sql += "		where a.type='K' and a.upper=0				";
            sql += "		 and b.type='K' and (a.seq=b.seq or a.seq=b.upper)	";
            sql += "		)c, tz_knowledge_code d					";
            sql += "	where d.type='K' and (c.seq2=d.seq or (c.seq2=d.upper and c.eorder2>0))	";
            sql += "	order by c.eorder1, c.eorder2, decode(c.seq2,d.seq,0,d.eorder)	";

            
/*
            if ( !v_searchtext.equals("") ) {      //    검색어가 있으면
               sql += " where gubunnm like " + StringManager.makeSQL("%" + v_searchtext + "%");
            }
*/

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new KCodeData();

                data.setSeq( ls.getInt("seq") );
                data.setUpper( ls.getInt("upper") );
                data.setEorder( ls.getInt("eorder") );
		data.setName( ls.getString("name") );
		data.setLevel( ls.getInt("lvl") );
		data.setLuserid( ls.getString("luserid") );

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
    자기 레벨만
    @param box          receive from the form object and session
    @return ArrayList   자기레벨 코드 리스트
    */
     public ArrayList selectListCodeEq(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        KCodeData data = null;
        
        int v_seq	= box.getInt("p_seq");

//        String v_searchtext = box.getString("p_searchtext");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "	select a.seq, a.upper, a.eorder, a.name, a.luserid		";
            sql += "	from tz_knowledge_code a					";
            sql += "	where a.type='K' 						";
            sql += "	  and a.upper=(select upper 					";
            sql += "			from tz_knowledge_code 				";
            sql += "			where type='K' and seq=" + v_seq + ")		";
            sql += "	order by a.eorder							";
            
/*
            if ( !v_searchtext.equals("") ) {      //    검색어가 있으면
               sql += " where gubunnm like " + StringManager.makeSQL("%" + v_searchtext + "%");
            }
*/

            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new KCodeData();

                data.setSeq( ls.getInt("seq") );
                data.setUpper( ls.getInt("upper") );
                data.setEorder( ls.getInt("eorder") );
		data.setName( ls.getString("name") );
		data.setLuserid( ls.getString("luserid") );

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
    대분류 코드화면 상세보기
    @param box          receive from the form object and session
    @return CodeData    조회한 상세정보
    */
   public KCodeData selectViewCode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        KCodeData data = null;

        String v_seq = box.getString("p_seq");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select seq, upper, eorder, name, luserid, ldate, type from TZ_knowledge_code        ";
            sql += "  where type='K' and seq = " + StringManager.makeSQL(v_seq); 
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new KCodeData();
                
                data.setSeq( ls.getInt("seq") );
                data.setUpper( ls.getInt("upper") );
                data.setEorder( ls.getInt("eorder") );
		data.setName( ls.getString("name") );         
		data.setLuserid( ls.getString("luserid") );         
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
    대분류코드 등록할때
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
     public int insertCode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String sql1 = "";
        int isOk = 0;
        String v_gubun = "";

        String v_gubunnm = box.getString("p_gubunnm");
        v_gubunnm = StringManager.replace(v_gubunnm,"'","");
        
        int v_seq	= 0;
        int v_upper	= box.getInt("p_upper");
        int v_eorder	= 0;
        
        /*
        int    v_maxlevel = box.getInt("p_maxlevel");
	String v_issystem = box.getString("p_issystem");
	*/
		
        String s_userid = box.getSession("userid");

        try { 
           connMgr = new DBConnectionManager();
           
           //seq 값을 구한다.
           sql  = "select nvl(max(seq),0) seq from TZ_knowledge_code where type='K'";
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) { 
               v_seq = ls.getInt(1) + 1;
           } else { 
               v_seq = 1;
           }
           

           //eorder 값을 구한다.
           sql  = "select nvl(max(eorder),0) seq from TZ_knowledge_code where type='K' and upper= " + v_upper + " ";
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) { 
               v_eorder = ls.getInt(1) + 1;
           } else { 
               v_eorder = 1;
           }           


           sql1 =  "insert into TZ_knowledge_code(seq, upper, eorder, name, luserid, ldate, type )   ";
           sql1 += " values (?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), 'K')                 ";

           pstmt = connMgr.prepareStatement(sql1);

           pstmt.setInt(1, v_seq);
           pstmt.setInt(2, v_upper);
           pstmt.setInt(3, v_eorder);
	   pstmt.setString(4, v_gubunnm);           
           pstmt.setString(5, s_userid);

           isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


    /**
    대분류코드 수정하여 저장할때
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
     public int updateCode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        String v_seq	  = box.getString("p_seq");
        String v_gubunnm  = box.getString("p_gubunnm");
        int    v_eorder   = box.getInt("p_eorder");		

        String s_userid    = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " update TZ_knowledge_code set eorder = ?, name = ?, luserid = ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
            sql += " where type='K' and seq = ?                                                                                               ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setInt(1, v_eorder);
            pstmt.setString(2, v_gubunnm);
	    pstmt.setString(3, s_userid);
            pstmt.setString(4, v_seq);            

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
    대분류코드 삭제할때 - 하위 소분류 삭제
    @param box      receive from the form object and session
    @return isOk    1:delete success,0:delete fail
    */
    public int deleteCode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1 = "";
        String sql2 = "";

        int isOk1 = 0;
        int isOk2 = 0;

        String v_seq  = box.getString("p_seq");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);  

            sql1  = " delete from TZ_knowledge_code			";
            sql1 += "   where 	 					";
            sql1 += "	(type,seq) in (					";
            sql1 += "		select type, seq			";
            sql1 += "		from tz_knowledge_code			";
            sql1 += "		start with type = 'K' and seq = ?	";
            sql1 += "		connect by prior seq = upper		";
            sql1 += "		)						";

            /*
            sql2  = " delete from TZ_CODE    ";
            sql2 += "   where gubun = ?      ";
            */

            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_seq);
            isOk1 = pstmt1.executeUpdate();
            
            /*
            pstmt2 = connMgr.prepareStatement(sql2);
            pstmt2.setString(1, v_gubun);
            isOk2 = pstmt2.executeUpdate();
            */

//            if ( isOk1 > 0 && isOk2 > 0) connMgr.commit();         //      2가지 sql 이 꼭 같이 delete 되어야 하는 경우이므로  
//            else connMgr.rollback();
            if ( isOk1 > 0 ) connMgr.commit();         //      하위 분류의 로우가 없을경우 isOk2 가 0 이므로 isOk2 > 0 조건 제외
            else connMgr.rollback();
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1 + "\r\n" +sql2);
            throw new Exception("sql = " + sql1 + "\r\n" + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
//        return isOk1*isOk2;
        return isOk1;
    }

   /**
   * 하위분류 코드화면 리스트
   */
    /**
    하위분류 코드화면 리스트
    @param box          receive from the form object and session
    @return ArrayList   하위분류코드 리스트
    */
    public ArrayList selectSubListCode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        KCodeData            data    = null;

        String v_seq  = box.getString("p_seq");
        String v_searchtext = box.getString("p_searchtext");

        try { 
            connMgr = new DBConnectionManager();

            list = new ArrayList();

            sql  = "	select seq, name, eorder			";
            sql += "	from tz_knowledge_code				";
            sql += "	where type='K'	and upper = " + v_seq + "	";
            
            if ( !v_searchtext.equals("") ) { sql += " and name like " + StringManager.makeSQL("%" + v_searchtext + "%"); }
            sql += "                                 order by eorder ";

            System.out.println("하위분류 코드 리스트 \n" + sql + "------------------------------\n");
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new KCodeData();

                data.setSeq( ls.getInt("seq") );
                data.setName( ls.getString("name") );
                data.setEorder( ls.getInt("eorder") );

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
    소분류 코드화면 상세보기
    @param box          receive from the form object and session
    @return CodeData    조회한 상세정보
    */
   public CodeData selectSubViewCode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        CodeData data = null;

        String v_gubun = box.getString("p_gubun");
        int    v_levels = box.getInt("p_levels");
        String v_code  = box.getString("p_code");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select codenm from TZ_CODE        ";
            sql += "  where gubun  = " + StringManager.makeSQL(v_gubun); 
            sql += "    and levels = " + v_levels;
            sql += "    and code   = " + StringManager.makeSQL(v_code);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new CodeData();
                data.setCodenm( ls.getString("codenm") );
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
    하위분류코드 등록할때
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int insertSubCode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String sql1 = "";
        int isOk = 0;
        String v_code = "";

        int v_seq	= box.getInt("p_seq");        
        String v_name	= box.getString("p_name");        
        int v_upper	= v_seq;
        int v_eorder	= 0;

        String s_userid = box.getSession("userid");

        try { 
           connMgr = new DBConnectionManager();

           sql  = "select nvl(max(seq),0) seq from TZ_knowledge_code   ";
           sql += " where type='K' 				";
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) { 
               v_seq = ls.getInt(1) + 1;
           } else { 
               v_seq = 1;
	   }
	   
           sql  = "select nvl(max(eorder),0) eorder from TZ_knowledge_code   ";
           sql += " where type='K' and upper = " + v_upper + " 			     ";
           ls = connMgr.executeQuery(sql);
           if ( ls.next() ) { 
               v_eorder = ls.getInt(1) + 1;
           } else { 
               v_eorder = 1;
	   }	   
		   
           sql1 =  "insert into TZ_knowledge_CODE(seq, upper, eorder, name, luserid, ldate, type ) ";
           sql1 += " values (?, ?, ?, ?, ?, to_char(sysdate, 'YYYYMMDDHH24MISS'), 'K')           ";

           pstmt = connMgr.prepareStatement(sql1);

           pstmt.setInt(1, v_seq);
           pstmt.setInt(2, v_upper);
           pstmt.setInt(3, v_eorder);
           pstmt.setString(4, v_name);
           pstmt.setString(5, s_userid);

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
    소분류코드 수정하여 저장할때
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail
    */
     public int updateSubCode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;

        String v_gubun  = box.getString("p_gubun");
        int    v_levels = box.getInt("p_levels");
        String v_code   = box.getString("p_code");
        String v_codenm = box.getString("p_codenm");

        String s_userid = box.getSession("userid");

        try { 
            connMgr = new DBConnectionManager();

            sql  = " update TZ_CODE set codenm = ? , luserid= ? , ldate = to_char(sysdate, 'YYYYMMDDHH24MISS') ";
            sql += "  where gubun = ? and levels = ? and code = ?                                              ";

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString(1, v_codenm);
            pstmt.setString(2, s_userid);
            pstmt.setString(3, v_gubun);
            pstmt.setInt(4, v_levels);
            pstmt.setString(5, v_code);

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
    소분류코드 삭제할때 - 하위 분류 삭제
    @param box      receive from the form object and session
    @return isOk    1:delete success,0:delete fail
    */
    public int deleteSubCode(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1 = "";
        String sql2 = "";

        int isOk1 = 0;
        int isOk2 = 0;

        String v_gubun  = box.getString("p_gubun");
        int    v_levels = box.getInt("p_levels");
        String v_code   = box.getString("p_code");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);  

            sql1  = " delete from TZ_CODE                            ";
            sql1 += "   where gubun = ? and levels = ? and code = ?  ";

            sql2  = " delete from TZ_CODE    ";
            sql2 += "   where gubun = ? and (upper = ? or parent = ? ) ";


            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_gubun);
            pstmt1.setInt(2, v_levels);
            pstmt1.setString(3, v_code);
            isOk1 = pstmt1.executeUpdate();


            pstmt2 = connMgr.prepareStatement(sql2);
            pstmt2.setString(1, v_gubun);
            pstmt2.setString(2, v_code);
            pstmt2.setString(3, v_code);
            isOk2 = pstmt2.executeUpdate();

//            if ( isOk1 > 0 && isOk2 > 0) connMgr.commit();         //      2가지 sql 이 꼭 같이 delete 되어야 하는 경우이므로  
//            else connMgr.rollback();
            if ( isOk1 > 0 ) connMgr.commit();         //      하위 분류의 로우가 없을경우 isOk2 가 0 이므로 isOk2 > 0 조건 제외
            else connMgr.rollback();

        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql1 + "\r\n" +sql2);
            throw new Exception("sql = " + sql1 + "\r\n" + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) try { connMgr.setAutoCommit(true); } catch ( Exception e ) { }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }
//        return isOk1*isOk2;
        return isOk1;
    }


/************************************* 공통함수들 ***********************************************************/

    /**
    *  코드구분명 (구분)
    */
    public static String getKCodeName (int seq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        KCodeData data = null;

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select name, upper from TZ_knowledge_CODE        ";
            sql += "  where seq = " + seq; 
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new KCodeData();
                data.setName( ls.getString("name") );
                result = data.getName();
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
	 * 하위코드 수동등록 여부
	 * @param gubun       구분코드
	 * @return result     Y : 수동등록, N : 자동등록
	 * @throws Exception
	 */
	public static String getCodeIssystem (String gubun) throws Exception { 
		DBConnectionManager connMgr     = null;
		ListSet             ls      = null;
		String              sql     = "";
		String result = "";
		CodeData data = null;

		try { 
			connMgr = new DBConnectionManager();

			sql  = " select issystem from TZ_CODEGUBUN        ";
			sql += "  where gubun = " + StringManager.makeSQL(gubun); 
			ls = connMgr.executeQuery(sql);

			if ( ls.next() ) { 
				data = new CodeData();
				data.setIssystem( ls.getString("issystem") );
				result = data.getIssystem();
			}
		}
		catch ( Exception ex ) { 
			ErrorManager.getErrorStackTrace(ex);
			throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
		}
		finally { 
			if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
			if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		}

		return result;
	}


    /**
    *  코드명 (구분,코드)
    */
    public static String getKCodeName (String gubun, String code) throws Exception { 
        return getKCodeName (gubun, code, 1);
    }

    /**
    *  코드명 (구분,코드,레벨)
    */
    public static String getKCodeName (String gubun, String code, int levels) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String              sql     = "";
        String result = "";
        CodeData data = null;

        try { 
            connMgr = new DBConnectionManager();

            sql  = " select codenm from TZ_CODE        ";
            sql += "  where gubun  = " + StringManager.makeSQL(gubun); 
            sql += "    and levels = " + levels;
            sql += "    and code   = " + StringManager.makeSQL(code);
            ls = connMgr.executeQuery(sql);

            if ( ls.next() ) { 
                data = new CodeData();
                data.setCodenm( ls.getString("codenm") );
                result = data.getCodenm();
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
    *  업로드 확장자 코드값 
    */
    public static ArrayList getUploadCodeName() throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList list = new ArrayList();        
        ListSet ls    = null;
        String    sql = "";
        DataBox  dbox = null;
        String s_gubun = "0057";  // 업로드확장자허용 - 코드 

        try { 
            connMgr = new DBConnectionManager();

            sql  = " SELECT codenm FROM TZ_CODE WHERE GUBUN='" +s_gubun + "' ORDER BY TO_NUMBER(CODE) ASC  ";
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { }}
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
}
