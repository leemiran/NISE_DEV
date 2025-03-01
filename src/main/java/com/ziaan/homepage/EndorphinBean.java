// **********************************************************
//  1. 제      목: Endorphin BEAN
//  2. 프로그램 명: EndorphinBean.java
//  3. 개      요: 엔돌핀관리 bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성: 
//  7. 수      정:
// **********************************************************
package com.ziaan.homepage;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;


public class EndorphinBean { 
    private ConfigSet config;
    private int row;

    public EndorphinBean() { 
        try { 
            config = new ConfigSet();
            row = Integer.parseInt(config.getProperty("page.manage.row") );        //        이 모듈의 페이지당 row 수를 셋팅한다
            // row = 100;
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }

    /**
    엔돌핀 목록
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList list(RequestBox box) throws Exception { 
       DBConnectionManager connMgr     = null;
       ListSet ls1			= null;
       ArrayList list1		= null;
       String sql1			= "";
       DataBox dbox1		= null;
 	   
	   sql1 = "select a.seq, a.content, a.luserid, a.ldate, decode(nvl(cnt, 0), 0, 'Y', 'N') delyn		\n"		
		   + "from TZ_ENDORPHIN_MASTER a left outer join (												\n"
		   + "    select seq, count(*) cnt																\n"
		   + "    from tz_endorphin																		\n"
		   + "    group by seq																			\n"
		   + ") b																						\n"
		   + "on a.seq = b.seq																			\n"
		   + "order by ldate desc																		\n";
	   
	   try { 
			connMgr = new DBConnectionManager();
			list1 = new ArrayList();

			ls1 = connMgr.executeQuery(sql1);
						 
			while ( ls1.next() ) { 
				dbox1 = ls1.getDataBox();
				list1.add(dbox1);
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
    엔돌핀 등록
    @param box      receive from the form object and session
    @return int		isOk
    */
    public int insert(RequestBox box) throws Exception { 
       DBConnectionManager connMgr	= null;
       PreparedStatement pstmt		= null;
       String sql= "",sql1 = "";
       ListSet             ls      = null;
       int isOk						= 0;
       int v_seq					= 0;
       String v_user_id				= box.getSession("userid");
       String v_content				= box.getString("p_content");			// 엔돌핀 내용

       try { 
           connMgr = new DBConnectionManager();
		   			
		   		sql = " select nvl(max(seq),0) +1 from tz_endorphin_master";
		   		ls = connMgr.executeQuery(sql);
		   		if ( ls.next() ) { 
		   				v_seq = ls.getInt(1);
		   		}
		   		ls.close();
		   		
           sql1  = "insert into TZ_ENDORPHIN_MASTER(seq,content,luserid,ldate) ";
		   		 sql1 += "values(?,?,?,to_char(sysdate, 'YYYYMMDDHH24MISS'))";	   
           pstmt = connMgr.prepareStatement(sql1);
           pstmt.setInt(1,v_seq);
           pstmt.setString(2,v_content);
           pstmt.setString(3,v_user_id);

		   isOk = pstmt.executeUpdate();

       }
       catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex, box, sql1);
           throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
       }
       finally { 
           if ( pstmt != null )   { try { pstmt.close(); } catch ( Exception e ) { } }
           if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
       }
       return isOk;
   }
   
   
    /**
    엔돌핀 등록
    @param box      receive from the form object and session
    @return int		isOk
    */
   public int update(RequestBox box) throws Exception { 
      DBConnectionManager connMgr     = null;
      PreparedStatement pstmt     = null;
      String sql1                 = "";
      int isOk                    = 0;
      String v_user_id			  = box.getSession("userid");				// 로그인 아이디

	  int v_seq					  = box.getInt("p_seq");					// 일련번호
      String v_content			  = box.getString("p_content" +v_seq);				// 엔돌핀내용


      try { 
          connMgr = new DBConnectionManager();

		  sql1  = "update TZ_ENDORPHIN_MASTER set content=?,luserid=?,ldate=to_char(sysdate, 'YYYYMMDDHH24MISS') ";
          sql1 += "where seq=? ";

          pstmt = connMgr.prepareStatement(sql1);

          pstmt.setString(1,v_content);
          pstmt.setString(2,v_user_id);
          pstmt.setInt(3,v_seq);

		  isOk = pstmt.executeUpdate();

      }
      catch ( Exception ex ) { 
          ErrorManager.getErrorStackTrace(ex, box, sql1);
          throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
      }
      finally { 
          if ( pstmt != null )  { try { pstmt.close(); } catch ( Exception e ) { } }
          if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
      }
      return isOk;
  }
  

    /**
    엔돌핀 등록
    @param box      receive from the form object and session
    @return int		isOk
    */
	  public int delete(RequestBox box) throws Exception { 
		 DBConnectionManager connMgr     = null;
		 PreparedStatement pstmt	 = null;
	     ListSet             ls      = null;
		 String sql1                 = "";
		 int isOk                    = 0;
		 int v_seq                   = box.getInt("p_seq");

		 try { 
			 connMgr = new DBConnectionManager();
			 
			 sql1 = "select count(*) cnt 											\n"
				 + "from tz_endorphin												\n"
				 + "where seq = " + SQLString.Format(v_seq) + "                 	\n";
			 
			ls = connMgr.executeQuery(sql1);
	   		if (ls.next() && ls.getInt("cnt") == 0) { 
	   			sql1  = "delete TZ_ENDORPHIN_MASTER where seq=?";
	   			pstmt = connMgr.prepareStatement(sql1);
	   			pstmt.setInt(1,v_seq);
	   			isOk = pstmt.executeUpdate();
	   		}
		 }
		 catch ( Exception ex ) { 
			 ErrorManager.getErrorStackTrace(ex, box, sql1);
			 throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
		 }
		 finally { 
			 if ( pstmt != null )   { try { pstmt.close(); } catch ( Exception e ) { } }
			 if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
		 }
		 return isOk;
	 }


    /**
    엔돌핀 목록
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectList(RequestBox box) throws Exception { 
       DBConnectionManager connMgr     = null;
       ListSet ls1			= null;
       ArrayList list1		= null;
       String sql1			= "";
       DataBox dbox1		= null;

	   String v_grcode		= box.getString("s_grcode");
 
       sql1  ="	select b.seq, b.content, grcode, usestartdt, useenddt, a.luserid, a.ldate";
       sql1 +="		from TZ_ENDORPHIN a, TZ_ENDORPHIN_MASTER b";
	     sql1 +="	where grcode( +) = '" +v_grcode + "'";
	     sql1 +="		and a.seq( +) = b.seq";
	     sql1 +="	order by grcode asc, ldate desc";

	   try { 
			connMgr = new DBConnectionManager();
			list1 = new ArrayList();

			ls1 = connMgr.executeQuery(sql1);
						 
			while ( ls1.next() ) { 
				dbox1 = ls1.getDataBox();
				list1.add(dbox1);
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
    엔돌핀 등록
    @param box      receive from the form object and session
    @return int		isOk
    */
    public int selectInsert(RequestBox box) throws Exception { 
       DBConnectionManager connMgr	= null;
       PreparedStatement pstmt		= null;
       PreparedStatement pstmt1		= null;
       String sql					= "";
       String sql1					= "";
       int isOk						= 0;
       int isOk1					= 0;

       String v_user_id				= box.getSession("userid");
       String v_grcode				= box.getString("s_grcode");			// 교육그룹
       String v_usestartdt			= (!box.getString("p_usestartdt").equals(""))?(box.getString("p_usestartdt")).replaceAll(".",""):"";	// 학습시작일
       String v_useenddt			= (!box.getString("p_useenddt").equals(""))?(box.getString("p_useenddt")).replaceAll(".",""):"";		// 학습종료일
       int	  v_seq					= 0;					// 번호

       Vector v_checks            = box.getVector("p_checks");
       Enumeration em             = v_checks.elements();

       try { 
           connMgr = new DBConnectionManager();

           sql  = "delete from TZ_ENDORPHIN";
		   sql += " where grcode=?";	   

           pstmt = connMgr.prepareStatement(sql);
           pstmt.setString(1,v_grcode);

		   isOk = pstmt.executeUpdate();

// 			if ( isOk == 1) { 

				while ( em.hasMoreElements() ) { 
				   v_seq   = Integer.parseInt((String)em.nextElement() );

				   sql1  = "insert into TZ_ENDORPHIN(grcode,seq,usestartdt,useenddt,luserid,ldate) ";
				   sql1 += "values(?,?,?,?,?,to_char(sysdate, 'YYYYMMDDHH24MISS'))";	   
				   
				   pstmt1 = connMgr.prepareStatement(sql1);
				   pstmt1.setString(1,v_grcode);
				   pstmt1.setInt(2,v_seq);
				   pstmt1.setString(3,v_usestartdt);
				   pstmt1.setString(4,v_useenddt);
				   pstmt1.setString(5,v_user_id);

				   isOk1 = pstmt1.executeUpdate();

				}
// 			}
       }
       catch ( Exception ex ) { 
           ErrorManager.getErrorStackTrace(ex, box, sql1);
           throw new Exception("sql1 = " + sql1 + "\r\n" + ex.getMessage() );
       }
       finally { 
           if ( pstmt != null )   { try { pstmt.close(); } catch ( Exception e ) { } }
           if ( pstmt1 != null )   { try { pstmt1.close(); } catch ( Exception e ) { } }
           if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
       }
       return isOk1;
   }


    /**
    엔돌핀 메인화면에 랜덤하게 보여주기 
    @param box      receive from the form object and session
    @return ArrayList
    */
    public ArrayList selectMain(RequestBox box) throws Exception { 
       DBConnectionManager connMgr     = null;
       ListSet ls1			= null;
       ArrayList list1		= null;
       String sql1			= "";
       DataBox dbox1		= null;

	   String v_grcode		= box.getSession("tem_grcode");
 
       sql1  ="	select content, xxx from";
       sql1 +="	(select ABS(DBMS_RANDOM.RANDOM) xxx, b.content from TZ_ENDORPHIN a, TZ_ENDORPHIN_MASTER b ";
	   sql1 +="	where grcode( +) = '" +v_grcode + "'";
	   sql1 +="	and a.seq = b.seq";
	   sql1 +="	order by xxx )";
	   sql1 +="where rownum = 1";

	   try { 
			connMgr = new DBConnectionManager();
			list1 = new ArrayList();

			ls1 = connMgr.executeQuery(sql1);
						 
			while ( ls1.next() ) { 
				dbox1 = ls1.getDataBox();
				list1.add(dbox1);
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



}