// **********************************************************
//  1. 제      목: 관련사이트 BEAN
//  2. 프로그램명: LinkSiteBean.java
//  3. 개      요: 관련사이트 BEAN
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 정경진 2009. 09. 03
//  7. 수      정:
// **********************************************************
package com.ziaan.homepage;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;

public class LinkSiteBean { 

    public LinkSiteBean() { }


    /**
    관련사이트 리스트 조회
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList SelectList(RequestBox box) throws Exception { 

        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList   list    = null;
        DataBox dbox        = null;
        String  sql         = "";

        //String  v_grcode    = box.getStringDefault ("p_grcode", box.getSession("tem_grcode"));
        String  v_isuse		= box.getStringDefault ("p_isuse","");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = "select seq,grcode "; // banner, islogin 
            sql += "      sort,";
            sql += "      sitenm,";
            sql += "      url,";
            sql += "      isuse ";
            sql += "     from TZ_LINKSITE where 1=1 ";
            //if ( !v_grcode.equals("") && !v_grcode.equals("ALL")) 
            //	sql += " and grcode = '" + v_grcode + "'";
            if ( v_isuse.equals("Y")) 
            	sql += "	and isuse = 'Y'";
            sql += " order by sort ";
            
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
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
    관련사이트 순위 수정
    @param box      receive from the form object and session
    @return int		isOk
    */
     public int updateSort(RequestBox box) throws Exception { 

		DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String  sql			= "";
        int isOk			= 0;

        String  v_userid = box.getSession("userid");

        //Vector v_grcode = box.getVector("grcode");
        Vector v_seq	= box.getVector("seq");
        Vector v_sort	= box.getVector("p_sort");
		//String  s_grcode     = "";
        String  s_seq		= "";
        String  s_sort		= "";

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            list = new ArrayList();

			sql  = " update TZ_LINKSITE set sort=? ";
			sql += " where seq = ?"; //grcode = ? and 
            
            pstmt = connMgr.prepareStatement(sql);

            /*if ( v_grcode != null ) { 
				for ( int i = 0; i < v_grcode.size() ; i++ ) { 
					s_grcode = (String )v_grcode.elementAt(i);
					s_seq = (String )v_seq.elementAt(i);
					s_sort = (String )v_sort.elementAt(i);

					pstmt.setString (1,  s_sort);
					pstmt.setString (2,  s_grcode);
					pstmt.setString (3,  s_seq);

					isOk = pstmt.executeUpdate();
				}
			}	*/		
            if ( v_seq != null ) { 
                for ( int i = 0; i < v_seq.size() ; i++ ) { 
                    //s_grcode = (String )v_grcode.elementAt(i);
                    s_seq = (String )v_seq.elementAt(i);
                    s_sort = (String )v_sort.elementAt(i);
    
                    pstmt.setString (1,  s_sort);
                    //pstmt.setString (2,  s_grcode);
                    pstmt.setString (2,  s_seq);
    
                    isOk = pstmt.executeUpdate();
                }
            }   
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e2 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }



    /**
    관련사이트 정보 조회
    @param box      receive from the form object and session
    @return ArrayList
    */
     public ArrayList SelectView(RequestBox box) throws Exception { 

        DBConnectionManager	connMgr	= null;
        ListSet ls          = null;
        ArrayList           list    = null;
        String  sql          = "";
        DataBox dbox        = null;

        //String  v_grcode     = box.getString ("p_grcode");
        String  v_seq		= box.getString ("p_seq");

        try { 
            connMgr = new DBConnectionManager();
            list = new ArrayList();

            sql = "select seq			,"; //grcode        , banner        , islogin       ,
            sql += "       sort			,";
			sql += "       sitenm		,";
            sql += "       url			,";
            sql += "       isuse		";
            /*sql += "       useridparam    ,";
			sql += "       nameparam      ,";
			sql += "       birth_dateparam     ,";
			sql += "       conoparam      ,";
			sql += "       pwdparam       ,";
			sql += "       deptnmparam    ,";
			sql += "       jikwiparam     ,";
			sql += "       jikwinmparam   ,";
			sql += "       compparam      ,";
			sql += "       companynmparam ,";
			sql += "       subjparam      ,";
			sql += "       subjseqparam   ,";
			sql += "       gadminparam    ,";
			sql += "       param1         ,";
			sql += "       paramvalue1    ,";
			sql += "       param2         ,";
			sql += "       paramvalue2    ,";
			sql += "       param3         ,";
			sql += "       paramvalue3    ,";
			sql += "       param4         ,";
			sql += "       paramvalue4    ,";
			sql += "       param5         ,";
			sql += "       paramvalue5    ,";
			sql += "       param6         ,";
			sql += "       paramvalue6    ";*/
            sql += "  from TZ_LINKSITE ";
            sql += "  where seq  = '" + v_seq + "'"; //grcode = '" + v_grcode + "'";
            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
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
    관련사이트 등록
    @param box      receive from the form object and session
    @return int		isOk
    */
     public int insert(RequestBox box) throws Exception { 

		DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String  sql			= "";
        int isOk            = 0;

        String v_userid = box.getSession("userid");

		String v_grcode			= box.getString ("p_grcode");
		String v_sitenm			= box.getString ("p_sitenm");
		String v_url			= box.getString ("p_url");
		//String v_banner			= box.getNewFileName("p_banner");
		String v_isuse			= box.getString ("p_isuse");
		//String v_islogin		= box.getString ("p_islogin");
		/*String v_useridparam	= box.getString ("p_useridparam");
		String v_nameparam		= box.getString ("p_nameparam");
		String v_birth_dateparam		= box.getString ("p_birth_dateparam");
		String v_conoparam		= box.getString ("p_conoparam");
		String v_pwdparam		= box.getString ("p_pwdparam");
		String v_deptnmparam	= box.getString ("p_deptnmparam");
		String v_jikwiparam		= box.getString ("p_jikwiparam");
		String v_jikwinmparam	= box.getString ("p_jikwinmparam");
		String v_compparam		= box.getString ("p_compparam");
		String v_companynmparam	= box.getString ("p_companynmparam");
		String v_subjparam		= box.getString ("p_subjparam");
		String v_subjseqparam	= box.getString ("p_subjseqparam");
		String v_gadminparam	= box.getString ("p_gadminparam");
		String v_param1			= box.getString ("p_param1");
		String v_paramvalue1	= box.getString ("p_paramvalue1");
		String v_param2			= box.getString ("p_param2");
		String v_paramvalue2	= box.getString ("p_paramvalue2");
		String v_param3			= box.getString ("p_param3");
		String v_paramvalue3	= box.getString ("p_paramvalue3");
		String v_param4			= box.getString ("p_param4");
		String v_paramvalue4	= box.getString ("p_paramvalue4");
		String v_param5			= box.getString ("p_param5");
		String v_paramvalue5	= box.getString ("p_paramvalue5");
		String v_param6			= box.getString ("p_param6");
		String v_paramvalue6	= box.getString ("p_paramvalue6");*/

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            list = new ArrayList();

			sql  = " insert into TZ_LINKSITE( grcode, seq, sort, sitenm, url, isuse,"; //islogin,"; // 8 //grcode, banner, 
			/*sql += "useridparam, nameparam, birth_dateparam, conoparam, pwdparam, deptnmparam, jikwiparam,"; // 7
			sql += "jikwinmparam, compparam, companynmparam, subjparam, subjseqparam, gadminparam,";  // 6
			sql += "param1, paramvalue1, param2, paramvalue2, param3, paramvalue3, param4, paramvalue4,"; // 8
			sql += "param5, paramvalue5, param6, paramvalue6,";*/ 
            sql += " luserid, ldate) "; // 6
			sql += " values (?, (select nvl(max(seq),0) from TZ_LINKSITE ) +1, (select nvl(max(seq),0) from TZ_LINKSITE ) +1,"; // 3
			sql += " ?, ?, ?, ?, sysdate)"; // 32 ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 

			pstmt = connMgr.prepareStatement(sql);
			
            int params = 1;
			pstmt.setString (params++,  v_grcode);
			pstmt.setString (params++,  v_sitenm);
			pstmt.setString (params++,  v_url);
			/*pstmt.setString (params++,  v_banner);*/
			pstmt.setString (params++,  v_isuse);
			/*pstmt.setString (params++,  v_islogin);
			pstmt.setString (params++,  v_useridparam);
			pstmt.setString (params++,  v_nameparam);
			pstmt.setString (params++,  v_birth_dateparam);
			pstmt.setString (params++,  v_conoparam);
			pstmt.setString (params++,  v_pwdparam);
			pstmt.setString (params++,  v_deptnmparam);
			pstmt.setString (params++,  v_jikwiparam);
			pstmt.setString (params++,  v_jikwinmparam);
			pstmt.setString (params++,  v_compparam);
			pstmt.setString (params++,  v_companynmparam);
			pstmt.setString (params++,  v_subjparam);
			pstmt.setString (params++,  v_subjseqparam);
			pstmt.setString (params++,  v_gadminparam);
			pstmt.setString (params++,  v_param1);
			pstmt.setString (params++,  v_paramvalue1);
			pstmt.setString (params++,  v_param2);
			pstmt.setString (params++,  v_paramvalue2);
			pstmt.setString (params++,  v_param3);
			pstmt.setString (params++,  v_paramvalue3);
			pstmt.setString (params++,  v_param4);
			pstmt.setString (params++,  v_paramvalue4);
			pstmt.setString (params++,  v_param5);
			pstmt.setString (params++,  v_paramvalue5);
			pstmt.setString (params++,  v_param6);
			pstmt.setString (params++,  v_paramvalue6);*/
			pstmt.setString (params++,  v_userid);

			isOk = pstmt.executeUpdate();

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e2 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


   /**
    관련사이트 수정
    @param box      receive from the form object and session
    @return int		isOk
    */
     public int update(RequestBox box) throws Exception { 

		DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String  sql			= "";
        int isOk			= 0;

        String  v_userid = box.getSession("userid");

        //String v_grcode			= box.getString ("p_grcode");
        int v_seq			= box.getInt("p_seq");
        String v_sitenm			= box.getString ("p_sitenm");
        String v_url			= box.getString ("p_url");
		/*String v_banner			= box.getNewFileName("p_banner");
		String v_banner1		= box.getString ("p_banner1");*/
        String v_isuse			= box.getString ("p_isuse");
        /*String v_islogin		= box.getString ("p_islogin");
		String v_useridparam	= box.getString ("p_useridparam");
		String v_nameparam		= box.getString ("p_nameparam");
		String v_birth_dateparam		= box.getString ("p_birth_dateparam");
		String v_conoparam		= box.getString ("p_conoparam");
		String v_pwdparam		= box.getString ("p_pwdparam");
		String v_deptnmparam	= box.getString ("p_deptnmparam");
		String v_jikwiparam		= box.getString ("p_jikwiparam");
		String v_jikwinmparam	= box.getString ("p_jikwinmparam");
		String v_compparam		= box.getString ("p_compparam");
		String v_companynmparam	= box.getString ("p_companynmparam");
		String v_subjparam		= box.getString ("p_subjparam");
		String v_subjseqparam	= box.getString ("p_subjseqparam");
		String v_gadminparam	= box.getString ("p_gadminparam");
		String v_param1			= box.getString ("p_param1");
		String v_paramvalue1	= box.getString ("p_paramvalue1");
		String v_param2			= box.getString ("p_param2");
		String v_paramvalue2	= box.getString ("p_paramvalue2");
		String v_param3			= box.getString ("p_param3");
		String v_paramvalue3	= box.getString ("p_paramvalue3");
		String v_param4			= box.getString ("p_param4");
		String v_paramvalue4	= box.getString ("p_paramvalue4");
		String v_param5			= box.getString ("p_param5");
		String v_paramvalue5	= box.getString ("p_paramvalue5");
		String v_param6			= box.getString ("p_param6");
		String v_paramvalue6	= box.getString ("p_paramvalue6");

		if ( v_banner.equals("")) v_banner = v_banner1;*/

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            list = new ArrayList();

			sql  = "update TZ_LINKSITE set";
			sql += " sitenm			 = ?, ";
			sql += " url			 = ?, "; 
			/*sql += " banner			 = ?, ";*/ 
			sql += " isuse			 = ?, "; 
			/*sql += " islogin		 = ?, ";*/ 
			sql += " luserid		 = ?, ";
			sql += " ldate			 = sysdate ";
			/*sql += " useridparam     = ?, ";
			sql += " nameparam       = ?, ";
			sql += " birth_dateparam      = ?, ";
			sql += " conoparam       = ?, ";
			sql += " pwdparam        = ?, ";
			sql += " deptnmparam     = ?, ";
			sql += " jikwiparam      = ?, ";
			sql += " jikwinmparam    = ?, ";
			sql += " compparam       = ?, ";
			sql += " companynmparam  = ?, ";
			sql += " subjparam       = ?, ";
			sql += " subjseqparam    = ?, ";
			sql += " gadminparam     = ?, ";
			sql += " param1          = ?, ";
			sql += " paramvalue1     = ?, ";
			sql += " param2          = ?, ";
			sql += " paramvalue2     = ?, ";
			sql += " param3          = ?, ";
			sql += " paramvalue3     = ?, ";
			sql += " param4          = ?, ";
			sql += " paramvalue4     = ?, ";
			sql += " param5          = ?, ";
			sql += " paramvalue5     = ?, ";
			sql += " param6          = ?, ";
			sql += " paramvalue6     = ?  ";*/
			sql += "where seq = ?"; //grcode = ? and 

			pstmt = connMgr.prepareStatement(sql);

            int params = 1;
			pstmt.setString (params++,  v_sitenm);
			pstmt.setString (params++,  v_url);
			//pstmt.setString (3,  v_banner);
			pstmt.setString (params++,  v_isuse);
            //pstmt.setString (5,  v_islogin);
			pstmt.setString (params++,  v_userid);
			/*pstmt.setString (7,  v_useridparam);
			pstmt.setString (8,  v_nameparam);
			pstmt.setString (9,  v_birth_dateparam);
			pstmt.setString (10,  v_conoparam);
			pstmt.setString (11,  v_pwdparam);
			pstmt.setString (12,  v_deptnmparam);
			pstmt.setString (13,  v_jikwiparam);
			pstmt.setString (14,  v_jikwinmparam);
			pstmt.setString (15,  v_compparam);
			pstmt.setString (16,  v_companynmparam);
			pstmt.setString (17,  v_subjparam);
			pstmt.setString (18,  v_subjseqparam);
			pstmt.setString (19,  v_gadminparam);
			pstmt.setString (20,  v_param1);
			pstmt.setString (21,  v_paramvalue1);
			pstmt.setString (22,  v_param2);
			pstmt.setString (23,  v_paramvalue2);
			pstmt.setString (24,  v_param3);
			pstmt.setString (25,  v_paramvalue3);
			pstmt.setString (26,  v_param4);
			pstmt.setString (27,  v_paramvalue4);
			pstmt.setString (28,  v_param5);
			pstmt.setString (29,  v_paramvalue5);
			pstmt.setString (30,  v_param6);
			pstmt.setString (31,  v_paramvalue6);
			pstmt.setString (32,  v_grcode);*/
			pstmt.setInt (params++,  v_seq);

			isOk = pstmt.executeUpdate();

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e2 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


   /**
    관련사이트 삭제
    @param box      receive from the form object and session
    @return int		isOk
    */
     public int delete(RequestBox box) throws Exception { 

		DBConnectionManager connMgr     = null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        ArrayList           list    = null;
        String  sql			= "";
        String  sql1			= "";
        int isOk            = 0;
        int isOk1           = 0;

        //String  v_grcode     = box.getString ("p_grcode");
        int v_seq			= box.getInt("p_seq");
        int v_sort			= box.getInt("p_sort");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            list = new ArrayList();

			sql  = " delete from TZ_LINKSITE ";
			sql += " where seq = ?"; //grcode = ? and 

			pstmt = connMgr.prepareStatement(sql);

			//pstmt.setString (1,  v_grcode);
			pstmt.setInt(1,  v_seq);

			isOk = pstmt.executeUpdate();
			
			if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }

			sql1  = " update TZ_LINKSITE set sort=sort-1 ";
			sql1 += " where sort > " +v_sort;

			pstmt = connMgr.prepareStatement(sql1);
			isOk1 = pstmt.executeUpdate();

			if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e1 ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e2 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }


}
