// **********************************************************
//  1. 제      목: 수강신청 제약 조건 Operation BEAN
//  2. 프로그램명: AcceptFileToDBBean.java
//  3. 개      요:
//  4. 환      경: JDK 1.3
//  5. 버      젼: 0.1
//  6. 작      성: 이창훈 2004. 11. 16
//  7. 수      정: 정경진 2005. 07. 18
// **********************************************************
package com.ziaan.propose;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;


public class AcceptFileToDBBean { 
    
    public AcceptFileToDBBean() { }

    /**
    수강신청 제약 목록 조회
    @param box          receive from the form object and session
    @return ArrayList   
    */        
    public ArrayList SelectBlackCondition(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList list = new ArrayList();
        String	sql1  = "";
        DataBox dbox= null;
        
        try { 
            sql1= "select notgraducnt "
            	+ "     , duemonth "
            	+ "     , userid "
            	+ "     , ldate "
            	+ "from   tz_strout_setup ";

			connMgr = new DBConnectionManager();
            ls = connMgr.executeQuery(sql1);

			while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            // if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

	/**
    수강신청 제약 정보 수정
	@param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
     public int updateBlackCodition(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
        
        String  s_userid = box.getSession("userid");
        String  v_unum = box.getString("unum");
        String  v_grcode = box.getString("grcode");
        String	v_gryear = box.getString("gryear");
        String	v_grseq = box.getString("grseq");
        String	v_conditioncode = box.getString("conditioncode");
        String  v_alertmsg = box.getString("alertmsg" +v_unum);

        try { 
            connMgr = new DBConnectionManager();

            sql =  " update TZ_BLACKCONDITION  set	\n";
            sql +=  "   alertmsg = ?,				\n";
			sql +=  "   luserid = ?,				\n";
			sql +=  "   ldate = sysdate			\n";
			sql +=  " where  grcode = ?			\n";
			sql +=  "   and  gryear = ?			\n";
			sql +=  "   and  grseq = ?			\n";
			sql +=  "   and  conditioncode = ?	\n";
           
			pstmt = connMgr.prepareStatement(sql);
			pstmt.setString( 1, v_alertmsg);
			pstmt.setString( 2, s_userid);
			pstmt.setString( 3, v_grcode);
			pstmt.setString( 4, v_gryear);
			pstmt.setString( 5, v_grseq);
			pstmt.setString( 6, v_conditioncode);
			
			isOk = pstmt.executeUpdate();
                
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }  


    /**
    수강신청 제약 정보 삭제
	@param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
     public int deleteBlackCodition(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
        
        String  v_grcode = box.getString("grcode");
        String	v_gryear = box.getString("gryear");
        String	v_grseq = box.getString("grseq");
        String	v_conditioncode = box.getString("conditioncode");

		try { 
            connMgr = new DBConnectionManager();

            sql =  " delete from TZ_BLACKCONDITION  \n";
			sql +=  " where  grcode = ?			\n";
			sql +=  "   and  gryear = ?			\n";
			sql +=  "   and  grseq = ?			\n";
			sql +=  "   and  conditioncode = ?	\n";
            
			pstmt = connMgr.prepareStatement(sql);
			pstmt.setString( 1, v_grcode);
			pstmt.setString( 2, v_gryear);
			pstmt.setString( 3, v_grseq);
			pstmt.setString( 4, v_conditioncode);
			
			isOk = pstmt.executeUpdate();
                
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }  

     

	/**
     수강신청 제약 정보 삭제
	 @param box      receive from the form object and session
     @return isOk    1:insert success,0:insert fail
    **/             
    public int deleteSubjseqStrOut(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq ) throws Exception { 
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int                 isOk    = 0;
         
        try { 
            sql  =  " delete from tz_strout     \n";
            sql +=  " where  year       = ?      \n";
            sql +=  " and    subj       = ?      \n";
            sql +=  " and    subjseq    = ?      \n";
             
            pstmt = connMgr.prepareStatement(sql);
            
            pstmt.setString( 1, p_year   );
            pstmt.setString( 2, p_subj   );
            pstmt.setString( 3, p_subjseq);
            
            isOk = pstmt.executeUpdate();
                 
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception(ex.getMessage() );
         } finally { 
             if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
         }

         return isOk;
     }
    
    
    
    /**
    수강신청 제약 정보 등록
   @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
   public int insertSubjseqStrOut(DBConnectionManager connMgr, RequestBox box, String p_userid, String p_name, String p_ldate, String p_year, String p_subj, String p_subjseq ) throws Exception { 
       PreparedStatement    pstmt           = null;
       ListSet              ls              = null;
       DataBox              dbox            = null;
       ArrayList            list            = new ArrayList();
       int                  iIdx            = 0;
       
       StringBuffer         sbSQL           = new StringBuffer("");
       int                  isOk            = 0;
       int                  v_cnt           = 0;
       int                  v_notgraducnt   = 0;
       int                  v_duemonth      = 0;
       String               v_isstrout      = "N";
       
       try {
           sbSQL.append(" select  count(*)  cnt                                             \n")
                .append(" from    tz_strout ts                                              \n")
                .append("     ,  (                                                          \n")
                .append("             select userid, max(ldate) ldate               		\n")
                .append("             from   tz_strout                                      \n")
                .append("             where  isstrout = 'Y'                                 \n")
                .append("             group  by userid                               		\n")
                .append("         )    v                                                    \n")
                .append(" where     ts.userid   =  " + SQLString.Format(p_userid) + "       \n")
                .append(" and       ts.userid   =  v.userid(+)                              \n")
                .append(" and       ts.ldate    >  nvl(v.ldate, '00010101')                 \n");
           
            ls  = connMgr.executeQuery(sbSQL.toString());

            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                
                v_cnt   = dbox.getInt("d_cnt");
            }
            
            list                    = this.SelectBlackCondition(box);
            
            for ( int i = 0 ; i < list.size() ; i++) {
                dbox                = (DataBox)list.get(0);
                
                v_notgraducnt               = dbox.getInt("d_notgraducnt");            
                v_duemonth                  = dbox.getInt("d_duemonth");
            }
            
            if ( (v_cnt + 1) >= v_notgraducnt )
                v_isstrout          = "Y";
            
           sbSQL.setLength(0);
           
           sbSQL.append(" insert into tz_strout                \n")
                .append(" (                                    \n")
                .append("         userid                       \n")
                .append("     ,   ldate                        \n")
                .append("     ,   subj                         \n")
                .append("     ,   year                         \n")
                .append("     ,   subjseq                      \n")
                .append("     ,   notgraducnt                  \n")
                .append("     ,   isstrout                     \n")
                .append(" )                                    \n")
                .append(" values                               \n")
                .append(" (                                    \n")
                .append("         ?                            \n")
                .append("     ,   ?                            \n")
                .append("     ,   ?                            \n")
                .append("     ,   ?                            \n")
                .append("     ,   ?                            \n")
                .append("     ,   1                            \n")
                .append("     ,   ?                            \n")
                .append(" )                                    \n");
           
           pstmt = connMgr.prepareStatement(sbSQL.toString());
           
           pstmt.setString( ++iIdx, p_userid    );
           pstmt.setString( ++iIdx, p_ldate     );
           pstmt.setString( ++iIdx, p_subj      );
           pstmt.setString( ++iIdx, p_year      );
           pstmt.setString( ++iIdx, p_subjseq   );
           pstmt.setString( ++iIdx, v_isstrout  );
           
           isOk = pstmt.executeUpdate();
                
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls    != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }       
    
    
    
    
     
     /**
     기간이 지난 수강신청 제약조건 삭제
    @param box      receive from the form object and session
     @return isOk    1:insert success,0:insert fail
     **/             
      public int deleteStroutProc(RequestBox box) throws Exception { 
         DBConnectionManager    connMgr     = null;
         PreparedStatement      pstmt       = null;
         PreparedStatement      pstmt1       = null;
         String                 sql         = "";
         int                    isOk        = 0;

         String                 v_grcode        = box.getStringDefault("s_grcode", "N000001");
         
        try {
            
             connMgr = new DBConnectionManager();
             connMgr.setAutoCommit(false);
             
             sql  = " delete from tz_strout a                                                                                                                                                            \n";
             sql += " where  exists (                                                                                                                                                                     \n";
             sql += "                 select ''                                                                                                                                                          \n";
             sql += "                 from   (                                                                                                                                                           \n";
             sql += "                         select userid, ldate                                                                                                                               \n";
             sql += "                         from   tz_strout ts                                                                                                                                         \n";
             sql += "                         where  exists   (                                                                                                                                           \n";
             sql += "                                           select ''                                                                                                                               \n";
             sql += "                                           from   tz_strout_setup       tss                                                                                                        \n";
             sql += "                                           where  to_char(sysdate, 'yyyymmdd') >= to_char(add_months(to_date(substr(ts.ldate, 1, 8), 'yyyymmdd'), tss.duemonth), 'yyyymmdd')       \n";
             sql += "                                           and    ts.isstrout      = 'Y'                                                                                                           \n";
             sql += "                                         )                                                                                                                                           \n";
             sql += "                        )    v                                                                                                                                                       \n";
             sql += "                 where  a.userid = v.userid                                                                                                                                        \n";
             sql += "                 and    a.ldate <= v.ldate                                                                                                                                         \n";
             sql += "               )                                                                                                                                                                     \n";
             
            pstmt = connMgr.prepareStatement(sql);
            
            isOk = pstmt.executeUpdate();
            
            // 1년단위로 초기화
            sql = "delete from tz_strout where substr(ldate,1,4) < to_char(sysdate,'yyyy')";
            pstmt1 = connMgr.prepareStatement(sql);
            isOk += pstmt1.executeUpdate();
            
            if(isOk > 0) {
            	connMgr.commit();
            } else {
            	connMgr.rollback();
            }
                 
         } catch ( Exception ex ) { 
             ErrorManager.getErrorStackTrace(ex);
             throw new Exception(ex.getMessage() );
         } finally { 
             if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
             if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
             if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
         }

         return isOk;
     }  
     

    /**
    수강신청 제약 정보 등록
	@param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
     public int insertBlackCodition(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String              sql     = "";
        String sql2 = "";
        int cnt = 99;
        int isOk = 0;
        
        String  s_userid = box.getSession("userid");
        String  v_grcode = box.getString("s_grcode");
        String	v_gryear = box.getString("s_gyear");
        String	v_grseq = box.getString("s_grseq");
        String	v_conditioncode = box.getString("p_conditioncode");
        String  v_alertmsg = box.getString("p_alertmsg");

        try { 
            connMgr = new DBConnectionManager();

            sql2 =  " select count(grcode) cnt from TZ_BLACKCONDITION			\n";
			sql2 +=  " where  grcode = " + SQLString.Format(v_grcode) +		"\n";
			sql2 +=  "   and  gryear = " + SQLString.Format(v_gryear) +		"\n";
			sql2 +=  "   and  grseq = " +SQLString.Format(v_grseq) +		"\n";
			sql2 +=  "   and  conditioncode =" +SQLString.Format(v_conditioncode) +	"\n";

            ls = connMgr.executeQuery(sql2);

			while ( ls.next() ) { 
                cnt = ls.getInt("cnt");
            }

			if ( cnt == 0) { 

				sql =  " insert into TZ_BLACKCONDITION		\n";
				sql +=  " values(?,?,?,?,?,?, sysdate)		\n";
				
				pstmt = connMgr.prepareStatement(sql);
				pstmt.setString( 1, v_grcode);
				pstmt.setString( 2, v_gryear);
				pstmt.setString( 3, v_grseq);
				pstmt.setString( 4, v_conditioncode);
				pstmt.setString( 5, v_alertmsg);
				pstmt.setString( 6, s_userid);
				
				isOk = pstmt.executeUpdate();
			} else { 
				isOk=99;
			}
                
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }  
    

    /**SelectBlackList
    수강신청 제약 정보 등록(삼진 아웃)
	@param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
     public int insertStrOutCodition(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String              sql     = "";
        String sql2 = "";
        int cnt	 = 0;
        int isOk = 0;
        String  v_sdate		= "";
        String  v_edate		= "";
        String  s_userid	= box.getSession("userid");
        String  v_grcode	= box.getString("s_grcode");
        int	v_notgraducnt	= box.getInt("p_notgraducnt");
        int	v_duemonth		= box.getInt("p_duemonth");
        
        String v_smonth 	= box.getString("p_smonth");	//기간 시작 월
        String v_sday		= box.getString("p_sday");	//기간 시작 일
        String v_emonth 	= box.getString("p_emonth");;	//기간 종료 월
        String v_eday		= box.getString("p_eday");	//기간 종료 일
        
        	   v_sdate		= v_smonth + v_sday;
        	   v_edate		= v_emonth + v_eday;
        	   
        
        try { 
            connMgr = new DBConnectionManager();

        	sql2  =  " select count(*) cnt from tz_strout_setup				 \n";

            ls = connMgr.executeQuery(sql2);

			while ( ls.next() ) { 
                cnt = ls.getInt("cnt");
            }
			ls.close();
			if ( cnt == 0) { 

				sql =  " insert into tz_strout_setup		\n";
				sql += "        ( notgraducnt, duemonth, userid, ldate ) \n";
				sql += " values ( ?, ?, ?, to_char(sysdate,'YYYYMMDDHH24MISS'))  \n";
				
				pstmt = connMgr.prepareStatement(sql);
				pstmt.setInt   ( 1, v_notgraducnt);
				pstmt.setInt   ( 2, v_duemonth);
				pstmt.setString( 3, s_userid);
				
				isOk = pstmt.executeUpdate();
				if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			} else { 
				sql =  " update tz_strout_setup		\n";
				sql += " set    notgraducnt = ?     \n";
				sql += "	  , duemonth = ?  		\n";
				sql += "	  , userid = ?  		\n";
				sql += "	  , ldate = to_char(sysdate,'yyyymmddhh24miss')	\n";
				
				pstmt = connMgr.prepareStatement(sql);

				pstmt.setInt   ( 1, v_notgraducnt);
				pstmt.setInt   ( 2, v_duemonth);
				pstmt.setString( 3, s_userid);
				
				isOk = pstmt.executeUpdate();
				if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			}
                
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
        	if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }      

    
    
    /**
    수강제약 대상자 조회
    @param box          receive from the form object and session
    @return ArrayList   
    */      
    public ArrayList SelectBlackList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	        = null;
        ListSet             ls              = null;
        ArrayList           list            = new ArrayList();
        String              sql1            = "";
        DataBox             dbox            = null;
        
        try { 
            this.deleteStroutProc(box);
            
            connMgr = new DBConnectionManager();
            list    = new ArrayList();
            dbox    = null;

            sql1  =" select tso.userid ";            
            sql1 +="      , get_name(tso.userid) as name ";            
            sql1 +="      , substr(tso.ldate, 1, 8) as ldate ";            
            sql1 +=" from   tz_strout  tso ";            
            sql1 +=" where  tso.isstrout = 'Y' ";
            sql1 +=" order  by get_name(tso.userid) ";
            
            ls = connMgr.executeQuery(sql1);
            
			while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            // if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
    수강제약 대상자 조회
    @param box          receive from the form object and session
    @return ArrayList   교육그룹리스트
    */      
    public DataBox SelectStrOutByUserId(RequestBox box, String v_userid) throws Exception { 
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list            = new ArrayList();
        StringBuffer        sbSQL           = new StringBuffer("");
        DataBox             dbox            = null;
        int                 v_notgraducnt   = 0;  
        int                 v_duemonth      = 0;
        
        try { 
            list                    = this.SelectBlackCondition(box);
            
            for ( int i = 0 ; i < list.size() ; i++) {
                dbox                = (DataBox)list.get(0);
                
                v_notgraducnt               = dbox.getInt("d_notgraducnt");            
                v_duemonth                  = dbox.getInt("d_duemonth");
            }
            
            this.deleteStroutProc(box);
            
            connMgr = new DBConnectionManager();
            
            list    = new ArrayList();
            dbox    = null;
            
            sbSQL.append(" select  tso.userid                                                                                                                   \n")
                 .append("     ,   max(get_name(tso.userid)) as    name                                                                                         \n")
                 .append("     ,   to_char(to_date(substr(max(tso.ldate), 1, 8), 'yyyymmdd'), 'yyyy.mm.dd')                                    ldate            \n")
                 .append("     ,   to_char(add_months(to_date(substr(max(tso.ldate), 1, 8), 'yyyymmdd'), " + v_duemonth + " ) + 1, 'yyyy.mm.dd')   freeldate    \n")
                 .append(" from    tz_strout  tso                                                                                                               \n")
                 .append(" where   tso.userid      = " + SQLString.Format(v_userid) + "                                                                         \n")
                 .append(" and     tso.isstrout    = 'Y'                                                                                                        \n")
                 .append(" group   by tso.userid                                                                                                                \n");
            
            ls = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sbSQL.toString());
            throw new Exception("sql = " + sbSQL.toString() + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return dbox;
    }    

    /**
    수강제약 대상자 삭제
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
	public int deleteBlackList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
        
        Vector v_check1     = new Vector();
        v_check1            = box.getVector("p_chk");
        Enumeration em1     = v_check1.elements();
        StringTokenizer st1 = null;
		String v_checks     = "";

        String  v_userid	= "";

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sql += " delete from tz_strout a                                                      \n";
            sql += " where  a.userid = ?                                                          \n";
            sql += " and    exists ( select ''                                                    \n";
            sql += "                 from   (                                                     \n";
            sql += "                          select userid, max(ldate) ldate                     \n";
            sql += "                          from   tz_strout ts                                 \n";
            sql += "                          where  ts.isstrout      = 'Y'                       \n";
            sql += "                          group  by ts.userid                                 \n";
            sql += "                        )  v                                                  \n";
            sql += "                 where  a.userid = v.userid                                   \n";
            sql += "                 and    a.ldate <= v.ldate                                    \n";
            sql += "               )                                                              \n";
            
            
            while ( em1.hasMoreElements() ) { 
                v_checks = (String)em1.nextElement();
                st1      = new StringTokenizer(v_checks,",");
                
                while ( st1.hasMoreElements() ) { 
                	v_userid   = (String)st1.nextToken();                    
                    pstmt = connMgr.prepareStatement(sql);
                	pstmt.setString( 1, v_userid);                    
                	isOk = pstmt.executeUpdate();
                    
                	pstmt.close();
                }
            }
            connMgr.commit();

        } catch ( Exception ex ) {
            try {
                connMgr.rollback();
            } catch ( Exception e ){}
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }  


    /**
    수강신청 대상자 등록 Insert
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
    public int insertBlackList(Hashtable data) throws Exception { 
        int isOk = 0;
        boolean v_CreateConnManager = false;
        boolean v_CreatePreparedStatement = false;

        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

		String v_grcode			= (String)data.get("grcode");
        String v_gryear			= (String)data.get("gryear");
        String v_grseq			= (String)data.get("grseq");
        String v_conditioncode	= (String)data.get("conditioncode");
        String v_gubun			= (String)data.get("gubun");
        String v_userid			= (String)data.get("userid");
        String v_luserid		= (String)data.get("luserid");

        // insert TZ_PROPOSE table
        sql =  "insert into tz_blacklist ( ";
        sql +=  " grcode, gryear, grseq, conditioncode, userid, gubun, luserid, ldate ) ";
        sql +=  " values ( ?, ?, ?, ?, ?, ?, ?, ? ) ";

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }
            pstmt = connMgr.prepareStatement(sql);
            v_CreatePreparedStatement = true;
           
                pstmt.setString(1,  v_grcode);
                pstmt.setString(2,  v_gryear);
                pstmt.setString(3,  v_grseq);
                pstmt.setString(4,  v_conditioncode);
                pstmt.setString(5,  v_userid);
                pstmt.setString(6,  v_gubun);
                pstmt.setString(7,  v_luserid);
                pstmt.setString(8, FormatDate.getDate("yyyyMMddHHmmss") ); // ldate             
           
                isOk = pstmt.executeUpdate();

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
        }

        return isOk;
    }


    /**
    수강제약 대상자 조회
    @param box          receive from the form object and session
    @return ArrayList   교육그룹리스트
    */      
    public String isExitBlack(RequestBox box, String v_userid) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql1  = "";
        DataBox dbox= null;
        
        String  v_grcode = box.getString("p_grcode");
        String  v_gryear = box.getString("p_gryear");
        String  v_grseq = box.getString("p_grseq");
        String  v_conditioncode = box.getString("p_conditioncode");
        String  v_errvalue = "0";
        
        try { 
            
            sql1 =" select				\n";
            sql1 +=" userid				\n";
            sql1 +=" from   tz_blacklist \n";
            sql1 +=" where				\n";
            sql1 +="   userid = '" +v_userid + "'";
            sql1 +="   and grcode = '" +v_grcode + "'";
            sql1 +="   and gryear = '" +v_gryear + "'";
            sql1 +="   and grseq = '" +v_grseq + "'";
            sql1 +="   and conditioncode = '" +v_conditioncode + "'";

			connMgr = new DBConnectionManager();
            ls = connMgr.executeQuery(sql1);

			if ( ls.next() ) { 
                v_errvalue = "10";
            }
			
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            // if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_errvalue;
    }


    /**
    회사별 수강제약 정보 조회
    @param box          receive from the form object and session
    @return ArrayList   교육그룹리스트
    */      
    public ArrayList SelectCompCondition(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList list = new ArrayList();
        String sql1  = "";
        DataBox dbox= null;
        
        String  v_comp = box.getString("s_company");
        
        try { 
            
            sql1 =" select				\n";
            sql1 +="   comp,				\n";
            sql1 +="   get_compnm(comp,2,2) companynm, \n";
            sql1 +="   duty,				\n";
            sql1 +="   language,			\n";
            sql1 +="	  allcondition,		\n";
            sql1 +="	  yearduty,			\n";
            sql1 +="	  yearlanguage		\n";
            sql1 +=" from				\n";
            sql1 +="   TZ_COMPCONDITION	\n";
            if ( !v_comp.equals("ALL") && ! v_comp.equals("")) sql1 +=" where	comp = '" +v_comp + "'";
            sql1 +="   order by comp		\n";
            
            connMgr = new DBConnectionManager();
            ls = connMgr.executeQuery(sql1);

			while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list.add(dbox);
            }

            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }



    /**
    회사별 수강제약 정보 수정
	@param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
     public int updateCompCodition(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
        
        String  s_userid = box.getSession("userid");
        String  v_unum = box.getString("unum");
        String  v_comp = box.getString("comp" +v_unum);
        int		v_duty = box.getInt("duty" +v_unum);
        int		v_language = box.getInt("language" +v_unum);
        int		v_allcondition = box.getInt("allcondition" +v_unum);
        int		v_yearduty = box.getInt("yearduty" +v_unum);
        int		v_yearlanguage = box.getInt("yearlanguage" +v_unum);

        try { 
            connMgr = new DBConnectionManager();

            sql =  " update TZ_COMPCONDITION  set \n";
            sql +=  "   duty =  ?,				\n";
			sql +=  "   language = ?,			\n";
			sql +=  "   allcondition = ?,		\n";
			sql +=  "   yearduty	= ?,			\n";
			sql +=  "   yearlanguage = ?,		\n";
			sql +=  "   luserid = ?,				\n";
			sql +=  "   ldate = sysdate			\n";
			sql +=  " where comp = ?				";
            
			pstmt = connMgr.prepareStatement(sql);
			pstmt.setInt( 1, v_duty);
			pstmt.setInt( 2, v_language);
			pstmt.setInt( 3, v_allcondition);
			pstmt.setInt( 4, v_yearduty);
			pstmt.setInt( 5, v_yearlanguage);
			pstmt.setString( 6, s_userid);
			pstmt.setString( 7, v_comp);
			
			isOk = pstmt.executeUpdate();
                
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }  


    /**
    회사별 수강제약 정보 삭제
	@param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
     public int deleteCompCodition(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        int isOk = 0;
        
        String  v_unum = box.getString("unum");
        String  v_comp = box.getString("comp" +v_unum);

        try { 
            connMgr = new DBConnectionManager();

            sql =  " delete from TZ_COMPCONDITION  \n";
			sql +=  " where comp = ?				";
            
			pstmt = connMgr.prepareStatement(sql);
			pstmt.setString(1, v_comp);
			
			isOk = pstmt.executeUpdate();
                
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }  



    /**
    회사별 수강제약 정보 등록
	@param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
     public int insertCompCodition(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        ListSet             ls      = null;
        String              sql     = "";
        String sql2 = "";
		int cnt = 0;
        int isOk = 0;
        
        String  s_userid = box.getSession("userid");
        String  v_comp = box.getString("s_company");
        int		v_duty = box.getInt("duty");
        int		v_language = box.getInt("language");
        int		v_allcondition = box.getInt("allcondition");
        int		v_yearduty = box.getInt("yearduty");
        int		v_yearlanguage = box.getInt("yearlanguage");

        try { 
            connMgr = new DBConnectionManager();

            sql2 =  " select count(comp) cnt from TZ_COMPCONDITION   \n";
            sql2 +=  " where comp = '" +v_comp + "'";

            ls = connMgr.executeQuery(sql2);

			while ( ls.next() ) { 
                cnt = ls.getInt("cnt");
            }

			if ( cnt == 0) { 

				sql =  " insert into TZ_COMPCONDITION   \n";
				sql +=  " values(?,?,?,?,?,?,?, sysdate)	\n";
				
				pstmt = connMgr.prepareStatement(sql);
				pstmt.setString( 1, v_comp);
				pstmt.setInt( 2, v_duty);
				pstmt.setInt( 3, v_language);
				pstmt.setInt( 4, v_allcondition);
				pstmt.setInt( 5, v_yearduty);
				pstmt.setInt( 6, v_yearlanguage);
				pstmt.setString( 7, s_userid);
				
				isOk = pstmt.executeUpdate();
				if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
			} else if ( cnt > 0) { 
				isOk = 99;
			}
                
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }   
    
  
    /**
    새로운 마스터과목코드 등록
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
     public int InsertMasterSubj(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;        

        PreparedStatement   pstmt   = null;   
        ListSet             ls      = null;     
        String              sql     = ""; String sql1 = "";
        String mastercd = "";
        int isOk = 0;   
        
        String v_grcode     = "";    
        String v_luserid    = "session"; // 세션변수에서 사용자 id를 가져온다.
        
        String  ss_grcode   = box.getString("s_grcode");
        String  ss_gyear    = box.getString("s_gyear");
        String  ss_grseq    = box.getString("s_grseq");
        
        try { 
            connMgr = new DBConnectionManager();
            
            //// MasterCode 생성
            sql1  =" select nvl(ltrim(rtrim(to_char(to_number(max(MASTERCD)) +1,'0000000'))),'0000001') MSTCD " ;
            sql1 += "From TZ_MASTERCD ";
                        
            ls = connMgr.executeQuery(sql1);
            if ( ls.next() ) { 
                mastercd = ls.getString("MSTCD");	
            }
            else{ 
                mastercd = "0000001";
            }
            
            //// insert TZ_EduGroup table
            sql =  "insert into TZ_MASTERCD(";
            sql +=  "grcode, gyear, grseq, ";
            sql +=  "mastercd, masternm,proposetype," ;
            sql +=  "isedutarget, userid, name,  ";
            sql +=  "luserid, ldate";
            sql +=  ")" ;
            sql +=  "values(";
            sql +=  "?,?,?, ";
            sql +=  "?,?,?,";
            sql +=  "?,?,?,";
            sql +=  "?,?)";

            // System.out.println("sql == > " + sql);
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1,  box.getString("s_grcode") );   
            pstmt.setString(2,  box.getString("s_gyear") );
            pstmt.setString(3,  box.getString("s_grseq") );
            pstmt.setString(4,  mastercd);
            pstmt.setString(5,  box.getString("p_masternm") );   
            pstmt.setString(6,  box.getString("p_proposetype") );    
            pstmt.setString(7,  box.getString("p_isedutarget") );
            pstmt.setString(8,  box.getString("s_userid") );
            pstmt.setString(9,  box.getString("s_name") );  
            pstmt.setString(10,  box.getSession("userid") );
            pstmt.setString(11, FormatDate.getDate("yyyyMMddHHmmss") );     
            
            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            isOk = 0;
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            connMgr.setAutoCommit(true);
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }  
    
      
    /**
    회사연결
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    **/             
     public int assignComp(DBConnectionManager connMgr, RequestBox box, String p_grcode) throws Exception { 

        PreparedStatement   pstmt   = null;   
        String              sql     = "";
        int isOk = 0;   
        
        String v_grcode     = p_grcode;    
        String v_luserid    = box.getSession("userid"); // 세션변수에서 사용자 id를 가져온다.
        
        try { 

            String v_codes    = box.getString("p_compTxt");
            StringTokenizer v_token = new StringTokenizer(v_codes, ";");
            String v_comp = "";
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
                        
            // TZ_GRcomp initialize 
            sql = " delete from tz_grcomp where grcode=?";
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, v_grcode);
            isOk = pstmt.executeUpdate();
			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }

            sql = " insert into tz_grcomp"
                + " (grcode, comp, indate, luserid, ldate) "
                + " values (?,?,to_char(sysdate,'YYYYMMDD'),?,to_char(sysdate,'YYYYMMDDHH24MISS'))";
                          
            while ( v_token.hasMoreTokens() ) { 
                v_comp = v_token.nextToken();
                // insert TZ_GRCOMP table
                pstmt = connMgr.prepareStatement(sql); 
                pstmt.setString(1, v_grcode);
                pstmt.setString(2, v_comp);
                pstmt.setString(3, v_luserid);
                isOk = pstmt.executeUpdate();
    			if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            }
        
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            connMgr.setAutoCommit(true);
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }    

    /**
    선택된 교육주관코드 수정
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail    
    **/
     public int UpdateEduGroup(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;        

        PreparedStatement   pstmt   = null;        
        String              sql     = "";
        int isOk = 0;   
        
        String v_grcode = box.getString("p_grcode");    
        String v_luserid      = "session"; // 세션변수에서 사용자 id를 가져온다.

        try { 
            connMgr = new DBConnectionManager();
            
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            connMgr.setAutoCommit(true);
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }   
    /**     
    코스리스트 
    @param box          receive from the form object and session
    @return ArrayList   코스 리스트
    */      
    public ArrayList TargetCourseList(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList list1 = null;
        String sql  = "";
        DataBox dbox= null;
        // SubjectInfoData data = null;

        try { 
            sql = "select course, coursenm, inuserid, indate, gradscore, gradfailcnt, luserid, ldate"
                + "  from tz_course order by coursenm"; 
            // System.out.println(sql);
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list1.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
    /**
    집합,사이버/대분류별 과목리스트 
    @param box          receive from the form object and session
    @return ArrayList   과목 리스트
    */
    public ArrayList TargetSubjectList(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList list1 = null;
        String  sql  = "";
        DataBox             dbox    = null;
        // SubjectInfoData data = null;

        String v_gubun      = box.getString("p_gubun");            
        String v_upperclass = box.getString("p_upperclass");      
        String v_grcode     = box.getString("s_grcode");
        String v_gyear      = box.getString("s_gyear");
        String v_grseq      = box.getString("s_grseq");

        if ( v_gubun.equals("")) v_gubun = "ALL";
        if ( v_upperclass.equals("")) v_upperclass = "ALL";

        try { 
            sql = "select a.subj, a.subjnm, a.isonoff, b.upperclass, b.classname "; 
            sql += "  from tz_subj     a, ";
            sql += "       tz_subjatt  b, ";
            sql += "       tz_grsubj   c  ";
            sql += " where a.subjclass = b.subjclass ";
            sql += " and  a.subj      = c.subjcourse";
            sql += " and c.grcode     = " +SQLString.Format(v_grcode);
            sql += " and a.isuse      = 'Y'";
                        
            if ( !v_gubun.equals("ALL"))
                sql += " and a.isonoff = " + SQLString.Format(v_gubun);
            if ( !v_upperclass.equals("ALL"))
                sql += " and a.upperclass = " + SQLString.Format(v_upperclass);
            sql += " order by a.subj ";
            System.out.println("sql_subjlist=" +sql);
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            ls = connMgr.executeQuery(sql);
            
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list1.add(dbox);
            }
            // System.out.println("sql_list=" +sql);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }




    /**
    마스터과목에 매핑된 과목/기수 리스트
    @param box          receive from the form object and session
    @return ArrayList   선택한 과목/기수 리스트
    */      
    public ArrayList SelectedList(RequestBox box) throws Exception { 
        ArrayList list1 = null;
        list1 = new ArrayList();
        // SubjectInfoData data = null;
        ProposeBean probean = new ProposeBean();
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        DataBox dbox= null;
            
        String v_subjectcodes    = box.getString("p_selectedcodes");
        String v_subjecttexts    = box.getString("p_selectedtexts");
        
        String v_grcode   = box.getString("s_grcode");
        String v_gyear    = box.getString("s_gyear");
        String v_grseq    = box.getString("s_grseq");
        String v_subj    = box.getString("s_grseq");
        String v_mastercd = box.getString("p_mastercd");
        
        
   
        try { 
            // String v_year    = probean.getSubjYear(v_grcode, v_gyear, v_grseq, v_subj, v_code);
            if ( !v_grcode.equals("") ) { 
                
                sql =  " select \n";
                sql += "  a.mastercd, \n";
                sql += "  a.masternm, \n";
                sql += "  a.subj,    \n";
                sql += "  a.year, \n";
                sql += "  a.subjseq, \n";
                sql += "  a.subjseqgr, \n";
                sql += "  a.subjnm, \n";
                sql += "  a.grcode, \n";
                sql += "  a.isedutarget, \n";
                sql += "  a.useproposeapproval, \n";
                sql += "  a.propstart, \n";
                sql += "  a.propend, \n";
                sql += "  a.edustart, \n";
                sql += "  a.eduend, \n";
                sql += "  (select count(*) from tz_edutarget y where a.mastercd=y.mastercd) as edutargetcnt,\n";
                sql += "  (select count(*) from tz_propose x where a.subj=x.subj and a.year=x.year and a.subjseq=x.subjseq) as prototcnt,      \n";
                sql += "  (select count(*) from tz_propose x where a.subj=x.subj and a.year=x.year and a.subjseq=x.subjseq and chkfinal = 'B') as finalbcnt,\n";
                sql += "  (select count(*) from tz_propose x where a.subj=x.subj and a.year=x.year and a.subjseq=x.subjseq and chkfinal = 'Y') as finalycnt,\n";
                sql += "  (select count(*) from tz_propose x where a.subj=x.subj and a.year=x.year and a.subjseq=x.subjseq and chkfinal = 'N') as finalncnt,\n";
                sql += "  (select count(*) from tz_propose x where a.subj=x.subj and a.year=x.year and a.subjseq=x.subjseq and isproposeapproval = 'N') as proncnt,\n";
                sql += "  (select count(*) from tz_propose x where a.subj=x.subj and a.year=x.year and a.subjseq=x.subjseq and isproposeapproval = 'Y') as proycnt,\n";
                sql += "  (select count(*) from tz_propose x where a.subj=x.subj and a.year=x.year and a.subjseq=x.subjseq and isproposeapproval = 'B') as probcnt,\n";
                sql += "  (select count(*) from tz_propose x where a.subj=x.subj and a.year=x.year and a.subjseq=x.subjseq and isproposeapproval = 'L') as prolcnt,\n";
                sql += "  (select count(*) from tz_propose x where a.subj=x.subj and a.year=x.year and a.subjseq=x.subjseq and isproposeapproval = 'L') as prolcnt\n";
                sql += " from \n";
                sql += "  VZ_MASTERSUBJSEQ a \n";
                sql += " where \n";
                sql += " mastercd =" +SQLString.Format(v_mastercd) + "\n";
                sql += " and grcode =" +SQLString.Format(v_grcode) + "\n";
                sql += " and grseq =" +SQLString.Format(v_grseq) + "\n";
                sql += " and gyear =" +SQLString.Format(v_gyear) + "\n";
                sql += " order by subj, subjseq";
                
                System.out.println("sql_list3=" +sql);
                connMgr = new DBConnectionManager();
                list1 = new ArrayList();
                ls = connMgr.executeQuery(sql);
                    while ( ls.next() ) { 
                	  dbox = ls.getDataBox();
                	  list1.add(dbox);
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }
    
    
    /**
    선택한 마스터과목정보
    @param box          receive from the form object and session
    @return ArrayList   선택한 마스터과목정보
    */
    public ArrayList SelectedMasterInfo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;        
        ListSet             ls      = null;
        ArrayList           list    = null;
        String              sql     = "";
        DataBox             dbox    = null;
        String v_grcode   = box.getString("s_grcode");
        String v_gyear    = box.getString("s_gyear");
        String v_grseq    = box.getString("s_grseq");
        // String v_subj    = box.getString("s_subj");
        String v_mastercd = box.getString("p_mastercd");
        
        try { 
            
            connMgr = new DBConnectionManager();            
            list = new ArrayList();

            sql  = " select ";
            sql += "   a.mastercd,a.grcode,a.gyear,a.grseq,a.mastercd,a.masternm,a.proposetype,a.isedutarget,a.userid,a.name, ";
            sql += "   (select count(userid) from tz_edutarget x where x.mastercd = a.mastercd) edutargetcnt ";
            sql += " from TZ_MASTERCD a";
            sql += " where ";
            sql += " a.mastercd =" +SQLString.Format(v_mastercd);
            sql += " and a.grcode =" +SQLString.Format(v_grcode);
            sql += " and a.grseq =" +SQLString.Format(v_grseq);
            sql += " and a.gyear =" +SQLString.Format(v_gyear);
            // System.out.print("sql_selectedMasterinfo=" +sql);
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
    과목/코스 지정정보 저장
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail    
    **/
     public int SaveAssign(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;        

        PreparedStatement   pstmt   = null;
        PreparedStatement pstmt1 = null;    
        ProposeBean probean = new ProposeBean();
        ListSet             ls      = null; 
        String              sql     = "";
        int isOk = 0;   
        
        String v_mastercd = box.getString("p_mastercd");    
        String v_grcode   = box.getString("s_grcode");    
        String v_gyear    = box.getString("s_gyear");
        String v_grseq    = box.getString("s_grseq");    
        String v_subj     = box.getString("p_subjects");    
        String v_luserid  = box.getSession("userid"); // 세션변수에서 사용자 id를 가져온다.
        
        String v_year     = "";
        // v_year = "2004";

        try { 

            String v_codes    = box.getString("p_selectedcodes");
            System.out.println("v_codesssss=" +v_codes);
            StringTokenizer v_token = new StringTokenizer(v_codes, ";");
            String v_code = "";
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            //// TZ_GRSUBJ initialize (tz_subjseq에 존재하는 경우 삭제불가) 
            // sql = " delete from tz_mastersubj where grcode=? and mastercd = ? and gyear = ?";
                // + "    and (subjcourse not in (select distinct subj from tz_subjseq where grcode=?))"
                // + "    and (subjcourse not in (select distinct course from tz_courseseq where grcode=?))";
            // System.out.println("gyear=" +v_gyear);
            // System.out.println("sql_delete=" +sql);
            // pstmt1 = connMgr.prepareStatement(sql);
            // pstmt1.setString(1, v_grcode);
            // pstmt1.setString(2, v_mastercd);
            // pstmt1.setString(3, v_gyear);
            // pstmt1.setString(3, v_grcode);
            // isOk = pstmt1.executeUpdate();
            isOk = 1;
                    
            while ( v_token.hasMoreTokens() ) { 
                v_code = v_token.nextToken();
                // System.out.println("parameter ==  ==  ==  ==  == > " +v_grcode + "," +v_gyear + "," +v_grseq + "," +v_subj + "," +v_code);
                v_year     = probean.getSubjYear(v_grcode, v_gyear, v_grseq, v_subj, v_code);
                // check exists in TZ_GRSUBJ
                sql = " select decode(count(*),0,'N','Y') isExist from tz_mastersubj " 
                    + " where mastercd = '" +v_mastercd + "' "///and grcode='" +v_grcode + "' and grseq = '" +v_grseq + "' and gyear = '" +v_gyear + "' "
                    + " and subjcourse='" +v_subj + "' and subjseq=rtrim('" +v_code + "') and year =" +SQLString.Format(v_year);
                
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
                ls = connMgr.executeQuery(sql);
                ls.next();
                
                if ( ls.getString("isExist").equals("N") ) { 
                    // insert TZ_GRSUBJ table
                    sql = " insert into tz_mastersubj"
                        + " (mastercd, subjcourse, subjseq, year, "
                        + "  grcode, grseq, gyear, "
                        + " grpcode, grpname, luserid, ldate) "
                        + " values (?, ?, ?, ?, "
                        + " ?, ?, ?,  "
                        + " 'N',0,?,to_char(sysdate,'YYYYMMDDHH24MISS'))";

                    pstmt = connMgr.prepareStatement(sql);
                    pstmt.setString(1, v_mastercd);
                    pstmt.setString(2, v_subj);
                    pstmt.setString(3, v_code);
                    pstmt.setString(4, v_year);
                    pstmt.setString(5, v_grcode);
                    pstmt.setString(6, v_grseq);
                    pstmt.setString(7, v_gyear);
                    pstmt.setString(8, v_luserid);
                    isOk = pstmt.executeUpdate();

                    // System.out.println("v_mastercd=" +v_mastercd);
                    // System.out.println("v_mastercd=" +v_grcode);
                    // System.out.println("v_mastercd=" +v_code);
                    // System.out.println("v_mastercd=" +v_gyear);
                    // System.out.println("v_mastercd=" +v_luserid);
                }
            
            }
        
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            connMgr.setAutoCommit(true);
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }       
    
    /**
    마스터과목 삭제
    @param box      receive from the form object and session
    @return isOk    1:delete success,0:delete fail    */
                
    public int DeleteMasterSubj(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;        
        PreparedStatement pstmt1 = null;        
        String sql1 = "";
        int    isOk1 = 0;
        PreparedStatement pstmt2 = null;        
        String sql2 = "";
        int    isOk2 = 0;   
        
        PreparedStatement pstmt3 = null;
        String sql3 = "";
        
        PreparedStatement pstmt4 = null;        
        String sql4 = "";
        int    isOk3 = 0;   
        
        PreparedStatement pstmt5 = null;
        String sql5 = "";
        
        ListSet             ls      = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        String v_mastercd  = box.getString("p_mastercd");
        String v_grcode    = box.getString("s_grcode");
        String v_gyear     = box.getString("s_gyear");
        String v_grseq     = box.getString("s_grseq");
        
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);  
            
            // delete TZ_MASTERCD table
            sql1 = "delete from TZ_MASTERCD where grcode= ? and grseq= ? and gyear= ? and mastercd= ? ";
            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_grcode);
            pstmt1.setString(2, v_grseq);
            pstmt1.setString(3, v_gyear);
            pstmt1.setString(4, v_mastercd);
            isOk1 = pstmt1.executeUpdate(); 
            
            // delete TZ_MASTERSUBJ table
            sql2  = "select count(mastercd) CNTS from TZ_MASTERSUBJ ";
            sql2 += " where grcode=" +SQLString.Format(v_grcode);
            sql2 += " and mastercd=" +SQLString.Format(v_mastercd);
            sql2 += " and gyear=" +SQLString.Format(v_gyear);
            // System.out.println("sql2_delete ==  == = > " +sql2);
            pstmt2 = connMgr.prepareStatement(sql2);
            rs1 = pstmt2.executeQuery();
            rs1.next();
            
            if ( rs1.getInt("CNTS") > 0) { 
              sql3 = "delete from TZ_MASTERSUBJ where grcode= ? and mastercd= ? and gyear= ?";
              pstmt3 = connMgr.prepareStatement(sql3);
              pstmt3.setString(1, v_grcode);
              pstmt3.setString(2, v_mastercd);
              pstmt3.setString(3, v_gyear);
              isOk2 = pstmt3.executeUpdate(); 
              // System.out.println("sql3_delete=" +sql3);
             // System.out.println("isok2=" +isOk2);
            } 
            else{ 
              isOk2 = 1; 
            }
            
            // delete TZ_EDUTARGET table
            sql4  = "select count(userid) CNTS from TZ_EDUTARGET ";
            sql4 += " where ";
            sql4 += " mastercd=" +SQLString.Format(v_mastercd);
            pstmt4 = connMgr.prepareStatement(sql4);
            rs2 = pstmt4.executeQuery();
            rs2.next();
            
            // System.out.println("v_grcode=" +v_grcode);
            // System.out.println("v_mastercd=" +v_mastercd);
            // System.out.println("v_gyear=" +v_gyear);
            // System.out.println("rs1.getInt(CNTS)=" +rs1.getInt("CNTS") );
            
            
            if ( rs2.getInt("CNTS") > 0) { 
              sql5 = "delete from TZ_EDUTARGET where mastercd= ? ";
              pstmt5 = connMgr.prepareStatement(sql5);
              pstmt5.setString(1, v_mastercd);
              isOk3 = pstmt5.executeUpdate(); 
            } 
            else{ 
              isOk3 = 1; 
            }
            
            
            
            
            if ( isOk1 > 0 && isOk2 > 0 && isOk3 > 0) connMgr.commit();         //      2가지 sql 이 꼭 같이 insert 되어야 하는 경우이므로  
            else connMgr.rollback();
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql2 = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            connMgr.setAutoCommit(true);
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }                 
            if ( pstmt3 != null ) { try { pstmt3.close(); } catch ( Exception e1 ) { } }
            if ( pstmt4 != null ) { try { pstmt4.close(); } catch ( Exception e1 ) { } }
            if ( pstmt5 != null ) { try { pstmt5.close(); } catch ( Exception e1 ) { } }
            if ( rs1 != null )    { try { rs1.close(); } catch ( Exception e ) { } }
            if ( rs2 != null )    { try { rs2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1*isOk2;
    }   
    
    
    /**
    마스터과목/기수 정보 삭제
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail    
    **/
     public int CancelMasterSubj(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;        

        PreparedStatement   pstmt   = null;
        PreparedStatement pstmt1 = null;    
        ProposeBean probean = new ProposeBean();
        ListSet             ls      = null; 
        String              sql     = "";
        int isOk = 0;   
        
        String v_mastercd = box.getString("p_mastercd");    
        String v_grcode   = box.getString("s_grcode");    
        String v_gyear    = box.getString("s_gyear");
        String v_grseq    = box.getString("s_grseq");    
        String v_luserid  = box.getSession("userid"); // 세션변수에서 사용자 id를 가져온다.
        String v_year     = "";
        String v_subj     = "";
        String v_subjseq  = "";
        
        
        Vector v_check1     = new Vector();
        v_check1            = box.getVector("p_checks"); // subj,subjseq
        Enumeration em1     = v_check1.elements();
        StringTokenizer st1 = null;
        String v_checks     = "";

        try { 
        	
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            while ( em1.hasMoreElements() ) { 
            	v_checks    = (String)em1.nextElement();
            	st1      = new StringTokenizer(v_checks,",");
            	while ( st1.hasMoreElements() ) { 
                    v_subj      = (String)st1.nextToken();
                    v_subjseq   = (String)st1.nextToken();
                    // System.out.println("v_subj=" +v_subj);
                    // System.out.println("v_subjseq=" +v_subjseq);
                    break;
                }
                v_year     = probean.getSubjYear(v_grcode, v_gyear, v_grseq, v_subj, v_subjseq);
                
            sql = " delete from tz_mastersubj where mastercd = ? and subjcourse=? and subjseq=? and year=?";
                // + "    and (subjcourse not in (select distinct subj from tz_subjseq where grcode=?))"
                // + "    and (subjcourse not in (select distinct course from tz_courseseq where grcode=?))";
            pstmt1 = connMgr.prepareStatement(sql);
            pstmt1.setString(1, v_mastercd);
            pstmt1.setString(2, v_subj);
            pstmt1.setString(3, v_subjseq);
            pstmt1.setString(4, v_year);
            isOk = pstmt1.executeUpdate();
            }            
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e ) { } }
            connMgr.setAutoCommit(true);
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }       
    
    
    /**     
    마스터과목 정보 
    @param box          receive from the form object and session
    @return ArrayList   코스 리스트
    */      
    public ArrayList SelectMasterInfo(RequestBox box) throws Exception {               
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        ArrayList list1 = null;
        String sql  = "";
        DataBox dbox= null;
        
        String v_mastercd = box.getString("s_mastercd");    
        String v_grcode   = box.getString("s_grcode");    
        String v_gyear    = box.getString("s_gyear");
        String v_grseq    = box.getString("s_grseq");    
        
        // SubjectInfoData data = null;

        try { 
            sql = "select masternm, proposetype, isedutarget ";
            sql += "  from TZ_MASTERCD "; 
            sql += "where "; 
            sql += " mastercd = " +SQLString.Format(v_mastercd); 
            sql += " and grcode= " +SQLString.Format(v_grcode);
            sql += " and gyear= " +SQLString.Format(v_gyear);
            sql += " and grseq= " +SQLString.Format(v_grseq);
            System.out.println("kdskldsjkldsfjklfjklsjkldsfjklfdsjklfdsjklfjkldsjklds" +sql);
            connMgr = new DBConnectionManager();
            list1 = new ArrayList();
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                dbox = ls.getDataBox();
                list1.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list1;
    }   
    
     //// //// //// //// //// //// //// //// //// //// //// ///결재테이블 존재개수//// //// //// //// //// //// //// //// //// //// //// //// //// // 
    public String getMasterPropType(String p_subj, String p_year, String p_subjseq) throws Exception { 
        DBConnectionManager	connMgr	= null;
        String v_proptypenm = "";
        try { 
            connMgr = new DBConnectionManager();
            v_proptypenm = getMasterPropType(connMgr, p_subj, p_year, p_subjseq);
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return v_proptypenm;
    }
    
    public String getMasterPropType(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq) throws Exception { 
        ListSet             ls      = null;
        String v_proptypenm = "";

        String sql  = "";

        try { 
        	sql  = " select b.codenm from vz_mastersubjseq a, tz_code b \n";
        	sql += " where a.subj  = " + SQLString.Format(p_subj);
        	sql += " and a.year    = " + SQLString.Format(p_year);
        	sql += " and a.subjseq = " + SQLString.Format(p_subjseq);
        	sql += " and b.gubun = '0019'" ;
        	sql += " and b.code = a.proposetype";
        	// sql += " and gubun   = " + SQLString.Format(p_gubun);
        	// sql += " and userid  = " + SQLString.Format(p_userid);
            // System.out.println(sql);
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                v_proptypenm = ls.getString("codenm");
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_proptypenm;
    }
    //// //// //// //// //// //// //// //// //// //// //// ///결재승인상태 리턴끝//// //// //// //// //// //// //// //// //// //// //// //// //// // 
    
    
    /**
    선택된 코스코드 삭제
    @param box      receive from the form object and session
    @return isOk    1:delete success,0:delete fail    
                
    public int DeleteEduGroup(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;        
        PreparedStatement pstmt1 = null;        
        String sql1 = "";
        int    isOk1 = 0;   
        PreparedStatement pstmt2 = null;        
        String sql2 = "";
        int    isOk2 = 0;   

        String v_EduGroup  = box.getString("p_EduGroup");
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);  
            
            // delete TZ_EduGroup table
            sql1 = "delete from TZ_EduGroup where EduGroup = ? ";
            pstmt1 = connMgr.prepareStatement(sql1);
            pstmt1.setString(1, v_EduGroup);
            isOk1 = pstmt1.executeUpdate(); 

            // delete TZ_EduGroupSUBJ table
            sql2 = "delete from TZ_EduGroupSUBJ where EduGroup = ? ";
            pstmt2 = connMgr.prepareStatement(sql2);
            pstmt2.setString(1, v_EduGroup);
            isOk2 = pstmt2.executeUpdate(); 

            if ( isOk1 > 0 && isOk2 > 0) connMgr.commit();         //      2가지 sql 이 꼭 같이 insert 되어야 하는 경우이므로  
            else connMgr.rollback();
        } catch ( Exception ex ) { 
            connMgr.rollback();
            ErrorManager.getErrorStackTrace(ex, box, sql2);
            throw new Exception("sql2 = " + sql2 + "\r\n" + ex.getMessage() );
        } finally { 
            connMgr.setAutoCommit(true);
            if ( pstmt1 != null ) { try { pstmt1.close(); } catch ( Exception e1 ) { } }
            if ( pstmt2 != null ) { try { pstmt2.close(); } catch ( Exception e1 ) { } }                 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk1*isOk2;
    }   
    */
}
